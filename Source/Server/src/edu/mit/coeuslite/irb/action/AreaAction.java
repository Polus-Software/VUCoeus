/*
 * AreaForm.java
 *
 * Created on March 1, 2005, 2:45 PM
 * Created on March 9, 2005, 4:32 PM
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

/* PMD check performed, and commented unused imports and variables on 18-OCT-2010
 * by Johncy M John
 */

package edu.mit.coeuslite.irb.action;

import edu.mit.coeus.bean.UserInfoBean;
import edu.mit.coeus.irb.bean.AreaOfResearchTxnBean;
import edu.mit.coeus.utils.UtilFactory;

//Start--1

import edu.mit.coeuslite.utils.CoeusLiteConstants;
import edu.mit.coeuslite.utils.LockBean;
//end--1

import edu.mit.coeuslite.utils.bean.WebTxnBean;
import edu.mit.coeuslite.utils.bean.MenuBean;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.validator.DynaValidatorForm;

/**
 *
 * @author  shijiv
 */
public class AreaAction extends ProtocolBaseAction {
    

// private ActionForward actionForward;
// private Timestamp dbTimestamp;
// private WebTxnBean webTxnBean;
 private static final String SAVE_MENU = "004";
 private static final String MENU_ITEMS ="menuItemsVector";
 private static final String PROTOCOL_NUMBER="protocolNumber";
 private static final String SEQUENCE_NUMBER="sequenceNumber";
 private static final String RESEARCH_AREA_CODE="researchAreaCode";
 private static final String EMPTY_STRING="";
 private static final String AC_TYPE="acType";
 private static final String AREA_TIME_STAMP="areaTimeStamp";
 private static final String GET_PROTO_RESEARCH_AREAS="getProtoResearchAreas";
 private static final String SUCCESS="success";
 private static final String UPD_AREAS_OF_RESEARCH="updateAreasOfResearch";
 private static final String AREA_RESEARCH="areaResearch";
 //private static final String UPD_MENU_CHECK_LIST="updateMenuCheckList";
 private static final String AREAS_OF_RESEARCH_FIELD="AREAS_OF_RESEARCH";
 private static final String USER ="user";
 private static final String AC_TYPE_INSERT="I";
 private static final String AC_TYPE_DELETE="D";
 private static final String AC_TYPE_UPDATE="U";
 private static final String ERROR_AREA_OF_RESEARCH="error.area_of_research_id";
 private static final String ERROR_AREA_OF_RESEARCH_NOT_EXISTS= "error.area_of_research_not_exists";
    
    /** Creates a new instance of AreaForm */
    public AreaAction() {
    }
    
   
    public void cleanUp() {
    }    
    
    public ActionForward performExecute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse res) throws Exception {
        DynaValidatorForm areaForm = (DynaValidatorForm)form;
        WebTxnBean webTxnBean= new WebTxnBean(); 
        HashMap hmpAreaData = new HashMap();     
            HttpSession session = request.getSession();
            
            //start--2
            
            String protocolNumber = (String)session.getAttribute(CoeusLiteConstants.PROTOCOL_NUMBER+session.getId());
            //end--2
            
            //start--3
            
            String sequenceNumber = (String)session.getAttribute(CoeusLiteConstants.SEQUENCE_NUMBER+session.getId());
            //end--3
            
            int seqNum = -1;
            if(sequenceNumber!= null){
                seqNum = Integer.parseInt(sequenceNumber);
            }
            hmpAreaData.put(PROTOCOL_NUMBER,protocolNumber);
            hmpAreaData.put(SEQUENCE_NUMBER,new Integer(seqNum));     
            if(!areaForm.get(AC_TYPE).equals(EMPTY_STRING)){
                 // Check if lock exists or not
                UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute("user");
                LockBean lockBean = getLockingBean(userInfoBean, (String)session.getAttribute(CoeusLiteConstants.PROTOCOL_NUMBER+session.getId()), request);
                boolean isLockExists = isLockExists(lockBean, lockBean.getModuleKey());
                LockBean lockData = getLockedData(CoeusLiteConstants.PROTOCOL_LOCK_STR+lockBean.getModuleNumber(), request);
                if(!isLockExists && lockBean.getSessionId().equals(lockData.getSessionId())) {
                    if(areaForm.getString(AC_TYPE).equalsIgnoreCase(AC_TYPE_INSERT)){
                        boolean isSuccess=false;
                        isSuccess=saveAreasofResearch(session, request, areaForm, hmpAreaData, protocolNumber, seqNum);                    
                        if(isSuccess){
                            return mapping.findForward(SUCCESS);
                        }                
                    }else if(areaForm.getString(AC_TYPE).equalsIgnoreCase(AC_TYPE_DELETE)){ 

                        boolean isSuccess=false;
                        isSuccess=deleteAreasofResearch(session, request, areaForm, hmpAreaData, protocolNumber, seqNum);   
                    }
                } else {
                    String errMsg = "release_lock_for";
                    ActionMessages messages = new ActionMessages();                
                    messages.add("errMsg", new ActionMessage(errMsg,lockBean.getModuleKey(),lockBean.getModuleNumber()));
                    saveMessages(request, messages);
                }   
            }
        ActionForward actionForward = mapping.findForward(SUCCESS);
        areaForm.reset(mapping,request);
        readSavedStatus(request);
        return actionForward;
        
    }    
    
    /* This method is used to save area of research page
     */
    private boolean saveAreasofResearch(HttpSession session,HttpServletRequest request, 
                                       DynaValidatorForm areaForm,
                                       HashMap hmpAreaData,
                                       String protocolNumber,int seqNum) throws Exception{
        
        boolean isMenuSaved = false;
        boolean isSuccess=false;
        //COEUSQA-2802 - Allow user to remove values for areas of research in IRB and IACUC 
        boolean isExistsInDB = true;
        WebTxnBean webTxnBean = new WebTxnBean();
       Timestamp dbTimestamp = prepareTimeStamp();
        areaForm.set(AREA_TIME_STAMP,dbTimestamp.toString());
        areaForm.set(PROTOCOL_NUMBER,protocolNumber);
        areaForm.set(SEQUENCE_NUMBER,new Integer(seqNum));
        isMenuSaved = true;
         String areaCode=(String)areaForm.get(RESEARCH_AREA_CODE);
        HashMap hmpSaveStatus = updateSaveStatus(session,protocolNumber,isMenuSaved);
        // Check for the Duplicate Area Of Research - start
        Hashtable htGetAreaData = (Hashtable)webTxnBean.getResults(request, GET_PROTO_RESEARCH_AREAS, hmpAreaData);
        Vector vcArea=(Vector)htGetAreaData.get(GET_PROTO_RESEARCH_AREAS);
        if(vcArea!= null && vcArea.size() > 0){
           boolean isPresent = checkAreaOfResearch(vcArea,request,areaCode);
           //COEUSQA-2802 - Allow user to remove values for areas of research in IRB and IACUC 
           if(isPresent) {
              isExistsInDB = checkAreaOfResearchExists(session, request, areaCode);
           }
           if(!isPresent || !isExistsInDB){
           session.setAttribute("areaData", vcArea);
           isSuccess=true;
           return isSuccess;
       }
    }// check for the duplicate Area Of Research - End

       webTxnBean.getResults(request, UPD_AREAS_OF_RESEARCH, areaForm);
        Hashtable htAreaData = (Hashtable)webTxnBean.getResults(request, AREA_RESEARCH, hmpAreaData);
        // Update Save Status for the Menu check
       // webTxnBean.getResults(request,UPD_MENU_CHECK_LIST, hmpSaveStatus);

        // Update the Menu status to the session.
       // updateSaveStatusToSession(session,isMenuSaved);
        session.setAttribute("areaData", htAreaData.get(GET_PROTO_RESEARCH_AREAS));
        resetAreaOfResearch(areaForm);
        return isSuccess;
    }
    
    
    /* This method is used to delete area of research 
     */    
    private boolean deleteAreasofResearch(HttpSession session,HttpServletRequest request, 
                                           DynaValidatorForm areaForm,
                                           HashMap hmpAreaData,
                                           String protocolNumber,int seqNum)throws Exception{
        boolean isMenuSaved = false;
        boolean isSuccess=false;        
        HashMap hmpSaveStatus = null;
        WebTxnBean webTxnBean = new WebTxnBean();
        Hashtable htGetAreaData = (Hashtable)webTxnBean.getResults(request,AREA_RESEARCH,hmpAreaData);
        Vector vcAreaData=(Vector)htGetAreaData.get(GET_PROTO_RESEARCH_AREAS);
        String code=areaForm.getString(RESEARCH_AREA_CODE);
        //Code Modified for Coeus4.3 Enhancement - starts
        if(vcAreaData!=null && vcAreaData.size()>1){
            for(int i=0;i<vcAreaData.size();i++){
                DynaValidatorForm dynaAreaForm = (DynaValidatorForm)vcAreaData.get(i);
                String rACode = dynaAreaForm.getString(RESEARCH_AREA_CODE);
                if(rACode.equals(code)){
                    dynaAreaForm.set(AC_TYPE,AC_TYPE_DELETE);
                    webTxnBean.getResults(request, UPD_AREAS_OF_RESEARCH, dynaAreaForm);
                    Hashtable htArea = (Hashtable)webTxnBean.getResults(request, AREA_RESEARCH, hmpAreaData);
                    Vector vcData = (Vector)htArea.get(GET_PROTO_RESEARCH_AREAS);
                    
                    if(vcData==null || vcData.size() == 0){
                        isMenuSaved = false;
                        hmpSaveStatus = updateSaveStatus(session,protocolNumber,isMenuSaved);
                    }else{
                        isMenuSaved = true;
                        hmpSaveStatus = updateSaveStatus(session,protocolNumber,isMenuSaved);
                    }
                    // Update Save Status for the Menu check
                    //webTxnBean.getResults(request,UPD_MENU_CHECK_LIST, hmpSaveStatus);
                    // Update the Menu status to the session.
                    // updateSaveStatusToSession(session,isMenuSaved);
                    session.setAttribute("areaData", htArea.get(GET_PROTO_RESEARCH_AREAS));
                    
                }
            }
        } else {
                ActionMessages messages = new ActionMessages();
                messages.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage("error.area_of_research_delete"));
                saveMessages(request, messages);            
        }
        //Code Modified for Coeus4.3 Enhancement - ends
        resetAreaOfResearch(areaForm);
          return isSuccess;  
    }
    
     /*This method returns a HashMap which is used to update the Save Status */
     private HashMap updateSaveStatus(HttpSession session,String protocolNumber,boolean isMenuSaved) throws Exception {
        
        Timestamp dbTimestamp = prepareTimeStamp();
        UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute(USER);
        String userId = userInfoBean.getUserId();
        HashMap hmpSaveMap=new HashMap(); 
        
        //start--4
        
        hmpSaveMap.put(CoeusLiteConstants.FIELD,AREAS_OF_RESEARCH_FIELD);
        //end--4
        
        //start--5
        
        hmpSaveMap.put(CoeusLiteConstants.PROTOCOL_NUMBER,protocolNumber);
        //end--5
        
        if (isMenuSaved) {
            
            //start--6
            
            hmpSaveMap.put(CoeusLiteConstants.AV_SAVED,"Y");
            //end--6
            
        } else {
            
            //start--7
            
            hmpSaveMap.put(CoeusLiteConstants.AV_SAVED,"N");
            //end--7
        }
        
        //start--8
        
        hmpSaveMap.put(CoeusLiteConstants.KEY_STUDY_TIMESTAMP,dbTimestamp.toString());
        //end--8
        
        //start--9
        
        hmpSaveMap.put(CoeusLiteConstants.USER,userId);
        //end--9
        
        //start--10
        
        hmpSaveMap.put(CoeusLiteConstants.AC_TYPE,AC_TYPE_UPDATE);
        //end--10
        return hmpSaveMap;
    }
     
     /* This method maintains the status of the page 
     * whether it is saved or not in a session
     */  
     
      private void  updateSaveStatusToSession (HttpSession session, boolean isMenuSaved) {
          Vector vcMenuItems = (Vector)session.getAttribute(MENU_ITEMS);
          Vector modifiedVector = new Vector();
          for (int index=0; index<vcMenuItems.size();index++) {
              MenuBean meanuBean = (MenuBean)vcMenuItems.get(index);
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
      
      /*This method is used to check duplicate area of research */
      
      public boolean checkAreaOfResearch(Vector vcData,HttpServletRequest request,String areaCode){
       boolean isPresent = true;
        for(int index = 0; index <vcData.size();index++){
            DynaValidatorForm dynaForm = (DynaValidatorForm)vcData.get(index);
            String code=(String)dynaForm.get(RESEARCH_AREA_CODE);
            if(areaCode.trim().equals(code)){
                isPresent = false;
                ActionMessages messages = new ActionMessages();
                messages.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage(ERROR_AREA_OF_RESEARCH));
                saveMessages(request, messages);
            }
        }
       return isPresent;
    }
      //COEUSQA-2802 - Allow user to remove values for areas of research in IRB and IACUC 
       /*This method is used to check duplicate area of research
        * @param HttpSession session,  HttpServletRequest request, String researchAreaCode
        * @return boolean (returns true if data exists in DB, else false)
        */
      
      public boolean checkAreaOfResearchExists(HttpSession session, HttpServletRequest request, String areaCode){
        boolean isPresent = true;
        try { 
            UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute(USER);
            String userId = userInfoBean.getUserId();
            AreaOfResearchTxnBean areaOfResearchTxnBean = new AreaOfResearchTxnBean(userId);
            int present = areaOfResearchTxnBean.checkResearchAreaExists(areaCode);
            if(present <= 0){
                isPresent = false;
                ActionMessages messages = new ActionMessages();
                messages.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage(ERROR_AREA_OF_RESEARCH_NOT_EXISTS));
                saveMessages(request, messages);
            }
        } catch (Exception ex) {
            UtilFactory.log(ex.getMessage());
            
        }
       return isPresent;
    }
      public void resetAreaOfResearch(DynaValidatorForm dynaAreaResearch)throws Exception{
          dynaAreaResearch.set("researchAreaCode" , EMPTY_STRING);
          dynaAreaResearch.set("researchAreaDescription" , EMPTY_STRING);
      }
    
    
    
}