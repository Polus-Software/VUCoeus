/*
 * @(#)InstituteProposalUpdateTxnBean.java 1.0 March 08, 2004, 4:20 PM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */
/* PMD check performed, and commented unused imports and
 * variables on 13-JULY-2011 by Bharati
 */

package edu.mit.coeus.instprop.bean;

import edu.mit.coeus.utils.MailActions;
import edu.mit.coeus.utils.ModuleConstants;
import edu.mit.coeus.utils.TypeConstants;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.utils.dbengine.DBEngineImpl;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.utils.dbengine.Parameter;
import edu.mit.coeus.utils.dbengine.ProcReqParameter;
import edu.mit.coeus.utils.dbengine.DBEngineConstants;
import edu.mit.coeus.utils.dbengine.TransactionMonitor;
//import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.utils.CoeusFunctions;
import edu.mit.coeus.utils.CoeusConstants;
import edu.mit.coeus.exception.CoeusException;

import edu.mit.coeus.utils.CoeusVector;

import java.util.Vector;
import java.util.Hashtable;
//import java.util.HashMap;
import java.sql.Timestamp;
//import java.sql.Date;
//import java.sql.SQLException;

import edu.mit.coeus.utils.query.*;
import edu.mit.coeus.bean.SequenceLogic;
import edu.mit.coeus.bean.IndicatorLogic;
import edu.mit.coeus.bean.CoeusBean;
import edu.mit.coeus.unit.bean.UnitDataTxnBean;
import edu.mit.coeus.unit.bean.UnitDetailFormBean;
import edu.mit.coeus.award.bean.AwardFundingProposalBean;
import edu.mit.coeus.award.bean.AwardUpdateTxnBean;
import edu.mit.coeus.bean.CoeusDataTxnBean;
import edu.mit.coeus.bean.CoeusMessageResourcesBean;
import edu.mit.coeus.bean.KeyPersonUnitBean;
import edu.mit.coeus.irb.bean.ProtocolDataTxnBean;
import edu.mit.coeus.irb.bean.ProtocolFundingSourceBean;
import edu.mit.coeus.irb.bean.ProtocolInfoBean;
import edu.mit.coeus.irb.bean.ProtocolLinkBean;
import edu.mit.coeus.irb.bean.ProtocolNotepadBean;
import edu.mit.coeus.irb.bean.ProtocolUpdateTxnBean;
import edu.mit.coeus.propdev.bean.InvestigatorCreditSplitBean;
//import edu.mit.coeus.utils.TypeConstants;
import edu.mit.coeus.utils.locking.LockingBean;
import edu.mit.coeus.utils.locking.LockingException;
/**
 * This class provides the methods for performing modify, insert and delete functionality using
 * stored procedures for Institute Proposal module.
 *
 * All methods use <code>DBEngineImpl</code> instance for
 * database interaction.
 *
 * @version 1.0 April 27, 2004, 3:20 PM
 * @author  Prasanna Kumar K
 */

public class InstituteProposalUpdateTxnBean {
    // instance of a dbEngine
    private DBEngineImpl dbEngine;
    // holds the dataset name
    private static final String DSN = "Coeus";
    
    // holds the userId for the logged in user
    private String userId;
    
    // Added by Shivakumar for bug fixing bug id 1351
    private static final char NEW_MODE = 'N';
    
    private static final char NEW_ENTRY_MODE = 'E';
    // End Shivakumar
    private TransactionMonitor transMon;
    
    private Timestamp dbTimestamp;  
    
    private String indicator;
    
    //Added for COEUSQA-3119- Need to implement IACUC link to Award, IP, Prop Dev, and IRB - start
    private static final int HUMAN_SUBJECTS_CODE = 1;
    private static final int ANIMAL_USAGE_CODE = 2;
    Vector iacucProtocolData = null;
    //Added for COEUSQA-3119- Need to implement IACUC link to Award, IP, Prop Dev, and IRB - end
    
    //Added for the Coeus Enhancement case:#1799 start step:1
    private String unitNumber;
    
    //For the Coeus Enhancement case:#1799 
    Vector protocolData = null;
    //End Coeus Enhancement case:#1799 step:1
    
    /** Creates a new instance of BudgetUpdateTxnBean */
    public InstituteProposalUpdateTxnBean() {
    }
    
    /**
     * Creates new BudgetUpdateTxnBean and initializes userId.
     * @param userId String which the Loggedin userid
     */
    public InstituteProposalUpdateTxnBean(String userId) throws DBException {
        this.userId = userId;
        dbEngine = new DBEngineImpl();
        transMon = TransactionMonitor.getInstance();
        //Coeus Enhancement: Case #1799 start
        dbTimestamp = (new CoeusFunctions()).getDBTimestamp();  
        //Coeus Enhancement: Case #1799 end
        
    } 
    
    /** Method used to update/insert Institute Proposal details
     *  <li>To update, it uses DW_UPDATE_INST_PROPOSAL procedure.
     *
     * @return boolean true for successful insert/modify
     * @param instituteProposalData Hashtable
     * @exception DBException if the instance of a dbEngine is null.
     * @exception CoeusException if the DB instance is not available. 
     */
    public boolean addUpdInstituteProposal(Hashtable instituteProposalData) throws CoeusException ,DBException,Exception{
        boolean success = false;
        Vector procedures = new Vector(5,3);
//        Vector param = new Vector();
//        ProcReqParameter procReqParameter = null;
        SequenceLogic sequenceLogic = null;
        IndicatorLogic indicatorLogic = null;
        //Added for Coeus Enhancement case:#1799 start step:2
        //Vector protocolData = null;
        //End Coeus Enhancement case:#1799 step:2
        InstituteProposalTxnBean instituteProposalTxnBean = new InstituteProposalTxnBean();
        
        dbTimestamp = (new CoeusFunctions()).getDBTimestamp();        
        
        //Instantiate Indicator Logic
        indicatorLogic = new IndicatorLogic();        
                    
        //Get Institute Proposal data to be saved        
        CoeusVector cvInstituteProposalData = (CoeusVector)instituteProposalData.get(InstituteProposalBean.class);
        InstituteProposalBean instituteProposalBean = (InstituteProposalBean)cvInstituteProposalData.elementAt(0);
        
        
        if(instituteProposalBean!= null && instituteProposalBean.getAcType() != null){
            /*code has been modified by Nadh to fix Indicator
             * (If you open the award in correct mode and save the
             * award without touching CostSharing information, CostSharing indicator should not be changed,
             * it should remain N1 or P1.  In the application its being cahnged to N0 or P0.)
             */
            //start 20 sep 2004
            //IDC Rates Indicator
            indicator = indicatorLogic.processLogic((CoeusVector)instituteProposalData.get(InstituteProposalIDCRateBean.class));
            if((indicator.equals("P0") && instituteProposalBean.getAcType().equals("U")) 
                    || (indicator.equals("N0") && instituteProposalBean.getAcType().equals("U"))) {
                indicator = instituteProposalBean.getIdcRateIndicator();
            }
            instituteProposalBean.setIdcRateIndicator(indicator);
            
            //Science Code Indicator
            indicator = indicatorLogic.processLogic((CoeusVector)instituteProposalData.get(InstituteProposalScienceCodeBean.class));
            if((indicator.equals("P0") && instituteProposalBean.getAcType().equals("U")) 
                    || (indicator.equals("N0") && instituteProposalBean.getAcType().equals("U"))) {
                indicator = instituteProposalBean.getScienceCodeIndicator();
            }
            instituteProposalBean.setScienceCodeIndicator(indicator);
            
            //Cost Sharing Indicator
            indicator = indicatorLogic.processLogic((CoeusVector)instituteProposalData.get(InstituteProposalCostSharingBean.class));
            if((indicator.equals("P0") && instituteProposalBean.getAcType().equals("U")) 
                    || (indicator.equals("N0") && instituteProposalBean.getAcType().equals("U"))) {
                indicator = instituteProposalBean.getCostSharingIndicator();
            }
            instituteProposalBean.setCostSharingIndicator(indicator);            
            
            //IP Review Activity Indicator
            indicator = indicatorLogic.processLogic((CoeusVector)instituteProposalData.get(InstituteProposalIPReviewActivityBean.class));
            if((indicator.equals("P0") && instituteProposalBean.getAcType().equals("U")) 
                    || (indicator.equals("N0") && instituteProposalBean.getAcType().equals("U"))) {
                indicator = instituteProposalBean.getIpReviewActivityIndicator();
            }
            instituteProposalBean.setIpReviewActivityIndicator(indicator);            
            
            //Special Review Indicator
            indicator = indicatorLogic.processLogic((CoeusVector)instituteProposalData.get(InstituteProposalSpecialReviewBean.class));
            if((indicator.equals("P0") && instituteProposalBean.getAcType().equals("U")) 
                    || (indicator.equals("N0") && instituteProposalBean.getAcType().equals("U"))) {
                indicator = instituteProposalBean.getSpecialReviewIndicator();
            }
            instituteProposalBean.setSpecialReviewIndicator(indicator);           
            //end nadh for the fix of indicator logic
            
            // 3823: Key Person record needed in Ip and Award  - Start
            // Set proper key person indicator logic
            indicator = indicatorLogic.processLogic((CoeusVector)instituteProposalData.get(InstituteProposalKeyPersonBean.class));
            if((indicator.equals("P0") && instituteProposalBean.getAcType().equals("U"))
            || (indicator.equals("N0") && instituteProposalBean.getAcType().equals("U"))) {
                indicator = instituteProposalBean.getKeyPersonIndicator();
            }
            instituteProposalBean.setKeyPersonIndicator(indicator);
            // 3823: Key Person record needed in Ip and Award  - End
            
            //Set Initial Contract Admin from Investigator Lead Unit OSP_Admin in Add mode
            /* if condition commented & new if condition added by Shivakumar for bug fixing
             * bug id 1351 */ 
//            if(instituteProposalBean.getAcType()!=null && instituteProposalBean.getAcType().equalsIgnoreCase("I")){
              if(instituteProposalBean.getMode()==NEW_MODE){
              // End Shivakumar    
                cvInstituteProposalData = (CoeusVector)instituteProposalData.get(InstituteProposalInvestigatorBean.class);            
                if(cvInstituteProposalData!=null){
                    CoeusVector cvLeadUnitData = null;
                    InstituteProposalInvestigatorBean instituteProposalInvestigatorBean = null;
                    for(int row = 0; row < cvInstituteProposalData.size(); row++){
                        instituteProposalInvestigatorBean = (InstituteProposalInvestigatorBean)cvInstituteProposalData.elementAt(row);
                        if(instituteProposalInvestigatorBean.isPrincipalInvestigatorFlag()){
                            cvLeadUnitData = instituteProposalInvestigatorBean.getInvestigatorUnits();
                            if(cvLeadUnitData!=null){
                                InstituteProposalUnitBean instituteProposalUnitBean = null;
                                for(int unitRow = 0 ; unitRow < cvLeadUnitData.size(); unitRow++){
                                    instituteProposalUnitBean = (InstituteProposalUnitBean)cvLeadUnitData.elementAt(unitRow);
                                    if(instituteProposalUnitBean.isLeadUnitFlag()){
                                        UnitDataTxnBean unitDataTxnBean = new UnitDataTxnBean();
                                        // JM 6-10-2013 we want the contract administrator instead here
                                        //UnitDetailFormBean unitDetailFormBean = unitDataTxnBean.getUnitOSPAdministrator(instituteProposalUnitBean.getUnitNumber());
                                        UnitDetailFormBean unitDetailFormBean = unitDataTxnBean.getUnitContractAdministrator(instituteProposalUnitBean.getUnitNumber());
                                        // JM END
                                        if(unitDetailFormBean!=null){
                                            instituteProposalBean.setInitialContractAdmin(unitDetailFormBean.getOspAdminId());
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
   
            //Update Institute Proposal table
            procedures.add(updateInstituteProposal(instituteProposalBean));
            
            //Instantiate SequenceLogic to set Sequence No as that of Parent
            //if any Child is modified & if there is a mismatch between Parent 
            //& child sequence number. 
            //Set Actype as I
            //Need not generate Sequence number always.
            sequenceLogic = new SequenceLogic(instituteProposalBean, false);
            
            //Update IDC Rates
            cvInstituteProposalData = null;
            cvInstituteProposalData = (CoeusVector)instituteProposalData.get(InstituteProposalIDCRateBean.class);
            if(cvInstituteProposalData!=null){
                //Process Sequence Logic
                if(isChildRecordChanged(cvInstituteProposalData)){
                    cvInstituteProposalData = sequenceLogic.processDetails(cvInstituteProposalData, true);
                }
                
                InstituteProposalIDCRateBean instituteProposalIDCRateBean = null;
                for(int row = 0; row < cvInstituteProposalData.size(); row++){
                    instituteProposalIDCRateBean = (InstituteProposalIDCRateBean)cvInstituteProposalData.elementAt(row);
                    if(instituteProposalIDCRateBean!=null && instituteProposalIDCRateBean.getAcType()!=null){
                        procedures.add(addUpdProposalIDCRates(instituteProposalIDCRateBean));
                    }
                }
            }
            
            //Update Science Code
            cvInstituteProposalData = null;
            cvInstituteProposalData = (CoeusVector)instituteProposalData.get(InstituteProposalScienceCodeBean.class);
            if(cvInstituteProposalData!=null){
                //Process Sequence Logic
                if(isChildRecordChanged(cvInstituteProposalData)){
                    cvInstituteProposalData = sequenceLogic.processDetails(cvInstituteProposalData, true);                
                }
                InstituteProposalScienceCodeBean instituteProposalScienceCodeBean = null;
                for(int row = 0; row < cvInstituteProposalData.size(); row++){
                    instituteProposalScienceCodeBean = (InstituteProposalScienceCodeBean)cvInstituteProposalData.elementAt(row);
                    if(instituteProposalScienceCodeBean!=null && instituteProposalScienceCodeBean.getAcType()!=null){
                        procedures.add(addUpdInstituteProposalScienceCode(instituteProposalScienceCodeBean));
                    }
                }
            }
            
            //Update Cost Sharing
            cvInstituteProposalData = null;
            cvInstituteProposalData = (CoeusVector)instituteProposalData.get(InstituteProposalCostSharingBean.class);
            if(cvInstituteProposalData!=null){
                //Process Sequence Logic
                if(isChildRecordChanged(cvInstituteProposalData)){
                    cvInstituteProposalData = sequenceLogic.processDetails(cvInstituteProposalData, true);                
                }
                InstituteProposalCostSharingBean instituteProposalCostSharingBean = null;
                for(int row = 0; row < cvInstituteProposalData.size(); row++){
                    instituteProposalCostSharingBean = (InstituteProposalCostSharingBean)cvInstituteProposalData.elementAt(row);
                    if(instituteProposalCostSharingBean!=null && instituteProposalCostSharingBean.getAcType()!=null){
                        procedures.add(addUpdProposalCostSharing(instituteProposalCostSharingBean));
                    }
                }
            }
            
            //Update IP Review Activity
            cvInstituteProposalData = null;
            cvInstituteProposalData = (CoeusVector)instituteProposalData.get(InstituteProposalIPReviewActivityBean.class);
            if(cvInstituteProposalData!=null){
                //Process Sequence Logic
                if(isChildRecordChanged(cvInstituteProposalData)){
                    cvInstituteProposalData = sequenceLogic.processDetails(cvInstituteProposalData, true);                
                }
                InstituteProposalIPReviewActivityBean instituteProposalIPReviewActivityBean = null;
                //Get max Activity Number to generate Activity Number in INSERT mode
                int maxActivityNumber = instituteProposalTxnBean.getMaxIPReviewActivityNumber(instituteProposalBean.getProposalNumber(), instituteProposalBean.getSequenceNumber());
                for(int row = 0; row < cvInstituteProposalData.size(); row++){
                    instituteProposalIPReviewActivityBean = (InstituteProposalIPReviewActivityBean)cvInstituteProposalData.elementAt(row);
                    if(instituteProposalIPReviewActivityBean!=null && instituteProposalIPReviewActivityBean.getAcType()!=null){
                        if(instituteProposalIPReviewActivityBean.getAcType().equalsIgnoreCase("I")){
                            maxActivityNumber = maxActivityNumber + 1;
                            instituteProposalIPReviewActivityBean.setActivityNumber(maxActivityNumber);
                        }                        
                        procedures.add(addUpdInstituteProposalReviewActivity(instituteProposalIPReviewActivityBean));
                    }
                }
            }
            
            //Update Institute Proposal Investigators
            cvInstituteProposalData = (CoeusVector)instituteProposalData.get(InstituteProposalInvestigatorBean.class);
            CoeusVector investigatorUnits = null;
            InstituteProposalInvestigatorBean instituteProposalInvestigatorBean = null;
            InstituteProposalUnitBean instituteProposalUnitBean = null;
            if(cvInstituteProposalData!=null && cvInstituteProposalData.size() > 0){
                //Process Sequence Logic
                boolean isInvestigatorChanged = false;
                if(isChildRecordChanged(cvInstituteProposalData)){
                    isInvestigatorChanged = true;
                    cvInstituteProposalData = sequenceLogic.processDetails(cvInstituteProposalData, true);
                }
                
                for(int row = 0; row < cvInstituteProposalData.size(); row++){
                    instituteProposalInvestigatorBean = (InstituteProposalInvestigatorBean)cvInstituteProposalData.elementAt(row);                 
                                        
                    if(instituteProposalInvestigatorBean!=null && instituteProposalInvestigatorBean.getAcType()!=null){
                        procedures.add(addUpdInstituteProposalInvestigator(instituteProposalInvestigatorBean));
                    }
                    
                    //Update Units for this Investigator
                    investigatorUnits = instituteProposalInvestigatorBean.getInvestigatorUnits();
                    if(investigatorUnits!=null && investigatorUnits.size() > 0){
                        //Process Sequence Logic
                        //Check if any Investigator is changed or Unit is changed
                        if(isInvestigatorChanged || isChildRecordChanged(investigatorUnits)){
                            instituteProposalInvestigatorBean.setInvestigatorUnits(sequenceLogic.processDetails(investigatorUnits, true));
                        }
                        
                        for(int index = 0; index < investigatorUnits.size(); index++){
                            instituteProposalUnitBean = (InstituteProposalUnitBean)investigatorUnits.elementAt(index);
                            if(instituteProposalUnitBean != null && instituteProposalUnitBean.getAcType()!=null){
                                procedures.add(addUpdInstituteProposalInvestigatorUnit(instituteProposalUnitBean));
                            }
                        }
                    }
                }
            }
           
            // 3823: Key Person Records Needed in Inst Proposal and Award - Start
            // Update the IP Key Persons
            cvInstituteProposalData = null;
           // CoeusVector kpunits = null;
            cvInstituteProposalData = (CoeusVector)instituteProposalData.get(InstituteProposalKeyPersonBean.class);
            if(cvInstituteProposalData != null){
                boolean isInvestigatorChanged=false;
                //Process Sequence Logic
                if(isChildRecordChanged(cvInstituteProposalData)){
                    cvInstituteProposalData = sequenceLogic.processDetails(cvInstituteProposalData, true);
               isInvestigatorChanged=true;
                }
                InstituteProposalKeyPersonBean instituteProposalKeyPersonFormBean;
                for(int keyPersonCount = 0; keyPersonCount < cvInstituteProposalData.size(); keyPersonCount++){
                    instituteProposalKeyPersonFormBean = (InstituteProposalKeyPersonBean) cvInstituteProposalData.elementAt(keyPersonCount);
                    if(instituteProposalKeyPersonFormBean != null && instituteProposalKeyPersonFormBean.getAcType() != null ){
                        procedures.add(addUpdInstituteProposalKeyPersons(instituteProposalKeyPersonFormBean));
                    }
                       //Update Units for this KeyPerson
                    
//                    kpunits = (CoeusVector)instituteProposalKeyPersonFormBean.getKeyPersonsUnits();
//                  // CoeusVector kpunitstmp;
//                    if(kpunits!=null && kpunits.size() > 0){
//                        //Process Sequence Logic
//                        //Check if any Investigator is changed or Unit is changed
//                        if(isInvestigatorChanged || isChildRecordChanged(kpunits)){
//
//                            instituteProposalKeyPersonFormBean.setKeyPersonsUnits(sequenceLogic.processDetails(kpunits, true));
                       

//                        for(int index = 0; index < kpunits.size(); index++){
//                           KeyPersonUnitBean kPUnitBean = (KeyPersonUnitBean)kpunits.elementAt(index);
//
//                               if(kPUnitBean != null && kPUnitBean.getAcType()!=null){
//                                kPUnitBean.setProposalNumber(instituteProposalKeyPersonFormBean.getProposalNumber());
//                                procedures.add(addUpdInstituteProposalKeyPersonsUnit(kPUnitBean));
//
//                           }
//
//                        }}}
                }
            }
            // 3823: Key Person Records Needed in Inst Proposal and Award - End

            //to add the keyperson units to the table...

            //keyperson unit entry ends....
            CoeusVector kPUnits=(CoeusVector)instituteProposalData.get(KeyPersonUnitBean.class);
            if(kPUnits!=null && kPUnits.size() > 0){
                kPUnits=sequenceLogic.processDetails(kPUnits,true);
              if( isChildRecordChanged(kPUnits)){
              
               for(int index = 0; index < kPUnits.size(); index++){
                           KeyPersonUnitBean kPUnitBean = (KeyPersonUnitBean)kPUnits.elementAt(index);

                               if(kPUnitBean != null && kPUnitBean.getAcType()!=null){
                               
                                procedures.add(addUpdInstituteProposalKeyPersonsUnit(kPUnitBean));

                           }

                        }
              
              
              }

            }


            //Update Special Review 
            cvInstituteProposalData = null;
            cvInstituteProposalData = (CoeusVector)instituteProposalData.get(InstituteProposalSpecialReviewBean.class);
            if(cvInstituteProposalData!=null){
                //Process Sequence Logic
                if(isChildRecordChanged(cvInstituteProposalData)){
                    cvInstituteProposalData = sequenceLogic.processDetails(cvInstituteProposalData, true);                
                }
                int maxReviewNumber = instituteProposalTxnBean.getMaxInstituteProposalSpecialReviewNumber(instituteProposalBean.getProposalNumber(), instituteProposalBean.getSequenceNumber());
                InstituteProposalSpecialReviewBean instituteProposalSpecialReviewBean = null;
                for(int row = 0; row < cvInstituteProposalData.size(); row++){
                    instituteProposalSpecialReviewBean = (InstituteProposalSpecialReviewBean)cvInstituteProposalData.elementAt(row);
                    if(instituteProposalSpecialReviewBean!=null && instituteProposalSpecialReviewBean.getAcType()!=null){
                        if(instituteProposalSpecialReviewBean.getAcType().equalsIgnoreCase("I")){
                            maxReviewNumber = maxReviewNumber+1;
                            instituteProposalSpecialReviewBean.setSpecialReviewNumber(maxReviewNumber);
                        }
                        procedures.add(addUpdInstituteProposalSpecialReview(instituteProposalSpecialReviewBean));
                        
                        //Added for COEUSQA-3119- Need to implement IACUC link to Award, IP, Prop Dev, and IRB - start
                        
                        int spRevCode = instituteProposalSpecialReviewBean.getSpecialReviewCode();
                        //if inserted special review is of type Human Subjects then linking has to be done between IACUC Protocol and IP
                        if(spRevCode == HUMAN_SUBJECTS_CODE){
                            //For the Coeus Enhancement :1799 start step:3
                        procedures.addAll(performProtocolLinkFromInstProp(instituteProposalSpecialReviewBean));
                        //End Coeus Enhancement case:#1799  step:3
                        //if inserted special review is of type Animal Subjects then linking has to be done between IACUC Protocol and IP
                        }else if(spRevCode == ANIMAL_USAGE_CODE){
                            procedures.addAll(performIACUCProtocolLinkFromInstProp(instituteProposalSpecialReviewBean));
                        }
                        
                       //Added for COEUSQA-3119- Need to implement IACUC link to Award, IP, Prop Dev, and IRB - end
                    }
                }
            }
            
            //Update Comments
            // Code commented by Shivakumar for CLOB implementation - BEGIN
//            cvInstituteProposalData = null;
//            cvInstituteProposalData = (CoeusVector)instituteProposalData.get(InstituteProposalCommentsBean.class);
//            if(cvInstituteProposalData!=null){
//                //Process Sequence Logic
//                if(isChildRecordChanged(cvInstituteProposalData)){
//                    cvInstituteProposalData = sequenceLogic.processDetails(cvInstituteProposalData, true);                
//                }
//                InstituteProposalCommentsBean instituteProposalCommentsBean = null;
//                for(int row = 0; row < cvInstituteProposalData.size(); row++){
//                    instituteProposalCommentsBean = (InstituteProposalCommentsBean)cvInstituteProposalData.elementAt(row);
//                    if(instituteProposalCommentsBean!=null && instituteProposalCommentsBean.getAcType()!=null){
//                        procedures.add(addUpdInstituteProposalComments(instituteProposalCommentsBean));
//                    }
//                }
//            }
            // Code commented by Shivakumar for CLOB implementation - END
            
            //Custom Elements
            cvInstituteProposalData = null;
            cvInstituteProposalData = (CoeusVector)instituteProposalData.get(InstituteProposalCustomDataBean.class);
            if(cvInstituteProposalData!=null){
                //Process Sequence Logic
                if(isChildRecordChanged(cvInstituteProposalData)){
                    cvInstituteProposalData = sequenceLogic.processDetails(cvInstituteProposalData, true);                
                }
                InstituteProposalCustomDataBean instituteProposalCustomDataBean = null;
                for(int row = 0; row < cvInstituteProposalData.size(); row++){
                    instituteProposalCustomDataBean = (InstituteProposalCustomDataBean)cvInstituteProposalData.elementAt(row);
                    if(instituteProposalCustomDataBean!=null && instituteProposalCustomDataBean.getAcType()!=null){
                        procedures.add(addUpdInstituteProposalCustomData(instituteProposalCustomDataBean));
                    }
                }
            }
            
            //Delete Funding proposals if selected by User in Unlock Proposal Option
            cvInstituteProposalData = null;
            cvInstituteProposalData = (CoeusVector)instituteProposalData.get(AwardFundingProposalBean.class);
            if(cvInstituteProposalData!=null){
                AwardFundingProposalBean awardFundingProposalBean = null;
                AwardUpdateTxnBean awardUpdateTxnBean = new AwardUpdateTxnBean(userId);
                for(int row = 0; row < cvInstituteProposalData.size(); row++){
                    awardFundingProposalBean = (AwardFundingProposalBean)cvInstituteProposalData.elementAt(row);
                    if(awardFundingProposalBean!=null && awardFundingProposalBean.getAcType()!=null){
                        procedures.add(awardUpdateTxnBean.addUpdAwardFundingProposal(awardFundingProposalBean));
                    }
                }
            }
            
            //If insert mode update corresponding Proposal Log status as Submitted only
            //if sequence Number is 1
            
            if(instituteProposalBean.getAcType().equalsIgnoreCase("I") && instituteProposalBean.getSequenceNumber()==1){
                procedures.add(updateProposalLogStatus(instituteProposalBean.getProposalNumber()));
            }
            // Code commented by Shivakumar for CLOB implementation - BEGIN
//            if(dbEngine!=null){
//                dbEngine.executeStoreProcs(procedures);
//            }else{
//                throw new CoeusException("db_exceptionCode.1000");
//            }
            // Code commented by Shivakumar for CLOB implementation - END
            
            // Code added by Shivakumar for CLOB implementation - BEGIN
            if(dbEngine!=null){
                java.sql.Connection conn = null;
                try{
                    conn = dbEngine.beginTxn();
                    dbEngine.executeStoreProcs(procedures,conn);
                    cvInstituteProposalData = null;
                    cvInstituteProposalData = (CoeusVector)instituteProposalData.get(InstituteProposalCommentsBean.class);
                    if(cvInstituteProposalData!=null){
                        Vector vecProcParameters = new Vector();
                        //Process Sequence Logic
                        if(isChildRecordChanged(cvInstituteProposalData)){
                            cvInstituteProposalData = sequenceLogic.processDetails(cvInstituteProposalData, true);                
                        }
                        InstituteProposalCommentsBean instituteProposalCommentsBean = null;
                        for(int row = 0; row < cvInstituteProposalData.size(); row++){
                            instituteProposalCommentsBean = (InstituteProposalCommentsBean)cvInstituteProposalData.elementAt(row);
                            if(instituteProposalCommentsBean!=null && instituteProposalCommentsBean.getAcType()!=null){
                                vecProcParameters.add(addUpdInstituteProposalComments(instituteProposalCommentsBean));
                            }
                        }
                        // Checking the Vector size before executing. 
                        if((vecProcParameters != null) && (vecProcParameters.size() > 0)){
                            dbEngine.batchSQLUpdate(vecProcParameters, conn);
                        }
                    }
                    dbEngine.commit(conn);
                    unLockProtocols(protocolData);
                    //Added for COEUSQA-3119- Need to implement IACUC link to Award, IP, Prop Dev, and IRB - start
                    unLockIacucProtocols(iacucProtocolData);
                    //Added for COEUSQA-3119- Need to implement IACUC link to Award, IP, Prop Dev, and IRB - end
                }catch(Exception sqlEx){
                    dbEngine.rollback(conn);
                    throw new CoeusException(sqlEx.getMessage());
                }finally{
                    dbEngine.endTxn(conn);
                }
            }else{
                throw new CoeusException("db_exceptionCode.1000");
            }
            // Code added by Shivakumar for CLOB implementation - END
            
            success = true;
        }
        return success;        
    }
    
    //Added for the Coeus Enhancement case:#1799 start step:4
    //Insert a message whenever the proposal is being inserted to the funding sources thru linking
    public ProcReqParameter  updateProtocolNotePad(ProtocolFundingSourceBean protocolFundingSourceBean) throws CoeusException,DBException{
        ProtocolDataTxnBean protocolDataTxnBean = new ProtocolDataTxnBean();
        ProtocolNotepadBean protocolNotepadBean = new ProtocolNotepadBean();
        protocolNotepadBean.setProtocolNumber(protocolFundingSourceBean.getProtocolNumber());
        protocolNotepadBean.setSequenceNumber(protocolFundingSourceBean.getSequenceNumber());
        int maxEntryNumber = protocolDataTxnBean.getMaxProtocolNotesEntryNumber(protocolFundingSourceBean.getProtocolNumber());
        maxEntryNumber = maxEntryNumber + 1;
        protocolNotepadBean.setEntryNumber(maxEntryNumber);
        if(protocolFundingSourceBean.getAcType() != null && protocolFundingSourceBean.getAcType().equals("I")){
           CoeusMessageResourcesBean coeusMessageResourcesBean
                                =new CoeusMessageResourcesBean();
            String insertComments = coeusMessageResourcesBean.parseMessageKey("notepadInsert_exceptionCode.6000");
            protocolNotepadBean.setComments(insertComments);
        }else{
            CoeusMessageResourcesBean coeusMessageResourcesBean =new CoeusMessageResourcesBean();
            String deleteComments = coeusMessageResourcesBean.parseMessageKey("notepadDelete_exceptionCode.6001");
            protocolNotepadBean.setComments(deleteComments);
        }
        
        protocolNotepadBean.setRestrictedFlag(false);
        protocolNotepadBean.setAcType("I");
        protocolNotepadBean.setUpdateTimestamp(dbTimestamp);
        ProtocolUpdateTxnBean protocolUpdateTxnBean = new ProtocolUpdateTxnBean(userId);
        return protocolUpdateTxnBean.addUpdProtocolNotepad(protocolNotepadBean);
    }
     
    //to insert to the protocol links table when the linking takes place from the institute
     private ProcReqParameter updateProtocolLink(ProtocolFundingSourceBean protocolFundingSourceBean,InstituteProposalSpecialReviewBean instituteProposalSpecialReviewBean) throws CoeusException,DBException{
            ProtocolLinkBean protocolLinkBean = new ProtocolLinkBean();
            protocolLinkBean.setProtocolNumber(protocolFundingSourceBean.getProtocolNumber());
            protocolLinkBean.setSequenceNumber(protocolFundingSourceBean.getSequenceNumber());
            protocolLinkBean.setModuleCode(2);
            protocolLinkBean.setModuleItemKey(instituteProposalSpecialReviewBean.getProposalNumber());
            protocolLinkBean.setModuleItemSeqNumber(instituteProposalSpecialReviewBean.getSequenceNumber());
            if(protocolFundingSourceBean.getAcType() != null && protocolFundingSourceBean.getAcType().equals("I")){
                CoeusMessageResourcesBean coeusMessageResourcesBean =new CoeusMessageResourcesBean();
                String insertComments = coeusMessageResourcesBean.parseMessageKey("notepadInsert_exceptionCode.6000");
                protocolLinkBean.setComments(insertComments);
                protocolLinkBean.setActionType("I");
            }else{
                CoeusMessageResourcesBean coeusMessageResourcesBean = new CoeusMessageResourcesBean();
                String deleteComments = coeusMessageResourcesBean.parseMessageKey("notepadDelete_exceptionCode.6001");
                protocolLinkBean.setComments(deleteComments);
                protocolLinkBean.setActionType("D");
            }
            ProtocolUpdateTxnBean protocolUpdateTxnBean = new ProtocolUpdateTxnBean(userId);
            return protocolUpdateTxnBean.addUpdProtocolLinks(protocolLinkBean);
            
        }
    //End Coeus Enhancement case:#1799 step:4

    /** Method used to update/insert Institute Proposal details
     *
     * @return ProcReqParameter to execute the Procedure
     * @param instituteProposalBean InstituteProposalBean
     * @exception DBException if the instance of a dbEngine is null.
     * @exception CoeusException if the DB instance is not available. 
     */
    public ProcReqParameter updateInstituteProposal(InstituteProposalBean instituteProposalBean) 
            throws CoeusException ,DBException{
        Vector param = new Vector();        
        param.addElement(new Parameter("PROPOSAL_NUMBER",
             DBEngineConstants.TYPE_STRING,
             instituteProposalBean.getProposalNumber()));
        param.addElement(new Parameter("SEQUENCE_NUMBER",
            DBEngineConstants.TYPE_INT,
            ""+instituteProposalBean.getSequenceNumber()));
        param.addElement(new Parameter("SPONSOR_PROPOSAL_NUMBER",
            DBEngineConstants.TYPE_STRING,
            instituteProposalBean.getSponsorProposalNumber()));
        param.addElement(new Parameter("PROPOSAL_TYPE_CODE",
            DBEngineConstants.TYPE_INT,
            ""+instituteProposalBean.getProposalTypeCode()));        
        param.addElement(new Parameter("CURRENT_ACCOUNT_NUMBER",
            DBEngineConstants.TYPE_STRING,
            instituteProposalBean.getCurrentAccountNumber()));                
        param.addElement(new Parameter("TITLE",
            DBEngineConstants.TYPE_STRING,
            instituteProposalBean.getTitle()));
        param.addElement(new Parameter("SPONSOR_CODE",
            DBEngineConstants.TYPE_STRING,
            instituteProposalBean.getSponsorCode()));
        param.addElement(new Parameter("PRIME_SPONSOR_CODE",
            DBEngineConstants.TYPE_STRING,
            instituteProposalBean.getPrimeSponsorCode()));  
        //Added by shiji for fixing bug id : 1905 : start
        if(instituteProposalBean.getRolodexId()==0){
            param.addElement(new Parameter("ROLODEX_ID",
            DBEngineConstants.TYPE_STRING,
            null));
        }else{
            param.addElement(new Parameter("ROLODEX_ID",
            DBEngineConstants.TYPE_INT,
            ""+instituteProposalBean.getRolodexId()));
        }
        //bug id : 1905 : end
        if(instituteProposalBean.getNoticeOfOpportunityCode()==0){
            param.addElement(new Parameter("NOTICE_OF_OPPORTUNITY_CODE",
                DBEngineConstants.TYPE_STRING,
                null));
        }else{
            param.addElement(new Parameter("NOTICE_OF_OPPORTUNITY_CODE",
                DBEngineConstants.TYPE_INT,
                ""+instituteProposalBean.getNoticeOfOpportunityCode()));                
        }
        param.addElement(new Parameter("GRAD_STUD_HEADCOUNT",
            DBEngineConstants.TYPE_INT,
            ""+instituteProposalBean.getGradStudHeadCount()));
        param.addElement(new Parameter("GRAD_PERSON_MONTHS",
            DBEngineConstants.TYPE_DOUBLE,
            ""+instituteProposalBean.getGradStudPersonMonths()));
        param.addElement(new Parameter("TYPE_OF_ACCOUNT",
            DBEngineConstants.TYPE_STRING,
            instituteProposalBean.getTypeOfAccount()));
        param.addElement(new Parameter("ACTIVITY_TYPE_CODE",
            DBEngineConstants.TYPE_INT,
            ""+instituteProposalBean.getProposalActivityTypeCode()));
        param.addElement(new Parameter("REQUESTED_START_DATE_INIT",
            DBEngineConstants.TYPE_DATE,
            instituteProposalBean.getRequestStartDateInitial()));
        param.addElement(new Parameter("REQUESTED_START_DATE_TOTAL",
            DBEngineConstants.TYPE_DATE,
            instituteProposalBean.getRequestStartDateTotal()));
        param.addElement(new Parameter("REQUESTED_END_DATE_INIT",
            DBEngineConstants.TYPE_DATE,
            instituteProposalBean.getRequestEndDateInitial()));
        param.addElement(new Parameter("REQUESTED_END_DATE_TOTAL",
            DBEngineConstants.TYPE_DATE,
            instituteProposalBean.getRequestEndDateTotal()));            
        param.addElement(new Parameter("TOTAL_DIRECT_COST_INIT",
            DBEngineConstants.TYPE_DOUBLE,
            ""+instituteProposalBean.getTotalDirectCostInitial()));
        param.addElement(new Parameter("TOTAL_DIRECT_COST_TOTAL",
            DBEngineConstants.TYPE_DOUBLE,
            ""+instituteProposalBean.getTotalDirectCostTotal()));
        param.addElement(new Parameter("TOTAL_INDIRECT_COST_INIT",
            DBEngineConstants.TYPE_DOUBLE,
            ""+instituteProposalBean.getTotalInDirectCostInitial()));
        param.addElement(new Parameter("TOTAL_INDIRECT_COST_TOTAL",
            DBEngineConstants.TYPE_DOUBLE,
            ""+instituteProposalBean.getTotalInDirectCostTotal()));
        param.addElement(new Parameter("NUMBER_OF_COPIES",
            DBEngineConstants.TYPE_STRING,
            instituteProposalBean.getNumberOfCopies()));
        param.addElement(new Parameter("DEADLINE_DATE",
            DBEngineConstants.TYPE_DATE,
            instituteProposalBean.getDeadLineDate()));            
        param.addElement(new Parameter("DEADLINE_TYPE",
            DBEngineConstants.TYPE_STRING,
            new Character(instituteProposalBean.getDeadLineType()).toString()));
        param.addElement(new Parameter("MAIL_BY",
            DBEngineConstants.TYPE_STRING,
            new Character(instituteProposalBean.getMailBy()).toString()));
        param.addElement(new Parameter("MAIL_TYPE",
            DBEngineConstants.TYPE_STRING,
            new Character(instituteProposalBean.getMailType()).toString()));
        param.addElement(new Parameter("MAIL_ACCOUNT_NUMBER",
            DBEngineConstants.TYPE_STRING,
            instituteProposalBean.getMailAccountNumber()));
        param.addElement(new Parameter("SUBCONTRACT_FLAG",
            DBEngineConstants.TYPE_STRING,
            instituteProposalBean.isSubcontractFlag() == false ? "N" : "Y"));
        param.addElement(new Parameter("COST_SHARING_INDICATOR",
            DBEngineConstants.TYPE_STRING,
            instituteProposalBean.getCostSharingIndicator()));
        param.addElement(new Parameter("IDC_RATE_INDICATOR",
            DBEngineConstants.TYPE_STRING,
            instituteProposalBean.getIdcRateIndicator()));
        param.addElement(new Parameter("SPECIAL_REVIEW_INDICATOR",
            DBEngineConstants.TYPE_STRING,
            instituteProposalBean.getSpecialReviewIndicator()));
        param.addElement(new Parameter("SCIENCE_CODE_INDICATOR",
            DBEngineConstants.TYPE_STRING,
            instituteProposalBean.getScienceCodeIndicator()));
//         3823: Key person record needed in IP and Award
        param.addElement(new Parameter("KEY_PERSON_INDICATOR",
                DBEngineConstants.TYPE_STRING,
                instituteProposalBean.getKeyPersonIndicator()));
        param.addElement(new Parameter("NSF_CODE",
            DBEngineConstants.TYPE_STRING,
            instituteProposalBean.getNsfCode()));        
        if(instituteProposalBean.getAcType().equalsIgnoreCase("I") && instituteProposalBean.getMode() == NEW_MODE){
            param.addElement(new Parameter("CREATE_TIMESTAMP",
                DBEngineConstants.TYPE_TIMESTAMP,dbTimestamp));
            param.addElement(new Parameter("INITIAL_CONTRACT_ADMIN",
                DBEngineConstants.TYPE_STRING,
                instituteProposalBean.getInitialContractAdmin()));            
        }else if(instituteProposalBean.getAcType().equalsIgnoreCase("I") && instituteProposalBean.getMode() == NEW_ENTRY_MODE){
            param.addElement(new Parameter("CREATE_TIMESTAMP",
                DBEngineConstants.TYPE_TIMESTAMP,
                instituteProposalBean.getCreateTimeStamp())); 
            param.addElement(new Parameter("INITIAL_CONTRACT_ADMIN",
                DBEngineConstants.TYPE_STRING,instituteProposalBean.getInitialContractAdmin()));
        }else if(!instituteProposalBean.getAcType().equalsIgnoreCase("I")){
            param.addElement(new Parameter("CREATE_TIMESTAMP",
                DBEngineConstants.TYPE_TIMESTAMP,
                instituteProposalBean.getCreateTimeStamp()));    
            param.addElement(new Parameter("INITIAL_CONTRACT_ADMIN",
                DBEngineConstants.TYPE_STRING,instituteProposalBean.getInitialContractAdmin()));
        }   
        //Bug Fix : 2118 - START 1
        if(instituteProposalBean.getIpReviewRequestTypeCode() == 0){//need to set as null
            param.addElement(new Parameter("IP_REVIEW_REQ_TYPE_CODE",
            DBEngineConstants.TYPE_STRING, null));
        }else{
            param.addElement(new Parameter("IP_REVIEW_REQ_TYPE_CODE",
            DBEngineConstants.TYPE_INT, ""+instituteProposalBean.getIpReviewRequestTypeCode()));
        }
        //Bug Fix : 2118 - END 1
        param.addElement(new Parameter("REVIEW_SUBMISSION_DATE",
            DBEngineConstants.TYPE_DATE,
            instituteProposalBean.getReviewSubmissionDate()));
        param.addElement(new Parameter("REVIEW_RECEIVE_DATE",
            DBEngineConstants.TYPE_DATE,
            instituteProposalBean.getReviewReceiveDate()));
        
        //Bug Fix : 2118 - START 2
        if(instituteProposalBean.getReviewResultCode() == 0) {//need to set as null
            param.addElement(new Parameter("REVIEW_RESULT_CODE",
            DBEngineConstants.TYPE_STRING, null));
        }else{
            param.addElement(new Parameter("REVIEW_RESULT_CODE",
            DBEngineConstants.TYPE_INT,
            ""+instituteProposalBean.getReviewResultCode()));
        }
        //Bug Fix : 2118 - END 2
        
        param.addElement(new Parameter("IP_REVIEWER",
            DBEngineConstants.TYPE_STRING,
            instituteProposalBean.getIpReviewer()));
        param.addElement(new Parameter("IP_REV_ACTIVITY_INDICATOR",
            DBEngineConstants.TYPE_STRING,
            instituteProposalBean.getIpReviewActivityIndicator()));
        param.addElement(new Parameter("CURRENT_AWARD_NUMBER",
            DBEngineConstants.TYPE_STRING,
            instituteProposalBean.getCurrentAwardNumber()));
        param.addElement(new Parameter("STATUS_CODE",
            DBEngineConstants.TYPE_INT,
            ""+instituteProposalBean.getStatusCode()));
        param.addElement(new Parameter("UPDATE_USER",
            DBEngineConstants.TYPE_STRING, userId));
        param.addElement(new Parameter("UPDATE_TIMESTAMP",
            DBEngineConstants.TYPE_TIMESTAMP,dbTimestamp));
        //Enhancement to include cfda number and opportunity number
        //Case # 2097
        param.addElement(new Parameter("CFDA_NUMBER",
            DBEngineConstants.TYPE_STRING,instituteProposalBean.getCfdaNumber()));
        param.addElement(new Parameter("OPPORTUNITY",
            DBEngineConstants.TYPE_STRING,instituteProposalBean.getOpportunity()));
        // Added for Case 2162  - adding Award Type - Start 
        param.addElement(new Parameter("AWARD_TYPE_CODE",
                    DBEngineConstants.TYPE_STRING,
                        instituteProposalBean.getAwardTypeCode()== 0 ? null :
                         ""+instituteProposalBean.getAwardTypeCode() ));
        // Added for Case 2162  - adding Award Type - End
        param.addElement(new Parameter("AW_PROPOSAL_NUMBER",
             DBEngineConstants.TYPE_STRING,
             instituteProposalBean.getProposalNumber()));
        param.addElement(new Parameter("AW_SEQUENCE_NUMBER",
            DBEngineConstants.TYPE_INT,
            ""+instituteProposalBean.getSequenceNumber()));
        param.addElement(new Parameter("AW_UPDATE_TIMESTAMP",
            DBEngineConstants.TYPE_TIMESTAMP, instituteProposalBean.getUpdateTimestamp()));
        param.addElement(new Parameter("AC_TYPE",
            DBEngineConstants.TYPE_STRING,
            instituteProposalBean.getAcType()));

        // 3823: Key Person Records Needed in Inst Proposal and Award - Start
//        StringBuffer sql = new StringBuffer(
//                                        "call DW_UPDATE_INST_PROPOSAL(");
        StringBuffer sql = new StringBuffer("call UPDATE_INST_PROPOSAL(");
        // 3823: Key Person Records Needed in Inst Proposal and Award - End
        
        sql.append(" <<PROPOSAL_NUMBER>> , ");
        sql.append(" <<SEQUENCE_NUMBER>> , ");
        sql.append(" <<SPONSOR_PROPOSAL_NUMBER>> , ");
        sql.append(" <<PROPOSAL_TYPE_CODE>> , ");
        sql.append(" <<CURRENT_ACCOUNT_NUMBER>> , ");
        sql.append(" <<TITLE>> , ");
        sql.append(" <<SPONSOR_CODE>> , ");
        sql.append(" <<PRIME_SPONSOR_CODE>> , ");
        sql.append(" <<ROLODEX_ID>> , ");
        sql.append(" <<NOTICE_OF_OPPORTUNITY_CODE>> , ");                
        sql.append(" <<GRAD_STUD_HEADCOUNT>> , ");
        sql.append(" <<GRAD_PERSON_MONTHS>> , ");                
        sql.append(" <<TYPE_OF_ACCOUNT>> , ");
        sql.append(" <<ACTIVITY_TYPE_CODE>> , ");
        sql.append(" <<REQUESTED_START_DATE_INIT>> , ");
        sql.append(" <<REQUESTED_START_DATE_TOTAL>> , ");
        sql.append(" <<REQUESTED_END_DATE_INIT>> , ");
        sql.append(" <<REQUESTED_END_DATE_TOTAL>> , ");
        sql.append(" <<TOTAL_DIRECT_COST_INIT>> , ");
        sql.append(" <<TOTAL_DIRECT_COST_TOTAL>> , ");
        sql.append(" <<TOTAL_INDIRECT_COST_INIT>> , ");
        sql.append(" <<TOTAL_INDIRECT_COST_TOTAL>> , ");
        sql.append(" <<NUMBER_OF_COPIES>> , ");
        sql.append(" <<DEADLINE_DATE>> , ");
        sql.append(" <<DEADLINE_TYPE>> , ");
        sql.append(" <<MAIL_BY>> , ");
        sql.append(" <<MAIL_TYPE>> , ");
        sql.append(" <<MAIL_ACCOUNT_NUMBER>> , ");
        sql.append(" <<SUBCONTRACT_FLAG>> , ");
        sql.append(" <<COST_SHARING_INDICATOR>> , ");
        sql.append(" <<IDC_RATE_INDICATOR>> , ");
        sql.append(" <<SPECIAL_REVIEW_INDICATOR>> , ");
        sql.append(" <<SCIENCE_CODE_INDICATOR>> , ");   
        // 3823: Key Person record needed in IP and Award
        sql.append(" <<KEY_PERSON_INDICATOR>> , ");            
        sql.append(" <<NSF_CODE>> , ");
        sql.append(" <<CREATE_TIMESTAMP>> , ");
        sql.append(" <<INITIAL_CONTRACT_ADMIN>> , ");
        sql.append(" <<IP_REVIEW_REQ_TYPE_CODE>> , ");
        sql.append(" <<REVIEW_SUBMISSION_DATE>> , ");
        sql.append(" <<REVIEW_RECEIVE_DATE>> , ");
        sql.append(" <<REVIEW_RESULT_CODE>> , ");
        sql.append(" <<IP_REVIEWER>> , ");
        sql.append(" <<IP_REV_ACTIVITY_INDICATOR>> , ");            
        sql.append(" <<CURRENT_AWARD_NUMBER>> , ");
        sql.append(" <<STATUS_CODE>> , ");            
        sql.append(" <<UPDATE_USER>> , ");
        sql.append(" <<UPDATE_TIMESTAMP>> , ");
        sql.append(" <<CFDA_NUMBER>> , ");
        sql.append(" <<OPPORTUNITY>> , ");
        // Added for Case 2162  - adding Award Type - Start 
        sql.append(" <<AWARD_TYPE_CODE>> , ");
        // Added for Case 2162  - adding Award Type - End
        sql.append(" <<AW_PROPOSAL_NUMBER>> , ");
        sql.append(" <<AW_SEQUENCE_NUMBER>> , ");
        sql.append(" <<AW_UPDATE_TIMESTAMP>> , ");
        sql.append(" <<AC_TYPE>> )");

        ProcReqParameter procReqParameter = new ProcReqParameter();
        procReqParameter.setDSN(DSN);
        procReqParameter.setParameterInfo(param);
        procReqParameter.setSqlCommand(sql.toString()); 

        return procReqParameter;        
    }
    
    
    /** Method used to update/insert Institute Proposal IDC Rates
     *  <li>To fetch the data, it uses DW_UPDATE_INST_PROP_IDC_RATE procedure.
     *
     * @return boolean true for successful insert/modify
     * @param instituteProposalIDCRateBean InstituteProposalIDCRateBean
     * @exception DBException if the instance of a dbEngine is null.
     * @exception CoeusException if the DB instance is not available. */
    public ProcReqParameter addUpdProposalIDCRates( InstituteProposalIDCRateBean instituteProposalIDCRateBean )  
        throws CoeusException ,DBException{
        Vector param = new Vector();
        param = new Vector();        
        param.addElement(new Parameter("PROPOSAL_NUMBER",
            DBEngineConstants.TYPE_STRING,
            instituteProposalIDCRateBean.getProposalNumber()));
        param.addElement(new Parameter("SEQUENCE_NUMBER",
            DBEngineConstants.TYPE_INT,
            ""+instituteProposalIDCRateBean.getSequenceNumber()));
        param.addElement(new Parameter("FISCAL_YEAR",
            DBEngineConstants.TYPE_STRING,
            instituteProposalIDCRateBean.getFiscalYear()));
        param.addElement(new Parameter("IDC_RATE_TYPE_CODE",
            DBEngineConstants.TYPE_INT,
            ""+instituteProposalIDCRateBean.getIdcRateTypeCode()));
        param.addElement(new Parameter("ON_CAMPUS_FLAG",
            DBEngineConstants.TYPE_STRING,
            instituteProposalIDCRateBean.isOnOffCampusFlag() == false ? "N" : "Y"));
        param.addElement(new Parameter("APPLICABLE_IDC_RATE",
            DBEngineConstants.TYPE_DOUBLE,
            ""+instituteProposalIDCRateBean.getApplicableIDCRate()));
        param.addElement(new Parameter("UNDERRECOVERY_OF_IDC",
            DBEngineConstants.TYPE_DOUBLE,
            ""+instituteProposalIDCRateBean.getUnderRecoveryIDC()));
        param.addElement(new Parameter("SOURCE_ACCOUNT",
            DBEngineConstants.TYPE_STRING,
            instituteProposalIDCRateBean.getSourceAccount()));           
        param.addElement(new Parameter("UPDATE_TIMESTAMP",
            DBEngineConstants.TYPE_TIMESTAMP,dbTimestamp));
        param.addElement(new Parameter("UPDATE_USER",
            DBEngineConstants.TYPE_STRING, userId));
        param.addElement(new Parameter("AW_PROPOSAL_NUMBER",
             DBEngineConstants.TYPE_STRING,
             instituteProposalIDCRateBean.getProposalNumber()));
        param.addElement(new Parameter("AW_SEQUENCE_NUMBER",
            DBEngineConstants.TYPE_INT,
            ""+instituteProposalIDCRateBean.getSequenceNumber()));
        param.addElement(new Parameter("AW_APPLICABLE_IDC_RATE",
            DBEngineConstants.TYPE_DOUBLE,
            ""+instituteProposalIDCRateBean.getAw_ApplicableIDCRate()));        
        param.addElement(new Parameter("AW_FISCAL_YEAR",
            DBEngineConstants.TYPE_STRING,
            instituteProposalIDCRateBean.getAw_FiscalYear()));        
        param.addElement(new Parameter("AW_IDC_RATE_TYPE_CODE",
            DBEngineConstants.TYPE_INT,
            ""+instituteProposalIDCRateBean.getAw_IdcRateTypeCode()));
        param.addElement(new Parameter("AW_ON_CAMPUS_FLAG",
            DBEngineConstants.TYPE_STRING,
            instituteProposalIDCRateBean.isAw_OnOffCampusFlag() == false ? "N" : "Y"));
        param.addElement(new Parameter("AW_SOURCE_ACCOUNT",
            DBEngineConstants.TYPE_STRING,
            instituteProposalIDCRateBean.getAw_SourceAccount()));           
        param.addElement(new Parameter("AW_UPDATE_TIMESTAMP",
            DBEngineConstants.TYPE_TIMESTAMP, instituteProposalIDCRateBean.getUpdateTimestamp()));
        param.addElement(new Parameter("AC_TYPE",
            DBEngineConstants.TYPE_STRING,
            instituteProposalIDCRateBean.getAcType()));

        StringBuffer sql = new StringBuffer(
                                        "call DW_UPDATE_INST_PROP_IDC_RATE(");
        sql.append(" <<PROPOSAL_NUMBER>> , ");
        sql.append(" <<SEQUENCE_NUMBER>> , ");
        sql.append(" <<FISCAL_YEAR>> , ");
        sql.append(" <<IDC_RATE_TYPE_CODE>> , ");
        sql.append(" <<ON_CAMPUS_FLAG>> , ");
        sql.append(" <<APPLICABLE_IDC_RATE>> , ");
        sql.append(" <<UNDERRECOVERY_OF_IDC>> , ");
        sql.append(" <<SOURCE_ACCOUNT>> , ");        
        sql.append(" <<UPDATE_TIMESTAMP>> , ");
        sql.append(" <<UPDATE_USER>> , ");        
        sql.append(" <<AW_PROPOSAL_NUMBER>> , ");        
        sql.append(" <<AW_SEQUENCE_NUMBER>> , ");
        sql.append(" <<AW_APPLICABLE_IDC_RATE>> , ");        
        sql.append(" <<AW_FISCAL_YEAR>> , ");
        sql.append(" <<AW_IDC_RATE_TYPE_CODE>> , ");
        sql.append(" <<AW_ON_CAMPUS_FLAG>> , ");
        sql.append(" <<AW_SOURCE_ACCOUNT>> , ");
        sql.append(" <<AW_UPDATE_TIMESTAMP>> , ");
        sql.append(" <<AC_TYPE>> )");

        ProcReqParameter procReqParameter = new ProcReqParameter();
        procReqParameter.setDSN(DSN);
        procReqParameter.setParameterInfo(param);
        procReqParameter.setSqlCommand(sql.toString());        
        return procReqParameter;
    }
    
    /** Method used to update/insert Institute Proposal Comments
     * <li>To fetch the data, it uses DW_UPDATE_INST_PROP_COMMENTS procedure.
     *
     * @return boolean true for successful insert/modify
     * @param instituteProposalCommentsBean InstituteProposalCommentsBean
     * @exception DBException if the instance of a dbEngine is null.
     * @exception CoeusException if the DB instance is not available. */
    
    // Commented by Shivakumar for CLOB implementation - BEGIN
//    public ProcReqParameter addUpdInstituteProposalComments( InstituteProposalCommentsBean instituteProposalCommentsBean )  
//        throws CoeusException ,DBException{
//        Vector param = new Vector();        
//        param = new Vector();        
//        param.addElement(new Parameter("PROPOSAL_NUMBER",
//            DBEngineConstants.TYPE_STRING,
//            instituteProposalCommentsBean.getProposalNumber()));
//        param.addElement(new Parameter("SEQUENCE_NUMBER",
//            DBEngineConstants.TYPE_INT,
//            ""+instituteProposalCommentsBean.getSequenceNumber()));
//        param.addElement(new Parameter("COMMENT_CODE",
//            DBEngineConstants.TYPE_INT,
//            ""+instituteProposalCommentsBean.getCommentCode()));
//        param.addElement(new Parameter("COMMENTS",
//            DBEngineConstants.TYPE_STRING,
//            instituteProposalCommentsBean.getComments()));
//        param.addElement(new Parameter("UPDATE_TIMESTAMP",
//            DBEngineConstants.TYPE_TIMESTAMP,dbTimestamp));
//        param.addElement(new Parameter("UPDATE_USER",
//            DBEngineConstants.TYPE_STRING, userId));
//        param.addElement(new Parameter("AW_PROPOSAL_NUMBER",
//             DBEngineConstants.TYPE_STRING,
//             instituteProposalCommentsBean.getProposalNumber()));
//        param.addElement(new Parameter("AW_SEQUENCE_NUMBER",
//            DBEngineConstants.TYPE_INT,
//            ""+instituteProposalCommentsBean.getSequenceNumber()));
//        param.addElement(new Parameter("AW_COMMENT_CODE",
//            DBEngineConstants.TYPE_DOUBLE,
//            ""+instituteProposalCommentsBean.getCommentCode()));
//        param.addElement(new Parameter("AW_UPDATE_TIMESTAMP",
//            DBEngineConstants.TYPE_TIMESTAMP, instituteProposalCommentsBean.getUpdateTimestamp()));
//        param.addElement(new Parameter("AC_TYPE",
//            DBEngineConstants.TYPE_STRING,
//            instituteProposalCommentsBean.getAcType()));
//
//        StringBuffer sql = new StringBuffer(
//                                        "call DW_UPDATE_INST_PROP_COMMENTS(");
//        sql.append(" <<PROPOSAL_NUMBER>> , ");
//        sql.append(" <<SEQUENCE_NUMBER>> , ");
//        sql.append(" <<COMMENT_CODE>> , ");
//        sql.append(" <<COMMENTS>> , ");
//        sql.append(" <<UPDATE_TIMESTAMP>> , ");
//        sql.append(" <<UPDATE_USER>> , ");        
//        sql.append(" <<AW_PROPOSAL_NUMBER>> , ");        
//        sql.append(" <<AW_SEQUENCE_NUMBER>> , ");
//        sql.append(" <<AW_COMMENT_CODE>> , ");        
//        sql.append(" <<AW_UPDATE_TIMESTAMP>> , ");
//        sql.append(" <<AC_TYPE>> )");
//
//        ProcReqParameter procReqParameter = new ProcReqParameter();
//        procReqParameter.setDSN(DSN);
//        procReqParameter.setParameterInfo(param);
//        procReqParameter.setSqlCommand(sql.toString());
//        return procReqParameter;
//    }
    // Commented by Shivakumar for CLOB implementation - END
    
    // Code added  by Shivakumar for CLOB implementation - BEGIN
    
    public ProcReqParameter addUpdInstituteProposalComments( InstituteProposalCommentsBean instituteProposalCommentsBean )  
        throws CoeusException ,DBException{
        Vector param = new Vector();        
        param = new Vector();        
        param.addElement(new Parameter("PROPOSAL_NUMBER",
            DBEngineConstants.TYPE_STRING,
            instituteProposalCommentsBean.getProposalNumber()));
        param.addElement(new Parameter("SEQUENCE_NUMBER",
            DBEngineConstants.TYPE_INT,
            ""+instituteProposalCommentsBean.getSequenceNumber()));
        param.addElement(new Parameter("COMMENT_CODE",
            DBEngineConstants.TYPE_INT,
            ""+instituteProposalCommentsBean.getCommentCode()));
        param.addElement(new Parameter("COMMENTS",
            DBEngineConstants.TYPE_STRING,
            instituteProposalCommentsBean.getComments()));
        param.addElement(new Parameter("UPDATE_TIMESTAMP",
            DBEngineConstants.TYPE_TIMESTAMP,dbTimestamp));
        param.addElement(new Parameter("UPDATE_USER",
            DBEngineConstants.TYPE_STRING, userId));
        
        param.addElement(new Parameter("AC_TYPE",
            DBEngineConstants.TYPE_STRING,
            instituteProposalCommentsBean.getAcType()));
        
        
        StringBuffer sqlProposal = new StringBuffer("");
        
        if(instituteProposalCommentsBean.getAcType().trim().equals("I")){
            sqlProposal.append("insert into osp$proposal_comments(");
            sqlProposal.append(" PROPOSAL_NUMBER , ");
            sqlProposal.append(" SEQUENCE_NUMBER , ");
            sqlProposal.append(" COMMENT_CODE , ");
            sqlProposal.append(" COMMENTS , ");
            sqlProposal.append(" UPDATE_TIMESTAMP , ");
            sqlProposal.append(" UPDATE_USER ) ");
            sqlProposal.append(" values (");
            sqlProposal.append(" <<PROPOSAL_NUMBER>> , ");
            sqlProposal.append(" <<SEQUENCE_NUMBER>> , ");
            sqlProposal.append(" <<COMMENT_CODE>> , ");
            sqlProposal.append(" <<COMMENTS>> , ");
            sqlProposal.append(" <<UPDATE_TIMESTAMP>> , ");
            sqlProposal.append(" <<UPDATE_USER>> ) ");
//            System.out.println("insert statement=>"+sqlBudget.toString());
        }else {
              param.addElement( new Parameter("AW_PROPOSAL_NUMBER",
                    DBEngineConstants.TYPE_STRING,
                    instituteProposalCommentsBean.getProposalNumber()));
              param.addElement(new Parameter("AW_SEQUENCE_NUMBER",
                    DBEngineConstants.TYPE_INT,
                    ""+instituteProposalCommentsBean.getSequenceNumber()));
              param.addElement(new Parameter("AW_COMMENT_CODE",
                    DBEngineConstants.TYPE_DOUBLE,
                    ""+instituteProposalCommentsBean.getCommentCode()));
              param.addElement(new Parameter("AW_UPDATE_TIMESTAMP",
                    DBEngineConstants.TYPE_TIMESTAMP, 
                    instituteProposalCommentsBean.getUpdateTimestamp()));
              if(instituteProposalCommentsBean.getAcType().trim().equals("U")){                
                sqlProposal.append("update osp$proposal_comments set");
                //Added to update the comment code - start
                sqlProposal.append(" COMMENT_CODE = ");
                sqlProposal.append(" <<COMMENT_CODE>> , ");
                //Added to update the comment code - End
                sqlProposal.append(" COMMENTS =  ");
                sqlProposal.append(" <<COMMENTS>> , ");
                sqlProposal.append(" UPDATE_TIMESTAMP = ");
                sqlProposal.append(" <<UPDATE_TIMESTAMP>> , ");
                sqlProposal.append(" UPDATE_USER = ");
                sqlProposal.append(" <<UPDATE_USER>>  ");
                sqlProposal.append(" where ");
                sqlProposal.append(" PROPOSAL_NUMBER = ");
                sqlProposal.append(" <<AW_PROPOSAL_NUMBER>> ");
                sqlProposal.append(" and SEQUENCE_NUMBER = ");
                sqlProposal.append(" <<AW_SEQUENCE_NUMBER>> ");
                sqlProposal.append(" and COMMENT_CODE = ");
                sqlProposal.append(" <<AW_COMMENT_CODE>> ");
                sqlProposal.append(" and UPDATE_TIMESTAMP = ");
                sqlProposal.append(" <<AW_UPDATE_TIMESTAMP>> ");
//                System.out.println("update statement=>"+sqlBudget.toString());
            }else if(instituteProposalCommentsBean.getAcType().trim().equals("D")){
                sqlProposal.append(" delete from osp$proposal_comments where ");
                sqlProposal.append(" PROPOSAL_NUMBER = ");
                sqlProposal.append(" <<AW_PROPOSAL_NUMBER>> ");
                sqlProposal.append(" and SEQUENCE_NUMBER = ");
                sqlProposal.append(" <<AW_SEQUENCE_NUMBER>> ");
                sqlProposal.append(" and COMMENT_CODE = ");
                sqlProposal.append(" <<AW_COMMENT_CODE>> ");
                sqlProposal.append(" and UPDATE_TIMESTAMP = ");
                sqlProposal.append(" <<AW_UPDATE_TIMESTAMP>> ");
//                System.out.println("delete statement=>"+sqlBudget.toString());
            }
        }    
        ProcReqParameter procReqParameter = new ProcReqParameter();
        procReqParameter.setDSN(DSN);
        procReqParameter.setParameterInfo(param);
        procReqParameter.setSqlCommand(sqlProposal.toString());
        return procReqParameter;
    }
    
    
    
    
    
    
    // Code added  by Shivakumar for CLOB implementation - END
    
    /** Method used to update/insert Institute Proposal Comments
     * <li>To update the data, it uses DW_UPD_INST_PROP_SCIENCE_CODE procedure.
     *
     * @return boolean true for successful insert/modify
     * @param instituteProposalScienceCodeBean InstituteProposalScienceCodeBean
     * @exception DBException if the instance of a dbEngine is null.
     * @exception CoeusException if the DB instance is not available. 
     */
    public ProcReqParameter addUpdInstituteProposalScienceCode( InstituteProposalScienceCodeBean instituteProposalScienceCodeBean )  
        throws CoeusException ,DBException{
        Vector param = new Vector();
        param = new Vector();        
        param.addElement(new Parameter("PROPOSAL_NUMBER",
            DBEngineConstants.TYPE_STRING,
            instituteProposalScienceCodeBean.getProposalNumber()));
        param.addElement(new Parameter("SEQUENCE_NUMBER",
            DBEngineConstants.TYPE_INT,
            ""+instituteProposalScienceCodeBean.getSequenceNumber()));
        param.addElement(new Parameter("SCIENCE_CODE",
            DBEngineConstants.TYPE_STRING,
            instituteProposalScienceCodeBean.getScienceCode()));
        param.addElement(new Parameter("UPDATE_TIMESTAMP",
            DBEngineConstants.TYPE_TIMESTAMP,dbTimestamp));
        param.addElement(new Parameter("UPDATE_USER",
            DBEngineConstants.TYPE_STRING, userId));
        param.addElement(new Parameter("AW_PROPOSAL_NUMBER",
             DBEngineConstants.TYPE_STRING,
             instituteProposalScienceCodeBean.getProposalNumber()));
        param.addElement(new Parameter("AW_SEQUENCE_NUMBER",
            DBEngineConstants.TYPE_INT,
            ""+instituteProposalScienceCodeBean.getSequenceNumber()));
        param.addElement(new Parameter("AW_SCIENCE_CODE",
            DBEngineConstants.TYPE_STRING,
            instituteProposalScienceCodeBean.getScienceCode()));
        param.addElement(new Parameter("AW_UPDATE_TIMESTAMP",
            DBEngineConstants.TYPE_TIMESTAMP, instituteProposalScienceCodeBean.getUpdateTimestamp()));
        param.addElement(new Parameter("AC_TYPE",
            DBEngineConstants.TYPE_STRING,
            instituteProposalScienceCodeBean.getAcType()));

        StringBuffer sql = new StringBuffer(
                                        "call DW_UPD_INST_PROP_SCIENCE_CODE(");
        sql.append(" <<PROPOSAL_NUMBER>> , ");
        sql.append(" <<SEQUENCE_NUMBER>> , ");
        sql.append(" <<SCIENCE_CODE>> , ");
        sql.append(" <<UPDATE_TIMESTAMP>> , ");
        sql.append(" <<UPDATE_USER>> , ");        
        sql.append(" <<AW_PROPOSAL_NUMBER>> , ");        
        sql.append(" <<AW_SEQUENCE_NUMBER>> , ");
        sql.append(" <<AW_SCIENCE_CODE>> , ");        
        sql.append(" <<AW_UPDATE_TIMESTAMP>> , ");
        sql.append(" <<AC_TYPE>> )");

        ProcReqParameter procReqParameter = new ProcReqParameter();
        procReqParameter.setDSN(DSN);
        procReqParameter.setParameterInfo(param);
        procReqParameter.setSqlCommand(sql.toString());        
        return procReqParameter;
    }
    
    /** Method used to update/insert Institute Proposal Cost Sharing
     *  <li>To fetch the data, it uses DW_UPDATE_INST_PROP_COST_SHARE procedure.
     *
     * @return boolean true for successful insert/modify
     * @param instituteProposalCostSharingBean InstituteProposalCostSharingBean
     * @exception DBException if the instance of a dbEngine is null.
     * @exception CoeusException if the DB instance is not available. 
     */
    public ProcReqParameter addUpdProposalCostSharing( InstituteProposalCostSharingBean instituteProposalCostSharingBean )  
        throws CoeusException ,DBException{
        Vector param = new Vector();        
        param = new Vector();        
        param.addElement(new Parameter("PROPOSAL_NUMBER",
            DBEngineConstants.TYPE_STRING,
            instituteProposalCostSharingBean.getProposalNumber()));
        param.addElement(new Parameter("SEQUENCE_NUMBER",
            DBEngineConstants.TYPE_INT,
            ""+instituteProposalCostSharingBean.getSequenceNumber()));
        param.addElement(new Parameter("FISCAL_YEAR",
            DBEngineConstants.TYPE_STRING,
            instituteProposalCostSharingBean.getFiscalYear()));
        param.addElement(new Parameter("COST_SHARING_TYPE_CODE",
            DBEngineConstants.TYPE_INT,
            ""+instituteProposalCostSharingBean.getCostSharingTypeCode()));
        param.addElement(new Parameter("SOURCE_ACCOUNT",
            DBEngineConstants.TYPE_STRING,
            instituteProposalCostSharingBean.getSourceAccount()));  
        param.addElement(new Parameter("COST_SHARING_PERCENTAGE",
            DBEngineConstants.TYPE_DOUBLE,
            ""+instituteProposalCostSharingBean.getCostSharingPercentage()));        
        param.addElement(new Parameter("AMOUNT",
            DBEngineConstants.TYPE_DOUBLE,
            ""+instituteProposalCostSharingBean.getAmount()));                
        param.addElement(new Parameter("UPDATE_TIMESTAMP",
            DBEngineConstants.TYPE_TIMESTAMP,dbTimestamp));
        param.addElement(new Parameter("UPDATE_USER",
            DBEngineConstants.TYPE_STRING, userId));
        param.addElement(new Parameter("AW_PROPOSAL_NUMBER",
             DBEngineConstants.TYPE_STRING,
             instituteProposalCostSharingBean.getProposalNumber()));
        param.addElement(new Parameter("AW_SEQUENCE_NUMBER",
            DBEngineConstants.TYPE_INT,
            ""+instituteProposalCostSharingBean.getSequenceNumber()));     
        param.addElement(new Parameter("AW_FISCAL_YEAR",
            DBEngineConstants.TYPE_STRING,
            instituteProposalCostSharingBean.getAw_FiscalYear()));
        param.addElement(new Parameter("AW_COST_SHARING_TYPE_CODE",
            DBEngineConstants.TYPE_INT,
            ""+instituteProposalCostSharingBean.getAw_CostSharingTypeCode()));        
        param.addElement(new Parameter("AW_SOURCE_ACCOUNT",
            DBEngineConstants.TYPE_STRING,
            instituteProposalCostSharingBean.getAw_SourceAccount()));           
        param.addElement(new Parameter("AW_UPDATE_TIMESTAMP",
            DBEngineConstants.TYPE_TIMESTAMP, instituteProposalCostSharingBean.getUpdateTimestamp()));
        param.addElement(new Parameter("AC_TYPE",
            DBEngineConstants.TYPE_STRING,
            instituteProposalCostSharingBean.getAcType()));

        StringBuffer sql = new StringBuffer(
                                        "call DW_UPDATE_INST_PROP_COST_SHARE(");
        sql.append(" <<PROPOSAL_NUMBER>> , ");
        sql.append(" <<SEQUENCE_NUMBER>> , ");
        sql.append(" <<FISCAL_YEAR>> , ");
        sql.append(" <<COST_SHARING_TYPE_CODE>> , ");
        sql.append(" <<SOURCE_ACCOUNT>> , ");
        sql.append(" <<COST_SHARING_PERCENTAGE>> , ");
        sql.append(" <<AMOUNT>> , ");                
        sql.append(" <<UPDATE_TIMESTAMP>> , ");
        sql.append(" <<UPDATE_USER>> , ");        
        sql.append(" <<AW_PROPOSAL_NUMBER>> , ");        
        sql.append(" <<AW_SEQUENCE_NUMBER>> , ");
        sql.append(" <<AW_FISCAL_YEAR>> , ");        
        sql.append(" <<AW_COST_SHARING_TYPE_CODE>> , ");
        sql.append(" <<AW_SOURCE_ACCOUNT>> , ");
        sql.append(" <<AW_UPDATE_TIMESTAMP>> , ");
        sql.append(" <<AC_TYPE>> )");

        ProcReqParameter procReqParameter = new ProcReqParameter();
        procReqParameter.setDSN(DSN);
        procReqParameter.setParameterInfo(param);
        procReqParameter.setSqlCommand(sql.toString());                
        return procReqParameter;
    }    
    
    /** Method used to update/insert Institute Proposal Cost Sharing
     *  <li>To fetch the data, it uses DW_UPD_PROP_IP_REV_ACTIVITY procedure.
     *
     * @return boolean true for successful insert/modify
     * @param instituteProposalIPReviewActivityBean InstituteProposalIPReviewActivityBean
     * @exception DBException if the instance of a dbEngine is null.
     * @exception CoeusException if the DB instance is not available. 
     */
    public ProcReqParameter addUpdInstituteProposalReviewActivity( InstituteProposalIPReviewActivityBean instituteProposalIPReviewActivityBean )  
        throws CoeusException ,DBException{
        Vector param = new Vector();        
        param = new Vector();        
        param.addElement(new Parameter("PROPOSAL_NUMBER",
            DBEngineConstants.TYPE_STRING,
            instituteProposalIPReviewActivityBean.getProposalNumber()));
        param.addElement(new Parameter("SEQUENCE_NUMBER",
            DBEngineConstants.TYPE_INT,
            ""+instituteProposalIPReviewActivityBean.getSequenceNumber()));
        param.addElement(new Parameter("ACTIVITY_NUMBER",
            DBEngineConstants.TYPE_INT,
            ""+instituteProposalIPReviewActivityBean.getActivityNumber()));
        param.addElement(new Parameter("IP_REV_ACTIVITY_TYPE_CODE",
            DBEngineConstants.TYPE_INT,
            ""+instituteProposalIPReviewActivityBean.getIpReviewActivityTypeCode()));
        param.addElement(new Parameter("ACTIVITY_DATE",
            DBEngineConstants.TYPE_DATE,
            instituteProposalIPReviewActivityBean.getActivityDate()));  
        param.addElement(new Parameter("COMMENTS",
            DBEngineConstants.TYPE_STRING,
            instituteProposalIPReviewActivityBean.getComments()));        
        param.addElement(new Parameter("UPDATE_TIMESTAMP",
            DBEngineConstants.TYPE_TIMESTAMP,dbTimestamp));
        param.addElement(new Parameter("UPDATE_USER",
            DBEngineConstants.TYPE_STRING, userId));
        param.addElement(new Parameter("AW_PROPOSAL_NUMBER",
             DBEngineConstants.TYPE_STRING,
             instituteProposalIPReviewActivityBean.getProposalNumber()));
        param.addElement(new Parameter("AW_SEQUENCE_NUMBER",
            DBEngineConstants.TYPE_INT,
            ""+instituteProposalIPReviewActivityBean.getSequenceNumber()));     
        param.addElement(new Parameter("AW_ACTIVITY_NUMBER",
            DBEngineConstants.TYPE_INT,
            ""+instituteProposalIPReviewActivityBean.getActivityNumber()));
        param.addElement(new Parameter("AW_UPDATE_TIMESTAMP",
            DBEngineConstants.TYPE_TIMESTAMP, instituteProposalIPReviewActivityBean.getUpdateTimestamp()));
        param.addElement(new Parameter("AC_TYPE",
            DBEngineConstants.TYPE_STRING,
            instituteProposalIPReviewActivityBean.getAcType()));

        StringBuffer sql = new StringBuffer(
                                        "call DW_UPD_PROP_IP_REV_ACTIVITY(");
        sql.append(" <<PROPOSAL_NUMBER>> , ");
        sql.append(" <<SEQUENCE_NUMBER>> , ");
        sql.append(" <<ACTIVITY_NUMBER>> , ");
        sql.append(" <<IP_REV_ACTIVITY_TYPE_CODE>> , ");
        sql.append(" <<ACTIVITY_DATE>> , ");
        sql.append(" <<COMMENTS>> , ");
        sql.append(" <<UPDATE_TIMESTAMP>> , ");
        sql.append(" <<UPDATE_USER>> , ");
        sql.append(" <<AW_PROPOSAL_NUMBER>> , ");
        sql.append(" <<AW_SEQUENCE_NUMBER>> , ");
        sql.append(" <<AW_ACTIVITY_NUMBER>> , ");        
        sql.append(" <<AW_UPDATE_TIMESTAMP>> , ");
        sql.append(" <<AC_TYPE>> )");

        ProcReqParameter procReqParameter = new ProcReqParameter();
        procReqParameter.setDSN(DSN);
        procReqParameter.setParameterInfo(param);
        procReqParameter.setSqlCommand(sql.toString());                
        return procReqParameter;
    }   

    /** Method used to update/insert Institute Proposal Investigator
     *  <li>To fetch the data, it uses DW_UPD_INST_PROP_INVES procedure.
     *
     * @return boolean true for successful insert/modify
     * @param instituteProposalInvestigatorBean InstituteProposalInvestigatorBean
     * @exception DBException if the instance of a dbEngine is null.
     * @exception CoeusException if the DB instance is not available. 
     */
    public ProcReqParameter addUpdInstituteProposalInvestigator( InstituteProposalInvestigatorBean instituteProposalInvestigatorBean )  
        throws CoeusException ,DBException{
        Vector param = new Vector();        
        param = new Vector();        
        param.addElement(new Parameter("PROPOSAL_NUMBER",
            DBEngineConstants.TYPE_STRING,
            instituteProposalInvestigatorBean.getProposalNumber()));
        param.addElement(new Parameter("SEQUENCE_NUMBER",
            DBEngineConstants.TYPE_INT,
            ""+instituteProposalInvestigatorBean.getSequenceNumber()));
        param.addElement(new Parameter("PERSON_ID",
            DBEngineConstants.TYPE_STRING,
            instituteProposalInvestigatorBean.getPersonId()));
        param.addElement(new Parameter("PERSON_NAME",
            DBEngineConstants.TYPE_STRING,
            instituteProposalInvestigatorBean.getPersonName()));
        param.addElement(new Parameter("PI_INVESTIGATOR_FLAG",
            DBEngineConstants.TYPE_STRING,
            instituteProposalInvestigatorBean.isPrincipalInvestigatorFlag() ? "Y" : "N"));
        param.addElement(new Parameter("FACULTY_FLAG",
            DBEngineConstants.TYPE_STRING,
            instituteProposalInvestigatorBean.isFacultyFlag() ? "Y" : "N"));        
        param.addElement(new Parameter("NON_MIT_PERSON_FLAG",
            DBEngineConstants.TYPE_STRING,
            instituteProposalInvestigatorBean.isNonMITPersonFlag() ? "Y" : "N"));            
        param.addElement(new Parameter("PERCENTAGE_EFFORT",
            DBEngineConstants.TYPE_FLOAT,
            ""+instituteProposalInvestigatorBean.getPercentageEffort()));           
        param.addElement(new Parameter("UPDATE_TIMESTAMP",
            DBEngineConstants.TYPE_TIMESTAMP,dbTimestamp));
        param.addElement(new Parameter("UPDATE_USER",
            DBEngineConstants.TYPE_STRING, userId));
        param.addElement(new Parameter("AW_PROPOSAL_NUMBER",
             DBEngineConstants.TYPE_STRING,
             instituteProposalInvestigatorBean.getProposalNumber()));
        param.addElement(new Parameter("AW_SEQUENCE_NUMBER",
            DBEngineConstants.TYPE_INT,
            ""+instituteProposalInvestigatorBean.getSequenceNumber()));     
        param.addElement(new Parameter("AW_PERSON_ID",
            DBEngineConstants.TYPE_STRING,
            instituteProposalInvestigatorBean.getPersonId()));
        param.addElement(new Parameter("AW_UPDATE_TIMESTAMP",
            DBEngineConstants.TYPE_TIMESTAMP, instituteProposalInvestigatorBean.getUpdateTimestamp()));
        param.addElement(new Parameter("AC_TYPE",
            DBEngineConstants.TYPE_STRING,
            instituteProposalInvestigatorBean.getAcType()));
        // Added for Case# 2229 - Multi PI Enhancement
         param.addElement(new Parameter("MULTI_PI_FLAG",
            DBEngineConstants.TYPE_STRING,
            instituteProposalInvestigatorBean.isMultiPIFlag() ? "Y" : "N"));
         // Added for Case# 2270- Tracking of Effort- Start
         param.addElement(new Parameter("ACADEMIC_YEAR_EFFORT",
            DBEngineConstants.TYPE_FLOAT,
            ""+instituteProposalInvestigatorBean.getAcademicYearEffort()));
         param.addElement(new Parameter("SUMMER_YEAR_EFFORT",
            DBEngineConstants.TYPE_FLOAT,
            ""+instituteProposalInvestigatorBean.getSummerYearEffort()));
         param.addElement(new Parameter("CALENDAR_YEAR_EFFORT",
            DBEngineConstants.TYPE_FLOAT,
            ""+instituteProposalInvestigatorBean.getCalendarYearEffort()));
         // Added for Case# 2270- Tracking of Effort- End
         
         // Commented for Case# 2270
//        StringBuffer sql = new StringBuffer(
//                                        "call DW_UPD_INST_PROP_INVES("); 
         // Added for  Case# 2270
        StringBuffer sql = new StringBuffer(
                                        "call UPDATE_INST_PROP_INVESTIGATORS(");
        sql.append(" <<PROPOSAL_NUMBER>> , ");
        sql.append(" <<SEQUENCE_NUMBER>> , ");
        sql.append(" <<PERSON_ID>> , ");
        sql.append(" <<PERSON_NAME>> , ");
        sql.append(" <<PI_INVESTIGATOR_FLAG>> , ");
        sql.append(" <<FACULTY_FLAG>> , ");
        sql.append(" <<NON_MIT_PERSON_FLAG>> , ");
        sql.append(" <<PERCENTAGE_EFFORT>> , ");
        sql.append(" <<UPDATE_TIMESTAMP>> , ");
        sql.append(" <<UPDATE_USER>> , ");
        // Added for Case# 2229 - Multi PI Enhancement
        sql.append(" <<MULTI_PI_FLAG>> , ");
        // Added for Case# 2270- Tracking of Effort- Start
        sql.append(" <<ACADEMIC_YEAR_EFFORT>> , ");
        sql.append(" <<SUMMER_YEAR_EFFORT>> , ");
        sql.append(" <<CALENDAR_YEAR_EFFORT>> , ");
        // Added for Case# 2270- Tracking of Effort- End
        sql.append(" <<AW_PROPOSAL_NUMBER>> , ");
        sql.append(" <<AW_SEQUENCE_NUMBER>> , ");
        sql.append(" <<AW_PERSON_ID>> , ");        
        sql.append(" <<AW_UPDATE_TIMESTAMP>> , ");
        sql.append(" <<AC_TYPE>> )");

        ProcReqParameter procReqParameter = new ProcReqParameter();
        procReqParameter.setDSN(DSN);
        procReqParameter.setParameterInfo(param);
        procReqParameter.setSqlCommand(sql.toString());        
        return procReqParameter;
    }
    
    /** Method used to update/insert/delete all the details of Investigator Units
     * <li>To fetch the data, it uses DW_UPD_INST_PROP_UNITS procedure.
     *
     * @return boolean this holds true for successfull insert/modify or
     * false if fails.
     * @param instituteProposalUnitBean InstituteProposalUnitBean
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available. */
    public ProcReqParameter addUpdInstituteProposalInvestigatorUnit( InstituteProposalUnitBean
                instituteProposalUnitBean)  throws CoeusException,DBException{
        Vector param = new Vector();
        param.addElement(new Parameter("PROPOSAL_NUMBER",
                    DBEngineConstants.TYPE_STRING,
                            instituteProposalUnitBean.getProposalNumber()));
        param.addElement(new Parameter("SEQUENCE_NUMBER",
                    DBEngineConstants.TYPE_INT,
                            ""+instituteProposalUnitBean.getSequenceNumber())); 
        param.addElement(new Parameter("PERSON_ID",
                    DBEngineConstants.TYPE_STRING,
                            instituteProposalUnitBean.getPersonId()));        
        param.addElement(new Parameter("UNIT_NUMBER",
                    DBEngineConstants.TYPE_STRING,
                            instituteProposalUnitBean.getUnitNumber()));
        param.addElement(new Parameter("LEAD_UNIT_FLAG",
                    DBEngineConstants.TYPE_STRING,
                            instituteProposalUnitBean.isLeadUnitFlag() ? "Y" : "N" ));
        param.addElement(new Parameter("UPDATE_TIMESTAMP",
                    DBEngineConstants.TYPE_TIMESTAMP,dbTimestamp));
        param.addElement(new Parameter("UPDATE_USER",
                    DBEngineConstants.TYPE_STRING, userId));
        param.addElement(new Parameter("AW_PROPOSAL_NUMBER",
                    DBEngineConstants.TYPE_STRING,
                            instituteProposalUnitBean.getProposalNumber()));
        param.addElement(new Parameter("AW_SEQUENCE_NUMBER",
                    DBEngineConstants.TYPE_INT,
                            ""+instituteProposalUnitBean.getSequenceNumber()));        
        param.addElement(new Parameter("AW_UNIT_NUMBER",
                    DBEngineConstants.TYPE_STRING,
                            instituteProposalUnitBean.getAw_UnitNumber()));
        param.addElement(new Parameter("AW_PERSON_ID",
                    DBEngineConstants.TYPE_STRING,
                            instituteProposalUnitBean.getAw_PersonId()));
        param.addElement(new Parameter("AW_UPDATE_TIMESTAMP",
                    DBEngineConstants.TYPE_TIMESTAMP,
                                instituteProposalUnitBean.getUpdateTimestamp()));
        param.addElement(new Parameter("AC_TYPE",
                    DBEngineConstants.TYPE_STRING,
                            instituteProposalUnitBean.getAcType()));

        StringBuffer sql = new StringBuffer(
                                        "call DW_UPD_INST_PROP_UNITS(");
        sql.append(" <<PROPOSAL_NUMBER>> , ");
        sql.append(" <<SEQUENCE_NUMBER>> , ");        
        sql.append(" <<PERSON_ID>> , ");
        sql.append(" <<UNIT_NUMBER>> , ");
        sql.append(" <<LEAD_UNIT_FLAG>> , ");
        sql.append(" <<UPDATE_TIMESTAMP>> , ");
        sql.append(" <<UPDATE_USER>> , ");        
        sql.append(" <<AW_PROPOSAL_NUMBER>> , ");
        sql.append(" <<AW_SEQUENCE_NUMBER>> , ");        
        sql.append(" <<AW_UNIT_NUMBER>> , ");
        sql.append(" <<AW_PERSON_ID>> , ");                
        sql.append(" <<AW_UPDATE_TIMESTAMP>> , ");
        sql.append(" <<AC_TYPE>> )");

        ProcReqParameter procReqParameter  = new ProcReqParameter();
        procReqParameter.setDSN(DSN);
        procReqParameter.setParameterInfo(param);
        procReqParameter.setSqlCommand(sql.toString());
        return procReqParameter;
    }






    // To add the keyperson unit query
     public ProcReqParameter addUpdInstituteProposalKeyPersonsUnit(KeyPersonUnitBean
                keyPersonUnitBean)  throws CoeusException,DBException{
        Vector param = new Vector();
        param.addElement(new Parameter("PROPOSAL_NUMBER",
                    DBEngineConstants.TYPE_STRING,
                            keyPersonUnitBean.getProposalNumber()));
        param.addElement(new Parameter("SEQUENCE_NUMBER",
                    DBEngineConstants.TYPE_INT,
                            ""+keyPersonUnitBean.getSequenceNumber()));
        param.addElement(new Parameter("PERSON_ID",
                    DBEngineConstants.TYPE_STRING,
                            keyPersonUnitBean.getPersonId()));
        param.addElement(new Parameter("UNIT_NUMBER",
                    DBEngineConstants.TYPE_STRING,
                            keyPersonUnitBean.getUnitNumber()));
//        param.addElement(new Parameter("LEAD_UNIT_FLAG",
//                    DBEngineConstants.TYPE_STRING,
//                            instituteProposalUnitBean.isLeadUnitFlag() ? "Y" : "N" ));
        param.addElement(new Parameter("UPDATE_TIMESTAMP",
                    DBEngineConstants.TYPE_TIMESTAMP,dbTimestamp));
        param.addElement(new Parameter("UPDATE_USER",
                    DBEngineConstants.TYPE_STRING, userId));
        param.addElement(new Parameter("AW_PROPOSAL_NUMBER",
                    DBEngineConstants.TYPE_STRING,
                            keyPersonUnitBean.getProposalNumber()));
        param.addElement(new Parameter("AW_SEQUENCE_NUMBER",
                    DBEngineConstants.TYPE_INT,
                            ""+keyPersonUnitBean.getSequenceNumber()));
        param.addElement(new Parameter("AW_UNIT_NUMBER",
                    DBEngineConstants.TYPE_STRING,
                            keyPersonUnitBean.getAw_UnitNumber()));
        param.addElement(new Parameter("AW_PERSON_ID",
                    DBEngineConstants.TYPE_STRING,
                            keyPersonUnitBean.getAw_PersonId()));
        param.addElement(new Parameter("AW_UPDATE_TIMESTAMP",
                    DBEngineConstants.TYPE_TIMESTAMP,
                                keyPersonUnitBean.getUpdateTimestamp()));
        param.addElement(new Parameter("AC_TYPE",
                    DBEngineConstants.TYPE_STRING,
                            keyPersonUnitBean.getAcType()));

        StringBuffer sql = new StringBuffer(
                                        "call UPD_INST_PROP_KP_UNITS(");
        sql.append(" <<PROPOSAL_NUMBER>> , ");
        sql.append(" <<SEQUENCE_NUMBER>> , ");
        sql.append(" <<PERSON_ID>> , ");
        sql.append(" <<UNIT_NUMBER>> , ");
       // sql.append(" <<LEAD_UNIT_FLAG>> , ");
        sql.append(" <<UPDATE_TIMESTAMP>> , ");
        sql.append(" <<UPDATE_USER>> , ");
        sql.append(" <<AW_PROPOSAL_NUMBER>> , ");
        sql.append(" <<AW_SEQUENCE_NUMBER>> , ");
        sql.append(" <<AW_UNIT_NUMBER>> , ");
        sql.append(" <<AW_PERSON_ID>> , ");
        sql.append(" <<AW_UPDATE_TIMESTAMP>> , ");
        sql.append(" <<AC_TYPE>> )");

        ProcReqParameter procReqParameter  = new ProcReqParameter();
        procReqParameter.setDSN(DSN);
        procReqParameter.setParameterInfo(param);
        procReqParameter.setSqlCommand(sql.toString());
        return procReqParameter;
    }
     //keyperson unit add ends.....






    
    /**  This method used to check whether any child records are changed
     *
     * @return boolean true if something is changed
     * @param childRecords CoeusVector of Child records
     */    
    private boolean isChildRecordChanged(CoeusVector childRecords){
        CoeusBean coeusBean = null;
        boolean isChanged = false;
        for(int row=0 ; row < childRecords.size(); row++){
            coeusBean = (CoeusBean)childRecords.elementAt(row);
            if(coeusBean.getAcType()!=null){
                isChanged = true;
                break;
            }
        }
        return isChanged;
    }
    
    /**  This method used to Merge Proposal Logs
     *  <li>To fetch the data, it uses the function FN_CONVERT_TEMP_LOG.
     * @return int indicating whether updation was success
     *
     * @param tempLogNumber String
     * @param newProposalNumber String
     *
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available. 
     */    
    public int mergeLog(String tempLogNumber,String newProposalNumber)
    throws CoeusException, DBException {
        
        int success = 0;
        Vector procedures = new Vector(3,2);
        procedures.add(convertTempLog(tempLogNumber, newProposalNumber));
        
        if(dbEngine!=null){
            try{// Bug Fix #1920
                dbEngine.executeStoreProcs(procedures);
            }catch (DBException dbEx){
                //Commented and Modified for Case# 2860 - COI SQL error - Start
//                throw new CoeusException(dbEx.getMessage());
                UtilFactory.log(dbEx.getMessage(),dbEx,"InstituteProposalUpdateTxnBean", "mergeLog");
                if(dbEx.getMessage().indexOf("ORA-20000") != -1){
                    throw new CoeusException(((DBException)dbEx).getUserMessage());
                } else {
                    throw new CoeusException(dbEx.getMessage());
                }
                //Commented and Modified for Case# 2860 - COI SQL error - End
            }
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        success = 1;
        return success;
        
    }
    
    /**  This method used to Merge Proposal Logs
     *  <li>To fetch the data, it uses the function FN_CONVERT_TEMP_LOG.
     * @return ProcReqParameter ProcReqParameter
     *
     * @param tempLogNumber String
     * @param newProposalNumber String
     *
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available. 
     */
    public ProcReqParameter convertTempLog(String tempLogNumber,String newProposalNumber)
    throws CoeusException, DBException {
        Vector param= new Vector();
        param.add(new Parameter("TEMP_LOG_NUMBER",
            DBEngineConstants.TYPE_STRING,tempLogNumber));
        param.add(new Parameter("NEW_PROPOSAL_NUMBER",
            DBEngineConstants.TYPE_STRING,newProposalNumber));
        
        StringBuffer sql = new StringBuffer(
            "{  <<OUT INTEGER SUCCESS>> = call FN_CONVERT_TEMP_LOG(");
        sql.append(" <<TEMP_LOG_NUMBER>> ,  ");
        sql.append(" <<NEW_PROPOSAL_NUMBER>> ) } ");
        
        ProcReqParameter procReqParameter  = new ProcReqParameter();
        procReqParameter.setDSN(DSN);
        procReqParameter.setParameterInfo(param);
        procReqParameter.setSqlCommand(sql.toString()); 
        
        return procReqParameter;
    }
    
    /** This method used to Update Proposal Log Status
     *  <li>To update the data, it uses the function FN_UPDATE_STATUS_PROPOSAL_LOG.
     * @return ProcReqParameter ProcReqParameter to execute the function
     * @param proposalNumber String
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available. 
     */
    public ProcReqParameter updateProposalLogStatus(String proposalNumber)
    throws CoeusException, DBException {
//        int success = 0;
        Vector param= new Vector();
//        Vector result = new Vector();
        param.add(new Parameter("PROPOSAL_NUMBER",
            DBEngineConstants.TYPE_STRING, proposalNumber));
        
        //Bug Fix: Case #2203 Start 1
        param.add(new Parameter("UPDATE_USER",DBEngineConstants.TYPE_STRING,userId));
        //Bug Fix: Case #2203 End 1
        
        StringBuffer sql = new StringBuffer(
            "{  <<OUT INTEGER SUCCESS>> = call FN_UPDATE_STATUS_PROPOSAL_LOG(");
        sql.append(" <<PROPOSAL_NUMBER>> ,");
        
        //Bug Fix: Case #2203 Start 2
        sql.append(" <<UPDATE_USER>> ) } ");
        //Bug Fix: Case #2203 End 2
        
        ProcReqParameter procReqParameter  = new ProcReqParameter();
        procReqParameter.setDSN(DSN);
        procReqParameter.setParameterInfo(param);
        procReqParameter.setSqlCommand(sql.toString()); 
        
        return procReqParameter;
    }
    
    /** Method used to update/insert Proposal Log
     * <li>To fetch the data, it uses DW_UPD_PROPOSAL_LOG procedure.
     *
     * @return ProcReqParameter to execute Procedure
     * @param instituteProposalLogBean InstituteProposalLogBean
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available. */
    public boolean updateProposalLog( InstituteProposalLogBean
                instituteProposalLogBean)  throws CoeusException,DBException{
                    
        boolean success = false;
        Vector procedures = new Vector(5,3);        
        
        dbTimestamp = (new CoeusFunctions()).getDBTimestamp();
        
        procedures.add(addUpdInstituteProposalLog(instituteProposalLogBean));
        
        if(dbEngine!=null){
            dbEngine.executeStoreProcs(procedures);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        success = true;        
        return success;
    }
    
    /** Method used to update/insert Proposal Log
     * <li>To fetch the data, it uses DW_UPD_PROPOSAL_LOG procedure.
     *
     * @return ProcReqParameter to execute Procedure
     * @param instituteProposalLogBean InstituteProposalLogBean
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available. */
    public ProcReqParameter addUpdInstituteProposalLog( InstituteProposalLogBean
                instituteProposalLogBean)  throws CoeusException,DBException{
        
        Vector param = new Vector();
        
        //If insert mode get next available Temp/Proposal Log Number 
        if(instituteProposalLogBean.getAcType().equalsIgnoreCase("I")){
            InstituteProposalTxnBean instituteProposalTxnBean = new InstituteProposalTxnBean();
            if(instituteProposalLogBean.getLogType()=='T'){
                instituteProposalLogBean.setProposalNumber(instituteProposalTxnBean.getNextTempLogNumber());
            }else{
                // Bug Fix - 2035 - Dont generate proposal number if it already exists - start
		if(instituteProposalLogBean.getProposalNumber()== null ){
                    // Bug Fix - 2035 - End
                    instituteProposalLogBean.setProposalNumber(instituteProposalTxnBean.getNextInstituteProposalNumber());
                }
            }
        }
        param.addElement(new Parameter("PROPOSAL_NUMBER",
            DBEngineConstants.TYPE_STRING,
            instituteProposalLogBean.getProposalNumber()));
        param.addElement(new Parameter("PROPOSAL_TYPE_CODE",
            DBEngineConstants.TYPE_INT,
            ""+instituteProposalLogBean.getProposalTypeCode())); 
        param.addElement(new Parameter("TITLE",
            DBEngineConstants.TYPE_STRING,
            instituteProposalLogBean.getTitle()));
        param.addElement(new Parameter("PI_ID",
            DBEngineConstants.TYPE_STRING,
            instituteProposalLogBean.getPrincipleInvestigatorId()));
        param.addElement(new Parameter("PI_NAME",
            DBEngineConstants.TYPE_STRING,
            instituteProposalLogBean.getPrincipleInvestigatorName()));
        param.addElement(new Parameter("NON_MIT_PERSON_FLAG",
            DBEngineConstants.TYPE_STRING,
            instituteProposalLogBean.isNonMITPersonFlag() ? "Y" : "N"));
        param.addElement(new Parameter("LEAD_UNIT",
            DBEngineConstants.TYPE_STRING,
            instituteProposalLogBean.getLeadUnit()));
        param.addElement(new Parameter("SPONSOR_CODE",
            DBEngineConstants.TYPE_STRING,
            instituteProposalLogBean.getSponsorCode()));        
        param.addElement(new Parameter("SPONSOR_NAME",
            DBEngineConstants.TYPE_STRING,
            instituteProposalLogBean.getSponsorName()));        
        param.addElement(new Parameter("LOG_STATUS",
            DBEngineConstants.TYPE_STRING,
            ""+instituteProposalLogBean.getLogStatus()));
        param.addElement(new Parameter("COMMENTS",
            DBEngineConstants.TYPE_STRING,
            instituteProposalLogBean.getComments()));
        param.addElement(new Parameter("AW_UPDATE_USER",
            DBEngineConstants.TYPE_STRING, instituteProposalLogBean.getUpdateUser()));        
        param.addElement(new Parameter("AW_UPDATE_TIMESTAMP",
            DBEngineConstants.TYPE_TIMESTAMP,instituteProposalLogBean.getUpdateTimestamp()));
        param.addElement(new Parameter("UPDATE_USER",
            DBEngineConstants.TYPE_STRING, userId));        
        param.addElement(new Parameter("UPDATE_TIMESTAMP",
            DBEngineConstants.TYPE_TIMESTAMP,dbTimestamp));
        //case 3263 start
        if(instituteProposalLogBean.getAcType().equalsIgnoreCase("I")){
            param.addElement(new Parameter("CREATE_USER",
            DBEngineConstants.TYPE_STRING, userId));        
            param.addElement(new Parameter("CREATE_TIMESTAMP",
            DBEngineConstants.TYPE_TIMESTAMP,dbTimestamp));
        }else{
            param.addElement(new Parameter("CREATE_USER",
            DBEngineConstants.TYPE_STRING, instituteProposalLogBean.getCreateUser()));        
            param.addElement(new Parameter("CREATE_TIMESTAMP",
            DBEngineConstants.TYPE_TIMESTAMP,instituteProposalLogBean.getCreateTimestamp()));
        }        
        param.addElement(new Parameter("DEADLINE_DATE",
            DBEngineConstants.TYPE_DATE,instituteProposalLogBean.getDeadlineDate()));
        //case 3263 end
        param.addElement(new Parameter("AC_TYPE",
            DBEngineConstants.TYPE_STRING,
            instituteProposalLogBean.getAcType()));
            
        StringBuffer sql = new StringBuffer(
                                        "call DW_UPD_PROPOSAL_LOG(");
        sql.append(" <<PROPOSAL_NUMBER>> , ");
        sql.append(" <<PROPOSAL_TYPE_CODE>> , ");        
        sql.append(" <<TITLE>> , ");
        sql.append(" <<PI_ID>> , ");
        sql.append(" <<PI_NAME>> , ");
        sql.append(" <<NON_MIT_PERSON_FLAG>> , ");
        sql.append(" <<LEAD_UNIT>> , ");
        sql.append(" <<SPONSOR_CODE>> , ");
        sql.append(" <<SPONSOR_NAME>> , ");
        sql.append(" <<LOG_STATUS>> , ");
        sql.append(" <<COMMENTS>> , ");        
        sql.append(" <<AW_UPDATE_USER>> , ");
        sql.append(" <<AW_UPDATE_TIMESTAMP>> , ");        
        sql.append(" <<UPDATE_USER>> , ");
        sql.append(" <<UPDATE_TIMESTAMP>> , "); 
        //case 3263 start
        sql.append(" <<CREATE_USER>> , ");        
        sql.append(" <<CREATE_TIMESTAMP>> , ");
        sql.append(" <<DEADLINE_DATE>> , "); 
        //case 3263 end     
        sql.append(" <<AC_TYPE>> )");
        
        ProcReqParameter procReqParameter  = new ProcReqParameter();
        procReqParameter.setDSN(DSN);
        procReqParameter.setParameterInfo(param);
        procReqParameter.setSqlCommand(sql.toString());
        
        return procReqParameter;
    }    
    
    /** Method used to update/insert Proposal Log
     * <li>To fetch the data, it uses DW_UPD_INST_PROP_SP_REV procedure.
     *
     * @return ProcReqParameter to execute Procedure
     * @param instituteProposalSpecialReviewBean InstituteProposalSpecialReviewBean
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available. 
     */
    public ProcReqParameter addUpdInstituteProposalSpecialReview( InstituteProposalSpecialReviewBean    
                instituteProposalSpecialReviewBean)  throws CoeusException,DBException{
        
        Vector param = new Vector();
        param.addElement(new Parameter("PROPOSAL_NUMBER",
            DBEngineConstants.TYPE_STRING,
            instituteProposalSpecialReviewBean.getProposalNumber()));
        param.addElement(new Parameter("SEQUENCE_NUMBER",
            DBEngineConstants.TYPE_INT,
            ""+instituteProposalSpecialReviewBean.getSequenceNumber())); 
        param.addElement(new Parameter("SPECIAL_REVIEW_NUMBER",
            DBEngineConstants.TYPE_INT,
            ""+instituteProposalSpecialReviewBean.getSpecialReviewNumber()));
        param.addElement(new Parameter("SPECIAL_REVIEW_CODE",
            DBEngineConstants.TYPE_INT,
            ""+instituteProposalSpecialReviewBean.getSpecialReviewCode()));
        param.addElement(new Parameter("APPROVAL_TYPE_CODE",
            DBEngineConstants.TYPE_INT,
            ""+instituteProposalSpecialReviewBean.getApprovalCode()));
        param.addElement(new Parameter("PROTOCOL_NUMBER",
            DBEngineConstants.TYPE_STRING,
            instituteProposalSpecialReviewBean.getProtocolSPRevNumber()));
        param.addElement(new Parameter("APPLICATION_DATE",
            DBEngineConstants.TYPE_DATE,
            instituteProposalSpecialReviewBean.getApplicationDate()));
        param.addElement(new Parameter("APPROVAL_DATE",
            DBEngineConstants.TYPE_DATE,
            instituteProposalSpecialReviewBean.getApprovalDate()));
        param.addElement(new Parameter("COMMENTS",
            DBEngineConstants.TYPE_STRING,
            instituteProposalSpecialReviewBean.getComments()));
        param.addElement(new Parameter("UPDATE_USER",
            DBEngineConstants.TYPE_STRING, userId));        
        param.addElement(new Parameter("UPDATE_TIMESTAMP",
            DBEngineConstants.TYPE_TIMESTAMP,dbTimestamp));
        param.addElement(new Parameter("AW_PROPOSAL_NUMBER",
            DBEngineConstants.TYPE_STRING,
            instituteProposalSpecialReviewBean.getProposalNumber()));
        param.addElement(new Parameter("AW_SEQUENCE_NUMBER",
            DBEngineConstants.TYPE_INT,
            ""+instituteProposalSpecialReviewBean.getSequenceNumber())); 
        param.addElement(new Parameter("AW_SPECIAL_REVIEW_NUMBER",
            DBEngineConstants.TYPE_INT,
            ""+instituteProposalSpecialReviewBean.getSpecialReviewNumber()));         
        param.addElement(new Parameter("AW_UPDATE_TIMESTAMP",
            DBEngineConstants.TYPE_TIMESTAMP,instituteProposalSpecialReviewBean.getUpdateTimestamp()));
        param.addElement(new Parameter("AC_TYPE",
            DBEngineConstants.TYPE_STRING,
            instituteProposalSpecialReviewBean.getAcType()));
            
        StringBuffer sql = new StringBuffer(
                                        "call DW_UPD_INST_PROP_SP_REV(");
        sql.append(" <<PROPOSAL_NUMBER>> , ");
        sql.append(" <<SEQUENCE_NUMBER>> , ");        
        sql.append(" <<SPECIAL_REVIEW_NUMBER>> , ");
        sql.append(" <<SPECIAL_REVIEW_CODE>> , ");
        sql.append(" <<APPROVAL_TYPE_CODE>> , ");
        sql.append(" <<PROTOCOL_NUMBER>> , ");
        sql.append(" <<APPLICATION_DATE>> , ");
        sql.append(" <<APPROVAL_DATE>> , ");
        sql.append(" <<COMMENTS>> , ");
        sql.append(" <<UPDATE_USER>> , ");
        sql.append(" <<UPDATE_TIMESTAMP>> , ");        
        sql.append(" <<AW_PROPOSAL_NUMBER>> , ");
        sql.append(" <<AW_SEQUENCE_NUMBER>> , ");        
        sql.append(" <<AW_SPECIAL_REVIEW_NUMBER>> , ");
        sql.append(" <<AW_UPDATE_TIMESTAMP>> , ");
        sql.append(" <<AC_TYPE>> )");
        
        ProcReqParameter procReqParameter  = new ProcReqParameter();
        procReqParameter.setDSN(DSN);
        procReqParameter.setParameterInfo(param);
        procReqParameter.setSqlCommand(sql.toString());
        
        return procReqParameter;
    }
    
    /** Method used to Save and Merge Proposal Log with Temp Log
     *
     * @return boolean true if update is success
     * @param instituteProposalLogBean InstituteProposalLogBean
     * @param tempLogNumber String
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available. */
    public boolean saveAndMergeProposalLog( InstituteProposalLogBean
                instituteProposalLogBean, String tempLogNumber) throws CoeusException,DBException{
                    
        boolean success = false;
        Vector procedures = new Vector(5,3);        
        
        dbTimestamp = (new CoeusFunctions()).getDBTimestamp();
        InstituteProposalTxnBean instituteProposalTxnBean = new InstituteProposalTxnBean();
        
        //Get Next Institute Proposal Number
        instituteProposalLogBean.setProposalNumber(instituteProposalTxnBean.getNextInstituteProposalNumber());
        //Merge and Save Proposal Log
        procedures.add(convertTempLog(tempLogNumber, instituteProposalLogBean.getProposalNumber()));
        procedures.add(addUpdInstituteProposalLog(instituteProposalLogBean));
        
        if(dbEngine!=null){
            try{// Bug Fix #1920
                dbEngine.executeStoreProcs(procedures);
            }catch (DBException dbEx){
                //Commented and Modified for Case# 2860 - COI SQL error - Start
//                throw new CoeusException(dbEx.getMessage());
                UtilFactory.log(dbEx.getMessage(),dbEx,"InstituteProposalUpdateTxnBean", "mergeLog");
                if(dbEx.getMessage().indexOf("ORA-20000") != -1){
                    throw new CoeusException(((DBException)dbEx).getUserMessage());
                } else {
                    throw new CoeusException(dbEx.getMessage());
                }
                //Commented and Modified for Case# 2860 - COI SQL error - End
            }
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        success = true;        
        return success;
    }
    
    /** Method used to Update IP Review Dialog data
     *
     * @return boolean true if update is success
     * @param ipReviewData Hashtable
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available. */
    public boolean updateIPReview(Hashtable ipReviewData) 
            throws CoeusException,DBException{
        boolean success = false;
        Vector procedures = new Vector(5,3);        
        
        dbTimestamp = (new CoeusFunctions()).getDBTimestamp();
        
        IndicatorLogic indicatorLogic = new IndicatorLogic();
            
        CoeusVector cvIpReviewData = (CoeusVector)ipReviewData.get(InstituteProposalBean.class);
        InstituteProposalBean instituteProposalBean = (InstituteProposalBean)cvIpReviewData.elementAt(0);        
        
        //Instantiate sequence logic
        SequenceLogic sequenceLogic = new SequenceLogic(instituteProposalBean, false);
        
        //IP Review Activity Indicator
        instituteProposalBean.setIpReviewActivityIndicator(indicatorLogic.processLogic((CoeusVector)ipReviewData.get(InstituteProposalIPReviewActivityBean.class)));
        //Always Update InstituteProposal if some thing is modified
        //B'cos Activity indicator has to be updated
        instituteProposalBean.setAcType("U");
        
        //Update Institute Proposal data
        if(instituteProposalBean!=null && instituteProposalBean.getAcType()!=null){
            procedures.add(updateInstituteProposal(instituteProposalBean));
        }
        
        //Update IP Review Activity
        cvIpReviewData = (CoeusVector)ipReviewData.get(InstituteProposalIPReviewActivityBean.class);
        if(cvIpReviewData!=null && cvIpReviewData.size() >0){
            //Process Sequence Logic
            if(isChildRecordChanged(cvIpReviewData)){
                cvIpReviewData = sequenceLogic.processDetails(cvIpReviewData, true);                
            }
            //Get max Activity Number to generate Activity Number in INSERT mode
            InstituteProposalTxnBean instituteProposalTxnBean = new InstituteProposalTxnBean();
            int maxActivityNumber = instituteProposalTxnBean.getMaxIPReviewActivityNumber(instituteProposalBean.getProposalNumber(), instituteProposalBean.getSequenceNumber());            
            InstituteProposalIPReviewActivityBean instituteProposalIPReviewActivityBean;
            for(int row = 0; row < cvIpReviewData.size(); row++){
                instituteProposalIPReviewActivityBean = (InstituteProposalIPReviewActivityBean)cvIpReviewData.elementAt(row);
                if(instituteProposalIPReviewActivityBean!=null && instituteProposalIPReviewActivityBean.getAcType()!=null){
                    if(instituteProposalIPReviewActivityBean.getAcType().equalsIgnoreCase("I")){
                        maxActivityNumber = maxActivityNumber + 1;
                        instituteProposalIPReviewActivityBean.setActivityNumber(maxActivityNumber);
                    }
                    procedures.add(addUpdInstituteProposalReviewActivity(instituteProposalIPReviewActivityBean));
                }
            }
        }
        
        //Update Comments
        // Commented by Shivakumar for CLOB implementation - BEGIN 
//        cvIpReviewData = (CoeusVector)ipReviewData.get(InstituteProposalCommentsBean.class);
//        if(cvIpReviewData!=null){
//            InstituteProposalCommentsBean instituteProposalCommentsBean;
//            for(int row = 0; row < cvIpReviewData.size(); row++){
//                instituteProposalCommentsBean = (InstituteProposalCommentsBean)cvIpReviewData.elementAt(row);
//                if(instituteProposalCommentsBean!=null && instituteProposalCommentsBean.getAcType()!=null){
//                    procedures.add(addUpdInstituteProposalComments(instituteProposalCommentsBean));
//                }
//            }
//        }
        // Commented by Shivakumar for CLOB implementation - END 
        // Commented by Shivakumar for CLOB implementation - BEGIN 
//        if(dbEngine!=null){
//            dbEngine.executeStoreProcs(procedures);
//        }else{
//            throw new CoeusException("db_exceptionCode.1000");
//        }
        // Commented by Shivakumar for CLOB implementation - END 
        // Code added by Shivakumar for CLOB implementation - BEGIN 
        
        if(dbEngine!=null){
            java.sql.Connection conn = null;
            try{
                conn = dbEngine.beginTxn();
                dbEngine.executeStoreProcs(procedures,conn);
                cvIpReviewData = (CoeusVector)ipReviewData.get(InstituteProposalCommentsBean.class);
                if(cvIpReviewData!=null && cvIpReviewData.size()>0){
                    Vector vecProcParameters = new Vector();
                    InstituteProposalCommentsBean instituteProposalCommentsBean;
                    for(int row = 0; row < cvIpReviewData.size(); row++){
                        instituteProposalCommentsBean = (InstituteProposalCommentsBean)cvIpReviewData.elementAt(row);
                        if(instituteProposalCommentsBean!=null && instituteProposalCommentsBean.getAcType()!=null){
                            vecProcParameters.add(addUpdInstituteProposalComments(instituteProposalCommentsBean));
                        }
                    }
                    if(vecProcParameters!= null && vecProcParameters.size() >0){
                            dbEngine.batchSQLUpdate(vecProcParameters, conn);
                        }
                    
                }
                dbEngine.commit(conn);
            }catch(Exception sqlEx){
                dbEngine.rollback(conn);
                throw new CoeusException(sqlEx.getMessage());
            }finally{
                 dbEngine.endTxn(conn);
            }    
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        
        
        // Code added by Shivakumar for CLOB implementation - END 
        success = true;
        return success;
    }
    
    /**
     *  Method used to update/insert all the details of a Institute Proposal 
     *  Custom elements details.
     *
     *  @param instituteProposalCustomDataBean details of others details.
     *  @return boolean this holds true for successfull insert/modify or
     *  false if fails.
     *  @exception DBException if any error during database transaction.
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
    public ProcReqParameter addUpdInstituteProposalCustomData(InstituteProposalCustomDataBean instituteProposalCustomDataBean)
            throws CoeusException,DBException{

        Vector param = new Vector();

        param.addElement(new Parameter("PROPOSAL_NUMBER",
            DBEngineConstants.TYPE_STRING,
            instituteProposalCustomDataBean.getProposalNumber()));
        param.addElement(new Parameter("SEQUENCE_NUMBER",
            DBEngineConstants.TYPE_INT,
            ""+instituteProposalCustomDataBean.getSequenceNumber()));
        param.addElement(new Parameter("COLUMN_NAME",
            DBEngineConstants.TYPE_STRING,
            instituteProposalCustomDataBean.getColumnName()));
        param.addElement(new Parameter("COLUMN_VALUE",
            DBEngineConstants.TYPE_STRING,
            instituteProposalCustomDataBean.getColumnValue()));
        param.addElement(new Parameter("UPDATE_TIMESTAMP",
            DBEngineConstants.TYPE_TIMESTAMP,dbTimestamp));        
        param.addElement(new Parameter("UPDATE_USER",
            DBEngineConstants.TYPE_STRING, userId));
        param.addElement(new Parameter("AW_PROPOSAL_NUMBER",
            DBEngineConstants.TYPE_STRING,
            instituteProposalCustomDataBean.getProposalNumber()));
        param.addElement(new Parameter("AW_SEQUENCE_NUMBER",
            DBEngineConstants.TYPE_INT,
            ""+instituteProposalCustomDataBean.getSequenceNumber()));         
        param.addElement(new Parameter("AW_COLUMN_NAME",
            DBEngineConstants.TYPE_STRING,
            instituteProposalCustomDataBean.getColumnName()));
        param.addElement(new Parameter("AW_UPDATE_TIMESTAMP",
            DBEngineConstants.TYPE_TIMESTAMP,
            instituteProposalCustomDataBean.getUpdateTimestamp()));
        param.addElement(new Parameter("AC_TYPE",
            DBEngineConstants.TYPE_STRING,
            instituteProposalCustomDataBean.getAcType()));

        StringBuffer sql = new StringBuffer(
                                        "call DW_UPDATE_PROPOSAL_CUSTOM_DATA(");
        sql.append(" <<PROPOSAL_NUMBER>> , ");
        sql.append(" <<SEQUENCE_NUMBER>> , ");
        sql.append(" <<COLUMN_NAME>> , ");
        sql.append(" <<COLUMN_VALUE>> , ");
        sql.append(" <<UPDATE_TIMESTAMP>> , ");
        sql.append(" <<UPDATE_USER>> , ");
        sql.append(" <<AW_PROPOSAL_NUMBER>> , ");
        sql.append(" <<AW_SEQUENCE_NUMBER>> , ");
        sql.append(" <<AW_COLUMN_NAME>> , ");
        sql.append(" <<AW_UPDATE_TIMESTAMP>> , ");
        sql.append(" <<AC_TYPE>> )");

        ProcReqParameter procReqParameter  = new ProcReqParameter();
        procReqParameter.setDSN(DSN);
        procReqParameter.setParameterInfo(param);
        procReqParameter.setSqlCommand(sql.toString());

        return procReqParameter;
    }
    
    /** This method used to Create new Sequence Number for Proposal
     *  <li>To update the data, it uses the function FN_CREATE_NEW_SEQUENCE_PROP.
     * @return ProcReqParameter ProcReqParameter to execute the function
     * @param proposalNumber String
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available. 
     */
    public ProcReqParameter createNewSequenceForProposal(String proposalNumber, String accountNumber)
    throws CoeusException, DBException {
//        int success = 0;
        Vector param= new Vector();
//        Vector result = new Vector();
        param.add(new Parameter("PROPOSAL_NUMBER",
            DBEngineConstants.TYPE_STRING, proposalNumber));
        param.add(new Parameter("ACCOUNT_NUMBER",
            DBEngineConstants.TYPE_STRING, accountNumber));        
        param.add(new Parameter("USER_ID",
            DBEngineConstants.TYPE_STRING, userId));        
        
        StringBuffer sql = new StringBuffer(
            "{  <<OUT INTEGER SUCCESS>> = call FN_CREATE_NEW_SEQUENCE_PROP(");
        sql.append(" <<PROPOSAL_NUMBER>>, <<ACCOUNT_NUMBER>>, <<USER_ID>>  ) } ");        
        
        ProcReqParameter procReqParameter  = new ProcReqParameter();
        procReqParameter.setDSN(DSN);
        procReqParameter.setParameterInfo(param);
        procReqParameter.setSqlCommand(sql.toString()); 
        
        return procReqParameter;
    }
    
    //Case 2106 Start 
   public void addUpdCreditSplit(CoeusVector cvData) throws CoeusException, DBException{
        dbTimestamp = (new CoeusFunctions()).getDBTimestamp();
        Vector procedures = new Vector(5,3);
        
        CoeusVector cvInvCreditData = (CoeusVector)cvData.get(0);
        CoeusVector cvUnitCreditData = (CoeusVector)cvData.get(1);
        
        if(cvInvCreditData != null && cvInvCreditData.size() > 0){
            for(int i = 0  ; i < cvInvCreditData.size() ; i++){
                InvestigatorCreditSplitBean invCreditSplitBean =
                            (InvestigatorCreditSplitBean)cvInvCreditData.get(i);

                if(invCreditSplitBean.getAcType() != null){
                    procedures.add(addUpdPerCredit(invCreditSplitBean));
                }//End of innner if
            }
        }
        
        if(cvUnitCreditData != null && cvUnitCreditData.size() > 0){
            for(int j = 0  ; j < cvUnitCreditData.size() ; j++){
                InvestigatorCreditSplitBean invCreditSplitBean =
                            (InvestigatorCreditSplitBean)cvUnitCreditData.get(j);

                if(invCreditSplitBean.getAcType() != null){
                    procedures.add(addUpdUnitCredit(invCreditSplitBean));
                }//End of inner if
            }
        }
        
        if(dbEngine!=null){
           try{
               if(procedures.size()>0){
                   dbEngine.executeStoreProcs(procedures);
               }
           }catch (DBException dbEx){
               throw new CoeusException(dbEx.getMessage());
           }
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
    }
    
    /**
     *  Method used to update/insert all the details of a Investigator credit split
     *  <li>To fetch the data, it uses UPD_PROPOSAL_PER_CREDIT_SPLIT procedure.
     *
     *  @param InvestigatorCreditSplitBean this bean contains data for insert.
     *  @return ProcReqParameter
     *  @exception DBException , CoeusException if the instance of a dbEngine is null.
     */
    public ProcReqParameter addUpdPerCredit(InvestigatorCreditSplitBean invCreditSplitBean)  
    throws CoeusException,DBException{
        Vector paramInvCredit = new Vector();
        
        paramInvCredit.addElement(new Parameter("AV_PROPOSAL_NUMBER",
            DBEngineConstants.TYPE_STRING, invCreditSplitBean.getModuleNumber()));
        
        paramInvCredit.addElement(new Parameter("AV_SEQUENCE_NUMBER",
            DBEngineConstants.TYPE_INT, ""+invCreditSplitBean.getSequenceNo()));
        
        paramInvCredit.addElement(new Parameter("AV_PERSON_ID",
            DBEngineConstants.TYPE_STRING, invCreditSplitBean.getPersonId()));
        
        paramInvCredit.addElement(new Parameter("AV_INV_CREDIT_TYPE_CODE",
            DBEngineConstants.TYPE_INT, ""+invCreditSplitBean.getInvCreditTypeCode()));
        
        paramInvCredit.addElement(new Parameter("AV_CREDIT",
            DBEngineConstants.TYPE_DOUBLE_OBJ, invCreditSplitBean.getCredit().equals(new Double(0)) ? null : invCreditSplitBean.getCredit()));
        
        paramInvCredit.addElement(new Parameter("AV_UPDATE_TIMESTAMP",
            DBEngineConstants.TYPE_TIMESTAMP, dbTimestamp));
        
        paramInvCredit.addElement(new Parameter("AV_UPDATE_USER",
            DBEngineConstants.TYPE_STRING, userId));
        
        paramInvCredit.addElement(new Parameter("AW_PROPOSAL_NUMBER",
            DBEngineConstants.TYPE_STRING, invCreditSplitBean.getModuleNumber()));
        
        paramInvCredit.addElement(new Parameter("AW_SEQUENCE_NUMBER",
            DBEngineConstants.TYPE_INT, ""+invCreditSplitBean.getSequenceNo()));
        
        paramInvCredit.addElement(new Parameter("AW_PERSON_ID",
            DBEngineConstants.TYPE_STRING, invCreditSplitBean.getPersonId()));
        
        paramInvCredit.addElement(new Parameter("AW_INV_CREDIT_TYPE_CODE",
            DBEngineConstants.TYPE_INT, ""+invCreditSplitBean.getInvCreditTypeCode()));
        
        paramInvCredit.addElement(new Parameter("AW_UPDATE_TIMESTAMP",
            DBEngineConstants.TYPE_TIMESTAMP, invCreditSplitBean.getUpdateTimestamp()));
        
        paramInvCredit.addElement(new Parameter("AC_TYPE",
            DBEngineConstants.TYPE_STRING, invCreditSplitBean.getAcType()));

        StringBuffer sqlInvCredit = new StringBuffer(
                                    "{ call UPD_PROPOSAL_PER_CREDIT_SPLIT(");
        sqlInvCredit.append(" <<AV_PROPOSAL_NUMBER>> , ");
        sqlInvCredit.append(" <<AV_SEQUENCE_NUMBER>> , ");
        sqlInvCredit.append(" <<AV_PERSON_ID>> , ");
        sqlInvCredit.append(" <<AV_INV_CREDIT_TYPE_CODE>> , ");
        sqlInvCredit.append(" <<AV_CREDIT>> , ");
        sqlInvCredit.append(" <<AV_UPDATE_TIMESTAMP>> , ");
        sqlInvCredit.append(" <<AV_UPDATE_USER>> , ");
        sqlInvCredit.append(" <<AW_PROPOSAL_NUMBER>> , ");
        sqlInvCredit.append(" <<AW_SEQUENCE_NUMBER>> , ");
        sqlInvCredit.append(" <<AW_PERSON_ID>> , ");
        sqlInvCredit.append(" <<AW_INV_CREDIT_TYPE_CODE>> , ");
        sqlInvCredit.append(" <<AW_UPDATE_TIMESTAMP>> , ");
        sqlInvCredit.append(" <<AC_TYPE>> ) }");

        ProcReqParameter procInvCredit  = new ProcReqParameter();
        procInvCredit.setDSN(DSN);
        procInvCredit.setParameterInfo(paramInvCredit);
        procInvCredit.setSqlCommand(sqlInvCredit.toString());

        return procInvCredit;

    }
    
    /**
     *  Method used to update/insert all the details of a Investigator credit split
     *  <li>To fetch the data, it uses UPD_EPS_PROP_UNIT_CREDIT_SPLIT procedure.
     *
     *  @param InvestigatorCreditSplitBean this bean contains data for insert.
     *  @return ProcReqParameter
     *  @exception DBException , CoeusException if the instance of a dbEngine is null.
     */
    public ProcReqParameter addUpdUnitCredit( InvestigatorCreditSplitBean invCreditSplitBean)  
    throws CoeusException,DBException{
        Vector paramUnitCredit = new Vector();
        
        paramUnitCredit.addElement(new Parameter("AV_PROPOSAL_NUMBER",
            DBEngineConstants.TYPE_STRING, invCreditSplitBean.getModuleNumber()));
        
        paramUnitCredit.addElement(new Parameter("AV_SEQUENCE_NUMBER",
            DBEngineConstants.TYPE_INT, ""+invCreditSplitBean.getSequenceNo()));
        
        paramUnitCredit.addElement(new Parameter("AV_PERSON_ID",
            DBEngineConstants.TYPE_STRING, invCreditSplitBean.getPersonId()));
        
        paramUnitCredit.addElement(new Parameter("AV_UNIT_NUMBER",
            DBEngineConstants.TYPE_STRING, invCreditSplitBean.getUnitNumber()));
        
        paramUnitCredit.addElement(new Parameter("AV_INV_CREDIT_TYPE_CODE",
            DBEngineConstants.TYPE_INT, ""+invCreditSplitBean.getInvCreditTypeCode()));
        
        paramUnitCredit.addElement(new Parameter("AV_CREDIT",
            DBEngineConstants.TYPE_DOUBLE_OBJ, invCreditSplitBean.getCredit().equals(new Double(0)) ? null : invCreditSplitBean.getCredit()));
        
        paramUnitCredit.addElement(new Parameter("AV_UPDATE_TIMESTAMP",
            DBEngineConstants.TYPE_TIMESTAMP, dbTimestamp));
        
        paramUnitCredit.addElement(new Parameter("AV_UPDATE_USER",
            DBEngineConstants.TYPE_STRING, userId));
        
        paramUnitCredit.addElement(new Parameter("AW_PROPOSAL_NUMBER",
            DBEngineConstants.TYPE_STRING, invCreditSplitBean.getModuleNumber()));
        
        paramUnitCredit.addElement(new Parameter("AW_SEQUENCE_NUMBER",
            DBEngineConstants.TYPE_INT, ""+invCreditSplitBean.getSequenceNo()));
        
        paramUnitCredit.addElement(new Parameter("AW_PERSON_ID",
            DBEngineConstants.TYPE_STRING, invCreditSplitBean.getPersonId()));
        
        paramUnitCredit.addElement(new Parameter("AW_UNIT_NUMBER",
            DBEngineConstants.TYPE_STRING, invCreditSplitBean.getUnitNumber()));
        
        paramUnitCredit.addElement(new Parameter("AW_INV_CREDIT_TYPE_CODE",
            DBEngineConstants.TYPE_INT, ""+invCreditSplitBean.getInvCreditTypeCode()));
        
        paramUnitCredit.addElement(new Parameter("AW_UPDATE_TIMESTAMP",
            DBEngineConstants.TYPE_TIMESTAMP, invCreditSplitBean.getUpdateTimestamp()));
        
        paramUnitCredit.addElement(new Parameter("AC_TYPE",
            DBEngineConstants.TYPE_STRING, invCreditSplitBean.getAcType()));

        StringBuffer sqlUnitCredit = new StringBuffer(
                                    "{ call UPD_PROPOSAL_UNIT_CREDIT_SPLIT(");
        sqlUnitCredit.append(" <<AV_PROPOSAL_NUMBER>> , ");
        sqlUnitCredit.append(" <<AV_SEQUENCE_NUMBER>> , ");
        sqlUnitCredit.append(" <<AV_PERSON_ID>> , ");
        sqlUnitCredit.append(" <<AV_UNIT_NUMBER>> , ");
        sqlUnitCredit.append(" <<AV_INV_CREDIT_TYPE_CODE>> , ");
        sqlUnitCredit.append(" <<AV_CREDIT>> , ");
        sqlUnitCredit.append(" <<AV_UPDATE_TIMESTAMP>> , ");
        sqlUnitCredit.append(" <<AV_UPDATE_USER >> , ");
        sqlUnitCredit.append(" <<AW_PROPOSAL_NUMBER>> , ");
        sqlUnitCredit.append(" <<AW_SEQUENCE_NUMBER>> , ");
        sqlUnitCredit.append(" <<AW_PERSON_ID>> , ");
        sqlUnitCredit.append(" <<AW_UNIT_NUMBER>> , ");
        sqlUnitCredit.append(" <<AW_INV_CREDIT_TYPE_CODE>> , ");
        sqlUnitCredit.append(" <<AW_UPDATE_TIMESTAMP>> , ");
        sqlUnitCredit.append(" <<AC_TYPE>> ) }");

        ProcReqParameter procUnitCredit  = new ProcReqParameter();
        procUnitCredit.setDSN(DSN);
        procUnitCredit.setParameterInfo(paramUnitCredit);
        procUnitCredit.setSqlCommand(sqlUnitCredit.toString());

        return procUnitCredit;
    }
    //Case 2106 End 
     //Case 2136 Start 
   public void addUpdInstPropAdmin(Vector vecData) throws CoeusException, DBException{
        dbTimestamp = (new CoeusFunctions()).getDBTimestamp();
        Vector procedures = new Vector(5,3);
        InstituteProposalBean instituteProposalBean = 
                                (InstituteProposalBean)vecData.get(0);
        CoeusVector cvInvAdminData = (CoeusVector)vecData.get(1);
        SequenceLogic sequenceLogic = new SequenceLogic(instituteProposalBean, false);
        
        if(cvInvAdminData != null){
            //Process Sequence Logic
            if(isChildRecordChanged(cvInvAdminData)){
                cvInvAdminData = sequenceLogic.processDetails(cvInvAdminData, true);
            }
        }
        if(cvInvAdminData != null && cvInvAdminData.size() > 0){
            for(int i = 0  ; i < cvInvAdminData.size() ; i++){
                InvestigatorUnitAdminTypeBean invBean =
                            (InvestigatorUnitAdminTypeBean)cvInvAdminData.get(i);

                if(invBean.getAcType() != null){
                    procedures.add(addUpdInstAdmin(invBean));
                }//End of innner if
            }
        }
        
        if(dbEngine!=null){
            java.sql.Connection conn = null;
            try{
                if(procedures.size() > 0 ){
                    conn = dbEngine.beginTxn();
                    dbEngine.executeStoreProcs(procedures,conn);
                    dbEngine.commit(conn);
                }
            }catch(Exception sqlEx){
                dbEngine.rollback(conn);
                throw new CoeusException(sqlEx.getMessage());
            }finally{
                dbEngine.endTxn(conn);
            }
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
    }
    
    /**
     *  Method used to update/insert all the details of a Investigator credit split
     *  <li>To fetch the data, it uses UPD_PROP_UNIT_ADMINISTRATORS procedure.
     *
     *  @param InvestigatorUnitAdminTypeBean this bean contains data for insert.
     *  @return ProcReqParameter
     *  @exception DBException , CoeusException if the instance of a dbEngine is null.
     */
    public ProcReqParameter addUpdInstAdmin(InvestigatorUnitAdminTypeBean invBean)  
    throws CoeusException,DBException{
        Vector paramInvCredit = new Vector();
        
        paramInvCredit.addElement(new Parameter("AV_PROPOSAL_NUMBER",
            DBEngineConstants.TYPE_STRING, invBean.getModuleNumber()));
        
        paramInvCredit.addElement(new Parameter("AV_SEQUENCE_NUMBER",
            DBEngineConstants.TYPE_INT, ""+invBean.getSequenceNumber()));
        
        paramInvCredit.addElement(new Parameter("AV_ADMINISTRATOR",
            DBEngineConstants.TYPE_STRING, invBean.getAdministrator()));
        
        paramInvCredit.addElement(new Parameter("AV_UNIT_ADMIN_TYPE_CODE",
            DBEngineConstants.TYPE_INT, ""+invBean.getUnitAdminType()));
        
        
        paramInvCredit.addElement(new Parameter("AV_UPDATE_TIMESTAMP",
            DBEngineConstants.TYPE_TIMESTAMP, dbTimestamp));
        
        paramInvCredit.addElement(new Parameter("AV_UPDATE_USER",
            DBEngineConstants.TYPE_STRING, userId));
        
        paramInvCredit.addElement(new Parameter("AW_PROPOSAL_NUMBER",
            DBEngineConstants.TYPE_STRING, invBean.getModuleNumber()));
        
        paramInvCredit.addElement(new Parameter("AW_SEQUENCE_NUMBER",
            DBEngineConstants.TYPE_INT, ""+invBean.getAwSequenceNo()));
        
        paramInvCredit.addElement(new Parameter("AW_ADMINISTRATOR",
            DBEngineConstants.TYPE_STRING, invBean.getAdministrator()));
        
        paramInvCredit.addElement(new Parameter("AW_UNIT_ADMIN_TYPE_CODE",
            DBEngineConstants.TYPE_INT, ""+invBean.getAwUnitAdminType()));
        
        paramInvCredit.addElement(new Parameter("AW_UPDATE_TIMESTAMP",
            DBEngineConstants.TYPE_TIMESTAMP, invBean.getUpdateTimestamp()));
        
        paramInvCredit.addElement(new Parameter("AW_UPDATE_USER",
            DBEngineConstants.TYPE_STRING, invBean.getUpdateUser()));
        
        paramInvCredit.addElement(new Parameter("AC_TYPE",
            DBEngineConstants.TYPE_STRING, invBean.getAcType()));

        StringBuffer sqlInvCredit = new StringBuffer(
                                    "{ call UPD_PROP_UNIT_ADMINISTRATORS(");
        sqlInvCredit.append(" <<AV_PROPOSAL_NUMBER>> , ");
        sqlInvCredit.append(" <<AV_SEQUENCE_NUMBER>> , ");
        sqlInvCredit.append(" <<AV_UNIT_ADMIN_TYPE_CODE>> , ");
        sqlInvCredit.append(" <<AV_ADMINISTRATOR>> , ");
        sqlInvCredit.append(" <<AV_UPDATE_TIMESTAMP>> , ");
        sqlInvCredit.append(" <<AV_UPDATE_USER>> , ");
        sqlInvCredit.append(" <<AW_PROPOSAL_NUMBER>> , ");
        sqlInvCredit.append(" <<AW_SEQUENCE_NUMBER>> , ");
        sqlInvCredit.append(" <<AW_UNIT_ADMIN_TYPE_CODE>> , ");
        sqlInvCredit.append(" <<AW_ADMINISTRATOR>> , ");
        sqlInvCredit.append(" <<AW_UPDATE_TIMESTAMP>> , ");
        sqlInvCredit.append(" <<AW_UPDATE_USER>> , ");
        sqlInvCredit.append(" <<AC_TYPE>> ) }");

        ProcReqParameter procInvCredit  = new ProcReqParameter();
        procInvCredit.setDSN(DSN);
        procInvCredit.setParameterInfo(paramInvCredit);
        procInvCredit.setSqlCommand(sqlInvCredit.toString());

        return procInvCredit;

    }
    //Case 2136 end 
    public static void main(String args[]){
        try{
            //InstituteProposalTxnBean instituteProposalTxnBean = new InstituteProposalTxnBean();
            InstituteProposalUpdateTxnBean instituteProposalUpdateTxnBean = new InstituteProposalUpdateTxnBean("COEUS");            
            //CoeusVector coeusVector = instituteProposalTxnBean.getInstituteProposalCustomData("04070005",2);
            //InstituteProposalCustomDataBean instituteProposalCustomDataBean = (InstituteProposalCustomDataBean)coeusVector.elementAt(0);
            //instituteProposalCustomDataBean.setAcType("U");
            //boolean success = instituteProposalUpdateTxnBean.addUpdInstituteProposalCustomData(instituteProposalCustomDataBean);
           // String personId = instituteProposalUpdateTxnBean.getPersonId("Rosenson, Lawrence");
     //       System.out.println("PersonId : "+personId);
            
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }
    
    
    //Added for the Coeus Enhancement case:#1799 start step:5
    private void unLockProtocols(Vector protocolData) throws CoeusException,DBException{
        if(protocolData != null && protocolData.size()>0) {
//            ProtocolUpdateTxnBean protocolUpdateTxnBean = new ProtocolUpdateTxnBean();
            for(int index=0;index<protocolData.size();index++) {
                ProtocolFundingSourceBean bean = (ProtocolFundingSourceBean)protocolData.get(index);
                transMon.releaseLock("osp$Protocol_"+bean.getProtocolNumber(),userId);
            }
        }
    }//End Coeus Enhancement case:#1799 step:5
    
    //Added for the Coeus Enhanancement case:#1799 to establish the link with the protocol step:6
    private Vector performProtocolLinkFromInstProp(InstituteProposalSpecialReviewBean instituteProposalSpecialReviewBean)
    throws Exception,DBException{
        Vector procedures =  new Vector(5,3);
        CoeusFunctions coeusFunctions = new CoeusFunctions();
        CoeusMessageResourcesBean coeusMessageResourcesBean
          =new CoeusMessageResourcesBean();
        int approvalCode = instituteProposalSpecialReviewBean.getAcType().equals("D") ? instituteProposalSpecialReviewBean.getAw_ApprovalCode() : 
                                            instituteProposalSpecialReviewBean.getApprovalCode();
        int specialReviewCode = instituteProposalSpecialReviewBean.getAcType().equals("D") ? instituteProposalSpecialReviewBean.getAw_SpecialReviewCode() : 
                                            instituteProposalSpecialReviewBean.getSpecialReviewCode();
        if(approvalCode == Integer.parseInt(coeusFunctions.getParameterValue(CoeusConstants.LINKED_TO_IRB_CODE)) &&
        Integer.parseInt(coeusFunctions.getParameterValue(CoeusConstants.ENABLE_PROTOCOL_TO_PROPOSAL_LINK)) == 1 &&
        instituteProposalSpecialReviewBean.getAcType() != null && specialReviewCode ==
        Integer.parseInt(coeusFunctions.getParameterValue(CoeusConstants.SPL_REV_TYPE_CODE_HUMAN))){
            ProtocolDataTxnBean protocolDataTxnBean = new ProtocolDataTxnBean(userId);
            if(protocolData == null)
                protocolData = new Vector();
            String spRevNum = instituteProposalSpecialReviewBean.getAcType().equals("I") ? instituteProposalSpecialReviewBean.getProtocolSPRevNumber() : instituteProposalSpecialReviewBean.getPrevSpRevProtocolNumber();
            CoeusDataTxnBean coeusDataTxnBean = new CoeusDataTxnBean();
            boolean validProtocolNumber = coeusDataTxnBean.validateProtocolNumber(spRevNum);
            if(validProtocolNumber){
                boolean lockCheck = protocolDataTxnBean.lockCheck(spRevNum, userId);
                //while updating the rows in the module,if we have got the lock then no need to acquire the lock once again
                if(protocolData != null && protocolData.size() > 0){
                    for(int i  = 0;i<protocolData.size();i++){
                        ProtocolFundingSourceBean protocolFundingSourceBean = (ProtocolFundingSourceBean)protocolData.get(i);
                        if(protocolFundingSourceBean.getProtocolNumber().equals(spRevNum)){
                            lockCheck = true;
                        }
                    }
                }
                if(!lockCheck){
                    unLockProtocols(protocolData);
                    String msg = "The Protocol is " + spRevNum+ " is modified by an other user";
                    throw new LockingException(msg);
                }else{
                    String indicator = "P1";
                    LockingBean lockingBean =  protocolDataTxnBean.getLock(spRevNum, userId, getUnitNumber());
                    protocolDataTxnBean.transactionCommit();
                    boolean lockAvailable = lockingBean.isGotLock();
                    if(lockAvailable){
                        ProtocolUpdateTxnBean protocolUpdateTxnBean = new ProtocolUpdateTxnBean(userId);
                        ProtocolFundingSourceBean protocolFundingSourceBean = null;
                        ProtocolInfoBean protocolInfoBean = (ProtocolInfoBean)protocolDataTxnBean.getProtocolMaintenanceDetails(spRevNum);
                        if(instituteProposalSpecialReviewBean.getAcType().equals("I")){//protocolNum1 == null) {
                            protocolFundingSourceBean = new ProtocolFundingSourceBean();
                            protocolFundingSourceBean.setProtocolNumber(spRevNum);
                            protocolFundingSourceBean.setSequenceNumber(protocolInfoBean.getSequenceNumber());
                            protocolFundingSourceBean.setFundingSourceTypeCode(5);
                            protocolFundingSourceBean.setFundingSource(instituteProposalSpecialReviewBean.getProposalNumber());
                            protocolFundingSourceBean.setAcType("I");
                            /** Check for the exisstence protocol number in the funding source
                             * Case Id 2002
                             */
                            int existsCount = protocolUpdateTxnBean.checkProtocolInFundingSource(
                                                spRevNum, protocolFundingSourceBean.getFundingSourceTypeCode(), 
                                                protocolFundingSourceBean.getFundingSource());
                            if(existsCount == 0){
                                procedures.add(protocolUpdateTxnBean.addUpdProtocolFundSource(protocolFundingSourceBean));
                            }
                            //Modified for COEUSDEV-75:Rework email engine so the email body is picked up from one place
                            protocolDataTxnBean.sendEmailNotification(ModuleConstants.PROPOSAL_INSTITUTE_MODULE_CODE,MailActions.SPECIAL_REVIEW_INSERTED,
                                    instituteProposalSpecialReviewBean.getProposalNumber(),instituteProposalSpecialReviewBean.getSequenceNumber(),spRevNum, ModuleConstants.PROTOCOL_MODULE_CODE);
                        }else if(instituteProposalSpecialReviewBean.getAcType().equals("D")){//!protocolNum.equals(protocolNum1)) {
                            Vector vecSpData = protocolDataTxnBean.getProtocolFundingSources(spRevNum, protocolInfoBean.getSequenceNumber());
                            int size = vecSpData.size();
                            for(int i=0;i<size;i++) {
                                protocolFundingSourceBean = (ProtocolFundingSourceBean)vecSpData.get(i);
                                if(instituteProposalSpecialReviewBean.getProposalNumber().equals(protocolFundingSourceBean.getFundingSource())) {
                                    protocolFundingSourceBean.setAcType("D");
                                    protocolFundingSourceBean.setAwFundingSourceTypeCode(5);
                                    procedures.add(protocolUpdateTxnBean.addUpdProtocolFundSource(protocolFundingSourceBean));
                                    size-=1;
                                    //Modified for COEUSDEV-75:Rework email engine so the email body is picked up from one place
                                    protocolDataTxnBean.sendEmailNotification(ModuleConstants.PROPOSAL_INSTITUTE_MODULE_CODE,MailActions.SPECIAL_REVIEW_DELETED,
                                            instituteProposalSpecialReviewBean.getProposalNumber(),instituteProposalSpecialReviewBean.getSequenceNumber(),spRevNum,ModuleConstants.PROTOCOL_MODULE_CODE);
                                }
                                if(size==0)
                                    indicator = "N1";
                            }
                        }
                        if(protocolFundingSourceBean != null){
                            procedures.add(updateProtocolNotePad(protocolFundingSourceBean));
                            procedures.add(updateProtocolLink(protocolFundingSourceBean,instituteProposalSpecialReviewBean));
                            protocolUpdateTxnBean.updateFundingSourceIndicator(
                            protocolFundingSourceBean.getProtocolNumber(),indicator,"7");
                            if(protocolUpdateTxnBean.updateInboxTable(spRevNum,ModuleConstants.PROTOCOL_MODULE_CODE,instituteProposalSpecialReviewBean.getAcType()) != null){
                                procedures.addAll(protocolUpdateTxnBean.updateInboxTable(spRevNum,ModuleConstants.PROTOCOL_MODULE_CODE,instituteProposalSpecialReviewBean.getAcType()));
                            }
                            protocolData.addElement(protocolFundingSourceBean);
                        }
                        
                    }
                }
            }
        }
        return procedures;
    }//End Coeus Enhancement case:#1799 step:6
    
    /**
     * Getter for property unitNumber.
     * @return Value of property unitNumber.
     */
    public java.lang.String getUnitNumber() {
        return unitNumber;
    }
    
    /**
     * Setter for property unitNumber.
     * @param unitNumber New value of property unitNumber.
     */
    public void setUnitNumber(java.lang.String unitNumber) {
        this.unitNumber = unitNumber;
    }
    //End Coeus Enhancement case:#1799  step:5
    
    // 3823: Key Person Records Needed in Inst Proposal and Award - Start
    /**
     * 
     * Method used to insert/update Institute Proposal Key Persons
     *  <li>To fetch the data, it uses UPDATE_INST_PROP_KEY_PERSONS procedure.
     * 
     * 
     * @param instituteProposalKeyPersonFormBean InstituteProposalKeyPersonFormBean
     * @return booleanKeyPersonBean
     * @exception DBException if the instance of a dbEngine is null.
     * @exception CoeusException if the DB instance is not available.
     */
    public ProcReqParameter addUpdInstituteProposalKeyPersons( InstituteProposalKeyPersonBean instituteProposalKeyPersonFormBean )
    throws CoeusException ,DBException{
        Vector param = new Vector();
        param = new Vector();
        param.addElement(new Parameter("PROPOSAL_NUMBER",
                DBEngineConstants.TYPE_STRING,
                instituteProposalKeyPersonFormBean.getProposalNumber()));
        param.addElement(new Parameter("SEQUENCE_NUMBER",
                DBEngineConstants.TYPE_INT,
                String.valueOf(instituteProposalKeyPersonFormBean.getSequenceNumber())));
        param.addElement(new Parameter("PERSON_ID",
                DBEngineConstants.TYPE_STRING,
                instituteProposalKeyPersonFormBean.getPersonId()));
        param.addElement(new Parameter("PERSON_NAME",
                DBEngineConstants.TYPE_STRING,
                instituteProposalKeyPersonFormBean.getPersonName()));
        param.addElement(new Parameter("PROJECT_ROLE",
                DBEngineConstants.TYPE_STRING,
                instituteProposalKeyPersonFormBean.getProjectRole()));
        param.addElement(new Parameter("FACULTY_FLAG",
                DBEngineConstants.TYPE_STRING,
                instituteProposalKeyPersonFormBean.isFacultyFlag() ? "Y" : "N"));
        param.addElement(new Parameter("NON_MIT_PERSON_FLAG",
                DBEngineConstants.TYPE_STRING,
                instituteProposalKeyPersonFormBean.isNonMITPersonFlag() ? "Y" : "N"));
        param.addElement(new Parameter("PERCENTAGE_EFFORT",
                DBEngineConstants.TYPE_FLOAT,
                ""+instituteProposalKeyPersonFormBean.getPercentageEffort()));       
        param.addElement(new Parameter("ACADEMIC_YEAR_EFFORT",
                DBEngineConstants.TYPE_FLOAT,
                ""+instituteProposalKeyPersonFormBean.getAcademicYearEffort()));
        param.addElement(new Parameter("SUMMER_YEAR_EFFORT",
                DBEngineConstants.TYPE_FLOAT,
                ""+instituteProposalKeyPersonFormBean.getSummerYearEffort()));
        param.addElement(new Parameter("CALENDAR_YEAR_EFFORT",
                DBEngineConstants.TYPE_FLOAT,
                ""+instituteProposalKeyPersonFormBean.getCalendarYearEffort()));        
        param.addElement(new Parameter("UPDATE_TIMESTAMP",
                DBEngineConstants.TYPE_TIMESTAMP,dbTimestamp));
        param.addElement(new Parameter("UPDATE_USER",
                DBEngineConstants.TYPE_STRING, userId));
        param.addElement(new Parameter("AW_PROPOSAL_NUMBER",
                DBEngineConstants.TYPE_STRING,
                instituteProposalKeyPersonFormBean.getProposalNumber()));
        param.addElement(new Parameter("AW_SEQUENCE_NUMBER",
                DBEngineConstants.TYPE_INT,
                ""+instituteProposalKeyPersonFormBean.getSequenceNumber()));
        param.addElement(new Parameter("AW_PERSON_ID",
                DBEngineConstants.TYPE_STRING,
                instituteProposalKeyPersonFormBean.getPersonId()));
        param.addElement(new Parameter("AW_UPDATE_USER",
                DBEngineConstants.TYPE_STRING, instituteProposalKeyPersonFormBean.getUpdateUser()));
        param.addElement(new Parameter("AW_UPDATE_TIMESTAMP",
                DBEngineConstants.TYPE_TIMESTAMP, instituteProposalKeyPersonFormBean.getUpdateTimestamp()));
        param.addElement(new Parameter("AC_TYPE",
                DBEngineConstants.TYPE_STRING,
                instituteProposalKeyPersonFormBean.getAcType()));

        StringBuffer sql = new StringBuffer(
                "call UPDATE_INST_PROP_KEY_PERSONS(");
        sql.append(" <<PROPOSAL_NUMBER>> , ");
        sql.append(" <<SEQUENCE_NUMBER>> , ");
        sql.append(" <<PERSON_ID>> , ");
        sql.append(" <<PERSON_NAME>> , ");
        sql.append(" <<PROJECT_ROLE>> , ");
        sql.append(" <<FACULTY_FLAG>> , ");
        sql.append(" <<NON_MIT_PERSON_FLAG>> , ");
        sql.append(" <<PERCENTAGE_EFFORT>> , ");
        sql.append(" <<ACADEMIC_YEAR_EFFORT>> , ");
        sql.append(" <<SUMMER_YEAR_EFFORT>> , ");
        sql.append(" <<CALENDAR_YEAR_EFFORT>> , ");
        sql.append(" <<UPDATE_USER>> , ");
        sql.append(" <<UPDATE_TIMESTAMP>> , ");
        sql.append(" <<AW_PROPOSAL_NUMBER>> , ");
        sql.append(" <<AW_SEQUENCE_NUMBER>> , ");
        sql.append(" <<AW_PERSON_ID>> , ");
        sql.append(" <<AW_UPDATE_USER>> , ");
        sql.append(" <<AW_UPDATE_TIMESTAMP>> , ");
        sql.append(" <<AC_TYPE>> )");
        
        ProcReqParameter procReqParameter = new ProcReqParameter();
        procReqParameter.setDSN(DSN);
        procReqParameter.setParameterInfo(param);
        procReqParameter.setSqlCommand(sql.toString());
        return procReqParameter;
    }
    // 3823: Key Person Records Needed in Inst Proposal and Award - End
    
    //Added for COEUSQA-1525 : Attachments for Institute Proposal - Start
    /**
     *  Method used to update/insert/delete Upload Document for Institute Proposal Attachment.
     *
     
     *  @return ProcReqParameter ProcReqParameter
     *  @exception DBException if any error during database transaction.
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
    public boolean addUpdProposalAttachment(InstituteProposalAttachmentBean proposalAttachmentBean)
    throws CoeusException,DBException{
        Vector param = new Vector();
        CoeusFunctions coeusFunctions = new CoeusFunctions();
        dbTimestamp = coeusFunctions.getDBTimestamp();
        StringBuffer sqlAttachmentDetails = new StringBuffer("") ;
        param.addElement(new Parameter("PROPOSAL_NUMBER",
                DBEngineConstants.TYPE_STRING,proposalAttachmentBean.getProposalNumber()));
        param.addElement(new Parameter("SEQUENCE_NUMBER",
                DBEngineConstants.TYPE_INT,new Integer(proposalAttachmentBean.getSequenceNumber())));
        param.addElement(new Parameter("ATTACHMENT_NUMBER",
                DBEngineConstants.TYPE_INT,new Integer(proposalAttachmentBean.getAttachmentNumber())));
        param.addElement(new Parameter("ATTACHMENT_TITLE",
                DBEngineConstants.TYPE_STRING,proposalAttachmentBean.getAttachmentTitle()));
        param.addElement(new Parameter("ATTACHMENT_TYPE_CODE",
                DBEngineConstants.TYPE_INT,proposalAttachmentBean.getAttachmentTypeCode()));
        if(proposalAttachmentBean.getFileBytes() != null){
            byte data[] = proposalAttachmentBean.getFileBytes();
            param.addElement(new Parameter("DOCUMENT",
                    DBEngineConstants.TYPE_BLOB, data ));
            param.addElement(new Parameter("DOC_UPDATE_USER",
                    DBEngineConstants.TYPE_STRING, userId));
            param.addElement(new Parameter("DOC_UPDATE_TIMESTAMP",
                    DBEngineConstants.TYPE_TIMESTAMP, dbTimestamp));
            param.addElement(new Parameter("FILE_NAME",
                    DBEngineConstants.TYPE_STRING,proposalAttachmentBean.getFileName()));
            param.addElement(new Parameter("MIME_TYPE",
                    DBEngineConstants.TYPE_STRING, proposalAttachmentBean.getMimeType()));
        }
          
        
        param.addElement(new Parameter("CONTACT_NAME",
                DBEngineConstants.TYPE_STRING, proposalAttachmentBean.getContactName()));
        param.addElement(new Parameter("PHONE_NUMBER",
                DBEngineConstants.TYPE_STRING, proposalAttachmentBean.getPhoneNumber()));
        param.addElement(new Parameter("EMAIL_ADDRESS",
                DBEngineConstants.TYPE_STRING, proposalAttachmentBean.getEmailAddress()));
        param.addElement(new Parameter("COMMENTS",
                DBEngineConstants.TYPE_STRING, proposalAttachmentBean.getComments()));
        param.addElement(new Parameter("UPDATE_USER",
                DBEngineConstants.TYPE_STRING, userId));
        param.addElement(new Parameter("UPDATE_TIMESTAMP",
                DBEngineConstants.TYPE_TIMESTAMP, dbTimestamp));
        param.addElement(new Parameter("AW_PROPOSAL_NUMBER",
                DBEngineConstants.TYPE_STRING,proposalAttachmentBean.getProposalNumber()));
        //COEUSQA-1525 - Attachments for Institute Proposal - Start
//        param.addElement(new Parameter("AW_SEQUENCE_NUMBER",
//                DBEngineConstants.TYPE_INT,new Integer(proposalAttachmentBean.getSequenceNumber())));
        param.addElement(new Parameter("AW_SEQUENCE_NUMBER",
                DBEngineConstants.TYPE_INT,new Integer(proposalAttachmentBean.getAwSequenceNumber())));
        //COEUSQA-1525 - End
        param.addElement(new Parameter("AW_ATTACHMENT_NUMBER",
                DBEngineConstants.TYPE_INT,new Integer(proposalAttachmentBean.getAttachmentNumber())));
        
        if(TypeConstants.INSERT_RECORD.equals(proposalAttachmentBean.getAcType())){
            sqlAttachmentDetails.append("insert into OSP$PROPOSAL_ATTACHMENTS(");
            sqlAttachmentDetails.append(" PROPOSAL_NUMBER , ");
            sqlAttachmentDetails.append(" SEQUENCE_NUMBER , ");
            sqlAttachmentDetails.append(" ATTACHMENT_NUMBER , ");
            sqlAttachmentDetails.append(" ATTACHMENT_TITLE , ");
            sqlAttachmentDetails.append(" ATTACHMENT_TYPE_CODE , ");
            if(proposalAttachmentBean.getFileBytes() != null){
                sqlAttachmentDetails.append(" DOCUMENT , ");
                sqlAttachmentDetails.append(" DOC_UPDATE_USER , ");
                sqlAttachmentDetails.append(" DOC_UPDATE_TIMESTAMP,");
                sqlAttachmentDetails.append(" MIME_TYPE , ");
                sqlAttachmentDetails.append(" FILE_NAME , ");
            }
            sqlAttachmentDetails.append(" CONTACT_NAME , ");
            sqlAttachmentDetails.append(" PHONE_NUMBER , ");
            sqlAttachmentDetails.append(" EMAIL_ADDRESS , ");
            sqlAttachmentDetails.append(" COMMENTS , ");
            sqlAttachmentDetails.append(" UPDATE_USER , ");
            sqlAttachmentDetails.append(" UPDATE_TIMESTAMP ) ");
            
            sqlAttachmentDetails.append(" VALUES (");
            sqlAttachmentDetails.append(" <<PROPOSAL_NUMBER>> , ");
            sqlAttachmentDetails.append(" <<SEQUENCE_NUMBER>> , ");
            sqlAttachmentDetails.append(" <<ATTACHMENT_NUMBER>> , ");
            sqlAttachmentDetails.append(" <<ATTACHMENT_TITLE>> , ");
            sqlAttachmentDetails.append(" <<ATTACHMENT_TYPE_CODE>> , ");
            
            if(proposalAttachmentBean.getFileBytes() != null){
                sqlAttachmentDetails.append(" <<DOCUMENT>> , ");
                sqlAttachmentDetails.append(" <<DOC_UPDATE_USER>> , ");
                sqlAttachmentDetails.append(" <<DOC_UPDATE_TIMESTAMP>> ,");
                sqlAttachmentDetails.append(" <<MIME_TYPE>> , ");
                sqlAttachmentDetails.append(" <<FILE_NAME>> , ");
            }
            sqlAttachmentDetails.append(" <<CONTACT_NAME>> , ");
            sqlAttachmentDetails.append(" <<PHONE_NUMBER>> , ");
            sqlAttachmentDetails.append(" <<EMAIL_ADDRESS>> , ");
            sqlAttachmentDetails.append(" <<COMMENTS>> , ");
            sqlAttachmentDetails.append(" <<UPDATE_USER>> , ");
            sqlAttachmentDetails.append(" <<UPDATE_TIMESTAMP>> ) ");
            
        }else if(TypeConstants.UPDATE_RECORD.equals(proposalAttachmentBean.getAcType())){
            sqlAttachmentDetails.append("update OSP$PROPOSAL_ATTACHMENTS set");
            sqlAttachmentDetails.append(" SEQUENCE_NUMBER =  ");
            sqlAttachmentDetails.append(" <<SEQUENCE_NUMBER>> , ");
            sqlAttachmentDetails.append(" ATTACHMENT_TITLE =  ");
            sqlAttachmentDetails.append(" <<ATTACHMENT_TITLE>> , ");
            sqlAttachmentDetails.append(" ATTACHMENT_TYPE_CODE =  ");
            sqlAttachmentDetails.append(" <<ATTACHMENT_TYPE_CODE>> , ");
            
            if(proposalAttachmentBean.getFileBytes() != null){
                sqlAttachmentDetails.append(" DOCUMENT =  ");
                sqlAttachmentDetails.append(" <<DOCUMENT>> , ");
                sqlAttachmentDetails.append(" DOC_UPDATE_USER =  ");
                sqlAttachmentDetails.append(" <<DOC_UPDATE_USER>> , ");
                sqlAttachmentDetails.append(" DOC_UPDATE_TIMESTAMP =  ");
                sqlAttachmentDetails.append(" <<DOC_UPDATE_TIMESTAMP>> , ");
                sqlAttachmentDetails.append(" MIME_TYPE =  ");
                sqlAttachmentDetails.append(" <<MIME_TYPE>> , ");
                sqlAttachmentDetails.append(" FILE_NAME =  ");
                sqlAttachmentDetails.append(" <<FILE_NAME>> , ");
            }
            
            sqlAttachmentDetails.append(" CONTACT_NAME =  ");
            sqlAttachmentDetails.append(" <<CONTACT_NAME>> , ");
            sqlAttachmentDetails.append(" PHONE_NUMBER =  ");
            sqlAttachmentDetails.append(" <<PHONE_NUMBER>> , ");
            sqlAttachmentDetails.append(" EMAIL_ADDRESS =  ");
            sqlAttachmentDetails.append(" <<EMAIL_ADDRESS>> , ");
            sqlAttachmentDetails.append(" COMMENTS =  ");
            sqlAttachmentDetails.append(" <<COMMENTS>> , ");
            sqlAttachmentDetails.append(" UPDATE_USER =  ");
            sqlAttachmentDetails.append(" <<UPDATE_USER>> , ");
            sqlAttachmentDetails.append(" UPDATE_TIMESTAMP =  ");
            sqlAttachmentDetails.append(" <<UPDATE_TIMESTAMP>>");
            sqlAttachmentDetails.append(" WHERE ");
            sqlAttachmentDetails.append(" PROPOSAL_NUMBER = ");
            sqlAttachmentDetails.append(" <<AW_PROPOSAL_NUMBER>> ");
            sqlAttachmentDetails.append(" and SEQUENCE_NUMBER = ");
            sqlAttachmentDetails.append(" <<AW_SEQUENCE_NUMBER>> ");            
            sqlAttachmentDetails.append(" and ATTACHMENT_NUMBER = ");
            sqlAttachmentDetails.append(" <<AW_ATTACHMENT_NUMBER>> ");
            
        }else if(TypeConstants.DELETE_RECORD.equals(proposalAttachmentBean.getAcType())){
            sqlAttachmentDetails.append(" delete from OSP$PROPOSAL_ATTACHMENTS where ");
            sqlAttachmentDetails.append(" PROPOSAL_NUMBER = ");
            sqlAttachmentDetails.append(" <<AW_PROPOSAL_NUMBER>> ");
            sqlAttachmentDetails.append(" and SEQUENCE_NUMBER = ");
            sqlAttachmentDetails.append(" <<AW_SEQUENCE_NUMBER>> ");
            sqlAttachmentDetails.append(" and ATTACHMENT_NUMBER = ");
            sqlAttachmentDetails.append(" <<AW_ATTACHMENT_NUMBER>> ");
        }
        if(dbEngine!=null) {
            dbEngine.executePreparedQuery("Coeus",sqlAttachmentDetails.toString(),param);
        } else {
            throw new CoeusException("db_exceptionCode.1000");
        }
        return true;
    }
    //COEUSQA-1525 : End
    
    //Added for COEUSQA-3119- Need to implement IACUC link to Award, IP, Prop Dev, and IRB - start
    //if inserted special review is of type Animal Subjects then linking has to be done between IACUC Protocol and IP
    private Vector performIACUCProtocolLinkFromInstProp(InstituteProposalSpecialReviewBean instituteProposalSpecialReviewBean)
    throws Exception,DBException{
        Vector procedures =  new Vector(5,3);
        CoeusFunctions coeusFunctions = new CoeusFunctions();
        CoeusMessageResourcesBean coeusMessageResourcesBean
          =new CoeusMessageResourcesBean();
        int approvalCode = instituteProposalSpecialReviewBean.getAcType().equals("D") ? instituteProposalSpecialReviewBean.getAw_ApprovalCode() : 
                                            instituteProposalSpecialReviewBean.getApprovalCode();
        int specialReviewCode = instituteProposalSpecialReviewBean.getAcType().equals("D") ? instituteProposalSpecialReviewBean.getAw_SpecialReviewCode() : 
                                            instituteProposalSpecialReviewBean.getSpecialReviewCode();
        if(approvalCode == Integer.parseInt(coeusFunctions.getParameterValue(CoeusConstants.LINKED_TO_IACUC_CODE)) &&
        Integer.parseInt(coeusFunctions.getParameterValue(CoeusConstants.ENABLE_IACUC_PROTOCOL_TO_PROPOSAL_LINK)) == 1 &&
        instituteProposalSpecialReviewBean.getAcType() != null && specialReviewCode ==
        Integer.parseInt(coeusFunctions.getParameterValue(CoeusConstants.IACUC_SPL_REV_TYPE_CODE))){
            edu.mit.coeus.iacuc.bean.ProtocolDataTxnBean protocolDataTxnBean = new edu.mit.coeus.iacuc.bean.ProtocolDataTxnBean(userId);
            if(iacucProtocolData == null)
                iacucProtocolData = new Vector();
            String spRevNum = instituteProposalSpecialReviewBean.getAcType().equals("I") ? instituteProposalSpecialReviewBean.getProtocolSPRevNumber() : instituteProposalSpecialReviewBean.getPrevSpRevProtocolNumber();
            CoeusDataTxnBean coeusDataTxnBean = new CoeusDataTxnBean();
            boolean validProtocolNumber = coeusDataTxnBean.validateIacucProtocolNumber(spRevNum);
            if(validProtocolNumber){
                boolean lockCheck = protocolDataTxnBean.lockCheck(spRevNum, userId);
                //while updating the rows in the module,if we have got the lock then no need to acquire the lock once again
                if(iacucProtocolData != null && iacucProtocolData.size() > 0){
                    for(int i  = 0;i<iacucProtocolData.size();i++){
                        edu.mit.coeus.iacuc.bean.ProtocolFundingSourceBean protocolFundingSourceBean = (edu.mit.coeus.iacuc.bean.ProtocolFundingSourceBean)iacucProtocolData.get(i);
                        if(protocolFundingSourceBean.getProtocolNumber().equals(spRevNum)){
                            lockCheck = true;
                        }
                    }
                }
                if(!lockCheck){
                    unLockIacucProtocols(iacucProtocolData);
                    String msg = "The Protocol is " + spRevNum+ " is modified by an other user";
                    throw new LockingException(msg);
                }else{
                    String indicator = "P1";
                    LockingBean lockingBean =  protocolDataTxnBean.getLock(spRevNum, userId, getUnitNumber());
                    protocolDataTxnBean.transactionCommit();
                    boolean lockAvailable = lockingBean.isGotLock();
                    if(lockAvailable){
                        edu.mit.coeus.iacuc.bean.ProtocolUpdateTxnBean protocolUpdateTxnBean = new edu.mit.coeus.iacuc.bean.ProtocolUpdateTxnBean(userId);
                        edu.mit.coeus.iacuc.bean.ProtocolFundingSourceBean protocolFundingSourceBean = null;
                        edu.mit.coeus.iacuc.bean.ProtocolInfoBean protocolInfoBean = (edu.mit.coeus.iacuc.bean.ProtocolInfoBean)protocolDataTxnBean.getProtocolMaintenanceDetails(spRevNum);
                        if(instituteProposalSpecialReviewBean.getAcType().equals("I")){//protocolNum1 == null) {
                            protocolFundingSourceBean = new edu.mit.coeus.iacuc.bean.ProtocolFundingSourceBean();
                            protocolFundingSourceBean.setProtocolNumber(spRevNum);
                            protocolFundingSourceBean.setSequenceNumber(protocolInfoBean.getSequenceNumber());
                            protocolFundingSourceBean.setFundingSourceTypeCode(5);
                            protocolFundingSourceBean.setFundingSource(instituteProposalSpecialReviewBean.getProposalNumber());
                            protocolFundingSourceBean.setAcType("I");
                            // Check for the exisstence protocol number in the funding source                          
                            int existsCount = protocolUpdateTxnBean.checkProtocolInFundingSource(
                                                spRevNum, protocolFundingSourceBean.getFundingSourceTypeCode(), 
                                                protocolFundingSourceBean.getFundingSource());
                            if(existsCount == 0){
                                procedures.add(protocolUpdateTxnBean.addUpdProtocolFundSource(protocolFundingSourceBean));
                            }
                            //Modified for COEUSDEV-75:Rework email engine so the email body is picked up from one place
//                            protocolDataTxnBean.sendEmailNotification(ModuleConstants.PROPOSAL_INSTITUTE_MODULE_CODE,MailActions.SPECIAL_REVIEW_INSERTED,
//                                    instituteProposalSpecialReviewBean.getProposalNumber(),instituteProposalSpecialReviewBean.getSequenceNumber(),spRevNum, ModuleConstants.IACUC_MODULE_CODE);
                        }else if(instituteProposalSpecialReviewBean.getAcType().equals("D")){//!protocolNum.equals(protocolNum1)) {
                            Vector vecSpData = protocolDataTxnBean.getProtocolFundingSources(spRevNum, protocolInfoBean.getSequenceNumber());
                            int size = vecSpData.size();
                            for(int i=0;i<size;i++) {
                                protocolFundingSourceBean = (edu.mit.coeus.iacuc.bean.ProtocolFundingSourceBean)vecSpData.get(i);
                                if(instituteProposalSpecialReviewBean.getProposalNumber().equals(protocolFundingSourceBean.getFundingSource())) {
                                    protocolFundingSourceBean.setAcType("D");
                                    protocolFundingSourceBean.setAwFundingSourceTypeCode(5);
                                    procedures.add(protocolUpdateTxnBean.addUpdProtocolFundSource(protocolFundingSourceBean));
                                    size-=1;
                                    //Modified for COEUSDEV-75:Rework email engine so the email body is picked up from one place
//                                    protocolDataTxnBean.sendEmailNotification(ModuleConstants.PROPOSAL_INSTITUTE_MODULE_CODE,MailActions.SPECIAL_REVIEW_DELETED,
//                                            instituteProposalSpecialReviewBean.getProposalNumber(),instituteProposalSpecialReviewBean.getSequenceNumber(),spRevNum,ModuleConstants.IACUC_MODULE_CODE);
                                }
                                if(size==0)
                                    indicator = "N1";
                            }
                        }
                        if(protocolFundingSourceBean != null){
                            procedures.add(updateIacucProtocolNotePad(protocolFundingSourceBean));
                            procedures.add(updateIacucProtocolLink(protocolFundingSourceBean,instituteProposalSpecialReviewBean));
                            protocolUpdateTxnBean.updateFundingSourceIndicator(
                            protocolFundingSourceBean.getProtocolNumber(),indicator,"9");
                           // if(protocolUpdateTxnBean.updateInboxTable(spRevNum,ModuleConstants.IACUC_MODULE_CODE,instituteProposalSpecialReviewBean.getAcType()) != null){
                                procedures.addAll(protocolUpdateTxnBean.updateInboxTable(spRevNum,ModuleConstants.IACUC_MODULE_CODE,instituteProposalSpecialReviewBean.getAcType()));
                          //  }
                            iacucProtocolData.addElement(protocolFundingSourceBean);
                        }
                        unLockIacucProtocols(iacucProtocolData);
                    }
                }
            }
        }
        return procedures;
    }
    
    private void unLockIacucProtocols(Vector iacucProtocolData) throws CoeusException,DBException{
        if(iacucProtocolData != null && iacucProtocolData.size()>0) {
            for(int index=0;index<iacucProtocolData.size();index++) {
                edu.mit.coeus.iacuc.bean.ProtocolFundingSourceBean bean = (edu.mit.coeus.iacuc.bean.ProtocolFundingSourceBean)iacucProtocolData.get(index);
                transMon.releaseLock("osp$IACUC Protocol_"+bean.getProtocolNumber(),userId); // check for the file name whether is shud b osp$ac_protocol or 
            }
        }
    }
    
    //Insert a message whenever the proposal is being inserted to the funding sources thru linking
    public ProcReqParameter  updateIacucProtocolNotePad( edu.mit.coeus.iacuc.bean.ProtocolFundingSourceBean protocolFundingSourceBean) throws CoeusException,DBException{
        edu.mit.coeus.iacuc.bean.ProtocolDataTxnBean protocolDataTxnBean = new edu.mit.coeus.iacuc.bean.ProtocolDataTxnBean();
        edu.mit.coeus.iacuc.bean.ProtocolNotepadBean protocolNotepadBean = new edu.mit.coeus.iacuc.bean.ProtocolNotepadBean();
        protocolNotepadBean.setProtocolNumber(protocolFundingSourceBean.getProtocolNumber());
        protocolNotepadBean.setSequenceNumber(protocolFundingSourceBean.getSequenceNumber());
        int maxEntryNumber = protocolDataTxnBean.getMaxProtocolNotesEntryNumber(protocolFundingSourceBean.getProtocolNumber());
        maxEntryNumber = maxEntryNumber + 1;
        protocolNotepadBean.setEntryNumber(maxEntryNumber);
        if(protocolFundingSourceBean.getAcType() != null && protocolFundingSourceBean.getAcType().equals("I")){
           CoeusMessageResourcesBean coeusMessageResourcesBean
                                =new CoeusMessageResourcesBean();
            String insertComments = coeusMessageResourcesBean.parseMessageKey("notepadInsert_IACUCexceptionCode.6000");
            protocolNotepadBean.setComments(insertComments);
        }else{
            CoeusMessageResourcesBean coeusMessageResourcesBean =new CoeusMessageResourcesBean();
            String deleteComments = coeusMessageResourcesBean.parseMessageKey("notepadInsert_IACUCexceptionCode.6001");
            protocolNotepadBean.setComments(deleteComments);
        }
        
        protocolNotepadBean.setRestrictedFlag(false);
        protocolNotepadBean.setAcType("I");
        protocolNotepadBean.setUpdateTimestamp(dbTimestamp);
        edu.mit.coeus.iacuc.bean.ProtocolUpdateTxnBean protocolUpdateTxnBean = new edu.mit.coeus.iacuc.bean.ProtocolUpdateTxnBean(userId);
        return protocolUpdateTxnBean.addUpdProtocolNotepad(protocolNotepadBean);
    }
     
    //to insert to the protocol links table when the linking takes place from the institute
     private ProcReqParameter updateIacucProtocolLink(edu.mit.coeus.iacuc.bean.ProtocolFundingSourceBean protocolFundingSourceBean,InstituteProposalSpecialReviewBean instituteProposalSpecialReviewBean) throws CoeusException,DBException{
            edu.mit.coeus.iacuc.bean.ProtocolLinkBean protocolLinkBean = new edu.mit.coeus.iacuc.bean.ProtocolLinkBean();
            protocolLinkBean.setProtocolNumber(protocolFundingSourceBean.getProtocolNumber());
            protocolLinkBean.setSequenceNumber(protocolFundingSourceBean.getSequenceNumber());
            protocolLinkBean.setModuleCode(2);
            protocolLinkBean.setModuleItemKey(instituteProposalSpecialReviewBean.getProposalNumber());
            protocolLinkBean.setModuleItemSeqNumber(instituteProposalSpecialReviewBean.getSequenceNumber());
            if(protocolFundingSourceBean.getAcType() != null && protocolFundingSourceBean.getAcType().equals("I")){
                CoeusMessageResourcesBean coeusMessageResourcesBean =new CoeusMessageResourcesBean();
                String insertComments = coeusMessageResourcesBean.parseMessageKey("notepadInsert_IACUCexceptionCode.6000");
                protocolLinkBean.setComments(insertComments);
                protocolLinkBean.setActionType("I");
            }else{
                CoeusMessageResourcesBean coeusMessageResourcesBean = new CoeusMessageResourcesBean();
                String deleteComments = coeusMessageResourcesBean.parseMessageKey("notepadDelete_IACUCexceptionCode.6001 ");
                protocolLinkBean.setComments(deleteComments);
                protocolLinkBean.setActionType("D");
            }
            edu.mit.coeus.iacuc.bean.ProtocolUpdateTxnBean protocolUpdateTxnBean = new edu.mit.coeus.iacuc.bean.ProtocolUpdateTxnBean(userId);
            return protocolUpdateTxnBean.addUpdProtocolLinks(protocolLinkBean);
            
        }
    //Added for COEUSQA-3119- Need to implement IACUC link to Award, IP, Prop Dev, and IRB - end
}
