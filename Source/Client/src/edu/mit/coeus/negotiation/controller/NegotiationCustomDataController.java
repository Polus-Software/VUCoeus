/*
 * NegotiationCustomDataController.java
 *
 * Created on July 12, 2004, 7:00 PM
 */

package edu.mit.coeus.negotiation.controller;

/**
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 * @author  nadhgj
 */
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;

import edu.mit.coeus.utils.query.*;
import edu.mit.coeus.exception.*;
import edu.mit.coeus.utils.*;
import edu.mit.coeus.utils.customelements.CustomElementsForm;
import edu.mit.coeus.gui.CoeusDlgWindow;
import edu.mit.coeus.gui.CoeusAppletMDIForm;
import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.negotiation.gui.NegotiationCustomDataPanel;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.negotiation.bean.NegotiationCustomElementsBean;
import edu.mit.coeus.negotiation.bean.NegotiationBaseBean;
import edu.mit.coeus.customelements.bean.CustomElementsInfoBean;



public class NegotiationCustomDataController extends NegotiationController implements TypeConstants, ActionListener{
    
     /**
     * Instance of Query Engine
     */
    private QueryEngine queryEngine;
    
    private char functionType;
    /**
     * Instance of CustomElements Form
     */
    private CustomElementsForm customElementsForm;
    
    /*CoeusVectors of custom elements data */
    private CoeusVector columnValues;
    
    /**
     * Instance of negotiation custom data dialog window
     */
    private CoeusDlgWindow dlgNegotiationData;
    
    /**
     * Instance of Custom data panel 
     */
    private NegotiationCustomDataPanel negotiationCustomDataPanel;
    
    
    private NegotiationBaseBean negotiationBaseBean;
    //Case :#3149 – Tabbing between fields does not work on others tabs - Start
    private boolean cancelButtonPressed = false;
    //Case :#3149 - End
       
    /**
     * To create an instance of MDIform
     */ 
    
    private CoeusAppletMDIForm mdiForm = CoeusGuiConstants.getMDIForm();
    
    //To access Messages.Properties file
    private CoeusMessageResources coeusMessageResources = CoeusMessageResources.getInstance();
   
    /*For setting dimentions*/
    private static final int WIDTH = 745;
    private static final int HEIGHT = 450;
    
    
    /** Creates a new instance of NegotiationCustomDataController */
    public NegotiationCustomDataController(NegotiationBaseBean negotiationBaseBean, char functionType) {
        super(negotiationBaseBean);
        customElementsForm = new CustomElementsForm();
        //Case :#3149 – Tabbing between fields does not work on others tabs - Start
        if(getFunctionType() != DISPLAY_MODE){
            customElementsForm.setFocusToNegotiation(true);
            customElementsForm.setTableFocus();
        }
        //Case :#3149 - End
        negotiationCustomDataPanel = new NegotiationCustomDataPanel();
        queryEngine = QueryEngine.getInstance();
        registerComponents();
        setFunctionType(functionType);
        postInitComponents();
        setFormData(negotiationBaseBean);
        
    }
    
    //this method displays the custom dialog window
    public void display() {
        // bug fix #1131 - Added by chandra - 12-Aug-2004
        if(columnValues!= null && columnValues.size() > 0){
         dlgNegotiationData.setVisible(true);
        }else{
            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey("There are no custom elements. "));
            return ;
        }// bug fix #1131 - Added by chandra - 12-Aug-2004-End
    }
    
    /**this method makes OK button visible based on function type
    */
    public void formatFields() {
        boolean enabled = getFunctionType() != TypeConstants.DISPLAY_MODE ? true : false;
        negotiationCustomDataPanel.btnOk.setVisible(enabled);  
    }
    
    
    public java.awt.Component getControlledUI() {
        return customElementsForm;
    }
    
    public Object getFormData() {
        return null;
    }
    
    public void registerComponents() {
    }
    
    /** This method saves form data 
     *@returns void
     
    */
    public void saveFormData() {
      
        if( isDataChanged() || getFunctionType() == TypeConstants.MODIFY_MODE || getFunctionType() == TypeConstants.ADD_MODE ) {
            Vector genericColumnValues = customElementsForm.getOtherColumnElementData();
            if( genericColumnValues != null && genericColumnValues.size() > 0 ){
               CustomElementsInfoBean genericCustElementsBean = null;
                int dataSize = genericColumnValues.size();
                for( int indx = 0; indx < dataSize; indx++ ) {
                    genericCustElementsBean = (CustomElementsInfoBean)genericColumnValues.get(indx);
                    NegotiationCustomElementsBean negotiationCustomElementsBean
                    = new NegotiationCustomElementsBean(genericCustElementsBean);
                    if( INSERT_RECORD.equals(genericCustElementsBean.getAcType()) ){
                        negotiationCustomElementsBean.setNegotiationNumber(this.negotiationBaseBean.getNegotiationNumber());
                         
                    }else{
                        if( genericCustElementsBean instanceof NegotiationCustomElementsBean ) {
                            NegotiationCustomElementsBean oldnegotiationCustomElementsBean =
                            (NegotiationCustomElementsBean)genericCustElementsBean;
                            negotiationCustomElementsBean.setNegotiationNumber(oldnegotiationCustomElementsBean.getNegotiationNumber());
                            
                            
                        }else{
                            continue;
                        }
                        
                    }
                    try{
                        String custAcType = negotiationCustomElementsBean.getAcType();
                        if( UPDATE_RECORD.equals(custAcType) ){
                            negotiationCustomElementsBean.setNegotiationNumber(this.negotiationBaseBean.getNegotiationNumber());
                            queryEngine.update(queryKey, negotiationCustomElementsBean);
                        }else if( INSERT_RECORD.equals(custAcType)){
                            negotiationCustomElementsBean.setNegotiationNumber(this.negotiationBaseBean.getNegotiationNumber());
                            queryEngine.insert(queryKey, negotiationCustomElementsBean);
                        }
                    }catch ( CoeusException ce ) {
                        ce.printStackTrace();
                    }
                    
                }
            }
        }
    }
    
    /** This method sets form data 
     *@returns void
     
    */
    public void setFormData(Object negotiationBaseBean) {
        this.negotiationBaseBean=(NegotiationBaseBean)negotiationBaseBean;
        try{
            columnValues = queryEngine.executeQuery(queryKey, NegotiationCustomElementsBean.class, CoeusVector.FILTER_ACTIVE_BEANS);
            customElementsForm.setFunctionType(getFunctionType());
            customElementsForm.setPersonColumnValues(columnValues);
            customElementsForm.setCanMaintainProposal(true);
            customElementsForm.setSaveRequired(false);
            
        }catch ( CoeusException ce ) {
            ce.printStackTrace();
        }
    }
    
     // This method is used for Validations. returns a boolean 
    public boolean validate() throws CoeusUIException {
        try {
            boolean isValid=customElementsForm.validateData();
            return isValid;
        } catch (Exception e) {
            CoeusOptionPane.showInfoDialog(e.getMessage());
            return false;
        }
        
    }
    
    /** this method called on form closing
     @returns void
     */
    private void performWindowClosing(){
        if(customElementsForm.isSaveRequired()){
            int option = CoeusOptionPane.showQuestionDialog(
            coeusMessageResources.parseMessageKey("saveConfirmCode.1002"),
            CoeusOptionPane.OPTION_YES_NO_CANCEL,
             CoeusOptionPane.DEFAULT_YES);
            System.out.println(option);
            System.out.println(JOptionPane.YES_OPTION);
            switch( option ) {
                case (JOptionPane.YES_OPTION ):
                    
                    try{
                        
                        if(validate()){
                            saveFormData();
                        }
                        
                    }catch (Exception exception){
                        exception.printStackTrace();
                    }
                    break;
                case(JOptionPane.NO_OPTION ):
                    close();
                    break;
                default:
                    break;
            }
        }else{
            close();
        }

    }
    // This method add Listeneres for ok and cancel buttons
    private void setListenersForButtons(){
        
        negotiationCustomDataPanel.btnOk.addActionListener(this);
        negotiationCustomDataPanel.btnCancel.addActionListener(this);
        
      }
    
    public boolean isDataChanged(){
        return customElementsForm.isSaveRequired();
    }
    
    /** This method initalize Custom Dlg window 
     *@ returns void
     */
    private void postInitComponents() {

        setListenersForButtons();
        dlgNegotiationData = new CoeusDlgWindow(mdiForm); 
        dlgNegotiationData.setResizable(false);
        dlgNegotiationData.setModal(true);
        negotiationCustomDataPanel.setCustomDataReference(customElementsForm);
      
       
        dlgNegotiationData.getContentPane().add(negotiationCustomDataPanel);
        dlgNegotiationData.setFont(CoeusFontFactory.getLabelFont());
        dlgNegotiationData.setDefaultCloseOperation(CoeusDlgWindow.DO_NOTHING_ON_CLOSE);
        dlgNegotiationData.setSize(WIDTH, HEIGHT);
        dlgNegotiationData.setTitle("Coeus");
        
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension dlgSize = dlgNegotiationData.getSize();
        dlgNegotiationData.setLocation(screenSize.width/2 - (dlgSize.width/2),
        screenSize.height/2 - (dlgSize.height/2));
        
        dlgNegotiationData.addWindowListener(new WindowAdapter(){
           
            public void windowClosing(WindowEvent we){
               
                      performWindowClosing();
                
            }
        });
        
        /*To set the default focus*/
        dlgNegotiationData.addComponentListener(
         new ComponentAdapter(){
             public void componentShown(ComponentEvent e){
                 //Case :#3149 – Tabbing between fields does not work on others tabs - Start
                 //negotiationCustomDataPanel.btnCancel.requestFocusInWindow();
                 if(getFunctionType() != DISPLAY_MODE && customElementsForm != null){
                     customElementsForm.setFocusToNegotiation(true);
                     customElementsForm.setTableFocus();
                 }
                 //Case :#3149 - End
             }
         });
    }
   
   public void actionPerformed(ActionEvent actionEvent) {
        Object source = actionEvent.getSource();
        
        if(source.equals(negotiationCustomDataPanel.btnOk)){
            try{
                   if(isDataChanged()){
                    if(validate()){
                       saveFormData();
                       dlgNegotiationData.setVisible(false);
                    }
                    //Case :#3149 – Tabbing between fields does not work on others tabs - Start
                    else{
                        if(getFunctionType() != DISPLAY_MODE){
                            cancelButtonPressed = false;
                            setTableCellFocus();
                        }
                    }
                    //Case :#3149 - End
                   }
                   else {
                      dlgNegotiationData.setVisible(false);
                    }
            }catch(Exception e){
                    CoeusOptionPane.showErrorDialog(e.getMessage());
          
                }
                     
        }else if(source.equals(negotiationCustomDataPanel.btnCancel)){
            //perform cancel action 
            performCancelAction();
        }
    }
   
   
   //Case :#3149 – Tabbing between fields does not work on others tabs - Start
   /**
    * Method to set focus to the table cell
    */
   private void setTableCellFocus(){
       if(getFunctionType() != DISPLAY_MODE && customElementsForm != null){
           JTable customTable = customElementsForm.getTable();
           int row = customElementsForm.getRow();
           int column = customElementsForm.getColumn();
           if(!cancelButtonPressed){
               row = customElementsForm.getmandatoryRow();
               column = customElementsForm.getmandatoryColumn();
           }
           
           boolean[] lookUpAvailable = customElementsForm.getLookUpAvailable();
           if(column == 2 && !lookUpAvailable[row]){
               row++;
           }
           if(lookUpAvailable[row]){
               if(!cancelButtonPressed){
                   column++;
               }
               customTable.editCellAt(row,column);
               customTable.setRowSelectionInterval(row,row);
               customTable.setColumnSelectionInterval(column,column);
               
           }else{
               customTable.editCellAt(row,column);
               customTable.setRowSelectionInterval(row,row);
               customTable.setColumnSelectionInterval(column,column);
           }
       }
   }
   //Case ;#3149 - End
   
   private void performCancelAction(){ 
          if(customElementsForm.isSaveRequired()){
            confirmClosing();
        }else{
            dlgNegotiationData.setVisible(false);
             
        }
     } 
   
   /**This method performs close action
    *@return void
    */
   private void confirmClosing(){
        try{
            int option = CoeusOptionPane.showQuestionDialog(
            coeusMessageResources.parseMessageKey("saveConfirmCode.1002"),
            CoeusOptionPane.OPTION_YES_NO_CANCEL,CoeusOptionPane.DEFAULT_CANCEL);
            if(option == CoeusOptionPane.SELECTION_YES){
                 if(validate()){
                        saveFormData();
                    }
            }else if(option == CoeusOptionPane.SELECTION_NO){
                setSaveRequired(false);
                dlgNegotiationData.setVisible(false);
            }else if(option==CoeusOptionPane.SELECTION_CANCEL){
                 //Case :#3149 – Tabbing between fields does not work on others tabs - Start
                if(getFunctionType() != DISPLAY_MODE){
                    cancelButtonPressed = true;
                    setTableCellFocus();
                }
                //Case :#3149 - End
                return;
            }
        }catch(Exception exception){
            exception.printStackTrace();
            CoeusOptionPane.showErrorDialog(exception.getMessage());
            
        }
    }
   
   /** this method closes the dialog window
    */ 
   private void close(){
        dlgNegotiationData.dispose();
    }
   
        
   
}