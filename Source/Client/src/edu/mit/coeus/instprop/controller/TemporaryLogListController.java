/*
 * TemporaryLogListController.java
 *
 * Created on May 11, 2004, 3:45 PM
 */

package edu.mit.coeus.instprop.controller;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;


import edu.mit.coeus.utils.*;
import edu.mit.coeus.gui.*;
import edu.mit.coeus.utils.query.*;
import edu.mit.coeus.gui.event.*;
import edu.mit.coeus.instprop.bean.*;
import edu.mit.coeus.instprop.gui.*;
import edu.mit.coeus.brokers.*;
import edu.mit.coeus.exception.CoeusUIException;

/**
 *
 * @author  ajaygm
 */
public class TemporaryLogListController extends InstituteProposalController
implements ActionListener{
    
    /** Holds an instance of <CODE>CommentsHistoryForm</CODE> */
    private TemporaryLogListForm temporaryLogListForm;
    
    //get an instance of mdi form
    private CoeusAppletMDIForm mdiForm = CoeusGuiConstants.getMDIForm() ;
    
    //dialog which contains this form
    private CoeusDlgWindow dlgTemporaryLogListController;
    
    //for query 
    private QueryEngine queryEngine;
    
    //for connecting to server 
    private static final String GET_SERVLET = "/InstituteProposalMaintenanceServlet";
    private static final String connect = CoeusGuiConstants.CONNECTION_URL + GET_SERVLET;
    
    //for saving the proposal
    private static final char SAVE_TEMP_PROPOSAL = 'L';
    
    //setting ther window title 
    private static final String WINDOW_TITLE = "Temporary Log List";
    
    //for setting dimentions 
    private static final int WIDTH = 625;
    private static final int HEIGHT =  310;
    
    //coeus vectors for saving the beans 
    private CoeusVector cvTemplogs;
    private CoeusVector cvTemplogBeans;
    private CoeusMessageResources coeusMessageResources;
    private TemporaryLogListTableModel temporaryLogListTableModel;
        
    //for setting data to the columns 
    private static final int PROPOSAL_NUMBER_INDEX = 0;
    private static final int PI_INDEX =1;
    private static final int SPONSOR_CODE_INDEX = 2;
    private static final int SPONSOR_NAME_INDEX = 3;
    private static final int TITLE_INDEX = 4;
    private static final int PROPOSAL_TYPE_INDEX = 5;
    private static final int LEAD_UNIT_INDEX = 6;
    private static final int COMMENTS_INDEX = 7;
    
    //Validation message 
    private static final String SELECT_LOG = "instPropLog_exceptionCode.1410";
  
    private static String  tempProposalNumber = null;
    
    private static final String EMPTY_STRING = "";
    
    //holds an instance of iplogbean
    private InstituteProposalLogBean ipLogBean = new InstituteProposalLogBean();
    private CoeusVector cvTempdata = new CoeusVector();
    

   /** Creates a new instance of CommentsHistoryController
     * @param CoeusVector,InstituteProposalLogBean.
     */
    public TemporaryLogListController(CoeusVector cvTemplogs, InstituteProposalLogBean ipLogBean) {
        queryEngine = QueryEngine.getInstance();
        this.cvTemplogs = cvTemplogs;
        this.ipLogBean = ipLogBean;
        coeusMessageResources = CoeusMessageResources.getInstance();
        registerComponents();
        setFormData(cvTemplogs);
        postInitComponents();   
        
    }
    
    /**
     * This method creates and sets the display attributes for the dialog
     */    
    public void postInitComponents(){
        dlgTemporaryLogListController = new CoeusDlgWindow(mdiForm);
        dlgTemporaryLogListController.setResizable(false);
        dlgTemporaryLogListController.setModal(true);
        dlgTemporaryLogListController.getContentPane().add(temporaryLogListForm);
        dlgTemporaryLogListController.setTitle(WINDOW_TITLE);
        dlgTemporaryLogListController.setFont(CoeusFontFactory.getLabelFont());
        dlgTemporaryLogListController.setSize(WIDTH, HEIGHT);
        
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension dlgSize = dlgTemporaryLogListController.getSize();
        dlgTemporaryLogListController.setLocation(screenSize.width/2 - (dlgSize.width/2),
        screenSize.height/2 - (dlgSize.height/2));
        
         dlgTemporaryLogListController.addComponentListener(
            new ComponentAdapter(){
                public void componentShown(ComponentEvent e){
                    requestDefaultFocus();
                }
        });
        
        dlgTemporaryLogListController.addEscapeKeyListener(new AbstractAction("escPressed"){
            public void actionPerformed(ActionEvent ae){
                    saveFormData();
            }
        });
       
        
        dlgTemporaryLogListController.addWindowListener(new WindowAdapter(){
             public void windowClosing(WindowEvent we){
               saveFormData();
             }
        });
     //code for disposing the window ends
    }
    
    /** Displays the Form which is being controlled.
     */
    public void display() {
        dlgTemporaryLogListController.setVisible(true);
    }
    
    /** Perform field formatting.
     * enabling, disabling components depending on the different conditions
     */
    public void formatFields() {
        
    }
    
    
    /** An overridden method of the controller
    * @return temporaryLogListForm returns the controlled form component
    */
    public Component getControlledUI() {
        return temporaryLogListForm;
    }
    
     /** Returns the form data
     * @return returns the form data
     */
    public Object getFormData() {
        return null;
    }
    
    /**
     * This method is used to set the listeners to the components.
     */    
    public void registerComponents() {
        temporaryLogListForm = new TemporaryLogListForm();
        
        temporaryLogListForm.btnOK.addActionListener(this);
        temporaryLogListForm.btnCancel.addActionListener(this);
        
        /** Code for focus traversal - start */
        java.awt.Component[] components = { temporaryLogListForm.btnCancel,
        temporaryLogListForm.btnOK
        };
        
        ScreenFocusTraversalPolicy traversePolicy = new ScreenFocusTraversalPolicy( components );
        temporaryLogListForm.setFocusTraversalPolicy(traversePolicy);
        temporaryLogListForm.setFocusCycleRoot(true);
        
        temporaryLogListTableModel = new TemporaryLogListTableModel();
        temporaryLogListForm.tblTempLog.setModel(temporaryLogListTableModel);
        setTableEditors();

    }
    
    /**
     * To set the default focus for the component
     */    
    public void requestDefaultFocus(){    
        temporaryLogListForm.btnCancel.requestFocus();
    }
    
    /**
     * For merging the Temp Log And Saving The Data
     */    
    public void mergeAndSaveFormData() throws CoeusUIException{
         RequesterBean requesterBean = new RequesterBean();
         ResponderBean responderBean = new ResponderBean();
         requesterBean.setFunctionType(SAVE_TEMP_PROPOSAL);
         ipLogBean.setAcType("I");
         cvTempdata.addElement(tempProposalNumber);
         cvTempdata.addElement(ipLogBean);
         requesterBean.setDataObject(cvTempdata);   
         
         AppletServletCommunicator comm = new AppletServletCommunicator(connect, requesterBean);
         comm.setRequest(requesterBean);
         comm.send();
         responderBean = comm.getResponse();
         
         if(responderBean.isSuccessfulResponse()) {
             ipLogBean = (InstituteProposalLogBean)responderBean.getDataObject();
             dlgTemporaryLogListController.dispose();
         }else{
             throw new CoeusUIException(responderBean.getMessage(), CoeusUIException.ERROR_MESSAGE);
         }
    }
    
    /** Saves the Form Data.
   */
    public void saveFormData() {
         RequesterBean requesterBean = new RequesterBean();
         ResponderBean responderBean = new ResponderBean();
         requesterBean.setFunctionType('I');
         ipLogBean.setAcType("I");
         requesterBean.setDataObject(ipLogBean);   
         
         AppletServletCommunicator comm = new AppletServletCommunicator(connect, requesterBean);
         comm.setRequest(requesterBean);
         comm.send();
         responderBean = comm.getResponse();
         
         if(responderBean.isSuccessfulResponse()) {
             ipLogBean = (InstituteProposalLogBean)responderBean.getDataObject();
             dlgTemporaryLogListController.dispose();
         }else{
            CoeusOptionPane.showErrorDialog(responderBean.getMessage());
            return ;
         }
    }    
    
    /** This method is used to set the form data specified in
     * <CODE> data </CODE>
     * @param data data to set to the form
     */
    public void setFormData(Object data) {
        cvTemplogs = (CoeusVector)data;
        temporaryLogListTableModel.setData(cvTemplogs);
        
    }
    
    /** Validate the form data/Form and returns true if
     * validation is through else returns false.
     * @throws CoeusUIException if some exception occurs or some validation fails.
     * @return true if
     * validation is through else returns false.
     */
    public boolean validate() throws edu.mit.coeus.exception.CoeusUIException {
        return true;
    }
    
     /** This method triggers all actions based on the event occured
     * @param actionEvent takes the actionEvent 
     */
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource ();
        try{
            if(source.equals(temporaryLogListForm.btnOK)){
                performUpdateOperation();
            }else if(source.equals(temporaryLogListForm.btnCancel)){
                saveFormData();
                //dlgTemporaryLogListController.dispose();
            }
        }catch(CoeusUIException coeusUIException){
             CoeusOptionPane.showDialog(coeusUIException);
             coeusUIException.printStackTrace();
        }
    }
    
  
    /**
     * Abstract Table model for displaying temp logs
     */    
    public class TemporaryLogListTableModel extends AbstractTableModel{
         //column names 
         private String ColumnName[] = {"Proposal Number","PI Name","Sponsor Code","Sponsor Name","Title",
         "Proposal Type","Lead Unit","Comments"};
         
         private Class colClass[] = {String.class,String.class,String.class,String.class,
         String.class,String.class,String.class,String.class};
         
         /**
          * Gets Column Count
          */         
         public int getColumnCount() {
             return ColumnName.length;
         }
         public Class getColumnClass(int columnIndex){
             return colClass[columnIndex];
         }
      
         
         /**
          * Gets Row Count
          */         
         public int getRowCount() {
             if (cvTemplogBeans == null){
                 return 0;
             }else 
                 return cvTemplogBeans.size();
         }
         
         /**
          * Sets Data
          */         
         public void setData(CoeusVector vecValue){
            cvTemplogBeans = vecValue;
         }
         
         /**
          * Gets The Column name
          */         
         public String getColumnName(int col) {
             return ColumnName[col];           
         }
            
         /**
          * Gets The Value At Specified Index
          */         
         public Object getValueAt(int rowIndex, int columnIndex) {
             InstituteProposalLogBean bean = (InstituteProposalLogBean)cvTemplogBeans.get(rowIndex);
             
             switch(columnIndex){
                 case PROPOSAL_NUMBER_INDEX:
                     return bean.getProposalNumber();
                 case PI_INDEX:
                     return bean.getPrincipleInvestigatorName();
                 case SPONSOR_CODE_INDEX:
                     return bean.getSponsorCode();
                 case SPONSOR_NAME_INDEX:
                     return bean.getSponsorName();
                 case TITLE_INDEX:
                     return bean.getTitle();
                 case PROPOSAL_TYPE_INDEX:
                     return bean.getProposalTypeDescription();
                 case LEAD_UNIT_INDEX:
                     return bean.getLeadUnit();
                 case COMMENTS_INDEX:
                     return bean.getComments();
             }
            return EMPTY_STRING;
         }
         
     }

    //Editor for the table 
     private void setTableEditors(){

            JTableHeader tableHeader = temporaryLogListForm.tblTempLog.getTableHeader();
            tableHeader.setReorderingAllowed(false);
            tableHeader.setFont(CoeusFontFactory.getLabelFont());
            temporaryLogListForm.tblTempLog.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
            temporaryLogListForm.tblTempLog.setRowHeight(22);
            temporaryLogListForm.tblTempLog.setSelectionBackground(new java.awt.Color(10,36,106));
            temporaryLogListForm.tblTempLog.setSelectionForeground(java.awt.Color.white);
            temporaryLogListForm.tblTempLog.setShowHorizontalLines(true);
            temporaryLogListForm.tblTempLog.setShowVerticalLines(true);
            temporaryLogListForm.tblTempLog.setOpaque(false);

            temporaryLogListForm.tblTempLog.setSelectionMode(
            DefaultListSelectionModel.SINGLE_SELECTION);
            TableColumn column;
            //int minWidth[] = {30, 45, 90, 90, 90, 90, 90, 90};
            int prefWidth[] = {110, 200, 100, 200, 300, 100, 90, 400};
            for(int index = 0; index < prefWidth.length; index++) {
                column = temporaryLogListForm.tblTempLog.getColumnModel().getColumn(index);
                column.setPreferredWidth(prefWidth[index]);
            }
     }
     
     /**
      * Updates Data To Server
      */     
     public void performUpdateOperation() throws CoeusUIException{        
         if (temporaryLogListForm.tblTempLog.getSelectedRow() == -1) {
         CoeusOptionPane.showInfoDialog(
         coeusMessageResources.parseMessageKey(SELECT_LOG));
        }else {
            int rowData = temporaryLogListForm.tblTempLog.getSelectedRow();
            InstituteProposalLogBean tempBean = new InstituteProposalLogBean();
            tempBean =(InstituteProposalLogBean) cvTemplogs.get(rowData);
            tempProposalNumber = tempBean.getProposalNumber();
            mergeAndSaveFormData();
        }
     }     
     
     /**
      * Getter for property ipLogBean.
      * @return Value of property ipLogBean.
      */
     public edu.mit.coeus.instprop.bean.InstituteProposalLogBean getIpLogBean() {
         return ipLogBean;
     }
     
     /**
      * Setter for property ipLogBean.
      * @param ipLogBean New value of property ipLogBean.
      */
     public void setIpLogBean(edu.mit.coeus.instprop.bean.InstituteProposalLogBean ipLogBean) {
         this.ipLogBean = ipLogBean;
     }     
}


