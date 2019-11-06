/*
 * QuestionExplanationController.java
 *
 * Created on September 9, 2008, 3:42 PM
 *
 */

package edu.mit.coeus.questionnaire.controller;

import edu.mit.coeus.gui.URLOpener;
import edu.mit.coeus.questionnaire.gui.QuestionExplanationForm;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.gui.CoeusAppletMDIForm;
import edu.mit.coeus.gui.CoeusDlgWindow;
import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.gui.event.Controller;
import edu.mit.coeus.questionnaire.bean.QuestionExplanationBean;
import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.LimitedPlainDocument;
import edu.mit.coeus.utils.ScreenFocusTraversalPolicy;
import edu.mit.coeus.utils.TypeConstants;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URL;
import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.HTMLFrameHyperlinkEvent;

/**
 *
 * @author sreenathv
 */
public class QuestionExplanationController extends Controller implements ActionListener,ChangeListener {
    
    private QuestionExplanationForm questionExplanationForm;
    private boolean dataFrom;
    private static final String EMPTY_STRING="";
    private CoeusAppletMDIForm mdiForm;
    private CoeusDlgWindow dlgExplanation;
    private static final int WIDTH=610;
    private static final int HEIGHT = 245;
    private CoeusVector vecExplanation;
    
    private boolean isDataModified = false;
    private CoeusMessageResources coeusMessageResources;
    private char mode = TypeConstants.DISPLAY_MODE;
    
    private static final int QUESTION_TAB = 0;
    private static final int EXPLANATION_TAB = 1;
    private static final int POLICY_TAB = 2;
    private static final int REGULATION_TAB = 3;
    
    private int questionId;
    private String description;
    private java.awt.Color disabledBackground = (java.awt.Color) javax.swing.UIManager.
            getDefaults().get("Panel.background");
    
    private static final String POLICY = "P";
    private static final String EXPLANATION = "E";
    private static final String REGULATION = "R";
    private String strBeanExplanation = null;
    private String strBeanPolicy = null;
    private String strBeanRegulation = null;
    private static final String SAVE_CONFIRMATION = "saveConfirmCode.1002";
    // 4272: Maintain history of Questionnaire
    private int questionVersionNumber;
    
    /** Creates a new instance of QuestionExplanationController */
    public QuestionExplanationController(CoeusAppletMDIForm mdiForm, char mode) throws CoeusException{
        
        this.mdiForm = mdiForm;
        this.dataFrom = dataFrom;
        setMode(mode);
        questionExplanationForm = new QuestionExplanationForm();
        coeusMessageResources = CoeusMessageResources.getInstance();
        registerComponents();
        formatFields();
        postInitComponents();
    }
    
    public void registerComponents() {
        questionExplanationForm.tbdPnExplanation.addChangeListener(this);// For bug fix 1605
        questionExplanationForm.btnCancel.addActionListener(this);
        questionExplanationForm.btnOk.addActionListener(this);
        
    }
    
    private void postInitComponents() {
        final Component[] components = { questionExplanationForm.btnOk,
        questionExplanationForm.btnCancel, questionExplanationForm.tbdPnExplanation,questionExplanationForm.txtArQuestion};
        ScreenFocusTraversalPolicy traversePolicy = new ScreenFocusTraversalPolicy( components );
        questionExplanationForm.setFocusTraversalPolicy(traversePolicy);
        questionExplanationForm.setFocusCycleRoot(true);
        
        dlgExplanation = new CoeusDlgWindow(mdiForm);
        dlgExplanation.getContentPane().add(getControlledUI());
        dlgExplanation.setTitle("Question: More");
        dlgExplanation.setFont(CoeusFontFactory.getLabelFont());
        dlgExplanation.setModal(true);
        dlgExplanation.setResizable(false);
        dlgExplanation.setSize(WIDTH,HEIGHT);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension dlgSize = dlgExplanation.getSize();
        dlgExplanation.setLocation(screenSize.width/2 - (dlgSize.width/2),
                screenSize.height/2 - (dlgSize.height/2));
        
        dlgExplanation.addEscapeKeyListener(
                new AbstractAction("escPressed"){
            public void actionPerformed(ActionEvent ae){
                performCancelAction();
                return;
            }
        });
        
        dlgExplanation.setDefaultCloseOperation(CoeusDlgWindow.DO_NOTHING_ON_CLOSE);
        dlgExplanation.addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent we){
                performCancelAction();
                return;
            }
        });
        
        dlgExplanation.addComponentListener(
                new ComponentAdapter(){
            public void componentShown(ComponentEvent e){
                setWindowFocus();
            }
        });
        
        questionExplanationForm.tbdPnExplanation.getActionMap().put("CTRL+T", new AbstractAction("CTRL+T") {
            public void actionPerformed(ActionEvent actionEvent) {
                installCtrlT();
                setFocusTraversalComponents(components);
            }
        });
        questionExplanationForm.tbdPnExplanation.addMouseListener(new MouseAdapter() {
            public void mouseClicked( MouseEvent me ) {
                setFocusTraversalComponents(components);
            }
        });
    }
    
    
    private void installCtrlT() {
        int index = questionExplanationForm.tbdPnExplanation.getSelectedIndex();
        if(index == questionExplanationForm.tbdPnExplanation.getTabCount() -1) {
            //Already at the end set to first tab
            index = 0;
        }else {
            index = index + 1;
        }
        questionExplanationForm.tbdPnExplanation.setSelectedIndex(index);
        //select the tab so as to make the next Ctrl+T work
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                int index = questionExplanationForm.tbdPnExplanation.getSelectedIndex();
                questionExplanationForm.tbdPnExplanation.getComponent(index).requestFocusInWindow();
            }
        });
    }
    
    
    /* this method will sets selected tab components to the passed components array
     */
    private void setFocusTraversalComponents(final Component[] components) {
        if(questionExplanationForm.tbdPnExplanation.getSelectedIndex() == EXPLANATION_TAB)
            components[components.length-1] = questionExplanationForm.editorExplanation;
        else if(questionExplanationForm.tbdPnExplanation.getSelectedIndex() == POLICY_TAB)
            components[components.length-1] = questionExplanationForm.editorPolicy;
        else if(questionExplanationForm.tbdPnExplanation.getSelectedIndex() == REGULATION_TAB)
            components[components.length-1] = questionExplanationForm.ediotrRegulation;
        else
            components[components.length-1] = questionExplanationForm.txtArQuestion;
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                ((JComponent)components[components.length-1]).requestFocusInWindow();
            }
        });
    }
    
    public void setFormData(Object data) throws edu.mit.coeus.exception.CoeusException {
        questionExplanationForm.txtArQuestion.setText(questionId+": "+description);
        if(data != null){
            setVecExplanation((CoeusVector)data);
            
            QuestionExplanationBean bean = (QuestionExplanationBean)search(EXPLANATION,null);
            if(bean == null){
                bean = (QuestionExplanationBean)search(EXPLANATION,TypeConstants.INSERT_RECORD);
            }
            if(bean != null){
                strBeanExplanation = bean.getExplanation();
                questionExplanationForm.editorExplanation.setText(strBeanExplanation);
            }
            
            bean = (QuestionExplanationBean)search(POLICY,null);
            if(bean == null){
                bean = (QuestionExplanationBean)search(POLICY,TypeConstants.INSERT_RECORD);
            }
            if(bean != null){
                strBeanPolicy = bean.getExplanation();
                questionExplanationForm.editorPolicy.setText(strBeanPolicy);
            }
            
            bean = (QuestionExplanationBean)search(REGULATION,null);
            if(bean == null){
                bean = (QuestionExplanationBean)search(REGULATION,TypeConstants.INSERT_RECORD);
            }
            if(bean != null){
                strBeanRegulation = bean.getExplanation();
                questionExplanationForm.ediotrRegulation.setText(strBeanRegulation);
            }
        }
    }
    
    public void formatFields() {
        questionExplanationForm.txtArQuestion.setEditable(false);
        questionExplanationForm.txtArQuestion.setBackground(disabledBackground);
        questionExplanationForm.txtArQuestion.setDocument(new LimitedPlainDocument(3000));
        questionExplanationForm.editorExplanation.setDocument(new LimitedPlainDocument(3000));
        questionExplanationForm.ediotrRegulation.setDocument(new LimitedPlainDocument(3000));
        if(getMode() == TypeConstants.DISPLAY_MODE){
            questionExplanationForm.editorExplanation.setEditable(false);
            questionExplanationForm.editorExplanation.setBackground(disabledBackground);
            questionExplanationForm.editorPolicy.setEditable(false);
            questionExplanationForm.editorPolicy.setBackground(disabledBackground);
            questionExplanationForm.ediotrRegulation.setEditable(false);
            questionExplanationForm.ediotrRegulation.setBackground(disabledBackground);
            questionExplanationForm.btnOk.setEnabled(false);
            
            // Setting the Content type of the Fields to Display HTML data
            
            HTMLEditorKit htmlEdKit = new HTMLEditorKit();
            questionExplanationForm.editorExplanation.setEditorKit(htmlEdKit);
            questionExplanationForm.editorExplanation.setContentType("text/html");
            questionExplanationForm.editorPolicy.setEditorKit(htmlEdKit);
            questionExplanationForm.editorPolicy.setContentType("text/html");
            questionExplanationForm.ediotrRegulation.setEditorKit(htmlEdKit);
            questionExplanationForm.ediotrRegulation.setContentType("text/html");
            
            
            questionExplanationForm.editorExplanation.addHyperlinkListener(new HyperlinkListener() {
                public void hyperlinkUpdate(HyperlinkEvent he) {
                    try {
                        if(he.getEventType() == HyperlinkEvent.EventType.ACTIVATED){
                            URLOpener.openUrl(he.getURL());
                        }
                    }catch(Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            
            questionExplanationForm.editorPolicy.addHyperlinkListener(new HyperlinkListener() {
                public void hyperlinkUpdate(HyperlinkEvent he) {
                    try {
                        if(he.getEventType() == HyperlinkEvent.EventType.ACTIVATED){
                            URLOpener.openUrl(he.getURL());
                        }
                    }catch(Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            
            questionExplanationForm.ediotrRegulation.addHyperlinkListener(new HyperlinkListener() {
                public void hyperlinkUpdate(HyperlinkEvent he) {
                    try {
                        if(he.getEventType() == HyperlinkEvent.EventType.ACTIVATED){
                            URLOpener.openUrl(he.getURL());
                        }
                    }catch(Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            

        }
    }
    
    private void setMode(char mode){
        this.mode = mode;
    }
    private char getMode(){
        return mode;
    }
    public void display(){
        dlgExplanation.setVisible(true);
    }
    
    public java.awt.Component getControlledUI() {
        return questionExplanationForm;
    }
    
    public Object getFormData() {
        return null;
    }
    
    public void actionPerformed(ActionEvent actionEvent) {
        Object source = actionEvent.getSource();
        if(source.equals(questionExplanationForm.btnCancel)){
            performCancelAction();
        }else if(source.equals(questionExplanationForm.btnOk)){
            setSaveData();
        }
    }
    
    public boolean isDataChanged(){
        return isDataModified;
    }
    
    private boolean isExplanationDataChanged(){
        String strFormExplanation = questionExplanationForm.editorExplanation.getText().trim();
        if(getMode() == TypeConstants.MODIFY_MODE){
            if( (strBeanExplanation != null && strFormExplanation != null
                    && strFormExplanation.length() != strBeanExplanation.length())
                    || (strBeanExplanation != null
                    && (strFormExplanation == null || strFormExplanation.equals(EMPTY_STRING)))
                    || ((strBeanExplanation == null || strBeanExplanation.equals(EMPTY_STRING))
                    && (strFormExplanation != null && !strFormExplanation.equals(EMPTY_STRING))) ){
                isDataModified = true;
                return true;
            }
        }else if(getMode() == TypeConstants.ADD_MODE){
            if(strFormExplanation != null && strFormExplanation.length()>0){
                isDataModified = true;
                return true;
            }
        }
        return false;
    }
    
    private boolean isPolicyDataChanged(){
        String strFormPolicy = questionExplanationForm.editorPolicy.getText().trim();
        if(getMode() == TypeConstants.MODIFY_MODE){
            if( (strBeanPolicy != null && strFormPolicy != null
                    && strFormPolicy.length() != strBeanPolicy.length())
                    || (strBeanPolicy != null
                    && (strFormPolicy == null || strFormPolicy.equals(EMPTY_STRING)))
                    || ((strBeanPolicy == null || strBeanPolicy.equals(EMPTY_STRING))
                    && (strFormPolicy != null && !strFormPolicy.equals(EMPTY_STRING))) ){
                isDataModified = true;
                return true;
            }
        }else if(getMode() == TypeConstants.ADD_MODE){
            if(strFormPolicy != null && strFormPolicy.length()>0){
                isDataModified = true;
                return true;
            }
        }
        return false;
    }
    
    private boolean isRegulationDataChanged(){
        String strFormRegulation = questionExplanationForm.ediotrRegulation.getText().trim();
        if(getMode() == TypeConstants.MODIFY_MODE){
            if( (strBeanRegulation != null && strFormRegulation != null
                    && strFormRegulation.length() != strBeanRegulation.length())
                    || (strBeanRegulation != null
                    && (strFormRegulation == null || strFormRegulation.equals(EMPTY_STRING)))
                    || ((strBeanRegulation == null || strBeanRegulation.equals(EMPTY_STRING))
                    && (strFormRegulation != null && !strFormRegulation.equals(EMPTY_STRING))) ){
                isDataModified = true;
                return true;
            }
        }else if(getMode() == TypeConstants.ADD_MODE){
            if(strFormRegulation != null && strFormRegulation.length()>0){
                isDataModified = true;
                return true;
            }
        }
        return false;
    }
    
    /**
     * To search an object(QuestionExplanationBean) in the CoeusVector(vecQuestionExplanation)
     * 
     * 
     * @param explanationType String
     * @param acType String
     * @return Object
     */
    private Object search(String explanationType, String acType){
        try{
            QuestionExplanationBean bean = null;
            for(int i=0;i<getVecExplanation().size();i++){
                bean = (QuestionExplanationBean)getVecExplanation().elementAt(i);
                if(acType != null && bean.getAcType() != null
                        && !acType.equals(EMPTY_STRING)
                        && bean.getExplanationType().equalsIgnoreCase(explanationType)
                        && bean.getAcType().equals(acType)){
                    return bean;
                }else if( acType == null
                        && bean.getExplanationType().equalsIgnoreCase(explanationType)
                        && bean.getAcType() == null){
                    return bean;
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }
    private void setSaveData(){
        if(getVecExplanation() == null){
            setVecExplanation(new CoeusVector());
        }
        if(isExplanationDataChanged()){
            if(questionExplanationForm.editorExplanation.getText() == null || questionExplanationForm.editorExplanation.getText().trim().equals(EMPTY_STRING)){
                deleteBean(EXPLANATION);
            }else{
                QuestionExplanationBean bean = (QuestionExplanationBean)search(EXPLANATION,null);
                QuestionExplanationBean modifiedBean = (QuestionExplanationBean)search(EXPLANATION,TypeConstants.INSERT_RECORD);
                if(bean != null){
                    bean.setAcType(TypeConstants.DELETE_RECORD);
                }
                if(modifiedBean != null){
                    modifiedBean.setExplanation(questionExplanationForm.editorExplanation.getText().trim());
                }else{
                    QuestionExplanationBean insertBean = createBean(EXPLANATION,questionExplanationForm.editorExplanation.getText().trim());
                    getVecExplanation().add(insertBean);
                }
            }
        }
        if(isPolicyDataChanged()){
            if(questionExplanationForm.editorPolicy.getText() == null || questionExplanationForm.editorPolicy.getText().trim().equals(EMPTY_STRING)){
                deleteBean(POLICY);
            }else{
                QuestionExplanationBean bean = (QuestionExplanationBean)search(POLICY,null);
                QuestionExplanationBean modifiedBean = (QuestionExplanationBean)search(POLICY,TypeConstants.INSERT_RECORD);
                if(bean != null){
                    bean.setAcType(TypeConstants.DELETE_RECORD);
                }
                if(modifiedBean != null){
                    modifiedBean.setExplanation(questionExplanationForm.editorPolicy.getText().trim());
                }else{
                    QuestionExplanationBean insertBean = createBean(POLICY,questionExplanationForm.editorPolicy.getText().trim());
                    getVecExplanation().add(insertBean);
                }
            }
        }
        if(isRegulationDataChanged()){
            if(questionExplanationForm.ediotrRegulation.getText() == null || questionExplanationForm.ediotrRegulation.getText().trim().equals(EMPTY_STRING)){
                deleteBean(REGULATION);
            }else{
                QuestionExplanationBean bean = (QuestionExplanationBean)search(REGULATION,null);
                QuestionExplanationBean modifiedBean = (QuestionExplanationBean)search(REGULATION,TypeConstants.INSERT_RECORD);
                if(bean != null){
                    bean.setAcType(TypeConstants.DELETE_RECORD);
                }
                if(modifiedBean != null){
                    modifiedBean.setExplanation(questionExplanationForm.ediotrRegulation.getText().trim());
                }else{
                    QuestionExplanationBean insertBean = createBean(REGULATION,questionExplanationForm.ediotrRegulation.getText().trim());
                    getVecExplanation().add(insertBean);
                }
            }
        }
        dlgExplanation.dispose();
    }
    private void deleteBean(String explanationType){
        QuestionExplanationBean bean = (QuestionExplanationBean)search(explanationType,null);
        QuestionExplanationBean modifiedBean = (QuestionExplanationBean)search(explanationType,TypeConstants.INSERT_RECORD);
        if(bean != null){
            bean.setAcType(TypeConstants.DELETE_RECORD);
        }
        if(modifiedBean != null){
            QuestionExplanationBean deleteBean = (QuestionExplanationBean)search(explanationType,TypeConstants.DELETE_RECORD);
            if(deleteBean != null){
                searchAndDelete(getVecExplanation(), modifiedBean,TypeConstants.INSERT_RECORD);
            }else{
                modifiedBean.setAcType(TypeConstants.DELETE_RECORD);
            }
            
        }
    }
    private QuestionExplanationBean createBean(String explanationType,String value){
        QuestionExplanationBean insertBean = new QuestionExplanationBean();
        insertBean.setQuestionId(new Integer(questionId));
        // 4272: Maintain history of Questionnaire
        insertBean.setVersionNumber(questionVersionNumber);
        insertBean.setExplanationType(explanationType);
        insertBean.setExplanation(value);
        insertBean.setAcType(TypeConstants.INSERT_RECORD);
        return insertBean;
    }
    /**
     * Method to Search a Bean and delete it
     * @param coeusVector CoeusVector
     * @param object Object
     * @return void
     **/
    private void searchAndDelete(CoeusVector coeusVector, Object object, String acType){
        if(object != null && coeusVector != null){
            QuestionExplanationBean bean = (QuestionExplanationBean)object;
            QuestionExplanationBean vectorBean = null;
            for(int i=0;i<coeusVector.size();i++){
                vectorBean = (QuestionExplanationBean)coeusVector.elementAt(i);
                if(vectorBean.getAcType()!=null
                        && vectorBean.getQuestionId().equals(bean.getQuestionId())
                        && vectorBean.getExplanationType().equals(bean.getExplanationType())
                        && vectorBean.getAcType().equals(acType) ){
                    coeusVector.removeElementAt(i);
                    break;
                }
            }
        }
    }
    public boolean validate() throws edu.mit.coeus.exception.CoeusUIException {
        return false;
    }
    
    public void saveFormData() throws edu.mit.coeus.exception.CoeusException {
    }
    
    
    
    
    private void setWindowFocus(){
        questionExplanationForm.btnCancel.requestFocusInWindow();
    }
    
    private void performCancelAction(){
        if(isExplanationDataChanged() || isPolicyDataChanged() || isRefreshRequired()){
            performWindowClosing();
        }else{
            isDataModified = false;
            dlgExplanation.setVisible(false);
        }
    }
    
    public void performWindowClosing(){
        int option = CoeusOptionPane.showQuestionDialog(
                coeusMessageResources.parseMessageKey(SAVE_CONFIRMATION),
                CoeusOptionPane.OPTION_YES_NO_CANCEL,CoeusOptionPane.DEFAULT_CANCEL);
        if(option == CoeusOptionPane.SELECTION_YES){
            setSaveData();
        }else if(option == CoeusOptionPane.SELECTION_NO){
            isDataModified = false;
            dlgExplanation.dispose();
        }else if(option==CoeusOptionPane.SELECTION_CANCEL){
            return;
        }
    }
    /** Getter for property questionId.
     * @return Value of property questionId.
     *
     */
    public int getQuestionId() {
        return questionId;
    }
    
    /** Setter for property questionId.
     * @param questionId New value of property questionId.
     *
     */
    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }
    
    /** Getter for property description.
     * @return Value of property description.
     *
     */
    public java.lang.String getDescription() {
        return description;
    }
    
    /** Setter for property description.
     * @param description New value of property description.
     *
     */
    public void setDescription(java.lang.String description) {
        this.description = description;
    }
    // For bug fix 1605
    public void stateChanged(ChangeEvent e) {
        int selectedIndex = questionExplanationForm.tbdPnExplanation.getSelectedIndex();
        switch(selectedIndex) {
            case QUESTION_TAB :
                questionExplanationForm.txtArQuestion.requestFocusInWindow();
                break;
            case EXPLANATION_TAB :
                questionExplanationForm.editorExplanation.requestFocusInWindow();
                questionExplanationForm.editorExplanation.setCaretPosition(0);
                break;
            case POLICY_TAB :
                questionExplanationForm.editorPolicy.requestFocus();
                questionExplanationForm.editorPolicy.setCaretPosition(0);
                break;
            case REGULATION_TAB :
                questionExplanationForm.ediotrRegulation.requestFocus();
                questionExplanationForm.ediotrRegulation.setCaretPosition(0);
                break;
        }
    }

    public CoeusVector getVecExplanation() {
        return vecExplanation;
    }

    public void setVecExplanation(CoeusVector vecExplanation) {
        this.vecExplanation = vecExplanation;
    }
    // 4272: Maintain history of Questionnaire - Start
    public void setQuestionVersionNumber(int versionNumber) {
        questionVersionNumber = versionNumber;
    }
    // 4272: Maintain history of Questionnaire - End
}
