/*
 * PrintDeltaReportForm.java
 *
 * Created on September 17, 2004, 12:03 PM
 * @author  bijosht
 */

/**
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */
package edu.mit.coeus.award.gui;

import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.gui.CoeusDlgWindow;
import edu.mit.coeus.gui.URLOpener;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.award.bean.AwardBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.utils.AppletServletCommunicator;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.exception.CoeusClientException;
import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.award.bean.AwardAmountInfoBean;

import java.awt.event.ActionListener;
import java.util.Hashtable;
import javax.swing.SpinnerNumberModel;
import java.applet.AppletContext;
import java.net.URL;
import java.awt.Cursor;
import javax.swing.event.ChangeListener;

public class PrintDeltaReportForm extends javax.swing.JPanel
implements ActionListener,ChangeListener{
    
    private static final String DLG_TITLE = "Print Delta Report";
    private CoeusDlgWindow dlgPrintReport;
    private AwardBean awardBean = null;
    private static final String SERVLET = "/AwardMaintenanceServlet";
    private static final String PRINT_SERVLET = "/ReportConfigServlet";
    
    private static final String COULD_NOT_CONTACT_SERVER = "Could not contact server";
    private static final char DELTA_REPORT = 'd';
    private static final char SIGNATURE_REQUIRED_REQUEST = 'e';
    private static final char GET_MAX_ACC_SEQ_NUM= 'm';
    
    private boolean isSignatureRequired = false;
    private int maxAccNumber;
    private int amountSequenceNumber;
    private int selectedSeqNumber;
    private boolean isSignatureReq = false;
    
    /** Creates new form PrintDeltaReportForm */
    public PrintDeltaReportForm() {
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
            return;
        }
        setSequenceNumbers();
        dlgPrintReport = new CoeusDlgWindow(CoeusGuiConstants.getMDIForm(), true);
        dlgPrintReport.setTitle(DLG_TITLE);
        dlgPrintReport.getContentPane().add(this);
        dlgPrintReport.setSize(370, 160);
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
        requesterBean.setDataObject(htDataToSend);
        requesterBean.setId("Award/DeltaReport");
        requesterBean.setFunctionType('R');
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
        String url = (String)responderBean.getDataObject();
        url = url.replace('\\', '/');
        AppletContext coeusContext = CoeusGuiConstants.getMDIForm().getCoeusAppletContext();
        try{
//            URL templateURL = new URL(CoeusGuiConstants.CONNECTION_URL + url);
//            if(coeusContext != null){
//                coeusContext.showDocument( templateURL, "_blank" );
//            }else{
//                javax.jnlp.BasicService bs = (javax.jnlp.BasicService)javax.jnlp.ServiceManager.lookup("javax.jnlp.BasicService");
//                bs.showDocument(templateURL);
//            }
            URL urlObj = new URL(url);
            URLOpener.openUrl(urlObj);
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            CoeusOptionPane.showErrorDialog(""+ex.getMessage());
            return false;
        }
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
    
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
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
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(7, 9, 0, 4);
        add(lblSequenceNumber, gridBagConstraints);

        lblAmtSeqNo.setFont(CoeusFontFactory.getLabelFont());
        lblAmtSeqNo.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblAmtSeqNo.setText("Amt Sequence No.:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        add(lblAmtSeqNo, gridBagConstraints);

        spnrSequenceNo.setFont(CoeusFontFactory.getNormalFont());
        spnrSequenceNo.setMinimumSize(new java.awt.Dimension(40, 22));
        spnrSequenceNo.setPreferredSize(new java.awt.Dimension(85, 22));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(7, 0, 0, 0);
        add(spnrSequenceNo, gridBagConstraints);

        spnrAmtSequnceNo.setFont(CoeusFontFactory.getNormalFont());
        spnrAmtSequnceNo.setMinimumSize(new java.awt.Dimension(40, 22));
        spnrAmtSequnceNo.setPreferredSize(new java.awt.Dimension(85, 22));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        add(spnrAmtSequnceNo, gridBagConstraints);

        chkSignatureReq.setFont(CoeusFontFactory.getLabelFont());
        chkSignatureReq.setText("Signature Required");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(7, 11, 0, 0);
        add(chkSignatureReq, gridBagConstraints);

        btnPrint.setFont(CoeusFontFactory.getLabelFont());
        btnPrint.setMnemonic('P');
        btnPrint.setText("Print");
        btnPrint.setMinimumSize(new java.awt.Dimension(55, 25));
        btnPrint.setPreferredSize(new java.awt.Dimension(75, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(7, 25, 0, 5);
        add(btnPrint, gridBagConstraints);

        btnClose.setFont(CoeusFontFactory.getLabelFont());
        btnClose.setMnemonic('C');
        btnClose.setText("Close");
        btnClose.setMinimumSize(new java.awt.Dimension(59, 25));
        btnClose.setPreferredSize(new java.awt.Dimension(75, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(6, 25, 0, 5);
        add(btnClose, gridBagConstraints);

    }//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JButton btnClose;
    public javax.swing.JButton btnPrint;
    public javax.swing.JCheckBox chkSignatureReq;
    public javax.swing.JLabel lblAmtSeqNo;
    public javax.swing.JLabel lblSequenceNumber;
    public javax.swing.JSpinner spnrAmtSequnceNo;
    public javax.swing.JSpinner spnrSequenceNo;
    // End of variables declaration//GEN-END:variables
    
}
