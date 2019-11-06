/*
 * SubcontractOtherController.java
 *
 * Created on September 27, 2004, 2:17 PM
 */

package edu.mit.coeus.subcontract.controller;

import edu.mit.coeus.subcontract.bean.SubContractBean;
import edu.mit.coeus.subcontract.bean.SubContractCustomDataBean;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.utils.TypeConstants;
import edu.mit.coeus.utils.query.QueryEngine;
import edu.mit.coeus.utils.customelements.CustomElementsForm;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.customelements.bean.CustomElementsInfoBean;

import java.util.Vector;

/**
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 * @author  jobinelias
 */
public class SubcontractOtherController extends SubcontractController implements TypeConstants {
	
	private QueryEngine queryEngine;
        private CustomElementsForm customElementsForm;
        private CoeusVector columnValues;
    	private SubContractBean subContractBean;
	boolean saveRequired;
	
    /* JM 2-27-2015 modifications to allow tab access if user has only this right */
    private boolean userHasModify = false;
    private boolean userHasCreate = false;
    /* JM END */
	
	/**
	 * Creates a new instance of SubcontractOtherController
	 * @param subContractBean SubContractBean
	 * @param functionType char
	 */
	public SubcontractOtherController(SubContractBean subContractBean, char functionType) {
		super(subContractBean);
		
		/* JM 2-27-2015 no access if not modifier */
        userHasModify = subContractBean.getHasModify();
        userHasCreate = subContractBean.getHasCreate();
		if (!userHasModify && !userHasCreate) {
			functionType = DISPLAY_SUBCONTRACT;
		}
		/* JM END */ 
		
		setFunctionType(functionType);
		customElementsForm = new CustomElementsForm();
        queryEngine = QueryEngine.getInstance();
        registerComponents();
        setFormData(subContractBean);
	}
	
	/**
	 * display method
	 */	
	public void display() {
	}
	
	 //Case :#3149 – Tabbing between fields does not work on others tabs - Start
        /**
         *Method to get CustomElementsForm object
         */
        public CustomElementsForm getCustomElementsForm(){
            return customElementsForm;
        }
        //Case :#3149 - End

	/**
	 * formatting the fields depends on the mode
	 */	
	public void formatFields() {
	}
	
	/**
	 * getting the UI
	 * @return Component
	 */	
	public java.awt.Component getControlledUI() {
		return customElementsForm;
	}
	
	/**
	 * getting the form data
	 * @return Object
	 */	
	public Object getFormData() {
		return null;
	}
	
	/**
	 * registering the components
	 */	
	public void registerComponents() {
	}
	
	/**
	 * saving the form data
	 * @throws CoeusException CoeusException
	 */	
	public void saveFormData() throws edu.mit.coeus.exception.CoeusException {
            //Modified for COEUSDEV-413 : Subcontract Custom data bug - Data getting wiped out - Start
//            if( isDataChanged() ){
		if( isDataChanged() || getFunctionType() == NEW_ENTRY_SUBCONTRACT || getFunctionType() == NEW_SUBCONTRACT) { //COEUSDEV-413 : End
            Vector genericColumnValues = customElementsForm.getOtherColumnElementData();
			if( genericColumnValues != null && genericColumnValues.size() > 0 ){
				CustomElementsInfoBean genericCustElementsBean = null;
				int dataSize = genericColumnValues.size();
				for( int indx = 0; indx < dataSize; indx++ ) {
					genericCustElementsBean = (CustomElementsInfoBean)genericColumnValues.get(indx);
					SubContractCustomDataBean subContractCustomDataBean
					= new SubContractCustomDataBean(genericCustElementsBean);
                                        SubContractCustomDataBean oldSubContractCustomDataBean = (SubContractCustomDataBean)genericColumnValues.get(indx);
					if(getFunctionType() == NEW_ENTRY_SUBCONTRACT) {
						subContractCustomDataBean.setAcType("I");
//						subContractCustomDataBean.setSubContractCode(this.subContractBean.getSubContractCode());
//						subContractCustomDataBean.setSequenceNumber(this.subContractBean.getSequenceNumber());
					}
					if( INSERT_RECORD.equals(subContractCustomDataBean.getAcType()) ) {
						subContractCustomDataBean.setSubContractCode(this.subContractBean.getSubContractCode());
						subContractCustomDataBean.setSequenceNumber(this.subContractBean.getSequenceNumber());
                                                
					} else {
						if( genericCustElementsBean instanceof SubContractCustomDataBean ) {
//							SubContractCustomDataBean oldSubContractCustomDataBean =
//							(SubContractCustomDataBean)genericCustElementsBean;
							subContractCustomDataBean.setAcType(genericCustElementsBean.getAcType());
							subContractCustomDataBean.setSubContractCode(oldSubContractCustomDataBean.getSubContractCode());
							subContractCustomDataBean.setSequenceNumber(oldSubContractCustomDataBean.getSequenceNumber());
							
						} else {
							continue;
						}
						
					}
					try {
						String custAcType = subContractCustomDataBean.getAcType();
						if( UPDATE_RECORD.equals(custAcType) ){
//							subContractCustomDataBean.setSubContractCode(this.subContractBean.getSubContractCode());
//							subContractCustomDataBean.setSequenceNumber(this.subContractBean.getSequenceNumber());
							queryEngine.update(queryKey, subContractCustomDataBean);
						}else if( INSERT_RECORD.equals(custAcType)){
							subContractCustomDataBean.setSubContractCode(this.subContractBean.getSubContractCode());
							subContractCustomDataBean.setSequenceNumber(this.subContractBean.getSequenceNumber());
							queryEngine.insert(queryKey, subContractCustomDataBean);
						}
					}catch ( CoeusException ce ) {
						ce.printStackTrace();
					}
				}
			}
			customElementsForm.setSaveRequired(false);
		}
	}
	
	/**
	 * setting up the form data
	 * @param subContractBean SubContractBean
	 */	
	public void setFormData(Object subContractBean) {
		this.subContractBean = (SubContractBean)subContractBean;
        try{
            columnValues = queryEngine.executeQuery(queryKey, SubContractCustomDataBean.class, CoeusVector.FILTER_ACTIVE_BEANS);        
            //Commented for COEUSDEV-413 : Subcontract Custom data bug - Data getting wiped out - Start
//            if(getFunctionType() == NEW_ENTRY_SUBCONTRACT) {
//                SubContractCustomDataBean subContractCustomDataBean;
//                for(int index = 0; index < columnValues.size(); index++) {
//                    subContractCustomDataBean = (SubContractCustomDataBean)columnValues.get(index);
//                    subContractCustomDataBean.setDefaultValue(subContractCustomDataBean.getColumnValue());
//                }//end for
//            }//end if NEW_CHILD_COPIED
            //COEUSDEV-413 : End
            customElementsForm.setFunctionType(getFunctionType());
            customElementsForm.setPersonColumnValues(columnValues);
            customElementsForm.setCanMaintainProposal(true);
            customElementsForm.setSaveRequired(false);
        }catch ( CoeusException ce ) {
            ce.printStackTrace();
        }
	}
	
	/**
	 * validate method
	 * @return boolean
	 * @throws CoeusUIException CoeusUIException
	 */	
	public boolean validate() throws edu.mit.coeus.exception.CoeusUIException {
		try {
            boolean isValid = customElementsForm.validateData();
            return isValid;
        } catch (Exception e) {
             CoeusOptionPane.showInfoDialog(e.getMessage());
             return false;
        }
	}
	
	/**
	 * refresh method
	 * @param refreshRequired boolean
	 */	
	public void setRefreshRequired(boolean refreshRequired) {
        super.setRefreshRequired(refreshRequired);
    }
    
	/**
	 * is refresh required method
	 * @return boolean
	 */	
    public boolean isRefreshRequired() {
        boolean retValue;
        retValue = super.isRefreshRequired();
        return retValue;
    }
    
	/**
	 * refresh method
	 */	
    public void refresh() {
        if (isRefreshRequired()) {
            setFormData(subContractBean);
                       
            setRefreshRequired(false);
        }
    }    
    
	/**
	 * checking for any data is changed
	 * @return boolean
	 */	
    public boolean isDataChanged(){
		
        return customElementsForm.isSaveRequired();
    }
	
	/**
	 * clean up method
	 */	
	public void cleanUp() {
		customElementsForm = null;
		columnValues = null;
	    subContractBean = null;
	}
}
