/*
 * AddModifyQuestionController.java
 *
 * Created on November 29, 2004, 4:45 PM
 */

package edu.mit.coeus.admin.controller;

import edu.mit.coeus.admin.gui.AddModifyQuestionForm;
import edu.mit.coeus.gui.CoeusAppletMDIForm;
import edu.mit.coeus.gui.CoeusDlgWindow;
import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.admin.bean.YNQBean;
import edu.mit.coeus.admin.bean.YNQExplanationBean;
import edu.mit.coeus.utils.ComboBoxBean;
import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.brokers.*;
import edu.mit.coeus.exception.CoeusUIException;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.DateUtils;
import edu.mit.coeus.utils.LimitedPlainDocument;
import edu.mit.coeus.utils.ObjectCloner;
import edu.mit.coeus.utils.ScreenFocusTraversalPolicy;
import edu.mit.coeus.utils.TypeConstants;
import edu.mit.coeus.utils.query.Equals;

import javax.swing.event.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.sql.Date;

/**
 *
 * @author  chandrashekara
 */
public class AddModifyQuestionController extends AdminController 
    implements ActionListener,ItemListener,FocusListener{
        
        
    private static final String EMPTY_STRING = "";
    private AddModifyQuestionForm addModifyQuestionForm;
    private CoeusAppletMDIForm mdiForm;
    private YNQBean yNQBean;
    private String questionId;
    private ComboBoxBean emptyBean;
    private CoeusDlgWindow dlgAddQuestion;
    private CoeusVector cvExplanation,  cvFilteredExplanation, cvQuestion;
    private ExplanationController  explanationController;
    private CoeusMessageResources coeusMessageResources;
    /** Date utils. */    
    private DateUtils dateUtils;
    
    /** Simple Date Format. */    
    private SimpleDateFormat simpleDateFormat;

    private static final String DATE_SEPARATERS = ":/.,|-";
    private static final String DATE_FORMAT_DISPLAY = "dd-MMM-yyyy";
    private static final String SIMPLE_DATE_FORMAT = "MM/dd/yyyy";
    
    private static final int WIDTH=610;
    private static final int HEIGHT = 288;
    private static final int YES_NO_SELECTED = 0;
    private static final int YES_NO_NA_SELECTED = 1;
    private static final String ACTIVE = "A";
    private static final String INACTIVE = "I";
    private static final int ANSWER_TWO = 2;
    private static final int ANSWER_THREE = 3;
    private static final char VALUE_YES = 'Y';
    private static final char VALUE_NO = 'N';
    private static final char VALUE_NOT_APPLICABLE = 'X';
    
    private static final String FINANCIAL_ENTITY = "F";
    private static final String FINANCIAL_ENTITY_VALUE = "Financial Entity";
    
    private static final String INDIVIDUAL = "I";
    private static final String INDIVIDUAL_VALUE = "Individual";
    
    private static final String INDIVIDUAL_CONFLICT = "C";
    private static final String INDIVIDUAL_CONFLICT_VALUE = "Individual-Conflict of Interest";
    
    private static final String ORGANIZATION = "O";
    private static final String ORGANIZATION_VALUE = "Organization";
    
    private static final String PROPOSAL = "P";
    private static final String PROPOSAL_VALUE = "Proposal";
    
    private final String appliesToCode[] = {EMPTY_STRING,FINANCIAL_ENTITY,INDIVIDUAL,
        INDIVIDUAL_CONFLICT,ORGANIZATION,PROPOSAL };
    private final String appliesToValues[]={EMPTY_STRING,FINANCIAL_ENTITY_VALUE,
        INDIVIDUAL_VALUE,INDIVIDUAL_CONFLICT_VALUE,ORGANIZATION_VALUE,PROPOSAL_VALUE };

    /** Please enter an effective date */
    private static final String ENTER_EFFECTIVE_DATE = "awardDetail_exceptionCode.1057";
    /** Please enter a valid Effective Date. */
    private static final String INVALID_EFFECTIVE_DATE = "awardDetail_exceptionCode.1052";
    private static final String ENTER_QUESTION_CODE = "ynqExceptionCode.1652";
    private static final String QUESTION_CODE = "ynqExceptionCode.1653";
    private static final String QUESTION_CODE_ALREADY_EXIST = "ynqExceptionCode.1654";
    private static final String APPLIES_TO_IS_NULL = "ynqExceptionCode.1655";
    private static final String QUESTION_DESCRIPTION_IS_NULL = "ynqExceptionCode.1656";
    private static final String SAVE_CONFIRMATION = "saveConfirmCode.1002";

    
    //private StringBuffer explnationRequierFor ;
    private String explanationRequiredFor;
    private String dateRequierdFor;
    
    public boolean isDataModified = false;
    
    /** Creates a new instance of AddModifyQuestionController */
    public AddModifyQuestionController(CoeusAppletMDIForm mdiForm, YNQBean yNQBean, 
        CoeusVector cvQuestion, CoeusVector cvExplanation, char mode) throws CoeusException{
        try{
            this.mdiForm = mdiForm;
            this.yNQBean = yNQBean;
            this.cvQuestion = cvQuestion;
            this.cvExplanation = cvExplanation;
            coeusMessageResources = CoeusMessageResources.getInstance();
            setFunctionType(mode);
            addModifyQuestionForm = new AddModifyQuestionForm();
            addModifyQuestionForm.txtArQuestion.setCaretPosition(0); 
            initComponents();
            getDefaultData();
            registerComponents();
            populateTypeCombo();
            setFormData(yNQBean);
            postInitComponents();
        }catch(Exception exception){
            exception.printStackTrace();
        }
    }
    private void initComponents(){
        try{
            dateUtils = new DateUtils();
            simpleDateFormat = new SimpleDateFormat(SIMPLE_DATE_FORMAT);
            emptyBean = new ComboBoxBean();
            emptyBean.setCode(EMPTY_STRING);
            emptyBean.setDescription(EMPTY_STRING);

            cvFilteredExplanation = new CoeusVector();
            if(cvExplanation != null && yNQBean != null){
                Equals eqQuestionCode =  new Equals("questionId" ,yNQBean.getQuestionId());
                CoeusVector tempVector = (CoeusVector)ObjectCloner.deepCopy(cvExplanation);
                cvFilteredExplanation = tempVector.filter(eqQuestionCode);
            }
        }catch(Exception exception){
            exception.printStackTrace();
        }
    }
    // Extract  the bean data and assign to the respective variables.
    private void getDefaultData(){
        if(yNQBean!= null){
            if(yNQBean.getQuestionId()!= null && (!yNQBean.getQuestionId().trim().equals(EMPTY_STRING))){
                questionId = yNQBean.getQuestionId(); 
            }else{
                questionId = EMPTY_STRING;
            }
            if(yNQBean.getExplanationRequiredFor()!= null && 
                (!yNQBean.getExplanationRequiredFor().equals(EMPTY_STRING))){
                    explanationRequiredFor = yNQBean.getExplanationRequiredFor();
            }else{
                explanationRequiredFor = EMPTY_STRING;
            }

            if(yNQBean.getDateRequiredFor()!= null &&
                (!yNQBean.getDateRequiredFor().equals(EMPTY_STRING))){
                    dateRequierdFor = yNQBean.getDateRequiredFor();
            }else{
                dateRequierdFor = EMPTY_STRING;
            }
        }
    }
    
    public void formatFields() {
    }
    
    public java.awt.Component getControlledUI() {
        return addModifyQuestionForm;
    }
    
    public Object getFormData() {
        return null;
    }
    
    public void registerComponents() {

        addModifyQuestionForm.txtEffectiveDate.setDocument(new LimitedPlainDocument(11));
        addModifyQuestionForm.txtCode.setDocument(new LimitedPlainDocument(4));//new JTextFieldFilter(JTextFieldFilter.ALPHA_NUMERIC,4)
        addModifyQuestionForm.txtArQuestion.setDocument(new LimitedPlainDocument(400));
        addModifyQuestionForm.txtGroup.setDocument(new LimitedPlainDocument(250));

        addModifyQuestionForm.txtEffectiveDate.addFocusListener(this);

        addModifyQuestionForm.btnCancel.addActionListener(this);
        addModifyQuestionForm.btnMore.addActionListener(this);
        addModifyQuestionForm.btnOK.addActionListener(this);

        addModifyQuestionForm.rdBtnYesNo.addItemListener(this);
        addModifyQuestionForm.rdBtnYesNoNA.addItemListener(this);
        addModifyQuestionForm.rdBtnActive.addItemListener(this);
        addModifyQuestionForm.rdBtnInactive.addItemListener(this);

        addModifyQuestionForm.chkdtYes.addItemListener(this);
        addModifyQuestionForm.chkdtNo.addItemListener(this);
        addModifyQuestionForm.chkDtNa.addItemListener(this);
        addModifyQuestionForm.chkYes.addItemListener(this);
        addModifyQuestionForm.chkNo.addItemListener(this);
        addModifyQuestionForm.chkNA.addItemListener(this);

        addModifyQuestionForm.cmbAppliesTo.addItemListener(this);
    }
    
    
    public void setFormData(Object data) throws CoeusException {
        yNQBean = (YNQBean)data;
        java.sql.Date nowDate = new java.sql.Date(new java.util.Date().getTime());
        if(yNQBean!=null && getFunctionType() == TypeConstants.MODIFY_MODE){
            // Set the code
            addModifyQuestionForm.txtCode.setText(yNQBean.getQuestionId());
            //set the description of the question
            addModifyQuestionForm.txtArQuestion.setText(yNQBean.getDescription());
            // Set the Property for the Group Name
            addModifyQuestionForm.txtGroup.setText(yNQBean.getGroupName());

            //set the EffectiveDate of the question
            java.sql.Date effectiveDate = yNQBean.getEffectiveDate();
            if(effectiveDate != null) {
                addModifyQuestionForm.txtEffectiveDate.setText(dateUtils.formatDate(effectiveDate.toString(), DATE_FORMAT_DISPLAY));
            }else{
                addModifyQuestionForm.txtEffectiveDate.setText(dateUtils.formatDate(nowDate.toString(), DATE_FORMAT_DISPLAY));
            }
            
            // Set the status of the question
            if(yNQBean.getStatus()!= null){
                if(yNQBean.getStatus().trim().equals(ACTIVE)){
                    addModifyQuestionForm.rdBtnActive.setSelected(true);
                }else if(yNQBean.getStatus().trim().equals(INACTIVE)){
                    addModifyQuestionForm.rdBtnInactive.setSelected(true);
                }
            }
            
            if(yNQBean.getNoOfAnswers()!= -1){
                if(yNQBean.getNoOfAnswers()==ANSWER_TWO){
                    addModifyQuestionForm.chkNA.setVisible(false);
                    addModifyQuestionForm.chkNA.setEnabled(false);
                    addModifyQuestionForm.chkDtNa.setVisible(false);
                    addModifyQuestionForm.chkDtNa.setEnabled(false);
                    addModifyQuestionForm.rdBtnYesNo.setSelected(true);
                    // set the explanation required for data.
                    // get the each character from teh string and set the values
                    for(int i=0; i<explanationRequiredFor.length();i++){
                        char explData =  explanationRequiredFor.charAt(i);
                        if(explData==VALUE_YES){
                            addModifyQuestionForm.chkYes.setSelected(true);
                        }else if(explData==VALUE_NO){
                            addModifyQuestionForm.chkNo.setSelected(true);
                        }
                    }
                    // set the Date required for data.
                    // get the each character from teh string and set the values
                    for(int index=0; index<dateRequierdFor.length();index++){
                        char dateRequired =  dateRequierdFor.charAt(index);
                        if(dateRequired==VALUE_YES){
                            addModifyQuestionForm.chkdtYes.setSelected(true);
                        }else if(dateRequired==VALUE_NO){
                            addModifyQuestionForm.chkdtNo.setSelected(true);
                        }
                    }
                }else if(yNQBean.getNoOfAnswers()==ANSWER_THREE){
                    addModifyQuestionForm.chkNA.setVisible(true);
                    addModifyQuestionForm.chkNA.setEnabled(true);
                    addModifyQuestionForm.chkDtNa.setVisible(true);
                    addModifyQuestionForm.chkDtNa.setEnabled(true);
                    addModifyQuestionForm.rdBtnYesNoNA.setSelected(true);
                    // set the explanation required for data.
                    // get the each character from teh string and set the values
                    for(int i=0; i<explanationRequiredFor.length();i++){
                        char explData =  explanationRequiredFor.charAt(i);
                        if(explData==VALUE_YES){
                            addModifyQuestionForm.chkYes.setSelected(true);
                        }else if(explData==VALUE_NO){
                            addModifyQuestionForm.chkNo.setSelected(true);
                        }else if(explData==VALUE_NOT_APPLICABLE){
                            addModifyQuestionForm.chkNA.setSelected(true);
                        }
                    }
                    // set the Date required for data.
                    // get the each character from teh string and set the values
                    for(int index=0; index<dateRequierdFor.length();index++){
                        char dateRequired =  dateRequierdFor.charAt(index);
                        if(dateRequired==VALUE_YES){
                            addModifyQuestionForm.chkdtYes.setSelected(true);
                        }else if(dateRequired==VALUE_NO){
                            addModifyQuestionForm.chkdtNo.setSelected(true);
                        }else if(dateRequired==VALUE_NOT_APPLICABLE){
                            addModifyQuestionForm.chkDtNa.setSelected(true);
                        }
                    }
                }else{
                    addModifyQuestionForm.rdBtnYesNo.setSelected(true);
                    addModifyQuestionForm.chkNA.setVisible(false);
                    addModifyQuestionForm.chkNA.setEnabled(false);
                    addModifyQuestionForm.chkDtNa.setVisible(false);
                    addModifyQuestionForm.chkDtNa.setEnabled(false);
                }
            }else{
                // Set empty and default values
                
                 // Set the code
                addModifyQuestionForm.txtCode.setText(EMPTY_STRING);
                addModifyQuestionForm.rdBtnActive.setSelected(true);
                addModifyQuestionForm.txtArQuestion.setText(EMPTY_STRING);
                addModifyQuestionForm.txtGroup.setText(EMPTY_STRING);
                addModifyQuestionForm.rdBtnYesNo.setSelected(true);
                if(nowDate != null) {
                    addModifyQuestionForm.txtEffectiveDate.setText(dateUtils.formatDate(nowDate.toString(), DATE_FORMAT_DISPLAY));
                }
            }
            
            //get the applies to values from the combo box.
            if(yNQBean.getQuestionType()!= null && (!yNQBean.getQuestionType().equals(EMPTY_STRING))){
                if(yNQBean.getQuestionType().equalsIgnoreCase(FINANCIAL_ENTITY)){
                    ComboBoxBean financialBean = new ComboBoxBean();
                    financialBean.setCode(FINANCIAL_ENTITY);
                    financialBean.setDescription(FINANCIAL_ENTITY_VALUE);
                    addModifyQuestionForm.cmbAppliesTo.setSelectedItem(financialBean);
                }else if(yNQBean.getQuestionType().equalsIgnoreCase(INDIVIDUAL)){
                    ComboBoxBean individualBean = new ComboBoxBean();
                    individualBean.setCode(INDIVIDUAL);
                    individualBean.setDescription(INDIVIDUAL_VALUE);
                    addModifyQuestionForm.cmbAppliesTo.setSelectedItem(individualBean);
                }else if(yNQBean.getQuestionType().equalsIgnoreCase(INDIVIDUAL_CONFLICT)){
                    ComboBoxBean individualConflictBean = new ComboBoxBean();
                    individualConflictBean.setCode(INDIVIDUAL_CONFLICT);
                    individualConflictBean.setDescription(INDIVIDUAL_CONFLICT_VALUE);
                    addModifyQuestionForm.cmbAppliesTo.setSelectedItem(individualConflictBean);
                }else if(yNQBean.getQuestionType().equalsIgnoreCase(PROPOSAL)){
                    ComboBoxBean proposalBean = new ComboBoxBean();
                    proposalBean.setCode(PROPOSAL);
                    proposalBean.setDescription(PROPOSAL_VALUE);
                    addModifyQuestionForm.cmbAppliesTo.setSelectedItem(proposalBean);
                }else if(yNQBean.getQuestionType().equalsIgnoreCase(ORGANIZATION)){
                    ComboBoxBean organizationBean = new ComboBoxBean();
                    organizationBean.setCode(ORGANIZATION);
                    organizationBean.setDescription(ORGANIZATION_VALUE);
                    addModifyQuestionForm.cmbAppliesTo.setSelectedItem(organizationBean);
                }
            }else {
                addModifyQuestionForm.cmbAppliesTo.setSelectedItem(emptyBean);
            }
        }else{
            // set the default values In Add Mode.
            addModifyQuestionForm.txtCode.setText(EMPTY_STRING);
            addModifyQuestionForm.rdBtnActive.setSelected(true);
            addModifyQuestionForm.txtArQuestion.setText(EMPTY_STRING);
            addModifyQuestionForm.txtGroup.setText(EMPTY_STRING);
            addModifyQuestionForm.rdBtnYesNo.setSelected(true);
            if(nowDate != null) {
                addModifyQuestionForm.txtEffectiveDate.setText(dateUtils.formatDate(nowDate.toString(), DATE_FORMAT_DISPLAY));
            }
            
            addModifyQuestionForm.cmbAppliesTo.setSelectedItem(emptyBean);
        }
        isDataModified = false;
    }
    // This method will populate the code and description for the 
    // applies to combo box.
    private void populateTypeCombo() {
        ComboBoxBean comboBoxBean;
        for(int i = 0; i<appliesToCode.length;i++){
            comboBoxBean = new ComboBoxBean(appliesToCode[i],appliesToValues[i]);
            addModifyQuestionForm.cmbAppliesTo.addItem(comboBoxBean);
        }
           
    } 
    private boolean isQuestionCodeChanged(){
        String strQuestionCode = addModifyQuestionForm.txtCode.getText().trim();
        if(yNQBean != null){
            if(!yNQBean.getQuestionId().equals(strQuestionCode)){
                return true;
            }
        }else if(strQuestionCode.length()>0){
            return true;
        }
        return false;
    }
    public boolean isDataChanged(){
        if(isDataModified){
            return true;
        }
        if(getFunctionType() == TypeConstants.MODIFY_MODE){
//            Checking whether Question Id changed
            String strQuestionCode = addModifyQuestionForm.txtCode.getText().trim();
            if(!yNQBean.getQuestionId().equals(strQuestionCode)){
                isDataModified = true;
                return true;
            }
//            Checking whether Question Description changed
            String strQuestionDesc = addModifyQuestionForm.txtArQuestion.getText().trim();
            if(!yNQBean.getDescription().equals(strQuestionDesc)){
                isDataModified = true;
                return true;
            }
            
//            checking whether Question Group Name changed or not
            String strGropuName = addModifyQuestionForm.txtGroup.getText().trim();
            if(yNQBean.getGroupName() == null){
                if(strGropuName.length() > 0){
                    isDataModified = true;
                }
            }else if(!yNQBean.getGroupName().equals(strGropuName)){
                isDataModified = true;
                return true;
            }
//            Checking whether Effective Date changed
            java.sql.Date effectiveDate = yNQBean.getEffectiveDate();
            if(effectiveDate != null) {
                if(!addModifyQuestionForm.txtEffectiveDate.getText().trim().equals(dateUtils.formatDate(effectiveDate.toString(), DATE_FORMAT_DISPLAY))){
                    isDataModified = true;
                    return true;
                }
            }else if(addModifyQuestionForm.txtEffectiveDate.getText().trim().length()>0){
                isDataModified = true;
                return true;
            }
        }else if(getFunctionType() == TypeConstants.ADD_MODE){
//            Checking whether Question Id changed
            String strQuestionCode = addModifyQuestionForm.txtCode.getText().trim();
            if(strQuestionCode.length()>0){
                isDataModified = true;
                return true;
            }
//            Checking whether Question Description changed
            String strQuestionDesc = addModifyQuestionForm.txtArQuestion.getText().trim();
            if(strQuestionDesc.length()>0){
                isDataModified = true;
                return true;
            }
            
//            checking whether Question Group name is altered or not
            String strGropu = addModifyQuestionForm.txtGroup.getText().trim();
            if(strGropu.length()>0){
                isDataModified = true;
                return true;
            }
            
            
//            Checking whether Effective Date changed
            java.sql.Date nowDate = new java.sql.Date(new java.util.Date().getTime());
            String strNowDate = dateUtils.formatDate(nowDate.toString(), DATE_FORMAT_DISPLAY);
            if(!addModifyQuestionForm.txtEffectiveDate.getText().trim().equals(strNowDate)){
                isDataModified = true;
                return true;
            }
        }
        return isDataModified;
    }
    public boolean validate() throws edu.mit.coeus.exception.CoeusUIException {
        
//        Validating Question Code
        String strCode = addModifyQuestionForm.txtCode.getText();
        if(strCode == null || strCode.trim().equals(EMPTY_STRING)){
            addModifyQuestionForm.txtCode.requestFocusInWindow();
            String mesg = coeusMessageResources.parseMessageKey(ENTER_QUESTION_CODE);
            CoeusOptionPane.showErrorDialog(mesg);
//            addModifyQuestionForm.txtCode.requestFocusInWindow();
            return false;
        }
        if(yNQBean != null && !yNQBean.getQuestionId().equals(strCode) 
            && isQuestionCodeExists(strCode)){
            addModifyQuestionForm.txtCode.requestFocusInWindow();
            String mesg = coeusMessageResources.parseMessageKey(QUESTION_CODE);
            mesg = mesg +" "+ strCode +" "+ coeusMessageResources.parseMessageKey(QUESTION_CODE_ALREADY_EXIST);
            CoeusOptionPane.showErrorDialog(mesg);
//            addModifyQuestionForm.txtCode.requestFocusInWindow();
            return false;
        }else if(yNQBean == null && isQuestionCodeExists(strCode)){
            addModifyQuestionForm.txtCode.requestFocusInWindow();
            String mesg = coeusMessageResources.parseMessageKey(QUESTION_CODE);
            mesg = mesg +" "+ strCode +" "+ coeusMessageResources.parseMessageKey(QUESTION_CODE_ALREADY_EXIST);
            CoeusOptionPane.showErrorDialog(mesg);
            return false;
        }

//        Validating AppliesTo
        ComboBoxBean obj = (ComboBoxBean)addModifyQuestionForm.cmbAppliesTo.getSelectedItem();
        if(obj == null || obj.getCode() == null || obj.getCode().trim().equals(EMPTY_STRING)){
            addModifyQuestionForm.cmbAppliesTo.requestFocusInWindow();
            CoeusOptionPane.showErrorDialog(coeusMessageResources.parseMessageKey(APPLIES_TO_IS_NULL));
            return false;
        }

//        Validating Effective Date
        String strDate = addModifyQuestionForm.txtEffectiveDate.getText().trim();
        if(strDate.equals(EMPTY_STRING)) {
            addModifyQuestionForm.txtEffectiveDate.requestFocusInWindow();
            CoeusOptionPane.showErrorDialog(coeusMessageResources.parseMessageKey(ENTER_EFFECTIVE_DATE));
            return false;
        }

        if(addModifyQuestionForm.txtEffectiveDate.hasFocus()) {
            strDate = dateUtils.formatDate(strDate,DATE_SEPARATERS, DATE_FORMAT_DISPLAY);
        }else {
            strDate = dateUtils.restoreDate(strDate, DATE_SEPARATERS);
        }
        if(strDate == null) {
            addModifyQuestionForm.txtEffectiveDate.requestFocusInWindow();
            CoeusOptionPane.showErrorDialog(coeusMessageResources.parseMessageKey(INVALID_EFFECTIVE_DATE));
            return false;
        }else {
            strDate = dateUtils.formatDate(strDate,DATE_SEPARATERS, DATE_FORMAT_DISPLAY);
            addModifyQuestionForm.txtEffectiveDate.setText(strDate);
        }
        
//        Validating Question Description
        String strDescription = addModifyQuestionForm.txtArQuestion.getText();
        if(strDescription == null || strDescription.trim().equals(EMPTY_STRING)){
            addModifyQuestionForm.txtArQuestion.requestFocusInWindow();
            CoeusOptionPane.showErrorDialog(coeusMessageResources.parseMessageKey(QUESTION_DESCRIPTION_IS_NULL));
            return false;
        }
        return true;
    }

    /**
     * To check whether the current YNQBean with the same QuestionCode
     * property exists in the cvQuestion CoeusVector.
     * @return boolean
     **/
    private boolean isQuestionCodeExists(String value){
        for(int i=0;i<cvQuestion.size();i++){
            String beanProperty = ((YNQBean)cvQuestion.elementAt(i)).getQuestionId();
            if(beanProperty.equals(value)){
                return true;
            }
        }
        return false;
    }
    
    public void display(){
    }
    
// This method will dislapys the window
    public YNQBean displayWindow() {
        dlgAddQuestion.setVisible(true);
        return yNQBean;
    }
    
    public void saveFormData() throws edu.mit.coeus.exception.CoeusException{
        try{
            if(!validate()){
                return;
            }
            if(isDataChanged()){
                if(getFunctionType() == TypeConstants.ADD_MODE){
                    yNQBean = new YNQBean();
                    yNQBean.setQuestionId(addModifyQuestionForm.txtCode.getText().trim());
                    yNQBean.setAcType(TypeConstants.INSERT_RECORD);
                }else{
                    if(isQuestionCodeChanged() && yNQBean.getAcType() == null){
                        yNQBean.setAcType(TypeConstants.INSERT_RECORD);
                    }else if(yNQBean.getAcType() == null && !isQuestionCodeChanged()){
                        yNQBean.setAcType(TypeConstants.UPDATE_RECORD);
                    }else if(yNQBean.getAcType().equals(TypeConstants.UPDATE_RECORD) && isQuestionCodeChanged()){
                        yNQBean.setAcType(TypeConstants.INSERT_RECORD);
                    }
                }
                
//                yNQBean.setQuestionId(addModifyQuestionForm.txtCode.getText().trim());
                setQuestionId(addModifyQuestionForm.txtCode.getText().trim());
                yNQBean.setDescription(addModifyQuestionForm.txtArQuestion.getText().trim());
                yNQBean.setGroupName(addModifyQuestionForm.txtGroup.getText().trim());
                String strDate = addModifyQuestionForm.txtEffectiveDate.getText().trim();
                java.util.Date utilDate = simpleDateFormat.parse(dateUtils.restoreDate(strDate,DATE_SEPARATERS));
                Date effectiveDate = new java.sql.Date(utilDate.getTime());
                yNQBean.setEffectiveDate(effectiveDate);
                ComboBoxBean obj = (ComboBoxBean)addModifyQuestionForm.cmbAppliesTo.getSelectedItem();
                yNQBean.setQuestionType(obj.getCode());
                
                //            Setting Status Property
                if(addModifyQuestionForm.rdBtnActive.isSelected()){
                    yNQBean.setStatus(ACTIVE);
                }else{
                    yNQBean.setStatus(INACTIVE);
                }
                
                //            Setting NoOfQuestions Property
                if(addModifyQuestionForm.rdBtnYesNo.isSelected()){
                    yNQBean.setNoOfAnswers(ANSWER_TWO);
                }else{
                    yNQBean.setNoOfAnswers(ANSWER_THREE);
                }
                
                //            Setting ExplanationRequiredFor Property
                String strExpReqdFor = EMPTY_STRING;
                if(addModifyQuestionForm.chkYes.isSelected()){
                    strExpReqdFor=strExpReqdFor+VALUE_YES;
                }
                if(addModifyQuestionForm.chkNo.isSelected()){
                    strExpReqdFor=strExpReqdFor+VALUE_NO;
                }
                if(yNQBean.getNoOfAnswers()==ANSWER_THREE &&  addModifyQuestionForm.chkNA.isSelected()){
                    strExpReqdFor=strExpReqdFor+VALUE_NOT_APPLICABLE;
                }
                yNQBean.setExplanationRequiredFor(strExpReqdFor);
                
                //            Setting DateRequiredFor Property
                String strDtReqdFor = EMPTY_STRING;
                if(addModifyQuestionForm.chkdtYes.isSelected()){
                    strDtReqdFor=strDtReqdFor+VALUE_YES;
                }
                if(addModifyQuestionForm.chkdtNo.isSelected()){
                    strDtReqdFor=strDtReqdFor+VALUE_NO;
                }
                if(yNQBean.getNoOfAnswers()==ANSWER_THREE &&  addModifyQuestionForm.chkDtNa.isSelected()){
                    strDtReqdFor=strDtReqdFor+VALUE_NOT_APPLICABLE;
                }
                yNQBean.setDateRequiredFor(strDtReqdFor);
                
//                if(yNQBean.getAcType()== null)
//                    yNQBean.setAcType(TypeConstants.UPDATE_RECORD);
            }
            if(yNQBean != null && !yNQBean.getQuestionId().equals(addModifyQuestionForm.txtCode.getText().trim()) 
                || (explanationController != null 
                && explanationController.isDataChanged())){
                updateExplanations();
                if(!isDataModified){
                    yNQBean = null;
                }
                isDataModified = true;
            }else if(!isDataModified){
                yNQBean = null;
            }
            dlgAddQuestion.setVisible(false);
        }catch(CoeusUIException uiException){
            throw new CoeusException(uiException.getMessage());
        }catch(Exception exception){
            throw new CoeusException(exception.getMessage());
        }
    }
    
    private void updateExplanations(){
        String questionCode = addModifyQuestionForm.txtCode.getText().trim();
        for(int i=0;i<cvFilteredExplanation.size();i++){
            YNQExplanationBean updateBean = (YNQExplanationBean)cvFilteredExplanation.elementAt(i);
            if((updateBean.getAcType() == null && yNQBean != null 
                && isQuestionCodeChanged())){

                YNQExplanationBean nullBean = (YNQExplanationBean)search(cvExplanation,updateBean,null);
                if(nullBean != null){
                    nullBean.setAcType(TypeConstants.DELETE_RECORD);
                    updateBean.setQuestionId(questionCode);
                    updateBean.setAcType(TypeConstants.INSERT_RECORD);
                    cvExplanation.add(updateBean);
                }else{
                    YNQExplanationBean insertBean = (YNQExplanationBean)search(cvExplanation,updateBean,TypeConstants.INSERT_RECORD);
                    if(insertBean != null){
                        insertBean.setQuestionId(questionCode);
                        insertBean.setExplanation(updateBean.getExplanation());
                    }else{
                        updateBean.setAcType(TypeConstants.INSERT_RECORD);
                        updateBean.setQuestionId(questionCode);
                        cvExplanation.add(updateBean);
                    }
                }
            }else if(updateBean.getAcType() != null 
                && updateBean.getAcType().equals(TypeConstants.INSERT_RECORD)){

                    YNQExplanationBean insertBean = (YNQExplanationBean)search(cvExplanation,updateBean,TypeConstants.INSERT_RECORD);
                    if(insertBean != null){
                        insertBean.setQuestionId(questionCode);
                        insertBean.setExplanation(updateBean.getExplanation());
                    }else{
                        updateBean.setAcType(TypeConstants.INSERT_RECORD);
                        updateBean.setQuestionId(questionCode);
                        cvExplanation.add(updateBean);
                    }
            }else if(updateBean.getAcType() != null 
                && updateBean.getAcType().equals(TypeConstants.DELETE_RECORD)){
                YNQExplanationBean nullBean = (YNQExplanationBean)search(cvExplanation,updateBean,null);
                if(nullBean != null){
                    nullBean.setAcType(TypeConstants.DELETE_RECORD);
                }else{
                    YNQExplanationBean insertBean = (YNQExplanationBean)search(cvFilteredExplanation,updateBean,TypeConstants.INSERT_RECORD);
                    if(insertBean == null){
                        searchAndDelete(cvExplanation,updateBean,TypeConstants.INSERT_RECORD);
                    }
                }
            }
        }
    }

    /**
     * Method to Search a Bean and delete it
     * @param coeusVector CoeusVector
     * @param object Object
     * @return void
     **/
    private void searchAndDelete(CoeusVector coeusVector, Object object, String acType){
        if(object != null && coeusVector != null){
            YNQExplanationBean bean = (YNQExplanationBean)object;
            YNQExplanationBean vectorBean = null;
            for(int i=0;i<coeusVector.size();i++){
                vectorBean = (YNQExplanationBean)coeusVector.elementAt(i);
                if(vectorBean.getAcType()!=null 
                    && vectorBean.getQuestionId().equals(bean.getQuestionId()) 
                    && vectorBean.getExplanationType().equals(bean.getExplanationType()) 
                    && vectorBean.getAcType().equals(acType) ){
                    coeusVector.removeElementAt(i);
                    break;
                }
            }
        }
    }
    
    /**
     * To search an object(YNQExplanationBean) in the CoeusVector(cvExplanation)
     * @param object Object
     * @param acType String
     * @return Object
     **/
    private Object search(CoeusVector coeusVector, Object object, String acType){
        try{
            YNQExplanationBean bean = null;
            for(int i=0;i<coeusVector.size();i++){
                bean = (YNQExplanationBean)coeusVector.elementAt(i);
                if(acType != null && bean.getAcType() != null 
                    && !acType.equals(EMPTY_STRING) 
                    && bean.getQuestionId().equals(yNQBean.getQuestionId())
                    && bean.getExplanationType().equalsIgnoreCase(((YNQExplanationBean)object).getExplanationType()) 
                    && bean.getAcType().equals(acType)){
                    return bean;
                }else if( acType == null 
                    && bean.getExplanationType().equalsIgnoreCase(((YNQExplanationBean)object).getExplanationType()) 
                    && bean.getQuestionId().equals(yNQBean.getQuestionId())
                    && bean.getAcType() == null){
                    return bean;
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }
    
    public void actionPerformed(ActionEvent actionEvent) {
        Object source = actionEvent.getSource();
        try{
            dlgAddQuestion.setCursor(new java.awt.Cursor(java.awt.Cursor.WAIT_CURSOR));
            
            if(source.equals(addModifyQuestionForm.btnCancel)){
                performCancelAction();
            }else if(source.equals(addModifyQuestionForm.btnMore)){
                performMoreAction();
            }else if(source.equals(addModifyQuestionForm.btnOK)){
                saveFormData();
            }
        }catch (CoeusException  coeusException){
            CoeusOptionPane.showErrorDialog(coeusException.getMessage());
        }finally{
            dlgAddQuestion.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        }
    }
    
    private void performMoreAction() throws CoeusException{
        String description = addModifyQuestionForm.txtArQuestion.getText().trim();
//        Validating Question Code
        String strCode = addModifyQuestionForm.txtCode.getText();
        if(strCode == null || strCode.trim().equals(EMPTY_STRING)){
            addModifyQuestionForm.txtCode.requestFocusInWindow();
            CoeusOptionPane.showErrorDialog(coeusMessageResources.parseMessageKey(ENTER_QUESTION_CODE));
            return;
        }
        if(yNQBean != null && !yNQBean.getQuestionId().equals(strCode) 
            && isQuestionCodeExists(strCode)){
            addModifyQuestionForm.txtCode.requestFocusInWindow();
            String mesg = coeusMessageResources.parseMessageKey(QUESTION_CODE);
            mesg = mesg + strCode+ coeusMessageResources.parseMessageKey(QUESTION_CODE_ALREADY_EXIST);
            CoeusOptionPane.showErrorDialog(mesg);
            return;
        }else if(yNQBean == null && isQuestionCodeExists(strCode)){
            addModifyQuestionForm.txtCode.requestFocusInWindow();
            String mesg = coeusMessageResources.parseMessageKey(QUESTION_CODE);
            mesg = mesg + strCode+ coeusMessageResources.parseMessageKey(QUESTION_CODE_ALREADY_EXIST);
            CoeusOptionPane.showErrorDialog(mesg);
            return;
        }
        if(cvFilteredExplanation == null){
            cvFilteredExplanation = new CoeusVector();
        }
        explanationController = new ExplanationController(mdiForm, TypeConstants.MODIFY_MODE);
        explanationController.setQuestionId(strCode);
        if(!description.equals(EMPTY_STRING)){
            explanationController.setDescription(description);
        }else{
            explanationController.setDescription(EMPTY_STRING);
        }
        explanationController.setFormData(cvFilteredExplanation);
        explanationController.display();
        
    }
    
//    private CoeusVector getExplanationForAll(String questionId) throws CoeusException{
//        CoeusVector data = null;
//        RequesterBean requester = new RequesterBean();
//        requester.setFunctionType(GET_ONLY_EXPLANATION);
//        requester.setDataObject(questionId);
//        AppletServletCommunicator comm
//        = new AppletServletCommunicator(connectTo, requester);
//        
//        comm.send();
//        ResponderBean responder = comm.getResponse();
//        if(responder!= null){
//            if(responder.isSuccessfulResponse()){
//                data = (CoeusVector)responder.getDataObjects();
//            }
//        }else{
//            throw new CoeusException(responder.getMessage(),0);
//        }
//        return data;
//    }
    
    private void performSelectOption(int type){
        if(type==YES_NO_SELECTED){
            if(addModifyQuestionForm.rdBtnYesNo.isSelected()){
                addModifyQuestionForm.chkDtNa.setVisible(false);
                addModifyQuestionForm.chkDtNa.setEnabled(false);
                addModifyQuestionForm.chkNA.setVisible(false);
                addModifyQuestionForm.chkNA.setEnabled(false);
            }
        }else if(type==YES_NO_NA_SELECTED){
            addModifyQuestionForm.chkDtNa.setEnabled(true);
            addModifyQuestionForm.chkDtNa.setVisible(true);
            addModifyQuestionForm.chkNA.setEnabled(true);
            addModifyQuestionForm.chkNA.setVisible(true);
        }
    }// end
     private void performCancelAction() throws CoeusException{
         try{
            if(isDataChanged() ||
                (explanationController != null && explanationController.isDataChanged()) ||
                getFunctionType() == TypeConstants.ADD_MODE ){
                String mesg = coeusMessageResources.parseMessageKey(SAVE_CONFIRMATION);
                int selectedOption = CoeusOptionPane.showQuestionDialog(
                coeusMessageResources.parseMessageKey(mesg),
                CoeusOptionPane.OPTION_YES_NO_CANCEL,
                CoeusOptionPane.DEFAULT_YES);
                if(selectedOption == CoeusOptionPane.SELECTION_YES) {
                    saveFormData();
                } else if(selectedOption == CoeusOptionPane.SELECTION_NO){
                    isDataModified = false;
                    yNQBean = null;
                    dlgAddQuestion.setVisible(false);
                }
            }else{
                yNQBean = null;
                dlgAddQuestion.setVisible(false);
            }
         }catch(CoeusException exception){
             throw new CoeusException(exception.getMessage());
         }
    }
    
    /** Specifies the Modal window */
    private void postInitComponents(){
            Component[] components = {  
                addModifyQuestionForm.txtCode, addModifyQuestionForm.cmbAppliesTo,
                addModifyQuestionForm.rdBtnActive,addModifyQuestionForm.rdBtnInactive,addModifyQuestionForm.txtEffectiveDate,
                addModifyQuestionForm.txtGroup,addModifyQuestionForm.txtArQuestion,
                addModifyQuestionForm.rdBtnYesNo,addModifyQuestionForm.rdBtnYesNoNA,
                addModifyQuestionForm.chkYes,addModifyQuestionForm.chkNo,addModifyQuestionForm.chkNA,
                addModifyQuestionForm.chkdtYes,addModifyQuestionForm.chkdtNo,addModifyQuestionForm.chkDtNa,
                addModifyQuestionForm.btnMore, addModifyQuestionForm.btnOK, addModifyQuestionForm.btnCancel};
            ScreenFocusTraversalPolicy traversePolicy = new ScreenFocusTraversalPolicy( components );
            addModifyQuestionForm.setFocusTraversalPolicy(traversePolicy);
            addModifyQuestionForm.setFocusCycleRoot(true);

            dlgAddQuestion = new CoeusDlgWindow(mdiForm);
            dlgAddQuestion.getContentPane().add(getControlledUI());
            dlgAddQuestion.setTitle("The Detail of a Question");
            dlgAddQuestion.setFont(CoeusFontFactory.getLabelFont());
            dlgAddQuestion.setModal(true);
            dlgAddQuestion.setResizable(false);
            dlgAddQuestion.setSize(WIDTH,HEIGHT);
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            Dimension dlgSize = dlgAddQuestion.getSize();
            dlgAddQuestion.setLocation(screenSize.width/2 - (dlgSize.width/2),
            screenSize.height/2 - (dlgSize.height/2));

            dlgAddQuestion.addEscapeKeyListener(
            new AbstractAction("escPressed"){
                public void actionPerformed(ActionEvent ae){
                    try{
                        performCancelAction();
                        return;
                    }catch(CoeusException exception){
                         CoeusOptionPane.showErrorDialog(exception.getMessage());
                    }
                }
            });
            dlgAddQuestion.setDefaultCloseOperation(CoeusDlgWindow.DO_NOTHING_ON_CLOSE);
            dlgAddQuestion.addWindowListener(new WindowAdapter(){
                public void windowClosing(WindowEvent we){
                    try{
                        performCancelAction();
                        return;
                    }catch(CoeusException exception){
                         CoeusOptionPane.showErrorDialog(exception.getMessage());
                    }
                }
            });

            dlgAddQuestion.addComponentListener(
            new ComponentAdapter(){
                public void componentShown(ComponentEvent e){
                    setWindowFocus();
                }
            });
    }
    
    private void setWindowFocus(){
        addModifyQuestionForm.txtCode.requestFocusInWindow();
    }
    
    public void itemStateChanged(ItemEvent itemEvent) {
        Object source= itemEvent.getSource();
         if(itemEvent.getStateChange() == ItemEvent.SELECTED) {
             if(source.equals(addModifyQuestionForm.rdBtnYesNo)){
                 performSelectOption(YES_NO_SELECTED);
             }else if(source.equals(addModifyQuestionForm.rdBtnYesNoNA)){
                 performSelectOption(YES_NO_NA_SELECTED);
             }
         }
         isDataModified = true;
    }
    
    /** listens to focus gained event.
     * @param focusEvent focusEvent
     */    
    public void focusGained(FocusEvent focusEvent) {
        if(focusEvent.isTemporary()){ 
            return ;
        }
        
        Object source = focusEvent.getSource();
        if(source.equals(addModifyQuestionForm.txtEffectiveDate)) {
            String effectiveDate = addModifyQuestionForm.txtEffectiveDate.getText();
            effectiveDate = dateUtils.restoreDate(effectiveDate, DATE_SEPARATERS);
            addModifyQuestionForm.txtEffectiveDate.setText(effectiveDate);
        }//End Effective date.
    }
    
    /** listsns to focus lost event.
     * @param focusEvent focusEvent
     */    
    public void focusLost(FocusEvent focusEvent) {
        
        if(focusEvent.isTemporary()){ 
            return ;
        }
        
        Object source = focusEvent.getSource();
        if(source.equals(addModifyQuestionForm.txtEffectiveDate)) {
            String effectiveDate;
            effectiveDate = addModifyQuestionForm.txtEffectiveDate.getText().trim();
            if(effectiveDate.equals(EMPTY_STRING)) {
                return ;
            }
            effectiveDate = dateUtils.formatDate(effectiveDate,DATE_SEPARATERS, DATE_FORMAT_DISPLAY);
            if(effectiveDate == null) {
                CoeusOptionPane.showErrorDialog(coeusMessageResources.parseMessageKey(INVALID_EFFECTIVE_DATE));
                setRequestFocusInThread(addModifyQuestionForm.txtEffectiveDate);
                //addModifyQuestionForm.txtEffectiveDate.requestFocusInWindow();
            }else {
                addModifyQuestionForm.txtEffectiveDate.setText(effectiveDate);
            }
        }//End Effective date validation.
    }
    private void setRequestFocusInThread(final Component component) {
        SwingUtilities.invokeLater( new Runnable() {
            public void run() {
                component.requestFocusInWindow();
            }
        });
    }
    
    /**
     * Getter for property questionId.
     * @return Value of property questionId.
     */
    public java.lang.String getQuestionId() {
        return questionId;
    }
    
    /**
     * Setter for property questionId.
     * @param questionId New value of property questionId.
     */
    public void setQuestionId(java.lang.String questionId) {
        this.questionId = questionId;
    }
    
}
