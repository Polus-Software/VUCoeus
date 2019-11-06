/*
 * @(#)ProtocolAORForm.java
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 */

/* PMD check performed, and commented unused imports and variables on 18-OCT-2010
 * by Johncy M John
 */

package edu.mit.coeus.iacuc.gui;

import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.gui.*;
import edu.mit.coeus.iacuc.bean.*;
import edu.mit.coeus.utils.AppletServletCommunicator;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.utils.ModuleConstants;
import edu.mit.coeus.utils.ScreenFocusTraversalPolicy;
import edu.mit.coeus.utils.TypeConstants;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.table.*;
import java.util.*;
import edu.mit.coeus.utils.CoeusOptionPane;
//import edu.mit.coeus.utils.*;
import edu.mit.coeus.exception.*;

/**
 * This class is used to display all the areas of research for a Protocol.
 * Areas of research can be added or removed for a Protocol
 * using this form. It calls AreaOfResearchHierarchyForm to display the
 * hierarchy tree for areas of research. If any parent area of research already
 * exists children of that node are not allowed to add explicitly. This 
 * component is being called or instantiated in Protocol Detail Form.
 * @@author  kprasad
 * @author Mukund C
 * @updated Subramanya Oct' 24 2002    
 */

public class ProtocolAORForm extends JComponent
                                    implements ActionListener {

    //holds reference to  CoeusAppletMDIForm
    private CoeusAppletMDIForm mdiForm = null;

    /* consists of the areas of expertise that are to be shown
       in the expertise table. */
    private Vector expertiseData=null;

    /*holds the Hash collection with research code and 
     *ProtocolReasearchAreasBean
     as value .*/
    private Hashtable protocolAORData = null;
        
    /* variable used to store the column names used to show in the table */
    private Vector colNames=null;

     /* flag used to indicate saving of modifications */
    private boolean saveRequired = false;

    //holds the function type
    private char functionType;
    
    //holds the updaed falg
    private boolean isUpdated = false;
    
    // Variables declaration - do not modify
    //holds the scrollPane for the table component.
    private JScrollPane scrPnMain;
    
    //holds the table component instance
    private JTable tblAOREntry;
    
    //holds the button instance delete funtion.
    private JButton btnDelete;
    
    //holds the button instance for add function.
    private JButton btnAdd;
       
    //holds CoeusMessageResources instance used for reading message Properties.
    private CoeusMessageResources coeusMessageResources;
    // End of variables declaration
    
    private static final char RESEARCH_AREA_EXISTS = 'E';
    private final static String connURL = CoeusGuiConstants.CONNECTION_URL + "/AreaOfResearchServlet";  
    private String deletedResearchCode;

    /** 
     * Creates new form ProtocolAORForm default Constructor.     
     */
    public ProtocolAORForm() {        
    }
    
    /** 
     * Creates new form ProtocolAORForm with parent MDI Form Window.
     * @param mdiParentForm parent window.
     */
    public ProtocolAORForm( CoeusAppletMDIForm mdiParentForm ) {
        mdiForm = mdiParentForm;        
    }

    /** 
     * Creates new form ProtocolAORForm with parent MDI Form Window &
     * table Data vector.
     * @param mdiParentForm parent window.
     * @param tblData collection of table Data
     */
    public ProtocolAORForm( CoeusAppletMDIForm mdiParentForm, Vector tblData ) {
        mdiForm = mdiParentForm;
    }


    /** 
     * Creates new form ProtocolAORForm with parent MDI Form Window &
     * table Data vector with mode of the form( like edit/modify/add).
     *
     * @param fnType form mode type.
     * @param aorData collection of table Data
     */
    public ProtocolAORForm( char fnType, Vector aorData ) {             
        protocolAORData = constructHashFromDataVector( aorData );        
        this.functionType = fnType;        
    }
    
    
    /**
     * This method is used to initialize the form components and set the form
     * data and set the enabled status for all components depending on the 
     * functionType specified while opening the form.
     *
     * @param mdiForm CoeusAppletMDIForm reference to the parent Component
     * @return JComponent reference to the ProtocolMaintenanceForm component 
     * after initializing and setting the data
     */
    public JComponent showProtocolAORForm(CoeusAppletMDIForm mdiForm){
        this.mdiForm = mdiForm;
        coeusMessageResources = CoeusMessageResources.getInstance();
        initComponents();
        //setFormData();
        formatFields();        
        return this;
    }
    
    
    /** 
     * This method is used to set the enabled status for the components 
     * depending on the functionType specified. 
     */
    private void formatFields(){
        //code modified for coeus4.3 enhancements that UI to be in display mode
        //when new amendment or renewal is created
//        boolean enabled = functionType != 'D' ? true : false ;
        boolean enabled = (functionType == CoeusGuiConstants.DISPLAY_MODE || 
                functionType == CoeusGuiConstants.AMEND_MODE) ? false : true;
        btnDelete.setEnabled( enabled );
        btnAdd.setEnabled( enabled );
        if( tblAOREntry.getRowCount() < 1 ){
            btnDelete.setEnabled( false );            
        }
        
        //Added by Amit 11/18/2003
        //code modified for coeus4.3 enhancements that UI to be in display mode
        //when new amendment or renewal is created
//        if(functionType == CoeusGuiConstants.DISPLAY_MODE){
        if(functionType == CoeusGuiConstants.DISPLAY_MODE || 
                functionType == CoeusGuiConstants.AMEND_MODE){
            java.awt.Color bgListColor = (java.awt.Color)javax.swing.UIManager.getDefaults().get("Panel.background");
        
            tblAOREntry.setBackground(bgListColor);    
            tblAOREntry.setSelectionBackground(bgListColor );
            tblAOREntry.setSelectionForeground(Color.black);            
        }
        else{
            tblAOREntry.setBackground(Color.white);            
            tblAOREntry.setSelectionBackground(Color.white);
            tblAOREntry.setSelectionForeground(Color.black);            
        }
        //end Amit          
    }
            
    
    /** This method is called from within the constructor to
     * initialize the form.
     */
    private void initComponents() {
        GridBagConstraints gridBagConstraints;

        btnAdd = new JButton();
        btnAdd.addActionListener( this );
        btnAdd.setMnemonic( 'A' );
        btnDelete = new JButton();        
        btnDelete.addActionListener( this );
        btnDelete.setEnabled( false );
        btnDelete.setMnemonic( 'D' );
        
        scrPnMain = new JScrollPane();
        tblAOREntry = new JTable();
        tblAOREntry.setFont( CoeusFontFactory.getNormalFont() );
        
        setLayout(new GridBagLayout());
        //*******************************************
    //    setPreferredSize( new Dimension( 560, 180 ));
        // Added by chandra 29/8/2003 - Align the form at the top left corner of the screen.
          setPreferredSize( new Dimension( 650, 418 ));
    //    setBorder(new EtchedBorder());// Added by chandra - To remove external border.
        btnAdd.setFont(CoeusFontFactory.getLabelFont());
        btnAdd.setText("Add");
        btnAdd.setName("btnAdd");
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new Insets(25, 0, 0, 0);
        add(btnAdd, gridBagConstraints);

        btnDelete.setFont(CoeusFontFactory.getLabelFont());
        btnDelete.setText("Delete");
        btnDelete.setName("btnDelete");
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
      //  gridBagConstraints.gridy = 1;
        // Added by chandra - To replace the Delete Button at a proper place
          gridBagConstraints.gridy = 0;
          gridBagConstraints.insets = new Insets(85, 0, 5, 0);
          // End Chandra
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        add(btnDelete, gridBagConstraints);
        //tblAOREntry.setSelectionBackground( Color.white );
        //tblAOREntry.setSelectionForeground( Color.black );
        
        tblAOREntry.setModel( new DefaultTableModel( getExpertiseData(),
                getColumnNames()){
            public boolean isCellEditable(int row, int col){
                return false;
            }
        });
        
        TableColumn clmAor = tblAOREntry.getColumn( "Code" );
        clmAor.setMaxWidth( 250 );        
        setHandIconDisplyColumn();

        scrPnMain.setBorder(new TitledBorder(null,
            "Area of Research", TitledBorder.LEFT,
            TitledBorder.TOP,
                CoeusFontFactory.getLabelFont()));

        //scrPnMain.setPreferredSize(new Dimension(650, 350));
        // Added by chandra
        scrPnMain.setMinimumSize(new Dimension(200, 318));
        scrPnMain.setPreferredSize(new Dimension(600, 318));
        // End Chandra
        ListSelectionModel perSelectionModel = tblAOREntry.getSelectionModel();
        perSelectionModel.setSelectionMode(
                                          ListSelectionModel.SINGLE_SELECTION );
        perSelectionModel.setSelectionInterval( 1,
                                                tblAOREntry.getColumnCount()-1);
        tblAOREntry.setSelectionModel( perSelectionModel );
        tblAOREntry.clearSelection();
        tblAOREntry.setFont(CoeusFontFactory.getNormalFont());
        tblAOREntry.getTableHeader().setFont(CoeusFontFactory.getLabelFont());
        
        //code modified for coeus4.3 enhancements that UI to be in display mode
        //when new amendment or renewal is created
//        if( tblAOREntry.getRowCount() > 0){
        if( tblAOREntry.getRowCount() > 0 && functionType != CoeusGuiConstants.DISPLAY_MODE 
                && functionType != CoeusGuiConstants.AMEND_MODE){
            btnDelete.setEnabled( true );
            tblAOREntry.setRowSelectionInterval( 0, 0 );
        }
        
        scrPnMain.setViewportView(tblAOREntry);

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = 350;
        gridBagConstraints.ipady = 75;
        gridBagConstraints.insets = new Insets(0, 0, 0, 10);
        add(scrPnMain, gridBagConstraints);
        java.awt.Component[] components = {tblAOREntry,btnAdd,btnDelete};
        ScreenFocusTraversalPolicy  traversePolicy = new ScreenFocusTraversalPolicy( components );
        setFocusTraversalPolicy(traversePolicy);
        setFocusCycleRoot(true);  

    }

    
    //supporting method to set the Hand icon display method
    private void setHandIconDisplyColumn(){
        
        TableColumn column = tblAOREntry.getColumnModel().getColumn(0);
        column.setMinWidth(30);
        column.setMaxWidth(30);
        column.setHeaderRenderer(new EmptyHeaderRenderer());
        
        column.setResizable(false);
        column.setCellRenderer(new IconRenderer());
        column.setPreferredWidth(30);
        
        column = tblAOREntry.getColumnModel().getColumn(1);
        column.setMinWidth(60);
        //column.setMaxWidth(60);
        column.setPreferredWidth(60);
        
        column = tblAOREntry.getColumnModel().getColumn(2);
        column.setMinWidth(440);
        //column.setMaxWidth(60);
        column.setPreferredWidth(440);
        
        tblAOREntry.setRowHeight( 21 );
        JTableHeader header = tblAOREntry.getTableHeader();
        header.setReorderingAllowed(false);
        //header.setResizingAllowed(false);
        tblAOREntry.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tblAOREntry.setOpaque(false);
        tblAOREntry.setShowVerticalLines(false);
        tblAOREntry.setShowHorizontalLines(false);
    }
    
    
    /**
     * This Method is used to Add/Delete Actions
     * @param btnAction denote the ActionEvent for the buttons.
     */
    public void actionPerformed( ActionEvent btnAction ){

        Object sourceAction = btnAction.getSource();
        if( sourceAction.equals( btnAdd ) ){
            try{
                getSelectedAreaOfExpertise();
                if( tblAOREntry.getRowCount() > 0 ){
                    btnDelete.setEnabled( true );        
                }            
             }catch(Exception e){
                 e.printStackTrace();
             }
        }else if( sourceAction.equals( btnDelete ) ){
            int selectedRow = tblAOREntry.getSelectedRow();     
            if( selectedRow == -1 ){
            CoeusOptionPane.showWarningDialog(
                            coeusMessageResources.parseMessageKey(
                                        "areaRsrchBaseWin_exceptionCode.1003"));
            }else{
                int selectedOption = CoeusOptionPane.showQuestionDialog(
                            coeusMessageResources.parseMessageKey(
                                        "protoAORFrm_delConfirmCode.1050"),
                                    CoeusOptionPane.OPTION_YES_NO, 
                                    CoeusOptionPane.DEFAULT_YES);
                if( selectedOption == JOptionPane.YES_OPTION ){
                String resID = tblAOREntry.getValueAt( selectedRow , 1 ).
                                                                    toString();                
                ProtocolReasearchAreasBean delBean = 
                       (ProtocolReasearchAreasBean)protocolAORData.get( resID.trim() );
                if( delBean != null && delBean.getAcType() == null  ){                    
                    delBean.setAcType( "D" );
                    saveRequired = true;
                }
                else{
                    //this condition is used to removed the inserted node
                    //programatically added and db wont have this entry.
                    protocolAORData.remove( resID );
                }
                
                System.out.println("IN del action performed "+protocolAORData.size());
                
                ((DefaultTableModel)
                            tblAOREntry.getModel()).removeRow( selectedRow );
                if( tblAOREntry.getRowCount() <= 0 ){
                      btnDelete.setEnabled( false );            
                }else{
                    int lastRowIndex = tblAOREntry.getRowCount() - 1 ;                    
                    tblAOREntry.setRowSelectionInterval(  lastRowIndex, 
                                                                lastRowIndex );
                    tblAOREntry.scrollRectToVisible( tblAOREntry.getCellRect(
                                                lastRowIndex ,0, true));
                    
                }
                }
            }
        }
    }

    
    /**
     * This method is used to show the AreaOfResearchHierarchyForm for selecting
     * the areas of research and for storing them in expertiseData vector which
     * will be sent to the server for storing in to the database
     *
     * @throws Exception if it is unable to show AreaOfResearchHierarchyForm
     * it will throw an exception
     */
    private void getSelectedAreaOfExpertise() throws java.lang.Exception{
        
        Hashtable selectedExpertises;
        CoeusDlgWindow dlgProtcol = new CoeusDlgWindow( mdiForm,"Select AOR",true);
        AreaOfResearchHierarchyForm aor = new AreaOfResearchHierarchyForm(
                                                                    dlgProtcol);
        dlgProtcol.pack();
        aor.requestDefaultFocusForComponent();
        dlgProtcol.show();

        selectedExpertises = aor.getSelectedAORNodes();
        if (selectedExpertises != null) {
        java.util.Iterator nodes = selectedExpertises.keySet().iterator();
        String expertiseDesc = null;
        String expertiseNode = null;

        if (expertiseData == null){
            expertiseData = new Vector();
        }
        Vector row = null;
        boolean found = false;
        while( nodes.hasNext() ){
            /* if any nodes have been selected add them to the table after
               checking for duplicates */
            row = new Vector();
            expertiseNode = nodes.next().toString();
            expertiseDesc = selectedExpertises.get(expertiseNode).toString();
            found = false;
            if (tblAOREntry.getRowCount() > 0) {
                for (int index =0 ; index < tblAOREntry.getRowCount();
                        index++) {
                    String strNode = (String)tblAOREntry.getValueAt(index,1);
                    if ( strNode.equalsIgnoreCase(expertiseNode) ){
                        found = true;
                        break;
                    }
                }
            }
            //comeHere
            System.out.println("found value is "+found);
            if (!found){
                saveRequired = true;
                row.addElement( "" );//hand icon column 
                row.addElement( expertiseNode);
                row.addElement( expertiseDesc);
                expertiseData.addElement( row );
                //adding data to Master hashTable
                try{
                ProtocolReasearchAreasBean newDataBean = 
                                              new ProtocolReasearchAreasBean();
                newDataBean.setResearchAreaCode( expertiseNode );
                newDataBean.setResearchAreaDescription( expertiseDesc );
                newDataBean.setAcType( "I" );

                isUpdated = true;
                if( protocolAORData.containsKey(expertiseNode) ) {
                    ProtocolReasearchAreasBean oldBean = 
                            (ProtocolReasearchAreasBean)protocolAORData.get(expertiseNode);
                    if( oldBean.getAcType()!= null && 
                            oldBean.getAcType().equalsIgnoreCase("D")){
                        //resetting acType to null as user deleted a row and added  the same record        
                        newDataBean = oldBean;         
                        newDataBean.setAcType(null);
                    }else{
                        newDataBean = oldBean;
                    }
                }
                protocolAORData.put( expertiseNode, newDataBean );
                }catch( Exception err ){
                    err.printStackTrace();
                }
            }
        }
        ((DefaultTableModel)tblAOREntry.getModel()).setDataVector(
                getExpertiseData(),getColumnNames());     
        setHandIconDisplyColumn();
        }
        
           int rowAdded = expertiseData.size() - 1 ;
           if( expertiseData !=  null && rowAdded >= 0 ){             
            tblAOREntry.setRowSelectionInterval( rowAdded, rowAdded );
            tblAOREntry.scrollRectToVisible( tblAOREntry.getCellRect(
                                                rowAdded ,0, true));
           }
           
    }
    
    //supporting method to construct the hashtable data from the vector of bean
    private Hashtable constructHashFromDataVector( Vector data ){     
        Hashtable newData = new Hashtable();
        expertiseData = new Vector();        
        if( data == null ){
            return newData;
        }        
        ProtocolReasearchAreasBean rowBean = null;
        Vector tableRowData = null;        
        for( int indx = 0 ; indx < data.size() ; indx ++ ){
            try{                
                rowBean = (ProtocolReasearchAreasBean) data.get( indx );
                tableRowData = new Vector();    
                tableRowData.addElement( "" );//empty hand - icon column
                tableRowData.addElement( rowBean.getResearchAreaCode() );
                tableRowData.addElement( rowBean.getResearchAreaDescription());
                expertiseData.addElement( tableRowData );
                newData.put( rowBean.getResearchAreaCode(), rowBean );
                tableRowData = null;
            }catch( Exception err ){
                err.printStackTrace();
            }
        }

        return newData;
        
    }
    
    

    /**
     * Method is used to get All the Table Data Bean for Save Operation.
     * @return Vector holds the set of all ProtocolReasearchAreasBean.
     */
    public Vector getFormData(){
        Iterator itr = protocolAORData.keySet().iterator(); 
        Vector aorCurrentData = new Vector();
        ProtocolReasearchAreasBean rowBean = null;
        Object key = null;
        
        while( itr.hasNext() ){
            key = itr.next();
            rowBean = (ProtocolReasearchAreasBean) protocolAORData.get( key );  
            // added to insert in amend mode.
            if( functionType == CoeusGuiConstants.AMEND_MODE 
                    || functionType == CoeusGuiConstants.ADD_MODE){
                if( rowBean.getAcType() == null || !rowBean.getAcType().equalsIgnoreCase("D") ) {
                    rowBean.setAcType( "I" ); 
                    aorCurrentData.addElement( rowBean );            
                }

            }else{
                aorCurrentData.addElement( rowBean );               
            }
        }        
        return aorCurrentData;
    }
    
    //COEUSQA-2802 - Allow user to remove values for areas of research in IRB and IACUC 
     /**
     * This method checks whether apecific research_area_code exists in table
     * @return boolean (returns true, if data exists, else false)
     */
     private boolean isResearchAreaCodeExists(Vector vecAreaOfResearch) {
     
        boolean isExists = true;
        if(vecAreaOfResearch != null && vecAreaOfResearch.size() > 0) {
            int count = -1;
            try {
                RequesterBean requesterBean = new RequesterBean();
                ResponderBean responderBean = new ResponderBean();
                requesterBean.setFunctionType(RESEARCH_AREA_EXISTS);
                Vector dataObjects = new Vector();
                dataObjects.addElement(ModuleConstants.IACUC_MODULE_CODE);
                dataObjects.addElement(vecAreaOfResearch);
                requesterBean.setDataObjects(dataObjects);
                AppletServletCommunicator comm = new AppletServletCommunicator(connURL, requesterBean);
                comm.send();
                if(responderBean!= null){
                responderBean = comm.getResponse();
                if (responderBean.isSuccessfulResponse()) {
                    count = Integer.parseInt(responderBean.getId());  
                    if(responderBean.getMessage() != null) {
                        setDeletedResearch(responderBean.getMessage());
                    }
                }
                isExists = (count > 0) ? true : false;
                }
               }catch(Exception ex) {
                    ex.printStackTrace();
               }
        }
       return isExists ;
     }
     
     private String getDeletedResearchCode() {
         return deletedResearchCode;
     }
     
     private void setDeletedResearch(String deletedResearchCode) {
         this.deletedResearchCode = deletedResearchCode;
     }
     
    
    /**
     * Method used to Set the Function Type of this From.
     * @param funType sets form mode type.
     */
     public void setFunctionType(char funType){
        this.functionType = funType;
        formatFields();
    }
     
     
    /**
     * Method is used to get All the Table Data Bean for Save as Hashtable
     * @return Hashtable holds the set of all ProtocolReasearchAreasBean.
     */
    public Hashtable getAllAORHashtableEntry(){
        
        return protocolAORData;         
    }
    
    /**
     * Gets the default data Vector shown in table. 
     */
    private Vector getExpertiseData(){
        return this.expertiseData;
    }

    /**
     * Sets the column names that are required for table
     * @param columnNames Collection consisting of column names.
     */
    public void setColumnNames(Vector columnNames){
        this.colNames = columnNames;
    }

    /**
     * Gets the Column names of the table available in this panel in a Vector
     *
     * @return A Vector of column names
     */
    public Vector getColumnNames(){
        if( colNames != null ){
            return colNames;
        }else{
            colNames = new Vector();
            colNames.addElement( "" ); //hand icon column
            colNames.addElement( "Code" );
            colNames.addElement( "Description" );
        }
        return this.colNames;
    }

    
    /**
     * validate the form data before sending to the database
     *
     * @return boolean True - all form controls are validation are successful,
     *                False - validation fails
     * @throws Exception while invoking error Message Dispaly.
     */
    public boolean validateData() throws Exception, CoeusUIException{ 
        System.out.println("validateData Fired");
        System.out.println("protocolAORData size is "+protocolAORData.size());
        int rowCount = tblAOREntry.getRowCount();
        if( protocolAORData  == null || protocolAORData.size() < 1 || rowCount <= 0){
            log(coeusMessageResources.parseMessageKey(
                                    "protoAORFrm_exceptionCode.1049"));            
            return false;
        } 
         //check whether research_area_code exits
        else if(!isResearchAreaCodeExists(getNonDeletedNodes())) {
            log(getDeletedResearchCode() + " " + coeusMessageResources.parseMessageKey(
                                    "areaRsrchBaseWin_exceptionCode.1012"));
            return false;
        }
        else{
            return true; 
        }
    }
  
     //COEUSQA-2802 - Allow user to remove values for areas of research in IRB and IACUC 
     /**
     * This method is for taking all non-deleted nodes to a vector
     * @return vector
     */
    private Vector getNonDeletedNodes() {
        Iterator iterator = protocolAORData.keySet().iterator(); 
        Object key = null;
        ProtocolReasearchAreasBean researchAreaBean = null; 
        Vector vecNonDeletedNodes = new Vector();
        Vector vecRow = null;
        while( iterator.hasNext() ){
            key = iterator.next();
            researchAreaBean = (ProtocolReasearchAreasBean) protocolAORData.get( key );  
            if(researchAreaBean.getAcType() != TypeConstants.DELETE_RECORD) {
                vecRow = new Vector();
                vecRow.addElement(researchAreaBean.getResearchAreaCode());
                vecRow.addElement(researchAreaBean.getResearchAreaDescription());
                vecNonDeletedNodes.addElement(vecRow);
            }
        }
        return vecNonDeletedNodes;
    }
    
    /**
     * This Method is used to set the form data.
     * @param aorTableData refreshed collection of table data
     */
    public void setFormData( Vector aorTableData ){
     protocolAORData = null;   
     protocolAORData = constructHashFromDataVector( aorTableData );  
     tblAOREntry.setModel( new DefaultTableModel( getExpertiseData(),
                getColumnNames()){
            public boolean isCellEditable(int row, int col){
                return false;
            }
        });
     setHandIconDisplyColumn();
     saveRequired = false;
     if( tblAOREntry.getRowCount() > 0 ){
//            btnDelete.setEnabled( true );
            tblAOREntry.setRowSelectionInterval( 0, 0 );
     }
    }
     
    /**
     * This method is used to get the status flag of save operation.
     * @return boolean True is Save required else False.
     */
    public boolean isSaveRequired(){
        return saveRequired;
    }
    
    //Added by Amit 11/21/2003
    /** This method use to implement focus on first editable component in this page.
     */
    public void setDefaultFocusForComponent(){
        //code modified for coeus4.3 enhancements that UI to be in display mode
        //when new amendment or renewal is created
//        if(!( functionType == CoeusGuiConstants.DISPLAY_MODE )) {        
        if(!( functionType == CoeusGuiConstants.DISPLAY_MODE ) &&
                !( functionType == CoeusGuiConstants.AMEND_MODE )) {            
        
            if(tblAOREntry.getRowCount() > 0 ) {
                tblAOREntry.requestFocusInWindow();
                tblAOREntry.setRowSelectionInterval(0, 0);
                tblAOREntry.setColumnSelectionInterval(1,1);
            }else{
                btnAdd.requestFocusInWindow();
            }            
        }
    }    
    //end Amit    
    
    
    /**
     * This method is used to set the save flag of save operation.
     * @param isSaveRequired True is Save required else False.
     */
    public void setSaveRequired( boolean isSaveRequired ){
        saveRequired = isSaveRequired ;
    }
    
    /**
     * This method is used to get the falg for the Area Of Research 
     * Modified Status
     * @return boolean True if Tree Nodes Modified else False.
     */
    public boolean isAORModified(){                
        return isUpdated; 
    } 
     
    /**
     * Throws the exception with the given message
     * @param mesg String represent the message to be logged.
     * @throws Exception throws new message.
     */
    public void log(String mesg) throws CoeusUIException {
        //Commented by sharath(20 - Aug - 2003)
        //throw new Exception(mesg);
        
        //Bug Fix ( Defect Id : 379)
        CoeusUIException coeusUIException = new CoeusUIException(mesg,CoeusUIException.WARNING_MESSAGE);
        coeusUIException.setTabIndex(CoeusGuiConstants.IACUC_AOR_EXCEP_TAB_INDEX);
        throw coeusUIException;
        //Bug Fix ( Defect Id : 379)
    }
    
    /**
     * This Class is Used to Create the Empty Header for the Table. This class
     * is instantiated to set the specified colum heaader empty. Usually this
     * kind of tabel render setting is done for Hand Icon selection provided
     * with its column heading to be empty.
     */    
    class EmptyHeaderRenderer extends JList implements TableCellRenderer {
        
        /**
         * Default constructor to set the default foreground/background and 
         * border properties of this renderer for a cell.
         */
        public EmptyHeaderRenderer() {
            
            setOpaque(true);
            setForeground(UIManager.getColor("TableHeader.foreground"));
            setBackground(UIManager.getColor("TableHeader.background"));
            setBorder(new EmptyBorder(0, 0, 0, 0));
            ListCellRenderer renderer = getCellRenderer();
            ((JLabel) renderer).setHorizontalAlignment(JLabel.CENTER);
            setCellRenderer(renderer);
            
        }
        
        /**
         * The Overridden method of TableCellRenderer which is called for 
         * every cell when a component
         * is going to be rendered in its cell.
         * Returns the component used for drawing the cell.
         * This method is used to configure the renderer appropriately before 
         * drawing
         *
         * @param table  the JTable that is asking the renderer to draw; can 
         * be null
         * @param value  the value of the cell to be rendered. It is up to 
         * the specific renderer to interpret and draw the value. 
         * For example, if value is the string "true", it could be rendered 
         * as a string or it could be rendered as a check box that is checked. 
         * null is a valid value
         * @param isSelected  true if the cell is to be rendered with the 
         * selection highlighted; otherwise false
         * @param hasFocus if true, render cell appropriately. For example, put 
         * a special border on the cell, if the cell can be edited, render in 
         * the color used to indicate editing
         * @param row the row index of the cell being drawn. When drawing the 
         * header, the value of row is -1
         * @param column  the column index of the cell being drawn
         * @return Component Respective Table Cell Editing component.
         */
        public Component getTableCellRendererComponent(JTable table,
                                                        Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
            
            return this;
            
        }

    }

    /**
     * This class is used to set the Table column with Icon ( Hand Icon ) 
     * renderer. This class is instantiated to set the particular table column
     * model with this TableCellRenderer to provide the Hand Icon display 
     * druing the Table Row Selection.
     */
    
    class IconRenderer  extends DefaultTableCellRenderer {
        
        //holds the Image Hand Icon  used in table column rendering.
        
        private  final  ImageIcon handIcon =
            new ImageIcon(getClass().getClassLoader().getResource(
                CoeusGuiConstants.HAND_ICON));
        
        
//        private  final  ImageIcon handIcon =
//            new ImageIcon(getClass().getClassLoader().getResource(
//                CoeusGuiConstants.HAND_ICON));
//        
        //holds the Image icon used as dummy setting 
        private  final  ImageIcon emptyIcon = null;
        
        /** Default Constructor*/
        public IconRenderer() {
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
         * @return Component Respective Table Cell Editing component.
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
    
}
