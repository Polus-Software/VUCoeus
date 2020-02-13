/*
 * @(#)ProtocolVulnerableSubjectsForm.java
 *
 * Created on March 25, 2003, 4:10 PM
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved. 
 */

package edu.mit.coeus.iacuc.gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import java.awt.*;
import javax.swing.border.*;
import java.awt.event.*;
import java.util.*;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;

import edu.mit.coeus.gui.*;
import edu.mit.coeus.utils.*;
import edu.mit.coeus.iacuc.bean.*;
import edu.mit.coeus.exception.*;

/**
 * This class is used to add Vulnerable subjects and count for each subject for a 
 * given protocol. User has to specify count for each vulnerable subject specified.
 * @author  ravikanth
 */
public class ProtocolVulnerableSubjectsForm extends javax.swing.JComponent {
    
    private JPanel pnlSubjects;
    private JScrollPane scrPnSubjects;
    private JTable tblSubjects;
    private JButton btnAddSubject, btnDeleteSubject;
    
    /* holds the mode in which the form is opened*/
    private char functionType = 'D';
    
    private Vector subjects;
    private Vector availableSubjects;
    private Vector selectedSubjects;
    private VulnerableSubjectsForm vulnerableSubjectsForm;
    
     //holds CoeusMessageResources instance used for reading message Properties.
    private CoeusMessageResources coeusMessageResources;
    
    private CoeusAppletMDIForm mdiForm;
    private ProtocolVulnerableSubListsBean subListBean;
    
    
    //Protocol Enhancement - Saving null in db Start 1
    //private final String SUBJECT_DEFAULT_COUNT = "1";
    private final Integer SUBJECT_DEFAULT_COUNT = null;
    //Protocol Enhancement - Saving null in db End 1
    
    private boolean saveRequired;

    /** Creates a new instance of ProtocolVulnerableSubjectsForm */
    public ProtocolVulnerableSubjectsForm() {
    }
    
    
    /**
     * Creates a new instance of ProtocolVulnerableSubjectsForm with the given 
     * functionType and collection of VulnerableSubject beans.
     * @param fType character which specifies the mode in which the form is opened.
     * @param subjectsList  Collection of ProtocolVulnerableSubListsBean.
     */    
    public ProtocolVulnerableSubjectsForm(char fType, Vector subjectsList){
        this.functionType = fType;
        this.subjects = subjectsList;
    }

    /** This method is used to initialize the form components, set the form
     * data and set the enabled status for all components depending on the
     * <CODE>functionType</CODE> specified while opening the form.
     *
     * @param parentForm reference to the <CODE>CoeusAppletMDIForm</CODE>.
     * @return JComponent reference to the 
     * <CODE>ProtocolVulnerableSubjectsForm</CODE> component after initializing 
     * and setting the data.
     */
    public JComponent showVulnerableSubjectsForm(CoeusAppletMDIForm parentForm){
        this.mdiForm = parentForm;
        coeusMessageResources = CoeusMessageResources.getInstance();
        initComponents();
        setFormData();
        formatFields();
        return pnlSubjects;
    }
    private void initComponents(){
        GridBagConstraints gridBagConstraints;
        pnlSubjects = new JPanel();
        btnAddSubject = new JButton();
        btnDeleteSubject = new JButton();
        scrPnSubjects = new JScrollPane();
        tblSubjects = new JTable();
        tblSubjects.setFont(CoeusFontFactory.getNormalFont());
        
        
        pnlSubjects.setLayout(new GridBagLayout());
        //pnlSubjects.setPreferredSize(new Dimension(500,190));
        //pnlSubjects.setMinimumSize(new Dimension(480,150));
        pnlSubjects.setMinimumSize(new Dimension(500,450));
        pnlSubjects.setMinimumSize(new Dimension(500,450));
        
//        pnlSubjects.setBorder(new TitledBorder(new EtchedBorder(),
//                "Vulnerable Subjects", TitledBorder.LEFT, TitledBorder.TOP,
//                CoeusFontFactory.getLabelFont()));
        
        btnAddSubject.setFont(CoeusFontFactory.getLabelFont());
        btnAddSubject.setMnemonic('A');
        btnAddSubject.setText("Add");
        btnAddSubject.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent ae){
                if( ( tblSubjects.getRowCount() > 0 ) && ( tblSubjects.isEditing() ) ){
                    if(tblSubjects.getCellEditor() != null){
                        tblSubjects.getCellEditor().stopCellEditing();
                    }
                }
                //Commented and Modified for Case#3259 - Naming terms in Subject Tab                
//                final CoeusDlgWindow dlgSubjects = new CoeusDlgWindow(mdiForm,
//                        "Available Vulnerable Subjects",true);
                final CoeusDlgWindow dlgSubjects = new CoeusDlgWindow(mdiForm,
                        "List of Subjects",true);
                vulnerableSubjectsForm =
                        new VulnerableSubjectsForm(dlgSubjects,
                                availableSubjects);
                dlgSubjects.getContentPane().add(vulnerableSubjectsForm);
                dlgSubjects.pack();
                
                Dimension screenSize = Toolkit.getDefaultToolkit().
                                                            getScreenSize();
                Dimension dlgSize = dlgSubjects.getSize();
                dlgSubjects.setLocation(screenSize.width/2 - (dlgSize.width/2),
                screenSize.height/2 - (dlgSize.height/2));
                dlgSubjects.setResizable(false);
                vulnerableSubjectsForm.requestDefaultFocusForComponent();
                dlgSubjects.setVisible(true);
                selectedSubjects = vulnerableSubjectsForm.getSelectedSubjects();
                
                boolean found = false;
                if(selectedSubjects.size()>0){
                    Enumeration enumSelectedSubjects =
                            selectedSubjects.elements();
                    while(enumSelectedSubjects.hasMoreElements()){
                        found = false;
                        Vector selectedSubject =
                                (Vector)enumSelectedSubjects.nextElement();
                        int rowCount = tblSubjects.getRowCount();
                        if( rowCount > 0 ){
                            Enumeration enumSubData = ((DefaultTableModel)
                                tblSubjects.getModel()).getDataVector().
                                                                    elements();
                            while(enumSubData.hasMoreElements()){
                                Vector oldSubject = (Vector)
                                            enumSubData.nextElement();

                                /* checking for duplicate subjects */
                                if(oldSubject.elementAt(1).equals(
                                        selectedSubject.elementAt(0).
                                                                toString()) ){
                                    found = true;
                                    break;
                                }
                            }
                        }
                        if(!found){
                            selectedSubject.addElement(SUBJECT_DEFAULT_COUNT);
                            selectedSubject.insertElementAt("",0);
                            ((DefaultTableModel)
                                    tblSubjects.getModel()).addRow(
                                                                selectedSubject);                            
                            ((DefaultTableModel)
                                    tblSubjects.getModel()).
                                                        fireTableDataChanged();            
                            int lastRow = tblSubjects.getRowCount()-1;
                            tblSubjects.scrollRectToVisible(
                                tblSubjects.getCellRect(lastRow ,0, true));
                            
                            tblSubjects.setRowSelectionInterval(lastRow,lastRow);
                            btnDeleteSubject.setEnabled( true );           
                            saveRequired = true;
                        }
                    }
                }
            }
        });
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        //gridBagConstraints.insets = new Insets(5, 5, 0, 10);
        gridBagConstraints.insets = new Insets(18, 5, 0, 10);
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        pnlSubjects.add(btnAddSubject, gridBagConstraints);

        btnDeleteSubject.setFont(CoeusFontFactory.getLabelFont());
        btnDeleteSubject.setMnemonic('D');
        btnDeleteSubject.setText("Delete");
        btnDeleteSubject.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                int totalRows = tblSubjects.getRowCount();
                int row = tblSubjects.getSelectedRow();
                int col = tblSubjects.getSelectedColumn();
                /* If there are more than one row in table then delete it */
                if (totalRows > 0) {
                    if( tblSubjects.isEditing() ){
                        if(tblSubjects.getCellEditor() != null){
                            tblSubjects.getCellEditor().stopCellEditing();
                        }
                    }
                    if (row != -1) {
                        int selectedOption = CoeusOptionPane.showQuestionDialog(
                                coeusMessageResources.parseMessageKey(
                                    "protoMntFrm_delConfirmCode.1080"),
                                    CoeusOptionPane.OPTION_YES_NO,
                                    CoeusOptionPane.DEFAULT_YES);
                        // if Yes then selectedOption is 0
                        // if No then selectedOption is 1
                        if (0 == selectedOption) {
                            ((DefaultTableModel)
                                tblSubjects.getModel()).removeRow(row);
                            //tblSubjects.clearSelection();
                            saveRequired = true;
                            // find out again row count in table
                            int newRowCount = tblSubjects.getRowCount();
                            // select the next row if exists
                            if (newRowCount > row) {
                                (tblSubjects.getSelectionModel())
                                    .setSelectionInterval(row, row);
                            } else {
                                (tblSubjects.getSelectionModel())    
                                    .setSelectionInterval(newRowCount - 1,
                                        newRowCount - 1);
                            }
                        }

                    }
                    if( tblSubjects.getRowCount() < 1 ){
                         btnDeleteSubject.setEnabled( false );           
                    }
                }
                
                
            }
        });


        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        //gridBagConstraints.insets = new Insets(5, 5, 0, 10);
        gridBagConstraints.insets = new Insets(8, 5, 0, 10);
        gridBagConstraints.anchor = GridBagConstraints.NORTHWEST;
        pnlSubjects.add(btnDeleteSubject, gridBagConstraints);

        tblSubjects.setModel(new DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Icon","Code","Description","Count"
            }
        ) {
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                //code added for coeus4.3 enhancements that UI to be in display mode
                //when new amendment or renewal is created 
//                if(functionType == 'D'){                
                if(functionType == CoeusGuiConstants.DISPLAY_MODE || 
                        functionType == CoeusGuiConstants.AMEND_MODE){
                    return false;
                }
                if(columnIndex == 3){
                    return true;
                }
                return false;
            }
        });
        tblSubjects.getTableHeader().setFont(CoeusFontFactory.getLabelFont() );
        tblSubjects.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        setTableEditors();
        
        // Added by chandra -02/09/2003
        // To change the look & feel
        //Protocol Enhancements - Reading header from the prop file Start
        String title = coeusMessageResources.parseLabelKey("subjects_labelCode.1000");
        
        /*scrPnSubjects.setBorder(new TitledBorder(new EtchedBorder(),
                "Vulnerable Subjects", TitledBorder.LEFT, TitledBorder.TOP,
                CoeusFontFactory.getLabelFont()));*/
        scrPnSubjects.setBorder(new TitledBorder(new EtchedBorder(),
                title, TitledBorder.LEFT, TitledBorder.TOP,
                CoeusFontFactory.getLabelFont()));
       //Protocol Enhancements - Reading header from the prop file End
        
        scrPnSubjects.setViewportView(tblSubjects);
        //scrPnSubjects.setPreferredSize(new Dimension(350,130));
        //scrPnSubjects.setMinimumSize(new Dimension(300,100));
        scrPnSubjects.setMinimumSize(new Dimension(400,390));
        scrPnSubjects.setPreferredSize(new Dimension(400,390));
        
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.insets = new Insets(5, 5, 5, 10);
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        pnlSubjects.add(scrPnSubjects, gridBagConstraints);
        
        setLayout(new FlowLayout(FlowLayout.LEFT));
        add(pnlSubjects);
        
        //Protocol Enhancement - Saving null in db 8
        tblSubjects.addMouseListener(new MouseAdapter(){
            //String strSearch=null;
            public void mouseClicked(MouseEvent me){
                int selRow = tblSubjects.getSelectedRow();
                //String stId=(String)tblFundSoForm.getModel().getValueAt(selRow,2);
                //strSearch=(String)((DefaultTableModel)tblFundSoForm.
                //getModel()).getValueAt(selRow,1);
                tblSubjects.editCellAt(selRow,3);
                tblSubjects.getEditorComponent().requestFocusInWindow();
            }
        });
        //Protocol Enhancement - Saving null in db 8
        
        java.awt.Component[] components = {tblSubjects, btnAddSubject,btnDeleteSubject};
        ScreenFocusTraversalPolicy  traversePolicy = new ScreenFocusTraversalPolicy( components );
        pnlSubjects.setFocusTraversalPolicy(traversePolicy);
        
        //Protocol Enhancement - Saving null in db 9
        setTableKeyTraversal();
        //Protocol Enhancement - Saving null in db 9
        
        pnlSubjects.setFocusCycleRoot(true);  
        
    }
    
    /**
     * This method is used to the set the Available Subjects of this component.
     * @param availSubjects Collection of Vulnerable Subject details.
     */
    public void setAvailableSubjects(Vector availSubjects){
        this.availableSubjects = availSubjects;
    }
    
    /* Method to get all the column names of JTable*/
    private Vector getSubjectColNames(){
        
        Enumeration enumColNames = tblSubjects.getColumnModel().getColumns();
        Vector vecColNames = new Vector();
        String strName = "";
        while(enumColNames.hasMoreElements()){
            
            strName = (String)((TableColumn)
                enumColNames.nextElement()).getHeaderValue();
            vecColNames.addElement(strName);
        }
        return vecColNames;
    }
    
    
    /**
     * This method is used to set the enabled status for the components
     * depending on the functionType specified.
     */
    private void formatFields(){

        //code added for coeus4.3 enhancements that UI to be in display mode
        //when new amendment or renewal is created
//        boolean enabled = functionType != 'D' ? true : false ;        
        boolean enabled = (functionType == CoeusGuiConstants.DISPLAY_MODE || 
                functionType == CoeusGuiConstants.AMEND_MODE) ? false : true;

        btnAddSubject.setEnabled(enabled);
        
        if( tblSubjects.getRowCount() > 0 ){
            btnDeleteSubject.setEnabled(enabled);
        }else{
            btnDeleteSubject.setEnabled( false );
        }
        
        //Added by Amit 11/18/2003
        //code added for coeus4.3 enhancements that UI to be in display mode
        //when new amendment or renewal is created
//        if(functionType == CoeusGuiConstants.DISPLAY_MODE){        
        if(functionType == CoeusGuiConstants.DISPLAY_MODE || 
                functionType == CoeusGuiConstants.AMEND_MODE){
            java.awt.Color bgListColor = (java.awt.Color)javax.swing.UIManager.getDefaults().get("Panel.background");
        
            tblSubjects.setBackground(bgListColor);    
            tblSubjects.setSelectionBackground(bgListColor );
            tblSubjects.setSelectionForeground(Color.black);            
        }
        else{
            tblSubjects.setBackground(Color.white);            
            tblSubjects.setSelectionBackground(Color.white);
            tblSubjects.setSelectionForeground(Color.black);            
        }
        //end Amit         

    }
    
    
    //supporting method to get the table data vector.
    private Vector getSubTableData(){
        Vector tableData = new Vector();

        if( ( subjects != null ) && ( subjects.size() > 0 ) ){
            int subCount = subjects.size();
            for( int loopIndex = 0 ; loopIndex <  subCount ; loopIndex++ ){
                Vector tableRowData = new Vector();
                subListBean = (ProtocolVulnerableSubListsBean)
                        subjects.elementAt(loopIndex);
                tableRowData.addElement("");
                tableRowData.addElement(""+subListBean.
                                                getVulnerableSubjectTypeCode());
                tableRowData.addElement(subListBean.
                                                getVulnerableSubjectTypeDesc());
                
                //Protocol Enhancement - Saving null in db Start 2
                //tableRowData.addElement(""+subListBean.getSubjectCount());
                tableRowData.addElement(subListBean.getSubjectCount());
                //Protocol Enhancement - Saving null in db End 2
                
                tableData.addElement(tableRowData);
            }
        }
        return tableData;
    }

    //supporting method to hide the repsective column in the table.
    private void setTableEditors(){
        tblSubjects.setOpaque(false);
        tblSubjects.setShowVerticalLines(false);
        tblSubjects.setShowHorizontalLines(false);
        tblSubjects.setRowHeight(22);
       // tblSubjects.setSelectionBackground(Color.white);
        //tblSubjects.setSelectionForeground(Color.black);
        
        TableColumn column = tblSubjects.getColumnModel().getColumn(0);
        column.setMinWidth(30);
        column.setMaxWidth(30);
        column.setResizable(false);
        column.setCellRenderer(new IconRenderer());
        column.setHeaderRenderer(new EmptyHeaderRenderer());
        column.setPreferredWidth(30);        
        
        column = tblSubjects.getColumnModel().getColumn(1);
        column.setMinWidth(0);
        //column.setMaxWidth(0);
        column.setPreferredWidth(0);
        
        column = tblSubjects.getColumnModel().getColumn(2);
        column.setMinWidth(250);
        //column.setMaxWidth(0);
        column.setPreferredWidth(250);
        
        column = tblSubjects.getColumnModel().getColumn(3);
        column.setMinWidth(100);
        //column.setMaxWidth(50);
        column.setPreferredWidth(100);
		//changed the allowable field length to 6 from 3 by Jobin. DB field changed to allow 6 numbers.
        column.setCellEditor(new DefaultCellEditor(
            new CoeusTextField(new JTextFieldFilter(JTextFieldFilter.NUMERIC,6),"",3)));
        tblSubjects.getTableHeader().setReorderingAllowed(false);
        //tblSubjects.getTableHeader().setResizingAllowed(false);
        tblSubjects.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        if(tblSubjects.getRowCount() > 0) {
            tblSubjects.setRowSelectionInterval(0,0);
        }
    }
 
    //supporting method to set the form data of protocol maintenance.
    private void setFormData(){
        //Commented, to bring the empty table from parent protocol
//        if ( ( subjects != null ) && ( subjects.size() > 0 )){
            ((DefaultTableModel)tblSubjects.getModel()).setDataVector(
                        getSubTableData(), getSubjectColNames());
                ((DefaultTableModel)
                        tblSubjects.getModel()).fireTableDataChanged();
//        }
        setTableEditors();
        saveRequired = false;
    }
    
    /** Validate the form data before sending to the database.
     *
     * @return true if all form controls have valid data, else false.
     * @throws Exception with the custom message if any form control doesn't
     * have valid data.
     */
    public boolean validateData() throws Exception, CoeusUIException{
    
        //Protocol Enhancement - Saving null in db Start 3
       /* int rowCount = tblSubjects.getRowCount();
        if( rowCount > 0 ){
            for( int rowIndex = 0 ; rowIndex < rowCount ; rowIndex++ ) {
                String subjectCount = (String)tblSubjects.getValueAt(rowIndex, 3 );
                if( subjectCount != null && subjectCount.trim().length() > 0 ){
                    int count = Integer.parseInt(subjectCount);
                    if( count <= 0 ){
                        tblSubjects.setRowSelectionInterval(rowIndex,rowIndex);
                        //throw new Exception(coeusMessageResources.parseMessageKey(
                        //    "vulSubjFrm_exceptionCode.2531"));
                        //Bug Fix ( Defect Id : 379)
                        log("vulSubjFrm_exceptionCode.2531");
                        //Bug Fix ( Defect Id : 379)
                    }
                }else{
                    tblSubjects.setRowSelectionInterval(rowIndex,rowIndex);
                    //throw new Exception(coeusMessageResources.parseMessageKey(
                    //    "vulSubjFrm_exceptionCode.2532"));
                    //Bug Fix ( Defect Id : 379)
                    log("vulSubjFrm_exceptionCode.2532");
                    //Bug Fix ( Defect Id : 379)
                    
                }
            }
        }*/
        
        //Protocol Enhancement - Saving null in db End 3
        
        /*else{
            throw new Exception(coeusMessageResources.parseMessageKey(
                "protoMntFrm_exceptionCode.1078"));
        }*/
        return true;
    }
    
    //Bug Fix ( Defect Id : 379)
    public void log(String mesg) throws CoeusUIException {
        CoeusUIException coeusUIException = new CoeusUIException(mesg,CoeusUIException.WARNING_MESSAGE);
        coeusUIException.setTabIndex(7);
        throw coeusUIException;
    }
    //Bug Fix ( Defect Id : 379)
    
    /**
     * This method is used to get the vulnerable subjects selected by the user, as a
     * collection of ProtocolVulnerableSubListsBean.
     * @throws Exception if unable to construct beans for the vulnerable subjects selected.
     * @return  Collection of ProtocolVulnerableSubListsBean
     */    
    public Vector getVulnerableSubjects() throws Exception{
        if(tblSubjects.getRowCount() > 0){
            boolean found = false;
            if(tblSubjects.isEditing()){
                if(tblSubjects.getCellEditor() != null){
                    tblSubjects.getCellEditor().stopCellEditing();
                }
            }
            Vector newSubjects = ((DefaultTableModel)
                    tblSubjects.getModel()).getDataVector();
            Enumeration enumNewSubjects = newSubjects.elements();
            ProtocolVulnerableSubListsBean newListBean = null;
            Vector newSubject = null;
            //subjects = protocolInfoBean.getVulnerableSubjectLists();
            while(enumNewSubjects.hasMoreElements()){
                found = false;
                newSubject = (Vector)enumNewSubjects.nextElement();
                newListBean = new ProtocolVulnerableSubListsBean();
                newListBean.setVulnerableSubjectTypeCode(
                        Integer.parseInt(newSubject.elementAt(1).toString()));

                newListBean.setVulnerableSubjectTypeDesc(
                        newSubject.elementAt(2).toString());
                
                //Protocol Enhancement - Saving null in db Start 4
                /*newListBean.setSubjectCount(
                    Integer.parseInt(newSubject.elementAt(3).toString()));*/
                if(newSubject.elementAt(3) instanceof String){
                    String subCount = (String)newSubject.elementAt(3);
                    if( (subCount != null && subCount.trim().equals("")) || 
                        (subCount == null) ){
                        newListBean.setSubjectCount(null);
                    }else{
                        newListBean.setSubjectCount(new Integer(subCount));
                    }
                }else{
                    newListBean.setSubjectCount((Integer)newSubject.elementAt(3));
                }
                //Protocol Enhancement - Saving null in db End 4

                if( ( subjects != null ) && ( subjects.size() > 0 ) ){
                    for( int loopIndex = 0 ; loopIndex < subjects.size() ;
                            loopIndex++ ){
                        subListBean = (ProtocolVulnerableSubListsBean)
                                subjects.elementAt(loopIndex);

                        if(newListBean.getVulnerableSubjectTypeCode()
                                == subListBean.getVulnerableSubjectTypeCode()){
                            found = true;
                            break;
                        }
                    }
                }else{
                    subjects = new Vector();
                }
                if(!found){
                    newListBean.setAcType("I");
                    /*if(functionType == 'M'){
                        protocolInfoBean.setAcType("U");
                    }*/
                    subjects.addElement(newListBean);                    
                    saveRequired = true;
                }
            }
            int subCount = subjects.size();
            for( int loopIndex = 0 ; loopIndex <  subCount ; loopIndex++ ){
                found = false;
                subListBean = (ProtocolVulnerableSubListsBean)
                        subjects.elementAt(loopIndex);
                subListBean.addPropertyChangeListener(
                    new PropertyChangeListener(){
                        public void propertyChange(PropertyChangeEvent pce){
                            
                            if(functionType == 'M'){
                                //Protocol Enhancement - Saving null in db Start 5
                                if(!pce.getOldValue().equals(pce.getNewValue())){
                                //Protocol Enhancement - Saving null in db End 5
                                    subListBean.setAcType("U");
                                    saveRequired = true;                             
                                }
                            }
                        }
                });
                int newSubCount = newSubjects.size();
                for(int tableRow = 0;tableRow < newSubCount ; tableRow++){
                    newSubject = (Vector)newSubjects.elementAt(tableRow);
                    newListBean = new ProtocolVulnerableSubListsBean();
                    newListBean.setVulnerableSubjectTypeCode(
                          Integer.parseInt(newSubject.elementAt(1).toString()));

                    newListBean.setVulnerableSubjectTypeDesc(
                            newSubject.elementAt(2).toString());

                    //Protocol Enhancement - Saving null in db Start 6
                    /*newListBean.setSubjectCount(
                        Integer.parseInt(newSubject.elementAt(3).toString()));*/
                    if(newSubject.elementAt(3) instanceof String){
                        String subjCount = (String)newSubject.elementAt(3);
                        if( (subjCount != null && subjCount.trim().equals("")) || 
                            (subjCount == null) ){
                            newListBean.setSubjectCount(null);
                        }else{
                            newListBean.setSubjectCount(new Integer(subjCount));
                        }
                    }else{
                        newListBean.setSubjectCount((Integer)newSubject.elementAt(3));
                    }
                    //Protocol Enhancement - Saving null in db End  6

                    if(newListBean.getVulnerableSubjectTypeCode()
                            == subListBean.getVulnerableSubjectTypeCode()){
                        found = true;
                        break;
                    }
                }
                if(!found){
                   subListBean.setAcType("D");
                   saveRequired = true;                   
                }else{
                    //Protocol Enhancement - Saving null in db Start 7
                    //subListBean.setSubjectCount(newListBean.getSubjectCount());
                    if(newListBean.getSubjectCount()== null){
                        if(subListBean.getSubjectCount() != null)
                            subListBean.setSubjectCount(null);
                    }else{
                        subListBean.setSubjectCount(newListBean.getSubjectCount());
                    }
                    //Protocol Enhancement - Saving null in db End 7
                }
               subjects.setElementAt(subListBean,loopIndex);
            }
        }else{
            if ( subjects != null && subjects.size() > 0 ){
                // Added By Raghunath P.V.
                ProtocolVulnerableSubListsBean protocolVulnerableSubListsBean = null;
                for(int index = 0; index < subjects.size(); index++){

                    protocolVulnerableSubListsBean = (ProtocolVulnerableSubListsBean)subjects.get(index);

                    if(protocolVulnerableSubListsBean != null){

                        String acType = protocolVulnerableSubListsBean.getAcType();
                        if(acType == null){
                            protocolVulnerableSubListsBean.setAcType("D");
                        }
                    }
                }
            }
        }
        return setAcTypesInAmend();
        //return subjects;
    }
    
    
    private Vector setAcTypesInAmend(){
        Vector vecToSend = new Vector();
        if(subjects != null){
            int size = subjects.size();
            ProtocolVulnerableSubListsBean protocolVulnerableSubListsBean = null;
            for(int index = 0; index < size; index++){
                protocolVulnerableSubListsBean = (ProtocolVulnerableSubListsBean)subjects.get(index);
        
                if( functionType == CoeusGuiConstants.AMEND_MODE ) {
                    if(protocolVulnerableSubListsBean.getAcType() == null 
                            ||  !protocolVulnerableSubListsBean.getAcType().equalsIgnoreCase("D")) {
                        protocolVulnerableSubListsBean.setAcType( "I" );
                        vecToSend.addElement( protocolVulnerableSubListsBean );
                    }
                }else{//if( protocolVulnerableSubListsBean.getAcType() != null ) 
                    vecToSend.addElement( protocolVulnerableSubListsBean );
                }
            }
            return vecToSend;
        }else{
            return subjects;
        }
    }
    
    /**
     * Method which specifies whether there is any unsaved information is present.
     * @return  boolean true if there is any unsaved data present, else false. */    
    public boolean isSaveRequired(){
        /* to check whether count has been modified for any vulnerable subject
           fetch all the details. if any changes have been made to any vulnerable
           subject count, the corresponding bean will fire property change listener
           and sets the saveRequired flag to true. */
        try{
            getVulnerableSubjects();
        }catch(Exception e){
            /* unable to fetch the details properyly, so user might have modified
               some data */
            saveRequired  = true;
        }
        return saveRequired;
    }
    
    //Added by Amit 11/21/2003
    /** This method use to implement focus on first editable component in this page.
     */
    public void setDefaultFocusForComponent(){
        //code added for coeus4.3 enhancements that UI to be in display mode
        //when new amendment or renewal is created
//        if(!( functionType == CoeusGuiConstants.DISPLAY_MODE )) {        
        if(!( functionType == CoeusGuiConstants.DISPLAY_MODE ) &&
               !( functionType == CoeusGuiConstants.AMEND_MODE )) {            
        
            if(tblSubjects.getRowCount() > 0 ) {
                tblSubjects.requestFocusInWindow();
                
                //included by raghu to remain the selection on row upon selection...
                //starts..
                int prevSelectedRow=tblSubjects.getSelectedRow();
                if(prevSelectedRow!=-1){
                    tblSubjects.setRowSelectionInterval(prevSelectedRow, prevSelectedRow);
                }
                else{
                    tblSubjects.setRowSelectionInterval(0, 0);
                }
                //ends
                
                tblSubjects.setColumnSelectionInterval(1,2);
            }else{
                btnAddSubject.requestFocusInWindow();
            }            
        }
    }    
    //end Amit     
    
    /**
     * This method is used to specify whether there is any unsaved data present.
     * @param save  boolean true to specify that save confirmation to be displayed
     * if the user tries to close the window without saving the details 
     */    
    public void setSaveRequired(boolean save){
        this.saveRequired = save;
    }
    
    /**
     * This method is used to specify the form opening mode.
     * @param fType  character which specifies the form open mode.
     */    
    public void setFunctionType(char fType){
        this.functionType = fType;
    }
    
    /**
     * This method is used to set the updated data to the form after saving to the database.
     * @param newSubjects  Collection of ProtocolVulnerableSubListsBean with latest
     * sequence number and data */    
    public void setValues(Vector newSubjects){
        
        int selectedRow=0;
        if(tblSubjects.getRowCount()>0){
            selectedRow=tblSubjects.getSelectedRow();
        }
       
        this.subjects = newSubjects;
        setFormData();
        formatFields();
        
        if(tblSubjects.getRowCount()>0 && tblSubjects.getRowCount() > selectedRow){
            tblSubjects.setRowSelectionInterval(selectedRow, selectedRow);
        }
    }
    
    //Protocol Enhancement - Saving null in db 10
    // This method will provide the key travrsal for the table cells
    // It specifies the tab and shift tab order.
    public void setTableKeyTraversal(){
        
        javax.swing.InputMap im = tblSubjects.getInputMap(JTable.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        // Have the enter key work the same as the tab key
        KeyStroke tab = KeyStroke.getKeyStroke(KeyEvent.VK_TAB, 0);
        KeyStroke shiftTab = KeyStroke.getKeyStroke(KeyEvent.VK_TAB,KeyEvent.SHIFT_MASK );
        
        // Override the default tab behaviour
        // Tab to the next editable cell. When no editable cells goto next cell.
        final Action oldTabAction = tblSubjects.getActionMap().get(im.get(tab));
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
                table.getEditorComponent().requestFocusInWindow();
                
            }
        };
        tblSubjects.getActionMap().put(im.get(tab), tabAction);
        
        
        
        
        // for the shift+tab action
        
        // Override the default tab behaviour
        // Tab to the previous editable cell. When no editable cells goto next cell.
        
        final Action oldTabAction1 = tblSubjects.getActionMap().get(im.get(shiftTab));
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
                        column = 3;
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
                table.getEditorComponent().requestFocusInWindow();
            }
        };
        tblSubjects.getActionMap().put(im.get(shiftTab), tabAction1);
        
        
        java.awt.Component[] components = {tblSubjects, btnAddSubject,btnDeleteSubject};
        ScreenFocusTraversalPolicy  traversePolicy = new ScreenFocusTraversalPolicy( components );
        setFocusTraversalPolicy(traversePolicy);
        setFocusCycleRoot(true);
        
        
    }
    //Protocol Enhancement - Saving null in db 10
}
