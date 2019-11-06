/** Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.award.controller;

import java.awt.Component;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
// JM 5-14-2012 added to resolve scrolling issues
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
// END JM
import java.util.Vector;
import java.util.HashMap;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JList;
import javax.swing.JTable;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.DefaultListModel;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import edu.mit.coeus.award.bean.AwardDetailsBean;
import edu.mit.coeus.award.bean.AwardContactDetailsBean;
import edu.mit.coeus.award.bean.AwardBaseBean;
import edu.mit.coeus.award.bean.AwardReportTermsBean;
import edu.mit.coeus.award.gui.AwardReportsForm;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.gui.event.BeanUpdatedListener;
import edu.mit.coeus.gui.event.BeanAddedListener;
import edu.mit.coeus.rolodexmaint.bean.RolodexMaintController;
import edu.mit.coeus.rolodexmaint.bean.RolodexDetailsBean;
import edu.mit.coeus.rolodexmaint.gui.RolodexMaintenanceDetailForm;
import edu.mit.coeus.utils.ComboBoxBean;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.DateUtils;
import edu.mit.coeus.utils.KeyConstants;
import edu.mit.coeus.utils.ObjectCloner;
import edu.mit.coeus.utils.TypeConstants;
import edu.mit.coeus.utils.query.Equals;
import edu.mit.coeus.utils.query.And;
import edu.mit.coeus.utils.query.QueryEngine;
import edu.mit.coeus.award.AwardConstants;
import edu.mit.coeus.utils.ScreenFocusTraversalPolicy;
import javax.swing.JScrollPane;

/**
 * AwardReportsController.java
 * Created on June 2, 2004, 2:37 PM
 * @author  Vyjayanthi
 */

public class AwardReportsController extends AwardController implements 
ActionListener, ListSelectionListener, MouseListener, BeanAddedListener, BeanUpdatedListener {
    
    /** Holds an instance of <CODE>AwardReportsForm</CODE> */
    private AwardReportsForm awardReportsForm;
    
    /** Holds an instance of <CODE>CoeusMessageResources</CODE> */
    private CoeusMessageResources coeusMessageResources;

    /** Holds an instance of <CODE>QueryEngine</CODE> */
    private QueryEngine queryEngine;
    
    /** Holds an instance of <CODE>ReportClassTableModel</CODE> */
    private ReportClassTableModel reportClassTableModel;
    
    /** Holds an instance of <CODE>ReportClassTableRenderer</CODE> */
    private ReportClassTableRenderer reportClassTableRenderer;
    
    /** Holds an instance of <CODE>ReportDetailsTableModel</CODE> */
    private ReportDetailsTableModel reportDetailsTableModel;
    
    /** Holds an instance of <CODE>ReportDetailsTableRenderer</CODE> */
    private ReportDetailsTableRenderer reportDetailsTableRenderer;
    
    /** Holds an instance of <CODE>ReportDetailsHeaderRenderer</CODE> */
    private ReportDetailsHeaderRenderer reportDetailsHeaderRenderer;
        
    /** Holds all the Report Class data */
    private CoeusVector cvReportClass;
    
    /** Holds all the Report Terms data */
    private CoeusVector cvReportDetails;
    
    /** Holds an instance of ListSelectionModel for Report Class */    
    private ListSelectionModel reportClassSelectionModel;
    
    /** Holds an instance of ListSelectionModel for Report Type */    
    private ListSelectionModel reportTypeSelectionModel;
    
    /** Holds all the report codes */
    private Vector vecReportCodes;
    
    /** To get the functionType in the renderer */
    private char functionType;
    
    /** Holds true if a bean is added, false otherwise */
    private boolean reportsModified;
    
    /** Holds the selected report class */
    private int selClass;
    
    /** Holds the newly added bean's reportDescription */
    private String selReportDescription;

    private static final java.awt.Color PANEL_BACKGROUND = 
            (java.awt.Color)javax.swing.UIManager.getDefaults().get("Panel.background");
    private static final String REQUIRED_DATEFORMAT = "dd-MMM-yyyy";
    private static final String[] fieldNames = {"frequencyCode", 
        "frequencyBaseCode", "ospDistributionCode", "dueDate"};
    
    private static final String OPENING_BRACE = " ( ";
    private static final String CLOSING_BRACE = " )";
    private static final String SPACE = " ";
    private static final String COMMA = ", ";
    private static final String HYPHEN = " - ";
    private static final int IMAGE_COLUMN = 0;
    private static final int TEXT_COLUMN = 1;
    private static final int DECREASED_ROW_HEIGHT = 22;
    private static final int INCREASED_ROW_HEIGHT = 50;
    
    private static final String REPORT_CLASS_CODE = "reportClassCode";
    private static final String REPORT_CODE = "reportCode";
    
    //Column Names of Report Details table
    private static final String FREQUENCY = "Frequency";
    private static final String FREQ_BASE = "Frequency Base";
    private static final String OSP_DIST = "Osp Distribution";
    private static final String DUE_DATE = "Due Date";
    
    /* 
    private static final int FREQ_COL = 0;
    private static final int FREQ_BASE_COL = 1;
    private static final int OSP_DIST_COL = 2;
    private static final int DUE_DATE_COL = 3;
     */
    
    private static final String SELECT_A_TYPE = "awardReports_exceptionCode.1251";
    private static final String SELECT_A_CLASS = "awardReports_exceptionCode.1252";
    private static final String SELECT_A_ROW_TO_DELETE = "orgIDCPnl_exceptionCode.1097";
    private static final String SELECT_A_ROW_TO_MODIFY = "protoBaseWin_exceptionCode.1051";
    private static final String REMOVE_TYPE = "awardReports_exceptionCode.1253";
    private static final String DELETE_ROW = "awardReports_exceptionCode.1254";
    private static final String CONFIRM_SYNC = "awardReports_exceptionCode.1255";
    private static final String ENTER_ATLEAST_ONE_REPORT = "awardReports_exceptionCode.1258";
    
    //Bug Fix:Performance Issue (Out of memory) Start 1
    private JScrollPane jscrPn;
    //Bug Fix:Performance Issue (Out of memory) End 1
    
    /** Creates a new instance of AwardReportsController 
     * @param awardBaseBean to call the constructor of the super class
     * @param functionType to set the functionType
     */
    public AwardReportsController(AwardBaseBean awardBaseBean, char functionType) {
        super(awardBaseBean);
        coeusMessageResources = CoeusMessageResources.getInstance();
        queryEngine = QueryEngine.getInstance();
        this.functionType = functionType;
        registerComponents();
        setFunctionType(functionType);
        setFormData(null);
        setRenderers();
    }
    
    /** To set the renderer to the table */
    private void setRenderers(){
        awardReportsForm.tblReportClass.setRowHeight(22);
        awardReportsForm.tblReportClass.setShowHorizontalLines(false);
        awardReportsForm.tblReportClass.setShowVerticalLines(false);
        
        //Set renderers for the Report Class table
        TableColumn column = awardReportsForm.tblReportClass.getColumnModel().getColumn(IMAGE_COLUMN);
        column.setMaxWidth(15);
        column.setMinWidth(15);
        column.setPreferredWidth(15);
        column.setCellRenderer(reportClassTableRenderer);
        column.setHeaderRenderer(new EmptyHeaderRenderer());
        
        column = awardReportsForm.tblReportClass.getColumnModel().getColumn(TEXT_COLUMN);
        column.setPreferredWidth(170);
        column.setMaxWidth(170);
        column.setCellRenderer(reportClassTableRenderer);
        column.setHeaderRenderer(new EmptyHeaderRenderer());

        //Code block to display data in single column - Start
        column = awardReportsForm.tblReportDetails.getColumnModel().getColumn(0);
        column.setPreferredWidth(672);
        column.setMaxWidth(672);
        column.setCellRenderer(reportDetailsTableRenderer);
        column.setHeaderRenderer(reportDetailsHeaderRenderer);
        //Code block to display data in single column - End

        JTableHeader header = awardReportsForm.tblReportDetails.getTableHeader();
        header.setReorderingAllowed(false);
        header.setResizingAllowed(false);        
        /* Commented to display data in single column and remove resizing
         * If uncommented for allowing resizing, the form file should be modified
         * to display four columns
        header.setResizingAllowed(true);
        header.setFont(CoeusFontFactory.getLabelFont());        

        //Set renderers for the Report Details table
        for( int index = 0; index < reportDetailsTableModel.getColumnCount(); index++){
            column = awardReportsForm.tblReportDetails.getColumnModel().getColumn(index);
            column.setMinWidth(168);
            column.setPreferredWidth(168);
            column.setCellRenderer(reportDetailsTableRenderer);
        }
         */
    }
    
    /** Displays the Form which is being controlled.
     */
    public void display() {
    }
    
    /** Perform field formatting.
     * enabling, disabling components depending on the different conditions
     */
    public void formatFields() {
        if( getFunctionType() == TypeConstants.DISPLAY_MODE ){
            
            //Disable all buttons
            awardReportsForm.btnAdd.setEnabled(false);
            awardReportsForm.btnModify.setEnabled(false);
            awardReportsForm.btnDelete.setEnabled(false);
            awardReportsForm.btnDelType.setEnabled(false);
            awardReportsForm.btnDelSync.setEnabled(false);//2796
            awardReportsForm.btnSync.setEnabled(false);
            
            awardReportsForm.tblReportClass.setBackground(PANEL_BACKGROUND);
            awardReportsForm.tblReportDetails.setBackground(PANEL_BACKGROUND);
            awardReportsForm.lstReportType.setBackground(PANEL_BACKGROUND);
            //Added with 2796: Sync to parent
        }else if (!awardBaseBean.isParent()){
            awardReportsForm.btnDelSync.setEnabled(false);
        }
        //2796 End
    }
    
    /** An overridden method of the controller
     * @return awardReportsForm returns the controlled form component
     */
    public java.awt.Component getControlledUI() {
        
        //Bug Fix:Performance Issue (Out of memory) Start 2
        //return awardReportsForm;
        //JIRA Case COEUSDEV-160, COEUSDEV-177 - START
        //jscrPn = new JScrollPane(awardReportsForm);
        //JIRA Case COEUSDEV-160, COEUSDEV-177 - END
        return jscrPn;
        //Bug Fix:Performance Issue (Out of memory) End 2
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
        awardReportsForm = new AwardReportsForm();
        awardReportsForm.tblReportDetails.setShowGrid(false);
        reportClassTableModel = new ReportClassTableModel();
        reportClassTableRenderer = new ReportClassTableRenderer();
        reportDetailsTableModel = new ReportDetailsTableModel();
        reportDetailsTableRenderer = new ReportDetailsTableRenderer();
        reportDetailsHeaderRenderer = new ReportDetailsHeaderRenderer();
                
        awardReportsForm.btnAdd.addActionListener(this);
        awardReportsForm.btnModify.addActionListener(this);
        awardReportsForm.btnDelete.addActionListener(this);
        awardReportsForm.btnDelType.addActionListener(this);
        awardReportsForm.btnDelSync.addActionListener(this);//2796
        awardReportsForm.btnSync.addActionListener(this);
        
        awardReportsForm.tblReportClass.setModel(reportClassTableModel);
        awardReportsForm.tblReportDetails.setModel(reportDetailsTableModel);
        awardReportsForm.tblReportDetails.setRowHeight(DECREASED_ROW_HEIGHT);
        awardReportsForm.tblReportDetails.addMouseListener(this);
        
        reportClassSelectionModel = awardReportsForm.tblReportClass.getSelectionModel();
        reportClassSelectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION );
        reportClassSelectionModel.addListSelectionListener( this );        
        awardReportsForm.tblReportClass.setSelectionModel(reportClassSelectionModel);
        
        reportTypeSelectionModel = awardReportsForm.lstReportType.getSelectionModel();
        reportTypeSelectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION );
        reportTypeSelectionModel.addListSelectionListener( this );        
        awardReportsForm.lstReportType.setSelectionModel(reportTypeSelectionModel);
        
        awardReportsForm.tblReportDetails.setSelectionMode(ListSelectionModel.SINGLE_SELECTION );
        
        // Added by chandra. to include screen focus traversal policy - 6-Aug-2004-start
         java.awt.Component components[] = { awardReportsForm.btnAdd,
                                                awardReportsForm.btnModify,
                                                awardReportsForm.btnDelete,
                                                awardReportsForm.btnDelType,
                                                awardReportsForm.btnSync,
                                                awardReportsForm.btnDelSync//2796
                                              };
        ScreenFocusTraversalPolicy  traversePolicy = new ScreenFocusTraversalPolicy( components );
        awardReportsForm.setFocusTraversalPolicy(traversePolicy);
        awardReportsForm.setFocusCycleRoot(true);
        

        addBeanUpdatedListener(this, AwardDetailsBean.class);
        addBeanUpdatedListener(this, AwardReportTermsBean.class);
        addBeanAddedListener(this, AwardReportTermsBean.class);
        
        //Bug Fix:1023 -- Start
        addBeanUpdatedListener(this, AwardContactDetailsBean.class);
        //Bug Fix:1023 -- End
        //JIRA Case COEUSDEV-160, COEUSDEV-177 - START
        jscrPn = new JScrollPane(awardReportsForm);
        // JM 4-10-2012 add listener to pass control to outer pane for scrolling
        jscrPn.addMouseWheelListener(new MouseWheelListener() {
            public void mouseWheelMoved(MouseWheelEvent e) {
                jscrPn.getParent().dispatchEvent(e);
            }
        });
        //JIRA Case COEUSDEV-160, COEUSDEV-177 - END
        //Added for COEUSQA-1456 : Templates-add User ID stamp & Timestamp - Start
        //Disables the Last Update and Update User components
        awardReportsForm.pnlUpdateDetails.setVisible(false);
        //COEUSQA-1456 : End
    }
    
    /** Saves the Form Data.
     */
    public void saveFormData() {
    }
    
    /** This method is used to set the form data specified in
     * <CODE> data </CODE>
     * @param data data to set to the form
     */
    public void setFormData(Object data) {        
        try{
            //Get the Report Class data and set it to the table
            cvReportClass = queryEngine.getDetails(queryKey, KeyConstants.REPORT_CLASS);
            
            //Get the Report Terms data
            cvReportDetails = queryEngine.executeQuery(queryKey,
                    AwardReportTermsBean.class, CoeusVector.FILTER_ACTIVE_BEANS);
        }catch (CoeusException coeusException){
            coeusException.printStackTrace();
        }
        
        if( cvReportClass != null && cvReportClass.size() > 0 ){
            reportClassTableModel.setData(cvReportClass);
            awardReportsForm.tblReportClass.clearSelection();
            if( reportsModified ){
                awardReportsForm.tblReportClass.setRowSelectionInterval(selClass, selClass);
            }else {
                awardReportsForm.tblReportClass.setRowSelectionInterval(0, 0);
            }
        }

    }
    
    /** Validate the form data/Form and returns true if
     * validation is through else returns false.
     * @throws CoeusUIException if some exception occurs or some validation fails.
     * @return true if
     * validation is through else returns false.
     */
    public boolean validate() throws edu.mit.coeus.exception.CoeusUIException {
        CoeusVector cvReports = null;
        try{
            cvReports = queryEngine.executeQuery(queryKey,
            AwardReportTermsBean.class, CoeusVector.FILTER_ACTIVE_BEANS);
        }catch (CoeusException coeusException){
            coeusException.printStackTrace();
        }
        if( cvReports == null || cvReports.size() == 0 ){
            CoeusOptionPane.showInfoDialog(
                coeusMessageResources.parseMessageKey(ENTER_ATLEAST_ONE_REPORT));
            return false;
        }
        return true;
    }
    
    /** This method triggers all actions based on the event occured
     * @param actionEvent takes the actionEvent
     */
    public void actionPerformed(java.awt.event.ActionEvent actionEvent) {
        Object source = actionEvent.getSource();
        if( source.equals(awardReportsForm.btnAdd) ){
            showAwardReportTerms(TypeConstants.ADD_MODE);
        }else if( source.equals(awardReportsForm.btnModify) ){
            showAwardReportTerms(TypeConstants.MODIFY_MODE);
        }else if( source.equals(awardReportsForm.btnDelete) ){
            deleteReport(false);//2796
        }else if( source.equals(awardReportsForm.btnDelType) ){
            deleteType();
        }else if( source.equals(awardReportsForm.btnSync) ){
            syncAwardReports();
        }//Added with case 2796: Sync to parent start
        else if( source.equals(awardReportsForm.btnDelSync) ){
            deleteAndSync();
        }
        //2796 End
    }
    
    /** To display the Report Terms screen
     * @param functionType
     */
    private void showAwardReportTerms(char functionType){
        CoeusVector cvFilteredData = new CoeusVector();
        CoeusVector cvReports = new CoeusVector();
        HashMap hmData = new HashMap();
        ComboBoxBean comboBoxBean;
        int selRow = awardReportsForm.tblReportDetails.getSelectedRow();
        selClass = awardReportsForm.tblReportClass.getSelectedRow();
        if( selClass == -1 ){
            CoeusOptionPane.showInfoDialog(
                    coeusMessageResources.parseMessageKey(SELECT_A_CLASS));
            return ;
        }else {
            comboBoxBean = (ComboBoxBean)cvReportClass.get(selClass);
            hmData.put(ComboBoxBean.class, comboBoxBean);
        }
        if( functionType == TypeConstants.ADD_MODE ){
            CoeusVector cvTemp = reportDetailsTableModel.getData();
            if( cvTemp != null ){
                cvFilteredData.addAll(cvTemp);
            }
        }else if( functionType == TypeConstants.MODIFY_MODE ){
            if( selRow == -1 ){
                CoeusOptionPane.showInfoDialog(
                    coeusMessageResources.parseMessageKey(SELECT_A_ROW_TO_MODIFY));
                return ;
            }else {
                AwardReportTermsBean bean = 
                        (AwardReportTermsBean)reportDetailsTableModel.getData().get(selRow);
                try{
                    hmData.put(AwardReportTermsBean.class, (AwardReportTermsBean)ObjectCloner.deepCopy(bean));
                }catch (Exception exception){
                    exception.printStackTrace();
                }
                Equals eqRepClass = new Equals(REPORT_CLASS_CODE, new Integer(bean.getReportClassCode()));
                Equals eqRepType = new Equals(REPORT_CODE, new Integer(bean.getReportCode()));
                And eqRepClassAndEqRepType = new And(eqRepClass, eqRepType);
                CoeusVector cvTemp = reportDetailsTableModel.getData().filter(eqRepClassAndEqRepType);
                if( cvTemp != null ){
                    cvFilteredData.addAll(cvTemp);
                }
            }
        }
        try {
            //Clone the data before sending to the controller to avoid reference issues
            cvReports = (CoeusVector) ObjectCloner.deepCopy(cvFilteredData);
        }catch (Exception exception){
            exception.printStackTrace();
        }
        hmData.put(CoeusVector.class, cvReports);
        AwardReportTermsController awardReportTermsController = 
            new AwardReportTermsController(awardBaseBean, functionType);
        awardReportTermsController.setBaseController(getBaseController());
        awardReportTermsController.setFormData(hmData);
        awardReportTermsController.display();
    }
    
    /** Sync the award reports with the template reports
     */
    private void syncAwardReports(){
        int option = CoeusOptionPane.showQuestionDialog(
                coeusMessageResources.parseMessageKey(CONFIRM_SYNC),
                CoeusOptionPane.OPTION_YES_NO, CoeusOptionPane.DEFAULT_YES);
        
        switch( option ){
            case (CoeusOptionPane.SELECTION_YES):
                //Call the Sync Reports of the AwardController
                if ( syncReports(EMPTY, getTemplateCode()) ){
                    setFormData(null);
                    setSaveRequired(true);
                }
        }
    }
    
    /** Removes the selected report detail
     ** Modified parameter with case 2796: Sync To Parent
     */
    private void deleteReport(boolean syncRequired){
        int selRow = awardReportsForm.tblReportDetails.getSelectedRow();
        int selType = awardReportsForm.lstReportType.getSelectedIndex();
        if( selRow == -1 ){
            //No row selected
            CoeusOptionPane.showInfoDialog(
                    coeusMessageResources.parseMessageKey(SELECT_A_ROW_TO_DELETE));
        }else if( selType == -1 ){
            //No Type selected
            CoeusOptionPane.showInfoDialog(
                    coeusMessageResources.parseMessageKey(SELECT_A_TYPE));
        }else{
            int option = CoeusOptionPane.showQuestionDialog(
                    coeusMessageResources.parseMessageKey(DELETE_ROW), 
                    CoeusOptionPane.OPTION_YES_NO, CoeusOptionPane.DEFAULT_YES);
            switch (option){
                case CoeusOptionPane.SELECTION_YES:
                    DefaultListModel listModel = (DefaultListModel)awardReportsForm.lstReportType.getModel();
                    AwardReportTermsBean bean = (AwardReportTermsBean)reportDetailsTableModel.getData().get(selRow);
                    
                    //Remove the selected bean
                    cvReportDetails.removeElement(bean);
                    
                    Equals eqRepClass = new Equals(REPORT_CLASS_CODE, new Integer(bean.getReportClassCode()));
                    Equals eqRepCode = new Equals(REPORT_CODE, new Integer(bean.getReportCode()));
                    And eqRepClassAndEqRepCode = new And(eqRepClass, eqRepCode);
                    
                    Equals eqFreqCode = new Equals(fieldNames[0], new Integer(bean.getFrequencyCode()));
                    Equals eqFreqBaseCode = new Equals(fieldNames[1], new Integer(bean.getFrequencyBaseCode()));
                    And eqFreqCodeAndFreqBaseCode = new And(eqFreqCode, eqFreqBaseCode);
                    
                    Equals eqOspCode = new Equals(fieldNames[2], new Integer(bean.getOspDistributionCode()));
                    Equals eqDueDate = new Equals(fieldNames[3], bean.getDueDate());
                    And eqOspCodeAndEqDueDate = new And(eqOspCode, eqDueDate);
                    
                    And eqRepAndFreq = new And(eqRepClassAndEqRepCode, eqFreqCodeAndFreqBaseCode);
                    And operator = new And(eqRepAndFreq, eqOspCodeAndEqDueDate);
                    
                    //Filter for the report class code, report code, frequency code,
                    //frequency base code, osp distribution code and due date
                    CoeusVector cvFilteredData = cvReportDetails.filter(operator);                    
                    //Commented for COEUSQA-2408 : CLONE -Award Sync - MOdify and sync reports not working correctly - Start        
//                    if( cvFilteredData == null || cvFilteredData.size() == 0 ){
//                        if( !(bean.getContactTypeCode() == -1 && bean.getRolodexId() == -1 )){
//                            //No recipients, set the following to indicate no recipients
//                            bean.setContactTypeCode(-1);
//                            bean.setRolodexId(-1);
//
//                            //Update the bean to the query engine
//                            bean.setAcType(TypeConstants.UPDATE_RECORD);
//                            bean.setSyncRequired(syncRequired);//2796
//                            try{
//                                queryEngine.update(queryKey, bean);
//                            }catch (CoeusException coeusException){
//                                coeusException.printStackTrace();
//                            }
//                            
//                            //Add the selected bean
//                            cvReportDetails.addElement(bean);
//                            
//                            reportDetailsTableModel.fireTableRowsUpdated(selRow, selRow);
//                            return ;
//                        }
//                    }
                    //COEUSQA-2408 :End
                    //Filter for the report class code and report code
                    cvFilteredData = cvReportDetails.filter(eqRepClassAndEqRepCode);
                    reportDetailsTableModel.setData(cvFilteredData);
                    reportDetailsTableModel.fireTableDataChanged();
                    
                    //Alter the row heights
                    resetRowHeights(cvFilteredData);
                    
                    if( cvFilteredData != null && cvFilteredData.size() > 0 ){
                        awardReportsForm.tblReportDetails.setRowSelectionInterval(0, 0);
                    }else{
                        //Remove the report code from the vector
                        vecReportCodes.removeElement(new Integer(bean.getReportCode()));

                        //Remove the selected report type and set the selection
                        listModel.removeElementAt(selType);
                        if( listModel.getSize() > 0 ){
                            awardReportsForm.lstReportType.setSelectedIndex(0);
                        }
                    }
                    
                    //Mark the bean as deleted and remove from the query engine
                    bean.setAcType(TypeConstants.DELETE_RECORD);
                    bean.setSyncRequired(syncRequired);//2796
                    try{
                        queryEngine.delete(queryKey, bean);
                    }catch (CoeusException coeusException){
                        coeusException.printStackTrace();
                    }
            }
        }
    }
    
    /** Removes the selected Type and the related records
     */
    private void deleteType(){
        int selType = awardReportsForm.lstReportType.getSelectedIndex();
        int selClass = awardReportsForm.tblReportClass.getSelectedRow();
        if( selClass == -1 ){
            //No Report class selected
            CoeusOptionPane.showInfoDialog(
                    coeusMessageResources.parseMessageKey(SELECT_A_CLASS));
        }else if( selType == -1 ){
            //No Report Code selected
            CoeusOptionPane.showInfoDialog(
                    coeusMessageResources.parseMessageKey(SELECT_A_TYPE));
        }else{
            int option = CoeusOptionPane.showQuestionDialog(
                    coeusMessageResources.parseMessageKey(REMOVE_TYPE), 
                    CoeusOptionPane.OPTION_YES_NO, CoeusOptionPane.DEFAULT_YES);
            switch (option){
                case CoeusOptionPane.SELECTION_YES:
                    DefaultListModel listModel = (DefaultListModel)awardReportsForm.lstReportType.getModel();
                    String repClassCode = ((ComboBoxBean)cvReportClass.get(selClass)).getCode();
                    Equals eqRepClassCode = new Equals(REPORT_CLASS_CODE, new Integer(repClassCode));
                    Equals eqRepCode = new Equals(REPORT_CODE, 
                            new Integer(vecReportCodes.get(selType).toString()));
                    And eqRepClassCodeAndEqRepCode = new And(eqRepClassCode, eqRepCode);
                    
                    //Filter to get all the beans for the selected report class and report code
                    CoeusVector cvFilteredDetails = cvReportDetails.filter(eqRepClassCodeAndEqRepCode);
                    
                    for( int index = 0; index < cvFilteredDetails.size(); index++ ){
                        AwardReportTermsBean bean = (AwardReportTermsBean)cvFilteredDetails.get(index);
                        cvReportDetails.removeElement(bean);
                        
                        //Remove the report code from the vector
                        vecReportCodes.removeElement(new Integer(bean.getReportCode()));
                        //Mark the bean as deleted and remove from the queryengine
                        bean.setAcType(TypeConstants.DELETE_RECORD);
                        try{
                            queryEngine.delete(queryKey, bean);
                        }catch (CoeusException coeusException){
                            coeusException.printStackTrace();
                        }
                    }
                    
                    //Remove the selected report type from the list and set the selection
                    listModel.removeElementAt(selType);
                    if( listModel.getSize() > 0 ){
                        awardReportsForm.lstReportType.setSelectedIndex(0);
                    }
            }
        }
    }

    /** This method sets the panel data based on the valueChanged of listSelectionEvent
     * @param listSelectionEvent takes the listSelectionEvent
     */
    public void valueChanged(javax.swing.event.ListSelectionEvent listSelectionEvent) {
        if( listSelectionEvent.getValueIsAdjusting() ) return ;
        Object source = listSelectionEvent.getSource();
        int selClass = awardReportsForm.tblReportClass.getSelectedRow();
        int selType = awardReportsForm.lstReportType.getSelectedIndex();
        CoeusVector cvFilteredDetails;
        DefaultListModel listModel = (DefaultListModel)awardReportsForm.lstReportType.getModel();
        if( source.equals(reportClassSelectionModel) && selClass >= 0 && 
        cvReportClass != null) {
            String repClassCode = ((ComboBoxBean)cvReportClass.get(selClass)).getCode();
            Equals eqRepClassCode = new Equals(REPORT_CLASS_CODE, new Integer(repClassCode));
            
            //Filter to get all beans for the selected Report Class Code
            cvFilteredDetails = cvReportDetails.filter(eqRepClassCode);

            if( cvFilteredDetails != null && cvFilteredDetails.size() > 0 ){
                //Clear the existing report codes
                vecReportCodes = new Vector();
                listModel.clear();
                
                for( int index = 0; index < cvFilteredDetails.size(); index++ ){
                    AwardReportTermsBean repBean = (AwardReportTermsBean)cvFilteredDetails.get(index);
                    if( !listModel.contains(repBean.getReportDescription().trim())){
                        //Add the report codes to the list if not already present
                        vecReportCodes.addElement(new Integer(repBean.getReportCode()));
                        listModel.addElement(repBean.getReportDescription().trim());
                    }
                }
            }else {
                listModel.clear();
            }
            
            //Set the selection
            if( listModel != null && !listModel.isEmpty() ){
                if( reportsModified ){
                    awardReportsForm.lstReportType.setSelectedValue(selReportDescription, true);
                    reportsModified = false;
                }else {
                    awardReportsForm.lstReportType.setSelectedIndex(0);
                }
            }

        }else if( source.equals(reportTypeSelectionModel) && cvReportDetails != null ){
            if( selType == -1 || selClass == -1 ){
                cvFilteredDetails = null;
            }else{
                String repClassCode = ((ComboBoxBean)cvReportClass.get(selClass)).getCode();
                Equals eqRepClassCode = new Equals(REPORT_CLASS_CODE, new Integer(repClassCode));
                Equals eqRepCode = new Equals(REPORT_CODE, 
                        new Integer(vecReportCodes.get(selType).toString()));
                And eqRepClassCodeAndEqRepCode = new And(eqRepClassCode, eqRepCode);
                
                //Filter to get all the beans for the selected report class and report code
                cvFilteredDetails = cvReportDetails.filter(eqRepClassCodeAndEqRepCode);
            }
            
            if( cvFilteredDetails != null ){
                cvFilteredDetails.sort(fieldNames, true);
            }
            
            //Set the Report Details data for the selected Report type
            reportDetailsTableModel.setData(cvFilteredDetails);
            reportDetailsTableModel.fireTableDataChanged();
            
            //Alter the row heights
            resetRowHeights(cvFilteredDetails);

            if( selClass != -1 ){
                reportClassTableModel.fireTableRowsUpdated(selClass, selClass);
            }
            if( cvFilteredDetails != null && cvFilteredDetails.size() > 0 ){
                awardReportsForm.tblReportDetails.setRowSelectionInterval(0, 0);
            }
        }
    }
    
    /** Set the row heights for the report details table based on the grouping
     * @param cvFilteredData
     */
    private void resetRowHeights(CoeusVector cvFilteredData) {
        if( cvFilteredData == null || cvFilteredData.size() == 0 ){
            return ;
        }
        int[] category = new int[3];
        java.sql.Date dueDate = null;
        boolean newGroup;
        for(int index = 0; index < cvFilteredData.size(); index++) {
            AwardReportTermsBean reportTermsBean = (AwardReportTermsBean)cvFilteredData.get(index);
            if( category[0] == reportTermsBean.getFrequencyCode() && 
            category[1] == reportTermsBean.getFrequencyBaseCode() &&
            category[2] == reportTermsBean.getOspDistributionCode() &&
            ( dueDate == null || dueDate.equals(reportTermsBean.getDueDate())) ){
                if( dueDate == null && reportTermsBean.getDueDate() != null ){
                    newGroup = true;
                }else{
                    newGroup = false;
                }
            }else {
                newGroup = true;
            }
            
            if( newGroup ){
                awardReportsForm.tblReportDetails.setRowHeight(index, INCREASED_ROW_HEIGHT);
                category[0] = reportTermsBean.getFrequencyCode();
                category[1] = reportTermsBean.getFrequencyBaseCode();
                category[2] = reportTermsBean.getOspDistributionCode();
                dueDate = reportTermsBean.getDueDate();
            }
        }
    }
    
    /** To get the contact name or organization name
     * @param awardReportTermsBean
     * @return contactName
     */
    private String getContactName(AwardReportTermsBean awardReportTermsBean){
        String contactName = EMPTY;
        String contactType = awardReportTermsBean.getContactTypeDescription() == null || 
            awardReportTermsBean.getContactTypeDescription().trim().length() == 0 ?
            EMPTY : awardReportTermsBean.getContactTypeDescription().trim();
        String organization = awardReportTermsBean.getOrganization() == null ||
            awardReportTermsBean.getOrganization().trim().length() == 0 ?
            EMPTY : awardReportTermsBean.getOrganization().trim();
        String firstName = awardReportTermsBean.getFirstName() == null ? EMPTY :
            awardReportTermsBean.getFirstName().trim();

        if ( firstName.length() > 0) {
            String suffix = awardReportTermsBean.getSuffix() == null ? EMPTY : 
                awardReportTermsBean.getSuffix().trim();
            String prefix = awardReportTermsBean.getPrefix() == null ? EMPTY :
                awardReportTermsBean.getPrefix().trim();
            String middleName = awardReportTermsBean.getMiddleName() == null ? EMPTY :
                awardReportTermsBean.getMiddleName().trim();
            String lastName = awardReportTermsBean.getLastName() == null ? EMPTY :
                awardReportTermsBean.getLastName().trim();

            if( !contactType.equals(EMPTY) ){
                contactName = (contactType + HYPHEN + lastName + suffix +
                COMMA + prefix + SPACE + firstName + SPACE + middleName).trim();
            }else {
                lastName = lastName + ( suffix.equals(EMPTY) ? suffix : SPACE + suffix);
                contactName = (lastName + COMMA + (prefix.equals(EMPTY) ? prefix : prefix + SPACE ) + firstName + 
                    SPACE + middleName).trim();
            }
        }else {
            if( !contactType.equals(EMPTY) ){
                contactName = contactType + HYPHEN + organization;
            }else {
                contactName = organization;
            }
        }
        return contactName;
    }

    /** Method to perform some action when the beanUpdated event is triggered
     * here it sets the <CODE>refreshRequired</CODE> flag
     * @param beanEvent takes the beanEvent */
    public void beanUpdated(edu.mit.coeus.gui.event.BeanEvent beanEvent) {
        if( beanEvent.getSource().getClass().equals(OtherHeaderController.class) ){
            if( beanEvent.getBean().getClass().equals(AwardDetailsBean.class)){
                setRefreshRequired(true);
            }
        }
        
        //Bug Fix:1023 -- Start
        if(beanEvent.getBean().getClass().equals(AwardContactDetailsBean.class)){
            CoeusVector cvFilterData = new CoeusVector();
            HashMap hmData = (HashMap)beanEvent.getObject();    
            
            String strPerson = (String)hmData.get(AwardConstants.PERSON_NAME);
            AwardContactDetailsBean beforeBean = (AwardContactDetailsBean)hmData.get(AwardConstants.BEAN_BEFORE_MODIFICATION);
            AwardContactDetailsBean afterBean = (AwardContactDetailsBean)hmData.get(AwardConstants.BEAN_AFTER_MODIFICATION);
            ComboBoxBean typeComboBean = (ComboBoxBean)hmData.get(AwardConstants.CONTACT_TYPE);
            
            Equals eqOrgainzation = new Equals("organization",strPerson.trim());
            Equals eqContactType = new Equals("contactTypeDescription",typeComboBean.getDescription().trim());
            And eqOrgainzationAndeqContactType  = new And(eqOrgainzation,eqContactType);
            
            
            if(cvFilterData != null){
                Equals eqFirstName = new Equals("firstName",beforeBean.getFirstName());
                Equals eqLastName = new Equals("lastName",beforeBean.getLastName());
                Equals eqMiddleName = new Equals("middleName",beforeBean.getMiddleName());
                Equals eqSuffix = new Equals("suffix",beforeBean.getSuffix());
                Equals eqPrefix = new Equals("prefix",beforeBean.getPrefix());


                And eqFirstNameAndeqLastName = new And(eqFirstName,eqLastName);
                And eqMiddleNameAndeqContactType = new And(eqMiddleName,eqContactType);
                And eqSuffixAndeqPrefix = new And(eqSuffix,eqPrefix);
                And intermediate = new And(eqFirstNameAndeqLastName,eqMiddleNameAndeqContactType);
                And finalData = new And(intermediate,eqSuffixAndeqPrefix);
                //Added for bug fixed for Case #2374 start
                try{
                    cvReportDetails = queryEngine.executeQuery(queryKey,
                                    AwardReportTermsBean.class, CoeusVector.FILTER_ACTIVE_BEANS);
                }catch(CoeusException coeusException){
                    coeusException.printStackTrace();
                }
                //Added for bug fixed for Case #2374 end
                cvFilterData = cvReportDetails.filter(finalData);
                if(cvFilterData.size() > 0){
                    for(int index = 0; index < cvFilterData.size(); index++){
                        AwardReportTermsBean reportTermsBean = (AwardReportTermsBean)cvFilterData.get(index);
                        if(!EMPTY.equals(afterBean.getFirstName().trim())){
                            reportTermsBean.setFirstName(afterBean.getFirstName());
                            reportTermsBean.setLastName(afterBean.getLastName());
                            reportTermsBean.setMiddleName(afterBean.getMiddleName());
                            reportTermsBean.setSuffix(afterBean.getSuffix());
                            reportTermsBean.setPrefix(afterBean.getPrefix());
                            reportTermsBean.setRolodexId(afterBean.getRolodexId());
                        }else{
                            reportTermsBean.setFirstName(EMPTY);
                            reportTermsBean.setLastName(EMPTY);
                            reportTermsBean.setMiddleName(EMPTY);
                            reportTermsBean.setSuffix(EMPTY);
                            reportTermsBean.setPrefix(EMPTY);
                            reportTermsBean.setOrganization(afterBean.getOrganization());
                            reportTermsBean.setRolodexId(afterBean.getRolodexId());
                        }
                        setRefreshRequired(true);
                        try{
                            queryEngine.update(queryKey,reportTermsBean);
                        }catch (CoeusException ce){
                            ce.printStackTrace();
                        }
                    }
                }else{
                    cvFilterData = cvReportDetails.filter(eqOrgainzationAndeqContactType);
                    if(cvFilterData.size()>0){
                        for(int index = 0; index < cvFilterData.size(); index++){
                            AwardReportTermsBean reportTermsBean = (AwardReportTermsBean)cvFilterData.get(index);
                            //Bug Fix 1491:Start
                            if(!EMPTY.equals(afterBean.getFirstName().trim())){
                                reportTermsBean.setFirstName(afterBean.getFirstName());
                                reportTermsBean.setLastName(afterBean.getLastName());
                                reportTermsBean.setMiddleName(afterBean.getMiddleName());
                                reportTermsBean.setSuffix(afterBean.getSuffix());
                                reportTermsBean.setPrefix(afterBean.getPrefix());
                                reportTermsBean.setRolodexId(afterBean.getRolodexId());
                            }else{
                                reportTermsBean.setFirstName(EMPTY);
                                reportTermsBean.setLastName(EMPTY);
                                reportTermsBean.setMiddleName(EMPTY);
                                reportTermsBean.setSuffix(EMPTY);
                                reportTermsBean.setPrefix(EMPTY);
                                reportTermsBean.setOrganization(afterBean.getOrganization());
                                reportTermsBean.setRolodexId(afterBean.getRolodexId());
                            }
                            //Bug Fix 1491:End
                            
                            setRefreshRequired(true);
                            try{
                                queryEngine.update(queryKey,reportTermsBean);
                            }catch (CoeusException ce){
                                ce.printStackTrace();
                            }
                        }
                    }
                }
            }
        }
        //Bug Fix:1023 End
    }
    
    /** Method to perform some action when the beanAdded event is triggered
     * @param beanEvent takes the beanEvent */
    public void beanAdded(edu.mit.coeus.gui.event.BeanEvent beanEvent) {
        AwardReportTermsBean newBean = (AwardReportTermsBean)beanEvent.getBean();
        selReportDescription = newBean.getReportDescription().trim();
        reportsModified = true;
        setRefreshRequired(true);
        refresh();
    }
    
    /** This method will refresh the form with the modified data */
    public void refresh(){
        if( !isRefreshRequired() ) return ;
        setFormData(null);
        setRefreshRequired(false);
    }

    /** Method to clean all objects */
    public void cleanUp() {
        
        //Bug Fix:Performance Issue (Out of memory) Start 3   
        //System.out.println("Award Rep");
        jscrPn.remove(awardReportsForm);
        jscrPn = null;
        //Bug Fix:Performance Issue (Out of memory) End 3
        
        awardReportsForm = null;
        coeusMessageResources = null;
        queryEngine = null;
        reportClassTableModel = null;
        reportClassTableRenderer = null;
        reportDetailsTableModel = null;
        reportDetailsTableRenderer = null;
        reportDetailsHeaderRenderer = null;
        cvReportClass = null;
        cvReportDetails = null;
        reportClassSelectionModel = null;
        reportTypeSelectionModel = null;
        vecReportCodes = null;
        selReportDescription = null;
        removeBeanUpdatedListener(this, AwardDetailsBean.class);
        removeBeanUpdatedListener(this, AwardReportTermsBean.class);
        removeBeanAddedListener(this, AwardReportTermsBean.class);
        removeBeanUpdatedListener(this, AwardContactDetailsBean.class);
    }
    
    public void mouseClicked(java.awt.event.MouseEvent mouseEvent) {
        if( mouseEvent.getClickCount() != 2 ) return ;
        int row = awardReportsForm.tblReportDetails.getSelectedRow();
        AwardReportTermsBean bean = (AwardReportTermsBean)
                reportDetailsTableModel.getData().get(row);
        if( bean.getRolodexId() != -1 ){
            //Show Rolodex screen
            String rolId = new Integer(bean.getRolodexId()).toString();
            RolodexMaintController rolodexController = new RolodexMaintController();
            RolodexDetailsBean rldxBean = rolodexController.displayRolodexInfo(rolId);
            RolodexMaintenanceDetailForm frmRolodex =
            new RolodexMaintenanceDetailForm('V',rldxBean);
            frmRolodex.showForm(CoeusGuiConstants.getMDIForm(), "Display Rolodex", true);
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
    
    //Added with Case 2796: Sync To Parent - Start
    private void deleteAndSync() {
        
        int selRow = awardReportsForm.tblReportDetails.getSelectedRow();
        int selType = awardReportsForm.lstReportType.getSelectedIndex();
        
        if( selRow == -1 ){//No row selected
            CoeusOptionPane.showInfoDialog(
                    coeusMessageResources.parseMessageKey(SELECT_A_ROW_TO_DELETE));
            return;
        }else if( selType == -1 ){//No Type selected
            CoeusOptionPane.showInfoDialog(
                    coeusMessageResources.parseMessageKey(SELECT_A_TYPE));
            return;
        }
        
        CoeusVector cvReports = null;
        try{
            cvReports = queryEngine.executeQuery(queryKey,
                    AwardReportTermsBean.class, CoeusVector.FILTER_ACTIVE_BEANS);
        }catch (CoeusException coeusException){
            coeusException.printStackTrace();
        }
        if( cvReports == null || cvReports.size() <= 1 ){
            CoeusOptionPane.showInfoDialog(
                    coeusMessageResources.parseMessageKey(ENTER_ATLEAST_ONE_REPORT));
            return;
        }
        //COEUSDEV 253: Add FabE and CS accounts to award sync screen.
        //Added for COEUSDEV-416 : Award Sync to Children - Display proper error message when not syncing because the award is not saved - Start
        HashMap syncTarget = showSyncTargetWindow(true,AwardConstants.REPORTS_SYNC,AwardConstants.DELETE_SYNC);
        //COEUSDEV-416 : End
        //COEUSDEV 253: End
        if(syncTarget != null){
            deleteReport(true);
            if(setSyncFlags(AwardReportTermsBean.class,true,syncTarget)){
                saveAndSyncAward(AwardReportTermsBean.class);
            }
        }
    }
    //2796 End
    
    //Inner Class ReportDetailsTableModel - Start
    class ReportDetailsTableModel extends DefaultTableModel {
        
        private CoeusVector cvDetails;
        private DateUtils dtUtils;
        //private static final String EMPTY_STRING = "";
        String[] colNames = {FREQUENCY, FREQ_BASE, OSP_DIST, DUE_DATE};
        Class[] colTypes = {String.class, String.class, String.class, String.class};

        ReportDetailsTableModel(){
            dtUtils = new DateUtils();
        }
        
        public void setData(CoeusVector cvDetails){
            this.cvDetails = cvDetails;
        }
        
        public CoeusVector getData(){
            return cvDetails;
        }
        
        public int getRowCount() {
            if( cvDetails == null ){
                return 0;
            }else{
                return cvDetails.size();
            }
        }

        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return false;
        }
        
        public Object getValueAt(int rowIndex, int columnIndex) {
            AwardReportTermsBean awardReportTermsBean = (AwardReportTermsBean)
                    cvDetails.get(rowIndex);
            /*
            switch (columnIndex){
                case FREQ_COL:
                    return awardReportTermsBean.getFrequencyDescription();
                case FREQ_BASE_COL:
                    return awardReportTermsBean.getFrequencyBaseDescription();
                case OSP_DIST_COL:
                    return awardReportTermsBean.getOspDistributionDescription();
                case DUE_DATE_COL:
                    return dtUtils.formatDate(awardReportTermsBean.getDueDate().toString(), REQUIRED_DATEFORMAT);
            }
            return EMPTY_STRING;
             */
            return awardReportTermsBean;
        }
        
        public String getColumnName(int columnIndex) {
            return colNames[columnIndex];
        }
        
        public Class getColumnClass(int columnIndex) {
            return colTypes[columnIndex];
        }
        
        public int getColumnCount() {
            return 1;//colNames.length;
        }
        
    }//Inner Class ReportDetailsTableModel - End
    
    //Inner Class ReportDetailsTableRenderer - Start
    class ReportDetailsTableRenderer extends DefaultTableCellRenderer {
        
        private DateUtils dtUtils;
        private JPanel pnlGroup;
        //To display the grouping values
        private JPanel pnlNewGroup;
        //private JLabel lblGroup;
        private JLabel lblValue;
        private JLabel lblContact;
        private Border groupBorder;
        private AwardReportTermsBean reportTermsBean;
        int[] category = new int[3];
        private java.sql.Date dueDate;
        private boolean newGroup;
        EmptyBorder emptyBorder;
        private static final String COPY_TO = " Copy to : ";
        private static final String COPIES_TO = " Copies to : ";
        private static final String NO_RECIPIENTS = "No Recipients";
        private static final String DEFAULT_DATE = "01-Jan-1900";
        private static final String EMPTY_STRING = "";
        private static final String TAB_SPACE = "        ";
        
        ReportDetailsTableRenderer(){
            dtUtils = new DateUtils();
            pnlGroup = new JPanel(new GridLayout(2, 1));
            pnlNewGroup = new JPanel(new GridLayout(1, 4));
            
            /*
            lblGroup = new JLabel();
            lblGroup.setFont(CoeusFontFactory.getLabelFont());
            lblGroup.setForeground(Color.BLACK);
             */

            emptyBorder = new EmptyBorder(2,2,2,2);
            LineBorder lineBorder = new LineBorder(Color.BLACK, 1);
            groupBorder = new CompoundBorder(emptyBorder, lineBorder);
            
            lblValue = new JLabel();
            lblValue.setOpaque(true);
            pnlGroup.add(pnlNewGroup);//lblGroup
            if( functionType == TypeConstants.DISPLAY_MODE ){
                pnlGroup.setBackground(PANEL_BACKGROUND);
                pnlNewGroup.setBackground(PANEL_BACKGROUND);
            }else {
                pnlGroup.setBackground(Color.WHITE);
                pnlNewGroup.setBackground(Color.WHITE);
            }
            //lblGroup.setBorder(groupBorder);
            pnlGroup.add(lblValue);
            
            lblContact = new JLabel();
            lblContact.setOpaque(true);
        }
        
        public Component getTableCellRendererComponent(javax.swing.JTable table,
        Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            if( row > 0 ){
                reportTermsBean = (AwardReportTermsBean)reportDetailsTableModel.getData().get(row - 1);
                category[0] = reportTermsBean.getFrequencyCode();
                category[1] = reportTermsBean.getFrequencyBaseCode();
                category[2] = reportTermsBean.getOspDistributionCode();
                dueDate = reportTermsBean.getDueDate();
            }else{
                category[0] = -1;
                category[1] = -1;
                category[2] = -1;
                dueDate = null;
            }
            reportTermsBean = (AwardReportTermsBean)reportDetailsTableModel.getData().get(row);
            if( category[0] == reportTermsBean.getFrequencyCode() && 
            category[1] == reportTermsBean.getFrequencyBaseCode() &&
            category[2] == reportTermsBean.getOspDistributionCode() &&
            ( dueDate == null || dueDate.equals(reportTermsBean.getDueDate())) ){
                if( dueDate == null && reportTermsBean.getDueDate() != null ){
                    newGroup = true;
                }else{
                    newGroup = false;
                }
            }else {
                newGroup = true;
            }

            if( newGroup ){
                /*
                switch (column){
                    case FREQ_COL:
                        lblGroup.setText(reportTermsBean.getFrequencyDescription());
                        break;
                    case FREQ_BASE_COL:
                        lblGroup.setText(reportTermsBean.getFrequencyBaseDescription());
                        break;
                    case OSP_DIST_COL:
                        lblGroup.setText(reportTermsBean.getOspDistributionDescription());
                        break;
                    case DUE_DATE_COL:
                        lblGroup.setText(dtUtils.formatDate(reportTermsBean.getDueDate().toString(), REQUIRED_DATEFORMAT));
                        if( lblGroup.getText().equals(DEFAULT_DATE)){
                            lblGroup.setText(EMPTY_STRING);
                            lblGroup.setBackground(PANEL_BACKGROUND);
                        }
                }
                 */
                String[] beanValues = {reportTermsBean.getFrequencyDescription(),
                        reportTermsBean.getFrequencyBaseDescription(),
                        reportTermsBean.getOspDistributionDescription(),
                        dtUtils.formatDate(reportTermsBean.getDueDate().toString(), REQUIRED_DATEFORMAT)};

                pnlNewGroup.removeAll();
                for( int index = 0; index < beanValues.length; index++ ){
                    JLabel lblGroup = new JLabel(beanValues[index]);
                    if (index == 3 && beanValues[index] != null && beanValues[index].equals(DEFAULT_DATE)){
                        lblGroup.setText(EMPTY_STRING);
                    }
                    lblGroup.setFont(CoeusFontFactory.getLabelFont());
                    lblGroup.setBorder(javax.swing.BorderFactory.createLineBorder(Color.BLACK));
                    pnlNewGroup.add(lblGroup);
                }
            }

            //To display the name/organization
            String contactName = getContactName(reportTermsBean);

            int copies = reportTermsBean.getNumberOfCopies();
            if( (reportTermsBean.getContactTypeCode() == -1 || 
            reportTermsBean.getContactTypeCode() == 0 ) && 
            (reportTermsBean.getRolodexId() == -1 ||
            reportTermsBean.getRolodexId() == 0 )){
                //No Contacts
                lblValue.setFont(CoeusFontFactory.getLabelFont());
                lblValue.setText(TAB_SPACE + NO_RECIPIENTS);
                lblContact.setFont(CoeusFontFactory.getLabelFont());
                lblContact.setText(TAB_SPACE + NO_RECIPIENTS);
            }else{
                lblValue.setFont(CoeusFontFactory.getNormalFont());
                lblValue.setText(copies == 1 ? TAB_SPACE + copies + COPY_TO + 
                    contactName: TAB_SPACE + copies + COPIES_TO + contactName);
                lblContact.setFont(CoeusFontFactory.getNormalFont());
                lblContact.setText(copies == 1 ? TAB_SPACE + copies + COPY_TO + 
                    contactName: TAB_SPACE + copies + COPIES_TO + contactName);
            }
            /*
            if( column != 0 ) {
                lblValue.setText(EMPTY_STRING);
                lblContact.setText(EMPTY_STRING);
            }
             */
            
            if(isSelected) {
                lblValue.setForeground(awardReportsForm.tblReportDetails.getSelectionForeground());
                lblValue.setBackground(awardReportsForm.tblReportDetails.getSelectionBackground());
                lblContact.setForeground(awardReportsForm.tblReportDetails.getSelectionForeground());
                lblContact.setBackground(awardReportsForm.tblReportDetails.getSelectionBackground());
            }else {
                lblValue.setForeground(Color.BLUE);
                lblContact.setForeground(Color.BLUE);
                if( functionType == TypeConstants.DISPLAY_MODE ){
                    pnlGroup.setBackground(PANEL_BACKGROUND);
                    lblValue.setBackground(PANEL_BACKGROUND);
                    lblContact.setBackground(PANEL_BACKGROUND);
                }else {
                    pnlGroup.setBackground(Color.WHITE);
                    lblValue.setBackground(Color.WHITE);
                    lblContact.setBackground(Color.WHITE);
                }
            }
            if( newGroup ){
                return pnlGroup;
            }else{
                return lblContact;
            }
        }
        
    }//Inner Class ReportDetailsTableRenderer - End
    
    //Inner Class ReportDetailsHeaderRenderer - Start
    class ReportDetailsHeaderRenderer extends DefaultTableCellRenderer 
    implements TableCellRenderer {
        private JLabel label;
        private JPanel pnlHeader;
        String[] colNames = {FREQUENCY, FREQ_BASE, OSP_DIST, DUE_DATE};
        
        ReportDetailsHeaderRenderer(){
            pnlHeader = new JPanel(new GridLayout(1, 4));
            for( int index = 0; index < colNames.length; index++ ){
                label = new JLabel(colNames[index]);
                label.setFont(CoeusFontFactory.getLabelFont());
                label.setBorder(javax.swing.BorderFactory.createRaisedBevelBorder());
                pnlHeader.add(label);
            }
        }
        
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            return pnlHeader;
        }
    }//Inner Class ReportDetailsHeaderRenderer - End
    
    //Inner Class ReportClassTableModel - Start
    class ReportClassTableModel extends AbstractTableModel {
        private CoeusVector cvClass;
        private String colName[] = {EMPTY, EMPTY};
        private Class colClass[] = {ImageIcon.class, String.class};
        
        public boolean isCellEditable(int row, int col){
            return false;
        }
        
        public int getRowCount() {
            if(cvClass == null){
                return 0;
            }else {
                return cvClass.size();
            }
        }
        
        public void setData(CoeusVector cvClass){
            this.cvClass = cvClass;
        }
        
        public Class getColumnClass(int columnIndex) {
            return colClass [columnIndex];
        }
        
        public String getColumnName(int column) {
            return colName[column];
        }
        
        public int getColumnCount() {
            return colName.length;
        }
        
        public Object getValueAt(int row, int col){
            ComboBoxBean comboBoxBean = (ComboBoxBean)cvClass.get(row);
            switch(col){
                case IMAGE_COLUMN:
                    return EMPTY;
                case TEXT_COLUMN:
                    return comboBoxBean.getDescription() + OPENING_BRACE + 
                            comboBoxBean.getCode() + CLOSING_BRACE ;
            }
            return EMPTY;
        }
    }//Inner Class ReportClassTableModel - End
    
    /** Renderer for the column table where the image icons are placed. 
     * Rendering the image icons based on the data.
     */
    private class ReportClassTableRenderer extends DefaultTableCellRenderer{
        java.net.URL emptyPageUrl = getClass().getClassLoader().getResource( CoeusGuiConstants.NEW_ICON);
        java.net.URL fillPageUrl = getClass().getClassLoader().getResource( CoeusGuiConstants.DATA_ICON);
        
        private JLabel lblIcon;
        ImageIcon EMPTY_PAGE_ICON, FILL_PAGE_ICON;
        
        ReportClassTableRenderer(){
            super();
            lblIcon = new JLabel();
        }
        
        public Component getTableCellRendererComponent(JTable table, Object value, 
        boolean isSelected, boolean hasFocus, int row, int column) {
            switch(column){
                case IMAGE_COLUMN:
                    if(emptyPageUrl != null){
                        EMPTY_PAGE_ICON = new ImageIcon(emptyPageUrl);
                        lblIcon.setIcon(EMPTY_PAGE_ICON);
                    }
                    
                    if(fillPageUrl != null){
                        FILL_PAGE_ICON = new ImageIcon(fillPageUrl);
                        lblIcon.setIcon(FILL_PAGE_ICON);
                    }
                    
                    ComboBoxBean comboBoxBean = (ComboBoxBean)cvReportClass.get(row);
                    Equals eqRepClassCode = new Equals(REPORT_CLASS_CODE, new Integer(comboBoxBean.getCode()));
                    
                    CoeusVector cvFilterdData = cvReportDetails.filter(eqRepClassCode);
                    if(cvFilterdData != null && cvFilterdData.size() > 0) {
                        lblIcon.setIcon(FILL_PAGE_ICON);
                    }else{
                        lblIcon.setIcon(EMPTY_PAGE_ICON);
                    }
                    return lblIcon;
            }
            return super.getTableCellRendererComponent(table, value,
            isSelected, hasFocus, row, column);
        }
    }// End of ReportClassTableRenderer
    
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
    
}
