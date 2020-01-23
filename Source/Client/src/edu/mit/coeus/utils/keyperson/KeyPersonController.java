/*
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */


/*
 * KeyPersonController.java
 *
 * Created on January 13, 2009, 7:37 PM
 *
 */

package edu.mit.coeus.utils.keyperson;

import edu.mit.coeus.departmental.gui.PersonDetailForm;
import edu.mit.coeus.exception.CoeusUIException;
import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.gui.event.Controller;
import edu.mit.coeus.bean.KeyPersonBean;
import edu.mit.coeus.bean.KeyPersonUnitBean;
import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.propdev.bean.ProposalKPUnitFormBean;
import edu.mit.coeus.propdev.bean.ProposalKeyPersonFormBean;
import edu.mit.coeus.rolodexmaint.gui.RolodexMaintenanceDetailForm;
import edu.mit.coeus.search.gui.CoeusSearch;
import edu.mit.coeus.unit.gui.UnitDetailForm;
import edu.mit.coeus.utils.AppletServletCommunicator;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.EmptyHeaderRenderer;
import edu.mit.coeus.utils.IconRenderer;
import edu.mit.coeus.utils.ObjectCloner;
import edu.mit.coeus.utils.ScreenFocusTraversalPolicy;
import edu.mit.coeus.utils.TypeConstants;
import edu.mit.coeus.utils.investigator.InvestigatorTableModel;
import edu.mit.coeus.utils.query.Equals;
import edu.mit.coeus.utils.query.NotEquals;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.DefaultListSelectionModel;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;


// JM 10-03-2012 needed to get home unit for key person
import edu.mit.coeus.exception.CoeusClientException;
// JM END

/* JM 4-8-2016 for external persons */
import edu.vanderbilt.coeus.gui.PersonTableCellRenderer;
/* JM END */
/**
 *
 * @author Sreenath
 */
public class KeyPersonController extends Controller
        implements ActionListener, ListSelectionListener,TypeConstants{
    KeyPersonUnitTableModel unitTableModel;
    UnitCellEditor unitCellEditor;
    private UnitTableCellRenderer unitTableCellRenderer;
    private KeyPersonTableModel keyPersonTableModel;
    private KeyPersonTableCellRenderer keyPersonTableCellRenderer;
    protected KeyPersonForm keyPersonForm;
    private KeyPersonCellEditor keyPersonCellEditor;
    private CoeusVector cvKeypersons;
    private CoeusVector cvUnit;
    private KeyPersonBean keyPersonBean;
    // KeyPersonTableModel unitTableModel;
    private CoeusMessageResources coeusMessageResources;
    //  UnitCellEditor unitCellEditor;
        private static final char GET_PROP_UNIT_ADMIN_TYPE_DATA = 'Q';
        private CoeusVector cvAdministratorDetails;
         private static final String INST_PROP_SERVLET = "/InstituteProposalMaintenanceServlet";
       private int prevInvRow,prevUnitRow;
         private HashMap hmDeletedUnits = new HashMap();
    // Represents the column header names for the investigator table
    private static final int KEY_PERSON_HAND_ICON_COLUMN = 0;
    private static final int KEY_PERSON_NAME_COLUMN = 1;
    private static final int KEY_PERSON_ROLE_COLUMN = 2;
    // JM 6-14-2012 reordered columns
    private static final int KEY_PERSON_FACULTY_COLUMN = 3;
    private static final int KEY_PERSON_EFFORT_COLUMN = 4;
    // JM END
    private static final int KEY_PERSON_ACAD_YEAR_COLUMN=5;
    private static final int KEY_PERSON_SUM_EFFORT_COLUMN=6;
    private static final int KEY_PERSON_CAL_YEAR_COLUMN =7;
    private static final int KEY_PERSON_ID_COLUMN = 8;
    private static final int KEY_PERSON_EMPLOYEE_FLAG_COLUMN = 9;
    //ppc change for keyperson starts
    private static final int KEY_PERSON_CERTIFY_FLAG_COLUMN = 10;
    //ppc change for keyperson ends
    
    // JM 6-13-2012 variable to hold effort column widths
    private static final int EFFORT_COLUMN_WIDTH = 78;
    private static final int POINTER_COLUMN_WIDTH = 30;
    private static final int NAME_COLUMN_WIDTH = 280; // was 300
    private static final int ROLE_COLUMN_WIDTH = 150; // was 180; 133
    private static final int FACULTY_COLUMN_WIDTH = 60; 
    private static final int UNIT_NAME_COLUMN_WIDTH = 530; // was 560
    // JM END
    
    private Vector deletedKeyPersons = new Vector();
   //  private int prevInvRow,prevUnitRow;
    private boolean dataChanged;
    private int keyPersonTabIndex;
    private String moduleNumber;
    private String moduleName;
    private String sequenceNumber;

    private static final int UNIT_HAND_COLUMN = 0;
    private static final int UNIT_LEAD_FLAG_COLUMN = 3;
    private static final int UNIT_NUMBER_COLUMN = 1;
    private static final int UNIT_NAME_COLUMN = 2;
    private static final int UNIT_OSP_ADMIN_COLUMN = 4;
     private static final String GET_PARAMETER_VALUE = "GET_PARAMETER_VALUE";
    private Vector deletedInvestigators = new Vector();

   // private HashMap hmDeletedUnits = new HashMap();
    //To check whether the data or screen is modified

    // JM 7-30-2012 added to get home unit for key person
    private static final String PROP_DEV_SERVLET = "/ProposalActionServlet";
    private static final char GET_HOME_UNIT_FOR_KP = 'q';
    //private static final char REFRESH_KEY_PERSON_DATA = 'r';
    // JM END

    public void updateInvestigator(String personId, KeyPersonUnitBean unitBean) {
        CoeusVector cvSelectedInv = cvKeypersons.filter(new Equals("personId",personId));
         KeyPersonBean keyPrsnBean = (KeyPersonBean)cvSelectedInv.get(0);
//        String oldUnitNumber = unitBean.getUnitNumber();
        String acType = unitBean.getAcType();
//        if(!(null ==  oldUnitNumber || EMPTY_STRING.equals(oldUnitNumber))){
        if( null == acType || UPDATE_RECORD.equals(acType) ){
            unitBean.setAcType(DELETE_RECORD);
            CoeusVector delUnits = new CoeusVector();
            if( hmDeletedUnits.containsKey(keyPrsnBean.getPersonId())) {
                delUnits = (CoeusVector)hmDeletedUnits.get(keyPrsnBean.getPersonId());
            }
            delUnits.add(unitBean);
            hmDeletedUnits.put(keyPrsnBean.getPersonId(),delUnits);
        }
//        }
        dataChanged = true;
        keyPrsnBean.setKeyPersonsUnits(unitTableModel.getData());
        String invAcType = keyPrsnBean.getAcType();
        if( null == invAcType ) {
            keyPrsnBean.setAcType(UPDATE_RECORD);
        }
    }

     public void updateUnits(){

        int perSelRow = keyPersonForm.tblInvestigator.getSelectedRow();
        if( perSelRow != -1 ) {
            KeyPersonBean keyPrsnBean = (KeyPersonBean)cvKeypersons.get( perSelRow );
            keyPrsnBean.setKeyPersonsUnits(unitTableModel.getData());
            String invAcType = keyPrsnBean.getAcType();
            if( null == invAcType ) {
                keyPrsnBean.setAcType(UPDATE_RECORD);
            }
            dataChanged = true;
        }
    }

     public String getParameterValue(){
        final String connectTo = CoeusGuiConstants.CONNECTION_URL+ "/coeusFunctionsServlet";
        final String PARAMETER = "ENABLE_KEYPERSON_UNITS";
        String value = CoeusGuiConstants.EMPTY_STRING;
        RequesterBean requester = new RequesterBean();
        ResponderBean responder = null;
        requester.setDataObject(GET_PARAMETER_VALUE);
        Vector vecParameter = new Vector();
        vecParameter.add(PARAMETER);
        requester.setDataObjects(vecParameter);
        AppletServletCommunicator comm
        = new AppletServletCommunicator(connectTo, requester);
        comm.send();
        responder = comm.getResponse();
        if(responder.isSuccessfulResponse()){
            value =(String) responder.getDataObject();
        }
        return value;
    }
    //new unit addtion ends

    /** 
     * Creates a new instance of KeyPersonController
     */
    public KeyPersonController() {
        cvKeypersons = new CoeusVector();
        coeusMessageResources = CoeusMessageResources.getInstance();
        registerComponents();
        setTableKeyTraversal();
    }
    
    public KeyPersonController(String moduleNo, String moduleName) {
        this();
        this.moduleNumber = moduleNo;
        this.moduleName = moduleName;
    }
    
    /**
     * To display the GUI
     * @return void
     */
    public void display() {
    //adding the units...
        if (prevInvRow != -1 && keyPersonForm.tblInvestigator.getRowCount() > prevInvRow ) {
            keyPersonForm.tblInvestigator.setRowSelectionInterval(prevInvRow,prevInvRow);
        }else if (keyPersonForm.tblInvestigator.getRowCount() > 0) {
            keyPersonForm.tblInvestigator.setRowSelectionInterval(0,0);
        }
        if (prevUnitRow != -1 && keyPersonForm.tblUnits.getRowCount() > prevUnitRow ) {
            keyPersonForm.tblUnits.setRowSelectionInterval(prevUnitRow,prevUnitRow);
        }else if (keyPersonForm.tblUnits.getRowCount() > 0) {
            keyPersonForm.tblUnits.setRowSelectionInterval(0,0);
        }
    }
    
    /**
     * To format the fields
     * @return void
     */
    public void formatFields(){
        boolean editable = getFunctionType() != DISPLAY_MODE;
        keyPersonForm.btnFindRolodex.setEnabled( editable );
        keyPersonForm.btnDelete.setEnabled( editable );
        keyPersonForm.btnAdd.setEnabled( editable );
        keyPersonForm.btnFindPerson.setEnabled( editable );
        keyPersonForm.btnFindUnit.setEnabled( editable );
        keyPersonForm.btnAddUnit.setEnabled( editable );
        keyPersonForm.btnDeleteUnit.setEnabled( editable );
        keyPersonTableModel.setEditable(editable );
        unitTableModel.setEditable( editable );

        if( keyPersonForm.tblInvestigator.getRowCount() <= 0 ){
            keyPersonForm.btnDelete.setEnabled( false );
        }else{
            keyPersonForm.tblInvestigator.setRowSelectionInterval(0,0);
        }
        keyPersonTableCellRenderer.setFunctionType(getFunctionType());
        unitTableCellRenderer.setFunctionType(getFunctionType());
        if(getFunctionType() == CoeusGuiConstants.DISPLAY_MODE){
            Color bgListColor = (java.awt.Color)javax.swing.UIManager.getDefaults().get("Panel.background");
            keyPersonForm.tblInvestigator.setBackground(bgListColor);
            keyPersonForm.tblInvestigator.setSelectionBackground(bgListColor );
            keyPersonForm.tblInvestigator.setSelectionForeground(Color.BLACK);
            keyPersonForm.tblUnits.setBackground(bgListColor);
            keyPersonForm.tblUnits.setSelectionBackground(bgListColor );
            keyPersonForm.tblUnits.setSelectionForeground(Color.BLACK);
        } else{
            keyPersonForm.tblInvestigator.setBackground(Color.white);
            keyPersonForm.tblInvestigator.setSelectionBackground(Color.white);
            keyPersonForm.tblInvestigator.setSelectionForeground(Color.black);
            keyPersonForm.tblUnits.setBackground(Color.white);
            keyPersonForm.tblUnits.setSelectionBackground(Color.white );
            keyPersonForm.tblUnits.setSelectionForeground(Color.black);
        }
        
        /* JM 9-4-2015 added to set font color based on status; 2-2-2016 added highlighting for external persons;
         * 	2-2-2016 added new method to allow multiple options */
        //edu.vanderbilt.coeus.gui.CustomTableCellRenderer renderer = 
    	//		new edu.vanderbilt.coeus.gui.CustomTableCellRenderer(KeyPersonTableModel.KEY_PERSON_STATUS_COLUMN,"I",Color.RED, true, false, false, getFunctionType());
    	PersonTableCellRenderer renderer = 
    			new PersonTableCellRenderer(KeyPersonTableModel.KEY_PERSON_STATUS_COLUMN,KeyPersonTableModel.KEY_PERSON_IS_EXTERNAL_PERSON,true,false,false,getFunctionType());
    	keyPersonForm.tblInvestigator.getColumnModel().getColumn(KEY_PERSON_NAME_COLUMN).setCellRenderer(renderer);
    	/* JM END */
    }
    
    public void updateKeyperson(String personId, KeyPersonUnitBean unitBean) {
        CoeusVector cvSelectedInv = cvKeypersons.filter(new Equals("personId",personId));
        KeyPersonBean invBean = (KeyPersonBean)cvSelectedInv.get(0);
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
        invBean.setKeyPersonsUnits(unitTableModel.getData());
        String invAcType = invBean.getAcType();
        if( null == invAcType ) {
            invBean.setAcType(UPDATE_RECORD);
        }
    }
    
    /**
     *
     * To get the Key Person Form.
     * @return Component
     */
    public Component getControlledUI() {

        return keyPersonForm;
    }

    /**
     * To get Key Person Data from the Key Person Table
     * @return Object
     */
    public Vector getFormData() {
        CoeusVector keyPersonData = keyPersonTableModel.getData();
        CoeusVector dataToSend = new CoeusVector();
        if( deletedKeyPersons.size() > 0 ) {
            int delCount = deletedKeyPersons.size();
            for( int delIndx = 0; delIndx < delCount; delIndx++ ) {
                KeyPersonBean delKeyPersonBean = (KeyPersonBean) deletedKeyPersons.get(delIndx);
                                CoeusVector cvDelUnits = delKeyPersonBean.getKeyPersonsUnits();
                if( cvDelUnits != null ) {
                    int delUnitCount = cvDelUnits.size();
                    for( int delUnitIndx = 0; delUnitIndx < delUnitCount; delUnitIndx++){
                     KeyPersonUnitBean    delUnitBean =
                                (KeyPersonUnitBean)cvDelUnits.get(delUnitIndx);
                        delUnitBean.setAcType(DELETE_RECORD);
                        cvDelUnits.set(delUnitIndx, delUnitBean);
                    }
                }
                dataToSend.add(delKeyPersonBean);
            }
        }
        if( keyPersonData != null ) {
            int keyPerSize = keyPersonData.size();
            for( int keyPerIndex = 0; keyPerIndex < keyPerSize; keyPerIndex++) {
                KeyPersonBean kPersonBean = (KeyPersonBean)keyPersonData.get(keyPerIndex);
                if( kPersonBean.getPersonId() != null && kPersonBean.getPersonId().trim().length() > 0 ) {
                      CoeusVector cvInvUnits = new CoeusVector();
                    if ( hmDeletedUnits.containsKey(kPersonBean.getPersonId()) ) {
                        cvInvUnits.addAll( (CoeusVector)hmDeletedUnits.get(kPersonBean.getPersonId()));
                    }
                    //Added for COEUSQA-2383 - Two Lead Unit appear for an Award start
                    //It is required to remove all deleted entry from database
                    if( deletedInvestigators.size() > 0 ) {
                        for( int delIndx = 0; delIndx < deletedInvestigators.size(); delIndx++ ) {
                             KeyPersonBean delkeyPrsnBean = (KeyPersonBean) deletedInvestigators.get(delIndx);
                             if ( hmDeletedUnits.containsKey(delkeyPrsnBean.getPersonId()) ) {
                                cvInvUnits.addAll( (CoeusVector)hmDeletedUnits.get(delkeyPrsnBean.getPersonId()));
                             }
                        }
                    }
                    //Added for COEUSQA-2383 Two Lead Unit appear for an Award end
                    CoeusVector units = kPersonBean.getKeyPersonsUnits();
                    if( units != null ) {
                        CoeusVector notNullUnits = units.filter(new NotEquals("unitNumber",null));
                        cvInvUnits.addAll(notNullUnits);
                    }
                    kPersonBean.setKeyPersonsUnits(cvInvUnits);

                    dataToSend.add(kPersonBean);
                }
            }
        }
        if( dataToSend.size() > 0 ){
            return dataToSend;
        }
                    return null;
    }
    
//        public void updateUnits(){
//        int perSelRow = keyPersonForm.tblInvestigator.getSelectedRow();
//        if( perSelRow != -1 ) {
//            ProposalKeyPersonFormBean invBean = (ProposalKeyPersonFormBean)cvKeypersons.get(perSelRow );
//            invBean.setKpUnits(unitTableModel.getData());
//            String invAcType = invBean.getAcType();
//            if( null == invAcType ) {
//                invBean.setAcType(UPDATE_RECORD);
//            }
//            dataChanged = true;
//        }
//    }
    
    /**
     * Registering components
     * @return void
     */
    public void registerComponents() {
        keyPersonForm = new KeyPersonForm();
         
        keyPersonForm.tblInvestigator.setBackground(Color.LIGHT_GRAY);
        if(getFunctionType()!=DISPLAY_MODE){
            keyPersonForm.btnAdd.addActionListener(this);
            keyPersonForm.btnDelete.addActionListener(this);
            keyPersonForm.btnFindPerson.addActionListener(this);
            keyPersonForm.btnFindRolodex.addActionListener(this);
            keyPersonForm.btnAddUnit.addActionListener(this);
            keyPersonForm.btnDeleteUnit.addActionListener(this);
            keyPersonForm.btnFindUnit.addActionListener(this);

            Component[] comp = {keyPersonForm.tblInvestigator,keyPersonForm.tblUnits,
            keyPersonForm.btnAdd,keyPersonForm.btnDelete,keyPersonForm.btnFindPerson,
            keyPersonForm.btnFindRolodex,keyPersonForm.btnAddUnit,keyPersonForm.btnDeleteUnit,
            keyPersonForm.btnFindUnit,
            };
            ScreenFocusTraversalPolicy traversalPolicy = new ScreenFocusTraversalPolicy(comp);
            keyPersonForm.setFocusTraversalPolicy(traversalPolicy);
            keyPersonForm.setFocusCycleRoot(true);
        }
        
        keyPersonForm.tblInvestigator.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        keyPersonForm.tblInvestigator.setSurrendersFocusOnKeystroke(true);
        keyPersonForm.tblUnits.setSurrendersFocusOnKeystroke(true);
        keyPersonForm.tblUnits.addMouseListener(new UnitDisplayAdapter());
        keyPersonForm.tblInvestigator.getSelectionModel().addListSelectionListener(this);
        keyPersonForm.tblInvestigator.addMouseListener(new KeyPersonDetailsAdapter());
        keyPersonForm.tblInvestigator.getTableHeader().setPreferredSize(new Dimension(0, 30));
        
        unitTableModel = new KeyPersonUnitTableModel();
        keyPersonTableModel = new KeyPersonTableModel();
        keyPersonTableModel.setController(this);
        unitTableModel.setController(this);
        keyPersonForm.tblUnits.getSelectionModel().addListSelectionListener(this);
        keyPersonForm.tblInvestigator.setModel(keyPersonTableModel);
        keyPersonForm.tblUnits.setModel(unitTableModel);
        unitCellEditor = new UnitCellEditor();
        unitCellEditor.setUnitDetailsListener(new UnitDisplayAdapter());
        unitTableCellRenderer = new UnitTableCellRenderer();
        keyPersonCellEditor = new KeyPersonCellEditor();
        keyPersonCellEditor.setViewKeyPersonDetailsListener(new KeyPersonDetailsAdapter());
//        investigatorCellEditor.setController(this);
//        unitCellEditor.setController(this);
        keyPersonTableCellRenderer = new KeyPersonTableCellRenderer();
        keyPersonTableCellRenderer.setFunctionType(getFunctionType());
        unitTableCellRenderer.setFunctionType(getFunctionType());

        showUnit();
    }
    
    /**
     * To create columns for the Key Person Table
     * @return void
     */
    public void setColumnData() {
       
        JTableHeader tableHeader = keyPersonForm.tblInvestigator.getTableHeader();
        tableHeader.setReorderingAllowed(false);
        tableHeader.setFont(CoeusFontFactory.getLabelFont());
        
        keyPersonForm.tblInvestigator.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        // JM 6-18-2012 change row height from 22 to 24 to be easier to read
        keyPersonForm.tblInvestigator.setRowHeight(24);
        // JM END
        keyPersonForm.tblInvestigator.setShowHorizontalLines(false);
        keyPersonForm.tblInvestigator.setShowVerticalLines(false);
        keyPersonForm.tblInvestigator.setOpaque(false);
        keyPersonForm.tblInvestigator.setSelectionMode(
                DefaultListSelectionModel.SINGLE_SELECTION);
        
        TableColumn column = keyPersonForm.tblInvestigator.getColumnModel().getColumn(KEY_PERSON_HAND_ICON_COLUMN);
        // JM 6-14-2012 set width to static variable
        column.setMaxWidth(POINTER_COLUMN_WIDTH);
        column.setMinWidth(POINTER_COLUMN_WIDTH);
        column.setPreferredWidth(POINTER_COLUMN_WIDTH);
        // JM END
        column.setResizable(true);
        column.setCellRenderer(new IconRenderer());
        column.setHeaderRenderer(new EmptyHeaderRenderer());
        int width=340;
        // JM 6-14-2012 set width to static variable
        if(moduleName.equalsIgnoreCase(CoeusGuiConstants.PROPOSAL_MODULE)){width=NAME_COLUMN_WIDTH;}
        // JM END
        column = keyPersonForm.tblInvestigator.getColumnModel().getColumn(KEY_PERSON_NAME_COLUMN);
        // JM 6-19-2012 width not getting set by above statement, so hard-coding
        //column.setMinWidth(width);
        //column.setPreferredWidth(width);
        column.setMinWidth(NAME_COLUMN_WIDTH);
        column.setPreferredWidth(NAME_COLUMN_WIDTH);
        // JM END
        column.setResizable(true);
        column.setCellEditor(keyPersonCellEditor);
        
        /* JM 4-8-2016 inactive and external persons highlighting */
    	PersonTableCellRenderer renderer = 
    			new PersonTableCellRenderer(KeyPersonTableModel.KEY_PERSON_STATUS_COLUMN,KeyPersonTableModel.KEY_PERSON_IS_EXTERNAL_PERSON,true,false,false,getFunctionType());
    	column.setCellRenderer(renderer);
    	/* JM END */
        
        column = keyPersonForm.tblInvestigator.getColumnModel().getColumn(KEY_PERSON_ROLE_COLUMN);
        column.setMinWidth(ROLE_COLUMN_WIDTH);
        column.setPreferredWidth(ROLE_COLUMN_WIDTH);
        column.setResizable(true);
        column.setCellEditor(keyPersonCellEditor);
        column.setCellRenderer(keyPersonTableCellRenderer);
        
        column = keyPersonForm.tblInvestigator.getColumnModel().getColumn(KEY_PERSON_EFFORT_COLUMN);
        // JM 6-14-2012 set width to static variable
        column.setMaxWidth(EFFORT_COLUMN_WIDTH);
        column.setMinWidth(EFFORT_COLUMN_WIDTH);
        column.setPreferredWidth(EFFORT_COLUMN_WIDTH);
        // JM END
        column.setResizable(true);
        column.setCellEditor(keyPersonCellEditor);
        column.setCellRenderer(keyPersonTableCellRenderer);
        
        
        column = keyPersonForm.tblInvestigator.getColumnModel().getColumn(KEY_PERSON_FACULTY_COLUMN);
        // JM 6-14-2012 set width to static variable
        column.setMaxWidth(FACULTY_COLUMN_WIDTH);
        column.setMinWidth(FACULTY_COLUMN_WIDTH);
        column.setPreferredWidth(FACULTY_COLUMN_WIDTH);
        // JM END
        column.setResizable(true);
        column.setCellEditor(keyPersonCellEditor);
        column.setCellRenderer(keyPersonTableCellRenderer);
        
        
        column = keyPersonForm.tblInvestigator.getColumnModel().getColumn(KEY_PERSON_ACAD_YEAR_COLUMN);
        // JM 6-13-2012 updated width from 0 so that this column will display
        column.setMaxWidth(EFFORT_COLUMN_WIDTH);
        column.setMinWidth(EFFORT_COLUMN_WIDTH);
        column.setPreferredWidth(EFFORT_COLUMN_WIDTH);
        // JM END
        column.setResizable(true);
        column.setCellEditor(keyPersonCellEditor);
        column.setCellRenderer(keyPersonTableCellRenderer);
        
        column = keyPersonForm.tblInvestigator.getColumnModel().getColumn(KEY_PERSON_SUM_EFFORT_COLUMN);
        // JM 6-13-2012 updated width from 0 so that this column will display
        column.setMaxWidth(EFFORT_COLUMN_WIDTH);
        column.setMinWidth(EFFORT_COLUMN_WIDTH);
        column.setPreferredWidth(EFFORT_COLUMN_WIDTH);
        // JM END
        column.setResizable(true);
        column.setCellEditor(keyPersonCellEditor);
        column.setCellRenderer(keyPersonTableCellRenderer);
        
        column = keyPersonForm.tblInvestigator.getColumnModel().getColumn(KEY_PERSON_CAL_YEAR_COLUMN);
        // JM 6-13-2012 updated width from 0 so that this column will display
        column.setMaxWidth(EFFORT_COLUMN_WIDTH);
        column.setMinWidth(EFFORT_COLUMN_WIDTH);
        column.setPreferredWidth(EFFORT_COLUMN_WIDTH);
        // JM END
        column.setResizable(true);
        column.setCellEditor(keyPersonCellEditor);
        column.setCellRenderer(keyPersonTableCellRenderer);
        
        column = keyPersonForm.tblInvestigator.getColumnModel().getColumn(KEY_PERSON_ID_COLUMN);
        column.setMaxWidth(0);
        column.setMinWidth(0);
        column.setPreferredWidth(0);
        column.setResizable(true);
        column.setCellEditor(keyPersonCellEditor);
        column.setCellRenderer(keyPersonTableCellRenderer);
        
        column = keyPersonForm.tblInvestigator.getColumnModel().getColumn(KEY_PERSON_EMPLOYEE_FLAG_COLUMN);
        column.setMaxWidth(0);
        column.setMinWidth(0);
        column.setPreferredWidth(0);
        column.setResizable(true);
        column.setCellEditor(keyPersonCellEditor);
        column.setCellRenderer(keyPersonTableCellRenderer);
        //ppc change for keyperson starts
        width=0;
        if(moduleName.equalsIgnoreCase(CoeusGuiConstants.PROPOSAL_MODULE)){width=60;}
        ImageRendererPPC imageRendererPPC=new ImageRendererPPC();
        column = keyPersonForm.tblInvestigator.getColumnModel().getColumn(KEY_PERSON_CERTIFY_FLAG_COLUMN);
        column.setMaxWidth(width);
        column.setMinWidth(width);
        column.setPreferredWidth(width);
        column.setResizable(true);
        column.setCellRenderer(imageRendererPPC);
        
        /* JM 9-9-2015 hidden status column */
        column = keyPersonForm.tblInvestigator.getColumnModel().getColumn(KeyPersonTableModel.KEY_PERSON_STATUS_COLUMN);
        column.setMaxWidth(0);
        column.setMinWidth(0);
        column.setPreferredWidth(0);
        column.setResizable(false);
        /* JM END */
        
        /* JM 2-11-2016 hidden IS_EXTERNAL_PERSON column */
        column = keyPersonForm.tblInvestigator.getColumnModel().getColumn(KeyPersonTableModel.KEY_PERSON_IS_EXTERNAL_PERSON);
        column.setMaxWidth(0);
        column.setMinWidth(0);
        column.setPreferredWidth(0);
        column.setResizable(false);
        /* JM END */
        
       
    //ppc change for keyperson ends

          // Setting the table header, column width, renderer and editor for the
        // display unit details header
        JTableHeader unitHeader = keyPersonForm.tblUnits.getTableHeader();
        //unitHeader.addMouseListener(new ColumnHeaderListener());
        unitHeader.setReorderingAllowed(false);
        unitHeader.setFont(CoeusFontFactory.getLabelFont());

        keyPersonForm.tblUnits.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        keyPersonForm.tblUnits.setRowHeight(22);

        keyPersonForm.tblUnits.setShowHorizontalLines(false);
        keyPersonForm.tblUnits.setShowVerticalLines(false);
        keyPersonForm.tblUnits.setOpaque(false);
        keyPersonForm.tblUnits.setSelectionMode(
                DefaultListSelectionModel.SINGLE_SELECTION);

        TableColumn unitColumn = keyPersonForm.tblUnits.getColumnModel().getColumn(UNIT_HAND_COLUMN);
        // JM 6-14-2012 set width to static variable
        unitColumn.setMaxWidth(POINTER_COLUMN_WIDTH);
        unitColumn.setMinWidth(POINTER_COLUMN_WIDTH);
        unitColumn.setPreferredWidth(POINTER_COLUMN_WIDTH);
        // JM END
        unitColumn.setResizable(true);
        unitColumn.setCellRenderer(new IconRenderer());
        unitColumn.setHeaderRenderer(new EmptyHeaderRenderer());

//        unitColumn = keyPersonForm.tblUnits.getColumnModel().getColumn(UNIT_LEAD_FLAG_COLUMN);
//        unitColumn.setMaxWidth(45);
//        unitColumn.setMinWidth(45);
//        unitColumn.setPreferredWidth(45);
//        unitColumn.setResizable(true);
//        unitColumn.setCellRenderer(unitTableCellRenderer);
//        unitColumn.setCellEditor(unitCellEditor);

        /* JM 2-1-2016 updated unit number col width */
        unitColumn = keyPersonForm.tblUnits.getColumnModel().getColumn(UNIT_NUMBER_COLUMN);
        unitColumn.setMaxWidth(NAME_COLUMN_WIDTH);
        unitColumn.setMinWidth(NAME_COLUMN_WIDTH);
        unitColumn.setPreferredWidth(NAME_COLUMN_WIDTH);
        unitColumn.setResizable(true);
        unitColumn.setCellRenderer(unitTableCellRenderer);
        unitColumn.setCellEditor(unitCellEditor);

        unitColumn = keyPersonForm.tblUnits.getColumnModel().getColumn(UNIT_NAME_COLUMN);
        // JM 6-18-2012 set width to static variable
        unitColumn.setMinWidth(UNIT_NAME_COLUMN_WIDTH);
        unitColumn.setMaxWidth(UNIT_NAME_COLUMN_WIDTH);
        unitColumn.setPreferredWidth(UNIT_NAME_COLUMN_WIDTH);
        // JM END
        unitColumn.setResizable(true);
        unitColumn.setCellRenderer(unitTableCellRenderer);
        unitColumn.setCellEditor(unitCellEditor);

//        unitColumn = keyPersonForm.tblUnits.getColumnModel().getColumn(UNIT_OSP_ADMIN_COLUMN);
//        unitColumn.setMinWidth(215);
//        unitColumn.setPreferredWidth(215);
//        unitColumn.setResizable(true);
//        unitColumn.setCellRenderer(unitTableCellRenderer);
//        unitColumn.setCellEditor(unitCellEditor);
    }

    /**
     * save form data
     * @return void
     */
    public void saveFormData() {
    }
    
    /**
     * Sets the Key Person data to the Key person Table.
     * @return void
     */
    public void setFormData(Object keyPersonData) {
        prevInvRow = keyPersonForm.tblInvestigator.getSelectedRow();
        prevUnitRow = keyPersonForm.tblUnits.getSelectedRow();
        cvKeypersons = (CoeusVector) keyPersonData;
        keyPersonTableModel.setData(cvKeypersons);

        if(cvKeypersons != null && cvKeypersons.size() > 0){
            for (int index =0; index < cvKeypersons.size(); index ++){
                keyPersonBean = (KeyPersonBean)cvKeypersons.get(index);
                cvUnit = keyPersonBean.getKeyPersonsUnits();
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
        deletedKeyPersons.clear();
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
            if (source.equals(keyPersonForm.tblInvestigator.getSelectionModel())) {
                selectedRow = keyPersonForm.tblInvestigator.getSelectedRow();
                if (selectedRow != -1) {
                    keyPersonBean= (KeyPersonBean)cvKeypersons.get(selectedRow);
                    cvUnitsData = keyPersonBean.getKeyPersonsUnits();
                    unitCellEditor.cancelCellEditing();
                    //unitTableModel.setCellEditable(keyPersonBean.isPrincipalInvestigatorFlag());
                    unitTableModel.setData(cvUnitsData);
                    unitTableModel.fireTableDataChanged();
                    if( unitTableModel.getRowCount() > 0 ){
                      keyPersonForm.tblUnits.setRowSelectionInterval(0,0);
                    }
                    keyPersonCellEditor.cancelCellEditing();
                    int selColumn = keyPersonForm.tblInvestigator.getSelectedColumn();
                    if( selColumn == KEY_PERSON_NAME_COLUMN ||
                            selColumn == KEY_PERSON_EFFORT_COLUMN ||
                            selColumn == KEY_PERSON_ROLE_COLUMN ||
                            selColumn == KEY_PERSON_ACAD_YEAR_COLUMN ||
                            selColumn == KEY_PERSON_SUM_EFFORT_COLUMN ||
                            selColumn == KEY_PERSON_CAL_YEAR_COLUMN ) {
                        keyPersonForm.tblInvestigator.editCellAt(selectedRow,selColumn);
                        java.awt.Component comp = keyPersonForm.tblInvestigator.getEditorComponent();
                        if( comp != null ) {
                            comp.requestFocusInWindow();
                        }
                    }
                }
            }            else if (source.equals(keyPersonForm.tblUnits.getSelectionModel())) {
                selectedRow = keyPersonForm.tblUnits.getSelectedRow();
                if (selectedRow != -1) {
                    unitCellEditor.cancelCellEditing();
                    int selColumn = keyPersonForm.tblUnits.getSelectedColumn();
                    if( selColumn == UNIT_NUMBER_COLUMN ){
                        keyPersonForm.tblUnits.editCellAt(selectedRow,selColumn);
                        java.awt.Component comp = keyPersonForm.tblUnits.getEditorComponent();
                        if( comp != null ) {
                            comp.requestFocusInWindow();
                        }
                    }
                }
            }
        }

    }
    private void log(String mesg){
        CoeusOptionPane.showWarningDialog(mesg);
    }
    /**
     * Method to validate UI entries
     * @return boolean true if validation passed succesfully
     */
    public boolean validate() throws CoeusUIException {
        keyPersonCellEditor.stopCellEditing();
        if(keyPersonForm.tblInvestigator.getRowCount() > 0){
            int rowCount = keyPersonForm.tblInvestigator.getRowCount();
            for(int keyPersonIndex=0; keyPersonIndex < rowCount ;keyPersonIndex++){
                String stPersonId=(String)((AbstractTableModel)
                keyPersonForm.tblInvestigator.getModel()).
                        getValueAt(keyPersonIndex,8);
                String stRole=(String)((AbstractTableModel)
                keyPersonForm.tblInvestigator.getModel()).
                        getValueAt(keyPersonIndex,2);
                if((stPersonId == null) || (stPersonId.trim().length() <= 0)){
                    keyPersonForm.tblInvestigator.setRowSelectionInterval(rowCount-1,rowCount-1);
                    keyPersonForm.tblInvestigator.scrollRectToVisible(keyPersonForm.tblInvestigator.getCellRect(rowCount-1 ,0, true));
                    showErrorMessage(coeusMessageResources.parseMessageKey("keyPerson_exceptionCode.1601"));
                    keyPersonForm.tblInvestigator.requestFocus();
                    return false;
                }
                if((stRole == null) || (stRole.trim().length() <= 0)){
                    keyPersonForm.tblInvestigator.setRowSelectionInterval(keyPersonIndex,keyPersonIndex);
                    keyPersonForm.tblInvestigator.scrollRectToVisible(keyPersonForm.tblInvestigator.getCellRect(keyPersonIndex ,0, true));
                    showErrorMessage(coeusMessageResources.parseMessageKey(
                            "keyPerson_exceptionCode.1602"));
                    keyPersonForm.tblInvestigator.requestFocus();
                    return false;
                }
            }
        }
               

        return true;
    }

    public KeyPersonBean getSelectedInvestigatorBean(){
        KeyPersonBean invBean = null;
        int selectedRow = keyPersonForm.tblInvestigator.getSelectedRow();
        if( selectedRow != -1 )
        {
            invBean = keyPersonTableModel.getKeyPersonBean(selectedRow);

        }
               

        return invBean;
    }
    
    /** 
     * To show the alert messages
     * @param mesg a string message to the user.
     * @throws Exception a exception thrown in the client side.
     */
    
    public void showErrorMessage(String mesg) throws CoeusUIException {
        
        CoeusUIException coeusUIException = new CoeusUIException(mesg,CoeusUIException.WARNING_MESSAGE);
        coeusUIException.setTabIndex(keyPersonTabIndex);
        throw coeusUIException;        
    }
       private void showWarningMessage( String str ){
        CoeusOptionPane.showWarningDialog(str);
    }
    
    public void actionPerformed(java.awt.event.ActionEvent actionEvent) {
        Object source = actionEvent.getSource();
        keyPersonCellEditor.stopCellEditing();
        if (source.equals(keyPersonForm.btnAdd)){
            performAddAction();
        }else if(source.equals(keyPersonForm.btnFindPerson)){
            performFindPerson();
        }else if(source.equals(keyPersonForm.btnFindRolodex)){
            performFindRolodex();
        }else if(source.equals(keyPersonForm.btnDelete)){
            performDelete();
        }else if(source.equals(keyPersonForm.btnAddUnit)){
            performAddUnit();
        }else if(source.equals(keyPersonForm.btnDeleteUnit)){
            performDeleteUnit(true);
        }else if(source.equals(keyPersonForm.btnFindUnit)){
            performFindUnit();
        }
    }
    

    private void performAddUnit(){
        int selectedRow = keyPersonForm.tblInvestigator.getSelectedRow();
        int rowCount = keyPersonForm.tblUnits.getRowCount();
        if( selectedRow != -1 ){
         KeyPersonBean  keyPrsnBean = keyPersonTableModel.getKeyPersonBean(selectedRow);
            String invPersonId="";
            if(keyPrsnBean != null ) {
                invPersonId = keyPrsnBean.getPersonId();
            }
            if(invPersonId != null && invPersonId.trim().length()>0){
                /* if investigator data present for selected row in
                  investigator table then add blank row to unit table */
                KeyPersonUnitBean unitBean = new KeyPersonUnitBean();
                unitBean.setPersonId(invPersonId);
                unitBean.setAcType(INSERT_RECORD);
                CoeusVector cvUnits = keyPrsnBean.getKeyPersonsUnits();
                if( cvUnits == null ) {
                    cvUnits = new CoeusVector();
                }
                int unitRowCount = unitTableModel.getRowCount();
                if( unitRowCount > 0 ){
                    String unitNumber = (String)unitTableModel.getValueAt( unitRowCount-1,
                            KeyPersonUnitTableModel.UNIT_NUMBER_COLUMN);
                    if( unitNumber == null || unitNumber.trim().length() == 0 ) {
                        keyPersonForm.tblUnits.setRowSelectionInterval(unitRowCount-1,
                                unitRowCount-1);
                        keyPersonForm.tblUnits.scrollRectToVisible(
                                keyPersonForm.tblUnits.getCellRect(unitRowCount-1 ,0, true));
                        keyPersonForm.tblUnits.requestFocusInWindow();
                        keyPersonForm.tblUnits.editCellAt(unitRowCount-1,
                                KeyPersonUnitTableModel.UNIT_NUMBER_COLUMN);
                        keyPersonForm.tblUnits.getEditorComponent().requestFocusInWindow();
                        return;
                    }
                }
                cvUnits.add(unitBean);
                keyPrsnBean.setKeyPersonsUnits(cvUnits);
                String invAcType = keyPrsnBean.getAcType();
                if( null == invAcType ) {
                    keyPrsnBean.setAcType(UPDATE_RECORD);
                }
               cvKeypersons.set(selectedRow, keyPrsnBean);
                unitTableModel.setData(cvUnits);
                unitTableModel.fireTableDataChanged();
                rowCount = keyPersonForm.tblUnits.getRowCount() -1 ;
                keyPersonForm.tblUnits.setRowSelectionInterval(rowCount, rowCount);
                keyPersonForm.tblUnits.scrollRectToVisible(
                        keyPersonForm.tblUnits.getCellRect(rowCount ,0, true));
                dataChanged = true;
                if( keyPersonForm.tblUnits.getRowCount() > 0 ){
                    keyPersonForm.btnDeleteUnit.setEnabled( true );
                    keyPersonForm.btnAddUnit.setEnabled( true );
                    keyPersonForm.btnFindUnit.setEnabled( true );
                }
            }else{

                showWarningMessage(
                        coeusMessageResources.parseMessageKey(
                        "keyPerson_exceptionCode.1601") );
                return;
            }
        }else{
            showWarningMessage(
                    coeusMessageResources.parseMessageKey(
                    "keyPerson_exceptionCode.1601") );
            return;
        }

        keyPersonForm.tblUnits.requestFocusInWindow();
        keyPersonForm.tblUnits.editCellAt(rowCount,UnitCellEditor.UNIT_NUMBER_COLUMN);
        keyPersonForm.tblUnits.getEditorComponent().requestFocusInWindow();
    }


    /* supporting method to delete the selected unit row from the units table */
    public void performDeleteUnit(boolean nomsg){
        int selectedRow = keyPersonForm.tblInvestigator.getSelectedRow();
        /* delete the selected unit information from table as well as from
          the vector which consists of all unit details for a particular
          investigator */
        CoeusVector deletedUnits = null;
        if(selectedRow != -1){
            KeyPersonBean keyPrsnBean = keyPersonTableModel.getKeyPersonBean(selectedRow);
            String personId="";
            if( keyPrsnBean != null ){
                personId = keyPrsnBean.getPersonId();
            }
            int selectedUnitRow = keyPersonForm.tblUnits.getSelectedRow();
            if( selectedUnitRow != -1 ){
                String unitNo = "";
           KeyPersonUnitBean    unitBean = unitTableModel.getUnitBean(selectedUnitRow);
                if( unitBean != null ) {
                    unitNo = unitBean.getUnitNumber();
                }
                if(unitNo != null && unitNo.trim().length()>0){
                    int selectedOption;
                    if(nomsg){
                    selectedOption = showDeleteConfirmMessage("Are you sure you want"
                            +" to remove "+unitNo+" ?");}
                    else{selectedOption=JOptionPane.YES_OPTION;}
                    
                    if( selectedOption == JOptionPane.YES_OPTION ){
                        //Added for COEUSQA-2037 : Software allows you to delete an investigator who is assigned credit in the credit split window
                        //When there is only one unit for the investigator exists,
                        //removes the unit without check the credit split
                        //When multiple unit exists, Checks for the unit credit split information,
                        //until split set to '0' won't allow to delete the unit
                        int unitAvailCount=keyPersonForm.tblUnits.getRowCount();
                        if(unitAvailCount >= 1){
//                            int selectedOpt = CoeusOptionPane.showQuestionDialog(
//                                    coeusMessageResources.parseMessageKey(CREDIT_SPLIT_EXISTS_FOR_INV_UNIT) ,
//                                    CoeusOptionPane.OPTION_YES_NO,
//                                    CoeusOptionPane.DEFAULT_YES);
//                            if(selectedOpt == JOptionPane.YES_OPTION){
//                                if(isDataChanged()){
//                                    MessageFormat formatter = new MessageFormat("");
//                                    String message = formatter.format(
//                                            coeusMessageResources.parseMessageKey(SAVE_BEFORE_OPEN_CREDIT_SPILT),
//                                            this.moduleText);
//                                    CoeusOptionPane.showInfoDialog(message);
//                                }else{
//                                    performCreditSplitAction();
//                                }
                            
                        
                            KeyPersonUnitBean personUnitBean = null;
                            CoeusVector invUnits = keyPrsnBean.getKeyPersonsUnits();
                            CoeusVector delUnits = invUnits.filter( new Equals("unitNumber",unitNo));
                            invUnits.remove(selectedUnitRow);
                            unitTableModel.setData(invUnits);
                            keyPrsnBean.setKeyPersonsUnits(invUnits);
                            String invAcType = keyPrsnBean.getAcType();
                            if( null == invAcType ) {
                                keyPrsnBean.setAcType(UPDATE_RECORD);
                            }

                            cvKeypersons.set(selectedRow,keyPrsnBean);
                            unitTableModel.fireTableDataChanged();
                            keyPersonTableModel.setData(cvKeypersons);
                            keyPersonTableModel.fireTableDataChanged();
                            keyPersonForm.tblInvestigator.setRowSelectionInterval(
                                    selectedRow, selectedRow);
                            if( delUnits != null && delUnits.size() > 0) {
                                int delSize = delUnits.size();
                                 deletedUnits = (CoeusVector)hmDeletedUnits.get(personId);
                                if( null == deletedUnits ){
                                    deletedUnits = new CoeusVector();
                                }
                                for( int indx = 0; indx < delSize; indx++ ) {
                                    personUnitBean = (KeyPersonUnitBean)delUnits.get(indx);
//                                String acType = personUnitBean.getAcType();
//                                if( null == acType || UPDATE_RECORD.equals(acType)){
                                    personUnitBean.setAcType(DELETE_RECORD);
                                    deletedUnits.add(personUnitBean);
//                                }
                                }
                                hmDeletedUnits.put(personId, deletedUnits);
                            }
                            dataChanged = true;
                        }
                         else{
//                     int selectedOption = showDeleteConfirmMessage(
//                        coeusMessageResources.parseMessageKey(
//                                        "unitDetFrm_exceptionCode.1331"));
//                    if( selectedOption == JOptionPane.YES_OPTION ){
                    dataChanged = true;
                    CoeusVector invUnits = keyPrsnBean.getKeyPersonsUnits();
                    
                  KeyPersonUnitBean   personUnitBean = (KeyPersonUnitBean)invUnits.get(selectedUnitRow);
                  invUnits.remove(selectedUnitRow);
//                                String acType = personUnitBean.getAcType();
//                                if( null == acType || UPDATE_RECORD.equals(acType)){
                                    personUnitBean.setAcType(DELETE_RECORD);
                              // CoeusVector  deletedUnits=new CoeusVector();
                               deletedUnits.add(personUnitBean);
                                hmDeletedUnits.put(personId, deletedUnits);
                    unitTableModel.setData(invUnits);
                    keyPrsnBean.setKeyPersonsUnits(invUnits);
                    cvKeypersons.set(selectedRow,keyPrsnBean);
                    unitTableModel.fireTableDataChanged();
                    }
                }

//                    }
                }else if(unitNo == null)
                {
                    //for deleting the empty created unit details.
                     CoeusVector invUnits = keyPrsnBean.getKeyPersonsUnits();

                 
                  invUnits.remove(selectedUnitRow);
                    unitTableModel.setData(invUnits);
                    keyPrsnBean.setKeyPersonsUnits(invUnits);
                     cvKeypersons.set(selectedRow,keyPrsnBean);
                    unitTableModel.fireTableDataChanged();
                }

                int newRowCount = keyPersonForm.tblUnits.getRowCount();
                if(newRowCount >0){
                    if(newRowCount > selectedUnitRow){
                        keyPersonForm.tblUnits.setRowSelectionInterval(selectedUnitRow,
                                selectedUnitRow);
                    }else{
                        keyPersonForm.tblUnits.setRowSelectionInterval(
                                newRowCount - 1,
                                newRowCount - 1);
                    }
                }else{
                    keyPersonForm.btnDeleteUnit.setEnabled( false );
                    keyPersonForm.btnAddUnit.requestFocusInWindow();
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
            int selectedRow = keyPersonForm.tblInvestigator.getSelectedRow();
            int selectedUnitRow = keyPersonForm.tblUnits.getSelectedRow();
            CoeusSearch proposalSearch = null;
            String unitID = null;
            // check whether any investigator has been selected or not
            if(selectedRow != -1){

                String pId =  "";
                KeyPersonBean keyPrsnBean = keyPersonTableModel.getKeyPersonBean(selectedRow);
                if( keyPrsnBean != null ){
                    pId = keyPrsnBean.getPersonId();
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
                                KeyPersonUnitBean unitBean =
                                        new KeyPersonUnitBean();
                                unitBean.setPersonId(pId);
                                //unitBean.setOspAdministratorName(adminPerson);
                                unitBean.setUnitNumber(unitID);
                                unitBean.setUnitName(unitName);
                               // unitBean.setLeadUnitFlag(false);
                                unitBean.setAcType( INSERT_RECORD );
                                CoeusVector cvInvUnits = keyPrsnBean.getKeyPersonsUnits();
                                if( cvInvUnits == null ) {
                                    cvInvUnits = new CoeusVector();
                                }
//                                addToHashtable(pId,unitBean);
                                if( selectedUnitRow != -1 ){
                                    // if any unit table row is selected update that
                                    //  row
                                    String oldUnitNumber = (String)unitTableModel.getValueAt(
                                            selectedUnitRow,KeyPersonUnitTableModel.UNIT_NUMBER_COLUMN);
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
                                keyPrsnBean.setKeyPersonsUnits(cvInvUnits);
                                String invAcType = keyPrsnBean.getAcType();
                                if( null == invAcType ) {
                                    keyPrsnBean.setAcType(UPDATE_RECORD);
                                }
                                cvKeypersons.set(selectedRow,keyPrsnBean);
                                unitTableModel.fireTableDataChanged();
                            }else{
                                showWarningMessage( "' " + unitID + " ' " +
                                        coeusMessageResources.parseMessageKey(
                                        "protoInvFrm_exceptionCode.1137"));
                            }
                        }// end of for
                    } // end of vSelectedUnit != null
                    if( keyPersonForm.tblUnits.getRowCount() > 0 ){
                        int lastRow = keyPersonForm.tblUnits.getRowCount() -1 ;
                        keyPersonForm.tblUnits.scrollRectToVisible(
                                keyPersonForm.tblUnits.getCellRect(lastRow ,0, true));
                        keyPersonForm.tblUnits.setRowSelectionInterval(lastRow,
                                lastRow);
                        /* enable delete unit button if any unit information
                         is present for selected investigator */
                        keyPersonForm.btnDeleteUnit.setEnabled( true );
                    }
                }else{
                    showWarningMessage(
                            coeusMessageResources.parseMessageKey(
                            "keyPerson_exceptionCode.1601") );
                }

            }else{
                showWarningMessage(
                        coeusMessageResources.parseMessageKey(
                        "keyPerson_exceptionCode.1601") );
            }

        }catch( Exception err ){
            err.printStackTrace();
        }

    }
    
    
    /**
     * This method adds a row to the end of the Key Person table
     * with the default values in the rows
     * @return void
     **/
    private void performAddAction() {

        int rowCount =  keyPersonForm.tblInvestigator.getRowCount();
        if(rowCount  > 0) {
            if(cvKeypersons != null && cvKeypersons.size() > 0){
                for(int index =0 ; index < cvKeypersons.size(); index++){
                    KeyPersonBean bean = (KeyPersonBean) cvKeypersons.get(index);
                    if ( CoeusGuiConstants.EMPTY_STRING.equals( bean.getPersonId().trim() ) ) {
                        keyPersonForm.tblInvestigator.setRowSelectionInterval(index,index);
                        keyPersonForm.tblInvestigator.scrollRectToVisible(
                                keyPersonForm.tblInvestigator.getCellRect(index ,0, true));
                        keyPersonForm.tblInvestigator.editCellAt(index,
                                KEY_PERSON_NAME_COLUMN);
                        return ;
                    }
                }
            }
        }
        
        KeyPersonBean newBean= new KeyPersonBean();
        newBean.setPersonName(CoeusGuiConstants.EMPTY_STRING);
        newBean.setProjectRole(CoeusGuiConstants.EMPTY_STRING);
        newBean.setPersonId(CoeusGuiConstants.EMPTY_STRING);
        newBean.setPercentageEffort(0.00f);
        newBean.setAcademicYearEffort(0.00f);
        newBean.setSummerYearEffort(0.00f);
        newBean.setCalendarYearEffort(0.00f);
        newBean.setPersonId("");
        newBean.setNonMITPersonFlag(true);
        //ppc change for keyperson starts
        newBean.setPpcCertifyFlag(0);
        //ppc change for keyperson ends
        newBean.setAcType(INSERT_RECORD);
        
        dataChanged = true;
        cvKeypersons  = keyPersonTableModel.getData();
        if( cvKeypersons == null ) {
            cvKeypersons = new CoeusVector();
        }
        cvKeypersons.add(newBean);
        keyPersonTableModel.fireTableRowsInserted(
                keyPersonTableModel.getRowCount()+1,keyPersonTableModel.getRowCount() + 1);
        
        int lastRow = keyPersonForm.tblInvestigator.getRowCount() - 1;
        if (lastRow >= 0){
            keyPersonForm.tblInvestigator.setRowSelectionInterval(lastRow,lastRow);
            keyPersonForm.tblInvestigator.scrollRectToVisible(
                    keyPersonForm.tblInvestigator.getCellRect(lastRow,
                    KEY_PERSON_NAME_COLUMN, true));
            keyPersonForm.btnDelete.setEnabled( true );
            
            
            
        }
        keyPersonForm.tblInvestigator.requestFocusInWindow();
        keyPersonForm.tblInvestigator.editCellAt(lastRow,KEY_PERSON_NAME_COLUMN);
        keyPersonForm.tblInvestigator.getEditorComponent().requestFocusInWindow();
    }
       
    /**
     * Method used for deleting a Key Person
     * @return void
     **/
    private void performDelete(){
        // delete investigator row after confirmation
        int selectedRow = keyPersonForm.tblInvestigator.getSelectedRow();
        if( selectedRow != -1 ){
            KeyPersonBean keypersonBean = keyPersonTableModel.getKeyPersonBean(selectedRow);
            String name = "";
            String personId="";
            if( keypersonBean != null ) {
                name = keypersonBean.getPersonName();
                personId = keypersonBean.getPersonId();
            }
            
            if(name != null && name.trim().length()>0){
                int selectedOption
                        = showDeleteConfirmMessage(
                        "Do you want to remove "+name+"?");
                if( selectedOption == JOptionPane.YES_OPTION ){
                    //deleting all the unit under the keyperson
//CoeusVector delunits;
//                    KeyPersonUnitBean kpunit;
//                    delunits=keypersonBean.getKeyPersonsUnits();
//                    for(int i=0;i<delunits.size();i++)
//                    {kpunit=(KeyPersonUnitBean)keypersonBean.getKeyPersonsUnits().get(i);
//                        if(kpunit!=null){updateKeyperson(keypersonBean.getPersonId(),kpunit);}}
//                   while(delunits.size()>0)
//                   {kpunit=(KeyPersonUnitBean)keypersonBean.getKeyPersonsUnits().get(0);
//                        if(kpunit!=null){performDeleteUnit(true);}
//                   }

                    if(personId != null && personId.trim().length()>0){
                        cvKeypersons.remove(selectedRow);
                        String acType = keypersonBean.getAcType();
                        if( null == acType || UPDATE_RECORD.equals(acType)){
                            keypersonBean.setAcType(DELETE_RECORD);
                            deletedKeyPersons.add(keypersonBean);
                        }
                        keyPersonTableModel.fireTableRowsDeleted(selectedRow, selectedRow);
                    }
                    dataChanged = true;
                }
            }else{
                int selectedOption = showDeleteConfirmMessage(coeusMessageResources.parseMessageKey(
                        "keyPerson_exceptionCode.1603"));
                if( selectedOption == JOptionPane.YES_OPTION ){
                    cvKeypersons.remove(selectedRow);
                    keyPersonTableModel.fireTableDataChanged();
                    dataChanged = true;
                }
            }
            if( keyPersonForm.tblInvestigator.getRowCount() <= 0 ){
                keyPersonForm.btnDelete.setEnabled( false );
                keyPersonForm.tblUnits.setRowSelectionInterval(0,0);
               // keyPersonForm.repaint();tblUnits
               // updateUnits();
                unitTableModel.setData(null);
                unitTableModel.fireTableDataChanged();
                keyPersonForm.tblInvestigator.requestFocusInWindow();
                keyPersonForm.btnAdd.requestFocusInWindow();
            }
            
            int newRowCount = keyPersonForm.tblInvestigator.getRowCount();
            if(newRowCount >0){
                if(newRowCount > selectedRow){
                    keyPersonForm.tblInvestigator.setRowSelectionInterval(selectedRow,
                            selectedRow);
                }else{
                    keyPersonForm.tblInvestigator.setRowSelectionInterval(
                            newRowCount - 1,newRowCount - 1);
                }
                
            }
        }else{
            showWarningMessage(coeusMessageResources.parseMessageKey(
                            "keyPerson_exceptionCode.1604"));
        }
    }
    
    // JM 8-22-2012 get login username for DB updates
    private String getLoginUsername() {
    	String loginUserName = CoeusGuiConstants.getMDIForm().getUserName();
    	return loginUserName;
    }
    // JM END
    
    
    // JM 8-1-2012 get the default home unit for a person
    private CoeusVector getHomeUnitForPerson(KeyPersonBean keyPersonBean) {
    	CoeusVector cvHomeUnit = new CoeusVector();
    	CoeusVector cvKeyPersonUnits = new CoeusVector();
    	KeyPersonUnitBean beanHomeUnit;

        RequesterBean requesterBean = new RequesterBean();
        requesterBean.setFunctionType(GET_HOME_UNIT_FOR_KP);
        requesterBean.setDataObject(keyPersonBean.getPersonId());
    
        AppletServletCommunicator appletServletCommunicator = new AppletServletCommunicator();
        appletServletCommunicator.setConnectTo(CoeusGuiConstants.CONNECTION_URL + PROP_DEV_SERVLET);
        appletServletCommunicator.setRequest(requesterBean);
        appletServletCommunicator.send();
        ResponderBean responderBean = appletServletCommunicator.getResponse();
        
        if (responderBean.getDataObjects() != null) {
        	cvHomeUnit = (CoeusVector) responderBean.getDataObjects();        	
        }
        else {
        	try {
				throw new CoeusClientException("Cannot retrieve home unit data for person.",CoeusClientException.ERROR_MESSAGE);
			} catch (CoeusClientException e) {
				e.printStackTrace();
			}
        }
        
        if (cvHomeUnit.size() > 0) {
	        beanHomeUnit = (KeyPersonUnitBean) cvHomeUnit.get(0);
	        //System.out.println("homeUnit :: " + beanHomeUnit.getUnitNumber());
	        
	    	KeyPersonUnitBean keyPersonHomeUnitBean = new KeyPersonUnitBean();
	    	keyPersonHomeUnitBean.setUnitNumber(beanHomeUnit.getUnitNumber());
	    	keyPersonHomeUnitBean.setUnitName(beanHomeUnit.getUnitName());
	    	keyPersonHomeUnitBean.setAcType("I");
    
	    	keyPersonHomeUnitBean.setAw_PersonId(keyPersonBean.getPersonId());
	    	keyPersonHomeUnitBean.setAw_UnitNumber(beanHomeUnit.getUnitNumber());
	    	keyPersonHomeUnitBean.setPersonId(keyPersonBean.getPersonId());
	    	keyPersonHomeUnitBean.setPersonName(keyPersonBean.getPersonName());
	    	//keyPersonHomeUnitBean.setProposalNumber(proposalNumber);
	    	//keyPersonHomeUnitBean.setSequenceNumber(0);
	    	keyPersonHomeUnitBean.setUpdateTimestamp(keyPersonBean.getUpdateTimestamp());
	    	keyPersonHomeUnitBean.setUpdateUser(getLoginUsername());
	    	
	    	cvKeyPersonUnits.add(keyPersonHomeUnitBean);
        }
       
    	return cvKeyPersonUnits;
    }
    // JM END
    
    /**
     * Method used for Searching for a Person and setting the Person details to
     * KeyPersonBean
     * @return void
     **/
    private void performFindPerson(){
        try{
            CoeusSearch coeusSearch =
                    new CoeusSearch(CoeusGuiConstants.getMDIForm(), "personSearch",
                    CoeusSearch.TWO_TABS_WITH_MULTIPLE_SELECTION ); 
            coeusSearch.showSearchWindow();
            Vector vSelectedPersons = coeusSearch.getMultipleSelectedRows();
            if ( vSelectedPersons != null ){
                HashMap singlePersonData = null;
                for(int indx = 0; indx < vSelectedPersons.size(); indx++ ){
                    
                    singlePersonData = (HashMap)vSelectedPersons.get( indx ) ;
                    
                    if( singlePersonData == null || singlePersonData.isEmpty() ){
                        continue;
                    }
                    String personId = checkForNull(singlePersonData.get( "PERSON_ID" ));
                    String personName = checkForNull(singlePersonData.get( "FULL_NAME" ));
                    String personRole = checkForNull(singlePersonData.get( "DIRECTORY_TITLE" ));
                    boolean faculty
                            = checkForNull(singlePersonData.get("IS_FACULTY")).
                            equalsIgnoreCase("y") ? true : false;
                    
                    KeyPersonBean keyPersonBean = new KeyPersonBean();
                    keyPersonBean.setPersonId(personId);
                    keyPersonBean.setPersonName(personName);
                    keyPersonBean.setProjectRole(personRole);
                    keyPersonBean.setPercentageEffort( new Float(0).floatValue() );
                    keyPersonBean.setFacultyFlag(faculty);
                    keyPersonBean.setNonMITPersonFlag(false);
                    keyPersonBean.setAcType(INSERT_RECORD );
                    keyPersonBean.setAw_PersonId(personId);
                    // JM 8-9-2012 get home unit for this person
                    keyPersonBean.setKeyPersonsUnits(getHomeUnitForPerson(keyPersonBean));
                    // JM END
                    setkeyPersonBean(keyPersonBean,false);
                }
            }
        }catch( Exception err ){
            err.printStackTrace();
        }
    }
    
    /**
     * Method used for Searching for a Rolodex Person and setting the rolodex details to
     * KeyPersonBean
     * @return void
     **/
    private void performFindRolodex(){
        try{
            CoeusSearch coeusSearch =
                    new CoeusSearch(CoeusGuiConstants.getMDIForm(), "rolodexSearch",
                    CoeusSearch.TWO_TABS_WITH_MULTIPLE_SELECTION ) ; 
            coeusSearch.showSearchWindow();
            Vector vSelectedRolodex = coeusSearch.getMultipleSelectedRows();
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
                    String personRole = checkForNull(singleRolodexData.get( "TITLE" ));
                    String rolodexName = null;
                    /* construct full name of the rolodex if his last name is present
                      otherwise use his organization name to display in person name
                      column of keyPerson table */
                    if ( lastName.length() > 0) {
                        rolodexName = ( lastName + " "+nameSuffix +", "+ namePreffix
                                +" "+firstName +" "+ middleName ).trim();
                    } else {
                        rolodexName = checkForNull(singleRolodexData.get("ORGANIZATION"));
                    }
                    KeyPersonBean keyPerson = new KeyPersonBean();
                    keyPerson.setPerOrRol(true);
                    keyPerson.setPersonId(rolodexID);
                    keyPerson.setPersonName(rolodexName);
                    keyPerson.setPercentageEffort(0.0f);
                    keyPerson.setProjectRole(personRole);
                    keyPerson.setFacultyFlag(false);
                    keyPerson.setNonMITPersonFlag(true);
                    keyPerson.setAcType( INSERT_RECORD );
                    setkeyPersonBean(keyPerson,false);
                } 
            }
        }catch( Exception err ){
            err.printStackTrace();
        }
        
    }
    
    /**
     * Method used for setting Empty String to the passes aguement if the  arguement is Null.
     * @return Empty String if the value is null
     **/
    private String checkForNull( Object value ){
        return (value==null)? "":value.toString();
    }
    
    /**
     * Method used to set the KeyPerson Bean into KeyPerson Vector and to the 
     * Key Person Table.
     * @return void
     */
    public void setkeyPersonBean(KeyPersonBean keyPerson,
            boolean replaceRow) {
        
        int selectedRow = keyPersonForm.tblInvestigator.getSelectedRow();
        String oldPersonId = "";
        KeyPersonBean keyPersonBean = null;
        if (selectedRow != -1){
            keyPersonBean = keyPersonTableModel.getKeyPersonBean(selectedRow);
            if( keyPersonBean != null ) {
                oldPersonId = keyPersonBean.getPersonId();
            }
            int lastRow = keyPersonForm.tblInvestigator.getRowCount() - 1;
            if( oldPersonId == null  || oldPersonId.trim().length() == 0 ){
                replaceRow = true;
            }else if( selectedRow != lastRow  && !replaceRow){
                selectedRow = lastRow;
                keyPersonBean = keyPersonTableModel.getKeyPersonBean(selectedRow);
                if( keyPersonBean != null ) {
                    oldPersonId = keyPersonBean.getPersonId();
                }
                if( oldPersonId == null  || oldPersonId.trim().length() == 0 ){
                    replaceRow = true;
                }
            }
        }
        
        String personId = keyPerson.getPersonId();
        String personName = keyPerson.getPersonName();
        
        if(!personId.equals(oldPersonId)){
            // Check for duplicate person entry
            if(! checkDuplicatePerson( personId )){
                dataChanged = true;
                if (replaceRow ) {
                    if( null == keyPersonBean.getAcType() ||
                            UPDATE_RECORD.equals(keyPersonBean.getAcType())){
                        keyPersonBean.setAcType(DELETE_RECORD);
                        try{
                            deletedKeyPersons.add(ObjectCloner.deepCopy(keyPersonBean));
                        }catch(Exception e){
                            CoeusOptionPane.showErrorDialog(e.getMessage());
                        }
                    }
                    keyPersonBean = keyPerson;
                    cvKeypersons.set(selectedRow, keyPersonBean);
                    keyPersonTableModel.fireTableDataChanged();
                    keyPersonForm.tblInvestigator.setRowSelectionInterval(selectedRow,
                            selectedRow);
                } else {
                    // New Entry
                    cvKeypersons.add(keyPerson);
                    keyPersonTableModel.fireTableDataChanged();
                    int  newRowCount = keyPersonForm.tblInvestigator.getRowCount();
                    selectedRow = newRowCount - 1;
                    keyPersonForm.tblInvestigator.setRowSelectionInterval(selectedRow,
                            selectedRow);
                    keyPersonForm.tblInvestigator.scrollRectToVisible(
                            keyPersonForm.tblInvestigator.getCellRect(selectedRow ,0, true));
                    keyPersonForm.tblInvestigator.editCellAt(selectedRow,
                            KEY_PERSON_NAME_COLUMN);

                }
                
            }else{
                showWarningMessage("' " + personName + " ' " +
                        "already exists.");
            }
        }else{
            showWarningMessage("' " + personName + " ' " +
                    "already exists.");
        }
        if( keyPersonForm.tblInvestigator.getRowCount() > 0 ){
            keyPersonForm.btnDelete.setEnabled( true );
        }
    }
    
    /**
     * Method used to check if the passed Person was already added as a Key person 
     * @return true if the person alreday exists 
     * @param personId
     */
    private boolean checkDuplicatePerson(String personId ){
        if( cvKeypersons != null ){
            CoeusVector dupIKeyPersons = cvKeypersons.filter(
                    new Equals("personId",personId));
            if( dupIKeyPersons != null && dupIKeyPersons.size() > 0 ) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Displays the message passed to this method as a Warning.
     * @return void
     */
  
    
    /**
     * Displays the message passed to this method as a Confirmation Message.
     * @return selectedOption
     */
    private int showDeleteConfirmMessage(String msg){
        int selectedOption = CoeusOptionPane.showQuestionDialog(msg,
                CoeusOptionPane.OPTION_YES_NO,
                CoeusOptionPane.DEFAULT_YES);
        return  selectedOption;
    }

    /** 
     * Getter for property dataChanged.
     * @return Value of property dataChanged.
     */
    public boolean isDataChanged() {
        keyPersonCellEditor.stopCellEditing();
        return dataChanged;
    }
    
    /** 
     * Setter for property dataChanged.
     * @param dataChanged New value of property dataChanged.
     */
    public void setDataChanged(boolean dataChanged) {
        this.dataChanged = dataChanged;
    }


     private Vector prepareData(){
        Vector vecData = new Vector();
        Hashtable htPersonData = new Hashtable();
        Hashtable htUnitData = new Hashtable();

        ProposalKeyPersonFormBean propInvestigatorFormBean;
        for (int index = 0 ; index < cvKeypersons.size() ; index++){
            propInvestigatorFormBean = new ProposalKeyPersonFormBean();

            ProposalKeyPersonFormBean invBean = (ProposalKeyPersonFormBean)cvKeypersons.get(index);

            propInvestigatorFormBean.setPercentageEffort(invBean.getPercentageEffort());
            // JM 6-15-2012 added academic, summer, and calendar efforts
            propInvestigatorFormBean.setAcademicYearEffort(invBean.getAcademicYearEffort());
            propInvestigatorFormBean.setSummerYearEffort(invBean.getSummerYearEffort());
            propInvestigatorFormBean.setCalendarYearEffort(invBean.getCalendarYearEffort());
            // END JM
            propInvestigatorFormBean.setPersonId(invBean.getPersonId());
            propInvestigatorFormBean.setPersonName(invBean.getPersonName());
            
            /* JM 9-9-2015 added person status */
            propInvestigatorFormBean.setStatus(invBean.getStatus());
            /* JM END */
            
            htPersonData.put(invBean.getPersonId() , propInvestigatorFormBean);

            CoeusVector cvInvUnitData = (CoeusVector) invBean.getKeyPersonsUnits();
            if(cvInvUnitData != null && cvInvUnitData.size() > 0){
                Vector vecUnitData = new Vector();
                ProposalKPUnitFormBean proposalLeadUnitFormBean = new ProposalKPUnitFormBean();

                for(int i = 0 ; i < cvInvUnitData.size(); i++){
                    proposalLeadUnitFormBean = new ProposalKPUnitFormBean();
                    ProposalKPUnitFormBean invUnitBean = (ProposalKPUnitFormBean)cvInvUnitData.get(i);

                    proposalLeadUnitFormBean.setLeadUnitFlag(invUnitBean.isLeadUnitFlag());
                    proposalLeadUnitFormBean.setPersonId(invUnitBean.getPersonId());
                    proposalLeadUnitFormBean.setPersonName(invUnitBean.getPersonName());
                    proposalLeadUnitFormBean.setUnitName(invUnitBean.getUnitName());
                    proposalLeadUnitFormBean.setUnitNumber(invUnitBean.getUnitNumber());
                    //Added for Case#2136 Enhancement start 5
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
     * Getter for property sequenceNumber.
     * 
     * @return Value of property sequenceNumber.
     */
    public java.lang.String getSeqNo() {
        return sequenceNumber;
    }
    
    /**
     * Setter for property sequenceNumber.
     * @param sequenceNumber New value of property sequenceNumber.
     */
    public void setSeqNo(java.lang.String seqNo) {
        this.sequenceNumber = seqNo;
    }
       
    public void setDefaultFocusInWindow() {
        keyPersonForm.tblInvestigator.requestFocusInWindow();
    }
    
    public void setKeyPersonTabIndex(int aKeyPersonTabIndex) {
        keyPersonTabIndex = aKeyPersonTabIndex;
    }
    
    class KeyPersonDetailsAdapter extends MouseAdapter {
        public void mouseClicked(MouseEvent me){
            if( me.getClickCount() == 2 ) {
                int selRow = keyPersonForm.tblInvestigator.getSelectedRow();
                if( selRow != -1 ) {
                    KeyPersonBean keyPersonBean = keyPersonTableModel.getKeyPersonBean(selRow);
                    String personId = keyPersonBean.getPersonId();
                    boolean nonEmployee = keyPersonBean.isNonMITPersonFlag();
                    if( personId != null && personId.trim().length() > 0 ) {
                        if(nonEmployee){
                            RolodexMaintenanceDetailForm frmRolodex
                                    = new RolodexMaintenanceDetailForm('V',personId);
                            frmRolodex.showForm(CoeusGuiConstants.getMDIForm(),
                                    CoeusGuiConstants.TITLE_ROLODEX,true);
                        }else{
                            String loginUserName = CoeusGuiConstants.getMDIForm().getUserName();
                            try{
                                
                                PersonDetailForm personDetailForm = new PersonDetailForm(personId ,loginUserName,DISPLAY_MODE);
                            }catch ( Exception e) {
                                CoeusOptionPane.showInfoDialog( e.getMessage() );
                            }
                        }
                    }
                }
            }
        }
    }
       class UnitDisplayAdapter extends MouseAdapter {
        public void mouseClicked( MouseEvent me ) {
           keyPersonCellEditor.stopCellEditing();//Added by Nadh for Bug fix 1611
            if (me.getClickCount() == 2) {
                int selUnitRow = keyPersonForm.tblUnits.getSelectedRow();
                if( selUnitRow != -1 ) {
                   KeyPersonUnitBean unitBean = unitTableModel.getUnitBean(selUnitRow);
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
    public void setTableKeyTraversal(){
        
        javax.swing.InputMap im = keyPersonForm.tblInvestigator.getInputMap(JTable.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        // Have the enter key work the same as the tab key
        KeyStroke tab = KeyStroke.getKeyStroke(KeyEvent.VK_TAB, 0);
        KeyStroke shiftTab = KeyStroke.getKeyStroke(KeyEvent.VK_TAB,KeyEvent.SHIFT_MASK );
        
        // Override the default tab behaviour
        // Tab to the next editable cell. When no editable cells goto next cell.
        final Action oldTabAction = keyPersonForm.tblInvestigator.getActionMap().get(im.get(tab));
        Action tabAction = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                oldTabAction.actionPerformed( e );
                JTable table = (JTable)e.getSource();
                int rowCount = table.getRowCount();
                int columnCount = table.getColumnCount();
                int row = table.getSelectedRow();
                int column = table.getSelectedColumn();
                
                while (! table.isCellEditable(row, column) ) {
                    column += 1;
                    
                    if (column == columnCount) {
                        column = 0;
                        row +=1;
                    }
                    
                    if (row == rowCount) {
                        row = 0;
                    }
                    
                    if (row == table.getSelectedRow()
                    && column == table.getSelectedColumn()) {
                        break;
                    }
                }
                
                table.changeSelection(row, column,false, false);
                table.editCellAt(row,column);
                table.getEditorComponent().requestFocusInWindow();
                
            }
        };
        keyPersonForm.tblInvestigator.getActionMap().put(im.get(tab), tabAction);
        
        
        
        
        // for the shift+tab action
        
        // Override the default tab behaviour
        // Tab to the previous editable cell. When no editable cells goto next cell.
        
        final Action oldTabAction1 = keyPersonForm.tblInvestigator.getActionMap().get(im.get(shiftTab));
        Action tabAction1 = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                oldTabAction1.actionPerformed( e );
                JTable table = (JTable)e.getSource();
                int rowCount = table.getRowCount();
                int columnCount = table.getColumnCount();
                int row = table.getSelectedRow();
                int column = table.getSelectedColumn();
                int totalColumns = 4;
                
                while (! table.isCellEditable(row, column) ) {                  
                    column -= 1;
                    if (column <= 0 ) {
                        column = totalColumns;
                        row -=1;
                    }
                    
                    if (row < 0) {
                        row = rowCount-1;
                    }
                    
                    if (row == table.getSelectedRow()
                    && column == table.getSelectedColumn()) {
                        break;
                    }
                }
                
                table.changeSelection(row, column, true, true);
                table.editCellAt(row,column);
                table.getEditorComponent().requestFocusInWindow();
            }
        };
        keyPersonForm.tblInvestigator.getActionMap().put(im.get(shiftTab), tabAction1);
    }



// class UnitDisplayAdapter extends MouseAdapter {
//        public void mouseClicked( MouseEvent me ) {
//            keyPersonCellEditor.stopCellEditing();//Added by Nadh for Bug fix 1611
//            if (me.getClickCount() == 2) {
//                int selUnitRow = keyPersonForm.tblUnits.getSelectedRow();
//                if( selUnitRow != -1 ) {
//                   KeyPersonUnitBean  unitBean = unitTableModel.getUnitBean(selUnitRow);
//                    String unitNumber = unitBean.getUnitNumber();
//                    if( unitNumber != null && unitNumber.trim().length() > 0 ) {
//                        try{
//                            UnitDetailForm frmUnit = new UnitDetailForm(unitNumber,'G');
//                            frmUnit.showUnitForm(CoeusGuiConstants.getMDIForm());
//                        } catch(Exception ex){
//                            CoeusOptionPane.showErrorDialog(
//                                    coeusMessageResources.parseMessageKey(
//                                    "protoKeyPer_exceptionCode.1136"));
//                        }
//                    }
//                }
//            }
//        }
//    }
    //ppc change for keyperson starts
       public class ImageRendererPPC extends DefaultTableCellRenderer {
        ImageIcon checkIcon;
        ImageIcon crossIcon;
        ImageIcon emptyIcon=null;
       public ImageRendererPPC(){
           checkIcon = new ImageIcon(getClass().getClassLoader().getResource(CoeusGuiConstants.COMPLETE_ICON));
           crossIcon = new ImageIcon(getClass().getClassLoader().getResource(CoeusGuiConstants.NONE_ICON));
      }
        
        public Component getTableCellRendererComponent(JTable table,
        Object value, boolean isSelected, boolean hasFocus, int row,
        int column) {
                setOpaque(false);
                value=getValueAt(row,column);
                setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
                int certifyValue = Integer.parseInt(value.toString());
                    if(certifyValue == 1){
                       setIcon(checkIcon);                    
                    }else if(certifyValue == -1){
                        setIcon(emptyIcon);
                    }else {
                        setIcon(crossIcon);
                    }
            return this;
        }
        public int getValueAt(int row, int col) {
        KeyPersonBean  keyPersonBean = (KeyPersonBean) cvKeypersons.get(row);
        return keyPersonBean.getPpcCertifyFlag();
        } 
    }
  //ppc change for keyperson ends
    public void showUnit()
    {
         String parameter="";
           parameter=getParameterValue();
           if(parameter.equals("0"))
           {
               keyPersonForm.jcrPnUnits.setVisible( false );
               keyPersonForm.btnDeleteUnit.setVisible(false);
               keyPersonForm.btnAddUnit.setVisible(false);
               keyPersonForm.btnFindUnit.setVisible(false);
           }
    }
}