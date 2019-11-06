/*
 * ProtocolStudyGroupsForm.java
 *
 * Created on March 18, 2010, 10:44 AM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved
 */
/* PMD check performed, and commented unused imports and variables on 09-MARCH-2011
 * by Md.Ehtesham Ansari
 */

package edu.mit.coeus.iacuc.gui;

import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.iacuc.bean.ProtocolInfoBean;
import edu.mit.coeus.iacuc.bean.ProtocolInvestigatorsBean;
import edu.mit.coeus.iacuc.bean.ProtocolKeyPersonnelBean;
import edu.mit.coeus.iacuc.bean.ProtocolPersonsResponsibleBean;
//import edu.mit.coeus.customelements.bean.CustomElementsInfoBean;
import edu.mit.coeus.iacuc.bean.ProtocolSpeciesBean;
import edu.mit.coeus.iacuc.bean.ProtocolStudyGroupBean;
import edu.mit.coeus.iacuc.bean.ProtocolStudyGroupLocationBean;
import edu.mit.coeus.search.gui.CoeusSearch;
import edu.mit.coeus.utils.customelements.CustomElementsForm;
import edu.mit.coeus.utils.query.Equals;
import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.*;
import javax.swing.table.*;
import javax.swing.event.*;
import java.util.*;
import edu.mit.coeus.gui.*;
import edu.mit.coeus.utils.*;
import edu.mit.coeus.exception.*;


/**
 * @author satheeshkumarkn
 */
class ProtocolStudyGroupsForm extends javax.swing.JComponent implements TypeConstants,
        ActionListener, ListSelectionListener, Observer {
    
    private char functionType,custFunctionType;
    private ListSelectionModel studyGroupSelectionModel;
    private boolean saveRequired;
    String connectTo = CoeusGuiConstants.CONNECTION_URL + "/IacucProtocolServlet";
    private CoeusMessageResources coeusMessageResources;
    private CoeusAppletMDIForm mdiForm;
    //Header columns for study group table - Start
    private static final int STUDY_GROUP_POINTER_COLUMN = 0;
    //Modified with CoeusQA-2551:Rework how user enters species,study groups and procedures in IACUC protocols
    private static final int SPECIES_GROUP_NAME_COLUMN = 1;
    private static final int SPECIES_COLUMN = 2;
    private static final int PROCEDURE_CATEGORY_COLUMN = 3;
    private static final int PROCEDURE_COLUMN = 4;
    private static final int PAIN_CATEGORY_COLUMN = 5;
    private static final int COUNT_COLUMN = 6;
    private static final int STUDY_GROUP_TABLE_ROW_HEIGHT = 22;
    //Width for study group pointer column
    private static final int STUDY_GROUP_POINTER_COLUMN_MAX_WIDTH = 30;
    private static final int STUDY_GROUP_POINTER_COLUMN_MIN_WIDTH = 30;
    private static final int STUDY_GROUP_POINTER_COLUMN_PREFERED_WIDTH = 30;
    //Width for study group name column:CoeusQA-2551
    private static final int SPECIES_GROUP_NAME_COLUMN_MAX_WIDTH = 300;
    private static final int SPECIES_GROUP_NAME_COLUMN_MIN_WIDTH = 150;
    private static final int SPECIES_GROUP_NAME_COLUMN_WIDTH = 172;
    //Width for study group table species column
    private static final int SPECIES_COLUMN_MAX_WIDTH = 300;
    private static final int SPECIES_COLUMN_MIN_WIDTH = 150;
    private static final int SPECIES_COLUMN_PREFERED_WIDTH = 200;
    //Width for study group table procedure category column
    private static final int PROCEDURE_CATEGORY_COLUMN_MAX_WIDTH = 150;
    private static final int PROCEDURE_CATEGORY_COLUMN_MIN_WIDTH = 150;
    private static final int PROCEDURE_CATEGORY_COLUMN_PREFERED_WIDTH = 150;
    //Width for study group table procedure column
    private static final int PROCEDURE_COLUMN_MAX_WIDTH = 110;
    private static final int PROCEDURE_COLUMN_MIN_WIDTH = 110;
    private static final int PROCEDURE_COLUMN_PREFERED_WIDTH = 110;
    //CoeusQA-2551:end
    //Width for study group table pain category column
    private static final int PAIN_CATEGORY_COLUMN_MAX_WIDTH = 100;
    private static final int PAIN_CATEGORY_COLUMN_MIN_WIDTH = 100;
    private static final int PAIN_CATEGORY_COLUMN_PREFERED_WIDTH = 100;
    //Width for study group table count column
    private static final int COUNT_COLUMN_MAX_WIDTH = 100;
    private static final int COUNT_COLUMN_MIN_WIDTH = 50;
    // Modified for COEUSQA-3708 : Coeus4.5: Increase To Viewable Count Field - Start
//    private static final int COUNT_COLUMN_PREFERED_WIDTH = 40;    
    private static final int COUNT_COLUMN_PREFERED_WIDTH = 60;
    // Modified for COEUSQA-3708 : Coeus4.5: Increase To Viewable Count Field - End
    //Header columns for study group table - End
    
    //Header columns for location table - Start
    private static final int LOCATION_POINTER_COLUMN = 0;
    private static final int LOCATION_TYPE_COLUMN = 1;
    private static final int LOCATION_NAME_COLUMN = 2;
    // Modified for COEUSQA-2625_Add another level of detail for Location in IACUC protocol_start
    private static final int LOCATION_ROOM_COLUMN = 3;
    private static final int DESCRIPTION_COLUMN = 4;
    // Modified for COEUSQA-2625_Add another level of detail for Location in IACUC protocol_end
    private static final int LOCATION_TABLE_ROW_HEIGHT = 22;
    
    //Width for location table pointer column
    private static final int LOCATION_POINTER_COLUMN_MAX_WIDTH = 30;
    private static final int LOCATION_POINTER_COLUMN_MIN_WIDTH = 30;
    private static final int LOCATION_POINTER_COLUMN_PREFERED_WIDTH = 30;
    //Width for location table location type column
    private static final int LOCATION_TYPE_COLUMN_MAX_WIDTH = 198;
    private static final int LOCATION_TYPE_COLUMN_MIN_WIDTH = 198;
    private static final int LOCATION_TYPE_COLUMN_PREFERED_WIDTH = 210;
    //Width for location table location name column
    private static final int LOCATION_NAME_COLUMN_MAX_WIDTH = 198;
    private static final int LOCATION_NAME_COLUMN_MIN_WIDTH = 198;
    private static final int LOCATION_NAME_COLUMN_PREFEREDN_WIDTH = 210;
    //Width for location table location name column
    private static final int DESCRIPTION_COLUMN_MAX_WIDTH = 198;
    private static final int DESCRIPTION_COLUMN_MIN_WIDTH = 198;
    private static final int DESCRIPTION_COLUMN_PREFERED_WIDTH = 363;
    // Added for COEUSQA-2625_Add another level of detail for Location in IACUC protocol _start
    private static final int LOCATION_ROOM_COLUMN_MAX_WIDTH = 198;
    private static final int LOCATION_ROOM_COLUMN_MIN_WIDTH = 198;
    private static final int LOCATION_ROOM_COLUMN_PREFERRED_WIDTH = 210;
    // Added for COEUSQA-2625_Add another level of detail for Location in IACUC protocol_end
    //Header columns for location table - End
    //Added for COEUSQA-2633 Ability to indicate which procedures IACUC study personnel will perform and training -Start
    private PersonResponsibleTableModel personResponsibleTableModel;    
    ProtocolPersonsResponsibleForm personsResponsibleForm;    
    private static final int PERSON_POINTER_COLUMN = 0;
    private static final int PERSON_NAME_COLUMN = 1;
    private static final int PERSON_TRAINING_COLUMN = 2;    
    private static final int PERSON_POINTER_COLUMN_WIDTH = 30;   
    private static final int PERSON_NAME_COLUMN_WIDTH = 700; 
    private static final int PERSON_TRAINING_COLUMN_WIDTH = 83;        
    private static final char GET_TRAINIG_INFO = 'a';    
    private static final String SPECIES_TYPE = "speciesObserver";
    private static final String PERSON_TYPE = "personObserver";
    private static final String SPECIES_OBSERVER = "edu.mit.coeus.iacuc.gui.ProtocolSpeciesForm";     
    //Added for COEUSQA-2633 Ability to indicate which procedures IACUC study personnel will perform and training -End
    private StudyGroupTableModel studyGroupTableModel;
    private LocationTableModel locationTableModel;
    private String protocolNumber;
    private int sequenceNumber;
    private static final int PROTOCOL_NUMBER_INDEX = 0;
    private static final int SEQUENCE_NUMBER_INDEX = 1;
    private static final int FUNCTION_TYPE_INDEX = 2;
    private static final char GET_STUDY_GROUP_DATA ='8';
    
    private static final int STUDY_GROUP_DATA_INDEX = 0;                
    private static final int PROC_CATEGORY_INDEX = 1;
    private static final int PROCEDURE_INDEX = 2;
    private static final int PAIN_CATEGORY_INDEX = 3;
    private static final int LOCATION_TYPE_INDEX = 4;
    private static final int LOCATION_NAME_INDEX = 5;
    private static final int PROCEDURE_CUSTOM_DATA_INDEX = 6;
    
    //Commented and Added for COEUSQA-3384 : Increase lengths for some IACUC fields - start
    //The Count field max char length on the studyGroups need to be increase for 5 digit long
    //private static final int COUNT_FIELD_CHAR_LENGTH = 3;
    private static final int COUNT_FIELD_CHAR_LENGTH = 8;
    //Commented and Added for COEUSQA-3384 : Increase lengths for some IACUC fields - end
    
//    private CoeusVector cvSpeciesType;
    private CoeusVector cvProcCategoryType;
    private HashMap hmProcedureForCat;
    private HashMap hmCustomDataForCat;
    private CoeusVector cvPainCategoryType;
    private CoeusVector cvLocationType;
    private CoeusVector cvLocationName;
    private Vector vecStudyGroup;
    //Added for COEUSQA-2633 Ability to indicate which procedures IACUC study personnel will perform and training -Start   
    private Vector vecPersonResponseData;
    private Vector vecStudyPersonData;
    private CoeusVector cvPersonRespDelete;
    private static final String PERSON_SELECT_TO_DELETE = "iacucProtoStudyGroupFrm_exceptionCode.1015";
    private static final String PERSON_DELETE_CONFIRMATION = "iacucProtoStudyGroupFrm_exceptionCode.1016";
    //Added for COEUSQA-2633 Ability to indicate which procedures IACUC study personnel will perform and training -End
    private BaseWindowObservable observable;
    private CoeusVector cvProcedureType;
    private static final int ZERO_ROW_INDEX = 0;
    private static CoeusVector cvStudyGroupDelete;
    private static CoeusVector cvLocationDelete = new CoeusVector();
    private static final String ROW_SELECT_TO_DELETE = "iacucProtoStudyGroupFrm_exceptionCode.1010";
    //private int STUDY_GROUP_TAB = 6;
    private CustomElementsForm customElementsForm;
    private HashMap hmCustomForm;
    private HashMap hmOthersDelete;
    private Object oldProcedureCatItem;
    private boolean itemChangeCancelling = false;
    private JPanel pnlCustomElements ;
    private static final String STUDY_GROUP_DELETE_CONFIRMATION = "iacucProtoStudyGroupFrm_exceptionCode.1006";
    private static final String STUDY_GROUP_LOC_DELETE_CONFIRMATION = "iacucProtoStudyGroupFrm_exceptionCode.1007";
    private static final String STUDY_GROUP_CUST_DELETE_CONFIRMATION = "iacucProtoStudyGroupFrm_exceptionCode.1008";
    private static final String ADD_SPECIES = "iacucProtoStudyGroupFrm_exceptionCode.1009";
    //Added with CoeusQA-2551:Rework how user enters species,study groups and procedures in IACUC protocols
    private HashMap hmGroup;
    private CoeusVector cvSpeciesGroupNames;
    //CoeusQA-2551:End
    //COEUSQA-2628-Add to Study Group screen a large comment box called "Overview and Timeline".
    private String overViewTimeLineDetails;
    //COEUSQA:3005 - Dependency between the Location Name and Location Type in IACUC - Start
    private HashMap hmLocationForType;
    private static final int LOCATION_INDEX = 2;
    private CoeusVector cvLocationTypeName;
    private Vector vecLocationData;
    private Object oldLocationTypeItem;
    private static final int LOCATION_TYPE_CODE = 7;
    //COEUSQA:3005 - End
    
    /**
     * Creates new form ProtocolStudyGroupsForm
     */
    public ProtocolStudyGroupsForm() {
    }
    /**
     * Creates new ProtocolStudyGroupsForm with the given functionType
     *
     * @param functionType character which represents the form opened mode.
     */
    public ProtocolStudyGroupsForm(String protocolNumber, int sequenceNumber ,char functionType) {
        this.protocolNumber = protocolNumber;
        this.sequenceNumber = sequenceNumber;
        this.functionType = functionType;
        observable  = new BaseWindowObservable();
        coeusMessageResources = CoeusMessageResources.getInstance();
    }
    
    
    /**
     * This method is used to initialize the form components,set the form
     * data and set the enabled status for all components depending on the
     * <CODE>functionType</CODE> specified while opening the form.
     *
     *
     * @param mdiForm reference to the parent component <CODE>CoeusAppletMDIForm</CODE>
     * @return JComponent reference to the <CODE>ProtocolStudyGroupsForm</CODE> component
     * after initializing and setting the data.
     */
    public JComponent showStudyGroupsForm(CoeusAppletMDIForm mdiForm){
        this.mdiForm = mdiForm;
        initComponents();
        postInitComponents();
        setFormData();
        setTableEditors();
        // Modified for for COEUSQA-2624_Location fields in IACUC protocol: Allow "select type" for drop down search box or find some other way to handle this long list_start
        Component[] comp = { tblStudyGroup,btnAddStudyGroup,btnDeleteStudyGroup,btnAddLocation,btnDeleteLocation,btnLocationName,btnLocationType };
        // Modified for COEUSQA-2624_Location fields in IACUC protocol: Allow "select type" for drop down search box or find some other way to handle this long list_end
        ScreenFocusTraversalPolicy traversal = new ScreenFocusTraversalPolicy(comp);
        setFocusCycleRoot(true);
        //Added for COEUSQA-2633 Ability to indicate which procedures IACUC study personnel will perform and training -Start
        setListenerForPersonResponseTable();
        //Added for COEUSQA-2633 Ability to indicate which procedures IACUC study personnel will perform and training -End
        return this;
    }
    
    /*
     * Post intialize the components
     */
    private void postInitComponents(){
        
        btnAddStudyGroup.addActionListener(this);
        btnDeleteStudyGroup.addActionListener(this);
        btnAddLocation.addActionListener(this);
        btnDeleteLocation.addActionListener(this);
        // Added for COEUSQA-2624_Location fields in IACUC protocol: Allow "select type" for drop down search box or find some other way to handle this long list_start
        btnLocationName.addActionListener(this);
        btnLocationType.addActionListener(this);
        // Added for COEUSQA-2624_Location fields in IACUC protocol: Allow "select type" for drop down search box or find some other way to handle this long list_end
        //COEUSQA-2633 Ability to indicate which procedures IACUC study personnel will perform and training -Start
        btnAddPersons.addActionListener(this);
        btnDeletePersons.addActionListener(this);
        //COEUSQA-2633 Ability to indicate which procedures IACUC study personnel will perform and training -End
        studyGroupTableModel = new StudyGroupTableModel();
        tblStudyGroup.setModel(studyGroupTableModel);
        studyGroupSelectionModel = tblStudyGroup.getSelectionModel();
        studyGroupSelectionModel.addListSelectionListener(this);
        studyGroupSelectionModel.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION );
        tblStudyGroup.setSelectionModel(studyGroupSelectionModel);
        tblStudyGroup.getTableHeader().setFont(CoeusFontFactory.getLabelFont());
        
        
        locationTableModel = new LocationTableModel();
        tblLocation.setModel(locationTableModel);
        ListSelectionModel locationSelectionModel = tblLocation.getSelectionModel();
        locationSelectionModel.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION );
        tblLocation.setSelectionModel(locationSelectionModel);
        
        tblLocation.getTableHeader().setFont(CoeusFontFactory.getLabelFont());
        tblLocation.setFont(CoeusFontFactory.getNormalFont());
        //Added for COEUSQA-2633 Ability to indicate which procedures IACUC study personnel will perform and training -Start
        personResponsibleTableModel = new PersonResponsibleTableModel();
        tblPersonsResponsible.setModel(personResponsibleTableModel);         
        ListSelectionModel personresponsibleSelectionModel = tblPersonsResponsible.getSelectionModel();
        personresponsibleSelectionModel.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION );         
        tblPersonsResponsible.setSelectionModel(personresponsibleSelectionModel);          
        tblPersonsResponsible.getTableHeader().setFont(CoeusFontFactory.getLabelFont());
        tblPersonsResponsible.setFont(CoeusFontFactory.getNormalFont());                             
        //Added for COEUSQA-2633 Ability to indicate which procedures IACUC study personnel will perform and training -End
        
//        if( tblLocation.getRowCount() > 0 ){
//            btnDeleteLocation.setEnabled( true );
//        }
        
        
    }
    
    
    /**
     * This method is used to set the enabled status for the components
     * depending on the functionType specified.
     */
    private void formatFields(){
        boolean enabled = false;
        if(functionType == CoeusGuiConstants.DISPLAY_MODE ||
                functionType == CoeusGuiConstants.AMEND_MODE){
            custFunctionType = TypeConstants.DISPLAY_MODE;
        }else{
            enabled = true;
            custFunctionType = functionType;
        }
        
        btnAddStudyGroup.setEnabled(enabled);
        btnDeleteStudyGroup.setEnabled(enabled);
        btnAddLocation.setEnabled(enabled);
        btnDeleteLocation.setEnabled(enabled);
        // Added for COEUSQA-2624_Location fields in IACUC protocol: Allow "select type" for drop down search box or find some other way to handle this long list_start        
        btnLocationType.setEnabled(enabled);
        btnLocationName.setEnabled(enabled);
        // Added for COEUSQA-2624_Location fields in IACUC protocol: Allow "select type" for drop down search box or find some other way to handle this long list_end
        //COEUSQA-2633 Ability to indicate which procedures IACUC study personnel will perform and training -Start
        btnAddPersons.setEnabled(enabled);
        btnDeletePersons.setEnabled(enabled);
        //COEUSQA-2633 Ability to indicate which procedures IACUC study personnel will perform and training -End
        //COEUSQA-2628-Add to Study Group screen a large comment box called "Overview and Timeline".
        txtArOverviewTimeline.setEditable(enabled);
                
        if(functionType == CoeusGuiConstants.DISPLAY_MODE && functionType == CoeusGuiConstants.AMEND_MODE){
            
            java.awt.Color bgListColor = (java.awt.Color)javax.swing.UIManager.getDefaults().get("Panel.background");
            
            tblStudyGroup.setBackground(bgListColor);
            tblStudyGroup.setSelectionBackground(bgListColor );
            tblStudyGroup.setSelectionForeground(java.awt.Color.BLACK);
            
            tblLocation.setBackground(bgListColor);
            tblLocation.setSelectionBackground(bgListColor );
            tblLocation.setSelectionForeground(java.awt.Color.BLACK);
            
        } else{
            tblStudyGroup.setBackground(java.awt.Color.white);
            tblStudyGroup.setSelectionBackground(java.awt.Color.white);
            tblStudyGroup.setSelectionForeground(java.awt.Color.black);
            
            tblLocation.setBackground(java.awt.Color.white);
            tblLocation.setSelectionBackground(java.awt.Color.white);
            tblLocation.setSelectionForeground(java.awt.Color.black);
        }
        
        
        if(functionType == TypeConstants.DISPLAY_MODE || functionType == TypeConstants.AMEND_MODE){
            java.awt.Color bgListColor = (java.awt.Color)javax.swing.UIManager.getDefaults().get("Panel.background");
            tblStudyGroup.setBackground(bgListColor);
            tblStudyGroup.setSelectionBackground(bgListColor );
            tblStudyGroup.setSelectionForeground(Color.black);

            tblLocation.setBackground(bgListColor);
            tblLocation.setSelectionBackground(bgListColor );
            tblLocation.setSelectionForeground(Color.black);
            
            //COEUSQA-2628-Add to Study Group screen a large comment box called "Overview and Timeline".
            txtArOverviewTimeline.setBackground(bgListColor);  
            //Added for COEUSQA-2633 Ability to indicate which procedures IACUC study personnel will perform and training -Start
            tblPersonsResponsible.setBackground(bgListColor);
            tblPersonsResponsible.setSelectionBackground(bgListColor);
            tblPersonsResponsible.setSelectionForeground(Color.black);
            //Added for COEUSQA-2633 Ability to indicate which procedures IACUC study personnel will perform and training -End
            
        } else{
            tblStudyGroup.setBackground(Color.white);
            tblStudyGroup.setSelectionBackground(Color.white);
            tblStudyGroup.setSelectionForeground(Color.black);
            
            tblLocation.setBackground(Color.white);
            tblLocation.setSelectionBackground(Color.white);
            tblLocation.setSelectionForeground(Color.black);
            
            //COEUSQA-2628-Add to Study Group screen a large comment box called "Overview and Timeline".
            txtArOverviewTimeline.setBackground(Color.white);
            //Added for COEUSQA-2633 Ability to indicate which procedures IACUC study personnel will perform and training -Start
            tblPersonsResponsible.setBackground(Color.white);
            tblPersonsResponsible.setSelectionBackground(Color.white);
            tblPersonsResponsible.setSelectionForeground(Color.black);
            //Added for COEUSQA-2633 Ability to indicate which procedures IACUC study personnel will perform and training -End
        }
        
    }
    
    /*
     * Method to set the editor properties
     *
     */
    private void setTableEditors(){
        tblStudyGroup.setOpaque(false);
        tblStudyGroup.setShowVerticalLines(false);
        tblStudyGroup.setShowHorizontalLines(false);
        tblStudyGroup.setRowHeight(STUDY_GROUP_TABLE_ROW_HEIGHT);
        
        tblStudyGroup.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tblStudyGroup.setFont( CoeusFontFactory.getNormalFont() );
        JTableHeader header = tblStudyGroup.getTableHeader();
        header.setReorderingAllowed(false);
        
        
        TableColumn clmName = tblStudyGroup.getColumnModel().getColumn(STUDY_GROUP_POINTER_COLUMN);
        clmName.setPreferredWidth(STUDY_GROUP_POINTER_COLUMN_PREFERED_WIDTH);
        clmName.setMaxWidth(STUDY_GROUP_POINTER_COLUMN_MAX_WIDTH);
        clmName.setMinWidth(STUDY_GROUP_POINTER_COLUMN_MIN_WIDTH);
        clmName.setHeaderRenderer(new EmptyHeaderRenderer());
        clmName.setCellRenderer(new IconRenderer());
        
        //Added with CoeusQQA-2551:Rework how user enters species,study groups and procedures in IACUC protocols
        clmName = tblStudyGroup.getColumnModel().getColumn(SPECIES_GROUP_NAME_COLUMN);
        clmName.setPreferredWidth(SPECIES_GROUP_NAME_COLUMN_WIDTH);
        clmName.setMaxWidth(SPECIES_GROUP_NAME_COLUMN_MAX_WIDTH);
        clmName.setMinWidth(SPECIES_GROUP_NAME_COLUMN_MIN_WIDTH);
        clmName.sizeWidthToFit();
        clmName.setCellEditor(new StudyGroupComboEditor(SPECIES_GROUP_NAME_COLUMN));
        
        clmName = tblStudyGroup.getColumnModel().getColumn(SPECIES_COLUMN);
        clmName.setPreferredWidth(SPECIES_COLUMN_PREFERED_WIDTH);
        clmName.setMaxWidth(SPECIES_COLUMN_MAX_WIDTH);
        clmName.setMinWidth(SPECIES_COLUMN_MIN_WIDTH);
        clmName.sizeWidthToFit();
//        clmName.setCellEditor(new StudyGroupComboEditor(SPECIES_COLUMN));
        
        clmName = tblStudyGroup.getColumnModel().getColumn(PROCEDURE_CATEGORY_COLUMN);
        clmName.setPreferredWidth(PROCEDURE_CATEGORY_COLUMN_PREFERED_WIDTH);
        clmName.setMaxWidth(PROCEDURE_CATEGORY_COLUMN_MAX_WIDTH);
        clmName.setMinWidth(PROCEDURE_CATEGORY_COLUMN_MIN_WIDTH);
        clmName.sizeWidthToFit();
        clmName.setCellEditor(new StudyGroupComboEditor(PROCEDURE_CATEGORY_COLUMN));
        
        clmName = tblStudyGroup.getColumnModel().getColumn(PROCEDURE_COLUMN);
        clmName.setPreferredWidth(PROCEDURE_COLUMN_PREFERED_WIDTH);
        clmName.setMaxWidth(PROCEDURE_COLUMN_MAX_WIDTH);
        clmName.setMinWidth(PROCEDURE_COLUMN_MIN_WIDTH);
        clmName.sizeWidthToFit();
        clmName.setCellEditor(new StudyGroupComboEditor(PROCEDURE_COLUMN));
        
        clmName = tblStudyGroup.getColumnModel().getColumn(PAIN_CATEGORY_COLUMN);
        clmName.setPreferredWidth(PAIN_CATEGORY_COLUMN_PREFERED_WIDTH);
        clmName.setMaxWidth(PAIN_CATEGORY_COLUMN_MAX_WIDTH);
        clmName.setMinWidth(PAIN_CATEGORY_COLUMN_MIN_WIDTH);
        // Commented for COEUSQA-2627_Move pain category in IACUC protocol to Protocol SpeciesGroup level_start
        //clmName.setCellEditor(new StudyGroupComboEditor(PAIN_CATEGORY_COLUMN));
        // Commented for COEUSQA-2627_Move pain category in IACUC protocol to Protocol SpeciesGroup level_end
        
        clmName = tblStudyGroup.getColumnModel().getColumn(COUNT_COLUMN);
        clmName.setPreferredWidth(COUNT_COLUMN_PREFERED_WIDTH);
        clmName.setMaxWidth(COUNT_COLUMN_MAX_WIDTH);
        clmName.setMinWidth(COUNT_COLUMN_MIN_WIDTH);
        if(functionType != TypeConstants.DISPLAY_MODE){
            clmName.setCellEditor(new DefaultCellEditor(
                    new CoeusTextField(new JTextFieldFilter(JTextFieldFilter.NUMERIC,COUNT_FIELD_CHAR_LENGTH),CoeusGuiConstants.EMPTY_STRING,COUNT_COLUMN)));
            clmName.setResizable(false);
        }         
        //Property for location table
        tblLocation.setOpaque(false);
        tblLocation.setShowVerticalLines(false);
        tblLocation.setShowHorizontalLines(false);
        tblLocation.setRowHeight(LOCATION_TABLE_ROW_HEIGHT);
        tblLocation.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tblLocation.setFont( CoeusFontFactory.getNormalFont() );
        header = tblLocation.getTableHeader();
        header.setReorderingAllowed(false);
        
        clmName = tblLocation.getColumnModel().getColumn(LOCATION_POINTER_COLUMN);
        clmName.setPreferredWidth(LOCATION_POINTER_COLUMN_PREFERED_WIDTH);
        clmName.setMaxWidth(LOCATION_POINTER_COLUMN_MAX_WIDTH);
        clmName.setMinWidth(LOCATION_POINTER_COLUMN_MIN_WIDTH);
        clmName.setHeaderRenderer(new EmptyHeaderRenderer());
        clmName.setCellRenderer(new IconRenderer());
        
        clmName = tblLocation.getColumnModel().getColumn(LOCATION_TYPE_COLUMN);
        clmName.setPreferredWidth(LOCATION_TYPE_COLUMN_PREFERED_WIDTH);
        clmName.setMaxWidth(LOCATION_TYPE_COLUMN_MAX_WIDTH);
        clmName.setMinWidth(LOCATION_TYPE_COLUMN_MIN_WIDTH);
        //COEUSQA:3005 - Dependency between the Location Name and Location Type in IACUC - Start
//        clmName.setCellEditor(new LocationComboEditor());
        clmName.setCellEditor(new LocationComboEditor(LOCATION_TYPE_COLUMN));
        //COEUSQA:3005 - End

        clmName = tblLocation.getColumnModel().getColumn(LOCATION_NAME_COLUMN);
        clmName.setPreferredWidth(LOCATION_NAME_COLUMN_PREFEREDN_WIDTH);
        clmName.setMaxWidth(LOCATION_NAME_COLUMN_MAX_WIDTH);
        clmName.setMinWidth(LOCATION_NAME_COLUMN_MIN_WIDTH);
        //COEUSQA:3005 - Dependency between the Location Name and Location Type in IACUC - Start
//        clmName.setCellEditor(new LocationComboEditor());
        clmName.setCellEditor(new LocationComboEditor(LOCATION_NAME_COLUMN));
        //COEUSQA:3005 - End
        
        clmName = tblLocation.getColumnModel().getColumn(DESCRIPTION_COLUMN);
        clmName.setPreferredWidth(DESCRIPTION_COLUMN_PREFERED_WIDTH);
        clmName.setMaxWidth(DESCRIPTION_COLUMN_MAX_WIDTH);
        clmName.setMinWidth(DESCRIPTION_COLUMN_MIN_WIDTH);
        if(functionType != TypeConstants.DISPLAY_MODE){
            clmName.setCellEditor(new DefaultCellEditor(
                    new CoeusTextField(new LimitedPlainDocument(60),CoeusGuiConstants.EMPTY_STRING,DESCRIPTION_COLUMN)));
            clmName.setResizable(false);
        }
        // Added for COEUSQA-2625_Add another level of detail for Location in IACUC protocol_start
        clmName = tblLocation.getColumnModel().getColumn(LOCATION_ROOM_COLUMN);
        clmName.setPreferredWidth(LOCATION_ROOM_COLUMN_PREFERRED_WIDTH);
        clmName.setMaxWidth(LOCATION_ROOM_COLUMN_MAX_WIDTH);
        clmName.setMinWidth(LOCATION_ROOM_COLUMN_MIN_WIDTH);
        if(functionType != TypeConstants.DISPLAY_MODE){
            clmName.setCellEditor(new DefaultCellEditor(
                    new CoeusTextField(new LimitedPlainDocument(60),CoeusGuiConstants.EMPTY_STRING,LOCATION_ROOM_COLUMN)));
            clmName.setResizable(true);
        }
        // Added for COEUSQA-2625_Add another level of detail for Location in IACUC protocol_end
	 //Added for COEUSQA-2633 Ability to indicate which procedures IACUC study personnel will perform and training -Start
        //Property for persons responsible table
        tblPersonsResponsible.setOpaque(false);
        tblPersonsResponsible.setShowVerticalLines(false);
        tblPersonsResponsible.setShowHorizontalLines(false);      
        tblPersonsResponsible.setFont( CoeusFontFactory.getNormalFont() );
        tblPersonsResponsible.setRowHeight(LOCATION_TABLE_ROW_HEIGHT);         
        tblPersonsResponsible.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        tblPersonsResponsible.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);                 
        header = tblPersonsResponsible.getTableHeader();
        header.setReorderingAllowed(false);
        
        clmName = tblPersonsResponsible.getColumnModel().getColumn(PERSON_POINTER_COLUMN);
        clmName.setPreferredWidth(PERSON_POINTER_COLUMN_WIDTH);
        clmName.setMaxWidth(PERSON_POINTER_COLUMN_WIDTH);
        clmName.setMinWidth(PERSON_POINTER_COLUMN_WIDTH);
        clmName.setHeaderRenderer(new EmptyHeaderRenderer());
        clmName.setCellRenderer(new IconRenderer());
        
        clmName = tblPersonsResponsible.getColumnModel().getColumn(PERSON_NAME_COLUMN);
        clmName.setPreferredWidth(PERSON_NAME_COLUMN_WIDTH);
        clmName.setMaxWidth(PERSON_NAME_COLUMN_WIDTH);
        clmName.setMinWidth(PERSON_NAME_COLUMN_WIDTH);  
        
        clmName = tblPersonsResponsible.getColumnModel().getColumn(PERSON_TRAINING_COLUMN);
        clmName.setPreferredWidth(PERSON_TRAINING_COLUMN_WIDTH);
        clmName.setMaxWidth(PERSON_TRAINING_COLUMN_WIDTH);
        clmName.setMinWidth(PERSON_TRAINING_COLUMN_WIDTH);        
        clmName.setCellRenderer(new ImageRenderer());
        //Added for COEUSQA-2633 Ability to indicate which procedures IACUC study personnel will perform and training -End 
    }
    
    /*
     * Method to set the study group form data
     *
     */
    public void setFormData(){
        hmCustomForm = new HashMap();
        hmOthersDelete = new HashMap();
        cvStudyGroupDelete = new CoeusVector();
        //Added for COEUSQA-2633 Ability to indicate which procedures IACUC study personnel will perform and training -Start
        cvPersonRespDelete = new CoeusVector();
        //Added for COEUSQA-2633 Ability to indicate which procedures IACUC study personnel will perform and training -End
                
        vecStudyGroup = getStudyGroupData();        
        formatFields();
        setCustomElementFormForStudyGroup();
        if(vecStudyGroup == null){
            vecStudyGroup = new Vector();
        }
        if(cvSpeciesGroupNames == null || 
                (cvSpeciesGroupNames.size() <1) || 
                vecStudyGroup == null || 
                (vecStudyGroup.size()< 1)){
            btnAddLocation.setEnabled(false);
            btnDeleteLocation.setEnabled(false);
            // Added for COEUSQA-2624_Location fields in IACUC protocol: Allow "select type" for drop down search box or find some other way to handle this long list_start
            btnLocationName.setEnabled(false);
            btnLocationType.setEnabled(false);
            // Added for COEUSQA-2624_Location fields in IACUC protocol: Allow "select type" for drop down search box or find some other way to handle this long list_end
            //COEUSQA-2633 Ability to indicate which procedures IACUC study personnel will perform and training -Start
            btnAddPersons.setEnabled(false);
            btnDeletePersons.setEnabled(false);
            //COEUSQA-2633 Ability to indicate which procedures IACUC study personnel will perform and training -End
        }
//        if( tblLocation.getRowCount() <= 0 ){
//            btnDeleteLocation.setEnabled( false );
//        }
        studyGroupTableModel.setData(vecStudyGroup);
        if(vecStudyGroup != null && vecStudyGroup.size() > 0){
            tblStudyGroup.setRowSelectionInterval(0,0);
        }else{
            btnDeleteStudyGroup.setEnabled(false);
        }
        //COEUSQA-2628-Add to Study Group screen a large comment box called "Overview and Timeline"
        txtArOverviewTimeline.setText(getOverviewTimelineDetails());
        saveRequired = false;
        setPropertyChangeSettings();
    }
   
    /*
     * Method to set the custom element form into the map 
     * Key - Study group id
     * Value - CustomElementsForm
     */
    private void setCustomElementFormForStudyGroup(){
        if(vecStudyGroup != null && vecStudyGroup.size()>0){
            
            
            for(int index=0;index<vecStudyGroup.size();index++){
                ProtocolStudyGroupBean studyGroupBean = (ProtocolStudyGroupBean)vecStudyGroup.get(index);
                
                customElementsForm = new CustomElementsForm(custFunctionType,true,new Vector());
                hmCustomForm.put(studyGroupBean.getStudyGroupId()+CoeusGuiConstants.EMPTY_STRING,customElementsForm);
                customElementsForm.setPersonColumnValues(studyGroupBean.getOtherDetails());
            }
        }
    }
    
    /*
     * Method to set study group bean property
     */
    private void setPropertyChangeSettings(){
        if(functionType != TypeConstants.AMEND_MODE &&
                vecStudyGroup != null && vecStudyGroup.size() > 0){
           ProtocolStudyGroupBean oldStudyGroupData = null;
            for(int index=0;index<vecStudyGroup.size();index++){
                oldStudyGroupData = (ProtocolStudyGroupBean)vecStudyGroup.get(index);
                oldStudyGroupData.addPropertyChangeListener(new PropertyChangeListener() {
                    public void propertyChange(PropertyChangeEvent pce) {
                        if( !( pce.getOldValue() == null && pce.getNewValue() == null ) ){
                            if(tblStudyGroup.isEditing()){
                                if(tblStudyGroup.getCellEditor() != null){
                                    tblStudyGroup.getCellEditor().stopCellEditing();
                                }
                            }
                            ProtocolStudyGroupBean studyGroupBean = 
                                    (ProtocolStudyGroupBean)vecStudyGroup.get(tblStudyGroup.getSelectedRow());
                            studyGroupBean.setAcType(TypeConstants.UPDATE_RECORD);
                            setSaveRequired(true);
                        }
                    }
                });
                final Vector vecLocation = oldStudyGroupData.getLocations();
                if(vecLocation != null && vecLocation.size()>0){
                    ProtocolStudyGroupLocationBean oldLocationBean = null;
                    for(int locationIndex=0;locationIndex<vecLocation.size();locationIndex++){
                        oldLocationBean = (ProtocolStudyGroupLocationBean)vecLocation.get(locationIndex);
                        oldLocationBean.addPropertyChangeListener(new PropertyChangeListener() {
                            public void propertyChange(PropertyChangeEvent pce) {
                                if( !( pce.getOldValue() == null && pce.getNewValue() == null ) ){
                                    if(tblLocation.isEditing()){
                                        if(tblLocation.getCellEditor() != null){
                                            tblLocation.getCellEditor().stopCellEditing();
                                        }
                                    }
                                    ProtocolStudyGroupLocationBean locationBean =
                                            (ProtocolStudyGroupLocationBean)vecLocation.get(tblLocation.getSelectedRow());
                                    locationBean.setAcType(TypeConstants.UPDATE_RECORD);
                                    setSaveRequired(true);
                                }
                            }
                        });
                    }
                }
            }
        }
    }
    
    /*
     * Method to get the study group data from the server
     *
     */
    private Vector getStudyGroupData(){
        RequesterBean request = new RequesterBean();
        Vector cvStudyGroup = null;
        Vector cvStudyGroupData = null;
        Vector vecProtocolDetail = new Vector();
        vecProtocolDetail.add(PROTOCOL_NUMBER_INDEX,protocolNumber); 
        vecProtocolDetail.add(SEQUENCE_NUMBER_INDEX,sequenceNumber);
        vecProtocolDetail.add(FUNCTION_TYPE_INDEX,functionType);
        request.setFunctionType(GET_STUDY_GROUP_DATA);
        request.setDataObjects(vecProtocolDetail);
        AppletServletCommunicator comm
                = new AppletServletCommunicator(connectTo, request);
        comm.send();
        ResponderBean response = comm.getResponse();
        if (response!=null){
            if (response.isSuccessfulResponse()){
                if(functionType == TypeConstants.MODIFY_MODE || 
                        functionType == TypeConstants.ADD_MODE ||
                        functionType == 'P' ||
                        functionType == TypeConstants.AMEND_MODE){
                    cvStudyGroupData = response.getDataObjects();
                    cvStudyGroup = (Vector)cvStudyGroupData.get(STUDY_GROUP_DATA_INDEX);
                    if(functionType == TypeConstants.AMEND_MODE &&
                            cvStudyGroup != null && cvStudyGroup.size() > 0){
                        for(int index=0;index<cvStudyGroup.size();index++){
                            ProtocolStudyGroupBean studyGroupBean = (ProtocolStudyGroupBean)cvStudyGroup.get(index);
                            studyGroupBean.setAcType(TypeConstants.INSERT_RECORD);
                            Vector vecLocation = studyGroupBean.getLocations();
                            if(vecLocation != null && vecLocation.size()>0){
                                for(int locIndex=0;locIndex<vecLocation.size();locIndex++){
                                    ProtocolStudyGroupLocationBean locationBean = 
                                            (ProtocolStudyGroupLocationBean)vecLocation.get(locIndex);
                                    locationBean.setAcType(TypeConstants.INSERT_RECORD);
                                }
                            }
//                            Vector vecCustomElem = studyGroupBean.getOtherDetails();
//                            if(vecCustomElem != null && vecCustomElem.size()>0){
//                                for(int otherIndex=0;otherIndex<vecCustomElem.size();otherIndex++){
//                                    ProtocolCustomElementsInfoBean customInfoBean = 
//                                            (ProtocolCustomElementsInfoBean)vecCustomElem.get(otherIndex);
//                                    customInfoBean.setAcType(TypeConstants.INSERT_RECORD);
//                                    
//                                }
//                            }
                        }
                    }
                    cvProcCategoryType = (CoeusVector)cvStudyGroupData.get(PROC_CATEGORY_INDEX);
                    hmProcedureForCat = (HashMap)cvStudyGroupData.get(PROCEDURE_INDEX);
                    cvPainCategoryType = (CoeusVector)cvStudyGroupData.get(PAIN_CATEGORY_INDEX);
                    cvLocationType = (CoeusVector)cvStudyGroupData.get(LOCATION_TYPE_INDEX);
                    cvLocationName = (CoeusVector)cvStudyGroupData.get(LOCATION_NAME_INDEX);
                    hmCustomDataForCat = (HashMap)cvStudyGroupData.get(PROCEDURE_CUSTOM_DATA_INDEX);  
                    //COEUSQA:3005 - Dependency between the Location Name and Location Type in IACUC - Start
                    hmLocationForType = (HashMap)cvStudyGroupData.get(LOCATION_TYPE_CODE);
                    //COEUSQA:3005 - End
                    
                }else{
                    cvStudyGroup = response.getDataObjects();
                }
            }
        }
        return cvStudyGroup;
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        btnAddStudyGroup = new javax.swing.JButton();
        btnDeleteStudyGroup = new javax.swing.JButton();
        scrPnLocation = new javax.swing.JScrollPane();
        tblLocation = new javax.swing.JTable();
        btnAddLocation = new javax.swing.JButton();
        btnDeleteLocation = new javax.swing.JButton();
        pnlOverviewTimeline = new javax.swing.JPanel();
        scrPnOverviewTimeline = new javax.swing.JScrollPane();
        txtArOverviewTimeline = new javax.swing.JTextArea();
        btnLocationType = new javax.swing.JButton();
        btnLocationName = new javax.swing.JButton();
        scrPnPersonResponsible = new javax.swing.JScrollPane();
        tblPersonsResponsible = new javax.swing.JTable();
        btnAddPersons = new javax.swing.JButton();
        btnDeletePersons = new javax.swing.JButton();
        pnlProcedures = new javax.swing.JPanel();
        scrPnStudyGroup = new javax.swing.JScrollPane();
        tblStudyGroup = new javax.swing.JTable();
        scrnPnCustomElements = new javax.swing.JScrollPane();

        setLayout(new java.awt.GridBagLayout());

        setMaximumSize(new java.awt.Dimension(1000, 525));
        setMinimumSize(new java.awt.Dimension(1000, 525));
        setPreferredSize(new java.awt.Dimension(1000, 525));
        btnAddStudyGroup.setFont(CoeusFontFactory.getLabelFont());
        btnAddStudyGroup.setMnemonic('A');
        btnAddStudyGroup.setText("Add");
        btnAddStudyGroup.setName("btnAdd");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(25, 5, 5, 5);
        add(btnAddStudyGroup, gridBagConstraints);

        btnDeleteStudyGroup.setFont(CoeusFontFactory.getLabelFont());
        btnDeleteStudyGroup.setMnemonic('D');
        btnDeleteStudyGroup.setText("Delete");
        btnDeleteStudyGroup.setName("btnDelete");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 5, 5);
        add(btnDeleteStudyGroup, gridBagConstraints);

        scrPnLocation.setBorder(javax.swing.BorderFactory.createTitledBorder("Locations"));
        scrPnLocation.setMaximumSize(new java.awt.Dimension(200, 50));
        scrPnLocation.setMinimumSize(new java.awt.Dimension(200, 50));
        scrPnLocation.setPreferredSize(new java.awt.Dimension(200, 50));
        scrPnLocation.setViewportView(tblLocation);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 15;
        gridBagConstraints.gridheight = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 400;
        gridBagConstraints.ipady = 44;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 0, 4);
        add(scrPnLocation, gridBagConstraints);

        btnAddLocation.setFont(CoeusFontFactory.getLabelFont());
        btnAddLocation.setMnemonic('n');
        btnAddLocation.setText("Add Location");
        btnAddLocation.setName("btnAddUnit");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 4, 4, 4);
        add(btnAddLocation, gridBagConstraints);

        btnDeleteLocation.setFont(CoeusFontFactory.getLabelFont());
        btnDeleteLocation.setMnemonic('l');
        btnDeleteLocation.setText("Delete Location");
        btnDeleteLocation.setName("btnDelUnit");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 5, 5);
        add(btnDeleteLocation, gridBagConstraints);

        pnlOverviewTimeline.setLayout(new java.awt.GridBagLayout());

        pnlOverviewTimeline.setBorder(javax.swing.BorderFactory.createTitledBorder("Overview and Timeline"));
        pnlOverviewTimeline.setMaximumSize(new java.awt.Dimension(200, 30));
        pnlOverviewTimeline.setMinimumSize(new java.awt.Dimension(200, 30));
        pnlOverviewTimeline.setPreferredSize(new java.awt.Dimension(200, 30));
        scrPnOverviewTimeline.setMaximumSize(new java.awt.Dimension(200, 30));
        scrPnOverviewTimeline.setMinimumSize(new java.awt.Dimension(200, 30));
        scrPnOverviewTimeline.setPreferredSize(new java.awt.Dimension(200, 30));
        txtArOverviewTimeline.setDocument(new LimitedPlainDocument(2000));
        txtArOverviewTimeline.setFont(CoeusFontFactory.getNormalFont());
        txtArOverviewTimeline.setLineWrap(true);
        txtArOverviewTimeline.setWrapStyleWord(true);
        scrPnOverviewTimeline.setViewportView(txtArOverviewTimeline);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.RELATIVE;
        gridBagConstraints.gridheight = java.awt.GridBagConstraints.RELATIVE;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 400;
        gridBagConstraints.ipady = 48;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 28, 5, 1);
        pnlOverviewTimeline.add(scrPnOverviewTimeline, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 25;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 400;
        gridBagConstraints.ipady = 51;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 5, 4);
        add(pnlOverviewTimeline, gridBagConstraints);

        btnLocationType.setFont(CoeusFontFactory.getLabelFont());
        btnLocationType.setMnemonic('p');
        btnLocationType.setText("Find Loc Type");
        btnLocationType.setMaximumSize(new java.awt.Dimension(123, 23));
        btnLocationType.setMinimumSize(new java.awt.Dimension(123, 23));
        btnLocationType.setPreferredSize(new java.awt.Dimension(123, 23));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 5, 5);
        add(btnLocationType, gridBagConstraints);

        btnLocationName.setFont(CoeusFontFactory.getLabelFont());
        btnLocationName.setMnemonic('i');
        btnLocationName.setText("Find Loc Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        add(btnLocationName, gridBagConstraints);

        scrPnPersonResponsible.setBorder(javax.swing.BorderFactory.createTitledBorder("Persons Responsible"));
        scrPnPersonResponsible.setMaximumSize(new java.awt.Dimension(200, 54));
        scrPnPersonResponsible.setMinimumSize(new java.awt.Dimension(200, 54));
        scrPnPersonResponsible.setPreferredSize(new java.awt.Dimension(200, 54));
        scrPnPersonResponsible.setViewportView(tblPersonsResponsible);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 19;
        gridBagConstraints.gridheight = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 400;
        gridBagConstraints.ipady = 40;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 0, 4);
        add(scrPnPersonResponsible, gridBagConstraints);

        btnAddPersons.setFont(CoeusFontFactory.getLabelFont());
        btnAddPersons.setLabel("Add Persons");
        btnAddPersons.setMaximumSize(new java.awt.Dimension(123, 23));
        btnAddPersons.setMinimumSize(new java.awt.Dimension(123, 23));
        btnAddPersons.setPreferredSize(new java.awt.Dimension(123, 23));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.insets = new java.awt.Insets(10, 5, 5, 5);
        add(btnAddPersons, gridBagConstraints);

        btnDeletePersons.setFont(CoeusFontFactory.getLabelFont());
        btnDeletePersons.setLabel("Delete Persons");
        btnDeletePersons.setMaximumSize(new java.awt.Dimension(123, 23));
        btnDeletePersons.setMinimumSize(new java.awt.Dimension(123, 23));
        btnDeletePersons.setPreferredSize(new java.awt.Dimension(123, 23));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 5, 5);
        add(btnDeletePersons, gridBagConstraints);

        pnlProcedures.setLayout(new java.awt.GridBagLayout());

        pnlProcedures.setBorder(javax.swing.BorderFactory.createTitledBorder("Procedures"));
        scrPnStudyGroup.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        scrPnStudyGroup.setMaximumSize(new java.awt.Dimension(445, 54));
        scrPnStudyGroup.setMinimumSize(new java.awt.Dimension(445, 54));
        scrPnStudyGroup.setPreferredSize(new java.awt.Dimension(445, 54));
        scrPnStudyGroup.setViewportView(tblStudyGroup);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 8;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 400;
        gridBagConstraints.ipady = 54;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 0, 5);
        pnlProcedures.add(scrPnStudyGroup, gridBagConstraints);

        scrnPnCustomElements.setBorder(javax.swing.BorderFactory.createTitledBorder("Additional Information"));
        scrnPnCustomElements.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrnPnCustomElements.setMaximumSize(new java.awt.Dimension(190, 115));
        scrnPnCustomElements.setMinimumSize(new java.awt.Dimension(190, 115));
        scrnPnCustomElements.setPreferredSize(new java.awt.Dimension(190, 115));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.gridheight = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(2, 3, 2, 3);
        pnlProcedures.add(scrnPnCustomElements, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 15;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        add(pnlProcedures, gridBagConstraints);

    }// </editor-fold>//GEN-END:initComponents


    public void actionPerformed(java.awt.event.ActionEvent actionEvent) {
        Object actionSource = actionEvent.getSource();
        if(actionSource.equals(btnAddStudyGroup)){
           if(functionType != TypeConstants.DISPLAY_MODE){
               if(tblStudyGroup.isEditing()){
                   if(tblStudyGroup.getCellEditor() != null){
                       tblStudyGroup.getCellEditor().stopCellEditing();
                   }
               }
               if(cvSpeciesGroupNames == null || cvSpeciesGroupNames.size()<1){
                   CoeusOptionPane.showWarningDialog(coeusMessageResources.parseMessageKey(ADD_SPECIES));
                   return;
               }
               ProtocolStudyGroupBean newStudyGroupBean = new ProtocolStudyGroupBean();
               newStudyGroupBean.setProtocolNumber(protocolNumber);
               newStudyGroupBean.setSequenceNumber(sequenceNumber);
               newStudyGroupBean.setAcType(TypeConstants.INSERT_RECORD);
               int studyGroupId = getMaxStudyGroupId()+1;
               newStudyGroupBean.setStudyGroupId(studyGroupId);
               customElementsForm = new CustomElementsForm(custFunctionType,true,new Vector());
               hmCustomForm.put(studyGroupId+CoeusGuiConstants.EMPTY_STRING,customElementsForm);
               vecStudyGroup.add(newStudyGroupBean);
               studyGroupTableModel.setData(vecStudyGroup);
               int lastAddedRow = tblStudyGroup.getRowCount()-1;
               tblStudyGroup.editCellAt(lastAddedRow,SPECIES_COLUMN);
               tblStudyGroup.setRowSelectionInterval(lastAddedRow,lastAddedRow);
               btnDeleteStudyGroup.setEnabled(true);
               btnAddLocation.setEnabled(true);
               //COEUSQA-2633 Ability to indicate which procedures IACUC study personnel will perform and training -Start
               btnAddPersons.setEnabled(true);
               //COEUSQA-2633 Ability to indicate which procedures IACUC study personnel will perform and training -End
               setSaveRequired(true);
           }
        }else if(actionSource.equals(btnAddLocation)){
           if(functionType != TypeConstants.DISPLAY_MODE){
               if(tblLocation.isEditing()){
                   if(tblLocation.getCellEditor() != null){
                       tblLocation.getCellEditor().stopCellEditing();
                   }
               }
               ProtocolStudyGroupLocationBean newLocationBean = new ProtocolStudyGroupLocationBean();
               newLocationBean.setProtocolNumber(protocolNumber);
               newLocationBean.setSequenceNumber(sequenceNumber);
               newLocationBean.setAcType(TypeConstants.INSERT_RECORD);
               int selStudyGroupRow = tblStudyGroup.getSelectedRow();
               ProtocolStudyGroupBean studyGroupBean = (ProtocolStudyGroupBean)vecStudyGroup.get(selStudyGroupRow);
               newLocationBean.setStudyGroupId(studyGroupBean.getStudyGroupId());
               newLocationBean.setStudyGroupLocationId(getMaxLocationId()+1);
               Vector vecLocation = null;
               vecLocation = studyGroupBean.getLocations();
               if(vecLocation == null){
                   vecLocation = new Vector();
               }
               vecLocation.add(newLocationBean);
               locationTableModel.setData(vecLocation);
               studyGroupBean.setLocations(vecLocation);
               int lastAddedRow = tblLocation.getRowCount()-1;
               // Commented for COEUSQA-2624_Location fields in IACUC protocol: Allow "select type" for drop down search box or find some other way to handle this long list_start
//               tblLocation.editCellAt(lastAddedRow,LOCATION_TYPE_COLUMN);
               // Commented for COEUSQA-2624_Location fields in IACUC protocol: Allow "select type" for drop down search box or find some other way to handle this long list_end
               tblLocation.setRowSelectionInterval(lastAddedRow,lastAddedRow);
               btnDeleteLocation.setEnabled(true);
               // Added for COEUSQA-2624_Location fields in IACUC protocol: Allow "select type" for drop down search box or find some other way to handle this long list_start
               btnLocationName.setEnabled(true);
               btnLocationType.setEnabled(true);
               // Added for COEUSQA-2624_Location fields in IACUC protocol: Allow "select type" for drop down search box or find some other way to handle this long list_end
               setSaveRequired(true);

           }
        }else if(actionSource.equals(btnDeleteStudyGroup)){
            if(functionType != TypeConstants.DISPLAY_MODE){
                if(tblStudyGroup.isEditing()){
                    if(tblStudyGroup.getCellEditor() != null){
                        tblStudyGroup.getCellEditor().stopCellEditing();
                    }
                }
                int selectedRow = tblStudyGroup.getSelectedRow();
                if(selectedRow > -1){
                    int selectedOption = CoeusOptionPane.
                            showQuestionDialog(coeusMessageResources.parseMessageKey(STUDY_GROUP_DELETE_CONFIRMATION),
                            CoeusOptionPane.OPTION_YES_NO,
                            CoeusOptionPane.DEFAULT_YES);
                    switch(selectedOption){
                        case CoeusOptionPane.SELECTION_YES:
                            ProtocolStudyGroupBean studyGroupBean = (ProtocolStudyGroupBean)vecStudyGroup.get(selectedRow);
                            if(!TypeConstants.INSERT_RECORD.equals(studyGroupBean.getAcType())){
                                studyGroupBean.setAcType(TypeConstants.DELETE_RECORD);
                                cvStudyGroupDelete.add(studyGroupBean);
                            }
                            vecStudyGroup.remove(selectedRow);
                            studyGroupTableModel.setData(vecStudyGroup);
                            locationTableModel.setData(new Vector());
                            //Added for COEUSQA-2633 Ability to indicate which procedures IACUC study personnel will perform and training -Start
                            personResponsibleTableModel.setData(new Vector());
                            //Added for COEUSQA-2633 Ability to indicate which procedures IACUC study personnel will perform and training -End
                            if(selectedRow == 0 && tblStudyGroup.getRowCount() > 0){
                                tblStudyGroup.setRowSelectionInterval(ZERO_ROW_INDEX,ZERO_ROW_INDEX);
                            }
                            if(selectedRow-1 > - 1){
                                tblStudyGroup.setRowSelectionInterval(selectedRow-1,selectedRow-1);
                            }
                            if(vecStudyGroup != null && vecStudyGroup.size() < 1){
                                btnDeleteStudyGroup.setEnabled(false);
                                btnDeleteLocation.setEnabled(false);
                                btnAddLocation.setEnabled(false);
                                // Added for COEUSQA-2624_Location fields in IACUC protocol: Allow "select type" for drop down search box or find some other way to handle this long list_start
                                btnLocationName.setEnabled(false);
                                btnLocationType.setEnabled(false);
                                // Added for COEUSQA-2624_Location fields in IACUC protocol: Allow "select type" for drop down search box or find some other way to handle this long list_end
                                //Added for COEUSQA-2633 Ability to indicate which procedures IACUC study personnel will perform and training -Start
                                btnAddPersons.setEnabled(false);
                                btnDeletePersons.setEnabled(false);
                                //Added for COEUSQA-2633 Ability to indicate which procedures IACUC study personnel will perform and training -End
                                loadOtherDetails(new ProtocolStudyGroupBean());
                            }
                            //Added for COEUSQA-2633 Ability to indicate which procedures IACUC study personnel will perform and training -Start
                            //observable.notifyObservers(vecStudyGroup);
                            notifyObserver(vecStudyGroup, SPECIES_TYPE);
                            //Added for COEUSQA-2633 Ability to indicate which procedures IACUC study personnel will perform and training -End
                            setSaveRequired(true);
                            break;
                        case CoeusOptionPane.SELECTION_NO:
                            break;
                        default:
                            break;
                    }
                }else{
                    CoeusOptionPane.showErrorDialog(coeusMessageResources.parseMessageKey(ROW_SELECT_TO_DELETE));
                }
            }
        }else if(actionSource.equals(btnDeleteLocation)){
            if(functionType != TypeConstants.DISPLAY_MODE){
                if(tblLocation.isEditing()){
                    if(tblLocation.getCellEditor() != null){
                        tblLocation.getCellEditor().stopCellEditing();
                    }
                }
                int selectedRow = tblLocation.getSelectedRow();
                if(selectedRow > -1){
                    int selectedOption = CoeusOptionPane.
                           showQuestionDialog(coeusMessageResources.parseMessageKey(STUDY_GROUP_LOC_DELETE_CONFIRMATION),
                            CoeusOptionPane.OPTION_YES_NO,
                            CoeusOptionPane.DEFAULT_YES);
                    switch(selectedOption){
                        case CoeusOptionPane.SELECTION_YES:
                            int studyGroupSelRow = tblStudyGroup.getSelectedRow();
                            ProtocolStudyGroupBean studyGroupBean = (ProtocolStudyGroupBean)vecStudyGroup.get(studyGroupSelRow);
                            Vector vecLocation = studyGroupBean.getLocations();
                            ProtocolStudyGroupLocationBean locationBean = 
                                    (ProtocolStudyGroupLocationBean)vecLocation.get(selectedRow);
                            if(!TypeConstants.INSERT_RECORD.equals(locationBean.getAcType())){
                                locationBean.setAcType(TypeConstants.DELETE_RECORD);
                                cvLocationDelete.add(locationBean);
                            }
                            vecLocation.remove(selectedRow);
                            locationTableModel.setData(vecLocation);
                            studyGroupBean.setLocations(vecLocation);
                            if(selectedRow == 0 && tblLocation.getRowCount() > 0){
                                tblLocation.setRowSelectionInterval(ZERO_ROW_INDEX,ZERO_ROW_INDEX);
                            }
                            if(selectedRow-1 > - 1){
                                tblLocation.setRowSelectionInterval(selectedRow-1,selectedRow-1);
                            }
                            if(vecLocation != null && vecLocation.size() < 1){
                                btnDeleteLocation.setEnabled(false);
                                // Added for COEUSQA-2624_Location fields in IACUC protocol: Allow "select type" for drop down search box or find some other way to handle this long list_start
                                btnLocationName.setEnabled(false);
                                btnLocationType.setEnabled(false);
                                // Added for COEUSQA-2624_Location fields in IACUC protocol: Allow "select type" for drop down search box or find some other way to handle this long list_end
                            }
                            setSaveRequired(true);
                            break;
                        case CoeusOptionPane.SELECTION_NO:
                            break;
                        default:
                            break;
                    }
                }else{
                    CoeusOptionPane.showErrorDialog(coeusMessageResources.parseMessageKey(ROW_SELECT_TO_DELETE));
                }
            }
        // Added for COEUSQA-2624_Location fields in IACUC protocol: Allow "select type" for drop down search box or find some other way to handle this long list_start    
        }else if(actionSource.equals(btnLocationType)){
            if(tblLocation.isEditing()){
                if(tblLocation.getCellEditor() != null){
                    tblLocation.getCellEditor().stopCellEditing();
                }
            }
            int selectedRow = tblLocation.getSelectedRow();
            if(selectedRow > -1){
                try{
                    CoeusSearch locationTypeSearch = new CoeusSearch( mdiForm, "LOCATIONTYPESEARCH",
                            CoeusSearch.TWO_TABS );
                    locationTypeSearch.showSearchWindow();
                    Vector vecSelectedLocType = locationTypeSearch.getMultipleSelectedRows();
                    if ( vecSelectedLocType != null ){
                        HashMap singleLocationData = null;
                        for(int indx = 0; indx < vecSelectedLocType.size(); indx++ ){
                            
                            singleLocationData = (HashMap)vecSelectedLocType.get( indx ) ;
                            
                            if( singleLocationData == null || singleLocationData.isEmpty() ){
                                continue;
                            }
                            String locTypeCode = checkForNull(singleLocationData.get( "LOCATION_TYPE_CODE" ));
                            String locationType = checkForNull(singleLocationData.get( "LOCATION" ));
                            int studyGroupSelRow = tblStudyGroup.getSelectedRow();
                            ProtocolStudyGroupBean studyGroupBean = (ProtocolStudyGroupBean)vecStudyGroup.get(studyGroupSelRow);
                            Vector vecLocation = studyGroupBean.getLocations();
                            ProtocolStudyGroupLocationBean locationBean = 
                                    (ProtocolStudyGroupLocationBean)vecLocation.get(selectedRow);
                            locationBean.setLocationTypeCode(new Integer(locTypeCode).intValue());
                            locationBean.setLocationTypeDesc(locationType);
                            locationTableModel.fireTableDataChanged();
                            tblLocation.setRowSelectionInterval(selectedRow,selectedRow);
                            //COEUSQA:3005 - Dependency between the Location Name and Location Type in IACUC - Start
                            locationBean.setLocationId(-1);
                            locationBean.setLocationName(null);
                            //COEUSQA:3005 - End
                            //tblLocation.getModel().fireTableDataChanged();
                            //tblLocation.getModel().getRowCount()+1,tblLocation.getModel().getRowCount()+1);
                            
                        } //end of for loop
                    }//end of vSelectedPersons != null
                }catch(Exception e){
                    e.printStackTrace();
                }               
                
            }
        }else if(actionSource.equals(btnLocationName)){
            if(tblLocation.isEditing()){
                if(tblLocation.getCellEditor() != null){
                    tblLocation.getCellEditor().stopCellEditing();
                }
            }
            int selectedRow = tblLocation.getSelectedRow();
            if(selectedRow > -1){
                try{
                    //COEUSQA:3005 - Dependency between the Location Name and Location Type in IACUC - Start
//                    CoeusSearch locationNameSearch = new CoeusSearch( mdiForm, "LOCATIONNAMESEARCH",
//                            CoeusSearch.TWO_TABS );
                    ProtocolStudyGroupLocationBean locationBean = (ProtocolStudyGroupLocationBean)vecLocationData.get(selectedRow);
                    if(locationBean.getLocationTypeCode() > 0) {
                        CoeusSearch locationNameSearch = new CoeusSearch( mdiForm, "LOCATIONNAMESEARCH",Integer.toString(locationBean.getLocationTypeCode()),
                                CoeusSearch.TWO_TABS );
                        //COEUSQA:3005 - End
                        locationNameSearch.showSearchWindow();
                        Vector vecSelectedLocations = locationNameSearch.getMultipleSelectedRows();
                        if ( vecSelectedLocations != null ){
                            HashMap singleData = null;
                            for(Object vecselectedlocations : vecSelectedLocations) {                                
                                singleData = (HashMap)vecselectedlocations;                                
                                if( singleData == null || singleData.isEmpty() ){
                                    continue;
                                }
                                String locCode = checkForNull(singleData.get( "LOCATION_ID" ));
                                String locationName = checkForNull(singleData.get( "LOCATION_NAME" ));
                                int studyGroupSelRow = tblStudyGroup.getSelectedRow();
                                ProtocolStudyGroupBean studyGroupBean = (ProtocolStudyGroupBean)vecStudyGroup.get(studyGroupSelRow);
                                Vector vecLocation = studyGroupBean.getLocations();
                                locationBean =
                                        (ProtocolStudyGroupLocationBean)vecLocation.get(selectedRow);
                                locationBean.setLocationId(new Integer(locCode).intValue());
                                locationBean.setLocationName(locationName);
                                locationTableModel.fireTableDataChanged();
                                tblLocation.setRowSelectionInterval(selectedRow,selectedRow);
                                //tblLocation.getModel().fireTableDataChanged();
                                //tblLocation.getModel().getRowCount()+1,tblLocation.getModel().getRowCount()+1);
                                
                            } //end of for loop
                        }//end of vSelectedPersons != null
                    }
                    //COEUSQA:3005 - Dependency between the Location Name and Location Type in IACUC - Start
                    else {
                        CoeusOptionPane.showErrorDialog(coeusMessageResources.parseMessageKey("proposal_sites_exceptionCode.1002"));
                    }
                    //COEUSQA:3005 - End
                    
                }catch(Exception e){
                    e.printStackTrace();
                }               
                
            }
        }
        // Added for COEUSQA-2624_Location fields in IACUC protocol: Allow "select type" for drop down search box or find some other way to handle this long list_end
	//COEUSQA-2633 Ability to indicate which procedures IACUC study personnel will perform and training -Start
        else if(actionSource.equals(btnAddPersons)){
             if(functionType != TypeConstants.DISPLAY_MODE){
               if(tblPersonsResponsible.isEditing()){
                   if(tblPersonsResponsible.getCellEditor() != null){
                       tblPersonsResponsible.getCellEditor().stopCellEditing();
                   }
               } 
               if(tblStudyGroup.isEditing()){
                   tblStudyGroup.getCellEditor().stopCellEditing();
               }
               int selStudyGroupRow = tblStudyGroup.getSelectedRow();
               ProtocolStudyGroupBean studyGroupBean = (ProtocolStudyGroupBean)vecStudyGroup.get(selStudyGroupRow); 
               int speciesCode = studyGroupBean.getSpeciesCode();
               int procedureCode = studyGroupBean.getProcedureCode();
               int studyGroupId = studyGroupBean.getStudyGroupId();
               try {
                   validatePerson(speciesCode, procedureCode, selStudyGroupRow);                 
               } catch (CoeusUIException ex) {
                 CoeusOptionPane.showInfoDialog( ex.getMessage() );
                 return;
               }                 
               Vector vecPersonResponsible = null;
               vecPersonResponsible =  studyGroupBean.getPersonsResponsible();
               if(vecPersonResponsible == null){
                   vecPersonResponsible = new Vector();
               }                
               Vector vecSendDataToForm = getInveStudyPersonnelData();                          
               personsResponsibleForm = new ProtocolPersonsResponsibleForm(mdiForm,
                       functionType, vecSendDataToForm, vecPersonResponsible, studyGroupId, speciesCode, procedureCode);
               
               vecPersonResponsible =  personsResponsibleForm.getSpeciesPersonResponse();
               personResponsibleTableModel.setData(vecPersonResponsible); 
               studyGroupBean.setPersonsResponsible(vecPersonResponsible);
               int lastAddedRow = tblPersonsResponsible.getRowCount()-1;                
               tblPersonsResponsible.setRowSelectionInterval(lastAddedRow,lastAddedRow);
               btnDeletePersons.setEnabled(true);
               notifyObserver(vecStudyGroup, PERSON_TYPE);
               setSaveRequired(true);

           }            
        }else if(actionSource.equals(btnDeletePersons)){
             if(functionType != TypeConstants.DISPLAY_MODE){                 
                int selectedRow = tblPersonsResponsible.getSelectedRow();
                if(selectedRow > -1){
                    int selectedOption = CoeusOptionPane.
                           showQuestionDialog(coeusMessageResources.parseMessageKey(PERSON_DELETE_CONFIRMATION),
                            CoeusOptionPane.OPTION_YES_NO,
                            CoeusOptionPane.DEFAULT_YES);
                    switch(selectedOption){
                        case CoeusOptionPane.SELECTION_YES:
                            int studyGroupSelRow = tblStudyGroup.getSelectedRow();
                            ProtocolStudyGroupBean studyGroupBean = (ProtocolStudyGroupBean)vecStudyGroup.get(studyGroupSelRow);
                            Vector vecPerson = studyGroupBean.getPersonsResponsible();
                            ProtocolPersonsResponsibleBean personResponseBean = 
                                    (ProtocolPersonsResponsibleBean)vecPerson.get(selectedRow);                            
                            personResponseBean.setAcType(TypeConstants.DELETE_RECORD);
                            if(cvPersonRespDelete == null){
                                cvPersonRespDelete = new CoeusVector();
                            }
                            cvPersonRespDelete.add(personResponseBean);                             
                            vecPerson.remove(selectedRow);
                            personResponsibleTableModel.setData(vecPerson);
                            studyGroupBean.setPersonsResponsible(vecPerson);
                            notifyObserver(vecStudyGroup, PERSON_TYPE);
                            if(selectedRow == 0 && tblPersonsResponsible.getRowCount() > 0){
                                tblPersonsResponsible.setRowSelectionInterval(ZERO_ROW_INDEX,ZERO_ROW_INDEX);
                            }
                            if(selectedRow-1 > - 1){
                                tblPersonsResponsible.setRowSelectionInterval(selectedRow-1,selectedRow-1);
                            }
                            if(vecPerson != null && vecPerson.size() < 1){
                                btnDeletePersons.setEnabled(false);
                            }
                            setSaveRequired(true);
                            break;
                        case CoeusOptionPane.SELECTION_NO:
                            break;
                        default:
                            break;
                    }
                }else{
                    CoeusOptionPane.showErrorDialog(coeusMessageResources.parseMessageKey(PERSON_SELECT_TO_DELETE));
                }
            }  
        }          
     //COEUSQA-2633 Ability to indicate which procedures IACUC study personnel will perform and training -End
    }
      
    // Added for COEUSQA-2624_Location fields in IACUC protocol: Allow "select type" for drop down search box or find some other way to handle this long list_start
    /**
     * Method used for setting Empty String to the passes aguement if the  arguement is Null.
     * @return Empty String if the value is null
     **/
    private String checkForNull( Object value ){
        return (value==null)? CoeusGuiConstants.EMPTY_STRING:value.toString();
    }
    // Added for COEUSQA-2624_Location fields in IACUC protocol: Allow "select type" for drop down search box or find some other way to handle this long list_end
    /*
     * Method to get the max study group id
     * @return maxStudyGroupId - int
     */
    private int getMaxStudyGroupId(){
        int maxStudyGroupId = 0;
        if(vecStudyGroup != null && vecStudyGroup.size() > 0){
            // Modified for COEUSQA-2774-IACUC Location disappears when user clicks "Add location" to add another one : Start
//            ProtocolStudyGroupBean studyGroupBean = (ProtocolStudyGroupBean)vecStudyGroup.get(vecStudyGroup.size()-1);
//            maxStudyGroupId = studyGroupBean.getStudyGroupId();
            for(Object obj: vecStudyGroup){
                ProtocolStudyGroupBean studyGroupBean = (ProtocolStudyGroupBean)obj;
                if(studyGroupBean.getStudyGroupId() > maxStudyGroupId){
                    maxStudyGroupId = studyGroupBean.getStudyGroupId();
                }
            }
            // COEUSQA-2774:End
        }
        return maxStudyGroupId;
    }
    
    /*
     * Method to get the max location id
     * @return maxLocationId - int
     */
    private int getMaxLocationId(){
        int maxLocationId = 0;
        int selectedRow = tblStudyGroup.getSelectedRow();
        ProtocolStudyGroupBean studyGroupBean = (ProtocolStudyGroupBean)vecStudyGroup.get(selectedRow);
        Vector vecLocation = studyGroupBean.getLocations();
        if(vecLocation != null && vecLocation.size() > 0){
            // Modified for COEUSQA-2774-IACUC Location disappears when user clicks "Add location" to add another one : Start
//            ProtocolStudyGroupLocationBean locationBean = 
//                    (ProtocolStudyGroupLocationBean)vecLocation.get(vecLocation.size()-1);
//            maxLocationId = locationBean.getStudyGroupLocationId();
            for(Object obj: vecLocation){
                ProtocolStudyGroupLocationBean locationBean = (ProtocolStudyGroupLocationBean)obj;
                if(locationBean.getStudyGroupLocationId() > maxLocationId){
                    maxLocationId = locationBean.getStudyGroupLocationId();
                }
            }
            // COEUSQA-2774 : End
        }
        return maxLocationId;
    }
    
    public void valueChanged(javax.swing.event.ListSelectionEvent listSelectionEvent) {
        // Modified for COEUSQA-2625_Add another level of detail for Location in IACUC protocol_start
        if(tblLocation.isEditing()){
            if(tblLocation.getCellEditor() != null){
                tblLocation.getCellEditor().stopCellEditing();
            }
        }
        // Modified for COEUSQA-2625_Add another level of detail for Location in IACUC protocol_end
        int rowIndex = tblStudyGroup.getSelectedRow();
        if(rowIndex > -1){
            ProtocolStudyGroupBean studyGroupBean = (ProtocolStudyGroupBean)vecStudyGroup.get(rowIndex);
            CustomElementsForm othersForm = (CustomElementsForm)hmCustomForm.get(studyGroupBean.getStudyGroupId()+CoeusGuiConstants.EMPTY_STRING);
            studyGroupBean.setOtherDetails(othersForm.getOtherColumnElementData());
            loadOtherDetails(studyGroupBean);
            Vector vecLocation = studyGroupBean.getLocations();
            loadLocationDetails(vecLocation);
            //Added for COEUSQA-2633 Ability to indicate which procedures IACUC study personnel will perform and training -Start
            Vector vecPersonsData = studyGroupBean.getPersonsResponsible();
            loadPersonsResponsibleDetails(vecPersonsData);            
            //Added for COEUSQA-2633 Ability to indicate which procedures IACUC study personnel will perform and training -End
        }
        if(functionType != CoeusGuiConstants.DISPLAY_MODE &&
                functionType != CoeusGuiConstants.AMEND_MODE){
            btnAddLocation.setEnabled(true);
            //Added for COEUSQA-2633 Ability to indicate which procedures IACUC study personnel will perform and training -Start
            btnAddPersons.setEnabled(true);
            //Added for COEUSQA-2633 Ability to indicate which procedures IACUC study personnel will perform and training -End
            if( tblLocation.getRowCount() > 0 ){
                btnDeleteLocation.setEnabled( true );
                // Added for COEUSQA-2624_Location fields in IACUC protocol: Allow "select type" for drop down search box or find some other way to handle this long list_start
                btnLocationName.setEnabled( true );
                btnLocationType.setEnabled( true );
                // Added for COEUSQA-2624_Location fields in IACUC protocol: Allow "select type" for drop down search box or find some other way to handle this long list_end
            }else{
                btnDeleteLocation.setEnabled( false );
                // Added for COEUSQA-2624_Location fields in IACUC protocol: Allow "select type" for drop down search box or find some other way to handle this long list_start
                btnLocationName.setEnabled( false );
                btnLocationType.setEnabled( false );
                // Added for COEUSQA-2624_Location fields in IACUC protocol: Allow "select type" for drop down search box or find some other way to handle this long list_end
            }
            //Added for COEUSQA-2633 Ability to indicate which procedures IACUC study personnel will perform and training -Start
            if( tblPersonsResponsible.getRowCount() > 0 ){
                btnDeletePersons.setEnabled( true );
            }else{
                btnDeletePersons.setEnabled( false );
            }
            //Added for COEUSQA-2633 Ability to indicate which procedures IACUC study personnel will perform and training -End
        }
        

    }
    
    /*
     * Method to load the locations 
     */
    public void loadLocationDetails(Vector vecLocation){
        if(vecLocation == null){
            vecLocation = new Vector();
        }
        locationTableModel.setData(vecLocation);
        if(vecLocation != null && vecLocation.size() > 0){
            tblLocation.setRowSelectionInterval(0,0);
        }
        setTableEditors();
    }
    
    /*
     * Method to load the custom elements for a study group
     * @param ProtocolStudyGroupBean
     */
    private void loadOtherDetails(ProtocolStudyGroupBean studyGroupBean){
        customElementsForm = (CustomElementsForm)hmCustomForm.get(studyGroupBean.getStudyGroupId()+CoeusGuiConstants.EMPTY_STRING);
        if(customElementsForm ==  null){
            customElementsForm = new CustomElementsForm(custFunctionType,true,new Vector());
        }
        Vector vecotherDetails = studyGroupBean.getOtherDetails();
        if(vecotherDetails == null || vecotherDetails.size() < 1){
            if(studyGroupBean.getProcedureCode() > 0 && hmCustomDataForCat!=null ){
                vecotherDetails = (Vector)hmCustomDataForCat.get(studyGroupBean.getProcedureCategoryCode()+CoeusGuiConstants.EMPTY_STRING);
                studyGroupBean.setOtherDetails(vecotherDetails);
            }
        }
        if(vecotherDetails == null){
            vecotherDetails = new Vector();
        }
        customElementsForm.setPersonColumnValues(vecotherDetails);
        pnlCustomElements = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnlCustomElements.add(customElementsForm);
        
        scrnPnCustomElements.setViewportView(pnlCustomElements);
        if(TypeConstants.DISPLAY_MODE != functionType && TypeConstants.DISPLAY_MODE != custFunctionType){
            customElementsForm.setTabFocus();
        }
    }
    
    /*
     * Method to get the save required flag
     */
    public boolean isSaveRequired() {
        if(tblStudyGroup.isEditing()){
            if(tblStudyGroup.getCellEditor() != null){
                tblStudyGroup.getCellEditor().stopCellEditing();
            }
        }
        if(tblLocation.isEditing()){
            if(tblLocation.getCellEditor() != null){
                tblLocation.getCellEditor().stopCellEditing();
            }
        }
        if(functionType == TypeConstants.AMEND_MODE &&
                vecStudyGroup != null && vecStudyGroup.size() > 0){
                return true;
        }
        if(!saveRequired && vecStudyGroup != null && vecStudyGroup.size() > 0){
            for(int index=0;index<vecStudyGroup.size();index++){
                ProtocolStudyGroupBean studyGroupBean = (ProtocolStudyGroupBean)vecStudyGroup.get(index);
                CustomElementsForm otherForm = (CustomElementsForm)hmCustomForm.get(studyGroupBean.getStudyGroupId()+CoeusGuiConstants.EMPTY_STRING);
                saveRequired = otherForm.isSaveRequired();
                if(saveRequired){
                    break;
                }
            }
        }
        return saveRequired;
    }
    
    
    
    /** Method used to set the saveRequired flag.
     * @param saveRequired New value of property saveRequired.
     */
    public void setSaveRequired(boolean saveRequired) {
        this.saveRequired = saveRequired;
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
    
    /*
     * Table model for Study group table
     *
     */
    class StudyGroupTableModel extends DefaultTableModel {
        private Vector vecData;
        //Added species group column and made species column non editable
        //CoeusQA-2551:Rework how user enters species,study groups and procedures in IACUC protocols
        private String colNames[] =
        {"","Species Group","Species","Procedure Category","Procedure","Pain Category","Count"};
        private Class colTypes[] =
        {Object.class,String.class,String.class, String.class, String.class,String.class, Integer.class};
        
        public boolean isCellEditable(int row, int column) {
            if(functionType != TypeConstants.DISPLAY_MODE && 
                    functionType != TypeConstants.AMEND_MODE &&
                    column != STUDY_GROUP_POINTER_COLUMN &&
                    column != SPECIES_COLUMN &&
                    // Added for COEUSQA-2627_Move pain category in IACUC protocol to Protocol SpeciesGroup level_start
                    column != PAIN_CATEGORY_COLUMN){
                    // Added for COEUSQA-2627_Move pain category in IACUC protocol to Protocol SpeciesGroup level_end
                return true;
            }else{
                return false;
            }
        }
        
        public int getColumnCount() {
            return colNames.length;
        }
        
        public Object getValueAt(int row, int column) {
            if(vecStudyGroup != null & vecStudyGroup.size() > 0){
                ProtocolStudyGroupBean protocolStudyGroupBean = (ProtocolStudyGroupBean)vecStudyGroup.get(row);
                switch(column) {
                    //CoeusQQA-2551:Rework how user enters species,study groups and procedures in IACUC protocols
                    case SPECIES_GROUP_NAME_COLUMN:
                        
                            ComboBoxBean cbGroup = new ComboBoxBean(CoeusGuiConstants.EMPTY_STRING,CoeusGuiConstants.EMPTY_STRING);
                            Vector vctGroup = cvSpeciesGroupNames.filter(new Equals("code", String.valueOf(protocolStudyGroupBean.getSpeciesId())));
                            if(vctGroup!=null && vctGroup.size() > 0){
                                cbGroup = (ComboBoxBean)vctGroup.get(0);
                            }
                            return cbGroup;
                        
                    case SPECIES_COLUMN:
                            String speciesName = null;
                            ProtocolSpeciesBean speciesBean = (ProtocolSpeciesBean)hmGroup.get(String.valueOf(protocolStudyGroupBean.getSpeciesId()));
                            if(speciesBean!=null){
                                speciesName = speciesBean.getSpeciesName();
                                //Added for COEUSQA-2633 Ability to indicate which procedures IACUC study personnel will perform and training -Start
                                protocolStudyGroupBean.setSpeciesCode(speciesBean.getSpeciesCode());
                                //Added for COEUSQA-2633 Ability to indicate which procedures IACUC study personnel will perform and training -End
                            }                            
                            return speciesName;
                     //CoeusQQA-2551:Rework how user enters species,study groups and procedures in IACUC protocols:   End
                    case PROCEDURE_CATEGORY_COLUMN:
                        if(TypeConstants.DISPLAY_MODE != functionType){
                            ComboBoxBean comboBoxBean = new ComboBoxBean(CoeusGuiConstants.EMPTY_STRING,CoeusGuiConstants.EMPTY_STRING);
                            int procedureCatCode = protocolStudyGroupBean.getProcedureCategoryCode();
                            Vector filteredVector = cvProcCategoryType.filter(new Equals("code", CoeusGuiConstants.EMPTY_STRING+procedureCatCode));
                            if(filteredVector!=null && filteredVector.size() > 0){
                                comboBoxBean = (ComboBoxBean)filteredVector.get(0);
                            }
                            return comboBoxBean;
                        }else{
                            return protocolStudyGroupBean.getProcedureCategoryName();
                        }
                    case PROCEDURE_COLUMN:
                        if(TypeConstants.DISPLAY_MODE != functionType){
                            ComboBoxBean comboBoxBean = new ComboBoxBean(CoeusGuiConstants.EMPTY_STRING,CoeusGuiConstants.EMPTY_STRING);
                            int procedureCatCode = protocolStudyGroupBean.getProcedureCategoryCode();
                            CoeusVector cvProcedureType = new CoeusVector();
                            if(hmProcedureForCat != null && hmProcedureForCat.size()>0 && procedureCatCode > 0){
                                cvProcedureType = (CoeusVector)hmProcedureForCat.get(procedureCatCode+CoeusGuiConstants.EMPTY_STRING);
                            }
                            int procedureCode = protocolStudyGroupBean.getProcedureCode();
                            Vector filteredVector = cvProcedureType.filter(new Equals("code", CoeusGuiConstants.EMPTY_STRING+procedureCode));
                            if(filteredVector!=null && filteredVector.size() > 0){
                                comboBoxBean = (ComboBoxBean)filteredVector.get(0);
                            }
                            return comboBoxBean;
                        }else{
                            return protocolStudyGroupBean.getProcedureName();
                        }
                    case PAIN_CATEGORY_COLUMN: 
                        // Modified for COEUSQA-2627_Move pain category in IACUC protocol to Protocol SpeciesGroup level_start
                        /*if(TypeConstants.DISPLAY_MODE != functionType){
                            ComboBoxBean comboBoxBean = new ComboBoxBean(CoeusGuiConstants.EMPTY_STRING,CoeusGuiConstants.EMPTY_STRING);
                            int painCatCode = protocolStudyGroupBean.getPainCategoryCode();
                            Vector filteredVector = cvPainCategoryType.filter(new Equals("code", CoeusGuiConstants.EMPTY_STRING+painCatCode));
                            if(filteredVector!=null && filteredVector.size() > 0){
                                comboBoxBean = (ComboBoxBean)filteredVector.get(0);
                            }
                            return comboBoxBean;
                        }else{
                            return protocolStudyGroupBean.getPainCategoryName();
                        }*/
                        String painCategoryName = CoeusGuiConstants.EMPTY_STRING;
                        if(TypeConstants.DISPLAY_MODE != functionType){
                            ProtocolSpeciesBean protoSpeciesBean = (ProtocolSpeciesBean)hmGroup.get(String.valueOf(protocolStudyGroupBean.getSpeciesId()));
                            if(protoSpeciesBean!=null){
                                int painCatCode = protoSpeciesBean.getPainCategoryCode();
                                if(cvPainCategoryType != null){
                                    Vector filteredVector = cvPainCategoryType.filter(new Equals("code", CoeusGuiConstants.EMPTY_STRING+painCatCode));
                                    if(filteredVector!=null && filteredVector.size() > 0){
                                        ComboBoxBean comboBoxBean = (ComboBoxBean)filteredVector.get(0);
                                        painCategoryName = comboBoxBean.getDescription();
                                    }
                                }
                                
                            }
                            return painCategoryName;
                        }else{
                            ProtocolSpeciesBean protoSpeciesBean = (ProtocolSpeciesBean)hmGroup.get(String.valueOf(protocolStudyGroupBean.getSpeciesId()));
                            if(protoSpeciesBean!=null){
                                return protoSpeciesBean.getPainCategoryName();
                            }
                        }
                         // Modified for COEUSQA-2627_Move pain category in IACUC protocol to Protocol SpeciesGroup level_end   
                    case COUNT_COLUMN:
                        return new Integer(protocolStudyGroupBean.getSpeciesCount());
                        
                }
            }
            return CoeusGuiConstants.EMPTY_STRING;
        }
        
        public String getColumnName(int colIndex) {
            return colNames[colIndex];
        }
        
        public Class getColumnClass(int colIndex) {
            return colTypes[colIndex];
        }
        
        public void setData(Vector cvData) {
            this.dataVector = cvData;
            this.vecData = cvData;
            fireTableDataChanged();
        }
        
        public void setValueAt(Object value, int row, int column) {
            if(TypeConstants.DISPLAY_MODE == functionType ||
                    value == null ||
                    vecStudyGroup == null ||
                    (vecStudyGroup != null && vecStudyGroup.size() < 1)){
                return;
            }
            ProtocolStudyGroupBean studyGroupBean = (ProtocolStudyGroupBean)vecStudyGroup.get(row);
            switch(column){
                //CoeusQA-2551:Rework how user enters species,study groups and procedures in IACUC protocols
                case SPECIES_GROUP_NAME_COLUMN:
                    ComboBoxBean studyGroupComboBoxBean = (ComboBoxBean)value;
                    int speciesId = 0;
                    //Added for COEUSQA-2633 Ability to indicate which procedures IACUC study personnel will perform and training -Start
                    int speciesCode = 0;
                    //Added for COEUSQA-2633 Ability to indicate which procedures IACUC study personnel will perform and training -End
                    if(studyGroupComboBoxBean != null){
                        String speciesGroupId = studyGroupComboBoxBean.getCode();
                        ProtocolSpeciesBean species = (ProtocolSpeciesBean)hmGroup.get(speciesGroupId);
                        if(species!=null){
                            speciesId = species.getSpeciesId();
                            //Added for COEUSQA-2633 Ability to indicate which procedures IACUC study personnel will perform and training -Start
                            speciesCode = species.getSpeciesCode();
                            //Added for COEUSQA-2633 Ability to indicate which procedures IACUC study personnel will perform and training -End
                        }
                    }
                     studyGroupBean.setSpeciesId(speciesId);
                     //Added for COEUSQA-2633 Ability to indicate which procedures IACUC study personnel will perform and training -Start
                     //observable.notifyObservers(vecStudyGroup);
                     studyGroupBean.setSpeciesCode(speciesCode);
                     notifyObserver(vecStudyGroup, SPECIES_TYPE);
                     loadPersonsResponsibleDetails(studyGroupBean.getPersonsResponsible());
                     //Added for COEUSQA-2633 Ability to indicate which procedures IACUC study personnel will perform and training -End
                     break;
                case SPECIES_COLUMN :
                    break;
                //CoeusQA-2551 End
                case PROCEDURE_CATEGORY_COLUMN :
                    ComboBoxBean procCatComboBoxBean = (ComboBoxBean)value;
                    if(procCatComboBoxBean != null && !CoeusGuiConstants.EMPTY_STRING.equals(procCatComboBoxBean.getCode())){
                        studyGroupBean.setProcedureCategoryCode(Integer.parseInt(procCatComboBoxBean.getCode()));
                        studyGroupBean.setProcedureCategoryName(procCatComboBoxBean.getDescription());
                    }else{
                        studyGroupBean.setProcedureCategoryCode(0);
                        studyGroupBean.setProcedureCategoryName(CoeusGuiConstants.EMPTY_STRING);
                    }
                    break;
                case PROCEDURE_COLUMN :
                    ComboBoxBean procedureComboBoxBean = (ComboBoxBean)value;
                    if(procedureComboBoxBean != null && !CoeusGuiConstants.EMPTY_STRING.equals(procedureComboBoxBean.getCode())){
                        studyGroupBean.setProcedureCode(Integer.parseInt(procedureComboBoxBean.getCode()));
                        studyGroupBean.setProcedureName(procedureComboBoxBean.getDescription());
                    }else{
                        studyGroupBean.setProcedureCode(0);
                        studyGroupBean.setProcedureName(CoeusGuiConstants.EMPTY_STRING);
                    }
                    //Added for COEUSQA-2633 Ability to indicate which procedures IACUC study personnel will perform and training -Start                      
                    loadPersonsResponsibleDetails(studyGroupBean.getPersonsResponsible());
                    //Added for COEUSQA-2633 Ability to indicate which procedures IACUC study personnel will perform and training -End
                    break;
                case PAIN_CATEGORY_COLUMN :
                    // Commented for COEUSQA-2627_Move pain category in IACUC protocol to Protocol SpeciesGroup level_start
                    /*ComboBoxBean painCatComboBoxBean = (ComboBoxBean)value;
                    if(painCatComboBoxBean != null && !CoeusGuiConstants.EMPTY_STRING.equals(painCatComboBoxBean.getCode())){
                        studyGroupBean.setPainCategoryCode(Integer.parseInt(painCatComboBoxBean.getCode()));
                        studyGroupBean.setPainCategoryName(painCatComboBoxBean.getDescription());
                    }else{
                        studyGroupBean.setPainCategoryCode(0);
                        studyGroupBean.setPainCategoryName(CoeusGuiConstants.EMPTY_STRING);
                        
                    }*/
                    // Commented for COEUSQA-2627_Move pain category in IACUC protocol to Protocol SpeciesGroup level_end
                    break;
                    
                case COUNT_COLUMN :
                    if(value == null || CoeusGuiConstants.EMPTY_STRING.equals(value)){
                        value = "0";
                    }
                    int speciesCount = Integer.parseInt(value.toString().trim());
                    studyGroupBean.setSpeciesCount(speciesCount);
                    break;
                default:
                    break;
                    
            }
            
        }
        
    }
    
    /*
     * Table model for Study group location table
     *
     */
    class LocationTableModel extends DefaultTableModel {
        Vector vecData;
        // Commented and Modified for COEUSQA-2625_Add another level of detail for Location in IACUC protocol_start
//        private String colNames[] =
//                                   {"","Location Type","Location Name","Description"};
//        private Class colTypes[] =
//                                   {Object.class,String.class, String.class, String.class};
        private String colNames[] = {"","Location Type","Location Name","Room","Description"};
        private Class colTypes[] =  {Object.class,String.class, String.class, Object.class, String.class};
        // Commented and Modified for COEUSQA-2625_Add another level of detail for Location in IACUC protocol_end
        public boolean isCellEditable(int row, int column) {
            if(functionType != TypeConstants.DISPLAY_MODE &&
                    functionType != TypeConstants.AMEND_MODE &&
                    column != LOCATION_POINTER_COLUMN){
                return true;
            }else{
                return false;
            }
        }
        
        public int getColumnCount() {
            return colNames.length;
        }
        
        public Object getValueAt(int row, int column) {
           //COEUSQA:3005 - Dependency between the Location Name and Location Type in IACUC - Start
           if(vecStudyGroup != null & vecStudyGroup.size() > 0){
           //COEUSQA:3005 - End
            ProtocolStudyGroupLocationBean locationBean = (ProtocolStudyGroupLocationBean)vecData.get(row);
            switch(column) {
                case LOCATION_TYPE_COLUMN:
                    if(TypeConstants.DISPLAY_MODE != functionType){
                        ComboBoxBean comboBoxBean = new ComboBoxBean(CoeusGuiConstants.EMPTY_STRING,CoeusGuiConstants.EMPTY_STRING);
                        int locationTypeCode = locationBean.getLocationTypeCode();
                        Vector filteredVector = cvLocationType.filter(new Equals("code", CoeusGuiConstants.EMPTY_STRING+locationTypeCode));
                        if(filteredVector!=null && filteredVector.size() > 0){
                            comboBoxBean = (ComboBoxBean)filteredVector.get(0);
                        }
                        return comboBoxBean;
                    }else{
                        return locationBean.getLocationTypeDesc();
                    }
                case LOCATION_NAME_COLUMN:
                    //COEUSQA:3005 - Dependency between the Location Name and Location Type in IACUC - Start
//                    if(TypeConstants.DISPLAY_MODE != functionType){
//                        ComboBoxBean comboBoxBean = new ComboBoxBean(CoeusGuiConstants.EMPTY_STRING,CoeusGuiConstants.EMPTY_STRING);
//                        int locationId = locationBean.getLocationId();
//                        Vector filteredVector = cvLocationName.filter(new Equals("code", CoeusGuiConstants.EMPTY_STRING+locationId));
//                        if(filteredVector!=null && filteredVector.size() > 0){
//                            comboBoxBean = (ComboBoxBean)filteredVector.get(0);
//                        }
//                        return comboBoxBean;
//                    }else{
//                        return locationBean.getLocationName();
//                    }
                    
                    if(TypeConstants.DISPLAY_MODE != functionType){
                            ComboBoxBean comboBoxBean = new ComboBoxBean(CoeusGuiConstants.EMPTY_STRING,CoeusGuiConstants.EMPTY_STRING);
                            int locationTypeCode = locationBean.getLocationTypeCode();
                            CoeusVector cvLocationTypeName = new CoeusVector();
                            if(hmLocationForType != null && hmLocationForType.size()>0 && locationTypeCode > 0){
                                cvLocationTypeName = (CoeusVector)hmLocationForType.get(locationTypeCode+CoeusGuiConstants.EMPTY_STRING); // Since locationTypeCode is integer value, To get a value from hmLocationForType(hashMap) adding EMPTY_STRING to make it as String
                            }
                            int locationId = locationBean.getLocationId();
                            Vector filteredVector = cvLocationTypeName.filter(new Equals("code", CoeusGuiConstants.EMPTY_STRING+locationId));
                            if(filteredVector!=null && filteredVector.size() > 0){
                                comboBoxBean = (ComboBoxBean)filteredVector.get(0);
                            }
                            return comboBoxBean;
                        }else{
                            return locationBean.getLocationName();
                        }
                    //COEUSQA:3005 - End
                    
                case DESCRIPTION_COLUMN:
                    return locationBean.getStudyGroupLocationName();
               // Added for COEUSQA-2625_Add another level of detail for Location in IACUC protocol_start
               case LOCATION_ROOM_COLUMN:
                    return locationBean.getLocationRoom();
               // Added for COEUSQA-2625_Add another level of detail for Location in IACUC protocol_end
            }
          }
            return CoeusGuiConstants.EMPTY_STRING;
        }
        
        public String getColumnName(int colIndex) {
            return colNames[colIndex];
        }
        
        public Class getColumnClass(int colIndex) {
            return colTypes[colIndex];
        }
        
        public void setData(Vector cvData) {
            dataVector = cvData;
            this.vecData = cvData;
            vecLocationData = cvData;
            fireTableDataChanged();
        }
        
        public void setValueAt(Object value, int row, int column) {
            if(TypeConstants.DISPLAY_MODE == functionType ||
                    value == null ||
                    vecData == null ||
                    (vecData != null && vecData.size() < 1)){
                return;
            }
            
            ProtocolStudyGroupLocationBean locationBean = (ProtocolStudyGroupLocationBean)vecData.get(row);
            switch(column) {
                case LOCATION_TYPE_COLUMN:
                    ComboBoxBean locationTypeComboBoxBean = (ComboBoxBean)value;
                    if(locationTypeComboBoxBean != null && !CoeusGuiConstants.EMPTY_STRING.equals(locationTypeComboBoxBean.getCode())){
                        locationBean.setLocationTypeCode(Integer.parseInt(locationTypeComboBoxBean.getCode()));
                        locationBean.setLocationTypeDesc(locationTypeComboBoxBean.getDescription());
                    }else{
                        locationBean.setLocationTypeCode(0);
                        locationBean.setLocationTypeDesc(CoeusGuiConstants.EMPTY_STRING);
                    }
                    break;
                case LOCATION_NAME_COLUMN:
                    ComboBoxBean locationNameComboBoxBean = (ComboBoxBean)value;
                    if(locationNameComboBoxBean != null && !CoeusGuiConstants.EMPTY_STRING.equals(locationNameComboBoxBean.getCode())){
                        locationBean.setLocationId(Integer.parseInt(locationNameComboBoxBean.getCode()));
                        locationBean.setLocationName(locationNameComboBoxBean.getDescription());
                    }else{
                        locationBean.setLocationId(0);
                        locationBean.setLocationName(CoeusGuiConstants.EMPTY_STRING);
                    }
                    break;
                case DESCRIPTION_COLUMN:
                    locationBean.setStudyGroupLocationName(value.toString().trim());
                    break;
                // Added for COEUSQA-2625_Add another level of detail for Location in IACUC protocol_start
                case LOCATION_ROOM_COLUMN:
                    locationBean.setLocationRoom(value.toString().trim());
                    break;
               // Added for COEUSQA-2625_Add another level of detail for Location in IACUC protocol_end
            }
        }
        
    }
    
    /*
     * Cell editor class for study group combo boxes
     *
     */  
    public class StudyGroupComboEditor extends AbstractCellEditor implements TableCellEditor{
        private JComboBox cmbSpeciesGroup;
        private JComboBox cmbProcCategory;
        private JComboBox cmbProcedure;
        private JComboBox cmPainCategory;
        private int column,row;
        StudyGroupComboEditor(int column){
            //CoeusQA-2551:Rework how user enters species,study groups and procedures in IACUC protocols
            cmbSpeciesGroup = new JComboBox();
            cmbSpeciesGroup.addItemListener(new ItemListener() {
                public void itemStateChanged(ItemEvent e) {
                    if(e.getStateChange() == ItemEvent.SELECTED){
                        stopCellEditing();
                        studyGroupTableModel.fireTableCellUpdated(tblStudyGroup.getSelectedRow(),SPECIES_COLUMN);
                        // Added for COEUSQA-2627_Move pain category in IACUC protocol to Protocol SpeciesGroup level_start
                        studyGroupTableModel.fireTableCellUpdated(tblStudyGroup.getSelectedRow(),PAIN_CATEGORY_COLUMN);
                        // Added for COEUSQA-2627_Move pain category in IACUC protocol to Protocol SpeciesGroup level_end
                    }
                }
            });
            cmbSpeciesGroup.addMouseListener(new MouseAdapter() {
                public void mouseClicked(){
                    populateSpeciesGroupComboBox();
                }
});
            //CoeusQA-2551:End
            cmbProcCategory = new JComboBox();
            populateProcCategoryComboBox();
            cmbProcedure = new JComboBox();
            populateProcedureComboBox();
            cmbProcCategory.addItemListener(new ItemListener() {
                public void itemStateChanged(ItemEvent e) {
                    
                    if(tblLocation.isEditing()){
                        tblStudyGroup.getCellEditor().stopCellEditing();
                    }
                    if(tblLocation.isEditing()){
                        tblLocation.getCellEditor().stopCellEditing();
                    }
                    if(e.getStateChange() == e.DESELECTED){
                        oldProcedureCatItem = e.getItem();
                        return ;
                    }
                    if(itemChangeCancelling){
                        itemChangeCancelling = false;
                        return;
                    }
                    int editingRow = tblStudyGroup.getEditingRow();
                    if(editingRow != -1){
                        if(oldProcedureCatItem != null &&
                                !CoeusGuiConstants.EMPTY_STRING.equals(((ComboBoxBean)oldProcedureCatItem).getCode())){
                            
                            int option = CoeusOptionPane.showQuestionDialog(coeusMessageResources.parseMessageKey(STUDY_GROUP_CUST_DELETE_CONFIRMATION)
                            ,CoeusOptionPane.OPTION_YES_NO,2);
                            switch( option ) {
                                case (CoeusOptionPane.SELECTION_YES):
                                    ComboBoxBean procedureCatBean = (ComboBoxBean)e.getItem();
                                    loadLocOtherDetails(procedureCatBean, CoeusOptionPane.SELECTION_YES,editingRow);
                                    tblStudyGroup.setRowSelectionInterval(editingRow,editingRow);
                                    break;
                                case(CoeusOptionPane.SELECTION_NO):
                                    itemChangeCancelling = true;
                                    cmbProcCategory.setSelectedItem(oldProcedureCatItem);
                                    loadLocOtherDetails((ComboBoxBean)oldProcedureCatItem, CoeusOptionPane.SELECTION_NO,editingRow);
                                    tblStudyGroup.setRowSelectionInterval(editingRow,editingRow);
                                    break;
                                default:
                                    break;
                            }
                            
                            
                        }else{
                            ComboBoxBean procedureCatBean = (ComboBoxBean)e.getItem();
                            loadLocOtherDetails(procedureCatBean, CoeusOptionPane.SELECTION_YES,editingRow);
                            tblStudyGroup.setRowSelectionInterval(editingRow,editingRow);
                        }
                    }
                }
            });
            
            //Added for COEUSQA-2633 Ability to indicate which procedures IACUC study personnel will perform and training -Start                      
            cmbProcedure.addItemListener(new ItemListener() {
                public void itemStateChanged(ItemEvent e) {                                        
                    if(e.getStateChange() == e.DESELECTED){
                        oldProcedureCatItem = e.getItem();
                        return ;
                    }
                    if(itemChangeCancelling){
                        itemChangeCancelling = false;
                        return;
                    }
                    int editingRow = tblStudyGroup.getEditingRow();
                    if(editingRow != -1){
                        if(oldProcedureCatItem != null &&
                                !CoeusGuiConstants.EMPTY_STRING.equals(((ComboBoxBean)oldProcedureCatItem).getCode())){                                                                                                                                        
                            ComboBoxBean procedureCatBean = (ComboBoxBean)e.getItem();
                            tblStudyGroup.getCellEditor().stopCellEditing();
                            ProtocolStudyGroupBean studyGroupBean = (ProtocolStudyGroupBean)vecStudyGroup.get(row);
                            studyGroupBean.setProcedureCode(procedureCatBean.getCode() == "" ? 0 : Integer.parseInt(procedureCatBean.getCode()));
                            loadPersonsResponsibleDetails(studyGroupBean.getPersonsResponsible());
                            tblStudyGroup.setRowSelectionInterval(editingRow,editingRow);                                                        
                        }
                    }
                }
            });
            //Added for COEUSQA-2633 Ability to indicate which procedures IACUC study personnel will perform and training -End            
            // Commented for COEUSQA-2627_Move pain category in IACUC protocol to Protocol SpeciesGroup level_start
            /*cmPainCategory = new JComboBox();
            populatePainCateComboBox();*/
            // Commented for COEUSQA-2627_Move pain category in IACUC protocol to Protocol SpeciesGroup level_end
        }
        public java.awt.Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            ComboBoxBean comboBoxBean = null;
            this.column = column;
            this.row = row;
            if(value instanceof ComboBoxBean){
                comboBoxBean = (ComboBoxBean)value;
            }
            switch(column){
                //Modified with CoeusQA-2551:Rework how user enters species,study groups and procedures in IACUC protocols
                case SPECIES_GROUP_NAME_COLUMN:
                    populateSpeciesGroupComboBox();
                    cmbSpeciesGroup.setSelectedItem(value);
                    return cmbSpeciesGroup;
                //CoeusQA-2551:End
                case PROCEDURE_CATEGORY_COLUMN:
                    cmbProcCategory.setSelectedItem(value);
                    return cmbProcCategory;
                case PROCEDURE_COLUMN:
                    ProtocolStudyGroupBean studyGroupBean = (ProtocolStudyGroupBean)vecStudyGroup.get(row);
                    if(hmProcedureForCat != null && hmProcedureForCat.size()>0){
                        cvProcedureType = (CoeusVector)hmProcedureForCat.get(studyGroupBean.getProcedureCategoryCode()+CoeusGuiConstants.EMPTY_STRING);
                    }else{
                        cvProcedureType = new CoeusVector();
                    }
                    populateProcedureComboBox();
                    cmbProcedure.setSelectedItem(value);
                    return cmbProcedure;
                case PAIN_CATEGORY_COLUMN:
                    cmPainCategory.setSelectedItem(value);
                    return cmPainCategory;
            }
            return new CoeusComboBox();
        }

        public Object getCellEditorValue() {            
            switch(column){
                case SPECIES_GROUP_NAME_COLUMN:
                    return cmbSpeciesGroup.getSelectedItem();
                case PROCEDURE_CATEGORY_COLUMN:
                    return cmbProcCategory.getSelectedItem();
                case PROCEDURE_COLUMN:
                    return cmbProcedure.getSelectedItem();
                // Commented for COEUSQA-2627_Move pain category in IACUC protocol to Protocol SpeciesGroup level_start
                /*case PAIN_CATEGORY_COLUMN:
                    return cmPainCategory.getSelectedItem();*/
                // Commented for COEUSQA-2627_Move pain category in IACUC protocol to Protocol SpeciesGroup level_end
            }
            return CoeusGuiConstants.EMPTY_STRING;
        }
        
//      Modified with CoeusQA-2551:Rework how user enters species,study groups and procedures in IACUC protocols
//        private void populateSpeciesComboBox(){
//            ComboBoxBean comboBoxBean;
//            cmbSpecies.removeAllItems();
//            cmbSpecies.addItem(new ComboBoxBean(CoeusGuiConstants.EMPTY_STRING,CoeusGuiConstants.EMPTY_STRING));
//            if(cvSpeciesType != null && cvSpeciesType.size() > 0){
//                for(int index = 0; index < cvSpeciesType.size(); index++) {
//                    comboBoxBean = (ComboBoxBean)cvSpeciesType.get(index);
//                    cmbSpecies.addItem(comboBoxBean);
//                }
//            }
//        }
        
        private void populateSpeciesGroupComboBox(){
            ComboBoxBean comboBoxBean;
            cmbSpeciesGroup.removeAllItems();
            cmbSpeciesGroup.addItem(new ComboBoxBean(CoeusGuiConstants.EMPTY_STRING,CoeusGuiConstants.EMPTY_STRING));
            if(cvSpeciesGroupNames != null && cvSpeciesGroupNames.size() > 0){
                for(int index = 0; index < cvSpeciesGroupNames.size(); index++) {
                    comboBoxBean = (ComboBoxBean)cvSpeciesGroupNames.get(index);
                    cmbSpeciesGroup.addItem(comboBoxBean);
                }
            }
        }
//        CoeusQA-2551:End    
        private void populateProcCategoryComboBox(){
            ComboBoxBean comboBoxBean;
            cmbProcCategory.addItem(new ComboBoxBean(CoeusGuiConstants.EMPTY_STRING,CoeusGuiConstants.EMPTY_STRING));
            if(cvProcCategoryType != null && cvProcCategoryType.size() > 0){
                for(int index = 0; index < cvProcCategoryType.size(); index++) {
                    comboBoxBean = (ComboBoxBean)cvProcCategoryType.get(index);
                    cmbProcCategory.addItem(comboBoxBean);
                }
            }
        }
        
        private void populateProcedureComboBox(){
            ComboBoxBean comboBoxBean;
            if(cmbProcedure != null){
                cmbProcedure.removeAllItems();
            }
            cmbProcedure.addItem(new ComboBoxBean(CoeusGuiConstants.EMPTY_STRING,CoeusGuiConstants.EMPTY_STRING));
            if(cvProcedureType != null && cvProcedureType.size() > 0){
                for(int index = 0; index < cvProcedureType.size(); index++) {
                    comboBoxBean = (ComboBoxBean)cvProcedureType.get(index);
                    cmbProcedure.addItem(comboBoxBean);
                }
            }
        }
        
        private void populatePainCateComboBox(){
            ComboBoxBean comboBoxBean;
            cmPainCategory.addItem(new ComboBoxBean(CoeusGuiConstants.EMPTY_STRING,CoeusGuiConstants.EMPTY_STRING));
            if(cvPainCategoryType != null && cvPainCategoryType.size() > 0){
                for(int index = 0; index < cvPainCategoryType.size(); index++) {
                    comboBoxBean = (ComboBoxBean)cvPainCategoryType.get(index);
                    cmPainCategory.addItem(comboBoxBean);
                }
            }
        }
        
        private void loadLocOtherDetails(ComboBoxBean comboBoxBean , int selectedOption, int editingRow){
            if(hmProcedureForCat != null && hmProcedureForCat.size()>0){
                cvProcedureType = (CoeusVector)hmProcedureForCat.get(comboBoxBean.getCode());
            }else{
                cvProcedureType = new CoeusVector();
            }
            populateProcedureComboBox();
            
            if(hmCustomDataForCat != null && hmCustomDataForCat.size()>0 &&
                    editingRow > -1){
                Vector vecOtherData = (Vector)hmCustomDataForCat.get(comboBoxBean.getCode());
                ProtocolStudyGroupBean studyGroupBean = (ProtocolStudyGroupBean)vecStudyGroup.get(editingRow);
                if((comboBoxBean.getCode() == null || CoeusGuiConstants.EMPTY_STRING.equals(comboBoxBean.getCode())) ||
                        studyGroupBean.getProcedureCategoryCode() !=
                        Integer.parseInt(comboBoxBean.getCode()) &&
                        selectedOption == CoeusOptionPane.SELECTION_YES){
                                         
                    studyGroupBean.setProcedureCode(0);
                    studyGroupBean.setProcedureName(CoeusGuiConstants.EMPTY_STRING);
                    studyGroupTableModel.setData(vecStudyGroup);
                }else{
                    if(comboBoxBean.getCode() == null || CoeusGuiConstants.EMPTY_STRING.equals(comboBoxBean.getCode())) {
                        studyGroupBean.setProcedureCode(0);
                        studyGroupBean.setProcedureName(CoeusGuiConstants.EMPTY_STRING);
                        studyGroupTableModel.setData(vecStudyGroup);
                    }
                     
                }
                if(comboBoxBean.getCode() == null || CoeusGuiConstants.EMPTY_STRING.equals(comboBoxBean.getCode())) {
                    vecOtherData = new Vector();
                }
                hmOthersDelete.put(studyGroupBean.getStudyGroupId()+CoeusGuiConstants.EMPTY_STRING,studyGroupBean.getOtherDetails());
                try{
                    if(selectedOption == CoeusOptionPane.SELECTION_YES){
                        studyGroupBean.setOtherDetails(vecOtherData);
                    }
                    loadOtherDetails(studyGroupBean);
                    
                }catch(Exception exc){
                    exc.printStackTrace();;
                }
                setSaveRequired(true);
                //Added for COEUSQA-2633 Ability to indicate which procedures IACUC study personnel will perform and training -Start                      
                loadPersonsResponsibleDetails(studyGroupBean.getPersonsResponsible());
                //Added for COEUSQA-2633 Ability to indicate which procedures IACUC study personnel will perform and training -End
            }            
        }        
    }
    
    /*
     * Cell editor class for study group location combo boxes
     *
     */  
    public class LocationComboEditor extends AbstractCellEditor implements TableCellEditor{
        private JComboBox cmbLocationType;
        private JComboBox cmLocationName;
        private int column,row;
        
        LocationComboEditor(int column){
            cmbLocationType = new JComboBox();
            populateLocTypeComboBox();
            cmLocationName = new JComboBox();
            populateLocNameComboBox();
            
            //COEUSQA:3005 - Dependency between the Location Name and Location Type in IACUC - Start
             
             cmbLocationType.addItemListener(new ItemListener() {
                public void itemStateChanged(ItemEvent e) {
                    
                    if(e.getStateChange() == e.DESELECTED){
                        oldLocationTypeItem = e.getItem();
                        return ;
                    }
                    if(itemChangeCancelling){
                        itemChangeCancelling = false;
                        return;
                    }
                    int editingRow = tblLocation.getEditingRow();
                    if(editingRow != -1){
                        int selectedRow = tblLocation.getSelectedRow();
                        int studyGroupSelRow = tblStudyGroup.getSelectedRow();
                        ProtocolStudyGroupBean studyGroupBean = (ProtocolStudyGroupBean)vecStudyGroup.get(studyGroupSelRow);
                        Vector vecLocation = studyGroupBean.getLocations();
                        ProtocolStudyGroupLocationBean locationBean =
                                (ProtocolStudyGroupLocationBean)vecLocation.get(selectedRow);
                        locationBean.setLocationId(-1);
                        locationBean.setLocationName(null);
                        if(oldLocationTypeItem != null &&
                                !CoeusGuiConstants.EMPTY_STRING.equals(((ComboBoxBean)oldLocationTypeItem).getCode())){                            
                            ComboBoxBean locationTypeBean = (ComboBoxBean)e.getItem();
                            loadLocOtherDetails(locationTypeBean,editingRow);
                            tblLocation.setRowSelectionInterval(editingRow,editingRow);                                                       
                        }else{
                            ComboBoxBean locationTypeBean = (ComboBoxBean)e.getItem();
                            loadLocOtherDetails(locationTypeBean, editingRow);
                            tblLocation.setRowSelectionInterval(editingRow,editingRow);
                        }
                    }
                }
            });
            
            
            cmLocationName.addItemListener(new ItemListener() {
                public void itemStateChanged(ItemEvent e) {                                        
                    if(e.getStateChange() == e.DESELECTED){
                        oldLocationTypeItem = e.getItem();
                        return ;
                    }
                    if(itemChangeCancelling){
                        itemChangeCancelling = false;
                        return;
                    }
                    int editingRow = tblStudyGroup.getEditingRow();
                    if(editingRow != -1){
                        if(oldLocationTypeItem != null &&
                                !CoeusGuiConstants.EMPTY_STRING.equals(((ComboBoxBean)oldLocationTypeItem).getCode())){                                                                                                                                        
                            ComboBoxBean locationTypeBean = (ComboBoxBean)e.getItem();
                          //  tblLocation.getCellEditor().stopCellEditing();
                            ProtocolStudyGroupLocationBean locationBean = (ProtocolStudyGroupLocationBean)vecLocationData.get(row);
                            locationBean.setLocationId(locationTypeBean.getCode() == "" ? 0 : Integer.parseInt(locationTypeBean.getCode()));
                            tblLocation.setRowSelectionInterval(editingRow,editingRow);                                                        
                        }
                    }
                }
            });
            
            
            
            //COEUSQA:3005 - End
        }
        public java.awt.Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            ComboBoxBean comboBoxBean = null;
            this.column = column;
            if(value instanceof ComboBoxBean){
                comboBoxBean = (ComboBoxBean)value;
            }
            switch(column){
                case LOCATION_TYPE_COLUMN:
                    cmbLocationType.setSelectedItem(value);
                    return cmbLocationType;
                case LOCATION_NAME_COLUMN:
                    //COEUSQA:3005 - Dependency between the Location Name and Location Type in IACUC - Start
//                    cmLocationName.setSelectedItem(value);
//                    return cmLocationName;
                    ProtocolStudyGroupLocationBean locationBean = (ProtocolStudyGroupLocationBean)vecLocationData.get(row);
                    if(hmProcedureForCat != null && hmProcedureForCat.size()>0){
                        cvLocationTypeName = (CoeusVector)hmLocationForType.get(locationBean.getLocationTypeCode()+CoeusGuiConstants.EMPTY_STRING);
                    }else{
                        cvLocationTypeName = new CoeusVector();
                    }
                    populateLocationComboBox();
                    cmLocationName.setSelectedItem(value);
                    return cmLocationName;
                     //COEUSQA:3005 - End
            }
            return new CoeusComboBox();
        }
        
        public Object getCellEditorValue() {
            switch(column){
                case LOCATION_TYPE_COLUMN:
                    return cmbLocationType.getSelectedItem();
                case LOCATION_NAME_COLUMN:
                    return cmLocationName.getSelectedItem();
            }
            return CoeusGuiConstants.EMPTY_STRING;
        }
        
        private void populateLocTypeComboBox(){
            ComboBoxBean comboBoxBean;
            cmbLocationType.addItem(new ComboBoxBean(CoeusGuiConstants.EMPTY_STRING,CoeusGuiConstants.EMPTY_STRING));
            if(cvLocationType != null && cvLocationType.size() > 0){
                for(int index = 0; index < cvLocationType.size(); index++) {
                    comboBoxBean = (ComboBoxBean)cvLocationType.get(index);
                    cmbLocationType.addItem(comboBoxBean);
                }
            }
        }
        private void populateLocNameComboBox(){
            ComboBoxBean comboBoxBean;
            //COEUSQA:3005 - Dependency between the Location Name and Location Type in IACUC - Start
            if(cmLocationName != null){
                cmLocationName.removeAllItems();
            }
            //COEUSQA:3005 - End
            cmLocationName.addItem(new ComboBoxBean(CoeusGuiConstants.EMPTY_STRING,CoeusGuiConstants.EMPTY_STRING));            
//            if(cvLocationName != null && cvLocationName.size() > 0){
//                for(int index = 0; index < cvLocationName.size(); index++) {
//                    comboBoxBean = (ComboBoxBean)cvLocationName.get(index);
//                    cmLocationName.addItem(comboBoxBean);
//                }
//            }
            if(cvLocationTypeName != null && cvLocationTypeName.size() > 0){
                for(int index = 0; index < cvLocationTypeName.size(); index++) {
                    comboBoxBean = (ComboBoxBean)cvLocationTypeName.get(index);
                    cmLocationName.addItem(comboBoxBean);
                }
            }
        }
        
        //COEUSQA:3005 - Dependency between the Location Name and Location Type in IACUC - Start        
        /*
         * Loads the locations to the location combo box based on location type.
         *
         */        
         private void populateLocationComboBox(){
            ComboBoxBean comboBoxBean;
            if(cmLocationName != null){
                cmLocationName.removeAllItems();
            }
            cmLocationName.addItem(new ComboBoxBean(CoeusGuiConstants.EMPTY_STRING,CoeusGuiConstants.EMPTY_STRING));
            if(cvLocationTypeName != null && cvLocationTypeName.size() > 0){
                for(int index = 0; index < cvLocationTypeName.size(); index++) {
                    comboBoxBean = (ComboBoxBean)cvLocationTypeName.get(index);
                    cmLocationName.addItem(comboBoxBean);
                }
            }
        }
         /*
          * Loads the locations to the location combo box.
          *
          */          
         private void loadLocOtherDetails(ComboBoxBean comboBoxBean ,int editingRow){
             if(hmLocationForType != null && hmLocationForType.size()>0){
                 cvLocationTypeName = (CoeusVector)hmLocationForType.get(comboBoxBean.getCode());
             }else{
                 cvLocationTypeName = new CoeusVector();
             }
             populateLocNameComboBox();                          
             
         }
        
         
        //COEUSQA:3005 - End

    }
    /**
     * Registers the Observer
     */
    public void registerObserver(Observer observer) {
        observable.addObserver(observer);
        //Added for COEUSQA-2633 Ability to indicate which procedures IACUC study personnel will perform and training -Start
        //observable.notifyObservers(vecStudyGroup);         
        String observerName = observer.getClass().getCanonicalName(); 
        if(SPECIES_OBSERVER.equals(observerName)){
        notifyObserver(vecStudyGroup, SPECIES_TYPE);
        }else{
         notifyObserver(vecStudyGroup, PERSON_TYPE);   
        }
        //Added for COEUSQA-2633 Ability to indicate which procedures IACUC study personnel will perform and training -End
    }
    
    /**
     * UnRegisters the Observer
     */
    public void unRegisterObserver(Observer observer) {
        observable.deleteObserver(observer);
    }
    
    /*
     * Gets the update from the Protocol Species form
     */
    public void update(Observable o, Object arg) {
//        Vector vecProtocolSpecies = (Vector)arg;
//        //Modified with CoeusQQA-2551:Rework how user enters species,study groups and procedures in IACUC protocols
////        populateSpeciesType(vecProtocolSpecies);
//        /* When the user updates a species info in the species tab,perform the following actions
//         * 1. Stop editing
//         * 2. Update combo list
//         * 3. Update already selected species info
//         * 4. Reset the row selection.
//         */
//        int selectedRow = tblStudyGroup.getSelectedRow();
//        if (tblStudyGroup.getCellEditor()!=null){
//            tblStudyGroup.getCellEditor().stopCellEditing();
//        }
//        populateSpeciesGroup(vecProtocolSpecies);
//        if(studyGroupTableModel!=null && tblStudyGroup.getRowCount()>0){
//            studyGroupTableModel.fireTableDataChanged();
//            
//        }
//        if(selectedRow >= 0){
//            tblStudyGroup.setRowSelectionInterval(selectedRow,selectedRow);
//        }
        //CoeusQA-2551 - End
        
        
        
        HashMap hmNofiyInfo = (HashMap)arg;
        if(hmNofiyInfo.get("speciesInfo")!=null){// coming from species
            Vector vecProtocolSpecies = (Vector)hmNofiyInfo.get("speciesInfo");
            /* When the user updates a species info in the species tab,perform the following actions
             * 1. Stop editing
             * 2. Update combo list
             * 3. Update already selected species info
             * 4. Reset the row selection.
             */
            int selectedRow = tblStudyGroup.getSelectedRow();
            if (tblStudyGroup.getCellEditor()!=null){
                tblStudyGroup.getCellEditor().stopCellEditing();
            }
            populateSpeciesGroup(vecProtocolSpecies);
            if(studyGroupTableModel!=null && tblStudyGroup.getRowCount()>0){
                studyGroupTableModel.fireTableDataChanged();                        
            }
            if(selectedRow >= 0){
                tblStudyGroup.setRowSelectionInterval(selectedRow,selectedRow);
                //Added for COEUSQA-2633 Ability to indicate which procedures IACUC study personnel will perform and training -Start
                ProtocolStudyGroupBean protocolStudyGroupBean = (ProtocolStudyGroupBean) vecStudyGroup.get(selectedRow);
                if(vecProtocolSpecies != null && vecProtocolSpecies.size()>0){
                    ProtocolSpeciesBean speciesBean;
                    for(Object objSpecies:vecProtocolSpecies){
                        speciesBean = (ProtocolSpeciesBean)objSpecies;
                        if(speciesBean.getSpeciesId() == protocolStudyGroupBean.getSpeciesId()){
                            protocolStudyGroupBean.setSpeciesCode(speciesBean.getSpeciesCode());
                        }
                    }
                    
                }                 
                loadPersonsResponsibleDetails(protocolStudyGroupBean.getPersonsResponsible());
                //Added for COEUSQA-2633 Ability to indicate which procedures IACUC study personnel will perform and training -End
            }            
        }else if(hmNofiyInfo.get("investigatorInfo")!=null){// coming from investigator
            Vector vecInvestitor = (Vector)hmNofiyInfo.get("investigatorInfo");
            vecPersonResponseData = new Vector();
            vecPersonResponseData.addAll(vecInvestitor);
        }else if(hmNofiyInfo.get("studyPersonnelInfo")!=null){
            Vector vecStudyPersonell = (Vector)hmNofiyInfo.get("studyPersonnelInfo");
            vecStudyPersonData = new Vector();
            vecStudyPersonData.addAll(vecStudyPersonell);
        }else if(hmNofiyInfo.get("deletedPersonsResponse")!= null){
            String deletedPersonId = (String)hmNofiyInfo.get("deletedPersonsResponse"); 
            if(vecStudyGroup != null && vecStudyGroup.size()>0){               
                ProtocolStudyGroupBean studyGroupBean;
                for(Object objStudyGroup:vecStudyGroup){
                    studyGroupBean = (ProtocolStudyGroupBean)objStudyGroup;
                    Vector vecTempPersonRespData = studyGroupBean.getPersonsResponsible();
                    if(vecTempPersonRespData != null && vecTempPersonRespData.size()>0){
                       ProtocolPersonsResponsibleBean personRespBean;
                       for(int index=0;index<vecTempPersonRespData.size();index++){
                           personRespBean = (ProtocolPersonsResponsibleBean)vecTempPersonRespData.get(index);
                           if(deletedPersonId.equals(personRespBean.getPersonId())){
                               personRespBean.setAcType(TypeConstants.DELETE_RECORD);
                               if(cvPersonRespDelete == null){
                                   cvPersonRespDelete = new CoeusVector();
                               }
                               cvPersonRespDelete.add(personRespBean);
                               vecTempPersonRespData.remove(index);
                               studyGroupBean.setPersonsResponsible(vecTempPersonRespData);
                               saveRequired = true;
                               break;
                           }
                       }
                        
                    }
                }
                notifyObserver(vecStudyGroup, PERSON_TYPE);
            }
        }
    }
       
    //Added for COEUSQA-2633 Ability to indicate which procedures IACUC study personnel will perform and training -Start
     private void notifyObserver(Vector vecObserverData, String observerId){
       HashMap hmNotifyInfo = new HashMap();
       if(SPECIES_TYPE.equals(observerId)){
       hmNotifyInfo.put("speciesInfo",vecObserverData);
       }else{
           ProtocolStudyGroupBean studyGroupBean;
           Vector vecPersonsObserverData = new Vector();
           if(vecObserverData != null && vecObserverData.size()>0){
               for(Object obj:vecObserverData){
                   studyGroupBean = (ProtocolStudyGroupBean)obj;
                   if(studyGroupBean.getPersonsResponsible() != null 
                     && studyGroupBean.getPersonsResponsible().size()>0){
                     vecPersonsObserverData.addAll(studyGroupBean.getPersonsResponsible());
                   }
               }
           }
            hmNotifyInfo.put("personInfo",vecPersonsObserverData);   
       }
       observable.notifyObservers(hmNotifyInfo);
    }
    //Added for COEUSQA-2633 Ability to indicate which procedures IACUC study personnel will perform and training -End
    
    //Commented with CoeusQQA-2551:Rework how user enters species,study groups and procedures in IACUC protocols
    /*
     * Method to populate the species type
     *
     */
//    public void populateSpeciesType(Vector vecProtocolSpecies){
//        if(cvSpeciesType != null){
//            cvSpeciesType.removeAllElements();
//        }else{
//            cvSpeciesType = new CoeusVector();
//        }
//        if(vecProtocolSpecies != null && vecProtocolSpecies.size() > 0){
//            for(int index=0;index<vecProtocolSpecies.size();index++){
//                ProtocolSpeciesBean speciesBean = (ProtocolSpeciesBean)vecProtocolSpecies.get(index);
//                if(speciesBean.getSpeciesCode() > 0){
//                    cvSpeciesType.add(new ComboBoxBean(speciesBean.getSpeciesCode()+CoeusGuiConstants.EMPTY_STRING,speciesBean.getSpeciesName()));
//                }
//            }
//        }
//    }
    
//Added with CoeusQQA-2551:Rework how user enters species,study groups and procedures in IACUC protocols
    /*
     * Method to populate the species group names
     *
     */
    public void populateSpeciesGroup(Vector vecProtocolSpecies){
       hmGroup = new HashMap();
       cvSpeciesGroupNames = new CoeusVector();
        if(vecProtocolSpecies != null && vecProtocolSpecies.size() > 0){
            for(int index=0;index<vecProtocolSpecies.size();index++){
                ProtocolSpeciesBean speciesBean = (ProtocolSpeciesBean)vecProtocolSpecies.get(index);
                if(speciesBean.getSpeciesId() > 0 && speciesBean.getSpeciesGroupName()!=null && !CoeusGuiConstants.EMPTY_STRING.equals(speciesBean.getSpeciesGroupName())){
                    cvSpeciesGroupNames.add(new ComboBoxBean(String.valueOf(speciesBean.getSpeciesId()),speciesBean.getSpeciesGroupName()));
                    hmGroup.put(String.valueOf(speciesBean.getSpeciesId()),speciesBean);
                }
            }
    }
       // Added for COEUSQA-2774-IACUC Location disappears when user clicks "Add location" to add another one : Start
       cvSpeciesGroupNames.sort("description");
       // COEUSQA-2774-End
    }
    
  /*
   * Method to validate the Species form
   *
   */
    public boolean validateData()throws CoeusUIException{
        if(tblStudyGroup.isEditing()){
            if(tblStudyGroup.getCellEditor() != null){
                tblStudyGroup.getCellEditor().stopCellEditing();
            }
        }
        if(tblLocation.isEditing()){
            if(tblLocation.getCellEditor() != null){
                tblLocation.getCellEditor().stopCellEditing();
            }
        }
       //Added for set the custum element value- start 
//        if(vecStudyGroup != null && vecStudyGroup.size() > 0){
//            ProtocolStudyGroupBean studyGroupBean = null;
//            for(int index=0;index<vecStudyGroup.size();index++){
//                studyGroupBean = (ProtocolStudyGroupBean)vecStudyGroup.get(index);
//                CustomElementsForm otherForm = (CustomElementsForm)hmCustomForm.get(studyGroupBean.getStudyGroupId()+CoeusGuiConstants.EMPTY_STRING);                
//                studyGroupBean.setOtherDetails(otherForm.getOtherColumnElementData());
//            }
//        }  
        //Added for set the custum element value- end
        boolean validate = true;
        int rowCount = tblStudyGroup.getRowCount();
        if(vecStudyGroup != null && vecStudyGroup.size()>0){
            for(int index=0; index < rowCount ;index++){
                ProtocolStudyGroupBean studyGroupBean = (ProtocolStudyGroupBean)vecStudyGroup.get(index);
                //Added row/column selection with CoeusQA-2551:Rework how user enters species,study groups and procedures in IACUC protocols
                if(studyGroupBean.getSpeciesId() < 1){
                    validate = false;
                    tblStudyGroup.setRowSelectionInterval(index,index);
                    tblStudyGroup.setColumnSelectionInterval(SPECIES_GROUP_NAME_COLUMN,SPECIES_GROUP_NAME_COLUMN);
                    errorMessage(coeusMessageResources.parseMessageKey("iacucProtoStudyGroupFrm_exceptionCode.1000"));
                    break;
                }else if(studyGroupBean.getProcedureCategoryCode() < 1){
                    validate = false;
                    tblStudyGroup.setRowSelectionInterval(index,index);
                    tblStudyGroup.setColumnSelectionInterval(PROCEDURE_CATEGORY_COLUMN,PROCEDURE_CATEGORY_COLUMN);
                    errorMessage(coeusMessageResources.parseMessageKey("iacucProtoStudyGroupFrm_exceptionCode.1001"));
                    break;
                }else if(studyGroupBean.getProcedureCode() < 1){
                    validate = false;
                    tblStudyGroup.setRowSelectionInterval(index,index);
                    tblStudyGroup.setColumnSelectionInterval(PROCEDURE_COLUMN,PROCEDURE_COLUMN);
                    errorMessage(coeusMessageResources.parseMessageKey("iacucProtoStudyGroupFrm_exceptionCode.1002"));
                    break;
                }
                // Commented for COEUSQA-2627_Move pain category in IACUC protocol to Protocol SpeciesGroup level_start
                /*else if(studyGroupBean.getPainCategoryCode() < 1){
                    validate = false;
                    tblStudyGroup.setRowSelectionInterval(index,index);
                    tblStudyGroup.setColumnSelectionInterval(PAIN_CATEGORY_COLUMN,PAIN_CATEGORY_COLUMN);
                    errorMessage(coeusMessageResources.parseMessageKey("iacucProtoStudyGroupFrm_exceptionCode.1003"));
                    break;
                }*/
                // Commented for COEUSQA-2627_Move pain category in IACUC protocol to Protocol SpeciesGroup level_end
                //Validation for location
                Vector vecLocation = studyGroupBean.getLocations();
                if(vecLocation != null && vecLocation.size() > 0){
                    for(int locIndex=0;locIndex<vecLocation.size();locIndex++){
                        ProtocolStudyGroupLocationBean locationBean =
                                (ProtocolStudyGroupLocationBean)vecLocation.get(locIndex);
                        if(locationBean.getLocationTypeCode() < 1){
                            validate = false;
                            errorMessage(coeusMessageResources.parseMessageKey("iacucProtoStudyGroupFrm_exceptionCode.1004"));
                            break;
                        }else if(locationBean.getLocationId() < 1){
                            validate = false;
                            errorMessage(coeusMessageResources.parseMessageKey("iacucProtoStudyGroupFrm_exceptionCode.1005"));
                            break;
                        }
                    }
                }
                //Validate custom elements
                CustomElementsForm otherForm = (CustomElementsForm)hmCustomForm.get(studyGroupBean.getStudyGroupId()+CoeusGuiConstants.EMPTY_STRING);
                try {
                    validate = otherForm.validateData();
                } catch (Exception ex) {
                    errorMessage(ex.getMessage());
                }
                        //Added for set the custum element date validation- start
//                        Vector vecOtherData = new Vector();                        
//                        vecOtherData = studyGroupBean.getOtherDetails();
//                        CustomElementsInfoBean customElementsInfoBean = null;
//                        for(int otherIndex=0;otherIndex<vecOtherData.size();otherIndex++){
//                        customElementsInfoBean = (CustomElementsInfoBean)vecOtherData.get(otherIndex);
//                        String strDataType = customElementsInfoBean.getDataType();
//                        if("DATE".equals(strDataType)){
//                        String strDate = customElementsInfoBean.getColumnValue();
//                        if((!strDate.equals(null))&& strDate.length()>0){             
//                               DateUtils dateUtils = new DateUtils();
//                                strDate = dateUtils.formatDate(strDate,":/.,|-","dd-MMM-yyyy");
//                                if(strDate == null){
//                                    validate = false;
//                                 errorMessage(coeusMessageResources.parseMessageKey("iacucProtoStudyGroupFrm_exceptionCode.1012"));
//                                 break;
//                                   
//                        }            
//                    }
//                }}
                        //Added for set the custum element date validation- End
           }
        }
        return validate;
    }
    
    private void errorMessage(String mesg) throws CoeusUIException{
        CoeusUIException coeusUIException = new CoeusUIException(mesg,CoeusUIException.WARNING_MESSAGE);
        coeusUIException.setTabIndex(CoeusGuiConstants.IACUC_STUDY_GROUP_EXCEP_TAB_INDEX);
        throw coeusUIException;
    }
    
    /*
     * Method to get the study group form data
     */
    public Vector getFormData(){
        Vector vecFormData = new Vector();        
        ProtocolStudyGroupBean studyGroupBean = null;
        if(vecStudyGroup != null && vecStudyGroup.size() > 0){
            for(int index=0;index<vecStudyGroup.size();index++){
                studyGroupBean = (ProtocolStudyGroupBean)vecStudyGroup.get(index);
                CustomElementsForm otherForm = (CustomElementsForm)hmCustomForm.get(studyGroupBean.getStudyGroupId()+CoeusGuiConstants.EMPTY_STRING);                
                studyGroupBean.setOtherDetails(otherForm.getOtherColumnElementData());
            }
        }                
        vecFormData.add(0,cvLocationDelete);
        vecFormData.add(1,cvStudyGroupDelete);
        vecFormData.add(2,vecStudyGroup);
        vecFormData.add(3,hmOthersDelete);
        //Added for COEUSQA-2633 Ability to indicate which procedures IACUC study personnel will perform and training -Start
        vecFormData.add(4,cvPersonRespDelete);
        //Added for COEUSQA-2633 Ability to indicate which procedures IACUC study personnel will perform and training -End
        return vecFormData;
    }
    
    //COEUSQA-2628-Add to Study Group screen a large comment box called "Overview and Timeline" - start
  
    /** Method for getting overViewTimeLineData from UI
     * @return String overViewTimeLineDetails 
     */
     public String getOverviewTimelineFromUI() {         
        overViewTimeLineDetails = txtArOverviewTimeline.getText();
        return overViewTimeLineDetails;
    }
    
     /** Method for setting overviewTimeline value to ProtocolInfoBean
     * @param ProtocolInfoBean protocolInfo
     * @return void
     */
     public void setOverviewTimelineValue(ProtocolInfoBean protocolInfo) {
         String overviewTimeline = getOverviewTimelineFromUI();
         if(!overviewTimeline.equals(protocolInfo.getOverviewTimeline())) {
             protocolInfo.setOverviewTimeline(overviewTimeline);  
             setSaveRequired(true);
        }
         
     }
    /** Method for setting the overViewTimeLineDetails
     * @param String overViewTimeLineDetails
     * @return void
     */
    public void setOverviewTimelineDetails(String overViewTimeLineDetails) {
        this.overViewTimeLineDetails = overViewTimeLineDetails;
        
    }
    
    /** Method for getting the overViewTimeLineDetails
     * @return String
     */
    public String getOverviewTimelineDetails() {
        return overViewTimeLineDetails;
    }
    //COEUSQA-2628-Add to Study Group screen a large comment box called "Overview and Timeline" - end 
   
    
    /** Getter for property protocolNumber.
     * @return Value of property protocolNumber.
     */
    public java.lang.String getProtocolNumber() {
        return protocolNumber;
    }
    
    /** Setter for property protocolNumber.
     * @param protocolNumber New value of property protocolNumber.
     */
    public void setProtocolNumber(java.lang.String protocolNumber) {
        this.protocolNumber = protocolNumber;
    }
    
    /** Getter for property sequenceNumber.
     * @return Value of property sequenceNumber.
     *
     */
    public int getSequenceNumber() {
        return sequenceNumber;
    }
    
    /** Setter for property sequenceNumber.
     * @param sequenceNumber New value of property sequenceNumber.
     *
     */
    public void setSequenceNumber(int sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }
    
    //Added for COEUSQA-2633 Ability to indicate which procedures IACUC study personnel will perform and training -Start
     /*
     * This method is used to load the person responsible data    
     * @param Vector 
     */
    public void loadPersonsResponsibleDetails(Vector vecPersonsResponsible){
        if(vecPersonsResponsible == null){
            vecPersonsResponsible = new Vector();
        }
        updatePersonTrainingFlag();
        personResponsibleTableModel.setData(vecPersonsResponsible);
        if(vecPersonsResponsible != null && vecPersonsResponsible.size() > 0){
            tblPersonsResponsible.setRowSelectionInterval(0,0);
        }        
    }
           
    /*
     * Table model for Study group person responsible table
     *
     */
    class PersonResponsibleTableModel extends DefaultTableModel {
        Vector vecData;
        private String colNames[] =
                                   {"","Investigators/Study Personnel","Trained"};
        private Class colTypes[] =
                                   {Object.class,String.class,Boolean.class};
        public boolean isCellEditable(int row, int column) {             
                return false;             
        }
        
        public int getColumnCount() {
            return colNames.length;
        }
        
        public Object getValueAt(int row, int column) {
            ProtocolPersonsResponsibleBean personsResponsibleBean = (ProtocolPersonsResponsibleBean)vecData.get(row);
            switch(column) {
                case PERSON_NAME_COLUMN:                     
                       return personsResponsibleBean.getPersonName();                      
                case PERSON_TRAINING_COLUMN:      
                      return personsResponsibleBean.isTrained();                                  
                }
            return CoeusGuiConstants.EMPTY_STRING;
        }
        
        public String getColumnName(int colIndex) {
            return colNames[colIndex];
        }
        
        public Class getColumnClass(int colIndex) {
            return colTypes[colIndex];
        }
        
        public void setData(Vector cvData) {
            dataVector = cvData;
            this.vecData = cvData;
            fireTableDataChanged();
        }
        
        public void setValueAt(Object value, int row, int column) {
            if(TypeConstants.DISPLAY_MODE == functionType ||
                    value == null ||
                    vecData == null ||
                    (vecData != null && vecData.size() < 1)){
                return;
            }
            
            ProtocolPersonsResponsibleBean personsResponsibleBean = (ProtocolPersonsResponsibleBean)vecData.get(row);
            switch(column) {
                case PERSON_NAME_COLUMN:  
                    personsResponsibleBean.setPersonId(value.toString().trim());
                    break;
                case PERSON_TRAINING_COLUMN:  
                    personsResponsibleBean.setTrained((Boolean)value);
                    break;
                 
            }
        }
        
    }
    
    /*
    * This method is used render the check and uncheck image icon for person training status     
    */ 
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
                setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
                //setBackground(Color.white);                
                    boolean isTraining = ((Boolean)value).booleanValue();
                    if(isTraining){
                        setIcon(checkIcon);
                    }else{
                        setIcon(crossIcon);
                    }                 
            return this;
            
        }
    }
    
    /*
     * This method is used to get the investigator/study personnel for person responsible 
     * when user is trying to add new person responsible         
     * @return Vector 
     */
    private Vector getInveStudyPersonnelData(){         
        CoeusVector vecPersonData = new CoeusVector();        
        if(vecPersonResponseData !=null && vecPersonResponseData.size()>0){
            ProtocolInvestigatorsBean investigatorBean;
            for(Object objInves:vecPersonResponseData){
                investigatorBean = (ProtocolInvestigatorsBean)objInves;
                ProtocolPersonsResponsibleBean personsResponsibleBean = new ProtocolPersonsResponsibleBean();                 
                personsResponsibleBean.setPersonId(investigatorBean.getPersonId());
                personsResponsibleBean.setPersonName(investigatorBean.getPersonName());                 
                // Added for COEUSQA-3694 : Coeus4.5: IACUC Procedure Personnel Issue Remains - Start
                personsResponsibleBean.setNonEmployeeFlag(investigatorBean.isNonEmployeeFlag());
                // Added for COEUSQA-3694 : Coeus4.5: IACUC Procedure Personnel Issue Remains - End
                vecPersonData.add(personsResponsibleBean);
            }
        }
        if(vecStudyPersonData !=null && vecStudyPersonData.size()>0){             
            CoeusVector cvKeyPersonData = new CoeusVector();
            Equals andPersonId;             
            ProtocolKeyPersonnelBean keyPersonnelBean;
            for(Object objKeyPerson:vecStudyPersonData){
                keyPersonnelBean = (ProtocolKeyPersonnelBean)objKeyPerson;
                ProtocolPersonsResponsibleBean personsResponsibleBean = new ProtocolPersonsResponsibleBean();                 
                personsResponsibleBean.setPersonId(keyPersonnelBean.getPersonId());
                personsResponsibleBean.setPersonName(keyPersonnelBean.getPersonName());                                                                             
                // Added for COEUSQA-3694 : Coeus4.5: IACUC Procedure Personnel Issue Remains - Start
                personsResponsibleBean.setNonEmployeeFlag(keyPersonnelBean.isNonEmployeeFlag());     
                // Added for COEUSQA-3694 : Coeus4.5: IACUC Procedure Personnel Issue Remains - End
                   andPersonId= new Equals("personId",personsResponsibleBean.getPersonId());                    
                   CoeusVector filteredResult = vecPersonData.filter(andPersonId);
                   if(filteredResult.size()==0){
                       cvKeyPersonData.add(personsResponsibleBean);
                   }                               
            }
            if(cvKeyPersonData != null && cvKeyPersonData.size()>0){
            vecPersonData.addAll(cvKeyPersonData);
            }
        }                
        return vecPersonData;
    }
        
    /*
     * This method is used to validate the person responsible 
     * when user is trying to add new person responsible      
     * @param int speciesCode
     * @param int procedureCode
     * @param int selStudyGroupRow
     * @throws CoeusUIException 
     */
     private void validatePerson(final int speciesCode, final int procedureCode, final int selStudyGroupRow) throws CoeusUIException {                  
        if(speciesCode < 1){                 
             tblStudyGroup.setRowSelectionInterval(selStudyGroupRow,selStudyGroupRow);
             tblStudyGroup.setColumnSelectionInterval(SPECIES_GROUP_NAME_COLUMN,SPECIES_GROUP_NAME_COLUMN);
             errorMessage(coeusMessageResources.parseMessageKey("iacucProtoStudyGroupFrm_exceptionCode.1000"));                                                                  
         }
        if(procedureCode < 1){                  
            tblStudyGroup.setRowSelectionInterval(selStudyGroupRow,selStudyGroupRow);
            tblStudyGroup.setColumnSelectionInterval(PROCEDURE_COLUMN,PROCEDURE_COLUMN);
            errorMessage(coeusMessageResources.parseMessageKey("iacucProtoStudyGroupFrm_exceptionCode.1002"));                
        }
    }
     
     /*
     * This method is used to update the training flag for the person
     * if speciesCode or procedureCode value has changed
     * 
     */
      private void updatePersonTrainingFlag() {
        if(vecStudyGroup !=null && vecStudyGroup.size()>0){
           Vector vecPersonsFlagUpdate = new Vector();
           ProtocolStudyGroupBean studyGroupBean;
           for(Object objStudyGrp:vecStudyGroup){
               studyGroupBean = (ProtocolStudyGroupBean)objStudyGrp;
               vecPersonsFlagUpdate = studyGroupBean.getPersonsResponsible();
               if(vecPersonsFlagUpdate != null && vecPersonsFlagUpdate.size()>0){
                   int speciesCode = studyGroupBean.getSpeciesCode();
                   int procedureCode = studyGroupBean.getProcedureCode();
                   ProtocolPersonsResponsibleBean personBean;
                   for(Object objPerson:vecPersonsFlagUpdate){
                       personBean = (ProtocolPersonsResponsibleBean)objPerson;
                       personBean.setTrained(getSpeciesTrainingInfo(personBean.getPersonId(),speciesCode,procedureCode));
                   }
               }
           }
        }            
     }
   
    /*
     * This method is used to get the training flag for the person
     * @param String personId
     * @param int speciesCode
     * @param int procedureCode
     * @return boolean Value
     */
    private boolean getSpeciesTrainingInfo(String personId, int speciesCode, int procedureCode){
        boolean isTrain = false;
        RequesterBean requesterBean = new RequesterBean();
        ResponderBean responderBean = new ResponderBean();
        Vector personSpeciesData = new Vector();
        personSpeciesData.add(0, personId);
        personSpeciesData.add(1, speciesCode);
        personSpeciesData.add(2, procedureCode);
        requesterBean.setDataObjects(personSpeciesData);
        requesterBean.setId("SPECIES_TRAINING");
        requesterBean.setFunctionType(GET_TRAINIG_INFO);
        String connectTo = CoeusGuiConstants.CONNECTION_URL + "/IacucProtocolServlet";         
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
    
    /*
    *This method is used to set the listeners to the Persons responsible table.
    *
    */
    private void setListenerForPersonResponseTable(){
        coeusMessageResources = CoeusMessageResources.getInstance();        
        tblPersonsResponsible.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent me) {
                if (me.getClickCount() == 2) {
                    try {
                         viewPersonTrainingDetails();
                    }catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
    
    /*
     * This method is used to View the Persons Responsible training details.
     *
     */
     private void viewPersonTrainingDetails() {
        int selectedRow = tblStudyGroup.getSelectedRow();
        int personSelectedRow = tblPersonsResponsible.getSelectedRow();
        ProtocolStudyGroupBean studyGroupBean = (ProtocolStudyGroupBean)vecStudyGroup.get(selectedRow);
        Vector vecPersonViewData = studyGroupBean.getPersonsResponsible();
        ProtocolPersonsResponsibleBean viewAltSerachBean = (ProtocolPersonsResponsibleBean)vecPersonViewData.get(personSelectedRow);         
        String personId = viewAltSerachBean.getPersonId();
        String personName = viewAltSerachBean.getPersonName();                                                           
        PersonResponsibleTrainingDetailForm personDetailForm = new PersonResponsibleTrainingDetailForm(mdiForm, personId,personName, functionType);                     
     }              
    //Added for COEUSQA-2633 Ability to indicate which procedures IACUC study personnel will perform and training -End

    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JButton btnAddLocation;
    public javax.swing.JButton btnAddPersons;
    public javax.swing.JButton btnAddStudyGroup;
    public javax.swing.JButton btnDeleteLocation;
    public javax.swing.JButton btnDeletePersons;
    public javax.swing.JButton btnDeleteStudyGroup;
    public javax.swing.JButton btnLocationName;
    public javax.swing.JButton btnLocationType;
    public javax.swing.JPanel pnlOverviewTimeline;
    public javax.swing.JPanel pnlProcedures;
    public javax.swing.JScrollPane scrPnLocation;
    public javax.swing.JScrollPane scrPnOverviewTimeline;
    public javax.swing.JScrollPane scrPnPersonResponsible;
    public javax.swing.JScrollPane scrPnStudyGroup;
    public javax.swing.JScrollPane scrnPnCustomElements;
    public javax.swing.JTable tblLocation;
    public javax.swing.JTable tblPersonsResponsible;
    public javax.swing.JTable tblStudyGroup;
    public javax.swing.JTextArea txtArOverviewTimeline;
    // End of variables declaration//GEN-END:variables
    
}
