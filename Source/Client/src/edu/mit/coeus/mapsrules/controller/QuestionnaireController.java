/*
 * @(#)QuestionnaireController.java 1.0 08/31/07
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

/* PMD check performed, and commented unused imports and
 * variables on 13-FEB-2008 by Leena
 */
package edu.mit.coeus.mapsrules.controller;

import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.exception.CoeusUIException;
import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.gui.event.Controller;
import edu.mit.coeus.mapsrules.gui.QuestionnaireForm;
import edu.mit.coeus.questionnaire.bean.QuestionnaireBaseBean;
import edu.mit.coeus.questionnaire.bean.QuestionnaireQuestionsBean;
import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.tree.TransferableUserRoleData;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Insets;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSource;
import java.awt.dnd.DragSourceDragEvent;
import java.awt.dnd.DragSourceDropEvent;
import java.awt.dnd.DragSourceEvent;
import java.awt.dnd.DragSourceListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

/**
 * This class will be used as the controller for the questionnaire tab in the
 * ConditionEditorForm for business rules
 *
 * @author leenababu
 */
public class QuestionnaireController extends Controller implements DragGestureListener,
        DragSourceListener{
    private QuestionnaireForm questionnaireForm;
    private CoeusVector cvQuestionnaire;
    private Map hmQuestionnaireQuestions;
    private Map hmQuestions;
    
    private int TYPE_COLUNM = 0;
    private int QUESTIONNAIRE_ID_COLUMN = 1;
    private int QUESTIONNNAIRE_QN_ID_COLUMN = 2;
    private int DESC_COLUMN = 3;
    
    private String EMPTY_STRING = "";
    private DragSource dragSource;
    Cursor dragCursor = new Cursor(Cursor.HAND_CURSOR);
    
    /** Creates a new instance of QuestionnaireController */
    public QuestionnaireController() {
        questionnaireForm = new QuestionnaireForm();
        registerComponents();
    }
    
    public Component getControlledUI() {
        return questionnaireForm;
    }
    
    public void setFormData(Object data) throws CoeusException {
        if(data!=null && ((CoeusVector)data).size() == 2){
            CoeusVector cvQuestionaireData = (CoeusVector)data;
            cvQuestionnaire = (CoeusVector)cvQuestionaireData.get(0);
            hmQuestionnaireQuestions = (HashMap)cvQuestionaireData.get(1);
            populateQuestionnaireTable();
        }
    }
    
    /**
     * Returns the questionBean given the questionId
     * @param questionId question id
     */
    public QuestionnaireQuestionsBean getQuestion(String questionId){
        if(questionId ==null || questionId.equals(EMPTY_STRING)){
            return null;
        }else{
            return (QuestionnaireQuestionsBean)hmQuestions.get(questionId);
        }
    }
    
    public void populateQuestionnaireTable(){
        Vector tableDataVector = new Vector();
        Vector tableRowVector = null;
        Vector columnVector = new Vector(4);
        CoeusVector cvQuestions ;
        columnVector.add(null);
        columnVector.add(null);
        columnVector.add(null);
        columnVector.add(null);
        QuestionnaireBaseBean questionnaireBaseBean = null;
        hmQuestions = new HashMap();
        for(int i = 0; i< cvQuestionnaire.size(); i++){
            tableRowVector = new Vector();
            //"Q" represent the questionnaire and QQ represents a Questionniare Question
            tableRowVector.add("Q");
            questionnaireBaseBean = (QuestionnaireBaseBean)((CoeusVector)cvQuestionnaire.get(i)).get(0);
            tableRowVector.add(Integer.toString(questionnaireBaseBean.getQuestionnaireId()));
            tableRowVector.add(EMPTY_STRING);
            tableRowVector.add(questionnaireBaseBean.getName());
            tableDataVector.add(tableRowVector);
            cvQuestions = (CoeusVector)hmQuestionnaireQuestions.get(Integer.toString(questionnaireBaseBean.getQuestionnaireId()));
            
            if(cvQuestions!=null){
                for(int j=0; j<cvQuestions.size(); j++){
                    tableRowVector = new Vector();
                    tableRowVector.add("QQ");
                    tableRowVector.add(Integer.toString(questionnaireBaseBean.getQuestionnaireId()));
                    tableRowVector.add(((QuestionnaireQuestionsBean)cvQuestions.get(j)).getQuestionId());
                    tableRowVector.add(((QuestionnaireQuestionsBean)cvQuestions.get(j)).getQuestionId()+" : "+((QuestionnaireQuestionsBean)cvQuestions.get(j)).getDescription());
                    tableDataVector.add(tableRowVector);
                    hmQuestions.put(((QuestionnaireQuestionsBean)cvQuestions.get(j)).getQuestionId().toString(),
                            (QuestionnaireQuestionsBean)cvQuestions.get(j));
                }
            }
        }
        QuestionnaireTableModel questionnaireTableModel = new QuestionnaireTableModel();
        questionnaireTableModel.setDataVector(tableDataVector, columnVector);
        questionnaireForm.tblQuestionnaire.setModel(questionnaireTableModel);
        questionnaireForm.tblQuestionnaire.setCellSelectionEnabled(false);
        questionnaireForm.tblQuestionnaire.setRowSelectionAllowed(true);
        questionnaireForm.tblQuestionnaire.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        questionnaireForm.tblQuestionnaire.getTableHeader().getColumnModel().getColumn(0).setMinWidth(0);
        questionnaireForm.tblQuestionnaire.getTableHeader().getColumnModel().getColumn(0).setMaxWidth(0);
        questionnaireForm.tblQuestionnaire.getTableHeader().getColumnModel().getColumn(0).setPreferredWidth(0);
        questionnaireForm.tblQuestionnaire.getTableHeader().getColumnModel().getColumn(1).setMinWidth(0);
        questionnaireForm.tblQuestionnaire.getTableHeader().getColumnModel().getColumn(1).setMaxWidth(0);
        questionnaireForm.tblQuestionnaire.getTableHeader().getColumnModel().getColumn(1).setPreferredWidth(0);
        questionnaireForm.tblQuestionnaire.getTableHeader().getColumnModel().getColumn(2).setMinWidth(0);
        questionnaireForm.tblQuestionnaire.getTableHeader().getColumnModel().getColumn(2).setMaxWidth(0);
        questionnaireForm.tblQuestionnaire.getTableHeader().getColumnModel().getColumn(2).setPreferredWidth(0);
        questionnaireForm.tblQuestionnaire.setTableHeader(null);
        questionnaireForm.tblQuestionnaire.setBackground((Color)UIManager.getDefaults().get("Panel.background"));
        questionnaireForm.tblQuestionnaire.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
        questionnaireForm.tblQuestionnaire.getColumnModel().getColumn(DESC_COLUMN).setCellRenderer(new QuestionnaireTableRenderer());
        questionnaireForm.tblQuestionnaire.setRowHeight(22);
        questionnaireForm.tblQuestionnaire.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        TableColumn columnDetails=questionnaireForm.tblQuestionnaire.getColumnModel().getColumn(DESC_COLUMN);
        columnDetails.setPreferredWidth(50);
        
    }
    public void display() {
        
    }
    
    public void formatFields() {
    }
    
    public Object getFormData() {
        return null;
    }
    
    public void registerComponents() {
        questionnaireForm.tblQuestionnaire.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e){
                if(e.getClickCount()==2){
                    int selectedRow = questionnaireForm.tblQuestionnaire.getSelectedRow();
                    if(selectedRow!=-1){
                        String type = questionnaireForm.tblQuestionnaire.getValueAt(selectedRow,0).toString();
                        if(type!=null && type.equals("QQ")){
                            QuestionnaireQuestionsBean questionnaireQuestionsBean =
                                    (QuestionnaireQuestionsBean)hmQuestions.
                                    get(questionnaireForm.tblQuestionnaire.getValueAt(selectedRow,2).toString());
                            if(questionnaireQuestionsBean!=null){
                                try {
                                    QuestionnaireQuestionFormController questionnaireQnFormController =
                                            new QuestionnaireQuestionFormController();
                                    questionnaireQnFormController.setFormData(questionnaireQuestionsBean);
                                    questionnaireQnFormController.display();
                                } catch (CoeusException ex) {
                                    ex.printStackTrace();
                                }
                            }
                        }
                    }
                }
            }
        });
        dragSource = new DragSource();
        dragSource.createDefaultDragGestureRecognizer(questionnaireForm.tblQuestionnaire,
                DnDConstants.ACTION_MOVE, this);
        
    }
    
    public void saveFormData() throws CoeusException {
    }
    
    public boolean validate() throws CoeusUIException {
        return false;
    }
    
    public void dragGestureRecognized(DragGestureEvent dge) {
        int selectedRow = questionnaireForm.tblQuestionnaire.getSelectedRow();
        if(selectedRow != -1 ){
            if(questionnaireForm.tblQuestionnaire.getValueAt(selectedRow,TYPE_COLUNM).toString().equals("QQ")){
                String questionId = questionnaireForm.tblQuestionnaire.getValueAt(selectedRow,QUESTIONNNAIRE_QN_ID_COLUMN).toString();
                QuestionnaireQuestionsBean questionnaireQnBean = (QuestionnaireQuestionsBean)hmQuestions.get(questionId);
                if(questionnaireQnBean!=null){
                    CoeusVector cvData = new CoeusVector();
                    cvData.add(questionnaireQnBean);
                    TransferableUserRoleData transferableData = new TransferableUserRoleData(cvData);
                    dragSource.startDrag( dge, dragCursor, transferableData, this);
                }
            }else{
                CoeusOptionPane.showInfoDialog("Please select a question");
            }
        }
    }
    
    public void dragDropEnd(DragSourceDropEvent dsde) {
    }
    
    public void dragEnter(DragSourceDragEvent dsde) {
    }
    
    public void dragExit(DragSourceEvent dse) {
    }
    
    public void dragOver(DragSourceDragEvent dsde) {
    }
    
    public void dropActionChanged(DragSourceDragEvent dsde) {
    }
    
    
    class QuestionnaireTableModel extends DefaultTableModel{
        public boolean isCellEditable(int row, int col){
            return false;
        }
    }
    class QuestionnaireTableRenderer extends DefaultTableCellRenderer{
        private javax.swing.JLabel label = new javax.swing.JLabel();
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            label.setText(value.toString());
            label.setFont(CoeusFontFactory.getNormalFont());
            label.setOpaque(true);
            label.setBorder(null);
            
            if(column == DESC_COLUMN){
                if(table.getValueAt(row,TYPE_COLUNM).toString().equals("Q")){
                    label.setFont(CoeusFontFactory.getLabelFont());
                }else{
                    label.setBorder(new CompoundBorder(new EmptyBorder(new Insets(1,4,1,4)),
                            label.getBorder()));
                }
                if(isSelected){
                    label.setBackground(table.getSelectionBackground());
                }else{
                    label.setBackground(table.getBackground());
                }
            }
            
            return label;
        }
        
    }
    
}
