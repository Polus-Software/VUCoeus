/**
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */


package edu.mit.coeus.award.controller;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;

import edu.mit.coeus.award.gui.*;
import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.gui.*;
import edu.mit.coeus.award.gui.SpecialRateForm;
import edu.mit.coeus.award.bean.*;
import edu.mit.coeus.utils.*;
import edu.mit.coeus.utils.query.*;
import edu.mit.coeus.exception.*;
import edu.mit.coeus.gui.event.*;
import edu.mit.coeus.bean.*;
import edu.mit.coeus.award.bean.*;
import java.text.DecimalFormat;
import java.text.ParseException;
import edu.ucsd.coeus.personalization.controller.AbstractController;


/**
 *
 * @author  surekhan
 */
public class SpecialRateController extends AwardController
implements ActionListener{
    
    /** Holds an instance of <CODE>SpecialRates Form</CODE> */
    private SpecialRateForm specialRateForm;
    
    private QueryEngine queryEngine;
    private AwardCommentsBean awardCommentsBean;
    private AwardHeaderBean awardHeaderBean;
    private CoeusDlgWindow dlgSpecialRate;
    private CoeusAppletMDIForm mdiForm = CoeusGuiConstants.getMDIForm();
    
    /** Holds CoeusMessageResources instance used for reading message Properties.
     */
    private CoeusMessageResources coeusMessageResources =
    CoeusMessageResources.getInstance();
    private static final String ENTER_POSITIVE_VALUE = "Please enter a positive value less than 999.99";
    
    /**Inavlid EB Rate Pair
     **/
    private static final String INVALID_EBRATE = "awardSpecialRate_exceptionCode.1901";
    
    private static final String SAVE_CHANGES = "saveConfirmCode.1002";
    
    /**Parameter value missing in OSP$PARAMETER 
     **/
     private static final String PARAMETER = "awardSpecialRate_exceptionCode.1902";
    
    
    private static final String WINDOW_TITLE  =  "Special Rate";
    private static final int WIDTH = 500;
    private static final int HEIGHT = 300;
    private static final java.awt.Color PANEL_BACKGROUND_COLOR =
    (Color) UIManager.getDefaults().get("Panel.background");
    private static final String COMMENT_CODE_FIELD = "commentCode";
    private int commentCode;
    private Double onCampusRate, offCampusRate;
    private CoeusVector cvRates;
    private CoeusVector cvCode;
    private static final char GET_SPECIAL_RATE = 'Q';
    private static final String AWARD_SERVLET = "/AwardMaintenanceServlet";
    private static final String connectTo = CoeusGuiConstants.CONNECTION_URL +
    AWARD_SERVLET;
     /** Creates a new instance of SpecialRate Controller */
    public SpecialRateController(AwardBaseBean awardBaseBean, char functionType) {
        super(awardBaseBean);
        this.mdiForm = mdiForm;
        cvCode = new CoeusVector();
        postInitComponents();
        queryEngine = QueryEngine.getInstance();
        setFunctionType(functionType);
        setFormData(null);
        registerComponents();
        formatFields();
    }
    
    
    private void postInitComponents(){
        specialRateForm = new SpecialRateForm();
        dlgSpecialRate = new CoeusDlgWindow(mdiForm);
        dlgSpecialRate.setResizable(false);
        dlgSpecialRate.setModal(true);
        dlgSpecialRate.getContentPane().add(specialRateForm);
        dlgSpecialRate.setFont(CoeusFontFactory.getLabelFont());
        dlgSpecialRate.setDefaultCloseOperation(CoeusDlgWindow.DO_NOTHING_ON_CLOSE);
        dlgSpecialRate.setSize(WIDTH, HEIGHT);
        
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension dlgSize = dlgSpecialRate.getSize();
        dlgSpecialRate.setLocation(screenSize.width/2 - (dlgSize.width/2),
        screenSize.height/2 - (dlgSize.height/2));
        
        dlgSpecialRate.addEscapeKeyListener(
        new AbstractAction("escPressed"){
            public void actionPerformed(ActionEvent ae){
                performWindowClosing();
                return;
            }
        });
        
        dlgSpecialRate.addWindowListener(new WindowAdapter(){
            
            public void windowClosing(WindowEvent we){
                performWindowClosing();
            }
        });
        //code for disposing the window ends
        
    }
    
    /** Displays the Form which is being controlled.
     */
    public void display() {
    	//    	rdias - UCSD's coeus personalization - Begin
    	AbstractController persnref = AbstractController.getPersonalizationControllerRef();
        persnref.customize_Form(getControlledUI(),"GENERIC");
        //		rdias - UCSD's coeus personalization - End
    	
        try{
            cvCode = queryEngine.executeQuery(queryKey,CoeusParameterBean.class,CoeusVector.FILTER_ACTIVE_BEANS);
            if(cvCode != null){
                CoeusVector cvSpecialRateCode = cvCode.filter(new Equals("parameterName", CoeusConstants.SPECIAL_RATE_COMMENT_CODE));
                if(cvSpecialRateCode == null || cvSpecialRateCode.size() == 0){
                    CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(PARAMETER));
                    return ;
                    
                }else {
                     //Added with COEUSDEV 293:4.4 QA - Dev2 Instance -missing parameter warning - but it is maintained
                     CoeusParameterBean param = (CoeusParameterBean)cvSpecialRateCode.get(0);
                     commentCode = Integer.parseInt(param.getParameterValue());
                     
                     CoeusVector cvAwardData = queryEngine.executeQuery(queryKey,
                             AwardCommentsBean.class, CoeusVector.FILTER_ACTIVE_BEANS);
                     
                     Equals eqComments = new Equals(COMMENT_CODE_FIELD, new Integer(commentCode));
                     if( cvAwardData != null && cvAwardData.size() > 0 ){
                         CoeusVector cvFilterdComment = cvAwardData.filter(eqComments);
                         if( cvFilterdComment != null && cvFilterdComment.size() > 0 ) {
                             awardCommentsBean = (AwardCommentsBean)cvFilterdComment.get(0);
                             specialRateForm.txtArSpecialRateComments.setText(awardCommentsBean.getComments());
                         }
                     }
                     dlgSpecialRate.show();
                    //COEUSDEV 293: End
                }
            }
        }catch(CoeusException coeusException){
            coeusException.printStackTrace();
        }
        
    }
    /** Perform field formatting.
     * enabling, disabling components depending on the different conditions
     */
    public void formatFields() {
        if( getFunctionType() == TypeConstants.DISPLAY_MODE ){
            specialRateForm.btnOK.setEnabled(false);
            specialRateForm.btnCancel.setEnabled(true);
            specialRateForm.btnRates.setEnabled(true);
            specialRateForm.txtArSpecialRateComments.setEditable(false);
            specialRateForm.txtOnCampus.setEditable(false);
            specialRateForm.txtOffCampus.setEditable(false);
            specialRateForm.txtArSpecialRateComments.setBackground(PANEL_BACKGROUND_COLOR);
        }
    }
    /** An overridden method of the controller
     * @return SpecialRateForm returns the controlled form component
     */
    public java.awt.Component getControlledUI()  {
        return specialRateForm;
    }
    /** Returns the form data
     * @return returns the form data
     */
    public Object getFormData() {
        return null;
    }
    /** This method is used to set the listeners to the components.
     */
    public void registerComponents() {
        /** Code for focus traversal - start */
        java.awt.Component[] components = { specialRateForm.txtOnCampus,specialRateForm.txtOffCampus,
        specialRateForm.txtArSpecialRateComments,specialRateForm.btnOK,
        specialRateForm.btnCancel,
        specialRateForm.btnRates};
        ScreenFocusTraversalPolicy traversePolicy = new ScreenFocusTraversalPolicy( components );
        specialRateForm.setFocusTraversalPolicy(traversePolicy);
        specialRateForm.setFocusCycleRoot(true);
        /** code for focus traversal - ends */
        
        //Add listeners to all the buttons
        specialRateForm.btnOK.addActionListener(this);
        specialRateForm.btnCancel.addActionListener(this);
        specialRateForm.btnRates.addActionListener(this);
        
        dlgSpecialRate.addComponentListener(
        new ComponentAdapter(){
            public void componentShown(ComponentEvent e){
                requestDefaultFocus();
            }
        });
    }
    
    /** To set the default focus for the component
     */
    private void requestDefaultFocus(){
        if( getFunctionType() == TypeConstants.DISPLAY_MODE ){
            specialRateForm.btnCancel.requestFocusInWindow();
        }else {
            specialRateForm.txtOnCampus.requestDefaultFocus();
        }
    }
    
    /** Saves the Form Data.
     */
    public void saveFormData(){
        try{
            if( isOnOffCampusModified() || isCommentsModified() ){
                saveData();
            }
            close();
        }catch(CoeusUIException cUiEx){
            CoeusOptionPane.showErrorDialog(cUiEx.getMessage());
        }
    }
    
    /** If any of the data in the fields is modified ,it is saved and gets
     *updated to the query engine */
    private void saveData() throws CoeusUIException{
        if( isOnOffCampusModified() ){
            awardHeaderBean.setAcType(TypeConstants.UPDATE_RECORD);
            /*
             *Changing datatype double to Double Object
             */
//            awardHeaderBean.setSpecialEBRateOnCampus(onCampusRate);
//            awardHeaderBean.setSpecialEBRateOffCampus(offCampusRate);
            awardHeaderBean.setSpecialEBRateOnCampus(onCampusRate);
            awardHeaderBean.setSpecialEBRateOffCampus(offCampusRate);
            
            //Update to query engine
            try{
                queryEngine.update(queryKey, awardHeaderBean);
                //fire to other controllers that this bean has benn modified
                BeanEvent beanEvent = new BeanEvent();
                beanEvent.setBean(awardHeaderBean);
                beanEvent.setSource(this);
                fireBeanUpdated(beanEvent);
                
            }catch (CoeusException coeusException){
                coeusException.printStackTrace();
                throw new CoeusUIException(coeusException.getMessage());
            }
        }
        if( isCommentsModified() ){
            if( awardCommentsBean != null ){
                awardCommentsBean.setAcType(TypeConstants.UPDATE_RECORD);
                awardCommentsBean.setComments(specialRateForm.txtArSpecialRateComments.getText());
                
                //Update to query engine
                try{
                    queryEngine.update(queryKey, awardCommentsBean);
                }catch (CoeusException coeusException){
                    coeusException.printStackTrace();
                    throw new CoeusUIException(coeusException.getMessage());
                }
            }else{
                AwardCommentsBean awardCommentsBean = new AwardCommentsBean();
                awardCommentsBean.setAcType(TypeConstants.INSERT_RECORD);
                awardCommentsBean.setComments(
                specialRateForm.txtArSpecialRateComments.getText().trim());
                awardCommentsBean.setCommentCode(commentCode);
                awardCommentsBean.setMitAwardNumber(awardBaseBean.getMitAwardNumber());
                awardCommentsBean.setSequenceNumber(awardBaseBean.getSequenceNumber());
                awardCommentsBean.setCheckListPrintFlag(false);
                
                //Insert to the query engine
                queryEngine.insert(queryKey, awardCommentsBean);
            }
        }
    }
    
    private void close(){
        dlgSpecialRate.dispose();
    }
    
    /** This method is used to set the form data specified in
     * <CODE> data </CODE>
     * @param data data to set to the form
     */
    
    public void setFormData(Object data) {
        CoeusVector cvAwardData = new CoeusVector();
        awardHeaderBean = new AwardHeaderBean();
        AwardDetailsBean awardDetailsBean = new AwardDetailsBean();
        CoeusVector cvFilterdComment = null;
        commentCode = 7;
        try{
            cvAwardData = queryEngine.executeQuery(queryKey,
            AwardDetailsBean.class, CoeusVector.FILTER_ACTIVE_BEANS);
            
            awardDetailsBean = (AwardDetailsBean)cvAwardData.get(0);
            
            cvAwardData = queryEngine.executeQuery(queryKey,
            AwardHeaderBean.class, CoeusVector.FILTER_ACTIVE_BEANS);
            
            awardHeaderBean = (AwardHeaderBean)cvAwardData.get(0);
            //Commented with COEUSDEV 293:4.4 QA - Dev2 Instance -missing parameter warning - but it is maintained
            //Moved this code to display method
//            cvAwardData = queryEngine.executeQuery(queryKey,
//            AwardCommentsBean.class, CoeusVector.FILTER_ACTIVE_BEANS);
//            
//            Equals eqComments = new Equals(COMMENT_CODE_FIELD, new Integer(commentCode));
//            if( cvAwardData != null && cvAwardData.size() > 0 ){
//                cvFilterdComment = cvAwardData.filter(eqComments);
//                if( cvFilterdComment != null && cvFilterdComment.size() > 0 ) {
//                    awardCommentsBean = (AwardCommentsBean)cvFilterdComment.get(0);
//                    specialRateForm.txtArSpecialRateComments.setText(
//                    awardCommentsBean.getComments());
//                }
//            }
            //COEUSDEV 293 End
        }catch (CoeusException coeusException){
            coeusException.printStackTrace();
        }
        
        specialRateForm.lblSponsorAwardNumberValue.setText(
        awardDetailsBean.getSponsorAwardNumber());
        specialRateForm.lblAwardNumberValue.setText(
        awardDetailsBean.getMitAwardNumber());
        //Case #2336 start
        specialRateForm.lblSequenceNumberValue.setText(EMPTY+awardBaseBean.getSequenceNumber());
        //specialRateForm.lblSequenceNumberValue.setText(EMPTY+awardDetailsBean.getSequenceNumber());
        //Case #2336 end
        specialRateForm.txtOnCampus.setText(awardHeaderBean.getSpecialEBRateOnCampus()==null?EMPTY:awardHeaderBean.getSpecialEBRateOnCampus().toString().trim());
        specialRateForm.txtOffCampus.setText(awardHeaderBean.getSpecialEBRateOffCampus()==null?EMPTY:awardHeaderBean.getSpecialEBRateOffCampus().toString().trim());
        dlgSpecialRate.setTitle(WINDOW_TITLE);
        
    }
    private Double checkFormat(String str) throws CoeusException,NumberFormatException{
        Double dd =  str.length()==0?null:new Double(str);
        if(dd==null)
            return dd;
        if(dd.doubleValue()<0 || dd.doubleValue()>999.99){
            throw new CoeusException("Invalid Entry");
        }
        return dd;
    }
    public boolean validate() throws edu.mit.coeus.exception.CoeusUIException {
        try{
            try{
                
                onCampusRate = checkFormat(specialRateForm.txtOnCampus.getText());
                offCampusRate = checkFormat(specialRateForm.txtOffCampus.getText());
            }catch (CoeusException nFEx){
                nFEx.printStackTrace();
                //coeusMessageResources.parseMessageKey("awardSpecialRate_exceptionCode.1902"), should be replaced with this message
                //And add the key in messages.properties file
                throw new CoeusUIException("Invalid EB rate");
            }catch (NumberFormatException nFEx){
                nFEx.printStackTrace();
                //coeusMessageResources.parseMessageKey("awardSpecialRate_exceptionCode.1902"), should be replaced with this message
                //And add the key in messages.properties file
                throw new CoeusUIException("Invalid EB rate");
            }
            
            //       double on_campus_rate = awardHeaderBean.getSpecialEBRateOnCampus();
            //       double off_campus_rate = awardHeaderBean.getSpecialEBRateOffCampus();
        //        if(on_campus_rate > 999.99 || off_campus_rate > 999.99 ||
            //        on_campus_rate < 0 || off_campus_rate < 0) {
            //            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(ENTER_POSITIVE_VALUE));
            //            return true;
            //        }
            String parameterValue;
            CoeusVector cvParameter = new CoeusVector();
            CoeusVector cvParameterValue = new CoeusVector();
            cvParameter = queryEngine.executeQuery(queryKey,CoeusParameterBean.class,CoeusVector.FILTER_ACTIVE_BEANS);
            if (cvParameter != null) {
                cvParameterValue = cvParameter.filter(new Equals("parameterName",
                "MIT_IDC_VALIDATION_ENABLED"));
                if (cvParameterValue != null && cvParameterValue.size() > 0) {
                    // for(int index = 0; index < cvParameterValue.size(); index++){
                    CoeusParameterBean parameterBean = (CoeusParameterBean)cvParameterValue.get(0);
                    parameterValue = parameterBean.getParameterValue();
                    if (parameterValue != null && parameterValue.trim().equals("1")) {
                       if(!getDataFromServer()){
                            CoeusOptionPane.showInfoDialog("Invalid EB Rate Pair");
                            specialRateForm.txtOnCampus.requestFocusInWindow();
                            return false;
                        }
                    }else{
                        return true;
                    }
                    
                }
            }
        }catch(CoeusException coeusException){
            coeusException.printStackTrace();
            throw new CoeusUIException(coeusException.getMessage());
        }
        
        return true;
    }
    
   
    
    
    
    /**
     * To make the class level instance as null
     */
    public void cleanUp() {
        specialRateForm = null;
        awardCommentsBean = null;
        awardHeaderBean = null;
        dlgSpecialRate = null;
        cvRates = null;
    }
    
    /** This method triggers all actions based on the event occured
     * @param actionEvent takes the actionEvent */
    
    public void actionPerformed(ActionEvent actionEvent) {
        Object source = actionEvent.getSource();
        if( source.equals(specialRateForm.btnOK)) {
            try{
                if(validate()){
                    saveFormData();
                }
            }catch(CoeusUIException coeusUIException){
                coeusUIException.printStackTrace();
                CoeusOptionPane.showErrorDialog(coeusUIException.getMessage());
            }
        }else if(source.equals(specialRateForm.btnCancel)) {
            //close();
            performWindowClosing();
        }else if(source.equals(specialRateForm.btnRates)) {
            showValidRates();
        }
    }
    
    private void showValidRates(){
        ValidRatesController validRatesController = new ValidRatesController(
        awardBaseBean);
        validRatesController.display();
        
    }
    
    private boolean isOnOffCampusModified(){
        if( (awardHeaderBean.getSpecialEBRateOnCampus()==null && 
            !specialRateForm.txtOnCampus.getText().trim().equals("")) ||
            (awardHeaderBean.getSpecialEBRateOffCampus()==null) && 
            !(specialRateForm.txtOffCampus.getText().trim().equals(""))){
            return true;
        }
        try{
            onCampusRate = new Double( specialRateForm.txtOnCampus.getText() );
            offCampusRate =new Double( specialRateForm.txtOffCampus.getText() );
        }catch(NumberFormatException nFEx){
//            throw new CoeusUIException("Invalid EB Rate");
//            CoeusOptionPane.showErrorDialog("Invalid EB Rate");
//            return false;
        }

        /*
         *  Changed the datattype to Double to double
         */
//        if( ! (awardHeaderBean.getSpecialEBRateOnCampus() == onCampusRate ) ||
//        !(awardHeaderBean.getSpecialEBRateOffCampus() == offCampusRate) ){
        if( (awardHeaderBean.getSpecialEBRateOnCampus()!=null && !awardHeaderBean.getSpecialEBRateOnCampus(). equals(onCampusRate)  )||
            (awardHeaderBean.getSpecialEBRateOffCampus()!=null && !awardHeaderBean.getSpecialEBRateOffCampus().equals(offCampusRate) )){
            return true;
        }else{
            return false;
        }
    }
    
    private boolean isCommentsModified(){
        if ( awardCommentsBean == null && specialRateForm.txtArSpecialRateComments.getText().trim().length() > 0){
            return true;
        }
        if( awardCommentsBean != null && !specialRateForm.txtArSpecialRateComments.getText().trim().equals(
        awardCommentsBean.getComments()) ){
            return true;
        }else{
            return false;
        }
        
    }
    
    private void performWindowClosing(){
        
        if(getFunctionType()!=TypeConstants.DISPLAY_MODE && (isCommentsModified() || isOnOffCampusModified())  ) {
            int option = CoeusOptionPane.showQuestionDialog(
            coeusMessageResources.parseMessageKey(SAVE_CHANGES),
            CoeusOptionPane.OPTION_YES_NO_CANCEL,
            JOptionPane.YES_OPTION);
            switch( option ) {
                case (JOptionPane.YES_OPTION ):
                    setSaveRequired(true);
                    try{
                        
                        if(validate()){
                            saveData();
                            close();
                        }
                    }catch(Exception exception){
                        exception.printStackTrace();
                        CoeusOptionPane.showErrorDialog(exception.getMessage());
                    }
                    break;
                case(JOptionPane.NO_OPTION ):
                    close();
                    break;
                default:
                    break;
            }
        }else{
            close();
        }
        
    }
    
    public boolean getDataFromServer(){
        if(onCampusRate==null && offCampusRate==null)
            return true;
        cvRates = new CoeusVector();
        RequesterBean requesterBean = new RequesterBean();
        ResponderBean responderBean = new ResponderBean();
        cvRates.add(onCampusRate);
        cvRates.add(offCampusRate);
        requesterBean.setFunctionType(GET_SPECIAL_RATE);
        requesterBean.setDataObject(cvRates);
        AppletServletCommunicator comm= new AppletServletCommunicator(connectTo,
        requesterBean);
        comm.setRequest(requesterBean);
        comm.send();
        responderBean = comm.getResponse();
        if(responderBean.isSuccessfulResponse()) {
            Boolean blnRates = (Boolean)responderBean.getDataObject();
            return blnRates.booleanValue();
        }else {
            return false;
        }
    }
    
//    private class CustomFocusAdapter extends FocusAdapter {
//        double onCampusRate = Double.parseDouble(specialRateForm.txtOnCampus.getText());
//        double offCampusRate = Double.parseDouble(specialRateForm.txtOffCampus.getText());
//        public void focusGained(FocusEvent fe){
//        }
//        public void focusLost(FocusEvent fe) {
//            if(fe.getSource() == specialRateForm.txtOnCampus ){
//                if( onCampusRate > 999.99 )  {
//                    CoeusOptionPane.showInfoDialog(
//                    coeusMessageResources.parseMessageKey(ENTER_POSITIVE_VALUE));
//                    specialRateForm.txtOnCampus.requestFocus();
//                    return ;
//                }
//            }else if( fe.getSource() == specialRateForm.txtOffCampus) {
//                if( offCampusRate > 999.99 ){
//                    CoeusOptionPane.showInfoDialog(
//                    coeusMessageResources.parseMessageKey(ENTER_POSITIVE_VALUE));
//                    specialRateForm.txtOffCampus.requestFocus();
//                    return ;
//                }
//            }
//        }
//    }
}






