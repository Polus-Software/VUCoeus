/*
 * @(#)ProtocolFundingForm.java  1.0  19/9/02
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

/* PMD check performed, and commented unused imports and variables on 20-APR-2011
 * by Bharati
 */

package edu.mit.coeus.irb.gui;

import edu.mit.coeus.propdev.bean.ProposalAwardHierarchyLinkBean;
import edu.mit.coeus.propdev.gui.MedusaDetailForm;
import java.awt.*;
import java.util.*;
import java.awt.event.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import java.awt.event.MouseEvent;
//import javax.swing.event.ListSelectionListener;
import java.beans.*;
import javax.swing.table.*;
import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.utils.*;
import edu.mit.coeus.unit.gui.*;
import edu.mit.coeus.sponsormaint.gui.*;
import edu.mit.coeus.gui.CoeusAppletMDIForm;
//import edu.mit.coeus.gui.CoeusInternalFrame;
//import edu.mit.coeus.gui.CoeusDlgWindow;
import edu.mit.coeus.search.gui.*;
import edu.mit.coeus.irb.bean.*;
import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.sponsormaint.bean.*;
import edu.mit.coeus.unit.bean.*;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.exception.*;

//Coeus Enhancement case #1787start
//import edu.mit.coeus.award.bean.AwardHeaderBean;
import edu.mit.coeus.award.bean.AwardBean;
import edu.mit.coeus.award.controller.AwardBaseWindowController;
import edu.mit.coeus.propdev.gui.ProposalDetailForm;
//import edu.mit.coeus.propdev.bean.ProposalDevelopmentFormBean;
import edu.mit.coeus.instprop.bean.InstituteProposalBean;
import edu.mit.coeus.instprop.bean.InstituteProposalBaseBean;
import edu.mit.coeus.instprop.controller.InstituteProposalBaseWindowController;
//Coeus Enhancement case #1787 end


/** <CODE>ProtocolFundingSourceForm</CODE> is a form object which display
 * all the protocol funding sources and can be used to <CODE>add/modify/display</CODE> the
 * funding source details.
 * This class is instantiated in <CODE>ProtocolDetailForm</CODE>.
 *
 * @author kprasad
 * @version: 1.0 September 30, 2002
 * @author Raghunath P.V
 * @version: 1.1 October 24, 2002
 */
public class ProtocolFundingSourceForm extends javax.swing.JComponent {
    
    /* button is used to Find Sponsor or Unit */
    private javax.swing.JButton btnFind;
    /* This is used to hold table data*/
    public javax.swing.JTable tblFundSoForm;
    /* This is used to delete a row in JTable*/
    private javax.swing.JButton btnDelete;
    /* This is used to ADD a row in a JTable*/
    private javax.swing.JButton btnAdd;
    
    public javax.swing.JButton btnStartProposal;
    /* This is used to have a reference of Child Window */
    private ProtocolFundingSourceBean protocolFNDBean;
    /* This is used to hold the mode D for Display, I for Add, U for Modify */
    private char functionType;
    /* This is used to hold vector of data beans*/
    private Vector protocolFundingSourceData = null;
    /* This is used to hold the reference of MDIFrame*/
    private CoeusAppletMDIForm mdiFormReference;
    /* This is used to notify whether the Save is required */
    private boolean saveRequired = false;
    /* This is used to identify the identifier, by which we can call either
       Sponsor or Unit */
    private String searchIdentifier = null;
    /* This is used to get all the available types in combobox*/
    private Vector availableTypes = null;
    /* Constant string to identify the sponsor search*/
    private static final String SPONSOR_SEARCH = "sponsorSearch";
    /* Constant string to identify the unit search*/
    private static final String LEAD_SEARCH = "leadunitSearch";
    private int enableProposalLink;
    //Coeus Enhancement case #1787 start
    /* Constant string to identify the award search*/
    private static final String AWARDS_SEARCH = "awardSearch";
    /* Constant string to identify the instituteproposal search*/
    private static final String INSTITUTEPROPOSAL_SEARCH = "proposalSearch";
    /* Constant string to identify the developmentproposal search*/
    private static final String DEVELOPMENTPROPOSAL_SEARCH = "PROPOSALDEVSEARCHNOROLES";
    //Coeus Enhancement case #1787 end
    
    //Added for COEUSQA-1434 : Add the functionality to set a status on a Sponsor record
    private static final String INACTIVE_STATUS = "I";
    
    /* Used for ConnectionURL */
    private String connectionURL = CoeusGuiConstants.CONNECTION_URL;
    /* Used to check whter the data is modified or not */
    private boolean protocolInfoModified = false;
    /* Used to hold the table data as beans*/
    private ProtocolFundingSourceBean newBean;
    /* Used to hold the database data as beans*/
    private ProtocolFundingSourceBean oldBean;
    /* Boolean variable used to find the deleted record*/
    private boolean found;
    
    /* Used to display this string as the title for a window*/
    private static final String DISPLAY_SPONSOR_TITLE = "DISPLAY SPONSOR";
    /* Used to display this string as the title for a window*/
    //holds the zero count value
    private static final int ZERO_COUNT = 0;
    /* This member variable is used to hold CoeusLabelFont*/
    java.awt.Font coeusLabelFont;
    /* This member variable is used to hold CoeusNormalFont*/
    java.awt.Font coeusNormalFont;
    
    /*holds CoeusMessageResources instance used for reading message Properties*/
    private CoeusMessageResources coeusMessageResources;
    
    //Added for bug fixing
    private static final int NAME_COLUMN = 2;
    private boolean error;
    
    private static final int SPONSOR_TYPE = 1;
    private static final int UNIT_TYPE = 2;
    private static final int OTHER_TYPE = 3;
    //Coeus Enhancement case #1787 start
    private static final int DEVELOPMENTPROPOSAL_TYPE = 4;
    private static final int INSTITUTEPROPOSAL_TYPE = 5;
    private static final int AWARDS_TYPE = 6;
    //Code added for Case#3388 - Implementing authorization check at department level - starts
    private static final char CAN_VIEW_AWARD =  'f' ;
    private static final char CAN_VIEW_INST_PROPOSAL = 'w';
    //Code added for Case#3388 - Implementing authorization check at department level - ends 
    //Added for COEUSQA-1434 : Add the functionality to set a status on a Sponsor record - start
    Vector vecDeletedFundSrc = new Vector();
    //Added for COEUSQA-1434 : Add the functionality to set a status on a Sponsor record - end
        
    //Coeus Enhancement case #1787 end
    //COEUSQA:2653 - Add Protocols to Medusa - Start
    private javax.swing.JButton btnMedusa;
    private static final String DEVELOPMENT_PROPOSAL = "Development Proposal";
    private static final String INSTITUTE_PROPOSAL = "Institute Proposal";
    private static final String AWARD = "Award";
    private final String CREATE_RIGHT="CREATE_PROPOSAL";
    boolean hasCreateProposalRight = false;
    public static ProtocolInfoBean protocolInfo = null;
    //COEUSQA:2653 - End
    /** Creates a new <CODE>ProtocolFundingSourceForm</CODE> <p>
     * <I>Default Constructor</I>
     */
    
    public ProtocolFundingSourceForm() {
        
    }
    
    /** Constructor that instantiate <CODE>ProtocolFundingSourceForm</CODE> and populate
     * components with the specified data. And it set the enable status
     * for all components depending on the functionType.
     * @param fundingSourceData is a Vector which consists of all the FundingSource details for a particular protocol.
     * @param functionType is a Character which specifies the mode in which the
     * form will be displayed.
     * <B>'A'</B> specifies that the form is in Add Mode
     * <B>'M'</B> specifies that the form is in Modify Mode
     * <B>'D'</B> specifies that the form is in Display Mode
     */
    
    public ProtocolFundingSourceForm(char functionType,
    java.util.Vector fundingSourceData,ProtocolInfoBean protocolInfoBean) {
        
        protocolFundingSourceData = new Vector();
        this.protocolFundingSourceData = fundingSourceData;
        this.functionType = functionType;
        protocolInfo = protocolInfoBean;
    }

    public ProtocolFundingSourceForm(char functionType,
    java.util.Vector fundingSourceData) {

        protocolFundingSourceData = new Vector();
        this.protocolFundingSourceData = fundingSourceData;
        this.functionType = functionType;
    }
    
    /** This method is used to determine whether the data to be saved or not.
     * @return true if the modifications done, false otherwise.
     */
    
    public boolean isSaveRequired(){
        //tblFundSoForm
        if(tblFundSoForm.getRowCount()>0){
            if(tblFundSoForm.isEditing()){
                if(tblFundSoForm.getCellEditor() != null){
                    tblFundSoForm.getCellEditor().stopCellEditing();
                }
            }
        }
        return saveRequired;
    }
    
    //Added by Amit 11/21/2003
    /** This method use to implement focus on first editable component in this page.
     */
    public void setDefaultFocusForComponent(){
        //Code modified for Case#3070 - Ability to change a funding source - starts
//        if(!( functionType == CoeusGuiConstants.DISPLAY_MODE )) {
        if(!( functionType == CoeusGuiConstants.DISPLAY_MODE 
                || functionType == CoeusGuiConstants.AMEND_MODE)) {
        //Code modified for Case#3070 - Ability to change a funding source - ends
            
            if(tblFundSoForm.getRowCount() > 0 ) {
                tblFundSoForm.requestFocusInWindow();
                
                //included by raghu to remain the selection on row upon selection...
                //starts..
                int prevSelectedRow=tblFundSoForm.getSelectedRow();
                if(prevSelectedRow!=-1){
                    tblFundSoForm.setRowSelectionInterval(prevSelectedRow, prevSelectedRow);
                }
                else{
                    tblFundSoForm.setRowSelectionInterval(0, 0);
                }
                //ends
                
                tblFundSoForm.setColumnSelectionInterval(1,1);
            }else{
                btnAdd.requestFocusInWindow();
            }
        }
    }
    //end Amit
    
    /** This method is used to set true or false to the saveRequired member variable.
     * @param save is a boolean variable to be set to saveRequired variable.
     */
    
    public void setSaveRequired(boolean save){
        
        this.saveRequired = save;
    }
    
    /** This method is used to determine whether the protocol information is modified.
     * @return true if the modifications done, false otherwise.
     */
    
    public boolean isProtocolInfoModified(){
        
        return protocolInfoModified;
    }
    
    /** This Method is used to get the functionType
     * @return a fuctionType like 'A','D','M'.
     */
    
    public char getFunctionType(){
        
        return functionType;
    }
    
    /** This Method is used to set the functionType.
     * @param fType is a Char data like 'A','D','M'.
     */
    public void setFunctionType(char fType){
        
        this.functionType = fType;
    }
    
    /** Method to set all the available function types.
     * @param aTypes a Vector consists of <CODE>ComboBoxBean</CODE>
     */
    public void setAvailableFundingSourceTypes(Vector aTypes){
        
        this.availableTypes = aTypes;
    }
    
    /* Helper method which gives vector of FundingSourceTypeDescriptions */
    
    private Vector getAvailableFundingSourceTypes() {
        
        ComboBoxBean comboEntry = null;
        Vector availableTypeDesc = new Vector();
        Vector locAvailableType = availableTypes;
        if( locAvailableType == null ){
            return availableTypeDesc;
        }
        int availableTypesSize = locAvailableType.size();
        for( int indx = 0; indx < availableTypesSize; indx++ ){
            
            comboEntry = ( ComboBoxBean ) locAvailableType.get( indx );
            availableTypeDesc.addElement( comboEntry.getDescription() );
        }
        return availableTypeDesc;
    }
    
    /* Helper method which gives available type Code value for the
     * available type description selected in JComboBox
     */
    private String getIDForName( String selType ){
        
        String stTypeID = "1";
        ComboBoxBean comboEntry = null;
        Vector locAvailableTypes = availableTypes;
        if( locAvailableTypes == null || selType == null ){
            return stTypeID;
        }
        int availableTypesSize = locAvailableTypes.size();
        for( int indx = 0; indx < availableTypesSize; indx++ ){
            
            comboEntry = ( ComboBoxBean ) locAvailableTypes.get( indx );
            if( ((String)comboEntry.getDescription()).equalsIgnoreCase(
            selType)  ){
                stTypeID = comboEntry.getCode();
                break;
            }
        }
        return stTypeID;
    }
    
    
    /** Method to get the Form Data
     * @return a Vector of <CODE>ProtocolFundingSourceBean</CODE>s.
     */
    public Vector getFormData(){
        
        return protocolFundingSourceData;
    }
    
    /** Method to set the Form data.
     * @param fundingSourceData a Vector of <CODE>ProtocolFundingSourceBean</CODE>.
     */
    
    public void setFundingSourceData(Vector fundingSourceData){
        //Code modified for Case#3070 - Ability to change a funding source - starts
        this.protocolFundingSourceData = fundingSourceData; 
        formatFields();
        int selectedRow=0;
        if(tblFundSoForm.getRowCount()>0){
            selectedRow=tblFundSoForm.getSelectedRow();
        }
        setFormData();
        setTableEditors();
        /* This logic is used to select the first row in the list of available
           rows in JTable*/
        if( tblFundSoForm.getRowCount() > ZERO_COUNT ){
            tblFundSoForm.setRowSelectionInterval(ZERO_COUNT, ZERO_COUNT);
        }else{
            btnDelete.setEnabled(false);
            btnFind.setEnabled(false);
            btnMedusa.setEnabled(false);
        }
        // setting bold property for table header values
        tblFundSoForm.getTableHeader().setFont(coeusLabelFont);        
        //Code modified for Case#3070 - Ability to change a funding source - ends
    }
    
    /** This method is used to initialize the components, set the data in the components.
     * This method is invoked in the <CODE>ProtocolDetailForm</CODE>.
     * @param mdiForm a reference of CoeusAppletMDIForm
     * @return a JComponent consists of all the form components.
     */
    
    public JComponent showProtocolFundingSourceForm(CoeusAppletMDIForm
    mdiForm){
        
        this.mdiFormReference = mdiForm;
        coeusLabelFont = CoeusFontFactory.getLabelFont();
        coeusNormalFont = CoeusFontFactory.getNormalFont();
        initComponents();
        // This method enable or disable the JButtons depending on the functionType
        formatFields();
        setFormData();
        setTableEditors();
        /* This logic is used to select the first row in the list of available
           rows in JTable*/
        if( tblFundSoForm.getRowCount() > ZERO_COUNT ){
            tblFundSoForm.setRowSelectionInterval(ZERO_COUNT, ZERO_COUNT);
        }else{
            btnDelete.setEnabled(false);
            //Bug fix code
            //Added by Vyjayanthi on 26/08/03
            btnFind.setEnabled(false);
            btnMedusa.setEnabled(false);
        }
        
        // setting bold property for table header values
        tblFundSoForm.getTableHeader().
        setFont(coeusLabelFont);
        coeusMessageResources = CoeusMessageResources.getInstance();
        return this;
    }
    
    /* Method to set the data in the JTable.
       This method sets the data which is available in protocolFundingSourceData
       Vector into JTable. */
    private void setFormData(){
        Vector vcDataPopulate = new Vector();
        Vector vcData=null;
        //COEUSQA:2653 - Add Protocols to Medusa - Start
        boolean isBtnMedusaEnabled = false;
        //COEUSQA:2653 - End
        if((protocolFundingSourceData!= null) &&
        (protocolFundingSourceData.size()>0)){
            
            for(int inCtrdata=0;
            inCtrdata < protocolFundingSourceData.size();
            inCtrdata++){
                
                protocolFNDBean=(ProtocolFundingSourceBean)
                protocolFundingSourceData.get(inCtrdata);
                String stTypeDes=
                protocolFNDBean.getFundingSourceTypeDesc();                
                String stSourceNo= protocolFNDBean.getFundingSource();
                String stSoName= protocolFNDBean.getFundingSourceName();
                vcData= new Vector();
                vcData.addElement("");
                vcData.addElement( stTypeDes );
                vcData.addElement(stSourceNo);
                vcData.addElement(stSoName == null ? "" : stSoName);
                vcDataPopulate.addElement(vcData);
                //COEUSQA:2653 - Add Protocols to Medusa - Start
                if(DEVELOPMENT_PROPOSAL.equals(stTypeDes) || INSTITUTE_PROPOSAL.equals(stTypeDes) || AWARD.equals(stTypeDes)) {
                    isBtnMedusaEnabled = true;
                }
                //COEUSQA:2653 - End
            }
            }
            //Modified with case 4398: table was not updating if protocolFundingSourceData is empty.
            ((DefaultTableModel)tblFundSoForm.getModel()).
            setDataVector(vcDataPopulate,getColumnNames());
            ((DefaultTableModel)tblFundSoForm.getModel()).
            fireTableDataChanged();
            
            if( tblFundSoForm.getRowCount() > 0 ){
                
                tblFundSoForm.getSelectionModel().
                setSelectionInterval(1,
                tblFundSoForm.getColumnCount() );
                tblFundSoForm.setRowSelectionInterval(0,0);
                //COEUSQA:2653 - Add Protocols to Medusa - Start
                if(isBtnMedusaEnabled == false) {
                    btnMedusa.setEnabled(false);
                }
                //COEUSQA:2653 - End
            }else{
                btnDelete.setEnabled(false);
                //Bug fix code
                //Added by Vyjayanthi on 26/08/03
                btnFind.setEnabled(false);
                btnMedusa.setEnabled(false);
            }
        
    }
    
    /* Method to get all the column names which are specified in the JTable*/
    private Vector getColumnNames(){
        
        Enumeration enumColNames = tblFundSoForm.getColumnModel().getColumns();
        Vector vecColNames = new Vector();
        while(enumColNames.hasMoreElements()){
            
            String strName = (String)((TableColumn)
            enumColNames.nextElement()).getHeaderValue();
            vecColNames.addElement(strName);
        }
        return vecColNames;
    }
    
    /* Method to get all the table data from JTable
       @return Vector, a Vector which consists of ProtocolFundingSourceBean's */
    private Vector getTableValues(){
        
        Vector keyValues = new Vector();
        if(tblFundSoForm.getRowCount()>0){
            
            int rowCount = tblFundSoForm.getRowCount();
            ProtocolFundingSourceBean pfBean;
            //Iterating till the last row in the JTable
            for(int inInd=0; inInd < rowCount ;inInd++){
                /* Checking the Condition whether the Value
                   is entered in the FundingSourceCode */
                if((tblFundSoForm.getValueAt(inInd,2) != null)
                && (tblFundSoForm.getValueAt(inInd,
                2).toString().trim().length()>0)){
                    
                    String stTypeDes=(String)((DefaultTableModel)
                    tblFundSoForm.getModel()).getValueAt(inInd,1);
                    String stInTypeCode=getIDForName(stTypeDes);
                    
                    String stFundCode=(String)((DefaultTableModel)
                    tblFundSoForm.getModel()).getValueAt(inInd,2);
                    String stFundName=(String)((DefaultTableModel)
                    tblFundSoForm.getModel()).getValueAt(inInd,3);
                    int inTypeCode= Integer.parseInt(stInTypeCode);
                    
                    pfBean= new ProtocolFundingSourceBean();
                    pfBean.setFundingSourceTypeDesc(stTypeDes);
                    pfBean.setFundingSource(stFundCode);
                    pfBean.setFundingSourceName("".equals(stFundName)? null
                    : stFundName);
                    pfBean.setFundingSourceTypeCode(inTypeCode);
                    keyValues.addElement(pfBean);
                }else{
            /* If nothing is entered in FundingSourceCode,
               then showing the error dialog to the user stating to enter
               funding source code */
                    CoeusOptionPane.showErrorDialog(
                    coeusMessageResources.parseMessageKey(
                    "protoFndSrcFrm_exceptionCode.1062"));
                    tblFundSoForm.requestFocus();
                }
            }
        }
        return keyValues;
    }
    
    /** Method to get the funding source data in a Vector
     * which consists of ProtocolFundingSourceBeans.
     * It sets the AcType as 'U' to the bean if any bean data is modified.
     * It sets the AcType to 'i' if any data is inserted into JTable by the user.
     * It sets the AcType to 'D' if any data is deleted from JTable by the user.
     * @return a Vector of ProtocolFundingSourceBeans.
     */
    
    public java.util.Vector getFundingSourceData(){
        /* This block of code is used to set AcType as D to all the beans
           if all the rows are deleted in the JTable and it sets the
           protocolInfoModified flag to true stating that the save is required
           for the user */
        int editRow = tblFundSoForm.getEditingRow();
        int editCol = tblFundSoForm.getEditingColumn();
                
        if(tblFundSoForm.getRowCount()>0){
            if( editRow!= -1 && editCol != -1 ) {
                tblFundSoForm.getCellEditor(editRow,editCol).stopCellEditing();
                System.out.println("Yes cell is editing while SAVING");
            }
        }
        // Case# 3219: Protocol funding source instute proposal locking error - Start
        // Remove all the ProtocolFundingSourceBean with AcType 'I' from protocolFundingSourceData
        if(protocolFundingSourceData != null){
            int fundingSize = protocolFundingSourceData.size();
            
            if(fundingSize > 0){
                for(int index = fundingSize-1 ; index >= 0 ; index--){
                    oldBean = (ProtocolFundingSourceBean)
                        protocolFundingSourceData.elementAt(index);
                    
                    if( "I".equals(oldBean.getAcType()) ){
                        protocolFundingSourceData.remove(index);
                    }
                }
            }
        }
        // Case# 3219: Protocol funding source instute proposal locking error - End
        if((protocolFundingSourceData!=null) &&
        (protocolFundingSourceData.size()>0) &&
        (tblFundSoForm.getRowCount()<=0)){
            for(int oldIndex = 0;
            
            oldIndex < protocolFundingSourceData.size();
            oldIndex++){
                oldBean = (ProtocolFundingSourceBean)
                protocolFundingSourceData.elementAt(oldIndex);
                oldBean.setAcType("D");
                setSaveRequired(true);
                if(functionType == 'M'){
                    protocolInfoModified = true;
                }
                protocolFundingSourceData.setElementAt(oldBean,
                oldIndex);
            }
            hasCreateProposalRight = hasCreateProposalRights();
           
            return protocolFundingSourceData;
        }
        /* This gets all the data from the JTable*/
        Vector newData = getTableValues();
        if((newData != null) && (newData.size() > 0)){
            
            if( functionType == 'E' || functionType == 'A' ) {
                return setAcTypeInAmend( newData );
            }
            
            for(int newLocIndex = 0; newLocIndex < newData.size();
            newLocIndex++){
                int foundIndex = -1;
                found = false;
                newBean = (ProtocolFundingSourceBean)newData.
                elementAt(newLocIndex);
                
                if(protocolFundingSourceData != null && functionType == 'M' ){
                    for(int oldLocIndex = 0;
                    oldLocIndex < protocolFundingSourceData.size();
                    oldLocIndex++){
                        oldBean = (ProtocolFundingSourceBean)
                        protocolFundingSourceData.elementAt(oldLocIndex);
                        
                        oldBean.addPropertyChangeListener(
                        new PropertyChangeListener(){
                            public void propertyChange(
                            PropertyChangeEvent pce){
                                /* if address has been changed to a
                                location setting the corresponding
                                rolodex id will fire property change
                                event. If any changes have been done
                                to exisiting location new record will
                                be inserted for protocol details with
                                new sequence no. So set acType for
                                both beans to MODIFY_RECORD */
                                if( !( pce.getOldValue() == null
                                && pce.getNewValue() == null ) ){
                                    oldBean.setAcType("U");
                                    setSaveRequired(true);
                                    protocolInfoModified = true;
                                }
                            }
                        });
                        if(newBean.getFundingSource().equals(
                        oldBean.getFundingSource())){
                            found = true;
                            foundIndex = oldLocIndex;
                            break;
                        }
                    }
                }/*else{
                    protocolFundingSourceData = new Vector();
                }*/
                //code modified for coeus4.3 enhancements that UI to be in display mode
                //when new amendment or renewal is created
//                if(!found){                
                if(!found && functionType != 'D'){
                    if( protocolFundingSourceData == null ) {
                        protocolFundingSourceData = new Vector();
                    }
                    /* if location is new set AcType to INSERT_RECORD */
                    newBean.setAcType("I");
                    setSaveRequired(true);
                    if(functionType == 'M'){
                        protocolInfoModified = true;
                    }
                    protocolFundingSourceData.addElement(newBean);
                }else{
                    /* if present set the values to the bean. if modified,
                    bean will fire property change event */
                    if(oldBean != null){
                        oldBean.setFundingSourceTypeDesc(newBean.
                        getFundingSourceTypeDesc());
                        
                        oldBean.setFundingSourceTypeCode(newBean.
                        getFundingSourceTypeCode());
                        
                        oldBean.setFundingSource(newBean.
                        getFundingSource());
                        
                        oldBean.setFundingSourceName(newBean.
                        getFundingSourceName());
                        
                        if(foundIndex != -1){
                            protocolFundingSourceData.setElementAt(oldBean,
                            foundIndex);
                        }
                    }
                }
            }
            if(protocolFundingSourceData != null &&
            protocolFundingSourceData.size() > 0){
                for(int oldLocIndex = 0;
                oldLocIndex < protocolFundingSourceData.size();
                oldLocIndex++){
                    found = false;
                    oldBean = (ProtocolFundingSourceBean)
                    protocolFundingSourceData.elementAt(oldLocIndex);
                    for(int newLocIndex = 0;
                    newLocIndex < newData.size();
                    newLocIndex++){
                        newBean = (ProtocolFundingSourceBean)
                        newData.elementAt(newLocIndex);
                        if(oldBean.getFundingSource().equals(
                        newBean.getFundingSource())){
                            found = true;
                            break;
                        }
                    }
                    if(!found){
                        /* if existing location has been deleted set acType
                        to DELETE_RECORD */
                        oldBean.setAcType("D");
                        setSaveRequired(true);
                        if(functionType == 'M'){
                            protocolInfoModified = true;
                        }
                        protocolFundingSourceData.setElementAt(oldBean,
                        oldLocIndex);
                    }
                }
            }
        }
        return protocolFundingSourceData;
        /*}
        else {
         
            return null;
        }*/
        
    }
    
    private Vector setAcTypeInAmend( Vector fundList ) {
        Vector newList = new Vector();
        ProtocolFundingSourceBean newBean;
        if( fundList != null ){
            int count = fundList.size();
            for( int indx = 0; indx < count; indx++) {
                newBean = ( ProtocolFundingSourceBean ) fundList.get(indx );
                if( newBean.getAcType() == null || !newBean.getAcType().equalsIgnoreCase("D") ) {
                    newBean.setAcType( "I" );
                    newList.addElement( newBean );
                }
            }
        }
        return newList;
        
    }
    
    
    
    /*
        This method is used to Enable or Disable the Buttons
        depending on the function Type. If the functionType is Display
        then it disables the Add, Delete, Find JButtons.
     */
    
    private void formatFields(){
        
        //Code modified for Case#3070 - Ability to change a funding source - starts
//        boolean enabled = functionType !='D' ? true : false;
        boolean enabled = (functionType !='D' && functionType !='E') ? true : false;
        //Code modified for Case#3070 - Ability to change a funding source - ends
        btnAdd.setEnabled(enabled);
        btnDelete.setEnabled(enabled);
        btnFind.setEnabled(enabled);
        btnMedusa.setEnabled(enabled);
        if(enableProposalLink==1)
            btnStartProposal.setVisible(true);
        else
            btnStartProposal.setVisible(false);
        //Added by Amit 11/18/2003
        //Code modified for Case#3070 - Ability to change a funding source - starts
//        if(functionType == CoeusGuiConstants.DISPLAY_MODE){
        if(functionType == CoeusGuiConstants.DISPLAY_MODE || functionType == CoeusGuiConstants.AMEND_MODE){
        //Code modified for Case#3070 - Ability to change a funding source - ends
            java.awt.Color bgListColor = (java.awt.Color)javax.swing.UIManager.getDefaults().get("Panel.background");
            
            tblFundSoForm.setBackground(bgListColor);
            tblFundSoForm.setSelectionBackground(bgListColor);
            tblFundSoForm.setSelectionForeground(Color.black);
        }
        else{
            tblFundSoForm.setBackground(Color.white);
            tblFundSoForm.setSelectionBackground(Color.white);
            tblFundSoForm.setSelectionForeground(Color.black);
        }
        //end Amit
        
        /**
         * Added the code
         * Updated by Raghunath P.V. july' 25 2003
         * To fix the id# DEF_05
         */
        tblFundSoForm.addMouseListener(new MouseAdapter(){
            String strSearch=null;
            public void mouseClicked(MouseEvent me){
                int selRow = tblFundSoForm.getSelectedRow();
                String stId=(String)tblFundSoForm.getModel().getValueAt(selRow,2);
                strSearch=(String)((DefaultTableModel)tblFundSoForm.
                getModel()).getValueAt(selRow,1);
                //COEUSQA:2653 - Add Protocols to Medusa - Start
                if(DEVELOPMENT_PROPOSAL.equals(strSearch) || INSTITUTE_PROPOSAL.equals(strSearch) || AWARD.equals(strSearch)) {
                    btnMedusa.setEnabled(true);
                } else {
                    btnMedusa.setEnabled(false);
                }
                //COEUSQA:2653 - End
                //Coeus Enhancement case #1787 start
                tblFundSoForm.editCellAt(selRow,2);
                tblFundSoForm.getEditorComponent().requestFocusInWindow();
                 //Coeus Enhancement case #1787 end
                if (me.getClickCount() == 2) {
                    displayFundingDetails(stId, strSearch);
                }
            }
        });
    }
    
    /** This method is used for validations.
     * After adding a row, user should enter a key study personnel without keeping it blank.
     * @return true if the validation succeed else return false.
     * @throws Exception with the given custom message.
     */
    
    public boolean validateData() throws Exception, CoeusUIException{
        int editRow = tblFundSoForm.getEditingRow();
        int editCol = tblFundSoForm.getEditingColumn();
        
        if(tblFundSoForm.getRowCount()>0){
            if( editRow!= -1 && editCol != -1 ) {
                tblFundSoForm.getCellEditor(editRow,editCol).stopCellEditing();
            }
            
            boolean valid=true;
            int rowCount = tblFundSoForm.getRowCount();
            for(int inInd=0; inInd<rowCount ;inInd++){
                String stCode=(String)((DefaultTableModel)
                tblFundSoForm.getModel()).getValueAt(inInd,2);
                if((stCode == null) || (stCode.trim().length() == 0)){
                    valid=false;
                    break;
                }
            }
            if(!valid){
                errorMessage(coeusMessageResources.parseMessageKey(
                "protoFndSrcFrm_exceptionCode.1058"));
                tblFundSoForm.requestFocus();
                return false;
            }
        }
        return true;
    }
    
    /* This method is used to set the cell editors to the columns,
       set the column width to each individual column, disable the column
       resizable feature to the JTable, setting single selection mode to the
       JTable */
    private void setTableEditors(){
        
        TableColumn column = tblFundSoForm.getColumnModel().getColumn(0);
        column.setMinWidth(30);
        column.setMaxWidth(30);
        column.setHeaderRenderer(new EmptyHeaderRenderer());
        
        column.setResizable(false);
        column.setCellRenderer(new IconRenderer());
        column.setPreferredWidth(30);
        JTableHeader header = tblFundSoForm.getTableHeader();
        header.setReorderingAllowed(false);
        //header.setResizingAllowed(false);
        tblFundSoForm.setRowHeight(24);
        tblFundSoForm.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        
        tblFundSoForm.setOpaque(false);
        tblFundSoForm.setShowVerticalLines(false);
        tblFundSoForm.setShowHorizontalLines(false);
        tblFundSoForm.setSelectionMode(
        DefaultListSelectionModel.SINGLE_SELECTION);
        
        
        TableColumn clmType = tblFundSoForm.getColumnModel().getColumn(1);
        clmType.setMinWidth(90);
        //clmType.setMaxWidth(60);
        clmType.setPreferredWidth(90);
        //clmType.setResizable(false);
        
        
        JComboBox  coeusCombo = new JComboBox();
        coeusCombo.setFont(coeusNormalFont);
        coeusCombo.setModel(new CoeusComboBox(
        getAvailableFundingSourceTypes(),
        false).getModel());
        clmType.setCellEditor(new DefaultCellEditor(coeusCombo ){
            
            public Object getCellEditorValue(){
                return ((JComboBox)getComponent()).getSelectedItem().
                toString();
            }
            
            public int getClickCountToStart(){
                return 1;
            }
        });
        
        /* This block of code contains logic for clearing the contents in
           the Funding source if the funding source type is changed in the
           JComboBox*/
        coeusCombo.addItemListener( new ItemListener(){
            // commented by manoj to fix the bug 02/09/2003
            //        boolean flag = true;
            public void itemStateChanged( ItemEvent item ){
                //              if( !flag ){ //item.getStateChange() == ItemEvent.SELECTED
                int sRow = tblFundSoForm.getEditingRow();
                if(sRow != -1){
                    
                    ((DefaultTableModel)tblFundSoForm.getModel()).
                    setValueAt("",sRow,2);
                    ((DefaultTableModel)tblFundSoForm.getModel()).
                    setValueAt("",sRow,3);
                    saveRequired = true;
                   // Coeus Enhancment case #1787 start
                    tblFundSoForm.editCellAt(sRow,2);
                    tblFundSoForm.getEditorComponent().requestFocusInWindow();
                     // Coeus Enhancment case #1787 end
                    //  flag = true;
                    if(enableProposalLink==1){
                        
                    }

                    String stSearch=null;
                    stSearch=(String)((DefaultTableModel)tblFundSoForm.
                            getModel()).getValueAt(sRow,1);
                  
                       hasCreateProposalRight = hasCreateProposalRights();
                    
                   if(DEVELOPMENTPROPOSAL_TYPE == Integer.parseInt(getIDForName(stSearch)) && hasCreateProposalRight){
                               btnStartProposal.setEnabled(true);
                            }
                   else{
                          btnStartProposal.setEnabled(false);
                   }
                }
                
                
                //                }
                //                flag = false;
            }
        });
        
        // Comments Necessary
        //Coeus Enhancement case #1787
        NameEditor nmEdtCode = new NameEditor("Number/Code",200);
        column = tblFundSoForm.getColumnModel().getColumn(2);
        //column.setMinWidth(225);
        // Added by Chandra 02/09/2003
        column.setMinWidth(100);
        
        //column.setMaxWidth(235);
        column.setPreferredWidth(100);
        // end Chandra
        //column.setResizable(false);
        column.setCellEditor(nmEdtCode);
        
        // Comments Necessary
        NameEditor nmEditName = new NameEditor("Name/Title",200);
        column = tblFundSoForm.getColumnModel().getColumn(3);
        column.setMinWidth(320);
        //column.setMaxWidth(225);
        //column.setResizable(false);
        column.setPreferredWidth(320);
        column.setCellEditor(nmEditName);
        
    }//End setTableEditor
    
    /* This method is used to get the UNIT hierarchy information
     * based on the unit number. It instantiates the RequesterBean class
     * sets the Function type as 'G' invokes the unitServlet and retrieves
     * information from the database.
     * @param id , a UnitNumber on which the information to retrieve
     */
    private Vector getUnitInfo(String id){
        //boolean success=false;
        Vector vUnitsearchData=null;
        RequesterBean requester = new RequesterBean();
        requester.setFunctionType('G');
        requester.setId(id);
        requester.setDataObject(id);
        String connectTo = connectionURL + "/unitServlet";
        AppletServletCommunicator comm
        = new AppletServletCommunicator(connectTo, requester);
        comm.send();
        ResponderBean response = comm.getResponse();
        if (response!=null){
            vUnitsearchData = new Vector();
            UnitDetailFormBean unitInfo =
            (UnitDetailFormBean) response.getDataObject();
            
            if((unitInfo != null) && (unitInfo.getUnitName() != null)){
                String stName = unitInfo.getUnitName();
                vUnitsearchData.addElement(stName);
            }
        }
        return vUnitsearchData;
    }
    
    
    /* This method is used to get the Sponsor information
     * based on the sponsor code. It instantiates the RequesterBean class
     * invokes the coeusFunctionsServlet and retrieves information from the database.
     * @param id , a UnitNumber on which the information to retrieve
     */
    private Vector getSponsorInfo(String id){
        Vector vsearchData=null;
        RequesterBean requester = new RequesterBean();
        requester.setDataObject("GET_SPONSORINFO");
        requester.setId(id);
        String connectTo = connectionURL + "/coeusFunctionsServlet";
        AppletServletCommunicator comm
        = new AppletServletCommunicator(connectTo, requester);
        comm.send();
        ResponderBean response = comm.getResponse();
        if (response!=null){
            vsearchData = new Vector();
            SponsorMaintenanceFormBean sponsorInfo =
            (SponsorMaintenanceFormBean) response.getDataObject();
            //Added for COEUSQA-1434 : Add the functionality to set a status on a Sponsor record - start
            if(sponsorInfo!=null && sponsorInfo.getName() !=null && (!(INACTIVE_STATUS.equals(sponsorInfo.getStatus())))){
                String stName = sponsorInfo.getName();
                vsearchData.addElement(stName);
            }
            //Added for COEUSQA-1434 : Add the functionality to set a status on a Sponsor record - end
        }
        return vsearchData;
    }
    //Coeus Enhancement case #1787 start
    /* This method is used to get the Award information
     * based on the MIT AWARD NUMBER. It instantiates the RequesterBean class
     * invokes the AwardMaintenanceServlet and retrieves information from the database.
     * @param id , a MitAwardNumber on which the information to retrieve
     */
    private String getAwardInfo(String id){
        // Vector vsearchData=null;
        String awardTitle = "";
        RequesterBean requester = new RequesterBean();
        requester.setFunctionType('g');
        requester.setDataObject(id);
        String connectTo = connectionURL + "/AwardMaintenanceServlet";
        AppletServletCommunicator comm
        = new AppletServletCommunicator(connectTo, requester);
        comm.send();
        ResponderBean response = comm.getResponse();
        // int isValid=1;
        if (response!=null && response.getDataObject() != null)
            // vsearchData = new Vector();
            awardTitle =(String) response.getDataObject();
        
        
        return awardTitle;
        
    }
     /* This method is used to get the Award information
      * based on the MIT AWARD NUMBER. It instantiates the RequesterBean class
      * invokes the AwardMaintenanceServlet and retrieves information from the database.
      * @param id , a MitAwardNumber on which the information to retrieve
      */
    private boolean isValidAward(String id){
        // Vector vsearchData=null;
        boolean exist = false;
        RequesterBean requester = new RequesterBean();
        requester.setFunctionType('v');
        requester.setDataObject(id);
        String connectTo = connectionURL + "/AwardMaintenanceServlet";
        AppletServletCommunicator comm
        = new AppletServletCommunicator(connectTo, requester);
        comm.send();
        ResponderBean response = comm.getResponse();
        // int isValid=1;
        if (response!=null && response.getDataObject()!=null){
            if(1 == ((Integer)response.getDataObject()).intValue())
                exist = true;
        }
        
        return exist;
        
    }
    /* This method is used to get the Institute Proposal information
     * based on the Institute Proposal number. It instantiates the RequesterBean class
     * invokes the InstituteProposalMaintenanceServlet and retrieves information from the database.
     * @param id , a ProposalNumber on which the information to retrieve
     */
    private String getInstituteProposalInfo(String id){
        //Vector vsearchData=null;
        String proposalTitle ="";
        RequesterBean requester = new RequesterBean();
        //Hashtable htData= new Hashtable();
        requester.setFunctionType('T');
        requester.setId(id);
        // InstituteProposalBean bean = new InstituteProposalBean();
        //  bean.setProposalNumber(id);
        //System.out.println("The Function Type is :"+functionType);
        // bean.setMode(functionType);
        // htData.put(InstituteProposalBean.class, bean);
        //  requester.setDataObject(htData);
        
        
        String connectTo = connectionURL + "/InstituteProposalMaintenanceServlet";
        AppletServletCommunicator comm
        = new AppletServletCommunicator(connectTo, requester);
        comm.send();
        ResponderBean response = comm.getResponse();
        // int isValid=1;
        if (response!=null && response.getDataObject()!=null){
            
            proposalTitle = (String)response.getDataObject();
            
            //  vsearchData = new Vector();
            
            //Hashtable responseHash = (Hashtable)response.getDataObject();
            // CoeusVector coeusVector =(CoeusVector)  responseHash.get(InstituteProposalBean.class);
            // if(coeusVector!=null) {
            //  InstituteProposalBean instpropInfo =(InstituteProposalBean) coeusVector.firstElement();
            
            //  if(instpropInfo!=null && instpropInfo.getTitle()!=null){
            //       String stName = instpropInfo.getTitle();
            //       vsearchData.addElement(stName);
            //   }
            
            
        }
        //}
        return proposalTitle;
        
    }
    
    private boolean isValidProposal(String id){
        //Vector vsearchData=null;
        boolean exist = false;
        RequesterBean requester = new RequesterBean();
        //Hashtable htData= new Hashtable();
        requester.setFunctionType('V');
        requester.setId(id);
        
        String connectTo = connectionURL + "/InstituteProposalMaintenanceServlet";
        AppletServletCommunicator comm
        = new AppletServletCommunicator(connectTo, requester);
        comm.send();
        ResponderBean response = comm.getResponse();
        // int isValid=1;
        if (response!=null && response.getDataObject()!=null){
            if(1 == ((Integer)response.getDataObject()).intValue())
                
                //  if("1" ==  Integer.parseInt((String)getDataObject()))
                exist = true;
            
            //  vsearchData = new Vector();
            
            //Hashtable responseHash = (Hashtable)response.getDataObject();
            // CoeusVector coeusVector =(CoeusVector)  responseHash.get(InstituteProposalBean.class);
            // if(coeusVector!=null) {
            //  InstituteProposalBean instpropInfo =(InstituteProposalBean) coeusVector.firstElement();
            
            //  if(instpropInfo!=null && instpropInfo.getTitle()!=null){
            //       String stName = instpropInfo.getTitle();
            //       vsearchData.addElement(stName);
            //   }
            
            
        }
        //}
        return exist;
        
    }
     private boolean isValidDevProposal(String id){
        //Vector vsearchData=null;
        boolean exist = false;
        RequesterBean requester = new RequesterBean();
        //Hashtable htData= new Hashtable();
        requester.setFunctionType('v');
        requester.setId(id);
        
        String connectTo = connectionURL + "/ProposalMaintenanceServlet";
        AppletServletCommunicator comm
        = new AppletServletCommunicator(connectTo, requester);
        comm.send();
        ResponderBean response = comm.getResponse();
        // int isValid=1;
        if (response!=null && response.getDataObject()!=null){
            if(1 == ((Integer)response.getDataObject()).intValue())
                
                //  if("1" ==  Integer.parseInt((String)getDataObject()))
                exist = true;
            
            //  vsearchData = new Vector();
            
            //Hashtable responseHash = (Hashtable)response.getDataObject();
            // CoeusVector coeusVector =(CoeusVector)  responseHash.get(InstituteProposalBean.class);
            // if(coeusVector!=null) {
            //  InstituteProposalBean instpropInfo =(InstituteProposalBean) coeusVector.firstElement();
            
            //  if(instpropInfo!=null && instpropInfo.getTitle()!=null){
            //       String stName = instpropInfo.getTitle();
            //       vsearchData.addElement(stName);
            //   }
            
            
        }
        //}
        return exist;
        
    }
    /* This method is used to get the development Proposal information
     * based on the proposal number. It instantiates the RequesterBean class
     * sets the Function type as 'D' invokes the ProposalMaintenanceServlet and retrieves
     * information from the database.
     * @param id , a ProposalNumber on which the information to retrieve
     */
    private String getDevelopmentProposalInfo(String id){
        //boolean success=false;
        Vector vUnitsearchData=null;
        RequesterBean requester = new RequesterBean();
        requester.setFunctionType('t');
        requester.setId(id);
        // requester.setDataObject(id);
        String connectTo = connectionURL + "/ProposalMaintenanceServlet";
        AppletServletCommunicator comm
        = new AppletServletCommunicator(connectTo, requester);
        comm.send();
        ResponderBean response = comm.getResponse();
        String proposalTitle ="";
      
            if (response!=null && response.getDataObject()!=null){
            
            proposalTitle = (String)response.getDataObject();
            
            //  vsearchData = new Vector();
            
            //Hashtable responseHash = (Hashtable)response.getDataObject();
            // CoeusVector coeusVector =(CoeusVector)  responseHash.get(InstituteProposalBean.class);
            // if(coeusVector!=null) {
            //  InstituteProposalBean instpropInfo =(InstituteProposalBean) coeusVector.firstElement();
            
            //  if(instpropInfo!=null && instpropInfo.getTitle()!=null){
            //       String stName = instpropInfo.getTitle();
            //       vsearchData.addElement(stName);
            //   }
            
            
        }
        //}
        return proposalTitle;
    }
    //Coeus Enhancement case #1787 end
    /* This method validates the data and ensures that no duplicate
     * records are entered by the user.
     * @param id, String which represents Funding source code.
     * @param selectedRow, Selected row entry in the JTable.
     * @return boolean, which represents whether the entry is duplicate or not.
     * returns true if the entry is duplicate else returns false.
     */
    private boolean checkDuplicateName(String id, int selectedRow){
        boolean duplicate = false;
        String stTestTypeDesc=(String)((DefaultTableModel)tblFundSoForm.
        getModel()).getValueAt(selectedRow,1);
        for(int rowIndex = 0; rowIndex < tblFundSoForm.getRowCount();
        rowIndex++){
            if(rowIndex==selectedRow){
                continue;
            }
            String stRemainRowType=(String)((DefaultTableModel)
            tblFundSoForm.getModel()).getValueAt(rowIndex,1);
            String stFundCode=(String)tblFundSoForm.getValueAt(rowIndex,2);
            if(stRemainRowType!=null && stFundCode!=null){
                if((stTestTypeDesc.equals(stRemainRowType)) &&
                (id.equals(stFundCode))){
                    duplicate=true;
                }
            }
        }
        return duplicate;
    }
    
    /** This method is called from within the showProtocolFundingSourceForm
     *  method to initialize the form.
     */
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;
        
        btnAdd = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        btnFind = new javax.swing.JButton();
        btnStartProposal =new javax.swing.JButton();
        javax.swing.JScrollPane scrPnPane1 = new javax.swing.JScrollPane();
        tblFundSoForm = new javax.swing.JTable();
        //  Dimension d=new Dimension(3,3);
        //  tblFundSoForm.setIntercellSpacing(d);
        tblFundSoForm.setSelectionBackground( Color.blue );
        tblFundSoForm.setSelectionForeground( Color.black );
        tblFundSoForm.setSurrendersFocusOnKeystroke(true);
        btnMedusa = new javax.swing.JButton();
        
        setLayout(new java.awt.GridBagLayout());
        /* modified to fix the bug with id: #190_6*/
        //setPreferredSize( new java.awt.Dimension( 700, 220 ));
        setPreferredSize( new java.awt.Dimension( 680, 410 ));
        // setBorder(new javax.swing.border.EtchedBorder()); To remove external border - Added by chandra
        scrPnPane1.setBorder(new javax.swing.border.TitledBorder(
        new javax.swing.border.EtchedBorder(),
        "Funding Sources",
        javax.swing.border.TitledBorder.LEFT,
        javax.swing.border.TitledBorder.TOP,
        coeusLabelFont));
        /* modified to fix the bug with id: #190_6*/
        //        scrPnPane1.setMinimumSize(new java.awt.Dimension(560, 200));
        //        scrPnPane1.setPreferredSize(new java.awt.Dimension(560, 200));
        scrPnPane1.setMinimumSize(new java.awt.Dimension(560, 405));
        scrPnPane1.setPreferredSize(new java.awt.Dimension(560, 405));
        
        btnAdd.setFont(coeusLabelFont);
        btnAdd.setText("Add");
        btnAdd.setMnemonic('A');
        btnAdd.setName("btnAdd");
        // modified by manoj to fix the bug 02/09/2003
        btnAdd.setMaximumSize(new java.awt.Dimension(90, 27));
        btnAdd.setMinimumSize(new java.awt.Dimension(90, 27));
        btnAdd.setPreferredSize(new java.awt.Dimension(90, 27));
        /* If the user presses Add button an enpty row is created in the
           JTable and the the new row added will be selected */
        btnAdd.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent ae){
                //Bug-Fix to stop cell editing before performing any actions - Start
                //Added by Vyjayanthi on 13/01/2004
                if(tblFundSoForm.isEditing()){
                    if(tblFundSoForm.getCellEditor() != null){
                        tblFundSoForm.getCellEditor().stopCellEditing();
                    }
                }
                //Bug-Fix to stop cell editing before performing any actions - End
                
                Vector newRowData = new Vector();
                if( !error ) {
                    newRowData.addElement( "" );
                    newRowData.addElement( ((ComboBoxBean)availableTypes.get( 0 )).
                    getDescription());
                    newRowData.addElement("");
                    newRowData.addElement("");
                    ((DefaultTableModel)tblFundSoForm.getModel()).
                    addRow( newRowData );
                    btnStartProposal.setEnabled(false);
                    
                    ((DefaultTableModel)tblFundSoForm.getModel()).fireTableDataChanged();
                    int lastRow = tblFundSoForm.getRowCount() - 1;
                    if(lastRow >= 0){
                        btnDelete.setEnabled(true);
                        tblFundSoForm.setRowSelectionInterval( lastRow, lastRow );
                        tblFundSoForm.scrollRectToVisible(tblFundSoForm.getCellRect(
                        lastRow ,ZERO_COUNT, true));
                    }
                    setTableEditors();
                    //added by manoj to fix the bug DEF_12 (Project Tracking.xls) 19/09/2003
                    tblFundSoForm.editCellAt(lastRow,2);
                    tblFundSoForm.getEditorComponent().requestFocusInWindow();
                    tblFundSoForm.setRowSelectionInterval( lastRow, lastRow );
                    saveRequired=true;
                    //Bug fix code
                    //Added by Vyjayanthi on 26/08/03
                    btnFind.setEnabled(true);
                }else{
                    error = false;
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
        gridBagConstraints.insets = new java.awt.Insets(27, 5, 5, 5);
        add(btnAdd, gridBagConstraints);
        
        btnDelete.setFont(coeusLabelFont);
        btnDelete.setText("Delete");
        btnDelete.setMnemonic('D');
        btnDelete.setPreferredSize(new java.awt.Dimension(90, 27));
        btnDelete.setMaximumSize(new java.awt.Dimension(90, 27));
        btnDelete.setMinimumSize(new java.awt.Dimension(90, 27));
        /* If the user presses Delete button the selected row will be deleted
           and the focus will be on the row which is prior to the deleted row */
        
        btnDelete.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                //Bug-Fix to stop cell editing before performing any actions - Start
                //Added by Vyjayanthi on 13/01/2004
                if(tblFundSoForm.isEditing()){
                    if(tblFundSoForm.getCellEditor() != null){
                        tblFundSoForm.getCellEditor().stopCellEditing();
                    }
                }
                //Bug-Fix to stop cell editing before performing any actions - End
                
                int totalRows = tblFundSoForm.getRowCount();
                /* If there are more than one row in table then delete it */
                
                //raghuSV : to stop cell editing while deleting the row....
                //starts...
                int editRow = tblFundSoForm.getEditingRow();
                int editCol = tblFundSoForm.getEditingColumn();
                if(tblFundSoForm.getRowCount()>0){
                    if( editRow!= -1 && editCol != -1 ) {
                        tblFundSoForm.getCellEditor(editRow,editCol).stopCellEditing();
                        
                    }
                }
                //ends
                if( !error ) {
                    if (totalRows > 0) {
                        /* get the selected row */
                        int selectedRow = tblFundSoForm.getSelectedRow();
                        if (selectedRow != -1) {
                            int selectedOption = CoeusOptionPane.
                            showQuestionDialog(
                            coeusMessageResources.parseMessageKey(
                            "protoFndSrcFrm_delConfirmCode.1056"),
                            CoeusOptionPane.OPTION_YES_NO,
                            CoeusOptionPane.DEFAULT_YES);
                            // if Yes then selectedOption is 0
                            // if No then selectedOption is 1
                            if (0 == selectedOption) {
                                //Added for COEUSQA-1434 : Add the functionality to set a status on a Sponsor record - start
                                 String stSearch = (String)((DefaultTableModel)tblFundSoForm.getModel()).getValueAt(selectedRow,1);
                                 if(SPONSOR_TYPE == Integer.parseInt(getIDForName(stSearch))){
                                     String sponsorCode = (String)((DefaultTableModel)tblFundSoForm.getModel()).getValueAt(selectedRow,2);
                                     vecDeletedFundSrc.add(sponsorCode);
                                 }
                                ((DefaultTableModel) tblFundSoForm.getModel()).removeRow(selectedRow);
                                //Added for COEUSQA-1434 : Add the functionality to set a status on a Sponsor record  - end
                                saveRequired = true;
                                tblFundSoForm.clearSelection();
                                // find out again row count in table
                                int newRowCount = tblFundSoForm.getRowCount();
                                if(newRowCount == 0){
                                    btnAdd.requestFocusInWindow();
                                    btnDelete.setEnabled(false);
                                    //Bug fix code
                                    //Added by Vyjayanthi on 26/08/03
                                    btnFind.setEnabled(false);
                                }else if (newRowCount > selectedRow) {
                                    // select the next row if exists
                                    (tblFundSoForm.getSelectionModel())
                                    .setSelectionInterval(selectedRow,
                                    selectedRow);
                                } else {
                                    tblFundSoForm.setRowSelectionInterval( newRowCount - 1,
                                    newRowCount -1 );
                                    tblFundSoForm.scrollRectToVisible( tblFundSoForm.getCellRect(
                                    newRowCount - 1 ,
                                    ZERO_COUNT, true));
                                }
                            }
                            
                        } // if total rows >0 and row is selected
                        else{
                            // if total rows >0 and row is not selected
                            CoeusOptionPane.showErrorDialog(
                            coeusMessageResources.parseMessageKey(
                            "protoFndSrcFrm_exceptionCode.1057"));
                        }
                    }
                }else{
                    error = false;
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
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 5, 5);
        add(btnDelete, gridBagConstraints);
        
        btnFind.setFont(coeusLabelFont);
        btnFind.setText("Find");
        btnFind.setMnemonic('F');
        btnFind.setPreferredSize(new java.awt.Dimension(90, 27));
        btnFind.setMaximumSize(new java.awt.Dimension(90, 27));
        btnFind.setMinimumSize(new java.awt.Dimension(90, 27));


        btnStartProposal.setFont(coeusLabelFont);
        btnStartProposal.setText("Start Proposal");
        btnStartProposal.setMnemonic('S');
        btnStartProposal.setPreferredSize(new java.awt.Dimension(112, 27));
        btnStartProposal.setMaximumSize(new java.awt.Dimension(112, 27));
        btnStartProposal.setMinimumSize(new java.awt.Dimension(112, 27));
  
//         btnStartProposal.addActionListener(new ActionListener()
//         {
//              public void actionPerformed(java.awt.event.ActionEvent evt) {
//                    if(tblFundSoForm.isEditing()){
//                    if(tblFundSoForm.getCellEditor() != null){
//                        tblFundSoForm.getCellEditor().stopCellEditing();
//                    }
//                }
//
//
//              }
//         });


        /* When the user press Find button then it creates a new row and based
           on the Funding type selected in the JCombobox it populates the data
           from the database*/
        btnFind.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                //Bug-Fix to stop cell editing before performing any actions - Start
                //Added by Vyjayanthi on 13/01/2004
                if(tblFundSoForm.isEditing()){
                    if(tblFundSoForm.getCellEditor() != null){
                        tblFundSoForm.getCellEditor().stopCellEditing();
                    }
                }
                //Bug-Fix to stop cell editing before performing any actions - End
                if( !error ) {
                    int inIndex=tblFundSoForm.getSelectedRow();
                    String stSearch=null;
                    if(inIndex != -1){
                        stSearch=(String)((DefaultTableModel)tblFundSoForm.
                        getModel()).getValueAt(inIndex,1);
                    }
                    if(stSearch!=null){
                        btnStartProposal.setEnabled(false);
                  
                        hasCreateProposalRight = hasCreateProposalRights();
                      
                        if(OTHER_TYPE != Integer.parseInt(getIDForName(stSearch))){
                            if(SPONSOR_TYPE == Integer.parseInt(getIDForName(stSearch))){
                                searchIdentifier = SPONSOR_SEARCH;
                            }
                            else if(UNIT_TYPE == Integer.parseInt(getIDForName(stSearch))){
                                searchIdentifier = LEAD_SEARCH;
                            }
                            //Coeus Enhancement case #1787 start
                            else if(DEVELOPMENTPROPOSAL_TYPE == Integer.parseInt(getIDForName(stSearch)) && hasCreateProposalRight){
                                searchIdentifier =
                                DEVELOPMENTPROPOSAL_SEARCH;
                                btnStartProposal.setEnabled(true);
                            }
                            else if(INSTITUTEPROPOSAL_TYPE == Integer.parseInt(getIDForName(stSearch))){
                                searchIdentifier = INSTITUTEPROPOSAL_SEARCH;
                            }
                            else if(AWARDS_TYPE == Integer.parseInt(getIDForName(stSearch))){
                                searchIdentifier = AWARDS_SEARCH;
                            }
                            //Coeus Enhancement case #1787 end
                            try{
                                CoeusSearch coeusSearch =
                                new CoeusSearch(mdiFormReference, searchIdentifier, 1);
                                coeusSearch.showSearchWindow();
                                HashMap fundingInfo = coeusSearch.getSelectedRow();
                                if(fundingInfo !=null){
                                    if(SPONSOR_TYPE == Integer.parseInt(getIDForName(stSearch))){
                                        /* set the full name of person to member name*/
                                        String stSponsorCode=Utils.
                                        convertNull(fundingInfo.get("SPONSOR_CODE"));;
                                        String stSponsorName=Utils.
                                        convertNull(fundingInfo.get("SPONSOR_NAME"));
                                        String stTypeDesc=( String)
                                        ((DefaultTableModel)tblFundSoForm.
                                        getModel()).getValueAt(inIndex,1);
                                        
                                        String stFundInfo=( String)
                                        ((DefaultTableModel)tblFundSoForm.
                                        getModel()).getValueAt(inIndex,2);
                                        
                                        boolean duplicate =
                                        checkDuplicateName(stSponsorCode,inIndex);
                                        
                                        Vector vFundInfo=null;
                                        if(stFundInfo != null && stFundInfo.trim().length() >0){
                                            if(!duplicate){
                                                vFundInfo = new Vector();
                                                vFundInfo.addElement("");
                                                vFundInfo.addElement(stTypeDesc);
                                                vFundInfo.addElement(stSponsorCode);
                                                vFundInfo.addElement(stSponsorName);
                                                
                                                ((DefaultTableModel)tblFundSoForm.getModel()).
                                                addRow(vFundInfo);
                                                ((DefaultTableModel)tblFundSoForm.getModel()).
                                                fireTableDataChanged();
                                                
                                                int newRowCount = tblFundSoForm.getRowCount();
                                                tblFundSoForm.getSelectionModel().
                                                setSelectionInterval(
                                                newRowCount - 1, newRowCount - 1);
                                                saveRequired = true;
                                            }else{
                                                CoeusOptionPane.showErrorDialog(
                                                coeusMessageResources.parseMessageKey(
                                                "protoFndSrcFrm_exceptionCode.1131"));
                                            }
                                        }else{
                                            if(!duplicate){
                                                ((DefaultTableModel)tblFundSoForm.getModel()
                                                ).setValueAt(stSponsorCode,inIndex,2);
                                                ((DefaultTableModel)tblFundSoForm.getModel()
                                                ).setValueAt(stSponsorName,inIndex,3);
                                                tblFundSoForm.getSelectionModel().
                                                setLeadSelectionIndex(inIndex);
                                                saveRequired = true;
                                            }else{
                                                CoeusOptionPane.showErrorDialog(
                                                coeusMessageResources.parseMessageKey(
                                                "protoFndSrcFrm_exceptionCode.1131"));
                                            }
                                        }
                                    }else if(UNIT_TYPE == Integer.parseInt(getIDForName(stSearch))){
                                        /* construct the full name of person */
                                        String stUnitCode=Utils.
                                        convertNull(fundingInfo.get("UNIT_NUMBER"));
                                        String stUnitName=Utils.
                                        convertNull(fundingInfo.get("UNIT_NAME"));
                                        String stTypeDes = (String)
                                        ((DefaultTableModel)tblFundSoForm.
                                        getModel()).getValueAt(inIndex,1);
                                        boolean duplicate =
                                        checkDuplicateName(stUnitCode,inIndex);
                                        String stFundInfo=( String)
                                        ((DefaultTableModel)tblFundSoForm.
                                        getModel()).getValueAt(inIndex,2);
                                        
                                        Vector vFundInfo=null;
                                        if(stFundInfo != null && stFundInfo.trim().length() >0){
                                            if(!duplicate){
                                                vFundInfo = new Vector();
                                                vFundInfo.addElement("");
                                                vFundInfo.addElement(stTypeDes);
                                                vFundInfo.addElement(stUnitCode);
                                                vFundInfo.addElement(stUnitName);
                                                
                                                ((DefaultTableModel)tblFundSoForm.
                                                getModel()).addRow(vFundInfo);
                                                ((DefaultTableModel)tblFundSoForm.
                                                getModel()).fireTableDataChanged();
                                                
                                                int newRowCount = tblFundSoForm.getRowCount();
                                                tblFundSoForm.getSelectionModel().
                                                setSelectionInterval(
                                                newRowCount - 1, newRowCount - 1);
                                                saveRequired = true;
                                            }else{
                                                CoeusOptionPane.showErrorDialog(
                                                coeusMessageResources.parseMessageKey(
                                                "protoFndSrcFrm_exceptionCode.1131"));
                                            }
                                        }else{
                                            if(!duplicate){
                                                ((DefaultTableModel)tblFundSoForm.getModel()
                                                ).setValueAt(stUnitCode,inIndex,2);
                                                ((DefaultTableModel)tblFundSoForm.getModel()
                                                ).setValueAt(stUnitName,inIndex,3);
                                                tblFundSoForm.getSelectionModel().
                                                setLeadSelectionIndex(inIndex);
                                                saveRequired = true;
                                            }else{
                                                CoeusOptionPane.showErrorDialog(
                                                coeusMessageResources.parseMessageKey(
                                                "protoFndSrcFrm_exceptionCode.1131"));
                                            }
                                        }
                                    }
                                    
                                    //Coeus Enhancement case #1787 start
                                    
                                    else if(DEVELOPMENTPROPOSAL_TYPE == Integer.parseInt(getIDForName(stSearch))){
                                        /* construct the full name of person */
                                        String stUnitCode=Utils.
                                        convertNull(fundingInfo.get("PROPOSAL_NUMBER"));
                                        String stUnitName=Utils.
                                        convertNull(fundingInfo.get("TITLE"));
                                        String stTypeDes = (String)
                                        ((DefaultTableModel)tblFundSoForm.
                                        getModel()).getValueAt(inIndex,1);
                                        boolean duplicate =
                                        checkDuplicateName(stUnitCode,inIndex);
                                        String stFundInfo=( String)
                                        ((DefaultTableModel)tblFundSoForm.
                                        getModel()).getValueAt(inIndex,2);
                                        
                                        Vector vFundInfo=null;
                                        if(stFundInfo != null && stFundInfo.trim().length() >0){
                                            if(!duplicate){
                                                vFundInfo = new Vector();
                                                vFundInfo.addElement("");
                                                vFundInfo.addElement(stTypeDes);
                                                vFundInfo.addElement(stUnitCode);
                                                vFundInfo.addElement(stUnitName);
                                                
                                                ((DefaultTableModel)tblFundSoForm.
                                                getModel()).addRow(vFundInfo);
                                                ((DefaultTableModel)tblFundSoForm.
                                                getModel()).fireTableDataChanged();
                                                
                                                int newRowCount = tblFundSoForm.getRowCount();
                                                tblFundSoForm.getSelectionModel().
                                                setSelectionInterval(
                                                newRowCount - 1, newRowCount - 1);
                                                saveRequired = true;
                                            }else{
                                                CoeusOptionPane.showErrorDialog(
                                                coeusMessageResources.parseMessageKey(
                                                "protoFndSrcFrm_exceptionCode.1131"));
                                            }
                                        }else{
                                            if(!duplicate){
                                                ((DefaultTableModel)tblFundSoForm.getModel()
                                                ).setValueAt(stUnitCode,inIndex,2);
                                                ((DefaultTableModel)tblFundSoForm.getModel()
                                                ).setValueAt(stUnitName,inIndex,3);
                                                tblFundSoForm.getSelectionModel().
                                                setLeadSelectionIndex(inIndex);
                                                saveRequired = true;
                                            }else{
                                                CoeusOptionPane.showErrorDialog(
                                                coeusMessageResources.parseMessageKey(
                                                "protoFndSrcFrm_exceptionCode.1131"));
                                            }
                                        }
                                    }
                                    else if(INSTITUTEPROPOSAL_TYPE == Integer.parseInt(getIDForName(stSearch))){
                                        /* construct the full name of person */
                                        String stUnitCode=Utils.
                                        convertNull(fundingInfo.get("PROPOSAL_NUMBER"));
                                        String stUnitName=Utils.
                                        convertNull(fundingInfo.get("TITLE"));
                                        String stTypeDes = (String)
                                        ((DefaultTableModel)tblFundSoForm.
                                        getModel()).getValueAt(inIndex,1);
                                        boolean duplicate =
                                        checkDuplicateName(stUnitCode,inIndex);
                                        String stFundInfo=( String)
                                        ((DefaultTableModel)tblFundSoForm.
                                        getModel()).getValueAt(inIndex,2);
                                        
                                        Vector vFundInfo=null;
                                        if(stFundInfo != null && stFundInfo.trim().length() >0){
                                            if(!duplicate){
                                                vFundInfo = new Vector();
                                                vFundInfo.addElement("");
                                                vFundInfo.addElement(stTypeDes);
                                                vFundInfo.addElement(stUnitCode);
                                                vFundInfo.addElement(stUnitName);
                                                
                                                ((DefaultTableModel)tblFundSoForm.
                                                getModel()).addRow(vFundInfo);
                                                ((DefaultTableModel)tblFundSoForm.
                                                getModel()).fireTableDataChanged();
                                                
                                                int newRowCount = tblFundSoForm.getRowCount();
                                                tblFundSoForm.getSelectionModel().
                                                setSelectionInterval(
                                                newRowCount - 1, newRowCount - 1);
                                                saveRequired = true;
                                            }else{
                                                CoeusOptionPane.showErrorDialog(
                                                coeusMessageResources.parseMessageKey(
                                                "protoFndSrcFrm_exceptionCode.1131"));
                                            }
                                        }else{
                                            if(!duplicate){
                                                ((DefaultTableModel)tblFundSoForm.getModel()
                                                ).setValueAt(stUnitCode,inIndex,2);
                                                ((DefaultTableModel)tblFundSoForm.getModel()
                                                ).setValueAt(stUnitName,inIndex,3);
                                                tblFundSoForm.getSelectionModel().
                                                setLeadSelectionIndex(inIndex);
                                                saveRequired = true;
                                            }else{
                                                CoeusOptionPane.showErrorDialog(
                                                coeusMessageResources.parseMessageKey(
                                                "protoFndSrcFrm_exceptionCode.1131"));
                                            }
                                        }
                                    }
                                    else if(AWARDS_TYPE == Integer.parseInt(getIDForName(stSearch))){
                                        /* construct the full name of person */
                                        String stUnitCode=Utils.
                                        convertNull(fundingInfo.get("MIT_AWARD_NUMBER"));
                                        String stUnitName=Utils.
                                        convertNull(fundingInfo.get("TITLE"));
                                        String stTypeDes = (String)
                                        ((DefaultTableModel)tblFundSoForm.
                                        getModel()).getValueAt(inIndex,1);
                                        boolean duplicate =
                                        checkDuplicateName(stUnitCode,inIndex);
                                        String stFundInfo=( String)
                                        ((DefaultTableModel)tblFundSoForm.
                                        getModel()).getValueAt(inIndex,2);
                                        
                                        Vector vFundInfo=null;
                                        if(stFundInfo != null && stFundInfo.trim().length() >0){
                                            if(!duplicate){
                                                vFundInfo = new Vector();
                                                vFundInfo.addElement("");
                                                vFundInfo.addElement(stTypeDes);
                                                vFundInfo.addElement(stUnitCode);
                                                vFundInfo.addElement(stUnitName);
                                                
                                                ((DefaultTableModel)tblFundSoForm.
                                                getModel()).addRow(vFundInfo);
                                                ((DefaultTableModel)tblFundSoForm.
                                                getModel()).fireTableDataChanged();
                                                
                                                int newRowCount = tblFundSoForm.getRowCount();
                                                tblFundSoForm.getSelectionModel().
                                                setSelectionInterval(
                                                newRowCount - 1, newRowCount - 1);
                                                saveRequired = true;
                                            }else{
                                                CoeusOptionPane.showErrorDialog(
                                                coeusMessageResources.parseMessageKey(
                                                "protoFndSrcFrm_exceptionCode.1131"));
                                            }
                                        }else{
                                            if(!duplicate){
                                                ((DefaultTableModel)tblFundSoForm.getModel()
                                                ).setValueAt(stUnitCode,inIndex,2);
                                                ((DefaultTableModel)tblFundSoForm.getModel()
                                                ).setValueAt(stUnitName,inIndex,3);
                                                tblFundSoForm.getSelectionModel().
                                                setLeadSelectionIndex(inIndex);
                                                saveRequired = true;
                                            }else{
                                                CoeusOptionPane.showErrorDialog(
                                                coeusMessageResources.parseMessageKey(
                                                "protoFndSrcFrm_exceptionCode.1131"));
                                            }
                                        }
                                    }
                                    
                                    //Coeus Enhancement case #1787 end
                                }
                            }catch(Exception e){
                            }
                            saveRequired = true;
                            if(tblFundSoForm.getCellEditor() !=null){
                                tblFundSoForm.getCellEditor().
                                cancelCellEditing();
                            }
                        }
                    }
                }else{
                    error = false;
                }
            }//action performed
        });//ActionListener
        
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 1;
        gridBagConstraints.gridheight = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 5, 5);
        add(btnFind, gridBagConstraints);
        
        //COEUSQA:2653 - Add Protocols to Medusa - Start
        btnMedusa.setFont(coeusLabelFont);
        btnMedusa.setText("Medusa");
        btnMedusa.setMnemonic('U');
        btnMedusa.setName("btnMedusa");
        btnMedusa.setMaximumSize(new java.awt.Dimension(112, 27));
        btnMedusa.setMinimumSize(new java.awt.Dimension(112, 27));
        btnMedusa.setPreferredSize(new java.awt.Dimension(112, 27));
        
        btnMedusa.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent ae){
                
                if(tblFundSoForm.isEditing()){
                    if(tblFundSoForm.getCellEditor() != null){
                        tblFundSoForm.getCellEditor().stopCellEditing();
                    }
                }
                
                if( !error ) {
                    int inIndex=tblFundSoForm.getSelectedRow();
                    String stMedusa=null;
                    if(inIndex != -1){
                        stMedusa=(String)((DefaultTableModel)tblFundSoForm.
                                getModel()).getValueAt(inIndex,1);
                    }
                    if(stMedusa!=null){
                        btnStartProposal.setEnabled(false);
                        if(OTHER_TYPE != Integer.parseInt(getIDForName(stMedusa))){
                            if(DEVELOPMENTPROPOSAL_TYPE == Integer.parseInt(getIDForName(stMedusa))){
                                String devProposalNumber = (String)((DefaultTableModel)tblFundSoForm.getModel()).getValueAt(inIndex,2);
                                showMedusaWindowForDevProposal(devProposalNumber);
                            } else if(INSTITUTEPROPOSAL_TYPE == Integer.parseInt(getIDForName(stMedusa))){
                                String instituteProposal = (String)((DefaultTableModel)tblFundSoForm.getModel()).getValueAt(inIndex,2);
                                showMedusaWindowForInstProposal(instituteProposal);
                            } else if(AWARDS_TYPE == Integer.parseInt(getIDForName(stMedusa))){
                                String awardNumber = (String)((DefaultTableModel)tblFundSoForm.getModel()).getValueAt(inIndex,2);
                                showMedusaWindowForAwardNumber(awardNumber);
                            }
                            saveRequired = true;
                            if(tblFundSoForm.getCellEditor() !=null){
                                tblFundSoForm.getCellEditor().
                                        cancelCellEditing();
                            }
                        }
                    }
                }else{
                    error = false;
                }
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 1;
        gridBagConstraints.gridheight = 1;
     
  //      gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 5, 5);
        add(btnMedusa, gridBagConstraints);                
        //COEUSQA:2653 - End

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 1;
        gridBagConstraints.gridheight = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 5, 5);
        add(btnStartProposal, gridBagConstraints);
        btnStartProposal.setEnabled(false);
        tblFundSoForm.setFont(coeusNormalFont);
        /* It creates a new DefaultTableModel with the column names */
        tblFundSoForm.setModel(new DefaultTableModel(new String[][]{},
        new String [] {"Icon", "Type", "Number/Code", "Name/Title"}
        ){
            public boolean isCellEditable(int row, int col){
                /* in display mode editing of  table fields will be disabled */
                // if( (AWARDS_TYPE == Integer.parseInt(getIDForName(stSearch)))||
                //  (INSTITUTEPROPOSAL_TYPE == Integer.parseInt(getIDForName(stSearch)))||
                //  (DEVELOPMENTPROPOSAL_TYPE == Integer.parseInt(getIDForName(stSearch))))
                //Code modified for Case#3070 - Ability to change a funding source - starts
//                if((functionType == 'D' || col == 0 || col == 3)  ){
                if((functionType == 'D' || functionType == 'E' || col == 0 || col == 3)  ){
                //Code modified for Case#3070 - Ability to change a funding source - ends
                    return false;
                }else{
                    return true;
                }
            }
            
            public int getClickCountToStart(){
                return 1;
            }
        }
        );
        
        scrPnPane1.setViewportView(tblFundSoForm);
        
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.gridheight = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        add(scrPnPane1, gridBagConstraints);
        // Added by Chandra 12/09/2003
        java.awt.Component[] components = {tblFundSoForm,btnAdd,btnDelete,btnFind,btnStartProposal,btnMedusa};
        ScreenFocusTraversalPolicy  traversePolicy = new ScreenFocusTraversalPolicy( components );
        setFocusTraversalPolicy(traversePolicy);
        //Coeus Enhancement case #1787 start
        setTableKeyTraversal();
        //Coeus Enhancement case #1787 end
        setFocusCycleRoot(true);
        // End Chandra
    }
    
    //Coeus Enhancement case #1787 start
    // This method will provide the key travrsal for the table cells
    // It specifies the tab and shift tab order.
    public void setTableKeyTraversal(){
        
        javax.swing.InputMap im = tblFundSoForm.getInputMap(JTable.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        // Have the enter key work the same as the tab key
        KeyStroke tab = KeyStroke.getKeyStroke(KeyEvent.VK_TAB, 0);
        KeyStroke shiftTab = KeyStroke.getKeyStroke(KeyEvent.VK_TAB,KeyEvent.SHIFT_MASK );
        
        // Override the default tab behaviour
        // Tab to the next editable cell. When no editable cells goto next cell.
        final Action oldTabAction = tblFundSoForm.getActionMap().get(im.get(tab));
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
                    
                    // Back to where we started, get out.
                    
                    if (row == table.getSelectedRow()
                    && column == table.getSelectedColumn()) {
                        break;
                    }
                }
                
                table.changeSelection(row, column,false, false);
                table.editCellAt(row,column);
                
            }
        };
        tblFundSoForm.getActionMap().put(im.get(tab), tabAction);
        
        
        
        
        // for the shift+tab action
        
        // Override the default tab behaviour
        // Tab to the previous editable cell. When no editable cells goto next cell.
        
        final Action oldTabAction1 = tblFundSoForm.getActionMap().get(im.get(shiftTab));
        Action tabAction1 = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                oldTabAction1.actionPerformed( e );
                JTable table = (JTable)e.getSource();
                int rowCount = table.getRowCount();
                int columnCount = table.getColumnCount();
                int row = table.getSelectedRow();
                int column = table.getSelectedColumn();
                
                while (! table.isCellEditable(row, column) ) {
                    
                    column -= 1;
                    
                    if (column <= 0) {
                        column = 5;
                        row -=1;
                    }
                    
                    if (row < 0) {
                        row = rowCount-1;
                    }
                    
                    // Back to where we started, get out.
                    
                    if (row == table.getSelectedRow()
                    && column == table.getSelectedColumn()) {
                        break;
                    }
                }
                
                table.changeSelection(row, column, true, true);
                table.editCellAt(row,column);
            }
        };
        tblFundSoForm.getActionMap().put(im.get(shiftTab), tabAction1);
        
        
       java.awt.Component[] components = {tblFundSoForm,btnAdd,btnDelete,btnStartProposal,btnMedusa};
        ScreenFocusTraversalPolicy  traversePolicy = new ScreenFocusTraversalPolicy( components );
        setFocusTraversalPolicy(traversePolicy);
        setFocusCycleRoot(true);
        
        
    }
    //Coeus Enhancement case #1787 end
    
    /** This method is used to show the alert messages to the user
     * @param mesg a String message to alert the user.
     * @throws Exception a exception to be thrown in the client side.
     */
    
    public static void errorMessage(String mesg) throws CoeusUIException {
        //Commented by sharath(20 - Aug - 2003)
        //throw new Exception(mesg);
        
        //Bug Fix ( Defect Id : 379)
        CoeusUIException coeusUIException = new CoeusUIException(mesg,CoeusUIException.WARNING_MESSAGE);
        coeusUIException.setTabIndex(5);
        throw coeusUIException;
        //Bug Fix ( Defect Id : 379)
    }

    /**
     * @return the enableProposalLink
     */
    public int getEnableProposalLink() {
        return enableProposalLink;
    }

    /**
     * @param enableProposalLink the enableProposalLink to set
     */
    public void setEnableProposalLink(int enableProposalLink) {
        this.enableProposalLink = enableProposalLink;
    }

   
    
    
    /**
     * Inner class which is used to provide empty header for the Icon Column.
     */
    
    static class EmptyHeaderRenderer extends JList implements TableCellRenderer {
        /**
         * Default constructor to set the default foreground/background
         * and border properties of this renderer for a cell.
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
        Object value,boolean isSelected, boolean hasFocus, int row, int column){
            return this;
        }
    }//End EmptyHeaderRenderer Inner Class
    
    /**
     * Inner class which is used to provide Icon to the JTable column as Renderer.
     * It extends DefaultTableCellRenderer.
     * This class overides getTableCellRendererComponent method which returns the
     * renderer component to the JTable Column.
     */
    
    static class IconRenderer  extends DefaultTableCellRenderer {
        
        //        private final ImageIcon HAND_ICON =
        //        new ImageIcon(getClass().getClassLoader().getResource(
        //        CoeusGuiConstants.HAND_ICON));
        
        private final ImageIcon HAND_ICON =
        new ImageIcon(getClass().getClassLoader().getResource(
        CoeusGuiConstants.HAND_ICON));
        private final ImageIcon EMPTY_ICON = null;
        /** Default Constructor*/
        IconRenderer() {
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
         * @return Component
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
                setIcon(HAND_ICON);
            }else{
                setIcon(EMPTY_ICON);
            }
            return this;
            
        }
        
    }//End Icon Rendering inner class
    
    /**
     * Inner Class used to provide textField as cell editor.
     * It extends DefaulCellEditor and implements TableCellEditor interface.
     * This class overides getTableCellEditorComponent method which returns the
     * editor component to the JTable Column.
     */
    
    class NameEditor extends DefaultCellEditor implements TableCellEditor {
        
        private JTextField txtCode;
        int selRow=0;
        String stSearch=null;
        String stTempCodeValue=null;
        String stTempCodeDesc=null;
        boolean temporary;
        // Comments Necessary
        NameEditor(final String colName, final int len ){
            super( new JTextField() );
            /* modified to fix the bug with id: #190_4 and #190_6*/
            //Coeus Enhancement case #1787 
            if(colName.equalsIgnoreCase("Number/Code")){
                String strTypeCode = null;
                txtCode = new JTextField();
                txtCode.setFont(coeusNormalFont);
                txtCode.setDocument(new LimitedPlainDocument(len));
                txtCode.setSelectionColor(Color.BLUE);
                /* If the user double clicks in the textField this is used
                   to populate the JDialag based on the funding type
                   in the JCombobox.
                   If the Funding Type is Sponsor it shows the Sponsor window
                   in the display mode.
                   If the Funding Type is Unit it shows the Unit window
                   in the display mode.
                 */
                txtCode.addMouseListener(new MouseAdapter(){
                    String strSearch=null;
                    public void mouseClicked(MouseEvent me){
                        //System.out.println("mouse clicked in Code");
                        String stId=(String)tblFundSoForm.getModel().
                        getValueAt(selRow,2);
                        strSearch=(String)((DefaultTableModel)tblFundSoForm.
                        getModel()).getValueAt(selRow,1);
                        if (me.getClickCount() == 2) {
                            displayFundingDetails(stId, strSearch);
                        }//End Mouse Click 2
                    }//End Mouse Clicked Event
                });//End Mouse Adaptor
                /* If the user press the return key in textField this is invoked */
                
                txtCode.addActionListener(new ActionListener() {
                    String stSearch=null;
                    //Vector vecData=new Vector();
                    public void actionPerformed(ActionEvent ae) {
                        // modfied by manoj to fix the bug id#27 IRB-SystemTestingDL-01.xls
                       
                                // getFundingSourceInfo(txtCode.getText());
                       // txtCode.grabFocus();
                    }//end action performed
                });
                
                txtCode.addFocusListener(new FocusAdapter(){
                    public void focusGained(FocusEvent fe){
                    //temporary = false;
                }
                    public void focusLost(FocusEvent fe){
                        if (!fe.isTemporary() ){
                          // if(!temporary){
                          //  stopCellEditing();
                       // }
                            // modified by manoj as it is not required when fixing
                            // the bug id#27 IRB-SystemTestingDl-01.xls
                          //  getFundingSourceInfo(txtCode.getText();
                        }
                    }
                });
                
                /* modified to fix the bug with id: #190_4*/
            }else if(colName.equalsIgnoreCase("Name/Title")){
                txtCode = new JTextField();
                txtCode.setEditable(false);
                txtCode.setFont(coeusNormalFont);
                txtCode.setDocument(new LimitedPlainDocument(len));
                txtCode.addMouseListener(new MouseAdapter(){
                    String strSearch=null;
                    public void mouseClicked(MouseEvent me){
                        //System.out.println("mouse clicked in Code");
                        String stId=(String)tblFundSoForm.getModel().
                        getValueAt(selRow,2);
                        strSearch=(String)((DefaultTableModel)tblFundSoForm.
                        getModel()).getValueAt(selRow,1);
                        //if (me.getClickCount() == 2) {
                        displayFundingDetails(stId, strSearch);
                        //}//End Mouse Click 2
                    }//End Mouse Clicked Event
                });//End Mouse Adaptor
                
                //Bug Fix
                /* If the user press the return key in textField this is invoked */
                txtCode.addActionListener(new ActionListener() {
                    String stSearch=null;
                    //Vector vecData=new Vector();
                    public void actionPerformed(ActionEvent ae) {
                        String stId, strSearch;
                        stId=(String)tblFundSoForm.getModel().
                        getValueAt(selRow,2);
                        strSearch=(String)((DefaultTableModel)tblFundSoForm.
                        getModel()).getValueAt(selRow,1);
                        displayFundingDetails(stId, strSearch);
                        
                    }//end action performed
                });
                //Bug Fixed!!
            }
        }//End Constructor
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
            stTempCodeValue = (String)tblFundSoForm.getValueAt(row,2);
            stTempCodeDesc = (String)tblFundSoForm.getValueAt(row,3);
            String newValue = ( String ) value ;
            if( newValue != null && newValue.length() > 0 ){
                txtCode.setText( (String)value );
            }else{
                txtCode.setText("");
            }
            txtCode.grabFocus();
            return txtCode;
        }
        
        
        
        
        /**
         * Forwards the message from the CellEditor to the delegate. Tell the
         * editor to stop editing and accept any partially edited value as the
         * value of the editor.
         * @return true if editing was stopped; false otherwise
         */
        public boolean stopCellEditing() {
            // modified by manoj to fix bug id#27 IRB-SystemTesting DL-01.xls
            getFundingSourceInfo(txtCode.getText());
            //txtCode.grabFocus();
            return super.stopCellEditing();
        }
        
        
        /* Method to get funding source information based
           on the funding source type selected in the JCombobox for the selected
           row. This method is called when the user press Return key, or tab
           out of the textField and in the focus lost */
        // modfied by manoj to fix bug id #27 IRB-SystemTestingDL-01.xls
        private void getFundingSourceInfo(String strId){
           
                  System.out.println("Coming Funding Source Info");
            if(tblFundSoForm.getRowCount()>0){
                System.out.println("Table has more than zero rows");
                Vector vecData=new Vector();
                //String strId = txtCode.getText().trim();
                String stNm=null;
                
                if (strId== null || strId.trim().length()<=0 ) {
                    
                    
                    tblFundSoForm.getModel().
                    setValueAt(stTempCodeValue == null? "":stTempCodeValue ,
                    selRow,2);
                    
                    tblFundSoForm.getModel().
                    setValueAt(stTempCodeDesc == null? "":stTempCodeDesc,
                    selRow,3);
                    
                    //                tblFundSoForm.getSelectionModel().
                    //                                        setLeadSelectionIndex(selRow);
                    if(tblFundSoForm.getCellEditor() != null){
                        tblFundSoForm.getCellEditor().cancelCellEditing();
                    }
                    error = false;
                } else if(tblFundSoForm.getRowCount()>0){
                    //Bug Fix code
                    
                    String code = (String)tblFundSoForm.getValueAt(tblFundSoForm.getSelectedRow(), NAME_COLUMN);
                    stSearch= (String)((DefaultTableModel)tblFundSoForm.
                    getModel()).getValueAt(selRow,1);
                    boolean duplicate = checkDuplicateName(
                    strId,selRow);
                    if(!duplicate){
                        if(SPONSOR_TYPE == Integer.parseInt(getIDForName(stSearch))){
                            //Added for COEUSQA-1434 : Add the functionality to set a status on a Sponsor record - start
                            boolean exitingSponsor = false;;
                            String sponsorCode = strId.trim();
                            if(protocolFundingSourceData != null && !protocolFundingSourceData.isEmpty()){
                                for(Object protocolFundinSourceData : protocolFundingSourceData){
                                    ProtocolFundingSourceBean fundingSourceBean = (ProtocolFundingSourceBean)protocolFundinSourceData;
                                    if(fundingSourceBean.getFundingSourceTypeCode() == SPONSOR_TYPE && 
                                            sponsorCode.equals(fundingSourceBean.getFundingSource())){
                                        exitingSponsor = true;
                                        break;
                                    }
                                }
                            }
                            if(exitingSponsor){
                                if(vecDeletedFundSrc != null && !vecDeletedFundSrc.isEmpty()){
                                    for(Object deletedSponsor:vecDeletedFundSrc){
                                        if(sponsorCode.equals(deletedSponsor.toString())){
                                            exitingSponsor = false;
                                            break;
                                        }
                                    }
                                }
                            }
                            if(!exitingSponsor){
                                vecData=getSponsorInfo(strId);
                                if(!((vecData.isEmpty()) || (vecData.size()<0))){
                                    stNm=(String)vecData.get(0);
                                    tblFundSoForm.getModel().
                                            setValueAt(strId,selRow,2);
                                    tblFundSoForm.getModel().
                                            setValueAt(stNm,selRow,3);
                                    tblFundSoForm.getSelectionModel().
                                            setLeadSelectionIndex(selRow);
                                    saveRequired = true;
                                    if(tblFundSoForm.getCellEditor()!=null){
                                        tblFundSoForm.getCellEditor().
                                                cancelCellEditing();
                                    }
                                    error = false;
                                }else{
                                    
                                    JOptionPane.showMessageDialog(mdiFormReference,
                                            coeusMessageResources.parseMessageKey(
                                            "protoFndSrcFrm_exceptionCode.1132"),
                                            "Coeus",
                                            JOptionPane.ERROR_MESSAGE);
                                    tblFundSoForm.getModel().
                                            setValueAt(stTempCodeValue,selRow,2);
                                    tblFundSoForm.getModel().
                                            setValueAt(stTempCodeDesc,selRow,3);
                                    tblFundSoForm.getSelectionModel().
                                            setLeadSelectionIndex(selRow);
                                    
                                    if(tblFundSoForm.getCellEditor()!=null){
                                        tblFundSoForm.getCellEditor().
                                                cancelCellEditing();
                                    }
                                    setRequestFocusInSpecialReviewThread(selRow,2);
                                    error = true;
                                }
                            }
                            //Added for COEUSQA-1434 : Add the functionality to set a status on a Sponsor record - end
                        }else if(UNIT_TYPE == Integer.parseInt(getIDForName(stSearch))){
                            
                            vecData=getUnitInfo(strId);
                            if(!vecData.isEmpty() || (vecData.size()<0)){
                                stNm=(String)vecData.get(0);
                                tblFundSoForm.getModel().
                                setValueAt(strId,selRow,2);
                                tblFundSoForm.getModel().
                                setValueAt(stNm,selRow,3);
                                saveRequired = true;
                                tblFundSoForm.getSelectionModel().
                                setLeadSelectionIndex(selRow);
                                if(tblFundSoForm.getCellEditor()!=null){
                                    tblFundSoForm.getCellEditor().
                                    cancelCellEditing();
                                }
                                error = false;
                            }else{
                                
                                JOptionPane.showMessageDialog(mdiFormReference,
                                coeusMessageResources.parseMessageKey(
                                "protoFndSrcFrm_exceptionCode.1133"),
                                "Coeus",
                                JOptionPane.ERROR_MESSAGE);
                                tblFundSoForm.getModel().
                                setValueAt(stTempCodeValue,selRow,2);
                                tblFundSoForm.getModel().
                                setValueAt(stTempCodeDesc,selRow,3);
                                tblFundSoForm.getSelectionModel().
                                setLeadSelectionIndex(selRow);
                                if(tblFundSoForm.getCellEditor()!=null){
                                    tblFundSoForm.getCellEditor().
                                    cancelCellEditing();
                                }
                                setRequestFocusInSpecialReviewThread(selRow,2);
                                error = true;
                            }
                        }
                        //Coeus Enhancement case #1787 start
                        if(AWARDS_TYPE == Integer.parseInt(getIDForName(stSearch))){
                            
                            if(isValidAward(strId)) {
                                
                                String title = getAwardInfo(strId);
                                //stNm=(String)vecData.get(0);
                                tblFundSoForm.getModel().
                                setValueAt(strId,selRow,2);
                                tblFundSoForm.getModel().
                                setValueAt(title,selRow,3);
                                tblFundSoForm.getSelectionModel().
                                setLeadSelectionIndex(selRow);
                                saveRequired = true;
                                if(tblFundSoForm.getCellEditor()!=null){
                                    tblFundSoForm.getCellEditor().
                                    cancelCellEditing();
                                }
                                
                            }
                            
                            else{
                                
                                JOptionPane.showMessageDialog(mdiFormReference,
                                coeusMessageResources.parseMessageKey(
                                "protoFndSrcFrm_exceptionCode.1135"),
                                "Coeus",
                                JOptionPane.ERROR_MESSAGE);
                                tblFundSoForm.getModel().
                                setValueAt(stTempCodeValue,selRow,2);
                                tblFundSoForm.getModel().
                                setValueAt(stTempCodeDesc,selRow,3);
                                tblFundSoForm.getSelectionModel().
                                setLeadSelectionIndex(selRow);
                                
                                if(tblFundSoForm.getCellEditor()!=null){
                                    tblFundSoForm.getCellEditor().
                                    cancelCellEditing();
                                }
                                setRequestFocusInSpecialReviewThread(selRow,2);
                                error = true;
                            }
                        }
                        if(INSTITUTEPROPOSAL_TYPE == Integer.parseInt(getIDForName(stSearch))){
                            if(isValidProposal(strId)) {
                                
                                String title = getInstituteProposalInfo(strId);
                                //stNm=(String)vecData.get(0);
                                tblFundSoForm.getModel().
                                setValueAt(strId,selRow,2);
                                tblFundSoForm.getModel().
                                setValueAt(title,selRow,3);
                                tblFundSoForm.getSelectionModel().
                                setLeadSelectionIndex(selRow);
                                tblFundSoForm.getSelectionModel().
                                setLeadSelectionIndex(selRow);
                                saveRequired = true;
                                if(tblFundSoForm.getCellEditor()!=null){
                                    tblFundSoForm.getCellEditor().
                                    cancelCellEditing();
                                }
                            }
                            
                            else{
                                
                                JOptionPane.showMessageDialog(mdiFormReference,
                                coeusMessageResources.parseMessageKey(
                                "protoFndSrcFrm_exceptionCode.1134"),
                                "Coeus",
                                JOptionPane.ERROR_MESSAGE);
                                tblFundSoForm.getModel().
                                setValueAt(stTempCodeValue,selRow,2);
                                tblFundSoForm.getModel().
                                setValueAt(stTempCodeDesc,selRow,3);
                                tblFundSoForm.getSelectionModel().
                                setLeadSelectionIndex(selRow);
                                
                                if(tblFundSoForm.getCellEditor()!=null){
                                    tblFundSoForm.getCellEditor().
                                    cancelCellEditing();
                                }
                                setRequestFocusInSpecialReviewThread(selRow,2);
                                error = true;
                            }
                        }
                        if(DEVELOPMENTPROPOSAL_TYPE == Integer.parseInt(getIDForName(stSearch))){
                                if(isValidDevProposal(strId)) {
                                
                                String title = getDevelopmentProposalInfo(strId);
                                //stNm=(String)vecData.get(0);
                                tblFundSoForm.getModel().
                                setValueAt(strId,selRow,2);
                                tblFundSoForm.getModel().
                                setValueAt(title,selRow,3);
                                tblFundSoForm.getSelectionModel().
                                setLeadSelectionIndex(selRow);
                                tblFundSoForm.getSelectionModel().
                                setLeadSelectionIndex(selRow);
                                saveRequired = true;
                                if(tblFundSoForm.getCellEditor()!=null){
                                    tblFundSoForm.getCellEditor().
                                    cancelCellEditing();
                                }
                            }else{
                                
                                JOptionPane.showMessageDialog(mdiFormReference,
                                coeusMessageResources.parseMessageKey(
                                "protoFndSrcFrm_exceptionCode.1134"),
                                "Coeus",
                                JOptionPane.ERROR_MESSAGE);
                               tblFundSoForm.getModel().
                                setValueAt(stTempCodeValue,selRow,2);
                                tblFundSoForm.getModel().
                                setValueAt(stTempCodeDesc,selRow,3);
                                tblFundSoForm.getSelectionModel().
                                setLeadSelectionIndex(selRow);
                                
                                if(tblFundSoForm.getCellEditor()!=null){
                                    tblFundSoForm.getCellEditor().
                                    cancelCellEditing();
                                }
                               setRequestFocusInSpecialReviewThread(selRow,2);
                                error = true;
                            }
                        }
                        //Coeus Enhancement case #1787 end
                        else if(OTHER_TYPE == Integer.parseInt(getIDForName(stSearch))){
                            
                            tblFundSoForm.getModel().
                            setValueAt(strId == null? "":strId ,
                            selRow,2);
                            tblFundSoForm.getSelectionModel().
                            setLeadSelectionIndex(selRow);
                            if(tblFundSoForm.getCellEditor()!=null){
                                tblFundSoForm.getCellEditor().
                                cancelCellEditing();
                            }
                            
                            super.cancelCellEditing();
                        }//for other
                    }else{
                        
                        JOptionPane.showMessageDialog(mdiFormReference,
                        coeusMessageResources.parseMessageKey(
                        "protoFndSrcFrm_exceptionCode.1131"),
                        "Coeus",
                        JOptionPane.ERROR_MESSAGE);
                        if(tblFundSoForm.getCellEditor() !=null){
                            tblFundSoForm.getCellEditor().cancelCellEditing();
                        }
                        tblFundSoForm.getModel().
                        setValueAt("",selRow,2);
                        tblFundSoForm.getModel().
                        setValueAt("",selRow,3);
                        error = true;
                         tblFundSoForm.getSelectionModel().
                        setLeadSelectionIndex(selRow);
                    }
                   
                }//End StrId checked
                
            }
        }
        
        /** Returns the value contained in the editor.
         * @return the value contained in the editor
         */
        public Object getCellEditorValue() {
            
            return ((JTextField)txtCode).getText();
        }
        
        /** Fires when the itemStateChanged in the JComboBox.
         * @param e  an ItemEvent
         */
        public void itemStateChanged(ItemEvent e) {
            super.fireEditingStopped();
        }
    }
    
    /**
     * This is a private method used to display the Dialog with the Unit
     * or Sponser details based on the parameters passed.
     * modified to fix the bug with id: #190_4
     */
    private void displayFundingDetails(String stId, String stSearch){
        if(tblFundSoForm.getCellEditor()!=null){
            tblFundSoForm.getCellEditor().
            cancelCellEditing();
        }
        if (stId == null || stId.equals("")){
            CoeusOptionPane.showErrorDialog(
            coeusMessageResources.parseMessageKey(
            "protoFndSrcFrm_exceptionCode.1062"));
        }else if(SPONSOR_TYPE == Integer.parseInt(getIDForName(stSearch))){
            SponsorMaintenanceForm frmSponsor =
            new SponsorMaintenanceForm('D',stId);
            frmSponsor.
            showForm(mdiFormReference,DISPLAY_SPONSOR_TITLE,true);
        } else if(UNIT_TYPE == Integer.parseInt(getIDForName(stSearch))){
            
            try{
                UnitDetailForm frmUnit =
                new UnitDetailForm(stId,'G');
                frmUnit.showUnitForm(mdiFormReference);
            } catch(Exception ex){
                CoeusOptionPane.
                showErrorDialog(coeusMessageResources.parseMessageKey(
                "protoFndSrcFrm_exceptionCode.1061"));
                
            }//End Catch
        }//End Unit Search
        //Coeus Enhancement case #1787 start
        else if(INSTITUTEPROPOSAL_TYPE == Integer.parseInt(getIDForName(stSearch))){
            
            try{
                displayInstituteProposal(stId);
            } catch(Exception ex){
                CoeusOptionPane.
                showErrorDialog(coeusMessageResources.parseMessageKey(
                "protoFndSrcFrm_exceptionCode.1063"));
                
            }//End Catch
        }//End Unit Search
        else if(AWARDS_TYPE == Integer.parseInt(getIDForName(stSearch))){
            
            try{
                displayAward(stId);
            } catch(Exception ex){
                CoeusOptionPane.
                showErrorDialog(coeusMessageResources.parseMessageKey(
                "protoFndSrcFrm_exceptionCode.1062"));
                
            }//End Catch
        }//End Unit Search
        
        
        else if(DEVELOPMENTPROPOSAL_TYPE == Integer.parseInt(getIDForName(stSearch))){
            
            try{
                ProposalDetailForm frmProposal =
                new ProposalDetailForm('D',stId,mdiFormReference);
                frmProposal.showDialogForm();
                
            } catch(Exception ex){
                CoeusOptionPane.
                showErrorDialog(coeusMessageResources.parseMessageKey(
                "protoFndSrcFrm_exceptionCode.1063"));
                
            }//End Catch
        }//End Unit Search
        
    }
     /** this method sets focus back to component
         * @return void
         */
        private void setRequestFocusInSpecialReviewThread(final int selrow , final int selcol){
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                     tblFundSoForm.editCellAt(selrow, selcol);
                     tblFundSoForm.getEditorComponent().requestFocusInWindow();
                   // tblFundSoForm.requestFocusInWindow();
                  //  tblFundSoForm.changeSelection( selrow, selcol,  false, false);
                    
                    //tblFundSoForm.setEditingColumn(2);                 
                    tblFundSoForm.setRowSelectionInterval(selrow, selrow);
                }
            });
        }
    
    //Coeus Enhancement case #1787 end
    
    //Coeus Enhancement case #1787 start
    /**
     * This is a private method used to get Display the award
     * details based on the parameters passed.
     * Coeus Enhancement case #1787
     */
    private void displayAward(String awardNumber) {
        //Code added for Case#3388 - Implementing authorization check at department level - starts
        //Check the user is having rights to view this award
        if(!canViewAward(awardNumber)){
            CoeusOptionPane.showErrorDialog(coeusMessageResources.parseMessageKey("repRequirements_exceptionCode.1056"));
            return;
        }
        //Code added for Case#3388 - Implementing authorization check at department level - ends        
        AwardBean awardBean = new AwardBean();
        awardBean.setMitAwardNumber(awardNumber);
        mdiFormReference.setCursor(new Cursor(java.awt.Cursor.WAIT_CURSOR));
        AwardBaseWindowController awardBaseWindowController = new AwardBaseWindowController("Display Award : ", 'D', awardBean);
        mdiFormReference.setCursor(new Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        //  awardBaseWindowController.setMaintainReporting(maintainReporting);
        awardBaseWindowController.display();
    }
    /**
     * This is a private method used to get Display the Institute Proposal
     * details based on the parameters passed.
     * Coeus Enhancement case #1787
     */
    private void displayInstituteProposal(String proposalNumber) throws CoeusClientException,CoeusException{
        
        //Code added for Case#3388 - Implementing authorization check at department level - starts
        if(!canViewProposal(proposalNumber)){
            CoeusOptionPane.showErrorDialog(coeusMessageResources.parseMessageKey("proposal_BaseWin_exceptionCode.7104"));
            return;
        }        
        //Code added for Case#3388 - Implementing authorization check at department level - ends        
        InstituteProposalBean instituteProposalBean= new InstituteProposalBean();
        instituteProposalBean.setProposalNumber(proposalNumber);
        instituteProposalBean.setMode('D');
        mdiFormReference.setCursor(new Cursor(java.awt.Cursor.WAIT_CURSOR));
        InstituteProposalBaseWindowController instituteProposalBaseWindowController =
        new InstituteProposalBaseWindowController("Display Institute Proposal ", 'D', (InstituteProposalBaseBean)instituteProposalBean);
        String proposalUnitNumber[] = instituteProposalBaseWindowController.displayProposal();
        mdiFormReference.setCursor(new Cursor(java.awt.Cursor.DEFAULT_CURSOR ));
        
    }
    //Coeus Enhancement case #1787end
    
    /**
     * Code added for Case#3388 - Implementing authorization check at department level
     * To check the user is having rights to view this award
     * @param awardNumber
     * @throws CoeusClientException
     * @return boolean
     */    
    private boolean canViewAward(String awardNumber){
        boolean canView = false;
        RequesterBean request = new RequesterBean();
        request.setFunctionType(CAN_VIEW_AWARD);
        request.setDataObject(awardNumber);
        String connect = CoeusGuiConstants.CONNECTION_URL +"/ProposalActionServlet";
        AppletServletCommunicator comm = new AppletServletCommunicator(connect, request);
        comm.send();
        ResponderBean response = comm.getResponse();
        if(response.isSuccessfulResponse()){
            canView = ((Boolean)response.getDataObject()).booleanValue();
        }
        return canView;
    }
    
    /**
     * Code added for Case#3388 - Implementing authorization check at department level
     * To check the user is having rights to view this institute proposal
     * @param institute proposal number
     * @throws CoeusClientException
     * @return boolean
     */    
    private boolean canViewProposal(String instProposalNumber){
        boolean canView = false;
        RequesterBean request = new RequesterBean();
        request.setFunctionType(CAN_VIEW_INST_PROPOSAL);
        request.setDataObject(instProposalNumber);
        String connect = CoeusGuiConstants.CONNECTION_URL + "/InstituteProposalMaintenanceServlet";        
        AppletServletCommunicator comm = new AppletServletCommunicator(connect, request);
        comm.send();
        ResponderBean response = comm.getResponse();
        if(response.isSuccessfulResponse()){
            canView = ((Boolean)response.getDataObject()).booleanValue();
        }
        return canView;
    }    
    
    
    //COEUSQA:2653 - Add Protocols to Medusa - Start
    /**
     * This method used to display the Medusa screen
     * for Development Proposal
     *
     */
    private void  showMedusaWindowForDevProposal(String devProposal){
        try{
            ProposalAwardHierarchyLinkBean linkBean;
            MedusaDetailForm medusaDetailform;
            linkBean = new ProposalAwardHierarchyLinkBean();
            linkBean.setDevelopmentProposalNumber(devProposal);
            linkBean.setBaseType(CoeusConstants.DEV_PROP);
            if( ( medusaDetailform = (MedusaDetailForm)mdiFormReference.getFrame(
                    CoeusGuiConstants.MEDUSA_BASE_FRAME_TITLE))!= null ){
                if( medusaDetailform.isIcon() ){
                    medusaDetailform.setIcon(false);
                }
                medusaDetailform.setSelectedNodeId(devProposal);
                medusaDetailform.setSelected( true );
                return;
            }
            medusaDetailform = new MedusaDetailForm(mdiFormReference,linkBean);
            medusaDetailform.setVisible(true);
            
            
        }catch(Exception exception){
            exception.printStackTrace();
        }
    }
    
    /**
     * This method used to display the Medusa screen
     * for Institute Proposal
     *
     */
    private void  showMedusaWindowForInstProposal(String instProposal){
        try{
            ProposalAwardHierarchyLinkBean linkBean;
            MedusaDetailForm medusaDetailform;
            linkBean = new ProposalAwardHierarchyLinkBean();
            linkBean.setInstituteProposalNumber(instProposal);
            linkBean.setBaseType(CoeusConstants.INST_PROP);
            if( ( medusaDetailform = (MedusaDetailForm)mdiFormReference.getFrame(
                    CoeusGuiConstants.MEDUSA_BASE_FRAME_TITLE))!= null ){
                if( medusaDetailform.isIcon() ){
                    medusaDetailform.setIcon(false);
                }
                medusaDetailform.setSelectedNodeId(instProposal);
                medusaDetailform.setSelected( true );
                return;
            }
            medusaDetailform = new MedusaDetailForm(mdiFormReference,linkBean);
            medusaDetailform.setVisible(true);
            
            
        }catch(Exception exception){
            exception.printStackTrace();
        }
    }
    
    /**
     * This method used to display the Medusa screen
     * for Award
     *
     */
    private void  showMedusaWindowForAwardNumber(String awardNumber){
        try{
            ProposalAwardHierarchyLinkBean linkBean;
            MedusaDetailForm medusaDetailform;
            linkBean = new ProposalAwardHierarchyLinkBean();
            linkBean.setAwardNumber(awardNumber);
            linkBean.setBaseType(CoeusConstants.AWARD);
            if( ( medusaDetailform = (MedusaDetailForm)mdiFormReference.getFrame(
                    CoeusGuiConstants.MEDUSA_BASE_FRAME_TITLE))!= null ){
                if( medusaDetailform.isIcon() ){
                    medusaDetailform.setIcon(false);
                }
                medusaDetailform.setSelectedNodeId(awardNumber);
                medusaDetailform.setSelected( true );
                return;
            }
            medusaDetailform = new MedusaDetailForm(mdiFormReference,linkBean);
            medusaDetailform.setVisible(true);
            
            
        }catch(Exception exception){
            exception.printStackTrace();
        }
    }
    //COEUSQA:2653 - End

    private boolean hasCreateProposalRights(){

        boolean hasRights = false;
        String connectTo = CoeusGuiConstants.CONNECTION_URL + "/coeusFunctionsServlet";
        RequesterBean request = new RequesterBean();
        Vector vecFnParams = new Vector();        
        Vector protoInput = new Vector();
        String leadUnit ="";
        String protocolNumber = "";
        int sequenceNumber = 0;

        if(protocolInfo != null ) {
             protocolNumber = protocolInfo.getProtocolNumber();
             sequenceNumber = protocolInfo.getSequenceNumber();
        }

        protoInput.addElement(protocolNumber);
        protoInput.addElement(sequenceNumber);

        request.setDataObjects(protoInput);
        request.setDataObject("GET_PROTOCOL_LEAD_UNIT");

        AppletServletCommunicator comm
                = new AppletServletCommunicator(connectTo, request);
        comm.send();
        ResponderBean response = comm.getResponse();
    
        if (response!=null){
            if (response.isSuccessfulResponse()){
                    leadUnit = ((String)response.getDataObject()).toString();
            }
        }       
         vecFnParams.addElement(CREATE_RIGHT);
         vecFnParams.addElement(leadUnit);
        request.setDataObjects(vecFnParams);
        request.setDataObject("FN_USER_HAS_DEPARTMENTAL_RIGHT");
         comm
                = new AppletServletCommunicator(connectTo, request);
        comm.send();
        response = comm.getResponse();
        /** Case Id 1856 and 1860.
         *Check the right not the role. Check CREATE_PROPOSAL right insted of checking
         *against role_id = 7
         */
        if (response!=null){
            if (response.isSuccessfulResponse()){
                    hasRights = ((Boolean)response.getDataObject()).booleanValue();

            }
        }
        return hasRights;
    }
    
}
