/** Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

/*
 * IPReviewDialogController.java
 *
 * Created on May 12, 2004, 3:21 PM
 */
package edu.mit.coeus.instprop.controller;

import  edu.mit.coeus.instprop.controller.IPReviewController;
import  edu.mit.coeus.instprop.gui.*;

import edu.mit.coeus.instprop.bean.*;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.utils.AppletServletCommunicator; 
import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.*;
import edu.mit.coeus.utils.query.*;
import edu.mit.coeus.gui.CoeusDlgWindow;
import edu.mit.coeus.gui.CoeusAppletMDIForm;
import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.gui.CoeusMessageResources;

import java.util.Hashtable;
import java.util.Date;
import java.awt.Component;
import javax.swing.JComponent;
import java.awt.event.*;
import java.sql.Timestamp;
import java.awt.Color;
import javax.swing.AbstractAction;
import java.awt.Dimension;
import java.awt.Toolkit;

/**
 * class which handles tha actions in the IP Review Dialog.
 * This extends the IPReviewController and almost all events it call is super.
 * saving it handles for itself even for setting of data it calls its super method.
 */
public class IPReviewDialogController  extends IPReviewController implements ActionListener {
    private InstituteProposalBaseBean instituteProposalBaseBean;
    private InstituteProposalBean instituteProposalBean;
    private InstituteProposalIPReviewBean instituteProposalIPReviewBean;
    private char functionType ;
    private QueryEngine queryEngine;
    private CoeusDlgWindow dlgIPReview;
    
    private ProposalDetailsForm proposalDetailsForm;
   
    private CoeusAppletMDIForm mdiForm = CoeusGuiConstants.getMDIForm();
    private static final String PROPOSAL_SERVLET = "/InstituteProposalMaintenanceServlet"; 
    private static final String connectTo = CoeusGuiConstants.CONNECTION_URL + PROPOSAL_SERVLET; 
    private static final char GET_IP_DATA='E';
    private static final String EMPTY_STRING="";
    private static final char GET_PROPOSAL_DATA = 'A';
    private static final char UPDATE_IPREVIEW  = 'M';
    private static final String DLG_TITLE = "Proposal IP Review";
    public static final String COULD_NOT_CONTACT_SERVER = "Could not contact server";
    private String proposalNumber;
    private DateUtils dtUtils = new DateUtils();
    private CoeusVector cvProposalData;
    private CoeusVector cvReviewData;
    private CoeusMessageResources coeusMessageResources;
    
    
    /**
     * Creates an instance of IPREviewDialogController
     */    
    public IPReviewDialogController (InstituteProposalBaseBean instituteProposalBaseBean,char functionType) {
        super(instituteProposalBaseBean); 
        this.instituteProposalBaseBean = instituteProposalBaseBean;
        this.functionType=functionType;
        queryEngine = QueryEngine.getInstance();
        coeusMessageResources = CoeusMessageResources.getInstance();
        initComponents();
        setData(instituteProposalBaseBean);
        super.setFunctionType(functionType);
        super.setFormData(instituteProposalBean);
        super.showCommentMissingMessage();
        super.formatFields();
        super.setColumnData();
        postInitComponents();
          
    }
    /**
     * Inialize components
     */    
    public void initComponents() {
        //iPReviewDialogForm=new IPReviewDialogForm();
        proposalDetailsForm=new ProposalDetailsForm(); 
        proposalDetailsForm.btnOK.addActionListener(this);
        proposalDetailsForm.btnCancel.addActionListener(this);
        proposalDetailsForm.txtInitialAmount.setEnabled(false);
        proposalDetailsForm.txtTotalAmount.setEnabled(false);
        proposalDetailsForm.txtArTitle.setEnabled(false);
        proposalDetailsForm.txtInitialAmount.setDisabledTextColor(Color.black);
        proposalDetailsForm.txtTotalAmount.setDisabledTextColor(Color.black);
        proposalDetailsForm.txtArTitle.setDisabledTextColor(Color.black);
    }    
    /**
     * creates the dialog box and displays the form
     */
    private void postInitComponents(){
        Component iPReviewForm=super.getControlledUI();
        dlgIPReview = new CoeusDlgWindow(mdiForm);
        dlgIPReview.getContentPane().setLayout(new java.awt.BorderLayout());
        dlgIPReview.getContentPane().add(proposalDetailsForm,java.awt.BorderLayout.NORTH);
        dlgIPReview.getContentPane().add(iPReviewForm,java.awt.BorderLayout.SOUTH);
        dlgIPReview.setTitle(DLG_TITLE);
        dlgIPReview.setResizable(false);
        dlgIPReview.setFont(CoeusFontFactory.getLabelFont());
        dlgIPReview.setModal(true);
        dlgIPReview.addEscapeKeyListener(
        new AbstractAction("escPressed"){
            public void actionPerformed(ActionEvent ae){
                performCancelAction();
                return;
            }
        });
        dlgIPReview.setDefaultCloseOperation(CoeusDlgWindow.DO_NOTHING_ON_CLOSE);
        dlgIPReview.addWindowListener(new WindowAdapter(){
             public void windowOpened(WindowEvent we) {
                focusInWindow();
            }
            public void windowClosing(WindowEvent we){
                performCancelAction();
                return;
            }
        });
        dlgIPReview.setLocation(CoeusDlgWindow.CENTER);
        dlgIPReview.pack();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension dlgSize = dlgIPReview.getSize();
        dlgIPReview.setLocation(screenSize.width/2 - (dlgSize.width/2),
        screenSize.height/2 - (dlgSize.height/2));
        dlgIPReview.show();
    }
    /**
     *Sets the focus in a default component
     */
    private  void focusInWindow(){
         super.setDialogFocus();
    }
 
    
    /**
     * Handles the actions in the window. 
     * For add and delete buttons it delgates the events to its super's actionHandler.
     * For other actions, it handles itself.
     */    
     public void actionPerformed(ActionEvent ae) {
        super.actionPerformed(ae);
        Object source=ae.getSource();
        
        if (source.equals(proposalDetailsForm.btnOK)) {
            super.saveFormData();
            Hashtable htProposalDetail;
            htProposalDetail = queryEngine.getDataCollection(queryKey);
            //htProposalDetail
            updateProposalData(htProposalDetail);
            dlgIPReview.dispose();
            
        } else if (source.equals(proposalDetailsForm.btnCancel)) {
            performCancelAction();
        }
     }
     
     /**
      * Performs cancel button action
      */     
     public void performCancelAction() {
         
        super.setBeanData();

        if (super.isModified() || super.isTableModified() || super.isCommentModified()) {
            int result=CoeusOptionPane.showQuestionDialog(coeusMessageResources.parseMessageKey("award_exceptionCode.1004"), CoeusOptionPane.OPTION_YES_NO_CANCEL, CoeusOptionPane.DEFAULT_YES);
            if(result==CoeusOptionPane.SELECTION_YES) {
                try{
                    if(!super.validate()) {
                        return ;
                    }
                }catch (edu.mit.coeus.exception.CoeusUIException exception){
                CoeusOptionPane.showDialog(exception);
                }
                super.setAcTypes();
                Hashtable htProposalDetail;
                htProposalDetail = queryEngine.getDataCollection(queryKey);
                updateProposalData(htProposalDetail);
            } else if (result == CoeusOptionPane.SELECTION_CANCEL){
                return;
            }
        } 
        dlgIPReview.dispose();         
     }
    
     /**
      * sets data for the form
      */     
     public void setData(Object instituteProposalBaseBean) {
         this.instituteProposalBaseBean = (InstituteProposalBaseBean)instituteProposalBaseBean;
         proposalNumber = this.instituteProposalBaseBean.getProposalNumber();
         Hashtable htServerData = getDataFromServer();
         cvReviewData = new CoeusVector();
         cvProposalData = new CoeusVector();
         
         cvProposalData = (CoeusVector)htServerData.get(InstituteProposalBean.class);
         cvReviewData = (CoeusVector) htServerData.get(InstituteProposalIPReviewBean.class);
         instituteProposalBean = (InstituteProposalBean)cvProposalData.elementAt(0);
         instituteProposalIPReviewBean  = (InstituteProposalIPReviewBean)cvReviewData.elementAt(0);
         
         prepareQueryKey(instituteProposalBean);
         super.prepareQueryKey(instituteProposalBean);
         queryEngine.addDataCollection(queryKey,htServerData);
         
         proposalDetailsForm.lblProposalNoValue.setText(proposalNumber);
         proposalDetailsForm.lblStatusValue.setText(instituteProposalBean.getStatusDescription());
         proposalDetailsForm.lblAccountValue.setText(instituteProposalIPReviewBean.getCurrentAccountNumber());
         proposalDetailsForm.lblInitialContractAdminValue.setText(instituteProposalIPReviewBean.getPersonFullName());
         proposalDetailsForm.lblSponsorValue.setText(instituteProposalIPReviewBean.getSponsorCode());
         proposalDetailsForm.lblSponsorName.setText(instituteProposalIPReviewBean.getSponsorName());
         proposalDetailsForm.lblSponsorTypeValue.setText(instituteProposalIPReviewBean.getSponsorTypeDescription());
         proposalDetailsForm.lblProposalTypeValue.setText(instituteProposalBean.getProposalTypeDescription());
         proposalDetailsForm.lblLeadUnitValue.setText(instituteProposalIPReviewBean.getUnitNumber());
         proposalDetailsForm.lblLeadUnitName.setText(instituteProposalIPReviewBean.getUnitName());
         proposalDetailsForm.lblPIValue.setText(instituteProposalIPReviewBean.getPersonName());
         proposalDetailsForm.txtArTitle.setText(instituteProposalIPReviewBean.getTitle());
         proposalDetailsForm.txtArTitle.setCaretPosition(0);
         
         if (instituteProposalIPReviewBean.getRequestStartDateInitial()!=null) {
             proposalDetailsForm.lblStartDateValue.setText(dtUtils.formatDate(
                    Utils.convertNull(instituteProposalIPReviewBean.
                        getRequestStartDateInitial().toString()), "dd-MMM-yyyy"));
         }
         else {
             proposalDetailsForm.lblStartDateValue.setText(EMPTY_STRING);
         }
         proposalDetailsForm.txtInitialAmount.setText(""+(instituteProposalIPReviewBean.getTotalDirectCostInitial()+instituteProposalIPReviewBean.getTotalInDirectCostInitial()));
         if (instituteProposalIPReviewBean.getRequestEndDateTotal()!=null) {
             proposalDetailsForm.lblEndDateValue.setText(dtUtils.formatDate(
                    Utils.convertNull(instituteProposalIPReviewBean.
                        getRequestEndDateTotal().toString()), "dd-MMM-yyyy"));
         } else {
             proposalDetailsForm.lblEndDateValue.setText(EMPTY_STRING);
         }
         proposalDetailsForm.txtTotalAmount.setText(""+
         (instituteProposalIPReviewBean.getTotalDirectCostTotal()+
         instituteProposalIPReviewBean.getTotalInDirectCostTotal()));
         
         if(instituteProposalIPReviewBean.getCreateTimeStamp()!= null){
            proposalDetailsForm.lblPropCreateDateValue.setText(
            dtUtils.formatDate(instituteProposalIPReviewBean.getCreateTimeStamp().toString(), "dd-MMM-yyyy"));
         }
         
         if(instituteProposalIPReviewBean.getUpdateTimestamp()!= null){
            proposalDetailsForm.lblLastModifiedDateValue.setText(
            dtUtils.formatDate(instituteProposalIPReviewBean.getUpdateTimestamp().toString(), "dd-MMM-yyyy"));
         }

     }
     /**
      * Gets the data from the server. The server call is made here.
      */
     private Hashtable getDataFromServer() {
        Hashtable htProposalData=null;
        RequesterBean requesterBean = new RequesterBean();
        ResponderBean responderBean = new ResponderBean();
        requesterBean.setFunctionType(GET_IP_DATA);
        requesterBean.setDataObject(proposalNumber);
        AppletServletCommunicator comm = new AppletServletCommunicator(connectTo, requesterBean);
        comm.setRequest(requesterBean);
        comm.send();
        responderBean = comm.getResponse();
        if(responderBean.isSuccessfulResponse()) {
            htProposalData = (Hashtable)responderBean.getDataObject();
         }else{
             CoeusOptionPane.showErrorDialog(responderBean.getMessage());
         }
        return htProposalData;
    }
    /**
     * Updation of the data is done here. For setting the data it calls its super
     * methods
     */
    private void updateProposalData(Hashtable proposalData) {
        RequesterBean requesterBean = new RequesterBean();
        requesterBean.setFunctionType(UPDATE_IPREVIEW);
        requesterBean.setDataObject(proposalData);
        AppletServletCommunicator appletServletCommunicator = new AppletServletCommunicator(connectTo, requesterBean);
        appletServletCommunicator.send();
        ResponderBean responderBean = appletServletCommunicator.getResponse();
        
        if(!responderBean.isSuccessfulResponse()) {
            CoeusOptionPane.showErrorDialog(responderBean.getMessage());
            return ;
        }
    }
}
