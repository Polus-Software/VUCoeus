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

package edu.mit.coeuslite.irb.action;

import edu.mit.coeus.award.bean.AwardSpecialReviewBean;
import edu.mit.coeus.award.bean.AwardTxnBean;
import edu.mit.coeus.bean.CoeusMessageResourcesBean;
import edu.mit.coeus.bean.SpecialReviewFormBean;
import edu.mit.coeus.bean.UserInfoBean;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.instprop.bean.InstituteProposalSpecialReviewBean;
import edu.mit.coeus.irb.bean.ProtocolFundingSourceBean;
import edu.mit.coeus.irb.bean.ProtocolLinkBean;
import edu.mit.coeus.irb.bean.ProtocolUpdateTxnBean;
import edu.mit.coeus.propdev.bean.NotepadBean;
import edu.mit.coeus.propdev.bean.NotepadTxnBean;
import edu.mit.coeus.user.bean.UserMaintDataTxnBean;
import edu.mit.coeus.utils.CoeusVector;
//import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.utils.dbengine.DBEngineImpl;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.utils.dbengine.ProcReqParameter;
import edu.mit.coeus.utils.dbengine.TransactionMonitor;
import edu.mit.coeus.utils.locking.LockingBean;
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
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.validator.DynaValidatorForm;

/**
 *
 *
 */
public class FundingSourcesAction extends ProtocolBaseAction {
//    private ActionForward actionForward;
//    private Timestamp dbTimestamp;   
//    private WebTxnBean webTxnBean ;    
    public static final String EMPTY_STRING = "";
    private static final String SAVE_STATUS = "005";
    private static final String MENU_ITEMS ="menuItemsVector";
    private static final String PROTOCOL_NUMBER="protocolNumber";
    private static final String SEQUENCE_NUMBER="sequenceNumber"; 
    private static final String AC_TYPE="acType";
    private static final String FUNDING_SOURCE_FORM="fundingSourceFrm";
    private static final String GET_PROTOCOL_FUNDING_SOURCES="getProtocolFundingSources";
    private static final String UPDATE_PROTOCOL_INDICATOR="updateProtocolIndicator";
    private static final String UPDATE_PROTOCOL_FUNDING_SOURCE="addUpdProtocolFundSource";
    private static final String UPDATE_MENU_CHECKlIST="updateMenuCheckList";
    private static final String FUNDING_SOURCE_FEILD ="FUNDING_SOURCE";
    private static final String AC_TYPE_INSERT="I";
    private static final String AC_TYPE_DELETE="D";
    private static final String AC_TYPE_UPDATE="U";
    private static final String FUNDING_SOURCE_TIMESTAMP="fundingSourceTimeStamp";
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
        
        //start--2
        
        protocolNumber=(String)session.getAttribute(CoeusLiteConstants.PROTOCOL_NUMBER+session.getId());
        seqNum=(String)session.getAttribute(CoeusLiteConstants.SEQUENCE_NUMBER+session.getId());
        //end--2
        
         if(seqNum!= null){
            sequenceNumber = Integer.parseInt(seqNum);
        }
        hmpfundingSource.put(PROTOCOL_NUMBER,protocolNumber);
        hmpfundingSource.put(SEQUENCE_NUMBER,new Integer(sequenceNumber));        
        dynaValidatorForm.set(PROTOCOL_NUMBER,protocolNumber);
        dynaValidatorForm.set(SEQUENCE_NUMBER,seqNum);               
        String acType = (String)dynaValidatorForm.get(AC_TYPE);
        UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute(USER+session.getId());        
        String unitNumber=userInfoBean.getUnitNumber();
        String loggedinUser=userInfoBean.getUserId(); 
        String userName=userInfoBean.getUserName();  
        
        /* Checks if the user has lock
         * if the user doesnt have lock he should not save funding source
         */
        LockBean lockBean = getLockingBean(userInfoBean, (String)session.getAttribute(CoeusLiteConstants.PROTOCOL_NUMBER+session.getId()), request);
        boolean isLockExists = isLockExists(lockBean, lockBean.getModuleKey());
        LockBean lockData = getLockedData(CoeusLiteConstants.PROTOCOL_LOCK_STR+lockBean.getModuleNumber(), request);

         //modified for editing funding source in display mode - start
        String functionType =  (String)session.getAttribute(CoeusLiteConstants.MODE_DETAILS+session.getId());          
        Boolean fundingSourceMode = (Boolean) session.getAttribute("MODIFY_PROTOCOL_FUNDING_SOURCE"+session.getId());  
        
        if(functionType!=null && !functionType.equals(EMPTY_STRING)){           
            if(functionType.equalsIgnoreCase("D") && fundingSourceMode.booleanValue()){
                if(isLockExists){
                    lockProtocol(protocolNumber, request);
                    if(!acType.equals(EMPTY_STRING)){ 
                       if(acType.equals(AC_TYPE_INSERT)){
                           boolean isSuccess = false;
                           isSuccess=saveFundingSource(session, request, dynaValidatorForm, hmpfundingSource, protocolNumber, seqNum);
                           if(isSuccess){
                                releaseLock(lockBean, request);
                                dynaValidatorForm.reset(mapping,request);
                                return mapping.findForward(SUCCESS);
                           }                          
                       }
                       else if(dynaValidatorForm.get(AC_TYPE).equals(AC_TYPE_DELETE)){
                          boolean isSuccess=false;
                          isSuccess=deleteFundingSource(session, request, dynaValidatorForm, hmpfundingSource, protocolNumber, seqNum);
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
                                dynaValidatorForm.reset(mapping,request);
                                return mapping.findForward(SUCCESS);
                           }                          
                       }
                       else if(dynaValidatorForm.get(AC_TYPE).equals(AC_TYPE_DELETE)){
                          boolean isSuccess=false;
                          isSuccess=deleteFundingSource(session, request, dynaValidatorForm, hmpfundingSource, protocolNumber, seqNum);
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
        return mapping.findForward(SUCCESS);
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
                //Added for COEUSQA-3119- Need to implement IACUC link to Award, IP, Prop Dev, and IRB - start
//                Vector procedures = new Vector(5,3);
                dbEngine = new DBEngineImpl();
                transMon = TransactionMonitor.getInstance();
                //Added for COEUSQA-3119- Need to implement IACUC link to Award, IP, Prop Dev, and IRB - end
                
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
                           session.setAttribute("fundingSources",htfundingSourceData.get(GET_PROTOCOL_FUNDING_SOURCES));
                           isSuccess=true;
                           return isSuccess;
                       }
                    }// check for the dupliucate Persons - End

                    //Save the Funding source details                
                    webTxnBean.getResults(request,UPDATE_PROTOCOL_FUNDING_SOURCE, dynaValidatorForm);
                    webTxnBean.getResults(request,UPDATE_PROTOCOL_INDICATOR, hmpIndMap);
                    Hashtable htFundingSourceTypes = (Hashtable)webTxnBean.getResults(request,FUNDING_SOURCE_FORM,hmpfundingSource);
                    //Update Save Status for the Menu check
                    //Hashtable updateSaveStatus=(Hashtable)webTxnBean.getResults(request, UPDATE_MENU_CHECKlIST, hmpsaveStatus);
                    //Update the Menu status to the session.
                    //updateSaveStatusToSession(session,isMenuSaved); 
//                    resetDynaForm(dynaValidatorForm);
                    session.setAttribute("fundingSources",htFundingSourceTypes.get(GET_PROTOCOL_FUNDING_SOURCES));
                    isSuccess = true;
                    //Added for COEUSQA-3119- Need to implement IACUC link to Award, IP, Prop Dev, and IRB - start
                    //If funding source is of type Institute Proposal and ENABLE_PROTOCOL_TO_PROPOSAL_LINK is enabled then
                    //protocol linking should be eastablished between protocol and Institue Proposal
                    UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute(USER+session.getId());
                    String userId = userInfoBean.getUserId();
                    ProtocolUpdateTxnBean protocolUpdateTxnBean = new ProtocolUpdateTxnBean(userId,userInfoBean.getUnitNumber());
                    ProtocolFundingSourceBean fundingSourceBean = new ProtocolFundingSourceBean();
                    int sequenceNumber = 0;
                    if(seqNum != null){
                        sequenceNumber = Integer.parseInt(seqNum);
                    }
                    lockedBeans = new Vector();
                    Vector procedures =  protocolUpdateTxnBean.updateFundingSourceLink(
                            getFundingSourceBeanFromForm(dynaValidatorForm),protocolNumber,sequenceNumber,null, new Vector(),lockedBeans);
                    resetDynaForm(dynaValidatorForm);
                    // Modified for COEUSQA-3730 : Coeus4.5: IRB Error Adding Funding Source - Start
//                    if(dbEngine!=null){
//                        dbEngine.executeStoreProcs(procedures);
//                    }else{
//                        throw new CoeusException("db_exceptionCode.1000");
//                    }
                    if(procedures.size() > 0){
                        if(dbEngine!=null){
                            dbEngine.executeStoreProcs(procedures);
                        }else{
                            throw new CoeusException("db_exceptionCode.1000");
                        }
                    }
                    // Modified for COEUSQA-3730 : Coeus4.5: IRB Error Adding Funding Source - End
                    unLockBeans(userId);    
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
              WebTxnBean webTxnBean = new WebTxnBean();
              //Added for COEUSQA-3119- Need to implement IACUC link to Award, IP, Prop Dev, and IRB - start
//              Vector procedures = new Vector(5,3);
              dbEngine = new DBEngineImpl();
              transMon = TransactionMonitor.getInstance();
              //Added for COEUSQA-3119- Need to implement IACUC link to Award, IP, Prop Dev, and IRB - end
              
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
                      webTxnBean.getResults(request,UPDATE_PROTOCOL_FUNDING_SOURCE, dynaForm);
                      vcfundData.remove(index);
                      if(vcfundData== null || vcfundData.size()==0){
                          isAllDeleted = true;
                          isMenuSaved = false;
                          hmpIndMap = updateIndicators(dynaValidatorForm,session,protocolNumber,seqNum,isAllDeleted);
                          webTxnBean.getResults(request, UPDATE_PROTOCOL_INDICATOR, hmpIndMap);
                          hmpsaveStatus = updateSaveStatus(session,protocolNumber,isMenuSaved);
                      }else{
                          isAllDeleted = false;
                          isMenuSaved = true;
                          hmpIndMap = updateIndicators(dynaValidatorForm,session,protocolNumber,seqNum,isAllDeleted);
                          webTxnBean.getResults(request, UPDATE_PROTOCOL_INDICATOR, hmpIndMap);
                          // update menu status
                          hmpsaveStatus = updateSaveStatus(session,protocolNumber,isMenuSaved);                          
                      }
                      /*Hashtable htupdIndicator=(Hashtable)*/webTxnBean.getResults(request,UPDATE_PROTOCOL_INDICATOR, hmpIndMap);
                      // Update Save Status for the Menu check
                      // Hashtable updateSaveStatus=(Hashtable)webTxnBean.getResults(request, UPDATE_MENU_CHECKlIST, hmpsaveStatus);
                      // Update the menu statzus to the session.
                      //updateSaveStatusToSession(session,isMenuSaved);
                      //Added for COEUSQA-3119- Need to implement IACUC link to Award, IP, Prop Dev, and IRB - start
                      //If special review type code is 5 and ENABLE_PROTOCOL_TO_PROPOSAL_LINK is enabled then delete the special review from the
                      //institute proposal and funding source from the Protocol
                      UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute(USER+session.getId());
                      String userId = userInfoBean.getUserId();
                      ProtocolUpdateTxnBean protocolUpdateTxnBean = new ProtocolUpdateTxnBean(userId,userInfoBean.getUnitNumber());
                      ProtocolFundingSourceBean fundingSourceBean = new ProtocolFundingSourceBean();
                      int sequenceNumber = 0;
                      if(seqNum != null){
                          sequenceNumber = Integer.parseInt(seqNum);
                      }
                        lockedBeans = new Vector();
                      Vector procedures =  protocolUpdateTxnBean.updateFundingSourceLink(
                              getFundingSourceBeanFromForm(dynaForm),protocolNumber,sequenceNumber,null, new Vector(),lockedBeans);
                      //execute al the procdures
                      if(dbEngine!=null){
                          if(procedures != null && procedures.size() > 0 ) {
                            dbEngine.executeStoreProcs(procedures);
                          }
                      }else{
                          throw new CoeusException("db_exceptionCode.1000");
                      }
                       unLockBeans(userId);          
                      //Added for COEUSQA-3119- Need to implement IACUC link to Award, IP, Prop Dev, and IRB - end                      
                  }
              }
              HashMap hmpProposalData = new HashMap();
                
                hmpProposalData.put("proposalNumber",fundingSourceNumber);
                UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute("user"+session.getId());
                hmpProposalData.put("updateUser", userInfoBean.getUserId());
         
//              if(fundingSourceNumber.length() >=7) {
//                deleteProposal(hmpProposalData, request);
//              }
                
              resetDynaForm(dynaValidatorForm);
              session.setAttribute("fundingSources",vcfundData);             
              return isSuccess;
                                          
    }
    
    
    
    private HashMap updateIndicators(DynaValidatorForm dynaValidatorForm,
        HttpSession session,String protocolNumber,String seqNum,boolean isAllDeleted) throws Exception{
        Timestamp dbTimestamp = prepareTimeStamp();
        dynaValidatorForm.set(FUNDING_SOURCE_TIMESTAMP,dbTimestamp.toString());
        //start--3
        
        Object data = session.getAttribute(CoeusLiteConstants.PROTOCOL_INDICATORS);
        //end--3
        HashMap hmIndicatorMap = (HashMap)data;
        WebUtilities utils =  new WebUtilities();
        //start--4
        
        HashMap processedIndicator= (HashMap)utils.processIndicator(hmIndicatorMap, CoeusLiteConstants.FUNDING_SOURCE_INDICATOR, isAllDeleted);
        String processFundingIndicator = (String)processedIndicator.get(CoeusLiteConstants.FUNDING_SOURCE_INDICATOR);
        session.setAttribute(CoeusLiteConstants.PROTOCOL_INDICATORS, processedIndicator);
        //end--4
        
        UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute(USER+session.getId());
        String userId = userInfoBean.getUserId();
        HashMap hmpIndMap=new HashMap();

        //start--5
        hmpIndMap.put(CoeusLiteConstants.FIELD,CoeusLiteConstants.FUNDING_SOURCE_INDICATOR_VALUE);
        hmpIndMap.put(CoeusLiteConstants.PROTOCOL_NUMBER,protocolNumber);
        hmpIndMap.put(CoeusLiteConstants.SEQUENCE_NUMBER,seqNum);
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
        hmpSaveMap.put(CoeusLiteConstants.PROTOCOL_NUMBER,protocolNumber);
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

     private void deleteProposal(HashMap hmpProposalData, HttpServletRequest request)throws Exception{

        String navigator = EMPTY_STRING;
        WebTxnBean webTxnBean = null;
        String proposalNumber = (String)hmpProposalData.get("proposalNumber");
        UserInfoBean userInfoBean = (UserInfoBean)request.getSession().getAttribute("user"+request.getSession().getId());
        webTxnBean = new WebTxnBean();

        HashMap hmValidProposal = new HashMap();
        hmValidProposal.put("proposalNumber",proposalNumber);
        Hashtable htValidProposal = (Hashtable)webTxnBean.getResults(request, "checkProposalNumber", hmValidProposal);
        HashMap hmValid = (HashMap)htValidProposal.get("checkProposalNumber");
        int validProposal = Integer.parseInt(hmValid.get("ll_count").toString());

        if(validProposal == 1){
            // If  Proposal exists, Check lock exists or not
            LockBean lockBean = getLockingBean(userInfoBean, (String)request.getSession().getAttribute(
                    CoeusLiteConstants.PROPOSAL_NUMBER+request.getSession().getId()), request);
 //           boolean alreadyLocked = isLockExists(lockBean, lockBean.getModuleKey());
  //          LockBean lockData = getLockedData(CoeusLiteConstants.PROP_DEV_LOCK_STR+lockBean.getModuleNumber(),request);
  //          if(!alreadyLocked && lockBean.getSessionId().equals(lockData.getSessionId())) {
                // lock exists , now delete the proposal
                webTxnBean = new WebTxnBean();
                Hashtable htDeleteProposal = (Hashtable)webTxnBean.getResults(request, "deleteProposal", hmpProposalData);
                HashMap hm = (HashMap)htDeleteProposal.get("deleteProposal");
                int deleteSuccess = Integer.parseInt(hm.get("deleteSuccessful").toString());

                if(deleteSuccess == 0){
                    // If the proposal is deleted successfully
                    releaseLock(lockBean, request);
                    navigator = "success";
                } else{
                    // If not able to Deletion is not success
                    navigator = "error";
                }
 //           }
        }
    }
    
      //Added for COEUSQA-3119- Need to implement IACUC link to Award, IP, Prop Dev, and IRB - start
     /**
      * If link is existing between Award and protocol or Institute proposal and protocol then it must insert comment 
      * as Human Subject special Review inserted or deleted.
      * @param protocolNumber
      * @param sequenceNum
      * @param userId
      * @returns protocolLinkBean
      **/
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
     
     
     /**
      * To release the lock for the protocol
      * @param userID
      * @returns lockingBean
      */
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
     
     /**
      * To update the notepad for the linking 
      * It adds the comment as special review inserted
      * @param fundSourceDesc to set the comment in notepad
      */
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
    
     private ProtocolFundingSourceBean getFundingSourceBeanFromForm(DynaValidatorForm validatorForm){
         ProtocolFundingSourceBean fundingSourceBean = new ProtocolFundingSourceBean();
         fundingSourceBean.setAcType((String)validatorForm.get(AC_TYPE));
         fundingSourceBean.setFundingSource((String)validatorForm.get("fundingSource"));
         fundingSourceBean.setFundingSourceName((String)validatorForm.get("fundingSourceName"));
         if(validatorForm.get("code") != null && !"".equals((String)validatorForm.get("code"))){
             fundingSourceBean.setFundingSourceTypeCode(Integer.parseInt((String)validatorForm.get("code")));
         }
         fundingSourceBean.setProtocolNumber((String)validatorForm.get(PROTOCOL_NUMBER));
         if(validatorForm.get(SEQUENCE_NUMBER) != null && !"".equals(validatorForm.get(SEQUENCE_NUMBER).toString())){
             fundingSourceBean.setSequenceNumber(Integer.parseInt((String)validatorForm.get(SEQUENCE_NUMBER)));
         }
         if(validatorForm.get(FUNDING_SOURCE_TIMESTAMP) != null && !"".equals(validatorForm.get(FUNDING_SOURCE_TIMESTAMP).toString())){
             fundingSourceBean.setUpdateTimestamp(Timestamp.valueOf(validatorForm.get(FUNDING_SOURCE_TIMESTAMP).toString()));
         }
         fundingSourceBean.setUpdateUser((String)validatorForm.get("fundingSourceUpdateUser"));
         return fundingSourceBean;
     }
     //Added for COEUSQA-3119- Need to implement IACUC link to Award, IP, Prop Dev, and IRB - end  
}
        
        
        
        

    
   
