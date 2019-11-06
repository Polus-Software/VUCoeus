/*
 * CoeusApplicationDetails.java
 *
 * Created on December 8, 2003, 12:49 PM
 */

package edu.mit.coeus.gui;

import edu.mit.coeus.brokers.*;
import edu.mit.coeus.utils.*;
import edu.mit.coeus.utils.CoeusGuiConstants;
import java.awt.* ;
import java.awt.event.ActionListener;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.HashMap;


public class CoeusApplicationDetails extends edu.mit.coeus.gui.CoeusDlgWindow 
                                           implements ActionListener 
{
    
    /** Creates new form CoeusApplicationDetails */
    public CoeusApplicationDetails() {
        super(CoeusGuiConstants.getMDIForm(), true );
        setTitle("Application Details") ;
        setSize(300, 100) ;
       
    }
    
   public void showForm() 
   {
        initComponents();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension dlgSize = getSize();
        setLocation(screenSize.width/2 - (dlgSize.width/2),
        screenSize.height/2 - (dlgSize.height/2));
        setValues() ;
        show();
        btnOk.requestFocusInWindow();
   } 
    
   private void setValues() 
   {
       txtCodeBase.setText(CoeusGuiConstants.CONNECTION_URL) ; 
       CoeusAppletMDIForm mdiForm = CoeusGuiConstants.getMDIForm() ;
       txtUser.setText(mdiForm.getUserName()) ;
       
       
        RequesterBean request = new RequesterBean();
        String connURL = CoeusGuiConstants.CONNECTION_URL +
                                                     "/loginServlet";
        request.setId("APP_DETAILS"); 
        request.setDataObject(request);

        AppletServletCommunicator comm = new AppletServletCommunicator(
                                                    connURL, request );
        comm.send();
        ResponderBean response = comm.getResponse();
        if ( response.isSuccessfulResponse() )
        {
            HashMap hashDetails = (HashMap)response.getDataObject() ;
            
            txtDataSource.setText(hashDetails.get("DataSource").toString()) ;
            String strTemp = hashDetails.get("BuildTimestamp").toString() ;
            strTemp = strTemp.substring(strTemp.indexOf('T') + 1, strTemp.length()) ;
            txtBuildDate.setText(hashDetails.get("BuildDate").toString() 
                                + " " + strTemp) ;
            
        }
            
   }
   
   
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        pnlMain = new javax.swing.JPanel();
        lblUser = new javax.swing.JLabel();
        txtUser = new javax.swing.JLabel();
        lblCodeBase = new javax.swing.JLabel();
        txtCodeBase = new javax.swing.JLabel();
        lblDataSource = new javax.swing.JLabel();
        txtBuildDate = new javax.swing.JLabel();
        lblBuildDate = new javax.swing.JLabel();
        btnOk = new javax.swing.JButton();
        txtDataSource = new javax.swing.JLabel();

        getContentPane().setLayout(new java.awt.GridBagLayout());

        pnlMain.setLayout(new java.awt.GridBagLayout());

        pnlMain.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.RAISED));
        pnlMain.setMinimumSize(new java.awt.Dimension(300, 100));
        pnlMain.setPreferredSize(new java.awt.Dimension(300, 100));
        lblUser.setFont(CoeusFontFactory.getLabelFont());
        lblUser.setText("Logged In User :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 3, 3);
        pnlMain.add(lblUser, gridBagConstraints);

        txtUser.setFont(CoeusFontFactory.getLabelFont());
        txtUser.setText("jLabel1");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 97;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 0, 0);
        pnlMain.add(txtUser, gridBagConstraints);

        lblCodeBase.setFont(CoeusFontFactory.getLabelFont());
        lblCodeBase.setText("CodeBase :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 3, 3);
        pnlMain.add(lblCodeBase, gridBagConstraints);

        txtCodeBase.setFont(CoeusFontFactory.getLabelFont());
        txtCodeBase.setText("jLabel1");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 120;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 0, 0);
        pnlMain.add(txtCodeBase, gridBagConstraints);

        lblDataSource.setFont(CoeusFontFactory.getLabelFont());
        lblDataSource.setText("Data Source :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 3, 3);
        pnlMain.add(lblDataSource, gridBagConstraints);

        txtBuildDate.setFont(CoeusFontFactory.getLabelFont());
        txtBuildDate.setText("jLable1");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 0, 0);
        pnlMain.add(txtBuildDate, gridBagConstraints);

        lblBuildDate.setFont(CoeusFontFactory.getLabelFont());
        lblBuildDate.setText("Build Date :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 3, 3);
        pnlMain.add(lblBuildDate, gridBagConstraints);

        btnOk.setFont(CoeusFontFactory.getLabelFont());
        btnOk.setText("OK");
        btnOk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOkClicked(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTH;
        gridBagConstraints.insets = new java.awt.Insets(74, 0, 0, 0);
        pnlMain.add(btnOk, gridBagConstraints);

        txtDataSource.setFont(CoeusFontFactory.getLabelFont());
        txtDataSource.setText("jLable1");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 0, 0);
        pnlMain.add(txtDataSource, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 200;
        gridBagConstraints.ipady = 100;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        getContentPane().add(pnlMain, gridBagConstraints);

        pack();
    }//GEN-END:initComponents

    private void btnOkClicked(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOkClicked
         this.dispose();
    }//GEN-LAST:event_btnOkClicked
    
    /** Exit the Application */
    public void actionPerformed(java.awt.event.ActionEvent e) {
    }    
    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnOk;
    private javax.swing.JLabel lblBuildDate;
    private javax.swing.JLabel lblCodeBase;
    private javax.swing.JLabel lblDataSource;
    private javax.swing.JLabel lblUser;
    private javax.swing.JPanel pnlMain;
    private javax.swing.JLabel txtBuildDate;
    private javax.swing.JLabel txtCodeBase;
    private javax.swing.JLabel txtDataSource;
    private javax.swing.JLabel txtUser;
    // End of variables declaration//GEN-END:variables
    
}
