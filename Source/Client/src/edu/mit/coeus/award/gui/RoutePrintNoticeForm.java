/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * RoutePrintNoticeForm.java
 *
 * Created on Dec 14, 2011, 11:27:24 AM
 */
package edu.mit.coeus.award.gui;

import edu.mit.coeus.award.bean.AwardBean;
import edu.mit.coeus.award.bean.AwardDocumentRouteBean;
import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.gui.CoeusDlgWindow;
import edu.mit.coeus.routing.gui.RoutingForm;
import edu.mit.coeus.utils.AppletServletCommunicator;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.utils.ModuleConstants;
import java.awt.Cursor;
import java.awt.event.ActionListener;
import java.util.Hashtable;
import java.util.Vector;
import javax.swing.JOptionPane;

/**
 *
 * @author midhunmk
 */
public class RoutePrintNoticeForm extends javax.swing.JPanel implements ActionListener{
    
    private static final String DLG_TITLE = "Route Award Notice";
    //private static final String SERVLET = "/AwardMaintenanceServlet";
    private static final String SERVLET = "/ReportConfigServlet";
    
    private static final String COULD_NOT_CONTACT_SERVER = "Could not contact server";
    private static final String ADDRESS_LIST = "ADDRESS_LIST";
    private static final String CLOSEOUT = "CLOSEOUT";
    private static final String COMMENTS = "COMMENTS"; 
    private static final String COST_SHARING = "COST_SHARING";
    private static final String EQUIPMENT = "EQUIPMENT";
    private static final String FLOW_THRU = "FLOW_THRU";
    private static final String FOREIGN_TRAVEL = "FOREIGN_TRAVEL";
    private static final String HIERARCHY_INFO = "HIERARCHY_INFO";
    private static final String INDIRECT_COST = "INDIRECT_COST";
    private static final String PAYEMENT = "PAYEMENT";
    private static final String PROPOSAL_DUE = "PROPOSAL_DUE";
    private static final String REPORTING = "REPORTING";
    private static final String SIGNATURE_REQUIRED ="SIGNATURE_REQUIRED";
    private static final String SCIENCE_CODE = "SCIENCE_CODE";
    private static final String SPECIAL_REVIEW = "SPECIAL_REVIEW";
    private static final String SUBCONTRACT = "SUBCONTRACT";
    private static final String TECH_REPORTING = "TECH_REPORTING";
    private static final String TERMS = "TERMS";
    //start csse 2010
    private static final String OTHER_DATA = "OTHER_DATA";
    //end case 2010
    //Added for Case 3122 - Award Notice Enhancement - Start
    private static final String FUNDING_SUMMARY = "FUNDING_SUMMARY";
     //Added for Case 3122 - Award Notice Enhancement - End
    
    private CoeusDlgWindow dlgPrintNotice;
    private AwardBean awardBean;
    
    private static final char AWARD_NOTICE = 'a';
    private static final char SIGNATURE_REQUIRED_REQUEST = 'r' ;
    private static final String AWARD_BEAN="AWARD_BEAN";
    //COEUSQA 2111 STARTS
    private static final String AWARD_DOCUMENT_BEAN="AWARD_DOCUMENT_BEAN";
    private static final int AWARD_NOTICE_CODE=1;
    private Integer signatureRequired=0;
    AwardDocumentRouteBean awardDocumentRouteBean;
    private static final char REMOVE_AWARD_DOCUMENT='G';
    private static final String SUBMISSION_SERVLET="/AwardSubmissionServlet";
    private static final char SUBMIT_FOR_APPROVE='A';
    private static final int AWARD_ROUTING_STATUS_CODE=1;
    //COEUSQA 2111 ENDS
    /** Creates new form RoutePrintNoticeForm */
    public RoutePrintNoticeForm() {
        
        initComponents();
        registerComponents();
    }
 public void registerComponents() {
        btnClose.addActionListener(this);
        btnDeselectAll.addActionListener(this);
        btnPrint.addActionListener(this);
        btnSelectAll.addActionListener(this);
        
    }
    
    private void enableComponents() {
        boolean signatureRequired = isSignatureRequired();
        chkSignatureReq.setSelected(signatureRequired);
        chkAddressList.setSelected(true);
        chkCloseout.setSelected(true);
        chkComments.setSelected(true);
        chkCostSharing.setSelected(true);
        chkEquipment.setSelected(true);
        chkFlowThru.setSelected(true);
        chkForeignTravel.setSelected(true);
        chkHierarchyInfo.setSelected(false);
        chkIndirectCost.setSelected(true);
        chkPayment.setSelected(true);
        chkProposalDue.setSelected(true);
        chkReporting.setSelected(true);
        chkScienceCode.setSelected(true);
        chkSpecialReview.setSelected(true);
        chkSubcontract.setSelected(true);
        chkTechReporting.setSelected(true);
        chkTerms.setSelected(true);
        //start csse 2010
        chkOtherData.setSelected(true);
        //end case 2010
         //Added for Case 3122 - Award Notice Enhancement - Start
        chkFundingSummary.setSelected(false);
         //Added for Case 3122 - Award Notice Enhancement - End
    }
    
    
    public void display() {
        enableComponents();
        dlgPrintNotice = new CoeusDlgWindow(CoeusGuiConstants.getMDIForm(), true);
        dlgPrintNotice.setTitle(DLG_TITLE);
        dlgPrintNotice.getContentPane().add(this);
        dlgPrintNotice.setSize(690, 320);
        dlgPrintNotice.setResizable(false);
        dlgPrintNotice.setLocation(CoeusDlgWindow.CENTER);
        dlgPrintNotice.setVisible(true);
    }
    
    private boolean getPDFUrl() {
        String userMessage="Are you sure you would like to submit award notice for routing?";
        edu.mit.coeus.award.bean.AwardDocumentRouteBean adrb = awardBean.getLatestAwardDocumentRouteBean();
        if((adrb!=null)&&(adrb.getRoutingDocumentNumber()>0)&&(adrb.getRoutingStatusCode()==1)){
        userMessage="Another document approval is already in progress . Do you want to submit new one?";
        }
        int confirm=CoeusOptionPane.showQuestionDialog(userMessage+"     ",CoeusOptionPane.OPTION_YES_NO,CoeusOptionPane.DEFAULT_YES);
        if(confirm==JOptionPane.YES_OPTION){
            
       
        RequesterBean requesterBean = new RequesterBean();
        requesterBean.setFunctionType(AWARD_NOTICE);
        Hashtable htDataToSend = getSelectedItems();
        htDataToSend.put(AWARD_BEAN, awardBean);
        htDataToSend.put(AWARD_DOCUMENT_BEAN,LoadAwardDocumentBean());
        requesterBean.setDataObject(htDataToSend);
        
        //For Streaming
        htDataToSend.put("LOGGED_IN_USER", CoeusGuiConstants.getMDIForm().getUserId());
        htDataToSend.put("REPORT_TYPE","AwardNotice");
        requesterBean.setId("Award/AwardNotice");
        requesterBean.setFunctionType('A');
        //For Streaming
        
        AppletServletCommunicator appletServletCommunicator = new AppletServletCommunicator(CoeusGuiConstants.CONNECTION_URL + SERVLET, requesterBean);
        appletServletCommunicator.setRequest(requesterBean);
        appletServletCommunicator.send();
        ResponderBean responderBean = appletServletCommunicator.getResponse();
        if(responderBean == null) {
            //Could not contact server.
            CoeusOptionPane.showErrorDialog(COULD_NOT_CONTACT_SERVER);
            return false;
        }
        if (!responderBean.isSuccessfulResponse()) {
            CoeusOptionPane.showErrorDialog(responderBean.getMessage());
            return false;
        }
        //document is saved to the data table
        awardDocumentRouteBean=(AwardDocumentRouteBean)responderBean.getDataObject();
        //checking for the presence of map.
        boolean mapsFound=false;
         mapsFound = doSubmitApproveAction();
         if(mapsFound){
             //setting the base bean with the details
        awardDocumentRouteBean.setRoutingStatusCode(AWARD_ROUTING_STATUS_CODE);
        awardDocumentRouteBean.setRoutingApprovalSeq(awardDocumentRouteBean.getRoutingDocumentNumber());
        awardBean.setLatestAwardDocumentRouteBean(awardDocumentRouteBean);
      
                    CoeusOptionPane.showInfoDialog("Award Notice submitted for approval routing.     ") ;
                     RoutingForm routingForm = new RoutingForm(
                                    awardDocumentRouteBean,
                                    ModuleConstants.AWARD_MODULE_CODE,awardDocumentRouteBean.getMitAwardNumber(),
                                    awardDocumentRouteBean.getRoutingDocumentNumber(),
                                    awardBean.getLeadUnitNumber(), false,true);
                     //routingForm.setShowRouting(true);
                     routingForm.display();
                }
         else{
             removeAwardDocumentBean();
             CoeusOptionPane.showInfoDialog("The Award Notice does not have any routing map defined.     ") ;
         }
        
         }
return true;

    }
    
    
    public void setAward(AwardBean awardBean) {
        this.awardBean = awardBean;
    }
    private Hashtable getSelectedItems() {
        Hashtable htSelectedItems = new Hashtable();
        htSelectedItems.put(SIGNATURE_REQUIRED, new Boolean(chkSignatureReq.isSelected()));
        htSelectedItems.put(ADDRESS_LIST, new Boolean(chkAddressList.isSelected()));
        htSelectedItems.put(CLOSEOUT, new Boolean(chkCloseout.isSelected()));
        htSelectedItems.put(COMMENTS, new Boolean(chkComments.isSelected()));
        htSelectedItems.put(COST_SHARING, new Boolean(chkCostSharing.isSelected()));
        htSelectedItems.put(EQUIPMENT, new Boolean(chkEquipment.isSelected()));
        htSelectedItems.put(FLOW_THRU, new Boolean(chkFlowThru.isSelected()));
        htSelectedItems.put(FOREIGN_TRAVEL, new Boolean(chkForeignTravel.isSelected()));
        htSelectedItems.put(HIERARCHY_INFO, new Boolean(chkHierarchyInfo.isSelected()));
        htSelectedItems.put(INDIRECT_COST, new Boolean(chkIndirectCost.isSelected()));
        htSelectedItems.put(PAYEMENT, new Boolean(chkPayment.isSelected()));
        htSelectedItems.put(PROPOSAL_DUE, new Boolean(chkProposalDue.isSelected()));
        htSelectedItems.put(REPORTING, new Boolean(chkReporting.isSelected()));
        htSelectedItems.put(SCIENCE_CODE, new Boolean(chkScienceCode.isSelected()));
        
        htSelectedItems.put(SPECIAL_REVIEW, new Boolean(chkSpecialReview.isSelected()));
        htSelectedItems.put(SUBCONTRACT, new Boolean(chkSubcontract.isSelected()));
        htSelectedItems.put(TECH_REPORTING, new Boolean(chkTechReporting.isSelected()));
        htSelectedItems.put(TERMS, new Boolean(chkTerms.isSelected()));
        //start csse 2010
        htSelectedItems.put(OTHER_DATA, new Boolean(chkOtherData.isSelected()));
       //end case 2010
         //Added for Case 3122 - Award Notice Enhancement - Start
        htSelectedItems.put(FUNDING_SUMMARY, new Boolean(chkFundingSummary.isSelected()));
         //Added for Case 3122 - Award Notice Enhancement - End
        return htSelectedItems;
    }
    /*
     *The method which makes a server call and checks whether signature is required
     */
    private boolean isSignatureRequired() {
        try {
            RequesterBean requesterBean = new RequesterBean();
            requesterBean.setFunctionType(SIGNATURE_REQUIRED_REQUEST);
            
            requesterBean.setDataObject(awardBean);
            AppletServletCommunicator appletServletCommunicator = new AppletServletCommunicator(CoeusGuiConstants.CONNECTION_URL + SERVLET, requesterBean);
            appletServletCommunicator.setRequest(requesterBean);
            appletServletCommunicator.send();
            ResponderBean responderBean = appletServletCommunicator.getResponse();
            if(responderBean == null) {
                //Could not contact server.
                return false;
            }
            signatureRequired = (Integer)responderBean.getDataObject();
            
            if (signatureRequired.intValue()==0) {
                return false;
            } else {
                return true;
            }
        }catch(Exception e) {
            return false;
        }
    }
    public void actionPerformed(java.awt.event.ActionEvent ae) {
        Object source =ae.getSource();
        try {
            dlgPrintNotice.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            if (source.equals(btnClose)) {
                dlgPrintNotice.setVisible(false);
            } else if (source.equals(btnPrint)) {
                boolean success=getPDFUrl();
                if (success) {
                    dlgPrintNotice.setVisible(false);
                }
            } else if (source.equals(btnSelectAll)) {
                chkAddressList.setSelected(true);
                chkCloseout.setSelected(true);
                chkComments.setSelected(true);
                chkCostSharing.setSelected(true);
                chkEquipment.setSelected(true);
                chkFlowThru.setSelected(true);
                chkForeignTravel.setSelected(true);
                chkHierarchyInfo.setSelected(true);
                chkIndirectCost.setSelected(true);
                chkPayment.setSelected(true);
                chkProposalDue.setSelected(true);
                chkReporting.setSelected(true);
                chkScienceCode.setSelected(true);
                chkSpecialReview.setSelected(true);
                chkSubcontract.setSelected(true);
                chkTechReporting.setSelected(true);
                chkTerms.setSelected(true);
                //start case 2010
                chkOtherData.setSelected(true);
                //end case 2010
                //Added for Case 3122 - Award Notice Enhancement - Start
                chkFundingSummary.setSelected(true);
                //Added for Case 3122 - Award Notice Enhancement - End
            } else if (source.equals(btnDeselectAll)) {
                chkAddressList.setSelected(false);
                chkCloseout.setSelected(false);
                chkComments.setSelected(false);
                chkCostSharing.setSelected(false);
                chkEquipment.setSelected(false);
                chkFlowThru.setSelected(false);
                chkForeignTravel.setSelected(false);
                chkHierarchyInfo.setSelected(false);
                chkIndirectCost.setSelected(false);
                chkPayment.setSelected(false);
                chkProposalDue.setSelected(false);
                chkReporting.setSelected(false);
                chkScienceCode.setSelected(false);
                chkSpecialReview.setSelected(false);
                chkSubcontract.setSelected(false);
                chkTechReporting.setSelected(false);
                chkTerms.setSelected(false);
                //start case 2010
                chkOtherData.setSelected(false);
                //end case 2010
                //Added for Case 3122 - Award Notice Enhancement - Start
                chkFundingSummary.setSelected(false);
                //Added for Case 3122 - Award Notice Enhancement - End
            }
        } finally {
            dlgPrintNotice.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }
        
    }
//COEUSQA 2111 STARTS
   private AwardDocumentRouteBean LoadAwardDocumentBean()
   {
      awardDocumentRouteBean=new AwardDocumentRouteBean();
      awardDocumentRouteBean.setMitAwardNumber(awardBean.getMitAwardNumber());
      awardDocumentRouteBean.setSequenceNumber(awardBean.getSequenceNumber());
      awardDocumentRouteBean.setRoutingDocumentNumber(-1);
      awardDocumentRouteBean.setDocumentData(null);
      awardDocumentRouteBean.setDocumentTypeCode(AWARD_NOTICE_CODE);
      awardDocumentRouteBean.setRoutingStatusCode(-1);
      if(chkSignatureReq.isSelected()){awardDocumentRouteBean.setSignatureFlag("Y");}
      else                            {awardDocumentRouteBean.setSignatureFlag("N");}
//      if(signatureRequired!=null){
//      awardDocumentRouteBean.setSignatureFlag((signatureRequired.intValue()==0)?"N":"Y");}
//      else{awardDocumentRouteBean.setSignatureFlag("N");}
      return awardDocumentRouteBean;
   }
   
   private boolean doSubmitApproveAction() {
        
        try {
            
        Vector requestParameters = new Vector();
        //Passes Protocol Number, unit number,"S" submit for approve option
        //requestParameters.add(String.valueOf(ModuleConstants.AWARD_MODULE_CODE));
        requestParameters.add(awardDocumentRouteBean.getMitAwardNumber());
        requestParameters.add(Integer.parseInt((String.valueOf(awardDocumentRouteBean.getRoutingDocumentNumber()))));
        requestParameters.add(awardBean.getLeadUnitNumber());
       //requestParameters.add(true);
        requestParameters.add("S");
       // requestParameters.add(null);
        RequesterBean requesterBean = new RequesterBean();
        requesterBean.setFunctionType(SUBMIT_FOR_APPROVE);
        requesterBean.setDataObjects(requestParameters);
        String connectTo = CoeusGuiConstants.CONNECTION_URL
                + SUBMISSION_SERVLET;
        AppletServletCommunicator comm = new AppletServletCommunicator(connectTo, requesterBean);
        comm.send();
        ResponderBean response = comm.getResponse();
        Vector submitApproveResultSetData = null;
        if(response.isSuccessfulResponse() && response.getDataObjects() != null){
            submitApproveResultSetData = response.getDataObjects();
            if(submitApproveResultSetData != null && submitApproveResultSetData.size() > 0) {
                if(submitApproveResultSetData.get(0) != null) {
                        // returns Integer representing Action on server end
                        if(((Integer) submitApproveResultSetData.get(0)).intValue() > 0) {
                            return true;
                        }//COEUSQA-2554_Parent key not found error while submitting protocol for Routing_Start
                        else if(((Integer) submitApproveResultSetData.get(0)).intValue() ==  0) {
                            //errDisplay = true;
                        }
                        //COEUSQA-2554_Parent key not found error while submitting protocol for Routing_end
                }
            }
        } else if(!response.isSuccessfulResponse()){
            CoeusOptionPane.showErrorDialog(response.getMessage());
            //invalidResponse = true;
        }
        }catch(Exception expec) {
            expec.printStackTrace();
        }
          return false;
    }
   
    private boolean removeAwardDocumentBean(){
       RequesterBean requesterBean = new RequesterBean();
        requesterBean.setFunctionType(REMOVE_AWARD_DOCUMENT);
        requesterBean.setDataObject(awardDocumentRouteBean);
        String connectTo = CoeusGuiConstants.CONNECTION_URL
                + SUBMISSION_SERVLET;
        AppletServletCommunicator comm = new AppletServletCommunicator(connectTo, requesterBean);
        comm.send();
        ResponderBean response = comm.getResponse();
        if(!response.isSuccessfulResponse()){
        CoeusOptionPane.showErrorDialog(response.getMessage());
        return false;
        }
       return true;
   }
//COEUSQA 2111 ENDS
    
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        chkSignatureReq = new javax.swing.JCheckBox();
        pnlInclude = new javax.swing.JPanel();
        chkAddressList = new javax.swing.JCheckBox();
        chkCloseout = new javax.swing.JCheckBox();
        chkComments = new javax.swing.JCheckBox();
        chkCostSharing = new javax.swing.JCheckBox();
        chkEquipment = new javax.swing.JCheckBox();
        chkFlowThru = new javax.swing.JCheckBox();
        chkForeignTravel = new javax.swing.JCheckBox();
        chkHierarchyInfo = new javax.swing.JCheckBox();
        chkIndirectCost = new javax.swing.JCheckBox();
        chkOtherData = new javax.swing.JCheckBox();
        chkPayment = new javax.swing.JCheckBox();
        chkProposalDue = new javax.swing.JCheckBox();
        chkReporting = new javax.swing.JCheckBox();
        chkScienceCode = new javax.swing.JCheckBox();
        chkSpecialReview = new javax.swing.JCheckBox();
        chkSubcontract = new javax.swing.JCheckBox();
        chkTechReporting = new javax.swing.JCheckBox();
        chkTerms = new javax.swing.JCheckBox();
        btnSelectAll = new javax.swing.JButton();
        btnDeselectAll = new javax.swing.JButton();
        chkFundingSummary = new javax.swing.JCheckBox();
        btnPrint = new javax.swing.JButton();
        btnClose = new javax.swing.JButton();

        setMaximumSize(new java.awt.Dimension(690, 320));
        setMinimumSize(new java.awt.Dimension(690, 320));
        setPreferredSize(new java.awt.Dimension(690, 320));
        setLayout(new java.awt.GridBagLayout());

        chkSignatureReq.setText("Signatures Required");
        chkSignatureReq.setMaximumSize(new java.awt.Dimension(200, 23));
        chkSignatureReq.setMinimumSize(new java.awt.Dimension(200, 23));
        chkSignatureReq.setPreferredSize(new java.awt.Dimension(200, 23));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        add(chkSignatureReq, gridBagConstraints);

        pnlInclude.setBorder(javax.swing.BorderFactory.createTitledBorder("Include"));
        pnlInclude.setMaximumSize(new java.awt.Dimension(520, 255));
        pnlInclude.setMinimumSize(new java.awt.Dimension(520, 255));
        pnlInclude.setPreferredSize(new java.awt.Dimension(520, 255));
        pnlInclude.setLayout(new java.awt.GridBagLayout());

        chkAddressList.setText("Address List");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 0);
        pnlInclude.add(chkAddressList, gridBagConstraints);

        chkCloseout.setText("Closeout");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 0);
        pnlInclude.add(chkCloseout, gridBagConstraints);

        chkComments.setText("Comments");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 0);
        pnlInclude.add(chkComments, gridBagConstraints);

        chkCostSharing.setText("Cost Sharing");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 0);
        pnlInclude.add(chkCostSharing, gridBagConstraints);

        chkEquipment.setText("Equipment");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 0);
        pnlInclude.add(chkEquipment, gridBagConstraints);

        chkFlowThru.setText("Flow Thru");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 0);
        pnlInclude.add(chkFlowThru, gridBagConstraints);

        chkForeignTravel.setText("Foreign Travel");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 0);
        pnlInclude.add(chkForeignTravel, gridBagConstraints);

        chkHierarchyInfo.setText("Hierarchy Info");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 36, 0, 0);
        pnlInclude.add(chkHierarchyInfo, gridBagConstraints);

        chkIndirectCost.setText("Indirect Cost");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 36, 0, 0);
        pnlInclude.add(chkIndirectCost, gridBagConstraints);

        chkOtherData.setText("Other Data");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 36, 0, 0);
        pnlInclude.add(chkOtherData, gridBagConstraints);

        chkPayment.setText("Payment");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 36, 0, 0);
        pnlInclude.add(chkPayment, gridBagConstraints);

        chkProposalDue.setText("Proposal Due");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 36, 0, 0);
        pnlInclude.add(chkProposalDue, gridBagConstraints);

        chkReporting.setText("Reporting");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 36, 0, 0);
        pnlInclude.add(chkReporting, gridBagConstraints);

        chkScienceCode.setText("Science Code");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 32, 0, 0);
        pnlInclude.add(chkScienceCode, gridBagConstraints);

        chkSpecialReview.setText("Special Review");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 32, 0, 0);
        pnlInclude.add(chkSpecialReview, gridBagConstraints);

        chkSubcontract.setText("Subcontract");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 32, 0, 0);
        pnlInclude.add(chkSubcontract, gridBagConstraints);

        chkTechReporting.setText("Technical Reporting");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 32, 0, 10);
        pnlInclude.add(chkTechReporting, gridBagConstraints);

        chkTerms.setText("Terms");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 32, 0, 0);
        pnlInclude.add(chkTerms, gridBagConstraints);

        btnSelectAll.setMnemonic('S');
        btnSelectAll.setText("Select All");
        btnSelectAll.setMinimumSize(new java.awt.Dimension(175, 23));
        btnSelectAll.setPreferredSize(new java.awt.Dimension(175, 23));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 20, 3, 0);
        pnlInclude.add(btnSelectAll, gridBagConstraints);

        btnDeselectAll.setMnemonic('D');
        btnDeselectAll.setText("Deselect All");
        btnDeselectAll.setMinimumSize(new java.awt.Dimension(175, 23));
        btnDeselectAll.setPreferredSize(new java.awt.Dimension(175, 23));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 107, 3, 0);
        pnlInclude.add(btnDeselectAll, gridBagConstraints);

        chkFundingSummary.setText("Funding Summary");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 36, 0, 0);
        pnlInclude.add(chkFundingSummary, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.insets = new java.awt.Insets(11, 0, 6, 0);
        add(pnlInclude, gridBagConstraints);

        btnPrint.setText("Route Document");
        btnPrint.setMaximumSize(new java.awt.Dimension(120, 25));
        btnPrint.setMinimumSize(new java.awt.Dimension(120, 25));
        btnPrint.setPreferredSize(new java.awt.Dimension(120, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(13, 6, 0, 0);
        add(btnPrint, gridBagConstraints);

        btnClose.setMnemonic('C');
        btnClose.setText("Close");
        btnClose.setMaximumSize(new java.awt.Dimension(120, 25));
        btnClose.setMinimumSize(new java.awt.Dimension(120, 25));
        btnClose.setPreferredSize(new java.awt.Dimension(120, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(9, 6, 0, 0);
        add(btnClose, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnClose;
    private javax.swing.JButton btnDeselectAll;
    private javax.swing.JButton btnPrint;
    private javax.swing.JButton btnSelectAll;
    private javax.swing.JCheckBox chkAddressList;
    private javax.swing.JCheckBox chkCloseout;
    private javax.swing.JCheckBox chkComments;
    private javax.swing.JCheckBox chkCostSharing;
    private javax.swing.JCheckBox chkEquipment;
    private javax.swing.JCheckBox chkFlowThru;
    private javax.swing.JCheckBox chkForeignTravel;
    private javax.swing.JCheckBox chkFundingSummary;
    private javax.swing.JCheckBox chkHierarchyInfo;
    private javax.swing.JCheckBox chkIndirectCost;
    private javax.swing.JCheckBox chkOtherData;
    private javax.swing.JCheckBox chkPayment;
    private javax.swing.JCheckBox chkProposalDue;
    private javax.swing.JCheckBox chkReporting;
    private javax.swing.JCheckBox chkScienceCode;
    private javax.swing.JCheckBox chkSignatureReq;
    private javax.swing.JCheckBox chkSpecialReview;
    private javax.swing.JCheckBox chkSubcontract;
    private javax.swing.JCheckBox chkTechReporting;
    private javax.swing.JCheckBox chkTerms;
    private javax.swing.JPanel pnlInclude;
    // End of variables declaration//GEN-END:variables
}
