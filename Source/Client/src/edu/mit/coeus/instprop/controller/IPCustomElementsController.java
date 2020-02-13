/*
 * IPCustomElementsController.java
 *
 * Created on May 25, 2004, 12:51 PM
 */

package edu.mit.coeus.instprop.controller;


/**
 *
 * @author  ravikanth
 */

import edu.mit.coeus.utils.query.QueryEngine;
import edu.mit.coeus.instprop.bean.*;
import edu.mit.coeus.exception.*;
import edu.mit.coeus.utils.*;
import edu.mit.coeus.utils.customelements.CustomElementsForm;
import edu.mit.coeus.customelements.bean.CustomElementsInfoBean;

import java.util.*;

public class IPCustomElementsController extends InstituteProposalController 
 implements TypeConstants {

    private QueryEngine queryEngine;
    private char functionType;
    private InstituteProposalBean instituteProposalBean; 
    private CustomElementsForm customElementsForm;
    private CoeusVector columnValues;
    private InstituteProposalBaseBean instituteProposalBaseBean;
    
    /** Creates a new instance of IPCustomElementsController */
    public IPCustomElementsController(InstituteProposalBaseBean instituteProposalBaseBean, char functionType) {
        super(instituteProposalBaseBean);
        this.functionType=functionType;
        customElementsForm = new CustomElementsForm();
        queryEngine = QueryEngine.getInstance();
        registerComponents();

    }
    
    public void display() {
    }
    
    public void formatFields() {
    }
    
    public java.awt.Component getControlledUI() {
        return customElementsForm;
    }
    
    //Case :#3149 – Tabbing between fields does not work on others tabs - Start
    /**
     * Method to set focus for the table
     */
    public void setTableFocus(){
        customElementsForm.setTableFocus();
    }
    /**
     * Method to get CustomElementsForm object
     */
    public CustomElementsForm getCustomElementsForm(){
        return customElementsForm;
    }
    /**
     * Method to set tabbing order in the table for other tab
     */
    public void setTabFocus(){
        customElementsForm.setTabFocus();
    }
    // Case :#3149 - End
    
    public Object getFormData() {
        return null;
    }
    
    public void registerComponents() {
    }
    
    public void saveFormData() {
        if( isDataChanged() ) {
            Vector genericColumnValues = customElementsForm.getOtherColumnElementData();
            if( genericColumnValues != null && genericColumnValues.size() > 0 ){
                CustomElementsInfoBean genericCustElementsBean = null;
                int dataSize = genericColumnValues.size();
                for( int indx = 0; indx < dataSize; indx++ ) {
                    genericCustElementsBean = (CustomElementsInfoBean)genericColumnValues.get(indx);
                    InstituteProposalCustomDataBean instPropCustBean 
                        = new InstituteProposalCustomDataBean(genericCustElementsBean);
                    if( INSERT_RECORD.equals(genericCustElementsBean.getAcType()) ){
                        instPropCustBean.setProposalNumber( this.instituteProposalBaseBean.getProposalNumber() );
                        instPropCustBean.setSequenceNumber( this.instituteProposalBaseBean.getSequenceNumber() );
                    }else{
                        if( genericCustElementsBean instanceof InstituteProposalCustomDataBean ) {
                            InstituteProposalCustomDataBean oldInstPropCustBean = 
                                (InstituteProposalCustomDataBean)genericCustElementsBean;
                            instPropCustBean.setProposalNumber( oldInstPropCustBean.getProposalNumber() );
                            instPropCustBean.setSequenceNumber( oldInstPropCustBean.getSequenceNumber() );

                        }else{
                            continue;
                        }

                    }
                    try{
                        String custAcType = instPropCustBean.getAcType();
                        if( UPDATE_RECORD.equals(custAcType) ){
                            queryEngine.update(queryKey, instPropCustBean);
                        }else if( INSERT_RECORD.equals(custAcType)){
                            queryEngine.insert(queryKey, instPropCustBean);
                        }else if( DELETE_RECORD.equals(custAcType)){
                            queryEngine.delete(queryKey, instPropCustBean);
                        }
                    }catch ( CoeusException ce ) {
                        ce.printStackTrace();
                    }

                }
            }
        }
    }
    
    public void setFormData(Object instituteProposalBaseBean) {
        this.instituteProposalBaseBean=(InstituteProposalBaseBean)instituteProposalBaseBean;        
        try{
            columnValues = queryEngine.executeQuery(queryKey, InstituteProposalCustomDataBean.class, CoeusVector.FILTER_ACTIVE_BEANS);        
            customElementsForm.setFunctionType(functionType);
            customElementsForm.setPersonColumnValues(columnValues);
            customElementsForm.setCanMaintainProposal(true);
            customElementsForm.setSaveRequired(false);
        }catch ( CoeusException ce ) {
            ce.printStackTrace();
        }
    }
    
    public boolean validate() throws CoeusUIException {
        try {
            boolean isValid=customElementsForm.validateData();
            return isValid;
        } catch (Exception e) {
             edu.mit.coeus.exception.CoeusUIException coeusUIException = new edu.mit.coeus.exception.CoeusUIException(e.getMessage());
            coeusUIException.setTabIndex(10); // JM 5-22-2013 changed index from 8 to 10
             throw coeusUIException;
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
            setFormData(instituteProposalBaseBean);
            //cvDeletedData.clear();
            
            setRefreshRequired(false);
        }
    }    
    
    public boolean isDataChanged(){
        return customElementsForm.isSaveRequired();
    }
}
