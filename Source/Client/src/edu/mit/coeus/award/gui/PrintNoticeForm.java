/*
 * PrintNoticeForm.java
 *
 * Created on August 13, 2004, 3:50 PM
 */

package edu.mit.coeus.award.gui;

/**
 *
 * @author  bijosht
 */

import edu.mit.coeus.gui.URLOpener;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.gui.CoeusDlgWindow;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.utils.AppletServletCommunicator;
import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.award.bean.AwardBean;
import edu.mit.coeus.utils.CoeusVector;

import java.net.URL;
import java.applet.AppletContext;
import java.awt.event.ActionListener;
import java.util.Hashtable;
import java.awt.Cursor;
public class PrintNoticeForm extends javax.swing.JPanel implements ActionListener{
    
    private static final String DLG_TITLE = "Print Notice";
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
    
    /** Creates new form PrintNoticeForm */
    public PrintNoticeForm() {
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
        dlgPrintNotice.setSize(650, 275);
        dlgPrintNotice.setResizable(false);
        dlgPrintNotice.setLocation(CoeusDlgWindow.CENTER);
        dlgPrintNotice.setVisible(true);
    }
    
    private boolean getPDFUrl() {
        RequesterBean requesterBean = new RequesterBean();
        requesterBean.setFunctionType(AWARD_NOTICE);
        Hashtable htDataToSend = getSelectedItems();
        htDataToSend.put(AWARD_BEAN, awardBean);
        requesterBean.setDataObject(htDataToSend);
        
        //For Streaming
        htDataToSend.put("LOGGED_IN_USER", CoeusGuiConstants.getMDIForm().getUserId());
        htDataToSend.put("REPORT_TYPE","AwardNotice");
        requesterBean.setId("Award/AwardNotice");
        requesterBean.setFunctionType('R');
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
        String url = (String)responderBean.getDataObject();
        url = url.replace('\\', '/');
        AppletContext coeusContext = CoeusGuiConstants.getMDIForm().getCoeusAppletContext();
        try{
//            URL templateURL = new URL(CoeusGuiConstants.CONNECTION_URL + url);
//            if(coeusContext != null){
//                coeusContext.showDocument( templateURL, "_blank" );
//            }else{
//                javax.jnlp.BasicService bs = (javax.jnlp.BasicService)javax.jnlp.ServiceManager.lookup("javax.jnlp.BasicService");
//                bs.showDocument(templateURL);
//            }
            URL urlObj = new URL(url);
            URLOpener.openUrl(urlObj);
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            CoeusOptionPane.showErrorDialog(""+ex.getMessage());
            return false;
        }
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
            Integer signatureRequired = (Integer)responderBean.getDataObject();
            
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
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
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

        setLayout(new java.awt.GridBagLayout());

        setMaximumSize(new java.awt.Dimension(610, 260));
        setMinimumSize(new java.awt.Dimension(610, 260));
        setPreferredSize(new java.awt.Dimension(610, 260));
        chkSignatureReq.setText("Signatures Required");
        chkSignatureReq.setMaximumSize(new java.awt.Dimension(200, 23));
        chkSignatureReq.setMinimumSize(new java.awt.Dimension(200, 23));
        chkSignatureReq.setPreferredSize(new java.awt.Dimension(200, 23));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        add(chkSignatureReq, gridBagConstraints);

        pnlInclude.setLayout(new java.awt.GridBagLayout());

        pnlInclude.setBorder(javax.swing.BorderFactory.createTitledBorder("Include"));
        pnlInclude.setMaximumSize(new java.awt.Dimension(520, 255));
        pnlInclude.setMinimumSize(new java.awt.Dimension(520, 255));
        pnlInclude.setPreferredSize(new java.awt.Dimension(520, 255));
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
        gridBagConstraints.weighty = 0.01;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        add(pnlInclude, gridBagConstraints);

        btnPrint.setMnemonic('P');
        btnPrint.setText("Print");
        btnPrint.setMaximumSize(new java.awt.Dimension(85, 25));
        btnPrint.setMinimumSize(new java.awt.Dimension(85, 25));
        btnPrint.setPreferredSize(new java.awt.Dimension(85, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(13, 6, 1, 5);
        add(btnPrint, gridBagConstraints);

        btnClose.setMnemonic('C');
        btnClose.setText("Close");
        btnClose.setMaximumSize(new java.awt.Dimension(85, 25));
        btnClose.setMinimumSize(new java.awt.Dimension(85, 25));
        btnClose.setPreferredSize(new java.awt.Dimension(85, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 6, 0, 5);
        add(btnClose, gridBagConstraints);

    }// </editor-fold>//GEN-END:initComponents
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JButton btnClose;
    public javax.swing.JButton btnDeselectAll;
    public javax.swing.JButton btnPrint;
    public javax.swing.JButton btnSelectAll;
    public javax.swing.JCheckBox chkAddressList;
    public javax.swing.JCheckBox chkCloseout;
    public javax.swing.JCheckBox chkComments;
    public javax.swing.JCheckBox chkCostSharing;
    public javax.swing.JCheckBox chkEquipment;
    public javax.swing.JCheckBox chkFlowThru;
    public javax.swing.JCheckBox chkForeignTravel;
    public javax.swing.JCheckBox chkFundingSummary;
    public javax.swing.JCheckBox chkHierarchyInfo;
    public javax.swing.JCheckBox chkIndirectCost;
    public javax.swing.JCheckBox chkOtherData;
    public javax.swing.JCheckBox chkPayment;
    public javax.swing.JCheckBox chkProposalDue;
    public javax.swing.JCheckBox chkReporting;
    public javax.swing.JCheckBox chkScienceCode;
    public javax.swing.JCheckBox chkSignatureReq;
    public javax.swing.JCheckBox chkSpecialReview;
    public javax.swing.JCheckBox chkSubcontract;
    public javax.swing.JCheckBox chkTechReporting;
    public javax.swing.JCheckBox chkTerms;
    public javax.swing.JPanel pnlInclude;
    // End of variables declaration//GEN-END:variables
    
}
