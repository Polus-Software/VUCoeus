/*
 * TemplateReportsController.java
 *
 * Created on December 17, 2004, 8:12 PM
 */

package edu.mit.coeus.admin.controller;

import edu.mit.coeus.admin.bean.AwardTemplateBean;
import edu.mit.coeus.admin.bean.AwardTemplateContactsBean;
import edu.mit.coeus.admin.bean.AwdTemplateRepTermsBean;
import edu.mit.coeus.admin.bean.TemplateBaseBean;
import edu.mit.coeus.utils.CoeusDateFormat;
import java.awt.Component;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
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
//import edu.mit.coeus.award.bean.AwardContactDetailsBean;
//import edu.mit.coeus.award.bean.AwardReportTermsBean;
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
//import edu.mit.coeus.award.bean.TemplateReportTermsBean;

import edu.mit.coeus.utils.ScreenFocusTraversalPolicy;
import edu.mit.coeus.utils.query.Or;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.StringTokenizer;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.plaf.basic.BasicGraphicsUtils;
import javax.swing.plaf.basic.BasicLabelUI;

/**
 *
 * @author  ajaygm
 */
public class TemplateReportsController extends AwardTemplateController implements
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
    //Added for COEUSQA-1456 : Templates-add User ID stamp & Timestamp - Start
    private static final int CONTACT_ROW_HEIGHT = 30;
    //COEUSQA-1456 : End
    
    /** Creates a new instance of TemplateReportsController */
    public TemplateReportsController(TemplateBaseBean templateBaseBean, char functionType) {
        super(templateBaseBean);
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
        awardReportsForm.btnSync.setEnabled(false);
        awardReportsForm.btnSync.setVisible(false);
        //Added with case 2796: Sync To Parent
        awardReportsForm.btnDelSync.setEnabled(false);
        awardReportsForm.btnDelSync.setVisible(false);
        //2796 End
        this.functionType = getFunctionType();
        if( getFunctionType() == TypeConstants.DISPLAY_MODE ){
            
            //Disable all buttons
            awardReportsForm.btnAdd.setEnabled(false);
            awardReportsForm.btnModify.setEnabled(false);
            awardReportsForm.btnDelete.setEnabled(false);
            awardReportsForm.btnDelType.setEnabled(false);
            awardReportsForm.btnSync.setEnabled(false);
            
            awardReportsForm.tblReportClass.setBackground(PANEL_BACKGROUND);
            awardReportsForm.tblReportDetails.setBackground(PANEL_BACKGROUND);
            awardReportsForm.lstReportType.setBackground(PANEL_BACKGROUND);
            reportDetailsTableRenderer.setBackgroundColor();
        }else{
            awardReportsForm.btnAdd.setEnabled(true);
            awardReportsForm.btnModify.setEnabled(true);
            awardReportsForm.btnDelete.setEnabled(true);
            awardReportsForm.btnDelType.setEnabled(true);
            awardReportsForm.btnSync.setEnabled(true);
            
            awardReportsForm.tblReportClass.setBackground(Color.WHITE);
            awardReportsForm.tblReportDetails.setBackground(Color.WHITE);
            awardReportsForm.lstReportType.setBackground(Color.WHITE);
            reportDetailsTableRenderer.setBackgroundColor();
        }
    }
    
    /** An overridden method of the controller
     * @return awardReportsForm returns the controlled form component
     */
    public java.awt.Component getControlledUI() {
        return awardReportsForm;
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
        awardReportsForm.btnDelType
        };
        ScreenFocusTraversalPolicy  traversePolicy = new ScreenFocusTraversalPolicy( components );
        awardReportsForm.setFocusTraversalPolicy(traversePolicy);
        awardReportsForm.setFocusCycleRoot(true);
        
        /*@todo*/
        addBeanUpdatedListener(this, AwardDetailsBean.class);
        addBeanUpdatedListener(this, AwdTemplateRepTermsBean.class);
        addBeanAddedListener(this, AwdTemplateRepTermsBean.class);
        
        //Bug Fix:1023 -- Start
        addBeanUpdatedListener(this, AwardTemplateContactsBean.class);
        //Bug Fix:1023 -- End
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
        if(data != null){
            this.templateBaseBean = (TemplateBaseBean)data;
        }
        try{
            //Get the Report Class data and set it to the table
            cvReportClass = queryEngine.getDetails(queryKey, KeyConstants.REPORT_CLASS);
            
            //Get the Report Terms data
            cvReportDetails = queryEngine.executeQuery(queryKey,
            AwdTemplateRepTermsBean.class , CoeusVector.FILTER_ACTIVE_BEANS);
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
        //Added for COEUSQA-1456 : Templates-add User ID stamp & Timestamp - Start
        //Sets the update time and update user
        if(cvReportDetails != null && cvReportDetails.size() > 0) {
            try {
                CoeusVector cvUpdateDetails = queryEngine.executeQuery(queryKey, "REPORTS_TEMPLATE_UPDATE_DETAIL", CoeusVector.FILTER_ACTIVE_BEANS);
                if(cvUpdateDetails != null && cvUpdateDetails.size() > 0) {
                    AwardTemplateBean updateDetail = (AwardTemplateBean)cvUpdateDetails.get(0);
                    if(functionType != TypeConstants.COPY_MODE &&
                            updateDetail.getUpdateTimestamp() != null){
                        String lastUpdate = CoeusDateFormat.format(updateDetail.getUpdateTimestamp().toString());
                        String updateUserName = updateDetail.getUpdateUserName();
                        awardReportsForm.pnlUpdateDetails.setVisible(true);
                        awardReportsForm.txtLastUpdate.setText(lastUpdate);
                        awardReportsForm.txtUpdateUser.setText(updateUserName);
                    }
                }
            } catch(Exception e) {
                e.printStackTrace();
            }
        } else {
            awardReportsForm.pnlUpdateDetails.setVisible(false);
        }
        //COEUSQA-1456 : End
        
    }
    
    /** Validate the form data/Form and returns true if
     * validation is through else returns false.
     * @throws CoeusUIException if some exception occurs or some validation fails.
     * @return true if
     * validation is through else returns false.
     */
    public boolean validate() throws edu.mit.coeus.exception.CoeusUIException {
        CoeusVector cvReports = null;
//        try{
//            cvReports = queryEngine.executeQuery(queryKey,
//            AwdTemplateRepTermsBean.class , CoeusVector.FILTER_ACTIVE_BEANS);
//        }catch (CoeusException coeusException){
//            coeusException.printStackTrace();
//        }
//        if( cvReports == null || cvReports.size() == 0 ){
//            CoeusOptionPane.showInfoDialog(
//            coeusMessageResources.parseMessageKey(ENTER_ATLEAST_ONE_REPORT));
//            return false;
//        }
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
            deleteReport();
        }else if( source.equals(awardReportsForm.btnDelType) ){
            deleteType();
        }else if( source.equals(awardReportsForm.btnSync) ){
            //            syncAwardReports();
        }
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
                AwdTemplateRepTermsBean bean =
                (AwdTemplateRepTermsBean)reportDetailsTableModel.getData().get(selRow);
                try{
                    hmData.put(AwdTemplateRepTermsBean.class, (AwdTemplateRepTermsBean)ObjectCloner.deepCopy(bean));
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
        /*@todo*/
        
        TemplateReportTermsController templateReportTermsController =
        new TemplateReportTermsController(templateBaseBean, functionType);
        templateReportTermsController.setFormData(hmData);
        templateReportTermsController.display();
    }
    
    /** Sync the award reports with the template reports
     */
    //    private void syncAwardReports(){
    //        int option = CoeusOptionPane.showQuestionDialog(
    //                coeusMessageResources.parseMessageKey(CONFIRM_SYNC),
    //                CoeusOptionPane.OPTION_YES_NO, CoeusOptionPane.DEFAULT_YES);
    //
    //        switch( option ){
    //            case (CoeusOptionPane.SELECTION_YES):
    //                //Call the Sync Reports of the AwardController
    //                if ( syncReports(EMPTY, getTemplateCode()) ){
    //                    setFormData(null);
    //                    setSaveRequired(true);
    //                }
    //        }
    //    }
    
    /** Removes the selected report detail
     */
    private void deleteReport(){
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
                    AwdTemplateRepTermsBean bean = (AwdTemplateRepTermsBean)reportDetailsTableModel.getData().get(selRow);
                    
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
                    
                    if( cvFilteredData == null || cvFilteredData.size() == 0 ){
                        if( !(bean.getContactTypeCode() == -1 && bean.getRolodexId() == -1 )){
                            //No recipients, set the following to indicate no recipients
                            bean.setContactTypeCode(-1);
                            bean.setRolodexId(-1);
                            
                            //Update the bean to the query engine
                            bean.setAcType(TypeConstants.UPDATE_RECORD);
                            try{
                                queryEngine.update(queryKey, bean);
                            }catch (CoeusException coeusException){
                                coeusException.printStackTrace();
                            }
                            
                            //Add the selected bean
                            cvReportDetails.addElement(bean);
                            
                            reportDetailsTableModel.fireTableRowsUpdated(selRow, selRow);
                            return ;
                        }
                    }
                    
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
                        AwdTemplateRepTermsBean bean = (AwdTemplateRepTermsBean)cvFilteredDetails.get(index);
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
                    AwdTemplateRepTermsBean repBean = (AwdTemplateRepTermsBean)cvFilteredDetails.get(index);
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
            AwdTemplateRepTermsBean reportTermsBean = (AwdTemplateRepTermsBean)cvFilteredData.get(index);
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
            //Added for COEUSQA-1456 : Templates-add User ID stamp & Timestamp - Start
            //sets the contact row height
            else{
                awardReportsForm.tblReportDetails.setRowHeight(index, CONTACT_ROW_HEIGHT);
            }
            //COEUSQA-1456 : End
        }
    }
    
    /** To get the contact name or organization name
     * @param AwdTemplateRepTermsBean
     * @return contactName
     */
    private String getContactName(AwdTemplateRepTermsBean awdTemplateRepTermsBean){
        String contactName = EMPTY;
        String contactType = awdTemplateRepTermsBean.getContactTypeDescription() == null ||
        awdTemplateRepTermsBean.getContactTypeDescription().trim().length() == 0 ?
        EMPTY : awdTemplateRepTermsBean.getContactTypeDescription().trim();
        String organization = awdTemplateRepTermsBean.getOrganization() == null ||
        awdTemplateRepTermsBean.getOrganization().trim().length() == 0 ?
        EMPTY : awdTemplateRepTermsBean.getOrganization().trim();
        String firstName = awdTemplateRepTermsBean.getFirstName() == null ? EMPTY :
            awdTemplateRepTermsBean.getFirstName().trim();
            
            if ( firstName.length() > 0) {
                String suffix = awdTemplateRepTermsBean.getSuffix() == null ? EMPTY :
                    awdTemplateRepTermsBean.getSuffix().trim();
                    String prefix = awdTemplateRepTermsBean.getPrefix() == null ? EMPTY :
                        awdTemplateRepTermsBean.getPrefix().trim();
                        String middleName = awdTemplateRepTermsBean.getMiddleName() == null ? EMPTY :
                            awdTemplateRepTermsBean.getMiddleName().trim();
                            String lastName = awdTemplateRepTermsBean.getLastName() == null ? EMPTY :
                                awdTemplateRepTermsBean.getLastName().trim();
                                
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
        //        if( beanEvent.getSource().getClass().equals(OtherHeaderController.class) ){
        //            if( beanEvent.getBean().getClass().equals(AwardDetailsBean.class)){
        //                setRefreshRequired(true);
        //            }
        //        }
        
        //Bug Fix:1023 -- Start
        if(beanEvent.getBean().getClass().equals(AwardTemplateContactsBean.class)){
            CoeusVector cvFilterData = new CoeusVector();
            HashMap hmData = (HashMap)beanEvent.getObject();
            
            String strPerson = (String)hmData.get(AwardConstants.PERSON_NAME);
            AwardTemplateContactsBean beforeBean = (AwardTemplateContactsBean)hmData.get(AwardConstants.BEAN_BEFORE_MODIFICATION);
            AwardTemplateContactsBean afterBean = (AwardTemplateContactsBean)hmData.get(AwardConstants.BEAN_AFTER_MODIFICATION);
            ComboBoxBean typeComboBean = (ComboBoxBean)hmData.get(AwardConstants.CONTACT_TYPE);
            
            Equals eqOrgainzation = new Equals("organization",strPerson.trim());
            Equals eqContactType = new Equals("contactTypeDescription",typeComboBean.getDescription().trim());
            And eqOrgainzationAndeqContactType  = new And(eqOrgainzation,eqContactType);
            
            
            if(cvFilterData != null){
                Equals eqFirstName = new Equals("firstName",beforeBean.getFirstName());
                Equals eqLastName = new Equals("lastName",beforeBean.getLastName());
                Equals eqMiddleName = new Equals("middleName",beforeBean.getMiddleName());
                Equals eqSuffix = new Equals("suffix",beforeBean.getSuffix());
                Equals eqEmptySuffix = new Equals("suffix",EMPTY);
                Equals eqPrefix = new Equals("prefix",beforeBean.getPrefix());
                Equals eqEmptyPrefix = new Equals("prefix",EMPTY);
                Or orSuffix = new Or(eqEmptySuffix, eqSuffix);
                Or orPrefix = new Or(eqEmptyPrefix,eqPrefix);
                
                And eqFirstNameAndeqLastName = new And(eqFirstName,eqLastName);
                And eqMiddleNameAndeqContactType = new And(eqMiddleName,eqContactType);
                And eqSuffixAndeqPrefix = new And(orSuffix,orPrefix);
                And intermediate = new And(eqFirstNameAndeqLastName,eqMiddleNameAndeqContactType);
                And finalData = new And(intermediate,eqSuffixAndeqPrefix);
                
                cvFilterData = cvReportDetails.filter(finalData);
                if(cvFilterData.size() > 0){
                    for(int index = 0; index < cvFilterData.size(); index++){
                        AwdTemplateRepTermsBean reportTermsBean = (AwdTemplateRepTermsBean)cvFilterData.get(index);
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
                            AwdTemplateRepTermsBean  reportTermsBean = (AwdTemplateRepTermsBean)cvFilterData.get(index);
                            reportTermsBean.setOrganization(strPerson);
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
        AwdTemplateRepTermsBean newBean = (AwdTemplateRepTermsBean)beanEvent.getBean();
        selReportDescription = newBean.getReportDescription().trim();
        reportsModified = true;
        setRefreshRequired(true);
        refresh();
    }
    
    /** This method will refresh the form with the modified data */
    public void refresh(){
        if( !isRefreshRequired() ) return ;
        //Modified for COEUSQA-1456 : Templates-add User ID stamp & Timestamp - Start
        if(functionType == TypeConstants.COPY_MODE){
            functionType = TypeConstants.MODIFY_MODE;
        }
        //COEUSQA-1456 : End
        setFormData(null);
        setRefreshRequired(false);
    }
    
    /** Method to clean all objects */
    public void cleanUp() {
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
        removeBeanUpdatedListener(this, AwdTemplateRepTermsBean.class);
        removeBeanAddedListener(this, AwdTemplateRepTermsBean.class);
        removeBeanUpdatedListener(this, AwardTemplateContactsBean.class);
    }
    
    public void mouseClicked(java.awt.event.MouseEvent mouseEvent) {
        if( mouseEvent.getClickCount() != 2 ) return ;
        int row = awardReportsForm.tblReportDetails.getSelectedRow();
        AwdTemplateRepTermsBean bean = (AwdTemplateRepTermsBean)
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
            AwdTemplateRepTermsBean awdTemplateRepTermsBean = (AwdTemplateRepTermsBean)
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
            return awdTemplateRepTermsBean;
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
        private AwdTemplateRepTermsBean reportTermsBean;
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
            /*if( functionType == TypeConstants.DISPLAY_MODE ){
                pnlGroup.setBackground(PANEL_BACKGROUND);
                pnlNewGroup.setBackground(PANEL_BACKGROUND);
            }else {
                pnlGroup.setBackground(Color.WHITE);
                pnlNewGroup.setBackground(Color.WHITE);
            }*/
            //lblGroup.setBorder(groupBorder);
            pnlGroup.add(lblValue);
            
            lblContact = new JLabel();
            lblContact.setOpaque(true);
        }
        
        public void setBackgroundColor(){
            if( functionType == TypeConstants.DISPLAY_MODE ){
                pnlGroup.setBackground(PANEL_BACKGROUND);
                pnlNewGroup.setBackground(PANEL_BACKGROUND);
            }else {
                pnlGroup.setBackground(Color.WHITE);
                pnlNewGroup.setBackground(Color.WHITE);
            }
        }
        
        public Component getTableCellRendererComponent(javax.swing.JTable table,
        Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            if( row > 0 ){
                reportTermsBean = (AwdTemplateRepTermsBean)reportDetailsTableModel.getData().get(row - 1);
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
            reportTermsBean = (AwdTemplateRepTermsBean)reportDetailsTableModel.getData().get(row);
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
                //Modified for COEUSQA-1456 : Templates-add User ID stamp & Timestamp - Start
                //Displays the last update time and user in the second line of the label
//                lblValue.setText(copies == 1 ? TAB_SPACE + copies + COPY_TO +
//                contactName: TAB_SPACE + copies + COPIES_TO + contactName);
//                lblContact.setFont(CoeusFontFactory.getNormalFont());
//                lblContact.setText(copies == 1 ? TAB_SPACE + copies + COPY_TO +
//                        contactName: TAB_SPACE + copies + COPIES_TO + contactName);
//                
                lblContact.setFont(CoeusFontFactory.getNormalFont());
                String copyText = COPY_TO;
                if(copies == 0 || copies == 1){
                    copyText = COPY_TO;
                }else{
                    copyText = COPIES_TO;
                }
                StringBuilder contactText = new StringBuilder();
                contactText.append(TAB_SPACE);
                contactText.append(copies);
                contactText.append(copyText);
                contactText.append(contactName);
                
                if(functionType != TypeConstants.COPY_MODE &&
                        reportTermsBean.getUpdateUserName() != null &&
                        !CoeusGuiConstants.EMPTY_STRING.equals(reportTermsBean.getUpdateUserName())){
                    contactText.append("\n");
                    int noOfTabs = 16;
                    if(reportTermsBean.getUpdateUserName().length()>15){
                        noOfTabs = 11;
                    }
                    for(int index=0;index<noOfTabs;index++){
                        contactText.append(TAB_SPACE);
                    }
                    contactText.append("Last Update : ");
                    contactText.append(CoeusDateFormat.format(reportTermsBean.getUpdateTimestamp().toString()));
                    contactText.append(", ");
                    contactText.append(reportTermsBean.getUpdateUserName());
                }
                lblValue.setUI(new MultiLineLabelUI());
                lblValue.setText(contactText.toString());
                lblContact.setFont(CoeusFontFactory.getNormalFont());
                
                if(copies == 0 || copies == 1){
                    copyText = COPY_TO;
                }else{
                    copyText = COPIES_TO;
                }
                StringBuilder secondContactText = new StringBuilder();
                secondContactText.append(TAB_SPACE);
                secondContactText.append(copies);
                secondContactText.append(copyText);
                secondContactText.append(contactName);
                
                if(functionType != TypeConstants.COPY_MODE &&
                        reportTermsBean.getUpdateUserName() != null &&
                        !CoeusGuiConstants.EMPTY_STRING.equals(reportTermsBean.getUpdateUserName())){
                    secondContactText.append("\n");
                    int noOfTabs = 16;
                    if(reportTermsBean.getUpdateUserName().length()>15){
                        noOfTabs = 11;
                    }
                    for(int index=0;index<noOfTabs;index++){
                        secondContactText.append(TAB_SPACE);
                    }
                    secondContactText.append("Last Update : ");
                    secondContactText.append(CoeusDateFormat.format(reportTermsBean.getUpdateTimestamp().toString()));
                    secondContactText.append(", ");
                    secondContactText.append(reportTermsBean.getUpdateUserName());
                }
                lblContact.setUI(new MultiLineLabelUI());
                lblContact.setText(secondContactText.toString());
                //COEUSQA-1456 : End
                
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
//                System.out.println("Inside isSelected:BackgroundColor"+awardReportsForm.tblReportDetails.getSelectionBackground());
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
}//End Class

//Added for COEUSQA-1456 : Templates-add User ID stamp & Timestamp - Start
//Class to provide multiple line in the label
class MultiLineLabelUI extends BasicLabelUI {
    
    protected String layoutCL(
            JLabel label,
            FontMetrics fontMetrics,
            String text,
            Icon icon,
            Rectangle viewR,
            Rectangle iconR,
            Rectangle textR) {
        String s = layoutCompoundLabel(
                (JComponent) label,
                fontMetrics,
                splitStringByLines(text),
                icon,
                label.getVerticalAlignment(),
                label.getHorizontalAlignment(),
                label.getVerticalTextPosition(),
                label.getHorizontalTextPosition(),
                viewR,
                iconR,
                textR,
                label.getIconTextGap());
        
        if( s.equals("") )
            return text;
        return s;
    }
    
    
    static final int LEADING = SwingConstants.LEADING;
    static final int TRAILING = SwingConstants.TRAILING;
    static final int LEFT = SwingConstants.LEFT;
    static final int RIGHT = SwingConstants.RIGHT;
    static final int TOP = SwingConstants.TOP;
    static final int CENTER = SwingConstants.CENTER;
    
    /**
     * Compute and return the location of the icons origin, the
     * location of origin of the text baseline, and a possibly clipped
     * version of the compound labels string.  Locations are computed
     * relative to the viewR rectangle.
     * The JComponents orientation (LEADING/TRAILING) will also be taken
     * into account and translated into LEFT/RIGHT values accordingly.
     */
    public static String layoutCompoundLabel(JComponent c,
            FontMetrics fm,
            String[] text,
            Icon icon,
            int verticalAlignment,
            int horizontalAlignment,
            int verticalTextPosition,
            int horizontalTextPosition,
            Rectangle viewR,
            Rectangle iconR,
            Rectangle textR,
            int textIconGap) {
        boolean orientationIsLeftToRight = true;
        int     hAlign = horizontalAlignment;
        int     hTextPos = horizontalTextPosition;
        
        
        if (c != null) {
            if (!(c.getComponentOrientation().isLeftToRight())) {
                orientationIsLeftToRight = false;
            }
        }
        
        
        // Translate LEADING/TRAILING values in horizontalAlignment
        // to LEFT/RIGHT values depending on the components orientation
        switch (horizontalAlignment) {
            case LEADING:
                hAlign = (orientationIsLeftToRight) ? LEFT : RIGHT;
                break;
            case TRAILING:
                hAlign = (orientationIsLeftToRight) ? RIGHT : LEFT;
                break;
        }
        
        // Translate LEADING/TRAILING values in horizontalTextPosition
        // to LEFT/RIGHT values depending on the components orientation
        switch (horizontalTextPosition) {
            case LEADING:
                hTextPos = (orientationIsLeftToRight) ? LEFT : RIGHT;
                break;
            case TRAILING:
                hTextPos = (orientationIsLeftToRight) ? RIGHT : LEFT;
                break;
        }
        
        return layoutCompoundLabel(fm,
                text,
                icon,
                verticalAlignment,
                hAlign,
                verticalTextPosition,
                hTextPos,
                viewR,
                iconR,
                textR,
                textIconGap);
    }
    
    
    /**
     * Compute and return the location of the icons origin, the
     * location of origin of the text baseline, and a possibly clipped
     * version of the compound labels string.  Locations are computed
     * relative to the viewR rectangle.
     * This layoutCompoundLabel() does not know how to handle LEADING/TRAILING
     * values in horizontalTextPosition (they will default to RIGHT) and in
     * horizontalAlignment (they will default to CENTER).
     * Use the other version of layoutCompoundLabel() instead.
     */
    public static String layoutCompoundLabel(
            FontMetrics fm,
            String[] text,
            Icon icon,
            int verticalAlignment,
            int horizontalAlignment,
            int verticalTextPosition,
            int horizontalTextPosition,
            Rectangle viewR,
            Rectangle iconR,
            Rectangle textR,
            int textIconGap) {
        /* Initialize the icon bounds rectangle iconR.
         */
        
        if (icon != null) {
            iconR.width = icon.getIconWidth();
            iconR.height = icon.getIconHeight();
        } else {
            iconR.width = iconR.height = 0;
        }
        
        /* Initialize the text bounds rectangle textR.  If a null
         * or and empty String was specified we substitute "" here
         * and use 0,0,0,0 for textR.
         */
        
        // Fix for textIsEmpty sent by Paulo Santos
        boolean textIsEmpty = (text == null) || (text.length == 0)
        || (text.length == 1 && ( (text[0]==null) || text[0].equals("") ));
        
        String rettext = "";
        if (textIsEmpty) {
            textR.width = textR.height = 0;
        } else {
            Dimension dim = computeMultiLineDimension( fm, text );
            textR.width = dim.width;
            textR.height = dim.height;
        }
        
        /* Unless both text and icon are non-null, we effectively ignore
         * the value of textIconGap.  The code that follows uses the
         * value of gap instead of textIconGap.
         */
        
        int gap = (textIsEmpty || (icon == null)) ? 0 : textIconGap;
        
        if (!textIsEmpty) {
            
            /* If the label text string is too wide to fit within the available
             * space "..." and as many characters as will fit will be
             * displayed instead.
             */
            
            int availTextWidth;
            
            if (horizontalTextPosition == CENTER) {
                availTextWidth = viewR.width;
            } else {
                availTextWidth = viewR.width - (iconR.width + gap);
            }
            
            
            if (textR.width > availTextWidth && text.length == 1) {
                String clipString = "...";
                int totalWidth = SwingUtilities.computeStringWidth(fm,clipString);
                int nChars;
                for(nChars = 0; nChars < text[0].length(); nChars++) {
                    totalWidth += fm.charWidth(text[0].charAt(nChars));
                    if (totalWidth > availTextWidth) {
                        break;
                    }
                }
                rettext = text[0].substring(0, nChars) + clipString;
                textR.width = SwingUtilities.computeStringWidth(fm,rettext);
            }
        }
        
        
        /* Compute textR.x,y given the verticalTextPosition and
         * horizontalTextPosition properties
         */
        
        if (verticalTextPosition == TOP) {
            if (horizontalTextPosition != CENTER) {
                textR.y = 0;
            } else {
                textR.y = -(textR.height + gap);
            }
        } else if (verticalTextPosition == CENTER) {
            textR.y = (iconR.height / 2) - (textR.height / 2);
        } else { // (verticalTextPosition == BOTTOM)
            if (horizontalTextPosition != CENTER) {
                textR.y = iconR.height - textR.height;
            } else {
                textR.y = (iconR.height + gap);
            }
        }
        
        if (horizontalTextPosition == LEFT) {
            textR.x = -(textR.width + gap);
        } else if (horizontalTextPosition == CENTER) {
            textR.x = (iconR.width / 2) - (textR.width / 2);
        } else { // (horizontalTextPosition == RIGHT)
            textR.x = (iconR.width + gap);
        }
        
        /* labelR is the rectangle that contains iconR and textR.
         * Move it to its proper position given the labelAlignment
         * properties.
         *
         * To avoid actually allocating a Rectangle, Rectangle.union
         * has been inlined below.
         */
        int labelR_x = Math.min(iconR.x, textR.x);
        int labelR_width = Math.max(iconR.x + iconR.width,
                textR.x + textR.width) - labelR_x;
        int labelR_y = Math.min(iconR.y, textR.y);
        int labelR_height = Math.max(iconR.y + iconR.height,
                textR.y + textR.height) - labelR_y;
        
        int dx, dy;
        
        if (verticalAlignment == TOP) {
            dy = viewR.y - labelR_y;
        } else if (verticalAlignment == CENTER) {
            dy = (viewR.y + (viewR.height / 2)) - (labelR_y + (labelR_height / 2));
        } else { // (verticalAlignment == BOTTOM)
            dy = (viewR.y + viewR.height) - (labelR_y + labelR_height);
        }
        
        if (horizontalAlignment == LEFT) {
            dx = viewR.x - labelR_x;
        } else if (horizontalAlignment == RIGHT) {
            dx = (viewR.x + viewR.width) - (labelR_x + labelR_width);
        } else { // (horizontalAlignment == CENTER)
            dx = (viewR.x + (viewR.width / 2)) -
                    (labelR_x + (labelR_width / 2));
        }
        
        /* Translate textR and glypyR by dx,dy.
         */
        
        textR.x += dx;
        textR.y += dy;
        
        iconR.x += dx;
        iconR.y += dy;
        
        return rettext;
    }
    
    protected void paintEnabledText(JLabel l, Graphics g, String s, int textX, int textY) {
        int accChar = l.getDisplayedMnemonic();
        g.setColor(l.getForeground());
        drawString(g, s, accChar, textX, textY);
    }
    
    
    protected void paintDisabledText(JLabel l, Graphics g, String s, int textX, int textY) {
        int accChar = l.getDisplayedMnemonic();
        g.setColor(l.getBackground());
        drawString(g, s, accChar, textX, textY);
    }
    
    protected void drawString( Graphics g, String s, int accChar, int textX, int textY ) {
        if( s.indexOf('\n') == -1 )
            BasicGraphicsUtils.drawString(g, s, accChar, textX, textY);
        else {
            String[] strs = splitStringByLines( s );
            int height = g.getFontMetrics().getHeight();
            // Only the first line can have the accel char
            BasicGraphicsUtils.drawString(g, strs[0], accChar, textX, textY);
            for( int i = 1; i < strs.length; i++ )
                g.drawString( strs[i], textX, textY + (height*i) );
        }
    }
    
    public static Dimension computeMultiLineDimension( FontMetrics fm, String[] strs ) {
        int i, c, width = 0;
        for(i=0, c=strs.length ; i < c ; i++)
            width = Math.max( width, SwingUtilities.computeStringWidth(fm,strs[i]) );
        return new Dimension( width, fm.getHeight() * strs.length );
    }
    
    
    protected String str;
    protected String[] strs;
    
    public String[] splitStringByLines( String str ) {
        if( str.equals(this.str) )
            return strs;
        
        this.str = str;
        
        int lines = 1;
        int i, c;
        for(i=0, c=str.length() ; i < c ; i++) {
            if( str.charAt(i) == '\n' )
                lines++;
        }
        strs = new String[lines];
        StringTokenizer st = new StringTokenizer( str, "\n" );
        
        int line = 0;
        while( st.hasMoreTokens() )
            strs[line++] = st.nextToken();
        
        return strs;
    }
}
//COEUSQA-1456 : End