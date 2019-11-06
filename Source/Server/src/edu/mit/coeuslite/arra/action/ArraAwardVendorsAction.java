/*
 * ArraAwardVendorsAction.java
 *
 * Created on August 11, 2009, 10:45 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package edu.mit.coeuslite.arra.action;

import edu.mit.coeus.arra.ArraAuthorization;
import edu.mit.coeus.arra.bean.ArraReportTxnBean;
import edu.mit.coeus.bean.UserInfoBean;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.utils.TypeConstants;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeuslite.utils.CoeusDynaFormList;
import edu.mit.coeuslite.utils.CoeusliteMenuItems;
import edu.mit.coeuslite.utils.LockBean;
import edu.mit.coeuslite.utils.bean.WebTxnBean;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.beanutils.DynaBean;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.validator.DynaValidatorForm;
/**
 *
 * @author keerthyjayaraj
 */
public class ArraAwardVendorsAction extends ArraBaseAction{
    
    private static final String ADD_VENDORS = "/addVendors";
    private static final String GET_VENDORS = "/getArraVendors";
    private static final String SAVE_VENDORS = "/saveVendorDetails";
    private static final String REMOVE_VENDORS = "/removeArraVendorDetails";
    private static final String REMOVED_VENDORS = "removedVendors";
    private static final String VENDOR_UPDATED = "vendorUpdated";
    private static final String DYNA_BEAN_LIST = "arraVendorDynaBeansList";
    private static final String SUCCESS = "success";
    private static final String EMPTY_STRING = "";
    private static final String AC_TYPE = "acType";
    private static final String UPDATE_TIMESTAMP = "updateTimestamp";
    private static final String AW_UPDATE_TIMESTAMP = "awUpdateTimestamp";
    private static final String UPDATE_ARRA_VENDORS = "updateArraVendor";
    private static final String SUBCONTRACT_CODE = "arraAwardSubcontractCode";
    private static final String EDIT_COLUMN_PROPERTIES_RIGHT = "canEditAllRight";
    
    protected static final String ARRA_REPORT_NUMBER = "arraReportNo";
    protected static final String ARRA_REPORT_AWARD_NUMBER = "arraReportAwardNo";
    protected static final String ARRA_REPORT_VERSION = "arraReportVersion";
   
    protected static final String ARRA_LOCK_STRING = "osp$Arra_";
    private static final String ARRA_REPORT_VENDORS_TABLE_NAME = "OSP$ARRA_REPORT_VENDORS";
    private static final String ARRA_REPORT_VENDORS_COLUMN_PROPERTIES = "arraVendorColumnProperties";
    /** Creates a new instance of ArraAwardVendorsAction */
    public ArraAwardVendorsAction() {
    }
    
    public ActionForward performExecute(ActionMapping actionMapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        
        HttpSession session = request.getSession();
        String navigator = EMPTY_STRING;
        //The subcontract code of Award vendors will be null.
        CoeusDynaFormList coeusDynaFormList = (CoeusDynaFormList) actionForm;
        ActionForward forward = getVendors(actionMapping,request, coeusDynaFormList);
        setSelectedStatusMenu(CoeusliteMenuItems.ARRA_VENDOR_MENU_CODE,session);
        return forward;
    }
    
    private ActionForward getVendors(ActionMapping actionMapping,HttpServletRequest request,CoeusDynaFormList coeusDynaFormList) throws Exception{
        String navigator = EMPTY_STRING;
        HttpSession session = request.getSession();        
        if(actionMapping.getPath().equalsIgnoreCase(GET_VENDORS)){
            navigator = getVendors(coeusDynaFormList, request);
        }else if(actionMapping.getPath().equalsIgnoreCase(ADD_VENDORS)){
            navigator = addArraVendor(coeusDynaFormList, request);
        }else if(actionMapping.getPath().equalsIgnoreCase(SAVE_VENDORS)){        
            String mitAwardNo = (String)session.getAttribute(ARRA_REPORT_AWARD_NUMBER);
            String reportNo = (String)session.getAttribute(ARRA_REPORT_NUMBER);
            UserInfoBean userInfoBean = (UserInfoBean) session.getAttribute("user"+session.getId());
            LockBean lockBean = getArraLockingBean(userInfoBean, mitAwardNo, new Integer(reportNo).intValue(),request);
            boolean moduleCanBeLocked = isLockExists(lockBean, lockBean.getModuleKey());
            LockBean lockData = getLockedData(ARRA_LOCK_STRING+lockBean.getModuleNumber(), request);
            if(moduleCanBeLocked || !lockBean.getSessionId().equals(lockData.getSessionId())) {
                String errMsg = "release_lock_for";
                String lockedArraAwardNumber = lockBean.getModuleNumber().substring(0,10);
                ActionMessages messages = new ActionMessages();
                messages.add("errMsg", new ActionMessage(errMsg,lockBean.getModuleKey(),lockedArraAwardNumber));
                saveMessages(request, messages);
            }else{
                deleteVendors(request);
                saveVendors(coeusDynaFormList,request);                
                navigator = getVendors(coeusDynaFormList, request);
            }
        
        }else if(actionMapping.getPath().equalsIgnoreCase(REMOVE_VENDORS)){
            navigator =removeVendor(coeusDynaFormList, request);;
        }
        
        ActionForward actionForward = actionMapping.findForward(navigator);
        return actionForward;
    }

    private void saveVendors(CoeusDynaFormList coeusDynaFormList, HttpServletRequest request) throws Exception{
        List lstVendors = coeusDynaFormList.getList();
        if(lstVendors!=null && !lstVendors.isEmpty()){
            WebTxnBean webTxnBean = new WebTxnBean();
            DynaActionForm dynaForm;
            double dynaAmount;
            String paymentAmt;
            
            for(int i=0;i<lstVendors.size();i++){
                dynaForm = (DynaActionForm)lstVendors.get(i);
                //validate Amount
                paymentAmt  = (String)dynaForm.get("strPaymentAmount");
                if(paymentAmt!=null){
                    try{
                        dynaAmount = formatStringToDouble(paymentAmt);
                    }catch(NumberFormatException ne){
                        dynaAmount = 0;
                    }
                    dynaForm.set("paymentAmount", new Double(dynaAmount));
                }
                //set acType
                if(dynaForm.get(AC_TYPE)==null || dynaForm.get(AC_TYPE).equals(EMPTY_STRING)) {
                    dynaForm.set(AC_TYPE,TypeConstants.UPDATE_RECORD);
                }
                //set timestamps
                dynaForm.set(AW_UPDATE_TIMESTAMP,dynaForm.get(UPDATE_TIMESTAMP));
                Timestamp dbTimestamp = prepareTimeStamp();
                dynaForm.set(UPDATE_TIMESTAMP,dbTimestamp.toString());
                webTxnBean.getResults(request, UPDATE_ARRA_VENDORS, dynaForm);
               
            }
        }
    }  
    private String removeVendor(CoeusDynaFormList coeusDynaFormList, HttpServletRequest request) throws Exception{
        String navigator = "success";
        HttpSession session = request.getSession();
        Vector vecRemovedVendors = (Vector) session.getAttribute(REMOVED_VENDORS);
        if (vecRemovedVendors == null){
            vecRemovedVendors= new Vector();
        }
        List arLstVendorData = coeusDynaFormList.getList();
        String vendorIndex = request.getParameter("vendorIndex");
        if(arLstVendorData !=null && vendorIndex !=null && !vendorIndex.equals(EMPTY_STRING)){
            int vendorRow = Integer.parseInt(vendorIndex);
            if( arLstVendorData.size() >vendorRow ){
                DynaActionForm dynaActionForm = (DynaActionForm) arLstVendorData.get(vendorRow);
                String acType = (String)dynaActionForm.get(AC_TYPE);
                if(!TypeConstants.INSERT_RECORD.equals(acType)){
                    dynaActionForm.set(AC_TYPE,TypeConstants.DELETE_RECORD);
                    vecRemovedVendors.addElement(dynaActionForm);
                }
                arLstVendorData.remove(vendorRow);
            }
        }
        coeusDynaFormList.setList(arLstVendorData);
        session.setAttribute(REMOVED_VENDORS, vecRemovedVendors);
        session.setAttribute(DYNA_BEAN_LIST,coeusDynaFormList);
        request.setAttribute(VENDOR_UPDATED,"Y");
        return navigator;
    }
    private void deleteVendors( HttpServletRequest request) throws Exception{
        HttpSession session = request.getSession();
        Vector vecRemovedVendors = (Vector) session.getAttribute(REMOVED_VENDORS);
        if(vecRemovedVendors!=null && !vecRemovedVendors.isEmpty()){
            WebTxnBean webTxnBean = new WebTxnBean();
            DynaActionForm dynaForm;
            for(int i=0;i<vecRemovedVendors.size();i++){
                dynaForm = (DynaActionForm)vecRemovedVendors.get(i);
                if(TypeConstants.DELETE_RECORD.equals(dynaForm.get(AC_TYPE))){
                    webTxnBean.getResults(request, UPDATE_ARRA_VENDORS , dynaForm);
                }
                vecRemovedVendors.remove(i--);
            }
        }
    }
     private String addArraVendor(CoeusDynaFormList coeusDynaFormList, HttpServletRequest request) throws Exception{
        String navigator = "success";
        List arLstVendorData = coeusDynaFormList.getList();
        DynaActionForm dynaFormData = coeusDynaFormList.getDynaForm(request,"arraAwardVendorsForm");
        DynaBean dynaNewBean = ((DynaBean)dynaFormData).getDynaClass().newInstance();
        HttpSession session = request.getSession();
        String reportNo =  (String)session.getAttribute(ARRA_REPORT_NUMBER);
        String mitAwardNo = (String)session.getAttribute(ARRA_REPORT_AWARD_NUMBER);
        dynaNewBean.set("arraReportNumber",new Integer(reportNo));
        dynaNewBean.set("mitAwardNumber",mitAwardNo);
        dynaNewBean.set("versionNumber",session.getAttribute(ARRA_REPORT_VERSION));
        dynaNewBean.set(AC_TYPE, TypeConstants.INSERT_RECORD);
        Timestamp prepareTimestamp = prepareTimeStamp();
        dynaNewBean.set(UPDATE_TIMESTAMP,prepareTimestamp.toString());
        if(arLstVendorData == null){
            arLstVendorData  = new Vector();
        }
        arLstVendorData.add(dynaNewBean);
        coeusDynaFormList.setList(arLstVendorData);
        session.setAttribute(DYNA_BEAN_LIST,coeusDynaFormList);
        request.setAttribute(VENDOR_UPDATED,"Y");
        return navigator;
    }
     private String getVendors(CoeusDynaFormList coeusDynaFormList, HttpServletRequest request) throws CoeusException, DBException, Exception{
          String navigator =EMPTY_STRING;
          HttpSession session = request.getSession();
          ArraAuthorization arraAuthorization = new ArraAuthorization();
          ArraReportTxnBean arraReprotTxnBean = new  ArraReportTxnBean();
          String reportNo =  (String)session.getAttribute(ARRA_REPORT_NUMBER);
          String mitAwardNo = (String)session.getAttribute(ARRA_REPORT_AWARD_NUMBER);
          Integer versionNumber = (Integer)session.getAttribute(ARRA_REPORT_VERSION);
          UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute("user"+session.getId());
          boolean canEditAll = arraAuthorization.canModifyAllArraReportFields(userInfoBean.getUserId(),mitAwardNo,versionNumber.intValue(), new Integer(reportNo).intValue());
          session.setAttribute(EDIT_COLUMN_PROPERTIES_RIGHT,new Boolean(canEditAll));
          HashMap vendorColumnProperties = arraReprotTxnBean.checkArraColumnProperties(ARRA_REPORT_VENDORS_TABLE_NAME);
          if(vendorColumnProperties != null && vendorColumnProperties.size()>0){
              session.setAttribute(ARRA_REPORT_VENDORS_COLUMN_PROPERTIES,vendorColumnProperties);
          }
          Vector vctVendors = getVendors(reportNo,mitAwardNo,null,request);
          coeusDynaFormList.setList(vctVendors);
          session.removeAttribute(REMOVED_VENDORS);
          session.setAttribute(DYNA_BEAN_LIST,coeusDynaFormList);
          
          
          
          navigator = SUCCESS;
          return navigator;
     }
}
