/*
 * DataViewForm.java
 *
 * Created on June 9, 2004, 1:03 PM
 */

package edu.mit.coeus.utils;

import edu.mit.coeus.brokers.*;
import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.gui.CoeusDlgWindow;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.iacuc.bean.ProtocolActionsBean;
import edu.mit.coeus.iacuc.bean.ProtocolSubmissionInfoBean;
import java.awt.Cursor;

import java.awt.event.WindowEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.text.SimpleDateFormat;
import javax.swing.AbstractAction;
import javax.swing.JOptionPane;

/**
 * This is a reusable component to view any huge data in a dialog by customizing the
 * title and providing the data to be viewed.
 * @author  ravikanth
 */
public class IacucDataViewForm extends javax.swing.JComponent {
    CoeusDlgWindow dlgWindow;
    
    //Protocol Enhancment - Case #1790:Start 1
    CoeusMessageResources coeusMessageResources = CoeusMessageResources.getInstance();
    private char functionType;
    private ProtocolActionsBean protocolActionsBean = new ProtocolActionsBean();
    private boolean OK_CLICKED = false;
    private ProtocolSubmissionInfoBean protoSubmInfoBean;
    private static final char SAVE_ACTION_COMMENTS = 'c';
    //Protocol Enhancment - Case #1790:End 1
    
    
    /** Creates new form DataViewForm */
    public IacucDataViewForm() {
        initComponents();
        dlgWindow = new CoeusDlgWindow(CoeusGuiConstants.getMDIForm(), "Coeus", true);
        dlgWindow.getContentPane().add(this);
        //dlgWindow.pack();
        dlgWindow.setSize(425, 280);
        dlgWindow.setResizable(false);
        dlgWindow.setLocation(CoeusDlgWindow.CENTER);
        registerComponents();
    }
    private void registerComponents(){
        dlgWindow.addComponentListener(new ComponentAdapter(){
             public void componentShown(ComponentEvent e){
                 
                 //Protocol Enhancment - Case #1790:Start 6
                 //btnOk.requestFocusInWindow();
                 btnCancel.requestFocusInWindow();
                 //Protocol Enhancment - Case #1790:Start 6
             }
         });
         
        btnOk.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent ae){
                //Protocol Enhancment -  Case #1790: Start 2
                //dlgWindow.setVisible(false);
                if(isSaveRequired()){
                    if(functionType == CoeusGuiConstants.MODIFY_MODE){
                        setOK_CLICKED(true);
                        protocolActionsBean.setComments(txtArData.getText().trim());
                        protocolActionsBean.setAcType("U");
                        dlgWindow.dispose();
                    }else{
                        saveFormData();
                        dlgWindow.dispose();
                    }
                }else{
                    dlgWindow.dispose();
                }
            //Protocol Enhancment -  Case #1790: End 2
        }
    });
    
    //Protocol Enhancment -  Case #1790: Start 3
    btnCancel.addActionListener(new ActionListener(){
        public void actionPerformed(ActionEvent ae){
            if(isSaveRequired()){
                performCancelAction();
            }else{
                dlgWindow.dispose();
            }
            
        }//End actionPerformed
    });
    
    dlgWindow.addEscapeKeyListener(new AbstractAction("escPressed"){
        public void actionPerformed(ActionEvent ae){
            if(isSaveRequired()){
                performCancelAction();
            }else{
                dlgWindow.dispose();
            }
        }
    });
    
    dlgWindow.setDefaultCloseOperation(CoeusDlgWindow.DO_NOTHING_ON_CLOSE);
    dlgWindow.addWindowListener(new WindowAdapter(){
        
        public void windowClosing(WindowEvent we){
            if(isSaveRequired()){
                performCancelAction();
            }else{
                dlgWindow.dispose();
            }
        }
    });
    
    //Protocol Enhancment -  Case #1790: End 3
}


    /**
     * Method used to show the dialog window.
     */
    public void display(){
        //        btnOk.requestFocusInWindow();
        //Protocol Enhancment -  Case #1790: Start 5
        boolean hasRights = checkForUserRights();
        if(!hasRights){
            btnOk.setEnabled(false);
            txtArData.setEditable(false);
            txtArData.setEnabled(false);
            txtArData.setBackground(
            (java.awt.Color) javax.swing.UIManager.getDefaults().get("Panel.background"));
        }
        //Case 2112 Start
        else{
            txtArData.setBackground(java.awt.Color.WHITE);
            btnOk.setEnabled(true);
            txtArData.setEditable(true);
            txtArData.setEnabled(true);
        }//Case 2112 End
        dlgWindow.setVisible(true);
        //Protocol Enhancment -  Case #1790: End 5
    }
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        scrPnData = new javax.swing.JScrollPane();
        txtArData = new javax.swing.JTextArea();
        btnOk = new javax.swing.JButton();
        lblUpdateUser = new javax.swing.JLabel();
        lblUpdateUserValue = new javax.swing.JLabel();
        lblUpdTimestamp = new javax.swing.JLabel();
        lblUpdTimestampValue = new javax.swing.JLabel();
        btnCancel = new javax.swing.JButton();

        setLayout(new java.awt.GridBagLayout());

        scrPnData.setMinimumSize(new java.awt.Dimension(300, 200));
        scrPnData.setPreferredSize(new java.awt.Dimension(300, 200));
        txtArData.setFont(CoeusFontFactory.getNormalFont());
        txtArData.setLineWrap(true);
        txtArData.setWrapStyleWord(true);
        txtArData.setDisabledTextColor(java.awt.Color.black);
        txtArData.setEnabled(false);
        scrPnData.setViewportView(txtArData);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridheight = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 0);
        add(scrPnData, gridBagConstraints);

        btnOk.setFont(CoeusFontFactory.getLabelFont());
        btnOk.setMnemonic('o');
        btnOk.setText("OK");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        add(btnOk, gridBagConstraints);

        lblUpdateUser.setFont(CoeusFontFactory.getLabelFont());
        lblUpdateUser.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblUpdateUser.setText("Update User: ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        add(lblUpdateUser, gridBagConstraints);

        lblUpdateUserValue.setFont(CoeusFontFactory.getNormalFont());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        add(lblUpdateUserValue, gridBagConstraints);

        lblUpdTimestamp.setFont(CoeusFontFactory.getLabelFont());
        lblUpdTimestamp.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblUpdTimestamp.setText("Update Timestamp: ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 5, 0, 0);
        add(lblUpdTimestamp, gridBagConstraints);

        lblUpdTimestampValue.setFont(CoeusFontFactory.getNormalFont());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 0, 0);
        add(lblUpdTimestampValue, gridBagConstraints);

        btnCancel.setFont(CoeusFontFactory.getLabelFont());
        btnCancel.setMnemonic('C');
        btnCancel.setText("Cancel");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        add(btnCancel, gridBagConstraints);

    }//GEN-END:initComponents

    /**
     * Method used to set the customized title to the dialog
     * @param title new customized title.
     */
    public void setTitle(java.lang.String title) {
        if( title != null && title.length() > 0 ){
            dlgWindow.setTitle(title);
        }
    }    
    
    //Protocol Enhancment -  Case #1790: Start 4
    /**
     * Method used to set the data to be shown.
     * @param data new data to be shown in the dialog.
     */
    /*public void setData(java.lang.String data) {
        txtArData.setText(data);
        txtArData.setCaretPosition(0);
    }*/
    
    public void setData(edu.mit.coeus.iacuc.bean.ProtocolActionsBean protocolActionsBean) {
        this.protocolActionsBean = protocolActionsBean;
        
        String DATE_FORMAT_DISPLAY = "dd-MMM-yyyy";
        String updateFormat = DATE_FORMAT_DISPLAY + " hh:mm a";
        
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(updateFormat);
        
        if(protocolActionsBean.getComments() == null){
            txtArData.setText("");
        }else{
            txtArData.setText(protocolActionsBean.getComments().trim());
        }
        txtArData.setCaretPosition(0);
//        lblUpdateUserValue.setText(protocolActionsBean.getUpdateUser());
        /*
         * UserID to UserName Enhancement - Start
         * Added UserUtils class to change userid to username
         */
        lblUpdateUserValue.setText(UserUtils.getDisplayName(protocolActionsBean.getUpdateUser()));
        // UserId to UserName - End
        lblUpdTimestampValue.setText(simpleDateFormat.format(protocolActionsBean.getUpdateTimestamp()));
    }
    
    /**
     * Getter for property OK_CLICKED.
     * @return Value of property OK_CLICKED.
     */
    public boolean isOK_CLICKED() {
        return OK_CLICKED;
    }
    
    /**
     * Setter for property OK_CLICKED.
     * @param OK_CLICKED New value of property OK_CLICKED.
     */
    public void setOK_CLICKED(boolean OK_CLICKED) {
        this.OK_CLICKED = OK_CLICKED;
    }
    
    public void setFunctionType(char functionType){
        this.functionType = functionType;
    }
    
    public void setProtoSubmissionBean(ProtocolSubmissionInfoBean protoSubmInfoBean){
        this.protoSubmInfoBean = protoSubmInfoBean;
    }
    public boolean isSaveRequired(){
        boolean save = false;
        if(protocolActionsBean.getComments() != null){
            String strComments = txtArData.getText().trim();
            if(!protocolActionsBean.getComments().trim().equals(strComments)){
                save = true;
                return save;
            }else{
                save = false;
                return save;
            }
        }
        return save;
    }
    
    private void performCancelAction(){
        String msg = coeusMessageResources.parseMessageKey(
        "saveConfirmCode.1002");
        
        int confirm = CoeusOptionPane.showQuestionDialog(msg,
        CoeusOptionPane.OPTION_YES_NO_CANCEL,
        CoeusOptionPane.DEFAULT_YES);
        switch(confirm){
            case ( JOptionPane.NO_OPTION ) :
                dlgWindow.dispose();
                break;
            case ( JOptionPane.YES_OPTION ) :
                if(functionType == CoeusGuiConstants.MODIFY_MODE){
                    setOK_CLICKED(true);
                    protocolActionsBean.setComments(txtArData.getText().trim());
                    protocolActionsBean.setAcType("U");
                    dlgWindow.dispose();
                }else{
                    saveFormData();
                    dlgWindow.dispose();
                }
                break;
            case ( JOptionPane.CANCEL_OPTION ) :
                break;
        }//End switch
    }
    
    private void saveFormData(){
        protocolActionsBean.setComments(txtArData.getText().trim());
        protocolActionsBean.setAcType("U");
        
        String connectTo = CoeusGuiConstants.CONNECTION_URL + "/protocolMntServlet";
        
        
        RequesterBean request = new RequesterBean();
        request.setFunctionType(SAVE_ACTION_COMMENTS);
        request.setId(protocolActionsBean.getProtocolNumber());
        request.setDataObject(protocolActionsBean) ;
        AppletServletCommunicator comm 
                    = new AppletServletCommunicator(connectTo, request);
         setCursor( new Cursor( Cursor.WAIT_CURSOR ) );        
        comm.send();
        ResponderBean response = comm.getResponse();
        setCursor( new Cursor( Cursor.DEFAULT_CURSOR ) );

        if (!response.isSuccessfulResponse()){
            Exception ex = response.getException();
            ex.printStackTrace();
        }
    }
    
    private boolean checkForUserRights(){
        String connectTo = CoeusGuiConstants.CONNECTION_URL + "/IacucProtoSubmissionDetailsServlet";
        
        
        RequesterBean request = new RequesterBean();
        request.setFunctionType('R');
        request.setId(protocolActionsBean.getProtocolNumber());
        request.setDataObject(protoSubmInfoBean) ;
        AppletServletCommunicator comm
            = new AppletServletCommunicator(connectTo, request);

        setCursor( new Cursor( Cursor.WAIT_CURSOR ) );        
        comm.send();
        ResponderBean response = comm.getResponse();
        setCursor( new Cursor( Cursor.DEFAULT_CURSOR ) );

        if (!response.isSuccessfulResponse()) 
        {
            return false;
        }
        return true ;
    }
    //Protocol Enhancment -  Case #1790: End 4
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnOk;
    private javax.swing.JLabel lblUpdTimestamp;
    private javax.swing.JLabel lblUpdTimestampValue;
    private javax.swing.JLabel lblUpdateUser;
    private javax.swing.JLabel lblUpdateUserValue;
    private javax.swing.JScrollPane scrPnData;
    private javax.swing.JTextArea txtArData;
    // End of variables declaration//GEN-END:variables
    
}
