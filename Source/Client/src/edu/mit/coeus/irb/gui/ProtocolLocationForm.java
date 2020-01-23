/*
 * @(#)ProtocolLocationForm.java  1.0  19/9/02
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

/* PMD check performed, and commented unused imports and variables on 16-FEB-2011
 * by Bharati
 */

package edu.mit.coeus.irb.gui;

import javax.swing.table.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.gui.*;
import edu.mit.coeus.irb.bean.ProtocolLocationListBean;
import edu.mit.coeus.rolodexmaint.bean.RolodexDetailsBean;
import edu.mit.coeus.utils.*;
import edu.mit.coeus.rolodexmaint.gui.*;
import edu.mit.coeus.search.gui.CoeusSearch;
import edu.mit.coeus.organization.gui.DetailForm;
import edu.mit.coeus.organization.bean.OrganizationAddressFormBean;
import edu.mit.coeus.exception.CoeusUIException;


/** This class is pluggable location component which gives the information
 * about the Location (Protocol) like Address, Place. This component provides
 * methods to <CODE>add/modify/delete</CODE> addresses.
 * @author ravikanth Created on October 24, 2002, 6:15 PM
 */
public class ProtocolLocationForm extends javax.swing.JComponent {

     /** used in searching for rolodex */
    private static final String ROLODEX_SEARCH = "rolodexSearch";
    
    /* used to display title in the search window */
    private static final String DISPLAY_TITLE = "DISPLAY ROLODEX";

    // Variables declaration - do not modify
    private javax.swing.JButton btnDeleteLocation;
    private javax.swing.JButton btnAddLocation;
    private javax.swing.JButton btnClearAddress;
    private javax.swing.JTextArea txtArAddress;
    private javax.swing.JScrollPane scrPnLocation;
    private javax.swing.JScrollPane scrPnAddress;
    private javax.swing.JTable tblLocation;
    private javax.swing.JButton btnAddAddress;
    // End of variables declaration
    
    /** char which specifies the mode in which the location form is opened */
    private char functionType = 'D';
    
    /** collection object which holds the location details in the form of 
        ProtocolLocationListBean. */
    private Vector locations = new Vector();
    
    /** HashMap which holds the location details as key value pairs with 
        OrganizationId as key and ProtocolLocationListBean as value to the corresponding location */
    private HashMap locationAddress;
    
    /** reference to CoeusAppletMDIForm */
    private CoeusAppletMDIForm mdiForm;
    
    /** Collection object which holds the names to be displayed in Location 
        table */
    private Vector columnNames;
    
    /** identifier which specifies the database table to used for searching the
        address like, Rolodex, Person etc. */
    private String searchIdentifier = "";
    
    /** integer value which holds the previous user selection in location table */
    private int previousSelRow = -1;

   //holds CoeusMessageResources instance used for reading message Properties.
    private CoeusMessageResources coeusMessageResources;
    
     private String contactAddress;
    
     private boolean saveRequired;
     
     private Vector organizationTypes = new Vector();
     // hold the value of the default organization type value 1 ( one ).
     private static final String DEFAULT_ORGANIZATION_TYPE = "1"; 
    /** Default Constructor used to create the <CODE>ProtocolLocationForm</CODE>.
     */
     // Added for Enable multicampus for default organization in protocols - Start
     private static final String GET_PARAMETER_VALUE = "GET_PARAMETER_VALUE";   
     private boolean isMulticampusOrg;
     // Added for Enable multicampus for default organization in protocols - End
    
    public ProtocolLocationForm(){
    }


    /** Creates new form <CODE>ProtocolLocationForm</CODE> with specified function type of the form,
     * Locations which already exists and parent window object for which
     * this component is attached.
     * @param functionType character which represents the mode in which the form is
     * opened. Ex: 'A' - Add Mode, 'M' - Modify Mode, 'D' - Display Mode
     * @param locations Collection of <CODE>ProtocolLocationListBean</CODE>s which already exists.
     * @param mdiForm reference to <CODE>CoeusAppletMDIForm</CODE>.
     */
    public ProtocolLocationForm(char functionType, Vector locations,Vector orgTypes,
    CoeusAppletMDIForm mdiForm) {
        this.mdiForm = mdiForm;
        this.organizationTypes = orgTypes;
        this.functionType = functionType;
        this.locations = locations;
        initComponents();

         //Added by Amit 11/18/2003
        if(functionType == CoeusGuiConstants.DISPLAY_MODE){
        java.awt.Color bgListColor = (java.awt.Color)javax.swing.UIManager.getDefaults().get("Panel.background");
            
            tblLocation.setBackground(bgListColor);            
        }
        else{
            tblLocation.setBackground(Color.white);            
        }
        //End Amit
        
        addMouseListenerToLocations();
        setTableColumnWidths();
        formatFields();
        coeusMessageResources = CoeusMessageResources.getInstance();
        // Modified for Enable multicampus for default organization in protocols - Start
//        //prps start - jul 17 2003
//        if ( functionType== 'A')// add mode
//        {   // then get the default location
//            setDefaultOrgAddress() ;
//            saveRequired = false ; // since this method is called when a new protocol is created no need to set the save flag
//        }
//        
//        //prps end
        checkDefOrgIsMultiCampus();
        if ( functionType== 'A' && !isMulticampusOrg) {  
            setDefaultOrgAddress() ;
            saveRequired = false ;
        }
        // Modified for Enable multicampus for default organization in protocols - End
        
        //prps end
    }
    
    private Vector getLocColumnNames(){
        if( columnNames == null ) {
            columnNames = new Vector(); 
            Enumeration enumColNames = tblLocation.getColumnModel().getColumns();
            while(enumColNames.hasMoreElements()){
                String strName = (String)((TableColumn)
                enumColNames.nextElement()).getHeaderValue();
                columnNames.addElement(strName);
            }
        }
        return columnNames;
    }
    
    
    private void addMouseListenerToLocations(){
        tblLocation.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent me) {
                if (me.getClickCount() == 2) {
                    DetailForm frmOrgDetailForm = null;
                    try {
                        int rowCount = tblLocation.getRowCount();
                        if(rowCount > 0){
                            int selectedRow = tblLocation.getSelectedRow();
                            if(selectedRow != -1){
                                String organizationID = (String)((DefaultTableModel)tblLocation.getModel()).getValueAt(selectedRow, 3);
                                if(organizationID != null){
                                    frmOrgDetailForm = new DetailForm(
                                            CoeusGuiConstants.DISPLAY_MODE, organizationID);
                                    if (frmOrgDetailForm != null) {    
                                        frmOrgDetailForm.showDialogForm(mdiForm);
                                    }
                                }
                            }
                        }
                    } catch (Exception e) {
                        CoeusOptionPane.showErrorDialog("Exception while opening Organization Details Window "+e.getMessage());
                    }
                }
            }
        });
    }

    /*supporting method to reset or format the fields with default's before
      filling data to the component
     */
    public void formatFields(){
        //code modified for coeus4.3 enhancements that UI to be in display mode
        //when new amendment or renewal is created
//        boolean enabled = functionType == 'D' ? false : true ;        
        boolean enabled = (functionType == 'D' || functionType == 'E') ? false : true ;
        txtArAddress.setEditable(false);
        txtArAddress.setFont(CoeusFontFactory.getNormalFont());
        btnAddAddress.setEnabled(enabled);
        btnAddLocation.setEnabled(enabled);
        btnClearAddress.setEnabled(enabled);
        if( this.tblLocation.getRowCount() > 0 ){
            btnDeleteLocation.setEnabled(enabled);
            btnAddAddress.setEnabled(enabled);
            btnClearAddress.setEnabled(enabled);
        }else{
            btnDeleteLocation.setEnabled(false);    
            btnAddAddress.setEnabled(false);
            btnClearAddress.setEnabled(false);
        }
        
        //Added by Amit 11/18/2003
        if(functionType == CoeusGuiConstants.DISPLAY_MODE){
        java.awt.Color bgListColor = (java.awt.Color)javax.swing.UIManager.getDefaults().get("Panel.background");
            
            tblLocation.setSelectionBackground(bgListColor );
            tblLocation.setSelectionForeground(Color.black);
        }
        else{
            tblLocation.setSelectionBackground(Color.white);
            tblLocation.setSelectionForeground(Color.black);
        }
        //End Amit        
    }

    /** This method is used to the set the previously selected location instances.
     * Usually this method is called during the process of syncronizing the data
     * after saving to the database.
     * @param loc Collection of <CODE>ProtocolLocationListBean</CODE> instances.
     */
    public void setLocations(Vector loc){
        this.locations = loc;
        reHashLocationAddress();
    }


    /* Supporting method to re-hash the location details for the purpose of 
     * rearranging data bean instances in the collection pool.
     */
    private void reHashLocationAddress(){
        if(locationAddress == null){
            locationAddress = new HashMap();
        }
        /* loop through all the available locations and create a hashtable 
           with location name as key and its corresponding location details which
           are in ProtocolLocationListBean as value.  And also change the table contents to
           reflect the new values. */
        if( (locations != null) &&  (locations.size()>0) ){
            ProtocolLocationListBean locationBean = null;
            Vector tableRowData = null;
            Vector tableData = new Vector();
            locationAddress.clear();
            for(int loopIndex=0;loopIndex<locations.size();loopIndex++){

                locationBean = (ProtocolLocationListBean)locations.elementAt(loopIndex);
                if(loopIndex == 0){
                    if( locationBean.getRolodexId() > 0 ){
                        btnClearAddress.setEnabled(false);
                    }
                }
                tableRowData = new Vector();
                /* to handle icon column */
                tableRowData.addElement("");
                // to hold organizaiton type
                tableRowData.addElement(locationBean.getOrganizationTypeName());
                tableRowData.addElement(locationBean.getOrganizationName());
                tableRowData.addElement(locationBean.getOrganizationId());        
                tableData.addElement(tableRowData);
                locationAddress.put(locationBean.getOrganizationId(),locationBean);
            }
            
            int prevSelectedRow=0;
            if(tblLocation.getRowCount()>0){
                prevSelectedRow=tblLocation.getSelectedRow();
            }
            ((DefaultTableModel)tblLocation.getModel()).setDataVector(tableData,
                    getLocColumnNames());
            ((DefaultTableModel)tblLocation.getModel()).fireTableDataChanged();
            setTableColumnWidths();
            //Code modified for coeus4.3 concurrent Amendments/Renewal enhancements
            //if selected row is less than the number of rows in the table,
            //then the selected row should be selected, otherwise the first row 
            //to be selected.
//            if(prevSelectedRow!=-1){
            if(prevSelectedRow!=-1 && tblLocation.getRowCount()>prevSelectedRow){
                tblLocation.setRowSelectionInterval(prevSelectedRow,prevSelectedRow);
            }
            else{
                tblLocation.setRowSelectionInterval(0,0);
            }
        }
    }

    /** Supporting method to get all the location details entered in this form.
     * This will be a collection of <CODE>ProtocolLocationListBean</CODE> instances.
     * @return Vector Collection of <CODE>ProtocolLocationListBean</CODE> instances.
     */
    public Vector getLocations(){
        int rowCount = tblLocation.getRowCount();
        locations = null;
        if( rowCount > 0 ) {
            locations = new Vector();
            /* loop through the location details entered by user and check  they
               are available in location table available. If available get the 
               corresponding LocationBean and add to the collection which will
               be sent back for saving into the database */
            if(locationAddress != null && locationAddress.size()>0){
                Set set = locationAddress.keySet();
                if(!set.isEmpty()){
                    Iterator iterator = set.iterator();
                    boolean found;
                    while(iterator.hasNext()){
                        found = false;
                        String orgId = (String)iterator.next();
                        if(orgId != null && orgId.trim().length()>0){
                            String tableOrgId = "";
                            int foundIndex=0;
                            for(int tableRow=0; tableRow < rowCount;tableRow++){
                                tableOrgId = (String)tblLocation.getValueAt(
                                tableRow,3);
                                if(tableOrgId != null){
                                    if(orgId.equals(tableOrgId)){
                                        found = true;
                                        foundIndex = tableRow;
                                        break;
                                    }
                                }
                            }
                            if(found){
                                ProtocolLocationListBean locBean
                                    = (ProtocolLocationListBean)locationAddress.get(
                                    orgId);
                                String orgTypeDesc = (String)tblLocation.getValueAt(foundIndex,1);
                                locBean.setOrganizationTypeId(getOrgTypeId( orgTypeDesc ));
                                locations.addElement(locBean);
                            }
                        }
                    }
                }
            }else{
                /* if there is only one location entered and if there is no 
                   address specified for that location then locationAddress 
                   hashtable will be empty so , create a new hashtable and 
                   create the ProtocolLocationListBean with only location name and send it
                   for saving. */
                
                /*locationAddress = new HashMap();
                ProtocolLocationListBean locBean = new ProtocolLocationListBean();
                // get Organization Id
                String orgId = (String)tblLocation.getValueAt(0,2);
                String orgName = ( String ) tblLocation.getValueAt(0,1);
                if( ( orgId != null ) && ( orgId.trim().length() > 0 ) ){
                    locBean.setOrganizationId( orgId );
                    locBean.setOrganizationName( orgName );
                    locationAddress.put(orgId,locBean);
                    locations.addElement(locBean);
                }*/
            }

        }
        return locations;
    }

    private int getOrgTypeId( String desc ) {
        ComboBoxBean bean;
        for( int indx = 0; indx < organizationTypes.size(); indx++) {
            bean = (ComboBoxBean) organizationTypes.get(indx);
            if( bean.getDescription().equalsIgnoreCase( desc ) ) {
                return Integer.parseInt( bean.getCode() );
            }
        }
        return 0;
    }
    
    private String getDefaultOrganizationType(){
        ComboBoxBean bean;
        for( int indx = 0; indx < organizationTypes.size(); indx++) {
            bean = (ComboBoxBean) organizationTypes.get(indx);
            if( bean.getCode().equalsIgnoreCase( DEFAULT_ORGANIZATION_TYPE ) ) {
                return bean.getDescription();
            }
        }
        return "";
    }
    
    // Added for Enable multicampus for default organization in protocols - Start
    /**
     * Method to get the organization type code
     * @return typeCode
     */
    public String getDefaultOrganizationTypeCode(){
        ComboBoxBean bean;
        for( int indx = 0; indx < organizationTypes.size(); indx++) {
            bean = (ComboBoxBean) organizationTypes.get(indx);
            if( bean.getCode().equalsIgnoreCase( DEFAULT_ORGANIZATION_TYPE ) ) {
                return bean.getCode();
            }
        }
        return "";
    }
    // Added for Enable multicampus for default organization in protocols - End

    /**
     * This method is used to set the fucntion type which specifies the form 
     * opened mode.
     * @param fType character which specifies the mode in which the form is 
     * opened. Ex: 'A' - Add Mode , 'M' - Modify Mode, 'D' - Display Mode.
     */
    public void setFunctionType(char fType){
        this.functionType = fType;
    }


    /** This method is used to set the identifier which specifies where the
     * address is to be searched.
     * @param search String identifier which specifies which database table
     * has to be searched like <CODE>Rolodex, Person</CODE> etc.
     */
    public void setSearchFrom(String search){
        this.searchIdentifier = search;
    }

    /**
     * This method is used to get the Search identifier which specifies which
     * database table will be used to search for address.
     * @return searchIdentifier String which specifies the database table used
     * for searching the address .
     */
    public String getSearchFrom(){
        return searchIdentifier;
    }
    public Vector getOrgTypeCodes(){
        Vector descVector = new Vector();
        Vector orgTypeCodes = organizationTypes;
        if(orgTypeCodes != null){
            int listSize = orgTypeCodes.size();
            for(int index = 0; index < listSize; index ++){
                descVector.addElement(((ComboBoxBean)orgTypeCodes.elementAt(index)).toString());
            }
        }
        return descVector;
    }

    /**
     * Supporting method to set the column widths of the Location display 
     * component.
     */
    private void setTableColumnWidths(){
       // setting the size and cell renderer to Icon Column
        TableColumn column = tblLocation.getColumnModel().getColumn(0);
        column.setMinWidth(30);
        column.setMaxWidth(30);
        column.setResizable(false);
        column.setCellRenderer(new IconRenderer());
        column.setHeaderRenderer(new EmptyHeaderRenderer());
        column.setPreferredWidth(30);
        // for organization Type
        column = tblLocation.getColumnModel().getColumn(1);
        if(organizationTypes != null && organizationTypes.size()>0){
            JComboBox  coeusCombo = new JComboBox(getOrgTypeCodes());
            coeusCombo.addItemListener(new ItemListener(){
                public void itemStateChanged(ItemEvent ie){
                    saveRequired = true;
                }
            });
            coeusCombo.setFont(CoeusFontFactory.getNormalFont());
            
            column.setCellEditor(new DefaultCellEditor(coeusCombo ));
        }
        //column.setPreferredWidth(120);
        column.setMinWidth(120);
        column = tblLocation.getColumnModel().getColumn(2);
        column.setMinWidth(215);
        //column.setHeaderRenderer(new EmptyHeaderRenderer());
        //OrganizationId column
        column = tblLocation.getColumnModel().getColumn(3);
        column.setMinWidth(0);
        column.setMaxWidth(0);
        column.setPreferredWidth(0);
        column.setResizable(false);
        column.setHeaderRenderer(new EmptyHeaderRenderer());
        
        /* setting blank header to the locations table */
        //tblLocation.setTableHeader(null);
        tblLocation.setShowVerticalLines(false);
        tblLocation.setShowHorizontalLines(false);       
        
        tblLocation.getTableHeader().setReorderingAllowed( false );
        tblLocation.getTableHeader().setFont(CoeusFontFactory.getLabelFont());
        tblLocation.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        //code modified for coeus4.3 enhancements that UI to be in display mode
        //when new amendment or renewal is created
//        if(tblLocation.getRowCount() > 0  && functionType != 'D'){
        if(tblLocation.getRowCount() > 0  && functionType != 'D' && functionType != 'E'){
            btnDeleteLocation.setEnabled(true);
            btnAddAddress.setEnabled(true);
        }
    }


    /** This method is called from within the constructor to
     * initialize the form.
     */
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        btnAddLocation = new javax.swing.JButton();
        btnDeleteLocation = new javax.swing.JButton();
        btnAddAddress = new javax.swing.JButton();
        btnClearAddress = new javax.swing.JButton();
        scrPnLocation = new javax.swing.JScrollPane();
        tblLocation = new javax.swing.JTable();
        scrPnAddress = new javax.swing.JScrollPane();
        txtArAddress = new javax.swing.JTextArea();
        txtArAddress.setOpaque(false);

        setLayout(new java.awt.GridBagLayout());

        btnAddLocation.setFont(CoeusFontFactory.getLabelFont());
        btnAddLocation.setText("Add");
        btnAddLocation.setMnemonic('A');
        btnAddLocation.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                btnAddLocationActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        //gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 5);
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 2);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHWEST;
        add(btnAddLocation, gridBagConstraints);

        btnDeleteLocation.setFont(CoeusFontFactory.getLabelFont());
        btnDeleteLocation.setText("Delete");
        // menmonic changed tp l to fix the bug id#8 
        // (CustomerReportedBugs_Tracking.xls) by manoj 02/09/2003
        btnDeleteLocation.setMnemonic('l');
        btnDeleteLocation.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                btnDeleteLocationActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
       // gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 5);
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 3);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        add(btnDeleteLocation, gridBagConstraints);

        btnAddAddress.setFont(CoeusFontFactory.getLabelFont());
        btnAddAddress.setText("Find Addr");
        btnAddAddress.setMnemonic('n');
        btnAddAddress.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                btnAddAddressActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        //gridBagConstraints.insets = new java.awt.Insets(20, 0, 0, 5);
        gridBagConstraints.insets = new java.awt.Insets(20, 0, 0, 2);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHWEST;
        add(btnAddAddress, gridBagConstraints);

        btnClearAddress.setFont(CoeusFontFactory.getLabelFont());
        btnClearAddress.setMnemonic('C');
        btnClearAddress.setText("Clear Addr");
        btnClearAddress.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                btnClearAddressActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        //gridBagConstraints.insets = new java.awt.Insets(5, 0, 5, 5);
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 5, 2);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        add(btnClearAddress, gridBagConstraints);

        scrPnLocation.setBorder(new TitledBorder(new EtchedBorder(),
                "Organization", TitledBorder.LEFT, TitledBorder.TOP,
                CoeusFontFactory.getLabelFont()));
        scrPnLocation.setPreferredSize(new java.awt.Dimension(380, 165));
        scrPnLocation.setMinimumSize(new java.awt.Dimension(380, 160));
        tblLocation.setModel(new DefaultTableModel(
            new Object [][] {
            },
            new String [] {
                "", "Type", "Organization", ""
            }
        ) {
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                //code modified for coeus4.3 enhancements that UI to be in display mode
                //when new amendment or renewal is created
//                if( columnIndex == 1 && functionType != 'D' ) {                
                if( columnIndex == 1 && functionType != 'D' && functionType != 'E' ) {
                    return true;
                }
                return false;
            }
        });
        tblLocation.setFont(CoeusFontFactory.getNormalFont());
        tblLocation.setRowHeight(22);
        tblLocation.setOpaque(false);
        if( tblLocation.getRowCount() > 0 ){
            tblLocation.setRowSelectionInterval(0,0);
        }
        tblLocation.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tblLocation.getSelectionModel().addListSelectionListener(
                                                  new TableSelectionListener());

        scrPnLocation.setViewportView(tblLocation);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;

        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 0); //(5,10,5,10)
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        add(scrPnLocation, gridBagConstraints);

        scrPnAddress.setBorder(new TitledBorder(new EtchedBorder(), "Address",
                TitledBorder.LEFT, TitledBorder.TOP,
                CoeusFontFactory.getLabelFont()));
        scrPnAddress.setPreferredSize(new Dimension(200, 165));
        scrPnAddress.setMinimumSize(new Dimension(200, 160));
        scrPnAddress.setViewportView(txtArAddress);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridheight = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        //gridBagConstraints.ipadx = 50;
        //gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 5); 
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 2); //(5,5,5,5)
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        add(scrPnAddress, gridBagConstraints);
        
        java.awt.Component[] components = {tblLocation,btnAddLocation, btnDeleteLocation, btnAddAddress, btnClearAddress};
        ScreenFocusTraversalPolicy  traversePolicy = new ScreenFocusTraversalPolicy( components );
        setFocusTraversalPolicy(traversePolicy);        
        setFocusCycleRoot(true);
        
        //Bug-fix to display Rolodex screen on double click on address - Start
        // Added by Vyjayanthi on 13/01/2004
        txtArAddress.addMouseListener( new MouseAdapter(){
            public void mouseClicked(MouseEvent me){
                if(me.getClickCount() == 2){
                    int selRow = tblLocation.getSelectedRow();
                    if(selRow != -1){
                        if( txtArAddress.getText().trim().equals("") ) return;
                        String key =  (String)tblLocation.getValueAt(selRow,3);
                        ProtocolLocationListBean locBean = new ProtocolLocationListBean();
                        if(locationAddress != null && locationAddress.containsKey(key)){
                            locBean = (ProtocolLocationListBean)locationAddress.get(key);
                            RolodexMaintenanceDetailForm frmRolodex = 
                                new RolodexMaintenanceDetailForm('V', String.valueOf(locBean.getRolodexId()) );
                            frmRolodex.showForm(mdiForm,DISPLAY_TITLE,true);
                        }
                    }
                }
            }
        });
        //Bug-fix to display Rolodex screen on double click on address - End
    }

    public boolean validateLocations() throws CoeusUIException{
        if( tblLocation.getRowCount() == 0 ){
            //Bug Fix( Defect Id : 379)
            throw new CoeusUIException( coeusMessageResources.parseMessageKey(
                                    "protoMntFrm_exceptionCode.1079") );
        }else{
            int locCount = tblLocation.getRowCount();
            String orgTypeVal;
            String orgName;
            for( int indx = 0; indx < locCount; indx++ ) {
                orgTypeVal = (String)tblLocation.getValueAt(indx,1);
                orgName = (String) tblLocation.getValueAt(indx,2);
                if( orgTypeVal == null || orgTypeVal.trim().length() == 0 ) {
                    //Bug Fix( Defect Id : 379)
                    throw new CoeusUIException( "Please select Organization Type for "+ orgName);
                }
            }
        }
        return true;
    }

    //Added by Nadh to fix Bug#598  11 aug 2004
    public java.awt.Component getFormComponents() {
        java.awt.Component component = tblLocation;
        return component;
    }//end Nadh 11 aug 2004
    //supporting method to handle the action events for the button clear event.
    private void btnClearAddressActionPerformed(ActionEvent evt) {
        // Add your handling code here:
        int rowCount = tblLocation.getRowCount();
        if( rowCount > 0 ) {
            int selRow = tblLocation.getSelectedRow();
            if(selRow != -1){
                String key =  (String)tblLocation.getValueAt(selRow,3);
                ProtocolLocationListBean locBean = new ProtocolLocationListBean();
                if(locationAddress != null && locationAddress.containsKey(key)){
                    locBean = (ProtocolLocationListBean)locationAddress.get(key);
                    if( ( locBean.getRolodexId() > 0 )
                            && ( locBean.getAddress() !=null ) ){
                        int option = CoeusOptionPane.showQuestionDialog(
                                    "Are you sure you want to delete this address ?",
                                    CoeusOptionPane.OPTION_YES_NO,
                                    CoeusOptionPane.DEFAULT_YES);
                        if(option == JOptionPane.YES_OPTION){
                            /* if the user wishes to clear the address for a 
                               specified location clear the values and set the 
                               bean in the hashtable */
                            locBean.setRolodexId(0);
                            locBean.setAddress(null);
                            locationAddress.put(key,locBean);
                            txtArAddress.setText("");
                            btnClearAddress.setEnabled(false);
                            saveRequired = true;
                        }
                    }
                }
            }
        }
    }


    //supporting method to handle the action event for add address button action
    private void btnAddAddressActionPerformed(ActionEvent evt) {
        // Add your handling code here:
        int rowCount = tblLocation.getRowCount();
        if( rowCount > 0 ) {
            int selRow = tblLocation.getSelectedRow();
            String orgId = null;
            if(selRow != -1){
                orgId = (String)tblLocation.getValueAt(selRow,3);
            }
            if(orgId != null && orgId.trim().length() > 0){
                /* if the user has entered location then show the Rolodex search
                   window for selecting the address. */
                try{
                    CoeusSearch coeusSearch =
                            new CoeusSearch(mdiForm, getSearchFrom(), 1);
                    coeusSearch.showSearchWindow();
                    HashMap personInfo = coeusSearch.getSelectedRow();
                    if(personInfo !=null){
                        if( getSearchFrom().equals( ROLODEX_SEARCH ) ){
                            /* construct the full name of person */
                            String locationId = "";
                            String address = "";
                            String organization = "";
                            String address1 = "";
                            String address2 = "";
                            String address3 = "";
                            String city = "";
                            String county = "";
                            String state = "";
                            String pinCode = "";
                            String country = "";

                            locationId = Utils.convertNull(
                                personInfo.get("ROLODEX_ID"));

                            organization = Utils.convertNull(
                                personInfo.get("ORGANIZATION"));

                            address1 = Utils.convertNull(
                                personInfo.get("ADDRESS_LINE_1"));

                            address2 = Utils.convertNull(
                                personInfo.get("ADDRESS_LINE_2"));

                            address3 = Utils.convertNull(
                                personInfo.get("ADDRESS_LINE_3"));

                            city = Utils.convertNull(
                                personInfo.get("CITY"));

                            county = Utils.convertNull(
                                personInfo.get("COUNTY"));

                            state = Utils.convertNull(
                                personInfo.get("STATE"));

                            pinCode = Utils.convertNull(
                                personInfo.get("POSTAL_CODE"));

                            country = Utils.convertNull(
                                personInfo.get("COUNTRY_CODE"));
                                
                            address1 = (address1 == "" )? address1 
                                : "\n"+address1;    
                            address2 = (address2 == "" )? address2 
                                : "\n"+address2;    
                            address3 = (address3 == "" )? address3 
                                : "\n"+address3;    
                            city = (city == "" )? city : "\n"+city;    
                            county = (county == "" )? county 
                                : "\n"+county;    
                            state = (state == "" )? state 
                                : "\n"+state;    
                            if(state.length() >0 && pinCode.length() > 0 ){
                                pinCode = " - " + pinCode;
                            }
                            country = (country == "" )? country 
                                : "\n"+country;    
                                
                            address = organization+address1+address2
                            +address3+city+county+state+pinCode+country;

                            txtArAddress.setText(address);
                            btnClearAddress.setEnabled(true);

                            ProtocolLocationListBean locBean = new ProtocolLocationListBean();
                            String key=(String)tblLocation.getValueAt(selRow,3);
                            if(locationAddress == null){
                                locationAddress = new HashMap();
                            }else if(locationAddress.containsKey(key)){
                                /* if any entry already available with the 
                                   specified location, then replace its address
                                   in locationBean and update in hashtable */
                                locBean = (ProtocolLocationListBean)locationAddress.get(key);
                                if(locBean.getOrganizationId() != null){
                                    if(!locBean.getOrganizationId().equals(key)){
                                        locationAddress.remove(
                                                         locBean.getOrganizationId());
                                    }
                                }
                            }
                            locBean.setOrganizationTypeId(
                                getOrgTypeId( (String)tblLocation.getValueAt(selRow,1)));
                            locBean.setOrganizationId((String)tblLocation.getValueAt(
                                selRow,3));
                            locBean.setOrganizationName((String)tblLocation.getValueAt(
                                selRow,2));
                            locBean.setAddress(address);
                            locBean.setRolodexId(Integer.parseInt(locationId));
                            locationAddress.put(key,locBean);
                            saveRequired = true;
                        }
                    }
                }catch(Exception ex){
                    CoeusOptionPane.showErrorDialog(ex.getMessage());
                }
            }else{
                CoeusOptionPane.showErrorDialog("Please enter a location"
                    +" name before selecting location\'s address");
            }
        }
    }


    //supporting method to handle Action Event for the button delete action.
    private void btnDeleteLocationActionPerformed(ActionEvent evt) {
        // Add your handling code here:
        int tableRowCount = tblLocation.getRowCount();
        if(tableRowCount >0){
            // allow to delete locations only if there are 2 or more locations
            int row = tblLocation.getSelectedRow();

            String key = (String)tblLocation.getValueAt(row,3);
            ProtocolLocationListBean locBean = new ProtocolLocationListBean();
            if(locationAddress != null && locationAddress.containsKey(key)){
                locBean = (ProtocolLocationListBean)locationAddress.get(key);
                String orgId = locBean.getOrganizationId();
                String orgName = locBean.getOrganizationName();
                int option = JOptionPane.NO_OPTION;
                //modified for internal issue #73 : Valid Message While deleting Organization and Unit
                if(orgId != null && orgId.trim().length()>0){
                    option = CoeusOptionPane.showQuestionDialog(
                        "Are you sure you want to remove "+orgName +
                        "?",
                        CoeusOptionPane.OPTION_YES_NO,
                        CoeusOptionPane.DEFAULT_YES);
                    if(option == JOptionPane.YES_OPTION){
                        /* if the user wishes to delete any location then remove
                           the location from the table as well as from the 
                           hashtable */
                        saveRequired = true;
                        locationAddress.remove(key);
                        ((DefaultTableModel)tblLocation.getModel()).removeRow(
                            row);
                        ((DefaultTableModel)tblLocation.getModel()
                        ).fireTableDataChanged();
                        int newRowCount = tblLocation.getRowCount();
                        if(newRowCount >0){
                            if(newRowCount > row){
                                tblLocation.setRowSelectionInterval(row,
                                    row);
                            }else{
                                tblLocation.setRowSelectionInterval(
                                    newRowCount - 1,
                                    newRowCount - 1);
                            }
                        }else{
                            txtArAddress.setText("");         
                            //tblLocation.setRowSelectionInterval(0,0);
                            btnDeleteLocation.setEnabled(false);            
                            btnAddAddress.setEnabled( false );
                            btnClearAddress.setEnabled( false );
                        }
                    }
                }
            }
        }

    }


    //supporting method to handle action events for button add location event.
    private void btnAddLocationActionPerformed(ActionEvent evt) {
        
        String orgId = "";
        String orgName = "";
        String contactAddressID="";
        String address="";
        try{
            CoeusSearch coeusSearch = new CoeusSearch(
                        CoeusGuiConstants.getMDIForm(), "ORGANIZATIONSEARCH", 1);
            coeusSearch.showSearchWindow();
            HashMap orgSelected = coeusSearch.getSelectedRow();
            if (orgSelected != null && !orgSelected.isEmpty() ) {
                orgName = Utils.convertNull(orgSelected.get(
                                                    "ORGANIZATION_NAME"));
                orgId = Utils.convertNull(orgSelected.get("ORGANIZATION_ID"));
                contactAddressID = ""+ orgSelected.get(
                                                        "CONTACT_ADDRESS_ID");
                
                address = getOrgAddress(contactAddressID);
            }
            if(orgId != null && orgId.trim().length() > 0 ) {
                // Add your handling code here:
                int tableRowCount = tblLocation.getRowCount();
                boolean duplicate = false;
                int selRow = tblLocation.getSelectedRow();
                if(tableRowCount >0){
                    for(int row=0;row < tableRowCount ; row++){
                        String existOrgId = (String)tblLocation.getValueAt(row,3);
                        // check for duplicate location names and inform the user
                        //   about it 
                        if(existOrgId.equals(orgId)){
                            CoeusOptionPane.showInfoDialog(
                            "You have entered same location");
                            tblLocation.setRowSelectionInterval(row,row);
                            duplicate = true;
                            break;
                        }

                    }
                 }
                if(!duplicate){
                    saveRequired = true;
                    if(locationAddress == null){
                        locationAddress = new HashMap();
                    }
                    ProtocolLocationListBean locBean = new ProtocolLocationListBean();
                    locBean.setOrganizationName(orgName);
                    locBean.setOrganizationId(orgId);        
                    locBean.setRolodexId(Integer.parseInt(contactAddressID));
                    locBean.setAddress(address);
                    //Added for COEUSQA-2879_LOG_IACUC and IRB_Correspondents do not auto-populate-Start
                    locBean.setAcType(TypeConstants.INSERT_RECORD);
                    //Added for COEUSQA-2879_LOG_IACUC and IRB_Correspondents do not auto-populate-End
                    locationAddress.put(orgId,locBean);

                    Vector tableRow = new Vector();
                    tableRow.addElement("");
                    // for organization type
                    tableRow.addElement(getDefaultOrganizationType());
                    tableRow.addElement(orgName);
                    tableRow.addElement(orgId);
                    ((DefaultTableModel)tblLocation.getModel()
                            ).addRow(tableRow);
                    ((DefaultTableModel)tblLocation.getModel()
                            ).fireTableDataChanged();

                    int lastRow = tblLocation.getRowCount();
                    tblLocation.setRowSelectionInterval(lastRow-1,
                    lastRow-1);
                  txtArAddress.setText(address);
                  btnClearAddress.setEnabled( true );
                }else{
                  txtArAddress.setText(txtArAddress.getText());
                }
                //txtArAddress.setText(address);
                btnDeleteLocation.setEnabled(true);  
                btnAddAddress.setEnabled( true );
            }
        }catch(Exception ex){
            CoeusOptionPane.showInfoDialog(ex.getMessage());
        }
    }

     /**
     * Gets the Rolodex address which is used to display below the Organization
     * Name.
     *
     * @param contactAddressID(rolodex ID)
     * @return boolean
     *  true - organization id is already available
     *  false - organization is is not available
     */
    public String getOrgAddress(String contactAddressID) {
        boolean duplicate = false;
        String connectTo = CoeusGuiConstants.CONNECTION_URL + "/coeusFunctionsServlet";
        RequesterBean request = new RequesterBean();
        request.setDataObject("GET_ROLODEXINFO");
        request.setId(contactAddressID);
        AppletServletCommunicator comm = new AppletServletCommunicator(connectTo, request);
        comm.send();
        ResponderBean response = comm.getResponse();
        if (response == null) {
            response = new ResponderBean();
            response.setResponseStatus(false);
            response.setMessage(coeusMessageResources.parseMessageKey(
                                            "server_exceptionCode.1000"));
        }
        if (response.isSuccessfulResponse()) {
            RolodexDetailsBean rolodexDetailsBean = (RolodexDetailsBean) 
                                                    response.getDataObject();
            if (rolodexDetailsBean != null) {
                contactAddress = Utils.convertNull(
                                            formatAddress(rolodexDetailsBean));
            }
        } else {
            log(response.getMessage());
        }
        return contactAddress;
    }
    
    
     /**
     * This method is used to format the address for organization
     * @param rolodexDetailsBean
     * @return a <CODE>String</CODE> representating the formatted address.
     */
    public String formatAddress(RolodexDetailsBean rolodexDetailsBean){
        if (rolodexDetailsBean != null ) {
           /* String firstName = Utils.convertNull(
                                    rolodexDetailsBean.getFirstName());
            String middleName = Utils.convertNull(
                                    rolodexDetailsBean.getMiddleName());
            String lastName = Utils.convertNull(
                                    rolodexDetailsBean.getLastName());
            String prefix = Utils.convertNull(
                                    rolodexDetailsBean.getPrefix());
            String suffix = Utils.convertNull(
                                    rolodexDetailsBean.getSuffix());
            if (lastName.length() > 0) {
                contactAddress = (lastName + " "+ suffix +", "+ 
                    prefix + " "+ firstName + " "+ middleName).trim() + "\n";
            } else {
                contactAddress = Utils.convertNull(
                            rolodexDetailsBean.getOrganization()) + "\n";
            }
            String temp = rolodexDetailsBean.getOrganization();
            if (temp != null && temp.length() > 0) {
                if (!temp.equals(contactAddress)) {
                    contactAddress = contactAddress + temp + "\n";
                } else {
                    contactAddress = temp + "\n";
                }
            }*/
            
            String temp = rolodexDetailsBean.getOrganization();
            if (temp != null && temp.length() > 0) {
                contactAddress = temp + "\n";
            }

            temp = rolodexDetailsBean.getAddress1();
            if (temp != null && temp.length() > 0) {
                contactAddress = contactAddress + temp + "\n";
            }
            temp = rolodexDetailsBean.getAddress2();
            if (temp != null && temp.length() > 0) {
                contactAddress = contactAddress + temp + "\n";
            }
            temp = rolodexDetailsBean.getAddress3();
            if (temp != null && temp.length() > 0) {
                contactAddress = contactAddress + temp + "\n";
            }
            temp = rolodexDetailsBean.getCity();
            if (temp != null && temp.length() > 0) {
                contactAddress = contactAddress + temp + "\n";
            }
            temp = rolodexDetailsBean.getState();
            if (temp != null && temp.length() > 0) {
                Vector states = rolodexDetailsBean.getStates();
                ComboBoxBean stateBean;
                for(int loopIndex = 0; loopIndex < states.size(); loopIndex++) {
                    stateBean = (ComboBoxBean) states.elementAt(loopIndex);
                    if (stateBean.getCode().equals(temp)) {
                        temp = stateBean.getDescription();
                    }
                }
            }

            if (temp != null && temp.length() > 0) {
                contactAddress = contactAddress + temp + " ";
            }
            temp = rolodexDetailsBean.getPostalCode();
            if (temp != null && temp.length() > 0) {
                contactAddress = contactAddress + temp + ", ";
            }
            temp = rolodexDetailsBean.getCountry();
            if (temp != null && temp.length() > 0) {
                Vector countries = rolodexDetailsBean.getCountries();
                ComboBoxBean countryBean;
                for(int loopIndex = 0 ; loopIndex < countries.size() ; loopIndex++ ){
                    countryBean = (ComboBoxBean) countries.elementAt(loopIndex);
                    if (countryBean.getCode().equals(temp)) {
                        temp = countryBean.getDescription();
                    }
                }
            }

            if (temp != null && temp.length() > 0) {
                contactAddress = contactAddress + temp + " ";
            }
            contactAddress = contactAddress;

        } 
        return Utils.convertNull(contactAddress); 
        
    }
    
    /**
     * This class is used to capture action event of the Table Selection. If 
     * user modifies any location details and changes the row selection, then this
     * class will capture the selection change and updates the hashtable entry 
     * for the previous selected row.
     */
    class TableSelectionListener implements ListSelectionListener{
        public void valueChanged(ListSelectionEvent e) {
            int selRow = tblLocation.getSelectedRow();
            int rowCount = tblLocation.getRowCount();
            if(selRow != -1){
                /*if(previousSelRow != -1 && rowCount > previousSelRow){

                    String key = (String)tblLocation.getValueAt(previousSelRow,
                                                                            2);
                    // check whether any location name entered for the previously
                    //   selected row 
                    if(key != null && key.trim().length()>0){
                        ProtocolLocationListBean locBean = new ProtocolLocationListBean();
                        if(locationAddress == null){
                            locationAddress = new HashMap();
                        }
                        // if the location name of the previously selected row
                        //   already exists in the hashtable update that entry with
                        //   new details otherwise create a new entry for that 
                        //   location 
                        if(locationAddress.containsKey(key)){
                            locBean = (ProtocolLocationListBean)locationAddress.get(key);
                        }
                        locBean.setOrganizationId((String)tblLocation.getValueAt(
                                previousSelRow,2));
                        locBean.setOrganizationName((String)tblLocation.getValueAt(
                                previousSelRow,1));
                        locationAddress.put(key,locBean);
                    }
                }*/
                /* check whether any address associated with the newly selected
                   location. If present, update the address textarea with the 
                   corresponding address. Otherwise clear the contents of the
                   address textarea */
                String key = (String)tblLocation.getValueAt(selRow,3);
                ProtocolLocationListBean locBean = new ProtocolLocationListBean();
                if( (locationAddress != null)
                        && (locationAddress.containsKey(key))){
                    locBean = (ProtocolLocationListBean)locationAddress.get(key);
                }
                if( (locBean.getAddress() == null )
                        || (locBean.getAddress().trim().length() == 0) ){
                    btnClearAddress.setEnabled(false);
                    txtArAddress.setText("");
                }else{
                    //code modified for coeus4.3 enhancements that UI to be in display mode
                    //when new amendment or renewal is created
//                    if(functionType != 'D'){                    
                    if(functionType != 'D' && functionType != 'E'){
                        btnClearAddress.setEnabled(true);
                    }
                    txtArAddress.setText(locBean.getAddress());
                }
                previousSelRow = selRow;
            }
        }
    }
    
    /**
     * display alert message
     *
     * @param mesg the message to be displayed
     */
    public void log(String mesg) {

        CoeusOptionPane.showErrorDialog(mesg);
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
    
    
    //prps start - jul 17 2003
    
    // Added for Enable multicampus for default organization in protocols - Start
    /*
     * Method to check the default organization is to be MULTICAMPUS organization
     *
     */
    public void checkDefOrgIsMultiCampus() {
        String connectTo = CoeusGuiConstants.CONNECTION_URL + "/coeusFunctionsServlet";
        RequesterBean request = new RequesterBean();
        request.setDataObject(GET_PARAMETER_VALUE);
        Vector vecParameter = new Vector();
        vecParameter.add("DEFAULT_ORGANIZATION_ID");
        request.setDataObjects(vecParameter);
        AppletServletCommunicator comm = new AppletServletCommunicator(connectTo, request);
        comm.send();
        ResponderBean response = comm.getResponse();
        if (response.isSuccessfulResponse()) {
            String defaultOrgId = (String)response.getDataObject();
            if(defaultOrgId != null && CoeusConstants.MULTICAMPUS_DEFAULT_ORG_ID.equalsIgnoreCase(defaultOrgId)){
                isMulticampusOrg = true;
            }
            
        }
    }
    // Added for Enable multicampus for default organization in protocols - End
    
     /**
     * This method sets the default location when a new protocol is created
     *
     */
    public void setDefaultOrgAddress()
    {
        String orgId = "";
        String orgName = "";
        String contactAddressID="";
        String address="";
        boolean duplicate = false;
        String connectTo = CoeusGuiConstants.CONNECTION_URL + "/coeusFunctionsServlet";
        RequesterBean request = new RequesterBean();
        request.setDataObject("GET_DEFAULT_LOCATION");
        //request.setId(contactAddressID);
        AppletServletCommunicator comm = new AppletServletCommunicator(connectTo, request);
        comm.send();
        ResponderBean response = comm.getResponse();
        if (response == null) {
            response = new ResponderBean();
            response.setResponseStatus(false);
            response.setMessage(coeusMessageResources.parseMessageKey(
                                            "server_exceptionCode.1000"));
        }
        if (response.isSuccessfulResponse()) {
            Vector vecObjects = response.getDataObjects() ;
            
            OrganizationAddressFormBean organizationAddress = (OrganizationAddressFormBean)vecObjects.get(0) ;
            
            RolodexDetailsBean rolodexDetailsBean = (RolodexDetailsBean) vecObjects.get(1) ;
            // Modified for Enable multicampus for default organization in protocols - Start
            if (organizationAddress!= null && rolodexDetailsBean != null) {
                orgId = response.getId();
            }
            setDefaultLocation(orgId,organizationAddress,rolodexDetailsBean);
                                                  
//            if (organizationAddress!= null && rolodexDetailsBean != null) 
//            {
//                orgId = response.getId();
//                orgName = Utils.convertNull(organizationAddress.getOrganizationName()) ;
//                contactAddressID = ""+ organizationAddress.getContactAddressId();
//           
//                address = Utils.convertNull(
//                                            formatAddress(rolodexDetailsBean));
//                try
//                {
//                    if(orgId != null && orgId.trim().length() > 0 ) {
//                    // Add your handling code here:
//                    int tableRowCount = tblLocation.getRowCount();
//                    int selRow = tblLocation.getSelectedRow();
//                        
//                        if(locationAddress == null){
//                            locationAddress = new HashMap();
//                        }
//                        ProtocolLocationListBean locBean = new ProtocolLocationListBean();
//                        locBean.setOrganizationName(orgName);
//                        locBean.setOrganizationId(orgId);        
//                        locBean.setRolodexId(Integer.parseInt(contactAddressID));
//                        locBean.setAddress(address);
//                        locationAddress.put(orgId,locBean);
//
//                        Vector tableRow = new Vector();
//                        tableRow.addElement("");
//                        tableRow.addElement(getDefaultOrganizationType());
//                        tableRow.addElement(orgName);
//                        tableRow.addElement(orgId);
//                        ((DefaultTableModel)tblLocation.getModel()
//                                ).addRow(tableRow);
//                        ((DefaultTableModel)tblLocation.getModel()
//                                ).fireTableDataChanged();
//
//                        int lastRow = tblLocation.getRowCount();
//                        tblLocation.setRowSelectionInterval(lastRow-1,
//                        lastRow-1);
//                      txtArAddress.setText(address);
//                      btnClearAddress.setEnabled( true );
//                      btnDeleteLocation.setEnabled(true);  
//                      btnAddAddress.setEnabled( true );
//                      
//                }
//            }catch(Exception ex){
//                CoeusOptionPane.showInfoDialog(ex.getMessage());
//            }
//                
//            }
            // Modified for Enable multicampus for default organization in protocols - End
        } else {
            log(response.getMessage());
        }
               
    }
    //prps end

    // Added for Enable multicampus for default organization in protocols - Start
    /**
     * Method to get is default organization is to be MULTICAMPUS
     * @return isMulticampusOrg
     */
    public boolean isMulticampusOrg() {
        return isMulticampusOrg;
    }
    
    /**
     * Method to set is default organization is to be MULTICAMPUS
     * @param isMulticampusOrg 
     */
    public void setIsMulticampusOrg(boolean isMulticampusOrg) {
        this.isMulticampusOrg = isMulticampusOrg;
    }
    
    /**
     * Method to set the organization details
     * @param orgId
     * @param organizationAddress
     * @param rolodexDetailsBean
     */
    public void setDefaultLocation(String orgId, OrganizationAddressFormBean organizationAddress,
            RolodexDetailsBean rolodexDetailsBean){
        if (organizationAddress!= null && rolodexDetailsBean != null) {
            String orgName = Utils.convertNull(organizationAddress.getOrganizationName()) ;
            String contactAddressID = ""+ organizationAddress.getContactAddressId();
            
            String address = Utils.convertNull(
                    formatAddress(rolodexDetailsBean));
            try {
                if(orgId != null && orgId.trim().length() > 0 ) {
                    int tableRowCount = tblLocation.getRowCount();
                    int selRow = tblLocation.getSelectedRow();
                    
                    if(locationAddress == null){
                        locationAddress = new HashMap();
                    }
                    ProtocolLocationListBean locBean = new ProtocolLocationListBean();
                    locBean.setOrganizationName(orgName);
                    locBean.setOrganizationId(orgId);
                    locBean.setRolodexId(Integer.parseInt(contactAddressID));
                    locBean.setAddress(address);
                    locationAddress.put(orgId,locBean);
                    
                    Vector tableRow = new Vector();
                    tableRow.addElement("");
                    tableRow.addElement(getDefaultOrganizationType());
                    tableRow.addElement(orgName);
                    tableRow.addElement(orgId);
                    ((DefaultTableModel)tblLocation.getModel()).addRow(tableRow);
                    ((DefaultTableModel)tblLocation.getModel()).fireTableDataChanged();
                    
                    int lastRow = tblLocation.getRowCount();
                    tblLocation.setRowSelectionInterval(lastRow-1,
                            lastRow-1);
                    txtArAddress.setText(address);
                    btnClearAddress.setEnabled( true );
                    btnDeleteLocation.setEnabled(true);
                    btnAddAddress.setEnabled( true );
                    
                }
            }catch(Exception ex){
                CoeusOptionPane.showInfoDialog(ex.getMessage());
            }
            
        }
    }
    // Added for Enable multicampus for default organization in protocols - End
}
