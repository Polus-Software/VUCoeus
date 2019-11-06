/*
 * WelcomeForm.java
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 * Created on November 5, 2004, 8:33 PM
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
import edu.mit.coeus.bean.UserInfoBean;
import edu.mit.coeus.utils.ScreenFocusTraversalPolicy;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentEvent;
import javax.swing.AbstractAction;
import java.awt.Frame;

public class WelcomeForm extends javax.swing.JComponent {
    
    private CoeusDlgWindow dlgWelcome;
    private static final int WIDTH = 410;
    private static final int HEIGHT = 240;
    private char[] newPassword;
    private char[] confirmPassword;
    private CoeusMessageResources coeusMessageResources;
    private static final String GET_SERVLET = "/UtilityServlet";
    private static final String connect = CoeusGuiConstants.CONNECTION_URL + GET_SERVLET;
    private Frame frame;
    private String oldPassword="";
    private String serverMsg = "";
    private UserInfoBean userInfoBean;
    private CoeusAppletMDIForm mdiForm;
    
    /** Creates new form WelcomeForm */
    public WelcomeForm(CoeusAppletMDIForm mdiForm, String oldPassword,UserInfoBean userInfoBean) {
        this.mdiForm= mdiForm;
        this.oldPassword = oldPassword;
        this.userInfoBean = userInfoBean;
        initComponents();
        java.awt.Component[] components = {txtNewPassword,txtConfirmPassword,btnOk,btnCancel};
        ScreenFocusTraversalPolicy traversalPolicy = new ScreenFocusTraversalPolicy(components);
        setFocusTraversalPolicy(traversalPolicy);
        setFocusCycleRoot(true);
        // Enhancement 2019 - To get the Version for the Coeus - start
        getVersionTitle();
        // Enhancement 2019 - end
        postInitComponent();
    }
    
     // Enhancement 2019 - Get the Coeus Version
    private void getVersionTitle(){
        String title = getTitle();
        lblWelcome.setText("Welcome to Coeus\u2122 "+title);
    }
    
    /*Enhancement 2019 
     *Communicate with the server to read the coeus version number from the 
     *coeus parameters
     */
    private String getTitle(){
        String title = "";
        RequesterBean requesterBean = new RequesterBean();
        requesterBean.setFunctionType('D');
        AppletServletCommunicator appletServletCommunicator = new AppletServletCommunicator(CoeusGuiConstants.CONNECTION_URL+GET_SERVLET, requesterBean);
        appletServletCommunicator.setRequest(requesterBean);
        appletServletCommunicator.send();
        ResponderBean responderBean = appletServletCommunicator.getResponse();
        if(responderBean != null) {
           if(responderBean.isSuccessfulResponse()){
               title = (String)responderBean.getDataObject();
           }else{
               CoeusOptionPane.showErrorDialog(responderBean.getMessage());
               return "";
           }
        }
        return title;
    }
    
    private void postInitComponent() {
        lblWelcome.setFont(new javax.swing.plaf.FontUIResource("MS Sans Serif",Font.BOLD,20));
        lblUserName.setText(userInfoBean.getUserId().toUpperCase() + " ( " + userInfoBean.getUserName() +  " )");
        lblUnitNo.setText(userInfoBean.getUnitNumber() + " ( " + userInfoBean.getUnitName() + " )");
        coeusMessageResources = CoeusMessageResources.getInstance();
        txtNewPassword.setDocument(new LimitedPlainDocument(30));
        txtConfirmPassword.setDocument(new LimitedPlainDocument(30));
        dlgWelcome = new CoeusDlgWindow(mdiForm,"Welcome",true);
        dlgWelcome.getContentPane().add(this);
        dlgWelcome.setResizable(false);
        dlgWelcome.setFont(CoeusFontFactory.getLabelFont());
        dlgWelcome.setSize(WIDTH, HEIGHT);
        dlgWelcome.setResizable(false);
        Dimension dlgLocation = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension dlgPassWordSize = dlgWelcome.getSize();
        dlgWelcome.setLocation((dlgLocation.width/2 - dlgPassWordSize.width/2), (dlgLocation.height/2 - dlgPassWordSize.height/2));
        
        dlgWelcome.addComponentListener(
         new ComponentAdapter(){
             public void componentShown(ComponentEvent e){
                 setWindowFocus(txtNewPassword);
             }
         });
         dlgWelcome.setDefaultCloseOperation(CoeusDlgWindow.DO_NOTHING_ON_CLOSE);
         dlgWelcome.addWindowListener(new java.awt.event.WindowAdapter(){
             public void windowClosing(java.awt.event.WindowEvent e){
                 dlgWelcome.dispose();
                 System.exit(0);
             }
         });
         dlgWelcome.addEscapeKeyListener(new AbstractAction("escPressed") {
             public void actionPerformed(ActionEvent actionEvent) {
                 dlgWelcome.dispose();
                 System.exit(0);
             }
         });
    }
    
    public void setWindowFocus(final java.awt.Component component){
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                component.requestFocusInWindow();
            }
        });
    }
    
    
    public void display() {
        dlgWelcome.show();
    }
    
    
    private boolean changePassword() {
        RequesterBean requesterBean = new RequesterBean();
        requesterBean.setId(userInfoBean.getUserId());
        requesterBean.setFunctionType('C');
        requesterBean.setDataObject(new String(txtNewPassword.getPassword()));
        AppletServletCommunicator appletServletCommunicator = new AppletServletCommunicator(CoeusGuiConstants.CONNECTION_URL+GET_SERVLET, requesterBean);
        appletServletCommunicator.setRequest(requesterBean);
        appletServletCommunicator.send();
        ResponderBean responderBean = appletServletCommunicator.getResponse();
        if(responderBean != null) {
            if(responderBean.getException() != null) {
                serverMsg = responderBean.getException().getMessage();
            }else {
                serverMsg = responderBean.getMessage();
            }
            return responderBean.isSuccessfulResponse();
        }
        return false;
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        lblWelcome = new javax.swing.JLabel();
        lblMessage = new javax.swing.JLabel();
        lblNewPassword = new javax.swing.JLabel();
        lblConfirmPassword = new javax.swing.JLabel();
        txtNewPassword = new javax.swing.JPasswordField();
        txtConfirmPassword = new javax.swing.JPasswordField();
        separator = new javax.swing.JSeparator();
        pnlUserInfo = new javax.swing.JPanel();
        lblUserName = new javax.swing.JLabel();
        lblUnitNo = new javax.swing.JLabel();
        lblUser = new javax.swing.JLabel();
        lblUnit = new javax.swing.JLabel();
        btnOk = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();

        setLayout(new java.awt.GridBagLayout());

        setMinimumSize(new java.awt.Dimension(420, 205));
        setPreferredSize(new java.awt.Dimension(420, 205));
        lblWelcome.setFont(new java.awt.Font("Microsoft Sans Serif", 3, 18));
        lblWelcome.setForeground(new java.awt.Color(51, 51, 255));
        lblWelcome.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblWelcome.setText("Welcome to Coeus\u2122 4.0");
        lblWelcome.setMinimumSize(new java.awt.Dimension(420, 38));
        lblWelcome.setPreferredSize(new java.awt.Dimension(420, 38));
        lblWelcome.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 6, 0, 0);
        add(lblWelcome, gridBagConstraints);

        lblMessage.setFont(CoeusFontFactory.getNormalFont());
        lblMessage.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblMessage.setText("<html>This is the first time you have logged into the application. All users are <br> required to choose a new password for the Coeus\u2122 application. </html>");
        lblMessage.setFocusable(false);
        lblMessage.setMaximumSize(new java.awt.Dimension(420, 30));
        lblMessage.setMinimumSize(new java.awt.Dimension(420, 30));
        lblMessage.setPreferredSize(new java.awt.Dimension(420, 30));
        lblMessage.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 6, 8, 0);
        add(lblMessage, gridBagConstraints);

        lblNewPassword.setFont(CoeusFontFactory.getLabelFont());
        lblNewPassword.setText("Enter new password:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 6, 0, 0);
        add(lblNewPassword, gridBagConstraints);

        lblConfirmPassword.setFont(CoeusFontFactory.getLabelFont());
        lblConfirmPassword.setText("Confirm new password:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(13, 6, 0, 0);
        add(lblConfirmPassword, gridBagConstraints);

        txtNewPassword.setFont(CoeusFontFactory.getNormalFont());
        txtNewPassword.setMinimumSize(new java.awt.Dimension(150, 23));
        txtNewPassword.setPreferredSize(new java.awt.Dimension(150, 23));
        txtNewPassword.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtNewPasswordKeyPressed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 6, 0, 0);
        add(txtNewPassword, gridBagConstraints);

        txtConfirmPassword.setFont(CoeusFontFactory.getNormalFont());
        txtConfirmPassword.setMinimumSize(new java.awt.Dimension(150, 23));
        txtConfirmPassword.setPreferredSize(new java.awt.Dimension(150, 23));
        txtConfirmPassword.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtConfirmPasswordKeyPressed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(13, 6, 0, 0);
        add(txtConfirmPassword, gridBagConstraints);

        separator.setBorder(new javax.swing.border.EtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        separator.setMinimumSize(new java.awt.Dimension(420, 2));
        separator.setPreferredSize(new java.awt.Dimension(420, 2));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 3;
        add(separator, gridBagConstraints);

        pnlUserInfo.setLayout(new java.awt.GridBagLayout());

        pnlUserInfo.setMinimumSize(new java.awt.Dimension(420, 55));
        pnlUserInfo.setPreferredSize(new java.awt.Dimension(420, 55));
        lblUserName.setFont(CoeusFontFactory.getNormalFont());
        lblUserName.setMinimumSize(new java.awt.Dimension(250, 20));
        lblUserName.setPreferredSize(new java.awt.Dimension(250, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        pnlUserInfo.add(lblUserName, gridBagConstraints);

        lblUnitNo.setFont(CoeusFontFactory.getNormalFont());
        lblUnitNo.setMinimumSize(new java.awt.Dimension(250, 20));
        lblUnitNo.setPreferredSize(new java.awt.Dimension(300, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 0, 0);
        pnlUserInfo.add(lblUnitNo, gridBagConstraints);

        lblUser.setFont(CoeusFontFactory.getLabelFont());
        lblUser.setText("User:");
        lblUser.setMaximumSize(new java.awt.Dimension(45, 20));
        lblUser.setMinimumSize(new java.awt.Dimension(45, 20));
        lblUser.setPreferredSize(new java.awt.Dimension(45, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 6, 0, 0);
        pnlUserInfo.add(lblUser, gridBagConstraints);

        lblUnit.setFont(CoeusFontFactory.getLabelFont());
        lblUnit.setText("Unit:");
        lblUnit.setMaximumSize(new java.awt.Dimension(45, 20));
        lblUnit.setMinimumSize(new java.awt.Dimension(45, 20));
        lblUnit.setPreferredSize(new java.awt.Dimension(45, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 6, 0, 0);
        pnlUserInfo.add(lblUnit, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.insets = new java.awt.Insets(0, 6, 0, 0);
        add(pnlUserInfo, gridBagConstraints);

        btnOk.setFont(CoeusFontFactory.getLabelFont());
        btnOk.setMnemonic('O');
        btnOk.setText("OK");
        btnOk.setMaximumSize(new java.awt.Dimension(73, 23));
        btnOk.setMinimumSize(new java.awt.Dimension(73, 23));
        btnOk.setPreferredSize(new java.awt.Dimension(73, 23));
        btnOk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOkActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 10, 0, 0);
        add(btnOk, gridBagConstraints);

        btnCancel.setFont(CoeusFontFactory.getLabelFont());
        btnCancel.setMnemonic('C');
        btnCancel.setText("Cancel");
        btnCancel.setMaximumSize(new java.awt.Dimension(73, 23));
        btnCancel.setMinimumSize(new java.awt.Dimension(73, 23));
        btnCancel.setPreferredSize(new java.awt.Dimension(73, 23));
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(13, 10, 0, 0);
        add(btnCancel, gridBagConstraints);

    }//GEN-END:initComponents

    private void txtConfirmPasswordKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtConfirmPasswordKeyPressed
        // Add your handling code here:
        if(evt.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER) {
            validateNewPassword();
        }
    }//GEN-LAST:event_txtConfirmPasswordKeyPressed

    private void txtNewPasswordKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNewPasswordKeyPressed
        // Add your handling code here:
        if(evt.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER) {
            validateNewPassword();
        }
    }//GEN-LAST:event_txtNewPasswordKeyPressed

    private void validateNewPassword() {
        newPassword = txtNewPassword.getPassword();
        confirmPassword = txtConfirmPassword.getPassword();
        int newPswLen = newPassword.length;
        int confirmPswLen = confirmPassword.length;
        String strNewPsw = new String(txtNewPassword.getPassword());
        String strConfirmPsw = new String(txtConfirmPassword.getPassword());
        
        if(newPswLen < 1) {
            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey("welcomeform_exceptionCode.1051"));
            setWindowFocus(txtNewPassword);
            return;
        }
        if(confirmPswLen < 1) {
            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey("welcomeform_exceptionCode.1052"));
            setWindowFocus(txtConfirmPassword);
            return;
        }
        if(newPswLen != confirmPswLen ) {
            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey("welcomeform_exceptionCode.1053"));
            setWindowFocus(txtNewPassword);
            return;
        }
        if(!strNewPsw.equals(strConfirmPsw)) {
            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey("welcomeform_exceptionCode.1053"));
            setWindowFocus(txtNewPassword);
            return;
        }
        if(strNewPsw.equals(oldPassword)) {
            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey("welcomeform_exceptionCode.1054"));
            setWindowFocus(txtNewPassword);
            return;
        }
        if(Character.isDigit(newPassword[0])) {
            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey("welcomeform_exceptionCode.1055"));
            setWindowFocus(txtNewPassword);
            return;
        }
        
        for(int index=0; index < newPswLen; index++) {
            if(!Character.isLetterOrDigit(newPassword[index])) {
                CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey("welcomeform_exceptionCode.1056"));
                txtNewPassword.requestFocusInWindow();
                return;
            }
        }
        if(!changePassword()) {
            CoeusOptionPane.showInfoDialog(serverMsg);
            setWindowFocus(txtNewPassword);
            return;
        }else {
            CoeusOptionPane.showInfoDialog(serverMsg);
            dlgWelcome.dispose();
            return;
        }
    }
    
    private void btnOkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOkActionPerformed
        // Add your handling code here:
        validateNewPassword();
    }//GEN-LAST:event_btnOkActionPerformed

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // TODO add your handling code here:
        dlgWelcome.dispose();
        System.exit(0);
    }//GEN-LAST:event_btnCancelActionPerformed
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JButton btnCancel;
    public javax.swing.JButton btnOk;
    public javax.swing.JLabel lblConfirmPassword;
    public javax.swing.JLabel lblMessage;
    public javax.swing.JLabel lblNewPassword;
    public javax.swing.JLabel lblUnit;
    public javax.swing.JLabel lblUnitNo;
    public javax.swing.JLabel lblUser;
    public javax.swing.JLabel lblUserName;
    public javax.swing.JLabel lblWelcome;
    public javax.swing.JPanel pnlUserInfo;
    public javax.swing.JSeparator separator;
    public javax.swing.JPasswordField txtConfirmPassword;
    public javax.swing.JPasswordField txtNewPassword;
    // End of variables declaration//GEN-END:variables
    
}
