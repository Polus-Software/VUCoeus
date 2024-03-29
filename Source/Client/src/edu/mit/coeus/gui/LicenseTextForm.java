/*
 * LicenseTextForm.java
 *
 * Created on March 4, 2005, 12:12 PM
 */

package edu.mit.coeus.gui;

import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.utils.AppletServletCommunicator;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.utils.CoeusOptionPane;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.AbstractAction;
//import sun.security.krb5.internal.crypto.e;

/**
 *
 * @author  geot
 */
public class LicenseTextForm extends javax.swing.JComponent {
    private CoeusDlgWindow dlgSubDetailForm;
    private static final String GET_SERVLET = "/UtilityServlet";
    /** Creates new form LicenseTextForm */
    public LicenseTextForm() throws Exception{
        initComponents();
        btnOk.setFont(CoeusFontFactory.getLabelFont());
        btnOk.setMnemonic('O');
        setData();
    }
    public void setData() throws Exception{
        RequesterBean request = new RequesterBean();
        String connURL = CoeusGuiConstants.CONNECTION_URL +
                                                     "/coeusFunctionsServlet";
        request.setDataObject("GET_LICENSE_TEXT"); 
        AppletServletCommunicator comm = new AppletServletCommunicator(
                                                    connURL, request );
        comm.send();
        ResponderBean response = comm.getResponse();
        if ( response.hasResponse()){
            String licenseText = (String)response.getDataObject() ;
            txtLicense.setText(licenseText);
        }        
        this.txtLicense.setCaretPosition(0);
    }
    public void display(){
        
        dlgSubDetailForm = new CoeusDlgWindow(CoeusGuiConstants.getMDIForm());
        dlgSubDetailForm.getContentPane().add(this);
        dlgSubDetailForm.setTitle("Coeus "+getProductionVersion() +" License Terms");
        dlgSubDetailForm.setFont(CoeusFontFactory.getLabelFont());
        dlgSubDetailForm.setModal(true);
        dlgSubDetailForm.setResizable(false);
//        dlgOppSelForm.setSize(WIDTH,HEIGHT);
        dlgSubDetailForm.pack();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension dlgSize = dlgSubDetailForm.getSize();
        dlgSubDetailForm.setLocation(screenSize.width/2 - (dlgSize.width/2),
        screenSize.height/2 - (dlgSize.height/2));
        
        dlgSubDetailForm.addEscapeKeyListener(
        new AbstractAction("escPressed"){
            public void actionPerformed(ActionEvent ae){
                closeAction();
                return;
            }
        });
        dlgSubDetailForm.setDefaultCloseOperation(CoeusDlgWindow.DO_NOTHING_ON_CLOSE);
        dlgSubDetailForm.addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent we){
                closeAction();
                return;
            }
        });
        
        dlgSubDetailForm.addComponentListener(
        new ComponentAdapter(){
            public void componentShown(ComponentEvent e){
                btnOk.setRequestFocusEnabled(true);
            }
        });
        dlgSubDetailForm.setVisible(true);
    }
    private void closeAction(){
        dlgSubDetailForm.dispose();
    }
    
     
    
    /*Enhancement 2019 
     *Communicate with the server to read the coeus version number from the 
     *coeus parameters
     */
    private String getProductionVersion(){
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

    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jPanel2 = new javax.swing.JPanel();
        txtLicense = new javax.swing.JTextPane();
        btnOk = new javax.swing.JButton();

        setLayout(new java.awt.BorderLayout());

        jPanel1.setLayout(new java.awt.GridBagLayout());

        jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        jScrollPane1.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));
        jScrollPane1.setMinimumSize(new java.awt.Dimension(500, 400));
        jScrollPane1.setPreferredSize(new java.awt.Dimension(500, 400));
        jPanel2.setLayout(new java.awt.BorderLayout());

        txtLicense.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 153)));
        txtLicense.setEditable(false);
        jPanel2.add(txtLicense, java.awt.BorderLayout.CENTER);

        jScrollPane1.setViewportView(jPanel2);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 0, 0);
        jPanel1.add(jScrollPane1, gridBagConstraints);

        btnOk.setText("OK");
        btnOk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOkActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(20, 4, 0, 3);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        jPanel1.add(btnOk, gridBagConstraints);

        add(jPanel1, java.awt.BorderLayout.CENTER);

    }//GEN-END:initComponents

    private void btnOkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOkActionPerformed
        // TODO add your handling code here:
        closeAction();
    }//GEN-LAST:event_btnOkActionPerformed
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnOk;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextPane txtLicense;
    // End of variables declaration//GEN-END:variables
    
}
