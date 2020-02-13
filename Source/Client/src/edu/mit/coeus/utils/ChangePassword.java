/*
 * ChangePassword.java
 *
 * Created on November 4, 2004, 9:22 AM
 */

package edu.mit.coeus.utils;

/**
 *
 * @author  nadhgj
 */
import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.gui.CoeusDlgWindow;
import edu.mit.coeus.gui.CoeusAppletMDIForm;
import edu.mit.coeus.brokers.*;
import edu.mit.coeus.gui.CoeusMessageResources;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentEvent;
import javax.swing.AbstractAction;
import java.util.Vector;

public class ChangePassword extends javax.swing.JComponent {
    
    private static final int WIDTH = 390;
    private static final int HEIGHT = 145;
    private static final String GET_SERVLET = "/UtilityServlet";
    private static final String connect = CoeusGuiConstants.CONNECTION_URL + GET_SERVLET;
    private CoeusDlgWindow dlgChangePassWord;
    private CoeusAppletMDIForm mdiForm = CoeusGuiConstants.getMDIForm();
    private char[] currentPassword;
    private char[] newPassword;
    private char[] confirmPassword;
    private String serverMsg = "";
    private CoeusMessageResources coeusMessageResources;
    /** Creates new form ChangePassword */
    public ChangePassword() {
        initComponents();
        java.awt.Component[] components = {txtCurrentPassword,txtNewPassword,txtConfirmPassword,btnOk,btnCancel};
        ScreenFocusTraversalPolicy traversalPolicy = new ScreenFocusTraversalPolicy(components);
        setFocusTraversalPolicy(traversalPolicy);
        setFocusCycleRoot(true);
        postInitComponent();
    }
    
    private void clearFields() {
        txtConfirmPassword.setText("");
        txtCurrentPassword.setText("");
        txtNewPassword.setText("");
    }
    private void postInitComponent() {
        coeusMessageResources = CoeusMessageResources.getInstance();
        txtNewPassword.setDocument(new LimitedPlainDocument(30));
        txtCurrentPassword.setDocument(new LimitedPlainDocument(30));
        txtConfirmPassword.setDocument(new LimitedPlainDocument(30));
        dlgChangePassWord = new CoeusDlgWindow(CoeusGuiConstants.getMDIForm(),true);
        dlgChangePassWord.getContentPane().add(this);
        dlgChangePassWord.setResizable(false);
        dlgChangePassWord.setFont(CoeusFontFactory.getLabelFont());
        dlgChangePassWord.setSize(WIDTH, HEIGHT);
        Dimension dlgLocation = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension dlgPassWordSize = dlgChangePassWord.getSize();
        dlgChangePassWord.setLocation((dlgLocation.width/2 - dlgPassWordSize.width/2), (dlgLocation.height/2 - dlgPassWordSize.height/2));
        dlgChangePassWord.setTitle("Change Password");
        
        dlgChangePassWord.addComponentListener(
         new ComponentAdapter(){
             public void componentShown(ComponentEvent e){
                 setWindowFocus(txtCurrentPassword);
             }
         });
         dlgChangePassWord.addEscapeKeyListener(new AbstractAction("escPressed") {
             public void actionPerformed(ActionEvent actionEvent) {
                 dlgChangePassWord.dispose();
             }
         });
         
         
    }
    
      private void setWindowFocus(final java.awt.Component component){
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                component.requestFocusInWindow();
            }
        });
    }
      
    
    public void display() {
        clearFields();
        dlgChangePassWord.show();
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        lblCurrentPassword = new javax.swing.JLabel();
        lblNewPassword = new javax.swing.JLabel();
        lblConfirmPassword = new javax.swing.JLabel();
        txtCurrentPassword = new javax.swing.JPasswordField();
        txtNewPassword = new javax.swing.JPasswordField();
        txtConfirmPassword = new javax.swing.JPasswordField();
        jPanel1 = new javax.swing.JPanel();
        btnOk = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();

        setLayout(new java.awt.GridBagLayout());

        setMinimumSize(new java.awt.Dimension(383, 108));
        lblCurrentPassword.setFont(CoeusFontFactory.getLabelFont());
        lblCurrentPassword.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblCurrentPassword.setText("Enter current password:");
        lblCurrentPassword.setMaximumSize(new java.awt.Dimension(145, 16));
        lblCurrentPassword.setMinimumSize(new java.awt.Dimension(145, 16));
        lblCurrentPassword.setPreferredSize(new java.awt.Dimension(145, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 12, 14, 0);
        add(lblCurrentPassword, gridBagConstraints);

        lblNewPassword.setFont(CoeusFontFactory.getLabelFont());
        lblNewPassword.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblNewPassword.setText("Enter new password:");
        lblNewPassword.setMaximumSize(new java.awt.Dimension(145, 16));
        lblNewPassword.setMinimumSize(new java.awt.Dimension(145, 16));
        lblNewPassword.setPreferredSize(new java.awt.Dimension(145, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 12, 14, 0);
        add(lblNewPassword, gridBagConstraints);

        lblConfirmPassword.setFont(CoeusFontFactory.getLabelFont());
        lblConfirmPassword.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblConfirmPassword.setText("Re-enter new password:");
        lblConfirmPassword.setMaximumSize(new java.awt.Dimension(145, 16));
        lblConfirmPassword.setMinimumSize(new java.awt.Dimension(145, 16));
        lblConfirmPassword.setPreferredSize(new java.awt.Dimension(145, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 12, 6, 0);
        add(lblConfirmPassword, gridBagConstraints);

        txtCurrentPassword.setFont(CoeusFontFactory.getNormalFont());
        txtCurrentPassword.setMaximumSize(new java.awt.Dimension(150, 22));
        txtCurrentPassword.setMinimumSize(new java.awt.Dimension(150, 22));
        txtCurrentPassword.setPreferredSize(new java.awt.Dimension(150, 22));
        txtCurrentPassword.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtCurrentPasswordKeyPressed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(6, 3, 14, 0);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        add(txtCurrentPassword, gridBagConstraints);

        txtNewPassword.setFont(CoeusFontFactory.getNormalFont());
        txtNewPassword.setMaximumSize(new java.awt.Dimension(150, 22));
        txtNewPassword.setMinimumSize(new java.awt.Dimension(150, 22));
        txtNewPassword.setPreferredSize(new java.awt.Dimension(150, 22));
        txtNewPassword.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtNewPasswordKeyPressed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 14, 0);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        add(txtNewPassword, gridBagConstraints);

        txtConfirmPassword.setFont(CoeusFontFactory.getNormalFont());
        txtConfirmPassword.setMaximumSize(new java.awt.Dimension(150, 22));
        txtConfirmPassword.setMinimumSize(new java.awt.Dimension(150, 22));
        txtConfirmPassword.setPreferredSize(new java.awt.Dimension(150, 22));
        txtConfirmPassword.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtConfirmPasswordKeyPressed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 6, 0);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weighty = 1.0;
        add(txtConfirmPassword, gridBagConstraints);

        jPanel1.setMinimumSize(new java.awt.Dimension(73, 60));
        jPanel1.setPreferredSize(new java.awt.Dimension(73, 60));
        btnOk.setFont(CoeusFontFactory.getLabelFont());
        btnOk.setMnemonic('O');
        btnOk.setLabel("OK");
        btnOk.setMaximumSize(new java.awt.Dimension(73, 23));
        btnOk.setMinimumSize(new java.awt.Dimension(73, 23));
        btnOk.setPreferredSize(new java.awt.Dimension(73, 23));
        btnOk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOkActionPerformed(evt);
            }
        });

        jPanel1.add(btnOk);

        btnCancel.setFont(CoeusFontFactory.getLabelFont());
        btnCancel.setMnemonic('C');
        btnCancel.setLabel("Cancel");
        btnCancel.setMaximumSize(new java.awt.Dimension(73, 23));
        btnCancel.setMinimumSize(new java.awt.Dimension(73, 23));
        btnCancel.setPreferredSize(new java.awt.Dimension(73, 23));
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });

        jPanel1.add(btnCancel);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 10);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        add(jPanel1, gridBagConstraints);

    }//GEN-END:initComponents

    private void txtConfirmPasswordKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtConfirmPasswordKeyPressed
        // Add your handling code here:
        if(evt.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER) {
            validatePassword();
        }
    }//GEN-LAST:event_txtConfirmPasswordKeyPressed

    private void txtNewPasswordKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNewPasswordKeyPressed
        // Add your handling code here:
        if(evt.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER) {
            validatePassword();
        }
    }//GEN-LAST:event_txtNewPasswordKeyPressed

    private void txtCurrentPasswordKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCurrentPasswordKeyPressed
        // Add your handling code here:
        if(evt.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER) {
            validatePassword();
        }
    }//GEN-LAST:event_txtCurrentPasswordKeyPressed

    private void validatePassword() {
        currentPassword = txtCurrentPassword.getPassword();
        newPassword = txtNewPassword.getPassword();
        confirmPassword = txtConfirmPassword.getPassword();
        String strNewPsw = new String(txtNewPassword.getPassword());
        String strConfirmPsw = new String(txtConfirmPassword.getPassword());
        int newPswLen = newPassword.length;
        int confirmPswLen = confirmPassword.length;
        if(currentPassword.length < 1) {
            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey("changePassword_exceptionCode.1001"));
            setWindowFocus(txtCurrentPassword);
            return;
        }
        if(newPswLen < 1) {
           CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey("changePassword_exceptionCode.1002"));
           setWindowFocus(txtNewPassword);
           return; 
        }
        if(confirmPswLen < 1) {
            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey("changePassword_exceptionCode.1003"));
            setWindowFocus(txtConfirmPassword);
            return; 
        }
        if(newPswLen != confirmPswLen ) {
            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey("changePassword_exceptionCode.1004"));
            setWindowFocus(txtNewPassword);
            return;
        }
        if(!strNewPsw.equals(strConfirmPsw)) {
            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey("changePassword_exceptionCode.1004"));
            setWindowFocus(txtNewPassword);
            return;
        }
        if(Character.isDigit(newPassword[0])) {
            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey("changePassword_exceptionCode.1005"));
            setWindowFocus(txtNewPassword);
            return;
        }
        for(int index=0; index < newPswLen; index++) {
            if(!Character.isLetterOrDigit(newPassword[index])) {
                CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey("changePassword_exceptionCode.1006"));
                txtNewPassword.requestFocusInWindow();
                return;
            }
        }
        if(!checkCurrentPassword()) {
            CoeusOptionPane.showInfoDialog(serverMsg);
            setWindowFocus(txtCurrentPassword);
            return;
        }else {
            CoeusOptionPane.showInfoDialog(serverMsg);
            dlgChangePassWord.dispose();
            return;
        }
    }
    
    private void btnOkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOkActionPerformed
        // TODO add your handling code here:
        validatePassword();
    }//GEN-LAST:event_btnOkActionPerformed

    private boolean checkCurrentPassword() {
        RequesterBean requesterBean = new RequesterBean();
        requesterBean.setId(mdiForm.getUserId());
        requesterBean.setFunctionType('B');
        Vector dataObject = new Vector();
        dataObject.add(new String(currentPassword));
        dataObject.add(new String(newPassword));
        requesterBean.setDataObject(dataObject);
        AppletServletCommunicator appletServletCommunicator = new AppletServletCommunicator(CoeusGuiConstants.CONNECTION_URL+GET_SERVLET, requesterBean);
        appletServletCommunicator.setRequest(requesterBean);
        appletServletCommunicator.send();
        ResponderBean responderBean = appletServletCommunicator.getResponse();
        if(responderBean != null) {
            if(responderBean.isSuccessfulResponse()) {
                serverMsg = responderBean.getMessage();
                return true;
            }else {
                serverMsg = responderBean.getMessage();
                return false;
            }
        }
        return false;
    }
    
    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // TODO add your handling code here:
        dlgChangePassWord.dispose();
    }//GEN-LAST:event_btnCancelActionPerformed
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JButton btnCancel;
    public javax.swing.JButton btnOk;
    public javax.swing.JPanel jPanel1;
    public javax.swing.JLabel lblConfirmPassword;
    public javax.swing.JLabel lblCurrentPassword;
    public javax.swing.JLabel lblNewPassword;
    public javax.swing.JPasswordField txtConfirmPassword;
    public javax.swing.JPasswordField txtCurrentPassword;
    public javax.swing.JPasswordField txtNewPassword;
    // End of variables declaration//GEN-END:variables
    
}
