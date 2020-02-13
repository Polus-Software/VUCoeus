/*
 * ProjectDescriptionForm.java
 *
 * Created on July 1, 2005, 3:22 PM
 */

package edu.mit.coeus.budget.gui;

import edu.mit.coeus.exception.CoeusUIException;
import edu.mit.coeus.gui.CoeusDlgWindow;
import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.utils.LimitedPlainDocument;
import edu.mit.coeus.utils.TypeConstants;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.AbstractAction;
import javax.swing.JOptionPane;

/**
 *
 * @author  tarique
 */
public class ProjectDescriptionForm extends javax.swing.JPanel {
    
    /** Creates new form ProjectDescriptionForm */
    CoeusDlgWindow dlgWindow;
    public boolean modifiedDescription=false;
    private static final String DESCRIPTION_VALIDATE="budget_project_income_exceptionCode.1169";
    private static final String SAVE_CHANGES="budget_project_income_exceptionCode.1171";
    private CoeusMessageResources coeusMessageResources;
    private char functionType;
    private String txtDesc;
    public ProjectDescriptionForm() {
        coeusMessageResources = CoeusMessageResources.getInstance();
        initComponents();
        dlgWindow = new CoeusDlgWindow(CoeusGuiConstants.getMDIForm(), "Project Income Description", true);
        dlgWindow.getContentPane().add(this);
        dlgWindow.pack();
        dlgWindow.setResizable(false);
        dlgWindow.setLocation(CoeusDlgWindow.CENTER);
        registerComponents();
    }
    
    public ProjectDescriptionForm(char functionType){
        this();
        this.functionType = functionType;
        
    }
    /**
     *Method to register listner for window closing and escape key pressed
     **/
    private void registerComponents(){
        dlgWindow.addComponentListener(new ComponentAdapter(){
             public void componentShown(ComponentEvent e){
                 if(functionType!=TypeConstants.DISPLAY_MODE){ 
                 btnOk.requestFocusInWindow();
                 }else{
                     btnCancel.requestFocusInWindow();
                 }
             }
         });
         dlgWindow.addWindowListener(new WindowAdapter(){
             public void windowClosing(WindowEvent we){
                 if(functionType!=TypeConstants.DISPLAY_MODE){  
                    if(txtDesc!=null&&!txtDesc.equals(txtArDescription.getText().trim())){
                        performCancelAction();
                    }else{
                        dlgWindow.dispose();
                    }
                 }else{
                     dlgWindow.dispose();
                 }
             }
        });
        dlgWindow.addEscapeKeyListener(new AbstractAction("escPressed"){
            public void actionPerformed(ActionEvent ae){
                if(functionType!=TypeConstants.DISPLAY_MODE){    
                 if(txtDesc!=null&&!txtDesc.equals(txtArDescription.getText().trim())){
                        performCancelAction();
                    }else{
                        dlgWindow.dispose();
                    }
                }else{
                    dlgWindow.dispose();
                }
            }
        });
    }
    /*
     *Method to validate the description Text area
     */
    private boolean validateDescription() throws CoeusUIException {
        String projectDescription=txtArDescription.getText().trim();
        if(projectDescription.equals("")||projectDescription==null){
            txtArDescription.setText("");
            txtArDescription.requestFocusInWindow();
            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(DESCRIPTION_VALIDATE));
            return false;
        }
        return true;
    }
    /**
     * Method to check any modification in description
     **/
    private void performCancelAction(){
            int option = CoeusOptionPane.showQuestionDialog(
            coeusMessageResources.parseMessageKey(SAVE_CHANGES),
            CoeusOptionPane.OPTION_YES_NO_CANCEL,
            2);
            switch( option ) {
                case (JOptionPane.YES_OPTION ):
                    try{
                        if( validateDescription()){
                            modifiedDescription=true;
                            dlgWindow.dispose();
                            }
                    }catch (Exception exception){
                        exception.printStackTrace();
                    }
                    break;
                case(JOptionPane.NO_OPTION ):
                      modifiedDescription=false;
                      dlgWindow.dispose();
                   break;
                default:
                    break;
            }
    }
    /**
     * Method used to show the dialog window.
     */
    public void display(){
        dlgWindow.setVisible(true);
    }
    /**
     * Method used to set the data to be shown.
     * @param data new data to be shown in the dialog.
     */
    public void setData(java.lang.String data) {
        txtDesc = data.trim();
        txtArDescription.setText(data.trim());
        txtArDescription.setCaretPosition(0);
    }
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        scrPnDescription = new javax.swing.JScrollPane();
        txtArDescription = new javax.swing.JTextArea();
        btnOk = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();

        setLayout(new java.awt.GridBagLayout());

        scrPnDescription.setMinimumSize(new java.awt.Dimension(300, 200));
        scrPnDescription.setPreferredSize(new java.awt.Dimension(300, 200));
        txtArDescription.setDocument(new LimitedPlainDocument(2000));
        txtArDescription.setFont(CoeusFontFactory.getNormalFont());
        txtArDescription.setLineWrap(true);
        txtArDescription.setWrapStyleWord(true);
        scrPnDescription.setViewportView(txtArDescription);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridheight = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 0);
        add(scrPnDescription, gridBagConstraints);

        btnOk.setFont(CoeusFontFactory.getLabelFont());
        btnOk.setMnemonic('O');
        btnOk.setText("OK");
        btnOk.setMaximumSize(new java.awt.Dimension(67, 25));
        btnOk.setMinimumSize(new java.awt.Dimension(67, 25));
        btnOk.setPreferredSize(new java.awt.Dimension(67, 25));
        btnOk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOkActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 11, 0, 4);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        add(btnOk, gridBagConstraints);

        btnCancel.setFont(CoeusFontFactory.getLabelFont());
        btnCancel.setMnemonic('C');
        btnCancel.setText("Cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 11, 0, 4);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        add(btnCancel, gridBagConstraints);

    }//GEN-END:initComponents

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        if(functionType!=TypeConstants.DISPLAY_MODE){  
                   if(txtDesc!=null&&!txtDesc.equals(txtArDescription.getText().trim())){
                        performCancelAction();
                    }else{
                        dlgWindow.dispose();
                    }
                 }else{
                     dlgWindow.dispose();
                 }
    }//GEN-LAST:event_btnCancelActionPerformed

    private void btnOkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOkActionPerformed
        try{
            if(functionType!=TypeConstants.DISPLAY_MODE){
            if(validateDescription()){
                modifiedDescription=true;
                dlgWindow.dispose();
            }
            }else{
                dlgWindow.dispose();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnOkActionPerformed
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JButton btnCancel;
    public javax.swing.JButton btnOk;
    public javax.swing.JScrollPane scrPnDescription;
    public javax.swing.JTextArea txtArDescription;
    // End of variables declaration//GEN-END:variables
    
}
