/*
 * @(#)ProtocolPrintSummary.java 1.0 March 18, 2009, 7:25 PM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

/* PMD check performed, and commented unused imports and variables on 09-MARCH-2011
 * by Md.Ehtesham Ansari
 */
package edu.mit.coeus.iacuc.gui;

import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.gui.CoeusAppletMDIForm;
import edu.mit.coeus.iacuc.bean.ProtocolExceptionBean;
import edu.mit.coeus.iacuc.bean.ProtocolSpeciesBean;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.iacuc.bean.ProtocolStudyGroupBean;
import edu.mit.coeus.utils.*;
import edu.mit.coeus.exception.*;
import edu.mit.coeus.brokers.*;
import edu.mit.coeus.utils.query.Equals;
import java.awt.Color;
import java.awt.Component;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.AbstractCellEditor;
import javax.swing.DefaultCellEditor;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.JComponent;
import java.awt.event.*;
import java.util.*;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.*;

/**
 *
 * @author satheeshkumarkn
 */

public class ProtocolSpeciesForm extends javax.swing.JComponent implements ActionListener, ListSelectionListener, Observer{
    
    private char functionType;
    private Vector vecProtocolSpecies, vecDeletedSpecies;
    private boolean saveRequired;
    private String protocolNumber;
    private CoeusAppletMDIForm mdiReference;
    private int sequenceNumber;
    private CoeusMessageResources coeusMessageResources;
    private static final int ICON_COLUMN = 0;
    private static final int ICON_MIN_WIDTH = 30;
    //Modified with CoeusQA-2551:Rework how user enters species,study groups and procedures in IACUC protocols
    private static final int SPECIES_GROUP_COLUMN = 1;
    // Added and Modified for COEUSQA-2627_Move pain category in IACUC protocol to Protocol SpeciesGroup level_start
    private static final int SPECIES_GROUP_COLUMN_MIN_WIDTH = 115;
    private static final int SPECIES_COLUMN = 2;
    private static final int SPECIES_COLUMN_MIN_WIDTH = 150;
    private static final int USDA_COLUMN = 3;
    private static final int USDA_COLUMN_MIN_WIDTH = 135;
    private static final int STRAIN_COLUMN = 4;
    //Modified/Added for-COEUSQA-2798 Add count type to species/group screen-Start
    private static final int STRAIN_COLUMN_MIN_WIDTH = 40;    
    private static final int STRAIN_COLUMN_MAX_WIDTH = 200; 
    private static final int COUNT_COLUMN_TYPE_WIDTH = 85;
    private static final int COUNT_TYPE_COLUMN =6;
    private static final int COUNT_COLUMN =7;
    //Modified/Added for-COEUSQA-2798 Add count type to species/group screen-End  
    private static final int PAIN_CATEGORY_COLUMN =5;    
    private static final int PAIN_CATEGORY_COLUMN_MAX_WIDTH = 200;
    private static final int PAIN_CATEGORY_COLUMN_MIN_WIDTH = 98;
    private static final int PAIN_CATEGORY_COLUMN_PREFERED_WIDTH = 100;
    // Added and Modified for COEUSQA-2627_Move pain category in IACUC protocol to Protocol SpeciesGroup level_end
    //CoeusQA-2551:End
    //COEUSQA-2724-Exceptions should be moved from Scientific Justification in IACUC
    private static final int EXCEPTION_COLUMN = 8;
    private static final int EXCEPTION_COLUMN_MIN_WIDTH = 70;
    
    private static final int EXCEPTION_CATEGORY_COLUMN = 1;
    private static final int DESCRIPTION_COLUMN = 2;
    private static final int EXCEPTION_CATEGORY_MIN_WIDTH = 178;
    private static final int DESCRIPTION_MIN_WIDTH = 620;
    private CoeusVector cvExceptionsCategory;
    private Vector vecExceptionsData, vecDeletedExceptions, vecSpeciesExceptions;
    
    private static final int COUNT_COLUMN_MIN_WIDTH = 20;
    private static final String SPECIES_DELETE_CONFIRMATION = "species_delete_confirmationCode.1000";
    private static final String ROW_SELECT_TO_DELETE = "species_select_exceptionCode.1001";
    private SpeciesTableModel speciesTableModel;
    private ExceptionsTableModel exceptionsTableModel;
    private CoeusVector cvSpeciesCode;
    // Added for COEUSQA-2627_Move pain category in IACUC protocol to Protocol SpeciesGroup level_start
    private CoeusVector cvPainCategory;
    // Added for COEUSQA-2627_Move pain category in IACUC protocol to Protocol SpeciesGroup level_end
    //Added for-COEUSQA-2798 Add count type to species/group screen-Start  
    private CoeusVector cvSpeciesCountType;
    //Added for-COEUSQA-2798 Add count type to species/group screen-End  
    private ProtocolSpeciesBean oldSpeciesData;
    //private int SPECIES_TAB = 13;
    private static final int ZERO_ROW_INDEX = 0;
    private static final int ROW_HEIGHT = 22;
    private static final int STRAIN_FIELD_CHAR_LENGTH = 30;
    //Commented and Added for COEUSQA-3384 : Increase lengths for some IACUC fields - start
    //The Count field max char length on the Groups/Species need to be increase for 5 digit long
    //private static final int COUNT_FIELD_CHAR_LENGTH = 4;
    private static final int COUNT_FIELD_CHAR_LENGTH = 8;
    //Commented and Added for COEUSQA-3384 : Increase lengths for some IACUC fields - end
    private static final String SELECT_SPECIES_TYPE = "iacucProtoSpeciesFrm_exceptionCode.1000";
    // Added for COEUSQA-2627_Move pain category in IACUC protocol to Protocol SpeciesGroup level_start
    private static final String SELECT_PAIN_CATEGORY_TYPE = "iacucProtoStudyGroupFrm_exceptionCode.1003";
    // Added for COEUSQA-2627_Move pain category in IACUC protocol to Protocol SpeciesGroup level_end
    private static final String DELETE_STUDY_GROUP_BEFORE_DEL_SPECIES = "iacucProtoStudyGroupFrm_exceptionCode.1011";
    private BaseWindowObservable observable;
    //private static final int TAB_INDEX_FOR_SPECIES_FROM_SPECIES_FORM = 30;
    private Vector vecStudyGroupData;
    //Added with CoeusQA-2551:Rework how user enters species,study groups and procedures in IACUC protocols
    private static final int GROUP_FIELD_CHAR_LENGTH = 50;
    private static final String ERRKEY_DUPL_GROUP_NAME = "iacucProtoSpeciesFrm_exceptionCode.1001";
    private static final String ERRKEY_EMPTY_GROUP_NAME = "iacucProtoSpeciesFrm_exceptionCode.1002";
    //CoeusQA-2551:End
    //Added for-COEUSQA-2508 Some schools do not use pain category-Start
    private final String PROTOCOL_SERVLET = "/IacucProtocolServlet";
    private final String PAIN_CATEGORY_CODE = "1";
    //Added for-COEUSQA-2508 Some schools do not use pain category-End
    private ScientificJustificationExceptionForm scientificJustificationExceptionForm;
    private boolean isUpdateBeforeSave = false;
    private int selectedExceptionRow = 0;
    private static final String EXCEPTION_DELETE_CONFIRMATION = "iacucPrtoScientJustFrm_exceptionCode.1000";
    private static final String EXCEPTION_ROW_SELECT_TO_DELETE = "iacucPrtoScientJustFrm_exceptionCode.1001";   
    private static final String DESELECT_MESSAGE_FOR_EXCEPTION = "iacucProtoSpeciesFrm_exceptionCode.1003";
    //private Hashtable exceptionHashData = new Hashtable();
    /** Creates new form ProtocolNotepadForm */
    public ProtocolSpeciesForm(Vector vecProtocolSpecies,char fnType){
        this.functionType = fnType;
        this.vecProtocolSpecies = vecProtocolSpecies;
        if(fnType == TypeConstants.AMEND_MODE && vecProtocolSpecies != null && vecProtocolSpecies.size()>0){
            for(int index=0;index<vecProtocolSpecies.size();index++){
                ProtocolSpeciesBean speciesBean = (ProtocolSpeciesBean)vecProtocolSpecies.get(index);
                speciesBean.setAcType(TypeConstants.INSERT_RECORD);
            }
        }
        observable  = new BaseWindowObservable();
        if (vecProtocolSpecies == null) {
            vecProtocolSpecies = new Vector();
        }
       
    }

    /*
     * Method to show the Species panel
     *
     */  
    public JComponent showProtocolSpeciesForm(CoeusAppletMDIForm mdiForm){
        this.mdiReference = mdiForm;
        initComponents();
        btnAdd.addActionListener(this);
        btnDelete.addActionListener(this);
        java.awt.Component[] components = {tblProtocolSpecies,btnAdd};
        ScreenFocusTraversalPolicy  traversePolicy = new ScreenFocusTraversalPolicy(components);
        this.setFocusTraversalPolicy(traversePolicy);
        this.setFocusCycleRoot(true);       
        if(cvSpeciesCode == null){
            cvSpeciesCode = new CoeusVector();
        }
        cvSpeciesCode.add(0,new ComboBoxBean("",""));
//        setSpeciesForm(this.vecProtocolSpecies);
        
        // Added for COEUSQA-2627_Move pain category in IACUC protocol to Protocol SpeciesGroup level_start
        if(cvPainCategory == null){
            cvPainCategory = new CoeusVector();
        }
        cvPainCategory.add(0,new ComboBoxBean("",""));
        // Added for COEUSQA-2627_Move pain category in IACUC protocol to Protocol SpeciesGroup level_end
        
        //Added for-COEUSQA-2798 Add count type to species/group screen-Start  
        if(cvSpeciesCountType == null){
            cvSpeciesCountType = new CoeusVector();
        }
        cvSpeciesCountType.add(0,new ComboBoxBean("",""));
        //Added for-COEUSQA-2798 Add count type to species/group screen-End  
        //COEUSQA-2724-Exceptions should be moved from Scientific Justification in IACUC
        loadExceptionCategory();
        btnAddException.addActionListener(this);
        btnDeleteException.addActionListener(this);
        btnModifyException.addActionListener(this);
        btnViewException.addActionListener(this);
        tblProtocolSpecies.getSelectionModel().addListSelectionListener(this);
        
        setListenersForExceptionTable();
        //Modified for COEUSQA-2633 Ability to indicate which procedures IACUC study personnel will perform and training qualifications for species-Start
        notifyObserver(vecProtocolSpecies);
        //Modified for COEUSQA-2633 Ability to indicate which procedures IACUC study personnel will perform and training qualifications for species-End
        return this;
    }
    
    /*
     * Method to set the Species form button properties
     */
    public void buttonSettings(){
        coeusMessageResources = CoeusMessageResources.getInstance();
        btnAdd.setFont(CoeusFontFactory.getLabelFont());
        btnDelete.setFont(CoeusFontFactory.getLabelFont());       
        if (TypeConstants.DISPLAY_MODE == functionType || TypeConstants.AMEND_MODE == functionType) {
            btnAdd.setEnabled(false);
            btnDelete.setEnabled(false);
            //COEUSQA-2724-Exceptions should be moved from Scientific Justification in IACUC
            btnAddException.setEnabled(false);
            btnDeleteException.setEnabled(false);
            btnModifyException.setEnabled(false);
            if(tblExceptions.getRowCount() > 0) {
                btnViewException.setEnabled(true);
            } else{
                btnViewException.setEnabled(false);
            }
            
        }else{
            btnAdd.setEnabled(true);            
            btnDelete.setEnabled(true);
        }
    }
    
    /*
     * Method to set the species form
     * @param vecProtocolSpecies - Vector
     */
    public void setSpeciesForm(Vector vecProtocolSpecies){
        
        saveRequired = false;
        if (vecProtocolSpecies == null) {
            vecProtocolSpecies = new Vector();
        }
        this.vecProtocolSpecies = vecProtocolSpecies;
        //COEUSQA-2724-Exceptions should be moved from Scientific Justification in IACUC
        if (vecExceptionsData == null) {
            vecExceptionsData = new Vector();
        }
        this.vecExceptionsData = getAllExceptions(vecProtocolSpecies);
        setFormData();        
        setTableColumnProperties(); 
        //COEUSQA-2724-Exceptions should be moved from Scientific Justification in IACUC
        setExceptionTableColumnProperties();
        buttonSettings();
        
        if(vecProtocolSpecies != null && vecProtocolSpecies.size() > 0){
            tblProtocolSpecies.setRowSelectionInterval(ZERO_ROW_INDEX, ZERO_ROW_INDEX);
        }else{
            btnDelete.setEnabled(false);
            //COEUSQA-2724-Exceptions should be moved from Scientific Justification in IACUC
            setExceptionButtonsEnabled(false);
        }
        setPropertyChangeSettings();
        if(functionType == TypeConstants.DISPLAY_MODE || functionType == TypeConstants.AMEND_MODE){
            java.awt.Color bgListColor = (java.awt.Color)javax.swing.UIManager.getDefaults().get("Panel.background");
            tblProtocolSpecies.setBackground(bgListColor);
            tblProtocolSpecies.setSelectionBackground(bgListColor );
            tblProtocolSpecies.setSelectionForeground(Color.black);
            //COEUSQA-2724-Exceptions should be moved from Scientific Justification in IACUC
            tblExceptions.setBackground(bgListColor);
            tblExceptions.setSelectionBackground(bgListColor );
            tblExceptions.setSelectionForeground(Color.black);
        } else{
            tblProtocolSpecies.setBackground(Color.white);
            tblProtocolSpecies.setSelectionBackground(Color.white);
            tblProtocolSpecies.setSelectionForeground(Color.black);
            //COEUSQA-2724-Exceptions should be moved from Scientific Justification in IACUC
            tblExceptions.setBackground(Color.white);
            tblExceptions.setSelectionBackground(Color.white);
            tblExceptions.setSelectionForeground(Color.black);
        }
        //Modified for COEUSQA-2633 Ability to indicate which procedures IACUC study personnel will perform and training qualifications for species-Start
        notifyObserver(vecProtocolSpecies);
        //Modified for COEUSQA-2633 Ability to indicate which procedures IACUC study personnel will perform and training qualifications for species-End
    }
    
    /*
     * Method to set the Table column properties
     *
     */
    private void setTableColumnProperties(){
        tblProtocolSpecies.setOpaque(false);
        tblProtocolSpecies.setShowVerticalLines(false);
        tblProtocolSpecies.setShowHorizontalLines(false);
        tblProtocolSpecies.setRowHeight(ROW_HEIGHT);
        tblProtocolSpecies.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tblProtocolSpecies.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);         
        TableColumn column = tblProtocolSpecies.getColumnModel().getColumn(ICON_COLUMN);
        column.setMinWidth(ICON_MIN_WIDTH);
        column.setMaxWidth(ICON_MIN_WIDTH);
        column.setPreferredWidth(ICON_MIN_WIDTH);
        column.setHeaderRenderer(new EmptyHeaderRenderer());
        column.setCellRenderer(new IconRenderer()); 
        column.setResizable(false);
        
        //Added with CoeusQA-2551:Rework how user enters species,study groups and procedures in IACUC protocols
        column = tblProtocolSpecies.getColumnModel().getColumn(SPECIES_GROUP_COLUMN);
        column.setMinWidth(SPECIES_GROUP_COLUMN_MIN_WIDTH);
        if(TypeConstants.DISPLAY_MODE != functionType && TypeConstants.AMEND_MODE != functionType){
            CoeusTextField txtGroupName = new CoeusTextField(new LimitedPlainDocument(GROUP_FIELD_CHAR_LENGTH),"",SPECIES_GROUP_COLUMN);
            txtGroupName.addFocusListener(new FocusAdapter(){
                public void focusLost( FocusEvent e )  {
                    //Added for ISSUE#1896-2551- Error in species Procedures- Premium - start
                    if(tblProtocolSpecies != null && tblProtocolSpecies.getSelectedRow() >= 0) { 
                        int selectedRow = tblProtocolSpecies.getSelectedRow();
                        String groupName = tblProtocolSpecies.getValueAt(selectedRow, 1).toString();
                        ProtocolSpeciesBean speciesBean = (ProtocolSpeciesBean)vecProtocolSpecies.get(selectedRow);
                        boolean duplicate  = false;
                        ProtocolSpeciesBean PspeciesBean;
                        for(int index=0;index<vecProtocolSpecies.size();index++){
                            if(selectedRow != index){
                                PspeciesBean = (ProtocolSpeciesBean)vecProtocolSpecies.get(index);
                                if( !CoeusGuiConstants.EMPTY_STRING.equals(groupName) && groupName.equalsIgnoreCase(PspeciesBean.getSpeciesGroupName())){
                                    duplicate = true;
                                    break;
                                }
                            }
                        }
                        if(duplicate){
                             CoeusOptionPane.showWarningDialog(coeusMessageResources.parseMessageKey(ERRKEY_DUPL_GROUP_NAME));
                             speciesBean.setSpeciesGroupName(CoeusGuiConstants.EMPTY_STRING);
                             tblProtocolSpecies.getModel().setValueAt(CoeusGuiConstants.EMPTY_STRING, selectedRow, 1);
                             tblProtocolSpecies.repaint();
                        }
                        //Added for ISSUE#1896-2551- Error in species Procedures- Premium - end
                        if(tblProtocolSpecies.getCellEditor() !=null){
                            tblProtocolSpecies.getCellEditor().stopCellEditing();
                        }
                    }
                }
            });
            column.setCellEditor(new DefaultCellEditor(txtGroupName));
        }
        //CoeusQA-2551:End
        
        column = tblProtocolSpecies.getColumnModel().getColumn(SPECIES_COLUMN);
        column.setMinWidth(SPECIES_COLUMN_MIN_WIDTH);
        if(TypeConstants.DISPLAY_MODE != functionType && TypeConstants.AMEND_MODE != functionType){
            column.setCellEditor(new SpeciesTypeEditor());          
        }               
        
        column = tblProtocolSpecies.getColumnModel().getColumn(USDA_COLUMN);
        column.setMinWidth(USDA_COLUMN_MIN_WIDTH);
        column.setResizable(false);

        column = tblProtocolSpecies.getColumnModel().getColumn(STRAIN_COLUMN);
        column.setMinWidth(STRAIN_COLUMN_MIN_WIDTH);
        column.setMaxWidth(STRAIN_COLUMN_MAX_WIDTH);
        column.setMinWidth(STRAIN_COLUMN_MIN_WIDTH);
        if(TypeConstants.DISPLAY_MODE != functionType && TypeConstants.AMEND_MODE != functionType){
            column.setCellEditor(new DefaultCellEditor(
                    new CoeusTextField(new LimitedPlainDocument(STRAIN_FIELD_CHAR_LENGTH),"",STRAIN_COLUMN)));
        }
        
        //Modified/Added for-COEUSQA-2798 Add count type to species/group screen-Start
        column = tblProtocolSpecies.getColumnModel().getColumn(COUNT_TYPE_COLUMN);
        column.setMinWidth(COUNT_COLUMN_TYPE_WIDTH);
        column.setMaxWidth(PAIN_CATEGORY_COLUMN_MAX_WIDTH);
        column.setMinWidth(COUNT_COLUMN_TYPE_WIDTH);
        if(TypeConstants.DISPLAY_MODE != functionType && TypeConstants.AMEND_MODE != functionType){
            column.setCellEditor(new SpeciesCountTypeEditor());           
        }
        //Modified/Added for-COEUSQA-2798 Add count type to species/group screen-End                
        column = tblProtocolSpecies.getColumnModel().getColumn(COUNT_COLUMN);
        column.setMinWidth(COUNT_COLUMN_MIN_WIDTH);
        if(TypeConstants.DISPLAY_MODE != functionType && TypeConstants.AMEND_MODE != functionType){
            column.setCellEditor(new DefaultCellEditor(
                    new CoeusTextField(new JTextFieldFilter(JTextFieldFilter.NUMERIC,COUNT_FIELD_CHAR_LENGTH),"",COUNT_COLUMN)));
            column.setResizable(false);
        }
        //Added for COEUSQA-2627_Move pain category in IACUC protocol to Protocol SpeciesGroup level_start
        column = tblProtocolSpecies.getColumnModel().getColumn(PAIN_CATEGORY_COLUMN);
        column.setPreferredWidth(PAIN_CATEGORY_COLUMN_PREFERED_WIDTH);
        column.setMaxWidth(PAIN_CATEGORY_COLUMN_MAX_WIDTH);
        column.setMinWidth(PAIN_CATEGORY_COLUMN_MIN_WIDTH);
        if(TypeConstants.DISPLAY_MODE != functionType && TypeConstants.AMEND_MODE != functionType){
            column.setCellEditor(new PainCategoryTypeEditor());
        }
        // Added for COEUSQA-2627_Move pain category in IACUC protocol to Protocol SpeciesGroup level_End
        
        //COEUSQA-2724-Exceptions should be moved from Scientific Justification in IACUC
        column = tblProtocolSpecies.getColumnModel().getColumn(EXCEPTION_COLUMN);
        column.setMinWidth(EXCEPTION_COLUMN_MIN_WIDTH);
        if(TypeConstants.DISPLAY_MODE != functionType && TypeConstants.AMEND_MODE != functionType){
            column.setCellEditor(new ExceptionCheckBoxEditor()); 
        }
        column.setResizable(false);
        
        tblProtocolSpecies.getTableHeader().setReorderingAllowed( false );
        tblProtocolSpecies.getTableHeader().setResizingAllowed(true);
        tblProtocolSpecies.setFont(CoeusFontFactory.getNormalFont());
        tblProtocolSpecies.getTableHeader().setFont(CoeusFontFactory.getLabelFont());
        }
    
     /*
     * Method to set the Table column properties
     *
     */
     private void setExceptionTableColumnProperties(){
         
        tblExceptions.setOpaque(false);
        tblExceptions.setShowVerticalLines(false);
        tblExceptions.setShowHorizontalLines(false); 
        //Modified for COEUSQA-2711 Increase IACUC Exception description to 1000 characters-Start    
//        tblExceptions.setRowHeight(ROW_HEIGHT);
        //Modified for COEUSQA-2711 Increase IACUC Exception description to 1000 characters-End
        tblExceptions.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        tblExceptions.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        TableColumn column = tblExceptions.getColumnModel().getColumn(ICON_COLUMN);
        column.setMinWidth(ICON_MIN_WIDTH);
        column.setMaxWidth(ICON_MIN_WIDTH);
        column.setPreferredWidth(ICON_MIN_WIDTH);
        column.setHeaderRenderer(new EmptyHeaderRenderer());
        column.setCellRenderer(new IconRenderer());
        column.setResizable(false);
        
        column = tblExceptions.getColumnModel().getColumn(EXCEPTION_CATEGORY_COLUMN);
        column.setMinWidth(EXCEPTION_CATEGORY_MIN_WIDTH);

        //Commented for COEUSQA-2711-Increase IACUC Scientific Justification to 1000 characters-Start
//        if(TypeConstants.DISPLAY_MODE != functionType){
//            column.setCellEditor(new ExceptionsCategoryEditor());
//        }
        //Commented for COEUSQA-2711-Increase IACUC Scientific Justification to 1000 characters-End
        
        column = tblExceptions.getColumnModel().getColumn(DESCRIPTION_COLUMN);
        column.setMinWidth(DESCRIPTION_MIN_WIDTH);
        column.setMaxWidth(DESCRIPTION_MIN_WIDTH);
        //Added for COEUSQA-2711 Increase IACUC Exception description to 1000 characters-Start
        column.setCellRenderer(new TextAreaRenderer());   
        //Added for COEUSQA-2711 Increase IACUC Exception description to 1000 characters-End

        //Commented for COEUSQA-2711-Increase IACUC Scientific Justification to 1000 characters-Start
//        if(TypeConstants.DISPLAY_MODE != functionType){
//            column.setCellEditor(new DefaultCellEditor(
//                    new CoeusTextField(new LimitedPlainDocument(DESCRIPTION_CHAR_LENGTH),"",DESCRIPTION_COLUMN)));
//        }
        //Commented for COEUSQA-2711-Increase IACUC Scientific Justification to 1000 characters-End

        tblExceptions.getTableHeader().setReorderingAllowed( false );
        tblExceptions.getTableHeader().setResizingAllowed(true);
        tblExceptions.setFont(CoeusFontFactory.getNormalFont());
        tblExceptions.getTableHeader().setFont(CoeusFontFactory.getLabelFont());
     }
     
    /** This method uses the notes Vector for the protocol
     *  vecProtocolSpecies and displays the contents of the vector in the table
     *
     */
    public void setFormData(){
        speciesTableModel = new SpeciesTableModel();
        tblProtocolSpecies.setModel(speciesTableModel);
        vecDeletedSpecies = new Vector();
        if ((vecProtocolSpecies != null) && ((vecProtocolSpecies.size())>0)){
            speciesTableModel.setData(vecProtocolSpecies);
            tblProtocolSpecies.setRowSelectionInterval(0,0);
        }
        //COEUSQA-2724-Exceptions should be moved from Scientific Justification in IACUC  
        exceptionsTableModel = new ExceptionsTableModel();
        tblExceptions.setModel(exceptionsTableModel);
    }
    
    //COEUSQA-2724-Exceptions should be moved from Scientific Justification in IACUC
     /*
     * Method to load Exception Category      *
     * @return void
     */
    private void loadExceptionCategory() {
        
        String connectTo = CoeusGuiConstants.CONNECTION_URL+ PROTOCOL_SERVLET;
        RequesterBean request = new RequesterBean();
        Vector vecData = new Vector();
        request.setFunctionType('$');
        AppletServletCommunicator comm = new AppletServletCommunicator(connectTo, request);
        comm.send();
        ResponderBean response = comm.getResponse();
        if (response!=null){
            if(response.isSuccessfulResponse()){
                vecData = response.getDataObjects();
                 cvExceptionsCategory = (CoeusVector)vecData.get(0);
            }
        }     
    }
    
    /** Action Performed Method
     * @param actionEvent Action Event Object
     */
    public void actionPerformed(java.awt.event.ActionEvent actionEvent) {
        Object actionSource = actionEvent.getSource();
        if(actionSource.equals(btnAdd)){
            if(functionType != TypeConstants.DISPLAY_MODE && functionType != TypeConstants.AMEND_MODE){
                if(tblProtocolSpecies.isEditing()){
                    if(tblProtocolSpecies.getCellEditor() != null){
                        tblProtocolSpecies.getCellEditor().stopCellEditing();
                    }
                }
                //Added with CoeusQA-2551:Rework how user enters species,study groups and procedures in IACUC protocols
                int newSpeciesId = getMaxSpeciesId()+1;
                ProtocolSpeciesBean speciesBeanToAdd = new ProtocolSpeciesBean();
                speciesBeanToAdd.setProtocolNumber(protocolNumber);
                speciesBeanToAdd.setSequenceNumber(sequenceNumber);
                speciesBeanToAdd.setSpeciesId(newSpeciesId);
                
                ProtocolSpeciesBean alreadyAddedBean = checkAlreadyDeleted(newSpeciesId);
                if(alreadyAddedBean == null){
                    speciesBeanToAdd.setAcType(TypeConstants.INSERT_RECORD);
                }else{
                    speciesBeanToAdd.setUpdateUser(alreadyAddedBean.getUpdateUser());
                    speciesBeanToAdd.setUpdateTimestamp(alreadyAddedBean.getUpdateTimestamp());
                    speciesBeanToAdd.setAcType(TypeConstants.UPDATE_RECORD);
                }
                vecProtocolSpecies.add(speciesBeanToAdd);
                //CoeusQA-2551:End
                speciesTableModel.setData(vecProtocolSpecies);
                btnDelete.setEnabled(true);
                setSaveRequired(true);
                int lastAddedRow = tblProtocolSpecies.getRowCount()-1;              
                tblProtocolSpecies.setRowSelectionInterval(lastAddedRow,lastAddedRow);
                tblProtocolSpecies.editCellAt(lastAddedRow,SPECIES_GROUP_COLUMN);
                tblProtocolSpecies.getEditorComponent().requestFocusInWindow();                
            }
        }else if(actionSource.equals(btnDelete)){
            if(functionType != TypeConstants.DISPLAY_MODE && functionType != TypeConstants.AMEND_MODE){
                if(tblProtocolSpecies.isEditing()){
                    if(tblProtocolSpecies.getCellEditor() != null){
                        tblProtocolSpecies.getCellEditor().stopCellEditing();
                    }
                }
                int selectedRow = tblProtocolSpecies.getSelectedRow();
                if(selectedRow > -1){
                    ProtocolSpeciesBean speciesBean = (ProtocolSpeciesBean)vecProtocolSpecies.get(selectedRow);                    
                    int selectedOption = CoeusOptionPane.
                            showQuestionDialog(
                            coeusMessageResources.parseMessageKey(
                            SPECIES_DELETE_CONFIRMATION),
                            CoeusOptionPane.OPTION_YES_NO,
                            CoeusOptionPane.DEFAULT_YES);
                    switch(selectedOption){
                        case CoeusOptionPane.SELECTION_YES:
                            
                            //Species code changed to species Id with CoeusQA:2551
                            if(isStudyGroupExistsForSpecies(speciesBean.getSpeciesId())){
                                CoeusOptionPane.showWarningDialog(
                                        coeusMessageResources.parseMessageKey(DELETE_STUDY_GROUP_BEFORE_DEL_SPECIES));
                                return;
                            }
                            if(!TypeConstants.INSERT_RECORD.equals(speciesBean.getAcType())){
                                speciesBean.setAcType(TypeConstants.DELETE_RECORD);
                                vecDeletedSpecies.add(speciesBean);
                                //Delete assosiated Exceptions
                                deleteSpeciesExceptions(speciesBean.getSpeciesExceptions());                                
                            }
                            vecProtocolSpecies.remove(selectedRow);
                            speciesTableModel.setData(vecProtocolSpecies);
                            if(vecProtocolSpecies.size() == 0) {
                                exceptionsTableModel.setData(new Vector());
                                setExceptionButtonsEnabled(false);
                                //Added for internal issue#121 : Short-Cut Keys not working for Procedures - start
                                btnAdd.requestFocusInWindow();
                                btnAdd.setFocusable(true);
                                //Added for internal issue#121 - end
                            }
                            if(selectedRow == 0 && tblProtocolSpecies.getRowCount() > 0){
                                    tblProtocolSpecies.setRowSelectionInterval(ZERO_ROW_INDEX,ZERO_ROW_INDEX);
                            }
                            if(selectedRow-1 > - 1){
                                tblProtocolSpecies.setRowSelectionInterval(selectedRow-1,selectedRow-1);
                            }
                            if(vecProtocolSpecies != null && vecProtocolSpecies.size() < 1){
                                btnDelete.setEnabled(false);
                            }
                            setSaveRequired(true);
                            //Modified for COEUSQA-2633 Ability to indicate which procedures IACUC study personnel will perform and training qualifications for species-Start
                            notifyObserver(vecProtocolSpecies);
                            //Modified for COEUSQA-2633 Ability to indicate which procedures IACUC study personnel will perform and training qualifications for species-End
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
        } 
        //COEUSQA-2724-Exceptions should be moved from Scientific Justification in IACUC
        else if(actionSource.equals(btnAddException)){
            if(functionType != TypeConstants.DISPLAY_MODE){
                int selectedRow = tblProtocolSpecies.getSelectedRow();                
                ProtocolExceptionBean newExceptionBean = new ProtocolExceptionBean();
                newExceptionBean.setProtocolNumber(protocolNumber);
                newExceptionBean.setSequenceNumber(sequenceNumber);
                newExceptionBean.setAcType(TypeConstants.INSERT_RECORD);
                newExceptionBean.setExceptionId(getMaxExceptionsId()+1);
                scientificJustificationExceptionForm = new ScientificJustificationExceptionForm(mdiReference,
                                    newExceptionBean,cvExceptionsCategory,functionType);
                scientificJustificationExceptionForm.showProtocolExceptionForm();
                newExceptionBean = scientificJustificationExceptionForm.getProtocolExceptionBean();
                //Add SpeciesId to associated 'ProtocolExceptionBean'                              
                ProtocolSpeciesBean speciesBean = (ProtocolSpeciesBean)vecProtocolSpecies.get(selectedRow);
                newExceptionBean.setSpeciesId(speciesBean.getSpeciesId());
                
                addNewExceptionBeanToDB(newExceptionBean);   
                //Set exception vector to SpeciesBean.
                speciesBean.setSpeciesExceptions(vecSpeciesExceptions);                
                //Set species action type to 'UPDATE', if exceptions are associated with selected species             
                if(speciesBean.getAcType() == null) {
                    speciesBean.setAcType(TypeConstants.UPDATE_RECORD);
                }
                
                if(tblExceptions.getRowCount()>0){
                    btnDeleteException.setEnabled(true);
                    btnModifyException.setEnabled(true);
                    btnViewException.setEnabled(true);
                }else{
                    btnDeleteException.setEnabled(false);
                    btnModifyException.setEnabled(false);
                    btnViewException.setEnabled(false);
                }
            } 
        }else if(actionSource.equals(btnDeleteException)){
            if(functionType != TypeConstants.DISPLAY_MODE){
                if(tblExceptions.isEditing()){
                    if(tblExceptions.getCellEditor() != null){
                        tblExceptions.getCellEditor().stopCellEditing();
                    }
                }
                selectedExceptionRow = tblExceptions.getSelectedRow();
                if(selectedExceptionRow > -1){
                    int selectedOption = CoeusOptionPane.
                            showQuestionDialog(
                            coeusMessageResources.parseMessageKey(
                            EXCEPTION_DELETE_CONFIRMATION),
                            CoeusOptionPane.OPTION_YES_NO,
                            CoeusOptionPane.DEFAULT_YES);
                    switch(selectedOption){
                        case CoeusOptionPane.SELECTION_YES:
                            int selectedRow = tblProtocolSpecies.getSelectedRow();
                            ProtocolSpeciesBean speciesBean = (ProtocolSpeciesBean)vecProtocolSpecies.get(selectedRow);
                            ProtocolExceptionBean exceptionsBean = (ProtocolExceptionBean)vecSpeciesExceptions.get(selectedExceptionRow);
                            //if(!TypeConstants.INSERT_RECORD.equals(exceptionsBean.getAcType())){
                                exceptionsBean.setAcType(TypeConstants.DELETE_RECORD);
                                vecDeletedExceptions.add(exceptionsBean);
                           // }
                            vecSpeciesExceptions.remove(selectedExceptionRow);
                            exceptionsTableModel.setData(vecSpeciesExceptions);
                            //Added for internal issue#121 : Short-Cut Keys not working for Procedures - start
                            if(vecSpeciesExceptions.size() == 0) {
                                exceptionsTableModel.setData(new Vector());
                                setExceptionButtonsEnabled(true);
                                 btnAddException.requestFocusInWindow();
                                 btnAddException.setFocusable(true);
                            }
                            //Added for internal issue#121 - end
                            if(selectedExceptionRow == 0 && tblExceptions.getRowCount() > 0){
                                    tblExceptions.setRowSelectionInterval(ZERO_ROW_INDEX,ZERO_ROW_INDEX);
                            }
                            if(selectedExceptionRow-1 > - 1){
                                tblExceptions.setRowSelectionInterval(selectedExceptionRow-1,selectedExceptionRow-1);
                            }
                            if(vecSpeciesExceptions != null && vecSpeciesExceptions.size() < 1){
                                btnDeleteException.setEnabled(false);
                                //Added for COEUSQA-2711-Increase IACUC Scientific Justification to 1000 characters-Start
                                btnModifyException.setEnabled(false);
                                btnViewException.setEnabled(false);         
                                //Added for COEUSQA-2711-Increase IACUC Scientific Justification to 1000 characters-End
                            }
                            //Set species action type to 'UPDATE', if exceptions are associated with selected species                             
                            if(speciesBean.getAcType() == null) {
                                speciesBean.setAcType(TypeConstants.UPDATE_RECORD);
                            }
                            setSaveRequired(true);
                            break;
                        case CoeusOptionPane.SELECTION_NO:
                            break;
                        default:
                            break;
                    }
                }else{
                    CoeusOptionPane.showErrorDialog(coeusMessageResources.parseMessageKey(EXCEPTION_ROW_SELECT_TO_DELETE));
                }
            }
        }  
        //Added for COEUSQA-2711-Increase IACUC Scientific Justification to 1000 characters-Start
        else if(actionSource.equals(btnModifyException)){
            if(functionType != TypeConstants.DISPLAY_MODE){ 
                int selectedRow = tblProtocolSpecies.getSelectedRow();
                ProtocolSpeciesBean speciesBean = (ProtocolSpeciesBean)vecProtocolSpecies.get(selectedRow);
                selectedExceptionRow = tblExceptions.getSelectedRow();
                ProtocolExceptionBean newExceptionBean = (ProtocolExceptionBean)vecSpeciesExceptions.get(selectedExceptionRow);
//                newExceptionBean.setProtocolNumber(protocolNumber);
//                newExceptionBean.setSequenceNumber(sequenceNumber);
                if((!TypeConstants.INSERT_RECORD.equals(newExceptionBean.getAcType()))) {
                newExceptionBean.setAcType(TypeConstants.UPDATE_RECORD);                 
                }else{
                    isUpdateBeforeSave = true;
                }
                scientificJustificationExceptionForm = new ScientificJustificationExceptionForm(mdiReference,
                                    newExceptionBean,cvExceptionsCategory,functionType);
                scientificJustificationExceptionForm.showProtocolExceptionForm();
                newExceptionBean = scientificJustificationExceptionForm.getProtocolExceptionBean();                 
                addNewExceptionBeanToDB(newExceptionBean); 
                //Set species action type to 'UPDATE', if exceptions are associated with selected species             
                if(speciesBean.getAcType() == null) {
                    speciesBean.setAcType(TypeConstants.UPDATE_RECORD);
                }
                setSaveRequired(true);
                tblExceptions.setRowSelectionInterval(selectedExceptionRow,selectedExceptionRow);
                btnDeleteException.setEnabled(true);                 
                btnModifyException.setEnabled(true);
                btnViewException.setEnabled(true);                                             
            }
        }else if(actionSource.equals(btnViewException)){                             
                        viewProtocolException();                                                                             
        } //else if(actionSource.equals(tblExceptions.getCol))
        //Added for COEUSQA-2711-Increase IACUC Scientific Justification to 1000 characters-End
    }   
     
    /*
    *This method is used to set the listeners to the components.
    *
    */
    private void setListenersForExceptionTable(){
        coeusMessageResources = CoeusMessageResources.getInstance();        
        tblExceptions.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent me) {
                if (me.getClickCount() == 2) {
                    try {
                         viewProtocolException();
                    }catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
//    Species Code changed to species Id CoeusQA-2551
//    private boolean isStudyGroupExistsForSpecies(int speciesCode){
    private boolean isStudyGroupExistsForSpecies(int speciesId){
        boolean studyGroupExists = false;
        if(vecStudyGroupData != null && vecStudyGroupData.size()>0){
            for(int index=0;index<vecStudyGroupData.size();index++){
                ProtocolStudyGroupBean studyGroupBean = (ProtocolStudyGroupBean)vecStudyGroupData.get(index);
                if(studyGroupBean.getSpeciesId() == speciesId){
                    studyGroupExists = true;
                    break;
                }
            }
        }
        return studyGroupExists;
    }
    /*
     * Model class for the species table
     *
     */
    class SpeciesTableModel extends DefaultTableModel {
        //Added column Species Group Name with CoeusQA-2551
        // Modified for COEUSQA-2627_Move pain category in IACUC protocol to Protocol SpeciesGroup level_start
        //Modified for COEUSQA-2724-Exceptions should be moved from Scientific Justification in IACUC
        private String colNames[] = {"","Group Name","Species","USDA Covered Type","Strain","Pain Category","Count Type","Count","Exception"};
        private Class colTypes[]  = {Object.class, Object.class,Object.class, Boolean.class, String.class,Object.class,Object.class,Integer.class, Boolean.class};
        // Modified for COEUSQA-2627_Move pain category in IACUC protocol to Protocol SpeciesGroup level_ end
        
        public boolean isCellEditable(int row, int column) {
            boolean canEdit = false;
            if(column != ICON_COLUMN &&
                    functionType != TypeConstants.DISPLAY_MODE &&
                    functionType != TypeConstants.AMEND_MODE){
                canEdit = true;
            }
            return canEdit;
        }
        
        public Object getValueAt(int row, int column) {
            if(vecProtocolSpecies != null & vecProtocolSpecies.size() > 0){
                ProtocolSpeciesBean speciesBean = (ProtocolSpeciesBean)vecProtocolSpecies.get(row);
                switch(column) {
                    //Added column Species Group Name with CoeusQA-2551
                    case SPECIES_GROUP_COLUMN:
                        // Modified for COEUSQA-2627_Move pain category in IACUC protocol to Protocol SpeciesGroup level_start
                         return speciesBean.getSpeciesGroupName() == null?"":speciesBean.getSpeciesGroupName();
                       // Modified for COEUSQA-2627_Move pain category in IACUC protocol to Protocol SpeciesGroup level_end  
                    case SPECIES_COLUMN:
                        if(TypeConstants.DISPLAY_MODE != functionType){
                            ComboBoxBean comboBoxBean = new ComboBoxBean("","");
                            int speciesCode = speciesBean.getSpeciesCode();
                            CoeusVector filteredVector = cvSpeciesCode.filter(new Equals("code", ""+speciesCode));
                            if(filteredVector!=null && filteredVector.size() > 0){
                                comboBoxBean = (ComboBoxBean)filteredVector.get(0);
                                
                            }
                            return comboBoxBean;
                        }else{
                            return speciesBean.getSpeciesName();
                        }
                    case USDA_COLUMN:
                        return speciesBean.isUsdaCovered();
                    case STRAIN_COLUMN:
                        return speciesBean.getStrain();
                    case COUNT_COLUMN:
                        return new Integer(speciesBean.getSpeciesCount());
                    //Added for-COEUSQA-2798 Add count type to species/group screen-Start
                    case COUNT_TYPE_COLUMN:
                        if(TypeConstants.DISPLAY_MODE != functionType){
                            ComboBoxBean comboBoxBean = new ComboBoxBean("","");
                            int countTypeCode = speciesBean.getSpeciesCountTypeCode();
                            CoeusVector filteredVector = cvSpeciesCountType.filter(new Equals("code", ""+countTypeCode));
                            if(filteredVector!=null && filteredVector.size() > 0){
                                comboBoxBean = (ComboBoxBean)filteredVector.get(0);
                                
                            }
                            return comboBoxBean;
                        }else{
                            return speciesBean.getspeciesCountTypeName();
                        }
                    //Added for-COEUSQA-2798 Add count type to species/group screen-End
                    // Added for COEUSQA-2627_Move pain category in IACUC protocol to Protocol SpeciesGroup level_start
                    case PAIN_CATEGORY_COLUMN:
                        if(TypeConstants.DISPLAY_MODE != functionType){
                            ComboBoxBean comboBoxBean = new ComboBoxBean("","");
                            int painCategoryCode = speciesBean.getPainCategoryCode();
                            CoeusVector filteredVector = cvPainCategory.filter(new Equals("code", ""+painCategoryCode));
                            if(filteredVector!=null && filteredVector.size() > 0){
                                comboBoxBean = (ComboBoxBean)filteredVector.get(0);
                                
                            }
                            return comboBoxBean;
                        }else{
                            return speciesBean.getPainCategoryName();
                        }    
                    // Added for COEUSQA-2627_Move pain category in IACUC protocol to Protocol SpeciesGroup level_end
                     //COEUSQA-2724-Exceptions should be moved from Scientific Justification in IACUC 
                     case EXCEPTION_COLUMN:
                        return speciesBean.isExceptionsPresent();
                    default:
                        break;
                }
            }
            return CoeusGuiConstants.EMPTY_STRING;
        }

        public int getColumnCount() {
            return colNames.length;
        }
        
        public int getRowCount() {
            return dataVector.size();
        }
        public String getColumnName(int colIndex) {
            return colNames[colIndex];
        }
        
        public void setData(Vector cvData) {
            dataVector = cvData;
            fireTableDataChanged();
        }
        
        public Class getColumnClass(int colIndex) {
            return colTypes[colIndex];
        }
        
        public void setValueAt(Object value, int row, int column) {
            if(TypeConstants.DISPLAY_MODE == functionType ||
                    value == null ||
                    vecProtocolSpecies == null ||
                    (vecProtocolSpecies != null && vecProtocolSpecies.size() < 1)){
                return;
            }
            ProtocolSpeciesBean speciesBean = (ProtocolSpeciesBean)vecProtocolSpecies.get(row);
            switch(column){
                //Added column Species Group Name with CoeusQA-2551
                case SPECIES_GROUP_COLUMN:
                   //Commented for ISSUE#1896-2551- Error in species Procedures- Premium - start
//                    boolean duplicate  = false;
//                    String groupName;
//                    ProtocolSpeciesBean PspeciesBean;
//                    for(int index=0;index<vecProtocolSpecies.size();index++){
//                        if(row != index){
//                            PspeciesBean = (ProtocolSpeciesBean)vecProtocolSpecies.get(index);
//                            groupName = value.toString();
//                            if( !CoeusGuiConstants.EMPTY_STRING.equals(groupName) && groupName.equalsIgnoreCase(PspeciesBean.getSpeciesGroupName())){
//                                duplicate = true;
//                                break;
//                            }
//                        }
//                    }
//                    if(duplicate){                    
//                        CoeusOptionPane.showWarningDialog(coeusMessageResources.parseMessageKey(ERRKEY_DUPL_GROUP_NAME));
//                        speciesBean.setSpeciesGroupName(CoeusGuiConstants.EMPTY_STRING);
//                        fireTableRowsUpdated(row-1,row-1);
//                    }else{
//                        speciesBean.setSpeciesGroupName(value.toString().trim());
//                    }
                    speciesBean.setSpeciesGroupName(value.toString().trim());
                    //Added for ISSUE#1896-2551- Error in species Procedures- Premium - end
                    //Modified for COEUSQA-2633 Ability to indicate which procedures IACUC study personnel will perform and training qualifications for species-Start
                    notifyObserver(vecProtocolSpecies);
                    //Modified for COEUSQA-2633 Ability to indicate which procedures IACUC study personnel will perform and training qualifications for species-End
                    break;
                case SPECIES_COLUMN :
                    ComboBoxBean speciesComboBoxBean = (ComboBoxBean)value;
                    if(speciesComboBoxBean != null && !CoeusGuiConstants.EMPTY_STRING.equals(speciesComboBoxBean.getCode())){
                        speciesBean.setSpeciesCode(Integer.parseInt(speciesComboBoxBean.getCode()));
                        speciesBean.setSpeciesName(speciesComboBoxBean.getDescription());
                    }else{
                        speciesBean.setSpeciesCode(0);
                        speciesBean.setSpeciesName(CoeusGuiConstants.EMPTY_STRING);
                        
                    }
//                    observable.notifyObservers(vecProtocolSpecies);
                    break;
                case USDA_COLUMN :
                    speciesBean.setUsdaCovered(((Boolean)value).booleanValue());
                    break;
                case STRAIN_COLUMN :
                    speciesBean.setStrain(value.toString().trim());
                    break;
                case COUNT_COLUMN :
                    if(value == null || CoeusGuiConstants.EMPTY_STRING.equals(value)){
                        value = "0";
                    }
                    int speciesCount = Integer.parseInt(value.toString().trim());
                    speciesBean.setSpeciesCount(speciesCount);
                    break;
                //Added for-COEUSQA-2798 Add count type to species/group screen-Start    
                case COUNT_TYPE_COLUMN :
                    ComboBoxBean countTypeComboBoxBean = (ComboBoxBean)value;
                    if(countTypeComboBoxBean != null && !CoeusGuiConstants.EMPTY_STRING.equals(countTypeComboBoxBean.getCode())){
                        speciesBean.setSpeciesCountTypeCode(Integer.parseInt(countTypeComboBoxBean.getCode()));
                        speciesBean.setSpeciesCountTypeName(countTypeComboBoxBean.getDescription());
                    }else{
                        speciesBean.setSpeciesCountTypeCode(0);
                        speciesBean.setSpeciesCountTypeName(CoeusGuiConstants.EMPTY_STRING);
                        
                    }
                    break;
                //Added for-COEUSQA-2798 Add count type to species/group screen-End
                // Added for COEUSQA-2627_Move pain category in IACUC protocol to Protocol SpeciesGroup level_start
                case PAIN_CATEGORY_COLUMN :
                    ComboBoxBean painCategoryComboBox = (ComboBoxBean)value;
                    if(painCategoryComboBox != null && !CoeusGuiConstants.EMPTY_STRING.equals(painCategoryComboBox.getCode())){
                        speciesBean.setPainCategoryCode(Integer.parseInt(painCategoryComboBox.getCode()));
                        speciesBean.setPainCategoryName(painCategoryComboBox.getDescription());
                    }else{
                        speciesBean.setPainCategoryCode(0);
                        speciesBean.setPainCategoryName(CoeusGuiConstants.EMPTY_STRING);
                        
                    } 
                    break;
                // Added for COEUSQA-2627_Move pain category in IACUC protocol to Protocol SpeciesGroup level_end
                    
                //COEUSQA-2724-Exceptions should be moved from Scientific Justification in IACUC
                case EXCEPTION_COLUMN :
                    speciesBean.setExceptionsPresent(((Boolean)value).booleanValue());
                    break;
                default:
                    break;

            }

        }
         
    }
    
    /*
     * Editor for Species type combo box
     */
    class SpeciesTypeEditor extends AbstractCellEditor implements TableCellEditor{
        private JComboBox cmbSpecies;
        private boolean populated = false;
        private int column;
        SpeciesTypeEditor() {
            cmbSpecies = new JComboBox();
            //Modified with CoeusQA-2551:Rework how user enters species,study groups and procedures in IACUC protocols
            cmbSpecies.addItemListener(new ItemListener() {
                public void itemStateChanged(ItemEvent e) {
                    if(e.getStateChange() == ItemEvent.SELECTED){
                        stopCellEditing();
                        //Modified for COEUSQA-2633 Ability to indicate which procedures IACUC study personnel will perform and training qualifications for species-Start
                        notifyObserver(vecProtocolSpecies);
                        //Modified for COEUSQA-2633 Ability to indicate which procedures IACUC study personnel will perform and training qualifications for species-End
                        setSaveRequired(true);
                    }
                }
            });
            //CoeusQA-2551 End
        }                
        private void populateCombo() {
            cmbSpecies.setModel(new DefaultComboBoxModel(cvSpeciesCode));
        }
        
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            ComboBoxBean comboBoxBean = (ComboBoxBean)value;
            this.column = column;
            switch(column) {
                case SPECIES_COLUMN:
                    if(! populated) {
                        populateCombo();
                        populated = true;
                    }
//                    if(comboBoxBean != null && (comboBoxBean.getDescription() != null &&
//                            !comboBoxBean.getDescription().equals(CoeusGuiConstants.EMPTY_STRING))) {
                        if(comboBoxBean != null && (comboBoxBean.getDescription() == null ||
                                comboBoxBean.getDescription().equals(CoeusGuiConstants.EMPTY_STRING))) {
                        ComboBoxBean selBean = (ComboBoxBean)cvSpeciesCode.get(0);
                        cmbSpecies.setSelectedItem(selBean);
                        return cmbSpecies;
                    }
                    cmbSpecies.setSelectedItem(value);
                    return cmbSpecies;
                default:
                    break;
            }
            return null;
        }
        
        public Object getCellEditorValue() {
            this.column = column;
            switch(column) {
                case SPECIES_COLUMN:
                    return cmbSpecies.getSelectedItem();
                default:
                    break;
            }
            return cmbSpecies;
        }
        public int getClickCountToStart(){
            return 1;
        }
    }

    // Added for COEUSQA-2627_Move pain category in IACUC protocol to Protocol SpeciesGroup level_start
   /**
     * Editor for Pain Category type combo box
     */
    class PainCategoryTypeEditor extends AbstractCellEditor implements TableCellEditor{
        private JComboBox cmbPainCategory;
        private boolean populated = false;
        private int column;
        PainCategoryTypeEditor() {
            cmbPainCategory = new JComboBox();            
            cmbPainCategory.addItemListener(new ItemListener() {
                public void itemStateChanged(ItemEvent e) {
                    if(e.getStateChange() == ItemEvent.SELECTED){
                        stopCellEditing();
                        //Modified for COEUSQA-2633 Ability to indicate which procedures IACUC study personnel will perform and training qualifications for species-Start
                        notifyObserver(vecProtocolSpecies);
                        //Modified for COEUSQA-2633 Ability to indicate which procedures IACUC study personnel will perform and training qualifications for species-End
                        setSaveRequired(true);
                    }
                }
            });          
        }
        
        /**
         * Populateing the Pain Category drop down
         */
        private void populateCombo() {
            cmbPainCategory.setModel(new DefaultComboBoxModel(cvPainCategory));
        }
        
        /**
         * Get editor component 
         * @param JTable table
         * @param Object value
         * @param boolean isSelected
         * @param int row
         * @param int column
         * @return Component editor component
         */
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            ComboBoxBean comboBoxBean = (ComboBoxBean)value;
            this.column = column;
            switch(column) {
                case PAIN_CATEGORY_COLUMN:
                    if(! populated) {
                        populateCombo();
                        populated = true;
                    }
                        if(comboBoxBean != null && (comboBoxBean.getDescription() == null ||
                                comboBoxBean.getDescription().equals(CoeusGuiConstants.EMPTY_STRING))) {
                        ComboBoxBean selBean = (ComboBoxBean)cvPainCategory.get(0);
                        cmbPainCategory.setSelectedItem(selBean);
                        return cmbPainCategory;
                    }
                    cmbPainCategory.setSelectedItem(value);
                    return cmbPainCategory;
                default:
                    break;
            }
            return null;
        }
        
        /**
         * Get value of cell editor
         * @return Object
         */
        public Object getCellEditorValue() {
            this.column = column;
            switch(column) {
                case PAIN_CATEGORY_COLUMN:
                    return cmbPainCategory.getSelectedItem();
                default:
                    break;
            }
            return cmbPainCategory;
        }
        
    }
    // Added for COEUSQA-2627_Move pain category in IACUC protocol to Protocol SpeciesGroup level_end
    
    //Added for-COEUSQA-2798 Add count type to species/group screen-Start
    /**
     * Editor for Species count type combo box
     */
    class SpeciesCountTypeEditor extends AbstractCellEditor implements TableCellEditor{
        private JComboBox cmbSpeciesCountType;
        private boolean populated = false;
        private int column;
        SpeciesCountTypeEditor() {
            cmbSpeciesCountType = new JComboBox();            
            cmbSpeciesCountType.addItemListener(new ItemListener() {
                public void itemStateChanged(ItemEvent e) {
                    if(e.getStateChange() == ItemEvent.SELECTED){
                        stopCellEditing();                         
                        setSaveRequired(true);
                    }
                }
            });          
        }
        
        /**
         * Populateing the Pain Category drop down
         */
        private void populateCombo() {
            cmbSpeciesCountType.setModel(new DefaultComboBoxModel(cvSpeciesCountType));
        }
        
        /**
         * Get editor component 
         * @param JTable table
         * @param Object value
         * @param boolean isSelected
         * @param int row
         * @param int column
         * @return Component editor component
         */
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            ComboBoxBean comboBoxBean = (ComboBoxBean)value;
            this.column = column;
            switch(column) {
                case COUNT_TYPE_COLUMN:
                    if(! populated) {
                        populateCombo();
                        populated = true;
                    }
                        if(comboBoxBean != null && (comboBoxBean.getDescription() == null ||
                                comboBoxBean.getDescription().equals(CoeusGuiConstants.EMPTY_STRING))) {
                        ComboBoxBean selBean = (ComboBoxBean)cvSpeciesCountType.get(0);
                        cmbSpeciesCountType.setSelectedItem(selBean);
                        return cmbSpeciesCountType;
                    }
                    cmbSpeciesCountType.setSelectedItem(value);
                    return cmbSpeciesCountType;
                default:
                    break;
            }
            return null;
        }
        
        /**
         * Get value of cell editor
         * @return Object
         */
        public Object getCellEditorValue() {
            this.column = column;
            switch(column) {
                case COUNT_TYPE_COLUMN:
                    return cmbSpeciesCountType.getSelectedItem();
                default:
                    break;
            }
            return cmbSpeciesCountType;
        }
        
    }
    //Added for-COEUSQA-2798 Add count type to species/group screen-End
    
    //COEUSQA-2724-Exceptions should be moved from Scientific Justification in IACUC
     class ExceptionCheckBoxEditor extends AbstractCellEditor implements TableCellEditor{
        private JCheckBox chkException;
        private int column;
        ExceptionCheckBoxEditor() {
            chkException = new JCheckBox();
            chkException.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    if(e.getSource() == chkException){
                        stopCellEditing();                       
                        performExceptionOperations();
                        setSaveRequired(true);
                    }
                }
            });            
        }   

        public Object getCellEditorValue() {
            this.column = column;
            switch(column) {
                case EXCEPTION_COLUMN:
                    return chkException.isSelected();
                default:
                    break;
            }
            return chkException;
        }

        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            this.column = column;
            switch(column) {
                case EXCEPTION_COLUMN:                   
                    chkException.setSelected((Boolean)value);
                    chkException.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
                    return chkException;
                default:
                    break;
            }
            return null;
        }   
        
        private void performExceptionOperations() {
            int selectedRow = tblProtocolSpecies.getSelectedRow();
            ProtocolSpeciesBean speciesBean = null;
            if(vecProtocolSpecies != null && vecProtocolSpecies.size() > 0) {
                speciesBean = (ProtocolSpeciesBean)vecProtocolSpecies.get(selectedRow);
                //Validate Species Group and Species Type
                if(speciesBean != null && speciesBean.getSpeciesExceptions() != null 
                        && speciesBean.getSpeciesExceptions().size() > 0){          

                    tblProtocolSpecies.getModel().setValueAt((Boolean)true, selectedRow, EXCEPTION_COLUMN );                    
                    CoeusOptionPane.showWarningDialog(
                                coeusMessageResources.parseMessageKey(DESELECT_MESSAGE_FOR_EXCEPTION));         
                } else {
                     btnAddException.setEnabled(chkException.isSelected());
                }                
            }                
        }
    }

    //COEUSQA-2724-Exceptions should be moved from Scientific Justification in IACUC
     /*
     * Model class for the Exception table
     *
     */
    class ExceptionsTableModel extends DefaultTableModel {
        private String colNames[] = {"","Category","Description"};
        private Class colTypes[]  = {Object.class, Object.class, String.class};
        
        public boolean isCellEditable(int row, int column) {
            boolean canEdit = false;             
            return canEdit;
        }
        
        public Object getValueAt(int row, int column) {
            if(vecSpeciesExceptions != null && vecSpeciesExceptions.size() > 0 && vecSpeciesExceptions.size() >= row){
                ProtocolExceptionBean exceptionsBean = (ProtocolExceptionBean)vecSpeciesExceptions.get(row);
                switch(column) {
                    case EXCEPTION_CATEGORY_COLUMN:
                        if(TypeConstants.DISPLAY_MODE != functionType){
                            ComboBoxBean comboBoxBean = new ComboBoxBean("","");
                            int exceptionCategoryCode = exceptionsBean.getExceptionCategoryCode();
                            CoeusVector filteredVector = cvExceptionsCategory.filter(new Equals("code", ""+exceptionCategoryCode));
                            if(filteredVector!=null && filteredVector.size() > 0){
                                comboBoxBean = (ComboBoxBean)filteredVector.get(0);
                                
                            }
                            return comboBoxBean;
                        }else{
                            return exceptionsBean.getExceptionCategoryDesc();
                        }
                    case DESCRIPTION_COLUMN:
                        return exceptionsBean.getExceptionDescription();
                    default:
                        break;
                }
            }
            return CoeusGuiConstants.EMPTY_STRING;
        }

        public int getColumnCount() {
            return colNames.length;
        }
        
        public String getColumnName(int colIndex) {
            return colNames[colIndex];
        }
        
        public void setData(Vector cvData) {
            dataVector = cvData;
            fireTableDataChanged();
        }
        
        public Class getColumnClass(int colIndex) {
            return colTypes[colIndex];
        }
        
        public void setValueAt(Object value, int row, int column) {
            if(TypeConstants.DISPLAY_MODE == functionType || value == null ||
                    vecSpeciesExceptions == null ||
                    (vecSpeciesExceptions != null && vecSpeciesExceptions.size() < 1) && vecSpeciesExceptions.size() >= row){
                return;
            }
            ProtocolExceptionBean exceptionsBean = (ProtocolExceptionBean)vecSpeciesExceptions.get(row);
            switch(column){
                case EXCEPTION_CATEGORY_COLUMN :
                    ComboBoxBean exceptionsComboBoxBean = (ComboBoxBean)value;
                    if(exceptionsComboBoxBean != null && !"".equals(exceptionsComboBoxBean.getCode())){
                        exceptionsBean.setExceptionCategoryCode(Integer.parseInt(exceptionsComboBoxBean.getCode()));
                        exceptionsBean.setExceptionCategoryDesc(exceptionsComboBoxBean.getDescription());
                    }
                    break;
                case DESCRIPTION_COLUMN :                   
                    exceptionsBean.setExceptionDescription(value.toString().trim());
                    break; 
                default:
                    break;

            }
        }  
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        pnlButtons = new javax.swing.JPanel();
        btnAdd = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        pnlExceptionButtons = new javax.swing.JPanel();
        btnAddException = new javax.swing.JButton();
        btnModifyException = new javax.swing.JButton();
        btnViewException = new javax.swing.JButton();
        btnDeleteException = new javax.swing.JButton();
        scrPnProtocolNotesViewer = new javax.swing.JScrollPane();
        tblProtocolSpecies = new javax.swing.JTable();
        scrPnExceptions = new javax.swing.JScrollPane();
        tblExceptions = new javax.swing.JTable();

        jMenu1.setText("Menu");
        jMenuBar1.add(jMenu1);

        setLayout(new java.awt.GridBagLayout());

        setMaximumSize(new java.awt.Dimension(980, 510));
        setMinimumSize(new java.awt.Dimension(980, 510));
        setPreferredSize(new java.awt.Dimension(980, 510));
        pnlButtons.setLayout(new java.awt.GridBagLayout());

        pnlButtons.setMaximumSize(new java.awt.Dimension(120, 100));
        pnlButtons.setMinimumSize(new java.awt.Dimension(120, 100));
        pnlButtons.setPreferredSize(new java.awt.Dimension(120, 100));
        btnAdd.setMnemonic('A');
        btnAdd.setText("Add");
        btnAdd.setToolTipText("");
        btnAdd.setMaximumSize(new java.awt.Dimension(100, 26));
        btnAdd.setMinimumSize(new java.awt.Dimension(100, 26));
        btnAdd.setPreferredSize(new java.awt.Dimension(100, 26));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 0);
        pnlButtons.add(btnAdd, gridBagConstraints);

        btnDelete.setIcon(new javax.swing.ImageIcon(""));
        btnDelete.setMnemonic('e');
        btnDelete.setText("Delete");
        btnDelete.setToolTipText("");
        btnDelete.setMaximumSize(new java.awt.Dimension(100, 26));
        btnDelete.setMinimumSize(new java.awt.Dimension(100, 26));
        btnDelete.setPreferredSize(new java.awt.Dimension(100, 26));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 0);
        pnlButtons.add(btnDelete, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        add(pnlButtons, gridBagConstraints);

        pnlExceptionButtons.setLayout(new java.awt.GridBagLayout());

        pnlExceptionButtons.setMaximumSize(new java.awt.Dimension(120, 150));
        pnlExceptionButtons.setMinimumSize(new java.awt.Dimension(120, 150));
        pnlExceptionButtons.setPreferredSize(new java.awt.Dimension(120, 150));
        btnAddException.setFont(CoeusFontFactory.getLabelFont());
        btnAddException.setMnemonic('d');
        btnAddException.setText("Add");
        btnAddException.setToolTipText("");
        btnAddException.setMaximumSize(new java.awt.Dimension(100, 26));
        btnAddException.setMinimumSize(new java.awt.Dimension(100, 26));
        btnAddException.setPreferredSize(new java.awt.Dimension(100, 26));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 0);
        pnlExceptionButtons.add(btnAddException, gridBagConstraints);

        btnModifyException.setFont(CoeusFontFactory.getLabelFont());
        btnModifyException.setMnemonic('M');
        btnModifyException.setText("Modify");
        btnModifyException.setToolTipText("");
        btnModifyException.setMaximumSize(new java.awt.Dimension(100, 26));
        btnModifyException.setMinimumSize(new java.awt.Dimension(100, 26));
        btnModifyException.setPreferredSize(new java.awt.Dimension(100, 26));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 0);
        pnlExceptionButtons.add(btnModifyException, gridBagConstraints);

        btnViewException.setFont(CoeusFontFactory.getLabelFont());
        btnViewException.setMnemonic('V');
        btnViewException.setText("View");
        btnViewException.setToolTipText("");
        btnViewException.setMaximumSize(new java.awt.Dimension(100, 26));
        btnViewException.setMinimumSize(new java.awt.Dimension(100, 26));
        btnViewException.setPreferredSize(new java.awt.Dimension(100, 26));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 0);
        pnlExceptionButtons.add(btnViewException, gridBagConstraints);

        btnDeleteException.setFont(CoeusFontFactory.getLabelFont());
        btnDeleteException.setMnemonic('l');
        btnDeleteException.setText("Delete");
        btnDeleteException.setToolTipText("");
        btnDeleteException.setMaximumSize(new java.awt.Dimension(100, 26));
        btnDeleteException.setMinimumSize(new java.awt.Dimension(100, 26));
        btnDeleteException.setPreferredSize(new java.awt.Dimension(100, 26));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 0);
        pnlExceptionButtons.add(btnDeleteException, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        add(pnlExceptionButtons, gridBagConstraints);

        scrPnProtocolNotesViewer.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        scrPnProtocolNotesViewer.setMaximumSize(new java.awt.Dimension(870, 250));
        scrPnProtocolNotesViewer.setMinimumSize(new java.awt.Dimension(870, 250));
        scrPnProtocolNotesViewer.setPreferredSize(new java.awt.Dimension(870, 250));
        tblProtocolSpecies.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        scrPnProtocolNotesViewer.setViewportView(tblProtocolSpecies);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        add(scrPnProtocolNotesViewer, gridBagConstraints);

        scrPnExceptions.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Exceptions", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, CoeusFontFactory.getLabelFont()));
        scrPnExceptions.setMaximumSize(new java.awt.Dimension(870, 250));
        scrPnExceptions.setMinimumSize(new java.awt.Dimension(870, 250));
        scrPnExceptions.setPreferredSize(new java.awt.Dimension(870, 215));
        tblExceptions.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        scrPnExceptions.setViewportView(tblExceptions);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        add(scrPnExceptions, gridBagConstraints);

    }// </editor-fold>//GEN-END:initComponents
    
    /** Getter for property saveRequired.
     * @return Value of property saveRequired.
     */
    public boolean isSaveRequired() {
        if(functionType == TypeConstants.AMEND_MODE && 
                vecProtocolSpecies != null && 
                vecProtocolSpecies.size()>0){
            return true;
        }
        return saveRequired;
    }
    
    
    /** This method use to implement focus on first editable component in this page.
     */
    public void setDefaultFocusForComponent(){
        if(tblProtocolSpecies.getRowCount() > 0 ) {
            tblProtocolSpecies.requestFocusInWindow();
            tblProtocolSpecies.setRowSelectionInterval(ZERO_ROW_INDEX, ZERO_ROW_INDEX);
            tblProtocolSpecies.setColumnSelectionInterval(ZERO_ROW_INDEX,1);
        }else if(btnAdd.isEnabled()) {
            btnAdd.requestFocusInWindow();
        }
    }

    
    /** Setter for property saveRequired.
     * @param saveRequired New value of property saveRequired.
     */
    public void setSaveRequired(boolean saveRequired) {
        this.saveRequired = saveRequired;
    }
    
    /** Getter for property functionType.
     * @return Value of property functionType.
     */
    public char getFunctionType() {
        return functionType;
    }
    
    /** Setter for property functionType.
     * @param functionType New value of property functionType.
     */
    public void setFunctionType(char functionType) {
        this.functionType = functionType;
    }
    
    /**
     * Getter for property vecProtocolSpecies.
     *
     * @return Value of property vecProtocolSpecies.
     */
    public Vector getProtocolSpecies() {
        return vecProtocolSpecies;
    }
    
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
    
    
    /*
     * Method to set the species code for Species form
     * @param cvSpeciesCode
     */
    public void setSpeciesCodes(CoeusVector cvSpeciesCode){
        this.cvSpeciesCode = cvSpeciesCode;
    }
    
    /*
     * Method to get the species code
     * @return vecSpeciesCodes
     */
    public CoeusVector getSpeciesCodes(){
        
        return cvSpeciesCode;
    }
    
    // Added for COEUSQA-2627_Move pain category in IACUC protocol to Protocol SpeciesGroup level_start
    /*
     * Method to set the pain category code for Species form
     * @param cvPainCategory
     */
    public void setPainCategoryCodes(CoeusVector cvPainCategory){
        this.cvPainCategory = cvPainCategory;
    }
    // Added for COEUSQA-2627_Move pain category in IACUC protocol to Protocol SpeciesGroup level_end
    
    //Added for-COEUSQA-2798 Add count type to species/group screen-Start  
    /*
     * Method to set the Species count type for Species form
     * @param cvSpeciesCountType
     */
    public void setSpeciesCountType(CoeusVector cvSpeciesCountType){
        this.cvSpeciesCountType = cvSpeciesCountType;
    }
   //Added for-COEUSQA-2798 Add count type to species/group screen-End  
    
    /*
     * Method to get the max species id
     * @return maxSpeciesId - int
     */
    private int getMaxSpeciesId(){
        int maxSpeciesId = 0;
        if(vecProtocolSpecies != null && vecProtocolSpecies.size() > 0){
            for(int i=0;i<vecProtocolSpecies.size();i++){
                ProtocolSpeciesBean speciesBean = (ProtocolSpeciesBean)vecProtocolSpecies.get(i);
                if(speciesBean.getSpeciesId()>maxSpeciesId){
                    maxSpeciesId = speciesBean.getSpeciesId();
                }
            }
        }
        return maxSpeciesId;
    }

    /*
     * Mehthod to set Species bean property
     */
    private void setPropertyChangeSettings(){
        if(functionType != TypeConstants.AMEND_MODE &&
                vecProtocolSpecies != null && vecProtocolSpecies.size() > 0){
            for(int index=0;index<vecProtocolSpecies.size();index++){
                oldSpeciesData = (ProtocolSpeciesBean)vecProtocolSpecies.get(index);
                oldSpeciesData.addPropertyChangeListener(new PropertyChangeListener() {
                    public void propertyChange(PropertyChangeEvent pce) {
                        if( !( pce.getOldValue() == null && pce.getNewValue() == null ) ){
                            ProtocolSpeciesBean speciesBean = (ProtocolSpeciesBean)vecProtocolSpecies.get(tblProtocolSpecies.getSelectedRow());
                            speciesBean.setAcType(TypeConstants.UPDATE_RECORD);
                            setSaveRequired(true);
                        }
                    }
                });
            }
        }
    }
    
    /*
     * Mehthod to validate the Species form
     *
     */
    public boolean validateData()throws CoeusUIException{
        if(tblProtocolSpecies.isEditing()){
            if(tblProtocolSpecies.getCellEditor() != null){
                tblProtocolSpecies.getCellEditor().stopCellEditing();
            }
        }
        boolean validate = true;
        int rowCount = tblProtocolSpecies.getRowCount();
        if(rowCount >= 0){
            //CoeusQA-2551:Rework how user enters species,study groups and procedures in IACUC protocols
            //check group name duplication and empty group name
            ProtocolSpeciesBean speciesBean,speciesBeanComp;
            String speciesGroup,speciesGroupComp;
            for(int index=0;index<vecProtocolSpecies.size();index++){
                
                speciesBean = (ProtocolSpeciesBean)vecProtocolSpecies.get(index);
                speciesGroup = speciesBean.getSpeciesGroupName();
                if(speciesGroup == null || CoeusGuiConstants.EMPTY_STRING.equals(speciesGroup)){
                    validate = false;
                    tblProtocolSpecies.setRowSelectionInterval(index,index);
                    tblProtocolSpecies.setColumnSelectionInterval(SPECIES_GROUP_COLUMN,SPECIES_GROUP_COLUMN);
                    errorMessage(coeusMessageResources.parseMessageKey(ERRKEY_EMPTY_GROUP_NAME));
                    break;
                }else{
                    for(int i=0;i<vecProtocolSpecies.size();i++){
                        if(i != index){
                            speciesBeanComp = (ProtocolSpeciesBean)vecProtocolSpecies.get(i);
                            speciesGroupComp = speciesBeanComp.getSpeciesGroupName();
                            if(speciesGroup.equalsIgnoreCase(speciesGroupComp)){
                                validate = false;
                                tblProtocolSpecies.setRowSelectionInterval(index,index);
                                tblProtocolSpecies.setColumnSelectionInterval(SPECIES_GROUP_COLUMN,SPECIES_GROUP_COLUMN);
                                //Added for ISSUE#1896-2551- Error in species Procedures- Premium - start
                                tblProtocolSpecies.setValueAt(CoeusGuiConstants.EMPTY_STRING, i, SPECIES_GROUP_COLUMN);
                                tblProtocolSpecies.repaint();
                                //Added for ISSUE#1896-2551- Error in species Procedures- Premium - end
                                errorMessage(coeusMessageResources.parseMessageKey(ERRKEY_DUPL_GROUP_NAME));             
                                break;
                            }
                        }
                    }
                }
            }
           
                //check whether species is selected or not.
                //Added for-COEUSQA-2508 Some schools do not use pain category-Start
                boolean isPainCategoryMandotory = isPainCategoryMandatory();
                //Added for-COEUSQA-2508 Some schools do not use pain category-End
                for(int inInd=0; inInd < rowCount ;inInd++){
                    String stSpeciesType =
                            (String)((DefaultTableModel) tblProtocolSpecies.getModel()).getValueAt(inInd,SPECIES_COLUMN).toString();
                    if((stSpeciesType == null) || (stSpeciesType.trim().length() <= 0)){
                        validate = false;
                        tblProtocolSpecies.setRowSelectionInterval(inInd,inInd);
                        tblProtocolSpecies.setColumnSelectionInterval(SPECIES_COLUMN,SPECIES_COLUMN);
                        errorMessage(coeusMessageResources.parseMessageKey(SELECT_SPECIES_TYPE));
                        break;
                    }
                    //Added for-COEUSQA-2508 Some schools do not use pain category-Start
                    if(isPainCategoryMandotory){
                    //Added for-COEUSQA-2508 Some schools do not use pain category-End
                    // Added for COEUSQA-2627_Move pain category in IACUC protocol to Protocol SpeciesGroup level_start                    
                    String paintCategoryType =
                            (String)((DefaultTableModel) tblProtocolSpecies.getModel()).getValueAt(inInd,PAIN_CATEGORY_COLUMN).toString();
                    if((paintCategoryType == null) || ("".equals(paintCategoryType))){
                        validate = false;
                        tblProtocolSpecies.setRowSelectionInterval(inInd,inInd);
                        tblProtocolSpecies.setColumnSelectionInterval(PAIN_CATEGORY_COLUMN,PAIN_CATEGORY_COLUMN);
                        errorMessage(coeusMessageResources.parseMessageKey(SELECT_PAIN_CATEGORY_TYPE));
                        break;
                    }
                    // Added for COEUSQA-2627_Move pain category in IACUC protocol to Protocol SpeciesGroup level_end  
                    //Added for-COEUSQA-2508 Some schools do not use pain category-Start
                    }
                    //Added for-COEUSQA-2508 Some schools do not use pain category-End
            }
        }
        //CoeusQA-2551:End
        return validate;
    }
    
    private void errorMessage(String mesg) throws CoeusUIException{
        CoeusUIException coeusUIException = new CoeusUIException(mesg,CoeusUIException.WARNING_MESSAGE);
        coeusUIException.setTabIndex(CoeusGuiConstants.IACUC_SPECIES_EXCEP_TAB_INDEX);
        throw coeusUIException;
    }

    /*
     * MEthod to get the Protocol species data
     * @return vecSpeices
     */
     public Vector getSpeciesData(){
         Vector vecAllSpecies = new Vector();
         vecAllSpecies.addAll(vecDeletedSpecies);
         vecAllSpecies.addAll(vecProtocolSpecies);
         addDeletedExceptionsToBean(vecAllSpecies);          
         return setAcTypesInAmendment(vecAllSpecies);
     }
     
     /**
      * Registers the Observer
      */
     public void registerObserver( Observer observer ) {
         observable.addObserver( observer );
     }
     
     /**
      * UnRegisters the Observer
      */
     public void unRegisterObserver( Observer observer ) {
         observable.deleteObserver( observer );
     }
     
     public void update(Observable o, Object arg) {        
        //Added for COEUSQA-2633 Ability to indicate which procedures IACUC study personnel will perform and training -Start
        HashMap hmNofiyInfo = (HashMap)arg;
        if(hmNofiyInfo.get("speciesInfo")!=null){// co
        vecStudyGroupData = (Vector)hmNofiyInfo.get("speciesInfo");
        //vecStudyGroupData =  (Vector)arg;
        //Added for COEUSQA-2633 Ability to indicate which procedures IACUC study personnel will perform and training -End
        }
    }
    //Added with CoeusQA-2551:Rework how user enters species,study groups and procedures in IACUC protocols
    private ProtocolSpeciesBean checkAlreadyDeleted(int currentSpeciesId){
        ProtocolSpeciesBean bean = null;
        if(vecDeletedSpecies!=null){
        for(int i=0;i<vecDeletedSpecies.size();i++){
            ProtocolSpeciesBean delBean = (ProtocolSpeciesBean)vecDeletedSpecies.get(i);
            if(delBean.getSpeciesId() == currentSpeciesId){
               bean = delBean;
               vecDeletedSpecies.remove(i);
            }
        }
        }
        return bean;
    }
     
    //Added for-COEUSQA-2508 Some schools do not use pain category-Start
    /*
     * Method isPainCategoryMandatory to get is pain category is mandatory or not
     * @return boolean
     */
     private boolean isPainCategoryMandatory() {                     
                String connectTo = CoeusGuiConstants.CONNECTION_URL+ PROTOCOL_SERVLET;
                boolean isAuthorized = false;
                char IS_PAIN_CATEGORY_MANDATORY = 'Y' ;
                RequesterBean request = new RequesterBean();
                request.setFunctionType(IS_PAIN_CATEGORY_MANDATORY);                 
                AppletServletCommunicator comm
                = new AppletServletCommunicator(connectTo, request);
                comm.send();
                ResponderBean response = comm.getResponse();
                if (response == null) {
                    response = new ResponderBean();
                    response.setResponseStatus(false);
                    response.setMessage(coeusMessageResources.parseMessageKey(
                    "server_exceptionCode.1000"));
                }
                
                if (response.isSuccessfulResponse()) {                   
                  String dataObject = response.getId(); 
                  if(PAIN_CATEGORY_CODE.equals(dataObject)){
                  isAuthorized =true;
                  }
                }
             return isAuthorized;
        }// end if functionType  
     //Added for-COEUSQA-2508 Some schools do not use pain category-End
     
     //COEUSQA-2724-Exceptions should be moved from Scientific Justification in IACUC - start
     /**
     * This method is used to set the Exception value after selecting the species table row
     * @param listSelectionEvent
     */
     public void valueChanged( ListSelectionEvent listSelectionEvent ) {
        int rowIndex = tblProtocolSpecies.getSelectedRow();
        if(listSelectionEvent.getSource().equals(tblProtocolSpecies.getSelectionModel())) {
            if(rowIndex >= 0){
                ProtocolSpeciesBean speciesBean = (ProtocolSpeciesBean)vecProtocolSpecies.get(rowIndex);             
                 //vecExceptionsData = speciesBean.getExceptionData();
                setSpeciesExceptions(speciesBean.getSpeciesExceptions());
                setExceptionTableData(speciesBean.getSpeciesExceptions());
                setExceptionTableColumnProperties();            
                if(functionType != CoeusGuiConstants.DISPLAY_MODE && functionType != CoeusGuiConstants.AMEND_MODE){                
                    if( tblExceptions.getRowCount() > 0){
                        setExceptionButtonsEnabled(true);
                        tblExceptions.setRowSelectionInterval(ZERO_ROW_INDEX, ZERO_ROW_INDEX);
                    } else{
                        if(speciesBean.isExceptionsPresent()) {
                            btnAddException.setEnabled(true);
                            btnDeleteException.setEnabled(false);
                            btnViewException.setEnabled(false);
                            btnModifyException.setEnabled(false);
                        } else {
                            setExceptionButtonsEnabled(false);
                        }
                    }
                }
                if(functionType == TypeConstants.DISPLAY_MODE || functionType == TypeConstants.AMEND_MODE) {
                    if( tblExceptions.getRowCount() > 0){
                        btnViewException.setEnabled(true);
                        tblExceptions.setRowSelectionInterval(ZERO_ROW_INDEX, ZERO_ROW_INDEX);
                    } else{
                        btnViewException.setEnabled(false);
                    }
                }
            }
        }
     }
     
    /*
    *This method is used to set the Exception Category Data into table.
    * @ return CoeusVector
    */
    public void setExceptionTableData(Vector speciesExceptions){
        exceptionsTableModel = new ExceptionsTableModel();
        tblExceptions.setModel(exceptionsTableModel);
        if(vecDeletedExceptions == null) {
             vecDeletedExceptions = new Vector();
        }
        if ((speciesExceptions != null) && ((speciesExceptions.size())>0)){
            exceptionsTableModel.setData(speciesExceptions);
        }
    }
      
    //COEUSQA-2724-Exceptions should be moved from Scientific Justification in IACUC
     /*
     * Method to get the max exception id
     * @return maxExceptionsId - int
     */
    private int getMaxExceptionsId(){
        int maxExceptionsId = 0;
        int selectedRow = tblProtocolSpecies.getSelectedRow();
        ProtocolSpeciesBean speciesBean = (ProtocolSpeciesBean)vecProtocolSpecies.get(selectedRow);
        Vector vecExceptions = getAllSepciesExceptions(speciesBean.getSpeciesId());
        if(vecExceptions != null && vecExceptions.size() > 0){
            for(Object obj: vecExceptions){
                ProtocolExceptionBean exceptionsBean = (ProtocolExceptionBean)obj;
                if(exceptionsBean.getExceptionId() > maxExceptionsId){
                    maxExceptionsId = exceptionsBean.getExceptionId();
                }
            }
        }
        return maxExceptionsId;
    }
    
    //Added for-COEUSQA-2711-Increase IACUC Scientific Justification to 1000 characters-Start
    /*New method addNewExceptionBeanToDB added to add new Exception
     *@param ProtocolExceptionBean
     *
     */
     private void addNewExceptionBeanToDB(ProtocolExceptionBean newExceptionBean){                     
       if((newExceptionBean.getExceptionDescription() != null)&&
            (newExceptionBean.getExceptionDescription().trim().length() > 0)&&
                (scientificJustificationExceptionForm.isSaveRequired())){                
              
                if( vecExceptionsData == null ) {
                    vecExceptionsData = new Vector();
                }
                //Vector containing the ProtocolExceptionBeans that needed to be added to the Database
                if(TypeConstants.INSERT_RECORD.equals(newExceptionBean.getAcType()) && !isUpdateBeforeSave){
                    setSaveRequired(true);
                    vecExceptionsData.addElement(newExceptionBean);                   
                }
                vecSpeciesExceptions = getSepciesExceptions(newExceptionBean.getSpeciesId());
                if(vecSpeciesExceptions != null) {
                    exceptionsTableModel.setData(vecSpeciesExceptions);
                }
                 int lastAddedRow = tblExceptions.getRowCount()-1;
                 if(TypeConstants.INSERT_RECORD.equals(newExceptionBean.getAcType())){
                    tblExceptions.setRowSelectionInterval(lastAddedRow,lastAddedRow);
                 }else{
                    tblExceptions.setRowSelectionInterval(selectedExceptionRow,selectedExceptionRow);
                 }
                isUpdateBeforeSave = false;
       }
    }     

      /*
     *This method is used to View the Protocol Exception.
     *
     */
     private void viewProtocolException() {
        selectedExceptionRow = tblExceptions.getSelectedRow();
        ProtocolExceptionBean newExceptionBean = (ProtocolExceptionBean)vecSpeciesExceptions.get(selectedExceptionRow);                
        scientificJustificationExceptionForm = new ScientificJustificationExceptionForm(mdiReference,
                            newExceptionBean,cvExceptionsCategory,'D');
        scientificJustificationExceptionForm.showProtocolExceptionForm();
        newExceptionBean = scientificJustificationExceptionForm.getProtocolExceptionBean();
    }
     
    /*
    *This method is used to set sepciesExceptions vector.
    * @ param Vector speciesExceptions
    */
    private void setSpeciesExceptions(Vector speciesExceptions) {
        vecSpeciesExceptions = speciesExceptions;
    }
    
    /*
    *This method is used to get sepciesExceptions for a specific speciesId.
    * @ param int speciesId
    * @ return Vector speciesExceptions
    */
    private Vector getSepciesExceptions(int speciesId) {
        Vector speciesExceptions = new Vector();
        ProtocolExceptionBean exceptionBean = null;
        if(vecExceptionsData != null && vecExceptionsData.size() > 0) {
            for(Object exception : vecExceptionsData) {
                if(exception instanceof ProtocolExceptionBean) {
                    exceptionBean = (ProtocolExceptionBean)exception;
                    if(exceptionBean != null && isNonDeletedException(exceptionBean)) {                            
                        if(exceptionBean.getSpeciesId() == speciesId) {
                            speciesExceptions.add(exceptionBean);
                        }
                    }
                }
            }
        }            
        return speciesExceptions;
        
    }
    
     /*
    *This method is used to get all exceptions (deleted and non-deleted) for a specific speciesId.
    * @ param int speciesId
    * @ return Vector speciesExceptions
    */
    private Vector getAllSepciesExceptions(int speciesId) {
        Vector speciesExceptions = new Vector();
        ProtocolExceptionBean exceptionBean = null;
        if(vecExceptionsData != null && vecExceptionsData.size() > 0) {
            for(Object exception : vecExceptionsData) {
                if(exception instanceof ProtocolExceptionBean) {
                    exceptionBean = (ProtocolExceptionBean)exception;                    
                    if(exceptionBean.getSpeciesId() == speciesId) {
                        speciesExceptions.add(exceptionBean);
                    }
                }
            }
        }            
        return speciesExceptions;
        
    }

    /*
    *This method is used to get all exceptions (deleted & non-deleted) for each and every species
    * @ param Vector vecAllSpecies
    * @ return Void
    */
    private void addDeletedExceptionsToBean(Vector vecAllSpecies) {
         ProtocolSpeciesBean speciesBean = null;
         Vector vecAllExceptions =  null;
         for(Object species : vecAllSpecies) {
             if(species instanceof ProtocolSpeciesBean) {
                 speciesBean = (ProtocolSpeciesBean)species;
                 if(speciesBean != null) {
                    vecAllExceptions = speciesBean.getSpeciesExceptions();
                    addDeletedSpeciesExceptions(speciesBean.getSpeciesId(), vecAllExceptions);
                    speciesBean.setSpeciesExceptions(vecAllExceptions);
                 }
             }
         }   
    }
    
    /*
    *This method is used to get all exceptions (deleted & non-deleted) for a specific species id
    * @ param int speciesId
    * @ return Vector deletedExceptions
    */
    private void addDeletedSpeciesExceptions(int speciesId, Vector vecAllExceptions) {
       ProtocolExceptionBean exceptionBean = null;
       if(vecDeletedExceptions != null && vecDeletedExceptions.size() > 0) {
           for(Object exception : vecDeletedExceptions) {
               if(exception instanceof ProtocolExceptionBean) {
                   exceptionBean = (ProtocolExceptionBean)exception;
                   if(exceptionBean != null && exceptionBean.getSpeciesId() == speciesId) {
                       vecAllExceptions.add(exceptionBean);
                   }
               }
           }
       }                 
    }
    
    /*
    * This method is used to get all exceptions associated with all species, irrespective of species id
    * @ param Vector vecSpecies
    * @ return Vector vecExceptions    
    */
    private Vector getAllExceptions(Vector vecSpecies) {
        Vector vecExceptions = new Vector();
        ProtocolSpeciesBean speciesBean = null;
         for(Object species : vecSpecies) {
             if(species instanceof ProtocolSpeciesBean) {
                 speciesBean = (ProtocolSpeciesBean)species;
                 if(speciesBean != null) {
                    vecExceptions.addAll(speciesBean.getSpeciesExceptions());
                 }
             }
         }     
        return vecExceptions;
    }

    /*
    *This method is used to delete assosiated Exceptions, set action type to 'DELETE' and add to 'vecDeletedExceptions'
    * @ param Vector speciesExceptions
    * @ return void     
    */
    private void deleteSpeciesExceptions(Vector speciesExceptions) {
         ProtocolExceptionBean exceptionBean = null;
          if(speciesExceptions != null && speciesExceptions.size() > 0) {
            for(Object exception : speciesExceptions) {
                if(exception instanceof ProtocolExceptionBean) {
                    exceptionBean = (ProtocolExceptionBean)exception;
                    exceptionBean.setAcType(TypeConstants.DELETE_RECORD);                    
                }
            }
          }
    }
    
    /*
    *This method is used to set exception buttons enabled/disabled
    * @ param boolean enabled
    * @ return void     
    */
    private void setExceptionButtonsEnabled(boolean enable) {
        btnAddException.setEnabled(enable);
        btnDeleteException.setEnabled(enable);
        btnViewException.setEnabled(enable);
        btnModifyException.setEnabled(enable);
    }
    
    /*
    *This method is used to check whether an exception is already deleted or not
    * @ param ProtocolExceptionBean exceptionBean
    * @ return boolean isNonDeleted   
    */
    private boolean isNonDeletedException(ProtocolExceptionBean exceptionBean) {
        boolean isNonDeleted = false;
        if(exceptionBean.getAcType() == null || 
                exceptionBean.getAcType().equalsIgnoreCase(TypeConstants.INSERT_RECORD) ||
                exceptionBean.getAcType().equalsIgnoreCase(TypeConstants.UPDATE_RECORD)) {
            isNonDeleted = true;
        }
        return isNonDeleted;
                
    }   
    
     private Vector setAcTypesInAmendment(Vector vecAllSpecies){
        Vector vecToSend = new Vector();
        Vector vecSpeciesExceptions = null;
        Vector vecNonDeletedException = null;
        ProtocolSpeciesBean speciesBean = null;
        ProtocolExceptionBean exceptionBean = null;
        int size = 0;
        int exceptionSize = 0;
        if(vecAllSpecies != null){            
            size = vecAllSpecies.size();            
            for(int index = 0; index < size; index++){                
                speciesBean = (ProtocolSpeciesBean)vecAllSpecies.get(index);
                if( functionType == CoeusGuiConstants.AMEND_MODE ||
                        functionType == CoeusGuiConstants.ADD_MODE || functionType == 'P') {
                    if(speciesBean.getAcType() == null ||  !speciesBean.getAcType().equalsIgnoreCase(TypeConstants.DELETE_RECORD)) {
                        speciesBean.setAcType(TypeConstants.INSERT_RECORD );
                        //Set action type of ExceptionBean for AMEND_MODE/ADD_MODE
                        vecNonDeletedException = new Vector();
                        vecSpeciesExceptions = speciesBean.getSpeciesExceptions();
                        if(vecSpeciesExceptions != null) {
                            exceptionSize = vecSpeciesExceptions.size();
                            for(int expIndex = 0; expIndex < exceptionSize; expIndex++) {
                                exceptionBean = (ProtocolExceptionBean)vecSpeciesExceptions.get(expIndex);
                                if(exceptionBean.getAcType() == null ||  !exceptionBean.getAcType().equalsIgnoreCase(TypeConstants.DELETE_RECORD)) {
                                    exceptionBean.setAcType(TypeConstants.INSERT_RECORD );
                                    vecNonDeletedException.addElement(exceptionBean);
                                }
                            }
                            speciesBean.setSpeciesExceptions(vecNonDeletedException);
                        }
                        vecToSend.addElement(speciesBean);
                    }
                }else{
                    vecToSend.addElement(speciesBean);
                }
            }
            return vecToSend;
        }else{
            return vecAllSpecies;
        }
    }
   //COEUSQA-2724-Exceptions should be moved from Scientific Justification in IACUC - end  
    
     
//Added for COEUSQA-2711 Increase IACUC Exception description to 1000 characters-Start
   /*** Table Render class for Exception table*/
   public class TextAreaRenderer extends JTextArea implements TableCellRenderer
   {

  public TextAreaRenderer() {
    setLineWrap(true);
    setWrapStyleWord(true);
    setFont(CoeusFontFactory.getNormalFont());
  }

  public Component getTableCellRendererComponent(JTable JTable,
      Object obj, boolean isSelected, boolean hasFocus, int row,
      int column) {   
    String description = (String)obj;    
    if(description.length() > 300){        
        description = description.replaceAll("\n","");
        description = description.replaceAll("\r","");
        description = description.trim();
        description = description.substring(0,297);
        description = description+"...";        
    }
    setText(description.trim());
    if(functionType == TypeConstants.DISPLAY_MODE || functionType == TypeConstants.AMEND_MODE){
            java.awt.Color bgListColor = (java.awt.Color)javax.swing.UIManager.getDefaults().get("Panel.background");
    setBackground(bgListColor);
    }
    setSize(tblExceptions.getColumnModel().getColumn(column).getWidth(),getPreferredSize().height);
    int hieght = (int)getPreferredSize().getHeight();
    if(hieght > JTable.getRowHeight(row)){         
        JTable.setRowHeight(row,hieght);      
    }
    return this;
  }
}
   //Added for COEUSQA-2711 Increase IACUC Exception description to 1000 characters-End
   
    //Added for COEUSQA-2633 Ability to indicate which procedures IACUC study personnel will perform and training qualifications for species-Start                            
    /*
    *This method is used to notify the observer
    * @param Vector vecProtocolSpecies
    *
    */
   private void notifyObserver(Vector vecProtocolSpecies){
       HashMap hmNotifyInfo = new HashMap();
       hmNotifyInfo.put("speciesInfo",vecProtocolSpecies);
       observable.notifyObservers(hmNotifyInfo);
   }
  //Added for COEUSQA-2633 Ability to indicate which procedures IACUC study personnel will perform and training qualifications for species-End
   
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdd;
    private javax.swing.JButton btnAddException;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnDeleteException;
    private javax.swing.JButton btnModifyException;
    private javax.swing.JButton btnViewException;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel pnlButtons;
    private javax.swing.JPanel pnlExceptionButtons;
    private javax.swing.JScrollPane scrPnExceptions;
    private javax.swing.JScrollPane scrPnProtocolNotesViewer;
    private javax.swing.JTable tblExceptions;
    private javax.swing.JTable tblProtocolSpecies;
    // End of variables declaration//GEN-END:variables
    
}
