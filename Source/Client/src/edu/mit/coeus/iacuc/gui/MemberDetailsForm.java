/*
 * @(#)MemberDetailsForm.java
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 */

/* PMD check performed, and commented unused imports and variables on 18-OCT-2010
 * by Johncy M John
 */

package edu.mit.coeus.iacuc.gui;

import edu.mit.coeus.iacuc.bean.CommitteeMemberExpertiseBean;
import edu.mit.coeus.iacuc.bean.CommitteeMembershipDetailsBean;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Vector;
import java.util.Hashtable;

import edu.mit.coeus.gui.*;
import edu.mit.coeus.utils.*;
import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;



/**
 * <code> MemberDetailsForm </code> is a class which constructs the tabbed pages
 * with all the required details for Members.
 *
 * @author Lenin Fernandes
 * @version 1.0  September, 21, 2002
 * @modified by Phaneendra Kumar B
 */

public class MemberDetailsForm extends JComponent{

    JPanel pnlForm;
    JButton btnOk,btnCancel;
    MemberMaintenanceForm memberMaintenanceForm;
    AreaOfResearchDetailForm areaOfResearchDetailForm;
    MemberStatusForm memberStatusForm;
    CommitteeMembershipDetailsBean memberdetail;
    boolean addFlag = false;
    private JDialog dlgParentComponent;
    private CoeusAppletMDIForm mdiForm;
    char functionType = 'D';
    private String connectionURL = CoeusGuiConstants.CONNECTION_URL;
    private WorkingJTabbedPane tbdPnTabbedPane ;
    private boolean saveRequired = false;
    private boolean memberExpertiseModified = false;
    private Hashtable availableMembers;
    //for duplicate personId bug fix
    private Hashtable allMemberList;
    //for duplicate personId fix
    private Vector membershipTypes = null;
    private int currentMemberRow = -1;
    private boolean memberSaved;

    private String committeeId;
    private String personId;

    //holds CoeusMessageResources instance used for reading message Properties.
    private CoeusMessageResources coeusMessageResources;
    //COEUSQA-2802 - Allow user to remove values for areas of research in IRB and IACUC 
    private static final int RESEARCH_AREA_TAB_INDEX = 1;
    /**
     * Default constructor
     */
    public MemberDetailsForm(){
        coeusMessageResources = CoeusMessageResources.getInstance();
    }

    /** Constructor which initializes the form components and sets the enabled
     * status for all the components depending on the specified function type.
     * It also fetches the membership details for the given <CODE>personId</CODE> of a
     * Committee. This constructor is usually used in other modules if member
     * details have to be shown for information.
     *
     * @param committeeId String representing the Committee where the given member
     * belongs to.
     * @param personId String representing Person ID whose details has to be
     * fetched.
     * @param functionType character representing the form opened mode.
     */
    public MemberDetailsForm(String committeeId, String personId, char functionType){
        this.functionType = functionType;
        this.committeeId = committeeId;
        this.personId = personId;
        memberdetail = new CommitteeMembershipDetailsBean();
        memberdetail.setCommitteeId(committeeId);
        memberdetail.setPersonId(personId);
        memberdetail = getMemberDetailsfromDB(memberdetail);
        coeusMessageResources = CoeusMessageResources.getInstance();
        dlgParentComponent = new JDialog(CoeusGuiConstants.getMDIForm(),
            "Membership Details", true);
        dlgParentComponent.getContentPane().add(createMemberDetails(
                CoeusGuiConstants.getMDIForm()));
        dlgParentComponent.setResizable(false);
        dlgParentComponent.pack();
        Dimension screenSize
                = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension dlgSize = dlgParentComponent.getSize();
        dlgParentComponent.setLocation(screenSize.width/2 - (dlgSize.width/2),
        screenSize.height/2 - (dlgSize.height/2));
        dlgParentComponent.addKeyListener(new KeyAdapter(){
            public void keyReleased(KeyEvent ke){
                if(ke.getKeyCode() == KeyEvent.VK_ESCAPE){
                    dlgParentComponent.dispose();
                }
            }
        });
       dlgParentComponent.show();
    }


    /** Constructor which constructs new <CODE>MemberDetailsForm</CODE> with the given parent
     * component and populates the class variable which holds the member details
     * with the bean given as parameter.
     *
     * @param dlgParentComponent reference to parent Dialog component.
     * @param det <CODE>CommitteeMembershipDetailsBean</CODE> with the all the member details.
     * @param functionType character specifying the form opened mode.
     */
    public MemberDetailsForm(JDialog dlgParentComponent,
            CommitteeMembershipDetailsBean det,char functionType){
        this.dlgParentComponent = dlgParentComponent;
        addFlag = true;
        this.functionType= functionType;
        memberdetail = det;
        coeusMessageResources = CoeusMessageResources.getInstance();
    }

    /** Constructor which constructs new <CODE>MemberDetailsForm</CODE> and populates the
     * class variable which holds the member details with the bean given as
     * parameter.
     *
     * @param det <CODE>CommitteeMembershipDetailsBean</CODE> with the all the member details.
     * @param functionType character specifying the form opened mode.
     */
    public MemberDetailsForm(CommitteeMembershipDetailsBean det,
                                    char functionType ){
        memberdetail = det;
        this.functionType= functionType;
        coeusMessageResources = CoeusMessageResources.getInstance();
    }

    /** This method is used to get the <CODE>CommitteeMembershipDetailsBean</CODE> information
     * in this Committee. This will be used in <CODE>CommMembersListForm</CODE>.
     *
     * @return <CODE>CommitteeMembershipDetailsBean</CODE> which consists of all
     * details of a member.
     */
    public CommitteeMembershipDetailsBean getMemberDetails(){
        return memberdetail;

    }

    /** This method is used to set the members information to this component.
     * This will be called from <CODE>CommMembersListForm</CODE> when the user selects a
     * member in the list and opens the details form.
     *
     * @param members Hashtable which consists of CommitteeMembershipDetailsBean.
     */
    public void setAvailableMembers(Hashtable members){
        this.availableMembers = members;
    }

    /** This method is used to set the selected member index in the members
     * list in <CODE>CommMembersListForm</CODE>. This will be used to check for duplicate
     * members.
     *
     * @param selectedMemberRow Index of the selected member in members table of
     * <CODE>CommMembersListForm</CODE>.
     */
    public void setCurrentMember(int selectedMemberRow){
        this.currentMemberRow = selectedMemberRow;
    }

    /**
     * This method is used to set whether the selected member details have been
     * saved to the database or not.
     *
     * @param saved boolean value true if selected member details are saved to 
     * the database else false.
     */
    public void setMemberSaved(boolean saved){
        this.memberSaved = saved;
    }
    /** This method is used to set the member details bean to the form which will
     * be used to populate the form components.
     *
     * @param memberDetail <CODE>CommitteeMembershipDetailsBean</CODE> with all the details of
     * a member.
     */
    public void setMemberDetails(CommitteeMembershipDetailsBean memberDetail){
        this.memberdetail = memberDetail;
    }

    /**
     * This method is used to check whether there are any unsaved modifications
     * exists. This will be used while closing the window to prompt the user
     * for confirming the changes made by him.
     * @return boolean true if any unsaved modifications exists
     * else false.
     */
    public boolean isSaveRequired(){
        if(memberMaintenanceForm.isSaveRequired()
        || areaOfResearchDetailForm.isSaveRequired()){
            saveRequired = true;
        }
        return saveRequired;
    }

    /**
     * This method is used to set the status of unsaved modification in the form.
     *
     * @param save boolean value which specifies the unsaved modifications status.
     */
    public void setSaveRequired(boolean save){
        memberMaintenanceForm.setSaveRequired(save);
        areaOfResearchDetailForm.setSaveRequired(save);
    }

    /** This method creates a panel with tab pages and other required components.
     *
     * @param mdiForm reference to <CODE>CoeusAppletMDIForm</CODE>.
     * @return JComponent reference to a panel which consists of tab controls
     * and other components.
     */
    public JComponent createMemberDetails(CoeusAppletMDIForm mdiForm){
        this.mdiForm = mdiForm;
        pnlForm = new JPanel();
        pnlForm.setLayout(new BorderLayout(10,10));
        pnlForm.add(createForm(),BorderLayout.CENTER);
        pnlForm.setSize(650,300);
        pnlForm.setLocation(200,200);

        JPanel pnlOk = new JPanel();
        JPanel pnlButtons = new JPanel();
        pnlButtons.setLayout(new BorderLayout(10,10));
        btnOk = new JButton();
        btnCancel = new JButton();
        java.awt.GridBagConstraints gridBagConstraints
            = new java.awt.GridBagConstraints();

        pnlOk.setLayout(new java.awt.GridBagLayout());
        btnOk.setMnemonic('O');
        btnOk.setText("OK");
        if(functionType == 'D'){
            btnOk.setEnabled(false);
        }else{
            btnOk.setEnabled(true);
        }
        btnOk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JComponent c = (JComponent) evt.getSource();
//                if (c.getVerifyInputWhenFocusTarget()) {
//                    //c.requestFocusInWindow();
//                    if (!c.hasFocus())
//                        return;
//                }                
                try{
                    setMemberInfo();
                }catch(Exception e){
                    e.printStackTrace();
                    CoeusOptionPane.showErrorDialog(e.getMessage());
                }
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(20, 5, 0, 8);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        pnlOk.add(btnOk, gridBagConstraints);

        btnCancel.setMnemonic('C');
        btnCancel.setText("Cancel");
        /*
         * This listener is associated with Cancel button to check before
         * closing the window for confirmation of changes done
         */
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JComponent c = (JComponent) evt.getSource();
                if (c.getVerifyInputWhenFocusTarget()) {
                    //c.requestFocusInWindow();
                    if (!c.hasFocus())
                        return;
                }                
                int option = JOptionPane.NO_OPTION;
                if(functionType != 'D'){
                    if(isSaveRequired()){
                        option
                            = CoeusOptionPane.showQuestionDialog(
                                coeusMessageResources.parseMessageKey(
                                                        "saveConfirmCode.1002"),
                                CoeusOptionPane.OPTION_YES_NO_CANCEL,
                                CoeusOptionPane.DEFAULT_YES);
                    }
                    if(option == JOptionPane.YES_OPTION){
                        try{
                            setMemberInfo();
                        }catch(Exception e){
                            e.printStackTrace();
                            CoeusOptionPane.showErrorDialog(e.getMessage());
                        }
                    }else if(option == JOptionPane.NO_OPTION){
                        saveRequired = false;
                        memberMaintenanceForm.setSaveRequired(false);
                        areaOfResearchDetailForm.setSaveRequired(false);
                        memberdetail = null;
                        dlgParentComponent.dispose();
                    }
                }else{
                    dlgParentComponent.dispose();
                }

            }
        });
        btnOk.setFont(CoeusFontFactory.getLabelFont());
        btnCancel.setFont(CoeusFontFactory.getLabelFont());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 8);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        pnlOk.add(btnCancel, gridBagConstraints);
        pnlOk.setAlignmentX(java.awt.Container.TOP_ALIGNMENT);

        pnlButtons.add(pnlOk,BorderLayout.NORTH);
        pnlForm.add(pnlButtons, BorderLayout.EAST);
        this.setLayout(new BorderLayout());
        add(pnlForm);
        return this;
    }

    public void requestDefaultFocusForComponent(){
        if( functionType == 'D') {
            btnCancel.requestFocusInWindow();
        }else{
            memberMaintenanceForm.requestDefaultFocusForComponent();
        }
    }

    /** This method is used to get the available member roles from the database.
     * These roles will be used by the user to select the roles for a member.
     *
     * @return Collection of <CODE>MemberRolesBean</CODE> where each
     * bean represents a role with code and description.
     */
    public Vector getAvailableRolesfromDB(){
        String connectTo = connectionURL + "/comMntServlet";
        Vector availableRolesDB = null;
        // connect to the database 
        RequesterBean request = new RequesterBean();
        request.setFunctionType('R');
        // Added for COEUSQA-2685_IACUC - comm member areas of research should point to IACUC areas of research_start
        request.setDataObject(CoeusConstants.IACUC_COMMITTEE_TYPE_CODE);
        // Added for COEUSQA-2685_IACUC - comm member areas of research should point to IACUC areas of research_end
        AppletServletCommunicator comm
                = new AppletServletCommunicator(connectTo, request);
        comm.send();
        ResponderBean response = comm.getResponse();
        if (response!=null){
            if (response.isSuccessfulResponse()){
                availableRolesDB = (Vector)response.getDataObject();
            }
        }
        return availableRolesDB;
    }

    /** This method is used to get the available member status types from the
     * database. These status information will be used by the user to select the
     * status for a member.
     *
     * @return Collection of <CODE>ComboBoxBean</CODE>s which consists of the
     * member status type code and description.
     */
    public Vector getAvailableStatusfromDB(){

        String connectTo = connectionURL + "/comMntServlet";
        Vector availableStatusDB = null;
        // connect to the database 
        RequesterBean request = new RequesterBean();
        request.setFunctionType('P');
        AppletServletCommunicator comm
                = new AppletServletCommunicator(connectTo, request);
        comm.send();
        ResponderBean response = comm.getResponse();
        if (response!=null){
            if (response.isSuccessfulResponse()){
                availableStatusDB = (Vector)response.getDataObject();
            }
        }
        return availableStatusDB;
    }


    /** This method is used to get the available membership types from the
     * database. These types will be used by the user to select the membership
     * type for a member.
     * @return Collection of <CODE>ComboBoxBean</CODE> with all the
     * membership type codes and descriptions.
     */
    public Vector getMembershipTypesFromDB(){
        String connectTo = connectionURL + "/comMntServlet";
        // connect to the database and get the membershipTypes
        RequesterBean request = new RequesterBean();
        request.setFunctionType('T');
        AppletServletCommunicator comm
                = new AppletServletCommunicator(connectTo, request);
        comm.send();
        ResponderBean response = comm.getResponse();
        if (response!=null){
            if (response.isSuccessfulResponse()){
                membershipTypes = (Vector)response.getDataObject();
            }
        }
        return membershipTypes;
    }

    /** This method creates all the tabs used to display the Member Details.
     * @return Tabbed pane with all the tab controls and form
     * components.
     */
    public JTabbedPane createForm() {
        tbdPnTabbedPane = new WorkingJTabbedPane();
        if(!memberSaved && functionType == 'M'){
//            functionType = 'A';
        }

        // create memberMaintenanceForm tab
        memberMaintenanceForm
                = new MemberMaintenanceForm(memberdetail,functionType);
        memberMaintenanceForm.setAvailableRoles(getAvailableRolesfromDB());
        memberMaintenanceForm.setMembershipTypes(getMembershipTypesFromDB());
        memberMaintenanceForm.setAvailableStatus(getAvailableStatusfromDB());
        // create Expertise tab
        Vector expertiseData= new Vector();
        Vector statusHistory = new Vector();
        if(memberdetail!=null){
            expertiseData
                = getMemberExpertiseData(memberdetail.getMemberExpertise());
            statusHistory = memberdetail.getMemberStatusHistory();
        }
        areaOfResearchDetailForm
            = new AreaOfResearchDetailForm(functionType,expertiseData);
        // create Status tab
        memberStatusForm = new MemberStatusForm( statusHistory);
        FlowLayout layout = new FlowLayout(FlowLayout.LEFT);
        JPanel pnlDetails = new JPanel(layout);
        pnlDetails.add(memberMaintenanceForm.showMemberMaintenanceForm(
            dlgParentComponent));
        JPanel pnlExpertise = new JPanel(layout);
        pnlExpertise.add(areaOfResearchDetailForm.showAreaOfResearch(mdiForm));
        areaOfResearchDetailForm.setAORHeader("Areas of Research this member "
        +"will be reviewing");
        JPanel pnlStatus = new JPanel(layout);
        pnlStatus.add(memberStatusForm.showMemberStatusForm(dlgParentComponent));
        tbdPnTabbedPane.setFont(CoeusFontFactory.getNormalFont());
        tbdPnTabbedPane.addTab("Details", pnlDetails );
        tbdPnTabbedPane.addTab("Expertise",  pnlExpertise);
        tbdPnTabbedPane.addTab("Status",   pnlStatus);

        tbdPnTabbedPane.setSelectedIndex(0);
         /* This  will catch the tab change event in the tabbed pane.
         * If the user selects any other tab from the CommitteeTab without
         * saving the committee information then it will prompt the user
         * to save the committee information before shifting to the other tab.
         */
        tbdPnTabbedPane.addChangeListener(new ChangeListener(){
            public void stateChanged(ChangeEvent ce){
                JTabbedPane pn = (JTabbedPane)ce.getSource();
                //JComponent c = (JComponent) evt.getSource();
//                if (pn.getVerifyInputWhenFocusTarget()) {
//                    //pn.requestFocusInWindow();
//                    if (!pn.hasFocus())
//                        return;
//                }                
                
                int selectedTab = pn.getSelectedIndex();                
                try {                    
                    switch ( selectedTab ) {
                        case 0 :
                                memberMaintenanceForm.setDefaultFocusToComponent();                                  
                                 break;
                        case 1 :
                                areaOfResearchDetailForm.setDefaultFocusToComponent();                                  
                                 break;                                
                    }
                }catch(Exception e) {
                    tbdPnTabbedPane.setSelectedIndex(0);
                    CoeusOptionPane.showErrorDialog(e.getMessage());
                }
            }
        });        
        return tbdPnTabbedPane;
    }


    /** This is used to set the member information. This will set the
     * corresponding beans with the data enetered in <CODE>MemberMaintenanceForm</CODE> and
     * <CODE>MemberExpertiseForm</CODE>. Then this data will be used in <CODE>CommMembersListForm</CODE>
     * for saving member details in the database.
     *
     * @throws Exception if any validation fails in any of the member details
     * forms.
     */
    public void setMemberInfo() throws Exception{
        if(functionType != 'D'){
            memberMaintenanceForm.stopTableEditing();
            if(!memberMaintenanceForm.validateFormData()){
                tbdPnTabbedPane.setSelectedIndex(0);
            }
            //COEUSQA-2802 - Allow user to remove values for areas of research in IRB and IACUC 
            else if(!areaOfResearchDetailForm.validateData()) {
                tbdPnTabbedPane.setSelectedIndex(RESEARCH_AREA_TAB_INDEX);
            }
            else{
                Vector newAreaOfResearches = new Vector();                
                memberdetail = memberMaintenanceForm.getMemberDetails();
                Vector expertise = areaOfResearchDetailForm.getExpertiseData();
                Vector availableMemberExpertise
                        = memberdetail.getMemberExpertise();
                if(availableMemberExpertise == null){
                    availableMemberExpertise = new Vector();
                }
                if(expertise!=null && expertise.size()>0){
                    for(int expertiseRow = 0; expertiseRow<expertise.size();
                            expertiseRow++){
                        boolean found = false;
                        CommitteeMemberExpertiseBean newMemberExpertiseBean
                                = new CommitteeMemberExpertiseBean();
                        Vector memberExpertise =
                                (Vector)expertise.elementAt(expertiseRow);
                        newMemberExpertiseBean.setResearchAreaCode(
                            memberExpertise.elementAt(0).toString());
                        newMemberExpertiseBean.setResearchAreaDesc(
                            memberExpertise.elementAt(1).toString());
                        if(availableMemberExpertise!=null){
                            CommitteeMemberExpertiseBean memExpBean = null;
                            for(int availableRow = 0;
                                availableRow < availableMemberExpertise.size();
                                availableRow++){
                                memExpBean =
                                (CommitteeMemberExpertiseBean)
                                availableMemberExpertise.elementAt(availableRow);
                                if(memExpBean.getResearchAreaCode().equals(
                                 newMemberExpertiseBean.getResearchAreaCode())){
                                    found = true;
                                    break;
                                }
                            }
                        }
                        if(!found){
                            // added by ravi for new seq no. logic
                            memberExpertiseModified = true;
                            newMemberExpertiseBean.setAcType("I");
                            /* If the expertise is newly added one and the
                             * the committee is in modify mode then the member
                             * details bean acType will be set to 'U',so that it
                             * will add a new record with new sequence number in
                             * the database.This is to maintain the history of
                             * a member in the committee for every change in the
                             * member information
                             */
                            if(functionType == 'M'){
                                memberdetail.setAcType("U");
                            }
                            //added by ravi for new seq no. logic
                            availableMemberExpertise.addElement(newMemberExpertiseBean);
                        }

                    }
                    if(availableMemberExpertise!=null){
                        CommitteeMemberExpertiseBean availMemberExpBean = null;
                        for(int expRow=0;expRow<availableMemberExpertise.size();
                            expRow++){
                            boolean found = false;
                            availMemberExpBean
                            = (CommitteeMemberExpertiseBean)
                            availableMemberExpertise.elementAt(expRow);
                            for(int expertiseRow = 0;
                            expertiseRow < expertise.size();
                            expertiseRow++){
                                Vector memberExpertise
                                = (Vector)expertise.elementAt(expertiseRow);
                                String memAORCode
                                = availMemberExpBean.getResearchAreaCode();
                                if(memAORCode.equals(
                                memberExpertise.elementAt(0).toString())){
                                    found = true;
                                    break;
                                }
                            }
                            CommitteeMemberExpertiseBean newMemberExpertiseBean
                            = availMemberExpBean;
                            /* If the already exsting member expertise
                             * information is deleted set the acType of that
                             * expertise bean as 'D'
                             */
                            if(!found){
                                newMemberExpertiseBean.setAcType("D");
                                memberdetail.setAcType("U");
                                memberExpertiseModified = true;

                                availableMemberExpertise.setElementAt(
                                    newMemberExpertiseBean,expRow);
                            }
                            // added by ravi for new seq no logic
                        }
                    }

                        memberdetail.setMemberExpertise(availableMemberExpertise);
                        if(memberExpertiseModified
                                && !memberdetail.getMemberExpertiseModified()){
                            memberdetail.setMemberExpertiseModified(
                                memberExpertiseModified);
                        }

                    boolean duplicate = false;                       
                    //for duplicate personId bug fix start 
                    
//                    if(availableMembers!= null && availableMembers.size()>0){
//                        for(int row=0;row<availableMembers.size();row++){
//                            if(row != currentMemberRow){
//                                CommitteeMembershipDetailsBean detailBean
//                                = (CommitteeMembershipDetailsBean)
//                                availableMembers.get(new Integer(row));
//                                if( (detailBean.getPersonId().equals(
//                                memberdetail.getPersonId()))){                                    
//                                    
//                                    if((detailBean.getTermStartDate().compareTo(memberdetail.getTermStartDate())==0)
//                                    
//                                    ||(detailBean.getTermEndDate().compareTo( memberdetail.getTermEndDate())==0)
//                                    
//                                    || (detailBean.getTermStartDate().after( memberdetail.getTermStartDate()) && detailBean.getTermStartDate().before( memberdetail.getTermEndDate()))
//                                    
//                                    ||(detailBean.getTermEndDate().after(memberdetail.getTermStartDate())&& detailBean.getTermEndDate().before( memberdetail.getTermEndDate()))
//                                    
//                                    ||(memberdetail.getTermStartDate().before(detailBean.getTermStartDate())&& memberdetail.getTermEndDate().after(detailBean.getTermEndDate()))
//                                    
//                                    || (memberdetail.getTermStartDate().after(detailBean.getTermStartDate())&& memberdetail.getTermStartDate().before(detailBean.getTermEndDate()))){
//                                        
//                                        log(coeusMessageResources.parseMessageKey(
//                                            "memDetFrm_exceptionCode.1031"));
//                                        duplicate = true;
//                                        tbdPnTabbedPane.setSelectedIndex(0);
//                                    }
//                                    log(coeusMessageResources.parseMessageKey(
//                                    "memDetFrm_exceptionCode.1031"));
//                                    duplicate = true;
//                                    tbdPnTabbedPane.setSelectedIndex(0);
//                                }
//                            }
//                        }
//                    }
                    
                     /* This will check for the duplicate member entered
                      * by validating with the existing members form Person Id
                      * and Term dates.If they are all matching then it will
                      * propmt the user as member is duplicate.
                      */                    
                    if(functionType != 'M' && allMemberList!= null && allMemberList.size()>0){
                        for(int row=0;row<allMemberList.size();row++){
                            if(row != currentMemberRow){
                                CommitteeMembershipDetailsBean detailBean
                                = (CommitteeMembershipDetailsBean)
                                allMemberList.get(new Integer(row));
                                if( (detailBean.getPersonId().equals(
                                memberdetail.getPersonId()))){
                                    log(coeusMessageResources.parseMessageKey(
                                    "memDetFrm_exceptionCode.1031"));
                                    duplicate = true;
                                    tbdPnTabbedPane.setSelectedIndex(0);
                                }
                            }
                        }
                    }
                    //for duplicate personId bug fix end
                    if(!duplicate){
                        dlgParentComponent.dispose();
                    }
                }else{
                    tbdPnTabbedPane.setSelectedIndex(1);
                    log(coeusMessageResources.parseMessageKey(
                                            "memDetFrm_exceptionCode.1032"));
                }
            }
        }
    }

    /** 
     * This method is used to get member expertise details from the vector of
     * CommitteeMemberExpertiseBean. 
     * @return expertiseData Vector which consists of member expertise details
     * which will be used to display in the MemberExpertise Form.
     */
    private Vector getMemberExpertiseData( Vector memberExpertise){
        Vector expertiseData = new Vector();
        if (memberExpertise != null ){
            for (int count=0;count<memberExpertise.size() ;count++){
                Vector expertiseRow = new Vector();
                CommitteeMemberExpertiseBean memExpertise =
                (CommitteeMemberExpertiseBean)
                memberExpertise.elementAt(count);
                if(memExpertise.getAcType() == null
                || memExpertise.getAcType()!= "D"){
                    expertiseRow.add(memExpertise.getResearchAreaCode());
                    expertiseRow.add(memExpertise.getResearchAreaDesc());
                    expertiseData.add(expertiseRow);
                }
            }
        }
        return expertiseData;
    }
    private void log(String msg) throws Exception{
        throw new Exception(msg);
    }



    /** This method is used to get the member details from database. This returns
     * <CODE>CommitteeMembershipDetailsBean</CODE> with complete information including
     * Member Roles, Expertise and status.
     *
     * @param memberdetail <CODE>CommitteeMembershipDetailsBean</CODE> which consists of
     * committee id and person id whose details to be fetched.
     * @return <CODE>CommitteeMembershipDetailsBean</CODE> with all the member details.
     */
    public CommitteeMembershipDetailsBean getMemberDetailsfromDB(
        CommitteeMembershipDetailsBean memberdetail){
        String connectTo = connectionURL + "/comMntServlet";
        RequesterBean request = new RequesterBean();
        request.setFunctionType('B');
        request.setDataObject(memberdetail);
        // Added for COEUSQA-2685_IACUC - comm member areas of research should point to IACUC areas of research_start
        Vector vecObject = new Vector();
        vecObject.add(0,CoeusConstants.IACUC_COMMITTEE_TYPE_CODE);
        request.setDataObjects(vecObject);
        // Added for COEUSQA-2685_IACUC - comm member areas of research should point to IACUC areas of research_end
        AppletServletCommunicator comm
                = new AppletServletCommunicator(connectTo, request);
        comm.send();
        ResponderBean response = comm.getResponse();
        if (response!=null){
            if (response.isSuccessfulResponse()){
                memberdetail
                    = (CommitteeMembershipDetailsBean)response.getDataObject();
            }
        }
        return memberdetail;
    }
    public class WorkingJTabbedPane extends JTabbedPane {

        public void setSelectedIndex(int index) {
            Component comp = KeyboardFocusManager.
            getCurrentKeyboardFocusManager().getFocusOwner();

            // if no tabs are selected
            // -OR- the current focus owner is me
            // -OR- I request focus from another component and get it
            // then proceed with the tab switch

            if(getSelectedIndex()==-1 || comp==this || requestFocus(false)) {
                super.setSelectedIndex(index);
            }
        } 
     }
    
    
    
    /**
     * Getter for property allMemberList.
     * @return Value of property allMemberList.
     */
    public java.util.Hashtable getAllMemberList() {
        return allMemberList;
    }    
    
    /**
     * Setter for property allMemberList.
     * @param allMemberList New value of property allMemberList.
     */
    public void setAllMemberList(java.util.Hashtable allMemberList) {
        this.allMemberList = allMemberList;
    }
    
}
