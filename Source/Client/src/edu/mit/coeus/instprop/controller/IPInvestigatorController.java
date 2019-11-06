/*
 * IPInvestigatorController.java
 *
 * Created on May 10, 2004, 1:20 PM
 */

package edu.mit.coeus.instprop.controller;

import edu.mit.coeus.instprop.bean.*;
import edu.mit.coeus.bean.InvestigatorBean;
import edu.mit.coeus.bean.InvestigatorUnitBean;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.exception.CoeusUIException;
import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.IconRenderer;
import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.utils.investigator.*;
import edu.mit.coeus.utils.query.QueryEngine;
import edu.mit.coeus.utils.query.Equals;
import edu.mit.coeus.utils.TypeConstants;
import edu.mit.coeus.search.gui.CoeusSearch;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.exception.*;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.event.ListSelectionListener;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import java.util.Vector;
import java.util.HashMap;

import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

/**
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 * @author  ravikanth
 */


public class IPInvestigatorController extends InvestigatorController {
    
    // For querying 
    private QueryEngine queryEngine;
    //To check whether the data or screen is modified
    private boolean modified = false;
    private static final String EMPTY_STRING = "";
    private InstituteProposalBaseBean ipBaseBean;
        
    //For getting the data from the bean
    private CoeusVector cvInvestigator;
    private CoeusVector cvUnit;
    
    // Represents the bean class
    
    private InvestigatorBean investigatorBean;
    private InstituteProposalInvestigatorBean  ipInvestigatorsBean;
    
    private String queryKey;
    private CoeusMessageResources coeusMessageResources;
    
    private boolean refreshRequired;
    
    /** Creates a new instance of IPInvestigatorController 
     * @param InstituteProposalBaseBean ipBaseBean
     */
    public IPInvestigatorController(InstituteProposalBaseBean ipBaseBean,String queryKey,char functionType) {
        super(ipBaseBean.getProposalNumber(), CoeusGuiConstants.INSTITUTE_PROPOSAL_MODULE);
        this.ipBaseBean = ipBaseBean;
        this.queryKey = queryKey;
        setFunctionType(functionType);
        queryEngine = QueryEngine.getInstance();
        cvInvestigator = new CoeusVector();
        cvUnit = new CoeusVector();
        coeusMessageResources = CoeusMessageResources.getInstance();
        setFormData();
    }
    
    /***
     * save form data
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
                    queryEngine.removeData(queryKey,InstituteProposalInvestigatorBean.class,eqAllData);
                    queryEngine.removeData(queryKey,InstituteProposalUnitBean.class,eqAllData);
                }
                cvInvestigator = (CoeusVector)getFormData();
                if( cvInvestigator != null ) {
                    String invAcType = null;
                    int invCount = cvInvestigator.size();
                    for( int indx = 0; indx < invCount; indx++) {
                        InvestigatorBean invBean = (InvestigatorBean)cvInvestigator.get(indx);
                        CoeusVector cvUnits = invBean.getInvestigatorUnits();
                        CoeusVector cvIPInvUnits = null;
                        if( cvUnits != null ) {
                            cvIPInvUnits = new CoeusVector();
                            int unitCount = cvUnits.size();
                            for( int unitIndx = 0; unitIndx < unitCount; unitIndx++){
                                InvestigatorUnitBean unitBean = 
                                    (InvestigatorUnitBean)cvUnits.get(unitIndx);
                                InstituteProposalUnitBean ipUnitBean = new InstituteProposalUnitBean(unitBean);
                                if( INSERT_RECORD.equals( unitBean.getAcType() ) ){    
                                    ipUnitBean.setProposalNumber(ipBaseBean.getProposalNumber());
                                    ipUnitBean.setSequenceNumber(ipBaseBean.getSequenceNumber());
                                    ipUnitBean.setAw_UnitNumber(unitBean.getUnitNumber());
                                    ipUnitBean.setAw_PersonId(unitBean.getPersonId());
                                }else{
                                    if( unitBean instanceof InstituteProposalUnitBean ){
                                        InstituteProposalUnitBean oldUnitBean = 
                                            (InstituteProposalUnitBean)unitBean;
                                        ipUnitBean.setProposalNumber(oldUnitBean.getProposalNumber());
                                              ipUnitBean.setSequenceNumber(oldUnitBean.getSequenceNumber());
                                    }else{
                                        continue;
                                    }
                                }
                                String unitAcType = ipUnitBean.getAcType();
                                if( UPDATE_RECORD.equals(unitAcType) ){
                                    queryEngine.update(queryKey, ipUnitBean);
                                }else if( INSERT_RECORD.equals(unitAcType)){
                                    queryEngine.insert(queryKey, ipUnitBean);
                                }else if( DELETE_RECORD.equals(unitAcType)){
                                    queryEngine.delete(queryKey, ipUnitBean);
                                }
                                cvIPInvUnits.add(ipUnitBean);
                            }
                        }
                        InstituteProposalInvestigatorBean ipInvBean = 
                            new InstituteProposalInvestigatorBean(invBean);
                        if( INSERT_RECORD.equals( invBean.getAcType() ) ){
                            ipInvBean.setProposalNumber(ipBaseBean.getProposalNumber());
                            ipInvBean.setSequenceNumber(ipBaseBean.getSequenceNumber());
                            ipInvBean.setAw_PersonId(invBean.getPersonId());
                        }else{
                            if( invBean instanceof InstituteProposalInvestigatorBean ){
                                InstituteProposalInvestigatorBean oldInvBean = 
                                    (InstituteProposalInvestigatorBean)invBean;
                                ipInvBean.setProposalNumber(oldInvBean.getProposalNumber());
                                ipInvBean.setSequenceNumber(oldInvBean.getSequenceNumber());
                            }else{
                                continue;
                            }
                        }
                        ipInvBean.setInvestigatorUnits(cvIPInvUnits);
                        invAcType = ipInvBean.getAcType();
                        if( UPDATE_RECORD.equals(invAcType) ){
                            queryEngine.update(queryKey, ipInvBean);
                        }else if( INSERT_RECORD.equals(invAcType)){
                            queryEngine.insert(queryKey, ipInvBean);
                        }else if( DELETE_RECORD.equals(invAcType)){
                            queryEngine.delete(queryKey, ipInvBean);
                        }
                    }
                }
            }catch(CoeusException ce ) {
                ce.printStackTrace();
            }
        }
        
    }
    public void setFormData(){
        try{
            boolean leadUnitAssigned = false;
            cvInvestigator = queryEngine.executeQuery(queryKey,InstituteProposalInvestigatorBean.class, 
                                CoeusVector.FILTER_ACTIVE_BEANS);        
            cvUnit = queryEngine.executeQuery(queryKey,InstituteProposalUnitBean.class, 
                                CoeusVector.FILTER_ACTIVE_BEANS);        
            if( cvInvestigator != null  && cvUnit != null ) {
                int invCount = cvInvestigator.size();
                for( int invIndx = 0; invIndx < invCount ; invIndx++ ) {
                    InvestigatorBean invBean = (InvestigatorBean)cvInvestigator.get(invIndx);
                    CoeusVector cvInvUnits = cvUnit.filter(new Equals("personId",invBean.getPersonId()));
                    invBean.setInvestigatorUnits(cvInvUnits);
                    // 3587: Multi Campus Enhancement - Start
                    // Set the lead unit of the IP
                    InvestigatorUnitBean invUnitBean = null;
                    if(cvInvUnits != null && !leadUnitAssigned){
                        int totalUnits = cvInvUnits.size();
                        for(int unitIndex = 0; unitIndex < totalUnits; unitIndex++){
                            invUnitBean = (InvestigatorUnitBean) cvInvUnits.get(unitIndex);
                            if(invUnitBean.isLeadUnitFlag()){
                                setLeadUnitNo(invUnitBean.getUnitNumber());
                                leadUnitAssigned = true;
                                break;
                            }
                        }
                    }
                    // 3587: Multi Campus Enhancement - End
                }
            }
            super.setFormData(cvInvestigator);
            
             //Case 2106 Start 1
            super.setModuleName(CoeusGuiConstants.INSTITUTE_PROPOSAL_MODULE);
            super.setModuleNumber(ipBaseBean.getProposalNumber());
            super.setSeqNo(""+ipBaseBean.getSequenceNumber());
            //Case 2106 End 1
            //Added for Brown's Enhancement
            fetchAdministratorData();
            //Added for Brown's Enhancement
        }catch(CoeusException ce ) {
            ce.printStackTrace();
            super.setFormData(null);
        }
    }
    
    /***
     * validate method
     * @return boolean
     */
    public boolean validate() throws CoeusUIException {
        if( isInvestigatorPresent(false) ) {
            //For bug fix # 1617
            if (isPIPresent(false)) {
                return super.validate();
            }
            CoeusOptionPane.showWarningDialog(coeusMessageResources.parseMessageKey(
            "protoInvFrm_exceptionCode.1064"));
            return false;
        }
        CoeusOptionPane.showWarningDialog(coeusMessageResources.parseMessageKey(
            "instPropInvestigator_exceptionCode.1151"));
        return false;
    }
    
    /** Getter for property queryKey.
     * @return Value of property queryKey.
     *
     */
    public java.lang.String getQueryKey() {
        return queryKey;
    }
    
    /** Setter for property queryKey.
     * @param queryKey New value of property queryKey.
     *
     */
    public void setQueryKey(java.lang.String queryKey) {
        this.queryKey = queryKey;
    }
    
    /** Getter for property refreshRequired.
     * @return Value of property refreshRequired.
     *
     */
    public boolean isRefreshRequired() {
        return refreshRequired;
    }
    
    /** Setter for property refreshRequired.
     * @param refreshRequired New value of property refreshRequired.
     *
     */
    public void setRefreshRequired(boolean refreshRequired) {
        this.refreshRequired = refreshRequired;
    }
    
    public void refresh(){
        if( isRefreshRequired() ) {
            setFormData();
            setRefreshRequired(false);
        }
    }
    
    /** Getter for property ipBaseBean.
     * @return Value of property ipBaseBean.
     *
     */
    public edu.mit.coeus.instprop.bean.InstituteProposalBaseBean getIpBaseBean() {
        return ipBaseBean;
    }
    
    /** Setter for property ipBaseBean.
     * @param ipBaseBean New value of property ipBaseBean.
     *
     */
    public void setIpBaseBean(edu.mit.coeus.instprop.bean.InstituteProposalBaseBean ipBaseBean) {
        this.ipBaseBean = ipBaseBean;
    }
    
    //Case 2106 Start 2
    public Component getControlledUI(){
       return super.getControlledUI();
    }
    //Case 2106 End 2
}
