/** Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

/*
 * MedusaAwardDetailForm.java
 *
 * Created on April 2, 2004, 9:57 AM
 */

package edu.mit.coeus.award.gui;

import javax.swing.*;

import edu.mit.coeus.utils.*;
import edu.mit.coeus.gui.*;
import edu.mit.coeus.award.bean.*;
import edu.mit.coeus.utils.query.Equals;

/**
 *
 * @author  surekhan
 * @modified by Vyjayanthi
 */
public class MedusaAwardDetailForm extends javax.swing.JComponent {
    
    private CoeusAppletMDIForm mdiForm;
    private static final String REQUIRED_DATEFORMAT = "dd-MMM-yyyy";
    private DateUtils dtUtils = new DateUtils();
    private CoeusVector vecAmountInfo = new CoeusVector();;
    private static final String EMPTY_STRING = "";
    private AwardAmountInfoBean awardAmountInfoBean;
    private int index;
    private static final String SOURCE_LIST = "SOURCE_LIST";
    private String source;

    /** Creates new form MedusaAwardDetailForm */
    public MedusaAwardDetailForm(CoeusAppletMDIForm mdiForm) {
        this.mdiForm = mdiForm;
        initComponents();
    }
    
    /** Creates new form MedusaAwardDetailForm */
    public MedusaAwardDetailForm(){
        initComponents();
    }
    
    public void showValues(AwardBean awardBean){

        if(awardBean == null ){
            return;
        }
        vecAmountInfo = awardBean.getAwardAmountInfo();
        if(vecAmountInfo!= null && vecAmountInfo.size() > 0){
            // bug fix #1706 - start
            //awardAmountInfoBean = (AwardAmountInfoBean)vecAmountInfo.get(0);
            String mitAwdNumber  = awardBean.getMitAwardNumber();
            Equals eqMitAwdNo = new Equals("mitAwardNumber",mitAwdNumber);
            CoeusVector vecTemp = vecAmountInfo.filter(eqMitAwdNo);
            if(vecTemp != null && vecTemp.size()> 0){
                awardAmountInfoBean = (AwardAmountInfoBean)vecTemp.get(0);
            }
            // bug fix #1706 - End
        }
        
       AwardHeaderBean  awardHeaderBean = awardBean.getAwardHeaderBean();
       
       String sponsAwardNo=awardBean.getSponsorAwardNumber();
       
       String sponsorNumber = awardBean.getSponsorCode();
       sponsorNumber = (sponsorNumber == null ?EMPTY_STRING:sponsorNumber);
       
       sponsAwardNo = (sponsAwardNo == null ?EMPTY_STRING:sponsAwardNo);
       String actType = awardHeaderBean.getActivityTypeDescription();
       actType = (actType == null ?EMPTY_STRING:actType);
       String awardType = awardHeaderBean.getAwardTypeDescription();
       awardType = (awardType == null ?EMPTY_STRING:awardType);
       
       String accountType=awardHeaderBean.getAccountTypeDescription();
       accountType = (accountType == null ?EMPTY_STRING:accountType);
       
       String arTitle = awardHeaderBean.getTitle();
       arTitle = (arTitle == null ?EMPTY_STRING:arTitle);
       
       String sponsor= awardBean.getSponsorName();
       sponsor = (sponsor == null ?EMPTY_STRING:sponsor);
       
        String status = awardBean.getStatusDescription();
        status = (status == null ? EMPTY_STRING : status );
        
        String apprvdEquip = awardBean.getApprvdEquipmentIndicator();
        apprvdEquip.trim();
        if(apprvdEquip != null && !apprvdEquip.equalsIgnoreCase(EMPTY_STRING)){
            char apprvdEquipmnt;
            apprvdEquipmnt = apprvdEquip.charAt(0);
            if( apprvdEquipmnt == 'P' ){
                chkApprvdEquipmt.setSelected(true);
            } else {
                chkApprvdEquipmt.setSelected(false);
            }
        }
 
        String apprvdSubCon= awardBean.getApprvdSubcontractIndicator();
        if(apprvdSubCon != null && !apprvdSubCon.equalsIgnoreCase(EMPTY_STRING)){
            char apprvdSubContract ;
            apprvdSubContract = apprvdSubCon.charAt(0);
            if( apprvdSubContract == 'P' ){
                chkApprvdSubCon.setSelected(true);
            } else {
                chkApprvdSubCon.setSelected(false);
            }
        }
 
        String apprvdForTrip =awardBean.getApprvdForeignTripIndicator();
        if(apprvdForTrip != null && !apprvdForTrip.equalsIgnoreCase(EMPTY_STRING)){
            char apprvdForeignTrip;
            
            apprvdForeignTrip = apprvdForTrip.charAt(0);
            if( apprvdForeignTrip == 'P' ){
                chkApprvdForeignTrip.setSelected(true);
            } else {
                chkApprvdForeignTrip.setSelected(false);
            }
        }

        String awardNo= awardBean.getMitAwardNumber();
        awardNo = (awardNo == null ?EMPTY_STRING:awardNo);

        String accntNo= awardBean.getAccountNumber();
        accntNo = (accntNo == null ?EMPTY_STRING:accntNo); 
 
        String paymtSchedule =awardBean.getPaymentScheduleIndicator();
       
        if(paymtSchedule != null && !paymtSchedule.equalsIgnoreCase(EMPTY_STRING)){
            char paymtSched;
            paymtSched = paymtSchedule.charAt(0);
            
            if( paymtSched == 'P' ){
                chkPaymtSchedle.setSelected(true);
            } else {
                chkPaymtSchedle.setSelected(false);
            }
        }
        String transferSpnsr= awardBean.getTransferSponsorIndicator();
        if(transferSpnsr != null && !transferSpnsr.equalsIgnoreCase(EMPTY_STRING)){
            char transferSponsor = 'N' ;
            transferSponsor = transferSpnsr.charAt(0);
            if( transferSponsor == 'P' ){
                chkTransferSpnsr.setSelected(true);
            } else {
                chkTransferSpnsr.setSelected(false);
            }
        }
        String costSharing =awardBean.getCostSharingIndicator();
       if(costSharing != null && !costSharing.equalsIgnoreCase(EMPTY_STRING)){
            char costShare = 'N' ;
            costShare = costSharing.charAt(0);
            if( costShare == 'P' ){
               chkCostSharing.setSelected(true);
            } else {
                chkCostSharing.setSelected(false);
            }
        }
        String indirectCost =awardBean.getIdcIndicator();
        if(indirectCost != null && !indirectCost.equalsIgnoreCase(EMPTY_STRING)){
            char idc = 'N' ;
            idc = costSharing.charAt(0); 
            if( idc == 'P' ){
               chkIndirectCost.setSelected(true);
            } else {
              chkIndirectCost.setSelected(false);
            }
        }
        
        
        if(awardBean.getAwardEffectiveDate()!= null ){
            String awardEffctiveDate = awardBean.getAwardEffectiveDate().toString();
            awardEffctiveDate = (awardEffctiveDate == null ? EMPTY_STRING : awardEffctiveDate );
            awardEffctiveDate = dtUtils.formatDate(awardEffctiveDate,REQUIRED_DATEFORMAT);
            lblAwardEffDateValue.setText(awardEffctiveDate );
        }else{
             lblAwardEffDateValue.setText(EMPTY_STRING);
        }        
        
        //Setting the date for AwardEffectiveDate - Added by Chandra
//        
        // Setting the text for Final Expiration date - Added by Chandra
        if(awardAmountInfoBean!= null &&  awardAmountInfoBean.getFinalExpirationDate()!= null ){
            String awardFinalExpDateValue = awardAmountInfoBean.getFinalExpirationDate().toString();
            awardFinalExpDateValue = (awardFinalExpDateValue == null ? EMPTY_STRING : awardFinalExpDateValue );
            awardFinalExpDateValue = dtUtils.formatDate(awardFinalExpDateValue,REQUIRED_DATEFORMAT);
            lblFinalExpDateValue.setText(awardFinalExpDateValue );
        }else{
             lblFinalExpDateValue.setText(EMPTY_STRING);
        }        
        
        // Setting the text for getObligationExpirationDate - Added by Chandra
        if(awardAmountInfoBean!= null &&  awardAmountInfoBean.getObligationExpirationDate()!= null ){
            String obligationExpirationDate = awardAmountInfoBean.getObligationExpirationDate().toString();
            obligationExpirationDate = (obligationExpirationDate == null ? EMPTY_STRING : obligationExpirationDate );
            obligationExpirationDate = dtUtils.formatDate(obligationExpirationDate,REQUIRED_DATEFORMAT);
            lblObligationExpDateValue.setText(obligationExpirationDate );
        }else{
             lblObligationExpDateValue.setText(EMPTY_STRING);
        }        
        
        // Setting the text for getObligationEfectiveDate - Added by Chandra
        if(awardAmountInfoBean!= null &&  awardAmountInfoBean.getCurrentFundEffectiveDate()!= null ){
            String obligationEffectiveDate = awardAmountInfoBean.getCurrentFundEffectiveDate().toString();
            obligationEffectiveDate = (obligationEffectiveDate == null ? EMPTY_STRING : obligationEffectiveDate );
            obligationEffectiveDate = dtUtils.formatDate(obligationEffectiveDate,REQUIRED_DATEFORMAT);
            lblObligationEffDateValue.setText(obligationEffectiveDate );
        }else{
             lblObligationEffDateValue.setText(EMPTY_STRING);
        }        
        
        if(awardAmountInfoBean!= null){
            txtAnticipatedAmount.setText(EMPTY_STRING+awardAmountInfoBean.getAnticipatedTotalAmount());
            txtObliAmountValue.setText(EMPTY_STRING+awardAmountInfoBean.getAmountObligatedToDate());
        }
        
        lblSponsorAwardNoValue.setText(awardBean.getSponsorAwardNumber());// Added by chandra
        
        lblActTypeValue.setText(actType);
        lblAwardTypeValue.setText(awardType);
        lblAccTypeValue.setText(accountType);
        txtArTitle1.setText(arTitle);
        txtArTitle1.setCaretPosition(0);
        lblAwardNoValue.setText(awardNo);
        lblAccntNoValue.setText(accntNo);
        
        String sponsorDetaisl = sponsorNumber + " : " +sponsor;
        lblSponsorValue.setText(sponsorDetaisl);
        lblStatusValue.setText(status);
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        lblSponAwardNumber = new javax.swing.JLabel();
        lblActType = new javax.swing.JLabel();
        lblAwardType = new javax.swing.JLabel();
        lblAccType = new javax.swing.JLabel();
        lblTitle = new javax.swing.JLabel();
        lblSponsor = new javax.swing.JLabel();
        lblAwardNumber = new javax.swing.JLabel();
        lblAccountNumber = new javax.swing.JLabel();
        lblAwardEffDate = new javax.swing.JLabel();
        lblFinalExpDate = new javax.swing.JLabel();
        lblAnticipatedAmount = new javax.swing.JLabel();
        lblObligEffDate = new javax.swing.JLabel();
        lblObligExpDate = new javax.swing.JLabel();
        lblObligatedAmount = new javax.swing.JLabel();
        lblStatus = new javax.swing.JLabel();
        lblSponsorAwardNoValue = new javax.swing.JLabel();
        lblActTypeValue = new javax.swing.JLabel();
        lblAwardTypeValue = new javax.swing.JLabel();
        lblAccTypeValue = new javax.swing.JLabel();
        lblAwardEffDateValue = new javax.swing.JLabel();
        lblFinalExpDateValue = new javax.swing.JLabel();
        lblObligationEffDateValue = new javax.swing.JLabel();
        lblObligationExpDateValue = new javax.swing.JLabel();
        lblStatusValue = new javax.swing.JLabel();
        lblSponsorValue = new javax.swing.JLabel();
        scrPnTitle = new javax.swing.JScrollPane();
        txtArTitle1 = new javax.swing.JTextArea();
        chkPaymtSchedle = new javax.swing.JCheckBox();
        chkTransferSpnsr = new javax.swing.JCheckBox();
        chkCostSharing = new javax.swing.JCheckBox();
        chkIndirectCost = new javax.swing.JCheckBox();
        chkApprvdSubCon = new javax.swing.JCheckBox();
        chkApprvdForeignTrip = new javax.swing.JCheckBox();
        chkApprvdEquipmt = new javax.swing.JCheckBox();
        lblAwardNoValue = new javax.swing.JLabel();
        lblAccntNoValue = new javax.swing.JLabel();
        txtAnticipatedAmount = new edu.mit.coeus.utils.DollarCurrencyTextField();
        txtObliAmountValue = new edu.mit.coeus.utils.DollarCurrencyTextField();
        lblApprvdEquip = new javax.swing.JLabel();
        lblApprvdSubCon = new javax.swing.JLabel();
        lblApprvdForeignTrip = new javax.swing.JLabel();
        lblPaymtSchedle = new javax.swing.JLabel();
        lblTransferSpnsr = new javax.swing.JLabel();
        lblCostSharing = new javax.swing.JLabel();
        lblIndirectCost = new javax.swing.JLabel();

        // JM 1-30-2014 sequence specific information
        lblAwSequence = new javax.swing.JLabel();

        setLayout(new java.awt.GridBagLayout());

        setMinimumSize(new java.awt.Dimension(150, 16));
        setPreferredSize(new java.awt.Dimension(150, 16));
        lblSponAwardNumber.setFont(CoeusFontFactory.getLabelFont());
        lblSponAwardNumber.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblSponAwardNumber.setText("Spon Award No.: ");
        lblSponAwardNumber.setMaximumSize(new java.awt.Dimension(120, 16));
        lblSponAwardNumber.setMinimumSize(new java.awt.Dimension(120, 16));
        lblSponAwardNumber.setPreferredSize(new java.awt.Dimension(120, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 4;
        gridBagConstraints.ipady = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        add(lblSponAwardNumber, gridBagConstraints);

        lblActType.setFont(CoeusFontFactory.getLabelFont());
        lblActType.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblActType.setText("Activity Type: ");
        lblActType.setMinimumSize(new java.awt.Dimension(120, 16));
        lblActType.setPreferredSize(new java.awt.Dimension(120, 16));
        lblActType.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.weightx = 1.0;
        add(lblActType, gridBagConstraints);

        lblAwardType.setFont(CoeusFontFactory.getLabelFont());
        lblAwardType.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblAwardType.setText("Award Type: ");
        lblAwardType.setMinimumSize(new java.awt.Dimension(120, 16));
        lblAwardType.setPreferredSize(new java.awt.Dimension(120, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        add(lblAwardType, gridBagConstraints);

        lblAccType.setFont(CoeusFontFactory.getLabelFont());
        lblAccType.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblAccType.setText("Account Type: ");
        lblAccType.setMinimumSize(new java.awt.Dimension(120, 16));
        lblAccType.setPreferredSize(new java.awt.Dimension(120, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        add(lblAccType, gridBagConstraints);

        lblTitle.setFont(CoeusFontFactory.getLabelFont());
        lblTitle.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblTitle.setText("Title: ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        add(lblTitle, gridBagConstraints);

        lblSponsor.setFont(CoeusFontFactory.getLabelFont());
        lblSponsor.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblSponsor.setText("Sponsor: ");
        lblSponsor.setMinimumSize(new java.awt.Dimension(120, 16));
        lblSponsor.setPreferredSize(new java.awt.Dimension(120, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 0);
        add(lblSponsor, gridBagConstraints);

        lblAwardNumber.setFont(CoeusFontFactory.getLabelFont());
        lblAwardNumber.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblAwardNumber.setText("Award No.: ");
        lblAwardNumber.setToolTipText("");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        add(lblAwardNumber, gridBagConstraints);

        lblAccountNumber.setFont(CoeusFontFactory.getLabelFont());
        lblAccountNumber.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
// JM 6-10-2011 changed Account No to Center No        
        lblAccountNumber.setText("Center No.: "); // JM
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        add(lblAccountNumber, gridBagConstraints);

        lblAwardEffDate.setFont(CoeusFontFactory.getLabelFont());
        lblAwardEffDate.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblAwardEffDate.setText("Award Eff Date: ");
        lblAwardEffDate.setMinimumSize(new java.awt.Dimension(120, 16));
        lblAwardEffDate.setPreferredSize(new java.awt.Dimension(120, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        add(lblAwardEffDate, gridBagConstraints);

        lblFinalExpDate.setFont(CoeusFontFactory.getLabelFont());
        lblFinalExpDate.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblFinalExpDate.setText("Final Exp Date: ");
        lblFinalExpDate.setMinimumSize(new java.awt.Dimension(120, 16));
        lblFinalExpDate.setPreferredSize(new java.awt.Dimension(120, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        add(lblFinalExpDate, gridBagConstraints);

        lblAnticipatedAmount.setFont(CoeusFontFactory.getLabelFont());
        lblAnticipatedAmount.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblAnticipatedAmount.setText("Anticipated Amount: ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 0);
        add(lblAnticipatedAmount, gridBagConstraints);

        lblObligEffDate.setFont(CoeusFontFactory.getLabelFont());
        lblObligEffDate.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblObligEffDate.setText("Obligation Eff Date: ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        add(lblObligEffDate, gridBagConstraints);

        lblObligExpDate.setFont(CoeusFontFactory.getLabelFont());
        lblObligExpDate.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblObligExpDate.setText("Obligation Exp Date: ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        add(lblObligExpDate, gridBagConstraints);

        lblObligatedAmount.setFont(CoeusFontFactory.getLabelFont());
        lblObligatedAmount.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblObligatedAmount.setText("Obligated Amount: ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 0);
        add(lblObligatedAmount, gridBagConstraints);

        lblStatus.setFont(CoeusFontFactory.getLabelFont());
        lblStatus.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblStatus.setText("Status: ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        add(lblStatus, gridBagConstraints);

        lblSponsorAwardNoValue.setFont(CoeusFontFactory.getNormalFont());
        lblSponsorAwardNoValue.setMaximumSize(new java.awt.Dimension(150, 16));
        lblSponsorAwardNoValue.setMinimumSize(new java.awt.Dimension(150, 16));
        lblSponsorAwardNoValue.setPreferredSize(new java.awt.Dimension(150, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        add(lblSponsorAwardNoValue, gridBagConstraints);

        lblActTypeValue.setFont(CoeusFontFactory.getNormalFont());
        lblActTypeValue.setMaximumSize(new java.awt.Dimension(150, 16));
        lblActTypeValue.setMinimumSize(new java.awt.Dimension(150, 16));
        lblActTypeValue.setPreferredSize(new java.awt.Dimension(150, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        add(lblActTypeValue, gridBagConstraints);

        lblAwardTypeValue.setFont(CoeusFontFactory.getNormalFont());
        lblAwardTypeValue.setMaximumSize(new java.awt.Dimension(150, 16));
        lblAwardTypeValue.setMinimumSize(new java.awt.Dimension(150, 16));
        lblAwardTypeValue.setPreferredSize(new java.awt.Dimension(150, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        add(lblAwardTypeValue, gridBagConstraints);

        lblAccTypeValue.setFont(CoeusFontFactory.getNormalFont());
        lblAccTypeValue.setMaximumSize(new java.awt.Dimension(150, 16));
        lblAccTypeValue.setMinimumSize(new java.awt.Dimension(150, 16));
        lblAccTypeValue.setPreferredSize(new java.awt.Dimension(150, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        add(lblAccTypeValue, gridBagConstraints);

        lblAwardEffDateValue.setFont(CoeusFontFactory.getNormalFont());
        lblAwardEffDateValue.setMaximumSize(new java.awt.Dimension(170, 16));
        lblAwardEffDateValue.setMinimumSize(new java.awt.Dimension(150, 16));
        lblAwardEffDateValue.setPreferredSize(new java.awt.Dimension(150, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        add(lblAwardEffDateValue, gridBagConstraints);

        lblFinalExpDateValue.setFont(CoeusFontFactory.getNormalFont());
        lblFinalExpDateValue.setMaximumSize(new java.awt.Dimension(170, 16));
        lblFinalExpDateValue.setMinimumSize(new java.awt.Dimension(150, 16));
        lblFinalExpDateValue.setPreferredSize(new java.awt.Dimension(150, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        add(lblFinalExpDateValue, gridBagConstraints);

        lblObligationEffDateValue.setFont(CoeusFontFactory.getNormalFont());
        lblObligationEffDateValue.setMaximumSize(new java.awt.Dimension(100, 20));
        lblObligationEffDateValue.setMinimumSize(new java.awt.Dimension(100, 20));
        lblObligationEffDateValue.setPreferredSize(new java.awt.Dimension(100, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        add(lblObligationEffDateValue, gridBagConstraints);

        lblObligationExpDateValue.setFont(CoeusFontFactory.getNormalFont());
        lblObligationExpDateValue.setMaximumSize(new java.awt.Dimension(100, 20));
        lblObligationExpDateValue.setMinimumSize(new java.awt.Dimension(100, 20));
        lblObligationExpDateValue.setPreferredSize(new java.awt.Dimension(100, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        add(lblObligationExpDateValue, gridBagConstraints);

        lblStatusValue.setFont(CoeusFontFactory.getNormalFont());
        lblStatusValue.setMinimumSize(new java.awt.Dimension(100, 20));
        lblStatusValue.setPreferredSize(new java.awt.Dimension(100, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        add(lblStatusValue, gridBagConstraints);

        lblSponsorValue.setFont(CoeusFontFactory.getNormalFont());
        lblSponsorValue.setMaximumSize(new java.awt.Dimension(170, 16));
        lblSponsorValue.setMinimumSize(new java.awt.Dimension(150, 16));
        lblSponsorValue.setPreferredSize(new java.awt.Dimension(150, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 0);
        add(lblSponsorValue, gridBagConstraints);

        scrPnTitle.setBorder(new javax.swing.border.EmptyBorder(new java.awt.Insets(1, 1, 1, 1)));
        scrPnTitle.setMinimumSize(new java.awt.Dimension(100, 43));
        scrPnTitle.setPreferredSize(new java.awt.Dimension(100, 43));
        txtArTitle1.setBackground(javax.swing.UIManager.getDefaults().getColor("Panel.background"));
        txtArTitle1.setEditable(false);
        txtArTitle1.setFont(CoeusFontFactory.getNormalFont());
        txtArTitle1.setLineWrap(true);
        txtArTitle1.setWrapStyleWord(true);
        txtArTitle1.setBorder(null);
        txtArTitle1.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        txtArTitle1.setSelectedTextColor(new java.awt.Color(0, 0, 51));
        txtArTitle1.setEnabled(false);
        scrPnTitle.setViewportView(txtArTitle1);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 1.0;
        add(scrPnTitle, gridBagConstraints);

        chkPaymtSchedle.setFont(CoeusFontFactory.getLabelFont());
        chkPaymtSchedle.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        chkPaymtSchedle.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        add(chkPaymtSchedle, gridBagConstraints);

        chkTransferSpnsr.setFont(CoeusFontFactory.getLabelFont());
        chkTransferSpnsr.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        chkTransferSpnsr.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        add(chkTransferSpnsr, gridBagConstraints);

        chkCostSharing.setFont(CoeusFontFactory.getLabelFont());
        chkCostSharing.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        chkCostSharing.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        add(chkCostSharing, gridBagConstraints);

        chkIndirectCost.setFont(CoeusFontFactory.getLabelFont());
        chkIndirectCost.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        chkIndirectCost.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        add(chkIndirectCost, gridBagConstraints);

        chkApprvdSubCon.setFont(CoeusFontFactory.getLabelFont());
        chkApprvdSubCon.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        chkApprvdSubCon.setMaximumSize(new java.awt.Dimension(140, 24));
        chkApprvdSubCon.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        add(chkApprvdSubCon, gridBagConstraints);

        chkApprvdForeignTrip.setFont(CoeusFontFactory.getLabelFont());
        chkApprvdForeignTrip.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        chkApprvdForeignTrip.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        add(chkApprvdForeignTrip, gridBagConstraints);

        chkApprvdEquipmt.setFont(CoeusFontFactory.getLabelFont());
        chkApprvdEquipmt.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        chkApprvdEquipmt.setMaximumSize(new java.awt.Dimension(130, 24));
        chkApprvdEquipmt.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        add(chkApprvdEquipmt, gridBagConstraints);

        lblAwardNoValue.setFont(CoeusFontFactory.getNormalFont());
        lblAwardNoValue.setToolTipText("");
        lblAwardNoValue.setMaximumSize(new java.awt.Dimension(170, 16));
        lblAwardNoValue.setMinimumSize(new java.awt.Dimension(150, 16));
        lblAwardNoValue.setPreferredSize(new java.awt.Dimension(150, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        add(lblAwardNoValue, gridBagConstraints);

        lblAccntNoValue.setFont(CoeusFontFactory.getNormalFont());
        lblAccntNoValue.setMaximumSize(new java.awt.Dimension(170, 16));
        lblAccntNoValue.setMinimumSize(new java.awt.Dimension(150, 16));
        lblAccntNoValue.setPreferredSize(new java.awt.Dimension(150, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        add(lblAccntNoValue, gridBagConstraints);

        txtAnticipatedAmount.setBackground(javax.swing.UIManager.getDefaults().getColor("Panel.background"));
        txtAnticipatedAmount.setBorder(new javax.swing.border.EmptyBorder(new java.awt.Insets(1, 1, 1, 1)));
        txtAnticipatedAmount.setEditable(false);
        txtAnticipatedAmount.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        txtAnticipatedAmount.setFont(CoeusFontFactory.getNormalFont());
        txtAnticipatedAmount.setMinimumSize(new java.awt.Dimension(100, 20));
        txtAnticipatedAmount.setPreferredSize(new java.awt.Dimension(100, 20));
        txtAnticipatedAmount.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 0);
        add(txtAnticipatedAmount, gridBagConstraints);

        txtObliAmountValue.setBackground(javax.swing.UIManager.getDefaults().getColor("Panel.background"));
        txtObliAmountValue.setBorder(new javax.swing.border.EmptyBorder(new java.awt.Insets(1, 1, 1, 1)));
        txtObliAmountValue.setEditable(false);
        txtObliAmountValue.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        txtObliAmountValue.setFont(CoeusFontFactory.getNormalFont());
        txtObliAmountValue.setMinimumSize(new java.awt.Dimension(100, 20));
        txtObliAmountValue.setPreferredSize(new java.awt.Dimension(100, 20));
        txtObliAmountValue.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 0);
        add(txtObliAmountValue, gridBagConstraints);

        lblApprvdEquip.setFont(CoeusFontFactory.getLabelFont());
        lblApprvdEquip.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblApprvdEquip.setText("Apprvd Equipment ");
        lblApprvdEquip.setMinimumSize(new java.awt.Dimension(120, 16));
        lblApprvdEquip.setPreferredSize(new java.awt.Dimension(120, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        add(lblApprvdEquip, gridBagConstraints);

        lblApprvdSubCon.setFont(CoeusFontFactory.getLabelFont());
        lblApprvdSubCon.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblApprvdSubCon.setText("Apprvd Subcontract ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        add(lblApprvdSubCon, gridBagConstraints);

        lblApprvdForeignTrip.setFont(CoeusFontFactory.getLabelFont());
        lblApprvdForeignTrip.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblApprvdForeignTrip.setText("Apprvd Foreign Trip ");
        lblApprvdForeignTrip.setMaximumSize(new java.awt.Dimension(116, 16));
        lblApprvdForeignTrip.setMinimumSize(new java.awt.Dimension(116, 16));
        lblApprvdForeignTrip.setPreferredSize(new java.awt.Dimension(116, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        add(lblApprvdForeignTrip, gridBagConstraints);

        lblPaymtSchedle.setFont(CoeusFontFactory.getLabelFont());
        lblPaymtSchedle.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblPaymtSchedle.setText("Payment Schedule ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        add(lblPaymtSchedle, gridBagConstraints);

        lblTransferSpnsr.setFont(CoeusFontFactory.getLabelFont());
        lblTransferSpnsr.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblTransferSpnsr.setText("Transfer Sponsor ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        add(lblTransferSpnsr, gridBagConstraints);

        lblCostSharing.setFont(CoeusFontFactory.getLabelFont());
        lblCostSharing.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblCostSharing.setText("Cost Sharing ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        add(lblCostSharing, gridBagConstraints);

        lblIndirectCost.setFont(CoeusFontFactory.getLabelFont());
        lblIndirectCost.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblIndirectCost.setText("Indirect Cost ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        add(lblIndirectCost, gridBagConstraints);

    }//GEN-END:initComponents

    /**
     * Getter for property index.
     * @return Value of property index.
     */
    public int getIndex() {
        return index;
    }    
    
    /**
     * Setter for property index.
     * @param index New value of property index.
     */
    public void setIndex(int index) {
        this.index = index;
    }    
    
    /**
     * Getter for property source.
     * @return Value of property source.
     */
    public java.lang.String getSource() {
        return source;
    }
    
    /**
     * Setter for property source.
     * @param source New value of property source.
     */
    public void setSource(java.lang.String source) {
        this.source = source;
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JCheckBox chkApprvdEquipmt;
    public javax.swing.JCheckBox chkApprvdForeignTrip;
    public javax.swing.JCheckBox chkApprvdSubCon;
    public javax.swing.JCheckBox chkCostSharing;
    public javax.swing.JCheckBox chkIndirectCost;
    public javax.swing.JCheckBox chkPaymtSchedle;
    public javax.swing.JCheckBox chkTransferSpnsr;
    public javax.swing.JLabel lblAccType;
    public javax.swing.JLabel lblAccTypeValue;
    public javax.swing.JLabel lblAccntNoValue;
    public javax.swing.JLabel lblAccountNumber;
    public javax.swing.JLabel lblActType;
    public javax.swing.JLabel lblActTypeValue;
    public javax.swing.JLabel lblAnticipatedAmount;
    public javax.swing.JLabel lblApprvdEquip;
    public javax.swing.JLabel lblApprvdForeignTrip;
    public javax.swing.JLabel lblApprvdSubCon;
    public javax.swing.JLabel lblAwardEffDate;
    public javax.swing.JLabel lblAwardEffDateValue;
    public javax.swing.JLabel lblAwardNoValue;
    public javax.swing.JLabel lblAwardNumber;
    javax.swing.JLabel lblAwardType;
    public javax.swing.JLabel lblAwardTypeValue;
    public javax.swing.JLabel lblCostSharing;
    public javax.swing.JLabel lblFinalExpDate;
    public javax.swing.JLabel lblFinalExpDateValue;
    public javax.swing.JLabel lblIndirectCost;
    public javax.swing.JLabel lblObligEffDate;
    public javax.swing.JLabel lblObligExpDate;
    public javax.swing.JLabel lblObligatedAmount;
    public javax.swing.JLabel lblObligationEffDateValue;
    public javax.swing.JLabel lblObligationExpDateValue;
    public javax.swing.JLabel lblPaymtSchedle;
    public javax.swing.JLabel lblSponAwardNumber;
    public javax.swing.JLabel lblSponsor;
    public javax.swing.JLabel lblSponsorAwardNoValue;
    public javax.swing.JLabel lblSponsorValue;
    public javax.swing.JLabel lblStatus;
    public javax.swing.JLabel lblStatusValue;
    public javax.swing.JLabel lblTitle;
    public javax.swing.JLabel lblTransferSpnsr;
    public javax.swing.JScrollPane scrPnTitle;
    public edu.mit.coeus.utils.DollarCurrencyTextField txtAnticipatedAmount;
    public javax.swing.JTextArea txtArTitle1;
    public edu.mit.coeus.utils.DollarCurrencyTextField txtObliAmountValue;
    
    // JM 1-30-2014 sequence specific information
    public javax.swing.JLabel lblAwSequence;
    
    // End of variables declaration//GEN-END:variables
    
}
