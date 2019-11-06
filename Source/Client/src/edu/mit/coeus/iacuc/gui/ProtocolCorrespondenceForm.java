
/*
 * @(#)ProtocolCorrespondenceForm.java
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 */

package edu.mit.coeus.iacuc.gui;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.table.*;
import javax.swing.event.*;
import javax.swing.border.*;

import java.util.*;
import java.beans.*;

import edu.mit.coeus.gui.*;
import edu.mit.coeus.iacuc.bean.*;
import edu.mit.coeus.utils.*;
import edu.mit.coeus.search.gui.*;
import edu.mit.coeus.brokers.*;
import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.rolodexmaint.gui.RolodexMaintenanceDetailForm;
import edu.mit.coeus.departmental.gui.PersonDetailForm;
import edu.mit.coeus.irb.bean.PersonInfoFormBean;

import edu.mit.coeus.exception.*;
/**
 * This class is used to display all the  correspondents for a selected protocol.
 * It contain correspondents type, name and comment description. The user
 * is privileged to add person, rolodex and delete the specific
 * correspondent entry from the table.
 * @@author  kprasad
 * @author Mukund C
 * @updated Subramanya Oct' 24 2002
 */
public class ProtocolCorrespondenceForm extends JComponent
implements ActionListener,
ListSelectionListener,
FocusListener {
    
    //holds reference to  CoeusAppletMDIForm
    private CoeusAppletMDIForm mdiForm = null;
    
    private int parentSequenceId;
    
    // Variables declaration - do not modify
    //holds the instance of scrollpanel for the comments block
    private JScrollPane scrPnComments;
    
    //holds the instance of scrollPnale for the correspondent block
    private JScrollPane scrPnCorrespondent;
    
    //holds the instace of the correspondents entry table
    private JTable tblCorsData;
    
    //holds the find rolodex button instance.
    private JButton btnFindRolodex;
    
    //holds the delete button instance
    private JButton btnDelete;
    
    //holds the add button instace
    private JButton btnAdd;
    
    //holds the button for find person instance
    private JButton btnFindPerson;
    
    //holds the textarea instance for Comments block
    private JTextArea txtArComments;
    
    /*
     * Removing the JLabel Comments as per the
     * Change Request id : 109 Feb' 14 2003
     * Updated by Raghunath P.V. Feb' 17 2003
     */
    //private JLabel lblComments;
    // End of variables declaration
    
    //holds the combobox Bean Data
    private Vector comboBoxBeanData = null;
    
    //holds the fucntion type
    private char functionType ;
    
    //holds collection of current table correspondents
    private Vector currentCorrespondents = null;
    
    //holds collections of deleted correspondents.
    private Vector deletedCorrespondents = null;
    
    //holds the so far saved info
    private boolean isFormDataUpdated = false;
    
    /* flag used to indicate saving of modifications */
    private boolean saveRequired = false;
    
    private boolean firstEntry = false;
    
    //holds the last selected Row
    private int lastSelectedRow = 0;
    
    /* Updated by Raghunath P.V. Mar' 4 2003
     * To fix the id# 190_3
     */
    // holds the string value used to display the Rolodex details
    private final String DISPLAY_TITLE = "DISPLAY ROLODEX";
    
    //holds the previous comment
    private String prvComments = "";
    
    //holds CoeusMessageResources instance used for reading message Properties.
    private CoeusMessageResources coeusMessageResources;
    boolean temp = false;
    
    private CoeusUtils coeusUtils = CoeusUtils.getInstance();
    
    /**
     * Creates new form ProtocolCorrespondenceForm. Default Constructor.
     */
    public ProtocolCorrespondenceForm() {
    }
    
    /**
     * Creates new form ProtocolCorrespondenceForm with the parent mdiForm
     * @param mdiParentForm parent form window.
     */
    public ProtocolCorrespondenceForm( CoeusAppletMDIForm mdiParentForm ) {
        mdiForm = mdiParentForm;
    }
    
    /**
     * Creates new ProtocolCorrespondenceForm form object with corresponents data
     * collections and form mode/type.
     * @param fnType function type/ form mode type.
     * @param corresPndData collection of correspondents details.
     * @param protocolCorrespondentTypes correspondents types.
     */
    public ProtocolCorrespondenceForm( char fnType, Vector corresPndData,
    Vector protocolCorrespondentTypes ) {
        this.currentCorrespondents = corresPndData;
        this.functionType = fnType;
        this.comboBoxBeanData = protocolCorrespondentTypes;
        deletedCorrespondents = new Vector();
    }
    
    /**
     * This method is used to initialize the form components and set the form
     * data and set the enabled status for all components depending on the
     * functionType specified while opening the form.
     *
     * @param mdiForm CoeusAppletMDIForm reference to the parent Component
     * @return JComponent reference to the ProtocolMaintenanceForm component
     * after initializing and setting the data
     */
    public JComponent showProtocolCorrespondentsForm(CoeusAppletMDIForm
    mdiForm){
        this.mdiForm = mdiForm;
        initComponents();
        formatFields();
        coeusMessageResources = CoeusMessageResources.getInstance();
        return this;
    }
    
    /**
     * This method is used to set the enabled status for the components
     * depending on the functionType specified.
     */
    private void formatFields(){

        //code modified for coeus4.3 enhancements that UI to be in display mode
        //when new amendment or renewal is created
//        boolean enabled = functionType != 'D' ? true : false ;
        boolean enabled = (functionType == CoeusGuiConstants.DISPLAY_MODE 
                || functionType == CoeusGuiConstants.AMEND_MODE) ? false : true ;
        btnFindRolodex.setEnabled( enabled );
        btnDelete.setEnabled( enabled );
        txtArComments.setEnabled( enabled );
        btnAdd.setEnabled( enabled );
        btnFindPerson.setEnabled( enabled );
        //txtArComments.setEditable( enabled );
        if( !enabled ){
            txtArComments.setBackground(this.getBackground());
        }else{
            txtArComments.setBackground(Color.white);   
        }
        if( tblCorsData.getRowCount() < 1 ){
            btnDelete.setEnabled( false );
            txtArComments.setEnabled( false );
        }
        
        //Added by Amit 11/18/2003
        if(functionType == CoeusGuiConstants.DISPLAY_MODE || functionType == CoeusGuiConstants.AMEND_MODE){
            java.awt.Color bgListColor = (java.awt.Color)javax.swing.UIManager.getDefaults().get("Panel.background");
        
            tblCorsData.setBackground(bgListColor);    
            tblCorsData.setSelectionBackground(bgListColor );
            tblCorsData.setSelectionForeground(Color.black);            
        }
        else{
            tblCorsData.setBackground(Color.white);            
            tblCorsData.setSelectionBackground(Color.white);
            tblCorsData.setSelectionForeground(Color.black);            
        }
        //end Amit        
        
        /**
         * Added the code
         * Updated by Raghunath P.V. july' 25 2003
         * To fix the id# DEF_05
         */
        tblCorsData.addMouseListener(new MouseAdapter(){
            String strSearch = null;
            public void mouseClicked(MouseEvent me){
                int selRow = tblCorsData.getSelectedRow();
                boolean nonEmployee = false;
                String pmCorrespondenceID = null;
                if( selRow != -1 && currentCorrespondents != null &&
                currentCorrespondents.size() >= selRow ){
                    ProtocolCorrespondentsBean curBean =
                    ( ProtocolCorrespondentsBean )
                    currentCorrespondents.get(  selRow );
                    nonEmployee = curBean.isNonEmployeeFlag();
                    pmCorrespondenceID = curBean.getPersonId();
                }
                if (me.getClickCount() == 2) {
                    displayCorrespondenceDetails(pmCorrespondenceID, nonEmployee);
                }
            }
        });
        
    }
    /**
     * Added the code
     * Updated by Raghunath P.V. july' 25 2003
     * To fix the id# DEF_05
     */
    private void displayCorrespondenceDetails(String stId, boolean nonEmployee){
        if((stId != null) && (stId.trim().length()>0 )){
            if(tblCorsData.getCellEditor()!=null){
                tblCorsData.getCellEditor().
                cancelCellEditing();
            }
            if(nonEmployee){
                //selected investigator is a rolodex, so show
                //rolodex details
                RolodexMaintenanceDetailForm frmRolodex
                = new RolodexMaintenanceDetailForm('V',stId);
                frmRolodex.showForm(mdiForm,DISPLAY_TITLE,true);
            }else{
                try{
                    int selRow = tblCorsData.getSelectedRow();
                    String loginUserName = mdiForm.getUserName();
                    
                    //Bug Fix: Pass the person id to get the person details Start.
                    //String personName=(String)tblCorsData.getValueAt(selRow,2);
                    /*Bug Fix:to get the person details with the person id instead of the person name*/
                    //PersonInfoFormBean personInfoFormBean = (PersonInfoFormBean)coeusUtils.getPersonInfoID(personName);
                    //PersonDetailForm personDetailForm =
                    //new PersonDetailForm(personInfoFormBean.getPersonID(),loginUserName,'D');
                    
                    PersonDetailForm personDetailForm = new PersonDetailForm(stId,loginUserName,
                            CoeusGuiConstants.DISPLAY_MODE);
                    //Bug Fix: Pass the person id to get the person details End.
                    
                }catch(Exception exception){
                    exception.printStackTrace();
                }
            }
        }
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     */
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;
        btnFindPerson = new JButton();
        btnFindPerson.addActionListener( this );
        btnFindPerson.setMnemonic( 'P' );
        
        btnDelete = new JButton();
        btnDelete.addActionListener( this );
        btnDelete.setEnabled( false );
        btnDelete.setMnemonic( 'D' );
        btnDelete.setFont( CoeusFontFactory.getLabelFont() );
        
        btnFindRolodex = new JButton();
        btnFindRolodex.setFont( CoeusFontFactory.getLabelFont() );
        btnFindRolodex.addActionListener( this );
        btnFindRolodex.setMnemonic( 'R' );
        
        btnAdd = new JButton();
        btnAdd.addActionListener( this );
        btnAdd.setMnemonic( 'A' );
        
        scrPnComments = new JScrollPane();
        
        tblCorsData = new JTable();
        
        scrPnCorrespondent = new JScrollPane();
        /*
         * Created the title for the scrPnCorrespondent as Correspondents
         * as per the Change Request id : 109,  Feb' 14 2003
         * Updated by Raghunath P.V. Feb' 17 2003
         */
        scrPnCorrespondent.setBorder(new TitledBorder(new EtchedBorder(),
        "Correspondents", TitledBorder.LEFT,
        TitledBorder.TOP,CoeusFontFactory.getLabelFont()));
        /*
         * Changed the size of the JScrollPane
         * as per the Change Request id : 109, Feb' 14 2003
         * Updated by Raghunath P.V. Feb' 17 2003
         */
//        scrPnCorrespondent.setMinimumSize(new java.awt.Dimension(500, 200));
//        scrPnCorrespondent.setPreferredSize(new java.awt.Dimension(500, 200));
        // Added by chandra - 28/08/2003
        scrPnCorrespondent.setMinimumSize(new java.awt.Dimension(500, 255));
        scrPnCorrespondent.setPreferredSize(new java.awt.Dimension(500, 330));
        
        txtArComments = new JTextArea();
        txtArComments.addFocusListener( this );
        
        /*
         * Removing the JLabel Comments as per the
         * Change Request id : 109,  Feb' 14 2003
         * Updated by Raghunath P.V. Feb' 17 2003
         */
        //lblComments = new JLabel();
        
        setLayout(new java.awt.GridBagLayout());
        
        //setPreferredSize( new java.awt.Dimension( 640, 300 ));
        setPreferredSize( new java.awt.Dimension( 640, 400 ));
        
       // setBorder(new EtchedBorder());  Removed the external border.
        btnAdd.setFont(CoeusFontFactory.getLabelFont());
        btnAdd.setText("Add");
        
        /* Changed the insets for proper layout of the display as per ChangeRequest
         * id : 109
         */
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(15, 5, 5, 5);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        add(btnAdd, gridBagConstraints);
        
        /* Changed the insets for proper layout of the display as per ChangeRequest
         * id : 109
         */
        btnFindPerson.setFont(CoeusFontFactory.getLabelFont());
        btnFindPerson.setText("Find Person");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 65, 5);
        add(btnFindPerson, gridBagConstraints);
        
        /* Changed the insets for proper layout of the display as per ChangeRequest
         * id : 109
         */
        btnFindRolodex.setFont(CoeusFontFactory.getLabelFont());
        btnFindRolodex.setText("Find Rolodex");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 5, 5);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        add(btnFindRolodex, gridBagConstraints);
        
        /* Changed the insets for proper layout of the display as per ChangeRequest
         * id : 109
         */
        btnDelete.setFont(CoeusFontFactory.getLabelFont());
        btnDelete.setText("Delete");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 5, 5);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        add(btnDelete, gridBagConstraints);
        
        /* Changed the JScrollPane title to comments
         * as per the Change Request id : 109,  Feb' 14 2003
         * Updated by Raghunath P.V. Feb' 17 2003
         */
        scrPnComments.setBorder(new TitledBorder(new EtchedBorder(),
        "Comments", TitledBorder.LEFT,
        TitledBorder.TOP,CoeusFontFactory.getLabelFont()));
        //tblCorsData.setSelectionBackground( Color.white );
        //tblCorsData.setSelectionForeground( Color.black );
        
        tblCorsData.setModel( constructCorrespondandentTableModel() );
        
        ListSelectionModel perSelectionModel = tblCorsData.getSelectionModel();
        perSelectionModel.addListSelectionListener( this );
        perSelectionModel.setSelectionMode(
        ListSelectionModel.SINGLE_SELECTION );
        perSelectionModel.setSelectionInterval( 1,
        tblCorsData.getColumnCount()-1);
        tblCorsData.setSelectionModel( perSelectionModel );
        tblCorsData.clearSelection();
        tblCorsData.setFont(CoeusFontFactory.getNormalFont());
        tblCorsData.getTableHeader().setFont(CoeusFontFactory.getLabelFont());
        
        setHandIconDisplyColumn();
        
        //sets the name input entry component to JTextfield with defaultstyle
        //document set.
        setNameEditor( 2 );
        
        setCorrespondentTypeColumn( 1 );
        
        scrPnCorrespondent.setViewportView(tblCorsData);
        
        if( currentCorrespondents == null ){
            currentCorrespondents = new Vector();
            btnDelete.setEnabled( false );
            txtArComments.setEnabled( false );
        }
        
        txtArComments.setName("txtArComments");
        txtArComments.setDocument( new LimitedPlainDocument( 2000 ) );
        /**
         * Added by Raghunath P.V. to fix the bug Id : DEF_06
         */
        scrPnComments.setHorizontalScrollBarPolicy(javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        txtArComments.setWrapStyleWord(true);
        txtArComments.setFont(CoeusFontFactory.getNormalFont());
        txtArComments.setLineWrap(true);
        scrPnComments.setViewportView(txtArComments);
        
        
        setDefaultRowSelection( 0 );
        
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 4;
        //gridBagConstraints.ipadx = 450;
        //gridBagConstraints.ipady = 75;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 0);
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        /*gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.gridheight = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);*/
        add(scrPnCorrespondent, gridBagConstraints);
        
        /* Changes in the code for proper layout of Comments scrollpane
         * as per ChangeRequest id : 109  Feb' 14 2003
         * Changed By: Raghunath P.V.  Feb' 17 2003
         * Removed ipadx and ipady properties
         */
        
        scrPnComments.setMinimumSize(new java.awt.Dimension(500, 145));
        scrPnComments.setPreferredSize(new java.awt.Dimension(500, 145));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        //gridBagConstraints.ipadx = 450;
        //gridBagConstraints.ipady = 50;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        add(scrPnComments, gridBagConstraints);
        
        //Added by Chandra 12/09/2003
        java.awt.Component[] components = {tblCorsData,btnAdd,btnDelete,btnFindPerson,btnFindRolodex,txtArComments};
        ScreenFocusTraversalPolicy  traversePolicy = new ScreenFocusTraversalPolicy( components );
        setFocusTraversalPolicy(traversePolicy);
        setFocusCycleRoot(true);  
        // End chandra
        
        /*
         * Removing the JLabel Comments as per the
         * Change Request id : 109 Feb' 14 2003
         * Updated by Raghunath P.V. Feb' 17 2003
         */
        
        /*lblComments.setFont(CoeusFontFactory.getLabelFont());
        lblComments.setText("");
        lblComments.setName("lblComments");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(25, 0, 5, 0);
        add(lblComments, gridBagConstraints);*/
        
        
    }
    
    
    //supporting method to set the Correspondent Type Column for combo list
    //disply;
    private void setCorrespondentTypeColumn( int columnIndex ){
        TableColumn clmCombo = tblCorsData.getColumnModel().
        getColumn(columnIndex);
        JComboBox coeusCombo = new JComboBox();
        coeusCombo.setFont(CoeusFontFactory.getNormalFont());
        coeusCombo.setModel( new CoeusComboBox(
        getCorrespondentTypeComboValues() ,false).getModel());
        clmCombo.setCellEditor(new DefaultCellEditor(coeusCombo ){
            public Object getCellEditorValue(){
                return ((JComboBox)getComponent()).getSelectedItem().
                toString();
            }
        });
    }
    
    
    //supporting method to set the Name Editor for respective column
    private void setNameEditor( int columnIndex ){
        NameEditor nmEdtName = new NameEditor( "Name", 90 );
        TableColumn clmName =
        tblCorsData.getColumnModel().getColumn(columnIndex);
        clmName.setCellEditor( nmEdtName );
    }
    
    
    //supporting method to set the Hand icon display method
    private void setHandIconDisplyColumn(){
        
        TableColumn column = tblCorsData.getColumnModel().getColumn(0);
        column.setMinWidth(30);
        column.setMaxWidth(30);
        column.setHeaderRenderer(new EmptyHeaderRenderer());
        
        column.setResizable(false);
        column.setCellRenderer(new IconRenderer());
        column.setPreferredWidth(30);
        
        column = tblCorsData.getColumnModel().getColumn(1);
        column.setMinWidth(120);
        //column.setMaxWidth(120);
        column.setPreferredWidth(120);
        
        column = tblCorsData.getColumnModel().getColumn(2);
        column.setMinWidth(330);
        //column.setMaxWidth(120);
        column.setPreferredWidth(330);
        
        tblCorsData.setRowHeight( 21 );
        JTableHeader header = tblCorsData.getTableHeader();
        header.setReorderingAllowed(false);
        //header.setResizingAllowed(false);
        tblCorsData.setOpaque(false);
        tblCorsData.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tblCorsData.setShowVerticalLines(false);
        tblCorsData.setShowHorizontalLines(false);
    }
    
    /**
     * This method is used to set whether the data is to be saved or not.
     * @param save boolean true if any modifications have been done and are not
     * saved, else  false.
     */
    public void setSaveRequired(boolean save){
        this.saveRequired = save;
    }
    
    /**
     * This Method is used to perform Add/Delete/Find-Person-Rolodex Actions
     * @param btnActionEvent Action denote the ActionEvent for the buttons.
     */
    public void actionPerformed( ActionEvent btnActionEvent ){
        
        Object actionSource = btnActionEvent.getSource();
        CoeusSearch protocolSearch = null;
        
        if( actionSource.equals( btnAdd ) ) {
            /*
             * Changes made for the Fix #190_3
             * Changes done by Raghunath P.V.
             **/
            constructNewPersonEntry( null, "" , false);
            //code modified for coeus4.3 enhancements that UI to be in display mode
            //when new amendment or renewal is created
//            if( tblCorsData.getRowCount() > 0 ){
            if( tblCorsData.getRowCount() > 0 && functionType != CoeusGuiConstants.DISPLAY_MODE){
                btnDelete.setEnabled( true );
                txtArComments.setEnabled( true );
            }
            tblCorsData.requestFocusInWindow();
            int selRow = tblCorsData.getSelectedRow();
            tblCorsData.editCellAt(selRow,2);
            tblCorsData.getEditorComponent().requestFocusInWindow();
            
        }else if( actionSource.equals( btnFindPerson ) ) {
            
            try{
                /**
                 * Updated For : REF ID 149 Feb' 14/18 2003
                 * Person Search allows for multiple entries, however,
                 * the user can only add 1 at a time
                 *
                 * Updated by Subramanya Feb' 19 2003
                 */
                int selectedRow = tblCorsData.getSelectedRow();
         //       if(selectedRow != - 1 && selectedRow >= 0 ){
        // added by manoj to fix bug id #21 IRB-SystemTestingDL-01.xls 26/08/2003     
                    String correspType = "";
                    if(selectedRow !=- 1 && selectedRow >=0){
                        correspType = (String)tblCorsData.getValueAt(selectedRow,1);
                    }
                    protocolSearch = new CoeusSearch( mdiForm,
                    CoeusGuiConstants.PERSON_SEARCH,
                    CoeusSearch.TWO_TABS_WITH_MULTIPLE_SELECTION );
                    //TWO_TABS ) ;
                    protocolSearch.showSearchWindow();
                    Vector vSelectedPersons = protocolSearch.getMultipleSelectedRows();
                    if( vSelectedPersons != null ) {
                        
                        HashMap singlePersonData = null;
                        for(int indx = 0; indx < vSelectedPersons.size(); indx++ ){
                            singlePersonData = (HashMap)vSelectedPersons.get( indx ) ;
                            if( singlePersonData == null || singlePersonData.isEmpty() ){
                                continue;
                            }
                            String personName = Utils.convertNull(
                            singlePersonData.get( "FULL_NAME" ));//result.get( "FULL_NAME" ));
                            String personID = Utils.convertNull(
                            singlePersonData.get( "PERSON_ID" ));//result.get( "PERSON_ID" ));
                            // added by manoj to fix bug id:21 IRB-SystemTestingDL-01.xls 26/08/2003
                            if(checkDuplicateCorrespondent(personName,correspType,-1)){
                                CoeusOptionPane.showWarningDialog(correspType+"  "+personName+". "+
                                coeusMessageResources.parseMessageKey(
                                "protoCorroFrm_exceptionCode.1055"));
                            }else{
                                ProtocolCorrespondentsBean curBean = null;
                                String beanPersonName = null;
                                
                                if( currentCorrespondents != null && selectedRow != -1 ){
                                    curBean = (ProtocolCorrespondentsBean) currentCorrespondents.
                                    get( selectedRow );
                                    beanPersonName = curBean.getPersonName();
                                }
                                
                                if( selectedRow != -1 && beanPersonName != null &&
                                beanPersonName.length() == 0  ){
                                    tblCorsData.setValueAt( personName , selectedRow, 2 );
                                    
                                    if( (curBean != null) && (curBean.getAcType()!=null ) &&
                                    (! curBean.getAcType().equalsIgnoreCase("I"))){

                                        curBean.setAcType( "U" );
                                        saveRequired = true;
                                        isFormDataUpdated = true;
                                    }
                                    curBean.setPersonName( personName );
                                    curBean.setPersonId( personID );
                                    String type =
                                    tblCorsData.getValueAt(selectedRow, 1 ).toString();
                                    curBean.setCorrespondentTypeCode(
                                    Integer.parseInt(getIDForName( type )));
                                    curBean.setCorrespondentTypeDesc( type );
                                    
                                }else{
                                    constructNewPersonEntry( personID, personName, false );
                                    selectedRow = currentCorrespondents.size() - 1;
                                    if( tblCorsData.getRowCount() > 0 ){
                                        btnDelete.setEnabled( true );
                                        txtArComments.setEnabled( true );
                                    }
                                }
                                if(selectedRow >=0){
                                    tblCorsData.clearSelection();
                                    tblCorsData.setRowSelectionInterval( selectedRow, selectedRow );
                                }
                            }
                        }//end of for loop
                    }// end of vSelectedPersons!= null
          //      }
            }catch( Exception err ){
                
                CoeusOptionPane.showErrorDialog( err.getMessage());
            }
            
        }else if( actionSource.equals( btnFindRolodex ) ) {
            
            try{
                int selectedRow = tblCorsData.getSelectedRow();
              //  if(selectedRow != -1 && selectedRow >=0 ){
                    /**
                     * Updated For : REF ID 149 Feb' 14/19 2003
                     * Person/Rolodex Search allows for multiple entries, however,
                     * the user can only add 1 at a time
                     *
                     * Updated by Subramanya Feb' 20 2003
                     */
                    //added by manoj to fix the bug id #21 IRB-SystemTestingDL-01.xls 26/08/2003
                    String correspType = "";
                    if(selectedRow != - 1 && selectedRow >=0){
                        correspType = (String)tblCorsData.getValueAt(selectedRow,1);
                    }
                    protocolSearch = new CoeusSearch( mdiForm,
                    CoeusGuiConstants.ROLODEX_SEARCH,
                    CoeusSearch.TWO_TABS_WITH_MULTIPLE_SELECTION ) ;
                    //            CoeusSearch.TWO_TABS ) ;
                    protocolSearch.showSearchWindow();
                    Vector vSelectedRolodex = protocolSearch.getMultipleSelectedRows();
                    
                    if ( vSelectedRolodex != null ){
                        
                        HashMap singleRolodexData = null;
                        for(int indx = 0; indx < vSelectedRolodex.size(); indx++ ){
                            
                            singleRolodexData = (HashMap)vSelectedRolodex.get( indx ) ;
                            
                            //Hashtable result = protocolSearch.getSelectedRow();
                /*HashMap result = protocolSearch.getSelectedRow();
                if( result == null || result.isEmpty() ){
                    return;
                }*/
                            if( singleRolodexData == null || singleRolodexData.isEmpty() ){
                                continue;
                            }
                            
                            String rolodexID = Utils.convertNull(
                            singleRolodexData.get( "ROLODEX_ID" ));
                            //      result.get( "ROLODEX_ID" ) );
                            String firstName = Utils.convertNull(
                            singleRolodexData.get( "FIRST_NAME" ));
                            //      result.get( "FIRST_NAME" ) );
                            String lastName = Utils.convertNull(
                            singleRolodexData.get( "LAST_NAME" ));
                            //      result.get( "LAST_NAME" ) );
                            String middleName = Utils.convertNull(
                            singleRolodexData.get( "MIDDLE_NAME" ));
                            //   result.get( "MIDDLE_NAME" ) );
                            String namePreffix = Utils.convertNull(
                            singleRolodexData.get( "PREFIX" ));
                            //       result.get( "PREFIX" ) );
                            String nameSuffix = Utils.convertNull(
                            singleRolodexData.get( "SUFFIX" ));
                            //        result.get( "SUFFIX" ) );
                            String rolodexName = null;
                            
                            if ( lastName.length() > 0) {
                                rolodexName = ( lastName +", " + nameSuffix + " " +
                                namePreffix + " " +
                                firstName + " " + middleName ).trim();
                            } else {
                                rolodexName = Utils.convertNull(
                                singleRolodexData.get("ORGANIZATION"));
                                //result.get("ORGANIZATION") );
                            }
                            // added by manoj to fix bug id#21 IRB-SystemTestingDL-01.xls 26/08/2003
                            if(checkDuplicateCorrespondent(rolodexName,correspType,-1)){
                                CoeusOptionPane.showWarningDialog(correspType+"  "+rolodexName+". "+
                                coeusMessageResources.parseMessageKey(
                                "protoCorroFrm_exceptionCode.1055"));
                            }else{
                                ProtocolCorrespondentsBean curBean = null;
                                String beanPersonName = null;
                                if( currentCorrespondents != null && selectedRow != -1 ){
                                    curBean = (ProtocolCorrespondentsBean) currentCorrespondents.
                                    get( selectedRow );
                                    beanPersonName = curBean.getPersonName();
                                }
                                
                                if( selectedRow != -1 && beanPersonName != null &&
                                beanPersonName.length() == 0 ){
                                    tblCorsData.setValueAt( checkForNull( rolodexName),
                                    selectedRow, 2);
                                    if( curBean != null ){
                                        if( (curBean.getAcType()!=null ) &&
                                        ( ! curBean.getAcType().equalsIgnoreCase("I") )){

                                            curBean.setAcType( "U" );
                                            isFormDataUpdated = true;
                                            saveRequired = true;
                                        }
                                        curBean.setPersonName( rolodexName );
                                        curBean.setNonEmployeeFlag( true );
                                        curBean.setPersonId( rolodexID );
                                        String type = tblCorsData.getValueAt(selectedRow, 1 ).
                                        toString();
                                        curBean.setCorrespondentTypeCode( Integer.parseInt(
                                        getIDForName( type )));
                                        curBean.setCorrespondentTypeDesc( type );
                                    }
                                    
                                }else{
                    /*
                     * Changes made for the Fix #190_3
                     * Changes done by Raghunath P.V.
                     **/
                                    constructNewPersonEntry( rolodexID, rolodexName, true );
                                    selectedRow = currentCorrespondents.size() - 1;
                                    if( tblCorsData.getRowCount() > 0 ){
                                        btnDelete.setEnabled( true );
                                        txtArComments.setEnabled( true );
                                    }
                                }
                                if(selectedRow >=0){
                                    tblCorsData.clearSelection();
                                    tblCorsData.setRowSelectionInterval( selectedRow, selectedRow );
                                }
                            }
                        } //end of for loop
                    }//end of vSelectedRolodex != null
              //  }// end if for seleced row
            }catch( Exception err ){
                CoeusOptionPane.showErrorDialog( err.getMessage());
            }
            
        }else if( actionSource.equals( btnDelete ) ) {
            
            int selectedRow = tblCorsData.getSelectedRow();
            //code modified for coeus4.3 enhancements that UI to be in display mode
            //when new amendment or renewal is created
//            if( selectedRow != -1 && functionType != 'D' ){
            if( selectedRow != -1 && functionType != CoeusGuiConstants.DISPLAY_MODE 
                    && functionType != CoeusGuiConstants.AMEND_MODE ){
                int selectedOption = CoeusOptionPane.showQuestionDialog(
                coeusMessageResources.parseMessageKey(
                "protoCorroFrm_delConfirmCode.1127"),
                CoeusOptionPane.OPTION_YES_NO,
                CoeusOptionPane.DEFAULT_YES);
                
                if( selectedOption == JOptionPane.YES_OPTION ){
                    ProtocolCorrespondentsBean curBean =
                    (ProtocolCorrespondentsBean) currentCorrespondents.
                    get( selectedRow );
                    if( curBean != null ){
                        if(curBean.getAcType() != null){

                            if( ! curBean.getAcType().equalsIgnoreCase("I") ) {

                                curBean.setAcType( "D" );
                                deletedCorrespondents.addElement( curBean );
                                currentCorrespondents.removeElementAt( selectedRow );
                            }
                            if( curBean.getAcType().equalsIgnoreCase("I") ){
                                currentCorrespondents.removeElementAt( selectedRow );
                            }

                        }else{

                            curBean.setAcType( "D" );
                            deletedCorrespondents.addElement( curBean );
                            currentCorrespondents.removeElementAt( selectedRow );
                        }
                        saveRequired = true;
                    }else{


                    }
                    ((DefaultTableModel)
                    tblCorsData.getModel()).removeRow( selectedRow );
                    
                    if( tblCorsData.getRowCount() <= 0 ){
                        btnAdd.requestFocusInWindow();
                        btnDelete.setEnabled( false );
                        txtArComments.setEnabled( false );
                        txtArComments.setText("");                       
                    }else{
                        btnDelete.setEnabled( true );
                        txtArComments.setEnabled( true );
                        ProtocolCorrespondentsBean firstBean =
                        (ProtocolCorrespondentsBean)
                        currentCorrespondents.get( 0 );
                        if(firstBean != null){
                                txtArComments.setText( firstBean.getComments() );
                                txtArComments.setCaretPosition(0);
                            }
                        if(selectedRow == 0){
                            tblCorsData.setRowSelectionInterval(selectedRow, selectedRow);
                        }else{
                            tblCorsData.setRowSelectionInterval(selectedRow-1, selectedRow-1);
                        }
                        /** Bug Fix #2049 - End
                         */
                                                 
                    }
                }
                
                
                
            }else{
                showWarningMessage();
            }

        }
    }
    
    //supporting method to check for null value
    private String checkForNull( Object value ){
        
        return value.toString().equalsIgnoreCase( "null" ) ?  "" :
            value.toString();
    }
    
    //supporting method to construct new person with newly generated sequesnce
    //number, combolist type and person/rolodex name
    /**
     * Changes made for the Fix #190_3
     * Changes done by Raghunath P.V.
     **/
    private void constructNewPersonEntry( String personID, String personName , boolean nonEmpFlag){
        
        Vector newRowData = new Vector();
        newRowData.addElement( "" );
        setNameEditor( 2 );
        
        TableColumn clmName = tblCorsData.getColumnModel().getColumn(1);
        JComboBox  coeusCombo = new JComboBox();
        coeusCombo.setModel( new CoeusComboBox(
        getCorrespondentTypeComboValues() ,false).getModel());
        clmName.setCellEditor(new DefaultCellEditor(coeusCombo ){
            public Object getCellEditorValue(){
                return ((JComboBox)getComponent()).getSelectedItem().
                toString();
            }
        });
        
        newRowData.addElement( ((ComboBoxBean)comboBoxBeanData.get( 0 )).
        getDescription());
        newRowData.addElement( personName );
        ((DefaultTableModel)tblCorsData.getModel()).addRow( newRowData );
        
        ProtocolCorrespondentsBean newCoresBean =
        new ProtocolCorrespondentsBean();
        newCoresBean.setPersonId( personID );
        newCoresBean.setAcType( "I" );
        saveRequired = true;
        //modified for the Bug fix,Bug Id:1812 start 
        newCoresBean.setCorrespondentTypeDesc(((ComboBoxBean)comboBoxBeanData.get(0)).getDescription());
        newCoresBean.setCorrespondentTypeCode(
                                    Integer.parseInt(getIDForName( ((ComboBoxBean)comboBoxBeanData.get(0)).getDescription() )));
        //newCoresBean.setCorrespondentTypeDesc("");
        //End Bug Fix:1812
        newCoresBean.setPersonName(personName);
        /**
         * Changes made for the Fix #190_3
         * Changes done by Raghunath P.V.
         **/
        newCoresBean.setNonEmployeeFlag(nonEmpFlag);
        if (currentCorrespondents != null ) {
            currentCorrespondents.addElement( newCoresBean );
        }
        btnDelete.setEnabled( true );
        txtArComments.setEnabled( true );
        int newRowAdded = currentCorrespondents.size() - 1;
        lastSelectedRow = newRowAdded;
        tblCorsData.setRowSelectionInterval(  newRowAdded, newRowAdded );
        tblCorsData.scrollRectToVisible( tblCorsData.getCellRect(
        newRowAdded ,0, true));
    }
    
    
    //supporting method to show the warning message
    private void showWarningMessage(){
        if( functionType != CoeusGuiConstants.DISPLAY_MODE ) {
            CoeusOptionPane.showWarningDialog(
            coeusMessageResources.parseMessageKey(
            "protoCorroFrm_exceptionCode.1053"));
        }
    }
    
    
    //supporting method to construct the DefaultTableModel
    private DefaultTableModel constructCorrespondandentTableModel() {
        
        DefaultTableModel mdlCoresp = new DefaultTableModel(
        getAllCorrespondentsTableData(),
        getAllCorrespondentColumnNames() ) {
            Class[] types = new Class [] {
                java.lang.Object.class,
                java.lang.Object.class,
                CoeusComboBox.class
            };
            
            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
            
            public boolean isCellEditable(int row, int col){
                
                //code modified for coeus4.3 enhancements that UI to be in display mode
                //when new amendment or renewal is created
//                if( functionType == 'D' ){
                if( functionType == CoeusGuiConstants.DISPLAY_MODE 
                        || functionType == CoeusGuiConstants.AMEND_MODE){
                    return false;
                }else {
                    if( col == 0 ){
                        return false;
                    }
                    return true;
                }
            }
        };
        return mdlCoresp;
    }
    
    
    /** Method handles the table row selection.
     * @param listSelectionEvent selection event for each row of the table.
     */
    public void valueChanged( ListSelectionEvent listSelectionEvent ) {
        
        int selectedRow = tblCorsData.getSelectedRow();
        String comment = null;
        int rowCount =  tblCorsData.getRowCount();
        if( selectedRow >= 0  && selectedRow <= rowCount &&
        firstEntry && currentCorrespondents != null){
            ProtocolCorrespondentsBean curBean = (ProtocolCorrespondentsBean)
            currentCorrespondents.get( selectedRow );
            //tblCorsData.editCellAt(selectedRow,2);            
            if( txtArComments.hasFocus() ){
                curBean = (ProtocolCorrespondentsBean)
                currentCorrespondents.get(  lastSelectedRow );
                curBean.setComments( txtArComments.getText());                
                updateComments(lastSelectedRow);
                if( (curBean.getAcType() == null) ||( !curBean.getAcType().equalsIgnoreCase(TypeConstants.INSERT_RECORD) )){
                    curBean.setAcType( TypeConstants.UPDATE_RECORD );
                }
                return;
            }else{
                lastSelectedRow = selectedRow;
            }           
            
            if( curBean != null ){
                comment = curBean.getComments();
                if( curBean.getAcType() == null ){                    

                    if( functionType != CoeusGuiConstants.DISPLAY_MODE )
                        // Updated Subramanya. Feb' 12 2003
                        /* Bug Fix for :
                         * The save confirmation message is popping up even if we are not any updating on the protocol details window
                         * Ref: 206
                         */
                        if( ( tblCorsData.getSelectedColumn() == 1 )||
                        (!curBean.getPersonName().equalsIgnoreCase(
                        tblCorsData.getValueAt( selectedRow, 2 ).toString() ))
                        ){
                            saveRequired = true;
                            isFormDataUpdated = true;
                        }
                }
                if( comment == null || comment.equalsIgnoreCase( "null" )){
                    comment = "";
                }
                txtArComments.setText( comment );
                 txtArComments.setCaretPosition(0);
            }
        }
        firstEntry = true ;
    }
    
    
    //supporting method to construct Comob Box Entry of Type Field
    private Vector getCorrespondentTypeComboValues() {
        
        ComboBoxBean comboEntry = null;
        Vector corType = new Vector();
        if( comboBoxBeanData == null ){
            return corType;
        }
        for( int indx = 0; indx < comboBoxBeanData.size(); indx++ ){
            comboEntry = ( ComboBoxBean ) comboBoxBeanData.get( indx );
            corType.addElement( comboEntry.getDescription() );
        }
        return corType;
    }
    
    
    //supporting method to construct Table data from db
    private Vector getAllCorrespondentsTableData(){
        
        Vector tableData = new Vector();
        ProtocolCorrespondentsBean pcData = null;
        Vector tableRowData;
        if( currentCorrespondents != null ) {
            
            for( int indx = 0; indx < currentCorrespondents.size(); indx++ ){
                pcData = ( ProtocolCorrespondentsBean ) currentCorrespondents.
                get( indx );
                tableRowData = new Vector();
                tableRowData.addElement( "" );
                tableRowData.addElement( pcData.getCorrespondentTypeDesc());
                tableRowData.addElement( pcData.getPersonName() );
                tableData.addElement( tableRowData );
            }
        }
        return tableData;
    }
    
    
    //resettting the correspondents type
    private void resetCorrespondentTypes(){
        
        ProtocolCorrespondentsBean compBean  = null;
        //get all current correspondents - from live table
        if ( currentCorrespondents != null ) {
            for( int indx = 0; indx < currentCorrespondents.size() ; indx ++ ){
                compBean = ( ProtocolCorrespondentsBean )
                currentCorrespondents.get(  indx );
                if( compBean != null ){
                    
                    String acType = compBean.getAcType();


                    if( acType == null ){
                        
                        int corTypeFromBean = compBean.getCorrespondentTypeCode();

                        String corTypeFromTable = tblCorsData.getValueAt(indx, 1 ).toString();
                        
                        int corIntTypeFromTable = Integer.parseInt( getIDForName( corTypeFromTable ) );

                        if( corIntTypeFromTable != corTypeFromBean ){
                            //Null check added
//                            if( !acType.equalsIgnoreCase("I") ){
                            if( acType == null || !acType.equalsIgnoreCase("I") ){

                                compBean.setCorrespondentTypeCode( corIntTypeFromTable );
                                compBean.setCorrespondentTypeDesc( corTypeFromTable );

                                compBean.setAcType("U");
                            }
                        }
                    }
                }
            }
        }
    }
    
    
    /** Method to get all the correspondence entries.
     * @return Vector contains ProtocolCorrespondentsBean
     */
    public Vector getFormData(){
        
        Vector saveCorrespondentsData = new Vector();
        ProtocolCorrespondentsBean compBean  = null;
        //get all current correspondents - from live table
        if( functionType != CoeusGuiConstants.AMEND_MODE ){
            for( int dindx = 0 ; dindx < deletedCorrespondents.size(); dindx++ ){
                compBean = ( ProtocolCorrespondentsBean )
                deletedCorrespondents.get(  dindx );
                if(compBean != null && saveCorrespondentsData !=null){
                    saveCorrespondentsData.insertElementAt(compBean,dindx);
                }
            }
        }
        if ( currentCorrespondents != null ) {
            for( int indx = 0; indx < currentCorrespondents.size() ; indx ++ ){
                compBean = ( ProtocolCorrespondentsBean )
                currentCorrespondents.get(  indx );
                if( compBean.getPersonId() != null &&
                compBean.getPersonId().length() >= 0 ){
                    resetCorrespondentTypes();
                    /* added by Geo
                     *  not sure whether this correct or not...
                     *  to be rechecked by Author
                     */
                    /* end block */
                    if(functionType == 'M') {
                        isFormDataUpdated = true;
                    }
                    // added to insert in amend mode.
                    if( functionType == CoeusGuiConstants.AMEND_MODE 
                            || functionType == CoeusGuiConstants.ADD_MODE){
                        compBean.setAcType( "I" );
                    }
                    
                    /*commented to send all the data from client to generalize seq no. implementation in server
                    if(compBean.getAcType() == null || compBean.getAcType().equalsIgnoreCase("null") ){
                        if(compBean.getSequenceNumber() != parentSequenceId ){
                            compBean.setAcType("I");
                        }
                    }*/
                    saveCorrespondentsData.addElement( compBean );
                }
            }
        }
        //send deleted rows if the opened mode is not copy mode.
        if( saveCorrespondentsData.size() > 0 ){
            return saveCorrespondentsData;
        }else{
            return null;
        }
        
    }
    
    
    //supporting method to get Table column Names
    private Vector getAllCorrespondentColumnNames(){
        
        Vector colNames = new Vector();
        colNames.addElement( "Sl No");
        colNames.addElement( "Type" );
        colNames.addElement( "Name" );
        return colNames;
    }
    
    /**
     * This class is used to define the Editor for Person Name field.
     * It intendent to check the length and input character validation
     * to plugged in component editor.
     */
    class NameEditor extends DefaultCellEditor implements TableCellEditor{
        
        private JTextField txtName = new JTextField();
        private int textLength = 10;
        boolean temporary;
        /**
         * This will Construct the Name Field Editor with specified length
         * and for a specified column Name.
         * @param colName String represents the column Name.
         * @param len integer represents the editing length.
         */
        public NameEditor( String colName, int len ){
            
            super( new JTextField() );
            textLength = len;
            txtName.setFont(CoeusFontFactory.getNormalFont());
            txtName.addFocusListener( new FocusAdapter(){
                public void focusGained(FocusEvent fe){
                    temporary = false;
                }
                public void focusLost( FocusEvent focusEvent ){
                    if( !focusEvent.isTemporary() ){
                        if(!temporary){
                            stopCellEditing();
                        }else{
                            cancelCellEditing();
                        }
                    }
                }});
             /* Updated by Raghunath P.V. Mar' 4 2003
              * To fix the id# 190_3
              */
                txtName.addMouseListener(new MouseAdapter(){
                    String strSearch=null;
                    public void mouseClicked(MouseEvent me){
                        int selRow = tblCorsData.getSelectedRow();
                        boolean nonEmployee=false;
                        String pmCorrespondenceID=null;
                        if( selRow != -1 && currentCorrespondents != null &&
                        currentCorrespondents.size() >= selRow ){
                            ProtocolCorrespondentsBean curBean =
                            ( ProtocolCorrespondentsBean )
                            currentCorrespondents.get(  selRow );
                            nonEmployee = curBean.isNonEmployeeFlag();
                            pmCorrespondenceID = curBean.getPersonId();
                        }
                        if (me.getClickCount() == 2) {
                            displayCorrespondenceDetails(pmCorrespondenceID, nonEmployee);
                        }//End Mouse Click 2
                    }
                });
        }
        /* Updated by Raghunath P.V. Mar' 4 2003
         * To fix the id# 190_3
         */
        /* Commented
         * Updated by Raghunath P.V. july' 25 2003
         * To fix the id# DEF_05
         */
        //         private void displayCorrespondenceDetails(String stId, boolean nonEmployee){
        //             if((stId != null) && (stId.trim().length()>0 )){
        //                if(tblCorsData.getCellEditor()!=null){
        //                                tblCorsData.getCellEditor().
        //                                                cancelCellEditing();
        //                }
        //                if(nonEmployee){
        //                     //selected investigator is a rolodex, so show
        //                       //rolodex details
        //                    RolodexMaintenanceDetailForm frmRolodex
        //                       = new RolodexMaintenanceDetailForm('V',stId);
        //                    frmRolodex.showForm(mdiForm,DISPLAY_TITLE,true);
        //                }else{
        //                  try{
        //                      int selRow = tblCorsData.getSelectedRow();
        //                      String loginUserName = mdiForm.getUserName();
        //                      String personName=(String)tblCorsData.getValueAt(selRow,2);
        //                      PersonDetailForm personDetailForm =
        //                        new PersonDetailForm(personName,loginUserName,'D');
        //                  }catch(Exception exception){
        //                      exception.printStackTrace();
        //                  }
        //                }
        //            }
        //        }
        
        /**
         * This method is over loaded to handle the item state changed events.
         * @param e ItemEvent
         */
        public void itemStateChanged(ItemEvent e) {
            super.fireEditingStopped();
        }
        
        /**
         * This method is used to geth the component from the table selection.
         * @param table JTable instance.
         * @param value String, particular cell value
         * @param isSelected is the cell selected
         * @param row row number
         * @param column column number
         * @return Component editing component.
         */
        public Component getTableCellEditorComponent(JTable table,
        Object value,
        boolean isSelected,
        int row,
        int column){
            
            String newValue = ( String ) value ;
            if( newValue != null && newValue.length() > 1 ){
                txtName.setDocument( new LimitedPlainDocument( textLength ));
                txtName.setText( (String)value );
                
            }else{
                txtName.setText( "");
            }
            return txtName;
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
         * @return true if editing was stopped; false otherwise
         */
        public boolean stopCellEditing() {
            temporary = true;
            String editingValue = (String)getCellEditorValue();
            txtName.setText( editingValue );
            ProtocolCorrespondentsBean curBean = null;
            int selectedRow = tblCorsData.getSelectedRow();
            if( selectedRow < 0 ){
                return true;
            }
            String type = tblCorsData.getValueAt( selectedRow, 1 ).toString();
            // added by manoj to fix bug #21 file:IRB-SystemTestingDL-01.xls 26/08/2003
            String personName = txtName.getText();
            if(personName != null && !personName.trim().equals("") && checkDuplicateCorrespondent(personName.trim(),type,selectedRow)){
                ((NameEditor)tblCorsData.getCellEditor(selectedRow,2)).
                cancelCellEditing();
                CoeusOptionPane.showWarningDialog(
                coeusMessageResources.parseMessageKey(
                "protoCorroFrm_exceptionCode.1055"));
                return true;
            }
            boolean flag =false;
            String beanPersonName = null;
            String tablePersonName = null;

            if (currentCorrespondents  != null &&
            currentCorrespondents.size() > 0 ) {
                
                curBean =  (ProtocolCorrespondentsBean )
                currentCorrespondents.get(selectedRow);

                if(curBean != null){
                    beanPersonName = checkForNull( curBean.getPersonName() );
                }
                tablePersonName = (String)tblCorsData.getValueAt(selectedRow , 2 );
                
                if( editingValue != null && editingValue.length() <= 0 ){
                    editingValue = tablePersonName;
                }
                
                if( !beanPersonName.equalsIgnoreCase( editingValue ) ){

                    flag = checkForDuplicatePersonName( editingValue, type, selectedRow );
                }else{

                    flag = true;
                }
                if( flag ){
                    super.cancelCellEditing();
                    return flag;
                }
                
            }
            PersonInfoFormBean personInfoFormBean = null;
            if( curBean != null &&  curBean.getAcType()!= null && (
            curBean.getAcType().equalsIgnoreCase("I") )){

                
                //Bug Fix: Validation on focus lost Start 1
                //personInfoFormBan = getPersonInfoID(editingValue.trim());
                personInfoFormBean = validatePerson(editingValue.trim());
                //Bug Fix: Validation on focus lost End 1
                
                if( personInfoFormBean != null ) {

                    curBean.setPersonId( personInfoFormBean.getPersonID() );
                    curBean.setPersonName( personInfoFormBean.getFullName() );
                    curBean.setCorrespondentTypeDesc( type );
                    curBean.setCorrespondentTypeCode( Integer.parseInt(
                    getIDForName( type ).toString()) );
                    tblCorsData.getModel().
                    setValueAt(personInfoFormBean.getFullName(),selectedRow,2);
                    if(tblCorsData.getCellEditor() !=null){
                        tblCorsData.getCellEditor().cancelCellEditing();
                    }
                }else{
                    //Bug Fix: Validation on focus lost Start 2
                    /*JOptionPane.showMessageDialog(null,coeusMessageResources.parseMessageKey(
                    "protoCorroFrm_exceptionCode.1054"));*/
                    //Bug Fix: Validation on focus lost End 2

/*                    CoeusOptionPane.showWarningDialog(
                                coeusMessageResources.parseMessageKey(
                                        "protoCorroFrm_exceptionCode.1054"));*/
                    super.cancelCellEditing();
                    return true;
                }
            }else {

                if( ! beanPersonName.equalsIgnoreCase( editingValue ) ){
                    personInfoFormBean = getPersonInfoID(
                    editingValue.trim());
                    if( personInfoFormBean != null ) {

                        curBean.setPersonId( personInfoFormBean.getPersonID() );
                        curBean.setPersonName( personInfoFormBean.getFullName() );
                        curBean.setCorrespondentTypeDesc( type );
                        curBean.setCorrespondentTypeCode( Integer.parseInt(
                        getIDForName( type ).toString()) );
                        // Added to fix the updation issue - Start
                        if(!TypeConstants.INSERT_RECORD.equals(curBean.getAcType())){
                            curBean.setAcType(TypeConstants.UPDATE_RECORD);
                        }
                        // Added to fix the updation issue - Start                            
                        tblCorsData.getModel().
                        setValueAt(personInfoFormBean.getFullName(),selectedRow,2);
                        if(tblCorsData.getCellEditor() !=null){
                            tblCorsData.getCellEditor().cancelCellEditing();
                        }
                    }// added by manoj to fix bug #34 IRB-SystemTestingDL-01.xls 26/08/2003
                    else{
                            JOptionPane.showMessageDialog(null,coeusMessageResources.parseMessageKey(
                            "protoCorroFrm_exceptionCode.1054"));
/*                    CoeusOptionPane.showWarningDialog(
                                coeusMessageResources.parseMessageKey(
                                        "protoCorroFrm_exceptionCode.1054"));*/
                            
                            super.cancelCellEditing();
                            return true;
                    }
                    if( functionType != CoeusGuiConstants.DISPLAY_MODE )
                        saveRequired = true;
                    isFormDataUpdated = true;
                }
            }
            tblCorsData.getSelectionModel().setLeadSelectionIndex( selectedRow);
            return super.stopCellEditing();
        } // ends stop cell editing
        
        
        /** Returns the value contained in the editor.
         * @return Object the value contained in the editor
         */
        public Object getCellEditorValue() {
            return ((JTextField)txtName).getText();
        }
    }
    /** This method is checks for duplicate correspondents
     * @param person takes string contains new person name 
     * @param correspType string contains new Correspondent type
     * @param selectedRow integer contains selected row value
     * @return boolean it returns true if duplicate correspondent presents, false otherwise
     */
    private boolean checkDuplicateCorrespondent(String person,String correspType,int selectedRow){
        int rowCount = tblCorsData.getRowCount();
        for(int rowIndex =0;rowIndex<rowCount;rowIndex++){
            String exCorresp = (String)tblCorsData.getValueAt(rowIndex,1);
            String exPerson = (String)tblCorsData.getValueAt(rowIndex,2);
            if(exCorresp.equalsIgnoreCase(correspType) && exPerson.equalsIgnoreCase(person)){
                if(selectedRow != rowIndex){
                    return true;
                }
            }
        }
        return false;
    }
    
    //supporting to method get the code for respective combobox description.
    private String getIDForName( String selCorrespondentType ){
        String coresID = "1";
        ComboBoxBean comboEntry = null;
        if( comboBoxBeanData == null || selCorrespondentType == null ){
            return coresID;
        }
        //Modified for COEUSQA-2879_IACUC and IRB_Correspondents do not auto-populate for protocols created in Premium-Start
        for( int indx = 0; indx < comboBoxBeanData.size(); indx++ ){
            comboEntry = ( ComboBoxBean ) comboBoxBeanData.get( indx );
            String decriptionFromCodeTbl = ((String)comboEntry.getDescription()).trim();
            if( (decriptionFromCodeTbl).equalsIgnoreCase(
            selCorrespondentType.trim())  ){
                coresID = comboEntry.getCode();
                break;
            }
        }
        //Modified for COEUSQA-2879_IACUC and IRB_Correspondents do not auto-populate for protocols created in Premium-End
        return coresID;
    }
    
    
    /** This is method is raised/fired from FocusListener for Table/TextArea
     * component of this correspondance form when it gets the component
     * Focus.
     * @param focusEvent FocusEvent
     */
    public void focusGained( FocusEvent focusEvent ) {
        Object source = focusEvent.getSource();
        int selectedRow = tblCorsData.getSelectedRow();
        if( source.equals( txtArComments )){
            if( selectedRow == -1 && tblCorsData.getRowCount() > 0 ){
                showWarningMessage();
                tblCorsData.requestFocus();
            }
        }
        prvComments = txtArComments.getText();
    }
    
    /**
     * This is method is raised/fired from FocusListener for Table/TextArea
     * component of this correspondance form when it losts the component
     * Focus.
     * @param focusEvent FocusEvent
     */
    public void focusLost( FocusEvent focusEvent ) {
        if ( !focusEvent.isTemporary()) {
            
            Object source = focusEvent.getSource();
            int selectedRow = lastSelectedRow;
            tblCorsData.getSelectedRow();
            /*if( source.equals( txtArComments ) &&
                selectedRow != -1  ){                */
            if( source.equals( txtArComments ) &&
            lastSelectedRow <= tblCorsData.getRowCount()  ){
                //Internal_Issue#1804_correspondents Comments not Saved from CNTRL S_Start
                 updateComments(selectedRow);  
//                ProtocolCorrespondentsBean curBean =
//                (ProtocolCorrespondentsBean )
//                currentCorrespondents.get(selectedRow);
//                curBean.setComments( txtArComments.getText() );
//                
//                if( ( curBean.getAcType() == null ) ||
//                ( !curBean.getAcType().equalsIgnoreCase("I") )){
//                    
//                    // Updated Subramanya. Feb' 12 2003
//                    /* Bug Fix for :
//                     * The save confirmation message is popping up even if we are not any updating on the protocol details window
//                     * Ref: 206
//                     */
//                    if( !prvComments.equals( txtArComments.getText() ) ) {
////                        System.out.println("InSide focuslost >>>> 5 >>>>");
//                        curBean.setAcType( "U" );
////                        System.out.println("setting actype to U in focus lost");
//                        saveRequired = true;
//                        isFormDataUpdated = true;
//                    }
//                }
                //Internal_Issue#1804_correspondents Comments not Saved from CNTRL S_End
            }
        }
    }
    
    
    //supporting method to check the duplicate person
    private boolean checkForDuplicatePersonName( String perName, String type,
    int selBean ){
        
        boolean isDuplicate = false;
        ProtocolCorrespondentsBean curBean = null;
        int recordMatchCount = 0;
        for( int indx = 0 ; indx < currentCorrespondents.size(); indx ++ ){
            curBean =  (ProtocolCorrespondentsBean )
            currentCorrespondents.get( indx );
            if( curBean.getCorrespondentTypeDesc().equalsIgnoreCase(type.trim())
            && curBean.getPersonName().equalsIgnoreCase( perName.trim())
            && selBean != indx ){
                
                recordMatchCount++;
                if( recordMatchCount == 2 && functionType != CoeusGuiConstants.DISPLAY_MODE &&
                tblCorsData.getSelectedColumn() == 2 ){
                    CoeusOptionPane.showWarningDialog(
                    coeusMessageResources.parseMessageKey(
                    "protoCorroFrm_exceptionCode.1055"));
                    ((NameEditor)tblCorsData.getCellEditor(selBean,2)).
                    cancelCellEditing();
                    isDuplicate = true;
                    break;
                }
            }
            
        }
        //the person Name against the db avlue
        if( ! isDuplicate ) {
            //check against db value......................
            boolean nameExists = isPersonNameExists( perName );
            if( !nameExists ){
                TableCellEditor editor =
                tblCorsData.getCellEditor( selBean, 2 );
            }
        }
        return isDuplicate;
    }
    
    //supporting method to check person name against db
    private boolean isPersonNameExists( String perName ){
        boolean isPersonExists = true;
        PersonInfoFormBean personInfoFormBean =
        getPersonInfoID( perName.trim());
        if(personInfoFormBean == null ){
            return isPersonExists;
        }else if ( personInfoFormBean.getFullName()!= null &&
        ! personInfoFormBean.getFullName().trim().equalsIgnoreCase(
        perName.trim())){
            
            int selRow = tblCorsData.getSelectedRow();
            if( functionType != CoeusGuiConstants.DISPLAY_MODE &&
            tblCorsData.getSelectedColumn() == 2  ){
                
                CoeusOptionPane.showWarningDialog(
                coeusMessageResources.parseMessageKey(
                "protoCorroFrm_exceptionCode.1054"));
                ((NameEditor)tblCorsData.getCellEditor(
                selRow,2)).cancelCellEditing();
                ProtocolCorrespondentsBean curBean =
                (ProtocolCorrespondentsBean )
                currentCorrespondents.get(  selRow );
                if( !curBean.getAcType().equalsIgnoreCase("I")){
                    ((DefaultTableModel)tblCorsData.getModel()).setValueAt(
                    curBean.getPersonName(), selRow , 2 );
                }else{
                    ((DefaultTableModel)tblCorsData.getModel()).setValueAt("",
                    selRow , 2 );
                }
            }
            isPersonExists = false;
        }
        return isPersonExists;
    }
    
    //supporint method check the null value
    private boolean checkForNullEntries(){
        boolean isNullExists = false;
        ProtocolCorrespondentsBean leftComparatorBean = null;
        for( int indx = 0 ; indx < currentCorrespondents.size(); indx ++ ){
            leftComparatorBean =  (ProtocolCorrespondentsBean )
            currentCorrespondents.get( indx );
            if(  leftComparatorBean.getPersonName()!= null &&
            leftComparatorBean.getPersonName().equalsIgnoreCase("") ){
                isNullExists = true;
                break;
            }
            if(  leftComparatorBean.getPersonId() == null ||
            leftComparatorBean.getPersonId().equalsIgnoreCase("") ){
                isNullExists = true;
                break;
            }
            /*if( isNullExists ){
                break;
            }*/
        }
        return isNullExists;
    }
    
    
    //supporting method to check is there any duplicate in the complete list.
    private boolean checkForDuplicatePersonName(){
        boolean isDuplicateExists = false;
        ProtocolCorrespondentsBean leftComparatorBean = null;
        ProtocolCorrespondentsBean rightComparatorBean = null;
        
        for( int indx = 0 ; indx < currentCorrespondents.size(); indx ++ ){
            leftComparatorBean =  (ProtocolCorrespondentsBean )
            currentCorrespondents.get( indx );
            for( int innderIndx = indx+1 ; innderIndx <
            currentCorrespondents.size(); innderIndx ++ ){
                rightComparatorBean =  (ProtocolCorrespondentsBean )
                currentCorrespondents.get( innderIndx );
                if(
                rightComparatorBean.getPersonName().equalsIgnoreCase("") ||
                leftComparatorBean.getPersonName().equalsIgnoreCase("") ){
                    isDuplicateExists = true;
                    break;
                }else if( ( leftComparatorBean != null &&
                rightComparatorBean != null ) &&
                leftComparatorBean.getCorrespondentTypeDesc().equalsIgnoreCase(
                rightComparatorBean.getCorrespondentTypeDesc() )
                && leftComparatorBean.getPersonName().equalsIgnoreCase(
                rightComparatorBean.getPersonName()  )){
                    
                    isDuplicateExists = true;
                    break;
                }
            }
            if( isDuplicateExists ){
                break;
            }
        }
        
        return isDuplicateExists;
    }
    
    
    /** This method validate the form data before sending it to the database
     *
     * @return boolean true - all form controls are validation are successful
     *                false - validation fails
     * @throws Exception while validating the form Data.
     */
    public boolean validateData() throws Exception, CoeusUIException{
        
        //validation done at the component level.
        boolean isDuplicateExists = false;
        /*
        if( currentCorrespondents  != null && currentCorrespondents.size() < 1){
            log(coeusMessageResources.parseMessageKey(
                                        "protoCorroFrm_exceptionCode.1128"));
            return false;
        }*/
        
        //resetCorrespondentTypes();
        isDuplicateExists = checkForNullEntries();
        if( !isDuplicateExists ) {
            isDuplicateExists = checkForDuplicatePersonName();
        }
        
        if( isDuplicateExists ){
            tblCorsData.requestFocus();
            log(coeusMessageResources.parseMessageKey(
            "protoCorroFrm_exceptionCode.1129"));
            return false;
        }else{
            return true;
        }
    }
    
    /** This method is used to set the functionType for the form.
     * @param functionType sets the functionType
     */
    public void setFunctionType(char functionType){
        this.functionType = functionType;
        formatFields();
    }
    
    
    
    /** throws the exception with the given message
     * @param mesg String represent the log message.
     * @throws Exception exception instance
     */
    public void log(String mesg) throws CoeusUIException {
        //Commented by sharath(20 - Aug - 2003)
        //throw new Exception(mesg);
        
        //Bug Fix ( Defect Id : 379)
        CoeusUIException coeusUIException = new CoeusUIException(mesg,CoeusUIException.WARNING_MESSAGE);
        coeusUIException.setTabIndex(CoeusGuiConstants.IACUC_CORRESPONDENTS_EXCEP_TAB_INDEX);
        throw coeusUIException;
        //Bug Fix ( Defect Id : 379)
    }
    
    
    /**
     * This method is used to get the flag for the form data updates
     * @return boolean True is updated else False.
     */
    public boolean isFormDataUpdatedFlag(){
        isFormDataUpdated = false;
        ProtocolCorrespondentsBean compBean  = null;
        //get all current correspondents - from live table
        if ( currentCorrespondents != null && this.functionType != CoeusGuiConstants.DISPLAY_MODE ) {
            for( int indx = 0; indx < currentCorrespondents.size() ; indx ++ ){
                compBean = ( ProtocolCorrespondentsBean )
                currentCorrespondents.get(  indx );
                if( compBean.getAcType() != null &&
                ( compBean.getAcType().equalsIgnoreCase("U") ||
                compBean.getAcType().equalsIgnoreCase("I") )){
                    isFormDataUpdated = true;
                    break;
                }
            }
        }
        return isFormDataUpdated;
    }
    
    //supporting method to select the default row when the application invoked
    private void setDefaultRowSelection( int rowIndex ){
        if( tblCorsData.getRowCount() > rowIndex &&
                functionType != CoeusGuiConstants.DISPLAY_MODE){
            btnDelete.setEnabled( true );
            txtArComments.setEnabled( true );
            tblCorsData.setRowSelectionInterval( rowIndex, rowIndex );
            saveRequired = false;
            ProtocolCorrespondentsBean curBean = (ProtocolCorrespondentsBean)
            currentCorrespondents.get( rowIndex );
            txtArComments.setText( curBean.getComments() );
             txtArComments.setCaretPosition(0);
        }
    }
    
    /**
     * This method is used to set this form data.
     * @param corresPndData collection of correspondent entry beans.
     */
    public void setFormData( Vector corresPndData ){
        //Added for COEUSQA-2879 IACUC_Correspondents do not auto-populate for protocols created in Premium-Start
        deletedCorrespondents = new Vector();
        //Added for COEUSQA-2879 IACUC_Correspondents do not auto-populate for protocols created in Premium-End
        int selectedRow=0;
        if(tblCorsData.getRowCount()>0)
        {
            selectedRow=tblCorsData.getSelectedRow();
        }
        
        currentCorrespondents = null;
        currentCorrespondents = corresPndData;
        if( currentCorrespondents == null ){
            currentCorrespondents = new Vector();
        }
        tblCorsData.setModel( constructCorrespondandentTableModel() );
        setNameEditor( 2 );
        setHandIconDisplyColumn();
        setCorrespondentTypeColumn( 1 );
        //code modified for coeus4.3 Amendments/Reenwal enhancements
//        if(tblCorsData.getRowCount()>0)
//            setDefaultRowSelection( selectedRow );        
        if(tblCorsData.getRowCount()>0 && tblCorsData.getRowCount() > selectedRow){
            if(selectedRow == -1){
                selectedRow = 0;
            }
            setDefaultRowSelection( selectedRow );
        }
            
        
        //setDefaultRowSelection( 0 );
        isFormDataUpdated = false;
        saveRequired = false;
    }
    
    /**
     * This method is used to get the status flag of save operation.
     * @return boolean True is Save required Else False.
     */
    public boolean isSaveRequired(){
        
        if( this.functionType ==  CoeusGuiConstants.DISPLAY_MODE ){
            return false;
        }
        ProtocolCorrespondentsBean curBean =  null;
        //routine to check the default selected row(0) is modified
        if( currentCorrespondents != null && currentCorrespondents.size() > 0 ){
            int size = currentCorrespondents.size();
            for(int index = 0; index < size; index++){
                
                curBean = (ProtocolCorrespondentsBean)currentCorrespondents.get( index );
                String corType = tblCorsData.getValueAt(index, 1 ).toString().trim();


                // added to check desc change in modify mode only
                if( ! curBean.getCorrespondentTypeDesc().equalsIgnoreCase(corType)){
                    curBean.setCorrespondentTypeDesc( corType );
                    curBean.setCorrespondentTypeCode( Integer.parseInt( getIDForName( corType ) ) );
                    if(curBean.getAcType() != null){
                        if(!curBean.getAcType().equalsIgnoreCase("I")){

                            curBean.setAcType("U");
                        }
                    }else{

                        curBean.setAcType("U");
                    }

                    saveRequired = true;
                }else{
                    curBean.setCorrespondentTypeDesc( corType );
                    curBean.setCorrespondentTypeCode( Integer.parseInt( getIDForName( corType ) ) );
                }
                int selectedRow = tblCorsData.getSelectedRow();
                if( selectedRow != -1 ){
                    //Internal_Issue#1804_correspondents Comments not Saved from CNTRL S_Start
                    updateComments(selectedRow);                    
//                    curBean = (ProtocolCorrespondentsBean)
//                    currentCorrespondents.get( selectedRow );
//                    if( curBean.getComments()!= null &&
//                    !(curBean.getComments().equalsIgnoreCase(
//                    txtArComments.getText() )) ){
//                        curBean.setComments( txtArComments.getText().trim() );
//                        if( ( curBean.getAcType() == null )  ||
//                        (! curBean.getAcType().equalsIgnoreCase("I" ) )){
////                            System.out.println("InSide isSaveRequired >>>> 8 >>>>");
//                            curBean.setAcType("U");
////                            System.out.println("setting actype to U if selRow != -1");
//                            saveRequired = true;
//                            isFormDataUpdated = true;
//                        }
//                    }
                    //Internal_Issue#1804_correspondents Comments not Saved from CNTRL S_End
                }
            }
        }
        
        return saveRequired;
    }
    
    //Added by Amit 11/21/2003
    /** This method use to implement focus on first editable component in this page.
     */
    public void setDefaultFocusForComponent(){
        if(!( functionType == CoeusGuiConstants.DISPLAY_MODE )) {            
        
            if(tblCorsData .getRowCount() > 0 ) {
                tblCorsData.requestFocusInWindow();
                
                //included by raghu to remain the selection on row upon selection...
                //starts..
                int prevSelectedRow=tblCorsData.getSelectedRow();
                if(prevSelectedRow!=-1){
                    tblCorsData.setRowSelectionInterval(prevSelectedRow, prevSelectedRow);
                }
                else{
                    tblCorsData.setRowSelectionInterval(0, 0);
                }
                //ends
                
                tblCorsData.setColumnSelectionInterval(1,1);
            }else{
                btnAdd.requestFocusInWindow();
            }            
        }
    }    
    //end Amit    
    
    //supporting method to validate the person name against the db value
    private PersonInfoFormBean getPersonInfoID( String name ){
        
        RequesterBean requester = new RequesterBean();
        //requester.setFunctionType('Y');
        requester.setDataObject("GET_PERSONINFO");
        requester.setId(name);
        String connectTo = CoeusGuiConstants.CONNECTION_URL +
        "/coeusFunctionsServlet";
        AppletServletCommunicator comm
        = new AppletServletCommunicator(connectTo, requester);
        comm.send();
        ResponderBean response = comm.getResponse();
        PersonInfoFormBean personInfoFormBean = null;
        if ( response!=null ){
            personInfoFormBean = (PersonInfoFormBean) response.getDataObject();
        }
        
        return personInfoFormBean;
    }
    
    //Bug Fix: Validation on focus lost Start 3
    private PersonInfoFormBean validatePerson(String name){
        
        PersonInfoFormBean personInfoFormBean = null;
        RequesterBean requesterBean = new RequesterBean();
        ResponderBean responderBean = new ResponderBean();
        requesterBean.setFunctionType('J');
        requesterBean.setDataObject(name);
        
        String connectTo = CoeusGuiConstants.CONNECTION_URL + "/unitServlet";
        
        AppletServletCommunicator comm = new AppletServletCommunicator
        (connectTo, requesterBean);
        comm.setRequest(requesterBean);
        comm.send();
        responderBean = comm.getResponse();
        if(responderBean != null){
            if(responderBean.isSuccessfulResponse()) {
                personInfoFormBean =(PersonInfoFormBean)responderBean.getDataObject();
                if(personInfoFormBean.getPersonID() == null){
                    //((JTextField)source).setText(""); 
                    CoeusOptionPane.showErrorDialog(
                        coeusMessageResources.parseMessageKey("investigator_exceptionCode.1007"));
                    personInfoFormBean = null;
                }else if(personInfoFormBean.getPersonID().equalsIgnoreCase("TOO_MANY")){
                    //((JTextField)source).setText("");
                    CoeusOptionPane.showErrorDialog
                        ("\""+name+"\""+" " +coeusMessageResources.parseMessageKey("repRequirements_exceptionCode.1055"));
                    personInfoFormBean = null;
                }
            }else{
                Exception ex = responderBean.getException();
                ex.printStackTrace();
            }
        }
        return personInfoFormBean;
    }
    //Bug Fix: Validation on focus lost End 3
    
    
    /** Getter for property parentSequenceId.
     * @return Value of property parentSequenceId.
     */
    public int getParentSequenceId() {
        return parentSequenceId;
    }
    
    /** Setter for property parentSequenceId.
     * @param parentSequenceId New value of property parentSequenceId.
     */
    public void setParentSequenceId(int parentSequenceId) {
        this.parentSequenceId = parentSequenceId;
    }
    
    /**
     * This Class is Used to Create the Empty Header for the Table. This class
     * is instantiated to set the specified colum heaader empty. Usually this
     * kind of tabel render setting is done for Hand Icon selection provided
     * with its column heading to be empty.
     */
    class EmptyHeaderRenderer extends JList implements TableCellRenderer {
        
        /**
         * Default constructor to set the default foreground/background and
         * border properties of this renderer for a cell.
         */
        public EmptyHeaderRenderer() {
            
            setOpaque(true);
            setForeground(UIManager.getColor("TableHeader.foreground"));
            setBackground(UIManager.getColor("TableHeader.background"));
            setBorder(new EmptyBorder(0, 0, 0, 0));
            ListCellRenderer renderer = getCellRenderer();
            ((JLabel) renderer).setHorizontalAlignment(JLabel.CENTER);
            setCellRenderer(renderer);
            
        }
        
        /**
         * The Overridden method of TableCellRenderer which is called for
         * every cell when a component
         * is going to be rendered in its cell.
         * Returns the component used for drawing the cell.
         * This method is used to configure the renderer appropriately before
         * drawing
         *
         * @param table  the JTable that is asking the renderer to draw; can
         * be null
         * @param value  the value of the cell to be rendered. It is up to
         * the specific renderer to interpret and draw the value.
         * For example, if value is the string "true", it could be rendered
         * as a string or it could be rendered as a check box that is checked.
         * null is a valid value
         * @param isSelected  true if the cell is to be rendered with the
         * selection highlighted; otherwise false
         * @param hasFocus if true, render cell appropriately. For example, put
         * a special border on the cell, if the cell can be edited, render in
         * the color used to indicate editing
         * @param row the row index of the cell being drawn. When drawing the
         * header, the value of row is -1
         * @param column  the column index of the cell being drawn
         * @return Component editing component.
         */
        public Component getTableCellRendererComponent(JTable table,
        Object value,
        boolean isSelected, boolean hasFocus, int row, int column) {
            
            return this;
            
        }
        
    }
    
    /**
     * This class is used to set the Table column with Icon ( Hand Icon )
     * renderer. This class is instantiated to set the particular table column
     * model with this TableCellRenderer to provide the Hand Icon display
     * druing the Table Row Selection.
     */
    class IconRenderer  extends DefaultTableCellRenderer {
        
        //holds the Image Hand Icon  used in table column rendering.
        private  final  ImageIcon handIcon =
        new ImageIcon(getClass().getClassLoader().getResource(
        CoeusGuiConstants.HAND_ICON));
        //
        //        private  final  ImageIcon handIcon =
        //        new ImageIcon(getClass().getResource(
        //        CoeusGuiConstants.HAND_ICON));
        
        //holds the Image icon used as dummy setting
        private  final  ImageIcon emptyIcon = null;
        
        /** Default Constructor*/
        public IconRenderer() {
        }
        
        /**
         * An overridden method to render the component(icon) in cell.
         * foreground/background for this cell and Font too.
         *
         * @param table  the JTable that is asking the renderer to draw;
         * can be null
         * @param value  the value of the cell to be rendered. It is up to the
         * specific renderer to interpret and draw the value. For example,
         * if value is the string "true", it could be rendered as a string or
         * it could be rendered as a check box that is checked. null is a
         * valid value
         * @param isSelected  true if the cell is to be rendered with the
         * selection highlighted; otherwise false
         * @param hasFocus if true, render cell appropriately. For example,
         * put a special border on the cell, if the cell can be edited, render
         * in the color used to indicate editing
         * @param row the row index of the cell being drawn. When drawing the
         * header, the value of row is -1
         * @param column  the column index of the cell being drawn
         * @return Component editing component.
         *
         * @see TableCellRenderer#getTableCellRendererComponent(JTable, Object,
         * boolean, boolean, int, int)
         */
        public Component getTableCellRendererComponent(JTable table,
        Object value, boolean isSelected, boolean hasFocus, int row,
        int column) {
            
            setText((String)value);
            setOpaque(false);
            
            /* if row is selected the place the icon in this cell wherever this
               renderer is used. */
            if( isSelected ){
                setIcon(handIcon);
            }else{
                setIcon(emptyIcon);
            }
            return this;
        }
    }
    
    //Added for internal issue 1804 begin
    public void setCorrespondentsTextValues(){
          
        ProtocolCorrespondentsBean corrspBean = null;
        if(currentCorrespondents != null ){
            for(Object obj: currentCorrespondents){
                corrspBean = (ProtocolCorrespondentsBean)currentCorrespondents.elementAt(tblCorsData.getSelectedRow());
                if(corrspBean != null){
                     corrspBean.setComments(txtArComments.getText());
                }
            }
        }
    }
    
    /*This method is used to update the Comments
     *@param int selected row
     */
     private void updateComments(int selRow ) {
        ProtocolCorrespondentsBean prBean = null;
        if(currentCorrespondents != null && currentCorrespondents.size() > selRow){
           prBean = (ProtocolCorrespondentsBean )currentCorrespondents.get(selRow);
        }
        if(prBean != null){
            
            String acTyp = prBean.getAcType();
            String curComments = txtArComments.getText().trim();
            String beanComments = prBean.getComments()==null?"":prBean.getComments();
            if(!TypeConstants.DELETE_RECORD.equals(acTyp)){
            if( (!curComments.equalsIgnoreCase(beanComments)) ) {
                prBean.setComments( txtArComments.getText() );
                if( (acTyp == null) ||( !acTyp.equalsIgnoreCase(TypeConstants.INSERT_RECORD) )){
                    prBean.setAcType( TypeConstants.UPDATE_RECORD );
                }
                saveRequired = true;
                isFormDataUpdated = true;
            }
        }
            if(currentCorrespondents != null){
                currentCorrespondents.setElementAt(prBean,selRow);
            }
        }

    }
    
    //Added for internal issue 1804 end 
}

