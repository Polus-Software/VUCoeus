/* 
 * ProtocolPersonsResponsibleForm.java
 *
 * Created on December 22, 2010, 5:44 PM
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
import edu.mit.coeus.gui.CoeusAppletMDIForm;
import edu.mit.coeus.gui.CoeusDlgWindow;
import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.iacuc.bean.ProtocolPersonsResponsibleBean;
import edu.mit.coeus.utils.AppletServletCommunicator;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.TypeConstants;
import edu.mit.coeus.utils.query.And;
import edu.mit.coeus.utils.query.Equals;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;
import java.util.Vector;
import javax.swing.AbstractAction;
import javax.swing.JDialog;
import javax.swing.ListSelectionModel;
import javax.swing.table.*;

/**
 *
 * @author  mdehtesham
 */
public class ProtocolPersonsResponsibleForm extends javax.swing.JPanel implements ActionListener {
    
    /* This is used to hold MDI form reference */
    private CoeusAppletMDIForm mdiReference;
    private CoeusDlgWindow dlgPersonResponsible;
    private CoeusMessageResources coeusMessageResources;
    //ProtocolPersonsResponsibleBean personResponsibleBean;
    PersonResponsibleTableModel personTableModel;
    private char functionType;
    Vector vecPersonResponseData; 
    Vector vecDefaultPersonData;
    private static final int PERSON_NAME_COLUMN = 0;
    private static final int PERSON_ID_COLUMN = 1;
    private static final int PERSON_TRAINING_COLUMN = 2;
    // Added for COEUSQA-3694 : Coeus4.5: IACUC Procedure Personnel Issue Remains - Start
    private static final int NON_EMPLOYEE_FLAG_COLUMN = 3;
    // Added for COEUSQA-3694 : Coeus4.5: IACUC Procedure Personnel Issue Remains - End
    private static final char GET_TRAINIG_INFO = 'a';    
    int speciesCode = 0;
    int procedureCode = 0;
    int studyGroupId = 0;
    HashMap hmPersons;
    private static final String PERSON_IS_SELECTED = "iacucProtoStudyGroupFrm_exceptionCode.1014";
    /** Creates new form ProtocolPersonsResponsibleForm */
    public ProtocolPersonsResponsibleForm() {
        initComponents();
    }
    
     public ProtocolPersonsResponsibleForm(CoeusAppletMDIForm mdiReference,
             char functionType, Vector vecPersonResponseData, Vector vecDefaultPersonData,
             int studyGroupId, int speciesCode, int procedureCode) {         
        this.mdiReference = mdiReference;
        this.functionType = functionType;      
        this.vecPersonResponseData = vecPersonResponseData;
        this.vecDefaultPersonData = vecDefaultPersonData;
        this.speciesCode = speciesCode;
        this.procedureCode = procedureCode;
        this.studyGroupId = studyGroupId;
        coeusMessageResources = CoeusMessageResources.getInstance();
        initComponents();        
        setFormData();
        setTableProperty();
        setListenerForButtons();
        showPersonResponsibleForm();      
    }
     
     
     /*
     * Table model for Study group person responsible table
     *
     */
    class PersonResponsibleTableModel extends DefaultTableModel {
        Vector vecData;
        // Modified for COEUSQA-3694 : Coeus4.5: IACUC Procedure Personnel Issue Remains - Start
//        private String colNames[] =
//                                   {"Investigators/Study Personnel","",""};
//        private Class colTypes[] =
//                                   {String.class,String.class,Boolean.class};
        private String colNames[] =
                                   {"Investigators/Study Personnel","","",""};
        private Class colTypes[] =
                                   {String.class,String.class,Boolean.class,Boolean.class};
        // Modified for COEUSQA-3694 : Coeus4.5: IACUC Procedure Personnel Issue Remains - End
        public boolean isCellEditable(int row, int column) {             
                return false;             
        }
        
        public int getColumnCount() {
            return colNames.length;
        }
        
        public Object getValueAt(int row, int column) {
            ProtocolPersonsResponsibleBean personBean = (ProtocolPersonsResponsibleBean)vecData.get(row);
            switch(column) {
                case PERSON_NAME_COLUMN:                     
                       return personBean.getPersonName();                 
             
                case PERSON_ID_COLUMN:                     
                       return personBean.getPersonId();                 
             
                case PERSON_TRAINING_COLUMN:                     
                       return personBean.isTrained();       
                // Added for COEUSQA-3694 : Coeus4.5: IACUC Procedure Personnel Issue Remains - Start                       
                case NON_EMPLOYEE_FLAG_COLUMN:
                    return personBean.isNonEmployeeFlag();
                // Added for COEUSQA-3694 : Coeus4.5: IACUC Procedure Personnel Issue Remains - End    
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
            
            ProtocolPersonsResponsibleBean personBean = (ProtocolPersonsResponsibleBean)vecData.get(row);
            switch(column) {
                case PERSON_ID_COLUMN:
                    personBean.setPersonId((String) value);
                    break; 
                case PERSON_NAME_COLUMN:  
                    personBean.setPersonName((String) value);                     
                    break;                 
                case PERSON_TRAINING_COLUMN:
                    personBean.setTrained((Boolean) value);
                    break; 
                // Added for COEUSQA-3694 : Coeus4.5: IACUC Procedure Personnel Issue Remains - Start                           
                case NON_EMPLOYEE_FLAG_COLUMN:
                    personBean.setNonEmployeeFlag(((Boolean)value).booleanValue());
                    break;
                // Added for COEUSQA-3694 : Coeus4.5: IACUC Procedure Personnel Issue Remains - End    
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

        scrPnPersonsResponsible = new javax.swing.JScrollPane();
        tblPersons = new javax.swing.JTable();
        pnlButtons = new javax.swing.JPanel();
        btnOk = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();

        setLayout(new java.awt.GridBagLayout());

        setMaximumSize(new java.awt.Dimension(410, 260));
        setMinimumSize(new java.awt.Dimension(410, 260));
        setPreferredSize(new java.awt.Dimension(410, 260));
        scrPnPersonsResponsible.setMaximumSize(new java.awt.Dimension(320, 240));
        scrPnPersonsResponsible.setMinimumSize(new java.awt.Dimension(320, 240));
        scrPnPersonsResponsible.setPreferredSize(new java.awt.Dimension(320, 240));
        tblPersons.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        scrPnPersonsResponsible.setViewportView(tblPersons);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(20, 5, 20, 5);
        add(scrPnPersonsResponsible, gridBagConstraints);

        pnlButtons.setLayout(new java.awt.GridBagLayout());

        pnlButtons.setMaximumSize(new java.awt.Dimension(80, 65));
        pnlButtons.setMinimumSize(new java.awt.Dimension(80, 65));
        pnlButtons.setPreferredSize(new java.awt.Dimension(80, 65));
        btnOk.setFont(CoeusFontFactory.getLabelFont());
        btnOk.setText("OK");
        btnOk.setMaximumSize(new java.awt.Dimension(75, 26));
        btnOk.setMinimumSize(new java.awt.Dimension(75, 26));
        btnOk.setPreferredSize(new java.awt.Dimension(75, 26));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 0);
        pnlButtons.add(btnOk, gridBagConstraints);

        btnCancel.setFont(CoeusFontFactory.getLabelFont());
        btnCancel.setLabel("Cancel");
        btnCancel.setMaximumSize(new java.awt.Dimension(75, 26));
        btnCancel.setMinimumSize(new java.awt.Dimension(75, 26));
        btnCancel.setPreferredSize(new java.awt.Dimension(75, 26));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        pnlButtons.add(btnCancel, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(20, 0, 0, 0);
        add(pnlButtons, gridBagConstraints);

    }// </editor-fold>//GEN-END:initComponents

     /** This method uses the cvExceptionsCategory Vector for the protocol
     * cvExceptionsCategory and displays the contents of the vector in the ScientificJustificationExceptionForm
     *
     */
    private void setFormData(){
        if(vecPersonResponseData == null){
            vecPersonResponseData = new Vector();
        } 
       personTableModel = new PersonResponsibleTableModel();
        tblPersons.setModel(personTableModel);                                          
            personTableModel.setData(vecPersonResponseData);            
        int selectedRow=0;
        if(tblPersons.getRowCount()>0)
        {
            tblPersons.setRowSelectionInterval(selectedRow,selectedRow);
        }
                                       
    }
     
    private void setTableProperty(){
        tblPersons.setBackground(java.awt.Color.white);         
        tblPersons.setSelectionForeground(java.awt.Color.black);
        tblPersons.getTableHeader().setFont(CoeusFontFactory.getLabelFont());
        tblPersons.setFont(CoeusFontFactory.getNormalFont());
        tblPersons.setOpaque(false);
        tblPersons.setShowVerticalLines(false);
        tblPersons.setShowHorizontalLines(false);
        tblPersons.setRowHeight(22);   
        tblPersons.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tblPersons.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        JTableHeader header = tblPersons.getTableHeader();
        header.setReorderingAllowed(false);
        TableColumn column = tblPersons.getColumnModel().getColumn(PERSON_NAME_COLUMN);         
        column.setMinWidth(315);
        column.setMaxWidth(315);
        column.setPreferredWidth(315);         
        column.setResizable(false);
        column = tblPersons.getColumnModel().getColumn(PERSON_ID_COLUMN); 
        column.setMinWidth(0);
        column.setMaxWidth(0);
        column.setPreferredWidth(0);         
        column.setResizable(false);
        column = tblPersons.getColumnModel().getColumn(PERSON_TRAINING_COLUMN);  
        column.setMinWidth(0);
        column.setMaxWidth(0);
        column.setPreferredWidth(0);         
        column.setResizable(false);
        // Added for COEUSQA-3694 : Coeus4.5: IACUC Procedure Personnel Issue Remains - Start
        column = tblPersons.getColumnModel().getColumn(NON_EMPLOYEE_FLAG_COLUMN);  
        column.setMinWidth(0);
        column.setMaxWidth(0);
        column.setPreferredWidth(0);         
        column.setResizable(false);
        // Added for COEUSQA-3694 : Coeus4.5: IACUC Procedure Personnel Issue Remains - End
    }
    
    private void setListenerForButtons(){
        btnOk.addActionListener(this);
        btnCancel.addActionListener(this);
    }
        
    /**
     * Method to initialise dialog     
     */
    public void showPersonResponsibleForm(){ 
        String title = "Persons Responsible"; 
        dlgPersonResponsible = new CoeusDlgWindow(mdiReference,
        title,true);
        Dimension screenSize = Toolkit.getDefaultToolkit().
        getScreenSize();         
        dlgPersonResponsible.setResizable(false);
        dlgPersonResponsible.setSize(600,500);
        dlgPersonResponsible.getContentPane().add(this);
        Dimension dlgSize = dlgPersonResponsible.getSize();
        dlgPersonResponsible.setLocation(screenSize.width/2 - (dlgSize.width/2),
        screenSize.height/2 - (dlgSize.height/2));
        screenSize = null;          
        dlgPersonResponsible.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        
        dlgPersonResponsible.addWindowListener(new WindowAdapter(){
            public void windowActivated(WindowEvent e) {
                if(e.getComponent()==null){
                    btnOk.requestFocusInWindow();
                }
            }
            public void windowClosing(WindowEvent we){                 
                try{
                    //validateData();
                    dlgPersonResponsible.dispose();
                    
                }catch(Exception ex){
                    CoeusOptionPane.showInfoDialog(ex.getMessage());
                }
            }
        });
        dlgPersonResponsible.addEscapeKeyListener(
        new AbstractAction("escPressed"){
            public void actionPerformed(ActionEvent ae){
                try{
                    if(TypeConstants.DISPLAY_MODE == functionType){
                    dlgPersonResponsible.dispose();         
                } 
                }catch(Exception ex){
                    CoeusOptionPane.showInfoDialog(ex.getMessage());
                }
            }
        });         
        dlgPersonResponsible.pack();
        dlgPersonResponsible.show();
    }
              
      public void actionPerformed( ActionEvent btnActionEvent ){

        Object actionSource = btnActionEvent.getSource();      

           if( actionSource.equals( this.btnOk ) ){
            
            int[] selectedRows = this.tblPersons.getSelectedRows();
            //ProtocolPersonsResponsibleBean personsResponsibleBean = null;
            
            if(selectedRows != null && selectedRows.length > 0){
                
                int selSize = selectedRows.length;
                
                Vector vecSelectedRows = new Vector();
                for(int index = 0 ; index < selSize; index++){
                    
                    int selIndex = selectedRows[index];
                    
                    String stPersonName = ( String )
                                ((DefaultTableModel)tblPersons.
                                        getModel()).getValueAt(selIndex,0);
                    String stPersonId = ( String )
                                ((DefaultTableModel)tblPersons.
                                        getModel()).getValueAt(selIndex,1);
                    boolean boNonEmployeeFlag = ((Boolean)((DefaultTableModel)tblPersons.
                                        getModel()).getValueAt(selIndex,3)).booleanValue();  
                    
                    ProtocolPersonsResponsibleBean personsResponsibleBean = new ProtocolPersonsResponsibleBean();
                    if(vecDefaultPersonData !=null && vecDefaultPersonData.size()>0){                         
                        Equals eqCorr;
                        And andPersonId; 
                        CoeusVector cvPersonData = new CoeusVector();
                        cvPersonData.addAll(vecDefaultPersonData);                            
                            personsResponsibleBean.setPersonId(stPersonId); 
                            personsResponsibleBean.setStudyGroupId(studyGroupId);
                            eqCorr= new Equals("personId",personsResponsibleBean.getPersonId());                             
                            CoeusVector filteredResult = cvPersonData.filter(eqCorr);
                            if(filteredResult.size()==0){
                                personsResponsibleBean.setPersonName(stPersonName);
                                personsResponsibleBean.setAcType(TypeConstants.INSERT_RECORD);
                                personsResponsibleBean.setTrained(getSpeciesTrainingInfo(stPersonId));                                
                                // Added for COEUSQA-3694 : Coeus4.5: IACUC Procedure Personnel Issue Remains - Start
                                personsResponsibleBean.setNonEmployeeFlag(boNonEmployeeFlag);
                                // Added for COEUSQA-3694 : Coeus4.5: IACUC Procedure Personnel Issue Remains - end
                                vecDefaultPersonData.add(personsResponsibleBean);
                            }else{
                                CoeusOptionPane.showInfoDialog
                                ("'"+stPersonName+"'"+" "+coeusMessageResources.parseMessageKey(PERSON_IS_SELECTED));
                                return;
                            }                                                 
                    }else{
                         personsResponsibleBean.setPersonId(stPersonId); 
                         personsResponsibleBean.setPersonName(stPersonName);
                         personsResponsibleBean.setStudyGroupId(studyGroupId);
                         personsResponsibleBean.setAcType(TypeConstants.INSERT_RECORD);
                         personsResponsibleBean.setTrained(getSpeciesTrainingInfo(stPersonId));  
                         // Added for COEUSQA-3694 : Coeus4.5: IACUC Procedure Personnel Issue Remains - Start        
                         personsResponsibleBean.setNonEmployeeFlag(boNonEmployeeFlag);
                         // Added for COEUSQA-3694 : Coeus4.5: IACUC Procedure Personnel Issue Remains - End
                         vecDefaultPersonData.add(personsResponsibleBean);
                    }
                    
                }
                if(vecDefaultPersonData != null && vecDefaultPersonData.size() > 0){
                setSpeciesPersonResponse(vecDefaultPersonData);
                }
                if( dlgPersonResponsible != null ){            
                dlgPersonResponsible.dispose();
                }
            }else{
                  CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey("iacucProtoStudyGroupFrm_exceptionCode.1013"));
                }                                    
        }else if(actionSource.equals( this.btnCancel )){
            
            if( dlgPersonResponsible != null ){            
                dlgPersonResponsible.dispose();
            }
            
        }
      }
    
    /** Get the training flag for the person id
     */
    private boolean getSpeciesTrainingInfo(String personId){
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
      
     /**
     * Getter for property new person responsible.
     * 
     * @return Value of vector vecDefaultPersonData.
     */
    public Vector getSpeciesPersonResponse() {
        return vecDefaultPersonData;
    }
    
    /**
     * Setter for property vecDefaultPersonData.
     * 
     * @param vecDefaultPersonData New value of person responsible data.
     */
    public void setSpeciesPersonResponse(Vector vecDefaultPersonData) {
        this.vecDefaultPersonData = vecDefaultPersonData;
    } 
      
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnOk;
    private javax.swing.JPanel pnlButtons;
    private javax.swing.JScrollPane scrPnPersonsResponsible;
    private javax.swing.JTable tblPersons;
    // End of variables declaration//GEN-END:variables
    
}
