/*
 * @(#)ProposalOrganizationAdminForm.java 1.0 03/05/03
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */
/*

/**
 *
 * @author  Sagin
 */
package edu.mit.coeus.propdev.gui;

import edu.mit.coeus.organization.bean.OrganizationMaintenanceFormBean;

import java.awt.event.*;
import java.util.*;
import java.awt.*;

import javax.swing.*;

import edu.mit.coeus.bean.InvestigatorBean;
import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.gui.*;
import edu.mit.coeus.propdev.bean.*;
import edu.mit.coeus.irb.gui.LocationForm;
import edu.mit.coeus.irb.bean.*;
import edu.mit.coeus.utils.*;
import edu.mit.coeus.exception.*;
import edu.mit.coeus.organization.bean.OrganizationAddressFormBean;
import edu.mit.coeus.rolodexmaint.bean.RolodexDetailsBean;
import edu.mit.coeus.search.gui.CoeusSearch;
//import edu.mit.coeus.search.gui.SearchResultWindow;
import edu.mit.coeus.organization.gui.DetailForm;
// JM 8-19-2011 added for help gidgets
import edu.vanderbilt.coeus.gui.CoeusHelpGidget;
// END
// JM 1-26-2016 added for billing agreement
import edu.vanderbilt.coeus.utils.CustomFunctions;



// JM END
//import java.beans.PropertyChangeListener;
//import java.beans.PropertyChangeEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumn;


public class ProposalOrganizationAdminForm extends javax.swing.JComponent 
        implements TypeConstants, ListSelectionListener,ActionListener {
  
    /* holds the reference of parent object*/
    private CoeusAppletMDIForm mdiForm=null;  
    
    /* This is used to hold vector of data beans*/
    private ProposalDevelopmentFormBean proposalDevelopmentFormBean;
     
    //holds the performing organization info bean
    private OrganizationAddressFormBean performingOrganizationInfo;
     
    //holds the Rolodex bean which contains the details corresponding to the 
    //contact address ID in the OrganizationAddressFormBean
    private RolodexDetailsBean rolodexDetailsBean;
    
    /* This is used to hold the mode D for Display, I for Add, U for Modify */
    private char functionType;
    
    /* This is used to notify whether the Save is required */
    private boolean saveRequired = false;
    
    //holds the list of Proposal Locations
    private Vector proposalLocations = null;
       
    //holds CoeusMessageResources instance used for reading message Properties.
    private CoeusMessageResources coeusMessageResources;
    
    private final String ROLODEX_SEARCH = "rolodexSearch";
    
    private String contactAddress;
    //Commented for case 2406 - Organization and Location - start
//    private int contactAddressId;
    
    //holds the Organization ID corresponding to the Organization Name
//    private String organizationID;
    
    /** Holds the Performing Organization ID corresponding to the Performing 
      Organization Name 
    */
//    private String performingOrganizationID;
    //Commented for case 2406 - Organization and Location - end
    boolean locModified = false;
    //Added for case 2406 - Organization and Location - start
    private CoeusVector cvSites;
    private final int ROW_HEIGHT = 24;
            
    private final int PROPOSAL_ORGANIZATION = 1;
    private final int PERFORMING_ORGANIZATION = 2;
    private final int OTHER_ORGANIZATION = 3;
    // JM 8-22-2011 need performance site specified for help options
    private final int PERFORMANCE_SITE = 4;    
    // END
    
    // JM 1-21-2016 new Billing Agreement type; 5-20-2016 new Outgoing Sub to VU/VUMC type
    private final int BILLING_AGREEMENT = 5;
    private final int OUTGOING_SUB_TO_VANDY = 6;
    // JM END
    
    private final int DISTRICT_HAND_ICON_COL = 0;
    private final int DISTRICT_NAME_COL = 1;
    
    private final int DISTRICT_HAND_ICON_WIDTH = 25;
    
    private final int LOCATION_HAND_ICON_COL = 0;
    private final int LOCATION_TYPE_COL = 1;
    private final int LOCATION_NAME_COL = 2;
    // JM 8-22-2011 added for help gidget
    private final int GIDGET_COL = 3;
    // END
    
    private final int LOCATION_HAND_ICON_WIDTH = 25;
    private final int LOCATION_TYPE_COL_WIDTH = 10;
    private final int LOCATION_NAME_COL_WIDTH = 100;
    // JM 8-22-2011 add for help gidget
    private final int GIDGET_COL_WIDTH = 30;   
    private String helpCode;
    // END
    
    // JM 1-26-2016 org id for billing agreement
    private String baOrgId;
    private String baOrgName;
    // JM END
    
    private LocationTableModel locationTableModel;
    private DistrictTableModel districtTableModel;
    
    private Vector locationTypes;
    private HashMap hmLocationTypes = new HashMap();
    
    private int selectedLocationRow;
    private CoeusVector cvDeletedCongDistricts = new CoeusVector();
    private CoeusVector cvDeletedSites = new CoeusVector();
    private LocationEditor locationEditor;
    private DistrictEditor districtEditor;
    private LocationRenderer locationRenderer;
    private java.awt.Color bgPanelColor = (java.awt.Color)javax.swing.UIManager.getDefaults().get("Panel.background");
    //Added for case 2406 - Organization and Location - end
    
    /** Creates new form ProposalOrganizationAdminForm */
    public ProposalOrganizationAdminForm() {
        initComponents();
        
    }
    
//     public static void main( String[] arg ){
//        javax.swing.JFrame frm = new javax.swing.JFrame("Proposal Development");
//        frm.getContentPane().add( new ProposalOrganizationAdminForm() );
//        frm.pack();
//        frm.show();
//        
//    }
    
    /** Creates new form <CODE>ProposalOrganizationAdminForm</CODE>
     *
     * @param functionType this will open the different mode like Display
     * @param proposalDevelopmentFormBean ProposalDevelopmentFormBean
     * 'D' specifies that the form is in Display Mode
     */
    public ProposalOrganizationAdminForm(char functionType, 
                ProposalDevelopmentFormBean proposalDevelopmentFormBean) {
        
        /* This is used to hold the mode D for Display, I for Add, U for Modify */
        this.functionType = functionType;
        
        /* Used to hold the table data as beans*/
        this.proposalDevelopmentFormBean = proposalDevelopmentFormBean;
    }
    
    /** This method is used to initialize the form components,set the form
     * data and set the enabled status for all components depending on the
     * <CODE>functionType</CODE> specified while opening the form.
     *
     * @param mdiForm reference to the parent component <CODE>CoeusAppletMDIForm</CODE>
     * @return JComponent reference to the <CODE>ProposalOrganizationAdminForm</CODE> component
     * after initializing and setting the data.
     */
    public JComponent showOrganizationAdminForm(CoeusAppletMDIForm mdiForm){
        this.mdiForm = mdiForm;
        initComponents();
        postInitComponents();
        setFormData();
        //Commented for case 2406 - Organization and Location - start
        //initLocations();
        //formatFields();
        //Commented for case 2406 - Organization and Location - end
        
        coeusMessageResources = CoeusMessageResources.getInstance();
        return this;
    }
    /**
     * Sets the column, model and other properties for the table.
     */
    public void setTableProperties(){
        tblLocations.getTableHeader().setFont(CoeusFontFactory.getLabelFont());
        tblLocations.getTableHeader().setPreferredSize(new Dimension(0,22));
        
        tblDistrict.getTableHeader().setFont(CoeusFontFactory.getLabelFont());
        tblLocations.getTableHeader().setPreferredSize(new Dimension(0,22));
        
        TableColumn tableColumn = tblLocations.getColumnModel().getColumn(LOCATION_HAND_ICON_COL);
        tableColumn.setCellRenderer(new IconRenderer());
        tableColumn.setHeaderRenderer(new EmptyHeaderRenderer());
        tableColumn.setPreferredWidth(LOCATION_HAND_ICON_WIDTH);
        tableColumn.setMaxWidth(LOCATION_HAND_ICON_WIDTH);
        tableColumn.setMinWidth(LOCATION_HAND_ICON_WIDTH);
        
        tableColumn = tblLocations.getColumnModel().getColumn(LOCATION_TYPE_COL);
        tableColumn.setPreferredWidth(LOCATION_TYPE_COL_WIDTH);
        tableColumn.setMinWidth(LOCATION_TYPE_COL_WIDTH);
        tableColumn.setCellEditor(locationEditor);
        tableColumn.setCellRenderer(locationRenderer);
        
        tableColumn = tblLocations.getColumnModel().getColumn(LOCATION_NAME_COL);
        tableColumn.setPreferredWidth(LOCATION_NAME_COL_WIDTH);
        tableColumn.setMinWidth(LOCATION_NAME_COL_WIDTH);
        tableColumn.setCellEditor(locationEditor);
        tableColumn.setCellRenderer(locationRenderer);
        
        // JM 8-22-2011 help gidget icon
        tableColumn = tblLocations.getColumnModel().getColumn(GIDGET_COL);
        IconRenderer gidgetIconRenderer = new IconRenderer();
        gidgetIconRenderer.setSelectedRowIcon(new CoeusHelpGidget().gidgetSoft());
        tableColumn.setCellRenderer(gidgetIconRenderer);
        tableColumn.setHeaderRenderer(new EmptyHeaderRenderer());
        tableColumn.setPreferredWidth(GIDGET_COL_WIDTH);
        tableColumn.setMaxWidth(GIDGET_COL_WIDTH);
        tableColumn.setMinWidth(GIDGET_COL_WIDTH);
        // END
        
        districtTableModel = new DistrictTableModel();
        tblDistrict.setModel(districtTableModel);
        
        tableColumn = tblDistrict.getColumnModel().getColumn(DISTRICT_HAND_ICON_COL);
        tableColumn.setCellRenderer(new IconRenderer());
        tableColumn.setHeaderRenderer(new EmptyHeaderRenderer());
        tableColumn.setPreferredWidth(DISTRICT_HAND_ICON_WIDTH);
        tableColumn.setMaxWidth(DISTRICT_HAND_ICON_WIDTH);
        tableColumn.setMinWidth(DISTRICT_HAND_ICON_WIDTH);
        
        tableColumn = tblDistrict.getColumnModel().getColumn(DISTRICT_NAME_COL);
        tableColumn.setCellEditor(districtEditor);
    }
    
    /**
     * Sets the gui component properties
     */
    public void postInitComponents(){
    	
    	// JM 1-26-2016 get system paramter
    	CustomFunctions custom = new CustomFunctions();
    	String[] params = (String[]) custom.getParameterValues("BILLING_AGREEMENT_ORG_ID");
    	baOrgId = (String) params[0];
    	// JM END
        
         if(functionType == CoeusGuiConstants.DISPLAY_MODE){
            tblLocations.setBackground(bgPanelColor);
            tblLocations.setSelectionBackground(bgPanelColor );
            tblLocations.setSelectionForeground(Color.black);
            
            tblDistrict.setBackground(bgPanelColor);
            tblDistrict.setSelectionBackground(bgPanelColor );
            tblDistrict.setSelectionForeground(Color.black);
        }else{
            tblLocations.setBackground(Color.white);
            tblLocations.setSelectionBackground(Color.white);
            tblLocations.setSelectionForeground(Color.black);
            
            tblDistrict.setBackground(Color.white);
            tblDistrict.setSelectionBackground(Color.white);
            tblDistrict.setSelectionForeground(Color.black);
        }
        
        JTableHeader header = tblLocations.getTableHeader();
        header.setReorderingAllowed(false);
         
        header = tblDistrict.getTableHeader();
        header.setReorderingAllowed(false);
        
        tblLocations.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblLocations.setRowHeight(ROW_HEIGHT);
        tblLocations.setOpaque(false);
        
        tblDistrict.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblDistrict.setRowHeight(ROW_HEIGHT);
        tblLocations.getSelectionModel().addListSelectionListener(this);
        tblDistrict.setOpaque(false);
        
        btnAddLoc.addActionListener(this);
        btnDeleteLoc.addActionListener(this);
        btnFindLoc.addActionListener(this);
        btnAddDistrict.addActionListener(this);
        btnDeleteDistrict.addActionListener(this);
        btnFindAddress.addActionListener(this);
        btnClearAddress.addActionListener(this);
        
        tblLocations.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e){
                if(e.getClickCount() ==2){
                    //invoke Organization display
                    try {
                        int selectedLocationRow = tblLocations.getSelectedRow();
                        helpCode = ""; // JM
                        if(selectedLocationRow !=-1){
                            ProposalSiteBean proposalSiteBean = (ProposalSiteBean)cvSites.get(selectedLocationRow);
                            //Show the organization detail pop up only for organizations of type
                            // PROPOSAL_ORGANIZATION and PERFORMING_ORGANIZATION
                            int locationTypeCode = proposalSiteBean.getLocationTypeCode();
                            
                        	// JM 8-19-2011 popup for gidget
                        	if (tblLocations.getSelectedColumn() == GIDGET_COL) {
                        		if (locationTypeCode == PROPOSAL_ORGANIZATION) {
                        			helpCode = "propOrg_helpCode.1001";
                        		}
                        		else if (locationTypeCode == PERFORMING_ORGANIZATION) {
                        			helpCode = "performOrg_helpCode.1001";                        			
                        		}
                        		else {
                        			helpCode = "otherOrg_helpCode.1001";                        			
                        		}
                    	        CoeusHelpGidget gidget = new CoeusHelpGidget(helpCode);
                        		gidget.popupFrame();
                        	}
                            // END
                        	else {
                            if(locationTypeCode == PROPOSAL_ORGANIZATION ||
                                    locationTypeCode == PERFORMING_ORGANIZATION ||
                                    locationTypeCode == OTHER_ORGANIZATION){
                                    if(proposalSiteBean.getOrganizationId()!=null){
                                         DetailForm detailForm = new DetailForm(DISPLAY_MODE, proposalSiteBean.getOrganizationId());
                                         if (detailForm != null) {    
                                            detailForm.showDialogForm(mdiForm);
                                         }
                                    }
                            }
                        }
                        }
                    } catch (Exception ex) {
                        log("Exception while opening Organization Details Window" 
                                                                + ex.getMessage());
                    }
                }
            }
        });
        
        Component[] components = { btnAddLoc, btnDeleteLoc, btnFindLoc,
        btnFindAddress, btnClearAddress, btnAddDistrict, btnDeleteDistrict}; 
        ScreenFocusTraversalPolicy screenFocusTraversalPolicy = new ScreenFocusTraversalPolicy(components);
        setFocusTraversalPolicy(screenFocusTraversalPolicy);
        setFocusCycleRoot(true);
    }
    
    //Commented for case 2406 - Organization and Locations - start
    /**
     * This method is used to set the enabled status for the components
     * depending on the functionType specified.
     */
//    private void formatFields(){  
//        boolean enabled = functionType != DISPLAY_MODE ? true:false ;
//        btnSearchPerformingOrg.setEnabled(enabled);
//        btnSearchOrg.setEnabled(enabled);
//        
//        //Bug Fix :1040 -- Start Ajay
//        txtOrganization.setEditable(false);
//        txtPerformingOrganization.setEditable(false);
//        //txtPerformingOrganization.setEnabled(false);
//        if(txtPerformingOrganization.isEnabled() && (functionType != CoeusGuiConstants.DISPLAY_MODE)){
//            txtPerformingOrganization.setBackground(Color.white);
//            txtPerformingOrganization.setOpaque(true);
//        }
//        //Bug Fix :1040 -- End Ajay
//        
//         if(txtOrganization.isEnabled() && (functionType != CoeusGuiConstants.DISPLAY_MODE)){
//            txtOrganization.setBackground(Color.white);
//            txtOrganization.setForeground(Color.black);
//            txtOrganization.setOpaque(true);
//        }
//    }
    
    /**
     * This method is used to set the form data specified in
     * <CODE> proposalDevelopmentFormBean, organizationAddressFormBean,
     * and rolodexDetailsBean </CODE> 
     */
//    public void setFormData(){
//            if (organizationAddressFormBean != null ) {
//                organizationID = proposalDevelopmentFormBean.getOrganizationId();
//                txtOrganization.setText(
//                    organizationAddressFormBean.getOrganizationName());
//                lblConDistrictDetail.setText(
//                    organizationAddressFormBean.getCongressionalDistrict());
//            }
//            
//            txtArAddressDetails.setText(formatAddress(this.rolodexDetailsBean));
//           
//            if (performingOrganizationInfo != null ) {
//                performingOrganizationID = proposalDevelopmentFormBean.
//                                                getPerformingOrganizationId();
//                txtPerformingOrganization.setText(
//                    performingOrganizationInfo.getOrganizationName());
//            }
//            locationForm.setLocations(getLocationsForProposal());
//         
//                                    
//        //}
//    }
    //Commented for case 2406 - Organization and Locations - end
    
    /**
     * Sets the data to the form
     */
    public void setFormData(){
        locationEditor = new LocationEditor();
        districtEditor = new DistrictEditor();
        locationRenderer = new LocationRenderer();
        cvSites = proposalDevelopmentFormBean.getSites();
        addProposalPerformSites();
        
        if(cvSites != null){
            locationTableModel = new LocationTableModel();
            tblLocations.setModel(locationTableModel);
            setTableProperties();
            if(tblLocations.getRowCount() > 0){
                tblLocations.getSelectionModel().setSelectionInterval(0,0);
            }
        }
    }
    public void addProposalPerformSites(){
        boolean proposalOrganizationPresent = false;
        boolean performingOrganizationPresent = false;
        ProposalSiteBean proposalSiteBean;
        for(int i=0; i<cvSites.size(); i++){
            proposalSiteBean = (ProposalSiteBean)cvSites.get(i);
            if(proposalSiteBean.getLocationTypeCode() == PROPOSAL_ORGANIZATION){
                proposalOrganizationPresent = true;
            }
             if(proposalSiteBean.getLocationTypeCode() == PERFORMING_ORGANIZATION){
                performingOrganizationPresent = true;
            }
            if(proposalOrganizationPresent && performingOrganizationPresent){
                break;
            }
        }
        if(!proposalOrganizationPresent){
            proposalSiteBean = new ProposalSiteBean();
            proposalSiteBean.setLocationTypeCode(PROPOSAL_ORGANIZATION);
            proposalSiteBean.setLocationName("");
            cvSites.add(0, proposalSiteBean);
        }
        if(!performingOrganizationPresent){
            proposalSiteBean = new ProposalSiteBean();
            proposalSiteBean.setLocationTypeCode(PERFORMING_ORGANIZATION);
            proposalSiteBean.setLocationName("");
            cvSites.add(1, proposalSiteBean);
        }
    }
    /** This method is used to set the <CODE>ProposalDevelopmentFormBean</CODE> with all the details
     * of the Proposal.
     * @param infoBean <CODE>ProposalDevelopmentFormBean</CODE> which consists of all the details of
     * Proposal for Proposal, Organization & Mailing Info tabpages.
     * This method will be called from Proposal Detail Form when using the 
     * Navigations menu items (Next & Previous)
     */
    public void setValues(ProposalDevelopmentFormBean infoBean){
        this.proposalDevelopmentFormBean = infoBean;
        //Commented for case 2406 Organization and Location - start
//        setOrgAddressInfo(infoBean.getOrganizationAddressFormBean());
//        setRolodexDetails(infoBean.getRolodexDetailsBean());
//        setPerformingOrganizationInfo(infoBean.getPerOrganizationAddressFormBean());
        //Commented for case 2406 Organization and Location - end
        setFormData();
        
        //formatFields();
    }
    
    // JM 1-14-2015 get address with DUNS; 8-3-2015 fix for performance site not showing correct address;
    // 	2-3-2016 added billing agreement to locations that display DUNS;
    //  5-20-2016 added outgoing sub to Vandy to location that display DUNS;
    /**
     * Overrides the method below to allow the organization info to be passed as well so we can get the DUNS
     * @param ProposalSiteBean proposalSiteBean
     * @param RolodexDetailsBean rolodexDetailsBean
     * @param boolean showContact
     * @return String formatted address
     */
    public String formatAddress(ProposalSiteBean proposalSiteBean,RolodexDetailsBean rolodexDetailsBean,boolean showContact) {
    	String contactAddress = formatAddress(rolodexDetailsBean,showContact);

    	if (proposalSiteBean.getLocationTypeCode() == OTHER_ORGANIZATION || 
    			proposalSiteBean.getLocationTypeCode() == BILLING_AGREEMENT || 
    			proposalSiteBean.getLocationTypeCode() == OUTGOING_SUB_TO_VANDY) {
    		
	        OrganizationMaintenanceFormBean bean = new OrganizationMaintenanceFormBean();
			try {
				bean = (OrganizationMaintenanceFormBean) getOrganizationDetails(proposalSiteBean.getOrganizationId(),true);
			} catch (Exception e) {
				System.out.println("Could not get DUNS number for organization");
			}
			if (bean.getDunsNumber() != "" && bean.getDunsNumber() != null) {
				contactAddress = contactAddress + "\n\nDUNS:  \t" + bean.getDunsNumber();
			}
    	}
		return edu.mit.coeus.utils.Utils.convertNull(contactAddress); 
    }
    
    /**
     * This method connect to the server and gets the organization details for the specified id, overrides MIT method
     * @param orgId organization id
     * @return Vector contain the organization details.
     */
    private OrganizationMaintenanceFormBean getOrganizationDetails(String orgId,boolean isCustom) throws Exception {
    	OrganizationMaintenanceFormBean bean = new OrganizationMaintenanceFormBean();
        String connectTo = CoeusGuiConstants.CONNECTION_URL + "/orgMntServlet";
        RequesterBean request = new RequesterBean();
        request.setFunctionType('J');
        request.setId(orgId);
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
            bean = (OrganizationMaintenanceFormBean) response.getDataObject();
        } else {
        	throw new Exception(response.getMessage());
        }
        return bean;
    }
    // JM END
    
    /**
     * This method is used to format the address for organization
     * @param rolodexDetailsBean
     * @return a <CODE>String</CODE> representing the formatted address.
     */
    public String formatAddress(RolodexDetailsBean rolodexDetailsBean, boolean showContact){
        if (rolodexDetailsBean != null ) {
            contactAddress = "";
            if(showContact){
                String firstName = edu.mit.coeus.utils.Utils.convertNull(
                                        rolodexDetailsBean.getFirstName());
                String middleName = edu.mit.coeus.utils.Utils.convertNull(
                                        rolodexDetailsBean.getMiddleName());
                String lastName = edu.mit.coeus.utils.Utils.convertNull(
                                        rolodexDetailsBean.getLastName());
                String prefix = edu.mit.coeus.utils.Utils.convertNull(
                                        rolodexDetailsBean.getPrefix());
                String suffix = edu.mit.coeus.utils.Utils.convertNull(
                                        rolodexDetailsBean.getSuffix());
                if (lastName.length() > 0) {
                    contactAddress = (lastName + " "+ suffix +", "+ 
                        prefix + " "+ firstName + " "+ middleName).trim() + "\n";
                } else {
                    contactAddress = edu.mit.coeus.utils.Utils.convertNull(
                                rolodexDetailsBean.getOrganization()) + "\n";
                }
            }
            String temp = rolodexDetailsBean.getOrganization();
            if (temp != null && temp.length() > 0) {
                if (!temp.equals(contactAddress)) {
                    contactAddress = contactAddress + temp + "\n";
                } else {
                    contactAddress = temp + "\n";
                }
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
                //Modified for case 2406 case 2406 - Organization and Locations - start
                if(states!=null){
                    for(int loopIndex = 0; loopIndex < states.size(); loopIndex++) {
                        stateBean = (ComboBoxBean) states.elementAt(loopIndex);
                        if (stateBean.getCode().equals(temp)) {
                            temp = stateBean.getDescription();
                        }
                    }
                }
                //Modified for case 2406 case 2406 - Organization and Locations - end
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
                //Modified for case 2406 case 2406 - Organization and Locations - start
                if(countries != null){
                    ComboBoxBean countryBean;
                    for(int loopIndex = 0 ; loopIndex < countries.size() ; loopIndex++ ){
                        countryBean = (ComboBoxBean) countries.elementAt(loopIndex);
                        if (countryBean.getCode().equals(temp)) {
                            temp = countryBean.getDescription();
                        }
                    }
                }
                //Modified for case 2406 case 2406 - Organization and Locations - end
            }

            if (temp != null && temp.length() > 0) {
                contactAddress = contactAddress + temp + " ";
            }

        } 
        return edu.mit.coeus.utils.Utils.convertNull(contactAddress); 
        
    } 
    
//    /**
//     *For displaying the Organization Address (With/without contact) 
//     */
//    private String getOrganizationAddress(RolodexDetailsBean rolodexDetailsBean, String organizationId, boolean showContact) {
//        contactAddress = "";
//        if(organizationId != null && organizationId.trim().length() > 0 && !organizationId.equals(null)) {
//            if (rolodexDetailsBean != null ) {
//               if(showContact){
//                    String firstName = edu.mit.coeus.utils.Utils.convertNull(
//                                            rolodexDetailsBean.getFirstName());
//                    String middleName = edu.mit.coeus.utils.Utils.convertNull(
//                                            rolodexDetailsBean.getMiddleName());
//                    String lastName = edu.mit.coeus.utils.Utils.convertNull(
//                                            rolodexDetailsBean.getLastName());
//                    String prefix = edu.mit.coeus.utils.Utils.convertNull(
//                                            rolodexDetailsBean.getPrefix());
//                    String suffix = edu.mit.coeus.utils.Utils.convertNull(
//                                            rolodexDetailsBean.getSuffix());
//                    if (lastName.length() > 0) {
//                        contactAddress = (lastName + " "+ suffix +", "+ 
//                            prefix + " "+ firstName + " "+ middleName).trim() + "\n";
//                    } else {
//                        contactAddress = edu.mit.coeus.utils.Utils.convertNull(
//                                    rolodexDetailsBean.getOrganization()) + "\n";
//                    }
//                }
//            }
//            try {
//                Vector dataObjects = getOrganizationDetails(organizationId);
//                OrganizationMaintenanceFormBean orgMaintFormData = (OrganizationMaintenanceFormBean) dataObjects.elementAt(0);
//                contactAddress = contactAddress + orgMaintFormData.getAddress();
//                return edu.mit.coeus.utils.Utils.convertNull(contactAddress);
//            } catch (Exception ex) {
//
//            }
//        }
//        return CoeusGuiConstants.EMPTY_STRING;
//    }
//    
//     /**
//     * This method connect to the server and gets the organization details for the specified id.
//     * @param orgId organization id
//     * @return Vector contain the organization details.
//     */
//    private Vector getOrganizationDetails(String orgId) throws Exception {
//        Vector dataObjects = null;
//        String connectTo = CoeusGuiConstants.CONNECTION_URL + "/orgMntServlet";
//        // connect to the database and get the formData for the given organization id
//        RequesterBean request = new RequesterBean();
//        request.setFunctionType('D');
//        request.setId(orgId);
//        AppletServletCommunicator comm = new AppletServletCommunicator(connectTo, request);
//        comm.send();        
//        ResponderBean response = comm.getResponse();
//        if (response == null) {
//            response = new ResponderBean();
//            response.setResponseStatus(false);
//            response.setMessage(coeusMessageResources.parseMessageKey(
//                    "server_exceptionCode.1000"));
//        }
//        if (response.isSuccessfulResponse()) {
//            dataObjects = response.getDataObjects();
//        } else {
//            if(response.isLocked()){
//                //this.locked = true;
//                return null;
//            }else{
//                throw new Exception(response.getMessage());
//            }
//        }
//        return dataObjects;
//    }
//    
    //Commented for case 2406 - Organization and Locations - start
    /** This method is used to set the OrganizationAddressFormBean
     * @param organizationAddressFormBean
     */
//    public void setOrgAddressInfo(OrganizationAddressFormBean 
//                                        organizationAddressFormBean){
//        this.organizationAddressFormBean = organizationAddressFormBean;
//    }
    
    /** This method is used to set the RolodexDetailsBean
     * @param rolodexDetailsBean
     */
//    public void setRolodexDetails(RolodexDetailsBean 
//                                        rolodexDetailsBean){
//        this.rolodexDetailsBean = rolodexDetailsBean;
//    }
    
    /** This method is used to set the Performing OrganizationAddressFormBean
     * @param organizationAddressFormBean
     */
//    public void setPerformingOrganizationInfo(OrganizationAddressFormBean 
//                                        organizationAddressFormBean){
//        this.performingOrganizationInfo = organizationAddressFormBean;
//    }
      //Commented for case 2406 - Organization and Location - end
    /** This method is used to find out whether modifications done to the data
     * have been saved or not.
     *
     * @return true if data is not saved after modifications, else false.
     */
    public boolean isSaveRequired(){
        return this.saveRequired;
    }
    
    //Added by Amit 11/21/2003
    /** This method use to implement focus on first editable component in this page.
     */
    public void setDefaultFocusForComponent(){
        if(!( functionType == CoeusGuiConstants.DISPLAY_MODE )) {
            //Modified for case 2406 - Organization and Location - start
//                btnSearchOrg.requestFocusInWindow();     
            btnAddLoc.requestFocusInWindow();
            //Modified for case 2406 - Organization and Location - end
        }
    }    
    //End Amit  
        
    
     /** This method is used to set whether modifications are to be saved or not.
     *
     * @param saveRequired boolean true if data is to be saved after modifications,
     * else false.
     */
    public void setSaveRequired(boolean saveRequired){
        this.saveRequired = saveRequired;
    }
    
    /** Method to get the functionType
     * @return a <CODE>Char</CODE> representation of functionType.
     */
    public char getFunctionType(){
        return this.functionType;
    }
     /** Method to set the functionType
     * @param fType is functionType to be set like 'D', 'I', 'M'
     */
    public void setFunctionType(char fType){
        this.functionType = fType;
    }
    
    /** This method is used for validations.
     * 
     * @return true if the validation succeed, false otherwise.
     * @throws Exception a exception to be thrown in the client side.
     */
    public boolean validateData() throws Exception{
        if(cvSites != null){
            stopAllCellEditing();
            ProposalSiteBean proposalSiteBean = null;
            for(int locationRow =0; locationRow<cvSites.size(); locationRow++){
                proposalSiteBean = (ProposalSiteBean)cvSites.get(locationRow);
                if(proposalSiteBean.getAcType()!=null){
                    if(!(proposalSiteBean.getLocationTypeCode()>0)){
                        setRequestFocusInThread(tblLocations,locationRow, LOCATION_NAME_COL);
                        errorMessage(coeusMessageResources.parseMessageKey("proposal_sites_exceptionCode.1002"));
                    }
                    if(proposalSiteBean.getLocationName() == null 
                            || proposalSiteBean.getLocationName().trim().length() == 0){
                        tblLocations.setRowSelectionInterval(locationRow,locationRow);
                        errorMessage(coeusMessageResources.parseMessageKey("proposal_sites_exceptionCode.1003"));
                    }
                }
                if(proposalSiteBean.getCongDistricts() != null 
                        && proposalSiteBean.getCongDistricts().size() > 0){
                    ProposalCongDistrictBean proposalCongDistrictBean = null;
                    CoeusVector cvCongDistricts = proposalSiteBean.getCongDistricts();
                    for(int districtRow=0; districtRow < cvCongDistricts.size(); districtRow++){
                        proposalCongDistrictBean = (ProposalCongDistrictBean)cvCongDistricts.get(districtRow);
                        if(proposalCongDistrictBean.getAcType()!=null){
                            if(proposalCongDistrictBean.getCongDistrict() == null
                                    || proposalCongDistrictBean.getCongDistrict().trim().length()==0){
                                tblLocations.setRowSelectionInterval(locationRow,locationRow);
                                
                                if(tblDistrict.getRowCount() > districtRow){
                                    setRequestFocusInThread(tblDistrict,districtRow, DISTRICT_NAME_COL);
                                }
                                errorMessage(coeusMessageResources.parseMessageKey("proposal_sites_exceptionCode.1004"));
                            }
                        }
                    }
                }
            }
        }
        return true;
    }
    //Commented for case 2406 - Organization and Locaion - start
    //    public boolean validateData() throws Exception{
//        Vector newLocations = locationForm.getLocations();
//        if (newLocations == null || newLocations.size() == 0) {
//                errorMessage(coeusMessageResources.parseMessageKey(
//                                "protoMntFrm_exceptionCode.1079"));
//                return false;
//        }
//        return true;
//    }
    //Commented for case 2406 - Organization and Locaion - end
    
    /** This method is used to show the alert messages to the user.
      * @param mesg a string message to the user.
      * @throws Exception a exception thrown in the client side.
      */
    public static void errorMessage(String mesg) throws Exception {
        CoeusUIException coeusUIException = new CoeusUIException(mesg,CoeusUIException.WARNING_MESSAGE);
        //Modified for case 2406 - Organizations and Locations - start
        coeusUIException.setTabIndex(1);
        coeusUIException.setTabIndex(ProposalDetailForm.ORGANIZATION_TAB);
        //Modified for case 2406 - Organizations and Locations - end
        throw coeusUIException;
    }
    public void stopAllCellEditing(){
        if(locationEditor != null){
            locationEditor.stopCellEditing();
        }
        if(districtEditor != null){
            districtEditor.stopCellEditing();
        }
    }
    
    public CoeusVector getFormData(){
        
        stopAllCellEditing();
        CoeusVector cvSitesData = new CoeusVector();
        if(cvSites != null){
            ProposalSiteBean proposalSiteBean;
            ProposalSiteBean newProposalSiteBean = null;
            ProposalCongDistrictBean proposalCongDistrictBean;
            boolean found = false;
            
            for(int i=0; i<cvSites.size(); i++){
                proposalSiteBean = (ProposalSiteBean)cvSites.get(i);
                try {
                    newProposalSiteBean = (ProposalSiteBean)ObjectCloner.deepCopy(proposalSiteBean);
                    cvSitesData.add(newProposalSiteBean);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            
            for(int i=0; i<cvSitesData.size(); i++){
                proposalSiteBean = (ProposalSiteBean)cvSitesData.get(i);
                if(cvDeletedCongDistricts !=null){
                    for(int j = 0; j<cvDeletedCongDistricts.size(); j++){
                        proposalCongDistrictBean = (ProposalCongDistrictBean)cvDeletedCongDistricts.get(j);
                         if(proposalCongDistrictBean.getSiteNumber() == proposalSiteBean.getSiteNumber()){
                            if(proposalSiteBean.getCongDistricts()==null){
                                proposalSiteBean.setCongDistricts(new CoeusVector());
                            }
                            proposalSiteBean.getCongDistricts().add(proposalCongDistrictBean);
                         }
                    }
                }
            }
            
            cvSitesData.addAll(cvDeletedSites);
            //Sort the data with deleted sites and deleted congressional districts
            //coming on the top
           cvSitesData.sort("acType");
           for(int i=0; i<cvSitesData.size(); i++){
                proposalSiteBean = (ProposalSiteBean)cvSitesData.get(i);
                if(proposalSiteBean.getCongDistricts() != null){
                    proposalSiteBean.getCongDistricts().sort("acType");
                }
           }
        }
        return cvSitesData;
    }
    
    //Commented for case 2406 - Organization and Location - start
//    public void getFormData(){
//        
//        boolean found = false;
//            proposalDevelopmentFormBean.setOrganizationId(organizationID);
//            proposalDevelopmentFormBean.setPerformingOrganizationId(
//                                                    performingOrganizationID);
//            Vector newLocations = locationForm.getLocations();
//            if (newLocations == null || newLocations.size() == 0) {
//                    setSaveRequired(true);
//                    return;
//            }
//            if((newLocations != null) && (newLocations.size() > 0)){
//                LocationBean locBean = null;
//                found = false;
//                //proposalLocations = proposalDevelopmentFormBean.getLocationLists();
//                for(int newLocIndex = 0; newLocIndex < newLocations.size();
//                    newLocIndex++){
//                    int foundIndex = -1;
//                    found = false;
//                    locBean = (LocationBean)newLocations.elementAt(newLocIndex);
//                    newLocListBean = new ProposalLocationFormBean();
//                    newLocListBean.setProposalLocation(locBean.getLocation());
//                    if( (locBean.getLocationId() != null)
//                            && (locBean.getLocationId().trim().length() > 0)){
//                        newLocListBean.setRolodexId(
//                                Integer.parseInt(locBean.getLocationId()));
//                    }
//
//                    if(proposalLocations != null && proposalLocations.size() > 0){
//                        for(int oldLocIndex = 0;
//                                oldLocIndex < proposalLocations.size();
//                                oldLocIndex++){
//                            oldLocListBean = (ProposalLocationFormBean)
//                                proposalLocations.elementAt(oldLocIndex);
//                            oldLocListBean.addPropertyChangeListener(
//                                 new PropertyChangeListener(){
//                                    public void propertyChange(
//                                            PropertyChangeEvent pce){
//                                        /* if address has been changed to a
//                                           location setting the corresponding
//                                           rolodex id will fire property change
//                                           event. If any changes have been done
//                                           to exisiting location new record will
//                                           be inserted for protocol details with
//                                           new sequence no. So set acType for
//                                           both beans to MODIFY_RECORD */
//                                        locModified = true;
//                                        if ( functionType == MODIFY_MODE && oldLocListBean.getAcType() == null ) {
//                                            oldLocListBean.setAcType("U");
//                                        }
//                                        proposalDevelopmentFormBean.setAcType("U");
//                                        saveRequired = true;
//                                    }
//                            });
//                            if(newLocListBean.getProposalLocation().equals(
//                                    oldLocListBean.getProposalLocation())){
//                                found = true;
//                                foundIndex = oldLocIndex;
//                                break;
//                            }
//                        }
//                    }else{
//                        proposalLocations = new Vector();
//                    }
//                    if(!found){
//                        /* if location is new set AcType to INSERT_RECORD */
//                        locModified = true;
//                        newLocListBean.setAcType("I");
//                        if(functionType == 'M'){
//                            proposalDevelopmentFormBean.setAcType("U");
//                        }
//                        proposalLocations.addElement(newLocListBean);
//                        saveRequired = true;
//                    }else{
//                        /* if present set the values to the bean. if modified,
//                           bean will fire property change event */
//                        if(oldLocListBean != null){
//                            if (oldLocListBean.getRolodexId() != 
//                                            newLocListBean.getRolodexId()) {
//                                setSaveRequired(true);
//                                oldLocListBean.setRolodexId(newLocListBean.getRolodexId());
//                                if ( functionType == MODIFY_MODE && oldLocListBean.getAcType() == null) {
//                                    oldLocListBean.setAcType("U");
//                                }
//                            }
//                            if(foundIndex != -1){
//                                proposalLocations.setElementAt(oldLocListBean,
//                                    foundIndex);
//                            }
//                        }
//                    }
//                }
//                if(proposalLocations != null && proposalLocations.size() > 0){
//                    for(int oldLocIndex = 0; oldLocIndex <
//                            proposalLocations.size(); oldLocIndex++){
//                        found = false;
//                        oldLocListBean = (ProposalLocationFormBean)
//                        proposalLocations.elementAt(oldLocIndex);
//                        for(int newLocIndex = 0; newLocIndex <
//                                        newLocations.size(); newLocIndex++){
//                            locBean = (LocationBean)newLocations.elementAt(
//                                                                   newLocIndex);
//                            if(oldLocListBean.getProposalLocation().equals(
//                                    locBean.getLocation())){
//                                found = true;
//                                break;
//                            }
//                        }
//                        if(!found){
//                            /* if existing location has been deleted set acType
//                               to DELETE_RECORD */
//                            
//                            saveRequired = true;
//                            locModified = true;
//                            if ( functionType == MODIFY_MODE && !INSERT_RECORD.equals(oldLocListBean.getAcType())) {
//                                oldLocListBean.setAcType("D");
//
//                                proposalDevelopmentFormBean.setAcType("U");
//                                proposalLocations.setElementAt(oldLocListBean,
//                                    oldLocIndex);
//                            } else {
//                                proposalLocations.removeElementAt( oldLocIndex );
//                            }
//                        }
//                    }
//                }
//            }else{
//                saveRequired = true;
//            }
//            proposalDevelopmentFormBean.setLocationLists(proposalLocations);
//            
//    } 
    /** This method is used to get the location details from the <CODE>ProposalLocationFormBean</CODE>
     *
     * @return Collection of <CODE>LocationBean</CODE> with all the location details
     * for this Proposal.
     */
//    public Vector getLocationsForProposal(){
//        Vector locations = new Vector();
//        //In Add mode set the location with Performing Organization Name as default value
//        if (functionType == ADD_MODE) {
//            LocationBean locBean = new LocationBean();
//            locBean.setLocation(performingOrganizationInfo.getOrganizationName());
//            locations.addElement(locBean);
//            return locations;
//        }
//        
//        if(proposalDevelopmentFormBean != null){
////            proposalLocations = proposalDevelopmentFormBean.getLocationLists();
//            if(proposalLocations != null){
//                LocationBean locBean = null;
//                ProposalLocationFormBean proposalLocationFormBean = null;
//                String fullAddress = "";
//                String address = null;
//                StringTokenizer stAddress = null;
//                for(int loopIndex = 0 ; loopIndex < proposalLocations.size() ;
//                    loopIndex++ ){
//                    locBean = new LocationBean();
//                    proposalLocationFormBean = ( ProposalLocationFormBean )
//                            proposalLocations.elementAt( loopIndex );
//
//                    locBean.setLocationId(""+proposalLocationFormBean.getRolodexId());
//                    locBean.setLocation(proposalLocationFormBean.getProposalLocation());
//                    address = proposalLocationFormBean.getAddress();
//                    if((address != null) && (address.trim().length() > 0)){
//                        stAddress = new StringTokenizer(address,"$#");
//                        while(stAddress.hasMoreTokens()){
//                            String addr = stAddress.nextToken();
//                            fullAddress += addr;
//                            fullAddress += (addr.length() > 0)? "\n" : "";
//                        }
//                        locBean.setAddress(fullAddress);
//                        fullAddress = "";
//                    }
//                    locations.addElement(locBean);
//                }
//            }
//        }
//        return locations;
//    }
//Commented for case 2406 - Organization and Location - end

    /**
     * Gets the Rolodex address which is used to display below the Organization
     * Name.
     *
     * @param contactAddressID(rolodex ID)
     * @return boolean
     *  true - organization id is already available
     *  false - organization is is not available
     */
    public RolodexDetailsBean getOrgAddress(String contactAddressID) {
//        boolean duplicate = false;
        String connectTo = CoeusGuiConstants.CONNECTION_URL + "/coeusFunctionsServlet";
        RolodexDetailsBean rolodexDetailsBean = null;
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
        try{
            if (response.hasResponse()) {
                rolodexDetailsBean = (RolodexDetailsBean)response.getDataObject();
            } 
        }catch(CoeusException e){
            log(e.getMessage());
        }
        return rolodexDetailsBean;
    }
    
    /**
     * display alert message
     *
     * @param mesg the message to be displayed
     */
    public void log(String mesg) {

        CoeusOptionPane.showErrorDialog(mesg);
    }

    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblLocations = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        txtArAddress = new javax.swing.JTextArea();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblDistrict = new javax.swing.JTable();
        btnAddLoc = new edu.mit.coeus.utils.CoeusButton();
        btnDeleteLoc = new edu.mit.coeus.utils.CoeusButton();
        btnFindLoc = new edu.mit.coeus.utils.CoeusButton();
        btnAddDistrict = new edu.mit.coeus.utils.CoeusButton();
        btnDeleteDistrict = new edu.mit.coeus.utils.CoeusButton();
        btnFindAddress = new edu.mit.coeus.utils.CoeusButton();
        btnClearAddress = new edu.mit.coeus.utils.CoeusButton();

        setLayout(new java.awt.GridBagLayout());

        jPanel1.setLayout(new java.awt.GridBagLayout());

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Organization/Location", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, CoeusFontFactory.getLabelFont()));
        jPanel1.setMaximumSize(new java.awt.Dimension(600, 200));
        jPanel1.setMinimumSize(new java.awt.Dimension(600, 200));
        jPanel1.setPreferredSize(new java.awt.Dimension(600, 200));
        jScrollPane1.setMinimumSize(new java.awt.Dimension(580, 170));
        jScrollPane1.setPreferredSize(new java.awt.Dimension(580, 170));
        tblLocations.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        tblLocations.setShowHorizontalLines(false);
        tblLocations.setShowVerticalLines(false);
        jScrollPane1.setViewportView(tblLocations);

        jPanel1.add(jScrollPane1, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        add(jPanel1, gridBagConstraints);

        jPanel2.setLayout(new java.awt.GridBagLayout());

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Address/Contact", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, CoeusFontFactory.getLabelFont()));
        jPanel2.setMinimumSize(new java.awt.Dimension(600, 120));
        jPanel2.setPreferredSize(new java.awt.Dimension(600, 120));
        jScrollPane3.setBorder(null);
        jScrollPane3.setMinimumSize(new java.awt.Dimension(580, 90));
        jScrollPane3.setPreferredSize(new java.awt.Dimension(580, 90));
        txtArAddress.setColumns(20);
        txtArAddress.setEditable(false);
        txtArAddress.setFont(CoeusFontFactory.getNormalFont());
        txtArAddress.setLineWrap(true);
        txtArAddress.setRows(4);
        txtArAddress.setWrapStyleWord(true);
        txtArAddress.setOpaque(false);
        jScrollPane3.setViewportView(txtArAddress);

        jPanel2.add(jScrollPane3, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        add(jPanel2, gridBagConstraints);

        jPanel3.setLayout(new java.awt.GridBagLayout());

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Congressional District", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, CoeusFontFactory.getLabelFont()));
        jPanel3.setMinimumSize(new java.awt.Dimension(600, 150));
        jPanel3.setPreferredSize(new java.awt.Dimension(600, 150));
        jScrollPane2.setMinimumSize(new java.awt.Dimension(580, 120));
        jScrollPane2.setPreferredSize(new java.awt.Dimension(580, 120));
        tblDistrict.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        tblDistrict.setShowHorizontalLines(false);
        tblDistrict.setShowVerticalLines(false);
        jScrollPane2.setViewportView(tblDistrict);

        jPanel3.add(jScrollPane2, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        add(jPanel3, gridBagConstraints);

        btnAddLoc.setMnemonic('A');
        btnAddLoc.setText("Add");
        btnAddLoc.setMaximumSize(new java.awt.Dimension(110, 23));
        btnAddLoc.setMinimumSize(new java.awt.Dimension(110, 23));
        btnAddLoc.setPreferredSize(new java.awt.Dimension(110, 23));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 3, 0, 3);
        add(btnAddLoc, gridBagConstraints);

        btnDeleteLoc.setMnemonic('t');
        btnDeleteLoc.setText("Delete");
        btnDeleteLoc.setMaximumSize(new java.awt.Dimension(110, 23));
        btnDeleteLoc.setMinimumSize(new java.awt.Dimension(110, 23));
        btnDeleteLoc.setPreferredSize(new java.awt.Dimension(110, 23));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 0, 3);
        add(btnDeleteLoc, gridBagConstraints);

        btnFindLoc.setMnemonic('n');
        btnFindLoc.setText("Find");
        btnFindLoc.setMaximumSize(new java.awt.Dimension(110, 23));
        btnFindLoc.setMinimumSize(new java.awt.Dimension(110, 23));
        btnFindLoc.setPreferredSize(new java.awt.Dimension(110, 23));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 0, 3);
        add(btnFindLoc, gridBagConstraints);

        btnAddDistrict.setMnemonic('i');
        btnAddDistrict.setText("Add District");
        btnAddDistrict.setMaximumSize(new java.awt.Dimension(110, 23));
        btnAddDistrict.setMinimumSize(new java.awt.Dimension(110, 23));
        btnAddDistrict.setPreferredSize(new java.awt.Dimension(110, 23));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 3, 0, 3);
        add(btnAddDistrict, gridBagConstraints);

        btnDeleteDistrict.setMnemonic('s');
        btnDeleteDistrict.setText("Delete District");
        btnDeleteDistrict.setMaximumSize(new java.awt.Dimension(110, 23));
        btnDeleteDistrict.setMinimumSize(new java.awt.Dimension(110, 23));
        btnDeleteDistrict.setPreferredSize(new java.awt.Dimension(110, 23));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 0, 3);
        add(btnDeleteDistrict, gridBagConstraints);

        btnFindAddress.setMnemonic('r');
        btnFindAddress.setText("Find Address");
        btnFindAddress.setMaximumSize(new java.awt.Dimension(110, 23));
        btnFindAddress.setMinimumSize(new java.awt.Dimension(110, 23));
        btnFindAddress.setPreferredSize(new java.awt.Dimension(110, 23));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 3, 0, 3);
        add(btnFindAddress, gridBagConstraints);

        btnClearAddress.setMnemonic('l');
        btnClearAddress.setText("Delete Address");
        btnClearAddress.setMaximumSize(new java.awt.Dimension(110, 23));
        btnClearAddress.setMinimumSize(new java.awt.Dimension(110, 23));
        btnClearAddress.setPreferredSize(new java.awt.Dimension(110, 23));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 0, 3);
        add(btnClearAddress, gridBagConstraints);

    }// </editor-fold>//GEN-END:initComponents

    //Commented for case 2406 - Organization and Location - start
    /** This method is called from within the constructor to
     * initialize the Location Form.
     */
//    private void initLocations() {
//        locationForm = new LocationForm(functionType,null,mdiForm);
//        locationForm.setSearchFrom(ROLODEX_SEARCH);
//        pnlLocationContainer.add(locationForm);
 //       pnlLocationContainer.setPreferredSize(new Dimension(700,200));
//    }
      
    
    /** Getter for property locModified.
     * @return Value of property locModified.
     */
//    public boolean isLocationModified() {
//        return locModified;
//    }    
  
    /** Setter for property locModified.
     * @param locModified New value of property locModified.
     */
//    public void setLocationModified(boolean locModified) {
//        this.locModified = locModified;
//    }    
      //Commented for case 2406 - Organization and Location - end
    
    //Added for case 2406 - Organization and Location - start
    public void valueChanged(ListSelectionEvent e){
        if(e.getSource().equals(tblLocations.getSelectionModel())){
            selectedLocationRow = tblLocations.getSelectedRow();
            if(selectedLocationRow != -1){
                ProposalSiteBean proposalSiteBean = (ProposalSiteBean)cvSites.get(selectedLocationRow);
                enableComponents(proposalSiteBean.getLocationTypeCode());
                if(proposalSiteBean.getRolodexDetailsBean() != null){
                	// JM 1-14-2015 passing in organization info so we can get the DUNS, though we won't for this org type
                    if(proposalSiteBean.getLocationTypeCode() == PROPOSAL_ORGANIZATION){
                        txtArAddress.setText(formatAddress(proposalSiteBean.getRolodexDetailsBean(), true));
                    }else{                      
                    	// JM 1-14-2015 passing in organization ID so we can get the DUNS
                        txtArAddress.setText(formatAddress(proposalSiteBean,proposalSiteBean.getRolodexDetailsBean(), false));
                        // JM END
                    }
                }else{
                    txtArAddress.setText("");
                    btnClearAddress.setEnabled(false);
                }
                if(proposalSiteBean.getCongDistricts() != null){
                    districtTableModel.setDistrictData(proposalSiteBean.getCongDistricts());
                }else{
                    districtTableModel.setDistrictData(new CoeusVector());
                }
                districtTableModel.fireTableDataChanged();
                if(tblDistrict.getRowCount()>0){
                    setRequestFocusInThread(tblDistrict,0,1);
                    //tblDistrict.setRowSelectionInterval(0, 0);
                }
            }
        }
    }
    
    /**
     * Enable/Disable the buttons according the functionType and
     * locationTypeCode
     * 
     * @param locationTypeCode
     */
    public void enableComponents(int locationTypeCode){
        if(functionType == DISPLAY_MODE){
            btnAddLoc.setEnabled(false);
            btnDeleteLoc.setEnabled(false);
            btnFindLoc.setEnabled(false);
            btnFindAddress.setEnabled(false);
            btnClearAddress.setEnabled(false);
            btnAddDistrict.setEnabled(false);
            btnDeleteDistrict.setEnabled(false);
        }else if(locationTypeCode == PROPOSAL_ORGANIZATION){
            btnDeleteLoc.setEnabled(false);
//JM            btnFindLoc.setEnabled(false);            
            btnFindLoc.setEnabled(true); //JM 5-25-2011 set to true per 4.4.2
            btnFindAddress.setEnabled(false);
            btnClearAddress.setEnabled(false);
         }else if(locationTypeCode == PERFORMING_ORGANIZATION){
            btnDeleteLoc.setEnabled(false);
            btnFindLoc.setEnabled(true);
            btnFindAddress.setEnabled(false);
            btnClearAddress.setEnabled(false);
         // JM 1-25-2016 set buttons for billing agreement
         }else if(locationTypeCode == BILLING_AGREEMENT){
             btnDeleteLoc.setEnabled(true);
             btnFindLoc.setEnabled(false);
             btnFindAddress.setEnabled(false);
             btnClearAddress.setEnabled(false);
         }else if(locationTypeCode == OTHER_ORGANIZATION){
            btnDeleteLoc.setEnabled(true);
            btnFindLoc.setEnabled(true);
            btnFindAddress.setEnabled(false);
            btnClearAddress.setEnabled(false);
        }else{
            btnDeleteLoc.setEnabled(true);
            btnFindLoc.setEnabled(false);
            btnFindAddress.setEnabled(true);
            btnClearAddress.setEnabled(true);
        }
    }
    
    /**
     * This class is used as table model for the table tblLocation
     */
    public class LocationTableModel extends AbstractTableModel {
    	// JM 8-19-2011 added empty column for gidgets
        private String[] columnNames = {"","Type", "Location",""};
        private ProposalSiteBean proposalSiteBean;
        
        public Object getValueAt(int row, int col){
            proposalSiteBean = (ProposalSiteBean) cvSites.get(row);
            if(col== LOCATION_TYPE_COL){
                if(hmLocationTypes.containsKey(String.valueOf(proposalSiteBean.getLocationTypeCode()))){
                    return hmLocationTypes.get(String.valueOf(proposalSiteBean.getLocationTypeCode()));
                }else{
                    return "";
                }
            }else if(col == LOCATION_NAME_COL){
                return proposalSiteBean.getLocationName();
            }
            return "";
        }
       
        public int getColumnCount() {
            return columnNames.length;
        }

        public int getRowCount() {
            return cvSites.size();
        }
        
        public String getColumnName(int col){
            return columnNames[col];
        }
        
        public boolean isCellEditable(int row, int col){
        	// JM 8-19-2011 appended gidget col
            if (functionType == DISPLAY_MODE || col == LOCATION_HAND_ICON_COL || col == GIDGET_COL) {
                return false;
            }
            ProposalSiteBean proposalSiteBean = (ProposalSiteBean) cvSites.get(row);
            int locationTypeCode = proposalSiteBean.getLocationTypeCode();
            if (locationTypeCode == PROPOSAL_ORGANIZATION || locationTypeCode == PERFORMING_ORGANIZATION ){
                return false;
            }
            else if (col== LOCATION_NAME_COL && 
            		(locationTypeCode == OTHER_ORGANIZATION || locationTypeCode == BILLING_AGREEMENT)) {
                return false;
            }
            else {
                return true;
            }
        }
    }
    private void setRequestFocusInThread(final JTable table, final int selrow , final int selcol){
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                table.setRowSelectionInterval(selrow, selrow);
                table.setColumnSelectionInterval(selcol, selcol);
                table.scrollRectToVisible(
                table.getCellRect(selrow ,0, true));
                table.editCellAt(selrow, selcol);
                if(table.getEditorComponent() != null){
                    table.getEditorComponent().requestFocusInWindow();
                }
            }
        });
    }
    
    /**
     * This class is used as cell editor for the table tblDistrict
     */
    public class DistrictEditor extends AbstractCellEditor implements TableCellEditor{
        private JTextField txtDistrictName;
        private int selColumn;
        private int selRow;
        private boolean temporary;
        private int selectedParentRow;
        public DistrictEditor(){
            txtDistrictName = new JTextField();
            txtDistrictName.setFont(CoeusFontFactory.getNormalFont());
            txtDistrictName.setBorder(new javax.swing.border.EmptyBorder(0,0,0,0));
            txtDistrictName.setDocument(new LimitedPlainDocument(50));
            txtDistrictName.setPreferredSize(new Dimension(200,0));
            txtDistrictName.addFocusListener(new FocusAdapter() {
                public void focusGained(FocusEvent e){
                    temporary = false;
                    selectedParentRow = selectedLocationRow;
                }
                
                public void focusLost(FocusEvent e){
                    if(!e.isTemporary()){
                        if(!temporary){
                            stopCellEditing();
                        }
                    }
                }
            });
        }

        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            selColumn = column;
            selRow = row;
            if(column == DISTRICT_NAME_COL){
                if(value != null){
                    txtDistrictName.setText(value.toString());
                }else{
                    txtDistrictName.setText("");
                }
                return txtDistrictName;
            }
            return txtDistrictName;
        }

        public Object getCellEditorValue() {
            if(selColumn == DISTRICT_NAME_COL){
                return txtDistrictName.getText();
            }
            return "";
        }
        
        public boolean stopCellEditing(){
            if (selColumn == DISTRICT_NAME_COL){
                temporary = true;
                String congDistrict = (String)getCellEditorValue();
                if(congDistrict!=null){
                    if(selectedParentRow != -1 
                            && selectedParentRow < cvSites.size()){
                        ProposalSiteBean proposalSiteBean = (ProposalSiteBean)cvSites.get(selectedParentRow);
                        CoeusVector cvCongDistricts = proposalSiteBean.getCongDistricts();
                        if(cvCongDistricts != null && selRow < cvCongDistricts.size()){
                            ProposalCongDistrictBean proposalCongDistrictBean = 
                                    (ProposalCongDistrictBean)cvCongDistricts.get(selRow);
                            if(proposalCongDistrictBean != null){
                                if(validateCongDistrict(cvCongDistricts, congDistrict, selRow)){
                                    if(!congDistrict.equals(proposalCongDistrictBean.getCongDistrict())){
                                        proposalCongDistrictBean.setCongDistrict(congDistrict);
                                        if(proposalCongDistrictBean.getAcType() == null){
                                            proposalCongDistrictBean.setAcType(TypeConstants.UPDATE_RECORD);
                                        }
                                        saveRequired = true;
                                    }
                                }
                            }
                        }
                    }
                }
            }
            return super.stopCellEditing();
        }
            
        public boolean validateCongDistrict(CoeusVector cvCongDistricts, String districtName, int currentRow){
            if(cvCongDistricts != null && (districtName != null && !districtName.trim().equals(""))){
                ProposalCongDistrictBean proposalCongDistrictBean;
                for(int districtRow = 0; districtRow < cvCongDistricts.size(); districtRow++){
                    if(districtRow != currentRow){
                        proposalCongDistrictBean = (ProposalCongDistrictBean)cvCongDistricts.get(districtRow);
                        if(districtName.equals(proposalCongDistrictBean.getCongDistrict())){
                            log(coeusMessageResources.parseMessageKey("proposal_sites_exceptionCode.1005"));
                            return false;
                        }
                    }
                }
            }
            return true;
        }
    }
    public class LocationRenderer extends DefaultTableCellRenderer{
        private JLabel lblLocation;
        private ProposalSiteBean proposalSiteBean;
        
        public LocationRenderer(){
            lblLocation = new JLabel();
            lblLocation.setOpaque(true);
        }
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            if(row < cvSites.size()){
                proposalSiteBean = (ProposalSiteBean)(cvSites.get(row));
                if(proposalSiteBean!=null ){
                    int locationTypeCode = proposalSiteBean.getLocationTypeCode();
                    lblLocation.setText(value.toString());
                    if(proposalSiteBean.getLocationTypeCode() == 1 || proposalSiteBean.getLocationTypeCode()==2){
                        lblLocation.setBackground(bgPanelColor);
                        lblLocation.setForeground(Color.BLACK);
                    }else{
                        lblLocation.setBackground(Color.white);
                        lblLocation.setForeground(Color.BLACK);
                    }
                }
            }
            return lblLocation;
        }

    }
    /**
     * This class is used as table cell editor for the table tblLocations
     */
    public class LocationEditor extends AbstractCellEditor implements TableCellEditor{
        private CoeusComboBox cmbLocationType;
        private JTextField txtLocationName;
        private int descriptionLenth = 60;
        private boolean temporary;
        int selColumn;
        int selRow;
        
        public LocationEditor(){
            txtLocationName = new JTextField();   
            txtLocationName.setBorder(new javax.swing.border.EmptyBorder(0,0,0,0));
            txtLocationName.setFont(CoeusFontFactory.getNormalFont());
            txtLocationName.setDocument(new LimitedPlainDocument(descriptionLenth));
            cmbLocationType = new CoeusComboBox();
            setComboBoxProperties();
            txtLocationName.addFocusListener(new FocusAdapter() {
                public void focusGained(FocusEvent e){
                    temporary = false;
                }
                
                public void focusLost(FocusEvent fe){
                    if(!fe.isTemporary()){
                        if(!temporary){
                            stopCellEditing();
                        }
                    }
                }
            });
        }
        
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            selRow = row;
            selColumn = column;
           if(column == LOCATION_TYPE_COL){
                if( value != null && !((String)value).trim().equals("")){
                    ComboBoxBean locationType = new ComboBoxBean();
                    locationType.setDescription(value.toString());
                    cmbLocationType.setSelectedItem(locationType);
                }else{
                    cmbLocationType.setSelectedIndex(0);
                }
                return cmbLocationType;
           }else if(column == LOCATION_NAME_COL){
               if(value != null){
                    txtLocationName.setText(value.toString());
               }else{
                   txtLocationName.setText("");
               }
               return txtLocationName;
           }
           return cmbLocationType;
        }
        
        public boolean stopCellEditing(){
            if(selColumn == LOCATION_NAME_COL){
                temporary = true;
                String locationName = (String)getCellEditorValue();
                if(validateLocationName(locationName, selRow)){
                    if(selRow < cvSites.size()){
                        ProposalSiteBean proposalSiteBean = (ProposalSiteBean)cvSites.get(selRow);
                        if(proposalSiteBean!=null){
                            if(!locationName.equals(proposalSiteBean)){
                                if(proposalSiteBean.getAcType()==null){
                                    proposalSiteBean.setAcType(TypeConstants.UPDATE_RECORD);
                                }
                            }
                            proposalSiteBean.setLocationName(locationName);
//                            locationTableModel.fireTableCellUpdated(selRow,selColumn);
                            saveRequired = true;
                        }
                    }
                }
            }
            return super.stopCellEditing();
        }
        
        public boolean validateLocationName(String locationName, int currentRow){
            if(cvSites!=null && locationName != null && !locationName.equals("")){
                ProposalSiteBean proposalSiteBean = null;
                if(currentRow<tblLocations.getRowCount()){
                    proposalSiteBean = (ProposalSiteBean)cvSites.get(currentRow);
                    int locationTypeCode = proposalSiteBean.getLocationTypeCode();
                    if(proposalSiteBean.getLocationTypeCode()<1){
                        return true;
                    }
                    for(int locationRow = 0; locationRow < cvSites.size(); locationRow++ ){
                        if(locationRow != currentRow){
                            proposalSiteBean = (ProposalSiteBean)cvSites.get(locationRow);
                            if(proposalSiteBean.getLocationTypeCode() == locationTypeCode
                                    && locationName.equals(proposalSiteBean.getLocationName())){
                                log(coeusMessageResources.parseMessageKey("proposal_sites_exceptionCode.1006"));
                                return false;
                            }
                        }
                    }
                }
            }
            return true;
        }
        
        public Object getCellEditorValue() {
            if(selColumn == LOCATION_NAME_COL){
                return txtLocationName.getText();
            }
            return "";
        }
        
        /**
         * Populates the combobox with data and sets the listeners
         */
        public void setComboBoxProperties(){
            //Set data 
            if(locationTypes != null){
                Vector filteredLocationTypes = new Vector();
                ComboBoxBean comboBoxBean = null;
                comboBoxBean = new ComboBoxBean("","");
                filteredLocationTypes.add(comboBoxBean);
                for(int i=0; i<locationTypes.size(); i++){
                    comboBoxBean = (ComboBoxBean)locationTypes.get(i);
                    if(!comboBoxBean.getCode().equals(String.valueOf(PROPOSAL_ORGANIZATION)) 
                        && !comboBoxBean.getCode().equals(String.valueOf(PERFORMING_ORGANIZATION)) ){
                        filteredLocationTypes.add(comboBoxBean);
                    }
                }
                cmbLocationType.setModel(new DefaultComboBoxModel(filteredLocationTypes));
            }
            //Set the listener
            cmbLocationType.addItemListener(new ItemListener(){
                public void itemStateChanged(ItemEvent itemEvent){
                    if(itemEvent.getStateChange() == ItemEvent.DESELECTED){
                        return;
                    }else if(itemEvent.getStateChange() == ItemEvent.SELECTED){
                        ComboBoxBean comboBoxBean = (ComboBoxBean)cmbLocationType.getSelectedItem();
                        if(comboBoxBean != null){
                            int selectedRow = selRow;

                        	// JM 1-26-2016 if billing agreement, get org info; 
                            //	5-20-2016 if outgoing sub to Vandy get org info
                        	ProposalSiteBean proposalSiteBean;
                            if(selectedRow != -1) {
                            	
                                proposalSiteBean = (ProposalSiteBean) cvSites.get(selectedRow); // JM
                                if (comboBoxBean.getCode() != null && !comboBoxBean.getCode().equals("")){
                                	/* JM 5-20-2016 added outgoing sub to Vandy to types that get org automatically */
                                	if (comboBoxBean.getCode().equals(((Integer) BILLING_AGREEMENT).toString()) || 
                                			comboBoxBean.getCode().equals(((Integer) OUTGOING_SUB_TO_VANDY).toString())) {
	                            		proposalSiteBean = getBillingAgreementOrSub(Integer.parseInt(comboBoxBean.getCode()));
	                            		baOrgName = proposalSiteBean.getLocationName();
	                                }
                                	/* JM END */
                            		
                                    if (validateLocationType(comboBoxBean.getCode(), proposalSiteBean.getLocationName())){
                                        boolean locationChanged = false;
                                        if (Integer.parseInt(comboBoxBean.getCode()) != proposalSiteBean.getLocationTypeCode()) {
                                            proposalSiteBean.setLocationName("");
                                            proposalSiteBean.setOrganizationId("");
                                            proposalSiteBean.setRolodexDetailsBean(null);
                                            locationChanged = true;
                                        }
                                        if (locationChanged && proposalSiteBean.getCongDistricts() != null) {
                                            for(int i = 0; i< proposalSiteBean.getCongDistricts().size(); i++){
                                                ProposalCongDistrictBean propCongDistrictBean =
                                                        (ProposalCongDistrictBean)proposalSiteBean.getCongDistricts().get(i);
                                                if(propCongDistrictBean.getAcType() == null
                                                        ||(propCongDistrictBean.getAcType()!= null
                                                        && !propCongDistrictBean.getAcType().equals(TypeConstants.INSERT_RECORD))){
                                                    propCongDistrictBean.setAcType(TypeConstants.DELETE_RECORD);
                                                }
                                                cvDeletedCongDistricts.add(propCongDistrictBean);
                                            }
                                            proposalSiteBean.setCongDistricts(new CoeusVector());
                                        }
                                    }

                                    proposalSiteBean.setLocationTypeCode(Integer.parseInt(comboBoxBean.getCode()));
                            		proposalSiteBean.setLocationTypeName((String)hmLocationTypes.get(comboBoxBean.getCode()));
                                }
                                    
                                saveRequired = true;
                                if(proposalSiteBean.getAcType()==null){
                                    proposalSiteBean.setAcType(TypeConstants.UPDATE_RECORD);
                                }
                                locationTableModel.fireTableDataChanged();
                                tblLocations.setRowSelectionInterval(selectedRow, selectedRow);
                            }
                            else {
                                tblLocations.getCellEditor().stopCellEditing();
                            }
                        }
                    }
                }
            });
        }
        /**
         * Checks whether the locationType selected is a valid one. Checks whether
         * there already exists a location of the same location type code 
         * and location name
         *
         * @param locationTypeCode 
         * @param locationName
         * @return boolean true if valid else false
         */
        public boolean validateLocationType(String locationTypeCode, String locationName){
            if(locationTypeCode != null && !locationTypeCode.equals("")){
                int locTypeCode = Integer.parseInt(locationTypeCode);
                if(cvSites!=null){
                    ProposalSiteBean proposalSiteBean = null;
                    for(int locationRow=0; locationRow<cvSites.size(); locationRow++){
                        if(locationRow != selRow){
                            proposalSiteBean = (ProposalSiteBean)cvSites.get(locationRow);
                            
                            //System.out.println("validateLocationType for " + proposalSiteBean.getLocationName());
                            
                            if(proposalSiteBean.getLocationTypeCode() == locTypeCode){
                                if(!locationName.equals("") && locationName.equals(proposalSiteBean.getLocationName())){
                                    log(coeusMessageResources.parseMessageKey("proposal_sites_exceptionCode.1006"));
                                    return false;
                                }
                            }
                        }
                    }//end of for loop
                }
            }
            return true;
        }
    }
    
    /**
     * This class is used as the table model for the table tblDistricts
     */
     public class DistrictTableModel extends AbstractTableModel{
        private CoeusVector cvDistricts;
        private String[] columnNames = {"","Name"};
        
        public Object getValueAt(int row, int col){
            ProposalCongDistrictBean proposalCongDistrictBean = 
                    (ProposalCongDistrictBean)cvDistricts.get(row);
            if(col == DISTRICT_NAME_COL){
                return proposalCongDistrictBean.getCongDistrict();
            }
            return "";
        }

        public int getColumnCount() {
            return columnNames.length;
        }

        public int getRowCount() {
            if(cvDistricts !=null){
                return cvDistricts.size();
            }
            return 0;
        }
        
        public String getColumnName(int col){
            return columnNames[col];
        }
        
        public boolean isCellEditable(int row, int column){
            if(functionType == DISPLAY_MODE || column == DISTRICT_HAND_ICON_COL){
                return false;
            }else{
                return true;
            }
        }
        
        /**
         * Setter method for the property cvDistrictsData
         * @param cvDistrictsData
         */
        public void setDistrictData(CoeusVector cvDistrictsData){
            cvDistricts = cvDistrictsData;
        }
        
        /**
         * Getter method for the property cvDistricts
         * @return CoeusVector
         */
        public CoeusVector getDistrictData(){
            return cvDistricts;
        }
    }
    
    /**
     * Sets the location types to the hashmap hmLocationTypes
     */
    public void setLocationTypes(Vector locationTypes) {
        this.locationTypes = locationTypes;
        if(locationTypes != null){
            ComboBoxBean comboBoxBean = null;
            for(int i=0; i < locationTypes.size(); i++) {
                comboBoxBean = (ComboBoxBean)locationTypes.get(i);
                hmLocationTypes.put(comboBoxBean.getCode(), comboBoxBean.getDescription());
            }
        }
    }
    
    public void actionPerformed(ActionEvent e){
        Object source = e.getSource();
        if(source.equals(btnAddLoc)){
            performAddLocationAction();
        }else if(source.equals(btnDeleteLoc)){
            performDeleteLocation();
        }else if(source.equals(btnFindLoc)){
            performFindLocation();
        }else if(source.equals(btnFindAddress)){
            performFindAddressAction();
        }else if(source.equals(btnClearAddress)){
            performClearAddressAction();
        }else if(source.equals(btnAddDistrict)){
            performAddDistrictAction();
        }else if(source.equals(btnDeleteDistrict)){
            performDeleteDistrictAction();
        }
    }
    
    /* JM 2-1-2016  get org info for billing agreement */
    private ProposalSiteBean getBillingAgreementOrSub(int locationType) {
    	
    	OrganizationMaintenanceFormBean baOrg;
	    ProposalSiteBean proposalSiteBean = new ProposalSiteBean();
    
	    int selectedRow = tblLocations.getSelectedRow();
	    if (selectedRow != -1) {
	        try {
	        	baOrg = getOrganizationDetails(baOrgId,false);
	        	baOrgName = baOrg.getOrganizationName();

	        	if (baOrg != null) {
	                saveRequired = true;
	
	                proposalSiteBean = (ProposalSiteBean) cvSites.get(selectedRow);

	                proposalSiteBean.setLocationTypeCode(locationType);
	                proposalSiteBean.setOrganizationId(baOrgId);
	                proposalSiteBean.setLocationName(baOrgName);
	                String contactAddressID = ((Integer) baOrg.getContactAddressId()).toString();
	                proposalSiteBean.setRolodexDetailsBean(getOrgAddress(contactAddressID));
	
	                if (proposalSiteBean.getAcType() != null && proposalSiteBean.getAcType().equals(TypeConstants.INSERT_RECORD)) {
	                    proposalSiteBean.setCongDistricts(new CoeusVector());
	                }
	                else {
	                    proposalSiteBean.setAcType(TypeConstants.UPDATE_RECORD);
	                    if (proposalSiteBean.getCongDistricts() != null) {
	                        for (int i = 0; i< proposalSiteBean.getCongDistricts().size(); i++) {
	                            ProposalCongDistrictBean propCongDistrictBean = 
	                                    (ProposalCongDistrictBean)proposalSiteBean.getCongDistricts().get(i);
	                            if (propCongDistrictBean.getAcType() == null 
	                                    ||(propCongDistrictBean.getAcType()!= null 
	                                    && !propCongDistrictBean.getAcType().equals(TypeConstants.INSERT_RECORD))) {
	                                propCongDistrictBean.setAcType(TypeConstants.DELETE_RECORD);
	                            }
	                            cvDeletedCongDistricts.add(propCongDistrictBean);
	                        }
	                    }
	                    proposalSiteBean.setCongDistricts(new CoeusVector());
	                }
	
	                
	                if (baOrg.getCongressionalDistrict().length() != 0) {
	                    ProposalCongDistrictBean proposalCongDistrictBean = 
	                            new ProposalCongDistrictBean();
	                    proposalCongDistrictBean.setCongDistrict(Utils.convertNull(baOrg.getCongressionalDistrict()));
	                    proposalCongDistrictBean.setAcType(TypeConstants.INSERT_RECORD);
	                    proposalSiteBean.getCongDistricts().add(proposalCongDistrictBean);
	                }
	                
	                /* Need to set column value */
	                //locationTableModel.setValueAt(baOrgName, selectedRow, LOCATION_NAME_COL);
	                
	                //cvSites.set(selectedRow, proposalSiteBean);
	                /* END */
	                
	                locationTableModel.fireTableDataChanged();
	                if(proposalSiteBean.getRolodexDetailsBean() != null) {
	                    txtArAddress.setText(formatAddress(proposalSiteBean,proposalSiteBean.getRolodexDetailsBean(), false));
	                    btnClearAddress.setEnabled(true);
	                }else{
	                    btnClearAddress.setEnabled(false);
	                }
	                districtTableModel.setDistrictData(proposalSiteBean.getCongDistricts());
	                districtTableModel.fireTableDataChanged();
	                tblLocations.setRowSelectionInterval(selectedRow, selectedRow); 
	            } 
	        }
	        catch (Exception e) {
	            log("Cannot retrieve organization details for billing agreement");
	        }
	    }
	    else {
	        log(coeusMessageResources.parseMessageKey("proposal_sites_exceptionCode.1001"));
	    }
	    
	    return proposalSiteBean;
	}
    /* JM END */
    
    /**
     * Performs the find location action
     */
    private void performFindLocation(){
        int selectedRow = tblLocations.getSelectedRow();
        if(selectedRow != -1){
            try {
                CoeusSearch coeusSearch = new CoeusSearch(mdiForm, "ORGANIZATIONSEARCH", 1);
                coeusSearch.showSearchWindow();
                HashMap orgSelected = coeusSearch.getSelectedRow();
                if (orgSelected != null && !orgSelected.isEmpty() ) {
                    saveRequired = true;

                    ProposalSiteBean proposalSiteBean = (ProposalSiteBean)cvSites.get(selectedRow);
                    proposalSiteBean.setOrganizationId(Utils.convertNull(orgSelected.get("ORGANIZATION_ID")));                
                    proposalSiteBean.setLocationName(Utils.convertNull(orgSelected.get("ORGANIZATION_NAME")));
                    String contactAddressID = orgSelected.get("CONTACT_ADDRESS_ID").toString();
                    proposalSiteBean.setRolodexDetailsBean(getOrgAddress(contactAddressID));

                    if(proposalSiteBean.getAcType() != null && proposalSiteBean.getAcType().equals(TypeConstants.INSERT_RECORD)){
                        proposalSiteBean.setCongDistricts(new CoeusVector());
                    }else{
                        proposalSiteBean.setAcType(TypeConstants.UPDATE_RECORD);
                        if(proposalSiteBean.getCongDistricts() != null){
                            for(int i = 0; i< proposalSiteBean.getCongDistricts().size(); i++){
                                ProposalCongDistrictBean propCongDistrictBean = 
                                        (ProposalCongDistrictBean)proposalSiteBean.getCongDistricts().get(i);
                                if(propCongDistrictBean.getAcType() == null 
                                        ||(propCongDistrictBean.getAcType()!= null 
                                        && !propCongDistrictBean.getAcType().equals(TypeConstants.INSERT_RECORD))){
                                    propCongDistrictBean.setAcType(TypeConstants.DELETE_RECORD);
                                }
                                cvDeletedCongDistricts.add(propCongDistrictBean);
                            }
                        }
                        proposalSiteBean.setCongDistricts(new CoeusVector());
                    }

                    if(Utils.convertNull(orgSelected.get("CONGRESSIONAL_DISTRICT")).length() != 0){
                        ProposalCongDistrictBean proposalCongDistrictBean = 
                                new ProposalCongDistrictBean();
                        proposalCongDistrictBean.setCongDistrict(Utils.convertNull(orgSelected.get("CONGRESSIONAL_DISTRICT")));
                        proposalCongDistrictBean.setAcType(TypeConstants.INSERT_RECORD);
                        proposalSiteBean.getCongDistricts().add(proposalCongDistrictBean);
                    }
                    locationTableModel.fireTableDataChanged();
                    if(proposalSiteBean.getRolodexDetailsBean() != null){
                    	// JM 1-14-2015 passing in organization info so we can get the DUNS
                        txtArAddress.setText(formatAddress(proposalSiteBean,proposalSiteBean.getRolodexDetailsBean(), false));
                        // JM END
                        btnClearAddress.setEnabled(true);
                    }else{
                        btnClearAddress.setEnabled(false);
                    }
                    districtTableModel.setDistrictData(proposalSiteBean.getCongDistricts());
                    districtTableModel.fireTableDataChanged();
                    tblLocations.setRowSelectionInterval(selectedRow, selectedRow); 
                } 
            }catch (Exception e) {
                log("Coeus Search is not available ... " + e.getMessage());
            }
        }else{
            log(coeusMessageResources.parseMessageKey("proposal_sites_exceptionCode.1001"));
        }
    }
    /**
     * Performs the delete location action
     */
    private void performDeleteLocation(){
        int selectedRow = tblLocations.getSelectedRow();
        if(selectedRow != -1){
            int option = CoeusOptionPane.showQuestionDialog(
                    coeusMessageResources.parseMessageKey("proposal_sites_exceptionCode.1007"),
                    CoeusOptionPane.OPTION_YES_NO,
                    CoeusOptionPane.DEFAULT_YES);
             if(option == JOptionPane.YES_OPTION){
                ProposalSiteBean proposalSiteBean = (ProposalSiteBean)cvSites.get(selectedRow);
                if(functionType == ADD_MODE){
                    cvSites.remove(selectedRow);
                }else{
                    if(cvDeletedSites == null){
                        cvDeletedSites = new CoeusVector();
                    }
                    proposalSiteBean.setAcType(TypeConstants.DELETE_RECORD);
                    saveRequired = true;
                    cvDeletedSites.add(proposalSiteBean);
                    cvSites.remove(selectedRow);
                }
                locationTableModel.fireTableDataChanged();
                if((selectedRow-1) != -1){
                    tblLocations.setRowSelectionInterval(selectedRow-1, selectedRow-1);
                }else if(tblDistrict.getRowCount() != 0){
                    tblDistrict.setRowSelectionInterval(0,0);
                }
             }
        }else{
            log(coeusMessageResources.parseMessageKey("proposal_sites_exceptionCode.1001"));
        }
    }
    
    /**
     * Performs the add district action
     */
    private void performAddDistrictAction(){
        int tableRowCount = tblDistrict.getRowCount();
        if(tableRowCount >0){
            if(tblDistrict.isEditing()){
                    tblDistrict.getCellEditor().cancelCellEditing();
            }
        }
        ProposalCongDistrictBean proposalCongDistrictBean = 
                new ProposalCongDistrictBean();
        proposalCongDistrictBean.setAcType(TypeConstants.INSERT_RECORD);
        if(selectedLocationRow != -1){
            ProposalSiteBean proposalSiteBean = (ProposalSiteBean)cvSites.get(selectedLocationRow);
            if(proposalSiteBean.getCongDistricts() == null){
                proposalSiteBean.setCongDistricts(new CoeusVector());
            }
            proposalSiteBean.getCongDistricts().add(proposalCongDistrictBean);
            districtTableModel.setDistrictData(proposalSiteBean.getCongDistricts());
            districtTableModel.fireTableDataChanged();
            int lastRow = tblDistrict.getRowCount() -1;
            tblDistrict.setRowSelectionInterval( lastRow, lastRow );
            tblDistrict.setColumnSelectionInterval(DISTRICT_NAME_COL,DISTRICT_NAME_COL);
            tblDistrict.editCellAt(lastRow, DISTRICT_NAME_COL);
            if(tblDistrict.getEditorComponent() !=null){
                tblDistrict.getEditorComponent().requestFocusInWindow();
            }
        }else{
            log(coeusMessageResources.parseMessageKey("proposal_sites_exceptionCode.1001"));
        }
    }
    
    /**
     * Performs the delete district action
     */
    private void performDeleteDistrictAction(){
        int selectedRow = tblDistrict.getSelectedRow();
        if(selectedRow != -1){
            int option = CoeusOptionPane.showQuestionDialog(
                    coeusMessageResources.parseMessageKey("proposal_sites_exceptionCode.1008"),
                    CoeusOptionPane.OPTION_YES_NO,
                    CoeusOptionPane.DEFAULT_YES);
             if(option == JOptionPane.YES_OPTION){
                ProposalSiteBean proposalSiteBean = (ProposalSiteBean)cvSites.get(selectedLocationRow);
                if(functionType == ADD_MODE){
                    proposalSiteBean.getCongDistricts().remove(selectedRow);
                }else{
                    ProposalCongDistrictBean proposalCongDistrictBean = 
                        (ProposalCongDistrictBean)proposalSiteBean.getCongDistricts().get(selectedRow);
                    if(cvDeletedCongDistricts == null){
                        cvDeletedCongDistricts = new CoeusVector();
                    }
                    proposalCongDistrictBean.setAcType(TypeConstants.DELETE_RECORD);
                    cvDeletedCongDistricts.add(proposalCongDistrictBean);
                    proposalSiteBean.getCongDistricts().remove(selectedRow);
                    saveRequired = true;
                }
                districtTableModel.setDistrictData(proposalSiteBean.getCongDistricts());
                districtTableModel.fireTableDataChanged();  
                 if((selectedRow-1) != -1){
                    tblDistrict.setRowSelectionInterval(selectedRow-1, selectedRow-1);
                }else if(tblDistrict.getRowCount() != 0){
                    tblDistrict.setRowSelectionInterval(0,0);
                }
             }
        }else{
            log(coeusMessageResources.parseMessageKey("proposal_sites_exceptionCode.1009"));
        }
    }
    
    /**
     * Performs the clear address action
     */
    private void performClearAddressAction(){
        int selectedRow = tblLocations.getSelectedRow();
        if(selectedRow !=-1){
            int option = CoeusOptionPane.showQuestionDialog(
                    coeusMessageResources.parseMessageKey("proposal_sites_exceptionCode.1010"),
                    CoeusOptionPane.OPTION_YES_NO,
                    CoeusOptionPane.DEFAULT_YES);
             if(option == JOptionPane.YES_OPTION){
                saveRequired = true;
                ProposalSiteBean proposalSiteBean = (ProposalSiteBean)cvSites.get(selectedRow);
                proposalSiteBean.setRolodexDetailsBean(null);
                if(proposalSiteBean.getAcType()==null){
                    proposalSiteBean.setAcType(TypeConstants.UPDATE_RECORD);
                }
                txtArAddress.setText("");
                btnClearAddress.setEnabled(false);
             }
        }else{
            log(coeusMessageResources.parseMessageKey("proposal_sites_exceptionCode.1001"));
        }
    }
    
    /**
     * Performs the find address action
     */
    private void performFindAddressAction(){
        int selectedRow = tblLocations.getSelectedRow();
        if(selectedRow!=-1){
            try{
                CoeusSearch coeusSearch = new CoeusSearch(mdiForm, ROLODEX_SEARCH, 1);
                coeusSearch.showSearchWindow();
                HashMap personInfo = coeusSearch.getSelectedRow();
                if(personInfo !=null){
                    ProposalSiteBean proposalSiteBean = (ProposalSiteBean)cvSites.get(selectedRow);
                    if(proposalSiteBean.getRolodexDetailsBean() == null){
                        RolodexDetailsBean rolodexDetailsBean = new RolodexDetailsBean();
                        proposalSiteBean.setRolodexDetailsBean(rolodexDetailsBean);
                        if(proposalSiteBean.getAcType()==null){
                            proposalSiteBean.setAcType(TypeConstants.UPDATE_RECORD);
                        }
                    }else{
                        if(!Utils.convertNull(personInfo.get("ROLODEX_ID")).equals(proposalSiteBean.getRolodexDetailsBean().getRolodexId())){
                            if(proposalSiteBean.getAcType()==null){
                                proposalSiteBean.setAcType(TypeConstants.UPDATE_RECORD);
                            }
                        }
                    }

                    /* construct the full name of person */
                    rolodexDetailsBean = proposalSiteBean.getRolodexDetailsBean();
                    rolodexDetailsBean.setFirstName(Utils.convertNull(personInfo.get("FIRST_NAME")));
                    rolodexDetailsBean.setMiddleName(Utils.convertNull(personInfo.get("MIDDLE_NAME")));
                    rolodexDetailsBean.setLastName(Utils.convertNull(personInfo.get("LAST_NAME")));
                    rolodexDetailsBean.setPrefix(Utils.convertNull(personInfo.get("PREFIX")));
                    rolodexDetailsBean.setSuffix(Utils.convertNull(personInfo.get("SUFFIX")));
                    rolodexDetailsBean.setRolodexId(Utils.convertNull(personInfo.get("ROLODEX_ID")));
                    rolodexDetailsBean.setOrganization(Utils.convertNull(personInfo.get("ORGANIZATION")));
                    rolodexDetailsBean.setAddress1(Utils.convertNull(personInfo.get("ADDRESS_LINE_1")));
                    rolodexDetailsBean.setAddress2(Utils.convertNull(personInfo.get("ADDRESS_LINE_2")));
                    rolodexDetailsBean.setAddress3(Utils.convertNull(personInfo.get("ADDRESS_LINE_3")));
                    rolodexDetailsBean.setCity(Utils.convertNull(personInfo.get("CITY")));
                    rolodexDetailsBean.setCountry(Utils.convertNull(personInfo.get("COUNTY")));
                    rolodexDetailsBean.setState(Utils.convertNull(personInfo.get("STATE")));
                    rolodexDetailsBean.setPostalCode(Utils.convertNull(personInfo.get("POSTAL_CODE")));
                    rolodexDetailsBean.setCountry(Utils.convertNull(personInfo.get("COUNTRY_CODE")));

                	// JM 1-14-2015 passing in organization info so we can get the DUNS
                    //txtArAddress.setText(formatAddress(rolodexDetailsBean, false));
                    txtArAddress.setText(formatAddress(proposalSiteBean,rolodexDetailsBean, false));
                    // JM END
                    btnClearAddress.setEnabled(true);
                    saveRequired = true;
                }
            }catch(Exception ex){
                CoeusOptionPane.showErrorDialog(ex.getMessage());
            }
        }else{
            log(coeusMessageResources.parseMessageKey("proposal_sites_exceptionCode.1001"));
        }
    }
    /**
     * Performs the add location action
     */
    private void performAddLocationAction(){
        int tableRowCount = tblLocations.getRowCount();
        if(tableRowCount >0){
            if(tblLocations.isEditing()){
                    tblLocations.getCellEditor().cancelCellEditing();
            }
        }
        ProposalSiteBean proposalSiteBean = new ProposalSiteBean();
        proposalSiteBean.setProposalNumber(proposalDevelopmentFormBean.getProposalNumber());
        proposalSiteBean.setLocationName("");
        proposalSiteBean.setLocationTypeCode(0);
        proposalSiteBean.setAcType(TypeConstants.INSERT_RECORD);
        cvSites.add(proposalSiteBean);
        saveRequired = true;
        locationTableModel.fireTableDataChanged();
        if(tblLocations.getCellEditor() != null){
            tblLocations.getCellEditor().stopCellEditing();
        }
        
        int lastRow = tblLocations.getRowCount() - 1;
        tblLocations.setRowSelectionInterval( lastRow, lastRow );
        tblLocations.setColumnSelectionInterval(1,1);
        tblLocations.editCellAt(lastRow, LOCATION_TYPE_COL);
        tblLocations.getEditorComponent().requestFocusInWindow();
    }
    //Added for case 2406 - Organization and Location - end
    // Variables declaration - do not modify//GEN-BEGIN:variables
    public edu.mit.coeus.utils.CoeusButton btnAddDistrict;
    public edu.mit.coeus.utils.CoeusButton btnAddLoc;
    public edu.mit.coeus.utils.CoeusButton btnClearAddress;
    public edu.mit.coeus.utils.CoeusButton btnDeleteDistrict;
    public edu.mit.coeus.utils.CoeusButton btnDeleteLoc;
    public edu.mit.coeus.utils.CoeusButton btnFindAddress;
    public edu.mit.coeus.utils.CoeusButton btnFindLoc;
    public javax.swing.JPanel jPanel1;
    public javax.swing.JPanel jPanel2;
    public javax.swing.JPanel jPanel3;
    public javax.swing.JScrollPane jScrollPane1;
    public javax.swing.JScrollPane jScrollPane2;
    public javax.swing.JScrollPane jScrollPane3;
    public javax.swing.JTable tblDistrict;
    public javax.swing.JTable tblLocations;
    public javax.swing.JTextArea txtArAddress;
    // End of variables declaration//GEN-END:variables
    
}
