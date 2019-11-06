/** Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.rates.controller;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.util.Hashtable;
import javax.swing.AbstractAction;
import javax.swing.JOptionPane;
import javax.swing.event.ChangeListener;

import edu.mit.coeus.brokers.*;
import edu.mit.coeus.exception.*;
import edu.mit.coeus.gui.CoeusDlgWindow;
import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.rates.bean.InstituteRatesBean;
import edu.mit.coeus.rates.gui.RatesBaseWindow;
import edu.mit.coeus.utils.AppletServletCommunicator;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.KeyConstants;
import edu.mit.coeus.utils.TypeConstants;
import edu.mit.coeus.utils.query.*;
import java.awt.Cursor;

/**
 * RatesBaseWindowController.java
 * Created on August 17, 2004, 4:01 PM
 * @author  Vyjayanthi
 */
public class RatesBaseWindowController extends RatesController
implements ActionListener, ChangeListener {
    
    private RatesBaseWindow ratesBaseWindow;
    public CoeusDlgWindow dlgRates;
    private QueryEngine queryEngine;
    private int selectedTabIndex;
    private InstituteRatesController instituteRatesController;
    private InstRateClassTypesController instRateClassTypesController;
    private boolean hasMaintainCodeTablesRt;
    
    private static final String CONNECTION_STRING = CoeusGuiConstants.CONNECTION_URL +
        "/RatesMaintenanceServlet";
    
    private static final String COULD_NOT_CONTACT_SERVER = "Could not contact server";
    private static final int WIDTH = 650;
    private static final int HEIGHT = 450;
    
    private static final String AC_TYPE = "acType";
    private static final String RATE_CLASS_TYPES = "Rate Class/Types(s)";
    private static final String INSTITUTE_RATES = "Institute Rates";
    private static final char GET_RATE_DATA = 'A';
    private static final char UPDATE_RATE_DATA = 'B';
    
    /** Creates a new instance of RatesBaseWindowController
     * @param instituteRatesBean the institute rates bean
     */
    public  RatesBaseWindowController(InstituteRatesBean instituteRatesBean) {
        super(instituteRatesBean);
        this.instituteRatesBean = instituteRatesBean;
        initComponents();
        registerComponents();
        setFormData(null);
    }
    
    /** Initialize the form
     */
    private void initComponents() {
        ratesBaseWindow = new RatesBaseWindow();
        queryEngine = QueryEngine.getInstance();
        
        dlgRates = new CoeusDlgWindow(CoeusGuiConstants.getMDIForm(), true);
        dlgRates.getContentPane().add(ratesBaseWindow);
        dlgRates.setResizable(false);
        dlgRates.setDefaultCloseOperation(CoeusDlgWindow.DO_NOTHING_ON_CLOSE);
        dlgRates.setFont(CoeusFontFactory.getLabelFont());
        dlgRates.setSize(WIDTH, HEIGHT);
        
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension dlgSize = dlgRates.getSize();
        dlgRates.setLocation(screenSize.width/2 - (dlgSize.width/2),
        screenSize.height/2 - (dlgSize.height/2));
    }
    
    /** Displays the Form which is being controlled.
     */
    public void display() {
        dlgRates.show();
    }
    
    /** Perform field formatting.
     * enabling, disabling components depending on the different conditions
     */
    public void formatFields() {
        if( getFunctionType() == TypeConstants.DISPLAY_MODE ){
            ratesBaseWindow.btnOK.setEnabled(false);
        }
    }
    
    /** An overridden method of the controller
     * @return ratesBaseWindow returns the controlled form component
     */
    public java.awt.Component getControlledUI() {
        return ratesBaseWindow;
    }
    
    /** Returns the form data
     * @return returns the form data
     */
    public Object getFormData() {
        return null;
    }
    public void cleanUp(){
        instRateClassTypesController.cleanUp();
        instituteRatesController.cleanUp();
        
        //Bug Fix: 1742: Performance Fix - Start 3
        ratesBaseWindow.tbdPnRates.removeChangeListener(this);
        ratesBaseWindow.tbdPnRates.remove(instRateClassTypesController.getControlledUI());
        ratesBaseWindow.tbdPnRates.remove(instituteRatesController.getControlledUI());
        instRateClassTypesController = null;
        instituteRatesController = null;
        queryEngine.removeDataCollection(queryKey);
        //Bug Fix: 1742: Performance Fix - End 3
    }
    
    /** This method is used to set the listeners to the components.
     */
    public void registerComponents() {
        ratesBaseWindow.btnOK.addActionListener(this);
        ratesBaseWindow.btnCancel.addActionListener(this);
        
        dlgRates.addEscapeKeyListener(
            new AbstractAction("escPressed"){
                public void actionPerformed(java.awt.event.ActionEvent ae){
                    performWindowClosing();
                }
        });
        
        dlgRates.addWindowListener(new java.awt.event.WindowAdapter(){            
            
            public void windowOpened(java.awt.event.WindowEvent we) {
                requestDefaultFocus();
            }
            
            public void windowClosing(java.awt.event.WindowEvent we){
                performWindowClosing();
                return;
            }
        });
    }
    
    /**
     * This method is used to perform the Window closing operation. Before closing
     * the window it checks the saveRequired flag and the function type.
     * If the saveRequired is true then it saves the details to the
     * database else dispose this JDialog.
     */
    private void performWindowClosing() {
        if(getFunctionType() != TypeConstants.DISPLAY_MODE){
            if( isSaveRequired() ){
                int option = CoeusOptionPane.showQuestionDialog(
                    coeusMessageResources.parseMessageKey(
                    "saveConfirmCode.1002"),
                CoeusOptionPane.OPTION_YES_NO_CANCEL,
                CoeusOptionPane.DEFAULT_YES);
                switch( option ){
                    case ( JOptionPane.YES_OPTION ):
                        try{
                            saveRatesData();
                        }catch (CoeusUIException coeusUIException){
                            //Validation failed
                        }
                        break;
                    case ( JOptionPane.NO_OPTION ):
                        cleanUp();
                        dlgRates.dispose();
                        break;
                }
            }else{
                dlgRates.dispose();
            }
        }else{
            dlgRates.dispose();            
        }
        
    }
    
    /** Checks if data in QueryEngine is modifed and needs to be saved.
     * @return returns false if nothing to save, true otherwise
     */
    public boolean isSaveRequired(){

        //If data is modified in any of the tabs
        if( instRateClassTypesController.isSaveRequired() ||
        instituteRatesController.isSaveRequired() ){
            return true;
        }
        
        java.util.Enumeration enumeration =  queryEngine.getKeyEnumeration(queryKey);
        
        Equals eqInsert = new Equals(AC_TYPE, TypeConstants.INSERT_RECORD);
        Equals eqUpdate = new Equals(AC_TYPE, TypeConstants.UPDATE_RECORD);
        Equals eqDelete = new Equals(AC_TYPE, TypeConstants.DELETE_RECORD);
        
        Or insertOrUpdate = new Or(eqInsert, eqUpdate);
        Or insertOrUpdateOrDelete = new Or(insertOrUpdate, eqDelete);
        
        Object key;
        CoeusVector data;
        boolean ratesModified = false;
        try{
            while(enumeration.hasMoreElements()) {
                key = enumeration.nextElement();
                
                if(!(key instanceof Class)) continue;
                
                data = queryEngine.executeQuery(queryKey, (Class)key, insertOrUpdateOrDelete);
                if(! ratesModified) {
                    if(data != null && data.size() > 0) {
                        ratesModified = true;
                        break;
                    }
                }
            }
        }catch (CoeusException coeusException) {
            coeusException.printStackTrace();
        }
        return ratesModified;
    }
    
    /** To set the default focus for the components depending 
     * on the the function type */
    private void requestDefaultFocus() {
        if( getFunctionType() == TypeConstants.DISPLAY_MODE ){
            ratesBaseWindow.btnCancel.requestFocus();
        }else {
            ratesBaseWindow.btnOK.requestFocus();
        }
    }
    
    /** Get the rates data from the server
     */
    private void fetchRatesData() {
        RequesterBean requesterBean = new RequesterBean();
        requesterBean.setFunctionType(GET_RATE_DATA);
        requesterBean.setDataObject(instituteRatesBean);
        
        AppletServletCommunicator appletServletCommunicator = new 
            AppletServletCommunicator(CONNECTION_STRING, requesterBean);
        appletServletCommunicator.setRequest(requesterBean);
        appletServletCommunicator.send();
        ResponderBean responderBean = appletServletCommunicator.getResponse();
        
        if(responderBean == null) {
            //Could not contact server.
            CoeusOptionPane.showErrorDialog(COULD_NOT_CONTACT_SERVER);
            return;
        }
        
        if(responderBean.isSuccessfulResponse()) {
            Hashtable ratesData = (Hashtable)responderBean.getDataObject();
            hasMaintainCodeTablesRt = ((Boolean)ratesData.get("HAS_OSP_RIGHT")).booleanValue();
            if( hasMaintainCodeTablesRt ){
                setFunctionType(TypeConstants.MODIFY_MODE);
            }else {
                setFunctionType(TypeConstants.DISPLAY_MODE);
            }
            //queryKey = RATES + ratesData.get("PARENT_UNIT_NUMBER").toString();

            //Set title
            dlgRates.setTitle(RATES + " for Unit " + instituteRatesBean.getUnitNumber());
            queryEngine.addDataCollection(queryKey, ratesData);

        }else {
            //Server Error
            CoeusOptionPane.showErrorDialog(responderBean.getMessage());
            return;
        }
    }
    
    /** Saves the Form Data
     * @throws CoeusException exception that occured
     */
    public void saveFormData() throws edu.mit.coeus.exception.CoeusException {
        instRateClassTypesController.saveFormData();
        instituteRatesController.saveFormData();        
    }
    
    /** Saves rates data to the server
     * @throws CoeusUIException if any exception occurs.
     */
    private void saveRatesData() throws CoeusUIException {
        if(getFunctionType() == TypeConstants.DISPLAY_MODE) {
            dlgRates.dispose();
            return ;
        }
        try{
            //Bug Fix id 1654
            //dlgRates.setCursor(new Cursor(Cursor.WAIT_CURSOR));
            try{
                if(!validate()) {
                    throw new CoeusUIException();
                }
            }catch (CoeusUIException coeusUIException) {
                throw coeusUIException;
            }

            saveFormData();
            
            Hashtable ratesData = new Hashtable();
            CoeusVector cvData = queryEngine.getDetails(queryKey, InstituteRatesBean.class);
            
            //Bug Fix: 1742: Performance Fix - Start 1
            //cvData.sort("acType");
            //Bug Fix: 1742: Performance Fix - End 1
            
            ratesData.put(InstituteRatesBean.class, cvData);

            cvData = queryEngine.getDetails(queryKey, KeyConstants.RATE_CLASS_DATA);
            ratesData.put(KeyConstants.RATE_CLASS_DATA, cvData);

            cvData = queryEngine.getDetails(queryKey, KeyConstants.RATE_TYPE_DATA);
            ratesData.put(KeyConstants.RATE_TYPE_DATA, cvData);
         
            RequesterBean requesterBean = new RequesterBean();
            requesterBean.setFunctionType(UPDATE_RATE_DATA);
            requesterBean.setDataObject(ratesData);
            
            AppletServletCommunicator appletServletCommunicator = new 
                AppletServletCommunicator(CONNECTION_STRING, requesterBean);
            appletServletCommunicator.send();
            ResponderBean responderBean = appletServletCommunicator.getResponse();
            
            if(responderBean == null) {
                //Could not contact server.
                CoeusOptionPane.showErrorDialog(COULD_NOT_CONTACT_SERVER);
                throw new CoeusUIException();
            }
            
            if(responderBean.isSuccessfulResponse()) {
                cleanUp();
                dlgRates.dispose();
            }else {
                //Server Error
                CoeusOptionPane.showErrorDialog(responderBean.getMessage());
                throw new CoeusUIException();
            }
        }catch (CoeusException coeusException){
            coeusException.printStackTrace();
        }
    }

    /** This method is used to set the form data specified in
     * <CODE> data </CODE>
     * @param data data to set to the form
     */
    public void setFormData(Object data) {
        fetchRatesData();

        if( instituteRatesController == null ){
            instituteRatesController = new InstituteRatesController(instituteRatesBean, getFunctionType());
        }
        if( instRateClassTypesController == null ){
            instRateClassTypesController = new InstRateClassTypesController(instituteRatesBean, getFunctionType());
        }
        ratesBaseWindow.tbdPnRates.addTab(RATE_CLASS_TYPES, instRateClassTypesController.getControlledUI());
        ratesBaseWindow.tbdPnRates.addTab(INSTITUTE_RATES, instituteRatesController.getControlledUI());
        ratesBaseWindow.tbdPnRates.addChangeListener(this);
    }
    
    /** Validate the form data/Form and returns true if
     * validation is through else returns false.
     * @throws CoeusUIException if some exception occurs or some validation fails.
     * @return true if
     * validation is through else returns false.
     */
    public boolean validate() throws edu.mit.coeus.exception.CoeusUIException {
        boolean validate = true;
        if( !instRateClassTypesController.validate() || 
        !instituteRatesController.validate() ){
            validate = false;
        }
        return validate;
    }

    /** This method triggers all actions based on the event occured
     * @param actionEvent takes the actionEvent */
    public void actionPerformed(java.awt.event.ActionEvent actionEvent) {
        Object source = actionEvent.getSource();
        try{
            //blockEvents(true);
            dlgRates.setCursor(new Cursor(Cursor.WAIT_CURSOR));
            if( source.equals(ratesBaseWindow.btnOK) ) {
                try{
                    saveRatesData();
                }catch (CoeusUIException coeusUIException){
                    //Validation failed
                }
            }else if( source.equals(ratesBaseWindow.btnCancel) ) {
                performWindowClosing();
            }
        }finally{
            dlgRates.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
           // blockEvents(false);
            
            //Bug Fix: 1742: Performance Fix - Start 2
            dlgRates.remove(ratesBaseWindow);
            ratesBaseWindow = null;
            dlgRates = null;
            //Bug Fix: 1742: Performance Fix - End 2
            
        }
    }
    
    /** This method triggers all actions based on the event occured
     * @param changeEvent takes the changeEvent
     */
    public void stateChanged(javax.swing.event.ChangeEvent changeEvent) {
        if(getFunctionType() != TypeConstants.DISPLAY_MODE) {
            try{
                instRateClassTypesController.saveFormData();
                instituteRatesController.saveFormData();
            }catch (CoeusException coeusException) {
                coeusException.printStackTrace();
                CoeusOptionPane.showErrorDialog(coeusException.getMessage());
            }
        }
    }
    
}
