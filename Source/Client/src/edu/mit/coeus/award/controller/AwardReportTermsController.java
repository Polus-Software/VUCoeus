/** Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

/* PMD check performed, and commented unused imports and variables on 27-AUG-2010
 * by Keerthy Jayaraj
 */
package edu.mit.coeus.award.controller;

import edu.mit.coeus.award.AwardConstants;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;
//import javax.swing.AbstractCellEditor;

import edu.mit.coeus.gui.*;
import edu.mit.coeus.brokers.*;
import edu.mit.coeus.bean.*;
import edu.mit.coeus.gui.event.BeanEvent;
import edu.mit.coeus.award.gui.*;
import edu.mit.coeus.award.bean.*;
import edu.mit.coeus.utils.*;
import edu.mit.coeus.utils.DateUtils.*;
import edu.mit.coeus.utils.query.*;
import edu.mit.coeus.exception.*;
import java.text.*;


/*
 * AwardReportTerms.java
 * @author  ajaygm
 * Created on June 7, 2004, 3:09 PM
 */

public class AwardReportTermsController extends AwardController
implements ActionListener, ItemListener{
    
    private AwardReportTermsBean awardReportTermsBean = new AwardReportTermsBean ();
    private AwardReportTermsBean awardReportTermsBeanDB; 
    private AwardDetailsBean awardDetailsBean = new AwardDetailsBean ();
    
    private ReportBean reportBean = new ReportBean () ;
    
    private CoeusDlgWindow dlgAwardReportTermsForm ;
    
    private CoeusAppletMDIForm mdiForm = CoeusGuiConstants.getMDIForm ();
    
    private CoeusMessageResources coeusMessageResources;
    
    private AwardReportTermsForm awardReportTermsForm;
    
    private QueryEngine queryEngine;
    
    private char functionType;
    
    
    /*Function Type to get valid report Frequency */
    private static final char GET_VALID_REPORT_FREQUENCY = 'N';
    
    private static final String EMPTY = "";
    
    private static final String WINDOW_TITLE = "Award Report Terms";
    private static final int WIDTH = 610;
    private static final int HEIGHT = 375;
    
    private int rowId;
    
    private int noOfCopies = 1;
    
    private CoeusVector cvAwardDetailsBean;
    private CoeusVector cvReportTerms;
    private CoeusVector cvAwardRepTermsBean;
    private CoeusVector cvReportTypeCode;
    private CoeusVector cvFrequencyBase;
    private CoeusVector cvReportClassFrequency;
    private CoeusVector cvOspDistribution;
    private CoeusVector cvDeletedItem;
    private CoeusVector cvAllData;
    private CoeusVector cvLastRowDeleted;
    
    private CoeusVector cvClassRelatedData;
    
    private static final int CONTACT_INDEX = 0;
    private static final int NAME_INDEX = 1;
    private static final int COPIES_INDEX = 2;
    
    private static String reportClass;
    private static String reportClassDescription;
    private boolean modified = false;
    
    private AwardReportTermsTableModel awardReportTermsTableModel;
    private AwardReportTermsRenderer awardReportTermsRenderer;
    private AwardReportTermsEditor awardReportTermsEditor;
    
    private java.util.Date utilDueDate;
    private static final String DATE_SEPARATERS = ":/.,|-";
    private static final String DATE_FORMAT_DISPLAY = "dd-MMM-yyyy";
    private static final String SIMPLE_DATE_FORMAT = "MM/dd/yyyy";
    private DateUtils dateUtils = new DateUtils ();
    //    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat(SIMPLE_DATE_FORMAT);
    private DateUtils dtUtils = new DateUtils ();
    private SimpleDateFormat dtFormat =
    new SimpleDateFormat ("MM/dd/yyyy");
    
    private int intialTypeCode;
    
    private boolean delPressed = false;
    
    /*For setting data in Add*/
    CoeusVector cvAddedPersons = new CoeusVector ();
    
    //    private static final String REPORT_CLASS_CODE = "reportClassCode";
    //    private static final String REPORT_CODE = "reportCode";
    
    private static final String NO_REPORT_TYPE = "awardReportTerms_exceptionCode.1751";
    private static final String CANNOT_ADD_ROW = "awardReportTerms_exceptionCode.1752";
    private static final String DUPLICATE_ROW = "awardReportTerms_exceptionCode.1753";
    private static final String ROW_TO_DELETE = "orgIDCPnl_exceptionCode.1097";
    private static final String DUPILCATE_CONTACT = "awardReportTerms_exceptionCode.1754";
    private static final String FREQUENCY_VALIDATE = "awardReportTerms_exceptionCode.1755";
    private static final String OSP_VALIDATE = "awardReportTerms_exceptionCode.1756";
    private static final String DELETE_CONFIRMATION = "instPropIPReview_exceptionCode.1353";
    private static final String SAVE_CHANGES = "saveConfirmCode.1002";
    
    //Added with COEUSDEV-561:Award Sync - Error syncing report after removing recipient:Start
    private static final String ERRKEY_INVALID_DUE_DATE = "awardReportTerms_exceptionCode.1757";
    //Award Report Terms Fields
    private static final String FREQUENCY_DISTRIBUTION = "frequencyDescription";
    private static final String FREQUENCY_BASE_DESCRIPTION = "frequencyBaseDescription";
    private static final String OSP_DISTRIBUTION_DESCRIPTION = "ospDistributionDescription";
    private static final String DUE_DATE = "dueDate";
    private static final String DEFAULT_DUE_DATE = "01-Jan-1900";// DD-MMM-YYYY
    //COEUSDEV-561:End
    
    //Represents the string for conneting to the servlet and getting Proposal Log data
    private static final String GET_SERVLET = "/AwardMaintenanceServlet";
    
    private static final String connect = CoeusGuiConstants.CONNECTION_URL + GET_SERVLET;
    
    
    private ComboBoxBean typeCmoboBean = new ComboBoxBean ();
    
    
    /** Creates a new instance of AwardReportTerm */
    public AwardReportTermsController (AwardBaseBean awardBaseBean, char funType) {
        super (awardBaseBean);
        queryEngine = QueryEngine.getInstance ();
        coeusMessageResources = CoeusMessageResources.getInstance ();
        //        this.awardReportTermsBean = awardReportTermsBean;
        //this.cvReportTerms = cvReportTerms;
        this.functionType = funType;
        registerComponents ();
        //setFormData(cvReportTerms);
        postInitComponents ();
        setFunctionType (funType);
    }
    
    
    //    public void setReportsData(CoeusVector cvReportTerms){
    //        this.cvReportTerms = cvReportTerms;
    //        //for (int index=0;index<cvReportTerms.size;index++){
    //            this.awardReportTermsBean = (AwardReportTermsBean)cvReportTerms.elementAt(0);
    //        //}
    //    }
    
    public void postInitComponents (){
        dlgAwardReportTermsForm = new CoeusDlgWindow (mdiForm);
        dlgAwardReportTermsForm.setResizable (false);
        dlgAwardReportTermsForm.setModal (true);
        dlgAwardReportTermsForm.getContentPane ().add (awardReportTermsForm);
        
        dlgAwardReportTermsForm.setTitle (WINDOW_TITLE);
        dlgAwardReportTermsForm.setFont (CoeusFontFactory.getLabelFont ());
        dlgAwardReportTermsForm.setSize (WIDTH, HEIGHT);
        
        Dimension screenSize = Toolkit.getDefaultToolkit ().getScreenSize ();
        Dimension dlgSize = dlgAwardReportTermsForm.getSize ();
        dlgAwardReportTermsForm.setLocation (screenSize.width/2 - (dlgSize.width/2),
        screenSize.height/2 - (dlgSize.height/2));
        
        dlgAwardReportTermsForm.addComponentListener (
        new ComponentAdapter (){
            public void componentShown (ComponentEvent e){
                requestDefaultFocus ();
            }
        });
        
        dlgAwardReportTermsForm.addEscapeKeyListener (new AbstractAction ("escPressed"){
            public void actionPerformed (ActionEvent ae){
                /*If esc is pressed ,when the focus is in the due date field
                 *the modified flag is not set.Hence to check weather the Due date is modified.
                 */
                if(functionType == TypeConstants.ADD_MODE &&
                !EMPTY.equals (awardReportTermsForm.txtDueDate.getText ().trim ())){
                    modified = true;
                }
                
                /*If esc is pressed ,when the focus is in the due date field,the valdiation for date
                 *is not done.
                 */
                if(!EMPTY.equals (awardReportTermsForm.txtDueDate.getText ().trim ())){
                    String date = awardReportTermsForm.txtDueDate.getText ().trim();
                    date = dateUtils.restoreDate(date, DATE_SEPARATERS);
                    date = dateUtils.formatDate (date, DATE_SEPARATERS, DATE_FORMAT_DISPLAY);
                    if(date == null) {
                        CoeusOptionPane.showInfoDialog (coeusMessageResources.parseMessageKey(ERRKEY_INVALID_DUE_DATE));
                        return ;
                    }
                }
                    
                    performCancelAction ();
                }
            });
            
            dlgAwardReportTermsForm.setDefaultCloseOperation (CoeusDlgWindow.DO_NOTHING_ON_CLOSE);
            dlgAwardReportTermsForm.addWindowListener (new WindowAdapter (){
                public void windowOpening (WindowEvent we){
                }
                public void windowClosing (WindowEvent we){
                    performCancelAction ();
                }
            });
            //code for disposing the window ends
        }
        
        public void display () {
            if(cvReportTypeCode.size () > 0){
                if(functionType == TypeConstants.MODIFY_MODE){
                    int rowCount =awardReportTermsForm.tblRecipients.getRowCount ();
                    if(rowCount > 0){
                        awardReportTermsForm.tblRecipients.setRowSelectionInterval (0,0);
                    }
                }
                dlgAwardReportTermsForm.setVisible (true);
                
            }else{
                CoeusOptionPane.showInfoDialog (
                coeusMessageResources.parseMessageKey (NO_REPORT_TYPE));
                return ;
            }
        }
        
        //Added with case 2796:Sync To PArent
        public void formatFields () {
            if(!awardBaseBean.isParent()){
                awardReportTermsForm.chkSync.setEnabled(false);
            }
        }
        //2796 End
        
        public Component getControlledUI () {
            return awardReportTermsForm;
        }
        
        public Object getFormData () {
            return null;
        }
        
        public void registerComponents () {
            //Add listeners to all the buttons
            awardReportTermsForm = new AwardReportTermsForm ();
            awardReportTermsForm.btnOK.addActionListener (this);
            awardReportTermsForm.btnCancel.addActionListener (this);
            awardReportTermsForm.btnAdd.addActionListener (this);
            awardReportTermsForm.btnDelete.addActionListener (this);
            
            
            /** Code for focus traversal - start */
            java.awt.Component[] components = { awardReportTermsForm.cmbType,
            awardReportTermsForm.cmbFrequency,awardReportTermsForm.cmbOSPDistribution,
            awardReportTermsForm.cmbFrequencyBase,awardReportTermsForm.txtDueDate,
            awardReportTermsForm.tblRecipients,awardReportTermsForm.btnAdd,
            awardReportTermsForm.btnDelete,awardReportTermsForm.btnOK,
            awardReportTermsForm.btnCancel
            };
            
            ScreenFocusTraversalPolicy traversePolicy = new ScreenFocusTraversalPolicy ( components );
            awardReportTermsForm.setFocusTraversalPolicy (traversePolicy);
            awardReportTermsForm.setFocusCycleRoot (true);
            
            /** Code for focus traversal - end */
            
            awardReportTermsForm.cmbType.addItemListener (this);
            awardReportTermsForm.cmbFrequency.addItemListener (this);
            awardReportTermsForm.cmbOSPDistribution.addItemListener (this);
            awardReportTermsForm.cmbFrequencyBase.addItemListener(this);//Added for Bug fix #1807
            
            CustomFocusAdapter customFocusAdapter = new CustomFocusAdapter ();
            awardReportTermsForm.txtDueDate.addFocusListener (customFocusAdapter);
            //if(functionType == TypeConstants.MODIFY_MODE){
            awardReportTermsRenderer = new AwardReportTermsRenderer ();
            awardReportTermsEditor = new AwardReportTermsEditor ();
            awardReportTermsTableModel = new AwardReportTermsTableModel ();
            awardReportTermsForm.tblRecipients.setModel (awardReportTermsTableModel);
            //}
            
            
            awardReportTermsForm.cmbFrequency.addKeyListener (new KeyAdapter (){
                public void keyPressed (KeyEvent kEvent){
                    if( kEvent.getKeyCode () == KeyEvent.VK_DELETE &&
                    kEvent.getSource () instanceof CoeusComboBox ){
                        //                            ComboBoxBean emptyBean = new ComboBoxBean(EMPTY,EMPTY);
                        //                            cvReportClassFrequency.add(0,emptyBean);
                        awardReportTermsForm.cmbFrequency.setModel (new DefaultComboBoxModel (cvReportClassFrequency));
                        awardReportTermsForm.cmbFrequency.requestFocusInWindow ();
                        kEvent.consume ();
                        if(dlgAwardReportTermsForm.isVisible ()){
                            delPressed = true;
                            ItemEvent itemEvent = new ItemEvent (awardReportTermsForm.cmbType, 0, null, ItemEvent.SELECTED);
                            itemStateChanged (itemEvent);
                        }
                    }
                }
            });
            
            awardReportTermsForm.cmbFrequencyBase.addKeyListener (new KeyAdapter (){
                public void keyPressed (KeyEvent kEvent){
                    if( kEvent.getKeyCode () == KeyEvent.VK_DELETE &&
                    kEvent.getSource () instanceof CoeusComboBox ){
                        //                            ComboBoxBean emptyBean = new ComboBoxBean(EMPTY,EMPTY);
                        //                            cvFrequencyBase.add(0,emptyBean);
                        //                            awardReportTermsForm.cmbFrequencyBase.setModel(new DefaultComboBoxModel(cvFrequencyBase));
                        awardReportTermsForm.cmbFrequencyBase.requestFocusInWindow ();
                        kEvent.consume ();
                        if(dlgAwardReportTermsForm.isVisible ()){
                            delPressed = true;
                            ItemEvent itemEvent = new ItemEvent (awardReportTermsForm.cmbFrequency, 0, null, ItemEvent.SELECTED);
                            itemStateChanged (itemEvent);
                        }
                    }
                }
            });
            
            awardReportTermsForm.cmbOSPDistribution.addKeyListener (new KeyAdapter (){
                public void keyPressed (KeyEvent kEvent){
                    if( kEvent.getKeyCode () == KeyEvent.VK_DELETE &&
                    kEvent.getSource () instanceof CoeusComboBox ){
                        //                            ComboBoxBean emptyBean = new ComboBoxBean(EMPTY,EMPTY);
                        //                            cvReportClassFrequency.add(0,emptyBean);
                        awardReportTermsForm.cmbOSPDistribution.setModel (new DefaultComboBoxModel (cvOspDistribution));
                        awardReportTermsForm.cmbOSPDistribution.requestFocusInWindow ();
                        kEvent.consume ();
                    }
                }
            });
            setTableEditors ();
        }
        
        /** To set the default focus for the component
         */
        public void requestDefaultFocus (){
            //Bug Fix : 1122 Start 
            awardReportTermsForm.cmbType.requestFocus ();
            //Bug Fix : 1122 End 
        }
        
        public void saveFormData () {
            awardReportTermsEditor.stopCellEditing ();
            boolean flag = true;
            //Added with case 2796:Sync To Parent
            boolean syncRequired = awardReportTermsForm.chkSync.isSelected();
            
            try{
                if(cvDeletedItem != null && cvDeletedItem.size () >0){
                    for(int index = 0; index < cvDeletedItem.size (); index++){
                        AwardReportTermsBean deletedReportTermsBean = (AwardReportTermsBean)
                        cvDeletedItem.get (index);
                        String acType = deletedReportTermsBean.getAcType ();
                        //Modified for COEUSDEV-561:Award Sync - Error syncing report after removing recipient:Start
//                        if(acType != null && acType.equals (TypeConstants.INSERT_RECORD)){
                        if(TypeConstants.INSERT_RECORD.equals(acType) && deletedReportTermsBean.getRowId()<=0){
                        //COEUSDEV-561:End
                            continue ;
                        }else {
                            deletedReportTermsBean.setAcType (TypeConstants.DELETE_RECORD);
                            deletedReportTermsBean.setMitAwardNumber (awardDetailsBean.getMitAwardNumber ());
                            deletedReportTermsBean.setSequenceNumber (awardDetailsBean.getSequenceNumber ());
                            //                            System.out.println("Time: "+deletedReportTermsBean.getDueDate().getTime ());
                            //                            System.out.println("Aw Time is :  "+deletedReportTermsBean.getAw_DueDate ().getTime ());
                            deletedReportTermsBean.setSyncRequired(syncRequired);//2796
                            queryEngine.delete (queryKey, deletedReportTermsBean);
                            
                            
                            BeanEvent beanEvent = new BeanEvent ();
                            beanEvent.setSource (this);
                            beanEvent.setBean (deletedReportTermsBean);
                            fireBeanAdded (beanEvent);
                            //return ;
                        }
                    }
                }
                
                
            /*The code is addedd since if the last row is delete and a new rolodex is added , the deleted row  will go
             *as update, rather than delete
             */
                if(cvReportTerms!= null && cvLastRowDeleted!=null && cvReportTerms.size () >0 && cvLastRowDeleted.size ()>0){
                    /*Code Duplicated ,add this in a method afterwards*/
                    
                    for(int index = 0; index < cvReportTerms.size (); index++){
                        AwardReportTermsBean reportTermsBean = (AwardReportTermsBean)cvReportTerms.get (index);
                        //Commented COEUSQA-2408 : CLONE -Award Sync - MOdify and sync reports not working correctly                          
                        //When modifying the report type to other , the existing report type will get modified instead of inserting a new record
//                        if(intialTypeCode != reportTermsBean.getReportCode ()){
//                            reportTermsBean.setAcType (TypeConstants.INSERT_RECORD);
//                        /*Making the row Id 0 , since it is Inserting
//                         *and the bean has already a row id , it will be replacing
//                         *the old row .Hence for generating a new row set it as 0
//                         */
//                            reportTermsBean.setRowId (0);
//                        }
                        //COEUSQA-2408 : End
                        if(reportTermsBean.getAcType ()!= null){
                            if(reportTermsBean.getAcType ().equals (TypeConstants.UPDATE_RECORD)){
                                //Modified for COEUSDEV-561:Award Sync - Error syncing report after removing recipient:Start
                                checkForGroup (reportTermsBean,cvClassRelatedData);
                                //COEUSDEV-561:End
                                reportTermsBean.setAcType (TypeConstants.DELETE_RECORD);
                                reportTermsBean.setSyncRequired(syncRequired);//2796
                                queryEngine.delete (queryKey, reportTermsBean);
                                reportTermsBean.setAcType (TypeConstants.INSERT_RECORD);
                                rowId = rowId+1;
                                //                            System.out.println("rowID:"+rowId);
                                //                            System.out.println("Date:"+reportTermsBean.getDueDate ().getTime());
                                
                                reportTermsBean.setRowId (rowId);
                                queryEngine.insert (queryKey, reportTermsBean);
                                
                                if((awardReportTermsBean.getContactTypeCode () == -1 || awardReportTermsBean.getContactTypeCode () == 0 )
                                &&(awardReportTermsBean.getRolodexId () == -1 || awardReportTermsBean.getRolodexId () == 0)&& flag ){
                                    
                                    awardReportTermsBean.setMitAwardNumber (awardBaseBean.getMitAwardNumber ());
                                    awardReportTermsBean.setSequenceNumber (awardBaseBean.getSequenceNumber ());
                                    queryEngine.delete (queryKey,awardReportTermsBean);
                                    flag = true;
                                }
                                
                                
                                
                                BeanEvent beanEvent = new BeanEvent ();
                                beanEvent.setSource (this);
                                beanEvent.setBean (reportTermsBean);
                                fireBeanAdded (beanEvent);
                                
                            }else if(reportTermsBean.getAcType ().equals (TypeConstants.INSERT_RECORD)){
                                //Modified for COEUSDEV-561:Award Sync - Error syncing report after removing recipient:Start
                                checkForGroup (reportTermsBean,cvClassRelatedData);
                                reportTermsBean.setMitAwardNumber(awardDetailsBean.getMitAwardNumber());
                                reportTermsBean.setSequenceNumber(awardDetailsBean.getSequenceNumber());
                                //COEUSDEV-561:End
                                reportTermsBean.setAcType (TypeConstants.INSERT_RECORD);
                                if(reportTermsBean.getRowId () == 0){
                                    rowId = rowId+1;
                                    reportTermsBean.setRowId (rowId);
                                }
                                reportTermsBean.setSyncRequired(syncRequired);//2796
                                queryEngine.insert (queryKey, reportTermsBean);
                                
                                if((awardReportTermsBean.getContactTypeCode () == -1 || awardReportTermsBean.getContactTypeCode () == 0 )
                                &&(awardReportTermsBean.getRolodexId () == -1 || awardReportTermsBean.getRolodexId () == 0)&& flag ){
                                    
                                    awardReportTermsBean.setMitAwardNumber (awardBaseBean.getMitAwardNumber ());
                                    awardReportTermsBean.setSequenceNumber (awardBaseBean.getSequenceNumber ());
                                    queryEngine.delete (queryKey,awardReportTermsBean);
                                    flag = true;
                                }
                                
                                BeanEvent beanEvent = new BeanEvent ();
                                beanEvent.setSource (this);
                                beanEvent.setBean (reportTermsBean);
                                fireBeanAdded (beanEvent);
                            }
                        }
                    }
                    
                    
                    
                    AwardReportTermsBean termsBean = (AwardReportTermsBean)cvLastRowDeleted.get (0);
                    //COEUSDEV 210: Award Syncing Issues
                    termsBean.setSyncRequired(syncRequired);//2796
                    //COEUSDEV 210 End
                    queryEngine.delete (queryKey,termsBean);
                    BeanEvent beanEvent = new BeanEvent ();
                    beanEvent.setSource (this);
                    beanEvent.setBean (termsBean);
                    fireBeanAdded (beanEvent);
                    
                }else if(cvReportTerms!= null && cvReportTerms.size () >0){
                    for(int index = 0; index < cvReportTerms.size (); index++){
                        AwardReportTermsBean reportTermsBean = (AwardReportTermsBean)cvReportTerms.get (index);
                        //Commented for COEUSQA-2408 : CLONE -Award Sync - MOdify and sync reports not working correctly  
                        //When modifying the report type to other , the existing report type will get modified instead of inserting a new record
//                        if(intialTypeCode != reportTermsBean.getReportCode ()){
//                            reportTermsBean.setAcType (TypeConstants.INSERT_RECORD);
//                        /*Making the row Id 0 , since it is Inserting
//                         *and the bean has already a row id , it will be replacing
//                         *the old row .Hence for generating a new row set it as 0
//                         */
//                            reportTermsBean.setRowId (0);
//                        }
                        //COEUSQA-2408 : End
                        if(reportTermsBean.getAcType ()!= null){
                            if(reportTermsBean.getAcType ().equals (TypeConstants.UPDATE_RECORD)){
                                //Modified for COEUSDEV-561:Award Sync - Error syncing report after removing recipient:Start
                                checkForGroup (reportTermsBean,cvClassRelatedData);
                                //COEUSDEV-561:End
                                reportTermsBean.setAcType (TypeConstants.DELETE_RECORD);
                                reportTermsBean.setSyncRequired(syncRequired);//2796
                                queryEngine.delete (queryKey, reportTermsBean);
                                
                                reportTermsBean.setAcType (TypeConstants.INSERT_RECORD);
                                rowId = rowId+1;
                                //                            System.out.println("rowID:"+rowId);
                                //                            System.out.println("Date:"+reportTermsBean.getDueDate ().getTime());
                                
                                reportTermsBean.setRowId (rowId);
                                queryEngine.insert (queryKey, reportTermsBean);
                                
                                if((awardReportTermsBean.getContactTypeCode () == -1 || awardReportTermsBean.getContactTypeCode () == 0 )
                                &&(awardReportTermsBean.getRolodexId () == -1 || awardReportTermsBean.getRolodexId () == 0)&& flag ){
                                    
                                    awardReportTermsBean.setMitAwardNumber (awardBaseBean.getMitAwardNumber ());
                                    awardReportTermsBean.setSequenceNumber (awardBaseBean.getSequenceNumber ());
                                    queryEngine.delete (queryKey,awardReportTermsBean);
                                    flag = true;
                                }
                                BeanEvent beanEvent = new BeanEvent ();
                                beanEvent.setSource (this);
                                beanEvent.setBean (reportTermsBean);
                                fireBeanAdded (beanEvent);
                                
                            }else if(reportTermsBean.getAcType ().equals (TypeConstants.INSERT_RECORD)){
                                //Modified for COEUSDEV-561:Award Sync - Error syncing report after removing recipient:Start
                                checkForGroup (reportTermsBean,cvClassRelatedData);
                                reportTermsBean.setMitAwardNumber(awardDetailsBean.getMitAwardNumber());
                                reportTermsBean.setSequenceNumber(awardDetailsBean.getSequenceNumber());
                                //COEUSDEV-561:End
                                reportTermsBean.setAcType (TypeConstants.INSERT_RECORD);
                                if(reportTermsBean.getRowId () == 0){
                                    rowId = rowId+1;
                                    reportTermsBean.setRowId (rowId);
                                }
                                reportTermsBean.setSyncRequired(syncRequired);//2796
                                queryEngine.insert (queryKey, reportTermsBean);
                                
                                if((awardReportTermsBean.getContactTypeCode () == -1 || awardReportTermsBean.getContactTypeCode () == 0 )
                                &&(awardReportTermsBean.getRolodexId () == -1 || awardReportTermsBean.getRolodexId () == 0)&& flag ){
                                    
                                    awardReportTermsBean.setMitAwardNumber (awardBaseBean.getMitAwardNumber ());
                                    awardReportTermsBean.setSequenceNumber (awardBaseBean.getSequenceNumber ());
                                    queryEngine.delete (queryKey,awardReportTermsBean);
                                    flag = true;
                                }
                                
                                //                            if(cvAllData.size ()>0){
                                //                                if(awardReportTermsBean.getContactTypeCode () == 0 && awardReportTermsBean.getRolodexId () == 0 && flag){
                                //                                    queryEngine.delete (queryKey,awardReportTermsBean);
                                //                                    flag = true;
                                //                                }
                                //                            }
                                
                                BeanEvent beanEvent = new BeanEvent ();
                                beanEvent.setSource (this);
                                beanEvent.setBean (reportTermsBean);
                                fireBeanAdded (beanEvent);
                            }
                        }
                    }
                }/*Added else since  'No recipients' was getting displayed twice when 3 recipients were deleted simultaneously
                 *so it wolud come to this condition,addded before the cvRep>0
                 */
                else if(cvLastRowDeleted != null && cvLastRowDeleted.size ()>0){
                    AwardReportTermsBean reportTermsBean = (AwardReportTermsBean)cvLastRowDeleted.get(0);
                    //Added for COEUSDEV-561:Award Sync - Error syncing report after removing recipient:Start
                    Equals eqReportClassCode = new Equals("reportClassCode",new Integer(reportClass));
                    And eqReportClassCodeAndActiveBeans = new And(eqReportClassCode,CoeusVector.FILTER_ACTIVE_BEANS);
                    CoeusVector allClassReports = queryEngine.executeQuery(queryKey,AwardReportTermsBean.class,eqReportClassCodeAndActiveBeans);
                    checkForGroup(reportTermsBean,allClassReports);
                    //COEUSDEV-561:End
                    reportTermsBean.setSyncRequired(syncRequired);//2796
                    queryEngine.update (queryKey,reportTermsBean);
                    BeanEvent beanEvent = new BeanEvent ();
                    beanEvent.setSource (this);
                    beanEvent.setBean (reportTermsBean);
                    fireBeanAdded (beanEvent);
                } else if(cvReportTerms.size () == 0 && awardReportTermsBean.getRolodexId () == -1  && awardReportTermsBean.getContactTypeCode () == -1
                /*&& awardReportTermsBean.getAcType () == null*/){
                    //Added for COEUSQA-2408 : CLONE -Award Sync - MOdify and sync reports not working correctly  
                    //When modifying the report type to other , the existing report type will get modified instead of inserting a new record
                    if(functionType == TypeConstants.MODIFY_MODE){
                            awardReportTermsBean.setAcType(TypeConstants.UPDATE_RECORD);
                    }else{
                        if(intialTypeCode != awardReportTermsBean.getReportCode()){
                            awardReportTermsBean.setRowId(0);
                        }
                        awardReportTermsBean.setAcType(TypeConstants.INSERT_RECORD);
                    }
                    //COEUSQA-2408 : end
                    //Added for COEUSQA-2412 : Award Sync - Delete and Sync reports - Start
                    awardReportTermsBean.setSyncRequired(syncRequired);
                    //COEUSQA-2412 : End
                    if(awardReportTermsBean.getRowId () == 0){
                        rowId = rowId+1 ;
                        awardReportTermsBean.setRowId (rowId);
                    }
                    queryEngine.insert (queryKey,awardReportTermsBean);
                    BeanEvent beanEvent = new BeanEvent ();
                    beanEvent.setSource (this);
                    beanEvent.setBean (awardReportTermsBean);
                    fireBeanAdded (beanEvent);
                }
                //            if(cvReportTerms.size () == 0 && intialTypeCode != awardReportTermsBean.getReportCode ()){
                //                //awardReportTermsBean.setRowId (0);
                //                awardReportTermsBean.setAcType (TypeConstants.INSERT_RECORD);
                //                rowId = rowId+1 ;
                //                awardReportTermsBean.setRowId (rowId);
                //                queryEngine.insert (queryKey,awardReportTermsBean);
                //                BeanEvent beanEvent = new BeanEvent();
                //                beanEvent.setSource(this);
                //                beanEvent.setBean(awardReportTermsBean);
                //                fireBeanAdded (beanEvent);
                //            }
                
                
                
                
                
                //else if(cvLastRowDeleted.size ()>0){
                //               AwardReportTermsBean reportbean = new AwardReportTermsBean();
                //               if(reportbean.getAcType().equals(TypeConstants.UPDATE_RECORD)){
                //                    reportbean.setAcType(TypeConstants.DELETE_RECORD);
                //                    queryEngine.delete(queryKey, reportTermsBean);
                //                    reportTermsBean.setAcType(TypeConstants.INSERT_RECORD);
                //                    rowId = rowId+1;
                //                    System.out.println("rowID:"+rowId);
                //                    System.out.println("Date:"+reportTermsBean.getDueDate ().getTime());
                //
                //                    reportTermsBean.setRowId(rowId);
                //                    queryEngine.insert(queryKey, reportTermsBean);
                ////                AwardReportTermsBean reportbean = new AwardReportTermsBean();
                ////                awardReportTermsBean.setAcType(TypeConstants.INSERT_RECORD);
                ////                rowId = rowId+1;
                ////                awardReportTermsBean.setRowId(rowId);
                ////                queryEngine.insert(queryKey, awardReportTermsBean);
                ////                BeanEvent beanEvent = new BeanEvent();
                ////                beanEvent.setSource(this);
                ////                beanEvent.setBean(awardReportTermsBean);
                ////                fireBeanAdded (beanEvent);
                //            }
                
                
            }catch (CoeusException coeusException){
                coeusException.printStackTrace ();
            }
            dlgAwardReportTermsForm.dispose ();
        }
        
        public void setFormData (Object data) {
            HashMap htData = new HashMap ();
            cvReportTerms = new CoeusVector ();
            htData = (HashMap)data;
            CoeusVector cvData = new CoeusVector () ;
            cvData = (CoeusVector) htData.get (CoeusVector.class);
            cvAllData = new CoeusVector ();
            if(cvData != null && cvData.size () > 0){
                AwardReportTermsBean reportTermsBean = (AwardReportTermsBean)cvData.get (0);
               
                //Added for COEUSQA-2408 : CLONE -Award Sync - MOdify and sync reports not working correctly - Start
                if(functionType == TypeConstants.MODIFY_MODE){
                    awardReportTermsBeanDB = reportTermsBean;
                    reportTermsBean.setAw_ContactTypeCode(awardReportTermsBeanDB.getAw_ContactTypeCode());
                    reportTermsBean.setAw_DueDate(awardReportTermsBeanDB.getAw_DueDate());
                    reportTermsBean.setAw_FrequencyBaseCode(awardReportTermsBeanDB.getAw_FrequencyBaseCode());
                    reportTermsBean.setAw_FrequencyCode(awardReportTermsBeanDB.getAw_FrequencyCode());
                    reportTermsBean.setAw_OspDistributionCode(awardReportTermsBeanDB.getAw_OspDistributionCode());
                    reportTermsBean.setAw_ReportClassCode(awardReportTermsBeanDB.getAw_ReportClassCode());
                    reportTermsBean.setAw_ReportCode(awardReportTermsBeanDB.getAw_ReportCode());
                    reportTermsBean.setAw_RolodexId(awardReportTermsBeanDB.getAw_RolodexId());
                }
                //COEUSQA-2408 : End
            /*For setting the type to the ComboBox when opened in add mode.
             *The type which is selected in the Reoprt Tab should be set to the combobox
             */
                typeCmoboBean =new ComboBoxBean (""+reportTermsBean.getReportCode (),reportTermsBean.getReportDescription ());
                
                
                cvAllData.addAll (cvData);
                
            } 
            /*ComBoBox got from HashTable for setting the Class to the dialog*/
            ComboBoxBean cmbReportclass  = (ComboBoxBean) htData.get (ComboBoxBean.class);
            reportClass = cmbReportclass.getCode ().trim ();
            
        /*Create an instance of Coeus Vector to get
         *the Sponsor Award Number ,Award Number*/
            cvAwardDetailsBean = new CoeusVector ();
            cvAwardRepTermsBean = new CoeusVector ();
            cvDeletedItem = new CoeusVector ();
            cvClassRelatedData = new CoeusVector ();
            
            try{
                /*For getting Award Details Bean to set Award No, Seq No, Sponsor No*/
                cvAwardDetailsBean = queryEngine.executeQuery (
                queryKey,AwardDetailsBean.class, CoeusVector.FILTER_ACTIVE_BEANS);
                
                /*For getting Award Details Bean to setting data to table*/
                cvAwardRepTermsBean = queryEngine.getDetails (
                queryKey,AwardReportTermsBean.class);
                if(cvAwardRepTermsBean != null && cvAwardRepTermsBean.size () > 0){
                    cvAwardRepTermsBean.sort ("rowId",false);
                    AwardReportTermsBean reportTermsBean =(AwardReportTermsBean) cvAwardRepTermsBean.get (0);
                    rowId = reportTermsBean.getRowId ();
                }
                
                /*For getting the data of all the types belonging to that a particular class*/
                Equals eqReportClassCode = new Equals ("reportClassCode",new Integer (reportClass));
                And eqReportClassCodeAndActiveBeans = new And (eqReportClassCode,CoeusVector.FILTER_ACTIVE_BEANS);
                cvClassRelatedData = queryEngine.executeQuery (
                queryKey,AwardReportTermsBean.class,eqReportClassCodeAndActiveBeans);
                
            }catch (CoeusException coeusException){
                coeusException.printStackTrace ();
            }
            
            
            
            
            if(functionType == TypeConstants.MODIFY_MODE){
                awardReportTermsBean = (AwardReportTermsBean)htData.get (AwardReportTermsBean.class);
                intialTypeCode = awardReportTermsBean.getReportCode ();
                if(cvAllData.size ()>0){
                    performFiltering ();
                }
                try{
                    String strDueDate = DEFAULT_DUE_DATE ;// DD-MMM-YYYY
                    utilDueDate =  dtFormat.parse (dtUtils.restoreDate (strDueDate, DATE_SEPARATERS));
                }catch (ParseException parseException) {
                    parseException.printStackTrace ();
                }
                java.sql.Date sqlDueDate = new java.sql.Date (utilDueDate.getTime ());
                
                
                //            Calendar calendar = Calendar.getInstance (TimeZone.getDefault ());
                //            calendar.set (1900, Calendar.JANUARY, 1 ,0 ,0 ,0);
                //            Date dueDate = new java.sql.Date(calendar.getTime().getTime());
                //            System.out.println("DAte:"+awardReportTermsBean.getDueDate ());
                if(!awardReportTermsBean.getDueDate ().equals (sqlDueDate )){
                    //String strDueDate = dateUtils.formatDate(awardReportTermsBean.getDueDate ().toString ().trim()
                    //                                      , DATE_SEPARATERS, DATE_FORMAT_DISPLAY);
                    
                    String strDate = dateUtils.formatDate (awardReportTermsBean.getDueDate ().toString (),DATE_FORMAT_DISPLAY);
                    
                    awardReportTermsForm.txtDueDate.setText (strDate);
                }
                Equals eqFreqDes = new Equals (FREQUENCY_DISTRIBUTION,awardReportTermsBean.getFrequencyDescription ());
                Equals eqFreqBase = new Equals (FREQUENCY_BASE_DESCRIPTION, awardReportTermsBean.getFrequencyBaseDescription ());
                Equals eqOsp = new Equals (OSP_DISTRIBUTION_DESCRIPTION,awardReportTermsBean.getOspDistributionDescription ());
                Equals eqDate = new Equals (DUE_DATE,awardReportTermsBean.getDueDate ());
                
                //            AwardReportTermsBean coeus = (AwardReportTermsBean)cvData.get (0);
                
                //            System.out.println("frequencyDescription Bean: "+awardReportTermsBean.getFrequencyDescription());
                //            System.out.println("frequencyDescription Vect: "+coeus.getFrequencyDescription());
                //
                //            System.out.println("frequencyBaseDescription Bean:"+ awardReportTermsBean.getFrequencyBaseDescription ());
                //            System.out.println("frequencyBaseDescription Vect:"+ coeus.getFrequencyBaseDescription ());
                //
                //            System.out.println("ospDistributionDescription Bean:"+awardReportTermsBean.getOspDistributionDescription ());
                //            System.out.println("ospDistributionDescription Vect:"+coeus.getOspDistributionDescription ());
                //
                //            System.out.println("Date Bean:"+awardReportTermsBean.getDueDate ());
                //            System.out.println("Date Vect:"+coeus.getDueDate ());
                
                And eqFreqDesAndeqFreqBase = new And (eqFreqDes, eqFreqBase);
                And eqOspAndeqDate = new And (eqOsp, eqDate);
                
                And filteredData = new And (eqFreqDesAndeqFreqBase,eqOspAndeqDate);
                //            CoeusVector cv = new CoeusVector();
                cvData = cvData.filter (filteredData);
                
                if(cvData.size ()>0){
                    for(int index = 0;index<cvData.size ();index++){
                        AwardReportTermsBean reportTermsBean = (AwardReportTermsBean)cvData.get (index);
                        if(reportTermsBean.getRolodexId () != -1 ){//&& //reportTermsBean.getContactTypeCode () != -1){
                            cvReportTerms.add (reportTermsBean);
                        }
                    }
                }
            }
            
            
            
            
            reportClassDescription = cmbReportclass.getDescription ().trim ();
            awardReportTermsForm.lblClassValue.setText (reportClassDescription);
            
            
            populateCombo ();
            
            
            awardDetailsBean = (AwardDetailsBean)cvAwardDetailsBean.get (0);
            awardReportTermsForm.awardHeaderForm.setFormData (awardDetailsBean);
            //        if(functionType == TypeConstants.MODIFY_MODE){
            if(cvReportTerms == null) {
                cvReportTerms = new CoeusVector ();
            }
            //if(functionType == TypeConstants.MODIFY_MODE){
            //  rowId = cvReportTerms.size();
            awardReportTermsTableModel.setData (cvReportTerms);
            //}
            
            //if in modify mode & description, frequency, frequency code is null/Empty
            //set empty beans as selected
            //        if(getFunctionType () == TypeConstants.MODIFY_MODE) {
            //
            //        }
            
        }
        
        
        
        public boolean validate () throws CoeusUIException {
            CoeusVector cvFilteredData = new CoeusVector ();
            Equals eqFreqDes = null;
            Equals eqFreqBase = null;
            Equals eqOsp = null;
            Equals eqTypeDesc = null;
            
            /*Moved Rep Flag*/
            
        /*Validation for the duplicate row
         *This code used when recipients are present
         */
            if(cvReportTerms.size ()>0){
                for(int index = 0; index<cvReportTerms.size (); index++){
                    AwardReportTermsBean reportTermsBean = (AwardReportTermsBean)cvReportTerms.get (index);
                    //Modified with COEUSDEV-561:Award Sync - Error syncing report after removing recipient:Start
                        eqTypeDesc = new Equals ("reportDescription",reportTermsBean.getReportDescription ());
//                    if(reportTermsBean.getFrequencyDescription () == null){
//                        reportTermsBean.setFrequencyDescription (EMPTY) ;
//                        eqFreqDes = new Equals ("frequencyDescription",reportTermsBean.getFrequencyDescription ());
//                    }else{
                        eqFreqDes = new Equals (FREQUENCY_DISTRIBUTION,reportTermsBean.getFrequencyDescription ());
//                    }
//                    if(reportTermsBean.getFrequencyBaseDescription ()  == null){
//                        reportTermsBean.setFrequencyBaseDescription (EMPTY);
//                        eqFreqBase = new Equals ("frequencyBaseDescription", reportTermsBean.getFrequencyBaseDescription ());
//                    }else{
                        eqFreqBase = new Equals (FREQUENCY_BASE_DESCRIPTION, reportTermsBean.getFrequencyBaseDescription ());
//                    }
                    
//                    if(reportTermsBean.getOspDistributionDescription () == null){
//                        reportTermsBean.setOspDistributionDescription (EMPTY);
//                        eqOsp = new Equals ("ospDistributionDescription",reportTermsBean.getOspDistributionDescription ());
//                    }else{
                        eqOsp = new Equals (OSP_DISTRIBUTION_DESCRIPTION,reportTermsBean.getOspDistributionDescription ());
//                    }
                    Equals eqDate = new Equals (DUE_DATE,reportTermsBean.getDueDate ());
                    
                    And eqFreqDesAndeqFreqBase = new And (eqFreqDes, eqFreqBase);
                    And eqOspAndeqDate = new And (eqOsp, eqDate);
                    And filteredData = new And (eqFreqDesAndeqFreqBase,eqOspAndeqDate);
                    And eqTypeDescAndfilteredData = new And (eqTypeDesc,filteredData);
                    /*---------------------------------*/
//                    Equals eqOrganization = new Equals ("organization",reportTermsBean.getOrganization ());
//                    Equals eqType = new Equals ("reportDescription",reportTermsBean.getReportDescription ());
//                    
//                    And eqOrgaAndeqType = new And (eqOrganization,eqType);
//                    
//                    Equals eqFirstname = new Equals ("firstName",reportTermsBean.getFirstName ());
//                    Equals eqLastName = new Equals ("lastName",reportTermsBean.getLastName ());
//                    Equals eqPrefix = new Equals ("prefix",reportTermsBean.getPrefix ());
//                    
//                    if(reportTermsBean.getMiddleName () != null && EMPTY.equals (reportTermsBean.getMiddleName ().trim ())){
//                        reportTermsBean.setMiddleName (null) ;
//                    }
//                    
//                    Equals eqMiddleName = new Equals ("middleName",reportTermsBean.getMiddleName ());
//                    
//                    And eqFirstnameAndeqLastName = new And (eqFirstname,eqLastName);
//                    And eqPrefixAndeqMiddleName = new And (eqMiddleName,eqPrefix);
//                    
//                    And eqintermediateData = new And (eqFirstnameAndeqLastName,eqPrefixAndeqMiddleName);
//                    
//                    And eqData = new And (eqOrgaAndeqType,eqintermediateData);
                    
                    Equals eqContactTypeCode = new Equals ("contactTypeCode",reportTermsBean.getContactTypeCode ());
                    Equals eqRolodexId = new Equals ("rolodexId",reportTermsBean.getRolodexId ());
                    And eqData = new And (eqContactTypeCode,eqRolodexId);
                    
                    And eqfinalData = new And (eqTypeDescAndfilteredData,eqData);
                    //COEUSDEV-561:End
                    cvFilteredData = cvClassRelatedData.filter (eqfinalData);
                    
                    if(cvFilteredData.size ()>0){
                        if(functionType == TypeConstants.ADD_MODE){
                            CoeusOptionPane.showInfoDialog (
                            coeusMessageResources.parseMessageKey (CANNOT_ADD_ROW));
                            return false;
                        }else if(functionType == TypeConstants.MODIFY_MODE){
                            CoeusOptionPane.showInfoDialog (
                            coeusMessageResources.parseMessageKey (DUPLICATE_ROW));
                            return false;
                        }
                    }
                }
            }
            
            /*This is used when there are no recipients*/
            else{
                if(cvClassRelatedData!=null){
                    /* Added from top*/
                    CoeusVector cvCheckForEmpty = new CoeusVector ();
                    
                    eqTypeDesc = new Equals ("reportDescription",awardReportTermsBean.getReportDescription ());
                /*If atleast one group is present for that type
                 *then Frequency, FrequencyBase, OSP Distribution, DueDate
                 *cant be Empty
                 */
                    cvCheckForEmpty = cvClassRelatedData.filter (eqTypeDesc);
                    if(cvCheckForEmpty.size ()>0) {
                        String strFreq = awardReportTermsForm.cmbFrequency.getSelectedItem ().toString ().trim ();
                        String strFreqBase = awardReportTermsForm.cmbFrequencyBase.getSelectedItem ().toString ().trim ();
                        String strOsp = awardReportTermsForm.cmbOSPDistribution.getSelectedItem ().toString ().trim ();
                        String strDueDate = awardReportTermsForm.txtDueDate.getText ().trim ();
                        if(EMPTY.equals (strFreq) && EMPTY.equals (strFreqBase) &&
                        EMPTY.equals (strOsp) && EMPTY.equals (strDueDate)){
                            if(functionType == TypeConstants.ADD_MODE){
                                CoeusOptionPane.showInfoDialog (
                                coeusMessageResources.parseMessageKey (CANNOT_ADD_ROW));
                                return false;
                            }else if(functionType == TypeConstants.MODIFY_MODE){
                                CoeusOptionPane.showInfoDialog (
                                coeusMessageResources.parseMessageKey (DUPLICATE_ROW));
                                return false;
                            }
                        }
                    }
                    
                    
                    
                    //Commented with COEUSDEV-561:Award Sync - Error syncing report after removing recipient:Start
//                    if(awardReportTermsBean.getFrequencyDescription () == null){
//                        awardReportTermsBean.setFrequencyDescription (EMPTY) ;
//                        eqFreqDes = new Equals ("frequencyDescription",awardReportTermsBean.getFrequencyDescription ());
//                    }else{
                        eqFreqDes = new Equals (FREQUENCY_DISTRIBUTION,awardReportTermsBean.getFrequencyDescription ());
//                    }
//                    if(awardReportTermsBean.getFrequencyBaseDescription ()  == null){
//                        awardReportTermsBean.setFrequencyBaseDescription (EMPTY);
//                        eqFreqBase = new Equals ("frequencyBaseDescription", awardReportTermsBean.getFrequencyBaseDescription ());
//                    }else{
                        eqFreqBase = new Equals (FREQUENCY_BASE_DESCRIPTION, awardReportTermsBean.getFrequencyBaseDescription ());
//                    }
                    
//                    if(awardReportTermsBean.getOspDistributionDescription () == null){
//                        awardReportTermsBean.setOspDistributionDescription (EMPTY);
//                        eqOsp = new Equals ("ospDistributionDescription",awardReportTermsBean.getOspDistributionDescription ());
//                    }else{
                        eqOsp = new Equals (OSP_DISTRIBUTION_DESCRIPTION,awardReportTermsBean.getOspDistributionDescription ());
//                    }
                    //COEUSDEV-561:End
                    Equals eqDate = new Equals (DUE_DATE,awardReportTermsBean.getDueDate ());
                    
                    And eqFreqDesAndeqFreqBase = new And (eqFreqDes, eqFreqBase);
                    And eqOspAndeqDate = new And (eqOsp, eqDate);
                    And filteredData = new And (eqFreqDesAndeqFreqBase,eqOspAndeqDate);
                    And eqTypeDescAndfilteredData = new And (eqTypeDesc,filteredData);
                    
                    /*------------------------------------------------------------------*/
                    
                    //                AwardReportTermsBean repBean = (AwardReportTermsBean)cvAllData.get (0);
                    
                    //                System.out.println("freq des:"+repBean .getFrequencyDescription ());
                    //                System.out.println("freq bse:"+repBean .getFrequencyBaseDescription ());
                    //                System.out.println("osp desc:"+repBean .getOspDistributionDescription ());
                    //                System.out.println("Due date:"+repBean .getDueDate ().getTime ());
                    //                System.out.println("Due Date Local :" + awardReportTermsBean.getDueDate ().getTime ());
                    
                    cvFilteredData = cvClassRelatedData.filter (eqTypeDescAndfilteredData);
                    if(cvFilteredData.size ()>0){
                        if(functionType == TypeConstants.ADD_MODE){
                            CoeusOptionPane.showInfoDialog (
                            coeusMessageResources.parseMessageKey (CANNOT_ADD_ROW));
                            return false;
                        }else if(functionType == TypeConstants.MODIFY_MODE){
                            CoeusOptionPane.showInfoDialog (
                            coeusMessageResources.parseMessageKey (DUPLICATE_ROW));
                            return false;
                        }
                    }
                }
            }//awardReportTermsBean.getFrequencyCode ()!= -1
            ComboBoxBean comboBean = (ComboBoxBean)awardReportTermsForm.cmbFrequency.getSelectedItem ();
            
            if(!EMPTY.equals (awardReportTermsForm.cmbFrequencyBase.getSelectedItem ().toString ().trim ()) ||
            !EMPTY.equals (awardReportTermsForm.cmbOSPDistribution.getSelectedItem ().toString ().trim ())||
            !EMPTY.equals (awardReportTermsForm.txtDueDate.getText ().trim ())){
                
                ComboBoxBean combo = (ComboBoxBean)awardReportTermsForm.cmbOSPDistribution.getSelectedItem ();
                if(!combo.getCode ().equals ("-1")){
                    if(EMPTY.equals (awardReportTermsForm.cmbFrequency.getSelectedItem ().toString ().trim ())){
                        awardReportTermsForm.cmbFrequency.requestFocusInWindow ();
                        CoeusOptionPane.showInfoDialog (coeusMessageResources.parseMessageKey (
                        FREQUENCY_VALIDATE));
                        return false;
                    }
                    //continue ;
                }
            }
            
            if(!EMPTY.equals (awardReportTermsForm.cmbFrequency.getSelectedItem ().toString ().trim ()) && getReportFlag ()){
                if(EMPTY.equals (awardReportTermsForm.cmbOSPDistribution.getSelectedItem ().toString ().trim ())){
                    awardReportTermsForm.cmbOSPDistribution.requestFocusInWindow ();
                    CoeusOptionPane.showInfoDialog (coeusMessageResources.parseMessageKey (
                    OSP_VALIDATE));
                    return false;
                }
            }
            
            if(cvReportTerms.size ()>0){
                if(EMPTY.equals (awardReportTermsForm.cmbFrequency.getSelectedItem ().toString ().trim ())){
                    awardReportTermsForm.cmbFrequency.requestFocusInWindow ();
                    CoeusOptionPane.showInfoDialog (coeusMessageResources.parseMessageKey (
                    FREQUENCY_VALIDATE));
                    return false;
                }
            }
            return true;
        }
        
        public void actionPerformed (ActionEvent actionEvent) {
            Object source = actionEvent.getSource ();
            
            if(source.equals (awardReportTermsForm.btnOK)){
                try{
                    
                    //Bug Fix:1482 Start
                    awardReportTermsEditor.stopCellEditing();
                    //Bug Fix:1482 End 
                    
                    performUpdate ();
                    
                    if(functionType == TypeConstants.ADD_MODE){
                        if(validate()){
                            //Case 2796:Sync To Parent
                            if (awardReportTermsForm.chkSync.isSelected()){
                                //COEUSDEV 253: Add FabE and CS accounts to Sync Screen
                                //Modified for COEUSDEV-416 : Award Sync to Children - Display proper error message when not syncing because the award is not saved
//                                HashMap target = showSyncTargetWindow(true,AwardConstants.ADD_SYNC);
                                HashMap target = showSyncTargetWindow(true,AwardConstants.REPORTS_SYNC,AwardConstants.ADD_SYNC);
                                //COEUSDEV-416 : End 
                                //COEUSDEV 253: End
                                if(target!=null){
                                    saveFormData();
                                    if(setSyncFlags(AwardReportTermsBean.class,true,target)){
                                        saveAndSyncAward(AwardReportTermsBean.class);
                                    }
                                }
                            }else{
                                saveFormData();
                            }
                            //2796 End
                            dlgAwardReportTermsForm.dispose();
                        }
                    }else if (modified){
                        if(validate ()){
                            //Case 2796:Sync To Parent
                            if (awardReportTermsForm.chkSync.isSelected()){
                                //COEUSDEV 253: Add FabE and CS accounts to Sync Screen
                                //Modified for COEUSDEV-416 : Award Sync to Children - Display proper error message when not syncing because the award is not saved
                                HashMap target = showSyncTargetWindow(true,AwardConstants.REPORTS_SYNC,AwardConstants.MODIFY_SYNC);
                                //COEUSDEV-416 : End 
                                //COEUSDEV 253: End
                                if(target!=null){
                                    saveFormData();
                                    if(setSyncFlags(AwardReportTermsBean.class,true,target)){
                                        saveAndSyncAward(AwardReportTermsBean.class);
                                    }
                                }
                            }else{
                                saveFormData();
                            }
                            //Case 2796:Sync To Parent
                            dlgAwardReportTermsForm.dispose ();
                        }
                    }else{
                        dlgAwardReportTermsForm.dispose ();
                    }
                    
                    
                    //                if(validate() && modified){
                    //                    saveFormData();
                    //                }else {
                    //                    dlgAwardReportTermsForm.dispose ();
                    //                }
                }catch (CoeusUIException coeusUIException){
                    coeusUIException.printStackTrace ();
                }
            }
            
            if(source.equals (awardReportTermsForm.btnCancel)){
                performCancelAction ();
            }
            if(source.equals (awardReportTermsForm.btnAdd)){
                performAddAction ();
            }
            if(source.equals (awardReportTermsForm.btnDelete)){
                performDeleteAction ();
            }
        }
        
        //    private void performDeleteAction() {
        //        int selectedRow = awardReportTermsForm.tblRecipients.getSelectedRow();
        //        if(selectedRow == -1){
        //            return;
        //        }
        //
        //        if(selectedRow != -1 && selectedRow >= 0){
        //            String mesg = DELETE_CONFIRMATION;
        //            int selectedOption = CoeusOptionPane.showQuestionDialog(
        //            coeusMessageResources.parseMessageKey(mesg),
        //            CoeusOptionPane.OPTION_YES_NO,
        //            CoeusOptionPane.DEFAULT_YES);
        //            if(selectedOption == CoeusOptionPane.SELECTION_YES) {
        //                AwardReportTermsBean deletedCostSharingBean = (AwardReportTermsBean)cvAwardDetailsBean.get(selectedRow);
        //                if (deletedCostSharingBean.getAcType() == null || deletedCostSharingBean.getAcType().equals(TypeConstants.UPDATE_RECORD)) {
        //                    cvDeletedItem.add(deletedCostSharingBean);
        //                }else{
        //                     try{
        //                        deletedCostSharingBean.setAcType(TypeConstants.DELETE_RECORD);
        //                        queryEngine.delete(queryKey, deletedCostSharingBean);
        //                    }catch (CoeusException coeusException){
        //                        coeusException.printStackTrace();
        //                    }
        //                }
        //                if(cvApprvdEqpBean!=null && cvApprvdEqpBean.size() > 0){
        //                    cvApprvdEqpBean.remove(selectedRow);
        //                    apprEquipmentTableModel.fireTableRowsDeleted(selectedRow, selectedRow);
        //                    modified = true;
        //                    deletedCostSharingBean.setAcType(TypeConstants.DELETE_RECORD);
        //
        //                }
        //
        //                if(selectedRow >0){
        //                    awardReportTermsForm.tblRecipients.setRowSelectionInterval(
        //                    selectedRow-1,selectedRow-1);
        //                    awardReportTermsForm.tblRecipients.scrollRectToVisible(
        //                    awardReportTermsForm.tblRecipients.getCellRect(
        //                    selectedRow -1 ,0, true));
        //                }else{
        //                    if(awardReportTermsForm.tblRecipients.getRowCount()>0){
        //                        awardReportTermsForm.tblRecipients.setRowSelectionInterval(0,0);
        //                    }
        //                }
        //            }
        //        }
        //    }
        //
        public Hashtable getDataFromServer () throws CoeusUIException{
            Hashtable htReportTerms = null;
            Integer reportClassCode = new Integer (reportClass);
            RequesterBean requesterBean = new RequesterBean ();
            ResponderBean responderBean = new ResponderBean ();
            requesterBean.setFunctionType (GET_VALID_REPORT_FREQUENCY);
            requesterBean.setDataObject (reportClassCode);
            
            AppletServletCommunicator comm = new AppletServletCommunicator (connect, requesterBean);
            comm.setRequest (requesterBean);
            comm.send ();
            responderBean = comm.getResponse ();
            
            if(responderBean.isSuccessfulResponse ()) {
                htReportTerms  = (Hashtable)responderBean.getDataObject ();
            }else{
                throw new CoeusUIException (responderBean.getMessage (), CoeusUIException.ERROR_MESSAGE);
            }
            return htReportTerms ;
        }
        
        public void populateCombo (){
            cvReportTypeCode = new CoeusVector ();
            cvReportClassFrequency = new CoeusVector ();
            cvFrequencyBase  = new CoeusVector ();
            Hashtable htDataFromServer= new Hashtable ();
            
            try{
                htDataFromServer = getDataFromServer ();
            }catch (CoeusUIException coeusUIException){
                CoeusOptionPane.showErrorDialog(coeusUIException.getMessage());
                coeusUIException.printStackTrace ();
            }
            cvReportTypeCode = (CoeusVector)htDataFromServer.get (ReportBean.class);
            cvReportClassFrequency = (CoeusVector)htDataFromServer.get (ValidReportClassReportFrequencyBean.class);
            cvFrequencyBase  = (CoeusVector)htDataFromServer.get (FrequencyBaseBean.class);
            cvOspDistribution = (CoeusVector)htDataFromServer.get (KeyConstants.DISTRIBUTION);
            
            setDataToOspCombo ();
            
            if(cvReportTypeCode != null && functionType == TypeConstants.ADD_MODE ){
                awardReportTermsForm.cmbType.setModel (new DefaultComboBoxModel (cvReportTypeCode));
                
                /* to display the code in the combo along with the description Bug Fix 1321 - start*/
                awardReportTermsForm.cmbType.setShowCode(true);
                /*Bug Fix 1321 - end*/
                
                awardReportTermsForm.cmbType.setSelectedItem (typeCmoboBean);
                setDataToFrequencyCombo ();
            }else if(cvReportTypeCode != null && functionType == TypeConstants.MODIFY_MODE){
                awardReportTermsForm.cmbType.setModel (new DefaultComboBoxModel (cvReportTypeCode));
                
                /* to display the code in the combo along with the description Bug Fix 1321 - start*/
                awardReportTermsForm.cmbType.setShowCode(true);
                /*Bug Fix 1321 - end*/
                
                //            System.out.println("Rep Desp:"+awardReportTermsBean.getReportDescription ());
                ComboBoxBean comboBean = new ComboBoxBean (""+awardReportTermsBean.getReportCode (),awardReportTermsBean.getReportDescription ());
                awardReportTermsForm.cmbType.setSelectedItem (comboBean);
                setDataToFrequencyCombo ();
            }
        }
        
        public void itemStateChanged (ItemEvent itemEvent) {
            //if dialog is not visible then return
            //        if(dlgAwardReportTermsForm!= null && !dlgAwardReportTermsForm.isVisible()){
            //                return ;
            //        }
            if(itemEvent.getStateChange () == itemEvent.DESELECTED){
                return ;
            }
            Object source = itemEvent.getSource ();
            
            if(source.equals (awardReportTermsForm.cmbType)){
                
                //reportBean = (ReportBean)awardReportTermsForm.cmbType.getSelectedItem ();
                setDataToFrequencyCombo ();
                if(dlgAwardReportTermsForm.isVisible ()) {
                    modified = true;
                }
            }else if(source.equals (awardReportTermsForm.cmbFrequency)){
                setDataToFrequencyBaseCombo ();
                if(dlgAwardReportTermsForm.isVisible ()) {
                    modified = true;
                }
            }else if(source.equals (awardReportTermsForm.cmbFrequencyBase)){
                CoeusVector cvEmpty = null;
                ComboBoxBean emptyBean = new ComboBoxBean (EMPTY, EMPTY);
                if(EMPTY.equals (awardReportTermsForm.cmbFrequency.getSelectedItem ().toString ().trim ())){
                    cvEmpty.add (0,emptyBean);
                    awardReportTermsForm.cmbFrequencyBase.setModel (new DefaultComboBoxModel (cvEmpty));
                    if(dlgAwardReportTermsForm.isVisible ()) {
                        modified = true;
                    }                //return ;
                } 
                //Added for Bug fix #1807 Strat
                else {
                    if(dlgAwardReportTermsForm.isVisible ()) 
                        modified = true;
                }//end
            }else if(source.equals (awardReportTermsForm.cmbOSPDistribution)){
                if(dlgAwardReportTermsForm.isVisible ()) {
                    modified = true;
                }
            }
        }
        
        
        private void setTableEditors (){
            try{
                JTableHeader tableHeader = awardReportTermsForm.tblRecipients.getTableHeader ();
                tableHeader.setReorderingAllowed (false);
                tableHeader.setFont (CoeusFontFactory.getLabelFont ());
                tableHeader.addMouseListener (new ColumnHeaderListener ());
                awardReportTermsForm.tblRecipients.setAutoResizeMode (javax.swing.JTable.AUTO_RESIZE_OFF);
                awardReportTermsForm.tblRecipients.setRowHeight (22);
                awardReportTermsForm.tblRecipients.setShowHorizontalLines (true);
                awardReportTermsForm.tblRecipients.setShowVerticalLines (true);
                awardReportTermsForm.tblRecipients.setOpaque (false);
                awardReportTermsForm.tblRecipients.setSelectionMode (
                DefaultListSelectionModel.SINGLE_SELECTION);
                TableColumn column;
                //int minWidth[] = {30, 45, 90, 90, 90, 90, 90, 90};
                int prefWidth[] = {205, 205, 108};
                for(int index = 0; index < prefWidth.length; index++) {
                    column = awardReportTermsForm.tblRecipients.getColumnModel ().getColumn (index);
                    column.setPreferredWidth (prefWidth[index]);
                    column.setCellEditor (awardReportTermsEditor);
                    column.setCellRenderer (awardReportTermsRenderer);
                }
            }catch(Exception e){
                e.printStackTrace ();
            }
        }
        
        public class AwardReportTermsTableModel extends AbstractTableModel {
            CoeusVector cvTempReportsData = new CoeusVector ();
            private String colName[] = {"Contact","Name/Organization","Copies"};
            private Class colClass[] = {String.class, String.class, String.class};
            
            
            public int getColumnCount (){
                return colName.length;
            }
            
            public Class getColumnClass (int colIndex){
                return colClass[colIndex];
            }
            public int getRowCount (){
                if(cvTempReportsData.size () < 1){
                    return 0;
                }else{
                    return cvTempReportsData.size ();
                }
            }
            
            public void setData (CoeusVector cvReportTerms){
                cvTempReportsData = cvReportTerms;
                //            if( cvReportTerms != null ){
                //                cvTempReportsData.addAll (cvReportTerms);
                //            }
                //}
                
            }
            public String getColumnName (int column){
                return colName[column];
            }
            
            public Object getValueAt (int row, int col) {
                if(cvTempReportsData.size ()<1){
                    return null ;
                }
                AwardReportTermsBean reportBean= (AwardReportTermsBean)cvTempReportsData.get (row);
                switch(col){
                    case CONTACT_INDEX:
                        return reportBean.getContactTypeDescription ();
                    case NAME_INDEX:
                        String rolodexName;
                        String firstName = (reportBean.getFirstName () == null ?EMPTY:reportBean.getFirstName ());
                        if ( firstName.length () > 0) {
                            String suffix = (reportBean.getSuffix () == null ?EMPTY:reportBean.getSuffix ());
                            String prefix = (reportBean.getPrefix () == null ?EMPTY:reportBean.getPrefix ());
                            String middleName = (reportBean.getMiddleName () == null ?EMPTY:reportBean.getMiddleName ());
                            rolodexName = (reportBean.getLastName () + " "+
                            suffix+", "+ prefix+" "+ firstName+" "+
                            middleName).trim ();
                            return rolodexName;
                        } else {
                            if(reportBean.getOrganization () == null){
                                rolodexName = EMPTY;
                                return rolodexName ;
                            }else{
                                return reportBean.getOrganization ();
                            }
                        }
                        
                    case COPIES_INDEX:
                        if(reportBean.getNumberOfCopies () == 0){
                            return null;
                        }else{
                            Integer noOfCopies  = new Integer (reportBean.getNumberOfCopies ());
                            return noOfCopies;
                        }
                }
                return EMPTY;
            }
            
            /**
             * Returns true if the cell at <code>rowIndex</code> and
             * <code>columnIndex</code>
             * is editable.  Otherwise, <code>setValueAt</code> on the cell will not
             * change the value of that cell.
             *
             * @param	rowIndex	the row whose value to be queried
             * @param	columnIndex	the column whose value to be queried
             * @return	true if the cell is editable
             * @see #setValueAt
             */
            public boolean isCellEditable (int rowIndex, int columnIndex) {
                if(columnIndex == 2){
                    return true;
                }else{
                    return false;
                }
            }
            
            /**
             * Sets the value in the cell at <code>columnIndex</code> and
             * <code>rowIndex</code> to <code>aValue</code>.
             *
             * @param	aValue		 the new value
             * @param	rowIndex	 the row whose value is to be changed
             * @param	columnIndex 	 the column whose value is to be changed
             * @see #getValueAt
             * @see #isCellEditable
             */
            public void setValueAt (Object aValue, int rowIndex, int columnIndex) {
                AwardReportTermsBean reportBean = (AwardReportTermsBean)cvTempReportsData.get (rowIndex);
                boolean changed = false;
                int value = 0;
                switch(columnIndex){
                    case COPIES_INDEX:
                        // Case# 3189:Can't see the report # copy changes for templates - Start
//                        int value = Integer.parseInt (aValue.toString ());
                        if( aValue != null && !(aValue.equals(""))){
                            value = Integer.parseInt(aValue.toString());
                        }
                        // Case# 3189:Can't see the report # copy changes for templates - End
                        if (value != reportBean.getNumberOfCopies ()) {
                            reportBean.setNumberOfCopies (value);
                            changed = modified = true;
                        }
                        break;
                    default :
                }
                
                if(changed){
                    if(reportBean.getAcType () == null) {
                        reportBean.setAcType (TypeConstants.UPDATE_RECORD);
                    }
                }
            }//End setValueAt
        }
        
        public class AwardReportTermsRenderer extends DefaultTableCellRenderer
        implements TableCellRenderer {
            //private JTextField txtComponent;
            public AwardReportTermsRenderer (){
                //txtComponent = new JTextField();
                //txtComponent.setBorder(new javax.swing.border.EmptyBorder(0,0,0,0));
                setOpaque (true);
            }
            public java.awt.Component getTableCellRendererComponent (javax.swing.JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int col){
                switch(col){
                    case CONTACT_INDEX:
                    case NAME_INDEX:
                    case COPIES_INDEX:
                        if(value == null || value.toString ().trim ().equals (EMPTY)){
                            setText (EMPTY);
                        }else{
                            setText (value.toString ());
                        }
                        
                        if(isSelected){
                            setBackground (Color.YELLOW);
                            setForeground (Color.BLACK);
                        }else{
                            setBackground (Color.WHITE);
                            setForeground (Color.BLACK);
                        }
                        return this;
                }
                return this;
            }
        }
        
        class AwardReportTermsEditor extends AbstractCellEditor implements TableCellEditor{
            private JTextField txtComponent;
            private int column;
            
            public AwardReportTermsEditor () {
                txtComponent = new JTextField ();
            }
            
            public Object getCellEditorValue () {
                switch(column){
                    case CONTACT_INDEX:
                    case NAME_INDEX:
                    case COPIES_INDEX:
                        return txtComponent.getText ();
                }
                return txtComponent;
            }
            
            public Component getTableCellEditorComponent (JTable table, Object value, boolean isSelected, int row, int column) {
                this.column = column;
                switch(column){
                    case COPIES_INDEX:
                        txtComponent.setDocument (new JTextFieldFilter (JTextFieldFilter.NUMERIC,2));
                        if(value == null){
                            txtComponent.setText (EMPTY);
                        }else{
                            txtComponent.setText (value.toString ());
                        }
                        return txtComponent;
                        
                        //                case VENDOR:
                        //                case MODEL:
                        //                    txtComponent.setDocument(new JTextFieldFilter(JTextFieldFilter.ALPHA_NUMERIC,50));
                        //                    if(value == null){
                        //                        txtComponent.setText(EMPTY_STRING);
                        //                    }else{
                        //                        txtComponent.setText(value.toString());
                        //                    }
                        //                    return txtComponent;
                        ////                    txtComponent.setText(value.toString());
                        ////                    return txtComponent;
                        
                        
                }
                return txtComponent;
            }
        }
        
        
        public class CustomFocusAdapter extends FocusAdapter{
            String dueDate;
            public void focusGained (FocusEvent focusEvent) {
                if(focusEvent.isTemporary ()){
                    return ;
                }
                Object source = focusEvent.getSource ();
                if(source.equals (awardReportTermsForm.txtDueDate)){
                    dueDate = awardReportTermsForm.txtDueDate.getText ();
                    dueDate = dateUtils.restoreDate (dueDate, DATE_SEPARATERS);
                    awardReportTermsForm.txtDueDate.setText (dueDate);
                    //System.out.println("Due Date(Focus Gained):"+dueDate);
                }
            }
            
            /** listsns to focus lost event.
             * @param focusEvent focusEvent
             */
            public void focusLost (FocusEvent focusEvent) {
                if(focusEvent.isTemporary ()) return ;
                
                Object source = focusEvent.getSource ();
                if(source.equals (awardReportTermsForm.txtDueDate)){
                    String date;
                    date = awardReportTermsForm.txtDueDate.getText ();
                    
                    //                System.out.println("Due Date(Focus Lost):"+dueDate);
                    //                System.out.println("Date(Focus Lost):"+date);
                    
                    if(!dueDate.equals (date)){
                        modified = true;
                        //                    System.out.println("Inside equals:"+modified);
                    }
                    
                    if(date.equals (EMPTY)) {
                        return ;
                    }
                    
                    date = dateUtils.formatDate (date, DATE_SEPARATERS, DATE_FORMAT_DISPLAY);
                    if(date == null) {
                        CoeusOptionPane.showInfoDialog (coeusMessageResources.parseMessageKey(ERRKEY_INVALID_DUE_DATE));
                        //        setRequestFocusInThread(awardReportTermsForm.txtDueDate);
                        //awardReportTermsForm.txtDueDate.requestFocusInWindow();
                    }else {
                        awardReportTermsForm.txtDueDate.setText (date);
                    }
                }
            }
        }
        
        public void setDataToFrequencyCombo (){
            CoeusVector tmpReportClassFrequency = new CoeusVector ();
            CoeusVector cvFrequencydata = new CoeusVector ();
            ComboBoxBean frequencyBean = null;
            ComboBoxBean comboBoxBean = null;
            ComboBoxBean emptyBean = new ComboBoxBean (EMPTY,EMPTY);
            
            ValidReportClassReportFrequencyBean validReportFrequencyBean = null;
            
            comboBoxBean = (ComboBoxBean)awardReportTermsForm.cmbType.getSelectedItem ();
            
            String reportTypeCode = comboBoxBean.getCode ();
            if(!reportTypeCode.equals (EMPTY)){
                Equals eqFrequency = new Equals ("reportCode", new Integer (reportTypeCode));
                tmpReportClassFrequency = cvReportClassFrequency.filter (eqFrequency);
            }
            
            for(int row = 0; row < tmpReportClassFrequency.size (); row++){
                validReportFrequencyBean  =
                (ValidReportClassReportFrequencyBean)tmpReportClassFrequency.elementAt (row);
                frequencyBean = new ComboBoxBean (""+validReportFrequencyBean.getFrequencyCode (), validReportFrequencyBean.getFrequencyDescription ());
                cvFrequencydata.addElement (frequencyBean);
            }
            
            cvFrequencydata.add (0,emptyBean);
            
            awardReportTermsForm.cmbFrequency.setModel (new DefaultComboBoxModel (cvFrequencydata));
            
            /* to display the code in the combo along with the description Bug Fix 1321 - start*/
            awardReportTermsForm.cmbFrequency.setShowCode(true);
            /*Bug Fix:1321 end*/
            
            if(functionType == TypeConstants.MODIFY_MODE){
                if(awardReportTermsBean.getFrequencyCode () == -1){
                    awardReportTermsForm.cmbFrequency.setSelectedItem (emptyBean);
                }else if(!dlgAwardReportTermsForm.isVisible () ){
                    comboBoxBean = new ComboBoxBean (""+awardReportTermsBean.getFrequencyCode (),awardReportTermsBean.getFrequencyDescription ());
                    awardReportTermsForm.cmbFrequency.setSelectedItem (comboBoxBean);
                    ItemEvent itemEvent = new ItemEvent (awardReportTermsForm.cmbFrequency, 0, null, ItemEvent.SELECTED);
                    itemStateChanged (itemEvent);
                }
            }
            ItemEvent itemEvent = new ItemEvent (awardReportTermsForm.cmbFrequency, 0, null, ItemEvent.SELECTED);
            itemStateChanged (itemEvent);
        }
        
        
        public void setDataToFrequencyBaseCombo (){
            CoeusVector cvtmpFrequencyBaseCode = new CoeusVector ();
            
            ComboBoxBean comboBean = new ComboBoxBean ();
            ComboBoxBean emptyBean = new ComboBoxBean (EMPTY, EMPTY);
         
            
            comboBean = (ComboBoxBean)awardReportTermsForm.cmbFrequency.getSelectedItem ();
            
        /*If data is present in frequency base combobox and del is pressed for
         *frequency base combobox,then should not clear the frequency base combobox
         */
            
            //        if(EMPTY.equals (comboBean.getCode()) && delPressed){
            //            return ;
            //        }
            
            if(EMPTY.equals (comboBean.getCode ())){
                awardReportTermsForm.cmbFrequencyBase.setModel (new DefaultComboBoxModel (cvtmpFrequencyBaseCode));
                return ;
            }
            
            
            String frequencyCode = comboBean.getCode ();
            Equals eqFrequencyBase = new Equals ("frequencyCode",new Integer (frequencyCode));
            cvtmpFrequencyBaseCode = cvFrequencyBase.filter (eqFrequencyBase);
            //if(functionType == TypeConstants.ADD_MODE){
            cvtmpFrequencyBaseCode.add (0, emptyBean);
            //  }
            
            awardReportTermsForm.cmbFrequencyBase.setModel (new DefaultComboBoxModel (cvtmpFrequencyBaseCode));
            
            /* to display the code in the combo along with the description Bug Fix 1321 - start*/
            awardReportTermsForm.cmbFrequencyBase.setShowCode (true);
            /*Bug Fix:1321 end*/
            
            if(functionType == TypeConstants.MODIFY_MODE && !delPressed){
                if(!dlgAwardReportTermsForm.isVisible () ){
                    comboBean = new ComboBoxBean (""+awardReportTermsBean.getFrequencyBaseCode (),awardReportTermsBean.getFrequencyBaseDescription ());
                    //System.out.println("Freq Base Desp:"+ awardReportTermsBean.getFrequencyDescription ());
                    
                    awardReportTermsForm.cmbFrequencyBase.setSelectedItem (comboBean);
                    
                }
            }
            
        }
        
        public void setDataToOspCombo (){
            
            ComboBoxBean emptyBean = new ComboBoxBean (EMPTY, EMPTY);
            cvOspDistribution.add (0, emptyBean);
            
            awardReportTermsForm.cmbOSPDistribution.setModel (new DefaultComboBoxModel (cvOspDistribution));
            
            /* to display the code in the combo along with the description Bug Fix 1321 - start*/
            awardReportTermsForm.cmbOSPDistribution.setShowCode(true);
            /*Bug Fix:1321 end*/
            
            if(functionType == TypeConstants.MODIFY_MODE){
                String strDesc = awardReportTermsBean.getOspDistributionDescription ();
                int code = awardReportTermsBean.getOspDistributionCode ();
                ComboBoxBean comboBean = new ComboBoxBean (""+code,strDesc);
                awardReportTermsForm.cmbOSPDistribution.setSelectedItem (comboBean);
                if(dlgAwardReportTermsForm.isVisible ()){
                    modified = true;
                }
            }
        }
        
        public void performAddAction (){
            CoeusVector cvData = new CoeusVector ();
            try{
                dlgAwardReportTermsForm.setCursor (new Cursor (Cursor.WAIT_CURSOR));
                AddRecipientsHeaderController addRecipientsHeaderController= new AddRecipientsHeaderController (awardBaseBean,functionType);
                addRecipientsHeaderController.setLabelData (reportClassDescription , awardReportTermsForm.cmbType.getSelectedItem ().toString ().trim ());
                addRecipientsHeaderController.display ();
                cvData = addRecipientsHeaderController.getSelectedPersons ();
                
                //Bug Fix:Performance Issue (Out of memory) Start 1
                addRecipientsHeaderController.cleanUp();
                //Bug Fix:Performance Issue (Out of memory) End 1
                
            }
            finally{
                dlgAwardReportTermsForm.setCursor (new Cursor (Cursor.DEFAULT_CURSOR));
            }
            //if( addRecipientsHeaderController.dlgAddRecipientsHeaderForm.isVisible () ){
            if( cvData == null || cvData.size () == 0 ){
                return ;
            }else {
                setDataToBean (cvData);
                setSaveRequired (true);
                modified = true;
                awardReportTermsForm.tblRecipients.setRowSelectionInterval (0, 0);
                //       }
            }
        }
        private void performDeleteAction () {
            cvLastRowDeleted = new CoeusVector ();
            awardReportTermsEditor.stopCellEditing ();
            int selectedRow = awardReportTermsForm.tblRecipients.getSelectedRow ();
            if(selectedRow == -1){
                CoeusOptionPane.showErrorDialog (coeusMessageResources.parseMessageKey (
                (ROW_TO_DELETE)));
                return ;
            }
            
            
            if(selectedRow != -1 && selectedRow >= 0){
                String mesg = DELETE_CONFIRMATION;
                int selectedOption = CoeusOptionPane.showQuestionDialog (
                coeusMessageResources.parseMessageKey (mesg),
                CoeusOptionPane.OPTION_YES_NO,
                CoeusOptionPane.DEFAULT_YES);
                if(selectedOption == CoeusOptionPane.SELECTION_YES) {
                    
                    AwardReportTermsBean deletedReportTerms = (AwardReportTermsBean)cvReportTerms.get (selectedRow);
                    
                    if(cvReportTerms!=null && cvReportTerms.size () > 0){
                        cvReportTerms.removeElementAt (selectedRow);
                        awardReportTermsTableModel.fireTableRowsDeleted (selectedRow, selectedRow);
                        modified = true;
                    }
                    
                    if(cvReportTerms == null || cvReportTerms.size () == 0) {
                        deletedReportTerms.setRolodexId (-1);
                        deletedReportTerms.setContactTypeCode (-1);
                        deletedReportTerms.setAcType (TypeConstants.UPDATE_RECORD);
                        cvLastRowDeleted.add (deletedReportTerms);
                        setSaveRequired (true);
                        modified = true;
                        return ;
                    }
                    
                    // if (deletedReportTerms.getAcType() == null ||
                    // deletedReportTerms.getAcType().equals(TypeConstants.UPDATE_RECORD)) {
                    /*deletedReportTerms.setAcType(TypeConstants.DELETE_RECORD);*/
                    cvDeletedItem.add (deletedReportTerms);
                    //}
                    //else{
                    //                     try{
                    //                        if(cvReportTerms == null || cvReportTerms.size () == 0) {
                    //                            deletedReportTerms.setRolodexId (-1);
                    //                            deletedReportTerms.setContactTypeCode (-1);
                    //                            deletedReportTerms.setAcType(TypeConstants.UPDATE_RECORD);
                    //                            cvLastRowDeleted.add(deletedReportTerms);
                    //                            queryEngine.update (queryKey, deletedReportTerms);
                    //   cvLastRowDeleted.add(deletedReportTerms);
                    //                            BeanEvent beanEvent = new BeanEvent();
                    //                            beanEvent.setSource(this);
                    //                            beanEvent.setBean(new AwardReportTermsBean());
                    //                            fireBeanUpdated (beanEvent);
                    //                        }//else{
                    //                            deletedReportTerms.setAcType (TypeConstants.DELETE_RECORD);
                    //                            queryEngine.delete (queryKey,deletedReportTerms);
                    //                        }
                    //                    }catch (CoeusException coeusException){
                    //                        coeusException.printStackTrace();
                    //                    }
                    //                }
                    //                if(cvReportTerms!=null && cvReportTerms.size() > 0){
                    //                    cvReportTerms.remove(selectedRow);
                    //                    awardReportTermsTableModel.fireTableRowsDeleted(selectedRow, selectedRow);
                    //                    modified = true;
                    //
                    //
                    //                }
                    
                    if(selectedRow >0){
                        awardReportTermsForm.tblRecipients.setRowSelectionInterval (
                        selectedRow-1,selectedRow-1);
                        awardReportTermsForm.tblRecipients.scrollRectToVisible (
                        awardReportTermsForm.tblRecipients.getCellRect (
                        selectedRow -1 ,0, true));
                    }else{
                        if(awardReportTermsForm.tblRecipients.getRowCount ()>0){
                            awardReportTermsForm.tblRecipients.setRowSelectionInterval (0,0);
                        }
                    }
                    setSaveRequired (true);
                    modified = true;
                }
            }
        }
        
        private void performCancelAction (){
            if(isSaveRequired () || modified){
                int option = CoeusOptionPane.showQuestionDialog (
                coeusMessageResources.parseMessageKey (SAVE_CHANGES),
                CoeusOptionPane.OPTION_YES_NO_CANCEL,
                JOptionPane.YES_OPTION);
                switch( option ) {
                    case (JOptionPane.YES_OPTION ):
                        setSaveRequired (true);
                        try{
                            performUpdate ();
                            if( validate () ){
                                saveFormData ();
                            }
                        }catch (Exception exception){
                            exception.printStackTrace ();
                        }
                        break;
                    case(JOptionPane.NO_OPTION ):
                        dlgAwardReportTermsForm.dispose ();
                        break;
                    default:
                        break;
                }
            }else{
                dlgAwardReportTermsForm.dispose ();
            }
        }
        
        
        public void setDataToBean (CoeusVector cvData){
            CoeusVector cvSelectedPersons = new CoeusVector ();
            
            String rolodexName;
            
            for(int i = 0; i<cvData.size (); i++){
                boolean duplicate = false;
                AwardContactDetailsBean awardContactDetailsBean = new AwardContactDetailsBean ();
                AwardReportTermsBean reportTermsBean = new AwardReportTermsBean ();
                awardContactDetailsBean = (AwardContactDetailsBean)cvData.elementAt (i) ;
                
                reportTermsBean.setContactTypeDescription (
                awardContactDetailsBean.getContactTypeDescription ()== null ?EMPTY:awardContactDetailsBean.getContactTypeDescription ());
                if(awardContactDetailsBean.getContactTypeCode () == 0){
                    reportTermsBean.setContactTypeCode (Integer.parseInt ("-1"));
                }else{
                    reportTermsBean.setContactTypeCode (awardContactDetailsBean.getContactTypeCode ());
                }
                
                reportTermsBean.setRolodexId (awardContactDetailsBean.getRolodexId ());
                
                String firstName = (awardContactDetailsBean.getFirstName () == null ?EMPTY:awardContactDetailsBean.getFirstName ());
                if ( firstName.length () > 0) {
                    String suffix = (awardContactDetailsBean.getSuffix () == null ?EMPTY:awardContactDetailsBean.getSuffix ());
                    String prefix = (awardContactDetailsBean.getPrefix () == null ?EMPTY:awardContactDetailsBean.getPrefix ());
                    String middleName = (awardContactDetailsBean.getMiddleName () == null ?EMPTY:awardContactDetailsBean.getMiddleName ());
                    rolodexName = (awardContactDetailsBean.getLastName () + " "+
                    suffix+", "+ prefix+" "+ firstName+" "+
                    middleName).trim ();
                    //reportTermsBean.setOrganization (rolodexName);
                    reportTermsBean.setFirstName (firstName);
                    reportTermsBean.setSuffix (suffix);
                    reportTermsBean.setPrefix (prefix);
                    reportTermsBean.setMiddleName (middleName);
                    reportTermsBean.setLastName (awardContactDetailsBean.getLastName ().trim ());
                    
                }// else {
                if(awardContactDetailsBean.getOrganization () == null){
                    rolodexName = EMPTY;
                }else{
                    rolodexName = awardContactDetailsBean.getOrganization ().trim ();
                }
                reportTermsBean.setOrganization (rolodexName);
                //  }
                //   rowId = rowId+1;
                
                reportTermsBean.setNumberOfCopies (noOfCopies);
                
                //Added for COEUSQA-2408 : CLONE -Award Sync - MOdify and sync reports not working correctly - Start
                if(functionType == TypeConstants.MODIFY_MODE){
                    reportTermsBean.setAw_ContactTypeCode(awardReportTermsBeanDB.getAw_ContactTypeCode());
                    reportTermsBean.setAw_DueDate(awardReportTermsBeanDB.getAw_DueDate());
                    reportTermsBean.setAw_FrequencyBaseCode(awardReportTermsBeanDB.getAw_FrequencyBaseCode());
                    reportTermsBean.setAw_FrequencyCode(awardReportTermsBeanDB.getAw_FrequencyCode());
                    reportTermsBean.setAw_OspDistributionCode(awardReportTermsBeanDB.getAw_OspDistributionCode());
                    reportTermsBean.setAw_ReportClassCode(awardReportTermsBeanDB.getAw_ReportClassCode());
                    reportTermsBean.setAw_ReportCode(awardReportTermsBeanDB.getAw_ReportCode());
                    reportTermsBean.setAw_RolodexId(awardReportTermsBeanDB.getAw_RolodexId());
                }
                //COEUSQA-2408 : End
                
                reportTermsBean.setAcType (TypeConstants.INSERT_RECORD);
                //   reportTermsBean.setRowId(rowId);
                
                duplicate = checkDuplicateRow (reportTermsBean);
                if(duplicate){
                    CoeusOptionPane.showInfoDialog (
                    coeusMessageResources.parseMessageKey (DUPILCATE_CONTACT));
                    continue ;
                }else{
                    cvSelectedPersons.add (reportTermsBean);
                }
            }
            //    cvReportTerms.addAll (cvSelectedPersons);
        /*
        cvAddedPersons.addAll (cvSelectedPersons);
        awardReportTermsTableModel.setData (cvAddedPersons);
        awardReportTermsTableModel.fireTableDataChanged ();
         */
            cvReportTerms.addAll (cvSelectedPersons);
            awardReportTermsTableModel.fireTableDataChanged ();
            //awardReportTermsTableModel.setData(cvSelectedPersons);
        }
        
        private boolean checkDuplicateRow (AwardReportTermsBean awardReportTermsBean ){
            
            AwardReportTermsBean reportTermsBean = new AwardReportTermsBean ();
            reportTermsBean = awardReportTermsBean;
            String rolodexName = null;
            //        CoeusVector coeusVector = new CoeusVector();
            
            if(cvReportTerms!=null && cvReportTerms.size () > 0){
                for(int index = 0; index < cvReportTerms.size (); index++){
                    AwardReportTermsBean reportBean =
                    (AwardReportTermsBean)cvReportTerms.get (index);
                    
                    
                    String firstName = (reportBean.getFirstName () == null ?EMPTY:reportBean .getFirstName ());
                    if ( firstName.length () > 0) {
                        String suffix = (reportBean .getSuffix () == null ?EMPTY:reportBean .getSuffix ());
                        String prefix = (reportBean .getPrefix () == null ?EMPTY:reportBean .getPrefix ());
                        String middleName = (reportBean .getMiddleName () == null ?EMPTY:reportBean .getMiddleName ());
                        rolodexName = (reportBean .getLastName () + " "+
                        suffix+", "+ prefix+" "+ firstName+" "+
                        middleName).trim ();
                    } //else// {
                    //                   if( reportBean.getOrganization() == null){
                    //                       rolodexName = EMPTY;
                    //                   }else{
                    //                       rolodexName = reportBean.getOrganization().trim();
                    //                   }
                    //               }
                    /*for cmparing */
                    String cmpRolodexName = null;
                    String cmpFirstName = (reportTermsBean.getFirstName () == null ?EMPTY:reportTermsBean .getFirstName ());
                    if ( firstName.length () > 0) {
                        String cmpSuffix = (reportTermsBean .getSuffix () == null ?EMPTY:reportTermsBean .getSuffix ());
                        String cmpPrefix = (reportTermsBean .getPrefix () == null ?EMPTY:reportTermsBean .getPrefix ());
                        String cmpMiddleName = (reportTermsBean .getMiddleName () == null ?EMPTY:reportTermsBean .getMiddleName ());
                        cmpRolodexName = (reportTermsBean .getLastName () + " "+
                        cmpSuffix+", "+ cmpPrefix+" "+ cmpFirstName+" "+
                        cmpMiddleName).trim ();
                    } //else {
                    //                   if( reportTermsBean.getOrganization() == null){
                    //                       cmpRolodexName = EMPTY;
                    //                   }else{
                    //                       cmpRolodexName = reportTermsBean.getOrganization().trim();
                    //                   }
                    //               }
                    
                    
                    int contactCode = reportBean.getContactTypeCode ();
                    
                    
                    
                    if(rolodexName != null && rolodexName.equals (cmpRolodexName) ){
                        //if(contactCode > 0){
                            if(contactCode == reportTermsBean.getContactTypeCode ()){
                                return true;
                            }
                       // }
                        //return false;
                    }
                    //Bug Fix:1395 Start
                    //Commented for bug fix 1395
                    //else if(reportBean.getOrganization ().equals (reportTermsBean.getOrganization ())){
                    else if(reportTermsBean.getRolodexId() != -1 || reportTermsBean.getRolodexId() != 0){
                        if(reportBean.getRolodexId() == reportTermsBean.getRolodexId()){
                            return true;
                        }
                    }//Bug Fix:1395 End
                }
            }
            return false;
        }
        public void performUpdate (){
            
            ComboBoxBean comboBoxBean = new ComboBoxBean ();
            if(functionType == TypeConstants.ADD_MODE){
                CoeusVector cvDataToFilter = new CoeusVector ();
                Equals eqFrequency = null;
                Equals eqFrequencyBase = null;
                Equals eqOsp = null;
                Equals eqDate = null;
                ComboBoxBean comboBean = new ComboBoxBean ();
            /*Check weather for the same group if Recipients exists
             *the base window
             */
                Equals eqClass = new Equals ("reportClassCode",new Integer (reportClass));
                
                comboBean = (ComboBoxBean)awardReportTermsForm.cmbType.getSelectedItem ();
                Equals eqType = new Equals ("reportCode",new Integer (comboBean.getCode ()));
                
                if(!EMPTY.equals (awardReportTermsForm.cmbFrequency.getSelectedItem ().toString ().trim ())){
                    comboBean = (ComboBoxBean)awardReportTermsForm.cmbFrequency.getSelectedItem ();
                    eqFrequency = new Equals ("frequencyCode",new Integer (comboBean.getCode ()));
                }else{
                    eqFrequency = new Equals ("frequencyCode",new Integer ("-1"));
                }
                
                if(!EMPTY.equals (awardReportTermsForm.cmbFrequencyBase.getSelectedItem ().toString ().trim ())){
                    comboBean = (ComboBoxBean)awardReportTermsForm.cmbFrequencyBase.getSelectedItem ();
                    eqFrequencyBase = new Equals ("frequencyBaseCode",new Integer (comboBean.getCode ()));
                }else{
                    eqFrequencyBase = new Equals ("frequencyBaseCode",new Integer ("-1"));
                }
                
                if(!EMPTY.equals (awardReportTermsForm.cmbOSPDistribution.getSelectedItem ().toString ().trim ())){
                    comboBean = (ComboBoxBean)awardReportTermsForm.cmbOSPDistribution.getSelectedItem ();
                    eqOsp = new Equals ("ospDistributionCode",new Integer (comboBean.getCode ()));
                }else {
                    eqOsp = new Equals ("ospDistributionCode",new Integer ("-1"));
                }
                java.sql.Date sqlDate = null;
                String strDueDate = awardReportTermsForm.txtDueDate.getText ().toString ().trim ();
                if(strDueDate.equals (EMPTY)){
                    try{
                        strDueDate = DEFAULT_DUE_DATE ;// DD-MMM-YYYY
                        utilDueDate =  dtFormat.parse (dtUtils.restoreDate (strDueDate, DATE_SEPARATERS));
                    }catch (ParseException parseException) {
                        parseException.printStackTrace ();
                    }
                    sqlDate = new java.sql.Date (utilDueDate.getTime ());
                }else{
                    strDueDate =dtUtils.restoreDate (strDueDate,DATE_SEPARATERS);
                    try{
                        Date dueDate = dtFormat.parse (strDueDate);
                        sqlDate = new java.sql.Date (dueDate.getTime ());
                    }catch (ParseException parseException){
                        parseException.printStackTrace ();
                    }
                }
                eqDate = new Equals (DUE_DATE,sqlDate);
                
                And eqClassAndeqType = new And (eqClass,eqType);
                And eqFrequencyAndeqFrequencyBase = new And (eqFrequency,eqFrequencyBase);
                And eqOspAndeqDate = new And (eqOsp,eqDate);
                
                And classAndfreq = new And (eqClassAndeqType,eqFrequencyAndeqFrequencyBase);
                And filterData = new And (eqOspAndeqDate,classAndfreq);
                //if(filterred)
                try{
                    cvDataToFilter = queryEngine.executeQuery (queryKey,AwardReportTermsBean.class,filterData);
                }catch (CoeusException coeusException){
                    coeusException.printStackTrace ();
                }
                if(cvDataToFilter.size ()>0){
                    for(int index = 0; index < cvDataToFilter.size ();index++){
                        AwardReportTermsBean reportTermsBean = (AwardReportTermsBean) cvDataToFilter.get (index);
                        if(reportTermsBean.getContactTypeCode () == -1 && reportTermsBean.getRolodexId () == -1){
                            reportTermsBean.setAcType (TypeConstants.DELETE_RECORD);
                            cvDeletedItem.add (reportTermsBean);
                        }
                    }
                }
            }//End of type And
            
            /*For setting data to the instance variable bean if there are no Name/Organiztion*/
            
            if(cvReportTerms.size ()<=0){
                awardReportTermsBean.setMitAwardNumber (awardDetailsBean.getMitAwardNumber ());
                
                //Bug Fix:1340 Start 1
                //awardReportTermsBean.setSequenceNumber (awardDetailsBean.getSequenceNumber ());
                //Bug Fix:1340 End 1
                
                awardReportTermsBean.setFinalReport (getReportFlag ());
                
                awardReportTermsBean.setReportClassCode (Integer.parseInt (reportClass));
                
                //System.out.println("S:"+awardReportTermsForm.cmbType.getSelectedItem().toString ().trim ());
                if(EMPTY.equals (awardReportTermsForm.cmbType.getSelectedItem ().toString ().trim ())){
                    awardReportTermsBean.setReportCode (Integer.parseInt ("-1"));
                }else{
                    comboBoxBean = (ComboBoxBean)awardReportTermsForm.cmbType.getSelectedItem ();
                    awardReportTermsBean.setReportCode (Integer.parseInt (comboBoxBean.getCode ()));
                    awardReportTermsBean.setReportDescription (comboBoxBean.getDescription ());
                }
                if(EMPTY.equals (awardReportTermsForm.cmbFrequency.getSelectedItem ().toString ().trim ())){
                    awardReportTermsBean.setFrequencyCode (-1);
                    awardReportTermsBean.setFrequencyDescription (EMPTY);
                    awardReportTermsForm.cmbFrequency.setSelectedItem (new String ("-1"));
                }else{
                    comboBoxBean = (ComboBoxBean)awardReportTermsForm.cmbFrequency.getSelectedItem ();
                    awardReportTermsBean.setFrequencyCode (Integer.parseInt (comboBoxBean.getCode ()));
                    awardReportTermsBean.setFrequencyDescription (comboBoxBean.getDescription ().trim ());
                }
                if(EMPTY.equals (awardReportTermsForm.cmbFrequencyBase.getSelectedItem ().toString ().trim ())){
                    awardReportTermsBean.setFrequencyBaseCode (-1);
                    awardReportTermsBean.setFrequencyBaseDescription (EMPTY);
                    awardReportTermsForm.cmbFrequencyBase.setSelectedItem (new String ("-1"));
                }else{
                    comboBoxBean = (ComboBoxBean)awardReportTermsForm.cmbFrequencyBase.getSelectedItem ();
                    awardReportTermsBean.setFrequencyBaseCode (Integer.parseInt (comboBoxBean.getCode ()));
                    //                System.out.println("Desc "+comboBoxBean.getDescription ().trim ());
                    awardReportTermsBean.setFrequencyBaseDescription (comboBoxBean.getDescription ().trim ());
                }
                
                if(EMPTY.equals (awardReportTermsForm.cmbOSPDistribution.getSelectedItem ().toString ().trim ())){
                    awardReportTermsBean.setOspDistributionCode (-1);
                    awardReportTermsBean.setOspDistributionDescription (EMPTY);
                    awardReportTermsForm.cmbOSPDistribution.setSelectedItem (new String ("-1"));
                }else{
                    comboBoxBean = (ComboBoxBean)awardReportTermsForm.cmbOSPDistribution.getSelectedItem ();
                    awardReportTermsBean.setOspDistributionCode (Integer.parseInt (comboBoxBean.getCode ()));
                    awardReportTermsBean.setOspDistributionDescription (comboBoxBean.getDescription ().trim ());
                }
                
                
                if(cvReportTerms.size () == 0){
                    awardReportTermsBean.setContactTypeCode (Integer.parseInt ("-1"));
                    awardReportTermsBean.setRolodexId (Integer.parseInt ("-1"));
                }
                
                String strDueDate = awardReportTermsForm.txtDueDate.getText ().toString ().trim ();
                if(strDueDate.equals (EMPTY)){
                    try{
                        strDueDate = DEFAULT_DUE_DATE ;// DD-MMM-YYYY
                        utilDueDate =  dtFormat.parse (dtUtils.restoreDate (strDueDate, DATE_SEPARATERS));
                    }catch (ParseException parseException) {
                        parseException.printStackTrace ();
                    }
                    awardReportTermsBean.setDueDate (new java.sql.Date (utilDueDate.getTime ()));
                    
                    //           Calendar calendar = Calendar.getInstance (TimeZone.getDefault ());
                    //           calendar.set (1900, Calendar.JANUARY, 1 ,0 ,0 ,0);
                    //           awardReportTermsBean.setDueDate(new java.sql.Date(calendar.getTime().getTime()));
                }else{
                    strDueDate =dtUtils.restoreDate (strDueDate,DATE_SEPARATERS);
                    try{
                        Date dueDate = dtFormat.parse (strDueDate);
                        awardReportTermsBean.setDueDate ( new java.sql.Date (dueDate.getTime ()));
                        //reportTermsBean.setDueDate (new java.sql.Date(date.getTime()));
                        //Date dateFrom = dtFormat.parse(awardReportTermsForm.txtDueDate.toString ().trim());
                    }catch (ParseException parseException){
                        parseException.printStackTrace ();
                    }
                }
            }
            boolean flag = false;
            if(functionType == TypeConstants.MODIFY_MODE){
                flag = isGroupModified ();
            }
            /*For setting for all the added beans*/
            for(int index = 0; index<cvReportTerms.size (); index++){
                
                AwardReportTermsBean reportTermsBean = (AwardReportTermsBean) cvReportTerms.get (index);
                if(flag){
                    reportTermsBean.setAcType (TypeConstants.UPDATE_RECORD);
                }
                reportTermsBean.setMitAwardNumber (awardDetailsBean.getMitAwardNumber ());
                
                //Bug Fix:1340 Start 2
                //reportTermsBean.setSequenceNumber (awardDetailsBean.getSequenceNumber ());
                //Bug Fix:1340 End 2
                
                reportTermsBean.setReportClassCode (Integer.parseInt (reportClass));
                reportTermsBean.setFinalReport (getReportFlag ());
                
                comboBoxBean = (ComboBoxBean)awardReportTermsForm.cmbType.getSelectedItem ();
                reportTermsBean.setReportCode (Integer.parseInt (comboBoxBean.getCode ()));
                reportTermsBean.setReportDescription (comboBoxBean.getDescription ());
                
                if(EMPTY.equals (awardReportTermsForm.cmbFrequency.getSelectedItem ().toString ().trim ())){
                    reportTermsBean.setFrequencyCode (-1);
                    reportTermsBean.setFrequencyDescription (EMPTY);
                    awardReportTermsForm.cmbFrequency.setSelectedItem (new String ("-1"));
                }else{
                    comboBoxBean = (ComboBoxBean)awardReportTermsForm.cmbFrequency.getSelectedItem ();
                    reportTermsBean.setFrequencyCode (Integer.parseInt (comboBoxBean.getCode ()));
                    reportTermsBean.setFrequencyDescription (comboBoxBean.getDescription ().trim ());
                }
                if(EMPTY.equals (awardReportTermsForm.cmbFrequencyBase.getSelectedItem ().toString ().trim ())){
                    reportTermsBean.setFrequencyBaseCode (-1);
                    reportTermsBean.setFrequencyBaseDescription (EMPTY);
                    awardReportTermsForm.cmbFrequencyBase.setSelectedItem (new String ("-1"));
                }else{
                    comboBoxBean = (ComboBoxBean)awardReportTermsForm.cmbFrequencyBase.getSelectedItem ();
                    reportTermsBean.setFrequencyBaseCode (Integer.parseInt (comboBoxBean.getCode ()));
                    reportTermsBean.setFrequencyBaseDescription (comboBoxBean.getDescription ().trim ());
                }
                if(EMPTY.equals (awardReportTermsForm.cmbOSPDistribution.getSelectedItem ().toString ().trim ())){
                    reportTermsBean.setOspDistributionCode (-1);
                    reportTermsBean.setOspDistributionDescription (EMPTY);
                    awardReportTermsForm.cmbOSPDistribution.setSelectedItem (new String ("-1"));
                }else{
                    comboBoxBean = (ComboBoxBean)awardReportTermsForm.cmbOSPDistribution.getSelectedItem ();
                    reportTermsBean.setOspDistributionCode (Integer.parseInt (comboBoxBean.getCode ()));
                    reportTermsBean.setOspDistributionDescription (comboBoxBean.getDescription ().trim ());
                }
                
                String strDate = awardReportTermsForm.txtDueDate.getText ().toString ().trim ();
                if(strDate.equals (EMPTY)){
                    try{
                        strDate = DEFAULT_DUE_DATE ;// DD-MMM-YYYY
                        utilDueDate =  dtFormat.parse (dtUtils.restoreDate (strDate, DATE_SEPARATERS));
                    }catch (ParseException parseException) {
                        parseException.printStackTrace ();
                    }
                    //            System.out.println("Due Date: "+strDate );
                    //            System.out.println("Utl Date: "+utilDueDate);
                    reportTermsBean.setDueDate (new java.sql.Date (utilDueDate.getTime ()));
                    //            System.out.println("Time: "+reportTermsBean.getDueDate().getTime ());
                    
                    //               Calendar calendar = Calendar.getInstance (TimeZone.getDefault ());
                    //               calendar.set (1900, Calendar.JANUARY, 1 ,0 ,0 ,0);
                    //                reportTermsBean.setDueDate(new java.sql.Date(calendar.getTime().getTime()));
                }else{
                    strDate =dtUtils.restoreDate (strDate,DATE_SEPARATERS);
                    try{
                        Date dueDate = dtFormat.parse (strDate);
                        reportTermsBean.setDueDate ( new java.sql.Date (dueDate.getTime ()));
                        //reportTermsBean.setDueDate (new java.sql.Date(date.getTime()));
                        //Date dateFrom = dtFormat.parse(awardReportTermsForm.txtDueDate.toString ().trim());
                    }catch (ParseException parseException){
                        parseException.printStackTrace ();
                    }
                }
            }
        }
        
    /*For validating the data ,filter the vector which has all the data
     *After filtering we will get the vector of the groups which does not belong
     *to the current one
     */
        public void performFiltering (){
            NotEquals noEqFreqDes = new NotEquals (FREQUENCY_DISTRIBUTION,awardReportTermsBean.getFrequencyDescription ());
            NotEquals noEqFreqBase = new NotEquals (FREQUENCY_BASE_DESCRIPTION, awardReportTermsBean.getFrequencyBaseDescription ());
            NotEquals noEqOsp = new NotEquals (OSP_DISTRIBUTION_DESCRIPTION,awardReportTermsBean.getOspDistributionDescription ());
            NotEquals noEqDate = new NotEquals (DUE_DATE,awardReportTermsBean.getDueDate ());
            
            Or noEqFreqDesOrnoEqFreqBase= new Or (noEqFreqDes, noEqFreqBase);
            Or noEqOspOrnoEqDate = new Or (noEqOsp, noEqDate);
            Or filteredData = new Or (noEqFreqDesOrnoEqFreqBase,noEqOspOrnoEqDate);
            cvAllData = cvAllData.filter (filteredData);
            cvClassRelatedData = cvClassRelatedData.filter (filteredData);
        }
        
        /** Supporting method which will be used for the focus lost for date
         *fields. This will be fired when the request focus for the specified
         *date field is invoked
         */
//        private void setRequestFocusInThread (final Component component) {
//            SwingUtilities.invokeLater ( new Runnable () {
//                public void run () {
//                    component.requestFocusInWindow ();
//                }
//            });
//        }
        
        public boolean isGroupModified (){
            String type = awardReportTermsBean.getReportDescription ().trim ();
            String cmpType = awardReportTermsForm.cmbType.getSelectedItem ().toString ().trim ();
            
            String freq = awardReportTermsBean.getFrequencyDescription ().trim ();
            String cmpFreq = awardReportTermsForm.cmbFrequency.getSelectedItem ().toString ().trim ();
            
            
            String freqBase = awardReportTermsBean.getFrequencyBaseDescription ().trim ();
            String cmpFreqBase = awardReportTermsForm.cmbFrequencyBase.getSelectedItem ().toString ().trim ();
            
            String osp = awardReportTermsBean.getOspDistributionDescription ().trim ();
            String cmpOsp = awardReportTermsForm.cmbOSPDistribution.getSelectedItem ().toString ().trim ();
            /*Check for date also*/
            java.sql.Date sqlBeanDate = awardReportTermsBean.getDueDate ();
            
            java.sql.Date sqlDate = null;
            String strDueDate = awardReportTermsForm.txtDueDate.getText ().toString ().trim ();
            if(strDueDate.equals (EMPTY)){
                try{
                    strDueDate = DEFAULT_DUE_DATE ;// DD-MMM-YYYY
                    utilDueDate =  dtFormat.parse (dtUtils.restoreDate (strDueDate, DATE_SEPARATERS));
                }catch (ParseException parseException) {
                    parseException.printStackTrace ();
                }
                sqlDate = new java.sql.Date (utilDueDate.getTime ());
            }else{
                strDueDate =dtUtils.restoreDate (strDueDate,DATE_SEPARATERS);
                try{
                    Date dueDate = dtFormat.parse (strDueDate);
                    sqlDate = new java.sql.Date (dueDate.getTime ());
                }catch (ParseException parseException){
                    parseException.printStackTrace ();
                }
            }
            if(type.equals (cmpType)&&freq.equals (cmpFreq)&&freqBase.equals (cmpFreqBase)&&
            osp.equals (cmpOsp)&&sqlBeanDate.equals (sqlDate)){
                return false;
            }else{
                return true;
            }
        }
        
        
        
        
    /**
     * To check if the report term data exists in the vector of all data.
     * If present and if the bean contains no recipients, then
     * delete that bean
     * @param reportTermsBean - The bean to be deleted if exists
     * @param cvClassRelatedData - The all class related data.
     */
        //Method params Modified with COEUSDEV-561:Award Sync - Error syncing report after removing recipient.
        public void checkForGroup (AwardReportTermsBean reportTermsBean, CoeusVector cvClassRelatedData){
            CoeusVector cvData = new CoeusVector ();
            Equals eqFreqDes = new Equals (FREQUENCY_DISTRIBUTION,reportTermsBean.getFrequencyDescription ());
            Equals eqFreqBase = new Equals (FREQUENCY_BASE_DESCRIPTION, reportTermsBean.getFrequencyBaseDescription ());
            Equals eqOsp = new Equals (OSP_DISTRIBUTION_DESCRIPTION,reportTermsBean.getOspDistributionDescription ());
            Equals eqDate = new Equals (DUE_DATE,reportTermsBean.getDueDate ());
            
            And eqFreqDesAndeqFreqBase = new And (eqFreqDes, eqFreqBase);
            And eqOspAndeqDate = new And (eqOsp, eqDate);
            And filteredData = new And (eqFreqDesAndeqFreqBase,eqOspAndeqDate);
            cvData = cvClassRelatedData.filter (filteredData);
            if(cvData.size ()>0){
                reportTermsBean = (AwardReportTermsBean)cvData.get (0);
                if(reportTermsBean.getRolodexId () == -1 && reportTermsBean.getContactTypeCode () == -1){
                    reportTermsBean.setAcType (TypeConstants.DELETE_RECORD);
                    try{
                        queryEngine.delete (queryKey,reportTermsBean);
                    }catch (CoeusException ce){
                        ce.printStackTrace ();
                    }
                }
            }
        }
        
        
        /** Method to clean all objects */
        public void cleanUp () {
            awardReportTermsBean = null;
            awardDetailsBean = null;
            reportBean = null;
            dlgAwardReportTermsForm = null;
            mdiForm = null;
            coeusMessageResources = null;
            awardReportTermsForm = null;
            queryEngine = null;
            cvAwardDetailsBean = null;
            cvReportTerms = null;
            cvAwardRepTermsBean = null;
            cvReportTypeCode = null;
            cvFrequencyBase = null;
            cvReportClassFrequency = null;
            cvOspDistribution = null;
            cvDeletedItem = null;
            cvAllData = null;
            cvLastRowDeleted = null;
            typeCmoboBean = null;
            awardReportTermsTableModel = null;
            awardReportTermsRenderer = null;
            awardReportTermsEditor = null;
            utilDueDate = null;
            dtUtils = null;
            dtFormat = null;
            cvAddedPersons = null;
        }
        
        /** This class will sort the column values in ascending and descending order
         * based on number of clicks.
         */
        public class ColumnHeaderListener extends MouseAdapter {
            
            String nameBeanId [][] ={
                {"0","contactTypeCode" },
                {"1","organization" },
                {"2","numberOfCopies"}
            };
            boolean sort =true;
            /** Mouse click handler for the table headers to sort upon the headers
             * @param evt mouse event
             */
            public void mouseClicked (MouseEvent evt) {
                awardReportTermsEditor.stopCellEditing ();
                try {
                    JTable table = ((JTableHeader)evt.getSource ()).getTable ();
                    TableColumnModel colModel = table.getColumnModel ();
                    // The index of the column whose header was clicked
                    int vColIndex = colModel.getColumnIndexAtX (evt.getX ());
                    if(cvReportTerms != null && cvReportTerms.size ()>0 &&
                    nameBeanId [vColIndex][1].length () >1 ){
                        //sort method cannot be used here. will have to sort these ourselves.
                        ((CoeusVector)cvReportTerms).sort (nameBeanId [vColIndex][1],sort);
                        if (sort) {
                            sort = false;
                        }
                        else {
                            sort = true;
                        }
                        awardReportTermsTableModel.fireTableRowsUpdated (
                        0, awardReportTermsTableModel.getRowCount ());
                    }
                } catch(Exception exception) {
                    exception.getMessage ();
                }
            }
        }// End of ColumnHeaderListener.................
        
        private boolean getReportFlag (){
            ComboBoxBean comboBoxBean = (ComboBoxBean)awardReportTermsForm.cmbType.getSelectedItem ();
            CoeusVector cvRepType = cvReportTypeCode.filter (new Equals ("code", comboBoxBean.getCode ().trim ()));
            reportBean  = (ReportBean)cvRepType.elementAt (0);
            boolean repFlag = reportBean.isFinalReportFlag ();
            return repFlag;
        }

    }//end of class
    
