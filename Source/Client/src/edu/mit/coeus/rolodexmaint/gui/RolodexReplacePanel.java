/*
 * @(#)RolodexReplacePanel.java
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 */

package edu.mit.coeus.rolodexmaint.gui;

import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.gui.CoeusDlgWindow;
import edu.mit.coeus.gui.CoeusAppletMDIForm;
import edu.mit.coeus.rolodexmaint.bean.RolodexDetailsBean;
import edu.mit.coeus.rolodexmaint.bean.RolodexReferencesBean;
import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.utils.AppletServletCommunicator;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.utils.ScreenFocusTraversalPolicy;
import edu.mit.coeus.gui.CoeusMessageResources;


import java.awt.ActiveEvent.*;
import java.awt.event.*;
import java.awt.Cursor;
import java.awt.Component;
import javax.swing.event.*;
import javax.swing.JPanel;
import javax.swing.AbstractAction;


/** 
 * This class is used to handle rolodex replacing.
 * This displays the two rolodex details and asks the user whether he wants to replace
 * the current with the new rolodex. If user selects yes the rolodex references 
 * for that particular table is replaced in the database with the new one.
 * It contains
 * 1.  a panel for current rolodex details. 
 *     It is a object of RolodexMaintenanceDetailForm.
 *     It gives a non editable panel for rolodex details. This is added to a scroll pane.
 * 2.  a panel for new rolodex details. 
 *     It is a object of RolodexMaintenanceDetailForm.
 *     It gives a non editable panel for rolodex details. This is added to a scroll pane.
 * 3.  Buttons Yes and No. 
 * Created on March 18, 2004, 7:43 PM
 * @author  bijosh  
 */

public class RolodexReplacePanel extends javax.swing.JComponent 
                                 implements ActionListener{
    
     private CoeusDlgWindow dlgReplaceRolodex;
     private CoeusAppletMDIForm mdiForm;
     private static final String EMPTY_STRING = "";
     private String newRolodexId = EMPTY_STRING;
     private String currentRolodexId = EMPTY_STRING;
     private static final String WINDOW_TITLE="Replace Rolodex Entry";
     private RolodexDetailsBean currentRolodexDetailsBean,newRolodexDetailsBean;
     private RolodexMaintenanceDetailForm rolodexMaintenanceDetailForm,
                                        newRolodexMaintenanceDetailForm; 
     private JPanel currentRolodexPanel,newRolodexPanel;
     private final String ROLODEX_SERVLET = "/rolMntServlet"; 
     private String connectTo = CoeusGuiConstants.CONNECTION_URL + ROLODEX_SERVLET;
     private static final char GET_DATA_FN_TYPE='V';
     private static final char UPDATE_DATA_FN_TYPE='G';
     private String labelString1="Are you sure you want to replace all occurances of Id ";
     private String labelString2=" in the table ";
     private String labelString3=" with rolodex id ";
     private String selectedTable;
     private RolodexReferencesBean rolodexReferencesBean;
     private CoeusMessageResources coeusMessageResources;
     
     private boolean yesClicked;
     
     /** Creates new form RolodexReplacePanel
      * @param mdiForm Parent for the dialog
      * @param currentRolodexDetailsBean Details of the current rolodex
      */
    public RolodexReplacePanel (CoeusAppletMDIForm mdiForm,
                                RolodexDetailsBean currentRolodexDetailsBean) {
        this.mdiForm=mdiForm;
        this.currentRolodexDetailsBean=currentRolodexDetailsBean;
        currentRolodexId=currentRolodexDetailsBean.getRolodexId();
        initComponents();
        postInitComponents();
     }
    
    
    
   /** This method is called from within the constructor to
     * display the form.
     * The display attributres are set before displaying it.
     */
    private void postInitComponents() {
        coeusMessageResources = CoeusMessageResources.getInstance();
        rolodexMaintenanceDetailForm = new RolodexMaintenanceDetailForm(
                                    currentRolodexId,currentRolodexDetailsBean);
        currentRolodexPanel=rolodexMaintenanceDetailForm.getRolodexComponent();
        scrPnCurrentRolodex.setViewportView(currentRolodexPanel);
        btnYes.addActionListener(this);
        btnNo.addActionListener(this);
        yesClicked=false;
        dlgReplaceRolodex = new CoeusDlgWindow(mdiForm);
        dlgReplaceRolodex.setModal(true);
        dlgReplaceRolodex .getContentPane().add(this);
        dlgReplaceRolodex .setTitle(WINDOW_TITLE);
        dlgReplaceRolodex .setFont(CoeusFontFactory.getLabelFont());
        dlgReplaceRolodex .setModal(true);
        dlgReplaceRolodex.setResizable(false);
     }

    /** This is called from outside of the class.(From RolodexReferencesPanel class)
     * This assigns values and components for the new rolodex
     * @param newRolodexId Id for the new rolodex
     * @param rolodexReferencesBean Rolodex references details bean for the table selected
     */    
    public void initailizeNewRolodexComponents(String newRolodexId,
                                    RolodexReferencesBean rolodexReferencesBean)
    {
        this.newRolodexId = newRolodexId;
        this.rolodexReferencesBean=rolodexReferencesBean;
        selectedTable=rolodexReferencesBean.getTableName();
        newRolodexDetailsBean=getDataFromServer();
        newRolodexMaintenanceDetailForm = new RolodexMaintenanceDetailForm(
                                        newRolodexId,newRolodexDetailsBean);
        newRolodexPanel=newRolodexMaintenanceDetailForm.getRolodexComponent();
        scrPnNewRolodex.setViewportView(newRolodexPanel);
        lblConfirmation.setText(labelString1+currentRolodexId+labelString2);
        lblConfirmation1.setText(selectedTable+" "+labelString3+newRolodexId);
        dlgReplaceRolodex.addEscapeKeyListener(
        new AbstractAction("escPressed"){
        public void actionPerformed(ActionEvent ae){
           dlgReplaceRolodex.dispose();
          }
        });
        // To set the tab order
        Component[] comp = {btnNo,btnYes};
        ScreenFocusTraversalPolicy traversal = new ScreenFocusTraversalPolicy(comp);
        setFocusTraversalPolicy(traversal);
        setFocusCycleRoot(true);
        dlgReplaceRolodex.setDefaultCloseOperation(CoeusDlgWindow.DO_NOTHING_ON_CLOSE);
        dlgReplaceRolodex.addWindowListener(new WindowAdapter(){
            public void windowActivated(WindowEvent we){
                btnNo.requestFocusInWindow();
            }
            public void windowClosing(WindowEvent we){
               dlgReplaceRolodex.dispose();
            }
        });
       
    }
   
    /** Displays the dialog. Also sets the size and location for the dialog.
     * @return yesClicked. Returns the boolean value whether the rolodex 
     *  has been replaced or not
     */    
    public boolean display(){
        dlgReplaceRolodex.pack();
        dlgReplaceRolodex.setLocation(CoeusDlgWindow.CENTER);
        dlgReplaceRolodex.setVisible(true);
        return yesClicked;
    }
    /** Closes the window
     */
    private void closeWindow(){
       dlgReplaceRolodex.dispose();
    }

    /** Handler for buttons btnYes and btnNo
     * @param actionEvent actionEvent for the buttons
     */    
    public void actionPerformed(ActionEvent  actionEvent){
        Object source = actionEvent.getSource();
        if (source.equals(btnYes)) {
            int result=CoeusOptionPane.showQuestionDialog(
                        rolodexReferencesBean.getCount()+" "+
                        coeusMessageResources.parseMessageKey(
                        "roldexReplace_exceptionCode.1100"), 
                        CoeusOptionPane.DEFAULT_YES,CoeusOptionPane.SELECTION_NO);
            if (result==CoeusOptionPane.SELECTION_YES) {
                dlgReplaceRolodex.setCursor( new Cursor( Cursor.WAIT_CURSOR ) ); 
                updateRolodexDetails();
            } else {
                CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(
                        "roldexReplace_exceptionCode.1101")); 
                yesClicked = false;
            }
            dlgReplaceRolodex.dispose();
        }
        else if (source.equals(btnNo)) {
            dlgReplaceRolodex.dispose();
            yesClicked = false;
        }
    }
    /* Gets data from server for the new rolodex
     */
    private RolodexDetailsBean getDataFromServer()
    {
        RequesterBean request = new RequesterBean();
        request.setDataObject(newRolodexId);
        request.setFunctionType(GET_DATA_FN_TYPE);
        AppletServletCommunicator comm
           = new AppletServletCommunicator(connectTo, request);
        comm.send();
        ResponderBean response = comm.getResponse();
        if (response.isSuccessfulResponse()) {
                 RolodexDetailsBean rolodexDetailsBean  = 
                                (RolodexDetailsBean)response.getDataObject();
                 return rolodexDetailsBean;
            
        }else{
            CoeusOptionPane.showErrorDialog(response.getMessage());
            return null;
        }
   
    }
    /* For updating the table with the new rolodex entry
     */
    private void updateRolodexDetails(){
        RequesterBean request = new RequesterBean();
        rolodexReferencesBean.setNewValue(newRolodexId);
        request.setDataObject(rolodexReferencesBean); //bean
        request.setFunctionType(UPDATE_DATA_FN_TYPE);
        AppletServletCommunicator comm
           = new AppletServletCommunicator(connectTo, request);
        comm.send();
        ResponderBean response = comm.getResponse();
        if (!response.isSuccessfulResponse()) {
            CoeusOptionPane.showErrorDialog(response.getMessage());   
        } else {
            yesClicked = true; //Only set to true here. i.e only when sucessfully replaced
        }
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        scrPnCurrentRolodex = new javax.swing.JScrollPane();
        scrPnNewRolodex = new javax.swing.JScrollPane();
        btnYes = new javax.swing.JButton();
        btnNo = new javax.swing.JButton();
        lblConfirmation = new javax.swing.JLabel();
        lblConfirmation1 = new javax.swing.JLabel();

        setLayout(new java.awt.GridBagLayout());

        setMinimumSize(new java.awt.Dimension(404, 90));
        scrPnCurrentRolodex.setMinimumSize(new java.awt.Dimension(590, 175));
        scrPnCurrentRolodex.setPreferredSize(new java.awt.Dimension(590, 175));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridheight = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        add(scrPnCurrentRolodex, gridBagConstraints);

        scrPnNewRolodex.setMinimumSize(new java.awt.Dimension(590, 175));
        scrPnNewRolodex.setPreferredSize(new java.awt.Dimension(590, 175));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.gridheight = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 2, 0);
        add(scrPnNewRolodex, gridBagConstraints);

        btnYes.setFont(CoeusFontFactory.getLabelFont());
        btnYes.setMnemonic('Y');
        btnYes.setText("Yes");
        btnYes.setMaximumSize(new java.awt.Dimension(75, 26));
        btnYes.setMinimumSize(new java.awt.Dimension(75, 26));
        btnYes.setPreferredSize(new java.awt.Dimension(75, 26));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(3, 6, 0, 4);
        add(btnYes, gridBagConstraints);

        btnNo.setFont(CoeusFontFactory.getLabelFont());
        btnNo.setMnemonic('N');
        btnNo.setText("No");
        btnNo.setMaximumSize(new java.awt.Dimension(75, 26));
        btnNo.setMinimumSize(new java.awt.Dimension(75, 26));
        btnNo.setPreferredSize(new java.awt.Dimension(75, 26));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(3, 6, 1, 4);
        add(btnNo, gridBagConstraints);

        lblConfirmation.setFont(CoeusFontFactory.getLabelFont());
        lblConfirmation.setForeground(new java.awt.Color(51, 51, 255));
        lblConfirmation.setText("Are you sure you want to replace all occurances of rolodex Id 12574 in the table OSP$GFGFSD with rolodex id 322");
        lblConfirmation.setMaximumSize(new java.awt.Dimension(646, 16));
        lblConfirmation.setMinimumSize(new java.awt.Dimension(400, 16));
        lblConfirmation.setPreferredSize(new java.awt.Dimension(400, 32));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        add(lblConfirmation, gridBagConstraints);

        lblConfirmation1.setFont(CoeusFontFactory.getLabelFont());
        lblConfirmation1.setForeground(new java.awt.Color(51, 51, 255));
        lblConfirmation1.setText("Table name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        add(lblConfirmation1, gridBagConstraints);

    }//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnNo;
    private javax.swing.JButton btnYes;
    private javax.swing.JLabel lblConfirmation;
    private javax.swing.JLabel lblConfirmation1;
    private javax.swing.JScrollPane scrPnCurrentRolodex;
    private javax.swing.JScrollPane scrPnNewRolodex;
    // End of variables declaration//GEN-END:variables
    
}
