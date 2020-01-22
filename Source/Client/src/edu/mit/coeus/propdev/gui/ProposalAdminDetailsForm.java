/*
 * @(#)ProposalAdminDetailsForm.java    Created on May 20, 2003, 10:46 AM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 * 
 */

package edu.mit.coeus.propdev.gui;

/**
 *
 * @author  senthil
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Vector;
import java.util.Hashtable;

import edu.mit.coeus.gui.*;
import edu.mit.coeus.propdev.bean.*;
import edu.mit.coeus.utils.*;
import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;

/** This is the Base Admin Form which contains the Admin Details
 * panel and the Investigator Certify Details Panel.
 */
public class ProposalAdminDetailsForm extends javax.swing.JComponent implements TypeConstants{

    private JPanel pnlForm;
    private JTabbedPane tbdPnTabbedPane ;
    private String loginUserName;
    private String hasRightFlag;
    ProposalAdminFormBean proposalAdminFormBean;
    private boolean saveRequired;
    private ProposalAdminInvestigatorCertificationForm proposalAdminInvestigatorCertificationForm;
    private JDialog dlgParentComponent;
    private CoeusAppletMDIForm coeusAppletMDIForm;
    private char functionType;
    private String proposalNumber;

    private CoeusMessageResources coeusMessageResources;
    private final String PROPOSAL_MAINT = "/ProposalMaintenanceServlet";
    private final String PROPOSAL_ADMIN_DETAILS_TITLE = "Admin Details";
    private final String PROPOSAL_ADMIN_INVESTIGATOR_CERTIFICATION_TITLE = "Inv. Certifications";
    
    private Vector vCertify;
    /** Creates a new instance of ProposalAdminDetailsForm */
    public ProposalAdminDetailsForm() {
        
    }
   
    /** Creates a new instance of ProposalAdminDetailsForm using
     * the Proposal Number and Function Type. Function Type is
     * passed as DISPLAY_MODE from ProposalBaseWindow.java.
     * @param proposalNumber The Proposal Number is obtained from the base window.
     * @param functionType The fuctionType determines whether the data is going to be display
     */
    public ProposalAdminDetailsForm(String proposalNumber,char functionType) {
        
        this.functionType = functionType;
        this.proposalNumber = proposalNumber;
        
        coeusMessageResources = CoeusMessageResources.getInstance();
        Vector vecFrm = getDataFromDB();
        setFormData(vecFrm);
        
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    
    private Vector getDataFromDB() {
        
        String connectTo = CoeusGuiConstants.CONNECTION_URL 
                + PROPOSAL_MAINT;
        /* connect to the database and get the formData for the
         * given proposalNumber
         */
        RequesterBean requesterBean = new RequesterBean();
        requesterBean.setId(proposalNumber);
        requesterBean.setFunctionType('E');
        
        AppletServletCommunicator appletServletCommunicator = new AppletServletCommunicator(
                connectTo, requesterBean);
                appletServletCommunicator.send();
        ResponderBean responderBean = appletServletCommunicator.getResponse();

        return responderBean.getDataObjects();
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    
    private void setFormData(Vector vformData) {
        
        hasRightFlag = (String)vformData.elementAt(0);
        proposalAdminFormBean = (ProposalAdminFormBean)vformData.elementAt(1);
        vCertify = (Vector)vformData.elementAt(2);
        /*if (proposalAdminFormBean == null){
            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(
                    "proposal_AdminDetails_exceptionCode.7109"));
        }else{*/
        
            dlgParentComponent = new JDialog(CoeusGuiConstants.getMDIForm(),
            "Proposal Admin Details", true);
            dlgParentComponent.getContentPane().add(showProposalAdminDetails(
            CoeusGuiConstants.getMDIForm()));
            dlgParentComponent.pack();
            dlgParentComponent.setResizable(false);

            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            Dimension dlgSize = dlgParentComponent.getSize();
            dlgParentComponent.setLocation(screenSize.width/2 - (dlgSize.width/2),
            screenSize.height/2 - (dlgSize.height/2));

            dlgParentComponent.addKeyListener(new KeyAdapter(){
                public void keyReleased(KeyEvent ke){
                    if(ke.getKeyCode() == KeyEvent.VK_ESCAPE){
                        performWindowClosing();
                    }
                }
            });
            dlgParentComponent.addWindowListener(new WindowAdapter(){
                 public void windowActivated(WindowEvent we) {
                    requestDefaultFocus();
                }
                
                public void windowClosing(WindowEvent we){
                    performWindowClosing();
                }
                
            });
            dlgParentComponent.show();
        //}
    }
    
   
    
    /** Closes the dialog */
    private void closeDialog(java.awt.event.WindowEvent evt) {
        setVisible(false);
        dlgParentComponent.dispose();
    }
   /**
     * This method is used to perform the Window closing operation
     */
    private void performWindowClosing(){
        int option = JOptionPane.NO_OPTION;
        try {
            Vector updCertifyVec = proposalAdminInvestigatorCertificationForm.getFormData();
        }catch (Exception e){
            proposalAdminInvestigatorCertificationForm.setSaveRequired(false);
        }
        if (proposalAdminInvestigatorCertificationForm.isSaveRequired()) {
               option
                    = CoeusOptionPane.showQuestionDialog(
                        coeusMessageResources.parseMessageKey(
                                                "saveConfirmCode.1002"),
                        CoeusOptionPane.OPTION_YES_NO_CANCEL,
                        CoeusOptionPane.DEFAULT_YES);
            }
            if(option == JOptionPane.YES_OPTION){
                try{
                    updateCertifyDetails();
                    dlgParentComponent.dispose();
                }catch(Exception e){
                    e.printStackTrace();
                    CoeusOptionPane.showErrorDialog(e.getMessage());
                }
            }else if(option == JOptionPane.NO_OPTION){
                dlgParentComponent.dispose();
            }
    }

    /** This Method is used to create the Base Panel with
     * the Buttons OK and Cancel.
     *
     * @param coeusAppletMDIForm coeusAppletMDIForm
     *
     * @return JComponent.
     */    
    public JComponent showProposalAdminDetails(CoeusAppletMDIForm coeusAppletMDIForm){

        this.coeusAppletMDIForm = coeusAppletMDIForm;
        pnlForm = new JPanel();
        pnlForm.setLayout(new BorderLayout(10,10));
        pnlForm.add(createForm(),BorderLayout.CENTER);
        pnlForm.setSize(650,250);
        pnlForm.setLocation(200,200);

        JPanel pnlMain = new JPanel();
        JPanel pnlButtons = new JPanel();
        pnlButtons.setLayout(new BorderLayout(10,10));
        JButton btnOk = new JButton();
        JButton btnCancel = new JButton();
        GridBagConstraints gridBagConstraints = new GridBagConstraints();

        pnlMain.setLayout(new GridBagLayout());
        btnOk.setMnemonic('O');
        btnOk.setText("OK");
                
        if( hasRightFlag.equalsIgnoreCase("Yes") ){
            btnOk.setEnabled(true);
        }else{
            btnOk.setEnabled(false);
        } 
        
        btnOk.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                try{
                    
                    updateCertifyDetails();
                    dlgParentComponent.dispose();
                    
                }catch(Exception e){
                    e.printStackTrace();
                    CoeusOptionPane.showErrorDialog(e.getMessage());
                }
            }
        });
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new Insets(5, 5, 0, 8);
        gridBagConstraints.anchor = GridBagConstraints.NORTH;
        pnlMain.add(btnOk, gridBagConstraints);

        btnCancel.setMnemonic('C');
        btnCancel.setText("Cancel");

        /*
         * This listener is associated with Cancel button to check before
         * closing the window for confirmation of changes done
         */
        btnCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                performWindowClosing();
            }
        });
        btnOk.setFont(CoeusFontFactory.getLabelFont());
        btnCancel.setFont(CoeusFontFactory.getLabelFont());

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new Insets(5, 5, 0, 8);
        gridBagConstraints.anchor = GridBagConstraints.NORTH;
        pnlMain.add(btnCancel, gridBagConstraints);
        pnlMain.setAlignmentX(Container.TOP_ALIGNMENT);

        pnlButtons.add(pnlMain,BorderLayout.NORTH);
        pnlForm.add(pnlButtons, BorderLayout.EAST);
        this.setLayout(new BorderLayout());
        add(pnlForm);
        
        // Added by chandra 08/02/2004 - start
        Component[] comp = {btnOk, btnCancel};
        ScreenFocusTraversalPolicy traversal = new ScreenFocusTraversalPolicy(comp);
        pnlMain.setFocusTraversalPolicy(traversal);
        pnlMain.setFocusCycleRoot(true);
        return this;
        // Added by chandra 08/02/2004 - end
    }
    
  
    
    /** This method is used to created the Admin Details and Inv Certifications
     * panels and add it to the main tabbed panel.
     * @return JTabbedPane
     */    
        public JTabbedPane createForm() {
        
        JTabbedPane admDetFormTabbedPane = new CoeusTabbedPane(CoeusTabbedPane.CTRL_T);
        
        proposalAdminInvestigatorCertificationForm =
            new ProposalAdminInvestigatorCertificationForm(functionType, vCertify);
             
        FlowLayout layout = new FlowLayout(FlowLayout.LEFT);
        
        JPanel pnlAdminDetails = new JPanel(layout);
        JPanel pnlInvestigatorCertify = new JPanel(layout);
        
        if(proposalAdminFormBean!=null){
            ProposalAdminDetails propAdminDetails = new ProposalAdminDetails(proposalAdminFormBean);
            pnlAdminDetails.add(propAdminDetails);
        }
        else
        {
            pnlAdminDetails.add(new JPanel());
        }

        pnlInvestigatorCertify.add(proposalAdminInvestigatorCertificationForm);

        admDetFormTabbedPane.setFont(CoeusFontFactory.getNormalFont());

        JTabbedPane tbdPnTabbedPane = new CoeusTabbedPane(CoeusTabbedPane.CTRL_T);
        tbdPnTabbedPane.setFont( CoeusFontFactory.getNormalFont() );
        tbdPnTabbedPane.addTab(PROPOSAL_ADMIN_DETAILS_TITLE, pnlAdminDetails );
        tbdPnTabbedPane.addTab(PROPOSAL_ADMIN_INVESTIGATOR_CERTIFICATION_TITLE,  pnlInvestigatorCertify);

        return tbdPnTabbedPane;
        
    }
    private void updateCertifyDetails()throws Exception{

            Vector updCertifyVec = proposalAdminInvestigatorCertificationForm.getFormData();
            if (proposalAdminInvestigatorCertificationForm.isSaveRequired()) {
            String connectTo = CoeusGuiConstants.CONNECTION_URL 
                    + PROPOSAL_MAINT;
            /* connect to the database and send the Admin Certification Form Data for the
             * given proposalNumber
             */
            RequesterBean requesterBean = new RequesterBean();
            requesterBean.setDataObject(updCertifyVec);
            requesterBean.setFunctionType('C');
            AppletServletCommunicator comm = new AppletServletCommunicator(
                    connectTo, requesterBean);
                    comm.send();
            ResponderBean responderBean = comm.getResponse();
        }
    }

    /** Getter for property saveRequired.
     * @return Value of property saveRequired.
     */
    public boolean isSaveRequired() {
        return saveRequired;
    }
    
    /** Setter for property saveRequired.
     * @param saveRequired New value of property saveRequired.
     */
    public void setSaveRequired(boolean saveRequired) {
        this.saveRequired = saveRequired;
    }
}