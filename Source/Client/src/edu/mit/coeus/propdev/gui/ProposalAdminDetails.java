/*
 * @(#)ProposalAdminDetails.java  1.0  April 22, 2003, 9:54 AM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

/* PMD check performed, and commented unused imports and variables on 14-NOV-2011
 * by Bharati
 */

package edu.mit.coeus.propdev.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
//import java.util.Vector;
import java.util.Hashtable;
import java.sql.Timestamp;

import edu.mit.coeus.gui.*;
import edu.mit.coeus.irb.bean.*;
import edu.mit.coeus.utils.*;
import edu.mit.coeus.propdev.bean.ProposalAdminFormBean;

/** This form is used display the Admin details extracted from
 * ProposalAdminFormBean.
 * @author Senthil
 */
public class ProposalAdminDetails extends javax.swing.JComponent implements TypeConstants{

    private JPanel pnlForm;

    ProposalAdminFormBean proposalAdminFormBean;

    //Variables used in ProposalAdminFormBean
       private String devProposalNumber;
       private String instProposalNumber;
       private int instPropSeqNumber;
       private Timestamp dateSubmittedByDept;
       private Timestamp dateReturnedToDept;
       private Timestamp dateApprovedByOsp;
       private Timestamp dateSubmittedToAgency;
       private Timestamp createDate;
       private String createUser;
       private String signedBy;
       private char submissionType;
       private String userName;
       private String sgnUserName;
       private String createdUserName;
       private DateUtils dtUtils;
       
       private Hashtable subTypeDescription;
       
    /** Creates new form ProposalAdminDetails */
    public ProposalAdminDetails() {
        initComponents();
    }

    /** Creates new form ProposalAdminDetails using the
     * ProposalAdminFormBean as input.
     * @param proposalAdminFormBean proposalAdminFormBean
     *
     */    
    public ProposalAdminDetails(ProposalAdminFormBean proposalAdminFormBean) {

        getFormData(proposalAdminFormBean);
        initComponents();
        setSubTypeDescription();
        setFormData();
    }

    /** This method is called from within the constructor to
     * get values from the ProposalAdminFormBean.
     * @param proposalAdminFormBean proposalAdminFormBean
     */
    
    public void getFormData(ProposalAdminFormBean proposalAdminFormBean) {
        
        devProposalNumber = proposalAdminFormBean.getDevProposalNumber();
        submissionType = proposalAdminFormBean.getSubmissionType();
        instProposalNumber = proposalAdminFormBean.getInstProposalNumber();
        createDate = proposalAdminFormBean.getCreatedDate();
        instPropSeqNumber = proposalAdminFormBean.getInstPropSeqNumber();
        createUser = proposalAdminFormBean.getCreatedUser();
        dateSubmittedByDept = proposalAdminFormBean.getDateSubmittedByDept();
        dateReturnedToDept = proposalAdminFormBean.getDateReturnedToDept();
        dateApprovedByOsp = proposalAdminFormBean.getDateApprovedByOsp();
        dateSubmittedToAgency = proposalAdminFormBean.getDateSubmittedToAgency();
        signedBy = proposalAdminFormBean.getSignedBy();
        sgnUserName = proposalAdminFormBean.getSignedUserName();        
        createdUserName = proposalAdminFormBean.getCreatedUserName();
    }
    
    /** This method is called from within the constructor to
     * get values from the ProposalAdminFormBean.
     */
    
    public void setFormData() {
        
        this.dtUtils = new DateUtils();
        
        //Setting values to the Form Variables
        lblProposalNum1.setText(devProposalNumber);
        lblSubmissionType1.setText((String)subTypeDescription.get(
                 ""+String.valueOf(submissionType)));
        lblProposalNumber1.setText(instProposalNumber);
        if (createDate == null){
            lblCreationDate1.setText("");
        }else{
            lblCreationDate1.setText(dtUtils.formatDate(
                 Utils.convertNull(createDate.toString()), "dd-MMM-yyyy"));
        }
        lblSeqNo1.setText(Integer.toString(instPropSeqNumber));
        lblCreatedBy1.setText(createdUserName);
        if (dateSubmittedByDept == null){
            lblSubmittedForApp1.setText("");
        }else{
            lblSubmittedForApp1.setText(dtUtils.formatDate(
                 Utils.convertNull(dateSubmittedByDept.toString()), "dd-MMM-yyyy"));
        }
        if (dateReturnedToDept == null){
            lblReturnedToDept1.setText("");
        }else{
            lblReturnedToDept1.setText(dtUtils.formatDate(
                 Utils.convertNull(dateReturnedToDept.toString()), "dd-MMM-yyyy"));
        }
        if (dateApprovedByOsp == null){
            lblApprovedByOsp1.setText("");
        }else{
            lblApprovedByOsp1.setText(dtUtils.formatDate(
                 Utils.convertNull(dateApprovedByOsp.toString()), "dd-MMM-yyyy"));
        }
        if (dateSubmittedToAgency == null){
            lblSubToSponsor1.setText("");
        }else{
            lblSubToSponsor1.setText(dtUtils.formatDate(
                 Utils.convertNull(dateSubmittedToAgency.toString()), "dd-MMM-yyyy"));
        }
        lblSignedBy1.setText(sgnUserName);
    }
    
    private void setSubTypeDescription(){
        subTypeDescription = new Hashtable();
        subTypeDescription.put("E","Electronic");
        subTypeDescription.put("P","Paper");
        //Added for COEUSQA-3008 : Proposal Admin Details always displays Submission Type as Paper - start
        //If proposal is S2S submission to Grants.gov(G,G proposal) then set submission type as "S"
        // "S" indicates for proposal is of type System to Sytem
        subTypeDescription.put("S","System to System");
        //Added for COEUSQA-3008 : Proposal Admin Details always displays Submission Type as Paper - end
    }
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */

    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        pnlAdminDetails = new javax.swing.JPanel();
        pnlInstituteProposalContainer1 = new javax.swing.JPanel();
        pnlInstituteProposal1 = new javax.swing.JPanel();
        lblDisplayProposalNo1 = new javax.swing.JLabel();
        lblDisplayCreDate1 = new javax.swing.JLabel();
        lblProposalNumber1 = new javax.swing.JLabel();
        lblCreatedBy1 = new javax.swing.JLabel();
        lblDisplaySeqNo1 = new javax.swing.JLabel();
        lblDisplayCreatedBy1 = new javax.swing.JLabel();
        lblSeqNo1 = new javax.swing.JLabel();
        lblCreationDate1 = new javax.swing.JLabel();
        pnlDatesContainer1 = new javax.swing.JPanel();
        pnlDates1 = new javax.swing.JPanel();
        lblDisplaySubForApp1 = new javax.swing.JLabel();
        lblDisplayRetToDept1 = new javax.swing.JLabel();
        lblSubmittedForApp1 = new javax.swing.JLabel();
        lblSubToSponsor1 = new javax.swing.JLabel();
        lblDisplayAppByOsp1 = new javax.swing.JLabel();
        lblDisplaySubToSponsor1 = new javax.swing.JLabel();
        lblApprovedByOsp1 = new javax.swing.JLabel();
        lblReturnedToDept1 = new javax.swing.JLabel();
        pnlProposalHeader1 = new javax.swing.JPanel();
        lblDisplayProposalNum1 = new javax.swing.JLabel();
        lblProposalNum1 = new javax.swing.JLabel();
        lblDisplaySubmissionType1 = new javax.swing.JLabel();
        lblSubmissionType1 = new javax.swing.JLabel();
        pnlSignedContainer1 = new javax.swing.JPanel();
        lblDisplaySignedBy1 = new javax.swing.JLabel();
        lblSignedBy1 = new javax.swing.JLabel();

        setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        pnlAdminDetails.setLayout(new java.awt.GridBagLayout());

        pnlInstituteProposalContainer1.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        pnlInstituteProposalContainer1.setBorder(new javax.swing.border.TitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0)), "Institute Proposal", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, CoeusFontFactory.getLabelFont()));
        pnlInstituteProposalContainer1.setPreferredSize(new java.awt.Dimension(550, 75));
        pnlInstituteProposal1.setLayout(new java.awt.GridBagLayout());

        pnlInstituteProposal1.setPreferredSize(new java.awt.Dimension(532, 34));
        lblDisplayProposalNo1.setFont(CoeusFontFactory.getLabelFont());
        lblDisplayProposalNo1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblDisplayProposalNo1.setText("Proposal Number:");
        lblDisplayProposalNo1.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        lblDisplayProposalNo1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 35, 0, 0);
        pnlInstituteProposal1.add(lblDisplayProposalNo1, gridBagConstraints);

        lblDisplayCreDate1.setFont(CoeusFontFactory.getLabelFont());
        lblDisplayCreDate1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblDisplayCreDate1.setText("Creation Date:");
        lblDisplayCreDate1.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        lblDisplayCreDate1.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 35, 0, 0);
        pnlInstituteProposal1.add(lblDisplayCreDate1, gridBagConstraints);

        lblProposalNumber1.setFont(CoeusFontFactory.getNormalFont());
        lblProposalNumber1.setText("jLabel1");
        lblProposalNumber1.setMaximumSize(new java.awt.Dimension(102, 17));
        lblProposalNumber1.setMinimumSize(new java.awt.Dimension(102, 17));
        lblProposalNumber1.setPreferredSize(new java.awt.Dimension(102, 17));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        pnlInstituteProposal1.add(lblProposalNumber1, gridBagConstraints);

        lblCreatedBy1.setFont(CoeusFontFactory.getNormalFont());
        lblCreatedBy1.setText("jLabel2");
        lblCreatedBy1.setMaximumSize(new java.awt.Dimension(102, 17));
        lblCreatedBy1.setMinimumSize(new java.awt.Dimension(102, 17));
        lblCreatedBy1.setPreferredSize(new java.awt.Dimension(135, 17));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        pnlInstituteProposal1.add(lblCreatedBy1, gridBagConstraints);

        lblDisplaySeqNo1.setFont(CoeusFontFactory.getLabelFont());
        lblDisplaySeqNo1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblDisplaySeqNo1.setText("Sequence Number:");
        lblDisplaySeqNo1.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        lblDisplaySeqNo1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 36, 0, 0);
        pnlInstituteProposal1.add(lblDisplaySeqNo1, gridBagConstraints);

        lblDisplayCreatedBy1.setFont(CoeusFontFactory.getLabelFont());
        lblDisplayCreatedBy1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblDisplayCreatedBy1.setText("Creation By:");
        lblDisplayCreatedBy1.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        lblDisplayCreatedBy1.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 36, 0, 0);
        pnlInstituteProposal1.add(lblDisplayCreatedBy1, gridBagConstraints);

        lblSeqNo1.setFont(CoeusFontFactory.getNormalFont());
        lblSeqNo1.setText("jLabel1");
        lblSeqNo1.setMaximumSize(new java.awt.Dimension(102, 17));
        lblSeqNo1.setMinimumSize(new java.awt.Dimension(102, 17));
        lblSeqNo1.setPreferredSize(new java.awt.Dimension(135, 17));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        pnlInstituteProposal1.add(lblSeqNo1, gridBagConstraints);

        lblCreationDate1.setFont(CoeusFontFactory.getNormalFont());
        lblCreationDate1.setText("jLabel2");
        lblCreationDate1.setMaximumSize(new java.awt.Dimension(102, 17));
        lblCreationDate1.setMinimumSize(new java.awt.Dimension(102, 17));
        lblCreationDate1.setPreferredSize(new java.awt.Dimension(102, 17));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        pnlInstituteProposal1.add(lblCreationDate1, gridBagConstraints);

        pnlInstituteProposalContainer1.add(pnlInstituteProposal1);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        pnlAdminDetails.add(pnlInstituteProposalContainer1, gridBagConstraints);

        pnlDatesContainer1.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        pnlDatesContainer1.setBorder(new javax.swing.border.TitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0)), "Dates", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, CoeusFontFactory.getLabelFont()));
        pnlDatesContainer1.setPreferredSize(new java.awt.Dimension(550, 75));
        pnlDates1.setLayout(new java.awt.GridBagLayout());

        lblDisplaySubForApp1.setFont(CoeusFontFactory.getLabelFont());
        lblDisplaySubForApp1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblDisplaySubForApp1.setText("Submitted for Approval:");
        lblDisplaySubForApp1.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        lblDisplaySubForApp1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 8, 0, 0);
        pnlDates1.add(lblDisplaySubForApp1, gridBagConstraints);

        lblDisplayRetToDept1.setFont(CoeusFontFactory.getLabelFont());
        lblDisplayRetToDept1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblDisplayRetToDept1.setText("Returned to Dept:");
        lblDisplayRetToDept1.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        lblDisplayRetToDept1.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 8, 0, 0);
        pnlDates1.add(lblDisplayRetToDept1, gridBagConstraints);

        lblSubmittedForApp1.setFont(CoeusFontFactory.getNormalFont());
        lblSubmittedForApp1.setText("jLabel1");
        lblSubmittedForApp1.setMaximumSize(new java.awt.Dimension(102, 17));
        lblSubmittedForApp1.setMinimumSize(new java.awt.Dimension(102, 17));
        lblSubmittedForApp1.setPreferredSize(new java.awt.Dimension(102, 17));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        pnlDates1.add(lblSubmittedForApp1, gridBagConstraints);

        lblSubToSponsor1.setFont(CoeusFontFactory.getNormalFont());
        lblSubToSponsor1.setText("jLabel2");
        lblSubToSponsor1.setMaximumSize(new java.awt.Dimension(102, 17));
        lblSubToSponsor1.setMinimumSize(new java.awt.Dimension(102, 17));
        lblSubToSponsor1.setPreferredSize(new java.awt.Dimension(135, 17));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        pnlDates1.add(lblSubToSponsor1, gridBagConstraints);

        lblDisplayAppByOsp1.setFont(CoeusFontFactory.getLabelFont());
        lblDisplayAppByOsp1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblDisplayAppByOsp1.setText("Approved by Osp:");
        lblDisplayAppByOsp1.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        lblDisplayAppByOsp1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 13, 0, 0);
        pnlDates1.add(lblDisplayAppByOsp1, gridBagConstraints);

        lblDisplaySubToSponsor1.setFont(CoeusFontFactory.getLabelFont());
        lblDisplaySubToSponsor1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblDisplaySubToSponsor1.setText("Submitted to Sponsor:");
        lblDisplaySubToSponsor1.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        lblDisplaySubToSponsor1.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 13, 0, 0);
        pnlDates1.add(lblDisplaySubToSponsor1, gridBagConstraints);

        lblApprovedByOsp1.setFont(CoeusFontFactory.getNormalFont());
        lblApprovedByOsp1.setText("jLabel1");
        lblApprovedByOsp1.setMaximumSize(new java.awt.Dimension(102, 17));
        lblApprovedByOsp1.setMinimumSize(new java.awt.Dimension(102, 17));
        lblApprovedByOsp1.setPreferredSize(new java.awt.Dimension(135, 17));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        pnlDates1.add(lblApprovedByOsp1, gridBagConstraints);

        lblReturnedToDept1.setFont(CoeusFontFactory.getNormalFont());
        lblReturnedToDept1.setText("jLabel2");
        lblReturnedToDept1.setMaximumSize(new java.awt.Dimension(102, 17));
        lblReturnedToDept1.setMinimumSize(new java.awt.Dimension(102, 17));
        lblReturnedToDept1.setPreferredSize(new java.awt.Dimension(102, 17));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        pnlDates1.add(lblReturnedToDept1, gridBagConstraints);

        pnlDatesContainer1.add(pnlDates1);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        pnlAdminDetails.add(pnlDatesContainer1, gridBagConstraints);

        pnlProposalHeader1.setLayout(new java.awt.GridBagLayout());

        lblDisplayProposalNum1.setFont(CoeusFontFactory.getLabelFont());
        lblDisplayProposalNum1.setText("Proposal Number: ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, 46, 0, 0);
        pnlProposalHeader1.add(lblDisplayProposalNum1, gridBagConstraints);

        lblProposalNum1.setFont(CoeusFontFactory.getNormalFont());
        lblProposalNum1.setText("jLabel2");
        lblProposalNum1.setPreferredSize(new java.awt.Dimension(122, 17));
        pnlProposalHeader1.add(lblProposalNum1, new java.awt.GridBagConstraints());

        lblDisplaySubmissionType1.setFont(CoeusFontFactory.getLabelFont());
        lblDisplaySubmissionType1.setText("Submission Type: ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, 28, 0, 0);
        pnlProposalHeader1.add(lblDisplaySubmissionType1, gridBagConstraints);

        lblSubmissionType1.setFont(CoeusFontFactory.getNormalFont());
        lblSubmissionType1.setText("jLabel4");
        lblSubmissionType1.setPreferredSize(new java.awt.Dimension(102, 17));
        pnlProposalHeader1.add(lblSubmissionType1, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        pnlAdminDetails.add(pnlProposalHeader1, gridBagConstraints);

        pnlSignedContainer1.setLayout(new java.awt.GridBagLayout());

        lblDisplaySignedBy1.setFont(CoeusFontFactory.getLabelFont());
        lblDisplaySignedBy1.setText("Signed By: ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 94, 0, 0);
        pnlSignedContainer1.add(lblDisplaySignedBy1, gridBagConstraints);

        lblSignedBy1.setFont(CoeusFontFactory.getNormalFont());
        lblSignedBy1.setText("jLabel6");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        pnlSignedContainer1.add(lblSignedBy1, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        pnlAdminDetails.add(pnlSignedContainer1, gridBagConstraints);

        add(pnlAdminDetails);

    }//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel lblDisplaySubmissionType1;
    private javax.swing.JLabel lblDisplayCreatedBy1;
    private javax.swing.JLabel lblDisplaySeqNo1;
    private javax.swing.JLabel lblSubToSponsor1;
    private javax.swing.JLabel lblDisplayRetToDept1;
    private javax.swing.JPanel pnlInstituteProposal1;
    private javax.swing.JLabel lblSubmissionType1;
    private javax.swing.JLabel lblSubmittedForApp1;
    private javax.swing.JLabel lblDisplaySignedBy1;
    private javax.swing.JLabel lblDisplayProposalNo1;
    private javax.swing.JLabel lblReturnedToDept1;
    private javax.swing.JLabel lblSignedBy1;
    private javax.swing.JPanel pnlDatesContainer1;
    private javax.swing.JPanel pnlProposalHeader1;
    private javax.swing.JLabel lblProposalNumber1;
    private javax.swing.JPanel pnlSignedContainer1;
    private javax.swing.JPanel pnlDates1;
    private javax.swing.JLabel lblDisplayCreDate1;
    private javax.swing.JLabel lblProposalNum1;
    private javax.swing.JLabel lblDisplayAppByOsp1;
    private javax.swing.JLabel lblCreationDate1;
    private javax.swing.JLabel lblDisplayProposalNum1;
    private javax.swing.JLabel lblApprovedByOsp1;
    private javax.swing.JPanel pnlInstituteProposalContainer1;
    private javax.swing.JLabel lblCreatedBy1;
    private javax.swing.JLabel lblSeqNo1;
    private javax.swing.JLabel lblDisplaySubToSponsor1;
    private javax.swing.JLabel lblDisplaySubForApp1;
    private javax.swing.JPanel pnlAdminDetails;
    // End of variables declaration//GEN-END:variables

}
