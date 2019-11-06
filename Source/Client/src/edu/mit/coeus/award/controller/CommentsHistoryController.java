/** Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */


package edu.mit.coeus.award.controller;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;

import edu.mit.coeus.gui.*;
import edu.mit.coeus.brokers.*;
import edu.mit.coeus.bean.*;
import edu.mit.coeus.award.gui.*;
import edu.mit.coeus.award.bean.*;
import edu.mit.coeus.utils.*;
import edu.mit.coeus.utils.query.*;
import edu.mit.coeus.exception.*;


/** CommentsHistoryController.java
 * Created on April 12, 2004, 5:52 PM
 * @author  ajaygm
 */
public class CommentsHistoryController extends AwardController 
implements ActionListener{
    
    /** Holds an instance of <CODE>CommentsHistoryForm</CODE> */
    private CommentsHistoryForm commentsHistoryForm;
    
    /** Holds an instance of <CODE>AwardDetailsBean</CODE> */
    private AwardDetailsBean awardDetailsBean;
    
    private AwardCommentsBean awardCommentsBean;
    
    private static final String GET_SERVLET = "/AwardMaintenanceServlet";
    
    private static final String connect = CoeusGuiConstants.CONNECTION_URL + GET_SERVLET;
    
    private CoeusAppletMDIForm mdiForm = CoeusGuiConstants.getMDIForm();
    private CoeusDlgWindow dlgCommentsHistoryController;
    private QueryEngine queryEngine;
    private CoeusVector cvComments;
    
    private CoeusVector cvHistoryData;
    private static final String WINDOW_TITLE = "Comments History";
    private static final int WIDTH = 585;
    private static final int HEIGHT =  275;
    private static final String NEXT = "N";
    private static final String PREVIOUS = "P";
    private static final char GET_COMMENTS_HISTORY = 'R';
    
    /** Temp Code for testing**/
    private int selectedSeq = 0;
    
//    private Vector vector = new Vector();
    
    public void getHistoryData (AwardCommentsBean awardCommentsBean) throws CoeusUIException {
        RequesterBean requesterBean = new RequesterBean();
        ResponderBean responderBean = new ResponderBean();
        requesterBean.setFunctionType(GET_COMMENTS_HISTORY);
        requesterBean.setDataObject(awardCommentsBean);   

        AppletServletCommunicator comm = new AppletServletCommunicator(connect, requesterBean);
        comm.setRequest(requesterBean);
        comm.send();
        responderBean = comm.getResponse();
        
        if(responderBean.isSuccessfulResponse()) {
            cvComments = (CoeusVector)responderBean.getDataObject();
        }else{
           throw new CoeusUIException(responderBean.getMessage(), CoeusUIException.ERROR_MESSAGE);
        }
       // return htReportTerms ;
        
        
        
        
        
//        try{
//            cvComments = queryEngine.getDetails(
//            queryKey, KeyConstants.AWARD_COMMENTS_HISTORY);
//        }catch (CoeusException coeusException){
//            coeusException.printStackTrace();
    //    }
        NotEquals neSeqNo = new NotEquals("sequenceNumber",
            new Integer(awardCommentsBean.getSequenceNumber()));
        Equals eqCommentCode = new Equals("commentCode",
            new Integer(awardCommentsBean.getCommentCode()));
        And neSeqNoAndeqCommentCode = new And(neSeqNo,eqCommentCode);
        cvHistoryData = cvComments.filter(neSeqNoAndeqCommentCode);//eqCommentCode);
        //cvHistoryData.sort("sequenceNumber", true);
    }
    
    /*Temp code ends here*/
    
    
    
    /** Creates a new instance of CommentsHistoryController */
    public CommentsHistoryController () {    
    }
    
    /** Creates a new instance of CommentsHistoryController
     * @param awardBaseBean,commentType,mdiForm
     */
    public CommentsHistoryController (AwardBaseBean awardBaseBean, 
    CommentTypeBean commentTypeBean,AwardCommentsBean awardCommentsBean){
        super(awardBaseBean);
        this.awardCommentsBean = awardCommentsBean;
        queryEngine = QueryEngine.getInstance ();
        registerComponents();
        setFormData(commentTypeBean);
        postInitComponents();
    }
    
    public void postInitComponents(){
        dlgCommentsHistoryController = new CoeusDlgWindow(mdiForm);
        dlgCommentsHistoryController.setResizable(false);
        dlgCommentsHistoryController.setModal(true);
        dlgCommentsHistoryController.getContentPane().add(commentsHistoryForm);
        dlgCommentsHistoryController.setTitle(WINDOW_TITLE);
        dlgCommentsHistoryController.setFont(CoeusFontFactory.getLabelFont());
        dlgCommentsHistoryController.setSize(WIDTH, HEIGHT);
        
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension dlgSize = dlgCommentsHistoryController.getSize();
        dlgCommentsHistoryController.setLocation(screenSize.width/2 - (dlgSize.width/2),
        screenSize.height/2 - (dlgSize.height/2));
        
         dlgCommentsHistoryController.addComponentListener(
            new ComponentAdapter(){
                public void componentShown(ComponentEvent e){
                    requestDefaultFocus();
                }
        });
        
//        getHistoryData();
    }
         
    /** Displays the Form which is being controlled.
     */
    public void display () {
        dlgCommentsHistoryController.setVisible(true);
    }
   
    /** Perform field formatting.
     * enabling, disabling components depending on the different conditions
     */
    public void formatFields () {
    }
    
    /** An overridden method of the controller
    * @return commentsHistoryForm returns the controlled form component
    */
    public Component getControlledUI () {
        return commentsHistoryForm;
    }
    
    /** Returns the form data
     * @return returns the form data
     */
    public Object getFormData () {
        return null;
    }
    
    /** This method is used to set the listeners to the components.
     */
    public void registerComponents () {
        //Add listeners to all the buttons
        commentsHistoryForm = new CommentsHistoryForm();
        commentsHistoryForm.btnClose.addActionListener(this);
        commentsHistoryForm.btnPrevious.addActionListener(this);
        commentsHistoryForm.btnNext.addActionListener(this);
      
        /** Code for focus traversal - start */
        java.awt.Component[] components = { commentsHistoryForm.btnClose,
        commentsHistoryForm.btnPrevious,commentsHistoryForm.btnNext,
        commentsHistoryForm.txtArComments
        };
        
        ScreenFocusTraversalPolicy traversePolicy = new ScreenFocusTraversalPolicy( components );
        commentsHistoryForm.setFocusTraversalPolicy(traversePolicy);
        commentsHistoryForm.setFocusCycleRoot(true);
         
        /** Code for focus traversal - end */
    }
    
    /** To set the default focus for the component
    */
    public void requestDefaultFocus(){    
       commentsHistoryForm.btnClose.requestFocus();
    }
    
    /** Saves the Form Data.
     */
    public void saveFormData () {
    }
    
     /** This method is used to set the form data specified in
     * <CODE> data </CODE>
     * @param data data to set to the form
     */
    public void setFormData (Object data) {
        CommentTypeBean commentTypeBean = (CommentTypeBean)data;
         
        /*Disable the Next Button On load */
        commentsHistoryForm.btnNext.setEnabled(false);
        
        /*Create an instance of Coeus Vector to get
         *the Sponsor Award Number ,Award Number*/
         
        CoeusVector cvAwardDetails = new CoeusVector();
        try{
            cvAwardDetails = queryEngine.executeQuery (
            queryKey,AwardDetailsBean.class, CoeusVector.FILTER_ACTIVE_BEANS);
        }catch (CoeusException coeusException){
            coeusException.printStackTrace ();
        }
        
        awardDetailsBean = (AwardDetailsBean)cvAwardDetails.get(0);
        commentsHistoryForm.lblSponsorAwardNumberValue.setText(
        awardDetailsBean.getSponsorAwardNumber());
        commentsHistoryForm.lblAwardNumberValue.setText(
        awardDetailsBean.getMitAwardNumber());
        
        /*To set the Comment Type*/
        commentsHistoryForm.lblComments.setText (
        commentTypeBean.getDescription()+" ( " +commentTypeBean.getCommentCode() +" )" );
        
        awardCommentsBean.setMitAwardNumber (awardDetailsBean.getMitAwardNumber());
        awardCommentsBean.setCommentCode (commentTypeBean.getCommentCode());
        
        /*Temp Code*/
        try{
            getHistoryData(awardCommentsBean);
        }catch (CoeusUIException coeusUIException){
            CoeusOptionPane.showErrorDialog(coeusUIException.getMessage());
            coeusUIException.printStackTrace ();
            
        }
        selectedSeq = cvHistoryData.size() - 1;
//        System.out.println(selectedSeq);
        if (selectedSeq == 0){
            awardCommentsBean = (AwardCommentsBean)cvHistoryData.get(selectedSeq);
            commentsHistoryForm.txtArComments.setText(awardCommentsBean.getComments());
            commentsHistoryForm.txtArComments.setCaretPosition(0);
            commentsHistoryForm.btnPrevious.setEnabled(false);
        }
        
        awardCommentsBean = (AwardCommentsBean)cvHistoryData.get(selectedSeq);
        commentsHistoryForm.txtArComments.setText(awardCommentsBean.getComments());
        commentsHistoryForm.txtArComments.setCaretPosition(0);
//        selectedSeq--;
    }
    
    /** Validate the form data/Form and returns true if
     * validation is through else returns false.
     * @throws CoeusUIException if some exception occurs or some validation fails.
     * @return true if
     * validation is through else returns false.
     */
    public boolean validate () 
    throws edu.mit.coeus.exception.CoeusUIException {
            return true;
    }
    
    /** This method triggers all actions based on the event occured
     * @param actionEvent takes the actionEvent 
     */
    public void actionPerformed (ActionEvent actionEvent) {
        Object source = actionEvent.getSource ();
        if( source.equals(commentsHistoryForm.btnClose)) {
           dlgCommentsHistoryController.dispose();
        }else if( source.equals(commentsHistoryForm.btnNext) ){
            dlgCommentsHistoryController.setCursor(new Cursor(Cursor.WAIT_CURSOR));
            showAwardDetails(NEXT);
            dlgCommentsHistoryController.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        }else if( source.equals(commentsHistoryForm.btnPrevious) ){
            dlgCommentsHistoryController.setCursor(new Cursor(Cursor.WAIT_CURSOR));
            showAwardDetails(PREVIOUS);
            dlgCommentsHistoryController.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        }
    }

    /** Displays the award details for the next award in the Award List screen
     * @param navigation holds "N" or "P", indicating Next or Previous, respectively
     */
    private void showAwardDetails(String navigation){
        if( navigation.equals(NEXT) ){
            selectedSeq++;
        }else if( navigation.equals(PREVIOUS) ){
            selectedSeq--;
        }
        
        /*Enable/Disble Previous/Next Button 
         *depending upon the number of Comments*/
        
        if(selectedSeq < cvHistoryData.size()-1){
            commentsHistoryForm.btnNext.setEnabled(true);
        }
        else{
            commentsHistoryForm.btnNext.setEnabled(false);
        }
        if(selectedSeq > 0 ){
            commentsHistoryForm.btnPrevious.setEnabled(true);
        }else{
            commentsHistoryForm.btnPrevious.setEnabled(false);
        }
        awardCommentsBean = (AwardCommentsBean)cvHistoryData.get(selectedSeq);
      
        commentsHistoryForm.txtArComments.setText(awardCommentsBean.getComments());
        commentsHistoryForm.txtArComments.setCaretPosition(0);
    }
    
    /** Method to clean all objects */
    public void cleanUp() {
        commentsHistoryForm = null;
        awardDetailsBean = null;
        awardCommentsBean = null;
        mdiForm = null;
        dlgCommentsHistoryController = null;
        queryEngine = null;
        cvComments = null;
        cvHistoryData = null;
    }
}