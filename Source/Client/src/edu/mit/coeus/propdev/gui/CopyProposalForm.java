/*
 * @(#)CopyProposalForm.java 1.0 05/23/03 3:19 AM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

/* PMD check performed, and commented unused imports and
 * variables on 24-AUG-2011 by Bharati
 */

package edu.mit.coeus.propdev.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Vector;
import java.util.Hashtable;
//import java.sql.Timestamp;

import edu.mit.coeus.gui.*;
import edu.mit.coeus.irb.bean.*;
import edu.mit.coeus.utils.*;
import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.budget.bean.BudgetInfoBean;
import edu.mit.coeus.departmental.gui.*;
import edu.mit.coeus.propdev.bean.ProposalDevelopmentFormBean;
import edu.mit.coeus.propdev.bean.ProposalInvestigatorFormBean;
import edu.mit.coeus.exception.CoeusClientException;
import java.awt.Cursor;
import javax.swing.table.DefaultTableModel;

// JM 11-19-2012 get parameters
import edu.vanderbilt.coeus.utils.CustomFunctions;
// JM END

/** This class is used to construct and display the Copy Proposal window.
 * This will be invoked from the <CODE>ProposalBaseWindow</CODE>.
 *
 * @author Prasanna Kumar
 * @version 1.0 March 16, 2003, 3:27 PM
 */

public class CopyProposalForm extends javax.swing.JComponent implements ActionListener, TypeConstants, LookUpWindowConstants {
    
    //holds JDialog object
    //private JDialog dlgParentComponent;
    private CoeusDlgWindow dlgParentComponent;
    //holds Source proposal number
    private String proposalNumber;
    //holds Sponsor name
    private String sponsorName;
    //holds Logged in User
    private String userId;
    //holds Proposal Maintenance Servlet name
    private final String PROPOSAL_MAINT = "/ProposalMaintenanceServlet";
    //holds Budget Flag
    private char budgetFlag;
    //holds Narrative Flag
    private char narrativeFlag;
    // holds reference to Base window
    private JTable tblProposal;
    //holds the new Unit number selected
    private String unitNumber;
    //holds the new Unit name selected
    private String unitName;
    //holds CoeusMessageResources instance used for reading message Properties.
    private CoeusMessageResources coeusMessageResources;
    //holds Status of Proposal
    private Hashtable statusDescription;
    //holds ProposalDevelopmentFormBean reference
    ProposalDevelopmentFormBean proposalDevelopmentFormBean = null;
    
    private CoeusAppletMDIForm mdiForm;
    //Added for case #2372 start 1
    private static final char GET_BUDGET_INFO_FOR_COPY = 'w';
    //Added for case #2372 end 1
    
    //COEUSQA-3273 : Inactive Cost Element in use Error Alert required for Proposal and Award Budgets - start
    private static final char GET_COST_ELEMENTS = '4';
    private static final String BUDGET_HAVE_INACTIVE_COST_ELEMENTS = "budgetSelect_exceptionCode.1061";
    //COEUSQA-3273 : Inactive Cost Element in use Error Alert required for Proposal and Award Budgets - end
    
    //Added for COEUSQA-3309 Inactive Appointment Type and Period Types error validation alert required in Budget - start
    private static final char GET_APPOINTMENT_AND_PERIOD_TYPES = '9';
    //Added for COEUSQA-3309 Inactive Appointment Type and Period Types error validation alert required in Budget - end
    
    //Added for case#3243
    private static final String SIMPLE_DATE_FORMAT = "yyyy/MM/dd";
    
    //Added for case #2903 - start
    boolean isFinalFlag = false;
    //Added for case #2903 - end
    // COEUSQA-2321: Copy Questionnaires for Proposal Development records - Start
    private char questionnaireFlag;
    private static final String QUESTIONNAIRE_SERVLET = "/questionnaireServlet";
    private static final char CHECK_CAN_COPY_QUESTIONNAIRE = 'd';
    boolean canCopyQnr = true;
    
    // JM 11-16-2012 need copy parameters
    private static final String ALLOWED_QUESTIONNAIRE_COPY = "ALLOWED_QUESTIONNAIRE_COPY";
    // JM END
    
    // COEUSQA-2321: Copy Questionnaires for Proposal Development records - End
    /** Creates new form CopyProposalForm
     * @param proposalNumber is the Source Proposal Number which has to be copied
     * @param sponsorName Name of Sponsor
     * @param userId  Logged in User Id
     */
    public CopyProposalForm(CoeusAppletMDIForm mdiForm,String proposalNumber , String sponsorName, String userId) {
        this.mdiForm = mdiForm;
        this.proposalNumber = proposalNumber;
        this.sponsorName = sponsorName;
        this.userId = userId;
    }
    
    // JM 11-19-2012 check if questionnaire can be copied
    private boolean checkQuestionnaireCopy() throws CoeusClientException {
        CustomFunctions function = new CustomFunctions();

        String[] disallowedStatusParams = function.getParameterValues(ALLOWED_QUESTIONNAIRE_COPY);
    	int proposalStatusCode = function.getProposalStatusCode(proposalNumber);
    	String proposalStatus = function.getProposalStatusDesc(proposalStatusCode);

    	java.util.Date proposalDate = new java.util.Date();
		java.util.Date nowDate = new java.util.Date();
    	long diffInDays = 0;
  		Vector proposalDetails = function.getProposalDetails(proposalNumber);
   		ProposalDevelopmentFormBean bean = 
   			(ProposalDevelopmentFormBean) proposalDetails.elementAt(0);
   		proposalDate = bean.getUpdateTimestamp();
		//System.out.println("today's timestamp :: " + new java.sql.Timestamp(nowDate.getTime()));
		//System.out.println("proposalDate :: " + new java.sql.Timestamp(proposalDate.getTime()));
    	diffInDays = (nowDate.getTime() - proposalDate.getTime()) / (1000 * 60 * 60 * 24);
    	//System.out.println("time diff :: " + diffInDays);
    	
    	boolean enableQ = false;
    	for (int d=0; d<disallowedStatusParams.length; d++) {
    		String[] params = disallowedStatusParams[d].split("=>");
    		if (proposalStatus.equals(params[0]) && ((int)diffInDays <= Integer.parseInt(params[1]))) {
    			enableQ = true;
    		}
    	}
     	return enableQ;
    }
    // JM END	
    
    /** This method displays the Copy Proposal Form
     */
    public void showCopyProposalForm(){
        coeusMessageResources = CoeusMessageResources.getInstance();
        
        initComponents();
        postInitComponents();
        try {
            setFormData(getOptionRights());
        }catch(CoeusClientException coeusClientException) {
            CoeusOptionPane.showDialog(coeusClientException);
        }
        
        dlgParentComponent = new CoeusDlgWindow(mdiForm,
                "Select Copy Options", true);
        
        dlgParentComponent.getContentPane().add(pnlCopyProposal);
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
        
        // Added by chandra 12th July 2004
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
        // End chandra 12th July 2004
        //dlgParentComponent.show();
    }
    
    public void display(){
        dlgParentComponent.setVisible(true);
    }
    
    /** This method closes the Copy Proposal Form
     */
    void performWindowClosing(){
        dlgParentComponent.dispose();
        //dlgParentComponent.dispose();
    }
    
    public void setWindowFocus(){
        btnOk.requestFocusInWindow();
    }
    
    /** This method closes the Copy Proposal Form
     */
    public void postInitComponents() {
        this.lblProposalNumber.setText(this.proposalNumber);
        this.lblSponsor.setText(this.sponsorName);
    }
    
    /** This method is used to get whether the Proposal has Narrative and Budget information.
     * @return Vector contains whether Proposal has Narrative and Budget information
     */
    public Vector getOptionRights() throws CoeusClientException {
        Vector vctOptionRights = null;
        String connectTo = CoeusGuiConstants.CONNECTION_URL
                + PROPOSAL_MAINT;
        /* connect to the database and get the formData for the
         * given proposalNumber
         */
        RequesterBean request = new RequesterBean();
        request.setId(proposalNumber);
        request.setFunctionType('Q');
        //Added for case#2903 - Modify all dev proposals should allow you to copy proposal
        //To get the unitNumber for MODIFY_ANY_PROPOSAL right checking.
        request.setDataObject(unitNumber);
        AppletServletCommunicator comm = new AppletServletCommunicator(
                connectTo, request);
        comm.send();
        ResponderBean response = comm.getResponse();
        if(!response.isSuccessfulResponse()) {
            throw new CoeusClientException(response.getMessage(),CoeusClientException.ERROR_MESSAGE);
        }
        vctOptionRights = (Vector)response.getDataObjects();
        return vctOptionRights;
    }
    
    /** This method is used to get whether the Proposal has Narrative and Budget information.
     * @param vctOptionRights contains whether Proposal has Budget and Narrative information
     */
    
    public void setFormData(Vector vctOptionRights) {
        String strHasBudget = vctOptionRights.elementAt(0).toString();
        String strHasNarrative = vctOptionRights.elementAt(1).toString();
        //COEUSQA-2321: Copy Questionnaires for Proposal Development records
        String strHasQnr = vctOptionRights.elementAt(2).toString();
        //Added for COEUSQA-3509 : Add warning message to Copy proposal window - start
        String strGGProposal = vctOptionRights.elementAt(3).toString();
        //Added for COEUSQA-3509 : Add warning message to Copy proposal window - end
        if(strHasBudget.equals("NO")) {
            this.chkBudget.setEnabled(false);
            this.rdBtnAllVersions.setEnabled(false);
            this.rdBtnFinallVersion.setEnabled(false);
        } else {
            this.chkBudget.setSelected(true);
            //Added for Case #2372 start 2
            try{
                java.util.List cvBudgetData = getBudgetInfo();
                if(cvBudgetData != null && cvBudgetData.size() > 0){
                    BudgetInfoBean budgetBean = null;
                    //boolean isFinalFlag = false;
                    //Modified for case #2903 - start
                    //boolean isFinalFlag = false;
                    isFinalFlag = false;
                    //Modified for case #2903 - ends
                    for ( int index = 0; index < cvBudgetData.size(); index ++ ){
                        budgetBean = ( BudgetInfoBean ) cvBudgetData.get( index );
                        isFinalFlag = budgetBean.isFinalVersion();
                        if( isFinalFlag ){
                            break;
                        }//end if
                    }//end for
                    if(!isFinalFlag){
                        this.rdBtnFinallVersion.setEnabled(false);
                        // Case# 2630:4.2 Premium - Copying a proposal
                        // 'All Version' is selected by Default if there is no 'Final Version'
                        this.rdBtnAllVersions.setSelected(true);
                    }//end if
                }
            }catch(CoeusClientException ce){
                CoeusOptionPane.showDialog(ce);
            }
            //Added for Case #2372 end 2
        }
        if(strHasNarrative.equals("NO")) {
            this.chkNarrative.setEnabled(false);
        }
        // COEUSQA-2321: Copy Questionnaires for Proposal Development records
        if("NO".equals(strHasQnr)) {
            this.chkQuestionnaire.setEnabled(false);
        }
        
        // JM 11-19-2012 set Questionnaire to not-enable unless meets criteria
        try {
			boolean enableQ = checkQuestionnaireCopy();
			this.chkQuestionnaire.setEnabled(checkQuestionnaireCopy());
		} 
        catch (CoeusClientException e) {
			System.out.println("Cannot determine if proposal questionnaire can be copied");
			//e.printStackTrace();
		}
		// JM END
        
        //Added for COEUSQA-3509 : Add warning message to Copy proposal window - start
        //if proposal is GG proposal then we have to provide the warning message while copying the proposal
        if("YES".equals(strGGProposal)){
            pnlDetails.setMaximumSize(new java.awt.Dimension(376, 187));
            pnlDetails.setMinimumSize(new java.awt.Dimension(376, 187));
            pnlDetails.setPreferredSize(new java.awt.Dimension(376, 187));
            lblMsg.setText(coeusMessageResources.parseMessageKey("proposal_Copy_MessageCode.1032"));
        }
        //Added for COEUSQA-3509 : Add warning message to Copy proposal window - end
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        rdBtnBudget = new javax.swing.JRadioButton();
        btnBgBudget = new javax.swing.ButtonGroup();
        pnlCopyProposal = new javax.swing.JPanel();
        sptrCopyProposal = new javax.swing.JSeparator();
        pnlDescription = new javax.swing.JPanel();
        pnlPropNumberSponsor1 = new javax.swing.JPanel();
        lblProposalNumberText = new javax.swing.JLabel();
        lblProposalNumber = new javax.swing.JLabel();
        lblSponsorText = new javax.swing.JLabel();
        lblSponsor = new javax.swing.JLabel();
        pnlDetails = new javax.swing.JPanel();
        pnlButtons = new javax.swing.JPanel();
        btnOk = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();
        pnlCheck = new javax.swing.JPanel();
        chkBudget = new javax.swing.JCheckBox();
        rdBtnAllVersions = new javax.swing.JRadioButton();
        rdBtnFinallVersion = new javax.swing.JRadioButton();
        chkNarrative = new javax.swing.JCheckBox();
        chkQuestionnaire = new javax.swing.JCheckBox();
        pnlMsg = new javax.swing.JPanel();
        lblMsg = new javax.swing.JLabel();

        btnBgBudget.add(rdBtnBudget);
        rdBtnBudget.setText("jRadioButton2");

        setLayout(new java.awt.GridBagLayout());

        setMaximumSize(new java.awt.Dimension(340, 170));
        setMinimumSize(new java.awt.Dimension(340, 170));
        setPreferredSize(new java.awt.Dimension(340, 170));
        pnlCopyProposal.setLayout(new java.awt.GridBagLayout());

        sptrCopyProposal.setBackground(java.awt.Color.black);
        sptrCopyProposal.setForeground(java.awt.Color.black);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 43;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTH;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 0, 0);
        pnlCopyProposal.add(sptrCopyProposal, gridBagConstraints);

        pnlDescription.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 0, 0));

        pnlPropNumberSponsor1.setLayout(new java.awt.GridBagLayout());

        lblProposalNumberText.setFont(CoeusFontFactory.getLabelFont()
        );
        lblProposalNumberText.setText("Proposal Number : ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        pnlPropNumberSponsor1.add(lblProposalNumberText, gridBagConstraints);

        lblProposalNumber.setFont(CoeusFontFactory.getNormalFont());
        lblProposalNumber.setText("jLabel1");
        lblProposalNumber.setPreferredSize(new java.awt.Dimension(200, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 49;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        pnlPropNumberSponsor1.add(lblProposalNumber, gridBagConstraints);

        lblSponsorText.setFont(CoeusFontFactory.getLabelFont());
        lblSponsorText.setText("Sponsor : ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        pnlPropNumberSponsor1.add(lblSponsorText, gridBagConstraints);

        lblSponsor.setFont(CoeusFontFactory.getNormalFont());
        lblSponsor.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblSponsor.setText("jLabel1");
        lblSponsor.setPreferredSize(new java.awt.Dimension(200, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 49;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        pnlPropNumberSponsor1.add(lblSponsor, gridBagConstraints);

        pnlDescription.add(pnlPropNumberSponsor1);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        pnlCopyProposal.add(pnlDescription, gridBagConstraints);

        pnlDetails.setLayout(new java.awt.GridBagLayout());

        pnlDetails.setMaximumSize(new java.awt.Dimension(376, 122));
        pnlDetails.setMinimumSize(new java.awt.Dimension(376, 122));
        pnlDetails.setPreferredSize(new java.awt.Dimension(376, 122));
        pnlButtons.setLayout(new java.awt.GridBagLayout());

        pnlButtons.setMinimumSize(new java.awt.Dimension(72, 90));
        pnlButtons.setPreferredSize(new java.awt.Dimension(72, 90));
        btnOk.setFont(CoeusFontFactory.getLabelFont());
        btnOk.setMnemonic('O');
        btnOk.setText("OK");
        btnOk.setMaximumSize(new java.awt.Dimension(67, 23));
        btnOk.setMinimumSize(new java.awt.Dimension(67, 23));
        btnOk.setPreferredSize(new java.awt.Dimension(67, 23));
        btnOk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOkActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 0);
        pnlButtons.add(btnOk, gridBagConstraints);

        btnCancel.setFont(CoeusFontFactory.getLabelFont());
        btnCancel.setMnemonic('C');
        btnCancel.setText("Cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        pnlButtons.add(btnCancel, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(11, 20, 0, 10);
        pnlDetails.add(pnlButtons, gridBagConstraints);

        pnlCheck.setLayout(new java.awt.GridBagLayout());

        pnlCheck.setMaximumSize(new java.awt.Dimension(264, 150));
        pnlCheck.setMinimumSize(new java.awt.Dimension(264, 150));
        pnlCheck.setPreferredSize(new java.awt.Dimension(264, 150));
        chkBudget.setFont(CoeusFontFactory.getLabelFont());
        chkBudget.setMnemonic('B');
        chkBudget.setText("Budget");
        chkBudget.setPreferredSize(new java.awt.Dimension(93, 20));
        chkBudget.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkBudgetActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 3, 0, 0);
        pnlCheck.add(chkBudget, gridBagConstraints);

        btnBgBudget.add(rdBtnAllVersions);
        rdBtnAllVersions.setFont(CoeusFontFactory.getLabelFont());
        rdBtnAllVersions.setMnemonic('A');
        rdBtnAllVersions.setText("All versions");
        rdBtnAllVersions.setPreferredSize(new java.awt.Dimension(100, 20));
        rdBtnAllVersions.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdBtnAllVersionsActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.ipadx = 43;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 36, 0, 0);
        pnlCheck.add(rdBtnAllVersions, gridBagConstraints);

        btnBgBudget.add(rdBtnFinallVersion);
        rdBtnFinallVersion.setFont(CoeusFontFactory.getLabelFont());
        rdBtnFinallVersion.setMnemonic('F');
        rdBtnFinallVersion.setText("Final version only");
        rdBtnFinallVersion.setMinimumSize(new java.awt.Dimension(100, 20));
        rdBtnFinallVersion.setPreferredSize(new java.awt.Dimension(100, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.ipadx = 43;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 36, 0, 0);
        pnlCheck.add(rdBtnFinallVersion, gridBagConstraints);

        chkNarrative.setFont(CoeusFontFactory.getLabelFont());
        chkNarrative.setMnemonic('N');
        chkNarrative.setText("Narrative");
        chkNarrative.setPreferredSize(new java.awt.Dimension(100, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 3, 0, 0);
        pnlCheck.add(chkNarrative, gridBagConstraints);

        chkQuestionnaire.setFont(CoeusFontFactory.getLabelFont());
        chkQuestionnaire.setText("Questionnaire");
        chkQuestionnaire.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        chkQuestionnaire.setMargin(new java.awt.Insets(0, 0, 0, 0));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 7, 0, 0);
        pnlCheck.add(chkQuestionnaire, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 0, 0);
        pnlDetails.add(pnlCheck, gridBagConstraints);

        pnlMsg.setLayout(new java.awt.GridBagLayout());

        pnlMsg.setMinimumSize(new java.awt.Dimension(100, 50));
        pnlMsg.setPreferredSize(new java.awt.Dimension(100, 50));
        lblMsg.setFont(CoeusFontFactory.getNormalFont());
        lblMsg.setForeground(new java.awt.Color(255, 0, 0));
        lblMsg.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblMsg.setPreferredSize(new java.awt.Dimension(350, 50));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        pnlMsg.add(lblMsg, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 8;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 92;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 15, 0);
        pnlDetails.add(pnlMsg, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        pnlCopyProposal.add(pnlDetails, gridBagConstraints);

        add(pnlCopyProposal, new java.awt.GridBagConstraints());

    }// </editor-fold>//GEN-END:initComponents
    
    private void chkBudgetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkBudgetActionPerformed
        // Add your handling code here:
        if(this.chkBudget.isSelected()) {
            this.rdBtnAllVersions.setEnabled(true);
            //Modified for case #2903 - start
            //this.rdBtnFinallVersion.setEnabled(true);
            if(isFinalFlag){
                this.rdBtnFinallVersion.setEnabled(true);
            }else{
                this.rdBtnFinallVersion.setEnabled(false);
                //  Case# 2630:4.2 Premium - Copying a proposal
                this.rdBtnAllVersions.setSelected(true);
            }
            //Modified for case #2903 - end
        } else {
            this.rdBtnAllVersions.setEnabled(false);
            this.rdBtnFinallVersion.setEnabled(false);
            this.rdBtnBudget.setSelected(true);
        }
    }//GEN-LAST:event_chkBudgetActionPerformed
    
    private void btnOkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOkActionPerformed
        //Validate the selection before copying
        try{
            dlgParentComponent.setCursor(new Cursor(Cursor.WAIT_CURSOR));
            boolean blnIsValid = validateData();
//            // COEUSQA-2321: Copy Questionnaires for Proposal Development records
            if(!canCopyQnr){
                dlgParentComponent.dispose(); 
                return;
            }
            //After all validations Copy
            //Vector vctCopiedProp;
            if(blnIsValid == true) {
                Vector vctCopiedProp = copyProposalDetails();
                
                int intIsCopied = 0 ;
                String strTargetPropNumber="";
                if(vctCopiedProp != null && vctCopiedProp.size()>0) {
                    intIsCopied = Integer.parseInt(vctCopiedProp.elementAt(0).toString());
                    this.proposalDevelopmentFormBean = (ProposalDevelopmentFormBean) vctCopiedProp.elementAt(1);
                    }
                if(intIsCopied < 0) {
                    CoeusOptionPane.showErrorDialog(coeusMessageResources.parseMessageKey(
                            "proposal_Copy_exceptionCode.1053"));
                        this.btnOk.setEnabled(false);
                    return;
                }
                
                //            dlgParentComponent.dispose();
                dlgParentComponent.dispose();
                updateRow();
                
            }
        }catch (CoeusClientException coeusClientException){
            CoeusOptionPane.showDialog(coeusClientException);
        }finally{
            dlgParentComponent.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        }
        
    }//GEN-LAST:event_btnOkActionPerformed
    
    
    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // Add your handling code here:
//        dlgParentComponent.dispose();
        dlgParentComponent.setCursor(new Cursor(Cursor.WAIT_CURSOR));
        dlgParentComponent.dispose();
        dlgParentComponent.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    }//GEN-LAST:event_btnCancelActionPerformed
    
    private void rdBtnAllVersionsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdBtnAllVersionsActionPerformed
        // Add your handling code here:
    }//GEN-LAST:event_rdBtnAllVersionsActionPerformed
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.ButtonGroup btnBgBudget;
    public javax.swing.JButton btnCancel;
    public javax.swing.JButton btnOk;
    public javax.swing.JCheckBox chkBudget;
    public javax.swing.JCheckBox chkNarrative;
    public javax.swing.JCheckBox chkQuestionnaire;
    public javax.swing.JLabel lblMsg;
    public javax.swing.JLabel lblProposalNumber;
    public javax.swing.JLabel lblProposalNumberText;
    public javax.swing.JLabel lblSponsor;
    public javax.swing.JLabel lblSponsorText;
    public javax.swing.JPanel pnlButtons;
    public javax.swing.JPanel pnlCheck;
    public javax.swing.JPanel pnlCopyProposal;
    public javax.swing.JPanel pnlDescription;
    public javax.swing.JPanel pnlDetails;
    public javax.swing.JPanel pnlMsg;
    public javax.swing.JPanel pnlPropNumberSponsor1;
    public javax.swing.JRadioButton rdBtnAllVersions;
    public javax.swing.JRadioButton rdBtnBudget;
    public javax.swing.JRadioButton rdBtnFinallVersion;
    public javax.swing.JSeparator sptrCopyProposal;
    // End of variables declaration//GEN-END:variables
    
    /**
     * This method is invoked when the user clicks OK button
     *
     * @return Vector which contains the list of Unit Numbers for the loggedin user
     */
    private Vector getUserUnits() throws CoeusClientException {
        Vector vecUserUnits = null;
        String connectTo = CoeusGuiConstants.CONNECTION_URL + CoeusGuiConstants.FUNCTION_SERVLET;
        RequesterBean request = new RequesterBean();
        Vector vecProcParams = new Vector();
        vecProcParams.addElement(userId);
        vecProcParams.addElement(TypeConstants.CREATE_RIGHT);
        //request.setFunctionType('U');
        request.setDataObjects(vecProcParams);
        request.setDataObject("GET_USER_UNITS");
        AppletServletCommunicator comm
                = new AppletServletCommunicator(connectTo, request);
        comm.send();
        ResponderBean response = comm.getResponse();
        
        if (response!=null){
            if (response.isSuccessfulResponse()){
                vecUserUnits = (Vector)response.getDataObject();
            } else {
                throw new CoeusClientException(response.getMessage(),CoeusClientException.ERROR_MESSAGE);
            }
        }
        
        return vecUserUnits;
    }
    
    /** This methods used to display the new Unit number selection form.
     *  can be copied.
     * @return boolean indicating whether unit number selected.
     */
    private boolean displayUnits() {
        boolean openNewProposal = false;
        ComboBoxBean comboBoxBean;
        try {
            Vector userUnits = getUserUnits();
            
            if (userUnits != null) {
                if (userUnits.size() == 1) {
                    comboBoxBean = (ComboBoxBean) userUnits.elementAt(0);
                    if (comboBoxBean != null) {
                        unitNumber = comboBoxBean.getCode();
                        unitName = comboBoxBean.getDescription();
                        openNewProposal = true;
                    }
                    
                } else if (userUnits.size() > 1) {
                    
                    String windowTitle = "Select Unit for New Proposal";
                    OtherLookupBean otherLookupBean =
                            new OtherLookupBean(windowTitle, userUnits, UNIT_COLUMN_NAMES);
                    CoeusTableWindow coeusTableWindow =
                            new CoeusTableWindow(otherLookupBean);
                    int selRow = otherLookupBean.getSelectedInd();
                    if(selRow >= 0){
                        comboBoxBean = (ComboBoxBean)userUnits.elementAt(selRow);
                        if(comboBoxBean != null){
                            unitNumber = comboBoxBean.getCode();
                            unitName = comboBoxBean.getDescription();
                            openNewProposal = true;
                        }
                    }
                }
            }
        }catch(CoeusClientException coeusClientException) {
            CoeusOptionPane.showDialog(coeusClientException);
        }
        return openNewProposal;
    }
    
    /** This methods used to check whether the Proposal Number and the selected Unit number
     *  can be copied.
     * @return int value returned from the stored procedure.
     */
    private int isOkToCopyPropBudget() throws CoeusClientException {
        String strIsOkToCopy = "NO";
        Vector vecUserUnits = null;
        String connectTo = CoeusGuiConstants.CONNECTION_URL + PROPOSAL_MAINT;
        RequesterBean request = new RequesterBean();
        Vector vecProcParams = new Vector();
        vecProcParams.addElement(proposalNumber);
        vecProcParams.addElement(unitNumber);
        request.setDataObjects(vecProcParams);
        request.setFunctionType('O');
        AppletServletCommunicator comm
                = new AppletServletCommunicator(connectTo, request);
        comm.send();
        ResponderBean response = comm.getResponse();
        
        if (response!=null){
            if (response.isSuccessfulResponse()){
                vecUserUnits = (Vector)response.getDataObjects();
            } else {
                throw new CoeusClientException(response.getMessage(),CoeusClientException.ERROR_MESSAGE);
            }
        }
        
        int intIsOk = Integer.parseInt(vecUserUnits.elementAt(0).toString());
        return intIsOk;
    }
    
    /** This methods used to copy the Proposal
     * @return Vector received from the stored procedure.
     */
    private Vector copyProposalDetails()  throws CoeusClientException {
        Vector vecUserUnits = null;
        String connectTo = CoeusGuiConstants.CONNECTION_URL + PROPOSAL_MAINT;
        RequesterBean request = new RequesterBean();
        Vector vecProcParams = new Vector();
        vecProcParams.addElement(proposalNumber);
        vecProcParams.addElement(unitNumber);
        vecProcParams.addElement(new Character(this.budgetFlag));
        vecProcParams.addElement(new Character(this.narrativeFlag));
        // COEUSQA-2321: Copy Questionnaires for Proposal Development records
        vecProcParams.addElement(new Character(questionnaireFlag));
        
        request.setDataObjects(vecProcParams);
        request.setFunctionType('V');
        AppletServletCommunicator comm
                = new AppletServletCommunicator(connectTo, request);
        comm.send();
        ResponderBean response = comm.getResponse();
        
        if (response!=null){
            if (response.isSuccessfulResponse()){
                vecUserUnits = (Vector)response.getDataObjects();
            } else {
                throw new CoeusClientException(response.getMessage(),CoeusClientException.ERROR_MESSAGE);
            }
        }
        
        //Vector intIsCopied = Integer.parseInt(vecUserUnits.elementAt(0).toString());
        
        return vecUserUnits;
    }
    
    /** This methods used to validate the form data.
     */
    private boolean validateData() throws CoeusClientException {
        if(this.chkBudget.isSelected()) {
            if(!this.rdBtnAllVersions.isSelected() && !this.rdBtnFinallVersion.isSelected()) {
                CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(
                        "proposal_Copy_exceptionCode.1051"));
                return false;
            } else {
                //Set budget flag
                if(this.rdBtnAllVersions.isSelected()) {
                    this.budgetFlag='A';
                } else if(this.rdBtnFinallVersion.isSelected()) {
                    this.budgetFlag='F';
                }
            }
        }else{
            this.budgetFlag=' ';
        }
  
        //Added for COEUSQA-3273 : Inactive Cost Element in use Error Alert required for Proposal and Award Budgets - start
        boolean  allow_copy = false;
        if(this.chkBudget.isSelected()){
            //get the budget details for proposal
            java.util.List cvBudgetData = getBudgetInfo();
            //Get the cost elements status form the server
            //if it returns true then budget holds inactive cost elements
            //commented and added for COEUSQA-3309 Inactive Appointment Type and Period Types error validation alert required in Budget - start
            //allow_copy = getProposalData(cvBudgetData,this.budgetFlag);     
            /*if(allow_copy && (this.budgetFlag == 'A' || this.budgetFlag == 'F')){
                int selection = CoeusOptionPane.showQuestionDialog(coeusMessageResources.parseMessageKey(BUDGET_HAVE_INACTIVE_COST_ELEMENTS),
                        CoeusOptionPane.OPTION_YES_NO, CoeusOptionPane.DEFAULT_YES);
                //if user selects yes then allows to copy the budget
                if(selection ==CoeusOptionPane.SELECTION_YES){
                    allow_copy = true;
                } else if(selection ==CoeusOptionPane.SELECTION_NO){
                    allow_copy = false;
                }
            }else{
                allow_copy = true;
            }*/
            //it returns the inactive types for the proposal budget
            //if it returns 0 then it doesnt hold any inactive types
            int inactive_Type = isInactiveATAndPTExist(proposalNumber,this.budgetFlag);
            if(inactive_Type == 0){
                allow_copy = true;
            }else {
                String msgKey = "budgetSelect_exceptionCode."+inactive_Type;
                int selection = CoeusOptionPane.showQuestionDialog(coeusMessageResources.parseMessageKey(msgKey),
                        CoeusOptionPane.OPTION_YES_NO, CoeusOptionPane.DEFAULT_YES);
                //if user selects yes then allows to copy the budget
                if(selection ==CoeusOptionPane.SELECTION_YES){
                    allow_copy = true;
                } else if(selection ==CoeusOptionPane.SELECTION_NO){
                    allow_copy = false;
                }
            }                   
        } 
        //Added for COEUSQA-3273 : Inactive Cost Element in use Error Alert required for Proposal and Award Budgets - end
        //added for COEUSQA-3309 Inactive Appointment Type and Period Types error validation alert required in Budget - end
        
        if(this.chkNarrative.isSelected()) {
            this.narrativeFlag='Y';
        } else {
            this.narrativeFlag='N';
        }
        // COEUSQA-2321: Copy Questionnaires for Proposal Development records - Start
        if(chkQuestionnaire.isSelected()){
            boolean canCopy = canCopyQuestionnaires();
            if(!canCopy){
                
//               CoeusOptionPane.show( coeusMessageResources.parseMessageKey(
//                        "proposal_Copy_exceptionCode.1054"));
//                this.canCopyQnr = false;
                int selectedOption = CoeusOptionPane.showQuestionDialog(
                        coeusMessageResources.parseMessageKey("proposal_Copy_exceptionCode.1054"),
                        CoeusOptionPane.OPTION_YES_NO,
                        JOptionPane.YES_OPTION);
                
                if(selectedOption == JOptionPane.YES_OPTION){
                    questionnaireFlag = 'N';
                }else if(selectedOption == JOptionPane.NO_OPTION){
                    this.canCopyQnr = false;
                    return false;
                }
                
            } else {
                questionnaireFlag = 'Y';
            }
        } else {
            questionnaireFlag = 'N';
        }
        // COEUSQA-2321: Copy Questionnaires for Proposal Development records - End
        boolean newUnitSelected = false;
        int intIsOkToCopy;
        //COEUSQA-3273 : Inactive Cost Element in use Error Alert required for Proposal and Award Budgets - start
        //if allow_copy is true then allow to copy the data
        if(this.chkBudget.isSelected()){
            if(allow_copy){
                newUnitSelected = displayUnits();//case Id #1821 -
                //Modified for Null Pointer Exception start
                //return true;
            }
        }else{
            newUnitSelected = displayUnits();//case Id #1821 -
        }
        //COEUSQA-3273 : Inactive Cost Element in use Error Alert required for Proposal and Award Budgets - end
        return newUnitSelected;
        //Modified for Null Pointer Exception end
        
        
    }
    
    /** This methods used get the description for the creationStatusCode
     * in this method hashtable is created to store the description for
     * status code in PB this have been done in data window
     */
    private void getStatusDescription(){
        statusDescription = new Hashtable();
        statusDescription.put("1","In Progress");
        statusDescription.put("2","Approval In Progress");
        statusDescription.put("3","Rejected");
        statusDescription.put("4","Approved");
        statusDescription.put("5","Submitted");
    }
    
    /** This methods used to update the Base Window table with the new Proposal and focus on that row
     */
    private void updateRow(){
        if(this.proposalDevelopmentFormBean != null) {
            getStatusDescription();
            String deadlineDate="";
            DateUtils dtUtils = new DateUtils();
            if(proposalDevelopmentFormBean.getDeadLineDate() != null){
                //Code modified for PT ID#3243 - start
                String dateFormat = CoeusServerProperties.getProperty(CoeusPropertyKeys.SEARCH_DATE_FORMAT, SIMPLE_DATE_FORMAT);
                //COEUSQA-1477 Dates in Search Results - Start
                deadlineDate = dtUtils.parseDateForSearchResults(
                        proposalDevelopmentFormBean.getDeadLineDate().toString(), dateFormat);
//                deadlineDate = dtUtils.formatDate(
//                        proposalDevelopmentFormBean.getDeadLineDate().toString(), dateFormat);
                //COEUSQA-1477 Dates in Search Results - End
                //Code modified for PT ID#3243 - end
            }
            String PIName = getPIDetails( proposalDevelopmentFormBean.getInvestigators() );
            Vector newRowData = new Vector();
            newRowData.addElement( proposalDevelopmentFormBean.getProposalNumber() );
            newRowData.addElement( proposalDevelopmentFormBean.getProposalTypeDesc() );
            
            newRowData.addElement( statusDescription.get(""+proposalDevelopmentFormBean.getStatusCode()));
            newRowData.addElement( proposalDevelopmentFormBean.getTitle() );
            newRowData.addElement( proposalDevelopmentFormBean.getOwnedBy() );
            newRowData.addElement( proposalDevelopmentFormBean.getOwnedByDesc() );
            // JM 1-20-2013 added to make copied proposal returns line up with search form
            newRowData.addElement( "" );
            // JM END
            newRowData.addElement( PIName );
            newRowData.addElement( deadlineDate );
            newRowData.addElement( proposalDevelopmentFormBean.getSponsorCode() );
            newRowData.addElement( proposalDevelopmentFormBean.getSponsorName() );
//            ((DefaultTableModel)tblProposal.getModel()).addRow(newRowData);
//            tblProposal.setRowSelectionInterval(0,tblProposal.getRowCount()-1);
//            }
            
            int lastRow = tblProposal.getRowCount();
            newRowData.addElement(""+lastRow);
            if( lastRow >= 0 ) {
                ((DefaultTableModel)tblProposal.getModel()).insertRow(lastRow,newRowData);
            }else{
                ((DefaultTableModel)tblProposal.getModel()).insertRow(0,newRowData);
            }
        }
    }
    
    /** This method is used to get the Primary Investigator details from the
     * collection of Investigators of this Proposal, which will be used to
     * populate the details in the <CODE>ProposalBaseWindow</CODE>.
     *
     * @param investigators Collection of <CODE>ProposalInvestigatorFormBean</CODE>s of a
     * Proposal.
     */
    private String getPIDetails(Vector investigators ){
        int count = ( investigators == null ? 0 : investigators.size() );
        String PIName = "";
        for (int i=0; i<count;i++) {
            ProposalInvestigatorFormBean investigatorBean
                    = (ProposalInvestigatorFormBean) investigators.elementAt(i);
            if (investigatorBean.isPrincipleInvestigatorFlag()) {
                PIName = investigatorBean.getPersonName();
                break;
            }
        }
        return PIName;
    }
    
    /** This method is used to set the Base window table reference.
     *
     * @param tblMaintable reference to Base Window table
     * Proposal.
     */
    public void setTableReference(JTable tblMaintable){
        this.tblProposal = tblMaintable;
    }
    
    /**
     * Action Performed method
     * @param actionEvent Action Event
     */
    public void actionPerformed(java.awt.event.ActionEvent actionEvent) {
    }
    //Added for case #2372 start 3
    private java.util.List getBudgetInfo() throws CoeusClientException {
        java.util.List cvBudgetData = null;
        final String CONNECTION_STRING = CoeusGuiConstants.CONNECTION_URL + "/BudgetMaintenanceServlet";
        RequesterBean requester = new RequesterBean();
        requester.setFunctionType( GET_BUDGET_INFO_FOR_COPY );
        requester.setId(proposalNumber);
        edu.mit.coeus.utils.AppletServletCommunicator comm
                = new edu.mit.coeus.utils.AppletServletCommunicator(CONNECTION_STRING, requester);
        comm.send();
        ResponderBean response = comm.getResponse();
        if ( response.isSuccessfulResponse() ){
            cvBudgetData = (CoeusVector)response.getDataObject();
        }else{
            throw new CoeusClientException(response.getMessage(),CoeusClientException.ERROR_MESSAGE);
        }
        return cvBudgetData;
    }
       
    /**
     * Getter for property unitNumber.
     * @return Value of property unitNumber.
     */
    public java.lang.String getUnitNumber() {
        return unitNumber;
    }
    
    /**
     * Setter for property unitNumber.
     * @param unitNumber New value of property unitNumber.
     */
    public void setUnitNumber(java.lang.String unitNumber) {
        this.unitNumber = unitNumber;
    }
    
    //Added for case #2372 end 3
   // COEUSQA-2321: Copy Questionnaires for Proposal Development records - Start
    private boolean canCopyQuestionnaires() {
        Boolean canCopy = false;
        Vector moduleData = new Vector();
        moduleData.add(3);
        moduleData.add(0);
        moduleData.add(proposalNumber);
        moduleData.add("0");
        
        RequesterBean requester = new RequesterBean();
        requester.setFunctionType( CHECK_CAN_COPY_QUESTIONNAIRE );
        requester.setDataObjects(moduleData);
        edu.mit.coeus.utils.AppletServletCommunicator comm
                = new edu.mit.coeus.utils.AppletServletCommunicator(CoeusGuiConstants.CONNECTION_URL + QUESTIONNAIRE_SERVLET, requester);
        comm.send();
        ResponderBean response = comm.getResponse();
        if ( response.isSuccessfulResponse() ){
            canCopy = (Boolean)response.getDataObject();
        }
        
        return canCopy;
    }
    
    // COEUSQA-2321: Copy Questionnaires for Proposal Development records - End
    
    //Added for COEUSQA-3273 : Inactive Cost Element in use Error Alert required for Proposal and Award Budgets - start
    /**
     * This method fetches the cost elements data from server
     * @param cvBudgetData
     * @param budgetFlag
     * @returns boolean value
     */
    private boolean getProposalData(java.util.List cvBudgetData, char budgetFlag){
        final String connectTo = CoeusGuiConstants.CONNECTION_URL+ "/BudgetMaintenanceServlet";
        RequesterBean requester = new RequesterBean();
        ResponderBean responder = null;
        int selection;
        Vector proposalData = new Vector();
        proposalData.add(cvBudgetData);
        proposalData.add(budgetFlag);
        requester.setFunctionType(GET_COST_ELEMENTS);
        requester.setDataObjects(proposalData);
        Vector inACtive = new Vector();
        AppletServletCommunicator comm
                = new AppletServletCommunicator(connectTo, requester);
        comm.send();
        responder = comm.getResponse();
        if(responder.isSuccessfulResponse()){
            inACtive =(Vector) responder.getDataObjects();
        }
        if(inACtive!= null && inACtive.size() >0){
            return true;
        }
        return false;
    }
    //Added for COEUSQA-3273 : Inactive Cost Element in use Error Alert required for Proposal and Award Budgets - end
    
    //Added for COEUSQA-3309 Inactive Appointment Type and Period Types error validation alert required in Budget - start
    /**
     * This method is to get the inactive types for the  proposal budget
     * @param proposalNumber
     * @param budgetFlag
     * @returns inactiveType
     */
    private int isInactiveATAndPTExist(String proposalNumber , char budgetFlag){
        final String connectTo = CoeusGuiConstants.CONNECTION_URL+ "/BudgetMaintenanceServlet";
        RequesterBean requester = new RequesterBean();
        ResponderBean responder = null;   
        int inactiveType = 0;
        Vector proposalData = new Vector();
        proposalData.add(proposalNumber);
        proposalData.add(budgetFlag);
        requester.setFunctionType(GET_APPOINTMENT_AND_PERIOD_TYPES);
        requester.setDataObjects(proposalData);
        Vector inACtive = new Vector();
        AppletServletCommunicator comm
                = new AppletServletCommunicator(connectTo, requester);
        comm.send();
        responder = comm.getResponse();
        if(responder.isSuccessfulResponse()){
            inactiveType = (Integer)responder.getDataObject();           
        }
        return inactiveType;
    }
    //Added for COEUSQA-3309 Inactive Appointment Type and Period Types error validation alert required in Budget - end
}
