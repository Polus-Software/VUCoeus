/*
 * @(#)MedusaPropSubContract.java 1.0 01/21/04 5:52 PM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.propdev.gui;

import edu.mit.coeus.utils.*;
import edu.mit.coeus.gui.*;
//import edu.mit.coeus.gui.CoeusAppletMDIForm;
//import edu.mit.coeus.gui.CoeusDlgWindow;
//import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.subcontract.bean.*;

import java.util.*;

/**
 * This class provides the implementation for the Medusa Proposal SunContractor Form
 * in the proposal detail window.
 *
 * @version 1.0 March 17, 2003, 5:00 PM
 * @author  Amit Jadhav
 */
public class MedusaPropSubContract extends javax.swing.JComponent {
    private static final String EMPTY_STRING = "";
    CoeusAppletMDIForm mdiForm;   
    SubContractBean subContractBean;
    private static final String REQUIRED_DATEFORMAT = "dd-MMM-yyyy";
    private DateUtils dtUtils = new DateUtils();
    private CoeusVector vecAmountDetails=null;
    private CoeusVector vecAmountReleased;
    private SubContractAmountInfoBean subContractAmountInfoBean=null;
  //  private SubContractAmountReleased subContractAmountReleased=null;
    
    /** Creates new form MedusaPropSubContract */
    public MedusaPropSubContract(CoeusAppletMDIForm mdiForm) {
        this.mdiForm = mdiForm;        
        initComponents();
    }
    
    public void setDataValues(SubContractBean subContractBean){
        vecAmountDetails = new CoeusVector();        
        vecAmountReleased = new CoeusVector();
        double obligatedAmount = 0.0;
        double anticipatedAmount = 0.0;
        double availableAmount = 0.0;
        double amountReleased = 0.0;
        
        vecAmountDetails = subContractBean.getSubContractAmountInfo();
        vecAmountReleased  = subContractBean.getSubContractAmtReleased();
        
        // Added by chandra
        if(vecAmountDetails != null && vecAmountDetails.size() > 0){
            //Get the last Subcontract Amount bean
            subContractAmountInfoBean = (SubContractAmountInfoBean)vecAmountDetails.get(vecAmountDetails.size() - 1);
            obligatedAmount = subContractAmountInfoBean.getObligatedAmount();
            anticipatedAmount = subContractAmountInfoBean.getAnticipatedAmount();
        }
        
        if(vecAmountReleased != null && vecAmountReleased.size() > 0){
            //subContractAmountReleased = (SubContractAmountReleased)vecAmountReleased.get(0);
            amountReleased = vecAmountReleased.sum("amountReleased");
            if(amountReleased !=0.0){
                txtAmountReleased.setValue(amountReleased);
            }else{
                txtAmountReleased.setValue(0.0);
            }
        }else{
            txtAmountReleased.setValue(0.0);
        }// End chandra
        
        //set sub code - Added by chandra
        String subCode = subContractBean.getSubContractCode();
        subCode = (subCode == null ? EMPTY_STRING : subCode );
        lblSubCodeValue.setText(subCode);
        
        String status = subContractBean.getStatusDescription();
        if(status!=null || !status.equals(EMPTY_STRING)){
            lblStatusValue.setText(status);
        }else{
            lblStatusValue.setText(EMPTY_STRING);
        }// End chandra
        
        String accountNum = subContractBean.getAccountNumber();
        accountNum =(accountNum == null ? EMPTY_STRING : accountNum );
        lblAccountNumValue.setText(accountNum);
        
        String subContractor = subContractBean.getSubContractorName();
        subContractor =(subContractor == null ? EMPTY_STRING : subContractor );
        lblSubContractorValue.setText(subContractor );
        
        if(subContractBean.getStartDate() != null){
            String startDate = subContractBean.getStartDate().toString();
            startDate =(startDate == null ? EMPTY_STRING :startDate );
            startDate = dtUtils.formatDate(startDate,REQUIRED_DATEFORMAT);
            lblStartDateValue.setText(startDate);
            // added by chandra 10/02/04- start
        }else{
            lblStartDateValue.setText(EMPTY_STRING);
        }
        // added by chandra 10/02/04- end
        
        // added by chandra 10/02/04- start
        if(subContractBean.getEndDate() != null){
            String endDate = subContractBean.getEndDate().toString();
            endDate =(endDate == null ? EMPTY_STRING : endDate );
            endDate = dtUtils.formatDate(endDate,REQUIRED_DATEFORMAT);
            lblEndDateValue.setText(endDate );
            // added by chandra 10/02/04- start
        }else{
            lblEndDateValue.setText(EMPTY_STRING);
        }// added by chandra 10/02/04- end
        
        
        
        // Added by chandra
        String subawardType = subContractBean.getSubAwardTypeDescription();
        if(subawardType!=null || !subawardType.equals(EMPTY_STRING)){
            lblSubawardTypeValue.setText(EMPTY_STRING+subawardType );
        }else{
            lblSubawardTypeValue.setText(EMPTY_STRING);
        }
        // End Chandra
        String purchaseAwardNum = subContractBean.getPurchaseOrderNumber();
        purchaseAwardNum = (purchaseAwardNum == null ? EMPTY_STRING : purchaseAwardNum );
        lblPurchaseAwardNumValue.setText(purchaseAwardNum );
        
        String title = subContractBean.getTitle();
        title =(title == null ? EMPTY_STRING : title );
        txtArTitle.setText(title);
        
        // Added by chandra
        String requisitioner = subContractBean.getRequisitionerName();
        requisitioner= (requisitioner == null ? EMPTY_STRING : requisitioner);
        lblRequisitionerValue.setText(requisitioner);
        
        String requisitionerUnit = subContractBean.getRequisitionerUnit();
        
        String requisitionerUnitName =  subContractBean.getRequisitionerUnitName();
        requisitionerUnitName =(requisitionerUnitName == null ? EMPTY_STRING : requisitionerUnitName );
        
        String unitDetails = requisitionerUnit + ", "+requisitionerUnitName;
        lblRequisitionerUnitValue.setText(unitDetails);
        // End Chandra
        String vendorNum = subContractBean.getVendorNumber();
        vendorNum =(vendorNum == null ? EMPTY_STRING : vendorNum );
        lblVendorNumValue.setText(vendorNum);
        
//        if ( subContractBean.getCloseOutDate() != null ){
//            String closeOutDate =  subContractBean.getCloseOutDate().toString();
//            closeOutDate =(closeOutDate == null  ? "" : closeOutDate );
//            closeOutDate =dtUtils.formatDate(closeOutDate, REQUIRED_DATEFORMAT);
//            lblCloseoutDateValue.setText(closeOutDate);
//        }]
        
        // Added by chandra 09/02/2004  bug fix-  start
        if(subContractBean.getCloseOutDate() != null){
            String closeOutDate =  subContractBean.getCloseOutDate().toString();
            closeOutDate =dtUtils.formatDate(closeOutDate, REQUIRED_DATEFORMAT);
            lblCloseoutDateValue.setText(closeOutDate);
        }else{
            lblCloseoutDateValue.setText(EMPTY_STRING);
        }
        // Added by chandra 09/02/2004 -  End
        
        
        String archiveLoc = subContractBean.getArchiveLocation();
        archiveLoc =(archiveLoc == null ? EMPTY_STRING : archiveLoc );
        lblArchiveLocationNum.setText(archiveLoc );
        
        String comments = subContractBean.getComments();
        comments =(comments == null ? EMPTY_STRING : comments );
        txtArComments.setText(comments);
        
        //set Obligated Amount Value
        if(subContractAmountInfoBean  != null && subContractAmountInfoBean.getObligatedAmount() != 0.0){
            txtObligatedAmount.setValue(obligatedAmount);
            
        }
        else{
            txtObligatedAmount.setValue(0.0);
        }
        //set Anticipated Amount Value
        if(subContractAmountInfoBean  != null  && subContractAmountInfoBean.getAnticipatedAmount() != 0.0){
            double anticipatedAmountValue = subContractAmountInfoBean.getAnticipatedAmount();
            anticipatedAmountValue =(anticipatedAmountValue == 0.0 ? 0.0 : anticipatedAmountValue );
            //txtAnticipatedAmountValue.setText("$"+""+anticipatedAmountValue );
            txtAnticipatedAmountValue.setValue(anticipatedAmount);
        }else{
            txtAnticipatedAmountValue.setValue(0.0);
        }
        availableAmount = obligatedAmount - amountReleased;
        if(availableAmount!=0.0){
            txtAvailableAmount.setValue(availableAmount);
        }else{
            txtAvailableAmount.setValue(0.0);
        }
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        lblSubCode = new javax.swing.JLabel();
        lblSubCodeValue = new javax.swing.JLabel();
        lblStatus = new javax.swing.JLabel();
        lblStatusValue = new javax.swing.JLabel();
        lblAccountNum = new javax.swing.JLabel();
        lblAccountNumValue = new javax.swing.JLabel();
        lblSubContractor = new javax.swing.JLabel();
        lblSubContractorValue = new javax.swing.JLabel();
        lblStartDate = new javax.swing.JLabel();
        lblStartDateValue = new javax.swing.JLabel();
        lblEndDate = new javax.swing.JLabel();
        lblEndDateValue = new javax.swing.JLabel();
        lblSubawardType = new javax.swing.JLabel();
        lblPurchaseAwardNum = new javax.swing.JLabel();
        lblSubawardTypeValue = new javax.swing.JLabel();
        lblPurchaseAwardNumValue = new javax.swing.JLabel();
        lblTitle = new javax.swing.JLabel();
        lblRequisitioner = new javax.swing.JLabel();
        lblRequisitionerValue = new javax.swing.JLabel();
        lblRequisitionerUnit = new javax.swing.JLabel();
        lblRequisitionerUnitValue = new javax.swing.JLabel();
        lblVendorNum = new javax.swing.JLabel();
        lblVendorNumValue = new javax.swing.JLabel();
        lblCloseoutDate = new javax.swing.JLabel();
        lblCloseoutDateValue = new javax.swing.JLabel();
        lblArchiveLocation = new javax.swing.JLabel();
        lblArchiveLocationNum = new javax.swing.JLabel();
        lblComments = new javax.swing.JLabel();
        scrPnlAmount = new javax.swing.JScrollPane();
        pnlAmount = new javax.swing.JPanel();
        lblObligatedAmount = new javax.swing.JLabel();
        lblAnticipatedAmount = new javax.swing.JLabel();
        lblAvailableAmount = new javax.swing.JLabel();
        lblAmountReleased = new javax.swing.JLabel();
        txtObligatedAmount = new edu.mit.coeus.utils.DollarCurrencyTextField();
        txtAvailableAmount = new edu.mit.coeus.utils.DollarCurrencyTextField();
        txtAmountReleased = new edu.mit.coeus.utils.DollarCurrencyTextField();
        txtAnticipatedAmountValue = new edu.mit.coeus.utils.DollarCurrencyTextField();
        scrPnComments = new javax.swing.JScrollPane();
        txtArComments = new javax.swing.JTextArea();
        scrPnTitle = new javax.swing.JScrollPane();
        txtArTitle = new javax.swing.JTextArea();

        setLayout(new java.awt.GridBagLayout());

        setMinimumSize(new java.awt.Dimension(600, 300));
        setPreferredSize(new java.awt.Dimension(600, 300));
        lblSubCode.setFont(CoeusFontFactory.getLabelFont());
        lblSubCode.setText("Sub Code:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        add(lblSubCode, gridBagConstraints);

        lblSubCodeValue.setFont(CoeusFontFactory.getNormalFont());
        lblSubCodeValue.setText(" ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.ipadx = 125;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        add(lblSubCodeValue, gridBagConstraints);

        lblStatus.setFont(CoeusFontFactory.getLabelFont());
        lblStatus.setText("Status:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        add(lblStatus, gridBagConstraints);

        lblStatusValue.setFont(CoeusFontFactory.getNormalFont());
        lblStatusValue.setText(" ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        add(lblStatusValue, gridBagConstraints);

        lblAccountNum.setFont(CoeusFontFactory.getLabelFont());
// JM 6-10-2011 changed Account No to Center No   
        lblAccountNum.setText("Center No:"); // JM
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        add(lblAccountNum, gridBagConstraints);

        lblAccountNumValue.setFont(CoeusFontFactory.getNormalFont());
        lblAccountNumValue.setText(" ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        add(lblAccountNumValue, gridBagConstraints);

        lblSubContractor.setFont(CoeusFontFactory.getLabelFont());
        lblSubContractor.setText("Subcontractor:"); // JM
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        add(lblSubContractor, gridBagConstraints);

        lblSubContractorValue.setFont(CoeusFontFactory.getNormalFont());
        lblSubContractorValue.setText(" ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        add(lblSubContractorValue, gridBagConstraints);

        lblStartDate.setFont(CoeusFontFactory.getLabelFont());
        lblStartDate.setText("Start Date:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        add(lblStartDate, gridBagConstraints);

        lblStartDateValue.setFont(CoeusFontFactory.getNormalFont());
        lblStartDateValue.setText(" ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        add(lblStartDateValue, gridBagConstraints);

        lblEndDate.setFont(CoeusFontFactory.getLabelFont());
        lblEndDate.setText("End Date:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        add(lblEndDate, gridBagConstraints);

        lblEndDateValue.setFont(CoeusFontFactory.getNormalFont());
        lblEndDateValue.setText(" ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        add(lblEndDateValue, gridBagConstraints);

        lblSubawardType.setFont(CoeusFontFactory.getLabelFont());
        lblSubawardType.setText("Subaward Type:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        add(lblSubawardType, gridBagConstraints);

        lblPurchaseAwardNum.setFont(CoeusFontFactory.getLabelFont());
        lblPurchaseAwardNum.setText("Purchase order Num:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        add(lblPurchaseAwardNum, gridBagConstraints);

        lblSubawardTypeValue.setFont(CoeusFontFactory.getNormalFont());
        lblSubawardTypeValue.setText(" ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        add(lblSubawardTypeValue, gridBagConstraints);

        lblPurchaseAwardNumValue.setFont(CoeusFontFactory.getNormalFont());
        lblPurchaseAwardNumValue.setText(" ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        add(lblPurchaseAwardNumValue, gridBagConstraints);

        lblTitle.setFont(CoeusFontFactory.getLabelFont());
        lblTitle.setText("Title:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 10, 0);
        add(lblTitle, gridBagConstraints);

        lblRequisitioner.setFont(CoeusFontFactory.getLabelFont());
        lblRequisitioner.setText("Requisitioner:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(10, 5, 0, 0);
        add(lblRequisitioner, gridBagConstraints);

        lblRequisitionerValue.setFont(CoeusFontFactory.getNormalFont());
        lblRequisitionerValue.setText(" ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 5, 0, 0);
        add(lblRequisitionerValue, gridBagConstraints);

        lblRequisitionerUnit.setFont(CoeusFontFactory.getLabelFont());
        lblRequisitionerUnit.setText("Requisitioner Unit:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        add(lblRequisitionerUnit, gridBagConstraints);

        lblRequisitionerUnitValue.setFont(CoeusFontFactory.getNormalFont());
        lblRequisitionerUnitValue.setText(" ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        add(lblRequisitionerUnitValue, gridBagConstraints);

        lblVendorNum.setFont(CoeusFontFactory.getLabelFont());
        lblVendorNum.setText("Vendor Num:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        add(lblVendorNum, gridBagConstraints);

        lblVendorNumValue.setFont(CoeusFontFactory.getNormalFont());
        lblVendorNumValue.setText(" ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        add(lblVendorNumValue, gridBagConstraints);

        lblCloseoutDate.setFont(CoeusFontFactory.getLabelFont());
        lblCloseoutDate.setText("Closeout Date:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        add(lblCloseoutDate, gridBagConstraints);

        lblCloseoutDateValue.setFont(CoeusFontFactory.getNormalFont());
        lblCloseoutDateValue.setText(" ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        add(lblCloseoutDateValue, gridBagConstraints);

        lblArchiveLocation.setFont(CoeusFontFactory.getLabelFont());
        lblArchiveLocation.setText("Archive Location:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        add(lblArchiveLocation, gridBagConstraints);

        lblArchiveLocationNum.setFont(CoeusFontFactory.getNormalFont());
        lblArchiveLocationNum.setText(" ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        add(lblArchiveLocationNum, gridBagConstraints);

        lblComments.setFont(CoeusFontFactory.getLabelFont());
        lblComments.setText("Comments:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 10, 0);
        add(lblComments, gridBagConstraints);

        scrPnlAmount.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0)));
        scrPnlAmount.setMinimumSize(new java.awt.Dimension(600, 60));
        pnlAmount.setLayout(new java.awt.GridBagLayout());

        pnlAmount.setMinimumSize(new java.awt.Dimension(0, 0));
        lblObligatedAmount.setFont(CoeusFontFactory.getLabelFont());
        lblObligatedAmount.setText("Obligated Amount:");
        lblObligatedAmount.setMinimumSize(new java.awt.Dimension(55, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.ipadx = 100;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        pnlAmount.add(lblObligatedAmount, gridBagConstraints);

        lblAnticipatedAmount.setFont(CoeusFontFactory.getLabelFont());
        lblAnticipatedAmount.setText("Anticipated Amount:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 100;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 15, 0, 0);
        pnlAmount.add(lblAnticipatedAmount, gridBagConstraints);

        lblAvailableAmount.setFont(CoeusFontFactory.getLabelFont());
        lblAvailableAmount.setText("Available Amount:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.ipadx = 100;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        pnlAmount.add(lblAvailableAmount, gridBagConstraints);

        lblAmountReleased.setFont(CoeusFontFactory.getLabelFont());
        lblAmountReleased.setText("Amount Released:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.ipadx = 100;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 15, 0, 0);
        pnlAmount.add(lblAmountReleased, gridBagConstraints);

        txtObligatedAmount.setBorder(new javax.swing.border.EmptyBorder(new java.awt.Insets(0, 0, 0, 0)));
        txtObligatedAmount.setEditable(false);
        txtObligatedAmount.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        txtObligatedAmount.setPreferredSize(new java.awt.Dimension(115, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        pnlAmount.add(txtObligatedAmount, gridBagConstraints);

        txtAvailableAmount.setBackground(javax.swing.UIManager.getDefaults().getColor("Panel.background"));
        txtAvailableAmount.setBorder(new javax.swing.border.EmptyBorder(new java.awt.Insets(0, 0, 0, 0)));
        txtAvailableAmount.setEditable(false);
        txtAvailableAmount.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        txtAvailableAmount.setPreferredSize(new java.awt.Dimension(115, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        pnlAmount.add(txtAvailableAmount, gridBagConstraints);

        txtAmountReleased.setBorder(new javax.swing.border.EmptyBorder(new java.awt.Insets(0, 0, 0, 0)));
        txtAmountReleased.setEditable(false);
        txtAmountReleased.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        txtAmountReleased.setPreferredSize(new java.awt.Dimension(115, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        pnlAmount.add(txtAmountReleased, gridBagConstraints);

        txtAnticipatedAmountValue.setBorder(new javax.swing.border.EmptyBorder(new java.awt.Insets(0, 0, 0, 0)));
        txtAnticipatedAmountValue.setEditable(false);
        txtAnticipatedAmountValue.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        txtAnticipatedAmountValue.setPreferredSize(new java.awt.Dimension(115, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        pnlAmount.add(txtAnticipatedAmountValue, gridBagConstraints);

        scrPnlAmount.setViewportView(pnlAmount);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 5, 0, 0);
        add(scrPnlAmount, gridBagConstraints);

        scrPnComments.setBorder(new javax.swing.border.EmptyBorder(new java.awt.Insets(1, 1, 1, 1)));
        scrPnComments.setMinimumSize(new java.awt.Dimension(450, 40));
        scrPnComments.setPreferredSize(new java.awt.Dimension(450, 40));
        txtArComments.setBackground((java.awt.Color) javax.swing.UIManager.getDefaults().get("Panel.background"));
        txtArComments.setDocument(new LimitedPlainDocument(150));
        txtArComments.setEditable(false);
        txtArComments.setFont(CoeusFontFactory.getNormalFont());
        txtArComments.setLineWrap(true);
        txtArComments.setWrapStyleWord(true);
        txtArComments.setAlignmentX(0.0F);
        txtArComments.setAlignmentY(0.0F);
        txtArComments.setBorder(null);
        scrPnComments.setViewportView(txtArComments);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 5, 5);
        add(scrPnComments, gridBagConstraints);

        scrPnTitle.setBorder(new javax.swing.border.EmptyBorder(new java.awt.Insets(1, 1, 1, 1)));
        scrPnTitle.setMinimumSize(new java.awt.Dimension(450, 40));
        scrPnTitle.setPreferredSize(new java.awt.Dimension(450, 40));
        txtArTitle.setBackground((java.awt.Color) javax.swing.UIManager.getDefaults().get("Panel.background"));
        txtArTitle.setDocument(new LimitedPlainDocument(150));
        txtArTitle.setEditable(false);
        txtArTitle.setFont(CoeusFontFactory.getNormalFont());
        txtArTitle.setLineWrap(true);
        txtArTitle.setWrapStyleWord(true);
        txtArTitle.setAlignmentX(0.0F);
        txtArTitle.setAlignmentY(0.0F);
        txtArTitle.setBorder(null);
        scrPnTitle.setViewportView(txtArTitle);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 5, 5);
        add(scrPnTitle, gridBagConstraints);

    }//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel lblAccountNum;
    private javax.swing.JLabel lblAccountNumValue;
    private javax.swing.JLabel lblAmountReleased;
    private javax.swing.JLabel lblAnticipatedAmount;
    private javax.swing.JLabel lblArchiveLocation;
    private javax.swing.JLabel lblArchiveLocationNum;
    private javax.swing.JLabel lblAvailableAmount;
    private javax.swing.JLabel lblCloseoutDate;
    private javax.swing.JLabel lblCloseoutDateValue;
    private javax.swing.JLabel lblComments;
    private javax.swing.JLabel lblEndDate;
    private javax.swing.JLabel lblEndDateValue;
    private javax.swing.JLabel lblObligatedAmount;
    private javax.swing.JLabel lblPurchaseAwardNum;
    private javax.swing.JLabel lblPurchaseAwardNumValue;
    private javax.swing.JLabel lblRequisitioner;
    private javax.swing.JLabel lblRequisitionerUnit;
    private javax.swing.JLabel lblRequisitionerUnitValue;
    private javax.swing.JLabel lblRequisitionerValue;
    private javax.swing.JLabel lblStartDate;
    private javax.swing.JLabel lblStartDateValue;
    private javax.swing.JLabel lblStatus;
    private javax.swing.JLabel lblStatusValue;
    private javax.swing.JLabel lblSubCode;
    private javax.swing.JLabel lblSubCodeValue;
    private javax.swing.JLabel lblSubContractor;
    private javax.swing.JLabel lblSubContractorValue;
    private javax.swing.JLabel lblSubawardType;
    private javax.swing.JLabel lblSubawardTypeValue;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JLabel lblVendorNum;
    private javax.swing.JLabel lblVendorNumValue;
    private javax.swing.JPanel pnlAmount;
    private javax.swing.JScrollPane scrPnComments;
    private javax.swing.JScrollPane scrPnTitle;
    private javax.swing.JScrollPane scrPnlAmount;
    private edu.mit.coeus.utils.DollarCurrencyTextField txtAmountReleased;
    private edu.mit.coeus.utils.DollarCurrencyTextField txtAnticipatedAmountValue;
    private javax.swing.JTextArea txtArComments;
    private javax.swing.JTextArea txtArTitle;
    private edu.mit.coeus.utils.DollarCurrencyTextField txtAvailableAmount;
    private edu.mit.coeus.utils.DollarCurrencyTextField txtObligatedAmount;
    // End of variables declaration//GEN-END:variables
    
}
