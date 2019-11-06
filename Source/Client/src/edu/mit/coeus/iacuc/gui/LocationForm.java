/*
 * @(#)LocationForm.java  1.0  19/9/02
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */
package edu.mit.coeus.iacuc.gui;

import javax.swing.table.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

import edu.mit.coeus.gui.*;
import edu.mit.coeus.iacuc.bean.LocationBean;
import edu.mit.coeus.utils.*;
import edu.mit.coeus.rolodexmaint.gui.*;
import edu.mit.coeus.search.gui.CoeusSearch;


/** This class is pluggable location component which gives the information
 * about the Location (Protocol) like Address, Place. This component provides
 * methods to <CODE>add/modify/delete</CODE> addresses.
 * @author ravikanth Created on October 24, 2002, 6:15 PM
 */
public class LocationForm extends javax.swing.JComponent {

     /** used in searching for rolodex */
    private static final String ROLODEX_SEARCH = "rolodexSearch";

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
        LocationBean. */
    private Vector locations;
    
    /** HashMap which holds the location details as key value pairs with 
        location as key and LocationBean as value to the corresponding location */
    private HashMap locationAddress;
    
    /** reference to CoeusAppletMDIForm */
    private CoeusAppletMDIForm mdiForm;
    
    /** Collection object which holds the names to be displayed in Location 
        table */
    private Vector columnNames = new Vector();
    
    /** identifier which specifies the database table to used for searching the
        address like, Rolodex, Person etc. */
    private String searchIdentifier = "";
    
    /** integer value which holds the previous user selection in location table */
    private int previousSelRow = -1;
    
    private CoeusMessageResources coeusMessageResources;

    /** Default Constructor used to create the <CODE>LocationForm</CODE>.
     */
    public LocationForm(){
    }


    /** Creates new form <CODE>LocationForm</CODE> with specified function type of the form,
     * Locations which already exists and parent window object for which
     * this component is attached.
     * @param functionType character which represents the mode in which the form is
     * opened. Ex: 'A' - Add Mode, 'M' - Modify Mode, 'D' - Display Mode
     * @param locations Collection of <CODE>LocationBean</CODE>s which already exists.
     * @param mdiForm reference to <CODE>CoeusAppletMDIForm</CODE>.
     */
    public LocationForm(char functionType, Vector locations,
    CoeusAppletMDIForm mdiForm) {
        this.mdiForm = mdiForm;
        this.functionType = functionType;
        this.locations = locations;
        initComponents();
        
        columnNames.addElement("");
        columnNames.addElement("");
        setTableColumnWidths();
        formatFields();
        coeusMessageResources = CoeusMessageResources.getInstance();
    }

    /*supporting method to reset or format the fields with default's before
      filling data to the component
     */
    private void formatFields(){
        boolean enabled = functionType == 'D' ? false : true ;
        txtArAddress.setEditable(false);
        txtArAddress.setFont(CoeusFontFactory.getNormalFont());
        btnAddAddress.setEnabled(enabled);
        btnAddLocation.setEnabled(enabled);
        btnClearAddress.setEnabled(enabled);
        if( this.tblLocation.getRowCount() > 0 ){
        btnDeleteLocation.setEnabled(enabled);
        }else{
        btnDeleteLocation.setEnabled(false);    
        }
        
         //Added by Amit 11/18/2003
        if(functionType == CoeusGuiConstants.DISPLAY_MODE){

            java.awt.Color bgListColor = (java.awt.Color)javax.swing.UIManager.getDefaults().get("Panel.background");
        
            tblLocation.setBackground(bgListColor);    
            tblLocation.setSelectionBackground(bgListColor );
            tblLocation.setSelectionForeground(java.awt.Color.BLACK);            
        }
        else{
            tblLocation.setBackground(java.awt.Color.white);            
            tblLocation.setSelectionBackground(java.awt.Color.white);
            tblLocation.setSelectionForeground(java.awt.Color.black);            
        }
        //end Amit         
        
    }

    /** This method is used to the set the previously selected location instances.
     * Usually this method is called during the process of syncronizing the data
     * after saving to the database.
     * @param loc Collection of <CODE>LocationBean</CODE> instances.
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
           are in LocationBean as value.  And also change the table contents to
           reflect the new values. */
        if( (locations != null) &&  (locations.size()>0) ){
            LocationBean locationBean = null;
            Vector tableRowData = null;
            Vector tableData = new Vector();
            locationAddress.clear();
            for(int loopIndex=0;loopIndex<locations.size();loopIndex++){

                locationBean = (LocationBean)locations.elementAt(loopIndex);
                if(loopIndex == 0){
                    if( (locationBean.getLocationId() == null )
                            || (locationBean.getLocationId().trim().length() ==
                                                                           0)){
                        btnClearAddress.setEnabled(false);
                    }
                }
                tableRowData = new Vector();
                /* to handle icon column */
                tableRowData.addElement("");
                tableRowData.addElement(locationBean.getLocation()== null ? ""
                        : locationBean.getLocation());
                tableData.addElement(tableRowData);
                locationAddress.put(locationBean.getLocation(),locationBean);
            }

            int prevSelRow = 0;
            if(tblLocation.getRowCount()>0)
            {
                prevSelRow = tblLocation.getSelectedRow();
            }
            ((DefaultTableModel)tblLocation.getModel()).setDataVector(tableData,
                    getColumnNames());
            ((DefaultTableModel)tblLocation.getModel()).fireTableDataChanged();
            setTableColumnWidths();
            if(tblLocation.getRowCount()>0)
            {
                
                if( prevSelRow != -1 ) {
                    tblLocation.setRowSelectionInterval(prevSelRow,prevSelRow);
                }else{
                    tblLocation.setRowSelectionInterval(0,0);
                }
            }
           //tblLocation.setRowSelectionInterval(0,0);
        }
    }

    /** Supporting method to get all the location details entered in this form.
     * This will be a collection of <CODE>LocationBean</CODE> instances.
     * @return Vector Collection of <CODE>LocationBean</CODE> instances.
     */
    public Vector getLocations(){
        locations = new Vector();
        int rowCount = tblLocation.getRowCount();
        if( rowCount > 0 ) {
            int selRow = tblLocation.getSelectedRow();
            if(selRow != -1){
                /* stop the editing by taking the value from editor component of
                   table and setting it to the corresponding cell, if user is 
                   still editing any location. */
                if(tblLocation.isEditing()){
                    String value = ((javax.swing.text.JTextComponent)
                                        tblLocation.getEditorComponent()).
                                        getText();
                        if( (value != null)){
                            tblLocation.setValueAt(value,selRow,1);
                        }
                        tblLocation.getCellEditor().cancelCellEditing();
                        LocationBean locBean = new LocationBean();
                        if(locationAddress == null){
                            locationAddress = new HashMap();
                        }
                        /* change the corresponding location details with the 
                           new location name */
                        String loc = (String)tblLocation.getValueAt(selRow,1);
                        if( ( loc != null ) && ( loc.trim().length() > 0 ) ){
                            locBean.setLocation( loc );
                            locationAddress.put(loc,locBean);
                        }
                }
            }
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
                        String locationName = (String)iterator.next();
                        if(locationName != null && locationName.trim().
                            length()>0){
                            String tableLocName = "";
                            for(int tableRow=0; tableRow < rowCount;tableRow++){
                                tableLocName = (String)tblLocation.getValueAt(
                                tableRow,1);
                                if(tableLocName != null){
                                    if(locationName.equals(tableLocName)){
                                        found = true;
                                        break;
                                    }
                                }
                            }
                            if(found){
                                LocationBean locBean
                                    = (LocationBean)locationAddress.get(
                                    locationName);
                                locations.addElement(locBean);
                            }
                        }
                    }
                }
            }else{
                /* if there is only one location entered and if there is no 
                   address specified for that location then locationAddress 
                   hashtable will be empty so , create a new hashtable and 
                   create the LocationBean with only location name and send it
                   for saving. */
                locationAddress = new HashMap();
                LocationBean locBean = new LocationBean();
                String loc = (String)tblLocation.getValueAt(0,1);
                if( ( loc != null ) && ( loc.trim().length() > 0 ) ){
                    locBean.setLocation( loc );
                    locationAddress.put(loc,locBean);
                    locations.addElement(locBean);
                }
            }
            
            //comeIntoMyCode
            
//            if(tblLocation.getRowCount()>0)
//            {
//                int row=tblLocation.getSelectedRow();
//                tblLocation.setRowSelectionInterval(row,row);
//            }
            
        }
        return locations;
    }

    //supporting method to get all the column Names used for Location table.
    private Vector getColumnNames(){
        return columnNames;
    }


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
        column.setPreferredWidth(30);

        column = tblLocation.getColumnModel().getColumn(1);
        column.setResizable(false);
        column.setPreferredWidth(150);
        column.setCellEditor(new CustomEditor());
        
        /* setting blank header to the locations table */
        tblLocation.setTableHeader(null);
        tblLocation.setShowVerticalLines(false);
        tblLocation.setShowHorizontalLines(false);
        //tblLocation.setSelectionBackground(Color.white);
        //tblLocation.setSelectionForeground(Color.black);
        tblLocation.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

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
        btnAddLocation.setText("Add Loc");
        btnAddLocation.setMnemonic('L');
        btnAddLocation.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                btnAddLocationActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(15, 0, 0, 5);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHWEST;
        add(btnAddLocation, gridBagConstraints);

        btnDeleteLocation.setFont(CoeusFontFactory.getLabelFont());
        btnDeleteLocation.setText("Delete Loc");
        btnDeleteLocation.setMnemonic('e');
        btnDeleteLocation.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                btnDeleteLocationActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 5);
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
        gridBagConstraints.insets = new java.awt.Insets(20, 0, 0, 5);
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
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 5, 5);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        add(btnClearAddress, gridBagConstraints);

        scrPnLocation.setBorder(new TitledBorder(new EtchedBorder(),
                "Location", TitledBorder.LEFT, TitledBorder.TOP,
                CoeusFontFactory.getLabelFont()));
        scrPnLocation.setPreferredSize(new java.awt.Dimension(250, 150));
        scrPnLocation.setMinimumSize(new java.awt.Dimension(200, 150));
        tblLocation.setModel(new DefaultTableModel(
            new Object [][] {
                {null, null}
            },
            new String [] {
                "", ""
            }
        ) {
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                /* allowing editing for Location column in add and modify modes*/
                if(functionType == 'D'){
                    return false;
                }else if(columnIndex == 1){
                    return true;
                }
                return false;
            }
        });
        tblLocation.setFont(CoeusFontFactory.getNormalFont());
        tblLocation.setRowHeight(22);
        tblLocation.setOpaque(false);
        if( tblLocation.getRowCount() > 0 ){
            int rowNum = tblLocation.getSelectedRow();
            if(rowNum > 0){
                tblLocation.setRowSelectionInterval(rowNum,rowNum);
            }
        }
        tblLocation.getSelectionModel().addListSelectionListener(
                                                  new TableSelectionListener());

        scrPnLocation.setViewportView(tblLocation);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;

        gridBagConstraints.insets = new java.awt.Insets(5, 10, 5, 10);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        add(scrPnLocation, gridBagConstraints);

        scrPnAddress.setBorder(new TitledBorder(new EtchedBorder(), "Address",
                TitledBorder.LEFT, TitledBorder.TOP,
                CoeusFontFactory.getLabelFont()));
        scrPnAddress.setPreferredSize(new Dimension(300, 150));
        scrPnAddress.setMinimumSize(new Dimension(200, 150));
        scrPnAddress.setViewportView(txtArAddress);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridheight = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        //gridBagConstraints.ipadx = 50;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        add(scrPnAddress, gridBagConstraints);
        
        // Added by chandra 08/02/2004 - start
        Component[] comp = {btnAddLocation, btnDeleteLocation,btnAddAddress,btnClearAddress};
        ScreenFocusTraversalPolicy traversal = new ScreenFocusTraversalPolicy(comp);
        setFocusTraversalPolicy(traversal);
        setFocusCycleRoot(true);
        // Added by chandra 08/02/2004 - end

    }


    //supporting method to handle the action events for the button clear event.
    private void btnClearAddressActionPerformed(ActionEvent evt) {
        // Add your handling code here:
        int rowCount = tblLocation.getRowCount();
        if( rowCount > 0 ) {
            int selRow = tblLocation.getSelectedRow();
            if(selRow != -1){
                String key =  (String)tblLocation.getValueAt(selRow,1);
                LocationBean locBean = new LocationBean();
                if(locationAddress != null && locationAddress.containsKey(key)){
                    locBean = (LocationBean)locationAddress.get(key);
                    if( ( locBean.getLocationId() != null )
                            && ( locBean.getAddress() !=null ) ){
                        int option = CoeusOptionPane.showQuestionDialog(
                                    "Are you sure you want to delete this address ?",
                                    CoeusOptionPane.OPTION_YES_NO,
                                    CoeusOptionPane.DEFAULT_YES);
                        if(option == JOptionPane.YES_OPTION){
                            /* if the user wishes to clear the address for a 
                               specified location clear the values and set the 
                               bean in the hashtable */
                            locBean.setLocationId(null);
                            locBean.setAddress(null);
                            locationAddress.put(key,locBean);
                            txtArAddress.setText("");
                            btnClearAddress.setEnabled(false);
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
            String locName = null;
            if(selRow != -1){
                /* if any location column is editing stop the editing */
                if(tblLocation.isEditing()){
                    String value = ((javax.swing.text.JTextComponent)
                                        tblLocation.getEditorComponent()).
                                        getText();
                        tblLocation.setValueAt(value,selRow,1);
                        tblLocation.getCellEditor().cancelCellEditing();
                }

                locName = (String)tblLocation.getValueAt(selRow,1);
            }
            if(locName != null && locName.trim().length() > 0){
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

                            LocationBean locBean = new LocationBean();
                            String key=(String)tblLocation.getValueAt(selRow,1);
                            if(locationAddress == null){
                                locationAddress = new HashMap();
                            }else if(locationAddress.containsKey(key)){
                                /* if any entry already available with the 
                                   specified location, then replace its address
                                   in locationBean and update in hashtable */
                                locBean = (LocationBean)locationAddress.get(key);
                                if(locBean.getLocation() != null){
                                    if(!locBean.getLocation().equals(key)){
                                        locationAddress.remove(
                                                         locBean.getLocation());
                                    }
                                }
                            }
                            locBean.setLocation((String)tblLocation.getValueAt(
                                selRow,1));
                            locBean.setAddress(address);
                            locBean.setLocationId(locationId);
                            locationAddress.put(key,locBean);
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

            String key = (String)tblLocation.getValueAt(row,1);
            LocationBean locBean = new LocationBean();
            if(locationAddress != null && locationAddress.containsKey(key)){
                locBean = (LocationBean)locationAddress.get(key);
                String locationName = locBean.getLocation();
                int option = JOptionPane.NO_OPTION;
                if(locationName != null && locationName.trim().length()>0){
                    option = CoeusOptionPane.showQuestionDialog(
                        "Are you sure you want to remove "+locationName +
                        " ?",
                        CoeusOptionPane.OPTION_YES_NO,
                        CoeusOptionPane.DEFAULT_YES);
                    if(option == JOptionPane.YES_OPTION){
                        /* if the user wishes to delete any location then remove
                           the location from the table as well as from the 
                           hashtable */
                        locationAddress.remove(key);
                        ((CustomEditor)tblLocation.getCellEditor(row,1)
                            ).cancelCellEditing();
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
                            btnDeleteLocation.setEnabled(false);            
                        }
                    }
                }else{
                    // if saved to hastable without location don't ask for
                    //confirmation just delete the row
                    locationAddress.remove(key);
                    ((CustomEditor)tblLocation.getCellEditor(row,1)
                            ).cancelCellEditing();
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
                            tblLocation.setRowSelectionInterval(newRowCount - 1,
                                newRowCount - 1);
                        }
                    }else{
                        txtArAddress.setText("");
                    }
                }
            }else{
                // if not saved to hastable don't ask for confirmation just
                // delete the row
                ((CustomEditor)tblLocation.getCellEditor(row,1)
                            ).cancelCellEditing();
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
                        tblLocation.setRowSelectionInterval(newRowCount - 1,
                            newRowCount - 1);
                    }
                }else{
                    txtArAddress.setText("");
                }
            }
        }

    }


    //supporting method to handle action events for button add location event.
    private void btnAddLocationActionPerformed(ActionEvent evt) {
        // Add your handling code here:
        int tableRowCount = tblLocation.getRowCount();
        boolean newRowToAdd = true;
        if(tableRowCount >0){
            int selRow = tblLocation.getSelectedRow();
            if(tblLocation.isEditing()){
                String value = ((javax.swing.text.JTextComponent)
                                    tblLocation.getEditorComponent()).getText();
                    tblLocation.setValueAt(value,selRow,1);
                    tblLocation.getCellEditor().cancelCellEditing();
            }
            /* check any empty row is available. If so, edit the location column
               in that row else create a new row for entering location details. */
            for(int row=0;row < tableRowCount ; row++){
                String location = (String)tblLocation.getValueAt(row,1);
                if((location == null) || (location.trim().length() == 0)){
                    tblLocation.setRowSelectionInterval(row,row);
                    tblLocation.editCellAt(row,1);
                    newRowToAdd = false;
                    break;
                }
                if(row >0){
                    /* check for duplicate location names and inform the user
                       about it */
                    String oldLocation = null;
                    for(int newRow = 0; newRow < row ; newRow++){
                        oldLocation = (String)tblLocation.getValueAt(newRow,1);
                        if(oldLocation.equals(location)){
                            CoeusOptionPane.showInfoDialog( 
                                coeusMessageResources.parseMessageKey(
                                    "location_exceptionCode.0505") );
                            tblLocation.setRowSelectionInterval(row,row);
                            tblLocation.editCellAt(row,1);
                            newRowToAdd = false;
                            break;
                        }
                    }

                }
            }
            if(newRowToAdd){
                newRowToAdd = false;
                if(locationAddress == null){
                    locationAddress = new HashMap();
                }
                LocationBean locBean = new LocationBean();
                String key = (String)tblLocation.getValueAt(tableRowCount-1,1);
                if(locationAddress.containsKey(key)){
                    locBean = (LocationBean)locationAddress.get(key);
                }
                locBean.setLocation((String)tblLocation.getValueAt(
                        tableRowCount-1,1));
                locationAddress.put(key,locBean);

                Vector tableRow = new Vector();
                tableRow.addElement("");
                tableRow.addElement("");
                ((DefaultTableModel)tblLocation.getModel()
                        ).addRow(tableRow);
                ((DefaultTableModel)tblLocation.getModel()
                        ).fireTableDataChanged();
                
                int lastRow = tblLocation.getRowCount();
                tblLocation.setRowSelectionInterval(lastRow-1,
                lastRow-1);
                tblLocation.editCellAt(lastRow-1,1);
                // Added by chandra 08/02/04 start
                tblLocation.getEditorComponent().requestFocusInWindow();
                // Added by chandra 08/02/04 end
            }

        }else{
            /* if there are no rows available in the location table create a
               new row */
            Vector tableRow = new Vector();
            tableRow.addElement("");
            tableRow.addElement("");
            ((DefaultTableModel)tblLocation.getModel()).addRow(tableRow);
            ((DefaultTableModel)tblLocation.getModel()).fireTableDataChanged();
            tblLocation.setRowSelectionInterval(0,0);
            tblLocation.editCellAt(0,1);
            // Added by chandra 08/02/04 start
            tblLocation.getEditorComponent().requestFocusInWindow();
            // Added by chandra 08/02/04 end
        }
        txtArAddress.setText("");
        btnDeleteLocation.setEnabled(true);  
        
        //Added by Amit 11/20/2003 for IRB_DEF_Gen_6
        //tblLocation.requestFocusInWindow();
        //tblLocation.editCellAt(tableRowCount,1);
        
        //End Amit        
    }


    /**
     * This class is used to set the editor for the text field which will be
     * used to table (secific row,column) to provide editing document feature.
     */
    class CustomEditor extends DefaultCellEditor implements TableCellEditor {

        private JTextField txtEditor = new JTextField();
        private int row = 0;
        private String locName = "";
        private boolean temporary;
        /**
         * This is a default consturctor creates an instance of Custom Editor
         */
        CustomEditor(){
            super(new JTextField());
            txtEditor.setFont(CoeusFontFactory.getNormalFont());
            txtEditor.addFocusListener(new FocusAdapter(){
                public void focusLost(FocusEvent fe){
                        if ( ! temporary ) {
                            stopCellEditing();
                        }
                }
            });
            setMouseAdapter();
        }

        //supporiting method to handle mouse listener actions
        private void setMouseAdapter(){
            txtEditor.addMouseListener(new MouseAdapter(){
                public void mouseClicked(MouseEvent e) {
                    if(e.getClickCount() == 2){
                        /* if any address is associated with this location then
                           show the Rolodex details corresponding to that
                           address when user double clicks with the mouse on 
                           that location column */
                        if(locationAddress != null){
                            if(locationAddress.containsKey(locName)){
                                LocationBean locBean = (LocationBean)
                                        locationAddress.get(locName);
                                if(locBean.getLocationId() != null && 
                                        locBean.getLocationId().trim().length() > 0
                                        && !locBean.getLocationId().trim().equals("0") ){
                                    if(getSearchFrom().equals(ROLODEX_SEARCH)){
                                        RolodexMaintenanceDetailForm detForm
                                             = new RolodexMaintenanceDetailForm(
                                                        'V',
                                                       locBean.getLocationId());
                                        detForm.showForm(mdiForm,
                                            "Rolodex Details",
                                            true);
                                    }
                                }
                            }
                        }
                    }
                }
            });

        }


        /**
         * An overridden method to set the editor component in a cell.
         * @param table - the JTable that is asking the editor to edit; can be
         * null
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
        public Component getTableCellEditorComponent(JTable table,Object value,
                boolean isSelected,int selRow,int column){
            this.locName = (String)value;
            this.row = selRow;
            txtEditor.setDocument(new LimitedPlainDocument(60));
            txtEditor.setText((String)value);
            return txtEditor;
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
            try {
                /* when the user stops editing the location name then create a
                   location bean with the location details */
                String editingValue = (String)getCellEditorValue();
                txtEditor.setText(editingValue);
                LocationBean locBean = new LocationBean();
                if(locationAddress == null){
                    locationAddress = new HashMap();
                }
                String loc = editingValue;
                /* if the newly entered location already exist in the hashtable
                   update that entry with newly entered details. */
                if( ( loc != null ) && ( loc.trim().length() > 0 ) ){
                    loc = loc.trim();
                    temporary = true;
                    
                    for(int newRow = 0; newRow < tblLocation.getRowCount(); newRow++){
                        String oldLocation = (String)tblLocation.getValueAt(newRow,1);
                        if(oldLocation.equals( loc ) && (newRow != row )){
                            
                            CoeusOptionPane.showInfoDialog(
                                  coeusMessageResources.parseMessageKey(
                                    "location_exceptionCode.0505"));
                            cancelCellEditing();
                            tblLocation.setRowSelectionInterval(row,row);
                            temporary = false;
                            return false;
                        }
                    }
                    
                    if(locationAddress.containsKey(loc)){
                        locBean = (LocationBean)locationAddress.get(loc);
                    }else if( locationAddress.containsKey( locName ) ) {
                        locBean = (LocationBean)locationAddress.get( locName );
                    }

                    locBean.setLocation( loc );
                    locationAddress.put(loc,locBean);
                }
            }
            catch(Exception exception) {
                return false;
            }
            return super.stopCellEditing();
        }

        /** Returns the value contained in the editor.
         * @return Object the value contained in the editor
         */
        public Object getCellEditorValue() {
            return txtEditor.getText();
        }


        /**
         * Invoked when an cell has been selected or deselected by the user.
         * The code written for this method performs the operations that need
         * to occur when an cell is selected (or deselected).
         * @param ItemEvent event which specifies the delegated object
         */
        public void itemStateChanged(ItemEvent e) {
            super.fireEditingStopped();
        }
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
                if(previousSelRow != -1 && rowCount > previousSelRow){
                    if(tblLocation.isEditing()){
                        /* if the previously selected row is still editing when
                           the table row selection change event occured then
                           stop the editing and update to hashtable*/
                        String value = ((javax.swing.text.JTextComponent)
                            tblLocation.getEditorComponent()).getText();
                        tblLocation.setValueAt(value,previousSelRow,1);
                        tblLocation.getCellEditor().cancelCellEditing();
                    }

                    String key = (String)tblLocation.getValueAt(previousSelRow,
                                                                            1);
                    /* check whether any location name entered for the previously
                       selected row */
                    if(key != null && key.trim().length()>0){
                        LocationBean locBean = new LocationBean();
                        if(locationAddress == null){
                            locationAddress = new HashMap();
                        }
                        /* if the location name of the previously selected row
                           already exists in the hashtable update that entry with
                           new details otherwise create a new entry for that 
                           location */
                        if(locationAddress.containsKey(key)){
                            locBean = (LocationBean)locationAddress.get(key);
                        }
                        locBean.setLocation((String)tblLocation.getValueAt(
                                previousSelRow,1));
                        locationAddress.put(key,locBean);
                    }
                }
                /* check whether any address associated with the newly selected
                   location. If present, update the address textarea with the 
                   corresponding address. Otherwise clear the contents of the
                   address textarea */
                String key = (String)tblLocation.getValueAt(selRow,1);
                LocationBean locBean = new LocationBean();
                if( (locationAddress != null)
                        && (locationAddress.containsKey(key))){
                    locBean = (LocationBean)locationAddress.get(key);
                }
                if( (locBean.getAddress() == null )
                        || (locBean.getAddress().trim().length() == 0) ){
                    btnClearAddress.setEnabled(false);
                    txtArAddress.setText("");
                }else{
                    if(functionType != 'D'){
                        btnClearAddress.setEnabled(true);
                    }
                    txtArAddress.setText(locBean.getAddress());
                }
                previousSelRow = selRow;
            }
        }
    }
}
