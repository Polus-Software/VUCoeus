/**
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

/*
 * AwardKeyPersonController.java
 *
 * Created on January 27, 2009, 4:14 PM
 */

package edu.mit.coeus.award.controller;

import edu.mit.coeus.award.bean.AwardBaseBean;
import edu.mit.coeus.award.bean.AwardKeyPersonBean;
import edu.mit.coeus.bean.KeyPersonBean;
import edu.mit.coeus.bean.KeyPersonUnitBean;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.exception.CoeusUIException;
import edu.mit.coeus.gui.event.BeanEvent;
import edu.mit.coeus.gui.event.BeanUpdatedListener;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.TypeConstants;
import edu.mit.coeus.utils.keyperson.KeyPersonController;
import edu.mit.coeus.utils.query.Equals;
import edu.mit.coeus.utils.query.QueryEngine;
import java.awt.Component;
import javax.swing.JScrollPane;

/**
 *
 * @author sreenathv
 */
public class AwardKeyPersonController extends KeyPersonController implements BeanUpdatedListener{
    
       
    private QueryEngine queryEngine;
    private AwardBaseBean awardBaseBean;
    private CoeusVector cvKeyPerson;
    private CoeusVector cvUnit;
    private String queryKey;
    private static int KEY_PERSON_TAB_INDEX = 8;
    
    private static final char NEW_ENTRY_MODE = 'E';
    private static final char COPIED_MODE = 'C';
    public static final char NEW_CHILD_COPIED = 'P';
    /**
     * Creates a new instance of AwardKeyPersonController
     * 
     * @param AwardBaseBean awardBaseBean
     * @param String quesryKey
     * @param char functionType
     */
    public AwardKeyPersonController(AwardBaseBean awardBaseBean,String queryKey,char functionType) {
        super(awardBaseBean.getMitAwardNumber(), CoeusGuiConstants.AWARD_MODULE);
        this.setAwardBaseBean(awardBaseBean);
        this.queryKey = queryKey;
        registerEvents();
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
                 if( getFunctionType() == AwardController.NEW_ENTRY){
                    //deleting all the investigators and units in query engine for new mode
                    //bcoz we may replace the data what we got from query engine with the new data.
                    //So the instance in query engine should be replaced with the new one.
                    Equals eqAllData = new Equals("proposalNumber",awardBaseBean.getMitAwardNumber());
                    queryEngine.removeData(queryKey,AwardKeyPersonBean.class,eqAllData);
                    queryEngine.removeData(queryKey,KeyPersonUnitBean.class,eqAllData);
                }
                cvKeyPerson = (CoeusVector)getFormData();
                if( cvKeyPerson != null ) {
                    String keyPersonAcType = null;
                    CoeusVector cvUnitQueryEngine= new CoeusVector();

                    for(int i=0;i<cvKeyPerson.size();i++)
                    { KeyPersonBean keyPerBeantmp = (KeyPersonBean)cvKeyPerson.get(i);
                      CoeusVector tmp=keyPerBeantmp.getKeyPersonsUnits();
                      if(tmp!=null)
                      {cvUnitQueryEngine.addAll(tmp);}
                    }
                    CoeusVector cvlist=new CoeusVector();
                    for(int i=0; i<cvUnitQueryEngine.size();i++)
                    {
                        KeyPersonUnitBean kPUB=(KeyPersonUnitBean)cvUnitQueryEngine.get(i);
                        String unitAcType = kPUB.getAcType();
                               if(unitAcType!=null)
                               {if(!cvlist.contains(kPUB)){
                                    kPUB.setProposalNumber(awardBaseBean.getMitAwardNumber());
                                    kPUB.setSequenceNumber(awardBaseBean.getSequenceNumber());
                                   // kPUB.setAw_UnitNumber(unitBean.getUnitNumber());
                                  // kPUB.setAw_PersonId(unitBean.getPersonId());
                                   cvlist.add(kPUB);}}
                    }
                     queryEngine.addCollection(queryKey, KeyPersonUnitBean.class, cvlist);
                    int keyPersonCount = cvKeyPerson.size();
                    for( int indx = 0; indx < keyPersonCount; indx++) {
                        KeyPersonBean keyPerBean = (KeyPersonBean)cvKeyPerson.get(indx);
                        AwardKeyPersonBean awardKeyPerBean =
                                new AwardKeyPersonBean(keyPerBean);
                        if( INSERT_RECORD.equals( keyPerBean.getAcType() ) ){
                            awardKeyPerBean.setMitAwardNumber(getAwardBaseBean().getMitAwardNumber());
                            awardKeyPerBean.setSequenceNumber(getAwardBaseBean().getSequenceNumber());
                            awardKeyPerBean.setAw_PersonId(keyPerBean.getPersonId());
                        }else{
                            if( keyPerBean instanceof AwardKeyPersonBean ){
                                AwardKeyPersonBean oldKeyPerBean =
                                        (AwardKeyPersonBean)keyPerBean;
                                awardKeyPerBean.setMitAwardNumber(oldKeyPerBean.getMitAwardNumber());
                                awardKeyPerBean.setSequenceNumber(oldKeyPerBean.getSequenceNumber());
                            }else{
                                continue;
                            }
                        }

                        if(awardKeyPerBean.getAcType()!= null){
                            if(awardKeyPerBean.getAcType().equals(TypeConstants.UPDATE_RECORD)){
                                //First delete the existing data and then insert the same. This is
                                //required since primary keys can be modified
                                awardKeyPerBean.setAcType(TypeConstants.DELETE_RECORD);
                                queryEngine.delete(queryKey, awardKeyPerBean);
                                awardKeyPerBean.setAcType(TypeConstants.INSERT_RECORD);
                                queryEngine.insert(queryKey, awardKeyPerBean);
                            }else if(awardKeyPerBean.getAcType().equals(TypeConstants.DELETE_RECORD)){
                                awardKeyPerBean.setAcType(TypeConstants.DELETE_RECORD);
                                queryEngine.delete(queryKey, awardKeyPerBean);
                            }else if(awardKeyPerBean.getAcType().equals(TypeConstants.INSERT_RECORD)){
                                awardKeyPerBean.setAcType(TypeConstants.INSERT_RECORD);
                                queryEngine.insert(queryKey, awardKeyPerBean);
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
            cvKeyPerson = queryEngine.executeQuery(queryKey,AwardKeyPersonBean.class,
                    CoeusVector.FILTER_ACTIVE_BEANS);            
            super.setModuleName(CoeusGuiConstants.AWARD_MODULE);
            super.setModuleNumber(getAwardBaseBean().getMitAwardNumber());
            super.setSeqNo(String.valueOf(getAwardBaseBean().getSequenceNumber()));
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
        try{
            return super.validate();
        }catch(CoeusUIException ex){
            CoeusOptionPane.showWarningDialog(ex.getMessage());
            return false;
        }
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
    
    public Component getControlledUI(){
        
        return super.getControlledUI();
    }

    public AwardBaseBean getAwardBaseBean() {
        return awardBaseBean;
    }

    public void setAwardBaseBean(AwardBaseBean awardBaseBean) {
        this.awardBaseBean = awardBaseBean;
    }

    public void beanUpdated(BeanEvent beanEvent) {
        if( beanEvent.getBean().getClass().equals(AwardKeyPersonBean.class) ){
            setDataChanged(true);
            refresh();
        }
    }

    private void registerEvents() {
        addBeanUpdatedListener(this, AwardKeyPersonBean.class);
    }
}
