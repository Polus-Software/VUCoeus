/*
 * AwardFNAController.java
 *
 * Created on September 16, 2005, 5:02 PM
 */

package edu.mit.coeus.award.controller;

import edu.mit.coeus.award.bean.AwardAmountFNABean;
import edu.mit.coeus.award.bean.AwardAmountInfoBean;
import edu.mit.coeus.award.bean.AwardBean;
import edu.mit.coeus.award.gui.AwardAmountFNADistributionForm;
import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.exception.CoeusClientException;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.exception.CoeusUIException;
import edu.mit.coeus.gui.CoeusAppletMDIForm;
import edu.mit.coeus.gui.CoeusDlgWindow;
import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.utils.AppletServletCommunicator;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.utils.CoeusTextField;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.DateUtils;
import edu.mit.coeus.utils.DollarCurrencyTextField;
import edu.mit.coeus.utils.TypeConstants;
import edu.mit.coeus.utils.query.Equals;
import edu.mit.coeus.utils.query.QueryEngine;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.Vector;
import javax.swing.AbstractAction;
import javax.swing.AbstractCellEditor;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumn;

/**
 *
 * @author  vinayks
 */
public class AwardAmountFNAController extends AwardController implements ActionListener {
    private AwardAmountFNADistributionForm awardAmountFNADistributionForm =new AwardAmountFNADistributionForm();
    private AwardAmountFNADistrTableModel awardAmountFNADistrTableModel;
    private AwardFNATotalTableModel awardFNATotalTableModel;
    private AwardFNATotalTableCellRenderer awardFNATotalTableCellRenderer;
    private AwardFNATtlAmtTableModel  awardFNATtlAmtTableModel;
    private AwardFNADetailTotalTableCellRenderer  awardFNADetailTotalTableCellRenderer;
    private AwardAmountFNADistrTableCellRenderer awardAmountFNATableCellRenderer;
    private AwardAmountFNADistrTableCellEditor awardAmountFNADistrTableCellEditor;
    private CoeusDlgWindow dlgAwardFNADistForm;
    private CoeusAppletMDIForm mdiForm = CoeusGuiConstants.getMDIForm();
    private int WIDTH=605;
    private int HEIGHT=385;
    private String EMPTY_STRING= "";
    
    
    private static final int TOTAL_COLUMN=0;
    private static final int DIRECT_TOTAL=1;
    private static final int INDIRECT_TOTAL=2;
    
    private static final int DETAIL_TOTAL_COLUMN=1;
    
   // private static final int HAND_ICON_COLUMN = 0;
    private static final int PERIOD_COLUMN=0;
    private static final int START_DATE_COLUMN=1;
    private static final int END_DATE_COLUMN =2;
    private static final int DIRECT_COST_COLUMN=3;
    private static final int INDIRECT_COST_COLUMN=4;
    
    
    private CoeusVector cvFNAData;
    private CoeusVector cvDeleteFNA;
    private AwardAmountInfoBean awardAmountInfoBean;
    private CoeusMessageResources coeusMessageResources;
    
    private static final String SERVLET = "/AwardMaintenanceServlet";
    private final String connectTo = CoeusGuiConstants.CONNECTION_URL+ SERVLET;
    private static final char GET_FNA_DATA = 'k';
    private static final char GET_PARAM_FNA_DATA = 'l';
    private static final char UPDATE_FNA = 't'; 
    
    private DateUtils dtUtils = new DateUtils();
    private java.text.SimpleDateFormat dtFormat
    = new java.text.SimpleDateFormat("MM/dd/yyyy");
    private static final String SIMPLE_DATE_FORMAT = "MM/dd/yyyy";
    private static final String REQUIRED_DATE_FORMAT = "dd-MMM-yyyy";
    private static final String DATE_SEPARATERS = ":/.,|-";
    
    private static final String START_DATE_MANDATORY = "adjustPeriod_exceptionCode.1452";
    private static final String END_DATE_MANDATORY = "adjustPeriod_exceptionCode.1453";
    private static final String END_DATE_LATER_THAN_START_DATE = "adjustPeriod_exceptionCode.1454";
    private static final String END_DATE_NOT_LATER_THAN_AWARD ="adjustPeriod_exceptionCode.2001";
    private static final String  START_DATE_NOT_PRIOR_AWARD_START_DATE= "adjustPeriod_exceptionCode.2002";
    //Added for Case 2269: Money & End Dates Tab/Panel in Awards Module - start
    //private static final String TITLE_WINDOW = "Award Amount F & A Distribution";
    private static final String TITLE_WINDOW = "Anticipated Funding Distribution";
    //Added for Case 2269: Money & End Dates Tab/Panel in Awards Module - end
    private static final String INVALID_START_DATE = "budget_common_exceptionCode.1001";
    private static final String INVALID_END_DATE = "budget_common_exceptionCode.1002";
    private static final String DELETE_CONFIRM="budget_project_income_exceptionCode.1170";
    private static final String TOTAL_FNA_NOT_EQUAL ="awardF&A_exceptionCode.2008";
    private static final String SHOULD_HAVE_ATLEAST_ONE_ROW = "awardF&A_exceptionCode.2010";
    private java.awt.Color disabledBackground = (java.awt.Color) javax.swing.UIManager.
    getDefaults().get("Panel.background");
    
    
    
    /** this variable tells whether this window modified or not
     */
    public boolean modified = false;
    private String mesg;
    private char functionType;
    private java.sql.Date beginDate;
    private boolean currentAward;
    //Added for bug fixed for case#2370 start 1
    private String queryKey;
    private AwardBean awardBean;
    private Vector awardData;
   //Added for bug fixed for case#2370 end 1
    //case #2332 start 1
    private CoeusVector cvDelOptData;
    private String data;
    //case #2332 end 1
    
    /** Creates a new instance of AwardFNAController */
    public AwardAmountFNAController(AwardAmountInfoBean awardAmountInfoBean,char functionType)
    throws edu.mit.coeus.exception.CoeusException{
        this.awardAmountInfoBean  =awardAmountInfoBean;
        this.functionType = functionType;
        registerComponents();
        coeusMessageResources = CoeusMessageResources.getInstance();
        formatFields();
//        setFormData(awardAmountInfoBean);
//        postInitComponents();
        
        
    }
    
    public void formatFields() {
        if(functionType  == CoeusGuiConstants.DISPLAY_MODE){
            awardAmountFNADistributionForm.btnAdd.setEnabled(false);
            awardAmountFNADistributionForm.btnOk.setEnabled(false);
            awardAmountFNADistributionForm.btnInsert.setEnabled(false);
            awardAmountFNADistributionForm.btnDelete.setEnabled(false);
            
        }
    }
    
    public java.awt.Component getControlledUI() {
        return awardAmountFNADistributionForm;
    }
    
    public Object getFormData() {
        return null;
    }
    
    private boolean getParamAwardFNADistr() throws CoeusClientException{
        String data = getParameterData();
        if(data.trim().equalsIgnoreCase("M") || data.trim().equalsIgnoreCase("O") ){
            return true;
        }else{
            return false;
        }
    }
    
    private String getParameterData() throws CoeusClientException{
        String data = null;
        RequesterBean request = new RequesterBean();
        request.setFunctionType(GET_PARAM_FNA_DATA);
        AppletServletCommunicator comm = new AppletServletCommunicator(connectTo, request);
        comm.send();
        ResponderBean response = comm.getResponse();
        if(response== null){
            CoeusOptionPane.showErrorDialog(COULD_NOT_CONTACT_SERVER);
        }
        if(response.isSuccessfulResponse()){
            data = (String)response.getDataObject();
        }else {
            throw new CoeusClientException(response.getMessage(),CoeusClientException.ERROR_MESSAGE);
            
        }
        return data ;
    }
    
    
    public void registerComponents() {
        cvDeleteFNA = new CoeusVector();
        awardAmountFNADistrTableModel =new AwardAmountFNADistrTableModel();
        awardAmountFNADistributionForm.tblAwardFNA.setModel(awardAmountFNADistrTableModel);
        
        awardAmountFNADistributionForm.btnOk.addActionListener(this);
        awardAmountFNADistributionForm.btnCancel.addActionListener(this);
        awardAmountFNADistributionForm.btnAdd.addActionListener(this);
        awardAmountFNADistributionForm.btnInsert.addActionListener(this);
        awardAmountFNADistributionForm.btnDelete.addActionListener(this);
        
        awardFNATotalTableModel = new AwardFNATotalTableModel();
        awardAmountFNADistributionForm.tblTotal.setModel(awardFNATotalTableModel);
        awardFNATotalTableCellRenderer= new AwardFNATotalTableCellRenderer();
        awardFNATtlAmtTableModel = new AwardFNATtlAmtTableModel();
        awardFNADetailTotalTableCellRenderer= new AwardFNADetailTotalTableCellRenderer();
        awardAmountFNADistrTableCellEditor = new AwardAmountFNADistrTableCellEditor();
        awardAmountFNATableCellRenderer = new AwardAmountFNADistrTableCellRenderer();
        
        awardAmountFNADistributionForm.tblDetailTotal.setModel(awardFNATtlAmtTableModel);
        
        
    }
    
    public void setFormData(Object data) throws edu.mit.coeus.exception.CoeusException {
        awardAmountInfoBean = (AwardAmountInfoBean)data;
        cvFNAData = new CoeusVector();
        //Case #2332 start 2
        cvDelOptData = new CoeusVector();
        //case #2332 end 2
        cvFNAData = getFNATableData();
        if(cvFNAData==null || cvFNAData.size()==0){
            cvFNAData = prepareBudgetPeriods();
            //            modified = true;
        }
        //Case #2332 start 3
        else{
            for(int index =0;index < cvFNAData.size();index++){
                AwardAmountFNABean bean = (AwardAmountFNABean)cvFNAData.get(index);
                int period = bean.getBudgetPeriod();
                if(period == 0){
                    if((bean.getSequenceNumber() == awardAmountInfoBean.getSequenceNumber())
                    && (bean.getAmountSequenceNumber() == awardAmountInfoBean.getAmountSequenceNumber())
                    ) {
                        bean.setAcType(TypeConstants.DELETE_RECORD);
                        cvFNAData.remove(index);
                        cvDelOptData.addElement(bean);
                        break;
                    }else{
                        cvFNAData.remove(index);
                        break;
                    }
                }
                
            }
        }
        if(cvFNAData == null || cvFNAData.size() == 0) {
            awardAmountFNADistributionForm.btnDelete.setEnabled(false);
        }
        //case #2332 end 3
//        else{            
//            for(int index =0;index<cvFNAData.size();index++){
//                AwardAmountFNABean bean = (AwardAmountFNABean)cvFNAData.get(index);
//                bean.setAmountSequenceNumber(awardAmountInfoBean.getAmountSequenceNumber());
//            }
//        }
        
        setHeaderData();
        awardAmountFNADistrTableModel.setData(cvFNAData);
        setTableEditors();
        
    }
    
    private void setHeaderData(){
        awardAmountFNADistributionForm.txtAnticipatedTotal.setValue(
        awardAmountInfoBean.getAnticipatedTotalAmount());
        
        if(awardAmountInfoBean.getAwardEffectiveDate()!=null){
            if(!isCurrentAward()){
                awardAmountFNADistributionForm.lblAwardBeginDateValue.setText(dtUtils.formatDate(
                awardAmountInfoBean.getAwardEffectiveDate().toString(), REQUIRED_DATE_FORMAT));
            }else{
                awardAmountFNADistributionForm.lblAwardBeginDateValue.setText(dtUtils.formatDate(
                getBeginDate().toString(), REQUIRED_DATE_FORMAT));
            }
        }else{
            awardAmountFNADistributionForm.lblAwardBeginDateValue.setText(dtUtils.formatDate(
            getBeginDate().toString(), REQUIRED_DATE_FORMAT));
        }
        
        //        if (awardAmountInfoBean.getAwardEffectiveDate() != null){
        //            awardAmountFNADistributionForm.lblAwardBeginDateValue.setText(dtUtils.formatDate(
        //            awardAmountInfoBean.getAwardEffectiveDate().toString(), REQUIRED_DATE_FORMAT));
        //        }
        
        if ( awardAmountInfoBean.getFinalExpirationDate() != null ){
            awardAmountFNADistributionForm.lblFinalExpirationDateValue.setText(dtUtils.formatDate(
            awardAmountInfoBean.getFinalExpirationDate().toString(), REQUIRED_DATE_FORMAT));
        }
        
        awardAmountFNADistributionForm.lblMitAwardNumberValue.setText(
        awardAmountInfoBean.getMitAwardNumber());
        awardAmountFNADistributionForm.txtObligatedTotal.setValue(awardAmountInfoBean.getAmountObligatedToDate());
    }
    
    private CoeusVector getFNATableData() throws edu.mit.coeus.exception.CoeusException{
        CoeusVector cvData = null;
        RequesterBean requester = new RequesterBean();
        requester.setFunctionType(GET_FNA_DATA);
        Vector clientData = new Vector();
        clientData.addElement(awardAmountInfoBean.getMitAwardNumber());
        clientData.addElement(new Integer(awardAmountInfoBean.getSequenceNumber()));
        clientData.addElement(new Integer(awardAmountInfoBean.getAmountSequenceNumber()));
        
        requester.setDataObjects(clientData);
        AppletServletCommunicator comm
        = new AppletServletCommunicator(connectTo, requester);
        comm.send();
        ResponderBean responder = comm.getResponse();
        if(responder.isSuccessfulResponse()){
            cvData = (CoeusVector)responder.getDataObject();
        }else{
            throw new CoeusException(responder.getMessage());
        }
        return cvData;
    }
    
    public boolean validate() throws edu.mit.coeus.exception.CoeusUIException {
        try{
            awardAmountFNADistrTableCellEditor.stopCellEditing();
            //case #2332 start 4
            data = getParameterData();
            if(data != null && data.trim().equalsIgnoreCase("M")){
                if(awardAmountFNADistributionForm.tblAwardFNA.getRowCount() == 0) {
                    CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(SHOULD_HAVE_ATLEAST_ONE_ROW));
                    return false;
                }
            }
            //case #2332 end 4
            AwardAmountFNABean awardAmountFNABean,nextAwardAmountFNABean;
            Date txtStartDate = dtFormat.parse(dtUtils.restoreDate(
            awardAmountFNADistributionForm.lblAwardBeginDateValue.getText(),DATE_SEPARATERS));
            
            Date txtEndDate = dtFormat.parse(dtUtils.restoreDate(
            awardAmountFNADistributionForm.lblFinalExpirationDateValue.getText(),DATE_SEPARATERS));
            
            if(cvFNAData!= null && cvFNAData.size() > 0){
                if(awardAmountFNADistrTableModel.getValueAt(0, START_DATE_COLUMN)==null){
                    mesg = coeusMessageResources.parseMessageKey(START_DATE_MANDATORY);
                    CoeusOptionPane.showErrorDialog(mesg);
                    awardAmountFNADistributionForm.tblAwardFNA.setRowSelectionInterval(0,0);
                    awardAmountFNADistributionForm.tblAwardFNA.setColumnSelectionInterval(START_DATE_COLUMN,START_DATE_COLUMN);
                    awardAmountFNADistributionForm.tblAwardFNA.editCellAt(0,START_DATE_COLUMN);
                    awardAmountFNADistributionForm.tblAwardFNA.getEditorComponent().requestFocusInWindow();
                    return false;
                }else if(awardAmountFNADistrTableModel.getValueAt(0, END_DATE_COLUMN)==null){
                    mesg = coeusMessageResources.parseMessageKey(END_DATE_MANDATORY);
                    CoeusOptionPane.showErrorDialog(mesg);
                    awardAmountFNADistributionForm.tblAwardFNA.setRowSelectionInterval(0,0);
                    awardAmountFNADistributionForm.tblAwardFNA.setColumnSelectionInterval(START_DATE_COLUMN,START_DATE_COLUMN);
                    awardAmountFNADistributionForm.tblAwardFNA.editCellAt(0,END_DATE_COLUMN);
                    awardAmountFNADistributionForm.tblAwardFNA.getEditorComponent().requestFocusInWindow();
                    return false;
                }
                Date tempStDate = new Date(((java.sql.Date)awardAmountFNADistrTableModel.getValueAt(0, START_DATE_COLUMN)).getTime());
                Date tempEnDate = new Date(((java.sql.Date)awardAmountFNADistrTableModel.getValueAt(0, END_DATE_COLUMN)).getTime());
                if(tempStDate.before(txtStartDate)){
                    mesg = coeusMessageResources.parseMessageKey(
                    START_DATE_NOT_PRIOR_AWARD_START_DATE);
                    CoeusOptionPane.showErrorDialog(mesg);
                    awardAmountFNADistributionForm.tblAwardFNA.setRowSelectionInterval(0,0);
                    awardAmountFNADistributionForm.tblAwardFNA.setColumnSelectionInterval(START_DATE_COLUMN,START_DATE_COLUMN);
                    awardAmountFNADistributionForm.tblAwardFNA.editCellAt(0,START_DATE_COLUMN);
                    awardAmountFNADistributionForm.tblAwardFNA.getEditorComponent().requestFocusInWindow();
                    return false;
                }else if(tempEnDate.after(txtEndDate)){
                    mesg = coeusMessageResources.parseMessageKey(
                    END_DATE_NOT_LATER_THAN_AWARD);
                    CoeusOptionPane.showErrorDialog(mesg);
                    awardAmountFNADistributionForm.tblAwardFNA.setRowSelectionInterval(0,0);
                    awardAmountFNADistributionForm.tblAwardFNA.setColumnSelectionInterval(START_DATE_COLUMN,START_DATE_COLUMN);
                    awardAmountFNADistributionForm.tblAwardFNA.editCellAt(0,END_DATE_COLUMN);
                    awardAmountFNADistributionForm.tblAwardFNA.getEditorComponent().requestFocusInWindow();
                    return false;
                }else if(tempStDate.after(tempEnDate) || tempEnDate.before(tempStDate)){
                    mesg =coeusMessageResources.parseMessageKey(
                    END_DATE_LATER_THAN_START_DATE);
                    CoeusOptionPane.showErrorDialog(mesg);
                    awardAmountFNADistributionForm.tblAwardFNA.setRowSelectionInterval(0,0);
                    awardAmountFNADistributionForm.tblAwardFNA.setColumnSelectionInterval(START_DATE_COLUMN,START_DATE_COLUMN);
                    awardAmountFNADistributionForm.tblAwardFNA.editCellAt(0,END_DATE_COLUMN);
                    awardAmountFNADistributionForm.tblAwardFNA.getEditorComponent().requestFocusInWindow();
                    return false;
                }
                //Validating Period Dates
                for(int index = 0; index < cvFNAData.size() - 1; index++) {
                    awardAmountFNABean = (AwardAmountFNABean)cvFNAData.get(index);
                    nextAwardAmountFNABean = (AwardAmountFNABean)cvFNAData.get(index + 1);
                    //Check if start date is null/ Empty
                    if(awardAmountFNABean.getStartDate()==null || nextAwardAmountFNABean.getStartDate()==null){
                        mesg = coeusMessageResources.parseMessageKey(
                        START_DATE_MANDATORY);
                        CoeusOptionPane.showErrorDialog(mesg);
                        awardAmountFNADistributionForm.tblAwardFNA.setRowSelectionInterval(index + 1,index + 1);
                        awardAmountFNADistributionForm.tblAwardFNA.setColumnSelectionInterval(START_DATE_COLUMN,START_DATE_COLUMN);
                        awardAmountFNADistributionForm.tblAwardFNA.editCellAt(index+1,START_DATE_COLUMN);
                        awardAmountFNADistributionForm.tblAwardFNA.getEditorComponent().requestFocusInWindow();
                        awardAmountFNADistributionForm.tblAwardFNA.scrollRectToVisible(
                        awardAmountFNADistributionForm.tblAwardFNA.getCellRect(index+1, START_DATE_COLUMN, true));
                        return false;
                    }
                    //Check if End date is null / Empty
                    else if(awardAmountFNABean.getEndDate() == null || nextAwardAmountFNABean.getEndDate()==null){
                        mesg = coeusMessageResources.parseMessageKey(
                        END_DATE_MANDATORY);
                        CoeusOptionPane.showErrorDialog(mesg);
                        awardAmountFNADistributionForm.tblAwardFNA.setRowSelectionInterval(index+1,index+1);
                        awardAmountFNADistributionForm.tblAwardFNA.setColumnSelectionInterval(END_DATE_COLUMN,END_DATE_COLUMN);
                        awardAmountFNADistributionForm.tblAwardFNA.editCellAt(index+1,END_DATE_COLUMN);
                        awardAmountFNADistributionForm.tblAwardFNA.getEditorComponent().requestFocusInWindow();
                        awardAmountFNADistributionForm.tblAwardFNA.scrollRectToVisible(
                        awardAmountFNADistributionForm.tblAwardFNA.getCellRect(index+1, END_DATE_COLUMN, true));
                        return false;
                    }
                    //Check if next period start date < this period end date
                    else if(nextAwardAmountFNABean.getEndDate().before(nextAwardAmountFNABean.getStartDate())
                    || awardAmountFNABean.getEndDate().before(awardAmountFNABean.getStartDate())){
                        mesg =coeusMessageResources.parseMessageKey(
                        END_DATE_LATER_THAN_START_DATE);
                        CoeusOptionPane.showErrorDialog(mesg);
                        awardAmountFNADistributionForm.tblAwardFNA.setRowSelectionInterval(index+1,index+1);
                        awardAmountFNADistributionForm.tblAwardFNA.setColumnSelectionInterval(END_DATE_COLUMN,END_DATE_COLUMN);
                        awardAmountFNADistributionForm.tblAwardFNA.editCellAt(index+1,END_DATE_COLUMN);
                        awardAmountFNADistributionForm.tblAwardFNA.getEditorComponent().requestFocusInWindow();
                        awardAmountFNADistributionForm.tblAwardFNA.scrollRectToVisible(
                        awardAmountFNADistributionForm.tblAwardFNA.getCellRect(index+1, END_DATE_COLUMN, true));
                        return false;
                    }
                    //check if period start date  > period end date
                    //Check if next period start date == this period end date
                    else if((nextAwardAmountFNABean.getStartDate().compareTo(awardAmountFNABean.getEndDate()) <= 0)){
                        CoeusOptionPane.showErrorDialog("Start Date of Period "+
                        nextAwardAmountFNABean.getBudgetPeriod() + " should be later than the end date of period " +
                        awardAmountFNABean.getBudgetPeriod() + " ");
                        awardAmountFNADistributionForm.tblAwardFNA.setRowSelectionInterval(index+1,index+1);
                        awardAmountFNADistributionForm.tblAwardFNA.setColumnSelectionInterval(START_DATE_COLUMN, START_DATE_COLUMN);
                        awardAmountFNADistributionForm.tblAwardFNA.editCellAt(index+1,START_DATE_COLUMN);
                        awardAmountFNADistributionForm.tblAwardFNA.getEditorComponent().requestFocusInWindow();
                        awardAmountFNADistributionForm.tblAwardFNA.scrollRectToVisible(
                        awardAmountFNADistributionForm.tblAwardFNA.getCellRect(index+1, START_DATE_COLUMN, true));
                        return false;
                    }
                    //Check for last period end date > budget end date
                    if(index == cvFNAData.size() - 2 &&
                    nextAwardAmountFNABean.getEndDate().after(txtEndDate)) {
                        mesg = coeusMessageResources.parseMessageKey(
                        END_DATE_NOT_LATER_THAN_AWARD);
                        CoeusOptionPane.showErrorDialog(mesg);
                        awardAmountFNADistributionForm.tblAwardFNA.setRowSelectionInterval(cvFNAData.size()-1, cvFNAData.size()-1);
                        awardAmountFNADistributionForm.tblAwardFNA.setColumnSelectionInterval(END_DATE_COLUMN,END_DATE_COLUMN);
                        awardAmountFNADistributionForm.tblAwardFNA.editCellAt(cvFNAData.size()-1,END_DATE_COLUMN);
                        awardAmountFNADistributionForm.tblAwardFNA.getEditorComponent().requestFocusInWindow();
                        awardAmountFNADistributionForm.tblAwardFNA.scrollRectToVisible(
                        awardAmountFNADistributionForm.tblAwardFNA.getCellRect(cvFNAData.size()-1, cvFNAData.size()-1, true));
                        return false;
                    }
                }
                // Check for the parameter value for the FNA distribution
                if(data != null && data.trim().equalsIgnoreCase("M")){
               // if(getParamAwardFNADistr()){
                    double totalAnticipated =  awardAmountInfoBean.getAnticipatedTotalAmount();
                    double tableValue = cvFNAData.sum("directCost")+cvFNAData.sum("indirectCost");
                    if(totalAnticipated != tableValue ){
                        CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(TOTAL_FNA_NOT_EQUAL));
                        return false;
                    }
                   
                }
            }else{
                //Bug Fix CaseId :2031 Start
                //String data = getParameterData();
                if(data.trim().equalsIgnoreCase("M")){
                    CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(TOTAL_FNA_NOT_EQUAL));
                    return false;
                }
                //Bug Fix CaseId :2031 End
            }
        }catch (Exception e){
            throw new CoeusUIException(e.getMessage());
        }
        return true;
    }
    
    public void postInitComponents() {
        dlgAwardFNADistForm = new CoeusDlgWindow(mdiForm);
        dlgAwardFNADistForm.setResizable(false);
        dlgAwardFNADistForm.setTitle(TITLE_WINDOW);
        dlgAwardFNADistForm.setModal(true);
        dlgAwardFNADistForm.getContentPane().add(awardAmountFNADistributionForm);
        dlgAwardFNADistForm.setFont(CoeusFontFactory.getLabelFont());
        dlgAwardFNADistForm.setDefaultCloseOperation(CoeusDlgWindow.DO_NOTHING_ON_CLOSE);
        dlgAwardFNADistForm.setSize(WIDTH,HEIGHT);
        
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension dlgSize = dlgAwardFNADistForm.getSize();
        dlgAwardFNADistForm.setLocation(screenSize.width/2 - (dlgSize.width/2),
        screenSize.height/2 - (dlgSize.height/2));
        dlgAwardFNADistForm.addWindowListener(new WindowAdapter(){
            
            public void windowClosing(WindowEvent we) {
                performCancelAction();
            }
        });
        
        dlgAwardFNADistForm.addEscapeKeyListener(
        new AbstractAction("escPressed"){
            public void actionPerformed(ActionEvent ae) {
               awardAmountFNADistrTableCellEditor.stopCellEditing();
                performCancelAction();
            }
        });
        
        dlgAwardFNADistForm.addComponentListener(
        new ComponentAdapter(){
            public void componentShown(ComponentEvent e){
                setWindowFocus();
            }
        });
    }
    
     private void setWindowFocus(){
        if(functionType == CoeusGuiConstants.DISPLAY_MODE) 
            awardAmountFNADistributionForm.btnCancel.requestFocusInWindow();
        else {
            if(cvFNAData != null && cvFNAData.size()>0)
                setRequestFocusInFNAThread(0,START_DATE_COLUMN);
            else
               awardAmountFNADistributionForm.btnAdd.requestFocusInWindow(); 
        }
    }
    
    /** this method sets focus back to component
     * @return void
     */
    private void setRequestFocusInFNAThread(final int selrow , final int selcol){
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
        public void run() {
            awardAmountFNADistributionForm.tblAwardFNA.requestFocusInWindow();
            awardAmountFNADistributionForm.tblAwardFNA.changeSelection( selrow, selcol, true, false);
            awardAmountFNADistributionForm.tblAwardFNA.setRowSelectionInterval(selrow, selrow);
           }
        });
    }
    
    private CoeusVector prepareBudgetPeriods(){
        CoeusVector cvPeriods = new CoeusVector();
        Calendar calStart, calEnd, calPeriodStart, calPeriodEnd;
        int startYear, endYear;
        calStart = Calendar.getInstance();
        
        if(awardAmountInfoBean.getAwardEffectiveDate()!=null){
            if(!isCurrentAward()){
                calStart.setTime(awardAmountInfoBean.getAwardEffectiveDate());
            }else{
                calStart.setTime(getBeginDate());
            }
        }else{
            calStart.setTime(getBeginDate());
        }
        
        
        
        //calStart.setTime(awardAmountInfoBean.getAwardEffectiveDate());
        calEnd = Calendar.getInstance();
        calEnd.setTime(awardAmountInfoBean.getFinalExpirationDate());
        
        startYear = calStart.get(Calendar.YEAR);
        endYear = calEnd.get(Calendar.YEAR);
        if(startYear < endYear) {
            //Award Budget spans more thrn a year. Break up required.
            calPeriodStart = calStart;
            calPeriodEnd = Calendar.getInstance();
            int budgetPeriod = 0;
            while(true) {
                budgetPeriod = budgetPeriod + 1;
                AwardAmountFNABean awardAmountFNABean = new AwardAmountFNABean();
                awardAmountFNABean.setMitAwardNumber(awardAmountInfoBean.getMitAwardNumber());
                awardAmountFNABean.setSequenceNumber(awardAmountInfoBean.getSequenceNumber());
                awardAmountFNABean.setAmountSequenceNumber(awardAmountInfoBean.getAmountSequenceNumber());
                awardAmountFNABean.setBudgetPeriod(budgetPeriod);
                awardAmountFNABean.setAcType(TypeConstants.INSERT_RECORD);
                
                awardAmountFNABean.setStartDate(new java.sql.Date(calPeriodStart.getTimeInMillis()));
                
                calPeriodStart.add(Calendar.YEAR, 1);
                calPeriodStart.add(Calendar.DATE, -1);
                calPeriodEnd.setTimeInMillis(calPeriodStart.getTimeInMillis());
                calPeriodStart.add(Calendar.DATE, 1);
                
                if(calPeriodEnd.after(calEnd) || calPeriodEnd.equals(calEnd)) {
                    awardAmountFNABean.setEndDate(awardAmountInfoBean.getFinalExpirationDate());
                    cvPeriods.add(awardAmountFNABean);
                    break;
                }
                
                awardAmountFNABean.setEndDate(new java.sql.Date(calPeriodEnd.getTimeInMillis()));
                cvPeriods.add(awardAmountFNABean);
                
            }
        }else {
            //Generate 1st Period.
            AwardAmountFNABean awardAmountFNABean = new AwardAmountFNABean();
            awardAmountFNABean.setBudgetPeriod(1);
            awardAmountFNABean.setAcType(TypeConstants.INSERT_RECORD);
            awardAmountFNABean.setMitAwardNumber(awardAmountInfoBean.getMitAwardNumber());
            awardAmountFNABean.setSequenceNumber(awardAmountInfoBean.getSequenceNumber());
            awardAmountFNABean.setAmountSequenceNumber(awardAmountInfoBean.getAmountSequenceNumber());
            
            awardAmountFNABean.setStartDate(awardAmountInfoBean.getAwardEffectiveDate());
            awardAmountFNABean.setEndDate(awardAmountInfoBean.getFinalExpirationDate());
            cvPeriods.add(awardAmountFNABean);
        }
        return cvPeriods;
    }
    
    
    public void display() {
        if(awardAmountFNADistributionForm.tblAwardFNA.getRowCount() >0){
            awardAmountFNADistributionForm.tblAwardFNA.setRowSelectionInterval(0,0);
            awardAmountFNADistributionForm.tblAwardFNA.scrollRectToVisible(
            awardAmountFNADistributionForm.tblAwardFNA.getCellRect(0, 0, true));
        }
         modified=false;
         dlgAwardFNADistForm.setVisible(true);
        
        
    }
    
    public void actionPerformed(ActionEvent actionEvent) {
        int index = awardAmountFNADistributionForm.tblAwardFNA.getSelectedRow();
        Object source = actionEvent.getSource();
        dlgAwardFNADistForm.setCursor(new Cursor(Cursor.WAIT_CURSOR));
        try{
            if(source.equals(awardAmountFNADistributionForm.btnOk)){
              performOkAction();
            }else if(source.equals(awardAmountFNADistributionForm.btnCancel)){
                performCancelAction();
            }else if(source.equals(awardAmountFNADistributionForm.btnAdd)){
                performAddAction();
            }else if(source.equals(awardAmountFNADistributionForm.btnInsert)){
                performInsertAction();
            }else if(source.equals(awardAmountFNADistributionForm.btnDelete)){
                performDeleteAction();
            }
        }catch (CoeusException exception){
            exception.printStackTrace();
            CoeusOptionPane.showErrorDialog(exception.getMessage());
        }catch (Exception e){
            e.printStackTrace();
            CoeusOptionPane.showErrorDialog(e.getMessage());
        } finally {
            dlgAwardFNADistForm.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        }
    }
    
    public void saveFormData() throws edu.mit.coeus.exception.CoeusException {
        
        CoeusVector cvFormData = new CoeusVector();
        
        if(cvFNAData!= null && cvFNAData.size() >0){
            cvFormData.addAll(cvFNAData);
        }
        updateSequenceNumber(cvFormData);  
        if(cvDeleteFNA != null && cvDeleteFNA.size() >0){
            cvFormData.addAll(cvDeleteFNA);
        }
        //case #2332 start 5
        if (data != null && data.equalsIgnoreCase("O")) {
           if(cvFNAData == null){
               cvFNAData = new CoeusVector();
           }
           if(cvFNAData != null  && cvFNAData.size() == 0) {
                    AwardAmountFNABean awardAmountFNABean = new AwardAmountFNABean();
                    awardAmountFNABean.setMitAwardNumber(awardAmountInfoBean.getMitAwardNumber());
                    awardAmountFNABean.setSequenceNumber(awardAmountInfoBean.getSequenceNumber());
                    awardAmountFNABean.setAmountSequenceNumber(awardAmountInfoBean.getAmountSequenceNumber());
                    awardAmountFNABean.setStartDate(awardAmountInfoBean.getAwardEffectiveDate());
                    awardAmountFNABean.setEndDate(awardAmountInfoBean.getFinalExpirationDate());
                    awardAmountFNABean.setBudgetPeriod(0);
                    awardAmountFNABean.setDirectCost(0);
                    awardAmountFNABean.setIndirectCost(0);
                    awardAmountFNABean.setAcType(TypeConstants.INSERT_RECORD);
                    cvFormData.addElement(awardAmountFNABean);
           }
           if(cvDelOptData != null && cvDelOptData.size() > 0){
               cvFormData.addAll(cvDelOptData);
           }
        }
        
        //case #2332 end 5
        sendToServer(getOrderedBeans(cvFormData));
    }
    
     /** set the current and max sequence number , current and max amount sequence
     *number while sending the data to the server
     */
    private void updateSequenceNumber(CoeusVector cvFNAData ) throws CoeusException{
        if(cvFNAData!= null && cvFNAData.size() > 0){
            for(int index=0; index < cvFNAData.size(); index++){
                AwardAmountFNABean bean = (AwardAmountFNABean)cvFNAData.get(index);
                if(bean.getSequenceNumber()!= awardAmountInfoBean.getSequenceNumber()){
                     bean.setSequenceNumber(awardAmountInfoBean.getSequenceNumber());
                     bean.setAmountSequenceNumber(awardAmountInfoBean.getAmountSequenceNumber());
                     bean.setAcType(TypeConstants.INSERT_RECORD);
                }
                if(bean.getAmountSequenceNumber()!= awardAmountInfoBean.getAmountSequenceNumber()){
                     bean.setSequenceNumber(awardAmountInfoBean.getSequenceNumber());
                     bean.setAmountSequenceNumber(awardAmountInfoBean.getAmountSequenceNumber());
                     bean.setAcType(TypeConstants.INSERT_RECORD);
                }
            }
        }
    }

    
    private CoeusVector getOrderedBeans(CoeusVector cvData) {
        Equals equals = null;
        CoeusVector cvOrderedBeans = new CoeusVector();
        equals = new Equals("acType",TypeConstants.DELETE_RECORD);
        cvOrderedBeans.addAll(cvData.filter(equals));
        equals = new Equals("acType",TypeConstants.UPDATE_RECORD);
        CoeusVector cvfilterData = cvData.filter(equals);
        cvfilterData.sort("budgetPeriod",false);
        cvOrderedBeans.addAll(cvfilterData);
        equals = new Equals("acType",TypeConstants.INSERT_RECORD);
        cvOrderedBeans.addAll(cvData.filter(equals));
        return cvOrderedBeans;
    }
    
    private void sendToServer(CoeusVector cvData) throws CoeusException{
        boolean success = false;
        RequesterBean request = new RequesterBean();
        request.setFunctionType(UPDATE_FNA);
        request.setDataObject(cvData);
        //Added for bug fixed for Case#2370 start 2
        Vector data = new Vector();
        QueryEngine queryEngine = QueryEngine.getInstance();
        Hashtable awardData = queryEngine.getDataCollection(getQueryKey());
        AwardBean bean = getAwardBean();
        bean.setAcType(TypeConstants.UPDATE_RECORD);
        awardData.put(AwardBean.class, getAwardBean());
        data.insertElementAt(awardData, 0);
        request.setDataObjects(data);
         //Added for bug fixed for Case#2370 end 2
        AppletServletCommunicator comm = new AppletServletCommunicator(connectTo, request);
        comm.send();
        ResponderBean response = comm.getResponse();
        if(response!= null){
            if(!response.isSuccessfulResponse()){
                throw new CoeusException(response.getMessage());
            }
        }else{
            CoeusOptionPane.showErrorDialog(COULD_NOT_CONTACT_SERVER);
        }
        //Added for bug fixed for Case#2370 start 3
        setAwardData((Vector)response.getDataObjects());
        //Added for bug fixed for Case#2370 end 3
    }
    
    
    
    public void performOkAction() throws CoeusException,CoeusClientException{
        awardAmountFNADistrTableCellEditor.stopCellEditing();
        //case #2332 start
        if(modified){
            //case #2332 end
            if(validate()){
                saveFormData();
                dlgAwardFNADistForm.dispose();
            }
        }else{
            // Check for the parameter value for the FNA distribution
            try{
                //case #2332 start
                String paramData = getParameterData();
                if(paramData != null && paramData.trim().equalsIgnoreCase("M") ){
                    double totalAnticipated =  awardAmountInfoBean.getAnticipatedTotalAmount();
                    double tableValue = cvFNAData.sum("directCost")+cvFNAData.sum("indirectCost");
                    if(totalAnticipated != tableValue ){
                        CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(TOTAL_FNA_NOT_EQUAL));
                        return ;
                    }
                }
                //case #2332 end
//                if(getParamAwardFNADistr()){
//                    double totalAnticipated =  awardAmountInfoBean.getAnticipatedTotalAmount();
//                    double tableValue = cvFNAData.sum("directCost")+cvFNAData.sum("indirectCost");
//                    if(totalAnticipated != tableValue ){
//                        CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(TOTAL_FNA_NOT_EQUAL));
//                        return ;
//                    }
//                    
//                }
            }catch(Exception ex){
                ex.printStackTrace();
                CoeusOptionPane.showErrorDialog(ex.getMessage());
            }
            dlgAwardFNADistForm.dispose();
        }
    }
    

    
    public void performCancelAction() {
        awardAmountFNADistrTableCellEditor.stopCellEditing();
       // boolean canClose = true;
        if(modified) {
            int option = CoeusOptionPane.showQuestionDialog(
            coeusMessageResources.parseMessageKey("saveConfirmCode.1002"),
            CoeusOptionPane.OPTION_YES_NO_CANCEL,CoeusOptionPane.DEFAULT_CANCEL);
            if(option == CoeusOptionPane.SELECTION_YES){
                try {
                    if(validate()){
                        saveFormData();
                        dlgAwardFNADistForm.dispose();
                    }
                }catch(CoeusException ex) {
                    //canClose = false;
                    ex.printStackTrace();
                    CoeusOptionPane.showErrorDialog(ex.getMessage());
                }catch(CoeusUIException ex){
                  ex.printStackTrace();
                    CoeusOptionPane.showErrorDialog(ex.getMessage());  
                }
            }else if(option == CoeusOptionPane.SELECTION_NO) {
                 dlgAwardFNADistForm.dispose();
            }
        }else{
            dlgAwardFNADistForm.dispose();
        }
//        if(canClose)
//            dlgAwardFNADistForm.dispose();
    }
    
    
    /** Adds a new empty period.The Mode is INSERT. */
    public void performAddAction(){
        awardAmountFNADistrTableCellEditor.stopCellEditing();
        awardAmountFNADistributionForm.btnDelete.setEnabled(true);
        AwardAmountFNABean newAwardAmountFNABean = new AwardAmountFNABean();
        newAwardAmountFNABean.setMitAwardNumber(awardAmountInfoBean.getMitAwardNumber());
        newAwardAmountFNABean.setSequenceNumber(awardAmountInfoBean.getSequenceNumber());
        newAwardAmountFNABean.setAmountSequenceNumber(awardAmountInfoBean.getAmountSequenceNumber());
        newAwardAmountFNABean.setBudgetPeriod(cvFNAData.size() +1);
        newAwardAmountFNABean.setAcType(TypeConstants.INSERT_RECORD);
        modified=true;
        cvFNAData.add(newAwardAmountFNABean);
        awardAmountFNADistrTableModel.fireTableRowsInserted(awardAmountFNADistrTableModel.getRowCount()+1,
        awardAmountFNADistrTableModel.getRowCount()+1);
        
        int lastRow = awardAmountFNADistributionForm.tblAwardFNA.getRowCount()-1;
        if(lastRow >= 0){
            awardAmountFNADistributionForm.tblAwardFNA.setRowSelectionInterval(lastRow,lastRow);
            awardAmountFNADistributionForm.tblAwardFNA.setColumnSelectionInterval(START_DATE_COLUMN,START_DATE_COLUMN);
//            awardAmountFNADistributionForm.tblAwardFNA.scrollRectToVisible(
//            awardAmountFNADistributionForm.tblAwardFNA.getCellRect(lastRow, HAND_ICON_COLUMN, true));
            
        }
        awardAmountFNADistributionForm.tblAwardFNA.editCellAt(lastRow,START_DATE_COLUMN);
        awardAmountFNADistributionForm.tblAwardFNA.getEditorComponent().requestFocusInWindow();
        
    }
    /** This method will specifies the  inserting of a new period as row+1 */
    private void performInsertAction(){
        int row = awardAmountFNADistributionForm.tblAwardFNA.getSelectedRow();
        if(row!=-1){
            awardAmountFNADistrTableCellEditor.stopCellEditing();
            AwardAmountFNABean awardAmountFNABean = null;
            
            awardAmountFNABean = new AwardAmountFNABean();
            awardAmountFNABean.setMitAwardNumber(awardAmountInfoBean.getMitAwardNumber());
            awardAmountFNABean.setSequenceNumber(awardAmountInfoBean.getSequenceNumber());
            awardAmountFNABean.setAmountSequenceNumber(awardAmountInfoBean.getAmountSequenceNumber());
            awardAmountFNABean.setBudgetPeriod(row+1);
            awardAmountFNABean.setStartDate(null);
            awardAmountFNABean.setEndDate(null);
            awardAmountFNABean.setDirectCost(0.00);
            awardAmountFNABean.setIndirectCost(0.00);
            awardAmountFNABean.setAcType(TypeConstants.INSERT_RECORD);
             modified=true;            
            cvFNAData.add(row, awardAmountFNABean);
            awardAmountFNADistrTableModel.fireTableRowsInserted(row,row);
            awardAmountFNADistributionForm.tblAwardFNA.setRowSelectionInterval(row,row);
            awardAmountFNADistributionForm.tblAwardFNA.setColumnSelectionInterval(START_DATE_COLUMN,START_DATE_COLUMN);
            awardAmountFNADistributionForm.tblAwardFNA.editCellAt(row,START_DATE_COLUMN);
            
//          awardAmountFNADistributionForm.tblAwardFNA.scrollRectToVisible(
//          awardAmountFNADistributionForm.tblAwardFNA.getCellRect(row, HAND_ICON_COLUMN, true));
          awardAmountFNADistributionForm.tblAwardFNA.getEditorComponent().requestFocusInWindow();
            
            for(int index = row + 1; index < cvFNAData.size(); index++) {
                awardAmountFNABean = (AwardAmountFNABean)cvFNAData.get(index);
                awardAmountFNABean.setBudgetPeriod(awardAmountFNABean.getBudgetPeriod() + 1);
                if(awardAmountFNABean.getAcType() == null)
                    awardAmountFNABean.setAcType(TypeConstants.UPDATE_RECORD);
//                 modified=true;
            }
            awardAmountFNADistrTableModel.fireTableCellUpdated(row + 1, cvFNAData.size());
        }else{
            performAddAction();
        }
    }
    
    
    
    public void performDeleteAction(){
        awardAmountFNADistrTableCellEditor.stopCellEditing();
        int rowCount=awardAmountFNADistributionForm.tblAwardFNA.getRowCount();
        int selectedRow=awardAmountFNADistributionForm.tblAwardFNA.getSelectedRow();
        
        
        int yesNo=CoeusOptionPane.showQuestionDialog(coeusMessageResources.parseMessageKey(DELETE_CONFIRM), 2, 3);
        if(yesNo==CoeusOptionPane.SELECTION_YES){
            if (selectedRow != -1&&selectedRow<=rowCount) {
                AwardAmountFNABean  awardAmountFNABean=(AwardAmountFNABean)cvFNAData.get(selectedRow);                
                if(!TypeConstants.INSERT_RECORD.equals(awardAmountFNABean.getAcType())){
                    //case #2332 start
                    awardAmountFNABean.setAcType(null);
                    if((awardAmountFNABean.getSequenceNumber() == awardAmountInfoBean.getSequenceNumber())
                            && (awardAmountFNABean.getAmountSequenceNumber() == awardAmountInfoBean.getAmountSequenceNumber()) ){
                        awardAmountFNABean.setAcType(TypeConstants.DELETE_RECORD);
                    }
                   /**Set the current and max sequence Number,current and max amount sequence
                     *Number,If the rows deleted are from previous sequence number ignore it.
                     */
                    
//                    if(awardAmountFNABean.getSequenceNumber()!=awardAmountInfoBean.getSequenceNumber()){
//                        awardAmountFNABean.setSequenceNumber(awardAmountInfoBean.getSequenceNumber());
//                        awardAmountFNABean.setAmountSequenceNumber(awardAmountInfoBean.getAmountSequenceNumber());
//                        awardAmountFNABean.setAcType(null);
//                    }
//                    if(awardAmountFNABean.getAmountSequenceNumber()!=awardAmountInfoBean.getAmountSequenceNumber()){
//                        awardAmountFNABean.setSequenceNumber(awardAmountInfoBean.getSequenceNumber());
//                        awardAmountFNABean.setAmountSequenceNumber(awardAmountInfoBean.getAmountSequenceNumber());
//                        awardAmountFNABean.setAcType(null);
//                    }
                    //case #2332 start
                    cvDeleteFNA.add(awardAmountFNABean);
                }
                cvFNAData.remove(selectedRow);
                awardAmountFNADistrTableModel.fireTableDataChanged();
                modified = true;
                awardFNATotalTableModel.fireTableDataChanged();
                awardFNATtlAmtTableModel.fireTableDataChanged();
                if(cvFNAData.size()==0){
                    awardAmountFNADistributionForm.btnDelete.setEnabled(false);
                    awardAmountFNADistributionForm.btnAdd.requestFocusInWindow();
                }
                for(int index = selectedRow; index < cvFNAData.size(); index++) {
                    AwardAmountFNABean bean = (AwardAmountFNABean)cvFNAData.get(index);
                    bean.setBudgetPeriod(bean.getBudgetPeriod() - 1);
                   
                }                
                awardAmountFNADistrTableModel.fireTableCellUpdated(selectedRow, cvFNAData.size());
                if(cvFNAData.size()>0) {
                    selectedRow = selectedRow==cvFNAData.size() ? selectedRow-1 : selectedRow;
                    awardAmountFNADistributionForm.tblAwardFNA.setRowSelectionInterval(selectedRow, selectedRow);
                }
                    
            }
        }
    }
    
    public double getTotalCost() {
        return cvFNAData.sum("directCost")+cvFNAData.sum("indirectCost");
    }
    
    private void setTableEditors(){
        try{
            JTableHeader tableHeader = awardAmountFNADistributionForm.tblAwardFNA.getTableHeader();
            tableHeader.setReorderingAllowed(false);
            tableHeader.setMaximumSize(new Dimension(100,27));
            tableHeader.setMinimumSize(new Dimension(100,27));
            tableHeader.setPreferredSize(new Dimension(100,27));            
            tableHeader.setFont(CoeusFontFactory.getLabelFont());
            
            
            awardAmountFNADistributionForm.tblAwardFNA.setRowHeight(22);
            awardAmountFNADistributionForm.tblAwardFNA.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
            awardAmountFNADistributionForm.tblAwardFNA.setShowHorizontalLines(false);
            awardAmountFNADistributionForm.tblAwardFNA.setSelectionBackground(java.awt.Color.white);
            awardAmountFNADistributionForm.tblAwardFNA.setSelectionForeground(java.awt.Color.black);
            awardAmountFNADistributionForm.tblAwardFNA.setShowVerticalLines(false);
            awardAmountFNADistributionForm.tblAwardFNA.setOpaque(false);
            awardAmountFNADistributionForm.tblAwardFNA.setSelectionMode(
            DefaultListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
            
            awardAmountFNADistributionForm.tblTotal.setShowHorizontalLines(false);
            awardAmountFNADistributionForm.tblTotal.setShowVerticalLines(false);
            awardAmountFNADistributionForm.tblTotal.setSelectionBackground(java.awt.Color.white);
            awardAmountFNADistributionForm.tblTotal.setSelectionForeground(java.awt.Color.black);
            awardAmountFNADistributionForm.tblTotal.setOpaque(true);
            
            awardAmountFNADistributionForm.tblDetailTotal.setShowHorizontalLines(false);
            awardAmountFNADistributionForm.tblDetailTotal.setShowVerticalLines(false);
            awardAmountFNADistributionForm.tblDetailTotal.setSelectionBackground(java.awt.Color.white);
            awardAmountFNADistributionForm.tblDetailTotal.setSelectionForeground(java.awt.Color.black);
            awardAmountFNADistributionForm.tblDetailTotal.setOpaque(true);
            
            TableColumn columnDetails=awardAmountFNADistributionForm.tblAwardFNA.getColumnModel().getColumn(PERIOD_COLUMN);
            
            columnDetails.setPreferredWidth(65);
            columnDetails.setCellEditor(awardAmountFNADistrTableCellEditor);
            columnDetails.setCellRenderer(awardAmountFNATableCellRenderer);
            
            columnDetails=awardAmountFNADistributionForm.tblAwardFNA.getColumnModel().getColumn(START_DATE_COLUMN);
            columnDetails.setPreferredWidth(90);
            columnDetails.setCellEditor(awardAmountFNADistrTableCellEditor);
            columnDetails.setCellRenderer(awardAmountFNATableCellRenderer);
            
            columnDetails=awardAmountFNADistributionForm.tblAwardFNA.getColumnModel().getColumn(END_DATE_COLUMN);
            columnDetails.setPreferredWidth(90);
            columnDetails.setCellEditor(awardAmountFNADistrTableCellEditor);
            columnDetails.setCellRenderer(awardAmountFNATableCellRenderer);
            
            columnDetails=awardAmountFNADistributionForm.tblAwardFNA.getColumnModel().getColumn(DIRECT_COST_COLUMN);
            columnDetails.setPreferredWidth(105);
            columnDetails.setCellEditor(awardAmountFNADistrTableCellEditor);
            columnDetails.setCellRenderer(awardAmountFNATableCellRenderer);
            
            columnDetails=awardAmountFNADistributionForm.tblAwardFNA.getColumnModel().getColumn(INDIRECT_COST_COLUMN);
            columnDetails.setPreferredWidth(105);
            columnDetails.setCellEditor(awardAmountFNADistrTableCellEditor);
            columnDetails.setCellRenderer(awardAmountFNATableCellRenderer);
            
            
            TableColumn column = awardAmountFNADistributionForm.tblTotal.getColumnModel().getColumn(TOTAL_COLUMN);
            column.setPreferredWidth(270);
            column.setCellRenderer(awardFNATotalTableCellRenderer);
            
            TableColumn columnData = awardAmountFNADistributionForm.tblTotal.getColumnModel().getColumn(DIRECT_TOTAL);
            columnData.setPreferredWidth(100);
            columnData.setCellRenderer(awardFNATotalTableCellRenderer);
            
            columnData = awardAmountFNADistributionForm.tblTotal.getColumnModel().getColumn(INDIRECT_TOTAL);
            columnData.setPreferredWidth(100);
            columnData.setCellRenderer(awardFNATotalTableCellRenderer);     
            
            
            TableColumn amtColumn = awardAmountFNADistributionForm.tblDetailTotal.getColumnModel().getColumn(DETAIL_TOTAL_COLUMN);
            amtColumn.setPreferredWidth(60);
            amtColumn.setCellRenderer(awardFNADetailTotalTableCellRenderer);
            
            amtColumn=awardAmountFNADistributionForm.tblDetailTotal.getColumnModel().getColumn(TOTAL_COLUMN);
            amtColumn.setPreferredWidth(120);
            amtColumn.setCellRenderer(awardFNADetailTotalTableCellRenderer);
            
           
            
        }catch(Exception e){
            e.printStackTrace();
            CoeusOptionPane.showErrorDialog(e.getMessage());
        }
    }
    
    
    public class AwardAmountFNADistrTableModel extends AbstractTableModel{
        
        private String colNames[]={"Period","Start Date","End Date","Direct Cost","Indirect Cost"};
        private Class colClass[]={Integer.class,String.class,String.class,Double.class,Double.class};
        
        public int getColumnCount() {
            return colNames.length;
        }
        
        public String getColumnName(int col){
            return colNames[col];
        }
        public Class getColumnClass(int col){
            return colClass[col];
        }
        public boolean isCellEditable(int row, int col){
            if(col==PERIOD_COLUMN || functionType==CoeusGuiConstants.DISPLAY_MODE){
                return false;
            }else{
                return true;
            }
        }
        
        public int getRowCount() {
            if(cvFNAData==null){
                return 0;
            }else{
                return cvFNAData.size();
            }
        }
        
        public void setData(CoeusVector cvFNAData){
            cvFNAData = cvFNAData;
        }
        
        public Object getValueAt(int row, int col) {
            AwardAmountFNABean bean = (AwardAmountFNABean)cvFNAData.get(row);
            switch(col){
                case PERIOD_COLUMN:
                    return new Integer(bean.getBudgetPeriod());
                case START_DATE_COLUMN:
                    return bean.getStartDate();
                case END_DATE_COLUMN:
                    return bean.getEndDate();
                case DIRECT_COST_COLUMN:
                    return new Double(bean.getDirectCost());
                case INDIRECT_COST_COLUMN:
                    return new Double(bean.getIndirectCost());
            }
            return EMPTY_STRING;
        }
        
        public void setValueAt(Object value, int row, int col){
            if(cvFNAData==null) return;
            double sumTotCost=0.0;
            double cost;
            String message=null;
            Date date = null;
            String strDate=null;
            java.sql.Date valueDate = null;
            java.sql.Date beanDate = null;
            
            AwardAmountFNABean bean = (AwardAmountFNABean)cvFNAData.get(row);
            switch(col){
                case START_DATE_COLUMN:
                    try{
                        if (value.toString().trim().length() > 0) {
                            strDate = dtUtils.formatDate(
                            value.toString().trim(), DATE_SEPARATERS, REQUIRED_DATE_FORMAT);
                        } else {
                            if(!value.toString().trim().equals(bean.getStartDate()))
                                modified = true;
                            bean.setStartDate(null);
                            return;
                        }
                        strDate = dtUtils.restoreDate(strDate, DATE_SEPARATERS);
                        if(strDate==null) {
                            throw new CoeusException();
                        }
                        date = dtFormat.parse(strDate.trim());
                    }catch (ParseException parseException) {
                        parseException.printStackTrace();
                        message = coeusMessageResources.parseMessageKey(
                        INVALID_START_DATE);
                        CoeusOptionPane.showErrorDialog(message);
                        return ;
                    }
                    catch (CoeusException coeusException) {
                        message = coeusMessageResources.parseMessageKey(
                        INVALID_START_DATE);
                        CoeusOptionPane.showErrorDialog(message);
                        return ;
                    }
                    valueDate = new java.sql.Date(date.getTime());
                    beanDate = bean.getStartDate();
                    if(!valueDate.equals(beanDate)){
                        if(bean.getAcType() == null) {
                            bean.setAcType(TypeConstants.UPDATE_RECORD);
                        }
                        bean.setStartDate(new java.sql.Date(date.getTime()));
                        modified=true;
                    }
                    break;
                case END_DATE_COLUMN:
                    try{
                        
                        if (value.toString().trim().length() > 0) {
                            strDate = dtUtils.formatDate(value.toString(), DATE_SEPARATERS, REQUIRED_DATE_FORMAT);
                        } else {
                            if(!value.toString().trim().equals(bean.getEndDate()))
                                modified = true;
                            bean.setEndDate(null);
                            return;
                        }
                        strDate = dtUtils.restoreDate(strDate, DATE_SEPARATERS);
                        if(strDate == null ) {
                            throw new CoeusException();
                        }
                        date = dtFormat.parse(strDate.trim());
                    }catch (ParseException parseException) {
                        parseException.printStackTrace();
                        message = coeusMessageResources.parseMessageKey(
                        INVALID_END_DATE);
                        CoeusOptionPane.showErrorDialog(message);
                        return ;
                    }catch (CoeusException coeusException) {
                        message = coeusMessageResources.parseMessageKey(
                        INVALID_END_DATE);
                        CoeusOptionPane.showErrorDialog(message);
                        return ;
                    }
                    valueDate = new java.sql.Date(date.getTime());
                    
                    beanDate = bean.getEndDate();
                    if(!valueDate.equals(beanDate)){
                        if(bean.getAcType() == null) {
                            bean.setAcType(TypeConstants.UPDATE_RECORD);
                        }
                        bean.setEndDate(new java.sql.Date(date.getTime()));
                        modified=true;
                    }
                    break;
                case DIRECT_COST_COLUMN:
                    cost = new Double(value.toString()).doubleValue();
                    if(cost!=  bean.getDirectCost()){
                        modified=true;
                        if(bean.getAcType() == null) {
                            bean.setAcType(TypeConstants.UPDATE_RECORD);
                        }
                        bean.setDirectCost(cost);
                        
                    }
                    awardFNATotalTableModel.fireTableCellUpdated(TOTAL_COLUMN, DIRECT_TOTAL);
                    break;
                case INDIRECT_COST_COLUMN:
                    cost = new Double(value.toString()).doubleValue();
                    if(cost!= bean.getIndirectCost()){
                        modified=true;
                        if(bean.getAcType() == null) {
                            bean.setAcType(TypeConstants.UPDATE_RECORD);
                        }
                        bean.setIndirectCost(cost);
                                            }
                    awardFNATotalTableModel.fireTableCellUpdated(TOTAL_COLUMN, INDIRECT_TOTAL);
                    break;
            }
        }
    }
    
    
    public class AwardAmountFNADistrTableCellEditor extends AbstractCellEditor implements TableCellEditor{
        private CoeusTextField txtComponent;
        private DollarCurrencyTextField txtCurrency;
        private int column;
        
        public AwardAmountFNADistrTableCellEditor(){
            txtComponent= new CoeusTextField();
            txtCurrency= new DollarCurrencyTextField();
            txtComponent.setBorder(new javax.swing.border.EmptyBorder(0,0,0,0));
            txtCurrency.setBorder(new javax.swing.border.EmptyBorder(0,0,0,0));
        }
        
        public java.awt.Component getTableCellEditorComponent(JTable table, Object value,
        boolean isSelected, int row, int col){
            this.column = col;
            switch(col) {
                case START_DATE_COLUMN :
                case END_DATE_COLUMN:
                    if(value == null || value.toString().equals(EMPTY_STRING)) {
                        txtComponent.setText(EMPTY_STRING);
                        return txtComponent;
                    }
                    String strDate = dtUtils.formatDate(value.toString(),SIMPLE_DATE_FORMAT);
                    if(strDate== null){
                        txtComponent.setText(value.toString());
                    }else{
                        txtComponent.setText(strDate);
                        return txtComponent;
                    }
                case DIRECT_COST_COLUMN :
                case INDIRECT_COST_COLUMN:
                    txtCurrency.setValue(new Double(value.toString()).doubleValue());
                    return txtCurrency;
            }
            return txtComponent;
        }
        
        public Object getCellEditorValue() {
            switch (column) {
                case DIRECT_COST_COLUMN:
                case INDIRECT_COST_COLUMN:
                    return txtCurrency.getValue();
                case START_DATE_COLUMN:
                case END_DATE_COLUMN:
                    return txtComponent.getText();
            }
            return ((CoeusTextField)txtComponent).getText();
        }
    }// end of AwardAmountFNADistrTableCellEditor...
    
    
    
    public class AwardAmountFNADistrTableCellRenderer extends DefaultTableCellRenderer{
        private DollarCurrencyTextField txtCurrency;
        private JLabel label;
        private java.awt.Color color;
        public AwardAmountFNADistrTableCellRenderer(){
            txtCurrency= new DollarCurrencyTextField();
            label = new JLabel();
            label.setBorder(new javax.swing.border.EmptyBorder(0,0,0,0));
            label.setOpaque(true);
            color = (java.awt.Color) javax.swing.UIManager.getDefaults().get("Panel.background");
        }
        
        public java.awt.Component getTableCellRendererComponent(JTable table, Object value,
        boolean isSelected, boolean hasFocus, int row, int col){
            
           
                    
            switch(col) {
                case PERIOD_COLUMN :
                    if(functionType == TypeConstants.DISPLAY_MODE ){
                        label.setBackground(disabledBackground);
                        label.setForeground(java.awt.Color.BLACK);
                    }else if(isSelected){
                        label.setBackground(Color.YELLOW);
                        label.setForeground(Color.black);
                    }else {
                        label.setBackground(Color.white);
                        label.setForeground(Color.black);
                    }
                    
                    
                    if(value == null || value.toString().trim().equals(EMPTY_STRING)){
                        label.setText(EMPTY_STRING);
                    }else{
                        label.setText(value.toString());
                    }
                    label.setHorizontalAlignment(LEFT);
                    return label;
                    
                case START_DATE_COLUMN :
                    
                case END_DATE_COLUMN :
                    if(functionType == TypeConstants.DISPLAY_MODE ){
                        label.setBackground(disabledBackground);
                        label.setForeground(java.awt.Color.BLACK);
                    }else if(isSelected){
                        label.setBackground(Color.YELLOW);
                        label.setForeground(Color.black);
                    }  else {
                        label.setBackground(Color.white);
                        label.setForeground(Color.black);
                    }
                    if(value == null || value.toString().trim().equals(EMPTY_STRING)){
                        label.setText(EMPTY_STRING);
                    }else{
                        value = dtUtils.formatDate(value.toString(),REQUIRED_DATE_FORMAT);
                        label.setText(value.toString());
                    }
                    label.setHorizontalAlignment(LEFT);
                    return label;
                    
                case DIRECT_COST_COLUMN :
                    
                case INDIRECT_COST_COLUMN :
                    if(functionType == TypeConstants.DISPLAY_MODE ){
                        label.setBackground(disabledBackground);
                        label.setForeground(java.awt.Color.BLACK);
                    }else if(isSelected){
                        label.setBackground(Color.YELLOW);
                        label.setForeground(Color.black);
                    } else {
                        label.setBackground(Color.white);
                        label.setForeground(Color.black);
                    }
                   
                    txtCurrency.setText(value.toString());
                    label.setHorizontalAlignment(RIGHT);
                    label.setText(txtCurrency.getText());
                    return label;
            }
            return label;
        }
    }// end of AwardAmountFNADistrTableCellRenderer...
    
    public class AwardFNATotalTableModel extends AbstractTableModel{
        
        private String colNames[] = { "Total Amount", EMPTY_STRING,EMPTY_STRING};
        private Class colClass[] = {String.class,Double.class,Double.class};
        
        
        public int getColumnCount() {
            return colNames.length;
        }
        
        public int getRowCount() {
            return 1;
        }
        
        /* gets the value at the cell*/
        public Object getValueAt(int row, int col) {
            double totalAmount = 0.00;
            String name = "Total:";
            if(col==TOTAL_COLUMN){
                return name;
            }
            if(col==DIRECT_TOTAL){
                totalAmount = cvFNAData.sum("directCost");
                awardFNATtlAmtTableModel.fireTableCellUpdated(TOTAL_COLUMN, DETAIL_TOTAL_COLUMN);
                return new Double(totalAmount);
            }
            if(col==INDIRECT_TOTAL){
                totalAmount = cvFNAData.sum("indirectCost");
                awardFNATtlAmtTableModel.fireTableCellUpdated(TOTAL_COLUMN, DETAIL_TOTAL_COLUMN);
                return new Double(totalAmount);
            }
            
            return EMPTY_STRING;
        }
        
    }
    
    public class AwardFNATotalTableCellRenderer extends DefaultTableCellRenderer{
        private DollarCurrencyTextField txtCurrency;
        private JLabel lblTotal,lblCurrency,lblDirect;
        
        public AwardFNATotalTableCellRenderer(){
            txtCurrency=new DollarCurrencyTextField();
            txtCurrency.setBackground(javax.swing.UIManager.getDefaults().getColor("Panel.background"));
            txtCurrency.setEnabled(false);
            txtCurrency.setForeground(java.awt.Color.BLACK);
            txtCurrency.setBorder(new javax.swing.border.EmptyBorder(0,0,0,0));
            lblTotal =new JLabel();
            lblTotal.setOpaque(true);
            lblTotal.setHorizontalAlignment(RIGHT);
            lblTotal.setFont(CoeusFontFactory.getLabelFont());
            lblCurrency = new JLabel();
            lblCurrency.setOpaque(true);
            lblCurrency.setHorizontalAlignment(RIGHT);
            lblCurrency.setFont(CoeusFontFactory.getLabelFont());
            lblDirect = new JLabel();
            lblDirect.setOpaque(true);
            lblDirect.setHorizontalAlignment(RIGHT);
            lblDirect.setFont(CoeusFontFactory.getLabelFont());
            
        }
        
        public java.awt.Component getTableCellRendererComponent(JTable table, Object value,
        boolean isSelected, boolean hasFocus, int row, int col){
            
            switch(col){
               case TOTAL_COLUMN :
                    if(value == null && value.toString().trim().equals(EMPTY_STRING)){
                        lblTotal.setText(EMPTY_STRING);
                    }else{
                        lblTotal.setText(value.toString());
                    }
                    return lblTotal;
                    
                case DIRECT_TOTAL :
                    if(value == null || value.toString().trim().equals(EMPTY_STRING)){
                        txtCurrency.setText(EMPTY_STRING);
                    }else{
                        txtCurrency.setValue(new Double(value.toString()).doubleValue());
                        lblDirect.setText(txtCurrency.getText());
                    }
                    return lblDirect;
                case INDIRECT_TOTAL:
                    if(value == null || value.toString().trim().equals(EMPTY_STRING)){
                        txtCurrency.setText(EMPTY_STRING);
                    }else{
                        txtCurrency.setValue(new Double(value.toString()).doubleValue());
                        lblCurrency.setText(txtCurrency.getText());
                    }
                    return lblCurrency;
            }
            return lblCurrency;
        }
    }
    
    
    public class AwardFNATtlAmtTableModel extends AbstractTableModel{
        private String colNames[]={"Total",EMPTY_STRING};
        private Class colClass[]={String.class,Double.class};
        
        public int getColumnCount() {
            return colNames.length;
        }
        
        public int getRowCount() {
            return 1;
        }
        
        public Object getValueAt(int row, int col) {
            String colName = "Total  Anticipated (Direct + Indirect):";
            if(col==TOTAL_COLUMN){
                return colName;
            }
            
            if(col==DETAIL_TOTAL_COLUMN){
                double totalAmount = cvFNAData.sum("directCost")+cvFNAData.sum("indirectCost");
                return new Double(totalAmount);
            }
            return EMPTY_STRING;
        }
    }
    
    public class AwardFNADetailTotalTableCellRenderer extends DefaultTableCellRenderer{
        private CoeusTextField txtComponent;
        private DollarCurrencyTextField txtCurrency;
        private JLabel lblComponent,lblCurrency;
        
        public AwardFNADetailTotalTableCellRenderer(){
            txtComponent=new CoeusTextField();
            txtCurrency=new DollarCurrencyTextField();
            lblComponent =new JLabel();
            lblCurrency= new JLabel();
            lblComponent.setOpaque(true);
            lblComponent.setHorizontalAlignment(RIGHT);
            lblComponent.setOpaque(true);
            lblComponent.setFont(CoeusFontFactory.getLabelFont());
            lblComponent.setForeground(java.awt.Color.blue);
            lblCurrency.setHorizontalAlignment(RIGHT);
            lblCurrency.setFont(CoeusFontFactory.getLabelFont());
            lblCurrency.setForeground(java.awt.Color.blue);
            txtComponent.setBackground(
            (java.awt.Color) javax.swing.UIManager.getDefaults().get("Panel.background"));
            txtComponent.setForeground(java.awt.Color.BLACK);
            txtComponent.setBorder(new javax.swing.border.EmptyBorder(0,0,0,0));
            txtComponent.setBorder(new javax.swing.border.EmptyBorder(0,0,0,0));
            
        }
        
        public java.awt.Component getTableCellRendererComponent(JTable table, Object value,
        boolean isSelected, boolean hasFocus, int row, int col){
            
            switch(col){
                case TOTAL_COLUMN :
                    if(value == null && value.toString().trim().equals(EMPTY_STRING)){
                        txtComponent.setText(EMPTY_STRING);
                        lblComponent.setText(txtComponent.getText());
                    }else{
                        txtComponent.setText(value.toString());
                        lblComponent.setText(txtComponent.getText());
                    }
                    return lblComponent;
                    
                case DETAIL_TOTAL_COLUMN:
                    
                    if(value == null && value.toString().trim().equals(EMPTY_STRING)){
                        txtCurrency.setText(EMPTY_STRING);
                        lblCurrency.setText(txtCurrency.getText());
                    }else{
                        txtCurrency.setText(value.toString());
                        lblCurrency.setText(txtCurrency.getText());
                    }
                    return lblCurrency;
            }
            return lblComponent;
            
        }
        
    }
    
    public void cleanUp() {      
        awardAmountInfoBean =null;
        cvFNAData = null;
        cvDeleteFNA = null;
        
        awardFNATotalTableModel = null;
        awardAmountFNADistrTableModel = null;
        awardFNATotalTableCellRenderer = null;
        awardFNATtlAmtTableModel = null;
        awardFNADetailTotalTableCellRenderer = null;
        awardAmountFNATableCellRenderer = null;
        awardAmountFNADistrTableCellEditor = null;
        dlgAwardFNADistForm = null;
        awardAmountFNADistributionForm = null;
        //Added for bug fixed for Case#2370 start 4
        awardData = null;
        //Added for bug fixed for Case#2370 end 4
        
    }
    
    /**
     * Getter for property beginDate.
     * @return Value of property beginDate.
     */
    public java.sql.Date getBeginDate() {
        return beginDate;
    }
    
    /**
     * Setter for property beginDate.
     * @param beginDate New value of property beginDate.
     */
    public void setBeginDate(java.sql.Date beginDate) {
        this.beginDate = beginDate;
    }
    
    /**
     * Getter for property currentAward.
     * @return Value of property currentAward.
     */
    public boolean isCurrentAward() {
        return currentAward;
    }
    
    /**
     * Setter for property currentAward.
     * @param currentAward New value of property currentAward.
     */
    public void setCurrentAward(boolean currentAward) {
        this.currentAward = currentAward;
    }
    //Added for bug fixed for case#2370 start 5
    /**
     * Getter for property queryKey.
     * @return Value of property queryKey.
     */
    public java.lang.String getQueryKey() {
        return queryKey;
    }
    
    /**
     * Setter for property queryKey.
     * @param queryKey New value of property queryKey.
     */
    public void setQueryKey(java.lang.String queryKey) {
        this.queryKey = queryKey;
    }
    
    /**
     * Getter for property awardBean.
     * @return Value of property awardBean.
     */
    public edu.mit.coeus.award.bean.AwardBean getAwardBean() {
        return awardBean;
    }
    
    /**
     * Setter for property awardBean.
     * @param awardBean New value of property awardBean.
     */
    public void setAwardBean(edu.mit.coeus.award.bean.AwardBean awardBean) {
        this.awardBean = awardBean;
    }
    
    /**
     * Getter for property awardData.
     * @return Value of property awardData.
     */
    public java.util.Vector getAwardData() {
        return awardData;
    }
    
    /**
     * Setter for property awardData.
     * @param awardData New value of property awardData.
     */
    public void setAwardData(java.util.Vector awardData) {
        this.awardData = awardData;
    }
    
    //Added for bug fixed for case#2370 end 5
}