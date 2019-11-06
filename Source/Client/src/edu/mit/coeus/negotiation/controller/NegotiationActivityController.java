/*
 * NegotiationActivityController.java
 *
 * Created on July 12, 2004, 5:58 PM
 */

/** Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */


package edu.mit.coeus.negotiation.controller;


import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.utils.documenttype.CoeusDocumentUtils;
import java.io.File;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.text.*;
import edu.mit.coeus.negotiation.gui.NegotiationActivityForm;
import edu.mit.coeus.negotiation.bean.*;
import edu.mit.coeus.utils.query.*;
import edu.mit.coeus.gui.*;
import edu.mit.coeus.utils.*;
import edu.mit.coeus.exception.*;
import edu.mit.coeus.gui.event.BeanEvent;
import edu.mit.coeus.utils.document.DocumentBean;
import edu.mit.coeus.utils.document.DocumentConstants;
import java.net.URL;

/**
 *
 * @author  surekhan
 */
public class NegotiationActivityController extends NegotiationController implements
        ActionListener , FocusListener {
    
    /* awardNegotiationForm which this controls*/
    private NegotiationActivityForm negotiationActivityForm = new NegotiationActivityForm();
    
    /*Bean which holds all the form data*/
    private  NegotiationActivitiesBean negotiationActivitiesBean;
    
    /*queryEngine Instance*/
    private QueryEngine queryEngine;
    
    /*Message Resource*/
    private CoeusMessageResources coeusMessageResources;
    
    /*mdiForm instance*/
    private CoeusAppletMDIForm mdiForm = CoeusGuiConstants.getMDIForm() ;
    
    /*Dialog Window*/
    private CoeusDlgWindow dlgNegotiationActivity;
    
    //Added with 2806 - Upload attachments to negotiation module - Start
    /*width of the dialog window*/
    private static final int WIDTH =800;
    
    /*height of the dialog window*/
    private static final int HEIGHT = 400;
    //Added with 2806 - Upload attachments to negotiation module - End
    
    /*Title of the dialog*/
    private static final String WINDOW_TITLE = "Negotiation Activity";
    
    /*Function Type declaration*/
    private char functionType;
    
    /*Please enter an activity type*/
    private static final String ACTIVITY_TYPE = "negotiationActivity_exceptionCode.1151";
    
    /*Please enter an activity description*/
    private static final String ACTIVITY_DESC = "negotiationActivity_exceptionCode.1152";
    
    /*Please enter an activity date*/
    private static final String ACTIVITY_DATE = "negotiationActivity_exceptionCode.1153";
    
    /*Please enter a valid followup date*/
    private static final String VALID_FOLLOW_DATE = "negotiationActivity_exceptionCode.1154";
    
    /*Please enter a valid activity date*/
    private static final String VALID_ACTIVITY_DATE = "negotiationActivity_exceptionCode.1155";
    
    /*save confirmation message*/
    private static final String SAVE_CHANGES = "saveConfirmCode.1002";
    
    private static final String EMPTY_STRING = "";
    
    /*date utils*/
    private DateUtils dateUtils = new DateUtils();
    
    private static final String DATE_SEPARATERS = ":/.,|-";
    
    /*the format for the date fields - display*/
    private static final String SIMPLE_DATE_FORMAT = "MM/dd/yyyy";
    
    /*Holds the activity type combo codes and description*/
    private CoeusVector cvActivityType;
    
    /*the date format*/
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat(SIMPLE_DATE_FORMAT);
    
    /*if any of the data is changed the modified flag is set to true*/
    private boolean modified ;
    
    //New local variables added for case 2806-Upload attachments to negotiation - Start
    private static final String DELETE_CONFIRM      = "negotiationActivity_exceptionCode.1156";
    private static final String STREAMING_SERVLET   = CoeusGuiConstants.CONNECTION_URL + "/StreamingServlet";
    private static final String NEGOTIATION_SERVLET = CoeusGuiConstants.CONNECTION_URL + "/NegotiationMaintenanceServlet";
    private static final char GET_BLOB_DATA_FOR_NEGOTIATION = 'y';
    private boolean isAttachmentAlreadyPresent      = false;
    private String attachmentAcType                 = EMPTY_STRING;
    private char parentFunctionType;
    private CoeusFileChooser fileChooser;
    private NegotiationAttachmentBean attachmentBean = new NegotiationAttachmentBean();
    //New local variables added for case 2806-Upload attachments to negotiation - End
    
    //Added for COEUSDEV-294 :  Error adding activity to a negotiation - Start
    private static final char GET_MAX_NEGOTIATION_ACTIVITY_NUMBER = 'm';
    //COEUSDEV-294 : End
    
    /** Creates a new instance of NegotiationActivityController */
    public NegotiationActivityController(NegotiationBaseBean negotiationBaseBean, char functionType) {
        super(negotiationBaseBean);
        this.functionType = functionType;
        postInitComponents();
        requestDefaultFocus();
        registerComponents();
        setFunctionType(functionType);
        
    }
    
    /*Instantiates instance objects*/
    private void postInitComponents(){
        coeusMessageResources = CoeusMessageResources.getInstance();
        dlgNegotiationActivity = new CoeusDlgWindow(mdiForm);
        dlgNegotiationActivity.setResizable(false);
        dlgNegotiationActivity.setModal(true);
        dlgNegotiationActivity.getContentPane().add(negotiationActivityForm);
        dlgNegotiationActivity.setFont(CoeusFontFactory.getLabelFont());
        dlgNegotiationActivity.setDefaultCloseOperation(CoeusDlgWindow.DO_NOTHING_ON_CLOSE);
        dlgNegotiationActivity.setSize(WIDTH, HEIGHT);
        
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension dlgSize = dlgNegotiationActivity.getSize();
        dlgNegotiationActivity.setLocation(screenSize.width/2 - (dlgSize.width/2),
                screenSize.height/2 - (dlgSize.height/2));
        
        
        /*the action performed on pressing the escape key*/
        dlgNegotiationActivity.addEscapeKeyListener(
                new AbstractAction("escPressed"){
            public void actionPerformed(ActionEvent ae){
                performCancelAction();
                return;
            }
        });
        
        /*the action performed on closing the dialog window*/
        dlgNegotiationActivity.addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent we){
                performCancelAction();
            }
        });
        
        
        dlgNegotiationActivity.addComponentListener(
                new ComponentAdapter(){
            public void componentShown(ComponentEvent e){
                requestDefaultFocus();
            }
        });
    }
    
    /*To display the form*/
    public void display() {
        negotiationActivityForm.btnCancel.requestFocusInWindow();
        dlgNegotiationActivity.show();
    }
    
    
    /*formats Components*/
    public void formatFields() {
        if(functionType  == TypeConstants.DISPLAY_MODE){
            java.awt.Color bgColor = (java.awt.Color)javax.swing.UIManager.getDefaults().get("Panel.background");
            negotiationActivityForm.btnOK.setEnabled(false);
            negotiationActivityForm.txtArDescription.setEnabled(false);
            negotiationActivityForm.chkOSPOnly.setEnabled(false);
            negotiationActivityForm.txtActivityDate.setEditable(false);
            negotiationActivityForm.txtFollowupDate.setEditable(false);
            negotiationActivityForm.txtArDescription.setBackground(bgColor);
            negotiationActivityForm.cmbActivityType.setEnabled(false);
            negotiationActivityForm.txtArDescription.setEditable(false);
            negotiationActivityForm.txtArDescription.setDisabledTextColor(java.awt.Color.BLACK);
            negotiationActivityForm.btnRemove.setEnabled(false);
            negotiationActivityForm.btnUpload.setEnabled(false);
        }
    }
    
    
    /** returns controlled UI.
     * @return Controlled UI
     */
    public java.awt.Component getControlledUI() {
        return negotiationActivityForm;
    }
    
    
    /** returns form data.
     * @return returns form data.
     */
    public Object getFormData() {
        return negotiationActivityForm;
    }
    
    
    /*registers components with listeners*/
    public void registerComponents() {
        java.awt.Component[] components = { negotiationActivityForm.btnCancel,
        negotiationActivityForm.cmbActivityType,
        negotiationActivityForm.txtActivityDate,
        negotiationActivityForm.txtFollowupDate,
        negotiationActivityForm.chkOSPOnly,
        //Added with 2806 - Upload attachments to negotiation module - Start
        negotiationActivityForm.btnUpload,
        negotiationActivityForm.btnRemove,
        negotiationActivityForm.btnView,
        //Added with 2806 - Upload attachments to negotiation module - End
        negotiationActivityForm.txtArDescription,
        negotiationActivityForm.btnOK,
        };
        
        ScreenFocusTraversalPolicy traversePolicy = new ScreenFocusTraversalPolicy( components );
        negotiationActivityForm.setFocusTraversalPolicy(traversePolicy);
        negotiationActivityForm.setFocusCycleRoot(true);
        
        negotiationActivityForm.btnCancel.addActionListener(this);
        negotiationActivityForm.btnOK.addActionListener(this);
        //Added with 2806 - Upload attachments to negotiation module - Start
        negotiationActivityForm.btnUpload.addActionListener(this);
        negotiationActivityForm.btnView.addActionListener(this);
        negotiationActivityForm.btnRemove.addActionListener(this);
        //Added with 2806 - Upload attachments to negotiation module - End
        negotiationActivityForm.txtActivityDate.addFocusListener(this);
        negotiationActivityForm.txtCreateDate.addFocusListener(this);
        negotiationActivityForm.txtFollowupDate.addFocusListener(this);
        negotiationActivityForm.txtLastUpdate.addFocusListener(this);
        negotiationActivityForm.txtLastUpdateBy.addFocusListener(this);
        negotiationActivityForm.txtArDescription.addFocusListener(this);
        negotiationActivityForm.chkOSPOnly.addActionListener(this);
        
    }
    
    /* saves the form data*/
    public void saveFormData() {
        int code;
        ComboBoxBean comboBoxBean;
        String strDate;
        java.util.Date date;
        NegotiationActivitiesBean bean  = new NegotiationActivitiesBean();
        try{
            
            /*Setting the values for Activity Type combo*/
            comboBoxBean = (ComboBoxBean)negotiationActivityForm.cmbActivityType.getSelectedItem();
            if(! comboBoxBean.getCode().equals(EMPTY_STRING)) {
                code = Integer.parseInt(comboBoxBean.getCode().trim());
                bean.setNegotiationActTypeCode(code);
                bean.setActivityTypeDescription(comboBoxBean.getDescription());
                negotiationActivityForm.cmbActivityType.setSelectedItem(comboBoxBean);
            }
            
            /*setting the values for activity date*/
            strDate = negotiationActivityForm.txtActivityDate.getText().trim();
            if(! strDate.equals(EMPTY)) {
                date = (java.util.Date)simpleDateFormat.parse(dateUtils.restoreDate(strDate,DATE_SEPARATERS));
                bean.setActivityDate(new java.sql.Date(date.getTime()));
            }else {
                bean.setActivityDate(null);
            }
            
            /*setting the values for followup date*/
            strDate = negotiationActivityForm.txtFollowupDate.getText().trim();
            if(!strDate.equals(EMPTY)){
                date = (java.util.Date)simpleDateFormat.parse(dateUtils.restoreDate(strDate, DATE_SEPARATERS));
                bean.setFollowUpDate(new java.sql.Date(date.getTime()));
            }else {
                bean.setFollowUpDate(null);
            }
            
            /*setting the description*/
            String desc = negotiationActivityForm.txtArDescription.getText().trim();
            bean.setDescription(desc);
            
            /*setting the restiction*/
            bean.setRestrictedView(negotiationActivityForm.chkOSPOnly.isSelected());
            //Added for case 2806 - Upload attachments to neg module - Start
            
            //Modified for COEUSDEV-294 :  Error adding activity to a negotiation 
            if(attachmentBean != null){
                attachmentBean.setAcType(attachmentAcType);
                bean.setAttachmentBean(attachmentBean);
            }
            //COEUSDEV-294 : End
            
            //Added for case 2806 - Upload attachments to neg module - End
            /*setting the values for the non editable fields*/
            if(negotiationActivitiesBean != null){
                bean.setNegotiationNumber(negotiationActivitiesBean.getNegotiationNumber());
                bean.setCreateDate(negotiationActivitiesBean.getCreateDate());
                bean.setActivityNumber(negotiationActivitiesBean.getActivityNumber());
                bean.setUpdateTimestamp(negotiationActivitiesBean.getUpdateTimestamp());
                bean.setUpdateUser(negotiationActivitiesBean.getUpdateUser());
                bean.setLastUpdatedBy(negotiationActivitiesBean.getLastUpdatedBy());
                bean.setAttachmentPresent(negotiationActivitiesBean.isAttachmentPresent());
            }
            
            if(negotiationActivitiesBean == null){
                /*Setting the values for Activity Type combo*/
                comboBoxBean = (ComboBoxBean)negotiationActivityForm.cmbActivityType.getSelectedItem();
                if(! comboBoxBean.getCode().equals(EMPTY_STRING)) {
                    code = Integer.parseInt(comboBoxBean.getCode().trim());
                    bean.setNegotiationActTypeCode(code);
                    bean.setActivityTypeDescription(comboBoxBean.getDescription());
                    negotiationActivityForm.cmbActivityType.setSelectedItem(comboBoxBean);
                }
                
                /*setting the system date to the bean*/
                java.util.Date dbDate = (java.util.Date)CoeusUtils.getDBTimeStamp();
                bean.setCreateDate(new java.sql.Date(dbDate.getTime()));
                
                bean.setNegotiationNumber(negotiationBaseBean.getNegotiationNumber());
                bean.setActivityNumber(getNextActivityNumber());
                bean.setAcType(TypeConstants.INSERT_RECORD);
                queryEngine.insert(queryKey, bean);
                
                BeanEvent beanEvent = new BeanEvent();
                beanEvent.setBean(bean);
                beanEvent.setSource(this);
                fireBeanUpdated(beanEvent);
                
            }
            
            /*Update if the data is changed*/
            StrictEquals strEqualsActivities = new StrictEquals();
            
            /*comparing the temp bean to the negotiation activities bean instance and if they are not equal
             *then trying to update or insert to the query engine based on the modes*/
            if(getFunctionType() == TypeConstants.MODIFY_MODE){
                if(!strEqualsActivities.compare(negotiationActivitiesBean ,bean )){
                    /*if the beans are not same then set back the values to the instance bean*/
                    negotiationActivitiesBean.setNegotiationActTypeCode(bean.getNegotiationActTypeCode());
                    negotiationActivitiesBean.setActivityDate(bean.getActivityDate());
                    negotiationActivitiesBean.setFollowUpDate(bean.getFollowUpDate());
                    negotiationActivitiesBean.setRestrictedView(bean.isRestrictedView());
                    negotiationActivitiesBean.setDescription(bean.getDescription());
                    negotiationActivitiesBean.setActivityTypeDescription(comboBoxBean.getDescription());
                    //Added for case 2806 - Upload attachments to neg module - Start
//                    negotiationActivitiesBean.setFileName(bean.getFileName());
                    negotiationActivitiesBean.setAttachmentBean(bean.getAttachmentBean());
                    //Added for case 2806 - Upload attachments to neg module - Start
                    negotiationActivitiesBean.setAcType(TypeConstants.UPDATE_RECORD);
                    queryEngine.update(queryKey, negotiationActivitiesBean);
                    
                    BeanEvent beanEvent = new BeanEvent();
                    beanEvent.setBean(bean);
                    beanEvent.setSource(this);
                    fireBeanUpdated(beanEvent);
                }
            }
            
            
            
        } catch(ParseException parseException){
            parseException.printStackTrace();
        } catch(CoeusException coeusException){
            coeusException.printStackTrace();
        }
        dlgNegotiationActivity.dispose();
    }
    
    /** sets data to form.
     * @param data data to be set.
     */
    public void setFormData(Object data) {
        this.negotiationActivitiesBean = (NegotiationActivitiesBean)data;
        queryEngine = QueryEngine.getInstance();
        try{
            
            /*to set the title of the dialog*/
            dlgNegotiationActivity.setTitle(WINDOW_TITLE);
            negotiationActivityForm.btnCancel.requestFocusInWindow();
            NegotiationActivitiesBean bean = new NegotiationActivitiesBean();
            
            cvActivityType = new CoeusVector();
            cvActivityType = queryEngine.getDetails(queryKey, KeyConstants.NEGOTIATION_ACTIVITY_TYPE);
            ComboBoxBean emptyBean = new ComboBoxBean(EMPTY, EMPTY);
            cvActivityType.add(0, emptyBean);
            
            negotiationActivityForm.txtArDescription.setDocument(new LimitedPlainDocument(2000));
            negotiationActivityForm.txtActivityDate.setDocument(new LimitedPlainDocument(12));
            negotiationActivityForm.txtFollowupDate.setDocument(new LimitedPlainDocument(12));
            /*if it is the new mode set the activity tpyes and the create date only to their respective fields*/
            if(negotiationActivitiesBean == null){
                negotiationActivityForm.cmbActivityType.setModel(new DefaultComboBoxModel(cvActivityType));
                ComboBoxBean comboBoxBean = new ComboBoxBean();
                comboBoxBean.setCode(EMPTY + bean.getNegotiationActTypeCode());
                comboBoxBean.setDescription(bean.getActivityTypeDescription());
                negotiationActivityForm.cmbActivityType.setSelectedItem(comboBoxBean);
                
                /*setting the sysdate to the create date field in the form if it is the NEW_MODE*/
                java.util.Date dbDate = (java.util.Date)CoeusUtils.getDBTimeStamp();
                bean.setCreateDate(new java.sql.Date(dbDate.getTime()));
                negotiationActivityForm.txtCreateDate.setText(dateUtils.formatDate(bean.getCreateDate().toString(),SIMPLE_DATE_FORMAT));
            }
            
            
            if(negotiationActivitiesBean != null){
                negotiationActivityForm.cmbActivityType.setModel(new DefaultComboBoxModel(cvActivityType));
                ComboBoxBean comboBoxBean = new ComboBoxBean();
                comboBoxBean.setCode(EMPTY + negotiationActivitiesBean.getNegotiationActTypeCode());
                comboBoxBean.setDescription(negotiationActivitiesBean.getActivityTypeDescription());
                negotiationActivityForm.cmbActivityType.setSelectedItem(comboBoxBean);
                
                
                negotiationActivityForm.txtArDescription.setText(negotiationActivitiesBean.getDescription());
                
                
                if(negotiationActivitiesBean.getActivityDate() != null){
                    negotiationActivityForm.txtActivityDate.setText(dateUtils.formatDate(negotiationActivitiesBean.getActivityDate().toString(),SIMPLE_DATE_FORMAT));
                }
                
                if(negotiationActivitiesBean.getCreateDate() != null){
                    negotiationActivityForm.txtCreateDate.setText(dateUtils.formatDate(negotiationActivitiesBean.getCreateDate().toString(),SIMPLE_DATE_FORMAT));
                }
                
                if(negotiationActivitiesBean.getFollowUpDate() != null){
                    negotiationActivityForm.txtFollowupDate.setText(dateUtils.formatDate(negotiationActivitiesBean.getFollowUpDate().toString(),SIMPLE_DATE_FORMAT));
                }
                
                if(negotiationActivitiesBean.getUpdateTimestamp() != null){
                    negotiationActivityForm.txtLastUpdate.setText(simpleDateFormat.format(negotiationActivitiesBean.getUpdateTimestamp()));
                }
                
                negotiationActivityForm.txtLastUpdateBy.setText(negotiationActivitiesBean.getLastUpdatedBy());
                
               //Added for case 2806 - Upload attachments to neg module - Start
                boolean isRestricted = negotiationActivitiesBean.isRestrictedView();
                negotiationActivityForm.chkOSPOnly.setSelected(isRestricted);
                
                if ( parentFunctionType == TypeConstants.MODIFY_MODE){
                    //can view document in modify mode
                    negotiationActivityForm.btnView.setEnabled(true);
                }else  if(isRestricted && functionType == TypeConstants.DISPLAY_MODE){
                    //OSP only documents are not allowed to view in display mode.
                     negotiationActivityForm.btnView.setEnabled(false);
                }
                
                attachmentBean = negotiationActivitiesBean.getAttachmentBean();
                if(attachmentBean!=null){
                    isAttachmentAlreadyPresent = true;
                    negotiationActivityForm.txtAttachment.setText(attachmentBean.getFileName());
                }else{
                    negotiationActivityForm.btnView.setEnabled(false);
                    negotiationActivityForm.btnRemove.setEnabled(false);
                }
                
                isAttachmentAlreadyPresent = negotiationActivitiesBean.isAttachmentPresent();
                //Added for case 2806 - Upload attachments to neg module - End
            }
            
            //Bug Fix 1745 Start
            if(getFunctionType() == TypeConstants.ADD_MODE){
                negotiationActivityForm.chkOSPOnly.setSelected(true);
                negotiationActivityForm.btnView.setEnabled(false);
                negotiationActivityForm.btnRemove.setEnabled(false);
            }
            //Bug Fix 1745 End         
        }catch (CoeusException coeusException){
            coeusException.printStackTrace();
        }
        
    }
    
    
    /** validates the form.
     * returns false if validation fails.
     * else returns true.
     * @throws CoeusUIException if any exception occurs / validation fails.
     * @return returns false if validation fails.
     * else returns true.
     */
    public boolean validate() throws edu.mit.coeus.exception.CoeusUIException {
        negotiationActivityForm.txtFollowupDate.requestFocusInWindow();
        negotiationActivityForm.txtActivityDate.requestFocusInWindow();
        String strDate;
        
        /* invalid activity date validation*/
        strDate = negotiationActivityForm.txtActivityDate.getText().trim();
        if(strDate.equals(EMPTY_STRING)){
            negotiationActivityForm.txtActivityDate.setText(EMPTY_STRING);
        }else{            
            strDate = dateUtils.formatDate(strDate,DATE_SEPARATERS, SIMPLE_DATE_FORMAT);
            if(strDate == null ) {
                CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(VALID_ACTIVITY_DATE));
                negotiationActivityForm.txtActivityDate.requestFocusInWindow();
                return false;
            }else {
                strDate = dateUtils.formatDate(strDate,DATE_SEPARATERS, SIMPLE_DATE_FORMAT);
                negotiationActivityForm.txtActivityDate.setText(strDate);
            }
        }
        
        
        
        /*invalid followup date validation*/
        strDate = negotiationActivityForm.txtFollowupDate.getText().trim();
        if(strDate.equals(EMPTY_STRING)) {
            negotiationActivityForm.txtFollowupDate.setText(EMPTY_STRING);
        }else{
            strDate = dateUtils.formatDate(strDate,DATE_SEPARATERS, SIMPLE_DATE_FORMAT);
            if(strDate == null) {
                CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(VALID_FOLLOW_DATE));
                negotiationActivityForm.txtFollowupDate.requestFocusInWindow();
                return false;
            }else {
                strDate = dateUtils.formatDate(strDate,DATE_SEPARATERS, SIMPLE_DATE_FORMAT);
                negotiationActivityForm.txtFollowupDate.setText(strDate);
            }
        }
        
        /*Activity Type combo validation*/
        if(negotiationActivityForm.cmbActivityType.getSelectedIndex() == 0){
            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(ACTIVITY_TYPE));
            setRequestFocusInThread(negotiationActivityForm.cmbActivityType);
            return false;
        }
        // Added for COEUSDEV-738 : Build parameter to make 'description' field not required for new activity in the negotiation module - Start
        /*Activity Description validation*/
//        if(negotiationActivityForm.txtArDescription.getText().trim().equals(EMPTY_STRING)){
//            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(ACTIVITY_DESC));
//            setRequestFocusInThread(negotiationActivityForm.txtArDescription);
//            return false;
//        }
        // Description will be validated only when ENABLE_NEGOTIATION_ACTIVITY_DESC_MAND parameter is enabled(1)
        try{
            CoeusVector cvActivityParameter = queryEngine.getDetails(queryKey, KeyConstants.ENABLE_NEGOTIATION_ACTIVITY_DESC_MANDATORY);
            if(cvActivityParameter != null && !cvActivityParameter.isEmpty()){
                String isNegotiationActivDescMand =  (String)cvActivityParameter.get(0);
                if(isNegotiationActivDescMand != null && "1".equals(isNegotiationActivDescMand)){
                    if(negotiationActivityForm.txtArDescription.getText().trim().equals(EMPTY_STRING)){
                        CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(ACTIVITY_DESC));
                        setRequestFocusInThread(negotiationActivityForm.txtArDescription);
                        return false;
                    }
                }
            }
        }catch(CoeusException coeusException){
            coeusException.printStackTrace();
        }
        // Added for COEUSDEV-738 : Build parameter to make 'description' field not required for new activity in the negotiation module - End
        /*activity date validation*/
        if(negotiationActivityForm.txtActivityDate.getText().trim().equals(EMPTY_STRING) ){
            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(ACTIVITY_DATE));
            setRequestFocusInThread(negotiationActivityForm.txtActivityDate);
            return false;
        }
        
        
        return true;
    }
    
    
    /** listens to action events.
     * @param actionEvent actionEvent
     */
    public void actionPerformed(ActionEvent actionEvent) {
        Object source = actionEvent.getSource();
        if(source.equals(negotiationActivityForm.btnOK)){
            try{
                if(validate()){
                    saveFormData();
                }
            }catch(CoeusUIException coeusUIException){
                coeusUIException.printStackTrace();
            }
        }else if(source.equals(negotiationActivityForm.btnCancel)){
            if( getFunctionType() == TypeConstants.DISPLAY_MODE ){
                dlgNegotiationActivity.dispose();
            }else {
                performCancelAction();
            }
            //Added with case 2806 - Upload attachments in negotiation module -Start
        }else if(source.equals(negotiationActivityForm.btnUpload)){
            if( getFunctionType() != TypeConstants.DISPLAY_MODE ){
                performUploadAction();
            }
        }else if(source.equals(negotiationActivityForm.btnView)){
                performViewAction();
        }else if(source.equals(negotiationActivityForm.btnRemove)){
            if( getFunctionType() != TypeConstants.DISPLAY_MODE ){
                performRemoveAction();
            }
        }
        //Added with case 2806 - Upload attachments in negotiation module -End
    }
    
    /*the action performed on the click of the cancel command button*/
    private void performCancelAction(){
        if(getFunctionType() == TypeConstants.DISPLAY_MODE){
            dlgNegotiationActivity.dispose();
            return;
        }
        try{
            NegotiationActivitiesBean tempBean  = new NegotiationActivitiesBean();
            int code;
            String strDate;
            java.util.Date date;
            
            
            /*Setting the values for Activity Type combo*/
            ComboBoxBean comboBoxBean = (ComboBoxBean)negotiationActivityForm.cmbActivityType.getSelectedItem();
            if(! comboBoxBean.getCode().equals(EMPTY_STRING)) {
                code = Integer.parseInt(comboBoxBean.getCode().trim());
                tempBean.setNegotiationActTypeCode(code);
                tempBean.setActivityTypeDescription(comboBoxBean.getDescription());
                negotiationActivityForm.cmbActivityType.setSelectedItem(comboBoxBean);
            }
            
            /*setting the values for activity date*/
            strDate = negotiationActivityForm.txtActivityDate.getText().trim();
            String actDate = dateUtils.formatDate(
                    strDate,DATE_SEPARATERS, SIMPLE_DATE_FORMAT);
            if(actDate == null){
                modified = true;
                
            }
            if(actDate != null){
                if(! strDate.equals(EMPTY)) {
                    date =(java.util.Date)simpleDateFormat.parse(actDate);
                    tempBean.setActivityDate(new java.sql.Date(date.getTime()));
                }else {
                    tempBean.setActivityDate(null);
                }
            }
            
            /*setting the values for followup date*/
            strDate = negotiationActivityForm.txtFollowupDate.getText().trim();
            String followDate = dateUtils.formatDate(strDate,DATE_SEPARATERS,SIMPLE_DATE_FORMAT);
            if(followDate == null){
                modified = true;
            }
            if(followDate != null){
                if(!strDate.equals(EMPTY)){
                    date = (java.util.Date)simpleDateFormat.parse(followDate);
                    tempBean.setFollowUpDate(new java.sql.Date(date.getTime()));
                }else {
                    tempBean.setFollowUpDate(null);
                }
            }
            
            /*setting the description*/
            String desc = negotiationActivityForm.txtArDescription.getText().trim();
            tempBean.setDescription(desc);
            
            /*setting the restiction*/
            tempBean.setRestrictedView(negotiationActivityForm.chkOSPOnly.isSelected());
            //Added for case 2806 - Upload attachments to neg module - Start
            /*setting the fileName */ 
//            String strFileName = negotiationActivityForm.txtAttachment.getText();
//            if(strFileName!=null && strFileName.indexOf('\\') != -1){
//                    strFileName = strFileName.substring(strFileName.trim().lastIndexOf('\\')+1);
//                }
//            tempBean.setFileName(strFileName);
            tempBean.setAttachmentBean(attachmentBean);
            //Added for case 2806 - Upload attachments to neg module - End
            /*setting the values for the non editable fields*/
            if(getFunctionType() == TypeConstants.MODIFY_MODE){
                tempBean.setNegotiationNumber(negotiationActivitiesBean.getNegotiationNumber());
                tempBean.setCreateDate(negotiationActivitiesBean.getCreateDate());
                tempBean.setActivityNumber(negotiationActivitiesBean.getActivityNumber());
                tempBean.setUpdateTimestamp(negotiationActivitiesBean.getUpdateTimestamp());
                tempBean.setUpdateUser(negotiationActivitiesBean.getUpdateUser());
                tempBean.setLastUpdatedBy(negotiationActivitiesBean.getLastUpdatedBy());
                tempBean.setAcType(negotiationActivitiesBean.getAcType());
                tempBean.setAttachmentPresent(negotiationActivitiesBean.isAttachmentPresent());
            }
            
            
            if(getFunctionType() == TypeConstants.ADD_MODE){
                modified = true;
            }
            
            /*comparing the temperory bean with the negotiation activities bean
             *if the beans are not equal then the modified flag is set to true and the cancel action
             *is performed*/
            if(negotiationActivitiesBean != null){
                StrictEquals strActivities = new StrictEquals();
                if(!strActivities.compare(negotiationActivitiesBean ,tempBean )){
                    modified =  true;
                }else{
                    modified = false;
                }
            }
            if(modified){
                negotiationActivityForm.txtActivityDate.removeFocusListener(this);
                negotiationActivityForm.txtFollowupDate.removeFocusListener(this);
                int option = CoeusOptionPane.showQuestionDialog(
                        coeusMessageResources.parseMessageKey(SAVE_CHANGES),
                        CoeusOptionPane.OPTION_YES_NO_CANCEL,
                        CoeusOptionPane.DEFAULT_YES);
                negotiationActivityForm.txtActivityDate.addFocusListener(this);
                negotiationActivityForm.txtFollowupDate.addFocusListener(this);
                switch( option ) {
                    case (JOptionPane.YES_OPTION ):
                        setSaveRequired(true);
                        try{
                            if( validate() ){
                                saveFormData();
                            }
                        }catch (Exception exception){
                            exception.printStackTrace();
                        }
                        break;
                    case(JOptionPane.NO_OPTION ):
                        dlgNegotiationActivity.dispose();
                        break;
                    default:
                        break;
                }
            }else{
                dlgNegotiationActivity.dispose();
            }
        } catch (ParseException parseException){
            parseException.printStackTrace();
        }
    }
    
    /** listens to focus gain event.
     * @param focusEvent focusEvent
     */
    public void focusGained(FocusEvent focusEvent) {
        if(focusEvent.isTemporary()) return ;
        
        Object source = focusEvent.getSource();
        
        if(source.equals(negotiationActivityForm.txtActivityDate)) {
            String effectiveDate = negotiationActivityForm.txtActivityDate.getText();
            effectiveDate = dateUtils.restoreDate(effectiveDate, DATE_SEPARATERS);
            negotiationActivityForm.txtActivityDate.setText(effectiveDate);
        } else if(source.equals(negotiationActivityForm.txtFollowupDate)) {
            String effectiveDate = negotiationActivityForm.txtFollowupDate.getText();
            effectiveDate = dateUtils.restoreDate(effectiveDate, DATE_SEPARATERS);
            negotiationActivityForm.txtFollowupDate.setText(effectiveDate);
        }
        
    }
    
    
    /** listens to focus lost event.
     * @param focusEvent focusEvent
     */
    public void focusLost(FocusEvent focusEvent) {
        Object source = focusEvent.getSource();
        if(source.equals(negotiationActivityForm.txtActivityDate)){
            String actDate;
            actDate = negotiationActivityForm.txtActivityDate.getText().trim();
            if( actDate.equals(EMPTY_STRING) ) return;
            String activityDate = dateUtils.formatDate(
                    actDate,DATE_SEPARATERS, SIMPLE_DATE_FORMAT);           
            if(activityDate == null){
           //     CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(VALID_ACTIVITY_DATE));
           //     setRequestFocusInThread(negotiationActivityForm.txtActivityDate);
            }else{
                negotiationActivityForm.txtActivityDate.setText(activityDate);
            }
        }else if(source.equals(negotiationActivityForm.txtFollowupDate)){
            String followDate;
            followDate = negotiationActivityForm.txtFollowupDate.getText().trim();
            if( followDate.equals(EMPTY_STRING)) return;
            String followupDate = dateUtils.formatDate(followDate,DATE_SEPARATERS,SIMPLE_DATE_FORMAT);
            if(followupDate == null){
                CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(VALID_FOLLOW_DATE));
                setRequestFocusInThread(negotiationActivityForm.txtFollowupDate);
            }else{
                negotiationActivityForm.txtFollowupDate.setText(followupDate);
            }
            
        }
    }
    
    /*to set the focus in the respective fields*/
    private void setRequestFocusInThread(final Component component) {
        SwingUtilities.invokeLater( new Runnable() {
            public void run() {
                component.requestFocusInWindow();
            }
        });
    }
    
    /*to set the default focus to the component*/
    public void requestDefaultFocus(){
        negotiationActivityForm.btnCancel.requestFocusInWindow();
        
    }
    
    /*to get the next activity number */
    private int getNextActivityNumber(){
        int activityNumber = 1;
        CoeusVector cvtempdata = new CoeusVector();
        NegotiationActivitiesBean negotiationActivitiesBean;
        try {
            //Added for COEUSDEV-294 : Error adding activity to a negotiation - Start
            //Get the max activity number for the negotiation
            int maxNegotiationActivityNumber = getMaxNegotiationActivityNumber(negotiationBaseBean.getNegotiationNumber());
            //COEUSDEV-294 : End
            cvtempdata = queryEngine.executeQuery(queryKey,
                    NegotiationActivitiesBean.class,CoeusVector.FILTER_ACTIVE_BEANS);
            if (cvtempdata!= null && cvtempdata.size() > 0) {
                cvtempdata.sort("activityNumber", false);
                negotiationActivitiesBean = (NegotiationActivitiesBean) cvtempdata.get(0);
                activityNumber = negotiationActivitiesBean.getActivityNumber() + 1;
            }//Added for COEUSDEV-294 : Error adding activity to a negotiation - Start
            //If no activities are avilable
            //If user doesn't have any osp rights, activities with osp will not be in the collection
            else{
                activityNumber = maxNegotiationActivityNumber+1;
            }
            //If osp activities are not dispalyed 'activityNumber' might be the activity number available in DB.
            //Check is made with the max activity number from DB.
            if(activityNumber <= maxNegotiationActivityNumber){
                activityNumber = activityNumber+1;
            }
            //COEUSDEV-294 : End
        } catch(Exception exception) {
            exception.printStackTrace();
        }
        return activityNumber;
    }
    
    /**
     * To make the class variable instances to null
     */
    public void cleanUp() {
        negotiationActivitiesBean = null;
        cvActivityType = null;
        negotiationBaseBean = null;
        dlgNegotiationActivity = null;
        //negotiationActivityForm = null;
    }
    
    //New methods added with case 2806 - Upload attachments to negotiation module - Start
    /* The action handle for Upload Attachment Button click */
    private void performUploadAction(){
        
        fileChooser = new CoeusFileChooser(CoeusGuiConstants.getMDIForm());
        fileChooser.setAcceptAllFileFilterUsed(true);
        fileChooser.showFileChooser();
        if(fileChooser.isFileSelected()){
            String fileName = fileChooser.getSelectedFile();
            File file = fileChooser.getFileName();
            if(fileName!=null && file!=null){
               if(isAttachmentAlreadyPresent){
                   attachmentAcType = TypeConstants.UPDATE_RECORD;
               }else{
                   attachmentAcType = TypeConstants.INSERT_RECORD;
               }
                negotiationActivityForm.txtAttachment.setText(fileName); 
                
                attachmentBean = new NegotiationAttachmentBean();
                attachmentBean.setFileName(file.getName());
                attachmentBean.setFileBytes(fileChooser.getFile());
                //Modified with case 4007: Icon based on attachment type
                CoeusDocumentUtils docTypeUtils =  CoeusDocumentUtils.getInstance();
                attachmentBean.setMimeType(docTypeUtils.getDocumentMimeType(attachmentBean));
                //4007 End
                negotiationActivityForm.btnView.setEnabled(true);
                negotiationActivityForm.btnRemove.setEnabled(true);
                requestDefaultFocus();
            }
        }
        
    }
    
    /* The action handle for Remove Attachment Button click */
    private void performRemoveAction(){
        int option = CoeusOptionPane.showQuestionDialog(
                coeusMessageResources.parseMessageKey(DELETE_CONFIRM),
                CoeusOptionPane.OPTION_YES_NO,
                CoeusOptionPane.DEFAULT_NO);
        if(option == JOptionPane.YES_OPTION){
            
            attachmentBean = new NegotiationAttachmentBean();//reset all the values
            if(isAttachmentAlreadyPresent){
                attachmentAcType = TypeConstants.DELETE_RECORD;
            }else{
                attachmentAcType = EMPTY_STRING;
            }
            negotiationActivityForm.txtAttachment.setText(EMPTY_STRING);
            negotiationActivityForm.btnView.setEnabled(false);
            negotiationActivityForm.btnRemove.setEnabled(false);
            negotiationActivityForm.btnUpload.setEnabled(true);
            requestDefaultFocus();
        }
    }
    
    /* The action handle for View Attachment Button click */
    private void performViewAction(){
        RequesterBean request = new RequesterBean();
        Vector dataObjects = new Vector();
        if(negotiationActivitiesBean!=null 
                && attachmentBean!=null
                    && attachmentBean.getFileBytes() == null){ 
            //this means that there is a blob in db but it is not fetched right now
            attachmentBean.setFileBytes(getBlobDataForNegActivity().getFileBytes());
        }
        dataObjects.add(0, attachmentBean);
        dataObjects.add(1,CoeusGuiConstants.getMDIForm().getUnitNumber());
        DocumentBean documentBean = new DocumentBean();
        Map map = new HashMap();
        map.put("DATA", dataObjects);
        map.put("USER_ID", CoeusGuiConstants.getMDIForm().getUserId());
        map.put(DocumentConstants.DOC_ON_URL_GENERATION, new Boolean(true));
        map.put(DocumentConstants.READER_CLASS, "edu.mit.coeus.negotiation.NegotiationAttachmentReader");
        documentBean.setParameterMap(map);
        request.setDataObject(documentBean);
        request.setFunctionType(DocumentConstants.GENERATE_STREAM_URL);
        //For Streaming
        
        AppletServletCommunicator comm
                = new AppletServletCommunicator(STREAMING_SERVLET, request);
        comm.send();
        ResponderBean response = comm.getResponse();
        if(response.isSuccessfulResponse()){
            try{
                map = (Map)response.getDataObject();
                String reportUrl = (String)map.get(DocumentConstants.DOCUMENT_URL);
                reportUrl = reportUrl.replace('\\', '/') ;
                URL urlObj = new URL(reportUrl);
                URLOpener.openUrl(urlObj);
            }catch (Exception e){
                e.printStackTrace();
                CoeusOptionPane.showInfoDialog( e.getMessage() );
            }
        }
    }
    
    /* The method to fetch blob data of attachment from DB */
    private NegotiationAttachmentBean getBlobDataForNegActivity(){
       
        NegotiationAttachmentBean negotiationAttachmentBean = new NegotiationAttachmentBean();
        negotiationAttachmentBean.setNegotiationNumber(negotiationBaseBean.getNegotiationNumber());
        negotiationAttachmentBean.setActivityNumber(negotiationActivitiesBean.getActivityNumber());
        RequesterBean request = new RequesterBean();
        request.setFunctionType(GET_BLOB_DATA_FOR_NEGOTIATION);
        request.setDataObject(negotiationAttachmentBean);
        AppletServletCommunicator comm = new AppletServletCommunicator(NEGOTIATION_SERVLET, request);
        comm.send();
        
        ResponderBean response = comm.getResponse();
        negotiationAttachmentBean = (NegotiationAttachmentBean)response.getDataObject();
        return negotiationAttachmentBean;
    }
    
    public void setParentFunctionType(char pFunctionType) {
        this.parentFunctionType = pFunctionType;
    }
    //New methods added with case 2806 - End
    
    //Added for COEUSDEV-294:  Error adding activity to a negotiation - Start
    /**
     * Method to get max negotiaton activity number
     * @param String negotiattionNumber
     * @return maxNegotiationActivityNumber
     */
    private int getMaxNegotiationActivityNumber(String negotiationNumber){
        int maxNegotiationActivityNumber = -1;
        RequesterBean request = new RequesterBean();
        Vector dataObjects = new Vector();
        request.setDataObject(negotiationNumber);
        request.setFunctionType(GET_MAX_NEGOTIATION_ACTIVITY_NUMBER);
        
        AppletServletCommunicator comm
                = new AppletServletCommunicator(NEGOTIATION_SERVLET, request);
        comm.send();
        ResponderBean response = comm.getResponse();
        if(response.isSuccessfulResponse()){
            try{
                Integer maxNegotiationActivityNum = (Integer)response.getDataObject();
                if(maxNegotiationActivityNum != null){
                    maxNegotiationActivityNumber = maxNegotiationActivityNum.intValue();
                }
                
            }catch (Exception e){
                CoeusOptionPane.showInfoDialog( e.getMessage() );
            }
        }
        return maxNegotiationActivityNumber;
    }
    //COEUSDEV-294 : End
}
