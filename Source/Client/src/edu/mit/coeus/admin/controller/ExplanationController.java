/*
 * ExplanationController.java
 *
 * Created on December 2, 2004, 10:26 AM
 */

package edu.mit.coeus.admin.controller;

import edu.mit.coeus.admin.gui.ExplanationForm;
import edu.mit.coeus.gui.CoeusAppletMDIForm;
import edu.mit.coeus.gui.CoeusDlgWindow;
import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.admin.bean.YNQExplanationBean;
import edu.mit.coeus.utils.LimitedPlainDocument;
import edu.mit.coeus.utils.ScreenFocusTraversalPolicy;
import edu.mit.coeus.utils.TypeConstants;

import javax.swing.event.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
/**
 *
 * @author  chandrashekara
 */
public class ExplanationController extends AdminController implements ActionListener,ChangeListener{
    
    private ExplanationForm explanationForm;
    private boolean dataFrom;
    private static final String EMPTY_STRING="";
    private CoeusAppletMDIForm mdiForm;
    private CoeusDlgWindow dlgExplanation;
    private static final int WIDTH=610;
    private static final int HEIGHT = 245;
    private CoeusVector cvExplanation;
//    private CoeusVector cvTabForms ;
    private boolean isDataModified = false;
    private CoeusMessageResources coeusMessageResources;
    private char mode = TypeConstants.DISPLAY_MODE;
    
    private static final int QUESTION_TAB = 0;
    private static final int EXPLANATION_TAB = 1;
    private static final int POLICY_TAB = 2;
    private static final int REGULATION_TAB = 3;
    
    // Specifying the tab names
//    private static final String QUESTION_TAB = "Question";
//    private static final String EXPLANATION_TAB = "Explanation";
//    private static final String POLICY_TAB = "Policy";
//    private static final String REGULATION_TAB = "Regulation";
//    //Specifiying the tab index
//    private static final int QUESTION_TAB_INDEX = 0;
//    private static final int EXPLANATION_TAB_INDEX = 1;
//    private static final int POLICY_TAB_INDEX = 2;
//    private static final int REGULATION_TAB_INDEX = 3;
    private String questionId;
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
    
    /** Creates a new instance of ExplanationController */
    public ExplanationController(CoeusAppletMDIForm mdiForm, char mode) throws CoeusException{
            
        this.mdiForm = mdiForm;
        this.dataFrom = dataFrom;
//        this.cvExplanation = cvExplanation;
        setMode(mode);
        explanationForm = new ExplanationForm();
        coeusMessageResources = CoeusMessageResources.getInstance();
        registerComponents();
        formatFields();
        postInitComponents();
//        buildTabbedData();
    }
    
//    public void addListeners(){
//        for(int index=0; index <cvTabForms.size(); index++){
//            explanationForm = (ExplanationForm)cvTabForms.get(index);
//            explanationForm.btnCancel.addActionListener(this);
//            explanationForm.btnOk.addActionListener(this);
//        }
//    }
//    
    public void registerComponents() {
//        coeusTabbedPane = new CoeusTabbedPane(CoeusTabbedPane.CTRL_T);
//        coeusTabbedPane.addChangeListener(this); 
//        coeusTabbedPane.addChangeListener(this);
        explanationForm.tbdPnExplanation.addChangeListener(this);// For bug fix 1605
        explanationForm.btnCancel.addActionListener(this);
        explanationForm.btnOk.addActionListener(this);
        
    }
   
    private void postInitComponents() {
         final Component[] components = { explanationForm.btnOk, 
            explanationForm.btnCancel, explanationForm.tbdPnExplanation,explanationForm.txtArQuestion};
        ScreenFocusTraversalPolicy traversePolicy = new ScreenFocusTraversalPolicy( components );
        explanationForm.setFocusTraversalPolicy(traversePolicy);
        explanationForm.setFocusCycleRoot(true);

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
        
        //Added for bug fix # 1605 - starts
        explanationForm.tbdPnExplanation.getActionMap().put("CTRL+T", new AbstractAction("CTRL+T") {
            public void actionPerformed(ActionEvent actionEvent) {
                installCtrlT();
                setFocusTraversalComponents(components);
            }
        });
        explanationForm.tbdPnExplanation.addMouseListener(new MouseAdapter() {
           public void mouseClicked( MouseEvent me ) {
               setFocusTraversalComponents(components);
           }
        });// bug fix # 1605 - ends
    }
    
    //Added for bug fix # 1605 - starts
    private void installCtrlT() {
        int index = explanationForm.tbdPnExplanation.getSelectedIndex();
        if(index == explanationForm.tbdPnExplanation.getTabCount() -1) {
            //Already at the end set to first tab
            index = 0;
        }else {
            index = index + 1;
        }
        explanationForm.tbdPnExplanation.setSelectedIndex(index);
        //select the tab so as to make the next Ctrl+T work
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                int index = explanationForm.tbdPnExplanation.getSelectedIndex();
                explanationForm.tbdPnExplanation.getComponent(index).requestFocusInWindow();
            }
        });
    }// bug fix # 1605 - ends
    
    //Added for bug fix # 1605 - starts
    /* this method will sets selected tab components to the passed components array
     */
    private void setFocusTraversalComponents(final Component[] components) {
        if(explanationForm.tbdPnExplanation.getSelectedIndex() == EXPLANATION_TAB)
            components[components.length-1] = explanationForm.txtArExplanation;
        else if(explanationForm.tbdPnExplanation.getSelectedIndex() == POLICY_TAB)
            components[components.length-1] = explanationForm.txtArPolicy;
        else if(explanationForm.tbdPnExplanation.getSelectedIndex() == REGULATION_TAB)
            components[components.length-1] = explanationForm.txtArRegulation;
        else
            components[components.length-1] = explanationForm.txtArQuestion;
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                ((JComponent)components[components.length-1]).requestFocusInWindow();
            }
        });
    }// bug fix # 1605 - ends

    public void setFormData(Object data) throws edu.mit.coeus.exception.CoeusException {
        cvExplanation = (CoeusVector)data;
        explanationForm.txtArQuestion.setText(questionId+": "+description);

        YNQExplanationBean bean = (YNQExplanationBean)search(EXPLANATION,null);
        if(bean == null){
            bean = (YNQExplanationBean)search(EXPLANATION,TypeConstants.INSERT_RECORD);
        }
        if(bean != null){
            strBeanExplanation = bean.getExplanation();
            explanationForm.txtArExplanation.setText(strBeanExplanation);
        }

        bean = (YNQExplanationBean)search(POLICY,null);
        if(bean == null){
            bean = (YNQExplanationBean)search(POLICY,TypeConstants.INSERT_RECORD);
        }
        if(bean != null){
            strBeanPolicy = bean.getExplanation();
            explanationForm.txtArPolicy.setText(strBeanPolicy);
        }
        
        bean = (YNQExplanationBean)search(REGULATION,null);
        if(bean == null){
            bean = (YNQExplanationBean)search(REGULATION,TypeConstants.INSERT_RECORD);
        }
        if(bean != null){
            strBeanRegulation = bean.getExplanation();
            explanationForm.txtArRegulation.setText(strBeanRegulation);
        }

//        if(cvExplanation!=null && cvExplanation.size()>0){
//            YNQExplanationBean  yNQExplanationBean;
//            for(int index=0; index<cvExplanation.size(); index++){
//                yNQExplanationBean = (YNQExplanationBean)cvExplanation.get(index);
//                if(yNQExplanationBean.getExplanationType().equalsIgnoreCase(EXPLANATION)){
//                    strBeanExplanation = yNQExplanationBean.getExplanation();
//                    explanationForm.txtArExplanation.setText(strBeanExplanation);
//                }else if(yNQExplanationBean.getExplanationType().equalsIgnoreCase(POLICY)){
//                    strBeanPolicy = yNQExplanationBean.getExplanation();
//                    explanationForm.txtArPolicy.setText(strBeanPolicy);
//                }else if(yNQExplanationBean.getExplanationType().equalsIgnoreCase(REGULATION)){
//                    strBeanRegulation = yNQExplanationBean.getExplanation();
//                    explanationForm.txtArRegulation.setText(strBeanRegulation);
//                }
//            }
//        }
    }
    
    public void formatFields() {
        explanationForm.txtArQuestion.setEditable(false);
        explanationForm.txtArQuestion.setBackground(disabledBackground);
        explanationForm.txtArQuestion.setDocument(new LimitedPlainDocument(3000));
        explanationForm.txtArExplanation.setDocument(new LimitedPlainDocument(3000));
        explanationForm.txtArRegulation.setDocument(new LimitedPlainDocument(3000));
        if(getMode() == TypeConstants.DISPLAY_MODE){
            explanationForm.txtArExplanation.setEditable(false);
            explanationForm.txtArExplanation.setBackground(disabledBackground);
            explanationForm.txtArPolicy.setEditable(false);
            explanationForm.txtArPolicy.setBackground(disabledBackground);
            explanationForm.txtArRegulation.setEditable(false);
            explanationForm.txtArRegulation.setBackground(disabledBackground);
            explanationForm.btnOk.setEnabled(false);
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
        return explanationForm;
    }
    
    public Object getFormData() {
        return null;
    }
    
    public void actionPerformed(ActionEvent actionEvent) {
        Object source = actionEvent.getSource();
//        if(cvTabForms!= null && cvTabForms.size()>0){
//            for(int index=0; index < cvTabForms.size(); index++){
//                explanationForm = (ExplanationForm)cvTabForms.get(index);
//            }
//        }
        if(source.equals(explanationForm.btnCancel)){
            performCancelAction();
        }else if(source.equals(explanationForm.btnOk)){
            setSaveData();
        }
    }

    public boolean isDataChanged(){
        return isDataModified;
    }

    private boolean isExplanationDataChanged(){
        String strFormExplanation = explanationForm.txtArExplanation.getText().trim();
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
        String strFormPolicy = explanationForm.txtArPolicy.getText().trim();
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
        String strFormRegulation = explanationForm.txtArRegulation.getText().trim();
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
     * To search an object(YNQExplanationBean) in the CoeusVector(cvExplanation)
     * @param explanationType String
     * @param acType String
     * @return Object
     **/
    private Object search(String explanationType, String acType){
        try{
            YNQExplanationBean bean = null;
            for(int i=0;i<cvExplanation.size();i++){
                bean = (YNQExplanationBean)cvExplanation.elementAt(i);
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
        if(cvExplanation == null){
            cvExplanation = new CoeusVector();
        }
        if(isExplanationDataChanged()){
            if(explanationForm.txtArExplanation.getText() == null || explanationForm.txtArExplanation.getText().trim().equals(EMPTY_STRING)){
                deleteBean(EXPLANATION);
            }else{
                YNQExplanationBean bean = (YNQExplanationBean)search(EXPLANATION,null);
                YNQExplanationBean modifiedBean = (YNQExplanationBean)search(EXPLANATION,TypeConstants.INSERT_RECORD);
                if(bean != null){
                    bean.setAcType(TypeConstants.DELETE_RECORD);
                }
                if(modifiedBean != null){
                    modifiedBean.setExplanation(explanationForm.txtArExplanation.getText().trim());
                }else{
                    YNQExplanationBean insertBean = createBean(EXPLANATION,explanationForm.txtArExplanation.getText().trim());
                    cvExplanation.add(insertBean);
                }
            }
        }
        if(isPolicyDataChanged()){
            if(explanationForm.txtArPolicy.getText() == null || explanationForm.txtArPolicy.getText().trim().equals(EMPTY_STRING)){
                deleteBean(POLICY);
            }else{
                YNQExplanationBean bean = (YNQExplanationBean)search(POLICY,null);
                YNQExplanationBean modifiedBean = (YNQExplanationBean)search(POLICY,TypeConstants.INSERT_RECORD);
                if(bean != null){
                    bean.setAcType(TypeConstants.DELETE_RECORD);
                }
                if(modifiedBean != null){
                    modifiedBean.setExplanation(explanationForm.txtArPolicy.getText().trim());
                }else{
                    YNQExplanationBean insertBean = createBean(POLICY,explanationForm.txtArPolicy.getText().trim());
                    cvExplanation.add(insertBean);
                }
            }
        }
        if(isRegulationDataChanged()){
            if(explanationForm.txtArRegulation.getText() == null || explanationForm.txtArRegulation.getText().trim().equals(EMPTY_STRING)){
                deleteBean(REGULATION);
            }else{
                YNQExplanationBean bean = (YNQExplanationBean)search(REGULATION,null);
                YNQExplanationBean modifiedBean = (YNQExplanationBean)search(REGULATION,TypeConstants.INSERT_RECORD);
                if(bean != null){
                    bean.setAcType(TypeConstants.DELETE_RECORD);
                }
                if(modifiedBean != null){
                    modifiedBean.setExplanation(explanationForm.txtArRegulation.getText().trim());
                }else{
                    YNQExplanationBean insertBean = createBean(REGULATION,explanationForm.txtArRegulation.getText().trim());
                    cvExplanation.add(insertBean);
                }
            }
        }
        dlgExplanation.dispose();
    }
    private void deleteBean(String explanationType){
        YNQExplanationBean bean = (YNQExplanationBean)search(explanationType,null);
        YNQExplanationBean modifiedBean = (YNQExplanationBean)search(explanationType,TypeConstants.INSERT_RECORD);
        if(bean != null){
            bean.setAcType(TypeConstants.DELETE_RECORD);
        }
        if(modifiedBean != null){
            YNQExplanationBean deleteBean = (YNQExplanationBean)search(explanationType,TypeConstants.DELETE_RECORD);
            if(deleteBean != null){
                searchAndDelete(cvExplanation,modifiedBean,TypeConstants.INSERT_RECORD);
            }else{
                modifiedBean.setAcType(TypeConstants.DELETE_RECORD);
            }
            
        }
    }
    private YNQExplanationBean createBean(String explanationType,String value){
        YNQExplanationBean insertBean = new YNQExplanationBean();
        insertBean.setQuestionId(questionId);
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
            YNQExplanationBean bean = (YNQExplanationBean)object;
            YNQExplanationBean vectorBean = null;
            for(int i=0;i<coeusVector.size();i++){
                vectorBean = (YNQExplanationBean)coeusVector.elementAt(i);
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
      
    
    
//    public void buildTabbedData(){
//        // Set the tabs according to the specification - based on type
//          ExplanationForm explanationForm = new ExplanationForm();
//          cvTabForms = new CoeusVector();
//          coeusTabbedPane.addTab(QUESTION_TAB,explanationForm);
//          cvTabForms.add(explanationForm);
//          explanationForm = new ExplanationForm();
//          coeusTabbedPane.addTab(EXPLANATION_TAB, explanationForm);
//          cvTabForms.add(explanationForm);
//          explanationForm = new ExplanationForm();
//          coeusTabbedPane.addTab(POLICY_TAB, explanationForm);
//          cvTabForms.add(explanationForm);
//          explanationForm = new ExplanationForm();
//          coeusTabbedPane.addTab(REGULATION_TAB,explanationForm);
//          cvTabForms.add(explanationForm);
//          dlgExplanation.getContentPane().add(coeusTabbedPane);
//    }
    
    private void setWindowFocus(){
        explanationForm.btnCancel.requestFocusInWindow();
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
    
//    public void stateChanged(ChangeEvent changeEvent) {
//        int selIndex = coeusTabbedPane.getSelectedIndex();
//        switch(selIndex){
//            case QUESTION_TAB_INDEX:
//                if (cvTabForms!=null && cvTabForms.size()>0) {
//
//                }
//                break;
//        }
//    }    
    
    /** Getter for property questionId.
     * @return Value of property questionId.
     *
     */
    public java.lang.String getQuestionId() {
        return questionId;
    }    
    
    /** Setter for property questionId.
     * @param questionId New value of property questionId.
     *
     */
    public void setQuestionId(java.lang.String questionId) {
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
        int selectedIndex = explanationForm.tbdPnExplanation.getSelectedIndex();
        switch(selectedIndex) {
            case QUESTION_TAB :
                explanationForm.txtArQuestion.requestFocusInWindow();
                break;
            case EXPLANATION_TAB :
                explanationForm.txtArExplanation.requestFocusInWindow();
                explanationForm.txtArExplanation.setCaretPosition(0);
                break;
            case POLICY_TAB :
                explanationForm.txtArPolicy.requestFocus();
                explanationForm.txtArPolicy.setCaretPosition(0);
                break;
            case REGULATION_TAB :
                explanationForm.txtArRegulation.requestFocus();
                explanationForm.txtArRegulation.setCaretPosition(0);
                break;                
        }
    }
    
//    private boolean isDataChanged(){
//        
//        // Check for the Explanation tab data is changed or not.
//        
//        explanationForm = (ExplanationForm)cvTabForms.get(EXPLANATION_TAB_INDEX);
//        String explanation = explanationForm.txtArExplanation.getText().trim();
//        if(yNQExplanationBean!=null && yNQExplanationBean.getExplanation()!= null &&
//        (!yNQExplanationBean.getExplanation().trim().equals(EMPTY_STRING))){
//            if((!explanation.equals(yNQExplanationBean.getExplanation().trim()))){
//                modified = true;
//            }
//        }else{
//            if(explanation.length()>0){
//                modified = true;
//            }
//        }
//        // Check whether Policy tab data is changed or not.
//        explanationForm = (ExplanationForm)cvTabForms.get(POLICY_TAB_INDEX);
//        String policy = explanationForm.txtArExplanation.getText().trim();
//        if(yNQExplanationBean!=null && yNQExplanationBean.getExplanation()!= null &&
//        (!yNQExplanationBean.getExplanation().trim().equals(EMPTY_STRING))){
//            if((!policy.equals(yNQExplanationBean.getExplanation().trim()))){
//                modified = true;
//            }
//        }else{
//            if(policy.length()>0){
//                modified = true;
//            }
//        }
//        
//        // Check for the Regulation tab data is changed or not.
//        
//        explanationForm = (ExplanationForm)cvTabForms.get(REGULATION_TAB_INDEX);
//        String regulation = explanationForm.txtArExplanation.getText().trim();
//        if(yNQExplanationBean!=null && yNQExplanationBean.getExplanation()!= null &&
//        (!yNQExplanationBean.getExplanation().trim().equals(EMPTY_STRING))){
//            if((!regulation.equals(yNQExplanationBean.getExplanation().trim()))){
//                modified = true;
//            }
//        }else{
//            if(regulation.length()>0){
//                modified = true;
//            }
//        }
//        return modified;
//    }
    
//			} else if(functionType == UPDATE_YNQ) {
//                            boolean success = true;
//                            Hashtable data = (Hashtable)requester.getDataObject();
//                            if(data != null){
//				CoeusVector cvYNQ = (CoeusVector)data.get(YNQBean.class);
//				CoeusVector cvYNQExp = (CoeusVector)data.get(YNQExplanationBean.class);
//				AdminTxnBean adminTxnBean = new AdminTxnBean(loggedinUser);
//				YNQBean ynqBean = null;
//                                YNQExplanationBean ynqExplanationBean = null;
//				if(cvYNQExp != null && cvYNQExp.size() > 0) {
//					for(int index=0; index < cvYNQExp.size(); index++) {
//						ynqExplanationBean = (YNQExplanationBean)cvYNQExp.elementAt(index);
//						if(ynqExplanationBean.getAcType()==null){
//							continue;
//						}else if(ynqExplanationBean.getAcType().equals(TypeConstants.DELETE_RECORD)){
//                                                    success = adminTxnBean.updateYNQExplanation(ynqExplanationBean);
//                                                }
//					}
//				}
//				if(cvYNQ != null && cvYNQ.size() > 0) {
//					for(int index=0; index < cvYNQ.size(); index++) {
//						ynqBean = (YNQBean)cvYNQ.elementAt(index);
//						if(ynqBean.getAcType()==null){
//							continue;
//						}else if(ynqBean.getAcType().equals(TypeConstants.DELETE_RECORD)){
//                                                    success = adminTxnBean.updateYNQ(ynqBean);
//                                                }
//					}
//				}
//				if(cvYNQ != null && cvYNQ.size() > 0) {
//					for(int index=0; index < cvYNQ.size(); index++) {
//						ynqBean = (YNQBean)cvYNQ.elementAt(index);
//						if(ynqBean.getAcType()==null){
//							continue;
//						}else if(ynqBean.getAcType().equals(TypeConstants.INSERT_RECORD)){
//                                                    success = adminTxnBean.updateYNQ(ynqBean);
//                                                }
//					}
//				}
//				if(cvYNQExp != null && cvYNQExp.size() > 0) {
//					for(int index=0; index < cvYNQExp.size(); index++) {
//						ynqExplanationBean = (YNQExplanationBean)cvYNQExp.elementAt(index);
//						if(ynqExplanationBean.getAcType()==null){
//							continue;
//						}else if(ynqExplanationBean.getAcType().equals(TypeConstants.INSERT_RECORD)){
//                                                    success = adminTxnBean.updateYNQExplanation(ynqExplanationBean);
//                                                }
//					}
//				}
//                            }
//				responder.setResponseStatus(success);
//				responder.setMessage(null);
     
}
