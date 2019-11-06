/*
 * AwardOtherController.java
 *
 * Created on June 29, 2004, 5:00 PM
 */

package edu.mit.coeus.award.controller;

/**
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 * @author  sharathk
 */

import edu.mit.coeus.utils.query.QueryEngine;
import edu.mit.coeus.award.bean.*;
// JM 10-8-2012 needed to roll custom data forward
import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
// JM END
import edu.mit.coeus.exception.*;
import edu.mit.coeus.utils.*;
import edu.mit.coeus.utils.customelements.CustomElementsForm;
import edu.mit.coeus.customelements.bean.CustomElementsInfoBean;
// JM 5-14-2012 added to resolved scrolling issues
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
// END JM
import java.util.*;
import javax.swing.JScrollPane;

public class AwardOtherController extends AwardController implements TypeConstants{
    
    private QueryEngine queryEngine;
    //private char functionType;
    private CustomElementsForm customElementsForm;

    private CoeusVector columnValues;
    private AwardBaseBean awardBaseBean; 
    
    //Bug Fix:Performance Issue (Out of memory) Start 1
    private JScrollPane jscrPn;
    //Bug Fix:Performance Issue (Out of memory) End 1
    
    // JM 6-27-2012 constants to hold function for custom data roll forward
    private static final char GET_AWARD_CUSTOM_FORWARD = '7';
    private static final String AWARD_MAINTENANCE_SERVLET = CoeusGuiConstants.CONNECTION_URL+"/AwardMaintenanceServlet";
    private static final String GET_PARAMETER_VALUE = "GET_PARAMETER_VALUE";   
    private static final String COEUS_FUNCTIONS_SERVLET = CoeusGuiConstants.CONNECTION_URL+"/coeusFunctionsServlet";
    private static final String AWARD_CUSTOM_FIELDS_FORWARD = "AWARD_CUSTOM_FIELDS_FORWARD";
    // JM END
    
    /** Creates a new instance of AwardOtherController */
    public AwardOtherController(AwardBaseBean awardBaseBean, char functionType) {
        super(awardBaseBean);
        setFunctionType(functionType);
        customElementsForm = new CustomElementsForm();
        //JIRA Case COEUSDEV-160, COEUSDEV-177 - START
        jscrPn = new JScrollPane(customElementsForm);
        //JIRA Case COEUSDEV-160, COEUSDEV-177 - END
        queryEngine = QueryEngine.getInstance();
        registerComponents();
        setFormData(awardBaseBean);
    }
    
    public void display() {
    }
    
    public void formatFields() {
    }
    
    public java.awt.Component getControlledUI() {
        
        //Bug Fix:Performance Issue (Out of memory) Start 2
        //return customElementsForm;
        //JIRA Case COEUSDEV-160, COEUSDEV-177 - START
        //jscrPn = new JScrollPane(customElementsForm);
        //JIRA Case COEUSDEV-160, COEUSDEV-177 - END
        return jscrPn;
        //Bug Fix:Performance Issue (Out of memory) End 2
    }
    
    //Case :#3149 � Tabbing between fields does not work on others tabs - Start
    /**
     * Method to get the CustomElementsForm Object
     */
    public CustomElementsForm getCustomElementsForm(){
        return customElementsForm;
    }
    //Case :#3149 - end
    
    public Object getFormData() {
        return null;
    }
    
    public void registerComponents() {
    }
    
    public void saveFormData() {
        //Bug fix : in new modes the other data was not getting saved
        if( isDataChanged() || getFunctionType() == NEW_AWARD || getFunctionType() == NEW_CHILD ||
        getFunctionType() == NEW_CHILD_COPIED || getFunctionType() == NEW_ENTRY) {
            Vector genericColumnValues = customElementsForm.getOtherColumnElementData();
            if( genericColumnValues != null && genericColumnValues.size() > 0 ){
                CustomElementsInfoBean genericCustElementsBean = null;
                int dataSize = genericColumnValues.size();
                for( int indx = 0; indx < dataSize; indx++ ) {
                    genericCustElementsBean = (CustomElementsInfoBean)genericColumnValues.get(indx);
                    AwardCustomDataBean awardCustomDataBean 
                        = new AwardCustomDataBean(genericCustElementsBean);
                    if( INSERT_RECORD.equals(genericCustElementsBean.getAcType()) ){
                        awardCustomDataBean.setMitAwardNumber(this.awardBaseBean.getMitAwardNumber());
                        //awardCustomDataBean.setSequenceNumber( this.awardBaseBean.getSequenceNumber());
                    }else{
                        if( genericCustElementsBean instanceof AwardCustomDataBean ) {
                            AwardCustomDataBean oldAwardCustomBean = 
                                (AwardCustomDataBean)genericCustElementsBean;
                            awardCustomDataBean.setMitAwardNumber( oldAwardCustomBean.getMitAwardNumber() );
                            //awardCustomDataBean.setSequenceNumber( oldAwardCustomBean.getSequenceNumber() );

                        }else{
                            continue;
                        }

                    }
                    try{
                    	// JM 7-11-2012 added this because new records not saving
                    	if (awardCustomDataBean.getAcType() == null) {
                    		awardCustomDataBean.setAcType(INSERT_RECORD);
                    	}
                    	// JM END
                        String custAcType = awardCustomDataBean.getAcType();
                        if( UPDATE_RECORD.equals(custAcType) ){
                            awardCustomDataBean.setSequenceNumber(this.awardBaseBean.getSequenceNumber());
                            queryEngine.update(queryKey, awardCustomDataBean);
                        }else if( INSERT_RECORD.equals(custAcType)){
                            awardCustomDataBean.setSequenceNumber(this.awardBaseBean.getSequenceNumber());
                            queryEngine.insert(queryKey, awardCustomDataBean);
                        }else if( DELETE_RECORD.equals(custAcType)){
                            queryEngine.delete(queryKey, awardCustomDataBean);
                        }
                    }catch ( CoeusException ce ) {
                        ce.printStackTrace();
                    }

                }
            }
        }
    }
    
    public void setFormData(Object awardBaseBean) {
        this.awardBaseBean=(AwardBaseBean)awardBaseBean;
        try{
            columnValues = queryEngine.executeQuery(queryKey, AwardCustomDataBean.class, CoeusVector.FILTER_ACTIVE_BEANS);        
            // JM 6-29-2012 set custom data to be rolled forward
            AwardCustomDataBean bean;
            String[] customForward = getParameterValues();
            for(int index = 0; index < columnValues.size(); index++) {
                bean = (AwardCustomDataBean)columnValues.get(index);
                for(int c = 0; c < customForward.length; c++) {
	                if (bean.getColumnName().contentEquals(customForward[c])) {
	                	bean.setColumnValue(getAwardCustomForward(bean.getColumnName()));
	                }
	                bean.setDefaultValue(bean.getColumnValue());
                }
            }
            // JM END
            
            //if its a new bean it'll display default value. but if its in new child copied mode
            //then the default value will be the value from parent
            if(getFunctionType() == NEW_CHILD_COPIED) {
                AwardCustomDataBean awardCustomDataBean;
                for(int index = 0; index < columnValues.size(); index++) {
                    awardCustomDataBean = (AwardCustomDataBean)columnValues.get(index);
                    awardCustomDataBean.setDefaultValue(awardCustomDataBean.getColumnValue());
                }//end for
            }//end if NEW_CHILD_COPIED
            
            customElementsForm.setFunctionType(getFunctionType());
            customElementsForm.setPersonColumnValues(columnValues);
            customElementsForm.setCanMaintainProposal(true);
            customElementsForm.setSaveRequired(false);
        }catch ( CoeusException ce ) {
            ce.printStackTrace();
        }
    }
    
    // JM 7-6-2012 method to get values from parameters for fields to roll forward
    private String[] getParameterValues() {
    	String values[] = new String[0];
    	String retVal = new String();
    	
    	// Create the requester
    	RequesterBean requester = new RequesterBean();
        ResponderBean responder;
        
        // Set parameters for the requester
        Vector dataObjects = new Vector();
        dataObjects.add(AWARD_CUSTOM_FIELDS_FORWARD);
        requester.setDataObjects(dataObjects);
        requester.setDataObject(GET_PARAMETER_VALUE);
        AppletServletCommunicator comm = new AppletServletCommunicator(COEUS_FUNCTIONS_SERVLET, requester);
        
        // Execute the call and get the response
        comm.send();
        responder = comm.getResponse();
        
        // Check for a response and process
        if (responder.getDataObject() != null) {
			retVal = (String) responder.getDataObject();
			values = retVal.split(",");
       
	        for (int i = 0; i < values.length; i++) {
	        	values[i] = values[i].trim();
	        }
        }
        
    	return values;
    }
    // JM END
    
    // JM 6-27-2012 method to get custom data to roll forward
    private String getAwardCustomForward(String colName) {
    	
    	getParameterValues();
    	
        Vector result = new Vector();
    	String colVal = new String();
    	HashMap row = new HashMap();
    	
    	// Create the requester
    	RequesterBean requester = new RequesterBean();
        ResponderBean responder;
        
        // Set parameters for the requester
        Vector dataObjects = new Vector();
        dataObjects.add(awardBaseBean.getMitAwardNumber());
        dataObjects.add(colName);
        requester.setDataObjects(dataObjects);
        requester.setFunctionType(GET_AWARD_CUSTOM_FORWARD);
        AppletServletCommunicator comm = new AppletServletCommunicator(AWARD_MAINTENANCE_SERVLET, requester);
        
        // Execute the call and get the response
        comm.send();
        responder = comm.getResponse();
        
        // Check for a response and process
        if (responder.getDataObject() != null) {
        	colVal = (String) responder.getDataObject();
        }
        else {
        	//do nothing
        }
        return colVal;    	
    }
    // JM END
    
    public boolean validate() throws CoeusUIException {
        try {
            boolean isValid=customElementsForm.validateData();
            return isValid;
        } catch (Exception e) {
             //edu.mit.coeus.exception.CoeusUIException coeusUIException = new edu.mit.coeus.exception.CoeusUIException(e.getMessage());
             //coeusUIException.setTabIndex(8);
            //throw coeusUIException; 
            CoeusOptionPane.showInfoDialog(e.getMessage());
            return false;
        }
        
    }
    
    public void setRefreshRequired(boolean refreshRequired) {
        super.setRefreshRequired(refreshRequired);
    }
    
    public boolean isRefreshRequired() {
        boolean retValue;
        retValue = super.isRefreshRequired();
        return retValue;
    }
    
    public void refresh() {
        if (isRefreshRequired()) {
            setFormData(awardBaseBean);
            //cvDeletedData.clear();
            
            setRefreshRequired(false);
        }
    }    
    
    public boolean isDataChanged(){
        return customElementsForm.isSaveRequired();
    }
    
    //Bug Fix:Performance Issue (Out of memory) Start 3
    public void cleanUp(){
        //System.out.println("Custom Elements Clean Up");
        jscrPn.remove(customElementsForm);
        jscrPn = null;
        
        customElementsForm = null;
        columnValues = null;
        awardBaseBean = null; 
    }
    //Bug Fix:Performance Issue (Out of memory) End 3
}