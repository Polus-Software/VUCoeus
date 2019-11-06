/*
 * @(#)MedusaPropDetailForm.java 1.0 01/20/04 11:01 AM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.propdev.gui;

import edu.mit.coeus.utils.*;
import edu.mit.coeus.gui.*;
import edu.mit.coeus.gui.CoeusAppletMDIForm;
import edu.mit.coeus.gui.CoeusDlgWindow;
import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.instprop.bean.*;

/**
 * This class provides the implementation for the Medusa Proposal Detail Form
 * in the proposal detail window.
 *
 * @version 1.0 March 17, 2003, 5:00 PM
 * @author  Amit Jadhav
 */
public class MedusaInstPropDetailForm extends javax.swing.JComponent {
    CoeusAppletMDIForm mdiForm;    
    InstituteProposalBean instituteProposalBean;   
    private static final String REQUIRED_DATEFORMAT = "dd-MMM-yyyy";
    private DateUtils dtUtils = new DateUtils();
    
    /** Creates new form MedusaPropDetailForm */
    public MedusaInstPropDetailForm(CoeusAppletMDIForm mdiForm) {
        this.mdiForm = mdiForm;
        initComponents();
        
    }
    
    public void setDataValues(InstituteProposalBean instituteProposalBean){
        
        //set proposal Number
        String proposalNumber = instituteProposalBean.getProposalNumber();
        proposalNumber =  (proposalNumber == null ? "":proposalNumber);               
        lblPropNumValue.setText(proposalNumber );
        
        //set status
        //int statusCode = instituteProposalBean.getStatusCode();
         String status = instituteProposalBean.getStatusDescription();
         status =  (status == null ? "":status);
         lblStatusValue.setText(status);
        
        //set title 0
        String instTitle = instituteProposalBean.getTitle();
        instTitle =  (instTitle == null ? "":instTitle);
        txtArTitle.setText(instTitle);
        txtArTitle.setCaretPosition(0);
        
        //set Proposal type
        //int proposalType = instituteProposalBean.getProposalTypeCode();
         String proposalType = instituteProposalBean.getProposalTypeDescription();
         proposalType =  (proposalType == null ? "":proposalType);
         lblPropTypeValue.setText(proposalType);
         
        
        //set proposal sponsor number
        String proposalSponsorNumber = instituteProposalBean.getSponsorProposalNumber();
        proposalSponsorNumber  = (proposalSponsorNumber == null ? "" : proposalSponsorNumber );
        lblSponsorPropNumvalue.setText(proposalSponsorNumber );
        
        //set Account
        String account = instituteProposalBean.getCurrentAccountNumber(); //MailAccountNumber();
        account = (account == null ? "" : account );
        lblAccountValue.setText(account);
        
        //set activity type 
        String activityType = instituteProposalBean.getProposalActivityTypeDescription();//IpReviewActivityIndicator();
        activityType = (activityType == null ? "" : activityType );
        lblActivityTypeValue.setText(activityType );                
        
        //set NSF code
        String nSFCode = instituteProposalBean.getNsfCodeDescription();
        nSFCode = (nSFCode  == null ? "" :nSFCode  );
        lblNSFCodeValue.setText(nSFCode );
        
        //set Notice of Opp
        String noticeOfOpp = instituteProposalBean.getNoticeOfOpportunityDescription();
        noticeOfOpp = (noticeOfOpp == null ? "" : noticeOfOpp ); 
        lblNoticeOfOppValue.setText(noticeOfOpp);
        
        //set sponsor
        String sponsorCode = instituteProposalBean.getSponsorCode();
        sponsorCode = (sponsorCode == null ? "" : sponsorCode );
        lblSponsorValue.setText(sponsorCode );
        
        //set sponsor name
        String sponsorName = instituteProposalBean.getSponsorName(); //ProposalNumber();
        sponsorName = (sponsorName == null ? "" : sponsorName );
        lblSponsorName.setText(sponsorName );
        
        //set prime sponsor
        String primeSponsor = instituteProposalBean.getPrimeSponsorCode();
        primeSponsor = (primeSponsor  == null ?"" :primeSponsor );
        lblPrimSponsorValue.setText(primeSponsor );
        
        //set prime sponsor name
        String primeSponsorName = instituteProposalBean.getPrimeSponsorName();
        primeSponsorName =(primeSponsorName == null ? "" : primeSponsorName );
        lblPrimSponsorName.setText(primeSponsorName );
        
        //set requestes start date- initial period.
        if(instituteProposalBean.getRequestStartDateInitial() != null ){
            String reqStartDateInitalPeriod = instituteProposalBean.getRequestStartDateInitial().toString();
            reqStartDateInitalPeriod = (reqStartDateInitalPeriod == null ? "" : reqStartDateInitalPeriod );
            reqStartDateInitalPeriod = dtUtils.formatDate(reqStartDateInitalPeriod,REQUIRED_DATEFORMAT);
            lblReqStartDateInitalPeriod.setText(reqStartDateInitalPeriod );
        }else{
             lblReqStartDateInitalPeriod.setText("");
        }        
        
        //set requestes start date- Total period.
        if(instituteProposalBean.getRequestStartDateTotal() != null){
            String reqStartDateTotalPeriod = instituteProposalBean.getRequestStartDateTotal().toString();
            reqStartDateTotalPeriod = (reqStartDateTotalPeriod == null ? "" : reqStartDateTotalPeriod ); 
            reqStartDateTotalPeriod = dtUtils.formatDate(reqStartDateTotalPeriod , REQUIRED_DATEFORMAT);
            lblReqStartDateTotalPeriod.setText(reqStartDateTotalPeriod );
        }else{
            lblReqStartDateTotalPeriod.setText("");
        }
        
        //set requestes end date- initial period.
        if(instituteProposalBean.getRequestEndDateInitial() != null){
            String reqEndDateInitialPeriod = instituteProposalBean.getRequestEndDateInitial().toString();
            reqEndDateInitialPeriod = (reqEndDateInitialPeriod ==null ? "" :reqEndDateInitialPeriod );
            reqEndDateInitialPeriod =dtUtils.formatDate(reqEndDateInitialPeriod ,REQUIRED_DATEFORMAT);
            lblReqEndDateInitialPeriod.setText(reqEndDateInitialPeriod );        
        }else{
            lblReqEndDateInitialPeriod.setText("");        
        }
        
        //set requestes end date- Total period.
        if(instituteProposalBean.getRequestEndDateTotal() != null){
            String reqEndDateTotalPeriod = instituteProposalBean.getRequestEndDateTotal().toString();
            reqEndDateTotalPeriod = (reqEndDateTotalPeriod ==null ? "" : reqEndDateTotalPeriod ); 
            reqEndDateTotalPeriod = dtUtils.formatDate(reqEndDateTotalPeriod ,REQUIRED_DATEFORMAT);            
            lblReqEndDateTotalPeriod.setText(reqEndDateTotalPeriod );
        }else{
            lblReqEndDateTotalPeriod.setText("");
        }

        //set total direct cost - initial period  
        //txtTotalDirectCostInitial
       double totalDirectCostInitialPeriod = instituteProposalBean.getTotalDirectCostInitial();
       totalDirectCostInitialPeriod = (totalDirectCostInitialPeriod == 0.0 ? 0.0 :totalDirectCostInitialPeriod );
        txtTotalDirectCostInitial.setText(""+totalDirectCostInitialPeriod );
        
        //set total direct cost - total period
        //txtDirectCostTotal
        double totalDirectCostTotalPeriod = instituteProposalBean.getTotalDirectCostTotal();
        totalDirectCostTotalPeriod =(totalDirectCostTotalPeriod == 0.0 ? 0.0 : totalDirectCostTotalPeriod );
        //txtTotalCostTotal.setText(""+totalDirectCostTotalPeriod );
        txtDirectCostTotal.setText(""+totalDirectCostTotalPeriod );
        
        //set total indirect cose - initial period
        //txtIndirectCostInitial
        double totalIndirectCostInitialPeriod = instituteProposalBean.getTotalInDirectCostInitial();
        totalIndirectCostInitialPeriod =(totalIndirectCostInitialPeriod == 0.0 ? 0.0 : totalIndirectCostInitialPeriod );
        txtIndirectCostInitial.setText(""+totalIndirectCostInitialPeriod );
        //txtTotalDirectCostInitial.setText(""+totalIndirectCostInitialPeriod );
        
        //set total indirect cost - total period
        //txtIndirectCostTotal
        double  totalIndirectCostTotalPeriod = instituteProposalBean.getTotalInDirectCostTotal();
        totalIndirectCostTotalPeriod = (totalIndirectCostTotalPeriod == 0.0 ? 0.0 : totalIndirectCostTotalPeriod );
        //txtIndirectCostTotal.setText(""+totalIndirectCostTotalPeriod );
        txtIndirectCostTotal.setText(""+totalIndirectCostTotalPeriod );
        
        
        //set total all cost - initial period
        //txtTotalCostInitial
        double totalAllCostInitialPeriod = totalDirectCostInitialPeriod+totalIndirectCostInitialPeriod;
        totalAllCostInitialPeriod = (totalAllCostInitialPeriod == 0.0 ? 0.0 : totalAllCostInitialPeriod );
        txtTotalCostInitial.setText(""+totalAllCostInitialPeriod );
        
        //set total all cost - total period
        //txtTotalCostTotal
        double totalAllCostTotalPeriod = totalDirectCostTotalPeriod+totalIndirectCostTotalPeriod;
        totalAllCostTotalPeriod = (totalAllCostTotalPeriod == 0.0 ? 0.0 :totalAllCostTotalPeriod );
        txtTotalCostTotal.setText(""+totalAllCostTotalPeriod );
        

       char firstChar;  
       String strValue;
        //set Cost Sharing boolean value
       strValue =  instituteProposalBean.getCostSharingIndicator();
        //System.out.println("cost sharing "+strValue);
        if( strValue != null ){
            firstChar = strValue.charAt(0);
            if( firstChar == 'P' ){
                chkCostSharing.setSelected(true);
            }else{
                chkCostSharing.setSelected(false);
            }
        }
        
        //set value to IDC Rates check box
        strValue =  instituteProposalBean.getIdcRateIndicator();
        //System.out.println("cost shareing "+strValue);
        if( strValue != null ){
            firstChar = strValue.charAt(0);
            if( firstChar == 'P' ){
                chkIDCRates.setSelected(true);
            }else{
                chkIDCRates.setSelected(false);
            }
        }
        
        //set value to Special Review check box.
        strValue =  instituteProposalBean.getSpecialReviewIndicator();
        //System.out.println("cost shareing "+strValue);
        if( strValue != null ){
            firstChar = strValue.charAt(0);
            if( firstChar == 'P' ){
                chkSpecialReview.setSelected(true);
            }else{
                chkSpecialReview.setSelected(false);
            }
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

        lblPropNum = new javax.swing.JLabel();
        lblPropNumValue = new javax.swing.JLabel();
        lblStatus = new javax.swing.JLabel();
        lblStatusValue = new javax.swing.JLabel();
        lblTitle = new javax.swing.JLabel();
        scrPnTitle = new javax.swing.JScrollPane();
        txtArTitle = new javax.swing.JTextArea();
        lblPropType = new javax.swing.JLabel();
        lblPropTypeValue = new javax.swing.JLabel();
        lblSponsorPropNum = new javax.swing.JLabel();
        lblSponsorPropNumvalue = new javax.swing.JLabel();
        lblAccount = new javax.swing.JLabel();
        lblAccountValue = new javax.swing.JLabel();
        lblActivityType = new javax.swing.JLabel();
        lblActivityTypeValue = new javax.swing.JLabel();
        lblNSFCode = new javax.swing.JLabel();
        lblNSFCodeValue = new javax.swing.JLabel();
        lblNoticeOfOpp = new javax.swing.JLabel();
        lblNoticeOfOppValue = new javax.swing.JLabel();
        lblSponsor = new javax.swing.JLabel();
        lblSponsorValue = new javax.swing.JLabel();
        lblSponsorName = new javax.swing.JLabel();
        lblPrimSponsor = new javax.swing.JLabel();
        lblPrimSponsorValue = new javax.swing.JLabel();
        lblPrimSponsorName = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        scrPnInitialPeriod = new javax.swing.JScrollPane();
        pnlInitialPeriod = new javax.swing.JPanel();
        lblReqStartDateInitalPeriod = new javax.swing.JLabel();
        lblReqEndDateInitialPeriod = new javax.swing.JLabel();
        txtTotalDirectCostInitial = new edu.mit.coeus.utils.DollarCurrencyTextField();
        txtIndirectCostInitial = new edu.mit.coeus.utils.DollarCurrencyTextField();
        txtTotalCostInitial = new edu.mit.coeus.utils.DollarCurrencyTextField();
        scrPnTotalPeriod = new javax.swing.JScrollPane();
        pnlTotalPeriod = new javax.swing.JPanel();
        lblReqStartDateTotalPeriod = new javax.swing.JLabel();
        lblReqEndDateTotalPeriod = new javax.swing.JLabel();
        txtDirectCostTotal = new edu.mit.coeus.utils.DollarCurrencyTextField();
        txtIndirectCostTotal = new edu.mit.coeus.utils.DollarCurrencyTextField();
        txtTotalCostTotal = new edu.mit.coeus.utils.DollarCurrencyTextField();
        scrPnPeriodTitle = new javax.swing.JScrollPane();
        pnlPeriodTitle = new javax.swing.JPanel();
        lblReqStartDate = new javax.swing.JLabel();
        lblReqEndDate = new javax.swing.JLabel();
        lblTotalDirectCost = new javax.swing.JLabel();
        lblTotalIndirectCost = new javax.swing.JLabel();
        lblTotalAllCost = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        pnlCheckBox = new javax.swing.JPanel();
        lblCostSharing = new javax.swing.JLabel();
        lblIDCRates = new javax.swing.JLabel();
        lblSpecialReview = new javax.swing.JLabel();
        chkCostSharing = new javax.swing.JCheckBox();
        chkIDCRates = new javax.swing.JCheckBox();
        chkSpecialReview = new javax.swing.JCheckBox();

        setLayout(new java.awt.GridBagLayout());

        lblPropNum.setFont(CoeusFontFactory.getLabelFont());
        lblPropNum.setText("Proposal No:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(10, 5, 0, 0);
        add(lblPropNum, gridBagConstraints);

        lblPropNumValue.setFont(CoeusFontFactory.getNormalFont());
        lblPropNumValue.setText(" ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 5, 0, 5);
        add(lblPropNumValue, gridBagConstraints);

        lblStatus.setFont(CoeusFontFactory.getLabelFont());
        lblStatus.setText("Status:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 5);
        add(lblStatus, gridBagConstraints);

        lblStatusValue.setFont(CoeusFontFactory.getNormalFont());
        lblStatusValue.setText(" ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 5);
        add(lblStatusValue, gridBagConstraints);

        lblTitle.setFont(CoeusFontFactory.getLabelFont());
        lblTitle.setText("Title:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 5, 5, 5);
        add(lblTitle, gridBagConstraints);

        scrPnTitle.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        scrPnTitle.setMaximumSize(new java.awt.Dimension(350, 40));
        scrPnTitle.setMinimumSize(new java.awt.Dimension(350, 40));
        scrPnTitle.setPreferredSize(new java.awt.Dimension(350, 40));
        txtArTitle.setBackground((java.awt.Color) javax.swing.UIManager.getDefaults().get("Panel.background"));
        txtArTitle.setDocument(new LimitedPlainDocument(200));
        txtArTitle.setEditable(false);
        txtArTitle.setFont(CoeusFontFactory.getNormalFont());
        txtArTitle.setLineWrap(true);
        txtArTitle.setWrapStyleWord(true);
        txtArTitle.setAlignmentX(0.0F);
        txtArTitle.setAlignmentY(0.0F);
        txtArTitle.setBorder(null);
        txtArTitle.setDisabledTextColor(java.awt.Color.black);
        scrPnTitle.setViewportView(txtArTitle);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 5, 5);
        add(scrPnTitle, gridBagConstraints);

        lblPropType.setFont(CoeusFontFactory.getLabelFont());
        lblPropType.setText("Proposal Type:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        add(lblPropType, gridBagConstraints);

        lblPropTypeValue.setFont(CoeusFontFactory.getNormalFont());
        lblPropTypeValue.setText(" ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 5);
        add(lblPropTypeValue, gridBagConstraints);

        lblSponsorPropNum.setFont(CoeusFontFactory.getLabelFont());
        lblSponsorPropNum.setText("Sponsor Prpsl No:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 5);
        add(lblSponsorPropNum, gridBagConstraints);

        lblSponsorPropNumvalue.setFont(CoeusFontFactory.getNormalFont());
        lblSponsorPropNumvalue.setText(" ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 5);
        add(lblSponsorPropNumvalue, gridBagConstraints);

        lblAccount.setFont(CoeusFontFactory.getLabelFont());
// JM 6-10-2011 changed Account No to Center No       
        lblAccount.setText("Center No:"); // JM 
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        add(lblAccount, gridBagConstraints);

        lblAccountValue.setFont(CoeusFontFactory.getNormalFont());
        lblAccountValue.setText(" ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 5);
        add(lblAccountValue, gridBagConstraints);

        lblActivityType.setFont(CoeusFontFactory.getLabelFont());
        lblActivityType.setText("Activity Type:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 5);
        add(lblActivityType, gridBagConstraints);

        lblActivityTypeValue.setFont(CoeusFontFactory.getNormalFont());
        lblActivityTypeValue.setText(" ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 5);
        add(lblActivityTypeValue, gridBagConstraints);

        lblNSFCode.setFont(CoeusFontFactory.getLabelFont());
        lblNSFCode.setText("NSF Code:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        add(lblNSFCode, gridBagConstraints);

        lblNSFCodeValue.setFont(CoeusFontFactory.getNormalFont());
        lblNSFCodeValue.setText(" ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 5);
        add(lblNSFCodeValue, gridBagConstraints);

        lblNoticeOfOpp.setFont(CoeusFontFactory.getLabelFont());
        lblNoticeOfOpp.setText("Notice Of Opp:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 5);
        add(lblNoticeOfOpp, gridBagConstraints);

        lblNoticeOfOppValue.setFont(CoeusFontFactory.getNormalFont());
        lblNoticeOfOppValue.setText(" ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 5);
        add(lblNoticeOfOppValue, gridBagConstraints);

        lblSponsor.setFont(CoeusFontFactory.getLabelFont());
        lblSponsor.setText("Sponsor:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        add(lblSponsor, gridBagConstraints);

        lblSponsorValue.setFont(CoeusFontFactory.getNormalFont());
        lblSponsorValue.setText(" ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        add(lblSponsorValue, gridBagConstraints);

        lblSponsorName.setFont(CoeusFontFactory.getNormalFont());
        lblSponsorName.setText(" ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 8, 0, 5);
        add(lblSponsorName, gridBagConstraints);

        lblPrimSponsor.setFont(CoeusFontFactory.getLabelFont());
        lblPrimSponsor.setText("Prime Sponsor:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        add(lblPrimSponsor, gridBagConstraints);

        lblPrimSponsorValue.setFont(CoeusFontFactory.getNormalFont());
        lblPrimSponsorValue.setText(" ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        add(lblPrimSponsorValue, gridBagConstraints);

        lblPrimSponsorName.setFont(CoeusFontFactory.getNormalFont());
        lblPrimSponsorName.setText(" ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 8, 0, 5);
        add(lblPrimSponsorName, gridBagConstraints);

        jPanel2.setLayout(new java.awt.GridBagLayout());

        scrPnInitialPeriod.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "Initial Period", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, CoeusFontFactory.getLabelFont(), new java.awt.Color(255, 0, 0)));
        scrPnInitialPeriod.setMaximumSize(new java.awt.Dimension(140, 130));
        scrPnInitialPeriod.setMinimumSize(new java.awt.Dimension(140, 130));
        scrPnInitialPeriod.setPreferredSize(new java.awt.Dimension(140, 130));
        pnlInitialPeriod.setLayout(new java.awt.GridBagLayout());

        pnlInitialPeriod.setMinimumSize(new java.awt.Dimension(130, 102));
        pnlInitialPeriod.setPreferredSize(new java.awt.Dimension(130, 102));
        lblReqStartDateInitalPeriod.setFont(CoeusFontFactory.getNormalFont());
        lblReqStartDateInitalPeriod.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblReqStartDateInitalPeriod.setText("date1");
        lblReqStartDateInitalPeriod.setMinimumSize(new java.awt.Dimension(125, 16));
        lblReqStartDateInitalPeriod.setPreferredSize(new java.awt.Dimension(125, 16));
        lblReqStartDateInitalPeriod.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        pnlInitialPeriod.add(lblReqStartDateInitalPeriod, gridBagConstraints);

        lblReqEndDateInitialPeriod.setFont(CoeusFontFactory.getNormalFont());
        lblReqEndDateInitialPeriod.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblReqEndDateInitialPeriod.setText("test");
        lblReqEndDateInitialPeriod.setMinimumSize(new java.awt.Dimension(125, 16));
        lblReqEndDateInitialPeriod.setPreferredSize(new java.awt.Dimension(125, 16));
        lblReqEndDateInitialPeriod.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        pnlInitialPeriod.add(lblReqEndDateInitialPeriod, gridBagConstraints);

        txtTotalDirectCostInitial.setBackground(javax.swing.UIManager.getDefaults().getColor("Panel.background"));
        txtTotalDirectCostInitial.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        txtTotalDirectCostInitial.setEditable(false);
        txtTotalDirectCostInitial.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTotalDirectCostInitialActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(2, 5, 0, 0);
        pnlInitialPeriod.add(txtTotalDirectCostInitial, gridBagConstraints);

        txtIndirectCostInitial.setBackground(javax.swing.UIManager.getDefaults().getColor("Panel.background"));
        txtIndirectCostInitial.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        txtIndirectCostInitial.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(2, 5, 0, 0);
        pnlInitialPeriod.add(txtIndirectCostInitial, gridBagConstraints);

        txtTotalCostInitial.setBackground(javax.swing.UIManager.getDefaults().getColor("Panel.background"));
        txtTotalCostInitial.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        txtTotalCostInitial.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(2, 5, 0, 0);
        pnlInitialPeriod.add(txtTotalCostInitial, gridBagConstraints);

        scrPnInitialPeriod.setViewportView(pnlInitialPeriod);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        jPanel2.add(scrPnInitialPeriod, gridBagConstraints);

        scrPnTotalPeriod.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "Total Period", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, CoeusFontFactory.getLabelFont(), new java.awt.Color(255, 0, 0)));
        scrPnTotalPeriod.setMaximumSize(new java.awt.Dimension(140, 130));
        scrPnTotalPeriod.setMinimumSize(new java.awt.Dimension(140, 130));
        scrPnTotalPeriod.setPreferredSize(new java.awt.Dimension(140, 130));
        pnlTotalPeriod.setLayout(new java.awt.GridBagLayout());

        pnlTotalPeriod.setMinimumSize(new java.awt.Dimension(3, 100));
        pnlTotalPeriod.setPreferredSize(new java.awt.Dimension(3, 100));
        lblReqStartDateTotalPeriod.setFont(CoeusFontFactory.getNormalFont());
        lblReqStartDateTotalPeriod.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblReqStartDateTotalPeriod.setText("date1");
        lblReqStartDateTotalPeriod.setMaximumSize(new java.awt.Dimension(125, 16));
        lblReqStartDateTotalPeriod.setMinimumSize(new java.awt.Dimension(125, 16));
        lblReqStartDateTotalPeriod.setPreferredSize(new java.awt.Dimension(125, 16));
        lblReqStartDateTotalPeriod.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        pnlTotalPeriod.add(lblReqStartDateTotalPeriod, gridBagConstraints);

        lblReqEndDateTotalPeriod.setFont(CoeusFontFactory.getNormalFont());
        lblReqEndDateTotalPeriod.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblReqEndDateTotalPeriod.setText("date2");
        lblReqEndDateTotalPeriod.setMaximumSize(new java.awt.Dimension(125, 16));
        lblReqEndDateTotalPeriod.setMinimumSize(new java.awt.Dimension(125, 16));
        lblReqEndDateTotalPeriod.setPreferredSize(new java.awt.Dimension(125, 16));
        lblReqEndDateTotalPeriod.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        pnlTotalPeriod.add(lblReqEndDateTotalPeriod, gridBagConstraints);

        txtDirectCostTotal.setBackground(javax.swing.UIManager.getDefaults().getColor("Panel.background"));
        txtDirectCostTotal.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        txtDirectCostTotal.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(2, 5, 0, 0);
        pnlTotalPeriod.add(txtDirectCostTotal, gridBagConstraints);

        txtIndirectCostTotal.setBackground(javax.swing.UIManager.getDefaults().getColor("Panel.background"));
        txtIndirectCostTotal.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        txtIndirectCostTotal.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(2, 5, 0, 0);
        pnlTotalPeriod.add(txtIndirectCostTotal, gridBagConstraints);

        txtTotalCostTotal.setBackground(javax.swing.UIManager.getDefaults().getColor("Panel.background"));
        txtTotalCostTotal.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        txtTotalCostTotal.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(2, 5, 0, 0);
        pnlTotalPeriod.add(txtTotalCostTotal, gridBagConstraints);

        scrPnTotalPeriod.setViewportView(pnlTotalPeriod);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel2.add(scrPnTotalPeriod, gridBagConstraints);

        scrPnPeriodTitle.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        scrPnPeriodTitle.setMaximumSize(new java.awt.Dimension(140, 130));
        scrPnPeriodTitle.setMinimumSize(new java.awt.Dimension(140, 130));
        scrPnPeriodTitle.setPreferredSize(new java.awt.Dimension(140, 130));
        pnlPeriodTitle.setLayout(new java.awt.GridBagLayout());

        lblReqStartDate.setFont(CoeusFontFactory.getLabelFont());
        lblReqStartDate.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblReqStartDate.setText("Requested Start Date:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(14, 5, 0, 3);
        pnlPeriodTitle.add(lblReqStartDate, gridBagConstraints);

        lblReqEndDate.setFont(CoeusFontFactory.getLabelFont());
        lblReqEndDate.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblReqEndDate.setText("Requested End Date:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(8, 5, 0, 3);
        pnlPeriodTitle.add(lblReqEndDate, gridBagConstraints);

        lblTotalDirectCost.setFont(CoeusFontFactory.getLabelFont());
        lblTotalDirectCost.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblTotalDirectCost.setText("Total Direct Cost:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 5, 0, 3);
        pnlPeriodTitle.add(lblTotalDirectCost, gridBagConstraints);

        lblTotalIndirectCost.setFont(CoeusFontFactory.getLabelFont());
        lblTotalIndirectCost.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblTotalIndirectCost.setText("Total Indirect Cost:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(7, 5, 0, 3);
        pnlPeriodTitle.add(lblTotalIndirectCost, gridBagConstraints);

        lblTotalAllCost.setFont(CoeusFontFactory.getLabelFont());
        lblTotalAllCost.setForeground(new java.awt.Color(255, 0, 0));
        lblTotalAllCost.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblTotalAllCost.setText("Total All Cost:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 3);
        pnlPeriodTitle.add(lblTotalAllCost, gridBagConstraints);

        scrPnPeriodTitle.setViewportView(pnlPeriodTitle);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(7, 5, 1, 3);
        jPanel2.add(scrPnPeriodTitle, gridBagConstraints);

        jScrollPane1.setBorder(null);
        jScrollPane1.setMaximumSize(new java.awt.Dimension(150, 100));
        jScrollPane1.setMinimumSize(new java.awt.Dimension(150, 100));
        jScrollPane1.setPreferredSize(new java.awt.Dimension(150, 100));
        pnlCheckBox.setLayout(new java.awt.GridBagLayout());

        pnlCheckBox.setMaximumSize(new java.awt.Dimension(112, 63));
        lblCostSharing.setFont(CoeusFontFactory.getLabelFont());
        lblCostSharing.setText("Cost Sharing");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        pnlCheckBox.add(lblCostSharing, gridBagConstraints);

        lblIDCRates.setFont(CoeusFontFactory.getLabelFont());
        lblIDCRates.setText("IDC Rates");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        pnlCheckBox.add(lblIDCRates, gridBagConstraints);

        lblSpecialReview.setFont(CoeusFontFactory.getLabelFont());
        lblSpecialReview.setText("Special Review");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        pnlCheckBox.add(lblSpecialReview, gridBagConstraints);

        chkCostSharing.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        pnlCheckBox.add(chkCostSharing, gridBagConstraints);

        chkIDCRates.setEnabled(false);
        chkIDCRates.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkIDCRatesActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        pnlCheckBox.add(chkIDCRates, gridBagConstraints);

        chkSpecialReview.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        pnlCheckBox.add(chkSpecialReview, gridBagConstraints);

        jScrollPane1.setViewportView(pnlCheckBox);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        jPanel2.add(jScrollPane1, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.gridwidth = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        add(jPanel2, gridBagConstraints);

    }// </editor-fold>//GEN-END:initComponents

    private void txtTotalDirectCostInitialActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTotalDirectCostInitialActionPerformed
        // Add your handling code here:
    }//GEN-LAST:event_txtTotalDirectCostInitialActionPerformed

    private void chkIDCRatesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkIDCRatesActionPerformed
        // Add your handling code here:
    }//GEN-LAST:event_chkIDCRatesActionPerformed
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox chkCostSharing;
    private javax.swing.JCheckBox chkIDCRates;
    private javax.swing.JCheckBox chkSpecialReview;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblAccount;
    private javax.swing.JLabel lblAccountValue;
    private javax.swing.JLabel lblActivityType;
    private javax.swing.JLabel lblActivityTypeValue;
    private javax.swing.JLabel lblCostSharing;
    private javax.swing.JLabel lblIDCRates;
    private javax.swing.JLabel lblNSFCode;
    private javax.swing.JLabel lblNSFCodeValue;
    private javax.swing.JLabel lblNoticeOfOpp;
    private javax.swing.JLabel lblNoticeOfOppValue;
    private javax.swing.JLabel lblPrimSponsor;
    private javax.swing.JLabel lblPrimSponsorName;
    private javax.swing.JLabel lblPrimSponsorValue;
    private javax.swing.JLabel lblPropNum;
    private javax.swing.JLabel lblPropNumValue;
    private javax.swing.JLabel lblPropType;
    private javax.swing.JLabel lblPropTypeValue;
    private javax.swing.JLabel lblReqEndDate;
    private javax.swing.JLabel lblReqEndDateInitialPeriod;
    private javax.swing.JLabel lblReqEndDateTotalPeriod;
    private javax.swing.JLabel lblReqStartDate;
    private javax.swing.JLabel lblReqStartDateInitalPeriod;
    private javax.swing.JLabel lblReqStartDateTotalPeriod;
    private javax.swing.JLabel lblSpecialReview;
    private javax.swing.JLabel lblSponsor;
    private javax.swing.JLabel lblSponsorName;
    private javax.swing.JLabel lblSponsorPropNum;
    private javax.swing.JLabel lblSponsorPropNumvalue;
    private javax.swing.JLabel lblSponsorValue;
    private javax.swing.JLabel lblStatus;
    private javax.swing.JLabel lblStatusValue;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JLabel lblTotalAllCost;
    private javax.swing.JLabel lblTotalDirectCost;
    private javax.swing.JLabel lblTotalIndirectCost;
    private javax.swing.JPanel pnlCheckBox;
    private javax.swing.JPanel pnlInitialPeriod;
    private javax.swing.JPanel pnlPeriodTitle;
    private javax.swing.JPanel pnlTotalPeriod;
    private javax.swing.JScrollPane scrPnInitialPeriod;
    private javax.swing.JScrollPane scrPnPeriodTitle;
    private javax.swing.JScrollPane scrPnTitle;
    private javax.swing.JScrollPane scrPnTotalPeriod;
    private javax.swing.JTextArea txtArTitle;
    private edu.mit.coeus.utils.DollarCurrencyTextField txtDirectCostTotal;
    private edu.mit.coeus.utils.DollarCurrencyTextField txtIndirectCostInitial;
    private edu.mit.coeus.utils.DollarCurrencyTextField txtIndirectCostTotal;
    private edu.mit.coeus.utils.DollarCurrencyTextField txtTotalCostInitial;
    private edu.mit.coeus.utils.DollarCurrencyTextField txtTotalCostTotal;
    private edu.mit.coeus.utils.DollarCurrencyTextField txtTotalDirectCostInitial;
    // End of variables declaration//GEN-END:variables
    
}
