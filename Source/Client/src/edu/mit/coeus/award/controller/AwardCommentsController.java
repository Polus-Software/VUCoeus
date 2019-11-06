/*
 * AwardCommentsController.java
 *
 * Created on March 25, 2004, 12:01 PM
 */

package edu.mit.coeus.award.controller;

import edu.mit.coeus.award.AwardConstants;
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
import java.util.Observer;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;
import java.awt.event.*;
import java.awt.*;
import java.util.Hashtable;
/** 
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 
* @author chandru
* @modified by Vyjayanthi
*/
public class AwardCommentsController extends AwardController 
implements ActionListener,ListSelectionListener, BeanUpdatedListener,DocumentListener{
    private AwardCommentsForm awardCommentsForm;

    private static final String COMMENT_CODE_FIELD = "commentCode";
    private static final String DESCRIPTION_FIELD = "description";

    private static final String CONFIRM_SYNC = "awardComments_exceptionCode.1404";
    private static final String OPENING_BRACE = " ( ";
    private static final String CLOSING_BRACE = " )";
    private static final java.awt.Color PANEL_BACKGROUND_COLOR = 
        (Color) UIManager.getDefaults().get("Panel.background");
    
    private static final char IS_COMMENTS_HAS_HISTORY = 'K';
    private static final String CONNECTION_STRING = CoeusGuiConstants.CONNECTION_URL +
        "/AwardMaintenanceServlet";
    
    private AwardBaseBean  awardBaseBean;
    private QueryEngine queryEngine;
    private CoeusVector cvCommentType;
    private CoeusVector cvComment;
    private CoeusVector cvFilterdComment;
//    private boolean initialLoad;
    
    private ColumnTableModel columnTableModel;
    private ColumnTableRenderer columnTableRenderer;
    private AwardCommentsBean  awardCommentsBean;
    private CommentTypeBean commentTypeBean;
    private CoeusMessageResources  coeusMessageResources;
    private int lastSelectedRow;
    private Equals eqComments;
    
    /** Stores the key value pairs of comment type code and boolean values 
     * if history is data is present */
    private Hashtable htHistory;
    
    private static final int IMAGE_COLUMN = 0;
    private static final int TEXT_COLUMN = 1;
    
    //Bug Fix:Performance Issue (Out of memory) Start 1
    private JScrollPane jscrPn;
    //Bug Fix:Performance Issue (Out of memory) End 1
    /** Creates a new instance of AwardCommentsController */
    public AwardCommentsController(AwardBaseBean awardBaseBean, char functionType) {
        super(awardBaseBean);
        coeusMessageResources = CoeusMessageResources.getInstance();
        queryEngine = QueryEngine.getInstance();
        cvComment = new CoeusVector();
        cvCommentType = new CoeusVector();
        cvFilterdComment = new CoeusVector();
        registerComponents();
        setFormData(awardBaseBean);
        setFunctionType(functionType);
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
        if( getFunctionType() == TypeConstants.DISPLAY_MODE ){
            awardCommentsForm.btnSync.setEnabled(false);
            awardCommentsForm.txtArComments.setEditable(false);
            awardCommentsForm.txtArComments.setBackground(PANEL_BACKGROUND_COLOR);
            awardCommentsForm.btnSyncToChildren.setEnabled(false);//COEUSDEV 211
        }
//         awardCommentsForm.btnSyncToChildren.setEnabled(false);//2796
    }
    
    /** An overridden method of the controller
     * @return  returns the controlled form component
     */
    public java.awt.Component getControlledUI() {
        
        //Bug Fix:Performance Issue (Out of memory) Start 2
        //return awardCommentsForm;
        //JIRA Case COEUSDEV-160, COEUSDEV-177 - START
        //jscrPn = new JScrollPane(awardCommentsForm);
        //JIRA Case COEUSDEV-160, COEUSDEV-177 - END
        return jscrPn;
        //Bug Fix:Performance Issue (Out of memory) End 2
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
        awardCommentsForm.btnSyncToChildren.addActionListener(this);//2796
        awardCommentsForm.txtArComments.setDocument(new LimitedPlainDocument(32000));
        awardCommentsForm.txtArComments.getDocument().addDocumentListener(this);//2796
        awardCommentsForm.tblCommentType.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        //awardCommentsForm.tblCommentType.getSelectionModel().addListSelectionListener(this);
        
        columnTableRenderer = new ColumnTableRenderer();
        columnTableModel = new ColumnTableModel();
        awardCommentsForm.tblCommentType.setModel(columnTableModel);
        
        addBeanUpdatedListener(this, AwardDetailsBean.class);
        addBeanUpdatedListener(this, AwardCommentsBean.class);
        //JIRA Case COEUSDEV-160, COEUSDEV-177 - START
        jscrPn = new JScrollPane(awardCommentsForm);
        // JM 4-10-2012 add listener to pass control to outer pane for scrolling
        jscrPn.addMouseWheelListener(new MouseWheelListener() {
            public void mouseWheelMoved(MouseWheelEvent e) {
            	jscrPn.getParent().dispatchEvent(e);
            }
        });
        //JIRA Case COEUSDEV-160, COEUSDEV-177 - END
        //Added for COEUSQA-1456 : Templates-add User ID stamp & Timestamp - Start
        //Disables the Last Update and Update User components in contact details and contact screen
        awardCommentsForm.pnlUpdateDetails.setVisible(false);
        awardCommentsForm.pnlUpdateDetails1.setVisible(false);
        //COEUSQA-1456 : End
        
    }
    
    /** Method to perform some action when the beanUpdated event is triggered
     * here it sets the <CODE>refreshRequired</CODE> flag
     * @param beanEvent takes the beanEvent */
    public void beanUpdated(BeanEvent beanEvent) {
        if( beanEvent.getSource().getClass().equals(OtherHeaderController.class) ){
            if( beanEvent.getBean().getClass().equals(AwardDetailsBean.class)){
                setRefreshRequired(true);
            }
        }
        if( beanEvent.getBean().getClass().equals(AwardCommentsBean.class) ){
            setRefreshRequired(true);
            refresh();
        }
    }
    
    /** This method will refresh the form with the modified data */
    public void refresh(){
        if( !isRefreshRequired() ) return ;
        setFormData(awardBaseBean);
        setRefreshRequired(false);
    }
    
    /** Method to clean up objects */
    public void cleanUp() {
        
        //Bug Fix:Performance Issue (Out of memory) Start 3
        removeBeanUpdatedListener(this, AwardDetailsBean.class);
        removeBeanUpdatedListener(this, AwardCommentsBean.class);
        
        if(jscrPn!=null){
            jscrPn.remove(awardCommentsForm);
            jscrPn = null;
            awardCommentsForm.scrPnComments.remove(awardCommentsForm.txtArComments);
            awardCommentsForm.pnlComment.remove(awardCommentsForm.scrPnComments);
            awardCommentsForm.scrPnComments = null;
            awardCommentsForm.pnlComment = null;
            awardCommentsForm.tblCommentType.getSelectionModel().removeListSelectionListener(this);
        }
        //Bug Fix:Performance Issue (Out of memory) End 3
        
        awardCommentsForm = null;
        awardBaseBean = null;
        queryEngine = null;
        cvCommentType = null;
        cvComment = null;
        cvFilterdComment = null;
        columnTableModel = null;
        columnTableRenderer = null;
        awardCommentsBean = null;
        commentTypeBean = null;
        coeusMessageResources = null;
        eqComments = null;
        htHistory = null;
       
    }
    
    /** Saves the Form Data.
     */
    public void saveFormData() {

        commentTypeBean = (CommentTypeBean) cvCommentType.get(awardCommentsForm.tblCommentType.getSelectedRow());
        
        //Set the acType for the selected bean
        setAcTypes(commentTypeBean);
        
        for( int index = 0; index < cvComment.size(); index++ ){
            AwardCommentsBean awardCommentsBean = (AwardCommentsBean)cvComment.get(index);
            if( awardCommentsBean.getAcType() == null ) continue;
            if( awardCommentsBean.getAcType().equals(TypeConstants.INSERT_RECORD) ){
               //Commented for bug fixed for case#2204 start 1 
//                if( awardCommentsBean.getComments() == null ||
//                awardCommentsBean.getComments().equals(EMPTY)){
//                    /**
//                     * Comments are null or empty
//                     * (ie. if text is entered and cleared before saving), do nothing
//                     */
//                    continue;
//                }else{
                //Commented for bug fixed for case#2204 end 1 
                    /**
                     * New record to be inserted
                     * Set the mit award number and insert to the query engine
                     */
                    awardCommentsBean.setMitAwardNumber(awardBaseBean.getMitAwardNumber());
                    queryEngine.insert(queryKey, awardCommentsBean);
                    //Commented for bug fixed for case#2204 start 2 
            //    }
                //Commented for bug fixed for case#2204 end 2 
            }else if( awardCommentsBean.getAcType().equals(TypeConstants.UPDATE_RECORD) ){
                
                //Update the query engine
                try{
                    queryEngine.update(queryKey, awardCommentsBean);
                }catch (CoeusException coeusException){
                    coeusException.printStackTrace();
                }
            }
        }
        setSaveRequired(false);
    }
    
    /** This method is used to set the form data specified in
     * <CODE> awardBaseBean </CODE>
     * @param awardBaseBean data to set to the form
     */
    public void setFormData(Object  awardBaseBean ) {
        htHistory = new Hashtable();
        try{
            this.awardBaseBean = (AwardBaseBean)awardBaseBean;
            cvCommentType = queryEngine.executeQuery(queryKey,CommentTypeBean.class, CoeusVector.FILTER_ACTIVE_BEANS);
            if(cvCommentType!= null && cvCommentType.size() > 0){
                cvCommentType.sort(DESCRIPTION_FIELD);
            }
            cvComment = queryEngine.executeQuery(queryKey,AwardCommentsBean.class, CoeusVector.FILTER_ACTIVE_BEANS);
            columnTableModel.setData(cvCommentType);
            awardCommentsForm.tblCommentType.repaint();

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
                AwardCommentsBean awardCommentsBean = new AwardCommentsBean();
                awardCommentsBean.setMitAwardNumber(this.awardBaseBean.getMitAwardNumber());
                awardCommentsBean.setCommentCode(commentTypeBean.getCommentCode());
                try{
                    awardCommentsForm.btnHistory.setEnabled( enableHistory(awardCommentsBean) );
                }catch (CoeusClientException coeusClientException){
                    coeusClientException.printStackTrace();
                }
            }
        }catch (CoeusException exception){
            exception.printStackTrace();
        }
    }
    
    public boolean validate() throws edu.mit.coeus.exception.CoeusUIException {
        return true;
    }
    
    /** This method triggers all actions based on the event occured
     * @param actionEvent takes the actionEvent
     */
    public void actionPerformed(ActionEvent actionEvent) {
        Object source = actionEvent.getSource();
        if( source.equals(awardCommentsForm.btnSync) ){
            performSyncOperation();
        }else if( source.equals(awardCommentsForm.btnHistory) ){
            showHistory();
        }//2796: Sync to Parent
        else if( source.equals(awardCommentsForm.btnSyncToChildren) ){
            syncCommentsToHierarchy();
        }
        //2796 End
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
       AwardCommentsBean awardCommentsBean = (AwardCommentsBean)cvFilterdComment.get(0);
       
       CommentsHistoryController commentsHistoryController = new CommentsHistoryController(
       awardBaseBean, commentTypeBean, awardCommentsBean);
       commentsHistoryController.display();
    }
    
    private void performSyncOperation(){
        int option = CoeusOptionPane.showQuestionDialog(
        coeusMessageResources.parseMessageKey(CONFIRM_SYNC), 
        CoeusOptionPane.OPTION_YES_NO, CoeusOptionPane.DEFAULT_YES);
        
        switch( option ){
            case (JOptionPane.YES_OPTION ):
                //Call the Sync Comments of the AwardController
                if ( syncComments(EMPTY, getTemplateCode()) ){
                    setFormData(awardBaseBean);
                    setSaveRequired(true);
                }
                break;
            case (JOptionPane.NO_OPTION ):
                break;
            default:
                break;
        }
    }
    
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
            awardCommentsBean = (AwardCommentsBean)cvFilterdComment.get(0);
          
            if(awardCommentsBean.getComments() != null){

//                if( initialLoad ){
//                    //Do nothing when the form is initially loaded
//                    if( awardCommentsForm.txtArComments.getText() == null ||
//                    awardCommentsForm.txtArComments.getText().trim().equals(EMPTY)){
//                        initialLoad = false;
//                        return ;
//                    }
//                }
                
                //If the comments are modified, set the ac type to 'U'
                if(!awardCommentsBean.getComments().trim().equals(awardCommentsForm.txtArComments.getText().trim())) {
                    awardCommentsBean.setComments(awardCommentsForm.txtArComments.getText().trim());
                    setSaveRequired(true);
                    if( awardCommentsBean.getAcType() == null ){
                        awardCommentsBean.setAcType(TypeConstants.UPDATE_RECORD);
                    }
                }
            }else{
                if( awardCommentsForm.txtArComments.getText() != null &&
                !awardCommentsForm.txtArComments.getText().trim().equals(EMPTY) ){
                    //If comments are entered
                    awardCommentsBean.setComments(awardCommentsForm.txtArComments.getText().trim());
                    setSaveRequired(true);
                    if( awardCommentsBean.getAcType() == null ){
                        awardCommentsBean.setAcType(TypeConstants.UPDATE_RECORD);
                    }
                }
            }
        }else{
            //Record does not exist for the selected comment code
            if( awardCommentsForm.txtArComments.getText() != null &&
            !awardCommentsForm.txtArComments.getText().trim().equals(EMPTY) ){
                AwardCommentsBean newBean = new AwardCommentsBean();
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
          int selRow = awardCommentsForm.tblCommentType.getSelectedRow();
          CommentTypeBean commentTypeBean;

          if(selRow == -1){
              selRow = lastSelectedRow;
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
              
              AwardCommentsBean awardCommentsBean = new AwardCommentsBean();
              awardCommentsBean.setMitAwardNumber(awardBaseBean.getMitAwardNumber());
              awardCommentsBean.setCommentCode(commentTypeBean.getCommentCode());
              try{
                awardCommentsForm.btnHistory.setEnabled( enableHistory(awardCommentsBean) );
              }catch (CoeusClientException coeusClientException){
                  coeusClientException.printStackTrace();
              }
          }
//          enableSyncButton();//2796
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
          awardCommentsBean = (AwardCommentsBean)cvFilterdComment.get(0);
          awardCommentsForm.txtArComments.setText(awardCommentsBean.getComments());
          awardCommentsForm.txtArComments.setCaretPosition(0);
        }else {
          awardCommentsForm.txtArComments.setText(EMPTY);
        }
        enableSyncButton();
    }
    //Added with case 2796: Sync to parent - Start
    //Saves the form;sets the sync fla and notifies oblserver
    private void syncCommentsToHierarchy() {
        
        saveFormData();
        commentTypeBean = (CommentTypeBean) cvCommentType.get(awardCommentsForm.tblCommentType.getSelectedRow());
        eqComments = new Equals(COMMENT_CODE_FIELD, new Integer(commentTypeBean.getCommentCode()));
//        And eqCommentsAndAcTypeNotNull = new And(eqComments,acTypeNotNull);
        try {
            CoeusVector cvComment = queryEngine.executeQuery(queryKey,AwardCommentsBean.class,eqComments);
            if(cvComment!=null && !cvComment.isEmpty()){
                //Modified for COEUSDEV-416 : Award Sync to Children - Display proper error message when not syncing because the award is not saved
//                HashMap target = showSyncTargetWindow(true,AwardConstants.SYNC);
                HashMap target = showSyncTargetWindow(true,AwardConstants.COMMENTS_SYNC,AwardConstants.SYNC);
                //COEUSDEV-416 : End
                if(target!=null){
                    awardCommentsBean = (AwardCommentsBean)cvComment.get(0);
                    awardCommentsBean.setSyncRequired(true);
                    //Modified for COEUSDEV-211:Sync to parent-comments-active awards only-updating closed awards too
                    if(awardCommentsBean.getAcType()==null ){
                        awardCommentsBean.setAcType(TypeConstants.UPDATE_RECORD);
                    }
                    queryEngine.setData(queryKey,AwardCommentsBean.class,awardCommentsBean);
                    if(setSyncFlags(AwardCommentsBean.class,true,target)){
                        setFormData(awardBaseBean);
                        saveAndSyncAward(AwardCommentsBean.class);
                    }
                    //COEUSDEV-211: End
                }
            }
        } catch (CoeusException ex) {
            ex.printStackTrace();
        }
    }
    
    public void insertUpdate(DocumentEvent e) {
        enableSyncButton();
    }

    public void removeUpdate(DocumentEvent e) {
        enableSyncButton();
    }

    public void changedUpdate(DocumentEvent e) {
        enableSyncButton();
    }
    
    /*Method to enable/disable sync Hierarchy button
     *when data is modified.
     */
    private void enableSyncButton(){
        //Modified for COEUSDEV-211:Sync to parent-comments-active awards only-updating closed awards too
        String text  = awardCommentsForm.txtArComments.getText().trim();
         if( getFunctionType() == TypeConstants.DISPLAY_MODE ){
            awardCommentsForm.btnSyncToChildren.setEnabled(false);
         }else if(awardBaseBean.isParent() && text.length()>0){
            awardCommentsForm.btnSyncToChildren.setEnabled(true);
        }else{
            awardCommentsForm.btnSyncToChildren.setEnabled(false);
        }
    }
    //COEUSDEV 211 End
    //2796 End
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
                        awardCommentsBean = (AwardCommentsBean)cvFilterdComment.get(0);
                        value = awardCommentsBean.getComments();
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
}
