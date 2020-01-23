/*
 * @(#)MemberMaintenanceForm.java  1.0  19/9/02
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */


package edu.mit.coeus.irb.gui;

import java.awt.event.*;
import java.util.Vector;
import edu.mit.coeus.utils.*;
import edu.mit.coeus.search.gui.CoeusSearch;
import edu.mit.coeus.irb.bean.*;
import edu.mit.coeus.rolodexmaint.bean.*;
import edu.mit.coeus.departmental.gui.PersonDetailForm;
import edu.mit.coeus.rolodexmaint.gui.RolodexMaintenanceDetailForm;

import java.text.SimpleDateFormat;
import javax.swing.*;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import javax.swing.table.*;
import edu.mit.coeus.gui.*;
import edu.mit.coeus.brokers.*;
import javax.swing.border.*;
import java.awt.*;
import java.sql.Date;



/** <code> MemberMaintenanceForm </code> is a form class which display all the
 * fields of member details and will be used to <CODE>add/modify/display</CODE> the member
 * details.
 *
 * @author Lenin Fernandes
 * @version: 1.0 September 19, 2002
 * @version: 1.1 October 1, 2002
 * @author ravikanth
 */


public class MemberMaintenanceForm extends JComponent {

    //holds reference to  CoeusAppletMDIForm
    private final CoeusAppletMDIForm mdiForm = CoeusGuiConstants.getMDIForm();

    
    private CoeusTextField txtTermEnd;
    private CoeusTextField txtTermStart;
    private CoeusTextField txtFromDate;
    private CoeusTextField txtToDate;
    private JCheckBox chkEmployeeSearch;
    private JButton btnAddRole;
    private JLabel lblTermEnd;
    /*
     * Fix for: GNIR-I Enchancement Phase - II
     * Despcription : To introduce check box for getting Member is Paid.
     * Updated by Subramanya 11th April 2003.
     */
    private JLabel lblPaidMember;
    private JCheckBox chkPaidMember;
    
    private JPanel pnlSearch;
    private JCheckBox chkEmployee;
    private JPanel pnlDetails;
    private JPanel pnlRoles;
    private JLabel lblMemberId;
    private JLabel lblPerson;
    private JPanel pnlPersonSearch;
    private CoeusTextField txtPerson;
    private JButton btnSearch;
    private JLabel lblName;
    private JPanel pnlRoleButtons;
    private JButton btnRemoveRole;
    private JScrollPane scrPnAvailableRoles;
    private JTable tblAvailableRoles;
    private JLabel lblEmployeeInSearch;
    private JComboBox cmbMemberType;
    private JLabel lblMemberType;
    private JTable tblSelectedRoles;
    private JLabel lblEmployee;
    private JScrollPane scrPnSelectedRoles;
    private JLabel lblTermStart;
    private JPanel pnlStatus;
    private JComboBox cmbStatus;
    private JLabel lblStatus;
    private JLabel lblFromDate;
    private JLabel lblToDate;
    
    /* specifies the mode in which the screen is displayed*/
    private char functionType;
    /* specifies whether employee check box is checked */
    boolean searchFlag = true;
    /* used in searching for rolodex */
    private final String ROLODEX_SEARCH = "rolodexSearch";
    /* used to specify to search in person table */
    private final String PERSON_SEARCH = "personSearch";
    private String searchIdentifier = "";
    
    /* holds all the available member roles */
    private Vector availableRoles = new Vector();
    /* holds all the selected member roles */
    private Vector memberRoles = new Vector();
    /* holds all the column names used to show selected member roles table */
    private Vector membcolNames = new Vector();
    /* holds all the column names used to show available member roles table */
    private Vector availcolNames = new Vector();
    /* holds all the available membership types */
    private Vector membershipTypes = new Vector();
    /* holds all the available status information */
    private Vector availableStatus = new Vector();
    /* specifies modifications are saved to database or not */
    private boolean saveRequired = false;
    
    /* specifies any member role has been modified */
    private boolean rolesModified = false;
    
    // holds the string value used to display the Rolodex details
    private final String DISPLAY_TITLE = "DISPLAY ROLODEX";    
    
    /* specifies whether any change has been made to member status*/
    private boolean statusModified = false;
    /* holds connection url */
    private String connectionURL = CoeusGuiConstants.CONNECTION_URL;
    /* used to hold all the form data */
    private CommitteeMembershipDetailsBean memberDetails = null;
    /* used to hold member status information */
    private CommitteeMemberStatusChangeBean memberStatus;
    /* used in changing formats for editing and displaying date fields */
    DateUtils dtUtils = new DateUtils();
    /* used in formatting date variables */
    private SimpleDateFormat dtFormat
    = new SimpleDateFormat("MM/dd/yyyy");
    private JDialog parent;
    /* holds person id which will be sent to server for fetching person details
     */
    private String personId="";
  // Added by Chandra - To validate date properly for copying dates  03/09/2003  
    private String REQUIRED_DATE_FORMAT = "MM/dd/YYYY";
    
    //holds CoeusMessageResources instance used for reading message Properties.
    private CoeusMessageResources coeusMessageResources;
    
    private static final String TOO_MANY = "TOO_MANY";
        String oldData = "";

        //private DateVerifier dateVerifier = new DateVerifier();
    /** Creates new <CODE>MemberMaintenanceForm</CODE>
     */
    public MemberMaintenanceForm() {
        coeusMessageResources = CoeusMessageResources.getInstance();
    }
    
    /** Constructor which builds form fields and populates them with data
     * specified in <CODE>CommitteeMembershipDetailsBean</CODE> and sets the enabled status
     * for all components depending on the <CODE>functionType</CODE> specified.
     * @param detail <CODE>CommitteeMembershipDetailsBean</CODE> which consists of all the
     * details of a member.
     * @param functionType Character which specifies the mode in which the
     * form will be shown.
     * <PRE>'A' specifies that the form is in <CODE>Add Mode</CODE>.
     * 'M' specifies that the form is in <CODE>Modify Mode</CODE>.
     * 'D' specifies that the form is in <CODE>Display Mode</CODE></PRE>.
     */
    public MemberMaintenanceForm(CommitteeMembershipDetailsBean detail,
    char functionType) {
        this.functionType = functionType;
        memberDetails = detail;
        coeusMessageResources = CoeusMessageResources.getInstance();
        
    }
    
    /** This method is used to initialize the form components, set available
     * member roles, set the values for all components and to set the enable
     * status for all components depending on the <CODE>functionType</CODE> specified.
     *
     * @param parent reference to parent dialog
     * @return JComponent reference of <CODE>MemberMaintenanceForm</CODE> after
     * initialization.
     */
    public JComponent showMemberMaintenanceForm(JDialog parent) {
        this.parent =parent;
        try{
            initAvailableRoles(getAvailableRoles());
            initComponents();
            setFormData();
            formatFields();
            
        }catch(Exception ex){
        }
           
        //Added by Amit 11/18/2003
        if(functionType == CoeusGuiConstants.DISPLAY_MODE){
        java.awt.Color bgListColor = (java.awt.Color)javax.swing.UIManager.getDefaults().get("Panel.background");
            
            tblSelectedRoles.setBackground(bgListColor);
            tblAvailableRoles.setBackground(bgListColor);
        }
        else{
            tblSelectedRoles.setBackground(Color.white);
            tblAvailableRoles.setBackground(Color.white);
        }
        //End Amit
        
        tblAvailableRoles.getTableHeader().setFont(CoeusFontFactory.getLabelFont());
        tblSelectedRoles.getTableHeader().setFont(CoeusFontFactory.getLabelFont());
        
        return this;
    }
    
    /** This method is used to set the available member status list
     *
     * @param status Vector which consists of <CODE>ComboBoxBean</CODE>s of member status
     * details
     */
    public void setAvailableStatus(Vector status){
        this.availableStatus = status;
    }
    
    /**
     * This method is used to set the form data specified in
     * <CODE>MemberMaintenanceFormBean</CODE> along with the available membership types and
     * available member status information.
     */
    public void setFormData(){
        
        if((membershipTypes != null) && (membershipTypes.size()>0)){
            /* Set available membership types */
            cmbMemberType.setModel(
            (new CoeusComboBox(membershipTypes,false)).getModel());
        }
        if((availableStatus != null) && (availableStatus.size()>0)){
            /* Set available member status */
            cmbStatus.setModel(
            (new CoeusComboBox(availableStatus,false)).getModel());
        }
        if(memberDetails!=null){
            memberStatus = memberDetails.getStatusInfo();
            if(memberStatus!=null){
                txtFromDate.setText(dtUtils.formatDate(
                memberStatus.getStartDate().toString(),"dd-MMM-yyyy"));
                txtToDate.setText(dtUtils.formatDate(
                memberStatus.getEndDate().toString(),"dd-MMM-yyyy"));
                
                ComboBoxBean comboBean = new ComboBoxBean();
                comboBean.setDescription(memberStatus.getStatusDescription());
                for(int typeRow=0;typeRow<cmbStatus.getItemCount();typeRow++){
                    if(((ComboBoxBean)cmbStatus.getItemAt(typeRow))
                    .getDescription().equals(comboBean.getDescription())){
                        cmbStatus.setSelectedIndex(typeRow);
                    }
                }
            }
            
            Vector memRoles = memberDetails.getMemberRoles();
            if(memRoles != null && memRoles.size() >0){
                /* roles exist for member set them to selected roles table by
                   converting each membershipRoleBean to vector of object which
                   table can understand */
                initMemberRoles(memberDetails.getMemberRoles());
                ((DefaultTableModel)tblSelectedRoles.getModel()).setDataVector(
                getMembersRoles(),getMemberColumnNames());
                ((DefaultTableModel)
                tblSelectedRoles.getModel()).fireTableDataChanged();
                setTableModelAndEditors(tblSelectedRoles);
                if( tblSelectedRoles.getRowCount() > 0 ) {
                    tblSelectedRoles.setRowSelectionInterval(0,0);
                }
                
            }
            /* Set person id and name of the member */
            personId = ( (Utils.checkNullStr(
            memberDetails.getPersonId().toString()) == null) ? ""
            : memberDetails.getPersonId().toString());
            txtPerson.setText((memberDetails.getPersonName() == null) ? ""
            : memberDetails.getPersonName() );
            
            txtTermStart.setText((memberDetails.getTermStartDate() == null)
            ? "" : dtUtils.formatDate(
            memberDetails.getTermStartDate().toString(),
            "dd-MMM-yyyy"));
            txtTermEnd.setText((memberDetails.getTermEndDate() == null) ? ""
            : dtUtils.formatDate(
            memberDetails.getTermEndDate().toString(),
            "dd-MMM-yyyy"));
            
            /* set the selected membership type */
            ComboBoxBean comboBean = new ComboBoxBean();
            comboBean.setCode(
            new Integer(memberDetails.getMembershipTypeCode()).toString());
            comboBean.setDescription(memberDetails.getMembershipTypeDesc());
            for(int typeRow=0;typeRow<cmbMemberType.getItemCount();typeRow++){
                if(((ComboBoxBean)
                cmbMemberType.getItemAt(typeRow)).toString().equals(
                comboBean.toString())){
                    cmbMemberType.setSelectedIndex(typeRow);
                }
            }
            if(memberDetails.getNonEmployeeFlag() == 'N'){
                /* check whether member is an employee of mit and set the
                   employee checkbox accordingly */
                chkEmployeeSearch.setSelected(true);
            }
            
            if( memberDetails.getPaidMemberFlag() == 'Y' ||
            memberDetails.getPaidMemberFlag() == 'y' ){
                chkPaidMember.setSelected( true );
            }
            //chkPaidMember.setSelected( memberDetails.isPaidMemberFlag() );
        }
        
        saveRequired = false;
    }
    
    /**
     * This method is used to set the enabled status for all the components
     * depending on the functionType
     *
     * @param boolean true if functionType == 'A' or 'M' else false
     */
    
    private void setEnabledStatus(boolean enabled){
        txtTermStart.setEditable(enabled);
        txtTermEnd.setEditable(enabled);
        btnRemoveRole.setEnabled(enabled);
        btnAddRole.setEnabled(enabled);
        cmbMemberType.setEnabled(enabled);
        cmbStatus.setEnabled(enabled);
        txtFromDate.setEditable(enabled);
        txtToDate.setEditable(enabled);
        chkPaidMember.setEnabled( enabled );
    }
    
    /**
     * This method is used to set the enabled status for components used for
     * searching the person/rolodex. The person/rolodex once set to a member
     * cannot be modified after saving the member.
     *
     * @param boolean enabled if functionType== 'A' else false.
     */
    private void setSearchEnabled(boolean enabled){
        chkEmployeeSearch.setEnabled(enabled);
        btnSearch.setEnabled(enabled);
        txtPerson.setEnabled(enabled);
        
        if(!enabled){
            btnSearch.setBackground(java.awt.Color.lightGray);
        }else{
            btnSearch.setBackground(java.awt.Color.white);
        }
        
    }
    /**
     * This method is used to set the enabled status for all the components
     */
    private void formatFields(){
        switch (functionType){
            case 'A' :
                setSearchEnabled(true);
                setEnabledStatus(true);
                //Modified By sharath (Bug Fix IRB System TestingDL-01.xls) No - 5
                if(memberDetails == null) {
                    chkEmployeeSearch.setSelected(true);
                }
                else {
                    if(memberDetails.getNonEmployeeFlag() == 'N'){
                       chkEmployeeSearch.setSelected(true);
                       searchFlag = true;
                       txtPerson.setEditable(true);
                    }else {
                        chkEmployeeSearch.setSelected(false);
                        searchFlag = false;
                        txtPerson.setEditable(false);
                    }
                    
                }
                //Modified By sharath (Bug Fix IRB System TestingDL-01.xls) No - 5
                break;
            case 'M' :
                setSearchEnabled(false);
                setEnabledStatus(true);
                break;
            case 'D' :
                setSearchEnabled(false);
                setEnabledStatus(false);
                break;
        }
        
    }
    
    /**
     * This method is used to initialize the available member roles by
     * converting each memberRolesBean into vector of objects.
     *
     * @param Vector which consists of available roles in the form of
     * memberRolesBean.
     */
    private void  initAvailableRoles(Vector roles){
        Vector tempMembers = new Vector();
        Vector tempMemberDetail=null;
        edu.mit.coeus.irb.bean.MemberRolesBean detail=null;
        for (int index=0;index< roles.size(); index++){
            tempMemberDetail = new Vector();
            detail = (edu.mit.coeus.irb.bean.MemberRolesBean) roles.elementAt(index);
            tempMemberDetail.addElement(
            Integer.toString(detail.getMembershipRoleCode()));
            tempMemberDetail.addElement(detail.getMembershipRoleDescription());
            tempMembers.addElement(tempMemberDetail);
            
        }
        setAvailableRoles(tempMembers);
        
        
        
    }
    /**
     * This method is used to initialize the selected member roles by
     * converting each memberRolesBean into vector of objects.
     *
     * @param Vector which consists of selected member roles in the form of
     * memberRolesBean.
     */
    private void  initMemberRoles(Vector memberRoles){
        Vector tempMembers = new Vector();
        Vector tempMemberDetail;
        CommitteeMemberRolesBean detail=null;
        if(memberRoles !=null ){
            for (int index=0;index < memberRoles.size(); index++){
                tempMemberDetail = new Vector();
                detail  = (CommitteeMemberRolesBean) memberRoles.elementAt(index);
                if((detail.getAcType() == null) || (detail.getAcType() != "D")){
                    /* if roles are not deleted  then show them */
                    tempMemberDetail.addElement("");
                    tempMemberDetail.addElement(
                    Integer.toString(detail.getMembershipRoleCode()));
                    tempMemberDetail.addElement(detail.getMembershipRoleDesc());
                    tempMemberDetail.addElement(
                    dtUtils.formatDate(
                    detail.getStartDate().toString(),"dd-MMM-yyyy"));
                    tempMemberDetail.addElement(
                    dtUtils.formatDate(
                    detail.getEndDate().toString(),"dd-MMM-yyyy"));
                    tempMembers.addElement(tempMemberDetail);
                }
                
            }
            setMembersRoles(tempMembers);
        }
        
    }
    
    /**
     * This method is called from within the showMemberMaintenanceForm to
     * initialize the form.
     */
    private void initComponents() {
        JPanel pnlMain = new JPanel();
        Vector availcols = new Vector();
        Vector membcols = new Vector();
        availcols.addElement("Role Code");
        availcols.addElement("Description");
        setAvailableColumnNames(availcols);
        membcols.addElement("");
        membcols.addElement("RoleCode");
        membcols.addElement("Description");
        membcols.addElement("Start Date");
        membcols.addElement("End Date");
        setMemberColumnNames(membcols);      
        
        GridBagConstraints gridBagConstraints;
        pnlSearch = new JPanel();
        pnlPersonSearch = new JPanel();
        lblEmployeeInSearch = new JLabel();
        lblPerson = new JLabel();
        chkEmployeeSearch = new javax.swing.JCheckBox();
        /* add item listener to checkbox */
        chkEmployeeSearch.addItemListener(new ItemListener(){
            public void itemStateChanged(ItemEvent e) {
                if(e.getStateChange() == ItemEvent.SELECTED){
                    /* is checked then allow the user to enter person name */
                    searchFlag = true;
                    txtPerson.setEditable(true);
                }else{
                    /* if unchecked force the user to use search window for
                       searching in rolodex */
                    searchFlag = false;
                    txtPerson.setEditable(false);
                }
            }
        });
        
        //Modified By sharath (Bug Fix IRB System TestingDL-01.xls) No - 5
        chkEmployeeSearch.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                txtPerson.setText("");
            }
        });
        //Modified By sharath (Bug Fix IRB System TestingDL-01.xls) No - 5
        
        txtPerson = new CoeusTextField();
        txtPerson.setDocument(new LimitedPlainDocument(90));
        //txtPerson.setDisabledTextColor((Color)UIManager.getDefaults().get("Panel.background"));
        if((functionType == CoeusGuiConstants.DISPLAY_MODE 
        || functionType == CoeusGuiConstants.MODIFY_MODE)
        && txtPerson != null){
            txtPerson.setBackground((Color)UIManager.getDefaults().get("Panel.background"));
        }
        txtPerson.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                String personName ="";
                if (txtPerson.getText().trim().length() <= 0) {
                    edu.mit.coeus.utils.CoeusOptionPane.showErrorDialog(
                    coeusMessageResources.parseMessageKey(
                    "protoKeyStPsnlFrm_exceptionCode.1069"));
                } else {
                    if(chkEmployeeSearch.isSelected()){
                        /* if checkbox is selected search in person table and
                           get the person name */
                        personId=getPersonID(txtPerson.getText().trim());
                    }
                    if ( personId.equalsIgnoreCase("") ){
                        edu.mit.coeus.utils.CoeusOptionPane.showErrorDialog(
                        coeusMessageResources.parseMessageKey(
                        "memMntFrm_exceptionCode.1124"));
                        
                    }else if(personId.equalsIgnoreCase(TOO_MANY)){
                        edu.mit.coeus.utils.CoeusOptionPane.showErrorDialog(
                        coeusMessageResources.parseMessageKey(
                        "memMntFrm_exceptionCode.1124"));
                    }
                }
            }
        });
        txtPerson.addMouseListener(new MouseAdapter(){
        public void mouseClicked(MouseEvent mouseEvent){            
            if(mouseEvent.getClickCount() == 2){
                boolean emp= chkEmployeeSearch.isSelected();
                if(emp){
                  try{
                      String loginUserName = mdiForm.getUserName();
                      
                      //Bug Fix: Pass the person id to get the person details Start.
                      //String personName=txtPerson.getText();
                      //PersonInfoFormBean personInfoFormBean = (PersonInfoFormBean)getPersonInfo(personName);
                      //PersonDetailForm personDetailForm = new PersonDetailForm(personInfoFormBean.getPersonID(),loginUserName,'D');
                      
                      PersonDetailForm personDetailForm = new PersonDetailForm(personId,loginUserName,'D');
                      //Bug Fix: Pass the person id to get the person details End.
                      
                  }catch(Exception exception){
                      exception.printStackTrace();
                  }                    
                }//end of true emp
                else{
                     RolodexMaintenanceDetailForm frmRolodex = new RolodexMaintenanceDetailForm('V',personId);
                     frmRolodex.showForm(mdiForm,DISPLAY_TITLE,true);                                       
                }//end of false emp              
                
            }
        }
        });
        
        btnSearch = new JButton();
        btnSearch.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                if(chkEmployeeSearch.isSelected()){
                    searchIdentifier = PERSON_SEARCH;
                }
                else{
                    searchIdentifier = ROLODEX_SEARCH;
                }
                try{
                    CoeusSearch coeusSearch =
                    new CoeusSearch(parent, searchIdentifier, 1);
                    coeusSearch.showSearchWindow();
                    java.util.HashMap personInfo = coeusSearch.getSelectedRow();
                    if(personInfo !=null){
                        if( chkEmployeeSearch.isSelected()){
                            /* set the full name of person to member name */
                            txtPerson.setText(
                            (personInfo.get("FULL_NAME") == null) ? " "
                            :personInfo.get("FULL_NAME").toString());
                            personId= Utils.convertNull(
                            personInfo.get("PERSON_ID"));
                        }else{
                            /* construct the full name of person */
                            String name = "";
                            String lastName = "";
                            String firstName = "";
                            String middleName = "";
                            String suffix = "";
                            String prefix = "";
                            personId=  Utils.convertNull(
                            personInfo.get("ROLODEX_ID"));
                            lastName = Utils.convertNull(
                            personInfo.get("LAST_NAME"));
                            
                            firstName = Utils.convertNull(
                            personInfo.get("FIRST_NAME"));
                            middleName = Utils.convertNull(
                            personInfo.get("MIDDLE_NAME"));
                            
                            suffix = Utils.convertNull(
                            personInfo.get("SUFFIX"));
                            
                            prefix = Utils.convertNull(
                            personInfo.get("PREFIX"));
                            /* construct full name of the rolodex if his
                               last name is presentotherwise use his
                               organization name to display the person name
                             */
                            if ( lastName.length() > 0) {
                                name = lastName+" "+suffix+", "+prefix+" "
                                +firstName+" "+middleName;
                            } else {
                                name = Utils.convertNull(
                                personInfo.get("ORGANIZATION"));
                            }
                            
                            txtPerson.setText(name);
                            
                        }
                        txtPerson.setCaretPosition(0);
                        saveRequired = true;
                    }
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
            
        });
        pnlDetails = new JPanel();
        lblMemberId = new JLabel();
        lblName = new JLabel();
        lblEmployee = new JLabel();
        chkEmployee = new javax.swing.JCheckBox();
        
        lblTermStart = new JLabel();
        txtTermStart = new CoeusTextField();
        txtTermStart.setDocument(new LimitedPlainDocument(12));
        lblTermEnd = new JLabel();
        txtTermEnd = new CoeusTextField();
        txtTermEnd.setDocument(new LimitedPlainDocument(12));
        
        lblPaidMember = new JLabel();
        chkPaidMember = new JCheckBox();
        chkPaidMember.addChangeListener( new ChangeListener(){
            public void  stateChanged( ChangeEvent chEvent ){
                saveRequired = true;
            }
        });
       
        //chkPaidMember.setText("Paid Member");
        
        //chkPaidMember.setHorizontalTextPosition(SwingConstants.LEFT);
        lblMemberType = new JLabel();
        cmbMemberType = new javax.swing.JComboBox();
        cmbMemberType.setFont(CoeusFontFactory.getNormalFont());
        cmbMemberType.addItemListener(new ItemListener(){
            public void itemStateChanged(ItemEvent ie){
                saveRequired = true;
            }
        });
        pnlRoles = new JPanel();
        scrPnAvailableRoles = new javax.swing.JScrollPane();
        tblAvailableRoles = new JTable();
        tblAvailableRoles.setFont(CoeusFontFactory.getNormalFont());
        
        
        pnlRoleButtons = new JPanel();
        pnlRoleButtons.setFocusable(true);
        btnRemoveRole = new JButton();
        btnAddRole = new JButton();
        btnRemoveRole.addActionListener(new RoleListener());
        btnAddRole.addActionListener(new RoleListener());
        scrPnSelectedRoles = new javax.swing.JScrollPane();
        
        tblSelectedRoles = new JTable();
        tblSelectedRoles.setFont(CoeusFontFactory.getNormalFont());
                
        //Added by Amit 11/18/2003
        if(functionType == CoeusGuiConstants.DISPLAY_MODE){
        java.awt.Color bgListColor = (java.awt.Color)javax.swing.UIManager.getDefaults().get("Panel.background");
            
            tblSelectedRoles.setSelectionBackground(bgListColor);
            tblSelectedRoles.setSelectionForeground(Color.BLACK);
        }
        else{
            tblSelectedRoles.setSelectionBackground(Color.WHITE);
            tblSelectedRoles.setSelectionForeground(Color.black);
        }
        //End Amit
      
        pnlMain.setLayout(new GridBagLayout());
        pnlStatus = new JPanel();
        pnlStatus.setLayout(new GridBagLayout());
        pnlStatus.setBorder(new TitledBorder(new EtchedBorder(), "Status",
        TitledBorder.LEFT, TitledBorder.TOP, CoeusFontFactory.getLabelFont()));
        lblStatus = new JLabel();
        cmbStatus = new javax.swing.JComboBox();
        lblFromDate = new JLabel();
        lblToDate = new JLabel();
        txtFromDate = new CoeusTextField();
        txtFromDate.setDocument(new LimitedPlainDocument(12));
        txtToDate = new CoeusTextField();
        txtToDate.setDocument(new LimitedPlainDocument(12));

        // Added by Chandra 12/09/2003
        java.awt.Component[] components = {chkEmployeeSearch,txtPerson,btnSearch,txtTermStart,
        txtTermEnd,cmbMemberType,chkPaidMember,cmbStatus,txtFromDate,txtToDate,
        tblSelectedRoles,btnAddRole,btnRemoveRole,tblAvailableRoles};
        ScreenFocusTraversalPolicy  traversePolicy = new ScreenFocusTraversalPolicy( components );
        pnlMain.setFocusTraversalPolicy(traversePolicy);
        pnlMain.setFocusCycleRoot(true);
        pnlStatus.setFocusTraversalPolicy(traversePolicy);
        pnlDetails.setFocusTraversalPolicy(traversePolicy);
        pnlDetails.setFocusCycleRoot(true);
        pnlRoles.setFocusTraversalPolicy(traversePolicy);
        pnlRoleButtons.setFocusTraversalPolicy(traversePolicy);
        // End Chandra
        
        pnlDetails.setLayout(new GridBagLayout());
        
        pnlDetails.setBorder(new TitledBorder(new EtchedBorder(),
        "Member Details", TitledBorder.LEFT, TitledBorder.TOP,
        CoeusFontFactory.getLabelFont()));
        
        lblPerson.setFont(CoeusFontFactory.getLabelFont());
        lblPerson.setText("Person:");
        lblPerson.setHorizontalTextPosition(SwingConstants.RIGHT);
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = GridBagConstraints.EAST;
        gridBagConstraints.insets = new Insets(6, 6, 6, 0);
        pnlDetails.add(lblPerson, gridBagConstraints);
        
        txtPerson.setFont(CoeusFontFactory.getNormalFont());
        txtPerson.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        txtPerson.setMinimumSize(new Dimension(124, 20));
        txtPerson.setPreferredSize(new Dimension(124, 20));
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.insets = new Insets(6, 4, 6, 6);
        pnlDetails.add(txtPerson, gridBagConstraints);
        
        btnSearch.setIcon(
        new javax.swing.ImageIcon(getClass().getClassLoader().getResource(
        CoeusGuiConstants.FIND_ICON)));
        btnSearch.setMaximumSize(new Dimension(25, 20));
        btnSearch.setMinimumSize(new Dimension(25, 20));
        btnSearch.setPreferredSize(new Dimension(25, 20));
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.insets = new Insets(4, 4, 0, 0);
        pnlDetails.add(btnSearch, gridBagConstraints);
        
        lblEmployeeInSearch.setFont(CoeusFontFactory.getLabelFont());
        lblEmployeeInSearch.setText("Employee:");
        lblEmployeeInSearch.setHorizontalTextPosition(SwingConstants.RIGHT);
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = GridBagConstraints.EAST;
        gridBagConstraints.insets = new Insets(6, 6, 0, 0);
        pnlDetails.add(lblEmployeeInSearch, gridBagConstraints);
        
        chkEmployeeSearch.setHorizontalAlignment(SwingConstants.LEFT);
        chkEmployeeSearch.setHorizontalTextPosition(SwingConstants.LEFT);
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.insets = new Insets(6, 4, 0, 6);
        pnlDetails.add(chkEmployeeSearch, gridBagConstraints);
        
        lblTermStart.setFont(CoeusFontFactory.getLabelFont());
        lblTermStart.setHorizontalAlignment(SwingConstants.RIGHT);
        lblTermStart.setText("Term Start:");
        lblTermStart.setHorizontalTextPosition(SwingConstants.RIGHT);
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = GridBagConstraints.EAST;
        gridBagConstraints.insets = new Insets(4, 6, 0, 0);
        pnlDetails.add(lblTermStart, gridBagConstraints);
        
        txtTermStart.setFont(CoeusFontFactory.getNormalFont());
        txtTermStart.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        txtTermStart.setMinimumSize(new Dimension(124, 20));
        txtTermStart.setPreferredSize(new Dimension(124, 20));
        if(functionType != 'D'){
            txtTermStart.addFocusListener(new MemberFocusAdapter());
            txtTermStart.setInputVerifier(new DateVerifier());
        }
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.insets = new Insets(4, 4, 0, 6);
        pnlDetails.add(txtTermStart, gridBagConstraints);
        
        lblTermEnd.setFont(CoeusFontFactory.getLabelFont());
        lblTermEnd.setHorizontalAlignment(SwingConstants.RIGHT);
        lblTermEnd.setText("Term End:");
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = GridBagConstraints.EAST;
        gridBagConstraints.insets = new Insets(4, 10, 6, 0);
        pnlDetails.add(lblTermEnd, gridBagConstraints);
        
        lblPaidMember.setFont(CoeusFontFactory.getLabelFont());
        lblPaidMember.setHorizontalAlignment(SwingConstants.RIGHT);
        lblPaidMember.setText("Paid Member:");
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = GridBagConstraints.EAST;
        gridBagConstraints.insets = new Insets(4, 10, 6, 0);
        pnlDetails.add(lblPaidMember, gridBagConstraints);
        
        txtTermEnd.setFont(CoeusFontFactory.getNormalFont());
        txtTermEnd.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        txtTermEnd.setMinimumSize(new Dimension(124, 20));
        txtTermEnd.setPreferredSize(new Dimension(124, 20));
        if(functionType != 'D'){
            txtTermEnd.addFocusListener(new MemberFocusAdapter());
            txtTermEnd.setInputVerifier(new DateVerifier());
        }
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.insets = new Insets(4, 4, 6, 0);
        pnlDetails.add(txtTermEnd, gridBagConstraints);
        
        lblMemberType.setFont(CoeusFontFactory.getLabelFont());
        lblMemberType.setText("Membership Type:");
        lblMemberType.setHorizontalTextPosition(SwingConstants.RIGHT);
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = GridBagConstraints.EAST;
        gridBagConstraints.insets = new Insets(4, 10, 6, 0);
        pnlDetails.add(lblMemberType, gridBagConstraints);
        
        cmbMemberType.setMinimumSize(new Dimension(124, 25));
        cmbMemberType.setPreferredSize(new Dimension(124, 25));
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.insets = new Insets(4, 4, 6, 6);
        pnlDetails.add(cmbMemberType, gridBagConstraints);
        
        //chkPaidMember.setHorizontalAlignment(SwingConstants.LEFT);
        //chkPaidMember.setHorizontalTextPosition(SwingConstants.LEFT);
        //chkPaidMember.setFont(CoeusFontFactory.getLabelFont());
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        //gridBagConstraints.insets = new Insets(4, 2, 6, 0);
        gridBagConstraints.insets = new Insets(4, 1, 6, 0);
        pnlDetails.add(chkPaidMember, gridBagConstraints);
        
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.insets = new Insets(6, 5, 6, 6);
        pnlMain.add(pnlDetails, gridBagConstraints);
        
        
        lblStatus.setText("Status :");
        lblStatus.setHorizontalAlignment(SwingConstants.RIGHT);
        lblStatus.setFont(CoeusFontFactory.getLabelFont());
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new Insets(6, 5, 0, 0);
        pnlStatus.add(lblStatus, gridBagConstraints);
        
        cmbStatus.addItemListener(new ItemListener(){
            public void itemStateChanged(ItemEvent ie){
                saveRequired = true;
            }
        });
        
        
        cmbStatus.setFont(CoeusFontFactory.getNormalFont());
        cmbStatus.setPreferredSize(new Dimension(100,20));
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new Insets(0, 10, 0, 6);
        pnlStatus.add(cmbStatus, gridBagConstraints);
        
        lblFromDate.setText("From :");
        lblFromDate.setHorizontalAlignment(SwingConstants.RIGHT);
        lblFromDate.setFont(CoeusFontFactory.getLabelFont());
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new Insets(10, 0, 0, 0);
        pnlStatus.add(lblFromDate, gridBagConstraints);
        
        lblToDate.setText("To :");
        lblToDate.setHorizontalAlignment(SwingConstants.RIGHT);
        lblToDate.setFont(CoeusFontFactory.getLabelFont());
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new Insets(10, 0, 0, 0);
        pnlStatus.add(lblToDate, gridBagConstraints);
        
        txtFromDate.setFont(CoeusFontFactory.getNormalFont());
        txtFromDate.setText("");
        txtFromDate.setPreferredSize(new Dimension(100,20));
        if(functionType != 'D'){
            txtFromDate.addFocusListener(new MemberFocusAdapter());
            txtFromDate.setInputVerifier(new DateVerifier());
        }
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new Insets(10, 10, 0, 6);
        pnlStatus.add(txtFromDate, gridBagConstraints);
        
        txtToDate.setFont(CoeusFontFactory.getNormalFont());
        txtToDate.setText("");
        txtToDate.setPreferredSize(new Dimension(100,20));
        if(functionType != 'D'){
            txtToDate.addFocusListener(new MemberFocusAdapter());
            txtToDate.setInputVerifier(new DateVerifier());
        }
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new Insets(10, 10, 6, 6);
        pnlStatus.add(txtToDate, gridBagConstraints);
        
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new Insets(6, 5, 6, 6);
        //Added for Case# 3229 - Inability to modify terms for Members with lapsed term dates - Start
        gridBagConstraints.fill = GridBagConstraints.VERTICAL;
        //Added for Case# 3229 - Inability to modify terms for Members with lapsed term dates - End
        pnlMain.add(pnlStatus, gridBagConstraints);
        
        
        pnlRoles.setLayout(new GridBagLayout());
        
        pnlRoles.setBorder(new TitledBorder(new EtchedBorder(), "Roles",
        TitledBorder.LEFT, TitledBorder.TOP, CoeusFontFactory.getLabelFont()));
        scrPnAvailableRoles.setBorder(new TitledBorder(new EtchedBorder(),
        "Available Roles", TitledBorder.LEFT, TitledBorder.TOP,
        CoeusFontFactory.getLabelFont()));
        //Commented and added for Case# 3229 - Inability to modify terms for Members with lapsed term dates - Start
//        scrPnAvailableRoles.setPreferredSize(new Dimension(200, 155)); 
        scrPnAvailableRoles.setPreferredSize(new Dimension(200, 250));    
        //Commented and added for Case# 3229 - Inability to modify terms for Members with lapsed term dates - End
        
        tblAvailableRoles.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
//        tblAvailableRoles.setFocusTraversalKeysEnabled(true);
//        tblAvailableRoles.setSurrendersFocusOnKeystroke(true);
        tblAvailableRoles.setModel(new DefaultTableModel(
        getAvailableRoles(), getAvailableColumnNames()){
            public boolean isCellEditable(int row, int col){
                return false;
            }
        }
        );
        setTableModelAndEditors(tblAvailableRoles);
        
        scrPnAvailableRoles.setViewportView(tblAvailableRoles);
        
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.insets = new Insets(6, 6, 6, 0);
        pnlRoles.add(scrPnAvailableRoles, gridBagConstraints);
        
        pnlRoleButtons.setLayout(new GridBagLayout());
        
        btnRemoveRole.setFont(CoeusFontFactory.getLabelFont());
        btnRemoveRole.setText(">>");
        btnRemoveRole.setName("Remove");
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        //Added for Case# 3229 - Inability to modify terms for Members with lapsed term dates - Start
        gridBagConstraints.anchor = GridBagConstraints.NORTH;
        //Added for Case# 3229 - Inability to modify terms for Members with lapsed term dates - Start
        gridBagConstraints.insets = new Insets(8, 6, 0, 6);
        pnlRoleButtons.add(btnRemoveRole, gridBagConstraints);
        
        btnAddRole.setFont(CoeusFontFactory.getLabelFont());
        btnAddRole.setText("<<");
        btnAddRole.setName("Add");
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        //Added for Case# 3229 - Inability to modify terms for Members with lapsed term dates - Start
        gridBagConstraints.anchor = GridBagConstraints.SOUTH;
        //Added for Case# 3229 - Inability to modify terms for Members with lapsed term dates - End
        gridBagConstraints.insets = new Insets(8, 6, 6, 6);
        pnlRoleButtons.add(btnAddRole, gridBagConstraints);
        
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        //Commented and added for Case# 3229 - Inability to modify terms for Members with lapsed term dates - Start
        //gridBagConstraints.insets = new Insets(20, 6, 6, 0);
        gridBagConstraints.insets = new Insets(6, 6, 6, 6);
        //Commented and added for Case# 3229 - Inability to modify terms for Members with lapsed term dates - End
        gridBagConstraints.anchor = GridBagConstraints.NORTH;
        //Added for Case# 3229 - Inability to modify terms for Members with lapsed term dates - Start
        gridBagConstraints.fill = GridBagConstraints.VERTICAL;
        //Added for Case# 3229 - Inability to modify terms for Members with lapsed term dates - End
        pnlRoles.add(pnlRoleButtons, gridBagConstraints);
        
        scrPnSelectedRoles.setBorder(new TitledBorder(new EtchedBorder(),
        "Selected Roles", TitledBorder.LEFT, TitledBorder.TOP,
        CoeusFontFactory.getLabelFont()));
        scrPnSelectedRoles.setMinimumSize(new Dimension(400, 200));
        scrPnSelectedRoles.setPreferredSize(new Dimension(400, 200));
        tblSelectedRoles.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
//        tblSelectedRoles.setFocusTraversalKeysEnabled(true);
//        tblSelectedRoles.setSurrendersFocusOnKeystroke(true);
        
        tblSelectedRoles.setModel(new DefaultTableModel(
        getMembersRoles(), getMemberColumnNames()){
            public boolean isCellEditable(int row, int col){
                if(functionType == 'D'){
                    return false;
                }else if(col <= 2){
                    return false;
                }
                return true;
            }
        }
        );
        setTableModelAndEditors(tblSelectedRoles);
        scrPnSelectedRoles.setViewportView(tblSelectedRoles);
        
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.insets = new Insets(6, 0, 6, 6);
        pnlRoles.add(scrPnSelectedRoles, gridBagConstraints);
        
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.insets = new Insets(6, 6, 6, 6);
        pnlMain.add(pnlRoles, gridBagConstraints);
        this.setLayout(new java.awt.BorderLayout());
        this.add(pnlMain);
        
        
    }
    /**
     * This method is used to set the model to the table and set the editors
     * for columns in the table
     *
     * @param JTable to which the editors has to be set
     */
    private void setTableModelAndEditors(JTable table){
        TableColumn  column;
        table.setRowHeight(22);
        JTableHeader header = table.getTableHeader();
        header.setReorderingAllowed(false);
        if(table == tblSelectedRoles){
            table.setShowHorizontalLines(false);
            table.setShowVerticalLines(false);
             table.setOpaque(false);
        
       
            column = table.getColumnModel().getColumn(0);
            column.setMinWidth(30);
            column.setMaxWidth(30);
            column.setPreferredWidth(30);
            column.setHeaderRenderer(new EmptyHeaderRenderer());
            column.setCellRenderer(new IconRenderer());
            
            column = table.getColumnModel().getColumn(1);
            column.setMinWidth(0);
            column.setMaxWidth(0);
            column.setPreferredWidth(0);
// Added by Chandra. Bug File IRB - SystemTestingDL-01. Bug No 11  
            column = table.getColumnModel().getColumn(2);
            column.setMinWidth(150);
//            column.setMaxWidth(150);
            column.setPreferredWidth(150);
// End Bug #11
            String columnName =
            ((DefaultTableModel)table.getModel()).getColumnName(3);
            DateEditor dateCellEditor = new DateEditor(columnName);
            column = table.getColumnModel().getColumn(3);
            column.setCellEditor(dateCellEditor);
            // Added by Chandra. Bug File IRB - SystemTestingDL-01. Bug No 11  
            column.setMinWidth(100);
//            column.setMaxWidth(100);
            column.setPreferredWidth(100);
            // End Bug #11
            columnName = ((DefaultTableModel)table.getModel()).getColumnName(4);
            dateCellEditor = new DateEditor(columnName);
            column = table.getColumnModel().getColumn(4);
            column.setCellEditor(dateCellEditor);
            // Added by Chandra. Bug File IRB - SystemTestingDL-01. Bug No 11  
            column.setMinWidth(100);
//            column.setMaxWidth(100);
            column.setPreferredWidth(100);
            // End Bug #11.
            
        }else if(table == tblAvailableRoles){
            column = table.getColumnModel().getColumn(0);
            column.setMinWidth(0);
            column.setMaxWidth(0);
            column.setPreferredWidth(0);
        }
    }
    /** This method checks for valid data in mandatory fields.
     * @return true if all mandatory fields contains valid data,
     * else false.
     * @throws Exception with custom message if any mandatory fields doesn't have
     * valid data.
     */
    public boolean validateFormData() throws Exception{
        if((personId==null || personId.trim().length()==0) && chkEmployeeSearch.isSelected()){
            //instead of getting only person id we will get all the person details so that
            // we can take the initCaps Full Name of the person.
            //personId = getPersonID(txtPerson.getText());
            PersonInfoFormBean personBean = getPersonInfo(txtPerson.getText());
            if( personBean != null ) {
                personId = personBean.getPersonID();
                txtPerson.setText(personBean.getFullName());
            }
        }
        if((txtPerson.getText()== null)
        || (txtPerson.getText().trim().length() == 0)){
            /* Term Start Date doesn't have any value */
            log(coeusMessageResources.parseMessageKey(
            "memMntFrm_exceptionCode.1033"));
            txtPerson.requestFocus();
            return false;
        }else if((personId== null) ||(personId.trim().length()==0)){
            log(coeusMessageResources.parseMessageKey(
            "memMntFrm_exceptionCode.1034"));
            txtPerson.requestFocus();
            return false;
        }else if(personId.equalsIgnoreCase(TOO_MANY)){
            log(coeusMessageResources.parseMessageKey(
            "memMntFrm_exceptionCode.1034"));
            txtPerson.requestFocus();
            return false;
            
        }else if((txtTermStart.getText()== null)
        || (txtTermStart.getText().trim().length() == 0)){
            /* Term Start Date doesn't have any value */
            log(coeusMessageResources.parseMessageKey(
            "memMntFrm_exceptionCode.1035"));
            txtTermStart.requestFocus();
            return false;
        }else if((txtTermEnd.getText()== null)
        || (txtTermEnd.getText().trim().length() == 0)){
            /* Term End Date doesn't have any value */
            log(coeusMessageResources.parseMessageKey(
            "memMntFrm_exceptionCode.1036"));
            txtTermEnd.requestFocus();
            return false;
        }else{
            java.util.Date termStartDate = dtFormat.parse(
            dtUtils.restoreDate(txtTermStart.getText(),"/:-,"));
            java.util.Date termEndDate = dtFormat.parse(
            dtUtils.restoreDate(txtTermEnd.getText(),"/:-,"));
            if(termEndDate.compareTo(termStartDate)<0){
                /* Term End Date is earlier than Term Start Date */
                log(coeusMessageResources.parseMessageKey(
                "memMntFrm_exceptionCode.1037"));
                txtTermEnd.requestFocus();
                return false;
            }
            
            if((txtFromDate.getText()== null)
            || (txtFromDate.getText().trim().length() == 0)){
                /* Term Start Date doesn't have any value */
                log(coeusMessageResources.parseMessageKey(
                "memMntFrm_exceptionCode.1038"));
                txtFromDate.requestFocus();
                return false;
            }else if((txtToDate.getText()== null)
            || (txtToDate.getText().trim().length() == 0)){
                /* Term End Date doesn't have any value */
                log(coeusMessageResources.parseMessageKey(
                "memMntFrm_exceptionCode.1039"));
                txtToDate.requestFocus();
                return false;
            }else{
                java.util.Date sDate = dtFormat.parse(
                dtUtils.restoreDate(txtFromDate.getText(),"/:-,"));
                java.util.Date eDate = dtFormat.parse(
                dtUtils.restoreDate(txtToDate.getText(),"/:-,"));
                if(eDate.compareTo(sDate)<0){
                    /* Term End Date is earlier than Term Start Date */
                    log(coeusMessageResources.parseMessageKey(
                    "memMntFrm_exceptionCode.1040"));
                    txtToDate.requestFocus();
                    return false;
                }
                if(sDate.compareTo(termStartDate)<0){
                    log(coeusMessageResources.parseMessageKey(
                    "memMntFrm_exceptionCode.1041"));
                    txtFromDate.requestFocus();
                    return false;
                }else if(termEndDate.compareTo(eDate)<0){
                    log(coeusMessageResources.parseMessageKey(
                    "memMntFrm_exceptionCode.1042"));
                    txtToDate.requestFocus();
                    return false;
                }
            }
            int tableRows = tblSelectedRoles.getRowCount();
            if(tableRows <= 0){
                /* No roles selected to member */
                log(coeusMessageResources.parseMessageKey(
                "memMntFrm_exceptionCode.1043"));
                return false;
            }
            java.util.Date roleStartDate=null;
            java.util.Date roleEndDate = null;
            for(int row=0;row<tableRows;row++){
                /* Check for role start date and role end date for all
                   selected member roles */
                if((tblSelectedRoles.getValueAt(row,3) == null)
                ||(tblSelectedRoles.getValueAt(row,
                3).toString().trim().length() ==0 ) ){
                    log("Please enter Term Start Date for role \""
                    +tblSelectedRoles.getValueAt(row,2)+"\"");
                    tblSelectedRoles.setRowSelectionInterval(row,row);
                    return false;
                }else if((tblSelectedRoles.getValueAt(row,4) == null)
                ||(tblSelectedRoles.getValueAt(row,
                4).toString().trim().length() ==0 ) ){
                    log("Please enter Term End Date for role \""
                    +tblSelectedRoles.getValueAt(row,2)+"\"");
                    tblSelectedRoles.setRowSelectionInterval(row,row);
                    return false;
                }else{
                    roleStartDate = dtFormat.parse(dtUtils.restoreDate(
                    tblSelectedRoles.getValueAt(row,3).toString()
                    ,"/:-,"));
                    roleEndDate = dtFormat.parse(dtUtils.restoreDate(
                    tblSelectedRoles.getValueAt(row,4).toString()
                    ,"/:-,"));
                    if(roleStartDate.compareTo(termStartDate)<0){
                        log(coeusMessageResources.parseMessageKey(
                        "memMntFrm_exceptionCode.1045"));
                        tblSelectedRoles.setRowSelectionInterval(row,row);
                        return false;
                    }
                    if(termEndDate.compareTo(roleEndDate)<0){
                        log(coeusMessageResources.parseMessageKey(
                        "memMntFrm_exceptionCode.1046"));
                        tblSelectedRoles.setRowSelectionInterval(row,row);
                        return false;
                    }
                    if(roleEndDate.compareTo(roleStartDate)<0){
                        /* Role end date is earlier than role start date */
                        log(coeusMessageResources.parseMessageKey(
                        "memMntFrm_exceptionCode.1044"));
                        tblSelectedRoles.setRowSelectionInterval(row,row);
                        return false;
                    }
                }
            }
            /* for each role check whether any role already exists with
               same role name and start dates and end dates falling between
               the start and end dates of this role */
            String roleName=null;
            String roleStDate =null;
            String rEndDate = null;
            java.util.Date rowStDate  = null;
            java.util.Date rowEndDate = null;
            java.util.Date nextRowStDate = null;
            java.util.Date nextRowEndDate = null;
            for(int row=0;row<tableRows;row++){
                for(int nextRow=row+1;nextRow<tableRows;nextRow++){
                    roleName = tblSelectedRoles.getValueAt(nextRow,
                    2).toString();
                    if(roleName.equals(tblSelectedRoles.getValueAt(row,
                    2).toString())){
                        roleStDate = tblSelectedRoles.getValueAt(nextRow,
                        3).toString();
                        rEndDate = tblSelectedRoles.getValueAt(nextRow,
                        4).toString();
                        rowStDate = dtFormat.parse(dtUtils.restoreDate(
                        tblSelectedRoles.getValueAt(row,
                        3).toString(),"/:-,"));
                        rowEndDate = dtFormat.parse(dtUtils.restoreDate(
                        tblSelectedRoles.getValueAt(row,
                        4).toString(),"/:-,"));
                        nextRowStDate = dtFormat.parse(dtUtils.restoreDate(
                        roleStDate,"/:-,"));
                        nextRowEndDate = dtFormat.parse(dtUtils.restoreDate(
                        rEndDate,"/:-,"));
                        /* role start date  should not fall between
                           start and end dates of any other role with same
                           name. And also check for vice versa */
                        if((rowStDate.compareTo(nextRowStDate)==0)
                        || (rowEndDate.compareTo(nextRowEndDate)==0)
                        || ((rowStDate.after(nextRowStDate))
                        && (rowStDate.before(
                        nextRowEndDate)))
                        || ((rowEndDate.after(nextRowStDate))
                        && (rowEndDate.before(
                        nextRowEndDate)))
                        || ((nextRowStDate.before(rowStDate))
                        && (nextRowEndDate.after(
                        rowEndDate)))
                        || ((nextRowStDate.after(rowStDate))
                        && (nextRowStDate.before(
                        rowEndDate)))){
                            log(coeusMessageResources.parseMessageKey(
                            "memMntFrm_exceptionCode.1047"));
                            tblSelectedRoles.setRowSelectionInterval(row,row);
                            return false;
                        }
                    }
                }
            }
            
        }
        return true;
    }
    
    /** This method is used to fetch the form details into
     * <CODE>CommiteeMembershipDetailsBean</CODE>. If the form is opened in <CODE>Add Mode</CODE> then
     * it will set the <CODE>AcType</CODE> to 'I' so that the data will be inserted into the
     * database. If the form is opened in <CODE>Modify Mode</CODE>, if user had changed the
     * contents or added any new roles or changed the member status then it will
     * set the <CODE>AcType</CODE> to 'U'
     *
     * @return <CODE>CommitteeMembershipDetailsBean</CODE>  which consists of all the member
     * details including all member roles and member status information.
     */
    public CommitteeMembershipDetailsBean getMemberDetails(){
        try{
            boolean found = false;
            boolean modified = false;
            int rowNum = tblSelectedRoles.getRowCount();
            int colCount = tblSelectedRoles.getColumnCount();
            Vector roles = new Vector();
            DefaultTableModel model =
            (DefaultTableModel)tblSelectedRoles.getModel();
            char paidMemberValue = chkPaidMember.isSelected() ? 'Y' : 'N' ;
            if(functionType == 'A'){
                memberDetails = new CommitteeMembershipDetailsBean();
                memberDetails.setAcType("I");
            }else if(functionType == 'M'){
                if(!dtUtils.formatDate(
                memberDetails.getTermStartDate().toString(),
                "dd-MMM-yyyy").equals(txtTermStart.getText())){
                    modified = true;
                }
                if(!dtUtils.formatDate(
                memberDetails.getTermEndDate().toString(),
                "dd-MMM-yyyy").equals(txtTermEnd.getText())){
                    modified = true;
                }
                if(!memberDetails.getMembershipTypeDesc().equals(
                ((ComboBoxBean)cmbMemberType.getSelectedItem()).toString())){
                    modified = true;
                }
                if( memberDetails.getPaidMemberFlag() != paidMemberValue ){
                    modified = true;
                }
                if(modified){
                    memberDetails.setAcType("U");
                }
            }
            memberDetails.setPersonId(personId);
            memberDetails.setPersonName(txtPerson.getText());
            memberDetails.setNonEmployeeFlag(chkEmployeeSearch.isSelected()
            ? 'N':'Y');
            memberDetails.setPaidMemberFlag( paidMemberValue );
            
            if((txtTermStart.getText()!= null)
            && (txtTermStart.getText().length()>0)){
                memberDetails.setTermStartDate(
                new Date(dtFormat.parse(
                dtUtils.restoreDate(txtTermStart.getText(),
                "/-:,")).getTime()));
            }
            if((txtTermEnd.getText()!= null)
            && (txtTermEnd.getText().length()>0)){
                memberDetails.setTermEndDate(
                new Date(dtFormat.parse(
                dtUtils.restoreDate(txtTermEnd.getText(),
                "/-:,")).getTime()));
            }
            ComboBoxBean membTypeBean =
            (ComboBoxBean)cmbMemberType.getSelectedItem();
            memberDetails.setMembershipTypeCode(Integer.parseInt(
            membTypeBean.getCode()));
            memberDetails.setMembershipTypeDesc(membTypeBean.toString());
            Vector availableMemberRoles=new Vector();
            if(functionType!= 'D'){
                /* loop through all the table rows and check whether they are
                   present in member roles. if not present set the AcType to 'I'
                 */
                CommitteeMemberRolesBean roleBean=null;
                String date = null;
                
                CommitteeMemberRolesBean membRoleBean = null;
                for(int row = 0;row < rowNum; row++){
                    found = false;
                    modified = false;
                    roleBean = new CommitteeMemberRolesBean();
                    roleBean.setMembershipRoleCode(Integer.parseInt(
                    (String)model.getValueAt(row,1)));
                    roleBean.setMembershipRoleDesc((
                    String)model.getValueAt(row,2));
                    date = dtUtils.restoreDate(model.getValueAt(row,
                    3).toString(),"/-:,");
                    roleBean.setStartDate(
                    new Date(dtFormat.parse(date).getTime()));
                    date = dtUtils.restoreDate(
                    model.getValueAt(row,4).toString(),"/-:,");
                    roleBean.setEndDate(
                    new Date(dtFormat.parse(date).getTime()));
                    if(functionType == 'A'){
                        rolesModified = true;
                        /* if there are no member roles available then all
                           selected roles are new roles so send them for
                           insertion by setting AcType to I */
                        roleBean.setAcType("I");
                    }else if(functionType == 'M' ){
                        /* if member roles already exists then check whether
                           they have been modified/deleted or new roles have
                           been added and set AcType accordingly. */
                        
                        availableMemberRoles = memberDetails.getMemberRoles();
                        for(int roleIndex = 0;
                        roleIndex<availableMemberRoles.size();
                        roleIndex++){
                            membRoleBean = (CommitteeMemberRolesBean)
                            availableMemberRoles.elementAt(roleIndex);
                            if((membRoleBean.getMembershipRoleCode()
                            == roleBean.getMembershipRoleCode())
                            && (membRoleBean.getStartDate().compareTo(
                            roleBean.getStartDate())==0)
                            && (membRoleBean.getEndDate().compareTo(
                            roleBean.getEndDate())==0)){
                                found = true;
                                break;
                            }
                        }
                    }
                    if(availableMemberRoles == null){
                        availableMemberRoles = new Vector();
                    }
                    if(!found){
                        /* if member roles are present and this particular
                           selected role is not present then it may be new
                           role so send it for insertion by setting
                           AcType to I */
                        roleBean.setAcType("I");
                        if(functionType == 'M'){
                            memberDetails.setAcType("U");
                        }
                        rolesModified = true;
                        availableMemberRoles.add(roleBean);
                        
                    }
                    
                }
            }
            
            if(functionType == 'M' ){
                /* in modify mode member roles will be present, loop through
                  all member roles and check whether they have been deleted. If
                  so set AcType for that role to 'D' and send to server for
                  deletion */
                Vector membRoles = memberDetails.getMemberRoles();
                CommitteeMemberRolesBean commMembRoleBean = null;
                for(int membRoleIndex=0;membRoleIndex<membRoles.size();
                membRoleIndex++){
                    commMembRoleBean = (CommitteeMemberRolesBean)
                    membRoles.elementAt(membRoleIndex);
                    if(rowNum>0){
                        found =false;
                        /* if there are any selected roles then check whether
                           member role has been deleted */
                        int roleCode=0;
                        String roleStDate = null;
                        String roleEndDate = null;
                        for(int tableRowIndex=0;tableRowIndex<rowNum;
                        tableRowIndex++){
                            roleCode = Integer.parseInt(
                            tblSelectedRoles.getValueAt(tableRowIndex,
                            1).toString());
                            roleStDate =
                            tblSelectedRoles.getValueAt(tableRowIndex,
                            3).toString();
                            roleEndDate = tblSelectedRoles.getValueAt(
                            tableRowIndex,4).toString();
                            if((commMembRoleBean.getMembershipRoleCode()
                            == roleCode)
                            && (roleStDate.equals(dtUtils.formatDate(
                            commMembRoleBean.getStartDate().toString(),
                            "dd-MMM-yyyy")))
                            && (roleEndDate.equals(dtUtils.formatDate(
                            commMembRoleBean.getEndDate().toString(),
                            "dd-MMM-yyyy")))){
                                found = true;
                                break;
                            }
                        }
                        if(!found){
                            /* if member role is not found in table then it has
                               been deleted. so send for deletion by setting
                               AcType to 'D' */
                            commMembRoleBean.setAcType("D");
                            if(functionType == 'M'){
                                memberDetails.setAcType("U");
                            }
                            rolesModified = true;
                            availableMemberRoles.setElementAt(commMembRoleBean,
                            membRoleIndex);
                        }
                        
                        
                    }else{
                        /* if member roles available and there are no selected
                           roles in table then send all member roles for
                           deletion by setting AcType to 'D' */
                        commMembRoleBean.setAcType("D");
                        if(functionType == 'M'){
                            memberDetails.setAcType("U");
                        }
                        rolesModified = true;
                        availableMemberRoles.setElementAt(commMembRoleBean,
                        membRoleIndex);
                    }
                }
            }
            memberDetails.setMemberRoles(availableMemberRoles);
            if(rolesModified && !memberDetails.getMemberRolesModified()){
                memberDetails.setMemberRolesModified(rolesModified);
            }
            if((functionType == 'A') || (memberStatus == null)){
                memberStatus = new CommitteeMemberStatusChangeBean();
                statusModified = true;
                memberStatus.setAcType("I");
                if((txtFromDate.getText()!=null)
                && (txtFromDate.getText().trim().length()>0)){
                    saveRequired = true;
                }
                if((txtToDate.getText()!=null)
                && (txtToDate.getText().trim().length()>0)){
                    saveRequired = true;
                }
            }else if(functionType == 'M'){
                modified = false;
                if(memberStatus!=null){
                    if(memberStatus.getStatusDescription()!=null){
                        if(!(((ComboBoxBean)
                        cmbStatus.getSelectedItem()).toString().equals(
                        memberStatus.getStatusDescription()))){
                            modified = true;
                        }
                    }
                    if(memberStatus.getStartDate()!=null){
                        if(!dtUtils.formatDate(
                        memberStatus.getStartDate().toString(),
                        "dd-MMM-yyyy").equals(txtFromDate.getText())){
                            modified = true;
                        }
                    }
                    if(memberStatus.getEndDate()!=null){
                        if(!dtUtils.formatDate(
                        memberStatus.getEndDate().toString(),
                        "dd-MMM-yyyy").equals(txtToDate.getText())){
                            modified = true;
                        }
                    }
                    if(modified){
                        memberStatus.setAcType("U");
                        saveRequired = true;
                        memberDetails.setAcType("U");
                        // added by ravi for new seq no. logic
                        statusModified = true;
                    }
                }
            }
            memberStatus.setMembershipStatusCode(Integer.parseInt(
            ((ComboBoxBean)cmbStatus.getSelectedItem()).getCode()));
            memberStatus.setStatusDescription(
            ((ComboBoxBean)cmbStatus.getSelectedItem()).toString());
            memberStatus.setStartDate(new Date(dtFormat.parse(
            dtUtils.restoreDate(txtFromDate.getText(),
            "/-:,")).getTime()));
            memberStatus.setEndDate(new Date(dtFormat.parse(
            dtUtils.restoreDate(txtToDate.getText(),"/-:,")).getTime()));
            memberDetails.setStatusDescription(
            ((ComboBoxBean)cmbStatus.getSelectedItem()).toString());
            // modified by ravi for new seq no. logic
            memberDetails.setStatusInfo(memberStatus);
            if(statusModified && !memberDetails.getMemberStatusModified()){
                memberDetails.setMemberStatusModified(statusModified);
            }
            
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return memberDetails;
    }
    
    /** This method is used to set the available membership types.
     *
     * @param membershipTypes Vector which consists of available membership types.
     */
    public void setMembershipTypes(Vector membershipTypes){
        this.membershipTypes = membershipTypes;
    }
    
    /** This method is used to fetch the available membership types.
     *
     * @return Vector which consists of available membership types.
     */
    public Vector getMembershipTypes(){
        return  membershipTypes;
    }
    
    /** This method is used to find out whether modifications done to the data
     * have been saved or not.
     *
     * @return true if data is not saved after modifications, else false.
     */
    public boolean isSaveRequired(){
        return saveRequired;
    }

    public void setDefaultFocusToComponent(){
        if( functionType != CoeusGuiConstants.DISPLAY_MODE ) {
            txtTermStart.requestFocus();
        }
    }    
    
    
    /** This method is used to set whether modifications are to be saved or not.
     *
     * @param save boolean true if data is to be saved after modifications,
     * else false.
     */
    public void setSaveRequired(boolean save){
        this.saveRequired = save;
    }
    
    
    
    /**
     * This method is used to fetch the selected member roles.
     *
     * @return Vector which consists of all selected member roles.
     */
    public  Vector getMembersRoles(){
        return this.memberRoles;
    }
    
    /**
     * This method is used to set the selected member roles.
     *
     * @param membRoles Vector which consists of all selected member roles.
     */
    public  void setMembersRoles(Vector membRoles){
        this.memberRoles = membRoles;
    }
    
    /** This method is used to fetch the available member roles.
     *
     * @return Vector which consists of all available member roles.
     */
    
    public  Vector getAvailableRoles(){
        return this.availableRoles;
    }
    
    /** This method is used to set the available member roles.
     *
     * @param availRoles Vector which consists of all available member roles.
     */
    public  void setAvailableRoles(Vector availRoles){
        this.availableRoles = availRoles;
    }
    
    /** This method is used to get the column names used to show the
     * available member roles.
     *
     * @return Vector which consists of column names used to show available
     * member roles.
     */
    public Vector getAvailableColumnNames(){
        return this.availcolNames;
    }
    
    /** This method is used to set the column names used to show the
     * available member roles.
     *
     * @param colNames Vector which consists of column names used to show available
     * member roles.
     */
    public void setAvailableColumnNames(Vector colNames){
        this.availcolNames = colNames;
    }
    
    /** This method is used to get the column names used to show the
     * selected member roles.
     *
     * @return Vector which consists of column names used to show selected
     * member roles.
     */
    public Vector getMemberColumnNames(){
        return this.membcolNames;
    }
    
    /** This method is used to set the column names used to show the
     * selected member roles.
     *
     * @param colNames Vector which consists of column names used to show selected
     * member roles.
     */
    public void setMemberColumnNames(Vector colNames){
        this.membcolNames = colNames;
    }
    
    /**
     * This method is used to throw exception with given message.
     *
     * @param mesg String representing the description of the exception.
     *
     * @throws Exception with the given message.
     */
    public void log(String mesg) throws Exception{
        throw new Exception(mesg);
    }
    
    /**
     * This method is used to add the selected available role to  member roles
     */
    private void addSelection(){
        Vector vecRow = new Vector();
        int rowNum = tblAvailableRoles.getSelectedRow();
        int colCount = tblAvailableRoles.getColumnCount();
        DefaultTableModel model
        = (DefaultTableModel)tblAvailableRoles.getModel();
        if ( (rowNum != -1) && (rowNum < model.getRowCount()) ){
            vecRow.add("");
            vecRow.add(model.getValueAt(rowNum,0));
            vecRow.add(model.getValueAt(rowNum,1));
            
            insertRowInMemberRoles(tblSelectedRoles,vecRow);
            tblSelectedRoles.requestFocusInWindow();
            int selRow = tblSelectedRoles.getSelectedRow();
            tblSelectedRoles.editCellAt(selRow,3);
            tblSelectedRoles.getEditorComponent().requestFocusInWindow();
        }
    }
    
    /**
     * This method is used to remove the selected member role
     */
    private void removeSelection() {
        int rowNum = tblSelectedRoles.getSelectedRow();
        int colCount = tblSelectedRoles.getColumnCount();
        DefaultTableModel model
        = (DefaultTableModel)tblSelectedRoles.getModel();
        if ( (rowNum != -1) && (rowNum <model.getRowCount()) ){
            saveRequired = true;
            model.removeRow(rowNum);
            model.fireTableDataChanged();
            
            int newRowCount = model.getRowCount();
            // select the next row if exists
            if (newRowCount > rowNum) {
                (tblSelectedRoles.getSelectionModel())
                .setSelectionInterval(rowNum,
                rowNum);
            } else {
                (tblSelectedRoles.getSelectionModel())
                .setSelectionInterval(newRowCount - 1,
                newRowCount - 1);
            }
        }
        
    }
    
    /**
     * This method is used in addSelection to add a specified row in the
     * specified table
     *
     * @param JTable to which the specified row to be added
     * @param Vector which consists of data that has to be added as a row in
     * a table
     */
    private void insertRowInMemberRoles(JTable table, Vector vtr){
        DefaultTableModel tableModel = (DefaultTableModel)table.getModel();
        Vector dVector = tableModel.getDataVector();
        int rowCount = dVector.size();
        int rowIndex=0;
        Vector vecElement=null;
        for(rowIndex =0;rowIndex<rowCount;rowIndex++){
            vecElement = (Vector)dVector.elementAt(rowIndex);
            if( ( ((String)vecElement.elementAt(2)).compareTo(
            (String)vtr.elementAt(2))) < 0){
                continue;
            }
            else {
                break;
            }
            
        }
        saveRequired=true;
        
        /*
         * Changes done as per the Change Request id : 170, Feb' 14 2003
         *
         * Copying start date and end date from term start date and end date
         * to the selected roles table
         * Modified by : Raghunath P.V.  Feb' 17 2003
         */
        if((txtTermStart.getText() != null) ||
        (!txtTermStart.getText().trim().equals(""))
        && txtTermStart.equals(REQUIRED_DATE_FORMAT)){
            vtr.addElement(txtTermStart.getText());
        }else{
            vtr.addElement("");
        }
        if((txtTermEnd.getText() != null) ||
        (!txtTermEnd.getText().trim().equals("")) && txtTermStart.equals(REQUIRED_DATE_FORMAT)){
            vtr.addElement(txtTermEnd.getText());
        }else{
            vtr.addElement("");
        }
        
        tableModel.insertRow(rowIndex,vtr);
        tableModel.fireTableDataChanged();
        table.setRowSelectionInterval(rowIndex,rowIndex);
        table.scrollRectToVisible(table.getCellRect(rowIndex,
        0, true));
        
    }
    
    /**
     * This method is used to stop the table editing and take the latest value
     * in the table cells
     *
     * @throws Exception with custom message if the data in the editing cell is
     * not valid.
     */
    public void stopTableEditing() throws Exception{
        
        int row = tblSelectedRoles.getSelectedRow();
        int col = tblSelectedRoles.getSelectedColumn();
        if(row != -1 && col != -1){
            //check whether any cell is editing
            if (tblSelectedRoles.isEditing()) {
                /* If there is any cell in editing while the delete
                   button pressed save the editing value to table and perform
                   the appropriate action. */
                String text = "";
                text = ((javax.swing.text.JTextComponent)
                tblSelectedRoles.getEditorComponent()).getText();
                
                
                String convertedDate =
                dtUtils.formatDate(text, "/-:," , "dd-MMM-yyyy");
                if (convertedDate==null  ){
              
                    ((javax.swing.text.JTextComponent)
                    tblSelectedRoles.getEditorComponent()).setText("");
                    tblSelectedRoles.getCellEditor().cancelCellEditing();
                    throw new Exception("Please enter a valid date");
                }else {
                    text = convertedDate;
                    tblSelectedRoles.setValueAt(text, row,col);
                    tblSelectedRoles.getCellEditor().cancelCellEditing();
                }
                
            }
        }
    }
    
    /**
     * Custom listener to for buttons which are used for adding and removing
     * member roles
     */
    private class RoleListener implements ActionListener {
        public void actionPerformed(ActionEvent ae){
            
            if (ae.getSource() instanceof JButton){
                if (((JButton)ae.getSource()).getName().equals("Add")){
                    addSelection();
                }else if(((JButton)ae.getSource()).getName().equals("Remove")){
                    removeSelection();
                }
            }
        }
    }
    
    /**
     * Custom focus adapter which is used for text fields which consists of
     * date values. This is mainly used to format and restore the date value
     * during focus gained / focus lost of the text field.
     */
    private class MemberFocusAdapter extends FocusAdapter{
        CoeusTextField dateField;
        String focusDate;
        String strDate = "";
        boolean temporary = false;
        public void focusGained(FocusEvent fe){
            if (fe.getSource() instanceof CoeusTextField){
                dateField = (CoeusTextField)fe.getSource();
                if ( (dateField.getText() != null)
                &&  (!dateField.getText().trim().equals(""))) {
                    oldData = dateField.getText();
                    if(!dateField.getText().equals(strDate) && !temporary){
                        String focusDate = dtUtils.restoreDate(
                        dateField.getText(),"/-:,");
                        dateField.setText(focusDate);
                    }
                }
            }
        }
        
//        public void focusLost(FocusEvent fe){
//            if (fe.getSource() instanceof CoeusTextField){
//                dateField = (CoeusTextField)fe.getSource();
//                temporary = fe.isTemporary();
//                // String editingValue = null;
//                if ( (dateField.getText() != null)
//                &&  (!dateField.getText().trim().equals(""))
//                && (!temporary) ) {
//                    strDate = dateField.getText();
//                    String convertedDate =
//                    dtUtils.formatDate(dateField.getText(), "/-:," ,
//                    "dd-MMM-yyyy");
//                    if (convertedDate==null ){
//                        edu.mit.coeus.utils.CoeusOptionPane.showErrorDialog("Please Enter a valid date");
//                        dateField.requestFocus();
//                        temporary = true;
//                    }else {
//                        focusDate = dateField.getText();
//                        dateField.setText(convertedDate);
//                        temporary = false;
//                        if(!oldData.equals(convertedDate)){
//                            saveRequired = true;
//                        }
//                        if(functionType == 'A') {
//                            if(dateField.equals(txtTermStart)) {
//                                if((txtFromDate.getText() == null) ||
//                                (txtFromDate.getText().trim().equals("")) ) {
//                                    txtFromDate.setText(dateField.getText());
//                                }
//                            }
//                            if(dateField.equals(txtTermEnd)){
//                                if((txtToDate.getText() == null) ||
//                                (txtToDate.getText().trim().equals(""))){
//                                    txtToDate.setText(dateField.getText());
//                                }
//                            }
//                        }
//                
//                    }
//                }
//                /*
//                 * Changes done as per the Change Request id : 170, Feb' 14 2003
//                 *
//                 * Populating the status start date and end date values with
//                 * term start date and end date in Add mode.
//                 * Modified by : Raghunath P.V.  Feb' 17 2003
//                 */
//                /*if ( (dateField.getText() != null)
//                &&  (!dateField.getText().trim().equals("")) &&
//            functionType == 'A') {
//                    if(dateField.equals(txtTermStart)) {
//                        if((txtFromDate.getText() == null) ||
//                        (txtFromDate.getText().trim().equals("")) ) {
//                            txtFromDate.setText(dateField.getText());
//                        }
//                    }
//                    if(dateField.equals(txtTermEnd)){
//                        if((txtToDate.getText() == null) ||
//                        (txtToDate.getText().trim().equals(""))){
//                            txtToDate.setText(dateField.getText());
//                        }
//                    }
//                }*/
//                
//            }
//        }
    }
    
/*
 * Inner class to set the editor for date columns/cells.
 */
    private class DateEditor extends DefaultCellEditor implements TableCellEditor {
        
        private String colName;
        private static final String DATE_SEPARATERS = ":/.,|-";
        private static final String REQUIRED_DATEFORMAT = "dd-MMM-yyyy";
        private CoeusTextField dateComponent = new CoeusTextField();
        boolean temporary;
        String colValue = "";
        String oldData = "";
        int col = 1;
        int row = 0;
        
        DateEditor(String colName) {
            super(new CoeusTextField());
            this.colName = colName;
            dateComponent.addFocusListener(new FocusAdapter(){
                public void focusGained(FocusEvent fe){
                    temporary = false;
                }
                public void focusLost(FocusEvent fe){
                    dateComponent = (CoeusTextField)fe.getSource();
                    if ( (dateComponent.getText() != null)
                    &&  (!dateComponent.getText().trim().equals(""))){
                        if(!temporary){
                            stopCellEditing();
                        }
                    }else{
                        tblSelectedRoles.getCellEditor().cancelCellEditing();
                    }
                }
                
            });
        }
        
        /**
         * An overridden method to set the editor component in a cell.
         * @param table - the JTable that is asking the editor to edit;
         * can be null
         * @param value - the value of the cell to be edited; it is up to the
         * specific editor to interpret and draw the value.
         * For example, if value is the string "true", it could be rendered as
         * a string or it could be rendered as a check box that is checked.
         * null is a valid value
         * @param isSelected - true if the cell is to be rendered with
         * highlighting
         * @param row - the row of the cell being edited
         * @param column - the column of the cell being edited
         * @return the component for editing
         */
        public Component getTableCellEditorComponent(JTable table, Object value,
        boolean isSelected, int row, int column){
            
            CoeusTextField txtField = dateComponent;
            // take the current value and convert the date to actual date
            String currentValue = (String)value;
            colValue = currentValue;
            oldData = currentValue;
            col = column;
            this.row = row;
            if( ( currentValue != null  )
            && (currentValue.trim().length()!= 0) ){
                String newValue = dtUtils.restoreDate(currentValue,
                DATE_SEPARATERS) ;
                txtField.setText(newValue);
                return dateComponent;
            }else{
                txtField.setText("");
            }
            
            return dateComponent;
        }
        /**
         * This method returns the mouse click counts after which the editor
         * should be invoked
         * @return int mouse click counts after which editor will be invoked
         */
        public int getClickCountToStart(){
            return 2;
        }
        /**
         * Forwards the message from the CellEditor to the delegate.
         * @return boolean true if editing was stopped; false otherwise
         */
        public boolean stopCellEditing() {
            try {
                String editingValue = (String)getCellEditorValue();
                int colNum = tblSelectedRoles.getSelectedColumn();
                int rowNum = tblSelectedRoles.getSelectedRow();
                
                if(editingValue!= null && editingValue.trim().length()>0){
                    //if there is any content in date column check for
                    //validity of that date
                    String formattedDate = dtUtils.formatDate(editingValue,
                    DATE_SEPARATERS,REQUIRED_DATEFORMAT);
                    if(null == formattedDate ) {
                        temporary = true;
                        CoeusOptionPane.showErrorDialog("Please enter a valid date");
                        dateComponent.requestFocusInWindow();
                        if(rowNum > -1 && colNum >0){
                            tblSelectedRoles.setRowSelectionInterval(rowNum, rowNum);
                            tblSelectedRoles.setColumnSelectionInterval(colNum, colNum);
                        }
                        
                        temporary = false;
//                        dateComponent.requestFocusInWindow();
                        //dateComponent.setText("");
                        //cancelCellEditing();
                        return false;
                    }else{
                        dateComponent.setText( formattedDate);
                        temporary = true;
                        if(!oldData.equals(formattedDate)){
                            saveRequired = true;
                        }
                    }
                }
            }
            catch(ClassCastException exception) {
                return false;
            }
            return super.stopCellEditing();
        }
        
        /** Returns the value contained in the editor.
         * @return the value contained in the editor
         */
        public Object getCellEditorValue() {
            return dateComponent.getText();
        }
        
        /**
         * Invoked when an cell has been selected or deselected by the user.
         * The code written for this method performs the operations that need
         * to occur when an cell is selected (or deselected).
         * @param e ItemEvent which dispatched the event
         */
        public void itemStateChanged(ItemEvent e) {
            super.fireEditingStopped();
        }
    }
    
    /** This method is used to fetch the person id from the database for the
     * specified person name.
     *
     * @param name String representing name of the person whose id is required.
     * @return ID of the specified person.
     */
    
    public String getPersonID(String name){
        boolean success=false;
        String personID=null;
        RequesterBean requester = new RequesterBean();
        requester.setFunctionType('Y');
        requester.setDataObject(name);
        String connectTo = connectionURL + "/comMntServlet";
        AppletServletCommunicator comm
        = new AppletServletCommunicator(connectTo, requester);
        comm.send();
        edu.mit.coeus.brokers.ResponderBean response = comm.getResponse();
        if (response!=null){
            success=true;
            personID = Utils.checkNullStr((String)response.getDataObject());
        }
        
        return personID;
    }
    /** This method is used to fetch the person details from the database for the
     * specified person id.
     *
     * @param id String representing person id for which the details are required.
     * @return Person name corresponding to the specified person id.
     */
    public String getPersonDetails(String id){
        boolean success=false;
        String personName= "";
        RequesterBean requester = new RequesterBean();
        requester.setFunctionType('V');
        requester.setDataObject(id);
        String connectTo = connectionURL + "/comMntServlet";
        AppletServletCommunicator comm
        = new AppletServletCommunicator(connectTo, requester);
        comm.send();
        edu.mit.coeus.brokers.ResponderBean response = comm.getResponse();
        PersonInfoFormBean personInfoFormBean = (PersonInfoFormBean)
        response.getDataObject();
        if (personInfoFormBean!=null) {
            success=true;
            personName = ((Utils.checkNullStr(
            personInfoFormBean.getFullName().toString()) == null )? ""
            :" " + personInfoFormBean.getFullName().toString());
        }
        
        return personName;
    }
    
    /** This method is used to fetch the Rolodex details from the database for the
     * specified rolodex id.
     *
     * @param id String representing rolodex id for which the details are required.
     * @return Rolodex name corresponding to the specified rolodex id.
     */
    
    public String getRolodexDetails(String id){
        boolean success=false;
        String rolodexName= "";
        RequesterBean requester = new RequesterBean();
        requester.setFunctionType('V');
        requester.setDataObject(id);
        String connectTo = connectionURL + "/rolMntServlet";
        AppletServletCommunicator comm
        = new AppletServletCommunicator(connectTo, requester);
        comm.send();
        edu.mit.coeus.brokers.ResponderBean response = comm.getResponse();
        RolodexDetailsBean rolodexDetailsBean = (RolodexDetailsBean)
        response.getDataObject();
        if (rolodexDetailsBean!=null) {
            success=true;
            String firstName = ( rolodexDetailsBean.getFirstName()==null ? ""
            :" " + rolodexDetailsBean.getFirstName().toString());
            String middleName = ( rolodexDetailsBean.getMiddleName()==null ? ""
            : " " + rolodexDetailsBean.getMiddleName().toString() + " ");
            String lastName = ( rolodexDetailsBean.getLastName()==null ? ""
            :rolodexDetailsBean.getLastName().toString());
            String prefix = ( rolodexDetailsBean.getPrefix()==null ? ""
            :", " + rolodexDetailsBean.getPrefix().toString() + " ");
            String suffix = ( rolodexDetailsBean.getSuffix()==null ? ""
            :" " + rolodexDetailsBean.getSuffix().toString());
            if (lastName.length()>0){
                rolodexName = (lastName + suffix + prefix
                + firstName + middleName).trim();
            }else{
                rolodexName = ( (Utils.checkNullStr(
                rolodexDetailsBean.getOrganization().toString())==null)
                ? ""
                :rolodexDetailsBean.getOrganization().toString().trim());
            }
            
        }
        return rolodexName;
    }
    public void requestDefaultFocusForComponent(){
        if( functionType == 'M'){
            txtTermStart.requestFocusInWindow();
            
        }else if( functionType == 'A'){
            txtPerson.requestFocusInWindow();
            
        }
    }
    
    
    private PersonInfoFormBean getPersonInfo(String name){
        
        Vector vsearchData=null;
        RequesterBean requester = new RequesterBean();
        if(name!=null && name.trim().length() > 0){
            requester.setDataObject("GET_PERSONINFO");
            requester.setId(name);
        }
        String connectTo = connectionURL + "/coeusFunctionsServlet";
        AppletServletCommunicator comm
                    = new AppletServletCommunicator(connectTo, requester);
        comm.send();
        ResponderBean response = comm.getResponse();
        if (response!=null){
            
            vsearchData = new Vector();
            PersonInfoFormBean personInfoFormBean =
                                (PersonInfoFormBean) response.getDataObject();
            if(personInfoFormBean != null){
                return personInfoFormBean;
            }
        }
        return null;
    }
    
    
    public class DateVerifier extends InputVerifier {
        public DateVerifier(){}
        String validDate;
        boolean verified;
        String convertedDate;
        public boolean verify(JComponent field){
            verified = true;
            // make the validatio here return true if ok false if not ok
            
            String strDate = ((JTextField)field).getText();            
            if(strDate.trim().equals("")){//return true if date textfield is empty.
                return true;
            }
            
            if(strDate.equals(convertedDate)){
                return true;
            }
            convertedDate = dtUtils.formatDate(((JTextField)field).getText(), 
                                        "/-:," ,"dd-MMM-yyyy");
            if (convertedDate==null ){
//                Runnable runnable = new Runnable() {
//                    public void run() {
//                        CoeusOptionPane.showErrorDialog("Please Enter a valid date....dateverifier");
//                    }
//                };
//                SwingUtilities.invokeLater(runnable);
                return false;
            }
            validDate = convertedDate;
            
//            ((JTextField)field).setText(convertedDate);
//            if(functionType == 'A') {
//                if(field.equals(txtTermStart)) {
//                    if((txtFromDate.getText() == null) ||
//                    (txtFromDate.getText().trim().equals("")) ) {
//                        txtFromDate.setText(((JTextField)field).getText());
//                    }
//                }
//                if(field.equals(txtTermEnd)){
//                    if((txtToDate.getText() == null) ||
//                    (txtToDate.getText().trim().equals(""))){
//                        txtToDate.setText(((JTextField)field).getText());
//                    }
//                }
//            }            
            return true;
        }
        
        public String getValidDate(){
            return validDate;
        }
public boolean shouldYieldFocus(JComponent field) {
    if( !verified ) {       
          if (verify(field)) {
                ((JTextField)field).setText(getValidDate());
                if( !oldData.equals(getValidDate()) ){
                    saveRequired = true;
                }
                if(functionType == 'A') {
                    if(field.equals(txtTermStart)) {
                        if((txtFromDate.getText() == null) ||
                        (txtFromDate.getText().trim().equals("")) ) {
                            txtFromDate.setText(((JTextField)field).getText());
                        }
                    }
                    if(field.equals(txtTermEnd)){
                        if((txtToDate.getText() == null) ||
                        (txtToDate.getText().trim().equals(""))){
                            txtToDate.setText(((JTextField)field).getText());
                        }
                    }
                }      
             verified = false;   
            return true;
          }

          field.setInputVerifier(null);          
        Runnable runnable = new Runnable() {
            public void run() {
                CoeusOptionPane.showErrorDialog("Please Enter a valid date");
            }
        };
        SwingUtilities.invokeLater(runnable);
          // Reinstall the input verifier.
          field.setInputVerifier(this);
          // Tell whoever called us that we don't want to yeild focus.
          verified = false;
          return false;
        }else{
            verified = false;
            return true;
         }
    }
    }
}
