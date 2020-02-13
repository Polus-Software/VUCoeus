/*
 * @(#)CoeusAboutForm.java 1.0 12/2/02 12:24 PM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.gui;

import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import java.awt.*;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.utils.*;
import java.awt.event.*;

/**
 * This class provides the GUI for the CoeusAboutForm in help menu.
 *
 * @version 1.0 December 2, 2002, 12:24 PM
 * @author  Mukundan C
 * modified Sagin on Dec-03-02
 */

public class CoeusAboutForm extends CoeusDlgWindow {

    private CoeusMessageResources coeusMessageResources;
    private static final String GET_SERVLET = "/UtilityServlet";

    /**
     * Creates a new CoeusAboutForm with the given parent frame and title
     *
     * @param parent reference to the parent Frame component.
     * @param title String representing the title to be displayed.
     * @param modal boolean value which specifies whether the dialog is modal window
     * or not.
     */
    public CoeusAboutForm(Frame parent, String title, boolean modal) {
        super(parent, title,modal);
        initComponents();
        btnOK.setMnemonic('O');
        setValues();
        getVersionTitle();
        this.setResizable(false);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension dlgSize = this.getSize();
        this.setLocation(screenSize.width/2 - (dlgSize.width/2),
        screenSize.height/2 - (dlgSize.height/2));

        // prps start - dec 8 2003
        lblImage.addMouseListener( new MouseAdapter(){
            public void mouseClicked( MouseEvent me ) {
                if( me.getClickCount() == 1 ) {
                    try{
                        CoeusApplicationDetails coeusApplicationDetails
                                    = new CoeusApplicationDetails() ;
                        coeusApplicationDetails.showForm() ;
                    }catch( Exception e) {
                        e.printStackTrace();
                        CoeusOptionPane.showInfoDialog( e.getMessage() );
                    }
                }
            }
        });

        // prps end - dec 8 2003

         btnOK.requestFocusInWindow();//By raghuSV: to set focus
        //
    }
    
     // Enhancement 2019 - Get the Coeus Version
    private void getVersionTitle(){
        String title = getProductionVersion();
        lblVersion.setText("Version "+title);
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

    /**
     * this method is called to set the messages to the lables and textarea
     * from the resources properties files
     */
    private void setValues(){
        coeusMessageResources = CoeusMessageResources.getInstance();
        lblHead.setText(coeusMessageResources.parseMessageKey(
                                                    "aboutFrm_copyRight_headCode.001"));
        
//        lblVersion.setText(coeusMessageResources.parseMessageKey(
//                                                 "aboutFrm_copyRight_versionCode.002"));
        
        
        lblAdd1.setText(coeusMessageResources.parseMessageKey(
                                                 "aboutFrm_copyRight_add1Code.003"));
        lblAdd2.setText(coeusMessageResources.parseMessageKey(
                                                 "aboutFrm_copyRight_add2Code.004"));
        lblAdd3.setText(coeusMessageResources.parseMessageKey(
                                                 "aboutFrm_copyRight_add3Code.005"));
        String message = coeusMessageResources.parseMessageKey(
                                                 "aboutFrm_copyRight_mesg1Code.006")+
                                                 "\n    "+
                         coeusMessageResources.parseMessageKey(
                                                 "aboutFrm_copyRight_mesg2Code.007");
        txtAreaMesg1.setText(message);
        txtAreaMesg1.setCaretPosition(0);
        //getRootPane().setDefaultButton(btnOK);

    }
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jLabel1 = new javax.swing.JLabel();
        pnlMain = new javax.swing.JPanel();
        lblImage = new javax.swing.JLabel();
        lblHead = new javax.swing.JLabel();
        btnOK = new javax.swing.JButton();
        lblVersion = new javax.swing.JLabel();
        lblAdd1 = new javax.swing.JLabel();
        lblAdd2 = new javax.swing.JLabel();
        lblAdd3 = new javax.swing.JLabel();
        lblBlank = new javax.swing.JLabel();
        sptDivider = new javax.swing.JSeparator();
        txtAreaMesg1 = new javax.swing.JTextArea();
        scrPnMsg = new javax.swing.JScrollPane();

        btnLicense = new javax.swing.JButton();
        jLabel1.setText("jLabel1");

        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                closeDialog(evt);
            }
        });

        pnlMain.setLayout(new java.awt.GridBagLayout());

        lblImage.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/coeus_abt.gif")));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 10;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 0, 0);
        pnlMain.add(lblImage, gridBagConstraints);

        lblHead.setText("Coeus\u00ae");
        lblHead.setFont(CoeusFontFactory.getLabelFont());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 0, 0);
        pnlMain.add(lblHead, gridBagConstraints);

        btnOK.setFont(CoeusFontFactory.getLabelFont());
        btnOK.setText("OK");
        btnOK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOKActionPerformed(evt);
            }
        });

        btnLicense.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLicenseActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.insets = new java.awt.Insets(10, 0, 0, 0);
        pnlMain.add(btnOK, gridBagConstraints);

        lblVersion.setText("Version 3.8");
        lblVersion.setFont(CoeusFontFactory.getNormalFont());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 0, 0);
        pnlMain.add(lblVersion, gridBagConstraints);

        lblAdd1.setText("   Copyright© 1996 - 2004, 2002 - 2005 Massachusetts Institute of Technology\n   ");
        lblAdd1.setFont(CoeusFontFactory.getLabelFont());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 0, 10);
        pnlMain.add(lblAdd1, gridBagConstraints);

        lblAdd2.setText("Cambridge, Massachusetts");
        lblAdd2.setFont(CoeusFontFactory.getLabelFont());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 0, 0);
        pnlMain.add(lblAdd2, gridBagConstraints);

        lblAdd3.setText("All Rights Reserved.");
        lblAdd3.setFont(CoeusFontFactory.getLabelFont());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 0, 0);
        pnlMain.add(lblAdd3, gridBagConstraints);

        btnLicense.setText("License Terms");
        btnLicense.setFont(CoeusFontFactory.getLabelFont());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 0, 0);
        pnlMain.add(btnLicense, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.ipadx = 20;
        gridBagConstraints.insets = new java.awt.Insets(10, 0, 0, 0);
        pnlMain.add(lblBlank, gridBagConstraints);

        sptDivider.setForeground(new java.awt.Color(0, 0, 0));
        sptDivider.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 2));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(10, 0, 0, 0);
        pnlMain.add(sptDivider, gridBagConstraints);

        scrPnMsg.setMaximumSize(new java.awt.Dimension(200, 100));
        scrPnMsg.setMinimumSize(new java.awt.Dimension(200, 100));
        scrPnMsg.setPreferredSize(new java.awt.Dimension(200, 100));
        
        txtAreaMesg1.setWrapStyleWord(true);
        txtAreaMesg1.setLineWrap(true);
        txtAreaMesg1.setEditable(false);
        txtAreaMesg1.setAutoscrolls(true);
        txtAreaMesg1.setFont(CoeusFontFactory.getNormalFont());
        txtAreaMesg1.setRows(4);
        txtAreaMesg1.setText("The software code provided to Coeus\u00ae licensees is a copyrighted work of the  Massachusetts Institute of Technolog. \n");
//        txtAreaMesg1.setPreferredSize(new java.awt.Dimension(500, 40));
        txtAreaMesg1.setOpaque(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.insets = new java.awt.Insets(10, 0, 0, 0);
//        pnlMain.add(txtAreaMesg1, gridBagConstraints);
        scrPnMsg.setViewportView(txtAreaMesg1);
        pnlMain.add(scrPnMsg, gridBagConstraints);

        getContentPane().add(pnlMain, java.awt.BorderLayout.NORTH);

        pack();
    }

    private void btnOKActionPerformed(java.awt.event.ActionEvent evt) {
        // Add your handling code here:
        this.dispose();
    }

    private void btnLicenseActionPerformed(java.awt.event.ActionEvent evt) {
        try{
            LicenseTextForm licenseForm = new LicenseTextForm();
            licenseForm.display();
        }catch(Exception ex){
            CoeusOptionPane.showErrorDialog(ex.getMessage());
        }
    }

    /** Closes the dialog */
    private void closeDialog(java.awt.event.WindowEvent evt) {
        setVisible(false);
        dispose();
    }

    // Variables declaration - do not modify
    private javax.swing.JButton btnOK;
    private javax.swing.JLabel lblImage;
    private javax.swing.JTextArea txtAreaMesg1;
    private javax.swing.JPanel pnlMain;
    private javax.swing.JLabel lblBlank;
    private javax.swing.JLabel lblHead;
    private javax.swing.JLabel lblAdd3;
    private javax.swing.JLabel lblAdd2;
    private javax.swing.JLabel lblAdd1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel lblVersion;
    private javax.swing.JSeparator sptDivider;
    private javax.swing.JButton btnLicense;
    private javax.swing.JScrollPane scrPnMsg;
    // End of variables declaration

    public static void main(String args[]){
        CoeusAboutForm form = new CoeusAboutForm(null,"test",true);
        form.setVisible(true);
    }
}
