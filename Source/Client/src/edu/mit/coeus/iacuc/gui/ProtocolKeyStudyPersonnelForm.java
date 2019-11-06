/*
 * @(#)ProtocolKeyStudyPersonnelForm.java  1.0  19/9/02
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */
/* PMD check performed, and commented unused imports and variables on 09-MARCH-2011
 * by Md.Ehtesham Ansari
 */
package edu.mit.coeus.iacuc.gui;

import edu.mit.coeus.utils.query.Equals;
import java.awt.*;
import java.util.*;
import java.awt.event.*;
import java.beans.*;
import javax.swing.*;
import javax.swing.table.*;

import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.utils.*;
import edu.mit.coeus.rolodexmaint.gui.RolodexMaintenanceDetailForm;
import edu.mit.coeus.gui.CoeusAppletMDIForm;
import edu.mit.coeus.search.gui.*;
import edu.mit.coeus.iacuc.bean.*;
import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.departmental.gui.PersonDetailForm;
import edu.mit.coeus.irb.bean.PersonInfoFormBean;

import edu.mit.coeus.exception.*;

/** <CODE>ProtocolKeyStudyPersonnelForm</CODE> is a form object which display
 * all the key study personnel's for the selected protocol and can be used to
 * <CODE>add/modify/display</CODE> the key study personnel details.
 * This class is instantiated in <CODE>ProtocolDetailForm</CODE>.
 *
 * @author kprasad
 * @version: 1.0 September 30, 2002
 * @author Raghunath P.V
 * @version: 1.1 October 24, 2002
 */
public class ProtocolKeyStudyPersonnelForm extends javax.swing.JComponent implements Observer {

    /* Used to hold a Jtable */
    private javax.swing.JScrollPane scrPnPane1;
    /* This is used to store the KeyStudyPersonal Data */
    private javax.swing.JTable tblKeyStPer;
    /* Button to search a person */
    private javax.swing.JButton btnFindPerson;
    /* Button to delete a row in table */
    private javax.swing.JButton btnDelete;
    /* Button to add a row in the JTable */
    private javax.swing.JButton btnAdd;
    /*  Button to search a Rolodex */
    private javax.swing.JButton btnFindRolodex;
    
    /* Data Beans to hold Key Study Person Details */
    private ProtocolKeyPersonnelBean protocolDataBean;
    private ProtocolKeyPersonnelBean protocolNewDataBean;
    private ProtocolKeyPersonnelBean protocolOldDataBean;

    /* Character variable to hold the function type which may be add,delete,
     modify */
    private char functionType;
    /* Vector to hold all the Data Beans */
    private Vector protocolKeyStudyPersonnelData;
    /* This is used to hold MDI form reference */
    private CoeusAppletMDIForm mdiReference;
    /* This is used to know whether data is modified or add  */
    private boolean saveRequired = false;
    /* Specifies the protocol information is modified or not */
    private boolean protocolInfoModified = false;
    /* boolean variable to check the condition whether it is found or not */
    private boolean found;
    /* Url for the servlet */
    private String connectionURL = CoeusGuiConstants.CONNECTION_URL;
    /* used in searching for rolodex */
    private static final String ROLODEX_SEARCH = "rolodexSearch";
    /* used to specify to search in person table */
    private static final String PERSON_SEARCH = "personSearch";
    /* used to display title in the search window */
    private static final String DISPLAY_TITLE = "DISPLAY ROLODEX";
    
    //holds CoeusMessageResources instance used for reading message Properties.
    private CoeusMessageResources coeusMessageResources;
    //holds the zero count value
    private static final int ZERO_COUNT = 0;

    // Vector that holds the ComboBoxBean for the Affiliation - Senthil AR
    private Vector vecAffiliation;
    private Vector vecRole;
    //The Affiliation ComboBox Senthil AR
    private JComboBox comboBox;
    
    private CoeusUtils coeusUtils = CoeusUtils.getInstance();
    
    // To get the training flag info the person  start
    private boolean isNewRow = false;
    private static final char GET_TRAINIG_INFO = 'a';
    // To get the training flag info the person End
    // Added for COEUSQA-2807 : IACUC_Affiliation_for_study_personnel_needs_separate_code_table - Start
    private static final String ERR_SELECT_AFFILIATION = "iacucProtocol_Affiliation_exceptionCode.1000";
    private static final int AFFILIATION_COLUMN_INDEX = 3;
    // Added for COEUSQA-2807 : IACUC_Affiliation_for_study_personnel_needs_separate_code_table - End
    //Added for COEUSQA-2633 Ability to indicate which procedures IACUC study personnel will perform and training -Start
    private BaseWindowObservable newObservable;   
    private Vector vecPersonResponsible;
    private Vector vecFinalObserverData;
    private Vector vecInvestigatorData;    
    private boolean isInvestigatorExists = false;
    private static final String KEY_PERSON_DATA = "keyPersonData";
    private static final String PERSON_RESP_DATA = "personRespData";
    //Added for COEUSQA-2633 Ability to indicate which procedures IACUC study personnel will perform and training -End

    /** Creates new form <CODE>ProtocolKeyStudyPersonnelForm</CODE>. <p>
     * <I> Default Constructor.</I>
     */
    
    public ProtocolKeyStudyPersonnelForm() {
       
    }

    /** Constructor that instantiate ProtocolKeyStudyPersonnelForm and populate the component with specified data.
     * And sets the enabled status for all components depending on the functionType.
     * @param keyStudyPersonnelData a Vector which consists of all the KeyStudyPersonnel details
     *
     * @param functionType is a Char variable which specifies the mode in which the
     * form will be displayed.
     * <B>'A'</B> specifies that the form is in Add Mode
     * <B>'M'</B> specifies that the form is in Modify Mode
     * <B>'D'</B> specifies that the form is in Display Mode
     */
    
    public ProtocolKeyStudyPersonnelForm(char functionType,
        java.util.Vector keyStudyPersonnelData, Vector vecAffiliation, Vector vecRole) {
            protocolKeyStudyPersonnelData = new Vector();
            this.protocolKeyStudyPersonnelData = keyStudyPersonnelData;
            this.functionType = functionType;
            this.vecAffiliation = vecAffiliation;
            this.vecRole = vecRole;
            //Added for COEUSQA-2633 Ability to indicate which procedures IACUC study personnel will perform and training -Start
            newObservable  = new BaseWindowObservable();
            //Added for COEUSQA-2633 Ability to indicate which procedures IACUC study personnel will perform and training -End
    }

    /** This Method is used to get the functionType
     * @return a fuctionType like 'A','D','M'.
     */
    
    public char getFunctionType(){
        return functionType;
    }

    /** This Method is used to set the functionType
     * @param fType is a Char data like 'A','D','M'.
     */
    
    public void setFunctionType(char fType){
        this.functionType = fType;
    }

    /** This Method to set the Form data.
     * @param KeyStudyPersonnelData is a Vector KeyStudyPersonnelBeans
     */
    
    public void setKeyStudyPersonnelData(Vector KeyStudyPersonnelData){
       
        int selectedRow=0;
        if(tblKeyStPer.getRowCount()>0)
        {
            selectedRow=tblKeyStPer.getSelectedRow();
        }
        
        this.protocolKeyStudyPersonnelData = KeyStudyPersonnelData;
        //code commented for coeus4.3 concurrent Amendments/Renewals enhancement
//        if(tblKeyStPer.getRowCount()>0)
//            tblKeyStPer.setRowSelectionInterval(selectedRow, selectedRow);
        
        //code added for coeus4.3 concurrent Amendments/Renewals enhancement - starts
        //To refresh the keypersons tab with new datas.
        setFormData();
        setTableEditors();
        ((DefaultTableModel)tblKeyStPer.getModel()).fireTableDataChanged();
        if(tblKeyStPer.getRowCount()>0 && tblKeyStPer.getRowCount()>selectedRow){
            tblKeyStPer.setRowSelectionInterval(selectedRow, selectedRow);
        } else if (tblKeyStPer.getRowCount()>0){
            tblKeyStPer.setRowSelectionInterval(0, 0);                        
            btnDelete.setEnabled(true);                  
        }else{
            btnDelete.setEnabled(false);   
        }
        //code added for coeus4.3 concurrent Amendments/Renewals enhancement - starts
    }

     /** This method is used to initialize the components, set the data in the components.
      * This method is invoked in the <CODE>ProtocolDetailForm</CODE>.
      * @param mdiForm is a reference of CoeusAppletMDIForm
      * @return a JPanel containing all the components with the data populated.
      */
    
    public JComponent showProtocolKeyStudyPersonnelForm(CoeusAppletMDIForm
    mdiForm){
        
        this.mdiReference = mdiForm;
        initComponents();
        // This method enable or disable the JButtons depending on the functionType 
        formatFields();
        setFormData();
        setTableEditors();
        /* This logic is used to select the first row in the list of available 
           rows in JTable*/
        if( tblKeyStPer!=null && tblKeyStPer.getRowCount() > ZERO_COUNT ){
            tblKeyStPer.setRowSelectionInterval(ZERO_COUNT,ZERO_COUNT);
        }else{
            btnDelete.setEnabled(false);
        }
        // setting bold property for table header values
        tblKeyStPer.getTableHeader().setFont(CoeusFontFactory.getLabelFont());
        //Added mouselistener here to avoid adding listeners multiple times
        tblKeyStPer.addMouseListener(new MouseAdapter(){
            public void mouseClicked(MouseEvent me){
                int selRow = tblKeyStPer.getSelectedRow();
                boolean nonEmployee=((Boolean)tblKeyStPer.getModel().getValueAt(selRow,5)).booleanValue();
                String stId=(String)tblKeyStPer.getModel().getValueAt(selRow,4);
                if (me.getClickCount() == 2) {
                    if((stId != null) && (stId.trim().length()>0 )){
                        if(nonEmployee){
                            RolodexMaintenanceDetailForm frmRolodex =
                                    new RolodexMaintenanceDetailForm('V',stId);
                            frmRolodex.showForm(mdiReference,DISPLAY_TITLE,true);
                        }else if(!nonEmployee){
                            try{
                                String loginUserName = mdiReference.getUserName();
                                PersonDetailForm personDetailForm = new PersonDetailForm(stId ,
                                        loginUserName, CoeusGuiConstants.DISPLAY_MODE);
                            }catch(Exception exception){
                                exception.printStackTrace();
                            }
                        }
                    }
                }
            }
        });
        coeusMessageResources = CoeusMessageResources.getInstance();
        return this;
    }

    /** Method to set the data in the JTable.
     * This method sets the data which is available in protocolKeyStudyPersonnelData
     * Vector to JTable.
     */
    
    public void setFormData(){
        
        Vector vcDataPopulate = new Vector();
        Vector vcData = null;
        if((protocolKeyStudyPersonnelData != null) &&
                        (protocolKeyStudyPersonnelData.size()>0)){
            int keyPersonnelSize = protocolKeyStudyPersonnelData.size();
            for(int inCtrdata=0;inCtrdata < keyPersonnelSize;inCtrdata++){
                            
                protocolDataBean=(ProtocolKeyPersonnelBean)
                                      protocolKeyStudyPersonnelData.get(inCtrdata);
                String slno=protocolDataBean.getPersonId();
                String personname=protocolDataBean.getPersonName();
//                String role = protocolDataBean.getPersonRoleCode();
                boolean faculty = protocolDataBean.isFacultyFlag();
                boolean employee = protocolDataBean.isNonEmployeeFlag();
                vcData= new Vector();
                vcData.addElement(CoeusGuiConstants.EMPTY_STRING);
                vcData.addElement(personname);
                vcData.addElement(protocolDataBean.getPersonRoleDesc());
                //Senthil AR commented to discard the boolean value
                //vcData.addElement(new Boolean(faculty));
                vcData.addElement(protocolDataBean.getAffiliationTypeDescription());
                //ComboBoxBean comboBean = new ComboBoxBean(faculty?"1":"3", faculty?"Faculty":"Affiliate");
                //vcData.addElement(comboBean);
                vcData.addElement(slno);
                vcData.addElement(new Boolean(employee));
                // Add the training flag info for the given person
                vcData.addElement(new Boolean(getTrainingInfo(slno)));
                vcDataPopulate.addElement(vcData);
            }
                //Added for COEUSQA-2633 Ability to indicate which procedures IACUC study personnel will perform and training -Start                 
                notifyObserver(protocolKeyStudyPersonnelData, KEY_PERSON_DATA);
                //Added for COEUSQA-2633 Ability to indicate which procedures IACUC study personnel will perform and training -End
                ((DefaultTableModel)tblKeyStPer.getModel()).setDataVector(
                                            vcDataPopulate,getColumnNames());
        } else {
            ((DefaultTableModel)tblKeyStPer.getModel()).setDataVector(
                                            new Vector(),getColumnNames());
        }        
    }

    /* Method to get all the table data from JTable
       @return Vector, a Vector which consists of ProtocolKeyPersonnelBean's */

    private Vector getTableValues(){
        
        Vector keyValues = new Vector();
        int rowCount = tblKeyStPer.getRowCount();
        ProtocolKeyPersonnelBean pkBean;
        for(int inInd=0; inInd < rowCount ;inInd++){
            String pId=(String)((DefaultTableModel)tblKeyStPer.getModel()).
                                                            getValueAt(inInd,4);
            if(pId != null && pId.trim().length()>0){
                String pName=(String)((DefaultTableModel)
                                    tblKeyStPer.getModel()).getValueAt(inInd,1);
                String pRole=(String)((DefaultTableModel)
                                    tblKeyStPer.getModel()).getValueAt(inInd,2);
                // Commented to convert the Boolean CheckBox to ComboBox list
                /*
                 boolean flag = ((Boolean)((DefaultTableModel)
                                    tblKeyStPer.getModel()).
                                            getValueAt(inInd,3)).booleanValue();
                 */
                /*ComboBoxBean faculty  = (ComboBoxBean)((DefaultTableModel)
                                    tblKeyStPer.getModel()).getValueAt(inInd,3);*/
                String affDesc = (String)((DefaultTableModel)tblKeyStPer.getModel()).getValueAt(inInd,3);
                boolean empFlag = ((Boolean)((DefaultTableModel)
                                    tblKeyStPer.getModel()).
                                            getValueAt(inInd,5)).booleanValue();
                pkBean= new ProtocolKeyPersonnelBean();
                pkBean.setPersonName(pName);
//                if(pRole!= null && !pRole.equals(CoeusGuiConstants.EMPTY_STRING)){
//                    pkBean.setPersonRole( (pRole.trim().length() == 0) ? null : pRole );
//                }else{
//                    pkBean.setPersonRole( null );
//                }
                String roleDesc = (String)((DefaultTableModel)tblKeyStPer.getModel()).getValueAt(inInd,2);
                pkBean.setPersonRoleCode(getPersonRoleId(roleDesc));
                //pkBean.setAffiliationTypeCode(Integer.parseInt(faculty.getCode()));
                
                pkBean.setAffiliationTypeCode(getAffiliationId( affDesc ) );
                pkBean.setNonEmployeeFlag(empFlag);
                pkBean.setPersonId(pId);
                // Add the training flag info for the given person
                pkBean.setTrainingFlag(getTrainingInfo(pId));
                keyValues.addElement(pkBean);
            }else{
                CoeusOptionPane.showErrorDialog(
                    coeusMessageResources.parseMessageKey(
                        "protoKeyStPsnlFrm_exceptionCode.1067"));
                tblKeyStPer.requestFocus();
            }
        }
        //Added for COEUSQA-2633 Ability to indicate which procedures IACUC study personnel will perform and training -Start
        if(keyValues != null && keyValues.size()>0){
        notifyObserver(keyValues, KEY_PERSON_DATA);
        }
        //Added for COEUSQA-2633 Ability to indicate which procedures IACUC study personnel will perform and training -End
        return keyValues;
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
    
    public Vector getRoleDescriptions(){
        Vector vecDescription = new Vector();
        if(vecRole != null){
            int listSize = vecRole.size();
            for(int index = 0; index < listSize; index ++){
                vecDescription.addElement(((ComboBoxBean)vecRole.elementAt(index)).toString());
            }
        }
        return vecDescription;
    }
    
    
    private int getPersonRoleId( String desc ) {
        ComboBoxBean bean;
        for( int indx = 0; indx < vecRole.size(); indx++) {
            bean = (ComboBoxBean) vecRole.get(indx);
            if( bean.getDescription().equalsIgnoreCase( desc ) ) {
                return Integer.parseInt( bean.getCode() );
            }
        }
        return 0;
    }
    
    private int getAffiliationId( String desc ) {
        ComboBoxBean bean;
        for( int indx = 0; indx < vecAffiliation.size(); indx++) {
            bean = (ComboBoxBean) vecAffiliation.get(indx);
            if( bean.getDescription().equalsIgnoreCase( desc ) ) {
                return Integer.parseInt( bean.getCode() );
            }
        }
        return 0;
    }
    
    /** Method to get the funding source data in a Vector
     * which consists of ProtocolKeyPersonnelBeans.
     * It sets the AcType as 'U' to the bean if any bean data is modified.
     * It sets the AcType to 'i' if any data is inserted into JTable by the user.
     * It sets the AcType to 'D' if any data is deleted from JTable by the user.
     * @return Vector of bean data.
     */

    public java.util.Vector getKeyStudyPersonnelData(){
        /* This block of code is used to set AcType as D to all the beans 
           if all the rows are deleted in the JTable and it sets the 
           protocolInfoModified flag to true stating that the save is required 
           for the user */
        if((protocolKeyStudyPersonnelData != null) &&
           (protocolKeyStudyPersonnelData.size()>0) &&
           (tblKeyStPer.getRowCount()<=0)){
            int noOfKeyPersons = protocolKeyStudyPersonnelData.size();
            for(int oldIndex = 0;oldIndex < noOfKeyPersons;oldIndex++){
                protocolOldDataBean = (ProtocolKeyPersonnelBean)
                protocolKeyStudyPersonnelData.elementAt(oldIndex);
                protocolOldDataBean.setAcType("D");
                setSaveRequired(true);
                if(functionType == 'M'){
                    protocolInfoModified = true;
                }
                protocolKeyStudyPersonnelData.
                    setElementAt(protocolOldDataBean,oldIndex);
            }
            return protocolKeyStudyPersonnelData;
        }
        /* This gets all the data from the JTable*/
        Vector newData = getTableValues();
        if((newData != null) && (newData.size() > 0)){
            if( functionType == CoeusGuiConstants.AMEND_MODE 
                    || functionType == CoeusGuiConstants.ADD_MODE) {   
                return setAcTypeInAmend( newData );
            }
            
            int dataSize = newData.size();
            for(int newLocIndex = 0; newLocIndex < dataSize;newLocIndex++){
                int foundIndex = -1;
                found = false;
                protocolNewDataBean = (ProtocolKeyPersonnelBean)newData.
                                                        elementAt(newLocIndex);
                if(protocolKeyStudyPersonnelData != null &&
                    protocolKeyStudyPersonnelData.size() > 0){
                      int noOfKeyPersons = protocolKeyStudyPersonnelData.size();
                    for(int oldLocIndex = 0;oldLocIndex < noOfKeyPersons;
                     oldLocIndex++){
                        protocolOldDataBean = (ProtocolKeyPersonnelBean)
                        protocolKeyStudyPersonnelData.elementAt(oldLocIndex);
                        protocolOldDataBean.addPropertyChangeListener(
                        
                        new PropertyChangeListener(){
                            public void propertyChange(
                            PropertyChangeEvent pce){
                                /*System.out.println("prop Name:"+pce.getPropertyName());
                                System.out.println("oldValue:"+pce.getOldValue() );
                                System.out.println("newValue:"+pce.getNewValue());*/
                                if ( pce.getNewValue() == null && pce.getOldValue() != null ) {
                                    protocolOldDataBean.setAcType("U");
                                    setSaveRequired(true);
                                    if(functionType == 'M'){
                                        protocolInfoModified = true;
                                    }
                                }
                                if( pce.getNewValue() != null && pce.getOldValue() == null ) {
                                    protocolOldDataBean.setAcType("U");
                                    setSaveRequired(true);
                                    if(functionType == 'M'){
                                        protocolInfoModified = true;
                                    }
                                }
                                if( pce.getNewValue()!=null && pce.getOldValue()!=null ) {
                                    if (!(  pce.getNewValue().toString().trim().equalsIgnoreCase(pce.getOldValue().toString().trim())))  {
                                        protocolOldDataBean.setAcType("U");
                                        setSaveRequired(true);
                                        if(functionType == 'M'){
                                            protocolInfoModified = true;
                                        }
                                    }
                                }
                                
                                
                                
                            }
                        });
                        if(protocolNewDataBean.getPersonName().equals(
                                    protocolOldDataBean.getPersonName())){
                                        
                            found = true;
                            foundIndex = oldLocIndex;
                            break;
                        }
                    }
                }else{
                    protocolKeyStudyPersonnelData = new Vector();
                }
                if(!found){
                    
                    //if location is new set AcType to INSERT_RECORD
                    protocolNewDataBean.setAcType("I");
                    setSaveRequired(true);
                    if(functionType == 'M'){
                        protocolInfoModified = true;
                    }
                    protocolKeyStudyPersonnelData.addElement(protocolNewDataBean);
                }else{
                        /* if present set the values to the bean. if modified,
                           bean will fire property change event */
                    if(protocolOldDataBean != null){
                        protocolOldDataBean.setPersonId(
                                        protocolNewDataBean.getPersonId());
                        protocolOldDataBean.setPersonName(
                                        protocolNewDataBean.getPersonName());
                        protocolOldDataBean.setPersonRoleCode(
                                        protocolNewDataBean.getPersonRoleCode());
                        //Senthil AR commented out to replace the faculty boolean value with Affilitaion Code
                        //protocolOldDataBean.setFacultyFlag(
                        //                protocolNewDataBean.isFacultyFlag());
                        protocolOldDataBean.setAffiliationTypeCode(
                                        protocolNewDataBean.getAffiliationTypeCode());
                        // Add the training flag info for the given person
                        protocolOldDataBean.setTrainingFlag(getTrainingInfo(protocolNewDataBean.getPersonId()));
                        if(foundIndex != -1){
                            protocolKeyStudyPersonnelData.setElementAt(
                                               protocolOldDataBean,foundIndex);
                        }
                    }
                }
            }
            if(protocolKeyStudyPersonnelData != null 
                && protocolKeyStudyPersonnelData.size() > 0){
                int noOfKeyPersons = protocolKeyStudyPersonnelData.size();
                for(int oldLocIndex = 0; oldLocIndex <noOfKeyPersons;
                        oldLocIndex++){
                            
                    found = false;
                    protocolOldDataBean = (ProtocolKeyPersonnelBean)
                    protocolKeyStudyPersonnelData.elementAt(oldLocIndex);
                    int newDataSize = newData.size();
                    for(int newLocIndex = 0; newLocIndex < newDataSize;
                    newLocIndex++){
                        protocolNewDataBean = (ProtocolKeyPersonnelBean)newData.
                                                        elementAt(newLocIndex);
                        if(protocolOldDataBean.getPersonName().equals(
                                        protocolNewDataBean.getPersonName())){
                                            
                            found = true;
                            break;
                        }
                    }
                    if(!found){
                        
                        protocolOldDataBean.setAcType("D");
                        setSaveRequired(true);
                        if(functionType == 'M'){
                            protocolInfoModified = true;
                        }
                        protocolKeyStudyPersonnelData.setElementAt(
                                            protocolOldDataBean,oldLocIndex);
                    }
                }
            }
        }
        return protocolKeyStudyPersonnelData;
    }

    private Vector setAcTypeInAmend( Vector keyList ) {
        Vector newList = new Vector();
        ProtocolKeyPersonnelBean newBean;
        if( keyList != null ){
            int count = keyList.size();
            for( int indx = 0; indx < count; indx++) {
                newBean = ( ProtocolKeyPersonnelBean ) keyList.get(indx );
                if( newBean.getAcType() == null || !newBean.getAcType().equalsIgnoreCase("D") ) {                
                    newBean.setAcType( "I" );
                    newList.addElement( newBean ); 
                }
            }
        }
        return newList;
        
    }
    
    /**
     * This method is used to get the Column Names of Key study personal
     * table data.
     * @return Vector collection of column names of key study personnel table.
     */
    
    private Vector getColumnNames(){
        Enumeration enumColNames = tblKeyStPer.getColumnModel().getColumns();
        Vector vecColNames = new Vector();
        while(enumColNames.hasMoreElements()){
            String strName = (String)((TableColumn)
            enumColNames.nextElement()).getHeaderValue();
            vecColNames.addElement(strName);
        }
        return vecColNames;
    }

    /*
        This method is used to Enable or Disable the Buttons
        depending on the function Type. If the functionType is Display 
        then it sets the Add, Delete, FindPerson, FindRolodex JButtons to disable.
    */
    
    public void formatFields(){
        
        //code modified for coeus4.3 enhancements that UI to be in display mode
        //when new amendment or renewal is created
//        boolean enabled = functionType !='D' ? true : false;
        boolean enabled = (functionType == CoeusGuiConstants.DISPLAY_MODE 
                || functionType == CoeusGuiConstants.AMEND_MODE) ? false : true;
        btnAdd.setEnabled(enabled);        
        btnFindPerson.setEnabled(enabled);        
        btnFindRolodex.setEnabled(enabled);        
         
        //Added by Amit 11/18/2003
        if(functionType == CoeusGuiConstants.DISPLAY_MODE || 
                functionType == CoeusGuiConstants.AMEND_MODE){
            java.awt.Color bgListColor = (java.awt.Color)javax.swing.UIManager.getDefaults().get("Panel.background");
        
            tblKeyStPer.setBackground(bgListColor);    
            tblKeyStPer.setSelectionBackground(bgListColor );
            tblKeyStPer.setSelectionForeground(Color.black);
            btnDelete.setEnabled(false);
        }
        else{
            tblKeyStPer.setBackground(Color.white);            
            tblKeyStPer.setSelectionBackground(Color.white);
            tblKeyStPer.setSelectionForeground(Color.black); 
            if(tblKeyStPer.getRowCount() > 0){
              btnDelete.setEnabled(true);  
            }
        }
       
        //end Amit
        //Commented mouselistener here to avoid adding listeners multiple times
//        tblKeyStPer.addMouseListener(new MouseAdapter(){
//                public void mouseClicked(MouseEvent me){
////Commented and Modified for Internal_Issue#1798_Rolodex detail window-Multiple "Esc" hits_Start
//                    int selRow = tblKeyStPer.getSelectedRow();
//                    boolean nonEmployee=((Boolean)tblKeyStPer.getModel().getValueAt(selRow,5)).booleanValue();
//                    String stId=(String)tblKeyStPer.getModel().getValueAt(selRow,4);
//                    if (me.getClickCount() == 2) {
//                        if((stId != null) && (stId.trim().length()>0 )){
//                            if(nonEmployee){
//                                RolodexMaintenanceDetailForm frmRolodex =
//                                        new RolodexMaintenanceDetailForm('V',stId);
//                                frmRolodex.showForm(mdiReference,DISPLAY_TITLE,true);
//                            }else if(!nonEmployee){
//                                try{
//                                    String loginUserName = mdiReference.getUserName();
//                                    
//                                    //Bug Fix: Pass the person id to get the person details Start 1
//                                    //String personName=(String)tblKeyStPer.getValueAt(selRow,1);
//                                    /*to get the person details using the person id instead of the person name*/
//                                    //PersonInfoFormBean personInfoFormBean = (PersonInfoFormBean)coeusUtils.getPersonInfoID(personName);
//                                    //PersonDetailForm personDetailForm =
//                                    //new PersonDetailForm(personInfoFormBean.getPersonID(),loginUserName,'D');
//                                    
//                                    PersonDetailForm personDetailForm = new PersonDetailForm(stId ,
//                                            loginUserName, CoeusGuiConstants.DISPLAY_MODE);
//                                    //Bug Fix: Pass the person id to get the person details End 1
//                                }catch(Exception exception){
//                                    exception.printStackTrace();
//                                }
//                            }
//                        }
//                    }
//                  if(me.getClickCount() == 2) {
//                      int selRow = tblKeyStPer.getSelectedRow();
//                      boolean nonEmployee=((Boolean)tblKeyStPer.getModel().
//                                       getValueAt(selRow,5)).booleanValue();
//                      String stId=(String)tblKeyStPer.
//                                        getModel().getValueAt(selRow,4);
//                      if ( stId == null && stId.trim().length() <= 0 ){
//                          CoeusOptionPane.showErrorDialog(
//                                coeusMessageResources.parseMessageKey(
//                                    "protoKeyStPsnlFrm_exceptionCode.1069"));
//                      }else if((nonEmployee) && (stId!=null)){
//
//                          RolodexMaintenanceDetailForm frmRolodex =
//                                 new RolodexMaintenanceDetailForm('V',stId);
//                          frmRolodex.showForm(mdiReference,DISPLAY_TITLE,true);
//
//                      }else if(!nonEmployee){
//                          try{
//                              String loginUserName = mdiReference.getUserName();
//                              
//                              //Bug Fix: Pass the person id to get the person details Start 1
//                              //String personName=(String)tblKeyStPer.getValueAt(selRow,1);
//                              /*to get the person details using the person id instead of the person name*/
//                              //PersonInfoFormBean personInfoFormBean = (PersonInfoFormBean)coeusUtils.getPersonInfoID(personName);
//                              //PersonDetailForm personDetailForm = 
//                                //new PersonDetailForm(personInfoFormBean.getPersonID(),loginUserName,'D');
//                              
//                              PersonDetailForm personDetailForm = new PersonDetailForm(stId ,
//                                      loginUserName, CoeusGuiConstants.DISPLAY_MODE);
//                              //Bug Fix: Pass the person id to get the person details End 1
//                          }catch(Exception exception){
//                              exception.printStackTrace();
//                          }
//                      }
//                    }
//Commented and Modified for Internal_Issue#1798_Rolodex detail window-Multiple "Esc" hits_End                    
//                }
//            });
    }

    /** This method is used for validations.
     * After adding a row,user must enter a key study personnel without keeping it blank.
     * @return true if the validation succeed, false otherwise.
     * @throws Exception a exception to be thrown in the client side.
     */
    
    public boolean validateFormData() throws Exception, CoeusUIException{
        
        //COEUSQA-2807 - IACUC- Affiliation for study personnel needs a separate code table 
         if(tblKeyStPer.isEditing()){
            if(tblKeyStPer.getCellEditor() != null){
                tblKeyStPer.getCellEditor().stopCellEditing();
            }
        }
        
        boolean valid=true;
        int rowCount = tblKeyStPer.getRowCount();
        for(int inInd=0; inInd < rowCount ;inInd++){
            String stPersonId=(String)((DefaultTableModel)
                                        tblKeyStPer.getModel()).
                                                getValueAt(inInd,4);
            if((stPersonId == null) || (stPersonId.trim().length() <= 0)){
                valid=false;
                break;
            }
        }
        if(!valid){
            errorMessage(coeusMessageResources.parseMessageKey(
                    "protoKeyStPsnlFrm_exceptionCode.1140"));
            tblKeyStPer.requestFocus();
            return false;
        }
        // Added for COEUSQA-2807 : IACUC_Affiliation_for_study_personnel_needs_separate_code_table - Start
        for(int inInd=0; inInd < rowCount ;inInd++){
            String affiliationType = (String)((DefaultTableModel)tblKeyStPer.getModel()).getValueAt(inInd,AFFILIATION_COLUMN_INDEX);
            if(affiliationType == null || CoeusGuiConstants.EMPTY_STRING.equals(affiliationType)){
                tblKeyStPer.setRowSelectionInterval(inInd,inInd);
                errorMessage(coeusMessageResources.parseMessageKey(ERR_SELECT_AFFILIATION));
                return false;
                
            }
        }
        // Added for COEUSQA-2807 : IACUC_Affiliation_for_study_personnel_needs_separate_code_table - End
        return true;
    }

    /* This method is used to set the cell editors to the columns, 
       set the column width to each individual column, disable the column 
       resizable feature to the JTable, setting single selection mode to the 
       JTable */
    
    private void setTableEditors(){
        tblKeyStPer.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        TableColumn column = tblKeyStPer.getColumnModel().getColumn(0);
        column.setMinWidth(30);
        column.setMaxWidth(30);
        column.setHeaderRenderer(new EmptyHeaderRenderer());
        
        column.setResizable(false);
        column.setCellRenderer(new IconRenderer());
        column.setPreferredWidth(30);
        JTableHeader header = tblKeyStPer.getTableHeader();
        header.setReorderingAllowed(false);
        //header.setResizingAllowed(false);
        tblKeyStPer.setRowHeight(24);

        tblKeyStPer.setOpaque(false);
        tblKeyStPer.setShowVerticalLines(false);
        tblKeyStPer.setShowHorizontalLines(false);
        tblKeyStPer.setSelectionMode(
                            DefaultListSelectionModel.SINGLE_SELECTION);
        column = tblKeyStPer.getColumnModel().getColumn(4);
        column.setMinWidth(0);
        column.setMaxWidth(0);
        column.setPreferredWidth(0);

        column = tblKeyStPer.getColumnModel().getColumn(5);
        column.setMinWidth(0);
        column.setMaxWidth(0);
        column.setPreferredWidth(0);
        // Include the training flag column start
        column = tblKeyStPer.getColumnModel().getColumn(6);
        column.setPreferredWidth(75);
        column.setCellRenderer(new ImageRenderer());
        // Include the training flag column end

        tblKeyStPer.setRowHeight(20);
        NameEditor nmEdtName = new NameEditor( "Name", 90 );
        column = tblKeyStPer.getColumnModel().getColumn(1);
        column.setMinWidth(195);
        //column.setMaxWidth(220);
        //column.setPreferredWidth(100);
        //column.setResizable(false);
        column.setCellEditor( nmEdtName );

//        NameEditor nmEdtRole = new NameEditor( "Role", 60 );
//        column = tblKeyStPer.getColumnModel().getColumn(2);
//        column.setMinWidth(130);
//        //column.setMaxWidth(120);
//        //column.setPreferredWidth(80);
//        //column.setResizable(false);
//        column.setCellEditor( nmEdtRole );
        
        column = tblKeyStPer.getColumnModel().getColumn(2);
        if ( functionType != CoeusGuiConstants.DISPLAY_MODE ) {
            comboBox = new JComboBox(getRoleDescriptions());
            comboBox.addItemListener(new ItemListener(){
                public void itemStateChanged(ItemEvent ie){
                    if(ie.getStateChange() == ItemEvent.SELECTED) {
                        saveRequired = true;
                    }
                }
            });
            comboBox.setFont(CoeusFontFactory.getNormalFont());
            column.setCellEditor(new DefaultCellEditor(comboBox ));
        }
        column.setMinWidth(150);
        
        column = tblKeyStPer.getColumnModel().getColumn(3);
        if ( functionType != CoeusGuiConstants.DISPLAY_MODE ) {
            comboBox = new JComboBox(getDescriptions());
            comboBox.addItemListener(new ItemListener(){
                public void itemStateChanged(ItemEvent ie){
                    if(ie.getStateChange() == ItemEvent.SELECTED) {
                        saveRequired = true;
                    }
                }
            });
            comboBox.setFont(CoeusFontFactory.getNormalFont());
            column.setCellEditor(new DefaultCellEditor(comboBox ));
            
        }
       column.setMinWidth(120);

    }

    /** This method is used to determine whether the data to be saved or not.
     * @return true if the modifications done, false otherwise.
     */
    
    public boolean isSaveRequired(){
        return saveRequired;
    }
    
    //Added by Amit 11/21/2003
    /** This method use to implement focus on first editable component in this page.
     */
    public void setDefaultFocusForComponent(){
        if(!( functionType == CoeusGuiConstants.DISPLAY_MODE )) {          
            if(tblKeyStPer .getRowCount() > 0 ) {
                tblKeyStPer.requestFocusInWindow();
                
                //included by raghu to remain the selection on row upon selection...
                //starts..
                int prevSelectedRow=tblKeyStPer.getSelectedRow();
                if(prevSelectedRow!=-1){
                    tblKeyStPer.setRowSelectionInterval(prevSelectedRow, prevSelectedRow);
                }
                else{
                    tblKeyStPer.setRowSelectionInterval(0, 0);
                }
                //ends
                
                tblKeyStPer.setColumnSelectionInterval(1,1);
                tblKeyStPer.setColumnSelectionInterval(1,1);
            }else{
                btnAdd.requestFocusInWindow();
            }            
        }
    }    
    //end Amit    

    /** This method is used to determine whether the protocol information is
     * modified.
     * @return true if the modifications done, false otherwise.
     */
    
    public boolean isProtocolInfoModified(){
        return protocolInfoModified;
    }

    /** This method is used to set true or false to the saveRequired member variable
     * @param save is a boolean variable to be set to saveRequired variable.
     */
    
    public void setSaveRequired(boolean save){
        this.saveRequired = save;
    }

   /** This method is called from within the showProtocolKeyStudyPersonnelForm 
     *  method to initialize the form.
     */
    
    private void initComponents() {
        
        java.awt.GridBagConstraints gridBagConstraints;
        btnAdd = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        btnFindPerson = new javax.swing.JButton();
        btnFindRolodex = new javax.swing.JButton();
        scrPnPane1 = new javax.swing.JScrollPane();
        tblKeyStPer = new javax.swing.JTable();
        //tblKeyStPer.setSelectionBackground( Color.white );
        //tblKeyStPer.setSelectionForeground( Color.black );
        
        setLayout(new java.awt.GridBagLayout());
        //setPreferredSize( new java.awt.Dimension( 600, 250 ));
        // Added by chandra - 28/8/2003 to increase the size of the scrollpane.
        setPreferredSize( new java.awt.Dimension(725, 415 ));
        //setBorder(new javax.swing.border.EtchedBorder());
        btnAdd.setFont(CoeusFontFactory.getLabelFont());
        btnAdd.setPreferredSize(new java.awt.Dimension(110, 25));
        btnAdd.setText("Add");
        btnAdd.setMnemonic('A');
        /* If the user presses Add button an enpty row is created in the 
           JTable and the the new row added will be selected */
        btnAdd.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent ae){
                if (tblKeyStPer.getModel() instanceof DefaultTableModel) {
                    /* add a new row in table and make it as selected row */
                    // change needed by raghu
                    // Added the ComboBoxBean for the Affiliation - Senthil AR
                    //ComboBoxBean comboBean = new ComboBoxBean("3", "Affiliation");
                    // Commented for COEUSQA-2807 : IACUC_Affiliation_for_study_personnel_needs_separate_code_table - Start
//                    ((DefaultTableModel)tblKeyStPer.getModel()).addRow(
//                        new Object[]{"","","","Affiliate","",new Boolean(false)});
                    ((DefaultTableModel)tblKeyStPer.getModel()).addRow(
                            new Object[]{"","","","","",new Boolean(false)});
                    // Commented for COEUSQA-2807 : IACUC_Affiliation_for_study_personnel_needs_separate_code_table - End
                    ((DefaultTableModel)tblKeyStPer.getModel()).fireTableDataChanged();

                    int lastRow = tblKeyStPer.getRowCount() - 1;
                    if(lastRow >= 0){
                        btnDelete.setEnabled(true);
                        tblKeyStPer.setRowSelectionInterval(lastRow,lastRow );
                        tblKeyStPer.scrollRectToVisible( 
                            tblKeyStPer.getCellRect(lastRow,ZERO_COUNT, true));
                    }
                    tblKeyStPer.requestFocusInWindow();
                    tblKeyStPer.editCellAt(lastRow,1);
                    tblKeyStPer.getEditorComponent().requestFocusInWindow();
                    //COEUSQA-2807 - IACUC- Affiliation for study personnel needs a separate code table 
                     ((DefaultTableModel)tblKeyStPer.getModel()).setValueAt(CoeusGuiConstants.EMPTY_STRING, lastRow, 3);
                     isNewRow = true;
                    setSaveRequired(true);
                    scrPnPane1.setViewportView(tblKeyStPer);
                }
            }
        }); 
        
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 1;
        gridBagConstraints.gridheight = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(27, 0, 5, 15);
        add(btnAdd, gridBagConstraints);

        btnDelete.setFont(CoeusFontFactory.getLabelFont());
        btnDelete.setText("Delete");
        btnDelete.setMnemonic('D');
        btnDelete.setPreferredSize(new java.awt.Dimension(110, 25));
        btnDelete.setMaximumSize(new java.awt.Dimension(110, 25));
        btnDelete.setMinimumSize(new java.awt.Dimension(110, 25));
        /* If the user presses Delete button the selected row will be deleted 
           and the focus will be on the row which is prior to the deleted row */
        btnDelete.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                int totalRows = tblKeyStPer.getRowCount();
                /* If there are more than one row in table then delete it */
                if (totalRows > 0) {
                    /* get the selected row */
                    int selectedRow = tblKeyStPer.getSelectedRow();
                    if (selectedRow != -1) {                      
                     //Added for COEUSQA-2633 Ability to indicate which procedures IACUC study personnel will perform and training -Start
                     boolean isDeletePersonResponse = false;
                     String message = CoeusGuiConstants.EMPTY_STRING;
                     String personId = (String)tblKeyStPer.getValueAt(
                                                                selectedRow,4);  
                     if(isPersonResponsePersentForPerson(personId) && !isInvestigatorExists){
                         isDeletePersonResponse = true;                       
                     }
                     if(isDeletePersonResponse){
                        message =  "protoKeyStPsnlFrm_delConfirmCode.1142";
                     }else{
                         message = "protoKeyStPsnlFrm_delConfirmCode.1141";
                     }
                     //Added for COEUSQA-2633 Ability to indicate which procedures IACUC study personnel will perform and training -End
                        int selectedOption = CoeusOptionPane.
                                            showQuestionDialog(
                                            coeusMessageResources.parseMessageKey(
                                            message),
                                            CoeusOptionPane.OPTION_YES_NO, 
                                            CoeusOptionPane.DEFAULT_YES);
                        // if Yes then selectedOption is 0
                        // if No then selectedOption is 1
                        if (0 == selectedOption) {
                            ((DefaultTableModel)
                            tblKeyStPer.getModel()).removeRow(selectedRow);
                            ((DefaultTableModel)
                            tblKeyStPer.getModel()).fireTableDataChanged();
                            saveRequired = true;
                            tblKeyStPer.clearSelection();
                            //Added for COEUSQA-2633 Ability to indicate which procedures IACUC study personnel will perform and training -Start
                            Vector vecObserverData = new Vector();
                            if(vecFinalObserverData == null || vecFinalObserverData.isEmpty()){
                                vecFinalObserverData = new Vector();
                            }
                            vecObserverData.addAll(vecFinalObserverData);
                            vecObserverData.remove(selectedRow);
                            notifyObserver(vecObserverData, KEY_PERSON_DATA);
                            if(isDeletePersonResponse){
                            Vector vecDeletedPersonId = new Vector();
                            vecDeletedPersonId.add(personId);
                            notifyObserver(vecDeletedPersonId, PERSON_RESP_DATA); 
                        }
                            //Added for COEUSQA-2633 Ability to indicate which procedures IACUC study personnel will perform and training -End
                            // find out again row count in table
                            int newRowCount = tblKeyStPer.getRowCount();
                            if(newRowCount == 0){
                                btnAdd.requestFocusInWindow();
                                btnDelete.setEnabled(false);
                            }else{
                                // select the next row if exists
                                if (newRowCount > selectedRow) {
                                    (tblKeyStPer.getSelectionModel())
                                    .setSelectionInterval(selectedRow,
                                    selectedRow);
                                } else {
                                    tblKeyStPer.setRowSelectionInterval( 
                                        newRowCount - 1, newRowCount -1 ); 
                                    tblKeyStPer.scrollRectToVisible( 
                                    tblKeyStPer.getCellRect(
                                                    newRowCount - 1 ,
                                                    ZERO_COUNT, true));
                                }
                            }
                        }

                    } // if total rows >0 and row is selected
                    else{
                        // if total rows >0 and row is not selected
                        CoeusOptionPane.
                                    showErrorDialog(
                                        coeusMessageResources.parseMessageKey(
                                            "protoFndSrcFrm_exceptionCode.1057"));
                    }
                }
            }
        });
        
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 1;
        gridBagConstraints.gridheight = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 15);
        add(btnDelete, gridBagConstraints);

        btnFindPerson.setFont(CoeusFontFactory.getLabelFont());
        btnFindPerson.setText("Find Person");
        btnFindPerson.setMnemonic('P');
        btnFindPerson.setPreferredSize(new java.awt.Dimension(110, 25));
        btnFindPerson.setMaximumSize(new java.awt.Dimension(110, 25));
        btnFindPerson.setMinimumSize(new java.awt.Dimension(110, 25));
        btnFindPerson.addActionListener(new ActionListener(){
            public void actionPerformed(java.awt.event.ActionEvent evt){
                try{
                    /**
                     * Updated For : REF ID 149 Feb' 14/18 2003
                     * Person Search allows for multiple entries, however, 
                     * the user can only add 1 at a time
                     *
                     * Updated by Subramanya Feb' 19 2003
                     */
                    int inIndex=tblKeyStPer.getSelectedRow();
                    
                    if(tblKeyStPer.isEditing()){
                        java.awt.Component comp = tblKeyStPer.getEditorComponent();
                        String value = null;
                        if (comp instanceof javax.swing.text.JTextComponent) {
                            value = ((javax.swing.text.JTextComponent)comp).getText();
                        }
                        else if (comp instanceof javax.swing.JComboBox) {
                            //value = ((javax.swing.JComboBox)comp).getSelectedItem().toString();
                            value = CoeusGuiConstants.EMPTY_STRING;
                        }
                        
                        if( (value != null)){
                            tblKeyStPer.setValueAt(value,inIndex,1);
                        }
                        tblKeyStPer.getCellEditor().cancelCellEditing();
                    }
                    
                    CoeusSearch coeusSearch =
                    new CoeusSearch(mdiReference, PERSON_SEARCH, 
                    CoeusSearch.TWO_TABS_WITH_MULTIPLE_SELECTION ); //1);
                    coeusSearch.showSearchWindow();

                    Vector vSelectedPersons = coeusSearch.getMultipleSelectedRows(); 
                    if( vSelectedPersons != null ){                                            
                    HashMap singlePersonData = null;
                    for(int indx = 0; indx < vSelectedPersons.size(); indx++ ){
                    
                    singlePersonData = (HashMap)vSelectedPersons.get( indx ) ;             
                    //HashMap personInfo = coeusSearch.getSelectedRow();
                    //if(personInfo!=null){
                    if( singlePersonData !=null ){
                        isNewRow = false;
                        /* construct the full name of person */
                        String personID = Utils.
                            convertNull(singlePersonData.get( "PERSON_ID" ));//personInfo.get("PERSON_ID"));
//                        String role = Utils.
//                            convertNull(singlePersonData.get( "DIRECTORY_TITLE" ));//personInfo.get("DIRECTORY_TITLE"));
                        String name = Utils.
                            convertNull(singlePersonData.get( "FULL_NAME" ));//personInfo.get("FULL_NAME"));
                        String tmpFaculty = Utils.
                            convertNull(singlePersonData.get( "IS_FACULTY" ));//personInfo.get("IS_FACULTY")).trim();
                        boolean faculty = tmpFaculty.equalsIgnoreCase("y") ? true : false;
                          String role = CoeusGuiConstants.EMPTY_STRING;
                                Vector vecRoleDesc = getRoleDescriptions();
                                if(vecRoleDesc != null && vecRoleDesc.size() > 0){
                                    role = (String)vecRoleDesc.get(0);
                                }
                        boolean duplicate = checkDuplicatePerson(personID);
                        if( tblKeyStPer.getSelectedRow() == -1 ){
                            
                                
                                Vector newKeyStudyEntry = new Vector();
                                newKeyStudyEntry.addElement( CoeusGuiConstants.EMPTY_STRING );
                                newKeyStudyEntry.addElement( name );
                              
                                newKeyStudyEntry.addElement(role);

                                //Senthil AR commented out to replace the boolean with String
                                //newKeyStudyEntry.addElement( new Boolean(faculty));
                                // Commented for COEUSQA-2807 : IACUC_Affiliation_for_study_personnel_needs_separate_code_table - Start                                
//                                newKeyStudyEntry.addElement((faculty)?"Faculty":"Affiliate");
                                newKeyStudyEntry.addElement(CoeusGuiConstants.EMPTY_STRING);
                                // Commented for COEUSQA-2807 : IACUC_Affiliation_for_study_personnel_needs_separate_code_table - Start
                                //newKeyStudyEntry.addElement( ((ComboBoxBean)((JComboBox)(tblKeyStPer.getCellEditor(0,3))).getSelectedItem()));
                                newKeyStudyEntry.addElement( personID );
                                newKeyStudyEntry.addElement( new Boolean(false) );
                                // Add the training flag info for the given person
                                newKeyStudyEntry.addElement(new Boolean(getTrainingInfo(personID)));
                                
                                ((DefaultTableModel)tblKeyStPer.getModel()
                                ).addRow( newKeyStudyEntry );
                                ((DefaultTableModel)
                                tblKeyStPer.getModel()).
                                                    fireTableDataChanged();
                                tblKeyStPer.setRowSelectionInterval(0,0);                                
                                btnDelete.setEnabled(true); 
                                //Added for COEUSQA-2633 Ability to indicate which procedures IACUC study personnel will perform and training -Start
                                Vector vecObserverData = new Vector();
                                if(vecFinalObserverData != null && !vecFinalObserverData.isEmpty()){
                                    vecObserverData = vecFinalObserverData;
                                }
                                ProtocolKeyPersonnelBean keyPersonnelBean = new ProtocolKeyPersonnelBean();
                                keyPersonnelBean.setPersonId(personID);
                                keyPersonnelBean.setPersonName(name);
                                keyPersonnelBean.setTrainingFlag((Boolean)newKeyStudyEntry.get(6));
                                vecObserverData.add(keyPersonnelBean);
                                notifyObserver(vecObserverData, KEY_PERSON_DATA);
                                //Added for COEUSQA-2633 Ability to indicate which procedures IACUC study personnel will perform and training -End
                                saveRequired=true;               
                                inIndex = 0;
                                continue;
                        }
                        
                        String stPersonId = ( String)
                                ((DefaultTableModel)tblKeyStPer.
                                        getModel()).getValueAt(inIndex,4);
                        
                        Vector vFundInfo=null;
                        if(stPersonId != null && stPersonId.trim().length() >0){
                            if(!duplicate){
                                vFundInfo = new Vector();
                                vFundInfo.addElement( CoeusGuiConstants.EMPTY_STRING );
                                vFundInfo.addElement( name );
                                vFundInfo.addElement( role );
                                //Senthil AR commented and added the new code
                                //vFundInfo.addElement( new Boolean(faculty));
                                //ComboBoxBean comboBean = new ComboBoxBean(faculty?"1":"3", faculty?"Faculty":"Affiliate");
                                //vFundInfo.addElement( comboBean );
                                // Commented for COEUSQA-2807 : IACUC_Affiliation_for_study_personnel_needs_separate_code_table - Start
//                                vFundInfo.addElement((faculty)?"Faculty":"Affiliate");
                                vFundInfo.addElement(CoeusGuiConstants.EMPTY_STRING);
                                // Commented for COEUSQA-2807 : IACUC_Affiliation_for_study_personnel_needs_separate_code_table - End
                                vFundInfo.addElement( personID );
                                vFundInfo.addElement( new Boolean(false) );
                                // Add the training flag info for the given person
                                vFundInfo.addElement(new Boolean(getTrainingInfo(personID)));

                                ((DefaultTableModel)tblKeyStPer.getModel()).
                                    addRow(vFundInfo);
                                ((DefaultTableModel)tblKeyStPer.getModel()).
                                    fireTableDataChanged();
                                
                                int newRowCount = tblKeyStPer.getRowCount();
                                tblKeyStPer.getSelectionModel().
                                    setSelectionInterval(
                                        newRowCount - 1, newRowCount - 1);
                                
                                //Added for COEUSQA-2633 Ability to indicate which procedures IACUC study personnel will perform and training -Start
                                Vector vecObserverData = new Vector();
                                if(vecFinalObserverData != null && !vecFinalObserverData.isEmpty()){
                                    vecObserverData = vecFinalObserverData;
                                }
                                ProtocolKeyPersonnelBean keyPersonnelBean = new ProtocolKeyPersonnelBean();
                                keyPersonnelBean.setPersonId(personID);
                                keyPersonnelBean.setPersonName(name);
                                keyPersonnelBean.setTrainingFlag((Boolean)vFundInfo.get(6));
                                vecObserverData.add(keyPersonnelBean);
                                notifyObserver(vecObserverData, KEY_PERSON_DATA);
                                //Added for COEUSQA-2633 Ability to indicate which procedures IACUC study personnel will perform and training -End
                                saveRequired = true;
                            }else{
                                CoeusOptionPane.showErrorDialog("'" + name +"' "+ 
                                    coeusMessageResources.parseMessageKey(
                                        "general_duplicateNameCode.2277"));
                            }
                         }else{
                             /* If the entry is not a duplicate then It adds 
                                to the selected row */
                                if(!duplicate){
                                
                                ((DefaultTableModel)tblKeyStPer.getModel()
                                    ).setValueAt(name,inIndex,1);
                                ((DefaultTableModel)tblKeyStPer.getModel()
                                    ).setValueAt(role,inIndex,2);
                                //Senthil AR commented out 
                                // Commented for COEUSQA-2807 : IACUC_Affiliation_for_study_personnel_needs_separate_code_table - Start
//                                ((DefaultTableModel)tblKeyStPer.getModel()).setValueAt((faculty)?"Faculty":"Affiliate",inIndex,3);
                                //((DefaultTableModel)tblKeyStPer.getModel()).setValueAt(CoeusGuiConstants.EMPTY_STRING,inIndex,AFFILIATION_COLUMN_INDEX);                                
                                 // Commented for COEUSQA-2807 : IACUC_Affiliation_for_study_personnel_needs_separate_code_table - End
                                /*ComboBoxBean comboBean = new ComboBoxBean((faculty)?"1":"3", (faculty)?"Faculty":"Affiliate");
                                ((DefaultTableModel)tblKeyStPer.getModel()
                                    ).setValueAt( comboBean, inIndex, 3);*/
                                ((DefaultTableModel)tblKeyStPer.getModel()
                                    ).setValueAt(personID,inIndex,4);
                                ((DefaultTableModel)tblKeyStPer.getModel()
                                    ).setValueAt(new Boolean(false),inIndex,5);
                                ((DefaultTableModel)tblKeyStPer.getModel()
                                    ).setValueAt(personID,inIndex,4);
                                // Add the training flag info for the given person
                                ((DefaultTableModel)tblKeyStPer.getModel()
                                    ).setValueAt(new Boolean(getTrainingInfo(personID)),inIndex,6);
                                ((DefaultTableModel)
                                tblKeyStPer.getModel()).
                                                    fireTableDataChanged();
                                tblKeyStPer.getSelectionModel().
                                                setLeadSelectionIndex(inIndex);
                                //Added for COEUSQA-2633 Ability to indicate which procedures IACUC study personnel will perform and training -Start
                                Vector vecObserverData = new Vector();
                                if(vecFinalObserverData != null && !vecFinalObserverData.isEmpty()){
                                    vecObserverData = vecFinalObserverData;
                                }
                                ProtocolKeyPersonnelBean keyPersonnelBean = new ProtocolKeyPersonnelBean();
                                keyPersonnelBean.setPersonId(personID);
                                keyPersonnelBean.setPersonName(name);
                                keyPersonnelBean.setTrainingFlag((Boolean)tblKeyStPer.getValueAt(inIndex,6));
                                vecObserverData.add(keyPersonnelBean);
                                notifyObserver(vecObserverData, KEY_PERSON_DATA);
                                //Added for COEUSQA-2633 Ability to indicate which procedures IACUC study personnel will perform and training -End
                                saveRequired = true;
                                }else{
                                    CoeusOptionPane.showErrorDialog("'" + name +"' "+ 
                                        coeusMessageResources.parseMessageKey(
                                            "general_duplicateNameCode.2277"));
                                }
                            }
                    }// singlePersonData != null //personInfo !=null
                    }
                    }// end of vSelectedPerson != null
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        });
        
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 1;
        gridBagConstraints.gridheight = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 15);
        add(btnFindPerson, gridBagConstraints);

        btnFindRolodex.setFont(CoeusFontFactory.getLabelFont());
        btnFindRolodex.setText("Find Rolodex");
        btnFindRolodex.setMnemonic('R');
        btnFindRolodex.setPreferredSize(new java.awt.Dimension(110, 25));
        btnFindRolodex.setMaximumSize(new java.awt.Dimension(110, 25));
        btnFindRolodex.setMinimumSize(new java.awt.Dimension(110, 25));
        btnFindRolodex.addActionListener(new ActionListener() {
            
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                
                try{
                    int inIndex=tblKeyStPer.getSelectedRow();
                    if(tblKeyStPer.isEditing()){
                        String value = ((javax.swing.text.JTextComponent)
                        tblKeyStPer.getEditorComponent()).getText();
                        if( (value != null)){
                            tblKeyStPer.setValueAt(value,inIndex,1);
                        }
                        tblKeyStPer.getCellEditor().cancelCellEditing();
                    }
                /**
                 * Updated For : REF ID 149 Feb' 14/19 2003
                 * Person/Rolodex Search allows for multiple entries, however, 
                 * the user can only add 1 at a time
                 *
                 * Updated by Subramanya Feb' 20 2003
                 */
                    CoeusSearch coeusSearch =
                    new CoeusSearch(mdiReference, ROLODEX_SEARCH, 
                     CoeusSearch.TWO_TABS_WITH_MULTIPLE_SELECTION ) ; //1);
                    coeusSearch.showSearchWindow();
                    Vector vSelectedRolodex = coeusSearch.getMultipleSelectedRows(); 
                    if ( vSelectedRolodex != null ){
                    isNewRow = false;
                    HashMap singleRolodexData = null;
                    for(int indx = 0; indx < vSelectedRolodex.size(); indx++ ){
                    
                        singleRolodexData = (HashMap)vSelectedRolodex.get( indx ) ;   
                    
                    //HashMap rolodexInfo = coeusSearch.getSelectedRow();
                    //if(rolodexInfo !=null){
                    if( singleRolodexData !=null){
                        
                        /* construct the full name of person */
                        
                        
                        String rolodexId = Utils.
                            convertNull(singleRolodexData.get("ROLODEX_ID"));//rolodexInfo.get("ROLODEX_ID"));
//                        String role = Utils.
//                            convertNull(singleRolodexData.get("TITLE"));//rolodexInfo.get("TITLE"));
                        String role = CoeusGuiConstants.EMPTY_STRING;
                        Vector vecRoleDesc = getRoleDescriptions();
                        if(vecRoleDesc != null && vecRoleDesc.size() > 0){
                            role = (String)vecRoleDesc.get(0);
                        }
                        String lastName = Utils.
                            convertNull(singleRolodexData.get("LAST_NAME"));//rolodexInfo.get("LAST_NAME"));
                        String firstName = Utils.
                            convertNull(singleRolodexData.get("FIRST_NAME"));//rolodexInfo.get("FIRST_NAME"));
                        String middleName = Utils.
                            convertNull(singleRolodexData.get("MIDDLE_NAME"));//rolodexInfo.get("MIDDLE_NAME"));
                        String suffix = Utils.
                            convertNull(singleRolodexData.get("SUFFIX"));//rolodexInfo.get("SUFFIX"));
                        String prefix = Utils.
                            convertNull(singleRolodexData.get("PREFIX"));//rolodexInfo.get("PREFIX"));
                        String name = lastName+" "+suffix+", "+
                            prefix+" "+firstName+" "+middleName;
                
                        if ( lastName.length() > 0) {
                            name = ( lastName +", " + suffix + " " + 
                                    prefix + " " +
                                    firstName + " " + middleName ).trim();
                        } else {
                            name = Utils.convertNull( 
                                            singleRolodexData.get("ORGANIZATION") );//rolodexInfo.get("ORGANIZATION") );
                        }
                        
                        // use this name variable to display in Jtable
                        boolean duplicate = checkDuplicatePerson(rolodexId);
                        if( tblKeyStPer.getSelectedRow() == -1 ){
                                Vector newKeyStudyEntry = new Vector();
                                newKeyStudyEntry.addElement( CoeusGuiConstants.EMPTY_STRING );
                                newKeyStudyEntry.addElement( name );
                                newKeyStudyEntry.addElement( role );
                                //Senthil AR Commented out
                                //newKeyStudyEntry.addElement(new Boolean(false));
                                //ComboBoxBean comboBean = new ComboBoxBean("3", "Affiliate");
                                //newKeyStudyEntry.addElement(comboBean);
                                // Commented for COEUSQA-2807 : IACUC_Affiliation_for_study_personnel_needs_separate_code_table - Start
//                                newKeyStudyEntry.addElement("Affiliate");
                                newKeyStudyEntry.addElement(CoeusGuiConstants.EMPTY_STRING);                                
                                // Commented for COEUSQA-2807 : IACUC_Affiliation_for_study_personnel_needs_separate_code_table - End
                                newKeyStudyEntry.addElement( rolodexId );
                                newKeyStudyEntry.addElement(new Boolean(true) );
                                // Add the training flag info for the given person
                                newKeyStudyEntry.addElement(new Boolean(getTrainingInfo(rolodexId)));
                                ((DefaultTableModel)tblKeyStPer.getModel()
                                ).addRow( newKeyStudyEntry );

                                ((DefaultTableModel)
                                tblKeyStPer.getModel()).fireTableDataChanged();
                                tblKeyStPer.setRowSelectionInterval(0,0);
                                btnDelete.setEnabled(true);
                                //Added for COEUSQA-2633 Ability to indicate which procedures IACUC study personnel will perform and training -Start
                                Vector vecObserverData = new Vector();
                                if(vecFinalObserverData != null && !vecFinalObserverData.isEmpty()){
                                    vecObserverData = vecFinalObserverData;
                                }
                                ProtocolKeyPersonnelBean keyPersonnelBean = new ProtocolKeyPersonnelBean();
                                keyPersonnelBean.setPersonId(rolodexId);
                                keyPersonnelBean.setPersonName(name);
                                keyPersonnelBean.setTrainingFlag((Boolean)newKeyStudyEntry.get(6));
                                vecObserverData.add(keyPersonnelBean);
                                notifyObserver(vecObserverData, KEY_PERSON_DATA);
                                //Added for COEUSQA-2633 Ability to indicate which procedures IACUC study personnel will perform and training -End
                                saveRequired = true;
                                inIndex = 0;
                                continue;
                        }
                        String stPersonId=( String)
                                ((DefaultTableModel)tblKeyStPer.
                                        getModel()).getValueAt(inIndex,4);
                        Vector vFundInfo=null;
                        if(stPersonId != null && stPersonId.trim().length() >0){
                            if(!duplicate){
                                vFundInfo = new Vector();
                                vFundInfo.addElement( CoeusGuiConstants.EMPTY_STRING );
                                vFundInfo.addElement( name );
                                vFundInfo.addElement( role );
                                //Senthil AR Commented out
                                //vFundInfo.addElement( new Boolean(false) );
                                //ComboBoxBean comboBean = new ComboBoxBean("3", "Affiliate");                                
                                //vFundInfo.addElement( comboBean );
                                // Commented for COEUSQA-2807 : IACUC_Affiliation_for_study_personnel_needs_separate_code_table - Start
//                                vFundInfo.addElement("Affiliate");
                                vFundInfo.addElement(CoeusGuiConstants.EMPTY_STRING);                                
                                // Commented for COEUSQA-2807 : IACUC_Affiliation_for_study_personnel_needs_separate_code_table - End
                                vFundInfo.addElement( rolodexId );
                                vFundInfo.addElement( new Boolean(true) );
                                // Add the training flag info for the given person
                                vFundInfo.addElement(new Boolean(getTrainingInfo(rolodexId)));
                                 isNewRow=false;
                                ((DefaultTableModel)tblKeyStPer.getModel()).
                                    addRow(vFundInfo);
                                ((DefaultTableModel)tblKeyStPer.getModel()).
                                    fireTableDataChanged();

                                int newRowCount = tblKeyStPer.getRowCount();
                                tblKeyStPer.getSelectionModel().
                                    setSelectionInterval(
                                        newRowCount - 1, newRowCount - 1);
                                //Added for COEUSQA-2633 Ability to indicate which procedures IACUC study personnel will perform and training -Start
                                Vector vecObserverData = new Vector();
                                if(vecFinalObserverData != null && !vecFinalObserverData.isEmpty()){
                                    vecObserverData = vecFinalObserverData;
                                }
                                ProtocolKeyPersonnelBean keyPersonnelBean = new ProtocolKeyPersonnelBean();
                                keyPersonnelBean.setPersonId(stPersonId);
                                keyPersonnelBean.setPersonName(name);
                                keyPersonnelBean.setTrainingFlag((Boolean)tblKeyStPer.getValueAt(inIndex,6));
                                vecObserverData.add(keyPersonnelBean);
                                notifyObserver(vecObserverData, KEY_PERSON_DATA);
                                //Added for COEUSQA-2633 Ability to indicate which procedures IACUC study personnel will perform and training -End
                                saveRequired = true;
                            }else{
                                CoeusOptionPane.showErrorDialog("' " + name + "' " +
                                    coeusMessageResources.parseMessageKey(
                                        "general_duplicateNameCode.2277"));
                            }
                         }else{  
                                if(!duplicate){
                                ((DefaultTableModel)tblKeyStPer.getModel()
                                    ).setValueAt(name,inIndex,1);
                                ((DefaultTableModel)tblKeyStPer.getModel()
                                    ).setValueAt(role,inIndex,2);
                                //Commented out -- Senthil AR
                                // Commented for COEUSQA-2807 : IACUC_Affiliation_for_study_personnel_needs_separate_code_table - Start                                
//                                ((DefaultTableModel)tblKeyStPer.getModel()
//                                    ).setValueAt("Affiliate",inIndex,3);
                                //((DefaultTableModel)tblKeyStPer.getModel()).setValueAt(CoeusGuiConstants.EMPTY_STRING,inIndex,AFFILIATION_COLUMN_INDEX);
                                 // Commented for COEUSQA-2807 : IACUC_Affiliation_for_study_personnel_needs_separate_code_table - End
                                /*ComboBoxBean comboBean = new ComboBoxBean("3", "Affiliate");
                                ((DefaultTableModel)tblKeyStPer.getModel()
                                    ).setValueAt(comboBean,inIndex,3);*/
                                ((DefaultTableModel)tblKeyStPer.getModel()
                                    ).setValueAt(rolodexId,inIndex,4);
                                ((DefaultTableModel)tblKeyStPer.getModel()
                                    ).setValueAt(new Boolean(true),inIndex,5);
                                // Add the training flag info for the given person
                                ((DefaultTableModel)tblKeyStPer.getModel()
                                    ).setValueAt(new Boolean(getTrainingInfo(rolodexId)),inIndex,6);
                                ((DefaultTableModel)
                                tblKeyStPer.getModel()).
                                                    fireTableDataChanged();
                                tblKeyStPer.getSelectionModel().
                                                setLeadSelectionIndex(inIndex);
                                //Added for COEUSQA-2633 Ability to indicate which procedures IACUC study personnel will perform and training -Start
                                Vector vecObserverData = new Vector();
                                if(vecFinalObserverData == null || vecFinalObserverData.isEmpty()){
                                    vecFinalObserverData = new Vector();
                                }
                                vecObserverData = vecFinalObserverData;
                                ProtocolKeyPersonnelBean keyPersonnelBean = new ProtocolKeyPersonnelBean();
                                keyPersonnelBean.setPersonId(stPersonId);
                                keyPersonnelBean.setPersonName(name);
                                keyPersonnelBean.setTrainingFlag((Boolean)tblKeyStPer.getValueAt(inIndex,6));
                                vecObserverData.add(keyPersonnelBean);
                                notifyObserver(vecObserverData, KEY_PERSON_DATA);
                                //Added for COEUSQA-2633 Ability to indicate which procedures IACUC study personnel will perform and training -End
                                saveRequired = true;
                                }else{                                ((DefaultTableModel)
                                tblKeyStPer.getModel()).
                                                    fireTableDataChanged();

                                    CoeusOptionPane.showErrorDialog("' " + name + "' " +
                                        coeusMessageResources.parseMessageKey(
                                           "general_duplicateNameCode.2277"));
                                }
                            }
                       }// singleRolodexData != null //personInfo !=null
                    }
                    }// end of vSelectedPerson != null            
                }catch(Exception e){
                }
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 1;
        gridBagConstraints.gridheight = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 15);
        add(btnFindRolodex, gridBagConstraints);

        tblKeyStPer.setFont(CoeusFontFactory.getNormalFont());
        /* It creates a new DefaultTableModel with the column names */
        tblKeyStPer.setModel(new DefaultTableModel(new Object[][]{},
        new Object [] {"Icon", "Person Name", "Role", "Affiliation","Id",
                                                                "EmployeeFlag","Training"}
        ){
            public boolean isCellEditable(int row, int col){
                
                /** In display mode editing of  table fields will be
                 * disabled. Also for training flag column it is non - editable
                 */

                //code modified for coeus4.3 enhancements that UI to be in display mode
                //when new amendment or renewal is created
//                if(functionType == 'D' || col == 0 || col ==6){                
                if(functionType == CoeusGuiConstants.DISPLAY_MODE || 
                        functionType == CoeusGuiConstants.AMEND_MODE || col == 0 || col ==6){
                    return false;
                }else{
                    return true;
                }
            }
            /* This method is invoked when ever the user changes the 
               contents in the table cell */
            public void fireTableCellUpdated(int row,int column){
                
                setSaveRequired(true);
            }
            /* This is Overridden method and is invoked when ever the 
               data changes in table cell */
            public void setValueAt(Object value, int row, int col) {
                
                    super.setValueAt(value,row,col);
                    fireTableCellUpdated(row,col);
            }

            public Class getColumnClass(int col){
                //Faculty (col 3) is changed from CheckBox to ComboBox
                //if( (col == 3 ) || (col == 5)){
                if( col == 5 || col == 6){
                    return Boolean.class;
                }
                return Object.class;
            }
        });
        
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.gridheight = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 20, 0, 5);
        scrPnPane1.setBorder(new javax.swing.border.TitledBorder(
        //Commented and Modified for Case# 3232-Word "key" is still being carried in the study personnel tab
//            new javax.swing.border.EtchedBorder(), "Key Study Persons", 
//                javax.swing.border.TitledBorder.LEFT, 
//                javax.swing.border.TitledBorder.TOP, 
//                CoeusFontFactory.getLabelFont()));
                new javax.swing.border.EtchedBorder(), "Study Persons", 
                javax.swing.border.TitledBorder.LEFT, 
                javax.swing.border.TitledBorder.TOP, 
                CoeusFontFactory.getLabelFont()));
//        scrPnPane1.setMinimumSize(new java.awt.Dimension(450, 200));
//        scrPnPane1.setPreferredSize(new java.awt.Dimension(450, 200));
        // Added by Chandra - To fit into the screen properly
        scrPnPane1.setMinimumSize(new java.awt.Dimension(600, 410));
        scrPnPane1.setPreferredSize(new java.awt.Dimension(600, 410));
        scrPnPane1.setViewportView(tblKeyStPer);
        add(scrPnPane1, gridBagConstraints);
        // Added by Chandra 12/09/2003
        java.awt.Component[] components = {tblKeyStPer,btnAdd,btnDelete,btnFindPerson,btnFindRolodex};
        ScreenFocusTraversalPolicy  traversePolicy = new ScreenFocusTraversalPolicy( components );
        setFocusTraversalPolicy(traversePolicy);
        setFocusCycleRoot(true);      
        // End Chandra
    }

     /** This method is used to show the alert messages to the user.
      * @param mesg a string message to the user.
      * @throws Exception a exception thrown in the client side.
      */
    public static void errorMessage(String mesg) throws CoeusUIException {
        //Commented by sharath(20 - Aug - 2003)
        //throw new Exception(mesg);
        
        //Bug Fix ( Defect Id : 379)
        CoeusUIException coeusUIException = new CoeusUIException(mesg,CoeusUIException.WARNING_MESSAGE);
        coeusUIException.setTabIndex(CoeusGuiConstants.IACUC_STUDY_PER_EXCEP_TAB_INDEX);
        throw coeusUIException;
        //Bug Fix ( Defect Id : 379)
        
    }

    /** 
     *  Method used to validate whether the keyStudyPerson is duplicate or not 
     */
    
    private boolean checkDuplicatePerson(String personId){
        
        boolean duplicate = false;
        String oldId = CoeusGuiConstants.EMPTY_STRING;
        int keyStudyPersonnelSize = tblKeyStPer.getRowCount();
        for(int rowIndex = 0; rowIndex < keyStudyPersonnelSize;
            rowIndex++){
                oldId = (String)tblKeyStPer.getValueAt(rowIndex,4);
                if(oldId != null){
                    if(oldId.equals(personId)){
                        duplicate = true;
                        break;
                    }
                }
        }
        return duplicate;
    }
    
    private boolean checkDuplicatePerson(String personId, int selectedRow){
        
        boolean duplicate = false;
        String oldId = CoeusGuiConstants.EMPTY_STRING;
        int keyStudyPersonnelSize = tblKeyStPer.getRowCount();
        for(int rowIndex = 0; rowIndex < keyStudyPersonnelSize;
            rowIndex++){
            if(rowIndex != selectedRow){
                
                oldId = (String)tblKeyStPer.getValueAt(rowIndex,4);
                if(oldId != null){
                    
                    if(oldId.equals(personId)){
                        duplicate = true;
                        break;
                    }
                }
            }//
        }
        return duplicate;
    }
    
    


    /**
     * This method is used to get the collection of PersonInfoFormBean.
     * @return Vector of Bean elements
     */
    
    private Vector getPersonInfo(String name){
        
        Vector vsearchData=null;
        RequesterBean requester = new RequesterBean();
        if(name!=null && name.trim().length() > 0){
            
            //Bug Fix: Validation on focus lost Start 1
            /*requester.setDataObject("GET_PERSONINFO");
            requester.setId(name);*/
            requester.setFunctionType('J');
            requester.setDataObject(name);
            //Bug Fix: Validation on focus lost End 1
            
        }
        
        //Bug Fix: Validation on focus lost Start 2
        /*String connectTo = connectionURL + "/coeusFunctionsServlet";*/
        String connectTo = connectionURL + "/unitServlet";
        //Bug Fix: Validation on focus lost End 2
        
        AppletServletCommunicator comm
                    = new AppletServletCommunicator(connectTo, requester);
        comm.send();
        ResponderBean response = comm.getResponse();
        if (response!=null){
            
            vsearchData = new Vector();
            PersonInfoFormBean personInfoFormBean =
                                (PersonInfoFormBean) response.getDataObject();
            
            //Bug Fix: Validation on focus lost Start 3
            if(personInfoFormBean.getPersonID() == null){
                CoeusOptionPane.showErrorDialog(
                    coeusMessageResources.parseMessageKey("investigator_exceptionCode.1007"));
                vsearchData = new Vector();
            }else if(personInfoFormBean.getPersonID().equalsIgnoreCase("TOO_MANY")){
                CoeusOptionPane.showErrorDialog
                    ("\""+name+"\""+" " +coeusMessageResources.parseMessageKey("repRequirements_exceptionCode.1055"));
                vsearchData = new Vector();
            }else{
                String stName = personInfoFormBean.getFullName();
                String stRole = CoeusGuiConstants.EMPTY_STRING;
                Vector vecRoleDesc = getRoleDescriptions();
                if(vecRoleDesc != null && vecRoleDesc.size() > 0){
                    stRole = (String)vecRoleDesc.get(0);
                }
                String stId= personInfoFormBean.getPersonID();
                boolean flag = personInfoFormBean.getFacFlag() == null ? false
                    :(personInfoFormBean.getFacFlag().toString().
                        equalsIgnoreCase("y")? true :false);

                vsearchData.addElement(CoeusGuiConstants.EMPTY_STRING);
                vsearchData.addElement(stName);
                vsearchData.addElement(stRole);
                vsearchData.addElement(new Boolean(flag));
                vsearchData.addElement(stId);
                vsearchData.addElement(new Boolean(false));
                // Add the training flag info for the given person
                vsearchData.addElement(new Boolean(getTrainingInfo(stId)));
            }
        }
        //Bug Fix: Validation on focus lost End 3
        return vsearchData;
    }

    /**
     * Inner Class used to provide textField as cell editor. 
     * It extends DefaulCellEditor and implements TableCellEditor interface.
     * This class overides getTableCellEditorComponent method which returns the 
     * editor component to the JTable Column.
     */
    
    class NameEditor extends DefaultCellEditor implements TableCellEditor {
        
        private JTextField txtName;
        int selRow=0;
        // Added by Raghunath P.V. to fix the bug
        int selCol=0;
        String stTempPersonName=null;
        String stTempRole=null;
        // Changed the booelan value to String Senthil AR
        //boolean stTempFaculty=false;
        //ComboBoxBean stTempFaculty;
        String stTempFaculty;

        /**
         * Constructor for NameEditor 
         * @colName Column Name
         * @len length of the editor field.
         */
        NameEditor( String colName, int len ){
            
            super( new JTextField() );
            txtName = new JTextField();
            if(colName.equals("Name")){

                txtName.setDocument( new LimitedPlainDocument( len ));
                txtName.addMouseListener(new MouseAdapter(){
                    public void mouseClicked(MouseEvent me){
                        
                      if(me.getClickCount() == 2) {
                          
                          boolean nonEmployee=((Boolean)tblKeyStPer.getModel().
                                           getValueAt(selRow,5)).booleanValue();
                          String stId=(String)tblKeyStPer.
                                            getModel().getValueAt(selRow,4);
                          if (txtName.getText().equals(CoeusGuiConstants.EMPTY_STRING) ){
                              CoeusOptionPane.showErrorDialog(
                                    coeusMessageResources.parseMessageKey(
                                        "protoKeyStPsnlFrm_exceptionCode.1069"));
                          }else if((nonEmployee) && (stId!=null)){
                              
                              RolodexMaintenanceDetailForm frmRolodex =
                                     new RolodexMaintenanceDetailForm('V',stId);
                              frmRolodex.showForm(mdiReference,DISPLAY_TITLE,true);
                              
                          }else if(!nonEmployee){
                              int selRow = tblKeyStPer.getSelectedRow();
                              try{
                                  String loginUserName = mdiReference.getUserName();
                                  
                                  //Bug Fix: Pass the person id to get the person details Start 2
                                  //String personName=(String)tblKeyStPer.getValueAt(selRow,1);
                                  /*to get the person details using the person id instead of the person name*/
                                  //PersonInfoFormBean personInfoFormBean = (PersonInfoFormBean)coeusUtils.getPersonInfoID(personName);
                                  //PersonDetailForm personDetailForm = 
                                    //new PersonDetailForm(personInfoFormBean.getPersonID(),loginUserName,'D');
                                  
                                  PersonDetailForm personDetailForm = new PersonDetailForm(stId,
                                          loginUserName, CoeusGuiConstants.DISPLAY_MODE);
                                  //Bug Fix: Pass the person id to get the person details End 2
                                  
                              }catch(Exception exception){
                                  exception.printStackTrace();
                              }
                            
                          }
                        }
                    }
                });
                txtName.addFocusListener(new FocusAdapter(){
                    
                    public void focusLost(FocusEvent fe){
                        
                        if (!fe.isTemporary()){
                           validatePersonName();
                        }
                    }
                });
                
                txtName.addActionListener(new ActionListener() {
                    
                    public void actionPerformed(ActionEvent ae) {
                        validatePersonName();
                    }
                });
            }else if( colName.equals("Role") ){
                
                txtName.setDocument(new LimitedPlainDocument(len));
                txtName.addFocusListener(new FocusAdapter(){
                    
                    public void focusLost(FocusEvent fe){
                        
                        if (!fe.isTemporary()){
                            
                            String stRoleCompare=txtName.getText().trim();
                            int index=tblKeyStPer.getSelectedRow();
                            if(!(stRoleCompare ==null && 
                                 stRoleCompare.equalsIgnoreCase(stTempRole))){
                                     // Added by chandra 26/11/2003 - The col 2 values are
                                     // copying to the other rows of the same column
                                      tblKeyStPer.getModel().
                                            setValueAt(stRoleCompare,selRow,2);
                                    saveRequired=true;
                                    // End Chandra 26/11/2003
//                                    tblKeyStPer.getModel().
//                                            setValueAt(stRoleCompare,index,2);
//                                    saveRequired=true;
                            }
                            if(tblKeyStPer.getCellEditor()!=null){
                                tblKeyStPer.getCellEditor().cancelCellEditing();
                            }
                        }
                    }
                });
            }
        }

        /** This overridden to get the custom cell component in the
         * JTable.
         * @param table JTable instance for which component is derived
         * @param value object value.
         * @param isSelected particular table cell is selected or not
         * @param row row index
         * @param column column index
         * @return a Component which is a editor component to the JTable cell.
         */
        
        public Component getTableCellEditorComponent(JTable table,
        Object value,
        boolean isSelected,
        int row,
        int column){
            
            selRow = row;
            selCol = column;
            stTempPersonName=(String)tblKeyStPer.getValueAt(row,1);
            stTempRole=(String)tblKeyStPer.getValueAt(row,2);
            // Commented since the CheckBox that stored boolean value is changed to ComboBox
            //stTempFaculty=((Boolean)tblKeyStPer.
            //                            getValueAt(row,3)).booleanValue();
            //stTempFaculty=((ComboBoxBean)((JComboBox)tblKeyStPer.getCellEditor()).getSelectedItem());
            stTempFaculty=(String)tblKeyStPer.getValueAt(row,3);
            String newValue = ( String ) value ;
            if( newValue != null && newValue.length() > 0 ){
                txtName.setText( (String)value );
            }else{
                txtName.setText(CoeusGuiConstants.EMPTY_STRING);
            }
            return txtName;
        }

        /**
        * Forwards the message from the CellEditor to the delegate. Tell the 
        * editor to stop editing and accept any partially edited value as the 
        * value of the editor.
        * @return true if editing was stopped; false otherwise
        */
        
        public boolean stopCellEditing() {
            isNewRow=false;
            /*if(tblKeyStPer.getCellEditor() != null){
                tblKeyStPer.getCellEditor().cancelCellEditing();
            }
            String editingValue = (String)getCellEditorValue();
            if(editingValue != null && editingValue.trim().length()>0){
                txtName.setText( editingValue );
            }*/
            if(selCol == 1){
                validatePersonName();
            }
            return super.stopCellEditing();
        }

        /** Returns the value contained in the editor.
         * @return a value contained in the editor
         */
        
        public Object getCellEditorValue() {
            return ((JTextField)txtName).getText();
        }

        /** This Overridden is used to handle the item state changed events.
         * @param e ItemEvent
         */
        
        public void itemStateChanged(ItemEvent e) {
            super.fireEditingStopped();
        }

        /**
         * Supporting method used to validate person Name for its correctness
         * existance with db data.
        */
        
        private void validatePersonName(){

            Vector vecData=new Vector();
            String stTxtNameValue = txtName.getText();
            if (stTxtNameValue.trim().length() <= 0) {
                
                tblKeyStPer.getModel().
                            setValueAt(stTempPersonName == null ? 
                                            CoeusGuiConstants.EMPTY_STRING:stTempPersonName ,selRow,1);
                tblKeyStPer.getModel().
                            setValueAt(stTempRole == null ?
                                            CoeusGuiConstants.EMPTY_STRING:stTempRole,selRow,2);
                /* Senthil AR
                tblKeyStPer.getModel().
                            setValueAt(new Boolean(stTempFaculty),selRow,3);
                 */
                tblKeyStPer.getModel().
                            setValueAt(stTempFaculty,selRow,3);
                if(tblKeyStPer.getCellEditor() != null){
                    tblKeyStPer.getCellEditor().cancelCellEditing();
                }
            } else {
                if(!(stTempPersonName.equalsIgnoreCase(stTxtNameValue))){
                     //Added for COEUSQA-2633 Ability to indicate which procedures IACUC study personnel will perform and training -Start
                    boolean isDeletePersonResponse = false;
                    String message = CoeusGuiConstants.EMPTY_STRING;
                    String personId = (String)tblKeyStPer.getValueAt(selRow,4);
                    if(isPersonResponsePersentForPerson(personId) && !isInvestigatorExists){
                         isDeletePersonResponse = true;                       
                     }                    
                    if(isDeletePersonResponse){
                        message =  "protoKeyStPsnlFrm_delConfirmCode.1142";
                        int selectedOption = CoeusOptionPane.
                                            showQuestionDialog(
                                            coeusMessageResources.parseMessageKey(
                                            message),
                                            CoeusOptionPane.OPTION_YES_NO, 
                                            CoeusOptionPane.DEFAULT_YES);
                        if(0 == selectedOption){
                            Vector vecDeletedPersonId = new Vector();
                            vecDeletedPersonId.add(personId);
                            notifyObserver(vecDeletedPersonId, PERSON_RESP_DATA); 
                        }else{
                            tblKeyStPer.setValueAt(stTempPersonName,selRow,1);
                            tblKeyStPer.setValueAt(personId,selRow,4);
                            tblKeyStPer.getCellEditor().cancelCellEditing();
                            return; 
                        }
                     }  
                     //Added for COEUSQA-2633 Ability to indicate which procedures IACUC study personnel will perform and training -End
                    vecData=getPersonInfo(txtName.getText().trim());
                    if(vecData != null && vecData.size()>0){

                        String stId=(String)vecData.get(4);
                        String stNm=(String)vecData.get(1);
                        String stRo=(String)vecData.get(2);
                        Boolean bFlag = (Boolean)vecData.get(3);
                        Boolean empFlag = (Boolean)vecData.get(5);
                        Boolean trainingFlag = (Boolean)vecData.get(6);
                        boolean duplicate = checkDuplicatePerson(stId,selRow);
                        if(!duplicate){

                            tblKeyStPer.getModel().setValueAt(stId,selRow,4);
                            tblKeyStPer.getModel().setValueAt(empFlag,selRow,5);
                            tblKeyStPer.getModel().setValueAt(trainingFlag,selRow,6);
                            tblKeyStPer.getModel().setValueAt(stNm,selRow,1);
                            tblKeyStPer.getModel().setValueAt(stRo,selRow,2);
                            
                            // Commented for COEUSQA-2807 : IACUC_Affiliation_for_study_personnel_needs_separate_code_table - Start
//                            String affiliation = bFlag.booleanValue()?"Faculty":"Affiliate";
                            //String affiliation = CoeusGuiConstants.EMPTY_STRING;                            
                           // tblKeyStPer.getModel().setValueAt(affiliation,selRow,3);
                            // Commented for COEUSQA-2807 : IACUC_Affiliation_for_study_personnel_needs_separate_code_table - End
                            if(tblKeyStPer.getCellEditor() !=null){
                                tblKeyStPer.getCellEditor().cancelCellEditing();
                            }
                            //Added for COEUSQA-2633 Ability to indicate which procedures IACUC study personnel will perform and training -Start
                            Vector vecObserverData = new Vector();
                            if(vecFinalObserverData != null && !vecFinalObserverData.isEmpty()){
                            vecObserverData.addAll(vecFinalObserverData);
                            }
                            ProtocolKeyPersonnelBean keyPersonnelBean = new ProtocolKeyPersonnelBean();
                            keyPersonnelBean.setPersonId(stId);
                            keyPersonnelBean.setPersonName(stNm);
                            keyPersonnelBean.setTrainingFlag(trainingFlag);
                            if(vecFinalObserverData !=null && !vecFinalObserverData.isEmpty()){             
                            CoeusVector cvKeyPersonData = new CoeusVector();
                            cvKeyPersonData.addAll(vecFinalObserverData);
                            Equals andPersonId;                                                                                                                                                                                                           
                            andPersonId= new Equals("personId", keyPersonnelBean.getPersonId());                    
                            CoeusVector filteredResult = cvKeyPersonData.filter(andPersonId);
                            if(filteredResult.size()==0){
                                vecObserverData.add(keyPersonnelBean);
                            }                                                                      
                            }else{
                                vecObserverData.add(keyPersonnelBean); 
                            }                                                                                                                                                    
                            if(!(CoeusGuiConstants.EMPTY_STRING.equals(stTempPersonName.trim()))){
                            vecObserverData.remove(tblKeyStPer.getSelectedRow());
                            }                            
                            notifyObserver(vecObserverData, KEY_PERSON_DATA);
                            //Added for COEUSQA-2633 Ability to indicate which procedures IACUC study personnel will perform and training -End
                            saveRequired = true;
                        }else{
                            CoeusOptionPane.showErrorDialog("'" + stNm +"' "+ 
                                coeusMessageResources.parseMessageKey(
                                   "general_duplicateNameCode.2277"));
                            tblKeyStPer.getModel().setValueAt(CoeusGuiConstants.EMPTY_STRING,selRow,1);
                            //Added For Issue #1842-2260- Error in Modifying Study Person -premium - Start
                            tblKeyStPer.getModel().setValueAt(CoeusGuiConstants.EMPTY_STRING,selRow,4);
                            //Added For Issue #1842-2260- Error in Modifying Study Person -premium - End
                            if(tblKeyStPer.getCellEditor() !=null){
                                tblKeyStPer.getCellEditor().cancelCellEditing();
                            }
                        }
                    }else{
                        //Bug Fix: Validation on focus lost Start 4
                        /*CoeusOptionPane.showErrorDialog(
                                coeusMessageResources.parseMessageKey(
                                    "protoCorroFrm_exceptionCode.1054"));*/
                        //Bug Fix: Validation on focus lost End 4
                        tblKeyStPer.getModel().
                        setValueAt(stTempPersonName,selRow,1);
                        tblKeyStPer.getModel().
                        setValueAt(stTempRole,selRow,2);
                        //Senthil AR commented out to have Combo in place of Checkbox
                        //tblKeyStPer.getModel().setValueAt(new
                        //Boolean(stTempFaculty),selRow,3);
                        tblKeyStPer.getModel().setValueAt(stTempFaculty,selRow,3);
                        if(tblKeyStPer.getCellEditor() !=null){
                          tblKeyStPer.getCellEditor().cancelCellEditing();
                        }
                    }
//                    tblKeyStPer.getSelectionModel().setLeadSelectionIndex(selRow);
                }else{
                    if(tblKeyStPer.getCellEditor() !=null){
                          tblKeyStPer.getCellEditor().cancelCellEditing();
                    }
                }
            }//Else part handle
        }
    }

   /**
     * Inner class which is used to provide Icon to the JTable column as Renderer.
     * It extends DefaultTableCellRenderer.
     * This class overides getTableCellRendererComponent method which returns the 
     * renderer component to the JTable Column. 
     */
    
    class IconRenderer  extends DefaultTableCellRenderer {

        private  final  ImageIcon handIcon =
        new ImageIcon(getClass().getClassLoader().getResource(
        CoeusGuiConstants.HAND_ICON));
        
//        private  final  ImageIcon handIcon =
//        new ImageIcon(getClass().getResource(
//        CoeusGuiConstants.HAND_ICON));
        private  final  ImageIcon emptyIcon = null;
        /** Default Constructor*/
        IconRenderer() {
            
        }

        /** An overridden method to render the component(icon) in cell.
         * foreground/background for this cell and Font too.
         *
         * @param table the JTable that is asking the renderer to draw;
         * can be null
         * @param value the value of the cell to be rendered. It is up to the
         * specific renderer to interpret and draw the value. For example,
         * if value is the string "true", it could be rendered as a string or
         * it could be rendered as a check box that is checked. null is a
         * valid value
         * @param isSelected true if the cell is to be rendered with the
         * selection highlighted; otherwise false
         * @param hasFocus if true, render cell appropriately. For example,
         * put a special border on the cell, if the cell can be edited, render
         * in the color used to indicate editing
         * @param row the row index of the cell being drawn. When drawing the
         * header, the value of row is -1
         * @param column the column index of the cell being drawn
         * @return Component which is to be rendered.
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

    /**
     * This class is used to construct the empty table header.
     */
    
    class EmptyHeaderRenderer extends JList implements TableCellRenderer {
        
        /**
         * Default constructor to set the default foreground/background and
         * border
         * properties of this renderer for a cell.
         */
        
        EmptyHeaderRenderer() {

            setOpaque(true);
            setForeground(UIManager.getColor("TableHeader.foreground"));
            setBackground(UIManager.getColor("TableHeader.background"));
            setBorder(new javax.swing.border.EmptyBorder(0, 0, 0, 0));
            ListCellRenderer renderer = getCellRenderer();
            ((JLabel) renderer).setHorizontalAlignment(JLabel.CENTER);
            setCellRenderer(renderer);
        }

        /** The Overridden method of TableCellRenderer which is called for every
         * cell when a component
         * is going to be rendered in its cell.
         * Returns the component used for drawing the cell.
         * This method is used to configure the renderer appropriately before
         * drawing
         *
         * @param table  the JTable that is asking the renderer to draw; can be
         * null
         * @param value  the value of the cell to be rendered. It is up to the
         * specific renderer to interpret and draw the value.
         * For example, if value is the string "true", it could be rendered as
         * a string or it could be rendered as a check box that is checked.
         * null is a valid value
         * @param isSelected  true if the cell is to be rendered with the
         * selection highlighted; otherwise false
         * @param hasFocus if true, render cell appropriately. For example,
         * put a special border on the cell, if the cell can be edited,
         * render in the color used to indicate editing
         * @param row the row index of the cell being drawn.
         * When drawing the header, the value of row is -1
         * @param column the column index of the cell being drawn
         * @return Component which is to be rendered.
         */
        
        public Component getTableCellRendererComponent(JTable table,
            Object value,boolean isSelected, boolean hasFocus, 
            int row, int column) {
                
                return this;
        }
    }
    /** It is an inner calss which provides the renderer for the training flag info
     *If the training flag exists for the person then show the checked icon
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
            boolean isTraining = false;
            int selectedRow=tblKeyStPer.getSelectedRow();
            setOpaque(false);
                setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
                if(!isNewRow || (row != selectedRow)){
                    if(value == null){
                        setIcon(crossIcon);
                    }else{
                    isTraining = ((Boolean)value).booleanValue();
                    }
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
    /** Get the training flag for the person id
     */
    private boolean getTrainingInfo(String personId){
        boolean isTrain = false;
        RequesterBean requesterBean = new RequesterBean();
        ResponderBean responderBean = new ResponderBean();
        requesterBean.setDataObject(personId);
        requesterBean.setFunctionType(GET_TRAINIG_INFO);
        
        String connectTo = connectionURL +"/IacucProtocolServlet";
        
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
    
    //Added for COEUSQA-2633 Ability to indicate which procedures IACUC study personnel will perform and training -Start
     /**
      * Registers the Observer
      */
     public void registerObserver( Observer observer ) {
         newObservable.addObserver( observer );
     }
     
     /**
      * UnRegisters the Observer
      */
     public void unRegisterObserver( Observer observer ) {
         newObservable.deleteObserver( observer );
     }        
     
    /**
     * This method is used to notify the observer
     * @param Vector investigatorInfo
     * @param String observerType
     */
     private void notifyObserver(Vector studyPersonnelInfo, String observerType){
         HashMap hmInfo = new HashMap();
         if(KEY_PERSON_DATA.equals(observerType)){
         vecFinalObserverData = new Vector();
         vecFinalObserverData = studyPersonnelInfo;         
         hmInfo.put("studyPersonnelInfo",studyPersonnelInfo);
         newObservable.notifyObservers(hmInfo);
         }else{
         String deletedPersonId = (String) studyPersonnelInfo.get(0);
         hmInfo.put("deletedPersonsResponse",deletedPersonId);
         newObservable.notifyObservers(hmInfo);   
         }
     }
     
    /**
     * This method is used to update the data from the observer
     * @param Observable o
     * @param Object arg
     */
     public void update(Observable o, Object arg) {                
        HashMap hmNofiyInfo = (HashMap)arg;
        if(hmNofiyInfo.get("personInfo")!=null){// co        
            vecPersonResponsible = (Vector)hmNofiyInfo.get("personInfo");               
        }else if(hmNofiyInfo.get("investigatorInfo")!=null){// coming from investigator
            Vector vecInvestitor = (Vector)hmNofiyInfo.get("investigatorInfo");
            vecInvestigatorData = new Vector();
            vecInvestigatorData.addAll(vecInvestitor);
        }
    } 
     
   /**
    * This method is used to check the person is added in study group person responsible
    * @param String personId
    * @return Boolean value
    */
    private boolean isPersonResponsePersentForPerson(String personId){
        boolean personresponsibleExists = false;
        isInvestigatorExists = false;
        if(vecPersonResponsible != null && !vecPersonResponsible.isEmpty()){
            for(Object obj:vecPersonResponsible){
                ProtocolPersonsResponsibleBean personsResponsibleBean = (ProtocolPersonsResponsibleBean)obj;
                if(personId.equals(personsResponsibleBean.getPersonId())){
                    personresponsibleExists = true;
                    break;
                }
            }
        }
        if(personresponsibleExists){
           if(vecInvestigatorData != null && !vecInvestigatorData.isEmpty()){
              ProtocolInvestigatorsBean investigatorsBean;
              for(Object obj:vecInvestigatorData){
                  investigatorsBean = (ProtocolInvestigatorsBean)obj;
                  if(personId.equals(investigatorsBean.getPersonId())){
                     isInvestigatorExists = true; 
                     break;
                  }
              }
           } 
        }
        return personresponsibleExists;
     }
    //Added for COEUSQA-2633 Ability to indicate which procedures IACUC study personnel will perform and training -End
}
