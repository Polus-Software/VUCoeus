/*
 * CoeusApplet.java
 *
 * Created on October 7, 2003, 3:35 PM
 */

package edu.mit.coeus.applet;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.net.URL;
import java.util.Vector;

import edu.mit.coeus.gui.CoeusAppletMDIForm;
import edu.mit.coeus.bean.CoeusPropertiesBean;
import edu.mit.coeus.bean.LoginBean;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.utils.WelcomeForm;
import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.utils.AppletServletCommunicator;
import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.bean.UserInfoBean;
import java.util.HashMap;
import java.applet.AppletContext;
import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.utils.CoeusPropertyKeys;
import java.awt.Frame;
import edu.mit.coeus.utils.ScreenFocusTraversalPolicy;

public class CoeusApplet extends javax.swing.JApplet implements ActionListener
{
    java.awt.Frame parentFrame = null ;
    boolean firstLogin;
    
    /** Creates new form CoeusApplet */
    public CoeusApplet()
    {
        super();
        initComponents();
    }

    public CoeusApplet(java.awt.Frame parent)
    {
        super();
        initComponents();
        parentFrame = parent ;
    }
    
         /**
     * This field is used to represent the connection URL.
     */
    public static String CONNECTION_URL;
    
    private ResponderBean response;
    private String globalImageName;
    JTextField txtTitle;
    //JLabel lblHeader,lblUser,lblPassword;
//    JPasswordField password;
//    JButton ok,reset; 
    /*CASE #304 begin */
    String errorMsg = null;
    /* Use to avoid popping up multiple error message windows */
    boolean errorMsgDisplayed = false;
    /*CASE #304 end */
    
    Container contentPane = getContentPane();    
    /** url connection to the servlet */
    /** servlet url string */
    //String hostName = "localhost"; // default hostname //prps commented 1 oct 2003
    //prps start 1 oct 2003
    String hostName = null ; 
    String hostCompleteURL = null ;
    //prps end 1 oct 2003 
    
    int port = -1;
    /* context path  */
    private String contextPath = "/CoeusApplet";
    private final String ACTIONPATH = "/loginServlet";

    CoeusPropertiesBean coeusProperties;
       
    //holds CoeusMessageResources instance used for reading message Properties.
    private CoeusMessageResources coeusMessageResources;

    // holds the user id obtained from the server
    private String sslUserId = null ;     
    private UserInfoBean userInfoBean;
    private String insatnceName;
    
    
    /**
     * Method to initialize the applet. During the initialization, it will attach all
     * the required parameters with the <code>Coeus</code> Application.
     */
    public void init() {
        super.init();
        setCoeusProperties();
        
        coeusMessageResources = CoeusMessageResources.getInstance();
        String codeBase = null ;
        
        try
        {
            //System.out.println("Will try Jnlp codebase") ;
            codeBase = this.getAppletCodeBase().toString();
            //System.out.println("Java Web Start got the code base without error " + codeBase) ;
        }
        catch(Exception ex)
        {
           try
           {
            javax.jnlp.BasicService bs 
                = (javax.jnlp.BasicService)javax.jnlp.ServiceManager.lookup("javax.jnlp.BasicService");
            codeBase = (String)bs.getCodeBase().toString() ;
            System.out.println("Java Web Start code base" + codeBase) ;
           }
           catch(Exception jnlpex)
           {
            //jnlpex.printStackTrace() ;
           }
        }
//        codeBase = "http://localhost:8081/";
        CONNECTION_URL = codeBase.substring(0,codeBase.length()-1);
        CoeusGuiConstants.CONNECTION_URL = CONNECTION_URL;
        
        // find the login mode from coeus.properties file on the server
        // and either present the user userid/pwd screen or
        // directly take him/her to the MDIFOrm
        String loginType = getLoginMode() ;
        
         if (loginType.equals("LDAPURL")){
          System.out.println("*** LDAP login *** " ) ;
          if (validateLDAPUser()){
             //validation success
             CoeusAppletMDIForm mdiform = new CoeusAppletMDIForm();
             mdiform.setAppletCodeBase(getAppletCodeBase());
             mdiform.setCoeusAppletContext( getAppletnJWSContext() );
             mdiform.setRoleID(0);
             mdiform.setUserName(sslUserId);
             mdiform.setUserInfo(userInfoBean);
             mdiform.setCoeusGlobalImageName(globalImageName);
             // Bug Fix #1135 - 17-Aug-2004 - start
             try{
                 UIManager.setLookAndFeel(UIManager.getLookAndFeel());
                 javax.swing.SwingUtilities.updateComponentTreeUI(mdiform);//.getJMenuBar());
             }catch (UnsupportedLookAndFeelException ex){
                 ex.printStackTrace();
             }
             //Bug fix Case #2137 start 1
             if(userInfoBean != null && userInfoBean.getStatus() == 'I'){
                 CoeusOptionPane.showInfoDialog("'"+userInfoBean.getUserId()+"' is Inactive.") ;
                 return;
             }
             //Bug fix Case #2137 end 1
             // Bug Fix #1135 - 17-Aug-2004 - End
             mdiform.showForm();
          }else{
             CoeusOptionPane.showInfoDialog("LDAP validation failed") ;
          }    
        }else if (loginType.equals("SSL")){
           System.out.println("*** client initiating SSL... *** " ) ;
            if (validateUserSSL())
            {
                    //CoeusOptionPane.showInfoDialog("SSL validation successfull.User id obtained:" + sslUserId) ;
                    //validation success
                    CoeusAppletMDIForm mdiform = new CoeusAppletMDIForm();
                    mdiform.setAppletCodeBase(getAppletCodeBase());
                    mdiform.setCoeusAppletContext( getAppletnJWSContext() );
                    mdiform.setRoleID(0);
                    mdiform.setUserName(sslUserId);
                    mdiform.setUserInfo(userInfoBean);
                    mdiform.setCoeusGlobalImageName(globalImageName);
                    // Bug Fix #1135 - 17-Aug-2004
                    try{
                        UIManager.setLookAndFeel(UIManager.getLookAndFeel());
                        javax.swing.SwingUtilities.updateComponentTreeUI(mdiform);
                    }catch (UnsupportedLookAndFeelException ex){
                        ex.printStackTrace();
                    }
                    //Bug fix Case #2137 start 2
                    if(userInfoBean != null && userInfoBean.getStatus() == 'I'){
                        CoeusOptionPane.showInfoDialog("'"+userInfoBean.getUserId()+"' is Inactive.") ;
                        return;
                    }
                    //Bug fix Case #2137 end 2
                    // Bug Fix #1135 - 17-Aug-2004
                    mdiform.showForm();
            }
            else{
                CoeusOptionPane.showInfoDialog("SSL validation failed") ;
            }
           
        }
        else{
            //contentPane.setLayout(null);
            //                lblHeader = new JLabel("Enter a User ID and password to log onto Coeus");
            //                lblUser = new JLabel("User ID:");
            //                lblPassword = new JLabel("Password:");
            //                lblHeader.setForeground(coeusProperties.getForegroundColor1());
            //                lblUser.setForeground(coeusProperties.getForegroundColor1());
            //                txtUserName.setNextFocusableComponent(password);
            //                lblPassword.setForeground(coeusProperties.getForegroundColor1());
            //                password.setNextFocusableComponent(ok);
            //                ok.setNextFocusableComponent(reset);
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
            //                txtUserName.requestFocus();
            // get the host name and port of the applet's web server.
            //prps - 2 oct 2003
            try {
                URL hostURL = getCodeBase() ;
                // modified by Subramanya
                String protocol = hostURL.getProtocol();
                hostName = hostURL.getHost();
                port = hostURL.getPort();
                contextPath = hostURL.getPath();
                if (port == -1) {
                    port = 80;
                }
            }
            catch(Exception ex) { //in case of jws
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
                }
                catch(Exception jnlpex) {
                    //jnlpex.printStackTrace() ;
                }
            }// end catch ex
            
        }// end else
        
    }
    
    
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    
    private void initComponents() {//GEN-BEGIN:initComponents
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

        pnlMain.setLayout(new java.awt.GridBagLayout());

        pnlMain.setBackground(new java.awt.Color(204, 204, 204));
        pnlMain.setPreferredSize(new java.awt.Dimension(380, 150));
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
        ok.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.ipadx = 23;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        pnlMain.add(ok, gridBagConstraints);

        reset.setFont(CoeusFontFactory.getLabelFont());
        reset.setText("Cancel");
        reset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                resetActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.ipadx = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
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

    }//GEN-END:initComponents

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
   
    
        public String getLoginMode()
        {
             boolean isValidationSuccess=true;
             String connectTo = CONNECTION_URL+"/loginServlet";
             String loginMode = "USERID" ;  // by default have userid/pwd
            
            RequesterBean request = new RequesterBean();
            request.setId("LOGINMODE") ;
            //request.setDataObject(loginBean);
            
            System.out.println("*** start communicating with the server  to get login mode *** " ) ;
            AppletServletCommunicator comm = new AppletServletCommunicator
                                                    (connectTo, request);
            comm.send();
            ResponderBean response = comm.getResponse();
            System.out.println("*** got response from server *** " ) ;
            if (response == null) {
                
                response = new ResponderBean();
                response.setResponseStatus(false);
                /* CASE #304 begin */
                String msg = coeusMessageResources.parseMessageKey(
                                                "server_exceptionCode.1147");
                errorMsg = msg;
                response.setMessage(msg);
                //response.setMessage(coeusMessageResources.parseMessageKey(
                                                //"server_exceptionCode.1000"));
                /* CASE #304 end */
            }
            
            isValidationSuccess = response.isSuccessfulResponse();
            System.out.println("*** obtd status " +  isValidationSuccess  + " *** " ) ;
             
            if (isValidationSuccess){
               HashMap hashProps = (HashMap)response.getDataObject() ;
               loginMode = hashProps.get("LoginMode").toString() ;
               globalImageName = (String)hashProps.get("COEUS_GLOBAL_IMAGE") ;
               // 2930: Auto-delete Current Locks based on new parameter 
//               CoeusGuiConstants.POLLING_INTERVAL = Integer.parseInt(hashProps.get("POLLING_INTERVAL").toString());
               insatnceName = (String)hashProps.get(CoeusPropertyKeys.JDBC_DRIVER_URL);
               System.out.println("*** Server found the Login Mode as " + loginMode + " *** " ) ;
            } 
            else{
                System.out.println("*** Server couldn't find the Login Mode *** " ) ;
            }
            
        return loginMode ;
    
    
    }
    
    public boolean validateLDAPUser() {
        boolean isValidationSuccess=true;
        
            String connectTo = CONNECTION_URL+"/loginServlet";

            //String connectTo = "https://localhost:8443/CoeusApplet" + "/loginServlet";

            System.out.println("Connection URL ---> " + connectTo);
            
            //LoginBean loginBean = new LoginBean(usr,password);
            
            RequesterBean request = new RequesterBean();
            request.setId("LDAPURL") ;
            //request.setDataObject(loginBean);
            System.out.println("*** start communicating with the server *** " ) ;
            AppletServletCommunicator comm = new AppletServletCommunicator(connectTo, request);
            comm.send();
            ResponderBean response = comm.getResponse();
            System.out.println("*** got response from server *** " ) ;
            if (response == null) {
                
                response = new ResponderBean();
                response.setResponseStatus(false);
                response.setMessage(coeusMessageResources.parseMessageKey(
                                                "server_exceptionCode.1000"));
            }
            
            isValidationSuccess = response.isSuccessfulResponse();
            System.out.println("*** obtained status " +  isValidationSuccess  + " *** " ) ;
            
            if (isValidationSuccess){
                sslUserId = response.getDataObject().toString() ;
                userInfoBean = (UserInfoBean)((Vector)response.getDataObjects()).get(0);
                globalImageName = (String )((Vector)response.getDataObjects()).get(1);
                // 2930: Auto-delete Current Locks based on new parameter 
//                CoeusGuiConstants.POLLING_INTERVAL = Integer.parseInt(((Vector)response.getDataObjects()).get(2).toString());
            }else{
                sslUserId = null ;
                CoeusOptionPane.showErrorDialog(response.getMessage()) ;
            }    
            
        return isValidationSuccess;
    }
    
    
    public boolean validateUserSSL() {
        boolean isValidationSuccess=true;
        
            String connectTo = CONNECTION_URL+"/loginServlet";
            
            //LoginBean loginBean = new LoginBean(usr,password);
            
            RequesterBean request = new RequesterBean();
            request.setId("SSL") ;
            //request.setDataObject(loginBean);
            System.out.println("*** start communicating with the server *** " ) ;
            AppletServletCommunicator comm = 
                    new AppletServletCommunicator(connectTo, request);
            comm.send();
            ResponderBean response = comm.getResponse();
            System.out.println("*** got response from server *** " ) ;
            if (response == null) { 
                response = new ResponderBean();
                response.setResponseStatus(false);
               /* CASE #304 begin */
                //response.setMessage(coeusMessageResources.parseMessageKey(
                                                //"server_exceptionCode.1000"));                
                String msg = coeusMessageResources.parseMessageKey(
                                            "coeusApplet_exceptionCode.1147");  
                errorMsg = msg;
                response.setMessage(msg);
                /* CASE #304 end */
            }
            
            isValidationSuccess = response.isSuccessfulResponse();
            System.out.println("*** obtained status " +  isValidationSuccess  + " *** " ) ;
            
            if (isValidationSuccess){
                sslUserId = response.getDataObject().toString() ;
                userInfoBean = (UserInfoBean)((Vector)response.getDataObjects()).get(0);
                globalImageName = (String )((Vector)response.getDataObjects()).get(1);
                // 2930: Auto-delete Current Locks based on new parameter 
//                CoeusGuiConstants.POLLING_INTERVAL = Integer.parseInt(((Vector)response.getDataObjects()).get(2).toString());
            }    
            else{
                sslUserId = null ;
                CoeusOptionPane.showErrorDialog(response.getMessage()) ;
            }    
            
        return isValidationSuccess;
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
                if(validateUser(txtUserName.getText(), password.getPassword())){
                    CoeusAppletMDIForm mdiform = new CoeusAppletMDIForm();
                    // validation success
                    if (parentFrame != null)
                    {
                        mdiform.setParentFrame(parentFrame) ; 
                    }    
                    else
                    {
                         mdiform.setParentFrame(null) ;
                    }    
                     
                    ok.setEnabled(false);
                    reset.setEnabled(false);
                    txtUserName.setEditable(false);
                    password.setEnabled(false);
                    if(firstLogin) {
                         showWelcomeForm(mdiform);
                     }
                    mdiform.setAppletCodeBase(getAppletCodeBase());
                    /* Use this code if running the applet from within the IDE.
                    mdiform.setCoeusAppletContext(null);    //getAppletContext() );
                    */
                    mdiform.setCoeusAppletContext(getAppletnJWSContext() );
                    mdiform.setRoleID(0);
                    
                    //Added by sharath to store user information in MDI Form
                    mdiform.setUserInfo((UserInfoBean)((Vector)response.getDataObjects()).get(0));
                    
                    mdiform.setUserName(txtUserName.getText());
                    mdiform.setCoeusGlobalImageName(globalImageName);
                    //Bug fix Case #2137 start 3
                    UserInfoBean userInfoBean = (UserInfoBean)((Vector)response.getDataObjects()).get(0);
                    if(userInfoBean != null && userInfoBean.getStatus() == 'I'){
                        CoeusOptionPane.showInfoDialog("'"+userInfoBean.getUserId()+"' is Inactive.") ;
                        return;
                    } 
                    //Bug fix Case #2137 end 3
                    mdiform.showForm();
                    // Bug Fix #1135 - 17-Aug-2004
                     try{
                        UIManager.setLookAndFeel(UIManager.getLookAndFeel());
                        javax.swing.SwingUtilities.updateComponentTreeUI(mdiform);
                    }catch (UnsupportedLookAndFeelException ex){
                        ex.printStackTrace();
                    }
                    // Bug Fix #1135 - 17-Aug-2004
                    resetFields();
                    if (parentFrame != null)
                    {    
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
        } else {
            if (parentFrame == null)
            {
                resetFields();
            }
            else
            {
                System.exit(0) ; // cancel on JWS shud exit the application
            }    
        }
    }
    
    
     private void showWelcomeForm(CoeusAppletMDIForm mdiform) {
        
        WelcomeForm welcomeForm = new WelcomeForm(mdiform, new String(password.getPassword()),(UserInfoBean)((Vector)response.getDataObjects()).get(0));
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
        int roleID=0;
        return roleID;
    }
    /** The method used to validate the username and password. It connects to the servlet and
     * do the user authentication by communicating with database and return the 
     * status. Any failure in authentication will return back to applet with error message.
     *
     * @param pass Array of characters
     * @param usr String Username
     * @return  boolean true if the username and password are valid user else False 
     */
    public boolean validateUser(String usr, char[] pass) {
        boolean isValidationSuccess=false;
        /* CASE #304 Display error message for blank username or password. */
       // if (usr.trim().equals("") || pass.length <0){
           // isValidationSuccess=false;
        if ( usr.trim().equals("") ){
            log(coeusMessageResources.parseMessageKey("coeusApplet_exceptionCode.1163"));
            errorMsgDisplayed = true;
            return isValidationSuccess;
        }
        else if ( pass.length <1 ){
            log(coeusMessageResources.parseMessageKey("coeusApplet_exceptionCode.1164"));
            errorMsgDisplayed = true;
            return isValidationSuccess;
        }else { 
            String connectTo = CONNECTION_URL+"/loginServlet";
            System.out.println("CONNECTION_URL IS " + CONNECTION_URL) ;
            // get the password string
            String password = "";
            for(int i=0;i<pass.length;i++){
                password = password+pass[i];
            }

            LoginBean loginBean = new LoginBean(usr,password);
            RequesterBean request = new RequesterBean();
            request.setId("USERID") ;
            request.setDataObject(loginBean);
            AppletServletCommunicator comm = new AppletServletCommunicator(connectTo, request);
            comm.send();
            response = comm.getResponse();
            
            if (response == null) {                
                response = new ResponderBean();
                response.setResponseStatus(false);
                /*Case #304 begin */
                //response.setMessage(coeusMessageResources.parseMessageKey(
                                               // "server_exceptionCode.1000"));
                String msg = coeusMessageResources.parseMessageKey(
                                        "coeusApplet_exceptionCode.1147");
                response.setMessage(msg);
                errorMsg = msg;  
                /*CASE #304 end */
            }   
            else if(response != null){
                /* CASE #304 begin */
                /* If validation failed, and an exception has been set in the
                 * response bean, get the exception message. */
                if(!response.isSuccessfulResponse()){
                    if(response.getException() != null){
                        errorMsg = response.getException().getMessage();
                    }
                }else{
                    isValidationSuccess = false;
                    userInfoBean = (UserInfoBean)((Vector)response.getDataObjects()).get(0);
                    globalImageName = (String )((Vector)response.getDataObjects()).get(1);
                    // 2930: Auto-delete Current Locks based on new parameter 
//                    CoeusGuiConstants.POLLING_INTERVAL = Integer.parseInt(((Vector)response.getDataObjects()).get(2).toString());
                    firstLogin = ((Boolean)((Vector)response.getDataObjects()).get(3)).booleanValue();
                    isValidationSuccess = response.isSuccessfulResponse();        
                }
                /*CASE #304 end */
            }
        }
        return isValidationSuccess;
    }

    /**
     * Displays the message,it gives the error message.
     * @param mesg String
     */
    public void log(String mesg) {
//        CoeusOptionPane.showWarningDialog(mesg);        
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
          try
            {
                hostURL = getCodeBase() ; 
            }
            catch(Exception ex)
            {
              //ex.printStackTrace();
              try
               {
                javax.jnlp.BasicService bs 
                    = (javax.jnlp.BasicService)javax.jnlp.ServiceManager.lookup("javax.jnlp.BasicService");
                hostURL = bs.getCodeBase() ;
               }
               catch(Exception jnlpex)
               {
                //jnlpex.printStackTrace() ;
               }
            }
            return hostURL;
        //prps end - 1 oct 2003
    }
    
    
    private AppletContext getAppletnJWSContext()
    {
        try
        {
         if (getAppletContext()==null)
             return null ;
         else
             return getAppletContext() ;
        }
        catch(Exception ex)
        {
           // ex.printStackTrace() ;
        }
        return null ;
    }
   
    
    
    
    
    
    
}
