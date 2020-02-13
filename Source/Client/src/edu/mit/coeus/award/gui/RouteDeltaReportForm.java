/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * RouteDeltaReportForm.java
 *
 * Created on Dec 14, 2011, 11:40:53 AM
 */
package edu.mit.coeus.award.gui;

import edu.mit.coeus.award.bean.AwardBean;
import edu.mit.coeus.award.bean.AwardDocumentRouteBean;
import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.exception.CoeusClientException;
import edu.mit.coeus.gui.CoeusDlgWindow;
import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.routing.gui.RoutingForm;
import edu.mit.coeus.utils.AppletServletCommunicator;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.ModuleConstants;
import java.awt.Cursor;
import java.awt.event.ActionListener;
import java.util.Hashtable;
import java.util.Vector;
import javax.swing.JOptionPane;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeListener;

/**
 *
 * @author midhunmk
 */
public class RouteDeltaReportForm extends javax.swing.JPanel implements ActionListener,ChangeListener{
    
    private static final String DLG_TITLE = "Route Delta Report";
    private CoeusDlgWindow dlgPrintReport;
    private AwardBean awardBean = null;
    private static final String SERVLET = "/AwardMaintenanceServlet";
    private static final String PRINT_SERVLET = "/ReportConfigServlet";
    
    private static final String COULD_NOT_CONTACT_SERVER = "Could not contact server";
    private static final char DELTA_REPORT = 'd';
    private static final char SIGNATURE_REQUIRED_REQUEST = 'e';
    private static final char GET_MAX_ACC_SEQ_NUM= 'm';
    private static final int AWARD_ROUTING_STATUS_CODE=1;
    
    private boolean isSignatureRequired = false;
    private int maxAccNumber;
    private int amountSequenceNumber;
    private int selectedSeqNumber;
    private boolean isSignatureReq = false;
    
    private static final int AWARD_DELTA_CODE=2;
    private AwardDocumentRouteBean awardDocumentRouteBean;
    private static final String SUBMISSION_SERVLET="/AwardSubmissionServlet";
    private static final char SUBMIT_FOR_APPROVE='A';
    private static final char REMOVE_AWARD_DOCUMENT='G';
    private static final String AWARD_DOCUMENT_BEAN="AWARD_DOCUMENT_BEAN";
    private CoeusMessageResources coeusMessageResources;
    private static final String DELTA_REPORT_MSG = "award.deltaReport_exceptionCode.1002";
    
    
    /** Creates new form RouteDeltaReportForm */
    public RouteDeltaReportForm() {
        coeusMessageResources = CoeusMessageResources.getInstance();
        initComponents();
     registerComponents();
    }
    
    private void registerComponents() {
        btnPrint.addActionListener(this);
        btnClose.addActionListener(this);
        spnrSequenceNo.addChangeListener(this);
    }
    /**
     * Displays the window
     */
    public void display() {
        if (awardBean.getSequenceNumber()==1){
            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(DELTA_REPORT_MSG));
            return;
        }
        setSequenceNumbers();
        dlgPrintReport = new CoeusDlgWindow(CoeusGuiConstants.getMDIForm(), true);
        dlgPrintReport.setTitle(DLG_TITLE);
        dlgPrintReport.getContentPane().add(this);
        dlgPrintReport.setSize(430, 160);
        dlgPrintReport.setResizable(false);
        dlgPrintReport.setLocation(CoeusDlgWindow.CENTER);
        dlgPrintReport.setVisible(true);
    }
    
    
    /**
     * Sets the models for JSpinners
     */
    private void setSequenceNumbers() {
        int initVal = awardBean.getSequenceNumber();
        int minVal = 2;
        int maxVal = awardBean.getSequenceNumber();
        int step = 1;
        SpinnerNumberModel spinnerNumberModel = new SpinnerNumberModel(initVal,minVal,maxVal,step);
        spnrSequenceNo.setModel(spinnerNumberModel);
        try {
            getInitialData();
        } catch (CoeusClientException cce) {
            cce.printStackTrace();
            CoeusOptionPane.showDialog(cce);
        }
        initVal = maxAccNumber;
        minVal = 1;
        maxVal = maxAccNumber;
        step = 1;
        spinnerNumberModel = new SpinnerNumberModel(initVal,minVal,maxVal,step);
        spnrAmtSequnceNo.setModel(spinnerNumberModel);
    }
    
    private void getInitialData() throws CoeusClientException {
        RequesterBean requesterBean = new RequesterBean();
        requesterBean.setFunctionType(SIGNATURE_REQUIRED_REQUEST);
        requesterBean.setDataObject(awardBean);
        AppletServletCommunicator appletServletCommunicator = new AppletServletCommunicator(CoeusGuiConstants.CONNECTION_URL + SERVLET, requesterBean);
        appletServletCommunicator.setRequest(requesterBean);
        appletServletCommunicator.send();
        ResponderBean responderBean = appletServletCommunicator.getResponse();
        if(responderBean == null) {
            CoeusClientException coeusClientException = new CoeusClientException(COULD_NOT_CONTACT_SERVER,CoeusClientException.ERROR_MESSAGE);
            throw coeusClientException;
        }
        if (!responderBean.isSuccessfulResponse()) {
            CoeusClientException coeusClientException = new CoeusClientException(responderBean.getMessage(),CoeusClientException.ERROR_MESSAGE);
            throw coeusClientException;
        }
        CoeusVector cvResponse = (CoeusVector)responderBean.getDataObject();
        Integer signatureRequired = (Integer)cvResponse.get(0);
        if (signatureRequired.intValue()==0) {
            isSignatureRequired = false;
        } else {
            isSignatureRequired = true;
        }
        Integer accNumberInteger = (Integer)cvResponse.get(1);
        maxAccNumber = accNumberInteger.intValue();
    }
    
    private boolean generateReport() {
        String userMessage="Are you sure you would like to submit delta report for routing?";
        edu.mit.coeus.award.bean.AwardDocumentRouteBean adrb = awardBean.getLatestAwardDocumentRouteBean();
        if((adrb!=null)&&(adrb.getRoutingDocumentNumber()>0)&&(adrb.getRoutingStatusCode()==1)){
        userMessage="Another document approval is already in progress . Do you want to submit new one?";
        }
        int confirm=CoeusOptionPane.showQuestionDialog(userMessage+"     ",CoeusOptionPane.OPTION_YES_NO,CoeusOptionPane.DEFAULT_YES);
        if(confirm==JOptionPane.YES_OPTION){
        Boolean boolSignatureReq = new Boolean(isSignatureReq);
        RequesterBean requesterBean = new RequesterBean();
        requesterBean.setFunctionType(DELTA_REPORT);
        CoeusVector cvDataToServer = new CoeusVector();
        cvDataToServer.add(awardBean); //0
        cvDataToServer.add(new Integer(selectedSeqNumber)); //1
        cvDataToServer.add(new Integer(amountSequenceNumber)); //2
        cvDataToServer.add(boolSignatureReq); //3
        
        //requesterBean.setDataObject(cvDataToServer);
        
        //For Streaming
        Hashtable htDataToSend = new Hashtable();
        htDataToSend.put("LOGGED_IN_USER", CoeusGuiConstants.getMDIForm().getUserId());
        htDataToSend.put("REPORT_TYPE","DeltaReport");
        htDataToSend.put("DATA", cvDataToServer);
        htDataToSend.put(AWARD_DOCUMENT_BEAN,LoadAwardDocumentBean());
        requesterBean.setDataObject(htDataToSend);
        requesterBean.setId("Award/DeltaReport");
        requesterBean.setFunctionType('A');
        //For Streaming
        
        AppletServletCommunicator appletServletCommunicator = new AppletServletCommunicator(CoeusGuiConstants.CONNECTION_URL + PRINT_SERVLET, requesterBean);
        appletServletCommunicator.setRequest(requesterBean);
        appletServletCommunicator.send();
        ResponderBean responderBean = appletServletCommunicator.getResponse();
        if(responderBean == null) {
            //Could not contact server.
            CoeusOptionPane.showErrorDialog(COULD_NOT_CONTACT_SERVER);
            return false;
        }
        if (!responderBean.isSuccessfulResponse()) {
            CoeusOptionPane.showErrorDialog(responderBean.getMessage());
            return false;
        }
       //loaded the document to the data table
        awardDocumentRouteBean=(AwardDocumentRouteBean)responderBean.getDataObject();
      
        //checking for the presence of map.
         boolean mapsFound=false;
         mapsFound = doSubmitApproveAction();
         if(mapsFound){
             //setting the base bean with the details
        awardDocumentRouteBean.setRoutingStatusCode(AWARD_ROUTING_STATUS_CODE);
        awardDocumentRouteBean.setRoutingApprovalSeq(awardDocumentRouteBean.getRoutingDocumentNumber());
        awardBean.setLatestAwardDocumentRouteBean(awardDocumentRouteBean);
                    CoeusOptionPane.showInfoDialog("Delta Report submitted for approval routing.     ") ;
                     RoutingForm routingForm = new RoutingForm(
                                    awardDocumentRouteBean,
                                    ModuleConstants.AWARD_MODULE_CODE,awardDocumentRouteBean.getMitAwardNumber(),
                                    awardDocumentRouteBean.getRoutingDocumentNumber(),
                                    awardBean.getLeadUnitNumber(), false,true);
                     routingForm.display();
                     
                     
                }
         else{
             removeAwardDocumentBean();
             CoeusOptionPane.showInfoDialog("The Delta report does not have any routing map defined.     ");
             
         }
        
         
    }
    return true;
    }
    public void actionPerformed(java.awt.event.ActionEvent ae) {
        Object src = ae.getSource();
        try {
            dlgPrintReport.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            if (src.equals(btnClose)) {
                dlgPrintReport.dispose();
            } else if (src.equals(btnPrint)) {
                amountSequenceNumber = ((Integer)spnrAmtSequnceNo.getValue()).intValue();
                selectedSeqNumber = ((Integer)spnrSequenceNo.getValue()).intValue();
                isSignatureReq = chkSignatureReq.isSelected();
                generateReport();
                dlgPrintReport.dispose();
            }
        } finally {
            dlgPrintReport.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }
    }
    
    public void setAward(AwardBean awardBean) {
        this.awardBean = awardBean;
    }
    /*
     * Makes a server call to get the maximum Amt sequence number for the
     * selected sequence number
     */
    private int getMaxAccSeqNum(int currentSeqNum)
    throws CoeusClientException {
        RequesterBean requesterBean = new RequesterBean();
        requesterBean.setFunctionType(GET_MAX_ACC_SEQ_NUM);
        AwardBean currentBean = new AwardBean();
        currentBean.setMitAwardNumber(awardBean.getMitAwardNumber());
        currentBean.setSequenceNumber(currentSeqNum);
        requesterBean.setDataObject(currentBean);
        AppletServletCommunicator appletServletCommunicator = new AppletServletCommunicator(CoeusGuiConstants.CONNECTION_URL + SERVLET, requesterBean);
        appletServletCommunicator.setRequest(requesterBean);
        appletServletCommunicator.send();
        ResponderBean responderBean = appletServletCommunicator.getResponse();
        if(responderBean == null) {
            CoeusClientException coeusClientException = new CoeusClientException(COULD_NOT_CONTACT_SERVER,CoeusClientException.ERROR_MESSAGE);
            throw coeusClientException;
        }
        if (!responderBean.isSuccessfulResponse()) {
            CoeusClientException coeusClientException = new CoeusClientException(responderBean.getMessage(),CoeusClientException.ERROR_MESSAGE);
            throw coeusClientException;
        }
        Integer response = (Integer)responderBean.getDataObject();
        int currentMaxSeq = response.intValue();
        return currentMaxSeq;
    }
    /** 
     * For sequence number JSpinner
     * Which changes the value for the Amt sequence number according to the sequence number selected
     */
    public void stateChanged(javax.swing.event.ChangeEvent changeEvent) {
        Object source = changeEvent.getSource();
        if(source.equals(spnrSequenceNo)){
            try{
                dlgPrintReport.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                Integer currSeqNum = (Integer)spnrSequenceNo.getValue();
                int maxAccSeqNum = getMaxAccSeqNum(currSeqNum.intValue());
                //int initVal = maxAccNumber;
                /* Bug Fix:1272 , to set the maximum amount seq number to the amount seq number field
                 on the sequence number change */
                int initVal = maxAccSeqNum;
                
                int minVal = 1;
                int maxVal = maxAccSeqNum;
                int step = 1;
                SpinnerNumberModel currentSpinnerNumberModel = new SpinnerNumberModel(initVal,minVal,maxVal,step);
                spnrAmtSequnceNo.setModel(currentSpinnerNumberModel);
                spnrAmtSequnceNo.setValue(new Integer(maxVal));
            } catch(Exception ex) {
                ex.printStackTrace();
                CoeusOptionPane.showErrorDialog(""+ex.getMessage());
            }
            finally {
                dlgPrintReport.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            }
        }
    }
    //COEUSQA 2111 STARTS
   private AwardDocumentRouteBean LoadAwardDocumentBean()
   {
      awardDocumentRouteBean=new AwardDocumentRouteBean();
      awardDocumentRouteBean.setMitAwardNumber(awardBean.getMitAwardNumber());
      awardDocumentRouteBean.setSequenceNumber(awardBean.getSequenceNumber());
      awardDocumentRouteBean.setRoutingDocumentNumber(-1);
      awardDocumentRouteBean.setDocumentData(null);
      awardDocumentRouteBean.setDocumentTypeCode(AWARD_DELTA_CODE);
      awardDocumentRouteBean.setRoutingStatusCode(-1);
      if(chkSignatureReq.isSelected()){awardDocumentRouteBean.setSignatureFlag("Y");}
      else                            {awardDocumentRouteBean.setSignatureFlag("N");}
      //awardDocumentRouteBean.setSignatureFlag((isSignatureRequired)?"N":"Y");
      return awardDocumentRouteBean;
   }
   
   private boolean doSubmitApproveAction() {
        
        try {
            
        Vector requestParameters = new Vector();
        //Passes Protocol Number, unit number,"S" submit for approve option
        //requestParameters.add(String.valueOf(ModuleConstants.AWARD_MODULE_CODE));
        requestParameters.add(awardDocumentRouteBean.getMitAwardNumber());
        requestParameters.add(Integer.parseInt((String.valueOf(awardDocumentRouteBean.getRoutingDocumentNumber()))));
        requestParameters.add(awardBean.getLeadUnitNumber());
       //requestParameters.add(true);
        requestParameters.add("S");
       // requestParameters.add(null);
        RequesterBean requesterBean = new RequesterBean();
        requesterBean.setFunctionType(SUBMIT_FOR_APPROVE);
        requesterBean.setDataObjects(requestParameters);
        String connectTo = CoeusGuiConstants.CONNECTION_URL
                + SUBMISSION_SERVLET;
        AppletServletCommunicator comm = new AppletServletCommunicator(connectTo, requesterBean);
        comm.send();
        ResponderBean response = comm.getResponse();
        Vector submitApproveResultSetData = null;
        if(response.isSuccessfulResponse() && response.getDataObjects() != null){
            submitApproveResultSetData = response.getDataObjects();
            if(submitApproveResultSetData != null && submitApproveResultSetData.size() > 0) {
                if(submitApproveResultSetData.get(0) != null) {
                        // returns Integer representing Action on server end
                        if(((Integer) submitApproveResultSetData.get(0)).intValue() > 0) {
                            return true;
                        }//COEUSQA-2554_Parent key not found error while submitting protocol for Routing_Start
                        else if(((Integer) submitApproveResultSetData.get(0)).intValue() ==  0) {
                            //errDisplay = true;
                        }
                        //COEUSQA-2554_Parent key not found error while submitting protocol for Routing_end
                }
            }
        } else if(!response.isSuccessfulResponse()){
            CoeusOptionPane.showErrorDialog(response.getMessage());
            //invalidResponse = true;
        }
        }catch(Exception expec) {
            expec.printStackTrace();
        }
          return false;
    }
   private boolean removeAwardDocumentBean(){
       RequesterBean requesterBean = new RequesterBean();
        requesterBean.setFunctionType(REMOVE_AWARD_DOCUMENT);
        requesterBean.setDataObject(awardDocumentRouteBean);
        String connectTo = CoeusGuiConstants.CONNECTION_URL
                + SUBMISSION_SERVLET;
        AppletServletCommunicator comm = new AppletServletCommunicator(connectTo, requesterBean);
        comm.send();
        ResponderBean response = comm.getResponse();
        if(!response.isSuccessfulResponse()){
        CoeusOptionPane.showErrorDialog(response.getMessage());
        return false;
        }
       return true;
   }
//COEUSQA 2111 ENDS
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        lblSequenceNumber = new javax.swing.JLabel();
        lblAmtSeqNo = new javax.swing.JLabel();
        spnrSequenceNo = new javax.swing.JSpinner();
        spnrAmtSequnceNo = new javax.swing.JSpinner();
        chkSignatureReq = new javax.swing.JCheckBox();
        btnPrint = new javax.swing.JButton();
        btnClose = new javax.swing.JButton();

        setLayout(new java.awt.GridBagLayout());

        lblSequenceNumber.setFont(CoeusFontFactory.getLabelFont());
        lblSequenceNumber.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblSequenceNumber.setText("Sequence No.:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.ipadx = 44;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(114, 24, 0, 0);
        add(lblSequenceNumber, gridBagConstraints);

        lblAmtSeqNo.setFont(CoeusFontFactory.getLabelFont());
        lblAmtSeqNo.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblAmtSeqNo.setText("Amt Sequence No.:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.ipadx = 30;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(10, 15, 0, 0);
        add(lblAmtSeqNo, gridBagConstraints);

        spnrSequenceNo.setFont(CoeusFontFactory.getNormalFont());
        spnrSequenceNo.setMinimumSize(new java.awt.Dimension(40, 22));
        spnrSequenceNo.setPreferredSize(new java.awt.Dimension(85, 22));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = 45;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(110, 0, 0, 0);
        add(spnrSequenceNo, gridBagConstraints);

        spnrAmtSequnceNo.setFont(CoeusFontFactory.getNormalFont());
        spnrAmtSequnceNo.setMinimumSize(new java.awt.Dimension(40, 22));
        spnrAmtSequnceNo.setPreferredSize(new java.awt.Dimension(85, 22));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = 45;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(6, 0, 0, 0);
        add(spnrAmtSequnceNo, gridBagConstraints);

        chkSignatureReq.setFont(CoeusFontFactory.getLabelFont());
        chkSignatureReq.setText("Signature Required");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(4, 24, 110, 0);
        add(chkSignatureReq, gridBagConstraints);

        btnPrint.setFont(CoeusFontFactory.getLabelFont());
        btnPrint.setText("Route Document");
        btnPrint.setMaximumSize(new java.awt.Dimension(105, 23));
        btnPrint.setMinimumSize(new java.awt.Dimension(105, 23));
        btnPrint.setPreferredSize(new java.awt.Dimension(105, 23));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = 25;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(109, 52, 0, 37);
        add(btnPrint, gridBagConstraints);

        btnClose.setFont(CoeusFontFactory.getLabelFont());
        btnClose.setMnemonic('C');
        btnClose.setText("Close");
        btnClose.setMaximumSize(new java.awt.Dimension(105, 23));
        btnClose.setMinimumSize(new java.awt.Dimension(105, 23));
        btnClose.setPreferredSize(new java.awt.Dimension(105, 23));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.ipadx = 25;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(8, 52, 0, 37);
        add(btnClose, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnClose;
    private javax.swing.JButton btnPrint;
    private javax.swing.JCheckBox chkSignatureReq;
    private javax.swing.JLabel lblAmtSeqNo;
    private javax.swing.JLabel lblSequenceNumber;
    private javax.swing.JSpinner spnrAmtSequnceNo;
    private javax.swing.JSpinner spnrSequenceNo;
    // End of variables declaration//GEN-END:variables
}
