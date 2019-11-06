/*
 * MedusaProposalDetailForm.java
 *
 * Created on December 30, 2003, 3:15 PM
 */

package edu.mit.coeus.propdev.gui;

import edu.mit.coeus.utils.*;
import edu.mit.coeus.gui.*;
//import edu.mit.coeus.gui.CoeusAppletMDIForm;
//import edu.mit.coeus.gui.CoeusDlgWindow;
import edu.mit.coeus.propdev.bean.ProposalDevelopmentFormBean;

import java.awt.*;

/**This class is part of the Medusa proposal development, this class takes the proposal development bean and sets
 *the value to the respective fields.
 *
 * @author  raghusv
 */
public class MedusaProposalDetailForm extends javax.swing.JComponent {
    
    /** Creates new form MedusaProposalDetailForm */
    private static CoeusAppletMDIForm mdiForm = null;
    //private String propNumber;
  //  private ProposalDevelopmentFormBean propDevelopmentBean;
    
    private static final String REQUIRED_DATEFORMAT = "dd-MMM-yyyy";
    
    private DateUtils dtUtils = new DateUtils();
     
    public MedusaProposalDetailForm(CoeusAppletMDIForm mdiForm) {
        
        //this.propDevelopmentBean=propDevForm;
        initComponents(); 
        this.mdiForm=mdiForm;
        //showValues(propDevForm);

//        showDevPropDetails();
//        propDevelopmentTxnBean = new ProposalDevelopmentTxnBean();
//        CoeusDlgWindow MPDet =new CoeusDlgWindow(mdiForm,"Inbox",true);
//        try{
//            MPDet.getContentPane().add( this );
//        }catch(Exception e){
//        }
//        MPDet.pack();
//        MPDet.show();
    }

//    //method to do server side interaction and get the proposal development bean given proposal number
//    private void showDevPropDetails(){
//
//        //interact with server side and get the development proposal info from database
////        try{
////            propDevelopmentBean = propDevelopmentTxnBean.getProposalDevelopmentDetails(propNumber);
////        }catch(Exception e){
////            
////        }
//        showValues(propDevelopmentBean);
//    }
    
    
    //method takes up the bean and sets all the respective fields
    public void showValues(ProposalDevelopmentFormBean devProposalBean){
        
        if ( devProposalBean == null){
            return ;
        }
         StringBuffer strBffr = new StringBuffer();
         strBffr.append((String)devProposalBean.getOwnedBy()+ ":");
         strBffr.append((String)devProposalBean.getOwnedByDesc());
         
        
        String proposalNumber = devProposalBean.getProposalNumber();
        proposalNumber =  (proposalNumber == null ? "":proposalNumber);
        //String leadUnit=devProposalBean.getO
        String rawDate=devProposalBean.getRequestStartDateInitial().toString();
        rawDate=(rawDate == null ? "":rawDate);
        String startDate = dtUtils.formatDate(rawDate,REQUIRED_DATEFORMAT);
        
        String propTypeValue = devProposalBean.getProposalTypeDesc();
        propTypeValue = (propTypeValue == null ? "":propTypeValue);
        String nsfCode = devProposalBean.getNsfCodeDescription();
        nsfCode = (nsfCode == null ? "":nsfCode);
        String sponsor= devProposalBean.getSponsorCode();
        sponsor = (sponsor == null ? "":sponsor);
        String primeSpnsr= devProposalBean.getPrimeSponsorCode();
        primeSpnsr = (primeSpnsr == null ? "":primeSpnsr);
        String sponsNo= devProposalBean.getSponsorProposalNumber();
        sponsNo= (sponsNo == null ? "":sponsNo);
        String title = devProposalBean.getProgramAnnouncementTitle();
        title= (title == null ? "":title);
        String titleArea = devProposalBean.getTitle();
        titleArea= (titleArea == null ? "":titleArea);
        String noticeOpp= devProposalBean.getNoticeOfOpportunityDescription();
        noticeOpp= (noticeOpp == null ? "":noticeOpp);

        String narrative = devProposalBean.getNarrativeStatus();
        narrative= (narrative == null ? "":narrative);
        if(narrative.equalsIgnoreCase("I") ){
            narrative = "Incomplete";
        }else if(narrative.equalsIgnoreCase("C")){
            narrative = "Complete";
        }else if(narrative.equalsIgnoreCase("N")){
            narrative = "None";
        }
        
        String status= devProposalBean.getCreationStatusDescription();
        status= (status == null ? "":status);
        
        String rawDate1=devProposalBean.getRequestEndDateInitial().toString();
        rawDate1=(rawDate1 == null ? "":rawDate1);
        String endDate = dtUtils.formatDate(rawDate1,REQUIRED_DATEFORMAT);
            
        String actType= devProposalBean.getProposalActivityTypeDesc();
        actType=(actType == null ? "":actType);
        
        String progmNo= devProposalBean.getProgramAnnouncementNumber();
        progmNo= (progmNo == null ? "":progmNo);
        String budget = devProposalBean.getBudgetStatus();
        budget= (budget == null ? "":budget);
        if(budget.equalsIgnoreCase("I") ){
            budget = "Incomplete";
        }else if(budget.equalsIgnoreCase("C")){
            budget = "Complete";
        }else if(budget.equalsIgnoreCase("N")){
            budget = "None";
        }
            
        lblPropNoValue.setText(proposalNumber);
        lblLeadUnitValue.setText(strBffr.toString());
        //lblLeadUnitValue.setText("");
        lblStartDateValue.setText(startDate);
        lblPropTypeValue.setText(propTypeValue);
        lblNSFCodeValue.setText(nsfCode);
        lblSponsorValue.setText(sponsor);
        lblPrimeSpnsrValue.setText(primeSpnsr);
        lblSponsPropNoValue.setText(sponsNo);
        txtArTitle.setText(titleArea);
        txtArProgramTitle.setText(title);
        lblNoticeOfOppValue.setText(noticeOpp);
        lblNarrValue.setText(narrative);
        lblStatusValue.setText(status);
        lblEndDateValue.setText(endDate);
        lblActTypeValue.setText(actType);
        lblProgNoValue.setText(progmNo);
        lblBudgetValue.setText(budget);
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
        
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        lblProposalNum = new javax.swing.JLabel();
        lblStatus = new javax.swing.JLabel();
        lblLeadUnit = new javax.swing.JLabel();
        lblStartDate = new javax.swing.JLabel();
        lblEndDate = new javax.swing.JLabel();
        lblTitle = new javax.swing.JLabel();
        scrPnTitle = new javax.swing.JScrollPane();
        txtArTitle = new javax.swing.JTextArea();
        lblProposalType = new javax.swing.JLabel();
        lblNSFCode = new javax.swing.JLabel();
        lblSponsorCode = new javax.swing.JLabel();
        lblPrimeSponsor = new javax.swing.JLabel();
        lblSponsorPropNum = new javax.swing.JLabel();
        lblActivityCode = new javax.swing.JLabel();
        lblProgramTitle = new javax.swing.JLabel();
        scrPnProgramTitle = new javax.swing.JScrollPane();
        txtArProgramTitle = new javax.swing.JTextArea();
        lblNoticeOfOpportunity = new javax.swing.JLabel();
        lblProgramNum = new javax.swing.JLabel();
        lblNarrative = new javax.swing.JLabel();
        lblBudget = new javax.swing.JLabel();
        lblPropNoValue = new javax.swing.JLabel();
        lblLeadUnitValue = new javax.swing.JLabel();
        lblStartDateValue = new javax.swing.JLabel();
        lblSponsorValue = new javax.swing.JLabel();
        lblPrimeSpnsrValue = new javax.swing.JLabel();
        lblSponsPropNoValue = new javax.swing.JLabel();
        lblNarrValue = new javax.swing.JLabel();
        lblProgNoValue = new javax.swing.JLabel();
        lblEndDateValue = new javax.swing.JLabel();
        lblStatusValue = new javax.swing.JLabel();
        lblBudgetValue = new javax.swing.JLabel();
        lblNoticeOfOppValue = new javax.swing.JLabel();
        lblPropTypeValue = new javax.swing.JLabel();
        lblNSFCodeValue = new javax.swing.JLabel();
        lblActTypeValue = new javax.swing.JLabel();

        setLayout(new java.awt.GridBagLayout());

        lblProposalNum.setFont(CoeusFontFactory.getLabelFont());
        lblProposalNum.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblProposalNum.setText("Proposal No:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 3, 5);
        add(lblProposalNum, gridBagConstraints);

        lblStatus.setFont(CoeusFontFactory.getLabelFont());
        lblStatus.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblStatus.setText("Status:");
        lblStatus.setToolTipText("Status");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 3, 5);
        add(lblStatus, gridBagConstraints);

        lblLeadUnit.setFont(CoeusFontFactory.getLabelFont());
        lblLeadUnit.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblLeadUnit.setText("Lead Unit:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 3, 5);
        add(lblLeadUnit, gridBagConstraints);

        lblStartDate.setFont(CoeusFontFactory.getLabelFont());
        lblStartDate.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblStartDate.setText("Start Date:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 3, 5);
        add(lblStartDate, gridBagConstraints);

        lblEndDate.setFont(CoeusFontFactory.getLabelFont());
        lblEndDate.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblEndDate.setText("End Date:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 3, 5);
        add(lblEndDate, gridBagConstraints);

        lblTitle.setFont(CoeusFontFactory.getLabelFont());
        lblTitle.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblTitle.setText("Title:");
        lblTitle.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 5, 3, 5);
        add(lblTitle, gridBagConstraints);

        scrPnTitle.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        scrPnTitle.setMaximumSize(new java.awt.Dimension(468, 43));
        scrPnTitle.setMinimumSize(new java.awt.Dimension(468, 43));
        scrPnTitle.setPreferredSize(new java.awt.Dimension(468, 43));
        txtArTitle.setBackground((java.awt.Color) javax.swing.UIManager.getDefaults().get("Panel.background"));
        txtArTitle.setDocument(new LimitedPlainDocument(200));
        txtArTitle.setEditable(false);
        txtArTitle.setFont(CoeusFontFactory.getNormalFont());
        txtArTitle.setLineWrap(true);
        txtArTitle.setWrapStyleWord(true);
        txtArTitle.setBorder(null);
        scrPnTitle.setViewportView(txtArTitle);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 3, 2);
        add(scrPnTitle, gridBagConstraints);

        lblProposalType.setFont(CoeusFontFactory.getLabelFont());
        lblProposalType.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblProposalType.setText("Proposal Type:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 3, 5);
        add(lblProposalType, gridBagConstraints);

        lblNSFCode.setFont(CoeusFontFactory.getLabelFont()
        );
        lblNSFCode.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblNSFCode.setText("NSF Code:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 3, 5);
        add(lblNSFCode, gridBagConstraints);

        lblSponsorCode.setFont(CoeusFontFactory.getLabelFont());
        lblSponsorCode.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblSponsorCode.setText("Sponsor:");
        lblSponsorCode.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 2, 5);
        add(lblSponsorCode, gridBagConstraints);

        lblPrimeSponsor.setFont(CoeusFontFactory.getLabelFont());
        lblPrimeSponsor.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblPrimeSponsor.setText("Prime Sponsor:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 3, 5);
        add(lblPrimeSponsor, gridBagConstraints);

        lblSponsorPropNum.setFont(CoeusFontFactory.getLabelFont());
        lblSponsorPropNum.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblSponsorPropNum.setText("Sponsor Proposal No:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 3, 5);
        add(lblSponsorPropNum, gridBagConstraints);

        lblActivityCode.setFont(CoeusFontFactory.getLabelFont());
        lblActivityCode.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblActivityCode.setText("Activity Type:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 3, 5);
        add(lblActivityCode, gridBagConstraints);

        lblProgramTitle.setFont(CoeusFontFactory.getLabelFont());
        lblProgramTitle.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblProgramTitle.setText("Program Title:");
        lblProgramTitle.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 5, 3, 5);
        add(lblProgramTitle, gridBagConstraints);

        scrPnProgramTitle.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        scrPnProgramTitle.setMaximumSize(new java.awt.Dimension(468, 43));
        scrPnProgramTitle.setMinimumSize(new java.awt.Dimension(468, 43));
        scrPnProgramTitle.setPreferredSize(new java.awt.Dimension(468, 43));
        txtArProgramTitle.setBackground((java.awt.Color) javax.swing.UIManager.getDefaults().get("Panel.background"));
        txtArProgramTitle.setDocument(new LimitedPlainDocument(150));
        txtArProgramTitle.setEditable(false);
        txtArProgramTitle.setFont(CoeusFontFactory.getNormalFont());
        txtArProgramTitle.setLineWrap(true);
        txtArProgramTitle.setWrapStyleWord(true);
        txtArProgramTitle.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        txtArProgramTitle.setRequestFocusEnabled(false);
        scrPnProgramTitle.setViewportView(txtArProgramTitle);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 3, 2);
        add(scrPnProgramTitle, gridBagConstraints);

        lblNoticeOfOpportunity.setFont(CoeusFontFactory.getLabelFont());
        lblNoticeOfOpportunity.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblNoticeOfOpportunity.setText("Notice Of Opportunity:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 3, 5);
        add(lblNoticeOfOpportunity, gridBagConstraints);

        lblProgramNum.setFont(CoeusFontFactory.getLabelFont());
        lblProgramNum.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblProgramNum.setText("Program No:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 3, 5);
        add(lblProgramNum, gridBagConstraints);

        lblNarrative.setFont(CoeusFontFactory.getLabelFont());
        lblNarrative.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblNarrative.setText("Narrative:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 3, 5);
        add(lblNarrative, gridBagConstraints);

        lblBudget.setFont(CoeusFontFactory.getLabelFont());
        lblBudget.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblBudget.setText("Budget:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 3, 5);
        add(lblBudget, gridBagConstraints);

        lblPropNoValue.setText("jLabel1");
        lblPropNoValue.setPreferredSize(new java.awt.Dimension(150, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        add(lblPropNoValue, gridBagConstraints);

        lblLeadUnitValue.setText("jLabel2");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        add(lblLeadUnitValue, gridBagConstraints);

        lblStartDateValue.setText("jLabel3");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        add(lblStartDateValue, gridBagConstraints);

        lblSponsorValue.setText("jLabel4");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        add(lblSponsorValue, gridBagConstraints);

        lblPrimeSpnsrValue.setText("jLabel5");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        add(lblPrimeSpnsrValue, gridBagConstraints);

        lblSponsPropNoValue.setText("jLabel6");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        add(lblSponsPropNoValue, gridBagConstraints);

        lblNarrValue.setText("jLabel7");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 1, 0, 1);
        add(lblNarrValue, gridBagConstraints);

        lblProgNoValue.setText("jLabel8");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        add(lblProgNoValue, gridBagConstraints);

        lblEndDateValue.setText("jLabel9");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        add(lblEndDateValue, gridBagConstraints);

        lblStatusValue.setText("jLabel10");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        add(lblStatusValue, gridBagConstraints);

        lblBudgetValue.setText("jLabel11");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(lblBudgetValue, gridBagConstraints);

        lblNoticeOfOppValue.setText("jLabel12");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        add(lblNoticeOfOppValue, gridBagConstraints);

        lblPropTypeValue.setText("jLabel13");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        add(lblPropTypeValue, gridBagConstraints);

        lblNSFCodeValue.setText("jLabel14");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        add(lblNSFCodeValue, gridBagConstraints);

        lblActTypeValue.setText("jLabel1");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        add(lblActTypeValue, gridBagConstraints);

    }// </editor-fold>//GEN-END:initComponents

    private void txtProposalNumActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtProposalNumActionPerformed
        // Add your handling code here:
    }//GEN-LAST:event_txtProposalNumActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel lblActTypeValue;
    private javax.swing.JLabel lblActivityCode;
    private javax.swing.JLabel lblBudget;
    private javax.swing.JLabel lblBudgetValue;
    private javax.swing.JLabel lblEndDate;
    private javax.swing.JLabel lblEndDateValue;
    private javax.swing.JLabel lblLeadUnit;
    private javax.swing.JLabel lblLeadUnitValue;
    private javax.swing.JLabel lblNSFCode;
    private javax.swing.JLabel lblNSFCodeValue;
    private javax.swing.JLabel lblNarrValue;
    private javax.swing.JLabel lblNarrative;
    private javax.swing.JLabel lblNoticeOfOppValue;
    private javax.swing.JLabel lblNoticeOfOpportunity;
    private javax.swing.JLabel lblPrimeSpnsrValue;
    private javax.swing.JLabel lblPrimeSponsor;
    private javax.swing.JLabel lblProgNoValue;
    private javax.swing.JLabel lblProgramNum;
    private javax.swing.JLabel lblProgramTitle;
    private javax.swing.JLabel lblPropNoValue;
    private javax.swing.JLabel lblPropTypeValue;
    private javax.swing.JLabel lblProposalNum;
    private javax.swing.JLabel lblProposalType;
    private javax.swing.JLabel lblSponsPropNoValue;
    private javax.swing.JLabel lblSponsorCode;
    private javax.swing.JLabel lblSponsorPropNum;
    private javax.swing.JLabel lblSponsorValue;
    private javax.swing.JLabel lblStartDate;
    private javax.swing.JLabel lblStartDateValue;
    private javax.swing.JLabel lblStatus;
    private javax.swing.JLabel lblStatusValue;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JScrollPane scrPnProgramTitle;
    private javax.swing.JScrollPane scrPnTitle;
    private javax.swing.JTextArea txtArProgramTitle;
    private javax.swing.JTextArea txtArTitle;
    // End of variables declaration//GEN-END:variables

}
