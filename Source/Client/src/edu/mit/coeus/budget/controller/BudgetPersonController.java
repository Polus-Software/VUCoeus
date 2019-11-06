/*
 * BudgetPersonController.java
 *
 * Created on October 7, 2003, 2:45 PM
 */

/* PMD check performed, and commented unused imports and variables on 01-MAR-2011
 * by Maharaja Palanichamy
 */

package edu.mit.coeus.budget.controller;
/** Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */
import edu.mit.coeus.budget.gui.BudgetPersonsForm;
import edu.mit.coeus.gui.CoeusAppletMDIForm;
import edu.mit.coeus.gui.CoeusFontFactory;
//import edu.mit.coeus.utils.CoeusGuiConstants;
//import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.exception.CoeusUIException;
import edu.mit.coeus.bean.CoeusBean;
import edu.mit.coeus.budget.bean.*;
import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.budget.gui.AppointmentsForPersonForm;
import edu.mit.coeus.gui.CoeusDlgWindow;
import edu.mit.coeus.utils.*;
import edu.mit.coeus.utils.query.*;
import edu.vanderbilt.coeus.gui.PersonTableCellRenderer;
import edu.mit.coeus.budget.gui.AppointmentDetailForm;
import edu.mit.coeus.irb.bean.PersonInfoFormBean;

import java.util.*;

import javax.swing.table.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import edu.mit.coeus.search.gui.*;

//import javax.swing.table.TableCellEditor;
import java.awt.event.*;
import java.awt.*;
import java.text.ParseException;


/**
 *
 * @author  chandrashekara
 */
public class BudgetPersonController extends Controller implements ActionListener{//, MouseListener  {
    // Table Model bean
    private Vector vecDataBean;
    CoeusVector vecSearchPerson;
    CoeusVector vecAppDetails;
    RequesterBean requester;
    ResponderBean responder;
    String mesg = null;
    private CoeusDlgWindow dlgPersonBudget;
    private AppointmentsForPersonForm appointmentsForPersonForm;
    
    BudgetPersonCellEditor budgetPersonCellEditor;
    BudgetPersonRenderer budgetPersonRenderer;
    BudgetPersonsForm budgetPersonsForm;
    BudgetPersonTableModel budgetPersonTableModel;
    private DateUtils dtUtils = new DateUtils();
    private java.text.SimpleDateFormat dtFormat
    = new java.text.SimpleDateFormat("MM/dd/yyyy");
    
    /** A collection Appointment Types */
    //Commented for Case 3869 - Save not working for budget person - start
//    private String appointments[]={"9M DURATION","10M DURATION","11M DURATION",
//    "12M DURATION","REG EMPLOYEE","SUM EMPLOYEE","TMP EMPLOYEE"};
    //Commented for Case 3869 - Save not working for budget person - end
    
    /** Bean contains the BudgetPersons information
     */
    BudgetPersonsBean budgetPersonsBean;
    /** hold the information about the Budget Persons
     */
    Vector vecBudgetPersonInfo;
    
    String proposalId;
    /** this contains the Coues message resources instance for parsing the messages
     */
    CoeusMessageResources coeusMessageResources = CoeusMessageResources.getInstance();
    
    /** this variable tells whether this window modified or not
     */
    private boolean modified = false;
    
    private CoeusAppletMDIForm mdiForm;
    
    private BudgetInfoBean budgetInfoBean;
    private Date budgetStartDate;
    private String personName;
    private String personId;
    /** Specifies the window Type */
    //    private boolean modal;
    private String proposalNumber;
    private int versionNumber;
    private String key;
    private NotEquals neq;
    private Equals eq;
    private Or or;
    /** Specifies the budget Start Date */
    private CoeusVector vecBudgetDate;
    /** Specifies the deleted persons information */
    private Vector vecDeletedPersons=null;
    /** Specifies the Persons Information */
    private CoeusVector vecBudgetPersons;
    private int rowID = 1;
    private boolean isInHierarchy;
    private char funType;
    private CoeusVector cvDataInNewMode;
    //    private boolean dataFromProp;
    //Added for Case 3869 - Save not working for budget person - start
    // Modified for COEUSQA-3709 : Coeus4.5: Budget Dropdown Issue -Start
    private Vector vecAppointmentTypes,vecActiveAppointmentTypes;
    // Modified for COEUSQA-3709 : Coeus4.5: Budget Dropdown Issue -End
    //COEUSQA-2036 Code Table Prop Dev Appt Type and Period Types - Start
    // JM 7-12-2012 12M DURATION is INACTIVE so setting to 12M EMPLOYEE
    //private final String REG_EMPLOYEE = "REG_EMPLOYEE";
   // private final String REG_EMPLOYEE = "12M DURATION";
    private final String REG_EMPLOYEE = "12M EMPLOYEE";
    //COEUSQA-2036 Code Table Prop Dev Appt Type and Period Types - End
    //Added for Case 3869 - Save not working for budget person - end
    //Added for Case#2918 - Use of Salary Anniversary Date for calculating inflation in budget development module
    private boolean showSalaryAnniversaryDate = false;
    //  4493: While adding a TBA appointment type should be defaulted to 12 Months - Start
    private String defaultTbaApntmntTypeCode;
    private String defaultTbaApntmntTypeDesc;
    // 4493: While adding a TBA appointment type should be defaulted to 12 Months - End
    //COEUSQA-1535-Access to institutionally maintained salaries in proposal budget - Start
    private static final String HIERARCHY_SERVLET = "/ProposalHierarchyServlet";
    private static final char CHECK_VIEW_INSTITUTIONAL_SALARIES_RIGHT = 'U';
    //COEUSQA-1535-Access to institutionally maintained salaries in proposal budget - End
    
    //COEUSQA-2036 Code Table Prop Dev Appt Type and Period Types - Start
    private final char GET_APPOINTMENT_TYPE ='1';
    private final char GET_ACTIVE_APPOINTMENT_TYPE ='2';
    //COEUSQA-2036 Code Table Prop Dev Appt Type and Period Types - End
    
    //COEUSQA-1559 Display calculated base salary amount on RR Budget form in out years - Start
    /** Specifies the Budget Period Information */
    public CoeusVector vecBudgetPeriods;    
    private final char CALCULATE_BASE_SALARY_FOR_PERIOD = '5';
    private static final char GET_BUDGET_PERIOD = 'i';
    private static final char GET_PARENT_DATA = 'A';
    
    /* JM 4-4-2016 external persons col */
    private static final int IS_EXTERNAL_PERSON_COL = 6;
    /* JM END */
    
    /* JM 4-4-2016 incremented indices to account for new external persons col
    private static final int BASE_SALARY_P1_COLUMN = 7;
    private static final int BASE_SALARY_P2_COLUMN = 8;
    private static final int BASE_SALARY_P3_COLUMN = 9;
    private static final int BASE_SALARY_P4_COLUMN = 10;
    private static final int BASE_SALARY_P5_COLUMN = 11;
    private static final int BASE_SALARY_P6_COLUMN = 12;
    private static final int BASE_SALARY_P7_COLUMN = 13;
    private static final int BASE_SALARY_P8_COLUMN = 14;
    private static final int BASE_SALARY_P9_COLUMN = 15;
    private static final int BASE_SALARY_P10_COLUMN = 16; 
    */
    private static final int BASE_SALARY_P1_COLUMN = 8;
    private static final int BASE_SALARY_P2_COLUMN = 9;
    private static final int BASE_SALARY_P3_COLUMN = 10;
    private static final int BASE_SALARY_P4_COLUMN = 11;
    private static final int BASE_SALARY_P5_COLUMN = 12;
    private static final int BASE_SALARY_P6_COLUMN = 13;
    private static final int BASE_SALARY_P7_COLUMN = 14;
    private static final int BASE_SALARY_P8_COLUMN = 15;
    private static final int BASE_SALARY_P9_COLUMN = 16;
    private static final int BASE_SALARY_P10_COLUMN = 17; 
    /* JM END */
    //COEUSQA-1559 Display calculated base salary amount on RR Budget form in out years - End
    
    /** Creates a new instance of BudgetPersonController
     * @param mdiForm
     * @param modal
     * @param budgetInfoBean
     */
    
    //    public BudgetPersonController(CoeusAppletMDIForm mdiForm,
    //    BudgetInfoBean budgetInfoBean,char functionType,CoeusVector cvDataInNewMode) {
    public BudgetPersonController(CoeusAppletMDIForm mdiForm,BudgetInfoBean budgetInfoBean) {
        this.mdiForm = mdiForm;
        this.budgetPersonsBean = budgetPersonsBean;
        this.budgetInfoBean = budgetInfoBean;
        proposalNumber = budgetInfoBean.getProposalNumber();
        versionNumber  = budgetInfoBean.getVersionNumber();
        budgetPersonsBean = new BudgetPersonsBean();
        budgetPersonsBean.setProposalNumber(budgetInfoBean.getProposalNumber());
        budgetPersonsBean.setVersionNumber(budgetInfoBean.getVersionNumber());
        
        personName = budgetPersonsBean.getFullName();
        personId = budgetPersonsBean.getPersonId();
        budgetPersonsBean.setFullName(personName);
        budgetPersonsBean.setPersonId(personId);
        try{
            key = (getProposalNumber()+getVersionNumber());
            vecBudgetPersons = new CoeusVector();
            vecDeletedPersons = new Vector();
            
            vecBudgetPersons = QueryEngine.getInstance().executeQuery(key, BudgetPersonsBean.class, CoeusVector.FILTER_ACTIVE_BEANS);
            //Added for Case#2918 - Use of Salary Anniversary Date for calculating inflation in budget development module - starts
            Hashtable htData =  QueryEngine.getInstance().getDataCollection(key);
            if(htData != null){
                Integer enableSalaryAnnivDate = (Integer) htData.get("ENABLE_SALARY_INFLATION_ANNIV_DATE");
                if(enableSalaryAnnivDate != null && enableSalaryAnnivDate.intValue() == 1){
                    showSalaryAnniversaryDate = true;
                }
                // 4493: While adding a TBA appointment type should be defaulted to 12 Months
                defaultTbaApntmntTypeCode = (String) htData.get(CoeusConstants.DEFAULT_TBA_APPOINTMENT_TYPE_CODE);
            }
            //Added for Case#2918 - Use of Salary Anniversary Date for calculating inflation in budget development module - ends
            //vecBudgetPersons =
            //Set the max rowID
            setMaxRowID();
            
            vecBudgetDate = new CoeusVector();
            vecBudgetDate = QueryEngine.getInstance().executeQuery(key,budgetInfoBean);
            // Null Pointer Exception throws for a Parent Proposal
            if(vecBudgetDate != null && vecBudgetDate.size() >0){
                budgetInfoBean = (BudgetInfoBean)vecBudgetDate.get(0);
                budgetStartDate = budgetInfoBean.getStartDate();
            }
            //Added for Case 3869 - Save not working for budget person - start
            fetchAppointmentTypes();
            // Added for COEUSQA-3709 : Coeus4.5: Budget Dropdown Issue - Start
            vecActiveAppointmentTypes = fetchActiveAppointmentTypes();
            // Added for COEUSQA-3709 : Coeus4.5: Budget Dropdown Issue - End
            //Added for Case 3869 - Save not working for budget person - end
            // 4493: While adding a TBA appointment type should be defaulted to 12 Months
            //COEUSQA-1559 Display calculated base salary amount on RR Budget form in out years - Start
            vecBudgetPeriods = new CoeusVector();            
            //budget period details
            vecBudgetPeriods = QueryEngine.getInstance().executeQuery(key,BudgetPeriodBean.class, CoeusVector.FILTER_ACTIVE_BEANS);            
            //COEUSQA-1559 Display calculated base salary amount on RR Budget form in out years - End
            validateDefaultTBAAppointmentType();
            setFormData(vecBudgetPersons);
        }catch(Exception Exception){
            Exception.getMessage();
        }
        budgetPersonsForm = new BudgetPersonsForm();
        budgetPersonsForm.lblProposalNumbervalue.setText(proposalNumber);
        budgetPersonsForm.lblProposalNumbervalue.setFont(CoeusFontFactory.getLabelFont());
        budgetPersonsForm.lblVersionvalue.setText(""+versionNumber);
        budgetPersonsForm.lblVersionvalue.setFont(CoeusFontFactory.getLabelFont());
        if(getFunctionType()==TypeConstants.DISPLAY_MODE){
            budgetPersonsForm.btnCancel.requestFocus();
        }
        registerComponents();
        setTableKeyTraversal();
        
        //COEUSQA-1559 Display calculated base salary amount on RR Budget form in out years - Start
        //budgetPersonTableModel = new BudgetPersonTableModel();
        budgetPersonTableModel = new BudgetPersonTableModel(proposalNumber,versionNumber);
        //COEUSQA-1559 Display calculated base salary amount on RR Budget form in out years - End
        
        budgetPersonsForm.tblBudgetPersons.setModel(budgetPersonTableModel);
        budgetPersonCellEditor  = new BudgetPersonCellEditor();
        budgetPersonRenderer = new BudgetPersonRenderer();
//        System.out.println("The Function Type is : "+funType);
        if(funType==TypeConstants.ADD_MODE){
            if(vecBudgetPersons!= null && vecBudgetPersons.size() >0){
                budgetPersonTableModel.setData(vecBudgetPersons);
            }else{
                budgetPersonTableModel.setData(cvDataInNewMode);
            }
        }else{
            budgetPersonTableModel.setData(vecBudgetPersons);
        }
        setTableEditors();
        //        postInitComponents();
    }
    //Added for Case 3869 - Save not working for budget person - start
    //COEUSQA-2036 Code Table Prop Dev Appt Type and Period Types - Start
    /**
     * Fetch all the appointment types from the database
     */
    public void fetchAppointmentTypes(){
        //String connectTo = CoeusGuiConstants.CONNECTION_URL+ "/coeusFunctionsServlet";
        String connectTo = CoeusGuiConstants.CONNECTION_URL+ "/BudgetMaintenanceServlet";
        RequesterBean requester = new RequesterBean();
        ResponderBean responder = null;
        requester.setFunctionType(GET_APPOINTMENT_TYPE);
               
        AppletServletCommunicator comm = new AppletServletCommunicator(connectTo, requester);        
        comm.send();
        responder = comm.getResponse();
        try{
            if(responder.hasResponse()){
                HashMap hmAppData = (HashMap)responder.getDataObject();
                vecAppointmentTypes = constructAppointmentTypeVector(hmAppData);
                if(vecAppointmentTypes == null){
                    vecAppointmentTypes = new Vector();
                }
            }
        }catch(CoeusException e){
            vecAppointmentTypes = new Vector();
            e.printStackTrace();
        }
    }
    
    /**
     * Fetch all the appointment types from the database
     * @return Vector vecActiveData
     */
    public Vector fetchActiveAppointmentTypes(){
        String connectTo = CoeusGuiConstants.CONNECTION_URL+ "/BudgetMaintenanceServlet";
        RequesterBean requester = new RequesterBean();
        ResponderBean responder = null;
        Vector vecActiveData = null;
        requester.setFunctionType(GET_ACTIVE_APPOINTMENT_TYPE);
               
        AppletServletCommunicator comm = new AppletServletCommunicator(connectTo, requester);        
        comm.send();
        responder = comm.getResponse();
        try{
            if(responder.hasResponse()){
                HashMap hmAppData = (HashMap)responder.getDataObject();
                vecActiveData = constructAppointmentTypeVector(hmAppData);
                if(vecActiveData == null){
                    vecActiveData = new Vector();
                }
            }
        }catch(CoeusException e){
            vecActiveData = new Vector();
            e.printStackTrace();
        }
        return vecActiveData;
    }
    
    /**
     * to create the appointment types data with inactive data
     * @param HashMap hmData
     * @return Vector vecApptData
     */
    public Vector constructAppointmentTypeVector(HashMap hmData){
        // JM 7-16-2012 added to allow sorted data in appointment type
    	Map sortedData = edu.vanderbilt.coeus.utils.CoeusComparator.sortByComparator(hmData);
    	// JM END
    	
        Vector vecApptData = new Vector();
        // JM 7-16-2012 added to allow sorted data in appointment type
        //Set<Map.Entry<String, String>> setData = hmData.entrySet();
        Set<Map.Entry<String, String>> setData = sortedData.entrySet();
        // JM END
        for(Map.Entry<String,String> mapData : setData){
            ComboBoxBean cmbBean = new ComboBoxBean();
            cmbBean.setCode(mapData.getKey());
            cmbBean.setDescription(mapData.getKey());
            vecApptData.add(cmbBean);
        }
        return vecApptData;
    }
    
    // Modified for COEUSQA-3709 : Coeus4.5: Budget Dropdown Issue - Start
    /**
     * to create the appointment types data with inactive data
     * @param String selectedValue
     * @return Vector modifiedActiveAppointmentTypes
     */
//    private Vector constructActiveAppointmentTypes(String selectedValue){
//        Vector activeAppointmentTypes = null;
//        Vector modifiedActiveAppointmentTypes = null;
//        String selectedCode = "";
//        boolean entryFlag = true;
//        //to fetch the active appointment types
//        activeAppointmentTypes = fetchActiveAppointmentTypes();
//        //to fetch all the active appointment types
//        fetchAppointmentTypes();
//        for(Object vecApptData : vecAppointmentTypes) {
//            ComboBoxBean cmbBean = (ComboBoxBean)vecApptData;
//            if(selectedValue.equalsIgnoreCase(cmbBean.getCode())) {
//                selectedCode = cmbBean.getCode();
//            }
//        }
//        
//        //to add the inactive appointment type to active data if already present in the selection
//        for(Object vecActiveApptData : activeAppointmentTypes) {
//            if(modifiedActiveAppointmentTypes==null || modifiedActiveAppointmentTypes.size() == 0){
//                if(selectedValue != null && vecActiveApptData != null) {
//                    ComboBoxBean cmbBean = (ComboBoxBean)vecActiveApptData;
//                    if(selectedValue.equals(cmbBean.getDescription())){
//                        modifiedActiveAppointmentTypes = activeAppointmentTypes;
//                        entryFlag = false;
//                    }
//                }
//            }
//        }
//        if(entryFlag){
//            int size = activeAppointmentTypes.size();
//            modifiedActiveAppointmentTypes = new Vector();
//            for(Object vecActiveAppointmentData : activeAppointmentTypes) {
//                ComboBoxBean cmbActiveBean = (ComboBoxBean)vecActiveAppointmentData;
//                modifiedActiveAppointmentTypes.add(cmbActiveBean);
//            }
//            ComboBoxBean cmbSelectedBean = new ComboBoxBean();
//            cmbSelectedBean.setDescription(selectedValue);
//            cmbSelectedBean.setCode(selectedCode);
//            modifiedActiveAppointmentTypes.add(cmbSelectedBean);
//        }
//        return modifiedActiveAppointmentTypes;
//    }
    private Vector constructActiveAppointmentTypes(String selectedValue){
        
        Vector modifiedActiveAppointmentTypes = null;
        String selectedCode = "";
        boolean entryFlag = true;
        for(Object vecApptData : vecAppointmentTypes) {
            ComboBoxBean cmbBean = (ComboBoxBean)vecApptData;
            if(selectedValue.equalsIgnoreCase(cmbBean.getCode())) {
                selectedCode = cmbBean.getCode();
            }
        }
        //to add the inactive appointment type to active data if already present in the selection
        for(Object vecActiveApptData : vecActiveAppointmentTypes) {
            if(modifiedActiveAppointmentTypes==null || modifiedActiveAppointmentTypes.size() == 0){
                if(selectedValue != null && vecActiveApptData != null) {
                    ComboBoxBean cmbBean = (ComboBoxBean)vecActiveApptData;
                    if(selectedValue.equals(cmbBean.getDescription())){
                        modifiedActiveAppointmentTypes = vecActiveAppointmentTypes;
                        entryFlag = false;
                    }
                }
            }
        }
        if(entryFlag){
            int size = vecActiveAppointmentTypes.size();
            modifiedActiveAppointmentTypes = new Vector();
            for(Object vecActiveAppointmentData : vecActiveAppointmentTypes) {
                ComboBoxBean cmbActiveBean = (ComboBoxBean)vecActiveAppointmentData;
                modifiedActiveAppointmentTypes.add(cmbActiveBean);
            }
            ComboBoxBean cmbSelectedBean = new ComboBoxBean();
            cmbSelectedBean.setDescription(selectedValue);
            cmbSelectedBean.setCode(selectedCode);
            modifiedActiveAppointmentTypes.add(cmbSelectedBean);
        }
        return modifiedActiveAppointmentTypes;
    }
    // Modified for COEUSQA-3709 : Coeus4.5: Budget Dropdown Issue - End
    
    /**
     * to fetch the active appointment type data     
     * @return String strAppointmentType
     */
    private String setActiveAppointmentType(){
        //to fetch the active appointment types
        String strAppointmentType = EMPTY_STRING;
        String strActiveAppointmentType = EMPTY_STRING;
//        Vector vecAppointmentTypes = fetchActiveAppointmentTypes();
        boolean entryFlag = true;
        int counter = 0;
        //to check if the active types have the regular employee
        for(Object cmbAppointmentData:vecActiveAppointmentTypes){
            ComboBoxBean cmbBean = (ComboBoxBean)cmbAppointmentData;
            if(REG_EMPLOYEE.equals(cmbBean.getCode())){
                entryFlag = false;
                strAppointmentType = REG_EMPLOYEE;
            }
            if(counter==0){
                strActiveAppointmentType = cmbBean.getCode();
            }
            counter++;
        }
        //if reg employee is not active then set the first occuring active type for selection
        if(entryFlag){
            strAppointmentType = strActiveAppointmentType;
        }
        return strAppointmentType;
    }
    //COEUSQA-2036 Code Table Prop Dev Appt Type and Period Types - End
    //Added for Case 3869 - Save not working for budget person - end
    /** Specifies the Modal window */
    private void postInitComponents() {
        dlgPersonBudget = new CoeusDlgWindow(mdiForm);
        dlgPersonBudget.getContentPane().add(budgetPersonsForm);
        dlgPersonBudget.setTitle("Budget Persons");
        dlgPersonBudget.setFont(CoeusFontFactory.getLabelFont());
        dlgPersonBudget.setModal(true);
        dlgPersonBudget.setResizable(false);
        //Added for Case#2918 - Use of Salary Anniversary Date for calculating inflation in budget development module - starts
        if(showSalaryAnniversaryDate){
            //Modified for Case#3893 : Java 1.5 issues - Start
//            budgetPersonsForm.scrpnPerson.setPreferredSize(new Dimension(620,225));
//            budgetPersonsForm.scrpnPerson.setMinimumSize(new Dimension(620,225));
//            dlgPersonBudget.setSize(750,HEIGHT);
            //Case#3893 - End
            budgetPersonsForm.scrpnPerson.setPreferredSize(new Dimension(690,225));
            budgetPersonsForm.scrpnPerson.setMinimumSize(new Dimension(690,225));
            dlgPersonBudget.setSize(820,HEIGHT);
        } else {
            // Added for COEUSDEV-1051 Budget Person window squeezing the header section - Start
            budgetPersonsForm.scrpnPerson.setPreferredSize(new Dimension(WIDTH,225));
            budgetPersonsForm.scrpnPerson.setMinimumSize(new Dimension(WIDTH,225));
            // Added for COEUSDEV-1051 Budget Person window squeezing the header section - End
            dlgPersonBudget.setSize(WIDTH,HEIGHT);
        }
        //Added for Case#2918 - Use of Salary Anniversary Date for calculating inflation in budget development module - ends
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension dlgSize = dlgPersonBudget.getSize();
        dlgPersonBudget.setLocation(screenSize.width/2 - (dlgSize.width/2),
        screenSize.height/2 - (dlgSize.height/2));
        
        dlgPersonBudget.addEscapeKeyListener(
        new AbstractAction("escPressed"){
            public void actionPerformed(ActionEvent ae){
                cancelPersonsAction();
                return;
            }
        });
        dlgPersonBudget.setDefaultCloseOperation(CoeusDlgWindow.DO_NOTHING_ON_CLOSE);
        dlgPersonBudget.addWindowListener(new WindowAdapter(){
            //            public void windowOpened(WindowEvent we) {
            //               setWindowFocus();
            //            }
            public void windowClosing(WindowEvent we){
                cancelPersonsAction();
                return;
            }
        });
        
        dlgPersonBudget.addComponentListener(
        new ComponentAdapter(){
            public void componentShown(ComponentEvent e){
                setWindowFocus();
            }
        });
    }
    private void setWindowFocus(){
        if(getFunctionType()!= TypeConstants.DISPLAY_MODE){
            int rowCount = budgetPersonsForm.tblBudgetPersons.getRowCount();
            if(rowCount!=0){
                budgetPersonsForm.tblBudgetPersons.setRowSelectionInterval(0,0);
                budgetPersonsForm.tblBudgetPersons.setColumnSelectionInterval(0, NAME_COLUNM);
                
                budgetPersonsForm.tblBudgetPersons.editCellAt(0,1);
                budgetPersonsForm.tblBudgetPersons.getEditorComponent().requestFocusInWindow();
            }
        }else{
            budgetPersonsForm.btnCancel.requestFocusInWindow();
        }
        
    }
    
    /** This method is used to set the form data specified in
     * <CODE> data </CODE>
     * @param data data to set to the form
     */
    public void setFormData(Object  data) {
        //COEUSQA-1535-Access to institutionally maintained salaries in proposal budget - Start
        /*try {            
            if(budgetInfoBean.getUnitNumber() != null ) {
                unitNumber =  budgetInfoBean.getUnitNumber();
            }            
        } catch(Exception e) {
            e.getMessage();
        }*/
        //COEUSQA-1535-Access to institutionally maintained salaries in proposal budget - End
    }
    /** perform field formatting./** This method use to implement focus on first editable component in this page.
     */
     /* enabling, disabling components depending on the
      * function type.
      */
    public void formatFields() {
    	/* JM 9-30-2015 always want Add TBA disabled due to bug */
    	budgetPersonsForm.btnTBASearch.setEnabled(false);
    	/* JM END */
        if(getFunctionType() == TypeConstants.DISPLAY_MODE){
            budgetPersonsForm.tblBudgetPersons.setEnabled(false);
            budgetPersonsForm.btnAdd.setEnabled(false);
            budgetPersonsForm.btnDelete.setEnabled(false);
            budgetPersonsForm.btnOK.setEnabled(false);
            budgetPersonsForm.btnSearch.setEnabled(false);
            // Added for coeus4.3 enhancements
            // Added for rolodex and tba search
            budgetPersonsForm.btnRolodexSearch.setEnabled(false);
            budgetPersonsForm.btnTBASearch.setEnabled(false);
            //COEUSQA-1559 Display calculated base salary amount on RR Budget form in out years - Start
            budgetPersonsForm.btnCalculateBaseSal.setEnabled(false);
            //COEUSQA-1559 Display calculated base salary amount on RR Budget form in out years - End
            budgetPersonsForm.btnCancel.requestFocus();
            //budgetPersonsForm.tblBudgetPersons.setBackground((java.awt.Color) javax.swing.UIManager.getDefaults().get("TabbedPane.tabAreaBackground"));
            //budgetPersonsForm.tblBudgetPersons.setShowHorizontalLines(true);
            //budgetPersonsForm.tblBudgetPersons.setShowVerticalLines(true);            
        }
        //COEUSQA-1535-Access to institutionally maintained salaries in proposal budget - Start
        /*try {
            //To check whether the user has rights to view salaries 
            //If the user does not have rights then the salary column is removed from model
            if(!hasRightToViewInstitutionalSalaries()){
                TableColumnModel tableModel = budgetPersonsForm.tblBudgetPersons.getColumnModel();
                TableColumn column = tableModel.getColumn(CALC_BASE_COLUMN);
                column.setPreferredWidth(0);
                column.setMaxWidth(0);
                column.setMinWidth(0);
                column.setWidth(0);
                tableModel.getColumn(NAME_COLUNM).setPreferredWidth(215);
                tableModel.getColumn(APPT_TYPE_COLUMN).setPreferredWidth(190);                   
            }
        } catch (CoeusException ex) {
            ex.printStackTrace();
        }*/
        //COEUSQA-1535-Access to institutionally maintained salaries in proposal budget - End
    }
    
    /** Getter for property ProposalNumber. of the current Budget
     * @return proposalNumber String : ProposalNumber property.
     */
    
    public String getProposalNumber() {
        return proposalNumber;
    }
    
    /** Setter property ProposalNumber. of the current Budget
     * @param proposalNumber String ProposalNumber property.
     */
    
    public void setProposalNumber(String proposalNumber) {
        this.proposalNumber = proposalNumber;
    }
    
    /** Getter for property versionNumber.
     * @return versionNumber int versionNumber property.
     */
    
    public int getVersionNumber() {
        return versionNumber;
    }
    
    /** Setter property versionNumber.
     * @param versionNumber
     */
    
    public void setVersionNumber(int versionNumber) {/** This method use to implement focus on first editable component in this page.
     */
        this.versionNumber = versionNumber;
    }
    
    
    /** returns the BudgetPersonsForm component */
    public Component getControlledUI() {
        return budgetPersonsForm;
    }
    
    /** returns the form data
     * @return returns the form data
     */
    public Object getFormData() {
        return budgetPersonsForm;
    }
    
    /** registers all the components of a Form */
    public void registerComponents() {
        
        budgetPersonsForm.btnOK.addActionListener(this);
        budgetPersonsForm.btnCancel.addActionListener(this);
        budgetPersonsForm.btnAdd.addActionListener(this);
        budgetPersonsForm.btnDelete.addActionListener(this);
        budgetPersonsForm.btnSearch.addActionListener(this);
        // Added for coeus4.3 enhancements
        // Added for rolodex and tba search
        budgetPersonsForm.btnRolodexSearch.addActionListener(this);
        budgetPersonsForm.btnTBASearch.addActionListener(this);
        // Modified for coeus4.3 enhancements
        // Modified for rolodex and tba search
        //COEUSQA-1559 Display calculated base salary amount on RR Budget form in out years - Start
        budgetPersonsForm.btnCalculateBaseSal.addActionListener(this);
        //COEUSQA-1559 Display calculated base salary amount on RR Budget form in out years - End
        java.awt.Component[] components = {
            budgetPersonsForm.tblBudgetPersons,
            budgetPersonsForm.btnOK,
            budgetPersonsForm.btnCancel,
            budgetPersonsForm.btnAdd,
            budgetPersonsForm.btnSearch,
            budgetPersonsForm.btnRolodexSearch,
            budgetPersonsForm.btnTBASearch,
            budgetPersonsForm.btnDelete
            //COEUSQA-1559 Display calculated base salary amount on RR Budget form in out years - Start
            ,budgetPersonsForm.btnCalculateBaseSal
            //COEUSQA-1559 Display calculated base salary amount on RR Budget form in out years - End
        };
        ScreenFocusTraversalPolicy  traversePolicy = new ScreenFocusTraversalPolicy( components );
        budgetPersonsForm.setFocusTraversalPolicy(traversePolicy);
        budgetPersonsForm.setFocusCycleRoot(true);
        
    }
    /** To display the Appointment details for a person who is having valid appointment details
     *Displays the Job code and Appointment type for a selected row. If the row doesn't contain
     *appointment details, don't display. Query the bean for the selected row
     */
    public void lookUpPersonDetails(){
        try{
            Equals appEquals;
            AppointmentsBean appointmentsBean;
            int selectedRow = budgetPersonsForm.tblBudgetPersons.getSelectedRow();
            budgetPersonsBean = (BudgetPersonsBean)vecBudgetPersons.get(selectedRow);
            String personId = budgetPersonsBean.getPersonId();
            String jobCode = budgetPersonsBean.getJobCode();
            appEquals = new Equals("jobCode", jobCode);
            vecAppDetails = getPersonInfo(personId);
            if( vecAppDetails !=null && vecAppDetails.size() > 0){
                vecAppDetails = vecAppDetails.filter(appEquals);
                appointmentsBean = (AppointmentsBean) vecAppDetails.get(0);
                AppointmentDetailForm appointmentDetailForm
                = new AppointmentDetailForm(appointmentsBean);
                appointmentDetailForm.setValues();
                appointmentDetailForm.display();
            }
        }catch(Exception e){
            e.getMessage();
        }
    }
    
    
    
    /** Check for the Duplication of the Person Name. If Job Code, Person Name(Full Name)
     *and Effective Date are same then throw the Duplication message. If any one of these are
     *different then accept the row
     */
    private boolean checkDuplicatePerson(){
        // Added for coeus4.3 enhancements
        Equals jobCodeEquals,effDateEquals, nameEquals, personIdEquals;
        And jobAndDate, jobAndDateAndName, jobAndDateAndNameAndPersonId;
        if(vecBudgetPersons!=null && vecBudgetPersons.size() > 0){
            for(int index = 0; index < vecBudgetPersons.size(); index++){
                BudgetPersonsBean budgetPersonsBean = ( BudgetPersonsBean )
                vecBudgetPersons.get(index);
                //             if(budgetPersonsBean.getPersonId().equalsIgnoreCase(EMPTY_STRING) ||
                //             budgetPersonsBean.getJobCode().equalsIgnoreCase(EMPTY_STRING) ||
                //             budgetPersonsBean.getEffectiveDate() == null) break;
                
                jobCodeEquals = new Equals("jobCode", budgetPersonsBean.getJobCode());
                effDateEquals = new Equals("effectiveDate", budgetPersonsBean.getEffectiveDate());
                nameEquals = new Equals("fullName", budgetPersonsBean.getFullName());
                // For checking duplicate Persons, person id is aslo added as a constraint
                personIdEquals = new Equals("personId", budgetPersonsBean.getPersonId());
                jobAndDate = new And(jobCodeEquals, effDateEquals);
                jobAndDateAndName = new And(jobAndDate, nameEquals);
                jobAndDateAndNameAndPersonId = new And(jobAndDateAndName, personIdEquals);
                CoeusVector coeusVector;
                //            coeusVector = vecBudgetPersons.filter(jobAndDateAndName);
                coeusVector = vecBudgetPersons.filter(jobAndDateAndNameAndPersonId);
                if(coeusVector.size()==-1)return false;
                if(coeusVector!=null && coeusVector.size() > 1){
                    mesg = coeusMessageResources.parseMessageKey(DUPLICATE_NAME);
                    CoeusOptionPane.showInfoDialog(mesg);
                    budgetPersonsForm.tblBudgetPersons.editCellAt(index,NAME_COLUNM);
                    budgetPersonCellEditor.txtPersonName.requestFocus();
                    budgetPersonsForm.tblBudgetPersons.setRowSelectionInterval(index,index);
                    budgetPersonsForm.tblBudgetPersons.scrollRectToVisible(
                    budgetPersonsForm.tblBudgetPersons.getCellRect(
                    index ,0, true));
                    return true;
                }
            }
        }
        return false;
    }
    
    
    /** validate the form data/Form and returns true if
     * validation is through else returns false.
     * @throws CoeusUIException if some exception occurs or some validation fails.
     * @return true if
     * validation is through else returns false.
     */
    public boolean validate() throws CoeusUIException {
        try{
            int rowCount = budgetPersonsForm.tblBudgetPersons.getRowCount();
            String jobCode;
            String personName;
            String message = null;
            for (int row = 0; row < rowCount; row++){
                jobCode =  (String)budgetPersonsForm.tblBudgetPersons.getValueAt(row,JOB_CODE_COLUMN);
                personName =(String)budgetPersonsForm.tblBudgetPersons.getValueAt(row,NAME_COLUNM);
                if(personName==null || personName.equals(EMPTY_STRING)){
                    message = coeusMessageResources.parseMessageKey(
                    ENTER_PERSON_NAME);
                    CoeusOptionPane.showInfoDialog(message);
                    budgetPersonsForm.tblBudgetPersons.setRowSelectionInterval(row,row);
                    budgetPersonsForm.tblBudgetPersons.scrollRectToVisible(
                    budgetPersonsForm.tblBudgetPersons.getCellRect(
                    row ,0, true));
                    budgetPersonsForm.tblBudgetPersons.editCellAt(row,NAME_COLUNM);
                    budgetPersonCellEditor.txtPersonName.requestFocus();
                    
                    return false;
                }else if(jobCode==null || jobCode.equals(EMPTY_STRING)){
                    message = coeusMessageResources.parseMessageKey(
                    ENTER_JOB_CODE);
                    CoeusOptionPane.showInfoDialog(message);
                    budgetPersonsForm.tblBudgetPersons.setRowSelectionInterval(row,row);
                    budgetPersonsForm.tblBudgetPersons.scrollRectToVisible(
                    budgetPersonsForm.tblBudgetPersons.getCellRect(
                    row ,0, true));
                    budgetPersonsForm.tblBudgetPersons.editCellAt(row,JOB_CODE_COLUMN);
                    budgetPersonCellEditor.txtJobCode.requestFocus();
                    return false;
                }
            }
            
        } catch(Exception e){
            e.getMessage();
        }
        return true;
    }
    /** Sets the table header as column header. Specifies the size of the column
     */
    private void setTableEditors(){
        try{
            JTableHeader tableHeader = budgetPersonsForm.tblBudgetPersons.getTableHeader();
            tableHeader.addMouseListener(new ColumnHeaderListener());
            tableHeader.setReorderingAllowed(false);
            tableHeader.setFont(CoeusFontFactory.getLabelFont());
            
            budgetPersonsForm.tblBudgetPersons.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
            budgetPersonsForm.tblBudgetPersons.setRowHeight(22);
            budgetPersonsForm.tblBudgetPersons.setSelectionBackground(java.awt.Color.white);
            budgetPersonsForm.tblBudgetPersons.setSelectionForeground(java.awt.Color.black);
            budgetPersonsForm.tblBudgetPersons.setShowHorizontalLines(false);
            budgetPersonsForm.tblBudgetPersons.setShowVerticalLines(false);
            budgetPersonsForm.tblBudgetPersons.setOpaque(false);
            budgetPersonsForm.tblBudgetPersons.setSelectionMode(
            DefaultListSelectionModel.SINGLE_SELECTION);
            
            /* JM 4-5-2016 changed all hard-coded column indexes to pointers */
            TableColumn column = budgetPersonsForm.tblBudgetPersons.getColumnModel().getColumn(HAND_ICON_COLUMN); // JM
            //column.setMinWidth(30);
            //column.setMaxWidth(50);
            column.setPreferredWidth(28);
            column.setResizable(true);
            column.setCellRenderer(new IconRenderer());
            column.setHeaderRenderer(new EmptyHeaderRenderer());
            
            column = budgetPersonsForm.tblBudgetPersons.getColumnModel().getColumn(NAME_COLUNM); // JM
            //column.setMinWidth(120);
            //column.setMaxWidth(350);
            column.setPreferredWidth(160);
            column.setResizable(true);
            column.setCellEditor(budgetPersonCellEditor);
            //column.setCellRenderer(budgetPersonRenderer); // JM using custom renderer
            
            /* JM 4-8-2016 setting for inactive and external persons */ 
        	PersonTableCellRenderer renderer = 
        		new PersonTableCellRenderer(HAND_ICON_COLUMN,IS_EXTERNAL_PERSON_COL,true,false,false,getFunctionType());
        	column.setCellRenderer(renderer);
        	/* JM END */
            
            column = budgetPersonsForm.tblBudgetPersons.getColumnModel().getColumn(JOB_CODE_COLUMN); // JM
            //column.setMinWidth(58);
            //column.setMaxWidth(60);
            //Modified for Case#3893 - Java 1.5 issues
            //column.setPreferredWidth(60);
            column.setPreferredWidth(80);
            //Case#3892 - End
            column.setResizable(true);
            column.setCellEditor(budgetPersonCellEditor);
            column.setCellRenderer(budgetPersonRenderer);
            
            column = budgetPersonsForm.tblBudgetPersons.getColumnModel().getColumn(APPT_TYPE_COLUMN); // JM
            //column.setMinWidth(108);
            //column.setMaxWidth(120);
            //Modified for Case#3893 - Java 1.5 issues
//            column.setPreferredWidth(105);
            column.setPreferredWidth(140);
            //CAse#3893 - End
            column.setResizable(true);
            column.setCellEditor(budgetPersonCellEditor);
            column.setCellRenderer(budgetPersonRenderer);
            
            column = budgetPersonsForm.tblBudgetPersons.getColumnModel().getColumn(EFF_DATE_COLUMN); // JM
            //column.setMinWidth(78);
            //column.setMaxWidth(350);
            column.setPreferredWidth(75);
            column.setCellEditor(budgetPersonCellEditor);
            column.setCellRenderer(budgetPersonRenderer);
            
            column = budgetPersonsForm.tblBudgetPersons.getColumnModel().getColumn(CALC_BASE_COLUMN); // JM
            //column.setMinWidth(100);
            //column.setMaxWidth(350);
            //COEUSQA-1535-Access to institutionally maintained salaries in proposal budget - Start
            column.setPreferredWidth(108);
            /*if(hasRightToViewInstitutionalSalaries()){
                column.setPreferredWidth(108);
            }else{
                column.setPreferredWidth(0);
                column.setMaxWidth(0);
                column.setMinWidth(0);
                column.setWidth(0);
            }*/
            column.setCellEditor(budgetPersonCellEditor);
            column.setCellRenderer(budgetPersonRenderer);
            
            /* JM 4-5-2016 remove external persons column from display */
            column = budgetPersonsForm.tblBudgetPersons.getColumnModel().getColumn(IS_EXTERNAL_PERSON_COL); // JM
            column.setMinWidth(0);
            column.setMaxWidth(0);
            column.setPreferredWidth(0);
            /* JM END */            
            
            //COEUSQA-1535-Access to institutionally maintained salaries in proposal budget - Start
            column.setResizable(true);
            column.setCellEditor(budgetPersonCellEditor);
            column.setCellRenderer(budgetPersonRenderer);
            //COEUSQA-1559 Display calculated base salary amount on RR Budget form in out years - Start
            //budget period details
            if(vecBudgetPeriods == null){
                vecBudgetPeriods = QueryEngine.getInstance().executeQuery(key,BudgetPeriodBean.class, CoeusVector.FILTER_ACTIVE_BEANS);
            }
            //COEUSQA-1559 Display calculated base salary amount on RR Budget form in out years - End
            //Added for Case#2918 - Use of Salary Anniversary Date for calculating inflation in budget development module - starts
            if(showSalaryAnniversaryDate){
                column = budgetPersonsForm.tblBudgetPersons.getColumnModel().getColumn(SALARY_ANNIVERSARY_DATE_COLUMN); // JM
                column.setPreferredWidth(80);
                column.setCellEditor(budgetPersonCellEditor);
                column.setCellRenderer(budgetPersonRenderer);
                //COEUSQA-1559 Display calculated base salary amount on RR Budget form in out years - Start
                if(vecBudgetPeriods!=null && vecBudgetPeriods.size()>0){
                	/* JM 4-5-2016 incremented from 7 to 8 due to new external persons column */
                    int columnIndex = 8;
                    /* JM END */
                    int numOfColumns = vecBudgetPeriods.size()+columnIndex;
                    for(;columnIndex<numOfColumns;columnIndex++){
                        column = budgetPersonsForm.tblBudgetPersons.getColumnModel().getColumn(columnIndex);
                        column.setPreferredWidth(110);
                        column.setCellEditor(budgetPersonCellEditor);
                        column.setCellRenderer(budgetPersonRenderer);
                    }
                }
                //COEUSQA-1559 Display calculated base salary amount on RR Budget form in out years - End
            }
            //COEUSQA-1559 Display calculated base salary amount on RR Budget form in out years - Start
            else{
                if(vecBudgetPeriods!=null && vecBudgetPeriods.size()>0){
                	/* JM 4-5-2016 incremented from 6 to 7 due to new external persons column */
                    int columnIndex = 7;
                    /* JM END */
                    int numOfColumns = vecBudgetPeriods.size()+columnIndex;
                    for(;columnIndex<numOfColumns;columnIndex++){
                        column = budgetPersonsForm.tblBudgetPersons.getColumnModel().getColumn(columnIndex);
                        column.setPreferredWidth(110);
                        column.setCellEditor(budgetPersonCellEditor);
                        column.setCellRenderer(budgetPersonRenderer);
                    }
                }
            }
            //COEUSQA-1559 Display calculated base salary amount on RR Budget form in out years - End
            //Added for Case#2918 - Use of Salary Anniversary Date for calculating inflation in budget development module - ends
            
            
        }catch(Exception e){
            e.getMessage();
        }
    }
    
    /** This class will sort the column values in ascending and descending order
     *based on number of clicks. This will sort only Name, Job code and Effective date
     *columns only which are primary keys.
     */
    
    public class ColumnHeaderListener extends MouseAdapter {
        String nameBeanId [][] ={
            {"0","" },
            {"1","fullName" },
            {"2","jobCode"},
            {"3","appointmentType" },
            {"4","effectiveDate"},
            {"5","calculationBase" },
            
        };
        boolean sort =true;
        /**
         * @param evt
         */
        public void mouseClicked(MouseEvent evt) {
            try {
                //Bug fix 1869 -START
                budgetPersonCellEditor.stopCellEditing();
                //Bug fix 1869 -END
                JTable table = ((JTableHeader)evt.getSource()).getTable();
                TableColumnModel colModel = table.getColumnModel();
                
                // The index of the column whose header was clicked
                int vColIndex = colModel.getColumnIndexAtX(evt.getX());
                // int mColIndex = table.convertColumnIndexToModel(vColIndex);
                if(vecBudgetPersons!=null && vecBudgetPersons.size()>0 &&
                nameBeanId [vColIndex][1].length() >1 ){
                    ((CoeusVector)vecBudgetPersons).sort(nameBeanId [vColIndex][1],sort);
                    if(sort)
                        sort = false;
                    else
                        sort = true;
                    budgetPersonTableModel.fireTableRowsUpdated(0, budgetPersonTableModel.getRowCount());
                }
            } catch(Exception exception) {
                exception.getMessage();
            }
        }
    }// End of ColumnHeaderListener.................
    
    
    /**
     * @param actionEvent
     */
    public void actionPerformed(java.awt.event.ActionEvent actionEvent) {
        int index = budgetPersonsForm.tblBudgetPersons.getSelectedRow();
        if(index != -1 && index >= 0){
            budgetPersonsForm.tblBudgetPersons.getCellEditor(index,1).stopCellEditing();
        }
        Object source  = actionEvent.getSource();
        if(source==budgetPersonsForm.btnAdd){
            addPersonsAction();
        }else if(source==budgetPersonsForm.btnCancel){
            cancelPersonsAction();
        }else if(source==budgetPersonsForm.btnDelete){
            deletePersonsAction();
        }else if(source==budgetPersonsForm.btnOK){
            OkPersonsAction();
        }else if(source==budgetPersonsForm.btnSearch){
            searchPersonsAction();
            // Added for coeus4.3 enhancements
            // Added for rolodex and tba search
        }else if(source==budgetPersonsForm.btnRolodexSearch){
            searchRolodexAction();
        }else if(source==budgetPersonsForm.btnTBASearch){
            searchTBAAction();
        }
        //COEUSQA-1559 Display calculated base salary amount on RR Budget form in out years - Start
        else if(source==budgetPersonsForm.btnCalculateBaseSal){
            calculateTotPeriodSalary();
        }
        //COEUSQA-1559 Display calculated base salary amount on RR Budget form in out years - End
    }
    
    /** Method which specifies the Add action and listeners.Specifies the added persons
     * details
     */
    private void addPersonsAction(){
        // Vector rowData = new Vector();
        String formatedDate = dtUtils.formatDate(budgetStartDate.toString(),SIMPLE_DATE_FORMAT);
        long date =  new java.util.Date(formatedDate).getTime();
        java.sql.Date startDate = new java.sql.Date(date);
        
        BudgetPersonsBean newBean = new BudgetPersonsBean();
        
        newBean.setProposalNumber(budgetInfoBean.getProposalNumber());
        newBean.setVersionNumber(budgetInfoBean.getVersionNumber());
        newBean.setFullName(EMPTY_STRING);
        newBean.setJobCode(EMPTY_STRING);
        //Modified for Case 3869 - Save not working for budget person - start
        //newBean.setAppointmentType(appointments[4]);
        //COEUSQA-2036 Code Table Prop Dev Appt Type and Period Types - Start
        //newBean.setAppointmentType(REG_EMPLOYEE);
        newBean.setAppointmentType(setActiveAppointmentType());
        //COEUSQA-2036 Code Table Prop Dev Appt Type and Period Types - End
        
        //Modified for Case 3869 - Save not working for budget person - start
        newBean.setEffectiveDate(startDate);
        newBean.setCalculationBase(0.0);
        newBean.setAcType(TypeConstants.INSERT_RECORD);
        
        //While inserting a new person, set the Row ID which identifies this bean
        //even when the Primary key values are changed
        //newBean.setRowId(getMaxRowID());
        
        modified= true;
        vecBudgetPersons.add(newBean);
        budgetPersonTableModel.fireTableRowsInserted(budgetPersonTableModel.getRowCount() + 1, budgetPersonTableModel.getRowCount() + 1);
        int lastRow = budgetPersonsForm.tblBudgetPersons.getRowCount()-1;
        if(lastRow >= 0){
            budgetPersonsForm.tblBudgetPersons.setRowSelectionInterval( lastRow, lastRow );
            budgetPersonsForm.tblBudgetPersons.setColumnSelectionInterval(1,1);
            budgetPersonsForm.tblBudgetPersons.scrollRectToVisible(
            budgetPersonsForm.tblBudgetPersons.getCellRect(
            lastRow ,0, true));
        }
        budgetPersonsForm.tblBudgetPersons.editCellAt(lastRow,NAME_COLUNM);
        budgetPersonsForm.tblBudgetPersons.getEditorComponent().requestFocusInWindow();
        budgetPersonCellEditor.txtPersonName.requestFocus();
    }
    
    /** Check for the modified persons.Get the status of the bean, whether the data is
     * changed or not
     */
    private void setModifiedPersons() {
        if (vecBudgetPersons != null && vecBudgetPersons.size() > 0) {
            int size = vecBudgetPersons.size();
            for (int index = 0; index < size; index++) {
                BudgetPersonsBean perBean = (BudgetPersonsBean) vecBudgetPersons.get(index);
                if (perBean.getAcType() == null) {
                    if (!perBean.getPersonId().equals(perBean.getAw_PersonId()) ||
                    //!personsBean.getFullName().equals(personsBean.getAw_FullName()) ||
                    !perBean.getJobCode().equals(perBean.getAw_JobCode()) ||
                    !perBean.getAppointmentType().equals(perBean.getAw_AppointmentType()) ||
                    !perBean.getEffectiveDate().equals(perBean.getAw_EffectiveDate())||
                    perBean.getCalculationBase()!= (perBean.getAw_CalculationBase()) ||
                    //check the salary anniversary date is modified or not.
                    isSalaryAnniversaryDateModified(perBean)){  
                        
                        perBean.setAcType(TypeConstants.UPDATE_RECORD);
                    }
                    //COEUSQA-1559 Display calculated base salary amount on RR Budget form in out years - Start
                    if(perBean.getBaseSalaryP1()!= (perBean.getAw_BaseSalaryP1()) ||
                            perBean.getBaseSalaryP2()!= (perBean.getAw_BaseSalaryP2()) ||
                            perBean.getBaseSalaryP3()!= (perBean.getAw_BaseSalaryP3()) ||
                            perBean.getBaseSalaryP4()!= (perBean.getAw_BaseSalaryP4()) ||
                            perBean.getBaseSalaryP5()!= (perBean.getAw_BaseSalaryP5()) ||
                            perBean.getBaseSalaryP6()!= (perBean.getAw_BaseSalaryP6()) ||
                            perBean.getBaseSalaryP7()!= (perBean.getAw_BaseSalaryP7()) ||
                            perBean.getBaseSalaryP8()!= (perBean.getAw_BaseSalaryP8()) ||
                            perBean.getBaseSalaryP9()!= (perBean.getAw_BaseSalaryP9()) ||
                            perBean.getBaseSalaryP10()!= (perBean.getAw_BaseSalaryP10())){
                        perBean.setAcType(TypeConstants.UPDATE_RECORD);
                    }
                    //COEUSQA-1559 Display calculated base salary amount on RR Budget form in out years - End){
                }
            }
        }
    }
    
    private boolean isSalaryAnniversaryDateModified(BudgetPersonsBean perBean){
        if(perBean.getSalaryAnniversaryDate() == null && perBean.getAw_SalaryAnniversaryDate() == null){
            return false;
        } else if(perBean.getSalaryAnniversaryDate() != null && perBean.getAw_SalaryAnniversaryDate() == null){
            return true;
        } else if(perBean.getSalaryAnniversaryDate() == null && perBean.getAw_SalaryAnniversaryDate() != null){
            return true;
        } else if(!perBean.getSalaryAnniversaryDate().toString().equals(perBean.getAw_SalaryAnniversaryDate().toString())){
            return true;
        }
        return false;
    }
    
    /** Delete the person from base window and check whether the person is deleted or
     * not and update to the queryEngine as delete
     */
    private void deletePersonsAction(){
        int rowIndex = budgetPersonsForm.tblBudgetPersons.getSelectedRow();
        if(rowIndex != -1 && rowIndex >= 0){
            mesg = DELETE_CONFIRMATION;
            
            // check whether the person exists in the personnel budget as line item
            BudgetPersonsBean deletedPersonsBean = (BudgetPersonsBean)vecBudgetPersons.get(rowIndex);
            String queryKey = budgetInfoBean.getProposalNumber()+budgetInfoBean.getVersionNumber();
            CoeusVector cvFilterData = null;
            try{
                cvFilterData = QueryEngine.getInstance().executeQuery(queryKey,BudgetPersonnelDetailsBean.class,
                CoeusVector.FILTER_ACTIVE_BEANS).filter((new Equals("personId", deletedPersonsBean.getPersonId())));
            }catch (CoeusException exception){
                exception.printStackTrace();
            }
            if(cvFilterData!= null && cvFilterData.size() > 0){
                CoeusOptionPane.showInfoDialog(
                coeusMessageResources.parseMessageKey(DELETE_PERSON));
                return ;
            }
            int selectedOption = CoeusOptionPane.showQuestionDialog(
            coeusMessageResources.parseMessageKey(mesg),
            CoeusOptionPane.OPTION_YES_NO,
            CoeusOptionPane.DEFAULT_YES);
            if(selectedOption == CoeusOptionPane.SELECTION_YES){
                if (deletedPersonsBean.getAcType() == null ||
                deletedPersonsBean.getAcType().equals(TypeConstants.UPDATE_RECORD)) {
                    vecDeletedPersons.add(deletedPersonsBean);
                }
                if(vecBudgetPersons!=null && vecBudgetPersons.size() > 0){
                    vecBudgetPersons.remove(rowIndex);
                    budgetPersonTableModel.fireTableRowsDeleted(rowIndex, rowIndex);
                    modified = true;
                    deletedPersonsBean.setAcType(TypeConstants.DELETE_RECORD);
                    budgetPersonCellEditor.stopCellEditing();
                }
                if(rowIndex >0){
                    budgetPersonsForm.tblBudgetPersons.setRowSelectionInterval(
                    rowIndex-1,rowIndex-1);
                    budgetPersonsForm.tblBudgetPersons.setColumnSelectionInterval(NAME_COLUNM, NAME_COLUNM);
                    budgetPersonsForm.tblBudgetPersons.scrollRectToVisible(
                    budgetPersonsForm.tblBudgetPersons.getCellRect(
                    rowIndex-1 ,0, true));
                }else{
                    if(budgetPersonsForm.tblBudgetPersons.getRowCount()>0){
                        budgetPersonsForm.tblBudgetPersons.setRowSelectionInterval(0,0);
                        budgetPersonsForm.tblBudgetPersons.setColumnSelectionInterval(NAME_COLUNM, NAME_COLUNM);
                    }
                }
                
            }
        }
    }
    
    
    /** Search the person, If the appointment type >1, display appointmentForPersonForm,
     * If appointment type=1 display the valid person with one appointment type without
     * a form. If appointment type is not there, display only valid person's full name
     */
    private void  searchPersonsAction(){
        try{
            
            int lastRow = budgetPersonsForm.tblBudgetPersons.getRowCount()-1;
            //   int selectedRow = budgetPersonsForm.tblBudgetPersons.getSelectedRow();
            String formatedDate = dtUtils.formatDate(budgetStartDate.toString(),SIMPLE_DATE_FORMAT);
            long date =  new java.util.Date(formatedDate).getTime();
            java.sql.Date startDate = new java.sql.Date(date);
            
            String personID = null;
            String personName = null;
            String salaryAnnivDate = null;
            java.sql.Date salaryAnniversaryDate = null;
            //            edu.mit.coeus.utils.Utils Utils;
            int inIndex=budgetPersonsForm.tblBudgetPersons.getSelectedRow();
            
            if(budgetPersonsForm.tblBudgetPersons.isEditing()){
                java.awt.Component comp = budgetPersonsForm.tblBudgetPersons.getEditorComponent();
                String value = null;
                if (comp instanceof javax.swing.text.JTextComponent) {
                    value = ((javax.swing.text.JTextComponent)comp).getText();
                }
                else if (comp instanceof javax.swing.JComboBox) {
                    
                    value = EMPTY_STRING;
                }
                
                if( (value != null)){
                    budgetPersonsForm.tblBudgetPersons.setValueAt(value,inIndex,1);
                }
                budgetPersonsForm.tblBudgetPersons.getCellEditor().cancelCellEditing();
            }
            
            CoeusSearch coeusSearch =
            new CoeusSearch(mdiForm, PERSON_SEARCH,
            CoeusSearch.TWO_TABS_WITH_MULTIPLE_SELECTION );
            coeusSearch.showSearchWindow();
            
            Vector vSelectedPersons = coeusSearch.getMultipleSelectedRows();
            
            if( vSelectedPersons != null ){
                //While inserting a new person, set the Row ID which identifies this bean
                //even when the Primary key values are changed
                //int rowID = getMaxRowID();
                
                HashMap singlePersonData = null;
                for(int indx = 0; indx < vSelectedPersons.size(); indx++ ){
                    singlePersonData = (HashMap)vSelectedPersons.get( indx ) ;
                    
                    if( singlePersonData !=null ){
                        /* construct the full name of person */
                        personID = edu.mit.coeus.utils.Utils.
                        convertNull(singlePersonData.get( "PERSON_ID" ));//personInfo.get("PERSON_ID"));
                        vecSearchPerson = getPersonInfo(personID);
                        personName = edu.mit.coeus.utils.Utils.
                        convertNull(singlePersonData.get( "FULL_NAME" ));//personInfo.get("FULL_NAME"));
                        salaryAnnivDate = edu.mit.coeus.utils.Utils.
                        convertNull(singlePersonData.get( "SALARY_ANNIVERSARY_DATE" ));
                        
                        if(salaryAnnivDate != null && !salaryAnnivDate.equals(EMPTY_STRING)){
                         String formatedAnnivDate = dtUtils.formatDate(salaryAnnivDate.toString(),SIMPLE_DATE_FORMAT);
                         long annvdate =  new java.util.Date(formatedAnnivDate).getTime();
                         salaryAnniversaryDate = new java.sql.Date(annvdate);   
                        }
                        
                        if(vecSearchPerson!=null){
                            if(vecSearchPerson.size() > 1){
                                
                                //Has more than one appointment
                                appointmentsForPersonForm = new AppointmentsForPersonForm(
                                mdiForm,true,personID,personName,vecSearchPerson);
                                AppointmentsBean appointmentsBean = appointmentsForPersonForm.display();
                                BudgetPersonsBean budgetPersonsBean = new BudgetPersonsBean();
                                budgetPersonsBean.setAw_ProposalNumber(budgetInfoBean.getProposalNumber());
                                budgetPersonsBean.setAw_VersionNumber(budgetInfoBean.getVersionNumber());
                                budgetPersonsBean.setAw_PersonId(personID);
                                budgetPersonsBean.setAw_JobCode(appointmentsBean.getJobCode());
                                budgetPersonsBean.setAw_AppointmentType(appointmentsBean.getAppointmentType());
                                budgetPersonsBean.setAw_EffectiveDate(startDate);
                                //COEUSQA-1535-Access to institutionally maintained salaries in proposal budget - Start
                                //budgetPersonsBean.setAw_CalculationBase(appointmentsBean.getSalary());
                                if(hasRightToViewInstitutionalSalaries(budgetInfoBean, personID)){
                                    budgetPersonsBean.setAw_CalculationBase(appointmentsBean.getSalary());
                                }else{
                                    budgetPersonsBean.setAw_CalculationBase(0.00);
                                }
                                //COEUSQA-1535-Access to institutionally maintained salaries in proposal budget - End
                                budgetPersonsBean.setProposalNumber(budgetInfoBean.getProposalNumber());
                                budgetPersonsBean.setVersionNumber(budgetInfoBean.getVersionNumber());
                                budgetPersonsBean.setPersonId(personID);
                                budgetPersonsBean.setFullName(personName);
                                //Code added for Coeus4.3 enhancements - starts
                                if(appointmentsBean.getJobCode()==null ||
                                appointmentsBean.getJobCode().equals(EMPTY_STRING)){
                                    budgetPersonsBean.setJobCode(getDefaultJobCode());
                                } else {
                                    budgetPersonsBean.setJobCode(appointmentsBean.getJobCode());
                                }
                                //Code commented for Coeus4.3 enhancement
                                //budgetPersonsBean.setJobCode(appointmentsBean.getJobCode());
                                //Code added for Coeus4.3 enhancements - ends
                                if(appointmentsBean.getAppointmentType() != null) {
                                    budgetPersonsBean.setAppointmentType(appointmentsBean.getAppointmentType());
                                }else{
                                    budgetPersonsBean.setAppointmentType(EMPTY_STRING);
                                }
                                 //Added for Case#2918 - Use of Salary Anniversary Date for calculating inflation in budget development module - Start
                                if(salaryAnniversaryDate !=null){
                                    budgetPersonsBean.setSalaryAnniversaryDate(salaryAnniversaryDate);
                                    budgetPersonsBean.setAw_SalaryAnniversaryDate(salaryAnniversaryDate);
                                }
                                //Added for Case#2918 - Use of Salary Anniversary Date for calculating inflation in budget development module - End
                            
                                budgetPersonsBean.setEffectiveDate(startDate);
                                //COEUSQA-1535-Access to institutionally maintained salaries in proposal budget - Start
                                //budgetPersonsBean.setCalculationBase(appointmentsBean.getSalary());
                                if(hasRightToViewInstitutionalSalaries(budgetInfoBean, personID)){
                                    budgetPersonsBean.setCalculationBase(appointmentsBean.getSalary());
                                }else{
                                    budgetPersonsBean.setCalculationBase(0.00);
                                }
                                //COEUSQA-1535-Access to institutionally maintained salaries in proposal budget - End
                                budgetPersonsBean.setAcType(TypeConstants.INSERT_RECORD);
                                //budgetPersonsBean.setRowId(rowID++);
                                modified = true;
                                budgetPersonTableModel.addRow(budgetPersonsBean);
                                budgetPersonTableModel.fireTableRowsInserted(budgetPersonTableModel.getRowCount(), budgetPersonTableModel.getRowCount());
                                if(lastRow >= 0){
                                    budgetPersonsForm.tblBudgetPersons.setRowSelectionInterval(
                                    lastRow+1,lastRow+1 );
                                    budgetPersonsForm.tblBudgetPersons.scrollRectToVisible(
                                    budgetPersonsForm.tblBudgetPersons.getCellRect(
                                    lastRow ,0, true));
                                }
                                
                            }else if(vecSearchPerson.size() == 1){
                                //Has exactly one appointment or not
                                BudgetPersonsBean budgetPersonsBean = new BudgetPersonsBean();
                                AppointmentsBean appointmentsBean = (AppointmentsBean)vecSearchPerson.elementAt(0);
                                
                                budgetPersonsBean.setAw_ProposalNumber(budgetInfoBean.getProposalNumber());
                                budgetPersonsBean.setAw_VersionNumber(budgetInfoBean.getVersionNumber());
                                budgetPersonsBean.setAw_PersonId(personID);
                                budgetPersonsBean.setAw_JobCode(appointmentsBean.getJobCode());
                                budgetPersonsBean.setAw_AppointmentType(appointmentsBean.getAppointmentType());
                                budgetPersonsBean.setAw_EffectiveDate(startDate);
                                //COEUSQA-1535-Access to institutionally maintained salaries in proposal budget - Start
                                //budgetPersonsBean.setAw_CalculationBase(appointmentsBean.getSalary());
                                if(hasRightToViewInstitutionalSalaries(budgetInfoBean, personID)){
                                    budgetPersonsBean.setAw_CalculationBase(appointmentsBean.getSalary());
                                }else{
                                    budgetPersonsBean.setAw_CalculationBase(0.00);
                                }
                                //COEUSQA-1535-Access to institutionally maintained salaries in proposal budget - End
                                
                                budgetPersonsBean.setProposalNumber(budgetInfoBean.getProposalNumber());
                                budgetPersonsBean.setVersionNumber(budgetInfoBean.getVersionNumber());
                                budgetPersonsBean.setPersonId(personID);
                                budgetPersonsBean.setFullName(personName);
                                //Code added for Coeus4.3 enhancements - starts
                                if(appointmentsBean.getJobCode()==null ||
                                appointmentsBean.getJobCode().equals(EMPTY_STRING)){
                                    budgetPersonsBean.setJobCode(getDefaultJobCode());
                                } else {
                                    budgetPersonsBean.setJobCode(appointmentsBean.getJobCode());
                                }
                                //Code commented for Coeus4.3 enhancement
                                //budgetPersonsBean.setJobCode(appointmentsBean.getJobCode());
                                //Code added for Coeus4.3 enhancements - ends
                                if(appointmentsBean.getAppointmentType() != null) {
                                    budgetPersonsBean.setAppointmentType(appointmentsBean.getAppointmentType());
                                }else{
                                    budgetPersonsBean.setAppointmentType(EMPTY_STRING);
                                }
                                 //Added for Case#2918 - Use of Salary Anniversary Date for calculating inflation in budget development module - Start
                                if(salaryAnniversaryDate !=null){
                                    budgetPersonsBean.setSalaryAnniversaryDate(salaryAnniversaryDate);
                                    budgetPersonsBean.setAw_SalaryAnniversaryDate(salaryAnniversaryDate);
                                }
                                //Added for Case#2918 - Use of Salary Anniversary Date for calculating inflation in budget development module - End
                            
                                budgetPersonsBean.setEffectiveDate(startDate);
                                //COEUSQA-1535-Access to institutionally maintained salaries in proposal budget - Start
                                //budgetPersonsBean.setCalculationBase(appointmentsBean.getSalary());
                                if(hasRightToViewInstitutionalSalaries(budgetInfoBean, personID)){
                                    budgetPersonsBean.setCalculationBase(appointmentsBean.getSalary());
                                }else{
                                    budgetPersonsBean.setCalculationBase(0.00);
                                }
                                //COEUSQA-1535-Access to institutionally maintained salaries in proposal budget - End
                                budgetPersonsBean.setAcType(TypeConstants.INSERT_RECORD);
                                //budgetPersonsBean.setRowId(rowID++);
                                modified = true;
                                budgetPersonTableModel.addRow(budgetPersonsBean);
                                budgetPersonTableModel.fireTableRowsInserted(budgetPersonTableModel.getRowCount(), budgetPersonTableModel.getRowCount());
                                
                                if(lastRow >= 0){
                                    budgetPersonsForm.tblBudgetPersons.setRowSelectionInterval(
                                    lastRow+1, lastRow+1);
                                    budgetPersonsForm.tblBudgetPersons.scrollRectToVisible(
                                    budgetPersonsForm.tblBudgetPersons.getCellRect(
                                    lastRow ,0, true));
                                }
                            }
                        }else{
                            //Has no appointment
                            
                            BudgetPersonsBean budgetPersonsBean = new BudgetPersonsBean();
                            
                            budgetPersonsBean.setAw_ProposalNumber(budgetInfoBean.getProposalNumber());
                            budgetPersonsBean.setAw_VersionNumber(budgetInfoBean.getVersionNumber());
                            budgetPersonsBean.setAw_PersonId(personID);
                            budgetPersonsBean.setAw_JobCode(getDefaultJobCode());
                            //Code Commeted for Coeus4.3 enhancement
                            //budgetPersonsBean.setAw_JobCode(EMPTY_STRING);
                            budgetPersonsBean.setAw_AppointmentType(EMPTY_STRING);
                            budgetPersonsBean.setAw_EffectiveDate(startDate);
                            budgetPersonsBean.setAw_CalculationBase(0.00);
                            
                            budgetPersonsBean.setProposalNumber(budgetInfoBean.getProposalNumber());
                            budgetPersonsBean.setVersionNumber(budgetInfoBean.getVersionNumber());
                            budgetPersonsBean.setPersonId(personID);
                            budgetPersonsBean.setFullName(personName);
                            budgetPersonsBean.setJobCode(getDefaultJobCode());
                            //Code Commeted for Coeus4.3 enhancement
                            //budgetPersonsBean.setJobCode(EMPTY_STRING);
                            //Added for Case 3869 - Save not working for budget person - start
                            //budgetPersonsBean.setAppointmentType(appointments[4]);
                            //COEUSQA-2036 Code Table Prop Dev Appt Type and Period Types - Start
                            //budgetPersonsBean.setAppointmentType(REG_EMPLOYEE);
                            budgetPersonsBean.setAppointmentType(setActiveAppointmentType());
                            //COEUSQA-2036 Code Table Prop Dev Appt Type and Period Types - End
                            //Added for Case 3869 - Save not working for budget person - end
                            //Added for Case#2918 - Use of Salary Anniversary Date for calculating inflation in budget development module - Start
                            if(salaryAnniversaryDate !=null){
                                budgetPersonsBean.setSalaryAnniversaryDate(salaryAnniversaryDate);
                                budgetPersonsBean.setAw_SalaryAnniversaryDate(salaryAnniversaryDate);
                            }     
                            //Added for Case#2918 - Use of Salary Anniversary Date for calculating inflation in budget development module - End
                            budgetPersonsBean.setEffectiveDate(startDate);
                            budgetPersonsBean.setCalculationBase(0.00);
                            budgetPersonsBean.setAcType(TypeConstants.INSERT_RECORD);
                            //budgetPersonsBean.setRowId(rowID++);
                            modified = true;
                            budgetPersonTableModel.addRow(budgetPersonsBean);
                            budgetPersonTableModel.fireTableRowsInserted(
                            budgetPersonTableModel.getRowCount(),
                            budgetPersonTableModel.getRowCount());
                        }
                        if(lastRow >= 0){
                            budgetPersonsForm.tblBudgetPersons.setRowSelectionInterval(
                            lastRow+1, lastRow+1 );
                            budgetPersonsForm.tblBudgetPersons.scrollRectToVisible(
                            budgetPersonsForm.tblBudgetPersons.getCellRect(
                            lastRow ,0, true));
                        }
                    }
                    
                    
                }
            }
        }catch(Exception exception){
            exception.getMessage();
        }
    }
    
    /*
     * Search for the Rolodex,
     */
    private void  searchRolodexAction(){
        try{
            int lastRow = budgetPersonsForm.tblBudgetPersons.getRowCount()-1;
            String formatedDate = dtUtils.formatDate(budgetStartDate.toString(),SIMPLE_DATE_FORMAT);
            long date =  new java.util.Date(formatedDate).getTime();
            java.sql.Date startDate = new java.sql.Date(date);
            String rolodexID = null;
            String rolodexName = null;
            //            edu.mit.coeus.utils.Utils Utils;
            int inIndex=budgetPersonsForm.tblBudgetPersons.getSelectedRow();
            if(budgetPersonsForm.tblBudgetPersons.isEditing()){
                java.awt.Component comp = budgetPersonsForm.tblBudgetPersons.getEditorComponent();
                String value = null;
                if (comp instanceof javax.swing.text.JTextComponent) {
                    value = ((javax.swing.text.JTextComponent)comp).getText();
                } else if (comp instanceof javax.swing.JComboBox) {
                    value = EMPTY_STRING;
                }
                
                if( (value != null)) {
                    budgetPersonsForm.tblBudgetPersons.setValueAt(value,inIndex,1);
                }
                budgetPersonsForm.tblBudgetPersons.getCellEditor().cancelCellEditing();
            }
            
            CoeusSearch coeusSearch = new CoeusSearch(mdiForm, "ROLODEXSEARCH",
            CoeusSearch.TWO_TABS_WITH_MULTIPLE_SELECTION );
            coeusSearch.showSearchWindow();
            
            Vector vSelectedRolodex = coeusSearch.getMultipleSelectedRows();
            
            if( vSelectedRolodex != null ){
                HashMap singleRolodexData = null;
                for(int indx = 0; indx < vSelectedRolodex.size(); indx++ ){
                    singleRolodexData = (HashMap)vSelectedRolodex.get( indx ) ;
                    if( singleRolodexData !=null ){
                        /* construct the full name of person */
                        
                        rolodexID = edu.mit.coeus.utils.Utils.convertNull(singleRolodexData.get( "ROLODEX_ID" ));//result.get( "ROLODEX_ID" ));
                        String firstName = edu.mit.coeus.utils.Utils.convertNull(singleRolodexData.get( "FIRST_NAME" ));//result.get( "FIRST_NAME" ));
                        String lastName = edu.mit.coeus.utils.Utils.convertNull(singleRolodexData.get( "LAST_NAME" ));//result.get( "LAST_NAME" ));
                        String middleName = edu.mit.coeus.utils.Utils.convertNull(singleRolodexData.get( "MIDDLE_NAME" ));//result.get( "MIDDLE_NAME" ));
                        //                        String namePreffix = edu.mit.coeus.utils.Utils.convertNull(singleRolodexData.get( "PREFIX" ));//result.get( "PREFIX" ));
                        //                        String nameSuffix = edu.mit.coeus.utils.Utils.convertNull(singleRolodexData.get( "SUFFIX" ));//result.get( "SUFFIX" ));
                        rolodexName = firstName+" "+middleName+" "+lastName;
                        
                        vecSearchPerson = getPersonInfo(rolodexID);
                        
                        if(vecSearchPerson!=null){
                            if(vecSearchPerson.size() > 1){
                                //Has more than one appointment
                                appointmentsForPersonForm = new AppointmentsForPersonForm(
                                mdiForm,true,rolodexID,rolodexName,vecSearchPerson);
                                AppointmentsBean appointmentsBean = appointmentsForPersonForm.display();
                                BudgetPersonsBean budgetPersonsBean = new BudgetPersonsBean();
                                budgetPersonsBean.setAw_ProposalNumber(budgetInfoBean.getProposalNumber());
                                budgetPersonsBean.setAw_VersionNumber(budgetInfoBean.getVersionNumber());
                                budgetPersonsBean.setAw_PersonId(rolodexID);
                                budgetPersonsBean.setAw_JobCode(appointmentsBean.getJobCode());
                                budgetPersonsBean.setAw_AppointmentType(appointmentsBean.getAppointmentType());
                                budgetPersonsBean.setAw_EffectiveDate(startDate);
                                //COEUSQA-1535-Access to institutionally maintained salaries in proposal budget - Start
                                //budgetPersonsBean.setAw_CalculationBase(appointmentsBean.getSalary());
                                if(hasRightToViewInstitutionalSalaries(budgetInfoBean, rolodexID)){
                                    budgetPersonsBean.setAw_CalculationBase(appointmentsBean.getSalary());
                                }else{
                                    budgetPersonsBean.setAw_CalculationBase(0.00);
                                }
                                //COEUSQA-1535-Access to institutionally maintained salaries in proposal budget - End
                                
                                budgetPersonsBean.setProposalNumber(budgetInfoBean.getProposalNumber());
                                budgetPersonsBean.setVersionNumber(budgetInfoBean.getVersionNumber());
                                budgetPersonsBean.setPersonId(rolodexID);
                                budgetPersonsBean.setFullName(rolodexName);
                                if(appointmentsBean.getJobCode()==null ||
                                appointmentsBean.getJobCode().equals(EMPTY_STRING)){
                                    budgetPersonsBean.setJobCode(getDefaultJobCode());
                                } else {
                                    budgetPersonsBean.setJobCode(appointmentsBean.getJobCode());
                                }
                                if(appointmentsBean.getAppointmentType() != null) {
                                    budgetPersonsBean.setAppointmentType(appointmentsBean.getAppointmentType());
                                }else{
                                    budgetPersonsBean.setAppointmentType(EMPTY_STRING);
                                }
                                budgetPersonsBean.setEffectiveDate(startDate);
                                //COEUSQA-1535-Access to institutionally maintained salaries in proposal budget - Start
                                //budgetPersonsBean.setCalculationBase(appointmentsBean.getSalary());
                                if(hasRightToViewInstitutionalSalaries(budgetInfoBean, rolodexID)){
                                    budgetPersonsBean.setCalculationBase(appointmentsBean.getSalary());
                                }else{
                                    budgetPersonsBean.setCalculationBase(0.00);
                                }
                                //COEUSQA-1535-Access to institutionally maintained salaries in proposal budget - End
                                budgetPersonsBean.setAcType(TypeConstants.INSERT_RECORD);
                                budgetPersonsBean.setNonEmployee(true);
                                modified = true;
                                budgetPersonTableModel.addRow(budgetPersonsBean);
                                budgetPersonTableModel.fireTableRowsInserted(budgetPersonTableModel.getRowCount(), budgetPersonTableModel.getRowCount());
                                if(lastRow >= 0){
                                    budgetPersonsForm.tblBudgetPersons.setRowSelectionInterval(
                                    lastRow+1,lastRow+1 );
                                    budgetPersonsForm.tblBudgetPersons.scrollRectToVisible(
                                    budgetPersonsForm.tblBudgetPersons.getCellRect(
                                    lastRow ,0, true));
                                }
                            }else if(vecSearchPerson.size() == 1){
                                //Has exactly one appointment or not
                                BudgetPersonsBean budgetPersonsBean = new BudgetPersonsBean();
                                AppointmentsBean appointmentsBean = (AppointmentsBean)vecSearchPerson.elementAt(0);
                                budgetPersonsBean.setAw_ProposalNumber(budgetInfoBean.getProposalNumber());
                                budgetPersonsBean.setAw_VersionNumber(budgetInfoBean.getVersionNumber());
                                budgetPersonsBean.setAw_PersonId(rolodexID);
                                budgetPersonsBean.setAw_JobCode(appointmentsBean.getJobCode());
                                budgetPersonsBean.setAw_AppointmentType(appointmentsBean.getAppointmentType());
                                budgetPersonsBean.setAw_EffectiveDate(startDate);
                                //COEUSQA-1535-Access to institutionally maintained salaries in proposal budget - Start
                                //budgetPersonsBean.setAw_CalculationBase(appointmentsBean.getSalary());
                                if(hasRightToViewInstitutionalSalaries(budgetInfoBean, rolodexID)){
                                    budgetPersonsBean.setAw_CalculationBase(appointmentsBean.getSalary());
                                }else{
                                    budgetPersonsBean.setAw_CalculationBase(0.00);
                                }
                                //COEUSQA-1535-Access to institutionally maintained salaries in proposal budget - End
                                budgetPersonsBean.setProposalNumber(budgetInfoBean.getProposalNumber());
                                budgetPersonsBean.setVersionNumber(budgetInfoBean.getVersionNumber());
                                budgetPersonsBean.setPersonId(rolodexID);
                                budgetPersonsBean.setFullName(rolodexName);
                                if(appointmentsBean.getJobCode()==null ||
                                appointmentsBean.getJobCode().equals(EMPTY_STRING)){
                                    budgetPersonsBean.setJobCode(getDefaultJobCode());
                                } else {
                                    budgetPersonsBean.setJobCode(appointmentsBean.getJobCode());
                                }
                                if(appointmentsBean.getAppointmentType() != null) {
                                    budgetPersonsBean.setAppointmentType(appointmentsBean.getAppointmentType());
                                }else{
                                    budgetPersonsBean.setAppointmentType(EMPTY_STRING);
                                }
                                budgetPersonsBean.setEffectiveDate(startDate);
                                //COEUSQA-1535-Access to institutionally maintained salaries in proposal budget - Start
                                //budgetPersonsBean.setCalculationBase(appointmentsBean.getSalary());
                                if(hasRightToViewInstitutionalSalaries(budgetInfoBean, rolodexID)){
                                    budgetPersonsBean.setCalculationBase(appointmentsBean.getSalary());
                                }else{
                                    budgetPersonsBean.setCalculationBase(0.00);
                                }
                                //COEUSQA-1535-Access to institutionally maintained salaries in proposal budget - End
                                budgetPersonsBean.setAcType(TypeConstants.INSERT_RECORD);
                                budgetPersonsBean.setNonEmployee(true);
                                modified = true;
                                budgetPersonTableModel.addRow(budgetPersonsBean);
                                budgetPersonTableModel.fireTableRowsInserted(budgetPersonTableModel.getRowCount(), budgetPersonTableModel.getRowCount());
                                
                                if(lastRow >= 0){
                                    budgetPersonsForm.tblBudgetPersons.setRowSelectionInterval(
                                    lastRow+1, lastRow+1);
                                    budgetPersonsForm.tblBudgetPersons.scrollRectToVisible(
                                    budgetPersonsForm.tblBudgetPersons.getCellRect(
                                    lastRow ,0, true));
                                }
                            }
                        }else{
                            //Has no appointment
                            
                            BudgetPersonsBean budgetPersonsBean = new BudgetPersonsBean();
                            
                            budgetPersonsBean.setAw_ProposalNumber(budgetInfoBean.getProposalNumber());
                            budgetPersonsBean.setAw_VersionNumber(budgetInfoBean.getVersionNumber());
                            budgetPersonsBean.setAw_PersonId(rolodexID);
                            budgetPersonsBean.setAw_JobCode(getDefaultJobCode());
                            budgetPersonsBean.setAw_AppointmentType(EMPTY_STRING);
                            budgetPersonsBean.setAw_EffectiveDate(startDate);
                            budgetPersonsBean.setAw_CalculationBase(0.00);
                            
                            budgetPersonsBean.setProposalNumber(budgetInfoBean.getProposalNumber());
                            budgetPersonsBean.setVersionNumber(budgetInfoBean.getVersionNumber());
                            budgetPersonsBean.setPersonId(rolodexID);
                            budgetPersonsBean.setFullName(rolodexName);
                            budgetPersonsBean.setJobCode(getDefaultJobCode());
                            //Modified for Case 3869 - Save not working for budget person - start
                            //budgetPersonsBean.setAppointmentType(appointments[4]);
                            //COEUSQA-2036 Code Table Prop Dev Appt Type and Period Types - Start
                            //budgetPersonsBean.setAppointmentType(REG_EMPLOYEE);
                            budgetPersonsBean.setAppointmentType(setActiveAppointmentType());
                            //COEUSQA-2036 Code Table Prop Dev Appt Type and Period Types - End
                            //Modified for Case 3869 - Save not working for budget person - start
                            budgetPersonsBean.setEffectiveDate(startDate);
                            budgetPersonsBean.setCalculationBase(0.00);
                            budgetPersonsBean.setAcType(TypeConstants.INSERT_RECORD);
                            budgetPersonsBean.setNonEmployee(true);
                            modified = true;
                            budgetPersonTableModel.addRow(budgetPersonsBean);
                            budgetPersonTableModel.fireTableRowsInserted(
                            budgetPersonTableModel.getRowCount(),
                            budgetPersonTableModel.getRowCount());
                        }
                        if(lastRow >= 0){
                            budgetPersonsForm.tblBudgetPersons.setRowSelectionInterval(
                            lastRow+1, lastRow+1 );
                            budgetPersonsForm.tblBudgetPersons.scrollRectToVisible(
                            budgetPersonsForm.tblBudgetPersons.getCellRect(
                            lastRow ,0, true));
                        }
                    }
                }
            }
        }catch(Exception exception){
            exception.getMessage();
        }
    }
    
    /*
     * Search for the TBA person,
     */
    public void searchTBAAction(){
        BudgetTBAPersonsController budgetTBAPersonsController =
        new BudgetTBAPersonsController(mdiForm);
        budgetTBAPersonsController.display();
        
        if(budgetTBAPersonsController.isOkClicked()){
            
            BudgetTBAPersonBean budgetTBAPersonBean =
            budgetTBAPersonsController.getSelectedTBAPersons();
            String personId = getTBAPersonId();
            String formatedDate = dtUtils.formatDate(budgetStartDate.toString(),SIMPLE_DATE_FORMAT);
            long date =  new java.util.Date(formatedDate).getTime();
            java.sql.Date startDate = new java.sql.Date(date);
            BudgetPersonsBean budgetPersonsBean = new BudgetPersonsBean();
            budgetPersonsBean.setAw_ProposalNumber(budgetInfoBean.getProposalNumber());
            budgetPersonsBean.setAw_VersionNumber(budgetInfoBean.getVersionNumber());
            budgetPersonsBean.setAw_PersonId(personId);
            if(budgetTBAPersonBean.getJobCode()==null ||
            budgetTBAPersonBean.getJobCode().equals(EMPTY_STRING)){
                budgetPersonsBean.setJobCode(getDefaultJobCode());
            } else {
                budgetPersonsBean.setJobCode(budgetTBAPersonBean.getJobCode());
            }
            budgetPersonsBean.setAw_JobCode(getDefaultJobCode());
            budgetPersonsBean.setAw_AppointmentType(EMPTY_STRING);
            budgetPersonsBean.setAw_EffectiveDate(startDate);
            budgetPersonsBean.setAw_CalculationBase(0.00);
            budgetPersonsBean.setProposalNumber(budgetInfoBean.getProposalNumber());
            budgetPersonsBean.setVersionNumber(budgetInfoBean.getVersionNumber());
            budgetPersonsBean.setPersonId(personId);
            budgetPersonsBean.setFullName(budgetTBAPersonBean.getName());
            if(budgetTBAPersonBean.getJobCode()==null ||
            budgetTBAPersonBean.getJobCode().equals(EMPTY_STRING)){
                budgetPersonsBean.setJobCode(getDefaultJobCode());
            } else {
                budgetPersonsBean.setJobCode(budgetTBAPersonBean.getJobCode());
            }
            //Added for Case 3869 - Save not working for budget person - start
            //budgetPersonsBean.setAppointmentType(appointments[4]);
            // 4493: While adding a TBA appointment type should be defaulted to 12 Months - Start
//            budgetPersonsBean.setAppointmentType(REG_EMPLOYEE);
            if(defaultTbaApntmntTypeDesc != null && !"".equals(defaultTbaApntmntTypeDesc)){
                budgetPersonsBean.setAppointmentType(defaultTbaApntmntTypeDesc);
            } else {
                //COEUSQA-2036 Code Table Prop Dev Appt Type and Period Types - Start
                //budgetPersonsBean.setAppointmentType(REG_EMPLOYEE);
                budgetPersonsBean.setAppointmentType(setActiveAppointmentType());
                //COEUSQA-2036 Code Table Prop Dev Appt Type and Period Types - End
            }
            // 4493: While adding a TBA appointment type should be defaulted to 12 Months - End
            //Added for Case 3869 - Save not working for budget person - end
            budgetPersonsBean.setEffectiveDate(startDate);
            budgetPersonsBean.setCalculationBase(0.00);
            budgetPersonsBean.setAcType(TypeConstants.INSERT_RECORD);
//            budgetPersonsBean.setNonEmployee(true);
            modified = true;
            budgetPersonTableModel.addRow(budgetPersonsBean);
            budgetPersonTableModel.fireTableRowsInserted(
            budgetPersonTableModel.getRowCount(),
            budgetPersonTableModel.getRowCount());
        }
    }
    
    /** Get the Persons Appointment Type. If more than one Appointment Type generate
     *Another form to select. Making a server side call.
     */
    
    public CoeusVector getPersonInfo(String personId) throws Exception{
        CoeusVector vctAppointments = null;
        requester = new RequesterBean();
        requester.setFunctionType(GET_APPOINTMENTS_FOR_PERSON);
        requester.setDataObject(personId);
        
        AppletServletCommunicator comm
        = new AppletServletCommunicator(connectTo, requester);
        
        comm.send();
        responder = comm.getResponse();
        if(responder.isSuccessfulResponse()){
            vctAppointments = (CoeusVector)responder.getDataObject();
            
        }
        return vctAppointments;
    }
    
    /** While saving to the base window, check for the data mofied, Duplucation and
     * Validate. If all these values returns true then only save the information to the
     * base window
     */
    private void OkPersonsAction(){
        try{
            if(modified){
                
                //                if(!checkDuplicatePerson()){
                //                    if(validate()){
                if(validate()){
                    if(!checkDuplicatePerson()){
                        setSaveRequired(true);
                        setModifiedPersons();
                        saveFormData();
                        closeDialog();
                    }
                }
            }else{
                closeDialog();
            }
            
        }catch(Exception exception){
            exception.printStackTrace();
        }
    }
    /** Specifies the cancel action, the validations are as followed earlier */
    private void cancelPersonsAction(){
        budgetPersonCellEditor.stopCellEditing();
        if(modified){
            confirmClosing();
        }else{
            //                dlgPersonBudget.setVisible(false);
            dlgPersonBudget.dispose();
        }
    }
    
    /** Confirm before closing the BudgetPersons dialog box */
    private void confirmClosing(){
        try{
            int option = CoeusOptionPane.showQuestionDialog(
            coeusMessageResources.parseMessageKey("saveConfirmCode.1002"),
            CoeusOptionPane.OPTION_YES_NO_CANCEL,CoeusOptionPane.DEFAULT_CANCEL);
            if(option == CoeusOptionPane.SELECTION_YES){
                
                //                if(!checkDuplicatePerson()){
                //                    if(validate()){
                if(validate()){
                    if(!checkDuplicatePerson()){
                        setSaveRequired(true);
                        setModifiedPersons();
                        saveFormData();
                        closeDialog();
                    }
                }
                
            }else if(option == CoeusOptionPane.SELECTION_NO){
                setSaveRequired(false);
                dlgPersonBudget.dispose();
                //                dlgPersonBudget.setVisible(false);
            }else if(option==CoeusOptionPane.SELECTION_CANCEL){
                return;
            }
        }catch(Exception exception){
            exception.getMessage();
        }
    }
    
    /** this method Closes this window
     */
    private void closeDialog() {
        dlgPersonBudget.dispose();
        //         dlgPersonBudget.setVisible(false);
    }
    
    /** displays the Form which is being controlled.
     */
    public void display(){
        postInitComponents();
        refresh();
        if(budgetPersonsForm.tblBudgetPersons.getRowCount() >0){
            budgetPersonsForm.tblBudgetPersons.setRowSelectionInterval(0,0);
        }
        budgetPersonCellEditor.stopCellEditing();
        
        /* JM 9-4-2015 added to set font color based on status 
    	edu.vanderbilt.coeus.gui.CustomTableCellRenderer renderer = 
    			new edu.vanderbilt.coeus.gui.CustomTableCellRenderer(0,"I",Color.RED, true, false, false, getFunctionType());
    	*/
    	PersonTableCellRenderer renderer = 
    		new PersonTableCellRenderer(HAND_ICON_COLUMN,IS_EXTERNAL_PERSON_COL,true,false,false,getFunctionType());
    	budgetPersonsForm.tblBudgetPersons.getColumnModel().getColumn(1).setCellRenderer(renderer);
    	/* JM END */
        
        modified=false;
        dlgPersonBudget.setVisible(true);
    }
    
    /** saves the Form Data.
     */
    public void saveFormData() {
        QueryEngine queryEngine = QueryEngine.getInstance();
        key = (getProposalNumber()+getVersionNumber());
        try {
            // Delete the persons details from the queryEngine
            if(vecDeletedPersons!=null && vecDeletedPersons.size() > 0){
                for(int index = 0; index < vecDeletedPersons.size(); index++) {
                    BudgetPersonsBean deletedBudgetPersonsBean  = (BudgetPersonsBean)vecDeletedPersons.get(index);
                    queryEngine.delete(key,deletedBudgetPersonsBean);
                }
            }
            
            //insert, update the records from the base window
            if (vecBudgetPersons != null && vecBudgetPersons.size() > 0) {
                int size = vecBudgetPersons.size();
                int index = 0;
                for(index = 0; index < size; index++) {
                    BudgetPersonsBean persBean = (BudgetPersonsBean) vecBudgetPersons.get(index);
                    if(persBean.getAcType()!=null){
                        if (persBean.getAcType().equals(TypeConstants.UPDATE_RECORD)) {
                            
                            //First delete the existing person and then insert the same. This is
                            //required since primary keys can be modified
                            persBean.setAcType(TypeConstants.DELETE_RECORD);
                            queryEngine.delete(key, persBean);
                            
                            persBean.setAcType(TypeConstants.INSERT_RECORD);
                            persBean.setRowId(rowID++);
                            queryEngine.insert(key, persBean);
                            
                        } else if (persBean.getAcType().equals(TypeConstants.INSERT_RECORD)) {
                            persBean.setRowId(rowID++);
                            queryEngine.insert(key, persBean);
                        }
                    }
                    
                }
            }
        } catch(Exception exception) {
            exception.getMessage();
        }
    }
    /** Table Model for the BudgetPersons
     */
    
    private class BudgetPersonTableModel extends AbstractTableModel implements TableModel{
        private BudgetPersonsBean budgetPersonsBean = null;
        private String colNames[];
        
        private Class colClass[];
        //COEUSQA-1559 Display calculated base salary amount on RR Budget form in out years - Start
        BudgetPersonTableModel(String proposalNumber,int versionNumber){
            loadPeriods(proposalNumber,versionNumber);
            loadColumns();
        }
        
        public void loadPeriods(String proposalNumber, int versionNumber){
            try {
                if(!getHierarchyForChildProposal(getProposalNumber())){
                    vecBudgetPeriods = QueryEngine.getInstance().executeQuery(key,BudgetPeriodBean.class, CoeusVector.FILTER_ACTIVE_BEANS);
                }else{
                    vecBudgetPeriods = getBudgetPeriodsForChildProposal(proposalNumber, versionNumber);
                }
            } catch (CoeusException ex) {
                ex.printStackTrace();
            }
        }
        
        public void loadColumns(){
            //To fetch the number of periods
            int numOfPeriods = vecBudgetPeriods.size();
            //to display only ten periods
            if(numOfPeriods > 10){
                numOfPeriods = 10;
            }
            //to calculate the number of columns for budget persons 
            
            int numOfColumns = 0;
            //Added for Case#2918 - Use of Salary Anniversary Date for calculating inflation in budget development module - starts
            if(showSalaryAnniversaryDate){
                numOfColumns = 7+numOfPeriods;               
            } else {
                numOfColumns = 6+numOfPeriods;
            }

            /* JM 4-4-2016 added column for external persons */
            numOfColumns = numOfColumns + 1;
            /* JM END */
            
            colNames = new String[numOfColumns];
            colClass = new Class[numOfColumns];
            //COEUSQA-1559 Display calculated base salary amount on RR Budget form in out years - End
            
            colNames[0] = "";
            colNames[1] = "Name";
            colNames[2] = "Job Code";
            colNames[3] = "Appointment Type";
            colNames[4] = "Eff Date";
            colNames[5] = "Calc Base";
            colClass[0] = ImageIcon.class;
            colClass[1] = String.class;
            colClass[2] = String.class;
            colClass[3] = String.class;
            colClass[4] = String.class;
            colClass[5] = Double.class;
            
            /* JM 4-4-2016 adding external persons class */
            colClass[IS_EXTERNAL_PERSON_COL] = String.class;
            colNames[IS_EXTERNAL_PERSON_COL] = "Is External Person?";
            /* JM END */
            
            /* JM 4-4-2016 increment col index from 6 to 7 to account for external persons col */
            if(showSalaryAnniversaryDate){
                colNames[SALARY_ANNIVERSARY_DATE_COLUMN] = "Anniv Date";
                colClass[SALARY_ANNIVERSARY_DATE_COLUMN ] = String.class;
            }
            /* JM END */
            
            //Added for Case#2918 - Use of Salary Anniversary Date for calculating inflation in budget development module - ends
            //Added for Case#2918 - Use of Salary Anniversary Date for calculating inflation in budget development module - starts
            //COEUSQA-1559 Display calculated base salary amount on RR Budget form in out years - Start
            //to display the period wise salary columns
            /* JM 4-4-2016 incremented start counts to 8 and 7 to account for new external persons col */
            if(showSalaryAnniversaryDate){
                int counter = 0;
                for(int count=8;count<numOfColumns;count++){
                    colNames[count] = "Base_salary_p"+(++counter);
                    colClass[count] = Double.class;
                }
            }else{
                int counter = 0;
                for(int count=7;count<numOfColumns;count++){
                    colNames[count] = "Base_salary_p"+(++counter);
                    colClass[count] = Double.class;
                }
            }
            /* JM END */
            //COEUSQA-1559 Display calculated base salary amount on RR Budget form in out years - End
        }
        
        public boolean isCellEditable(int row,int column){
            if(column==HAND_ICON_COLUMN) {
                return false;
            }else{
                return true;
            }
        }
        public int getColumnCount() {
            return colNames.length;
        }
        
        public void setData(Vector data){
            vecDataBean = data;
        }
        
        public int getRowCount() {
            if(vecDataBean==null)return 0;
            return vecDataBean.size();
        }
        
        public Class getColumnClass(int columnIndex) {
            return colClass [columnIndex];
        }
        
        public String getColumnName(int column) {
            return colNames[column];
        }
        
        public void addRow(BudgetPersonsBean budgetPersonsBean) {
            vecDataBean.add(budgetPersonsBean);
        }
        
        /** sets the row for the bean with the specified index
         * @param index
         * @param budgetPersonsBean
         */
        public void setRow(int index, BudgetPersonsBean budgetPersonsBean ) {
            vecDataBean.set(index, budgetPersonsBean);
        }
        
        /** gets the persons information with the specified index
         * @param index
         * @return
         */
        public BudgetPersonsBean getRow(int index) {
            return (BudgetPersonsBean)vecDataBean.get(index);
        }
        
        public Object getValueAt(int row, int column) {
            budgetPersonsBean = ( BudgetPersonsBean ) vecBudgetPersons.get( row );            
            switch(column){
                case HAND_ICON_COLUMN:
                	/* JM 9-7-2015 return person status value but won't be used or displayed */
                    //return EMPTY_STRING;
                	return budgetPersonsBean.getPersonStatus();
                	/* JM END */
                case NAME_COLUNM:
                    if(budgetPersonsBean.getFullName()==null || budgetPersonsBean.getFullName().equals(EMPTY_STRING)) {
                        return EMPTY_STRING;
                    }else{
               			return new String(budgetPersonsBean.getFullName());
                    }
                    
                case JOB_CODE_COLUMN:
                    if(budgetPersonsBean.getJobCode()==null || budgetPersonsBean.getJobCode().equals(EMPTY_STRING)) {
                        return EMPTY_STRING;
                    }else{
                        return new String(budgetPersonsBean.getJobCode());
                    }
                    
                case EFF_DATE_COLUMN:
                    if(budgetPersonsBean.getEffectiveDate() == null
                    ||budgetPersonsBean.getEffectiveDate().equals(EMPTY_STRING)){
                        return EMPTY_STRING;
                    }
                    else{
                        return budgetPersonsBean.getEffectiveDate();
                    }
                case CALC_BASE_COLUMN:
                    return new Double(budgetPersonsBean.getCalculationBase());
                case APPT_TYPE_COLUMN:
                    if(budgetPersonsBean.getAppointmentType()!=null){
                        return new String(budgetPersonsBean.getAppointmentType());
                    }else {
                        return EMPTY_STRING;
                    }
                /* JM 4-4-2016 external persons */
                case IS_EXTERNAL_PERSON_COL:
                    if(budgetPersonsBean.getIsExternalPerson()!=null){
                        return new String(budgetPersonsBean.getIsExternalPerson());
                    }else {
                        return EMPTY_STRING;
                    }
                /* JM END */
                //Added for Case#2918 - Use of Salary Anniversary Date for calculating inflation in budget development module - starts
                case SALARY_ANNIVERSARY_DATE_COLUMN:
                    //COEUSQA-1559 Display calculated base salary amount on RR Budget form in out years - Start
                    /*if(budgetPersonsBean.getSalaryAnniversaryDate() == null
                    ||budgetPersonsBean.getSalaryAnniversaryDate().equals(EMPTY_STRING)){
                        return EMPTY_STRING;
                    }
                    else{
                        return budgetPersonsBean.getSalaryAnniversaryDate();
                    }*/
                    if(showSalaryAnniversaryDate){
                        if(budgetPersonsBean.getSalaryAnniversaryDate() == null
                                ||budgetPersonsBean.getSalaryAnniversaryDate().equals(EMPTY_STRING)){
                            return EMPTY_STRING;
                        } else{
                            return budgetPersonsBean.getSalaryAnniversaryDate();
                        }
                    }else{
                        return new Double(budgetPersonsBean.getBaseSalaryP1());
                    }
                    //COEUSQA-1559 Display calculated base salary amount on RR Budget form in out years - End
                //Added for Case#2918 - Use of Salary Anniversary Date for calculating inflation in budget development module - ends
                //COEUSQA-1559 Display calculated base salary amount on RR Budget form in out years - Start
                case BASE_SALARY_P1_COLUMN:
                    if(showSalaryAnniversaryDate){
                        return new Double(budgetPersonsBean.getBaseSalaryP1());
                    }else{
                        return new Double(budgetPersonsBean.getBaseSalaryP2());
                    }
                case BASE_SALARY_P2_COLUMN:                  
                    if(showSalaryAnniversaryDate){
                        return new Double(budgetPersonsBean.getBaseSalaryP2());
                    }else{
                        return new Double(budgetPersonsBean.getBaseSalaryP3());
                    }
                case BASE_SALARY_P3_COLUMN:                  
                    if(showSalaryAnniversaryDate){
                        return new Double(budgetPersonsBean.getBaseSalaryP3());
                    }else{
                        return new Double(budgetPersonsBean.getBaseSalaryP4());
                    }
                case BASE_SALARY_P4_COLUMN:                  
                    if(showSalaryAnniversaryDate){
                        return new Double(budgetPersonsBean.getBaseSalaryP4());
                    }else{
                        return new Double(budgetPersonsBean.getBaseSalaryP5());
                    }
                case BASE_SALARY_P5_COLUMN:                  
                    if(showSalaryAnniversaryDate){
                        return new Double(budgetPersonsBean.getBaseSalaryP5());
                    }else{
                        return new Double(budgetPersonsBean.getBaseSalaryP6());
                    }
                case BASE_SALARY_P6_COLUMN:                  
                    if(showSalaryAnniversaryDate){
                        return new Double(budgetPersonsBean.getBaseSalaryP6());
                    }else{
                        return new Double(budgetPersonsBean.getBaseSalaryP7());
                    }
                 case BASE_SALARY_P7_COLUMN:                  
                    if(showSalaryAnniversaryDate){
                        return new Double(budgetPersonsBean.getBaseSalaryP7());
                    }else{
                        return new Double(budgetPersonsBean.getBaseSalaryP8());
                    }
                 case BASE_SALARY_P8_COLUMN:                  
                    if(showSalaryAnniversaryDate){
                        return new Double(budgetPersonsBean.getBaseSalaryP8());
                    }else{
                        return new Double(budgetPersonsBean.getBaseSalaryP9());
                    }
                 case BASE_SALARY_P9_COLUMN:                  
                    if(showSalaryAnniversaryDate){
                        return new Double(budgetPersonsBean.getBaseSalaryP9());
                    }else{
                        return new Double(budgetPersonsBean.getBaseSalaryP10());
                    }
                 case BASE_SALARY_P10_COLUMN:                  
                	 return new Double(budgetPersonsBean.getBaseSalaryP10());
                //COEUSQA-1559 Display calculated base salary amount on RR Budget form in out years - End
            }
            return EMPTY_STRING;
        }
        
        public void setValueAt(Object value, int row, int column){
            BudgetPersonsBean budgetPersonsBean = (BudgetPersonsBean)vecDataBean.get(row);            
            Date date;
            String mesg = null;
            String strDate;
            double cost;
            switch(column){
                case NAME_COLUNM:
                    if(budgetPersonsBean.getFullName()!=null && budgetPersonsBean.getFullName().equals(value.toString())) return ;
                    if(value == null || value.toString().equals(EMPTY_STRING)) {
                        budgetPersonsBean.setFullName(EMPTY_STRING);
                        budgetPersonsBean.setJobCode(EMPTY_STRING);
                        //Added for Case 3869 - Save not working for budget person - start
                        //budgetPersonsBean.setAppointmentType(appointments[4]);
                        //COEUSQA-2036 Code Table Prop Dev Appt Type and Period Types - Start
                        //budgetPersonsBean.setAppointmentType(REG_EMPLOYEE);
                        budgetPersonsBean.setAppointmentType(setActiveAppointmentType());
                        //COEUSQA-2036 Code Table Prop Dev Appt Type and Period Types - End
                        //Added for Case 3869 - Save not working for budget person - end
                        budgetPersonsBean.setCalculationBase(0);
                    }else{
                        getValidPersons(value.toString().trim());
                    }
                    break;
                case JOB_CODE_COLUMN:
                    if(budgetPersonsBean.getJobCode().equals(value.toString())) break;
                    budgetPersonsBean.setJobCode(value.toString().trim());
                    modified = true;
                    break;
                case EFF_DATE_COLUMN:
                    try{
                        if (value.toString().trim().length() > 0) {
                            strDate = dtUtils.formatDate(
                            value.toString().trim(), DATE_SEPARATERS, REQUIRED_DATE_FORMAT);
                        } else {
                            return;
                        }
                        
                        strDate = dtUtils.restoreDate(strDate, DATE_SEPARATERS);
                        if(strDate==null) {
                            throw new CoeusException();
                        }
                        date = dtFormat.parse(strDate.trim());
                    }catch (ParseException parseException) {
                        parseException.getMessage();
                        mesg = coeusMessageResources.parseMessageKey(
                        INVALID_EFFECTIVE_DATE);
                        CoeusOptionPane.showErrorDialog(mesg);
                        return ;
                    }
                    catch (CoeusException coeusException) {
                        mesg = coeusMessageResources.parseMessageKey(
                        INVALID_EFFECTIVE_DATE);
                        CoeusOptionPane.showErrorDialog(mesg);
                        return ;
                    }
                    if(budgetPersonsBean.getEffectiveDate().equals(date)) break;
                    budgetPersonsBean.setEffectiveDate(new java.sql.Date(date.getTime()));
                    modified=true;
                    break;
                case CALC_BASE_COLUMN:
                    if(value==null)return;
                    cost = new Double(value.toString()).doubleValue();
                    if(budgetPersonsBean.getCalculationBase()== cost) break;
                    budgetPersonsBean.setCalculationBase(cost);
                    modified = true;
                    break;
                case APPT_TYPE_COLUMN:
                    if(value == null)return;
                    if(budgetPersonsBean.getAppointmentType()!= null &&
                    budgetPersonsBean.getAppointmentType().equals(value.toString())) break;
                    
                    budgetPersonsBean.setAppointmentType(value.toString());
                    modified = true;
                    break;
                //Added for Case#2918 - Use of Salary Anniversary Date for calculating inflation in budget development module - starts
                case SALARY_ANNIVERSARY_DATE_COLUMN:
                //COEUSQA-1559 Display calculated base salary amount on RR Budget form in out years - Start
                    if(showSalaryAnniversaryDate){
                        try{
                            if (value.toString().trim().length() > 0) {
                                strDate = dtUtils.formatDate(
                                        value.toString().trim(), DATE_SEPARATERS, REQUIRED_DATE_FORMAT);
                            } else {
                                budgetPersonsBean.setSalaryAnniversaryDate(null);
                                modified=true;
                                return;
                            }
                            
                            strDate = dtUtils.restoreDate(strDate, DATE_SEPARATERS);
                            if(strDate==null) {
                                throw new CoeusException();
                            }
                            date = dtFormat.parse(strDate.trim());
                        }catch (ParseException parseException) {
                            parseException.getMessage();
                            mesg = coeusMessageResources.parseMessageKey(
                                    INVALID_SALARY_ANNIVERSARY_DATE);
                            CoeusOptionPane.showErrorDialog(mesg);
                            return ;
                        } catch (CoeusException coeusException) {
                            mesg = coeusMessageResources.parseMessageKey(
                                    INVALID_SALARY_ANNIVERSARY_DATE);
                            CoeusOptionPane.showErrorDialog(mesg);
                            return ;
                        }
                        if(budgetPersonsBean.getSalaryAnniversaryDate() != null
                                && budgetPersonsBean.getSalaryAnniversaryDate().equals(date)) break;
                        budgetPersonsBean.setSalaryAnniversaryDate(new java.sql.Date(date.getTime()));
                        modified=true;
                    }else{
                        if(value==null)return;
                        cost = new Double(value.toString()).doubleValue();
                        if(budgetPersonsBean.getBaseSalaryP1()== cost) break;
                        budgetPersonsBean.setBaseSalaryP1(cost);
                        modified = true;
                    }
                    break;
                case BASE_SALARY_P1_COLUMN:
                    if(showSalaryAnniversaryDate){
                        if(value==null)return;
                        cost = new Double(value.toString()).doubleValue();
                        if(budgetPersonsBean.getBaseSalaryP1()== cost) break;
                        budgetPersonsBean.setBaseSalaryP1(cost);
                        modified = true;
                    }else{
                        if(value==null)return;
                        cost = new Double(value.toString()).doubleValue();
                        if(budgetPersonsBean.getBaseSalaryP2()== cost) break;
                        budgetPersonsBean.setBaseSalaryP2(cost);
                        modified = true;
                    }
                    break;
                case BASE_SALARY_P2_COLUMN:    
                    if(showSalaryAnniversaryDate){
                        if(value==null)return;
                        cost = new Double(value.toString()).doubleValue();
                        if(budgetPersonsBean.getBaseSalaryP2()== cost) break;
                        budgetPersonsBean.setBaseSalaryP2(cost);
                        modified = true;
                    }else{
                        if(value==null)return;
                        cost = new Double(value.toString()).doubleValue();
                        if(budgetPersonsBean.getBaseSalaryP3()== cost) break;
                        budgetPersonsBean.setBaseSalaryP3(cost);
                        modified = true;
                    }
                    break;
                case BASE_SALARY_P3_COLUMN:
                    if(showSalaryAnniversaryDate){
                        if(value==null)return;
                        cost = new Double(value.toString()).doubleValue();
                        if(budgetPersonsBean.getBaseSalaryP3()== cost) break;
                        budgetPersonsBean.setBaseSalaryP3(cost);
                        modified = true;
                    }else{
                        if(value==null)return;
                        cost = new Double(value.toString()).doubleValue();
                        if(budgetPersonsBean.getBaseSalaryP4()== cost) break;
                        budgetPersonsBean.setBaseSalaryP4(cost);
                        modified = true;
                    }
                    break;
                case BASE_SALARY_P4_COLUMN:
                   if(showSalaryAnniversaryDate){
                        if(value==null)return;
                        cost = new Double(value.toString()).doubleValue();
                        if(budgetPersonsBean.getBaseSalaryP4()== cost) break;
                        budgetPersonsBean.setBaseSalaryP4(cost);
                        modified = true;
                    }else{
                        if(value==null)return;
                        cost = new Double(value.toString()).doubleValue();
                        if(budgetPersonsBean.getBaseSalaryP5()== cost) break;
                        budgetPersonsBean.setBaseSalaryP5(cost);
                        modified = true;
                    }
                    break;
                case BASE_SALARY_P5_COLUMN:
                    if(showSalaryAnniversaryDate){
                        if(value==null)return;
                        cost = new Double(value.toString()).doubleValue();
                        if(budgetPersonsBean.getBaseSalaryP5()== cost) break;
                        budgetPersonsBean.setBaseSalaryP5(cost);
                        modified = true;
                    }else{
                        if(value==null)return;
                        cost = new Double(value.toString()).doubleValue();
                        if(budgetPersonsBean.getBaseSalaryP6()== cost) break;
                        budgetPersonsBean.setBaseSalaryP6(cost);
                        modified = true;
                    }
                    break;
                case BASE_SALARY_P6_COLUMN:
                    if(showSalaryAnniversaryDate){
                        if(value==null)return;
                        cost = new Double(value.toString()).doubleValue();
                        if(budgetPersonsBean.getBaseSalaryP6()== cost) break;
                        budgetPersonsBean.setBaseSalaryP6(cost);
                        modified = true;
                    }else{
                        if(value==null)return;
                        cost = new Double(value.toString()).doubleValue();
                        if(budgetPersonsBean.getBaseSalaryP7()== cost) break;
                        budgetPersonsBean.setBaseSalaryP7(cost);
                        modified = true;
                    }
                    break;
                case BASE_SALARY_P7_COLUMN:
                    if(showSalaryAnniversaryDate){
                        if(value==null)return;
                        cost = new Double(value.toString()).doubleValue();
                        if(budgetPersonsBean.getBaseSalaryP7()== cost) break;
                        budgetPersonsBean.setBaseSalaryP7(cost);
                        modified = true;
                    }else{
                        if(value==null)return;
                        cost = new Double(value.toString()).doubleValue();
                        if(budgetPersonsBean.getBaseSalaryP8()== cost) break;
                        budgetPersonsBean.setBaseSalaryP8(cost);
                        modified = true;
                    }
                    break;
                case BASE_SALARY_P8_COLUMN:
                    if(showSalaryAnniversaryDate){
                        if(value==null)return;
                        cost = new Double(value.toString()).doubleValue();
                        if(budgetPersonsBean.getBaseSalaryP8()== cost) break;
                        budgetPersonsBean.setBaseSalaryP8(cost);
                        modified = true;
                    }else{
                        if(value==null)return;
                        cost = new Double(value.toString()).doubleValue();
                        if(budgetPersonsBean.getBaseSalaryP9()== cost) break;
                        budgetPersonsBean.setBaseSalaryP9(cost);
                        modified = true;
                    }
                    break;
                case BASE_SALARY_P9_COLUMN:
                    if(showSalaryAnniversaryDate){
                        if(value==null)return;
                        cost = new Double(value.toString()).doubleValue();
                        if(budgetPersonsBean.getBaseSalaryP9()== cost) break;
                        budgetPersonsBean.setBaseSalaryP9(cost);
                        modified = true;
                    }else{
                        if(value==null)return;
                        cost = new Double(value.toString()).doubleValue();
                        if(budgetPersonsBean.getBaseSalaryP10()== cost) break;
                        budgetPersonsBean.setBaseSalaryP10(cost);
                        modified = true;
                    }
                    break;
                case BASE_SALARY_P10_COLUMN:
                    if(showSalaryAnniversaryDate){
                        if(value==null)return;
                        cost = new Double(value.toString()).doubleValue();
                        if(budgetPersonsBean.getBaseSalaryP10()== cost) break;
                        budgetPersonsBean.setBaseSalaryP10(cost);
                        modified = true;
                    }
                    break;
                //COEUSQA-1559 Display calculated base salary amount on RR Budget form in out years - End
            }
            //Added for Case#2918 - Use of Salary Anniversary Date for calculating inflation in budget development module - ends
            //            edu.mit.coeus.gui.event.BeanEvent beanEvent = new edu.mit.coeus.gui.event.BeanEvent();
            //            beanEvent.setBean(budgetPersonsBean);
            //            fireBeanUpdated(beanEvent);
            
            budgetPersonTableModel.fireTableRowsUpdated(row,row);
        }
    }
    
    
    /** Display only the valid persons with their appointment types. */
    private void  getValidPersons(String name){
        try{
            String validPerson =  null;
            //java.sql.Date startDate;
            String formatedDate = dtUtils.formatDate(budgetStartDate.toString(),
            REQUIRED_DATE_FORMAT);
            long dates =  new java.util.Date(formatedDate).getTime();
            java.sql.Date effDate = new java.sql.Date(dates);
            int selRow = budgetPersonsForm.tblBudgetPersons.getSelectedRow();
            PersonInfoFormBean personInfoFormBean;
            //validPerson = getValidPersonInfo(name);
            personInfoFormBean = getValidPersonInfo(name);
            
            CoeusVector vecData = null ;
            //if(validPerson!=null && !validPerson.equals(EMPTY_STRING)){
            if(personInfoFormBean!=null){
                validPerson = personInfoFormBean.getPersonID();
                name = personInfoFormBean.getFullName();
                //Added for Case#2918 - Use of Salary Anniversary Date for calculating inflation in budget development module - start
                java.sql.Date salaryAnnvDate = (java.sql.Date) personInfoFormBean.getSalaryAnniversaryDate();
                //Added for Case#2918 - Use of Salary Anniversary Date for calculating inflation in budget development module - end
                vecData = getPersonInfo(validPerson);
                if(vecData!=null){
                    if(vecData.size()>1){
                        appointmentsForPersonForm = new AppointmentsForPersonForm(
                        mdiForm,true,validPerson,name,vecData);
                        
                        BudgetPersonsBean budgetPersonsBean = budgetPersonTableModel.getRow(selRow);
                        if(budgetPersonsBean.getAppointmentType() != null && !budgetPersonsBean.getAppointmentType().equals(EMPTY_STRING)) return ;
                        AppointmentsBean appointmentsBean = appointmentsForPersonForm.display();
                        budgetPersonsBean.setAw_ProposalNumber(budgetInfoBean.getProposalNumber());
                        budgetPersonsBean.setAw_VersionNumber(budgetInfoBean.getVersionNumber());
                        budgetPersonsBean.setAw_PersonId(validPerson);
                        budgetPersonsBean.setAw_JobCode(appointmentsBean.getJobCode());
                        budgetPersonsBean.setAw_AppointmentType(appointmentsBean.getAppointmentType());
                        budgetPersonsBean.setAw_EffectiveDate(effDate);
                        //COEUSQA-1535-Access to institutionally maintained salaries in proposal budget - Start
                        //budgetPersonsBean.setAw_CalculationBase(appointmentsBean.getSalary());
                        if(hasRightToViewInstitutionalSalaries(budgetInfoBean, validPerson)){
                            budgetPersonsBean.setAw_CalculationBase(appointmentsBean.getSalary());
                        }else{
                            budgetPersonsBean.setAw_CalculationBase(0.00);
                        }
                        //COEUSQA-1535-Access to institutionally maintained salaries in proposal budget - End
                        budgetPersonsBean.setProposalNumber(budgetInfoBean.getProposalNumber());
                        budgetPersonsBean.setVersionNumber(budgetInfoBean.getVersionNumber());
                        budgetPersonsBean.setPersonId(validPerson);
                        budgetPersonsBean.setFullName(name);
                        //Code added for Coeus4.3 enhancements - starts
                        if(appointmentsBean.getJobCode()==null ||
                        appointmentsBean.getJobCode().equals(EMPTY_STRING)){
                            budgetPersonsBean.setJobCode(getDefaultJobCode());
                        } else {
                            budgetPersonsBean.setJobCode(appointmentsBean.getJobCode());
                        }
                        //Code commented for Coeus4.3 enhancement
                        //budgetPersonsBean.setJobCode(appointmentsBean.getJobCode());
                        //Code added for Coeus4.3 enhancements - ends
                        if(appointmentsBean.getAppointmentType() != null) {
                            budgetPersonsBean.setAppointmentType(appointmentsBean.getAppointmentType());
                        }else{
                            budgetPersonsBean.setAppointmentType(EMPTY_STRING);
                        }
                        //COEUSQA-1535-Access to institutionally maintained salaries in proposal budget - Start
                        //budgetPersonsBean.setCalculationBase(appointmentsBean.getSalary());
                        if(hasRightToViewInstitutionalSalaries(budgetInfoBean, validPerson)){
                            budgetPersonsBean.setCalculationBase(appointmentsBean.getSalary());
                        }else{
                            budgetPersonsBean.setCalculationBase(0.00);
                        }
                        //COEUSQA-1535-Access to institutionally maintained salaries in proposal budget - End
                        budgetPersonsBean.setAcType(TypeConstants.INSERT_RECORD);
                        //budgetPersonsBean.setAcType(TypeConstants.UPDATE_RECORD);
                        //budgetPersonsBean.setRowId(getMaxRowID());
                        modified = true;
                        budgetPersonTableModel.setRow(selRow, budgetPersonsBean);
                        budgetPersonTableModel.fireTableRowsUpdated(selRow, selRow);
                        
                        if(selRow>0){
                            budgetPersonsForm.tblBudgetPersons.setRowSelectionInterval( selRow, selRow );
                            budgetPersonsForm.tblBudgetPersons.scrollRectToVisible(
                            budgetPersonsForm.tblBudgetPersons.getCellRect(
                            selRow ,0, true));
                        }
                        
                    }else if(vecData.size()==1){
                        BudgetPersonsBean budgetPersonsBean = budgetPersonTableModel.getRow(selRow);
                        AppointmentsBean appointmentsBean = (AppointmentsBean)vecData.get(0);
                        budgetPersonsBean.setAw_ProposalNumber(budgetInfoBean.getProposalNumber());
                        budgetPersonsBean.setAw_VersionNumber(budgetInfoBean.getVersionNumber());
                        budgetPersonsBean.setAw_PersonId(validPerson);
                        budgetPersonsBean.setAw_JobCode(appointmentsBean.getJobCode());
                        budgetPersonsBean.setAw_AppointmentType(appointmentsBean.getAppointmentType());
                        budgetPersonsBean.setAw_EffectiveDate(effDate);
                        //COEUSQA-1535-Access to institutionally maintained salaries in proposal budget - Start
                        //budgetPersonsBean.setAw_CalculationBase(appointmentsBean.getSalary());
                        if(hasRightToViewInstitutionalSalaries(budgetInfoBean, validPerson)){
                            budgetPersonsBean.setAw_CalculationBase(appointmentsBean.getSalary());
                        }else{
                            budgetPersonsBean.setAw_CalculationBase(0.00);
                        }
                        //COEUSQA-1535-Access to institutionally maintained salaries in proposal budget - End
                        
                        budgetPersonsBean.setProposalNumber(budgetInfoBean.getProposalNumber());
                        budgetPersonsBean.setVersionNumber(budgetInfoBean.getVersionNumber());
                        budgetPersonsBean.setPersonId(validPerson);
                        budgetPersonsBean.setFullName(name);
                        //Code added for Coeus4.3 enhancements - starts
                        if(appointmentsBean.getJobCode()==null ||
                        appointmentsBean.getJobCode().equals(EMPTY_STRING)){
                            budgetPersonsBean.setJobCode(getDefaultJobCode());
                        } else {
                            budgetPersonsBean.setJobCode(appointmentsBean.getJobCode());
                        }
                        //Code commented for Coeus4.3 enhancement
                        //budgetPersonsBean.setJobCode(appointmentsBean.getJobCode());
                        //Code added for Coeus4.3 enhancements - ends
                        if(appointmentsBean.getAppointmentType() != null) {
                            budgetPersonsBean.setAppointmentType(appointmentsBean.getAppointmentType());
                        }else{
                            budgetPersonsBean.setAppointmentType(EMPTY_STRING);
                        }
                        //Added for Case#2918 - Use of Salary Anniversary Date for calculating inflation in budget development module - start
                        if(salaryAnnvDate !=null ){
                            budgetPersonsBean.setSalaryAnniversaryDate(salaryAnnvDate);
                            budgetPersonsBean.setAw_SalaryAnniversaryDate(salaryAnnvDate);
                        }else{
                           budgetPersonsBean.setSalaryAnniversaryDate(null); 
                        }
                        //Added for Case#2918 - Use of Salary Anniversary Date for calculating inflation in budget development module - end
                        budgetPersonsBean.setEffectiveDate(effDate);
                        //COEUSQA-1535-Access to institutionally maintained salaries in proposal budget - Start
                        //budgetPersonsBean.setCalculationBase(appointmentsBean.getSalary());
                        if(hasRightToViewInstitutionalSalaries(budgetInfoBean, validPerson)){
                            budgetPersonsBean.setCalculationBase(appointmentsBean.getSalary());
                        }else{
                            budgetPersonsBean.setCalculationBase(0.00);
                        }
                        //COEUSQA-1535-Access to institutionally maintained salaries in proposal budget - End
                        budgetPersonsBean.setAcType(TypeConstants.INSERT_RECORD);
                        modified = true;
                        budgetPersonTableModel.setRow(selRow, budgetPersonsBean);
                        budgetPersonTableModel.fireTableRowsUpdated(selRow, selRow);
                        if(selRow>0){
                            budgetPersonsForm.tblBudgetPersons.setRowSelectionInterval( selRow, selRow );
                            budgetPersonsForm.tblBudgetPersons.scrollRectToVisible(
                            budgetPersonsForm.tblBudgetPersons.getCellRect(
                            selRow ,0, true));
                        }
                    }
                }else {
                    BudgetPersonsBean budgetPersonsBean = budgetPersonTableModel.getRow(selRow);
                    budgetPersonsBean.setAw_ProposalNumber(budgetInfoBean.getProposalNumber());
                    budgetPersonsBean.setAw_VersionNumber(budgetInfoBean.getVersionNumber());
                    budgetPersonsBean.setAw_PersonId(validPerson);
                    budgetPersonsBean.setAw_JobCode(EMPTY_STRING);
                    budgetPersonsBean.setAw_AppointmentType(EMPTY_STRING);
                    budgetPersonsBean.setAw_EffectiveDate(effDate);
                    budgetPersonsBean.setAw_CalculationBase(0.00);
                    budgetPersonsBean.setProposalNumber(budgetInfoBean.getProposalNumber());
                    budgetPersonsBean.setVersionNumber(budgetInfoBean.getVersionNumber());
                    budgetPersonsBean.setPersonId(validPerson);
                    budgetPersonsBean.setFullName(name);
                    budgetPersonsBean.setJobCode(EMPTY_STRING);
                    //Added for Case 3869 - Save not working for budget person - start
                    //budgetPersonsBean.setAppointmentType(appointments[4]);
                    //COEUSQA-2036 Code Table Prop Dev Appt Type and Period Types - Start
                    //budgetPersonsBean.setAppointmentType(REG_EMPLOYEE);
                    budgetPersonsBean.setAppointmentType(setActiveAppointmentType());
                    //COEUSQA-2036 Code Table Prop Dev Appt Type and Period Types - End
                    //Added for Case 3869 - Save not working for budget person - end
                    //Added for Case#2918 - Use of Salary Anniversary Date for calculating inflation in budget development module - start
                    if(salaryAnnvDate !=null ){
                        budgetPersonsBean.setSalaryAnniversaryDate(salaryAnnvDate);
                        budgetPersonsBean.setAw_SalaryAnniversaryDate(salaryAnnvDate);
                    }else{
                           budgetPersonsBean.setSalaryAnniversaryDate(null); 
                        }
                    //Added for Case#2918 - Use of Salary Anniversary Date for calculating inflation in budget development module - end
                       
                    budgetPersonsBean.setEffectiveDate(effDate);
                    budgetPersonsBean.setCalculationBase(0.00);
                    budgetPersonsBean.setAcType(TypeConstants.INSERT_RECORD);
                    //budgetPersonsBean.setAcType(TypeConstants.UPDATE_RECORD);
                    //budgetPersonsBean.setRowId(getMaxRowID());
                    modified = true;
                    budgetPersonTableModel.setRow(selRow, budgetPersonsBean);
                    budgetPersonTableModel.fireTableRowsUpdated(selRow, selRow);
                }// End Else
            }else{
                //Invalid message
                CoeusOptionPane.showInfoDialog(
                coeusMessageResources.parseMessageKey(
                INVALID_NAME));
            }
        }catch(Exception exception){
            exception.getMessage();
        }
    }
    
    
    /** This is Iconrendere to display HAND icon for the selected row in the table
     */
    static class IconRenderer  extends DefaultTableCellRenderer {
        
        /** This holds the Image Icon of Hand Icon
         */
        private final ImageIcon HAND_ICON =
        new ImageIcon(getClass().getClassLoader().getResource(CoeusGuiConstants.HAND_ICON));
        private final ImageIcon EMPTY_ICON = null;
        /** Default Constructor*/
        IconRenderer() {
        }
        
        public Component getTableCellRendererComponent(JTable table,
        Object value, boolean isSelected, boolean hasFocus, int row,
        int column) {
            
        	/* JM 9-7-2015 using this column to set person status so hiding text */
            //setText((String)value);
        	/* JM END */
        	
            setOpaque(false);
            /* if row is selected the place the icon in this cell wherever this
               renderer is used. */
            if( isSelected ){
                setIcon(HAND_ICON);
            }else{
                setIcon(EMPTY_ICON);
            }
            return this;
        }
        
    }//End Icon Rendering inner class
    
    
    /**
     * Inner class which is used to provide empty header for the Icon Column.
     */
    
    class EmptyHeaderRenderer extends JList implements TableCellRenderer {
        /**
         * Default constructor to set the default foreground/background
         * and border properties of this renderer for a cell.
         */
        EmptyHeaderRenderer() {
            setOpaque(true);
            setForeground(UIManager.getColor("TableHeader.foreground"));
            setBackground(UIManager.getColor("TableHeader.background"));
            setBorder(new javax.swing.border.EmptyBorder(0, 0, 0, 0));
            ListCellRenderer renderer = getCellRenderer();
            ((JLabel) renderer).setHorizontalAlignment(JLabel.CENTER);
            setCellRenderer(renderer);
        }
        
        public Component getTableCellRendererComponent(JTable table,
        Object value,boolean isSelected, boolean hasFocus, int row, int column){
            return this;
        }
    }
    
    /** An Inner class of the BudgetPersonController. Specifies the renderer for the
     * table component
     */
    private class BudgetPersonRenderer extends DefaultTableCellRenderer{
        
        private JTextField txtComponent;
        private DollarCurrencyTextField txtBaseComponent;
        //COEUSQA-1559 Display calculated base salary amount on RR Budget form in out years - Start
        private DollarCurrencyTextField txtBaseP1Component;
        private DollarCurrencyTextField txtBaseP2Component;
        private DollarCurrencyTextField txtBaseP3Component;
        private DollarCurrencyTextField txtBaseP4Component;
        private DollarCurrencyTextField txtBaseP5Component;
        private DollarCurrencyTextField txtBaseP6Component;
        private DollarCurrencyTextField txtBaseP7Component;
        private DollarCurrencyTextField txtBaseP8Component;
        private DollarCurrencyTextField txtBaseP9Component;
        private DollarCurrencyTextField txtBaseP10Component;
        //COEUSQA-1559 Display calculated base salary amount on RR Budget form in out years - End
        /** */
        BudgetPersonRenderer(){
            txtComponent = new JTextField();
            txtComponent.setBorder(new EmptyBorder(0,0,0,0));
            txtBaseComponent = new DollarCurrencyTextField();
            txtBaseComponent.setBorder(new EmptyBorder(0,0,0,0));
            //COEUSQA-1559 Display calculated base salary amount on RR Budget form in out years - Start
            txtBaseP1Component = new DollarCurrencyTextField();
            txtBaseP1Component.setBorder(new EmptyBorder(0,0,0,0));
            txtBaseP2Component = new DollarCurrencyTextField();
            txtBaseP2Component.setBorder(new EmptyBorder(0,0,0,0));
            txtBaseP3Component = new DollarCurrencyTextField();
            txtBaseP3Component.setBorder(new EmptyBorder(0,0,0,0));
            txtBaseP4Component = new DollarCurrencyTextField();
            txtBaseP4Component.setBorder(new EmptyBorder(0,0,0,0));
            txtBaseP5Component = new DollarCurrencyTextField();
            txtBaseP5Component.setBorder(new EmptyBorder(0,0,0,0));
            txtBaseP6Component = new DollarCurrencyTextField();
            txtBaseP6Component.setBorder(new EmptyBorder(0,0,0,0));
            txtBaseP7Component = new DollarCurrencyTextField();
            txtBaseP7Component.setBorder(new EmptyBorder(0,0,0,0));
            txtBaseP8Component = new DollarCurrencyTextField();
            txtBaseP8Component.setBorder(new EmptyBorder(0,0,0,0));
            txtBaseP9Component = new DollarCurrencyTextField();
            txtBaseP9Component.setBorder(new EmptyBorder(0,0,0,0));
            txtBaseP10Component = new DollarCurrencyTextField();
            txtBaseP10Component.setBorder(new EmptyBorder(0,0,0,0));
            //COEUSQA-1559 Display calculated base salary amount on RR Budget form in out years - End
        }
        /** Overridden method of the rendrer
         * @param table
         * @param value
         * @param isSelected
         * @param hasFocus
         * @param row
         * @param column
         * @return
         */
        public Component getTableCellRendererComponent(JTable table,Object value,
        boolean isSelected, boolean hasFocus,int row,int column){
            if(getFunctionType() == TypeConstants.DISPLAY_MODE){
                txtComponent.setBackground((java.awt.Color) javax.swing.UIManager.getDefaults().get("Panel.background"));
                txtBaseComponent.setBackground((java.awt.Color) javax.swing.UIManager.getDefaults().get("Panel.background"));
                //COEUSQA-1559 Display calculated base salary amount on RR Budget form in out years - Start
                txtBaseP1Component.setBackground((java.awt.Color) javax.swing.UIManager.getDefaults().get("Panel.background"));
                txtBaseP2Component.setBackground((java.awt.Color) javax.swing.UIManager.getDefaults().get("Panel.background"));
                txtBaseP3Component.setBackground((java.awt.Color) javax.swing.UIManager.getDefaults().get("Panel.background"));
                txtBaseP4Component.setBackground((java.awt.Color) javax.swing.UIManager.getDefaults().get("Panel.background"));
                txtBaseP5Component.setBackground((java.awt.Color) javax.swing.UIManager.getDefaults().get("Panel.background"));
                txtBaseP6Component.setBackground((java.awt.Color) javax.swing.UIManager.getDefaults().get("Panel.background"));
                txtBaseP7Component.setBackground((java.awt.Color) javax.swing.UIManager.getDefaults().get("Panel.background"));
                txtBaseP8Component.setBackground((java.awt.Color) javax.swing.UIManager.getDefaults().get("Panel.background"));
                txtBaseP9Component.setBackground((java.awt.Color) javax.swing.UIManager.getDefaults().get("Panel.background"));
                txtBaseP10Component.setBackground((java.awt.Color) javax.swing.UIManager.getDefaults().get("Panel.background"));                
                //COEUSQA-1559 Display calculated base salary amount on RR Budget form in out years - End
            }
            switch(column){
                case NAME_COLUNM:
                case JOB_CODE_COLUMN:
                    txtComponent.setText(value.toString());
                    return txtComponent;
                case EFF_DATE_COLUMN:
                    // Added for COEUSDEV-1059 Effective Date in Budget Person not formatted properly - Start
                    txtComponent.setText(value.toString());
                    return txtComponent;
                    // Added for COEUSDEV-1059 Effective Date in Budget Person not formatted properly - End
                //Added for Case#2918 - Use of Salary Anniversary Date for calculating inflation in budget development module - starts
                case SALARY_ANNIVERSARY_DATE_COLUMN:
                    if(showSalaryAnniversaryDate){
                        if(value != null && !value.equals(EMPTY_STRING)){
                            value = dtUtils.formatDate(value.toString(),REQUIRED_DATE_FORMAT);
                            txtComponent.setText(value.toString());
                        } else {
                            txtComponent.setText(EMPTY_STRING);
                        }
                        return txtComponent;
                    }else{
                        txtBaseP1Component.setText(value.toString());
                        return txtBaseP1Component;
                    }
                    //COEUSQA-1559 Display calculated base salary amount on RR Budget form in out years - End
                //Added for Case#2918 - Use of Salary Anniversary Date for calculating inflation in budget development module - ends
                case CALC_BASE_COLUMN:
                    txtBaseComponent.setText(value.toString());
                    return txtBaseComponent;
                case APPT_TYPE_COLUMN:
                    txtComponent.setText(value.toString());
                    return txtComponent;
                //COEUSQA-1559 Display calculated base salary amount on RR Budget form in out years - Start
                case BASE_SALARY_P1_COLUMN:
                    if(showSalaryAnniversaryDate){
                        txtBaseP1Component.setText(value.toString());
                        return txtBaseP1Component;
                    }else{
                        txtBaseP2Component.setText(value.toString());
                        return txtBaseP2Component;
                    }
                case BASE_SALARY_P2_COLUMN:
                    if(showSalaryAnniversaryDate){
                        txtBaseP2Component.setText(value.toString());
                        return txtBaseP2Component;
                    }else{
                        txtBaseP3Component.setText(value.toString());
                        return txtBaseP3Component;
                    }
                case BASE_SALARY_P3_COLUMN:
                    if(showSalaryAnniversaryDate){
                        txtBaseP3Component.setText(value.toString());
                        return txtBaseP3Component;
                    }else{
                        txtBaseP4Component.setText(value.toString());
                        return txtBaseP4Component;
                    }
                case BASE_SALARY_P4_COLUMN:
                    if(showSalaryAnniversaryDate){
                        txtBaseP4Component.setText(value.toString());
                        return txtBaseP4Component;
                    }else{
                        txtBaseP5Component.setText(value.toString());
                        return txtBaseP5Component;
                    }
                case BASE_SALARY_P5_COLUMN:
                    if(showSalaryAnniversaryDate){
                        txtBaseP5Component.setText(value.toString());
                        return txtBaseP5Component;
                    }else{
                        txtBaseP6Component.setText(value.toString());
                        return txtBaseP6Component;
                    }
                case BASE_SALARY_P6_COLUMN:
                    if(showSalaryAnniversaryDate){
                        txtBaseP6Component.setText(value.toString());
                        return txtBaseP6Component;
                    }else{
                        txtBaseP7Component.setText(value.toString());
                        return txtBaseP7Component;
                    }
                case BASE_SALARY_P7_COLUMN:
                    if(showSalaryAnniversaryDate){
                        txtBaseP7Component.setText(value.toString());
                        return txtBaseP7Component;
                    }else{
                        txtBaseP8Component.setText(value.toString());
                        return txtBaseP8Component;
                    }
                case BASE_SALARY_P8_COLUMN:
                    if(showSalaryAnniversaryDate){
                        txtBaseP8Component.setText(value.toString());
                        return txtBaseP8Component;
                    }else{
                        txtBaseP9Component.setText(value.toString());
                        return txtBaseP9Component;
                    }
                case BASE_SALARY_P9_COLUMN:
                    if(showSalaryAnniversaryDate){
                        txtBaseP9Component.setText(value.toString());
                        return txtBaseP9Component;
                    }else{
                        txtBaseP10Component.setText(value.toString());
                        return txtBaseP10Component;
                    }
                case BASE_SALARY_P10_COLUMN:
                    if(showSalaryAnniversaryDate){
                        txtBaseP10Component.setText(value.toString());
                        return txtBaseP10Component;
                    }
                //COEUSQA-1559 Display calculated base salary amount on RR Budget form in out years - End
            }
            return super.getTableCellRendererComponent(table, value,
            isSelected, hasFocus, row, column);
        }
        
    }
    
    
    private class BudgetPersonCellEditor extends AbstractCellEditor implements MouseListener,TableCellEditor {
        
        int selRow=0;
        private CoeusTextField txtPersonComponent;
        private CoeusTextField txtJobCode;
        private CoeusTextField txtPersonName;
        private DollarCurrencyTextField txtBaseComponent;
        private CoeusComboBox cmbAppointmnetType;
        private int column;
        int len=6;
        private ComboBoxBean comboBoxBean;
        //COEUSQA-1559 Display calculated base salary amount on RR Budget form in out years - Start
        private DollarCurrencyTextField txtBaseP1Component;
        private DollarCurrencyTextField txtBaseP2Component;
        private DollarCurrencyTextField txtBaseP3Component;
        private DollarCurrencyTextField txtBaseP4Component;
        private DollarCurrencyTextField txtBaseP5Component;
        private DollarCurrencyTextField txtBaseP6Component;
        private DollarCurrencyTextField txtBaseP7Component;
        private DollarCurrencyTextField txtBaseP8Component;
        private DollarCurrencyTextField txtBaseP9Component;
        private DollarCurrencyTextField txtBaseP10Component;
        //COEUSQA-1559 Display calculated base salary amount on RR Budget form in out years - End
        /** An Inner class of the BudgetPersoncontroller. Editor for the table component */
        BudgetPersonCellEditor(){
            
            txtPersonName = new CoeusTextField();
            txtPersonComponent = new CoeusTextField();
            txtJobCode = new CoeusTextField(6);
            txtJobCode.setDocument(new LimitedPlainDocument(len));
            txtBaseComponent = new DollarCurrencyTextField();
            //Modified for Case 3869 - Save not working for budget person - start
            //cmbAppointmnetType = new JComboBox(appointments);
            cmbAppointmnetType = new CoeusComboBox();
            cmbAppointmnetType.setModel(new DefaultComboBoxModel(vecAppointmentTypes));
            comboBoxBean = new ComboBoxBean();
            //Modified for Case 3869 - Save not working for budget person - end
            //COEUSQA-1559 Display calculated base salary amount on RR Budget form in out years - Start
            txtBaseP1Component = new DollarCurrencyTextField();
            txtBaseP2Component = new DollarCurrencyTextField();
            txtBaseP3Component = new DollarCurrencyTextField();
            txtBaseP4Component = new DollarCurrencyTextField();
            txtBaseP5Component = new DollarCurrencyTextField();
            txtBaseP6Component = new DollarCurrencyTextField();
            txtBaseP7Component = new DollarCurrencyTextField();
            txtBaseP8Component = new DollarCurrencyTextField();
            txtBaseP9Component = new DollarCurrencyTextField();
            txtBaseP10Component = new DollarCurrencyTextField();
            //COEUSQA-1559 Display calculated base salary amount on RR Budget form in out years - End
            registerComponents();
        }
        
        private void registerComponents(){
            txtPersonName.addMouseListener(this);
            txtPersonComponent.addMouseListener(this);
            txtJobCode.addMouseListener(this);
            txtBaseComponent.addMouseListener(this);
            cmbAppointmnetType.addMouseListener(this);
            
        }
        /** overridden method of the Editor
         * @param table
         * @param value
         * @param isSelected
         * @param row
         * @param column
         * @return
         */
        public Component getTableCellEditorComponent(JTable table,Object value,
        boolean isSelected, int row,int column){
            this.column = column;
            switch(column){
                case NAME_COLUNM:
                    // case JOB_CODE_COLUMN:
                    
                    // case JOB_CODE_COLUMN:
                    txtPersonName.setText(value.toString());
                    return txtPersonName;
                case JOB_CODE_COLUMN:
                    txtJobCode.setText(value.toString());
                    return txtJobCode;
                case EFF_DATE_COLUMN:
                    txtPersonComponent.setText(dtUtils.formatDate(value.toString(),SIMPLE_DATE_FORMAT));
                    return txtPersonComponent;
                case CALC_BASE_COLUMN:
                    txtBaseComponent.setText(value.toString());
                    return txtBaseComponent;
                case APPT_TYPE_COLUMN:
                    comboBoxBean.setDescription(value.toString());
                    //COEUSQA-2036 Code Table Prop Dev Appt Type and Period Types - Start
                    // Commented for COEUSQA-3709 : Coeus4.5: Budget Dropdown Issue - Start
//                    cmbAppointmnetType.removeAllItems();
                    // Commented for COEUSQA-3709 : Coeus4.5: Budget Dropdown Issue - End
                    Vector vecModifiedVectorData = constructActiveAppointmentTypes(value.toString());
                    cmbAppointmnetType.setModel(new DefaultComboBoxModel(vecModifiedVectorData));
                    //COEUSQA-2036 Code Table Prop Dev Appt Type and Period Types - Start
                    cmbAppointmnetType.setSelectedItem(comboBoxBean);
                    return cmbAppointmnetType;
                //Added for Case#2918 - Use of Salary Anniversary Date for calculating inflation in budget development module - starts
                case SALARY_ANNIVERSARY_DATE_COLUMN:
                    if(showSalaryAnniversaryDate){
                        if(value != null && !value.equals(EMPTY_STRING)){
                            txtPersonComponent.setText(dtUtils.formatDate(value.toString(),SIMPLE_DATE_FORMAT));
                        } else {
                            txtPersonComponent.setText(EMPTY_STRING);
                        }
                        return txtPersonComponent;
                    }
                    //COEUSQA-1559 Display calculated base salary amount on RR Budget form in out years - Start
                    else{
                        txtBaseP1Component.setText(value.toString());
                        return txtBaseP1Component;
                    }
                //Added for Case#2918 - Use of Salary Anniversary Date for calculating inflation in budget development module - ends
                case BASE_SALARY_P1_COLUMN:
                    if(showSalaryAnniversaryDate){
                        txtBaseP1Component.setText(value.toString());
                        return txtBaseP1Component;
                    }else{
                        txtBaseP2Component.setText(value.toString());
                        return txtBaseP2Component;
                    }
                case BASE_SALARY_P2_COLUMN:
                    if(showSalaryAnniversaryDate){
                        txtBaseP2Component.setText(value.toString());
                        return txtBaseP2Component;
                    }else{
                        txtBaseP3Component.setText(value.toString());
                        return txtBaseP3Component;
                    }
                case BASE_SALARY_P3_COLUMN:
                    if(showSalaryAnniversaryDate){
                        txtBaseP3Component.setText(value.toString());
                        return txtBaseP3Component;
                    }else{
                        txtBaseP4Component.setText(value.toString());
                        return txtBaseP4Component;
                    }
                case BASE_SALARY_P4_COLUMN:
                    if(showSalaryAnniversaryDate){
                        txtBaseP4Component.setText(value.toString());
                        return txtBaseP4Component;
                    }else{
                        txtBaseP5Component.setText(value.toString());
                        return txtBaseP5Component;
                    }
                case BASE_SALARY_P5_COLUMN:
                    if(showSalaryAnniversaryDate){
                        txtBaseP5Component.setText(value.toString());
                        return txtBaseP5Component;
                    }else{
                        txtBaseP6Component.setText(value.toString());
                        return txtBaseP6Component;
                    }
                case BASE_SALARY_P6_COLUMN:
                    if(showSalaryAnniversaryDate){
                        txtBaseP6Component.setText(value.toString());
                        return txtBaseP6Component;
                    }else{
                        txtBaseP7Component.setText(value.toString());
                        return txtBaseP7Component;
                    }
                case BASE_SALARY_P7_COLUMN:
                    if(showSalaryAnniversaryDate){
                        txtBaseP7Component.setText(value.toString());
                        return txtBaseP7Component;
                    }else{
                        txtBaseP8Component.setText(value.toString());
                        return txtBaseP8Component;
                    }
                case BASE_SALARY_P8_COLUMN:
                    if(showSalaryAnniversaryDate){
                        txtBaseP8Component.setText(value.toString());
                        return txtBaseP8Component;
                    }else{
                        txtBaseP9Component.setText(value.toString());
                        return txtBaseP9Component;
                    }
                case BASE_SALARY_P9_COLUMN:
                    if(showSalaryAnniversaryDate){
                        txtBaseP9Component.setText(value.toString());
                        return txtBaseP9Component;
                    }else{
                        txtBaseP10Component.setText(value.toString());
                        return txtBaseP10Component;
                    }
                case BASE_SALARY_P10_COLUMN:
                    if(showSalaryAnniversaryDate){
                        txtBaseP10Component.setText(value.toString());
                        return txtBaseP10Component;
                    }
                //COEUSQA-1559 Display calculated base salary amount on RR Budget form in out years - End
            }
            return txtBaseComponent;
        }
        
        public Object getCellEditorValue() {
            switch (column) {
                case NAME_COLUNM:
                    return txtPersonName.getText();
                case JOB_CODE_COLUMN:
                    return txtJobCode.getText();
                case EFF_DATE_COLUMN:
                    return txtPersonComponent.getText();
                case CALC_BASE_COLUMN:
                    return txtBaseComponent.getValue();
                case APPT_TYPE_COLUMN:
                    return cmbAppointmnetType.getSelectedItem();
                //Added for Case#2918 - Use of Salary Anniversary Date for calculating inflation in budget development module - starts
                case SALARY_ANNIVERSARY_DATE_COLUMN:
                    if(showSalaryAnniversaryDate){
                        return txtPersonComponent.getText();
                    }
                    //COEUSQA-1559 Display calculated base salary amount on RR Budget form in out years - Start
                    else{
                        return txtBaseP1Component.getValue();
                    }
               //Added for Case#2918 - Use of Salary Anniversary Date for calculating inflation in budget development module - ends
                case BASE_SALARY_P1_COLUMN:
                    if(showSalaryAnniversaryDate){
                        return txtBaseP1Component.getValue();
                    } else{
                        return txtBaseP2Component.getValue();
                    }
                case BASE_SALARY_P2_COLUMN:
                    if(showSalaryAnniversaryDate){
                        return txtBaseP2Component.getValue();
                    } else{
                        return txtBaseP3Component.getValue();
                    }
                case BASE_SALARY_P3_COLUMN:
                    if(showSalaryAnniversaryDate){
                        return txtBaseP3Component.getValue();
                    } else{
                        return txtBaseP4Component.getValue();
                    }
                case BASE_SALARY_P4_COLUMN:
                    if(showSalaryAnniversaryDate){
                        return txtBaseP4Component.getValue();
                    } else{
                        return txtBaseP5Component.getValue();
                    }
                case BASE_SALARY_P5_COLUMN:
                    if(showSalaryAnniversaryDate){
                        return txtBaseP5Component.getValue();
                    } else{
                        return txtBaseP6Component.getValue();
                    }
                case BASE_SALARY_P6_COLUMN:
                    if(showSalaryAnniversaryDate){
                        return txtBaseP6Component.getValue();
                    } else{
                        return txtBaseP7Component.getValue();
                    }
                case BASE_SALARY_P7_COLUMN:
                    if(showSalaryAnniversaryDate){
                        return txtBaseP7Component.getValue();
                    } else{
                        return txtBaseP8Component.getValue();
                    }
                case BASE_SALARY_P8_COLUMN:
                    if(showSalaryAnniversaryDate){
                        return txtBaseP8Component.getValue();
                    } else{
                        return txtBaseP9Component.getValue();
                    }
                case BASE_SALARY_P9_COLUMN:
                    if(showSalaryAnniversaryDate){
                        return txtBaseP9Component.getValue();
                    } else{
                        return txtBaseP10Component.getValue();
                    }
                case BASE_SALARY_P10_COLUMN:
                    if(showSalaryAnniversaryDate){
                        return txtBaseP10Component.getValue();
                    }
                //COEUSQA-1559 Display calculated base salary amount on RR Budget form in out years - End
            }
            return ((JTextField)txtBaseComponent).getText();
        }
        
        public void mouseClicked(MouseEvent mouseEvent) {
            if(mouseEvent.getClickCount() != 2) return ;
            if(mouseEvent.getSource().equals(txtPersonName) ||
            mouseEvent.getSource().equals(txtBaseComponent) ||
            mouseEvent.getSource().equals(txtJobCode)||
            mouseEvent.getSource().equals(cmbAppointmnetType)){
                lookUpPersonDetails();
            }
        }
        public void mouseEntered(java.awt.event.MouseEvent mouseEvent) {
        }
        public void mouseExited(java.awt.event.MouseEvent mouseEvent) {
        }
        
        public void mousePressed(java.awt.event.MouseEvent mouseEvent) {
        }
        
        public void mouseReleased(java.awt.event.MouseEvent mouseEvent) {
        }
        
        public boolean stopCellEditing() {
            
            if(column == NAME_COLUNM){
                //validatePersonName();
            }
            return super.stopCellEditing();
        }
        public int getClickCountToStart(){
            return 1;
        }
        
    }
    
    /** returns the valid person details. The person should be present in database
     * @param personId
     * @throws Exception
     * @return
     */
    public PersonInfoFormBean getValidPersonInfo(String personId) throws Exception{
        //        String personInfo = null;
        PersonInfoFormBean personInfoFormBean = null;
        // CoeusVector vctAppointments = null;
        requester = new RequesterBean();
        //requester.setFunctionType(GET_VALID_PERSON);
        requester.setDataObject(personId);
        
        // Modified to get the full name as title case
        requester.setDataObject("GET_PERSONINFO");
        requester.setId(personId);
        
        
        AppletServletCommunicator comm
        = new AppletServletCommunicator(connect, requester);
        
        comm.send();
        responder = comm.getResponse();
        if(responder.isSuccessfulResponse()){
            //modified to get the full name as title case
            //personInfo = (String)responder.getDataObject();
            personInfoFormBean = (PersonInfoFormBean)responder.getDataObject();
            //personInfo = personInfoFormBean.getPersonID();
            //personInfoFormBean.getFullName();
            
        }
        return personInfoFormBean;
    }
    
    
    /* This method sets the maximum Row ID from the vector of Budget Persons beans
     *that is present in queryEngine
     */
    private void setMaxRowID() {
        CoeusVector cvBudgetPersons = new CoeusVector();
        BudgetPersonsBean personsBean;
        try {
            cvBudgetPersons = QueryEngine.getInstance().getDetails(key,BudgetPersonsBean.class);
            if (cvBudgetPersons != null && cvBudgetPersons.size() > 0) {
                cvBudgetPersons.sort("rowId", false);
                personsBean = (BudgetPersonsBean) cvBudgetPersons.get(0);
                rowID = personsBean.getRowId() + 1;
            }
        } catch(Exception exception) {
            exception.printStackTrace();
        }
    }
    
    /** refreshes the window */
    public void refresh() {
        try{
            key = (getProposalNumber()+getVersionNumber());
            vecBudgetPersons = new CoeusVector();
            
            neq = new NotEquals("acType", TypeConstants.DELETE_RECORD);
            eq = new Equals("acType", null);
            or = new Or(neq, eq);
            vecBudgetPersons = QueryEngine.getInstance().executeQuery(key, BudgetPersonsBean.class, or);
            //since old AC types can have AC Type as Insert and Clicking OK Again
            //it shd not try to insert the Bean Again.
            resetAcTYpe(vecBudgetPersons);
            vecBudgetDate = QueryEngine.getInstance().executeQuery(key,budgetInfoBean);
            
            budgetInfoBean = (BudgetInfoBean)vecBudgetDate.get(0);
            budgetStartDate = budgetInfoBean.getStartDate();
            budgetPersonsBean.setProposalNumber(budgetInfoBean.getProposalNumber());
            budgetPersonsBean.setVersionNumber(budgetInfoBean.getVersionNumber());
            
            personName = budgetPersonsBean.getFullName();
            personId = budgetPersonsBean.getPersonId();
            budgetPersonsBean.setFullName(personName);
            budgetPersonsBean.setPersonId(personId);
            budgetPersonTableModel.setData(vecBudgetPersons);
            budgetPersonTableModel.fireTableDataChanged();
            modified=false;
            
        }catch(Exception exception){
            exception.getMessage();
        }
        
    }
    
    /** refreshes the window */
    public void refresh(CoeusVector cvData,String proposalNumber,int version) {        
        vecBudgetPersons = cvData;
        budgetPersonsForm.lblProposalNumbervalue.setText(proposalNumber);
        budgetPersonsForm.lblVersionvalue.setText(""+version);
        //COEUSQA-1559 Display calculated base salary amount on RR Budget form in out years - Start
        //to reload the persons data
        budgetPersonTableModel.loadPeriods(proposalNumber,version);
        budgetPersonTableModel.loadColumns();
        budgetPersonTableModel.fireTableStructureChanged();
        setTableEditors();
        //COEUSQA-1559 Display calculated base salary amount on RR Budget form in out years - End
        budgetPersonTableModel.setData(vecBudgetPersons);
        budgetPersonTableModel.fireTableDataChanged();
        if(vecBudgetPersons.size() > 0)
            budgetPersonsForm.tblBudgetPersons.setRowSelectionInterval(0, 0);
    }
    
    /** since old AC types can have AC Type as Insert and Clicking OK Again it shouldd
     * not try to insert the Bean Again.
     */
    private void resetAcTYpe(CoeusVector coeusVector) {
        if(coeusVector == null || coeusVector.size() == 0) return ;
        for(int index = 0; index < coeusVector.size(); index++) {
            ((CoeusBean)coeusVector.get(index)).setAcType(null);
        }
    }
    
    
    
    
    // This method will provide the key travrsal for the table cells
    // It specifies the tab and shift tab order.
    public void setTableKeyTraversal(){
        
        javax.swing.InputMap im = budgetPersonsForm.tblBudgetPersons.getInputMap(JTable.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        // Have the enter key work the same as the tab key
        KeyStroke tab = KeyStroke.getKeyStroke(KeyEvent.VK_TAB, 0);
        KeyStroke shiftTab = KeyStroke.getKeyStroke(KeyEvent.VK_TAB,KeyEvent.SHIFT_MASK );
        
        // Override the default tab behaviour
        // Tab to the next editable cell. When no editable cells goto next cell.
        final Action oldTabAction = budgetPersonsForm.tblBudgetPersons.getActionMap().get(im.get(tab));
        Action tabAction = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                oldTabAction.actionPerformed( e );
                JTable table = (JTable)e.getSource();
                int rowCount = table.getRowCount();
                int columnCount = table.getColumnCount();
                int row = table.getSelectedRow();
                int column = table.getSelectedColumn();
                
                while (! table.isCellEditable(row, column) ) {
                    column += 1;
                    
                    if (column == columnCount) {
                        column = 0;
                        row +=1;
                    }
                    if (row == rowCount) {
                        row = 0;
                    }
                    
                    // Back to where we started, get out.
                    
                    if (row == table.getSelectedRow()
                    && column == table.getSelectedColumn()) {
                        break;
                    }
                }
                
                table.changeSelection(row, column, false, false);
            }
        };
        budgetPersonsForm.tblBudgetPersons.getActionMap().put(im.get(tab), tabAction);
        
        
        
        
        // for the shift+tab action
        
        // Override the default tab behaviour
        // Tab to the previous editable cell. When no editable cells goto next cell.
        
        final Action oldTabAction1 = budgetPersonsForm.tblBudgetPersons.getActionMap().get(im.get(shiftTab));
        Action tabAction1 = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                oldTabAction1.actionPerformed( e );
                JTable table = (JTable)e.getSource();
                int rowCount = table.getRowCount();
                //                int columnCount = table.getColumnCount();
                int row = table.getSelectedRow();
                int column = table.getSelectedColumn();
                
                while (! table.isCellEditable(row, column) ) {
                    column -= 1;
                    if (column <= 0) {
                        column = 5;
                        row -=1;
                    }
                    
                    if (row < 0) {
                        row = rowCount-1;
                    }
                    // Back to where we started, get out.
                    
                    if (row == table.getSelectedRow()
                    && column == table.getSelectedColumn()) {
                        break;
                    }
                }
                
                table.changeSelection(row, column, false, false);
            }
        };
        budgetPersonsForm.tblBudgetPersons.getActionMap().put(im.get(shiftTab), tabAction1);
    }
    
    /**
     * Setter for property isInHierarchy.
     * @param isInHierarchy New value of property isInHierarchy.
     */
    public void setIsInHierarchy(boolean isInHierarchy) {
        this.isInHierarchy = isInHierarchy;
    }
    
    public void setDialogRef(CoeusDlgWindow dlgWindow) {
        dlgPersonBudget = dlgWindow;
    }
    
    /**
     * Getter for property cvDataInNewMode.
     * @return Value of property cvDataInNewMode.
     */
    public edu.mit.coeus.utils.CoeusVector getCvDataInNewMode() {
        return cvDataInNewMode;
    }
    
    /**
     * Setter for property cvDataInNewMode.
     * @param cvDataInNewMode New value of property cvDataInNewMode.
     */
    public void setCvDataInNewMode(edu.mit.coeus.utils.CoeusVector cvDataInNewMode) {
        this.cvDataInNewMode = cvDataInNewMode;
    }
    
    /**
     * To get default job code from the parameter table for given parameter(job code).
     * @return String
     */
    public String getDefaultJobCode(){
        final String connectTo = CoeusGuiConstants.CONNECTION_URL+ "/coeusFunctionsServlet";
        final String PARAMETER = "DEFAULT_JOB_CODE";
        String value = EMPTY_STRING;
        //        CoeusVector vctAppointments = null;
        RequesterBean requester = new RequesterBean();
        ResponderBean responder = null;
        requester.setDataObject(GET_PARAMETER_VALUE);
        Vector vecParameter = new Vector();
        vecParameter.add(PARAMETER);
        requester.setDataObjects(vecParameter);
        AppletServletCommunicator comm
        = new AppletServletCommunicator(connectTo, requester);
        comm.send();
        responder = comm.getResponse();
        if(responder.isSuccessfulResponse()){
            value =(String) responder.getDataObject();
        }
        return value;
    }
    
    /**
     * To get default job code from the parameter table for given parameter(job code).
     * @return String
     */
    public String getTBAPersonId(){
        String personId = null;
        requester = new RequesterBean();
        requester.setFunctionType(GET_TBA_PERSON_ID);
        requester.setDataObject(personId);
        AppletServletCommunicator comm
        = new AppletServletCommunicator(connectTo, requester);
        comm.send();
        responder = comm.getResponse();
        if(responder.isSuccessfulResponse()){
            personId = (String)responder.getDataObject();
        }
        return personId;
    }
    
    // 4493: While adding a TBA appointment type should be defaulted to 12 Months - Start
    /**
     * Compares the Default Appointment Type Code Entry  in the Parameter Table
     * with available Appoinment Type Codes. If any matching enty is found, the corrsponding
     * Appointment type Description will be set to defaultTbaApntmntTypeDesc. If no matching entry
     * is found, REG_EMPLOYEE will be set as the defaultTbaApntmntTypeDesc.
     *
     * @return void
     */
    private void validateDefaultTBAAppointmentType() {
        boolean isDefaultTypePresent = false;
        String regEmpCode = EMPTY_STRING;
        if(vecAppointmentTypes != null && vecAppointmentTypes.size() > 0){
            int appointmentCount = vecAppointmentTypes.size();
            ComboBoxBean cmbBean;
            for(int index = 0; index < appointmentCount; index++){
                cmbBean = (ComboBoxBean) vecAppointmentTypes.get(index);
                if(cmbBean.getCode() != null){
                    if(cmbBean.getCode().equalsIgnoreCase(defaultTbaApntmntTypeCode)){
                        // If the Default Appointment Type code in the Parameter Table and Appointnment Type
                        // Code of the ComboBoxBean is equal, set the  defaultTbaApntmntTypeDesc
                        // as the Description of the ComboBoxBean.
                        isDefaultTypePresent = true;
                        defaultTbaApntmntTypeDesc = cmbBean.getDescription();
                    }
                    if(cmbBean.getDescription() != null){
                        if(cmbBean.getDescription().equalsIgnoreCase(REG_EMPLOYEE)){
                            // Save the Appointment Type Code of the REG EMPLOYEE
                            // This Code will be set as the defaultTbaApntmntTypeCode if
                            // the user has entered any invalid enrty in the Parameter Table
                            regEmpCode = cmbBean.getCode();
                        }
                    }
                }
            }
            if(!isDefaultTypePresent){
                // Set the Appointment type code and Description as that of the
                // REG EMPLOYEE since there are no matching Code for defaultTbaApntmntTypeCode
                // found in the vecAppointmentTypes
                defaultTbaApntmntTypeCode = regEmpCode;
                defaultTbaApntmntTypeDesc = REG_EMPLOYEE;
            }
        }
    }
    // 4493: While adding a TBA appointment type should be defaulted to 12 Months - End
    
    // All Final Variable declarations
    
    
    /* used to specify to search in person table */
    private static final String PERSON_SEARCH = "personSearch";
    
    /** Message of the Screen
     */
    //Added for Case#2918 - Use of Salary Anniversary Date for calculating inflation in budget development module
    private static final String INVALID_SALARY_ANNIVERSARY_DATE = "budgetPersons_exceptionCode.1308";
    private static final String INVALID_EFFECTIVE_DATE = "budgetPersons_exceptionCode.1301";
    private static final String ENTER_PERSON_NAME = "budgetPersons_exceptionCode.1302";
    private static final String ENTER_JOB_CODE = "budgetPersons_exceptionCode.1303";
    private static final String DUPLICATE_NAME = "budgetPersons_exceptionCode.1304";
    private static final String DELETE_CONFIRMATION = "budgetPersons_exceptionCode.1305";
    private static final String INVALID_NAME = "budgetPersons_exceptionCode.1306";
    private static final String DELETE_PERSON = "budgetPersons_exceptionCode.1307";
    private static final String EMPTY_STRING = "";
    
    private static final int HAND_ICON_COLUMN = 0;
    private static final int NAME_COLUNM = 1;
    private static final int JOB_CODE_COLUMN = 2;
    private static final int APPT_TYPE_COLUMN = 3;
    private static final int EFF_DATE_COLUMN = 4;
    private static final int CALC_BASE_COLUMN = 5;
    //Added for Case#2918 - Use of Salary Anniversary Date for calculating inflation in budget development module
    /* JM 4-4-2016 incrementing from 6 to 7 to account for external persons col */
    private static final int SALARY_ANNIVERSARY_DATE_COLUMN = 7;
    /* JM END */
    
    //COEUSQA-1559 Display calculated base salary amount on RR Budget form in out years - Start
    //Modified for Case#3893 - Java 1.5 issues
    //private static final int WIDTH = 670;
    //private static final int WIDTH = 740;
    //Case#3893 - End
    //private static final int HEIGHT =280;
    
    private static final int WIDTH = 800;
    private static final int HEIGHT = 375; // JM was 300
    //COEUSQA-1559 Display calculated base salary amount on RR Budget form in out years - End
    
    //private final String VALID_PERSON = "/comMntServlet";
    
    private final char GET_APPOINTMENTS_FOR_PERSON = 'D';
    
    //private final char GET_VALID_PERSON = 'Y';
    
    
    //    private final char GET_APP_DETAILS = 'D';
    private final String BUDGET_PERSONS ="/BudgetMaintenanceServlet";
    //private final String connect  = CoeusGuiConstants.CONNECTION_URL + VALID_PERSON;
    private final String connect   = CoeusGuiConstants.CONNECTION_URL + CoeusGuiConstants.FUNCTION_SERVLET;
    private final String connectTo = CoeusGuiConstants.CONNECTION_URL+ BUDGET_PERSONS;
    //    private final String con = CoeusGuiConstants.CONNECTION_URL + GET_APP_DETAILS;
    
    private static final String DATE_SEPARATERS = ":/.,|-";
    
    private static final String SIMPLE_DATE_FORMAT = "MM/dd/yyyy";
    private static final String REQUIRED_DATE_FORMAT = "dd-MMM-yyyy";
    // Added for Coeus4.3 enhancement
    private static final String GET_PARAMETER_VALUE = "GET_PARAMETER_VALUE";
    private static final char GET_TBA_PERSON_ID = 'r'; 
    
    //COEUSQA-1535-Access to institutionally maintained salaries in proposal budget - Start
     /**
     *     
     * To check if user has the rights to view institutional salaries
     * @return boolean value for right     
     * @throws CoeusException if exception
     */
    protected boolean hasRightToViewInstitutionalSalaries(BudgetInfoBean budgetInfoBean
            , String appointmentPersonId) throws CoeusException{
        String connect = CoeusGuiConstants.CONNECTION_URL + HIERARCHY_SERVLET;
        RequesterBean request = new RequesterBean();
        Boolean hasRight = null;
        CoeusVector cvDataToServer = new CoeusVector();
        //To set the proposal number, function type
        cvDataToServer.add(budgetInfoBean.getProposalNumber());       
        cvDataToServer.add(new Boolean(isParentProposal()));
        cvDataToServer.add(budgetInfoBean);
        cvDataToServer.add(appointmentPersonId);
        request.setDataObject(cvDataToServer);
        request.setFunctionType(CHECK_VIEW_INSTITUTIONAL_SALARIES_RIGHT);
        AppletServletCommunicator comm = new AppletServletCommunicator(connect, request);
        comm.send();
        ResponderBean response = comm.getResponse();
        if (response!= null && response.isSuccessfulResponse()){
            hasRight = (Boolean)response.getDataObject();
        }else {
            throw new CoeusException(response.getMessage());
        }
        return hasRight;
    }
    //COEUSQA-1535-Access to institutionally maintained salaries in proposal budget - End
    
    //COEUSQA-1559 Display calculated base salary amount on RR Budget form in out years - Start
    /**
     * Method to calculate base salary for all periods     
     */
    private void calculateTotPeriodSalary(){
        int rowIndex = budgetPersonsForm.tblBudgetPersons.getSelectedRow();
        if(rowIndex != -1 && rowIndex >= 0){
            // fetch the person data from the all data
            BudgetPersonsBean budgetPersonsBean = (BudgetPersonsBean)vecBudgetPersons.get(rowIndex);
            //calculate the period wise salary for the person
            double currentPeriodSalary = 0.00;
            HashMap hmBaseSalForAllPeriods = calculateSalaryWithInflation(budgetPersonsBean, vecBudgetPeriods);
            for(int periodCount = 0;periodCount<hmBaseSalForAllPeriods.size();periodCount++){
                int periodKey = periodCount+1;
                switch(periodKey){
                    case 1:
                        currentPeriodSalary = (Double)hmBaseSalForAllPeriods.get(periodKey);
                        budgetPersonsBean.setBaseSalaryP1(currentPeriodSalary);
                        modified = true;
                        break;
                    case 2:
                        currentPeriodSalary = (Double)hmBaseSalForAllPeriods.get(periodKey);
                        budgetPersonsBean.setBaseSalaryP2(currentPeriodSalary);
                        modified = true;
                        break;
                    case 3:
                        currentPeriodSalary = (Double)hmBaseSalForAllPeriods.get(periodKey);
                        budgetPersonsBean.setBaseSalaryP3(currentPeriodSalary);
                        modified = true;
                        break;
                    case 4:
                        currentPeriodSalary = (Double)hmBaseSalForAllPeriods.get(periodKey);
                        budgetPersonsBean.setBaseSalaryP4(currentPeriodSalary);
                        modified = true;
                        break;
                    case 5:
                        currentPeriodSalary = (Double)hmBaseSalForAllPeriods.get(periodKey);
                        budgetPersonsBean.setBaseSalaryP5(currentPeriodSalary);
                        modified = true;
                        break;
                    case 6:
                        currentPeriodSalary = (Double)hmBaseSalForAllPeriods.get(periodKey);
                        budgetPersonsBean.setBaseSalaryP6(currentPeriodSalary);
                        modified = true;
                        break;
                    case 7:
                        currentPeriodSalary = (Double)hmBaseSalForAllPeriods.get(periodKey);
                        budgetPersonsBean.setBaseSalaryP7(currentPeriodSalary);
                        modified = true;
                        break;
                    case 8:
                        currentPeriodSalary = (Double)hmBaseSalForAllPeriods.get(periodKey);
                        budgetPersonsBean.setBaseSalaryP8(currentPeriodSalary);
                        modified = true;
                        break;
                    case 9:
                        currentPeriodSalary = (Double)hmBaseSalForAllPeriods.get(periodKey);
                        budgetPersonsBean.setBaseSalaryP9(currentPeriodSalary);
                        modified = true;
                        break;
                    case 10:
                        currentPeriodSalary = (Double)hmBaseSalForAllPeriods.get(periodKey);
                        budgetPersonsBean.setBaseSalaryP10(currentPeriodSalary);
                        modified = true;
                        break;
                }
                budgetPersonTableModel.fireTableRowsUpdated(rowIndex, rowIndex);
            }
        }
    }
    
    /**
     * Fetch the default inflation rate from the database
     * @return Double
     */
    public Double fetchInflationRateForSalary(){
        final String connectTo = CoeusGuiConstants.CONNECTION_URL+ "/coeusFunctionsServlet";
        final String PARAMETER = "DEFAULT_INFLATION_RATE_FOR_SALARY";
        String value = EMPTY_STRING;
        Double defaultInflationRate = null;
        RequesterBean requester = new RequesterBean();
        ResponderBean responder = null;
        requester.setDataObject(GET_PARAMETER_VALUE);
        Vector vecParameter = new Vector();
        vecParameter.add(PARAMETER);
        requester.setDataObjects(vecParameter);
        AppletServletCommunicator comm
                = new AppletServletCommunicator(connectTo, requester);
        comm.send();
        responder = comm.getResponse();
        if(responder.isSuccessfulResponse()){
            value = (String) responder.getDataObject();
            if(value.length()>0){
                defaultInflationRate = new Double(value);
            }else{
                defaultInflationRate = 0.00;
            }
        }
        return defaultInflationRate;
    }
    
    /**
     * Method to calculate base salary for all periods     
     * @param BudgetPersonsBean
     * @param Vector     
     * @return HashMap     
     */
    public HashMap calculateSalaryWithInflation(BudgetPersonsBean budgetPersonsBean, Vector vecBudgetPeriods){
        String connectTo = CoeusGuiConstants.CONNECTION_URL+ "/BudgetMaintenanceServlet";
        Vector vecData = new Vector(5);
        HashMap hmPeriodBaseSalary = new HashMap();
        RequesterBean requester = new RequesterBean();
        ResponderBean responder = null;
        requester.setFunctionType(CALCULATE_BASE_SALARY_FOR_PERIOD);
        double inflationRate = fetchInflationRateForSalary();        
        //add the data to the collection vector
        vecData.add(inflationRate);
        vecData.add(budgetPersonsBean);
        vecData.add(vecBudgetPeriods);
        vecData.add(budgetInfoBean);
        
        requester.setDataObjects(vecData);
        
        AppletServletCommunicator comm = new AppletServletCommunicator(connectTo, requester);        
        comm.send();
        responder = comm.getResponse();
        try {
            if(responder.hasResponse()){
                //retrieve the base salary of all periods
                hmPeriodBaseSalary = (HashMap)responder.getDataObject();
            }
        } catch (CoeusException ex) {
            ex.printStackTrace();
        }
        return hmPeriodBaseSalary;
    }
    
    /**
     * Fetch the budget periods for the given proposal number and version number
     * @param String
     * @param int
     * @return CoeusVector
     */
    public CoeusVector getBudgetPeriodsForChildProposal(String proposalNumber, int versionNumber){
        final String connectTo = CoeusGuiConstants.CONNECTION_URL+ "/BudgetMaintenanceServlet";        
        CoeusVector vecChildBudgetPeriods = new CoeusVector();
        RequesterBean requester = new RequesterBean();
        ResponderBean responder = null;
        requester.setFunctionType(GET_BUDGET_PERIOD);
        Vector vecParameter = new Vector();
        vecParameter.add(proposalNumber);
        vecParameter.add(new Integer(versionNumber));
        requester.setDataObjects(vecParameter);
        AppletServletCommunicator comm
                = new AppletServletCommunicator(connectTo, requester);
        comm.send();
        responder = comm.getResponse();
        if(responder.isSuccessfulResponse()){
            vecChildBudgetPeriods = (CoeusVector) responder.getDataObjects();            
        }
        return vecChildBudgetPeriods;
    }
    
    /**
     * Check whether the parent proposal is existing for the proposal number
     * @param String
     * @param boolean
     * @return CoeusVector
     */
    public boolean getHierarchyForChildProposal(String proposalNumber){
        final String connectTo = CoeusGuiConstants.CONNECTION_URL+ "/ProposalHierarchyServlet";
        boolean isHierarchy = false;
        RequesterBean requester = new RequesterBean();
        ResponderBean responder = null;
        requester.setFunctionType(GET_PARENT_DATA);
        requester.setId(proposalNumber);
        AppletServletCommunicator comm
                = new AppletServletCommunicator(connectTo, requester);
        comm.send();
        responder = comm.getResponse();
        if(responder.isSuccessfulResponse()){
            HashMap hmParentData = (HashMap) responder.getDataObject();
            isHierarchy = (Boolean)hmParentData.get("IN_HIERARCHY");
        }
        return isHierarchy;
    }
    //COEUSQA-1559 Display calculated base salary amount on RR Budget form in out years - End
}