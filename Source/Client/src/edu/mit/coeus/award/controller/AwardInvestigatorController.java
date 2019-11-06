/*
 * AwardInvestigatorController.java
 *
 * Created on March 30, 2004, 12:04 PM
 */
/* PMD check performed, and commented unused imports and variables on 27-OCT-2010
 * by Keerthy Jayaraj
 */
package edu.mit.coeus.award.controller;

import edu.mit.coeus.award.AwardConstants;
import edu.mit.coeus.award.bean.AwardBaseBean;
import edu.mit.coeus.award.bean.AwardInvestigatorsBean;
import edu.mit.coeus.award.bean.AwardUnitBean;
import edu.mit.coeus.bean.CoeusParameterBean;
import edu.mit.coeus.bean.InvestigatorBean;
import edu.mit.coeus.bean.InvestigatorUnitBean;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.exception.CoeusUIException;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.utils.CoeusConstants;
import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.KeyConstants;
import edu.mit.coeus.utils.TypeConstants;
import edu.mit.coeus.utils.investigator.InvestigatorController;
import edu.mit.coeus.utils.query.QueryEngine;
import edu.mit.coeus.utils.query.Equals;
import edu.mit.coeus.gui.event.*;
import edu.mit.coeus.utils.CoeusGuiConstants;

import java.awt.Component;
// JM 4-10-2012 added to fix scrolling issues
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
// JM END
import java.util.HashMap;

import javax.swing.JScrollPane;

/**
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 * @author  jobinelias
 */
public class AwardInvestigatorController extends InvestigatorController 
implements BeanUpdatedListener
{
    
    // For querying 
    private QueryEngine queryEngine;
       
    private AwardBaseBean awardBaseBean;
        
    //For getting the data from the bean
    private CoeusVector cvInvestigator;
    private CoeusVector cvUnit;
    
    private String queryKey;
    private CoeusMessageResources coeusMessageResources;
    
    private boolean refreshRequired;
    
    //Bug Fix:Performance Issue (Out of memory) Start 1
    private JScrollPane jscrPn;
    //Bug Fix:Performance Issue (Out of memory) End 1
    //Case 2796: Sync to Parent
    private AwardSyncDetailsController syncController = null;
    private AwardBaseWindowController baseController = null;
    //2796 End
    
    //Added for COEUSQA-2434 : Award Sync - Error syncing terms - misleading error message - Start
    boolean isDeleteAndSync = false;
    private static final String CANNOT_DELETE_SYNC_ONE_INVESTIGATOR = "awardInvestigator_exceptionCode.1001";
    //COEUSQA-2434 : End

    /** Creates a new instance of AwardInvestigatorController 
     * @param AwardBaseBean awardBaseBean
     */
    public AwardInvestigatorController(AwardBaseBean awardBaseBean,String queryKey) {
        super(awardBaseBean.getMitAwardNumber(), CoeusGuiConstants.AWARD_MODULE);
        this.awardBaseBean = awardBaseBean;
        this.queryKey = queryKey;
        //JIRA Case COEUSDEV-160, COEUSDEV-177 - START
        jscrPn = new JScrollPane(super.getControlledUI());
        // JM 4-10-2012 add listener to pass control to outer pane for scrolling
        jscrPn.addMouseWheelListener(new MouseWheelListener() {
            public void mouseWheelMoved(MouseWheelEvent e) {
            	jscrPn.getParent().dispatchEvent(e);
            }
        });
        //JIRA Case COEUSDEV-160, COEUSDEV-177 - END
        queryEngine = QueryEngine.getInstance();
        cvInvestigator = new CoeusVector();
        cvUnit = new CoeusVector();
        coeusMessageResources = CoeusMessageResources.getInstance();
        setFormData();
        registerEvents();
    }
    
    public void registerEvents() {
        addBeanUpdatedListener(this, AwardInvestigatorsBean.class);
    }
    
    public void beanUpdated(BeanEvent beanEvent) {
        if(beanEvent.getSource().getClass().equals(FundingProposalsController.class) ||
        beanEvent.getSource().getClass().equals(AwardBaseWindowController.class)) {
            if(beanEvent.getBean().getClass().equals(AwardInvestigatorsBean.class)) {
                setRefreshRequired(true);
                refresh();
            }
        }
    }
    
    
    /***
     * save form data
     * @return void
     */
    public void saveFormData() {
        if( isDataChanged() ) {
            try{
                if( getFunctionType() == AwardController.NEW_AWARD ||
                     getFunctionType() == AwardController.NEW_CHILD_COPIED){// Other condition is for Bug Fix #1710
                    //deleting all the investigators and units in query engine for new mode
                    //bcoz we may replace the data what we got from query engine with the new data.
                    //So the instance in query engine should be replaced with the new one.
                Equals eqAllData = new Equals("mitAwardNumber",awardBaseBean.getMitAwardNumber());
                    queryEngine.removeData(queryKey,AwardInvestigatorsBean.class,eqAllData);
                    queryEngine.removeData(queryKey,AwardUnitBean.class,eqAllData);
                }
                //Added for COEUSQA-2383 Two Lead Unit appear for an Award - Start
                //Removing the inserted entry through Funding Proposal 
                //(Deletion is done by changing the value in the investigator table and unit table, Not by 'Delete' Button)
                 Equals eqAwInv =new Equals("acType",TypeConstants.INSERT_RECORD);     
                 queryEngine.removeData(queryKey,AwardInvestigatorsBean.class,eqAwInv);
                 queryEngine.removeData(queryKey,AwardUnitBean.class,eqAwInv);
                 //Added for COEUSQA-2383 Two Lead Unit appear for an Award End
                
                cvInvestigator = (CoeusVector)getFormData();
                if( cvInvestigator != null ) {
                    String invAcType = null;
                    int invCount = cvInvestigator.size();
                    for( int indx = 0; indx < invCount; indx++) {
                        InvestigatorBean invBean = (InvestigatorBean)cvInvestigator.get(indx);
                        CoeusVector cvUnits = invBean.getInvestigatorUnits();
                        CoeusVector cvAwdInvUnits = null;
                        if( cvUnits != null ) {
                            cvAwdInvUnits = new CoeusVector();
                            int unitCount = cvUnits.size();
                            for( int unitIndx = 0; unitIndx < unitCount; unitIndx++){
                                InvestigatorUnitBean unitBean = 
                                    (InvestigatorUnitBean)cvUnits.get(unitIndx);
                                AwardUnitBean awardUnitBean = new AwardUnitBean(unitBean);
                                if( INSERT_RECORD.equals( unitBean.getAcType() ) ){    
                                    awardUnitBean.setMitAwardNumber(awardBaseBean.getMitAwardNumber());
                                    awardUnitBean.setSequenceNumber(awardBaseBean.getSequenceNumber());
                                    awardUnitBean.setAw_PersonId(unitBean.getPersonId());
                                    awardUnitBean.setAw_UnitNumber(unitBean.getUnitNumber());
                                    cvAwdInvUnits.add(awardUnitBean);
                                }else{
                                    if( unitBean instanceof AwardUnitBean){
                                        AwardUnitBean oldUnitBean = 
                                            (AwardUnitBean)unitBean;
                                        awardUnitBean.setMitAwardNumber(oldUnitBean.getMitAwardNumber());
                                        awardUnitBean.setSequenceNumber(oldUnitBean.getSequenceNumber());
                                    }else{
                                        continue;
                                    }
                                    
                                }
                                String unitAcType = awardUnitBean.getAcType();
                                if( UPDATE_RECORD.equals(unitAcType) ){
                                    queryEngine.update(queryKey, awardUnitBean);
                                }else if( INSERT_RECORD.equals(unitAcType)){
                                    queryEngine.insert(queryKey, awardUnitBean);
                                }else if( DELETE_RECORD.equals(unitAcType)){
                                    queryEngine.delete(queryKey, awardUnitBean);
                                }
                                cvAwdInvUnits.add(awardUnitBean);
                                
                            }
                        }
                        AwardInvestigatorsBean awdInvBean = 
                            new AwardInvestigatorsBean(invBean);
                        if( INSERT_RECORD.equals( invBean.getAcType() ) ){
                            awdInvBean.setMitAwardNumber(awardBaseBean.getMitAwardNumber());
                            awdInvBean.setSequenceNumber(awardBaseBean.getSequenceNumber());
                            awdInvBean.setAw_PersonId(invBean.getPersonId());
                        }else{
                            if( invBean instanceof AwardInvestigatorsBean ){
                                AwardInvestigatorsBean oldInvBean = 
                                    (AwardInvestigatorsBean)invBean;
                                awdInvBean.setMitAwardNumber(oldInvBean.getMitAwardNumber());
                                awdInvBean.setSequenceNumber(oldInvBean.getSequenceNumber());
                            }else{
                                continue;
                            }
                        }
                        
                        awdInvBean.setInvestigatorUnits(cvAwdInvUnits);
                        invAcType = awdInvBean.getAcType();
                         
                        if( UPDATE_RECORD.equals(invAcType) ){
                            queryEngine.update(queryKey, awdInvBean);
                        }else if( INSERT_RECORD.equals(invAcType)){
                            queryEngine.insert(queryKey, awdInvBean);
                        }else if( DELETE_RECORD.equals(invAcType)){
                            queryEngine.delete(queryKey, awdInvBean);
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
            cvInvestigator = queryEngine.executeQuery(queryKey,AwardInvestigatorsBean.class, 
                                CoeusVector.FILTER_ACTIVE_BEANS);  
            cvUnit = queryEngine.executeQuery(queryKey,AwardUnitBean.class, 
                                CoeusVector.FILTER_ACTIVE_BEANS);        
            if( cvInvestigator != null  && cvUnit != null ) {
                int invCount = cvInvestigator.size();
                for( int invIndx = 0; invIndx < invCount ; invIndx++ ) {
                    InvestigatorBean invBean = (InvestigatorBean)cvInvestigator.get(invIndx);
                    CoeusVector cvInvUnits = cvUnit.filter(new Equals("personId",invBean.getPersonId()));
                    invBean.setInvestigatorUnits(cvInvUnits);
                    // 3587: Multi Campus Enhancement - Start
                    // Set the lead unit of the award
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
            super.setDataChanged(false);
            // Commented for COEUSQA-3642 : Issues with COEUSQA-1676 Credit Split Infrastructure project - Start
//            super.setFormData(cvInvestigator);
            // Commented for COEUSQA-3642 : Issues with COEUSQA-1676 Credit Split Infrastructure project - End
            //Case 2106 Start 1
            super.setModuleName(CoeusGuiConstants.AWARD_MODULE);
            super.setModuleNumber(awardBaseBean.getMitAwardNumber());
            super.setSeqNo(""+awardBaseBean.getSequenceNumber());
            //Case 2106 End 1
            // Added for COEUSQA-3642 : Issues with COEUSQA-1676 Credit Split Infrastructure project - Start
            super.setFormData(cvInvestigator);
            // Added for COEUSQA-3642 : Issues with COEUSQA-1676 Credit Split Infrastructure project - End
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
        if( isInvestigatorPresent(false) ) {//case 2796
            // For bug fix #1617
            if (isPIPresent(false)) {//Case 2796
                return super.validate();
            }
            CoeusOptionPane.showWarningDialog(coeusMessageResources.parseMessageKey(
            "protoInvFrm_exceptionCode.1064"));
            return false;
        }
        CoeusOptionPane.showWarningDialog(coeusMessageResources.parseMessageKey(
            "awardInvestigator_exceptionCode.1351"));
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
    
    //Bug Fix:Performance Issue (Out of memory) Start 2
    public Component getControlledUI(){
        //JIRA Case COEUSDEV-160, COEUSDEV-177 - START
        //jscrPn = new JScrollPane(super.getControlledUI());
        //JIRA Case COEUSDEV-160, COEUSDEV-177 - END
        return jscrPn;
    }
    
    public void cleanUp(){
        //System.out.println("Investigator Clean up");
        jscrPn.remove(super.getControlledUI());
        jscrPn = null;
        awardBaseBean = null;
        cvInvestigator = null;
        cvUnit = null;
        coeusMessageResources = null;
        removeBeanUpdatedListener(this, AwardInvestigatorsBean.class);
    }
    //Bug Fix:Performance Issue (Out of memory) End 2
    
    //Case 2106 2
    public Component getForm(){
        return super.getControlledUI();
    }
    
    /**
     * Getter for property awardBaseBean.
     * @return Value of property awardBaseBean.
     */
    public edu.mit.coeus.award.bean.AwardBaseBean getAwardBaseBean() {
        return awardBaseBean;
    }
    
    /**
     * Setter for property awardBaseBean.
     * @param awardBaseBean New value of property awardBaseBean.
     */
    public void setAwardBaseBean(edu.mit.coeus.award.bean.AwardBaseBean awardBaseBean) {
        this.awardBaseBean = awardBaseBean;
    }
    
    //Case 2106 2
    
    //New Methods added with Case 2796: Sync To Parent - Start
    
    //This function checks whether for the particular award, 
    //whether syncing is allowed or not.
    public boolean isSyncEnabled(){
        return awardBaseBean.isParent();
    }
    
    //COEUSDEV217 :Sync to Child Awards: Investigator Sync - inconsistent results and LOST AWARD NODE
    /* This function is called when the user selects Sycing of Investigator
     * to child awards.
     * This function performs necessary validations required prior to sync,
     * fetches the sync target and syncs the data.
     */
    public boolean syncInvestigator() {
        
        boolean syncSuccess = false;
        try {
            if(validate()){
                //Modified with COEUSDEV 253: Add Fabe and cs accounts to sync screen.
                HashMap syncTarget = getSyncTarget(AwardConstants.SYNC);
                InvestigatorBean invBean = getSelectedInvestigatorBean();
                if(syncTarget != null){
                    invBean.setSyncRequired(true);
                    invBean.setSyncTarget((String) syncTarget.get(KeyConstants.SYNC_TARGET));
                    invBean.setParameter(syncTarget);
                    if(invBean.getAcType()==null){
                        invBean.setAcType(TypeConstants.UPDATE_RECORD);
                        super.setDataChanged(true);
                    }
                    saveAndSyncAward(AwardInvestigatorsBean.class);
                    syncSuccess = true;
                }
            }
        } catch (CoeusUIException ex) {
            ex.printStackTrace();
        }
        return syncSuccess;
    }
    
    /* This function is called when the user selects Delete and Sync Investigator
     * to child awards.
     * This function performs necessary validations required prior to sync,
     * fetches the sync target and syncs the data.
     */
    public boolean deleteAndSyncInvestigator() {
        //Added for COEUSQA-2434 : Award Sync - Error syncing terms - misleading error message - Start
        isDeleteAndSync = true;
        //COEUSQA-2434 : End
        boolean syncSuccess = false;
        if(super.deleteAndSyncInvestigator()){
            //Modified with COEUSDEV 253: Add Fabe and cs accounts to sync screen.
            HashMap target = getSyncTarget(AwardConstants.DELETE_SYNC);
            InvestigatorBean bean = getSelectedInvestigatorBean();
            if(target!=null){
                performDelete(true);
                if(bean!=null && TypeConstants.DELETE_RECORD.equals(bean.getAcType())  && bean.isSyncRequired()){
                    bean.setSyncTarget((String) target.get(KeyConstants.SYNC_TARGET));
                    bean.setParameter(target);
                    saveAndSyncAward(AwardInvestigatorsBean.class);
                    syncSuccess = true;
                }
            }
        }
        //Added for COEUSQA-2434 : Award Sync - Error syncing terms - misleading error message - Start
        //Reset the flag when the delete sync is performed
        isDeleteAndSync = false;
        //COEUSQA-2434 : End
        return syncSuccess;
    }
    //COEUSDEV217 : End
    //Validates the award and returns the sync target.
    //Modified with COEUSDEV 253: Add Fabe and cs accounts to sync screen.
    private HashMap getSyncTarget(char mode){
        
        HashMap target = null;
        //Modified for COEUSDEV-416 : Award Sync to Children - Display proper error message when not syncing because the award is not saved
//        if(baseController!=null && baseController.validateBeforeSync()){
        if(baseController!=null && baseController.validateBeforeSync(AwardConstants.INVESTIGATOR_SYNC,mode)){//COEUSDEV-416 : End
            syncController   = new AwardSyncDetailsController(mode);
            // Added for COEUSDEV-563:Award Sync to Parent is not triggering SAP feed for child accounts its touching
            syncController.setEnableSAPFeedValidate(isSAPFeedEnabled());
            // COEUSDEV-563:End
            target = syncController.display();
        }
        return target;
    }
    
    //Saves the award forms and validates prior to syncing
    public boolean validateBeforeDelete() {
        
        if( isInvestigatorPresent(true) ) {
            if (isPIPresent(true)) {
                return super.validateBeforeDelete();
            }
            CoeusOptionPane.showWarningDialog(coeusMessageResources.parseMessageKey(
                    "protoInvFrm_exceptionCode.1064"));
            return false;
        }
        //Modified for COEUSQA-2434 : Award Sync - Error syncing terms - misleading error message - Start
//        CoeusOptionPane.showWarningDialog(coeusMessageResources.parseMessageKey(
//                "awardInvestigator_exceptionCode.1351"));
        if(!isDeleteAndSync){
            CoeusOptionPane.showWarningDialog(coeusMessageResources.parseMessageKey(
                    "awardInvestigator_exceptionCode.1351"));
        }else{
            CoeusOptionPane.showWarningDialog(coeusMessageResources.parseMessageKey(
                    CANNOT_DELETE_SYNC_ONE_INVESTIGATOR));
            //Reset the flag after the validation message
            isDeleteAndSync = false;
        }
        //COEUSQA-2434
        return false;
    }

    
    //Sets a reference to basewindow controller
    public void setBaseController(AwardBaseWindowController awardBaseWindowController) {
        this.baseController = awardBaseWindowController;
    }
   
    //Saves the award the syncs the required data
   public void saveAndSyncAward(Object moduleKey){
        if(baseController != null){
            baseController.saveAndSyncAward(moduleKey);
        }
    }
    //2796: Sync to Parent : End
   // Added for COEUSDEV-563:Award Sync to Parent is not triggering SAP feed for child accounts its touching
   /** Method to check the value of SAP feed enabling parameter - SAP_FEED_ENABLED
    *  @return true is SAP feed is enabled; false if not enabled.
    */
   public boolean isSAPFeedEnabled(){
         boolean sapFeedEnabled = false;
         CoeusVector cvParameter;
         try {
             cvParameter = queryEngine.executeQuery(queryKey, CoeusParameterBean.class, CoeusVector.FILTER_ACTIVE_BEANS);
             if(cvParameter != null && !cvParameter.isEmpty()){
                 CoeusVector cvSAPFeed = cvParameter.filter(new Equals("parameterName", CoeusConstants.SAP_FEED_ENABLED));
                 if(cvSAPFeed != null && !cvSAPFeed.isEmpty()){
                     CoeusParameterBean param = (CoeusParameterBean)cvSAPFeed.get(0);
                     if(param!=null && "1".equals(param.getParameterValue())){
                          sapFeedEnabled = true;
                     }
                 }
             }
         }catch (CoeusException ex) {}
         return sapFeedEnabled;
     }
   // COEUSDEV-563:End
}
