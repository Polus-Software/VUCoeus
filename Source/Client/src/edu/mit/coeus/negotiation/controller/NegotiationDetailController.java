/** Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.negotiation.controller;

import edu.mit.coeus.gui.URLOpener;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
//case 3590 start
//import java.text.SimpleDateFormat;
import java.util.Date;
import java.text.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
//case 3590 end
import java.util.HashMap;
import java.io.File;

import edu.mit.coeus.brokers.*;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.gui.CoeusAppletMDIForm;
import edu.mit.coeus.gui.CoeusMessageResources;
//import edu.mit.coeus.gui.event.BeanUpdatedListener;
import edu.mit.coeus.negotiation.bean.*;
import edu.mit.coeus.negotiation.gui.NegotiationDetailForm;
import edu.mit.coeus.negotiation.gui.ActivityDetailsPanel;
import edu.mit.coeus.search.gui.CoeusSearch;
import edu.mit.coeus.sponsormaint.gui.SponsorMaintenanceForm;
import edu.mit.coeus.sponsormaint.bean.SponsorMaintenanceFormBean;
import edu.mit.coeus.utils.AppletServletCommunicator;
import edu.mit.coeus.utils.CoeusFileChooser;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.utils.ComboBoxBean;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.DateUtils;
import edu.mit.coeus.utils.KeyConstants;
import edu.mit.coeus.utils.query.Equals;
import edu.mit.coeus.utils.query.QueryEngine;
import edu.mit.coeus.utils.ScreenFocusTraversalPolicy;
import edu.mit.coeus.utils.TypeConstants;
import edu.mit.coeus.exception.CoeusClientException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Hashtable;
import edu.mit.coeus.gui.event.*;
//end case 1735
import java.util.Observable;
import java.util.Observer;

/**
 * NegotiationDetailController.java
 * Created on July 14, 2004, 11:17 AM
 * @author  Vyjayanthi
 */
//case 3590 start
//public class NegotiationDetailController extends NegotiationController 
//implements ActionListener, MouseListener, ItemListener, BeanUpdatedListener {
public class NegotiationDetailController extends NegotiationController 
implements ActionListener, MouseListener, FocusListener, ItemListener, BeanUpdatedListener,Observer {
//case 3590 end    
    
    /** Holds an instance of the <CODE>NegotiationDetailForm</CODE> */
    private NegotiationDetailForm negotiationDetailForm = new NegotiationDetailForm();
    
    /** Holds the info details */
    private NegotiationInfoBean negotiationInfoBean;
    
    /** Holds the info bean from query engine */
    private NegotiationInfoBean qryNegotiationInfoBean;
    
    /** Holds the institute proposal details */
    private NegotiationHeaderBean negotiationHeaderBean;
    
    /** Holds negotiation activities */
    private CoeusVector cvActivities;
    
    /** Holds true if user has any osp right, false otherwise */
    private boolean hasAnyOspRight;
    
    private CoeusVector cvActivitiesPanels = new CoeusVector();
    
    /** Date utils */    
    private DateUtils dateUtils;
    
    /** Simple Date Format. */    
    private SimpleDateFormat simpleDateFormat;
    
    /** Holds the selected activity */
    private int selectedRow;
    
    /** Holds user rights */
    private CoeusVector cvUserRights ;
    
    /** Holds user rights when negotiation is opened from Medusa */
    private CoeusVector cvMedusaRights;
    
    /** Holds user rights when negotiation is opened from Institute Proposal */
    private CoeusVector cvInstitutePropRights;
    
    /** Message Resource */    
    private CoeusMessageResources coeusMessageResources;
    
    /** The mdi form */    
    private CoeusAppletMDIForm mdiForm = CoeusGuiConstants.getMDIForm();
    
    /** Query Engine instance. */    
    private QueryEngine queryEngine;
   
    /** Holds from where Negotiation is opened
     * it is either Negotiation List or Institute Proposal List */
//    private String invokedFrom;
    
    /** Holds true is the form needs to be refreshed with latest data, false otherwise */
    private boolean refreshRequired;
    
    private static final String REQUIRED_FORMAT = "dd-MMM-yyyy";
    private static final String SIMPLE_DATE_FORMAT = "MM/dd/yyyy";
    private static final String PERSON_SEARCH = "PERSONSEARCH";
    private static final String PERSON_ID = "PERSON_ID";
    private static final String FULL_NAME = "FULL_NAME";
    private static final String GET_SPONSOR_INFO = "GET_SPONSORINFO";
    private static final String CODE = "code";
    private static final String RESTRICTED_VIEW = "restrictedView";
    private static final String[] SORTING_FIELDS = {"activityDate", "activityNumber"};
    
    private static final int HAS_OSP_RIGHT = 0;
    private static final int HAS_ANY_OSP_RIGHT = 1;
    
    private final String DISPLAY_SPONSOR = "Display Sponsor";
    private static final String FUNCTION_SERVLET_URL = CoeusGuiConstants.CONNECTION_URL + "/coeusFunctionsServlet";
    
    //Messages    
    private static final String INVALID_SPONSOR_CODE = "prop_invalid_sponsor_exceptionCode.2509";
    private static final String FUNCTIONALITY_NOT_IMPL = "funcNotImpl_exceptionCode.1100";
    private static final String ENTER_NEGOTIATION_STATUS = "negotiationDetail_exceptionCode.1105";
    private static final String ENTER_NEGOTIATOR = "negotiationDetail_exceptionCode.1106";
    //case 3590 start
    private static final String SPONSOR_SEARCH = "sponsorSearch";
    private static final String ENTER_NEGOTIATION_AGREEMENT = "negotiationDetail_exceptionCode.1109";
    private static final String ENTER_EFFECTIVE_DATE = "negotiationDetail_exceptionCode.1110";
    //Please enter a valid proposed start date
    private static final String VALID_PROPOSED_START_DATE = "negotiationDetail_exceptionCode.1111";
    //Please enter a valid effective date
    private static final String VALID_EFFECTIVE_DATE = "negotiationDetail_exceptionCode.1112";
    //There is no Location information available
    private static final String NO_LOCATION_INFORMATION = "negotiationDetail_exceptionCode.1113";
    //Please enter a location.
    private static final String ENTER_LOCATION = "negotiationDetail_exceptionCode.1114";
    //Effective date should be greater than or equal to 
    private static final String RIGHT__EFFECTIVE_DATE = "negotiationDetail_exceptionCode.1115";
    private static final String DATE_SEPARATERS = ":/.,|-";
    private static final char GET_NEGOTIATION_LOCATION_HISTORY = 'S';
    private static final String NEGO_SERVLET_URL = CoeusGuiConstants.CONNECTION_URL + "/NegotiationMaintenanceServlet";
    private boolean locationTypeChange = false, locationChange = false;
    private Date date_today, currentEffDate, prevEffDate;
    private NegotiationLocationBean negotiationLocationBean;     
    private NegotiationLocationBean qryNegotiationLocationBean;
    //case 3590 end
    //case 3961 start
    private boolean linkedToInstituteProposal = false;
    private static final char INST_PROP_COUNT = 'I';
    //case 3961 end
    
    //start case 1735
     private static final char PRINT_NEGOTIATION_ACTIVITY = 'A';
     //private static final String connect = CoeusGuiConstants.CONNECTION_URL+"/printServlet";
     private static final String connect = CoeusGuiConstants.CONNECTION_URL + "/ReportConfigServlet";
     private static final String PRINT_ONE_ACTIVITY = "printOne";
     private static final String PRINT_ALL_ACTIVITY = "printAll";    
    //end case 1735 
    //Added for case 4185- start date and Closed Date Fields - Start
     private static final String ERRKEY_INVALID_START_DATE      = "negotiationDetail_exceptionCode.1116";
     private static final String ERRKEY_EMPTY_START_DATE        = "negotiationDetail_exceptionCode.1117";
     private static final String ERRKEY_INVALID_CLOSED_DATE     = "negotiationDetail_exceptionCode.1118";
     private static final String ERRKEY_INVALID_START_DT_RANGE  = "negotiationDetail_exceptionCode.1202";
     private static final String ERRKEY_INVALID_CLOSED_DT_RANGE = "negotiationDetail_exceptionCode.1203";
     private static final String ERRKEY_SELECT_ACTIVITY_TO_PRINT= "negotiationActivity_exceptionCode.1157";
     private String prevStartDate   = EMPTY;
     private String prevClosedDate  = EMPTY;
    //Added for case 4185- start date and Closed Date Fields - End
     
    /** Creates a new instance of NegotiationDetailController
     * @param negotiationBaseBean negotiation base bean
     * @param functionType function type in which this form is being opened.
     */
    public NegotiationDetailController(NegotiationBaseBean negotiationBaseBean, char functionType) {
        super(negotiationBaseBean);        
    }
    
    /** Instantiate instance objects and populate negotiation status combobox */
    public void initComponents(){
        queryEngine = QueryEngine.getInstance();
        coeusMessageResources = CoeusMessageResources.getInstance();
        dateUtils = new DateUtils();
        simpleDateFormat = new SimpleDateFormat(SIMPLE_DATE_FORMAT);
        CoeusVector cvStatus;
        //case 3590 start
        CoeusVector cvAgreementType;
        CoeusVector cvLocationType;
        date_today = new Date();
        //case 3590 end
        try{
            cvStatus = queryEngine.getDetails(queryKey, KeyConstants.NEGOTIATION_STATUS);
            ComboBoxBean emptyBean = new ComboBoxBean(EMPTY, EMPTY);
            cvStatus.add(0, emptyBean);
            negotiationDetailForm.cmbStatus.setModel(new DefaultComboBoxModel(cvStatus));
            //case 3590 start
            cvAgreementType = queryEngine.getDetails(queryKey, KeyConstants.NEGOTIATION_AGREEMENT_TYPE);
            cvAgreementType.add(0, emptyBean);
            negotiationDetailForm.cmbAgreement.setModel(new DefaultComboBoxModel(cvAgreementType));
            
            cvLocationType = queryEngine.getDetails(queryKey, KeyConstants.NEGOTIATION_LOCATION_TYPE);
            cvLocationType.add(0, emptyBean);
            negotiationDetailForm.cmbLocation.setModel(new DefaultComboBoxModel(cvLocationType));
            //case 3590 end    
        }catch (CoeusException coeusException){
            coeusException.printStackTrace();
        }
    }
    
    /** Displays the Form which is being controlled.
     */
    public void display() {
    }
    
    /** Perform field formatting.
     * enabling, disabling components depending on the different conditions
     */
    public void formatFields() {    
        if( getFunctionType() == TypeConstants.DISPLAY_MODE ){
            negotiationDetailForm.txtArTitle.setEditable(false);
            negotiationDetailForm.txtDocFolder.setEditable(false);
            negotiationDetailForm.cmbStatus.setEnabled(false);
            negotiationDetailForm.btnSelFile.setEnabled(false);
            negotiationDetailForm.btnSelPerson.setEnabled(false);
            negotiationDetailForm.btnNew.setEnabled(false);
            negotiationDetailForm.btnModify.setEnabled(false);  
            //case 3590 start
            negotiationDetailForm.cmbAgreement.setEnabled(false);
            negotiationDetailForm.txtProposedStartDate.setEnabled(false);
            negotiationDetailForm.txtPrimeSponsor.setEnabled(false);
            negotiationDetailForm.btnSelSponsor.setEnabled(false);
            negotiationDetailForm.cmbLocation.setEnabled(false);
            negotiationDetailForm.txtEffectiveDate.setEnabled(false);
            
            negotiationDetailForm.txtPrimeSponsor.setBackground(new Color(212,208,200));
            negotiationDetailForm.txtEffectiveDate.setBackground(new Color(212,208,200));
            negotiationDetailForm.txtProposedStartDate.setBackground(new Color(212,208,200));       
            //case 3590 end
            // Added for case 4185 - Start Date and Closed Date Fields - Start
            negotiationDetailForm.txtStartDate.setEditable(false);
            negotiationDetailForm.txtClosedDate.setEditable(false);
            negotiationDetailForm.txtStartDate.setBackground(new Color(212,208,200));
            negotiationDetailForm.txtClosedDate.setBackground(new Color(212,208,200));
            // Added for case 4185 - Start Date and Closed Date Fields - End
            setRequestFocusInThread(negotiationDetailForm.btnPrint);
        }else {        
            setRequestFocusInThread(negotiationDetailForm.cmbStatus);           
        }
        //Addded COEUSDEV-268 :  user without the modify negotiation role should not see the new modify activity buttons as enabled - Start
        //To enable or disable 'New' and 'Modify' activity button based on MODIFY_ACTIVITIES right
        if(getFunctionType()!= TypeConstants.DISPLAY_MODE){
            if(queryEngine == null){
                queryEngine = queryEngine.getInstance();
            }
            Hashtable negotiationData = queryEngine.getDataCollection(queryKey);
            CoeusVector cvRights = (CoeusVector)negotiationData.get("MODIFY_ACTIVITIES");
            if(cvRights != null && cvRights.size()>0){
                Boolean modifyActivity = (Boolean)cvRights.get(0);
                if(modifyActivity != null){
                    boolean hasModifyActivityRight = modifyActivity.booleanValue();
                    negotiationDetailForm.btnNew.setEnabled(hasModifyActivityRight);
                    negotiationDetailForm.btnModify.setEnabled(hasModifyActivityRight);
                }
            }
        }
        //COEUSDEV-268 : END
        // Added by chandra to fix #1199 - start.14th Sept 2004
        if(!negotiationDetailForm.txtNegotiator.isEditable()){
            if(getFunctionType()!= TypeConstants.DISPLAY_MODE){
                negotiationDetailForm.txtNegotiator.setBackground(Color.white);
                negotiationDetailForm.txtNegotiator.setForeground(Color.black);
                negotiationDetailForm.txtNegotiator.setOpaque(true);
            }
        }//Added by chandra to fix #1199 - End.14th Sept 2004        
    }
    
    /** An overridden method of the controller
     * @return negotiationDetailForm returns the controlled form component
     */
    public java.awt.Component getControlledUI() {
        return negotiationDetailForm;
    }
    
    /** Returns the form data
     * @return returns the form data
     */
    public Object getFormData() {
        return negotiationBaseBean;
    }
    
    /** This method is used to set the listeners to the components.
     */
    public void registerComponents() {

        //Setting focus traversal - START
        //case 3590 start
//        Component components[] = new Component[]{negotiationDetailForm.cmbStatus, 
//        negotiationDetailForm.btnSelPerson, negotiationDetailForm.txtDocFolder, 
//        negotiationDetailForm.btnSelFile, negotiationDetailForm.btnNew,
//        negotiationDetailForm.btnModify, negotiationDetailForm.btnPrint,
//        negotiationDetailForm.btnPrintAll};
        
        //Focus traversal order changed for Case 4185 - Added fields start date and closed date - Start
        Component components[] = new Component[]{negotiationDetailForm.cmbStatus, 
        negotiationDetailForm.btnSelPerson, 
        negotiationDetailForm.txtStartDate,negotiationDetailForm.txtClosedDate,
        negotiationDetailForm.txtDocFolder, 
        negotiationDetailForm.btnSelFile, negotiationDetailForm.cmbAgreement,
        negotiationDetailForm.txtProposedStartDate,
        negotiationDetailForm.cmbLocation,negotiationDetailForm.txtEffectiveDate, negotiationDetailForm.btnLocation,
        negotiationDetailForm.txtPrimeSponsor,negotiationDetailForm.btnSelSponsor, 
        negotiationDetailForm.btnNew, negotiationDetailForm.btnModify, 
        negotiationDetailForm.btnPrint, negotiationDetailForm.btnPrintAll};
        //Focus traversal order changed for Case 4185 - Added fields start date and closed date - End
        //case 3590 end
      
        ScreenFocusTraversalPolicy  traversePolicy = new ScreenFocusTraversalPolicy( components );
        negotiationDetailForm.setFocusTraversalPolicy(traversePolicy);
        negotiationDetailForm.setFocusCycleRoot(true);
        //Setting focus traversal - END
        
        negotiationDetailForm.btnNew.addActionListener(this);
        negotiationDetailForm.btnModify.addActionListener(this);
        negotiationDetailForm.btnSelFile.addActionListener(this);
        negotiationDetailForm.btnSelPerson.addActionListener(this);
        negotiationDetailForm.txtSponsor.addMouseListener(this);
        negotiationDetailForm.btnPrint.addActionListener(this);
        negotiationDetailForm.btnPrintAll.addActionListener(this);
        negotiationDetailForm.cmbStatus.addItemListener(this);
        
        //case 3590 start
        negotiationDetailForm.cmbAgreement.addItemListener(this);
        negotiationDetailForm.txtProposedStartDate.addFocusListener(this);
        negotiationDetailForm.txtPrimeSponsor.addFocusListener(this);
        negotiationDetailForm.txtPrimeSponsor.addMouseListener(this);
        negotiationDetailForm.btnSelSponsor.addActionListener(this);
        //negotiationDetailForm.cmbLocation.addItemListener(this);
        negotiationDetailForm.txtEffectiveDate.addFocusListener(this);
        negotiationDetailForm.btnLocation.addActionListener(this);
        //Added for Case 4185 - New fields start date and closed date - Start
        negotiationDetailForm.txtStartDate.addFocusListener(this);
        negotiationDetailForm.txtClosedDate.addFocusListener(this);
        //Added for Case 4185 - New fields start date and closed date - End
        addBeanUpdatedListener(this, NegotiationLocationBean.class);        
        //case 3590 end

        
        addBeanUpdatedListener(this, NegotiationHeaderBean.class);
        addBeanUpdatedListener(this, NegotiationActivitiesBean.class);
        //start case 1735
        addBeanUpdatedListener(this, NegotiationInfoBean.class);
        //end case 1735
        
    }
    
    /** This method is used to remove the listeners of the components.
     */
    public void unRegisterComponents() {
        negotiationDetailForm.btnNew.removeActionListener(this);
        negotiationDetailForm.btnModify.removeActionListener(this);
        negotiationDetailForm.btnSelFile.removeActionListener(this);
        negotiationDetailForm.btnSelPerson.removeActionListener(this);
        negotiationDetailForm.txtSponsor.removeMouseListener(this);
        negotiationDetailForm.btnPrint.removeActionListener(this);
        negotiationDetailForm.btnPrintAll.removeActionListener(this);
        negotiationDetailForm.cmbStatus.removeItemListener(this);
        
        //case 3590 start
        negotiationDetailForm.cmbAgreement.removeItemListener(this);
        negotiationDetailForm.txtProposedStartDate.removeFocusListener(this);
        negotiationDetailForm.txtPrimeSponsor.removeFocusListener(this); 
        negotiationDetailForm.txtPrimeSponsor.removeMouseListener(this);
        negotiationDetailForm.btnSelSponsor.removeActionListener(this);
        negotiationDetailForm.cmbLocation.removeItemListener(this);
        negotiationDetailForm.txtEffectiveDate.removeFocusListener(this);
        negotiationDetailForm.btnLocation.removeActionListener(this);
        //Added for Case 4185 - New fields start date and closed date - Start
        negotiationDetailForm.txtStartDate.removeFocusListener(this);
        negotiationDetailForm.txtClosedDate.removeFocusListener(this);
        //Added for Case 4185 - New fields start date and closed date - End
        removeBeanUpdatedListener(this, NegotiationLocationBean.class);        
        //case 3590 end
        
        removeBeanUpdatedListener(this, NegotiationHeaderBean.class);
        removeBeanUpdatedListener(this, NegotiationActivitiesBean.class);
        //start case 1735
        removeBeanUpdatedListener(this, NegotiationInfoBean.class);
        //end case 1735
    }

    /** Sets the focus in the given component
     * @param component for which the focus has to be set
     */
    private void setRequestFocusInThread(final Component component) {
        SwingUtilities.invokeLater( new Runnable() {
            public void run() {
                component.requestFocusInWindow();
            }
        });
    }
    
    /** Saves the Form Data.
     */
    public void saveFormData() {
        
        
        if( isDataModified() ){
            
         //case 3590 start
         try{        
         //case 3590 end            
            ComboBoxBean comboBoxBean = (ComboBoxBean)negotiationDetailForm.cmbStatus.getSelectedItem();
            negotiationInfoBean.setStatusCode(Integer.parseInt(comboBoxBean.getCode()));
            //Added for case 4185 - new editable fields - start date and closed date -Start
            String strStartDate = negotiationDetailForm.txtStartDate.getText().trim();
            if (!strStartDate.equals(EMPTY)){
                Date date = (java.util.Date)simpleDateFormat.parse(dateUtils.restoreDate(strStartDate,DATE_SEPARATERS));
                negotiationInfoBean.setStartDate(new java.sql.Date(date.getTime()));
            }else{
                 negotiationInfoBean.setStartDate(null);
            }
             String strClosedDate = negotiationDetailForm.txtClosedDate.getText().trim();
             if (!strClosedDate.equals(EMPTY)){
                Date date = (java.util.Date)simpleDateFormat.parse(dateUtils.restoreDate(strClosedDate,DATE_SEPARATERS));
                negotiationInfoBean.setClosedDate(new java.sql.Date(date.getTime()));
            }else{
                 negotiationInfoBean.setClosedDate(null);
            }
            //Added for case 4185 - new editable fields - start date and end date - End
            negotiationInfoBean.setDocFileAddress(negotiationDetailForm.txtDocFolder.getText());
            //case 3590 start
            ComboBoxBean comboBoxBeanAgree = (ComboBoxBean)negotiationDetailForm.cmbAgreement.getSelectedItem();
            negotiationInfoBean.setNegotiationAgreeTypeCode(Integer.parseInt(comboBoxBeanAgree.getCode()));
            String strDate = negotiationDetailForm.txtProposedStartDate.getText().trim();
            if (!strDate.equals(EMPTY)){
                java.util.Date date = (java.util.Date)simpleDateFormat.parse(dateUtils.restoreDate(strDate,DATE_SEPARATERS));
                negotiationInfoBean.setProposedStartDate(new java.sql.Date(date.getTime()));
            }else {
                negotiationInfoBean.setProposedStartDate(null);
            }
           //case 3961 start
            if (!linkedToInstituteProposal) {
           //case3961                
                String sponsorCode = negotiationDetailForm.txtPrimeSponsor.getText().trim();
                if (sponsorCode.equals(EMPTY))
                    negotiationInfoBean.setPrimeSponsorCode(null);
                else if ( validateSponsorCode(sponsorCode) != null){                
                        negotiationInfoBean.setPrimeSponsorCode(sponsorCode);
                }else {
                    CoeusOptionPane.showInfoDialog(
                            coeusMessageResources.parseMessageKey(INVALID_SPONSOR_CODE));
                }            
            }   
            if (isLocationDataChanged() ){                
                ComboBoxBean comboBoxBeanLocaton = (ComboBoxBean)negotiationDetailForm.cmbLocation.getSelectedItem();
                negotiationLocationBean.SetNegotiationLocationTypeCode(Integer.parseInt(comboBoxBeanLocaton.getCode()));
                strDate = negotiationDetailForm.txtEffectiveDate.getText().trim();
                if (!strDate.equals(EMPTY)){
                    java.util.Date date = (java.util.Date)simpleDateFormat.parse(dateUtils.restoreDate(strDate,DATE_SEPARATERS));
                    negotiationLocationBean.setEffectiveDate(new java.sql.Date(date.getTime()));
                }else {
                    negotiationLocationBean.setEffectiveDate(null);
                }
                negotiationLocationBean.setNegotiationNumber(negotiationInfoBean.getNegotiationNumber());
                if(locationTypeChange){
                    if (qryNegotiationLocationBean != null){
                        negotiationLocationBean.SetLocationNumber(qryNegotiationLocationBean.getLocationNumber() + 1);
                    }else {
                        negotiationLocationBean.SetLocationNumber(1);
                    }
                    negotiationLocationBean.setAcType(TypeConstants.INSERT_RECORD);
                }else{
                    negotiationLocationBean.SetLocationNumber(qryNegotiationLocationBean.getLocationNumber());
                    negotiationLocationBean.setAcType(TypeConstants.UPDATE_RECORD);
                }
            }
            //case 3590 end 
            if( negotiationInfoBean.getAcType() == null ){
                negotiationInfoBean.setAcType(TypeConstants.UPDATE_RECORD);
            }
            //case 3590 start
//            try{
            //case 3590 end
            queryEngine.update(queryKey, negotiationInfoBean);
            //case 3590 start
            if (locationTypeChange){
                queryEngine.insert(queryKey, negotiationLocationBean);
            }else if (locationChange){
                queryEngine.update(queryKey, negotiationLocationBean);
            }        
               
         } catch(ParseException parseException){
            parseException.printStackTrace();
         }catch (CoeusClientException coeusClientException){
            CoeusOptionPane.showDialog(coeusClientException);
         //case 3590 end
         }catch (CoeusException coeusException){
                coeusException.printStackTrace();
         }
        }
    }
    
    /** This method is used to set the form data specified in
     * <CODE> data </CODE>
     * @param data data to set to the form
     */
    public void setFormData(Object data) {
//        this.invokedFrom = (String)data;
        if( getFunctionType() != TypeConstants.ADD_MODE ){
            //Set values of detail, header, activities
            try{
                CoeusVector cvData = queryEngine.getDetails(queryKey, NegotiationInfoBean.class);
                if( cvData != null && cvData.size() > 0 ){
                    negotiationInfoBean = (NegotiationInfoBean)cvData.get(0);

                    //Query again to avoid reference problem
                    //Used in isDataModified to check if negotiation data is modified
                    cvData = queryEngine.getDetails(queryKey, NegotiationInfoBean.class);
                    qryNegotiationInfoBean = (NegotiationInfoBean)cvData.get(0);
                    if( qryNegotiationInfoBean.getDocFileAddress() == null ){
                        qryNegotiationInfoBean.setDocFileAddress(EMPTY);
                    }else{
                        negotiationDetailForm.txtDocFolder.setText(qryNegotiationInfoBean.getDocFileAddress());
                    }
                }
                

                cvData = queryEngine.getDetails(queryKey, NegotiationHeaderBean.class);
                if( cvData != null && cvData.size() > 0 ){
                    negotiationHeaderBean = (NegotiationHeaderBean)cvData.get(0);
                }

                ComboBoxBean comboBoxBean = new ComboBoxBean();
                comboBoxBean.setCode(EMPTY + negotiationInfoBean.getStatusCode());
                comboBoxBean.setDescription(negotiationInfoBean.getStatusDescription());
                negotiationDetailForm.cmbStatus.setSelectedItem(comboBoxBean);
                
                //case 3590 start    
                
                comboBoxBean = new ComboBoxBean();
                comboBoxBean.setCode(EMPTY + negotiationInfoBean.getNegotiationAgreeTypeCode());
                comboBoxBean.setDescription(negotiationInfoBean.getNegotiationAgreeTypeDescription());
                negotiationDetailForm.cmbAgreement.setSelectedItem(comboBoxBean);
                
                
                cvData = queryEngine.getDetails(queryKey, NegotiationLocationBean.class);
                if( cvData != null && cvData.size() > 0 ){
                    negotiationLocationBean = (NegotiationLocationBean)cvData.get(0);
                    //Query again to avoid reference problem
                    cvData = queryEngine.getDetails(queryKey, NegotiationLocationBean.class);
                    qryNegotiationLocationBean = (NegotiationLocationBean)cvData.get(0);
                    
                }else{
                    negotiationLocationBean = new NegotiationLocationBean();
                }
                if (qryNegotiationLocationBean != null){
                    CoeusVector cvLocationHistory = (CoeusVector)getLocationHistoryData();
                    if (cvLocationHistory != null && cvLocationHistory.size() > 1 ){
                        Equals eq = new Equals("locationNumber", new Integer(qryNegotiationLocationBean.getLocationNumber() - 1));
                        cvLocationHistory = cvLocationHistory.filter(eq);
                        if (cvLocationHistory != null && cvLocationHistory.size() > 0){
                            NegotiationLocationBean prevNegotiationLocationBean = (NegotiationLocationBean)cvLocationHistory.get(0);   
                            if (prevNegotiationLocationBean != null && prevNegotiationLocationBean.getEffectiveDate() != null){
                                prevEffDate = (java.util.Date)prevNegotiationLocationBean.getEffectiveDate();
                            }
                        }
                    }
                }
                
                if (negotiationLocationBean != null && negotiationLocationBean.getEffectiveDate() != null){
                    currentEffDate = (java.util.Date)negotiationLocationBean.getEffectiveDate();
                }
                
                if ( negotiationLocationBean != null && negotiationLocationBean.getNegotiationLocationTypeCode() > 0 ){
                    comboBoxBean = new ComboBoxBean();
                    comboBoxBean.setCode(EMPTY + negotiationLocationBean.getNegotiationLocationTypeCode());
                    comboBoxBean.setDescription(negotiationLocationBean.getNegotiationLocationTypeDes());
                }else{
                    comboBoxBean = new ComboBoxBean(EMPTY, EMPTY);
                }
                negotiationDetailForm.cmbLocation.setSelectedItem(comboBoxBean);
                negotiationDetailForm.cmbLocation.addItemListener(this);
                //case 3590 end 

                //Set data to all components
                setRefreshRequired(true);
                setNegotiationData();
                
                //Get negotiation activities
                cvActivities = queryEngine.executeQuery(queryKey, NegotiationActivitiesBean.class, CoeusVector.FILTER_ACTIVE_BEANS);
                hasAnyOspRight = ((Boolean)cvUserRights.get(HAS_ANY_OSP_RIGHT)).booleanValue();
                if( cvActivities != null && cvActivities.size() > 0 ){
                    if( !hasAnyOspRight ){
                        Equals eqRestrictedView = new Equals(RESTRICTED_VIEW, false);
                        cvActivities = cvActivities.filter(eqRestrictedView);
                    }
                    setNegotiationActivities(hasAnyOspRight);
                }
                //Added for COEUSDEV-294 :  Error adding activity to a negotiation - Start
                //To refresh the activity panel when no activities are present
                else if(cvActivities == null || (cvActivities != null && cvActivities.size() < 1)){
                    setNegotiationActivities(hasAnyOspRight);
                }
                //COEUSDEV - 294 : End
                
            }catch (CoeusException coeusException){
                coeusException.printStackTrace();
            }
        }else{//case 4140 start
            negotiationDetailForm.cmbLocation.addItemListener(this);
            //case 4140 end
        }
    }
    
    /** Sets the negotiation data to all components
     */
    private void setNegotiationData() {
        CoeusVector cvData;
        try{
            if( isRefreshRequired() ){
                cvData = queryEngine.getDetails(queryKey, NegotiationInfoBean.class);
                if( cvData != null && cvData.size() > 0 ){
                    negotiationInfoBean = (NegotiationInfoBean)cvData.get(0);
                }

                cvData = queryEngine.getDetails(queryKey, NegotiationHeaderBean.class);
                if( cvData != null && cvData.size() > 0 ){
                    negotiationHeaderBean = (NegotiationHeaderBean)cvData.get(0);
                }
                
                //case 3961 start
                checkNegotiationPrimaeSponsor();
                //case 3961 end
            }
            negotiationDetailForm.txtPropNo.setText(negotiationInfoBean.getNegotiationNumber());
            negotiationDetailForm.txtNegotiator.setText(negotiationInfoBean.getNegotiatorName());
            negotiationDetailForm.txtDocFolder.setText(negotiationInfoBean.getDocFileAddress());
            if( negotiationInfoBean.getStartDate() != null ){
                negotiationDetailForm.txtStartDate.setText(dateUtils.formatDate(negotiationInfoBean.getStartDate().toString(), REQUIRED_FORMAT));
            }else{
                negotiationDetailForm.txtStartDate.setText(EMPTY);
            }
            //case 4185 - New field closed date - start
            if( negotiationInfoBean.getClosedDate() != null ){
                negotiationDetailForm.txtClosedDate.setText(dateUtils.formatDate(negotiationInfoBean.getClosedDate().toString(), REQUIRED_FORMAT));
            }else{
                negotiationDetailForm.txtClosedDate.setText(EMPTY);
            }
            //case 4185 - New field closed date - end
            negotiationDetailForm.txtPI.setText(negotiationHeaderBean.getPiName());
            /** Bug Fix 1864 -start
             */
            if(negotiationHeaderBean.getSponsorCode() == null){
                negotiationDetailForm.txtSponsor.setText(EMPTY);
            }else{
                negotiationDetailForm.txtSponsor.setText(negotiationHeaderBean.getSponsorCode().trim());
            }
            /** Bug Fix 1864 -End
             */
            negotiationDetailForm.lblSponsorName.setText(negotiationHeaderBean.getSponsorName());
            negotiationDetailForm.txtLeadUnit.setText(negotiationHeaderBean.getLeadUnit());
            negotiationDetailForm.lblLeadUnitName.setText(negotiationHeaderBean.getUnitName());
            negotiationDetailForm.txtArTitle.setText(negotiationHeaderBean.getTitle());
            negotiationDetailForm.txtArTitle.setCaretPosition(0);

            cvData = queryEngine.getDetails(queryKey, KeyConstants.PROPOSAL_TYPE);
            if( cvData != null && cvData.size() > 0 ){
                Equals eqPropType = new Equals(CODE, EMPTY+ negotiationHeaderBean.getProposalTypeCode());
                CoeusVector cvTemp = cvData.filter(eqPropType);
                if( cvTemp != null && cvTemp.size() > 0 ){
                    negotiationDetailForm.txtPropType.setText(((ComboBoxBean)cvData.get(0)).getDescription());
                }
            }
        negotiationDetailForm.txtPropType.setText(negotiationHeaderBean.getProposalTypeDescription());
        negotiationDetailForm.txtContractAdmin.setText(negotiationHeaderBean.getInitialContractAdmin());
  
        //case 3590 start    
        if( negotiationInfoBean.getProposedStartDate() != null ){
            negotiationDetailForm.txtProposedStartDate.setText(dateUtils.formatDate(negotiationInfoBean.getProposedStartDate().toString(), REQUIRED_FORMAT));
        }else{
            negotiationDetailForm.txtProposedStartDate.setText(EMPTY);
        }
        //case 3961 start
        if (linkedToInstituteProposal) {
            if(negotiationHeaderBean.getPrimeSponsorCode() == null){
                negotiationDetailForm.txtPrimeSponsor.setText(EMPTY);
            }else{
                negotiationDetailForm.txtPrimeSponsor.setText(negotiationHeaderBean.getPrimeSponsorCode().trim());
            }
         
            negotiationDetailForm.lblPrimeSponsorName.setText(negotiationHeaderBean.getPrimeSponsorName());
                
        }else {
        //case 3961 end
            if(negotiationInfoBean.getPrimeSponsorCode() == null){
                negotiationDetailForm.txtPrimeSponsor.setText(EMPTY);
            }else{
                negotiationDetailForm.txtPrimeSponsor.setText(negotiationInfoBean.getPrimeSponsorCode().trim());
            }          
            negotiationDetailForm.lblPrimeSponsorName.setText(negotiationInfoBean.getPrimeSponsorName());
        }
       
        if (negotiationLocationBean != null && negotiationLocationBean.getEffectiveDate() != null){
            negotiationDetailForm.txtEffectiveDate.setText(dateUtils.formatDate(negotiationLocationBean.getEffectiveDate().toString(), REQUIRED_FORMAT));
//            //to set num of days
            Date effDate = negotiationLocationBean.getEffectiveDate();
            if (!effDate.equals(EMPTY)){
                if (effDate.after(date_today)){
                    negotiationDetailForm.txtNumOfDays.setText("");
                }else{
                    negotiationDetailForm.txtNumOfDays.setText(dateUtils.dateDifference(date_today,effDate)+"");
//                    int numOfdays = dateUtils.dateDifference(date_today,effDate);
//                    if (numOfdays == 0)
//                                negotiationDetailForm.txtNumOfDays.setText("");
//                            else
//                                negotiationDetailForm.txtNumOfDays.setText(numOfdays +"");
                }                    
            }           
            
        }else{
            negotiationDetailForm.txtEffectiveDate.setText(EMPTY);
            negotiationDetailForm.txtNumOfDays.setText(EMPTY);
        }   
        //case 3590 end
        }catch (CoeusException coeusException){
            coeusException.printStackTrace();
        }
    }
    /** Set the Negotiation Activity details
     * @param hasAnyOspRight holds true if user has any osp right
     */
    private void setNegotiationActivities(boolean hasAnyOspRight) {
        if( isRefreshRequired() ) {
            negotiationDetailForm.pnlActivities.removeAll();
            try{
                cvActivities = queryEngine.executeQuery(queryKey, NegotiationActivitiesBean.class, CoeusVector.FILTER_ACTIVE_BEANS);
            }catch (CoeusException coeusException){
                coeusException.printStackTrace();
            }
        }
        if( cvActivities != null ){
            cvActivities.sort(SORTING_FIELDS, false);
        }
        //Modified for COEUSDEV-268 : user without the modify negotiation role should not see the new modify activity buttons as enabled - Start
        //Enable or disable 'New' and 'Modify' of activities based on the MODIFY_ACTIVITIES right
        if(getFunctionType() != TypeConstants.DISPLAY_MODE){
            if(queryEngine == null){
                queryEngine = queryEngine.getInstance();
            }
            Hashtable negotiationData = queryEngine.getDataCollection(queryKey);
            CoeusVector cvRights = (CoeusVector)negotiationData.get("MODIFY_ACTIVITIES");
            if(cvRights != null && cvRights.size()>0){
                Boolean modifyActivity = (Boolean)cvRights.get(0);
                if(modifyActivity != null){
                    boolean hasModifyActivityRight = modifyActivity.booleanValue();
                    negotiationDetailForm.btnNew.setEnabled(hasModifyActivityRight);
                    negotiationDetailForm.btnModify.setEnabled(hasModifyActivityRight);
                }
            }
        }
        //COEUSDEV-268 : End
       for( int index = 0; index < cvActivities.size(); index++ ){
            NegotiationActivitiesBean negotiationActivitiesBean = (NegotiationActivitiesBean)cvActivities.get(index);
            addBeanListener(negotiationActivitiesBean);
            //Added for case 2806 - Upload attachments to neg module - Start
            char functionType = getFunctionType();
            if( cvUserRights != null && cvUserRights.size() > 0 ){
                boolean hasOspRight = ((Boolean)cvUserRights.get(HAS_OSP_RIGHT)).booleanValue();
                if( functionType == TypeConstants.MODIFY_MODE && !hasOspRight ){
                    if( negotiationActivitiesBean.getUpdateUser() != null &&
                            !(negotiationActivitiesBean.getUpdateUser().equalsIgnoreCase(mdiForm.getUserName())) ){
                        functionType = TypeConstants.DISPLAY_MODE;
                    }
                }
            }
            //Addded COEUSDEV-268 :  user without the modify negotiation role should not see the new modify activity buttons as enabled - Start
            //To enable or diable activity panel based on MODIFY_ACTIVITIES rights
            if(functionType != TypeConstants.DISPLAY_MODE){
                if(queryEngine == null){
                    queryEngine = queryEngine.getInstance();
                }
                Hashtable negotiationData = queryEngine.getDataCollection(queryKey);
                CoeusVector cvRights = (CoeusVector)negotiationData.get("MODIFY_ACTIVITIES");
                if(cvRights != null && cvRights.size()>0){
                    Boolean modifyActivity = (Boolean)cvRights.get(0);
                    if(modifyActivity != null){
                        boolean hasModifyActivityRight = modifyActivity.booleanValue();
                        if(!hasModifyActivityRight){
                            functionType = TypeConstants.DISPLAY_MODE;
                        }
                    }
                }
            }
            //COEUSDEV-286 : End
            ActivityDetailsPanel activityDetailsPanel = new ActivityDetailsPanel(negotiationActivitiesBean, functionType);
            activityDetailsPanel.setRestrictedViewVisible(hasAnyOspRight);
            activityDetailsPanel.setParentFunctionType(getFunctionType());
            //Added for case 2806 - Upload attachments to neg module - End
            activityDetailsPanel.setRowIndex(index);
            activityDetailsPanel.addMouseListener(this);
            activityDetailsPanel.txtArDescription.addMouseListener(this);
            activityDetailsPanel.chkOspOnly.addMouseListener(this);
            if( index == 0 ){
                activityDetailsPanel.lblIcon.setIcon(new ImageIcon(getClass().getClassLoader().getResource(CoeusGuiConstants.NEGOTIATION_FOLDMARK_ICON)));
                activityDetailsPanel.txtArDescription.setForeground(Color.BLUE);
                activityDetailsPanel.scrPnDescription.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
                selectedRow = 0;
            }
         
            cvActivitiesPanels.addElement(activityDetailsPanel.getChildPanel());
            negotiationDetailForm.pnlActivities.add(activityDetailsPanel.getChildPanel());
            
        }
        if(cvActivities.size() <= 3 ){
            negotiationDetailForm.pnlActivities.setLayout(new java.awt.GridLayout(3,1,0,0));
        }else{
            negotiationDetailForm.pnlActivities.setLayout(new java.awt.GridLayout(cvActivities.size(),1,0,0));
        }
        negotiationDetailForm.pnlActivities.revalidate();
    }

    /** To check if any bean values have changed */
    private void addBeanListener(NegotiationActivitiesBean negotiationActivitiesBean){
        negotiationActivitiesBean.addPropertyChangeListener(
        new PropertyChangeListener(){
            public void propertyChange(PropertyChangeEvent pce){
                if ( pce.getNewValue() == null && pce.getOldValue() != null ) {
                    setSaveRequired(true);
                }
                if( pce.getNewValue() != null && pce.getOldValue() == null ) {
                    setSaveRequired(true);
                }
                if( pce.getNewValue()!=null && pce.getOldValue()!=null ) {
                    if (!(  pce.getNewValue().toString().trim().equalsIgnoreCase(pce.getOldValue().toString().trim())))  {
                        setSaveRequired(true);
                    }
                }
            }
            });
    }

    /** Displays the Person Search screen
     */
    private void showPersonSearch(){
        try{
            CoeusSearch personSearch = new CoeusSearch( 
                mdiForm, PERSON_SEARCH, CoeusSearch.TWO_TABS );
            personSearch.showSearchWindow();
            HashMap hmSelectedPerson = personSearch.getSelectedRow();
            if( hmSelectedPerson != null ){
                String personId = (String)hmSelectedPerson.get(PERSON_ID);
                String personName = (String)hmSelectedPerson.get(FULL_NAME);
                negotiationInfoBean.setNegotiatorId(personId);
                negotiationInfoBean.setNegotiatorName(personName);
                if( negotiationInfoBean.getAcType() == null ){
                    negotiationInfoBean.setAcType(TypeConstants.UPDATE_RECORD);
                }
                queryEngine.update(queryKey, negotiationInfoBean);
                negotiationDetailForm.txtNegotiator.setText(personName);
            }
        }catch (Exception exception){
            exception.printStackTrace();
        }
    }
    
    /** Validate the form data/Form and returns true if
     * validation is through else returns false.
     * @throws CoeusUIException if some exception occurs or some validation fails.
     * @return true if
     * validation is through else returns false.
     */
    public boolean validate() throws edu.mit.coeus.exception.CoeusUIException {
        if( negotiationDetailForm.cmbStatus.getSelectedIndex() == 0 ){
            CoeusOptionPane.showInfoDialog(
            coeusMessageResources.parseMessageKey(ENTER_NEGOTIATION_STATUS));
            negotiationDetailForm.cmbStatus.requestFocusInWindow();
            return false;
        }else if( negotiationDetailForm.txtNegotiator.getText() == null ||
        negotiationDetailForm.txtNegotiator.getText().trim().equals(EMPTY)){
            CoeusOptionPane.showInfoDialog(
            coeusMessageResources.parseMessageKey(ENTER_NEGOTIATOR));
            negotiationDetailForm.btnSelPerson.requestFocusInWindow();
            return false;
        }
        //Added for case 4185 - Start Date and Closed Date Validations - Start
        else if( negotiationDetailForm.txtStartDate.getText() == null ||
        negotiationDetailForm.txtStartDate.getText().trim().equals(EMPTY)){
            CoeusOptionPane.showInfoDialog(
            coeusMessageResources.parseMessageKey(ERRKEY_EMPTY_START_DATE));
            negotiationDetailForm.txtStartDate.requestFocusInWindow();
            setRequestFocusInThread(negotiationDetailForm.txtStartDate);
            return false;
        }
        //Added for case 4185 - Start Date and Closed Date Validations - End
        //case 3590 start
        else if( negotiationDetailForm.cmbAgreement.getSelectedIndex() == 0 ){
            CoeusOptionPane.showInfoDialog(
            coeusMessageResources.parseMessageKey(ENTER_NEGOTIATION_AGREEMENT));
            negotiationDetailForm.cmbAgreement.requestFocusInWindow();
            return false;
        }
        //if the location is selected then the effective date is required.
        else if (negotiationDetailForm.cmbLocation.getSelectedIndex() != 0 && (
                negotiationDetailForm.txtEffectiveDate.getText() == null ||
                negotiationDetailForm.txtEffectiveDate.getText().trim().equals(EMPTY))) {
            CoeusOptionPane.showInfoDialog(
            coeusMessageResources.parseMessageKey(ENTER_EFFECTIVE_DATE));
            negotiationDetailForm.txtEffectiveDate.requestFocusInWindow();
            return false;
        }
        //if the effective date is entered then the location is required.
        else if (!negotiationDetailForm.txtEffectiveDate.getText().equals(EMPTY)
                    && negotiationDetailForm.cmbLocation.getSelectedIndex() == 0 ) {
            CoeusOptionPane.showInfoDialog(
            coeusMessageResources.parseMessageKey(ENTER_LOCATION));
            negotiationDetailForm.txtEffectiveDate.requestFocusInWindow();
            return false;
        }
        //validate effective date 
        else {
            String effDate = negotiationDetailForm.txtEffectiveDate.getText().trim();            
            if (!effDate.equals(EMPTY) && qryNegotiationLocationBean != null){    
                try{
//                  Added with case 4185 - implement correct date format parsing.
                    effDate = dateUtils.restoreDate(effDate,DATE_SEPARATERS);
                    java.util.Date date = (java.util.Date)simpleDateFormat.parse(effDate);
                    if (negotiationLocationBean.getNegotiationLocationTypeCode() != qryNegotiationLocationBean.getNegotiationLocationTypeCode()){            
                        if (currentEffDate != null && currentEffDate.after(date)){
                            SimpleDateFormat dateFormat = new SimpleDateFormat(REQUIRED_FORMAT);
                            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(RIGHT__EFFECTIVE_DATE) + " " + dateFormat.format(currentEffDate));
                            setRequestFocusInThread(negotiationDetailForm.txtEffectiveDate);
                            return false;                                
                        }else if(prevEffDate != null&& prevEffDate.after(date)){
                            SimpleDateFormat dateFormat = new SimpleDateFormat(REQUIRED_FORMAT);
                            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(RIGHT__EFFECTIVE_DATE) + " " + dateFormat.format(prevEffDate));
                            setRequestFocusInThread(negotiationDetailForm.txtEffectiveDate);
                            return false;                          
                        }

                    }
                } catch(ParseException parseException){
                        parseException.printStackTrace();
                }
            }
            //Added for case 4185 - Start Date and Closed Date Validations - Start
            String strClosedDate = negotiationDetailForm.txtClosedDate.getText().trim();
            if(!strClosedDate.equals(EMPTY)){
                String strStartDate  = negotiationDetailForm.txtStartDate.getText().trim();
                try{
                    strClosedDate = dateUtils.restoreDate(strClosedDate,DATE_SEPARATERS);
                    Date cDate = (Date)simpleDateFormat.parse(strClosedDate);
                    strStartDate = dateUtils.restoreDate(strStartDate,DATE_SEPARATERS);
                    Date sDate = (Date)simpleDateFormat.parse(strStartDate);
                    if(sDate.after(cDate)){
                        CoeusOptionPane.showInfoDialog(
                                coeusMessageResources.parseMessageKey(ERRKEY_INVALID_CLOSED_DT_RANGE));
                        setRequestFocusInThread(negotiationDetailForm.txtClosedDate);
                        return false;
                    }
                }catch(ParseException pe){
                    pe.printStackTrace();
                }
            }
            //4185 End
        }
        //case 3590 end 
        return true;
    }
    
    public void mouseClicked(java.awt.event.MouseEvent mouseEvent) {
        try{
            if( mouseEvent.getSource() == negotiationDetailForm.txtSponsor &&
            mouseEvent.getClickCount() == 2 ){
                String sponsorCode = negotiationDetailForm.txtSponsor.getText();
                String sponsorDesc = validateSponsorCode(sponsorCode);
                if (sponsorDesc == null ) {
                    CoeusOptionPane.showInfoDialog(
                        coeusMessageResources.parseMessageKey(INVALID_SPONSOR_CODE));
                }else{
                    SponsorMaintenanceForm sponsorForm = new SponsorMaintenanceForm(TypeConstants.DISPLAY_MODE, sponsorCode);
                    sponsorForm.showForm(mdiForm, DISPLAY_SPONSOR, true);
                }
            }else if( mouseEvent.getSource().getClass().equals(ActivityDetailsPanel.class) ||
            mouseEvent.getSource().getClass().equals(JTextArea.class) ||
            mouseEvent.getSource().getClass().equals(JCheckBox.class) ) {

                //Display Activities Screen on doubleClick
                if( mouseEvent.getClickCount() == 2 ){
                    if( selectedRow != -1 ){
                        blockEvents(true);
                        //If negotiation is opened in new mode and some activities are present
                        char functionType = getFunctionType();
                        if( functionType == TypeConstants.ADD_MODE && cvActivities.size() > 0 ){
                            functionType = TypeConstants.MODIFY_MODE;
                        }
                        //Addded for COEUSDEV-268 :  user without the modify negotiation role should not see the new modify activity buttons as enabled - Start
                        //To enable or diable activity panel based on MODIFY_ACTIVITIES rights
                        if(functionType != TypeConstants.DISPLAY_MODE){
                            if(queryEngine == null){
                                queryEngine = queryEngine.getInstance();
                            }
                            Hashtable negotiationData = queryEngine.getDataCollection(queryKey);
                            CoeusVector cvRights = (CoeusVector)negotiationData.get("MODIFY_ACTIVITIES");
                            if(cvRights != null && cvRights.size()>0){
                                Boolean modifyActivity = (Boolean)cvRights.get(0);
                                if(modifyActivity != null){
                                    boolean hasModifyActivityRight = modifyActivity.booleanValue();
                                    if(!hasModifyActivityRight){
                                        functionType = TypeConstants.DISPLAY_MODE;
                                    }
                                }
                            }
                        }
                        //COEUSDEV-286
                     
                        showActivities(functionType);
                        blockEvents(false);
                    }
                }
                //To avoid deselection of the selected panel
                if( selectedRow == ActivityDetailsPanel.selectedRow ) return ;

                //Reset the color and icon for the previously selected panel
                ActivityDetailsPanel activityDetailsPanel = (ActivityDetailsPanel)negotiationDetailForm.pnlActivities.getComponent(selectedRow);
                activityDetailsPanel.scrPnDescription.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
                activityDetailsPanel.txtArDescription.setForeground(Color.BLACK);
                activityDetailsPanel.lblIcon.setIcon(null);            
                selectedRow = ActivityDetailsPanel.selectedRow;

            }
            //case 3590 start
            else if( mouseEvent.getSource() == negotiationDetailForm.txtPrimeSponsor &&
            mouseEvent.getClickCount() == 2 ){
                String sponsorCode = negotiationDetailForm.txtPrimeSponsor.getText();
                String sponsorDesc = validateSponsorCode(sponsorCode);
                if (sponsorDesc == null ) {
                    CoeusOptionPane.showInfoDialog(
                        coeusMessageResources.parseMessageKey(INVALID_SPONSOR_CODE));
                }else{
                    SponsorMaintenanceForm sponsorForm = new SponsorMaintenanceForm(TypeConstants.DISPLAY_MODE, sponsorCode);
                    sponsorForm.showForm(mdiForm, DISPLAY_SPONSOR, true);
                }
            }
           //case 3590 end 
        }catch (CoeusClientException coeusClientException){
            CoeusOptionPane.showDialog(coeusClientException);
        }
    }
    
    public void mouseEntered(java.awt.event.MouseEvent e) {
    }
    
    public void mouseExited(java.awt.event.MouseEvent e) {
    }
    
    public void mousePressed(java.awt.event.MouseEvent e) {
    }
    
    public void mouseReleased(java.awt.event.MouseEvent e) {
    }
    
    /** To check if the sponsor code is valid
     */
    private String validateSponsorCode(String sponsorCode) throws CoeusClientException{
        String sponsorDesc = null;
        RequesterBean request = new RequesterBean();
        request.setDataObject(GET_SPONSOR_INFO);
        request.setId(sponsorCode);
        AppletServletCommunicator comm = new AppletServletCommunicator(
            FUNCTION_SERVLET_URL, request);
        comm.send();
        ResponderBean response = comm.getResponse();
        if (response == null) {
            response = new ResponderBean();
            response.setResponseStatus(false);
        }
        if (response.isSuccessfulResponse()) {
            SponsorMaintenanceFormBean sponsorInfo =
            (SponsorMaintenanceFormBean) response.getDataObject();
            if(sponsorInfo != null && sponsorInfo.getName() != null){
                sponsorDesc = sponsorInfo.getName();
            }
        }else{
            throw new CoeusClientException(response.getMessage(),CoeusClientException.ERROR_MESSAGE);
        }
        return sponsorDesc;
    }
    
    /** This method triggers all actions based on the event occured
     * @param actionEvent takes the actionEvent */
    public void actionPerformed(java.awt.event.ActionEvent actionEvent) {
        Object source = actionEvent.getSource();
        try{
            blockEvents(true);
            if( source.equals( negotiationDetailForm.btnNew )){
                showActivities(TypeConstants.ADD_MODE);
            }else if( source.equals(negotiationDetailForm.btnModify )){
                showActivities(TypeConstants.MODIFY_MODE);
            }else if( source.equals(negotiationDetailForm.btnSelPerson )){
                showPersonSearch();
            }else if( source.equals(negotiationDetailForm.btnSelFile )){
                showFileChooser();
            //case 1735 start
            }else if( source.equals(negotiationDetailForm.btnPrint )){
                createReport(PRINT_ONE_ACTIVITY);
            }else if( source.equals(negotiationDetailForm.btnPrintAll )){
                createReport(PRINT_ALL_ACTIVITY);
            //case 1735 end
            //case 3590 start
            }else if( source.equals(negotiationDetailForm.btnSelSponsor)) {
                showSponsorSearch();
//            }else if(source.equals(negotiationDetailForm.txtPrimeSponsor)){
//                checkSponsor();    
            }else if( source.equals(negotiationDetailForm.btnLocation )){
                showLocationHistory();    
            //case 3590 end
            }else{
                CoeusOptionPane.showInfoDialog(
                    coeusMessageResources.parseMessageKey(FUNCTIONALITY_NOT_IMPL));
            }
           }catch (Exception ex) {
               ex.printStackTrace();
               
           }finally{
               blockEvents(false);
           }
    }
    
    /** Display the file chooser to select the file
     */
    private void showFileChooser(){
        CoeusFileChooser fileChooser = new CoeusFileChooser(mdiForm);
       // fileChooser.setSelectedFileExtension(fileExtension);
        // Added by chandra to Fix Bug #1200 - 14th Sept 2004 - start
       // fileChooser.setAcceptAllFileFilterUsed(true);
        // Added by chandra to Fix Bug #1200 - 14th Sept 2004 - end
        
        /** Added by chandra to select the entire directory insted of
         *the selected file. This change is done according to new
         *requirement.
         *Bug Fix #1090 - Start - 28th Sept 2004
         */
        String fileName = negotiationInfoBean.getDocFileAddress();
        // Select the file which has to be renamed 
        if(fileName!= null && (!fileName.trim().equals(EMPTY))){
            File selFile = new File(fileName);
            fileChooser.setDirectory(selFile);
        }
        fileChooser.showDirectory();
        // End Chandra for bug fix #1090 - 28th Sept 2004
        if(fileChooser.isFileSelected()){
            String file = fileChooser.getSelectedFile();
            negotiationDetailForm.txtDocFolder.setText(""+file);
            negotiationInfoBean.setDocFileAddress(""+file);
            if( negotiationInfoBean.getAcType() == null ){
                negotiationInfoBean.setAcType(TypeConstants.UPDATE_RECORD);
            }
            try{
                queryEngine.update(queryKey, negotiationInfoBean);
            }catch (CoeusException coeusException){
                coeusException.printStackTrace();
            }
        }
    }
    
    /** Displays the Activities screen
     * @param functionType the mode to open the screen
     */
    private void showActivities(char functionType){
        try{
        NegotiationActivitiesBean negotiationActivitiesBean = null;
        if( functionType != TypeConstants.ADD_MODE ){
            if( cvActivities != null && cvActivities.size() > 0 ){
                negotiationActivitiesBean = (NegotiationActivitiesBean)cvActivities.get(selectedRow);
            }else {
                //No activities to modify, do nothing
                return ;
            }
            if( cvUserRights != null && cvUserRights.size() > 0 ){
                boolean hasOspRight = ((Boolean)cvUserRights.get(HAS_OSP_RIGHT)).booleanValue();
                if( functionType == TypeConstants.MODIFY_MODE && !hasOspRight ){
                    if( negotiationActivitiesBean.getUpdateUser() != null &&
                    !(negotiationActivitiesBean.getUpdateUser().equalsIgnoreCase(mdiForm.getUserName())) ){
                        functionType = TypeConstants.DISPLAY_MODE;
                    }
                }
            }
        }else {
            negotiationBaseBean = negotiationInfoBean;
        }

        NegotiationActivityController negotiationActivityController = 
            new NegotiationActivityController(negotiationBaseBean, functionType);
        //Added for case 2806 - Upload attachments to neg module - Start
        negotiationActivityController.setParentFunctionType(getFunctionType());
        //Added for case 2806 - Upload attachments to neg module - End
        negotiationActivityController.setFormData(negotiationActivitiesBean);
        negotiationActivityController.display();
        negotiationActivityController.cleanUp();
        }catch(Exception exception){
            CoeusOptionPane.showErrorDialog(exception.getMessage());
        }
    }
    
    //start case 1735    
    private void createReport(String printType) {
        BeanEvent beanEvent = new BeanEvent();
        beanEvent.setBean(negotiationInfoBean);
        beanEvent.setSource(this);
        Hashtable htPrintParams = new Hashtable();         
        htPrintParams.put("NEGOTIATION_NUM", negotiationInfoBean.getNegotiationNumber());
        htPrintParams.put("PRINT_TYPE",printType);
        if (printType.equals(PRINT_ONE_ACTIVITY)){
            //Case 4185: Printing Errors with negotiations
            if(selectedRow==-1 || cvActivities== null || cvActivities.isEmpty() ){
                CoeusOptionPane.showErrorDialog(coeusMessageResources.parseMessageKey(ERRKEY_SELECT_ACTIVITY_TO_PRINT));
                return;
            }else{
                NegotiationActivitiesBean negotiationActivitiesBean = (NegotiationActivitiesBean)cvActivities.get(selectedRow);
                htPrintParams.put("ACTIVITY_NUM",""+negotiationActivitiesBean.getActivityNumber());
            }
            //4185 End
        }
        beanEvent.setObject(htPrintParams);
        fireBeanUpdated(beanEvent);
    }
    /** 
     * Print Negotiation Activity Report
     * @param htPrintParams 
     */

    public void printActivity(Hashtable htPrintParams)throws CoeusException{       
        RequesterBean requester = new RequesterBean();
        requester.setFunctionType(PRINT_NEGOTIATION_ACTIVITY);
        htPrintParams.put("USER_ID", mdiForm.getUserId());
        requester.setDataObject(htPrintParams);
        
        //For Streaming
        String printType = (String)htPrintParams.get("PRINT_TYPE");
        if (printType.equalsIgnoreCase("printNegotiation")) {
             requester.setId("Negotiation/ActivityPrintAll");
        }else{
           requester.setId("Negotiation/Activity");
        }            
        requester.setFunctionType('R');
        //For Streaming
        
        AppletServletCommunicator comm
         = new AppletServletCommunicator(connect, requester);
         
        comm.send();
        ResponderBean responder = comm.getResponse();
        String fileName = "";
        if(responder.isSuccessfulResponse()){
//             AppletContext coeusContxt = CoeusGuiConstants.getMDIForm().getCoeusAppletContext();
//             
//           
             fileName = (String)responder.getDataObject();
//             System.out.println("Report Filename is=>"+fileName);
//             
//             fileName.replace('\\', '/') ; // this is fix for Mac
//             URL reportUrl = null;
//             try{
//                reportUrl = new URL( CoeusGuiConstants.CONNECTION_URL + fileName );
//             
//             
//             if (coeusContxt != null) {
//                 coeusContxt.showDocument( reportUrl, "_blank" );
//             }else {
//                 javax.jnlp.BasicService bs = (javax.jnlp.BasicService)javax.jnlp.ServiceManager.lookup("javax.jnlp.BasicService");
//                 bs.showDocument( reportUrl );
//             }
//             }catch(MalformedURLException muEx){
//                 throw new CoeusException(muEx.getMessage());
//             }catch(Exception uaEx){
//                 throw new CoeusException(uaEx.getMessage());
//             }
             try{
                 URL url = new URL(fileName);
                 URLOpener.openUrl(url);
             }catch (MalformedURLException malformedURLException) {
                 throw new CoeusException(malformedURLException.getMessage());
             }
         }else{
             throw new CoeusException(responder.getMessage());
         }               
    }
    //end case 1735 
    
    /**
     * Setter for property cvUserRights.
     * @param cvUserRights New value of property cvUserRights.
     */
    public void setUserRights(edu.mit.coeus.utils.CoeusVector cvUserRights) {
        this.cvUserRights = cvUserRights;
    }
    
    /** Listens to bean updated event.
     * @param beanEvent beanEvent
     */    
    public void beanUpdated(edu.mit.coeus.gui.event.BeanEvent beanEvent) {
        if( beanEvent.getBean().getClass().equals(NegotiationActivitiesBean.class )) {
            setRefreshRequired(true);
            setNegotiationActivities(hasAnyOspRight);
        }else if( beanEvent.getBean().getClass().equals(NegotiationHeaderBean.class)) {
            setRefreshRequired(true);
            setNegotiationData();
        }
    }
    
    /** Checks if the negotiation data in the sheet is modified
     * @return true if data is modified, false otherwise
     */
    public boolean isDataModified(){
        if( qryNegotiationInfoBean == null && getFunctionType() == TypeConstants.ADD_MODE ){
            try{
                CoeusVector cvData = queryEngine.getDetails(queryKey, NegotiationInfoBean.class);
                qryNegotiationInfoBean = (NegotiationInfoBean)cvData.get(0);
                if( qryNegotiationInfoBean.getDocFileAddress() == null ){
                    qryNegotiationInfoBean.setDocFileAddress(EMPTY);
                } 
                //case 3590 start
                if( qryNegotiationInfoBean.getPrimeSponsorCode() == null ){
                    qryNegotiationInfoBean.setPrimeSponsorCode(EMPTY);
                } 
                //case 3590 end
                
            }catch (CoeusException coeusException ){
                coeusException.printStackTrace();
            }
        }
        if( qryNegotiationInfoBean == null || negotiationInfoBean == null ){
            return false;
        }
        
        //To avoid null pointer since this is called to check if data is modified 
        //while closing before validating the form 
        if ( qryNegotiationInfoBean.getNegotiatorId() == null ){
            qryNegotiationInfoBean.setNegotiatorId(EMPTY);
        }
        
        //case 3590 start       
        if( qryNegotiationInfoBean.getPrimeSponsorCode() == null ){
            qryNegotiationInfoBean.setPrimeSponsorCode(EMPTY);
        } 
       
        try{
            String strDate;
            strDate = negotiationDetailForm.txtProposedStartDate.getText().trim();
            if (!strDate.equals(EMPTY)){
                if (qryNegotiationInfoBean.getProposedStartDate() == null ) return true;
                java.util.Date date = (java.util.Date)simpleDateFormat.parse(dateUtils.restoreDate(strDate,DATE_SEPARATERS));
                if (!qryNegotiationInfoBean.getProposedStartDate().equals(new java.sql.Date(date.getTime()))) return true;
            }else {
                if (qryNegotiationInfoBean.getProposedStartDate() != null) return true;
            }    
            //Added for case 4185 - Two editable fields - start date and closed date -Start
            String strStartDate;
            strStartDate = negotiationDetailForm.txtStartDate.getText().trim();
            if (!strStartDate.equals(EMPTY)){
                if (qryNegotiationInfoBean.getStartDate() == null ) return true;
                Date date = (java.util.Date)simpleDateFormat.parse(dateUtils.restoreDate(strStartDate,DATE_SEPARATERS));
                if (!qryNegotiationInfoBean.getStartDate().equals(new java.sql.Date(date.getTime()))) return true;
            }else {
                if (qryNegotiationInfoBean.getStartDate() != null) return true;
            }
            
            String strClosedDate;
            strClosedDate = negotiationDetailForm.txtClosedDate.getText().trim();
            if (!strClosedDate.equals(EMPTY)){
                if (qryNegotiationInfoBean.getClosedDate() == null ) return true;
                Date date = (java.util.Date)simpleDateFormat.parse(dateUtils.restoreDate(strClosedDate,DATE_SEPARATERS));
                if (!qryNegotiationInfoBean.getClosedDate().equals(new java.sql.Date(date.getTime()))) return true;
            }else {
                if (qryNegotiationInfoBean.getClosedDate() != null) return true;
            }
            //Added for case 4185 - Two editable fields - start date and closed date -End
        }catch(ParseException parseException){
            parseException.printStackTrace();
        }
        if ( isLocationDataChanged()) return true;
        
//        if( qryNegotiationInfoBean.getStatusCode() != negotiationInfoBean.getStatusCode() ||
        //case 3961 start 
        if (linkedToInstituteProposal){
            if( qryNegotiationInfoBean.getStatusCode() != negotiationInfoBean.getStatusCode() ||
                qryNegotiationInfoBean.getNegotiationAgreeTypeCode()!= negotiationInfoBean.getNegotiationAgreeTypeCode() ||
                !qryNegotiationInfoBean.getNegotiatorId().equals(negotiationInfoBean.getNegotiatorId()) ||
                !(qryNegotiationInfoBean.getDocFileAddress().trim()).equals(negotiationDetailForm.txtDocFolder.getText().trim())){
              return true;
            }else {
              return false;
            }
        }else {
        //case 3961 end 
            if( qryNegotiationInfoBean.getStatusCode() != negotiationInfoBean.getStatusCode() ||
                qryNegotiationInfoBean.getNegotiationAgreeTypeCode()!= negotiationInfoBean.getNegotiationAgreeTypeCode() ||            
                !qryNegotiationInfoBean.getPrimeSponsorCode().equals(negotiationDetailForm.txtPrimeSponsor.getText().trim()) ||
                //case 3590 end
                !qryNegotiationInfoBean.getNegotiatorId().equals(negotiationInfoBean.getNegotiatorId()) ||
                !qryNegotiationInfoBean.getDocFileAddress().equals(negotiationDetailForm.txtDocFolder.getText().trim())){
              return true;
            }else {
              return false;
            }
        }
        
    }
    
   
    //case 3590 start
    public void focusGained(FocusEvent focusEvent) {
        if(focusEvent.isTemporary()) return ;
        
        Object source = focusEvent.getSource();
        //Added for case 4185 - Start Date and Closed Date - Start
         if(source.equals(negotiationDetailForm.txtStartDate)) {
            String strStartDate = negotiationDetailForm.txtStartDate.getText();
            prevStartDate = strStartDate;
            strStartDate = dateUtils.restoreDate(strStartDate, DATE_SEPARATERS);
            negotiationDetailForm.txtStartDate.setText(strStartDate);
            
        } else if(source.equals(negotiationDetailForm.txtClosedDate)) {
            String strClosedDate = negotiationDetailForm.txtClosedDate.getText();
            prevClosedDate = strClosedDate;
            strClosedDate = dateUtils.restoreDate(strClosedDate, DATE_SEPARATERS);
            negotiationDetailForm.txtClosedDate.setText(strClosedDate);
        }
        //Added for case 4185 - Start Date and Closed Date - End
        else if(source.equals(negotiationDetailForm.txtProposedStartDate)) {
            String proposedStrtDate = negotiationDetailForm.txtProposedStartDate.getText();
            proposedStrtDate = dateUtils.restoreDate(proposedStrtDate, DATE_SEPARATERS);
            negotiationDetailForm.txtProposedStartDate.setText(proposedStrtDate);
        }
        else if(source.equals(negotiationDetailForm.txtEffectiveDate)) {
            String effectiveDate = negotiationDetailForm.txtEffectiveDate.getText();
            effectiveDate = dateUtils.restoreDate(effectiveDate, DATE_SEPARATERS);
            negotiationDetailForm.txtEffectiveDate.setText(effectiveDate);
        }
        
    }
    
    public void focusLost(FocusEvent focusEvent) {
        if(focusEvent.isTemporary())   return ;     
        Object source = focusEvent.getSource();
        //Added for case 4185 - Start Date and Closed Date - Start
        if(source.equals(negotiationDetailForm.txtStartDate)){
            String strStartDate;
            strStartDate = negotiationDetailForm.txtStartDate.getText().trim();
            if( strStartDate.equals(EMPTY) ) return;
            String startDate = dateUtils.formatDate(strStartDate,DATE_SEPARATERS, REQUIRED_FORMAT);
            if(startDate == null){
                startDate = dateUtils.restoreDate(strStartDate, DATE_SEPARATERS);
                if (startDate == null || startDate.equals(strStartDate)) {
                    CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(ERRKEY_INVALID_START_DATE));
                    negotiationDetailForm.txtStartDate.setText(prevStartDate);
                    setRequestFocusInThread(negotiationDetailForm.txtStartDate);
                    return;
                }
            }else{
                negotiationDetailForm.txtStartDate.setText(startDate);
            }
            //validate start date - closed date range
            validateStartClosedDateRange(negotiationDetailForm.txtStartDate);
        }else if(source.equals(negotiationDetailForm.txtClosedDate)){
            String strClosedDate;
            strClosedDate = negotiationDetailForm.txtClosedDate.getText().trim();
            if( strClosedDate.equals(EMPTY) ) return;
            String closedDate = dateUtils.formatDate(strClosedDate,DATE_SEPARATERS, REQUIRED_FORMAT);
            if(closedDate == null){
                closedDate = dateUtils.restoreDate(strClosedDate, DATE_SEPARATERS);
                if (closedDate == null || closedDate.equals(strClosedDate)) {
                    CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(ERRKEY_INVALID_CLOSED_DATE));
                    negotiationDetailForm.txtClosedDate.setText(prevClosedDate);
                    setRequestFocusInThread(negotiationDetailForm.txtClosedDate);
                    return;
                }
            }else{
                negotiationDetailForm.txtClosedDate.setText(closedDate);
            }
            //validate start date - closed date range
            validateStartClosedDateRange(negotiationDetailForm.txtClosedDate);
        }
        //Added for case 4185 - Start Date and Closed Date - End
        else if(source.equals(negotiationDetailForm.txtProposedStartDate)){
            String pdsDate;
            pdsDate = negotiationDetailForm.txtProposedStartDate.getText().trim();
            if( pdsDate.equals(EMPTY) ) return;
            String proposedStrtDate = dateUtils.formatDate(pdsDate,DATE_SEPARATERS, REQUIRED_FORMAT);
            if(proposedStrtDate == null){
                proposedStrtDate = dateUtils.restoreDate(pdsDate, DATE_SEPARATERS);
                if (proposedStrtDate == null || proposedStrtDate.equals(pdsDate)) {
                    CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(VALID_PROPOSED_START_DATE));
                    negotiationDetailForm.txtProposedStartDate.setText("");
                    setRequestFocusInThread(negotiationDetailForm.txtProposedStartDate);
                }
            }else{
                negotiationDetailForm.txtProposedStartDate.setText(proposedStrtDate);
            }
        }else if(source.equals(negotiationDetailForm.txtEffectiveDate)){
            String effDate;
            effDate = negotiationDetailForm.txtEffectiveDate.getText().trim();
            if( effDate.equals(EMPTY)) {
                negotiationDetailForm.txtNumOfDays.setText("");
                return;
            }
            String effectiveDate = dateUtils.formatDate(effDate,DATE_SEPARATERS,REQUIRED_FORMAT);
            if(effectiveDate == null){
                effectiveDate =  dateUtils.restoreDate(effDate, DATE_SEPARATERS);
                if (effectiveDate == null || effectiveDate.equals(effDate)){
                    CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(VALID_EFFECTIVE_DATE));
                    negotiationDetailForm.txtEffectiveDate.setText("");
                    negotiationDetailForm.txtNumOfDays.setText("");
                    setRequestFocusInThread(negotiationDetailForm.txtEffectiveDate);
                }
            }else{  
                if (!effDate.equals(EMPTY)){
                    try{
                        String strDate = dateUtils.formatDate(effDate,DATE_SEPARATERS, REQUIRED_FORMAT);
                        strDate = dateUtils.restoreDate(strDate,DATE_SEPARATERS);
                        java.util.Date date = (java.util.Date)simpleDateFormat.parse(strDate);
                        if (qryNegotiationLocationBean != null){
                            if (negotiationLocationBean.getNegotiationLocationTypeCode() != qryNegotiationLocationBean.getNegotiationLocationTypeCode()){
                                if (currentEffDate != null && currentEffDate.after(date)){
                                    SimpleDateFormat dateFormat = new SimpleDateFormat(REQUIRED_FORMAT);
                                    CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(RIGHT__EFFECTIVE_DATE) + " " + dateFormat.format(currentEffDate));
                                    negotiationDetailForm.txtEffectiveDate.setText("");
                                    negotiationDetailForm.txtNumOfDays.setText("");
                                    setRequestFocusInThread(negotiationDetailForm.txtEffectiveDate);
                                    return;
                                }
                            }else if(prevEffDate != null&& prevEffDate.after(date)){
                                SimpleDateFormat dateFormat = new SimpleDateFormat(REQUIRED_FORMAT);
                                CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(RIGHT__EFFECTIVE_DATE) + " " + dateFormat.format(prevEffDate));
                                negotiationDetailForm.txtEffectiveDate.setText("");
                                negotiationDetailForm.txtNumOfDays.setText("");
                                setRequestFocusInThread(negotiationDetailForm.txtEffectiveDate);
                                return;                                
                            }
                        }                       

                        if (date.after(date_today)){
                            //set blank
                            negotiationDetailForm.txtEffectiveDate.setText(effectiveDate);
                            negotiationDetailForm.txtNumOfDays.setText("");
                        }else{
                            //calc number of days
                            negotiationDetailForm.txtEffectiveDate.setText(effectiveDate);
                            int numOfdays = dateUtils.dateDifference(date_today,date);
                            negotiationDetailForm.txtNumOfDays.setText(numOfdays +"");
//                            if (numOfdays == 0)
//                                negotiationDetailForm.txtNumOfDays.setText("");
//                            else
//                                negotiationDetailForm.txtNumOfDays.setText(numOfdays +"");
                        }
                    } catch(ParseException parseException){
                        parseException.printStackTrace();
                    }
                }//else negotiationDetailForm.txtNumOfDays.setText("");            
        }
      }else if(source.equals(negotiationDetailForm.txtPrimeSponsor)){
            checkSponsor();
      }
    }
    
    public boolean isLocationDataChanged(){  
            if (qryNegotiationLocationBean == null){
                if (negotiationLocationBean != null && 
                        (negotiationLocationBean.getNegotiationLocationTypeCode() != 0 ||
                            !negotiationDetailForm.txtEffectiveDate.getText().trim().equals(EMPTY))){
                    locationTypeChange = true;
                    return true;
                }
            }else {
                if ( negotiationLocationBean.getNegotiationLocationTypeCode() != qryNegotiationLocationBean.getNegotiationLocationTypeCode()){
                       locationTypeChange = true;
                       return true;
                }
                try{ 
                   String strDate = negotiationDetailForm.txtEffectiveDate.getText().trim();
                   if (!strDate.equals(EMPTY)){
                        if (qryNegotiationLocationBean.getEffectiveDate() == null ){
                            locationChange = true;
                            return true;
                        }                       
                        java.util.Date date = (java.util.Date)simpleDateFormat.parse(dateUtils.restoreDate(strDate,DATE_SEPARATERS));
                        if (!qryNegotiationLocationBean.getEffectiveDate().equals(new java.sql.Date(date.getTime()))){
                            locationChange = true;
                            return true;
                        }
                        
                   }else {
                        if (qryNegotiationLocationBean.getEffectiveDate() != null){
                            locationChange = true;
                            return true;
                        }
                   }
                }catch(ParseException parseException){
                    parseException.printStackTrace();
                } 
            }
            locationTypeChange = false;
            locationChange = false;
            return false;   
    }
    
    public void setLocationBeanChanged(){
        negotiationLocationBean = new NegotiationLocationBean();
        negotiationLocationBean.setAcType(TypeConstants.INSERT_RECORD);
    }
    
     public void showSponsorSearch(){
         try{
             CoeusSearch sponsorSearch = new CoeusSearch(CoeusGuiConstants.getMDIForm(), SPONSOR_SEARCH, CoeusSearch.TWO_TABS);
             sponsorSearch.showSearchWindow();
             HashMap hmSelectedRow = sponsorSearch.getSelectedRow();
             if (hmSelectedRow != null){
                String sponsorCode = hmSelectedRow.get("SPONSOR_CODE").toString();
                String sponsorName = hmSelectedRow.get("SPONSOR_NAME").toString();
                negotiationDetailForm.txtPrimeSponsor.setText(sponsorCode);
                negotiationDetailForm.lblPrimeSponsorName.setText(sponsorName);
             }
          }catch (Exception exception){
            exception.printStackTrace();
        }
    }
    
    private void showLocationHistory(){
        if (negotiationLocationBean != null && negotiationLocationBean.getLocationNumber() > 0 ){
               NegotiationLocationHistoryController negoLocHisController  = 
                       new NegotiationLocationHistoryController(negotiationLocationBean);
               //Added for case 4185- Effective date should be editable - Start
               negoLocHisController.setFunctionType(getFunctionType());
               negoLocHisController.registerObserver(this);
               //Added for case 4185- Effective date should be editable - End
               negoLocHisController.display();
        }else{
               CoeusOptionPane.showInfoDialog(
                       coeusMessageResources.parseMessageKey(NO_LOCATION_INFORMATION));
        }
    }
       
    public void checkSponsor(){
        String sponsorCode;
        sponsorCode = negotiationDetailForm.txtPrimeSponsor.getText().trim();
        if(sponsorCode.trim().equals(EMPTY)){
            negotiationDetailForm.lblPrimeSponsorName.setText("");
            return;
        }else{
            try{
                String sponsorDesc = validateSponsorCode(sponsorCode);
                if (sponsorDesc == null ) {
                    CoeusOptionPane.showInfoDialog(
                        coeusMessageResources.parseMessageKey(INVALID_SPONSOR_CODE));
                    negotiationDetailForm.lblPrimeSponsorName.setText("");
                    negotiationDetailForm.txtPrimeSponsor.setText("");
                }else    
                    negotiationDetailForm.lblPrimeSponsorName.setText(sponsorDesc);                
         }catch (CoeusClientException coeusClientException){
            CoeusOptionPane.showDialog(coeusClientException);
         }
      
      }
    }  

    public Object getLocationHistoryData() {
        RequesterBean requesterBean = new RequesterBean();
        requesterBean.setFunctionType(GET_NEGOTIATION_LOCATION_HISTORY);
        //case 4140 start
//        requesterBean.setDataObject(negotiationBaseBean.getNegotiationNumber())
        requesterBean.setDataObject(negotiationInfoBean.getNegotiationNumber());
        //case 4140 end
        
        AppletServletCommunicator appletServletCommunicator = new AppletServletCommunicator(NEGO_SERVLET_URL, requesterBean);
        appletServletCommunicator.setRequest(requesterBean);
        
        appletServletCommunicator.send();
        ResponderBean responderBean = appletServletCommunicator.getResponse();
        
        if(responderBean == null) {
            //Could not contact server.
            CoeusOptionPane.showErrorDialog(COULD_NOT_CONTACT_SERVER);
            return null;
        }
        return responderBean.getDataObjects();
    }
    
    //case 3590 end
    
    //cae 3961 start
public void checkNegotiationPrimaeSponsor() {
//        check negotiation is linked to institute prop.
     try{
        if (isNegotiationLinkedToIP(negotiationInfoBean.getNegotiationNumber())){
            linkedToInstituteProposal = true;
            negotiationDetailForm.txtPrimeSponsor.setEnabled(false);
            negotiationDetailForm.btnSelSponsor.setEnabled(false);
            negotiationDetailForm.txtPrimeSponsor.setBackground(new Color(212,208,200));
        }else {
            linkedToInstituteProposal = false;
//            if( getFunctionType() == TypeConstants.DISPLAY_MODE ){
//                negotiationDetailForm.txtPrimeSponsor.setEnabled(true);
//                negotiationDetailForm.btnSelSponsor.setEnabled(true);
//            }
        }
     }catch (CoeusClientException coeusClientException){
        CoeusOptionPane.showDialog(coeusClientException);
     }
  
}
    
    private boolean isNegotiationLinkedToIP(String proposalNumber) 
        throws CoeusClientException {
        RequesterBean requester = new RequesterBean();
        requester.setFunctionType(INST_PROP_COUNT);
        requester.setDataObject(proposalNumber);
        AppletServletCommunicator comm = new AppletServletCommunicator(NEGO_SERVLET_URL, requester);
        comm.send();
        ResponderBean response = comm.getResponse();
        if(response.isSuccessfulResponse()){
            return ((Boolean)response.getDataObject()).booleanValue();
        }else {
            throw new CoeusClientException(response.getMessage());
        }
    }

    
    //case 3961 end
    
    /** Listens to item state changed event.
     * @param itemEvent itemEvent
     */    
    public void itemStateChanged(ItemEvent itemEvent) {
        if(itemEvent.getStateChange() == itemEvent.DESELECTED) return ;
        
        //case 3590 start
//        ComboBoxBean comboBoxBean = (ComboBoxBean)negotiationDetailForm.cmbStatus.getSelectedItem();
//        if(! comboBoxBean.getCode().equals(EMPTY)) {
//            int code = Integer.parseInt(comboBoxBean.getCode());
//            negotiationInfoBean.setStatusCode(code);
//            if( negotiationInfoBean.getAcType() == null ){
//                negotiationInfoBean.setAcType(TypeConstants.UPDATE_RECORD);
//            }
//        }
        if(itemEvent.getSource().equals(negotiationDetailForm.cmbStatus)) {
            ComboBoxBean comboBoxBean = (ComboBoxBean)negotiationDetailForm.cmbStatus.getSelectedItem();
            if(! comboBoxBean.getCode().equals(EMPTY)) {
                int code = Integer.parseInt(comboBoxBean.getCode());
                negotiationInfoBean.setStatusCode(code);
                if( negotiationInfoBean.getAcType() == null ){
                    negotiationInfoBean.setAcType(TypeConstants.UPDATE_RECORD);
                }
            } 
        }else if(itemEvent.getSource().equals(negotiationDetailForm.cmbAgreement)){
            ComboBoxBean comboBoxBean = (ComboBoxBean)negotiationDetailForm.cmbAgreement.getSelectedItem();
            if(! comboBoxBean.getCode().equals(EMPTY)) {
                int code = Integer.parseInt(comboBoxBean.getCode());
                negotiationInfoBean.setNegotiationAgreeTypeCode(code);
                if( negotiationInfoBean.getAcType() == null ){
                    negotiationInfoBean.setAcType(TypeConstants.UPDATE_RECORD);
                }
            } 
        }else if(itemEvent.getSource().equals(negotiationDetailForm.cmbLocation)){
            ComboBoxBean comboBoxBean = (ComboBoxBean)negotiationDetailForm.cmbLocation.getSelectedItem();
            if(! comboBoxBean.getCode().equals(EMPTY)) {
                int code = Integer.parseInt(comboBoxBean.getCode());
                if (negotiationLocationBean == null ) setLocationBeanChanged();
                negotiationLocationBean.SetNegotiationLocationTypeCode(code);
                //Code Commented with case 4376:Location date field should not be autofilled
//                if (negotiationDetailForm.txtEffectiveDate.getText().trim() == null ||
//                        negotiationDetailForm.txtEffectiveDate.getText().trim().equals(EMPTY)) {
//                    SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat(REQUIRED_FORMAT);
//                    negotiationDetailForm.txtEffectiveDate.setText(simpleDateFormat2.format(date_today));
//                    negotiationDetailForm.txtNumOfDays.setText("");
//                }
                if(isLocationDataChanged() && locationTypeChange){
                    negotiationDetailForm.txtEffectiveDate.setText(EMPTY);
                    negotiationDetailForm.txtNumOfDays.setText(EMPTY);
                }
                //Case 4376:End
            } 
        }
        //case 3590 end
        
        
        
    }
    
    /** Refreshes the GUI controlled by this. */    
    public void refresh() {
        if(isRefreshRequired()) {
            setFormData(negotiationBaseBean);
            setRefreshRequired(false);
            //case 3590 start
            locationChange = false;
            locationTypeChange = false;
            //case 3590 end
        }
    }
    
    /**
     * Setter for property negotiationInfoBean.
     * @param negotiationInfoBean New value of property negotiationInfoBean.
     */
    public void setNegotiationInfoBean(edu.mit.coeus.negotiation.bean.NegotiationInfoBean negotiationInfoBean) {
        this.negotiationInfoBean = negotiationInfoBean;        
        //case 3961 start
        checkNegotiationPrimaeSponsor();
        //case 3961 end
    }
    
    /** To clean all objects
     */
    public void cleanUp(){        
        cvActivities = null;
        cvActivitiesPanels = null;
        cvUserRights = null;
        mdiForm = null;
        //start case 1735
        removeBeanUpdatedListener(this, NegotiationInfoBean.class);
        //end case 1735
        removeBeanUpdatedListener(this, NegotiationHeaderBean.class);
        removeBeanUpdatedListener(this, NegotiationActivitiesBean.class);
        //case 3590 start
        removeBeanUpdatedListener(this, NegotiationLocationBean.class);
        //case 3590 end
             
    }
    
    /**
     * Setter for property cvMedusaRights.
     * @param cvMedusaRights New value of property cvMedusaRights.
     */
    public void setMedusaRights(edu.mit.coeus.utils.CoeusVector cvMedusaRights) {
        this.cvMedusaRights = cvMedusaRights;
    }
    
    /**
     * Setter for property cvInstitutePropRights.
     * @param IPRights New value of property cvInstitutePropRights.
     */
    public void setInstitutePropRights(edu.mit.coeus.utils.CoeusVector cvInstitutePropRights) {
        this.cvInstitutePropRights = cvInstitutePropRights;
    }
    //Added for case 4185- Effective date should be editable - Start
    public void update(Observable o, Object arg) {
        if(arg instanceof NegotiationLocationBean){
//            HashMap hmData = (HashMap)arg;
            NegotiationLocationBean negotiationLocBean = (NegotiationLocationBean)arg;
            if(negotiationLocBean!=null){
                try {
                    queryEngine.removeData(queryKey,negotiationLocationBean);
                    queryEngine.addData(queryKey, negotiationLocBean);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                setRefreshRequired(true);
                refresh();
            }
        }
    }
    
    //This method will validate the start date and end date range on tabbing out
    // of start date/end date.
    private void validateStartClosedDateRange(JTextField invokedField){
        
        String strPreviousDate = EMPTY;
        String strStartDate    = negotiationDetailForm.txtStartDate.getText().trim();
        String strEndDate      = negotiationDetailForm.txtClosedDate.getText().trim();
        
        if ( strStartDate.length() > 0  && strEndDate.length()>0) {
            
            Date startDate  = null;
            Date endDate    = null;
            
            try {
                String strDate1 =  dateUtils.formatDate(strStartDate, DATE_SEPARATERS, REQUIRED_FORMAT);
                try{
                    if(strDate1==null){
                        startDate = simpleDateFormat.parse(dateUtils.restoreDate(strStartDate,DATE_SEPARATERS));
                    }else{
                        startDate = simpleDateFormat.parse(dateUtils.formatDate(strStartDate,DATE_SEPARATERS, SIMPLE_DATE_FORMAT));
                    }
                }catch(java.text.ParseException pe){
                    strPreviousDate = prevStartDate;
                    throw new CoeusException(ERRKEY_INVALID_START_DATE);
                }
                
                String strDate11 =  dateUtils.formatDate(strEndDate, DATE_SEPARATERS, REQUIRED_FORMAT);
                try{
                    if(strDate11==null){
                        endDate = simpleDateFormat.parse(dateUtils.restoreDate(strEndDate,DATE_SEPARATERS));
                    }else{
                        endDate= simpleDateFormat.parse(dateUtils.formatDate(strEndDate,DATE_SEPARATERS, SIMPLE_DATE_FORMAT));
                    }
                }catch(java.text.ParseException pe){
                    strPreviousDate = prevClosedDate;
                    throw new CoeusException(ERRKEY_INVALID_CLOSED_DATE);
                }
                if(startDate !=null && startDate.compareTo(endDate)>0){
                    if(invokedField.equals(negotiationDetailForm.txtStartDate)){
                        strPreviousDate = prevStartDate;
                        throw new CoeusException(ERRKEY_INVALID_START_DT_RANGE);
                    }else if(invokedField.equals(negotiationDetailForm.txtClosedDate)){
                        strPreviousDate = prevClosedDate;
                        throw new CoeusException(ERRKEY_INVALID_CLOSED_DT_RANGE);
                    }
                }
            }catch(CoeusException e){
                CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(e.getUserMessage()));
                setRequestFocusInThread(invokedField);
                invokedField.setText(strPreviousDate);
            }
        }
    }
    //Added for case 4185- Effective date should be editable - End    
}
