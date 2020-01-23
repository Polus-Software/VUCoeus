/*
 * AwardTransactionDetailsController.java
 *
 * Created on June 3, 2004, 3:40 PM
 * @author  bijosht
 */

/**
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.award.controller;

import edu.mit.coeus.award.bean.AwardAmountInfoBean;
import edu.mit.coeus.award.bean.AwardAmountTransactionBean;
import edu.mit.coeus.award.controller.AwardController;
import edu.mit.coeus.award.gui.AwardAmountTreeTable;
import edu.mit.coeus.award.gui.AwardTransactionDetailsForm;
import edu.mit.coeus.award.gui.AwardGoToForm;
import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.gui.CoeusDlgWindow;
import edu.mit.coeus.gui.event.BeanEvent;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.utils.*;
import edu.mit.coeus.gui.*;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.Toolkit;
import java.util.Vector;
import javax.swing.AbstractAction;

import java.net.MalformedURLException;
import java.net.URL;
import java.applet.AppletContext;
import java.util.Hashtable;

/**
 * This class is controller class which is used to handle
 * the transaction details display of awards.
 */
public class AwardTransactionDetailsController extends AwardController
implements ActionListener{
    private AwardTransactionDetailsForm awardTransactionDetailsForm;
    private CoeusDlgWindow dlgAwardTransaction, dlgParent;
    
    private static final String AWARD_SERVLET = "/AwardMaintenanceServlet";
    private static final String connectTo = CoeusGuiConstants.CONNECTION_URL +
    AWARD_SERVLET;
    private static final String PRINT_SERVLET = CoeusGuiConstants.CONNECTION_URL +"/ReportConfigServlet";
    private static final char GET_TRANSACTION_DATA = 'L';
    
    private static final String DIALOG_TITLE = "Transaction Detail for Award - ";
    private static final String NO_AWARD_AMOUNT = "awardMoneyAndEndDates_exceptionCode.1173";
    private CoeusVector cvTransactions;
    private CoeusVector cvHistoryData;
    private AwardAmountInfoBean awardAmountInfoBean;
    private String awardNumber;
    private int selHistoryIndex;
    
    private static final char PRINT_BUDGET_MODIFICATION = 'f';
    
    private CoeusMessageResources coeusMessageResources;
    //Added for Case 2269: Money & End Dates Tab/Panel in Awards Module - start
    private AwardAmountTransactionBean awardAmountTransactionBean;
    private DateUtils dateUtils;
    //Added for Case 2269: Money & End Dates Tab/Panel in Awards Module - end
    
    /** Creates a new instance of AwardTransactionDetailsController */
    public AwardTransactionDetailsController(CoeusDlgWindow parent) {
        dlgParent = parent;
        initComponents();
        registerComponents();
        postInitComponents();
    }
    /**
     * Creates instances of the gui.
     */
    public void initComponents() {
        awardTransactionDetailsForm = new AwardTransactionDetailsForm();
        cvTransactions=new CoeusVector();
        //Added for Case 2269: Money & End Dates Tab/Panel in Awards Module - start
        dateUtils = new DateUtils();
        //Added for Case 2269: Money & End Dates Tab/Panel in Awards Module - end
    }
    /**
     * Register listeners to the components
     */
    public void registerComponents() {
        awardTransactionDetailsForm.btnClose.addActionListener(this);
        awardTransactionDetailsForm.btnExpandAll.addActionListener(this);
        awardTransactionDetailsForm.btnGoto.addActionListener(this);
        awardTransactionDetailsForm.btnPrev.addActionListener(this);
        awardTransactionDetailsForm.btnNext.addActionListener(this);
        awardTransactionDetailsForm.btnPrint.addActionListener(this);
    }
    
    /**
     * Creates a dialog instance and adds the forms to the dialog.
     */
    public void postInitComponents() {
        dlgAwardTransaction = new CoeusDlgWindow(dlgParent);
        dlgAwardTransaction.getContentPane().add(awardTransactionDetailsForm);
        dlgAwardTransaction.setResizable(false);
        dlgAwardTransaction.setModal(true);
        dlgAwardTransaction.addEscapeKeyListener(
        new AbstractAction("escPressed"){
            public void actionPerformed(ActionEvent ae){
                dlgAwardTransaction.dispose();
                return;
            }
        });
        dlgAwardTransaction.setDefaultCloseOperation(CoeusDlgWindow.DO_NOTHING_ON_CLOSE);
        dlgAwardTransaction.addWindowListener(new WindowAdapter(){
            public void windowOpened(WindowEvent we) {
                setDefaultFocus();
            }
            public void windowClosing(WindowEvent we){
                dlgAwardTransaction.dispose();
                return;
            }
        });
    }
    
    /**
     * sets the focus to the default component on the opening of the screen
     */
    private void setDefaultFocus() {
        awardTransactionDetailsForm.btnClose.requestFocusInWindow();
    }
    
    /**
     * Displays the dialog and sets the display properties
     */
    public void display() {
        dlgAwardTransaction.pack();
        //dlgAwardTransaction.setSize(850, 450);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension dlgSize = dlgAwardTransaction.getSize();
        dlgAwardTransaction.setLocation(screenSize.width/2 - (dlgSize.width/2),
        screenSize.height/2 - (dlgSize.height/2));
        dlgAwardTransaction.setVisible(true);
    }
    
    /**
     * No implementaion required.
     */
    public void formatFields() {
    }
    
    public java.awt.Component getControlledUI() {
        return null;
    }
    
    public Object getFormData() {
        return null;
    }
    /**
     * Removes the references of the class variables
     */
    public void cleanUp() {
        awardTransactionDetailsForm = null;
        dlgAwardTransaction = null;
        dlgParent = null;
        cvTransactions = null;
        cvHistoryData = null;
        awardAmountInfoBean = null;
        awardNumber = null;
    }
    
    /**
     * No implementation required as the screen is always read only.
     */
    public void saveFormData() {
    }
    
    public void setFormData(Object data) {
    }
    /**
     * Sets the data.
     */
    public void setData(CoeusVector cvData,int selectedIndex) {
        cvHistoryData =cvData;
        selHistoryIndex =selectedIndex;
        awardAmountInfoBean = (AwardAmountInfoBean)cvHistoryData.get(selHistoryIndex);
        if (selHistoryIndex<1){
            awardTransactionDetailsForm.btnPrev.setEnabled(false);
        } else {
            awardTransactionDetailsForm.btnPrev.setEnabled(true);
        }
        
        if (selHistoryIndex==cvHistoryData.size()-1){
            awardTransactionDetailsForm.btnNext.setEnabled(false);
        } else {
            awardTransactionDetailsForm.btnNext.setEnabled(true);
        }
        dlgAwardTransaction.setTitle(DIALOG_TITLE + awardAmountInfoBean.getMitAwardNumber() + " Transaction - "+awardAmountInfoBean.getTransactionId());
        String awrdNum = awardAmountInfoBean.getMitAwardNumber();
        awardAmountInfoBean.setMitAwardNumber(createRootAwardNumber(awrdNum));
        getDataFromServer();
        if(cvTransactions.size()>0) {
            
            awardTransactionDetailsForm.awardAmountTreeTable.setBeans(cvTransactions,
            (AwardAmountInfoBean)cvTransactions.get(0));
            awardTransactionDetailsForm.awardAmountTreeTable.setAwardColor(awrdNum, java.awt.Color.blue);
            awardTransactionDetailsForm.awardAmountTreeTable.setEditable(false);
            awardTransactionDetailsForm.awardAmountTreeTable.setSelectionEnabled(false);
         
           // #3857 -- start
            if(AwardAmountTreeTable.SET_DITRECT_INDIRECT && awardTransactionDetailsForm.awardAmountTreeTable.btnDirectInDirect.isSelected())
                awardTransactionDetailsForm.awardAmountTreeTable.btnDirectInDirect.doClick();
          //#3857 -- end
          
            awardTransactionDetailsForm.awardAmountTreeTable.btnTotal.doClick();
        }
        
        
        //Added for Case 2269: Money & End Dates Tab/Panel in Awards Module - start
        if(awardAmountTransactionBean != null){
            awardTransactionDetailsForm.lblTransactionType.setText(awardAmountTransactionBean.getTransactionTypeDescription());
            awardTransactionDetailsForm.txtArComments.setText(awardAmountTransactionBean.getComments());
            if(awardAmountTransactionBean.getNoticeDate() != null){
                awardTransactionDetailsForm.txtNoticeDate.setText(dateUtils.formatDate(
                                 awardAmountTransactionBean.getNoticeDate().toString(),"dd-MMM-yyyy"));
            }else{
                awardTransactionDetailsForm.txtNoticeDate.setText("");
            }
        }else{
            awardTransactionDetailsForm.lblTransactionType.setText("");
            awardTransactionDetailsForm.txtArComments.setText("");
            awardTransactionDetailsForm.txtNoticeDate.setText("");
        }
        //Added for Case 2269: Money & End Dates Tab/Panel in Awards Module - end
    }
    
    public boolean validate() throws edu.mit.coeus.exception.CoeusUIException {
        return true;
    }
   
    /**
     * Handler for the buttons
     */
    public void actionPerformed(java.awt.event.ActionEvent e) {
        Object source=e.getSource();
        try{
            dlgAwardTransaction.setCursor( new Cursor(Cursor.WAIT_CURSOR));
            if (source.equals(awardTransactionDetailsForm.btnClose)) {
                dlgAwardTransaction.dispose();
            } else if (source.equals(awardTransactionDetailsForm.btnExpandAll)) {
                // handler for expand all button to be written
                awardTransactionDetailsForm.awardAmountTreeTable.expandAll(true);
            } else if (source.equals(awardTransactionDetailsForm.btnGoto)) { // Go to Button
                // handler for Go to button
                // displays the go to award window
                AwardGoToForm awardGoToForm = new AwardGoToForm();
                int selOption = awardGoToForm.display();
                if (selOption!=awardGoToForm.CANCEL) { // if the window was not cancelled
                    String value = awardGoToForm.getValue();
                    if (selOption==awardGoToForm.MIT_NUMBER) {
                        awardTransactionDetailsForm.awardAmountTreeTable.gotoMITAwardNumber(value);
                    } else if (selOption==awardGoToForm.ACCOUNT_NUMBER) {
                        awardTransactionDetailsForm.awardAmountTreeTable.gotoAccountNumber(value);
                    }
                }
            } else if (source.equals(awardTransactionDetailsForm.btnPrev)) { //Prev button
                selHistoryIndex = selHistoryIndex-1;
                setData(cvHistoryData,selHistoryIndex);
                BeanEvent beanEvent = new BeanEvent();
                beanEvent.setMessageId(selHistoryIndex);
                beanEvent.setBean(new AwardAmountInfoBean());
                beanEvent.setSource(this);
                fireBeanUpdated(beanEvent);
            } else if (source.equals(awardTransactionDetailsForm.btnNext)) { //Next button
                // handler for Next button to be written
                selHistoryIndex = selHistoryIndex+1;
                setData(cvHistoryData,selHistoryIndex);
                BeanEvent beanEvent = new BeanEvent();
                beanEvent.setMessageId(selHistoryIndex);
                beanEvent.setBean(new AwardAmountInfoBean());
                beanEvent.setSource(this);
                fireBeanUpdated(beanEvent);
            } else if(source.equals(awardTransactionDetailsForm.btnPrint)) {
                 if (awardTransactionDetailsForm.awardAmountTreeTable.getRowCount()> 0) {
                    try{
                        printBudgetModification();

                    }catch(Exception exe){
                        exe.printStackTrace();
                        CoeusOptionPane.showErrorDialog(exe.getMessage());
                    }
                    
                }

            }
        }catch (Exception ee) {
            ee.printStackTrace();
        }
        finally {
            dlgAwardTransaction.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        }
    }
    
    /**
     * Creates the root award number from the award number passed as arguement
     */
    private String createRootAwardNumber(String awardNumber) {
        String mitAwardNumer=awardNumber.substring(0,7)+"001";
        return mitAwardNumer;
    }
    
    /**
     * Gets data from the server by making a server call.
     **/
    public void getDataFromServer() {
        RequesterBean requesterBean = new RequesterBean();
        ResponderBean responderBean = new ResponderBean();
        requesterBean.setFunctionType(GET_TRANSACTION_DATA);
        requesterBean.setDataObject(awardAmountInfoBean);
        AppletServletCommunicator comm= new AppletServletCommunicator(connectTo,
        requesterBean);
        comm.setRequest(requesterBean);
        comm.send();
        responderBean = comm.getResponse();
        if(responderBean.isSuccessfulResponse()) {
            cvTransactions = (CoeusVector)responderBean.getDataObject();
            //Added for Case 2269: Money & End Dates Tab/Panel in Awards Module - start
            Vector vecServerData = responderBean.getDataObjects();
            if(vecServerData != null && vecServerData.size() > 0){
                awardAmountTransactionBean = (AwardAmountTransactionBean)vecServerData.get(0);
            }
            //Added for Case 2269: Money & End Dates Tab/Panel in Awards Module - end
        }else{
            // Added by chandra 16th Sept 2004
            edu.mit.coeus.utils.CoeusOptionPane.showErrorDialog(responderBean.getMessage());
            dlgAwardTransaction.dispose();
            //End Chandra
        }
    }
    
    /**
     * Getter for property awardNumber.
     * @return Value of property awardNumber.
     */
    public String getAwardNumber() {
        return awardNumber;
    }
    
    /**
     * Setter for property awardNumber.
     * @param awardNumber New value of property awardNumber.
     */
    public void setAwardNumber(String awardNumber) {
        this.awardNumber = awardNumber;
    }
    
    //For print Budget Modification report
    private void printBudgetModification()throws CoeusException{
        RequesterBean requesterBean = new RequesterBean();
        ResponderBean responderBean = new ResponderBean();
        requesterBean.setFunctionType(PRINT_BUDGET_MODIFICATION);
        Hashtable htPrintParams = new Hashtable();
        htPrintParams.put("MIT_AWARD_NUMBER",awardAmountInfoBean.getMitAwardNumber());
        htPrintParams.put("TRANSACTION_ID",awardAmountInfoBean.getTransactionId());
        requesterBean.setDataObject(htPrintParams);
        
        //For Streaming
        requesterBean.setId("Award/BudgetHistoryTransactionDetail");
        requesterBean.setFunctionType('R');
        //For Streaming
        
        AppletServletCommunicator comm= new AppletServletCommunicator(PRINT_SERVLET, requesterBean);
        comm.setRequest(requesterBean);
        comm.send();
        responderBean = comm.getResponse();
        String fileName = "";      
        if(responderBean.isSuccessfulResponse()){
             AppletContext coeusContxt = CoeusGuiConstants.getMDIForm().getCoeusAppletContext();
             
             fileName = (String)responderBean.getDataObject();
             System.out.println("Report Filename is=>"+fileName);
             
             fileName.replace('\\', '/') ; // this is fix for Mac
//             URL reportUrl = null;
             try{
//                reportUrl = new URL( CoeusGuiConstants.CONNECTION_URL + fileName );
//             
//             
//             if (coeusContxt != null) {
//                 coeusContxt.showDocument( reportUrl, "_blank" );
//             }else {
//                 javax.jnlp.BasicService bs = (javax.jnlp.BasicService)javax.jnlp.ServiceManager.lookup("javax.jnlp.BasicService");
//                 bs.showDocument( reportUrl );
//             }
                 URL urlObj = new URL(fileName);
                 URLOpener.openUrl(urlObj);
             }catch(MalformedURLException muEx){
                 throw new CoeusException(muEx.getMessage());
             }catch(Exception uaEx){
                 throw new CoeusException(uaEx.getMessage());
             }
             
        }else{
            if (responderBean.getDataObject().equals("NoAwardAmountInfo")){
                CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(NO_AWARD_AMOUNT) + awardAmountInfoBean.getTransactionId() +". ");
            }else{
                 throw new CoeusException(responderBean.getMessage());
            }
        }
    }
}
