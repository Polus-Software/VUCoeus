/*
 * @(#)ResetPasswordForm.java 1.0 September 7, 2007, 11:09 AM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

/*
 * ResetPasswordForm.java
 *
 * Created on September 7, 2007, 11:09 AM
 */

package edu.mit.coeus.user.gui;

import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.gui.CoeusAppletMDIForm;
import edu.mit.coeus.gui.CoeusDlgWindow;
import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.utils.*;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Vector;
import javax.swing.AbstractAction;

/**
 *
 * @author  nandkumarsn
 */
public class ResetPasswordForm extends javax.swing.JPanel implements ActionListener{
        
    private static final int WIDTH = 390;
    private static final int HEIGHT = 145;
    private static final String UTILITY_SERVLET = "/UtilityServlet";    
    private CoeusDlgWindow dlgResetPassword;    
    private CoeusAppletMDIForm mdiForm = CoeusGuiConstants.getMDIForm();    
    private CoeusMessageResources coeusMessageResources;    
    private char[] newPassword;
    private char[] confirmPassword;    
    private String serverMsg = "";
    private String userId;
    private String userName;
    private static final char RESET_PASSWORD = 'E';
    
    /** Creates new form ChangePassword */
    public ResetPasswordForm() {
        initComponents();
        java.awt.Component[] components = {txtPwd, txtRePwd, btnOk, btnCancel};
        ScreenFocusTraversalPolicy traversalPolicy = new ScreenFocusTraversalPolicy(components);
        setFocusTraversalPolicy(traversalPolicy);
        setFocusCycleRoot(true);
        postInitComponent();
    }
    
    private void clearFields() {
        txtPwd.setText("");
        txtRePwd.setText("");        
    }    
    
    private void postInitComponent() {        
        coeusMessageResources = CoeusMessageResources.getInstance();
        txtPwd.setDocument(new LimitedPlainDocument(30));
        txtRePwd.setDocument(new LimitedPlainDocument(30));        
        dlgResetPassword = new CoeusDlgWindow(CoeusGuiConstants.getMDIForm(), true);
        dlgResetPassword.getContentPane().add(this);
        dlgResetPassword.setResizable(false);
        dlgResetPassword.setFont(CoeusFontFactory.getLabelFont());
        dlgResetPassword.setSize(WIDTH, HEIGHT);
        Dimension dlgLocation = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension dlgPasswordSize = dlgResetPassword.getSize();
        dlgResetPassword.setLocation((dlgLocation.width/2 - dlgPasswordSize.width/2), (dlgLocation.height/2 - dlgPasswordSize.height/2));
        dlgResetPassword.addComponentListener(
         new ComponentAdapter(){
             public void componentShown(ComponentEvent e){
                 setWindowFocus(txtPwd);
             }
         });
         
         dlgResetPassword.addEscapeKeyListener(new AbstractAction("escPressed"){
             public void actionPerformed(ActionEvent actionEvent){
                if(saveConfirmation()){
                    performCloseOperation();
                }else{
                    dlgResetPassword.dispose();
                }                                  
             }
         });     
         dlgResetPassword.setDefaultCloseOperation(CoeusDlgWindow.DO_NOTHING_ON_CLOSE);
         dlgResetPassword.addWindowListener(new WindowAdapter(){
             public void windowClosing(WindowEvent we){
                 if(saveConfirmation()){
                     performCloseOperation();
                 }else{
                     dlgResetPassword.dispose();
                 }
             }
         });        
         btnOk.addActionListener(this);
         btnCancel.addActionListener(this);
    }  
        
    private boolean saveConfirmation(){
        boolean saveConfirmFlag = false;
        newPassword = txtPwd.getPassword();
        confirmPassword = txtRePwd.getPassword();
        int newPswLen = newPassword.length;
        int confirmPswLen = confirmPassword.length;  
        if(newPswLen > 0 || confirmPswLen > 0){
            saveConfirmFlag = true;
        }else{
            saveConfirmFlag = false;
        }   
        return saveConfirmFlag;
    }
    
    private void setWindowFocus(final java.awt.Component component){
        javax.swing.SwingUtilities.invokeLater(new Runnable(){
            public void run(){
                component.requestFocusInWindow();
            }
        });
    }
    
    public void display(){
        clearFields();
        dlgResetPassword.show();
    }
    
    public void actionPerformed(ActionEvent actionEvent) {
        Object actionSource = actionEvent.getSource();
        try{
            if(actionSource.equals(btnOk)){
                validatePassword();
            }
            if(actionSource.equals(btnCancel)){
                newPassword = txtPwd.getPassword();
                confirmPassword = txtRePwd.getPassword(); 
                int newPswLen = newPassword.length;
                int confirmPswLen = confirmPassword.length;            
                if(newPswLen > 0 || confirmPswLen > 0){
                    performCloseOperation();
                }else{
                    dlgResetPassword.dispose();
                }
            }
        }catch(CoeusException ex){
            ex.printStackTrace();
        }
    }
    
    private void validatePassword() throws CoeusException{
        
        newPassword = txtPwd.getPassword();
        confirmPassword = txtRePwd.getPassword();
        String strNewPsw = new String(txtPwd.getPassword());
        String strConfirmPsw = new String(txtRePwd.getPassword());
        int newPswLen = newPassword.length;
        int confirmPswLen = confirmPassword.length;
        if(newPswLen < 1){
           CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey("changePassword_exceptionCode.1002"));
           setWindowFocus(txtPwd);
           return; 
        }
        if(confirmPswLen < 1){
            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey("changePassword_exceptionCode.1003"));
            setWindowFocus(txtRePwd);
            return; 
        }
        if(newPswLen != confirmPswLen ){
            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey("changePassword_exceptionCode.1004"));
            setWindowFocus(txtPwd);
            return;
        }
        if(!strNewPsw.equals(strConfirmPsw)){
            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey("changePassword_exceptionCode.1004"));
            setWindowFocus(txtPwd);
            return;
        }
        if(Character.isDigit(newPassword[0])){
            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey("changePassword_exceptionCode.1005"));
            setWindowFocus(txtPwd);
            return;
        }
        for(int index=0; index < newPswLen; index++){
            if(!Character.isLetterOrDigit(newPassword[index])){
                CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey("changePassword_exceptionCode.1006"));
                txtPwd.requestFocusInWindow();
                return;
            }
        }
        if(!updatePassword()){
            CoeusOptionPane.showInfoDialog(serverMsg);
            setWindowFocus(txtPwd);
            return;
        }else {
            CoeusOptionPane.showInfoDialog(serverMsg);
            dlgResetPassword.dispose();
            return;
        }
    }
    
    private boolean updatePassword(){
        RequesterBean requesterBean = new RequesterBean();
        requesterBean.setId(mdiForm.getUserId());
        requesterBean.setFunctionType(RESET_PASSWORD);
        Vector dataObject = new Vector();        
        dataObject.add(new String(newPassword));        
        dataObject.add(getUserId());                
        requesterBean.setDataObject(dataObject);
        AppletServletCommunicator appletServletCommunicator = new AppletServletCommunicator(CoeusGuiConstants.CONNECTION_URL+UTILITY_SERVLET, requesterBean);
        appletServletCommunicator.setRequest(requesterBean);
        appletServletCommunicator.send();
        ResponderBean responderBean = appletServletCommunicator.getResponse();
        if(responderBean != null){
            if(responderBean.isSuccessfulResponse()){
                serverMsg = responderBean.getMessage();
                return true;
            }else {
                serverMsg = responderBean.getMessage();
                return false;
            }
        }
        return false;
    }  

    public String getUserId(){
        return userId;
    }

    public void setUserId(String userId){
        this.userId = userId;
    }

    public String getUserName(){
        return userName;
    }

    public void setUserName(String userName){
        this.userName = userName;
    }
    
    public void setMessages(){        
        lblMsg.setText(coeusMessageResources.parseMessageKey("resetPasswordHelpCode.1000"));
        String msgTitle = coeusMessageResources.parseMessageKey("resetPasswordHelpCode.1001") +" " +getUserName();
        dlgResetPassword.setTitle(msgTitle);
    }
    
    public void performCloseOperation(){   
        int option = CoeusOptionPane.showQuestionDialog(
                coeusMessageResources.parseMessageKey("saveConfirmCode.1002"),
                CoeusOptionPane.OPTION_YES_NO_CANCEL,2);
        switch(option){
            case (CoeusOptionPane.SELECTION_YES):
                dlgResetPassword.setCursor(new java.awt.Cursor(java.awt.Cursor.WAIT_CURSOR));
                try{
                    validatePassword();
                }catch(CoeusException ex){
                    ex.printStackTrace();
                }
                dlgResetPassword.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
                break;
            case(CoeusOptionPane.SELECTION_NO):
                dlgResetPassword.dispose();
                break;
            default:
                break;
        }        
    } 
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        lblMsg = new javax.swing.JLabel();
        pnlButtons = new javax.swing.JPanel();
        btnOk = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();
        lblPwd = new javax.swing.JLabel();
        lblRePwd = new javax.swing.JLabel();
        txtPwd = new javax.swing.JPasswordField();
        txtRePwd = new javax.swing.JPasswordField();

        setLayout(new java.awt.GridBagLayout());

        lblMsg.setForeground(java.awt.Color.blue);
        lblMsg.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblMsg.setMaximumSize(new java.awt.Dimension(140, 40));
        lblMsg.setMinimumSize(new java.awt.Dimension(140, 40));
        lblMsg.setPreferredSize(new java.awt.Dimension(140, 40));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        add(lblMsg, gridBagConstraints);

        pnlButtons.setLayout(new java.awt.GridBagLayout());

        btnOk.setFont(CoeusFontFactory.getLabelFont());
        btnOk.setMnemonic('O');
        btnOk.setText("OK");
        btnOk.setMaximumSize(new java.awt.Dimension(73, 23));
        btnOk.setMinimumSize(new java.awt.Dimension(73, 23));
        btnOk.setPreferredSize(new java.awt.Dimension(73, 23));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 3, 0);
        pnlButtons.add(btnOk, gridBagConstraints);

        btnCancel.setFont(CoeusFontFactory.getLabelFont());
        btnCancel.setMnemonic('C');
        btnCancel.setText("Cancel");
        btnCancel.setMaximumSize(new java.awt.Dimension(73, 23));
        btnCancel.setMinimumSize(new java.awt.Dimension(73, 23));
        btnCancel.setPreferredSize(new java.awt.Dimension(73, 23));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 0, 0);
        pnlButtons.add(btnCancel, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        add(pnlButtons, gridBagConstraints);

        lblPwd.setFont(CoeusFontFactory.getLabelFont());
        lblPwd.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblPwd.setText("Enter New Password:");
        lblPwd.setMaximumSize(new java.awt.Dimension(140, 20));
        lblPwd.setMinimumSize(new java.awt.Dimension(140, 20));
        lblPwd.setPreferredSize(new java.awt.Dimension(140, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 3, 0);
        add(lblPwd, gridBagConstraints);

        lblRePwd.setFont(CoeusFontFactory.getLabelFont());
        lblRePwd.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblRePwd.setText("Re-enter New Password:");
        lblRePwd.setMaximumSize(new java.awt.Dimension(140, 20));
        lblRePwd.setMinimumSize(new java.awt.Dimension(140, 20));
        lblRePwd.setPreferredSize(new java.awt.Dimension(140, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 0, 0);
        add(lblRePwd, gridBagConstraints);

        txtPwd.setFont(CoeusFontFactory.getNormalFont());
        txtPwd.setMinimumSize(new java.awt.Dimension(150, 22));
        txtPwd.setPreferredSize(new java.awt.Dimension(150, 22));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 3, 0);
        add(txtPwd, gridBagConstraints);

        txtRePwd.setFont(CoeusFontFactory.getNormalFont());
        txtRePwd.setMinimumSize(new java.awt.Dimension(150, 22));
        txtRePwd.setPreferredSize(new java.awt.Dimension(150, 22));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 0, 0);
        add(txtRePwd, gridBagConstraints);

    }// </editor-fold>//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnOk;
    private javax.swing.JLabel lblMsg;
    private javax.swing.JLabel lblPwd;
    private javax.swing.JLabel lblRePwd;
    private javax.swing.JPanel pnlButtons;
    private javax.swing.JPasswordField txtPwd;
    private javax.swing.JPasswordField txtRePwd;
    // End of variables declaration//GEN-END:variables
    
}
