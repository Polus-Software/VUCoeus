/*
 * AwardCloseoutController.java
 *
 * Created on June 9, 2004, 11:24 AM
 */
/** 
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 * 
 */

package edu.mit.coeus.award.controller;

import edu.mit.coeus.utils.query.*;
import edu.mit.coeus.utils.*;
import edu.mit.coeus.gui.*;
import edu.mit.coeus.award.bean.*;
import edu.mit.coeus.award.gui.AwardCloseOutForm;
import edu.mit.coeus.award.gui.AwardHeaderForm;
import edu.mit.coeus.exception.*;
import edu.mit.coeus.brokers.*;
import edu.mit.coeus.bean.CoeusParameterBean;
import edu.mit.coeus.bean.*;

import java.awt.event.*;
import javax.swing.AbstractAction;
import javax.swing.*;
import java.util.Hashtable;
import java.util.Vector;
import java.util.Date;
import java.util.Calendar;
/* @author chandru
*/
public class AwardCloseoutController extends AwardController implements ActionListener{
    
    private QueryEngine queryEngine;
    private CoeusMessageResources coeusMessageResources;
    private DateUtils dateUtils;
    private java.text.SimpleDateFormat simpleDateFormat;
    private CoeusAppletMDIForm mdiForm = CoeusGuiConstants.getMDIForm();
    private AwardCloseOutForm awardCloseOutForm = null;
    private AwardHeaderForm awardHeaderForm = null;
    private char functionType;
    private CoeusVector cvCloseoutData;
    private static final int WIDTH = 550;
    private static final int HEIGHT = 280;
    private CoeusDlgWindow dlgCloseout;
    private static final String DATE_SEPARATERS = ":/.,|-";
    private static final String REQUIRED_DATEFORMAT = "dd-MMM-yyyy";
    private static final String SIMPLE_DATE_FORMAT = "MM/dd/yyyy";
    private static final String EMPTY_STRING = "";
    private static final int DATE_MAX_LIMIT = 11;
    private AwardCloseOutBean awardCloseOutBean = null;
    private static final char GET_CLOSE_OUT_DATA = 'O';
    private final String CLOSE_OUT_SERVLET ="/AwardMaintenanceServlet";
    private final String connect  = CoeusGuiConstants.CONNECTION_URL + CLOSE_OUT_SERVLET;
    private static final String MULTIPLE = "MULTIPLE";
    private static final String TITLE = "Award Closeout";
    private boolean modified = false;
    private boolean dataChange = false;
    private static final String DATE_VALIDATION = "awardCloseout_exceptionCode.1556";
    private static final String MISSING_FISCAL_CODE = "awardCloseout_exceptionCode.1557";
    private static final String MISSING_TECHNICAL_CODE = "awardCloseout_exceptionCode.1558";
    private static final String MISSING_PATENT_CODE = "awardCloseout_exceptionCode.1559";
    private static final String MISSING_PROPERTY_CODE = "awardCloseout_exceptionCode.1560";
    private Hashtable awardcloseoutData;
    private CoeusVector cvParameterData;
    private CoeusParameterBean coeusParameterBean;
    private static final String PARAMETER_VALUE = "parameterName";
    
    
    public AwardCloseoutController(){
        
    }
    /** Creates a new instance of AwardCloseoutController */
    public AwardCloseoutController(AwardBaseBean awardBaseBean, char functionType) {
        super(awardBaseBean);
        this.functionType = functionType;
        queryEngine = QueryEngine.getInstance();
        coeusMessageResources = CoeusMessageResources.getInstance();
        awardCloseOutForm = new AwardCloseOutForm();
        awardHeaderForm = new AwardHeaderForm();
        simpleDateFormat = new java.text.SimpleDateFormat(SIMPLE_DATE_FORMAT);
        dateUtils = new DateUtils();
        registerComponents();
        setFormData(awardBaseBean);
        postInitComponents();
        formatFields();
        display();
        
    }
    /** this method  will initialize the dialog box and sets required 
     *properticies of the dialog box.
     */ 
     private void postInitComponents(){
         
         dlgCloseout = new CoeusDlgWindow(mdiForm);
         dlgCloseout.setResizable(false);
         dlgCloseout.setModal(true);
         dlgCloseout.getContentPane().add(awardCloseOutForm);
         dlgCloseout.setFont(CoeusFontFactory.getLabelFont());
         dlgCloseout.setTitle(TITLE);
         dlgCloseout.setDefaultCloseOperation(CoeusDlgWindow.DO_NOTHING_ON_CLOSE);
         dlgCloseout.setSize(WIDTH, HEIGHT);
         java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
         java.awt.Dimension dlgSize = dlgCloseout.getSize();
         dlgCloseout.setLocation(screenSize.width/2 - (dlgSize.width/2),
         screenSize.height/2 - (dlgSize.height/2));
         dlgCloseout.addEscapeKeyListener(
         new AbstractAction("escPressed"){
             public void actionPerformed(ActionEvent ae){
                 performCancelAction();
                 return;
             }
         });
         dlgCloseout.setDefaultCloseOperation(CoeusDlgWindow.DO_NOTHING_ON_CLOSE);
         dlgCloseout.addWindowListener(new WindowAdapter(){
             public void windowClosing(WindowEvent we){
                 performCancelAction();
                 
             }
         });
         
         dlgCloseout.addComponentListener(
         new ComponentAdapter(){
             public void componentShown(ComponentEvent e){
                 requestDefaultFocus();
             }
         });
     }
     
     /** sets the default focus when the window is opened */     
     private void requestDefaultFocus(){
         if(functionType!= DISPLAY_MODE){
            awardCloseOutForm.txtArchiveLocation.requestFocusInWindow();
         }else{
             awardCloseOutForm.btnCancel.requestFocusInWindow();
         }
     }
     
     /** Handle the Action when clicked on Cancel button */     
     private void performCancelAction(){
          if(!isDataChanged()){
            confirmClosing();
        }else{
            dlgCloseout.setVisible(false);
        }
     }
     
     /** Confirm before closing the BudgetPersons dialog box */    
    private void confirmClosing(){
        try{
            int option = CoeusOptionPane.showQuestionDialog(
            coeusMessageResources.parseMessageKey("saveConfirmCode.1002"),
            CoeusOptionPane.OPTION_YES_NO_CANCEL,CoeusOptionPane.DEFAULT_CANCEL);
            if(option == CoeusOptionPane.SELECTION_YES){
                 if(validate()){
                        saveFormData();
                    }
            }else if(option == CoeusOptionPane.SELECTION_NO){
                setSaveRequired(false);
                dlgCloseout.setVisible(false);
            }else if(option==CoeusOptionPane.SELECTION_CANCEL){
                return;
            }
        }catch(Exception exception){
            exception.printStackTrace();
            CoeusOptionPane.showErrorDialog(exception.getMessage());
            
        }
    }
     
    /** Check whether any data is changed usicng StrictEquals. This method will
     * be usefull while closing the window.Prompt the save confirmation whenever
     * a change is done to the form values
     * @return
     */    
     private boolean isDataChanged(){
        StrictEquals stCloseout = new StrictEquals();
        AwardCloseOutBean qryBean = new AwardCloseOutBean();
        Hashtable htData = new Hashtable();
        AwardReportTermsBean beans = null;
        htData = getCloseOutData();
        CoeusVector cvTempData = new CoeusVector();
        try{
            if(awardCloseOutBean== null){
                awardCloseOutBean = new AwardCloseOutBean();
                awardCloseOutBean.setMitAwardNumber(awardBaseBean.getMitAwardNumber());
            }

            setSaveFormData();
            CoeusVector cvTemp = queryEngine.executeQuery(queryKey,AwardCloseOutBean.class, CoeusVector.FILTER_ACTIVE_BEANS);
            if(cvTemp!= null && cvTemp.size() > 0){
                qryBean = (AwardCloseOutBean)cvTemp.get(0);
            }else{
                cvTempData = (CoeusVector)htData.get(AwardCloseOutBean.class);
                if(cvTempData != null && cvTempData.size()>0){
                    qryBean  = (AwardCloseOutBean)cvTempData.elementAt(0);
                }
            }
            if(qryBean!= null){
                if(qryBean.getInvoiceDueDate()!= null){
                    if(qryBean.getInvoiceDueDate().trim().equals(EMPTY_STRING)){
                        qryBean.setInvoiceDueDate(null);
                    }
                }
                if(qryBean.getPropertyDueDate()!= null){
                    if(qryBean.getPropertyDueDate().trim().equals(EMPTY_STRING)){
                        qryBean.setPropertyDueDate(null);
                    }
                }
                if(qryBean.getTechnicalDueDate()!= null){
                    if(qryBean.getTechnicalDueDate().trim().equals(EMPTY_STRING)){
                        qryBean.setTechnicalDueDate(null);
                    }
                }
            }
            //awardCloseOutBean.setMitAwardNumber(awardBaseBean.getMitAwardNumber());
            dataChange = stCloseout.compare(awardCloseOutBean, qryBean);
        }catch (CoeusException coeusException){
            coeusException.printStackTrace();
        }
        return dataChange;
     }
     
     
     /** setting data to the awardCloseOutBean. Sets the form data to the awardCloseOutBean */     
     private void setSaveFormData(){
         String strDate;
         java.util.Date date;
         try{
             
             // Setting the invoice Date
             strDate = awardCloseOutForm.txtInvoiceSubDate.getText().trim();
             if(! strDate.equals(EMPTY_STRING)) {
                 String strDate1 =  dateUtils.formatDate(strDate, DATE_SEPARATERS, REQUIRED_DATEFORMAT);
                 if(strDate1 == null){
                     strDate1 =dateUtils.restoreDate(strDate, DATE_SEPARATERS);
                     if(strDate1== null || strDate1.equals(strDate)){
                         awardCloseOutBean.setAcType(TypeConstants.INSERT_RECORD);
                         awardCloseOutBean.setFinalInvSubmissionDate(null);
                     }else{
                         date = simpleDateFormat.parse(dateUtils.restoreDate(strDate,DATE_SEPARATERS));
                         awardCloseOutBean.setFinalInvSubmissionDate(new java.sql.Date(date.getTime()));
                     }
                 }else{
                     date = simpleDateFormat.parse(dateUtils.formatDate(strDate,DATE_SEPARATERS, SIMPLE_DATE_FORMAT));
                     awardCloseOutBean.setFinalInvSubmissionDate(new java.sql.Date(date.getTime()));
                 }
             }else{
                    awardCloseOutBean.setFinalInvSubmissionDate(null);
             }
             
             
             // Setting the date for the Technical
             
             strDate = awardCloseOutForm.txtTechnicalSubDate.getText().trim();
             if(! strDate.equals(EMPTY_STRING)) {
                 String strDate1 =  dateUtils.formatDate(strDate, DATE_SEPARATERS, REQUIRED_DATEFORMAT);
                 if(strDate1 == null){
                     strDate1 =dateUtils.restoreDate(strDate, DATE_SEPARATERS);
                     if(strDate1== null || strDate1.equals(strDate)){
                         awardCloseOutBean.setAcType(TypeConstants.INSERT_RECORD);
                         awardCloseOutBean.setFinalTechSubmissionDate(null);
                     }else{
                         date = simpleDateFormat.parse(dateUtils.restoreDate(strDate,DATE_SEPARATERS));
                         awardCloseOutBean.setFinalTechSubmissionDate(new java.sql.Date(date.getTime()));
                     }
                 }else{
                     date = simpleDateFormat.parse(dateUtils.formatDate(strDate,DATE_SEPARATERS, SIMPLE_DATE_FORMAT));
                     awardCloseOutBean.setFinalTechSubmissionDate(new java.sql.Date(date.getTime()));
                 }
             }else{
                    awardCloseOutBean.setFinalTechSubmissionDate(null);
             }
             
             // setting the date for patent
             
             strDate = awardCloseOutForm.txtPatentSubDate.getText().trim();
             if(! strDate.equals(EMPTY_STRING)) {
                 String strDate1 =  dateUtils.formatDate(strDate, DATE_SEPARATERS, REQUIRED_DATEFORMAT);
                 if(strDate1 == null){
                     strDate1 =dateUtils.restoreDate(strDate, DATE_SEPARATERS);
                     if(strDate1== null || strDate1.equals(strDate)){
                         awardCloseOutBean.setAcType(TypeConstants.INSERT_RECORD);
                         awardCloseOutBean.setFinalPatentSubmissionDate(null);
                     }else{
                         date = simpleDateFormat.parse(dateUtils.restoreDate(strDate,DATE_SEPARATERS));
                         awardCloseOutBean.setFinalPatentSubmissionDate(new java.sql.Date(date.getTime()));
                     }
                 }else{
                     date = simpleDateFormat.parse(dateUtils.formatDate(strDate,DATE_SEPARATERS, SIMPLE_DATE_FORMAT));
                     awardCloseOutBean.setFinalPatentSubmissionDate(new java.sql.Date(date.getTime()));
                 }
             }else{
                    awardCloseOutBean.setFinalPatentSubmissionDate(null);
             }
             
             // Setting the property date
             
             strDate = awardCloseOutForm.txtPropertSubDate.getText().trim();
             if(! strDate.equals(EMPTY_STRING)) {
                 String strDate1 =  dateUtils.formatDate(strDate, DATE_SEPARATERS, REQUIRED_DATEFORMAT);
                 if(strDate1 == null){
                     strDate1 =dateUtils.restoreDate(strDate, DATE_SEPARATERS);
                     if(strDate1== null || strDate1.equals(strDate)){
                         awardCloseOutBean.setAcType(TypeConstants.INSERT_RECORD);
                         awardCloseOutBean.setFinalPropSubmissionDate(null);
                     }else{
                         date = simpleDateFormat.parse(dateUtils.restoreDate(strDate,DATE_SEPARATERS));
                         awardCloseOutBean.setFinalPropSubmissionDate(new java.sql.Date(date.getTime()));
                     }
                 }else{
                     date = simpleDateFormat.parse(dateUtils.formatDate(strDate,DATE_SEPARATERS, SIMPLE_DATE_FORMAT));
                     awardCloseOutBean.setFinalPropSubmissionDate(new java.sql.Date(date.getTime()));
                 }
             }else{
                    awardCloseOutBean.setFinalPropSubmissionDate(null);
             }
             
             
             //setting the closeout date.
             
             strDate = awardCloseOutForm.txtCloseoutDate.getText().trim();
             if(! strDate.equals(EMPTY_STRING)) {
                 String strDate1 =  dateUtils.formatDate(strDate, DATE_SEPARATERS, REQUIRED_DATEFORMAT);
                 if(strDate1 == null){
                     strDate1 =dateUtils.restoreDate(strDate, DATE_SEPARATERS);
                     if(strDate1== null || strDate1.equals(strDate)){
                         awardCloseOutBean.setAcType(TypeConstants.INSERT_RECORD);
                         awardCloseOutBean.setCloseOutDate(null);
                     }else{
                         date = simpleDateFormat.parse(dateUtils.restoreDate(strDate,DATE_SEPARATERS));
                         awardCloseOutBean.setCloseOutDate(new java.sql.Date(date.getTime()));
                     }
                 }else{
                     date = simpleDateFormat.parse(dateUtils.formatDate(strDate,DATE_SEPARATERS, SIMPLE_DATE_FORMAT));
                     awardCloseOutBean.setCloseOutDate(new java.sql.Date(date.getTime()));
                 }
             }else{
                    awardCloseOutBean.setCloseOutDate(null);
             }
             if(awardCloseOutBean.getArchiveLocation()== null && awardCloseOutForm.txtArchiveLocation.getText().trim().equals(EMPTY_STRING)){
                 awardCloseOutBean.setArchiveLocation(null);
             }else{
                awardCloseOutBean.setArchiveLocation(awardCloseOutForm.txtArchiveLocation.getText().trim());
             }
            // }
         }catch (java.text.ParseException parseException){
             parseException.printStackTrace();
         }
     }
     
   /** resisters all the components and adding the listener for the componets
    *in the form and sets the FocusTraversalPolicies
    */  
    public void registerComponents() {
        CustomFocusAdapter customFocusAdapter = new CustomFocusAdapter();
        awardCloseOutForm.btnOk.addActionListener(this);
        awardCloseOutForm.btnCancel.addActionListener(this);
        
        awardCloseOutForm.txtInvoiceSubDate.addFocusListener(customFocusAdapter);
        awardCloseOutForm.txtTechnicalSubDate.addFocusListener(customFocusAdapter);
        awardCloseOutForm.txtPatentSubDate.addFocusListener(customFocusAdapter);
        awardCloseOutForm.txtPropertSubDate.addFocusListener(customFocusAdapter);
        awardCloseOutForm.txtCloseoutDate.addFocusListener(customFocusAdapter);
        
        awardCloseOutForm.txtArchiveLocation.setDocument(new LimitedPlainDocument(50));
        awardCloseOutForm.txtTechnicalSubDate.setDocument(new LimitedPlainDocument(DATE_MAX_LIMIT));
        awardCloseOutForm.txtTechnicalSubDate.setDocument(new LimitedPlainDocument(DATE_MAX_LIMIT));
        awardCloseOutForm.txtPatentSubDate.setDocument(new LimitedPlainDocument(DATE_MAX_LIMIT));
        awardCloseOutForm.txtPropertSubDate.setDocument(new LimitedPlainDocument(DATE_MAX_LIMIT));
        awardCloseOutForm.txtCloseoutDate.setDocument(new LimitedPlainDocument(DATE_MAX_LIMIT));
        awardCloseOutForm.txtInvoiceSubDate.setDocument(new LimitedPlainDocument(DATE_MAX_LIMIT));
        
        if(functionType!= DISPLAY_MODE){
            java.awt.Component components[] = { awardCloseOutForm.txtArchiveLocation,
                                                awardCloseOutForm.txtCloseoutDate,
                                                awardCloseOutForm.txtInvoiceSubDate,
                                                awardCloseOutForm.txtTechnicalSubDate,
                                                awardCloseOutForm.txtPatentSubDate,
                                                awardCloseOutForm.txtPropertSubDate,
                                                awardCloseOutForm.btnOk,
                                                awardCloseOutForm.btnCancel
          
                                              };
        ScreenFocusTraversalPolicy  traversePolicy = new ScreenFocusTraversalPolicy( components );
        awardCloseOutForm.setFocusTraversalPolicy(traversePolicy);
        awardCloseOutForm.setFocusCycleRoot(true);
        }
        
    }
    
    /** sets the form data. Get the data from the server only if the values are not
     * there else get the data from the server by making a server call.
     */    
    public void setFormData(Object awardBaseBean) {
        try{
            awardBaseBean = (AwardBaseBean)awardBaseBean;
            AwardReportTermsBean beans = null;
            awardcloseoutData = new Hashtable();
            boolean isFinalPresent = false;
            cvParameterData = new CoeusVector();
            awardcloseoutData = getCloseOutData();
            if(functionType!= NEW_CHILD_COPIED && functionType!= NEW_AWARD){
            CoeusVector cvData = queryEngine.executeQuery(queryKey,AwardCloseOutBean.class, CoeusVector.FILTER_ACTIVE_BEANS);
            if(cvData!= null && cvData.size() > 0){
                awardCloseOutBean = (AwardCloseOutBean)cvData.get(0);
            }else{
                    awardcloseoutData = getCloseOutData();
                    CoeusVector cvAwardCloseOut = (CoeusVector)awardcloseoutData.get(AwardCloseOutBean.class);
                    awardCloseOutBean = (AwardCloseOutBean)cvAwardCloseOut.elementAt(0);
                }
            }
           
           CoeusVector cvData = queryEngine.executeQuery(queryKey,AwardCloseOutBean.class, CoeusVector.FILTER_ACTIVE_BEANS);
            if(cvData!= null && cvData.size() > 0){
                awardCloseOutBean = (AwardCloseOutBean)cvData.get(0);
            }
           cvParameterData = (CoeusVector)awardcloseoutData.get(CoeusParameterBean.class);
            if(functionType==NEW_AWARD){
                  CoeusVector cvReports = queryEngine.executeQuery(queryKey,AwardReportTermsBean.class, CoeusVector.FILTER_ACTIVE_BEANS);
                if(cvReports!= null && cvReports.size() > 0){
                    for(int index=0; index < cvReports.size(); index++){
                        beans = (AwardReportTermsBean)cvReports.get(index);
                    }
                   if(beans.isFinalReport()){
                        Hashtable htData = getCloseOutDateInNewMode();
                        isFinalPresent = true;
                        CoeusVector cvDateData = (CoeusVector)htData.get(AwardCloseOutBean.class);
                        if(cvDateData!= null && cvDateData.size() > 0){
                            awardCloseOutBean = (AwardCloseOutBean)cvDateData.get(0);
                        }
                    }else{
                        isFinalPresent = false;
                    }
                }
            }
            
            
            AwardDetailsBean awardDetailsBean = null;
            CoeusVector cvAwardDetails = new CoeusVector();
            cvCloseoutData = new CoeusVector();
            cvAwardDetails = queryEngine.executeQuery(queryKey,AwardDetailsBean.class, CoeusVector.FILTER_ACTIVE_BEANS);
            //cvCloseoutData  = queryEngine.executeQuery(queryKey, AwardCloseOutBean.class, CoeusVector.FILTER_ACTIVE_BEANS);
            
            
            //set the value for the header
            if(cvAwardDetails!= null && cvAwardDetails.size() > 0){
                awardDetailsBean = (AwardDetailsBean)cvAwardDetails.get(0);
                awardCloseOutForm.awardHeaderForm.setFormData(awardDetailsBean);
                //Case #2336 start
                awardCloseOutForm.awardHeaderForm.lblSequenceNumberValue.setText(EMPTY_STRING+((AwardBaseBean)awardBaseBean).getSequenceNumber());
                //Case #2336 end
            }
            if(functionType==NEW_CHILD_COPIED){
                // Commented COEUSDEV-547 : Award - Copied awards are copying close out information from source award, when it should not - Start
//                CoeusVector cvTemp = queryEngine.getDetails(queryKey, AwardCloseOutBean.class);
//                if(cvTemp!= null && cvTemp.size() > 0){
//                    awardCloseOutBean  = (AwardCloseOutBean)cvTemp.get(0);
//                }else{
//                    awardCloseOutBean  = (AwardCloseOutBean)awardcloseoutData.get(AwardCloseOutBean.class);
//                }
//              // Commented COEUSDEV-547 : Award - Copied awards are copying close out information from source award, when it should not - End  
                cvParameterData = queryEngine.getDetails(queryKey,CoeusParameterBean.class);
            }
            
            if(awardCloseOutBean!= null){
                
                // set the Archive Location
                if(awardCloseOutBean.getArchiveLocation()!= null && (!(awardCloseOutBean.getArchiveLocation().equals(EMPTY_STRING)))){
                    awardCloseOutForm.txtArchiveLocation.setText(awardCloseOutBean.getArchiveLocation().trim());
                }else{
                    awardCloseOutForm.txtArchiveLocation.setText(null);
                }
                
                // set the closeout date
                if(awardCloseOutBean.getCloseOutDate()!= null){
                    String closeOutDate = dateUtils.formatDate(awardCloseOutBean.getCloseOutDate().toString(),REQUIRED_DATEFORMAT);
                    awardCloseOutForm.txtCloseoutDate.setText(closeOutDate);
                }else{
                    awardCloseOutForm.txtCloseoutDate.setText(null);
                }
                
                // set the invoice date
                if(awardCloseOutBean.getFinalInvSubmissionDate()!= null){
                    String invoiceDate = dateUtils.formatDate(awardCloseOutBean.getFinalInvSubmissionDate().toString(),REQUIRED_DATEFORMAT);
                    awardCloseOutForm.txtInvoiceSubDate.setText(invoiceDate);
                }else{
                    awardCloseOutForm.txtInvoiceSubDate.setText(null);
                }
                
                // Set the Technical Date
                if(awardCloseOutBean.getFinalTechSubmissionDate()!= null){
                    String technicalDate = dateUtils.formatDate(awardCloseOutBean.getFinalTechSubmissionDate().toString(),REQUIRED_DATEFORMAT);
                    awardCloseOutForm.txtTechnicalSubDate.setText(technicalDate);
                }else{
                    awardCloseOutForm.txtTechnicalSubDate.setText(null);
                }
                
                // set the Patent date
                
                if(awardCloseOutBean.getFinalPatentSubmissionDate()!= null){
                    String patentDate = dateUtils.formatDate(awardCloseOutBean.getFinalPatentSubmissionDate().toString(),REQUIRED_DATEFORMAT);
                    awardCloseOutForm.txtPatentSubDate.setText(patentDate);
                }else{
                    awardCloseOutForm.txtPatentSubDate.setText(null);
                }
                
                //set the Property date
                if(awardCloseOutBean.getFinalPropSubmissionDate()!= null){
                    String patentDate = dateUtils.formatDate(awardCloseOutBean.getFinalPropSubmissionDate().toString(),REQUIRED_DATEFORMAT);
                    awardCloseOutForm.txtPropertSubDate.setText(patentDate);
                }else{
                    awardCloseOutForm.txtPropertSubDate.setText(null);
                }
                
                
                
                //set the invoice due date
                if(functionType==NEW_AWARD){
                    if(isFinalPresent== false){
                        awardCloseOutForm.txtInvoiceDate.setText(null);
                        awardCloseOutBean.setInvoiceDueDate(null);
                        queryEngine.update(queryKey,awardCloseOutBean);
                    }else{
                        if(awardCloseOutBean.getInvoiceDueDate()!= null){
                         awardCloseOutForm.txtInvoiceDate.setText(awardCloseOutBean.getInvoiceDueDate());
                        }
                    }
                }else{
                    if(awardCloseOutBean.getInvoiceDueDate()!= null){
                         awardCloseOutForm.txtInvoiceDate.setText(awardCloseOutBean.getInvoiceDueDate());
                    }
                }
                // setting the technical due date
                if(functionType==NEW_AWARD){
                    if(isFinalPresent== false){
                        awardCloseOutForm.txtTechnicalDate.setText(null);
                        awardCloseOutBean.setTechnicalDueDate(null);
                        queryEngine.update(queryKey,awardCloseOutBean);
                    }else{
                        if(awardCloseOutBean.getTechnicalDueDate()!= null){
                            awardCloseOutForm.txtTechnicalDate.setText(awardCloseOutBean.getTechnicalDueDate());
                        }
                    }
                }else{
                    if(awardCloseOutBean.getTechnicalDueDate()!= null){
                        awardCloseOutForm.txtTechnicalDate.setText(awardCloseOutBean.getTechnicalDueDate());
                    }
                }
                // setting the patent due date
                if(functionType==NEW_AWARD){
                    if(isFinalPresent==false){
                        awardCloseOutForm.txtPatentDate.setText(null);
                        awardCloseOutBean.setPatentDueDate(null);
                        queryEngine.update(queryKey,awardCloseOutBean);
                    }else{
                         if(awardCloseOutBean.getPatentDueDate()!= null){
                            awardCloseOutForm.txtPatentDate.setText(awardCloseOutBean.getPatentDueDate());
                        }
                    }
                }else{
                    if(awardCloseOutBean.getPatentDueDate()!= null){
                        awardCloseOutForm.txtPatentDate.setText(awardCloseOutBean.getPatentDueDate());
                    }
                }
                
                // setting the property due date
                if(functionType==NEW_AWARD){
                    if(isFinalPresent==false){
                        awardCloseOutForm.txtIPropertyDate.setText(null);
                        awardCloseOutBean.setPropertyDueDate(null);
                        queryEngine.update(queryKey,awardCloseOutBean);
                    }else{
                        if(awardCloseOutBean.getPropertyDueDate()!= null){
                            awardCloseOutForm.txtIPropertyDate.setText(awardCloseOutBean.getPropertyDueDate());
                        }
                    }
                }else{
                    if(awardCloseOutBean.getPropertyDueDate()!= null){
                        awardCloseOutForm.txtIPropertyDate.setText(awardCloseOutBean.getPropertyDueDate());
                    }
                }
            }
        }catch (CoeusException coeusException){
            coeusException.printStackTrace();
        }
    }
    
    /** Contact the server initially if there is no data from the queryEngine.
     *Get the awardCloseoutBean and set it to the form components
     * @return the AwardCloseoutBean.
     */    
    private Hashtable getCloseOutData(){
         Hashtable htData = null;
        RequesterBean requester;
        ResponderBean responder;
        
        requester = new RequesterBean();
        requester.setFunctionType(GET_CLOSE_OUT_DATA);
        requester.setDataObject(awardBaseBean.getMitAwardNumber());
        
        AppletServletCommunicator comm
        = new AppletServletCommunicator(connect, requester);
        
        comm.send();
        responder = comm.getResponse();
        if(responder.isSuccessfulResponse()){
            htData = (Hashtable)responder.getDataObject();
        }else{
            CoeusOptionPane.showInfoDialog(responder.getMessage());
        }
        
        return htData;
    }
    
    /** It is an overridden method. Specifies the components in the different modes
     *that it has been opened
     */
    public void formatFields() {
        if(functionType==DISPLAY_MODE){
            awardCloseOutForm.txtArchiveLocation.setEditable(false);
            awardCloseOutForm.txtCloseoutDate.setEditable(false);
            awardCloseOutForm.txtInvoiceSubDate.setEditable(false);
            awardCloseOutForm.txtTechnicalSubDate.setEditable(false);
            awardCloseOutForm.txtPatentSubDate.setEditable(false);
            awardCloseOutForm.txtPropertSubDate.setEditable(false);
            awardCloseOutForm.btnOk.setEnabled(false);
        }
    }
    
    /**
     * @return the form file
     */    
    public java.awt.Component getControlledUI() {
        return awardCloseOutForm;
    }
    
    public Object getFormData() {
        return null;
    }
    /** This method will validate the form data
     */
    public boolean validate() throws edu.mit.coeus.exception.CoeusUIException {
        String strDate;
        // Validattion for the invoice submission date
        strDate = awardCloseOutForm.txtInvoiceSubDate.getText().trim();
        if(!strDate.equals(EMPTY_STRING)) {
            String strDate1 = dateUtils.formatDate(strDate, DATE_SEPARATERS, REQUIRED_DATEFORMAT);
            if(strDate1 == null) {
                strDate1 = dateUtils.restoreDate(strDate, DATE_SEPARATERS);
                if( strDate1 == null || strDate1.equals(strDate)) {
                    CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(DATE_VALIDATION));
                    awardCloseOutForm.txtInvoiceSubDate.requestFocusInWindow();
                    return false;
                }
            }else {
                strDate = strDate1;
                awardCloseOutForm.txtInvoiceSubDate.setText(strDate);
            }
        }
        // Validation for the Technical Submission date
        strDate = awardCloseOutForm.txtTechnicalSubDate.getText().trim();
        if(!strDate.equals(EMPTY_STRING)) {
            String strDate1 = dateUtils.formatDate(strDate, DATE_SEPARATERS, REQUIRED_DATEFORMAT);
            if(strDate1 == null) {
                strDate1 = dateUtils.restoreDate(strDate, DATE_SEPARATERS);
                if( strDate1 == null || strDate1.equals(strDate)) {
                    CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(DATE_VALIDATION));
                    awardCloseOutForm.txtTechnicalSubDate.requestFocusInWindow();
                    return false;
                }
            }else {
                strDate = strDate1;
                awardCloseOutForm.txtTechnicalSubDate.setText(strDate);
            }
        }
        // Validation for the patent submission date
        strDate = awardCloseOutForm.txtPatentSubDate.getText().trim();
        if(!strDate.equals(EMPTY_STRING)) {
            String strDate1 = dateUtils.formatDate(strDate, DATE_SEPARATERS, REQUIRED_DATEFORMAT);
            if(strDate1 == null) {
                strDate1 = dateUtils.restoreDate(strDate, DATE_SEPARATERS);
                if( strDate1 == null || strDate1.equals(strDate)) {
                    CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(DATE_VALIDATION));
                    awardCloseOutForm.txtPatentSubDate.requestFocusInWindow();
                    return false;
                }
            }else {
                strDate = strDate1;
                awardCloseOutForm.txtPatentSubDate.setText(strDate);
            }
        }
        // Validation for the proerty submission date
        strDate = awardCloseOutForm.txtPropertSubDate.getText().trim();
        if(!strDate.equals(EMPTY_STRING)) {
            String strDate1 = dateUtils.formatDate(strDate, DATE_SEPARATERS, REQUIRED_DATEFORMAT);
            if(strDate1 == null) {
                strDate1 = dateUtils.restoreDate(strDate, DATE_SEPARATERS);
                if( strDate1 == null || strDate1.equals(strDate)) {
                    CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(DATE_VALIDATION));
                    awardCloseOutForm.txtPropertSubDate.requestFocusInWindow();
                    return false;
                }
            }else {
                strDate = strDate1;
                awardCloseOutForm.txtPropertSubDate.setText(strDate);
            }
        }
        
        strDate = awardCloseOutForm.txtCloseoutDate.getText().trim();
        if(!strDate.equals(EMPTY_STRING)) {
            String strDate1 = dateUtils.formatDate(strDate, DATE_SEPARATERS, REQUIRED_DATEFORMAT);
            if(strDate1 == null) {
                strDate1 = dateUtils.restoreDate(strDate, DATE_SEPARATERS);
                if( strDate1 == null || strDate1.equals(strDate)) {
                    CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(DATE_VALIDATION));
                    awardCloseOutForm.txtCloseoutDate.requestFocusInWindow();
                    return false;
                }
            }else {
                strDate = strDate1;
                awardCloseOutForm.txtCloseoutDate.setText(strDate);
            }
        }
        return true;
    }
    
    /** It is an overridden method which will saves the form data
     */
    public void saveFormData() {
        StrictEquals stCloseout = new StrictEquals();
        AwardCloseOutBean qryBean = new AwardCloseOutBean();
        Hashtable htData = new Hashtable();
        htData = getCloseOutData();
        try{
            setSaveFormData();
            if(validate()){
                CoeusVector cvdTemp = queryEngine.executeQuery(queryKey,AwardCloseOutBean.class, CoeusVector.FILTER_ACTIVE_BEANS);
                if(cvdTemp!= null && cvdTemp.size() > 0){
                    qryBean = (AwardCloseOutBean)cvdTemp.get(0);
                }else{
                    CoeusVector cvTempData = (CoeusVector)htData.get(AwardCloseOutBean.class);
                    if(cvTempData!= null && cvTempData.size()> 0){
//                    qryBean = (AwardCloseOutBean)htData.get(AwardCloseOutBean.class);
                        qryBean = (AwardCloseOutBean)cvTempData.elementAt(0);
                    }
                }
                dataChange = stCloseout.compare(awardCloseOutBean, qryBean);
                if(!dataChange){
                    CoeusVector dataObject = new CoeusVector();
                    dataObject.add(awardCloseOutBean);
                    if(dataObject!= null && dataObject.size()>0){
                        if(functionType==NEW_AWARD){
                            awardCloseOutBean.setAcType(TypeConstants.INSERT_RECORD);
                            queryEngine.addCollection(queryKey,AwardCloseOutBean.class, dataObject);
                        }
                        awardCloseOutBean.setAcType(TypeConstants.UPDATE_RECORD);
                        queryEngine.addCollection(queryKey,AwardCloseOutBean.class, dataObject);
                    }
                }
                dlgCloseout.dispose();
            }
        }catch (CoeusException coeusException){
            coeusException.printStackTrace();
        }catch (CoeusUIException coeusUIException){
            coeusUIException.printStackTrace();
        }
    }
    
    public void display() {
        CoeusVector cvData = null;
        // Check for the  Parameter value for FISCAL_CLASS_CODE
        if(cvParameterData!= null){// && cvParameterData.size() > 0){
            cvData = cvParameterData.filter(new Equals(
            "parameterName", CoeusConstants.FISCAL_CLASS_CODE));
            if(cvData==null || cvData.size() <=0){
                CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(MISSING_FISCAL_CODE));
            }
        }
        // Check for the  Parameter value for TECHNICAL_MANAGEMENT_CLASS_CODE
        if(cvParameterData!= null){// && cvParameterData.size() > 0){
            cvData = cvParameterData.filter(new Equals(
            "parameterName", CoeusConstants.TECHNICAL_MANAGEMENT_CLASS_CODE));
            if(cvData==null || cvData.size() <=0){
                CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(MISSING_TECHNICAL_CODE ));
            }
        }
        
        // Check for the  Parameter value for INTELLECTUAL_PROPERTY_CLASS_CODE
        if(cvParameterData!= null){// && cvParameterData.size() > 0){
            cvData = cvParameterData.filter(new Equals(
            "parameterName", CoeusConstants.INTELLECTUAL_PROPERTY_CLASS_CODE));
            if(cvData==null || cvData.size() <=0){
                CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(MISSING_PATENT_CODE));
            }
        }
        
        // Check for the  Parameter value for PROPERTY_CLASS_CODE
        if(cvParameterData!= null){// && cvParameterData.size() > 0){
            cvData = cvParameterData.filter(new Equals(
            "parameterName", CoeusConstants.PROPERTY_CLASS_CODE));
            if(cvData==null || cvData.size() <=0){
                CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(MISSING_PROPERTY_CODE));
            }
        }
        dlgCloseout.setVisible(true);
    }
    
    public void actionPerformed(ActionEvent actionEvent) {
        Object source = actionEvent.getSource();
        
        if(source.equals(awardCloseOutForm.btnOk)){
           saveFormData();
        }else if(source.equals(awardCloseOutForm.btnCancel)){
            performCancelAction();
        }
    }
    
    /** An inner class provides the focusLost and focusGained methods
     *which will validate the dates when tabs out.
     */
    public class CustomFocusAdapter extends java.awt.event.FocusAdapter{
        
        /**
         * @param focusEvent
         */        
        public void focusGained(FocusEvent focusEvent){
            Object source = focusEvent.getSource();
            if(focusEvent.isTemporary()) return ;
            
            if(source.equals(awardCloseOutForm.txtInvoiceSubDate)){
                String invoiceDate;
                invoiceDate= awardCloseOutForm.txtInvoiceSubDate.getText();
                invoiceDate= dateUtils.restoreDate(invoiceDate, DATE_SEPARATERS);
                awardCloseOutForm.txtInvoiceSubDate.setText(invoiceDate);
            }else if(source.equals(awardCloseOutForm.txtTechnicalSubDate)){
                String technicalDate;
                technicalDate= awardCloseOutForm.txtTechnicalSubDate.getText();
                technicalDate= dateUtils.restoreDate(technicalDate, DATE_SEPARATERS);
                awardCloseOutForm.txtTechnicalSubDate.setText(technicalDate);
            }else if(source.equals(awardCloseOutForm.txtPatentSubDate)){
                String patentDate;
                patentDate= awardCloseOutForm.txtPatentSubDate.getText();
                patentDate= dateUtils.restoreDate(patentDate, DATE_SEPARATERS);
                awardCloseOutForm.txtPatentSubDate.setText(patentDate);
            }else if(source.equals(awardCloseOutForm.txtPropertSubDate)){
                String propertyDate;
                propertyDate= awardCloseOutForm.txtPropertSubDate.getText();
                propertyDate= dateUtils.restoreDate(propertyDate, DATE_SEPARATERS);
                awardCloseOutForm.txtPropertSubDate.setText(propertyDate);
            }else if(source.equals(awardCloseOutForm.txtCloseoutDate)){
                String closeOutDate;
                closeOutDate= awardCloseOutForm.txtCloseoutDate.getText();
                closeOutDate= dateUtils.restoreDate(closeOutDate, DATE_SEPARATERS);
                awardCloseOutForm.txtCloseoutDate.setText(closeOutDate);
            }
        }
        
        /**
         * @param focusEvent
         */        
        public void focusLost(FocusEvent focusEvent) {
            if(focusEvent.isTemporary()) return ;
            
            Object source = focusEvent.getSource();
            if(source.equals(awardCloseOutForm.txtInvoiceSubDate)){
                String invoiceDate;
                invoiceDate = awardCloseOutForm.txtInvoiceSubDate.getText().trim();
                
                if(invoiceDate.equals(EMPTY_STRING)) return ;
                
                invoiceDate = dateUtils.formatDate(
                invoiceDate,DATE_SEPARATERS, REQUIRED_DATEFORMAT);
                
                if(invoiceDate == null) {
                    CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(DATE_VALIDATION));
                    setRequestFocusInThread(awardCloseOutForm.txtInvoiceSubDate);
                }else {
                    awardCloseOutForm.txtInvoiceSubDate.setText(invoiceDate);
                }
            }else if(source.equals(awardCloseOutForm.txtTechnicalSubDate)){
                String technicalDate;
                technicalDate= awardCloseOutForm.txtTechnicalSubDate.getText().trim();
                
                if(technicalDate.equals(EMPTY_STRING)) return ;
                
                technicalDate = dateUtils.formatDate(
                technicalDate,DATE_SEPARATERS, REQUIRED_DATEFORMAT);
                
                if(technicalDate== null) {
                    CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(DATE_VALIDATION));
                    setRequestFocusInThread(awardCloseOutForm.txtTechnicalSubDate);
                }else {
                    awardCloseOutForm.txtTechnicalSubDate.setText(technicalDate);
                }
            }else if(source.equals(awardCloseOutForm.txtPatentSubDate)){
                String patentDate;
                patentDate= awardCloseOutForm.txtPatentSubDate.getText().trim();
                
                if(patentDate.equals(EMPTY_STRING)) return ;
                
                patentDate = dateUtils.formatDate(
                patentDate,DATE_SEPARATERS, REQUIRED_DATEFORMAT);
                
                if(patentDate== null) {
                    CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(DATE_VALIDATION));
                    setRequestFocusInThread(awardCloseOutForm.txtPatentSubDate);
                }else {
                    awardCloseOutForm.txtPatentSubDate.setText(patentDate);
                }
            }else if(source.equals(awardCloseOutForm.txtPropertSubDate)){
                String propDate;
                propDate= awardCloseOutForm.txtPropertSubDate.getText().trim();
                
                if(propDate.equals(EMPTY_STRING)) return ;
                
                propDate = dateUtils.formatDate(
                propDate,DATE_SEPARATERS, REQUIRED_DATEFORMAT);
                
                if(propDate== null) {
                    CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(DATE_VALIDATION));
                    setRequestFocusInThread(awardCloseOutForm.txtPropertSubDate);
                }else {
                    awardCloseOutForm.txtPropertSubDate.setText(propDate);
                }
            }else if(source.equals(awardCloseOutForm.txtCloseoutDate)){
                String closeOutDate;
                closeOutDate = awardCloseOutForm.txtCloseoutDate.getText().trim();
                
                if(closeOutDate.equals(EMPTY_STRING)) return ;
                
                closeOutDate = dateUtils.formatDate(
                closeOutDate,DATE_SEPARATERS, REQUIRED_DATEFORMAT);
                
                if(closeOutDate== null) {
                    CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(DATE_VALIDATION));
                    setRequestFocusInThread(awardCloseOutForm.txtCloseoutDate);
                }else {
                    awardCloseOutForm.txtCloseoutDate.setText(closeOutDate);
                }
            }
        }
    }
    
     /** Supporting method which will be used for the focus lost for date
     *fields. This will be fired when the request focus for the specified
     *date field is invoked
     */
    private void setRequestFocusInThread(final java.awt.Component component) {
        javax.swing.SwingUtilities.invokeLater( new Runnable() {
            public void run() {
                component.requestFocusInWindow();
            }
        });
    }
    
    
    public void cleanUp(){
        
    }
    
    
    private Hashtable getCloseOutDateInNewMode(){
       Hashtable hshCloseOutData = new Hashtable();
       String mitAwardNumber = awardBaseBean.getMitAwardNumber();
       AwardReportTermsBean awardReportTermsBean = new AwardReportTermsBean();
       DateUtils dateUtils = new DateUtils();
       CoeusVector cvFileterdRateCodes = new CoeusVector();
       String paramValue = EMPTY_STRING;
       try{
            CoeusVector cvDate = queryEngine.executeQuery(queryKey,AwardCloseOutBean.class,CoeusVector.FILTER_ACTIVE_BEANS);
            if(cvDate!= null && cvDate.size() > 0){
                AwardCloseOutBean awardCloseOutBean  = (AwardCloseOutBean)cvDate.get(0);
            }
            if(awardCloseOutBean==null){
                awardCloseOutBean = new AwardCloseOutBean();
                awardCloseOutBean.setMitAwardNumber(mitAwardNumber);
            }

           // DepartmentPersonTxnBean departmentPersonTxnBean = new DepartmentPersonTxnBean();
            Date dueDate = null;
            int mulipleEntries = 0;
            CoeusVector cvFilteredData = null;        
            Equals eqReportClassCode = null;
            Equals eqFinalReport = null;
            And eqReportClassCodeAndFinalReport = null;

            //Get Report Terms
            CoeusVector awardReportTerms  = queryEngine.executeQuery(queryKey,AwardReportTermsBean.class,CoeusVector.FILTER_ACTIVE_BEANS);
            if(awardReportTerms==null){
                awardReportTerms = new CoeusVector();
            }
            //CoeusVector awardReportTerms = ObjectCloner.deepCopy(awardReportTerms);

            //Get All Frequency

            //CoeusVector cvFrequency = awardLookUpDataTxnBean.getFrequency();
            CoeusVector cvFrequency  = queryEngine.executeQuery(queryKey,FrequencyBean.class, CoeusVector.FILTER_ACTIVE_BEANS);

            CoeusVector cvFilteredFrequency = null;
            FrequencyBean frequencyBean = null;
            int frequencyDays;
            int frequencyMonths;

            //Get Money and End Dates Data
            AwardAmountInfoBean awardAmountInfoBean = new AwardAmountInfoBean();

            CoeusVector cvAwardAmount = queryEngine.executeQuery(queryKey,AwardAmountInfoBean.class,CoeusVector.FILTER_ACTIVE_BEANS);
            if(cvAwardAmount== null){
                cvAwardAmount = new CoeusVector();
            }

            awardAmountInfoBean = (AwardAmountInfoBean)cvAwardAmount.get(0);

            awardAmountInfoBean.setMitAwardNumber(mitAwardNumber);        


            //Get Award Header Data
            //AwardHeaderBean awardHeaderBean = awardTxnBean.getAwardHeader(mitAwardNumber);        

            CoeusVector cvAwardHeader = queryEngine.executeQuery(queryKey,AwardHeaderBean.class,CoeusVector.FILTER_ACTIVE_BEANS);
            if(cvAwardHeader == null){
                cvAwardHeader = new CoeusVector();
            }

            AwardHeaderBean awardHeaderBean = (AwardHeaderBean)cvAwardHeader.get(0);
            //Holds all class codes
            CoeusVector cvClassCodes = new CoeusVector();
            CoeusParameterBean coeusParameterBean = new CoeusParameterBean();

            //CoeusVector cvParameter = queryEngine.getDetails(queryKey,CoeusParameterBean.class);
            
            cvFileterdRateCodes = cvParameterData.filter(new Equals(PARAMETER_VALUE, CoeusConstants.FISCAL_CLASS_CODE));
	    if(cvFileterdRateCodes!= null && cvFileterdRateCodes.size() > 0){
                coeusParameterBean  = (CoeusParameterBean)cvFileterdRateCodes.get(0);
                paramValue = coeusParameterBean.getParameterValue();
            }
            
            if(paramValue!=null && (!paramValue.trim().equals(EMPTY_STRING))){
                coeusParameterBean.setParameterName(CoeusConstants.FISCAL_CLASS_CODE);
                coeusParameterBean.setParameterValue(paramValue);
                //coeusParameterBean.setParameterValue("1");
                cvClassCodes.addElement(coeusParameterBean);

                //eqReportClassCode = new Equals("reportClassCode", new Integer(paramValue));
                eqReportClassCode = new Equals("reportClassCode", new Integer(paramValue));
                eqFinalReport = new Equals("finalReport", true);
                eqReportClassCodeAndFinalReport = new And(eqReportClassCode, eqFinalReport);            
                cvFilteredData = awardReportTerms.filter(eqReportClassCodeAndFinalReport);
                if(cvFilteredData.size() > 0){
                    awardReportTermsBean = (AwardReportTermsBean)cvFilteredData.elementAt(0);
                    cvFilteredData = getDistinctRows(cvFilteredData);
                        if(cvFilteredData.size() > 0){
                            calculateDueDate(cvFilteredData, cvFrequency, awardAmountInfoBean.getFinalExpirationDate());
                        }
                    cvFilteredData = removeDuplicateDueDateRows(cvFilteredData);                

                }else{
//                    if(cvFilteredData.size() == 0){
//                    awardCloseOutBean.setInvoiceDueDate("");
//                    }
                }
                if(cvFilteredData.size() == 0){
                    awardCloseOutBean.setInvoiceDueDate("");
                }else if(cvFilteredData.size() == 1){
                    awardCloseOutBean.setInvoiceDueDate(dateUtils.formatDate(((AwardReportTermsBean)cvFilteredData.elementAt(0)).getDueDate().toString(),"dd-MMM-yyyy"));
                }else if(cvFilteredData.size()>1){
                    //awardCloseOutBean.setInvoiceDueDate("MULTIPLE");
                }
                if(awardAmountInfoBean != null && 
                    awardAmountInfoBean.getFinalExpirationDate()!=null &&
                    cvFilteredData.size()!=0){
                    Calendar calendar = Calendar.getInstance();
                    calendar.clear();
                    calendar.setTime(awardAmountInfoBean.getFinalExpirationDate());
//                    calendar.set(Calendar.DATE, calendar.get(Calendar.DATE)+awardHeaderBean.getFinalInvoiceDue());                
                    calendar.set(Calendar.DATE, calendar.get(Calendar.DATE)+(awardHeaderBean.getFinalInvoiceDue()==null?0:awardHeaderBean.getFinalInvoiceDue().intValue()));
                    dueDate = new java.sql.Date(calendar.getTime().getTime());

                    if(cvFilteredData.size() == 0){
                        awardCloseOutBean.setInvoiceDueDate(dateUtils.formatDate(dueDate.toString(),"dd-MMM-yyyy"));
                    }else{
                        cvFilteredData = cvFilteredData.filter(new Equals("dueDate", dueDate));
                        if(cvFilteredData.size() == 0){
                            awardCloseOutBean.setInvoiceDueDate("MULTIPLE");
                        }else{
                            awardCloseOutBean.setInvoiceDueDate(dateUtils.formatDate(dueDate.toString(),"dd-MMM-yyyy"));
                        }
                    }
                }else{
//                    if(awardReportTerms.size() > 1){
//                        awardCloseOutBean.setInvoiceDueDate("MULTIPLE");
//                    }else if(awardReportTerms.size() == 1){
//                        awardReportTermsBean = (AwardReportTermsBean)awardReportTerms.get(0);
//                        awardCloseOutBean.setInvoiceDueDate(dateUtils.formatDate(((AwardReportTermsBean)awardReportTerms.elementAt(0)).getDueDate().toString(),"dd-MMM-yyyy"));
//                    }else{
//                        awardCloseOutBean.setInvoiceDueDate("");
//                    }
                }
            }else{
                awardCloseOutBean.setInvoiceDueDate("");
            } 

            //Set Technical date        
            cvFileterdRateCodes = cvParameterData.filter(new Equals(PARAMETER_VALUE, CoeusConstants.TECHNICAL_MANAGEMENT_CLASS_CODE));
            if(cvFileterdRateCodes!= null && cvFileterdRateCodes.size() > 0){
                coeusParameterBean  = (CoeusParameterBean)cvFileterdRateCodes.get(0);
                paramValue = coeusParameterBean.getParameterValue();
            }
            if(paramValue!=null && (!paramValue.trim().equals(EMPTY_STRING))){
                coeusParameterBean = new CoeusParameterBean();
                coeusParameterBean.setParameterName(CoeusConstants.TECHNICAL_MANAGEMENT_CLASS_CODE);
                coeusParameterBean.setParameterValue(paramValue);
                cvClassCodes.addElement(coeusParameterBean);

                eqReportClassCode = new Equals("reportClassCode", new Integer(paramValue));
                eqFinalReport = new Equals("finalReport", true);
                eqReportClassCodeAndFinalReport = new And(eqReportClassCode, eqFinalReport);            
                cvFilteredData = awardReportTerms.filter(eqReportClassCodeAndFinalReport);
                if(cvFilteredData.size() > 0){
                    awardReportTermsBean = (AwardReportTermsBean)cvFilteredData.elementAt(0);
                    cvFilteredData = getDistinctRows(cvFilteredData);
                    if(cvFilteredData.size() > 0){
                        calculateDueDate(cvFilteredData, cvFrequency, awardAmountInfoBean.getFinalExpirationDate());
                    }
                    cvFilteredData = removeDuplicateDueDateRows(cvFilteredData);
                    if(cvFilteredData.size() > 1){
                        awardCloseOutBean.setTechnicalDueDate("MULTIPLE");
                    }else if(cvFilteredData.size() == 1){
                        awardCloseOutBean.setTechnicalDueDate(dateUtils.formatDate(((AwardReportTermsBean)cvFilteredData.elementAt(0)).getDueDate().toString(),"dd-MMM-yyyy"));
                    }else{
                        awardCloseOutBean.setTechnicalDueDate("");
                    }
                }else if(cvFilteredData.size() == 0){
                    awardCloseOutBean.setTechnicalDueDate("");
                }
            }else{
                awardCloseOutBean.setTechnicalDueDate(null);
            }

            //Set Patent date
            cvFilteredData = null;
            cvFileterdRateCodes = cvParameterData.filter(new Equals(PARAMETER_VALUE, CoeusConstants.INTELLECTUAL_PROPERTY_CLASS_CODE));
            if(cvFileterdRateCodes!= null && cvFileterdRateCodes.size() > 0){
                coeusParameterBean  = (CoeusParameterBean)cvFileterdRateCodes.get(0);
                paramValue = coeusParameterBean.getParameterValue();
            }
            if(paramValue!=null && (!paramValue.trim().equals(EMPTY_STRING))){
                coeusParameterBean = new CoeusParameterBean();
                coeusParameterBean.setParameterName(CoeusConstants.INTELLECTUAL_PROPERTY_CLASS_CODE);
                coeusParameterBean.setParameterValue(paramValue);
                cvClassCodes.addElement(coeusParameterBean);

                eqReportClassCode = new Equals("reportClassCode", new Integer(paramValue));
                eqFinalReport = new Equals("finalReport", true);
                eqReportClassCodeAndFinalReport = new And(eqReportClassCode, eqFinalReport);            
                cvFilteredData = awardReportTerms.filter(eqReportClassCodeAndFinalReport);
                if(cvFilteredData.size() > 0){
                    awardReportTermsBean = (AwardReportTermsBean)cvFilteredData.elementAt(0);
                    cvFilteredData = getDistinctRows(cvFilteredData);
                    if(cvFilteredData.size() > 0){
                        calculateDueDate(cvFilteredData, cvFrequency, awardAmountInfoBean.getFinalExpirationDate());
                    }
                    cvFilteredData = removeDuplicateDueDateRows(cvFilteredData);
                    if(cvFilteredData.size() > 1){
                        awardCloseOutBean.setPatentDueDate("MULTIPLE");
                    }else if(cvFilteredData.size() == 1){
                        awardCloseOutBean.setPatentDueDate(dateUtils.formatDate(((AwardReportTermsBean)cvFilteredData.elementAt(0)).getDueDate().toString(),"dd-MMM-yyyy"));
                    }else{
                        awardCloseOutBean.setPatentDueDate("");
                    }
                }else if(cvFilteredData.size() == 1){
                    awardCloseOutBean.setPatentDueDate("");
                }
            }else{
                awardCloseOutBean.setPatentDueDate("");
            }
            //Set Property due date
           cvFileterdRateCodes = cvParameterData.filter(new Equals(PARAMETER_VALUE, CoeusConstants.PROPERTY_CLASS_CODE));
           if(cvFileterdRateCodes!= null && cvFileterdRateCodes.size()> 0){
            coeusParameterBean  = (CoeusParameterBean)cvFileterdRateCodes.get(0);
            paramValue = coeusParameterBean.getParameterValue();
           }
            if(paramValue!=null&& (!paramValue.trim().equals(EMPTY_STRING))){
                coeusParameterBean = new CoeusParameterBean();
                coeusParameterBean.setParameterName(CoeusConstants.PROPERTY_CLASS_CODE);
                coeusParameterBean.setParameterValue(paramValue);
                cvClassCodes.addElement(coeusParameterBean);

                eqReportClassCode = new Equals("reportClassCode", new Integer(paramValue));
                eqFinalReport = new Equals("finalReport", true);
                eqReportClassCodeAndFinalReport = new And(eqReportClassCode, eqFinalReport);            
                cvFilteredData = awardReportTerms.filter(eqReportClassCodeAndFinalReport);
                if(cvFilteredData.size() > 0){
                    awardReportTermsBean = (AwardReportTermsBean)cvFilteredData.elementAt(0);
                    cvFilteredData = getDistinctRows(cvFilteredData);
                    if(cvFilteredData.size() > 0){
                        calculateDueDate(cvFilteredData, cvFrequency, awardAmountInfoBean.getFinalExpirationDate());
                    }
                    cvFilteredData = removeDuplicateDueDateRows(cvFilteredData);
                    if(cvFilteredData.size() > 1){
                        awardCloseOutBean.setPropertyDueDate("MULTIPLE");
                    }else if(cvFilteredData.size() == 1){
                        awardCloseOutBean.setPropertyDueDate(dateUtils.formatDate(((AwardReportTermsBean)cvFilteredData.elementAt(0)).getDueDate().toString(),"dd-MMM-yyyy"));
                    }else{
                        awardCloseOutBean.setPropertyDueDate("");
                    }
                }else if(cvFilteredData.size() == 0){
                    awardCloseOutBean.setPropertyDueDate("");
                }
            }else{
                awardCloseOutBean.setPropertyDueDate("");
            }
            CoeusVector cvAwardCloseOut = new CoeusVector();
            AwardCloseOutBean qryAwardCloseout;
            try{
                qryAwardCloseout= (AwardCloseOutBean)ObjectCloner.deepCopy(awardCloseOutBean);
                cvAwardCloseOut.add(qryAwardCloseout); 
                queryEngine.addCollection(queryKey,AwardCloseOutBean.class,cvAwardCloseOut);
            }catch (Exception exception) {
                exception.printStackTrace();
            }
            
            hshCloseOutData.put(AwardCloseOutBean.class, cvAwardCloseOut);
            hshCloseOutData.put(CoeusParameterBean.class, cvClassCodes);
                        
       }catch (CoeusException coeusException){
           coeusException.printStackTrace();
       }
        return hshCloseOutData;
    }   
    
    //
    private void calculateDueDate(CoeusVector cvReportTerms, CoeusVector cvFrequency, java.sql.Date finalExpirationDate)
        throws CoeusException{
        try{
            AwardReportTermsBean awardReportTermsBean = null;
            CoeusVector cvFilteredFrequency = null;
            FrequencyBean frequencyBean = null;
            int frequencyDays = 0;
            int frequencyMonths = 0;
            java.sql.Date dueDate = null;
            Calendar calendar = Calendar.getInstance();
            calendar.clear();
            calendar.set(1900, 0, 1);
            java.sql.Date calendarDate = new java.sql.Date(calendar.getTime().getTime());

            java.sql.Date clonedDate = (java.sql.Date)ObjectCloner.deepCopy(finalExpirationDate);

            for(int row = 0; row < cvReportTerms.size(); row++){
                awardReportTermsBean = (AwardReportTermsBean)cvReportTerms.elementAt(row);

                if(awardReportTermsBean.getDueDate().equals(calendarDate)){
                    cvFilteredFrequency = cvFrequency.filter(new Equals("code", ""+awardReportTermsBean.getFrequencyCode()));
                    if(cvFilteredFrequency.size() > 0){
                        frequencyBean = (FrequencyBean)cvFilteredFrequency.elementAt(0);
                        frequencyDays = frequencyBean.getNumberOfDays();
                        frequencyMonths = frequencyBean.getNumberOfMonths();
                        calendar.setTime(finalExpirationDate);
                        calendar.set(Calendar.DATE, calendar.get(Calendar.DATE)+frequencyDays);
                        calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH)+frequencyMonths);
                        dueDate =  new java.sql.Date(calendar.getTime().getTime());
                        awardReportTermsBean.setDueDate(dueDate);
                    }
                }
            }
            //Reset the date
            finalExpirationDate = clonedDate;
        }catch(Exception ex){
            throw new CoeusException(ex.getMessage());
        }
    }
    
    private CoeusVector getDistinctRows(CoeusVector cvReportTerms){
        CoeusVector cvDistict = new CoeusVector();
        Equals eqReportCode = null;
        Equals eqFrequencyCode = null;
        Equals eqDueDate = null;
        NotEquals notRowId = null;
        And reportCodeAndFrequencyCode = null;
        And reportCodeAndFrequencyCodeAndDueDate = null;
        And rowId = null;
        
        AwardReportTermsBean awardReportTermsBean = null;
        CoeusVector filteredData = null;
        CoeusVector distinctData = new CoeusVector();
        
        for(int row=0; row < cvReportTerms.size(); row++){
            awardReportTermsBean = (AwardReportTermsBean)cvReportTerms.elementAt(row);
            eqReportCode = new Equals("reportCode", new Integer(awardReportTermsBean.getReportCode()));
            eqFrequencyCode = new Equals("frequencyCode", new Integer(awardReportTermsBean.getFrequencyCode()));
            eqDueDate = new Equals("dueDate", awardReportTermsBean.getDueDate());
            notRowId = new NotEquals("rowId", new Integer(awardReportTermsBean.getRowId()));
            
            reportCodeAndFrequencyCode = new And(eqReportCode, eqFrequencyCode);
            reportCodeAndFrequencyCodeAndDueDate = new And(reportCodeAndFrequencyCode, eqDueDate);
            rowId = new And(reportCodeAndFrequencyCodeAndDueDate, notRowId);
            filteredData = cvReportTerms.filter(rowId);
            if(filteredData.size() > 0){
                cvReportTerms.remove(row);
                row--;
            }
        }
        return cvReportTerms;
    }
    
    private CoeusVector removeDuplicateDueDateRows(CoeusVector cvReportTerms){
        Equals eqDueDate = null;
        NotEquals notRowId = null;
        And rowId = null;
        
        AwardReportTermsBean awardReportTermsBean = null;
        CoeusVector filteredData = null;
        for(int row=0; row < cvReportTerms.size(); row++){
            awardReportTermsBean = (AwardReportTermsBean)cvReportTerms.elementAt(row);
            eqDueDate = new Equals("dueDate", awardReportTermsBean.getDueDate());
            notRowId = new NotEquals("rowId", new Integer(awardReportTermsBean.getRowId()));
            rowId = new And(eqDueDate, notRowId);
            filteredData = cvReportTerms.filter(rowId);
            if(filteredData.size() > 0){
                cvReportTerms.remove(row);
                row--;
            }
        }
        return cvReportTerms;
    }
    
    
    
}
