/*
 * TemplateCommentsController.java
 *
 * Created on December 17, 2004, 7:18 PM
 */
/* PMD check performed, and commented unused imports and variables on 2-Sept-2010
 * by Keerthy Jayaraj
 */
package edu.mit.coeus.admin.controller;

import edu.mit.coeus.admin.bean.AwardTemplateBean;
import edu.mit.coeus.admin.bean.AwardTemplateCommentsBean;
import edu.mit.coeus.admin.bean.TemplateBaseBean;
import edu.mit.coeus.award.gui.AwardCommentsForm;
import edu.mit.coeus.gui.*;
import edu.mit.coeus.gui.event.*;
import edu.mit.coeus.utils.*;
import edu.mit.coeus.utils.query.*;
import edu.mit.coeus.award.bean.*;
import edu.mit.coeus.exception.*;
import edu.mit.coeus.bean.CommentTypeBean;
import edu.mit.coeus.brokers.*;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;
import java.awt.event.*;
import java.awt.*;
import java.util.Hashtable;

/**
 *
 * @author  ajaygm
 */
public class TemplateCommentsController extends AwardTemplateController 
implements ActionListener, ListSelectionListener,Observer/*, BeanUpdatedListener */{
    
    private AwardCommentsForm awardCommentsForm;
    
    private static final String COMMENT_CODE_FIELD = "commentCode";
    private static final String DESCRIPTION_FIELD = "description";
    
//    private static final String CONFIRM_SYNC = "awardComments_exceptionCode.1404";
    private static final String REMOVE_SPACES = "awardTemplateExceptionCode.1606";
    private static final String OPENING_BRACE = " ( ";
    private static final String CLOSING_BRACE = " )";
    private static final java.awt.Color PANEL_BACKGROUND_COLOR =
    (Color) UIManager.getDefaults().get("Panel.background");
    
    private static final char IS_COMMENTS_HAS_HISTORY = 'K';
    private static final String CONNECTION_STRING = CoeusGuiConstants.CONNECTION_URL +
    "/AwardMaintenanceServlet";
    
    //    private AwardBaseBean  awardBaseBean;
    private TemplateBaseBean templateBaseBean;
    private QueryEngine queryEngine;
    private CoeusVector cvCommentType;
    private CoeusVector cvComment;
    private CoeusVector cvFilterdComment;
    //    private boolean initialLoad;
    
    private ColumnTableModel columnTableModel;
    private ColumnTableRenderer columnTableRenderer;
    //    private AwardCommentsBean  awardCommentsBean;
//    private TemplateCommentsBean templateCommentsBean;
    private AwardTemplateCommentsBean awardTemplateCommentsBean;
    private CommentTypeBean commentTypeBean;
    private CoeusMessageResources  coeusMessageResources;
    private int lastSelectedRow;
    private Equals eqComments;
    
    /** Stores the key value pairs of comment type code and boolean values
     * if history is data is present */
    private Hashtable htHistory;
    
    private static final int IMAGE_COLUMN = 0;
    private static final int TEXT_COLUMN = 1;
    //Added For COEUSDEV-562: Changes to comments in award templates not getting saved : Start
    private BaseWindowObservable observable;
    //COEUSDEV-562: End
     int count=0;
     boolean inside = false;
        
    /** Creates a new instance of TemplateComments */
    public TemplateCommentsController(TemplateBaseBean templateBaseBean, char functionType) {
        super(templateBaseBean);
        coeusMessageResources = CoeusMessageResources.getInstance();
        //Added For COEUSDEV-562: Changes to comments in award templates not getting saved : Start
        observable =new BaseWindowObservable();
        //Added For COEUSDEV-562: End
        queryEngine = QueryEngine.getInstance();
        cvComment = new CoeusVector();
        cvCommentType = new CoeusVector();
        cvFilterdComment = new CoeusVector();
        registerComponents();
        setFunctionType(functionType);
        setFormData(templateBaseBean);
        setColumnData();
    }
    
    /** To display the screen
     */
    public void display() {
    }
    
    /** Perform field formatting.
     * enabling, disabling components depending on the different conditions
     */
    public void formatFields() {
        awardCommentsForm.btnSync.setEnabled(false);
        awardCommentsForm.btnSync.setVisible(false);
        awardCommentsForm.btnHistory.setEnabled(false);
        awardCommentsForm.btnHistory.setVisible(false);
        //Added with case 2796: Sync To Parent
        awardCommentsForm.btnSyncToChildren.setEnabled(false);
        awardCommentsForm.btnSyncToChildren.setVisible(false);
        //2796 End
        if( getFunctionType() == TypeConstants.DISPLAY_MODE ){
            awardCommentsForm.txtArComments.setEditable(false);
            awardCommentsForm.txtArComments.setBackground(PANEL_BACKGROUND_COLOR);
        }else{
            awardCommentsForm.txtArComments.setBackground(Color.white);
            awardCommentsForm.txtArComments.setEditable(true);
        }
    }
    
    /** An overridden method of the controller
     * @return  returns the controlled form component
     */
    public java.awt.Component getControlledUI() {
        return awardCommentsForm;
    }
    
    /** Returns the form data
     * @return awardCommentsForm the form
     */
    public Object getFormData() {
        return awardCommentsForm;
    }
    
    /** This method is used to set the listeners to the components.
     */
    public void registerComponents() {
        awardCommentsForm = new AwardCommentsForm();
        awardCommentsForm.btnHistory.addActionListener(this);
        awardCommentsForm.btnSync.addActionListener(this);
        awardCommentsForm.tblCommentType.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        //awardCommentsForm.tblCommentType.getSelectionModel().addListSelectionListener(this);
        
        columnTableRenderer = new ColumnTableRenderer();
        columnTableModel = new ColumnTableModel();
        awardCommentsForm.tblCommentType.setModel(columnTableModel);
        
        //        addBeanUpdatedListener(this, AwardDetailsBean.class);
        //        addBeanUpdatedListener(this, AwardCommentsBean.class);
        
    }
    
    //    /** Method to perform some action when the beanUpdated event is triggered
    //     * here it sets the <CODE>refreshRequired</CODE> flag
    //     * @param beanEvent takes the beanEvent */
    //    public void beanUpdated(BeanEvent beanEvent) {
    //        if( beanEvent.getSource().getClass().equals(OtherHeaderController.class) ){
    //            if( beanEvent.getBean().getClass().equals(AwardDetailsBean.class)){
    //                setRefreshRequired(true);
    //            }
    //        }
    //        if( beanEvent.getBean().getClass().equals(AwardCommentsBean.class) ){
    //            setRefreshRequired(true);
    //            refresh();
    //        }
    //    }
    
    /** This method will refresh the form with the modified data */
    public void refresh(){
        if( !isRefreshRequired() ) return ;
        //Modified for COEUSQA-1456 : Templates-add User ID stamp & Timestamp - Start
        if(getFunctionType() == TypeConstants.COPY_MODE){
            setFunctionType(TypeConstants.MODIFY_MODE);
        }
        //Modified for COEUSQA-1456 : Templates-add User ID stamp & Timestamp - End
        setFormData(templateBaseBean);
        setRefreshRequired(false);
    }
    
    /** Method to clean up objects */
    public void cleanUp() {
        awardCommentsForm = null;
        templateBaseBean = null;
        queryEngine = null;
        cvCommentType = null;
        cvComment = null;
        cvFilterdComment = null;
        columnTableModel = null;
        columnTableRenderer = null;
        //        awardCommentsBean = null;
//        templateCommentsBean = null;
        awardTemplateCommentsBean = null;
        commentTypeBean = null;
        coeusMessageResources = null;
        eqComments = null;
        htHistory = null;
        //        removeBeanUpdatedListener(this, AwardDetailsBean.class);
        //        removeBeanUpdatedListener(this, AwardCommentsBean.class);
    }
    
    /** Saves the Form Data.
     */
    public void saveFormData() {
        
        commentTypeBean = (CommentTypeBean) cvCommentType.get(awardCommentsForm.tblCommentType.getSelectedRow());
        
        //Set the acType for the selected bean
        setAcTypes(commentTypeBean);
        //Added For COEUSQA-2518:CLONE -Award Template - Copy - Changes to invoice instructions and comments not saved - start
//        CoeusVector cvUpdatedComments = null;
//        try {
//            if(getFunctionType() == TypeConstants.COPY_MODE) {
//                cvUpdatedComments = queryEngine.executeQuery(queryKey,AwardTemplateCommentsBean.class, CoeusVector.FILTER_ACTIVE_BEANS);
//                if(cvUpdatedComments != null && cvUpdatedComments.size() > 0) {
//                    if(cvComment != null && cvComment.size() > 0) {
//                        cvComment.set(0, cvUpdatedComments.get(0));
//                    }
//                }
//            }
//        } catch(CoeusException exception) {
//            exception.printStackTrace();
//        }
        //Added For COEUSQA-2518:CLONE -Award Template - Copy - Changes to invoice instructions and comments not saved - end
        for( int index = 0; index < cvComment.size(); index++ ){
            AwardTemplateCommentsBean awardTemplateCommentsBean = (AwardTemplateCommentsBean)cvComment.get(index);
            if( awardTemplateCommentsBean.getAcType() == null ) continue;
            if( awardTemplateCommentsBean.getAcType().equals(TypeConstants.INSERT_RECORD) ){
                
                if( awardTemplateCommentsBean.getComments() == null ||
                awardTemplateCommentsBean.getComments().equals(EMPTY)){
                    /**
                     * Comments are null or empty
                     * (ie. if text is entered and cleared before saving), do nothing
                     */
                    //Modified For COEUSDEV-562: Changes to comments in award templates not getting saved : Start
                    if(getFunctionType() == TypeConstants.COPY_MODE){
                        try {
                            queryEngine.delete(queryKey,AwardTemplateCommentsBean.class,awardTemplateCommentsBean);
                        } catch (CoeusException ex) {
                            ex.printStackTrace();
                        }
                    }else continue;
                    //COEUSDEV-562: End
                }else{
                    /**
                     * New record to be inserted
                     * Set the mit award number and insert to the query engine
                     */
                    /*@todo*/     //               awardCommentsBean.setMitAwardNumber(awardBaseBean.getMitAwardNumber());
                    awardTemplateCommentsBean.setTemplateCode(templateBaseBean.getTemplateCode());
                    queryEngine.insert(queryKey, awardTemplateCommentsBean);
                }
                
            }else if( awardTemplateCommentsBean.getAcType().equals(TypeConstants.UPDATE_RECORD) ){
                
                //Update the query engine
                try{
                    awardTemplateCommentsBean.setTemplateCode(templateBaseBean.getTemplateCode());
                    queryEngine.update(queryKey, awardTemplateCommentsBean);
                }catch (CoeusException coeusException){
                    coeusException.printStackTrace();
                }
            }
        }
        //Added For COEUSDEV-562: Changes to comments in award templates not getting saved : Start
        observable.notifyObservers();
        //COEUSDEV-562:End
        setSaveRequired(false);
    }
    
    /** This method is used to set the form data specified in
     * <CODE> templateBaseBean </CODE>
     * @param templateBaseBean data to set to the form
     */
    public void setFormData(Object  templateBaseBean ) {
        htHistory = new Hashtable();
        try{
            this.templateBaseBean = (TemplateBaseBean)templateBaseBean;
            cvCommentType = queryEngine.executeQuery(queryKey,CommentTypeBean.class, CoeusVector.FILTER_ACTIVE_BEANS);
            if(cvCommentType!= null && cvCommentType.size() > 0){
                cvCommentType.sort(DESCRIPTION_FIELD);
                Equals eqTemplateFlag = new Equals("templateFlag", true);
                cvCommentType = cvCommentType.filter(eqTemplateFlag);
            }
            columnTableModel.setData(cvCommentType);
            awardCommentsForm.tblCommentType.repaint();
            
            cvComment = queryEngine.executeQuery(queryKey,AwardTemplateCommentsBean.class, CoeusVector.FILTER_ACTIVE_BEANS);
            
            //Added for COEUSQA-1456 : Templates-add User ID stamp & Timestamp - Start
            //Sets uypdate time and update user in the UI
            try {
                CoeusVector cvUpdateDetails = queryEngine.executeQuery(queryKey, "COMMENTS_TEMPLATE_UPDATE_DETAIL", CoeusVector.FILTER_ACTIVE_BEANS);
                if(cvUpdateDetails != null && cvUpdateDetails.size() > 0) {
                    AwardTemplateBean updateDetail = (AwardTemplateBean)cvUpdateDetails.get(0);
                    if(getFunctionType() != TypeConstants.COPY_MODE &&
                            updateDetail.getUpdateTimestamp() != null){
                        String lastUpdate = CoeusDateFormat.format(updateDetail.getUpdateTimestamp().toString());
                        String updateUserName = updateDetail.getUpdateUserName();
                        awardCommentsForm.pnlUpdateDetails.setVisible(true);
                        awardCommentsForm.pnlUpdateDetails1.setVisible(true);
                        awardCommentsForm.txtLastUpdate1.setText(lastUpdate);
                        awardCommentsForm.txtUpdateUser1.setText(updateUserName);
                    }else{
                        awardCommentsForm.txtLastUpdate1.setText(CoeusGuiConstants.EMPTY_STRING);
                        awardCommentsForm.txtUpdateUser1.setText(CoeusGuiConstants.EMPTY_STRING);
                    }
                }else{
                    awardCommentsForm.pnlUpdateDetails.setVisible(false);
                    awardCommentsForm.pnlUpdateDetails1.setVisible(false);
                }
            } catch(Exception e) {
                e.printStackTrace();
            }
            //COEUSQA-1456 : End
    
            if( awardCommentsForm.tblCommentType.getRowCount() != 0 ){
                //                initialLoad = true;
                
                //To refresh the comments for the selected row after sync
                if( awardCommentsForm.tblCommentType.getSelectedRow() != -1 ){
                    setAwardComments(awardCommentsForm.tblCommentType.getSelectedRow());
                }
                
                //Select the first row by default
                awardCommentsForm.tblCommentType.setRowSelectionInterval(0,0);
                
                //To refresh the comments displayed for the first row
                setAwardComments(awardCommentsForm.tblCommentType.getSelectedRow());
                
                //Add the listener after setting the comments for the first row
                awardCommentsForm.tblCommentType.getSelectionModel().removeListSelectionListener(this);
                awardCommentsForm.tblCommentType.getSelectionModel().addListSelectionListener(this);
            }
            
            if( cvCommentType != null && cvCommentType.size() > 0 ){
                CommentTypeBean commentTypeBean = (CommentTypeBean)cvCommentType.get(0);
                AwardTemplateCommentsBean awardTemplateCommentsBean= new AwardTemplateCommentsBean();
                /*@todo*/ //                awardCommentsBean.setMitAwardNumber(this.awardBaseBean.getMitAwardNumber());
                awardTemplateCommentsBean.setCommentCode(commentTypeBean.getCommentCode());
//                try{
////                    awardCommentsForm.btnHistory.setEnabled( enableHistory(awardTemplateCommentsBean) );
//                }catch (CoeusClientException coeusClientException){
//                    coeusClientException.printStackTrace();
//                }
            }
        }catch (CoeusException exception){
            exception.printStackTrace();
        }
    }
    
    public boolean validate() throws edu.mit.coeus.exception.CoeusUIException {
        //Comments are null or empty(ie. if text is entered and cleared before saving)
        
        String strComments = awardCommentsForm.txtArComments.getText();
        if(strComments.length() > 0 && strComments.trim().equals(EMPTY)){
            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(REMOVE_SPACES));
            awardCommentsForm.txtArComments.selectAll();
//            awardCommentsForm.txtArComments.setText(EMPTY);
            awardCommentsForm.txtArComments.requestFocus();
            
            return false;
        }else{
            return true;
        }
    }
    
    /** This method triggers all actions based on the event occured
     * @param actionEvent takes the actionEvent
     */
    public void actionPerformed(ActionEvent actionEvent) {
        Object source = actionEvent.getSource();
        if( source.equals(awardCommentsForm.btnSync) ){
            //            performSyncOperation();
        }else if( source.equals(awardCommentsForm.btnHistory) ){
            showHistory();
        }
    }
    
    /** To display the History screen
     */
    private void showHistory(){
        //Get the CommentTypeBean for the selected commentType
        int selectedRow = awardCommentsForm.tblCommentType.getSelectedRow();
        CommentTypeBean commentTypeBean = (CommentTypeBean)cvCommentType.get(selectedRow);
        
        //Filter and get the awardCommentsBean for the selected comment code
        eqComments = new Equals(COMMENT_CODE_FIELD, new Integer(commentTypeBean.getCommentCode()));
        cvFilterdComment = cvComment.filter(eqComments);
//        AwardCommentsBean awardCommentsBean = (AwardCommentsBean)cvFilterdComment.get(0);
        
        //       CommentsHistoryController commentsHistoryController = new CommentsHistoryController(
        //       awardBaseBean, commentTypeBean, awardCommentsBean);
        //       commentsHistoryController.display();
    }
    
    //    private void performSyncOperation(){
    //        int option = CoeusOptionPane.showQuestionDialog(
    //        coeusMessageResources.parseMessageKey(CONFIRM_SYNC),
    //        CoeusOptionPane.OPTION_YES_NO, CoeusOptionPane.DEFAULT_YES);
    //
    //        switch( option ){
    //            case (JOptionPane.YES_OPTION ):
    //                //Call the Sync Comments of the AwardController
    //                if ( syncComments(EMPTY, getTemplateCode()) ){
    //                    setFormData(awardBaseBean);
    //                    setSaveRequired(true);
    //                }
    //                break;
    //            case (JOptionPane.NO_OPTION ):
    //                break;
    //            default:
    //                break;
    //        }
    //    }
    
    private void setColumnData(){
        awardCommentsForm.tblCommentType.setRowHeight(22);
        awardCommentsForm.tblCommentType.setShowHorizontalLines(false);
        awardCommentsForm.tblCommentType.setShowVerticalLines(false);
        
        TableColumn column = awardCommentsForm.tblCommentType.getColumnModel().getColumn(IMAGE_COLUMN);
        column.setMaxWidth(15);
        column.setMinWidth(15);
        column.setPreferredWidth(15);
        column.setCellRenderer(columnTableRenderer);
        column.setHeaderRenderer(new EmptyHeaderRenderer());
        
        column = awardCommentsForm.tblCommentType.getColumnModel().getColumn(TEXT_COLUMN);
        column.setCellRenderer(columnTableRenderer);
        column.setHeaderRenderer(new EmptyHeaderRenderer());
        
    }
    
    private void setAcTypes(CommentTypeBean commentTypeBean){
        
        eqComments = new Equals(COMMENT_CODE_FIELD, new Integer(commentTypeBean.getCommentCode()));
        cvFilterdComment = cvComment.filter(eqComments);
        
        if(cvFilterdComment != null && cvFilterdComment.size() > 0) {
            //Comments record already exists for the selected comment code
            awardTemplateCommentsBean = (AwardTemplateCommentsBean)cvFilterdComment.get(0);
            
            if(awardTemplateCommentsBean.getComments() != null){
                
                //                if( initialLoad ){
                //                    //Do nothing when the form is initially loaded
                //                    if( awardCommentsForm.txtArComments.getText() == null ||
                //                    awardCommentsForm.txtArComments.getText().trim().equals(EMPTY)){
                //                        initialLoad = false;
                //                        return ;
                //                    }
                //                }
                String comments = awardCommentsForm.txtArComments.getText().trim();
//                if(getFunctionType() == TypeConstants.COPY_MODE &&
//                        (comments==null || CoeusGuiConstants.EMPTY_STRING.equals(comments))){
//                    queryEngine.removeData(queryKey,AwardTemplateCommentsBean.class,eqComments);
//                }
                //If the comments are modified, set the ac type to 'U'
                if(!awardTemplateCommentsBean.getComments().trim().equals(comments)) {
                    awardTemplateCommentsBean.setComments(comments);
                    setSaveRequired(true);
                    if( awardTemplateCommentsBean.getAcType() == null ){
                        awardTemplateCommentsBean.setAcType(TypeConstants.UPDATE_RECORD);
                    }
                }
            }else{
                if( awardCommentsForm.txtArComments.getText() != null &&
                !awardCommentsForm.txtArComments.getText().trim().equals(EMPTY) ){
                    //If comments are entered
                    awardTemplateCommentsBean.setComments(awardCommentsForm.txtArComments.getText().trim());
                    setSaveRequired(true);
                    if( awardTemplateCommentsBean.getAcType() == null ){
                        awardTemplateCommentsBean.setAcType(TypeConstants.UPDATE_RECORD);
                    }
                }
            }
        }else{
            //Record does not exist for the selected comment code
            if( awardCommentsForm.txtArComments.getText() != null &&
            !awardCommentsForm.txtArComments.getText().trim().equals(EMPTY) ){
                AwardTemplateCommentsBean newBean = new AwardTemplateCommentsBean();
                newBean.setAcType(TypeConstants.INSERT_RECORD);
                newBean.setCommentCode(commentTypeBean.getCommentCode());
                newBean.setComments(awardCommentsForm.txtArComments.getText().trim());
                cvComment.addElement(newBean);
                setSaveRequired(true);
            }
        }
    }
    
    /** This method sets the panel data based on the valueChanged of listSelectionEvent
     * @param listSelectionEvent takes the listSelectionEvent
     */
    public void valueChanged(ListSelectionEvent listSelectionEvent) {
       
//        if(listSelectionEvent.getValueIsAdjusting())
//            return ;
        int selRow = awardCommentsForm.tblCommentType.getSelectedRow();
        CommentTypeBean commentTypeBean;
        
        if(selRow == -1){
            selRow = lastSelectedRow;
        }
        
        String strComments = awardCommentsForm.txtArComments.getText();
        if(strComments.length()>0 && strComments.trim().equals(EMPTY) && !inside){
            inside = true;
            awardCommentsForm.txtArComments.selectAll();
            awardCommentsForm.txtArComments.requestFocus();
            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(REMOVE_SPACES));
            awardCommentsForm.tblCommentType.setRowSelectionInterval(lastSelectedRow, lastSelectedRow);
            count++;

            return ;
        }else{
            inside = false;
        }
        /** Get the selected row and get the comment code for the selected row/ bean
         *the filter with the comment type and get the corresponding data for the
         *text area
         */
        if( selRow >= 0  && cvCommentType != null && cvCommentType.size() > 0) {
            
            commentTypeBean = (CommentTypeBean) cvCommentType.get(lastSelectedRow);
            
            //Update comments for the previously selected comment type
            setAcTypes(commentTypeBean);
            
            lastSelectedRow = selRow;
            
            setAwardComments(selRow);
            
            AwardTemplateCommentsBean awardTemplateCommentsBean = new AwardTemplateCommentsBean();
            /*@todo*/              //awardCommentsBean.setMitAwardNumber(awardBaseBean.getMitAwardNumber());
            awardTemplateCommentsBean.setCommentCode(commentTypeBean.getCommentCode());
            //              try{
            //                awardCommentsForm.btnHistory.setEnabled( enableHistory(awardCommentsBean) );
            //              }catch (CoeusClientException coeusClientException){
            //                  coeusClientException.printStackTrace();
            //              }
        }
    }
    
    /** To enable or disable history button based on the return value from server
     * @param awardCommentsBean
     */
    private boolean enableHistory(AwardCommentsBean awardCommentsBean)
    throws CoeusClientException {
        Object key = new Integer(awardCommentsBean.getCommentCode());
        if( htHistory.containsKey(key) ){
            return ((Boolean)htHistory.get(key)).booleanValue();
        }
        boolean enableHistory;
        RequesterBean requester = new RequesterBean();
        requester.setFunctionType(IS_COMMENTS_HAS_HISTORY);
        requester.setDataObject(awardCommentsBean);
        AppletServletCommunicator comm = new AppletServletCommunicator(CONNECTION_STRING, requester);
        comm.send();
        ResponderBean response = comm.getResponse();
        if(response.isSuccessfulResponse()){
            enableHistory = ((Boolean)response.getDataObject()).booleanValue();
            if( !htHistory.containsKey(key) ){
                htHistory.put(key, response.getDataObject());
            }
        }else {
            throw new CoeusClientException(response.getMessage());
        }
        return enableHistory;
    }
    
    /** To set the comments for the selected comment type
     * @param selectedRow
     */
    private void setAwardComments(int selectedRow){
        CommentTypeBean commentTypeBean = (CommentTypeBean)cvCommentType.get(selectedRow);
        eqComments = new Equals(COMMENT_CODE_FIELD, new Integer(commentTypeBean.getCommentCode()));
        cvFilterdComment = cvComment.filter(eqComments);
        if(cvFilterdComment != null && cvFilterdComment.size() > 0) {
            awardTemplateCommentsBean = (AwardTemplateCommentsBean)cvFilterdComment.get(0);
            awardCommentsForm.txtArComments.setText(awardTemplateCommentsBean.getComments());
            awardCommentsForm.txtArComments.setCaretPosition(0);
            //Added for COEUSQA-1456 : Templates-add User ID stamp & Timestamp - Start
            if(getFunctionType() != TypeConstants.COPY_MODE &&
                    awardTemplateCommentsBean.getUpdateTimestamp() != null){
                awardCommentsForm.txtUpdateUser.setText(awardTemplateCommentsBean.getUpdateUserName());
                String lastUpdate = CoeusDateFormat.format(awardTemplateCommentsBean.getUpdateTimestamp().toString());
                awardCommentsForm.txtLastUpdate.setText(lastUpdate);
            }else{
                awardCommentsForm.txtUpdateUser.setText(CoeusGuiConstants.EMPTY_STRING);
                awardCommentsForm.txtLastUpdate.setText(CoeusGuiConstants.EMPTY_STRING);
            }
            //COEUSQA-1456 : End
        }else {
            awardCommentsForm.txtArComments.setText(EMPTY);
            //Added for COEUSQA-1456 : Templates-add User ID stamp & Timestamp - Start
            awardCommentsForm.txtUpdateUser.setText(CoeusGuiConstants.EMPTY_STRING);
            awardCommentsForm.txtLastUpdate.setText(CoeusGuiConstants.EMPTY_STRING);
            //COEUSQA-1456 : End
        }

    }
    
    //Added For COEUSDEV-562: Changes to comments in award templates not getting saved : Start
    /** 
     * Method for registering the observer 
     */
    public void registerObserver(Observer observer) {
        observable.addObserver(observer);
    }
    
    /*
     * Implementation of abstract method update for Observer Interface.
     */
    public void update(Observable o, Object arg) {
        HashMap hmDetails = (HashMap)arg;
        boolean found = false;
        int invoiceInstrCommentCode = ((Integer)hmDetails.get(Integer.class)).intValue();
        AwardTemplateCommentsBean commentBean = (AwardTemplateCommentsBean)hmDetails.get(AwardTemplateCommentsBean.class);
        if(commentBean!=null && cvComment!=null && !cvComment.isEmpty()){
        for(int i = 0 ; i < cvComment.size();i++){
            AwardTemplateCommentsBean tempcommentBean = (AwardTemplateCommentsBean)cvComment.get(i);
            if(tempcommentBean.getCommentCode() == invoiceInstrCommentCode){
                cvComment.set(i,commentBean);
               setAwardComments(awardCommentsForm.tblCommentType.getSelectedRow());
               found = true;
               break;
            }
        }
        }
        if(!found && commentBean!=null && !EMPTY.equals(commentBean.getComments())){
            if(cvComment == null){
                cvComment = new CoeusVector();
            }
            cvComment.add(commentBean);
        }
    }
    //COEUSDEV-562:End
    
    /** To display the Comment Types
     */
    public class ColumnTableModel extends AbstractTableModel{
        private String colName[] = {EMPTY,EMPTY};
        private Class colClass[] = {ImageIcon.class, String.class};
        
        public boolean isCellEditable(int row, int col){
            return false;
        }
        public   int getRowCount() {
            if(cvCommentType==null){
                return 0;
            }else
                return cvCommentType.size();
        }
        public void setData(CoeusVector cvCommentType){
            cvCommentType= cvCommentType;
        }
        public Class getColumnClass(int columnIndex) {
            return colClass [columnIndex];
        }
        public String getColumnName(int column) {
            return colName[column];
        }
        public int getColumnCount() {
            return colName.length;
        }
        
        public Object getValueAt(int row, int col){
            CommentTypeBean commentTypeBean = (CommentTypeBean)cvCommentType.get(row);
            switch(col){
                case IMAGE_COLUMN:
                    return EMPTY;
                case TEXT_COLUMN:
                    return commentTypeBean.getDescription() + OPENING_BRACE + commentTypeBean.getCommentCode() + CLOSING_BRACE ;
            }
            return EMPTY;
        }
    }
    
    /** Renderer for the column table where the image icons are placed. Rendering
     *the image icons based on the data.
     */
    private class ColumnTableRenderer extends DefaultTableCellRenderer{
        java.net.URL emptyPageUrl = getClass().getClassLoader().getResource( CoeusGuiConstants.NEW_ICON);
        java.net.URL fillPageUrl = getClass().getClassLoader().getResource( CoeusGuiConstants.DATA_ICON);
        private JLabel lblIcon;
        ImageIcon EMPTY_PAGE_ICON, FILL_PAGE_ICON;
        ColumnTableRenderer(){
            super();
            lblIcon = new JLabel();
            
        }
        public Component getTableCellRendererComponent(JTable table,
        Object value, boolean isSelected, boolean hasFocus, int row,
        int column) {
            switch(column){
                case IMAGE_COLUMN:
                    if(emptyPageUrl != null){
                        EMPTY_PAGE_ICON = new ImageIcon(emptyPageUrl);
                        lblIcon.setIcon(EMPTY_PAGE_ICON);
                    }
                    
                    if(fillPageUrl != null){
                        FILL_PAGE_ICON = new ImageIcon(fillPageUrl);
                        lblIcon.setIcon(FILL_PAGE_ICON);
                    }
                    
                    commentTypeBean = (CommentTypeBean)cvCommentType.get(row);
                    Equals eqComments = new Equals(COMMENT_CODE_FIELD, new Integer(commentTypeBean.getCommentCode()));
                    cvFilterdComment = cvComment.filter(eqComments);
                    if(cvFilterdComment != null && cvFilterdComment.size() > 0) {
                        awardTemplateCommentsBean = (AwardTemplateCommentsBean)cvFilterdComment.get(0);
                        value = awardTemplateCommentsBean.getComments();
                    }
                    
                    if(value== null || value.toString().trim().equals(EMPTY)){
                        lblIcon.setIcon(EMPTY_PAGE_ICON);
                    }else {
                        lblIcon.setIcon(FILL_PAGE_ICON);
                    }
                    return lblIcon;
                case TEXT_COLUMN:
                    if(value== null || value.toString().trim().equals(EMPTY)){
                        setText(EMPTY);
                    }else{
                        setText(value.toString());
                    }
            }
            return super.getTableCellRendererComponent(table, value,
            isSelected, hasFocus, row, column);
        }
    }// End of ColumnOverrideRenderer class..............
    
    
    /**
     * Inner class which is used to provide empty header for the Icon Column.
     */
    
    class EmptyHeaderRenderer extends JList implements TableCellRenderer {
        /**
         * Default constructor to set the default foreground/background
         * and border properties of this renderer for a cell.
         */
        EmptyHeaderRenderer() {
            setOpaque(true);
            setForeground(UIManager.getColor("TableHeader.foreground"));
            setBackground(UIManager.getColor("TableHeader.background"));
            setBorder(new javax.swing.border.EmptyBorder(0, 0, 0, 0));
            ListCellRenderer renderer = getCellRenderer();
            ((JLabel) renderer).setHorizontalAlignment(JLabel.CENTER);
            setCellRenderer(renderer);
        }
        
        public Component getTableCellRendererComponent(JTable table,
        Object value,boolean isSelected, boolean hasFocus, int row, int column){
            return this;
        }
    }
    
}//End Class
