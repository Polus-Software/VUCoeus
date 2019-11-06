/**
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

/*
 * IPInvestigatorController.java
 *
 * Created on May 10, 2004, 1:20 PM
 *
 * @author  Sreenath
 */

package edu.mit.coeus.instprop.controller;

import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.exception.CoeusUIException;
import edu.mit.coeus.instprop.bean.InstituteProposalBaseBean;
import edu.mit.coeus.instprop.bean.InstituteProposalKeyPersonBean;
import edu.mit.coeus.bean.KeyPersonBean;
import edu.mit.coeus.bean.KeyPersonUnitBean;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.TypeConstants;
import edu.mit.coeus.utils.keyperson.KeyPersonController;
import edu.mit.coeus.utils.query.Equals;
import edu.mit.coeus.utils.query.QueryEngine;
import java.awt.Component;
import java.util.Iterator;

public class IPKeyPersonController extends KeyPersonController {
    
    private QueryEngine queryEngine;
    private InstituteProposalBaseBean ipBaseBean;

    //For getting the data from the bean
    private CoeusVector cvUnit;
    private CoeusVector cvKeyPerson;
    private String queryKey;
    private static int KEY_PERSON_TAB_INDEX = 3;
    /** Creates a new instance of IPInvestigatorController
     * @param InstituteProposalBaseBean ipBaseBean
     * @param String quesryKey
     * @param char functionType
     */
    public IPKeyPersonController(InstituteProposalBaseBean ipBaseBean,String queryKey,char functionType) {
        super(ipBaseBean.getProposalNumber(), CoeusGuiConstants.INSTITUTE_PROPOSAL_MODULE);
        this.ipBaseBean = ipBaseBean;
        this.queryKey = queryKey;
        setFunctionType(functionType);
        queryEngine = QueryEngine.getInstance();
        cvKeyPerson = new CoeusVector();
        setFormData();
    }
    
    /**
     * Updates the Query Engine with the modified InstituteProposalKeyPersonBeans
     * @return void
     */
    public void saveFormData() {
        if( isDataChanged() ) {
            try{
                 if( getFunctionType() == InstituteProposalController.NEW_INST_PROPOSAL){
                    //deleting all the investigators and units in query engine for new mode
                    //bcoz we may replace the data what we got from query engine with the new data.
                    //So the instance in query engine should be replaced with the new one.
                    Equals eqAllData = new Equals("proposalNumber",ipBaseBean.getProposalNumber());
                    queryEngine.removeData(queryKey,InstituteProposalKeyPersonBean.class,eqAllData);
                    queryEngine.removeData(queryKey,KeyPersonUnitBean.class,eqAllData);
                }
                cvKeyPerson = (CoeusVector)getFormData();
                if( cvKeyPerson != null ) {
                    String keyPersonAcType = null;
                    CoeusVector cvUnitQueryEngine= new CoeusVector();

                    for(int i=0;i<cvKeyPerson.size();i++)
                    { KeyPersonBean keyPerBeantmp = (KeyPersonBean)cvKeyPerson.get(i);
                      CoeusVector tmp=keyPerBeantmp.getKeyPersonsUnits();
                      if(tmp!=null){cvUnitQueryEngine.addAll(tmp);}
                    }
                    CoeusVector cvlist=new CoeusVector();
                    for(int i=0; i<cvUnitQueryEngine.size();i++)
                    {
                        KeyPersonUnitBean kPUB=(KeyPersonUnitBean)cvUnitQueryEngine.get(i);
                        String unitAcType = kPUB.getAcType();
                               if(unitAcType!=null)
                               {if(!cvlist.contains(kPUB)){
                                    kPUB.setProposalNumber(ipBaseBean.getProposalNumber());
                                    kPUB.setSequenceNumber(ipBaseBean.getSequenceNumber());
                                   // kPUB.setAw_UnitNumber(unitBean.getUnitNumber());
                                  // kPUB.setAw_PersonId(unitBean.getPersonId());
                                   cvlist.add(kPUB);}}
                    }
                     queryEngine.addCollection(queryKey, KeyPersonUnitBean.class, cvlist);
                    int keyPersonCount = cvKeyPerson.size();
                    for( int indx = 0; indx < keyPersonCount; indx++) {
                        KeyPersonBean keyPerBean = (KeyPersonBean)cvKeyPerson.get(indx);
                         CoeusVector cvUnits = keyPerBean.getKeyPersonsUnits();
                        
//
//                        CoeusVector cvIPInvUnits = null;
      //          if( cvUnits != null ) {
//                            cvIPInvUnits = new CoeusVector();
//                            int unitCount = cvUnits.size();
//                            for( int unitIndx = 0; unitIndx < unitCount; unitIndx++){
//                                KeyPersonUnitBean unitBean =
//                                    (KeyPersonUnitBean)cvUnits.get(unitIndx);
//                               KeyPersonUnitBean ipUnitBean = new KeyPersonUnitBean(unitBean);
//                                if( INSERT_RECORD.equals( unitBean.getAcType() ) ){
//                                    ipUnitBean.setProposalNumber(ipBaseBean.getProposalNumber());
//                                    ipUnitBean.setSequenceNumber(ipBaseBean.getSequenceNumber());
//                                    ipUnitBean.setAw_UnitNumber(unitBean.getUnitNumber());
//                                    ipUnitBean.setAw_PersonId(unitBean.getPersonId());
//                                }
//
//                                String unitAcType = ipUnitBean.getAcType();
//                                if( UPDATE_RECORD.equals(unitAcType) ){
//                                    queryEngine.update(queryKey,ipUnitBean );
//                                }else if( INSERT_RECORD.equals(unitAcType)){
//                                    queryEngine.insert(queryKey, ipUnitBean);
//                                }else if( DELETE_RECORD.equals(unitAcType)){
//                                    queryEngine.delete(queryKey, ipUnitBean);
//                                }
//                                cvIPInvUnits.add(ipUnitBean);
//                            }
 //                  }


                        InstituteProposalKeyPersonBean ipKeyPersonbean =
                                new InstituteProposalKeyPersonBean(keyPerBean);
                        if( INSERT_RECORD.equals( keyPerBean.getAcType() ) ){
                            ipKeyPersonbean.setProposalNumber(ipBaseBean.getProposalNumber());
                            ipKeyPersonbean.setSequenceNumber(ipBaseBean.getSequenceNumber());
                            ipKeyPersonbean.setAw_PersonId(keyPerBean.getPersonId());
                        }else{
                            if( keyPerBean instanceof InstituteProposalKeyPersonBean ){
                                InstituteProposalKeyPersonBean oldKeyPerBean =
                                        (InstituteProposalKeyPersonBean)keyPerBean;
                                ipKeyPersonbean.setProposalNumber(oldKeyPerBean.getProposalNumber());
                                ipKeyPersonbean.setSequenceNumber(oldKeyPerBean.getSequenceNumber());
                            }else{
                                continue;
                            }
                        }
                        
                        if(ipKeyPersonbean.getAcType()!= null){
                            if(ipKeyPersonbean.getAcType().equals(TypeConstants.UPDATE_RECORD)){
                                ipKeyPersonbean.setAcType(TypeConstants.DELETE_RECORD);
                                queryEngine.delete(queryKey, ipKeyPersonbean);
                                ipKeyPersonbean.setAcType(TypeConstants.INSERT_RECORD);
                            queryEngine.insert(queryKey, ipKeyPersonbean);
                            }else if(ipKeyPersonbean.getAcType().equals(TypeConstants.DELETE_RECORD)){
                                ipKeyPersonbean.setAcType(TypeConstants.DELETE_RECORD);
                                queryEngine.delete(queryKey, ipKeyPersonbean);
                            }else if(ipKeyPersonbean.getAcType().equals(TypeConstants.INSERT_RECORD)){
                                ipKeyPersonbean.setAcType(TypeConstants.INSERT_RECORD);
                                queryEngine.insert(queryKey, ipKeyPersonbean);
                            }
                        }
                    }
                }
            }catch(CoeusException ce ) {
                ce.printStackTrace();
            }
        }
    }
    
    /**
     * Gets the IP Key Person data from Quesry Engine and sets data to Key Person Form 
     * @return void
     */
    public void setFormData(){
        try{
            cvKeyPerson = queryEngine.executeQuery(queryKey,InstituteProposalKeyPersonBean.class,
                    CoeusVector.FILTER_ACTIVE_BEANS);            
            super.setModuleName(CoeusGuiConstants.INSTITUTE_PROPOSAL_MODULE);
            super.setModuleNumber(ipBaseBean.getProposalNumber());
            super.setSeqNo(String.valueOf(ipBaseBean.getSequenceNumber()));
            super.setKeyPersonTabIndex(KEY_PERSON_TAB_INDEX);
            super.setFormData(cvKeyPerson);
            super.setDataChanged(true);
        }catch(CoeusException ce ) {
            ce.printStackTrace();
            super.setFormData(null);
        }
    }
    
    /**
     * Checks all the required fields are filled with appropriate data.
     * @return boolean true if validation passed successfully
     */
    public boolean validate() throws CoeusUIException {
        return super.validate();
    }
    
    /** 
     * Getter for property queryKey.
     * @return Value of property queryKey.
     */
    public java.lang.String getQueryKey() {
        return queryKey;
    }
    
    /** 
     * Setter for property queryKey.
     * @param queryKey New value of property queryKey.
     */
    public void setQueryKey(java.lang.String queryKey) {
        this.queryKey = queryKey;
    }

    
    public void refresh(){
        if(isDataChanged() ) {
            setFormData();
            setDataChanged(false);
        }
    }

    /** 
     * Getter for property ipBaseBean.
     * @return Value of property ipBaseBean.
     */
    public edu.mit.coeus.instprop.bean.InstituteProposalBaseBean getIpBaseBean() {
        return ipBaseBean;
    }
    
    /** 
     * Setter for property ipBaseBean.
     * @param ipBaseBean New value of property ipBaseBean.
     */
    public void setIpBaseBean(edu.mit.coeus.instprop.bean.InstituteProposalBaseBean ipBaseBean) {
        this.ipBaseBean = ipBaseBean;
    }
    
    public Component getControlledUI(){
        return super.getControlledUI();
    }
    
}
