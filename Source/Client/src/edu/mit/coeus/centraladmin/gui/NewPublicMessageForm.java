/*
 * NewPublicMessage.java
 *
 * Created on December 15, 2004, 4:35 PM
 */

package edu.mit.coeus.centraladmin.gui;

import javax.swing.JFrame;
import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.gui.CoeusAppletMDIForm;
import edu.mit.coeus.gui.CoeusDlgWindow;
import edu.mit.coeus.utils.ScreenFocusTraversalPolicy;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.utils.AppletServletCommunicator;
import edu.mit.coeus.exception.*;
import edu.mit.coeus.utils.query.*;
import edu.mit.coeus.brokers.*;
import edu.mit.coeus.utils.LimitedPlainDocument;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.*;
import javax.swing.*;
import edu.mit.coeus.utils.TypeConstants;

/**
 *
 * @author  shijiv
 */
public class NewPublicMessageForm extends javax.swing.JPanel implements ActionListener {
    
    private static final int WIDTH = 500;
    private static final int HEIGHT = 318;
    private static final String WINDOW_TITLE = "New Public Message";
    private static final char SAVE_NEW_MESSAGE='A';
    private final String BUDGET_SERVLET ="/CentralAdminMaintenanceServlet";
    private final String connectTo = CoeusGuiConstants.CONNECTION_URL+ BUDGET_SERVLET;
    
    private CoeusAppletMDIForm mdiForm;
    private CoeusDlgWindow dlgNewPublicMessage;
    private CoeusMessageResources coeusMessageResources;
    
    /** Creates new form NewPublicMessage */
    public NewPublicMessageForm(CoeusAppletMDIForm mdiForm) {
        this.mdiForm=mdiForm;
        coeusMessageResources = CoeusMessageResources.getInstance();
        initComponents();
        txtArMessage.setLineWrap(true);
        txtArMessage.setWrapStyleWord(true);
        postInitComponents();
        registerComponents();
        
    }
    
     private void postInitComponents() {
        dlgNewPublicMessage = new CoeusDlgWindow(mdiForm);
        dlgNewPublicMessage.setResizable(false);
        dlgNewPublicMessage.setModal(true);
        dlgNewPublicMessage.getContentPane().add(this);
        dlgNewPublicMessage.setFont(CoeusFontFactory.getLabelFont());
        dlgNewPublicMessage.setDefaultCloseOperation(CoeusDlgWindow.DO_NOTHING_ON_CLOSE);
        dlgNewPublicMessage.setSize(WIDTH, HEIGHT);
        dlgNewPublicMessage.setTitle(WINDOW_TITLE); 
        
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension dlgSize = dlgNewPublicMessage.getSize();
        dlgNewPublicMessage.setLocation(screenSize.width/2 - (dlgSize.width/2),
        screenSize.height/2 - (dlgSize.height/2));
        
        dlgNewPublicMessage.addWindowListener(new WindowAdapter(){
            
            public void windowClosing(WindowEvent we){
                performCancelAction();
                return;
            }
        });
        
        dlgNewPublicMessage.addEscapeKeyListener(
        new AbstractAction("escPressed"){
            public void actionPerformed(ActionEvent ae){
                performCancelAction();
                return;
            }
        });
        
        dlgNewPublicMessage.addComponentListener(
        new ComponentAdapter(){
            public void componentShown(ComponentEvent e){
                setWindowFocus();
            }
        });
     }
    
     public void registerComponents() {
         java.awt.Component[] components = {btnCancel,btnOK,txtArMessage};
         ScreenFocusTraversalPolicy traversePolicy = new ScreenFocusTraversalPolicy( components );
         this.setFocusTraversalPolicy(traversePolicy);
         this.setFocusCycleRoot(true);
         
         txtArMessage.setDocument(new LimitedPlainDocument(2000));
         btnCancel.addActionListener(this);
         btnOK.addActionListener(this);
     }
     
     public void actionPerformed(ActionEvent actionEvent) {
         Object source=actionEvent.getSource();
        if(source.equals(btnCancel)) {
             performCancelAction();
         }else if(source.equals(btnOK)) {
             performOkAction();
         }
     }
     
     public void performCancelAction() {
       dlgNewPublicMessage.setVisible(false);  
     }
     
     public void performOkAction() {
         String message = txtArMessage.getText().trim();
         if(message == null || message.equals("")) {
             CoeusOptionPane.showErrorDialog(coeusMessageResources.parseMessageKey("newPublicMessageExceptionCode.1751"));
         }else {
             try {
             saveMessage(message);
             }catch(CoeusClientException coeusClientException) {
                 CoeusOptionPane.showDialog(coeusClientException);
             }
         }
     }
     
     public void saveMessage(String message) throws CoeusClientException{
        RequesterBean requesterBean = new RequesterBean();
        ResponderBean responderBean = new ResponderBean();
         
        
        requesterBean.setFunctionType(SAVE_NEW_MESSAGE);
        //customElemInfoBean.setAcType(TypeConstants.DELETE_RECORD);
        requesterBean.setDataObject(message);
        AppletServletCommunicator comm
        = new AppletServletCommunicator(connectTo, requesterBean);
        
        comm.send();
        responderBean = comm.getResponse();
        
        if(responderBean!= null){
            if(responderBean.isSuccessfulResponse()){
                dlgNewPublicMessage.setVisible(false);
            }else{
                throw new CoeusClientException(responderBean.getMessage(),CoeusClientException.ERROR_MESSAGE);
            }
        }
     }
     
     public void setWindowFocus() {
         txtArMessage.requestFocusInWindow();
     }
     
     public void display() {
         dlgNewPublicMessage.setVisible(true);
     }
     
     /** This method is called from within the constructor to
      * initialize the form.
      * WARNING: Do NOT modify this code. The content of this method is
      * always regenerated by the Form Editor.
      */
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        lblCreate = new javax.swing.JLabel();
        lblmessage = new javax.swing.JLabel();
        btnCancel = new javax.swing.JButton();
        btnOK = new javax.swing.JButton();
        scrPnPublicMessage = new javax.swing.JScrollPane();
        txtArMessage = new javax.swing.JTextArea();

        setLayout(new java.awt.GridBagLayout());

        setMinimumSize(new java.awt.Dimension(382, 314));
        setPreferredSize(new java.awt.Dimension(500, 318));
        lblCreate.setFont(new java.awt.Font("Microsoft Sans Serif", 0, 11));
        lblCreate.setIcon(new javax.swing.ImageIcon("F:\\Tomcat 4.1\\webapps\\CoeusApplet\\images\\blackIcon.gif"));
        lblCreate.setText("Create a new public Message");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(2, 3, 2, 0);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        add(lblCreate, gridBagConstraints);

        lblmessage.setFont(new java.awt.Font("Microsoft Sans Serif", 0, 11));
        lblmessage.setIcon(new javax.swing.ImageIcon("F:\\Tomcat 4.1\\webapps\\CoeusApplet\\images\\blackIcon.gif"));
        lblmessage.setText("This message will be displayed when Coeus users login to the application next time.");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(2, 3, 0, 0);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        add(lblmessage, gridBagConstraints);

        btnCancel.setFont(CoeusFontFactory.getLabelFont()
        );
        btnCancel.setMnemonic('C');
        btnCancel.setText("Cancel");
        btnCancel.setMinimumSize(new java.awt.Dimension(73, 25));
        btnCancel.setPreferredSize(new java.awt.Dimension(73, 25));
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.weightx = 1.0;
        add(btnCancel, gridBagConstraints);

        btnOK.setFont(CoeusFontFactory.getLabelFont());
        btnOK.setMnemonic('O');
        btnOK.setText("OK");
        btnOK.setMinimumSize(new java.awt.Dimension(73, 25));
        btnOK.setPreferredSize(new java.awt.Dimension(73, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        add(btnOK, gridBagConstraints);

        scrPnPublicMessage.setBorder(new javax.swing.border.EtchedBorder());
        scrPnPublicMessage.setMinimumSize(new java.awt.Dimension(410, 230));
        scrPnPublicMessage.setPreferredSize(new java.awt.Dimension(410, 240));
        txtArMessage.setFont(CoeusFontFactory.getNormalFont());
        scrPnPublicMessage.setViewportView(txtArMessage);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 2, 0);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHWEST;
        gridBagConstraints.weighty = 1.0;
        add(scrPnPublicMessage, gridBagConstraints);

    }//GEN-END:initComponents

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnCancelActionPerformed
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JButton btnCancel;
    public javax.swing.JButton btnOK;
    public javax.swing.JLabel lblCreate;
    public javax.swing.JLabel lblmessage;
    public javax.swing.JScrollPane scrPnPublicMessage;
    public javax.swing.JTextArea txtArMessage;
    // End of variables declaration//GEN-END:variables
   
    
   /* public static void main(String args[]) {
        NewPublicMessageForm newPublicMessageForm =new NewPublicMessageForm();
        JFrame frame= new JFrame();
        frame.getContentPane().add(newPublicMessageForm);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 318);
        frame.show();
    }*/
}
