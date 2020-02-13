/** Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */
package edu.mit.coeus.award.controller;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;
import java.text.ParseException;

import edu.mit.coeus.gui.*;
import edu.mit.coeus.brokers.*;
import edu.mit.coeus.bean.*;
import edu.mit.coeus.award.gui.*;
import edu.mit.coeus.award.bean.*;
import edu.mit.coeus.utils.*;
import edu.mit.coeus.utils.query.*;
import edu.mit.coeus.exception.*;
import edu.mit.coeus.search.gui.CoeusSearch;
import javax.swing.plaf.basic.BasicComboBoxEditor;
import edu.ucsd.coeus.personalization.controller.AbstractController;


/*
 * ApprForeignTripController.java
 * @author  ajaygm
 * Created on May 27, 2004, 10:10 AM
 */

public class ApprForeignTripController extends AwardController implements ActionListener{
    
    /** Holds an instance of <CODE>ApprForeignTripForm</CODE> */
    private ApprForeignTripForm apprForeignTripForm;
    
    /**
     * To create an instance of MDIform
     */  
    private CoeusAppletMDIForm mdiForm = CoeusGuiConstants.getMDIForm();
    
    /**
     * Instance of the Dialog
     */   
    private CoeusDlgWindow dlgApprForeignTripForm;
    
    /**
     * Instance of Coeus Message Resources
     */    
    private CoeusMessageResources coeusMessageResources;
    
    /**
     * Instance of Query Engine
     */    
    private QueryEngine queryEngine;
    
    /*CoeusVector For setting deq no and award no*/
    private CoeusVector cvAwardDetailsBean;
    
    /*CoeusVectors of award base bean */
    private CoeusVector cvAwardBaseBean;
    
    /*CoeusVectors of approved foreign trip bean */
    private CoeusVector cvApprvdForeignTrpBean;
    
    /*CoeusVectors of deleted beans */
    private CoeusVector cvDeletedItem;
    
    /*CoeusVectors of Investigator bean */
    private CoeusVector cvInvestgator;
    
    /*Holds the persons in the combox*/
    private CoeusVector cvPersons;
    
    /*Table Model, editor and Renderer for Foreign trip table and total table*/
    private ApprForeignTripTableModel apprForeignTripTableModel;
    private ApprForeignTripEditor apprForeignTripEditor;
    private ApprForeignTripRenderer apprForeignTripRenderer;
    private AmountTabelModel amountTabelModel;
    private AmountTableCellRenderer amountTableCellRenderer;
    
    /*Holds the initial id for invalid persons.*/
    private int initInvalidPerId = -1;
    
    /*Creating the instance of awardDetailsBean*/
    private AwardDetailsBean awardDetailsBean = new AwardDetailsBean();
    
    /*Creating the instance of Date utils*/
    private DateUtils dtUtils = new DateUtils();
    
    /*COmbobox for the person column*/
    private CoeusComboBox cmbPerson = new CoeusComboBox();
    
    /** Editor component for combo box.
     */
    private final CoeusTextField txtComboEditor;
    
    /*Holds the persons info*/
    private ComboBoxBean comboBoxBean ;
    
    /*Model for the person combobox*/ 
    private DefaultComboBoxModel comboBoxModel;
    
    /*Instance of Approved Foreign Trip Bean*/
    private AwardApprovedForeignTripBean awardApprovedForeignTripBean;
    
    /*For assigning row ID*/
    private int rowId;
    
    private static final String EMPTY_STRING = "";
    
    /*Validation messages*/
    private static final String DELETE_CONFIRMATION = "instPropIPReview_exceptionCode.1353";
    private static final String DUPLICATE_ROW = "awardCostsharing_exceptionCode.1607";
    private static final String SAVE_CHANGES = "saveConfirmCode.1002";
    private static final String PERSON_VALIDATE = "approvedForeignTripControllerCode.1851";
    private static final String DESTINATION_VALIDATE = "approvedForeignTripControllerCode.1852";
    private static final String DATE_FROM_VALIDATE = "approvedForeignTripControllerCode.1853";
    private static final String DATE_TO_VALIDATE = "approvedForeignTripControllerCode.1854";

    /*For formating the date*/
    private static final String DATE_FORMAT = "dd-MMM-yyyy";
    private static final String DATE_SEPARATERS = ":/.,|-";
    private static final String SIMPLE_DATE_FORMAT = "MM/dd/yyyy";
    private java.text.SimpleDateFormat dtFormat = 
            new java.text.SimpleDateFormat("MM/dd/yyyy");
    
    /*TO check weather the data is modified*/
    private boolean modified = false;
    
    /*Column indexes for the table*/
    private static final int PERSON_INDEX = 0;
    private static final int DESTINATION_INDEX = 1;
    private static final int DATE_FROM_INDEX  = 2;
    private static final int DATE_TO_INDEX = 3;
    private static final int AMOUNT_INDEX = 4;
    private static final int TOTAL_INDEX=0;
    private static final int TOTAL_AMOUNT_INDEX=1;

    /*For setting the dimentions*/
    private static final String WINDOW_TITLE = "Approved Foreign Trip";
    private static final int WIDTH = 685;
    private static final int HEIGHT = 325;
    
    private java.awt.Color disabledBackground = (java.awt.Color) javax.swing.UIManager.
    getDefaults().get("Panel.background");
    
   public ApprForeignTripController(AwardBaseBean awardBaseBean, char funcType){
        super(awardBaseBean);
        queryEngine = QueryEngine.getInstance ();
        coeusMessageResources = CoeusMessageResources.getInstance();
        awardApprovedForeignTripBean = new AwardApprovedForeignTripBean ();
        txtComboEditor = new CoeusTextField ();
        registerComponents();
        setFormData(awardBaseBean);
        postInitComponents();
        setFunctionType(funcType);
        cmbPerson.setEditable(true);
        
        
        /*Performs the OK clicked action if enter is pressed*/
        txtComboEditor.addKeyListener (new KeyAdapter (){
            public void keyPressed (KeyEvent kEvent){
                if( kEvent.getKeyCode () == KeyEvent.VK_ENTER){
                    apprForeignTripEditor.stopCellEditing ();
                    apprForeignTripForm.btnOk.doClick ();
                    kEvent.consume ();
                }
            }
        });

        /*Editor for the combobox*/
        cmbPerson.setEditor ( new BasicComboBoxEditor (){
            public Component getEditorComponent (){
                return txtComboEditor;
            }
            public void setItem (Object object) {
                if(!object.toString ().equals (EMPTY)) {
                    txtComboEditor.setText (object.toString ());
                }
            }

            public Object getItem () {
                return txtComboEditor.getText ();
            }
        });
   }
  
    /** This method creates and sets the display attributes for the dialog
     */
    public void postInitComponents(){
        dlgApprForeignTripForm = new CoeusDlgWindow(mdiForm);
        dlgApprForeignTripForm.setResizable(false);
        dlgApprForeignTripForm.setModal(true);
        dlgApprForeignTripForm.getContentPane().add(apprForeignTripForm);
        dlgApprForeignTripForm.setTitle(WINDOW_TITLE);
        dlgApprForeignTripForm.setFont(CoeusFontFactory.getLabelFont());
        dlgApprForeignTripForm.setSize(WIDTH, HEIGHT);
        
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension dlgSize = dlgApprForeignTripForm.getSize();
        dlgApprForeignTripForm.setLocation(screenSize.width/2 - (dlgSize.width/2),
        screenSize.height/2 - (dlgSize.height/2));
        
         dlgApprForeignTripForm.addComponentListener(
            new ComponentAdapter(){
                public void componentShown(ComponentEvent e){
                    requestDefaultFocus();
                }
        });
        
        //code for disposing the window 
        dlgApprForeignTripForm.addEscapeKeyListener(new AbstractAction("escPressed"){
            public void actionPerformed(ActionEvent ae){
                    performCancelAction();
            }
        });
       
        
        dlgApprForeignTripForm.setDefaultCloseOperation(CoeusDlgWindow.DO_NOTHING_ON_CLOSE);
        dlgApprForeignTripForm.addWindowListener(new WindowAdapter(){
             public void windowOpening(WindowEvent we){
                 apprForeignTripForm.tblApprovedForeignTrip.setRowSelectionInterval(0,0);
                 apprForeignTripForm.tblApprovedForeignTrip.editCellAt(0,0);
             }
             public void windowClosing(WindowEvent we){
                    performCancelAction();
             }
        });
        //code for disposing the window ends
    }//end of postInitComponents
   
    /** Displays the Form which is being controlled.
    */
   public void display() {
   	//    	rdias - UCSD's coeus personalization - Begin
	   AbstractController persnref = AbstractController.getPersonalizationControllerRef();
       persnref.customize_Form(getControlledUI(),"GENERIC");
       persnref.customize_Form(apprForeignTripForm.awardHeaderForm,"GENERIC");
       //		rdias - UCSD's coeus personalization - End	   
       if(getFunctionType() != TypeConstants.DISPLAY_MODE){
           if(apprForeignTripForm.tblApprovedForeignTrip.getRowCount ()>0){
                apprForeignTripForm.tblApprovedForeignTrip.setRowSelectionInterval (0,0);
            }
       }
       dlgApprForeignTripForm.setVisible (true);
   }
   
   /** Perform field formatting.
    * enabling, disabling components depending on the different conditions
    */
   public void formatFields() {
       if(getFunctionType() == TypeConstants.DISPLAY_MODE){
            apprForeignTripForm.btnAdd.setEnabled(false);
            apprForeignTripForm.btnDelete.setEnabled(false);
            apprForeignTripForm.btnOk.setEnabled(false);
            apprForeignTripForm.btnFindPerson.setEnabled(false);
            apprForeignTripForm.tblApprovedForeignTrip.setEnabled(false);
        }
   }
   
   /** An overridden method of the controller
   * @return apprEquipmentForm returns the controlled form component
   */
   public Component getControlledUI() {
       return apprForeignTripForm;
   }
   
  /** Returns the form data
    * @return returns the form data
    */
   public Object getFormData() {
       return null;
   }
   
  /** This method is used to set the listeners to the components.
   */
   public void registerComponents() {
       //Add listeners to all the buttons
        apprForeignTripForm = new ApprForeignTripForm();
        apprForeignTripForm.btnOk.addActionListener(this);
        apprForeignTripForm.btnCancel.addActionListener(this);
        apprForeignTripForm.btnAdd.addActionListener(this);
        apprForeignTripForm.btnDelete.addActionListener (this);
        apprForeignTripForm.btnFindPerson.addActionListener(this);
        
        /** Code for focus traversal - start */
        java.awt.Component[] components = { txtComboEditor,apprForeignTripForm.tblApprovedForeignTrip,
        apprForeignTripForm.btnOk,apprForeignTripForm.btnCancel,apprForeignTripForm.btnAdd,
        apprForeignTripForm.btnDelete,apprForeignTripForm.btnFindPerson
        };
        
        ScreenFocusTraversalPolicy traversePolicy = new ScreenFocusTraversalPolicy( components );
        apprForeignTripForm.setFocusTraversalPolicy(traversePolicy);
        apprForeignTripForm.setFocusCycleRoot(true);
         
        /** Code for focus traversal - end */
        apprForeignTripEditor = new ApprForeignTripEditor();
        apprForeignTripRenderer = new ApprForeignTripRenderer();
        apprForeignTripTableModel = new ApprForeignTripTableModel();
        apprForeignTripForm.tblApprovedForeignTrip.setModel (apprForeignTripTableModel);
        
        amountTabelModel = new AmountTabelModel();
        amountTableCellRenderer = new AmountTableCellRenderer();
        apprForeignTripForm.tblApprovedForeignTripTotal.setModel(amountTabelModel);
        
        //setting as the default button when enter is pressed.
        apprForeignTripForm.tblApprovedForeignTrip.addKeyListener(new KeyAdapter(){
            public void keyPressed(KeyEvent kEvent){
                if( kEvent.getKeyCode() == KeyEvent.VK_ENTER &&
                        kEvent.getSource() instanceof JTable ){
                            apprForeignTripEditor.stopCellEditing();
                            apprForeignTripForm.btnOk.doClick();
                            apprForeignTripForm.tblApprovedForeignTrip.requestFocusInWindow();
                            kEvent.consume();
                }
            }
        });
        
        
        setTableEditors();
   }
   
   /** To set the default focus for the component
    */
   public void requestDefaultFocus(){ 
       if(getFunctionType()!= DISPLAY_MODE){
           if(apprForeignTripForm.tblApprovedForeignTrip.getRowCount ()>0){
               apprForeignTripForm.tblApprovedForeignTrip.setRowSelectionInterval (0,0);
               apprForeignTripForm.tblApprovedForeignTrip.setColumnSelectionInterval (0,0);
               apprForeignTripForm.tblApprovedForeignTrip.editCellAt (0,0);
               cmbPerson.getEditor().selectAll ();
               apprForeignTripForm.btnAdd.requestFocus();
           }else{
                apprForeignTripForm.btnAdd.requestFocus();
            }
       }else{
           apprForeignTripForm.btnCancel.requestFocusInWindow();
       }
   }
    
   /** Saves the Form Data.
    */
   public void saveFormData() {
       apprForeignTripEditor.stopCellEditing();
        try{
            CoeusVector cvTemp = new CoeusVector();
            if(modified){
                if(cvDeletedItem!= null && cvDeletedItem.size() >0){
                    cvTemp.addAll(cvDeletedItem);
                }
                
                if(cvApprvdForeignTrpBean!= null && cvApprvdForeignTrpBean.size() >0){
                    cvTemp.addAll(cvApprvdForeignTrpBean);
                }
                
                if(cvTemp!=null){
                    for(int index = 0; index < cvTemp.size(); index++){
                        AwardApprovedForeignTripBean bean = (AwardApprovedForeignTripBean)cvTemp.get(index);
                        if(bean.getAcType()!= null){
                            if(bean.getAcType().equals(TypeConstants.UPDATE_RECORD)){
                                bean.setAcType(TypeConstants.DELETE_RECORD);
                                queryEngine.delete(queryKey, bean);
                                bean.setAcType(TypeConstants.INSERT_RECORD);
                                rowId = rowId + 1;
                                bean.setRowId(rowId);
                                queryEngine.insert(queryKey, bean);
                             }else if(bean.getAcType().equals(TypeConstants.DELETE_RECORD)){
                                bean.setAcType(TypeConstants.DELETE_RECORD);
                                queryEngine.delete(queryKey, bean);
                            }else if(bean.getAcType().equals(TypeConstants.INSERT_RECORD)){
                                bean.setAcType(TypeConstants.INSERT_RECORD);
                                if(bean.getRowId () == 0){
                                    rowId = rowId + 1;
                                    bean.setRowId(rowId);
                                }
                                queryEngine.insert(queryKey, bean);
                            }
                        }
                    }
                }
            }
        }catch (CoeusException coeusException){
            coeusException.printStackTrace();
        }
       dlgApprForeignTripForm.dispose();
   }//end of saveFormData

   
   /** This method is used to set the form data specified in
     * <CODE> data </CODE>
     * @param data data to set to the form
     */
   public void setFormData(Object data) {
       modified = false;
       AwardBaseBean awardBaseBean= (AwardBaseBean)data;
       
        /*Create an instance of Coeus Vector to get
         *the Sponsor Award Number ,Award Number*/
        cvAwardDetailsBean = new CoeusVector();   
        /*Holds award base bean*/
        cvAwardBaseBean = new CoeusVector();
        
        /*Holds apprv foreign trip bean*/
        cvApprvdForeignTrpBean = new CoeusVector();
        /*Holds deleted bean*/
        cvDeletedItem = new CoeusVector();
        
        /*Holds investigator bean*/
        cvInvestgator = new CoeusVector();
        
        try{
            
            /*For getting Award Details Bean to set Award No, Seq No, Sponsor No*/
            cvAwardDetailsBean = queryEngine.executeQuery (
            queryKey,AwardDetailsBean.class, CoeusVector.FILTER_ACTIVE_BEANS);
            
            /*For getting Approved Equipments Bean to setting data to table*/
            cvApprvdForeignTrpBean = queryEngine.executeQuery (
            queryKey,AwardApprovedForeignTripBean.class, CoeusVector.FILTER_ACTIVE_BEANS);
            if(cvApprvdForeignTrpBean != null && cvApprvdForeignTrpBean.size() > 0){
                cvApprvdForeignTrpBean.sort ("rowId",false);
                AwardApprovedForeignTripBean foreignTripBean = (AwardApprovedForeignTripBean)cvApprvdForeignTrpBean.get (0);
                rowId = foreignTripBean.getRowId ();
            }
            
            setInitialIdForInvalidPersons(cvApprvdForeignTrpBean);
            
            cvInvestgator = queryEngine.executeQuery(
            queryKey,AwardInvestigatorsBean.class, CoeusVector.FILTER_ACTIVE_BEANS);
            
            apprForeignTripEditor.populateCombo(); 
            
        }catch (CoeusException coeusException){
            coeusException.printStackTrace ();
        }

        awardDetailsBean = (AwardDetailsBean)cvAwardDetailsBean.get(0);
        apprForeignTripForm.awardHeaderForm.setFormData (awardDetailsBean);
        //Case #2336 start
        apprForeignTripForm.awardHeaderForm.lblSequenceNumberValue.setText(EMPTY_STRING+awardBaseBean.getSequenceNumber());
        //Case #2336 end
        
        apprForeignTripTableModel.setData (cvApprvdForeignTrpBean);
        amountTabelModel.setData(cvApprvdForeignTrpBean);
        amountTabelModel.fireTableDataChanged();

        if(apprForeignTripForm.tblApprovedForeignTrip.getRowCount () == 0){
            apprForeignTripForm.tblApprovedForeignTripTotal.setVisible (false);
            apprForeignTripForm.btnFindPerson.setEnabled (false);
        }else{
            apprForeignTripForm.tblApprovedForeignTripTotal.setVisible (true);
            if(getFunctionType() == TypeConstants.DISPLAY_MODE){
                apprForeignTripForm.btnFindPerson.setEnabled (false);
            }else{
                apprForeignTripForm.btnFindPerson.setEnabled (true);
            }
        }
   }
   
   //sets the initial Id for invalid persons.
   //if no invalid persons found will set throws id to -1
   //else will set the least of negetive as initial invalid person Id
   private void setInitialIdForInvalidPersons(CoeusVector cvTripBean) {
       int perId;
       AwardApprovedForeignTripBean tripBean;
       for(int index = 0; index < cvTripBean.size(); index++){
           tripBean = (AwardApprovedForeignTripBean)cvTripBean.get(index);
           try{
                perId = Integer.parseInt(tripBean.getPersonId());
                if(perId < 0 && perId < initInvalidPerId) {
                    initInvalidPerId = perId;
                }
           }catch (NumberFormatException numberFormatException) {
               //Should never occur. still in case.
               numberFormatException.printStackTrace();
           }
       }//end for
   }//end setInitialIdForInvalidPersons
   
   /** Validate the form data/Form and returns true if
     * validation is through else returns false.
     * @throws CoeusUIException if some exception occurs or some validation fails.
     * @return true if
     * validation is through else returns false.
     */
   public boolean validate() throws CoeusUIException {
       apprForeignTripEditor.stopCellEditing();

       for(int index=0 ; index < cvApprvdForeignTrpBean.size() ; index++){
           AwardApprovedForeignTripBean foreignTripBean = (AwardApprovedForeignTripBean)cvApprvdForeignTrpBean.elementAt(index);
           if(foreignTripBean.getPersonName() == null || EMPTY_STRING.equals(foreignTripBean.getPersonName())){
               CoeusOptionPane.showInfoDialog(
               coeusMessageResources.parseMessageKey(PERSON_VALIDATE));
               apprForeignTripForm.tblApprovedForeignTrip.setRowSelectionInterval(index,index);
               apprForeignTripForm.tblApprovedForeignTrip.setColumnSelectionInterval(0,0);
               setRequestFocusInThread(txtComboEditor);
               apprForeignTripForm.tblApprovedForeignTrip.editCellAt(index,PERSON_INDEX);
               return false;
           }
           
           if(foreignTripBean.getDestination() == null || EMPTY_STRING.equals(foreignTripBean.getDestination())) {
               CoeusOptionPane.showInfoDialog(
               coeusMessageResources.parseMessageKey(DESTINATION_VALIDATE));
               apprForeignTripForm.tblApprovedForeignTrip.setRowSelectionInterval(index,index);
               apprForeignTripForm.tblApprovedForeignTrip.setColumnSelectionInterval(0,0);
               setRequestFocusInThread(apprForeignTripEditor.txtComponent);
               apprForeignTripForm.tblApprovedForeignTrip.editCellAt(index,DESTINATION_INDEX);
               return false;
           }
           
           if(foreignTripBean.getDateFrom() == null || EMPTY_STRING.equals(foreignTripBean.getDateFrom().toString().trim())) {
               CoeusOptionPane.showInfoDialog(
               coeusMessageResources.parseMessageKey(DATE_FROM_VALIDATE));
               apprForeignTripForm.tblApprovedForeignTrip.setRowSelectionInterval(index,index);
               setRequestFocusInThread(apprForeignTripEditor.txtDate);
               apprForeignTripForm.tblApprovedForeignTrip.editCellAt(index,DATE_FROM_INDEX);
               return false;
           }else{
               Date dateFrom = null;
               Date dateTo = null;
               AwardApprovedForeignTripBean  apprTripBean = (AwardApprovedForeignTripBean)cvApprvdForeignTrpBean.get(index);
               dateFrom = apprTripBean.getDateFrom();
               dateTo = apprTripBean.getDateTo();
               
               if(dateFrom != null && dateTo != null){
                   if(dateFrom.after(dateTo) || dateTo.before(dateFrom)){
                       String mesg =coeusMessageResources.parseMessageKey(DATE_TO_VALIDATE);
                       CoeusOptionPane.showInfoDialog(mesg);
                       apprForeignTripForm.tblApprovedForeignTrip.setRowSelectionInterval(index,index);
                       setRequestFocusInThread(apprForeignTripEditor.txtDate);
                       apprForeignTripForm.tblApprovedForeignTrip.editCellAt(index, DATE_FROM_INDEX);
                       
                       return false ;
                   }
               }
           }
       }
       boolean duplicate = checkDuplicateRow();
       return !duplicate;
   }
   
   private boolean checkDuplicateRow(){
        apprForeignTripEditor.stopCellEditing();
        Equals personEq,destEq,dateFrmEq;
        CoeusVector coeusVector = new CoeusVector();
        
        And personEqAnddestEq,frgnTripEquals;
        
        if(cvApprvdForeignTrpBean!=null && cvApprvdForeignTrpBean.size() > 0){
            for(int index = 0; index < cvApprvdForeignTrpBean.size(); index++){
                AwardApprovedForeignTripBean awardApprovedForeignTripBean = 
                        (AwardApprovedForeignTripBean)cvApprvdForeignTrpBean.get(index);
                
                personEq = new Equals("personName", awardApprovedForeignTripBean.getPersonName());
                destEq = new Equals("destination", awardApprovedForeignTripBean.getDestination());
                dateFrmEq = new Equals("dateFrom", awardApprovedForeignTripBean.getDateFrom());
                
                personEqAnddestEq = new And(personEq, destEq);
                
                frgnTripEquals = new And(personEqAnddestEq,dateFrmEq);
                coeusVector = cvApprvdForeignTrpBean.filter(frgnTripEquals);
                if(coeusVector.size()==-1){
                    return false;
                }
                
                if(coeusVector!=null && coeusVector.size() > 1){
                    CoeusOptionPane.showInfoDialog(DUPLICATE_ROW);
                    apprForeignTripForm.tblApprovedForeignTrip.editCellAt(index,PERSON_INDEX);
                    apprForeignTripForm.tblApprovedForeignTrip.requestFocus();
                    apprForeignTripForm.tblApprovedForeignTrip.setRowSelectionInterval(index,index);
                    apprForeignTripForm.tblApprovedForeignTrip.scrollRectToVisible(
                        apprForeignTripForm.tblApprovedForeignTrip.getCellRect(
                        index ,0, true));
                    modified = true;
                    return true;
                }
            }
        }
        return false;
    }
 
   
   /** This method triggers all actions based on the event occured
     * @param actionEvent takes the actionEvent 
     */
   
   public void actionPerformed(ActionEvent actionEvent) {
       Object source = actionEvent.getSource();
       if(source.equals(apprForeignTripForm.btnOk)){
           try{
               if( validate()){
                   saveFormData();
               }
           }catch (Exception exception){
               exception.printStackTrace();
           }
       }else if(source.equals(apprForeignTripForm.btnCancel)){
           performCancelAction();
       }else if(source.equals(apprForeignTripForm.btnAdd)){
           performAddAction();
       }else if(source.equals(apprForeignTripForm.btnDelete)){
           performDeleteAction();
       }else if(source.equals(apprForeignTripForm.btnFindPerson)){
           performFindPerson();
       }
   }
   
   /*Sets the tabe editors renderers and width for each column of the table*/
    private void setTableEditors(){
        try{
            JTableHeader tableHeader = apprForeignTripForm.tblApprovedForeignTrip.getTableHeader();
            tableHeader.setReorderingAllowed(false);
            tableHeader.setFont(CoeusFontFactory.getLabelFont());
            tableHeader.addMouseListener (new ColumnHeaderListener ());
            apprForeignTripForm.tblApprovedForeignTrip.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
            apprForeignTripForm.tblApprovedForeignTrip.setRowHeight(22);
            apprForeignTripForm.tblApprovedForeignTrip.setSelectionBackground(new java.awt.Color(10,36,106));
            apprForeignTripForm.tblApprovedForeignTrip.setSelectionForeground(java.awt.Color.white);
            apprForeignTripForm.tblApprovedForeignTrip.setShowHorizontalLines(true);
            apprForeignTripForm.tblApprovedForeignTrip.setShowVerticalLines(true);
            apprForeignTripForm.tblApprovedForeignTrip.setOpaque(false);

            apprForeignTripForm.tblApprovedForeignTrip.setSelectionMode(
            DefaultListSelectionModel.SINGLE_SELECTION);
            
            apprForeignTripForm.tblApprovedForeignTripTotal.setShowHorizontalLines(false);
            apprForeignTripForm.tblApprovedForeignTripTotal.setShowVerticalLines(false);
            
            TableColumn column;
            //int minWidth[] = {30, 45, 90, 90, 90, 90, 90, 90};
            int prefWidth[] = {155, 155, 80, 80,100};
            for(int index = 0; index < prefWidth.length; index++) {
                column = apprForeignTripForm.tblApprovedForeignTrip.getColumnModel().getColumn(index);
                column.setPreferredWidth(prefWidth[index]);
                column.setCellRenderer(apprForeignTripRenderer);
                column.setCellEditor(apprForeignTripEditor);
            }
                        
            TableColumn amountColumn = 
                apprForeignTripForm.tblApprovedForeignTripTotal.getColumnModel().getColumn(TOTAL_INDEX);
            amountColumn.setPreferredWidth(575);
            amountColumn.setResizable(true);
            amountColumn.setCellRenderer(amountTableCellRenderer);


            amountColumn = apprForeignTripForm.tblApprovedForeignTripTotal.getColumnModel().getColumn(TOTAL_AMOUNT_INDEX);
            amountColumn.setPreferredWidth(125);
            amountColumn.setResizable(true);
            amountColumn.setCellRenderer(amountTableCellRenderer);
                        
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    /*Table Model of Appr foreign Trip*/
    public class ApprForeignTripTableModel extends AbstractTableModel{
         CoeusVector cvTempFrgnBean;
         private String ColumnName[] = {"Person Name","Destination","Date From","Date To","Amount"};
         
         private Class colClass[] = {String.class,String.class,
                                        String.class,String.class,String.class};
         
         public int getColumnCount() {
             return ColumnName.length;
         }
         public Class getColumnClass(int columnIndex){
             return colClass[columnIndex];
         }
      
         public int getRowCount() {
             if (cvTempFrgnBean == null){
                 return 0;
             }else {
                 return cvTempFrgnBean.size();
             }
         }
         
         public void setData(CoeusVector cvApprvdForeignTrpBean){
            this.cvTempFrgnBean = cvApprvdForeignTrpBean;
         }
         
         public String getColumnName(int col) {
             return ColumnName[col];           
         }
         
         public Object getValueAt(int rowIndex, int columnIndex) {
             awardApprovedForeignTripBean = (AwardApprovedForeignTripBean)cvTempFrgnBean.get(rowIndex);
             switch(columnIndex){
                 case PERSON_INDEX:
                     return awardApprovedForeignTripBean.getPersonName ();

                 case DESTINATION_INDEX:
                     return awardApprovedForeignTripBean.getDestination();
                 
                 case DATE_FROM_INDEX:
                     return awardApprovedForeignTripBean.getDateFrom();
                 
                 case DATE_TO_INDEX:
                     return awardApprovedForeignTripBean.getDateTo();
                 
                 case AMOUNT_INDEX:
                     double amount = awardApprovedForeignTripBean.getAmount ();
                     amountTabelModel.fireTableDataChanged();
                     return (""+amount );
             }
            return EMPTY_STRING;
         }
         
         /**
          * Returns true if the cell at <code>rowIndex</code> and
          * <code>columnIndex</code>
          * is editable.  Otherwise, <code>setValueAt</code> on the cell will not
          * change the value of that cell.
          *
          * @param	rowIndex	the row whose value to be queried
          * @param	columnIndex	the column whose value to be queried
          * @return	true if the cell is editable
          * @see #setValueAt
          */
         public boolean isCellEditable(int rowIndex, int columnIndex) {
             if(getFunctionType() == TypeConstants.DISPLAY_MODE){
                 return false;
             }else{
                 return true;
             }
         }
         
         /**
          * Sets the value in the cell at <code>columnIndex</code> and
          * <code>rowIndex</code> to <code>aValue</code>.
          *
          * @param	aValue		 the new value
          * @param	rowIndex	 the row whose value is to be changed
          * @param	columnIndex 	 the column whose value is to be changed
          * @see #getValueAt
          * @see #isCellEditable
          */
         public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
             awardApprovedForeignTripBean = (AwardApprovedForeignTripBean)cvTempFrgnBean.get(rowIndex);
             boolean changed = false;
             String strDateFrom = null;
             String strDateTo = null;
             Date dateFrom = null;
             Date dateTo = null;
             switch(columnIndex){
                 case PERSON_INDEX:
                     String description = (String)aValue;
                     Equals eqPerson = new Equals("description", description);
                     CoeusVector cvSePerson = cvPersons.filter(eqPerson);
                    if (cvSePerson.size() > 0){
                        //description present.
                        ComboBoxBean comboBoxBean = (ComboBoxBean)cvSePerson.get(0);
                        awardApprovedForeignTripBean.setPersonId (comboBoxBean.getCode ());
                        awardApprovedForeignTripBean.setPersonName (comboBoxBean.getDescription ());
                    }else {
                        //description absent. i.e not a valid person.
                        //code has to be set manually(a negative number).
                        initInvalidPerId = initInvalidPerId - 1;
                        awardApprovedForeignTripBean.setPersonId (EMPTY_STRING + initInvalidPerId);
                        awardApprovedForeignTripBean.setPersonName (description);
                        addPersons(EMPTY_STRING + initInvalidPerId, description);
                    }
                    changed = modified = true;
                    break;

                 case DESTINATION_INDEX:
                     if (!aValue.toString().trim().equals(awardApprovedForeignTripBean.getDestination().trim())) {
                          awardApprovedForeignTripBean.setDestination(aValue.toString().trim());
                          changed = modified = true;
                      }
                     break;
                     
                 case DATE_FROM_INDEX:
                     try{
                        if (aValue.toString().trim().length() > 0) {
                            strDateFrom= dtUtils.formatDate(
                            aValue.toString().trim(), DATE_SEPARATERS, DATE_FORMAT);
                        }else{
                            awardApprovedForeignTripBean.setDateFrom(null);
                            return ;
                        }
                        
                        strDateFrom = dtUtils.restoreDate(strDateFrom, DATE_SEPARATERS);
                        if(strDateFrom == null) {
                            throw new CoeusException();
                        }
                        dateFrom = dtFormat.parse(strDateFrom.trim());
                    }catch (ParseException parseException) {
                        parseException.getMessage();
                        CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(
                         "Item "+ "\"" + aValue + "\"" + " does not pass validation test" ));
                    }
                    catch (CoeusException coeusException) {
                         CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(
                         "Item "+ "\"" + aValue + "\"" + " does not pass validation test" ));
                         return ;
                    }
                    awardApprovedForeignTripBean.setDateFrom(new java.sql.Date(dateFrom.getTime()));
                    changed=modified=true;
                    break;

                 case DATE_TO_INDEX:
                     try{
                        if (aValue.toString().trim().length() > 0) {
                            strDateTo= dtUtils.formatDate(
                            aValue.toString().trim(), DATE_SEPARATERS, DATE_FORMAT);
                        } else {
                            awardApprovedForeignTripBean.setDateTo(null);
                            return ;
                        }
                        strDateTo = dtUtils.restoreDate(strDateTo, DATE_SEPARATERS);
                        if(strDateTo == null) {
                            throw new CoeusException();
                        }
                        dateTo = dtFormat.parse(strDateTo.trim());
                    }catch (ParseException parseException) {
                        parseException.getMessage();
                         CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(
                         "Item "+ "\"" + aValue + "\"" + " does not pass validation test" ));
                         awardApprovedForeignTripBean.setDateTo(null);
                         return ;
                    }
                    catch (CoeusException coeusException) {
                         CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(
                         "Item "+ "\"" + aValue + "\"" + " does not pass validation test" ));
                         awardApprovedForeignTripBean.setDateTo(null);
                         return ;
                    }
                    awardApprovedForeignTripBean.setDateTo(new java.sql.Date(dateTo.getTime()));
                    changed=modified=true;
                    break;

                 case AMOUNT_INDEX:
                    double amount = Double.parseDouble(aValue.toString());
                    if(amount != awardApprovedForeignTripBean.getAmount()) {
                        awardApprovedForeignTripBean.setAmount (amount);
                        changed = modified = true;
                    }
                    break;
             }
             
             if(changed){
                 String ac = awardApprovedForeignTripBean.getAcType() ;
                 if(awardApprovedForeignTripBean.getAcType() == null) {
                     awardApprovedForeignTripBean.setAcType(TypeConstants.UPDATE_RECORD);
                 }
             }
         }//End setValueAt
      }//End Class Table Model
      
    /*Table cell renderer of Appr foreign Trip*/
    class ApprForeignTripRenderer extends DefaultTableCellRenderer implements TableCellRenderer {

        private CoeusTextField txtComponent;
        private DollarCurrencyTextField txtAmount;
        private JLabel lblText,lblAmount;
        
        public ApprForeignTripRenderer(){
            txtComponent = new CoeusTextField();
            txtAmount =  new DollarCurrencyTextField();
            lblText = new JLabel();
            lblAmount = new JLabel();
            lblText.setOpaque(true);
            lblAmount.setOpaque(true);
            lblAmount.setHorizontalAlignment(RIGHT);
            txtComponent.setBorder(new javax.swing.border.EmptyBorder(0,0,0,0));
            txtAmount.setBorder(new javax.swing.border.EmptyBorder(0,0,0,0));
        }
        
        public java.awt.Component getTableCellRendererComponent(JTable table, Object value,
        boolean isSelected, boolean hasFocus, int row, int col){
            switch(col) {
                case PERSON_INDEX:
                case DESTINATION_INDEX:
                    if(getFunctionType() == TypeConstants.DISPLAY_MODE ){
                        lblText.setBackground(disabledBackground);
                        lblText.setForeground(java.awt.Color.BLACK);
                    }else if(isSelected){
                        lblText.setBackground(java.awt.Color.YELLOW);
                        lblText.setForeground(java.awt.Color.black);
                    }else{
                        lblText.setBackground(java.awt.Color.white);
                        lblText.setForeground(java.awt.Color.black);
                    }
                    
                    if(value == null || value.toString().trim().equals(EMPTY_STRING)){
                        txtComponent.setText(EMPTY_STRING);
                        lblText.setText(txtComponent.getText());
                    }else{
                        txtComponent.setText(value.toString());
                        lblText.setText(txtComponent.getText());
                    }
                    return lblText;
                    
                case DATE_FROM_INDEX:
                case DATE_TO_INDEX:
                    if(value == null){
                        txtComponent.setText(EMPTY_STRING);
                        lblText.setText(txtComponent.getText());
                    }else{
                        value = dtUtils.formatDate(value.toString(),DATE_FORMAT);
                        txtComponent.setText(value.toString());
                        lblText.setText(txtComponent.getText());
                    }
                    return lblText;
                    
                case AMOUNT_INDEX:
                    if(getFunctionType() == TypeConstants.DISPLAY_MODE ){
                        lblAmount.setBackground(disabledBackground);
                        lblAmount.setForeground(java.awt.Color.BLACK);
                    }else if(isSelected){
                        lblAmount.setBackground(java.awt.Color.YELLOW);
                        lblAmount.setForeground(java.awt.Color.black);
                    }else{
                        lblAmount.setBackground(java.awt.Color.white);
                        lblAmount.setForeground(java.awt.Color.black);
                    }
                    
                    if(value == null || value.toString().trim().equals(EMPTY_STRING)){
                        txtAmount.setText(EMPTY_STRING);
                        lblAmount.setText(txtAmount.getText());
                    }else{
                        txtAmount.setValue(Double.parseDouble(value.toString()));
                        lblAmount.setText(txtAmount.getText());
                    }
                    return lblAmount;
            }
            return lblAmount;
        }
    }
    
    /*Table cell Editor of Appr foreign Trip */
    class ApprForeignTripEditor extends AbstractCellEditor implements TableCellEditor
      {
        private CoeusTextField txtComponent;
        private CoeusTextField txtDate;
        private DollarCurrencyTextField txtAmount;
        private int column;
                        
        public ApprForeignTripEditor() {
            txtComponent = new CoeusTextField();
            txtDate = new CoeusTextField();
            txtAmount = new DollarCurrencyTextField(12,DollarCurrencyTextField.RIGHT,true);
            
            /*Code for performing the OK presssed action starts*/
            txtComponent.addKeyListener(new KeyAdapter(){
                public void keyPressed(KeyEvent kEvent){
                    if( kEvent.getKeyCode() == KeyEvent.VK_ENTER){
                        apprForeignTripEditor.stopCellEditing();
                        apprForeignTripForm.btnOk.doClick();
                        kEvent.consume();
                    }
                }
            });
            
            txtDate.addKeyListener(new KeyAdapter(){
                public void keyPressed(KeyEvent kEvent){
                    if( kEvent.getKeyCode() == KeyEvent.VK_ENTER){
                        apprForeignTripEditor.stopCellEditing();
                        apprForeignTripForm.btnOk.doClick();
                        kEvent.consume();
                    }
                }
            });
            
            txtAmount.addKeyListener(new KeyAdapter(){
                public void keyPressed(KeyEvent kEvent){
                    if( kEvent.getKeyCode() == KeyEvent.VK_ENTER){
                        apprForeignTripEditor.stopCellEditing();
                        apprForeignTripForm.btnOk.doClick();
                        kEvent.consume();
                    }
                }
            });
            /*Code for performing the OK presssed action ends*/
        }
        
        private void populateCombo() {
            int size = cvInvestgator.size();
            cvPersons = new CoeusVector();
            comboBoxModel = new DefaultComboBoxModel();
           
            for(int index = 0; index < size; index++) {
                InvestigatorBean  investigatorBean = 
                    (InvestigatorBean)cvInvestgator.get(index);
                String str = investigatorBean.getPersonId();
                str = investigatorBean.getPersonName(); 
                addPersons(investigatorBean.getPersonId(),investigatorBean.getPersonName());
            }
            
            //get persons from award approved.
            for(int index = 0; index < cvApprvdForeignTrpBean.size(); index++) {
                AwardApprovedForeignTripBean awardApprovedForeignTripBean = 
                (AwardApprovedForeignTripBean)cvApprvdForeignTrpBean.get(index);
                addPersons(awardApprovedForeignTripBean.getPersonId(), 
                awardApprovedForeignTripBean.getPersonName());
            }//End for
            cmbPerson.setModel(comboBoxModel);
        }
        
        public Object getCellEditorValue() {
            switch(column){
                case PERSON_INDEX:
                    return txtComboEditor.getText ();
                case DESTINATION_INDEX:
                    return txtComponent.getText();
                case DATE_FROM_INDEX:
                case DATE_TO_INDEX:
                    return txtDate.getText();
                case AMOUNT_INDEX:
                    return txtAmount.getValue();
            }
            return txtComponent;
        }
        
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            this.column = column;
            switch(column){
                case PERSON_INDEX:
                    if(!EMPTY_STRING.equals (value)){
                        ComboBoxBean selectedBean = new ComboBoxBean(EMPTY_STRING, value.toString ());
                        cmbPerson.setSelectedItem(selectedBean);                        
                    }
                    ((BasicComboBoxEditor)cmbPerson.getEditor ()).setItem (value);
                    ((BasicComboBoxEditor)cmbPerson.getEditor ()).selectAll ();
                    Component cmp = cmbPerson.getEditor ().getEditorComponent ();
                    if( cmp instanceof CoeusTextField) {
                        ((CoeusTextField)cmp).setText ((String)value);
                    }
                    return cmbPerson;
            
                case DESTINATION_INDEX:
                    txtComponent.setDocument(new LimitedPlainDocument(30));
                    if(value == null){
                        txtComponent.setText(EMPTY_STRING);
                    }else{
                        txtComponent.setText(value.toString());
                    }
                    return txtComponent;
                    
                case DATE_FROM_INDEX:
                case DATE_TO_INDEX:
                    String strDate = EMPTY_STRING;
                    if(value != null){
                        txtDate.setDocument(new LimitedPlainDocument(11));
                        strDate = dtUtils.formatDate(value.toString(), SIMPLE_DATE_FORMAT); 
                        txtDate.setText(strDate);
                    }else {
                        txtDate.setText(EMPTY_STRING);
                    }
                    return txtDate;
                case AMOUNT_INDEX:
                    if(value== null){
                        txtAmount.setValue(0.00);
                    }else{
                        txtAmount.setValue(new Double(value.toString()).doubleValue());
                    }
                    return txtAmount;
            }
            return txtComponent;
        }
        
         public void setFocus(int column) {
            switch (column) {
                case PERSON_INDEX:
                    cmbPerson.requestFocusInWindow();
                case DESTINATION_INDEX:
                    txtComponent.requestFocusInWindow();
                case DATE_FROM_INDEX:
                    txtDate.requestFocusInWindow();
                case DATE_TO_INDEX:
                    txtDate.requestFocusInWindow();
                case AMOUNT_INDEX:
                    txtAmount.requestFocusInWindow();
            }//End switch
        }//End setFocus
    }
      
      public class AmountTabelModel extends AbstractTableModel {
        private String colName[] = {"Total: ", ""};
        private Class colClass[] = {String.class, Double.class};
        
        public boolean isCellEditable(int row, int col){
            return false;
        }
        public int getColumnCount(){
            return colName.length;
        }
        
        public Class getColumnClass(int colIndex){
            return colClass[colIndex];
        }
        public int getRowCount(){
            return 1;
        }
        
        public void setData(CoeusVector cvApprvdForeignTrpBean){
            cvApprvdForeignTrpBean = cvApprvdForeignTrpBean;
        }
        public String getColumnName(int column){
            return colName[column];
        }
        
        public Object getValueAt(int row, int col) {
           double totalAmount = 0.00;
           String name = "Total: ";
           if(col == TOTAL_INDEX){
               return name;
           }
           if(col == TOTAL_AMOUNT_INDEX){
               totalAmount = cvApprvdForeignTrpBean.sum("amount");
               return new Double(totalAmount);
           }
            return EMPTY_STRING;
        }
    }
        
        public class AmountTableCellRenderer extends DefaultTableCellRenderer 
        implements TableCellRenderer {
        private JTextField txtComponent;
        private DollarCurrencyTextField txtCurrencyComponent;
        
        public AmountTableCellRenderer(){
            txtComponent = new JTextField();
            txtCurrencyComponent = new DollarCurrencyTextField();
            txtComponent.setBackground(
                        (java.awt.Color) javax.swing.UIManager.getDefaults().get("Panel.background"));
            txtComponent.setHorizontalAlignment(JTextField.RIGHT);
            txtComponent.setForeground(java.awt.Color.BLACK);
            txtComponent.setFont(CoeusFontFactory.getLabelFont());
            txtComponent.setBorder(new javax.swing.border.EmptyBorder(0,0,0,0));
            txtCurrencyComponent.setBackground(
                        (java.awt.Color) javax.swing.UIManager.getDefaults().get("Panel.background"));
            txtCurrencyComponent.setForeground(java.awt.Color.BLACK);
            txtCurrencyComponent.setBorder(new javax.swing.border.EmptyBorder(0,0,0,0));
        }

        public java.awt.Component getTableCellRendererComponent(javax.swing.JTable table, Object value,
        boolean isSelected, boolean hasFocus, int row, int col){
            JLabel lblText = new JLabel();
            lblText.setHorizontalAlignment (RIGHT);
            switch(col){
                case TOTAL_INDEX:
                    if(value == null || value.toString().trim().equals(EMPTY_STRING)){
                        txtComponent.setText(EMPTY_STRING);
                        lblText.setText (txtComponent.getText ());
                    }else{
                        txtComponent.setText(value.toString());
                        lblText.setText (txtComponent.getText ());
                    }
                    return lblText;

                case TOTAL_AMOUNT_INDEX:
                    if(value == null || value.toString().trim().equals(EMPTY_STRING)){
                        txtCurrencyComponent.setText(EMPTY_STRING);
                        lblText.setText (txtCurrencyComponent.getText ());
                    }else{
                        txtCurrencyComponent.setValue(new Double(value.toString()).doubleValue());
                        lblText.setText (txtCurrencyComponent.getText ());
                    }
                    return lblText;
            }
            return lblText;
        }

    }
   
    /*Adds a new row*/
    private void performAddAction() {
        if(cvApprvdForeignTrpBean != null && cvApprvdForeignTrpBean.size() > 0){
            apprForeignTripEditor.stopCellEditing();
        }
        
        double cost = 0.0;
        
        /*IF empty row are present then do not add a new row*/
        if (cvApprvdForeignTrpBean.size()>0) {
            AwardApprovedForeignTripBean lastRowBean=(AwardApprovedForeignTripBean)cvApprvdForeignTrpBean.elementAt(cvApprvdForeignTrpBean.size()-1);
            if (cost==lastRowBean.getAmount() && (EMPTY_STRING.equals(lastRowBean.getPersonName())) && (EMPTY_STRING.equals(lastRowBean.getDestination()))) {
                if(cvApprvdForeignTrpBean.size()>0) {
                    apprForeignTripForm.tblApprovedForeignTrip.setRowSelectionInterval (cvApprvdForeignTrpBean.size()-1,cvApprvdForeignTrpBean.size()-1);
                    apprForeignTripForm.tblApprovedForeignTrip.setColumnSelectionInterval (0,0);
                }
                return;
            }
        }
        if(!apprForeignTripForm.tblApprovedForeignTripTotal.isVisible ()){
            apprForeignTripForm.tblApprovedForeignTripTotal.setVisible (true);
        }
        if(!apprForeignTripForm.btnFindPerson.isEnabled ()){
            apprForeignTripForm.btnFindPerson.setEnabled (true);
        }
        
        AwardApprovedForeignTripBean newRowBean = new AwardApprovedForeignTripBean();
        newRowBean.setMitAwardNumber(awardBaseBean.getMitAwardNumber());
        newRowBean.setSequenceNumber(awardBaseBean.getSequenceNumber());
        newRowBean.setPersonId(EMPTY_STRING);
        newRowBean.setPersonName(EMPTY_STRING);
        newRowBean.setDestination(EMPTY_STRING);
        newRowBean.setAmount(0.0);
        rowId = rowId+1;
        newRowBean.setRowId(rowId++);
        newRowBean.setAcType(TypeConstants.INSERT_RECORD);
        modified = true;
        cvApprvdForeignTrpBean.add(newRowBean);
        apprForeignTripTableModel.fireTableRowsInserted(apprForeignTripTableModel.getRowCount(),
        apprForeignTripTableModel.getRowCount());

        int lastRow = apprForeignTripForm.tblApprovedForeignTrip.getRowCount()-1;
        if(lastRow >= 0){
            apprForeignTripForm.tblApprovedForeignTrip.setRowSelectionInterval(lastRow,lastRow);
            apprForeignTripForm.tblApprovedForeignTrip.setColumnSelectionInterval (0, 0);
            apprForeignTripForm.tblApprovedForeignTrip.scrollRectToVisible(
            apprForeignTripForm.tblApprovedForeignTrip.getCellRect(lastRow, PERSON_INDEX , true));
            apprForeignTripForm.tblApprovedForeignTrip.editCellAt(lastRow,PERSON_INDEX);
            apprForeignTripForm.tblApprovedForeignTrip.getEditorComponent ().requestFocusInWindow ();
        }

        if(apprForeignTripForm.tblApprovedForeignTrip.getRowCount() == 0){
            apprForeignTripForm.tblApprovedForeignTrip.setVisible(false);
        }else{
            apprForeignTripForm.tblApprovedForeignTrip.setVisible(true);
        }
    }
    
    /*IF cancel button is pressed*/
    private void performCancelAction(){
         apprForeignTripEditor.stopCellEditing();
         
         if(modified){
             int option = CoeusOptionPane.showQuestionDialog(
             coeusMessageResources.parseMessageKey(SAVE_CHANGES),
             CoeusOptionPane.OPTION_YES_NO_CANCEL,
             JOptionPane.YES_OPTION);
             switch( option ) {
                 case (JOptionPane.YES_OPTION ):
                     setSaveRequired(true);
                     try{
                         if( validate() ){
                             saveFormData();
                         }
                     }catch (Exception exception){
                         exception.printStackTrace();
                     }
                     break;
                 case(JOptionPane.NO_OPTION ):
                     dlgApprForeignTripForm.dispose();
                     break;
                 default:
                     break;
             }
         }else{
             dlgApprForeignTripForm.dispose();
         }
     }
     
    /*Deletes the row*/
    private void performDeleteAction() {
        apprForeignTripEditor.stopCellEditing();
        int selectedRow = apprForeignTripForm.tblApprovedForeignTrip.getSelectedRow();
        if(selectedRow == -1){
            return;
        }

        if(selectedRow != -1 && selectedRow >= 0){
            String mesg = DELETE_CONFIRMATION;
            int selectedOption = CoeusOptionPane.showQuestionDialog(
            coeusMessageResources.parseMessageKey(mesg),
            CoeusOptionPane.OPTION_YES_NO,
            CoeusOptionPane.DEFAULT_YES);
            if(selectedOption == CoeusOptionPane.SELECTION_YES) {
                AwardApprovedForeignTripBean deletedCostSharingBean = (AwardApprovedForeignTripBean)cvApprvdForeignTrpBean.get(selectedRow);
                deletedCostSharingBean.setAcType(TypeConstants.DELETE_RECORD);    
                cvDeletedItem.add(deletedCostSharingBean);
                if(cvApprvdForeignTrpBean!=null && cvApprvdForeignTrpBean.size() > 0){
                    cvApprvdForeignTrpBean.remove(selectedRow);
                    apprForeignTripTableModel.fireTableRowsDeleted(selectedRow, selectedRow);
                    modified = true;
                }
                
                if(selectedRow >0){
                    apprForeignTripForm.tblApprovedForeignTrip.setRowSelectionInterval(
                    selectedRow-1,selectedRow-1);
                    apprForeignTripForm.tblApprovedForeignTrip.scrollRectToVisible(
                     apprForeignTripForm.tblApprovedForeignTrip.getCellRect(
                     selectedRow -1 ,0, true));
                }else{
                    apprForeignTripForm.tblApprovedForeignTripTotal.setVisible (false);
                    apprForeignTripForm.btnFindPerson.setEnabled (false);
                }
            }
        }
    }    
    
     /* Supporting method used for person search */
     private void performFindPerson(){
        try{
            CoeusSearch proposalSearch = null;
            proposalSearch = new CoeusSearch(CoeusGuiConstants.getMDIForm(), "PERSONSEARCH",
                                             CoeusSearch.TWO_TABS );     
            proposalSearch.showSearchWindow();
            if(proposalSearch.getSelectedRow() == null) {
                return ;
            }
            String personId = proposalSearch.getSelectedRow().get("PERSON_ID").toString().trim();
            String personName = proposalSearch.getSelectedRow().get("FULL_NAME").toString().trim();
            addPersons(personId, personName);
        }catch( Exception err ){
            err.printStackTrace();
        }
     }
     
     /*Adds the persons to the combobox */
     public void addPersons(String personId , String personName ){
            personId  = personId;
            personName = personName;
           
            comboBoxBean = new ComboBoxBean(personId,personName);
            
            int selRow = apprForeignTripForm.tblApprovedForeignTrip.getSelectedRow();
            AwardApprovedForeignTripBean awardApprovedForeignTripBean  = new AwardApprovedForeignTripBean();
            if(selRow == -1 || selRow >= cvApprvdForeignTrpBean.size()) return ;
              
            //Check duplicate
            Equals eqPerson = new Equals("description", personName);
            if(cvPersons.filter(eqPerson).size() > 0){
                //Duplicate Person. Don't Add. just return.
                awardApprovedForeignTripBean = (AwardApprovedForeignTripBean)cvApprvdForeignTrpBean.get(selRow);
                awardApprovedForeignTripBean.setPersonId(personId);
                awardApprovedForeignTripBean.setPersonName(personName);
                apprForeignTripTableModel.fireTableRowsUpdated(selRow,selRow);
                return ;
            }
            
            comboBoxModel.addElement(comboBoxBean);
            cvPersons.add(comboBoxBean);
            cmbPerson.setSelectedItem (comboBoxBean);

            awardApprovedForeignTripBean = (AwardApprovedForeignTripBean)cvApprvdForeignTrpBean.get(selRow);
            awardApprovedForeignTripBean.setPersonId(personId);
            awardApprovedForeignTripBean.setPersonName(personName);
            apprForeignTripTableModel.fireTableRowsUpdated(selRow,selRow);
        }
     
     /*To set the focus inside the table cell*/
     private void setRequestFocusInThread(final Component component) {
         SwingUtilities.invokeLater( new Runnable() {
            public void run() {
                component.requestFocusInWindow();
            }
        });
    }
         
     /** This class will sort the column values in ascending and descending order
      * based on number of clicks.
      */
     public class ColumnHeaderListener extends MouseAdapter {

         String nameBeanId [][] ={
             {"0","personName"},
             {"1","destination" },
             {"2","dateFrom"},
             {"3","dateTo"},
             {"4","amount"}
         };
         boolean sort =true;
         /** Mouse click handler for the table headers to sort upon the headers
          * @param evt mouse event
          */
         public void mouseClicked (MouseEvent evt) {
             apprForeignTripEditor.stopCellEditing ();
             try {
                 JTable table = ((JTableHeader)evt.getSource ()).getTable ();
                 TableColumnModel colModel = table.getColumnModel ();
                 
                 // The index of the column whose header was clicked
                 int vColIndex = colModel.getColumnIndexAtX (evt.getX ());
                 if(cvApprvdForeignTrpBean != null && cvApprvdForeignTrpBean.size ()>0 &&
                 nameBeanId [vColIndex][1].length () >1 ){
                 
                     //sort method cannot be used here. will have to sort these ourselves.
                     ((CoeusVector)cvApprvdForeignTrpBean).sort (nameBeanId [vColIndex][1],sort);
                     if (sort) {
                         sort = false;
                     }
                     else {
                         sort = true;
                     }
                     apprForeignTripTableModel.fireTableRowsUpdated (
                     0, apprForeignTripTableModel.getRowCount ());
                 }
             } catch(Exception exception) {
                 exception.getMessage ();
             }
         }
     }// End of ColumnHeaderListener.................
        
}//end of ApprForeignTripController




