/*
 * QuestionsController.java
 *
 * Created on October 17, 2005, 11:49 AM
 */

package edu.mit.coeus.mapsrules.controller;

import edu.mit.coeus.admin.controller.AddModifyQuestionController;
import edu.mit.coeus.admin.bean.YNQBean;
import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.gui.CoeusAppletMDIForm;
import edu.mit.coeus.gui.CoeusDlgWindow;
import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.mapsrules.gui.QuestionsForm;
import edu.mit.coeus.propdev.gui.QuestionMoreForm;
import edu.mit.coeus.utils.AppletServletCommunicator;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.TypeConstants;
import edu.mit.coeus.utils.query.Equals;
import edu.mit.coeus.utils.question.bean.YNQExplanationBean;
import edu.mit.coeus.utils.tree.TransferableUserRoleData;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSource;
import java.awt.dnd.DragSourceListener;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;

/**
 *
 * @author  vinayks
 */
public class QuestionsController extends RuleController implements DragSourceListener,
        DragGestureListener,ActionListener{
    
    private String EMPTY_STRING ="";
    private static final int ID_COLUMN=0;
    private static final int DESCRIPTION_COLUMN=1;
    private int WIDTH=550;
    private int HEIGHT=230;
    private QuestionsTableModel questionsTableModel;
    private QuestionsForm questionsForm;
    private CoeusAppletMDIForm mdiForm = CoeusGuiConstants.getMDIForm();
    private CoeusDlgWindow dlgQuestions;
    private CoeusVector cvYNQData;
    private boolean sortCodeAsc = true;
    private boolean sortDescAsc = false;
    
    private static final char GET_YNQ_EXPLANATION = 'G';
    
    private static final String RULES_SERVLET = "/RuleMaintenanceServlet";
    
    DragSource dragSource;
    Cursor dragCursor = new Cursor(Cursor.HAND_CURSOR);
    
    /** Creates a new instance of QuestionsController */
    public QuestionsController() {
        registerComponents();
        formatFields();
    }
    
    public void registerComponents() {
        questionsForm = new QuestionsForm();
        questionsTableModel = new QuestionsTableModel();
        
//        questionsForm.btnMore.addActionListener(this);
        
        dragSource = new DragSource();
        dragSource.createDefaultDragGestureRecognizer(questionsForm.tblQuestions, DnDConstants.ACTION_MOVE, this);
        
        //questionsForm.tblQuestions.getTableHeader().addMouseListener(this);
        
       // questionsForm.tblQuestions.getTableHeader().addMouseListener(new CustomMouseAdapter());
        
        questionsForm.tblQuestions.setModel(questionsTableModel);
        questionsForm.tblQuestions.setRowSelectionAllowed(true);
        questionsForm.tblQuestions.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e){
                if(e.getClickCount()==2){
                    int selRow = questionsForm.tblQuestions.getSelectedRow();
                    YNQBean ynqBean = (YNQBean)cvYNQData.get(selRow);
                    try{
                        CoeusVector cvYNQExp = getYNQExpData(ynqBean.getQuestionId());
                        CoeusVector cvExp = new CoeusVector();
                        edu.mit.coeus.admin.bean.YNQExplanationBean adminYNQExpBean = null;
                        if(cvYNQExp!=null){
                            for(int i=0;i<cvYNQExp.size(); i++){
                                adminYNQExpBean = new edu.mit.coeus.admin.bean.YNQExplanationBean();
                                adminYNQExpBean.setQuestionId((
                                        (edu.mit.coeus.utils.question.bean.YNQExplanationBean)cvYNQExp.get(i)).
                                        getQuestionId());
                                adminYNQExpBean.setExplanation((
                                        (edu.mit.coeus.utils.question.bean.YNQExplanationBean)cvYNQExp.get(i)).
                                        getExplanation());
                                adminYNQExpBean.setExplanationType((
                                        (edu.mit.coeus.utils.question.bean.YNQExplanationBean)cvYNQExp.get(i)).
                                        getExplanationType());
                                cvExp.add(adminYNQExpBean);
                            }
                        }
                    AddModifyQuestionController addModifyQuestionController = 
                            new AddModifyQuestionController(CoeusGuiConstants.getMDIForm(),
                            ynqBean, null, cvExp, TypeConstants.DISPLAY_MODE);
                    addModifyQuestionController.displayWindow();
                    }catch(Exception exception){
                    
                    }
                }
            }
        });
    }
    
    public void formatFields() {
    }
    
    public java.awt.Component getControlledUI() {
        return questionsForm;
        
    }
    
    public void setFormData(Object data) throws edu.mit.coeus.exception.CoeusException {
//        Equals questionTypeEquals = new Equals("questionType","P");
        cvYNQData = new CoeusVector();
        cvYNQData = (CoeusVector)data;
//        cvYNQData = cvYNQData.filter(questionTypeEquals);
        if(cvYNQData != null || cvYNQData.size() > 0){
            questionsTableModel.setData(cvYNQData);
        }
        setTableEditors();
    }
    
    public Object getFormData() {
        return null;
    }
    
    public void display() {
        dlgQuestions.setVisible(true);
    }
    
    public void saveFormData() throws edu.mit.coeus.exception.CoeusException {
    }
    
    public boolean validate() throws edu.mit.coeus.exception.CoeusUIException {
        return false;
    }
    
    public void postInitComponents() {
        dlgQuestions = new CoeusDlgWindow(mdiForm);
        dlgQuestions.setResizable(false);
        dlgQuestions.setModal(true);
        dlgQuestions.getContentPane().add(questionsForm);
        dlgQuestions.setFont(CoeusFontFactory.getLabelFont());
        dlgQuestions.setDefaultCloseOperation(CoeusDlgWindow.DO_NOTHING_ON_CLOSE);
        dlgQuestions.setSize(WIDTH,HEIGHT);
        
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension dlgSize = dlgQuestions.getSize();
        dlgQuestions.setLocation(screenSize.width/2 - (dlgSize.width/2),
                screenSize.height/2 - (dlgSize.height/2));
        dlgQuestions.addWindowListener(new WindowAdapter(){
            
            public void windowClosing(WindowEvent we) {
                performCancelAction();
            }
        });
        
        
    }
    public void performCancelAction(){
        dlgQuestions.setVisible(false);
    }
    
    private void setTableEditors(){
        JTableHeader tableHeader = questionsForm.tblQuestions.getTableHeader();
        tableHeader.setReorderingAllowed(false);
        tableHeader.setFont(CoeusFontFactory.getLabelFont());
        
        questionsForm.tblQuestions.setRowHeight(22);
        questionsForm.tblQuestions.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        questionsForm.tblQuestions.setSelectionMode(
                DefaultListSelectionModel.SINGLE_SELECTION);
        questionsForm.tblQuestions.setBackground((Color) UIManager.getDefaults().get("Panel.background"));
        questionsForm.tblQuestions.setShowHorizontalLines(true);
        questionsForm.tblQuestions.setShowVerticalLines(true);
        questionsForm.tblQuestions.setOpaque(false);
        
        TableColumn columnDetails=questionsForm.tblQuestions.getColumnModel().getColumn(ID_COLUMN);
        columnDetails.setPreferredWidth(50);
        
        columnDetails=questionsForm.tblQuestions.getColumnModel().getColumn(DESCRIPTION_COLUMN);
        columnDetails.setPreferredWidth(650);
       
    }//End of setTableEditors
    
    public void dragDropEnd(java.awt.dnd.DragSourceDropEvent dsde) {
    }
    
    public void dragEnter(java.awt.dnd.DragSourceDragEvent dsde) {
    }
    
    public void dragExit(java.awt.dnd.DragSourceEvent dse) {
    }
    
    public void dragGestureRecognized(java.awt.dnd.DragGestureEvent dragGestureEvent) {
        CoeusVector cvTranferData = new CoeusVector();
        int row = questionsForm.tblQuestions.getSelectedRow();
        if(row != -1){
            YNQBean yNQBean;
            if(cvYNQData != null && cvYNQData.size() > 0){
                yNQBean = (YNQBean)cvYNQData.get(row);
                cvTranferData.addElement(yNQBean);
            }
            
            TransferableUserRoleData transferableData = new TransferableUserRoleData(cvTranferData);
            dragSource.startDrag(dragGestureEvent, dragCursor,transferableData, this);
        }
    }
    
    public void dragOver(java.awt.dnd.DragSourceDragEvent dsde) {
    }
    
    public void dropActionChanged(java.awt.dnd.DragSourceDragEvent dsde) {
    }
    
    public class CustomMouseAdapter extends MouseAdapter {
        public void mouseClicked(MouseEvent mouseEvent) {
            Point clickedPoint = mouseEvent.getPoint();
            int xPosition = (int)clickedPoint.getX();
            int selectedRow = questionsForm.tblQuestions.getSelectedRow();
            YNQBean yNQBean= null;
            if(selectedRow!= -1){
                yNQBean=(YNQBean)cvYNQData.get(selectedRow);
            }
            int columnIndex = questionsForm.tblQuestions.getColumnModel().getColumnIndexAtX(xPosition);
            switch (columnIndex) {
                case ID_COLUMN:
                    if(sortCodeAsc) {
                        cvYNQData.sort("questionId", false);
                        sortCodeAsc = false;
                    }else {
                        //Code already sorted in Descending order. Sort now in Ascending order.
                        cvYNQData.sort("questionId",true);
                        sortCodeAsc = true;
                    }
                    break;
                case DESCRIPTION_COLUMN:
                    if(sortDescAsc){
                        cvYNQData.sort("description",false);
                        sortDescAsc = false;
                    }else{
                        cvYNQData.sort("description",true);
                        sortDescAsc = true;
                    }
                    break;
            }
            
            questionsTableModel.fireTableDataChanged();
            if(selectedRow!= -1){
                questionsForm.tblQuestions.setRowSelectionInterval(
                        cvYNQData.indexOf(yNQBean),cvYNQData.indexOf(yNQBean));
                
                questionsForm.tblQuestions.scrollRectToVisible(
                        questionsForm.tblQuestions.getCellRect(
                        cvYNQData.indexOf(yNQBean) ,0, true));
            }else{
                questionsForm.tblQuestions.setRowSelectionInterval(0,0);
            }
        }
    }// End of ColumnHeaderListener.................
    
    
    public void actionPerformed(java.awt.event.ActionEvent e) {
        Object source = e.getSource();
//        try{
//            if(source.equals(questionsForm.btnMore)){
//                showMoreForm();
//            }
//        }catch (CoeusException ce){
//            ce.printStackTrace();
//            CoeusOptionPane.showErrorDialog(ce.getMessage());
//        }
    }
    
    public void showMoreForm()throws CoeusException{
        int selRow = questionsForm.tblQuestions.getSelectedRow();
        if(selRow == -1){
            CoeusOptionPane.showInfoDialog("Please Select a row");
            return ;
        }
        YNQBean ynqBean = (YNQBean)cvYNQData.get(selRow);
        
        String questionId = ynqBean.getQuestionId();
        String questionDesc = ynqBean.getDescription();
        
        CoeusVector cvYNQExp = getYNQExpData(questionId);
        
        String explanation = EMPTY_STRING;
        String policy = EMPTY_STRING;
        String regulation = EMPTY_STRING;
        
        for(int index = 0 ; index < cvYNQExp.size() ; index++){
            YNQExplanationBean ynqExplanationBean =
                    (YNQExplanationBean)cvYNQExp.get(index);
            
            if(ynqExplanationBean.getExplanationType().equalsIgnoreCase("E")){
                if(ynqExplanationBean.getExplanation() != null){
                    explanation = ynqExplanationBean.getExplanation();
                }//End of inner if
            }else if(ynqExplanationBean.getExplanationType().equalsIgnoreCase("P")){
                if(ynqExplanationBean.getExplanation() != null){
                    policy = ynqExplanationBean.getExplanation();
                }//End of inner if
            }else if(ynqExplanationBean.getExplanationType().equalsIgnoreCase("R")){
                if(ynqExplanationBean.getExplanation() != null){
                    regulation = ynqExplanationBean.getExplanation();
                }//End of inner if
            }
        }
        
        QuestionMoreForm more =
                new QuestionMoreForm(TypeConstants.DISPLAY_MODE,questionId,questionDesc,explanation,policy,regulation);
        more.setVisible(true);
    }
    
    
    private CoeusVector getYNQExpData(String questionId) throws CoeusException{
        CoeusVector cvData = null;
        
        RequesterBean request = new RequesterBean();
        ResponderBean response = null;
        
        request.setDataObject(questionId);
        request.setFunctionType(GET_YNQ_EXPLANATION);
        
        String connectTo = CoeusGuiConstants.CONNECTION_URL + RULES_SERVLET;
        
        AppletServletCommunicator comm = new AppletServletCommunicator(connectTo, request);
        comm.send();
        response = comm.getResponse();
        if(response!=null){
            if(response.isSuccessfulResponse()){
                cvData = (CoeusVector)response.getDataObject();
            }else{
                throw new CoeusException(response.getMessage(),0);
            }
        }
        return cvData;
    }
    
    
    public class QuestionsTableModel extends AbstractTableModel{
        private String []colNames ={"ID","Description"};
        private Class []colClass ={String.class,String.class};
        
        public int getColumnCount() {
            return colNames.length;
        }
        public String getColumnName(int col){
            return colNames[col];
        }
        public Class getColumnClass(int col){
            return colClass[col];
        }
        
        public int getRowCount() {
            if(cvYNQData == null){
                return 0;
            }else{
                return cvYNQData.size();
            }
        }
        
        private void setData(CoeusVector cvYNQData){
            cvYNQData = cvYNQData;
        }
        
        public Object getValueAt(int row, int col) {
            YNQBean yNQBean = (YNQBean)cvYNQData.get(row);
            switch(col){
                case ID_COLUMN :
                    return yNQBean.getQuestionId();
                case DESCRIPTION_COLUMN :
                    return yNQBean.getDescription();
            }
            return EMPTY_STRING;
        }
        
    }//End of QuestionsTableModel
    
    public void cleanUp(){
        dlgQuestions = null;
        questionsForm = null;
        cvYNQData = null;
    }
    
    
    
}
