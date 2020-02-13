
/*
 * @(#)InvestigatorController.java 1.0 3/30/04
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

/* PMD check performed, and commented unused imports and
 * variables on 13-NOV-2007 by Leena
 */

package edu.mit.coeus.utils.investigator;

import edu.mit.coeus.bean.InvestigatorBean;
import edu.mit.coeus.bean.InvestigatorUnitBean;
import edu.mit.coeus.brokers.*;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.exception.CoeusUIException;
import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.gui.event.Controller;
import edu.mit.coeus.propdev.bean.InvCreditTypeBean;
import edu.mit.coeus.propdev.bean.InvestigatorCreditSplitBean;
import edu.mit.coeus.utils.investigator.invCreditSplit.InvestigatorCreditSplitController;
import edu.mit.coeus.utils.query.Equals;
import edu.mit.coeus.utils.query.NotEquals;
import edu.mit.coeus.utils.query.And;
import edu.mit.coeus.utils.query.Or;
//import edu.mit.coeus.utils.query.QueryEngine;
import edu.mit.coeus.search.gui.CoeusSearch;
import edu.mit.coeus.utils.*;
import edu.mit.coeus.unit.bean.*;
import edu.mit.coeus.rolodexmaint.gui.RolodexMaintenanceDetailForm;
import edu.mit.coeus.departmental.gui.PersonDetailForm;
import edu.mit.coeus.instprop.bean.InvestigatorUnitAdminTypeBean;
//import edu.mit.coeus.irb.bean.PersonInfoFormBean;
import edu.mit.coeus.propdev.bean.ProposalInvestigatorFormBean;
import edu.mit.coeus.propdev.bean.ProposalLeadUnitFormBean;
import edu.mit.coeus.unit.gui.UnitDetailForm;
import edu.mit.coeus.utils.investigator.invCreditSplit.InvCreditSplitObject;
import edu.mit.coeus.utils.query.QueryEngine;
import edu.vanderbilt.coeus.gui.PersonTableCellRenderer;
import edu.vanderbilt.coeus.utils.CustomFunctions;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.*;
import java.text.MessageFormat;

import javax.swing.*;
import javax.swing.event.ListSelectionListener;
//import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;

import java.util.Vector;
import java.util.HashMap;
import java.util.Hashtable;

import javax.swing.table.JTableHeader;
//import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

/**
 *
 * @author  jobinelias
 */
public class InvestigatorController  extends Controller
        implements ActionListener, ListSelectionListener,TypeConstants {
    // Creating the instances of model,editor and renderer classes
    
    //providing package access so that can be invoked from controller reference in
    // investigator table model to reset/update lead unit flag for non-pi rows.
    InvestigatorUnitTableModel unitTableModel;
    UnitCellEditor unitCellEditor;
    private UnitTableCellRenderer unitTableCellRenderer;
    InvestigatorCellEditor investigatorCellEditor;
    private InvestigatorTableModel investigatorTableModel;
    private InvestigatorTableCellRenderer investigatorTableCellRenderer;
    private InvestigatorForm investigatorForm;
    private static final String EMPTY_STRING = "";
    
    //For getting the data from the bean
    private CoeusVector cvInvestigator;
    private CoeusVector cvUnit;
    
    // Represents the bean class
    
    private InvestigatorBean investigatorBean;
    
    
    private CoeusMessageResources coeusMessageResources;
    
    // Represents the column header names for the investigator table
    private static final int INVESTIGATOR_HAND_COLUMN = 0;
    private static final int INVESTIGATOR_NAME_COLUMN = 1;
    //Commented for Coeus 4.3 -PT ID:2229 Multi PI - start
//    private static final int INVESTIGATOR_EFFORT_COLUMN = 2;
//    private static final int INVESTIGATOR_PI_COLUMN=3;
//    private static final int INVESTIGATOR_FACULTY_COLUMN=4;
    //Commented for Coeus 4.3 -PT ID:2229 Multi PI - end
    
    //Added for Coeus 4.3 -PT ID:2229 Multi PI - start
    private static final int INVESTIGATOR_PI_COLUMN=2;
    private static final int INVESTIGATOR_MULTI_PI_COLUMN = 3;
    private static final int INVESTIGATOR_FACULTY_COLUMN=4;
    private static final int INVESTIGATOR_EFFORT_COLUMN =5;
    //Added for Coeus 4.3 -PT ID:2229 Multi PI - end
    
    //Added for Coeus 4.3-PT ID:2270 Tracking Effort- start
    private static final int INVESTIGATOR_ACAD_YEAR_COLUMN=6;
    private static final int INVESTIGATOR_SUM_EFFORT_COLUMN=7;
    private static final int INVESTIGATOR_CAL_YEAR_COLUMN =8;
    //Added for Coeus 4.3-PT ID:2270 Tracking Effort- end

    // Represents the column header names for the unit table
    private static final int UNIT_HAND_COLUMN = 0;
    private static final int UNIT_LEAD_FLAG_COLUMN = 1;
    private static final int UNIT_NUMBER_COLUMN = 2;
    private static final int UNIT_NAME_COLUMN = 3;
    private static final int UNIT_OSP_ADMIN_COLUMN = 4;
    private Vector deletedInvestigators = new Vector();
    private HashMap hmDeletedUnits = new HashMap();
    //To check whether the data or screen is modified
    private boolean dataChanged;
    private int prevInvRow,prevUnitRow;
//    private CoeusUtils coeusUtils = CoeusUtils.getInstance();
    
    //Case 2106 Start 1
    private String moduleNumber;
    private String moduleName;
    private String seqNo;
    //Case 2106 End 1
    //Added for Case#2136 Enhancement start 0
    private String leadUnitNo;
    //Added for Case#2136 Enhancement start 0
    
    private static final char GET_PROP_UNIT_ADMIN_TYPE_DATA = 'Q';
    private static final String INST_PROP_SERVLET = "/InstituteProposalMaintenanceServlet";
    
    private static final char GET_AWARD_UNIT_ADMIN_TYPE_DATA = 'y';
    private static final String AWARD_SERVLET = "/AwardMaintenanceServlet";
    
     //Added for Case#3587 - Multi Campus enchanment - Start
    private static final char USER_HAS_RIGHT_IN_UNIT_LEVEL = 'R';
    private static final String LEAD_UNIT_NUMBER = "LEAD_UNIT_NUMBER";
    private static final String RIGHT_ID = "RIGHT_ID";
    private static final String CREATE_INST_PROPOSAL = "CREATE_INST_PROPOSAL";
    
    private static final char CHECK_USER_HAS_CREATE_AWARD_RIGHT = '0';    
    //Case#3587 - End
    private CoeusVector cvAdministratorDetails;
    //Added for COEUSQA-2037 : Software allows you to delete an investigator who is assigned credit in the credit split window - Start
    private static final String AWARD_MODULE_CODE = "1";
    private static final String INSTITUTE_PROPOSAL_MODULE_CODE = "2";
    private static final int MODULE_CODE_INDEX = 0;
    private static final int MODULE_ITEM_KEY_INDEX = 1;
    private static final int MODULE_ITEM_KEY_SEQUENCE_INDEX = 2;
    private static final int PERSON_ID_INDEX = 3;
    private static final int ADDS_TO_HUNDRED_INDEX = 4;
    private static final int UNIT_NUMBER_INDEX = 5;
    private static final char CHECK_CREDIT_SPLIT_FOR_INVESTIGATOR = 'L';
    private static final char CHECK_CREDIT_SPLIT_FOR_INV_UNIT = 'U';
    private static final String SAVE_BEFORE_OPEN_CREDIT_SPILT = "module_investigator_exceptionCode.1000";
    private static final String CREDIT_SPLIT_EXISTS_FOR_INV = "proposal_investigatorCreditSplit_exceptionCode.10001";
    private static final String CREDIT_SPLIT_EXISTS_FOR_INV_UNIT = "proposal_investigatorUnitCreditSplit_exceptionCode.10002";
    private String moduleText = CoeusGuiConstants.EMPTY_STRING;
    
    /** Creates a new instance of InvestigatorController
     */
    public InvestigatorController() {
        cvInvestigator = new CoeusVector();
        cvUnit = new CoeusVector();
        coeusMessageResources = CoeusMessageResources.getInstance();
        registerComponents();
        
    }
     
    public InvestigatorController(String moduleNo, String moduleName) {
        this();
        this.moduleNumber = moduleNo;
        this.moduleName = moduleName;
        //Added for COEUSQA-2037 : Software allows you to delete an investigator who is assigned credit in the credit split window - Start
        if(moduleName.equals(CoeusGuiConstants.AWARD_MODULE)){
            this.moduleText = "award";
        }else if(moduleName.equals(CoeusGuiConstants.INSTITUTE_PROPOSAL_MODULE)){
            this.moduleText = "proposal";
        }
        //COEUSQA-2037 : End
    }
    /**
     * TO display the GUI
     * @return void
     */
    public void display() {
        fetchAdministratorData();
        if (prevInvRow != -1 && investigatorForm.tblInvestigator.getRowCount() > prevInvRow ) {
            investigatorForm.tblInvestigator.setRowSelectionInterval(prevInvRow,prevInvRow);
        }else if (investigatorForm.tblInvestigator.getRowCount() > 0) {
            investigatorForm.tblInvestigator.setRowSelectionInterval(0,0);
        }
        if (prevUnitRow != -1 && investigatorForm.tblUnits.getRowCount() > prevUnitRow ) {
            investigatorForm.tblUnits.setRowSelectionInterval(prevUnitRow,prevUnitRow);
        }else if (investigatorForm.tblUnits.getRowCount() > 0) {
            investigatorForm.tblUnits.setRowSelectionInterval(0,0);
        }
    }
    /**
     * To format the fields
     * @return void
     */
    public void formatFields(){
        boolean enabled = getFunctionType() != DISPLAY_MODE ? true : false ;
        investigatorForm.btnRolodex.setEnabled( enabled );
        investigatorForm.btnDelete.setEnabled( enabled );
        investigatorForm.btnAdd.setEnabled( enabled );
        investigatorForm.btnFindPerson.setEnabled( enabled );
        investigatorForm.btnFindUnit.setEnabled( enabled );
        investigatorForm.btnAddUnit.setEnabled( enabled );
        investigatorForm.btnDelUnit.setEnabled( enabled );
        //Case 2796: Sync to Parent
        boolean syncEnabled = enabled && isSyncEnabled();
        investigatorForm.btnSyncInv.setEnabled(syncEnabled);
        investigatorForm.btnDelSyncInv.setEnabled(syncEnabled);
        //2796 End
        investigatorTableModel.setEditable(enabled );
        unitTableModel.setEditable( enabled );
        if( investigatorForm.tblInvestigator.getRowCount() <= 0 ){
            investigatorForm.btnDelete.setEnabled( false );
            investigatorForm.btnAddUnit.setEnabled( false );
            investigatorForm.btnDelUnit.setEnabled( false );
            investigatorForm.btnFindUnit.setEnabled( false );
            //Case 2796: Sync to Parent
            investigatorForm.btnDelSyncInv.setEnabled(isSyncEnabled());
            //2796 End
        }
        investigatorTableCellRenderer.setFunctionType(getFunctionType());
        unitTableCellRenderer.setFunctionType(getFunctionType());
        if(getFunctionType() == CoeusGuiConstants.DISPLAY_MODE){
            
            java.awt.Color bgListColor = (java.awt.Color)javax.swing.UIManager.getDefaults().get("Panel.background");
            
            investigatorForm.tblUnits.setBackground(bgListColor);
            investigatorForm.tblUnits.setSelectionBackground(bgListColor );
            investigatorForm.tblUnits.setSelectionForeground(java.awt.Color.BLACK);
            
            investigatorForm.tblInvestigator.setBackground(bgListColor);
            investigatorForm.tblInvestigator.setSelectionBackground(bgListColor );
            investigatorForm.tblInvestigator.setSelectionForeground(java.awt.Color.BLACK);
        } else{
            investigatorForm.tblUnits.setBackground(java.awt.Color.white);
            investigatorForm.tblUnits.setSelectionBackground(java.awt.Color.white);
            investigatorForm.tblUnits.setSelectionForeground(java.awt.Color.black);
            
            investigatorForm.tblInvestigator.setBackground(java.awt.Color.white);
            investigatorForm.tblInvestigator.setSelectionBackground(java.awt.Color.white);
            investigatorForm.tblInvestigator.setSelectionForeground(java.awt.Color.black);
        }
        //Case 2796: Sync To Parent
        if(CoeusGuiConstants.AWARD_MODULE.equals(moduleName)){
            investigatorForm.btnSyncInv.setVisible(true);
            investigatorForm.btnDelSyncInv.setVisible(true);
        }else{
            investigatorForm.btnSyncInv.setVisible(false);
            investigatorForm.btnDelSyncInv.setVisible(false);
        }
        //Case 2796: End
        
        /* JM 9-4-2015 added to set font color based on status; 2-2-2016 added highlighting for external persons;
         * 	2-2-2016 added new method to allow multiple options */
    	//edu.vanderbilt.coeus.gui.CustomTableCellRenderer renderer = 
    	//		new edu.vanderbilt.coeus.gui.CustomTableCellRenderer(InvestigatorTableModel.INVESTIGATOR_STATUS_COLUMN,"I",Color.RED, true, false, false, getFunctionType());
    	PersonTableCellRenderer renderer = 
    			new PersonTableCellRenderer(InvestigatorTableModel.INVESTIGATOR_STATUS_COLUMN,InvestigatorTableModel.INVESTIGATOR_IS_EXTERNAL_PERSON_COLUMN,true,false,false,getFunctionType());
    	investigatorForm.tblInvestigator.getColumnModel().getColumn(INVESTIGATOR_NAME_COLUMN).setCellRenderer(renderer);
    	/* JM END */   
    }
    
    /**
     * To get the UI
     * @return Component
     */
    public Component getControlledUI() {
        return investigatorForm;
    }
    /**
     * To get the form data
     * @return Object
     */
    public Object getFormData() {
        CoeusVector invData = investigatorTableModel.getData();
        CoeusVector dataToSend = new CoeusVector();
        if( deletedInvestigators.size() > 0 ) {
            int delCount = deletedInvestigators.size();
            for( int delIndx = 0; delIndx < delCount; delIndx++ ) {
                InvestigatorBean delInvBean = (InvestigatorBean) deletedInvestigators.get(delIndx);
                CoeusVector cvDelUnits = delInvBean.getInvestigatorUnits();
                if( cvDelUnits != null ) {
                    int delUnitCount = cvDelUnits.size();
                    for( int delUnitIndx = 0; delUnitIndx < delUnitCount; delUnitIndx++){
                        InvestigatorUnitBean delUnitBean =
                                (InvestigatorUnitBean)cvDelUnits.get(delUnitIndx);
                        delUnitBean.setAcType(DELETE_RECORD);
                        cvDelUnits.set(delUnitIndx, delUnitBean);
                    }
                }
                delInvBean.setInvestigatorUnits(cvDelUnits);
                dataToSend.add(delInvBean);
            }
        }
        if( invData != null ) {
            int invSize = invData.size();
            for( int invIndx = 0; invIndx < invSize; invIndx++) {
                InvestigatorBean invBean = (InvestigatorBean)invData.get(invIndx);
                if( invBean.getPersonId() != null && invBean.getPersonId().trim().length() > 0 ) {
                    CoeusVector cvInvUnits = new CoeusVector();
                    if ( hmDeletedUnits.containsKey(invBean.getPersonId()) ) {
                        cvInvUnits.addAll( (CoeusVector)hmDeletedUnits.get(invBean.getPersonId()));
                    }
                    //Added for COEUSQA-2383 - Two Lead Unit appear for an Award start
                    //It is required to remove all deleted entry from database
                    if( deletedInvestigators.size() > 0 ) {
                        for( int delIndx = 0; delIndx < deletedInvestigators.size(); delIndx++ ) {
                             InvestigatorBean delInvBean = (InvestigatorBean) deletedInvestigators.get(delIndx);
                             if ( hmDeletedUnits.containsKey(delInvBean.getPersonId()) ) {
                                cvInvUnits.addAll( (CoeusVector)hmDeletedUnits.get(delInvBean.getPersonId()));
                             }
                        }
                    }
                    //Added for COEUSQA-2383 Two Lead Unit appear for an Award end
                    CoeusVector units = invBean.getInvestigatorUnits();
                    if( units != null ) {
                        CoeusVector notNullUnits = units.filter(new NotEquals("unitNumber",null));
                        cvInvUnits.addAll(notNullUnits);
                    }
                    invBean.setInvestigatorUnits(cvInvUnits);
                    dataToSend.add(invBean);
                }
            }
        }
        if( dataToSend.size() > 0 ){
            return dataToSend;
        }
        return null;
    }
    //Method param modified with case 2796: Sync TO Parent
    public boolean isInvestigatorPresent(boolean beforeDelete){
        int compCount = beforeDelete? 1:0;
        CoeusVector cvInv = investigatorTableModel.getData();
        if( cvInv == null || cvInv.size() <= compCount ) {
            return false;
        }
        return true;
    }
    /*
     * Checks whether there is any PI. This should be called only after
     * isInvestigatorPresent() validation is passed.
     */
    // Added For bug fix # 1617
    //Method param modified with case 2796: Sync TO Parent
    public boolean isPIPresent(boolean beforeDelete) {
        
        CoeusVector cvInv = null;
        try {
            cvInv = (CoeusVector) ObjectCloner.deepCopy(investigatorTableModel.getData());
        } catch (Exception ex) {
            return false;
        }
        if( cvInv == null || cvInv.size() == 0 ){
            return false;
        }else {
            int selRow = investigatorForm.tblInvestigator.getSelectedRow();
            if(beforeDelete && selRow != -1){
                cvInv.remove(selRow);
            }
            Equals piValue = new Equals("principalInvestigatorFlag",true);
            NotEquals personValue = new NotEquals("personId",null);
            And validPi = new And(piValue,personValue);
//            NotEquals notDeleted = new NotEquals("acType",DELETE_RECORD );
//            And validPiAndNotDeleted = new And(validPi, notDeleted);
            CoeusVector piData = cvInv.filter(validPi);
            if( piData == null || piData.size() == 0 ) {
                return false;
            }
        }
        return true;
    }
    /***
     * Registering components
     * @return void
     */
    public void registerComponents() {
        investigatorForm = new InvestigatorForm();
        investigatorForm.tblInvestigator.setBackground(Color.LIGHT_GRAY);
        
        if(getFunctionType()!=DISPLAY_MODE){
            investigatorForm.btnAdd.addActionListener(this);
            investigatorForm.btnAddUnit.addActionListener(this);
            investigatorForm.btnDelUnit.addActionListener(this);
            investigatorForm.btnDelete.addActionListener(this);
            investigatorForm.btnFindPerson.addActionListener(this);
            investigatorForm.btnFindUnit.addActionListener(this);
            investigatorForm.btnRolodex.addActionListener(this);
            investigatorForm.btnSyncInv.addActionListener(this);//2796
            investigatorForm.btnDelSyncInv.addActionListener(this);//2796
            Component[] comp = {investigatorForm.tblInvestigator,investigatorForm.tblUnits,
            investigatorForm.btnAdd,investigatorForm.btnDelete,investigatorForm.btnFindPerson,
            investigatorForm.btnRolodex,
            //Case 2106 Start 2
            investigatorForm.btnCreditSplit,
            //Case 2106 End 2
            //Case 2796:Sync to parent
            investigatorForm.btnSyncInv,investigatorForm.btnDelSyncInv,
            //2796 End
            //Added for Case#2136 Enhancement start 1
            investigatorForm.btnAdminType,
            //Added for Case#2136 Enhancement end 1
            investigatorForm.btnAddUnit,investigatorForm.btnDelUnit,
            investigatorForm.btnFindUnit,
            };
            ScreenFocusTraversalPolicy traversalPolicy = new ScreenFocusTraversalPolicy(comp);
            investigatorForm.setFocusTraversalPolicy(traversalPolicy);
            investigatorForm.setFocusCycleRoot(true);
        }
        
        investigatorForm.tblInvestigator.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        investigatorForm.tblInvestigator.setSurrendersFocusOnKeystroke(true);
        investigatorForm.tblUnits.setSurrendersFocusOnKeystroke(true);
        investigatorForm.tblUnits.addMouseListener(new UnitDisplayAdapter());
        investigatorForm.tblInvestigator.getSelectionModel().addListSelectionListener(this);
        investigatorForm.tblInvestigator.addMouseListener(new InvestigatorDetailsAdapter());
        //Added for Coeus 4.3-PT ID:2270 Tracking Effort- start
        investigatorForm.tblInvestigator.getTableHeader().setPreferredSize(new Dimension(0, 30));
        //Added for Coeus 4.3-PT ID:2270 Tracking Effort- end
//        investigatorForm.tblUnits.getSelectionModel().addListSelectionListener(this);
        unitTableModel = new InvestigatorUnitTableModel();
        investigatorTableModel = new InvestigatorTableModel();
        investigatorTableModel.setController(this);
        unitTableModel.setController(this);
        //Added for case id 2229 - start
        investigatorForm.tblUnits.getSelectionModel().addListSelectionListener(this);
        //Added for case id 2229 - end
        // Setting up the table model
        investigatorForm.tblInvestigator.setModel(investigatorTableModel);
        investigatorForm.tblUnits.setModel(unitTableModel);
        // creating the instances of editors and renderers.
        unitCellEditor = new UnitCellEditor();
        unitCellEditor.setUnitDetailsListener(new UnitDisplayAdapter());
        unitTableCellRenderer = new UnitTableCellRenderer();
        investigatorCellEditor = new InvestigatorCellEditor();
        investigatorCellEditor.setViewInvestigatorDetailsListener(new InvestigatorDetailsAdapter());
//        investigatorCellEditor.setController(this);
//        unitCellEditor.setController(this);
        investigatorTableCellRenderer = new InvestigatorTableCellRenderer();
        investigatorTableCellRenderer.setFunctionType(getFunctionType());
        unitTableCellRenderer.setFunctionType(getFunctionType());
        
    }
    
    /***
     * setting up the column data
     * @return void
     */
    private void setColumnData() {
        // Setting the table header, column width, renderer and editor for the
        // editing subcontract table
        JTableHeader tableHeader = investigatorForm.tblInvestigator.getTableHeader();
        tableHeader.setReorderingAllowed(false);
        tableHeader.setFont(CoeusFontFactory.getLabelFont());
        
        investigatorForm.tblInvestigator.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        investigatorForm.tblInvestigator.setRowHeight(22);
        investigatorForm.tblInvestigator.setShowHorizontalLines(false);
        investigatorForm.tblInvestigator.setShowVerticalLines(false);
        investigatorForm.tblInvestigator.setOpaque(false);
        investigatorForm.tblInvestigator.setSelectionMode(
                DefaultListSelectionModel.SINGLE_SELECTION);
        
        TableColumn column = investigatorForm.tblInvestigator.getColumnModel().getColumn(INVESTIGATOR_HAND_COLUMN);
        column.setMaxWidth(30);
        column.setMinWidth(30);
        column.setPreferredWidth(30);
        column.setResizable(true);
        column.setCellRenderer(new IconRenderer());
        column.setHeaderRenderer(new EmptyHeaderRenderer());
        
        column = investigatorForm.tblInvestigator.getColumnModel().getColumn(INVESTIGATOR_NAME_COLUMN);
        column.setMinWidth(358);
        column.setPreferredWidth(358);
        column.setResizable(true);
        column.setCellEditor(investigatorCellEditor);
        column.setCellRenderer(investigatorTableCellRenderer);
        
        column = investigatorForm.tblInvestigator.getColumnModel().getColumn(INVESTIGATOR_EFFORT_COLUMN);
        column.setMaxWidth(78);
        column.setMinWidth(78);
        column.setPreferredWidth(78);
        column.setResizable(true);
        column.setCellEditor(investigatorCellEditor);
        column.setCellRenderer(investigatorTableCellRenderer);
        
        column = investigatorForm.tblInvestigator.getColumnModel().getColumn(INVESTIGATOR_PI_COLUMN);
        column.setMaxWidth(30);
        column.setMinWidth(30);
        column.setPreferredWidth(30);
        column.setResizable(true);column.setCellEditor(investigatorCellEditor);
        column.setCellRenderer(investigatorTableCellRenderer);
        
        column = investigatorForm.tblInvestigator.getColumnModel().getColumn(INVESTIGATOR_FACULTY_COLUMN);
        column.setMaxWidth(60);
        column.setMinWidth(60);
        column.setPreferredWidth(60);
        column.setResizable(true);
        column.setCellEditor(investigatorCellEditor);
        column.setCellRenderer(investigatorTableCellRenderer);
        
        //Added for Coeus 4.3 -PT ID:2229 Multi PI - start
        column = investigatorForm.tblInvestigator.getColumnModel().getColumn(INVESTIGATOR_MULTI_PI_COLUMN);
        column.setMaxWidth(60);
        column.setMinWidth(60);
        column.setPreferredWidth(60);
        column.setResizable(true);column.setCellEditor(investigatorCellEditor);
        column.setCellRenderer(investigatorTableCellRenderer);
        //Added for Coeus 4.3 -PT ID:2229 Multi PI - end
        
        //Added for Coeus 4.3-PT ID:2270 Tracking Effort- start
        column = investigatorForm.tblInvestigator.getColumnModel().getColumn(INVESTIGATOR_ACAD_YEAR_COLUMN);
        column.setMaxWidth(81);
        column.setMinWidth(81);
        column.setPreferredWidth(81);
        column.setResizable(true);
        column.setCellEditor(investigatorCellEditor);
        column.setCellRenderer(investigatorTableCellRenderer);
        
        column = investigatorForm.tblInvestigator.getColumnModel().getColumn(INVESTIGATOR_SUM_EFFORT_COLUMN);
        column.setMaxWidth(81);
        column.setMinWidth(81);
        column.setPreferredWidth(81);
        column.setResizable(true);
        column.setCellEditor(investigatorCellEditor);
        column.setCellRenderer(investigatorTableCellRenderer);
        
        column = investigatorForm.tblInvestigator.getColumnModel().getColumn(INVESTIGATOR_CAL_YEAR_COLUMN);
        column.setMaxWidth(81);
        column.setMinWidth(81);
        column.setPreferredWidth(81);
        column.setResizable(true);
        column.setCellEditor(investigatorCellEditor);
        column.setCellRenderer(investigatorTableCellRenderer);
        //Added for Coeus 4.3-PT ID:2270 Tracking Effort- end
        
        /* JM 9-9-2015 hidden status column */
        column = investigatorForm.tblInvestigator.getColumnModel().getColumn(InvestigatorTableModel.INVESTIGATOR_STATUS_COLUMN);
        column.setMaxWidth(0);
        column.setMinWidth(0);
        column.setPreferredWidth(0);
        column.setResizable(false);
    	/* JM END */     
        
        /* JM 2-2-2016 hidden isExternalPerson column */
        column = investigatorForm.tblInvestigator.getColumnModel().getColumn(InvestigatorTableModel.INVESTIGATOR_IS_EXTERNAL_PERSON_COLUMN);
        column.setMaxWidth(0);
        column.setMinWidth(0);
        column.setPreferredWidth(0);
        column.setResizable(false);
    	/* JM END */  
        
        // Setting the table header, column width, renderer and editor for the
        // display unit details header
        JTableHeader unitHeader = investigatorForm.tblUnits.getTableHeader();
        //unitHeader.addMouseListener(new ColumnHeaderListener());
        unitHeader.setReorderingAllowed(false);
        unitHeader.setFont(CoeusFontFactory.getLabelFont());
        
        investigatorForm.tblUnits.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        investigatorForm.tblUnits.setRowHeight(22);
        
        investigatorForm.tblUnits.setShowHorizontalLines(false);
        investigatorForm.tblUnits.setShowVerticalLines(false);
        investigatorForm.tblUnits.setOpaque(false);
        investigatorForm.tblUnits.setSelectionMode(
                DefaultListSelectionModel.SINGLE_SELECTION);
        
        TableColumn unitColumn = investigatorForm.tblUnits.getColumnModel().getColumn(UNIT_HAND_COLUMN);
        unitColumn.setMaxWidth(30);
        unitColumn.setMinWidth(30);
        unitColumn.setPreferredWidth(30);
        unitColumn.setResizable(true);
        unitColumn.setCellRenderer(new IconRenderer());
        unitColumn.setHeaderRenderer(new EmptyHeaderRenderer());
        
        unitColumn = investigatorForm.tblUnits.getColumnModel().getColumn(UNIT_LEAD_FLAG_COLUMN);
        unitColumn.setMaxWidth(45);
        unitColumn.setMinWidth(45);
        unitColumn.setPreferredWidth(45);
        unitColumn.setResizable(true);
        unitColumn.setCellRenderer(unitTableCellRenderer);
        unitColumn.setCellEditor(unitCellEditor);
        
        unitColumn = investigatorForm.tblUnits.getColumnModel().getColumn(UNIT_NUMBER_COLUMN);
        unitColumn.setMaxWidth(150);
        unitColumn.setMinWidth(150);
        unitColumn.setPreferredWidth(150);
        unitColumn.setResizable(true);
        unitColumn.setCellRenderer(unitTableCellRenderer);
        unitColumn.setCellEditor(unitCellEditor);
        
        unitColumn = investigatorForm.tblUnits.getColumnModel().getColumn(UNIT_NAME_COLUMN);
        unitColumn.setMinWidth(410);
        unitColumn.setPreferredWidth(410);
        unitColumn.setResizable(true);
        unitColumn.setCellRenderer(unitTableCellRenderer);
        unitColumn.setCellEditor(unitCellEditor);
        
        unitColumn = investigatorForm.tblUnits.getColumnModel().getColumn(UNIT_OSP_ADMIN_COLUMN);
        unitColumn.setMinWidth(215);
        unitColumn.setPreferredWidth(215);
        unitColumn.setResizable(true);
        unitColumn.setCellRenderer(unitTableCellRenderer);
        unitColumn.setCellEditor(unitCellEditor);
        
    }
    /**
     * save form data
     * @return void
     */
    public void saveFormData() {
    }
    
    /**
     * setting up the form data
     * @return void
     */
    public void setFormData(Object investigatorData) {
        // Added for COEUSQA-3642 : Issues with COEUSQA-1676 Credit Split Infrastructure project - Start
        investigatorTableModel.setModuleName(getModuleName());
        investigatorTableModel.setModuleNumber(getModuleNumber());
        investigatorTableModel.setSeqNo(getSeqNo());
        unitTableModel.setModuleName(getModuleName());
        unitTableModel.setModuleNumber(getModuleNumber());
        unitTableModel.setSeqNo(getSeqNo());
        // Added for COEUSQA-3642 : Issues with COEUSQA-1676 Credit Split Infrastructure project - End
        prevInvRow = investigatorForm.tblInvestigator.getSelectedRow();
        prevUnitRow = investigatorForm.tblUnits.getSelectedRow();
        cvInvestigator = (CoeusVector)investigatorData;
        investigatorTableModel.setData(cvInvestigator);
        
        if(cvInvestigator != null && cvInvestigator.size() > 0){
            for (int index =0; index < cvInvestigator.size(); index ++){
                investigatorBean = (InvestigatorBean)cvInvestigator.get(index);
                cvUnit = investigatorBean.getInvestigatorUnits();
            }
            if(cvUnit != null && cvUnit.size() > 0){
                unitTableModel.setData(cvUnit);
                unitTableModel.fireTableDataChanged();
            }
        }
        setColumnData();
        formatFields();
        display();
        hmDeletedUnits.clear();
        deletedInvestigators.clear();
    }
    /**
     * Method used to handle the value change events for the table.
     * @param listSelectionEvent event which delegates selection changes for a
     * table.
     * @return void
     */
    public void valueChanged(ListSelectionEvent listSelectionEvent) {
        if( !listSelectionEvent.getValueIsAdjusting()){
            CoeusVector cvUnitsData = new CoeusVector();
            ListSelectionModel source = (ListSelectionModel)listSelectionEvent.getSource();
            int selectedRow=0;
            if (source.equals(investigatorForm.tblInvestigator.getSelectionModel())) {
                selectedRow = investigatorForm.tblInvestigator.getSelectedRow();
                if (selectedRow != -1) {
                    investigatorBean= (InvestigatorBean)cvInvestigator.get(selectedRow);
                    cvUnitsData = investigatorBean.getInvestigatorUnits();
                    unitCellEditor.cancelCellEditing();
                    unitTableModel.setCellEditable(investigatorBean.isPrincipalInvestigatorFlag());
                    unitTableModel.setData(cvUnitsData);
                    unitTableModel.fireTableDataChanged();
                    if( unitTableModel.getRowCount() > 0 ){
                        investigatorForm.tblUnits.setRowSelectionInterval(0,0);
                    }
                    investigatorCellEditor.cancelCellEditing();
                    int selColumn = investigatorForm.tblInvestigator.getSelectedColumn();
                    //Modified for case id 2270 - start
                    if( selColumn == INVESTIGATOR_NAME_COLUMN ||
                            selColumn == INVESTIGATOR_EFFORT_COLUMN ||
                            selColumn == INVESTIGATOR_ACAD_YEAR_COLUMN ||
                            selColumn == INVESTIGATOR_CAL_YEAR_COLUMN ||
                            selColumn == INVESTIGATOR_SUM_EFFORT_COLUMN ) {
                        //Added for Coeus 4.3-PT ID:2270 Tracking Effort- end
                        investigatorForm.tblInvestigator.editCellAt(selectedRow,selColumn);
                        java.awt.Component comp = investigatorForm.tblInvestigator.getEditorComponent();
                        if( comp != null ) {
                            comp.requestFocusInWindow();
                        }
                    }
                }
            }
            //Added for case id 2229 - start
            else if (source.equals(investigatorForm.tblUnits.getSelectionModel())) {
                selectedRow = investigatorForm.tblUnits.getSelectedRow();
                if (selectedRow != -1) {
                    unitCellEditor.cancelCellEditing();
                    int selColumn = investigatorForm.tblUnits.getSelectedColumn();
                    if( selColumn == UNIT_NUMBER_COLUMN ){
                        investigatorForm.tblUnits.editCellAt(selectedRow,selColumn);
                        java.awt.Component comp = investigatorForm.tblUnits.getEditorComponent();
                        if( comp != null ) {
                            comp.requestFocusInWindow();
                        }
                    }
                }
            }
            //Added for case id 2229 - end
        }
    }
    
    private void log(String mesg){
        CoeusOptionPane.showWarningDialog(mesg);
    }
    
    /***
     * validate method
     * @return boolean
     */
    public boolean validate() throws CoeusUIException {
        //moved the validate code to validate(boolean beforeDelete) method: Case 2796
        return validate(false);
    }
    
    /***
     * action performed
     * @param ActionEvent actionEvent
     * @return void
     */
    public void actionPerformed(java.awt.event.ActionEvent actionEvent) {
        Object source = actionEvent.getSource();
        investigatorCellEditor.stopCellEditing();
        unitCellEditor.stopCellEditing();
        if (source.equals(investigatorForm.btnAdd)){
            performAddAction();
        }else if(source.equals(investigatorForm.btnFindPerson)){
            // opening the find person screen
            performFindPerson();
        }else if(source.equals(investigatorForm.btnRolodex)){
            // Opening the rolodex screen
            performFindRolodex();
        }else if(source.equals(investigatorForm.btnAddUnit)){
            performAddUnit();
        }else if(source.equals(investigatorForm.btnDelUnit)){
            performDeleteUnit();
        }else if(source.equals(investigatorForm.btnFindUnit)){
            performFindUnit();
        }else if(source.equals(investigatorForm.btnDelete)){
            performDelete(false);
            //2796: Sync To Parent
        }else if(source.equals(investigatorForm.btnSyncInv)){
            syncInvestigator();
        }else if(source.equals(investigatorForm.btnDelSyncInv)){
            deleteAndSyncInvestigator();
        }
        //2796 End
    }
    
    /**
     * This method will provide a row by performing the add action
     * @return void
     **/
    private void performAddAction() {
//      investigatorCellEditor.stopCellEditing();
        int rowCount =  investigatorForm.tblInvestigator.getRowCount();
        if(rowCount  > 0) {
            if(cvInvestigator != null && cvInvestigator.size() > 0){
                for(int index =0 ; index < cvInvestigator.size(); index++){
                    InvestigatorBean bean = (InvestigatorBean)
                    cvInvestigator.get(index);
                    if ( EMPTY_STRING.equals( bean.getPersonId().trim() ) ) {
                        investigatorForm.tblInvestigator.setRowSelectionInterval(index,index);
                        investigatorForm.tblInvestigator.scrollRectToVisible(
                                investigatorForm.tblInvestigator.getCellRect(index ,0, true));
                        investigatorForm.tblInvestigator.editCellAt(index,
                                InvestigatorCellEditor.INVESTIGATOR_NAME_COLUMN);
                        
                        return ;
                    }
                }
            }
        }
        
        InvestigatorBean newBean= new InvestigatorBean();
        newBean.setPersonName(EMPTY_STRING);
        newBean.setPersonId(EMPTY_STRING);
        newBean.setPercentageEffort(0.00f);
        newBean.setAcType(INSERT_RECORD);
        if( rowCount == 0 ) {
            newBean.setPrincipalInvestigatorFlag(true);
        }
        dataChanged = true;
        cvInvestigator  = investigatorTableModel.getData();
        if( cvInvestigator == null ) {
            cvInvestigator = new CoeusVector();
        }
        cvInvestigator.add(newBean);
        investigatorTableModel.fireTableRowsInserted(
                investigatorTableModel.getRowCount()+1,investigatorTableModel.getRowCount() + 1);
        
        int lastRow = investigatorForm.tblInvestigator.getRowCount() - 1;
        if (lastRow >= 0){
            investigatorForm.tblInvestigator.setRowSelectionInterval(lastRow,lastRow);
            investigatorForm.tblInvestigator.scrollRectToVisible(
                    investigatorForm.tblInvestigator.getCellRect(lastRow,
                    INVESTIGATOR_NAME_COLUMN, true));
            investigatorForm.btnDelete.setEnabled( true );
            investigatorForm.btnDelSyncInv.setEnabled(isSyncEnabled());//2796
            //Case 2106 Start 3
            investigatorForm.btnCreditSplit.setEnabled(true);
            //Case 2106 End 3
            //Added for Case#2136 Enhancement start 2
            investigatorForm.btnAdminType.setEnabled( true );
            //Added for Case#2136 Enhancement end 2
            
        }
        investigatorForm.tblInvestigator.requestFocusInWindow();
        investigatorForm.tblInvestigator.editCellAt(lastRow,INVESTIGATOR_NAME_COLUMN);
        investigatorForm.tblInvestigator.getEditorComponent().requestFocusInWindow();
    }
    
    
    /* supporting method to delete a investigator row */
    //Method Param modified with case 2796:Sync To Parent
    public void performDelete(boolean syncRequired){
        // delete investigator row after confirmation
        int selectedRow = investigatorForm.tblInvestigator.getSelectedRow();
        if( selectedRow != -1 ){
            InvestigatorBean invBean = investigatorTableModel.getInvestigatorBean(selectedRow);
            String name = "";
            String personId="";
            if( invBean != null ) {
                name = invBean.getPersonName();
                personId = invBean.getPersonId();
            }
            
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
                    if(investigatorForm.tblInvestigator.getRowCount() > 1 && checkInvHasAddsToHundredCreditSplit(personId)){
                        int selectedOpt = CoeusOptionPane.showQuestionDialog(
                                coeusMessageResources.parseMessageKey(CREDIT_SPLIT_EXISTS_FOR_INV) ,
                                CoeusOptionPane.OPTION_YES_NO,
                                CoeusOptionPane.DEFAULT_YES);
                        if(selectedOpt == JOptionPane.YES_OPTION){
                            if(selectedOpt == JOptionPane.YES_OPTION){
                                if(isDataChanged()){
                                      MessageFormat formatter = new MessageFormat("");
                                      String message = formatter.format(
                                              coeusMessageResources.parseMessageKey(SAVE_BEFORE_OPEN_CREDIT_SPILT),
                                              this.moduleText);
                                      CoeusOptionPane.showInfoDialog(message);
                                }else{
                                    performCreditSplitAction();
                                }
                            }
                            
                        }
                    }else{
                        //COEUSQA-2037 : End
                        if(personId != null && personId.trim().length()>0){
                            cvInvestigator.remove(selectedRow);
//                        String acType = invBean.getAcType();
//                        if( null == acType || UPDATE_RECORD.equals(acType)){
                            // Added for COEUSQA-3642 : Issues with COEUSQA-1676 Credit Split Infrastructure project - Start
                            if(CoeusGuiConstants.AWARD_MODULE.equals(moduleName)){
                                deleteInvAndUnitCreditSplit(invBean.getPersonId(),invBean.getAcType());
                            }
                            // Added for COEUSQA-3642 : Issues with COEUSQA-1676 Credit Split Infrastructure project - End
                            invBean.setAcType(DELETE_RECORD);
                            invBean.setSyncRequired(syncRequired);//2796
                            deletedInvestigators.add(invBean);
//                        }
                            if(hmDeletedUnits.containsKey(personId)){
                                hmDeletedUnits.remove(personId);
                            }
                            investigatorTableModel.fireTableDataChanged();
                        }
                        dataChanged = true;
                    }
                }
            }else{
//                 int selectedOption = showDeleteConfirmMessage(coeusMessageResources.parseMessageKey(
//                                        "protoInvForm_exceptionCode.1221"));
//                    if( selectedOption == JOptionPane.YES_OPTION ){
                cvInvestigator.remove(selectedRow);
                investigatorTableModel.fireTableDataChanged();
                dataChanged = true;
//                    }
            }
            if( investigatorForm.tblInvestigator.getRowCount() <= 0 ){
                /* if there are no investigator rows after deleting this
                  particular row then clear all entries in unit table */
                investigatorForm.btnDelete.setEnabled( false );
                investigatorForm.btnDelSyncInv.setEnabled(false);//2796
                //Case 2106 Start 4
                investigatorForm.btnCreditSplit.setEnabled(false);
                //Case 2106 End 4
                //Added for Case#2136 Enhancement start 3
                investigatorForm.btnAdminType.setEnabled( false );
                //Added for Case#2136 Enhancement end 3
                
                unitTableModel.setData(null);
                unitTableModel.fireTableDataChanged();
                investigatorForm.tblInvestigator.requestFocusInWindow();
                investigatorForm.btnAdd.requestFocusInWindow();
                investigatorForm.btnDelUnit.setEnabled( false );
                investigatorForm.btnAddUnit.setEnabled( false );
                investigatorForm.btnFindUnit.setEnabled( false );
            }
            
            int newRowCount = investigatorForm.tblInvestigator.getRowCount();
            int newSelRow = 0;
            if(newRowCount >0){
                if(newRowCount > selectedRow){
                    investigatorForm.tblInvestigator.setRowSelectionInterval(selectedRow,
                            selectedRow);
                    newSelRow = selectedRow;
                }else{
                    investigatorForm.tblInvestigator.setRowSelectionInterval(
                            newRowCount - 1,newRowCount - 1);
                    newSelRow = newRowCount - 1;
                }
//                investigatorForm.tblInvestigator.requestFocusInWindow();
//                investigatorForm.tblInvestigator.editCellAt(newSelRow,1);
//                investigatorForm.tblInvestigator.getEditorComponent().requestFocusInWindow();
            }
        }else{
            showWarningMessage(
                    coeusMessageResources.parseMessageKey(
                    "protoInvFrm_exceptionCode.1066") );
        }
    }
    
    
    /* supporting method used for person search */
    private void performFindPerson(){
        try{
            CoeusSearch proposalSearch = null;
//            int selectedRow = investigatorForm.tblInvestigator.getSelectedRow();
            
            proposalSearch = new CoeusSearch(CoeusGuiConstants.getMDIForm(), "PERSONSEARCH",
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
                    
                    InvestigatorBean investigator = new InvestigatorBean();
                    investigator.setPersonId(personId);
                    investigator.setPersonName(personName);
                    investigator.setPercentageEffort( new Float(0).floatValue() );
                    investigator.setPrincipalInvestigatorFlag(false);
                    investigator.setFacultyFlag(faculty);
                    investigator.setNonMITPersonFlag(false);
                    investigator.setAcType(INSERT_RECORD );
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
    
    /* supported method to perform rolodex search */
    private void performFindRolodex(){
        try{
            CoeusSearch proposalSearch = null;
            proposalSearch = new CoeusSearch( CoeusGuiConstants.getMDIForm(),
                    CoeusGuiConstants.ROLODEX_SEARCH,
                    CoeusSearch.TWO_TABS_WITH_MULTIPLE_SELECTION ) ;
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
                    InvestigatorBean investigator = new InvestigatorBean();
                    investigator.setPersonId(rolodexID);
                    investigator.setPersonName(rolodexName);
                    investigator.setPercentageEffort(0.0f);
                    investigator.setPrincipalInvestigatorFlag(false);
                    investigator.setFacultyFlag(false);
                    // as rolodex is not employee so set non-employee flag true
                    investigator.setNonMITPersonFlag(true);
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
    
    /* supporting method to add blank row the units table */
    private void performAddUnit(){
        int selectedRow = investigatorForm.tblInvestigator.getSelectedRow();
        int rowCount = investigatorForm.tblUnits.getRowCount();
        if( selectedRow != -1 ){
            InvestigatorBean invBean = investigatorTableModel.getInvestigatorBean(selectedRow);
            String invPersonId="";
            if(invBean != null ) {
                invPersonId = invBean.getPersonId();
            }
            if(invPersonId != null && invPersonId.trim().length()>0){
                /* if investigator data present for selected row in
                  investigator table then add blank row to unit table */
                InvestigatorUnitBean unitBean = new InvestigatorUnitBean();
                unitBean.setPersonId(invPersonId);
                unitBean.setAcType(INSERT_RECORD);
                CoeusVector cvUnits = invBean.getInvestigatorUnits();
                if( cvUnits == null ) {
                    cvUnits = new CoeusVector();
                }
                int unitRowCount = unitTableModel.getRowCount();
                if( unitRowCount > 0 ){
                    String unitNumber = (String)unitTableModel.getValueAt( unitRowCount-1,
                            InvestigatorUnitTableModel.UNIT_NUMBER_COLUMN);
                    if( unitNumber == null || unitNumber.trim().length() == 0 ) {
                        investigatorForm.tblUnits.setRowSelectionInterval(unitRowCount-1,
                                unitRowCount-1);
                        investigatorForm.tblUnits.scrollRectToVisible(
                                investigatorForm.tblUnits.getCellRect(unitRowCount-1 ,0, true));
                        investigatorForm.tblUnits.requestFocusInWindow();
                        investigatorForm.tblUnits.editCellAt(unitRowCount-1,
                                InvestigatorUnitTableModel.UNIT_NUMBER_COLUMN);
                        investigatorForm.tblUnits.getEditorComponent().requestFocusInWindow();
                        return;
                    }
                }
                // Added for COEUSQA-3642 : Issues with COEUSQA-1676 Credit Split Infrastructure project - Start
                if(CoeusGuiConstants.AWARD_MODULE.equals(moduleName)){
                    //COEUSQA-4022
                    if(unitBean.getUnitNumber() != null ){
                    addInvCreditOrUnitSplitDetails(invPersonId,CoeusGuiConstants.EMPTY_STRING,false,unitBean.getUnitNumber());
                   }
                }
                // Added for COEUSQA-3642 : Issues with COEUSQA-1676 Credit Split Infrastructure project - End
                
                cvUnits.add(unitBean);
                invBean.setInvestigatorUnits(cvUnits);
                String invAcType = invBean.getAcType();
                if( null == invAcType ) {
                    invBean.setAcType(UPDATE_RECORD);
                }
                cvInvestigator.set(selectedRow, invBean);
                unitTableModel.setData(cvUnits);
                unitTableModel.fireTableDataChanged();
                rowCount = investigatorForm.tblUnits.getRowCount() -1 ;
                investigatorForm.tblUnits.setRowSelectionInterval(rowCount, rowCount);
                investigatorForm.tblUnits.scrollRectToVisible(
                        investigatorForm.tblUnits.getCellRect(rowCount ,0, true));
                dataChanged = true;
                if( investigatorForm.tblUnits.getRowCount() > 0 ){
                    investigatorForm.btnDelUnit.setEnabled( true );
                    investigatorForm.btnAddUnit.setEnabled( true );
                    investigatorForm.btnFindUnit.setEnabled( true );
                }
            }else{
                showWarningMessage(
                        coeusMessageResources.parseMessageKey(
                        "protoInvFrm_exceptionCode.1066") );
                return;
            }
        }else{
            showWarningMessage(
                    coeusMessageResources.parseMessageKey(
                    "protoInvFrm_exceptionCode.1066") );
            return;
        }
        
        investigatorForm.tblUnits.requestFocusInWindow();
        investigatorForm.tblUnits.editCellAt(rowCount,UnitCellEditor.UNIT_NUMBER_COLUMN);
        investigatorForm.tblUnits.getEditorComponent().requestFocusInWindow();
    }
    
    
    /* supporting method to delete the selected unit row from the units table */
    public void performDeleteUnit(){
        int selectedRow = investigatorForm.tblInvestigator.getSelectedRow();
        /* delete the selected unit information from table as well as from
          the vector which consists of all unit details for a particular
          investigator */
        if(selectedRow != -1){
            InvestigatorBean invBean = investigatorTableModel.getInvestigatorBean(selectedRow);
            String personId="";
            if( invBean != null ){
                personId = invBean.getPersonId();
            }
            int selectedUnitRow = investigatorForm.tblUnits.getSelectedRow();
            if( selectedUnitRow != -1 ){
                String unitNo = "";
                InvestigatorUnitBean unitBean = unitTableModel.getUnitBean(selectedUnitRow);
                if( unitBean != null ) {
                    unitNo = unitBean.getUnitNumber();
                }
                if(unitNo != null && unitNo.trim().length()>0){
                    int selectedOption = showDeleteConfirmMessage("Are you sure you want"
                            +" to remove "+unitNo+" ?");
                    if( selectedOption == JOptionPane.YES_OPTION ){
                        //Added for COEUSQA-2037 : Software allows you to delete an investigator who is assigned credit in the credit split window
                        //When there is only one unit for the investigator exists,
                        //removes the unit without check the credit split
                        //When multiple unit exists, Checks for the unit credit split information,
                        //until split set to '0' won't allow to delete the unit
                         //COEUSQA:1676 - Award Credit Split - Start
//                        if(investigatorForm.tblUnits.getRowCount() > 1 
//                                && checkInvUnitHasAddsToHundCreditSplit(personId,unitNo)){
                        if(investigatorForm.tblUnits.getRowCount() > 0
                                && checkInvUnitHasAddsToHundCreditSplit(personId,unitNo)){
                        //COEUSQA:1676 - End
                            int selectedOpt = CoeusOptionPane.showQuestionDialog(
                                    coeusMessageResources.parseMessageKey(CREDIT_SPLIT_EXISTS_FOR_INV_UNIT) ,
                                    CoeusOptionPane.OPTION_YES_NO,
                                    CoeusOptionPane.DEFAULT_YES);
                            if(selectedOpt == JOptionPane.YES_OPTION){
                                if(isDataChanged()){
                                    MessageFormat formatter = new MessageFormat("");
                                    String message = formatter.format(
                                            coeusMessageResources.parseMessageKey(SAVE_BEFORE_OPEN_CREDIT_SPILT),
                                            this.moduleText);
                                    CoeusOptionPane.showInfoDialog(message);
                                }else{
                                    performCreditSplitAction();
                                }
                            }
                        }else{
                            InvestigatorUnitBean personUnitBean = null;
                            CoeusVector invUnits = invBean.getInvestigatorUnits();
                            CoeusVector delUnits = invUnits.filter( new Equals("unitNumber",unitNo));
                            invUnits.remove(selectedUnitRow);
                            unitTableModel.setData(invUnits);
                            invBean.setInvestigatorUnits(invUnits);
                            String invAcType = invBean.getAcType();
                            if( null == invAcType ) {
                                invBean.setAcType(UPDATE_RECORD);
                            }
                            cvInvestigator.set(selectedRow,invBean);
                            unitTableModel.fireTableDataChanged();
                            investigatorTableModel.setData(cvInvestigator);
                            investigatorTableModel.fireTableDataChanged();
                            investigatorForm.tblInvestigator.setRowSelectionInterval(
                                    selectedRow, selectedRow);
                            if( delUnits != null && delUnits.size() > 0) {
                                int delSize = delUnits.size();
                                CoeusVector deletedUnits = (CoeusVector)hmDeletedUnits.get(personId);
                                if( null == deletedUnits ){
                                    deletedUnits = new CoeusVector();
                                }
                                // Added for COEUSQA-3642 : Issues with COEUSQA-1676 Credit Split Infrastructure project - Start
                                String acType = CoeusGuiConstants.EMPTY_STRING;
                                // Added for COEUSQA-3642 : Issues with COEUSQA-1676 Credit Split Infrastructure project - End
                                for( int indx = 0; indx < delSize; indx++ ) {
                                    personUnitBean = (InvestigatorUnitBean)delUnits.get(indx);
//                                String acType = personUnitBean.getAcType();
//                                if( null == acType || UPDATE_RECORD.equals(acType)){
                                    // Added for COEUSQA-3642 : Issues with COEUSQA-1676 Credit Split Infrastructure project - Start
                                    acType = personUnitBean.getAcType();
                                    // Added for COEUSQA-3642 : Issues with COEUSQA-1676 Credit Split Infrastructure project - End
                                    personUnitBean.setAcType(DELETE_RECORD);
                                    deletedUnits.add(personUnitBean);
//                                }
                                }
                                hmDeletedUnits.put(personId, deletedUnits);
                                // Added for COEUSQA-3642 : Issues with COEUSQA-1676 Credit Split Infrastructure project - Start
                                if(CoeusGuiConstants.AWARD_MODULE.equals(moduleName)){
                                    deleteUnitCreditSplitForInv(personId,unitNo, acType);
                                }
                                // Added for COEUSQA-3642 : Issues with COEUSQA-1676 Credit Split Infrastructure project - End
                            }
                            dataChanged = true;
                        }
                    }
                }else{
//                     int selectedOption = showDeleteConfirmMessage(
//                        coeusMessageResources.parseMessageKey(
//                                        "unitDetFrm_exceptionCode.1331"));
//                    if( selectedOption == JOptionPane.YES_OPTION ){
                    dataChanged = true;
                    CoeusVector invUnits = invBean.getInvestigatorUnits();
                    invUnits.remove(selectedUnitRow);
                    unitTableModel.setData(invUnits);
                    invBean.setInvestigatorUnits(invUnits);
                    cvInvestigator.set(selectedRow,invBean);
                    unitTableModel.fireTableDataChanged();
//                    }
                }
                
                int newRowCount = investigatorForm.tblUnits.getRowCount();
                if(newRowCount >0){
                    if(newRowCount > selectedUnitRow){
                        investigatorForm.tblUnits.setRowSelectionInterval(selectedUnitRow,
                                selectedUnitRow);
                    }else{
                        investigatorForm.tblUnits.setRowSelectionInterval(
                                newRowCount - 1,
                                newRowCount - 1);
                    }
                }else{
                    investigatorForm.btnDelUnit.setEnabled( false );
                    investigatorForm.btnAddUnit.requestFocusInWindow();
                }
            }else{
                showWarningMessage(coeusMessageResources.parseMessageKey(
                        "protoInvFrm_exceptionCode.1133") );
            }
        }
        
    }
    
    /* supporting method to perform unit search */
    private void performFindUnit(){
        try{
            int selectedRow = investigatorForm.tblInvestigator.getSelectedRow();
            int selectedUnitRow = investigatorForm.tblUnits.getSelectedRow();
            CoeusSearch proposalSearch = null;
            String unitID = null;
            // check whether any investigator has been selected or not
            if(selectedRow != -1){
                
                String pId =  "";
                InvestigatorBean invBean = investigatorTableModel.getInvestigatorBean(selectedRow);
                if( invBean != null ){
                    pId = invBean.getPersonId();
                }
                // check whether investigator specified in selected row
                if(pId != null && pId.trim().length()>0){
                    /* cancel unit number colum editing when find unit
                      button is clicked */
                    
                    proposalSearch = new CoeusSearch( CoeusGuiConstants.getMDIForm(),
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
                            
                            /* check for duplicate unit number for a particular
                              investigator */
                            if( !unitTableModel.isDuplicateUnitID( unitID )) {
                                dataChanged = true;
                                /* if not duplicate construct unit bean and add to
                                  unit hashtable */
                                InvestigatorUnitBean unitBean =
                                        new InvestigatorUnitBean();
                                unitBean.setPersonId(pId);
                                unitBean.setOspAdministratorName(adminPerson);
                                unitBean.setUnitNumber(unitID);
                                unitBean.setUnitName(unitName);
                                unitBean.setLeadUnitFlag(false);
                                unitBean.setAcType( INSERT_RECORD );
                                // Added for COEUSQA-3642 : Issues with COEUSQA-1676 Credit Split Infrastructure project - Start
                                if(CoeusGuiConstants.AWARD_MODULE.equals(moduleName)){
                                    addInvCreditOrUnitSplitDetails(pId,CoeusGuiConstants.EMPTY_STRING,false,unitID);
                                }
                                // Added for COEUSQA-3642 : Issues with COEUSQA-1676 Credit Split Infrastructure project - End

                                CoeusVector cvInvUnits = invBean.getInvestigatorUnits();
                                if( cvInvUnits == null ) {
                                    cvInvUnits = new CoeusVector();
                                }
//                                addToHashtable(pId,unitBean);
                                if( selectedUnitRow != -1 ){
                                    // if any unit table row is selected update that
                                    //  row
                                    String oldUnitNumber = (String)unitTableModel.getValueAt(
                                            selectedUnitRow,InvestigatorUnitTableModel.UNIT_NUMBER_COLUMN);
                                    if(oldUnitNumber == null
                                            || oldUnitNumber.trim().length() == 0 ){
                                        cvInvUnits.set(selectedUnitRow,unitBean);
                                    }else{
                                        cvInvUnits.add(unitBean);
                                    }
                                }else{
                                    cvInvUnits.add(unitBean);
                                }
                                unitTableModel.setData(cvInvUnits);
                                invBean.setInvestigatorUnits(cvInvUnits);
                                String invAcType = invBean.getAcType();
                                if( null == invAcType ) {
                                    invBean.setAcType(UPDATE_RECORD);
                                }
                                cvInvestigator.set(selectedRow,invBean);
                                unitTableModel.fireTableDataChanged();
                            }else{
                                showWarningMessage( "' " + unitID + " ' " +
                                        coeusMessageResources.parseMessageKey(
                                        "protoInvFrm_exceptionCode.1137"));
                            }
                        }// end of for
                    } // end of vSelectedUnit != null
                    if( investigatorForm.tblUnits.getRowCount() > 0 ){
                        int lastRow = investigatorForm.tblUnits.getRowCount() -1 ;
                        investigatorForm.tblUnits.scrollRectToVisible(
                                investigatorForm.tblUnits.getCellRect(lastRow ,0, true));
                        investigatorForm.tblUnits.setRowSelectionInterval(lastRow,
                                lastRow);
                        /* enable delete unit button if any unit information
                         is present for selected investigator */
                        investigatorForm.btnDelUnit.setEnabled( true );
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
    
    //supporting method to check for null value
    private String checkForNull( Object value ){
        return (value==null)? "":value.toString();
        /*return value.toString().equalsIgnoreCase( "null" ) ?  "" :
            value.toString();
         */
    }
    
    /* supporting method used to set the investigator bean into hashtable and
       update the investigator table. If homeUnit has been specified then
       this method will update unit table and makes an entry in unit hashtable.
       If replace row is true it will update the selected row otherwise adds a
       new row to the table.
     */
    public void setInvestigatorBean(InvestigatorBean investigator,
            String homeUnit, boolean replaceRow) {
        
        int selectedRow = investigatorForm.tblInvestigator.getSelectedRow();
        String oldPersonId = "";
        boolean primaryInv = false;
        InvestigatorBean invBean = null;
        if (selectedRow != -1){
            invBean = investigatorTableModel.getInvestigatorBean(selectedRow);
            if( invBean != null ) {
                oldPersonId = invBean.getPersonId();
                primaryInv = invBean.isPrincipalInvestigatorFlag();
                
            }
            int lastRow = investigatorForm.tblInvestigator.getRowCount() - 1;
            if( oldPersonId == null  || oldPersonId.trim().length() == 0 ){
                replaceRow = true;
                investigator.setPrincipalInvestigatorFlag(primaryInv);
            }else if( selectedRow != lastRow  && !replaceRow){
                selectedRow = lastRow;
                invBean = investigatorTableModel.getInvestigatorBean(selectedRow);
                if( invBean != null ) {
                    oldPersonId = invBean.getPersonId();
                    primaryInv = invBean.isPrincipalInvestigatorFlag();
                    investigator.setPrincipalInvestigatorFlag(primaryInv);
                }
                if( oldPersonId == null  || oldPersonId.trim().length() == 0 ){
                    replaceRow = true;
                    investigator.setPrincipalInvestigatorFlag(primaryInv);
                }
            }
        } else if(investigatorForm.tblInvestigator.getRowCount() == 0){
            investigator.setPrincipalInvestigatorFlag(true);
        }
        
        String personId = investigator.getPersonId();
        String personName = investigator.getPersonName();
//        boolean pi = investigator.isPrincipalInvestigatorFlag();
//        boolean faculty = investigator.isFacultyFlag();
//        boolean nonEmp = investigator.isNonMITPersonFlag();
//        float effortPercent = investigator.getPercentageEffort();
        
        if(!personId.equals(oldPersonId)){
            // check for duplicate person entry
            if(! checkDuplicatePerson( personId ,selectedRow)){
                dataChanged = true;
                if (replaceRow ) {
                    if( null == invBean.getAcType() ||
                            UPDATE_RECORD.equals(invBean.getAcType())){
                        // Added for COEUSQA-3642 : Issues with COEUSQA-1676 Credit Split Infrastructure project - Start
                        if(CoeusGuiConstants.AWARD_MODULE.equals(moduleName)){
                            deleteInvAndUnitCreditSplit(invBean.getPersonId(), TypeConstants.DELETE_RECORD);
                        }
                        // Added for COEUSQA-3642 : Issues with COEUSQA-1676 Credit Split Infrastructure project - End
                        invBean.setAcType(DELETE_RECORD);
                        try{
                            deletedInvestigators.add(ObjectCloner.deepCopy(invBean));
                        }catch(Exception e){
                            CoeusOptionPane.showErrorDialog(e.getMessage());
                        }
                        //Added for COEUSQA-2383 - Two Lead Unit appear for an Award - commented this code - start
                        //Deleted entry is required in this vector for removing it from database.
//                        if( hmDeletedUnits.containsKey(invBean.getPersonId())){
//                            hmDeletedUnits.remove(invBean.getPersonId());
//                        }
                        // Added for COEUSQA-2383 - Two Lead Unit appear for an Award - commented this code - end
                    }
                    invBean = investigator;
                    cvInvestigator.set(selectedRow, invBean);
                    investigatorTableModel.fireTableDataChanged();
                    investigatorForm.tblInvestigator.setRowSelectionInterval(selectedRow,
                            selectedRow);
                } else {
//                    // otherwise add new entry
                    cvInvestigator.add(investigator);
                    investigatorTableModel.fireTableDataChanged();
                    int  newRowCount = investigatorForm.tblInvestigator.getRowCount();
                    selectedRow = newRowCount - 1;
                    investigatorForm.tblInvestigator.setRowSelectionInterval(selectedRow,
                            selectedRow);
                    investigatorForm.tblInvestigator.scrollRectToVisible(
                            investigatorForm.tblInvestigator.getCellRect(selectedRow ,0, true));
                    investigatorForm.tblInvestigator.editCellAt(selectedRow,
                            InvestigatorCellEditor.INVESTIGATOR_NAME_COLUMN);
                    
                }
                // Added for COEUSQA-3642 : Issues with COEUSQA-1676 Credit Split Infrastructure project - Start                
                if(TypeConstants.INSERT_RECORD.equals(investigator.getAcType()) && 
                        CoeusGuiConstants.AWARD_MODULE.equals(moduleName)){
                    addInvCreditOrUnitSplitDetails(investigator.getPersonId(),
                            investigator.getPersonName(),investigator.isPrincipalInvestigatorFlag(),CoeusGuiConstants.EMPTY_STRING);
                }
                // Added for COEUSQA-3642 : Issues with COEUSQA-1676 Credit Split Infrastructure project - End
                if(homeUnit != null && homeUnit.trim().length()>0){
                        /* if investigator have home unit add corresponding unit
                          details to the existing unit information for that
                          particular investigator by checking for duplicate entry */
                    UnitDetailFormBean unitDetail = getUnitInfoBean( homeUnit );
                    if( (unitDetail != null) && (unitDetail.getUnitNumber() != null)){
                        if( !unitTableModel.isDuplicateUnitID( unitDetail.getUnitNumber() )){
                            InvestigatorUnitBean unitBean = new InvestigatorUnitBean();
                            
                            unitBean.setUnitNumber(unitDetail.getUnitNumber());
                            unitBean.setUnitName(unitDetail.getUnitName());
                            unitBean.setPersonId(personId);
                            unitBean.setLeadUnitFlag(false);
                            unitBean.setOspAdministratorName(unitDetail.getOspAdminName());
                            unitBean.setAcType( INSERT_RECORD );
                            // Added for COEUSQA-3642 : Issues with COEUSQA-1676 Credit Split Infrastructure project - Start
                            if(CoeusGuiConstants.AWARD_MODULE.equals(moduleName)){
                                addInvCreditOrUnitSplitDetails(personId,CoeusGuiConstants.EMPTY_STRING,false, unitDetail.getUnitNumber());
                            }
                            // Added for COEUSQA-3642 : Issues with COEUSQA-1676 Credit Split Infrastructure project - End
                            CoeusVector unitData = investigator.getInvestigatorUnits();
                            //For bug fix # 1618
                            unitData = new CoeusVector();
                            unitData.add(unitBean);
                            unitTableModel.setData(unitData);
                            investigator.setInvestigatorUnits(unitData);
                            investigatorForm.tblUnits.setRowSelectionInterval(0,0);
                            investigatorForm.btnDelUnit.setEnabled(true);
                        }
                    }
                } else {
                    CoeusVector unitData = new CoeusVector();
                    //unitData.add(unitBean);
                    unitTableModel.setData(unitData);
                    investigator.setInvestigatorUnits(unitData);
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
        if( investigatorForm.tblInvestigator.getRowCount() > 0 ){
            investigatorForm.btnDelete.setEnabled( true );
            investigatorForm.btnDelSyncInv.setEnabled(isSyncEnabled());
            //Case 2106 Start 5
            investigatorForm.btnCreditSplit.setEnabled(true);
            //Case 2106 End 5
            //Added for Case#2136 Enhancement start 4
            investigatorForm.btnAdminType.setEnabled( true );
            //Added for Case#2136 Enhancement end 4
            
            investigatorForm.btnAddUnit.setEnabled( true );
            investigatorForm.btnFindUnit.setEnabled( true );
        }
    }
    private boolean checkDuplicatePerson(String personId, int selectedRow){
        if( cvInvestigator != null ){
            CoeusVector dupInvestigators = cvInvestigator.filter(
                    new Equals("personId",personId));
            if( dupInvestigators != null && dupInvestigators.size() > 0 ) {
                return true;
            }
        }
        return false;
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
    
    //supporting method to get UnitDetailFormBean for specific unit number
    //which will be used to validate the unit id/name against the
    
    private UnitDetailFormBean getUnitInfoBean( String unitNumber ){
        boolean success=false;
//        String personID=null;
        RequesterBean requester = new RequesterBean();
        requester.setFunctionType('G');
        requester.setId( unitNumber );
        String connectTo = CoeusGuiConstants.CONNECTION_URL + "/unitServlet";
        AppletServletCommunicator comm = new AppletServletCommunicator(connectTo, requester);
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
    
    public void updateInvestigator(String personId, InvestigatorUnitBean unitBean) {
        CoeusVector cvSelectedInv = cvInvestigator.filter(new Equals("personId",personId));
        InvestigatorBean invBean = (InvestigatorBean)cvSelectedInv.get(0);
//        String oldUnitNumber = unitBean.getUnitNumber();
        String acType = unitBean.getAcType();
//        if(!(null ==  oldUnitNumber || EMPTY_STRING.equals(oldUnitNumber))){
        if( null == acType || UPDATE_RECORD.equals(acType) ){
            unitBean.setAcType(DELETE_RECORD);
            CoeusVector delUnits = new CoeusVector();
            if( hmDeletedUnits.containsKey(invBean.getPersonId())) {
                delUnits = (CoeusVector)hmDeletedUnits.get(invBean.getPersonId());
            }
            delUnits.add(unitBean);
            hmDeletedUnits.put(invBean.getPersonId(),delUnits);
        }
//        }
        dataChanged = true;
        invBean.setInvestigatorUnits(unitTableModel.getData());
        String invAcType = invBean.getAcType();
        if( null == invAcType ) {
            invBean.setAcType(UPDATE_RECORD);
        }
        
    }
    
    public void updateUnits(){
        int perSelRow = investigatorForm.tblInvestigator.getSelectedRow();
        if( perSelRow != -1 ) {
            InvestigatorBean invBean = (InvestigatorBean)cvInvestigator.get( perSelRow );
            invBean.setInvestigatorUnits(unitTableModel.getData());
            String invAcType = invBean.getAcType();
            if( null == invAcType ) {
                invBean.setAcType(UPDATE_RECORD);
            }
            dataChanged = true;
        }
    }
    /** Getter for property dataChanged.
     * @return Value of property dataChanged.
     *
     */
    public boolean isDataChanged() {
        investigatorCellEditor.stopCellEditing();
        unitCellEditor.stopCellEditing();
        return dataChanged;
    }
    
    /** Setter for property dataChanged.
     * @param dataChanged New value of property dataChanged.
     *
     */
    public void setDataChanged(boolean dataChanged) {
        this.dataChanged = dataChanged;
    }
    
    //Case 2106 Start 6
    public Object getCreditSplitData(){
        //CoeusVector cvData = new CoeusVector();
        
        Vector vecInvData = prepareData();
        
        InvCreditSplitObject invCreditSplitObject = new InvCreditSplitObject();
        invCreditSplitObject.setModuleName(getModuleName());
        invCreditSplitObject.setModuleNumber(getModuleNumber());
        invCreditSplitObject.setSequenceNo(getSeqNo());
        invCreditSplitObject.setInvData(vecInvData);
        
        //cvData.add(invCreditSplitObject);
        /*InvestigatorCreditSplitController invCreditSplitController =
                        new InvestigatorCreditSplitController(getFunctionType());
        invCreditSplitController.setFormData(invCreditSplitObject);
         
        invCreditSplitController.display();*/
        return invCreditSplitObject;
    }
    
    private Vector prepareData(){
        Vector vecData = new Vector();
        Hashtable htPersonData = new Hashtable();
        Hashtable htUnitData = new Hashtable();
        ProposalInvestigatorFormBean propInvestigatorFormBean;
        for (int index = 0 ; index < cvInvestigator.size() ; index++){
            propInvestigatorFormBean = new ProposalInvestigatorFormBean();
            
            InvestigatorBean invBean = (InvestigatorBean) cvInvestigator.get(index);
            
            propInvestigatorFormBean.setConflictOfIntersetFlag(invBean.isConflictOfIntersetFlag());
            propInvestigatorFormBean.setFacultyFlag(invBean.isFacultyFlag());
            propInvestigatorFormBean.setFedrDebrFlag(invBean.isFedrDebrFlag());
            propInvestigatorFormBean.setFedrDelqFlag(invBean.isFedrDelqFlag());
            propInvestigatorFormBean.setNonMITPersonFlag(invBean.isNonMITPersonFlag());
            propInvestigatorFormBean.setPercentageEffort(invBean.getPercentageEffort());
            propInvestigatorFormBean.setPersonId(invBean.getPersonId());
            propInvestigatorFormBean.setPersonName(invBean.getPersonName());
            propInvestigatorFormBean.setPrincipleInvestigatorFlag(invBean.isPrincipalInvestigatorFlag());
            
            /* JM 9-9-2015 added person status */
            propInvestigatorFormBean.setStatus(invBean.getStatus());
            /* JM END */    

            /* JM 9-9-2015 added isExternalPerson flag */
            propInvestigatorFormBean.setIsExternalPerson(invBean.getIsExternalPerson());
            /* JM END */  
            
            htPersonData.put(invBean.getPersonId() , propInvestigatorFormBean);
            
            CoeusVector cvInvUnitData = invBean.getInvestigatorUnits();
            if(cvInvUnitData != null && cvInvUnitData.size() > 0){
                Vector vecUnitData = new Vector();
                ProposalLeadUnitFormBean proposalLeadUnitFormBean = new ProposalLeadUnitFormBean();
                
                for(int i = 0 ; i < cvInvUnitData.size(); i++){
                    proposalLeadUnitFormBean = new ProposalLeadUnitFormBean();
                    InvestigatorUnitBean invUnitBean = (InvestigatorUnitBean)cvInvUnitData.get(i);
                    
                    proposalLeadUnitFormBean.setLeadUnitFlag(invUnitBean.isLeadUnitFlag());
                    proposalLeadUnitFormBean.setPersonId(invUnitBean.getPersonId());
                    proposalLeadUnitFormBean.setPersonName(invUnitBean.getPersonName());
                    proposalLeadUnitFormBean.setUnitName(invUnitBean.getUnitName());
                    proposalLeadUnitFormBean.setUnitNumber(invUnitBean.getUnitNumber());
                    //Added for Case#2136 Enhancement start 5
                    if(proposalLeadUnitFormBean.isLeadUnitFlag()){
                        setLeadUnitNo(proposalLeadUnitFormBean.getUnitNumber());
                    }
                    //Added for Case#2136 Enhancement end 5
                    vecUnitData.add(proposalLeadUnitFormBean);
                }
                htUnitData.put(invBean.getPersonId() , vecUnitData);
            }//End of inner if
        }//End of for
        
        vecData.add(htPersonData);
        vecData.add(htUnitData);
        return vecData;
    }
    
    /**
     * Getter for property moduleNumber.
     * @return Value of property moduleNumber.
     */
    public java.lang.String getModuleNumber() {
        return moduleNumber;
    }
    
    /**
     * Setter for property moduleNumber.
     * @param moduleNumber New value of property moduleNumber.
     */
    public void setModuleNumber(java.lang.String moduleNumber) {
        this.moduleNumber = moduleNumber;
    }
    
    /**
     * Getter for property moduleName.
     * @return Value of property moduleName.
     */
    public java.lang.String getModuleName() {
        return moduleName;
    }
    
    /**
     * Setter for property moduleName.
     * @param moduleName New value of property moduleName.
     */
    public void setModuleName(java.lang.String moduleName) {
        this.moduleName = moduleName;
    }
    
    /**
     * Getter for property seqNo.
     * @return Value of property seqNo.
     */
    public java.lang.String getSeqNo() {
        return seqNo;
    }
    
    /**
     * Setter for property seqNo.
     * @param seqNo New value of property seqNo.
     */
    public void setSeqNo(java.lang.String seqNo) {
        this.seqNo = seqNo;
    }
    
    
    //Case 2106 End 6
    //Added for Case#2136 Enhancement start 6
    /**
     * Getter for property leadUnitNo.
     * @return Value of property leadUnitNo.
     */
    public java.lang.String getLeadUnitNo() {
        return leadUnitNo;
    }
    
    /**
     * Setter for property leadUnitNo.
     * @param leadUnitNo New value of property leadUnitNo.
     */
    public void setLeadUnitNo(java.lang.String leadUnitNo) {
        this.leadUnitNo = leadUnitNo;
    }
    //Added for Case#2136 Enhancement end 6
    class InvestigatorDetailsAdapter extends MouseAdapter {
        public void mouseClicked(MouseEvent me){
            if( me.getClickCount() == 2 ) {
                int selRow = investigatorForm.tblInvestigator.getSelectedRow();
                if( selRow != -1 ) {
                    InvestigatorBean invBean = investigatorTableModel.getInvestigatorBean(selRow);
                    String personId = invBean.getPersonId();
                    boolean nonEmployee = invBean.isNonMITPersonFlag();
                    if( personId != null && personId.trim().length() > 0 ) {
                        if(nonEmployee){
                            /* selected investigator is a rolodex, so show
                               rolodex details */
                            RolodexMaintenanceDetailForm frmRolodex
                                    = new RolodexMaintenanceDetailForm('V',personId);
                            frmRolodex.showForm(CoeusGuiConstants.getMDIForm(),
                                    CoeusGuiConstants.TITLE_ROLODEX,true);
                        }else{
                            //Bug Fix: Pass the person id to get the person details Start 1
                            //String personName = invBean.getPersonName();
                            String loginUserName = CoeusGuiConstants.getMDIForm().getUserName();
                            /*Bug Fix:to get the person details with the person id instead of the person name.*/
                            //PersonInfoFormBean personInfoFormBean = (PersonInfoFormBean)coeusUtils.getPersonInfoID(personName);
                            //Bug Fix: Pass the person id to get the person details End 1
                            try{
                                //Bug Fix: Pass the person id to get the person details Start 2
                                //PersonDetailForm personDetailForm =
                                //new PersonDetailForm(personInfoFormBean.getPersonID(),loginUserName,DISPLAY_MODE);
                                
                                PersonDetailForm personDetailForm = new PersonDetailForm(personId ,loginUserName,DISPLAY_MODE);
                                //Bug Fix: Pass the person id to get the person details End 2
                            }catch ( Exception e) {
                                CoeusOptionPane.showInfoDialog( e.getMessage() );
                            }
                        }
                    }
                }
            }
        }
    }
    
    
    // supporting class to display unit details on double clicking of any row
    // in unit table.
    class UnitDisplayAdapter extends MouseAdapter {
        public void mouseClicked( MouseEvent me ) {
            investigatorCellEditor.stopCellEditing();//Added by Nadh for Bug fix 1611
            if (me.getClickCount() == 2) {
                int selUnitRow = investigatorForm.tblUnits.getSelectedRow();
                if( selUnitRow != -1 ) {
                    InvestigatorUnitBean unitBean = unitTableModel.getUnitBean(selUnitRow);
                    String unitNumber = unitBean.getUnitNumber();
                    if( unitNumber != null && unitNumber.trim().length() > 0 ) {
                        try{
                            UnitDetailForm frmUnit = new UnitDetailForm(unitNumber,'G');
                            frmUnit.showUnitForm(CoeusGuiConstants.getMDIForm());
                        } catch(Exception ex){
                            CoeusOptionPane.showErrorDialog(
                                    coeusMessageResources.parseMessageKey(
                                    "protoInvFrm_exceptionCode.1136"));
                        }
                    }
                }
            }
        }
    }
    
    // Added by Noorul for Enhancement 11-01-07 starts
    /**
     * To fetch Administrators details.
     * @throws CoeusException
     * @return coeusVector
     */
    public void fetchAdministratorData(){
        try{
            cvAdministratorDetails = getAdministratorDetails();
            if(moduleName.equals(CoeusGuiConstants.AWARD_MODULE)){
                investigatorForm.lblAdministrator.setText("Award Administrator:");
            }else if(moduleName.equals(CoeusGuiConstants.INSTITUTE_PROPOSAL_MODULE)){
                investigatorForm.lblAdministrator.setText("Proposal Administrator:");
            }
            investigatorForm.lblAdminPhone.setText("Phone No:");
            investigatorForm.lblAdminEmail.setText("Email:");
            investigatorForm.lblAdministratorResult.setText(EMPTY_STRING);
            investigatorForm.lblAdminPhoneNo.setText(EMPTY_STRING);
            investigatorForm.lblAdminEmailValue.setText(EMPTY_STRING);
            if(cvAdministratorDetails!=null && cvAdministratorDetails.size()>0){
                InvestigatorUnitAdminTypeBean adminTypeBean = (InvestigatorUnitAdminTypeBean) cvAdministratorDetails.get(0);
                investigatorForm.lblAdministratorResult.setText(adminTypeBean.getAdminName());
                investigatorForm.lblAdminPhoneNo.setText(adminTypeBean.getPhoneNumber());
                investigatorForm.lblAdminEmailValue.setText(adminTypeBean.getEmailAddress());
            } else {
                if(moduleName.equals(CoeusGuiConstants.AWARD_MODULE)){
                    investigatorForm.lblAdministratorResult.setText(coeusMessageResources.parseMessageKey(
                            "protoInvFrm_exceptionCode.10661"));
                }else if(moduleName.equals(CoeusGuiConstants.INSTITUTE_PROPOSAL_MODULE)){
                    investigatorForm.lblAdministratorResult.setText(coeusMessageResources.parseMessageKey(
                            "protoInvFrm_exceptionCode.10662"));
                }
                investigatorForm.lblAdminPhone.setText(EMPTY_STRING);
                investigatorForm.lblAdminEmail.setText(EMPTY_STRING);
            }
        } catch (CoeusException e){
            e.printStackTrace();
        }
    }
    
    /**
     * To get Proposal Administrators details.
     * @throws CoeusException
     * @return coeusVector
     */
    private CoeusVector getAdministratorDetails() throws CoeusException{
        CoeusVector cvData = new CoeusVector();
        RequesterBean request = new RequesterBean();
        ResponderBean response = null;
        String connectTo = null;
        
        request.setDataObject(moduleNumber);
        if(moduleName.equals(CoeusGuiConstants.AWARD_MODULE)){
            Vector vecData = new Vector();
            vecData.add(0, moduleNumber);
            vecData.add(1, moduleNumber);
            request.setDataObjects(vecData);
            request.setFunctionType(GET_AWARD_UNIT_ADMIN_TYPE_DATA);
            connectTo = CoeusGuiConstants.CONNECTION_URL + AWARD_SERVLET;
            
        }else if(moduleName.equals(CoeusGuiConstants.INSTITUTE_PROPOSAL_MODULE)){
            
            request.setFunctionType(GET_PROP_UNIT_ADMIN_TYPE_DATA);
            connectTo = CoeusGuiConstants.CONNECTION_URL + INST_PROP_SERVLET;
            
        }
        
        AppletServletCommunicator comm = new AppletServletCommunicator(connectTo, request);
        comm.send();
        response = comm.getResponse();
        if(response != null){
            if(!response.isSuccessfulResponse()){
                throw new CoeusException(response.getMessage());
            }
            CoeusVector cvMainData = (CoeusVector)response.getDataObject();
            cvData = (CoeusVector)cvMainData.get(1);
        }else{
            throw new CoeusException(response.getMessage());
        }
        
        return cvData;
    }
    // Added by Noorul for Enhancement 11-01-07 ends
    
    //COEUSDEV217 :Sync to Child Awards: Investigator Sync - inconsistent results and LOST AWARD NODE
    //Case 2796: Sync To Parent
    /* This function is called when the user selects Sycing of Investigator
     * in hierarchy
     * This function only performs necessary validations required prior to sync.
     * Override this function for module level sync implementation.
     */
    public boolean syncInvestigator() {
        return true;
    }
    
    /* This function is called when the user selects Delete and Sync Investigator
     * in hierarchy
     * This function only performs necessary validations required prior to sync.
     * Override this function for module level sync implementation.
     */
    public boolean deleteAndSyncInvestigator() {
        int selectedRow = investigatorForm.tblInvestigator.getSelectedRow();
        if(selectedRow==-1){
            showWarningMessage(coeusMessageResources.parseMessageKey(
                    "protoInvFrm_exceptionCode.1066") );
            return false;
        }else if (!validateBeforeDelete()){
            return false;
        }else{
            return true;
        }
    }
    //COEUSDEV217 : End
    public boolean isSyncEnabled() {
        return false;
    }
    
    public boolean validateBeforeDelete(){
        return validate(true);
    }
    
    //set boolean beforeDelInvestigator to true, if validation is required
    //before removing the investigator row
    private boolean validate(boolean beforeDelInvestigator){
        
        investigatorCellEditor.stopCellEditing();
        unitCellEditor.stopCellEditing();
        boolean piPresent = false;
        CoeusVector invData = null;
        try {
            invData = (CoeusVector) ObjectCloner.deepCopy(investigatorTableModel.getData());
        } catch (Exception ex) {
        }
        if( invData != null && invData.size() > 0 ){
            int selRow = investigatorForm.tblInvestigator.getSelectedRow();
            if(beforeDelInvestigator && selRow != -1){
                invData.remove(selRow);
            }
            int invSize = invData.size();
            for( int invIndx = 0 ; invIndx < invSize; invIndx++ ){
                InvestigatorBean invBean = (InvestigatorBean)invData.get(invIndx);
                if( invBean.getPersonId() != null && invBean.getPersonId().trim().length() > 0 ) {
                    CoeusVector invUnits = invBean.getInvestigatorUnits();
                    if( invUnits == null || invUnits.size() == 0 ){
                       /* if there is no unit information associated with any
                          investigator show message and return as validty false */
                        investigatorForm.tblInvestigator.setRowSelectionInterval(invIndx,invIndx);
                        log("There is no unit information associated with "
                                + invBean.getPersonName());
                        return false;
                    }else{
                        NotEquals notDeleted = new NotEquals("acType",DELETE_RECORD);
                        Equals nullAcTypes = new Equals("acType",null);
                        Or notDeletedOrNulls = new Or(notDeleted, nullAcTypes);
                        CoeusVector notDeletedUnits = invUnits.filter(notDeletedOrNulls);
                        if( notDeletedUnits == null || notDeletedUnits.size() == 0 ){
                           /* if there is no unit information associated with any
                              investigator show message and return as validty false */
                            investigatorForm.tblInvestigator.setRowSelectionInterval(invIndx,invIndx);
                            log("There is no unit information associated with "
                                    + invBean.getPersonName());
                            return false;
                        }
                        Equals nullUnit = new Equals("unitNumber",null);
                        CoeusVector nullUnits = (CoeusVector) notDeletedUnits.filter(
                                nullUnit );
                        if( notDeletedUnits.size() == 1 && nullUnits != null && nullUnits.size() > 0 ) {
                           /* if there is no unit information associated with any
                              investigator show message and return as validty false */
                            investigatorForm.tblInvestigator.setRowSelectionInterval(
                                    invIndx,invIndx);
                            log("There is no unit information associated with "
                                    + invBean.getPersonName());
                            return false;
                        }
                        if( invBean.isPrincipalInvestigatorFlag() ) {
                            piPresent = true;
                            Equals leadValue = new Equals("leadUnitFlag",true);
                            NotEquals unitValue = new NotEquals("unitNumber",null);
                            And validLead = new And(leadValue,unitValue);
//                            NotEquals notDeleted = new NotEquals("acType",DELETE_RECORD);
                            And validAndNotDeleted = new And( validLead, notDeletedOrNulls);
                            CoeusVector cvLeads = invUnits.filter(validAndNotDeleted);
                            if( cvLeads == null || cvLeads.size() == 0 ){
                                /* if none of the units have been marked as lead
                                  for primary investigator row then show error
                                  message */
                                log(coeusMessageResources.parseMessageKey(
                                        "protoInvFrm_exceptionCode.1063"));
                                return false;
                            }//Added for Case#3587 - multicampus enhancement  - Start
                            else if(CoeusGuiConstants.INSTITUTE_PROPOSAL_MODULE.equalsIgnoreCase(moduleName)){
                                InvestigatorUnitBean instituteUnitBean = (InvestigatorUnitBean)cvLeads.get(0);
                                String leadUnitNumber = instituteUnitBean.getUnitNumber();
                                if(leadUnitNo != null && !leadUnitNumber.equalsIgnoreCase(leadUnitNo)){
                                    boolean canModify = checkUserHasCreateIPRight(leadUnitNumber);
                                    if(!canModify){
                                        log(coeusMessageResources.parseMessageKey("instPropBase_exceptionCode.1067"));
                                        return false;
                                    }
                                }
                            }else if(CoeusGuiConstants.AWARD_MODULE .equalsIgnoreCase(moduleName)){
                                InvestigatorUnitBean investigatorUnitBean = (InvestigatorUnitBean) cvLeads.get(0);
                                String leadUnitNumber = investigatorUnitBean.getUnitNumber();
                                if(leadUnitNo != null && !leadUnitNumber.equalsIgnoreCase(leadUnitNo)){
                                    boolean canModify = checkUserHasCreateAwardRight(leadUnitNumber);
                                    if(!canModify){
                                        log(coeusMessageResources.parseMessageKey("awardDetail_exceptionCode.1067"));
                                        return false;
                                    }
                                }
                            }
                            //Case#3587 - End
                        }
                    }
                }
            }
            if( !piPresent ){
              /* if none of the investigators have been specified as PI then
                 show error message */
                log(coeusMessageResources.parseMessageKey(
                        "protoInvFrm_exceptionCode.1064"));
                return false;
            }
        }
        

        return true;
    }
    //2796 End
    
    
    //Added for Case#3587 - Multi Campus enchament - Start
    /*
     * Method to check logged in user has 'MODIFY_INST_PROPOSAL' right in proposa lead unit
     * @param leadUnitNumber
     * @return canModify
     */
    private boolean checkUserHasCreateIPRight(String leadUnitNumber){
        boolean hasRight = false;
        RequesterBean request = new RequesterBean();
        Hashtable htRightCheck = new Hashtable();
        htRightCheck.put(LEAD_UNIT_NUMBER,leadUnitNumber);
        htRightCheck.put(RIGHT_ID,CREATE_INST_PROPOSAL);
        request.setFunctionType(USER_HAS_RIGHT_IN_UNIT_LEVEL);
        request.setDataObject(htRightCheck);
        String connectTo = CoeusGuiConstants.CONNECTION_URL + INST_PROP_SERVLET;
        AppletServletCommunicator comm = new AppletServletCommunicator(connectTo, request);
        comm.send();
        ResponderBean response = comm.getResponse();
        if(response.isSuccessfulResponse()){
            hasRight = ((Boolean)response.getDataObject()).booleanValue();
        }
        return hasRight;
    }
    //Case#3587 - End

    private boolean checkUserHasCreateAwardRight(String leadUnitNumber) {
        boolean createRight = false;
         RequesterBean requesterBean = new RequesterBean();
         requesterBean.setFunctionType(CHECK_USER_HAS_CREATE_AWARD_RIGHT);
         requesterBean.setDataObject(leadUnitNumber);
         
         AppletServletCommunicator appletServletCommunicator = new
                 AppletServletCommunicator(CoeusGuiConstants.CONNECTION_URL + AWARD_SERVLET, requesterBean);
         appletServletCommunicator.send();
         ResponderBean responderBean = appletServletCommunicator.getResponse();
         
         if(responderBean != null) {
             if(responderBean.isSuccessfulResponse()) {
                 Boolean right = (Boolean) responderBean.getDataObject();
                 createRight = right.booleanValue();
             }
         }
         return createRight;
    }
    
    //COEUSDEV215 :Sync to Child Awards: Investigator Sync - inconsistent results and LOST AWARD NODE
    public InvestigatorBean getSelectedInvestigatorBean(){
        InvestigatorBean invBean = null;
        int selectedRow = investigatorForm.tblInvestigator.getSelectedRow();
        if( selectedRow != -1 ){
            invBean = investigatorTableModel.getInvestigatorBean(selectedRow);
        }
        return invBean;
    }
    //COEUSDEV215 : End
    
    //Added for COEUSQA-2037 : Software allows you to delete an investigator who is assigned credit in the credit split window - Start
    /*
     * Method to check investigatoe has Adds to hundred credit split value greater than '0'
     * @param personId
     * @return hasCreditSplit
     */
    private boolean checkInvHasAddsToHundredCreditSplit(String personId){
        boolean hasCreditSplit = false;
        String connectTo = CoeusGuiConstants.CONNECTION_URL + CoeusGuiConstants.USER_SERVLET;
        RequesterBean request = new RequesterBean();
        CoeusVector cvInvestigatorData = new CoeusVector();
        if(moduleName.equals(CoeusGuiConstants.AWARD_MODULE)){
            cvInvestigatorData.add(MODULE_CODE_INDEX,AWARD_MODULE_CODE);
        }else if(moduleName.equals(CoeusGuiConstants.INSTITUTE_PROPOSAL_MODULE)){
            cvInvestigatorData.add(MODULE_CODE_INDEX,INSTITUTE_PROPOSAL_MODULE_CODE);
        } 
        cvInvestigatorData.add(MODULE_ITEM_KEY_INDEX,moduleNumber);
        cvInvestigatorData.add(MODULE_ITEM_KEY_SEQUENCE_INDEX,new Integer(getSeqNo()));
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
        String connectTo = CoeusGuiConstants.CONNECTION_URL + CoeusGuiConstants.USER_SERVLET;
        RequesterBean request = new RequesterBean();
        CoeusVector cvInvUnitData = new CoeusVector();
        if(moduleName.equals(CoeusGuiConstants.AWARD_MODULE)){
            cvInvUnitData.add(MODULE_CODE_INDEX,AWARD_MODULE_CODE);
        }else if(moduleName.equals(CoeusGuiConstants.INSTITUTE_PROPOSAL_MODULE)){
            cvInvUnitData.add(MODULE_CODE_INDEX,INSTITUTE_PROPOSAL_MODULE_CODE);
        }
        cvInvUnitData.add(MODULE_ITEM_KEY_INDEX,moduleNumber);
        cvInvUnitData.add(MODULE_ITEM_KEY_SEQUENCE_INDEX,new Integer(getSeqNo()));
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
    /*
     * Method to open the credit split window
     */
    private void performCreditSplitAction(){
        InvestigatorCreditSplitController invCreditSplitController =
                new InvestigatorCreditSplitController(getFunctionType());
        invCreditSplitController.setFormData(getCreditSplitData());
        invCreditSplitController.display();
    }
    //COEUSQA-2037 : End
    
    // Added for COEUSQA-3642 : Issues with COEUSQA-1676 Credit Split Infrastructure project - Start
    /**
     * Method to delete investigator credit split along with unit credit credit split
     * @param personId 
     * @param invAcType 
     */
    private void deleteInvAndUnitCreditSplit(String personId, String invAcType){
        try{
            QueryEngine queryEngine = QueryEngine.getInstance();
            String queryKey = getModuleNumber() + getSeqNo();
            Equals eqPersonId = new Equals("personId",personId);
            And andPersonIdAcType = null;
            if(TypeConstants.INSERT_RECORD.equals(invAcType)){
                Equals eqAcType = new Equals("acType",TypeConstants.INSERT_RECORD);
                andPersonIdAcType = new And(eqAcType,eqPersonId);
                queryEngine.removeData(queryKey,CoeusConstants.INVESTIGATOR_CREDIT_SPLIT_KEY,andPersonIdAcType);
                queryEngine.removeData(queryKey,CoeusConstants.INVESTIGATOR_UNIT_CREDIT_SPLIT_KEY,andPersonIdAcType);
            }else{
                andPersonIdAcType = new And(CoeusVector.FILTER_ACTIVE_BEANS,eqPersonId);
                queryEngine.setUpdate(queryKey, CoeusConstants.INVESTIGATOR_CREDIT_SPLIT_KEY, 
                        InvestigatorCreditSplitBean.class,"acType",String.class,TypeConstants.DELETE_RECORD, andPersonIdAcType);
                queryEngine.setUpdate(queryKey, CoeusConstants.INVESTIGATOR_UNIT_CREDIT_SPLIT_KEY, 
                        InvestigatorCreditSplitBean.class,"acType",String.class,TypeConstants.DELETE_RECORD, andPersonIdAcType);
            }
        }catch(CoeusException coeusExp){
            coeusExp.printStackTrace();
        }
    }

    /**
     * Method to delete unit credit split for investigator
     * @param personId 
     * @param unitNo 
     * @param acType 
     */
    private void deleteUnitCreditSplitForInv(String personId, String unitNo, String acType) {
        try{
            QueryEngine queryEngine = QueryEngine.getInstance();
            String queryKey = getModuleNumber() + getSeqNo();
            Equals eqPersonId = new Equals("personId",personId);
            Equals eqUnitNo = new Equals("unitNumber",unitNo);
            And andPersonIdUnitAcType = new And(eqUnitNo,eqPersonId);
            
            And andPersonUnitAcType = null;
            if(TypeConstants.INSERT_RECORD.equals(acType)){
                Equals eqAcType = new Equals("acType",TypeConstants.INSERT_RECORD);
                andPersonUnitAcType = new And(eqAcType,andPersonIdUnitAcType);
                queryEngine.removeData(queryKey,CoeusConstants.INVESTIGATOR_UNIT_CREDIT_SPLIT_KEY,andPersonUnitAcType);
            }else{
                andPersonUnitAcType = new And( CoeusVector.FILTER_ACTIVE_BEANS,andPersonIdUnitAcType);
                queryEngine.setUpdate(queryKey, CoeusConstants.INVESTIGATOR_UNIT_CREDIT_SPLIT_KEY, 
                        InvestigatorCreditSplitBean.class,"acType",String.class,TypeConstants.DELETE_RECORD, andPersonUnitAcType);
            }
        }catch(CoeusException coeusExp){
            coeusExp.printStackTrace();
        }
    }
    
    /**
     * Method to add credit split details for investigator and unit 
     * @param personId 
     * @param personName 
     * @param primaryInv 
     * @param unitNumber 
     */
    private void addInvCreditOrUnitSplitDetails(String personId, String personName,boolean primaryInv, String unitNumber){
        try {
            String queryKey = getModuleNumber() + getSeqNo();
            QueryEngine queryEngine = QueryEngine.getInstance();
            CoeusVector cvInvCreditSplitType = queryEngine.getDetails(queryKey, CoeusConstants.INVESTIGATOR_CREDIT_TYPES_KEY);
            CoeusVector cvAwardCreditSplit = null;
            if(CoeusGuiConstants.EMPTY_STRING.equals(unitNumber)){
                cvAwardCreditSplit = queryEngine.getDetails(queryKey, CoeusConstants.INVESTIGATOR_CREDIT_SPLIT_KEY);
            }else{
                cvAwardCreditSplit = queryEngine.getDetails(queryKey, CoeusConstants.INVESTIGATOR_UNIT_CREDIT_SPLIT_KEY);
            }
            if(cvInvCreditSplitType != null && !cvInvCreditSplitType.isEmpty()){
                InvestigatorCreditSplitBean investigatorCreditSplitBean  = null;
                for(Object invCreditSplitType : cvInvCreditSplitType){
                    InvCreditTypeBean invCreditTypeBean = (InvCreditTypeBean)invCreditSplitType;
                    investigatorCreditSplitBean = new InvestigatorCreditSplitBean();
                    investigatorCreditSplitBean.setModuleNumber(getModuleNumber());
                    investigatorCreditSplitBean.setSequenceNo(Integer.parseInt(getSeqNo()));
                    investigatorCreditSplitBean.setCredit(0.0);
                    investigatorCreditSplitBean.setInvCreditTypeCode(invCreditTypeBean.getInvCreditTypeCode());
                    investigatorCreditSplitBean.setPersonId(personId);
                    investigatorCreditSplitBean.setPersonName(personName);
                    investigatorCreditSplitBean.setPiFlag(primaryInv);
                    if(!CoeusGuiConstants.EMPTY_STRING.equals(unitNumber)){
                        investigatorCreditSplitBean.setUnitNumber(unitNumber);
                    }
                    investigatorCreditSplitBean.setAcType(TypeConstants.INSERT_RECORD);
                    cvAwardCreditSplit.add(investigatorCreditSplitBean);
                }
            }
            Hashtable htDataCollection = queryEngine.getDataCollection(queryKey);
            if(CoeusGuiConstants.EMPTY_STRING.equals(unitNumber)){
                htDataCollection.put(CoeusConstants.INVESTIGATOR_CREDIT_SPLIT_KEY, cvAwardCreditSplit == null ? new CoeusVector() : cvAwardCreditSplit);
            }else{
                htDataCollection.put(CoeusConstants.INVESTIGATOR_UNIT_CREDIT_SPLIT_KEY, cvAwardCreditSplit == null ? new CoeusVector() : cvAwardCreditSplit);
            }
            queryEngine.removeDataCollection(queryKey);
            queryEngine.addDataCollection(queryKey,htDataCollection);
        } catch (CoeusException ex) {
            ex.printStackTrace();
        }
    }
    // Added for COEUSQA-3642 : Issues with COEUSQA-1676 Credit Split Infrastructure project - End    
}
