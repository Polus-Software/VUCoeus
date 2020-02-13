/** Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */
package edu.mit.coeus.award.controller;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;
import java.util.*;
import java.text.SimpleDateFormat;
import java.text.ParseException;

import edu.mit.coeus.gui.*;
import edu.mit.coeus.utils.*;
import edu.mit.coeus.utils.query.*;
import edu.mit.coeus.award.bean.*;
import edu.mit.coeus.bean.*;
import edu.mit.coeus.brokers.*;
import edu.mit.coeus.exception.*;
//import edu.mit.coeus.utils.query.QueryEngine;
import edu.mit.coeus.award.gui.AwardAddReportingReqForm;
//import edu.mit.coeus.exception.CoeusUIException;
import edu.mit.coeus.award.gui.AwardAddRecipientsForm;


/**
 * AwardAddReportingReqController.java
  * @author  Ajay G M 
 */
public class AwardAddReportingReqController extends RepRequirementController
implements ActionListener,ItemListener,Observer{
    
    /** Holds an instance of <CODE>AwardAddReportingReqForm</CODE> */
    private AwardAddReportingReqForm awardAddReportingReqForm;
    
    /** Holds an instance of <CODE>AwardAddRecipientsForm</CODE> */
    private AwardAddRecipientsForm awardAddRecipientsForm;
    
    /** Holds an instance of <CODE>AwardAddRecipientsController</CODE> */
    private AwardAddRecipientsController awardAddRecipientsController;
    
    /**
     * To create an instance of MDIform
     */
    private CoeusAppletMDIForm mdiForm = CoeusGuiConstants.getMDIForm();
    
    /**
     * Instance of the Dialog
     */
    private CoeusDlgWindow dlgReportingReqForm;
    
    /**
     * Instance of Coeus Message Resources
     */
    private CoeusMessageResources coeusMessageResources;
    
    /**
     * Instance of Query Engine
     */
    private QueryEngine queryEngine;
    
    /*Listener*/
    private BaseWindowObservable observable  = new BaseWindowObservable();
    
    /*Holds rep requirements bean*/
    private CoeusVector cvReportingReqBean;
    
    /*Holds deleted beans*/
    private CoeusVector cvDeletedItem;
    
    /*Holds Report Type*/
    private CoeusVector cvReportTypeCode;
    
    /*Holds Frequency*/
    private CoeusVector cvReportClassFrequency;
    
    /*Holds Frequency Base*/
    private CoeusVector cvFrequencyBase;
    
    /*Holds Distribution*/
    private CoeusVector cvDistribution;
    
    /*Holds Status*/
    private CoeusVector cvStatus;
    
    /*Holds All Beans of Rep Requirements*/
    private CoeusVector cvRepRequirements;
    
    /*Holds All the persons selected from the add recipients window*/
    private CoeusVector cvSelectedPersons;
    
    ComboBoxBean reportComboBean;
    
    /*Function Type to get valid report Frequency */
    private static final char GET_VALID_REPORT_FREQUENCY = 'N';
    
    //Represents the string for conneting to the servlet and getting Proposal Log data
    private static final String GET_SERVLET = "/AwardMaintenanceServlet";
    
    /*For connecting to the server*/
    private static final String connect = CoeusGuiConstants.CONNECTION_URL + GET_SERVLET;
    
    
    private AwardAddRepReqTableModel awardAddRepReqTableModel;
    private AwardAddRepReqRenderer awardAddRepReqRenderer;
    private AwardAddRepReqCellEditor awardAddRepReqCellEditor;
    
    /*Validation Messages*/
    private static final String DUPILCATE_CONTACT = "awardReportTerms_exceptionCode.1754";
    private static final String ROW_TO_DELETE = "orgIDCPnl_exceptionCode.1097";
    private static final String DELETE_CONFIRMATION = "instPropIPReview_exceptionCode.1353";
    
    private static final String REPORT_VALIDATE = "addRepRequirements_exceptionCode.1001";
    private static final String FREQUENCY_VALIDATE = "addRepRequirements_exceptionCode.1002";
    private static final String STATUS_VALIDATE = "addRepRequirements_exceptionCode.1003";
    private static final String SELECT_RECIPIENTS  = "addRepRequirements_exceptionCode.1004";
    private static final String DISTRIBUTION_VALIDATE = "addRepRequirements_exceptionCode.1005";
    private static final String DATE_VALIDATE = "addRepRequirements_exceptionCode.1006";
    private static final String SAVE_CHANGES = "saveConfirmCode.1002";
    
    /*Column Indicies*/
    private static final int CONTACT_INDEX = 0;
    private static final int ADDRESS_INDEX = 1;
    private static final int COPIES_INDEX = 2;
    
    /*For date formating*/
    private DateUtils dateUtils = new DateUtils();
    private SimpleDateFormat dtFormat = new SimpleDateFormat("MM/dd/yyyy");
    private static final String DATE_SEPARATERS = ":/.,|-";
    private static final String DATE_FORMAT_DISPLAY = "dd-MMM-yyyy";
    
    /*For setting the dimentions*/
    private static final String WINDOW_TITLE = "Add Reporting Requirements";
    private static final int WIDTH = 575;
    //Commented/Added for case#2268 - Report Tracking Functionality
//    private static final int HEIGHT = 300;
    private static final int HEIGHT = 150;
    
    /*For displaying the Copies*/
    private int noOfCopies = 1;
    
    public static final int OK_CLICKED = 1;
    public static final int CANCEL_CLICKED = 0;
    private int clickedItem = CANCEL_CLICKED;
    
    private boolean modified = false;
    
    private int reportNo;
    /** Creates a new instance of AwardAddReportingReqController */
    public AwardAddReportingReqController(AwardBaseBean awardBaseBean, char functionType) {
        super(awardBaseBean);
        queryEngine = QueryEngine.getInstance();
        coeusMessageResources = CoeusMessageResources.getInstance();
        registerComponents();
        postInitComponents();
        setFunctionType(functionType);
        reportNo = getMaxReportNo();

//        /*Added for Checking*/
//        getDAta();
    }
    
    /**
     * Registers the Observer
     */
    public void registerObserver( Observer observer ) {
        observable.addObserver( observer );
    }
    
    /** This method creates and sets the display attributes for the dialog
     */
    public void postInitComponents(){
        /*Register the observer for the Add Recipients component*/
        //awardAddRecipientsController = new AwardAddRecipientsController(awardBaseBean,getFunctionType());
        awardAddRecipientsForm = new AwardAddRecipientsForm();
        //        awardAddRecipientsController.registerObserver(this);
        
        
        dlgReportingReqForm = new CoeusDlgWindow(mdiForm);
        dlgReportingReqForm.setResizable(false);
        dlgReportingReqForm.setModal(true);
        dlgReportingReqForm.getContentPane().add(awardAddReportingReqForm);
        
        dlgReportingReqForm.setTitle(WINDOW_TITLE);
        dlgReportingReqForm.setFont(CoeusFontFactory.getLabelFont());
        dlgReportingReqForm.setSize(WIDTH, HEIGHT);
        
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension dlgSize = dlgReportingReqForm.getSize();
        dlgReportingReqForm.setLocation(screenSize.width/2 - (dlgSize.width/2),
        screenSize.height/2 - (dlgSize.height/2));
        
        dlgReportingReqForm.addComponentListener(
        new ComponentAdapter(){
            public void componentShown(ComponentEvent e){
                requestDefaultFocus();
            }
        });
        
        dlgReportingReqForm.addEscapeKeyListener(new AbstractAction("escPressed"){
            public void actionPerformed(ActionEvent ae){
                /*If esc is pressed ,when the focus is in the due date field
                 *the modified flag is not set.Hence to check weather the Due date is modified.
                 */
                if(!EMPTY.equals(awardAddReportingReqForm.txtDueDate.getText().trim())){
                    modified = true;
                }
                
                /*If esc is pressed ,when the focus is in the due date field,the valdiation for date
                 *is not done.
                 */
                if(!EMPTY.equals(awardAddReportingReqForm.txtDueDate.getText().trim())){
                    String date = awardAddReportingReqForm.txtDueDate.getText().trim();
                    date = dateUtils.restoreDate(date, DATE_SEPARATERS);
                    date = dateUtils.formatDate(date, DATE_SEPARATERS, DATE_FORMAT_DISPLAY);
                    if(date == null) {
                        setRequestFocusInThread(awardAddReportingReqForm.txtDueDate);
                        CoeusOptionPane.showInfoDialog(
                        coeusMessageResources.parseMessageKey(DATE_VALIDATE)
                        );
                        return ;
                    }
                }
                
                performCancelAction();
            }
        });
        
        dlgReportingReqForm.setDefaultCloseOperation(CoeusDlgWindow.DO_NOTHING_ON_CLOSE);
        dlgReportingReqForm.addWindowListener(new WindowAdapter(){
            public void windowOpening(WindowEvent we){
            }
            public void windowClosing(WindowEvent we){
                performCancelAction();
            }
        });
        //code for disposing the window ends
    }
    
    /**
     * Displays the Form which is being controlled.
     */    
    public void display() {
    }

    
    public int displayForm(){
        dlgReportingReqForm.setVisible(true);
        return clickedItem;
    }
    
    /** Perform field formatting.
     * enabling, disabling components depending on the different conditions
     */
    public void formatFields() {
        awardAddReportingReqForm.txtClass.setEditable(false);
        awardAddReportingReqForm.txtClass.setEnabled(false);
    }
    
    /** An overridden method of the controller
     * @return apprEquipmentForm returns the controlled form component
     */
    public Component getControlledUI() {
        return awardAddReportingReqForm;
    }
    
    /** Returns the form data
     * @return returns the form data
     */
    public Object getFormData() {
        return null;
    }
    
    /** This method is used to set the listeners to the components.
     */
    public void registerComponents() {
        //Add listeners to all the buttons
        awardAddReportingReqForm = new AwardAddReportingReqForm();
        awardAddReportingReqForm.btnOK.addActionListener(this);
        awardAddReportingReqForm.btnCancel.addActionListener(this);
        awardAddReportingReqForm.btnAdd.addActionListener(this);
        awardAddReportingReqForm.btnDelete.addActionListener(this);
        
        
        /** Code for focus traversal - start */
        java.awt.Component[] components = { awardAddReportingReqForm.cmbType,
        awardAddReportingReqForm.cmbFrequency,awardAddReportingReqForm.cmbDistribution,
        awardAddReportingReqForm.cmbFrequencyBase,awardAddReportingReqForm.txtDueDate,
        awardAddReportingReqForm.cmbStatus, awardAddReportingReqForm.tblRecipients,
        awardAddReportingReqForm.btnAdd,awardAddReportingReqForm.btnDelete,
        awardAddReportingReqForm.btnOK,awardAddReportingReqForm.btnCancel
        };
        
        ScreenFocusTraversalPolicy traversePolicy = new ScreenFocusTraversalPolicy( components );
        awardAddReportingReqForm.setFocusTraversalPolicy(traversePolicy);
        awardAddReportingReqForm.setFocusCycleRoot(true);
        
        /** Code for focus traversal - end */
        
        awardAddReportingReqForm.cmbType.addItemListener(this);
        awardAddReportingReqForm.cmbFrequency.addItemListener(this);
        awardAddReportingReqForm.cmbDistribution.addItemListener(this);
        awardAddReportingReqForm.cmbStatus.addItemListener(this);
        
        CustomFocusAdapter customFocusAdapter = new CustomFocusAdapter();
        awardAddReportingReqForm.txtDueDate.addFocusListener(customFocusAdapter);
        
        awardAddRepReqTableModel = new AwardAddRepReqTableModel();
        awardAddRepReqCellEditor = new AwardAddRepReqCellEditor();
        awardAddRepReqRenderer = new AwardAddRepReqRenderer();
        awardAddReportingReqForm.tblRecipients.setModel(awardAddRepReqTableModel);
        
        
        awardAddReportingReqForm.cmbFrequency.addKeyListener(new KeyAdapter(){
            public void keyPressed(KeyEvent kEvent){
                if( kEvent.getKeyCode() == KeyEvent.VK_DELETE &&
                kEvent.getSource() instanceof CoeusComboBox ){
                    awardAddReportingReqForm.cmbFrequency.setModel(new DefaultComboBoxModel(cvReportClassFrequency));
                    awardAddReportingReqForm.cmbFrequency.requestFocusInWindow();
                    kEvent.consume();
                    if(dlgReportingReqForm.isVisible()){
                        //    delPressed = true;
                        ItemEvent itemEvent = new ItemEvent(awardAddReportingReqForm.cmbType, 0, null, ItemEvent.SELECTED);
                        itemStateChanged(itemEvent);
                    }
                }
            }
        });
        
        awardAddReportingReqForm.cmbFrequencyBase.addKeyListener(new KeyAdapter(){
            public void keyPressed(KeyEvent kEvent){
                if( kEvent.getKeyCode() == KeyEvent.VK_DELETE &&
                kEvent.getSource() instanceof CoeusComboBox ){
                    awardAddReportingReqForm.cmbFrequencyBase.requestFocusInWindow();
                    kEvent.consume();
                    if(dlgReportingReqForm.isVisible()){
                        //                        delPressed = true;
                        ItemEvent itemEvent = new ItemEvent(awardAddReportingReqForm.cmbFrequencyBase, 0, null, ItemEvent.SELECTED);
                        itemStateChanged(itemEvent);
                    }
                }
            }
        });
        
        awardAddReportingReqForm.cmbDistribution.addKeyListener(new KeyAdapter(){
            public void keyPressed(KeyEvent kEvent){
                if( kEvent.getKeyCode() == KeyEvent.VK_DELETE &&
                kEvent.getSource() instanceof CoeusComboBox ){
                    awardAddReportingReqForm.cmbDistribution.setModel(new DefaultComboBoxModel(cvDistribution));
                    awardAddReportingReqForm.cmbDistribution.requestFocusInWindow();
                    kEvent.consume();
                }
            }
        });
        setTableEditors();
        //Added for case#2268 - Report Tracking Functionality - start
        awardAddReportingReqForm.lblRecipients.setVisible(false);
        awardAddReportingReqForm.scrpnRecipients.setVisible(false);
        awardAddReportingReqForm.btnAdd.setVisible(false);
        awardAddReportingReqForm.btnDelete.setVisible(false);             
        //Added for case#2268 - Report Tracking Functionality - end        
    }
    
    /** To set the default focus for the component
     */
    public void requestDefaultFocus(){
        awardAddReportingReqForm.cmbType.requestFocus();
    }
    
    
    /** This method is used to set the form data specified in
     * <CODE> data </CODE>
     * @param data data to set to the form
     */
    public void setFormData(Object data) {
        /*For getting the data from Base Window*/
        this.reportComboBean = (ComboBoxBean)data;
        
        cvReportingReqBean  = new CoeusVector();
        cvRepRequirements = new CoeusVector();
        cvDeletedItem = new CoeusVector();
        
        /*For populating the Status combo*/
        cvStatus = new CoeusVector();
        
        try{
            cvStatus = (CoeusVector) queryEngine.getDetails(queryKey,KeyConstants.AWARD_REPORT_STATUS);
            
//            /*Added for testing*/
//            CoeusVector cv = (CoeusVector)queryEngine.getDetails(queryKey,KeyConstants.CONTACT_TYPES);
//            System.out.println("CV:"+cv.size());
        }catch (CoeusException coeusException){
            coeusException.printStackTrace();
        }
        /*Uncheck*/
        //        htData = (HashMap)data;
        //        CoeusVector cvData = new CoeusVector () ;
        //        cvData = (CoeusVector) htData.get (CoeusVector.class);
        //        cvAllData = new CoeusVector ();
        //        if(cvData != null && cvData.size () > 0){
        //            AwardReportTermsBean reportTermsBean = (AwardReportTermsBean)cvData.get (0);
        //        /*For setting the type to the ComboBox when opened in add mode.
        //         *The type which is selected in the Reoprt Tab should be set to the combobox
        //         */
        //            typeCmoboBean =new ComboBoxBean (""+reportTermsBean.getReportCode (),reportTermsBean.getReportDescription ());
        //
        //
        //            cvAllData.addAll (cvData);
        //
        //        }
        //        /*ComBoBox got from HashTable for setting the Class to the dialog*/
        //        ComboBoxBean cmbReportclass  = (ComboBoxBean) htData.get (ComboBoxBean.class);
        //        reportClass = cmbReportclass.getCode ().trim ();
        
        /*Uncheck*/
        
        //        try{
        //            /*For getting  to setting data to table*/
        //            cvAwardRepTermsBean = queryEngine.getDetails(
        //            queryKey,AwardReportTermsBean.class);
        //            if(cvAwardRepTermsBean != null && cvAwardRepTermsBean.size() > 0){
        //                cvAwardRepTermsBean.sort("rowId",false);
        //                AwardReportTermsBean reportTermsBean =(AwardReportTermsBean) cvAwardRepTermsBean.get(0);
        //                rowId = reportTermsBean.getRowId();
        //            }
        //
        //            /*For getting the data of all the types belonging to that a particular class*/
        //            Equals eqReportClassCode = new Equals("reportClassCode",new Integer(reportClass));
        //            And eqReportClassCodeAndActiveBeans = new And(eqReportClassCode,CoeusVector.FILTER_ACTIVE_BEANS);
        //            cvClassRelatedData = queryEngine.executeQuery(
        //            queryKey,AwardReportTermsBean.class,eqReportClassCodeAndActiveBeans);
        //
        //        }catch (CoeusException coeusException){
        //            coeusException.printStackTrace();
        //        }
        
        
        
        //
        //        if(functionType == TypeConstants.MODIFY_MODE){
        //            awardReportTermsBean = (AwardReportTermsBean)htData.get(AwardReportTermsBean.class);
        //            intialTypeCode = awardReportTermsBean.getReportCode();
        //            if(cvAllData.size()>0){
        //                performFiltering();
        //            }
        //            try{
        //                String strDueDate = "01-Jan-1900" ;// DD-MMM-YYYY
        //                utilDueDate =  dtFormat.parse(dtUtils.restoreDate(strDueDate, DATE_SEPARATERS));
        //            }catch (ParseException parseException) {
        //                parseException.printStackTrace();
        //            }
        //            java.sql.Date sqlDueDate = new java.sql.Date(utilDueDate.getTime());
        //
        //            if(!awardReportTermsBean.getDueDate().equals(sqlDueDate )){
        //                String strDate = dateUtils.formatDate(awardReportTermsBean.getDueDate().toString(),DATE_FORMAT_DISPLAY);
        //                awardReportTermsForm.txtDueDate.setText(strDate);
        //            }
        //            Equals eqFreqDes = new Equals("frequencyDescription",awardReportTermsBean.getFrequencyDescription());
        //            Equals eqFreqBase = new Equals("frequencyBaseDescription", awardReportTermsBean.getFrequencyBaseDescription());
        //            Equals eqOsp = new Equals("ospDistributionDescription",awardReportTermsBean.getOspDistributionDescription());
        //            Equals eqDate = new Equals("dueDate",awardReportTermsBean.getDueDate());
        //
        //            And eqFreqDesAndeqFreqBase = new And(eqFreqDes, eqFreqBase);
        //            And eqOspAndeqDate = new And(eqOsp, eqDate);
        //
        //            And filteredData = new And(eqFreqDesAndeqFreqBase,eqOspAndeqDate);
        //            cvData = cvData.filter(filteredData);
        //
        //            if(cvData.size()>0){
        //                for(int index = 0;index<cvData.size();index++){
        //                    AwardReportTermsBean reportTermsBean = (AwardReportTermsBean)cvData.get(index);
        //                    if(reportTermsBean.getRolodexId() != -1 ){
        //                        cvReportTerms.add(reportTermsBean);
        //                    }
        //                }
        //            }
        //        }
        awardAddReportingReqForm.txtClass.setText(reportComboBean.getDescription().trim());
        awardAddRepReqTableModel.setData(cvRepRequirements);
        populateCombo();
    }
    
    /**
     * Saves the Form Data.
     */    
    public void saveFormData() {
//        try{
//            if(cvDeletedItem != null && cvDeletedItem.size() >0){
//                for(int index = 0; index < cvDeletedItem.size(); index++){
//                    AwardReportReqBean deletedRepReqBean = (AwardReportReqBean)cvDeletedItem.get(index);
//                    
//                    deletedRepReqBean.setAcType(TypeConstants.DELETE_RECORD);
//                    deletedRepReqBean.setMitAwardNumber(awardBaseBean.getMitAwardNumber());
//                    
//                    queryEngine.delete(queryKey, deletedRepReqBean);
//                }
//            }
            if(cvRepRequirements !=null && cvRepRequirements.size() >0){
                for(int index = 0 ; index<cvRepRequirements.size(); index++){
                    AwardReportReqBean awardReportReqBean = (AwardReportReqBean)cvRepRequirements.get(index);
                    queryEngine.insert(queryKey,awardReportReqBean);
                    clickedItem = OK_CLICKED;
                }
            }
//        }catch (CoeusException coeusException){
//            coeusException.printStackTrace();
//        }
    }

     /** Validate the form data/Form and returns true if
     * validation is through else returns false.
     * @throws CoeusUIException if some exception occurs or some validation fails.
     * @return true if
     * validation is through else returns false.
     */
    
    public boolean validate() throws CoeusUIException {
        if(EMPTY.equals(awardAddReportingReqForm.cmbType.getSelectedItem().toString().trim())){
            setRequestFocusInThread(awardAddReportingReqForm.cmbType);
            CoeusOptionPane.showInfoDialog(
            coeusMessageResources.parseMessageKey(REPORT_VALIDATE));
            return false;
        }
        if(EMPTY.equals(awardAddReportingReqForm.cmbFrequency.getSelectedItem().toString().trim())){
            setRequestFocusInThread(awardAddReportingReqForm.cmbFrequency);
            CoeusOptionPane.showInfoDialog(
            coeusMessageResources.parseMessageKey(FREQUENCY_VALIDATE));
            return false;
        }
        if(EMPTY.equals(awardAddReportingReqForm.cmbStatus.getSelectedItem().toString().trim())){
            setRequestFocusInThread(awardAddReportingReqForm.cmbStatus);
            CoeusOptionPane.showInfoDialog(
            coeusMessageResources.parseMessageKey(STATUS_VALIDATE));
            return false;
        }
        
        if(getFinalReportFlag()){
            if(EMPTY.equals(awardAddReportingReqForm.cmbDistribution.getSelectedItem().toString().trim())){
                setRequestFocusInThread(awardAddReportingReqForm.cmbDistribution);
                CoeusOptionPane.showInfoDialog(
                coeusMessageResources.parseMessageKey(DISTRIBUTION_VALIDATE));
                return false;
            }
        }
        
        //Commented for case#2268 - Report Tracking Functionality - start
//        if(awardAddReportingReqForm.tblRecipients.getRowCount() == 0){
//            CoeusOptionPane.showErrorDialog(
//            coeusMessageResources.parseMessageKey(SELECT_RECIPIENTS));
//            return false;
//        }
        //Commented for case#2268 - Report Tracking Functionality - end
        
        if(!EMPTY.equals(awardAddReportingReqForm.txtDueDate.getText().trim())){
            String date = awardAddReportingReqForm.txtDueDate.getText().trim();
            date = dateUtils.restoreDate(date, DATE_SEPARATERS);
            date = dateUtils.formatDate(date, DATE_SEPARATERS, DATE_FORMAT_DISPLAY);
            if(date == null) {
                setRequestFocusInThread(awardAddReportingReqForm.txtDueDate);
                CoeusOptionPane.showInfoDialog(
                coeusMessageResources.parseMessageKey(DATE_VALIDATE)
                );
                return false;
            }
        }
        return true;
    }
    
    /** This method triggers all actions based on the event occured
     * @param actionEvent takes the actionEvent 
     */
    public void actionPerformed(ActionEvent actionEvent ) {
        Object source = actionEvent.getSource();
        if(source.equals(awardAddReportingReqForm.btnOK)){
            try{
                if(validate()){
                    awardAddRepReqCellEditor.stopCellEditing();
                    performUpdate();
                    saveFormData();
                    dlgReportingReqForm.dispose();
                }
            }catch (CoeusUIException coeusUIException ){
                coeusUIException.printStackTrace();
            }
        }else if(source.equals(awardAddReportingReqForm.btnCancel)){
            performCancelAction();
        }else if(source.equals(awardAddReportingReqForm.btnAdd)){
            performAddAction();
        }else if(source.equals(awardAddReportingReqForm.btnDelete)){
            performDeleteAction();
        }
    }
    
    
    /*Method for populating combo*/
    /**
     * Populates the ComboBox with data got from the server.
     */    
    public void populateCombo(){
        /*For populating the Type Combo*/
        cvReportTypeCode = new CoeusVector();
        
        /*For populating the Frequency Combo*/
        cvReportClassFrequency = new CoeusVector();
        
        /*For populating the Frequency Base Combo*/
        cvFrequencyBase  = new CoeusVector();
        
        /*For populating the Distribution combo*/
        cvDistribution = new CoeusVector();
        
        
        /*For storing the data got form server*/
        Hashtable htDataFromServer= new Hashtable();
        
        try{
            htDataFromServer = getDataFromServer();
        }catch (CoeusUIException coeusUIException){
            CoeusOptionPane.showErrorDialog(coeusUIException.getMessage());
            coeusUIException.printStackTrace();
        }
        cvReportTypeCode = (CoeusVector)htDataFromServer.get(ReportBean.class);
        cvReportClassFrequency = (CoeusVector)htDataFromServer.get(ValidReportClassReportFrequencyBean.class);
        cvFrequencyBase  = (CoeusVector)htDataFromServer.get(FrequencyBaseBean.class);
        cvDistribution = (CoeusVector)htDataFromServer.get(KeyConstants.DISTRIBUTION);
        
        setDataToStatusCombo();
        setDataToOspCombo();
        
        // if(cvReportTypeCode != null && functionType == TypeConstants.ADD_MODE ){
        ComboBoxBean emptyBean = new ComboBoxBean(EMPTY, EMPTY);
        cvReportTypeCode.add(0, emptyBean);
        awardAddReportingReqForm.cmbType.setModel(new DefaultComboBoxModel(cvReportTypeCode));
        
        setDataToFrequencyCombo();
        //        //}else if(cvReportTypeCode != null && functionType == TypeConstants.MODIFY_MODE){
        //            awardReportTermsForm.cmbType.setModel(new DefaultComboBoxModel(cvReportTypeCode));
        //
        //            //            System.out.println("Rep Desp:"+awardReportTermsBean.getReportDescription ());
        //            ComboBoxBean comboBean = new ComboBoxBean(""+awardReportTermsBean.getReportCode(),awardReportTermsBean.getReportDescription());
        //            awardReportTermsForm.cmbType.setSelectedItem(comboBean);
        //            setDataToFrequencyCombo();
        //        }
    }
    
    /*For getting the data for populating combos*/
    /**
     * Gets the ComboBox data from the server.
     */    
    public Hashtable getDataFromServer() throws CoeusUIException{
        Hashtable htReportTerms = null;
        /*Uncheck hard codeded the data*/
        Integer reportClassCode = new Integer(reportComboBean.getCode());
        
        RequesterBean requesterBean = new RequesterBean();
        ResponderBean responderBean = new ResponderBean();
        requesterBean.setFunctionType(GET_VALID_REPORT_FREQUENCY);
        
        /*Uncheck hard codeded the data*/
        requesterBean.setDataObject(reportClassCode);
        
        AppletServletCommunicator comm = new AppletServletCommunicator(connect, requesterBean);
        comm.setRequest(requesterBean);
        comm.send();
        responderBean = comm.getResponse();
        
        if(responderBean.isSuccessfulResponse()) {
            htReportTerms  = (Hashtable)responderBean.getDataObject();
        }else{
            throw new CoeusUIException(responderBean.getMessage(), CoeusUIException.ERROR_MESSAGE);
        }
        return htReportTerms ;
    }
    
    /*Populates Frequency Combo*/
    /**
     * Sets the data to Frequency Combo.
     */    
    public void setDataToFrequencyCombo(){
        CoeusVector tmpReportClassFrequency = new CoeusVector();
        CoeusVector cvFrequencydata = new CoeusVector();
        ComboBoxBean frequencyBean = null;
        ComboBoxBean comboBoxBean = null;
        ComboBoxBean emptyBean = new ComboBoxBean(EMPTY,EMPTY);
        
        ValidReportClassReportFrequencyBean validReportFrequencyBean = null;
        
        comboBoxBean = (ComboBoxBean)awardAddReportingReqForm.cmbType.getSelectedItem();
        
        String reportTypeCode = comboBoxBean.getCode();
        if(!reportTypeCode.equals(EMPTY)){
            Equals eqFrequency = new Equals("reportCode", new Integer(reportTypeCode));
            tmpReportClassFrequency = cvReportClassFrequency.filter(eqFrequency);
        }
        
        for(int row = 0; row < tmpReportClassFrequency.size(); row++){
            validReportFrequencyBean  =
            (ValidReportClassReportFrequencyBean)tmpReportClassFrequency.elementAt(row);
            frequencyBean = new ComboBoxBean(""+validReportFrequencyBean.getFrequencyCode(), validReportFrequencyBean.getFrequencyDescription());
            cvFrequencydata.addElement(frequencyBean);
        }
        
        cvFrequencydata.add(0,emptyBean);
        
        awardAddReportingReqForm.cmbFrequency.setModel(new DefaultComboBoxModel(cvFrequencydata));
        
        //        if(functionType == TypeConstants.MODIFY_MODE){
        //            if(awardReportTermsBean.getFrequencyCode() == -1){
        //                awardAddReportingReqForm.cmbFrequency.setSelectedItem(emptyBean);
        //            }else if(!dlgReportingReqForm.isVisible() ){
        //                comboBoxBean = new ComboBoxBean(""+awardReportTermsBean.getFrequencyCode(),awardReportTermsBean.getFrequencyDescription());
        //                awardAddReportingReqForm.cmbFrequency.setSelectedItem(comboBoxBean);
        //                ItemEvent itemEvent = new ItemEvent(awardAddReportingReqForm.cmbFrequency, 0, null, ItemEvent.SELECTED);
        //                itemStateChanged(itemEvent);
        //            }
        //        }
        //        ItemEvent itemEvent = new ItemEvent(awardAddReportingReqForm.cmbFrequency, 0, null, ItemEvent.SELECTED);
        //        itemStateChanged(itemEvent);
    }
    
    /*Populates Frequency Base Combo*/
    /**
     * Sets the data to Frequency Base Combo.
     */    
    public void setDataToFrequencyBaseCombo(){
        CoeusVector cvtmpFrequencyBaseCode = new CoeusVector();
        
        ComboBoxBean comboBean = new ComboBoxBean();
        ComboBoxBean emptyBean = new ComboBoxBean(EMPTY, EMPTY);
        
        comboBean = (ComboBoxBean)awardAddReportingReqForm.cmbFrequency.getSelectedItem();
        
        /*If data is present in frequency base combobox and del is pressed for
         *frequency base combobox,then should not clear the frequency base combobox
         */
        
        //        if(EMPTY.equals (comboBean.getCode()) && delPressed){
        //            return ;
        //        }
        
        if(EMPTY.equals(comboBean.getCode())){
            awardAddReportingReqForm.cmbFrequencyBase.setModel(new DefaultComboBoxModel(cvtmpFrequencyBaseCode));
            return ;
        }
        
        String frequencyCode = comboBean.getCode();
        Equals eqFrequencyBase = new Equals("frequencyCode",new Integer(frequencyCode));
        cvtmpFrequencyBaseCode = cvFrequencyBase.filter(eqFrequencyBase);
        //if(functionType == TypeConstants.ADD_MODE){
        cvtmpFrequencyBaseCode.add(0, emptyBean);
        //  }
        
        awardAddReportingReqForm.cmbFrequencyBase.setModel(new DefaultComboBoxModel(cvtmpFrequencyBaseCode));
        //  awardReportTermsForm.cmbFrequencyBase.setShowCode (true);
        //        if(functionType == TypeConstants.MODIFY_MODE && !delPressed){
        //            if(!dlgReportingReqForm.isVisible() ){
        //                comboBean = new ComboBoxBean(""+awardReportTermsBean.getFrequencyBaseCode(),awardReportTermsBean.getFrequencyBaseDescription());
        //                //System.out.println("Freq Base Desp:"+ awardReportTermsBean.getFrequencyDescription ());
        //
        //                awardReportTermsForm.cmbFrequencyBase.setSelectedItem(comboBean);
        //
        //            }
        //        }
        
    }
    
    /*Populates OSP Distribution Combo*/
    /**
     * Sets the data to OSP Distribution Combo.
     */    
    public void setDataToOspCombo(){
        ComboBoxBean emptyBean = new ComboBoxBean(EMPTY, EMPTY);
        cvDistribution.add(0, emptyBean);
        
        awardAddReportingReqForm.cmbDistribution.setModel(new DefaultComboBoxModel(cvDistribution));
        
        //        if(functionType == TypeConstants.MODIFY_MODE){
        //            String strDesc = awardReportTermsBean.getOspDistributionDescription();
        //            int code = awardReportTermsBean.getOspDistributionCode();
        //            ComboBoxBean comboBean = new ComboBoxBean(""+code,strDesc);
        //            awardReportTermsForm.cmbOSPDistribution.setSelectedItem(comboBean);
        //            if(dlgAwardReportTermsForm.isVisible()){
        //                modified = true;
        //            }
        //        }
    }
    
    /*Populates the Status combo*/
    /**
     * Sets the data to Status Combo.
     */    
    public void setDataToStatusCombo(){
        ComboBoxBean emptyBean = new ComboBoxBean(EMPTY, EMPTY);
        cvStatus.add(0, emptyBean);
        awardAddReportingReqForm.cmbStatus.setModel(new DefaultComboBoxModel(cvStatus));
    }
    
    /*Is fired when the data in the combobox changes*/
    /**
     * Listener which listens to the item state change.
     */    
    public void itemStateChanged(ItemEvent itemEvent) {
        
        if(itemEvent.getStateChange() == itemEvent.DESELECTED){
            return ;
        }
        Object source = itemEvent.getSource();
        
        if(source.equals(awardAddReportingReqForm.cmbType)){
            setDataToFrequencyCombo();
            if(dlgReportingReqForm.isVisible()) {
                modified = true;
            }
        }else if(source.equals(awardAddReportingReqForm.cmbFrequency)){
            setDataToFrequencyBaseCombo();
            if(dlgReportingReqForm.isVisible()) {
                modified = true;
            }
        }else if(source.equals(awardAddReportingReqForm.cmbFrequencyBase)){
//            CoeusVector cvEmpty = new CoeusVector();
//            ComboBoxBean emptyBean = new ComboBoxBean(EMPTY, EMPTY);
            setDataToFrequencyBaseCombo();
        }else if(source.equals(awardAddReportingReqForm.cmbDistribution)){
            if(dlgReportingReqForm.isVisible()) {
                modified = true;
            }
        }else if(source.equals(awardAddReportingReqForm.cmbStatus)){
            if(dlgReportingReqForm.isVisible()){
                modified = true;
            }
        }
    }
    
    /*For setting the data to award report terms bean got from add recipients*/
    /**
     * Sets the data got from Award Contact Details Bean to Award Rep Req Bean
     */    
    public void setDataToBean(CoeusVector cvData){
        CoeusVector cvPersons = new CoeusVector();
        
        String rolodexName;
        
        for(int i = 0; i<cvData.size(); i++){
            boolean duplicate = false;
            AwardContactDetailsBean awardContactDetailsBean = new AwardContactDetailsBean();
            AwardReportReqBean awardReportReqBean = new AwardReportReqBean();
            awardContactDetailsBean = (AwardContactDetailsBean)cvData.elementAt(i) ;
            
            awardReportReqBean.setContactTypeDescription(
            awardContactDetailsBean.getContactTypeDescription()== null ?EMPTY:awardContactDetailsBean.getContactTypeDescription());
            if(awardContactDetailsBean.getContactTypeCode() == 0){
                awardReportReqBean.setContactTypeCode(Integer.parseInt("-1"));
            }else{
                awardReportReqBean.setContactTypeCode(awardContactDetailsBean.getContactTypeCode());
            }
            
            awardReportReqBean.setRolodexId(awardContactDetailsBean.getRolodexId());
            
            String firstName = (awardContactDetailsBean.getFirstName() == null ?EMPTY:awardContactDetailsBean.getFirstName());
            if ( firstName.length() > 0) {
                String suffix = (awardContactDetailsBean.getSuffix() == null ?EMPTY:awardContactDetailsBean.getSuffix());
                String prefix = (awardContactDetailsBean.getPrefix() == null ?EMPTY:awardContactDetailsBean.getPrefix());
                String middleName = (awardContactDetailsBean.getMiddleName() == null ?EMPTY:awardContactDetailsBean.getMiddleName());
                rolodexName = (awardContactDetailsBean.getLastName() + " "+
                suffix+", "+ prefix+" "+ firstName+" "+
                middleName).trim();
                //reportTermsBean.setOrganization (rolodexName);
                awardReportReqBean.setFirstName(firstName);
                awardReportReqBean.setSuffix(suffix);
                awardReportReqBean.setPrefix(prefix);
                awardReportReqBean.setMiddleName(middleName);
                awardReportReqBean.setLastName(awardContactDetailsBean.getLastName().trim());
                
            }
            if(awardContactDetailsBean.getOrganization() == null){
                rolodexName = EMPTY;
            }else{
                rolodexName = awardContactDetailsBean.getOrganization().trim();
            }
            awardReportReqBean.setOrganization(rolodexName);
            
            awardReportReqBean.setNumberOfCopies(noOfCopies);
            
            
            awardReportReqBean.setAcType(TypeConstants.INSERT_RECORD);
            
            
            duplicate = checkDuplicateRow(awardReportReqBean);
            if(duplicate){
                CoeusOptionPane.showInfoDialog(
                coeusMessageResources.parseMessageKey(DUPILCATE_CONTACT));
                continue ;
            }else{
                cvPersons.add(awardReportReqBean);
            }
        }//End of for loop
        cvRepRequirements.addAll(cvPersons);
        awardAddRepReqTableModel.fireTableDataChanged();
    }
    
    
    /*Checks for diuplicate entries*/
    private boolean checkDuplicateRow(AwardReportReqBean awardReportReqBean ){
        AwardReportReqBean reportReqBean = new AwardReportReqBean();
        reportReqBean = awardReportReqBean;
        String rolodexName = null;
        //        CoeusVector coeusVector = new CoeusVector();
        
        if(cvRepRequirements!=null && cvRepRequirements.size() > 0){
            for(int index = 0; index < cvRepRequirements.size(); index++){
                AwardReportReqBean reportReqBeanExisting =
                (AwardReportReqBean)cvRepRequirements.get(index);
                
                String firstName = (reportReqBeanExisting.getFirstName() == null ?EMPTY:reportReqBeanExisting .getFirstName());
                if ( firstName.length() > 0 ) {
                    String suffix = (reportReqBeanExisting .getSuffix() == null ?EMPTY:reportReqBeanExisting .getSuffix());
                    String prefix = (reportReqBeanExisting .getPrefix() == null ?EMPTY:reportReqBeanExisting .getPrefix());
                    String middleName = (reportReqBeanExisting .getMiddleName() == null ?EMPTY:reportReqBeanExisting.getMiddleName());
                    rolodexName = (reportReqBeanExisting .getLastName() + " "+
                    suffix+", "+ prefix+" "+ firstName+" "+
                    middleName).trim();
                }
                
                /*for cmparing */
                String cmpRolodexName = null;
                String cmpFirstName = (reportReqBean.getFirstName() == null ?EMPTY:reportReqBean .getFirstName());
                if ( firstName.length() > 0) {
                    String cmpSuffix = (reportReqBean .getSuffix() == null ?EMPTY:reportReqBean .getSuffix());
                    String cmpPrefix = (reportReqBean .getPrefix() == null ?EMPTY:reportReqBean .getPrefix());
                    String cmpMiddleName = (reportReqBean .getMiddleName() == null ?EMPTY:reportReqBean .getMiddleName());
                    cmpRolodexName = (reportReqBean .getLastName() + " "+
                    cmpSuffix+", "+ cmpPrefix+" "+ cmpFirstName+" "+
                    cmpMiddleName).trim();
                }
                int contactCode = reportReqBeanExisting.getContactTypeCode();
                if(rolodexName != null && rolodexName.equals(cmpRolodexName) ){
                    // if(contactCode > 0){
                    if(contactCode == reportReqBean.getContactTypeCode()){
                        return true;
                        //     }
                    }
                }else if(reportReqBeanExisting.getOrganization().equals(reportReqBean.getOrganization())){
                    return true;
                }
            }
        }
        return false;
    }
    
    /*Method to get the final report flag */  
    private boolean getFinalReportFlag(){
        ComboBoxBean comboBoxBean = (ComboBoxBean)awardAddReportingReqForm.cmbType.getSelectedItem();
        CoeusVector cvRepType = cvReportTypeCode.filter(new Equals("code", comboBoxBean.getCode().trim()));
        ReportBean reportBean  = (ReportBean)cvRepType.elementAt(0);
        boolean repFlag = reportBean.isFinalReportFlag();
        return repFlag;
    }
    
    /*For Updating*/
    /**
     * Sets properties to all Award Rep Req Beans.
     */    
    public void performUpdate(){
        ComboBoxBean comboBoxBean;
        //Commented/Added for case#2268 - Report Tracking Functionality - start
//        for(int index = 0; index < cvRepRequirements.size() ; index ++){
//            AwardReportReqBean awardReportReqBean = (AwardReportReqBean)cvRepRequirements.get(index);
            AwardReportReqBean awardReportReqBean = new AwardReportReqBean();
            awardReportReqBean.setMitAwardNumber(awardBaseBean.getMitAwardNumber());
            reportNo = reportNo+1;
            awardReportReqBean.setReportNumber(reportNo);
            
            awardReportReqBean.setReportClassCode(Integer.parseInt(reportComboBean.getCode()));
            
            comboBoxBean = (ComboBoxBean)awardAddReportingReqForm.cmbStatus.getSelectedItem();
            awardReportReqBean.setReportStatusCode(Integer.parseInt(comboBoxBean.getCode()));
            awardReportReqBean.setReportStatusDescription(comboBoxBean.getDescription());
            
            comboBoxBean = (ComboBoxBean)awardAddReportingReqForm.cmbType.getSelectedItem();
            awardReportReqBean.setReportCode(Integer.parseInt(comboBoxBean.getCode()));
            awardReportReqBean.setReportDescription(comboBoxBean.getDescription().trim());
        
            comboBoxBean = (ComboBoxBean)awardAddReportingReqForm.cmbFrequency.getSelectedItem();
            awardReportReqBean.setFrequencyCode(Integer.parseInt(comboBoxBean.getCode()));
            awardReportReqBean.setFrequencyDescription(comboBoxBean.getDescription().trim());
            
            if(EMPTY.equals(awardAddReportingReqForm.cmbFrequencyBase.getSelectedItem().toString().trim())){
                awardReportReqBean.setFrequencyBaseCode(-1);
            }else{
                comboBoxBean = (ComboBoxBean)awardAddReportingReqForm.cmbFrequencyBase.getSelectedItem();
                awardReportReqBean.setFrequencyBaseCode(Integer.parseInt(comboBoxBean.getCode()));
                awardReportReqBean.setFrequencyBaseDescription(comboBoxBean.getDescription().trim());
            }
            
            if(EMPTY.equals(awardAddReportingReqForm.cmbDistribution.getSelectedItem().toString().trim())){
                awardReportReqBean.setOspDistributionCode(-1);
            }else{
                comboBoxBean = (ComboBoxBean)awardAddReportingReqForm.cmbDistribution.getSelectedItem();
                awardReportReqBean.setOspDistributionCode(Integer.parseInt(comboBoxBean.getCode()));
                awardReportReqBean.setOspDistributionDescription(comboBoxBean.getDescription().trim());
            }
            
            if( awardReportReqBean.getContactTypeCode() == 0){
                awardReportReqBean.setContactTypeCode(-1);
            }
            
            awardReportReqBean.setOverdueCounter(0);
            
            String strDueDate = awardAddReportingReqForm.txtDueDate.getText().toString().trim();
            if(!strDueDate.equals(EMPTY)){
                strDueDate =dateUtils.restoreDate(strDueDate,DATE_SEPARATERS);
                try{
                    Date dueDate = dtFormat.parse(strDueDate);
                    awardReportReqBean.setDueDate( new java.sql.Date(dueDate.getTime()));
                }catch (ParseException parseException){
                    parseException.printStackTrace();
                }
            }
//        }
          cvRepRequirements.add(awardReportReqBean);
          //Commented/Added for case#2268 - Report Tracking Functionality - end
    }
    /**This is an Inner class of the Add reporting requirement Form.
     * Specifies the data for the Award.
     * This table model specifies only the Add Reporting requirement details.
     */
    public class AwardAddRepReqTableModel extends AbstractTableModel {
        CoeusVector cvTempRepRequirements = new CoeusVector();
        /**
         * Creates a new instance of Award add reporting requirement TableModel
         */
        AwardAddRepReqTableModel() {
        }
        
        /** Specifies the column Names */
        private String[] colNames = {"Contact","Address","Copies"};
        
        /** Specifies the column class and its data types as objects */
        private Class colClass[] = {String.class, String.class, String.class};
        
        /**
         * This method is to check whether the specified cell is editable or not
         * @param int row
         * @param int col
         * @return boolean
         */
        public boolean isCellEditable(int row,int col) {
            if(col == 2){
                return true;
            }else{
                return false;
            }
        }
        
        /**
         * This method is to get the column name
         * @param int column
         * @return String
         */
        public String getColumnName(int column) {
            return colNames[column];
        }
        
        /**
         * This method is to get the column class
         * @param int columnIndex
         * @return Class
         */
        public Class getColumnClass(int columnIndex) {
            return colClass [columnIndex];
        }
        
        /**
         * This method is to get the column count
         * @return int
         */
        public int getColumnCount() {
            return colNames.length;
        }
        
        /**
         * This method is to get the row count
         * @return int
         */
        public int getRowCount() {
            if(cvTempRepRequirements.size() < 1){
                return 0;
            }else{
                return cvTempRepRequirements.size();
            }
        }
        
        /**
         * This method will specifies the data for the table model.
         * @param CoeusVector cvData
         * @return void
         */
        public void setData(CoeusVector cvRepRequirements){
            this.cvTempRepRequirements = cvRepRequirements;
        }
        
        /**
         *This method is to get the value with respect to the row and column
         *@param int row
         *@param int col
         *@return Object
         */
        public Object getValueAt(int row, int col) {
            if(cvTempRepRequirements.size()<1){
                return null ;
            }
            AwardReportReqBean reportReqBean= (AwardReportReqBean)cvTempRepRequirements.get(row);
            switch(col){
                case CONTACT_INDEX:
                    return reportReqBean.getContactTypeDescription();
                case ADDRESS_INDEX:
                    String rolodexName;
                    String firstName = (reportReqBean.getFirstName() == null ?EMPTY:reportReqBean.getFirstName());
                    if ( firstName.length() > 0) {
                        String suffix = (reportReqBean.getSuffix() == null ?EMPTY:reportReqBean.getSuffix());
                        String prefix = (reportReqBean.getPrefix() == null ?EMPTY:reportReqBean.getPrefix());
                        String middleName = (reportReqBean.getMiddleName() == null ?EMPTY:reportReqBean.getMiddleName());
                        rolodexName = (reportReqBean.getLastName() + " "+
                        suffix+", "+ prefix+" "+ firstName+" "+
                        middleName).trim();
                        return rolodexName;
                    } else {
                        if(reportReqBean.getOrganization() == null){
                            rolodexName = EMPTY;
                            return rolodexName ;
                        }else{
                            return reportReqBean.getOrganization();
                        }
                    }
                    
                case COPIES_INDEX:
//                    if(reportReqBean.getNumberOfCopies() == 0){
//                        return null;
//                    }else{
                        Integer noOfCopies  = new Integer(reportReqBean.getNumberOfCopies());
                        return noOfCopies;
//                    }
            }
            return EMPTY;
        }
        
        /**
         * This method is to set the value with respect to the row and column
         * @param Object aValue
         * @param int row
         * @param int col
         */
        public void setValueAt(Object aValue, int row, int col) {
            AwardReportTermsBean reportReqBean = (AwardReportTermsBean)cvTempRepRequirements.get(row);
            boolean changed = false;
            switch(col){
                case COPIES_INDEX:
                    if(EMPTY.equals(aValue.toString().trim())){
                        reportReqBean.setNumberOfCopies(0);
                        changed = modified = true;
                        break;
                    }
                    int value = Integer.parseInt(aValue.toString());
                    if (value != reportReqBean.getNumberOfCopies()) {
                        reportReqBean.setNumberOfCopies(value);
                        changed = modified = true;
                    }
                    break;
                default :
            }
            
            if(changed){
                if(reportReqBean.getAcType() == null) {
                    reportReqBean.setAcType(TypeConstants.UPDATE_RECORD);
                }
            }
        }
    }
    
    /** An inner class provides the editor for the reporting details.
     */
    public class AwardAddRepReqCellEditor extends AbstractCellEditor implements TableCellEditor{
        private CoeusTextField txtComponent;
        private int column;
        
        public AwardAddRepReqCellEditor(){
            txtComponent = new CoeusTextField();
        }
        
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            this.column = column;
            switch(column){
                case COPIES_INDEX:
                    txtComponent.setDocument(new JTextFieldFilter(JTextFieldFilter.NUMERIC,2));
                    if(value == null){
                        txtComponent.setText(EMPTY);
                    }else{
                        txtComponent.setText(value.toString());
                    }
                    return txtComponent;
            }
            return txtComponent;
        }
        
        public Object getCellEditorValue() {
            switch(column){
                case CONTACT_INDEX:
                case ADDRESS_INDEX:
                case COPIES_INDEX:
                    return txtComponent.getText();
            }
            return txtComponent;
        }
    }// End of reporting requirement Editor...................
    
    
    /** An Inner class, provides renderer for the reporting requirement details
     */
    public class AwardAddRepReqRenderer extends DefaultTableCellRenderer
    implements TableCellRenderer {
        JTextField txtCopiesComponent;
        /**
         * Default Constructor
         */
        public AwardAddRepReqRenderer() {
            setOpaque(true);
        }
        
        public Component getTableCellRendererComponent(JTable table,Object value,
        boolean isSelected,boolean hasFocus ,int row, int col) {
            switch(col) {
                case CONTACT_INDEX:
                case ADDRESS_INDEX:
                case COPIES_INDEX:
                    if(value == null || value.toString().trim().equals(EMPTY)){
                        setText(EMPTY);
                    }else{
                        setText(value.toString());
                    }
                    
                    if(isSelected){
                        setBackground(Color.YELLOW);
                        setForeground(Color.BLACK);
                    }else{
                        setBackground(Color.WHITE);
                        setForeground(Color.BLACK);
                    }
                    return this;
            }
            return this;
        }
    }// end of class reporting requirement Renderer.
    
    /*For adding thr recipients*/
    /**
     * Adds the recipients.
     */    
    public void performAddAction(){
        try{
            dlgReportingReqForm.setCursor(new Cursor(Cursor.WAIT_CURSOR));
            awardAddRecipientsController = new AwardAddRecipientsController(awardBaseBean,getFunctionType(), queryKey);
            awardAddRecipientsController.registerObserver(this);
            awardAddRecipientsForm.lblAwardContactList.setOpaque(true);
            awardAddRecipientsForm.lblAwardContactList.setForeground(Color.blue);
            awardAddRecipientsForm.lblContactDetails.setForeground(Color.BLUE);
            awardAddRecipientsController.display();
        }
        finally{
            dlgReportingReqForm.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        }
    }
    
    /*Performs the action of cancel button*/
    private void performCancelAction(){
        if(isSaveRequired() || modified){
            int option = CoeusOptionPane.showQuestionDialog(
            coeusMessageResources.parseMessageKey(SAVE_CHANGES),
            CoeusOptionPane.OPTION_YES_NO_CANCEL,
            JOptionPane.YES_OPTION);
            switch( option ) {
                case (JOptionPane.YES_OPTION ):
                    setSaveRequired(true);
                    try{
                        if( validate() ){
                            awardAddRepReqCellEditor.stopCellEditing();
                            performUpdate();
                            saveFormData();
                            dlgReportingReqForm.dispose();
                        }
                    }catch (Exception exception){
                        exception.printStackTrace();
                    }
                    break;
                case(JOptionPane.NO_OPTION ):
                    dlgReportingReqForm.dispose();
                    break;
                default:
                    break;
            }
        }else{
            dlgReportingReqForm.dispose();
        }
    }
    
    /*Sets the editor,renderer for the columns*/
    private void setTableEditors(){
        try{
            JTableHeader tableHeader = awardAddReportingReqForm.tblRecipients.getTableHeader();
            tableHeader.setReorderingAllowed(true);
            tableHeader.setFont(CoeusFontFactory.getLabelFont());
            //tableHeader.addMouseListener(new ColumnHeaderListener());
            awardAddReportingReqForm.tblRecipients.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
            awardAddReportingReqForm.tblRecipients.setRowHeight(22);
            awardAddReportingReqForm.tblRecipients.setShowHorizontalLines(true);
            awardAddReportingReqForm.tblRecipients.setShowVerticalLines(true);
            awardAddReportingReqForm.tblRecipients.setOpaque(false);
            awardAddReportingReqForm.tblRecipients.setSelectionMode(
            DefaultListSelectionModel.SINGLE_SELECTION);
            TableColumn column;
            
            int prefWidth[] = {195,195,94};
            for(int index = 0; index < prefWidth.length; index++) {
                column = awardAddReportingReqForm.tblRecipients.getColumnModel().getColumn(index);
                column.setPreferredWidth(prefWidth[index]);
                column.setCellEditor(awardAddRepReqCellEditor);
                column.setCellRenderer(awardAddRepReqRenderer);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    
    /*Deletes the selected row*/
    private void performDeleteAction() {
        //cvLastRowDeleted = new CoeusVector();
        awardAddRepReqCellEditor.stopCellEditing();
        int selectedRow = awardAddReportingReqForm.tblRecipients.getSelectedRow();
        
        if(selectedRow == -1 || cvRepRequirements.size() == 0){
            CoeusOptionPane.showErrorDialog(coeusMessageResources.parseMessageKey(
            (ROW_TO_DELETE)));
            return ;
        }
        
        if(selectedRow != -1 && selectedRow >= 0){
            String mesg = DELETE_CONFIRMATION;
            int selectedOption = CoeusOptionPane.showQuestionDialog(
            coeusMessageResources.parseMessageKey(mesg),
            CoeusOptionPane.OPTION_YES_NO,
            CoeusOptionPane.DEFAULT_YES);
            if(selectedOption == CoeusOptionPane.SELECTION_YES) {
                //AwardReportTermsBean deletedReportTerms = (AwardReportTermsBean)cvRepRequirements.get(selectedRow);
                
                if(cvRepRequirements!=null && cvRepRequirements.size() > 0){
                    cvRepRequirements.removeElementAt(selectedRow);
                    awardAddRepReqTableModel.fireTableRowsDeleted(selectedRow, selectedRow);
                    modified = true;
                }
                
                //cvDeletedItem.add(deletedReportTerms);
                
                if(selectedRow >0){
                    awardAddReportingReqForm.tblRecipients.setRowSelectionInterval(
                    selectedRow-1,selectedRow-1);
                    awardAddReportingReqForm.tblRecipients.scrollRectToVisible(
                    awardAddReportingReqForm.tblRecipients.getCellRect(
                    selectedRow -1 ,0, true));
                }else{
                    if(awardAddReportingReqForm.tblRecipients.getRowCount()>0){
                        awardAddReportingReqForm.tblRecipients.setRowSelectionInterval(0,0);
                    }
                }
                setSaveRequired(true);
                modified = true;
            }
        }
    }
    
    /**
     * This method is called whenever the observed object is changed. An
     * application calls an <tt>Observable</tt> object's
     * <code>notifyObservers</code> method to have all the object's
     * observers notified of the change.
     *
     * @param   o     the observable object.
     * @param   arg   an argument passed to the <code>notifyObservers</code>
     *                 method.
     */
    public void update(Observable o, Object arg) {
        if (arg instanceof CoeusVector || arg instanceof String){
            if(arg.equals(EMPTY)){
                awardAddRecipientsController.dispose();
            }else{
                cvSelectedPersons = new CoeusVector();
                cvSelectedPersons = (CoeusVector)arg;
                if(cvSelectedPersons != null){
                    setDataToBean(cvSelectedPersons);
                    setSaveRequired(true);
                    modified = true;
                    awardAddReportingReqForm.tblRecipients.setRowSelectionInterval(0, 0);
                    awardAddRecipientsController.dispose();
                }
            }
        }
    }
    
    /*Adapter class for Date Field*/
    public class CustomFocusAdapter extends FocusAdapter{
        String dueDate;
        /**
         * listens to focus gained event.
         * @param focusEvent focusEvent
         */        
        public void focusGained(FocusEvent focusEvent) {
            if(focusEvent.isTemporary()){
                return ;
            }
            Object source = focusEvent.getSource();
            if(source.equals(awardAddReportingReqForm.txtDueDate)){
                dueDate = awardAddReportingReqForm.txtDueDate.getText();
                dueDate = dateUtils.restoreDate(dueDate, DATE_SEPARATERS);
                awardAddReportingReqForm.txtDueDate.setText(dueDate);
                //System.out.println("Due Date(Focus Gained):"+dueDate);
            }
        }
        
        /**
         * listens to focus lost event.
         * @param focusEvent focusEvent
         */
        public void focusLost(FocusEvent focusEvent) {
            if(focusEvent.isTemporary()){
                return ;
            }
            
            Object source = focusEvent.getSource();
            if(source.equals(awardAddReportingReqForm.txtDueDate)){
                String date;
                date = awardAddReportingReqForm.txtDueDate.getText();
                if(!dueDate.equals(date)){
                    modified = true;
                }
                
                if(date.equals(EMPTY)) {
                    return ;
                }
                
                date = dateUtils.formatDate(date, DATE_SEPARATERS, DATE_FORMAT_DISPLAY);
                if(date == null) {
                    setRequestFocusInThread(awardAddReportingReqForm.txtDueDate);
                    CoeusOptionPane.showInfoDialog(
                    coeusMessageResources.parseMessageKey(DATE_VALIDATE));
                }else {
                    awardAddReportingReqForm.txtDueDate.setText(date);
                }
            }
        }
    }
    
    
//    /** This is Iconrenderer to display HAND icon for the selected row in the table
//     */
//    static class IconRenderer  extends DefaultTableCellRenderer {
//        
//        /** This holds the Image Icon of Hand Icon
//         */
//        private final ImageIcon HAND_ICON =
//        new ImageIcon(getClass().getClassLoader().getResource(
//        CoeusGuiConstants.HAND_ICON));
//        private final ImageIcon EMPTY_ICON = null;
//        /** Default Constructor*/
//        IconRenderer() {
//        }
//        
//        public Component getTableCellRendererComponent(JTable table,
//        Object value, boolean isSelected, boolean hasFocus, int row,
//        int column) {
//            
//            setText((String)value);
//            setOpaque(false);
//            /* if row is selected the place the icon in this cell wherever this
//               renderer is used. */
//            if (isSelected) {
//                setIcon(HAND_ICON);
//            } else {
//                setIcon(EMPTY_ICON);
//            }
//            return this;
//        }
//        
//    }//End Icon Rendering inner class
//    
//    /**
//     * Inner class which is used to provide empty header for the Icon Column.
//     */
//    
//    class EmptyHeaderRenderer extends JList implements TableCellRenderer {
//        /**
//         * Default constructor to set the default foreground/background
//         * and border properties of this renderer for a cell.
//         */
//        EmptyHeaderRenderer() {
//            setOpaque(true);
//            setForeground(UIManager.getColor("TableHeader.foreground"));
//            setBackground(UIManager.getColor("TableHeader.background"));
//            setBorder(new EmptyBorder(0, 0, 0, 0));
//            ListCellRenderer renderer = getCellRenderer();
//            ((JLabel) renderer).setHorizontalAlignment(JLabel.CENTER);
//            setCellRenderer(renderer);
//        }
//        
//        public Component getTableCellRendererComponent(JTable table,
//        Object value,boolean isSelected, boolean hasFocus,
//        int row, int column) {
//            return this;
//        }
//    }
    
//    /*Test Function*/
//    private void getDAta(){
//        try{
//            CoeusVector cv = (CoeusVector) queryEngine.executeQuery(queryKey,AwardReportReqBean.class,CoeusVector.FILTER_ACTIVE_BEANS);
//            if(cv == null){
//                System.out.println("Null");
//            }else{
//                System.out.println("Data:"+cv.size());
//            }
//        }catch (CoeusException coeusException){
//            coeusException.printStackTrace();
//        }
//    }
    
    /*To set the focus inside the table cell*/
    private void setRequestFocusInThread(final Component component) {
        SwingUtilities.invokeLater( new Runnable() {
            public void run() {
                component.requestFocusInWindow();
            }
        });
    }
}//end of class