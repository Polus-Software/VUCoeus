/*
 * @(#)ProtocolInvestigatorsForm.java
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 */

/* PMD check performed, and commented unused imports and variables on 16-FEB-2011
 * by Bharati
 */

package edu.mit.coeus.irb.gui;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import javax.swing.event.*;

import java.util.*;
//import java.text.NumberFormat;
//import java.text.DecimalFormat;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import edu.mit.coeus.gui.*;
import edu.mit.coeus.irb.bean.*;
import edu.mit.coeus.rolodexmaint.gui.RolodexMaintenanceDetailForm;
import edu.mit.coeus.utils.*;
import edu.mit.coeus.search.gui.*;
import edu.mit.coeus.brokers.*;
import edu.mit.coeus.unit.bean.*;
import edu.mit.coeus.unit.gui.UnitDetailForm;
import edu.mit.coeus.departmental.gui.PersonDetailForm;

import edu.mit.coeus.exception.*;
import edu.mit.coeus.mail.bean.MailMessageInfoBean;

/** This class is used to create ProtocolInvestigators tab window with the
 * privilege of adding, deleting and finding new person/unit. This component
 * is instantiated from <CODE>ProtocolDetailsForm</CODE>.
 *
 * @author Subramanya
 * @updated by Ravikanth
 */
public class ProtocolInvestigatorsForm extends JComponent
    implements ActionListener,ListSelectionListener{
    
    //holds reference to  CoeusAppletMDIForm
    private final CoeusAppletMDIForm mdiForm = CoeusGuiConstants.getMDIForm();
    
    /* holds the Data Vector for person entries. each object contains a vector 
     * of ProtocolInvestigatorsBean data objects
     */
    private Vector personData;
    
    /* holds complete Investigator data with key as personId and 
     * ProtocolInvestigatorBean as value */
    private Hashtable investigatorData;
    
    /* holds complete Unit details for each investigator with personId as Key
     * and ProtocolInvestigatorUnitsBean as value */
    private Hashtable unitHashData;
    
    //holds the list selection model for unit table
    private ListSelectionModel unitSelectionModel = null;
    
    //holds the lilst selection model for persontable
    private ListSelectionModel perSelectionModel = null;
    
    //holds the connection string
    private String connectionURL = CoeusGuiConstants.CONNECTION_URL;
    
    /* holds the fucntion Type which specifies the mode in which the form has
     * been opened */
    private char functionType;
    
    // holds the string value used to display the Rolodex details
    private final String DISPLAY_TITLE = "DISPLAY ROLODEX";
    
    // holds character value used to specify that the form is opened in Add mode.
    private final char ADD_MODE = 'A';
    
    // holds character value used to specify that the form is opened in Modify mode.
    private final char MODIFY_MODE = 'M';
    
    // holds character value used to specify that the form is opened in Delete mode.
    private final char DISPLAY_MODE = 'D';

    // holds string value used to specify that the record has to be inserted.
    private final String INSERT_RECORD = "I";
    
    // holds string value used to specify that the record has to be updated.
    private final String UPADTE_RECORD = "U";
    
    /* holds string value used to specify that the record has to be marked as
     * deleted. */
    private final String DELETE_RECORD = "D";

    //holds the amend type falg
    private static final char AMEND_MODE = 'E';
    
    
    /* holds the collection of existing investigator beans when the form is opened
     * in MODIFY_MODE or DISPLAY_MODE */
    private Vector existingInvestigators;
    
    //holds the previously selected row index of person table.
    private int previousSelRow = -1;
    
    //flag used to determine whether the data is to be saved or not
    private boolean saveRequired = false;

    /* flag which specifies whether the particular investigator details 
     * have to stored in database with new sequence number so as to maintain 
     * history of changes made to a protocol investigator. */
    private boolean investigatorModified = false;

    private boolean createProposal=false;
    //holds CoeusMessageResources instance used for reading message Properties.
    private CoeusMessageResources coeusMessageResources;
    
    private String leadUnitNumber;
    private String irbProtocolNumber="";
    private int protocolSeqNumber;
    // Vector that holds the ComboBoxBean for the Affiliation - Senthil AR
    private Vector vecAffiliation;
    //The Affiliation ComboBox Senthil AR
    private JComboBox comboBox;
    private ProtocolInvestigatorsBean protocolInvBean;
    
    //Coeus enhancement 32.2 added by shiji - step 1 : start
    private boolean isNewRow = false;
    private static final char GET_TRAINIG_INFO = 'a';
    private static final char SEND_DELETE_NOTIFICATION = '5';
    //Coeus enhancement 32.2 - step 1 : end
    private CoeusUtils coeusUtils = CoeusUtils.getInstance();
   
    
    /** Default constructor which creates new form
     * <CODE>ProtocolInvestigatorsForm</CODE>.
     */
    public ProtocolInvestigatorsForm() {
    }
    
    /** Constructor which creates new form
     * <CODE>ProtocolInvestigatorsForm</CODE> with the specified <CODE>functionType</CODE> and
     * <CODE>investigators</CODE> data.
     * @param functionType character represents the mode in which the form is
     * opened.
     * @param investigators collection of <CODE>ProtocolInvestigatorsBean</CODE>s.
     */
    public ProtocolInvestigatorsForm( char functionType,
    Vector investigators, Vector vecAffiliation ) {
        this.functionType = functionType;
        this.vecAffiliation = vecAffiliation;
        existingInvestigators = new Vector();
        existingInvestigators = investigators;
        investigatorData = constructInvHashData( investigators );
    }
    
    /** This method is used to set the <CODE>investigators</CODE> collection object
     * @param investigators collection of <CODE>ProtocolInvestigatorsBean</CODE>s.
     */
    public void setInvestigatorData(Vector investigators){
        int selectedRow=0;
        int leadSelectedRow=0;
        if(tblPerson.getRowCount()>0)
        {
            selectedRow=tblPerson.getSelectedRow();
        }
        if(tblLead.getRowCount()>0)
        {
            leadSelectedRow=tblLead.getSelectedRow();
        }
        
        this.existingInvestigators = new Vector();
        this.existingInvestigators = investigators;
        investigatorData = constructInvHashData( investigators );
        setFormData();
        formatFields();
        setTableEditors();
//        if(tblPerson.getRowCount()>0)
//            tblPerson.setRowSelectionInterval(selectedRow, selectedRow);
//        if(tblLead.getRowCount()>0)
//            tblLead.setRowSelectionInterval(leadSelectedRow, leadSelectedRow);
    }
    
    /** This method is used to check the <CODE>saveRequired</CODE> flag.
     * @return boolean true is save required else false.
     */
    public boolean isSaveRequired(){
        return saveRequired;
    }
    
    //Added by Amit 11/21/2003
    /** This method use to implement focus on first editable component in this page.
     */
    public void setDefaultFocusForComponent(){
        //code modified for coeus4.3 enhancements that UI to be in display mode
        //when new amendment or renewal is created
//        if(!( functionType == CoeusGuiConstants.DISPLAY_MODE )) {        
        if(!( functionType == CoeusGuiConstants.DISPLAY_MODE ) && 
                !( functionType == AMEND_MODE )) {            
        
            if(tblPerson .getRowCount() > 0 ) {
                tblPerson.requestFocusInWindow();
                int prevSelectedRow=tblPerson.getSelectedRow();
                if(prevSelectedRow!=-1 && tblPerson.getRowCount() > prevSelectedRow){
                    tblPerson.setRowSelectionInterval(prevSelectedRow, prevSelectedRow);
                }
                else{
                    tblPerson.setRowSelectionInterval(0, 0);
                }
                tblPerson.setColumnSelectionInterval(1,1);
            }else{
                btnAdd.requestFocusInWindow();
            }            
        }
    }    
    //end Amit
    
    /** This method is used to set the <CODE>saveRequired</CODE> flag.
     * @param save boolean true if modifications are not saved, else false.
     */
    public void setSaveRequired(boolean save){
        this.saveRequired = save;
    }
    
    /** This method is used to set the <CODE>functionType</CODE> of this component.
     * @param functionType character which specifies the form opened mode.
     */
    public void setFunctionType(char functionType){
        this.functionType = functionType;
    }
    
    /** This method is used to initialize the form components,set the form
     * data and set the enabled status for all components depending on the
     * <CODE>functionType</CODE> specified while opening the form.
     *
     * @param mdiForm reference to the parent component <CODE>CoeusAppletMDIForm</CODE>
     * @return JComponent reference to the <CODE>ProtocolInvestigatorsForm</CODE> component
     * after initializing and setting the data.
     */
    public JComponent showProtocolInvestigatorsForm(final CoeusAppletMDIForm mdiForm){
        //this.mdiForm = mdiForm;
        initComponents();
        setFormData();
        formatFields();
        setTableEditors();
        
        coeusMessageResources = CoeusMessageResources.getInstance();
        //Added by manoj to fix the bug id #21 IRB-SystemTestingDL-01.xls 
        /**
         * Added the code
         * Updated by Raghunath P.V. july' 25 2003
         * To fix the id# DEF_05
         */
        tblPerson.addMouseListener(new MouseAdapter(){
            public void mouseClicked(MouseEvent me){
                int selRow = tblPerson.getSelectedRow();
                //Coeus enhancement 32.2 modified by shiji - step 2 : start
                boolean nonEmployee=((Boolean)tblPerson.getModel().
                                getValueAt(selRow,6)).booleanValue();
                String stId=(String)tblPerson.getModel().getValueAt(
                                                                selRow,5 );
                //Coeus enhancement 32.2 - step 2 : end
                if (me.getClickCount() == 2) {

                    if((stId != null) && (stId.trim().length()>0 )){
                        if(nonEmployee){
                            /* selected investigator is a rolodex, so show
                               rolodex details */
                            RolodexMaintenanceDetailForm frmRolodex 
                               = new RolodexMaintenanceDetailForm('V',stId);
                            frmRolodex.showForm(mdiForm,DISPLAY_TITLE,true);
                        }else{
                          try{
                              String loginUserName = mdiForm.getUserName();
                              //Bug Fix: Pass the person id to get the person details Start 1
                              //String personName=(String)tblPerson.getValueAt(selRow,1);
                              /*Bug Fix:to get the person details with the person id instead of the person name*/
                              //PersonInfoFormBean personInfoFormBean = (PersonInfoFormBean)coeusUtils.getPersonInfoID(personName);
                              //PersonDetailForm personDetailForm = 
                                //new PersonDetailForm(personInfoFormBean.getPersonID(),loginUserName,'D');
                              
                              PersonDetailForm personDetailForm = new PersonDetailForm(stId ,loginUserName,DISPLAY_MODE);
                              //Bug Fix: Pass the person id to get the person details End 1 
                              
                          }catch(Exception exception){
                              exception.printStackTrace();
                          }
                        }
                    }
                }
            }
        });
        /**
         * Added the code
         * Updated by Raghunath P.V. july' 25 2003
         * To fix the id# DEF_05
         */
        tblLead.addMouseListener(new MouseAdapter(){
            public void mouseClicked(MouseEvent me){
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
                    }
                }
            }
        });
        return this;
    }
    
    /**
     * This method is used to set the enabled status for the components
     * depending on the functionType specified.
     */
    private void formatFields(){
        //code modified for coeus4.3 enhancements that UI to be in display mode
        //when new amendment or renewal is created
//        boolean enabled = functionType != DISPLAY_MODE ? true : false ;
        boolean enabled = (functionType == DISPLAY_MODE || functionType == AMEND_MODE) ? false : true ;
        btnFindRolodex.setEnabled( enabled );
        btnDelete.setEnabled( enabled );
        btnAdd.setEnabled( enabled );
        btnFindPerson.setEnabled( enabled );
        btnFindUnit.setEnabled( enabled );
        btnAddUnit.setEnabled( enabled );
        //code added for coeus4.3 concurrent Amendments/Renewals enhancement 
        //To set the editable mode to the Unit Delete button
        btnDelUnit.setEnabled( enabled );
        if( tblPerson.getRowCount() < 1 ){
           btnDelete.setEnabled( false );
           btnAddUnit.setEnabled( false );
           btnFindUnit.setEnabled( false );
        }
        
        //Added by Amit 11/18/2003
        //code modified for coeus4.3 enhancements that UI to be in display mode
        //when new amendment or renewal is created
//        if(functionType == CoeusGuiConstants.DISPLAY_MODE){        
        if(functionType == CoeusGuiConstants.DISPLAY_MODE || functionType == AMEND_MODE){
            java.awt.Color bgListColor = (java.awt.Color)javax.swing.UIManager.getDefaults().get("Panel.background");
        
            tblPerson.setBackground(bgListColor);            
            tblLead.setBackground(bgListColor);
        
            tblPerson.setSelectionBackground(bgListColor );
            tblLead.setSelectionBackground(bgListColor );
            
            tblPerson.setSelectionForeground(Color.black);
            tblLead.setSelectionForeground(Color.black);
        }
        else{

            tblPerson.setBackground(Color.white);            
            tblLead.setBackground(Color.white);

            tblPerson.setSelectionBackground(Color.white);
            tblLead.setSelectionBackground(Color.white);
            
            tblPerson.setSelectionForeground(Color.black);
            tblLead.setSelectionForeground(Color.black);
        }
        //End Amit         
    }
    
    //supporting method to set this component data.
    private void setFormData(){
        if(existingInvestigators != null){
            /* if investigators available then set the person table data */
            ((DefaultTableModel)tblPerson.getModel()).setDataVector(personData,
                    getPersonTableColumnNames());
            if( tblPerson.getRowCount() > 0 ) {
                /* show the first investigators unit details */
                //Coeus enhancement 32.2 modified by shiji - step 3 : start
                String firstPerson =(tblPerson.getValueAt( 0,5 ) == null ? ""
                    : tblPerson.getValueAt( 0,5 ).toString());
                //Coeus enhancement 32.2 - step 3 : end
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
                    /* if there are no investigators present show empty table
                     * for investigators table */
                    ((DefaultTableModel)tblLead.getModel()).setDataVector(
                        new Object[][]{}, getUnitTableColumnNames().toArray());
            }
        }
        setTableEditors();
        saveRequired = false;
    }

    /** Constructor which creates new form
     * <CODE>ProtocolInvestigatorsForm</CODE> with the given parent component.
     * @param mdiParentForm reference to parent component, i.e., <CODE>CoeusAppletMDIForm</CODE>
     */
    public ProtocolInvestigatorsForm( CoeusAppletMDIForm mdiParentForm ) {
       // this.mdiForm = mdiParentForm;
    }
    
    /** This method is called from within the showProtocolInvestigatorsForm() to
     * initialize the form.
     */
    private void initComponents() {
        GridBagConstraints gridBagConstraints;
        
        btnAdd = new JButton();
        btnAdd.addActionListener( this );
        btnDelete = new JButton();
        btnDelete.addActionListener( this );
        btnDelete.setEnabled( false );
        
        btnFindRolodex = new JButton();
        btnFindRolodex.addActionListener( this );
        btnFindPerson = new JButton();
        btnFindPerson.addActionListener( this );
        scrPnPerson = new JScrollPane();
        tblPerson = new JTable();
        
        scrPnUnit = new JScrollPane();
        tblLead = new JTable();
        btnAddUnit = new JButton();
        btnAddUnit.addActionListener( this );
        btnDelUnit = new JButton();
        btnDelUnit.addActionListener( this );
        btnDelUnit.setEnabled( false );
        
        btnFindUnit = new JButton();
        btnFindUnit.addActionListener( this );
        
        // Added by chandra. To remove the external border.
        setLayout(new GridBagLayout());
        //Coeus enhancement 32.2 modified by shiji - step 4 : start
        setPreferredSize( new Dimension( 730,  410 ) );
        //Coeus enhancement 32.2 - step 4 : end
        // End
        //setPreferredSize( new Dimension( 600,  350 ) );  
       // setBorder(new EtchedBorder());    Removed External border.
        btnAdd.setFont(CoeusFontFactory.getLabelFont());
        btnAdd.setText("Add");
        btnAdd.setMnemonic('A');
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        add(btnAdd, gridBagConstraints);
        
        btnDelete.setFont(CoeusFontFactory.getLabelFont());
        btnDelete.setText("Delete");
        btnDelete.setMnemonic('D');
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
     
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        add(btnDelete, gridBagConstraints);
        
        btnFindRolodex.setFont(CoeusFontFactory.getLabelFont());
        btnFindRolodex.setText("Find Rolodex");
        btnFindRolodex.setMnemonic('R');
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        add(btnFindRolodex, gridBagConstraints);
        
        btnFindPerson.setFont(CoeusFontFactory.getLabelFont());
        btnFindPerson.setText("Find Person");
        btnFindPerson.setMnemonic('P');
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        add(btnFindPerson, gridBagConstraints);
        
     // Added by chandra - 02/09/2003 To make better Look&Feel
        scrPnPerson.setBorder(new EtchedBorder());
        tblPerson.setFont(CoeusFontFactory.getNormalFont());
        tblPerson.setModel( new DefaultTableModel(new Object[][]{},
            getPersonTableColumnNames().toArray()){
                //Coeus enhancement 32.2 modified by shiji - step 5 : start
                Class[] types = new Class [] {
                    Object.class,String.class,Boolean.class,Object.class,Object.class,
                    Object.class,Boolean.class
                };
                //Coeus enhancement 32.2 - step 5 : end
                public Class getColumnClass(int columnIndex) {
                    return types [columnIndex];
                }
                public boolean isCellEditable(int row, int col){
                    //code modified for coeus4.3 enhancements that UI to be in display mode
                    //when new amendment or renewal is created
//                    if( functionType == DISPLAY_MODE ){
                    if( functionType == DISPLAY_MODE || functionType == AMEND_MODE){
                        /* in display mode return all columns as non-editable */
                        return false;
                    }else {
                        /* In other modes person name column, PI column and 
                          faculty column are editable */
                        //Coeus enhancement 32.2 modified by shiji - step 6 : start
                        if( col == 0 || col == 5 || col == 6 || col == 4 ){
                        //Coeus enhancement 32.2 - step 6 : end
                            return false;
                        }else if(col == 1 || col == 3){
                            return true;
                        }
                        /* If any person/rolodex is selected then allow to edit
                           PI column otherwise not */
                        //Coeus enhancement 32.2 modified by shiji - step 7 : start
                        String personId = (String)tblPerson.getValueAt(row,5);
                        //Coeus enhancement 32.2 - step 7 : end
                        if(personId != null && personId.trim().length()>0){
                            return true;
                        }
                        return false;
                    }
                }
                public void setValueAt(Object value, int row, int col) {
                    if( col == 2 ){
                        //requird checking with old value before setting new value to table cell
                        // to fire save required flag.
                        boolean oldVal = ((Boolean) getValueAt(row,col)).booleanValue();
                        if( oldVal != ((Boolean)value).booleanValue()){
                            saveRequired = true;
                        }
                    }
                    super.setValueAt(value,row,col);                
                    fireTableCellUpdated(row,col);      
                    if(col == 2){
                       /* If setting PI as true for any row, then reset for
                          all other rows as false */     
                       boolean set = ((Boolean)value).booleanValue();
                       if(set){       
                           //Coeus enhancement 32.2 modified by shiji - step 8 : start
                           resetPIFlag((String)tblPerson.getValueAt(row,5)); 
                           //Coeus enhancement 32.2 - step 8 : end
                           //saveRequired = true;
                       }
                    }
                }
        });
      
        perSelectionModel = tblPerson.getSelectionModel();
        perSelectionModel.addListSelectionListener( this );
        perSelectionModel.setSelectionMode(
        ListSelectionModel.SINGLE_SELECTION );
        tblPerson.setSelectionModel( perSelectionModel );
        tblPerson.getTableHeader().setFont(CoeusFontFactory.getLabelFont());
        
        scrPnPerson.setViewportView(tblPerson);
        // Added by Chandra
        //Coeus enhancement 32.2 modified by shiji - step 9 : start
        scrPnPerson.setMinimumSize(new Dimension(160,130));
        scrPnPerson.setPreferredSize(new Dimension(160,130));
        //Coeus enhancement 32.2 - step 9 : end
        // End chandra
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        //gridBagConstraints.gridheight = 4;
        gridBagConstraints.gridheight = 4;
        gridBagConstraints.ipadx = 400;
        //gridBagConstraints.ipady = 75;
        gridBagConstraints.ipady = 75;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 10);
        add(scrPnPerson, gridBagConstraints);
        
        scrPnUnit.setBorder(new EtchedBorder());
        //scrPnUnit.setBorder(null);
        
        unitSelectionModel = tblLead.getSelectionModel();
        unitSelectionModel.addListSelectionListener( this );
        unitSelectionModel.setSelectionMode(
        ListSelectionModel.SINGLE_SELECTION );
        tblLead.setSelectionModel( unitSelectionModel );
        tblLead.setModel(new DefaultTableModel(new Object[][]{},
            getUnitTableColumnNames().toArray()){
                Class[] types = new Class [] {
                    java.lang.Object.class,  
                    java.lang.Boolean.class, java.lang.String.class,
                    java.lang.String.class, java.lang.String.class
                };

                public Class getColumnClass(int columnIndex) {
                    return types [columnIndex];
                }
                public boolean isCellEditable(int row, int col){
                    //code modified for coeus4.3 enhancements that UI to be in display mode
                    //when new amendment or renewal is created
//                    if( functionType == DISPLAY_MODE ){
                    if( functionType == DISPLAY_MODE || functionType == AMEND_MODE){
                        /* in display mode return all columns as non-editable */
                        return false;
                    }else {
                        /* In other modes Lead Unit column and Unit Name column 
                           are editable */
                        if(col == 2){
                            return true;
                        }else if(col == 0 || col == 3 || col == 4){
                            /* remaining colums are not editable */
                            return false;
                        }else if(tblPerson.getSelectedRow() != -1){
                            /* if any investigator row is selected and the
                               row is not blank, then allow to change the 
                               Lead Unit flag */
                            int selPersonRow = tblPerson.getSelectedRow();
                            boolean piFlag = ((Boolean)tblPerson.getValueAt(
                                                selPersonRow,2)).booleanValue();
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
                    if( col == 1 ){
                        //requird checking with old value before setting new value to table cell
                        // to fire save required flag.
                        boolean oldVal = ((Boolean) getValueAt(row,col)).booleanValue();
                        if( oldVal != ((Boolean)value).booleanValue()){
                            saveRequired = true;
                        }
                    }
                    super.setValueAt(value,row,col);  
                    fireTableCellUpdated(row,col);
                    if(col == 1){
                       /* If setting Lead Unit as true for any row, then reset 
                          for all other rows as false */     
                       boolean set = ((Boolean)value).booleanValue();
                       if(set){
                        resetLeadUnits(row);
                        //saveRequired = true;
                        leadUnitNumber = (String)tblLead.getValueAt(row,2);
                        }else{
                            leadUnitNumber = null;
                        }
                    }   
                }
        });
        
        tblLead.getTableHeader().setFont(CoeusFontFactory.getLabelFont());
        tblLead.setFont(CoeusFontFactory.getNormalFont());
        if( tblLead.getRowCount() > 0 ){
            btnDelUnit.setEnabled( true );
        }
        
        scrPnUnit.setViewportView(tblLead);
        // Added by chandra
        scrPnUnit.setMinimumSize(new Dimension(160,80));
        scrPnUnit.setPreferredSize(new Dimension(160,80));
        // End Chandra
        
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        //gridBagConstraints.gridheight = 3;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.ipadx = 400;
        gridBagConstraints.ipady = 95;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        //gridBagConstraints.insets = new java.awt.Insets(50, 0, 0, 0);
        // Added by chandra 02/09/03 - 
        gridBagConstraints.insets = new java.awt.Insets(10, 0, 0, 0);
        // end chandra
        add(scrPnUnit, gridBagConstraints);
        
        btnAddUnit.setFont(CoeusFontFactory.getLabelFont());
        btnAddUnit.setText("Add Unit");
        btnAddUnit.setMnemonic('n');
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(50, 0, 0, 0);
        add(btnAddUnit, gridBagConstraints);
            
        btnDelUnit.setFont(CoeusFontFactory.getLabelFont());
        btnDelUnit.setText("Delete Unit");
        btnDelUnit.setMnemonic('l');
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        add(btnDelUnit, gridBagConstraints);
        
        btnFindUnit.setFont(CoeusFontFactory.getLabelFont());
        btnFindUnit.setText("Find Unit");
        btnFindUnit.setMnemonic('U');
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        add(btnFindUnit, gridBagConstraints);
        // Added by Chandra 12/9/2003
        
        java.awt.Component[] components = {tblPerson,btnAdd,btnDelete,btnFindPerson,btnFindRolodex,tblLead,btnAddUnit,btnDelUnit,btnFindUnit};
        ScreenFocusTraversalPolicy  traversePolicy = new ScreenFocusTraversalPolicy( components );
        setFocusTraversalPolicy(traversePolicy);
        setFocusCycleRoot(true);        
    }

    //supporting method to set the Table Editors like Name editor, empty 
    //column editor and icon renderer.
    private void setTableEditors(){
        
        tblPerson.setOpaque(false);
        tblPerson.setShowVerticalLines(false);
        tblPerson.setShowHorizontalLines(false);
        tblPerson.setRowHeight(22);
        tblPerson.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        //tblPerson.setSelectionBackground(Color.white);
        //tblPerson.setSelectionForeground(Color.black);
        JTableHeader header = tblPerson.getTableHeader();
        header.setReorderingAllowed(false);
        //header.setResizingAllowed(false);

        //setting custom editor for Person name column of investigator table
        NameEditor nmEdtName = new NameEditor( "Person Name", 90 );
        TableColumn clmName = tblPerson.getColumnModel().getColumn(1);
        clmName.setCellEditor( nmEdtName );
        clmName.setPreferredWidth(345);        
        clmName.setMinWidth(345);

        // setting icon renderer to the first column of the investigator table
        clmName = tblPerson.getColumnModel().getColumn(0);
        clmName.setPreferredWidth(30);
        //clmName.setMaxWidth(30);
        clmName.setMinWidth(30);
        clmName.setHeaderRenderer(new EmptyHeaderRenderer());
        clmName.setCellRenderer(new IconRenderer());
        
        /* setting widths to PI column and Faculty column */
        clmName = tblPerson.getColumnModel().getColumn(2);
        clmName.setPreferredWidth(30);
        //clmName.setMaxWidth(30);
        clmName.setMinWidth(30);
        
        clmName.sizeWidthToFit();

        clmName = tblPerson.getColumnModel().getColumn(3);
        if ( functionType != DISPLAY_MODE ) {
            comboBox = new JComboBox(getDescriptions());
            comboBox.addItemListener(new ItemListener(){
                public void itemStateChanged(ItemEvent ie){
                    if(ie.getStateChange() == ItemEvent.SELECTED) {
                        saveRequired = true;
                    }
                }
            });
            comboBox.setFont(CoeusFontFactory.getNormalFont());
            clmName.setCellEditor(new DefaultCellEditor(comboBox ));
            
        }
        clmName.setPreferredWidth(90);
        //clmName.setMaxWidth(90);
        clmName.setMinWidth(90);
        //clmName.sizeWidthToFit();
        
        clmName = tblPerson.getColumnModel().getColumn(4);
        //Coeus enhancement 32.2 added by shiji - step 10 : start
        clmName.setPreferredWidth(60);
        //clmName.setMaxWidth(0);
        clmName.setMinWidth(60);
        clmName.setCellRenderer(new ImageRenderer());
        
        // to hide person id column
        clmName = tblPerson.getColumnModel().getColumn(5);
        clmName.setPreferredWidth(0);
        clmName.setMaxWidth(0);
        clmName.setMinWidth(0);
        
        // to hide Employee column
        clmName = tblPerson.getColumnModel().getColumn(6);
        //Coeus enhancement 32.2 - step 10 : end
        clmName.setPreferredWidth(0);
        clmName.setMaxWidth(0);
        clmName.setMinWidth(0);

        tblLead.setOpaque(false);   
        tblLead.setShowVerticalLines(false);
        tblLead.setShowHorizontalLines(false);
        tblLead.setRowHeight(22);
        tblLead.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        //tblLead.setSelectionBackground(Color.white);
        //tblLead.setSelectionForeground(Color.black);
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
        clmName.setPreferredWidth(45);
        //clmName.setMaxWidth(40);
        clmName.setMinWidth(45);
        //clmName.sizeWidthToFit();
        
        //setting custom editor to Unit Number column of units table 
        //NumberEditor numEdtName = new NumberEditor( "Number", 8 );
        
        /**
         * Updated For : REF ID 145 Feb' 14 2003
         * Org Unit entries appear to be limited to numerical entries. Can 
         * this be modified to include alphanumeric as well.
         * setted JTextFieldFilter.ALPHA_NUMERIC in NumberEditor
         * Updated by Subramanya Feb' 19 2003
         */
        
        NumberEditor numEdtName = new NumberEditor( "Number", 8 );
        clmName = tblLead.getColumnModel().getColumn(2);
        clmName.setCellEditor( numEdtName );
        clmName.setPreferredWidth(60);
        //clmName.setMaxWidth(60);
        clmName.setMinWidth(60);
        
        clmName = tblLead.getColumnModel().getColumn(3);       
        clmName.setPreferredWidth(215);        
        clmName.setMinWidth(215);
        
        clmName = tblLead.getColumnModel().getColumn(4);
        //Coeus enhancement 32.2 modified by shiji - step 11 : start
        clmName.setPreferredWidth(190);        
        clmName.setMinWidth(190);
        //Coeus enhancement 32.2 - step 11 : end

    }
    
    public Vector getDescriptions(){
        Vector descVector = new Vector();
        //Vector desc = vecAffiliation;
        if(vecAffiliation != null){
            int listSize = vecAffiliation.size();
            for(int index = 0; index < listSize; index ++){
                descVector.addElement(((ComboBoxBean)vecAffiliation.elementAt(index)).toString());
            }
        }
        return descVector;
    }
    
    private int getAffiliationId( String desc ) {
        ComboBoxBean bean;
        if(vecAffiliation!=null){
        for( int indx = 0; indx < vecAffiliation.size(); indx++) {
            bean = (ComboBoxBean) vecAffiliation.get(indx);
            if( bean.getDescription().equalsIgnoreCase( desc ) ) {
                return Integer.parseInt( bean.getCode() );
            }
        }
        }
        return 0;
    }
    
    private void updateAffiliateType() {
        Enumeration enumKeys = investigatorData.keys();
        while (enumKeys.hasMoreElements() ) {
            protocolInvBean  = (ProtocolInvestigatorsBean)investigatorData.get(enumKeys.nextElement()); 
            protocolInvBean.addPropertyChangeListener( new PropertyChangeListener(){
                public void propertyChange(PropertyChangeEvent pce){
                    /*System.out.println("propName:"+pce.getPropertyName());
                    System.out.println("oldVal:"+pce.getOldValue()+":");
                    System.out.println("newVal:"+pce.getNewValue()+":");*/
                    if ( pce.getNewValue() == null && pce.getOldValue() != null ) {
                        saveRequired = true;
                    }
                    if( pce.getNewValue() != null && pce.getOldValue() == null ) {
                        saveRequired = true;
                    }
                    if( pce.getNewValue()!=null && pce.getOldValue()!=null ) {
                        if (!(  pce.getNewValue().toString().trim().equalsIgnoreCase(pce.getOldValue().toString().trim())))  {
                            saveRequired = true;
                        }
                    }
                    if( functionType == MODIFY_MODE ) {
                        protocolInvBean.setAcType(UPADTE_RECORD);
                    }
                }
            });
            
            for( int indx = 0; indx < tblPerson.getRowCount(); indx++ ) {
                //Coeus enhancement 32.2 modified by shiji - step 12 : start
                String perId = (String) tblPerson.getValueAt(indx,5);
                //Coeus enhancement 32.2 - step 12 : end
                if( protocolInvBean.getPersonId().equalsIgnoreCase( perId ) ){
                    String affDesc = (String)tblPerson.getValueAt( indx, 3);                
                    protocolInvBean.setAffiliationTypeCode( getAffiliationId( affDesc ) );
                    break;
                }
            }
        }
    }
    
    public void setInvestigatorOnCeateProtocl(boolean creatteProposal){
        createProposal=creatteProposal;
    }
    /** This method is used to get the Investigators information along with
     * the unit details entered for each Investigator.
     * @return Vector collection of <CODE>ProtocolInvestigatorsBean</CODE>.
     */    
    public Vector getFormData(){
        Vector newInvestigatorsData = null;
        /* In modify mode loop through all exisiting investigators and check
           whether any investigators have been deleted. If yes, send that
           investigator bean with acType as DELETE_RECORD.  */    
        if( investigatorData != null && tblPerson.getRowCount() > 0 ) {
            updateAffiliateType();
        }
        if (existingInvestigators != null && functionType == 'M' ) {
            newInvestigatorsData = new Vector();
            for(int invCount = 0 ; invCount < existingInvestigators.size() ;
                    invCount++) {
                ProtocolInvestigatorsBean protocolInvBean 
                    = (ProtocolInvestigatorsBean)
                        existingInvestigators.get(invCount);
                if (investigatorData != null ) {
                    //System.out.println("investigator present");
                    if ( investigatorData.containsKey(
                                             protocolInvBean.getPersonId()) ) {
                        //System.out.println("exsitingInv contains :"+protocolInvBean.getPersonId());                     
                        protocolInvBean.setInvestigatorUnits(
                                                  getUnitData(protocolInvBean));
                        //System.out.println("sets unit data....");
                        if(investigatorModified){
                            /* for any existing investigator if his units have
                               been modified then send that bean with acType as
                               UPDATE_RECORD  */
                            //System.out.println("inv modified due to unit change");
                            protocolInvBean.setAcType(UPADTE_RECORD);
                            saveRequired = true;
                        }
                    }else {
                        protocolInvBean.setAcType(DELETE_RECORD);
                        protocolInvBean.setInvestigatorUnits(null);
                        saveRequired = true;
                    }
                }
                newInvestigatorsData.add(protocolInvBean);
            }
        }
        /* compare all existing investigators with new set of investigators */
        if (investigatorData != null ) {
            //System.out.println("investigatorData != null");
            if(newInvestigatorsData == null){
                newInvestigatorsData = new Vector();
            }
            Enumeration enumKeys = investigatorData.keys();
            while (enumKeys.hasMoreElements() ) {
                boolean found = false;
                ProtocolInvestigatorsBean protocolInvBean  
                    = (ProtocolInvestigatorsBean)investigatorData.get(
                                                        enumKeys.nextElement()); 
                protocolInvBean.setInvestigatorUnits(
                                                  getUnitData(protocolInvBean));
                //System.out.println("inv:"+protocolInvBean);
                //System.out.println("invModified?"+investigatorModified);
                if ((existingInvestigators != null )&&  functionType == 'M' ) {
                    for(int invCount = 0 ; invCount < existingInvestigators.size() ;
                            invCount++) {
                        ProtocolInvestigatorsBean exInvBean = (ProtocolInvestigatorsBean) 
                            existingInvestigators.get(invCount);
                        if(protocolInvBean.getPersonId().equals(
                                exInvBean.getPersonId())){
                            found = true;
                            break;
                        }
                    }
                }
                //code modified for coeus4.3 enhancements that UI to be in display mode
                //when new amendment or renewal is created                
//                if(!found){
                if(!found && functionType != DISPLAY_MODE){
                    /* if any investigator has been added newly send that bean
                       with acType as INSERT_RECORD */
                    
                    protocolInvBean.setAcType(INSERT_RECORD);
                    saveRequired = true;
                    newInvestigatorsData.add(protocolInvBean);
                    //System.out.println("adding record:"+protocolInvBean.getPersonId()+" acType:"+protocolInvBean.getAcType());
                }
                else if(createProposal && functionType==DISPLAY_MODE){
                    protocolInvBean.setAcType(INSERT_RECORD);
                    saveRequired = true;
                    newInvestigatorsData.add(protocolInvBean);
                }
            }
        }        
        return setAcTypesInAmend(newInvestigatorsData);
    }
    
    private Vector setAcTypesInAmend( Vector existingInv ) {
        Vector newInv = new Vector();
        ProtocolInvestigatorsBean invBean;
            if( functionType == AMEND_MODE ) {
                if( existingInv != null ) {
                    int count = existingInv.size();
                    for( int indx =0 ; indx < count; indx++){
                        invBean = ( ProtocolInvestigatorsBean ) existingInv.elementAt(indx);
                        if( invBean.getAcType() == null || !invBean.getAcType().equalsIgnoreCase( DELETE_RECORD ) ) {
                            invBean.setAcType( INSERT_RECORD );
                            Vector invUnits = invBean.getInvestigatorUnits();
                            if( invUnits != null ) {
                                for ( int unitIndx =0 ; unitIndx < invUnits.size(); unitIndx++){
                                    ProtocolInvestigatorUnitsBean protocolUnitBean  
                                       = (ProtocolInvestigatorUnitsBean)invUnits.get(unitIndx);
                                    if( protocolUnitBean.getAcType() == null || 
                                            !protocolUnitBean.getAcType().equalsIgnoreCase( DELETE_RECORD ) ) {
                                        protocolUnitBean.setAcType( INSERT_RECORD );
                                        invUnits.set(unitIndx, protocolUnitBean);
                                    }
                                }
                            }
                            newInv.addElement( invBean );
                        }
                    }
                }
            }else{
                newInv = existingInv;
            }
        return newInv;
    }
    //supporting method to get the Unit Detials for the specified investigator.
    private Vector getUnitData(ProtocolInvestigatorsBean invBean){
        investigatorModified = false;
        Vector newUnitsData = null;
        if  (invBean != null) {
            newUnitsData = new Vector();
            Vector bnUnits  = invBean.getInvestigatorUnits();
            Vector unitData = (Vector)unitHashData.get(invBean.getPersonId());
            /* loop through all available units for specified investigator and
               check whether any unit has been modified */
            if ((bnUnits != null) && (bnUnits .size() >0 )  && functionType != 'A') {         
                for (int bnUnitCount = 0; 
                                 bnUnitCount < bnUnits.size(); bnUnitCount++ ){
                    final ProtocolInvestigatorUnitsBean bnUnit = 
                        (ProtocolInvestigatorUnitsBean)bnUnits.get(bnUnitCount);
                    boolean found =false;
                    if (unitData != null && unitData.size() > 0) {                        
                        for ( int unitCount =0 ;unitCount < unitData.size() ; 
                                                                  unitCount++){
                            ProtocolInvestigatorUnitsBean protocolUnitBean  
                               = (ProtocolInvestigatorUnitsBean)
                                unitData.get(unitCount);
                            if (bnUnit.getUnitNumber().equalsIgnoreCase(
                                            protocolUnitBean.getUnitNumber())) {
                                found =true;
                                //System.out.println("bnUnit:"+bnUnit.isLeadUnitFlag());
                                //System.out.println("protoUnit:"+protocolUnitBean.isLeadUnitFlag() );
                                if(bnUnit.isLeadUnitFlag() 
                                        != protocolUnitBean.isLeadUnitFlag()){
                                    /* if lead unit flag is modified then mark
                                       the bean acType to UPDATE_RECORD */        
                                    bnUnit.setAcType(UPADTE_RECORD);
                                    //System.out.println("unit bean updated:"+bnUnit.getUnitNumber());
                                    //System.out.println("acType:"+bnUnit.getAcType());
                                    saveRequired = true;
                                    if(functionType == MODIFY_MODE){
                                        investigatorModified = true;
                                    }
                                }
                                bnUnit.setLeadUnitFlag(
                                            protocolUnitBean.isLeadUnitFlag());
                                //Added for COEUSQA-2879_LOG_IACUC and IRB_Correspondents do not auto-populate-Start
                                if(TypeConstants.DELETE_RECORD.equals(bnUnit.getAcType())
                                   || isSaveRequired()){
                                    if(TypeConstants.DELETE_RECORD.equals(bnUnit.getAcType())){
                                    bnUnit.setAcType(null);
                                    }
                                    bnUnit.setCanLoadCorrepondents(true);
                                }
                                //Added for COEUSQA-2879_LOG_IACUC and IRB_Correspondents do not auto-populate-End
                            }
                        }
                    }
                    if (!found) {
                        /* if existing unit is not present in current units list
                           then mark that bean as DELETE_RECORD and send to db */
                        bnUnit.setAcType(DELETE_RECORD);
                        saveRequired = true;
                        if(functionType == MODIFY_MODE){
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
                    ProtocolInvestigatorUnitsBean protocolUnitBean = 
                         (ProtocolInvestigatorUnitsBean)unitData.get(unitCount);
                    boolean  found = false;
                    if ((bnUnits != null) && (bnUnits .size() >0 ) ) {
                        for (int bnUnitCount = 0; bnUnitCount < bnUnits.size(); 
                                                                bnUnitCount++ ){
                            ProtocolInvestigatorUnitsBean bnUnit = 
                                    (ProtocolInvestigatorUnitsBean)
                                                        bnUnits.get(bnUnitCount);
                            if (protocolUnitBean.getUnitNumber().
                                    equalsIgnoreCase(bnUnit.getUnitNumber())){
                                found = true;
                            }
                        }
                    }
                    if (!found || functionType == 'A') {
                        /* if any unit has been newly added set acType to that
                           bean as INSERT_RECORD  and send to db */
                        protocolUnitBean.setAcType(INSERT_RECORD);
                        saveRequired = true;
                        if(functionType == MODIFY_MODE){
                            investigatorModified = true;
                        }
                        newUnitsData.add(protocolUnitBean);
                    }
                }
            }
        }        
        return newUnitsData;
    }

    /** This method is used to validate the mandatory fields of this form.
     * @throws Exception if mandatory fields are not specified or
     * validation check failed for these fields.
     * @return true if data in all mandatory fields are valid, else false.
     */
    public boolean validateData() throws Exception, CoeusUIException{
        if(tblPerson.getRowCount()>0){
            int perRowCount = tblPerson.getRowCount();
            boolean piPresent = false;
            boolean pi = false;
            boolean leadUnitPresent = false;
            String selPersonId="";
            String selPersonName = "";
            Vector perUnits = null;
            ProtocolInvestigatorUnitsBean perUnitBean 
                = new ProtocolInvestigatorUnitsBean();
            
           validateFormData();           
            
            /* for all investigator rows there should be atleast one unit 
               associated with it. And only one investigator should be PI */
            for(int perRow = 0 ; perRow < perRowCount ; perRow++ ){
                //Coeus enhancement 32.2 modified by shiji - step 13 : start
               selPersonId = (String)tblPerson.getValueAt(perRow,5);
               //Coeus enhancement 32.2 - step 13 : end
               selPersonName = (String)tblPerson.getValueAt(perRow,1);
               pi = ((Boolean)tblPerson.getValueAt(perRow,2)).booleanValue();
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
                                perUnitBean = (ProtocolInvestigatorUnitsBean)
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
            
        }else{
            /* if atleast one investigator is not specified then show error msg
               and return validity false */
            log(coeusMessageResources.parseMessageKey(
                                            "protoInvFrm_exceptionCode.1065"));            
            return false;
        }
        return true;
    }
    
    /** This method is used for validations.
     * After adding a row,user must enter a person name without keeping it blank.
     * @return true if the validation succeed, false otherwise.
     * @throws Exception a exception to be thrown in the client side.
     */    
    public boolean validateFormData() throws Exception, CoeusUIException{
        
             boolean valid=true;
            int rowCount = tblPerson.getRowCount();
            for(int inInd=0; inInd < rowCount ;inInd++){
                String stPersonId=(String)((DefaultTableModel)tblPerson.getModel()).getValueAt(inInd,1);
                if((stPersonId == null) || (stPersonId.trim().length() <= 0)){
                    tblPerson.setRowSelectionInterval(inInd,inInd);
                    log(coeusMessageResources.parseMessageKey("protoInvFrm_exceptionCode.1142"));
                    break;
                }
            }            
        return true;
    }    
    
    //supporting method to throw exceptions during validation check.
    private void log(String mesg) throws CoeusUIException{
       //Commented by sharath(20 - Aug - 2003)
        //throw new Exception(mesg);
        
        //Bug Fix ( Defect Id : 379)
        CoeusUIException coeusUIException = new CoeusUIException(mesg,CoeusUIException.WARNING_MESSAGE);
        coeusUIException.setTabIndex(1);
        throw coeusUIException;
        //Bug Fix ( Defect Id : 379)
    }     
    
    //supporting method to construct unit table data for a specified 
    // investigator id
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
        ProtocolInvestigatorUnitsBean investigatorUnitsBean = null;
        
        for( int indx = 0; indx <  unitDataBean.size(); indx++ ){
            try{
                // construct vector of vectors from unitBeans 
                investigatorUnitsBean
                =  ( ProtocolInvestigatorUnitsBean ) unitDataBean.get( indx );
                rowData = new Vector();
                rowData.add( "");
                rowData.add( new Boolean( investigatorUnitsBean.isLeadUnitFlag() ));
                if( investigatorUnitsBean.isLeadUnitFlag() ) {
                    leadUnitNumber = investigatorUnitsBean.getUnitNumber();
                }
                rowData.add( investigatorUnitsBean.getUnitNumber() );
                rowData.add( investigatorUnitsBean.getUnitName() );
                rowData.add( investigatorUnitsBean.getPersonName() );
                unitTableData.add( rowData );
                investigatorUnitsBean = null;
                rowData = null;
            }catch( Exception err ){
                err.printStackTrace();
            }
        }
        
        return unitTableData;
        
    }
    
    
    //supporting method to construct and return the Unit Table Column headers
    private Vector getUnitTableColumnNames(){
        
        Vector unitHeaders = new Vector();
        unitHeaders.add( "Sl No" );
        unitHeaders.add( "Lead"  );
        unitHeaders.add( "Number");
        unitHeaders.add( "Name"  );
        unitHeaders.add( "OSP Admin" );
        
        return unitHeaders ;
    }
    
    
    //supporting method to construct and return the Person Table Column headers
    private Vector getPersonTableColumnNames(){
        
        Vector personHeaders = new Vector();
        personHeaders.add( "Sl No");
        personHeaders.add( "Person Name" );
        personHeaders.add( "PI" );
        personHeaders.add( "Affiliation" );
        //Coeus enhancement 32.2 added by shiji - step 14 : start
        personHeaders.add( "Training" );
        //Coeus enhancement 32.2 - step 14 : end
        personHeaders.add( "" );
        personHeaders.add( "Non employee" );
        return personHeaders ;
    }
    
    //supporting method to construct hash table from the data vector(bean)
    private Hashtable constructInvHashData( Vector dataBean ){
        Hashtable newData = new Hashtable();
        unitHashData = new Hashtable();
        personData = new Vector();
        if( dataBean == null ){
            return newData;
        }
        
        ProtocolInvestigatorsBean protocolInvestigatorBean = null;
        
        Vector personTableRow = null;
        String personId = null;
        Vector unDetail = null;
        
        for( int indx = 0; indx < dataBean.size() ; indx ++ ){
            try{
                // construct table data from vector of investigator beans
                protocolInvestigatorBean = new ProtocolInvestigatorsBean();
                protocolInvestigatorBean = ( ProtocolInvestigatorsBean ) 
                                                          dataBean.get( indx );
               if( protocolInvestigatorBean != null){
                    personId  = protocolInvestigatorBean.getPersonId();
                     //code added to get selected protocol number,sequence number..starts
                    irbProtocolNumber=protocolInvestigatorBean.getProtocolNumber();
                    protocolSeqNumber=protocolInvestigatorBean.getSequenceNumber();
                    //code added to get selected protocol number,sequence number..ends
                     //Coeus enhancement 32.2 added by shiji - step 15 : start
                     protocolInvestigatorBean.setTrainingFlag(getTrainingInfo(personId));
                     //Coeus enhancement 32.2 - step 15 : end
                    /* insert each investigator bean into hastable with personId
                      as key and investigator bean as value */
                    newData.put( personId, protocolInvestigatorBean );
                    personTableRow = new Vector();
                    personTableRow.add( "");
                    personTableRow.add(
                                      protocolInvestigatorBean.getPersonName());
                    personTableRow.add( new Boolean(
                    protocolInvestigatorBean.isPrincipalInvestigatorFlag() ));
                    /*
                    personTableRow.add(
                    new Boolean( protocolInvestigatorBean.isFacultyFlag() ));
                     */
                    personTableRow.add(protocolInvestigatorBean.getAffiliationTypeDescription());
                    //Coeus enhancement 32.2 added by shiji - step 16 : start
                    personTableRow.add(new Boolean(protocolInvestigatorBean.isTrainingFlag()));
                    //Coeus enhancement 32.2 - step 16 : end
                    //System.out.println("aff desc:");
                    personTableRow.add(personId);
                    personTableRow.add(
                        new Boolean(
                            protocolInvestigatorBean.isNonEmployeeFlag()));
                    unDetail = new Vector();
                    unDetail = protocolInvestigatorBean.getInvestigatorUnits();
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
    //Coeus enhancement 32.2 - step 17 : start
    private boolean getTrainingInfo(String personId){
        boolean isTrain = false;
        RequesterBean requesterBean = new RequesterBean();
        ResponderBean responderBean = new ResponderBean();
        requesterBean.setDataObject(personId);
        requesterBean.setFunctionType(GET_TRAINIG_INFO);
        
        String connectTo = connectionURL +"/protocolMntServlet";
        
        AppletServletCommunicator comm = new AppletServletCommunicator
        (connectTo, requesterBean);
        comm.setRequest(requesterBean);
        comm.send();
        responderBean = comm.getResponse();
        if(responderBean != null){
            if(responderBean.isSuccessfulResponse()) {
                isTrain = ((Boolean)responderBean.getDataObject()).booleanValue();
            }
        }
        return isTrain;
    }
    //Coeus enhancement 32.2 - step 17 : end
    
    /** This method is used to handle the Action Event delegated by <CODE>Find Person,
     * Find Rolodex, Find Unit, Add Unit, Add Person, Delete Person </CODE>and
     * <CODE>Delete Unit</CODE> buttons
     * @param btnActionEvent <CODE>ActionEvent</CODE>, a semantic event which indicates that a
     * component-defined action occured.
     */
    public void actionPerformed( ActionEvent btnActionEvent ){
        
        CoeusSearch protocolSearch = null;
        Object actionSource = btnActionEvent.getSource();
        int selectedRow = tblPerson.getSelectedRow();
        /* when any of the buttons fires actionperformed event and if investigator
          table is still editing the person name column then take the value from
          editor component of that cell and store the value in that cell and
          continue with corresponding actionperformed event */
        if(tblPerson.isEditing() && tblPerson.getEditingColumn() == 1){
            String value = null;
            value = ((javax.swing.text.JTextComponent)
            tblPerson.getEditorComponent()).getText();
            if( (value != null)){
                tblPerson.setValueAt(value,selectedRow,1);
            }
            tblPerson.getCellEditor().cancelCellEditing();
        }
        
        
        
        if( actionSource.equals( btnFindPerson ) ) {
            // FindPerson button is clicked , show person search 
            try{ 
                /**
                 * Updated For : REF ID 149 Feb' 14 2003
                 * Person Search allows for multiple entries, however, 
                 * the user can only add 1 at a time
                 *
                 * Updated by Subramanya Feb' 17 2003
                 */
                protocolSearch = new CoeusSearch( mdiForm, "PERSONSEARCH",
                CoeusSearch.TWO_TABS_WITH_MULTIPLE_SELECTION ); //TWO_TABS ) ;
                protocolSearch.showSearchWindow();
                Vector vSelectedPersons = protocolSearch.getMultipleSelectedRows(); 
                if ( vSelectedPersons != null ){
                HashMap singlePersonData = null;
                for(int indx = 0; indx < vSelectedPersons.size(); indx++ ){
                    
                    singlePersonData = (HashMap)vSelectedPersons.get( indx ) ;                    
                
                /*HashMap result = protocolSearch.getSelectedRow();
                if( result == null || result.isEmpty() ){
                    return;
                }*/
                if( singlePersonData == null || singlePersonData.isEmpty() ){
                    continue;
                }
                String personId = checkForNull(singlePersonData.get( "PERSON_ID" ));//result.get( "PERSON_ID" )) ;
                String personName = checkForNull(singlePersonData.get( "FULL_NAME" ));//result.get( "FULL_NAME" )) ;
                String homeUnit = checkForNull( singlePersonData.get( "HOME_UNIT" ));//result.get( "HOME_UNIT" ));
                //Coeus enhancement 32.2 added by shiji - step 18 : start
                boolean isTraining = getTrainingInfo(personId);
                isNewRow=false;
                //Coeus enhancement 32.2 - step 18 : end
                /*boolean faculty 
                    = checkForNull(result.get("IS_FACULTY")).equalsIgnoreCase(
                        "y") ? true : false;*/
                boolean faculty 
                    = checkForNull(singlePersonData.get("IS_FACULTY")).
                      equalsIgnoreCase("y") ? true : false;
                
                // construct bean from search result information        
                ProtocolInvestigatorsBean investigator =
                                               new  ProtocolInvestigatorsBean();
                investigator.setPersonId(personId);
                investigator.setPersonName(personName);
                investigator.setPrincipalInvestigatorFlag(false); 
                investigator.setFacultyFlag(faculty);
                investigator.setAffiliationTypeCode( faculty ? 1 : 3 );
                investigator.setAffiliationTypeDescription( faculty ? "Faculty" : "Affiliate");
                investigator.setNonEmployeeFlag(false);
                //Coeus enhancement 32.2 added by shiji - step 19 : start
                investigator.setTrainingFlag(isTraining);
                //Coeus enhancement 32.2 - step 19 : end
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
            
        }else if( actionSource.equals( btnFindRolodex ) ) {
            // FindRolodex clicked, show Search Rolodex
            try{
                /**
                 * Updated For : REF ID 149 Feb' 14/19 2003
                 * Person/Rolodex Search allows for multiple entries, however, 
                 * the user can only add 1 at a time
                 *
                 * Updated by Subramanya Feb' 20 2003
                 */
                protocolSearch = new CoeusSearch( mdiForm, "ROLODEXSEARCH",
                CoeusSearch.TWO_TABS_WITH_MULTIPLE_SELECTION ) ;
                //CoeusSearch.TWO_TABS ) ;
                protocolSearch.showSearchWindow();
                Vector vSelectedRolodex = protocolSearch.getMultipleSelectedRows(); 
                if ( vSelectedRolodex != null ){
                    
                HashMap singleRolodexData = null;
                for(int indx = 0; indx < vSelectedRolodex.size(); indx++ ){
                    
                    singleRolodexData = (HashMap)vSelectedRolodex.get( indx ) ;      
                    
                /*HashMap result = protocolSearch.getSelectedRow();
                if( result == null || result.isEmpty() ){
                    return;
                }*/
                if( singleRolodexData == null || singleRolodexData.isEmpty() ){
                    continue;
                }
                    
                String rolodexID = checkForNull(singleRolodexData.get( "ROLODEX_ID" ));//result.get( "ROLODEX_ID" ));
                String firstName = checkForNull(singleRolodexData.get( "FIRST_NAME" ));//result.get( "FIRST_NAME" ));
                String lastName = checkForNull(singleRolodexData.get( "LAST_NAME" ));//result.get( "LAST_NAME" ));
                String middleName = checkForNull(singleRolodexData.get( "MIDDLE_NAME" ));//result.get( "MIDDLE_NAME" ));
                String namePreffix = checkForNull(singleRolodexData.get( "PREFIX" ));//result.get( "PREFIX" ));
                String nameSuffix = checkForNull(singleRolodexData.get( "SUFFIX" ));//result.get( "SUFFIX" ));
                String rolodexName = null;
                //Coeus enhancement 32.2 added by shiji - step 20 : start
                boolean isTraining = getTrainingInfo(rolodexID);
                isNewRow=false;
                //Coeus enhancement 32.2 - step 20 : end
                /* construct full name of the rolodex if his last name is present
                  otherwise use his organization name to display in person name
                  column of investigator table */
                if ( lastName.length() > 0) {
                    rolodexName = ( lastName + " "+nameSuffix +", "+ namePreffix
                    +" "+firstName +" "+ middleName ).trim();
                } else {
                    rolodexName = checkForNull(singleRolodexData.get("ORGANIZATION"));//result.get("ORGANIZATION"));
                }
                ProtocolInvestigatorsBean investigator = 
                                                new ProtocolInvestigatorsBean();
                investigator.setPersonId(rolodexID);
                investigator.setPersonName(rolodexName);
                investigator.setPrincipalInvestigatorFlag(false);
                investigator.setFacultyFlag(false);
                investigator.setAffiliationTypeCode(3);
                investigator.setAffiliationTypeDescription("Affiliate");
                //Coeus enhancement 32.2 added by shiji - step 21 : start
                investigator.setTrainingFlag(isTraining);
                //Coeus enhancement 32.2 - step 21 : end
                // as rolodex is not employee so set non-employee flag true
                investigator.setNonEmployeeFlag(true);
                /* as rolodex doesn't have any unit information by default, send
                  null for homeUnit parameter. Send false to replaceRow as we 
                  have to create a new row for each investigator entry. */
                setInvestigatorBean(investigator,null,false);
                } //end of for loop
                }//end of vSelectedRolodex != null
            }catch( Exception err ){
                err.printStackTrace();
            }
        }else if( actionSource.equals( btnFindUnit ) ) {
            try{
                int selectedUnitRow = tblLead.getSelectedRow();
                String unitID = null;
                
                // check whether any investigator has been selected or not
                if(selectedRow != -1){ 
                    //Coeus enhancement 32.2 modified by shiji - step 22 : start
                    String pId =  (String)tblPerson.getValueAt(selectedRow,5);
                    //Coeus enhancement 32.2 - step 22 : end
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
                        
                        /**
                         * Updated For : REF ID 149 Feb' 14/19 2003
                         * Person/Rolodex/LeadUnit Search allows for multiple entries, however, 
                         * the user can only add 1 at a time
                         *
                         * Updated by Subramanya Feb' 20 2003
                         */
                        protocolSearch = new CoeusSearch( mdiForm, 
                                                            "LEADUNITSEARCH",
                                CoeusSearch.TWO_TABS_WITH_MULTIPLE_SELECTION ) ;  
                                      //                  CoeusSearch.TWO_TABS ) ;
                        protocolSearch.showSearchWindow();
                        
                        Vector vSelectedUnit = protocolSearch.getMultipleSelectedRows(); 
                
                        if ( vSelectedUnit != null ){

                        HashMap singleLeadUnitData = null;
                        for(int indx = 0; indx < vSelectedUnit.size(); indx++ ){
                    
                            singleLeadUnitData = (HashMap)vSelectedUnit.get( indx ) ;   
                    
                        /*HashMap result = protocolSearch.getSelectedRow();
                        if( result == null || result.isEmpty() ){
                            return;
                        }*/
                        if( singleLeadUnitData == null || singleLeadUnitData.isEmpty() ){
                            continue;
                        }
                            
                        unitID = checkForNull(singleLeadUnitData.get( "UNIT_NUMBER" ));//result.get( "UNIT_NUMBER" ));
                        String unitName = checkForNull(singleLeadUnitData.get( "UNIT_NAME" ));//result.get( "UNIT_NAME" ));
                        String adminPerson = checkForNull(singleLeadUnitData.get( "ADMINTR" ));//result.get( "ADMINTR" ));

                        String selPersonName 
                            = tblPerson.getValueAt( selectedRow, 2).toString();
                        /* check for duplicate unit number for a particular
                          investigator */
                        if( !isDuplicateUnitID( pId, unitID )) {
                            saveRequired = true;
                            /* if not duplicate construct unit bean and add to
                              unit hashtable */
                            ProtocolInvestigatorUnitsBean unitBean = null;
                            unitBean = new ProtocolInvestigatorUnitsBean();
                            unitBean.setPersonId(pId);
                            unitBean.setPersonName(adminPerson);
                            unitBean.setUnitNumber(unitID);
                            unitBean.setUnitName(unitName);
                            unitBean.setLeadUnitFlag(false);
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
                                   tblLead.setValueAt( adminPerson , 
                                    selectedUnitRow, 4 );
                                }else{
                                    // otherwise add a new row
                                    Vector newData = new Vector();
                                    newData.add( "");
                                    newData.add( new Boolean( false ) );
                                    newData.add( unitID );
                                    newData.add( unitName );
                                    newData.add( adminPerson );
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
                                newData.add( adminPerson );
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
        }else if( actionSource.equals( btnAdd ) ) {
            // add a blank row to investigator table
            Vector newData = new Vector();
            //Coeus enhancement 32.2 added by shiji - step 23 : start
            isNewRow = true;
            //Coeus enhancement 32.2 - step 23 : end
            int rowCount = tblPerson.getRowCount() ;
            newData.add( "");
            newData.add( "" );
            if(rowCount == 0){
                /* if the row being added is the first row select this row
                  as PI */
                newData.add( new Boolean( true ) );
            }else{
                newData.add( new Boolean( false ) );
            }
            newData.add("Affiliate");
            //Coeus enhancement 32.2 added by shiji - step 24 : start
            newData.add( new Boolean( false ) );
            //Coeus enhancement 32.2 - step 24 : end
           // newData.add("");
            newData.add("");
            newData.add( new Boolean( false ) );
            // add empty data to unit table 
            ((DefaultTableModel)tblPerson.getModel()).addRow( newData );
            tblPerson.setRowSelectionInterval(rowCount,rowCount);
            ((DefaultTableModel)tblLead.getModel()).setDataVector(
                        new Object[][]{}, getUnitTableColumnNames().toArray());

            setTableEditors();
            rowCount = tblPerson.getRowCount() -1 ;
            tblPerson.scrollRectToVisible(tblPerson.getCellRect(rowCount ,
                0, true));
            btnDelete.setEnabled( true );
            tblPerson.requestFocusInWindow();
            tblPerson.editCellAt(rowCount,1);
            tblPerson.getEditorComponent().requestFocusInWindow();
            saveRequired = true;
        }else if( actionSource.equals( btnAddUnit ) ) {
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
                //Coeus enhancement 32.2 modified by shiji - step 25 : start
                String pId =  (String)tblPerson.getValueAt(selectedRow,5);
                //Coeus enhancement 32.2 - step 25 : end
                if(pId != null && pId.trim().length()>0){
                    /* if investigator data present for selected row in 
                      investigator table then add blank row to unit table */
                    int rowCount = tblLead.getRowCount();
                    Vector newData = new Vector();
                    newData.add( "");
                    newData.add( new Boolean( false ) );
                    newData.add( "" );
                    newData.add( "" );
                    newData.add( "" );

                    ((DefaultTableModel)tblLead.getModel()).addRow( newData );
                    rowCount = tblLead.getRowCount() -1 ;
                    setTableEditors();
                    tblLead.scrollRectToVisible(
                        tblLead.getCellRect(rowCount ,0, true));
                    
                    tblLead.setRowSelectionInterval(rowCount, rowCount);
                    tblLead.requestFocusInWindow();
                    tblLead.editCellAt(rowCount,2);
                    tblLead.getEditorComponent().requestFocusInWindow();
                    
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
        }else if( actionSource.equals( btnDelete ) ) {
            // delete investigator row after confirmation
            if( selectedRow != -1 ){
               String name = (String)tblPerson.getValueAt(selectedRow,1);
                if(name != null && name.trim().length()>0){
                    int selectedOption 
                        = showDeleteConfirmMessage(
                            "Are you sure you want to remove "+name+
                            " and associated units?");
                    if( selectedOption == JOptionPane.YES_OPTION ){
                        //Coeus enhancement 32.2 modified by shiji - step 26 : start
                        String personId = (String)tblPerson.getValueAt(
                                                                selectedRow,5);
                        //Coeus enhancement 32.2 - step 26 : end
                        if(personId != null && personId.trim().length()>0){
                            if(investigatorData.containsKey(personId)){
                                /* remove investigator details from investigator
                                 hashtable and also his corresponding unit details
                                 from unit hashtable */
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
                }else{
                   // int selectedOption = showDeleteConfirmMessage(
                     //       "Are you sure you want to remove this Investigator person?");
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
                     tblPerson.editCellAt(rowCount,1);
                     tblPerson.getEditorComponent().requestFocusInWindow();             
                }                         
                if( tblPerson.getRowCount() <= 0 ){
                    /* if there are no investigator rows after deleting this 
                      particular row then clear all entries in unit table */
                    btnDelete.setEnabled( false );
                    ((DefaultTableModel)tblLead.getModel()).setDataVector(
                        new Object[][]{}, getUnitTableColumnNames().toArray());

                    setTableEditors();
                    btnAdd.requestFocusInWindow();
                    btnDelUnit.setEnabled( false );
                    btnAddUnit.setEnabled( false );
                    btnFindUnit.setEnabled( false );
                }
            }else{
                showWarningMessage( 
                                coeusMessageResources.parseMessageKey(
                                        "protoInvFrm_exceptionCode.1066") );
            }
        }else if( actionSource.equals( btnDelUnit ) ) {
            /* delete the selected unit information from table as well as from
              the vector which consists of all unit details for a particular
              investigator */
            if(selectedRow != -1){
                //Coeus enhancement 32.2 modified by shiji - step 27 : start
                String personId = (String)tblPerson.getValueAt(selectedRow,5);
                //Coeus enhancement 32.2 - step 27 : end
                int selectedUnitRow = tblLead.getSelectedRow();
                if( selectedUnitRow != -1 ){
                    String unitNo = (String)tblLead.getValueAt(selectedUnitRow,2);
                    //modified for internal issue #73 : Valid Message While deleting Organization and Unit
                    if(unitNo != null && unitNo.trim().length()>0){
                        int selectedOption 
                            = showDeleteConfirmMessage("Are you sure you want"
                            +" to remove "+unitNo+"?");
                        if( selectedOption == JOptionPane.YES_OPTION ){
                            ProtocolInvestigatorUnitsBean personUnitBean = null;
                            if(unitHashData.containsKey(personId)){
                                Vector personUnits = new Vector();
                                personUnits = (Vector)unitHashData.get(personId);
                                if(personUnits!= null){
                                    for(int unitIndex = 0; 
                                            unitIndex < personUnits.size();
                                            unitIndex++){
                                        personUnitBean 
                                            = (ProtocolInvestigatorUnitsBean)
                                               personUnits.elementAt(unitIndex);                                       
                                       if(personUnitBean.getUnitNumber().equals(
                                                unitNo)){
                                       //Added for COEUSQA-2879_LOG_IACUC and IRB_Correspondents do not auto-populate-Start
                                       personUnitBean.setAcType(TypeConstants.DELETE_RECORD);
                                       //Added for COEUSQA-2879_LOG_IACUC and IRB_Correspondents do not auto-populate-End
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
                    }else{ int selectedOption = showDeleteConfirmMessage(coeusMessageResources.parseMessageKey(
                                        "unitDetFrm_exceptionCode.1331"));
                        if( selectedOption == JOptionPane.YES_OPTION ){                        
                            saveRequired = true;
                            ((DefaultTableModel)tblLead.getModel()).removeRow(
                                            selectedUnitRow );
                    }
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
                        btnAddUnit.requestFocusInWindow();
                        btnDelUnit.setEnabled( false );
                    }
                }else{
                    showWarningMessage(
                                coeusMessageResources.parseMessageKey(
                                        "protoInvFrm_exceptionCode.1133") );
                }
            }
        }
    }
    /* supporting method used to set the investigator bean into hashtable and 
       update the investigator table. If homeUnit has been specified then 
       this method will update unit table and makes an entry in unit hashtable. 
       If replace row is true it will update the selected row otherwise adds a
       new row to the table.
     */
    private void setInvestigatorBean(ProtocolInvestigatorsBean investigator,
        String homeUnit, boolean replaceRow){
        int selectedRow = tblPerson.getSelectedRow();
        String oldPersonId = "";
        boolean primaryInv = false;
        if(selectedRow != -1){
            //Coeus enhancement 32.2 modified by shiji - step 28 : start
            oldPersonId = (String)tblPerson.getValueAt(selectedRow,5);
            //Coeus enhancement 32.2 - step 28 : end
            if( oldPersonId == null  || oldPersonId.trim().length() == 0 ){
                replaceRow = true;
                primaryInv = ((Boolean)tblPerson.getValueAt(selectedRow
                    ,2)).booleanValue();
                investigator.setPrincipalInvestigatorFlag(primaryInv);
            }
        }else if(tblPerson.getRowCount() == 0){
            investigator.setPrincipalInvestigatorFlag(true);
        }
               
        String personId = investigator.getPersonId();
        String personName = investigator.getPersonName();
        String affiliation = investigator.getAffiliationTypeDescription();
        boolean pi = investigator.isPrincipalInvestigatorFlag();
        //Coeus enhancement 32.2 added by shiji - step 29 : start
        boolean isTraining = investigator.isTrainingFlag();
        //Coeus enhancement 32.2 - step 29 : end
        //boolean faculty = investigator.isFacultyFlag();
        boolean nonEmp = investigator.isNonEmployeeFlag();
        
        if(!personId.equals(oldPersonId)){
            // check for duplicate person entry
            if(! checkDuplicatePerson( personId ,selectedRow)){
                saveRequired = true;
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
                   tblPerson.setValueAt("",selectedRow,0);
                   tblPerson.setValueAt( personName, selectedRow, 1 );
                   tblPerson.setValueAt( affiliation,selectedRow,3);
                   //Coeus enhancement 32.2 modified by shiji - step 30 : start
                   tblPerson.setValueAt( new Boolean(isTraining),selectedRow,4);
                   tblPerson.setValueAt( personId,selectedRow,5);
                   tblPerson.setValueAt( new Boolean(nonEmp),selectedRow,6);
                   //Coeus enhancement 32.2 - step 30 : start
                }else{
                        // otherwise add new entry
                        Vector newData = new Vector();
                        newData.add( "");
                        newData.add( personName );
                        if(tblPerson.getRowCount() == 0){
                            newData.add( new Boolean( true ) );
                        }else{
                            newData.add( new Boolean( false ) );
                        }
                        newData.add( affiliation );
                        //Coeus enhancement 32.2 modified by shiji - start
                        newData.add(new Boolean(isTraining));
                        //Coeus enhancement 32.2 - end
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
                            ProtocolInvestigatorUnitsBean unitBean 
                                    = new ProtocolInvestigatorUnitsBean();

                            unitBean.setUnitNumber(
                                            unitDetail.getUnitNumber());
                            unitBean.setUnitName(unitDetail.getUnitName());
                            unitBean.setPersonName(
                                        unitDetail.getOspAdminName());
                            unitBean.setPersonId(personId);
                            
                            // added for enhancement of selecting lead unit for IP - by nadh 
                            //start 4 aug 2004
                            if( investigator.isPrincipalInvestigatorFlag() ){
                                unitBean.setLeadUnitFlag(true);
                            }else{
                                unitBean.setLeadUnitFlag(false);
                            }
                            //nadh end 4 aug 2004
                            
                            addToHashtable(personId,unitBean);
                           ((DefaultTableModel)tblLead.getModel()).
                                setDataVector(
                                getUnitTableData( personId ),
                                getUnitTableColumnNames());
                            if(tblLead.getRowCount()>0){
                                tblLead.setRowSelectionInterval(0,0);
                                btnDelUnit.setEnabled(true);
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
        }else{
                showWarningMessage("' " + personName + " ' " + 
                                coeusMessageResources.parseMessageKey(
                                        "general_duplicateNameCode.2277"));
         }
        if( tblPerson.getRowCount() > 0 ){
            btnDelete.setEnabled( true );
            btnAddUnit.setEnabled( true );
            btnFindUnit.setEnabled( true );
        }
    }
    /* Supporting method used to add unit bean to the unit hashtable for a
       specified investigator */
    private void addToHashtable(String selPersonId,
            ProtocolInvestigatorUnitsBean unitsBean){
        ProtocolInvestigatorUnitsBean unitBean = null;        
        int selectedRow = tblLead.getSelectedRow();
        boolean   found = false;
        Vector units = (Vector)unitHashData.get(selPersonId);
        if (units != null ) {
            String unitId = unitsBean.getUnitNumber();
            for (int count = 0 ;count < units.size() ; count++){
                unitBean = (ProtocolInvestigatorUnitsBean)units.get(count);
                if (unitId.equalsIgnoreCase(unitBean.getUnitNumber())) {
                    unitBean.setUnitNumber(unitsBean.getUnitNumber());
                    unitBean.setUnitName(unitsBean.getUnitName());
                    unitBean.setLeadUnitFlag(unitsBean.isLeadUnitFlag());
                    unitBean.setPersonId(selPersonId);
                    unitBean.setPersonName(unitsBean.getPersonName());
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
    
    //Coeus enhancement 32.2 added by shiji - step 31 : start
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
            int selectedRow=tblPerson.getSelectedRow();
            setOpaque(false);
//            if(!value.equals("")) {
                setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
                //setBackground(Color.white);
                if(!isNewRow || (row != selectedRow)){
                    boolean isTraining = ((Boolean)value).booleanValue();
                    if(isTraining){
                        setIcon(checkIcon);
                    }else{
                        setIcon(crossIcon);
                    }
                }else if(isNewRow && (row == selectedRow)) {
                    setIcon(emptyIcon);
                }
            return this;
            
        }
    }
    //Coeus enhancement 32.2 - step 31 : end
    /**
     * Inner Class to define the Name Editor Class Used for PersonName,
     * UnitNumber.
     */
    private class NameEditor extends DefaultCellEditor implements TableCellEditor {
        
        private JTextField txtName = new JTextField();
        private int textLength = 10;
        private String oldValue = "";
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
                //Coeus enhancement 32.2 modified by shiji - step 32 : start
                oldPersonId = (String)tblPerson.getValueAt(selectedRow,5);
                //Coeus enhancement 32.2 - step 32 : end
                primaryInv = ((Boolean)tblPerson.getValueAt(selectedRow
                    ,2)).booleanValue();
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
                    ProtocolInvestigatorsBean invBean = 
                                            new ProtocolInvestigatorsBean();
                    invBean.setPersonId(personInfo.getPersonID());
                    //Coeus enhancement 32.2 added by shiji - step 33 : start
                    boolean isTraining = getTrainingInfo(personInfo.getPersonID());
                    invBean.setTrainingFlag(isTraining);
                    //Coeus enhancement 32.2 - step 33 : end
                    invBean.setPersonName(personInfo.getFullName());
                    invBean.setPrincipalInvestigatorFlag(primaryInv);
                    invBean.setNonEmployeeFlag(false);
                    /*invBean.setFacultyFlag(
                        personInfo.getFacFlag().equalsIgnoreCase("y")  
                        ? true : false);*/
                    invBean.setAffiliationTypeCode( 
                        personInfo.getFacFlag().equalsIgnoreCase("y") ? 1 : 3 );
                    invBean.setAffiliationTypeDescription(
                        personInfo.getFacFlag().equalsIgnoreCase("y") ? "Faculty" : "Affiliate");
                    String homeUnit = personInfo.getHomeUnit();
                    String pId = personInfo.getPersonID();
                    if(!pId.equals(oldPersonId)){ 
                        /* send the constructed bean, homeUnit number for the
                          investigator and send replaceRow as true as we have to
                          overwrite the existing row */
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
        
            txtName.addMouseListener(new MouseAdapter(){
                public void mouseClicked(MouseEvent me){
                    int selRow = tblPerson.getSelectedRow();
                    //Coeus enhancement 32.2 modified by shiji - step 34 : start
                    boolean nonEmployee=((Boolean)tblPerson.getModel().
                                    getValueAt(selRow,6)).booleanValue();
                    String stId=(String)tblPerson.getModel().getValueAt(
                                                                    selRow,5 );
                    //Coeus enhancement 32.2 - step 34 : end
                    if (me.getClickCount() == 2) {
                        
                        if((stId != null) && (stId.trim().length()>0 )){
                            if(nonEmployee){
                                /* selected investigator is a rolodex, so show
                                   rolodex details */
                                RolodexMaintenanceDetailForm frmRolodex 
                                   = new RolodexMaintenanceDetailForm('V',stId);
                                frmRolodex.showForm(mdiForm,DISPLAY_TITLE,true);
                            }else{
                              try{
                                  String loginUserName = mdiForm.getUserName();
                                  
                                  //Bug Fix: Pass the person id to get the person details Start 2
                                  //String personName=(String)tblPerson.getValueAt(selRow,1);
                                  /*Bug Fix:to get the person details with the person id instead of the person name*/
                                  //PersonInfoFormBean personInfoFormBean = (PersonInfoFormBean)coeusUtils.getPersonInfoID(personName);
                                  //PersonDetailForm personDetailForm =
                                  //new PersonDetailForm(personInfoFormBean.getPersonID(),loginUserName,'D');
                                  
                                  PersonDetailForm personDetailForm =new PersonDetailForm(stId ,loginUserName,DISPLAY_MODE);
                                  //Bug Fix: Pass the person id to get the person details End 2
                                  
                              }catch(Exception exception){
                                  exception.printStackTrace();
                              }
                            }
                        }
                        ((NameEditor)tblPerson.getCellEditor(selRow,
                            1)).cancelCellEditing();

                    }
                }
            });
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
            //Coeus enhancement 32.2 added by shiji - step 35 : start
            isNewRow=false;
            //Coeus enhancement 32.2 - step 35 : end
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

    }
    
    //supporting method to validate the personName entered. If valid returns
    //PersonInfoFormBean otherwise null.
    
    //Bug fix for person Validation on focus lost Start 2 
    //Changed the arg from personName to source
    //private PersonInfoFormBean checkPersonName( String personName ){
    private PersonInfoFormBean checkPersonName( Object source ){
        
        //Bug fix for person Validation on focus lost Start
        /*PersonInfoFormBean personInfo = null;
        personInfo = getPersonInfoID( personName );
        if( personInfo == null || personInfo.getPersonID() == null){ 
            personInfo = null;
            CoeusOptionPane.showWarningDialog(
                                coeusMessageResources.parseMessageKey(
                                        "protoCorroFrm_exceptionCode.1054"));
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
            
            txtName.addMouseListener(new MouseAdapter(){
                public void mouseClicked(MouseEvent me){
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
            });
        }
        
        
        private void getUnitDetails(){
            int row = tblLead.getSelectedRow();
            String unitNo = "";
            if(txtName.getText() != null && txtName.getText().trim().length()>0){
                if(!oldValue.equalsIgnoreCase(txtName.getText().trim())){
                    UnitDetailFormBean unitDetail 
                            = getUnitInfoBean( txtName.getText().trim() );
                    if(unitDetail != null){
                        int selPersonRow = tblPerson.getSelectedRow();
                        //Coeus enhancement 32.2 modified by shiji - step 36 : start
                        String personId = 
                                    (String)tblPerson.getValueAt(selPersonRow,5);
                        //Coeus enhancement 32.2 - step 36 : end
                        boolean leadUnit =((Boolean)tblLead.getModel().
                                        getValueAt(row,1)).booleanValue();
                        unitNo = txtName.getText().trim();
                        if(!unitNo.equals(oldValue)){
                            if( !isDuplicateUnitID( personId, 
                                    unitDetail.getUnitNumber())){
                                /* modified to delete the old unit details 
                                  if user modifies the unit details by editing the 
                                  unit number */        
                                ProtocolInvestigatorUnitsBean personUnitBean = null;
                                if(unitHashData.containsKey(personId)){
                                    Vector personUnits = new Vector();
                                    personUnits = (Vector)unitHashData.get(personId);
                                    if(personUnits!= null){
                                        for(int unitIndex = 0; 
                                                unitIndex < personUnits.size();
                                                unitIndex++){
                                            personUnitBean 
                                                = (ProtocolInvestigatorUnitsBean)
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
                                ProtocolInvestigatorUnitsBean unitBean 
                                        = new ProtocolInvestigatorUnitsBean();

                                unitBean.setUnitNumber(unitDetail.getUnitNumber());
                                unitBean.setUnitName(unitDetail.getUnitName());
                                unitBean.setPersonName(unitDetail.getOspAdminName());
                                unitBean.setPersonId(personId);
                                unitBean.setLeadUnitFlag(leadUnit);
                                addToHashtable(personId,unitBean);
                                String unitName = unitDetail.getUnitName();
                                String ospAdmin = unitDetail.getOspAdminName();
                                if( leadUnit ) {
                                    leadUnitNumber = unitDetail.getUnitNumber();
                                }
                                ((DefaultTableModel)tblLead.getModel()).setValueAt(
                                    unitDetail.getUnitNumber(),row,2);
                                ((DefaultTableModel)tblLead.getModel()).setValueAt(
                                    unitName,row,3);
                                ((DefaultTableModel)tblLead.getModel()).setValueAt(
                                    ospAdmin,row,4);
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
    
    
    //supporting method to show the warning message
    private void showWarningMessage( String str ){
        CoeusOptionPane.showWarningDialog(str);
    }
    
    
    //supporting method to show the delete confirm message.
    private int showDeleteConfirmMessage(String msg){
        int selectedOption = CoeusOptionPane.showQuestionDialog(msg,
                                    CoeusOptionPane.OPTION_YES_NO, 
                                    CoeusOptionPane.DEFAULT_YES);
        return  selectedOption;
    }
    
    /** 
     * Method used to handle the value change events for the table.
     * @param listSelectionEvent event which delegates selection changes for a
     * table.
     */
    public void valueChanged( ListSelectionEvent listSelectionEvent ) {
        
        Object source = listSelectionEvent.getSource();
        int selectedLeadUnitRow = tblLead.getSelectedRow();
        int selectedRow = tblPerson.getSelectedRow();
        int rowCount = tblPerson.getRowCount();
        
        if( (source.equals(perSelectionModel) )&& (selectedRow >= 0 )
                && (investigatorData != null)) {
            
            ProtocolInvestigatorsBean investigatorRow=null;        
            investigatorRow = (ProtocolInvestigatorsBean)
            //Coeus enhancement 32.2 modified by shiji - step 37 : start
            investigatorData.get( tblPerson.getValueAt( selectedRow, 
                5) == null ? "" : tblPerson.getValueAt( selectedRow,
                5).toString() );
            //Coeus enhancement 32.2 - step 37 : end
            if(investigatorRow != null){    
                Vector unitsForPerson = getUnitTableData( 
                                                investigatorRow.getPersonId());
                if( unitsForPerson != null ){
                    ((DefaultTableModel)tblLead.getModel()).setDataVector(
                    unitsForPerson, getUnitTableColumnNames() );
                    if(tblLead.getRowCount() > 0 ){
                        tblLead.setRowSelectionInterval(0,0);
                    }
                    //code modified for coeus4.3 enhancements that UI to be in display mode
                    //when new amendment or renewal is created
//                    if(functionType != DISPLAY_MODE){                    
                    if(functionType != DISPLAY_MODE && functionType != AMEND_MODE){
                        btnDelUnit.setEnabled( true );
                    }
                }
            }
            else {
                ((DefaultTableModel)tblLead.getModel()).setDataVector(
                new Vector(), getUnitTableColumnNames() );
            }
            previousSelRow = selectedRow;    
            setTableEditors();
        }
    }
    // supporting method used to update the bean for the specified row with newly
    // set faculty flag.
    /*private void updateFacultyFlag( int personRow ) {
        ProtocolInvestigatorsBean investigatorRow=null;        
        investigatorRow = (ProtocolInvestigatorsBean)
        investigatorData.get( tblPerson.getValueAt( personRow, 
            4) == null ? "" : tblPerson.getValueAt( personRow,
            4).toString() );
        if( investigatorRow != null ) {
            investigatorRow.setPersonName(
                (String)tblPerson.getValueAt(personRow,1));
            investigatorRow.setPrincipalInvestigatorFlag(
                ((Boolean)tblPerson.getValueAt( personRow, 
                2)).booleanValue());
            // Senthil AR - Commented because CheckBox is replaced by the ComboBox
            //investigatorRow.setFacultyFlag(((Boolean)tblPerson.getValueAt( personRow,3)).booleanValue());
             
            investigatorRow.setAffiliationTypeCode(Integer.parseInt(((ComboBoxBean)comboBox.getSelectedItem()).getCode()));
            investigatorRow.setPersonId(
                (String)tblPerson.getValueAt(personRow,4));
            investigatorRow.setNonEmployeeFlag(
                ((Boolean)tblPerson.getValueAt( personRow,
                5)).booleanValue());
            saveRequired = true;
            investigatorModified = true;
            investigatorData.put(investigatorRow.getPersonId(),investigatorRow);
        }
        
        
    }*/
    
    //supporting method to reset the lead unit flag only one occurance allowed
    private void resetLeadUnits( int selectedRowNumber ){
        int rowCount = tblLead.getRowCount();
        int selPersonRow = tblPerson.getSelectedRow();
        //Coeus enhancement 32.2 modified by shiji - step 38 : start
        String personId = (String)tblPerson.getValueAt(selPersonRow,5);
        //Coeus enhancement 32.2 - step 38 : end
        Vector units = (Vector) unitHashData.get(personId);
        if(units != null && units.size()>0){
            String unitNo =(String) tblLead.getValueAt(selectedRowNumber,2);
            for (int unitCount = 0 ;unitCount < units.size() ; unitCount++) {
                ProtocolInvestigatorUnitsBean unitBean = 
                        (ProtocolInvestigatorUnitsBean)units.get(unitCount);
                if(!unitBean.getUnitNumber().equals(unitNo)){
                    if( unitBean.isLeadUnitFlag() ) {
                        unitBean.setLeadUnitFlag(false);
                        if( unitBean.getAcType() == null ) {
                            unitBean.setAcType( "U" );
                        }
                    }
                    tblLead.setValueAt(new Boolean(false),unitCount,1);
                }else{
                    unitBean.setLeadUnitFlag(true);
                    if( unitBean.getAcType() == null ) {
                        unitBean.setAcType( "U" );
                    }
                    investigatorModified = true;
                    saveRequired = true;
                    //System.out.println("unit bean with true:"+unitBean.getUnitNumber());
                }
                
                units.set(unitCount,unitBean);
                
            }
            unitHashData.put(personId,units);
            ProtocolInvestigatorsBean invBean = ( ProtocolInvestigatorsBean ) investigatorData.get( personId );
            if( invBean.getAcType() == null ) {
                invBean.setAcType( "U" );
            }
            
        }else{
            for (int unitCount = 0 ;unitCount < 
                                        tblLead.getRowCount() ; unitCount++) {
                if(selectedRowNumber != unitCount){
                    tblLead.setValueAt(new Boolean(false),unitCount,1);
                }
            }
        }
    }
    
    //supporting method to reset the pi flag only one occurance allowed
    private void resetPIFlag( String personId){
        ProtocolInvestigatorsBean investigatorRow = (ProtocolInvestigatorsBean)
            investigatorData.get( personId);
        int selectedRowNumber = tblPerson.getSelectedRow();
        if(investigatorRow != null){
            investigatorRow.setPrincipalInvestigatorFlag(true);
            if( functionType == MODIFY_MODE ) {
                investigatorRow.setAcType( UPADTE_RECORD );
            }
            investigatorData.put(personId,investigatorRow);
            
            for( int indx =  tblPerson.getRowCount()-1; indx >= 0 ;  indx-- ){
                if( selectedRowNumber != indx ){
                    tblPerson.setValueAt( new Boolean( false ), indx, 2);
                    //Coeus enhancement 32.2 modified by shiji - step 39 : start
                    String ldPersonId = tblPerson.getValueAt(indx,5).toString();
                    //Coeus enhancement 32.2 - step 39 : end
                    investigatorRow = (ProtocolInvestigatorsBean)
                            investigatorData.get( ldPersonId);
                    if(investigatorRow != null){
                        if( investigatorRow.isPrincipalInvestigatorFlag() ) {
                            investigatorRow.setPrincipalInvestigatorFlag(false);
                            if( functionType == MODIFY_MODE ) {
                                investigatorRow.setAcType( UPADTE_RECORD );
                            }
                            investigatorData.put(ldPersonId,investigatorRow);
                            resetLeadUnitFlag(ldPersonId);
                        }
                            
                    }
                }
            }
        }else{            
            for( int indx =  tblPerson.getRowCount()-1; indx >= 0 ;  indx-- ){
                if( selectedRowNumber != indx ){
                    tblPerson.setValueAt( new Boolean( false ), indx, 2);
                }
            }
        }
    }
    
    
    //supporting method to reset the Lead Unit Flag based on person ID selected
    private void resetLeadUnitFlag(String personId) {

        if (personId != null && !personId.equalsIgnoreCase("")){
            Vector units = (Vector)unitHashData.get( personId );
            if( units != null ){
                ProtocolInvestigatorUnitsBean curUnitBean = null;
                for( int indx = 0 ; indx < units.size(); indx ++ ){
                curUnitBean = (ProtocolInvestigatorUnitsBean) units.get( indx );
                    if (curUnitBean.isLeadUnitFlag()) {
                        curUnitBean.setLeadUnitFlag(false);
                        if( functionType == MODIFY_MODE ) {
                            curUnitBean.setAcType( UPADTE_RECORD );
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
    
    //supporting method to check the duplicates
    private boolean checkDuplicatePerson(String personId, int selectedRow){
        boolean duplicate = false; 
        String oldId = "";
        for(int rowIndex = 0; rowIndex < tblPerson.getRowCount();
        rowIndex++){            
            if(rowIndex != selectedRow){
                //Coeus enhancement 32.2 modified by shiji - step 40 : start
                oldId = (String)tblPerson.getValueAt(rowIndex,5);
                //Coeus enhancement 32.2 - step 40 : start
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
            ProtocolInvestigatorUnitsBean curBean = null;
            for( int indx = 0 ; indx < units.size(); indx ++ ){
                curBean = (ProtocolInvestigatorUnitsBean) units.get( indx );
                if( (curBean.getUnitNumber().equalsIgnoreCase( unitID ))){
                    isDuplicateUnit = true;
                }
            }
        }
        return isDuplicateUnit;
    }
    //supporting method to get PersonInfoBean for specific person which will
    //validate the person name against the db value
    private PersonInfoFormBean getPersonInfoID( String name ){
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
    }
    
    //supporting method to get UnitDetailFormBean for specific unit number
    //which will be used to validate the unit id/name against the
    private UnitDetailFormBean getUnitInfoBean( String unitNumber ){
        boolean success=false;
        String personID=null;
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
            success=true;
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

    //visual swing components variable declaration.
    private JTable tblLead;
    private JButton btnDelUnit;
    private JScrollPane scrPnUnit;
    private JTable tblPerson;
    private JScrollPane scrPnPerson;
    private JButton btnFindRolodex;
    private JButton btnDelete;
    private JButton btnAdd;
    private JButton btnFindPerson;
    private JButton btnFindUnit;
    private JButton btnAddUnit;
}
