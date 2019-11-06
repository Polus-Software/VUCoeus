/*
 * EditReportingRequirementController.java
 *
 * Created on July 16, 2004, 2:46 PM
 */

package edu.mit.coeus.award.controller;

import edu.mit.coeus.award.bean.AwardBaseBean;
import edu.mit.coeus.award.bean.AwardReportReqBean;
import edu.mit.coeus.gui.CoeusDlgWindow;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.gui.CoeusAppletMDIForm;
import edu.mit.coeus.utils.query.QueryEngine;
import edu.mit.coeus.award.gui.EditRepRequirementsForm;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.utils.ScreenFocusTraversalPolicy;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.search.gui.CoeusSearch;
import edu.mit.coeus.utils.Utils;
import edu.mit.coeus.utils.DateUtils;
import edu.mit.coeus.utils.KeyConstants;
import edu.mit.coeus.utils.ComboBoxBean;
import edu.mit.coeus.utils.JTextFieldFilter;
import edu.mit.coeus.utils.LimitedPlainDocument;
import edu.mit.coeus.utils.TypeConstants;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.utils.query.Equals;

import javax.swing.*;
import javax.swing.DefaultComboBoxModel;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.FocusListener;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.sql.Date;
import javax.swing.SwingUtilities;
import java.text.ParseException;


/**
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 * @author  jobinelias
 */
public class EditReportingRequirementController extends RepRequirementController implements
ActionListener,FocusListener {
    // For querying
    private QueryEngine queryEngine;
    // To check whether the data or screen is modified
    private boolean modified = false;
    
    private boolean isErrorOccured = false;
    //Represents the dialog box
    private CoeusDlgWindow dlgEditRepRequirment;
    
    //Represents the message resources
    private CoeusMessageResources coeusMessageResources;
    
    /*date utils*/
    private DateUtils dateUtils = new DateUtils();
    
    private static final String DATE_SEPARATERS = ":/.,|-";
    
    private static final String REQUIRED_DATEFORMAT = "dd-MMM-yyyy";
    
    /*the date format*/
    private static final String SIMPLE_DATE_FORMAT = "MM/dd/yyyy";
    
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat(SIMPLE_DATE_FORMAT);
    
    private int returnAction = 0;
    
    /** If OK button selected return a value */
    public static final int OK_SELECTED = 1;
    
    /** If Cancel button selected return a value */
    public static final int CANCEL_SELECTED = 2;
    
    // For the data
    private CoeusVector cvData;
    
    //Title for the Edit Reporting Requirement Dialog Window
    private static final String EDIT_REP_REQ_TITLE = "Edit Reporting Requirements ";
    
    // Represents the form for the Edit Reporting Requirement screen
    private EditRepRequirementsForm editRepRequirementsForm;
    
    //setting up the width and height of the screen
    private static final int WIDTH = 600;
    private static final int HEIGHT = 250;
    
    //Represents the mdiForm
    private CoeusAppletMDIForm mdiForm;
    
    /*Please enter a valid activity date*/
    private static final String VALID_ACTIVITY_DATE = "negotiationActivity_exceptionCode.1155";
    
    // Added for COEUSQA-2794_Specific error message required for Person entry in Award Reporting Requirements maintenance_start
    private static final String ENTER_VALID_PERSON_OR_USE_SEARCH = "repRequirements_exceptionCode.1059";
    // Added for COEUSQA-2794_end
    
    /* The focus flag is set when the person is searched from the search window and to allow the person with
       the same person name and different person ids to be entered.Bug Fix:1273*/
    private boolean focus = false;
    
    private String perName;
    
    private String personNumber;
    
    /** Creates a new instance of EditReportingRequirementController
     * @param awardBaseBean AwardBaseBean
     * @param functionType char
     */
    public EditReportingRequirementController(AwardBaseBean awardBaseBean, char functionType) {
        super(awardBaseBean);
        coeusMessageResources = CoeusMessageResources.getInstance();
        queryEngine = QueryEngine.getInstance();
        
        registerComponents();
        formatFields();
        setFunctionType(functionType);
        postInitComponents();
    }
    
    /** Displaying the Edit dialog window and return the value of the button action
     * @return int
     */
    public int displayEditDialog() {
        dlgEditRepRequirment.setVisible(true);
        return returnAction;
    }
    
    /** Display method */
    public void display() {
        
    }
    /** Format fields method */
    public void formatFields() {
        
    }
    
    /** To get the controlled UI
     * @return java.awt.Component
     */
    public java.awt.Component getControlledUI() {
        return editRepRequirementsForm;
    }
    
    /** Returning the form data
     * @return Object CoeusVector cvData
     */
    public Object getFormData() {
        return cvData;
    }
    
    /**
     * Specifies the Modal window
     */
    private void postInitComponents() {
        mdiForm = CoeusGuiConstants.getMDIForm();
        dlgEditRepRequirment = new CoeusDlgWindow(mdiForm);
        dlgEditRepRequirment.getContentPane().add(editRepRequirementsForm);
        dlgEditRepRequirment.setTitle(EDIT_REP_REQ_TITLE);
        dlgEditRepRequirment.setFont(CoeusFontFactory.getLabelFont());
        dlgEditRepRequirment.setModal(true);
        dlgEditRepRequirment.setResizable(false);
        dlgEditRepRequirment.setSize(WIDTH,HEIGHT);
        
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension dlgSize = dlgEditRepRequirment.getSize();
        dlgEditRepRequirment.setLocation(screenSize.width/2 - (dlgSize.width/2),
        screenSize.height/2 - (dlgSize.height/2));
        
        dlgEditRepRequirment.addEscapeKeyListener(
        new AbstractAction("escPressed"){
            public void actionPerformed(ActionEvent ae) {
                performCancelAction();
                return;
            }
        });
        dlgEditRepRequirment.setDefaultCloseOperation(CoeusDlgWindow.DO_NOTHING_ON_CLOSE);
        dlgEditRepRequirment.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) {
                performCancelAction();
                return;
            }
        });
        
        dlgEditRepRequirment.addComponentListener(
        new ComponentAdapter(){
            public void componentShown(ComponentEvent e){
                requestDefaultFocus();
            }
        });
    }
    
    /** Registering all the components */
    public void registerComponents() {
        
        editRepRequirementsForm = new EditRepRequirementsForm();
        /** Code for focus traversal - start */
        java.awt.Component[] components = { editRepRequirementsForm.cmbStatus,
        editRepRequirementsForm.txtActivityDate,editRepRequirementsForm.txtOverdueCounter,
        editRepRequirementsForm.txtPerson,editRepRequirementsForm.btnPersonSearch,
        editRepRequirementsForm.txtArComments,editRepRequirementsForm.btnOK,
        editRepRequirementsForm.btnCancel
        };
        
        ScreenFocusTraversalPolicy traversePolicy = new ScreenFocusTraversalPolicy( components );
        editRepRequirementsForm.setFocusTraversalPolicy(traversePolicy);
        editRepRequirementsForm.setFocusCycleRoot(true);
        
        /** Code for focus traversal - end */
        editRepRequirementsForm.txtActivityDate.addFocusListener(this);
        editRepRequirementsForm.txtPerson.addFocusListener(this);
        editRepRequirementsForm.btnOK.addActionListener(this);
        editRepRequirementsForm.btnCancel.addActionListener(this);
        editRepRequirementsForm.btnPersonSearch.addActionListener(this);
        editRepRequirementsForm.txtActivityDate.setDocument(new LimitedPlainDocument(11));
        editRepRequirementsForm.txtArComments.setDocument(new LimitedPlainDocument(1000));
        editRepRequirementsForm.txtArComments.setWrapStyleWord(true);
        editRepRequirementsForm.txtArComments.setLineWrap(true);
        
        //closing the screen when enter key is pressed.Has to done for each component
        editRepRequirementsForm.txtActivityDate.addKeyListener(new KeyAdapter(){
            public void keyPressed(KeyEvent kEvent){
                if ( kEvent.getKeyCode() == KeyEvent.VK_ENTER) {
                    
                    editRepRequirementsForm.btnOK.doClick();
                    kEvent.consume();
                }
            }
        });
        
        editRepRequirementsForm.txtArComments.addKeyListener(new KeyAdapter(){
            public void keyPressed(KeyEvent kEvent){
                if ( kEvent.getKeyCode() == KeyEvent.VK_ENTER) {
                    
                    editRepRequirementsForm.btnOK.doClick();
                    kEvent.consume();
                }
            }
        });
        
        editRepRequirementsForm.txtOverdueCounter.addKeyListener(new KeyAdapter(){
            public void keyPressed(KeyEvent kEvent){
                if ( kEvent.getKeyCode() == KeyEvent.VK_ENTER) {
                    
                    editRepRequirementsForm.btnOK.doClick();
                    kEvent.consume();
                }
            }
        });
        
        editRepRequirementsForm.txtPerson.addKeyListener(new KeyAdapter(){
            public void keyPressed(KeyEvent kEvent){
                if ( kEvent.getKeyCode() == KeyEvent.VK_ENTER) {
                    
                    editRepRequirementsForm.btnOK.doClick();
                    kEvent.consume();
                }
            }
        });
        
    }
    
    /** Saving all the values */
    public void saveFormData() {
        DateUtils dtUtils =new DateUtils();
        java.util.Date date;
        
        try {
            if (cvData != null && cvData.size() > 0) {
                for (int index = 0; index < cvData.size(); index++) {
                    AwardReportReqBean awardReportReqBean = (AwardReportReqBean)cvData.get(index);
                    /*setting the values for comments field*/
                    String cmbSel = editRepRequirementsForm.cmbStatus.getSelectedItem().toString();
                    // getting all the values for the status combo box
                    
                    if (!EMPTY.equals(cmbSel) && cmbSel != null && !cmbSel.equals(awardReportReqBean.getReportStatusDescription())) {
                        CoeusVector  cvStatus = (CoeusVector) queryEngine.getDetails(queryKey,KeyConstants.AWARD_REPORT_STATUS);
                        Equals eqStatus =  new Equals("Description" ,cmbSel);
                        CoeusVector cvNewStatus = cvStatus.filter(eqStatus);
                        ComboBoxBean bean = (ComboBoxBean)cvNewStatus.get(0);
                        String code = bean.getCode();
                        awardReportReqBean.setReportStatusDescription(cmbSel);
                        awardReportReqBean.setReportStatusCode(Integer.parseInt(code));
                        modified = true;
                    }
                    /*setting the values for activity date*/
                    String activityDate = editRepRequirementsForm.txtActivityDate.getText().trim();
                    if (EMPTY.equals(activityDate)) {
                        //awardReportReqBean.setActivityDate(null);
                    } else {
                        String dateValue =  dtUtils.formatDate(activityDate, DATE_SEPARATERS, REQUIRED_DATEFORMAT);
                        if(dateValue== null){
                            dateValue =dtUtils.restoreDate(activityDate, DATE_SEPARATERS);
                            if( dateValue == null || dateValue.equals(activityDate)) {
                                awardReportReqBean.setAcType(TypeConstants.UPDATE_RECORD);
                            }else{
                                
                                date = simpleDateFormat.parse(dtUtils.restoreDate(activityDate,DATE_SEPARATERS));
                                awardReportReqBean.setActivityDate(new java.sql.Date(date.getTime()));
                                modified = true;
                            }
                        }else {
                            date = simpleDateFormat.parse(dtUtils.formatDate(activityDate,DATE_SEPARATERS, SIMPLE_DATE_FORMAT));
                            awardReportReqBean.setActivityDate(new java.sql.Date(date.getTime()));
                            modified = true;
                        }
                    }
                    /*setting the values for overdue counter*/
                    String overDueCounter = editRepRequirementsForm.txtOverdueCounter.getText().trim();
                    if (!EMPTY.equals(overDueCounter)) {
                        int overDueVal = Integer.parseInt(overDueCounter);
                        if (overDueVal != awardReportReqBean.getOverdueCounter()) {
                            awardReportReqBean.setOverdueCounter(overDueVal);
                            modified = true;
                        }
                    }
                    
                    /*setting the values for Person Name*/
                    String personName = editRepRequirementsForm.txtPerson.getText();
                    if (!EMPTY.equals(personName)) {
                        boolean isValidName = authenticatePerson(personName , focus);
                        if (isValidName && !personName.equals(awardReportReqBean.getFullName())) {
                            
                            if(focus){
                                awardReportReqBean.setFullName(perName);
                                awardReportReqBean.setPersonId(personNumber);
                            }else{
                                awardReportReqBean.setFullName(getAuthenticatedPersonName());
                                awardReportReqBean.setPersonId(getAuthenticatedPersonId());
                            }
                            focus = false;
                            modified = true;
                        }
                        focus = false;
                    }
                    
                    /*setting the values for comments field*/
                    String comments = editRepRequirementsForm.txtArComments.getText().trim();
                    if (EMPTY.equals(comments)) {
                        //awardReportReqBean.setComments(null);
                    } else if (!comments.equals(awardReportReqBean.getComments())){
                        awardReportReqBean.setComments(comments);
                        modified = true;
                    }
                    if (modified) {
                        awardReportReqBean.setAcType(TypeConstants.UPDATE_RECORD);
                        queryEngine.update(queryKey,awardReportReqBean);
                    }
                }
            }
            //focus = false;
            
        } catch (CoeusException coeusException) {
            coeusException.printStackTrace();
        } catch (ParseException parseException) {
            parseException.printStackTrace();
        }
    }
    
    /** Setting up the value in the fields
     * @param data Object
     */
    public void setFormData(Object data) {
        try {
            if (data != null) {
                CoeusVector  cvStatus = (CoeusVector) queryEngine.getDetails(queryKey,KeyConstants.AWARD_REPORT_STATUS);
                ComboBoxBean comboBoxBean = new ComboBoxBean();
                comboBoxBean.setDescription(EMPTY);
                comboBoxBean.setCode(EMPTY);
                cvStatus.add(0,comboBoxBean);
                DefaultComboBoxModel cmbModel = new DefaultComboBoxModel(cvStatus);
                editRepRequirementsForm.cmbStatus.setModel(cmbModel);
                
            }
            editRepRequirementsForm.txtOverdueCounter.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
            editRepRequirementsForm.txtOverdueCounter.setDocument(new JTextFieldFilter(JTextFieldFilter.NUMERIC,3));
            cvData = (CoeusVector)data;
            
        } catch (CoeusException coeusException) {
            coeusException.printStackTrace();
        }
    }
    
    /** validate method
     * @return boolean
     * @throws CoeusUIException Throwing the CoeusUIException
     */
    public boolean validate() throws edu.mit.coeus.exception.CoeusUIException {
        
        if (isErrorOccured) {
            isErrorOccured = false;
            return false;
        }
        //set focus to this component if this field does not have focus so as to
        //have the required date format for parsing
        String activityDate = editRepRequirementsForm.txtActivityDate.getText().trim();
        activityDate = dateUtils.restoreDate(activityDate,DATE_SEPARATERS);
        
        if(!activityDate.equals(EMPTY)) {
            //String activityFormatDate = dateUtils.formatDate(
            //activityDate,DATE_SEPARATERS, REQUIRED_DATEFORMAT);
            boolean validDate = dateUtils.validateDate(activityDate, DATE_SEPARATERS);
            
            if(!validDate) {//activityFormatDate == null){
                CoeusOptionPane.showErrorDialog(coeusMessageResources.parseMessageKey(VALID_ACTIVITY_DATE));
                setRequestFocusInThread(editRepRequirementsForm.txtActivityDate);
                return false;
            }else{
                modified = true;
            }
        }
        
        boolean isCheckSuccessful =true;
        String personName = editRepRequirementsForm.txtPerson.getText().trim();
        if (!EMPTY.equals(personName)) {
            isCheckSuccessful = authenticatePerson(personName ,focus);
        }
        if(isCheckSuccessful) {
            editRepRequirementsForm.txtPerson.setText(personName);
            modified = true;
        } else {
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    editRepRequirementsForm.txtPerson.requestFocus();
                }
            });
            return false;
        }
        return true;
    }
    
    /** Action performed method
     * @param actionEvent ActionEvent
     */
    public void actionPerformed(java.awt.event.ActionEvent actionEvent) {
        Object source = actionEvent.getSource();
        if (source.equals(editRepRequirementsForm.btnOK)) {
            performOKOperation();
            returnAction = OK_SELECTED;
        }
        if (source.equals(editRepRequirementsForm.btnCancel)) {
            returnAction = CANCEL_SELECTED;
            performCancelAction();
        }
        if (source.equals(editRepRequirementsForm.btnPersonSearch)) {
            performPersonSearch();
        }
    }
    
    /**
     * OK Operation
     */
    private void performOKOperation() {
        try {
            if (validate()) {
                saveFormData();
                dlgEditRepRequirment.setVisible(false);
            } else {
                return;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
    /**
     * Cancel Operation
     */
    private void performCancelAction(){
        dlgEditRepRequirment.setVisible(false);
    }
    /**
     * default focus on load
     */
    private void requestDefaultFocus(){
        editRepRequirementsForm.btnCancel.requestFocusInWindow();
    }
    /** this method Closes this window
     */
    private void closeDialog() {
        
        dlgEditRepRequirment.setVisible(false);
    }
    /**
     * person search
     */
    private void performPersonSearch() {
        try {
            dlgEditRepRequirment.setCursor( new Cursor(Cursor.WAIT_CURSOR));
            CoeusSearch coeusSearch = new CoeusSearch(
            mdiForm, "PERSONSEARCH", 1);
            coeusSearch.showSearchWindow();
            HashMap personSelected = coeusSearch.getSelectedRow();
            if (personSelected != null && !personSelected.isEmpty() ) {
                String number = Utils.convertNull(personSelected.get("PERSON_ID"));
                String fullName = Utils.convertNull(personSelected.get(
                "FULL_NAME"));
                perName = fullName;
                personNumber = number;
                if (fullName.length() > 0) {
                    editRepRequirementsForm.txtPerson.setText(Utils.convertNull(fullName));
                    focus = true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally{
            dlgEditRepRequirment.setCursor( new Cursor( Cursor.DEFAULT_CURSOR ));
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
    /** listens to focus gained event.
     * @param focusEvent focusEvent
     */
    public void focusGained(FocusEvent focusEvent) {
        if (focusEvent.isTemporary()) return;
        Object source = focusEvent.getSource();
        if(source.equals(editRepRequirementsForm.txtActivityDate)){
            String activityDate;
            activityDate = editRepRequirementsForm.txtActivityDate.getText().trim();
            if(activityDate.equals(EMPTY)) return;
            String activityFormatDate = dateUtils.restoreDate(activityDate,DATE_SEPARATERS);
            if(activityFormatDate == null){
                isErrorOccured = true;
                CoeusOptionPane.showErrorDialog(coeusMessageResources.parseMessageKey(VALID_ACTIVITY_DATE));
                setRequestFocusInThread(editRepRequirementsForm.txtActivityDate);
            }else{
                editRepRequirementsForm.txtActivityDate.setText(activityFormatDate);
            }
        }
        
    }
    /** listens to focus lost event.
     * @param focusEvent focusEvent
     */
    public void focusLost(FocusEvent focusEvent) {
        if (focusEvent.isTemporary()) return;
        Object source = focusEvent.getSource();
        if(source.equals(editRepRequirementsForm.txtActivityDate)){
            String activityDate;
            activityDate = editRepRequirementsForm.txtActivityDate.getText().trim();
            if(activityDate.equals(EMPTY)) return;
            String activityFormatDate = dateUtils.formatDate(
            activityDate,DATE_SEPARATERS, REQUIRED_DATEFORMAT);
            if(activityFormatDate == null){
                isErrorOccured = true;
                CoeusOptionPane.showErrorDialog(coeusMessageResources.parseMessageKey(VALID_ACTIVITY_DATE));
                setRequestFocusInThread(editRepRequirementsForm.txtActivityDate);
            }else{
                editRepRequirementsForm.txtActivityDate.setText(activityFormatDate);
                modified = true;
            }
        }
        
        if(source.equals(editRepRequirementsForm.txtPerson)) {
            
            boolean isCheckSuccessful =true;
            String personName = editRepRequirementsForm.txtPerson.getText().trim();
            if (!EMPTY.equals(personName)) {
                isCheckSuccessful = authenticatePerson(personName , focus);
            }
            if(isCheckSuccessful) {
                editRepRequirementsForm.txtPerson.setText(personName);
                modified = true;
            } else {
                isErrorOccured = true;
                /* Added for COEUSQA-2794_Specific error message required for Person entry in 
                 Award Reporting Requirements maintenance_start*/
                CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(ENTER_VALID_PERSON_OR_USE_SEARCH));
                editRepRequirementsForm.txtPerson.setText(CoeusGuiConstants.EMPTY_STRING);
                /* Added for COEUSQA-2794_Specific error message required for Person entry in 
                 Award Reporting Requirements maintenance_end*/
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        editRepRequirementsForm.txtPerson.requestFocus();
                    }
                });
            }
        }
        
    }
    
    /**
     * Getter for property modified.
     * @return Value of property modified.
     */
    public boolean isModified() {
        return modified;
    }
    
    /**
     * Setter for property modified.
     * @param modified New value of property modified.
     */
    public void setModified(boolean modified) {
        this.modified = modified;
    }
}
