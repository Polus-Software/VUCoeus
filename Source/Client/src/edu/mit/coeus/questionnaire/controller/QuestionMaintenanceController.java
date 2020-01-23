/*
 * QuestionMaintenanceController.java
 *
 * Created on September 22, 2006, 8:47 PM
 */

package edu.mit.coeus.questionnaire.controller;

import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.gui.CoeusAppletMDIForm;
import edu.mit.coeus.gui.CoeusDlgWindow;
import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.questionnaire.bean.QuestionBaseBean;
import edu.mit.coeus.questionnaire.bean.QuestionsMaintainanceBean;
import edu.mit.coeus.questionnaire.gui.QuestionMaintenanceForm;
import edu.mit.coeus.utils.AppletServletCommunicator;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.ComboBoxBean;
import edu.mit.coeus.utils.ScreenFocusTraversalPolicy;
import edu.mit.coeus.utils.query.Equals;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Vector;
import javax.swing.AbstractAction;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

/**
 *
 * @author  vinayks
 */
public class QuestionMaintenanceController extends QuestionnaireController implements ActionListener, ItemListener {
    
    private QuestionMaintenanceForm questionMaintenanceForm;
    private QuestionsTableModel questionsTableModel;
    private CoeusVector cvQuestionsData ;    
    private static final char GET_QUESTIONS = 'C';
    private static final char UPDATE_QUESTIONS = 'E';
    private static final String QUESTIONNAIRE_SERVLET = "/questionnaireServlet";
    private static final int QUESTION_ID_COLUMN=0;
    private static final int QUESTION_COLUMN=1;
    // 4272: Maintain history of Questionnaires - Start
//    private static final int VALID_ANSWER_COLUMN = 2;
//    private static final int LOOKUP_NAME_COLUMN=3;
//    private static final int LOOKUP_GUI_COLUMN=4;
    private static final int STATUS_COLUMN = 2; 
    private static final int VALID_ANSWER_COLUMN = 3;
    private static final int LOOKUP_NAME_COLUMN = 4;
    private static final int LOOKUP_GUI_COLUMN = 5;
    // 4272: Maintain history of Questionnaires - End
    private AddQuestionController addQuestionController;
    private CoeusDlgWindow dlgMaintananceQuestionsForm ;
    private CoeusAppletMDIForm mdiForm = CoeusGuiConstants.getMDIForm();
    private String WINDOW_TITLE = EMPTY_STRING;
    //Code modified for PT ID#2946 - Questionnaire enhancement - start
    private static final int WIDTH = 870;
    private static final int HEIGHT = 500;
    //Code modified for PT ID#2946 - Questionnaire enhancement - end
    private boolean navigation;
    private boolean okClicked;
    //Added for question group - start - 1
    private Vector cvQuestionGroup;
    //Added for question group - end - 1
     private CoeusVector vecQuestionExplanation;
    // 4272: Maintain history of Questionnaires - Start
    private static final char CHECK_QUESTION_USED_IN_QUESTIONNAIRE = 'X'; 
    private boolean isNewVersion; 
    private CoeusMessageResources coeusMessageResources;
    // 4272: Maintain history of Questionnaires - End
    
    // 4597: Questionaire maintenance is not checking for rights - Start
    private static final String MAINTAIN_QUESTIONS = "MAINTAIN_QUESTIONS";
    private static final String FN_USER_HAS_OSP_RIGHT = "FN_USER_HAS_OSP_RIGHT";
    private static final String FUNCTION_SERVLET = "/coeusFunctionsServlet";
    // 4597: Questionaire maintenance is not checking for rights - End
    /** Creates a new instance of QuestionMaintenanceController */
    public QuestionMaintenanceController(String title,String unitNumber) {
        WINDOW_TITLE = title;
        registerComponents();
        //Code added for PT ID#2946 - Questionnaire enhancement
        questionMaintenanceForm.lblMessage.setVisible(false);
        postInitComponents();
        // 4272: Maintain history of Questionnaires
        coeusMessageResources =CoeusMessageResources.getInstance();
    }
    
     public QuestionMaintenanceController(String title,String unitNumber, boolean navigation) {
        this.navigation = navigation;
        WINDOW_TITLE = title;
        registerComponents();
        setVisibleMenus();
        postInitComponents();
        // 4272: Maintain history of Questionnaires
        coeusMessageResources = CoeusMessageResources.getInstance();
    }
     
     private void setVisibleMenus(){
         WINDOW_TITLE = "Questions";
         questionMaintenanceForm.btnAdd.setText("OK");
         questionMaintenanceForm.btnAdd.setMnemonic('O');
         questionMaintenanceForm.btnDisplay.setVisible(false);
         questionMaintenanceForm.btnModify.setVisible(false);
         //Code added for PT ID#2946 - Questionnaire enhancement
         questionMaintenanceForm.lblMessage.setVisible(true);
     }
    
    /** This method creates and sets the display attributes for the dialog
     */
    public void postInitComponents(){
        dlgMaintananceQuestionsForm = new CoeusDlgWindow(mdiForm);
        dlgMaintananceQuestionsForm.setResizable(false);
        dlgMaintananceQuestionsForm.setModal(true);
        dlgMaintananceQuestionsForm.getContentPane().add(questionMaintenanceForm);
        dlgMaintananceQuestionsForm.setTitle(WINDOW_TITLE);
        dlgMaintananceQuestionsForm.setFont(CoeusFontFactory.getLabelFont());
        dlgMaintananceQuestionsForm.setSize(WIDTH, HEIGHT);
        
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension dlgSize = dlgMaintananceQuestionsForm.getSize();
        dlgMaintananceQuestionsForm.setLocation(screenSize.width/2 - (dlgSize.width/2),
        screenSize.height/2 - (dlgSize.height/2));
        
        dlgMaintananceQuestionsForm.addComponentListener(
        new ComponentAdapter(){
            public void componentShown(ComponentEvent e){
                setDefaultFocus();
            }
        });
        
        dlgMaintananceQuestionsForm.addEscapeKeyListener(new AbstractAction("escPressed"){
            public void actionPerformed(ActionEvent ae){
                performCloseAction();
            }
        });
        
        dlgMaintananceQuestionsForm.setDefaultCloseOperation(CoeusDlgWindow.DO_NOTHING_ON_CLOSE);
        dlgMaintananceQuestionsForm.addWindowListener(new WindowAdapter(){
            public void windowOpening(WindowEvent we){
                
            }
            public void windowClosing(WindowEvent we){
                performCloseAction();
            }
        });
        //code for disposing the window ends   
        // 4597: Questionaire maintenance is not checking for rights
        enableDisableButtons();
    }
    
    private void setDefaultFocus(){
        questionMaintenanceForm.btnAdd.requestFocusInWindow();
    }
    
    public void display() {
        if(questionMaintenanceForm.tblQuestions.getRowCount()>0){
            questionMaintenanceForm.tblQuestions.setRowSelectionInterval(0,0);
        }
        dlgMaintananceQuestionsForm.setVisible(true);
    }
    
    public void formatFields() {
    }
    
    public java.awt.Component getControlledUI() {
        return questionMaintenanceForm;
    }
    
    public Object getFormData() {
        return null;
    }
    
    public void registerComponents() {
        questionMaintenanceForm = new QuestionMaintenanceForm();
        questionsTableModel =new QuestionsTableModel();
        questionMaintenanceForm.tblQuestions.setModel(questionsTableModel);
        questionMaintenanceForm.btnAdd.addActionListener( this);
        questionMaintenanceForm.btnModify.addActionListener( this);
        questionMaintenanceForm.btnClose.addActionListener(this);
        questionMaintenanceForm.btnDisplay.addActionListener(this);
        //Added for question group - start - 2
        questionMaintenanceForm.cmbGroup.addItemListener(this);
        //Added for question group - end - 2
        cvQuestionsData = new CoeusVector();        
        
        Component component[]  = {
                //Added for question group - start - 3
                questionMaintenanceForm.cmbGroup,
                //Added for question group - end - 3
                questionMaintenanceForm.btnAdd,
                questionMaintenanceForm.btnModify,
                questionMaintenanceForm.btnDisplay,
                questionMaintenanceForm.btnClose                
            };
            ScreenFocusTraversalPolicy  traversal = new ScreenFocusTraversalPolicy(component);
            questionMaintenanceForm.setFocusTraversalPolicy(traversal);
            questionMaintenanceForm.setFocusCycleRoot(true);
        
    }
    
    public void saveFormData() throws edu.mit.coeus.exception.CoeusException {
    }
    
    public void setFormData(Object data) throws edu.mit.coeus.exception.CoeusException {
        cvQuestionsData = getTableData();
        questionsTableModel.setData(cvQuestionsData);
        setTableEditors();
        //Added for question group - start - 4
        populateQuestionGroups();
        //Added for question group - end - 4
    }
    
    public boolean validate() throws edu.mit.coeus.exception.CoeusUIException {
        return false ;
    }
    
    public void actionPerformed(java.awt.event.ActionEvent actionEvent) {
        Object source = actionEvent.getSource();
        try{
            if(source.equals(questionMaintenanceForm.btnClose)) {
                performCloseAction();
            } else if(source.equals(questionMaintenanceForm.btnDisplay)){
                performDisplayAction();   
            } else if(source.equals(questionMaintenanceForm.btnAdd)){
                performAddAction();
            } else if(source.equals(questionMaintenanceForm.btnModify)){
                performModifyAction();
            }
        }catch(CoeusException coeusException){
            coeusException.getMessage();
            CoeusOptionPane.showErrorDialog(coeusException.getMessage());
        }catch (Exception exception){
            exception.getMessage();
            CoeusOptionPane.showErrorDialog(exception.getMessage());
        }
    }
    
    public void performAddAction()throws CoeusException{
        if(navigation){
            getQuestionForQuestionnaire();
            setOkClicked(true);
            dlgMaintananceQuestionsForm.dispose();
        }else{
            //Modified for case#3083 - Question Maintenance gives exception if answer length is too long
            //addQuestionController = new AddQuestionController('M');
            // Case#: 3524: Add Explanation field to Questions Start
//            addQuestionController = new AddQuestionController('M', false);
            addQuestionController = new AddQuestionController('A', false);
            // Case#: 3524: Add Explanation field to Questions Start - End
            addQuestionController.setFormData(null);
            addQuestionController.display();
            if(addQuestionController.isOkClicked()) {
                QuestionsMaintainanceBean maintainanceBean =
                (QuestionsMaintainanceBean) addQuestionController.getFormData();
                maintainanceBean.setAcType("I");
                cvQuestionsData = updateData(maintainanceBean);
                // Case#: 3524: Add Explanation field to Questions - Start
                vecQuestionExplanation = addQuestionController.getVecQuestionExplanation();
                updateQuestionExplanation(vecQuestionExplanation);
                // Case#: 3524: Add Explanation field to Questions - End
                //Added for question group - start - 5
                questionsTableModel.setData(cvQuestionsData);                
                questionMaintenanceForm.tblQuestions.setModel(questionsTableModel);                
                //Added for question group - end - 5
                questionsTableModel.fireTableDataChanged();
            }
        }
    }
    
    public QuestionBaseBean getQuestionForQuestionnaire() throws CoeusException{
        int selRow = questionMaintenanceForm.tblQuestions.getSelectedRow();
        QuestionBaseBean baseBean = null;
        if(selRow!= -1){
            QuestionsMaintainanceBean bean  = (QuestionsMaintainanceBean)cvQuestionsData.get(selRow);
            baseBean = new QuestionBaseBean();
            baseBean.setQuestionId(bean.getQuestionId());
            // 4272: Maintain history of Questionnaires 
            baseBean.setVersionNumber(bean.getVersionNumber());
            baseBean.setDescription(bean.getDescription());
        }else{
            CoeusOptionPane.showInfoDialog("Please select a question");
        }
        return baseBean;
    }
    
    public void performModifyAction()throws CoeusException {
        //Modified for case#3083 - Question Maintenance gives exception if answer length is too long
        //addQuestionController = new AddQuestionController('M');        
        addQuestionController = new AddQuestionController('M', true);
        int selectedRow = questionMaintenanceForm.tblQuestions.getSelectedRow();
        //Added for question group - start - 6
        int groupTypeCodeBefore, groupTypeCodeAfter;
        //Added for question group - end - 6
        QuestionsMaintainanceBean bean;
        if(selectedRow != -1) {
            bean = (QuestionsMaintainanceBean)cvQuestionsData.get(selectedRow);
            // 4272: Maintain history of Questionnaires - Start
            boolean isQuestionUsed = checkQuestionUsedInQuestionnaire(bean);
            if(isQuestionUsed){
                int selectedOption = CoeusOptionPane.showQuestionDialog(coeusMessageResources.parseMessageKey("questions_exceptionCode.1014"),
                        CoeusOptionPane.OPTION_YES_NO,
                        CoeusOptionPane.DEFAULT_YES);
                if(selectedOption == CoeusOptionPane.SELECTION_YES) {
                    int version = bean.getVersionNumber();
                    bean.setVersionNumber( version + 1);
                    bean.setAcType("I");    
                    isNewVersion = true;
                } else {
                    isNewVersion = false;
                    return;
                }
            } else{
                isNewVersion = false;
                bean.setAcType("U");    
            }           
            addQuestionController.setIsNewVersion(isNewVersion);
            // 4272: Maintain history of Questionnaires - End
            addQuestionController.setFormData(bean);
            //Added for question group - start - 7
            groupTypeCodeBefore = getSelectedIndex(bean.getGroupTypeCode());            
            //Added for question group - end - 7
        } else {
            CoeusOptionPane.showInfoDialog("Select a Question to modify");
            return ;
        }
        addQuestionController.display();
        
        if(addQuestionController.isOkClicked()) {
            bean =(QuestionsMaintainanceBean) addQuestionController.getFormData();
            //Added for question group - start - 8
            groupTypeCodeAfter = getSelectedIndex(bean.getGroupTypeCode());
            //bean.setGroupTypeCode(groupTypeCodeAfter);
            //Added for question group - end - 8
            // 4272: Maintain history of Questionnaires 
           // bean.setAcType("U");             
            cvQuestionsData = updateData(bean);
            // Case#: 3524: Add Explanation field to Questions - Start
            vecQuestionExplanation = addQuestionController.getVecQuestionExplanation();
            updateQuestionExplanation(vecQuestionExplanation);
            // Case#: 3524: Add Explanation field to Questions - End
            questionsTableModel.fireTableDataChanged();   
            //Modified for question group - start - 1
            if(groupTypeCodeBefore == groupTypeCodeAfter){
                questionMaintenanceForm.tblQuestions.setRowSelectionInterval(selectedRow, selectedRow);            
            }
            //Modified for question group - end - 1
        }
    }
    
    public void performDisplayAction()throws CoeusException {
        //Modified for case#3083 - Question Maintenance gives exception if answer length is too long
        //addQuestionController = new AddQuestionController('M');        
        addQuestionController = new AddQuestionController('D', true);
        int selectedRow = questionMaintenanceForm.tblQuestions.getSelectedRow();
        QuestionsMaintainanceBean bean;
        if(selectedRow != -1) {
            bean = (QuestionsMaintainanceBean)cvQuestionsData.get(selectedRow);
            addQuestionController.setFormData(bean);
        } else {
            CoeusOptionPane.showInfoDialog("Select a Question to Display");
            return ;
        }
        addQuestionController.display();        
    }
    
    public class QuestionsTableModel extends AbstractTableModel{
        //Modified for case#3083 - Question Maintenance gives exception if answer length is too long
        //private String colNames[]={"Question Id","Question"};
        // 4272: Maintain history of Questionnaires - Start
        // private String colNames[]={"Id","Question"};
        private String colNames[]={"Id","Question", "Status"};
        private Class colClass[]={Integer.class,String.class, String.class};
        // 4272: Maintain history of Questionnaires - End
        public int getColumnCount() {
            return colNames.length;
        }
        
        public String getColumnName(int col){
            return colNames[col];
        }
        public Class getColumnClass(int col){
            return colClass[col];
        }
        public boolean isCellEditable(int row, int col){
            return false;
        }
        
        public int getRowCount() {
            if(cvQuestionsData==null){
                return 0;
            }else{
                return cvQuestionsData.size();
            }
        }
        
        public void setData(CoeusVector cvQuestionsData){
            cvQuestionsData = cvQuestionsData ;
        }
        
        public Object getValueAt(int row, int col) {
            QuestionsMaintainanceBean bean = (QuestionsMaintainanceBean)cvQuestionsData.get(row);
            switch(col){
                case QUESTION_ID_COLUMN:
                    return bean.getQuestionId();
                case QUESTION_COLUMN:
                    return bean.getDescription();
                case VALID_ANSWER_COLUMN:
                    return bean.getValidAnswers();
                case LOOKUP_NAME_COLUMN:
                    return bean.getLookupName();
                case LOOKUP_GUI_COLUMN:
                    return bean.getLookupGui();
                // 4272: Maintain history of Questionnaires
                case STATUS_COLUMN:
                    if("A".equalsIgnoreCase(bean.getStatus())){
                        return "Active";
                    } else{
                        return "Inactive";
                    }
                // 4272: Maintain history of Questionnaires - End
            }
            return EMPTY_STRING;
        }
    }
    
    public CoeusVector getTableData() throws CoeusException{
        CoeusVector cvQuestions = null ;
        RequesterBean requester = new RequesterBean();
        requester.setFunctionType( GET_QUESTIONS );
        AppletServletCommunicator comm = new AppletServletCommunicator(
        CoeusGuiConstants.CONNECTION_URL+QUESTIONNAIRE_SERVLET, requester);
        comm.send();
        ResponderBean response = comm.getResponse();
        if ( !response.isSuccessfulResponse() ){
            throw new CoeusException(response.getMessage());
        }else{
            cvQuestions  = (CoeusVector)response.getDataObject();
            // 4272: Maintain History of Questionnaires - Start
            if(navigation){
                // Filter only Active questions
                cvQuestions = (CoeusVector)cvQuestions.filter(new Equals("status", "A"));
            }
            // 4272: Maintain History of Questionnaires - End
        }
        return cvQuestions;
    }
    
    private void setTableEditors(){
        try{
            JTableHeader tableHeader = questionMaintenanceForm.tblQuestions.getTableHeader();
            tableHeader.setReorderingAllowed(false);
            tableHeader.addMouseListener(new ColumnHeaderListener());
            //Modified for case#3083 - Question Maintenance gives exception if answer length is too long
            //tableHeader.setMaximumSize(new Dimension(50,27));
            //tableHeader.setMinimumSize(new Dimension(50,27));
            //tableHeader.setPreferredSize(new Dimension(50,27));            
            tableHeader.setMaximumSize(new Dimension(50,22));
            tableHeader.setMinimumSize(new Dimension(50,22));
            tableHeader.setPreferredSize(new Dimension(50,22));            
            tableHeader.setFont(CoeusFontFactory.getLabelFont());
            
            questionMaintenanceForm.tblQuestions.setRowHeight(22);
            questionMaintenanceForm.tblQuestions.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
            questionMaintenanceForm.tblQuestions.setShowHorizontalLines(true);
            questionMaintenanceForm.tblQuestions.setShowVerticalLines(true);
            questionMaintenanceForm.tblQuestions.setOpaque(false);
            questionMaintenanceForm.tblQuestions.setSelectionMode(
            DefaultListSelectionModel.SINGLE_SELECTION);
            questionMaintenanceForm.tblQuestions.setRowSelectionAllowed(true);
            
            TableColumn columnDetails=questionMaintenanceForm.tblQuestions.getColumnModel().getColumn(QUESTION_ID_COLUMN);
            //Modified for case#3083 - Question Maintenance gives exception if answer length is too long            
            //columnDetails.setPreferredWidth(70);
            columnDetails.setPreferredWidth(50);
            
            columnDetails=questionMaintenanceForm.tblQuestions.getColumnModel().getColumn(QUESTION_COLUMN);
            // 4272: Maintain history of Questionnaires - Start
//            columnDetails.setPreferredWidth(678);
            columnDetails.setPreferredWidth(600);    
            columnDetails=questionMaintenanceForm.tblQuestions.getColumnModel().getColumn(STATUS_COLUMN);
            columnDetails.setPreferredWidth(80);
            // 4272: Maintain history of Questionnaires - End
        } catch(Exception e){
            e.printStackTrace();
            CoeusOptionPane.showErrorDialog(e.getMessage());
        }
    }
    
    public CoeusVector updateData(QuestionsMaintainanceBean bean) throws CoeusException{
        RequesterBean requester = new RequesterBean();
        CoeusVector cvQuestions = null ;
        requester.setDataObject(bean);
        requester.setFunctionType( UPDATE_QUESTIONS );
        AppletServletCommunicator comm = new AppletServletCommunicator(
        CoeusGuiConstants.CONNECTION_URL+QUESTIONNAIRE_SERVLET, requester);
        comm.send();
        ResponderBean response = comm.getResponse();
        if ( !response.isSuccessfulResponse() ){
            throw new CoeusException(response.getMessage());
        }else{
            cvQuestions  = (CoeusVector)response.getDataObject();
        }
        //Added for question group - start - 9        
        ComboBoxBean selected = (ComboBoxBean)cvQuestionGroup.get(questionMaintenanceForm.cmbGroup.getSelectedIndex());
        if(!selected.getCode().equals(EMPTY_STRING)){
            int groupTypeCode = Integer.parseInt(selected.getCode());        
            if(groupTypeCode != 0){
                cvQuestions = getGroupQuestions(cvQuestions, groupTypeCode);
            }
        }
        //Added for question group - end - 9
        return cvQuestions;
    }
    
    public void performCloseAction(){
        dlgMaintananceQuestionsForm.dispose();
    }
    
    /**
     * Getter for property okClicked.
     * @return Value of property okClicked.
     */
    public boolean isOkClicked() {
        return okClicked;
    }
    
    /**
     * Setter for property okClicked.
     * @param okClicked New value of property okClicked.
     */
    public void setOkClicked(boolean okClicked) {
        this.okClicked = okClicked;
    }
    
    /** This class will sort the column values in ascending and descending order
     *based on number of clicks. This will sort only questionId, name, description and status
     */
    
    public class ColumnHeaderListener extends MouseAdapter {
        String nameBeanId [][] ={
            {"0","questionId" },
            {"1","description" },
            //Added for COEUSDEV-250 : Question Maintenance window: sort by columns except Status - Start
            //To sort the status column in ascending and descending order
            {"2","status"}
            //COEUSDEV-250 : END
        };
        boolean sort =true;
        /**
         * @param evt
         */        
        public void mouseClicked(MouseEvent evt) {
            try {
                JTable table = ((JTableHeader)evt.getSource()).getTable();
                TableColumnModel colModel = table.getColumnModel();
                
                // The index of the column whose header was clicked
                int vColIndex = colModel.getColumnIndexAtX(evt.getX());
                if(cvQuestionsData!=null && cvQuestionsData.size()>0 &&
                nameBeanId [vColIndex][1].length() >1 ){
                    ((CoeusVector)cvQuestionsData).sort(nameBeanId [vColIndex][1],sort);
                    if(sort)
                        sort = false;
                    else
                        sort = true;
                    questionsTableModel.fireTableRowsUpdated(0, questionsTableModel.getRowCount());
                }
            } catch(Exception exception) {
                exception.getMessage();
            }
        }
    }// End of ColumnHeaderListener.................
    
    /**
     * Added for question group - start - 10
     * This method gets the question group types and sets 
     * the collection to question group combo box
     * @return void
     */
    public void populateQuestionGroups(){     
        cvQuestionGroup = null;
        String connectTo = CoeusGuiConstants.CONNECTION_URL + "/questionnaireServlet";
        RequesterBean requester = new RequesterBean();
        requester.setFunctionType('Q');
        AppletServletCommunicator comm = new AppletServletCommunicator(connectTo, requester);        
        comm.send();
        ResponderBean res = comm.getResponse();
        if (res != null){
            cvQuestionGroup = (Vector)res.getDataObject();
            if(cvQuestionGroup != null && cvQuestionGroup.size() > 0){                
                questionMaintenanceForm.cmbGroup.setModel(new DefaultComboBoxModel(cvQuestionGroup));
            }
        }
    }
    
    /**
     * This method detects change in combo box for groups
     * @param itemEvent
     * @return void
     */ 
     public void itemStateChanged(ItemEvent itemEvent){
        Object source = itemEvent.getSource();      
        int groupTypeCode;
        if(source.equals(questionMaintenanceForm.cmbGroup)){                        
            ComboBoxBean selected = (ComboBoxBean)cvQuestionGroup.get(questionMaintenanceForm.cmbGroup.getSelectedIndex());
            //Code modified for PT ID#2946 - Questionnaire enhancement - start
            if(!selected.getCode().toString().equals(EMPTY_STRING)){
                groupTypeCode = Integer.parseInt(selected.getCode());
            }else{
                groupTypeCode = 0;
            }
            //Code modified for PT ID#2946 - Questionnaire enhancement - end
            //Show the questions related to groups
            try{
                if(groupTypeCode != 0){
                    questionsTableModel = new QuestionsTableModel();
                    cvQuestionsData = getGroupQuestions(getTableData(), groupTypeCode);                                    
                }else{     
                    cvQuestionsData = getTableData();                    
                }       
                questionsTableModel.setData(cvQuestionsData);                
                questionMaintenanceForm.tblQuestions.setModel(questionsTableModel);
                setTableEditors(); 
            }catch(Exception ex){
                
            }
        }
     }
     
     /**      
      * This method filters questions based on group type code
      * @param cvQuestionsData
      * @param groupTypeCode
      * @return CoeusVector
      */
     private CoeusVector getGroupQuestions(CoeusVector cvQuestionsData, int groupTypeCode){
         CoeusVector cvGroupQuestionsData = new CoeusVector();
         int gTypeCode;
         for(int index = 0; index < cvQuestionsData.size(); index++){
             QuestionsMaintainanceBean questionsMaintainanceBean = (QuestionsMaintainanceBean)cvQuestionsData.get(index);
             gTypeCode = questionsMaintainanceBean.getGroupTypeCode();
             if(gTypeCode == groupTypeCode){
                 cvGroupQuestionsData.add(questionsMaintainanceBean);
             }             
         }
         return cvGroupQuestionsData;
     }
     
    /**
     * This method gets the selected index for the given group type code
     * @param theIndex
     * @param selectedIndex
     */
    private int getSelectedIndex(int theIndex){        
        ComboBoxBean comboBoxBean = null;
        int selectedIndex = 0;
        for(int index = 0; index < cvQuestionGroup.size(); index++){
            comboBoxBean = (ComboBoxBean)cvQuestionGroup.get(index);
            if(comboBoxBean.getCode().equals(Integer.toString(theIndex))){
                selectedIndex = index;
                break;
            }
        } 
        return selectedIndex;
    }     
    //Added for question group - end - 10

    // Case#: 3524: Add Explanation field to Questions - Start
    private void updateQuestionExplanation(CoeusVector vecQuestionExplanation) throws CoeusException {
        
        RequesterBean requester = new RequesterBean();
        requester.setDataObject(vecQuestionExplanation);
        requester.setFunctionType('U');
        AppletServletCommunicator comm = new AppletServletCommunicator(
                CoeusGuiConstants.CONNECTION_URL+QUESTIONNAIRE_SERVLET, requester);
        comm.send();
        ResponderBean response = comm.getResponse();
        if ( !response.isSuccessfulResponse() ){
            throw new CoeusException(response.getMessage());
        }
    }
// Case#: 3524: Add Explanation field to Questions - End
    // 4272: Maintain history of Questionnaires   - Strat
    
    private boolean checkQuestionUsedInQuestionnaire(QuestionsMaintainanceBean questionMaintBean) throws CoeusException{
        
        if(questionMaintBean != null){
            if(questionMaintBean.getVersionNumber() == 0){
                return false;
            }
        }
        boolean isQuestionUsed = false;
        RequesterBean requester = new RequesterBean();
        requester.setFunctionType( CHECK_QUESTION_USED_IN_QUESTIONNAIRE );
        requester.setDataObject(questionMaintBean);
        AppletServletCommunicator comm = new AppletServletCommunicator(
                CoeusGuiConstants.CONNECTION_URL+QUESTIONNAIRE_SERVLET, requester);
        comm.send();
        ResponderBean response = comm.getResponse();
        if ( !response.isSuccessfulResponse() ){
            throw new CoeusException(response.getMessage());
        }else{
            isQuestionUsed  = ((Boolean) response.getDataObject()).booleanValue();
        }
        return isQuestionUsed;
        
    }
    // 4272: Maintain history of Questionnaires - End

    // 4597: Questionaire maintenance is not checking for rights - Start
    private void enableDisableButtons() {
        boolean hasRight = fetchQuestionsRight();
        
        questionMaintenanceForm.btnAdd.setEnabled(hasRight);
        questionMaintenanceForm.btnModify.setEnabled(hasRight);
    }
    
    
    private boolean fetchQuestionsRight() {
        boolean hasQuestionsRight = false;
        String connectTo = CoeusGuiConstants.CONNECTION_URL+ FUNCTION_SERVLET;
        RequesterBean request = new RequesterBean();
        request.setId(MAINTAIN_QUESTIONS);
        request.setDataObject(FN_USER_HAS_OSP_RIGHT);
        AppletServletCommunicator comm
                = new AppletServletCommunicator(connectTo, request);
        comm.send();
        ResponderBean response = comm.getResponse();
        
        if (response.isSuccessfulResponse()) {
            if(response.getDataObject() != null){
                Boolean right = (Boolean) response.getDataObject();
                hasQuestionsRight = right.booleanValue();
            }else{
                CoeusOptionPane.showErrorDialog(response.getMessage()) ;
                hasQuestionsRight = false ;
            }
        }
        
        return  hasQuestionsRight ;
        
    }
    // 4597: Questionaire maintenance is not checking for rights - End
}
