/*
 * BudgetJustificationController.java
 *
 * Created on November 28, 2003, 12:40 PM
 */

package edu.mit.coeus.budget.controller;

import java.awt.Component;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JOptionPane;
import javax.swing.AbstractAction;
import javax.swing.event.DocumentListener;
import javax.swing.event.DocumentEvent;
import java.util.Hashtable;

import edu.mit.coeus.utils.TypeConstants;
import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.budget.gui.BudgetJustificationForm;
import edu.mit.coeus.utils.query.QueryEngine;
import edu.mit.coeus.utils.query.Or;
import edu.mit.coeus.utils.query.Equals;
import edu.mit.coeus.utils.query.NotEquals;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.CoeusDateFormat;
import edu.mit.coeus.utils.ScreenFocusTraversalPolicy;
import edu.mit.coeus.gui.CoeusDlgWindow;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.utils.LimitedPlainDocument;
import edu.mit.coeus.utils.UserUtils;

import edu.mit.coeus.budget.bean.BudgetInfoBean;
import edu.mit.coeus.budget.bean.BudgetDetailBean;
import edu.mit.coeus.budget.bean.BudgetJustificationBean;


/** Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 * @author  ranjeeva
 */
public class BudgetJustificationController extends Controller implements TypeConstants,ActionListener {
    
    
    /** Parent Component instance */
    private Component parent;
    /** Parameter For Model Form Window */
    boolean modal;
    /** BudgetJustification Form instance whihc form is UI */
    private BudgetJustificationForm budgetJustificationForm;
    /** CoeusMessageResources instance for Messages */
    private CoeusMessageResources coeusMessageResources;
    /** Query Engine instance */
    private QueryEngine queryEngine;
    
    
    /** instance of BudgetInfoBean */    
    private BudgetInfoBean budgetInfoBean;
    /** instance of queryKey
     * value equals ProposalNumber plus VersionNumber
     */    
    private String queryKey;
    /** CoeusVector Containing BudgetDetailBean */    
    private CoeusVector vecBudgetDetailBean;
    /** CoeusVector Containing BudgetJustificationBean */    
    private CoeusVector vecBudgetJustificationBean;
    /** instance OR operator for
     * actype null and Not equals Delete
     */    
    private Or acTypeNullandNotDelete;
    
    /** check if textArea content in UI is modified */    
    private  boolean isTextAreaContentModified = false;
    
    /** Constant String Containing Messages */    
    private static final String NO_LINEITEM_JUSTIFICATION = "budgetJustification__exceptionCode.1500"; //"There is no line Item budget justifications";
    /** Constant String for ACTYPE value */    
    private static final String ACTYPE = "acType";
    /** Constant String for NEWLINE value */    
    private static final String NEWLINE = "\n";
    /** Constant String for TITLE value */    
    private static final String TITLE = "Budget Justification ";
    /** Constant String for LINE SEPERATOR value */    
    private static final String LINE_SEPERATOR = "*****************************************************";
    
    /** Constant String for save Changes messsage */    
    private final String SAVE_CHANGES = "budget_saveChanges_exceptionCode.1210";
    
    /** Creates a new instance of BudgetJustificationController */
    public BudgetJustificationController() {
        super();
        this.parent= CoeusGuiConstants.getMDIForm();
        this.modal =  true;
        initialiseController();
    }
    
    
    /** Creates a new instance of BudgetJustificationController
     * with parameters
     * @param parent Component parent form
     * @param modal booelan if <CODE>true<CODE> modal window
     * @param budgetInfoBean BudgetInfoBean instance
     */    
    public BudgetJustificationController(Component parent,boolean modal,BudgetInfoBean budgetInfoBean) {
        super();
        this.parent= parent;
        this.modal =  modal;
        this.budgetInfoBean = budgetInfoBean;
        initialiseController();
    }
    
    /** Method to initialise the Controller */
    private void initialiseController() {
        if(budgetJustificationForm == null) {
            budgetJustificationForm =  new BudgetJustificationForm();   //(Component) parent,modal);
            // Added by chandra - To limit the size of the characters allowed
//            budgetJustificationForm.txtArBudgetJustification.setDocument(new LimitedPlainDocument(3878));
            // End chandra
        }
        
        queryEngine = QueryEngine.getInstance();
        coeusMessageResources = CoeusMessageResources.getInstance();
        
        registerComponents();
        formatFields();
        setFormData(budgetInfoBean);
    }
    
    
    /** displey Method to make form visible */
    public void display() {
        
        ((BudgetJustificationForm) getControlledUI()).setVisible(true);
    }
    
    /** method to set Visible the form on close */    
    public void close() {
        ((BudgetJustificationForm) getControlledUI()).setVisible(false);
    }
    
    
    /** Method to Format the Field in Ui based on function Type */
    public void formatFields() {
        
        if(getFunctionType() == DISPLAY_MODE) {
            budgetJustificationForm.btnOk.setEnabled(false);
            budgetJustificationForm.btnConsolidate.setEnabled(false);
            budgetJustificationForm.txtArBudgetJustification.setEditable(false);
            budgetJustificationForm.txtArBudgetJustification.setBackground((java.awt.Color) javax.swing.UIManager.getDefaults().get("Panel.background"));
            
        } else {
            budgetJustificationForm.btnOk.setEnabled(true);
            budgetJustificationForm.btnConsolidate.setEnabled(true);
        }
        
    }
    
    /** Method to return an instance of UI form
     * @return Component instaince of BudgetJustificationForm
     */
    public java.awt.Component getControlledUI() {
        
        return budgetJustificationForm;
    }
    
    /** get the Form Data
     * @return Object
     */
    public Object getFormData() {
        
        return budgetJustificationForm;
    }
    
    /** To Register  all the Actionlistner for the Components of UI */
    public void registerComponents() {
        
        budgetJustificationForm.btnOk.addActionListener(this);
        budgetJustificationForm.btnCancel.addActionListener(this);
        budgetJustificationForm.btnConsolidate.addActionListener(this);
        budgetJustificationForm.setDefaultCloseOperation(CoeusDlgWindow.DO_NOTHING_ON_CLOSE);
        budgetJustificationForm.txtArBudgetJustification.getDocument().addDocumentListener(new TextAreaDocumentListener());
        
        // Travel all the components while pressing tab button
        java.awt.Component[] components = {
            budgetJustificationForm.txtArBudgetJustification,
            budgetJustificationForm.btnOk,
            budgetJustificationForm.btnCancel,
            budgetJustificationForm.btnConsolidate
        };
        ScreenFocusTraversalPolicy traversePolicy = new ScreenFocusTraversalPolicy( components );
        budgetJustificationForm.setFocusTraversalPolicy(traversePolicy);
        budgetJustificationForm.setFocusCycleRoot(true);
        
        
        
        budgetJustificationForm.addKeyListener(new KeyAdapter(){
            public void keyReleased(KeyEvent ke){
                if(ke.getKeyCode() == KeyEvent.VK_ESCAPE){
                    validateOnClose();
                }
            }
        });
        
        budgetJustificationForm.addEscapeKeyListener(
        new AbstractAction("escPressed"){
            public void actionPerformed(ActionEvent ae){
                validateOnClose();
            }
        });
        
        budgetJustificationForm.addWindowListener( new WindowAdapter() {
            public void windowClosing(WindowEvent we ) {
                validateOnClose();
            }
        });
        
        
    }
    
    /** Saves the Form Data */
    public void saveFormData() {
        try {
            
            String proposalNumber = budgetInfoBean.getProposalNumber();
            int versionNumber = budgetInfoBean.getVersionNumber();
            
            if(checkOnlyNewLineCharacters(budgetJustificationForm.txtArBudgetJustification.getText())) {
                budgetJustificationForm.txtArBudgetJustification.setText("");
            }
                
            if(vecBudgetJustificationBean != null && vecBudgetJustificationBean.size() > 0) {
                BudgetJustificationBean budgetJustificationBean = (BudgetJustificationBean) vecBudgetJustificationBean.get(0);
                budgetJustificationBean.setJustification(budgetJustificationForm.txtArBudgetJustification.getText());
                budgetJustificationBean.setAcType(TypeConstants.UPDATE_RECORD);
                queryEngine.update(proposalNumber+versionNumber,budgetJustificationBean);
                
            } else {
                
                BudgetJustificationBean budgetJustificationBean = new BudgetJustificationBean();
                budgetJustificationBean.setProposalNumber(proposalNumber);
                budgetJustificationBean.setVersionNumber(versionNumber);
                budgetJustificationBean.setJustification(budgetJustificationForm.txtArBudgetJustification.getText());
                budgetJustificationBean.setAcType(TypeConstants.INSERT_RECORD);
                queryEngine.insert(proposalNumber+versionNumber,budgetJustificationBean);
            }
            
        } catch(Exception e) {
            e.getMessage();
        }
    }
    
    /** set the Bean data to the UI
     * @param data Object Bean
     */
    public void setFormData(Object data) {
        
        budgetInfoBean = (BudgetInfoBean) data;
        
            try {
                
                BudgetDetailBean budgetDetailBean =  new BudgetDetailBean();
                
                String proposalNumber = budgetInfoBean.getProposalNumber();
                int versionNumber = budgetInfoBean.getVersionNumber();
                queryKey = proposalNumber+versionNumber;
                budgetJustificationForm.setTitle(TITLE+"for "+proposalNumber+", Version "+versionNumber);
                
                
                Equals equalsActype = new Equals(ACTYPE,null);
                NotEquals notEqualsDelete = new NotEquals(ACTYPE,TypeConstants.DELETE_RECORD);
                acTypeNullandNotDelete = new Or(equalsActype,notEqualsDelete);
                
                vecBudgetDetailBean = queryEngine.executeQuery(queryKey,budgetDetailBean.getClass(),acTypeNullandNotDelete);
                
                BudgetJustificationBean budgetJustificationBean = new BudgetJustificationBean();
                budgetJustificationBean.setProposalNumber(proposalNumber);
                budgetJustificationBean.setVersionNumber(versionNumber);
                
                vecBudgetJustificationBean = queryEngine.executeQuery(queryKey,budgetJustificationBean.getClass(),acTypeNullandNotDelete);
                
                if(vecBudgetJustificationBean != null && vecBudgetJustificationBean.size() > 0) {
                    budgetJustificationBean = (BudgetJustificationBean) vecBudgetJustificationBean.get(0);
                    budgetJustificationForm.txtArBudgetJustification.setText(budgetJustificationBean.getJustification());
                    
                    if(budgetJustificationBean.getUpdateTimestamp() != null) {
                    String updateTimestamp = CoeusDateFormat.format(budgetJustificationBean.getUpdateTimestamp().toString()) ;
                    budgetJustificationForm.txtLastUpdate.setText(updateTimestamp);
//                    budgetJustificationForm.txtUpdateUser.setText(budgetJustificationBean.getUpdateUser());
                    /*
                     * UserID to UserName Enhancement - Start
                     * Added UserUtils class to change userid to username
                     */
                    budgetJustificationForm.txtUpdateUser.setText(UserUtils.getDisplayName(budgetJustificationBean.getUpdateUser()));
                    // UserId to UserName Enhancement - End
                    }
                }
                budgetJustificationForm.txtArBudgetJustification.requestFocus();
                budgetJustificationForm.txtArBudgetJustification.setFont(CoeusFontFactory.getNormalFont());
                budgetJustificationForm.txtArBudgetJustification.setCaretPosition(0);
                
            } catch (CoeusException coeusException ) {
                coeusException.getMessage();
            }
        
        
    }
    
    /** validate all UI actions
     * @return boolean if <true> validation true
     * @throws CoeusUIException CoeusUIException instance
     */
    public boolean validate() throws edu.mit.coeus.exception.CoeusUIException {
        
        return false;
    }
    
    /** action Performed Method
     * @param actionEvent ActionEvent Object
     */
    public void actionPerformed(java.awt.event.ActionEvent actionEvent) {
        
        Object source = actionEvent.getSource();
        
        if (source.equals(budgetJustificationForm.btnOk)){
            if(isTextAreaContentModified) {
                setSaveRequired(true);
            }
            
            if(isSaveRequired()) {
                saveFormData();
                close();
            } else
                close();
            return;
        }
        
        if (source.equals(budgetJustificationForm.btnCancel)){
            validateOnClose();
        }
        
        if (source.equals(budgetJustificationForm.btnConsolidate)){
            
            if(! consolidate()) {
                CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(NO_LINEITEM_JUSTIFICATION));
            }
            
        }
    }
    
    
    
    
    /** method to consolidate the justification for the Budget
     * @return boolean if <CODE> true <CODE> successfully consolidation
     */    
    public boolean consolidate() {
        String budgetJustificationTotal = "";
        boolean consolidated = false;
        String PERIOD ="Period ";
        boolean afterFirstTimeAdd = false;
        try {
            if(vecBudgetDetailBean != null && vecBudgetDetailBean.size() > 0) {
                Hashtable hasTab = new Hashtable();
                //coeusqa-2758 start
                String sort[] = {"budgetPeriod", "costElementDescription"};
                vecBudgetDetailBean.sort(sort, true);
                //coeusqa-2758 end
                for(int indexCount=0;indexCount<vecBudgetDetailBean.size();indexCount++) {
                    
                    BudgetDetailBean budgetDetailBean = (BudgetDetailBean) vecBudgetDetailBean.get(indexCount);
                    String budgetCategoryBeanDescription = budgetDetailBean.getCostElementDescription();
                    
                    String eachBudgetJustification = "";
                    if(budgetDetailBean.getBudgetJustification() != null && budgetCategoryBeanDescription != null
                    && budgetDetailBean.getBudgetJustification().length() > 0 && budgetCategoryBeanDescription.length() > 0) {
                        String budgetPeriod = budgetDetailBean.getBudgetPeriod()+"";
                        if(!hasTab.containsKey(budgetPeriod)) {
                            if(afterFirstTimeAdd) {
                                eachBudgetJustification = NEWLINE;
                            } else
                                afterFirstTimeAdd = true;
                            eachBudgetJustification += PERIOD+budgetPeriod+NEWLINE;
                            hasTab.put(budgetPeriod,eachBudgetJustification);
                        }
                        
                        eachBudgetJustification += budgetCategoryBeanDescription+NEWLINE;
                        eachBudgetJustification += budgetDetailBean.getBudgetJustification()+NEWLINE;
                        budgetJustificationTotal += eachBudgetJustification;
                        consolidated = true;
                    }
                    
                }
                
                if(consolidated && budgetJustificationTotal.length() > 0) {
                    String valueTxtArBudgetJustification = budgetJustificationForm.txtArBudgetJustification.getText();
                    boolean addNewLine = true;
                    if(checkOnlyNewLineCharacters(valueTxtArBudgetJustification)) {
                        addNewLine = false;
                    }
                    
                    if(valueTxtArBudgetJustification != null && valueTxtArBudgetJustification.length() > 0 && addNewLine ) {
                        valueTxtArBudgetJustification += NEWLINE;
                        valueTxtArBudgetJustification +=  LINE_SEPERATOR+NEWLINE+NEWLINE;
                    }
                    else
                        valueTxtArBudgetJustification ="";
                    
                    budgetJustificationForm.txtArBudgetJustification.setFont(CoeusFontFactory.getNormalFont());
                    budgetJustificationForm.txtArBudgetJustification.setText(valueTxtArBudgetJustification+budgetJustificationTotal) ;
                    setSaveRequired(true);
                } else
                    consolidated = false;
                
                
            }
            
            
        } catch (Exception exp ) {
            exp.getMessage();
        }
        
        return consolidated ;
    }
    
    /** checks whether a given string contains only new line charaters '\n'
     * @param textString String to check for new line characters
     * @return boolean if <CODE> true <CODE > String only contains new line characters
     */    
    public boolean checkOnlyNewLineCharacters(String textString) {
        
        boolean isOnlyNewLineCharacters = false;
        int lastIndex = textString.lastIndexOf("\n");
        int firstIndex = textString.indexOf("\n");
        int length = textString.length();
        if(length == (lastIndex - firstIndex)+1) {
            isOnlyNewLineCharacters = true;
        }
        
            return isOnlyNewLineCharacters;
    }
    
    /** validates on close witha Confirm window */    
    public void validateOnClose() {
        if(isTextAreaContentModified) {
            setSaveRequired(true);
        }
        
        if(isSaveRequired()) {
            
            int option = JOptionPane.NO_OPTION;
            option  = CoeusOptionPane.showQuestionDialog(coeusMessageResources.parseMessageKey(SAVE_CHANGES),
            CoeusOptionPane.OPTION_YES_NO_CANCEL,
            CoeusOptionPane.DEFAULT_YES);
            
            switch(option) {
                case ( JOptionPane.YES_OPTION ):
                    saveFormData();
                    close();
                    break;
                    
                case ( JOptionPane.NO_OPTION ):
                    close();
                    break;
                case ( JOptionPane.CANCEL_OPTION):
                    break;
            }
            
            
        } else
            close();
    }
    
    
    
    /** TextArea Document Listener */    
    class TextAreaDocumentListener implements DocumentListener {
        
        /** variable to hold length of String */        
        int changeLength = 0;
        /** indicates initial text is retrieved */        
        boolean obtainedInitialTextlength = false;
        /** length of text in the document */        
        int textLength = 0;
        
        /** insertUpdate method
         * @param e DocuemntEvent instance
         */        
        public void insertUpdate(DocumentEvent e) {
            
            if(obtainedInitialTextlength) {
                if(e.getLength() > 0) {
                    isTextAreaContentModified = true;
                    changeLength += e.getLength();
                }
            }
            
            if(obtainedInitialTextlength == false) {
                obtainedInitialTextlength = true;
                textLength = e.getLength();
            }
            
            
        }
        
        /** removeUpdate method
         * @param e DocuemntEvent instance
         */        
        public void removeUpdate(DocumentEvent e) {
            
            changeLength -= e.getLength();
            if(changeLength == 0) {
                isTextAreaContentModified = false;
            } else
                isTextAreaContentModified = true;
            
        }
        
        /** cheangeUpdate method
         * @param e DocuemntEvent instance
         */        
        public void changedUpdate(DocumentEvent e) {
            //Plain text components don't fire these events
        }
        
        
        
        
    }
    
    
}
