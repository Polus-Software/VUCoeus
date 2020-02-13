/*
 * QuestionMaintainanceController.java
 *
 * Created on November 26, 2004, 12:18 PM
 */

package edu.mit.coeus.admin.controller;

import edu.mit.coeus.admin.gui.QuestionMaintainanceForm;
import edu.mit.coeus.gui.CoeusAppletMDIForm;
import edu.mit.coeus.gui.CoeusDlgWindow;
import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.brokers.*;
import edu.mit.coeus.utils.AppletServletCommunicator;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.query.Equals;
import edu.mit.coeus.admin.bean.YNQBean;
import edu.mit.coeus.admin.bean.YNQExplanationBean;

import edu.mit.coeus.utils.MultipleTableColumnSorter;
import edu.mit.coeus.utils.ObjectCloner;
import edu.mit.coeus.utils.ScreenFocusTraversalPolicy;
import edu.mit.coeus.utils.TypeConstants;

import javax.swing.table.*;
import javax.swing.event.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.awt.Cursor;
import java.util.Hashtable;
import javax.swing.table.TableModel;

/**
 *
 * @author  chandrashekara
 */
public class QuestionMaintainanceController  extends AdminController implements ActionListener{
    private QuestionMaintainanceForm questionMaintainanceForm;
    private CoeusDlgWindow dlgQuestionForm;
    private CoeusMessageResources coeusMessageResources;
    private CoeusAppletMDIForm mdiForm;
    private static final int WIDTH=690;
    private static final int HEIGHT = 420;
    private static final int CODE_COLUMN=0;
    private static final int APPLIES_COLUMN=1;
    private static final int QUESTION_COLUMN = 2;
    private CoeusVector cvExplanation,cvQuestion,cvQuestionFinal;
    private static final String EMPTY_STRING = "";
    private final String GET_SERVLET ="/AdminMaintenanceServlet";
    private final String connectTo = CoeusGuiConstants.CONNECTION_URL+ GET_SERVLET;
    private static final char GET_YNQ_DETAILS = 'F';
    private static final char UPDATE_YNQ = 'K';
    private boolean right;
    private boolean isDataModified = false;
    private YNQBean yNQBean;
    private QuesionMaintainanceTableModel quesionMaintainanceTableModel;
    private QuestionTableCellRenderer questionTableCellRenderer;
    
    private static final String FINANCIAL_ENTITY = "F";
    private static final String FINANCIAL_ENTITY_VALUE = "Financial Entity";
    
    private static final String INDIVIDUAL = "I";
    private static final String INDIVIDUAL_VALUE = "Individual";
    
    private static final String INDIVIDUAL_CONFLICT = "C";
    private static final String INDIVIDUAL_CONFLICT_VALUE = "Individual-Conflict of Interest";
    
    private static final String ORGANIZATION = "O";
    private static final String ORGANIZATION_VALUE = "Organization";
    
    private static final String PROPOSAL = "P";
    private static final String PROPOSAL_VALUE = "Proposal";
    
    //Messages
    private static final String PLEASE_SELECT_QUESTION = "ynqExceptionCode.1651";
    private static final String SAVE_CONFIRMATION = "saveConfirmCode.1002";
    private MultipleTableColumnSorter sorter;
    
    /** Creates a new instance of QuestionMaintainanceController */
    public QuestionMaintainanceController(CoeusAppletMDIForm mdiForm) throws CoeusException {
        this.mdiForm = mdiForm;
        questionMaintainanceForm = new QuestionMaintainanceForm();
        coeusMessageResources = CoeusMessageResources.getInstance();
        registerComponents();
        setFormData(null);
        setColumnData();
        postInitComponents();
        
    }
    
    public void display() {
        if(right){
            questionMaintainanceForm.btnAdd.setEnabled(true);
            questionMaintainanceForm.btnModiy.setEnabled(true);
            questionMaintainanceForm.btnOK.setEnabled(true);
        }else{
            questionMaintainanceForm.btnAdd.setEnabled(false);
            questionMaintainanceForm.btnModiy.setEnabled(false);
            questionMaintainanceForm.btnOK.setEnabled(false);
        }
        
        if(questionMaintainanceForm.tblQuestion.getRowCount()>0){
            questionMaintainanceForm.tblQuestion.setRowSelectionInterval(0,0);
        }
        dlgQuestionForm.setVisible(true);
    }
    
    public void formatFields() {
    }
    
    public java.awt.Component getControlledUI() {
        return questionMaintainanceForm;
    }
    
    public Object getFormData() {
        return null;
    }
    
    public void registerComponents() {
        quesionMaintainanceTableModel = new QuesionMaintainanceTableModel();
        questionTableCellRenderer= new QuestionTableCellRenderer();
        questionMaintainanceForm.tblQuestion.setModel(quesionMaintainanceTableModel);
        questionMaintainanceForm.btnAdd.addActionListener(this);
        questionMaintainanceForm.btnCancel.addActionListener(this);
        questionMaintainanceForm.btnModiy.addActionListener(this);
        questionMaintainanceForm.btnMore.addActionListener(this);
        questionMaintainanceForm.btnOK.addActionListener(this);
    }
    
    public void setFormData(Object data) throws edu.mit.coeus.exception.CoeusException {
        try{
            cvExplanation = new CoeusVector();
            cvQuestion = new CoeusVector();
            cvQuestionFinal = new CoeusVector();
            Hashtable questionData = getYNQData();
            right = ((Boolean)questionData.get(edu.mit.coeus.utils.KeyConstants.VIEW_RIGHTS)).booleanValue();
            cvExplanation = (CoeusVector)questionData.get(YNQExplanationBean.class);
            cvQuestion = (CoeusVector)questionData.get(YNQBean.class);
            cvQuestionFinal = (CoeusVector)ObjectCloner.deepCopy(cvQuestion);
            quesionMaintainanceTableModel.setData(cvQuestion);
        }catch(Exception exception){
            exception.printStackTrace();
        }
    }
    
    private Hashtable getYNQData() throws CoeusException{
        Hashtable data = null;
        RequesterBean requester = new RequesterBean();
        requester.setFunctionType(GET_YNQ_DETAILS);
        AppletServletCommunicator comm
        = new AppletServletCommunicator(connectTo, requester);
        
        comm.send();
        ResponderBean responder = comm.getResponse();
        if(responder!= null){
            if(responder.isSuccessfulResponse()){
                data = (Hashtable)responder.getDataObject();
            }
        }else{
            throw new CoeusException(responder.getMessage(),0);
        }
        return data;
    }
    
    public void saveFormData() throws edu.mit.coeus.exception.CoeusException {
        try{
            RequesterBean requesterBean = new RequesterBean();
            requesterBean.setFunctionType(UPDATE_YNQ);
            Hashtable data = new Hashtable();
            data.put(YNQBean.class,cvQuestionFinal);
            data.put(YNQExplanationBean.class,cvExplanation);
            requesterBean.setDataObject(data);
            AppletServletCommunicator appletServletCommunicator = new AppletServletCommunicator();
            appletServletCommunicator.setConnectTo(CoeusGuiConstants.CONNECTION_URL + GET_SERVLET);
            appletServletCommunicator.setRequest(requesterBean);
            appletServletCommunicator.send();
            ResponderBean responderBean = appletServletCommunicator.getResponse();
            if(responderBean!= null){
                if(!responderBean.isSuccessfulResponse()){
                    throw new CoeusException(responderBean.getMessage(), 1);
                }
            }else{
                throw new CoeusException("Response is Null.", 1);
            }
            dlgQuestionForm.setVisible(false);
        }catch(CoeusException exception){
            exception.printStackTrace();
        }
    }
    
    
    
    public boolean validate() throws edu.mit.coeus.exception.CoeusUIException {
        return false;
    }
    
    private void setColumnData(){
        JTableHeader tableHeader = questionMaintainanceForm.tblQuestion.getTableHeader();
        tableHeader.addMouseListener(new ColumnHeaderListener());
//        if( sorter == null ) {
//                sorter = new MultipleTableColumnSorter((TableModel)questionMaintainanceForm.tblQuestion.getModel());
//                sorter.setTableHeader(questionMaintainanceForm.tblQuestion.getTableHeader());
//                questionMaintainanceForm.tblQuestion.setModel(sorter);
//        }
        
        tableHeader.setReorderingAllowed(false);
        tableHeader.setFont(CoeusFontFactory.getLabelFont());
        
        questionMaintainanceForm.tblQuestion.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        questionMaintainanceForm.tblQuestion.setRowHeight(22);
        questionMaintainanceForm.tblQuestion.setShowHorizontalLines(true);
        questionMaintainanceForm.tblQuestion.setShowVerticalLines(true);
        questionMaintainanceForm.tblQuestion.setOpaque(true);
        questionMaintainanceForm.tblQuestion.setSelectionMode(
        DefaultListSelectionModel.SINGLE_SELECTION);
        questionMaintainanceForm.tblQuestion.setRowSelectionAllowed(true);
        
        TableColumn column = questionMaintainanceForm.tblQuestion.getColumnModel().getColumn(CODE_COLUMN);
        column.setPreferredWidth(50);
        column.setResizable(true);
        
        column = questionMaintainanceForm.tblQuestion.getColumnModel().getColumn(APPLIES_COLUMN);
        column.setPreferredWidth(90);
        column.setResizable(true);
        column.setCellRenderer(questionTableCellRenderer);
        
        column = questionMaintainanceForm.tblQuestion.getColumnModel().getColumn(QUESTION_COLUMN);
        column.setPreferredWidth(440);
        column.setResizable(true);
    }
    
    /** Specifies the Modal window */
    private void postInitComponents() {
        
        Component[] components = { questionMaintainanceForm.btnMore,
        questionMaintainanceForm.btnCancel, questionMaintainanceForm.btnModiy,
        questionMaintainanceForm.btnAdd,questionMaintainanceForm.btnOK};
        ScreenFocusTraversalPolicy traversePolicy = new ScreenFocusTraversalPolicy( components );
        questionMaintainanceForm.setFocusTraversalPolicy(traversePolicy);
        questionMaintainanceForm.setFocusCycleRoot(true);
        
        dlgQuestionForm = new CoeusDlgWindow(mdiForm);
        dlgQuestionForm.getContentPane().add(getControlledUI());
        dlgQuestionForm.setTitle("Questions"); // For bug fix 1604
        dlgQuestionForm.setFont(CoeusFontFactory.getLabelFont());
        dlgQuestionForm.setModal(true);
        dlgQuestionForm.setResizable(false);
        dlgQuestionForm.setSize(WIDTH,HEIGHT);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension dlgSize = dlgQuestionForm.getSize();
        dlgQuestionForm.setLocation(screenSize.width/2 - (dlgSize.width/2),
        screenSize.height/2 - (dlgSize.height/2));
        
        dlgQuestionForm.addEscapeKeyListener(
        new AbstractAction("escPressed"){
            public void actionPerformed(ActionEvent ae){
                performCancelAction();
                return;
            }
        });
        dlgQuestionForm.setDefaultCloseOperation(CoeusDlgWindow.DO_NOTHING_ON_CLOSE);
        dlgQuestionForm.addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent we){
                performCancelAction();
                return;
            }
        });
        
        dlgQuestionForm.addComponentListener(
        new ComponentAdapter(){
            public void componentShown(ComponentEvent e){
                setWindowFocus();
            }
        });
    }
    
    private void setWindowFocus(){
        questionMaintainanceForm.btnMore.requestFocusInWindow();
    }
    
    public void actionPerformed(ActionEvent actionEvent) {
        try{
            dlgQuestionForm.setCursor(new java.awt.Cursor(java.awt.Cursor.WAIT_CURSOR));
            
            Object source = actionEvent.getSource();
            if(source.equals(questionMaintainanceForm.btnCancel)){
                performCancelAction();
            }else if(source.equals(questionMaintainanceForm.btnModiy)){
                performModifyAction();
            }else if(source.equals(questionMaintainanceForm.btnAdd)){
                performAddAction();
            }else if(source.equals(questionMaintainanceForm.btnMore)){
                performMoreAction();
            }else if(source.equals(questionMaintainanceForm.btnOK)){
                saveFormData();
            }
        }catch (CoeusException  coeusException){
            edu.mit.coeus.utils.CoeusOptionPane.showErrorDialog(coeusException.getMessage());
        }finally{
            dlgQuestionForm.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        }
    }
    
    private void performMoreAction() throws CoeusException{
        int selRow = questionMaintainanceForm.tblQuestion.getSelectedRow();
        if(selRow!=-1){
            yNQBean = (YNQBean)cvQuestion.get(selRow);
            Equals eqQuestionCode =  new Equals("questionId" ,yNQBean.getQuestionId());
            CoeusVector cvFilteredExplanation = new CoeusVector();
            if(cvExplanation != null){
                cvFilteredExplanation = cvExplanation.filter(eqQuestionCode);
                if(cvFilteredExplanation != null){
                    ExplanationController explanationController = new ExplanationController(mdiForm, TypeConstants.DISPLAY_MODE);
                    explanationController.setQuestionId(yNQBean.getQuestionId());
                    explanationController.setDescription(yNQBean.getDescription());
                    explanationController.setFormData(cvFilteredExplanation);
                    explanationController.display();
                }
            }
        }else{
            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(PLEASE_SELECT_QUESTION));
            return;
        }
    }
    
    private void performAddAction() throws CoeusException{
        if(cvExplanation != null){
            try{
                AddModifyQuestionController addModifyQuestionController =
                new AddModifyQuestionController(mdiForm, null, cvQuestion, cvExplanation,TypeConstants.ADD_MODE);
                YNQBean yNQBeanNew = addModifyQuestionController.displayWindow();
                if(yNQBeanNew != null){
                    cvQuestion.add(yNQBeanNew);
                    cvQuestionFinal.add((YNQBean)ObjectCloner.deepCopy(yNQBeanNew));
                    isDataModified = true;
                    quesionMaintainanceTableModel.fireTableDataChanged();
                    int index = searchIndex(cvQuestion, yNQBeanNew);
                    if(index != -1){
                        questionMaintainanceForm.tblQuestion.setRowSelectionInterval(index, index);
                    }
                }
            }catch(Exception exception){
                exception.printStackTrace();
            }
        }
    }
    
    private void performModifyAction()throws CoeusException{
        try{
            int selRow = questionMaintainanceForm.tblQuestion.getSelectedRow();
            if(selRow!=-1){
                yNQBean = (YNQBean)cvQuestion.get(selRow);
                if(yNQBean != null && cvExplanation != null){
                    AddModifyQuestionController addModifyQuestionController =
                    new AddModifyQuestionController(mdiForm, yNQBean, cvQuestion, cvExplanation,TypeConstants.MODIFY_MODE);
                    yNQBean = addModifyQuestionController.displayWindow();
                    if(yNQBean != null){
                        if(yNQBean.getAcType().equals(TypeConstants.UPDATE_RECORD)){
                            YNQBean bean = (YNQBean)search(cvQuestionFinal,yNQBean,null);
                            if(bean == null){
                                bean = (YNQBean)search(cvQuestionFinal,yNQBean,TypeConstants.UPDATE_RECORD);
                            }
                            if(bean != null){
                                setBeanData(bean,yNQBean);
                                bean.setAcType(TypeConstants.UPDATE_RECORD);
                            }
                        }else if(yNQBean.getAcType().equals(TypeConstants.INSERT_RECORD)){
                            YNQBean bean = (YNQBean)search(cvQuestionFinal,yNQBean,null);
                            if(bean != null){
                                bean.setAcType(TypeConstants.DELETE_RECORD);
                                YNQBean newBean = (YNQBean)ObjectCloner.deepCopy(yNQBean);
                                newBean.setQuestionId(addModifyQuestionController.getQuestionId());
                                newBean.setAcType(TypeConstants.INSERT_RECORD);
                                cvQuestionFinal.add(newBean);
                            }else{
                                bean = (YNQBean)search(cvQuestionFinal,yNQBean,TypeConstants.UPDATE_RECORD);
                                if(bean != null){
                                    bean.setAcType(TypeConstants.DELETE_RECORD);
                                    YNQBean newBean = (YNQBean)ObjectCloner.deepCopy(yNQBean);
                                    newBean.setQuestionId(addModifyQuestionController.getQuestionId());
                                    newBean.setAcType(TypeConstants.INSERT_RECORD);
                                    cvQuestionFinal.add(newBean);
                                }else{
                                    bean = (YNQBean)search(cvQuestionFinal,yNQBean,TypeConstants.INSERT_RECORD);
                                    if(bean != null){
                                        setBeanData(bean,yNQBean);
                                        bean.setQuestionId(addModifyQuestionController.getQuestionId());
                                    }else{
                                        bean = (YNQBean)ObjectCloner.deepCopy(yNQBean);
                                        bean.setQuestionId(addModifyQuestionController.getQuestionId());
                                        bean.setAcType(TypeConstants.INSERT_RECORD);
                                        cvQuestionFinal.add(bean);
                                    }
                                }
                            }
                        }
                        yNQBean.setQuestionId(addModifyQuestionController.getQuestionId());
                        
                    }
                    if(addModifyQuestionController.isDataModified){
                        isDataModified = true;
                        quesionMaintainanceTableModel.fireTableDataChanged();
                        questionMaintainanceForm.tblQuestion.setRowSelectionInterval(selRow, selRow);
                    }
                }
            }else{
                CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(PLEASE_SELECT_QUESTION));
                return;
            }
        }catch(Exception exception){
            exception.printStackTrace();
        }
    }
    private void setBeanData(YNQBean original,YNQBean updating){
        original.setQuestionId(updating.getQuestionId());
        original.setDescription(updating.getDescription());
        original.setEffectiveDate(updating.getEffectiveDate());
        original.setQuestionType(updating.getQuestionType());
        original.setStatus(updating.getStatus());
        original.setNoOfAnswers(updating.getNoOfAnswers());
        original.setGroupName(updating.getGroupName());
        original.setExplanationRequiredFor(updating.getExplanationRequiredFor());
        original.setDateRequiredFor(updating.getDateRequiredFor());
    }
    private void performCancelAction(){
        try{
            if(isDataModified){
                String mesg = coeusMessageResources.parseMessageKey(SAVE_CONFIRMATION);
                int selectedOption = CoeusOptionPane.showQuestionDialog(
                coeusMessageResources.parseMessageKey(mesg),
                CoeusOptionPane.OPTION_YES_NO_CANCEL,
                CoeusOptionPane.DEFAULT_YES);
                if(selectedOption == CoeusOptionPane.SELECTION_YES) {
                    saveFormData();
                } else if(selectedOption == CoeusOptionPane.SELECTION_NO){
                    dlgQuestionForm.setVisible(false);
                } 
            }else{
                dlgQuestionForm.setVisible(false);
            }
        }catch(CoeusException exception){
            exception.printStackTrace();
        }
    }
    /**
     * Method to Search a Bean and delete it
     * @param coeusVector CoeusVector
     * @param object Object
     * @return void
     **/
    private void searchAndDelete(CoeusVector coeusVector, Object object){
        if(object != null && coeusVector != null){
            YNQBean bean = (YNQBean)object;
            YNQBean vectorBean = null;
            for(int i=0;i<coeusVector.size();i++){
                vectorBean = (YNQBean)coeusVector.elementAt(i);
                if(vectorBean.getAcType()!=null && bean.getAcType()!=null
                && vectorBean.getQuestionId().equals(bean.getQuestionId())
                && vectorBean.getAcType().equals(bean.getAcType()) ){
                    coeusVector.removeElementAt(i);
                    break;
                }
            }
        }
    }
    
    /**
     * Method to search an object
     * @param coeusVector CoeusVector
     * @param object Object
     * @param acType String
     * @return Object
     **/
    private Object search(CoeusVector coeusVector, Object object, String acType){
        try{
            if(object != null && coeusVector != null){
                YNQBean bean = (YNQBean)object;
                YNQBean vectorBean = null;
                for(int i=0;i<coeusVector.size();i++){
                    vectorBean = (YNQBean)coeusVector.elementAt(i);
                    if(acType != null && vectorBean.getAcType() != null
                    && !acType.equals(EMPTY_STRING)
                    && vectorBean.getQuestionId().equals(bean.getQuestionId())
                    && vectorBean.getAcType().equals(acType)){
                        return vectorBean;
                    }else if( acType == null
                    && vectorBean.getQuestionId().equals(bean.getQuestionId())
                    && vectorBean.getAcType() == null){
                        return vectorBean;
                    }
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * Method to search index of an object
     * @param coeusVector CoeusVector
     * @param object Object
     * @return int
     **/
    private int searchIndex(CoeusVector coeusVector, Object object){
        if(object != null && coeusVector != null){
            YNQBean bean = (YNQBean)object;
            YNQBean vectorBean = null;
            for(int i=0;i<coeusVector.size();i++){
                vectorBean = (YNQBean)coeusVector.elementAt(i);
                if(vectorBean.getQuestionId().equals(bean.getQuestionId())){
                    return i;
                }
            }
        }
        return -1;
    }
    
    //    private CoeusVector getYNQDetails(String questionId) throws CoeusException{
    //        CoeusVector data = null;
    //        RequesterBean requester = new RequesterBean();
    //        requester.setFunctionType(GET_ALL_QUESTION_DEATAILS);
    //        requester.setDataObject(questionId);
    //        AppletServletCommunicator comm
    //        = new AppletServletCommunicator(connectTo, requester);
    //
    //        comm.send();
    //        ResponderBean responder = comm.getResponse();
    //        if(responder!= null){
    //            if(responder.isSuccessfulResponse()){
    //                data = (CoeusVector)responder.getDataObjects();
    //            }
    //        }else{
    //            throw new CoeusException(responder.getMessage(),0);
    //        }
    //        return data;
    //    }
    
    
    /** This class will sort the column values in ascending and descending order
     *based on number of clicks. This will sort by Question Id,
     *explanation Type and Explnation
     */
    
    public class ColumnHeaderListener extends MouseAdapter {
        String nameBeanId [][] ={
            {"0","questionId" },
            {"1","questionType" },
            {"2","description"},
        };
        boolean sort =true;
        /**
         * @param evt
         */
        public void mouseClicked(MouseEvent evt) {
            try {
                dlgQuestionForm.setCursor(new Cursor(Cursor.WAIT_CURSOR));
                JTable table = ((JTableHeader)evt.getSource()).getTable();
                TableColumnModel colModel = table.getColumnModel();
                
                // The index of the column whose header was clicked
                int vColIndex = colModel.getColumnIndexAtX(evt.getX());
                // int mColIndex = table.convertColumnIndexToModel(vColIndex);
                if(cvQuestion!=null && cvQuestion.size()>0 &&
                nameBeanId [vColIndex][1].length() >1 ){
                    // For bug fix 1604
                    ((CoeusVector)cvQuestion).sort(nameBeanId [vColIndex][1],sort);
                    if(sort){
                        sort = false;
                    }else{
                        sort = true;
                    }
                    quesionMaintainanceTableModel.fireTableRowsUpdated(0, quesionMaintainanceTableModel.getRowCount());
                }
            } catch(Exception exception) {
                exception.getMessage();
            }finally{
                dlgQuestionForm.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        }
    }// End of ColumnHeaderListener.................
    
    
    public class QuesionMaintainanceTableModel extends AbstractTableModel{
        private String[] colName = {"Code","Applies To","Question"};
        private Class[] colClass = {String.class,String.class,String.class};
        
        public boolean isCellEditable(int row, int col){
            return false;
        }
        public int getColumnCount() {
            return colName.length;
        }
        
        public String getColumnName(int col){
            return colName[col];
        }
        
        public Class getColumnClass(int col){
            return colClass[col];
        }
        
        public void setData(CoeusVector cvQ){
            cvQuestion = cvQ;
            fireTableDataChanged();
        }
        
        public int getRowCount() {
            if(cvQuestion==null){
                return 0;
            }else{
                return cvQuestion.size();
            }
        }
        
        public Object getValueAt(int row, int col) {
            YNQBean yNQBean= (YNQBean)cvQuestion.get(row);
            switch(col){
                case CODE_COLUMN:
                    return yNQBean.getQuestionId();
                case APPLIES_COLUMN:
                    return yNQBean.getQuestionType();
                case QUESTION_COLUMN:
                    return yNQBean.getDescription();
            }
            return EMPTY_STRING;
        }
    }// end of table model class
    
    
    private class QuestionTableCellRenderer extends DefaultTableCellRenderer{
        //        private CoeusTextField txtComp;
        public QuestionTableCellRenderer(){
            //            txtComp = new CoeusTextField();
            //            txtComp.setBorder(new EmptyBorder(0,0,0,0));
            //            txtComp.setOpaque(true);
        }
        /** Overridden method of the rendrer
         * @param table
         * @param value
         * @param isSelected
         * @param hasFocus
         * @param row
         * @param column
         * @return
         */
        public Component getTableCellRendererComponent(JTable table,Object value,
        boolean isSelected, boolean hasFocus,int row,int column){
            if(column==APPLIES_COLUMN){
                if(value.toString().equals(FINANCIAL_ENTITY)){
                    setText(FINANCIAL_ENTITY_VALUE);
                }else if(value.toString().equals(INDIVIDUAL)){
                    setText(INDIVIDUAL_VALUE);
                }else if(value.toString().equals(INDIVIDUAL_CONFLICT)){
                    setText(INDIVIDUAL_CONFLICT_VALUE);
                }else if(value.toString().equals(ORGANIZATION)){
                    setText(ORGANIZATION_VALUE);
                }else if(value.toString().equals(PROPOSAL)){
                    setText(PROPOSAL_VALUE);
                }
            }
            return super.getTableCellRendererComponent(table,getText(),
            isSelected, hasFocus,row,column);
        }
    }
}
