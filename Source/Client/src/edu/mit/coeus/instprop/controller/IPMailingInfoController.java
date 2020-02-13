/** Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

/*
 * IPMailingInfoController.java
 *
 * Created on April 19, 2004, 11:40 AM
 */
package edu.mit.coeus.instprop.controller;
/**
 *
 * @author  bijosh
 */

import edu.mit.coeus.bean.CoeusParameterBean;
import edu.mit.coeus.exception.*;
import edu.mit.coeus.gui.CoeusAppletMDIForm;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.gui.event.*;
import edu.mit.coeus.instprop.bean.*;
import edu.mit.coeus.instprop.controller.InstituteProposalController;
import edu.mit.coeus.instprop.gui.IPMailingInfoForm;
import edu.mit.coeus.rolodexmaint.gui.RolodexMaintenanceDetailForm;
import edu.mit.coeus.utils.query.QueryEngine;
import edu.mit.coeus.utils.query.Equals;
import edu.mit.coeus.utils.*;
import edu.mit.coeus.search.gui.CoeusSearch;

import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.StringTokenizer;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;


/**
 * Cretes an instance of IPMailingInfoController
 */
public class IPMailingInfoController extends InstituteProposalController 
implements ActionListener, BeanUpdatedListener {
    private static final String EMPTY_STRING = "";
    private InstituteProposalBaseBean instituteProposalBaseBean;
    private static final char POSTMARK='P';
    private static final char RECEIPT='R';
    private static final char OSP='O';
    private static final char DEPARTMENT='D';
    private static final char REGULAR='R';
    private static final char DHL='D';
    private static final char ELECTRONIC='E';
    private static final char NONE=' ';
    private static final String DATE_SEPARATERS = ":/.,|-";
    private static final String REQUIRED_DATEFORMAT = "dd-MMM-yyyy";
    private static final String SIMPLE_DATE_FORMAT = "MM/dd/yyyy";
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat(SIMPLE_DATE_FORMAT);
    private static final char ROLODEX_DISPLAY_MODE='V';
    private boolean isCommentPresent=false;

    private CoeusVector cvCommentDescription;
    private DateUtils dtUtils =new DateUtils();
    private int mailingAddressId;
    private IPMailingInfoForm iPMailingInfoForm;
    private QueryEngine queryEngine;
    private char functionType;
    private InstituteProposalBean instituteProposalBean; 
    private CoeusVector cvMailingInfo;
    private CoeusAppletMDIForm mdiForm;
    private CoeusVector cvParameters;
    private InstituteProposalCommentsBean commentsBean; 
    private boolean modified=false;
    private java.text.SimpleDateFormat dtFormat
    = new java.text.SimpleDateFormat("MM/dd/yyyy");
    private CoeusMessageResources coeusMessageResources;
    private int accountNumberMaxLength = 0;
    
    /** Creates a new instance of IPMailingInfoController */
    public IPMailingInfoController(InstituteProposalBaseBean instituteProposalBaseBean, char functionType) {
        super(instituteProposalBaseBean);
        this.functionType=functionType;
        queryEngine = QueryEngine.getInstance();
        coeusMessageResources = CoeusMessageResources.getInstance();
        iPMailingInfoForm = new IPMailingInfoForm();
        registerComponents();
        setFormData(instituteProposalBaseBean);
        showCommentMissingMessage();
        formatFields();
    }
    
    /**
     * Registers listeners and components depending on the mode.
     */    
    public  void registerComponents(){
        if (functionType!=DISPLAY_PROPOSAL) {
            iPMailingInfoForm.btnAddRolodex.addActionListener(this);
            iPMailingInfoForm.btnRemoveRolodex.addActionListener(this);
            if (functionType !=DISPLAY_PROPOSAL) {
                addBeanUpdatedListener(this, InstituteProposalBean.class);
            }
        }
        iPMailingInfoForm.rdBtnDepartment.addActionListener(this);
        iPMailingInfoForm.rdBtnOSP.addActionListener(this);
        if (functionType != DISPLAY_PROPOSAL ) {
            iPMailingInfoForm.txtDate.addFocusListener(new CustomFocusAdapter());
        }
        iPMailingInfoForm.txtAddressName.addMouseListener(new CustomMouseAdapter());
        mdiForm=CoeusGuiConstants.getMDIForm();
    }
    
    /**
     * Displays the message that the comment code is missing if comment is not present
     */    
    public void showCommentMissingMessage() {
        if (!isCommentPresent) {
             if (functionType!=NEW_INST_PROPOSAL) {
                    // Missing proposal comment code in OSP$PARAMETER table
                    CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey("instPropBase_exceptionCode.1004"));
             }
        }
    }
    
    /**
     * Sets the focus to the component when the window is opened
     */    
    public void setDefaultFocusForComponent(){
        if( functionType!= DISPLAY_PROPOSAL) {
            iPMailingInfoForm.txtDate.requestFocusInWindow();
        }
    }    
    
    /**
     * Sets the data for the form
     */    
    public void setFormData(Object instituteProposalBaseBean) {
        this.instituteProposalBaseBean=(InstituteProposalBaseBean)instituteProposalBaseBean;
        cvMailingInfo = new CoeusVector();
        
        iPMailingInfoForm.txtAccount.setText(EMPTY_STRING);
        iPMailingInfoForm.txtArAddress.setText(EMPTY_STRING);
        iPMailingInfoForm.txtArComments.setText(EMPTY_STRING);
        iPMailingInfoForm.txtAddressName.setText(EMPTY_STRING);
        iPMailingInfoForm.txtNumberOfCopies.setText(EMPTY_STRING);
        iPMailingInfoForm.rdBtnExtra.setVisible(false);
        iPMailingInfoForm.rdBtnExtraMailBy.setVisible(false);
        iPMailingInfoForm.rdBtnExtraDeadline.setVisible(false);
        iPMailingInfoForm.rdBtnExtra.setSelected(true);
        iPMailingInfoForm.rdBtnExtraMailBy.setSelected(true);
        iPMailingInfoForm.rdBtnExtraDeadline.setSelected(true);
        iPMailingInfoForm.txtAddressName.setEnabled(false);
        iPMailingInfoForm.txtArAddress.setEnabled(false);
        iPMailingInfoForm.txtArAddress.setDisabledTextColor(java.awt.Color.black);
        iPMailingInfoForm.txtAddressName.setDisabledTextColor(java.awt.Color.black);
        try {
            cvMailingInfo = queryEngine.executeQuery(queryKey, InstituteProposalBean.class, CoeusVector.FILTER_ACTIVE_BEANS);
            if(cvMailingInfo!= null && cvMailingInfo.size() >0){
                instituteProposalBean = (InstituteProposalBean)cvMailingInfo.get(0);
            }else{
                instituteProposalBean = new InstituteProposalBean();
            }
            cvParameters = queryEngine.executeQuery(queryKey,CoeusParameterBean.class,CoeusVector.FILTER_ACTIVE_BEANS);
            //Modified for Case#2402- use a parameter to set the length of the account number throughout app - Start
//            for (int index=0;index<cvParameters.size();index++) {
//                CoeusParameterBean coeusParameterBean=(CoeusParameterBean)cvParameters.elementAt(index);
//                if(CoeusConstants.PROPOSAL_COMMENT_CODE.equals(coeusParameterBean.getParameterName())){
//                    isCommentPresent=true;
//                    //Modified for Case#2402- use a parameter to set the length of the account number throughout app - Start
//                    //break;
//                }
//            }
            //To get the INDIRECT_COST_COMMENT_CODE parameter
            if(cvParameters != null && cvParameters.size()>0){
                CoeusVector cvProposalCommentCode = cvParameters.filter(new Equals("parameterName", CoeusConstants.PROPOSAL_COMMENT_CODE));
                
                if(cvProposalCommentCode != null && cvProposalCommentCode.size()>0){
                    isCommentPresent = true;
                }
                //To get the MAX_ACCOUNT_NUMBER_LENGTH parameter
                CoeusVector cvAccountNumberLimit = cvParameters.filter(new Equals("parameterName", CoeusConstants.MAX_ACCOUNT_NUMBER_LENGTH));
                if(cvAccountNumberLimit != null
                        && cvAccountNumberLimit.size() > 0){
                    CoeusParameterBean parameterBean = (CoeusParameterBean)cvAccountNumberLimit.get(0);
                    accountNumberMaxLength = Integer.parseInt(parameterBean.getParameterValue());
                }
            }
            //Sets the AccountNumber length based on accountNumLength value and allow the field to
            //accept alphanumeric with comma,hyphen and periods
            iPMailingInfoForm.txtAccount.setDocument(new JTextFieldFilter((JTextFieldFilter.ALPHA_NUMERIC+JTextFieldFilter.COMMA_HYPHEN_PERIOD),accountNumberMaxLength));
            //Case#2402 - End
            
            if (!isCommentPresent) {
                iPMailingInfoForm.txtArComments.setEditable(false);
                iPMailingInfoForm.txtArComments.setOpaque(false);
            }else {
                if (functionType != DISPLAY_PROPOSAL) {
                    iPMailingInfoForm.txtArComments.setEditable(true);
                    iPMailingInfoForm.txtArComments.setOpaque(true);
                }
                cvCommentDescription=new CoeusVector();
                cvCommentDescription=queryEngine.getDetails(queryKey,InstituteProposalCommentsBean.class);                                
                if (cvCommentDescription!= null && cvCommentDescription.size()>0) {
                    //CoeusVector return
                    CoeusVector cvMailingInfoCommentCode = cvParameters.filter(new Equals("parameterName", CoeusConstants.PROPOSAL_COMMENT_CODE));
                    CoeusParameterBean coeusParameterBean = null;
                    coeusParameterBean = (CoeusParameterBean)cvMailingInfoCommentCode.elementAt(0);

                    Equals equals = new Equals("commentCode", new Integer(coeusParameterBean.getParameterValue()));
                    cvCommentDescription = cvCommentDescription.filter(equals);
                    if(cvCommentDescription!=null && cvCommentDescription.size() > 0){
                        this.commentsBean=(InstituteProposalCommentsBean)cvCommentDescription.elementAt(0);
                        iPMailingInfoForm.txtArComments.setText(this.commentsBean.getComments());
                        iPMailingInfoForm.txtArComments.setCaretPosition(0);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }            
        if (instituteProposalBean.getDeadLineDate()!=null) {
            iPMailingInfoForm.txtDate.setText(dtUtils.formatDate(
                    Utils.convertNull(instituteProposalBean.
                    getDeadLineDate().toString()), REQUIRED_DATEFORMAT));
        } else {
            iPMailingInfoForm.txtDate.setText(EMPTY_STRING);
        }
        
        if (instituteProposalBean.getDeadLineType()==POSTMARK) {
            iPMailingInfoForm.rdBtnPostmark.setSelected(true);
            iPMailingInfoForm.rdBtnReceipt.setSelected(false);
        } else if (instituteProposalBean.getDeadLineType()==RECEIPT){
            iPMailingInfoForm.rdBtnPostmark.setSelected(false);
            iPMailingInfoForm.rdBtnReceipt.setSelected(true);            
        }
        if (OSP==instituteProposalBean.getMailBy()) {
            iPMailingInfoForm.rdBtnOSP.setSelected(true);
            iPMailingInfoForm.rdBtnDepartment.setSelected(false);
            if (instituteProposalBean.getMailType()==REGULAR) {
                iPMailingInfoForm.rdBtnRegular.setSelected(true);
                iPMailingInfoForm.rdBtnDHL.setSelected(false);
                iPMailingInfoForm.rdBtnElectronic.setSelected(false);            
            } else if (instituteProposalBean.getMailType()==DHL) {
                iPMailingInfoForm.rdBtnRegular.setSelected(false);
                iPMailingInfoForm.rdBtnDHL.setSelected(true);
                iPMailingInfoForm.rdBtnElectronic.setSelected(false);                        
            } else if(instituteProposalBean.getMailType()==ELECTRONIC) {
                iPMailingInfoForm.rdBtnRegular.setSelected(false);
                iPMailingInfoForm.rdBtnDHL.setSelected(false);
                iPMailingInfoForm.rdBtnElectronic.setSelected(true);                                    
            }
            if (instituteProposalBean.getMailAccountNumber()!=null) {
                //Modified for Case#2402- use a parameter to set the length of the account number throughout app - Start
//                iPMailingInfoForm.txtAccount.setText(instituteProposalBean.getMailAccountNumber());
                String mailAccountNumber = instituteProposalBean.getMailAccountNumber().trim();
                if(mailAccountNumber.length() > accountNumberMaxLength){
                    mailAccountNumber = mailAccountNumber.substring(0,accountNumberMaxLength);
                }
                iPMailingInfoForm.txtAccount.setText(mailAccountNumber);
                //Case#2402 - End
            }else {
                iPMailingInfoForm.txtAccount.setText(CoeusGuiConstants.EMPTY_STRING);
            }
        } else if (instituteProposalBean.getMailBy()==DEPARTMENT) {
            iPMailingInfoForm.txtAccount.setEnabled(false);
            iPMailingInfoForm.txtAccount.setText(EMPTY_STRING);
            iPMailingInfoForm.rdBtnOSP.setSelected(false);
            iPMailingInfoForm.rdBtnDepartment.setSelected(true); 
            iPMailingInfoForm.rdBtnRegular.setSelected(false);
            iPMailingInfoForm.rdBtnDHL.setSelected(false);
            iPMailingInfoForm.rdBtnElectronic.setSelected(false); 
            iPMailingInfoForm.rdBtnRegular.setEnabled(false);
            iPMailingInfoForm.rdBtnDHL.setEnabled(false);
            iPMailingInfoForm.rdBtnElectronic.setEnabled(false); 
        }else {
            if (instituteProposalBean.getMailType()==REGULAR) {
                iPMailingInfoForm.rdBtnRegular.setSelected(true);
                iPMailingInfoForm.rdBtnDHL.setSelected(false);
                iPMailingInfoForm.rdBtnElectronic.setSelected(false);            
            } else if (instituteProposalBean.getMailType()==DHL) {
                iPMailingInfoForm.rdBtnRegular.setSelected(false);
                iPMailingInfoForm.rdBtnDHL.setSelected(true);
                iPMailingInfoForm.rdBtnElectronic.setSelected(false);                        
            } else if(instituteProposalBean.getMailType()==ELECTRONIC) {
                iPMailingInfoForm.rdBtnRegular.setSelected(false);
                iPMailingInfoForm.rdBtnDHL.setSelected(false);
                iPMailingInfoForm.rdBtnElectronic.setSelected(true);                                    
            }      
            if (instituteProposalBean.getMailAccountNumber()!=null) {
                //iPMailingInfoForm.txtAccount.setText(instituteProposalBean.getMailAccountNumber());
                //Modified for Case#2402- use a parameter to set the length of the account number throughout app - Start
//                iPMailingInfoForm.txtAccount.setText(instituteProposalBean.getMailAccountNumber());
                String mailAccountNumber = instituteProposalBean.getMailAccountNumber().trim();
                if(mailAccountNumber.length() > accountNumberMaxLength){
                    mailAccountNumber = mailAccountNumber.substring(0,accountNumberMaxLength);
                }
                iPMailingInfoForm.txtAccount.setText(mailAccountNumber);
                //Case#2402 - End
            }else {
                iPMailingInfoForm.txtAccount.setText(CoeusGuiConstants.EMPTY_STRING);
            }
            
        }
        if(instituteProposalBean.getNumberOfCopies()!=null) {
            iPMailingInfoForm.txtNumberOfCopies.setText(instituteProposalBean.getNumberOfCopies());
        }
        else{
            iPMailingInfoForm.txtNumberOfCopies.setText(EMPTY_STRING);
        }
        mailingAddressId=instituteProposalBean.getRolodexId();
        iPMailingInfoForm.txtAddressName.setText(instituteProposalBean.getRolodexName());
        if (instituteProposalBean.getMailingAddress()!=null) {
            iPMailingInfoForm.txtArAddress.setText(formatMailingAddress(instituteProposalBean.getMailingAddress()));
        }
    }
    
    public void display() {
    }
    
    /**
     * Perform field formatting.
     * enabling, disabling components depending on the
     * function type.
     */    
    public void formatFields() {
        if(functionType== DISPLAY_PROPOSAL){
            iPMailingInfoForm.txtAccount.setEditable(false);
            iPMailingInfoForm.txtDate.setEditable(false);
            iPMailingInfoForm.txtNumberOfCopies.setEditable(false);
            iPMailingInfoForm.txtArAddress.setEditable(false);
            iPMailingInfoForm.txtArAddress.setOpaque(false);
            iPMailingInfoForm.txtArComments.setEditable(false);
            iPMailingInfoForm.txtArComments.setOpaque(false);
            iPMailingInfoForm.txtAddressName.setEditable(false);
            iPMailingInfoForm.rdBtnDHL.setEnabled(false);
            iPMailingInfoForm.rdBtnDepartment.setEnabled(false);
            iPMailingInfoForm.rdBtnElectronic.setEnabled(false);
            iPMailingInfoForm.rdBtnOSP.setEnabled(false);
            iPMailingInfoForm.rdBtnPostmark.setEnabled(false);
            iPMailingInfoForm.rdBtnReceipt.setEnabled(false);
            iPMailingInfoForm.rdBtnRegular.setEnabled(false);
            iPMailingInfoForm.btnAddRolodex.setEnabled(false);
            iPMailingInfoForm.btnRemoveRolodex.setEnabled(false);
        }
    }
    
    /**
     * returns the Component
     */    
    public java.awt.Component getControlledUI() {
        return iPMailingInfoForm;
    }
    
    /**
     * Returns the form data
     */    
    public Object getFormData() {
        return null;
    }
    /**
     * Saves the data
     */    
    public void saveFormData() {
        Date modifiedDobDate;
        String dateValue;
        Date date;
        try{
            dateValue = iPMailingInfoForm.txtDate.getText().trim();
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
                     instituteProposalBean.setDeadLineDate(new java.sql.Date(date.getTime()));
                 }
                }else {
                    date = simpleDateFormat.parse(dtUtils.formatDate(dateValue,DATE_SEPARATERS, SIMPLE_DATE_FORMAT));
                    instituteProposalBean.setDeadLineDate(new java.sql.Date(date.getTime()));
                }
            }else{
                instituteProposalBean.setDeadLineDate(null);
            }
            if (iPMailingInfoForm.rdBtnPostmark.isSelected()) {
                instituteProposalBean.setDeadLineType(POSTMARK);
            } else if (iPMailingInfoForm.rdBtnReceipt.isSelected()) {
                instituteProposalBean.setDeadLineType(RECEIPT);
            } else {
                instituteProposalBean.setDeadLineType(NONE);
            }

            if (iPMailingInfoForm.rdBtnOSP.isSelected()) {
                instituteProposalBean.setMailBy(OSP);
            } else if (iPMailingInfoForm.rdBtnDepartment.isSelected()){
                instituteProposalBean.setMailBy(DEPARTMENT);
            } else {
                instituteProposalBean.setMailBy(NONE);
            }
            if (iPMailingInfoForm.rdBtnRegular.isSelected()) {
                instituteProposalBean.setMailType(REGULAR);
            } else if (iPMailingInfoForm.rdBtnDHL.isSelected()) {
                instituteProposalBean.setMailType(DHL);
            } else if (iPMailingInfoForm.rdBtnElectronic.isSelected()) {
                instituteProposalBean.setMailType(ELECTRONIC);
            } else {
                instituteProposalBean.setMailType(NONE);
            }
            if (iPMailingInfoForm.txtAccount.getText() !=null && !(iPMailingInfoForm.txtAccount.getText().trim().equals(EMPTY_STRING))) {
                instituteProposalBean.setMailAccountNumber(iPMailingInfoForm.txtAccount.getText().trim());
            }else{
                instituteProposalBean.setMailAccountNumber(null);
            }
            if (!iPMailingInfoForm.txtNumberOfCopies.getText().trim().equals(EMPTY_STRING)) {
                instituteProposalBean.setNumberOfCopies(iPMailingInfoForm.txtNumberOfCopies.getText());
            }else {
                instituteProposalBean.setNumberOfCopies(null);
            }

            if (mailingAddressId == 0) {
               instituteProposalBean.setRolodexId(0);
               instituteProposalBean.setRolodexName(EMPTY_STRING);
               //instituteProposalBean.setMailingAddress(EMPTY_STRING);
            }else{
                instituteProposalBean.setRolodexId(mailingAddressId);
                instituteProposalBean.setRolodexName(iPMailingInfoForm.txtAddressName.getText());
                //instituteProposalBean.setMailingAddress(iPMailingInfoForm.txtArAddress.getText());
            }
            StrictEquals stBeanEquals = new StrictEquals(); 
            InstituteProposalBean qryBean=new InstituteProposalBean();
            CoeusVector cvOrginalData = queryEngine.getDetails(queryKey, InstituteProposalBean.class);
            if(cvOrginalData!= null && cvOrginalData.size() >0){
                qryBean=(InstituteProposalBean)cvOrginalData.elementAt(0);
            }
            boolean isDataSame=stBeanEquals.compare(instituteProposalBean,qryBean);
            if (!isDataSame) {
                modified=true;
            }
            if (modified) {
                if(functionType == CORRECT_INST_PROPOSAL) {
                        instituteProposalBean.setAcType(TypeConstants.UPDATE_RECORD);
                        queryEngine.update(queryKey, instituteProposalBean);
                }else{
                        instituteProposalBean.setAcType(TypeConstants.INSERT_RECORD);
                        queryEngine.insert(queryKey, instituteProposalBean);
                }
                //Fire Proposal Details Modified event
                BeanEvent beanEvent = new BeanEvent();
                beanEvent.setSource(this);
                beanEvent.setBean(instituteProposalBean);
                fireBeanUpdated(beanEvent);
            }
            StrictEquals stCommentsEquals = new StrictEquals();
            InstituteProposalCommentsBean queryCommentsBean = new InstituteProposalCommentsBean();
            CoeusVector cvTempComment = queryEngine.getDetails(queryKey, InstituteProposalCommentsBean.class);
            cvParameters = queryEngine.executeQuery(queryKey,CoeusParameterBean.class,CoeusVector.FILTER_ACTIVE_BEANS);
            CoeusVector cvMailingInfoComment = null;
            CoeusParameterBean coeusParameterBean = null;
            CoeusVector cvMailingInfoCommentCode = cvParameters.filter(new Equals("parameterName", CoeusConstants.PROPOSAL_COMMENT_CODE));
            if(cvMailingInfoCommentCode!=null && cvMailingInfoCommentCode.size() > 0){
                coeusParameterBean = (CoeusParameterBean)cvMailingInfoCommentCode.elementAt(0);
            }
            if (cvTempComment!= null && cvTempComment.size()>0) {
                if(coeusParameterBean!=null){
                    Equals equals = new Equals("commentCode", new Integer(coeusParameterBean.getParameterValue()));

                    cvMailingInfoComment = cvTempComment.filter(equals);
                    if(cvMailingInfoComment!=null && cvMailingInfoComment.size() > 0){
                        queryCommentsBean = (InstituteProposalCommentsBean)cvMailingInfoComment.elementAt(0);                            
                    }
                    else{
                        queryCommentsBean=new InstituteProposalCommentsBean();
                    }
                }
            }
            if (coeusParameterBean!=null){
                if(commentsBean!= null){
                    commentsBean.setComments(iPMailingInfoForm.txtArComments.getText());
                    if(! stCommentsEquals.compare(commentsBean, queryCommentsBean)){
                        //Data Changed. save to query Engine.
                        commentsBean.setAcType(TypeConstants.UPDATE_RECORD);
                        queryEngine.update(queryKey, commentsBean);
                    }
                }else{
                    if (!EMPTY_STRING.equals(iPMailingInfoForm.txtArComments.getText())) {
                        commentsBean = new InstituteProposalCommentsBean();
                        commentsBean.setProposalNumber( this.instituteProposalBaseBean.getProposalNumber());
                        commentsBean.setSequenceNumber( this.instituteProposalBaseBean.getSequenceNumber());
                        commentsBean.setCommentCode(Integer.parseInt(coeusParameterBean.getParameterValue()));
                        commentsBean.setComments(iPMailingInfoForm.txtArComments.getText());
                        commentsBean.setAcType(TypeConstants.INSERT_RECORD);
                        queryEngine.insert(queryKey,commentsBean);
                    }
                }
            }        
        }catch (CoeusException exeception){
            exeception.printStackTrace();
        }catch (java.text.ParseException parseException){
            parseException.printStackTrace();
        }
        finally {
            modified=false;
        }
    }
    
    /**
     * validate the form data/Form and returns true if
     * validation is through else returns false.
     */    
      public boolean validate() throws edu.mit.coeus.exception.CoeusUIException {
          
          if (iPMailingInfoForm.rdBtnOSP.isSelected()==true && iPMailingInfoForm.rdBtnDHL.isSelected()==true ) {
              if (EMPTY_STRING.equals((iPMailingInfoForm.txtAccount.getText()).trim())){
                  iPMailingInfoForm.txtAccount.requestFocusInWindow();
                  //Account Number is mandatory when mailed by OSP and Mail type is DHLiPMailingInfoForm.txtDate.getText().trim();. \nPlease enter a valid Account Number
                  CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey("instPropMailingInfo_exceptionCode.1102"));
                  iPMailingInfoForm.txtAccount.requestFocusInWindow();
                  return false;
              }
          }
          String strDate = iPMailingInfoForm.txtDate.getText().trim();
          if(!strDate.equals(EMPTY_STRING)) {
              String strDate1 = dtUtils.formatDate(strDate, DATE_SEPARATERS, REQUIRED_DATEFORMAT);
              if(strDate1 == null) {
                  strDate1 = dtUtils.restoreDate(strDate, DATE_SEPARATERS);
                  if( strDate1 == null || strDate1.equals(strDate)) {
                      CoeusOptionPane.showInfoDialog("Please enter a valid deadline date");
                      //coeusMessageResources.parseMessageKey("Please enter deadLine date"));
                      iPMailingInfoForm.txtDate.requestFocusInWindow();
                      return false;
                  }
              }else {
                  strDate = strDate1;
                  iPMailingInfoForm.txtDate.setText(strDate);
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
    
   /** This method is used to format the mailing address which is received from
     *  the ProposalDevelopmentFormBean
     *
     * @param address which is unformatted
     * @return String containing formatted address
     * for this Proposal.
     */
    public String formatMailingAddress(String address){

        String fullAddress = "";
        StringTokenizer stAddress = null;
        stAddress = new StringTokenizer(address,"$#");
        while(stAddress.hasMoreTokens()){
            String addr = stAddress.nextToken();
            fullAddress += addr;
            fullAddress += (addr.length() > 0)? "\n" : "";
        }
        return fullAddress;
    }
    
    /**
     * Handles search rolodex and Delete rolodex events.
     * Also hanles the state changes for the radio buttons
     */    
    public void actionPerformed(ActionEvent ae) {
        Object source=ae.getSource();
        if (source==iPMailingInfoForm.btnAddRolodex) {
            searchAddressActionPerformed(ae);
        } else if (source==iPMailingInfoForm.btnRemoveRolodex) {
            removeAddressActionPerformed(ae);
        } else if(source == iPMailingInfoForm.rdBtnDepartment) {
             iPMailingInfoForm.rdBtnExtra.setSelected(true);
             if (iPMailingInfoForm.rdBtnDepartment.isSelected()) {
                 iPMailingInfoForm.txtAccount.setText(EMPTY_STRING);
                 iPMailingInfoForm.txtAccount.setEnabled(false);
                 iPMailingInfoForm.rdBtnRegular.setEnabled(false);
                 iPMailingInfoForm.rdBtnDHL.setEnabled(false);
                 iPMailingInfoForm.rdBtnElectronic.setEnabled(false);
             } else {
                 iPMailingInfoForm.txtAccount.setEnabled(true);
                 iPMailingInfoForm.rdBtnRegular.setEnabled(true);
                 iPMailingInfoForm.rdBtnDHL.setEnabled(true);
                 iPMailingInfoForm.rdBtnElectronic.setEnabled(true);             
             }
        } else if(source == iPMailingInfoForm.rdBtnOSP) {
            iPMailingInfoForm.txtAccount.setEnabled(true);
            if (iPMailingInfoForm.rdBtnDepartment.isSelected()) {
                 iPMailingInfoForm.rdBtnExtra.setSelected(true);
                 iPMailingInfoForm.rdBtnRegular.setEnabled(false);
                 iPMailingInfoForm.rdBtnDHL.setEnabled(false);
                 iPMailingInfoForm.rdBtnElectronic.setEnabled(false);
            } else {
                 iPMailingInfoForm.rdBtnRegular.setEnabled(true);
                 iPMailingInfoForm.rdBtnDHL.setEnabled(true);
                 iPMailingInfoForm.rdBtnElectronic.setEnabled(true);             
            }
        }
        
    }
    
    private void searchAddressActionPerformed(java.awt.event.ActionEvent evt) {
            try {
            CoeusSearch coeusSearch = new CoeusSearch(
                        mdiForm, "ROLODEXSEARCH", 1);
            coeusSearch.showSearchWindow();
            String contactAddress = "";
            String mailingAddressName = "";
            HashMap rolodexSelected = coeusSearch.getSelectedRow();
            if (rolodexSelected != null && !rolodexSelected.isEmpty() ) {
                mailingAddressId = new Integer(Utils.convertNull(rolodexSelected.get(
                                                    "ROLODEX_ID"))).intValue();
                String firstName = Utils.convertNull(rolodexSelected.get(
                                                        "FIRST_NAME"));
                String middleName = Utils.convertNull(rolodexSelected.get(
                                                        "MIDDLE_NAME"));
                String lastName = Utils.convertNull(rolodexSelected.get(
                                                        "LAST_NAME"));
                String prefix = Utils.convertNull(rolodexSelected.get(
                                                        "PREFIX"));
                String suffix = Utils.convertNull(rolodexSelected.get(
                                                        "SUFFIX"));
                if (lastName.length() > 0) {
                    mailingAddressName = (lastName + " "+ suffix +", "+
                        prefix + " "+ firstName + " "+ middleName).trim();
                } else {
                    mailingAddressName = Utils.convertNull(
                                rolodexSelected.get("ORGANIZATION"));
                }
                iPMailingInfoForm.txtAddressName.setText(Utils.convertNull(mailingAddressName));

                String temp = Utils.convertNull(rolodexSelected.get(
                                                    "ORGANIZATION"));

                if (temp != null && temp.length() > 0) {
                    contactAddress = contactAddress + temp + "\n";
                }
                temp = Utils.convertNull(rolodexSelected.get(
                                                        "ADDRESS_LINE_1"));
                if (temp != null && temp.length() > 0) {
                    contactAddress = contactAddress + temp + "\n";
                }
                temp = Utils.convertNull(rolodexSelected.get(
                                                        "ADDRESS_LINE_2"));
                if (temp != null && temp.length() > 0) {
                    contactAddress = contactAddress + temp + "\n";
                }
                temp = Utils.convertNull(rolodexSelected.get(
                                                        "ADDRESS_LINE_3"));
                if (temp != null && temp.length() > 0) {
                    contactAddress = contactAddress + temp + "\n";
                }
                temp = Utils.convertNull(rolodexSelected.get("CITY"));
                if (temp != null && temp.length() > 0) {
                    contactAddress = contactAddress + temp + "\n";
                }
                temp = Utils.convertNull(rolodexSelected.get(
                                                        "STATE_DESCRIPTION"));
                if (temp != null && temp.length() > 0) {
                    contactAddress = contactAddress + temp + " ";
                }
                temp = Utils.convertNull(rolodexSelected.get("POSTAL_CODE"));
                if (temp != null && temp.length() > 0) {
                    contactAddress = contactAddress + temp + ", ";
                }
                temp = Utils.convertNull(rolodexSelected.get("COUNTRY_NAME"));
                if (temp != null && temp.length() > 0) {
                    contactAddress = contactAddress + temp + " ";
                }

                iPMailingInfoForm.txtArAddress.setText(Utils.convertNull(contactAddress));
            }

            //saveRequired = true;
        } catch (Exception e) {
            e.printStackTrace();
            //log("Coeus Search is not available.." + e.getMessage());
        }
    }    

    
   
        private void removeAddressActionPerformed(java.awt.event.ActionEvent evt) {
        if (iPMailingInfoForm.txtAddressName.getText() != null && !iPMailingInfoForm.txtAddressName.getText().equals("")) {

            String msg = "Are you sure you want to remove mailing address entry? \n" +
                            iPMailingInfoForm.txtAddressName.getText();
            int confirm = CoeusOptionPane.showQuestionDialog(msg,
            CoeusOptionPane.OPTION_YES_NO,
            CoeusOptionPane.DEFAULT_YES);
            switch(confirm){
                case(JOptionPane.YES_OPTION):
                    try{
                        iPMailingInfoForm.txtAddressName.setText("");
                        iPMailingInfoForm.txtArAddress.setText("");
                        mailingAddressId = 0;
                    }catch(Exception ex){
                        ex.printStackTrace();
                    }
                    setSaveRequired(true);
                    break;
            }
        }
    }
        
        /**
         * Fires this event when the bean, instituteProposalBean is updated
         */        
        public void beanUpdated(BeanEvent beanEvent) {
            if(beanEvent.getBean().getClass().equals(InstituteProposalBean.class)) {
                instituteProposalBean = (InstituteProposalBean)beanEvent.getBean();
                setRefreshRequired(true);
            }
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
     

     
     
        
/** This is an inner class which is used to set the focus settings
  */   
 public class  CustomFocusAdapter extends FocusAdapter {
     /**
      * Focus gained event handler
      */     
    public void focusGained(FocusEvent focusEvent) {
        if (focusEvent.isTemporary()) {
            return ;
        }
        //sets the format of date to mm/dd/yy on focus
        if (focusEvent.getSource()== iPMailingInfoForm.txtDate){
            String strDate = iPMailingInfoForm.txtDate.getText();
            strDate = dtUtils.restoreDate(strDate, DATE_SEPARATERS);
            iPMailingInfoForm.txtDate.setText(strDate);
        }
      }
       
    /**
     * Focus lost event handler
     */    
    public void focusLost(FocusEvent focusEvent) {
        try {
            if (focusEvent.isTemporary()) {
                return;
            }
            //sets the format of date to dd-mmm-yyyy when focus is lost
            if ( focusEvent.getSource()== iPMailingInfoForm.txtDate ){ 
//                 &&  ((edu.mit.coeus.utils.CoeusTextField)focusEvent.getSource()).isShowing() ) {
                String mailDate;
                mailDate = iPMailingInfoForm.txtDate.getText().trim();
                
                /** Bug fix # 1042.
                      *Added by chandra 14th July 2004
                      */
                     if(!mailDate.equals(EMPTY_STRING)) {
                         String strDate1 = dtUtils.formatDate(mailDate, DATE_SEPARATERS, REQUIRED_DATEFORMAT);
                         if(strDate1 == null) {
                             strDate1 = dtUtils.restoreDate(mailDate, DATE_SEPARATERS);
                             if( strDate1 == null || strDate1.equals(mailDate)) {
                                 CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey("instPropMailingInfo_exceptionCode.1101"));
                                 setRequestFocusInThread(iPMailingInfoForm.txtDate);
                                 return ;
                             }
                         }else {
                             mailDate = strDate1;
                             iPMailingInfoForm.txtDate.setText(mailDate);
                         }
                     }
                 }// End chandra - 14th July 2004
                
                
                /*if(mailDate.equals(EMPTY_STRING)) return ;
                mailDate = dtUtils.formatDate(
                mailDate,DATE_SEPARATERS, REQUIRED_DATEFORMAT);
                if(mailDate == null) {
                    CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey("instPropMailingInfo_exceptionCode.1101"));
                    setRequestFocusInThread(iPMailingInfoForm.txtDate);
                }else {
                    iPMailingInfoForm.txtDate.setText(
                    mailDate);
                }
            }*/
      
         }catch(Exception exception){
            exception.printStackTrace();
        }
  }        
 
}
 
 /**
  * Mouse adapter
  */ 
 public class CustomMouseAdapter extends MouseAdapter{
     
     /**
      * mouse cliked
      */     
     public void mouseClicked(MouseEvent mouseEvent){
        if(iPMailingInfoForm.txtAddressName.getText().equals(EMPTY_STRING)) {
            return;
        }
        if(mouseEvent.getClickCount()!=2)return ;
        displayRolodexDetails();
     }
     
     /**
      * Displays the rolodex details in display mode.
      */     
     public void displayRolodexDetails(){
        RolodexMaintenanceDetailForm rolodexMaintenanceDetailForm=new RolodexMaintenanceDetailForm(ROLODEX_DISPLAY_MODE,String.valueOf(mailingAddressId));
        rolodexMaintenanceDetailForm.showForm(mdiForm,"Display Rolodex",true);
     }
 }
 
 
}
