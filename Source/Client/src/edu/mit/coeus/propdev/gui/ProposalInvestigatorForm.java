/*
 * @(#)ProposalInvestigatorForm.java
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 */

/* PMD check performed, and commented unused imports and variables on 16-SEPT-2010
 * by Satheesh Kumar K N
 */

package edu.mit.coeus.propdev.gui;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.text.MessageFormat;

import javax.swing.*;
import javax.swing.table.*;
import javax.swing.event.*;

import java.util.*;

import edu.mit.coeus.gui.*;
import edu.mit.coeus.irb.bean.*;
import edu.mit.coeus.propdev.bean.*;
import edu.mit.coeus.rolodexmaint.gui.RolodexMaintenanceDetailForm;
import edu.mit.coeus.utils.*;
import edu.mit.coeus.search.gui.*;
import edu.mit.coeus.brokers.*;
import edu.mit.coeus.unit.bean.*;
import edu.mit.coeus.unit.gui.UnitDetailForm;
import edu.mit.coeus.departmental.gui.*;
import edu.mit.coeus.bean.RoleRightInfoBean;
import edu.mit.coeus.exception.*;
import edu.mit.coeus.utils.investigator.invCreditSplit.InvCreditSplitObject;
import edu.mit.coeus.utils.investigator.invCreditSplit.InvestigatorCreditSplitController;
import edu.mit.coeus.gui.URLOpener;

// JM 8-30-2011 added for help
import edu.vanderbilt.coeus.gui.CoeusHelpGidget;
import edu.vanderbilt.coeus.gui.CoeusToolTip;
// END

// JM custom table cell renderer
import edu.vanderbilt.coeus.gui.PersonTableCellRenderer;
// JM END 

// JM 1-26-2016	change button label based on parameter
import edu.vanderbilt.coeus.utils.CustomFunctions;
// JM

/**
 * This class is used to create ProposalInvestigatorForm tab window with the
 * privilege of adding, deleting and finding new person/unit. It allows to
 * certify the person Entry with Certify button. This component
 * is instantiated from <CODE>ProposalDetailForm</CODE>.
 *
 * @author Raghunath P.V.  Created on April 22, 2003, 9:48 AM
 */
class ProposalInvestigatorForm extends javax.swing.JComponent implements TypeConstants, ActionListener, ListSelectionListener {

    private CoeusAppletMDIForm mdiForm;

    /* holds the Data Vector for person entries. each object contains a vector
     * of ProposalInvestigatorFormBean data objects
     */
    private Vector personData;
//    private Vector vecAnswer;
    private String sponsorCode;
    private String sponsorName;

    /* holds complete Investigator data with key as personId and
     * ProposalInvestigatorFormBean as value */
    private Hashtable investigatorData;

    /* holds complete Unit details for each investigator with personId as Key
     * and ProposalLeadUnitsBean as value */
    private Hashtable unitHashData;

    /* holds the fucntion Type which specifies the mode in which the form has
     * been opened */
    private char functionType;

    //hodls the person list slection instance
    private ListSelectionModel perSelectionModel;

    //holds the list selection model for unit table
//    private ListSelectionModel unitSelectionModel;

    /* holds the collection of existing investigator beans when the form is opened
     * in MODIFY_MODE or DISPLAY_MODE */
    private Vector existingInvestigators;

    //holds the previously selected row index of person table.
    private int previousSelRow = -1;

    //flag used to determine whether the data is to be saved or not
    private boolean saveRequired;

    private boolean certifyEnabled;


    /* flag which specifies whether the particular investigator details
     * have to stored in database  */
    private boolean investigatorModified;
     private boolean viewDeptRightExist;
     private boolean isNewRow=false;
      private Object isPPC;
    //holds the connection string
    private String connectionURL = CoeusGuiConstants.CONNECTION_URL;

    //holds CoeusMessageResources instance used for reading message Properties.
    private CoeusMessageResources coeusMessageResources;

    private String leadUnitNumber;

    private String proposalUnitNumber, proposalUnitName, proposalId;

    private ProposalCertifyForm certifyForm;


    private Vector questions; //Proposal Investigator questions
    private Hashtable moreExplanations; // more explanations for the questions

    //role rights
    private Vector roleRights;

    private final String CERTIFY_RIGHT_ID = "CERTIFY";

    private ProposalDetailForm detailForm;

    private Vector persons = new Vector();

    //Bug Fix : 1222 - STEP 6 - START
    private NameEditor nmEdtName;
    //Bug Fix : 1222 - STEP 6 - END

    private boolean parent;
    //Added by Tarique for proposal Hierarchy
    private boolean inHierarchy = false;
    //Added by Tarique end here

    //Added for Coeus 4.3 -PT ID:2229 Multi PI - start
    private int POINTER_COLUMN = 0;
    private int NAME_COLUMN = 1;
    private int PI_COLUMN = 2;
    private int MULTI_PI_COLUMN = 3;
    private int FACULTY_COLUMN = 4;
    private int EFFORT_COLUMN = 5;
    //Commented for Coeus 4.3-PT ID:2270 Tracking Effort- start
//    private int PERSON_ID_COLUMN = 6;
//    private int NONEMP_COLUMN = 7;
    //Commented for Coeus 4.3-PT ID:2270 Tracking Effort- end
    //Added for Coeus 4.3 -PT ID:2229 Multi PI - end

    //Added for Coeus 4.3-PT ID:2270 Tracking Effort- start
    private int ACADEMIC_YEAR_COLUMN = 6;
    private int SUMMER_EFFORT_COLUMN = 7;
    private int CALENDAR_YEAR_COLUMN = 8;
    private int PERSON_ID_COLUMN = 9;
    private int NONEMP_COLUMN = 10;
    private int CERTIFY_COLUMN = 11;
    
    /* JM 2-1-2016 new isExternalPerson column */
    private int IS_EXTERNAL_PERSON_COLUMN = 12;
    /* JM END */
    
    //Added for Coeus 4.3-PT ID:2270 Tracking Effort- end
    //Added for COEUSQA-2037 : Software allows you to delete an investigator who is assigned credit in the credit split window - Start
    private static final String PROPOSAL_MODULE_CODE = "3";
    private static final int MODULE_CODE_INDEX = 0;
    private static final int MODULE_ITEM_KEY_INDEX = 1;
    private static final int MODULE_ITEM_KEY_SEQUENCE_INDEX = 2;
    private static final int PERSON_ID_INDEX = 3;
    private static final int ADDS_TO_HUNDRED_INDEX = 4;
    private static final int UNIT_NUMBER_INDEX = 5;
    private static final int PROPOSAL_SEQUENCE = 0;
    private static final char CHECK_CREDIT_SPLIT_FOR_INVESTIGATOR = 'L';
    private static final char CHECK_CREDIT_SPLIT_FOR_INV_UNIT = 'U';
    private static final String SAVE_BEFORE_OPEN_CREDIT_SPILT = "module_investigator_exceptionCode.1000";
    private static final String CREDIT_SPLIT_EXISTS_FOR_INV = "proposal_investigatorCreditSplit_exceptionCode.10001";
    private static final String CREDIT_SPLIT_EXISTS_FOR_INV_UNIT = "proposal_investigatorUnitCreditSplit_exceptionCode.10002";
//~~~~ new 1/19/2011
    private ProposalDevelopmentFormBean proposalDevelopmentFormBean = null;
    static final String connect = CoeusGuiConstants.CONNECTION_URL + "/ProposalActionServlet";
    private static final char GET_PARAMETER_VALUE='y';
    private final char MAINTAIN_PERSON_CERTIFICATION ='x';
    private final char MAINTAIN_DEPT_PERSONNEL_CERT  ='3';
    private final char VIEW_DEPT_PERSNL_CERTIFN  ='u';
    private boolean rightExist;
    private boolean parameterExist;
    private boolean deptRightExist;
    private boolean viewDeptRightExistForAggre;
         ImageIcon checkIcon;
        ImageIcon crossIcon;
        ImageIcon emptyIcon=null;
    //COEUS-67
    private static final char GET_APP_HOME_URL = 'o';  
    //COEUS-67
   //COEUSQA-3956
    private static final char IS_NEED_TO_SHOW_YNQ_WHEN_PPC = 'j';  
    //COEUSQA-3956   
    //COEUSQA-2037 : End
    
	// JM 11-14-2012 custom button labels; 1-26-2016 dynamic Find Employee button label
	private static String FIND_NON_EMPLOYEE = "Find Non-Employee";
	private String FIND_EMPLOYEE = "Find VU Employee";
	private String acronym, sister;
	// JM END    
    
    /** Creates new form ProposalInvestigatorForm */
    public ProposalInvestigatorForm() {
    }
    /**
     * Creates new ProposalInvestigatorForm with the given functionType and collection
     * of Investigators data.
     *
     * @param functionType character which represents the form opened mode.
     * @param investigators Collection of ProposalInvestigatorFormBean
     */
    public ProposalInvestigatorForm(char functionType, Vector investigators) {

        this.functionType = functionType;
        existingInvestigators = new Vector();
        existingInvestigators = investigators;
        investigatorData = constructInvHashData( investigators );
        
      
    }

    // supporting method to construct investigator hashtable with personId as key
    // and ProposalInvestigatorFormBean as value.
    private Hashtable constructInvHashData( Vector dataBean ){

        Hashtable newData = new Hashtable();
        unitHashData = new Hashtable();
        personData = new Vector();
        if( dataBean == null ){
            return newData;
        }

        ProposalInvestigatorFormBean propInvestigatorBean = null;

        Vector personTableRow = null;
        String personId = null;
        Vector unDetail = null;
        if( persons == null ) {
            persons = new Vector();
        }
        for( int indx = 0; indx < dataBean.size() ; indx ++ ){
            try{
                // construct table data from vector of investigator beans
                propInvestigatorBean = new ProposalInvestigatorFormBean();
                propInvestigatorBean = ( ProposalInvestigatorFormBean )
                dataBean.get( indx );
                if( propInvestigatorBean != null){

                    personId  = propInvestigatorBean.getPersonId();
                    /* insert each investigator bean into hashtable with personId
                      as key and investigator bean as value */
                    newData.put( personId, propInvestigatorBean );
                    persons.addElement( personId );
                    personTableRow = new Vector();

                    /* JM 9-7-2015 add person status */
                    //personTableRow.add( "");
                    personTableRow.add(propInvestigatorBean.getStatus());
                    /* JM END */
                    
                    personTableRow.add(propInvestigatorBean.getPersonName());
                    personTableRow.add( new Boolean(propInvestigatorBean.isPrincipleInvestigatorFlag() ));
                    personTableRow.add(new Boolean(propInvestigatorBean.isMultiPIFlag()));
                    personTableRow.add( new Boolean( propInvestigatorBean.isFacultyFlag() ));
                    
                    if(propInvestigatorBean.getPercentageEffort() == 0.0){
                        personTableRow.add( ".00" );
                    }else{
                        personTableRow.add( Float.toString(propInvestigatorBean.getPercentageEffort()));
                    }
                    //Modified for Coeus 4.3 -PT ID:2229 Multi PI - end

                    //Added for Coeus 4.3-PT ID:2270 Tracking Effort- start
                    if(propInvestigatorBean.getAcademicYearEffort() == 0.0){
                        personTableRow.add( ".00" );
                    }else{
                        personTableRow.add( Float.toString(propInvestigatorBean.getAcademicYearEffort()));
                    }
                    
                    if(propInvestigatorBean.getSummerYearEffort() == 0.0){
                        personTableRow.add( ".00" );
                    }else{
                        personTableRow.add( Float.toString(propInvestigatorBean.getSummerYearEffort()));
                    }
                    
                    if(propInvestigatorBean.getCalendarYearEffort() == 0.0){
                        personTableRow.add( ".00" );
                    }else{
                        personTableRow.add( Float.toString(propInvestigatorBean.getCalendarYearEffort()));
                    }
                    //Added for Coeus 4.3-PT ID:2270 Tracking Effort- end

                    personTableRow.add(personId);
                    personTableRow.add(new Boolean(propInvestigatorBean.isNonMITPersonFlag()));
                    //Added for Case# 2229
                    //added for ppc certify flag..starts
                    isPPC=propInvestigatorBean.isCertifyFlag();
                    //personTableRow.add( new Boolean( propInvestigatorBean.isCertifyFlag() ));
                    personTableRow.add(isPPC);
                    //added for ppc certify flag..ends
                    
                    /* JM 1-29-2016 add isExternalPerson flag */
                    personTableRow.add(propInvestigatorBean.getIsExternalPerson());
                    /* JM END */
                    
                    unDetail = new Vector();
                    unDetail = propInvestigatorBean.getInvestigatorUnits();
                    /* add investigator units into hashtable with investigator id
                     as key and unit bean as value */
                    if( personId != null && unDetail != null ){
                        unitHashData.put( personId , (Vector)unDetail.clone() );
                    }
                    personData.add( personTableRow );
                }
            }catch( Exception err ){
                err.printStackTrace();
            }
        }
        return newData;
    }
    //Added by Tarique for proposal hierachy start
    public JComponent showPropHieInvestigatorForm(CoeusAppletMDIForm mdiForm , boolean inHierarchy){
        this.inHierarchy = inHierarchy;
        JComponent investigatorForm
                = showProposalInvestigatorsForm(mdiForm);
        return this;

    }
    //Added by Tarique end here
    /** This method is used to initialize the form components,set the form
     * data and set the enabled status for all components depending on the
     * <CODE>functionType</CODE> specified while opening the form.
     *
     * @param mdiForm reference to the parent component <CODE>CoeusAppletMDIForm</CODE>
     * @return JComponent reference to the <CODE>ProposalInvestigatorForm</CODE> component
     * after initializing and setting the data.
     */
    public JComponent showProposalInvestigatorsForm(CoeusAppletMDIForm mdiForm){
        this.mdiForm = mdiForm;
        initComponents();
        postInitComponents();
  //checking parameter
        try
          {

            parameterExist=fetchParameterValue();
            //COEUSQA-3956 Start
            if(parameterExist){
                if(ISNeedToShowYNQWhenPPCEnabled(proposalId)){
                    parameterExist = false;
                }
            }          
           //COEUSQA-3956 End            
            if(parameterExist){
             viewDeptRightExistForAggre=getViewCertificationRight();
            rightExist=getMaintainPersonCertificationRight();
            if(!rightExist){
                deptRightExist=getMaintainDepartmentPersonCertificationRight();
            }
            else if(!rightExist && !deptRightExist)
            {
                viewDeptRightExist=getViewDepartmentPersonCertificationRight();
            }

            if(rightExist||deptRightExist||viewDeptRightExist||viewDeptRightExistForAggre){

            certifyEnabled = true;}
//Added for COEUSDEV-736:
//This code is added to disable  Certify button  if the proposal is a child proposal.
            if(inHierarchy){
          boolean  isParent = isParent();
              if(isParent){
                  certifyEnabled = true;
              }
              else
              {   certifyEnabled = false;}
               }
//Added for COEUSDEV-736: to disable Certify button end.

            }
            else
            {
             validateCertifyButton();
            }
            }
            catch(Exception ex)
            {
                ex.printStackTrace();
            }
      //  validateCertifyButton();
   //checking parameter

        setFormData();
        formatFields();
        setTableEditors();
        coeusMessageResources = CoeusMessageResources.getInstance();
        Component[] comp = { tblPerson,btnAdd,btnDelete,btnFindRolodex,btnFindPerson,
        btnCertify,
        //Case 2106 Start 1
        btnCreditSplit,
        //Case 2106 End 1

        tblLead,btnAddUnit,btnDelUnit,btnFindUnit
        };
        ScreenFocusTraversalPolicy traversal = new ScreenFocusTraversalPolicy(comp);
        setFocusTraversalPolicy(traversal);
        setFocusCycleRoot(true);

        return this;
    }



    // supporting method to set listeners to all buttons and table models.
    private void postInitComponents(){

        btnAdd.addActionListener( this );
        btnDelete.addActionListener( this );
        btnDelete.setEnabled( false );
        btnFindRolodex.addActionListener( this );
        btnFindPerson.addActionListener( this );
        btnAddUnit.addActionListener( this );
        btnCertify.addActionListener( this );
        btnCertify.setEnabled(false);
        btnDelUnit.addActionListener( this );
        btnDelUnit.setEnabled( false );
        btnFindUnit.addActionListener( this );

        //Case 2106 Start 2
        btnCreditSplit.addActionListener( this );
        //Case 2106 End 2

        // Define and set the Table Model for investigator table
        setInvestigatorModel();

        perSelectionModel = tblPerson.getSelectionModel();
        perSelectionModel.addListSelectionListener( this );
        perSelectionModel.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION );
        tblPerson.setSelectionModel( perSelectionModel );
        tblPerson.getTableHeader().setFont(CoeusFontFactory.getLabelFont());
        
        // Define and set the Table Model for lead table
        setUnitLeadModel();
        ListSelectionModel unitSelectionModel = tblLead.getSelectionModel();
        unitSelectionModel.addListSelectionListener( this );
        unitSelectionModel.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION );
        tblLead.setSelectionModel( unitSelectionModel );

        tblLead.getTableHeader().setFont(CoeusFontFactory.getLabelFont());
        tblLead.setFont(CoeusFontFactory.getNormalFont());
        if( tblLead.getRowCount() > 0 ){
            btnDelUnit.setEnabled( true );
        }

        tblPerson.addMouseListener( new PersonDisplayAdapter());

        tblLead.addMouseListener( new UnitDisplayAdapter());
    }

     private void validateCertifyButton(){

//        if ( functionType != DISPLAY_MODE ){
        // enable or disable the certify button
        int idx = roleRights.size();
        //boolean enabled = false;
        for (int rightCount = 0 ;rightCount < idx ; rightCount++) {
            RoleRightInfoBean roleRightInfoBean = (RoleRightInfoBean) roleRights.elementAt(rightCount);
            if (roleRightInfoBean.getRightId().equalsIgnoreCase(CERTIFY_RIGHT_ID) ) {
                //btnCertify.setEnabled(true);
                certifyEnabled = true;
                break;
            }
            else {certifyEnabled = false;}
        }
        //btnCertify.setEnabled(enabled);
//        }
    }

    /**
     * Method used to set the Proposal Role Rigths
     * @param roleRights Collection of user rights.
     */

    public void setRoleRights(Vector roleRights){
        this.roleRights = roleRights;
    }
    /**
     * Method used to set the reference of the ProposalDetailForm to this screen.
     *
     * @param detForm reference to the ProposalDetailForm
     */
    public void setDetailForm( ProposalDetailForm detForm) {
        this.detailForm = detForm;
    }
    // supporting method to set the tablemodel for investigator table
    private void setInvestigatorModel(){

        tblPerson.setModel( new DefaultTableModel(new Object[][]{},getPersonTableColumnNames().toArray()){

        	/* JM 2-1-2016 added isExternalPerson column */
            Class[] types = new Class [] {
                Object.class, String.class, Boolean.class, Boolean.class, Boolean.class,
                Object.class,Object.class,Object.class,Object.class, Object.class,Boolean.class,ImageIcon.class,
                String.class
            };
            //Added for Coeus 4.3-PT ID:2270 Tracking Effort- end
            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
            public boolean isCellEditable(int row, int col){
                if( functionType == DISPLAY_MODE ){
                    /* in display mode return all columns as non-editable */
                    return false;
                }else {
                    // check if the proposal in the hierarchy, then enable PI flag component
                    //Code commented and modified for Case#3183 - Proposal Hierarchy enhancement -start
                    //%effort, summer effort, academic year, calendar year and faculty
                    //fields are editable in parent too.
                    //Modified for Coeus 4.3 -PT ID:2229 Multi PI - start
                    //if(col==1 || col==2 || col==4){
//                    if(col== NAME_COLUMN || col== EFFORT_COLUMN || col==FACULTY_COLUMN ||
//                            col == ACADEMIC_YEAR_COLUMN || col == SUMMER_EFFORT_COLUMN ||
//                            col == CALENDAR_YEAR_COLUMN || col == MULTI_PI_COLUMN){
                    if(col == NAME_COLUMN){
                        if(functionType != DISPLAY_MODE  && isParent()){
                            return false;
                        }
                    }
                    if( col == POINTER_COLUMN || col == PERSON_ID_COLUMN || col == NONEMP_COLUMN || 
                    		col == CERTIFY_COLUMN || col == IS_EXTERNAL_PERSON_COLUMN) {
                        // icon, personid and non-employee flag columns are not editable
                        return false;
                    }else if(col == NAME_COLUMN || col == EFFORT_COLUMN || col == FACULTY_COLUMN ||
                            col == ACADEMIC_YEAR_COLUMN || col == SUMMER_EFFORT_COLUMN ||
                            col == CALENDAR_YEAR_COLUMN || col == MULTI_PI_COLUMN ){
                        /* In other modes person name column, PI column and
                          faculty column are editable */
                        return true;
                    }
                    //Code commented and modified for Case#3183 - Proposal Hierarchy enhancement - end
                        /* If any person/rolodex is selected then allow to edit
                           PI column otherwise not */
                    String personId = (String)tblPerson.getValueAt(row,PERSON_ID_COLUMN);
                    //Modified for Coeus 4.3 -PT ID:2229 Multi PI - end
                    if(personId != null && personId.trim().length()>0){
                        return true;
                    }
                    return false;
                }


            }
            
            public void setValueAt(Object value, int row, int col) {

                super.setValueAt(value,row,col);
                fireTableCellUpdated(row,col);
                //Modified for Coeus 4.3 -PT ID:2229 Multi PI - start
//                if(col == 3){
                if(col == PI_COLUMN){
                       /* If setting PI as true for any row, then reset for
                          all other rows as false */
                    boolean set = ((Boolean)value).booleanValue();
                    if(set){
                        //resetPIFlag((String)tblPerson.getValueAt(row,5));
                        resetPIFlag((String)tblPerson.getValueAt(row,PERSON_ID_COLUMN));
                    }
                    saveRequired = true;
                    //}else if(col == 4){
                }else if(col == FACULTY_COLUMN){
                           /* If setting Faculty as true the update the same in the
                           bean which will be available in hashtable
                           unitHashData with personId as key */
                    updateFacultyFlag(row);
                }
 
                else if(col == MULTI_PI_COLUMN){
                    // Set the value of MULTI_PI to the Bean
                    ProposalInvestigatorFormBean investigatorRow=null;
                    investigatorRow = (ProposalInvestigatorFormBean)
                    investigatorData.get( tblPerson.getValueAt( row,
                            PERSON_ID_COLUMN) == null ? "" : tblPerson.getValueAt( row,
                            PERSON_ID_COLUMN).toString() );
                    if( investigatorRow != null ) {
                        investigatorRow.setMultiPIFlag(((Boolean)value).booleanValue());

                        investigatorData.put(investigatorRow.getPersonId(),investigatorRow);
                        saveRequired = true;
                        investigatorModified = true;
                        if( investigatorRow.getAcType() == null ) {
                            investigatorRow.setAcType( UPDATE_RECORD );
                        }
                    }
                }
               

            }
        });
    }

    // supporting method used to update the bean for the specified row with newly
    // set faculty flag.
    private void updateFacultyFlag( int personRow ) {

        ProposalInvestigatorFormBean investigatorRow=null;
        investigatorRow = (ProposalInvestigatorFormBean)
        //Modified for Coeus 4.3-PT ID:2270 Tracking se- start
//        investigatorData.get( tblPerson.getValueAt( personRow,
//                5) == null ? "" : tblPerson.getValueAt( personRow,
//                5).toString() );
        investigatorData.get( tblPerson.getValueAt( personRow,
                PERSON_ID_COLUMN) == null ? "" : tblPerson.getValueAt( personRow,
                PERSON_ID_COLUMN).toString() );
        if( investigatorRow != null ) {
            investigatorRow.setPersonName(
                    (String)tblPerson.getValueAt(personRow,NAME_COLUMN));
            float effortPercentage = tblPerson.getValueAt(personRow,EFFORT_COLUMN) == null ? 0 :
                new Float(tblPerson.getValueAt(personRow,EFFORT_COLUMN).toString().trim()).floatValue();
            investigatorRow.setPercentageEffort( effortPercentage );
            investigatorRow.setPrincipleInvestigatorFlag(
                    ((Boolean)tblPerson.getValueAt( personRow,
                    PI_COLUMN)).booleanValue());

            // Added for Case# 2229
            investigatorRow.setMultiPIFlag(
                    ((Boolean)tblPerson.getValueAt( personRow,
                    MULTI_PI_COLUMN)).booleanValue());

            investigatorRow.setFacultyFlag(
                    ((Boolean)tblPerson.getValueAt( personRow,
                    FACULTY_COLUMN)).booleanValue());
            investigatorRow.setPersonId(
                    (String)tblPerson.getValueAt(personRow,PERSON_ID_COLUMN));
            investigatorRow.setNonMITPersonFlag(
                    ((Boolean)tblPerson.getValueAt( personRow,
                    NONEMP_COLUMN)).booleanValue());
            //Modified for Coeus 4.3 -PT ID:2229 Multi PI - end
            saveRequired = true;
            investigatorModified = true;
            if( investigatorRow.getAcType() == null ) {
                investigatorRow.setAcType( UPDATE_RECORD );
            }
            investigatorData.put(investigatorRow.getPersonId(),investigatorRow);
        }
    }

//added for ppc certify flag....... starts
       public class ImageRenderer extends DefaultTableCellRenderer {
       ImageIcon checkIcon;
        ImageIcon crossIcon;
        ImageIcon emptyIcon=null;
       public ImageRenderer(){
           checkIcon = new ImageIcon(getClass().getClassLoader().getResource(CoeusGuiConstants.COMPLETE_ICON));
           crossIcon = new ImageIcon(getClass().getClassLoader().getResource(CoeusGuiConstants.NONE_ICON));
      }
        public Component getTableCellRendererComponent(JTable table,
        Object value, boolean isSelected, boolean hasFocus, int row,
        int column) {
            
                setOpaque(false);
                ProposalInvestigatorFormBean investigatorRow=null;
                    investigatorRow = (ProposalInvestigatorFormBean)
                    investigatorData.get( tblPerson.getValueAt( row,
                            PERSON_ID_COLUMN) == null ? "" : tblPerson.getValueAt( row,
                            PERSON_ID_COLUMN).toString() );
                if(investigatorRow != null) {
                    value=investigatorRow.isCertifyFlag();
                }
                setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
                    if(value != null && value.equals(true)){
                       setIcon(checkIcon);
                    }else{
                        setIcon(crossIcon);
                    }
            return this;
        }
    }
//added for ppc certify flag....... ends
    // supporting method used to reset the selected row's unit bean whenever
    // lead unit flag is changed. If lead unit check box is deselected it will
    // update the corresponding bean and fires save required. And if the check box
    // is checked it will call resetLeadUnits method which will validate the
    // unit number with proposal lead unit number and sets lead unit flag accordingly.
    private boolean resetUnit( int selectedRowNumber, boolean value) {
        if (!value ) {
            int selPersonRow = tblPerson.getSelectedRow();
            //Modified for Coeus 4.3-PT ID:2270 Tracking Effort- start
            //String personId = (String)tblPerson.getValueAt(selPersonRow,5);
            String personId = (String)tblPerson.getValueAt(selPersonRow,PERSON_ID_COLUMN);
            //Modified for Coeus 4.3-PT ID:2270 Tracking Effort- end
            Vector units = (Vector) unitHashData.get(personId);
            String unitNo =(String) tblLead.getValueAt(selectedRowNumber,2);
            for (int unitCount = 0 ;unitCount < units.size() ; unitCount++) {
                ProposalLeadUnitFormBean unitBean =
                        (ProposalLeadUnitFormBean)units.get(unitCount);
                if(unitBean.getUnitNumber().equals(unitNo)){
                    unitBean.setLeadUnitFlag( false );
                    if( unitBean.getAcType() == null ) {
                        unitBean.setAcType( UPDATE_RECORD );

                    }
                    saveRequired = true;
                    units.set( unitCount, unitBean );
                    return true;
                }
            }
            return false;
        }else{
            return resetLeadUnits( selectedRowNumber );
        }
    }
        /*supporting method to reset the lead unit flag after validating with
         * proposal lead unit number. If the user selected unit number as lead unit
         * is not same as proposal's lead unit number then checks whether proposal's
         * lead unit number is present in the list of unit numbers for that particular
         * investigator. If present, inform user that proposal's lead unit number
         * will be set as investigators lead unit number. If not present, ask
         * user whether he wants to replace the selected unit number with
         * proposal's lead unit number and replace if he wishes.
         */
    private boolean resetLeadUnits( int selectedRowNumber ){
        int rowCount = tblLead.getRowCount();
        int selPersonRow = tblPerson.getSelectedRow();
        //Modified for Coeus 4.3 -PT ID:2229 Multi PI - start
        //String personId = (String)tblPerson.getValueAt(selPersonRow,5);
        String personId = (String)tblPerson.getValueAt(selPersonRow,PERSON_ID_COLUMN);
        //Modified for Coeus 4.3 -PT ID:2229 Multi PI - end
        Vector units = (Vector) unitHashData.get(personId);
        boolean found = false;
        boolean reset = false;
        int foundIndex = -1;
        int unitIndx = -1;
        if(units != null && units.size()>0){
            String unitNo =(String) tblLead.getValueAt(selectedRowNumber,2);
            for( int indx =  tblLead.getRowCount()-1; indx >= 0 ;  indx-- ){
                boolean oldValue = ((Boolean)tblLead.getValueAt(indx, 1)).booleanValue();
                if( oldValue ) {
                    tblLead.setValueAt( new Boolean( false ), indx, 1);
                }
            }
            if( !unitNo.equals( proposalUnitNumber ) ) {
                // selected unit number is not same as proposal's lead unit number
                for (int unitCount = 0 ;unitCount < units.size() ; unitCount++) {
                    ProposalLeadUnitFormBean unitBean =
                            (ProposalLeadUnitFormBean)units.get(unitCount);
                    if(unitBean.getUnitNumber().equals(proposalUnitNumber)){
                        found = true;
                        foundIndex = unitCount;
                        break;
                    }else if( unitBean.getUnitNumber().equals( unitNo ) ) {
                        unitIndx = unitCount;
                    }
                }
                if( found ) {
                    // proposal's lead unit number is present in the list of unit
                    // units for the investigator.
                    CoeusOptionPane.showInfoDialog( "The lead unit for this proposal is\n \'"
                            + proposalUnitNumber + ": " + proposalUnitName +"\'.");
                    ProposalLeadUnitFormBean unitBean =
                            (ProposalLeadUnitFormBean)units.get( foundIndex );
                    unitBean.setLeadUnitFlag( true );
                    tblLead.setValueAt(new Boolean( true ),foundIndex,1);
                    reset = false;
                    if( unitBean.getAcType() == null ) {
                        unitBean.setAcType( UPDATE_RECORD );
                        saveRequired = true;
                    }
                    units.set( foundIndex, unitBean );
                }else {
                    // proposal's lead unit number is not present in the list of unit
                    // units for the investigator. Confirm to replace.

                    int option = CoeusOptionPane.SELECTION_NO;
                    option = CoeusOptionPane.showQuestionDialog( "The lead unit for this proposal is\n \'"
                            + proposalUnitNumber + ": " + proposalUnitName +"\'.\n"
                            + "Do you want to set unit \'" + unitNo + "\' to \'"+ proposalUnitNumber + "\'?",
                            CoeusOptionPane.OPTION_YES_NO, CoeusOptionPane.DEFAULT_YES );

                    if( option == CoeusOptionPane.SELECTION_YES ) {
                        // user wishes to replace
                        ProposalLeadUnitFormBean unitBean = new ProposalLeadUnitFormBean();
                        unitBean.setUnitNumber( proposalUnitNumber );
                        unitBean.setUnitName( proposalUnitName );
                        unitBean.setLeadUnitFlag( true );
                        unitBean.setPersonId( personId );
                        unitBean.setAcType( INSERT_RECORD );
                        units.set( unitIndx, unitBean );
                        saveRequired = true;
                        tblLead.setValueAt(proposalUnitNumber, unitIndx, 2 );
                        tblLead.setValueAt(proposalUnitName,unitIndx, 3 );
                        //tblLead.setValueAt(new Boolean(true), unitIndx, 1);
                        reset = true;
                    }else{
                        reset = false;
                    }
                }
            }else{
                ProposalLeadUnitFormBean unitBean =
                        (ProposalLeadUnitFormBean)units.get( selectedRowNumber );
                unitBean.setLeadUnitFlag( true );
                //tblLead.setValueAt(new Boolean(true),selectedRowNumber,1);
                if( unitBean.getAcType() == null ) {
                    unitBean.setAcType( UPDATE_RECORD );
                }
                units.set( selectedRowNumber, unitBean );
                reset = true;
            }
            unitHashData.put(personId,units);
        }
        return reset;
    }

    //supporting method to reset the pi flag only one occurance allowed
    private void resetPIFlag( String personId){
        ProposalInvestigatorFormBean investigatorRow = (ProposalInvestigatorFormBean)
        investigatorData.get( personId);
        int selectedRowNumber = tblPerson.getSelectedRow();
        if(investigatorRow != null){
            investigatorRow.setPrincipleInvestigatorFlag(true);
            if ( investigatorRow.getAcType() == null ) {
                investigatorRow.setAcType( UPDATE_RECORD );
            }
            investigatorData.put(personId,investigatorRow);

            for( int indx =  tblPerson.getRowCount()-1; indx >= 0 ;  indx-- ){
                if( selectedRowNumber != indx ){
                    //Modified for Coeus 4.3 -PT ID:2229 Multi PI - start
                    tblPerson.setValueAt( new Boolean( false ), indx, PI_COLUMN);
                    String ldPersonId = tblPerson.getValueAt(indx,PERSON_ID_COLUMN).toString();
                    //Modified for Coeus 4.3 -PT ID:2229 Multi PI - end
                    investigatorRow = (ProposalInvestigatorFormBean)investigatorData.get( ldPersonId);
                    if(investigatorRow != null){
                        if( investigatorRow.isPrincipleInvestigatorFlag()
                        && investigatorRow.getAcType() == null ) {
                            investigatorRow.setAcType( UPDATE_RECORD );
                        }
                        investigatorRow.setPrincipleInvestigatorFlag(false);
                        investigatorData.put(ldPersonId,investigatorRow);
                        resetLeadUnitFlag(ldPersonId);
                    }
                }
            }
        }else{
            for( int indx =  tblPerson.getRowCount()-1; indx >= 0 ;  indx-- ){
                if( selectedRowNumber != indx ){
                    //Modified for Coeus 4.3 -PT ID:2229 Multi PI - start
                    tblPerson.setValueAt( new Boolean( false ), indx, PI_COLUMN);
                    //Modified for Coeus 4.3 -PT ID:2229 Multi PI - end
                }
            }
        }
    }

   
    //supporting method to reset the Lead Unit Flag based on person ID selected
    private void resetLeadUnitFlag(String personId) {

        if (personId != null && !personId.equalsIgnoreCase("")){
            Vector units = (Vector)unitHashData.get( personId );
            if( units != null ){
                ProposalLeadUnitFormBean curUnitBean = null;
                for( int indx = 0 ; indx < units.size(); indx ++ ){
                    curUnitBean = (ProposalLeadUnitFormBean) units.get( indx );
                    if (curUnitBean.isLeadUnitFlag()) {
                        curUnitBean.setLeadUnitFlag(false);
                        if( curUnitBean.getAcType() == null ) {
                            curUnitBean.setAcType( UPDATE_RECORD );
                        }
                    }
                    units.set(indx,curUnitBean);
                }
                unitHashData.put( personId ,units);
            }

        }
    }

    //supporting method to check for null value
    private String checkForNull( Object value ){
        return (value==null)? "":value.toString();
        /*return value.toString().equalsIgnoreCase( "null" ) ?  "" :
            value.toString();
         */
    }
    /* supporting method to set the table model to unit table */
    private void setUnitLeadModel(){

        tblLead.setModel(new DefaultTableModel(new Object[][]{},
                getUnitTableColumnNames().toArray()){
            Class[] types = new Class [] {
                java.lang.Object.class,
                java.lang.Boolean.class, java.lang.String.class,
                java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
            public boolean isCellEditable(int row, int col){
                if( functionType == DISPLAY_MODE ){
                    /* in display mode return all columns as non-editable */
                    return false;
                }else {
                    // Check if the proposal is in hierarchy
//                    if(col==1){
//                        if(functionType!= DISPLAY_MODE && isParent()){
//                            return false;
//                        }
//                    }
                        /* In other modes Lead Unit column and Unit Name column
                           are editable */
                    if(col == 2){
                        boolean leadFlag = ((Boolean)tblLead.getValueAt(
                                row,1)).booleanValue();
                        if( leadFlag ){
                            return false;
                        }else{
                            return true;
                        }
                    }else if(col == 0 || col == 3 ){ //|| col == 4
                        /* remaining colums are not editable */
                        return false;
                    }else if(tblPerson.getSelectedRow() != -1){
                            /* if any investigator row is selected and the
                               row is not blank, then allow to change the
                               Lead Unit flag */
                        int selPersonRow = tblPerson.getSelectedRow();
                        //Modified for Coeus 4.3 -PT ID:2229 Multi PI - start
//                        boolean piFlag = ((Boolean)tblPerson.getValueAt(
//                                selPersonRow,3)).booleanValue();
                        boolean piFlag = ((Boolean)tblPerson.getValueAt(
                                selPersonRow,PI_COLUMN)).booleanValue();
                        //Modified for Coeus 4.3 -PT ID:2229 Multi PI - end
                        String unitNum = (String)tblLead.getValueAt(row,2);
                        if(piFlag && (unitNum != null)
                        && (unitNum.trim().length()>0)){
                            return true;
                        }
                    }
                    return false;
                }
            }

            public void setValueAt(Object value, int row, int col) {

                if(col == 1){
                       /* If setting Lead Unit as true for any row, then reset
                          for all other rows as false */
                    boolean set = ((Boolean)value).booleanValue();
                    if ( resetUnit( row, set ) ) {
                        super.setValueAt(value,row,col);
                        fireTableCellUpdated(row,col);
                    }
                }else{
                    super.setValueAt(value,row,col);
                    fireTableCellUpdated(row,col);
                }
            }
        });
    }

    /**
     * This method is used to set the enabled status for the components
     * depending on the functionType specified.
     */
    private void formatFields(){

        boolean enabled = functionType != DISPLAY_MODE ? true : false ;

        btnFindRolodex.setEnabled( enabled );
        btnDelete.setEnabled( enabled );
        btnAdd.setEnabled( enabled );
        btnFindPerson.setEnabled( enabled );
        btnFindUnit.setEnabled( enabled );
        btnAddUnit.setEnabled( enabled );

        btnCertify.setEnabled( certifyEnabled );
        if(functionType == DISPLAY_MODE) {
            
            try{
            parameterExist=fetchParameterValue();
            //COEUSQA-3956 Start
            if(parameterExist){
                if(ISNeedToShowYNQWhenPPCEnabled(proposalId)){
                    parameterExist = false;
                }
            }          
           //COEUSQA-3956 End            
            if(parameterExist){
             viewDeptRightExistForAggre=getViewCertificationRight();
            rightExist=getMaintainPersonCertificationRight();
            if(!rightExist){
                deptRightExist=getMaintainDepartmentPersonCertificationRight();
            }
            else if(!rightExist && !deptRightExist)
            {
                viewDeptRightExist=getViewDepartmentPersonCertificationRight();
            }
            if(rightExist||deptRightExist||viewDeptRightExist||viewDeptRightExistForAggre){
             btnCertify.setEnabled(true);
            }
//Added for COEUSDEV-736:
//This code is added to disable  Certify button  if the proposal is a child proposal.
             if(inHierarchy){
              boolean  isParent = isParent();
              if(isParent){
                  btnCertify.setEnabled(true);
              }
              else
              {
                  btnCertify.setEnabled(false);
              }
               }
//Added for COEUSDEV-736: to disable Certify button end.
            }
            else{ btnCertify.setEnabled(true);//COEUSDEV-856 parameter ENABLE_PROP_PERSON_SELF_CERTIFY returns false. enable certify button}
            }
            }catch(Exception e){}
        }

        //Added for bug fixed for case #2301 start 1
        /*
         *Commented by Geo
         *It should enable certify button even in display mode
         */
//        if(functionType == DISPLAY_MODE) {
//            btnCertify.setEnabled( enabled );
//        }else{
//            btnCertify.setEnabled( false );
//            if(certifyEnabled){
//            }
//        }
        //Added for bug fixed for case #2301 end 1
        if( tblPerson.getRowCount() < 1 ){
            btnDelete.setEnabled( false );
            btnAddUnit.setEnabled( false );
            btnFindUnit.setEnabled( false );
            btnCertify.setEnabled( false );
        }

        //Added by Amit 11/19/2003
        if(functionType == CoeusGuiConstants.DISPLAY_MODE){

            java.awt.Color bgListColor = (java.awt.Color)javax.swing.UIManager.getDefaults().get("Panel.background");

            tblLead.setBackground(bgListColor);
            tblLead.setSelectionBackground(bgListColor );
            tblLead.setSelectionForeground(java.awt.Color.BLACK);

            tblPerson.setBackground(bgListColor);
            tblPerson.setSelectionBackground(bgListColor );
            tblPerson.setSelectionForeground(java.awt.Color.BLACK);
            //Case 2106 Start 7
            if(tblPerson.getRowCount() < 1){
                btnCreditSplit.setEnabled(false);
            }
            //Case 2106 End 7
        } else{
            tblLead.setBackground(java.awt.Color.white);
            tblLead.setSelectionBackground(java.awt.Color.white);
            tblLead.setSelectionForeground(java.awt.Color.black);

            tblPerson.setBackground(java.awt.Color.white);
            tblPerson.setSelectionBackground(java.awt.Color.white);
            tblPerson.setSelectionForeground(java.awt.Color.black);
            //Case 2106 Start 8
            if(tblPerson.getRowCount() < 1){
                btnCreditSplit.setEnabled(false);
            }
            //Case 2106 End 8
        }
        //end Amit

        boolean isParent = isParent();
        if(isParent && functionType!= DISPLAY_MODE){
            btnFindRolodex.setEnabled( false);
            btnDelete.setEnabled( false );
            btnAdd.setEnabled( false);
            btnFindPerson.setEnabled( false);
            btnFindUnit.setEnabled( false);
            btnAddUnit.setEnabled( false);
            //Code commented for bug fix case#3183
            //enable credit split button for the parent proposal
//            btnCreditSplit.setEnabled(false);
            btnDelUnit.setEnabled( false);
        }

    }

    //supporting method to set the Table Editors like Name editor, empty
    //column editor and icon renderer.
    private void setTableEditors(){

        tblPerson.setOpaque(false);
        tblPerson.setShowVerticalLines(false);
        tblPerson.setShowHorizontalLines(false);
        tblPerson.setRowHeight(22);
        //tblPerson.setSelectionBackground(Color.white);
        //tblPerson.setSelectionForeground(Color.black);
        tblPerson.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tblPerson.setFont( CoeusFontFactory.getNormalFont() );
        JTableHeader header = tblPerson.getTableHeader();
        header.setReorderingAllowed(false);
        //header.setResizingAllowed(false);

        // setting icon renderer to the first column of the investigator table
        //Modified for Coeus 4.3 -PT ID:2229 Multi PI - start
        //TableColumn clmName = tblPerson.getColumnModel().getColumn(0);
        TableColumn clmName = tblPerson.getColumnModel().getColumn(POINTER_COLUMN);
        //Modified for Coeus 4.3 -PT ID:2229 Multi PI - end
        clmName.setPreferredWidth(30);
        clmName.setMaxWidth(30);
        clmName.setMinWidth(30);
        clmName.setHeaderRenderer(new EmptyHeaderRenderer());
        clmName.setCellRenderer(new IconRenderer());

        //setting custom editor for Person name column of investigator table
        //Bug Fix : 1222 - STEP - 5 - START
        //NameEditor nmEdtName = new NameEditor( "Person Name", 90 );
        //made NameEditor nmEdtName as instance variable.
        if(nmEdtName == null) {
            nmEdtName = new NameEditor( "Person Name", 90 );
        }
        //Bug Fix : 1222 - STEP - 5 - START
        //Modified for Coeus 4.3 -PT ID:2229 Multi PI - start
        clmName = tblPerson.getColumnModel().getColumn(NAME_COLUMN);
        clmName.setCellEditor( nmEdtName );
        //Reduced width
        clmName.setPreferredWidth(290);
        clmName.setMinWidth(290);
        clmName.setMaxWidth(290);

        //CurrencyField effortField = new CurrencyField();
        //Changed the order of the column
        //clmName = tblPerson.getColumnModel().getColumn(2);
        clmName = tblPerson.getColumnModel().getColumn(EFFORT_COLUMN);
        //Modified for Coeus 4.3 -PT ID:2229 Multi PI - end
        clmName.setPreferredWidth(60);
        //clmName.setMaxWidth(65);
        clmName.setMinWidth(60);
        clmName.setMaxWidth(60);
        TableCellEditor currencyCellEditor =
                new CurrencyEditor("% Effort"){
            public boolean stopCellEditing(){
                int selRow = tblPerson.getSelectedRow();
                //                System.out.println("editorValue:"+ getCellEditorValue());
                String editorValue = ((javax.swing.text.JTextComponent)
                tblPerson.getEditorComponent()).getText();

                float newEffortVal = new Float( editorValue ).floatValue();
                if( selRow != -1 ) {
                    //Modified for Coeus 4.3 -PT ID:2229 Multi PI - start
                    String personId = (String)tblPerson.getValueAt( selRow,PERSON_ID_COLUMN);
                    float effortPercentage = tblPerson.getValueAt(selRow,EFFORT_COLUMN) == null ? 0 :
                        new Float(tblPerson.getValueAt(selRow,EFFORT_COLUMN).toString().trim()).floatValue();
                    //Modified for Coeus 4.3 -PT ID:2229 Multi PI - end
                    if( personId != null  && investigatorData != null) {
                        if( investigatorData.containsKey( personId ) ) {
                            ProposalInvestigatorFormBean invBean =
                                    (ProposalInvestigatorFormBean)investigatorData.get(personId);
                            if( newEffortVal != effortPercentage ) {
                                invBean.setPercentageEffort( newEffortVal );
                                if( invBean.getAcType() == null ) {
                                    invBean.setAcType( UPDATE_RECORD );
                                    saveRequired = true;
                                    investigatorData.put( personId, invBean );
                                    //Modified for Coeus 4.3 -PT ID:2229 Multi PI - start
                                    //tblPerson.setValueAt(Float.toString(newEffortVal),selRow,2);
                                    tblPerson.setValueAt(Float.toString(newEffortVal),selRow,EFFORT_COLUMN);
                                    //Modified for Coeus 4.3 -PT ID:2229 Multi PI - end
                                }
                            }
                        }
                    }

                }
                return super.stopCellEditing();
            }
        };
        clmName.setCellEditor(currencyCellEditor);

        /* setting widths to PI column and Faculty column */
        //Modified for Coeus 4.3 -PT ID:2229 Multi PI - start
        //changed the column order
        clmName = tblPerson.getColumnModel().getColumn(PI_COLUMN);
        //Modified for Coeus 4.3 -PT ID:2229 Multi PI - end
        clmName.setPreferredWidth(36);
        //clmName.setMaxWidth(30);
        clmName.setMinWidth(36);
        clmName.sizeWidthToFit();

        //Modified for Coeus 4.3 -PT ID:2229 Multi PI - start
        //clmName = tblPerson.getColumnModel().getColumn(4);
        clmName = tblPerson.getColumnModel().getColumn(FACULTY_COLUMN);
        //Modified for Coeus 4.3 -PT ID:2229 Multi PI - end
        clmName.setPreferredWidth(55);
        //clmName.setMaxWidth(60);
        clmName.setMinWidth(55);
        clmName.sizeWidthToFit();

        // to hide person id column
        //Modified for Coeus 4.3 -PT ID:2229 Multi PI - start
        //changed the column order
        //clmName = tblPerson.getColumnModel().getColumn(6);
        clmName = tblPerson.getColumnModel().getColumn(PERSON_ID_COLUMN);
        //Modified for Coeus 4.3 -PT ID:2229 Multi PI - end
        clmName.setPreferredWidth(0);
        //clmName.setMaxWidth(0);
        clmName.setMinWidth(0);

        // to hide Employee column
        //Modified for Coeus 4.3 -PT ID:2229 Multi PI - start
        //changed the column order
        clmName = tblPerson.getColumnModel().getColumn(NONEMP_COLUMN);
        //Modified for Coeus 4.3 -PT ID:2229 Multi PI - end
        clmName.setPreferredWidth(0);
        //clmName.setMaxWidth(0);
        clmName.setMinWidth(0);
        //Added for Coeus 4.3 -PT ID:2229 Multi PI - start
        //Column for Multi PI
        clmName = tblPerson.getColumnModel().getColumn(MULTI_PI_COLUMN);
        clmName.setPreferredWidth(55);
        clmName.setMinWidth(55);
        clmName.sizeWidthToFit();
        //Added for Coeus 4.3 -PT ID:2229 Multi PI - end

        //Added for Coeus 4.3-PT ID:2270 Tracking Effort- start
        clmName = tblPerson.getColumnModel().getColumn(ACADEMIC_YEAR_COLUMN);
        clmName.setPreferredWidth(85);
        clmName.setMinWidth(85);
        clmName.sizeWidthToFit();
        TableCellEditor currencyAcadamicCellEditor =
                new CurrencyEditor("% Academic Year")

        {
            public boolean stopCellEditing(){
                int selRow = tblPerson.getSelectedRow();
                String editorValue = ((javax.swing.text.JTextComponent)
                tblPerson.getEditorComponent()).getText();

                float newAcademicYear = new Float( editorValue ).floatValue();
                if( selRow != -1 ) {
                    String personId = (String)tblPerson.getValueAt( selRow,PERSON_ID_COLUMN);
                    float academicYear = tblPerson.getValueAt(selRow,ACADEMIC_YEAR_COLUMN) == null ? 0 :
                        new Float(tblPerson.getValueAt(selRow,ACADEMIC_YEAR_COLUMN).toString().trim()).floatValue();
                    if( personId != null  && investigatorData != null) {
                        if( investigatorData.containsKey( personId ) ) {
                            ProposalInvestigatorFormBean invBean =
                                    (ProposalInvestigatorFormBean)investigatorData.get(personId);
                            if( newAcademicYear  != academicYear ) {
                                invBean.setAcademicYearEffort( newAcademicYear  );
                                if( invBean.getAcType() == null ) {
                                    invBean.setAcType( UPDATE_RECORD );
                                    saveRequired = true;
                                    investigatorData.put( personId, invBean );
                                    tblPerson.setValueAt(Float.toString(newAcademicYear ),selRow,ACADEMIC_YEAR_COLUMN);
                                }
                            }
                        }
                    }

                }
                return super.stopCellEditing();
            }
        };
        clmName.setCellEditor(currencyAcadamicCellEditor);

        clmName = tblPerson.getColumnModel().getColumn(SUMMER_EFFORT_COLUMN);
        clmName.setPreferredWidth(85);
        clmName.setMinWidth(85);
        clmName.sizeWidthToFit();
        TableCellEditor currencySummerEffortCellEditor =
                new CurrencyEditor("% Summer Effort")


        {
            public boolean stopCellEditing(){
                int selRow = tblPerson.getSelectedRow();
                String editorValue = ((javax.swing.text.JTextComponent)
                tblPerson.getEditorComponent()).getText();

                float newSummerEffort = new Float( editorValue ).floatValue();
                if( selRow != -1 ) {
                    String personId = (String)tblPerson.getValueAt( selRow,PERSON_ID_COLUMN);
                    float summerEffort = tblPerson.getValueAt(selRow,SUMMER_EFFORT_COLUMN) == null ? 0 :
                        new Float(tblPerson.getValueAt(selRow,SUMMER_EFFORT_COLUMN).toString().trim()).floatValue();
                    if( personId != null  && investigatorData != null) {
                        if( investigatorData.containsKey( personId ) ) {
                            ProposalInvestigatorFormBean invBean =
                                    (ProposalInvestigatorFormBean)investigatorData.get(personId);
                            if( newSummerEffort != summerEffort ) {
                                invBean.setSummerYearEffort( newSummerEffort );
                                if( invBean.getAcType() == null ) {
                                    invBean.setAcType( UPDATE_RECORD );
                                    saveRequired = true;
                                    investigatorData.put( personId, invBean );
                                    tblPerson.setValueAt(Float.toString(newSummerEffort),selRow,SUMMER_EFFORT_COLUMN);

                                }
                            }
                        }
                    }

                }
                return super.stopCellEditing();
            }
        };
        clmName.setCellEditor(currencySummerEffortCellEditor);

        clmName = tblPerson.getColumnModel().getColumn(CALENDAR_YEAR_COLUMN);
        clmName.setPreferredWidth(85);
        clmName.setMinWidth(85);
      //  clmName.sizeWidthToFit();
        TableCellEditor currencyCalendarYearCellEditor =
                new CurrencyEditor("% Calendar Year")

        {
            public boolean stopCellEditing(){
                int selRow = tblPerson.getSelectedRow();
                String editorValue = ((javax.swing.text.JTextComponent)
                tblPerson.getEditorComponent()).getText();

                float newCalendarYear = new Float( editorValue ).floatValue();
                if( selRow != -1 ) {
                    String personId = (String)tblPerson.getValueAt( selRow,PERSON_ID_COLUMN);
                    float calendarYear = tblPerson.getValueAt(selRow,EFFORT_COLUMN) == null ? 0 :
                        new Float(tblPerson.getValueAt(selRow,EFFORT_COLUMN).toString().trim()).floatValue();
                    if( personId != null  && investigatorData != null) {
                        if( investigatorData.containsKey( personId ) ) {
                            ProposalInvestigatorFormBean invBean =
                                    (ProposalInvestigatorFormBean)investigatorData.get(personId);
                            //Comment for COEUSDEV-266 : Calendar % Effort will not save in Proposal Development - Start
                            //Allowed to enter same value in Effort % and Calendar Year Effort column
//                            if( newCalendarYear != calendarYear ) {
                            //COEUSDEV-226 : End
                                invBean.setCalendarYearEffort( newCalendarYear );
                                if( invBean.getAcType() == null ) {
                                    invBean.setAcType( UPDATE_RECORD );
                                    saveRequired = true;
                                    investigatorData.put( personId, invBean );
                                    //Modified for COEUSDEV-266 : Calendar % Effort will not save in Proposal Development - Start
                                    //Setting the newCalendarYear column value CALENDAR_YEAR_COLUMN column not to EFFORT_COLUMN
                                    tblPerson.setValueAt(Float.toString(newCalendarYear),selRow,CALENDAR_YEAR_COLUMN);
//                                    tblPerson.setValueAt(Float.toString(newCalendarYear),selRow,EFFORT_COLUMN);
                                    //COEUSDEV-266 : End
                                }
                              //Comment for COEUSDEV-266 : Calendar % Effort will not save in Proposal Development - Start
//                            }
                              //COEUSDEV-226 : End
                        }
                    }

                }
                return super.stopCellEditing();
            }
        };
        clmName.setCellEditor(currencyCalendarYearCellEditor);
        //added for ppc certify flag..starts
        clmName = tblPerson.getColumnModel().getColumn(CERTIFY_COLUMN);
        clmName.setPreferredWidth(55);
        clmName.setMinWidth(55);
        clmName.sizeWidthToFit();
        clmName.setCellRenderer(new ImageRenderer());
        //tblPerson.getTableHeader().setPreferredSize(new Dimension(0, 30)); // JM commented
        //added for ppc certify flag..ends
        
        /* JM 2-1-2016 add isExternalPerson column */
        clmName = tblPerson.getColumnModel().getColumn(IS_EXTERNAL_PERSON_COLUMN);
        clmName.setPreferredWidth(0);
        clmName.setMinWidth(0);
        /* JM END */

// JM 7-5-2011 update header height to be prettier
        //tblLead.getTableHeader().setPreferredSize(new Dimension(tblLead.getColumnModel().getTotalColumnWidth(), 40));
        tblLead.getTableHeader().setPreferredSize(new Dimension(tblLead.getTableHeader().getWidth(),35));
        tblPerson.getTableHeader().setPreferredSize(new Dimension(tblPerson.getTableHeader().getWidth(),35));
// END
        //Added for Coeus 4.3-PT ID:2270 Tracking Effort- end

        tblLead.setOpaque(false);
        tblLead.setShowVerticalLines(false);
        tblLead.setShowHorizontalLines(false);
        tblLead.setRowHeight(22);
        tblLead.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        //tblLead.setSelectionBackground(Color.white);
        //tblLead.setSelectionForeground(Color.black);
        tblLead.setFont( CoeusFontFactory.getNormalFont() );
        header = tblLead.getTableHeader();
        header.setReorderingAllowed(false);
        //header.setResizingAllowed(false);

        // setting icon renderer to the first column of the units table
        clmName = tblLead.getColumnModel().getColumn(0);
        clmName.setPreferredWidth(30);
        clmName.setMaxWidth(30);
        clmName.setMinWidth(30);
        clmName.setHeaderRenderer(new EmptyHeaderRenderer());
        clmName.setCellRenderer(new IconRenderer());

        // setting size to Lead Unit column
        clmName = tblLead.getColumnModel().getColumn(1);
        //Modified for Coeus 4.3 -PT ID:2229 Multi PI - start
        clmName.setPreferredWidth(86);
        //clmName.setMaxWidth(40);
        clmName.setMinWidth(86);
        //Modified for Coeus 4.3 -PT ID:2229 Multi PI - end
        clmName.sizeWidthToFit();

        NumberEditor numEdtName = new NumberEditor( "Number", 8 );
        clmName = tblLead.getColumnModel().getColumn(2);
        clmName.setCellEditor( numEdtName );
        clmName.setPreferredWidth(130);
        //clmName.setMaxWidth(75);
        clmName.setMinWidth(130);

        clmName = tblLead.getColumnModel().getColumn(3);
        //Modified for Coeus 4.3 -PT ID:2229 Multi PI - start
        clmName.setPreferredWidth(580);
        clmName.setMinWidth(580);
        //Modified for Coeus 4.3 -PT ID:2229 Multi PI - end
    }

    private void setFormData(){
    	
        //if(existingInvestigators != null){// commented for fixing the the bug #986 9th July 2004
        /* if investigators available then set the person table data */
        ((DefaultTableModel)tblPerson.getModel()).setDataVector(personData,
                getPersonTableColumnNames());
        ((DefaultTableModel)tblPerson.getModel()).fireTableDataChanged();
        
        /* JM 1-8-2016 added to set font color based on status; passing in MODIFY MODE to retain colors; 
         * 1-29-2016 new method to allow multiple options */
    	//edu.vanderbilt.coeus.gui.CustomTableCellRenderer renderer = 
    	//		new edu.vanderbilt.coeus.gui.CustomTableCellRenderer(POINTER_COLUMN,"I",Color.RED, true, false, false, getFunctionType());
    	PersonTableCellRenderer renderer = 
    			new PersonTableCellRenderer(POINTER_COLUMN,IS_EXTERNAL_PERSON_COLUMN,true,false,false,getFunctionType());
    	tblPerson.getColumnModel().getColumn(NAME_COLUMN).setCellRenderer(renderer);
    	/* JM END */
        
        if( tblPerson.getRowCount() > 0 ) {
            /* show the first investigators unit details */
            //Modified for Coeus 4.3 -PT ID:2229 Multi PI - start
            String firstPerson =(tblPerson.getValueAt( 0,PERSON_ID_COLUMN ) == null ? ""
                    : tblPerson.getValueAt( 0,PERSON_ID_COLUMN ).toString());
            //Modified for Coeus 4.3 -PT ID:2229 Multi PI - end
            if( ( firstPerson != null )&& (!firstPerson.equalsIgnoreCase("") )){
                ((DefaultTableModel)tblLead.getModel()).setDataVector(
                        getUnitTableData( firstPerson ),
                        getUnitTableColumnNames());
                tblPerson.addRowSelectionInterval( 0, 0 );
                if(tblLead.getRowCount() > 0 ){
                    tblLead.setRowSelectionInterval(0,0);
                }
            }else{
                    /* if there are no investigators present show empty table
                     * for unit details table */
                ((DefaultTableModel)tblLead.getModel()).setDataVector(
                        new Object[][]{}, getUnitTableColumnNames().toArray());
            }

        }else{
                    /* if there are no units present show empty table
                     * for units table */
            ((DefaultTableModel)tblLead.getModel()).setDataVector(
                    new Object[][]{}, getUnitTableColumnNames().toArray());
        }
        //}
        setTableEditors();
        saveRequired = false;
    }

    //supporting method to construct and return the Person Table Column headers

    private Vector getPersonTableColumnNames(){

        Vector personHeaders = new Vector();
        personHeaders.add( "");
        personHeaders.add( "Person Name" );
        //Modified for Coeus 4.3 -PT ID:2229 Multi PI - start
        personHeaders.add( "PI" );
        personHeaders.add( "Multi PI" );
        personHeaders.add( "Faculty" );
        personHeaders.add( "Effort %" );
        //Modified for Coeus 4.3 -PT ID:2229 Multi PI - end
        //Added for Coeus 4.3-PT ID:2270 Tracking Effort- start
        // JM 6-14-2012 updated column headers
        personHeaders.add( "<html>Academic <br>Effort %</html>" );
        personHeaders.add( "<html>Summer <br>Effort %</html>" );
        personHeaders.add( "<html>Calendar<br>Effort %</html>" );
        // JM END
        //Added for Coeus 4.3-PT ID:2270 Tracking Effort- end
        personHeaders.add( "" );
        personHeaders.add( "Non employee" );
        personHeaders.add( "Certify" );
        /* JM 2-1-2016 added is not employee column */
        personHeaders.add("Is External Person");
        /* JM END */
        return personHeaders ;
    }

    //supporting method to construct and return the Unit Table Column headers
    private Vector getUnitTableColumnNames(){

        Vector unitHeaders = new Vector();

        unitHeaders.add( "Sl No" );
        //Modified for Coeus 4.3-PT ID:2270 Tracking Effort- start
//        unitHeaders.add( "Lead"  );
//        unitHeaders.add( "Number");
//        unitHeaders.add( "Name"  );
        unitHeaders.add( "Lead Unit"  );
        unitHeaders.add( "Unit Number");
        unitHeaders.add( "Unit Name"  );
        //Modified for Coeus 4.3-PT ID:2270 Tracking Effort- end

        return unitHeaders ;
    }

    private Vector getUnitTableData( String personId ){

        Vector unitTableData = new Vector();
        Vector unitDataBean = new Vector();

        // fetch the unit details from hashtable for specified investigator
        unitDataBean = (Vector)unitHashData.get( personId );

        if( unitDataBean == null || unitDataBean.size() <= 0 ){
            // if there are no unit details present return empty vector
            return unitTableData;
        }
        Vector rowData = new Vector();
        ProposalLeadUnitFormBean investigatorUnitsBean = null;

        for( int indx = 0; indx <  unitDataBean.size(); indx++ ){
            try{
                // construct vector of vectors from unitBeans
                investigatorUnitsBean
                        =  ( ProposalLeadUnitFormBean ) unitDataBean.get( indx );

                rowData = new Vector();

                rowData.add( "");
                rowData.add( new Boolean( investigatorUnitsBean.isLeadUnitFlag() ));
                rowData.add( investigatorUnitsBean.getUnitNumber() );
                rowData.add( investigatorUnitsBean.getUnitName() );

                if( investigatorUnitsBean.isLeadUnitFlag() ) {
                    leadUnitNumber = investigatorUnitsBean.getUnitNumber();
                }

                unitTableData.add( rowData );

                investigatorUnitsBean = null;

                rowData = null;

            }catch( Exception err ){
                err.printStackTrace();
            }
        }
        return unitTableData;
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        btnAdd = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        btnFindPerson = new javax.swing.JButton();
        btnFindRolodex = new javax.swing.JButton();
        scrPnPerson = new javax.swing.JScrollPane();
        //tblPerson = new javax.swing.JTable(); // JM instantiated below

        // JM 8-30-3011 add tool tips to column headers; 2-1-2016 added not employee column
 	  	final String[] columnToolTips = {
 	  			null,
 	  			CoeusToolTip.getToolTip("personName_toolTip.1003"),
 	  			CoeusToolTip.getToolTip("piFlag_toolTip.1003"),
 	  			CoeusToolTip.getToolTip("multiPIFlag_toolTip.1003"),
 	  			CoeusToolTip.getToolTip("facultyFlag_toolTip.1003"),
 	  			CoeusToolTip.getToolTip("effortPct_toolTip.1003"),
 	  			CoeusToolTip.getToolTip("academicEffortPct_toolTip.1003"),
 	  			CoeusToolTip.getToolTip("summerEffortPct_toolTip.1003"),
 	  			CoeusToolTip.getToolTip("calendarEffortPct_toolTip.1003"),
 	  			null, null, null
 	  	};
 	  	
 	  	tblPerson = new JTable(new DefaultTableModel()) {        
 	        //Implement table header tool tips.
 	        protected JTableHeader createDefaultTableHeader() {
 	            return new JTableHeader(columnModel) {
 	                public String getToolTipText(MouseEvent e) {
 	                    java.awt.Point p = e.getPoint();
 	                    int index = columnModel.getColumnIndexAtX(p.x);
 	                    int realIndex = columnModel.getColumn(index).getModelIndex();
 	                    return columnToolTips[realIndex];
 	                }
 	            };
 	        }
 	    };
       
 		tblPerson.setFont(CoeusFontFactory.getLabelFont());
 //END  
 	  	// JM END
        
        scrPnUnit = new javax.swing.JScrollPane();
        tblLead = new javax.swing.JTable();
        btnAddUnit = new javax.swing.JButton();
        btnDelUnit = new javax.swing.JButton();
        btnFindUnit = new javax.swing.JButton();
        btnCertify = new javax.swing.JButton();
        btnCreditSplit = new javax.swing.JButton();

        setLayout(new java.awt.GridBagLayout());
 
        // JM 1-27-2016 set button size
        JButton[] buttons = {btnAddUnit,btnDelUnit,btnFindUnit,btnCertify,btnCreditSplit,
        		btnAdd,btnDelete,btnFindPerson,btnFindRolodex};
        
        for (int i=0; i < buttons.length; i++) {
        	buttons[i].setPreferredSize(new Dimension(190,26));
        }
        // JM END
        
        btnAdd.setFont(new java.awt.Font("Microsoft Sans Serif", 1, 11));
        btnAdd.setMnemonic('A');
        btnAdd.setText("Add");
        btnAdd.setName("btnAdd"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 4, 0);
        add(btnAdd, gridBagConstraints);

        btnDelete.setFont(new java.awt.Font("Microsoft Sans Serif", 1, 11));
        btnDelete.setMnemonic('D');
        btnDelete.setText("Delete");
        btnDelete.setName("btnDelete"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 4, 0);
        add(btnDelete, gridBagConstraints);

        btnFindPerson.setFont(new java.awt.Font("Microsoft Sans Serif", 1, 11));
        btnFindPerson.setMnemonic('P');
        // JM 11-14-2012 updated button label
        //btnFindPerson.setText("Find Person");
        btnFindPerson.setText(FIND_EMPLOYEE);
        btnFindPerson.setToolTipText(CoeusToolTip.getToolTip("personBtn_toolTip.1000"));
        // JM END
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2; // JM 4-11-2016 reversed order of employee and non-employee buttons
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 4, 0);
        add(btnFindPerson, gridBagConstraints);

        btnFindRolodex.setFont(new java.awt.Font("Microsoft Sans Serif", 1, 11));
        btnFindRolodex.setMnemonic('R');
        // JM 11-14-2012 updated button label
        //btnFindRolodex.setText("Find Rolodex");
        btnFindRolodex.setText(FIND_NON_EMPLOYEE);
        btnFindRolodex.setToolTipText(CoeusToolTip.getToolTip("rolodexBtn_toolTip.1000"));
        // JM END
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3; // JM 4-11-2016 reversed order of employee and non-employee buttons
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 4, 0);
        add(btnFindRolodex, gridBagConstraints);

        // JM 6-12-2012 changed border
        //scrPnPerson.setBorder(null);
        scrPnPerson.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        // JM END
        scrPnPerson.setMaximumSize(new java.awt.Dimension(445, 500));
        scrPnPerson.setMinimumSize(new java.awt.Dimension(445, 100));
        scrPnPerson.setPreferredSize(new java.awt.Dimension(445, 100));
        scrPnPerson.setViewportView(tblPerson);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 8;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.ipadx = 400;
        gridBagConstraints.ipady = 75;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 0, 5);
        add(scrPnPerson, gridBagConstraints);

        // JM 6-12-2012 changed border
        //scrPnUnit.setBorder(null);
        scrPnUnit.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        // JM END
        scrPnUnit.setMaximumSize(new java.awt.Dimension(200, 100));
        scrPnUnit.setMinimumSize(new java.awt.Dimension(200, 100));
        scrPnUnit.setPreferredSize(new java.awt.Dimension(200, 100));
        scrPnUnit.setViewportView(tblLead);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 400;
        gridBagConstraints.ipady = 75;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(67, 3, 2, 4);
        add(scrPnUnit, gridBagConstraints);

// JM 8-30-2011 panel for page usage directions
	    CoeusHelpGidget gidgetUsage = new CoeusHelpGidget("effortCalc_helpCode.1003");
	    JScrollPane scrPnUsage = new JScrollPane(gidgetUsage.createHelpScrollArea());
	    scrPnUsage.setMaximumSize(new java.awt.Dimension(830, 250));
	    scrPnUsage.setMinimumSize(new java.awt.Dimension(830, 250));
	    scrPnUsage.setPreferredSize(new java.awt.Dimension(830, 250));
	
	    JPanel pnlUsage = gidgetUsage.createHelpPanel();
	    pnlUsage.add(scrPnUsage, java.awt.BorderLayout.CENTER);
	     
	    gridBagConstraints = new java.awt.GridBagConstraints();
	    gridBagConstraints.gridx = 0;
	    gridBagConstraints.gridy = 20;
	    gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
	    gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTH;
	    gridBagConstraints.insets = new java.awt.Insets(50, 2, 2, 2); //JM 50 was 126
	    add(pnlUsage, gridBagConstraints);
//END

        btnAddUnit.setFont(new java.awt.Font("Microsoft Sans Serif", 1, 11)); // NOI18N
        btnAddUnit.setMnemonic('n');
        btnAddUnit.setText("Add Unit");
        btnAddUnit.setName("btnAddUnit"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        //gridBagConstraints.insets = new java.awt.Insets(68, 5, 4, 5);
        gridBagConstraints.insets = new java.awt.Insets(68, 0, 4, 0);
        add(btnAddUnit, gridBagConstraints);

        btnDelUnit.setFont(new java.awt.Font("Microsoft Sans Serif", 1, 11));
        btnDelUnit.setMnemonic('l');
        btnDelUnit.setText("Delete Unit");
        btnDelUnit.setName("btnDelUnit"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 4, 0);
        add(btnDelUnit, gridBagConstraints);

        btnFindUnit.setFont(new java.awt.Font("Microsoft Sans Serif", 1, 11));
        btnFindUnit.setMnemonic('u');
        btnFindUnit.setText("Find Unit");
        btnFindUnit.setName("btnFindUnit"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 4, 0);
        add(btnFindUnit, gridBagConstraints);

        btnCertify.setFont(new java.awt.Font("Microsoft Sans Serif", 1, 11));
        btnCertify.setMnemonic('C');
        btnCertify.setText("Certify");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 4, 0);
        add(btnCertify, gridBagConstraints);

        btnCreditSplit.setFont(CoeusFontFactory.getLabelFont());
        btnCreditSplit.setMnemonic('t');
        btnCreditSplit.setText("Credit Split");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 4, 0);
        add(btnCreditSplit, gridBagConstraints);
    }// </editor-fold>

    private void stopAllEditing( int selectedRow ) {
        if(tblPerson.isEditing() ){
            //Modified for Coeus 4.3 -PT ID:2229 Multi PI - start
            if( tblPerson.getEditingColumn() == NAME_COLUMN ){
                String value = null;
                value = ((javax.swing.text.JTextComponent)
                tblPerson.getEditorComponent()).getText();
                if( value != null){
                    tblPerson.setValueAt(value,selectedRow,NAME_COLUMN);
                }
                tblPerson.getCellEditor().cancelCellEditing();
            }
            //Commented for Coeus 4.3-PT ID:2270 Tracking Effort - start
//            else if( tblPerson.getEditingColumn() == EFFORT_COLUMN ) {
//                if( selectedRow!= -1 && tblPerson.getCellEditor(selectedRow, EFFORT_COLUMN) != null ) {
//                    tblPerson.getCellEditor(selectedRow, EFFORT_COLUMN).stopCellEditing();
//                }
//
//            }
            //Commented for Coeus 4.3-PT ID:2270 Tracking Effort - end

            //Added for Coeus 4.3-PT ID:2270 Tracking Effort- start
            else if( tblPerson.getEditingColumn() == EFFORT_COLUMN ||
                    tblPerson.getEditingColumn() == ACADEMIC_YEAR_COLUMN ||
                    tblPerson.getEditingColumn() == CALENDAR_YEAR_COLUMN ||
                    tblPerson.getEditingColumn() == SUMMER_EFFORT_COLUMN) {
                if( selectedRow!= -1 && tblPerson.getCellEditor(selectedRow, tblPerson.getEditingColumn()) != null ) {
                    tblPerson.getCellEditor(selectedRow, tblPerson.getEditingColumn()).stopCellEditing();
                }
            }
            //Added for Coeus 4.3-PT ID:2270 Tracking Effort- end
            //Modified for Coeus 4.3 -PT ID:2229 Multi PI - start
        }

        if( tblLead.isEditing() ){
            tblLead.getCellEditor().stopCellEditing();
        }

    }
    public void actionPerformed(java.awt.event.ActionEvent actionEvent) {

        Object actionSource = actionEvent.getSource();
        int selectedRow = tblPerson.getSelectedRow();

        stopAllEditing( selectedRow );
        if( tblLead.isEditing() ) {
            tblLead.getCellEditor().stopCellEditing();
        }
        if( actionSource.equals( btnFindPerson ) ) {
            performFindPerson();
        }else if( actionSource.equals( btnFindRolodex ) ) {
            performFindRolodex();
        }else if( actionSource.equals( btnFindUnit ) ) {
            performFindUnit();
        }else if( actionSource.equals( btnAdd ) ) {
            performAdd();
        }else if( actionSource.equals( btnAddUnit ) ) {
            performAddUnit();
        }else if( actionSource.equals( btnDelete ) ) {
            performDelete();
        }else if( actionSource.equals( btnDelUnit ) ) {
            performDeleteUnit();
        }else if ( actionSource.equals( btnCertify ) ) {

      //checking parameter S T A R T
        try
            {

            parameterExist=fetchParameterValue();
            //COEUSQA-3956 Start
            if(parameterExist){
                if(ISNeedToShowYNQWhenPPCEnabled(proposalId)){
                    parameterExist = false;
                }
            }          
           //COEUSQA-3956 End            
           if(parameterExist){
             viewDeptRightExistForAggre=getViewCertificationRight();
            rightExist=getMaintainPersonCertificationRight();
            if(!rightExist){
                deptRightExist=getMaintainDepartmentPersonCertificationRight();
            }else if(!rightExist && !deptRightExist){
                viewDeptRightExist=getViewDepartmentPersonCertificationRight();
            }
            if(rightExist||deptRightExist||viewDeptRightExist||viewDeptRightExistForAggre){

                  if (tblPerson.getSelectedRow() != -1){
               //send URL TO BROWSER code method
                      openUrlCertify();
                  }
                }
//Added for COEUSDEV-736:
//This code is added to disable  Certify button  if the proposal is a child proposal.
               if(inHierarchy){
              boolean isParent = isParent();
              if(isParent){
                  if (tblPerson.getSelectedRow() != -1){
                      openUrlCertify();
                  }}
              else{certifyEnabled=false;}
               }
//Added for COEUSDEV-736: to disable Certify button end.
            }
            else{
                if (tblPerson.getSelectedRow() != -1){
                    performCertify();
                }
            }
        }
            catch(Exception ex)
            {
                ex.printStackTrace();
            }
   //checking parameter  E N D

        }
        //Case 2106 Start 3
        else if( actionSource.equals( btnCreditSplit ) ){
            if(isSaveRequired()){
                MessageFormat formatter = new MessageFormat("");
                String message = formatter.format(
                        coeusMessageResources.parseMessageKey(SAVE_BEFORE_OPEN_CREDIT_SPILT),
                        "proposal");
                CoeusOptionPane.showInfoDialog(message);
            }else{
                performCreditSplitAction();
            }
        }
        //Case 2106 End 3
    }

    //supporting method to show the warning message
    private void showWarningMessage( String str ){
        CoeusOptionPane.showWarningDialog(str);
    }
    /* supporting method used for person search */
    private void performFindPerson(){
        try{
            CoeusSearch proposalSearch = null;
//            int selectedRow = tblPerson.getSelectedRow();
            proposalSearch = new CoeusSearch( mdiForm, "PERSONSEARCH",
                    CoeusSearch.TWO_TABS_WITH_MULTIPLE_SELECTION ); //TWO_TABS ) ;
            proposalSearch.showSearchWindow();
            Vector vSelectedPersons = proposalSearch.getMultipleSelectedRows();
            if ( vSelectedPersons != null ){
                HashMap singlePersonData = null;
                for(int indx = 0; indx < vSelectedPersons.size(); indx++ ){

                    singlePersonData = (HashMap)vSelectedPersons.get( indx ) ;

                    if( singlePersonData == null || singlePersonData.isEmpty() ){
                        continue;
                    }
                    String personId = checkForNull(singlePersonData.get( "PERSON_ID" ));//result.get( "PERSON_ID" )) ;
                    String personName = checkForNull(singlePersonData.get( "FULL_NAME" ));//result.get( "FULL_NAME" )) ;
                    String homeUnit = checkForNull( singlePersonData.get( "HOME_UNIT" ));//result.get( "HOME_UNIT" ));

                    boolean faculty
                            = checkForNull(singlePersonData.get("IS_FACULTY")).
                            equalsIgnoreCase("y") ? true : false;

                    ProposalInvestigatorFormBean investigator =
                            new ProposalInvestigatorFormBean();
                    investigator.setPersonId(personId);
                    investigator.setPersonName(personName);
                    investigator.setPercentageEffort( new Float(0).floatValue() );
                    investigator.setPrincipleInvestigatorFlag(false);

                    // Added for Case# 2229 2229
                    investigator.setMultiPIFlag(false);

                    investigator.setFacultyFlag(faculty);
                    investigator.setNonMITPersonFlag(false);
                     //added for ppc certify flag..starts
                     investigator.setCertifyFlag(false);
                      //added for ppc certify flag..ends
                    investigator.setAcType( INSERT_RECORD );
                /* send the bean and investigator home unit number so that the
                  method checks for dupliate person name and adds the person to
                  investigator table as well as to investigator hashtable. If
                  homeUnit number is specified then it retrevies corresponding
                  unit details and updates unit table along with unit hashtable.
                  Send false to replaceRow as we have to create a new row for
                  each investigator entry.
                 */
                    setInvestigatorBean(investigator,homeUnit,false);
                } //end of for loop
            }//end of vSelectedPersons != null
        }catch( Exception err ){
            err.printStackTrace();
        }
    }

     /* supporting method used to set the investigator bean into hashtable and
       update the investigator table. If homeUnit has been specified then
       this method will update unit table and makes an entry in unit hashtable.
       If replace row is true it will update the selected row otherwise adds a
       new row to the table.
      */
    private void setInvestigatorBean(ProposalInvestigatorFormBean investigator,
            String homeUnit, boolean replaceRow){

        //Bug Fix : 1222 - STEP 4 - START
        //int selectedRow = tblPerson.getSelectedRow();

        int selectedRow = nmEdtName.getEditedRow();
        //Bug Fix : 1222 - STEP 4 - END

        String oldPersonId = "";
        boolean primaryInv = false;

        //Bug fix: 1233 Since error was occuring due to bug fix 1222
        /*if(selectedRow != -1)*/
        /*
         *  Fix for #2116 by Geo on 11/01/2006
         *
         */
//        if(selectedRow > 0){
        // Case#2629:4.2 Premium - Investigator tab - Start
        // Changed the Order of If-Else If Condition.
        if(tblPerson.getRowCount() == 0){
            investigator.setPrincipleInvestigatorFlag(true);
        }else if(selectedRow >= 0){
            //Modified for Coeus 4.3 -PT ID:2229 Multi PI - start
            oldPersonId = (String)tblPerson.getValueAt(selectedRow,PERSON_ID_COLUMN);

            if( oldPersonId == null  || oldPersonId.trim().length() == 0 ){
                replaceRow = true;
//                primaryInv = ((Boolean)tblPerson.getValueAt(selectedRow
//                        ,3)).booleanValue();
                primaryInv = ((Boolean)tblPerson.getValueAt(selectedRow
                        ,PI_COLUMN)).booleanValue();
                //Modified for Coeus 4.3 -PT ID:2229 Multi PI - end
                investigator.setPrincipleInvestigatorFlag(primaryInv );
            }
        }
        // Case#2629:4.2 Premium - Investigator tab - End
        String personId = investigator.getPersonId();
        String personName = investigator.getPersonName();
        boolean pi = investigator.isPrincipleInvestigatorFlag();

        // Added for Case# 2229
        boolean multi_PI = investigator.isMultiPIFlag();

        boolean faculty = investigator.isFacultyFlag();
        boolean nonEmp = investigator.isNonMITPersonFlag();
        float effortPercent = investigator.getPercentageEffort();

        if(!personId.equals(oldPersonId)){
            // check for duplicate person entry
            if(! checkDuplicatePerson( personId ,selectedRow)){
                saveRequired = true;
                if( persons == null ) {
                    persons = new Vector();
                }
                persons.addElement( personId );
                detailForm.addPropPerson( personId , !nonEmp );
                investigatorData.put(personId,investigator);
                if( replaceRow ){
                    if(oldPersonId != null && oldPersonId.trim().length()>0){
                           /* if not duplicate entry and any old entry present in
                             selected row, delete hashtable entry with old person id
                             in investigator hashtable and also frm unit hashtable
                            */
                        investigatorData.remove(oldPersonId);
                        if(unitHashData.containsKey(oldPersonId)){
                            unitHashData.remove(oldPersonId);
                        }
                    }
                    // update the investigator row if any row is selected
                    //Modified for Coeus 4.3 -PT ID:2229 Multi PI - start
                    tblPerson.setValueAt( personName, selectedRow, NAME_COLUMN );
                    tblPerson.setValueAt( new Boolean(pi),selectedRow,PI_COLUMN);               //Handle PI
                    tblPerson.setValueAt(new Boolean(multi_PI),selectedRow,MULTI_PI_COLUMN);
                    tblPerson.setValueAt( new Boolean(faculty),selectedRow,FACULTY_COLUMN);    //Handle faculty
                    if(effortPercent == 0.0){
                        tblPerson.setValueAt( ".00", selectedRow, EFFORT_COLUMN );
                    }else{
                        tblPerson.setValueAt( new Float( effortPercent), selectedRow, EFFORT_COLUMN);  //Handle Effort
                    }
                    tblPerson.setValueAt( personId,selectedRow,PERSON_ID_COLUMN);
                    tblPerson.setValueAt( new Boolean(nonEmp),selectedRow,NONEMP_COLUMN);
                    //added for ppc certify flag..starts
                    tblPerson.setValueAt( personId,selectedRow,CERTIFY_COLUMN);
                    //added for ppc certify flag..ends
                    //Modified for Coeus 4.3 -PT ID:2229 Multi PI - end
                }else{
                    // otherwise add new entry
                    Vector newData = new Vector();
                    newData.add( "" );
                    newData.add( personName );
                    //Commented for Coeus 4.3 -PT ID:2229 Multi PI - start
                    //newData.add( ".00" );                          //Handle Effort
                    //Commented for Coeus 4.3 -PT ID:2229 Multi PI - end
                    if(tblPerson.getRowCount() == 0){
                        newData.add( new Boolean( true ) );
                    }else{
                        newData.add( new Boolean( false ) );
                    }
                    newData.add(new Boolean(false));
                    newData.add( new Boolean( faculty ) );
                    //Added for Coeus 4.3 -PT ID:2229 Multi PI - start
                    newData.add( ".00" );                          //Handle Effort
                    //Added for Coeus 4.3 -PT ID:2229 Multi PI - end
                    //Added for Coeus 4.3-PT ID:2270 Tracking Effort- start
                    newData.add( ".00" );
                    newData.add( ".00" );
                    newData.add( ".00" );
                    //Added for Coeus 4.3-PT ID:2270 Tracking Effort- end
                    newData.add( personId );
                    newData.add( new Boolean(nonEmp) );

                    ((DefaultTableModel)tblPerson.getModel()).addRow(
                            newData );
                    setTableEditors();
                    int  newRowCount = tblPerson.getRowCount();
                    tblPerson.scrollRectToVisible(
                            tblPerson.getCellRect(newRowCount-1 ,0, true));

                    tblPerson.setRowSelectionInterval(newRowCount - 1,
                            newRowCount - 1);
                }

                if(homeUnit != null && homeUnit.trim().length()>0){
                        /* if investigator have home unit add corresponding unit
                          details to the existing unit information for that
                          particular investigator by checking for duplicate entry */
                    UnitDetailFormBean unitDetail
                            = getUnitInfoBean( homeUnit );
                    if( (unitDetail != null)
                    && (unitDetail.getUnitNumber() != null)){
                        if( !isDuplicateUnitID( personId,
                                unitDetail.getUnitNumber() )){
                            ProposalLeadUnitFormBean unitBean
                                    = new ProposalLeadUnitFormBean();

                            unitBean.setUnitNumber(
                                    unitDetail.getUnitNumber());
                            unitBean.setUnitName(unitDetail.getUnitName());
                            unitBean.setPersonId(personId);
                            unitBean.setLeadUnitFlag(false);
                            unitBean.setAcType( INSERT_RECORD );
                            addToHashtable(personId,unitBean);
                            ((DefaultTableModel)tblLead.getModel()).
                                    setDataVector(
                                    getUnitTableData( personId ),
                                    getUnitTableColumnNames());
                            if(tblLead.getRowCount()>0){
                                tblLead.setRowSelectionInterval(0,0);
                                if(isParent()){
                                    btnDelUnit.setEnabled(false);
                                }else{
                                    btnDelUnit.setEnabled(true);
                                }

                            }
                            setTableEditors();
                        }
                    }
                }
            }else{
                showWarningMessage("' " + personName + " ' " +
                        coeusMessageResources.parseMessageKey(
                        "general_duplicateNameCode.2277"));
            }
        }
        //Commented for Bug Fix:1222 - STEP 7 - Start
        /*else{
            showWarningMessage("' " + personName + " ' " +
            coeusMessageResources.parseMessageKey(
            "general_duplicateNameCode.2277"));
        }*/
        //Bug Fix:1222 - STEP 7 - END

        if( tblPerson.getRowCount() > 0 ){
            btnDelete.setEnabled( true );
            if(certifyEnabled){
                btnCertify.setEnabled( true );
            }else{
                btnCertify.setEnabled( false );
            }
            btnAddUnit.setEnabled( true );
            btnFindUnit.setEnabled( true );
            //Case 2106 Start 6
            btnCreditSplit.setEnabled(true);
            //Case 2106 End 6
        }
    }


    //supporting method to check the duplicates
    private boolean checkDuplicatePerson(String personId, int selectedRow){
        boolean duplicate = false;
        String oldId = "";
        for(int rowIndex = 0; rowIndex < tblPerson.getRowCount();
        rowIndex++){
            if(rowIndex != selectedRow){
                //Modified for Coeus 4.3-PT ID:2270 Tracking Effort- start
                //oldId = (String)tblPerson.getValueAt(rowIndex,5);
                oldId = (String)tblPerson.getValueAt(rowIndex,PERSON_ID_COLUMN);
                //Modified for Coeus 4.3-PT ID:2270 Tracking Effort- end
                if(oldId != null){
                    if(oldId.equals(personId)){
                        duplicate = true;
                        break;
                    }
                }
            }
        }
        return duplicate;
    }


    //supporting method to check the unitID already exists
    private boolean isDuplicateUnitID( String personId, String unitID ){

        boolean isDuplicateUnit = false;
        Vector units = (Vector) unitHashData.get( personId);
        if( units != null && (unitID != null) && (unitID.trim().length()>0)){
            ProposalLeadUnitFormBean curBean = null;
            for( int indx = 0 ; indx < units.size(); indx ++ ){
                curBean = (ProposalLeadUnitFormBean) units.get( indx );
                if( (curBean.getUnitNumber().equalsIgnoreCase( unitID ))){
                    isDuplicateUnit = true;
                }
            }
        }
        return isDuplicateUnit;
    }

        /* Supporting method used to add unit bean to the unit hashtable for a
       specified investigator */
    private void addToHashtable(String selPersonId,
            ProposalLeadUnitFormBean unitsBean){

        ProposalLeadUnitFormBean unitBean = null;
//        int selectedRow = tblLead.getSelectedRow();
        boolean   found = false;
        Vector units = (Vector)unitHashData.get(selPersonId);
        if (units != null ) {
            String unitId = unitsBean.getUnitNumber();
            for (int count = 0 ;count < units.size() ; count++){
                unitBean = (ProposalLeadUnitFormBean)units.get(count);

                if (unitId.equalsIgnoreCase(unitBean.getUnitNumber())) {

                    unitBean.setUnitNumber(unitsBean.getUnitNumber());
                    unitBean.setUnitName(unitsBean.getUnitName());
                    unitBean.setLeadUnitFlag(unitsBean.isLeadUnitFlag());
                    unitBean.setPersonId(selPersonId);
                    /*unitBean.setPersonName(unitsBean.getPersonName());*/
                    units.set(count,unitBean);
                    found = true;
                    break;
                }
            }
        }else{
            units = new Vector();
        }
        if(!found) {
            units.add(unitsBean);
        }
        unitHashData.put(selPersonId,units);
    }

    //supporting method to get PersonInfoBean for specific person which will
    //validate the person name against the db value
   /* private PersonInfoFormBean getPersonInfoID( String name ){
        boolean success=false;
        String personID=null;
        RequesterBean requester = new RequesterBean();
        requester.setDataObject("GET_PERSONINFO");
        requester.setId(name);
        String connectTo = connectionURL + "/coeusFunctionsServlet";
        AppletServletCommunicator comm
                = new AppletServletCommunicator(connectTo, requester);
        comm.send();
        ResponderBean response = comm.getResponse();
        PersonInfoFormBean personInfoFormBean = null;
        if ( response!=null ){
            success=true;
            personInfoFormBean = (PersonInfoFormBean) response.getDataObject();
        }

        return personInfoFormBean;
    }*/

    //supporting method to get UnitDetailFormBean for specific unit number
    //which will be used to validate the unit id/name against the

    private UnitDetailFormBean getUnitInfoBean( String unitNumber ){
//        boolean success=false;
//        String personID=null;
        RequesterBean requester = new RequesterBean();
        requester.setFunctionType('G');
        requester.setId( unitNumber );
        String connectTo = connectionURL + "/unitServlet";
        AppletServletCommunicator comm
                = new AppletServletCommunicator(connectTo, requester);
        comm.send();
        ResponderBean response = comm.getResponse();
        UnitDetailFormBean unitInfoBean = null;
        if ( response!=null ){
//            success=true;
            unitInfoBean = (UnitDetailFormBean) response.getDataObject();
        }

        if( unitInfoBean == null || unitInfoBean.getUnitNumber() == null){
            unitInfoBean = null;
            CoeusOptionPane.showWarningDialog(
                    coeusMessageResources.parseMessageKey(
                    "protoInvFrm_exceptionCode.1138"));
        }
        return unitInfoBean;
    }

    /** Getter for property leadUnitNumber.
     * @return Value of property leadUnitNumber.
     */

    public java.lang.String getLeadUnitNumber() {
        return ( leadUnitNumber == null ? "" : leadUnitNumber ) ;
    }

    /* supported method to perform rolodex search */
    private void performFindRolodex(){
        try{
            CoeusSearch proposalSearch = null;
            proposalSearch = new CoeusSearch( mdiForm,
                    CoeusGuiConstants.ROLODEX_SEARCH,
                    CoeusSearch.TWO_TABS_WITH_MULTIPLE_SELECTION ) ;
            //CoeusSearch.TWO_TABS ) ;
            proposalSearch.showSearchWindow();
            Vector vSelectedRolodex = proposalSearch.getMultipleSelectedRows();
            if ( vSelectedRolodex != null ){

                HashMap singleRolodexData = null;
                for(int indx = 0; indx < vSelectedRolodex.size(); indx++ ){

                    singleRolodexData = (HashMap)vSelectedRolodex.get( indx ) ;

                    if( singleRolodexData == null || singleRolodexData.isEmpty() ){
                        continue;
                    }

                    String rolodexID = checkForNull(singleRolodexData.get( "ROLODEX_ID" ));
                    String firstName = checkForNull(singleRolodexData.get( "FIRST_NAME" ));
                    String lastName = checkForNull(singleRolodexData.get( "LAST_NAME" ));
                    String middleName = checkForNull(singleRolodexData.get( "MIDDLE_NAME" ));
                    String namePreffix = checkForNull(singleRolodexData.get( "PREFIX" ));
                    String nameSuffix = checkForNull(singleRolodexData.get( "SUFFIX" ));
                    String rolodexName = null;
            /* construct full name of the rolodex if his last name is present
              otherwise use his organization name to display in person name
              column of investigator table */
                    if ( lastName.length() > 0) {
                        rolodexName = ( lastName + " "+nameSuffix +", "+ namePreffix
                                +" "+firstName +" "+ middleName ).trim();
                    } else {
                        rolodexName = checkForNull(singleRolodexData.get("ORGANIZATION"));
                    }
                    ProposalInvestigatorFormBean investigator =
                            new ProposalInvestigatorFormBean();
                    investigator.setPersonId(rolodexID);
                    investigator.setPersonName(rolodexName);
                    investigator.setPercentageEffort(0.0f);
                    investigator.setPrincipleInvestigatorFlag(false);

                    // Added for Case# 2229
                    investigator.setMultiPIFlag(false);

                    investigator.setFacultyFlag(false);
                    // as rolodex is not employee so set non-employee flag true
                    investigator.setNonMITPersonFlag(true);
                    //added for ppc certify flag..starts
                    investigator.setCertifyFlag(false);
                    //added for ppc certify flag..ends
                    investigator.setAcType( INSERT_RECORD );
            /* as rolodex doesn't have any unit information by default, send
              null for homeUnit parameter. Send false to replaceRow as we
              have to create a new row for each investigator entry. */
                    setInvestigatorBean(investigator,null,false);
                } //end of for loop
            }//end of vSelectedRolodex != null
        }catch( Exception err ){
            err.printStackTrace();
        }

    }
    /* supporting method to call Certify form */
    private void performCertify() {

        int selectedRow = tblPerson.getSelectedRow();
        //Modified for Coeus 4.3 -PT ID:2229 Multi PI - start
        String personId = (String)tblPerson.getValueAt(selectedRow,PERSON_ID_COLUMN);
        String personName = (String)tblPerson.getValueAt(selectedRow,NAME_COLUMN);
        //Modified for Coeus 4.3 -PT ID:2229 Multi PI - end
        if(personId != null && personId.trim().length()>0){
            if( detailForm.isProposalPerson( personId ) ){
                ProposalInvestigatorFormBean investigator =
                        (ProposalInvestigatorFormBean) investigatorData.get(personId);
                Vector data = new Vector();
                if (this.getSponsorCode() != null) {
                    data.add(this.getSponsorCode()+":"+this.getSponsorName());
                }else{
                    data.add("");
                }
                data.add(investigator.getProposalNumber());
                data.add(personName);
                data.add(personId);
                certifyForm = new ProposalCertifyForm(functionType, questions,
                        moreExplanations,data,investigator, proposalId);

//                Vector vAnswers = investigator.getInvestigatorAnswers();
            }else{
                CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(
                        "proposal_InvForm_exceptionCode.7000" ) );
            }
        }else{
            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(
                    "proposal_InvForm_exceptionCode.7000" ) );
        }
    }
    /*send to browser*/
    private void openUrlCertify() throws IOException, Exception
    {
        /*commented is in the case of selected user*/
//    int selectedRow = tblPerson.getSelectedRow();
//    String personId = (String)tblPerson.getValueAt(selectedRow,PERSON_ID_COLUMN);
        /*this is in the case of login user*/
    String personId =(String)mdiForm.getUserId();
    String ProposalNum= proposalId;
    String urlSend=null;
    String baseUrl = getAppHomeUrl();
    StringBuffer url =null;
                            url=new StringBuffer();
                            url.append(baseUrl);
                            url.append("rightPersonCertify.do?proposalNumber=");
                            url.append(ProposalNum+"&id_person="+personId);
                            urlSend=url.toString();
        try{
            URLOpener.openUrl(urlSend);
            }catch(Exception ex){
                CoeusOptionPane.showErrorDialog(ex.getMessage());
            }

    }

    /* supporting method to perform unit search */
    private void performFindUnit(){
        try{
            int selectedRow = tblPerson.getSelectedRow();
            int selectedUnitRow = tblLead.getSelectedRow();
            CoeusSearch proposalSearch = null;
            String unitID = null;
            // check whether any investigator has been selected or not
            if(selectedRow != -1){
                //Modified for Coeus 4.3-PT ID:2270 Tracking Effort- start
                //String pId =  (String)tblPerson.getValueAt(selectedRow,5);
                String pId =  (String)tblPerson.getValueAt(selectedRow,PERSON_ID_COLUMN);
                //Modified for Coeus 4.3-PT ID:2270 Tracking Effort- end
                // check whether investigator specified in selected row
                if(pId != null && pId.trim().length()>0){
                    /* cancel unit number colum editing when find unit
                      button is clicked */
                    if((tblLead.isEditing())
                    && (tblLead.getEditingColumn() == 2 )){
                        String value = null;
                        value = ((javax.swing.text.JTextComponent)
                        tblLead.getEditorComponent()).getText();
                        if( (value != null)){
                            tblLead.setValueAt(value,selectedUnitRow,2);
                        }
                        tblLead.getCellEditor().cancelCellEditing();
                    }

                    proposalSearch = new CoeusSearch( mdiForm,
                            "LEADUNITSEARCH",
                            CoeusSearch.TWO_TABS_WITH_MULTIPLE_SELECTION ) ;
                    //                  CoeusSearch.TWO_TABS ) ;
                    proposalSearch.showSearchWindow();

                    Vector vSelectedUnit = proposalSearch.getMultipleSelectedRows();

                    if ( vSelectedUnit != null ){

                        HashMap singleLeadUnitData = null;
                        for(int indx = 0; indx < vSelectedUnit.size(); indx++ ){

                            singleLeadUnitData = (HashMap)vSelectedUnit.get( indx ) ;

                            if( singleLeadUnitData == null || singleLeadUnitData.isEmpty() ){
                                continue;
                            }

                            unitID = checkForNull(singleLeadUnitData.get( "UNIT_NUMBER" ));
                            String unitName = checkForNull(singleLeadUnitData.get( "UNIT_NAME" ));
                            String adminPerson = checkForNull(singleLeadUnitData.get( "ADMINTR" ));

//                            String selPersonName
//                                    = tblPerson.getValueAt( selectedRow, 2).toString();
                    /* check for duplicate unit number for a particular
                      investigator */
                            if( !isDuplicateUnitID( pId, unitID )) {
                                saveRequired = true;
                        /* if not duplicate construct unit bean and add to
                          unit hashtable */
                                ProposalLeadUnitFormBean unitBean =
                                        new ProposalLeadUnitFormBean();
                                unitBean.setPersonId(pId);
                                unitBean.setPersonName(adminPerson);
                                unitBean.setUnitNumber(unitID);
                                unitBean.setUnitName(unitName);
                                unitBean.setLeadUnitFlag(false);
                                unitBean.setAcType( INSERT_RECORD );
                                addToHashtable(pId,unitBean);

                                if( selectedUnitRow != -1 ){
                                    // if any unit table row is selected update that
                                    //  row
                                    String oldUnitNumber
                                            = (String)tblLead.getValueAt(selectedUnitRow,2);
                                    if(oldUnitNumber == null
                                            || oldUnitNumber.trim().length() == 0 ){
                                        tblLead.setValueAt( unitID ,
                                                selectedUnitRow, 2 );
                                        tblLead.setValueAt( unitName ,
                                                selectedUnitRow, 3 );
                                    }else{
                                        // otherwise add a new row
                                        Vector newData = new Vector();
                                        newData.add( "");
                                        newData.add( new Boolean( false ) );
                                        newData.add( unitID );
                                        newData.add( unitName );
                                        ((DefaultTableModel)tblLead.getModel()).addRow(
                                                newData );
                                        setTableEditors();
                                        int lastRow = tblLead.getRowCount() -1 ;
                                        tblLead.scrollRectToVisible(
                                                tblLead.getCellRect(lastRow ,0, true));
                                        tblLead.setRowSelectionInterval(lastRow,
                                                lastRow);
                                    }
                                }else{
                                    // otherwise add a new row
                                    Vector newData = new Vector();
                                    newData.add( "");
                                    newData.add( new Boolean( false ) );
                                    newData.add( unitID );
                                    newData.add( unitName );
                                    ((DefaultTableModel)tblLead.getModel()).addRow(
                                            newData );
                                    setTableEditors();
                                    int lastRow = tblLead.getRowCount() -1 ;
                                    tblLead.scrollRectToVisible(
                                            tblLead.getCellRect(lastRow ,0, true));
                                    tblLead.setRowSelectionInterval(lastRow,
                                            lastRow);

                                }
                            }else{
                                showWarningMessage( "' " + unitID + " ' " +
                                        coeusMessageResources.parseMessageKey(
                                        "protoInvFrm_exceptionCode.1137"));
                            }
                        }// end of for
                    } // end of vSelectedUnit != null
                    if( tblLead.getRowCount() > 0 ){
                        /* enable delete unit button if any unit information
                         is present for selected investigator */
                        btnDelUnit.setEnabled( true );
                    }
                }else{
                    showWarningMessage(
                            coeusMessageResources.parseMessageKey(
                            "protoInvFrm_exceptionCode.1066") );
                }

            }else{
                showWarningMessage(
                        coeusMessageResources.parseMessageKey(
                        "protoInvFrm_exceptionCode.1066") );
            }

        }catch( Exception err ){
            err.printStackTrace();
        }

    }
    /* supporting method to add a blank row to the investigator table */
    private void performAdd(){
        // add a blank row to investigator table
        Vector newData = new Vector();
        int rowCount = tblPerson.getRowCount() ;
        newData.add("");
        newData.add("");
        //Modified for Coeus 4.3-PT ID:2270 Tracking Effort- start
        if(rowCount == 0){
                /* if the row being added is the first row select this row
                  as PI */
            newData.add( new Boolean( true ) );
        }else{
            newData.add( new Boolean( false ) );
        }
        newData.add(new Boolean(false));
        newData.add( new Boolean( false ) );
        newData.add(".00");
        newData.add(".00");
        newData.add(".00");
        newData.add(".00");
        //Modified for Coeus 4.3-PT ID:2270 Tracking Effort- end
        newData.add("");
        newData.add( new Boolean( false ) );
        //added for ppc certify flag..starts
        newData.add( new Boolean( false ) );
        //added for ppc certify flag..ends
        // add empty data to unit table
        ((DefaultTableModel)tblPerson.getModel()).addRow( newData );
        tblPerson.setRowSelectionInterval(rowCount,rowCount);
        ((DefaultTableModel)tblLead.getModel()).setDataVector(
                new Object[][]{}, getUnitTableColumnNames().toArray());

        //Added by Amit 11/20/2003 for IRB_DEF_Gen_6
        tblPerson.requestFocusInWindow();
        //Modified for Coeus 4.3 -PT ID:2229 Multi PI - start
        tblPerson.editCellAt(rowCount,NAME_COLUMN);
        //Modified for Coeus 4.3 -PT ID:2229 Multi PI - end
        tblPerson.getEditorComponent().requestFocusInWindow();
        //End Amit

        setTableEditors();
        rowCount = tblPerson.getRowCount() -1 ;
        tblPerson.scrollRectToVisible(tblPerson.getCellRect(rowCount ,
                0, true));
        btnDelete.setEnabled( true );
        saveRequired = true;
        if(certifyEnabled){
            btnCertify.setEnabled( true );
        }else{
            btnCertify.setEnabled( false );
        }
    }
    /* supporting method to add blank row the units table */
    private void performAddUnit(){
        int selectedRow = tblPerson.getSelectedRow();
        int rowCount = tblLead.getRowCount();
        if( selectedRow != -1 ){
            if(tblLead.isEditing() && tblLead.getEditingColumn() == 2){
                String value = null;
                value = ((javax.swing.text.JTextComponent)
                tblLead.getEditorComponent()).getText();
                if( (value != null)){
                    tblLead.setValueAt(value,selectedRow,2);
                }
                tblLead.getCellEditor().cancelCellEditing();
            }
            //Modified for Coeus 4.3-PT ID:2270 Tracking Effort- start
            //String pId =  (String)tblPerson.getValueAt(selectedRow,5);
            String pId =  (String)tblPerson.getValueAt(selectedRow,PERSON_ID_COLUMN);
            //Modified for Coeus 4.3-PT ID:2270 Tracking Effort- end
            if(pId != null && pId.trim().length()>0){
                /* if investigator data present for selected row in
                  investigator table then add blank row to unit table */

                Vector newData = new Vector();
                newData.add( "");
                newData.add( new Boolean( false ) );
                newData.add( "" );
                newData.add( "" );

                ((DefaultTableModel)tblLead.getModel()).addRow( newData );
                rowCount = tblLead.getRowCount() -1 ;
                setTableEditors();
                tblLead.scrollRectToVisible(
                        tblLead.getCellRect(rowCount ,0, true));

                tblLead.setRowSelectionInterval(rowCount, rowCount);

                if( tblLead.getRowCount() > 0 ){
                    btnDelUnit.setEnabled( true );
                    btnAddUnit.setEnabled( true );
                    btnFindUnit.setEnabled( true );
                }
            }else{
                showWarningMessage(
                        coeusMessageResources.parseMessageKey(
                        "protoInvFrm_exceptionCode.1066") );
            }
        }else{
            showWarningMessage(
                    coeusMessageResources.parseMessageKey(
                    "protoInvFrm_exceptionCode.1066") );
        }

        //Added by Amit 11/20/2003 for IRB_DEF_Gen_6
        tblLead.requestFocusInWindow();
        tblLead.editCellAt(rowCount,2);
        tblLead.getEditorComponent().requestFocusInWindow();
        //Ens Amit
    }
    /* supporting method to delete a investigator row */
    private void performDelete(){
        // delete investigator row after confirmation
        //Added by Tarique for proposal Hierarchy
        if(inHierarchy){
            int rowCount = tblPerson.getRowCount();
            if(rowCount == 1){
                CoeusOptionPane.showInfoDialog(coeusMessageResources.
                        parseMessageKey("propHierarchy_exceptionCode.1018"));
                return;
            }
        }
        ////Added by Tarique end here
        int selectedRow = tblPerson.getSelectedRow();
        if( selectedRow != -1 ){
            //Modified for Coeus 4.3 -PT ID:2229 Multi PI - start
            String name = (String)tblPerson.getValueAt(selectedRow,NAME_COLUMN);
            //Modified for Coeus 4.3 -PT ID:2229 Multi PI - end
            if(name != null && name.trim().length()>0){
                int selectedOption
                        = showDeleteConfirmMessage(
                        "Are you sure you want to remove "+name+
                        " and associated units?");
                if( selectedOption == JOptionPane.YES_OPTION ){
                    //Added for COEUSQA-2037 : Software allows you to delete an investigator who is assigned credit in the credit split window - Start
                    //When there is only one investigator exists, removes the investigator without check the credit split
                    //When multiple investigator exists, Checks for the investigator credit split information,
                    //until split set to '0' won't allow to delete the investigator
                    String personId = (String)tblPerson.getValueAt(
                            selectedRow,PERSON_ID_COLUMN);
                    if(tblPerson.getRowCount() > 1 && checkInvHasAddsToHundredCreditSplit(personId)){
                          int selectedOpt = CoeusOptionPane.showQuestionDialog(
                                  coeusMessageResources.parseMessageKey(CREDIT_SPLIT_EXISTS_FOR_INV) ,
                                  CoeusOptionPane.OPTION_YES_NO,
                                  CoeusOptionPane.DEFAULT_YES);
                          if(selectedOpt == JOptionPane.YES_OPTION){
                              if(selectedOpt == JOptionPane.YES_OPTION){
                                  if(isSaveRequired()){
                                      MessageFormat formatter = new MessageFormat("");
                                      String message = formatter.format(
                                              coeusMessageResources.parseMessageKey(SAVE_BEFORE_OPEN_CREDIT_SPILT),
                                              "proposal");
                                     CoeusOptionPane.showInfoDialog(message);
                                  }else{
                                      performCreditSplitAction();
                                  }
                              }

                          }
                    }else{
                        //COEUSQA-2037 : End
                        //Modified for Coeus 4.3-PT ID:2270 Tracking Effort- start
//                    String personId = (String)tblPerson.getValueAt(
//                            selectedRow,5);
                        //Commented for COEUSQA-2037 : Software allows you to delete an investigator who is assigned credit in the credit split window - Start
//                    String personId = (String)tblPerson.getValueAt(
//                            selectedRow,PERSON_ID_COLUMN);
                        //COEUSQA-2037 : END
                        //Modified for Coeus 4.3-PT ID:2270 Tracking Effort- end
                        if(personId != null && personId.trim().length()>0){
                            if(investigatorData.containsKey(personId)){
                            /* remove investigator details from investigator
                             hashtable and also his corresponding unit details
                             from unit hashtable */
                                for( int indx = 0 ; indx < persons.size() ; indx++ ) {
                                    String existingPersonId = (String) persons.elementAt( indx );
                                    if( existingPersonId.equals( personId ) ) {
                                        persons.removeElementAt(indx );
                                        break;
                                    }
                                }
                                detailForm.deletePropPerson(personId, true );
                                // Added for COEUSQA-2637 : CLONE -Proposal dev - YNQ save is not consistent - Start
                                for(Object exitingInv : existingInvestigators){
                                    ProposalInvestigatorFormBean investigatorFormBean = (ProposalInvestigatorFormBean)exitingInv;
                                    if(investigatorFormBean.getPersonId().equals(personId)){
                                        investigatorFormBean.setAcType(TypeConstants.DELETE_RECORD);
                                        Vector vecInvUnits = investigatorFormBean.getInvestigatorUnits();
                                        for(Object investigator : vecInvUnits){
                                            ProposalLeadUnitFormBean leadUnitFormBean = (ProposalLeadUnitFormBean)investigator;
                                            leadUnitFormBean.setAcType(TypeConstants.DELETE_RECORD);
                                        }
                                        Vector vecInvAnswers = investigatorFormBean.getInvestigatorAnswers();
                                        for(Object investigatorAnswers : vecInvAnswers){
                                            ProposalYNQFormBean proposalYNQFormBean = (ProposalYNQFormBean)investigatorAnswers;
                                            proposalYNQFormBean.setAcType(TypeConstants.DELETE_RECORD);
                                        }
                                    }
                                }
                                // Added for COEUSQA-2637 : CLONE -Proposal dev - YNQ save is not consistent - End

                                investigatorData.remove(personId);
                                if(unitHashData.containsKey(personId)){
                                    unitHashData.remove(personId);
                                }
                            }
                        }
                        ((DefaultTableModel)
                        tblPerson.getModel()).removeRow( selectedRow );
                        saveRequired = true;
                    }
                }
            }else{
                int selectedOption = showDeleteConfirmMessage(coeusMessageResources.parseMessageKey(
                        "protoInvForm_exceptionCode.1221"));
                if( selectedOption == JOptionPane.YES_OPTION ){
                    ((DefaultTableModel)
                    tblPerson.getModel()).removeRow( selectedRow );
                    saveRequired = true;
                }
            }

            int newRowCount = tblPerson.getRowCount();
            if(newRowCount >0){
                if(newRowCount > selectedRow){
                    tblPerson.setRowSelectionInterval(selectedRow,
                            selectedRow);
                }else{
                    tblPerson.setRowSelectionInterval(
                            newRowCount - 1,
                            newRowCount - 1);
                }
                int rowCount = tblPerson.getRowCount() -1 ;
                tblPerson.requestFocusInWindow();
                //Modified for Coeus 4.3 -PT ID:2229 Multi PI - start
                tblPerson.editCellAt(rowCount,NAME_COLUMN);
                //Modified for Coeus 4.3 -PT ID:2229 Multi PI - end
                tblPerson.getEditorComponent().requestFocusInWindow();
            }
            if( tblPerson.getRowCount() <= 0 ){
                /* if there are no investigator rows after deleting this
                  particular row then clear all entries in unit table */
                btnDelete.setEnabled( false );
                btnCertify.setEnabled( false );
                ((DefaultTableModel)tblLead.getModel()).setDataVector(
                        new Object[][]{}, getUnitTableColumnNames().toArray());
                setTableEditors();
                btnAdd.requestFocusInWindow();
                btnDelUnit.setEnabled( false );
                btnAddUnit.setEnabled( false );
                btnFindUnit.setEnabled( false );
                btnCertify.setEnabled( false );
                //Case 2106 Start 5
                btnCreditSplit.setEnabled( false );
                //Case 2106 End 5
            }
        }else{
            showWarningMessage(
                    coeusMessageResources.parseMessageKey(
                    "protoInvFrm_exceptionCode.1066") );
        }
    }
    /* supporting method to delete the selected unit row from the units table */
    private void performDeleteUnit(){
        int selectedRow = tblPerson.getSelectedRow();
        /* delete the selected unit information from table as well as from
          the vector which consists of all unit details for a particular
          investigator */
        if(selectedRow != -1){
            //Modified for Coeus 4.3-PT ID:2270 Tracking Effort- start
            //String personId = (String)tblPerson.getValueAt(selectedRow,5);
            String personId = (String)tblPerson.getValueAt(selectedRow,PERSON_ID_COLUMN);
            //Modified for Coeus 4.3-PT ID:2270 Tracking Effort- end
            int selectedUnitRow = tblLead.getSelectedRow();
            if( selectedUnitRow != -1 ){
                String unitNo = (String)tblLead.getValueAt(selectedUnitRow,2);
                if(unitNo != null && unitNo.trim().length()>0){
                    int selectedOption
                            = showDeleteConfirmMessage("Are you sure you want"
                            +" to remove "+unitNo+" ?");
                    if( selectedOption == JOptionPane.YES_OPTION ){
                        //Added for COEUSQA-2037 : Software allows you to delete an investigator who is assigned credit in the credit split window
                        //When there is only one unit for the investigator exists,
                        //removes the unit without check the credit split
                        //When multiple unit exists, Checks for the unit credit split information,
                        //until split set to '0' won't allow to delete the unit
                        if(tblLead.getRowCount() > 1 && checkInvUnitHasAddsToHundCreditSplit(personId,unitNo)){
                              int selectedOpt = CoeusOptionPane.showQuestionDialog(
                                              coeusMessageResources.parseMessageKey(CREDIT_SPLIT_EXISTS_FOR_INV_UNIT) ,
                                  CoeusOptionPane.OPTION_YES_NO,
                                  CoeusOptionPane.DEFAULT_YES);
                              if(selectedOpt == JOptionPane.YES_OPTION){
                                  if(isSaveRequired()){
                                      MessageFormat formatter = new MessageFormat("");
                                      String message = formatter.format(
                                              coeusMessageResources.parseMessageKey(SAVE_BEFORE_OPEN_CREDIT_SPILT),
                                              "proposal");
                                      CoeusOptionPane.showInfoDialog(message);
                                  }else{
                                      performCreditSplitAction();
                                  }
                              }
                        }else{
                            //COEUSQA-2037 : End
                            ProposalLeadUnitFormBean personUnitBean = null;
                            if(unitHashData.containsKey(personId)){
                                Vector personUnits = new Vector();
                                personUnits = (Vector)unitHashData.get(personId);
                                if(personUnits!= null){
                                    for(int unitIndex = 0;
                                    unitIndex < personUnits.size();
                                    unitIndex++){
                                        personUnitBean =
                                                (ProposalLeadUnitFormBean)
                                                personUnits.elementAt(unitIndex);
                                        if(personUnitBean.getUnitNumber().equals(
                                                unitNo)){
                                            personUnits.removeElementAt(unitIndex);
                                            break;
                                        }
                                    }
                                }
                                unitHashData.put(personId,personUnits);
                            }
                            saveRequired = true;
                            ((DefaultTableModel)tblLead.getModel()
                            ).removeRow( selectedUnitRow );
                        }
                    }
                }else{
                    int selectedOption = showDeleteConfirmMessage(coeusMessageResources.parseMessageKey(
                            "unitDetFrm_exceptionCode.1331"));
                    if( selectedOption == JOptionPane.YES_OPTION ){
                        saveRequired = true;
                        ((DefaultTableModel)tblLead.getModel()).removeRow(
                                selectedUnitRow );
                    }
                    /*saveRequired = true;
                    ((DefaultTableModel)tblLead.getModel()).removeRow(
                    selectedUnitRow );*/
                }

                int newRowCount = tblLead.getRowCount();
                if(newRowCount >0){
                    if(newRowCount > selectedUnitRow){
                        tblLead.setRowSelectionInterval(selectedUnitRow,
                                selectedUnitRow);
                    }else{
                        tblLead.setRowSelectionInterval(
                                newRowCount - 1,
                                newRowCount - 1);
                    }
                }else{
                    btnDelUnit.setEnabled( false );
                }
            }else{
                showWarningMessage(
                        coeusMessageResources.parseMessageKey(
                        "protoInvFrm_exceptionCode.1133") );
            }
        }

    }
    /* implementation for changing the units table data depending on the selected
     * investigator row.
     */
    public void valueChanged(javax.swing.event.ListSelectionEvent listSelectionEvent) {

        Object source = listSelectionEvent.getSource();

//        int selectedLeadUnitRow = tblLead.getSelectedRow();
        int selectedRow = tblPerson.getSelectedRow();
//        int rowCount = tblPerson.getRowCount();

        if( (source.equals(perSelectionModel) )&& (selectedRow >= 0 )
        && (investigatorData != null)) {

            ProposalInvestigatorFormBean investigatorRow=null;
            investigatorRow = (ProposalInvestigatorFormBean)
            //Modified for Coeus 4.3-PT ID:2270 Tracking Effort- start
//            investigatorData.get( tblPerson.getValueAt( selectedRow,
//                    5) == null ? "" : tblPerson.getValueAt( selectedRow,
//                    5).toString() );
            investigatorData.get( tblPerson.getValueAt( selectedRow,
                    PERSON_ID_COLUMN) == null ? "" : tblPerson.getValueAt( selectedRow,
                    PERSON_ID_COLUMN).toString() );
            //Modified for Coeus 4.3-PT ID:2270 Tracking Effort- end
            if(investigatorRow != null){
                Vector unitsForPerson = getUnitTableData(
                        investigatorRow.getPersonId());
                if( unitsForPerson != null ){
                    ((DefaultTableModel)tblLead.getModel()).setDataVector(
                            unitsForPerson, getUnitTableColumnNames() );
                    if(tblLead.getRowCount() > 0 ){
                        tblLead.setRowSelectionInterval(0,0);
                    }
                    // Case 3183 - Proposal Hierarchy enhancment - Start
                    //Disable the Unit Delete Button, Check if the proposal is in parent hierachy
                    if(functionType != DISPLAY_MODE && !isParent()){
                        btnDelUnit.setEnabled( true );
                    } // Case 3183 - Proposal Hierarchy enhancment - End
                }
            } else {
                ((DefaultTableModel)tblLead.getModel()).setDataVector(
                        new Vector(), getUnitTableColumnNames() );
            }
            previousSelRow = selectedRow;

            setTableEditors();
        }
    }

    /** Method used to know whether changes have been made to investigator form
     * @return boolean true if data is changed, else false.
     */

    public boolean isSaveRequired() {
        return saveRequired;
    }

    //Added by Amit 11/21/2003
    /** This method use to implement focus on first editable component in this page.
     */
    public void setDefaultFocusForComponent(){
        if(tblPerson.getRowCount() > 0 ) {
            tblPerson.requestFocusInWindow();
            int rowNum = tblPerson.getSelectedRow();
            if(rowNum  > 0){
                tblPerson.setRowSelectionInterval(rowNum ,rowNum );
            }
            tblPerson.setColumnSelectionInterval(1,1);
            int rowLead = tblLead.getSelectedRow();
            if(rowLead > 0){
                tblLead.setRowSelectionInterval(rowLead,rowLead);
            }
            tblLead.setColumnSelectionInterval(1,1);
        }else{
            btnAdd.requestFocusInWindow();
        }
    }
    //end Amit

    /** Method used to set the saveRequired flag.
     * @param saveRequired New value of property saveRequired.
     */
    public void setSaveRequired(boolean saveRequired) {
        this.saveRequired = saveRequired;
        if(certifyForm != null){
            certifyForm.setSaveRequired(saveRequired);
        }
    }

    /** Method which returns the form opened mode.
     * @return character representing the form opened mode.
     */
    public char getFunctionType() {
        return functionType;
    }

    /** Method to set the form opened mode.
     * @param functionType character representing the form opened mode.
     */
    public void setFunctionType(char functionType) {
        this.functionType = functionType;
    }

    /** This method is used to set the investigators collection object
     * @param investigators collection of <CODE>ProposalInvestigatorFormBean</CODE>s.
     */
    public void setInvestigatorData(Vector investigators){
        int selectedRow=0;
        int leadSelectedRow=0;
        if(tblPerson.getRowCount()>0) {
            selectedRow=tblPerson.getSelectedRow();
        }
        if(tblLead.getRowCount()>0) {
            leadSelectedRow=tblLead.getSelectedRow();
        }

        this.existingInvestigators = new Vector();
        this.existingInvestigators = investigators;
        investigatorData = constructInvHashData( investigators );

        setFormData();
        //Added for Case #2301 start 2

        //checking parameter
           try
              {

            parameterExist=fetchParameterValue();
            //COEUSQA-3956 Start
            if(parameterExist){
                if(ISNeedToShowYNQWhenPPCEnabled(proposalId)){
                    parameterExist = false;
                }
            }          
           //COEUSQA-3956 End
            if(parameterExist)
            {
            viewDeptRightExistForAggre=getViewCertificationRight();
            viewDeptRightExistForAggre=getViewCertificationRight();
            rightExist=getMaintainPersonCertificationRight();
            if(!rightExist){
                deptRightExist=getMaintainDepartmentPersonCertificationRight();
            }else if(!rightExist && !deptRightExist){
                viewDeptRightExist=getViewDepartmentPersonCertificationRight();
            }
            if(rightExist||deptRightExist||viewDeptRightExist||viewDeptRightExistForAggre)
            {
            certifyEnabled = true;
            }
 //Added for COEUSDEV-736:
 //This code is added to disable  Certify button  if the proposal is a child proposal.
              boolean isParent = isParent();
              if(inHierarchy)
              {
              if(isParent)
              {
                 certifyEnabled = true;
              }
              else
              {
                 certifyEnabled = false;
              }
              }
//Added for COEUSDEV-736: to disable Certify button end.
           }
         else
            {
             validateCertifyButton();
            }
            }
            catch(Exception ex)
            {
                ex.printStackTrace();
            }
   //checking parameter
      //  validateCertifyButton();


        //Addef for Case #2301 end 2
        formatFields();
        setTableEditors();

        if(tblPerson.getRowCount()>0){
            //Bug Fix:1233 Start
            //tblPerson.setRowSelectionInterval(selectedRow, selectedRow);
            //Bug Fix:1233 End
            // Case# 3779: Get 'row index out of range' error on units - Start
//            if(tblLead.getRowCount()>0){
//                tblLead.setRowSelectionInterval(leadSelectedRow, leadSelectedRow);
//            }
            int rowCount = tblLead.getRowCount();
            if(rowCount > 0){
                if(leadSelectedRow < rowCount){
                    tblLead.setRowSelectionInterval(leadSelectedRow, leadSelectedRow);
                } else {
                    tblLead.setRowSelectionInterval(0, 0);
                }
                // Case# 3779: Get 'row index out of range' error on units - End
            }
        }
    }

    /** This method is used to validate the mandatory fields of this form.
     * @throws Exception if mandatory fields are not specified or
     * validation check failed for these fields.
     * @return true if data in all mandatory fields are valid, else false.
     */
    public boolean validateData() throws Exception{

        if(tblPerson.getRowCount()>0){
            int perRowCount = tblPerson.getRowCount();
            boolean piPresent = false;
            boolean pi = false;
            boolean leadUnitPresent = false;
            String selPersonId="";
            String selPersonName = "";
            Vector perUnits = null;
            ProposalLeadUnitFormBean perUnitBean
                    = new ProposalLeadUnitFormBean();
            validateFormData();
            /* for all investigator rows there should be atleast one unit
               associated with it. And only one investigator should be PI */
            for(int perRow = 0 ; perRow < perRowCount ; perRow++ ){
                //Modified for Coeus 4.3 -PT ID:2229 Multi PI - start
                selPersonId = (String)tblPerson.getValueAt(perRow,PERSON_ID_COLUMN);
                selPersonName = (String)tblPerson.getValueAt(perRow,NAME_COLUMN);
                pi = ((Boolean)tblPerson.getValueAt(perRow,PI_COLUMN)).booleanValue();
                //Modified for Coeus 4.3 -PT ID:2229 Multi PI - end
                if(selPersonId != null && selPersonId.trim().length()>0){
                    perUnits = (Vector)unitHashData.get(selPersonId);
                    if( (perUnits == null) || (perUnits.size() == 0)){
                       /* if there is no unit information associated with any
                          investigator show message and return as validty false */
                        tblPerson.setRowSelectionInterval(perRow,perRow);
                        log("There is no unit information associated with "
                                + selPersonName);

                        return false;
                    }else{
                       /* if a particular investigator has been marked as PI
                          then atleast on unit detail should be marked as lead
                        */
                        if(pi){
                            piPresent = true;
                            for(int perUnitCount = 0;
                            perUnitCount < perUnits.size();
                            perUnitCount++){
                                perUnitBean = (ProposalLeadUnitFormBean)
                                perUnits.elementAt(perUnitCount);
                                if(perUnitBean.isLeadUnitFlag()){
                                    leadUnitPresent = true;
                                    break;
                                }
                            }
                            if(!leadUnitPresent){
                                /* if none of the units have been marked as lead
                                  for primary investigator row then show error
                                  message and return validity false */
                                log(coeusMessageResources.parseMessageKey(
                                        "protoInvFrm_exceptionCode.1063"));
                                return false;
                            }
                        }
                    }
                }
            }
            if(!piPresent){
              /* if none of the investigators have been specified as PI then
                 show error message and return validity false */
                log(coeusMessageResources.parseMessageKey(
                        "protoInvFrm_exceptionCode.1064"));
                return false;
            }
        
        }

        return true;
    }

    public boolean validateFormData() throws Exception, CoeusUIException{
//        boolean valid=true;
        int rowCount = tblPerson.getRowCount();
        for(int inInd=0; inInd < rowCount ;inInd++){
            //Modified for Coeus 4.3 -PT ID:2229 Multi PI - start
            String stPersonId=(String)((DefaultTableModel)tblPerson.getModel()).getValueAt(inInd,PERSON_ID_COLUMN);
            //Modified for Coeus 4.3 -PT ID:2229 Multi PI - end
            if((stPersonId == null) || (stPersonId.trim().length() <= 0)){
                tblPerson.setRowSelectionInterval(inInd,inInd);
                log(coeusMessageResources.parseMessageKey("protoInvFrm_exceptionCode.1142"));
                break;
            }
        }
        return true;
    }

    //supporting method to throw exceptions during validation check.
    //    private void log(String msg) throws Exception{
    private void log(String mesg) throws CoeusUIException{
        //        throw new Exception(msg);
        CoeusUIException coeusUIException = new CoeusUIException(mesg,CoeusUIException.WARNING_MESSAGE);
        coeusUIException.setTabIndex(3);
        throw coeusUIException;

    }

    /** This method is used to get the Investigators information along with
     * the unit details entered for each Investigator.
     * @return Vector collection of <CODE>ProposalInvestigatorFormBean</CODE>.
     */
    public Vector getFormData(){
        Vector newInvestigatorsData = null;
        int sRow = tblPerson.getSelectedRow();

        //Bug Fix:1035 Start
        int rowCount = tblPerson.getRowCount();
        if(tblPerson.getCellEditor() != null && rowCount > 0){
            tblPerson.getCellEditor().stopCellEditing();
        }
        //Bug Fix:1035 End

        stopAllEditing( sRow );
        /* In modify mode loop through all exisiting investigators and check
           whether any investigators have been deleted. If yes, send that
           investigator bean with acType as DELETE_RECORD. If any changes have
           been made to existing investigators then set a flag so that this
           particular proposal will be modified to database with investigator and
           corresponding units data */

        if (existingInvestigators != null && existingInvestigators.size() > 0) {
            newInvestigatorsData = new Vector();
            for(int invCount = 0 ; invCount < existingInvestigators.size() ;
            invCount++) {
                ProposalInvestigatorFormBean proposalInvBean
                        = (ProposalInvestigatorFormBean)
                        existingInvestigators.get(invCount);
                if (investigatorData != null ) {

                    // Modified for COEUSQA-2637 : CLONE -Proposal dev - YNQ save is not consistent - Start
//                    if ( investigatorData.containsKey(
//                            proposalInvBean.getPersonId())) {
                        if ( investigatorData.containsKey(
                                proposalInvBean.getPersonId()) && !TypeConstants.DELETE_RECORD.equals(proposalInvBean.getAcType())) { // COEUSQA-2637 : End
                        proposalInvBean.setInvestigatorUnits(
                                getUnitData(proposalInvBean));
                        if(investigatorModified){
                            /* for any existing investigator if his units have
                               been modified then send that bean with acType as
                               UPDATE_RECORD  */
                            proposalInvBean.setAcType(UPDATE_RECORD);
                            saveRequired = true;
                        }
                    }else {
                        proposalInvBean.setAcType(DELETE_RECORD);
                        Vector qtns = proposalInvBean.getInvestigatorAnswers();
                        if( qtns != null && qtns.size() > 0 ) {
                            int qtnsSize = qtns.size();
                            ProposalYNQFormBean questionBean;
                            for( int indx=0 ; indx < qtnsSize ; indx++ ) {
                                questionBean = ( ProposalYNQFormBean )qtns.elementAt( indx );
                                questionBean.setAcType( DELETE_RECORD );
                                qtns.setElementAt( questionBean, indx );
                            }
                        }
                        proposalInvBean.setInvestigatorAnswers( qtns );

                        proposalInvBean.setInvestigatorUnits(null);
                        saveRequired = true;
                    }
                }

                // Added for COEUSQA-2637 : CLONE -Proposal dev - YNQ save is not consistent - Start
                // When an investigator is deleted, corresponding unit's acType is set to delete
                if(TypeConstants.DELETE_RECORD.equals(proposalInvBean.getAcType())){
                    Vector vecinvUnits = proposalInvBean.getInvestigatorUnits();
                    if(vecinvUnits != null && vecinvUnits.size()>0){
                        for(Object invUnitsDetails : vecinvUnits){
                            ProposalInvestigatorFormBean proposalInvestigatorFormBean = (ProposalInvestigatorFormBean)invUnitsDetails;
                            proposalInvestigatorFormBean.setAcType(TypeConstants.DELETE_RECORD);
                        }
                    }
                }
                // Added for COEUSQA-2637 : CLONE -Proposal dev - YNQ save is not consistent - End
                newInvestigatorsData.add(proposalInvBean);
            }
        }
        /* compare all existing investigators with new set of investigators */
        if (investigatorData != null ) {
            if(newInvestigatorsData == null){
                newInvestigatorsData = new Vector();
            }
            Enumeration enumKeys = investigatorData.keys();
            while (enumKeys.hasMoreElements() ) {
                boolean found = false;
                ProposalInvestigatorFormBean proposalInvBean
                        = (ProposalInvestigatorFormBean)investigatorData.get(
                        enumKeys.nextElement());
                proposalInvBean.setInvestigatorUnits(
                        getUnitData(proposalInvBean));
                if ((existingInvestigators != null )
                && (existingInvestigators.size() > 0)) {
                    for(int invCount = 0 ;
                    invCount < existingInvestigators.size() ; invCount++) {
                        ProposalInvestigatorFormBean exInvBean
                                = (ProposalInvestigatorFormBean)
                                existingInvestigators.get(invCount);

                        // Modified for COEUSQA-2637 : CLONE -Proposal dev - YNQ save is not consistent - Start
//                        if(proposalInvBean.getPersonId().equals(
//                                exInvBean.getPersonId())){
                        if(proposalInvBean.getPersonId().equals(
                                exInvBean.getPersonId()) && !TypeConstants.DELETE_RECORD.equals(exInvBean.getAcType())){ // COEUSQA-2637 : End
                            found = true;
                            break;
                        }
                    }
                }
                if(!found){
                    /* if any investigator has been added newly send that bean
                       with acType as INSERT_RECORD */
                    proposalInvBean.setAcType( INSERT_RECORD );
                    saveRequired = true;
                    newInvestigatorsData.add(proposalInvBean);
                }
            }
        }
        if(certifyForm != null){
            if(certifyForm.isSaveRequired()){
                saveRequired = true;
            }
        }
        return newInvestigatorsData;
    }



    //supporting method to get the Unit Detials for the specified investigator.
    private Vector getUnitData(ProposalInvestigatorFormBean invBean){
        investigatorModified = false;
        Vector newUnitsData = null;
        if  (invBean != null) {
            newUnitsData = new Vector();
            Vector bnUnits  = invBean.getInvestigatorUnits();
            Vector unitData = (Vector)unitHashData.get(invBean.getPersonId());
            /* loop through all available units for specified investigator and
               check whether any unit has been modified */
            if ((bnUnits != null) && (bnUnits .size() >0 ) ) {
                for (int bnUnitCount = 0;
                bnUnitCount < bnUnits.size(); bnUnitCount++ ){
                    final ProposalLeadUnitFormBean bnUnit =
                            (ProposalLeadUnitFormBean)bnUnits.get(bnUnitCount);
                    boolean found =false;
                    if (unitData != null && unitData.size() > 0) {
                        for ( int unitCount =0 ;unitCount < unitData.size() ;
                        unitCount++){
                            ProposalLeadUnitFormBean proposalUnitBean
                                    = (ProposalLeadUnitFormBean)
                                    unitData.get(unitCount);
                            if (bnUnit.getUnitNumber().equalsIgnoreCase(
                                    proposalUnitBean.getUnitNumber())) {
                                found =true;
                                if(bnUnit.isLeadUnitFlag()
                                != proposalUnitBean.isLeadUnitFlag()){
                                    /* if lead unit flag is modified then mark
                                       the bean acType to UPDATE_RECORD */
                                    bnUnit.setAcType( UPDATE_RECORD );
                                    saveRequired = true;
                                    if(functionType == MODIFY_MODE ){
                                        investigatorModified = true;
                                    }
                                }
                                bnUnit.setLeadUnitFlag(
                                        proposalUnitBean.isLeadUnitFlag());
                            }
                        }
                    }
                    if (!found) {
                        /* if existing unit is not present in current units list
                           then mark that bean as DELETE_RECORD and send to db */
                        bnUnit.setAcType( DELETE_RECORD );
                        saveRequired = true;
                        if(functionType == MODIFY_MODE ){
                            investigatorModified = true;
                        }
                    }
                    newUnitsData.add(bnUnit);
                }
            }
            /* loop through all current list of units and check whether any
               unit has been newly added */
            if (unitData != null && unitData.size() > 0) {
                for ( int unitCount =0 ;unitCount < unitData.size() ;
                unitCount++){
                    ProposalLeadUnitFormBean proposalUnitBean =
                            (ProposalLeadUnitFormBean)unitData.get(unitCount);
                    boolean  found = false;
                    if ((bnUnits != null) && (bnUnits .size() >0 ) ) {
                        for (int bnUnitCount = 0; bnUnitCount < bnUnits.size();
                        bnUnitCount++ ){
                            ProposalLeadUnitFormBean bnUnit =
                                    (ProposalLeadUnitFormBean)
                                    bnUnits.get(bnUnitCount);
                            if (proposalUnitBean.getUnitNumber().
                                    equalsIgnoreCase(bnUnit.getUnitNumber())){
                                found = true;
                            }
                        }
                    }
                    if (!found) {
                        /* if any unit has been newly added set acType to that
                           bean as INSERT_RECORD  and send to db */
                        proposalUnitBean.setAcType( INSERT_RECORD );
                        saveRequired = true;
                        if(functionType == MODIFY_MODE ){
                            investigatorModified = true;
                        }
                        newUnitsData.add(proposalUnitBean);
                    }
                }
            }
        }
        return newUnitsData;
    }

    /** Method used to get the proposals sponsor code.
     * @return String representing the proposal's SponsorCode.
     */
    public java.lang.String getSponsorCode() {
        return sponsorCode;
    }

    /** Method used to set the proposal's sponsor code , which is used in CertifyForm
     * @param sponsorCode String representing the proposal's SponsorCode.
     */
    public void setSponsorCode(java.lang.String sponsorCode) {
        this.sponsorCode = sponsorCode;
    }

    /** Method used to get the proposals sponsor Name.
     * @return String representing the proposal's Sponsor Name.
     */
    public java.lang.String getSponsorName() {
        return sponsorName;
    }

    /** Method used to set the proposal's sponsor name , which is used in CertifyForm
     * @param sponsorName String representing the proposal's Sponsor Name.
     */
    public void setSponsorName(java.lang.String sponsorName) {
        this.sponsorName = sponsorName;
    }

    /**
     * set the questions related to Proposal Investigators. It sets the vectors of
     * QuestionListBean.
     * @param questions Collection of QuestionListBean.
     */
    public void setInvestigatorQuestions(Vector questions){
        this.questions = questions;
    }
    /**
     * set the explanation for the questions. It sets the hashtable of
     * more explanations. The hashtable contains key as question id and explanation type
     * and value as explanations.
     *
     * @param moreExplanations Hashtable containing more explanation for the questions.
     */
    public void setMoreExplanations(Hashtable moreExplanations){
        this.moreExplanations = moreExplanations;
    }


    //supporting method to show the delete confirm message.
    private int showDeleteConfirmMessage(String msg){
        int selectedOption = CoeusOptionPane.showQuestionDialog(msg,
                CoeusOptionPane.OPTION_YES_NO,
                CoeusOptionPane.DEFAULT_YES);
        return  selectedOption;
    }

    //supporting method to validate the personName entered. If valid returns
    //PersonInfoFormBean otherwise null.

    //Bug fix for person Validation on focus lost Start 2
    //Changed the arg from personName to source
    //private PersonInfoFormBean checkPersonName( String personName ){
    private PersonInfoFormBean checkPersonName( Object source ){


        /*PersonInfoFormBean personInfo = null;
        personInfo = CoeusUtils.getInstance().getPersonInfoID( personName );
        if( personInfo == null || personInfo.getPersonID() == null){
            personInfo = null;
            CoeusOptionPane.showWarningDialog("\""+personName+"\"" + " "+
            coeusMessageResources.parseMessageKey(
             "repRequirements_exceptionCode.1055"));
        }
        return personInfo;*/

        String personName = ((JTextField)source).getText().trim();

        PersonInfoFormBean personInfo = null;
        RequesterBean requesterBean = new RequesterBean();
        ResponderBean responderBean = new ResponderBean();
        requesterBean.setFunctionType('J');
        requesterBean.setDataObject(personName);

        String connectTo = connectionURL + "/unitServlet";

        AppletServletCommunicator comm = new AppletServletCommunicator
                (connectTo, requesterBean);
        comm.setRequest(requesterBean);
        comm.send();
        responderBean = comm.getResponse();
        if(responderBean != null){
            if(responderBean.isSuccessfulResponse()) {
                personInfo =(PersonInfoFormBean)responderBean.getDataObject();
                if(personInfo.getPersonID() == null){
                    ((JTextField)source).setText("");
                    CoeusOptionPane.showErrorDialog(
                            coeusMessageResources.parseMessageKey("investigator_exceptionCode.1007"));
                    personInfo = null;
                }else if(personInfo.getPersonID().equalsIgnoreCase("TOO_MANY")){
                    ((JTextField)source).setText("");
                    CoeusOptionPane.showErrorDialog
                            ("\""+personName+"\""+" " +coeusMessageResources.parseMessageKey("repRequirements_exceptionCode.1055"));
                    personInfo = null;
                }
            }else{
                Exception ex = responderBean.getException();
                ex.printStackTrace();
            }
        }
        return personInfo;
        //Bug fix for person Validation on focus lost End 2
    }

    /** Method to get the Proposal's lead unit number.
     * @return String representing the proposal's lead unit number.
     */
    public String getProposalUnitNumber() {
        return proposalUnitNumber;
    }

    /** Method to set the proposal's lead unit number
     * @param proposalUnitNumber String representing the proposal's lead unit number.
     */
    public void setProposalUnitNumber(String proposalUnitNumber) {
        this.proposalUnitNumber = proposalUnitNumber;
    }

    /** Method to get the Proposal's lead unit name.
     * @return String representing the proposal's lead unit name.
     */
    public String getProposalUnitName() {
        return proposalUnitName;
    }

    /** Method to set the proposal's lead unit name
     * @param proposalUnitName String representing the proposal's lead unit name.
     */
    public void setProposalUnitName(String proposalUnitName) {
        this.proposalUnitName = proposalUnitName;
    }

    /**
     * Method used to check whether the given person is used in Investigator form
     * or not.
     * @param personId String representing the person id to verify.
     * @return boolean true if the given personId is used in ProposalInvestigatorForm,
     * else false.
     */
    public boolean isPersonPresent( String personId ) {
        if( persons == null ) {
            persons = new Vector();
        }
        int size = persons.size();
        for( int indx = 0 ; indx < size ; indx++ ) {
            if( persons.elementAt( indx ).equals( personId ) ) {
                return true;
            }
        }
        return false;
    }

    /** Getter for property proposalId.
     * @return Value of property proposalId.
     */
    public java.lang.String getProposalId() {
        return proposalId;
    }

    /** Setter for property proposalId.
     * @param proposalId New value of property proposalId.
     */
    public void setProposalId(java.lang.String proposalId) {
        this.proposalId = proposalId;
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

    /**
     * Inner Class to define the Name Editor Class Used for PersonName,
     * UnitNumber.
     */
    private class NameEditor extends DefaultCellEditor implements TableCellEditor {

        private JTextField txtName = new JTextField();
        private int textLength = 10;
        private String oldValue = "";

        //Bug Fix : 1222 - STEP 1 - START
        private int editedRow = -1;
        //Bug Fix : 1222 - STEP 1 - END

        NameEditor( String colName, int len ){

            super( new JTextField() );
            textLength = len;
            txtName.setFont(CoeusFontFactory.getNormalFont());
            txtName.setDocument(new LimitedPlainDocument(len));
            setListeners();
        }
        /* supporting method used to construct the investigator bean by fetching
           the details from the database for the entered person name. */
        private void updatePersonDetails(){
            int selectedRow = tblPerson.getSelectedRow();
            String oldPersonId = "";
            boolean primaryInv = false;
            if(selectedRow != -1){
                //Modified for Coeus 4.3-PT ID:2270 Tracking Effort- start
//                oldPersonId = (String)tblPerson.getValueAt(selectedRow,5);
//                primaryInv = ((Boolean)tblPerson.getValueAt(selectedRow
//                        ,3)).booleanValue();
                oldPersonId = (String)tblPerson.getValueAt(selectedRow,PERSON_ID_COLUMN);
                primaryInv = ((Boolean)tblPerson.getValueAt(selectedRow
                        ,PI_COLUMN)).booleanValue();

                //Modified for Coeus 4.3-PT ID:2270 Tracking Effort- end
            }

            if(txtName.getText() != null && txtName.getText().trim().length()>0){
                if( !oldValue.equalsIgnoreCase( txtName.getText().trim() )) {
                /* get the person details from database for the entered name.
                   If the person already exists checkPersonName returns null */

                    //Bug fix for person Validation on focus lost Start 1
                /*PersonInfoFormBean personInfo =
                                    checkPersonName( txtName.getText().trim() );*/
                    PersonInfoFormBean personInfo = checkPersonName( txtName );
                    //Bug fix for person Validation on focus lost End 1

                    if(personInfo != null){
                        ProposalInvestigatorFormBean invBean =
                                new ProposalInvestigatorFormBean();
                        invBean.setPersonId(personInfo.getPersonID());
                        invBean.setPersonName(personInfo.getFullName());
                        invBean.setPrincipleInvestigatorFlag(primaryInv);

                        invBean.setPercentageEffort( Float.parseFloat("0") );
                        invBean.setNonMITPersonFlag(false);
                        invBean.setFacultyFlag(
                                personInfo.getFacFlag().equalsIgnoreCase("y")
                                ? true : false);
                        String homeUnit = personInfo.getHomeUnit();
                        String pId = personInfo.getPersonID();
                        if(!pId.equals(oldPersonId)){
                            for( int indx = 0 ; indx < persons.size() ; indx++ ) {
                                String existingPersonId = (String) persons.elementAt( indx );
                                if( existingPersonId.equals( oldPersonId ) ) {
                                    persons.removeElementAt(indx );
                                    break;
                                }
                            }
                            detailForm.deletePropPerson(oldPersonId, true );
                        /* send the constructed bean, homeUnit number for the
                          investigator and send replaceRow as true as we have to
                          overwrite the existing row */
                            invBean.setAcType( INSERT_RECORD );
                            setInvestigatorBean(invBean,homeUnit,true);
                        }
                    }
                }
            }
            super.cancelCellEditing();
        }

        /* supporting method used to set focus listener and action listener for
           the editor text field */
        private void setListeners(){
            txtName.addMouseListener( new PersonDisplayAdapter());
            txtName.addFocusListener(new FocusAdapter(){
                public void focusLost(FocusEvent fe){
                    if (!fe.isTemporary()){
                        updatePersonDetails();
                    }
                }
            });
            txtName.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    updatePersonDetails();
                }
            });

        }
        public Component getTableCellEditorComponent(JTable table, Object value,
                boolean isSelected, int row, int column){

            //Bug Fix : 1222 - STEP 2 - START
            editedRow = row;
            //Bug Fix : 1222 - STEP 2 - END

            String newValue = ( String ) value ;
            oldValue = newValue;
            if( newValue != null && newValue.length() > 0 ){
                txtName.setText( (String)value );
            }else{
                txtName.setText("");
            }
            return txtName;
        }

        /**
         * Forwards the message from the CellEditor to the delegate.
         * @return true if editing was stopped; false otherwise
         */
        public boolean stopCellEditing() {
            updatePersonDetails();
            return super.stopCellEditing();
        }

        /** Returns the value contained in the editor.
         * @return Object the value contained in the editor
         */
        public Object getCellEditorValue() {
            return ((JTextField)txtName).getText();
        }

        public void itemStateChanged(ItemEvent e) {
            super.fireEditingStopped();
        }


        //Bug Fix : 1222 - STEP 3 - START
        public int getEditedRow() {
            return editedRow;
        }
        //Bug Fix : 1222 - STEP 3 - START
    }

    /**
     * This inner Class is used to set the Number Editor for Unit Number.
     */
    private class NumberEditor extends DefaultCellEditor implements TableCellEditor {

        private JTextField txtName = new JTextField();
        private int colLength = 1;
        private String oldValue = "";

        NumberEditor( String colName, int colLength ){

            super( new JTextField() );
            this.colLength = colLength;
            txtName.setFont(CoeusFontFactory.getNormalFont());
            txtName.setDocument( new JTextFieldFilter(JTextFieldFilter.ALPHA_NUMERIC,
                    colLength ));
            setListeners();

        }
        public Component getTableCellEditorComponent(JTable table, Object value,
                boolean isSelected, int row, int column){
            String newValue = ( String ) value ;
            oldValue = newValue;
            if( (newValue != null) && (newValue.length() > 0 ) ){
                txtName.setText( (String)value );
            }else{
                txtName.setText("");
            }
            return txtName;
        }

        private void setListeners(){
            txtName.addFocusListener(new FocusAdapter(){
                public void focusLost(FocusEvent fe){
                    if (!fe.isTemporary() ){
                        getUnitDetails();
                    }
                }
            });
            txtName.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    getUnitDetails();
                }
            });
            txtName.addMouseListener( new UnitDisplayAdapter());
        }


        private void getUnitDetails(){
            int row = tblLead.getSelectedRow();
            String unitNo = "";
            if(txtName.getText() != null && txtName.getText().trim().length()>0){
                if(!oldValue.equalsIgnoreCase(txtName.getText().trim())){
                    UnitDetailFormBean unitDetail
                            = CoeusUtils.getInstance().getUnitInfoBean( txtName.getText().trim() );
                    if(unitDetail != null){
                        int selPersonRow = tblPerson.getSelectedRow();
                        //Modified for Coeus 4.3-PT ID:2270 Tracking Effort- start
//                        String personId =
//                                (String)tblPerson.getValueAt(selPersonRow,5);
                        String personId =
                                (String)tblPerson.getValueAt(selPersonRow,PERSON_ID_COLUMN);
                        //Modified for Coeus 4.3-PT ID:2270 Tracking Effort- end
                        boolean leadUnit =((Boolean)tblLead.getModel().
                                getValueAt(row,1)).booleanValue();
                        unitNo = txtName.getText().trim();
                        if(!unitNo.equals(oldValue)){
                            if( !isDuplicateUnitID( personId,
                                    unitDetail.getUnitNumber())){
                                /* modified to delete the old unit details
                                  if user modifies the unit details by editing the
                                  unit number */
                                ProposalLeadUnitFormBean personUnitBean = null;
                                if(unitHashData.containsKey(personId)){
                                    Vector personUnits = new Vector();
                                    personUnits = (Vector)unitHashData.get(personId);
                                    if(personUnits!= null){
                                        for(int unitIndex = 0;
                                        unitIndex < personUnits.size();
                                        unitIndex++){
                                            personUnitBean
                                                    = (ProposalLeadUnitFormBean)
                                                    personUnits.elementAt(unitIndex);
                                            if(personUnitBean.getUnitNumber().equals(
                                                    oldValue)){
                                                personUnits.removeElementAt(unitIndex);
                                                break;
                                            }
                                        }
                                    }
                                    unitHashData.put(personId,personUnits);
                                }
                                /* end of modification for deleting old unit details */
                                oldValue = unitNo;
                                saveRequired = true;
                                ProposalLeadUnitFormBean unitBean
                                        = new ProposalLeadUnitFormBean();

                                unitBean.setUnitNumber(unitDetail.getUnitNumber());
                                unitBean.setUnitName(unitDetail.getUnitName());
                                unitBean.setPersonId(personId);
                                unitBean.setLeadUnitFlag(leadUnit);
                                unitBean.setAcType( INSERT_RECORD );
                                addToHashtable(personId,unitBean);
                                String unitName = unitDetail.getUnitName();
//                                String ospAdmin = unitDetail.getOspAdminName();
                                ((DefaultTableModel)tblLead.getModel()).setValueAt(
                                        unitDetail.getUnitNumber(),row,2);
                                ((DefaultTableModel)tblLead.getModel()).setValueAt(
                                        unitName,row,3);
                            }else{
                                showWarningMessage( "' " + unitNo + "' " +
                                        coeusMessageResources.parseMessageKey(
                                        "protoInvFrm_exceptionCode.1137"));
                            }
                        }
                    }
                }
            }
            super.cancelCellEditing();
        }


        /**
         * Forwards the message from the CellEditor to the delegate.
         * @return true if editing was stopped; false otherwise
         */
        public boolean stopCellEditing() {
            getUnitDetails();
            return super.stopCellEditing();
        }

        /** Returns the value contained in the editor.
         * @return Object the value contained in the editor
         */
        public Object getCellEditorValue() {
            return ((JTextField)txtName).getText();
        }
    }
    // supporting class to display PersonDetails or Rolodex details on
    // double clicking of any investigator row.
    class PersonDisplayAdapter extends MouseAdapter {
        public void mouseClicked( MouseEvent me ) {
            int selRow = tblPerson.getSelectedRow();
            //Modified for Coeus 4.3 -PT ID:2229 Multi PI - start
//            boolean nonEmployee=((Boolean)tblPerson.getModel().
//                    getValueAt(selRow,6)).booleanValue();
//            String stId=(String)tblPerson.getModel().getValueAt(
//                    selRow,5 );
            boolean nonEmployee=((Boolean)tblPerson.getModel().
                    getValueAt(selRow,NONEMP_COLUMN)).booleanValue();
            String stId=(String)tblPerson.getModel().getValueAt(
                    selRow,PERSON_ID_COLUMN );
            //Modified for Coeus 4.3 -PT ID:2229 Multi PI - end

            if ( me.getClickCount() == 2) {
                if((stId != null) && (stId.trim().length()>0 )){
                    if(nonEmployee){
                        /* selected investigator is a rolodex, so show
                           rolodex details */
                        RolodexMaintenanceDetailForm frmRolodex
                                = new RolodexMaintenanceDetailForm('V',stId);
                        frmRolodex.showForm(mdiForm,
                                CoeusGuiConstants.TITLE_ROLODEX,true);
                    }else{

                        //String personName =
                        //(String)tblPerson.getModel().getValueAt(selRow,1);
                        /*Bug Fix, to get the person detail data with the personId instead of the person name*/
                        //PersonInfoFormBean personInfoFormBean = getPersonInfoID(personName);


                        String loginUserName = mdiForm.getUserName();
                        try{
                            //Changed the constructor for Case #1602 - Person Enhancement Start
                            //PersonDetailForm personDetailForm =new PersonDetailForm(stId ,loginUserName,DISPLAY_MODE);
                            char MODULE_CODE = 'I';
                            PersonDetailForm personDetailForm =new PersonDetailForm(
                                    stId ,loginUserName,DISPLAY_MODE, MODULE_CODE, proposalId);
                            //Changed the constructor for Case #1602 - Person Enhancement End
                        }catch ( Exception e) {
                            CoeusOptionPane.showInfoDialog( e.getMessage() );
                        }
                    }
                }
                //Modified for Coeus 4.3 -PT ID:2229 Multi PI - start
                ((NameEditor)tblPerson.getCellEditor(selRow,
                        NAME_COLUMN)).cancelCellEditing();
                //Modified for Coeus 4.3 -PT ID:2229 Multi PI - end

            }
        }
    }

    // supporting class to display unit details on double clicking of any row
    // in unit table.
    class UnitDisplayAdapter extends MouseAdapter {
        public void mouseClicked( MouseEvent me ) {
            int selRow = tblLead.getSelectedRow();
            String unitId=
                    (String)tblLead.getModel().getValueAt(selRow,2);
            if(unitId != null && unitId.trim().length() >0){
                if (me.getClickCount() == 2) {
                    try{
                        UnitDetailForm frmUnit =
                                new UnitDetailForm(unitId,'G');
                        frmUnit.showUnitForm(mdiForm);
                    } catch(Exception ex){
                        CoeusOptionPane.showErrorDialog(
                                coeusMessageResources.parseMessageKey(
                                "protoInvFrm_exceptionCode.1136"));
                    }
                    ((NumberEditor)tblLead.getCellEditor(selRow,
                            2)).cancelCellEditing();
                }
            }
        }
    }

    //Case 2106 Start 4
    private void performCreditSplitAction(){
        Vector vecData = new Vector();

        InvCreditSplitObject invCreditSplitObject = new InvCreditSplitObject();

        InvestigatorCreditSplitController invCreditSplitController =
                new InvestigatorCreditSplitController(functionType);


        invCreditSplitObject.setModuleName(CoeusGuiConstants.PROPOSAL_MODULE);
        invCreditSplitObject.setModuleNumber(proposalId);

        vecData.add(investigatorData);
        vecData.add(unitHashData);
        invCreditSplitObject.setInvData(vecData);

        invCreditSplitController.setFormData(invCreditSplitObject);
        invCreditSplitController.display();
        invCreditSplitController.cleanUp();

    }
    //Case 2106 End 4
    //Added for case id 3183 - start
    public void setInHierarchy(boolean inHierarchy){
        this.inHierarchy = inHierarchy;
    }
    //Added for case id 3183 - end
    //Added for COEUSQA-2037 : Software allows you to delete an investigator who is assigned credit in the credit split window - Start
    /*
     * Method to check investigatoe has Adds to hundred credit split value greater than '0'
     * @param personId
     * @return hasCreditSplit
     */
    private boolean checkInvHasAddsToHundredCreditSplit(String personId){
        boolean hasCreditSplit = false;
        ProposalInvestigatorFormBean investigator =
                (ProposalInvestigatorFormBean) investigatorData.get(personId);
        String connectTo = CoeusGuiConstants.CONNECTION_URL + CoeusGuiConstants.USER_SERVLET;
        RequesterBean request = new RequesterBean();
        CoeusVector cvInvestigatorData = new CoeusVector();
        cvInvestigatorData.add(MODULE_CODE_INDEX,PROPOSAL_MODULE_CODE);
        cvInvestigatorData.add(MODULE_ITEM_KEY_INDEX,investigator.getProposalNumber());
        cvInvestigatorData.add(MODULE_ITEM_KEY_SEQUENCE_INDEX,new Integer(PROPOSAL_SEQUENCE));
        cvInvestigatorData.add(PERSON_ID_INDEX,personId);
        cvInvestigatorData.add(ADDS_TO_HUNDRED_INDEX,"Y");
        request.setDataObjects(cvInvestigatorData);
        request.setFunctionType(CHECK_CREDIT_SPLIT_FOR_INVESTIGATOR);
        AppletServletCommunicator comm = new AppletServletCommunicator(
                connectTo, request);
        comm.send();
        ResponderBean response = comm.getResponse();
        if(response != null && response.isSuccessfulResponse()){
            hasCreditSplit = ((Boolean)response.getDataObject()).booleanValue();
        }
        return hasCreditSplit;
    }

    /*
     * Method to check investigator unit has Adds to hundred credit split greater value than '0'
     * @param personId
     * @param unitNumber
     * @return hasCreditSplit
     */
    private boolean checkInvUnitHasAddsToHundCreditSplit(String personId, String unitNumber){
        boolean hasCreditSplit = false;
        ProposalInvestigatorFormBean investigator =
                (ProposalInvestigatorFormBean) investigatorData.get(personId);
        String connectTo = CoeusGuiConstants.CONNECTION_URL + CoeusGuiConstants.USER_SERVLET;
        RequesterBean request = new RequesterBean();
        CoeusVector cvInvUnitData = new CoeusVector();
        cvInvUnitData.add(MODULE_CODE_INDEX,PROPOSAL_MODULE_CODE);
        cvInvUnitData.add(MODULE_ITEM_KEY_INDEX,investigator.getProposalNumber());
        cvInvUnitData.add(MODULE_ITEM_KEY_SEQUENCE_INDEX,new Integer(PROPOSAL_SEQUENCE));
        cvInvUnitData.add(PERSON_ID_INDEX,personId);
        cvInvUnitData.add(ADDS_TO_HUNDRED_INDEX,"Y");
        cvInvUnitData.add(UNIT_NUMBER_INDEX,unitNumber);
        request.setDataObjects(cvInvUnitData);
        request.setFunctionType(CHECK_CREDIT_SPLIT_FOR_INV_UNIT);
        AppletServletCommunicator comm = new AppletServletCommunicator(
                connectTo, request);
        comm.send();
        ResponderBean response = comm.getResponse();
        if(response != null && response.isSuccessfulResponse()){
            hasCreditSplit = ((Boolean)response.getDataObject()).booleanValue();
        }
        return hasCreditSplit;
    }
    //COEUSQA-2037 : End

    //~*~**~**~*~*~**~*~*~*~**~*~
  private boolean fetchParameterValue()throws Exception{
        Map mpParameterName = new HashMap();
        boolean parameterFlag = false;
        RequesterBean  requesterB = new RequesterBean();
        requesterB.setFunctionType(GET_PARAMETER_VALUE);
       // requesterBean.setId("ENABLE_PROP_PERSON_SELF_CERTIFY");
        requesterB.setParameterValue("ENABLE_PROP_PERSON_SELF_CERTIFY");
      //  requesterB.setDataObject("ENABLE_PROP_PERSON_SELF_CERTIFY");
        AppletServletCommunicator comm = new AppletServletCommunicator(
        connect, requesterB);
        comm.send();
           ResponderBean response = comm.getResponse();
        if(response!= null){
            if(response.isSuccessfulResponse()){
                parameterFlag = ((Boolean)response.getParameterValue()).booleanValue();

            }else {
                throw new Exception(response.getMessage());
            }
        }
         return parameterFlag ;
    }

    //Added For PPC
     private boolean getMaintainPersonCertificationRight()throws Exception{
       // String ProposalNum= proposalDevelopmentFormBean.getProposalNumber();
        String ProposalNum= proposalId;
        boolean hasMaintainPersonCertificationRight = false;
        RequesterBean  requesterBean = new RequesterBean();
        requesterBean.setFunctionType( MAINTAIN_PERSON_CERTIFICATION );
        requesterBean.setId(ProposalNum);
        requesterBean.setDataObject((String)mdiForm.getUserId() );
        AppletServletCommunicator comm = new AppletServletCommunicator(
        connect, requesterBean);
        comm.send();
        ResponderBean response = comm.getResponse();
        if(response!= null){
            if(response.isSuccessfulResponse()){
                hasMaintainPersonCertificationRight = ((Boolean)response.getDataObject()).booleanValue();
            }else {
                throw new Exception(response.getMessage());
            }
        }
        return hasMaintainPersonCertificationRight;
    }
       private boolean getViewCertificationRight()throws Exception{
       // String ProposalNum= proposalDevelopmentFormBean.getProposalNumber();
        String ProposalNum= proposalId;
        boolean hasMaintainPersonCertificationRight = false;
        RequesterBean  requesterBean = new RequesterBean();
       requesterBean.setFunctionType(VIEW_DEPT_PERSNL_CERTIFN );
        requesterBean.setId(ProposalNum);
        requesterBean.setDataObject((String)mdiForm.getUserId() );
        AppletServletCommunicator comm = new AppletServletCommunicator(
        connect, requesterBean);
        comm.send();
        ResponderBean response = comm.getResponse();
        if(response!= null){
            if(response.isSuccessfulResponse()){
                hasMaintainPersonCertificationRight = ((Boolean)response.getDataObject()).booleanValue();
            }else {
                throw new Exception(response.getMessage());
            }
        }
        return hasMaintainPersonCertificationRight;
    }

  private boolean getMaintainDepartmentPersonCertificationRight()throws Exception{
      //  String ProposalNum= proposalDevelopmentFormBean.getProposalNumber();
           String ProposalNum= proposalId;
        boolean hasMaintainPersonCertificationRight = false;
        RequesterBean  requesterBean = new RequesterBean();
        requesterBean.setFunctionType(MAINTAIN_DEPT_PERSONNEL_CERT );
        requesterBean.setId(ProposalNum);
        requesterBean.setDataObject((String)mdiForm.getUserId() );
        AppletServletCommunicator comm = new AppletServletCommunicator(
        connect, requesterBean);
        comm.send();
        ResponderBean response = comm.getResponse();
        if(response!= null){
            if(response.isSuccessfulResponse()){
                hasMaintainPersonCertificationRight = ((Boolean)response.getDataObject()).booleanValue();
            }else {
                throw new Exception(response.getMessage());
            }
        }
        return hasMaintainPersonCertificationRight;
    }
   private boolean getViewDepartmentPersonCertificationRight()throws Exception{
      //  String ProposalNum= proposalDevelopmentFormBean.getProposalNumber();
           String ProposalNum= proposalId;
        boolean hasMaintainPersonCertificationRight = false;
        RequesterBean  requesterBean = new RequesterBean();
        requesterBean.setFunctionType(VIEW_DEPT_PERSNL_CERTIFN );
        requesterBean.setId(ProposalNum);
        requesterBean.setDataObject((String)mdiForm.getUserId() );
        AppletServletCommunicator comm = new AppletServletCommunicator(
        connect, requesterBean);
        comm.send();
        ResponderBean response = comm.getResponse();
        if(response!= null){
            if(response.isSuccessfulResponse()){
                hasMaintainPersonCertificationRight = ((Boolean)response.getDataObject()).booleanValue();
            }else {
                throw new Exception(response.getMessage());
            }
        }
        return hasMaintainPersonCertificationRight;
    }
   //COEUS-67
    private String getAppHomeUrl() throws Exception{     
        RequesterBean  requesterBean = new RequesterBean();
        requesterBean.setFunctionType(GET_APP_HOME_URL);      
        String appHomeUrl = null;
        AppletServletCommunicator comm = new AppletServletCommunicator(
        connect, requesterBean);
        comm.send();
        ResponderBean response = comm.getResponse();
        if(response!= null){
            if(response.isSuccessfulResponse()){
                appHomeUrl = (String)response.getParameterValue();
            }else {
                throw new Exception(response.getMessage());
            }
        }
        return appHomeUrl;
    }
   //COEUS-67 
 //~*~**~**~*~*~**~*~*~*~**~*~
    //COEUSQA-3951  start
  public boolean ISNeedToShowYNQWhenPPCEnabled(String proposalNumber) throws Exception{               
        boolean showYNQ = false;
        RequesterBean  requesterBean = new RequesterBean();
        requesterBean.setFunctionType(IS_NEED_TO_SHOW_YNQ_WHEN_PPC);
        requesterBean.setParameterValue(proposalNumber);
        AppletServletCommunicator comm = new AppletServletCommunicator(
        connect, requesterBean);
        comm.send();
        ResponderBean response = comm.getResponse();
        if(response!= null){
            if(response.isSuccessfulResponse()){
                showYNQ = ((Boolean)response.getDataObject()).booleanValue();
            }else {
                throw new Exception(response.getMessage());
            }
        }
        return showYNQ;
    }  
  
  //COEUSQA-3951  end

    // Variables declaration - do not modify
    public javax.swing.JButton btnAdd;
    public javax.swing.JButton btnAddUnit;
    public javax.swing.JButton btnCertify;
    public javax.swing.JButton btnCreditSplit;
    public javax.swing.JButton btnDelUnit;
    public javax.swing.JButton btnDelete;
    public javax.swing.JButton btnFindPerson;
    public javax.swing.JButton btnFindRolodex;
    public javax.swing.JButton btnFindUnit;
    public javax.swing.JScrollPane scrPnPerson;
    public javax.swing.JScrollPane scrPnUnit;
    public javax.swing.JTable tblLead;
    public javax.swing.JTable tblPerson;
    // End of variables declaration

}
