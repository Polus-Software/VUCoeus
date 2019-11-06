/*
 * ProtocolAssignmentFrom.java
 *
 * Created on November 21, 2002, 6:56 PM 
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.iacuc.gui;

import javax.swing.table.DefaultTableModel;
import java.util.*;
import javax.swing.JComponent;
import javax.swing.JTable;
import java.sql.Date;
import javax.swing.table.*;
import javax.swing.UIManager;
import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import edu.mit.coeus.exception.*;
import edu.mit.coeus.utils.*;
import edu.mit.coeus.iacuc.bean.*;
import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.gui.CoeusMessageResources;

/**
 *
 * This class is used for displaying the Protocol submission details for the 
 * Schedule selected. This is shown on Protocol Assignment tabpage of Schedule
 * Details screen. 
 * @author  Sagin
 */
public class ProtocolAssignmentForm extends JComponent {
   
    // Variables declaration - do not modify
    private char functionType;
    
    /* This is used to hold vector of data beans*/
    private Vector protoAssgmtFormData = null;

    // End of variables declaration
    // message resource instance
    CoeusMessageResources coeusMessageResources = null;        
    
    DefaultTableModel tblModel ; 
    HashMap hmData = new HashMap();        
    
    private int INDEX_COLUMN = 8;
    private int SUBMISSION_NUMBER_COLUMN = 7;
    /** Creates new form ProtocolAssignmentFrom */
    public ProtocolAssignmentForm() {
        coeusMessageResources = CoeusMessageResources.getInstance();
    }
    /**
     * Constructor which builds form fields and populates them with data
     * specified in ProtocolSubmissionInfoBean and sets the enabled status
     * for all components depending on the functionType specified.
     * @param ProtocolSubmissionInfoBean, a vector which consists of all the 
     * Protocol Submission details list for the Schedule.
     * @param functionType Character which specifies the mode in which the
     * form will be shown.
     * 'A' specifies that the form is in Add Mode
     * 'M' specifies that the form is in Modify Mode
     * 'D' specifies that the form is in Display Mode
     * Since this form is used only for display, the functionType passed should 
     * be always 'D'.
     */

    public ProtocolAssignmentForm(Vector protoAssgmtFormData) {
                    
        this.protoAssgmtFormData = protoAssgmtFormData;
        coeusMessageResources = CoeusMessageResources.getInstance();
    }
    
    //Constructor with FunctionType argument to get the set value.
    public ProtocolAssignmentForm(char functionType) {
    this.functionType=functionType;
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;
        javax.swing.JScrollPane scrPnProtoAssgnmt;

        scrPnProtoAssgnmt = new javax.swing.JScrollPane();
        tblProtoAssgnmt = new javax.swing.JTable();

        setLayout(new java.awt.GridBagLayout());

        scrPnProtoAssgnmt.setFont(CoeusFontFactory.getNormalFont());
        scrPnProtoAssgnmt.setMinimumSize(new java.awt.Dimension(980, 550));
        scrPnProtoAssgnmt.setPreferredSize(new java.awt.Dimension(980, 550));
        scrPnProtoAssgnmt.setAutoscrolls(true);
        tblProtoAssgnmt.setModel(new DefaultTableModel(new String[][]{},
            new String [] {"Protocol No", "Protocol Title", "Submission Type",
                "Sub. Type Qualifier", "Sub. Review Type",
                "Submission Status", "Submission Date","Submission Number","Index"}
        ){
            public boolean isCellEditable(int row,int col){
                return false;
            }
        });
        tblProtoAssgnmt.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tblProtoAssgnmt.setSelectionForeground(java.awt.Color.white);
        scrPnProtoAssgnmt.setViewportView(tblProtoAssgnmt);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.gridheight = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        add(scrPnProtoAssgnmt, gridBagConstraints);

    }//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable tblProtoAssgnmt;
    // End of variables declaration//GEN-END:variables


    //supporting method to set the Table Formats like showing hand icon for
    //selected/current record.
    private void setTableFormat(){
        
        JTable tblLocalProtoAssgnmt = this.tblProtoAssgnmt;
        if(functionType=='D')
        {
            this.tblProtoAssgnmt.setBackground((Color) UIManager.getDefaults().get("Panel.background"));
//            this.tblProtoAssgnmt.setSelectionBackground((Color) UIManager.getDefaults().get("Panel.background"));
//            this.tblProtoAssgnmt.setSelectionForeground(Color.black);
        }
        /*
         * Fix for: GNIR-I Enchancement Phase - II
         * Despcription : To Populate the Protocol Detail Window.
         * Updated by Subramanya 12th April 2003.
         */
        this.tblProtoAssgnmt.addMouseListener( new MouseAdapter(){
            public void mouseClicked( MouseEvent protoEvent ){
                if( protoEvent.getClickCount() == 2 ){
                    ProtocolSubmissionInfoBean protoBean = 
                                                getSelectedSubmissionDetails();
                    String prtNumber = null;
                    if( protoBean != null ){
                       prtNumber = protoBean.getProtocolNumber();
                       if( prtNumber  != null ){
                           try{
                            new ProtocolDetailForm( 
                                 CoeusGuiConstants.DISPLAY_MODE,
                                 prtNumber, 
                                 CoeusGuiConstants.getMDIForm() ).
                                                            showDialogForm();
                           }catch( CoeusException coe ) {
                                CoeusOptionPane.showDialog(new CoeusClientException(coe));
                           }catch ( Exception showErr ){
                               showErr.printStackTrace();
                                CoeusOptionPane.showErrorDialog(
                                    coeusMessageResources.parseMessageKey(
                                    "protoAssignFrm_exceptionCode.2215"));                                
                           } 
                       }
                    }
                }
            }
        });
        tblLocalProtoAssgnmt.setRowHeight(22);
        JTableHeader header = tblLocalProtoAssgnmt.getTableHeader();
        header.setReorderingAllowed(false);
        /* commented by ravi on 27-03-2003 for fix id: 194 */
        //header.setResizingAllowed(false);
        
        
        TableSorter sorter = new TableSorter(
            (DefaultTableModel)tblProtoAssgnmt.getModel(),false);
        tblProtoAssgnmt.setModel(sorter);
        sorter.addMouseListenerToHeaderInTable(tblProtoAssgnmt); //ADDED THIS

        //Set column size for Index
        TableColumn clmName = tblProtoAssgnmt.getColumn("Index");
        clmName.setMaxWidth(0);
        clmName.setMinWidth(0);
        clmName.setPreferredWidth(0);
        
        clmName = tblProtoAssgnmt.getColumn("Submission Number");
        clmName.setMaxWidth(0);
        clmName.setMinWidth(0);
        clmName.setPreferredWidth(0);
        
        clmName = tblProtoAssgnmt.getColumn("Protocol No");
        clmName.setMinWidth(100);

        clmName = tblProtoAssgnmt.getColumn("Protocol Title");
        clmName.setMinWidth(140);
        
        clmName = tblProtoAssgnmt.getColumn("Submission Type");
        clmName.setMinWidth(190);

        clmName = tblProtoAssgnmt.getColumn("Sub. Type Qualifier");
        clmName.setMinWidth(190);
        
        clmName = tblProtoAssgnmt.getColumn("Sub. Review Type");
        clmName.setMinWidth(120);
        
        clmName = tblProtoAssgnmt.getColumn("Submission Status");
        clmName.setMinWidth(120);
        
        clmName = tblProtoAssgnmt.getColumn("Submission Date");
        clmName.setMinWidth(100);
        /*
         *"Protocol No", "Protocol Title", "Submission Type",
                "Sub. Type Qualifier", "Sub. Review Type",
                "Submission Status", "Submission Date"
         */        
    }
    
    //Added by Amit 11/21/2003
    /** This method use to implement focus on first editable component in this page.
     */
    public void setDefaultFocusForComponent(){       
            if(tblProtoAssgnmt.getRowCount() > 0 ) {
                int rowNum = tblProtoAssgnmt.getSelectedRow();
                tblProtoAssgnmt.requestFocusInWindow();
                if(rowNum > 0){                    
                    tblProtoAssgnmt.setRowSelectionInterval(rowNum,rowNum);
                }
                tblProtoAssgnmt.setColumnSelectionInterval(0,0);       
        }
    }    
    //end Amit     
    
    /**
     * Method to set the Form data.
     * @param Vector of bean data.
     */
    
    public void setProtocolAssignmentFormData(Vector protocolAssgmtFormData){
        
        this.protoAssgmtFormData = protocolAssgmtFormData;
    }

    /**
     * This method is called from the ScheduleDetailForm. The Form is 
     * constructed with the data avalilable and returned
     * @param JComponent ProtocolAssignmentForm
     */
    
    public JComponent showProtocolAssignmentForm(){
         
        initComponents();
        setFormData();
        setTableFormat();
        JTable tblLocalProtoAssgnmt = this.tblProtoAssgnmt;
        if( tblLocalProtoAssgnmt!=null && tblLocalProtoAssgnmt.getRowCount() > 0 ){
            tblLocalProtoAssgnmt.setRowSelectionInterval(0,0);
        }
        
        //setting font for records
        tblLocalProtoAssgnmt.setFont(CoeusFontFactory.getNormalFont());
        
        // setting bold property for table header values
        tblLocalProtoAssgnmt.getTableHeader().
                        setFont(CoeusFontFactory.getLabelFont());
        
        return this;
    }

    /* Method to set the data to the JTable*/
    private void setFormData() {
        Vector vcDataPopulate = new Vector();
        Vector vcData=null;
        
        /* Used for Date formatting */
        DateUtils dtUtils = new DateUtils();

        String protocolNo, protocolTitle, submissionTypeDesc, 
            submissionTypeQualDesc, submissionReviewTypeDesc, 
            submissionDate, sequenceNumber, submissionNumber, submissionStatusDesc = "";   //prps added sequenceNumber,

        /* This bean will hold all the field values for a record */
        ProtocolSubmissionInfoBean protocolSubmissionInfoBean;
        int maxSize = 0;
        Vector localprotoAsgnmtData = protoAssgmtFormData;
        JTable tblLocalProtoAssgnmt = this.tblProtoAssgnmt;
        
        if(localprotoAsgnmtData!= null) {
            maxSize = localprotoAsgnmtData.size();
        }
        
        if((localprotoAsgnmtData!= null) &&(maxSize>0)) {
            for(int recordCount=0; recordCount < maxSize; recordCount++) {
                protocolSubmissionInfoBean=(ProtocolSubmissionInfoBean)
                        localprotoAsgnmtData.get(recordCount);
                
                protocolNo = protocolSubmissionInfoBean.getProtocolNumber();
                protocolTitle = protocolSubmissionInfoBean.getTitle();
                submissionTypeDesc = 
                    protocolSubmissionInfoBean.getSubmissionTypeDesc();
                submissionTypeQualDesc = 
                    protocolSubmissionInfoBean.getSubmissionQualTypeDesc();
                submissionReviewTypeDesc = 
                    protocolSubmissionInfoBean.getProtocolReviewTypeDesc();
                submissionStatusDesc = 
                    protocolSubmissionInfoBean.getSubmissionStatusDesc();
                submissionDate = dtUtils.formatDate(
                    protocolSubmissionInfoBean.getSubmissionDate().
                        toString(),"dd-MMM-yyyy");

                 //prps start
               submissionNumber = String.valueOf(protocolSubmissionInfoBean.getSubmissionNumber()) ;
                //prps end
                
                vcData= new Vector();
/*                vcData.add( protocolNo.equals(null) ? "" : protocolNo );
                vcData.add( protocolTitle.equals(null) ? "" : protocolTitle );
                vcData.add( submissionTypeDesc.equals(null) ? "" : submissionTypeDesc );
                vcData.add( submissionTypeQualDesc.equals(null) ? "" : 
                    submissionTypeQualDesc );
                vcData.add( submissionReviewTypeDesc.equals(null) ? "" : 
                    submissionReviewTypeDesc );
                vcData.add( submissionStatusDesc.equals(null) ? "" : 
                    submissionStatusDesc ); */
                vcData.add( protocolNo == null ? "" : protocolNo );
                vcData.add( protocolTitle == null ? "" : protocolTitle );
                vcData.add( submissionTypeDesc == null ? "" : submissionTypeDesc );
                vcData.add( submissionTypeQualDesc == null ? "" : 
                    submissionTypeQualDesc );
                vcData.add( submissionReviewTypeDesc == null ? "" : 
                    submissionReviewTypeDesc );
                vcData.add( submissionStatusDesc == null ? "" : 
                    submissionStatusDesc ); 
                
                vcData.add( submissionDate );
                vcData.add(""+submissionNumber);
                vcData.add(""+recordCount);
                hmData.put(""+recordCount, protocolSubmissionInfoBean);
                vcDataPopulate.add(vcData);
                
                
            }
            ((DefaultTableModel)tblLocalProtoAssgnmt.getModel()).
                setDataVector(vcDataPopulate,getColumnNames());
            
            if( tblLocalProtoAssgnmt.getRowCount() > 0 ){
                 tblLocalProtoAssgnmt.getSelectionModel().setSelectionInterval(1, 
                    tblLocalProtoAssgnmt.getColumnCount() );
                tblLocalProtoAssgnmt.setRowSelectionInterval(0,0); 
                
            }
            
        }
        
    }

    /* Method to get all the column names of JTable*/
    private Vector getColumnNames(){
        
        Enumeration enumColNames = tblProtoAssgnmt.getColumnModel().getColumns();
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
     * This method is used to get the Protocol Submission bean  of the selected table row.
     *
     * @return ProtocolSubmissionInfoBean which consists of the submission details.
     */
    public ProtocolSubmissionInfoBean getSelectedSubmissionDetails(){
        int selRow = tblProtoAssgnmt.getSelectedRow();
        if(selRow != -1){
            if(protoAssgmtFormData != null && protoAssgmtFormData.size() > selRow ){
                int indexValue = Integer.parseInt(
                    (String)tblProtoAssgnmt.getValueAt(selRow, INDEX_COLUMN ));
                return (ProtocolSubmissionInfoBean)hmData.get( ""+indexValue );
            }
        }
        return null;
    }
    
    /**
     * This method is used to get the Protocol Submission bean  for the given table row.
     *
     * @return ProtocolSubmissionInfoBean which consists of the submission details.
     */
    public ProtocolSubmissionInfoBean getSubmissionDetails(int rowIndex){
        if(rowIndex != -1){
            if(protoAssgmtFormData != null && protoAssgmtFormData.size() > rowIndex ){
                int indexValue = Integer.parseInt(
                    (String)tblProtoAssgnmt.getValueAt(rowIndex, INDEX_COLUMN ));
                return (ProtocolSubmissionInfoBean)hmData.get( ""+indexValue );
            }
        }
        return null;
    }
    public void updateActionStatus( ProtocolActionChangesBean actionChangeBean ,int selRow) {
//        int selRow = tblProtoAssgnmt.getSelectedRow();
        if(selRow != -1 && actionChangeBean != null ){
            int indexValue = Integer.parseInt((String)tblProtoAssgnmt.getValueAt(
                                selRow, INDEX_COLUMN ));
            ProtocolSubmissionInfoBean submissionBean = 
                    (ProtocolSubmissionInfoBean)hmData.get(""+indexValue);
            
            submissionBean.setProtocolNumber(actionChangeBean.getProtocolNumber());
            submissionBean.setSequenceNumber(actionChangeBean.getSequenceNumber());
            submissionBean.setSubmissionNumber(actionChangeBean.getSubmissionNumber());
            submissionBean.setSubmissionStatusCode(actionChangeBean.getSubmissionStatusCode());
            submissionBean.setSubmissionStatusDesc(actionChangeBean.getSubmissionStatusDescription());
            submissionBean.setSubmissionTypeCode(actionChangeBean.getSubmissionTypeCode());
            submissionBean.setSubmissionTypeDesc(actionChangeBean.getSubmissionTypeDescription());
            hmData.put(""+indexValue, submissionBean);

            ((DefaultTableModel)tblProtoAssgnmt.getModel()).setValueAt(
            actionChangeBean.getProtocolNumber(), indexValue, 0);

            ((DefaultTableModel)tblProtoAssgnmt.getModel()).setValueAt(
            actionChangeBean.getSubmissionTypeDescription(), indexValue, 2);

            ((DefaultTableModel)tblProtoAssgnmt.getModel()).setValueAt(
            actionChangeBean.getSubmissionStatusDescription(), indexValue, 5);

            ((DefaultTableModel)tblProtoAssgnmt.getModel()).setValueAt(
            ""+actionChangeBean.getSubmissionNumber(), indexValue, SUBMISSION_NUMBER_COLUMN);
            
        }
    }
    
}
