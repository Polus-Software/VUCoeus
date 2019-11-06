/*
 * ReviewAwardRepReq.java
 *
 * Created on June 23, 2005, 5:01 PM
 */

package edu.mit.coeus.award.gui;

import edu.mit.coeus.award.bean.AwardAmountInfoBean;
import edu.mit.coeus.award.bean.AwardBaseBean;
import edu.mit.coeus.award.bean.AwardBean;
import edu.mit.coeus.award.controller.AwardReportingReqController;
import edu.mit.coeus.award.controller.ReportingReqBaseWindowController;
import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.exception.CoeusClientException;
import edu.mit.coeus.gui.CoeusAppletMDIForm;
import edu.mit.coeus.gui.CoeusDlgWindow;
import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.gui.CoeusInternalFrame;
import edu.mit.coeus.utils.AppletServletCommunicator;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.TypeConstants;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyVetoException;
import javax.swing.AbstractAction;
import javax.swing.JFrame;

/**
 *
 * @author  ajaygm
 */
public class ReviewAwardRepReq extends javax.swing.JComponent
implements ActionListener{
    
    private CoeusAppletMDIForm mdiForm = CoeusGuiConstants.getMDIForm();
    private AwardBaseBean awardBaseBean;
    private AwardReportingReqController awardReportingReqController = null;
    private static final String WINDOW_TITLE = "Review Award Reporting Requirements";
    private CoeusDlgWindow dlgReviewRepReq;
    private static final int WIDTH = 455;
    private static final int HEIGHT = 225;
    private char functionType;
    public static final char CORRECT_AWARD = 'M';
    public static final char IS_AWARD_LOCKED = 'I';
    public static final String REP_REQ_SERVLET = "/AwardReportReqMaintenanceServlet";
    
    /** Creates new form ReviewAwardRepReq */
    public ReviewAwardRepReq(AwardBaseBean awardBaseBean, char functionType) {
        this.awardBaseBean = awardBaseBean;
        this.functionType =functionType;
        initComponents();
        registerComponents();
        postInitComponents();
        formatFields();
    }
    
    //    public static void main(String s[]){
    //        JFrame frame = new JFrame("Review");
    //        ReviewAwardRepReq reviewAwardRepReq = new ReviewAwardRepReq();
    //        frame.getContentPane().add(reviewAwardRepReq);
    //        frame.setSize(455, 225);
    //        frame.show();
    //    }
    
    
    
    /** This method is used to set the listeners to the components.
     */
    public void registerComponents() {
        btnOK.addActionListener(this);
        //btnCancel.addActionListener(this);
    }
    
    /** This method creates and sets the display attributes for the dialog
     */
    public void postInitComponents(){
        dlgReviewRepReq = new CoeusDlgWindow(mdiForm);
        dlgReviewRepReq.setResizable(false);
        dlgReviewRepReq.setModal(true);
        dlgReviewRepReq.getContentPane().add(this);
        dlgReviewRepReq.setTitle(WINDOW_TITLE);
        dlgReviewRepReq.setFont(CoeusFontFactory.getLabelFont());
        dlgReviewRepReq.setSize(WIDTH, HEIGHT);
        
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension dlgSize = dlgReviewRepReq.getSize();
        dlgReviewRepReq.setLocation(screenSize.width/2 - (dlgSize.width/2),
        screenSize.height/2 - (dlgSize.height/2));
        
        dlgReviewRepReq.addComponentListener(
        new ComponentAdapter(){
            public void componentShown(ComponentEvent e){
                requestDefFocus();
            }
        });
        
        dlgReviewRepReq.addEscapeKeyListener(new AbstractAction("escPressed"){
            public void actionPerformed(ActionEvent ae){
                dlgReviewRepReq.dispose();
            }
        });
        
        dlgReviewRepReq.setDefaultCloseOperation(CoeusDlgWindow.DO_NOTHING_ON_CLOSE);
        dlgReviewRepReq.addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent we){
                dlgReviewRepReq.dispose();
            }
        });
        //code for disposing the window ends
    }
    
    /** To set the default focus for the component
     */
    public void requestDefFocus(){
        btnOK.requestFocus();
    }
    
    public void formatFields(){
        txtArReviewAwards.setEnabled(false);
        //txtArReviewAwards.setEditable(false);
        txtArReviewAwards.setDisabledTextColor(Color.BLACK);
        txtArReviewAwards.setBackground(
        (java.awt.Color) javax.swing.UIManager.getDefaults().get("Panel.background"));
    }
    
    /** Displays the Form which is being controlled.
     */
    public void display() {
        dlgReviewRepReq.setVisible(true);
    }
    
    public void setFormData(Object data){
        CoeusVector cvRepReq =  (CoeusVector)data;
        if(cvRepReq != null && cvRepReq.size()>0){
            StringBuffer strData = new StringBuffer();
            for(int index = 0 ; index < cvRepReq.size() ; index++){
                String mitAwdNo = (String)cvRepReq.get(index);
                strData.append(mitAwdNo);
                strData.append("\n");
            }
            txtArReviewAwards.setText(strData.toString());
        }
    }
    
    /*private void showReportingReqirements(){
        String awardNumber = awardBaseBean.getMitAwardNumber();
        RequesterBean requester = new RequesterBean();
        ResponderBean responder;
        
        char mode = TypeConstants.DISPLAY_MODE;
        //check if any reporting Requirement Window is opened and >= 3
        //Disp msg Cannot open more than 3 Award Reporting Requirement windows.
        //Please close atleast one Award Reporting Requirement window.
        boolean repReqWinOpen = isRepReqWindowOpen(awardNumber, TypeConstants.DISPLAY_MODE, true, true);
        if(repReqWinOpen) {
            return ;
        }
        //else Check if this rep req is opened then bring it to front
        
        if(functionType == TypeConstants.DISPLAY_MODE) {
            //check if any reporting Requirements is already opened in Modify Mode.
            //Disp msg Only one Reporting Requirements window can be open in modify mode.
            //Reporting Requirements window for award "+Award + " is already open in modify mode.
            //Do you want to open Reporting Requirements for the selected award in display mode."
            repReqWinOpen = isRepReqWindowOpen(awardNumber, TypeConstants.MODIFY_MODE, false, true);
            if(repReqWinOpen) {
                int selected = CoeusOptionPane.showQuestionDialog("Only one Reporting Requirements window can be open in modify mode."+
                "Reporting Requirements window for award " + awardNumber + " is already open in modify mode."+
                "Do you want to open Reporting Requirements for the selected award in display mode.",
                CoeusOptionPane.OPTION_YES_NO, CoeusOptionPane.DEFAULT_YES);
                if(selected == CoeusOptionPane.SELECTION_NO) {
                    return ;
                }else {
                    mode = TypeConstants.DISPLAY_MODE;
                }
            }else {
                // No reporting Requirements is opened in Modify Mode
                mode = TypeConstants.MODIFY_MODE;
            }
        }//End if CORRECT_AWARD
        
        //if Modify Mode then check if Award sheet is opened in Modes M, N, E,C,P.
        //Award sheet for +s_mit_award_number + is open in modify mode.
        //Cannot open reporting requirements window in modify mode when
        //an award is being modified.\n Do you want to open reporting
        //requirements window for the selected award in display mode
        if(mode == TypeConstants.MODIFY_MODE) {
            if(isAwardWindowOpen(awardNumber, CORRECT_AWARD, false)) {
                //An Award is opened in Modify Mode
                AwardBaseWindow awardBaseWindow = (AwardBaseWindow)mdiForm.getEditingFrame(CoeusGuiConstants.AWARD_BASE_WINDOW);
                if(awardBaseWindow == null) {
                    //No Award is being modified
                }else {
                    
                    //Bug Fix:1672 Start
                    //awardNumber = awardBaseWindow.getAwardBaseBean().getMitAwardNumber();
                    String awdNumber = awardBaseWindow.getAwardBaseBean().getMitAwardNumber();
                    //Bug Fix:1672 End
                    
                    int selection = CoeusOptionPane.showQuestionDialog("Award sheet for " + awdNumber + " is open in modify mode."
                    +"Cannot open reporting requirements window in modify mode when "
                    +"an award is being modified.\n Do you want to open reporting "
                    +"requirements window for the selected award in display mode.",
                    CoeusOptionPane.OPTION_YES_NO, CoeusOptionPane.DEFAULT_YES);
                    if(selection == CoeusOptionPane.SELECTION_NO) {
                        return ;
                    }else {
                        mode = TypeConstants.DISPLAY_MODE;
                    }//End if else selection
                }//End if else Award Modified (award = null)
            }
        }
        
        //if award is locked then display Yes No Dialog
        //"Do you want to open reporting requirements for the selected award in display mode.
        if(mode == TypeConstants.MODIFY_MODE) {
            requester.setFunctionType(IS_AWARD_LOCKED);
            requester.setDataObject(awardNumber);
            AppletServletCommunicator comm = new AppletServletCommunicator(CoeusGuiConstants.CONNECTION_URL + REP_REQ_SERVLET, requester);
            
            comm.send();
            responder = comm.getResponse();
            if(responder.isSuccessfulResponse()){
                boolean awardLocked = ((Boolean)responder.getDataObject()).booleanValue();
                if(awardLocked) {
                    CoeusOptionPane.showInfoDialog(responder.getMessage());
                    int selection = CoeusOptionPane.showQuestionDialog("Do you want to open reporting requirements for the selected award in display mode.",
                    CoeusOptionPane.OPTION_YES_NO, CoeusOptionPane.DEFAULT_YES);
                    if(selection == CoeusOptionPane.SELECTION_NO) {
                        return ;
                    }else {
                        mode = TypeConstants.DISPLAY_MODE;
                    }
                }
            }else {
                //Server Error
                CoeusOptionPane.showInfoDialog(responder.getMessage());
                return ;
            }
        }//End if Modify mode
        
        AwardBean awardBean = new AwardBean();
        awardBean.setMitAwardNumber(awardNumber);
        ReportingReqBaseWindowController reportingReqBaseWindowController;
        reportingReqBaseWindowController = new ReportingReqBaseWindowController(awardBean, mode);
        reportingReqBaseWindowController.display();
    }*/
    
    /** This method is used to check whether the given Award number is already
      * opened in the given mode or not.
      * @param refId refId - for award its Award Number.
      * @param mode mode of Form open.
      * @param displayMessage if true displays error messages else doesn't.
      * @return true if Award window is already open else returns false.
      */
//     boolean isAwardWindowOpen(String refId, char mode, boolean displayMessage) {
//        boolean duplicate = false;
//        try{
//            duplicate = mdiForm.checkDuplicate(CoeusGuiConstants.AWARD_BASE_WINDOW, refId, mode );
//        }catch(Exception e){
//            // Exception occured.  Record may be already opened in requested mode
//            //   or if the requested mode is edit mode and application is already
//            //   editing any other record. 
//            duplicate = true;
//            if( displayMessage ){
//                if(e.getMessage().length() > 0 ) {
//                    CoeusOptionPane.showInfoDialog(e.getMessage());
//                }
//            }
//            // try to get the requested frame which is already opened 
//            CoeusInternalFrame frame = mdiForm.getFrame(
//                    CoeusGuiConstants.AWARD_BASE_WINDOW,refId);
//            /*if(frame == null){
//                // if no frame opened for the requested record then the 
//                //   requested mode is edit mode. So get the frame of the
//                //   editing record. 
//                frame = mdiForm.getEditingFrame( 
//                    CoeusGuiConstants.AWARD_BASE_WINDOW );
//            }*/
//            if (frame != null){
//                try{
//                    frame.setSelected(true);
//                    frame.setVisible(true);
//                }catch (PropertyVetoException propertyVetoException) {
//                    
//                }
//            }
//            return true;
//        }
//        return false;
//     }
     
   /** This method is used to check whether the given Reporting Requirement is already
      * opened in the given mode or not.
      * @param refId refId - for Reporting Requirement its Award Number.
      * @param mode mode of Form open.
      * @param displayMessage if true displays error messages else doesn't.
      * @return true if Reporting Requirement window is already open else returns false.
      */
     /*boolean isRepReqWindowOpen(String refId, char mode, boolean displayMessage, boolean selectFrame) {
        boolean duplicate = false;
        try{
            duplicate = mdiForm.checkDuplicate(CoeusGuiConstants.REPORTING_REQ_BASE_WINDOW, refId, mode );
        }catch(Exception e){
            // Exception occured.  Record may be already opened in requested mode
            //   or if the requested mode is edit mode and application is already
            //   editing any other record. 
            duplicate = true;
            if( displayMessage ){
                if(e.getMessage().length() > 0 ) {
                    CoeusOptionPane.showInfoDialog(e.getMessage());
                }
            }
            
            if(selectFrame) {
                // try to get the requested frame which is already opened
                CoeusInternalFrame frame = mdiForm.getFrame(
                CoeusGuiConstants.REPORTING_REQ_BASE_WINDOW, refId);
                if (frame != null){
                    try{
                        frame.setSelected(true);
                        frame.setVisible(true);
                    }catch (PropertyVetoException propertyVetoException) {
                        
                    }
                }
            }
            
            return true;
        }
        return false;
     }*/
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        lblDescription1 = new javax.swing.JLabel();
        scrPnReviewAwards = new javax.swing.JScrollPane();
        txtArReviewAwards = new javax.swing.JTextArea();
        btnOK = new javax.swing.JButton();

        setLayout(new java.awt.GridBagLayout());

        lblDescription1.setFont(CoeusFontFactory.getLabelFont());
        lblDescription1.setText("Reporting Requirements have changed for the following awards:\n");
        lblDescription1.setPreferredSize(new java.awt.Dimension(310, 15));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(6, 3, 0, 0);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        add(lblDescription1, gridBagConstraints);

        txtArReviewAwards.setFont(CoeusFontFactory.getNormalFont());
        scrPnReviewAwards.setViewportView(txtArReviewAwards);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 0, 0);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(scrPnReviewAwards, gridBagConstraints);

        btnOK.setFont(CoeusFontFactory.getLabelFont());
        btnOK.setMnemonic('Y');
        btnOK.setText("OK");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 0, 0);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        add(btnOK, gridBagConstraints);

    }//GEN-END:initComponents
    
    public void actionPerformed(java.awt.event.ActionEvent e) {
        Object source = e.getSource();
        if(source.equals(btnOK)){
            dlgReviewRepReq.dispose();
            //showReportingReqirements();
        }/*else if(source.equals(btnCancel)){
            dlgReviewRepReq.dispose();
        }*/
    }
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JButton btnOK;
    public javax.swing.JLabel lblDescription1;
    public javax.swing.JScrollPane scrPnReviewAwards;
    public javax.swing.JTextArea txtArReviewAwards;
    // End of variables declaration//GEN-END:variables
    
}
