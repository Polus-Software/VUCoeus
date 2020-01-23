/*
 * IacucExceptionsAction.java
 *
 * Created on February 18, 2010, 7:12 PM
 *
 */

/* PMD check performed, and commented unused imports and variables on 20-JAN-2011
 * by Satheesh Kumar K N
 */
package edu.mit.coeuslite.iacuc.action;

import edu.mit.coeus.bean.UserInfoBean;
//import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.utils.TypeConstants;
import edu.mit.coeus.utils.UtilFactory;
//import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeuslite.utils.CoeusLiteConstants;
import edu.mit.coeuslite.utils.CoeusliteMenuItems;
//import edu.mit.coeuslite.utils.WebUtilities;
import edu.mit.coeuslite.utils.bean.WebTxnBean;
//import java.io.IOException;
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
 * @author sreenathv
 */
public class IacucExceptionsAction extends ProtocolBaseAction{

    private static final String SUCCESS = "success";
    private static final String GET_IACUC_PROTO_EXCEPTION = "/getIacucProtoExceptions";
    //private static final String SAVE_IACUC_PROTO_EXCEPTION = "/saveIacucProtoException";
    //private static final String REMOVE_IACUC_PROTO_EXCEPTION = "/removeIacucProtoException";
    //private static final String LOAD_IACUC_PROTO_EXCEPTION = "/loadIacucProtoExceptionDetails";
    private static final String SAVE_IACUC_PROTO_PRINCIPLES = "/saveIacucProtoPrinciples";
    //Added for internal issue fix 1480
    private static final String SAVE_IACUC_PROTO_EXCEPTIONS_AND_PRINCIPLES = "/saveIacucProtoPrinciplesAndException";
    
    public IacucExceptionsAction() {
    }
    public ActionForward performExecute(ActionMapping actionMapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        String navigator = SUCCESS;
        navigator = performExceptionAction(actionMapping, actionForm, request);
        
        Map mapMenuList = new HashMap();
        mapMenuList.put("menuItems",CoeusliteMenuItems.IACUC_MENU_ITEMS);
        mapMenuList.put("menuCode",CoeusliteMenuItems.IACUC_PROTOCOL_SCIENTIFIC_JUST_MENU);
        setSelectedMenuList(request, mapMenuList);
        readSavedStatus(request);
        return actionMapping.findForward(navigator);
    }

 

    public void cleanUp() {
    }

    private String performExceptionAction(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request) {

//        String navigator = SUCCESS;
        HttpSession session = request.getSession();
        WebTxnBean webTxnBean = new WebTxnBean();
        DynaValidatorForm dynaForm = (DynaValidatorForm) actionForm;
        
        String protocolNumber = (String)session.getAttribute(CoeusLiteConstants.IACUC_PROTOCOL_NUMBER+session.getId());
        String sequnceNumber = (String)session.getAttribute(CoeusLiteConstants.IACUC_SEQUENCE_NUMBER+session.getId());
        
        
        
        if(actionMapping.getPath().equalsIgnoreCase(GET_IACUC_PROTO_EXCEPTION)){
            try {

            } catch (Exception ex) {
                UtilFactory.log(ex.getMessage());
            }
        // Commented  for COEUSQA-2724-Exceptions should be moved from Scientific Justification in IACUC_Start
//        } else if(actionMapping.getPath().equalsIgnoreCase(SAVE_IACUC_PROTO_EXCEPTION)){
             //The code excist here refactered to a method saveIacucProtocolException for internal issue fix 1480
//               saveIacucProtocolException(dynaForm, request,protocolNumber,sequnceNumber);
//        } else if(actionMapping.getPath().equalsIgnoreCase(REMOVE_IACUC_PROTO_EXCEPTION)){
//            try {
//                dynaForm.set("acType",TypeConstants.DELETE_RECORD);
//                dynaForm.set("awExceptionId", Integer.valueOf(request.getParameter("exceptionId")));
//                
//                dynaForm.set("awProtocolNumber", protocolNumber);
//                dynaForm.set("awSequenceNumber", sequnceNumber);
//                dynaForm.set("awUpdateTimeStamp", request.getParameter("updateTimestamp"));
//                Hashtable htProtoSpeciesData = (Hashtable)webTxnBean.getResults(request,"updateIacucProtocolException", dynaForm);
//            } catch (Exception ex) {
//                UtilFactory.log(ex.getMessage());
//            }
        
//        } else if(actionMapping.getPath().equalsIgnoreCase(LOAD_IACUC_PROTO_EXCEPTION)){
//            
//            dynaForm.set("protocolNumber", protocolNumber);
//            dynaForm.set("sequenceNumber", sequnceNumber);
//            dynaForm.set("exceptionId", Integer.valueOf(request.getParameter("exceptionId")));
//            try {
//                Hashtable htProtoSpeciesData = (Hashtable)webTxnBean.getResults(request,"getIacucProtoExceptionForEdit", dynaForm);
//                Vector vecSpeciesDet =(Vector)htProtoSpeciesData.get("getIacucProtoExceptionForEdit");
//                if(vecSpeciesDet != null && !vecSpeciesDet.isEmpty()){
//                    DynaValidatorForm exceptionsData =  (DynaValidatorForm) vecSpeciesDet.get(0);
//                    if(exceptionsData != null){
//                        
//                        dynaForm.set("exceptionCategoryCode", exceptionsData.get("exceptionCategoryCode"));
//                        dynaForm.set("exceptionDescription", exceptionsData.get("exceptionDescription"));
//                        dynaForm.set("exceptionId", exceptionsData.get("exceptionId"));
//                        dynaForm.set("exceptionCategoryDesc", exceptionsData.get("exceptionCategoryDesc"));
//                        dynaForm.set("awUpdateTimeStamp", exceptionsData.get("awUpdateTimeStamp"));
//                        dynaForm.set("awExceptionId", exceptionsData.get("awExceptionId"));
//                    }
//                }
//                
//                
//            } catch (Exception ex) {
//                UtilFactory.log(ex.getMessage());
//            }
//            
//            dynaForm.set("acType",TypeConstants.UPDATE_RECORD);
        // Commented  for COEUSQA-2724-Exceptions should be moved from Scientific Justification in IACUC_end
        } else if(actionMapping.getPath().equalsIgnoreCase(SAVE_IACUC_PROTO_PRINCIPLES)){
             //The code excist here refactered to a method saveIacucProtoPrinciples for internal issue fix 1480
            saveIacucProtoPrinciples(dynaForm, request,protocolNumber,sequnceNumber);
        //Added for internal issue fix 1480 begin
        }else if(actionMapping.getPath().equalsIgnoreCase(SAVE_IACUC_PROTO_EXCEPTIONS_AND_PRINCIPLES)){
               saveIacucProtoPrinciples(dynaForm, request,protocolNumber,sequnceNumber);
               // Commented  for COEUSQA-2724-Exceptions should be moved from Scientific Justification in IACUC_start        
//               String isExceptionVisible =(String)request.getParameter("exceptionVisible");        
//               if(isExceptionVisible != null && "yes".equals(isExceptionVisible)){
//                   saveIacucProtocolException(dynaForm, request,protocolNumber,sequnceNumber);
//               }
         // Commented  for COEUSQA-2724-Exceptions should be moved from Scientific Justification in IACUC_end
        } 
       //Added for internal issue fix 1480 end
        try {
            HashMap hmProtocolData = new HashMap();
           
            hmProtocolData.put("protocolNumber", protocolNumber);
            hmProtocolData.put("sequenceNumber", sequnceNumber);
            
            // Commented  for COEUSQA-2724-Exceptions should be moved from Scientific Justification in IACUC_start
            /*Hashtable htProtoExceptionsData = (Hashtable)webTxnBean.getResults(request,"getIacucProtocolExceptions", hmProtocolData);
            Vector vecProtoExceptionsData =(Vector) htProtoExceptionsData.get("getIacucProtocolExceptions");
            
            dynaForm.set("vecAddedExceptions", vecProtoExceptionsData);
            session.setAttribute("vecAddedExceptions",vecProtoExceptionsData);
            
            Hashtable htExceptionsData = (Hashtable)webTxnBean.getResults(request , "getIacucExceptionCategory" , null );
            Vector vecExceptionsData =(Vector)htExceptionsData.get("getIacucExceptionCategory");
            session.setAttribute("vecExceptions",vecExceptionsData);*/
            // Commented  for COEUSQA-2724-Exceptions should be moved from Scientific Justification in IACUC_end
            
            Hashtable htProtopriciplesData = (Hashtable)webTxnBean.getResults(request,"getIacucProtoPrinciples", hmProtocolData);
            Vector vecPriciplesDet =(Vector)htProtopriciplesData.get("getIacucProtoPrinciples");
            if(vecPriciplesDet != null && !vecPriciplesDet.isEmpty()){
                DynaValidatorForm exceptionsData =  (DynaValidatorForm) vecPriciplesDet.get(0);
                
                dynaForm.set("reductionPrinciple", exceptionsData.get("reductionPrinciple"));
                dynaForm.set("refinementPrinciple", exceptionsData.get("refinementPrinciple"));
                dynaForm.set("replacementPrinciple", exceptionsData.get("replacementPrinciple"));
                //Added for indicator logic
                dynaForm.set("sequenceNumber", exceptionsData.get("sequenceNumber"));
                // Commented  for COEUSQA-2724-Exceptions should be moved from Scientific Justification in IACUC_start
                /*dynaForm.set("exceptionPresent", exceptionsData.get("exceptionPresent").equals("Y")?"on":"off");*/
                // Commented  for COEUSQA-2724-Exceptions should be moved from Scientific Justification in IACUC_end
                dynaForm.set("awPriciplesUpdateTimeStamp", exceptionsData.get("awPriciplesUpdateTimeStamp"));
                
                 dynaForm.set("principlesAcType",TypeConstants.UPDATE_RECORD);
                
            } else {
                dynaForm.set("principlesAcType",TypeConstants.INSERT_RECORD);
            }
        } catch (Exception ex) {
            UtilFactory.log(ex.getMessage());
        }
        
        return SUCCESS;
    }

//    private boolean validateFormData(DynaValidatorForm dynaForm, HttpServletRequest request) {
//          boolean valid = true;
//        
//         
//        String exceptionDesc = (String) dynaForm.get("exceptionDescription");
//        
//        if("".equals(exceptionDesc.trim())){
//            ActionMessages messages = new ActionMessages();
//            messages.add("invalidExceptionDesc", new ActionMessage("scientifiJustification.exceptionCategoryDesc.error.mandatory"));
//            saveMessages(request, messages);
//            valid = false;
//         } //Added for 1485 - user data entry is not restricted - start
//           //Modified for-COEUSQA-2711-Increase IACUC Scientific Justification to 1000 characters-Start
//        else if(exceptionDesc.trim().length() > 1000) {
//             ActionMessages messages = new ActionMessages();
//             messages.add("invalidExceptionDesc", new ActionMessage("scientifiJustification.exceptionCategoryDesc.error.length"));
//             saveMessages(request, messages);
//             valid = false;
//         }//Added for 1485 - user data entry is not restricted - end
//         //Modified for-COEUSQA-2711-Increase IACUC Scientific Justification to 1000 characters-End
//       
//         int exceptionCode = (Integer) dynaForm.get("exceptionCategoryCode");
//          //ActionMessages messages = new ActionMessages();
//          if(0 == exceptionCode){
//              ActionMessages messages = new ActionMessages();
//              messages.add("invalidExceptionCode", new ActionMessage("scientifiJustification.exceptionCategory.error.mandatory"));
//              saveMessages(request, messages);
//              valid = false;
//          }
//         //Added for 1485 - user data entry is not restricted - start
//         dynaForm.set("isExceptionVisible", "Y");
//         //Added for 1485 - user data entry is not restricted - end
//        return valid;
//    }
   
    //Added for 1485 - user data entry is not restricted - start
    private boolean validatePrinciplesData(DynaValidatorForm dynaForm, HttpServletRequest request) {
          boolean valid = true;
          ActionMessages messages = new ActionMessages();
          if(isExceedsMaximumLength(dynaForm, "reductionPrinciple")) {  
            messages.add("invalidPrincipleReduction", new ActionMessage("reductionPrinciple.error.maxlength"));
            saveMessages(request, messages);
            valid = false;
          }
          if(isExceedsMaximumLength(dynaForm, "refinementPrinciple")) {
            messages.add("invalidPrincipleRefinement", new ActionMessage("refinementPrinciple.error.maxlength"));
            saveMessages(request, messages);
            valid = false;
          }
          if(isExceedsMaximumLength(dynaForm, "replacementPrinciple")) {
            messages.add("invalidPrincipleReplacement", new ActionMessage("replacementPrinciple.error.maxlength"));
            saveMessages(request, messages);
            valid = false;
          }
//         dynaForm.set("isExceptionVisible", "N");
         return valid;
    }
    
    private boolean isExceedsMaximumLength(DynaValidatorForm dynaForm, String parameter) {
       if(dynaForm.get(parameter).toString().trim().length() > 2000) {
           return true;
       } 
       return false;
    }
     //Added for 1485 - user data entry is not restricted - end
    
    
       private String saveIacucProtoPrinciples(DynaValidatorForm dynaForm,
              HttpServletRequest request, 
              String protocolNumber, String sequnceNumber){
              String returnValue = "";
                 try {
                
                //Added for 1485 - user data entry is not restricted  - start
                 boolean validData = validatePrinciplesData(dynaForm, request);
                    if(!validData ){
                    return "invalidPrinciple";
                    // return "invalidData";
                    }
               //Added for 1485 - user data entry is not restricted  - end
                
                dynaForm.set("protocolNumber", protocolNumber);
                //Modified for sequence number start
                
                String formSeqNumber = (String)dynaForm.get("sequenceNumber");
                
                if(isGenerateSequence(request) && !sequnceNumber.equals(formSeqNumber)){
                    if(TypeConstants.UPDATE_RECORD.equals(dynaForm.get("principlesAcType"))){
                        dynaForm.set("principlesAcType",TypeConstants.INSERT_RECORD);
                    }
                }
                
                //Modified for sequence number end
                
                dynaForm.set("sequenceNumber", sequnceNumber);
                HttpSession session = request.getSession();
                UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute("user"+session.getId());
                
                dynaForm.set("updateUser", userInfoBean.getUserId());
                Timestamp dbTimestamp = prepareTimeStamp();
                dynaForm.set("updateTimeStamp",dbTimestamp.toString());
                
                dynaForm.set("awProtocolNumber", protocolNumber);
                dynaForm.set("awSequenceNumber", sequnceNumber);
                
                
//                if("on".equalsIgnoreCase((String) dynaForm.get("exceptionPresent"))){
//                    dynaForm.set("exceptionPresent", "Y");
//                } else {
//                    dynaForm.set("exceptionPresent", "N");
//                }
                WebTxnBean webTxnBean = new WebTxnBean();
                Hashtable htProtoPriciplesData = (Hashtable)webTxnBean.getResults(request,"updateIacucProtoPrinciples", dynaForm);
                
                updateIndicators(false,request,CoeusLiteConstants.SCIENTIFIC_JUSTIF_INDICATOR_VALUE,CoeusLiteConstants.SCIENTIFIC_JUSTIF_INDICATOR);
                //Commented for indicator logic
                //dynaForm.set("principlesAcType",TypeConstants.UPDATE_RECORD);
            } catch (Exception ex) {
                UtilFactory.log(ex.getMessage());
            }
              return returnValue;
            }
      
      
//      private String saveIacucProtocolException(DynaValidatorForm dynaForm,
//              HttpServletRequest request, 
//              String protocolNumber, String sequnceNumber){
//          String returnValue = "";
//           try {
//                
//                 boolean validData = validateFormData(dynaForm, request);
//                    if(!validData ){
//                        return "invalidData";
//                    }
//                 
//                //Added for COEUSQA-2711_Increase IACUC Scientific Justification-start
//                String exceptionDesc = (String) dynaForm.get("exceptionDescription");                 
//                dynaForm.set("exceptionDescription", exceptionDesc.trim());
//                //Added for COEUSQA-2711_Increase IACUC Scientific Justification-end
//                dynaForm.set("protocolNumber", protocolNumber);
//                dynaForm.set("sequenceNumber", sequnceNumber);
//                HttpSession session = request.getSession();
//                UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute("user"+session.getId());
//                
//                dynaForm.set("updateUser", userInfoBean.getUserId());
//                Timestamp dbTimestamp = prepareTimeStamp();
//                dynaForm.set("updateTimeStamp",dbTimestamp.toString());
//               
//                dynaForm.set("awProtocolNumber", protocolNumber);
//                dynaForm.set("awSequenceNumber", sequnceNumber);
//
//                WebTxnBean webTxnBean = new WebTxnBean();
//                String exceptionAcType = request.getParameter("acType");
//                if(exceptionAcType == null || "".equals(exceptionAcType)){
//                    dynaForm.set("acType", "I");
//                }
//                Hashtable htProtoSpeciesData = (Hashtable)webTxnBean.getResults(request,"updateIacucProtocolException", dynaForm);
//                dynaForm.set("acType", "");
//                
//            } catch (Exception ex) {
//                UtilFactory.log(ex.getMessage());
//            }
//           
//           return returnValue;
//        } 
    
       
       //Added for inticator logic begin
       
     // Method is moved to ProtocolBaseAction
     /**
     * Method to Update key persons indicator
     * @param isAllDeleted
     * @throws Exception if exception occur
     */
//    private void updateIndicators(boolean isAllDeleted, HttpServletRequest request) throws Exception{
//        HttpSession session = request.getSession();
//        WebTxnBean webTxnBean = new WebTxnBean();
//        UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute("user"+session.getId());
//        Object data = session.getAttribute(CoeusLiteConstants.IACUC_PROTOCOL_INDICATORS);
//        HashMap hmIndicatorMap = (HashMap)data;
//        WebUtilities utils =  new WebUtilities();
//        HashMap processedIndicator= (HashMap)utils.processIndicator(hmIndicatorMap, CoeusLiteConstants.SCIENTIFIC_JUSTIF_INDICATOR, isAllDeleted);
//        String  scientificJustificationIndicator  = (String)processedIndicator.get(CoeusLiteConstants.SCIENTIFIC_JUSTIF_INDICATOR);
//        session.setAttribute(CoeusLiteConstants.IACUC_PROTOCOL_INDICATORS, processedIndicator);
//        HashMap hashMap = new HashMap();
//        hashMap.put(CoeusLiteConstants.IACUC_PROTOCOL_NUMBER,session.getAttribute(CoeusLiteConstants.IACUC_PROTOCOL_NUMBER+session.getId()));
//        hashMap.put(CoeusLiteConstants.IACUC_SEQUENCE_NUMBER,session.getAttribute(CoeusLiteConstants.IACUC_SEQUENCE_NUMBER+session.getId()));
//        hashMap.put(CoeusLiteConstants.FIELD,CoeusLiteConstants.SCIENTIFIC_JUSTIF_INDICATOR_VALUE);
//        hashMap.put(CoeusLiteConstants.INDICATOR,scientificJustificationIndicator);
//        Timestamp dbTimestamp = prepareTimeStamp();
//        hashMap.put(CoeusLiteConstants.KEY_STUDY_TIMESTAMP,dbTimestamp.toString());
//        hashMap.put(CoeusLiteConstants.USER,userInfoBean.getUserId());
//        webTxnBean.getResults(request, "updateIacucIndicator", hashMap);
//    }
     //Added for inticator logic end
   
}
