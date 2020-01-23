/** Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

/*
 * IPReviewController.java
 *
 * Created on April 13, 2004, 5:21 PM
 */

package edu.mit.coeus.instprop.controller;

/**
 *
 * @author  bijosht
 */
import edu.mit.coeus.utils.*;
import edu.mit.coeus.utils.query.*;
import edu.mit.coeus.instprop.gui.IPReviewForm;
import edu.mit.coeus.instprop.bean.*;
import edu.mit.coeus.instprop.controller.InstituteProposalController;
import edu.mit.coeus.search.gui.CoeusSearch;
import edu.mit.coeus.gui.CoeusAppletMDIForm;
import edu.mit.coeus.bean.*;
import edu.mit.coeus.gui.CoeusFontFactory;
import javax.swing.DefaultListSelectionModel;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.departmental.gui.PersonDetailForm;
import edu.mit.coeus.gui.event.*;
import edu.mit.coeus.irb.bean.PersonInfoFormBean;

import javax.swing.table.*;
import javax.swing.AbstractCellEditor;
import javax.swing.JTextField;
import java.awt.event.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Date;
import javax.swing.JOptionPane;
import java.text.ParseException;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.SwingUtilities;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JTable;
import javax.swing.KeyStroke;

/**
 * This class is the controller for the IP review. It handles the activities in IP
 * Review.
 */
public class IPReviewController extends InstituteProposalController implements
ActionListener,BeanUpdatedListener{
    private static final String EMPTY_STRING = "";
    private static final int IPREVIEW_ACTIVITY_TYPE_COLUMN = 0;
    private static final int ACTIVITY_DATE_COLUMN = 1;
    private static final int COMMENTS_COLUMN = 2;
    private static final String DELETE_CONFIRMATION = "budgetPersons_exceptionCode.1305";
    private static final char PERSON_DISPLAY_MODE='D';
    private CoeusMessageResources coeusMessageResources;
    private IPReviewTableModel iPReviewTableModel;
    private IPReviewTableCellEditor iPReviewTableCellEditor;
    private IPReviewTableCellRenderer iPReviewTableCellRenderer;
    private IPReviewForm iPReviewForm;
    private InstituteProposalCommentsBean reviewCommentsBean;
    private InstituteProposalCommentsBean reviewerCommentsBean;
    private QueryEngine queryEngine;
    private int rowID = 1; //Used for uniquely identifying InstituteProposalIPReviewActivityBean
    private boolean isCommentModified=false;
    private boolean isReviewCommentSame=true;
    private boolean isReviewerCommentSame=true;
    private boolean checkRight = false;
    
    private CoeusParameterBean coeusParameterBean;
    private CoeusParameterBean coeusReviewerParameterBean;
    
    private CoeusVector cvProposalData;
    private CoeusVector cvIpTableData;
    private CoeusVector cvComments;
    private CoeusVector cvCommentDescription;
    private CoeusVector cvActivityType;
    private CoeusVector cvReviewRequirementType;
    private CoeusVector cvReviewResultType;  
    private CoeusVector cvDeletedData;
    private CoeusVector cvReviewComment;
    private CoeusVector cvReviewerComment;
    
    private char functionType;
    private CoeusAppletMDIForm mdiForm;    
    private InstituteProposalBaseBean instituteProposalBaseBean;
    private InstituteProposalBean instituteProposalBean;

    private boolean isCommentPresent=false;
    private boolean isIPReviewCommentPresent=false;
    private boolean activityTypeComboPopulated=false;
    private DateUtils dtUtils = new DateUtils();
    private java.text.SimpleDateFormat dtFormat
    = new java.text.SimpleDateFormat("MM/dd/yyyy");
    private static final String DATE_SEPARATERS = ":/.,|-";
    private static final String REQUIRED_DATEFORMAT = "dd-MMM-yyyy";
    private static final String SIMPLE_DATE_FORMAT = "MM/dd/yyyy";
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat(SIMPLE_DATE_FORMAT);
    private String DATE_VALIDATION_MSG="Please enter a valid Date";

    boolean modified=false;
    boolean tableModified = false;
    private String reviewerId="0";
    private String personName;
    private char type; //used for checking ehther it is called from dialog
    private ComboBoxBean emptyComboBoxBean;
    
    private CoeusUtils coeusUtils = CoeusUtils.getInstance();
    
    /** Creates a new instance of IPReviewController */
    public IPReviewController(InstituteProposalBaseBean instituteProposalBaseBean,char functionType) {
        super(instituteProposalBaseBean);
        this.functionType=functionType;
        queryEngine = QueryEngine.getInstance();
        coeusMessageResources = CoeusMessageResources.getInstance();
        registerComponents();
//        getComboData();
        setMaxRowID();
        setFormData(instituteProposalBaseBean);
        showCommentMissingMessage();
        formatFields();
        setColumnData();
    }
    
    /**
     * Creates an instance of IP Reviewcontrioller.
     */    
    public IPReviewController(InstituteProposalBaseBean instituteProposalBaseBean) {
        super(instituteProposalBaseBean);
        this.instituteProposalBaseBean=instituteProposalBaseBean;
        queryEngine = QueryEngine.getInstance();
        coeusMessageResources = CoeusMessageResources.getInstance();
        type='M';
        registerComponents();
//        getComboData();
        iPReviewForm.txtDateSubmittedIP.requestFocusInWindow();
        setMaxRowID();
        
    }
    
    /**
     * REgisters the listeners and components
     */    
    public  void registerComponents(){
        iPReviewForm = new IPReviewForm();
        iPReviewTableModel = new IPReviewTableModel();
        iPReviewTableCellEditor = new IPReviewTableCellEditor();
        iPReviewTableCellRenderer = new IPReviewTableCellRenderer();
        iPReviewForm.tblActivity.setModel(iPReviewTableModel); 
        mdiForm=CoeusGuiConstants.getMDIForm();
        iPReviewForm.btnAdd.addActionListener(this);
        iPReviewForm.btnDelete.addActionListener(this);
        iPReviewForm.btnReviewerSearch.addActionListener(this);
        iPReviewForm.btnRemoveReviewer.addActionListener(this);
        CustomFocusAdapter customFocusAdapter=new CustomFocusAdapter();
        if (functionType!=DISPLAY_PROPOSAL){
            iPReviewForm.txtDateFromIP.addFocusListener(customFocusAdapter);
            iPReviewForm.txtDateSubmittedIP.addFocusListener(customFocusAdapter);
        }
        iPReviewForm.txtReviewer.addMouseListener(new CustomMouseAdapter());
    
        Component components[] = {iPReviewForm.txtDateSubmittedIP,iPReviewForm.cmbReviewRequirement,
        iPReviewForm.txtDateFromIP,iPReviewForm.cmbReviewResult,iPReviewForm.btnReviewerSearch,
        iPReviewForm.txtComment,iPReviewForm.txtIPReviewerComment,iPReviewForm.tblActivity,iPReviewForm.btnAdd,iPReviewForm.btnDelete};
        
        ScreenFocusTraversalPolicy  traversePolicy = new ScreenFocusTraversalPolicy( components );
        iPReviewForm.setFocusTraversalPolicy(traversePolicy);
        iPReviewForm.setFocusCycleRoot(true);
        if(functionType!= DISPLAY_PROPOSAL){
            addBeanUpdatedListener(this, InstituteProposalBean.class);
        }
        
        setTableKeyTraversal();
    }
    
    public void getComboData() {
        emptyComboBoxBean=new ComboBoxBean(EMPTY_STRING,EMPTY_STRING);
        try {
            cvReviewRequirementType = queryEngine.getDetails(queryKey,KeyConstants.IP_REVIEW_REQUIREMENT_TYPE);         
            cvReviewResultType = queryEngine.getDetails(queryKey,KeyConstants.IP_REVIEW_RESULT_TYPE);
            //Bug Fix : 2118 - Problem with IP review tab - START
            if (cvReviewRequirementType != null && cvReviewRequirementType.size() > 0){
                 cvReviewRequirementType.add(0, emptyComboBoxBean);
                 cvReviewResultType.add(0, emptyComboBoxBean);
             }
            //Bug Fix : 2118 - Problem with IP review tab - END
            iPReviewForm.cmbReviewRequirement.setModel(new DefaultComboBoxModel(cvReviewRequirementType));
            iPReviewForm.cmbReviewResult.setModel(new DefaultComboBoxModel(cvReviewResultType));
            
            if (functionType==DISPLAY_PROPOSAL){
                cvReviewRequirementType.add(emptyComboBoxBean);
                cvReviewResultType.add(emptyComboBoxBean);
           }
            cvActivityType = queryEngine.getDetails(queryKey,KeyConstants.IP_REVIEW_ACTIVITY_TYPE);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
   
    
    /**
     * This method is used to display the message that the comment  is missing. If the comment is missing it displays the message.
     */    
    public void showCommentMissingMessage() {
        if (!isCommentPresent) {
            if (functionType!=NEW_INST_PROPOSAL) {
                CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey("instPropBase_exceptionCode.1007"));
            }
        }
        if (!isIPReviewCommentPresent) {
            if (functionType!=NEW_INST_PROPOSAL) {
                CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey("instPropBase_exceptionCode.1008"));
            }
        }
    }    
     
    /**
     * Sets the data to the respective fields
     */    
    public void setFormData(Object instituteProposalBaseBean){
        this.instituteProposalBaseBean = (InstituteProposalBaseBean)instituteProposalBaseBean ;
        cvProposalData=new CoeusVector();
        cvIpTableData = new CoeusVector();
        cvComments = new CoeusVector();
        //cvActivityType = new CoeusVector();
        cvDeletedData = new CoeusVector();
        iPReviewForm.txtReviewer.setDisabledTextColor(Color.black);
        iPReviewForm.txtReviewer.setText(EMPTY_STRING);
        iPReviewForm.txtComment.setText(EMPTY_STRING);
        iPReviewForm.txtIPReviewerComment.setText(EMPTY_STRING);
        iPReviewForm.txtDateSubmittedIP.requestFocus();
        try {
        cvProposalData=queryEngine.executeQuery(queryKey,InstituteProposalBean.class, CoeusVector.FILTER_ACTIVE_BEANS);
        if(cvProposalData!= null && cvProposalData.size() > 0){
            instituteProposalBean=(InstituteProposalBean)cvProposalData.elementAt(0);
        }else{
            instituteProposalBean = new InstituteProposalBean();
        }
        if( cvReviewRequirementType == null || cvReviewRequirementType.size() == 0 ){
            getComboData();
        }
        personName = instituteProposalBean.getIpReviewerName();
        cvComments = queryEngine.executeQuery(queryKey,CoeusParameterBean.class,CoeusVector.FILTER_ACTIVE_BEANS);
        for (int index=0;index<cvComments.size();index++) {
            CoeusParameterBean coeusParameterBean=(CoeusParameterBean)cvComments.elementAt(index);
            if(CoeusConstants.PROPOSAL_IP_REVIEW_COMMENT_CODE.equals(coeusParameterBean.getParameterName())){
                isCommentPresent=true;
            }
            if(CoeusConstants.PROPOSAL_IP_REVIEWER_COMMENT_CODE.equals(coeusParameterBean.getParameterName())){
                isIPReviewCommentPresent=true;
            }
        }

        if (!isCommentPresent) {
                iPReviewForm.txtComment.setEditable(false);
                iPReviewForm.txtComment.setOpaque(false);
        }else {
            if (isCheckRight() && (functionType !=DISPLAY_PROPOSAL)) {
                iPReviewForm.txtComment.setEditable(true);
                iPReviewForm.txtComment.setOpaque(true);
                
            }
            cvCommentDescription=new CoeusVector();
            cvCommentDescription=queryEngine.getDetails(queryKey,InstituteProposalCommentsBean.class);                                
            if (cvCommentDescription!= null && cvCommentDescription.size()>0) {
                //CoeusVector return
                CoeusVector cvReviewCommentCode = cvComments.filter(new Equals("parameterName", CoeusConstants.PROPOSAL_IP_REVIEW_COMMENT_CODE));
                CoeusParameterBean coeusParameterBean = null;
                coeusParameterBean = (CoeusParameterBean)cvReviewCommentCode.elementAt(0);

                Equals equals = new Equals("commentCode", new Integer(coeusParameterBean.getParameterValue()));

                cvCommentDescription = cvCommentDescription.filter(equals);
                if(cvCommentDescription!=null && cvCommentDescription.size() > 0){
                    this.reviewCommentsBean=(InstituteProposalCommentsBean)cvCommentDescription.elementAt(0);
                    iPReviewForm.txtComment.setText(this.reviewCommentsBean.getComments());
                    iPReviewForm.txtComment.setCaretPosition(0);
                }
            }
        }
         
        if (!isIPReviewCommentPresent) {
                iPReviewForm.txtIPReviewerComment.setEditable(false);
                iPReviewForm.txtIPReviewerComment.setOpaque(false);
        } else {
                if (isCheckRight() && functionType != DISPLAY_PROPOSAL) {
                    iPReviewForm.txtIPReviewerComment.setEditable(true);
                    iPReviewForm.txtIPReviewerComment.setOpaque(true);
                }
                cvCommentDescription=new CoeusVector();
                cvCommentDescription=queryEngine.getDetails(queryKey,InstituteProposalCommentsBean.class);                                
                if (cvCommentDescription!= null && cvCommentDescription.size()>0) {
                    CoeusVector cvReviewerCommentCode = cvComments.filter(new Equals("parameterName", CoeusConstants.PROPOSAL_IP_REVIEWER_COMMENT_CODE));
                    CoeusParameterBean coeusParameterBean = null;
                    coeusParameterBean = (CoeusParameterBean)cvReviewerCommentCode.elementAt(0);

                    Equals equals = new Equals("commentCode", new Integer(coeusParameterBean.getParameterValue()));
                    cvCommentDescription = cvCommentDescription.filter(equals);
                    if(cvCommentDescription!=null && cvCommentDescription.size() > 0){
                        this.reviewerCommentsBean=(InstituteProposalCommentsBean)cvCommentDescription.elementAt(0);
                        iPReviewForm.txtIPReviewerComment.setText(this.reviewerCommentsBean.getComments());
                        iPReviewForm.txtIPReviewerComment.setCaretPosition(0);
                    }
                }
            }
        cvIpTableData = queryEngine.executeQuery(queryKey, InstituteProposalIPReviewActivityBean.class, CoeusVector.FILTER_ACTIVE_BEANS);
        if (cvIpTableData!=null && cvIpTableData.size()>0) {
            iPReviewTableModel.setData(cvIpTableData);
        }
//            iPReviewForm.cmbReviewRequirement.setModel(new DefaultComboBoxModel(cvReviewRequirementType));
//            iPReviewForm.cmbReviewResult.setModel(new DefaultComboBoxModel(cvReviewResultType));
        }catch(Exception e) {
            e.printStackTrace();
        }
        setDisplayData();
        
   }
    // set the focus when it is called from MDI form
    /**
     * Sets the focus to the component when the form loads
     */    
    public void setDefaultFocusForComponent(){
         // Bug Fix #935 - 14th June chandra - Start
        
          if(functionType != DISPLAY_PROPOSAL) {
            if(iPReviewForm.tblActivity.getRowCount() > 0 ) {
                iPReviewForm.tblActivity.requestFocusInWindow();
                
                int prevSelectedRow=iPReviewForm.tblActivity.getSelectedRow();
                if(prevSelectedRow!=-1){
                    iPReviewForm.tblActivity.setRowSelectionInterval(prevSelectedRow, prevSelectedRow);
                }
                else{
                    iPReviewForm.tblActivity.setRowSelectionInterval(0, 0);
                }
                iPReviewForm.tblActivity.setColumnSelectionInterval(1,1);
            }else{
                iPReviewForm.btnAdd.requestFocusInWindow();
            }            
        }
          // Bug Fix #935 - 14th June chandra - End
    }
    
    /**
     * This method is called from the IPDialogController. It calles this method using super. This method is used to set the focus inthe dialog when it opens
     */    
    public void setDialogFocus(){
        iPReviewForm.txtDateSubmittedIP.requestFocusInWindow();
        if(iPReviewForm.tblActivity.getRowCount()>0) {
           iPReviewForm.tblActivity.setRowSelectionInterval(0,0);
        }
    }

    /**
     * Sets the column properties for the table columns
     */    
    public void setColumnData(){
        JTableHeader tableHeader = iPReviewForm.tblActivity.getTableHeader();
        tableHeader.setReorderingAllowed(false);
        tableHeader.setFont(CoeusFontFactory.getLabelFont());
        iPReviewForm.tblActivity.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        iPReviewForm.tblActivity.setRowHeight(22);
        iPReviewForm.tblActivity.setSelectionBackground(java.awt.Color.yellow);
        iPReviewForm.tblActivity.setSelectionForeground(java.awt.Color.black);
        iPReviewForm.tblActivity.setShowHorizontalLines(false);
        iPReviewForm.tblActivity.setShowVerticalLines(false);
        iPReviewForm.tblActivity.setOpaque(false);
        iPReviewForm.tblActivity.setSelectionMode(
        DefaultListSelectionModel.SINGLE_SELECTION);
        
        
        TableColumn column = iPReviewForm.tblActivity.getColumnModel().getColumn(IPREVIEW_ACTIVITY_TYPE_COLUMN);
        column.setPreferredWidth(180);
        column.setResizable(true);
        column.setCellRenderer(iPReviewTableCellRenderer);
        column.setCellEditor(iPReviewTableCellEditor);
        
        column = iPReviewForm.tblActivity.getColumnModel().getColumn(ACTIVITY_DATE_COLUMN);
        column.setPreferredWidth(90);
        column.setResizable(true);
        column.setCellRenderer(iPReviewTableCellRenderer);
        column.setCellEditor(iPReviewTableCellEditor);
       
        column = iPReviewForm.tblActivity.getColumnModel().getColumn(COMMENTS_COLUMN);
        column.setPreferredWidth(365);
        column.setResizable(true);
        column.setCellRenderer(iPReviewTableCellRenderer);
        column.setCellEditor(iPReviewTableCellEditor);

       }    

  public void setDisplayData() {
        if (instituteProposalBean.getReviewSubmissionDate()!=null) {
            iPReviewForm.txtDateSubmittedIP.setText(dtUtils.formatDate(
                    Utils.convertNull(instituteProposalBean.
                        getReviewSubmissionDate().toString()), REQUIRED_DATEFORMAT));
        } else {
            iPReviewForm.txtDateSubmittedIP.setText(null);
        }  
        
        if (instituteProposalBean.getReviewReceiveDate()!=null) {
            iPReviewForm.txtDateFromIP.setText(dtUtils.formatDate(
                    Utils.convertNull(instituteProposalBean.
                        getReviewReceiveDate().toString()), REQUIRED_DATEFORMAT));
        } else {
            iPReviewForm.txtDateFromIP.setText(null);
        }
        if(instituteProposalBean.getIpReviewerName()!=null) {
            reviewerId=instituteProposalBean.getIpReviewer();
            iPReviewForm.txtReviewer.setText(instituteProposalBean.getIpReviewerName());
        }
        
       int typeCode=instituteProposalBean.getIpReviewRequestTypeCode();
       CoeusVector filteredVector;
//       if(typeCode>0) {
           filteredVector = cvReviewRequirementType.filter(new Equals("code",""+typeCode));
           if(filteredVector!=null && filteredVector.size() > 0){
                ComboBoxBean comboBoxBean = null;
                comboBoxBean = (ComboBoxBean)filteredVector.get(0);
                iPReviewForm.cmbReviewRequirement.setSelectedItem(comboBoxBean);
            }else{
                iPReviewForm.cmbReviewRequirement.setSelectedItem(emptyComboBoxBean);
            }
//       } 
//       else if (functionType==DISPLAY_PROPOSAL){
//           //iPReviewForm.cmbReviewRequirement.addItem(new ComboBoxBean(EMPTY_STRING, EMPTY_STRING));
//           iPReviewForm.cmbReviewRequirement.setSelectedItem(new ComboBoxBean("",""));
//       }
        filteredVector=null;
        typeCode=instituteProposalBean.getReviewResultCode();
//        if (typeCode>0) {
            filteredVector = cvReviewResultType.filter(new Equals("code",""+typeCode));
            if(filteredVector!=null && filteredVector.size() > 0){
                ComboBoxBean comboBoxBean = null;
                comboBoxBean = (ComboBoxBean)filteredVector.get(0);
                iPReviewForm.cmbReviewResult.setSelectedItem(comboBoxBean);
            }else{
                iPReviewForm.cmbReviewResult.setSelectedItem(new ComboBoxBean("",""));
            }      
//        }
//        else if(functionType==DISPLAY_PROPOSAL){
//            //iPReviewForm.cmbReviewResult.addItem(new ComboBoxBean(EMPTY_STRING, EMPTY_STRING));
//            iPReviewForm.cmbReviewResult.setSelectedItem(new ComboBoxBean(EMPTY_STRING, EMPTY_STRING));
//        }
 }
    
    
    public void display() {
    }
    
    /**
     * Perform field formatting.
     * enabling, disabling components depending on the
     * function type.
     */    
    public void formatFields() {
        iPReviewForm.txtReviewer.setEnabled(false);
        if (functionType == DISPLAY_PROPOSAL) {
            disableComponents();
        }
    }
    
    private void disableComponents() {
        iPReviewForm.btnAdd.setEnabled(false);
        iPReviewForm.btnDelete.setEnabled(false);
        iPReviewForm.btnRemoveReviewer.setEnabled(false);
        iPReviewForm.btnReviewerSearch.setEnabled(false);
        iPReviewForm.txtDateFromIP.setEditable(false);
        iPReviewForm.txtDateSubmittedIP.setEditable(false);
        //added for the bug fix:1032 for the grayed out fields in the display mode start
        iPReviewForm.txtDateFromIP.setOpaque(false);
        iPReviewForm.txtDateSubmittedIP.setOpaque(false);
        //bug fix end.
        iPReviewForm.txtReviewer.setEditable(false);
        iPReviewForm.txtReviewer.setOpaque(false);
        iPReviewForm.txtComment.setEditable(false);
        iPReviewForm.txtComment.setOpaque(false);
        iPReviewForm.txtIPReviewerComment.setEditable(false);
        iPReviewForm.txtIPReviewerComment.setOpaque(false);
        iPReviewForm.cmbReviewRequirement.setEnabled(false);
        iPReviewForm.cmbReviewResult.setEnabled(false);        
    }
    
    /**
     *
     * Returns the Component.
     */    
    public java.awt.Component getControlledUI() {
        return iPReviewForm;
    }
    
    /**
     * Returns form data
     */    
    public Object getFormData() {
        return null;
    }
    
    /**
     * Saves the data for the form
     */    
    public void saveFormData() {
        setBeanData();
        setAcTypes();
        //Fire Proposal Details Modified event
          BeanEvent beanEvent = new BeanEvent();
          beanEvent.setSource(this);
          beanEvent.setBean(instituteProposalBean);
          fireBeanUpdated(beanEvent); 
     }
    
    /**
     * Sets the bean with data
     */    
    public void setBeanData() {
        iPReviewTableCellEditor.stopCellEditing();
        //Resetting the flags
        modified=false;
        //tableModified=false;
        isCommentModified=false;
        
        Date modifiedDate;
        String dateValue;
        Date date;
        try{            
            /*dateValue = dtUtils.restoreDate(iPReviewForm.txtDateSubmittedIP.getText(), DATE_SEPARATERS);
            if( dateValue.trim().length() >0 && dateValue != null ){
              modifiedDate = dtFormat.parse(dateValue);
              instituteProposalBean.setReviewSubmissionDate(
              new java.sql.Date(modifiedDate.getTime()));
            } else {
                instituteProposalBean.setReviewSubmissionDate(null);
            }*/

            //Bijosh
            dateValue = iPReviewForm.txtDateSubmittedIP.getText().trim();
            if(! dateValue.equals(EMPTY_STRING)) {
                String strDate1 =  dtUtils.formatDate(dateValue, DATE_SEPARATERS, REQUIRED_DATEFORMAT);
                if(strDate1== null){
                      strDate1 =dtUtils.restoreDate(dateValue, DATE_SEPARATERS);
                 if( strDate1 == null || strDate1.equals(dateValue)) {
                     if(functionType== NEW_INST_PROPOSAL){
                            instituteProposalBean.setAcType(TypeConstants.INSERT_RECORD);
                        }else if(functionType== NEW_ENTRY_INST_PROPOSAL){
                         instituteProposalBean.setAcType(TypeConstants.INSERT_RECORD);
                        }else if(functionType==CORRECT_INST_PROPOSAL){
                            instituteProposalBean.setAcType(TypeConstants.UPDATE_RECORD);
                        }
                        instituteProposalBean.setReviewSubmissionDate(null);
                 }else{
                     date = simpleDateFormat.parse(dtUtils.restoreDate(dateValue,DATE_SEPARATERS));
                     instituteProposalBean.setReviewSubmissionDate(new java.sql.Date(date.getTime()));
                 }
                }else {
                    //date = simpleDateFormat.parse(dateValue);//dateUtils.restoreDate(strDate,DATE_SEPARATERS)
                    date = simpleDateFormat.parse(dtUtils.formatDate(dateValue,DATE_SEPARATERS, SIMPLE_DATE_FORMAT));
                    instituteProposalBean.setReviewSubmissionDate(new java.sql.Date(date.getTime()));
                }
            }else{
                instituteProposalBean.setReviewSubmissionDate(null);
            }
                   
            //Bijosh
            
            
            //From instfdetailcontroller
            /*
            dateValue = iPReviewForm.txtDateSubmittedIP.getText().trim();
            if(! dateValue.equals(EMPTY_STRING)) {
                String dateValue1 =  dtUtils.formatDate(dateValue, DATE_SEPARATERS, REQUIRED_DATEFORMAT);
                if(dateValue1 == null){
                    dateValue1 =dtUtils.restoreDate(dateValue, DATE_SEPARATERS);
                    if(dateValue1== null || dateValue1.equals(dateValue)){
                        instituteProposalBean.setReviewSubmissionDate(null);
                    }else{
                        date = simpleDateFormat.parse(dtUtils.restoreDate(dateValue,DATE_SEPARATERS));
                        instituteProposalBean.setReviewSubmissionDate(new java.sql.Date(date.getTime()));
                    }
                }else{
                    date = simpleDateFormat.parse(dateValue);
                    instituteProposalBean.setReviewSubmissionDate(new java.sql.Date(date.getTime()));
                }
            }else {
                 instituteProposalBean.setReviewSubmissionDate(null);
            }*/
            //From instdetailController
            //Bijosh1
            dateValue = iPReviewForm.txtDateFromIP.getText().trim();
            if(! dateValue.equals(EMPTY_STRING)) {
                String strDate1 =  dtUtils.formatDate(dateValue, DATE_SEPARATERS, REQUIRED_DATEFORMAT);
                if(strDate1== null){
                      strDate1 =dtUtils.restoreDate(dateValue, DATE_SEPARATERS);
                 if( strDate1 == null || strDate1.equals(dateValue)) {
                     if(functionType== NEW_INST_PROPOSAL){
                            instituteProposalBean.setAcType(TypeConstants.INSERT_RECORD);
                        }else if(functionType== NEW_ENTRY_INST_PROPOSAL){
                         instituteProposalBean.setAcType(TypeConstants.INSERT_RECORD);
                        }else if(functionType==CORRECT_INST_PROPOSAL){
                            instituteProposalBean.setAcType(TypeConstants.UPDATE_RECORD);
                        }
                            instituteProposalBean.setRequestStartDateTotal(null);
                 }else{
                     date = simpleDateFormat.parse(dtUtils.restoreDate(dateValue,DATE_SEPARATERS));
                     instituteProposalBean.setReviewReceiveDate(new java.sql.Date(date.getTime()));
                 }
                }else {
                    //date = simpleDateFormat.parse(dateValue);//dateUtils.restoreDate(strDate,DATE_SEPARATERS)
                    date = simpleDateFormat.parse(dtUtils.formatDate(dateValue,DATE_SEPARATERS, SIMPLE_DATE_FORMAT));
                    instituteProposalBean.setReviewReceiveDate(new java.sql.Date(date.getTime()));
                }
            }else{
                instituteProposalBean.setReviewReceiveDate(null);
            }
            //Bijosh1
            // For Date from IP
            /*dateValue = iPReviewForm.txtDateFromIP.getText().trim();
            if(! dateValue.equals(EMPTY_STRING)) {
                String dateValue1 =  dtUtils.formatDate(dateValue, DATE_SEPARATERS, REQUIRED_DATEFORMAT);
                if(dateValue1 == null){
                    dateValue1 =dtUtils.restoreDate(dateValue, DATE_SEPARATERS);
                    if(dateValue1== null || dateValue1.equals(dateValue)){
                        instituteProposalBean.setReviewReceiveDate(null);
                    }else{
                        date = simpleDateFormat.parse(dtUtils.restoreDate(dateValue,DATE_SEPARATERS));
                        instituteProposalBean.setReviewReceiveDate(new java.sql.Date(date.getTime()));
                    }
                }else{
                    date = simpleDateFormat.parse(dateValue);
                    instituteProposalBean.setReviewReceiveDate(new java.sql.Date(date.getTime()));
                }
            }else {
                instituteProposalBean.setReviewReceiveDate(null);
            }
            */
            
            // For Date from IP
            
            /*dateValue = dtUtils.restoreDate(iPReviewForm.txtDateFromIP.getText(), DATE_SEPARATERS);
            if( dateValue.trim().length() >0 && dateValue != null ){
              modifiedDate = dtFormat.parse(dateValue);
              instituteProposalBean.setReviewReceiveDate(
              new java.sql.Date(modifiedDate.getTime()));
            }else {
                instituteProposalBean.setReviewReceiveDate(null);
            }*/
             ComboBoxBean comboBoxReviewRqmtBean = (ComboBoxBean)iPReviewForm.cmbReviewRequirement.getSelectedItem();
            
            int typeCode;
            if(! comboBoxReviewRqmtBean.getCode().equals(EMPTY_STRING)) {
                typeCode = Integer.parseInt(comboBoxReviewRqmtBean.getCode().trim());
                instituteProposalBean.setIpReviewRequestTypeCode(typeCode);
            } else {
                instituteProposalBean.setIpReviewRequestTypeCode(0);
            }

            // Setting combo value 
            ComboBoxBean comboBoxBean = (ComboBoxBean)iPReviewForm.cmbReviewResult.getSelectedItem();
            if(! comboBoxBean.getCode().equals(EMPTY_STRING)) {
                typeCode = Integer.parseInt(comboBoxBean.getCode().trim());
                instituteProposalBean.setReviewResultCode(typeCode);
            } else {
                instituteProposalBean.setReviewResultCode(0);
            }
            if (iPReviewForm.txtReviewer.getText().equals(EMPTY_STRING)) {
                instituteProposalBean.setIpReviewer(null);
            }else {
                instituteProposalBean.setIpReviewer(reviewerId);
            }
            StrictEquals stBeanEquals = new StrictEquals(); 
            InstituteProposalBean qryBean=new InstituteProposalBean();
            CoeusVector cvOrginalData = queryEngine.getDetails(queryKey, InstituteProposalBean.class);
            qryBean=(InstituteProposalBean)cvOrginalData.elementAt(0);
            boolean isDataSame=stBeanEquals.compare(instituteProposalBean,qryBean);
            if (!isDataSame) {
                setModified(true);
            }
            StrictEquals stCommentsEquals = new StrictEquals();
            InstituteProposalCommentsBean queryCommentsBean = new InstituteProposalCommentsBean();
            CoeusVector cvTempComment = queryEngine.getDetails(queryKey, InstituteProposalCommentsBean.class);
            cvComments = queryEngine.executeQuery(queryKey,CoeusParameterBean.class,CoeusVector.FILTER_ACTIVE_BEANS);
            CoeusVector cvReviewComment = null;
            coeusParameterBean = null;
            CoeusVector cvReviewCommentCode = cvComments.filter(new Equals("parameterName", CoeusConstants.PROPOSAL_IP_REVIEW_COMMENT_CODE));
            if(cvReviewCommentCode!=null && cvReviewCommentCode.size() > 0){
                coeusParameterBean = (CoeusParameterBean)cvReviewCommentCode.elementAt(0);
            }
            if (cvTempComment!= null && cvTempComment.size()>0) {
                if(coeusParameterBean!=null){
                    Equals equals = new Equals("commentCode", new Integer(coeusParameterBean.getParameterValue()));

                    cvReviewComment = cvTempComment.filter(equals);
                    if(cvReviewComment!=null && cvReviewComment.size() > 0){
                        queryCommentsBean = (InstituteProposalCommentsBean)cvReviewComment.elementAt(0);                            
                    }
                }
            }
            
            //Bijosh
            if (coeusParameterBean!=null){
                if(reviewCommentsBean!= null){
                    reviewCommentsBean.setComments(iPReviewForm.txtComment.getText());
                    isReviewCommentSame=stCommentsEquals.compare(reviewCommentsBean, queryCommentsBean);
                    if(! isReviewCommentSame){
                        //Data Changed. save to query Engine.
                        reviewCommentsBean.setAcType(TypeConstants.UPDATE_RECORD);
                        queryEngine.update(queryKey, reviewCommentsBean);
                    }
                }else {
                    if (!EMPTY_STRING.equals(iPReviewForm.txtComment.getText().trim())) {
                        reviewCommentsBean = new InstituteProposalCommentsBean();
                        reviewCommentsBean.setProposalNumber( this.instituteProposalBaseBean.getProposalNumber());
                        reviewCommentsBean.setSequenceNumber( this.instituteProposalBaseBean.getSequenceNumber());
                        reviewCommentsBean.setCommentCode(Integer.parseInt(coeusParameterBean.getParameterValue()));
                        reviewCommentsBean.setComments(iPReviewForm. txtComment.getText());
                        queryEngine.insert(queryKey,reviewCommentsBean);
                        
                    }
                }
            }
            //Bijosh
             /*if(reviewerCommentsBean!= null){
                reviewerCommentsBean.setComments(iPReviewForm.txtIPReviewerComment.getText());
            }else{
                reviewerCommentsBean = new InstituteProposalCommentsBean();
                reviewerCommentsBean.setProposalNumber(instituteProposalBean.getProposalNumber());
                reviewerCommentsBean.setSequenceNumber(instituteProposalBean.getSequenceNumber());
                reviewerCommentsBean.setComments(iPReviewForm.txtIPReviewerComment.getText());
            }*/ //Bijosh commented 
            
            queryCommentsBean = new InstituteProposalCommentsBean();
            cvTempComment=new CoeusVector();
            cvTempComment = queryEngine.getDetails(queryKey, InstituteProposalCommentsBean.class);
            cvComments = queryEngine.executeQuery(queryKey,CoeusParameterBean.class,CoeusVector.FILTER_ACTIVE_BEANS);
            cvReviewerComment = null;
            coeusReviewerParameterBean = null;
            CoeusVector cvReviewerCommentCode = cvComments.filter(new Equals("parameterName", CoeusConstants.PROPOSAL_IP_REVIEWER_COMMENT_CODE));
            if(cvReviewerCommentCode!=null && cvReviewerCommentCode.size() > 0){
                coeusReviewerParameterBean = (CoeusParameterBean)cvReviewerCommentCode.elementAt(0);
            }
            if (cvTempComment!= null && cvTempComment.size()>0) {
                if(coeusReviewerParameterBean!=null){
                    Equals equals = new Equals("commentCode", new Integer(coeusReviewerParameterBean.getParameterValue()));

                    cvReviewerComment = cvTempComment.filter(equals);
                    if(cvReviewerComment!=null && cvReviewerComment.size() > 0){
                        queryCommentsBean = (InstituteProposalCommentsBean)cvReviewerComment.elementAt(0);                            
                    }
                }
            }
            if(coeusReviewerParameterBean!=null) {
                if(reviewerCommentsBean!= null){
                    reviewerCommentsBean.setComments(iPReviewForm.txtIPReviewerComment.getText());
                    isReviewerCommentSame=stCommentsEquals.compare(reviewerCommentsBean, queryCommentsBean);
                    if(! isReviewerCommentSame){
                        //Data Changed. save to query Engine.
                        reviewerCommentsBean.setAcType(TypeConstants.UPDATE_RECORD);
                        queryEngine.update(queryKey, reviewerCommentsBean);
                    }
                    //isReviewerCommentSame=stCommentsEquals.compare(reviewerCommentsBean, queryCommentsBean);
                }else {
                    if (!EMPTY_STRING.equals(iPReviewForm.txtIPReviewerComment.getText())) {
                        reviewerCommentsBean = new InstituteProposalCommentsBean();
                        reviewerCommentsBean.setProposalNumber( this.instituteProposalBaseBean.getProposalNumber());
                        reviewerCommentsBean.setSequenceNumber( this.instituteProposalBaseBean.getSequenceNumber());
                        reviewerCommentsBean.setCommentCode(Integer.parseInt(coeusReviewerParameterBean.getParameterValue()));
                        reviewerCommentsBean.setComments(iPReviewForm. txtComment.getText());
                        reviewerCommentsBean.setAcType(TypeConstants.INSERT_RECORD);
                        queryEngine.insert(queryKey,reviewerCommentsBean);
                    }
                }    
            }
            if (isReviewCommentSame && isReviewerCommentSame) {
                isCommentModified=false;
            } else {
                isCommentModified=true;
            }
        } catch(Exception e){
            e.printStackTrace();
        }         
    }
    
    /**
     * Sets the Ac types
     */    
    public void setAcTypes() {
        try{
            if (isModified()) {
                if(functionType == CORRECT_INST_PROPOSAL) {
                    instituteProposalBean.setAcType(TypeConstants.UPDATE_RECORD);
                    queryEngine.update(queryKey, instituteProposalBean);
                }else{
                    instituteProposalBean.setAcType(TypeConstants.INSERT_RECORD);
                    queryEngine.insert(queryKey, instituteProposalBean);
                }
            }
            CoeusVector dataObject = new CoeusVector();
            if(tableModified){
                if(cvDeletedData!= null && cvDeletedData.size() >0){
                     dataObject.addAll(cvDeletedData);
                }
                if(cvIpTableData!= null && cvIpTableData.size() >0){
                     dataObject.addAll(cvIpTableData);
                }
                if(dataObject!=null) {
                    for(int index = 0; index < dataObject.size(); index++){
                        InstituteProposalIPReviewActivityBean bean = (InstituteProposalIPReviewActivityBean) dataObject.get(index);
                        if(bean.getAcType()!= null){
                            if(bean.getAcType().equals(TypeConstants.UPDATE_RECORD)){
                                //First delete the existing data and then insert the same. This is
                                //required since primary keys can be modified
                                bean.setAcType(TypeConstants.DELETE_RECORD);
                                queryEngine.delete(queryKey, bean);
                                bean.setAcType(TypeConstants.INSERT_RECORD);
                                bean.setRowId(rowID++);
                                queryEngine.insert(queryKey, bean);
                                // When the Child record is changed, parent should also notified
                                // coz the instituteProposalBean has to notify the activity bean
                                instituteProposalBean.setAcType(TypeConstants.UPDATE_RECORD);
                                queryEngine.update(queryKey, instituteProposalBean);
                            }else if(bean.getAcType().equals(TypeConstants.DELETE_RECORD)){
                                bean.setAcType(TypeConstants.DELETE_RECORD);
                                queryEngine.delete(queryKey, bean);
                            }else if(bean.getAcType().equals(TypeConstants.INSERT_RECORD)){
                                bean.setAcType(TypeConstants.INSERT_RECORD);
                                queryEngine.insert(queryKey, bean);
                            }
                        }
                    }
                }
            }
            //For comment updation
            /*
            if(reviewCommentsBean!= null){
                 if(! isReviewCommentSame){
                    //Data Changed. save to query Engine.
                     if(cvReviewComment==null || cvReviewComment.size() == 0){
                         if(coeusParameterBean != null){
                            reviewCommentsBean.setAcType(TypeConstants.INSERT_RECORD);
                            reviewCommentsBean.setCommentCode(Integer.parseInt(coeusParameterBean.getParameterValue()));
                            queryEngine.insert(queryKey, reviewCommentsBean);
                         }
                     }else{
                        reviewCommentsBean.setAcType(TypeConstants.UPDATE_RECORD);
                        queryEngine.update(queryKey, reviewCommentsBean);
                     }
                 }
                            
                 
            }
            
            //For reviewer comment
            if(reviewerCommentsBean!= null){
                 if(! isReviewerCommentSame){
                    //Data Changed. save to query Engine.
                     if(cvReviewerComment==null || cvReviewerComment.size() == 0){
                         if(coeusReviewerParameterBean != null){
                            reviewerCommentsBean.setAcType(TypeConstants.INSERT_RECORD);
                            reviewerCommentsBean.setCommentCode(Integer.parseInt(coeusReviewerParameterBean.getParameterValue()));
                            queryEngine.insert(queryKey, reviewerCommentsBean);
                         }
                     }else{
                        reviewerCommentsBean.setAcType(TypeConstants.UPDATE_RECORD);
                        queryEngine.update(queryKey, reviewerCommentsBean);
                     }
                 }
            }
            
            */
            //Comment updation ends
            
        }catch(Exception e) {
            e.printStackTrace();
        }        
    }
    /**
     * Fires when bean is updated. Applys for InstituteProposalBean
     */    
    public void beanUpdated(BeanEvent beanEvent) {
        if(beanEvent.getBean().getClass().equals(InstituteProposalBean.class)) {
            instituteProposalBean = (InstituteProposalBean)beanEvent.getBean();
            setRefreshRequired(true);
        }
    }
    
    
    /**
     * validate the form data/Form and returns true if
     * validation is through else returns false.
     */    
    public boolean validate() throws edu.mit.coeus.exception.CoeusUIException {
        String strDate = iPReviewForm.txtDateSubmittedIP.getText().trim();
        if(!strDate.equals(EMPTY_STRING)) {
            String strDate1 = dtUtils.formatDate(strDate, DATE_SEPARATERS, REQUIRED_DATEFORMAT);
            if(strDate1 == null) {
                strDate1 = dtUtils.restoreDate(strDate, DATE_SEPARATERS);
                if( strDate1 == null || strDate1.equals(strDate)) {
                    CoeusOptionPane.showInfoDialog(DATE_VALIDATION_MSG);
                    //coeusMessageResources.parseMessageKey("Please enter deadLine date"));
                    iPReviewForm.txtDateSubmittedIP.requestFocusInWindow();
                    return false;
                }
            }else {
                strDate = strDate1;
                iPReviewForm.txtDateSubmittedIP.setText(strDate);
            }
        }
        
         strDate = iPReviewForm.txtDateFromIP.getText().trim();
        if(!strDate.equals(EMPTY_STRING)) {
            String strDate1 = dtUtils.formatDate(strDate, DATE_SEPARATERS, REQUIRED_DATEFORMAT);
            if(strDate1 == null) {
                strDate1 = dtUtils.restoreDate(strDate, DATE_SEPARATERS);
                if( strDate1 == null || strDate1.equals(strDate)) {
                    CoeusOptionPane.showInfoDialog(DATE_VALIDATION_MSG);
                    //coeusMessageResources.parseMessageKey("Please enter deadLine date"));
                    iPReviewForm.txtDateFromIP.requestFocusInWindow();
                    return false;
                }
            }else {
                strDate = strDate1;
                iPReviewForm.txtDateFromIP.setText(strDate);
            }
        }

        for (int index=0;index<cvIpTableData.size();index++) {
            InstituteProposalIPReviewActivityBean instituteProposalIPReviewActivityBean = ( InstituteProposalIPReviewActivityBean )
            cvIpTableData.get(index);
            if (instituteProposalIPReviewActivityBean.getIpReviewActivityTypeCode()==0) {
                CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey("instPropIPReview_exceptionCode.1356")+(index+1));
                return false;
            }
        }
        return true;
    }
    
    /**
     * Setter for property refreshRequired of super
     */    
    public void setRefreshRequired(boolean refreshRequired) {
        super.setRefreshRequired(refreshRequired);
    }
    
    /**
     * Getter for property refreshRequired
     */    
    public boolean isRefreshRequired() {
        boolean retValue;
        retValue = super.isRefreshRequired();
        return retValue;
    }
    
    /**
     * To refresh the GUI with new Data
     */    
    public void refresh() {
        if (isRefreshRequired()) {
            setFormData(instituteProposalBaseBean);
            //cvDeletedData.clear();
            setRefreshRequired(false);
        }
    }    
    /* This method sets the maximum Row ID from the vector of 
     *that is present in queryEngine
     */
    private void setMaxRowID() {
        CoeusVector cvIPReview = new CoeusVector();
        InstituteProposalIPReviewActivityBean instituteProposalIPReviewActivityBean;
        try {
            cvIPReview = queryEngine.getDetails(queryKey, 
                InstituteProposalIPReviewActivityBean.class);
            if (cvIPReview != null && cvIPReview.size() > 0) {
                    cvIPReview.sort("rowId", false);
                    instituteProposalIPReviewActivityBean = (InstituteProposalIPReviewActivityBean) cvIPReview.get(0);
                    rowID = instituteProposalIPReviewActivityBean.getRowId() + 1;
            }
        } catch(Exception exception) {
            exception.printStackTrace();
        }
    }
    
    /**
     * Handler for the buttons 
     */    
    public void actionPerformed(ActionEvent ae) {
        Object source=ae.getSource();
        if (source==iPReviewForm.btnReviewerSearch) {
            searchAddressActionPerformed(ae);
        } else if (source==iPReviewForm.btnRemoveReviewer) {
            removeAddressActionPerformed(ae);
        } else if (source==iPReviewForm.btnAdd) {
            performAddAction();
        } else if (source==iPReviewForm.btnDelete) {
            performDeleteAction();
        }
    }
    
    /**
     * Performs search operation and sets the selected person name to the textbox
     */  
    
    private void searchAddressActionPerformed(java.awt.event.ActionEvent evt) {
            try {
            CoeusSearch coeusSearch = new CoeusSearch(
                        mdiForm, "PERSONSEARCH", 1);
            coeusSearch.showSearchWindow();
            HashMap personSelected = coeusSearch.getSelectedRow();
            if (personSelected != null && !personSelected.isEmpty() ) {
                
                String fullName = Utils.convertNull(personSelected.get(
                                                        "FULL_NAME"));
                personName=fullName;
                reviewerId=Utils.convertNull(personSelected.get(
                                                    "PERSON_ID"));
                
                //Bug Fix: Pass the person id to get the person details Start 1
                instituteProposalBean.setIpReviewer(reviewerId);
                //Bug Fix: Pass the person id to get the person details End 1
                
                if (fullName.length() > 0) {
                    iPReviewForm.txtReviewer.setText(Utils.convertNull(fullName));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }    
    
     /**
     * Performs remove operation on the reviewer.
     */  
    private void removeAddressActionPerformed(java.awt.event.ActionEvent evt) {
        if (EMPTY_STRING.equals(iPReviewForm.txtReviewer.getText()))
            return;
        if (iPReviewForm.txtReviewer.getText() != null ) {        
        String msg = coeusMessageResources.parseMessageKey("instPropIPReview_exceptionCode.1355")+"\n" +
                iPReviewForm.txtReviewer.getText();
            int confirm = CoeusOptionPane.showQuestionDialog(msg,
            CoeusOptionPane.OPTION_YES_NO,
            CoeusOptionPane.DEFAULT_YES);

            switch(confirm){
                case(JOptionPane.YES_OPTION):
                    try{
                        iPReviewForm.txtReviewer.setText("");
                        reviewerId = "0";
                    }catch(Exception ex){
                        ex.printStackTrace();
                        
                    }
                    setModified(true);
                    //modified=true;
                    break;
            }        
        }
        
    }

    /* To handle Add button actioon
     */
    private void performAddAction() {
        iPReviewTableCellEditor.stopCellEditing();
        /*String formatedDate = dtUtils.formatDate(ipActivityDate.toString(),SIMPLE_DATE_FORMAT);
        long date =  new java.util.Date(formatedDate).getTime();
        java.sql.Date activityDate = new java.sql.Date(date);*/
        
        InstituteProposalIPReviewActivityBean newBean = new InstituteProposalIPReviewActivityBean();
        
        newBean.setProposalNumber(instituteProposalBaseBean.getProposalNumber());
        newBean.setSequenceNumber(instituteProposalBaseBean.getSequenceNumber());
        newBean.setIpReviewActivityTypeCode(0);
        newBean.setActivityDate(null);
        newBean.setComments(EMPTY_STRING);
        newBean.setRowId(rowID++);
        newBean.setAcType(TypeConstants.INSERT_RECORD);
        //setModified(true);
        tableModified=true;
        //modified= true;
        cvIpTableData.add(newBean);
        iPReviewTableModel.fireTableRowsInserted(iPReviewTableModel.getRowCount() + 1, iPReviewTableModel.getRowCount() + 1);
        int lastRow = iPReviewForm.tblActivity.getRowCount()-1;
        if(lastRow >= 0){
            // Modified by chandra - to get the focus properly on the respective cells - start 15th June
            iPReviewForm.tblActivity.setRowSelectionInterval( lastRow, lastRow );
            iPReviewForm.tblActivity.setColumnSelectionInterval(0,0);
            iPReviewForm.tblActivity.scrollRectToVisible(
            iPReviewForm.tblActivity.getCellRect(
            lastRow ,0, true));
        }
        iPReviewForm.tblActivity.editCellAt(lastRow,IPREVIEW_ACTIVITY_TYPE_COLUMN);
        iPReviewForm.tblActivity.getEditorComponent().requestFocusInWindow();
        // Modified by chandra - to get the focus properly on the respective cells - End
    }
    

    /** To Handle Delete button action
     */    
    private void performDeleteAction(){
        iPReviewTableCellEditor.stopCellEditing();
        int rowIndex = iPReviewForm.tblActivity.getSelectedRow();
        if (rowIndex==-1) {
            CoeusOptionPane.showErrorDialog("Please select a Row");
            return;
        }
        if(rowIndex != -1 && rowIndex >= 0){
            String mesg = DELETE_CONFIRMATION;
            int selectedOption = CoeusOptionPane.showQuestionDialog(
            coeusMessageResources.parseMessageKey(mesg),
            CoeusOptionPane.OPTION_YES_NO,
            CoeusOptionPane.DEFAULT_YES);
            if(selectedOption == CoeusOptionPane.SELECTION_YES) {
                InstituteProposalIPReviewActivityBean deletedIPReviewActivityBean = 
                (InstituteProposalIPReviewActivityBean)cvIpTableData.get(rowIndex);
                cvDeletedData.add(deletedIPReviewActivityBean);
                if (deletedIPReviewActivityBean.getAcType() == null ||
                deletedIPReviewActivityBean.getAcType().equals(TypeConstants.UPDATE_RECORD)) {
                    cvDeletedData.add(deletedIPReviewActivityBean);
                }
                if(cvIpTableData!=null && cvIpTableData.size() > 0){
                    cvIpTableData.remove(rowIndex);
                    iPReviewTableModel.fireTableRowsDeleted(rowIndex, rowIndex);
                    //setModified(true);
                    tableModified = true;
                    deletedIPReviewActivityBean.setAcType(TypeConstants.DELETE_RECORD);
                }
                if(rowIndex >0){
                    iPReviewForm.tblActivity.setRowSelectionInterval(
                    rowIndex-1,rowIndex-1);
                    iPReviewForm.tblActivity.scrollRectToVisible(
                    iPReviewForm.tblActivity.getCellRect(
                    rowIndex-1 ,0, true));
                }else{
                    if(iPReviewForm.tblActivity.getRowCount()>0){
                        iPReviewForm.tblActivity.setRowSelectionInterval(0,0);
                    }
                }
            }
        }
    }
    /**
     * Getter for property modified.
     * @return Value of property modified.
     */
    public boolean isModified() {
        return this.modified;
    }
    
    /**
     * Setter for property modified.
     * @param modified New value of property modified.
     */
    public void setModified(boolean modified) {
        this.modified = modified;
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
    
/** Supporting method which will be used for the focus lost for date 
     *fields. This will be fired when the request focus for the specified 
     *date field is invoked
     */
     private void setRequestFocusInThread(final java.awt.Component component) {
        SwingUtilities.invokeLater( new Runnable() {
            public void run() {
                component.requestFocusInWindow();
            }
        });
    }
    
     /**
      * Getter for property isCommentModified.
      * @return Value of property isCommentModified.
      */
     public boolean isCommentModified() {
         return isCommentModified;
     }     
     /**
      * Setter for property isCommentModified.
      * @param isCommentModified New value of property isCommentModified.
      */
     public void setCommentModified(boolean isCommentModified) {
         this.isCommentModified = isCommentModified;
     }
     
     /** Getter for property checkRight.
      * @return Value of property checkRight.
      *
      */
     public boolean isCheckRight() {
         return checkRight;
     }
     
     /** Setter for property checkRight.
      * @param checkRight New value of property checkRight.
      *
      */
     public void setCheckRight(boolean checkRight) {
         this.checkRight = checkRight;
        if(!this.checkRight){
            disableComponents();
        }
    }
     
/** This is an inner class which is used to set the focus settings
  */   
     public class  CustomFocusAdapter extends FocusAdapter {
         public void focusGained(FocusEvent focusEvent) {
             if (focusEvent.isTemporary()) {
                 return ;
             }
             //sets the format of date to mm/dd/yy on focus
             Object source=focusEvent.getSource();
             if (source.equals(iPReviewForm.txtDateFromIP) || source.equals(iPReviewForm.txtDateSubmittedIP)){
                 String strDate = ((JTextField)source).getText().trim();
                 strDate = dtUtils.restoreDate(((JTextField)source).getText(), DATE_SEPARATERS);
                 ((JTextField)source).setText(strDate);
             }
         }
         
         public void focusLost(FocusEvent focusEvent) {
             try {
                 if (focusEvent.isTemporary()) {
                     return ;
                 }
                 //sets the format of date to dd-mmm-yyyy when focus is lost
                 Object source=focusEvent.getSource();
                 if ( source.equals(iPReviewForm.txtDateFromIP) || source.equals(iPReviewForm.txtDateSubmittedIP)) {
                     JTextField txtDateField=((JTextField)source);
                     String mailDate;
                     mailDate = txtDateField.getText().trim();
                     /** Bug fix # 1042.
                      *Added by chandra 14th July 2004
                      */
                     if(!mailDate.equals(EMPTY_STRING)) {
                         String strDate1 = dtUtils.formatDate(mailDate, DATE_SEPARATERS, REQUIRED_DATEFORMAT);
                         if(strDate1 == null) {
                             strDate1 = dtUtils.restoreDate(mailDate, DATE_SEPARATERS);
                             if( strDate1 == null || strDate1.equals(mailDate)) {
                                 CoeusOptionPane.showInfoDialog(DATE_VALIDATION_MSG);
                                 setRequestFocusInThread(txtDateField);
                                 return ;
                             }
                         }else {
                             mailDate = strDate1;
                             txtDateField.setText(mailDate);
                         }
                     }
                 }// End chandra - 14th July 2004
                 
             }catch(Exception exception){
                 exception.printStackTrace();
             }
         }
     }

/**
 * Mouse adapter class which handles double clicks on revewer text
 */
     public class CustomMouseAdapter extends MouseAdapter {
         public void mouseClicked(MouseEvent me){
             if(iPReviewForm.txtReviewer.getText().equals(EMPTY_STRING)) {
                 return;
             }
             if( me.getClickCount()== 2 ) {
                 String loginUserName = CoeusGuiConstants.getMDIForm().getUserName();
                 try{

                     //Bug Fix: Pass the person id to get the person details Start 2
                     /*Bug Fix:to get the person details with the person id instead of the person name*/
                     //PersonInfoFormBean PersonInfoFormBean = (PersonInfoFormBean)coeusUtils.getPersonInfoID(personName);
                     new PersonDetailForm(instituteProposalBean.getIpReviewer(),loginUserName,PERSON_DISPLAY_MODE);
                     //Bug Fix: Pass the person id to get the person details End 2                                          
                     
                 }catch ( Exception e) {
                     CoeusOptionPane.showInfoDialog( e.getMessage() );
                 }
             }
             
         }
     }
     
    /**
     * Table model class for Activity table
     */
     public class IPReviewTableModel extends AbstractTableModel {
         private String colName[] = {"IP Review Activity Type","Activity Date","Comments"};
         private Class colClass[] = {String.class, String.class, String.class};
         
         public  Class getColumnClass(int col){
             return colClass[col];
         }
         public boolean isCellEditable(int row, int col) {
             if (functionType == DISPLAY_PROPOSAL) {
                 return false;
             }
             if (type!='M' && !isCheckRight()) {
                 return false;
             }
                 return true;
         }
         public int getColumnCount() {
             return colName.length;
         }
         
         public int getRowCount() {
             return (cvIpTableData == null)?0:cvIpTableData.size();
         }
         
         public void setData(CoeusVector cvIpTableData){
             cvIpTableData = cvIpTableData;
         }
         
         public Object getValueAt(int rowIndex, int columnIndex) {
             InstituteProposalIPReviewActivityBean instituteProposalIPReviewActivityBean=
             (InstituteProposalIPReviewActivityBean)cvIpTableData.elementAt(rowIndex);
             switch (columnIndex) {
                 case IPREVIEW_ACTIVITY_TYPE_COLUMN:
                     int typeCode=instituteProposalIPReviewActivityBean.getIpReviewActivityTypeCode();
                     CoeusVector filteredVector = cvActivityType.filter(new Equals("code",""+typeCode));
                     if(filteredVector!=null && filteredVector.size() > 0){
                         ComboBoxBean comboBoxBean = null;
                         comboBoxBean = (ComboBoxBean)filteredVector.get(0);
                         return comboBoxBean;
                     }else{
                         return new ComboBoxBean("","");
                     }
                 case ACTIVITY_DATE_COLUMN:
                     if (instituteProposalIPReviewActivityBean.getActivityDate()!=null) {
                         return instituteProposalIPReviewActivityBean.getActivityDate();
                     }
                     return EMPTY_STRING;
                 case COMMENTS_COLUMN:
                     if (instituteProposalIPReviewActivityBean.getComments()!=null) {
                         return instituteProposalIPReviewActivityBean.getComments();
                     }
                     else {
                         return EMPTY_STRING;
                     }
             }
             return EMPTY_STRING;
         }
         public void setValueAt(Object value, int row, int col){
             InstituteProposalIPReviewActivityBean instituteProposalIPReviewActivityBean=
             (InstituteProposalIPReviewActivityBean)cvIpTableData.elementAt(row);
             String message=null;
             Date date = null;
             String activityDate=null;
             switch (col) {
                 case IPREVIEW_ACTIVITY_TYPE_COLUMN:
                     ComboBoxBean comboBoxBean = (ComboBoxBean)cvActivityType.filter(new Equals("description", value.toString())).get(0);
                     int typeCode = Integer.parseInt(comboBoxBean.getCode());
                     if( typeCode != instituteProposalIPReviewActivityBean.getIpReviewActivityTypeCode()){
                         instituteProposalIPReviewActivityBean.setIpReviewActivityTypeCode(typeCode);
                         tableModified = true;
                     }
                     break;
                 case ACTIVITY_DATE_COLUMN:
                     try{
                         if (value.toString().trim().length() > 0) {
                             activityDate = dtUtils.formatDate(
                             value.toString().trim(), DATE_SEPARATERS, REQUIRED_DATEFORMAT);
                         } else {
                             instituteProposalIPReviewActivityBean.setActivityDate(null);
                             return;
                         }
                         activityDate = dtUtils.restoreDate(activityDate, DATE_SEPARATERS);
                         if(activityDate==null) {
                             throw new CoeusException();
                         }
                         date = dtFormat.parse(activityDate.trim());
                         if(instituteProposalIPReviewActivityBean.getActivityDate()==null) {
                             setModified(true);
                             //modified=true;
                         } else if (instituteProposalIPReviewActivityBean.getActivityDate().equals(new java.sql.Date(date.getTime()))) {
                             break;
                         }
                     }catch (ParseException parseException) {
                         parseException.printStackTrace();
                         message = "Invalid date";
                         CoeusOptionPane.showErrorDialog(coeusMessageResources.parseMessageKey("instPropIPReview_exceptionCode.1354"));
                         return ;
                     }
                     catch (CoeusException coeusException) {
                         message = "Invalid date";
                         CoeusOptionPane.showErrorDialog(coeusMessageResources.parseMessageKey("instPropIPReview_exceptionCode.1354"));
                         return ;
                     }
                     instituteProposalIPReviewActivityBean.setActivityDate(
                     new java.sql.Date(date.getTime()));
                     tableModified = true;
                     break;
                 case COMMENTS_COLUMN:
                     
                     if (value==null) {
                         if(instituteProposalIPReviewActivityBean.getComments()!=null) {
                             instituteProposalIPReviewActivityBean.setComments(EMPTY_STRING);
                             tableModified=true;
                             
                         }
                         break;
                     }
                     if(instituteProposalIPReviewActivityBean.getComments()==null)  {
                         instituteProposalIPReviewActivityBean.setComments(value.toString());
                         tableModified=true;
                     }
                     if (!value.toString().trim().equals(instituteProposalIPReviewActivityBean.getComments().trim())) {
                         instituteProposalIPReviewActivityBean.setComments(value.toString());
                         tableModified = true;
                     }
                     break;
             }
             //to set ACType in bean
             if(instituteProposalIPReviewActivityBean.getAcType()== null){
                 instituteProposalIPReviewActivityBean.setAcType(TypeConstants.UPDATE_RECORD);
             }
         }
         public String getColumnName(int col){
             return colName[col];
         }
     }
    /**
     * Table cell Editor class for Activity table
     */
    public class IPReviewTableCellEditor extends AbstractCellEditor implements TableCellEditor{
        private CoeusComboBox cmbReviewType;
        private CoeusTextField txtComponent;
        private CoeusTextField txtComment;
        private int column;        
        public IPReviewTableCellEditor() {
            cmbReviewType=new CoeusComboBox();
            txtComponent=new CoeusTextField();
            txtComment = new CoeusTextField();
            txtComment.setDocument(new LimitedPlainDocument(150));
        }
        private void populateActivityTypeCombo () {
           int size = cvActivityType.size();
            ComboBoxBean comboBoxBean;
            for(int index = 0; index < size; index++) {
                comboBoxBean = (ComboBoxBean)cvActivityType.get(index);
                cmbReviewType.addItem(comboBoxBean);
            }            
        }
        
        public java.awt.Component getTableCellEditorComponent(javax.swing.JTable table,
        Object value, boolean isSelected, int row, int column) {
            this.column = column;
            switch(column){
                case IPREVIEW_ACTIVITY_TYPE_COLUMN:
                    if(! activityTypeComboPopulated) {
                        populateActivityTypeCombo();
                        activityTypeComboPopulated = true;
                    }
                    if (isSelected) {
                        cmbReviewType.setBackground(java.awt.Color.yellow);
                    }
                    else {
                        cmbReviewType.setBackground(java.awt.Color.white);
                    }
                    cmbReviewType.setSelectedItem(value);
                    return cmbReviewType;
                case ACTIVITY_DATE_COLUMN:
                    if (isSelected) {
                        txtComponent.setBackground(java.awt.Color.yellow);
                    }else {
                        txtComponent.setBackground(java.awt.Color.white);
                    }
                    if(value == null || value.toString().equals(EMPTY_STRING)) {
                        txtComponent.setText(EMPTY_STRING);
                        return txtComponent;
                    }
                    txtComponent.setText(dtUtils.formatDate(value.toString(),SIMPLE_DATE_FORMAT));
                    return txtComponent;
                case COMMENTS_COLUMN:
                    if (isSelected) {
                        txtComment.setBackground(java.awt.Color.yellow);
                    }else {
                        txtComment.setBackground(java.awt.Color.white);
                    }
                    txtComment.setText(value.toString());
                    return txtComment;
            }
            return txtComponent;
        }
        
        public Object getCellEditorValue() {
            switch(column) {
                case IPREVIEW_ACTIVITY_TYPE_COLUMN:
                    return cmbReviewType.getSelectedItem();
                case ACTIVITY_DATE_COLUMN:
                   return txtComponent.getText();
                case COMMENTS_COLUMN:
                    return txtComment.getText();
            }
            return txtComponent.getText();
        }
        
    }
    /**
     * Table Renderer class for Activity table
     */
    public class IPReviewTableCellRenderer extends DefaultTableCellRenderer{
        private CoeusTextField txtComponent;
        
        public IPReviewTableCellRenderer(){
            txtComponent = new CoeusTextField();
            txtComponent.setBorder(new javax.swing.border.EmptyBorder(0,0,0,0));
        }
        public java.awt.Component getTableCellRendererComponent(javax.swing.JTable table,Object value,
        boolean isSelected,boolean hasFocus,int row,int col) {
            
            if(isSelected) {
                txtComponent.setBackground(Color.yellow);
            } else {
                txtComponent.setBackground(Color.white);
            }
            switch (col) {
                case IPREVIEW_ACTIVITY_TYPE_COLUMN:
                    txtComponent.setText(value.toString());
                    return txtComponent;
                case ACTIVITY_DATE_COLUMN:
                    if(value == null || value.toString().equals(EMPTY_STRING)){
                        txtComponent.setText(EMPTY_STRING);
                        return txtComponent;
                    }else{
                        String val = dtUtils.formatDate(value.toString(),REQUIRED_DATEFORMAT);
                        txtComponent.setText(val);
                        return txtComponent;
                    }
                case COMMENTS_COLUMN:
                    txtComponent.setText(value.toString());
                    return txtComponent;
            }
            return txtComponent;
        }
    }
    
    
    
    int row;
    int column;
    
    // This method will provide the key travrsal for the table cells
    // It specifies the tab and shift tab order.
    public void setTableKeyTraversal(){
        
        javax.swing.InputMap im = iPReviewForm.tblActivity.getInputMap(JTable.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        // Have the enter key work the same as the tab key
        KeyStroke tab = KeyStroke.getKeyStroke(KeyEvent.VK_TAB, 0);
        KeyStroke shiftTab = KeyStroke.getKeyStroke(KeyEvent.VK_TAB,KeyEvent.SHIFT_MASK );
        
        // Override the default tab behaviour
        // Tab to the next editable cell. When no editable cells goto next cell.
        final Action oldTabAction = iPReviewForm.tblActivity.getActionMap().get(im.get(tab));
        Action tabAction = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                int rowCount = iPReviewForm.tblActivity.getRowCount();
                int columnCount = iPReviewForm.tblActivity.getColumnCount();
                if(row==rowCount-1 && column==columnCount-1){
                    row = 0;
                    column = 0;
                    iPReviewTableCellEditor.stopCellEditing();
                    iPReviewForm.btnAdd.requestFocusInWindow();
                    return;
                }
                oldTabAction.actionPerformed( e );
                JTable table = (JTable)e.getSource();
                rowCount = table.getRowCount();
                columnCount = table.getColumnCount();
                row = table.getSelectedRow();
                column = table.getSelectedColumn();
                
                while ( table.isCellEditable(row, column) ) {
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
                
            }
        };
        iPReviewForm.tblActivity.getActionMap().put(im.get(tab), tabAction);
        
        
        
        // Override the default tab behaviour
        // Tab to the previous editable cell. When no editable cells goto next cell.
        
        final Action oldTabAction1 = iPReviewForm.tblActivity.getActionMap().get(im.get(shiftTab));
        Action tabAction1 = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                column = iPReviewForm.tblActivity.getSelectedColumn();
                row = iPReviewForm.tblActivity.getSelectedRow();
                int rowCount = iPReviewForm.tblActivity.getRowCount();
                int columnCount = iPReviewForm.tblActivity.getColumnCount();
                if(row==0 && column==0){
                    row = 0;
                    column = 0;
                    iPReviewTableCellEditor.stopCellEditing();
                    iPReviewForm.btnAdd.requestFocusInWindow();
                    return;
                }
                oldTabAction1.actionPerformed( e );
                JTable table = (JTable)e.getSource();
                rowCount = table.getRowCount();
                columnCount = table.getColumnCount();
                row = table.getSelectedRow();
                column = table.getSelectedColumn();
                
            }
        };
        iPReviewForm.tblActivity.getActionMap().put(im.get(shiftTab), tabAction1);
    }
    
}
