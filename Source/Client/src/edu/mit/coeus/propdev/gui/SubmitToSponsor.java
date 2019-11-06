/** Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.propdev.gui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Vector;

import edu.mit.coeus.gui.*;
import edu.mit.coeus.utils.*;
import edu.mit.coeus.exception.*;
import edu.mit.coeus.propdev.bean.*;
import edu.mit.coeus.instprop.bean.*;
import edu.mit.coeus.brokers.*;
import edu.mit.coeus.s2s.bean.ProcessGrantsSubmission;
import edu.mit.coeus.s2s.bean.S2SHeader;
import edu.mit.coeus.search.gui.CoeusSearch;
import java.util.HashMap;

/** Submit to sponsor form
 * SubmitToSponsor.java
 * @author  chandrashekara
 * Created on December 31, 2003, 5:41 PM
 */
public class SubmitToSponsor extends javax.swing.JComponent
implements ActionListener, ItemListener{
   
    private ProposalDevelopmentFormBean proposalDevelopmentFormBean;
   // private InstituteProposalBean instituteProposalBean;
    private CoeusDlgWindow dlgSubmitToSponsor;
    private CoeusAppletMDIForm mdiForm;
    private boolean modal;
    private CoeusMessageResources coeusMessageResources = CoeusMessageResources.getInstance();
    private String proposalID;
    private String instPropNumber;
    private BaseWindowObservable observable;
    
    private static final int WIDTH = 435;
    private static final int HEIGHT = 280;

    /** Holds the connection string */
    private static final String connectTo = CoeusGuiConstants.CONNECTION_URL +
                                             "/ProposalActionServlet";
    private static final char VALIDATE_NUMBER = 'T';
    
    private static final char FEED_INST_NUMBER = 'U';
    
    private static final String submissionType = "P";
    
    private static final String submissionStatus = "S";
    
    private static final String EMPTY_STRING = "";
    // Update the child status
    private static final char UPDATE_CHILD_STATUS = 'z'; 

    
    //Added by Vyjayanthi on 27/01/2004 - Start
    private char formMode;
    private static final char APPROVE_MODE = 'A';
    private static final char SUBMIT_TO_SPONSOR_MODE = 'S';
    private static final char GENERATE_INST_PROP_FOR_APPROVE = 'Y';
    private static final char UPDATE_PROP_CREATION_STATUS = 'V';
    private int creationStatus;
    //Added by Vyjayanthi on 27/01/2004 - End
    
    private String generate = "G";
   // private String propNumber;
    private boolean isMenuEnabled = false;
    
    private static final String TITLE = "Institute Proposal Number";
    
    private static final String SELECT_ANY_OPTION = "proposalSubmitToSponsor_exceptionCode.1122";
    private static final String DO_NOT_GENERATE_INST_PROP ="proposalSubmitToSponsor_exceptionCode.1123";
    private static final String NOT_A_VALID_NUMBER = "proposalSubmitToSponsor_exceptionCode.1121";
    
    //Added by Vyjayanthi on 27/01/2004
    private static final String APPROVED_STATUS = "proposal_Action_exceptionCode.8016";
    private static final String PROPOSAL_APPROVED = "proposal_Action_exceptionCode.8014";
    private boolean hierarchy;
    private boolean parent;
    
    /** Creates new form SubmitToSponsor */
    public SubmitToSponsor(CoeusAppletMDIForm mdiForm, boolean modal,String proposalID) {
        this.mdiForm = mdiForm;
        this.modal = modal;
        this.proposalID = proposalID;
        initComponents();
        /*case 2955 start
        String devProp = "Development proposal " +proposalID + "   is a revision";
        lblDevProposal.setText(devProp);
        * case 2955 end */
        registerComponents();
        postInitComponents();
        
    }
    
    
    private void registerComponents(){
        btnOk.addActionListener(this);
        chkGenerateInstProp.addItemListener(this);
        chkDoNotGenerateInstProp.addItemListener(this);
        txtOriginalProp.addFocusListener(new CustomFocusAdapter());
        btnInstProposalSearch.addActionListener(this);
    }
    public void itemStateChanged(ItemEvent itemEvent) {
        if(itemEvent.getSource() == chkGenerateInstProp && chkGenerateInstProp.isSelected()){
            chkDoNotGenerateInstProp.setSelected(false);
            
        }else if(itemEvent.getSource() == chkDoNotGenerateInstProp && chkDoNotGenerateInstProp.isSelected()){
            chkGenerateInstProp.setSelected(false);
            
        }
    }
    /** This method is called from within the constructor to
     * initialize the form. */
    public void postInitComponents(){    
        dlgSubmitToSponsor = new CoeusDlgWindow(mdiForm, modal);
        dlgSubmitToSponsor.setDefaultCloseOperation(CoeusDlgWindow.DO_NOTHING_ON_CLOSE);
        dlgSubmitToSponsor.getContentPane().add(this);
        dlgSubmitToSponsor.setResizable(false);
        dlgSubmitToSponsor.setTitle(TITLE);
        dlgSubmitToSponsor.setFont(CoeusFontFactory.getLabelFont());
        dlgSubmitToSponsor.setSize(WIDTH,HEIGHT);
        
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension dlgSize = dlgSubmitToSponsor.getSize();
        dlgSubmitToSponsor.setLocation(screenSize.width/2 - (dlgSize.width/2),
        screenSize.height/2 - (dlgSize.height/2));
        
        
        
        /** Code for focus traversal - start */
        java.awt.Component[] components = { txtOriginalProp, chkGenerateInstProp,
            chkDoNotGenerateInstProp, btnOk};
        ScreenFocusTraversalPolicy traversePolicy = new ScreenFocusTraversalPolicy( components );
        setFocusTraversalPolicy(traversePolicy);
        setFocusCycleRoot(true);
        /** Code for focus traversal - end */
        
        dlgSubmitToSponsor.addEscapeKeyListener(
        new AbstractAction("escPressed"){
            public void actionPerformed(ActionEvent ae){
                dlgSubmitToSponsor.dispose();
                isMenuEnabled = true;
            }
        });
        //Commented during Code-review since it is already present in postInitComponents()
//        dlgSubmitToSponsor.setDefaultCloseOperation(CoeusDlgWindow.DO_NOTHING_ON_CLOSE);
        dlgSubmitToSponsor.addWindowListener(new WindowAdapter(){
             public void windowActivated(WindowEvent we) {
                requestDefaultFocusOnComponent();
            }
            public void windowClosing(WindowEvent we){
               dlgSubmitToSponsor.dispose();
                isMenuEnabled = true;
            }
        });
        
        // JM 9-24-2013 want to bring forward keyed IP id
   		txtOriginalProp.setText(getOriginalProposalId());
   		// JM END
    }
    
    // JM 9-25-2013 get Original Proposal ID to feed forward
    public String getOriginalProposalId() {
    	Vector details = new Vector();
    	String originalId = "";
    	edu.vanderbilt.coeus.utils.CustomFunctions custom = 
    		new edu.vanderbilt.coeus.utils.CustomFunctions();
    	try {
			details = (Vector) custom.getProposalDetails(proposalID);
		} catch (CoeusClientException e) {
			System.out.println("SubmitToSponsor cannot retrieve proposal details");
		}
		if (details.size() > 0) {
			ProposalDevelopmentFormBean bean = new ProposalDevelopmentFormBean();
			bean = (ProposalDevelopmentFormBean) details.get(0);
			originalId = bean.getContinuedFrom();
		}
		return originalId;
    }
    // JM END
    	
    /** To set the default focus for the specified component
     */
    private void requestDefaultFocusOnComponent(){
        txtOriginalProp.requestFocus();
    }

    /**
     *  Method to invoke submit to grants.gov web service
     *  
     */
    public void submitGrantsGov() throws CoeusException{
        if((proposalDevelopmentFormBean.getCfdaNumber()==null || proposalDevelopmentFormBean.getCfdaNumber().trim().equals("")) && 
            (proposalDevelopmentFormBean.getProgramAnnouncementNumber()==null||proposalDevelopmentFormBean.getProgramAnnouncementNumber().trim().equals(""))){
                return;
        }
        HashMap params = new HashMap();
        params.put("PROPOSAL_NUMBER", proposalDevelopmentFormBean.getProposalNumber());
//        params.put("SPONSOR_CODE", proposalDevelopmentFormBean.getSponsorCode());
//        params.put("SPONSOR_NAME", proposalDevelopmentFormBean.getSponsorName());
        S2SHeader headerParam = new S2SHeader();
        headerParam.setSubmissionTitle(proposalDevelopmentFormBean.getProposalNumber());
        if(proposalDevelopmentFormBean.getCfdaNumber()!=null){
            StringBuffer tempCfdaNum = new StringBuffer(proposalDevelopmentFormBean.getCfdaNumber());
            int charIndex = tempCfdaNum.indexOf(".");
            if(charIndex==-1){
                tempCfdaNum.insert(2,'.');
            }
            headerParam.setCfdaNumber(tempCfdaNum.toString());
        }
        //coeus-680 start
//        headerParam.setOpportunityId(proposalDevelopmentFormBean.getProgramAnnouncementNumber());
        headerParam.setOpportunityId(proposalDevelopmentFormBean.getProgramAnnouncementNumber().toUpperCase().trim().replaceAll(" ", ""));
        //coeus-680 end
        headerParam.setAgency(proposalDevelopmentFormBean.getSponsorCode()+" : "+
                                proposalDevelopmentFormBean.getSponsorName());

        headerParam.setStreamParams(params);
        ProcessGrantsSubmission grantSubmission = new ProcessGrantsSubmission(headerParam);
        grantSubmission.setObservable(observable);
        grantSubmission.setPropDevBean(proposalDevelopmentFormBean);
        grantSubmission.setInvokeType(S2SConstants.AUTO_SUBMISSION); 
        grantSubmission.showS2SSubmissionForm();
    }
    
    private boolean  validateData(){
        boolean dispose = true;
        try{
            if(chkGenerateInstProp.isSelected()){
                dlgSubmitToSponsor.setVisible(false);
                instPropNumber = txtOriginalProp.getText().trim();
                feedInstitutePropNumber(proposalID,instPropNumber,generate,submissionType,submissionStatus);
                isMenuEnabled = false;
                
            }else if(chkDoNotGenerateInstProp.isSelected()){
                dlgSubmitToSponsor.setVisible(false);
                isMenuEnabled = true;
                CoeusOptionPane.showInfoDialog(
                coeusMessageResources.parseMessageKey(DO_NOT_GENERATE_INST_PROP));
                
            }else if(txtOriginalProp.getText() != null &&
            !txtOriginalProp.getText().equals(EMPTY_STRING)){
                //dlgSubmitToSponsor.setVisible(false);
                instPropNumber = txtOriginalProp.getText().trim();
                boolean isValidInstProp = validateProposalNumber();
                if(!isValidInstProp){
                    isMenuEnabled = true;
                    CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(NOT_A_VALID_NUMBER));
                    return false;
                }else{
                    generate = "N";
                    dlgSubmitToSponsor.setVisible(false);
                    feedInstitutePropNumber(proposalID,instPropNumber,generate,submissionType,submissionStatus);
                    //CoeusOptionPane.showInfoDialog("A new sequence number for Institute proposal "+seqNumber +" has been created");
                    isMenuEnabled = false;
                }
            }else {
                isMenuEnabled = true;
                CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(SELECT_ANY_OPTION));
                dispose = false;
            }
            submitGrantsGov();
        }catch(CoeusClientException coeusClientException){
            coeusClientException.printStackTrace();
            CoeusOptionPane.showErrorDialog(coeusClientException.getMessage());
        }catch(CoeusException coeusException){
            coeusException.printStackTrace();
            CoeusOptionPane.showErrorDialog(coeusException.getMessage());
        }
        return dispose;
    }
    
    //Added by Vyjayanthi on 27/01/2004
    /** Perform validation for approval
     * @return true if successful, false otherwise
     */
    private boolean validateDataForApproval(){
        ProposalDevelopmentFormBean propDevFormBean = this.proposalDevelopmentFormBean;
        Vector vecData = new Vector();
        propDevFormBean.setCreationStatusCode(4);
        try{
            if( chkDoNotGenerateInstProp.isSelected() ){
                dlgSubmitToSponsor.setVisible(false);
                isMenuEnabled = true;
                CoeusOptionPane.showInfoDialog(
                    coeusMessageResources.parseMessageKey(APPROVED_STATUS));
                propDevFormBean.setCreationStatusCode(4);
                updateProposalStatus(propDevFormBean);
                return true;
            }else if( chkGenerateInstProp.isSelected() ){
                dlgSubmitToSponsor.setVisible(false);
                generate = "G";
                instPropNumber = null;
                isMenuEnabled = false;
            }else if(txtOriginalProp.getText() != null &&
            !txtOriginalProp.getText().equals(EMPTY_STRING)){
                instPropNumber = txtOriginalProp.getText().trim();
                boolean isValidInstProp = validateProposalNumber();
                if(!isValidInstProp){
                    isMenuEnabled = true;
                    CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(NOT_A_VALID_NUMBER));
                    return false;
                }else{
                    generate = "N";
                    dlgSubmitToSponsor.setVisible(false);
                    isMenuEnabled = false;
                }
            }else {
                isMenuEnabled = true;
                CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(SELECT_ANY_OPTION));
                return false;
            }
            propDevFormBean.setCreationStatusCode(creationStatus);
            updateProposalStatus(propDevFormBean);
            vecData.addElement(proposalID);
            vecData.addElement(instPropNumber);
            vecData.addElement(generate);
            vecData.addElement(submissionType);
            vecData.addElement(submissionStatus);
            vecData.addElement(new Integer(propDevFormBean.getCreationStatusCode() ));
            feedInstPropForApprove(vecData);
            submitGrantsGov();
            isMenuEnabled = false;
        }catch(CoeusClientException coeusClientException){
            coeusClientException.printStackTrace();
        }catch(CoeusException coeusException){
            coeusException.printStackTrace();
            CoeusOptionPane.showErrorDialog(coeusException.getMessage());
        }
        return true;
    }
        
    //Added by Vyjayanthi on 27/01/2004
    /** To update the proposal status */
    private void updateProposalStatus(ProposalDevelopmentFormBean proposalDevelopmentFormBean)
    throws CoeusClientException{
        RequesterBean requester = new RequesterBean();
        requester.setFunctionType(UPDATE_PROP_CREATION_STATUS);
        requester.setDataObject(proposalDevelopmentFormBean);
        AppletServletCommunicator comm = new AppletServletCommunicator(connectTo, requester);
        comm.send();

        ResponderBean response = comm.getResponse();
        if ( response != null ){
            if( response.isSuccessfulResponse() ){
                CoeusOptionPane.showInfoDialog(
                    coeusMessageResources.parseMessageKey(PROPOSAL_APPROVED));
                setProposalDevelopmentFormBean(
                    (ProposalDevelopmentFormBean)response.getDataObject());
            }else{
                throw new CoeusClientException(response.getMessage());
            }
        }
    }
    
    //Added by Vyjayanthi on 27/01/2004
    /** Method called if the form is opened in approval mode */
    public void feedInstPropForApprove(Vector vecData) throws CoeusClientException{
        String message;
        int checkEDI;
        boolean hasEDIRight = false;
        String instProp = "";
        
        RequesterBean requester = new RequesterBean();
        requester.setFunctionType(GENERATE_INST_PROP_FOR_APPROVE);
        requester.setDataObjects(vecData);
        AppletServletCommunicator comm = new AppletServletCommunicator(connectTo, requester);
        comm.send();

        ResponderBean response = comm.getResponse();
        if ( response != null ){
            if( response.isSuccessfulResponse() ){
                Vector vecDetails  = response.getDataObjects();
                instProp = (String)vecDetails.get(0);
                checkEDI = ((Integer) vecDetails.get(1)).intValue();
                hasEDIRight = ((Boolean) vecDetails.get(2)).booleanValue();
                if( generate.equalsIgnoreCase("N") ){
                    message = "A new sequence number for Institute proposal " + 
                                instProp + " has been created.";
                }else{
                    message = "Institute Proposal " + instProp+ " has been generated";
                }
                if(checkEDI == 1){
                    if( !hasEDIRight ){
                        CoeusOptionPane.showInfoDialog(message + 
                            "\n EDI transaction cannot be generated as you have insufficient rights.");
                    }else{
                        int confirm = CoeusOptionPane.showQuestionDialog(message + 
                            "\n Do you want to generate EDI transaction for the proposal?", 
                            CoeusOptionPane.OPTION_YES_NO, CoeusOptionPane.DEFAULT_YES);
                        switch( confirm ){
                            case JOptionPane.YES_OPTION:
                               GenerateEdiTxnForm generateEdiTxnForm = new GenerateEdiTxnForm(mdiForm, true, proposalID);
                               generateEdiTxnForm.display();
                                break;
                            case JOptionPane.NO_OPTION:
                                break;
                        }
                    }
                }else{
                    //No EDI for this proposal
                    CoeusOptionPane.showInfoDialog(message);
                }
            }else{
                throw new CoeusClientException(response.getMessage());
            }
        }
    }    
    
    private boolean  validateProposalNumber() throws CoeusClientException{
        boolean isValidNumber=false;
        RequesterBean request = new RequesterBean();
        request.setFunctionType(VALIDATE_NUMBER);
        request.setDataObject(instPropNumber);
        AppletServletCommunicator comm = new AppletServletCommunicator(connectTo, request);
        comm.send();
        ResponderBean response = comm.getResponse();
        if(response.isSuccessfulResponse()){
            System.out.println("SuccessFull Response in MedusaDetailsForm");
            Boolean obj = (Boolean) response.getDataObject();
            isValidNumber = obj.booleanValue();
        }else {
            System.out.println("Error while loading Details in MedusaDetailsForm");
            throw new CoeusClientException(response.getMessage());
        }
        return  isValidNumber ;
    }
    
    
    
    private void feedInstitutePropNumber(String proposalID, String instPropNumber,
    String generate,String submissionType,String submissionStatus) throws CoeusClientException{
        Vector dataObjects = new Vector(3,2);
        String message2 = "";
        int checkEDI;
        boolean hasEDIRight = false;
        String instProp="";
        RequesterBean request = new RequesterBean();
        
        dataObjects.addElement(proposalID);
        dataObjects.addElement(instPropNumber);
        dataObjects.addElement(generate);
        dataObjects.addElement(submissionType);
        dataObjects.addElement(submissionStatus);
        
        request.setFunctionType(FEED_INST_NUMBER);
        request.setDataObjects(dataObjects);
        AppletServletCommunicator comm = new AppletServletCommunicator(connectTo, request);
        comm.send();
        ResponderBean response = comm.getResponse();
        String message = "";
        if(response.isSuccessfulResponse()){
            System.out.println("SuccessFull Response");
            //instProp = (String) response.getDataObject();
            Vector vecDetails  = response.getDataObjects();
            instProp = (String)vecDetails.get(0);
            checkEDI = ((Integer) vecDetails.get(1)).intValue();
            hasEDIRight = ((Boolean) vecDetails.get(2)).booleanValue();
            if(generate.equalsIgnoreCase("N")){
                message = "A new sequence number for Institute proposal " + instProp + " has been created.";
            }else{
              	message =  "Institute Proposal " + instProp + " has been generated";
            }
            
            if(checkEDI != 1){
                CoeusOptionPane.showInfoDialog(message);
            }else{
                if(!hasEDIRight){
                    //No Right Message
                    message2 = message + " \n An EDI transaction cannot be generated as you have insufficient rights.";
                    CoeusOptionPane.showInfoDialog(message2);
                }else{
                    //Show Form
                    message2 = message + " \n Do you want to generate EDI transaction for the proposal ?";
                    int confirm = CoeusOptionPane.showQuestionDialog(message2,  CoeusOptionPane.OPTION_YES_NO,
                    CoeusOptionPane.DEFAULT_YES);
                    switch(confirm){
                        case JOptionPane.YES_OPTION:
                            GenerateEdiTxnForm generateEdiTxnForm = new GenerateEdiTxnForm(mdiForm, true, proposalID);
                            generateEdiTxnForm.display();
                            break;
                        case JOptionPane.NO_OPTION:
                            break;
                    }
                }
            }
        }else {
            System.out.println("Error while loading Details...");
            throw new CoeusClientException(response.getMessage());
        }
    }
    
    
    public boolean display(){
        dlgSubmitToSponsor.setVisible(true);
        return isMenuEnabled;
    }
    
    private class CustomFocusAdapter extends FocusAdapter{
        public void focusLost(FocusEvent fe){
            String instProp = txtOriginalProp.getText();
            //if(txtOriginalProp.getText().trim().equals(null) || txtOriginalProp.getText().trim().equals(EMPTY_STRING)){
            if(instProp.trim().equals(null) || instProp.trim().equals(EMPTY_STRING) || instProp.trim().length() < 0){
                chkDoNotGenerateInstProp.setEnabled(true);
                chkGenerateInstProp.setEnabled(true);
            }else{
                chkDoNotGenerateInstProp.setEnabled(false);
                chkGenerateInstProp.setEnabled(false);
                chkDoNotGenerateInstProp.setSelected(false);
                chkGenerateInstProp.setSelected(false);
            }
        }
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        lblOriginalProp = new javax.swing.JLabel();
        lblEnterOriginalProp = new javax.swing.JLabel();
        txtOriginalProp = new edu.mit.coeus.utils.CoeusTextField();
        lblOriginalInstProp = new javax.swing.JLabel();
        chkGenerateInstProp = new javax.swing.JCheckBox();
        lblGenerateInstProp = new javax.swing.JLabel();
        lblChoose = new javax.swing.JLabel();
        lblDoNotGenerate = new javax.swing.JLabel();
        lblPropNotSubmitted = new javax.swing.JLabel();
        chkDoNotGenerateInstProp = new javax.swing.JCheckBox();
        btnOk = new javax.swing.JButton();
        btnInstProposalSearch = new javax.swing.JButton();

        setLayout(new java.awt.GridBagLayout());

        lblOriginalProp.setFont(CoeusFontFactory.getLabelFont());
        lblOriginalProp.setText("If you know the original proposal number:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 8, 5, 0);
        add(lblOriginalProp, gridBagConstraints);

        lblEnterOriginalProp.setFont(CoeusFontFactory.getNormalFont());
        lblEnterOriginalProp.setText("Enter the original proposal number:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 20, 5, 0);
        add(lblEnterOriginalProp, gridBagConstraints);

        txtOriginalProp.setMinimumSize(new java.awt.Dimension(90, 22));
        txtOriginalProp.setPreferredSize(new java.awt.Dimension(90, 22));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        add(txtOriginalProp, gridBagConstraints);

        lblOriginalInstProp.setFont(CoeusFontFactory.getLabelFont());
        lblOriginalInstProp.setText("If you do not know the original Institute Proposal number:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 8, 5, 0);
        add(lblOriginalInstProp, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        add(chkGenerateInstProp, gridBagConstraints);

        lblGenerateInstProp.setFont(CoeusFontFactory.getNormalFont());
        lblGenerateInstProp.setText("Generate a new Institute Proposal number.");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 20, 0, 0);
        add(lblGenerateInstProp, gridBagConstraints);

        lblChoose.setFont(CoeusFontFactory.getNormalFont());
        lblChoose.setText("(Choose this option only if  you are sure there is no Institute Proposal in the system)");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 20, 15, 0);
        add(lblChoose, gridBagConstraints);

        lblDoNotGenerate.setFont(CoeusFontFactory.getNormalFont());
        lblDoNotGenerate.setText("Do NOT generate a new institute proposal number.");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 20, 0, 0);
        add(lblDoNotGenerate, gridBagConstraints);

        lblPropNotSubmitted.setFont(CoeusFontFactory.getNormalFont());
        lblPropNotSubmitted.setText("(The proposal will not be submitted to the sponsor)");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 20, 5, 0);
        add(lblPropNotSubmitted, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        add(chkDoNotGenerateInstProp, gridBagConstraints);

        btnOk.setFont(CoeusFontFactory.getLabelFont());
        btnOk.setMnemonic('O');
        btnOk.setText("OK");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 5);
        add(btnOk, gridBagConstraints);

        btnInstProposalSearch.setIcon(new javax.swing.ImageIcon( getClass().getClassLoader().getResource(CoeusGuiConstants.SEARCH_ICON)));
        btnInstProposalSearch.setMaximumSize(new java.awt.Dimension(23, 23));
        btnInstProposalSearch.setMinimumSize(new java.awt.Dimension(23, 23));
        btnInstProposalSearch.setPreferredSize(new java.awt.Dimension(23, 23));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 3, 5);
        add(btnInstProposalSearch, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnInstProposalSearch;
    private javax.swing.JButton btnOk;
    private javax.swing.JCheckBox chkDoNotGenerateInstProp;
    private javax.swing.JCheckBox chkGenerateInstProp;
    private javax.swing.JLabel lblChoose;
    private javax.swing.JLabel lblDoNotGenerate;
    private javax.swing.JLabel lblEnterOriginalProp;
    private javax.swing.JLabel lblGenerateInstProp;
    private javax.swing.JLabel lblOriginalInstProp;
    private javax.swing.JLabel lblOriginalProp;
    private javax.swing.JLabel lblPropNotSubmitted;
    private edu.mit.coeus.utils.CoeusTextField txtOriginalProp;
    // End of variables declaration//GEN-END:variables

    /** This method triggers all actions based on the event occured
     * @param actionEvent takes the actionEvent */
    public void actionPerformed(ActionEvent e) {
        if(e.getSource().equals(btnOk)){
            //Check if entered proposal is same as proposal in the proposal window
            String newOriginalProposal = txtOriginalProp.getText();
            //Case - 3349 - START
            String originalProposal = proposalDevelopmentFormBean.getContinuedFrom();
            if(originalProposal != null && newOriginalProposal != null && !originalProposal.equals(newOriginalProposal)) {
                //Case - 3349 - END
                //Warn
                String message = coeusMessageResources.parseMessageKey("proposalSubmitToSponsor_exceptionCode.1124");
                int selection = CoeusOptionPane.showQuestionDialog(message, CoeusOptionPane.OPTION_YES_NO, CoeusOptionPane.DEFAULT_NO);
                if(selection == CoeusOptionPane.SELECTION_NO) {
                    return ;
                }
            }
            
            //Modified by Vyjayanthi on 27/01/2004 to perform actions based on the opening mode
            if( getFormMode() == SUBMIT_TO_SPONSOR_MODE ){
                if (validateData()) {
                    dlgSubmitToSponsor.dispose();
                }
            }else if( getFormMode() == APPROVE_MODE ){
                if( validateDataForApproval() ){
                    dlgSubmitToSponsor.dispose();
                }
            }
            if(isHierarchy() && isParent()){
                updateChildStatus(proposalID);
            }
        }else if(e.getSource().equals(btnInstProposalSearch)) {
            try {
             CoeusSearch coeusSearch = new CoeusSearch(CoeusGuiConstants.getMDIForm(), "PROPOSALSEARCH", CoeusSearch.TWO_TABS);
             coeusSearch.showSearchWindow();
             HashMap selected = coeusSearch.getSelectedRow();
             if (selected != null && !selected.isEmpty() ) {
                 String proposalNumber=Utils.convertNull(selected.get("PROPOSAL_NUMBER"));
                 
                 //if(proposalNumber.trim().equals(txtOriginalProp.getText().trim())) {
                 //    log(coeusMessageResources.parseMessageKey("cannot_be_same_prop_num_exceptionCode.2303"));
                 //}else {
                     txtOriginalProp.setText(proposalNumber);
                 //}
             }
         } catch (Exception exception) {
             //e.printStackTrace();
             CoeusOptionPane.showErrorDialog(exception.getMessage());
         }
        }
    }
    
    //Code Added by Vyjayanthi on 27/01/2004 - Start
    /** Getter for property formMode.
     * @return Value of property formMode.
     *
     */
    public char getFormMode() {
        return formMode;
    }
    
    /** Setter for property formMode.
     * @param formMode New value of property formMode.
     *
     */
    public void setFormMode(char formMode) {
        this.formMode = formMode;
    }
    
    /** Getter for property creationStatus.
     * @return Value of property creationStatus.
     *
     */
    public int getCreationStatus() {
        return creationStatus;
    }
    
    /** Setter for property creationStatus.
     * @param creationStatus New value of property creationStatus.
     *
     */
    public void setCreationStatus(int creationStatus) {
        this.creationStatus = creationStatus;
    }
    
    /** Getter for property proposalDevelopmentFormBean.
     * @return Value of property proposalDevelopmentFormBean.
     *
     */
    public edu.mit.coeus.propdev.bean.ProposalDevelopmentFormBean getProposalDevelopmentFormBean() {
        return proposalDevelopmentFormBean;
    }
    
    /** Setter for property proposalDevelopmentFormBean.
     * @param proposalDevelopmentFormBean New value of property proposalDevelopmentFormBean.
     *
     */
    public void setProposalDevelopmentFormBean(edu.mit.coeus.propdev.bean.ProposalDevelopmentFormBean proposalDevelopmentFormBean) {
        this.proposalDevelopmentFormBean = proposalDevelopmentFormBean;
    }
    
    /**
     * Getter for property observable.
     * @return Value of property observable.
     */
    public edu.mit.coeus.utils.BaseWindowObservable getObservable() {
        return observable;
    }
    
    /**
     * Setter for property observable.
     * @param observable New value of property observable.
     */
    public void setObservable(edu.mit.coeus.utils.BaseWindowObservable observable) {
        this.observable = observable;
    }
    
     /** Update the child proposal's creation status code, if the parent proposal
     *performed actions like Submit, Approve, Reject, PostSubmission
     */
    private boolean updateChildStatus(String proposalNumber) {
        boolean success = false;
        Vector data = new Vector();
        RequesterBean request = new RequesterBean();
        try{
            request.setFunctionType( UPDATE_CHILD_STATUS );
            request.setDataObject( proposalNumber );
            AppletServletCommunicator comm = new AppletServletCommunicator(
            connectTo, request);
            comm.send();
            ResponderBean response = comm.getResponse();
            if( response.isSuccessfulResponse() ){
                success = ((Boolean)response.getDataObject()).booleanValue();
            }else{
                throw new Exception(response.getMessage());
            }
        }catch (Exception ex){
            ex.printStackTrace();
            CoeusOptionPane.showErrorDialog(ex.getMessage());
        }
        return success;
    }
    
    /**
     * Getter for property hierarchy.
     * @return Value of property hierarchy.
     */
    public boolean isHierarchy() {
        return hierarchy;
    }    
    
    /**
     * Setter for property hierarchy.
     * @param hierarchy New value of property hierarchy.
     */
    public void setHierarchy(boolean hierarchy) {
        this.hierarchy = hierarchy;
    }    
    
    /**
     * Getter for property parent.
     * @return Value of property parent.
     */
    public boolean isParent() {
        return parent;
    }
    
    /**
     * Setter for property parent.
     * @param parent New value of property parent.
     */
    public void setParent(boolean parent) {
        this.parent = parent;
    }
    
    //Code Added by Vyjayanthi on 27/01/2004 - End
    //comment out all for case 2955
    //Case - 2920 - START
    //public void setDevProposalText(String text) {
    //    lblDevProposal.setText("Development proposal   " +proposalID + " "+text);
    //}
    //Case - 2920 - END
}
