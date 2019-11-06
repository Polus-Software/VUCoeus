/*
 * PrintSubcontractController.java
 *
 * Created on March 24, 2005, 12:25 PM
 */

package edu.mit.coeus.admin.controller;

import edu.mit.coeus.admin.gui.PrintSubcontractForm;
import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.gui.CoeusAppletMDIForm;
import edu.mit.coeus.gui.CoeusDlgWindow;
import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.gui.URLOpener;
import edu.mit.coeus.utils.*;
import java.applet.AppletContext;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Hashtable;
import javax.swing.AbstractAction;


/**
 *
 * @author  jenlu
 */
public class PrintSubcontractController implements ActionListener{
    private PrintSubcontractForm printSubcontractForm;
    private CoeusDlgWindow dlgPrintSub;
    private CoeusMessageResources coeusMessageResources;
    private CoeusAppletMDIForm mdiForm;
    private String formID;
    private String awardNumber;
    private String awardNumbers[];
    private String printType;
    private String startDate;
    private String endDate;
    private static final char GET_FISCAL_PERIOD = 'F'; 
    private static final String GET_SERVLET = "/SubcontractMaintenenceServlet";
    private static final String connect = CoeusGuiConstants.CONNECTION_URL + GET_SERVLET;
    
    private static final char PRINT_294_295_REPORT = 'R';
    private static final String PRINT_SERVLET = "/printServlet";
    //private static final String printConnect = CoeusGuiConstants.CONNECTION_URL + PRINT_SERVLET;
    private static final String printConnect = CoeusGuiConstants.CONNECTION_URL + "/ReportConfigServlet";
   
    /** Creates a new instance of PrintSubcontractController */
    public PrintSubcontractController(Hashtable htFormParams) {
        this.formID = (String)htFormParams.get("FORM_ID");
        if (formID.equals("294")){
            this.awardNumber = (String)htFormParams.get("AWARD_NUM");
            this.awardNumbers = (String[])htFormParams.get("AWARD_NUMS");
        }
        printSubcontractForm = new PrintSubcontractForm();
        postInitComponents();
        registerComponents();
       
        display();
    }
    
     /** this method  will initialize the dialog box and sets required 
     *properticies of the dialog box.
     */ 
     private void postInitComponents(){
         
         dlgPrintSub = new CoeusDlgWindow(mdiForm);
         dlgPrintSub.setResizable(false);
         dlgPrintSub.setModal(true);
         dlgPrintSub.getContentPane().add(printSubcontractForm);
         dlgPrintSub.setFont(CoeusFontFactory.getLabelFont());
         dlgPrintSub.setTitle("Print Subcontract Reports");
         dlgPrintSub.setDefaultCloseOperation(CoeusDlgWindow.DO_NOTHING_ON_CLOSE);
         dlgPrintSub.setSize(380, 200);
         java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
         java.awt.Dimension dlgSize = dlgPrintSub.getSize();
         dlgPrintSub.setLocation(screenSize.width/2 - (dlgSize.width/2),
         screenSize.height/2 - (dlgSize.height/2));
         
         dlgPrintSub.addEscapeKeyListener(
         new AbstractAction("escPressed"){
             public void actionPerformed(ActionEvent ae){
                 performCloseAction();
                 return;
             }
         });
         dlgPrintSub.setDefaultCloseOperation(CoeusDlgWindow.DO_NOTHING_ON_CLOSE);
         dlgPrintSub.addWindowListener(new WindowAdapter(){
             public void windowClosing(WindowEvent we){
                 performCloseAction();
                 
             }
         });
         
         dlgPrintSub.addComponentListener(
         new ComponentAdapter(){
             public void componentShown(ComponentEvent e){
                 setWindowFocus();
             }
         });
         
                  
     }
     
     private void setWindowFocus(){
        printSubcontractForm.btnClose.requestFocusInWindow();
    }
    private void performCloseAction(){
        dlgPrintSub.dispose();
    }
    
    public void setPrintSubcontractScreent() {
        try{
        CoeusVector cvFP = getDefaultFiscalPeriod();
        printSubcontractForm.lblStart.setText(cvFP.get(0).toString());
        printSubcontractForm.lblEnd.setText(cvFP.get(1).toString());
        
        if (formID.equals("294")){
            printSubcontractForm.lbl295Report.setVisible(false);
            printSubcontractForm.lbl295To.setVisible(false);
            printSubcontractForm.txtEnd.setVisible(false);
            printSubcontractForm.txtStart.setVisible(false);
        }
        }catch (CoeusException coeusException){
            CoeusOptionPane.showErrorDialog(coeusException.getMessage());
        }
    }
       
   
    public void display() {
        setPrintSubcontractScreent();
        dlgPrintSub.setVisible(true);
    }
    
    public void registerComponents() {
        
//       CustomFocusAdapter customFocusAdapter = new CustomFocusAdapter();
        
//        printSubcontractForm.txtStart.addFocusListener(customFocusAdapter);
//        printSubcontractForm.txtEnd.addFocusListener(customFocusAdapter);
        printSubcontractForm.btnPrint.addActionListener(this);
        printSubcontractForm.btnClose.addActionListener(this);
        
    }
    
    public CoeusVector getDefaultFiscalPeriod()throws CoeusException{
        CoeusVector cvData = null;
        RequesterBean requester;
        ResponderBean responder;
        requester = new RequesterBean();
        requester.setFunctionType(GET_FISCAL_PERIOD);
        AppletServletCommunicator comm
        = new AppletServletCommunicator(connect, requester);
        
        comm.send();
        responder = comm.getResponse();
        if(responder!= null){
            if(responder.isSuccessfulResponse()){
                cvData = (CoeusVector)responder.getDataObject();
            }else{
                throw new CoeusException(responder.getMessage(),0);
            }
        }
        return cvData;
    
                
    }
   
    public void actionPerformed(java.awt.event.ActionEvent e) {
        Object source = e.getSource();
        try{
            if (source.equals(printSubcontractForm.btnClose)){
                performCloseAction();
            }
            if (source.equals(printSubcontractForm.btnPrint)){
                performPrintAction();
            }
        }catch(Exception ex){
            CoeusOptionPane.showErrorDialog(ex.getMessage());
        }
    }
    public void performPrintAction(){
        Hashtable htPrintParams = new Hashtable();
        htPrintParams.put("FORM_ID", formID);
        
        if (formID.equals("295")){
            startDate = printSubcontractForm.txtStart.getText();
            endDate = printSubcontractForm.txtEnd.getText();
            if ((startDate == null || startDate.length() == 0) || (endDate == null || endDate.length() == 0)){
                CoeusOptionPane.showErrorDialog("Please enter a Start and End fiscal year for the 295 report. ");
//        CoeusOptionPane.showInfoDialog(
//             coeusMessageResources.parseMessageKey("funcNotImpl_exceptionCode.1100"));
//        String year = startDate.substring(0,4); 
                return;
            }
            if (startDate.length() != 6){
                CoeusOptionPane.showErrorDialog("Invalid period start. Period start should be in the format YYYYMM where YYYY is the fiscal year and MM is fiscal month (01 For July, 02 For August etc.).");
                return;
            }
            if (endDate.length() != 6){
                CoeusOptionPane.showErrorDialog("Invalid period end. Period end should be in the format YYYYMM where YYYY is the fiscal year and MM is fiscal month (01 For July, 02 For August etc.).");
                return;
            }
            if (Integer.parseInt(startDate) >  Integer.parseInt(endDate) ){
                CoeusOptionPane.showErrorDialog("Period start cannot be later than period end.");
                return;
            }
            
            htPrintParams.put("START_DATE", startDate);
            htPrintParams.put("END_DATE", endDate);
        }
        else{
            htPrintParams.put("AWARD_NUM", awardNumber);
            htPrintParams.put("AWARD_NUMS", awardNumbers);
        }
        
        if (printSubcontractForm.rdBtnRegular.isSelected()){
            printType = "REG";
        }else if (printSubcontractForm.rdBtnFinal.isSelected()){
            printType = "FIN";
        }else if (printSubcontractForm.rdBtnRevised.isSelected()){
            printType = "REV";
        } 
        htPrintParams.put("PRINT_TYPE", printType);
        
        RequesterBean requesterBean = new RequesterBean();
        ResponderBean responderBean = new ResponderBean();
        requesterBean.setFunctionType(PRINT_294_295_REPORT);
        requesterBean.setDataObject(htPrintParams);
        
        //For Streaming
        String printType = (String)htPrintParams.get("PRINT_TYPE");
        if(formID.equals("295")){
            requesterBean.setId("Subcontract/295");
        }else {
            requesterBean.setId("Subcontract/294");
        }
        requesterBean.setFunctionType('R');
        //For Streaming
        
        AppletServletCommunicator comm = new AppletServletCommunicator(printConnect,requesterBean);
        comm.setRequest(requesterBean);
        comm.send();
        responderBean = comm.getResponse(); 
        if(responderBean == null) {
            //Could not contact server.
            CoeusOptionPane.showErrorDialog("Could not contact server.");
            return;
        }
        if (!responderBean.isSuccessfulResponse()) {
            CoeusOptionPane.showErrorDialog(responderBean.getMessage());
            return;
        }else{
//        if(responderBean.isSuccessfulResponse()){
             String fileName = "";  
             //AppletContext coeusContxt = CoeusGuiConstants.getMDIForm().getCoeusAppletContext();
             
             fileName = (String)responderBean.getDataObject();
             System.out.println("Report Filename is=>"+fileName);
             
             fileName.replace('\\', '/') ; 
             URL reportUrl = null;
             try{
                reportUrl = new URL( fileName );
                URLOpener.openUrl(reportUrl);
                performCloseAction();//3587
             /*if (coeusContxt != null) {
                 coeusContxt.showDocument( reportUrl, "_blank" );
             }else {
                 javax.jnlp.BasicService bs = (javax.jnlp.BasicService)javax.jnlp.ServiceManager.lookup("javax.jnlp.BasicService");
                 bs.showDocument( reportUrl );
             }*/
//             }catch(MalformedURLException muEx){
//                 throw new CoeusException(muEx.getMessage());
//             }catch(Exception uaEx){
//                 throw new CoeusException(uaEx.getMessage());
             } catch (Exception ex) {
                ex.printStackTrace();
                CoeusOptionPane.showErrorDialog(""+ex.getMessage());
             }
        
        }
    }
    
}
