/*
 * FundingSourcesAction.java
 *
 * Created on March 3, 2005, 12:02 PM
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

/* PMD check performed, and commented unused imports and variables on 13-JULY-2011
 * by Bharati
 */

package edu.mit.coeuslite.iacuc.action;

import edu.mit.coeus.award.bean.AwardBean;
import edu.mit.coeus.award.bean.AwardLookUpDataTxnBean;
import edu.mit.coeus.award.bean.AwardSpecialReviewBean;
import edu.mit.coeus.award.bean.AwardTxnBean;
import edu.mit.coeus.award.bean.AwardUpdateTxnBean;
import edu.mit.coeus.bean.CoeusMessageResourcesBean;
import edu.mit.coeus.bean.SpecialReviewFormBean;
import edu.mit.coeus.bean.UserInfoBean;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.iacuc.bean.ProtocolDataTxnBean;
import edu.mit.coeus.iacuc.bean.ProtocolLinkBean;
import edu.mit.coeus.iacuc.bean.ProtocolUpdateTxnBean;
import edu.mit.coeus.instprop.bean.InstituteProposalBean;
import edu.mit.coeus.instprop.bean.InstituteProposalSpecialReviewBean;
import edu.mit.coeus.instprop.bean.InstituteProposalTxnBean;
import edu.mit.coeus.instprop.bean.InstituteProposalUpdateTxnBean;
import edu.mit.coeus.iacuc.bean.ProtocolFundingSourceBean;
import edu.mit.coeus.propdev.bean.NotepadBean;
import edu.mit.coeus.propdev.bean.NotepadTxnBean;
import edu.mit.coeus.utils.CoeusConstants;
import edu.mit.coeus.utils.CoeusFunctions;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.MailActions;
import edu.mit.coeus.utils.ModuleConstants;
import edu.mit.coeus.utils.dbengine.DBEngineImpl;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.utils.dbengine.ProcReqParameter;
import edu.mit.coeus.utils.dbengine.TransactionMonitor;
import edu.mit.coeus.utils.locking.LockingBean;
import edu.mit.coeus.utils.locking.LockingException;
//import edu.mit.coeus.utils.UtilFactory;
//start--1

import edu.mit.coeuslite.utils.CoeusLiteConstants;
import edu.mit.coeuslite.utils.LockBean;
//end--1

import edu.mit.coeuslite.utils.bean.MenuBean;
import edu.mit.coeuslite.utils.bean.WebTxnBean;
import edu.mit.coeuslite.utils.WebUtilities;
import java.sql.Timestamp;    
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
//import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
//import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.validator.DynaValidatorForm;

/**
 *
 *
 */
public class FundingSourcesAction extends ProtocolBaseAction {
//    private ActionForward actionForward;
//    private WebTxnBean webTxnBean ;
    public static final String EMPTY_STRING = "";
    private static final String SAVE_STATUS = "AC005";
    private static final String MENU_ITEMS ="iacucmenuItemsVector";
    private static final String PROTOCOL_NUMBER="protocolNumber";
    private static final String SEQUENCE_NUMBER="sequenceNumber";
    private static final String AC_TYPE="acType";
    private static final String FUNDING_SOURCE_FORM="iacucFundingSourceFrm";
    private static final String GET_PROTOCOL_FUNDING_SOURCES="getIacucFundingSources";
    private static final String UPDATE_PROTOCOL_INDICATOR="updateIacucIndicator";
    private static final String UPDATE_PROTOCOL_FUNDING_SOURCE="addUpdIacucFundSource";
    private static final String UPDATE_MENU_CHECKlIST="updateMenuCheckList";
    private static final String FUNDING_SOURCE_FEILD ="FUNDING_SOURCE";
    private static final String AC_TYPE_INSERT="I";
    private static final String AC_TYPE_DELETE="D";
    private static final String AC_TYPE_UPDATE="U";
    private static final String FUNDING_SOURCE_TIMESTAMP="fundingSourceTimeStamp";
    private static final String UPDATE_USER = "fundingSourceUpdateUser";
    private static final String FUNDING_SOURCE="fundingSource";
    private static final String USER ="user";
    private static final String SUCCESS="success";
    private static final String FUNDING_SOURCE_TYPE_CODE ="code";
    private static final String ERROR_PROTO_FUNDING_SOURCE="error.proto_funding_source";
    private static final String RELEASE_LOCK_FOR = "release_lock_for";
    
    //Added for COEUSQA-3119- Need to implement IACUC link to Award, IP, Prop Dev, and IRB - start
    private DBEngineImpl dbEngine;
    private Vector lockedBeans = null;
    private TransactionMonitor transMon;
    private static final String DESCRIPTION = "description";
    private Timestamp dbTimestamp;
    private CoeusVector notepadBeans = null;
    private String navigator = "success";
    private String typeCode = "";
    //Added for COEUSQA-3119- Need to implement IACUC link to Award, IP, Prop Dev, and IRB - end
        
    
     /** Creates a new instance of FundingSourcesAction */
    public FundingSourcesAction() {
    }
    
    public void cleanUp() {
    }
    
    public ActionForward performExecute(ActionMapping mapping,
                                        ActionForm form,
                                        HttpServletRequest request, 
                                        HttpServletResponse response) throws Exception {
        String protocolNumber=EMPTY_STRING;
        String seqNum=EMPTY_STRING;
        int sequenceNumber = -1;
        HttpSession session = request.getSession();
        DynaValidatorForm dynaValidatorForm= (DynaValidatorForm)form; 
        WebTxnBean webTxnBean=new WebTxnBean();
        HashMap hmpfundingSource = new HashMap();
        Vector procedures = new Vector(5,3);
        
        //start--2
        
        protocolNumber=(String)session.getAttribute(CoeusLiteConstants.IACUC_PROTOCOL_NUMBER+session.getId());
        seqNum=(String)session.getAttribute(CoeusLiteConstants.IACUC_SEQUENCE_NUMBER+session.getId());
        //end--2
          
         if(seqNum!= null){
            
            sequenceNumber = Integer.parseInt(seqNum);
        }
        hmpfundingSource.put(PROTOCOL_NUMBER,protocolNumber);
        hmpfundingSource.put(SEQUENCE_NUMBER,new Integer(sequenceNumber));        
        dynaValidatorForm.set(PROTOCOL_NUMBER,protocolNumber);
        dynaValidatorForm.set(SEQUENCE_NUMBER,seqNum);               
        String acType = (String)dynaValidatorForm.get(AC_TYPE);
        //COEUSQA-3119- Need to implement IACUC link to Award, IP, Prop Dev, and IRB - start
        typeCode = (String)dynaValidatorForm.get("code");
        navigator = "success";
        //COEUSQA-3119- Need to implement IACUC link to Award, IP, Prop Dev, and IRB - end
        UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute(USER+session.getId());        
        String unitNumber=userInfoBean.getUnitNumber();
        String loggedinUser=userInfoBean.getUserId(); 
        String userName=userInfoBean.getUserName();  
        
        /* Checks if the user has lock
         * if the user doesnt have lock he should not save funding source
         */
        LockBean lockBean = getLockingBean(userInfoBean, (String)session.getAttribute(CoeusLiteConstants.IACUC_PROTOCOL_NUMBER+session.getId()), request);
        boolean isLockExists = isLockExists(lockBean, lockBean.getModuleKey());
        LockBean lockData = getLockedData(CoeusLiteConstants.IACUC_PROTO_LOCK_STR+lockBean.getModuleNumber(), request);

         //modified for editing funding source in display mode - start
        String functionType =  (String)session.getAttribute(CoeusLiteConstants.MODE_DETAILS+session.getId());          
        Boolean fundingSourceMode = (Boolean) session.getAttribute("MODIFY_IACUC_PROTOCOL_FUNDING_SOURCE"+session.getId());  
        
        if(functionType!=null && !functionType.equals(EMPTY_STRING)){           
            if(functionType.equalsIgnoreCase("D") && fundingSourceMode.booleanValue()){
                if(isLockExists){
                    lockProtocol(protocolNumber, request);
                    if(!acType.equals(EMPTY_STRING)){ 
                       if(acType.equals(AC_TYPE_INSERT)){
                           boolean isSuccess = false;
                           isSuccess=saveFundingSource(session, request, dynaValidatorForm, hmpfundingSource, protocolNumber, seqNum);
                           if(isSuccess){
                               //COEUSQA-3119- Need to implement IACUC link to Award, IP, Prop Dev, and IRB - start
//                                releaseLock(lockBean, request);
//                                dynaValidatorForm.reset(mapping,request);
//                                return mapping.findForward(SUCCESS);
                               //sending mail after insertion of funding source
                               if(typeCode.equals("5")|| typeCode.equals("6")){ // 5 - Institute Proposal , 6- Award
                                    session.setAttribute("IACUC_ACTION_CODE","552");
                                    session.setAttribute("MODULE_CODE", (""+ModuleConstants.IACUC_MODULE_CODE));
                                    navigator = "iacucEmail";
                                }
                               releaseLock(lockBean, request);
                               dynaValidatorForm.reset(mapping,request);                                                               
                               return mapping.findForward(navigator);
                               //COEUSQA-3119- Need to implement IACUC link to Award, IP, Prop Dev, and IRB - end
                           }                          
                       }
                       else if(dynaValidatorForm.get(AC_TYPE).equals(AC_TYPE_DELETE)){
                          boolean isSuccess=false;
                          isSuccess=deleteFundingSource(session, request, dynaValidatorForm, hmpfundingSource, protocolNumber, seqNum);
                          //COEUSQA-3119- Need to implement IACUC link to Award, IP, Prop Dev, and IRB - start   
                          //Sending mail after deletion of the funding source
                          if(typeCode.equals("5")|| typeCode.equals("6")){ // 5 - Institute Proposal , 6- Award
                              session.setAttribute("IACUC_ACTION_CODE","553");
                              session.setAttribute("MODULE_CODE", (""+ModuleConstants.IACUC_MODULE_CODE));
                              navigator = "iacucEmail";
                          }                          
                          //COEUSQA-3119- Need to implement IACUC link to Award, IP, Prop Dev, and IRB - end
                      }
                    }
                    releaseLock(lockBean, request);
                 }else{
                    String errMsg = RELEASE_LOCK_FOR;
                    ActionMessages messages = new ActionMessages();
                    messages.add("errMsg", new ActionMessage(errMsg,lockBean.getModuleKey(),lockBean.getModuleNumber()));
                    saveMessages(session, messages);
                }
            }else{
                //Check if lock exists or not
                if(!isLockExists && lockBean.getSessionId().equals(lockData.getSessionId())) {
                    if(!acType.equals(EMPTY_STRING)){ 
                       if(acType.equals(AC_TYPE_INSERT)){
                           boolean isSuccess = false;
                               isSuccess=saveFundingSource(session, request, dynaValidatorForm, hmpfundingSource, protocolNumber, seqNum);
                           if(isSuccess){                                
                                //COEUSQA-3119- Need to implement IACUC link to Award, IP, Prop Dev, and IRB - start
                                if(typeCode.equals("5") || typeCode.equals("6")){ // 5 - Institute Proposal , 6- Award
                                    session.setAttribute("IACUC_ACTION_CODE","552");
                                    session.setAttribute("MODULE_CODE", (""+ModuleConstants.IACUC_MODULE_CODE));
                                    navigator = "iacucEmail";
                                }
                                dynaValidatorForm.reset(mapping,request);
                                //return mapping.findForward(SUCCESS);                                
                                return mapping.findForward(navigator);
                                //COEUSQA-3119- Need to implement IACUC link to Award, IP, Prop Dev, and IRB - end
                           }                          
                       }
                       else if(dynaValidatorForm.get(AC_TYPE).equals(AC_TYPE_DELETE)){
                          boolean isSuccess=false;
                          isSuccess=deleteFundingSource(session, request, dynaValidatorForm, hmpfundingSource, protocolNumber, seqNum);
                          //COEUSQA-3119- Need to implement IACUC link to Award, IP, Prop Dev, and IRB - start                          
                          if(typeCode.equals("5") || typeCode.equals("6")){ // 5 - Institute Proposal , 6- Award
                              session.setAttribute("IACUC_ACTION_CODE","553");
                              session.setAttribute("MODULE_CODE", (""+ModuleConstants.IACUC_MODULE_CODE));
                              navigator = "iacucEmail";
                          }                          
                          //COEUSQA-3119- Need to implement IACUC link to Award, IP, Prop Dev, and IRB - end
                      }
                    }
                }else{
                    String errMsg = RELEASE_LOCK_FOR;
                    ActionMessages messages = new ActionMessages();
                    messages.add("errMsg", new ActionMessage(errMsg,lockBean.getModuleKey(),lockBean.getModuleNumber()));
                    saveMessages(session, messages);
                }
            }
        }
        //modified for editing funding source in display mode - end
        
        
        dynaValidatorForm.reset(mapping,request);
        readSavedStatus(request);
        //COEUSQA-3119- Need to implement IACUC link to Award, IP, Prop Dev, and IRB - start
//      return mapping.findForward(SUCCESS);
        return mapping.findForward(navigator);
        //COEUSQA-3119- Need to implement IACUC link to Award, IP, Prop Dev, and IRB - end
    }   
    
    
    /* In this method the form data is saved and retrieved for displaying
     * Funding source indicators are updated
     *@ param hmpfundingSource contains protocol number and sequence number
     */
    private boolean saveFundingSource(HttpSession session,HttpServletRequest request, 
                                      DynaValidatorForm dynaValidatorForm,
                                      HashMap hmpfundingSource,
                                      String protocolNumber,String seqNum)throws Exception{
      
                                          
               boolean isAllDeleted= false;
               boolean isMenuSaved = false;
               boolean isSuccess=false;
               Vector procedures = new Vector(5,3);
               dbEngine = new DBEngineImpl();
               transMon = TransactionMonitor.getInstance();
                
                //Added for validation of Number/Code field
                boolean fundingSourceNumber = false;
                fundingSourceNumber = checkFundingSourceNumber(dynaValidatorForm,request);
                
                if(fundingSourceNumber){
                
                   String fundSource=(String)dynaValidatorForm.get(FUNDING_SOURCE);
                   String acType = (String)dynaValidatorForm.get(AC_TYPE);
                   //Modified for COEUSDEV-326 : Lite - Protocol Funding source - application should validate the sponsor code entered by the user  - Start
                   String code = (String)dynaValidatorForm.get("code");
                   //COEUSDEV-326 : End
                   HashMap hmpIndMap = updateIndicators(dynaValidatorForm,session,protocolNumber,seqNum,isAllDeleted);
                   isMenuSaved = true;
                   HashMap hmpsaveStatus = updateSaveStatus(session,protocolNumber,isMenuSaved);                              
                   WebTxnBean webTxnBean = new WebTxnBean();
                   // Check for the Duplicate Persons - start
                   Hashtable htfundingSourceData = (Hashtable)webTxnBean.getResults(request,FUNDING_SOURCE_FORM,hmpfundingSource);
                   Vector vcfundingSource=(Vector)htfundingSourceData.get(GET_PROTOCOL_FUNDING_SOURCES);
                   
                   if(vcfundingSource!= null && vcfundingSource.size() > 0){
                       //Modified for COEUSDEV-326 : Lite - Protocol Funding source - application should validate the sponsor code entered by the user  - Start
//                       boolean isPresent = checkFundingSource(vcfundingSource,request,fundSource);
                       boolean isPresent = checkFundingSource(vcfundingSource,request,fundSource,code);
                       //COEUSDEV-326 : End
                       if(!isPresent){
                           session.setAttribute("iacucFundingSources",htfundingSourceData.get(GET_PROTOCOL_FUNDING_SOURCES));
                           isSuccess=true;
                           return isSuccess;
                       }
                    }// check for the dupliucate Persons - End
                   //Modified for indicator logic begin
                    vcfundingSource = (vcfundingSource == null)? new Vector() : vcfundingSource;
                   
                    
                    if(isGenerateSequence(request) ){
                         //Modified for COEUSQA-3064 Unable to save funding source type in Lite if it is other than base types supplied 
                         updateRecordsToNewSequence(vcfundingSource,FUNDING_SOURCE_TIMESTAMP, UPDATE_USER, UPDATE_PROTOCOL_FUNDING_SOURCE, request);
                       
                    } 
                    //Saving funding source details
                    webTxnBean.getResults(request,UPDATE_PROTOCOL_FUNDING_SOURCE, dynaValidatorForm);
                 
                   //Modified for indicator logic end
                    webTxnBean.getResults(request,UPDATE_PROTOCOL_INDICATOR, hmpIndMap);
                    Hashtable htFundingSourceTypes = (Hashtable)webTxnBean.getResults(request,FUNDING_SOURCE_FORM,hmpfundingSource);
                    //Update Save Status for the Menu check
                    //Hashtable updateSaveStatus=(Hashtable)webTxnBean.getResults(request, UPDATE_MENU_CHECKlIST, hmpsaveStatus);
                    //Update the Menu status to the session.
                    //updateSaveStatusToSession(session,isMenuSaved); 
                    resetDynaForm(dynaValidatorForm);
                    session.setAttribute("iacucFundingSources",htFundingSourceTypes.get(GET_PROTOCOL_FUNDING_SOURCES));
                    isSuccess = true;
                    
                    //Added for COEUSQA-3119- Need to implement IACUC link to Award, IP, Prop Dev, and IRB  - start
                    //If funding source is of type Institute Proposal and ENABLE_PROTOCOL_TO_PROPOSAL_LINK is enabled then
                    //protocol linking should be eastablished between protocol and Institue Proposal
                    CoeusFunctions functions= new CoeusFunctions();
                    String linkAward=functions.getParameterValue(CoeusConstants.ENABLE_IACUC_PROTOCOL_TO_AWARD_LINK);
                    String linkProposal=functions.getParameterValue(CoeusConstants.ENABLE_IACUC_PROTOCOL_TO_PROPOSAL_LINK);
                    String indicator="P1";
                    UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute(USER+session.getId());
                    String userId = userInfoBean.getUserId();
                    String unitNumber=userInfoBean.getUnitNumber();
                    ProtocolFundingSourceBean protocolFundingSourceBean = null;
                    lockedBeans = new Vector();
                    ProtocolUpdateTxnBean protocolUpdateTxnBean = new ProtocolUpdateTxnBean(userId);
                    int sequenceNum = 0;
                    
                    CoeusMessageResourcesBean coeusMessageResourcesBean = new CoeusMessageResourcesBean();
                    if(code.equals("5")){
                        if(linkProposal.equals("1")){
                            InstituteProposalTxnBean instituteProposalTxnBean = new InstituteProposalTxnBean();
                            boolean getLock = instituteProposalTxnBean.lockCheck(fundSource,userId);
                            if(getLock){
                                InstituteProposalBean instituteProposalBean = instituteProposalTxnBean.getInstituteProposalDetails(fundSource);
                                int maxSpecialRevNumber = instituteProposalTxnBean.getMaxInstituteProposalSpecialReviewNumber(fundSource,instituteProposalBean.getSequenceNumber());
                                
                                InstituteProposalUpdateTxnBean instituteProposalUpdateTxnBean = new InstituteProposalUpdateTxnBean(userId);
                                InstituteProposalSpecialReviewBean insituteProposalSpReviewBean = null;
                                try {
                                    LockingBean lockingBean = instituteProposalTxnBean.getInstituteProposalLock(fundSource,userId,unitNumber);
                                } catch(Exception e) {
                                    unLockBeans(userId);
                                    String msg = coeusMessageResourcesBean.parseMessageKey("locking_exceptionCode.1032")+" "+fundSource+" "+coeusMessageResourcesBean.parseMessageKey("locking_exceptionCode.1030");
                                    throw new LockingException(msg);
                                }
                                if(acType.equals(AC_TYPE_INSERT)){
                                    insituteProposalSpReviewBean = new InstituteProposalSpecialReviewBean();
                                    insituteProposalSpReviewBean.setProposalNumber(fundSource);
                                    insituteProposalSpReviewBean.setSpecialReviewCode(new Integer(functions.getParameterValue(CoeusConstants.IACUC_SPL_REV_TYPE_CODE)).intValue());
                                    insituteProposalSpReviewBean.setApprovalCode(new Integer(functions.getParameterValue(CoeusConstants.LINKED_TO_IACUC_CODE)).intValue());
                                    insituteProposalSpReviewBean.setSpecialReviewNumber(maxSpecialRevNumber+1);
                                    insituteProposalSpReviewBean.setProtocolSPRevNumber(protocolNumber);
                                    insituteProposalSpReviewBean.setAcType("I");
                                    insituteProposalSpReviewBean.setComments(coeusMessageResourcesBean.parseMessageKey("comment_exceptionCode_IACUCSpecialReview.1000"));
                                    
                                    insituteProposalSpReviewBean.setSequenceNumber(instituteProposalBean.getSequenceNumber());
                                    lockedBeans.add(insituteProposalSpReviewBean);
                                    ProtocolDataTxnBean protocolDataTxnBean = new ProtocolDataTxnBean();
                                    //Checking for the link, if link is exist then update the funding source indicator
                                    //Send the mail notification as special review inserted for particular module
                                    boolean canLink = protocolDataTxnBean.canAddProtocolLinks(protocolNumber, "2",fundSource);
                                    if(protocolNumber != null && protocolNumber.length() == 10 && canLink){
                                        procedures.add(instituteProposalUpdateTxnBean.addUpdInstituteProposalSpecialReview(insituteProposalSpReviewBean));
                                        protocolUpdateTxnBean.updateFundingSourceIndicator(fundSource,indicator,"2");
                                        sequenceNum = Integer.parseInt(seqNum);
                                        try {
//                                            protocolDataTxnBean.sendEmailNotification(ModuleConstants.IACUC_MODULE_CODE,MailActions.SPECIAL_REVIEW_INSERTED,
//                                                    protocolNumber,sequenceNum,fundSource,ModuleConstants.PROPOSAL_INSTITUTE_MODULE_CODE);
                                            
                                        } catch(Exception e) {
                                        }
                                        procedures.add(updateSpecialReviewLink(protocolNumber,sequenceNum,insituteProposalSpReviewBean,userId));
                                        //need to get desc for the funding code
                                        /*protocolFundingSourceBean = new ProtocolFundingSourceBean();
                                        Vector vecFundTypeDesc = protocolDataTxnBean.getFundSourceTypes();
                                        protocolFundingSourceBean = (ProtocolFundingSourceBean) vecFundTypeDesc.get(5);
                                        String fundDesc = protocolFundingSourceBean.getFundingSourceTypeDesc(); // chk value is coming or*/
                                        
                                        //protocolUpdateTxnBean.updateSpecialReviewNotePad(acType); // this has to be done
                                        Vector inboxData = protocolUpdateTxnBean.updateInboxTable(fundSource,ModuleConstants.PROPOSAL_INSTITUTE_MODULE_CODE,acType);
                                        if(inboxData != null)
                                            procedures.addAll(inboxData);
                                    }else if(insituteProposalSpReviewBean != null){
                                        procedures.add(updateSpecialReviewLink(protocolNumber,sequenceNum,insituteProposalSpReviewBean,userId));
                                    }
                                    //execute all the procedures
                                    if(procedures.size() > 0){
                                        if(dbEngine!=null){
                                            dbEngine.executeStoreProcs(procedures);
                                        }else{
                                            throw new CoeusException("db_exceptionCode.1000");
                                        }
                                    }                                    
                                }
                            } else {
                                unLockBeans(userId);
                                String msg = coeusMessageResourcesBean.parseMessageKey("locking_exceptionCode.1032")+" "+fundSource+" "+coeusMessageResourcesBean.parseMessageKey("locking_exceptionCode.1030");
                                throw new LockingException(msg);
                            }
                        }
                        //If funding source is of type Award and ENABLE_PROTOCOL_TO_AWARD_LINK is enabled then
                        //protocol linking should be eastablished between protocol and Award
                    }else if(code.equals("6")){
                        if(linkAward.equals("1")){
                            AwardTxnBean awardTxnBean = new AwardTxnBean();
                            boolean getLock = awardTxnBean.lockCheck(fundSource,userId);
                            if(getLock){
                                AwardBean awardBean = awardTxnBean.getAward(fundSource);
                                AwardLookUpDataTxnBean awardLookUpDataTxnBean = new AwardLookUpDataTxnBean();
                                int maxSpecialRevNumber = awardLookUpDataTxnBean.getMaxAwardSpecialReviewNumber(fundSource,awardBean.getSequenceNumber());
                                AwardUpdateTxnBean awardUpdateTxnBean = new AwardUpdateTxnBean(userId);
                                AwardSpecialReviewBean awardSpReviewBean = null;
                                try {
                                    LockingBean lockingBean = awardTxnBean.getAwardLock(fundSource,userId,unitNumber);
                                } catch(Exception e) {
                                    unLockBeans(userId);
                                    String msg = coeusMessageResourcesBean.parseMessageKey("locking_exceptionCode.1031")+" "+fundSource+" "+coeusMessageResourcesBean.parseMessageKey("locking_exceptionCode.1030");
                                    throw new LockingException(msg);
                                }
                                if(acType.equals(AC_TYPE_INSERT)){
                                    awardSpReviewBean = new AwardSpecialReviewBean();
                                    awardSpReviewBean.setMitAwardNumber(fundSource);
                                    awardSpReviewBean.setSpecialReviewCode(new Integer(functions.getParameterValue(CoeusConstants.IACUC_SPL_REV_TYPE_CODE)).intValue());
                                    awardSpReviewBean.setApprovalCode(new Integer(functions.getParameterValue(CoeusConstants.LINKED_TO_IACUC_CODE)).intValue());
                                    awardSpReviewBean.setSpecialReviewNumber(maxSpecialRevNumber+1);
                                    awardSpReviewBean.setProtocolSPRevNumber(protocolNumber);
                                    awardSpReviewBean.setAcType("I");
                                    awardSpReviewBean.setComments(coeusMessageResourcesBean.parseMessageKey("comment_exceptionCode_IACUCSpecialReview.1000"));
                                    awardSpReviewBean.setSequenceNumber(awardBean.getSequenceNumber());
                                    
                                    lockedBeans.add(awardSpReviewBean);
                                    ProtocolDataTxnBean protocolDataTxnBean = new ProtocolDataTxnBean(userId);
                                    //Checking for the link, if link is exist then update the funding source indicator
                                    //Send the mail notification as special review inserted for particular module
                                    boolean canLink = protocolDataTxnBean.canAddProtocolLinks(protocolNumber, "2",fundSource);
                                    if(protocolNumber != null && protocolNumber.length() == 10 && canLink){
                                        procedures.add(awardUpdateTxnBean.addUpdAwardSpecialReview(awardSpReviewBean));
                                        protocolUpdateTxnBean.updateFundingSourceIndicator(fundSource,indicator,"1");
                                        sequenceNum = Integer.parseInt(seqNum);
                                        try {
//                                            protocolDataTxnBean.sendEmailNotification(ModuleConstants.IACUC_MODULE_CODE,MailActions.SPECIAL_REVIEW_INSERTED,
//                                                    protocolNumber,sequenceNum,fundSource,ModuleConstants.AWARD_MODULE_CODE);
                                        } catch(Exception e) {
                                        }
                                        procedures.add(updateSpecialReviewLink(protocolNumber,sequenceNum,awardSpReviewBean,userId));
                                        //updateSpecialReviewNotePad(protocolFundingSourceBean); /// this has to be done
                                        Vector inboxData = protocolUpdateTxnBean.updateInboxTable(fundSource,ModuleConstants.AWARD_MODULE_CODE,acType);
                                        if(inboxData != null)
                                            procedures.addAll(inboxData);
                                    }else if(awardSpReviewBean != null){
                                        procedures.add(updateSpecialReviewLink(protocolNumber,sequenceNum,awardSpReviewBean,userId));
                                    }
                                    //execute al the procdures
                                    if(procedures.size() > 0){
                                        if(dbEngine!=null){
                                            dbEngine.executeStoreProcs(procedures);
                                        }else{
                                            throw new CoeusException("db_exceptionCode.1000");
                                        }
                                    }                                    
                                }
                                //Releasing the lock
                            } else {
                                unLockBeans(userId);
                                String msg = coeusMessageResourcesBean.parseMessageKey("locking_exceptionCode.1031")+" "+fundSource+" "+coeusMessageResourcesBean.parseMessageKey("locking_exceptionCode.1030");
                                throw new LockingException(msg);
                            }                           
                        }                       
                    }
                }                
                //Added for COEUSQA-3119- Need to implement IACUC link to Award, IP, Prop Dev, and IRB - end
                
                readSavedStatus(request);
                return isSuccess;
                                          
    }
    
    /* In this method the form data is deleted and retrieved for displaying
     * Funding source indicators are updated
     */
    private boolean deleteFundingSource(HttpSession session,HttpServletRequest request,
            DynaValidatorForm dynaValidatorForm,
            HashMap hmpfundingSource,
            String protocolNumber,String seqNum)throws Exception{
        boolean isAllDeleted= false;
        boolean isMenuSaved = false;
        boolean isSuccess=false;
        HashMap hmpIndMap = null;
        HashMap hmpsaveStatus = null;
        CoeusFunctions functions= new CoeusFunctions();
        dbTimestamp = functions.getDBTimestamp();
        //Added for COEUSQA-3119- Need to implement IACUC link to Award, IP, Prop Dev, and IRB - start
        Vector procedures = new Vector(5,3);
        dbEngine = new DBEngineImpl();
        transMon = TransactionMonitor.getInstance();
        //Added for COEUSQA-3119- Need to implement IACUC link to Award, IP, Prop Dev, and IRB - end
        WebTxnBean webTxnBean = new WebTxnBean();
        Hashtable htfundingSourceData = (Hashtable)webTxnBean.getResults(request,FUNDING_SOURCE_FORM,hmpfundingSource);
        Vector vcfundData=(Vector)htfundingSourceData.get(GET_PROTOCOL_FUNDING_SOURCES);
        //String fundingSourceTypeCode=(String)dynaValidatorForm.get(FUNDING_SOURCE_TYPE_CODE);
        //String fundingSource=(String)dynaValidatorForm.get(FUNDING_SOURCE);
        String fundingSourceNumber = request.getParameter("fundingSourceNumber");
        String fundSource;
        String fundSourceDesc;
        for(int index=0;index<vcfundData.size();index++){
            DynaValidatorForm dynaForm=(DynaValidatorForm)vcfundData.get(index);
            //String fundingSourceCode=(String)dynaForm.get(FUNDING_SOURCE_TYPE_CODE);
            //Modified for Case#4514 - Can not delete sponsor funding source if sponsor code < 6 - Start
            //If the SponsorCode value is less than 6 character code, the value from database will have 6 characters(remaining character with space)
            //Since the SPONSOR_CODE datatype is CHAR with size 6, so trim is done
            //fundSource=((String)dynaForm.get(FUNDING_SOURCE));
            fundSource=((String)dynaForm.get(FUNDING_SOURCE)).trim();
            fundSourceDesc = (String) dynaForm.get(DESCRIPTION);
            //Case#4514 - End
            if(fundSource.equals(fundingSourceNumber)){
                dynaForm.set(AC_TYPE,AC_TYPE_DELETE);
                //webTxnBean.getResults(request,UPDATE_PROTOCOL_FUNDING_SOURCE, dynaForm);
                vcfundData.remove(index);
                //Added for indicator logic begin
                boolean isSequenceUpdated = false;
                if(isGenerateSequence(request) ){
                    //Modified for COEUSQA-3064 Unable to save funding source type in Lite if it is other than base types supplied
                    isSequenceUpdated = updateRecordsToNewSequence(vcfundData,FUNDING_SOURCE_TIMESTAMP, UPDATE_USER, UPDATE_PROTOCOL_FUNDING_SOURCE, request);
                }
                if(!isSequenceUpdated){
                    webTxnBean.getResults(request,UPDATE_PROTOCOL_FUNDING_SOURCE, dynaForm);
                }
                //Added for indicator logic end
                if(vcfundData== null || vcfundData.size()==0){
                    
                    isMenuSaved = false;
                    hmpIndMap = updateIndicators(dynaValidatorForm,session,protocolNumber,seqNum,true);
                    webTxnBean.getResults(request, UPDATE_PROTOCOL_INDICATOR, hmpIndMap);
                    hmpsaveStatus = updateSaveStatus(session,protocolNumber,isMenuSaved);
                } else{
                    //isAllDeleted = false;
                    isMenuSaved = true;
                    //Commented for indicator logic
                    //hmpIndMap = updateIndicators(dynaValidatorForm,session,protocolNumber,seqNum,isAllDeleted);
                    //webTxnBean.getResults(request, UPDATE_PROTOCOL_INDICATOR, hmpIndMap);
                    //update menu status
                    hmpsaveStatus = updateSaveStatus(session,protocolNumber,isMenuSaved);
                }
                
                //Added for COEUSQA-3119- Need to implement IACUC link to Award, IP, Prop Dev, and IRB - start
                //If special review type code is 5 and ENABLE_IACUC_PROTOCOL_TO_PROPOSAL_LINK is enabled then delete the special review from the
                //institute proposal and funding source from the Iacuc protocol
                String linkAward=functions.getParameterValue(CoeusConstants.ENABLE_IACUC_PROTOCOL_TO_AWARD_LINK);
                String linkProposal=functions.getParameterValue(CoeusConstants.ENABLE_IACUC_PROTOCOL_TO_PROPOSAL_LINK);
                String code = (String)dynaForm.get("code");
                // COEUSQA-3119- Need to implement IACUC link to Award, IP, Prop Dev, and IRB - start
                typeCode  = (String)dynaForm.get("code");
                // COEUSQA-3119- Need to implement IACUC link to Award, IP, Prop Dev, and IRB  - end
                String acType = (String)dynaForm.get(AC_TYPE);
                UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute(USER+session.getId());
                String userId = userInfoBean.getUserId();
                String indicator = "P1";
                CoeusMessageResourcesBean coeusMessageResourcesBean =new CoeusMessageResourcesBean();
                ProtocolUpdateTxnBean protocolUpdateTxnBean = new ProtocolUpdateTxnBean(userId);
                int sequenceNum = 0;
                if(code.equals("5")){
                    if(linkProposal.equals("1")){
                        InstituteProposalTxnBean instituteProposalTxnBean = new InstituteProposalTxnBean();
                        InstituteProposalSpecialReviewBean insituteProposalSpReviewBean = null;
                        ProtocolFundingSourceBean protocolFundingSourceBean = null;
                        InstituteProposalUpdateTxnBean instituteProposalUpdateTxnBean = new InstituteProposalUpdateTxnBean(userId);
                        Vector specRevCount =  instituteProposalTxnBean.getInstituteProposalSpecialReview(fundSource);
                        specRevCount = (specRevCount == null)? new CoeusVector() : specRevCount;
                        int size = specRevCount.size();
                        for(int i = 0;i<specRevCount.size();i++){
                            insituteProposalSpReviewBean =(InstituteProposalSpecialReviewBean)specRevCount.get(i);
                            String specialRevNum = insituteProposalSpReviewBean.getProtocolSPRevNumber();
                            specialRevNum = (specialRevNum == null)? "" : specialRevNum;
                            if(specialRevNum.equals(protocolNumber.substring(0, 10))){
                                insituteProposalSpReviewBean.setAcType("D");
                                break;
                            } else{
                                insituteProposalSpReviewBean = null;
                            }
                        }
                        
                        if(insituteProposalSpReviewBean==null){
                            ProtocolDataTxnBean protocolDataTxnBean = new ProtocolDataTxnBean(userId);
                            Vector vecProtocolLinks = protocolDataTxnBean.getProtocolLinksData(protocolNumber);
                            if(vecProtocolLinks!=null && !vecProtocolLinks.isEmpty()){
                                String fundingSrcToDelete = fundSource;
                                for(int i=0;i<vecProtocolLinks.size();i++){
                                    ProtocolLinkBean protocolLinkBean = (ProtocolLinkBean)vecProtocolLinks.elementAt(i);
                                    if(protocolLinkBean.getModuleItemKey().equals(fundingSrcToDelete)
                                    && "I".equals(protocolLinkBean.getActionType())){
                                        insituteProposalSpReviewBean = new InstituteProposalSpecialReviewBean();
                                        insituteProposalSpReviewBean.setProposalNumber(fundingSrcToDelete);
                                        insituteProposalSpReviewBean.setSequenceNumber(protocolLinkBean.getModuleItemSeqNumber());
                                        break;
                                    }
                                }
                            }
                        }
                        //lockedBeans.add(insituteProposalSpReviewBean);
                        if(insituteProposalSpReviewBean != null && protocolNumber != null && protocolNumber.length() == 10 && insituteProposalSpReviewBean.getAcType()!=null) {
                            procedures.add(instituteProposalUpdateTxnBean.addUpdInstituteProposalSpecialReview(insituteProposalSpReviewBean));
                            
                            size--;
                            //After deleting the funding source update the funding source indicator as N1
                            if(size==0)
                                indicator = "N1";
                            
                            protocolUpdateTxnBean.updateFundingSourceIndicator(fundSource,indicator,"2");
                            ProtocolDataTxnBean protocolDataTxnBean = new ProtocolDataTxnBean(userId);
                            sequenceNum = Integer.parseInt(seqNum);
                            //Need to send the notification for the deletion of special review as special review deleted
                            try {
//                                protocolDataTxnBean.sendEmailNotification(ModuleConstants.IACUC_MODULE_CODE,MailActions.SPECIAL_REVIEW_DELETED,
//                                        protocolNumber,sequenceNum,fundSource,ModuleConstants.PROPOSAL_INSTITUTE_MODULE_CODE);
                            } catch(Exception e) {
                            }
                            procedures.add(updateSpecialReviewLink(protocolNumber,sequenceNum,insituteProposalSpReviewBean,userId));
                            updateSpecialReviewNotePad(acType,fundSourceDesc,fundSource,code,userId);
                            Vector inboxData = protocolUpdateTxnBean.updateInboxTable(fundSource,ModuleConstants.PROPOSAL_INSTITUTE_MODULE_CODE,acType);
                            if(inboxData != null)
                                procedures.addAll(inboxData);
                        }
                    }
                    //If special review type code is 6 and ENABLE_IACUC_PROTOCOL_TO_AWARD_LINK is enabled then delete the special review from the
                //award and funding source from the Iacuc protocol
                }else if(code.equals("6")){
                    if(linkAward.equals("1")){
                        AwardTxnBean awardTxnBean = new AwardTxnBean();
                        AwardSpecialReviewBean awardSpReviewBean = null;
                        AwardUpdateTxnBean awardUpdateTxnBean = new AwardUpdateTxnBean(userId);
                        Vector specRevCount =  awardTxnBean.getAwardSpecialReview(fundSource);
                        specRevCount = (specRevCount == null)? new CoeusVector() : specRevCount;
                        int size = specRevCount.size();
                        
                        for(int i = 0;i<specRevCount.size();i++){
                            awardSpReviewBean =(AwardSpecialReviewBean)specRevCount.get(i);
                            String specialRevNum = awardSpReviewBean.getProtocolSPRevNumber();
                            specialRevNum = (specialRevNum == null)? "" : specialRevNum;
                            if(specialRevNum.equals(protocolNumber.substring(0, 10))){
                                awardSpReviewBean.setAcType("D");
                                break;
                            } else
                                awardSpReviewBean = null;
                        }
                        
                        if(awardSpReviewBean==null){
                            ProtocolDataTxnBean protocolDataTxnBean = new ProtocolDataTxnBean(userId);
                            Vector vecProtocolLinks = protocolDataTxnBean.getProtocolLinksData(protocolNumber);
                            if(vecProtocolLinks!=null && !vecProtocolLinks.isEmpty()){
                                String fundingSrcToDelete = fundSource;
                                for(int i=0;i<vecProtocolLinks.size();i++){
                                    ProtocolLinkBean protocolLinkBean = (ProtocolLinkBean)vecProtocolLinks.elementAt(i);
                                    if(protocolLinkBean.getModuleItemKey().equals(fundingSrcToDelete)
                                    && "I".equals(protocolLinkBean.getActionType())){
                                        awardSpReviewBean = new AwardSpecialReviewBean();
                                        awardSpReviewBean.setMitAwardNumber(fundingSrcToDelete);
                                        awardSpReviewBean.setSequenceNumber(protocolLinkBean.getModuleItemSeqNumber());
                                        break;
                                    }
                                }
                            }
                        }
                        //lockedBeans.add(awardSpReviewBean);
                        
                        if(awardSpReviewBean != null && protocolNumber != null && protocolNumber.length() == 10 && awardSpReviewBean.getAcType()!=null) {
                            procedures.add(awardUpdateTxnBean.addUpdAwardSpecialReview(awardSpReviewBean));
                            
                            size--;
                            //After deleting the funding source update the funding source indicator as N1
                            if(size==0)
                                indicator = "N1";
                            
                            protocolUpdateTxnBean.updateFundingSourceIndicator(fundSource,indicator,"1");
                            ProtocolDataTxnBean protocolDataTxnBean = new ProtocolDataTxnBean(userId);
                            sequenceNum = Integer.parseInt(seqNum);
                            //Need to send the notification for the deletion of special review as special review deleted
                            try {
//                                protocolDataTxnBean.sendEmailNotification(ModuleConstants.IACUC_MODULE_CODE,MailActions.SPECIAL_REVIEW_DELETED,
//                                        protocolNumber,sequenceNum,fundSource,ModuleConstants.AWARD_MODULE_CODE);
                            } catch(Exception e) {
                            }
                            procedures.add(updateSpecialReviewLink(protocolNumber,sequenceNum,awardSpReviewBean,userId));
                            updateSpecialReviewNotePad(acType,fundSourceDesc,fundSource,code,userId);
                            Vector inboxData = protocolUpdateTxnBean.updateInboxTable(fundSource,ModuleConstants.AWARD_MODULE_CODE,acType);
                            if(inboxData != null)
                                procedures.addAll(inboxData);
                        } else if(awardSpReviewBean != null){
                            procedures.add(updateSpecialReviewLink(protocolNumber,sequenceNum,awardSpReviewBean,userId));
                        }
                    }
                }
                //execute al the procdures
                if(procedures.size() > 0){
                    if(dbEngine!=null){
                        dbEngine.executeStoreProcs(procedures);
                    }else{
                        throw new CoeusException("db_exceptionCode.1000");
                    }
                }                
                //Added for COEUSQA-3119- Need to implement IACUC link to Award, IP, Prop Dev, and IRB - end
                
                // webTxnBean.getResults(request,UPDATE_PROTOCOL_INDICATOR, hmpIndMap);
                /*Hashtable htupdIndicator=(Hashtable)*/
                // Update Save Status for the Menu check
                // Hashtable updateSaveStatus=(Hashtable)webTxnBean.getResults(request, UPDATE_MENU_CHECKlIST, hmpsaveStatus);
                // Update the menu statzus to the session.
                //updateSaveStatusToSession(session,isMenuSaved);
            }
        }
        
        resetDynaForm(dynaValidatorForm);
        session.setAttribute("iacucFundingSources",vcfundData);
        return isSuccess;
        
    }
    
    
    
    private HashMap updateIndicators(DynaValidatorForm dynaValidatorForm,
        HttpSession session,String protocolNumber,String seqNum,boolean isAllDeleted) throws Exception{
        Timestamp dbTimestamp = prepareTimeStamp();
        dynaValidatorForm.set(FUNDING_SOURCE_TIMESTAMP,dbTimestamp.toString());
        //start--3
        
        Object data = session.getAttribute(CoeusLiteConstants.IACUC_PROTOCOL_INDICATORS);
        //end--3
        HashMap hmIndicatorMap = (HashMap)data;
        WebUtilities utils =  new WebUtilities();
        //start--4
        
        HashMap processedIndicator= (HashMap)utils.processIndicator(hmIndicatorMap, CoeusLiteConstants.FUNDING_SOURCE_INDICATOR, isAllDeleted);
        String processFundingIndicator = (String)processedIndicator.get(CoeusLiteConstants.FUNDING_SOURCE_INDICATOR);
        session.setAttribute(CoeusLiteConstants.IACUC_PROTOCOL_INDICATORS, processedIndicator);
        //end--4
        
        UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute(USER+session.getId());
        String userId = userInfoBean.getUserId();
        HashMap hmpIndMap=new HashMap();

        //start--5
        hmpIndMap.put(CoeusLiteConstants.FIELD,CoeusLiteConstants.FUNDING_SOURCE_INDICATOR_VALUE);
        hmpIndMap.put(CoeusLiteConstants.IACUC_PROTOCOL_NUMBER,protocolNumber);
        hmpIndMap.put(CoeusLiteConstants.IACUC_SEQUENCE_NUMBER,seqNum);
        hmpIndMap.put(CoeusLiteConstants.INDICATOR,processFundingIndicator);
        hmpIndMap.put(CoeusLiteConstants.KEY_STUDY_TIMESTAMP,dbTimestamp.toString());
        hmpIndMap.put(CoeusLiteConstants.USER,userId);
        //end--5
        
        return hmpIndMap;
    }
    
    /*This method returns a HashMap which is used to update the Save Status */
    private HashMap updateSaveStatus(HttpSession session,String protocolNumber,boolean isAllDeleted) throws Exception {
        
        Timestamp dbTimestamp = prepareTimeStamp();
        UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute(USER+session.getId());
        String userId = userInfoBean.getUserId();
        HashMap hmpSaveMap=new HashMap();
        
        //start--6
        hmpSaveMap.put(CoeusLiteConstants.FIELD,FUNDING_SOURCE_FEILD);
        hmpSaveMap.put(CoeusLiteConstants.IACUC_PROTOCOL_NUMBER,protocolNumber);
        if (isAllDeleted) {
            hmpSaveMap.put(CoeusLiteConstants.AV_SAVED,"Y");
        } else {
            hmpSaveMap.put(CoeusLiteConstants.AV_SAVED,"N");
        }
        hmpSaveMap.put(CoeusLiteConstants.KEY_STUDY_TIMESTAMP,dbTimestamp.toString());
        hmpSaveMap.put(CoeusLiteConstants.USER,userId);
        hmpSaveMap.put(CoeusLiteConstants.AC_TYPE,AC_TYPE_UPDATE);
        //end--6
        
        return hmpSaveMap;
    }
    
    /* This method maintains the status of the page 
     * whether it is saved or not in a session
     *  return a boolean value 
     */
    
    private void  updateSaveStatusToSession(HttpSession session, boolean isMenuSaved) {
        Vector vecMenuItems = (Vector)session.getAttribute(MENU_ITEMS);
        Vector modifiedVector = new Vector();
        for (int index=0; index<vecMenuItems.size();index++) {
            MenuBean meanuBean = (MenuBean)vecMenuItems.get(index);
            String menuId = meanuBean.getMenuId();
            if (menuId.equals(SAVE_STATUS)) {
                if (isMenuSaved) {
                    meanuBean.setDataSaved(true);
                } else {
                    meanuBean.setDataSaved(false);
                }
            }
            modifiedVector.add(meanuBean);
        }
        session.setAttribute(MENU_ITEMS, modifiedVector);
    }
    
    /* This method is used to check whether duplicate data is saved
     */
//Modified for COEUSDEV-326 : Lite - Protocol Funding source - application should validate the sponsor code entered by the user  - Start
//    public boolean checkFundingSource(Vector data,HttpServletRequest request,String fundSource){
    public boolean checkFundingSource(Vector data,HttpServletRequest request,String fundSource,String code){//COEUSDEV-326 : End
       boolean isPresent = true;
        for(int index = 0; index <data.size();index++){
            DynaValidatorForm form = (DynaValidatorForm)data.get(index);
            String fundingSource=(String)form.get(FUNDING_SOURCE);
            //Modified for COEUSDEV-326 : Lite - Protocol Funding source - application should validate the sponsor code entered by the user  - Start
            String fundingCode = (String)form.get("code");
//            if(fundSource.trim().equals(fundingSource)){
            if(fundSource.trim().equals(fundingSource) && code.equals(fundingCode)){//COEUSDEV-326 : End
                isPresent = false;
                ActionMessages messages = new ActionMessages();
                messages.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage(ERROR_PROTO_FUNDING_SOURCE));
                saveMessages(request, messages);
            }
        }
       return isPresent;
    }
    
    public void resetDynaForm(DynaValidatorForm dynaValidatorForm)throws Exception{
        dynaValidatorForm.set("code" ,EMPTY_STRING);
        dynaValidatorForm.set("fundingSource" ,EMPTY_STRING);
        dynaValidatorForm.set("fundingSourceName",EMPTY_STRING);
    }
    
    
    
    
    /**
     * Added for validations of all searches
     * This method checks whether the user has entered the Number/Code field correctly, viz
     * Sponsor Number, Unit Number, Development proposal number
     * Institute proposal number or Award Number
     */
    
    private boolean checkFundingSourceNumber(DynaValidatorForm dynaValidatorForm, HttpServletRequest request) throws Exception{
        
        boolean valid = true;
        ActionMessages actionMessages = new ActionMessages();
        Map hmNumber  = new HashMap();
        String count;
        String strNumber;
        WebTxnBean webTxnBean = new WebTxnBean();
        if(dynaValidatorForm.get("code").equals("6")){
            strNumber = (String)dynaValidatorForm.get(FUNDING_SOURCE);
            //Execute the stored function fn_is_valid_award_num to get the count
            hmNumber.put("mitAwardNumber", strNumber);
            hmNumber =(Hashtable)webTxnBean.getResults(request,"checkMitAwardNumber",hmNumber);
            hmNumber = (HashMap)hmNumber.get("checkMitAwardNumber");
            count = (String)hmNumber.get("ll_count");
            if(Integer.parseInt(count) < 1){
                //Modified for COEUSDEV-326 : Lite - Protocol Funding source - application should validate the sponsor code entered by the user - Start
//            	actionMessages.add("fundingSrc.error.invalidAwardNo",new ActionMessage("fundingSrc.error.invalidAwardNo"));
                actionMessages.add("fundingSrc.error.invalidAwardNo",new ActionMessage("fundingSrc.validationError.invalidAwardNo"));
                //COEUSDEV-326 : End
                saveMessages(request, actionMessages);
                valid = false;
            }
        }
        
        if(dynaValidatorForm.get("code").equals("5")){
            strNumber = (String)dynaValidatorForm.get(FUNDING_SOURCE);
            //Execute the stored function fn_is_valid_inst_prop_num to get the count
            hmNumber.put("proposalNumber", strNumber);
            hmNumber =(Hashtable)webTxnBean.getResults(request,"checkInstPropNumber",hmNumber);
            hmNumber = (HashMap)hmNumber.get("checkInstPropNumber");
            count = (String)hmNumber.get("ll_count");
            if(Integer.parseInt(count) < 1){
                //Modified for COEUSDEV-326 : Lite - Protocol Funding source - application should validate the sponsor code entered by the user - Start
//            	actionMessages.add("fundingSrc.error.invalidInstNo",new ActionMessage("fundingSrc.error.invalidInstNo"));
                actionMessages.add("fundingSrc.error.invalidInstNo",new ActionMessage("fundingSrc.validationError.invalidInstNo"));
                //COEUSDEV-326 : End
                saveMessages(request, actionMessages);
                valid = false;
            }
       }       
        
       if(dynaValidatorForm.get("code").equals("4")){
            strNumber = (String)dynaValidatorForm.get(FUNDING_SOURCE);
            //Execute the stored function FN_IS_VALID_DEV_PROP_NUM to get the count
            hmNumber.put("proposalNumber", strNumber);
            hmNumber =(Hashtable)webTxnBean.getResults(request,"checkDevPropNumber",hmNumber);
            hmNumber = (HashMap)hmNumber.get("checkDevPropNumber");
            count = (String)hmNumber.get("ll_count");
            if(Integer.parseInt(count) < 1){
                //Modified for COEUSDEV-326 : Lite - Protocol Funding source - application should validate the sponsor code entered by the user - Start
//                actionMessages.add("fundingSrc.error.invalidDevNo",new ActionMessage("fundingSrc.error.invalidDevNo"));
                actionMessages.add("fundingSrc.error.invalidDevNo",new ActionMessage("fundingSrc.validationError.invalidDevNo"));
                //COEUSDEV-326 : End
                saveMessages(request, actionMessages);
                valid = false;
            }
       }       
        
       if(dynaValidatorForm.get("code").equals("1")){
            strNumber = (String)dynaValidatorForm.get(FUNDING_SOURCE);
            //Execute the stored function Get_Valid_Sponsor_Code to get the count
            hmNumber.put("sponsorCode", strNumber);
            hmNumber =(Hashtable)webTxnBean.getResults(request,"isValidSponsorCode",hmNumber);
            hmNumber = (HashMap)hmNumber.get("isValidSponsorCode");
            count = (String)hmNumber.get("isValid");
            //Modified for COEUSDEV-326 : Lite - Protocol Funding source - application should validate the sponsor code entered by the user - Start
            //Checked for sponsor code from DB is empty, if empty or null then through the invalid sponsor code validation message
//            if(count == null){
//            	actionMessages.add("fundingSrc.error.invalidSponsorNo",new ActionMessage("fundingSrc.error.invalidSponsorNo"));
//                saveMessages(request, actionMessages);
//                valid = false;
//              }
            if(count == null || EMPTY_STRING.equals(count.trim()) ){
            	actionMessages.add("fundingSrc.error.invalidSponsorNo",new ActionMessage("fundingSrc.validationError.invalidSponsorNo"));
                //COEUSDEV-326 : End
                saveMessages(request, actionMessages);
                valid = false;
            }else{
                //If the sponsor code is valid again set it to form,
                //when user enter sapce before the sponsor code or after it will be trimed in the DB
                dynaValidatorForm.set(FUNDING_SOURCE,count);
            }
            //COEUSDEV-326 : End
       }  
        
       if(dynaValidatorForm.get("code").equals("2")){
            strNumber = (String)dynaValidatorForm.get(FUNDING_SOURCE);
            //Execute the stored function Fn_Get_Unit_Name to get the count
            hmNumber.put("ownedUnit", strNumber);
            hmNumber =(Hashtable)webTxnBean.getResults(request,"getUnitDesc",hmNumber);
            hmNumber = (HashMap)hmNumber.get("getUnitDesc");
            count = (String)hmNumber.get("RetVal");
            if(count == null){
                //Modified for COEUSDEV-326 : Lite - Protocol Funding source - application should validate the sponsor code entered by the user - Start
//                actionMessages.add("fundingSrc.error.invalidUnitNo",new ActionMessage("fundingSrc.error.invalidUnitNo"));
                actionMessages.add("fundingSrc.error.invalidUnitNo",new ActionMessage("fundingSrc.validationError.invalidUnitNo"));
                //COEUSDEV-326 : END
                saveMessages(request, actionMessages);
                valid = false;
            }
        }          
        
      
        return valid;
        
    }
    
    /**
     * This method locks a particular protcol 
     * @throws Exception
     */
//    private void lockProtocol(String protocolNumber, HttpServletRequest request) throws Exception {
//        HttpSession session = request.getSession();
//        UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute("user"+session.getId());
//        LockBean lockBean = getLockingBean(userInfoBean, protocolNumber,request);
//        lockBean.setMode("M");
//        lockModule(lockBean,request);
//        session.setAttribute(CoeusLiteConstants.RECORD_LOCKED+session.getId(),new Boolean(true));       
//    }
    
    
    //Added for COEUSQA-3119- Need to implement IACUC link to Award, IP, Prop Dev, and IRB - start
     public ProcReqParameter updateSpecialReviewLink(String protocolNumber,int sequenceNum,SpecialReviewFormBean specialReviewBean,String userId) throws CoeusException,DBException{
        ProtocolLinkBean protocolLinkBean = new ProtocolLinkBean();
        protocolLinkBean.setProtocolNumber(protocolNumber);
        protocolLinkBean.setSequenceNumber(sequenceNum);
        
        if(specialReviewBean instanceof InstituteProposalSpecialReviewBean) {
            protocolLinkBean.setModuleCode(2);
            protocolLinkBean.setModuleItemKey(((InstituteProposalSpecialReviewBean)specialReviewBean).getProposalNumber());
        } else if(specialReviewBean instanceof AwardSpecialReviewBean) {
            protocolLinkBean.setModuleCode(1);
            protocolLinkBean.setModuleItemKey(((AwardSpecialReviewBean)specialReviewBean).getMitAwardNumber());
            
        }
        
        protocolLinkBean.setModuleItemSeqNumber(specialReviewBean.getSequenceNumber());
        if(specialReviewBean.getAcType() != null && specialReviewBean.getAcType().equals("I")){
            CoeusMessageResourcesBean coeusMessageResourcesBean =new CoeusMessageResourcesBean();
            String insertComments = coeusMessageResourcesBean.parseMessageKey("notepadInsert_IACUCexceptionCode.6000");
            protocolLinkBean.setComments(insertComments);
            protocolLinkBean.setActionType("I");
        }else{
            CoeusMessageResourcesBean coeusMessageResourcesBean = new CoeusMessageResourcesBean();
            String deleteComments = coeusMessageResourcesBean.parseMessageKey("notepadDelete_IACUCexceptionCode.6001");
            protocolLinkBean.setComments(deleteComments);
            protocolLinkBean.setActionType("D");
        }
        ProtocolUpdateTxnBean protocolUpdateTxnBean = new ProtocolUpdateTxnBean(userId);
        return protocolUpdateTxnBean.addUpdProtocolLinks(protocolLinkBean);      
    }
     
     
     //
     public LockingBean unLockBeans(String userId) throws CoeusException,DBException{
        
        LockingBean lockingBean = null;
        
        if(lockedBeans != null && lockedBeans.size()>0) {
            for(int index=0;index<lockedBeans.size();index++) {
                Object bean = lockedBeans.get(index);
                if(bean instanceof InstituteProposalSpecialReviewBean){
                    InstituteProposalSpecialReviewBean instituteProposalSpecialReviewBean = (InstituteProposalSpecialReviewBean)bean;
                    lockingBean = transMon.releaseLock("osp$Proposal_"+instituteProposalSpecialReviewBean.getProposalNumber(),userId);                   
                } else if(bean instanceof AwardSpecialReviewBean){
                    AwardSpecialReviewBean awardSpecialReviewBean = (AwardSpecialReviewBean)bean;
                    AwardTxnBean awTxnBean = new AwardTxnBean();
                    String rootAwardNumber = awTxnBean.getRootAward(awardSpecialReviewBean.getMitAwardNumber());
                    lockingBean = transMon.releaseLock("osp$Award_"+rootAwardNumber,userId);
                }
            }
        }
        return lockingBean;
    }
     
     public void  updateSpecialReviewNotePad(String acType,String fundSourceDesc,String fundSource,String code,String userId) throws CoeusException,DBException{
         
         NotepadTxnBean notepadTxnBean = new NotepadTxnBean();
         NotepadBean notepadBean = new NotepadBean();         
         Timestamp dbTimestamp;
         try {
             dbTimestamp = prepareTimeStamp();
             if(acType.equals("I"))
                 notepadBean.setComments(new CoeusMessageResourcesBean().parseMessageKey("comment_exceptionCode_IACUCSpecialReview.1000")+fundSourceDesc);
             else if(acType.equals("D"))
                 notepadBean.setComments(new CoeusMessageResourcesBean().parseMessageKey("comment_exceptionCode_IACUCSpecialReview.1001")+fundSourceDesc);
             int maxEntryNumber = notepadTxnBean.getMaxProposalNotesEntryNumber(fundSource);
             maxEntryNumber = maxEntryNumber + 1;
             notepadBean.setProposalAwardNumber(fundSource);
             notepadBean.setNotePadType(new Integer(code).toString());
             notepadBean.setEntryNumber(maxEntryNumber);
             notepadBean.setRestrictedView(false);
             notepadBean.setAcType("I");
             notepadBean.setUpdateTimestamp(dbTimestamp);
             notepadBean.setUpdateUser(userId);
             notepadBean.setUserName(userId);
             notepadBeans.add(notepadBean);
         } catch (Exception ex) {
             ex.printStackTrace();
         }
         
     }
    //Added for COEUSQA-3119- Need to implement IACUC link to Award, IP, Prop Dev, and IRB - end
    
}
        
        
        
        

    
   
