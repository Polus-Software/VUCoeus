/*
 * @(#)ProtocolSubmissionDisplayForm.java
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 * 
 *
 * Created on November 27, 2002, 10:38 AM
 */

package edu.mit.coeus.irb.gui;

import java.util.*;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableColumn;
import javax.swing.table.DefaultTableModel;

import edu.mit.coeus.brokers.*;
import edu.mit.coeus.gui.*;
import edu.mit.coeus.utils.*;
import edu.mit.coeus.irb.bean.*;

/** This class is used to show the Protocol Submission details to the user. This
 * consists of the Committee where the Protocol is submitted and the Schedule
 * details when this Protocol is going to be reviewed along with the
 * reviewers and submission type details.
 *
 * @author ravikanth
 */
public class ProtocolSubmissionDisplayForm extends CoeusDlgWindow {
    
    /** holds the protocol number extracted from protocolInfo */
    String protocolId; 
    
    /** reference object of ProtocolSubmissionInfoBean which will be used to
        get the values from database */
    ProtocolSubmissionInfoBean submissionBean;
    /** character which represents the form is opened in display mode */
    private static final char DISPLAY_MODE = 'D';
    
    /** char which represents the form opened mode */
    private static char functionType = DISPLAY_MODE;
    
    /** reference of DateUtils object which is used for converting dates */
    private DateUtils dtUtils = new DateUtils();
    
    /** reference object of CoeusMessageResources which will be used to get the
       messages to be displayed to the user */
    CoeusMessageResources coeusMessageResources 
            = CoeusMessageResources.getInstance();
    
    
    /**
     * Creates new form <CODE>ProtocolSubmissionDisplayForm</CODE> with the specified
     * parent, title and Protocol Id.
     *
     * @param parent reference to the parent frame.
     * @param title String representing the title to be displayed to the form.
     * @param modal boolean value which specifies that the dialog is modal or
     * not.
     * @param protocolId String representing protocol number whose submission
     * details has to be shown.
     * @throws Exception if unable to get the Protocol Submission details properly
     * from the database.
     */
    public ProtocolSubmissionDisplayForm(java.awt.Frame parent, String title, 
        boolean modal,String protocolId) throws Exception{
        super(parent, title, modal);
        this.protocolId = protocolId;
        initComponents();
        setTableModel();
        getSubmissionDetails();
        setFormData();
        tblReviewers.getTableHeader().setFont(CoeusFontFactory.getLabelFont());
        setTableFormat();
    }
    
    /**
     * This method is used to get the protocol submission details for the given
     * protocol number from the database.
     */
    private void getSubmissionDetails() throws Exception{
        String connectTo = CoeusGuiConstants.CONNECTION_URL+"/protocolSubSrvlt";
        
        // connect to the database and get the ProtocolSubmissionDetails 
        // for the given protocol id
        RequesterBean request = new RequesterBean();
        request.setFunctionType(functionType);
        request.setId(protocolId);
        AppletServletCommunicator comm = new AppletServletCommunicator(
                                                            connectTo, request);
        comm.send();
        ResponderBean response = comm.getResponse();
        if (response.isSuccessfulResponse()) {
            Vector dataObjects = response.getDataObjects();
            
            if(dataObjects != null){
                submissionBean = (ProtocolSubmissionInfoBean)
                    dataObjects.elementAt(0);
                if( (submissionBean == null)  
                        ||  (submissionBean.getProtocolNumber().trim().length() 
                            == 0)){
                                throw new Exception(
                                coeusMessageResources.parseMessageKey(
                                "protoSubmissionFrm_exceptionCode.2008"));
                        
                }
            }else{
                throw new Exception(coeusMessageResources.parseMessageKey(
                    "exceptionCode.keyNotFound"));
            }

        }else{
            throw new Exception(coeusMessageResources.parseMessageKey(
                "exceptionCode.keyNotFound"));
        }
    }
    
    /**
     * This method is used to set the form data by extracting the details from
     * the ProtocolSubmissionInfoBean.
     */
    private void setFormData(){
        if(submissionBean != null){
            txtSubmissionType.setText(submissionBean.getSubmissionTypeDesc());
            txtReviewType.setText(submissionBean.getProtocolReviewTypeDesc());
            txtTypeQualifier.setText(submissionBean.getSubmissionQualTypeDesc());
            txtCommitteeID.setText(submissionBean.getCommitteeId());
            txtCommitteeName.setText(submissionBean.getCommitteeName());
            txtScheduleID.setText(submissionBean.getScheduleId());
            txtScheduleDate.setText(dtUtils.formatDate(
                submissionBean.getScheduleDate().toString(),"dd-MMM-yyyy"));
            
            Vector reviewers = submissionBean.getProtocolReviewer();
            
            if(reviewers != null && reviewers.size() > 0){
                ProtocolReviewerInfoBean reviewerBean;
                Object[][] tableData = new Object[ reviewers.size() ][];
                Object[] tableRowData;
                
                for(int revCount = 0; revCount < reviewers.size(); revCount++){
                    tableRowData = new Object[3];
                    reviewerBean = (ProtocolReviewerInfoBean)
                        reviewers.elementAt(revCount);
                    tableRowData[0] = reviewerBean.getPersonId();
                    tableRowData[1] = reviewerBean.getPersonName();
                    tableRowData[2] = reviewerBean.getReviewerTypeDesc();
                    tableData[revCount] = tableRowData;
                }
                ((DefaultTableModel)tblReviewers.getModel()).setDataVector(
                    tableData , getColumnNames());
                ((DefaultTableModel)
                    tblReviewers.getModel()).fireTableDataChanged();
            }
            
        }
    }
    
    /**
     * Supporting method used to get the column names to be used in the table
     * @return  colNames String[] which consists of column header names.
     */    
    
    private String[] getColumnNames(){
        
        Enumeration enumColumns = tblReviewers.getColumnModel().getColumns();
        String[] colNames = new String[ tblReviewers.getColumnCount() ];
        int colIndex = 0;
        while( enumColumns.hasMoreElements()){
            colNames[ colIndex++ ] = ((TableColumn)
                    enumColumns.nextElement()).getHeaderValue().toString();
        }
        return colNames;
    }
    
        /**
     * This method is used to set the model to the reviewers table with 
     * default values and column names.
     */
    
    private void setTableModel(){
    
        tblReviewers.setModel(new javax.swing.table.DefaultTableModel(
        new Object [][] {},
        new String [] {
            "Id","Name", "Reviewer Type"
        }
        ) {
            boolean[] canEdit = new boolean [] {false, false, false};
            
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
    }

    /**
     * Supporting method used to set the display formats of the table like
     * column lengths etc.
     */
        
    private void setTableFormat(){
        
        tblReviewers.setSelectionMode(
            ListSelectionModel.SINGLE_SELECTION);
        tblReviewers.getTableHeader().setReorderingAllowed(false);
        tblReviewers.getTableHeader().setResizingAllowed(false);

        TableColumn column = tblReviewers.getColumn("Reviewer Type");
        column.setMaxWidth(100);
        column.setMinWidth(100);
        column.setPreferredWidth(100);
        
        column = tblReviewers.getColumn("Id");
        column.setMaxWidth(0);
        column.setMinWidth(0);
        column.setPreferredWidth(0);
        
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        pnlMain = new javax.swing.JPanel();
        lblSubType = new javax.swing.JLabel();
        lblRevType = new javax.swing.JLabel();
        lblQualifier = new javax.swing.JLabel();
        lblCommId = new javax.swing.JLabel();
        lblName = new javax.swing.JLabel();
        lblSchId = new javax.swing.JLabel();
        lblSchDate = new javax.swing.JLabel();
        scrPnReviewers = new javax.swing.JScrollPane();
        tblReviewers = new javax.swing.JTable();
        btnOk = new javax.swing.JButton();
        txtSubmissionType = new edu.mit.coeus.utils.CoeusTextField();
        txtReviewType = new edu.mit.coeus.utils.CoeusTextField();
        txtTypeQualifier = new edu.mit.coeus.utils.CoeusTextField();
        txtCommitteeID = new edu.mit.coeus.utils.CoeusTextField();
        txtCommitteeName = new edu.mit.coeus.utils.CoeusTextField();
        txtScheduleID = new edu.mit.coeus.utils.CoeusTextField();
        txtScheduleDate = new edu.mit.coeus.utils.CoeusTextField();

        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                closeDialog(evt);
            }
        });

        pnlMain.setLayout(new java.awt.GridBagLayout());

        lblSubType.setText("Submission Type :");
        lblSubType.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblSubType.setFont(CoeusFontFactory.getLabelFont());
        lblSubType.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 0, 0);
        pnlMain.add(lblSubType, gridBagConstraints);

        lblRevType.setText("Review Type :");
        lblRevType.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblRevType.setFont(CoeusFontFactory.getLabelFont());
        lblRevType.setPreferredSize(new java.awt.Dimension(120, 20));
        lblRevType.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 0, 0);
        pnlMain.add(lblRevType, gridBagConstraints);

        lblQualifier.setText("Submission Type Qualifier :");
        lblQualifier.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblQualifier.setFont(CoeusFontFactory.getLabelFont());
        lblQualifier.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 0, 0);
        pnlMain.add(lblQualifier, gridBagConstraints);

        lblCommId.setText("Committee ID :");
        lblCommId.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblCommId.setFont(CoeusFontFactory.getLabelFont());
        lblCommId.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 0, 0);
        pnlMain.add(lblCommId, gridBagConstraints);

        lblName.setText("Committee Name :");
        lblName.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblName.setFont(CoeusFontFactory.getLabelFont());
        lblName.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 0, 0);
        pnlMain.add(lblName, gridBagConstraints);

        lblSchId.setText("Schedule ID :");
        lblSchId.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblSchId.setFont(CoeusFontFactory.getLabelFont());
        lblSchId.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 0, 0);
        pnlMain.add(lblSchId, gridBagConstraints);

        lblSchDate.setText("Scheduled Date :");
        lblSchDate.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblSchDate.setFont(CoeusFontFactory.getLabelFont());
        lblSchDate.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 0, 0);
        pnlMain.add(lblSchDate, gridBagConstraints);

        scrPnReviewers.setBorder(new javax.swing.border.TitledBorder(new javax.swing.border.EtchedBorder(), "Selected Reviewers", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, CoeusFontFactory.getLabelFont()));
        scrPnReviewers.setPreferredSize(new java.awt.Dimension(300, 300));
        scrPnReviewers.setMinimumSize(new java.awt.Dimension(300, 300));
        tblReviewers.setFont(CoeusFontFactory.getNormalFont());
        tblReviewers.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblReviewersMouseClicked(evt);
            }
        });

        scrPnReviewers.setViewportView(tblReviewers);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.gridheight = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 0);
        pnlMain.add(scrPnReviewers, gridBagConstraints);

        btnOk.setMnemonic('O');
        btnOk.setFont(CoeusFontFactory.getLabelFont());
        btnOk.setText("OK");
        btnOk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOkActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 0, 10);
        pnlMain.add(btnOk, gridBagConstraints);

        txtSubmissionType.setEditable(false);
        txtSubmissionType.setPreferredSize(new java.awt.Dimension(120, 20));
        txtSubmissionType.setMinimumSize(new java.awt.Dimension(120, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 0, 0);
        pnlMain.add(txtSubmissionType, gridBagConstraints);

        txtReviewType.setEditable(false);
        txtReviewType.setPreferredSize(new java.awt.Dimension(120, 20));
        txtReviewType.setMinimumSize(new java.awt.Dimension(120, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 0, 0);
        pnlMain.add(txtReviewType, gridBagConstraints);

        txtTypeQualifier.setEditable(false);
        txtTypeQualifier.setPreferredSize(new java.awt.Dimension(120, 20));
        txtTypeQualifier.setMinimumSize(new java.awt.Dimension(120, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 0, 0);
        pnlMain.add(txtTypeQualifier, gridBagConstraints);

        txtCommitteeID.setEditable(false);
        txtCommitteeID.setPreferredSize(new java.awt.Dimension(120, 20));
        txtCommitteeID.setMinimumSize(new java.awt.Dimension(120, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 0, 0);
        pnlMain.add(txtCommitteeID, gridBagConstraints);

        txtCommitteeName.setEditable(false);
        txtCommitteeName.setMinimumSize(new java.awt.Dimension(120, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 0, 0);
        pnlMain.add(txtCommitteeName, gridBagConstraints);

        txtScheduleID.setEditable(false);
        txtScheduleID.setPreferredSize(new java.awt.Dimension(120, 20));
        txtScheduleID.setMinimumSize(new java.awt.Dimension(120, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 0, 0);
        pnlMain.add(txtScheduleID, gridBagConstraints);

        txtScheduleDate.setEditable(false);
        txtScheduleDate.setPreferredSize(new java.awt.Dimension(120, 20));
        txtScheduleDate.setMinimumSize(new java.awt.Dimension(120, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 0, 0);
        pnlMain.add(txtScheduleDate, gridBagConstraints);

        getContentPane().add(pnlMain, java.awt.BorderLayout.CENTER);

        pack();
    }//GEN-END:initComponents
    /**
     * This method is used to display the membership details of the reviewer
     * 
     * @param evt MouseEvent to check the click count when the details has to
     * be displayed.
     */
    private void tblReviewersMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblReviewersMouseClicked
        if(evt.getClickCount() == 2){
            int selRow = tblReviewers.getSelectedRow();
            if(selRow != -1){
                String memberId = (String)tblReviewers.getValueAt(selRow,0);
                String committeeId = txtCommitteeID.getText();
                new MemberDetailsForm(committeeId,memberId,'D');
            }
        }
    }//GEN-LAST:event_tblReviewersMouseClicked

    private void btnOkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOkActionPerformed
        setVisible(false);
        dispose();
    }//GEN-LAST:event_btnOkActionPerformed
    
    /** Closes the dialog */
    private void closeDialog(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_closeDialog
        setVisible(false);
        dispose();
    }//GEN-LAST:event_closeDialog
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel lblName;
    private edu.mit.coeus.utils.CoeusTextField txtSubmissionType;
    private javax.swing.JLabel lblRevType;
    private javax.swing.JLabel lblSchId;
    private javax.swing.JLabel lblQualifier;
    private javax.swing.JLabel lblSchDate;
    private edu.mit.coeus.utils.CoeusTextField txtScheduleDate;
    private javax.swing.JPanel pnlMain;
    private edu.mit.coeus.utils.CoeusTextField txtScheduleID;
    private edu.mit.coeus.utils.CoeusTextField txtCommitteeName;
    private javax.swing.JButton btnOk;
    private javax.swing.JTable tblReviewers;
    private javax.swing.JScrollPane scrPnReviewers;
    private javax.swing.JLabel lblSubType;
    private javax.swing.JLabel lblCommId;
    private edu.mit.coeus.utils.CoeusTextField txtCommitteeID;
    private edu.mit.coeus.utils.CoeusTextField txtTypeQualifier;
    private edu.mit.coeus.utils.CoeusTextField txtReviewType;
    // End of variables declaration//GEN-END:variables
    
}
