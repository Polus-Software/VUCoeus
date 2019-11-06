/*
 * @(#)CopyProtocolForm.java 1.0 04/6/2012
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */   

package edu.mit.coeus.irb.gui;

import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.departmental.gui.LookUpWindowConstants;
import edu.mit.coeus.exception.CoeusClientException;
import edu.mit.coeus.gui.CoeusAppletMDIForm;
import edu.mit.coeus.gui.CoeusDlgWindow;
import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.utils.AppletServletCommunicator;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.utils.TypeConstants;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Vector;
import javax.swing.AbstractAction;

/**
 *
 * @author  manjunathabn
 */
 
    
public class CopyProtocolForm extends javax.swing.JComponent implements ActionListener, TypeConstants, LookUpWindowConstants {
    
    String protocolNumber;
    private String userId;
    private String sequenceNumber;
    private CoeusAppletMDIForm mdiForm;
    private CoeusDlgWindow dlgParentComponent;    
    private final String IRB_PROTOCOL = "/protocolSubSrvlt";
    private static final char CHK_ATTACHMENT_QUESTIONAIRES_EXIST = 'Q';
    private boolean copyQnr;
    private boolean copyAttachments;
    private boolean copyOtherAttachments;
    private boolean cancel = true;
    private static final String ENABLE_COPY_PROTO_QNR = "ENABLE_COPY_PROTO_QNR";
    private static final String ENABLE_COPY_PROTO_ATTACHMENT = "ENABLE_COPY_PROTO_ATTACHMENT";
    private static final String ENABLE_COPY_PROTO_OTHER_ATTACHMENT = "ENABLE_COPY_PROTO_OTHER_ATTACHMENT";
    private static final String GET_PARAMETER_VALUE = "GET_PARAMETER_VALUE";
    
    /** Creates new form CopyProtocolForm
     * @param protocolNumber is the Source Protocol Number which has to be copied
     * @param userId  Logged in User Id
     */
    public CopyProtocolForm(CoeusAppletMDIForm mdiForm,String protocolNumber, String userId, String sequenceNumber) {
        this.mdiForm = mdiForm;
        this.protocolNumber = protocolNumber;
        this.userId = userId;
        this.sequenceNumber = sequenceNumber;
    }
    
    /** Creates new form CopyProtocolForm */
    public void showCopyProtocolForm() {
        initComponents();
         try {
            setFormData(getOptionRights());
        }catch(CoeusClientException coeusClientException) {
            CoeusOptionPane.showDialog(coeusClientException);
        }
        chkAttachments.setSelected(false);
        chkOtherAttachments.setSelected(false);
        chkQuestionnaire.setSelected(false);
        lblProtocolNumber.setText(protocolNumber);
        
        dlgParentComponent = new CoeusDlgWindow(mdiForm,
                "Select Copy Options", true);
        
            dlgParentComponent.getContentPane().add(pnlCopyProtocol);
        dlgParentComponent.pack();
        dlgParentComponent.setResizable(false);
        Dimension screenSize
                = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension dlgSize = dlgParentComponent.getSize();
        dlgParentComponent.setLocation(screenSize.width/2 - (dlgSize.width/2),
                screenSize.height/2 - (dlgSize.height/2));
        dlgParentComponent.addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent we){
                performWindowClosing();
                return;
            }
        });
        
        dlgParentComponent.addEscapeKeyListener(
                new AbstractAction("escPressed"){
            public void actionPerformed(ActionEvent ae){
                performWindowClosing();
                return;
            }
        });
        dlgParentComponent.setDefaultCloseOperation(CoeusDlgWindow.DO_NOTHING_ON_CLOSE);
        dlgParentComponent.addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent we){
                performWindowClosing();
                return;
            }
        });
        
        dlgParentComponent.addComponentListener(
                new ComponentAdapter(){
            public void componentShown(ComponentEvent e){
                setWindowFocus();
            }
        });
    }
    
     public void display(){
        dlgParentComponent.setVisible(true);
    }
    
    void performWindowClosing(){
        dlgParentComponent.dispose();
    }
    
    public void setWindowFocus(){
        btnOk.requestFocusInWindow();
    }
    
    /** This method closes the Copy Proposal Form
     */
    public void postInitComponents() {
        this.lblProtocolNumber.setText(this.protocolNumber);
    }
    
    /** This method is used to get whether the Proposal has Narrative and Budget information.
     * @return Vector contains whether Proposal has Narrative and Budget information
     */             
    public Vector getOptionRights() throws CoeusClientException {
        Vector vctOptionRights = null;
        String connectTo = CoeusGuiConstants.CONNECTION_URL + IRB_PROTOCOL;    
        RequesterBean request = new RequesterBean();        
        request.setFunctionType(CHK_ATTACHMENT_QUESTIONAIRES_EXIST);  
        Vector vecParameter = new Vector();
        vecParameter.add(protocolNumber);
        vecParameter.add(sequenceNumber);
        request.setDataObjects(vecParameter);
        AppletServletCommunicator comm = new AppletServletCommunicator(
                connectTo, request);
        comm.send();
        ResponderBean response = comm.getResponse();
        if(response.isSuccessfulResponse()) {
          vctOptionRights = (Vector)response.getDataObjects();
        }
        return vctOptionRights;
    }
    
    /** This method is used to enable/disable the checkbox based on the vctOptionRights values
     * @param vctOptionRights contains whether Protocol has Attachments and Questionnaires.
     */
    
    public void setFormData(Vector vctOptionRights) {
        String strHasAttachments = vctOptionRights.elementAt(0).toString();
        String strHasOtherAttachments = vctOptionRights.elementAt(1).toString();
        String strHasQnr = vctOptionRights.elementAt(2).toString();
        
        String copyQnr = getParameterValue(ENABLE_COPY_PROTO_QNR);
        String copyAttachments = getParameterValue(ENABLE_COPY_PROTO_ATTACHMENT);
        String copyOtherAttach = getParameterValue(ENABLE_COPY_PROTO_OTHER_ATTACHMENT);
        
        if("1".equals(copyAttachments)) {
            if(strHasAttachments.equals("NO")) {
                this.chkAttachments.setEnabled(false);
            } else {
                this.chkAttachments.setSelected(true);
            }
        } else{
            this.chkAttachments.setEnabled(false);
        }
        
        if("1".equals(copyOtherAttach)) {
            if(strHasOtherAttachments.equals("NO")) {
                this.chkOtherAttachments.setEnabled(false);
            } else {
                this.chkOtherAttachments.setSelected(true);
            }
        } else{
            this.chkOtherAttachments.setEnabled(false);
        }
        
        if("1".equals(copyQnr)) {
            if("NO".equals(strHasQnr)) {
                this.chkQuestionnaire.setEnabled(false);
            }else{
                this.chkQuestionnaire.setSelected(true);
            }
        } else{
            this.chkQuestionnaire.setEnabled(false);
        }
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        pnlCopyProtocol = new javax.swing.JPanel();
        pnlDescription = new javax.swing.JPanel();
        lblProtocolNumberText = new javax.swing.JLabel();
        lblProtocolNumber = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        pnlDetails = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        chkAttachments = new javax.swing.JCheckBox();
        chkQuestionnaire = new javax.swing.JCheckBox();
        chkOtherAttachments = new javax.swing.JCheckBox();
        pnlBtns = new javax.swing.JPanel();
        btnOk = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();

        setLayout(new java.awt.GridBagLayout());

        setMinimumSize(new java.awt.Dimension(340, 170));
        setPreferredSize(new java.awt.Dimension(340, 170));
        pnlCopyProtocol.setLayout(new java.awt.GridBagLayout());

        pnlCopyProtocol.setMinimumSize(new java.awt.Dimension(340, 170));
        pnlCopyProtocol.setPreferredSize(new java.awt.Dimension(340, 170));
        pnlDescription.setLayout(new java.awt.GridBagLayout());

        pnlDescription.setMinimumSize(new java.awt.Dimension(340, 30));
        pnlDescription.setPreferredSize(new java.awt.Dimension(340, 30));
        lblProtocolNumberText.setFont(CoeusFontFactory.getLabelFont());
        lblProtocolNumberText.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblProtocolNumberText.setText("Protocol Number :");
        lblProtocolNumberText.setMaximumSize(new java.awt.Dimension(130, 14));
        lblProtocolNumberText.setMinimumSize(new java.awt.Dimension(130, 14));
        lblProtocolNumberText.setPreferredSize(new java.awt.Dimension(130, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 2);
        pnlDescription.add(lblProtocolNumberText, gridBagConstraints);

        lblProtocolNumber.setFont(CoeusFontFactory.getLabelFont());
        lblProtocolNumber.setText("jlabel");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 180);
        pnlDescription.add(lblProtocolNumber, gridBagConstraints);

        pnlCopyProtocol.add(pnlDescription, new java.awt.GridBagConstraints());

        jSeparator1.setBackground(new java.awt.Color(0, 0, 0));
        jSeparator1.setForeground(new java.awt.Color(0, 0, 0));
        jSeparator1.setMinimumSize(new java.awt.Dimension(340, 2));
        jSeparator1.setPreferredSize(new java.awt.Dimension(340, 2));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        pnlCopyProtocol.add(jSeparator1, gridBagConstraints);

        pnlDetails.setLayout(new java.awt.GridBagLayout());

        pnlDetails.setMinimumSize(new java.awt.Dimension(340, 130));
        pnlDetails.setPreferredSize(new java.awt.Dimension(340, 130));
        jPanel3.setLayout(new java.awt.GridBagLayout());

        jPanel3.setMinimumSize(new java.awt.Dimension(250, 148));
        jPanel3.setPreferredSize(new java.awt.Dimension(250, 148));
        chkAttachments.setFont(CoeusFontFactory.getLabelFont());
        chkAttachments.setText("Attachments");
        chkAttachments.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        chkAttachments.setMargin(new java.awt.Insets(0, 0, 0, 0));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 0, 4, 90);
        jPanel3.add(chkAttachments, gridBagConstraints);

        chkQuestionnaire.setFont(CoeusFontFactory.getLabelFont());
        chkQuestionnaire.setText("Questionnaires");
        chkQuestionnaire.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        chkQuestionnaire.setMargin(new java.awt.Insets(0, 0, 0, 0));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 70, 90);
        jPanel3.add(chkQuestionnaire, gridBagConstraints);

        chkOtherAttachments.setFont(CoeusFontFactory.getLabelFont());
        chkOtherAttachments.setText("Other Attachments");
        chkOtherAttachments.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        chkOtherAttachments.setMargin(new java.awt.Insets(0, 0, 0, 0));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 4, 0);
        jPanel3.add(chkOtherAttachments, gridBagConstraints);

        pnlDetails.add(jPanel3, new java.awt.GridBagConstraints());

        pnlBtns.setLayout(new java.awt.GridBagLayout());

        pnlBtns.setMinimumSize(new java.awt.Dimension(100, 80));
        pnlBtns.setPreferredSize(new java.awt.Dimension(100, 80));
        btnOk.setFont(CoeusFontFactory.getLabelFont());
        btnOk.setText("Ok");
        btnOk.setMaximumSize(new java.awt.Dimension(71, 23));
        btnOk.setMinimumSize(new java.awt.Dimension(71, 23));
        btnOk.setPreferredSize(new java.awt.Dimension(71, 23));
        btnOk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOkActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 4, 0);
        pnlBtns.add(btnOk, gridBagConstraints);

        btnCancel.setFont(CoeusFontFactory.getLabelFont());
        btnCancel.setText("Cancel");
        btnCancel.setMaximumSize(new java.awt.Dimension(71, 23));
        btnCancel.setMinimumSize(new java.awt.Dimension(71, 23));
        btnCancel.setPreferredSize(new java.awt.Dimension(71, 23));
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        pnlBtns.add(btnCancel, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        pnlDetails.add(pnlBtns, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        pnlCopyProtocol.add(pnlDetails, gridBagConstraints);

        add(pnlCopyProtocol, new java.awt.GridBagConstraints());

    }// </editor-fold>//GEN-END:initComponents

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
       // TODO add your handling code here:
       // this.setIsCancel(true);
        dlgParentComponent.dispose();
    }//GEN-LAST:event_btnCancelActionPerformed

    private void btnOkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOkActionPerformed
       // TODO add your handling code here:   
        this.setCancel(false);
        if(chkAttachments.isSelected()){
            this.setCopyAttachments(true);
        }
        else {
            setCopyAttachments(false);
        }
        if(chkOtherAttachments.isSelected()){
            this.setCopyOtherAttachments(true);
        }
        else {
            setCopyOtherAttachments(false);
        }
        if(chkQuestionnaire.isSelected()){
            this.setCopyQnr(true);
        }
        else {
            setCopyQnr(false);
        }
        dlgParentComponent.dispose();
    }//GEN-LAST:event_btnOkActionPerformed

    public void actionPerformed(ActionEvent e) {
    }

    public boolean isCopyQnr() {
        return copyQnr;
    }

    public void setCopyQnr(boolean copyQnr) {
        this.copyQnr = copyQnr;
    }

    public boolean isCopyAttachments() {
        return copyAttachments;
    }

    public void setCopyAttachments(boolean copyAttachments) {
        this.copyAttachments = copyAttachments;
    }

    public boolean isCopyOtherAttachments() {
        return copyOtherAttachments;
    }

    public void setCopyOtherAttachments(boolean copyOtherAttachments) {
        this.copyOtherAttachments = copyOtherAttachments;
    }

    public boolean isCancel() {
        return cancel;
    }

    public void setCancel(boolean cancel) {
        this.cancel = cancel;
    }

   
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnOk;
    private javax.swing.JCheckBox chkAttachments;
    private javax.swing.JCheckBox chkOtherAttachments;
    private javax.swing.JCheckBox chkQuestionnaire;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JLabel lblProtocolNumber;
    private javax.swing.JLabel lblProtocolNumberText;
    private javax.swing.JPanel pnlBtns;
    private javax.swing.JPanel pnlCopyProtocol;
    private javax.swing.JPanel pnlDescription;
    private javax.swing.JPanel pnlDetails;
    // End of variables declaration//GEN-END:variables
    
    /**
   * Method to get the value for the parameter 
   * @return value
   */ 
   public String getParameterValue(String parameter){
        final String connectTo = CoeusGuiConstants.CONNECTION_URL+ "/coeusFunctionsServlet";
        String value = CoeusGuiConstants.EMPTY_STRING;
        RequesterBean requester = new RequesterBean();
        ResponderBean responder = null;
        requester.setDataObject(GET_PARAMETER_VALUE);
        Vector vecParameter = new Vector();
        vecParameter.add(parameter);
        requester.setDataObjects(vecParameter);
        AppletServletCommunicator comm
        = new AppletServletCommunicator(connectTo, requester);
        comm.send();
        responder = comm.getResponse();
        if(responder.isSuccessfulResponse()){
            value =(String) responder.getDataObject();
        }
        return value;
    } 
}
