/**
 * @(#)ProtocolSpecialReviewForm.java  1.0  April 12, 2003, 12:17 PM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */ 

package edu.mit.coeus.irb.gui;

import edu.mit.coeus.irb.bean.*;
import edu.mit.coeus.utils.*;
import edu.mit.coeus.gui.*;
import edu.mit.coeus.bean.*;
import edu.mit.coeus.exception.*;

import java.util.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import javax.swing.event.*;
import java.awt.event.*;
import java.awt.*;
 
/** 
 * <CODE>ProtocolSpecialReviewForm </CODE>is a form object which display
 * the Protocol Special Reviews and it is used to <CODE> add/delete/display/Modify </CODE> the Special Reviews for a particular protocol.
 * This class will be instantiated from <CODE>ProtocolDetailForm</CODE>.
 * @version 1.0 April 12, 2003, 12:17 PM
 * @author Raghunath P.V.
 */
public class ProtocolSpecialReviewForm extends javax.swing.JComponent implements TypeConstants, ActionListener, 
                                        FocusListener, ListSelectionListener{
    
    private char functionType;
    private boolean saveRequired;
    //Vector to hold special review type Codes - Lookup values
    private Vector vecSpecialReviewCode;
    private Vector vecDeletedSpecialreviewCodes;
    //Vector to hold approval type Codes - Lookup values
    private Vector vecApprovalTypeCode;
    //Vector to hold special review data for a paricular protocol
    private Vector vecSpecialReviewData;
    //Vector to hold Special Review Validate Beans used for validations - Lookup values
    private Vector vecValidateRules;
    //Vector To hold  special review Description Values
    private Vector vecSpecialReviewCodeDescriptions;
    //Vector To hold  Approval Description Values
    private Vector vecApprovalCodeDescriptions;
    // Combo used as a Cell Editor for special review code
    private JComboBox specialCombo;
    // Combo used as a Cell Editor for special review code
    private JComboBox approvalCombo;
    /* This is used to hold MDI form reference */
    private CoeusAppletMDIForm mdiReference;
    private boolean firstEntry = false;
    //holds the last selected Row
    private int lastSelectedRow = 0;
    //holds the previous comment
    private String prvComments = "";
    //holds the  comment
    private String absComments = "";
    //holds the so far saved info
    private boolean isFormDataUpdated = false;
    /** instance of DateUtils object which will be used for formatting dates */
    private DateUtils dtUtils;
    //holds CoeusMessageResources instance used for reading message Properties.
    private CoeusMessageResources coeusMessageResources;
    private ProtocolSpecialReviewFormBean protocolSpecialReviewFormBean;
    /** used to parse the dates which are represented as strings */
    private java.text.SimpleDateFormat dtFormat;
    private static final String DATE_SEPARATERS = ":/.,|-";
    private static final String REQUIRED_DATEFORMAT = "dd-MMM-yyyy";
    private static final int ZERO_COUNT = 0;
    private int previousSelRow = -1;
    
    private TableCellEditor dateEditComponent;    
    
    /** Creates new form ProtocolSpecialReviewForm */
    public ProtocolSpecialReviewForm() {
    }
    
    /** Creates new form <CODE>ProtocolSpecialReviewForm</CODE>
     *
     * @param functionType this will open the different mode like Display
     * @param specialReviewData is a Vector of Special review info beans.
     * 'D' specifies that the form is in Display Mode
     */
    public ProtocolSpecialReviewForm( char functionType, Vector specialReviewData) {
        
        this.functionType = functionType;
        this.vecSpecialReviewData = specialReviewData;
        this.vecDeletedSpecialreviewCodes = new Vector();
        this.dtUtils = new DateUtils();
        this.dtFormat = new java.text.SimpleDateFormat("MM/dd/yyyy");
        approvalCombo = new JComboBox();
        approvalCombo.setFont(CoeusFontFactory.getNormalFont());
        specialCombo = new JComboBox();
        specialCombo.setFont(CoeusFontFactory.getNormalFont());
        
        dateEditComponent = new DateEditor("ApplDate");                      //// Handle
    }
    
    /** This method is used to initialize the components, set the data in the components.
      * This method is invoked in the <CODE>ProtocolDetailForm</CODE>.
      * @param mdiForm is a reference of CoeusAppletMDIForm
      * @return a JPanel containing all the components with the data populated.
      */
    
    public JComponent showProtocolSpecialReviewForm(CoeusAppletMDIForm
        mdiForm){
            
        this.mdiReference = mdiForm;        
        initComponents();
        java.awt.Component[] components = {tblSpecialReview,btnAdd,btnDelete,txtAreaComments};
        ScreenFocusTraversalPolicy  traversePolicy = new ScreenFocusTraversalPolicy( components );
        setFocusTraversalPolicy(traversePolicy);
        setFocusCycleRoot(true);
        setAcTypes();
        // This method Adds the data to the form i.e. Jtable
        setFormData();
        // This method enables/ disables the Buttons based on the function type.
        formatFields();
        // This method does the settings to the jtable.
        setTableEditors();
        /* This logic is used to select the first row in the list of available 
           rows in JTable*/
        if( tblSpecialReview!=null && tblSpecialReview.getRowCount() > ZERO_COUNT ){
            
            tblSpecialReview.setRowSelectionInterval(ZERO_COUNT,ZERO_COUNT);
            
            ProtocolSpecialReviewFormBean firstBean =
                (ProtocolSpecialReviewFormBean)vecSpecialReviewData.get( ZERO_COUNT );
            
            if(firstBean != null){
                txtAreaComments.setText( firstBean.getComments() );
                txtAreaComments.setCaretPosition(0);
            }            
        }else{
            // If no Data is there then set the delete button to disable mode.
            txtAreaComments.setEnabled(false);
            btnDelete.setEnabled(false);
        }
        // This method adds the listeners to all the buttons and to the Jtable
        setListenersForButtons();
        // setting bold property for table header values
        tblSpecialReview.getTableHeader().setFont(CoeusFontFactory.getLabelFont());
        coeusMessageResources = CoeusMessageResources.getInstance();
        return this;
    }
    
    /**
     * This Method is used to set the Vector which contains the special 
     * review for the selected protocol number and sequence number.
     * @param specialReviewData is a vector which contain the collection of SpecialReviewInfoBeans
     */
    public void setProtocolSpecialReviewData(Vector specialReviewData){
        
        int selectedRow=0;
        if(tblSpecialReview.getRowCount()>0)
        {
            selectedRow=tblSpecialReview.getSelectedRow();
        }
        
        this.vecSpecialReviewData = specialReviewData;
        // added by ravi to reset the deleted vector after saving
        vecDeletedSpecialreviewCodes = new Vector();
        setAcTypes();
        //code commented for coeus4.3 concurrent Amendments/Renewals enhancement
//        if(tblSpecialReview.getRowCount()>0)
//            tblSpecialReview.setRowSelectionInterval(selectedRow, selectedRow);
        
        //code added for coeus4.3 concurrent Amendments/Renewals enhancement - starts
        //To refresh the special review tab with new datas.
//        txtAreaComments.setText("");
        setFormData();
        setTableEditors();
        formatFields();
        if(tblSpecialReview.getRowCount()>0 && tblSpecialReview.getRowCount()>selectedRow){
            tblSpecialReview.setRowSelectionInterval(selectedRow, selectedRow);
        } else if (tblSpecialReview.getRowCount()>0) {
            tblSpecialReview.setRowSelectionInterval(0, 0);        
        }
        //code added for coeus4.3 concurrent Amendments/Renewals enhancement - ends
    }
    
    /**
     * This method is used to set the Vector which contain the values for 
     * validating the row data based on the Special Review code and approval code
     * @param validateVector is a Vector which contain the SRApprovalInfoBean collection 
     * @return void
     */
    
    public void setValidateVector(Vector validateVector){
        this.vecValidateRules = validateVector;
    }
    
   /**
     * This method is used to trigger the save required flag, 
     * when the user Clicks the Save button and the user is in the Editor Field.
     */

    public void handleEditableComponents(){
        
        int rowCount = tblSpecialReview.getRowCount();
        if( rowCount > 0 ) {
            int selRow = tblSpecialReview.getSelectedRow();
            int colNum = tblSpecialReview.getSelectedColumn();
            if(selRow != -1){
                if(tblSpecialReview.isEditing()){
                    tblSpecialReview.getCellEditor(selRow,colNum).stopCellEditing();
                }
            }
        }
     }
    
   /** This method is used to perform Validations.
     * @return true if the validation succeed, false otherwise.
     * @throws Exception is a exception to be thrown in the client side.
     */
    public boolean validateData() throws Exception, CoeusUIException{
        
        boolean valid=true;
        int rowCount = tblSpecialReview.getRowCount();
        if(rowCount >= 0){

        for(int inInd=0; inInd < rowCount ;inInd++){
            
            String stSpecialDesc = (String)((DefaultTableModel)
                                        tblSpecialReview.getModel()).
                                                getValueAt(inInd,1);
            String stApprovalDesc = (String)((DefaultTableModel)
                                        tblSpecialReview.getModel()).
                                                getValueAt(inInd,2);
            
            if((stSpecialDesc == null) || (stSpecialDesc.trim().length() <= 0)){
                valid=false;
                errorMessage(coeusMessageResources.parseMessageKey(
                    "protocol_SpecialReviewForm_exceptionCode.1063"));
                tblSpecialReview.requestFocus();
            break;
            }
            
            if((stApprovalDesc == null) || (stApprovalDesc.trim().length() <= 0)){
                valid=false;
                errorMessage(coeusMessageResources.parseMessageKey(
                    "protocol_SpecialReviewForm_exceptionCode.1064"));
                 tblSpecialReview.requestFocus();
                break;
            }
            
            String specialDesc = (String)tblSpecialReview.getValueAt(inInd, 1);
            String approvalDesc = (String)tblSpecialReview.getValueAt(inInd, 2);
            String proNum = (String)tblSpecialReview.getValueAt(inInd, 3);
            String apDate = (String)tblSpecialReview.getValueAt(inInd, 4);
            String arDate = (String)tblSpecialReview.getValueAt(inInd, 5);
            if(specialDesc != null && specialDesc.trim().length() > 0 && 
                        approvalDesc != null && approvalDesc.trim().length() > 0){

                String srCode = getIDForName(specialDesc, vecSpecialReviewCode);
                String aprCode = getIDForName(approvalDesc, vecApprovalTypeCode);
                int sCode = Integer.parseInt(srCode); 
                int appCode = Integer.parseInt(aprCode);
                boolean validate = validateRowData(sCode, appCode, proNum, apDate, arDate);
                if(!validate){
                    valid = false;
                    break;
                }else{
                    if( ( apDate != null && apDate.trim().length() > 0 )
                         && ( arDate != null  && arDate.trim().length() > 0 ) ) {
                        java.util.Date applDate = dtFormat.parse(
                        dtUtils.restoreDate(apDate,"/:-,"));
                        java.util.Date apprDate = dtFormat.parse(
                        dtUtils.restoreDate(arDate,"/:-,"));
                        if(apprDate.compareTo(applDate)<0){
                            /* Approval Date is earlier than Application Date */
                            errorMessage(
                            coeusMessageResources.parseMessageKey(
                                "protocol_SpecialReviewForm_exceptionCode.1101"));
                            valid = false;
                            break;
                        }
                    }
                }
            }
        }
        if(!valid){
            return false;
        }
      }
      return true;
    }
        
    /**
     * This method is used to get the Form data.
     * @returns Vector of ProtocolSpecialReviewFormBean's
     */
    
    public Vector getProtocolSpecialReviewData(){
        
        if(vecDeletedSpecialreviewCodes != null){
            int delSize = vecDeletedSpecialreviewCodes.size();
            ProtocolSpecialReviewFormBean protocolReviewBean = null;
            for(int index = 0; index < delSize; index++){
                protocolReviewBean = (ProtocolSpecialReviewFormBean)vecDeletedSpecialreviewCodes.get(index);
                if(protocolReviewBean != null && vecSpecialReviewData !=null){
                    vecSpecialReviewData.add(protocolReviewBean);
                }
            }
        }
        return setAcTypesInAmend();
        //printData();
        //return vecSpecialReviewData;
    }
    
    // Testing
    private void printData(){
        if(vecSpecialReviewData != null){
            ProtocolSpecialReviewFormBean pBean = null;
            System.out.println("Reviews Client side , Size is "+vecSpecialReviewData.size());
            System.out.println("******************************");
            for(int index = 0; index < vecSpecialReviewData.size(); index++){
                pBean = (ProtocolSpecialReviewFormBean)vecSpecialReviewData.get(index);
                if(pBean != null){
                    System.out.println("In Detail Form Values to Server");
                    System.out.println("Bean "+index);

                    System.out.println("In Actype "+pBean.getAcType());
                    System.out.println("In getApprovalCode "+pBean.getApprovalCode());
                    System.out.println("In getSpecialReviewCode "+pBean.getSpecialReviewCode());
                    System.out.println("In getSpecialReviewNumber "+pBean.getSpecialReviewNumber());
                }
            }
        } 
    }
    
   /** This method is used to find out whether modifications done to the data
     * have been saved or not.
     *
     * @return true if data is not saved after modifications, else false.
     */
    public boolean isSaveRequired()  {
        
        handleEditableComponents();
        int selRow = tblSpecialReview.getSelectedRow();
        if( selRow != -1 ) {
            updateComments(selRow);
        }
        
        return saveRequired;
        
    }
    
    //Added by Amit 11/21/2003
    /** This method use to implement focus on first editable component in this page.
     */
    public void setDefaultFocusForComponent(){
        //code modified for coeus4.3 enhancements that UI to be in display mode
        //when new amendment or renewal is created
//        if(!( functionType == CoeusGuiConstants.DISPLAY_MODE )) {
        if(!( functionType == CoeusGuiConstants.DISPLAY_MODE ) &&
                !( functionType == CoeusGuiConstants.AMEND_MODE )) {            
        
            if(tblSpecialReview.getRowCount() > 0 ) {
                tblSpecialReview.requestFocusInWindow();
                
                //included by raghu to remain the selection on row upon selection...
                //starts..
                int prevSelectedRow=tblSpecialReview.getSelectedRow();
                if(prevSelectedRow!=-1){
                    tblSpecialReview.setRowSelectionInterval(prevSelectedRow, prevSelectedRow);
                }
                else{
                    tblSpecialReview.setRowSelectionInterval(0, 0);
                }
                //ends
                tblSpecialReview.setColumnSelectionInterval(1,1);
            }else{
                btnAdd.requestFocusInWindow();
            }            
        }
    }    
    //end Amit   
    
     /** This method is used to set whether modifications are to be saved or not.
     *
     * @param saveRequired boolean true if data is to be saved after modifications,
     * else false.
     */
    public void setSaveRequired(boolean saveRequired){
        
        this.saveRequired = saveRequired;
        
    }
    
    /** Method to get the functionType
     * @return a <CODE>Char</CODE> representation of functionType.
     */
    public char getFunctionType(){
        
        return this.functionType;
        
    }
     /** Method to set the functionType
     * @param fType is functionType to be set like 'D', 'I', 'M'
     */
    public void setFunctionType(char fType){
        
        this.functionType = fType;
        
    }
    
    /**
     * This method is used to set the available Special Review Type Codes
     * @param reviewCodes collection of available Special Review Type Codes
     */
    public void setSpecialReviewTypeCodes(Vector reviewCodes){
        this.vecSpecialReviewCode = reviewCodes;
        if(vecSpecialReviewCode != null){
            vecSpecialReviewCodeDescriptions = getDescriptionsForCombo(vecSpecialReviewCode);
        }
        if(vecSpecialReviewCodeDescriptions != null){
            specialCombo.setModel(new CoeusComboBox(
                                        vecSpecialReviewCodeDescriptions,
                                        false).getModel()); 
            specialCombo.addItemListener(new ItemListener(){
            public void itemStateChanged( ItemEvent item ){
                
                    // 3370: Special Review Tab - Start
//                    int row = tblSpecialReview.getSelectedRow();
//                    int col = tblSpecialReview.getSelectedColumn();
                    int row = tblSpecialReview.getEditingRow();
                    int col = tblSpecialReview.getEditingColumn();
                    // 3370: Special Review Tab - End
                    
                    ProtocolSpecialReviewFormBean pbean = null;
                    String prevValue = "";
                    if(vecSpecialReviewData != null && row != -1){
                        pbean = (ProtocolSpecialReviewFormBean)vecSpecialReviewData.elementAt(row);
                        if(pbean != null){
                            int code = pbean.getSpecialReviewCode();
                            prevValue = getDescriptionForId(code, vecSpecialReviewCode);
                            String curValue = ((JComboBox)item.getSource()).getSelectedItem().toString();
                            if(curValue != null) {//&& prevValue != null)
                                if(!curValue.equalsIgnoreCase(prevValue)){
                                    saveRequired = true;
                                    String stCode = getIDForName(curValue, vecSpecialReviewCode); 
                                    pbean.setSpecialReviewCode(new Integer(stCode).intValue());
                                    String aType = pbean.getAcType();
                                        if(!aType.equalsIgnoreCase(INSERT_RECORD)){
                                            pbean.setAcType(UPDATE_RECORD);
                                        }
                                    //((DefaultTableModel)tblSpecialReview.getModel()).setValueAt(curValue,row,col);  // Handle
                                }
                            }
                            //((DefaultTableModel)tblSpecialReview.getModel()).setValueAt(prevValue,row,col);  // Handle
                            //System.out.println("Bean Value is "+pbean.getSpecialReviewCode());
                        }
                    }
            }
        });
        }
    }
    
    /**
     * This method is used to set the available approval Types
     * @param approvalTypes collection of available approval Types
     */
    public void setApprovalTypes(Vector approvalTypes){
        
        this.vecApprovalTypeCode = approvalTypes;
        if(vecApprovalTypeCode != null){
            vecApprovalCodeDescriptions = getDescriptionsForCombo(vecApprovalTypeCode);
        }
        if(vecApprovalCodeDescriptions != null){
            approvalCombo.setModel(new CoeusComboBox(
                                        vecApprovalCodeDescriptions,
                                        false).getModel()); 
            approvalCombo.addItemListener(new ItemListener(){

            public void itemStateChanged( ItemEvent itemEvent ){ 
                
                // 3370: Special Review Tab - Start
//                int row = tblSpecialReview.getSelectedRow();
//                int col = tblSpecialReview.getSelectedColumn();
                int row = tblSpecialReview.getEditingRow();
                int col = tblSpecialReview.getEditingColumn();
                // 3370: Special Review Tab - End
                
                ProtocolSpecialReviewFormBean pbean = null;
                String prevValue = "";
                if(vecSpecialReviewData != null && row != -1){
                    pbean = (ProtocolSpecialReviewFormBean)vecSpecialReviewData.elementAt(row);
                    if(pbean != null){
                        int code = pbean.getApprovalCode();
                        prevValue = getDescriptionForId(code, vecApprovalTypeCode);
                        String curValue = ((JComboBox)itemEvent.getSource()).getSelectedItem().toString();
                        if(curValue != null) {
                            if(!curValue.equalsIgnoreCase(prevValue)){
                                saveRequired = true;
                                String stCode = getIDForName(curValue, vecApprovalTypeCode); 
                                pbean.setApprovalCode(new Integer(stCode).intValue());
                                String aType = pbean.getAcType();
                                if(!aType.equalsIgnoreCase(INSERT_RECORD)){
                                    pbean.setAcType(UPDATE_RECORD);
                                }
                                //((DefaultTableModel)tblSpecialReview.getModel()).setValueAt(curValue,row,col); // Handle
                            }
                        }
                        //((DefaultTableModel)tblSpecialReview.getModel()).setValueAt(prevValue,row,col); //// Handle
                    }
                }
            }
        });
        }
    }
    
    // This method is used to set the actype as Insert mode to the beans in amend mode. 
    
    private Vector setAcTypesInAmend(){
        Vector vecToSend = new Vector();
        if(vecSpecialReviewData != null){
            
            int size = vecSpecialReviewData.size();
            ProtocolSpecialReviewFormBean protocolReviewFormBean = null;
            for(int index = 0; index < size; index++){
                
                protocolReviewFormBean = (ProtocolSpecialReviewFormBean)vecSpecialReviewData.get(index);

                if( functionType == CoeusGuiConstants.AMEND_MODE ||
                        functionType == CoeusGuiConstants.ADD_MODE) {
                    if(protocolReviewFormBean.getAcType() == null 
                            ||  !protocolReviewFormBean.getAcType().equalsIgnoreCase(DELETE_RECORD)) {
                        protocolReviewFormBean.setAcType( INSERT_RECORD );
                        vecToSend.addElement( protocolReviewFormBean );
                    }
                }else{
                    vecToSend.addElement( protocolReviewFormBean );
                }
            }
            return vecToSend;
        }else{
            return vecSpecialReviewData;
        }
    }
    
    private void setAcTypes(){
        
        if(vecSpecialReviewData != null){
            
            int size = vecSpecialReviewData.size();
            ProtocolSpecialReviewFormBean protocolReviewFormBean = null;
            for(int index = 0; index < size; index++){
                
                protocolReviewFormBean = (ProtocolSpecialReviewFormBean)vecSpecialReviewData.get(index);
                
                if(protocolReviewFormBean != null){
                    
                    String acTye = protocolReviewFormBean.getAcType();
                    if(acTye == null){
                        protocolReviewFormBean.setAcType("null");
                    }
                }
            }
        }
    }

    /**
     * Method which form the String array. Each String ia a combination of 
     * Special Review Code, Approval Code, Protocol flag, Application Date Flag, Approval Date Flag
     */
    private String[] formValidateString(){
        SRApprovalInfoBean srBean = null;
        String[] validateStringCollection = null;
        if(vecValidateRules != null){
            int size = vecValidateRules.size();
            validateStringCollection = new String[size];
            String protocolFlag = null;
            String approvalDateFlag = null;
            String applicationDateFlag = null;
            int approvalCode;
            int specialReviewCode;
            for(int index = 0; index < size ; index++){
                srBean = (SRApprovalInfoBean)vecValidateRules.elementAt(index);
                String validate = null;
                if(srBean != null){
                    
                    specialReviewCode = srBean.getSpecialReviewCode();
                    approvalCode = srBean.getApprovalTypeCode();
                    protocolFlag = srBean.getProtocolFlag();
                    applicationDateFlag = srBean.getApplicationDateFlag();
                    approvalDateFlag = srBean.getApprovalDateFlag();
                                      
                    validate = ""+specialReviewCode + approvalCode + protocolFlag 
                                    + applicationDateFlag + approvalDateFlag;
                    validateStringCollection[index] = validate;
                    
                }
            }
        }
        return validateStringCollection;
    }
    
    /**
     * Method which gets the Combination of Protocol Flag, 
     * Application Date Flag, and Approval date Flag for the input combination 
     * of Special Review Code and Approval Code.
     */
    private String getValidateString(int specialCode , int approval){
        
        String[] validate = formValidateString();
        String paramString = "" + specialCode + approval;
         String resultString = null;
         if(validate != null){
            int size = validate.length;
            String stTemp = null;
            for(int index = 0; index < size; index++){
                stTemp = validate[index];
                   if(stTemp.startsWith(paramString)){
                  resultString = stTemp.substring(paramString.length());
                }
            }
        }
       return resultString;
    }
    
    /**
     * This Method validates the Table Row Data.
     * In a Row, For the selected combination of Approval Code and 
     * SPecial Review Code, this method performs the mandatory checking of the 
     * protocol number entry, Application date entry, and Approval Date entry.
     */
    private boolean validateRowData(int code , int aCode, String protocolNum, 
                                            String applDate, String apprDate) throws Exception{
        
        String stData =  getValidateString(code, aCode); 
        String protocolNumberFlag = null;
        String appliDateFlag = null;
        String arDateFlag = null;
        
        if(stData != null){
            
            protocolNumberFlag = ""+stData.charAt(0);
            appliDateFlag = ""+stData.charAt(1);
            arDateFlag = ""+stData.charAt(2);

            if(protocolNumberFlag != null){
                if(protocolNumberFlag.trim().equalsIgnoreCase("Y")) {
                    if(protocolNum == null || protocolNum.trim().length() <=0){
                        errorMessage(coeusMessageResources.parseMessageKey(
                        "protocol_SpecialReviewForm_exceptionCode.1304"));
                        tblSpecialReview.requestFocus();
                        return false;
                    }
                }
            }
            if(appliDateFlag != null){
                if(appliDateFlag.trim().equalsIgnoreCase("Y")) {
                    if(applDate == null || applDate.trim().length() <=0){
                        errorMessage(coeusMessageResources.parseMessageKey(
                            "protocol_SpecialReviewForm_exceptionCode.1305"));
                        tblSpecialReview.requestFocus();
                        return false;
                    }
                }
            }
            if(arDateFlag != null){
                if(arDateFlag.trim().equalsIgnoreCase("Y")) {
                    if(apprDate == null || apprDate.trim().length() <=0){
                        errorMessage(coeusMessageResources.parseMessageKey(
                            "protocol_SpecialReviewForm_exceptionCode.1306"));
                        //tblSpecialReview.requestFocus();
                        return false;
                    }
                }
            }
        }
        return true;
    }
    
   /** This method is used to show the alert messages to the user.
     * @param mesg is a message to alert the user.
     * @throws Exception is the <CODE>Exception</CODE> to throw in the client side.
     */
    
    private void errorMessage(String mesg) throws CoeusUIException{
        //Commented by sharath(20 - Aug - 2003)
        //throw new Exception(mesg);
        
        //Bug Fix ( Defect Id : 379)
        CoeusUIException coeusUIException = new CoeusUIException(mesg,CoeusUIException.WARNING_MESSAGE);
        //Modified for case 3552 - IRB attachments - start
        coeusUIException.setTabIndex(9);
        //Modified for case 3552 - IRB attachments - end
        throw coeusUIException;
        //Bug Fix ( Defect Id : 379)
    }
    
    
    // This method is used to set the listeners to the components.
    
    private void setListenersForButtons(){
        
        btnAdd.addActionListener(this);
        btnDelete.addActionListener(this);
        txtAreaComments.addFocusListener( this );
        
        ListSelectionModel specialReviewSelectionModel = tblSpecialReview.getSelectionModel();
        specialReviewSelectionModel.addListSelectionListener( this );
        specialReviewSelectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION );
        
    }
    
    // This method disables/ enables the components based on the function type.
    private void formatFields(){
        
        //code modified for coeus4.3 enhancements that UI to be in display mode
        //when new amendment or renewal is created
//        boolean enabled = functionType != DISPLAY_MODE ? true : false;        
        boolean enabled = (functionType == DISPLAY_MODE || 
                functionType == CoeusGuiConstants.AMEND_MODE) ? false : true;
        btnAdd.setEnabled(enabled);
        btnDelete.setEnabled(enabled);
        txtAreaComments.setEnabled(enabled);
        
        //Added by Amit 11/18/2003
        //code modified for coeus4.3 enhancements that UI to be in display mode
        //when new amendment or renewal is created
//        if(functionType == CoeusGuiConstants.DISPLAY_MODE){        
        if(functionType == CoeusGuiConstants.DISPLAY_MODE 
                || functionType == CoeusGuiConstants.AMEND_MODE){
            java.awt.Color bgListColor = (java.awt.Color)javax.swing.UIManager.getDefaults().get("Panel.background");
        
            tblSpecialReview.setBackground(bgListColor);    
            tblSpecialReview.setSelectionBackground(bgListColor );
            tblSpecialReview.setSelectionForeground(Color.black); 
            txtAreaComments.setBackground(bgListColor);    
        }
        else{
            tblSpecialReview.setBackground(Color.white);            
            tblSpecialReview.setSelectionBackground(Color.white);
            tblSpecialReview.setSelectionForeground(Color.black);            
            txtAreaComments.setBackground(Color.white);            
            
        }
        //end Amit        
    }
    // this method sets the data to the JTable.
    private void setFormData(){
        
        Vector vcDataPopulate = new Vector();
        Vector vcData=null;
        
        if((vecSpecialReviewData!= null) &&
            (vecSpecialReviewData.size() > 0)){
                
                for(int inCtrdata=0;
                inCtrdata < vecSpecialReviewData.size();
                inCtrdata++){
                    
                    protocolSpecialReviewFormBean = (ProtocolSpecialReviewFormBean)
                            vecSpecialReviewData.get(inCtrdata);

                    
                    String specialReviewDesc = protocolSpecialReviewFormBean.getSpecialReviewDescription();
                    
                    String approvalDesc = protocolSpecialReviewFormBean.getApprovalDescription();
                    
                    
                    int specialReviewNumber = protocolSpecialReviewFormBean.getSpecialReviewNumber();
                    
                    String protocolSpecialReviewNum = protocolSpecialReviewFormBean.getProtocolSPRevNumber();
                    
                    Date applicationDate = protocolSpecialReviewFormBean.getApplicationDate();
                    Date approvalDate = protocolSpecialReviewFormBean.getApprovalDate();
                    String comments = protocolSpecialReviewFormBean.getComments();                    

                    vcData= new Vector();
 
                    vcData.addElement("");
                    vcData.addElement(specialReviewDesc);
                    vcData.addElement(approvalDesc);
                    vcData.addElement(protocolSpecialReviewNum);
                    if(applicationDate != null){
                        vcData.addElement(dtUtils.formatDate(applicationDate.toString(),"dd-MMM-yyyy"));
                    }else{
                        vcData.addElement("");
                    }
                    if(approvalDate != null){
                        vcData.addElement(dtUtils.formatDate(approvalDate.toString(),"dd-MMM-yyyy"));
                    }else{
                        vcData.addElement("");
                    }
                    vcData.addElement(protocolSpecialReviewNum);
                    vcData.addElement("");
                    vcData.addElement("");
                    vcDataPopulate.addElement(vcData);
                    
                }
                    ((DefaultTableModel)tblSpecialReview.getModel()).
                        setDataVector(vcDataPopulate,getColumnNames());
                    ((DefaultTableModel)tblSpecialReview.getModel()).
                        fireTableDataChanged();
                        
                    // If the Table contains more than one row.. move the cursor to the first row..
                    //code commented for coeus4.3 concurrent Amendments/Renewals enhancement
//                    if( tblSpecialReview.getRowCount() > 0 ){
//
//                        tblSpecialReview.setRowSelectionInterval(0,0);                       
//                    }else{
//                        btnDelete.setEnabled(false);
//                    }
                    //code added for coeus4.3 concurrent Amendments/Renewals enhancement - starts
                    //To refresh the keypersons tab with new datas.                    
                    if( tblSpecialReview.getRowCount() > 0 
                            && tblSpecialReview.getRowCount()>tblSpecialReview.getSelectedRow()){
                        tblSpecialReview.setRowSelectionInterval(0,0);                       
                    } else if(tblSpecialReview.getRowCount() <= 0){
                        btnDelete.setEnabled(false);
                    }
            } else {
                ((DefaultTableModel)tblSpecialReview.getModel()).
                                        setDataVector(new Vector(), getColumnNames());            
            }
            //code added for coeus4.3 concurrent Amendments/Renewals enhancement - ends
    }
    
    // this method sets the Cell Editors to the JTable..
    private void setTableEditors(){
        
        ColumnValueEditor columnValueEditor = new ColumnValueEditor(20); //prps modified this from 8 to 20 - jan 09 2004
        ColumnValueRenderer columnValueRenderer = new ColumnValueRenderer();
        TableColumn column = tblSpecialReview.getColumnModel().getColumn(0);
        
        column.setMinWidth(30);
        column.setMaxWidth(30);
        column.setHeaderRenderer(new EmptyHeaderRenderer());
        
        txtAreaComments.setLineWrap(true);
        scrPnCommentsContainer.setViewportView(txtAreaComments);
        
        column.setResizable(false);
        column.setCellRenderer(new IconRenderer());
        column.setPreferredWidth(30);
        
        JTableHeader header = tblSpecialReview.getTableHeader();
        header.setReorderingAllowed(false);
        //header.setResizingAllowed(false);
        
        tblSpecialReview.setRowHeight(22);
        
        tblSpecialReview.setOpaque(false);
        tblSpecialReview.setShowVerticalLines(false);
        tblSpecialReview.setShowHorizontalLines(false);
        //tblSpecialReview.setSelectionBackground(Color.white);
        //tblSpecialReview.setSelectionForeground(Color.black);
        tblSpecialReview.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        
        tblSpecialReview.setSelectionMode(
        DefaultListSelectionModel.SINGLE_SELECTION);   
        
        column = tblSpecialReview.getColumnModel().getColumn(1);
        
        column.setMinWidth(220);
        //column.setMaxWidth(220);
        column.setPreferredWidth(220);
        //column.setResizable(false);
        //column.setCellRenderer(new ColumnValueRenderer());
        column.setCellEditor(new DefaultCellEditor(specialCombo ){
            public Object getCellEditorValue(){
                return ((JComboBox)getComponent()).getSelectedItem().toString();
            }
        });

        column = tblSpecialReview.getColumnModel().getColumn(2);
        
        column.setMinWidth(100);
        //column.setMaxWidth(100);
        column.setPreferredWidth(100);
        //column.setResizable(false);
        //column.setCellRenderer(columnValueRenderer);
        column.setCellEditor(new DefaultCellEditor(approvalCombo ){
            public Object getCellEditorValue(){
                return ((JComboBox)getComponent()).getSelectedItem().
                        toString();
            }
        });
        
        column = tblSpecialReview.getColumnModel().getColumn(3);
        column.setMinWidth(90);
        //column.setMaxWidth(90);
        column.setPreferredWidth(90);
        //column.setResizable(false);
        //column.setCellRenderer(columnValueRenderer);
        column.setCellEditor(columnValueEditor);

        column = tblSpecialReview.getColumnModel().getColumn(4);
        column.setMinWidth(90);
        //column.setMaxWidth(90);
        column.setPreferredWidth(90);
        //column.setResizable(false);
        column.setCellEditor(dateEditComponent);
        
        column = tblSpecialReview.getColumnModel().getColumn(5);
        column.setMinWidth(114);
        //column.setMaxWidth(114);
        column.setPreferredWidth(114);
        //column.setResizable(false);
        column.setCellEditor(dateEditComponent);
        
        column = tblSpecialReview.getColumnModel().getColumn(6);
        column.setMinWidth(0);
        //column.setMaxWidth(0);
        column.setPreferredWidth(0);
        
        column = tblSpecialReview.getColumnModel().getColumn(7);
        column.setMinWidth(0);
        //column.setMaxWidth(0);
        column.setPreferredWidth(0);
        
        column = tblSpecialReview.getColumnModel().getColumn(8);
        column.setMinWidth(0);
        //column.setMaxWidth(0);
        column.setPreferredWidth(0);
        
    }
    
    /* Helper method which gives vector of Special Review Descriptions */
    
    private Vector getDescriptionsForCombo(Vector vecDescValues) {
        
        ComboBoxBean comboEntry = null;
        Vector vecSpecialdesc = new Vector();
        
        Vector locSpecialType = vecDescValues;
        
        if( locSpecialType == null ){
            return vecDescValues;
        }else{
            int specialCodesSize = locSpecialType.size(); 
            for( int indx = 0; indx < specialCodesSize; indx++ ){
                comboEntry = ( ComboBoxBean ) locSpecialType.get( indx );
                if(comboEntry != null){
                    vecSpecialdesc.addElement( comboEntry.getDescription() );
                }
            }
            // To Insert a blank item in the Combo box
            vecSpecialdesc.insertElementAt("",0);
            return vecSpecialdesc;
        }
    }
    
    /* Helper method which gives available type Code value for the 
     * available type description selected in JComboBox
     */
    private String getIDForName( String selType, Vector vecTypeCodes ){

        String stTypeID = "1";
        ComboBoxBean comboEntry = null;
        Vector locSpecialType = vecTypeCodes;
        if( locSpecialType == null || selType == null ){
            return stTypeID;
        }
        int specialCodesSize = locSpecialType.size();
        for( int indx = 0; indx < specialCodesSize; indx++ ){
            
            comboEntry = ( ComboBoxBean ) locSpecialType.get( indx );
            if( ((String)comboEntry.getDescription()).equalsIgnoreCase(
                                                      selType)  ){
                stTypeID = comboEntry.getCode();
                break;
            }
        }        
        return stTypeID;
    }
    
    /* Helper method to get the description for the corresponding id in corresponding vector*/
    
    private String getDescriptionForId(int id, Vector vecTypeCodes){
        
        String stDesc = null;
        String stId = new String(""+id);
        ComboBoxBean comboEntry = null;
        if(vecTypeCodes != null){
            int specialCodesSize = vecTypeCodes.size();
            for( int indx = 0; indx < specialCodesSize; indx++ ){

                comboEntry = ( ComboBoxBean ) vecTypeCodes.get( indx );
                if( ((String)comboEntry.getCode()).equalsIgnoreCase(stId)  ){
                    stDesc = comboEntry.getDescription();
                    break;
                }
            }
        }
        return stDesc;
    }

    /**
     * This helper method is used to get all the column names of the JTable.
     */
    private Vector getColumnNames(){
        
        Enumeration enumColNames = tblSpecialReview.getColumnModel().getColumns();
        Vector vecColNames = new Vector();
        while(enumColNames.hasMoreElements()){
            String strName = (String)((TableColumn)
            enumColNames.nextElement()).getHeaderValue();
            vecColNames.addElement(strName);
        }
        return vecColNames;
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        pnlTableContainer = new javax.swing.JPanel();
        scrPnPane1 = new javax.swing.JScrollPane();
        tblSpecialReview = new javax.swing.JTable();
        pnlButtonsContainer = new javax.swing.JPanel();
        btnAdd = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        pnlComments = new javax.swing.JPanel();
        scrPnCommentsContainer = new javax.swing.JScrollPane();
        txtAreaComments = new javax.swing.JTextArea();
        lblSpecialReviewComments = new javax.swing.JLabel();

        setLayout(new java.awt.GridBagLayout());

        pnlTableContainer.setLayout(new java.awt.BorderLayout());

        pnlTableContainer.setMaximumSize(new java.awt.Dimension(660, 260));
        pnlTableContainer.setMinimumSize(new java.awt.Dimension(660, 260));
        pnlTableContainer.setPreferredSize(new java.awt.Dimension(660, 260));
        scrPnPane1.setBorder(new javax.swing.border.TitledBorder(null, "Special Reviews", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, CoeusFontFactory.getLabelFont()));
        scrPnPane1.setMaximumSize(new java.awt.Dimension(640, 250));
        scrPnPane1.setMinimumSize(new java.awt.Dimension(640, 250));
        scrPnPane1.setPreferredSize(new java.awt.Dimension(640, 250));
        tblSpecialReview.setFont(CoeusFontFactory.getNormalFont());
        tblSpecialReview.setModel(new CustomTableModel());
        tblSpecialReview.setShowHorizontalLines(false);
        scrPnPane1.setViewportView(tblSpecialReview);

        pnlTableContainer.add(scrPnPane1, java.awt.BorderLayout.CENTER);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 10;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(4, 3, 2, 3);
        add(pnlTableContainer, gridBagConstraints);

        pnlButtonsContainer.setLayout(new java.awt.GridBagLayout());

        btnAdd.setFont(CoeusFontFactory.getLabelFont());
        btnAdd.setMnemonic('A');
        btnAdd.setText("Add");
        btnAdd.setMaximumSize(new java.awt.Dimension(106, 26));
        btnAdd.setMinimumSize(new java.awt.Dimension(106, 26));
        btnAdd.setPreferredSize(new java.awt.Dimension(85, 26));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 4, 0);
        pnlButtonsContainer.add(btnAdd, gridBagConstraints);

        btnDelete.setFont(CoeusFontFactory.getLabelFont());
        btnDelete.setMnemonic('D');
        btnDelete.setText("Delete");
        btnDelete.setMaximumSize(new java.awt.Dimension(106, 26));
        btnDelete.setMinimumSize(new java.awt.Dimension(106, 26));
        btnDelete.setPreferredSize(new java.awt.Dimension(85, 26));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 4, 0);
        pnlButtonsContainer.add(btnDelete, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 10;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.RELATIVE;
        gridBagConstraints.gridheight = java.awt.GridBagConstraints.RELATIVE;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(14, 5, 2, 2);
        add(pnlButtonsContainer, gridBagConstraints);

        pnlComments.setLayout(new java.awt.GridBagLayout());

        pnlComments.setMaximumSize(new java.awt.Dimension(660, 130));
        pnlComments.setMinimumSize(new java.awt.Dimension(660, 130));
        pnlComments.setPreferredSize(new java.awt.Dimension(660, 130));
        scrPnCommentsContainer.setHorizontalScrollBarPolicy(javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrPnCommentsContainer.setMaximumSize(new java.awt.Dimension(660, 120));
        scrPnCommentsContainer.setMinimumSize(new java.awt.Dimension(660, 100));
        scrPnCommentsContainer.setPreferredSize(new java.awt.Dimension(660, 115));
        txtAreaComments.setDocument(new LimitedPlainDocument( 2000 ));
        txtAreaComments.setFont(CoeusFontFactory.getNormalFont());
        txtAreaComments.setLineWrap(true);
        txtAreaComments.setWrapStyleWord(true);
        scrPnCommentsContainer.setViewportView(txtAreaComments);

        pnlComments.add(scrPnCommentsContainer, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 10;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 10, 3);
        add(pnlComments, gridBagConstraints);

        lblSpecialReviewComments.setFont(CoeusFontFactory.getLabelFont());
        lblSpecialReviewComments.setText("  Special Review Comments :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 0, 0);
        add(lblSpecialReviewComments, gridBagConstraints);

    }//GEN-END:initComponents

    public void actionPerformed(java.awt.event.ActionEvent actionEvent) {
        Object actionSource = actionEvent.getSource();
        if(actionSource.equals(btnAdd)){
            try{
                if(tblSpecialReview.getCellEditor() != null){
                    tblSpecialReview.getCellEditor().cancelCellEditing();
                }
                Vector newRowData = new Vector();
                TableColumn clmTable = tblSpecialReview.getColumnModel().getColumn(1);
                clmTable.setCellEditor(new DefaultCellEditor(specialCombo ){
                    public Object getCellEditorValue(){
                        return ((JComboBox)getComponent()).getSelectedItem().toString();
                    }
                });
                
                clmTable = tblSpecialReview.getColumnModel().getColumn(2);
                //clmTable.setCellRenderer(new ColumnValueRenderer());
                clmTable.setCellEditor(new DefaultCellEditor(approvalCombo ){
                    public Object getCellEditorValue(){
                        return ((JComboBox)getComponent()).getSelectedItem().toString();
                    }
                });
                
                newRowData.addElement("");
                String specialDesc = (String)getDescriptionsForCombo(vecSpecialReviewCode).elementAt(0);
                String stSpecialCode = getIDForName(specialDesc, vecSpecialReviewCode);
                newRowData.addElement(specialDesc);
                String approvalDesc = (String)getDescriptionsForCombo(vecApprovalTypeCode).elementAt(0);
                String stApprovalCode = getIDForName(approvalDesc, vecApprovalTypeCode);
                newRowData.addElement(approvalDesc);
                
                newRowData.addElement("");
                newRowData.addElement("");
                newRowData.addElement("");
                newRowData.addElement("");
                newRowData.addElement("");
                newRowData.addElement("");
                
                int spCode = new Integer(stSpecialCode).intValue();
                int apCode = new Integer(stApprovalCode).intValue();
                
                // Create a new Bean and Add to Special Review Vector
                
                ProtocolSpecialReviewFormBean pBean = new ProtocolSpecialReviewFormBean();
                
                pBean.setAcType(INSERT_RECORD);
                pBean.setApprovalCode(apCode);
                pBean.setSpecialReviewCode(spCode);
                
                if(vecSpecialReviewData != null){
                    vecSpecialReviewData.addElement(pBean);
                }else{
                    vecSpecialReviewData = new Vector();
                    vecSpecialReviewData.addElement(pBean);
                }
                
                ((DefaultTableModel)tblSpecialReview.getModel()).addRow( newRowData );
                ((DefaultTableModel)tblSpecialReview.getModel()).fireTableDataChanged();
                
                int lastRow = tblSpecialReview.getRowCount() - 1;
                if(lastRow >= 0){
                    btnDelete.setEnabled(true);
                    txtAreaComments.setEnabled(true);
                    tblSpecialReview.setRowSelectionInterval( lastRow, lastRow );
                    txtAreaComments.setText("");
                    tblSpecialReview.scrollRectToVisible(tblSpecialReview.getCellRect(
                            lastRow ,ZERO_COUNT, true));
                }
                tblSpecialReview.requestFocusInWindow();
                tblSpecialReview.editCellAt(lastRow,1);
                tblSpecialReview.getEditorComponent().requestFocusInWindow();
                
                saveRequired=true;
                lastSelectedRow = lastRow;
            }catch(Exception e){
                e.printStackTrace();
            }
        }else if(actionSource.equals(btnDelete)){
            int totalRows = tblSpecialReview.getRowCount();
            if (totalRows > 0) {
                int selectedRow = tblSpecialReview.getSelectedRow();
                if (selectedRow != -1) {
                    int selectedOption = CoeusOptionPane.
                            showQuestionDialog(
                            coeusMessageResources.parseMessageKey(
                            "protocol_SpecialReviewForm_delConfirmCode.1050"),
                            CoeusOptionPane.OPTION_YES_NO,
                            CoeusOptionPane.DEFAULT_YES);
                    if (0 == selectedOption) {
                        ProtocolSpecialReviewFormBean protocolReviewBean = null;
                        
                        if(vecSpecialReviewData != null){
                            protocolReviewBean =
                                    (ProtocolSpecialReviewFormBean) vecSpecialReviewData.get( selectedRow );
                        }
                        
                        if( (protocolReviewBean.getAcType() != null ) &&
                                ( ! protocolReviewBean.getAcType().equalsIgnoreCase(INSERT_RECORD) )) {
                            
                            vecDeletedSpecialreviewCodes.addElement( protocolReviewBean );
                        }
                        
                        if( protocolReviewBean != null ){
                            
                            protocolReviewBean.setAcType( DELETE_RECORD );
                            saveRequired = true;
                        }
                        
                        vecSpecialReviewData.removeElementAt( selectedRow );
                        
                        
                        ((DefaultTableModel)
                        tblSpecialReview.getModel()).removeRow(selectedRow);
                        
                        ((DefaultTableModel)
                        tblSpecialReview.getModel()).fireTableDataChanged();
                        
                        saveRequired = true;
                        
                        int newRowCount = tblSpecialReview.getRowCount();
                        if(newRowCount == 0){
                            btnAdd.requestFocusInWindow();
                            btnDelete.setEnabled(false);
                            txtAreaComments.setEnabled(false);
                            txtAreaComments.setText("");
                        }else if (newRowCount > selectedRow) {
                            ProtocolSpecialReviewFormBean firstBean =
                                    (ProtocolSpecialReviewFormBean)vecSpecialReviewData.get(selectedRow);
                            if(firstBean != null){
                                txtAreaComments.setText( firstBean.getComments() );
                                txtAreaComments.setCaretPosition(0);
                            }
                            (tblSpecialReview.getSelectionModel())
                            .setSelectionInterval(selectedRow,
                                    selectedRow);
                            
                        } else {
                            tblSpecialReview.setRowSelectionInterval( newRowCount - 1,
                                    newRowCount -1 );
                            tblSpecialReview.scrollRectToVisible( tblSpecialReview.getCellRect(
                                    newRowCount - 1 ,
                                    ZERO_COUNT, true));
                            ProtocolSpecialReviewFormBean firstBean =
                                    (ProtocolSpecialReviewFormBean)vecSpecialReviewData.get( newRowCount -1 );
                            if(firstBean != null){
                                txtAreaComments.setText( firstBean.getComments() );
                                txtAreaComments.setCaretPosition(0);
                            }
                        }
                    }
                    
                }else{
                    CoeusOptionPane.
                            showErrorDialog(
                            coeusMessageResources.parseMessageKey(
                            "protoFndSrcFrm_exceptionCode.1057"));
                }
            }
        }
    }
    
    /**
     * On the TextArea Focus gained get the Comments from the text area.
     */
    public void focusGained(java.awt.event.FocusEvent focusEvent) {
        Object source = focusEvent.getSource();
        int selectedRow = tblSpecialReview.getSelectedRow();
        if( source.equals( txtAreaComments )){
            if( selectedRow == -1 && tblSpecialReview.getRowCount() > 0 ){
                showWarningMessage();
                tblSpecialReview.requestFocus();
            }
        }
        prvComments = txtAreaComments.getText();
        if(prvComments == null){
            prvComments = "";
        }
    }

    //supporting method to show the warning message
    private void showWarningMessage(){
        if( functionType != DISPLAY_MODE ) {
            CoeusOptionPane.showWarningDialog(
                                coeusMessageResources.parseMessageKey(
                                        "protocol_SpecialReviewForm_exceptionCode.1053"));
        }
    }
    private void updateComments(int selRow ) {
        ProtocolSpecialReviewFormBean prBean = null;
        if(vecSpecialReviewData != null && vecSpecialReviewData.size() > selRow){
           prBean = (ProtocolSpecialReviewFormBean )vecSpecialReviewData.get(selRow);
        }
        if(prBean != null){
            //Changes made check from equals to not equals
            String acTyp = prBean.getAcType();
            String curComments = txtAreaComments.getText().trim();
            String beanComments = prBean.getComments()==null?"":prBean.getComments();
            if( (!curComments.equalsIgnoreCase(beanComments)) ) {
                prBean.setComments( txtAreaComments.getText() );
                if( (acTyp == null) ||( !acTyp.equalsIgnoreCase(INSERT_RECORD) )){
                    prBean.setAcType( UPDATE_RECORD );
                }
                saveRequired = true;
            }
            if(vecSpecialReviewData != null){
                vecSpecialReviewData.setElementAt(prBean,selRow);
            }
        }

    }
    /**
     *  This method fires when ever focus is lost from the JTextArea. 
     *  It sets the changed value to the corresponding bean. 
     *  And sets the ACtype to the bean.
     */
    public void focusLost(java.awt.event.FocusEvent focusEvent) {
        //System.out.println("FocusLost Fired");
        if ( !focusEvent.isTemporary()) {
            Object source = focusEvent.getSource();
            int selectedRow = lastSelectedRow;
            if( source.equals( txtAreaComments ) &&
                        lastSelectedRow <= tblSpecialReview.getRowCount()  ){
                updateComments(selectedRow);        
            }
        }        
    }
    /**
     *  This method is fired whenever the table row is changed. 
     *  This method contains the implementation of changing the text area contents whenever table row is changed.
     */
    public void valueChanged(javax.swing.event.ListSelectionEvent listSelectionEvent) {
        
        int selectedRow = tblSpecialReview.getSelectedRow();
        
        String comment = null;
        int rowCount =  tblSpecialReview.getRowCount();
        if( selectedRow >= 0  && selectedRow <= rowCount &&
            firstEntry && vecSpecialReviewData != null){

                ProtocolSpecialReviewFormBean curBean = (ProtocolSpecialReviewFormBean)
                                       vecSpecialReviewData.get( selectedRow );
               updateComments(lastSelectedRow);
               lastSelectedRow = selectedRow;     
               if( curBean != null ){
                    comment = curBean.getComments();
                    if( curBean.getAcType() == null ){
                        //curBean.setAcType(UPDATE_RECORD);
                    }
                    if( comment == null){
                        comment = "";
                    }
                    txtAreaComments.setText( comment );
                    txtAreaComments.setCaretPosition(0);
                }
            }
        firstEntry = true ;
    }
    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel pnlTableContainer;
    private javax.swing.JButton btnAdd;
    private javax.swing.JPanel pnlComments;
    private javax.swing.JScrollPane scrPnPane1;
    private javax.swing.JTable tblSpecialReview;
    private javax.swing.JButton btnDelete;
    private javax.swing.JTextArea txtAreaComments;
    private javax.swing.JPanel pnlButtonsContainer;
    private javax.swing.JScrollPane scrPnCommentsContainer;
    private javax.swing.JLabel lblSpecialReviewComments;
    // End of variables declaration//GEN-END:variables
    
    /**
     * This is a Custom table model for the special review Jtable.
     */
    public class CustomTableModel extends DefaultTableModel{
        
        public CustomTableModel(){
            super(new Object[][]{}, new Object [] 
                {"Icon", "Special Review", "Approval", "Protocol No.", "Appl. Date", "Appr. Date", "SpecialRevNum", "SeqNo", "protocolNo"});
        }
        public boolean isCellEditable(int row, int col){
                //code modified for coeus4.3 enhancements that UI to be in display mode
                //when new amendment or renewal is created            
//                if((functionType == DISPLAY_MODE) || (col == 0)){
                if((functionType == DISPLAY_MODE) || (col == 0) 
                || (functionType == CoeusGuiConstants.AMEND_MODE)){
                    return false;
                }else{
                    return true;
                }
        }
        /* This method is invoked when ever the user changes the 
               contents in the table cell */
        public void fireTableCellUpdated(int row,int column){
            super.fireTableCellUpdated(row,column);
        }
        
        public Class getColumnClass(int col){
            return Object.class;
        }
    }
    
    /*
     * Inner class to set the editor for date columns/cells.
     */
    class DateEditor extends AbstractCellEditor implements TableCellEditor {

        private String colName;
        private static final String DATE_SEPARATERS = ":/.,|-";
        private static final String REQUIRED_DATEFORMAT = "dd-MMM-yyyy";
        private JTextField dateComponent = new JTextField();
        private String stDateValue;
        private int selectedRow;
        private int selectedColumn;
        boolean temporary;
        DateEditor(String colName) {
            this.colName = colName;
            ((JTextField)dateComponent).setFont(CoeusFontFactory.getNormalFont());
            dateComponent.addFocusListener(new FocusAdapter(){
                public void focusGained(FocusEvent fe){
                    temporary = false;
                }
                public void focusLost(FocusEvent fe){
                    if ( !fe.isTemporary()  ){
                        if(!temporary){
                            stopCellEditing();
                        }
                    }
                }
            });
        }
        
        /**
         * Date validation
         */

        private boolean validateEditorComponent(){

            temporary = true;
            String formattedDate = null;
            String editingValue = (String) getCellEditorValue();
            if (editingValue != null && editingValue.trim().length() > 0) {
                // validate date field
                formattedDate = new DateUtils().formatDate(editingValue,
                    DATE_SEPARATERS,REQUIRED_DATEFORMAT);
                //  Added by Chandra. Bug List IRB SystemTestingDL-01.xls Bug No. 23
                if(formattedDate == null && formattedDate!=REQUIRED_DATEFORMAT) {
                    // invalid date                    
                    CoeusOptionPane.showErrorDialog(
                        coeusMessageResources.parseMessageKey(
                            "memMntFrm_exceptionCode.1048"));
                    dateComponent.setText(stDateValue);
                    dateComponent.requestFocus();
                    return false;
                    //tblSpecialReview.editCellAt(selectedRow,selectedColumn);
                }else{
                    // valid date
                    dateComponent.setText(formattedDate);
                    if(!editingValue.equals(stDateValue)){
                        setModel(formattedDate);
                    }
                }
            }
            if( ((editingValue == null ) || (editingValue.trim().length()== 0 )) && 
                        (stDateValue != null) && (stDateValue.trim().length()>= 0 )){
                saveRequired = true;
                setModel(null);
            }
            return true;
        }
       
        
        // Sets the editing value to the Bean
        private void setModel(String formatDate){//editingValue
            saveRequired=true;
            String appDate = dtUtils.restoreDate(formatDate,"/:-,");
            ProtocolSpecialReviewFormBean pBean = 
                        (ProtocolSpecialReviewFormBean)vecSpecialReviewData.elementAt(selectedRow);
            String aType = pBean.getAcType();                
            try{
                if(selectedColumn == 4){
                    if(appDate != null){//editingValue    Handle
                        pBean.setApplicationDate(new java.sql.Date(dtFormat.parse(appDate).getTime()));
                    }else{
                        pBean.setApplicationDate(null);
                    }
                }else if(selectedColumn == 5){
                    if(appDate != null){
                        pBean.setApprovalDate(new java.sql.Date(dtFormat.parse(appDate).getTime()));
                    }else{
                        pBean.setApprovalDate(null);
                    }
                }
            }catch(Exception e){
                e.printStackTrace();
            }
            if(aType != null){
              if(!aType.equalsIgnoreCase(INSERT_RECORD)){
                     pBean.setAcType(UPDATE_RECORD);
              }
            }
        }
        
        public Component getTableCellEditorComponent(JTable table,Object value,
            boolean isSelected, int row,int column){

            selectedRow = row;
            selectedColumn = column;
            JTextField tfield =(JTextField)dateComponent;
            String currentValue = (String)value;
            String stTempValue =(String)tblSpecialReview.getValueAt(row,column);
            if(stTempValue != null){
                stDateValue = dtUtils.restoreDate(stTempValue,DATE_SEPARATERS) ;
            }
            if( ( currentValue != null  ) && (currentValue.trim().length()!= 0) ){
                String newValue = dtUtils.restoreDate(currentValue,
                    DATE_SEPARATERS) ;
                tfield.setText(newValue);
                return dateComponent;
            }

            tfield.setText( ((String)value));
            return dateComponent;
        }

        /**
         * Forwards the message from the CellEditor to the delegate.
         * @return true if editing was stopped; false otherwise
         */
        public boolean stopCellEditing() {
            if( validateEditorComponent()){
                return super.stopCellEditing();
            }
              return  false;
            //return super.stopCellEditing();
        }

        /** Returns the value contained in the editor.
         * @return the value contained in the editor
         */
        public Object getCellEditorValue() {
            return ((JTextField)dateComponent).getText();
        }

        /**
         * Invoked when an cell has been selected or deselected by the user.
         * The code written for this method performs the operations that need to
         * occur when an cell is selected (or deselected).
         * @param e an ItemEvent.
         */
        public void itemStateChanged(ItemEvent e) {
            super.fireEditingStopped();
        }
        
        public int getClickCountToStart(){
            return 1;
        }
    }
    
    /**
     * This Class is used as a Renderer to all the Column Cells.
     */
    public class ColumnValueRenderer extends JTextField implements TableCellRenderer {
       
        private int selRow;
        private int selCol;
        public ColumnValueRenderer() {
          setOpaque(true);
          setBorder(new EmptyBorder(0,0,0,0));
          tblSpecialReview.editCellAt(selRow,selCol);
          setFont(CoeusFontFactory.getNormalFont());
        }

        public Component getTableCellRendererComponent(JTable table, 
            Object value, boolean isSelected, boolean hasFocus, int row, int column) {
              
              selRow = row;
              selCol = column;
              setText( (value == null) ? "" : value.toString() );
              return this;
        }
    }
    
   /**
    * This is a Default Cell Editor  JTectField Component for the protocol Number.
    */
    class ColumnValueEditor extends DefaultCellEditor
                                                implements TableCellEditor {
        private JTextField txtDesc;
        private int selectedRow ;
        private int selectedCol ;
        private String stProtocolSPRevNumber;
        // Constructor which sets the size of TextField
        ColumnValueEditor(int len ){
            
            super(new JTextField());
            txtDesc = new JTextField();
            txtDesc.setFont(CoeusFontFactory.getNormalFont());
            txtDesc.setDocument(new LimitedPlainDocument(len));
            txtDesc.addFocusListener(new FocusAdapter(){
                public void focusLost(FocusEvent fe){
                    
                    if (!fe.isTemporary() ){
                       stopCellEditing();
                       tblSpecialReview.setRowSelectionInterval(selectedRow,selectedRow);
                    }
                }
            });
        }
        // This method sets the edited value in the bean.
        
        private void setEditorValueToBean(String editingValue){
            ProtocolSpecialReviewFormBean pBean = null;
            if( (editingValue == null )){// || (editingValue.trim().length()== 0 )
                /*((JTextField)txtDesc).setText( editingValue);
                //((DefaultTableModel)tblSpecialReview.getModel()).setValueAt(editingValue,selectedRow,selectedCol); // Handle
                tblSpecialReview.setRowSelectionInterval(selectedRow, selectedRow);*/
                ((JTextField)txtDesc).setText( "");
                ((DefaultTableModel)tblSpecialReview.getModel()).setValueAt("",selectedRow,selectedCol); // Handle
                tblSpecialReview.setRowSelectionInterval(selectedRow, selectedRow);                
            }else{
                    ((JTextField)txtDesc).setText( editingValue);
                    if(!editingValue.equalsIgnoreCase(stProtocolSPRevNumber)){
                        saveRequired = true;
                        
                        if(vecSpecialReviewData != null){

                            pBean = (ProtocolSpecialReviewFormBean)vecSpecialReviewData.elementAt(selectedRow);
                            if(pBean != null){
                                pBean.setProtocolSPRevNumber(editingValue);
                                String aType = pBean.getAcType();

                                if (aType != null){
                                        if(!aType.equalsIgnoreCase(INSERT_RECORD)) {
                                            pBean.setAcType( UPDATE_RECORD );
                                            saveRequired = true;
                                        }
                                    }
                                if(vecSpecialReviewData != null){
                                    vecSpecialReviewData.setElementAt(pBean,selectedRow);
                                }
                            }
                        }
                        //((DefaultTableModel)tblSpecialReview.getModel()).setValueAt(editingValue,selectedRow,selectedCol);// Handle
                        ((DefaultTableModel)tblSpecialReview.getModel()).setValueAt(editingValue,selectedRow,selectedCol);// Handle
                    }else{
                        ((DefaultTableModel)tblSpecialReview.getModel()).setValueAt(stProtocolSPRevNumber,selectedRow,selectedCol);// Handle
                    }
            }
        }
        
        public Component getTableCellEditorComponent(JTable table,
        Object value,
        boolean isSelected,
        int row,
        int column){
            
            selectedRow = row;
            selectedCol = column;
            stProtocolSPRevNumber = (String)tblSpecialReview.getValueAt(row,3);
            if(stProtocolSPRevNumber == null){
                stProtocolSPRevNumber = "";
            }
            String newValue = ( String ) value ;
            if( newValue != null && newValue.length() > 0 ){
                txtDesc.setText( (String)newValue );
            }else{
                txtDesc.setText("");
            }
            this.selectedRow = row;
            return txtDesc;
        }

        public int getClickCountToStart(){
            return 1;
        }
        
        /** Returns the value contained in the editor.
         * @return the value contained in the editor
         */
        public Object getCellEditorValue() {
            return ((JTextField)txtDesc).getText();
        }
        
        public boolean stopCellEditing() {
            String editingValue = (String)getCellEditorValue();
            setEditorValueToBean(editingValue);
            ProtocolSpecialReviewFormBean pBean = null;
            if( (editingValue == null )){// || (editingValue.trim().length()== 0 )
                ((JTextField)txtDesc).setText( editingValue);
                //((DefaultTableModel)tblSpecialReview.getModel()).setValueAt(editingValue,selectedRow,selectedCol); // Handle
                tblSpecialReview.setRowSelectionInterval(selectedRow, selectedRow);
                return super.stopCellEditing();
            }else{
                    if(!editingValue.equalsIgnoreCase(stProtocolSPRevNumber)){
                        saveRequired = true;
                        
                        if(vecSpecialReviewData != null){
                            pBean = (ProtocolSpecialReviewFormBean)vecSpecialReviewData.elementAt(selectedRow);
                            if(pBean != null){
                                pBean.setProtocolSPRevNumber(editingValue);
                                String aType = pBean.getAcType();
                                if (aType != null){
                                        if(!aType.equalsIgnoreCase(INSERT_RECORD)) {
                                            pBean.setAcType( UPDATE_RECORD );
                                            saveRequired = true;
                                        }
                                    }
                                if(vecSpecialReviewData != null){
                                    vecSpecialReviewData.setElementAt(pBean,selectedRow);
                                }
                            }
                        }
                        //((DefaultTableModel)tblSpecialReview.getModel()).setValueAt(editingValue,selectedRow,selectedCol);// Handle
                    }
                    //((DefaultTableModel)tblSpecialReview.getModel()).setValueAt(stProtocolSPRevNumber,selectedRow,selectedCol);// Handle
                    
            }
            ((JTextField)txtDesc).setText( editingValue);
            if(selectedRow != -1){
                tblSpecialReview.setRowSelectionInterval(selectedRow, selectedRow);
            }
            return super.stopCellEditing();
        }

        protected void fireEditingStopped() {
          super.fireEditingStopped();
        }
    }        
}
