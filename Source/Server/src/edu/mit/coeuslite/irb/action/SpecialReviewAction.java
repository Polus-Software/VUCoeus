/*
 * SpecialReviewAction.java
 *
 * Created on March 4, 2005, 12:07 PM
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeuslite.irb.action;

import edu.mit.coeus.bean.UserInfoBean;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.validator.DynaValidatorForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import javax.servlet.http.HttpSession;
import java.util.Vector;
//start--1
import edu.mit.coeuslite.utils.CoeusLiteConstants;
//end--1
import edu.mit.coeuslite.utils.bean.MenuBean;
import edu.mit.coeuslite.utils.bean.WebTxnBean;
import edu.mit.coeuslite.utils.DateUtils;
import edu.mit.coeuslite.utils.LockBean;
import edu.mit.coeuslite.utils.WebUtilities;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Hashtable;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.ActionMessage;
import edu.mit.coeus.utils.ModuleConstants;
/**
 *
 * @author  vinayks
 */
public class SpecialReviewAction extends ProtocolBaseAction {
//    private ActionForward actionForward = null;
//    private WebTxnBean txnBean;
//    private Timestamp dbTimestamp;
    public static final String EMPTY_STRING = "";
    private static final String SAVE_MENU = "007";
    private static final String MENU_ITEMS ="menuItemsVector";
    //Removing instance variable case# 2960
    //private DateUtils dateUtils = new edu.mit.coeuslite.utils.DateUtils();
   
    private static final String APPLICATION_DATE="applicationDate";
    private static final String APPROVAL_DATE="approvalDate";
    private static final String SPECIAL_REVIEW_CODE="specialReviewCode";
    private static final String AC_TYPE="acType";
    private static final String AC_TYPE_INSERT="I";
    private static final String AC_TYPE_DELETE="D";
    //Added for Coeus 4.3 Enhancement - starts
    private static final String AC_TYPE_UPDATE="U";
    private static final String AC_TYPE_EDIT="E";
    // Added for Coeus 4.3 Enhancement - ends
    private static final String PROTOCOL_NUMBER="protocolNumber";
    private static final String SEQUENCE_NUMBER="sequenceNumber";
    private static final String GET_PROTOCOL_SPECIAL_REVIEW = "getProtocolSpecialReview";
    private static final String UPDATE_PROTOCOL_INDICATOR="updateProtocolIndicator";
    private static final String UPDATE_TIMESTAMP="updateTimestamp";
    private static final String GET_SPECIAL_REVIEW_CODE="getSpeciaReviewCode";
    private static final String GET_REVIEW_APPROVAL_TYPE="getReviewApprovalType";
    private static final String SPECIAL_REVIEW_NUMBER="specialReviewNumber";
    private static final String GET_SPECIAL_REVIEW_NMUBER = "getNextSpecialReviewNumber";
    private static final String UPDATE_MENU_CHECKlIST = "updateMenuCheckList";
    private static final String UPDATE_SPECIAL_REVIEW = "updProtoSpecialReview";
//    private HttpServletRequest request ;
//    private ActionMessages messages ;
    
    /** Creates a new instance of SpecialReviewAction */
    public SpecialReviewAction() {
    }
    public ActionForward performExecute(ActionMapping mapping, 
        ActionForm form, HttpServletRequest request, 
        HttpServletResponse res) throws Exception{
       
            
        String protocolNumber=EMPTY_STRING;
        String seqNum= EMPTY_STRING;
        int sequenceNumber = -1;
        DynaValidatorForm specialReviewForm = (DynaValidatorForm)form;
        HttpSession session = request.getSession();
        WebTxnBean txnBean = new WebTxnBean();
        //Modified for instance variable case#2960.
        DateUtils dateUtils = new DateUtils();        
        boolean isAllDeleted = false;
        boolean isMenuSaved = false;
        HashMap hmpSpRev = new HashMap();
        String date=specialReviewForm.get("applicationDate").toString();
        date = dateUtils.formatDate(date,":/.,|-","MM/dd/yyyy");
        specialReviewForm.set("applicationDate",date);
        date=specialReviewForm.get("approvalDate").toString();
        date = dateUtils.formatDate(date,":/.,|-","MM/dd/yyyy");
        specialReviewForm.set("approvalDate",date);
        
        ActionMessages messages = new ActionMessages();
        //start--2
        protocolNumber = 
            (String)session.getAttribute(CoeusLiteConstants.PROTOCOL_NUMBER+session.getId());
        seqNum = 
            (String)session.getAttribute(CoeusLiteConstants.SEQUENCE_NUMBER+session.getId());
        //end--2
        
        if(seqNum!= null){
            sequenceNumber = Integer.parseInt(seqNum);
        }
        hmpSpRev.put("protocolNumber",protocolNumber);
        hmpSpRev.put("sequenceNumber",new Integer(sequenceNumber));
        // COEUSQA-2320 Show in Lite for Special Review in Code table
        // Add module code to the map.
        hmpSpRev.put("moduleCode", ModuleConstants.IACUC_MODULE_CODE);
        
        UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute("user"+session.getId());        
        String unitNumber=userInfoBean.getUnitNumber();
        String loggedinUser=userInfoBean.getUserId(); 
        String userName=userInfoBean.getUserName();  
             
        LockBean lockBean = getLockingBean(userInfoBean, (String)session.getAttribute(CoeusLiteConstants.PROTOCOL_NUMBER+session.getId()), request);
        boolean isLockExists = isLockExists(lockBean, lockBean.getModuleKey());
        LockBean lockData = getLockedData(CoeusLiteConstants.PROTOCOL_LOCK_STR+lockBean.getModuleNumber(), request);
        
        //modified for editing special review in display mode : case 4590 - start
        String functionType =  (String)session.getAttribute(CoeusLiteConstants.MODE_DETAILS+session.getId());
        Boolean fundingSourceMode = (Boolean) session.getAttribute("MODIFY_PROTO_SPECIAL_RVW"+session.getId());
        
        if(functionType!=null && !functionType.equals(EMPTY_STRING)){
            if(functionType.equalsIgnoreCase("D") && fundingSourceMode.booleanValue()){
                if(isLockExists){
                    lockProtocol(protocolNumber, request);
                    if(specialReviewForm.get(AC_TYPE)!=null){

                        boolean isSuccess=false;
                        if(specialReviewForm.get(AC_TYPE).toString().equalsIgnoreCase(AC_TYPE_INSERT) ||
                                specialReviewForm.get(AC_TYPE).toString().equalsIgnoreCase(AC_TYPE_UPDATE)){
    
                            boolean isErrorPResent = validateRowData(specialReviewForm, request);
                            if(!isErrorPResent){
                                isSuccess = saveSpecialReview(request,specialReviewForm, hmpSpRev, protocolNumber, seqNum);
                                specialReviewForm = resetFormDatas(specialReviewForm);
                                releaseLock(lockBean, request);
                                return mapping.findForward("success");
                            }
                        }else if(specialReviewForm.get(AC_TYPE).toString().equalsIgnoreCase(AC_TYPE_DELETE)){
                            
                            isSuccess=deleteSpecialReview(request,specialReviewForm, hmpSpRev, protocolNumber, seqNum);
                            specialReviewForm = resetFormDatas(specialReviewForm);
                        } else if(specialReviewForm.get(AC_TYPE).toString().equalsIgnoreCase(AC_TYPE_EDIT)){
                            
                            specialReviewForm = editSpecialReview(request, specialReviewForm, protocolNumber, seqNum);
                            specialReviewForm.set(AC_TYPE, AC_TYPE_UPDATE);  
                        }
                    }
                    releaseLock(lockBean, request);
                }else{
                    String errMsg = "release_lock_for";
                    messages = new ActionMessages();
                    messages.add("errMsg", new ActionMessage(errMsg,lockBean.getModuleKey(),lockBean.getModuleNumber()));
                    saveMessages(session, messages);
                }
            }else{
                
                if(!isLockExists && lockBean.getSessionId().equals(lockData.getSessionId())) {
                    if(specialReviewForm.get(AC_TYPE)!=null){
                        // Modified for Coeus 4.3 Enhancement
                        if(specialReviewForm.get(AC_TYPE).toString().equalsIgnoreCase(AC_TYPE_INSERT) ||
                                specialReviewForm.get(AC_TYPE).toString().equalsIgnoreCase(AC_TYPE_UPDATE)){
                            boolean isSuccess=false;
                            boolean isErrorPResent = validateRowData(specialReviewForm, request);
                            if(!isErrorPResent){
                                isSuccess = saveSpecialReview(request,
                                        specialReviewForm, hmpSpRev, protocolNumber, seqNum);
                                specialReviewForm = resetFormDatas(specialReviewForm);
                                return mapping.findForward("success");
                            }
                            
                        }else if(specialReviewForm.get(AC_TYPE).toString().
                                equalsIgnoreCase(AC_TYPE_DELETE)){
                            boolean isSuccess=false;
                            isSuccess=deleteSpecialReview(request,
                                    specialReviewForm, hmpSpRev, protocolNumber, seqNum);
                            specialReviewForm = resetFormDatas(specialReviewForm);
                            // Added for Coeus 4.3 Enhancement - starts
                        } else if(specialReviewForm.get(AC_TYPE).toString().
                                equalsIgnoreCase(AC_TYPE_EDIT)){
                            specialReviewForm = editSpecialReview(request, specialReviewForm, protocolNumber, seqNum);
                            specialReviewForm.set(AC_TYPE, AC_TYPE_UPDATE);
                        }
                        // Added for Coeus 4.3 Enhancement - ends
                    }
                } else {
                    String errMsg = "release_lock_for";
                    messages = new ActionMessages();
                    messages.add("errMsg", new ActionMessage(errMsg,lockBean.getModuleKey(),lockBean.getModuleNumber()));
                    saveMessages(request, messages);
                }
            }
        }
        specialReviewForm.reset(mapping,request);
        readSavedStatus(request);
        return mapping.findForward("success");
        //modified for editing special review in display mode - Case 4590 - End
    }
    
    /*This method saves the special Review and
     *updates the indicator 
     */
    private boolean saveSpecialReview(HttpServletRequest request, 
                                       DynaValidatorForm specialReviewForm,
                                       HashMap hmpSpRev,
                                       String protocolNumber,String seqNum)throws Exception{

        HttpSession session = request.getSession();  
        //Modified for instance variable case#2960.
        DateUtils dateUtils = new DateUtils();        
        boolean isAllDeleted = false;
        boolean isMenuSaved = false;                                   
        boolean isSuccess=false;
        String splRevCode = (String)specialReviewForm.get(SPECIAL_REVIEW_CODE);
        specialReviewForm.set(PROTOCOL_NUMBER,protocolNumber);
        specialReviewForm.set(SEQUENCE_NUMBER,seqNum);
        isMenuSaved = true;
        WebTxnBean txnBean = new WebTxnBean();
        HashMap hmpIndMap = updateIndicators(specialReviewForm,request,
                                            protocolNumber,seqNum,isAllDeleted);
        HashMap hmSaveStatus = updateSaveStatus(request,protocolNumber,
                                            isMenuSaved);        
        // Check for the Duplicate Special REview code - start
        Hashtable htSplData = 
            (Hashtable)txnBean.getResults(request, GET_PROTOCOL_SPECIAL_REVIEW, hmpSpRev);
        Vector vecSplRev = 
            (Vector)htSplData.get(GET_PROTOCOL_SPECIAL_REVIEW);
        /*if(vecSplRev!= null && vecSplRev.size() > 0){
           boolean isPresent = checkDuplicateSpecialReview(vecSplRev,request,splRevCode);
           if(true){
               if(vecSplRev!= null && vecSplRev.size() >0){
                   for(int i=0;i<vecSplRev.size();i++){
                       DynaValidatorForm dynaValidatorForm = (DynaValidatorForm)vecSplRev.get(i);
                       String applDate = (String)dynaValidatorForm.get(APPLICATION_DATE);
                       String approvalDate = (String)dynaValidatorForm.get(APPROVAL_DATE);
                       if(applDate != null){
                           String value = 
                                dateUtils.formatDate(applDate,edu.mit.coeuslite.utils.DateUtils.MM_DD_YYYY);
                           dynaValidatorForm.set(APPLICATION_DATE,value);

                       }else{
                           dynaValidatorForm.set(APPLICATION_DATE,"");
                       }
                       if(approvalDate != null){
                           String dateValue = 
                                dateUtils.formatDate(approvalDate,edu.mit.coeuslite.utils.DateUtils.MM_DD_YYYY);
                           dynaValidatorForm.set(APPROVAL_DATE,dateValue);

                       }else{
                           dynaValidatorForm.set(APPROVAL_DATE,"");
                       }
                   }
               }
               request.setAttribute("ReviewList",vecSplRev);
               isSuccess=true;
               return isSuccess;
           }
        }// check for the Special REview - End*/
        // Added for Coeus 4.3 Enhancement - starts
        if(specialReviewForm.get(AC_TYPE).toString().equalsIgnoreCase(AC_TYPE_INSERT)){
            Hashtable spRevData = 
                (Hashtable)txnBean.getResults(request, GET_SPECIAL_REVIEW_NMUBER, hmpSpRev);
            HashMap hm = 
                (HashMap)spRevData.get(GET_SPECIAL_REVIEW_NMUBER);
            int spRevNumber = Integer.parseInt(hm.get("spRevNumber").toString());        
            specialReviewForm.set(SPECIAL_REVIEW_NUMBER,new Integer(spRevNumber+1));
        }
        // Added for Coeus 4.3 Enhancement - ends
        txnBean.getResults(request, UPDATE_PROTOCOL_INDICATOR, hmpIndMap);
        txnBean.getResults(request, UPDATE_SPECIAL_REVIEW, specialReviewForm);
        Hashtable hsSpRev = 
            (Hashtable)txnBean.getResults(request, "specialReview", hmpSpRev);
        // Update Save Status for the Menu check
       // txnBean.getResults(request, UPDATE_MENU_CHECKlIST, hmSaveStatus);
        // Update the Menu status to the session.
        updateSaveStatusToSession(session,isMenuSaved);
        /** Added for Coeus 4.3 Enhancement -Start
         *  Added specialReviewFlag for Customization, these flags can be set from CodeTables.
         *  filter if specialReviewTypeFlag is Y
         */        
        Vector vecSpecialReviewDetails = (Vector) hsSpRev.get(GET_SPECIAL_REVIEW_CODE);
        Vector vecFilteredData = new Vector();
        for(int index=0; index<vecSpecialReviewDetails.size(); index++){
            DynaValidatorForm form = (DynaValidatorForm) vecSpecialReviewDetails.get(index);
            // COEUSQA-2320 Show in Lite for Special Review in Code table
            // if(form.get("specialReviewFlag")!=null && form.get("specialReviewFlag").equals("Y")){
            if("Y".equals(form.get("showInLite"))){
                vecFilteredData.add(form);
            }
        }
        session.setAttribute("splReview", vecFilteredData);         
//        session.setAttribute("splReview", hsSpRev.get(GET_SPECIAL_REVIEW_CODE));
        //Added for Coeus 4.3 Enhancement -Ends
        session.setAttribute("approval", hsSpRev.get(GET_REVIEW_APPROVAL_TYPE));
        Vector vecData = (Vector)hsSpRev.get(GET_PROTOCOL_SPECIAL_REVIEW);
        if(vecData!= null && vecData.size() >0){
            for(int i=0;i<vecData.size();i++){
                DynaValidatorForm dynaValidatorForm = (DynaValidatorForm)vecData.get(i);
                String applDate = (String)dynaValidatorForm.get(APPLICATION_DATE);
                String approvalDate = (String)dynaValidatorForm.get(APPROVAL_DATE);
                if(applDate != null){
                    String value = dateUtils.formatDate(applDate,edu.mit.coeuslite.utils.DateUtils.MM_DD_YYYY);
                    dynaValidatorForm.set(APPLICATION_DATE,value);

                }else{
                    dynaValidatorForm.set(APPLICATION_DATE,"");
                }
                if(approvalDate != null){
                    String dateValue = dateUtils.formatDate(approvalDate,edu.mit.coeuslite.utils.DateUtils.MM_DD_YYYY);
                    dynaValidatorForm.set(APPROVAL_DATE,dateValue);

                }else{
                    dynaValidatorForm.set(APPROVAL_DATE,"");
                }
            }
        }
        session.setAttribute("ReviewList",vecData);
       return isSuccess;
    }   
    
    
    /*This method deletes the special Review and
     *updates the indicator 
     */
    private boolean deleteSpecialReview(HttpServletRequest request, 
                                       DynaValidatorForm specialReviewForm,
                                       HashMap hmpSpRev,
                                       String protocolNumber,String seqNum)throws Exception{ boolean isAllDeleted = false;
        //Modified for instance variable case#2960.
        DateUtils dateUtils = new DateUtils();                                       
        boolean isMenuSaved = false;
        boolean isSuccess=false;
        HttpSession session = request.getSession();
        WebTxnBean txnBean = new WebTxnBean();
        specialReviewForm.set(PROTOCOL_NUMBER,protocolNumber);
        specialReviewForm.set(SEQUENCE_NUMBER,seqNum);
        specialReviewForm.set("specialReviewCode", "0");
        specialReviewForm.set("approvalCode", "0");
        txnBean.getResults(request, UPDATE_SPECIAL_REVIEW, specialReviewForm);
        Hashtable hsSpRev = (Hashtable)txnBean.getResults(request, "specialReview", hmpSpRev);
        HashMap hmSaveStatus = null;
        HashMap hmpIndMap = null;
        Vector formData = (Vector)hsSpRev.get(GET_PROTOCOL_SPECIAL_REVIEW);
        if(formData==null || formData.size()==0){
            isAllDeleted = true;
            isMenuSaved = false;
            hmpIndMap = updateIndicators(specialReviewForm,request,protocolNumber,seqNum,isAllDeleted);
            txnBean.getResults(request, UPDATE_PROTOCOL_INDICATOR, hmpIndMap);
            hmSaveStatus = updateSaveStatus(request,protocolNumber,isMenuSaved);
        }else{
            isMenuSaved = true;;
            isAllDeleted = false;
            hmpIndMap = updateIndicators(specialReviewForm,request,protocolNumber,seqNum,isAllDeleted);
            txnBean.getResults(request, UPDATE_PROTOCOL_INDICATOR, hmpIndMap);
            hmSaveStatus = updateSaveStatus(request,protocolNumber,isMenuSaved);
        }
        // Update Save Status for the Menu check
       //txnBean.getResults(request, UPDATE_MENU_CHECKlIST, hmSaveStatus);
        // Update the Menu status to the session.
        updateSaveStatusToSession(session,isMenuSaved);
        /** Added for Coeus 4.3 Enhancement -Start
         *  Added specialReviewFlag for Customization, these flags can be set from CodeTables.
         *  filter if specialReviewTypeFlag is Y
         */        
        Vector vecSpecialReviewDetails = (Vector) hsSpRev.get(GET_SPECIAL_REVIEW_CODE);
        for(int index=0; index<vecSpecialReviewDetails.size(); index++){
            DynaValidatorForm form = (DynaValidatorForm) vecSpecialReviewDetails.get(index);
            //Modified for Case#3751 - Unable to remove special review info from protocol details- Start
            // COEUSQA-2320 Show in Lite for Special Review in Code table
            //if(form.get("specialReviewFlag") == null || form.get("specialReviewFlag").equals("N")){
            if(!"Y".equals(form.get("showInLite"))){
                vecSpecialReviewDetails.removeElementAt(index--);
            }
            //Modified for Case#3751 - Unable to remove special review info from protocol details- End
        }
        session.setAttribute("splReview", vecSpecialReviewDetails);
//        session.setAttribute("splReview", hsSpRev.get(GET_SPECIAL_REVIEW_CODE));
        //Added for Coeus 4.3 Enhancement -Ends      
        session.setAttribute("approval", hsSpRev.get(GET_REVIEW_APPROVAL_TYPE));
        Vector vecData = (Vector)hsSpRev.get(GET_PROTOCOL_SPECIAL_REVIEW);
        if(vecData!= null && vecData.size() >0){
            for(int i=0;i<vecData.size();i++){
                DynaValidatorForm dynaValidatorForm = (DynaValidatorForm)vecData.get(i);
                String applDate = (String)dynaValidatorForm.get(APPLICATION_DATE);
                String approvalDate = (String)dynaValidatorForm.get(APPROVAL_DATE);
                if(applDate != null){
                    String value = dateUtils.formatDate(applDate,edu.mit.coeuslite.utils.DateUtils.MM_DD_YYYY);
                    dynaValidatorForm.set(APPLICATION_DATE,value);
                }else{
                     dynaValidatorForm.set(APPLICATION_DATE,"");
                }
                if(approvalDate != null){
                    String dateValue = dateUtils.formatDate(approvalDate,edu.mit.coeuslite.utils.DateUtils.MM_DD_YYYY);
                    dynaValidatorForm.set(APPROVAL_DATE,dateValue);

                }else{
                    dynaValidatorForm.set(APPROVAL_DATE,"");
                }
            }
        }
        session.setAttribute("ReviewList",vecData); 
        readSavedStatus(request);
      return isSuccess;             
        
    }
    
    
    private HashMap updateIndicators(DynaValidatorForm specialReviewForm,HttpServletRequest request,
        String protocolNumber,String seqNum,boolean isAllDeleted) throws Exception{
                // for setting the Indicator Logic - Start
            HttpSession session = request.getSession();
                Timestamp dbTimestamp = prepareTimeStamp();
                specialReviewForm.set(UPDATE_TIMESTAMP,dbTimestamp.toString());
                
                //start--3
                Object data = session.getAttribute(CoeusLiteConstants.PROTOCOL_INDICATORS);
                //end--3
                
                HashMap indicatorMap = (HashMap)data;
                WebUtilities utils =  new WebUtilities();
                
                //start--4
                HashMap processedIndicator= (HashMap)utils.processIndicator(indicatorMap, CoeusLiteConstants.SPECIAL_REVIEW_INDICATOR, isAllDeleted);
                String processSpecialReviewIndicator = (String)processedIndicator.get(CoeusLiteConstants.SPECIAL_REVIEW_INDICATOR);
                session.setAttribute(CoeusLiteConstants.PROTOCOL_INDICATORS, processedIndicator);
                //end--4
                
                UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute("user"+session.getId());
                String userId = userInfoBean.getUserId();
                HashMap hmpIndMap=new HashMap();
                
                //start--5
                hmpIndMap.put(CoeusLiteConstants.FIELD,CoeusLiteConstants.SPECIAL_REVIEW_INDICATOR_VALUE);
                hmpIndMap.put(CoeusLiteConstants.PROTOCOL_NUMBER,protocolNumber);
                hmpIndMap.put(CoeusLiteConstants.SEQUENCE_NUMBER,seqNum);
                hmpIndMap.put(CoeusLiteConstants.INDICATOR,processSpecialReviewIndicator);
                hmpIndMap.put(CoeusLiteConstants.KEY_STUDY_TIMESTAMP,dbTimestamp.toString());
                hmpIndMap.put(CoeusLiteConstants.USER,userId);
                //end--5
                return hmpIndMap;
    }
    
    
   /*This method returns a HashMap which is used to update the Save Status */
     private HashMap updateSaveStatus(HttpServletRequest request,String protocolNumber,boolean isMenuSaved) throws Exception {
        HttpSession session = request.getSession();
        Timestamp dbTimestamp = prepareTimeStamp();
        UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute("user"+session.getId());
        String userId = userInfoBean.getUserId();
        HashMap hmpSaveMap=new HashMap();
        
        //start--6
        hmpSaveMap.put(CoeusLiteConstants.FIELD,"SPECIAL_REVIEW");
        hmpSaveMap.put(CoeusLiteConstants.PROTOCOL_NUMBER,protocolNumber);
        //end--6
        
        if (isMenuSaved) {
            //start--7
            hmpSaveMap.put(CoeusLiteConstants.AV_SAVED,"Y");
            //end--7
        } else {
            //start--8
           hmpSaveMap.put(CoeusLiteConstants.AV_SAVED,"N");
            //end--8
        }
        
        //start--9
        hmpSaveMap.put(CoeusLiteConstants.KEY_STUDY_TIMESTAMP,dbTimestamp.toString());
        hmpSaveMap.put(CoeusLiteConstants.USER,userId);
        hmpSaveMap.put(CoeusLiteConstants.AC_TYPE,"U");
        //end--9
        return hmpSaveMap;
    }
     
      /* This method maintains the status of the page 
     * whether it is saved or not in a session
     */ 
    
    private void  updateSaveStatusToSession (HttpSession session, boolean isMenuSaved) {
          Vector vecMenuItems = (Vector)session.getAttribute(MENU_ITEMS);
          Vector modifiedVector = new Vector();
          for (int index=0; index<vecMenuItems.size();index++) {
              MenuBean meanuBean = (MenuBean)vecMenuItems.get(index);
              String menuId = meanuBean.getMenuId();
              if (menuId.equals(SAVE_MENU)) {
                  if(isMenuSaved){
                    meanuBean.setDataSaved(true);  
                  }else{
                      meanuBean.setDataSaved(false);
                  }
                  
              }
              modifiedVector.add(meanuBean);
          }
          session.setAttribute(MENU_ITEMS, modifiedVector);
      } 
    
    public void cleanUp() {
    }
    
     public boolean checkDuplicateSpecialReview(Vector data,HttpServletRequest request,String splRevCode){
       boolean isPresent = true;
        for(int index = 0; index <data.size();index++){
            DynaValidatorForm form = (DynaValidatorForm)data.get(index);
            String specialRevCode=(String)form.get(SPECIAL_REVIEW_CODE);
            if(splRevCode.trim().equals(specialRevCode)){
                isPresent = false;
                ActionMessages messages = new ActionMessages();
                messages.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage("error.special_review_code_id"));
                saveMessages(request, messages);
            }
        }
       return isPresent;
    }
     
     public Vector getProtocolSpecialReview()throws Exception{
         Vector vecProtoSpecialReview = null ;
         return vecProtoSpecialReview;
     }
     
     /**
      * This Method validates the form data
      * For the selected combination of Approval Code and
      * SPecial Review Code, this method performs the mandatory checking of the
      * protocol number entry, Application date entry, and Approval Date entry.
      */
    private boolean validateRowData(DynaValidatorForm dynaSpecialForm, HttpServletRequest request) throws Exception{
        ActionMessages messages = new ActionMessages();
        int specialReviewCode = (Integer.parseInt(
                                    (String)dynaSpecialForm.get("specialReviewCode")));
        int approvalTypeCode = (Integer.parseInt(
                                    (String)dynaSpecialForm.get("approvalCode")));
        String protocolNumber = (String)dynaSpecialForm.get("spRevProtocolNumber");
                                  
        String applDate = (String)dynaSpecialForm.get("applicationDate") ;
        String apprDate = (String)dynaSpecialForm.get("approvalDate") ;
        
        String stData =  getValidateString(specialReviewCode, approvalTypeCode, request); 
        String protocolNumberFlag = null;
        String appliDateFlag = null;
        String arDateFlag = null;   
        boolean isErrorPresent = false ;
        if(stData != null){            
            protocolNumberFlag = ""+stData.charAt(0);
            appliDateFlag = ""+stData.charAt(1);
            arDateFlag = ""+stData.charAt(2);
            String temp = "" ;
            if(protocolNumberFlag != null){
                if(protocolNumberFlag.trim().equalsIgnoreCase("Y")) {
                    if(protocolNumber == null || protocolNumber.trim().length() <=0){
                        messages.add("error.specialReview.protocolNum",new ActionMessage("error.specialReview.protocolNum"));
                        saveMessages(request, messages);
                        isErrorPresent = true ;
                        System.out.println("You have to enter 'protocol number' with your current choice of special review and approval type");

                    }
                }
            }
            if(appliDateFlag != null){
                if(appliDateFlag.trim().equalsIgnoreCase("Y")) {
                    if(applDate == null || applDate.trim().length() <=0){
                        messages.add("error.specialReview.applicationDate",new ActionMessage("error.specialReview.applicationDate"));
                        saveMessages(request, messages);
                        isErrorPresent = true ;
                        System.out.println("You have to enter 'Application Date' with your current choice of special review and approval type");
                       
                    }
                }
            }
            if(arDateFlag != null){
                if(arDateFlag.trim().equalsIgnoreCase("Y")) {
                    if(apprDate == null || apprDate.trim().length() <=0){
                        messages.add("error.specialReview.approvalDate",new ActionMessage("error.specialReview.approvalDate"));
                        saveMessages(request, messages);
                        isErrorPresent = true ;
                    }
                }
            }
        }
        return isErrorPresent;
    }
    
     /**
     * Method which gets the Combination of Protocol Flag, 
     * Application Date Flag, and Approval date Flag for the input combination 
     * of Special Review Code and Approval Code.
     */
    private String getValidateString(int specialCode , int approval, HttpServletRequest request) throws Exception{
        
        String[] validate = formValidateString(request);
        String paramString = "" + specialCode + approval;
         String resultString = null;
         if(validate != null){
            int size = validate.length;
            String stTemp = null;
            for(int index = 0; index < size; index++){
                stTemp = validate[index];
                   if(stTemp.startsWith(paramString)){
                  resultString = stTemp.substring(paramString.length());
                }
            }
        }
       return resultString;
    }
    
     /**
     * Method which form the String array. Each String ia a combination of 
     * Special Review Code, Approval Code, Protocol flag, Application Date Flag, Approval Date Flag
     */
    private String[] formValidateString(HttpServletRequest request) throws Exception{
        Vector vecValidateRules = getValidSpecialReview(request);
        String[] validateStringCollection = null;
        if(vecValidateRules != null){
            int size = vecValidateRules.size();
            validateStringCollection = new String[size];
            String protocolFlag = null;
            String approvalDateFlag = null;
            String applicationDateFlag = null;
            int approvalCode = -1;
            int specialReviewCode = -1;
            for(int index = 0; index < size ; index++){
                DynaValidatorForm dynaValidForm = 
                    (DynaValidatorForm)vecValidateRules.elementAt(index);
                String validate = null;
                if(dynaValidForm != null){                    
                    specialReviewCode = Integer.parseInt(((String)dynaValidForm.get("specialReviewCode")));
                    approvalCode = Integer.parseInt(((String)dynaValidForm.get("approvalCode")));
                    protocolFlag = (String)dynaValidForm.get("protocolNumFlag");
                    applicationDateFlag = (String)dynaValidForm.get("applicationDateFlag");
                    approvalDateFlag = (String)dynaValidForm.get("approvalDateFlag");
                                      
                    validate = ""+specialReviewCode + approvalCode + protocolFlag 
                                    + applicationDateFlag + approvalDateFlag;
                    validateStringCollection[index] = validate;
                    
                }
            }
        }
        return validateStringCollection;
    }
    
    private Vector getValidSpecialReview(HttpServletRequest request)throws Exception{
        WebTxnBean txnBean = new WebTxnBean();
        Hashtable htValidSpecialReview = (Hashtable)txnBean.getResults(request, "getValidSpecialReview" , null);
        Vector vecValidSpecialReview = (Vector)htValidSpecialReview.get("getValidSpecialReview");
        return vecValidSpecialReview;
    }
    
    private DynaValidatorForm resetFormDatas(DynaValidatorForm specialReviewForm)throws Exception {
        specialReviewForm.set("specialReviewCode", "");
        specialReviewForm.set("approvalCode", "");
        specialReviewForm.set("spRevProtocolNumber", "");
        specialReviewForm.set("applicationDate", "");
        specialReviewForm.set("approvalDate", "");
        specialReviewForm.set("comments", "");
        specialReviewForm.set(AC_TYPE, EMPTY_STRING);
        return specialReviewForm;
    }
 
    /**
     * Added for Coeus 4.3 Enhancement
     * To set the values for record which edit is clicked.
     * @param request
     * @param dynaForm
     * @param protocolNumber
     * @param sequesnceNumber
     * @throws Exception
     * @return DynaValidatorForm
     */    
    private DynaValidatorForm editSpecialReview(HttpServletRequest request,
        DynaValidatorForm dynaForm, String protocolNumber, String sequesnceNumber)throws Exception{
        Vector vecSpecialReview = (Vector) request.getSession().getAttribute("ReviewList");
        if(vecSpecialReview!=null && vecSpecialReview.size()>0){
            for(int index=0; index<vecSpecialReview.size(); index++){
                DynaValidatorForm form = (DynaValidatorForm) vecSpecialReview.get(index);
                if(form.get("protocolNumber").equals(protocolNumber) && 
                    form.get("sequenceNumber").equals(sequesnceNumber) &&
                    form.get("specialReviewNumber").equals(dynaForm.get("specialReviewNumber"))){
                    dynaForm.set("approvalCode", form.get("approvalCode"));
                    dynaForm.set("specialReviewDescription", form.get("specialReviewDescription"));
                    dynaForm.set("approvalDate", form.get("approvalDate"));
                    dynaForm.set("applicationDate", form.get("applicationDate"));
                    dynaForm.set("specialReviewCode", form.get("specialReviewCode"));
                    dynaForm.set("spRevProtocolNumber", form.get("spRevProtocolNumber"));
                    dynaForm.set("specialReviewNumber", form.get("specialReviewNumber"));
                    dynaForm.set("comments", form.get("comments"));
                    dynaForm.set("approvalDescription", form.get("approvalDescription"));
                }
            }
        }
        return dynaForm;
    }
    
}
