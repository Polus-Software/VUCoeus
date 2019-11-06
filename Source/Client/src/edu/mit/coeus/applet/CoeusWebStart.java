/*
 * CoeusApplet.java
 *
 * Created on October 7, 2003, 3:35 PM
 */

package edu.mit.coeus.applet;

import com.sun.security.auth.callback.DialogCallbackHandler;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.net.URL;
import java.util.Vector;

import edu.mit.coeus.gui.CoeusAppletMDIForm;
import edu.mit.coeus.bean.CoeusPropertiesBean;
import edu.mit.coeus.bean.LoginBean;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.utils.AppletServletCommunicator;
import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.utils.WelcomeForm;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.bean.UserInfoBean;
import edu.mit.coeus.exception.CoeusException;
import java.util.HashMap;
import java.applet.AppletContext;
import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.user.auth.AuthResponseListener;
import edu.mit.coeus.user.auth.ClientAuthServiceFactory;
import edu.mit.coeus.user.auth.ClientAuthServiceHelper;
import edu.mit.coeus.user.auth.CoeusAuthService;
import edu.mit.coeus.user.auth.bean.AuthXMLNodeBean;
import edu.mit.coeus.utils.CoeusPropertyKeys;
import edu.mit.coeus.utils.KeyConstants;
import java.awt.Frame;
import edu.mit.coeus.utils.ScreenFocusTraversalPolicy;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.util.Hashtable;
import java.util.Properties;
import java.util.Set;

public class CoeusWebStart extends javax.swing.JApplet implements ActionListener, AuthResponseListener {
    java.awt.Frame parentFrame = null ;
    
    private boolean userIdMode;
    private boolean validUser;
    private boolean firstLogin;
    private final static String KERBEROS = "KERBEROS";
    private final static String USERID = "USERID";
    private final static String LDAP = "LDAP";
    private final static String LDAP_WITH_USER_MAP = "LDAP_WITH_USER_MAP";
    
    private final static String LDAPURL = "LDAPURL";
    private final static String SSL = "SSL";
    
    public static String CONNECTION_URL;
    // 2930: Auto-delete Current Locks based on new parameter 
//    public static int POLLING_INTERVAL;
    
    private ResponderBean response;
    /*CASE #304 begin */
    String errorMsg = null;
    /* Use to avoid popping up multiple error message windows */
    boolean errorMsgDisplayed = false;
    /*CASE #304 end */
    
    private Container contentPane = getContentPane();
    private String hostName = null ;
    
    int port = -1;
    /* context path  */
    private String contextPath = "/Coeus4";
    private final String ACTIONPATH = "/loginServlet";
    
    private CoeusPropertiesBean coeusProperties;
    
    //holds CoeusMessageResources instance used for reading message Properties.
    private CoeusMessageResources coeusMessageResources;
    
    // holds the user id obtained from the server
    private String userId = null ;
    private UserInfoBean userInfoBean;
    private String globalImageName;
    private String instanceName;
    private String loginMode = USERID;
    private AuthXMLNodeBean authNodeBean;
    /** Creates new form CoeusApplet */
    public CoeusWebStart() {
        super();
        initComponents();
    }
    
    public CoeusWebStart(java.awt.Frame parent) {
        super();
        initComponents();
        parentFrame = parent ;
    }
    
    public void showMDIForm(){
        CoeusAppletMDIForm mdiform = new CoeusAppletMDIForm();
        mdiform.setParentFrame(parentFrame);
        mdiform.setAppletCodeBase(getAppletCodeBase());
        mdiform.setCoeusAppletContext( getAppletnJWSContext() );
        mdiform.setRoleID(0);
        mdiform.setUserName(userId);
        mdiform.setUserInfo(userInfoBean);
        mdiform.setCoeusGlobalImageName(globalImageName);
        mdiform.setInstanceName(instanceName);
        edu.mit.coeus.utils.CoeusGuiConstants.setMDIForm(mdiform);
        if(firstLogin && CoeusGuiConstants.SWING_LOGIN_MODE.equals(CoeusGuiConstants.USERID)) {
            showWelcomeForm(mdiform);
        }        
        
        mdiform.showForm();
        validUser=true;
    }
    /**
     * Method to initialize the applet. During the initialization, it will attach all
     * the required parameters with the <code>Coeus</code> Application.
     */
    public void init() {
        super.init();
        setCoeusProperties();
        System.out.println("Java Version : "+System.getProperty("java.version"));
        System.out.println("OS : "+System.getProperty("os.name"));
        coeusMessageResources = CoeusMessageResources.getInstance();
        String codeBase = null ;
        try {
            javax.jnlp.BasicService bs
            = (javax.jnlp.BasicService)javax.jnlp.ServiceManager.lookup("javax.jnlp.BasicService");
            codeBase = (String)bs.getCodeBase().toString() ;
            System.out.println("Java Web Start code base" + codeBase) ;
        }catch(Exception jnlpex) {
            //                codeBase = this.getAppletCodeBase().toString();
            codeBase = "http://localhost:8080/Coeus45Trunk/";
        }
        //CONNECTION_URL = codeBase.substring(0,codeBase.length()-1);
        CONNECTION_URL = codeBase.replaceAll("/$", "");
        CoeusGuiConstants.CONNECTION_URL = CONNECTION_URL;
        // 2930: Auto-delete Current Locks based on new parameter
//        CoeusGuiConstants.POLLING_INTERVAL = POLLING_INTERVAL;
        
        // find the login mode from coeus.properties file on the server
        // and either present the user userid/pwd screen or
        // directly take him/her to the MDIFOrm
        loginMode = getLoginMode() ;
        CoeusGuiConstants.SWING_LOGIN_MODE = loginMode;
        
        try{
            if((loginMode.equals(USERID) || loginMode.equals(LDAP) || loginMode.equals(LDAP_WITH_USER_MAP))){
                userIdMode = true;
                txtUserName.addActionListener(this);
                password.addActionListener(this);
                password.setEchoChar('*');
                //focus traversal policy
                // starts
                java.awt.Component[] components = {txtUserName,password,ok, reset};
                ScreenFocusTraversalPolicy  traversePolicy = new ScreenFocusTraversalPolicy( components );
                pnlMain.setFocusTraversalPolicy(traversePolicy);
                pnlMain.setFocusCycleRoot(true);
                //ends
                
                txtUserName.requestFocus() ;
                // get the host name and port of the applet's web server.
                try {
                    URL hostURL = getCodeBase() ;
                    String protocol = hostURL.getProtocol();
                    hostName = hostURL.getHost();
                    port = hostURL.getPort();
                    contextPath = hostURL.getPath();
                    if (port == -1) {
                        port = 80;
                    }
                }catch(Exception ex) { //in case of jws
                    try {
                        javax.jnlp.BasicService bs
                        = (javax.jnlp.BasicService)javax.jnlp.ServiceManager.lookup("javax.jnlp.BasicService");
                        URL hostURL = bs.getCodeBase() ;
                        String protocol = hostURL.getProtocol();
                        port = hostURL.getPort();
                        contextPath = hostURL.getPath();
                        if (port == -1) {
                            port = 8080 ;
                        }
                    }catch(Exception jnlpex) {
                        jnlpex.printStackTrace() ;
                    }
                }// end catch ex
            }else{
                authenticateUser();
                setValidUser(true);
//                if(authenticateUser()){
//                    setValidUser(true);
//                    showMDIForm();
//                }
            }
        }catch(CoeusException ex){
            ex.printStackTrace();
            String parsedError = CoeusMessageResources.getInstance().parseMessageKey(ex.getMessage());
            CoeusOptionPane.showErrorDialog(this,parsedError==null?ex.getMessage():parsedError);
        }
    }
    
    public void setInitFocus(){
        txtUserName.requestFocus();
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        pnlMain = new javax.swing.JPanel();
        lblPassword = new javax.swing.JLabel();
        lblUser = new javax.swing.JLabel();
        txtUserName = new javax.swing.JTextField();
        ok = new javax.swing.JButton();
        reset = new javax.swing.JButton();
        lblHeader = new javax.swing.JLabel();
        password = new javax.swing.JPasswordField();
        jLabel1 = new javax.swing.JLabel();

        getContentPane().setLayout(new java.awt.GridBagLayout());

        setBackground(new java.awt.Color(204, 204, 204));
        pnlMain.setLayout(new java.awt.GridBagLayout());

        pnlMain.setBackground(new java.awt.Color(204, 204, 204));
        pnlMain.setPreferredSize(new java.awt.Dimension(400, 150));
        lblPassword.setFont(CoeusFontFactory.getLabelFont());
        lblPassword.setText("Password :");
        lblPassword.setFocusable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 6);
        pnlMain.add(lblPassword, gridBagConstraints);

        lblUser.setFont(CoeusFontFactory.getLabelFont());
        lblUser.setText("User Name :");
        lblUser.setFocusable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 6);
        pnlMain.add(lblUser, gridBagConstraints);

        txtUserName.setColumns(10);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.ipadx = 150;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 3);
        pnlMain.add(txtUserName, gridBagConstraints);

        ok.setFont(CoeusFontFactory.getLabelFont());
        ok.setText("OK");
        ok.setMaximumSize(new java.awt.Dimension(75, 23));
        ok.setMinimumSize(new java.awt.Dimension(75, 23));
        ok.setPreferredSize(new java.awt.Dimension(75, 23));
        ok.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 1, 3, 20);
        pnlMain.add(ok, gridBagConstraints);

        reset.setFont(CoeusFontFactory.getLabelFont());
        reset.setText("Cancel");
        reset.setMaximumSize(new java.awt.Dimension(75, 23));
        reset.setMinimumSize(new java.awt.Dimension(75, 23));
        reset.setPreferredSize(new java.awt.Dimension(67, 23));
        reset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                resetActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 1, 0, 20);
        pnlMain.add(reset, gridBagConstraints);

        lblHeader.setFont(CoeusFontFactory.getLabelFont());
        lblHeader.setText("Enter a User ID and Password to log onto Coeus");
        lblHeader.setFocusable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipady = 25;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 0, 8, 0);
        pnlMain.add(lblHeader, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.ipadx = 150;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 3);
        pnlMain.add(password, gridBagConstraints);

        // JM 12-19-2014 commented out image
        //jLabel1.setIcon(new ImageIcon(getClass().getClassLoader().getResource("coeus.gif") ));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 31;
        gridBagConstraints.ipady = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 3);
        pnlMain.add(jLabel1, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        getContentPane().add(pnlMain, gridBagConstraints);

    }// </editor-fold>//GEN-END:initComponents
    
    private void resetActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_resetActionPerformed
    {//GEN-HEADEREND:event_resetActionPerformed
        actionPerformed(evt) ;
    }//GEN-LAST:event_resetActionPerformed
    
    private void okActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_okActionPerformed
    {//GEN-HEADEREND:event_okActionPerformed
        actionPerformed(evt) ;
    }//GEN-LAST:event_okActionPerformed
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JLabel jLabel1;
    public javax.swing.JLabel lblHeader;
    public javax.swing.JLabel lblPassword;
    public javax.swing.JLabel lblUser;
    public javax.swing.JButton ok;
    public javax.swing.JPasswordField password;
    public javax.swing.JPanel pnlMain;
    public javax.swing.JButton reset;
    public javax.swing.JTextField txtUserName;
    // End of variables declaration//GEN-END:variables
    
    public String getLoginMode() {
        boolean serverResponse=true;
        String connectTo = CONNECTION_URL+"/loginServlet";
        loginMode = USERID ;  // by default have userid/pwd
        
        RequesterBean request = new RequesterBean();
        request.setId("LOGINMODE") ;
        
        System.out.println("Start communicating with the server  to get login mode" ) ;
        AppletServletCommunicator comm = new AppletServletCommunicator
        (connectTo, request);
        comm.send();
        ResponderBean response = comm.getResponse();
        System.out.println("Got response from server" ) ;
        if (response == null) {
            
            response = new ResponderBean();
            response.setResponseStatus(false);
            /* CASE #304 begin */
            String msg = coeusMessageResources.parseMessageKey(
            "server_exceptionCode.1147");
            errorMsg = msg;
            response.setMessage(msg);
            /* CASE #304 end */
        }
        serverResponse = response.isSuccessfulResponse();
        System.out.println("Server response is " +  serverResponse ) ;
        
        if (serverResponse){
            HashMap hashProps = (HashMap)response.getDataObject() ;
            loginMode = hashProps.get("LoginMode").toString() ;
            globalImageName = (String)hashProps.get(CoeusPropertyKeys.COEUS_GLOBAL_IMAGE) ;
            // 2930: Auto-delete Current Locks based on new parameter
//            CoeusGuiConstants.POLLING_INTERVAL = Integer.parseInt(hashProps.get(CoeusPropertyKeys.POLLING_INTERVAL).toString());
            CoeusGuiConstants.HELP_URL = (String)hashProps.get(CoeusPropertyKeys.HELP_URL);
            String kerbRealm = (String)hashProps.get(CoeusPropertyKeys.KERBEROS_REALM);
            String kerbServer = (String)hashProps.get(CoeusPropertyKeys.KERBEROS_KDC_SERVER);
            instanceName = (String)hashProps.get(CoeusPropertyKeys.DB_INSTANCE_NAME);
            /*
             *Added by Geo for refactoring Authentication with factory
             */
            authNodeBean = (AuthXMLNodeBean)hashProps.get(CoeusPropertyKeys.CLIENT_AUTH_CLASS);
            String secLoginMode = (String)hashProps.get(CoeusPropertyKeys.SECONDARY_LOGIN_MODE);
            if(secLoginMode!=null && !secLoginMode.trim().equals("")){
                CoeusGuiConstants.SECONDARY_LOGIN_MODE = secLoginMode;
            }
            System.out.println("Server found the Login Mode as " + loginMode) ;
        }
        else{
            System.out.println("Server couldn't find the Login Mode" ) ;
        }
        return loginMode ;
    }
    
    private boolean authenticateUser() throws CoeusException{
        CoeusAuthService authService = ClientAuthServiceFactory.getInstance(this.authNodeBean);
        authService.addParam("LOGIN_MODE", loginMode);
        authService.addParam("PARENT_FRAME",parentFrame);
        authService.addParam(AuthXMLNodeBean.class ,authNodeBean);
        authService.addResponseListener(this);
        return authService.authenticate();
    }
    
    /** The method used to validate the username and password. It connects to the servlet and
     * do the user authentication by communicating with database and return the
     * status. Any failure in authentication will return back to applet with error message.
     *
     * @param pass Array of characters
     * @param usr String Username
     * @return  boolean true if the username and password are valid user else False
     */
    private boolean authenticateUser(String usr, char[] pass) throws CoeusException{
        CoeusAuthService authService = ClientAuthServiceFactory.getInstance(this.authNodeBean);
        authService.addParam("LOGIN_MODE", loginMode);
        authService.addParam("USER_ID", usr);
        authService.addParam("PASSWORD", new String(pass));
        authService.addResponseListener(this);
        return authService.authenticate();
    }
    
    /**
     *  The method used to set the look and feel for the applet.
     */
    public void setCoeusProperties(){
        coeusProperties = new CoeusPropertiesBean();
        String className = coeusProperties.getLookAndFeelClass();
        try {
            UIManager.installLookAndFeel("coeus", className);
            UIManager.setLookAndFeel(className);
        }catch(Exception ex) {
            ex.printStackTrace();
        }
        System.out.println("UIManager.getLookAndFeel: "+UIManager.getLookAndFeel());
        contentPane.setBackground(coeusProperties.getBackgroundColor());
        //contentPane.setForeground(coeusProperties.getForegroundColor());
        contentPane.setFont(coeusProperties.getFont());
    }
    
    /**
     * The overridden method, which will be implemented for the <code>OK</code> button
     * <code>Cancel</code> button. If there is an error with user authentication, it will
     * alert the error messgae to the user.
     *
     * @param ae ActionEvent
     */
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() != reset) {
            /* Reset errorMsgDisplayed to false. */
            errorMsgDisplayed = false;
            try{
                if(authenticateUser(txtUserName.getText(), password.getPassword())){
                    ok.setEnabled(false);
                    reset.setEnabled(false);
                    txtUserName.setEditable(false);
                    password.setEnabled(false);
//                    CoeusAppletMDIForm mdiform = new CoeusAppletMDIForm();
//                    // validation success
//                    if (parentFrame != null) {
//                        mdiform.setParentFrame(parentFrame) ;
//                    }
//                    else {
//                        mdiform.setParentFrame(null) ;
//                    }
                    
                    //added to show the welcome screen if the user is logging in first time
//                    if(firstLogin && CoeusGuiConstants.SWING_LOGIN_MODE.equals(CoeusGuiConstants.USERID)) {
//                        showWelcomeForm(mdiform);
//                    }
//                    showMDIForm();
                    resetFields();
                    if (parentFrame != null) {
                        parentFrame.setVisible(false) ;
                    }
                }else{
                    // validation failed
                    /*CASE #304 Begin */
                    /* Display error window only if another error
                       window, such as blank username/password has not
                       already been displayed. */
                    if(!errorMsgDisplayed){
                        if(errorMsg != null){
                            log(errorMsg);
                        }
                        else{
                            /*CASE #304 End */
                            log(coeusMessageResources.parseMessageKey(
                            "coeusApplet_exceptionCode.1146"));
                        }
                    }
                }
            }catch(Exception ex){
                ex.printStackTrace();
                log(ex.getMessage());
            }
        } else {
            if (parentFrame == null) {
                resetFields();
            }
            else {
                System.exit(0) ; // cancel on JWS shud exit the application
            }
        }
    }
    /**
     * Method used to show the welcome screen if the user is logging in for the first time
     **/
    private void showWelcomeForm(CoeusAppletMDIForm mdiform) {
        
        WelcomeForm welcomeForm = new WelcomeForm(mdiform, new String(password.getPassword()),userInfoBean);
        /*postInitWelcomForm();*/
        welcomeForm.display();
    }
    
    /**
     * Method used to clear the data from the controls
     */
    public void resetFields(){
        txtUserName.setText("");
        password.setText("");
        
    }
    
    /** get the authentication role id
     * @return  int Role ID
     */
    public int getRoleID(){
        //        int roleID=0;
        return 0;
    }
    /**
     * Displays the message,it gives the error message.
     * @param mesg String
     */
    public void log(String mesg) {
        CoeusOptionPane.showWarningDialog(this,mesg);
    }
    
    /**
     * Get the Applet code base
     * @return URL Applet code Base.
     */
    public URL getAppletCodeBase(){
        //prps start - 1 oct 2003
        URL hostURL = null ;
        // in case of JWS it will be null
        try {
            hostURL = getCodeBase() ;
        }
        catch(Exception ex) {
            //ex.printStackTrace();
            try {
                javax.jnlp.BasicService bs
                = (javax.jnlp.BasicService)javax.jnlp.ServiceManager.lookup("javax.jnlp.BasicService");
                hostURL = bs.getCodeBase() ;
            }
            catch(Exception jnlpex) {
                //jnlpex.printStackTrace() ;
            }
        }
        return hostURL;
        //prps end - 1 oct 2003
    }
    
    
    private AppletContext getAppletnJWSContext() {
        try {
            if (getAppletContext()==null)
                return null ;
            else
                return getAppletContext() ;
        }
        catch(Exception ex) {
            // ex.printStackTrace() ;
        }
        return null ;
    }
    
    /** Getter for property userIdMode.
     * @return Value of property userIdMode.
     *
     */
    public boolean isUserIdMode() {
        return userIdMode;
    }
    
    /** Setter for property userIdMode.
     * @param userIdMode New value of property userIdMode.
     *
     */
    public void setUserIdMode(boolean userIdMode) {
        this.userIdMode = userIdMode;
    }
    
    /** Getter for property validUser.
     * @return Value of property validUser.
     *
     */
    public boolean isValidUser() {
        return validUser;
    }
    
    /** Setter for property validUser.
     * @param validUser New value of property validUser.
     *
     */
    public void setValidUser(boolean validUser) {
        this.validUser = validUser;
    }
    
    public void respond(Object res) throws CoeusException {
        if(res!=null && res instanceof ResponderBean){
            ResponderBean authResponse = (ResponderBean)res;
            if(authResponse.isSuccessfulResponse()){
                setMDIFormAttr(authResponse);
                setValidUser(true);
                showMDIForm();
            }else{
                //login failed
                System.out.println("Login failed");
            }
        }
    }
    public void setMDIFormAttr(ResponderBean res) throws CoeusException {
        Hashtable resData = (Hashtable)res.getDataObject();
        userId = (String)resData.get(KeyConstants.USER_ID);
        userInfoBean = (UserInfoBean)resData.get(KeyConstants.USER_INFO);
        firstLogin = ((Boolean)resData.get(KeyConstants.FIRST_TIME_LOGIN)).booleanValue();
        
        //Check if User is Active
        if(userInfoBean.getStatus() != 'A') {
            throw new CoeusException(
            coeusMessageResources.parseMessageKey("coeusApplet_exceptionCode.1165"));
        }
        
    }
    
}
