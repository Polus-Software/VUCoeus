/*
 * AmountReleasedCommentsController.java
 *
 * Created on July 10, 2007, 9:26 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package edu.mit.coeus.subcontract.controller;

import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.exception.CoeusUIException;
import edu.mit.coeus.gui.CoeusAppletMDIForm;
import edu.mit.coeus.gui.CoeusDlgWindow;
import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.subcontract.bean.SubContractAmountReleased;
import edu.mit.coeus.subcontract.gui.AmountReleasedCommentsForm;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.utils.ScreenFocusTraversalPolicy;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.AbstractAction;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

/**
 *
 * @author noorula
 */
public class AmountReleasedCommentsController extends SubcontractController implements ActionListener {
    
    private AmountReleasedCommentsForm amountReleasedCommentsForm;
    private CoeusAppletMDIForm mdiForm = CoeusGuiConstants.getMDIForm();
    private CoeusDlgWindow dlgAmountReleasedChange;
    private static final int WIDTH = 490;
    private static final int HEIGHT = 245;
    private boolean commentsMandatory;
    private boolean okClicked;
    private static final String SAVE_CHANGES = "saveConfirmCode.1002";
    private CoeusMessageResources coeusMesssageResources; 
    private String title; 
    private String invoiceAction;
    /** Creates a new instance of AmountReleasedCommentsController */
    public AmountReleasedCommentsController(String title,String invoiceAction) {
        coeusMesssageResources = CoeusMessageResources.getInstance();
        this.title = title;
        this.invoiceAction = invoiceAction;
        amountReleasedCommentsForm = new AmountReleasedCommentsForm();
        registerComponents();
        postInitComponents();
    }

    /*Instantiates instance objects*/
    private void postInitComponents(){
        
        dlgAmountReleasedChange = new CoeusDlgWindow(mdiForm, title);
        dlgAmountReleasedChange.setResizable(false);
        dlgAmountReleasedChange.setModal(true);
        
        dlgAmountReleasedChange.getContentPane().add(amountReleasedCommentsForm);
        dlgAmountReleasedChange.setFont(CoeusFontFactory.getLabelFont());
        dlgAmountReleasedChange.setDefaultCloseOperation(CoeusDlgWindow.DO_NOTHING_ON_CLOSE);
        dlgAmountReleasedChange.setSize(WIDTH, HEIGHT);
        
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension dlgSize = dlgAmountReleasedChange.getSize();
        dlgAmountReleasedChange.setLocation(screenSize.width/2 - (dlgSize.width/2),
        screenSize.height/2 - (dlgSize.height/2));
        
        dlgAmountReleasedChange.addEscapeKeyListener(
        new AbstractAction("escPressed"){
            public void actionPerformed(ActionEvent ae){
                close();
                return;
            }
        });
        
        dlgAmountReleasedChange.setDefaultCloseOperation(CoeusDlgWindow.DO_NOTHING_ON_CLOSE);
        dlgAmountReleasedChange.addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent we){
                close();
            }
        });
        
        dlgAmountReleasedChange.addComponentListener(
        new ComponentAdapter(){
            public void componentShown(ComponentEvent e){
                setDefaultFocusInWindow();
            }
        });        
    }
    /**
     * Method to register components
     */
    public void registerComponents() {
        java.awt.Component[] components = {amountReleasedCommentsForm.txtArComments, 
                 amountReleasedCommentsForm.btnOk, amountReleasedCommentsForm.btnCancel};        
        ScreenFocusTraversalPolicy  traversePolicy = new ScreenFocusTraversalPolicy( components );
        amountReleasedCommentsForm.setFocusTraversalPolicy(traversePolicy);
        amountReleasedCommentsForm.setFocusCycleRoot(true);
        amountReleasedCommentsForm.btnOk.addActionListener(this);
        amountReleasedCommentsForm.btnCancel.addActionListener(this);
        amountReleasedCommentsForm.txtArComments.setFont(new CoeusFontFactory().getNormalFont());
    }    
    
    public void cleanUp() {
    }

    public Component getControlledUI() {
        return amountReleasedCommentsForm;
    }

    public void setFormData(Object data) throws CoeusException {
    }

    /**
     * To get the entered comments
     * @return Object Commentes entered
     */
    public Object getFormData() {
        return amountReleasedCommentsForm.txtArComments.getText().trim();
    }

    public void formatFields() {
    }

    public boolean validate() throws CoeusUIException {
        return true;
    }

    public void saveFormData() throws CoeusException {
    }

    public void display() {
        dlgAmountReleasedChange.setVisible(true);
    }

    public void actionPerformed(ActionEvent actionEvent) {
        Object source = actionEvent.getSource();
        if(source.equals(amountReleasedCommentsForm.btnCancel)){
            close();
        } else if(source.equals(amountReleasedCommentsForm.btnOk)){
            if(saveData()){
                dlgAmountReleasedChange.dispose();
            }
        }
    }
    
    /**
     * To set the required values to display in the form
     * @param subContractAmountReleased bean
     */
    public void showChangeReleased(SubContractAmountReleased subContractAmountReleased) {
        if(subContractAmountReleased != null){
            amountReleasedCommentsForm.txtSubcontractNumber.setText(
                    subContractAmountReleased.getSubContractCode());
            amountReleasedCommentsForm.txtInvoiceNumber.setText(
                    subContractAmountReleased.getInvoiceNumber());            
        }
        if(invoiceAction!=null && invoiceAction.equals("R")){
            amountReleasedCommentsForm.btnOk.setText("Reject");
        }else{
            amountReleasedCommentsForm.btnOk.setText("Approve");
        }
        dlgAmountReleasedChange.show();
    }
    
    /**
     * Validation check for mandatory comments needed will takes place.
     */
    private boolean saveData(){
        if(isCommentsMandatory()
            && amountReleasedCommentsForm.txtArComments.getText().trim().equals(EMPTY_STRING)){
            CoeusOptionPane.showInfoDialog(coeusMesssageResources.parseMessageKey("subcontractAmountRelease_exceptionCode.1213"));
            setRequestFocusInThread(amountReleasedCommentsForm.txtArComments);
            return false;
        }
        setOkClicked(true);
        return true;
    }
    
    /**
     * To close the dialogue window, before closing save confirmation 
     * validation check will takes place.
     */
    private void close(){
        if(amountReleasedCommentsForm.txtArComments.getText().trim().equals(EMPTY_STRING)){
            dlgAmountReleasedChange.dispose();
            return;
        } else {
            int option = CoeusOptionPane.showQuestionDialog(
                    coeusMesssageResources.parseMessageKey(SAVE_CHANGES),
                    CoeusOptionPane.OPTION_YES_NO_CANCEL,
                    JOptionPane.YES_OPTION);
            switch( option ) {
                case (JOptionPane.YES_OPTION ):
                    if(saveData()){
                        dlgAmountReleasedChange.dispose();
                    }
                    break;
                case(JOptionPane.NO_OPTION ):
                    dlgAmountReleasedChange.dispose();
                    break;
                default:
                    break;
            }
        }
    }
    
    /** Supporting method which will be used for the focus lost for date
     *fields. This will be fired when the request focus for the specified
     *date field is invoked
     */
    private void setRequestFocusInThread(final Component component) {
        SwingUtilities.invokeLater( new Runnable() {
            public void run() {
                component.requestFocusInWindow();
            }
        });
    }    

    /** Getter for property commentsMandatory.
     * @return Value of property commentsMandatory.
     */    
    public boolean isCommentsMandatory() {
        return commentsMandatory;
    }

    /** Setter for property commentsMandatory.
     * @param commentsMandatory New value of property commentsMandatory.
     */
    public void setCommentsMandatory(boolean commentsMandatory) {
        this.commentsMandatory = commentsMandatory;
    }

    /** Getter for property okClicked.
     * @return Value of property okClicked.
     */
    public boolean isOkClicked() {
        return okClicked;
    }

    /** Setter for property okClicked.
     * @param okClicked New value of property okClicked.
     */    
    public void setOkClicked(boolean okClicked) {
        this.okClicked = okClicked;
    }
    /**
     * Set the focus to the text area while opening the form
     */
    private void setDefaultFocusInWindow(){
        amountReleasedCommentsForm.txtArComments.requestFocusInWindow();
    }
}
