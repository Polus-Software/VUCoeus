/*
 * @(#)MedusaIACUCProtocolForm.java 1.0 04/10/11 3:26 PM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */
package edu.mit.coeus.propdev.gui;

import edu.mit.coeus.gui.CoeusAppletMDIForm;
import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.iacuc.bean.ProtocolInfoBean;
import edu.mit.coeus.utils.*;


/**
 *
 * @author  manjunathabn
 */
public class MedusaIACUCProtocolForm extends javax.swing.JComponent {
    
    CoeusAppletMDIForm mdiForm;    
    private static final String REQUIRED_DATEFORMAT = "dd-MMM-yyyy";
    private DateUtils dtUtils = new DateUtils();
    
    /** Creates new form MedusaIRBProtocolForm */
    public MedusaIACUCProtocolForm() {
        initComponents();
    }
    
    /** Creates new form MedusaPropSubContract */
    public MedusaIACUCProtocolForm(CoeusAppletMDIForm mdiForm) {
        this.mdiForm = mdiForm;        
        initComponents();
    }
    
    /*
     * Method takes up the bean and sets all the respective fields
     */
    public void setDataValues(ProtocolInfoBean protocolInfoBean){
        //set proposal Number
        String protocolNumber = protocolInfoBean.getProtocolNumber();
        protocolNumber =  (protocolNumber == null ? CoeusGuiConstants.EMPTY_STRING:protocolNumber);
        lblProtNumValue.setText(protocolNumber );
        
        String status = protocolInfoBean.getProtocolStatusDesc();
        status =  (status == null ? CoeusGuiConstants.EMPTY_STRING :status);
        lblStatusValue.setText(status);
        
        String instTitle = protocolInfoBean.getTitle();
        instTitle =  (instTitle == null ? CoeusGuiConstants.EMPTY_STRING:instTitle);
        txtArTitle.setText(instTitle);
        txtArTitle.setCaretPosition(0);
        
        String protocolType = protocolInfoBean.getProtocolTypeDesc();
        protocolType =  (protocolType == null ? CoeusGuiConstants.EMPTY_STRING:protocolType);
        lblTypeValue.setText(protocolType);
        
        if(protocolInfoBean.getApprovalDate() != null ){
            String approvalDate = protocolInfoBean.getApprovalDate().toString();
            approvalDate = (approvalDate == null ? CoeusGuiConstants.EMPTY_STRING : approvalDate );
            approvalDate = dtUtils.formatDate(approvalDate,REQUIRED_DATEFORMAT);
            lblAppDateValue.setText(approvalDate );
        }else{
            lblAppDateValue.setText(CoeusGuiConstants.EMPTY_STRING);
        }
                
        if(protocolInfoBean.getLastApprovalDate() != null ){
            String lastApprovalDate = protocolInfoBean.getLastApprovalDate().toString();
            lastApprovalDate = (lastApprovalDate == null ? CoeusGuiConstants.EMPTY_STRING : lastApprovalDate );
            lastApprovalDate = dtUtils.formatDate(lastApprovalDate,REQUIRED_DATEFORMAT);
            lblLastAppDateValue.setText(lastApprovalDate );
        }else{
            lblLastAppDateValue.setText(CoeusGuiConstants.EMPTY_STRING);
        }
        
        if(protocolInfoBean.getApplicationDate() != null ){
            String applDate = protocolInfoBean.getApplicationDate().toString();
            applDate = (applDate == null ? CoeusGuiConstants.EMPTY_STRING : applDate );
            applDate = dtUtils.formatDate(applDate,REQUIRED_DATEFORMAT);
            lblApplDateValue.setText(applDate );
        }else{
            lblApplDateValue.setText(CoeusGuiConstants.EMPTY_STRING);
        }
        
        if(protocolInfoBean.getExpirationDate() != null ){
            String expDate = protocolInfoBean.getExpirationDate().toString();
            expDate = (expDate == null ? CoeusGuiConstants.EMPTY_STRING : expDate );
            expDate = dtUtils.formatDate(expDate,REQUIRED_DATEFORMAT);
            lblExpDateValue.setText(expDate );
        }else{
            lblExpDateValue.setText(CoeusGuiConstants.EMPTY_STRING);
        }
        
        String layStatement_1 = protocolInfoBean.getLayStmt1();
        layStatement_1 =  (layStatement_1 == null ? CoeusGuiConstants.EMPTY_STRING:layStatement_1);
        txtArLayStmnt1.setText(layStatement_1);
        
        String layStatement_2 = protocolInfoBean.getLayStmt2();
        layStatement_2 =  (layStatement_2 == null ? CoeusGuiConstants.EMPTY_STRING:layStatement_2);
        txtArLayStmnt2.setText(layStatement_2);
        
        String refenceNum_1 = protocolInfoBean.getRefNum_1();
        refenceNum_1 =  (refenceNum_1 == null ? CoeusGuiConstants.EMPTY_STRING:refenceNum_1);
        txtArRefNum1.setText(refenceNum_1);
        
        String refenceNum_2 = protocolInfoBean.getRefNum_1();
        refenceNum_2 =  (refenceNum_2 == null ? CoeusGuiConstants.EMPTY_STRING:refenceNum_2);
        txtArRefNum2.setText(refenceNum_2);
        
    }
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        lblProtNum = new javax.swing.JLabel();
        lblProtNumValue = new javax.swing.JLabel();
        lblStatus = new javax.swing.JLabel();
        lblStatusValue = new javax.swing.JLabel();
        lblType = new javax.swing.JLabel();
        lblTypeValue = new javax.swing.JLabel();
        lblTitle = new javax.swing.JLabel();
        scrPnTitle = new javax.swing.JScrollPane();
        txtArTitle = new javax.swing.JTextArea();
        lblAppDate = new javax.swing.JLabel();
        lblAppDateValue = new javax.swing.JLabel();
        lblLastAppDate = new javax.swing.JLabel();
        lblLastAppDateValue = new javax.swing.JLabel();
        lblApplDate = new javax.swing.JLabel();
        lblApplDateValue = new javax.swing.JLabel();
        lblExpDate = new javax.swing.JLabel();
        lblExpDateValue = new javax.swing.JLabel();
        lblRefNum1 = new javax.swing.JLabel();
        lblArLayStmt1 = new javax.swing.JLabel();
        scrPnLayStmnt1 = new javax.swing.JScrollPane();
        txtArLayStmnt1 = new javax.swing.JTextArea();
        lblLayStmnt2 = new javax.swing.JLabel();
        scrLayStmnt2 = new javax.swing.JScrollPane();
        txtArLayStmnt2 = new javax.swing.JTextArea();
        scrPnRefNum1 = new javax.swing.JScrollPane();
        txtArRefNum1 = new javax.swing.JTextArea();
        lblRefNum2 = new javax.swing.JLabel();
        scrPnRefNum2 = new javax.swing.JScrollPane();
        txtArRefNum2 = new javax.swing.JTextArea();

        setLayout(new java.awt.GridBagLayout());

        lblProtNum.setFont(CoeusFontFactory.getLabelFont());
        lblProtNum.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblProtNum.setText("Protocol No:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipady = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 2, 0, 1);
        add(lblProtNum, gridBagConstraints);

        lblProtNumValue.setFont(CoeusFontFactory.getNormalFont());
        lblProtNumValue.setText("p num");
        lblProtNumValue.setMaximumSize(new java.awt.Dimension(3, 14));
        lblProtNumValue.setMinimumSize(new java.awt.Dimension(3, 14));
        lblProtNumValue.setPreferredSize(new java.awt.Dimension(3, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 111;
        gridBagConstraints.ipady = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 2, 0, 0);
        add(lblProtNumValue, gridBagConstraints);

        lblStatus.setFont(CoeusFontFactory.getLabelFont());
        lblStatus.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblStatus.setText("Status:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipady = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 86, 0, 0);
        add(lblStatus, gridBagConstraints);

        lblStatusValue.setText("status");
        lblStatusValue.setMaximumSize(new java.awt.Dimension(34, 14));
        lblStatusValue.setMinimumSize(new java.awt.Dimension(34, 14));
        lblStatusValue.setPreferredSize(new java.awt.Dimension(34, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 7;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 26;
        gridBagConstraints.ipady = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 1, 0, 0);
        add(lblStatusValue, gridBagConstraints);

        lblType.setFont(CoeusFontFactory.getLabelFont());
        lblType.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblType.setText("Type:");
        lblType.setMaximumSize(new java.awt.Dimension(40, 14));
        lblType.setMinimumSize(new java.awt.Dimension(40, 14));
        lblType.setPreferredSize(new java.awt.Dimension(35, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 2, 0, 1);
        add(lblType, gridBagConstraints);

        lblTypeValue.setText("type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.ipadx = 92;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 2, 0, 0);
        add(lblTypeValue, gridBagConstraints);

        lblTitle.setFont(CoeusFontFactory.getLabelFont());
        lblTitle.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblTitle.setText("Title:");
        lblTitle.setMaximumSize(new java.awt.Dimension(24, 4));
        lblTitle.setMinimumSize(new java.awt.Dimension(24, 4));
        lblTitle.setPreferredSize(new java.awt.Dimension(35, 4));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 6;
        gridBagConstraints.ipady = 16;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 2, 19, 1);
        add(lblTitle, gridBagConstraints);

        scrPnTitle.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        scrPnTitle.setMaximumSize(new java.awt.Dimension(350, 30));
        scrPnTitle.setMinimumSize(new java.awt.Dimension(350, 30));
        scrPnTitle.setPreferredSize(new java.awt.Dimension(350, 30));
        txtArTitle.setBackground((java.awt.Color) javax.swing.UIManager.getDefaults().get("Panel.background"));
        txtArTitle.setDocument(new LimitedPlainDocument(200));
        txtArTitle.setEditable(false);
        txtArTitle.setFont(CoeusFontFactory.getNormalFont());
        txtArTitle.setWrapStyleWord(true);
        txtArTitle.setBorder(null);
        txtArTitle.setDisabledTextColor(java.awt.Color.black);
        txtArTitle.setEnabled(false);
        txtArTitle.setMaximumSize(new java.awt.Dimension(150, 30));
        scrPnTitle.setViewportView(txtArTitle);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 70;
        gridBagConstraints.ipady = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 1, 0, 0);
        add(scrPnTitle, gridBagConstraints);

        lblAppDate.setFont(CoeusFontFactory.getLabelFont());
        lblAppDate.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblAppDate.setText("Approval Date:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipady = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 2, 0, 1);
        add(lblAppDate, gridBagConstraints);

        lblAppDateValue.setFont(CoeusFontFactory.getNormalFont());
        lblAppDateValue.setText("appDate");
        lblAppDateValue.setMaximumSize(new java.awt.Dimension(50, 14));
        lblAppDateValue.setMinimumSize(new java.awt.Dimension(50, 14));
        lblAppDateValue.setPreferredSize(new java.awt.Dimension(70, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.ipadx = 40;
        gridBagConstraints.ipady = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 1, 0, 0);
        add(lblAppDateValue, gridBagConstraints);

        lblLastAppDate.setFont(CoeusFontFactory.getLabelFont());
        lblLastAppDate.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblLastAppDate.setText("Last Approval Date:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipady = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 25, 0, 0);
        add(lblLastAppDate, gridBagConstraints);

        lblLastAppDateValue.setFont(CoeusFontFactory.getNormalFont());
        lblLastAppDateValue.setText("lastAppDate");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 7;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipady = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 1, 0, 0);
        add(lblLastAppDateValue, gridBagConstraints);

        lblApplDate.setFont(CoeusFontFactory.getLabelFont());
        lblApplDate.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblApplDate.setText("Application Date:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipady = -1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 2, 0, 1);
        add(lblApplDate, gridBagConstraints);

        lblApplDateValue.setFont(CoeusFontFactory.getNormalFont());
        lblApplDateValue.setText("appldate");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.ipadx = 48;
        gridBagConstraints.ipady = -2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 0, 0);
        add(lblApplDateValue, gridBagConstraints);

        lblExpDate.setFont(CoeusFontFactory.getLabelFont());
        lblExpDate.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblExpDate.setText("Expiration Date:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 2;
        gridBagConstraints.ipady = -2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 45, 0, 0);
        add(lblExpDate, gridBagConstraints);

        lblExpDateValue.setFont(CoeusFontFactory.getNormalFont());
        lblExpDateValue.setText("expDate");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 7;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 19;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 1, 0, 0);
        add(lblExpDateValue, gridBagConstraints);

        lblRefNum1.setFont(CoeusFontFactory.getLabelFont());
        lblRefNum1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblRefNum1.setText("Reference No 1:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(3, 2, 0, 1);
        add(lblRefNum1, gridBagConstraints);

        lblArLayStmt1.setFont(CoeusFontFactory.getLabelFont());
        lblArLayStmt1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblArLayStmt1.setText("Lay Statement 1:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 7;
        gridBagConstraints.ipady = 16;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(3, 2, 10, 1);
        add(lblArLayStmt1, gridBagConstraints);

        scrPnLayStmnt1.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        scrPnLayStmnt1.setMaximumSize(new java.awt.Dimension(350, 30));
        scrPnLayStmnt1.setMinimumSize(new java.awt.Dimension(350, 30));
        scrPnLayStmnt1.setPreferredSize(new java.awt.Dimension(350, 30));
        txtArLayStmnt1.setBackground((java.awt.Color) javax.swing.UIManager.getDefaults().get("Panel.background"));
        txtArLayStmnt1.setColumns(20);
        txtArLayStmnt1.setDocument(new LimitedPlainDocument(200));
        txtArLayStmnt1.setEditable(false);
        txtArLayStmnt1.setFont(CoeusFontFactory.getNormalFont());
        txtArLayStmnt1.setWrapStyleWord(true);
        txtArLayStmnt1.setBorder(null);
        txtArLayStmnt1.setDisabledTextColor(java.awt.Color.black);
        txtArLayStmnt1.setEnabled(false);
        scrPnLayStmnt1.setViewportView(txtArLayStmnt1);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 80;
        gridBagConstraints.ipady = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(9, 1, 0, 0);
        add(scrPnLayStmnt1, gridBagConstraints);

        lblLayStmnt2.setFont(CoeusFontFactory.getLabelFont());
        lblLayStmnt2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblLayStmnt2.setText("Lay Statement 2:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(3, 2, 0, 1);
        add(lblLayStmnt2, gridBagConstraints);

        scrLayStmnt2.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        scrLayStmnt2.setMaximumSize(new java.awt.Dimension(350, 30));
        scrLayStmnt2.setMinimumSize(new java.awt.Dimension(350, 30));
        scrLayStmnt2.setPreferredSize(new java.awt.Dimension(350, 30));
        txtArLayStmnt2.setBackground((java.awt.Color) javax.swing.UIManager.getDefaults().get("Panel.background"));
        txtArLayStmnt2.setColumns(20);
        txtArLayStmnt2.setDocument(new LimitedPlainDocument(200));
        txtArLayStmnt2.setEditable(false);
        txtArLayStmnt2.setFont(CoeusFontFactory.getNormalFont());
        txtArLayStmnt2.setWrapStyleWord(true);
        txtArLayStmnt2.setDisabledTextColor(java.awt.Color.black);
        txtArLayStmnt2.setEnabled(false);
        scrLayStmnt2.setViewportView(txtArLayStmnt2);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 15;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 80;
        gridBagConstraints.ipady = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 0, 205);
        add(scrLayStmnt2, gridBagConstraints);

        scrPnRefNum1.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        scrPnRefNum1.setMaximumSize(new java.awt.Dimension(350, 30));
        scrPnRefNum1.setMinimumSize(new java.awt.Dimension(350, 30));
        scrPnRefNum1.setPreferredSize(new java.awt.Dimension(350, 30));
        txtArRefNum1.setBackground((java.awt.Color) javax.swing.UIManager.getDefaults().get("Panel.background"));
        txtArRefNum1.setColumns(20);
        txtArRefNum1.setDocument(new LimitedPlainDocument(200));
        txtArRefNum1.setEditable(false);
        txtArRefNum1.setFont(CoeusFontFactory.getNormalFont());
        txtArRefNum1.setWrapStyleWord(true);
        txtArRefNum1.setBorder(null);
        txtArRefNum1.setDisabledTextColor(java.awt.Color.black);
        txtArRefNum1.setEnabled(false);
        txtArRefNum1.setPreferredSize(new java.awt.Dimension(100, 18));
        scrPnRefNum1.setViewportView(txtArRefNum1);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.gridwidth = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 70;
        gridBagConstraints.ipady = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 1, 0, 0);
        add(scrPnRefNum1, gridBagConstraints);

        lblRefNum2.setFont(CoeusFontFactory.getLabelFont());
        lblRefNum2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblRefNum2.setText("Reference No 2:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(3, 2, 21, 1);
        add(lblRefNum2, gridBagConstraints);

        scrPnRefNum2.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        scrPnRefNum2.setMaximumSize(new java.awt.Dimension(350, 30));
        scrPnRefNum2.setMinimumSize(new java.awt.Dimension(350, 30));
        scrPnRefNum2.setPreferredSize(new java.awt.Dimension(350, 30));
        txtArRefNum2.setBackground((java.awt.Color) javax.swing.UIManager.getDefaults().get("Panel.background"));
        txtArRefNum2.setDocument(new LimitedPlainDocument(200));
        txtArRefNum2.setEditable(false);
        txtArRefNum2.setFont(CoeusFontFactory.getNormalFont());
        txtArRefNum2.setWrapStyleWord(true);
        txtArRefNum2.setBorder(null);
        txtArRefNum2.setDisabledTextColor(java.awt.Color.black);
        txtArRefNum2.setEnabled(false);
        txtArRefNum2.setPreferredSize(new java.awt.Dimension(100, 18));
        scrPnRefNum2.setViewportView(txtArRefNum2);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.gridwidth = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 70;
        gridBagConstraints.ipady = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 267, 0);
        add(scrPnRefNum2, gridBagConstraints);

    }// </editor-fold>//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel lblAppDate;
    private javax.swing.JLabel lblAppDateValue;
    private javax.swing.JLabel lblApplDate;
    private javax.swing.JLabel lblApplDateValue;
    private javax.swing.JLabel lblArLayStmt1;
    private javax.swing.JLabel lblExpDate;
    private javax.swing.JLabel lblExpDateValue;
    private javax.swing.JLabel lblLastAppDate;
    private javax.swing.JLabel lblLastAppDateValue;
    private javax.swing.JLabel lblLayStmnt2;
    private javax.swing.JLabel lblProtNum;
    private javax.swing.JLabel lblProtNumValue;
    private javax.swing.JLabel lblRefNum1;
    private javax.swing.JLabel lblRefNum2;
    private javax.swing.JLabel lblStatus;
    private javax.swing.JLabel lblStatusValue;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JLabel lblType;
    private javax.swing.JLabel lblTypeValue;
    private javax.swing.JScrollPane scrLayStmnt2;
    private javax.swing.JScrollPane scrPnLayStmnt1;
    private javax.swing.JScrollPane scrPnRefNum1;
    private javax.swing.JScrollPane scrPnRefNum2;
    private javax.swing.JScrollPane scrPnTitle;
    private javax.swing.JTextArea txtArLayStmnt1;
    private javax.swing.JTextArea txtArLayStmnt2;
    private javax.swing.JTextArea txtArRefNum1;
    private javax.swing.JTextArea txtArRefNum2;
    private javax.swing.JTextArea txtArTitle;
    // End of variables declaration//GEN-END:variables
    
}
