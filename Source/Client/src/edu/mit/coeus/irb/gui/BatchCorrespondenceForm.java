/*
 * BatchCorrespondenceForm.java
 *
 * Created on March 2, 2004, 4:23 PM
 */

package edu.mit.coeus.irb.gui;


import java.util.Vector;
import java.util.Enumeration;
import java.util.Date;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.utils.CoeusDateFormat;
import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.utils.ScreenFocusTraversalPolicy;
import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.utils.AppletServletCommunicator;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.gui.*;
import edu.mit.coeus.irb.bean.*;
import edu.mit.coeus.utils.CoeusTextField;
import edu.mit.coeus.utils.DateUtils;
import edu.mit.coeus.utils.LimitedPlainDocument;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.Calendar;


public class BatchCorrespondenceForm extends edu.mit.coeus.gui.CoeusDlgWindow 
                                           implements ActionListener {
    
   private CoeusMessageResources messageResource;    
   private String connURL = CoeusGuiConstants.CONNECTION_URL +
                                                     "/correspondenceServlet";
   private DateUtils dtUtils = new DateUtils();
   private java.text.SimpleDateFormat dtFormat
            = new java.text.SimpleDateFormat("MM/dd/yyyy");

   private int defaultTimeWindow = 0 ;
   private String titleDescription ; 
   private String batchId ;
   private int batchTypeCode ;
   private String committeeId ;
   private String committeeName ;
   
   //prps start mar 25 2004
    private  StatusWindow statusWindow;
   //prps end mar 25 2004
   
    //prps code start Apr 1 2004
    private int finalActionTypeCode;
    private int finalActionCorrespondenceType ;
    //prps code end Apr 1 2004
   
    public BatchCorrespondenceForm(Vector vecParam) 
    {
        super(CoeusGuiConstants.getMDIForm(), true );
        this.committeeId = vecParam.get(0).toString() ;
        this.committeeName = vecParam.get(1).toString() ;
        this.batchTypeCode = Integer.parseInt(vecParam.get(2).toString()) ;
       
        statusWindow = new StatusWindow(CoeusGuiConstants.getMDIForm(), true);
    }
    
     public void showForm() throws Exception 
     {
        
        messageResource = CoeusMessageResources.getInstance();
        initComponents();
        
        txtCurrStartDate.setDocument(new LimitedPlainDocument(11));
        txtCurrStartDate.addFocusListener(new CustomFocusAdapter());

        txtCurrEndDate.setDocument(new LimitedPlainDocument(11));
        txtCurrEndDate.addFocusListener(new CustomFocusAdapter());
        
        btnOk.addActionListener(this);
        btnCancel.addActionListener(this);
                
      
        java.awt.Component[] components = {txtCurrStartDate, txtCurrEndDate, btnOk, btnCancel };
        ScreenFocusTraversalPolicy  traversePolicy = new ScreenFocusTraversalPolicy( components );
        pnlMain.setFocusTraversalPolicy(traversePolicy);
        pnlMain.setFocusCycleRoot(true);
        
        setResizable( false );
        setValues();
        pack();
     
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension dlgSize = getSize();
        setLocation(screenSize.width/2 - (dlgSize.width/2),
        screenSize.height/2 - (dlgSize.height/2));
        show();
             
    }
   
         
 
     
     private void setValues() throws Exception
     {
        txtCommitteeId.setText(this.committeeId) ;
        txtCommitteeName.setText(this.committeeName) ;
         
        Vector batchData  = getBatchDetails() ;
        if (batchData != null)
        {
            Vector vecBatchDetail = (Vector)batchData.get(0) ;
            if (vecBatchDetail != null)
            {
                if (vecBatchDetail.size() > 0)
                { // populate the data
                    BatchCorrespondenceBean batchCorrespondenceBean
                            = (BatchCorrespondenceBean) vecBatchDetail.get(0) ;
                    
                    this.defaultTimeWindow =  batchCorrespondenceBean.getDefaultTimeWindow() ;        
                    this.titleDescription = batchCorrespondenceBean.getDescription() ;
                 
                    //prps code start Apr 1 2004
                    // if finalActionCorrespondenceType = 0 & finalActionTypeCode = 0 
                    // then no action performed when all the reminder are generated and
                    // no correspondence generated
                    // if finalActionCorrespondenceType > 0  & finalActionTypeCode = 0 
                    // this doesnt happen
                    // if finalActionCorrespondenceType = 0  & finalActionTypeCode > 0 
                    // then action performed when all the reminder are generated and
                    // generate the standard correspondence(meaning the same correspondence)
                    // which is generated when an action is performed from schedule screen
                    // if finalActionCorrespondenceType > 0  & finalActionTypeCode > 0 
                    // then action performed when all the reminder are generated but this
                    // use the new correspondence code to get the correspondence
                    this.finalActionCorrespondenceType = batchCorrespondenceBean.getFinalActionCorrespondenceType() ;
                    this.finalActionTypeCode = batchCorrespondenceBean.getFinalActionTypeCode() ;
                    //prps code end Apr 1 2004
                    java.sql.Timestamp tempDate = (java.sql.Timestamp) batchCorrespondenceBean.getTodaysDate() ;
                    txtCurrStartDate.setText( dtUtils.formatDate((tempDate).toString(),
                                "dd-MMM-yyyy")) ;  
                    this.setTitle(titleDescription) ;
                }    
             }    
             
             Vector vecLastRunDetail = (Vector)batchData.get(1) ;
             if (vecLastRunDetail != null)
             {
                if (vecLastRunDetail.size() > 0)
                { // greater than zero then it is not first time the screen is opened, so no previous data
                  BatchCorrespondenceBean batchCorrespondenceBean
                            = (BatchCorrespondenceBean) vecLastRunDetail.get(0) ;
                  
                  this.batchId = batchCorrespondenceBean.getCorrespondenceBatchId() ;
                  
                  if (batchCorrespondenceBean.getBatchRunDate() != null)
                  {
                    txtLastRunDate.setText(dtUtils.formatDate(
                      batchCorrespondenceBean.getBatchRunDate().toString(), "dd-MMM-yyyy"))  ;
                  }
                  
                  if (batchCorrespondenceBean.getTimeWindowStart() != null)
                  {
                      txtLastStartDate.setText(dtUtils.formatDate(
                      batchCorrespondenceBean.getTimeWindowStart().toString(), "dd-MMM-yyyy"))  ;
                  }
                  
                  if (batchCorrespondenceBean.getTimeWindowEnd() != null)
                  {
                      txtLastEndDate.setText(dtUtils.formatDate(
                      batchCorrespondenceBean.getTimeWindowEnd().toString(), "dd-MMM-yyyy"))  ;
                  }    
                    
                }
                
             }    
        }// end if not null    
      
        
        showEndDate() ;
        
        
     }
     
     
     public Vector getBatchDetails() throws Exception
     {
        Vector batchData  = new Vector(3,2);
        
        RequesterBean request = new RequesterBean();
        request.setFunctionType('B');
        BatchCorrespondenceBean batchCorrespondenceBean = new BatchCorrespondenceBean() ;
        batchCorrespondenceBean.setCommitteeId(this.committeeId) ;
        batchCorrespondenceBean.setCorrespondenceBatchTypeCode(this.batchTypeCode) ;
        request.setDataObject(batchCorrespondenceBean) ;
        
        AppletServletCommunicator comm = new AppletServletCommunicator(
                                                    connURL, request );
        comm.send();
        ResponderBean response = comm.getResponse();
        if ( response.isSuccessfulResponse() ){
            batchData = response.getDataObjects();
            if( batchData == null || batchData.size() == 0 ) {
                throw new Exception(messageResource.parseMessageKey(
                    "abstractReportViewFrm_exceptionCode.1003"));
            }
        }else{
            
            if(response.getDataObject() != null){
                Object obj = response.getDataObject();
                if( obj  instanceof CoeusException){
                    throw (CoeusException)response.getDataObject();
                }
            }else{
                throw new Exception(response.getMessage());
            }
        }
        return batchData ;
      } 
    
     
     public boolean validateData() throws Exception
    {
        
    if((txtCurrStartDate.getText()== null || txtCurrEndDate.getText() == null))
    {
            /* Application Date doesn't have any value */
            txtCurrStartDate.requestFocus() ;
            return false;
            
    }
    else if (txtCurrStartDate.getText().trim().length() <= 0)
    {
        txtCurrStartDate.requestFocus() ;
        return false ;
    }    
    else if (txtCurrEndDate.getText().trim().length() <= 0)
    {
        txtCurrEndDate.requestFocus() ;
        return false ;
    }
    else{
            Date applnDate = null;
            Date apprDate = null;
            Date expDate = null;

            String oldDate;
            String convertedDate ;
            if((txtCurrStartDate.getText() != null)
                && (txtCurrStartDate.getText().trim().length() > 0)){

                convertedDate = dtUtils.formatDate(txtCurrStartDate.getText(),
                        "/-:," , "dd-MMM-yyyy");
                
                if (convertedDate==null){
                    oldDate = dtUtils.restoreDate(txtCurrStartDate.getText(),"/-:,");
                    if(oldDate == null || oldDate.equals(txtCurrStartDate.getText())){
                        txtCurrStartDate.requestFocus() ;
                        return false;
                    }
                }
                apprDate = dtFormat.parse(
                    dtUtils.restoreDate(txtCurrStartDate.getText(),"/:-,"));

                if(apprDate == null){
                    txtCurrStartDate.requestFocus() ;
                    return false;
                }
            }
            if((txtCurrEndDate.getText() != null)
                && (txtCurrEndDate.getText().trim().length() > 0)){

                convertedDate = dtUtils.formatDate(txtCurrEndDate.getText(),
                        "/-:," , "dd-MMM-yyyy");
                if (convertedDate==null){
                    oldDate = dtUtils.restoreDate(txtCurrEndDate.getText(),"/-:,");
                    if(oldDate == null || oldDate.equals(txtCurrEndDate.getText())){
                        txtCurrEndDate.requestFocus() ;
                        return false;
                    }
                }

                expDate = dtFormat.parse(
                    dtUtils.restoreDate(txtCurrEndDate.getText(),"/:-,"));
                if(expDate == null){
                    txtCurrEndDate.requestFocus() ;
                    return false;
                }
            }

            if(expDate != null && apprDate != null){
                if( (expDate.compareTo(apprDate) <0 ))
                {
                    txtCurrEndDate.requestFocus();  
                    return false;
                }
            }
           
        }
        return true;
    }
 

     
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        pnlMain = new javax.swing.JPanel();
        txtCommitteeId = new edu.mit.coeus.utils.CoeusTextField();
        lblCommitteeId = new javax.swing.JLabel();
        lblCommitteeName = new javax.swing.JLabel();
        txtCommitteeName = new edu.mit.coeus.utils.CoeusTextField();
        lblLastRunDate = new javax.swing.JLabel();
        txtLastRunDate = new edu.mit.coeus.utils.CoeusTextField();
        btnOk = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();
        lblLastStartDate = new javax.swing.JLabel();
        lblLastEndDate = new javax.swing.JLabel();
        txtLastStartDate = new edu.mit.coeus.utils.CoeusTextField();
        txtLastEndDate = new edu.mit.coeus.utils.CoeusTextField();
        txtCurrStartDate = new edu.mit.coeus.utils.CoeusTextField();
        txtCurrEndDate = new edu.mit.coeus.utils.CoeusTextField();
        lblCurrStartDate = new javax.swing.JLabel();
        lblCurrEndDate = new javax.swing.JLabel();
        lblLastDetail = new javax.swing.JLabel();
        lblCurrentDetail = new javax.swing.JLabel();

        getContentPane().setLayout(new java.awt.GridBagLayout());

        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                exitForm(evt);
            }
        });

        pnlMain.setLayout(new java.awt.GridBagLayout());

        txtCommitteeId.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 100;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 4, 0);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        pnlMain.add(txtCommitteeId, gridBagConstraints);

        lblCommitteeId.setFont(CoeusFontFactory.getLabelFont());
        lblCommitteeId.setLabelFor(lblCommitteeName);
        lblCommitteeId.setText("Committee Id:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 3, 3);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        pnlMain.add(lblCommitteeId, gridBagConstraints);

        lblCommitteeName.setFont(CoeusFontFactory.getLabelFont());
        lblCommitteeName.setText("Committee Name:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(1, 16, 4, 3);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        pnlMain.add(lblCommitteeName, gridBagConstraints);

        txtCommitteeName.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 150;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 4, 0);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        pnlMain.add(txtCommitteeName, gridBagConstraints);

        lblLastRunDate.setFont(CoeusFontFactory.getLabelFont());
        lblLastRunDate.setText("Run Date:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 3, 3);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        pnlMain.add(lblLastRunDate, gridBagConstraints);

        txtLastRunDate.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.ipadx = 100;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 4, 0);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        pnlMain.add(txtLastRunDate, gridBagConstraints);

        btnOk.setFont(CoeusFontFactory.getLabelFont());
        btnOk.setText("OK");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 13, 0, 0);
        pnlMain.add(btnOk, gridBagConstraints);

        btnCancel.setFont(CoeusFontFactory.getLabelFont());
        btnCancel.setText("Cancel");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.ipadx = 8;
        gridBagConstraints.insets = new java.awt.Insets(0, 14, 0, 0);
        pnlMain.add(btnCancel, gridBagConstraints);

        lblLastStartDate.setFont(CoeusFontFactory.getLabelFont());
        lblLastStartDate.setText("Start Date:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 3);
        pnlMain.add(lblLastStartDate, gridBagConstraints);

        lblLastEndDate.setFont(CoeusFontFactory.getLabelFont());
        lblLastEndDate.setText("End Date:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 3);
        pnlMain.add(lblLastEndDate, gridBagConstraints);

        txtLastStartDate.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.ipadx = 100;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        pnlMain.add(txtLastStartDate, gridBagConstraints);

        txtLastEndDate.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.ipadx = 100;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        pnlMain.add(txtLastEndDate, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.ipadx = 100;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 3, 0);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        pnlMain.add(txtCurrStartDate, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.ipadx = 100;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 3, 0);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        pnlMain.add(txtCurrEndDate, gridBagConstraints);

        lblCurrStartDate.setFont(CoeusFontFactory.getLabelFont());
        lblCurrStartDate.setText("Start Date:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 3, 3);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        pnlMain.add(lblCurrStartDate, gridBagConstraints);

        lblCurrEndDate.setFont(CoeusFontFactory.getLabelFont());
        lblCurrEndDate.setLabelFor(lblCommitteeName);
        lblCurrEndDate.setText("End Date:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 3, 3);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        pnlMain.add(lblCurrEndDate, gridBagConstraints);

        lblLastDetail.setFont(CoeusFontFactory.getLabelFont());
        lblLastDetail.setText("Previous Run Details:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.ipadx = 250;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 3, 0);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        pnlMain.add(lblLastDetail, gridBagConstraints);

        lblCurrentDetail.setFont(CoeusFontFactory.getLabelFont());
        lblCurrentDetail.setText("Current Run Details:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 250;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 3, 0);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        pnlMain.add(lblCurrentDetail, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.ipadx = 15;
        gridBagConstraints.ipady = 20;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        getContentPane().add(pnlMain, gridBagConstraints);

        pack();
    }//GEN-END:initComponents
    
    /** Exit the Application */
    private void exitForm(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_exitForm
        
    }//GEN-LAST:event_exitForm
    
    public void actionPerformed(java.awt.event.ActionEvent actionEvent) 
    {
         Object source = actionEvent.getSource();
        try{
            if( source.equals ( btnCancel ) ){
                dispose();
            }
            else if( source.equals( btnOk ) ) 
            {
                if (CoeusOptionPane.showQuestionDialog(messageResource.parseMessageKey(
                    "batchReportViewFrm_exceptionCode.1004"), CoeusOptionPane.OPTION_YES_NO,
                    CoeusOptionPane.DEFAULT_YES) == 0)
                {    
                    if (validateData())
                    {   
                        //prps start - added this progress monitor window mar 25 2004
                        if(statusWindow == null) 
                        {
                            statusWindow = new StatusWindow(CoeusGuiConstants.getMDIForm(), true);
                        }
                        statusWindow.setHeader("Generating Batch Correspondence...");
                        statusWindow.setFooter("Please Wait...");
                        statusWindow.display();
        
                        Thread thread = new Thread(new Runnable() {
                                    public void run() 
                                    {
                                        try
                                        {
                                        runBatchGeneration() ;
                                        }
                                        catch(Exception ex)
                                        {
                                            statusWindow.setVisible(false);    
                                            CoeusOptionPane.showErrorDialog(ex.getMessage()) ;
                                        }
                                        statusWindow.setVisible(false);
                                       
                                    }
                                 });
                        thread.start();
                    } // prps end mar 25 2004
                    else
                    {
                        CoeusOptionPane.showErrorDialog("Please enter a valid date in mm/dd/yyyy format.") ;
                    }    
                }   
            }
        }catch(Exception e) {
            e.printStackTrace();
            CoeusOptionPane.showInfoDialog( e.getMessage() );
        }
        
    }    
    
    
    private void controlBtn(boolean control)
    {
        btnOk.setEnabled(control) ;
        btnCancel.setEnabled(control) ;
    }
    
    private void runBatchGeneration() throws Exception
    {
        Vector batchData  = new Vector(3,2);
        controlBtn(false) ;
        RequesterBean request = new RequesterBean();
        request.setFunctionType('X') ;
        BatchCorrespondenceBean batchCorrespondenceBean = new BatchCorrespondenceBean() ;
        batchCorrespondenceBean.setCommitteeId(this.committeeId) ;
        batchCorrespondenceBean.setCorrespondenceBatchTypeCode(this.batchTypeCode) ;
        batchCorrespondenceBean.setTimeWindowStart( new java.sql.Date(dtFormat.parse(
                            dtUtils.restoreDate(txtCurrStartDate.getText(),"/-:,")).getTime())) ;
        batchCorrespondenceBean.setTimeWindowEnd(new java.sql.Date(dtFormat.parse(
                            dtUtils.restoreDate(txtCurrEndDate.getText(),"/-:,")).getTime())) ;
        //prps code start Apr 1 2004
        batchCorrespondenceBean.setFinalActionCorrespondenceType(this.finalActionCorrespondenceType) ;
        batchCorrespondenceBean.setFinalActionTypeCode(this.finalActionTypeCode) ;
        //prps code end Apr 1 2004
        request.setDataObject(batchCorrespondenceBean) ; 
        
        AppletServletCommunicator comm = new AppletServletCommunicator(
                                                    connURL, request );
        comm.send();
        ResponderBean response = comm.getResponse();
               
        if ( response.isSuccessfulResponse() ){
            batchData = (Vector)response.getDataObject();
            if( batchData == null ) 
            { // no protocol expiring in the dates chosen
               controlBtn(true) ;
                throw new Exception(response.getMessage());
            }
            else
            { 
                if (batchData.size() >0 )
                {
                    // batchData is the list of protoocols expiring
                    // use this id to open the screen which follows
                    if (statusWindow != null)
                    {    
                        statusWindow.setVisible(false) ;
                    }    
                    this.batchId = response.getId() ;
                    if (CoeusOptionPane.showQuestionDialog( response.getMessage() 
                        + "\n" + messageResource.parseMessageKey(
                    "batchReportViewFrm_exceptionCode.1005"), CoeusOptionPane.OPTION_YES_NO,
                    CoeusOptionPane.DEFAULT_YES) == 0)
                    {
                        BatchCorrespondenceBean currBatchCorrespondenceBean = new BatchCorrespondenceBean() ;
                        currBatchCorrespondenceBean.setCorrespondenceBatchId(this.batchId) ;
                        currBatchCorrespondenceBean.setCommitteeId(this.committeeId) ;
                        currBatchCorrespondenceBean.setCommitteeName(this.committeeName) ;
                        BatchCorrespondenceDetailForm batchCorrespondenceDetailForm 
                            = new BatchCorrespondenceDetailForm(currBatchCorrespondenceBean) ;
                        batchCorrespondenceDetailForm.showForm() ;
                    }
                    
                     controlBtn(true) ;
                }
               
            }    
        }else{
            
            if(response.getDataObject() != null){
                Object obj = response.getDataObject();
                if( obj  instanceof CoeusException){
                    throw (CoeusException)response.getDataObject();
                }
            }else{
                throw new Exception(response.getMessage());
            }
        }
           
           
    }
    
    
     public void showEndDate() throws java.text.ParseException
    {
            Date apprDate = null;
            String stDate = dtUtils.restoreDate(txtCurrStartDate.getText(),"/:-,");
            apprDate = new java.sql.Date((dtFormat.parse(stDate)).getTime());
            java.util.GregorianCalendar cal = new java.util.GregorianCalendar();
            cal.setTimeInMillis(dtFormat.parse(stDate).getTime());
            // set the time gap appropriately
            cal.set(Calendar.DATE,cal.get(Calendar.DATE)+ this.defaultTimeWindow);
            String expDate = (cal.get(Calendar.MONTH)+1)+"/"
                    +cal.get(Calendar.DATE)+"/"+(cal.get(Calendar.YEAR));
            txtCurrEndDate.setText(dtUtils.formatDate( expDate, "/", "dd-MMM-yyyy"));
      }   
        
    
    
         /**
    * Custom focus adapter which is used for text fields which consists of
    * date values. This is mainly used to format and restore the date value
    * during focus gained / focus lost of the text field.
    */
    private class CustomFocusAdapter extends FocusAdapter{
        //hols the data display Text Field
        CoeusTextField dateField;
        String strDate = "";
        String oldData = "";
        boolean temporary = false;

        public void focusGained (FocusEvent fe){
            if (fe.getSource() instanceof CoeusTextField){
                dateField = (CoeusTextField)fe.getSource();
                if ( (dateField.getText() != null)
                        &&  (!dateField.getText().trim().equals(""))) {
                    oldData = dateField.getText();
                    String focusDate = dtUtils.restoreDate(
                            dateField.getText(),"/-:,");
                    dateField.setText(focusDate);
                }
            }
        }

        public void focusLost (FocusEvent fe){
             if (fe.getSource() instanceof CoeusTextField){
                dateField = (CoeusTextField)fe.getSource();
                
                temporary = fe.isTemporary();
                if ( (dateField.getText() != null)
                        &&  (!dateField.getText().trim().equals(""))
                        && (!temporary) ) {
                    strDate = dateField.getText();
                    String convertedDate =
                            dtUtils.formatDate(dateField.getText(), "/-:," ,
                                    "dd-MMM-yyyy");
                    if (convertedDate==null){
                             if( fe.getSource() == txtCurrStartDate )
                             {    
                                CoeusOptionPane.showErrorDialog("Please enter a valid start date in mm/dd/yyyy format.");
                             }
                             if( fe.getSource() == txtCurrEndDate )
                             {
                                CoeusOptionPane.showErrorDialog("Please enter a valid end date in mm/dd/yyyy format.");
                             }     
                        dateField.setText(oldData);
                        dateField.requestFocusInWindow() ;
                        temporary = true;
                    }else {
                        dateField.setText(convertedDate);
                        temporary = false;
                        // prps commented this - Apr 14 2003
//                        if( fe.getSource() == txtCurrStartDate ) {
//                             try{  
//                                 //showEndDate(); 
//                              }catch(java.text.ParseException PException){ 
//                                CoeusOptionPane.showErrorDialog("Please enter a valid date in mm/dd/yyyy format.");
//                              }
//                        }
                    }
                }
            }
        }
    }    
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnOk;
    private javax.swing.JLabel lblCommitteeId;
    private javax.swing.JLabel lblCommitteeName;
    private javax.swing.JLabel lblCurrEndDate;
    private javax.swing.JLabel lblCurrStartDate;
    private javax.swing.JLabel lblCurrentDetail;
    private javax.swing.JLabel lblLastDetail;
    private javax.swing.JLabel lblLastEndDate;
    private javax.swing.JLabel lblLastRunDate;
    private javax.swing.JLabel lblLastStartDate;
    private javax.swing.JPanel pnlMain;
    private edu.mit.coeus.utils.CoeusTextField txtCommitteeId;
    private edu.mit.coeus.utils.CoeusTextField txtCommitteeName;
    private edu.mit.coeus.utils.CoeusTextField txtCurrEndDate;
    private edu.mit.coeus.utils.CoeusTextField txtCurrStartDate;
    private edu.mit.coeus.utils.CoeusTextField txtLastEndDate;
    private edu.mit.coeus.utils.CoeusTextField txtLastRunDate;
    private edu.mit.coeus.utils.CoeusTextField txtLastStartDate;
    // End of variables declaration//GEN-END:variables
    
}
