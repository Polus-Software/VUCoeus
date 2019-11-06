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

import edu.mit.coeus.gui.*;
import edu.mit.coeus.brokers.*;
import edu.mit.coeus.bean.*;
import edu.mit.coeus.award.gui.*;
import edu.mit.coeus.award.bean.*;
import edu.mit.coeus.utils.*;
import edu.mit.coeus.utils.query.*;
import edu.mit.coeus.exception.*;

/*
 * AddRecipientsHeaderController .java
 * @author  ajaygm
 * Created on June 10, 2004, 8:05 PM
 */

public class AddRecipientsHeaderController extends AwardController 
implements ActionListener,Observer{
    
    /*Creates an instance of contacts bean used for populating the table*/
    private AwardContactBean awardContactBean = new AwardContactBean();
    
    /**
     * Instance of the Dialog
     */ 
    public CoeusDlgWindow dlgAddRecipientsHeaderForm;
    
    /**
     * To create an instance of MDIform
     */ 
    private CoeusAppletMDIForm mdiForm = CoeusGuiConstants.getMDIForm();
    
    /**
     * Instance of Coeus Message Resources
     */ 
    private CoeusMessageResources coeusMessageResources;
    
    /*Holds an instance of Add Recipients Header Form*/ 
    private AddRecipientsHeaderForm addRecipientsHeaderForm;
    
    /*Holds an instance of Add Recipients Header controller*/
    private AwardAddRecipientsController awardAddRecipientsController;
    
    /*Holds an instance of Award Add Recipients Form*/
    private AwardAddRecipientsForm awardAddRecipientsForm;
    
    /**
     * Instance of Query Engine
     */
    private QueryEngine queryEngine;
    
    
    private char functionType;    
  
    /*CoeusVector For setting seq no and award no*/
    private CoeusVector cvAwardDetailsBean;
    
    /*CoeusVectors of contacts bean */
    private CoeusVector cvContactType;
    
    /*CoeusVectors of Approved Equipment trip bean */
    private CoeusVector cvAwardContactBean;

    
    /*Contains the persons selected*/
    private CoeusVector cvSelectedPersons = null;
    private ListSelectionModel contactsSelectionModel;
    
    /*For setting dimentions*/
    private static final String WINDOW_TITLE = "Add Recipients";
    private static final int WIDTH = 610;
    private static final int HEIGHT = 375;
    
    private static final String EMPTY = "";
    private DateUtils dateUtils = new DateUtils();
    
    /** Creates a new instance of AddRecipients */
    public AddRecipientsHeaderController (AwardBaseBean awardBaseBean , char funType) {
        super(awardBaseBean);
        queryEngine = QueryEngine.getInstance ();
        coeusMessageResources = CoeusMessageResources.getInstance();
        this.functionType = funType;
        
        registerComponents();
        setFormData(null);
        postInitComponents();
        setFunctionType(funType);
    }
    
    public void postInitComponents(){
        awardAddRecipientsController = new AwardAddRecipientsController(awardBaseBean,functionType);
        awardAddRecipientsController.registerObserver(this);
        awardAddRecipientsForm = new AwardAddRecipientsForm();
        
        
        /*changed code*/
        awardAddRecipientsForm = (AwardAddRecipientsForm)awardAddRecipientsController.getControlledUI();
        awardAddRecipientsForm.lblAwardContactList.setForeground (new java.awt.Color(255,0,51));
        awardAddRecipientsForm.lblContactDetails.setForeground (new java.awt.Color(255,0,51));
        
        addRecipientsHeaderForm.pnl.add(awardAddRecipientsController.getControlledUI());
        dlgAddRecipientsHeaderForm = new CoeusDlgWindow(mdiForm);
        dlgAddRecipientsHeaderForm.setResizable(false);
        dlgAddRecipientsHeaderForm.setModal(true);
        dlgAddRecipientsHeaderForm.getContentPane().add(addRecipientsHeaderForm);
        
        dlgAddRecipientsHeaderForm.setTitle(WINDOW_TITLE);
        dlgAddRecipientsHeaderForm.setFont(CoeusFontFactory.getLabelFont());
        dlgAddRecipientsHeaderForm.setSize(WIDTH, HEIGHT);
        
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension dlgSize = dlgAddRecipientsHeaderForm.getSize();
        dlgAddRecipientsHeaderForm.setLocation(screenSize.width/2 - (dlgSize.width/2),
        screenSize.height/2 - (dlgSize.height/2));
        
         dlgAddRecipientsHeaderForm.addComponentListener(
            new ComponentAdapter(){
                public void componentShown(ComponentEvent e){
                    requestDefaultFocus();
                }
        });
        
        dlgAddRecipientsHeaderForm.addEscapeKeyListener(new AbstractAction("escPressed"){
            public void actionPerformed(ActionEvent ae){
                   dlgAddRecipientsHeaderForm.dispose ();
            }
        });
       
        
        dlgAddRecipientsHeaderForm.addWindowListener(new WindowAdapter(){
             public void windowOpening(WindowEvent we){
             }
             public void windowClosing(WindowEvent we){
                //    performCancelAction();
             }
        });
     //code for disposing the window ends
        
        
        
        dlgAddRecipientsHeaderForm.pack();
    }
    
    public void requestDefaultFocus(){    
        awardAddRecipientsForm.btnCancel.requestFocus ();
    }
    
    public void display() {
        dlgAddRecipientsHeaderForm.setVisible(true);
    }
    
    public CoeusVector getSelectedPersons(){
        return   cvSelectedPersons;
    }
    
    public void formatFields() {
    }
    
    public Component getControlledUI() {
        return addRecipientsHeaderForm;
    }
    
    public Object getFormData() {
        return null;
    }
    
    public void registerComponents() {
        addRecipientsHeaderForm = new AddRecipientsHeaderForm();
    }
    
    public void saveFormData() {
    }
    
    public void setFormData(Object data) {
        cvAwardDetailsBean = new CoeusVector();   
        AwardDetailsBean awardDetailsBean = new AwardDetailsBean(); 
        try{
            /*For getting Award Details Bean to set Award No, Seq No, Sponsor No*/
            cvAwardDetailsBean = queryEngine.executeQuery (
            queryKey,AwardDetailsBean.class, CoeusVector.FILTER_ACTIVE_BEANS);
            
        }catch (CoeusException coeusException){
            coeusException.printStackTrace ();
        }
        awardDetailsBean = (AwardDetailsBean)cvAwardDetailsBean.get(0);
        addRecipientsHeaderForm.awardHeaderForm.setFormData(awardDetailsBean);
    }
    
    public boolean validate() throws CoeusUIException {
        return true;
    }
    
    public void actionPerformed(ActionEvent e) {
    }
    
    public void setLabelData(String reportClass,String reportType){
        addRecipientsHeaderForm.lblClassValue.setText (reportClass);
        addRecipientsHeaderForm.lblTypeValue.setText (reportType);
    }
    
    public void update (Observable o, Object arg) {
        if (arg instanceof CoeusVector || arg instanceof String){
            if(arg.equals (EMPTY)){
                dlgAddRecipientsHeaderForm.dispose();
            }else{
                cvSelectedPersons = (CoeusVector)arg;
                if(cvSelectedPersons != null){
                    dlgAddRecipientsHeaderForm.dispose();
                }
            }
        }
    }
    
    /** Method to clean all objects */
    public void cleanUp() {
        
        //Bug Fix:Performance Issue (Out of memory) Start 1
        awardAddRecipientsController.unRegisterObserver(this);
        awardAddRecipientsController.cleanUp();
        awardAddRecipientsController = null;
        //Bug Fix:Performance Issue (Out of memory) End 1

        awardContactBean = null;
        dlgAddRecipientsHeaderForm = null;
        mdiForm = null;
        coeusMessageResources = null;
        addRecipientsHeaderForm = null;
        awardAddRecipientsForm = null;
        queryEngine = null;
        cvSelectedPersons = null;
        contactsSelectionModel = null;
        cvAwardDetailsBean = null;
        cvContactType = null;
        cvAwardContactBean = null;
       
        dateUtils = null;
    }
    
}
