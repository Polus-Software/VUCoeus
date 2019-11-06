/* 
 * PersonResponsibleTrainingDetailForm.java
 *
 * Created on March 07, 2011, 5:44 PM
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
import edu.mit.coeus.iacuc.bean.PersonResponsibleTrainingDetailBean;
import edu.mit.coeus.utils.AppletServletCommunicator;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.utils.DateUtils;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Vector;
import javax.swing.AbstractAction;
import javax.swing.JDialog;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;

/**
 *
 * @author  mdehtesham
 */
public class PersonResponsibleTrainingDetailForm extends javax.swing.JPanel implements ActionListener {
    
     /* This is used to hold MDI form reference */
    private CoeusAppletMDIForm mdiReference;
    private CoeusDlgWindow dlgIacucAlternativeSearch;      
    PersonTrainingDetailTableModel trainingdetailTableModel;
    private DateUtils dtUtils = new DateUtils();      
    String connectTo = CoeusGuiConstants.CONNECTION_URL + "/IacucProtocolServlet";
    private static final char GET_TRAINING_INFO = 'a';
    private static final String SPECIES_TRAINING_DETAIL = "SPECIES_TRAINING_DETAIL"; 
    //COEUSQA:3537 - Coeus IACUC Training Revisions - Start
//    private static final int TRAINING_DESC = 0;
    private static final int SPECIES_TYPE = 0;
    private static final int PROCUDERE_CATEGORY = 1;
    private static final int PROCUDERE_TYPE = 2;
//    private static final int DATE_REQUESTED = 3;
    private static final int DATE_SUBMITTED = 3;
//    private static final int DATE_ACKNOWLEDGE = 5;
//    private static final int FOLLOWUP_DATE = 6;
    //COEUSQA:3537 - End
    private static final int SPECIES_COLUMN_MIN_WIDTH = 110;
    private static final int SPECIES_COLUMN_MAX_WIDTH = 350;
    private static final int TRAINING_DESC_COLUMN_WIDTH = 200;
    private static final int TRAINING_DESC_COLUMN_MAX_WIDTH = 350;
    private static final int PROCEDURE_COL_WIDTH = 100;
    private static final int PROCEDURE_COL_MAX_WIDTH = 350;
    private static final int DATE_COL_WIDTH = 160;
    private static final int DATE_COL_MAX_WIDTH = 140;
    private static final int TRAINING_TABLE_ROW_HEIGHT = 22;
    private String personId = "";
    private String personName = "";
    private char functionType;
    
    /** Creates new form PersonResponsibleTrainingDetailForm */
    public PersonResponsibleTrainingDetailForm() {
        initComponents();
    }
    
    /** Creates new form PersonResponsibleTrainingDetailForm 
     *  to show and load the person training detail
     * @param CoeusAppletMDIForm mdiReference
     * @param String personId
     * @param String personName
     */
    public PersonResponsibleTrainingDetailForm(CoeusAppletMDIForm mdiReference, String personId, String personName, char functionType){
        this.mdiReference = mdiReference;
        this.personId = personId;
        this.personName = personName;
        this.functionType = functionType;
        initComponents();
        setListenersForButtons();
        setFormData();
        setTrainingDetailColumnProperties();
        formatField();
        showPersonTrainingDetailForm();                
    }
    
    /**
     * Method to initialise popup person training details dialog
     */
    public void showPersonTrainingDetailForm(){
         
        String title = "Training Information for - "+personName;         
        dlgIacucAlternativeSearch = new CoeusDlgWindow(mdiReference,
        title,true);
        Dimension screenSize = Toolkit.getDefaultToolkit().
        getScreenSize();         
        dlgIacucAlternativeSearch.setResizable(false);
        dlgIacucAlternativeSearch.setSize(650,300);
        dlgIacucAlternativeSearch.getContentPane().add(this);
        Dimension dlgSize = dlgIacucAlternativeSearch.getSize();
        dlgIacucAlternativeSearch.setLocation(screenSize.width/2 - (dlgSize.width/2),
        screenSize.height/2 - (dlgSize.height/2));
        screenSize = null;          
        dlgIacucAlternativeSearch.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        
        dlgIacucAlternativeSearch.addWindowListener(new WindowAdapter(){
            public void windowActivated(WindowEvent e) {
                if(e.getComponent()==null){                   
                }
            }
            public void windowClosing(WindowEvent we){                 
                try{                    
                    dlgIacucAlternativeSearch.dispose();
                    
                }catch(Exception ex){
                    CoeusOptionPane.showInfoDialog(ex.getMessage());
                }
            }
        });
        dlgIacucAlternativeSearch.addEscapeKeyListener(
        new AbstractAction("escPressed"){
            public void actionPerformed(ActionEvent ae){
                try{                     
                    dlgIacucAlternativeSearch.dispose();                                                             
                }catch(Exception ex){
                    CoeusOptionPane.showInfoDialog(ex.getMessage());
                }
            }
        });         
        dlgIacucAlternativeSearch.pack();
        dlgIacucAlternativeSearch.show();
    }
    
    /**
     * This method is used to set the person training detail into
     * person training detail form
     */
    private void setFormData(){
        Vector vecPersonTrainingData = getPersonTrainingData(personId);       
        trainingdetailTableModel = new PersonTrainingDetailTableModel();        
        if(vecPersonTrainingData == null || vecPersonTrainingData.isEmpty()){
          vecPersonTrainingData = new Vector();
        }      
        trainingdetailTableModel.setData(vecPersonTrainingData);
        tblTrainingDetail.setModel(trainingdetailTableModel);
    }
    
    /*
     * This Method is use to set the Table column properties
     *
     */
     private void setTrainingDetailColumnProperties(){
                  
        tblTrainingDetail.setOpaque(false);
        tblTrainingDetail.setShowVerticalLines(false);
        tblTrainingDetail.setShowHorizontalLines(false);
        tblTrainingDetail.setRowHeight(TRAINING_TABLE_ROW_HEIGHT);
        
        tblTrainingDetail.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tblTrainingDetail.setFont( CoeusFontFactory.getNormalFont() );
        JTableHeader header = tblTrainingDetail.getTableHeader();
        header.setReorderingAllowed(false);
             
        //COEUSQA:3537 - Coeus IACUC Training Revisions - Start
//        TableColumn column = tblTrainingDetail.getColumnModel().getColumn(TRAINING_DESC);
//        column.setPreferredWidth(TRAINING_DESC_COLUMN_WIDTH);
//        column.setMinWidth(TRAINING_DESC_COLUMN_WIDTH);
//        column.setMaxWidth(TRAINING_DESC_COLUMN_MAX_WIDTH);        
//        column.sizeWidthToFit();
//        column.setResizable(true);
        
        TableColumn column = tblTrainingDetail.getColumnModel().getColumn(SPECIES_TYPE);
        column.setPreferredWidth(SPECIES_COLUMN_MIN_WIDTH);
        column.setMinWidth(SPECIES_COLUMN_MIN_WIDTH);
        column.setMaxWidth(SPECIES_COLUMN_MAX_WIDTH);        
        column.sizeWidthToFit();
        column.setResizable(true);
        
        column = tblTrainingDetail.getColumnModel().getColumn(PROCUDERE_CATEGORY);
        column.setPreferredWidth(TRAINING_DESC_COLUMN_WIDTH);
        column.setMinWidth(PROCEDURE_COL_WIDTH);
        column.setMaxWidth(PROCEDURE_COL_MAX_WIDTH);           
        column.sizeWidthToFit();
        column.setResizable(true);
        
        column = tblTrainingDetail.getColumnModel().getColumn(PROCUDERE_TYPE);
        column.setPreferredWidth(PROCEDURE_COL_WIDTH);
        column.setMinWidth(PROCEDURE_COL_WIDTH);
        column.setMaxWidth(PROCEDURE_COL_MAX_WIDTH);           
        column.sizeWidthToFit();
        column.setResizable(true);
                        
//        column = tblTrainingDetail.getColumnModel().getColumn(DATE_REQUESTED);
//        column.setPreferredWidth(DATE_COL_WIDTH);
//        column.setMinWidth(DATE_COL_WIDTH);
//        column.setMaxWidth(DATE_COL_MAX_WIDTH);           
//        column.sizeWidthToFit();
//        column.setResizable(true);
        
        column = tblTrainingDetail.getColumnModel().getColumn(DATE_SUBMITTED);
        column.setPreferredWidth(DATE_COL_WIDTH);
        column.setMinWidth(DATE_COL_WIDTH);
        column.setMaxWidth(DATE_COL_MAX_WIDTH);           
        column.sizeWidthToFit();
        column.setResizable(true);
        
//        column = tblTrainingDetail.getColumnModel().getColumn(DATE_ACKNOWLEDGE);
//        column.setPreferredWidth(DATE_COL_WIDTH);
//        column.setMinWidth(DATE_COL_WIDTH);
//        column.setMaxWidth(DATE_COL_MAX_WIDTH);           
//        column.sizeWidthToFit();
//        column.setResizable(true);
//        
//        column = tblTrainingDetail.getColumnModel().getColumn(FOLLOWUP_DATE);
//        column.setPreferredWidth(DATE_COL_WIDTH);
//        column.setMinWidth(DATE_COL_WIDTH);
//        column.setMaxWidth(DATE_COL_MAX_WIDTH);           
//        column.sizeWidthToFit();
//        column.setResizable(true);
        //COEUSQA:3537 - End
         
        tblTrainingDetail.getTableHeader().setReorderingAllowed( false );
        tblTrainingDetail.getTableHeader().setResizingAllowed(true);
        tblTrainingDetail.setFont(CoeusFontFactory.getNormalFont());       
        tblTrainingDetail.getTableHeader().setFont(CoeusFontFactory.getLabelFont());
     }
    
    /**
     * This method is used to get all the person training detail 
     * for selected person
     * @param String personId
     * @return Vector data
     */
    private Vector getPersonTrainingData(String personId){
        Vector data = null;
        
        RequesterBean requester;
        ResponderBean responder;
        
        requester = new RequesterBean();
        requester.setId(SPECIES_TRAINING_DETAIL);
        requester.setFunctionType(GET_TRAINING_INFO);
        requester.setDataObject(personId);        
        AppletServletCommunicator comm
                = new AppletServletCommunicator(connectTo, requester);        
        comm.send();
        responder = comm.getResponse();
        if(responder.isSuccessfulResponse()){
            data = (Vector)responder.getDataObjects();
        }
        return data;
    }
    
   /** This method is used to set the listeners to the buttons OK and Cancel */
    private void setListenersForButtons(){         
        btnCancel.addActionListener(this); 
        btnCancel.setFocusable(true);
    }
    
    /*
     * This method is used to format the tblTrainingDetail mode         
     */
    private void formatField(){
        if(functionType == CoeusGuiConstants.DISPLAY_MODE || functionType == CoeusGuiConstants.AMEND_MODE){
            
            java.awt.Color bgListColor = (java.awt.Color)javax.swing.UIManager.getDefaults().get("Panel.background");
            
            tblTrainingDetail.setBackground(bgListColor);
            tblTrainingDetail.setSelectionBackground(bgListColor );
            tblTrainingDetail.setSelectionForeground(java.awt.Color.BLACK);
        } else{
            tblTrainingDetail.setBackground(java.awt.Color.white);
            tblTrainingDetail.setSelectionBackground(java.awt.Color.white);
            tblTrainingDetail.setSelectionForeground(java.awt.Color.black);
        }
    }
    
    /** Action Performed Method
     * @param actionEvent Action Event Object
     */    
    public void actionPerformed(java.awt.event.ActionEvent actionEvent) {
        Object actionSource = actionEvent.getSource();
        try{
             if (actionSource.equals(btnCancel)){
                dlgIacucAlternativeSearch.dispose();                 
            }  
        }catch(Exception e){
            e.printStackTrace();
            CoeusOptionPane.showInfoDialog( e.getMessage() );
        }
    }
    
    /*
     * Table model for Study group person responsible table
     *
     */
    class PersonTrainingDetailTableModel extends DefaultTableModel {
        Vector vecData;
        //COEUSQA:3537 - Coeus IACUC Training Revisions - Start
//        private String colNames[] =
//                                   {"Training","Species","Procedure","Date Requested","Date Submitted","Date Acknowledge","Followup Date"};        
        
//        private Class colTypes[] =
//                                   {String.class,String.class,String.class,Object.class,Object.class,Object.class,Object.class};
        private String colNames[] =
                                   {"Species","Procedure Category","Procedure","Date Completed"};
         private Class colTypes[] =
                                   {String.class,String.class,String.class,Object.class};
        //COEUSQA:3537 - End
        public boolean isCellEditable(int row, int column) {             
                return false;             
        }
        
        public int getColumnCount() {
            return colNames.length;
        }
        
         /**
         * This method is used to get the table column value          
         * @param int row
         * @param int column
         * @return Object value
         */
        public Object getValueAt(int row, int column) {
            PersonResponsibleTrainingDetailBean trainingDetailBean = (PersonResponsibleTrainingDetailBean)vecData.get(row);
            switch(column) {
                //COEUSQA:3537 - Coeus IACUC Training Revisions - Start
//                case TRAINING_DESC:                     
//                       return trainingDetailBean.getTrainingDescription();                      
                case SPECIES_TYPE:                
                      return trainingDetailBean.getSpeciesType();
                case PROCUDERE_CATEGORY:                
                      return trainingDetailBean.getProcedureCategory(); 
                case PROCUDERE_TYPE:                
                      return trainingDetailBean.getProcedureType(); 
//                case DATE_REQUESTED:
//                         if(trainingDetailBean.getDateRequested() == null){
//                            return CoeusGuiConstants.EMPTY_STRING;
//                        }else{
//                            return dtUtils.formatDate(trainingDetailBean.getDateRequested().toString(),"dd-MMM-yyyy");
//                        }                         
                case DATE_SUBMITTED:
                        if(trainingDetailBean.getDateSubmitted() == null){
                            return CoeusGuiConstants.EMPTY_STRING;
                        }else{
                            return dtUtils.formatDate(trainingDetailBean.getDateSubmitted().toString(),"dd-MMM-yyyy");
                        }                       
//                case DATE_ACKNOWLEDGE: 
//                        if(trainingDetailBean.getDateAcknowledge() == null){
//                            return CoeusGuiConstants.EMPTY_STRING;
//                        }else{
//                            return dtUtils.formatDate(trainingDetailBean.getDateAcknowledge().toString(),"dd-MMM-yyyy");
//                        }                       
//                case FOLLOWUP_DATE:
//                        if(trainingDetailBean.getFollowupDate() == null){
//                            return CoeusGuiConstants.EMPTY_STRING;
//                        }else{
//                            return dtUtils.formatDate(trainingDetailBean.getFollowupDate().toString(),"dd-MMM-yyyy");
//                        }
                        //COEUSQA:3537 - End
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
        
         /**
         * This method is used to set the table column value          
         * @param Object value
         * @param int row
         * @param int column        
         */
        public void setValueAt(Object value, int row, int column) {                         
            PersonResponsibleTrainingDetailBean trainingDetailBean = (PersonResponsibleTrainingDetailBean)vecData.get(row);
            switch(column) {  
                //COEUSQA:3537 - Coeus IACUC Training Revisions - Start
//               case TRAINING_DESC:                                             
//                       break;
                case SPECIES_TYPE:                                       
                      break;
                case PROCUDERE_CATEGORY:                                       
                      break;
                case PROCUDERE_TYPE:                                       
                      break;
//                case DATE_REQUESTED:                                       
//                      break;
                case DATE_SUBMITTED:                                                              
                      break;
//               ;
                //COEUSQA:3537 - End
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

        jPanel1 = new javax.swing.JPanel();
        btnCancel = new javax.swing.JButton();
        scrPnTrainingDetail = new javax.swing.JScrollPane();
        tblTrainingDetail = new javax.swing.JTable();

        setLayout(new java.awt.GridBagLayout());

        setMaximumSize(new java.awt.Dimension(650, 300));
        setMinimumSize(new java.awt.Dimension(650, 300));
        setPreferredSize(new java.awt.Dimension(650, 300));
        jPanel1.setLayout(new java.awt.GridBagLayout());

        jPanel1.setMaximumSize(new java.awt.Dimension(80, 30));
        jPanel1.setMinimumSize(new java.awt.Dimension(80, 30));
        jPanel1.setPreferredSize(new java.awt.Dimension(80, 30));
        btnCancel.setFont(CoeusFontFactory.getLabelFont());
        btnCancel.setText("Cancel");
        jPanel1.add(btnCancel, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 5);
        add(jPanel1, gridBagConstraints);

        scrPnTrainingDetail.setBorder(javax.swing.BorderFactory.createTitledBorder("Training Details"));
        scrPnTrainingDetail.setMaximumSize(new java.awt.Dimension(570, 290));
        scrPnTrainingDetail.setMinimumSize(new java.awt.Dimension(570, 290));
        scrPnTrainingDetail.setPreferredSize(new java.awt.Dimension(570, 290));
        tblTrainingDetail.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        scrPnTrainingDetail.setViewportView(tblTrainingDetail);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        add(scrPnTrainingDetail, gridBagConstraints);

    }// </editor-fold>//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancel;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane scrPnTrainingDetail;
    private javax.swing.JTable tblTrainingDetail;
    // End of variables declaration//GEN-END:variables
    
}
