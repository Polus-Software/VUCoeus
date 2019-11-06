/*
 * @(#)NegotiationAction.java 1.0 July 6, 2009, 10:59 AM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeuslite.negotiation.action;

import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.negotiation.bean.NegotiationHeaderBean;
import edu.mit.coeus.negotiation.bean.NegotiationInfoBean;
import edu.mit.coeus.negotiation.bean.NegotiationTxnBean;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeuslite.utils.CoeusliteMenuItems;
import edu.mit.coeus.utils.DateUtils;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeuslite.utils.bean.WebTxnBean;
import java.io.IOException;



import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import org.apache.struts.validator.DynaValidatorForm;


/**
 *
 * @author satheeshkumarkn
 * @version
 */
public class NegotiationAction extends NegotiationBaseAction {
    
    private static final String EMPTY_STRING = "";
    private static final String NEGOTIATION_NUMBER_PARAM = "NEGOTIATION_NUMBER";
    private static final String NEGOTIATION_NUMBER = "negotiationNumber";
    private static final String NEGOTIATION_HEADER_BEAN = "negotiationHeaderBean";
    private static final String NEGOTIATION_LOCATIONS = "negotiationLocations";
    private static final String NEGOTIATION_LOCATION_HISTORY = "negotiationLocationHistory";
    private static final String VIEW_NEGOTIATION = "/viewNegotiation";
    private static final String ACTION_SUCCESS = "success";
    private static final String SIMPLE_DATE_FORMAT = "MM/dd/yyyy";
    private static final String EFFECTIVE_DATE = "effectiveDate";
    private static final String NUMBER_OF_DAYS = "numberOfDays";
    
    private DateUtils dateUtils;
    private String effectiveDateStr,effectiveDateNextStr;
    private Date effectiveDate,effectiveDateNext;
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(SIMPLE_DATE_FORMAT);
    public NegotiationAction() {
    }
    
    public ActionForward performExecuteNegotiation(ActionMapping actionMapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception{
        HttpSession session = request.getSession();
        String navigator = EMPTY_STRING;
        DynaValidatorForm dynaForm = (DynaValidatorForm)form;
        String negotiationNumber = (String)request.getParameter(NEGOTIATION_NUMBER_PARAM);
        if(actionMapping.getPath().equals(VIEW_NEGOTIATION)){
            getNegotiationHeaderDetails(negotiationNumber,request);
            getNegotiationMenus(request);
            navigator = getNegotiationDetails(negotiationNumber,dynaForm,request);
            
        }
        setSelectedStatusMenu(CoeusliteMenuItems.NEGOTIATION_DETAILS_MENU,session);
        return actionMapping.findForward(navigator);
    }
    /**This method is used to get the Negotiation Details form DB
     *@param request - HttpServletRequestObject.
     *@param dynaForm - Contains negotiationInfoForm
     *@param negotiationNumber - Get negotiation number from request
     *@return navigator
     */
    private String getNegotiationDetails(String negotiationNumber, DynaValidatorForm dynaForm, HttpServletRequest request)throws Exception {
        String navigator = EMPTY_STRING;
        String effDateStr = EMPTY_STRING;
        dateUtils =  new DateUtils();
        Date date_today =  new Date();
        String noOfDays  =EMPTY_STRING;
        NegotiationTxnBean negotiationTxnBean = new NegotiationTxnBean();
        HttpSession session = request.getSession();
        NegotiationInfoBean negotiationInfoBean = negotiationTxnBean.getNegotiationInfo(negotiationNumber);
        
        NegotiationHeaderBean negotiationHeaderBean = (NegotiationHeaderBean)session.getAttribute(NEGOTIATION_HEADER_BEAN);
        dynaForm.set("initialContractAdmin",negotiationHeaderBean.getInitialContractAdmin());
        dynaForm.set("proposalTypeDescription",negotiationHeaderBean.getProposalTypeDescription());
        dynaForm.set("title",negotiationHeaderBean.getTitle());
        if(negotiationHeaderBean.getPrimeSponsorCode() != null && negotiationHeaderBean.getPrimeSponsorCode().trim().length()>0){
            negotiationInfoBean.setPrimeSponsorCode(negotiationHeaderBean.getPrimeSponsorCode().trim());
            negotiationInfoBean.setPrimeSponsorName(negotiationHeaderBean.getPrimeSponsorName().trim());
        }else if(negotiationInfoBean.getPrimeSponsorCode() != null && negotiationInfoBean.getPrimeSponsorCode().trim().length()>0){
            negotiationInfoBean.setPrimeSponsorCode(negotiationInfoBean.getPrimeSponsorCode().trim());
            negotiationInfoBean.setPrimeSponsorName(negotiationInfoBean.getPrimeSponsorName().trim());
        }
//      dynaForm.set("primeSponsorCode",negotiationHeaderBean.getPrimeSponsorCode());
//      dynaForm.set("primeSponsorName",negotiationHeaderBean.getPrimeSponsorName());
        BeanUtilsBean copyBean = new BeanUtilsBean();
        copyBean.copyProperties(dynaForm,negotiationInfoBean);
        
        
        Vector cvNegotiationLocation = getNegotiationLastLocation(negotiationNumber,request);
        if(cvNegotiationLocation != null && cvNegotiationLocation.size()>0){
            DynaValidatorForm locationdynaForm = (DynaValidatorForm)cvNegotiationLocation.get(0);
            effDateStr = (String) locationdynaForm.get(EFFECTIVE_DATE);
            String strDatesimple = dateUtils.formatDate(effDateStr,SIMPLE_DATE_FORMAT);
            Date effDate = simpleDateFormat.parse(strDatesimple);
            if(effDateStr != null){
                if (effDate.after(date_today)){
                    noOfDays = EMPTY_STRING;
                }else{
                    int days =dateUtils.dateDifference(date_today,effDate);
                    noOfDays = new Integer(days).toString();
                }
            }
            locationdynaForm.set(NUMBER_OF_DAYS,noOfDays);
        }
        Vector cvNegotiationLocHis = getNegotiationLocationHistory(negotiationNumber,request);
        request.setAttribute(NEGOTIATION_LOCATIONS,cvNegotiationLocation);
        request.setAttribute(NEGOTIATION_LOCATION_HISTORY,cvNegotiationLocHis);
        
        navigator = ACTION_SUCCESS;
        return navigator;
    }
    /**This method is used to get the Negotiation Location History Details form DB
     *@param request - HttpServletRequestObject.
     *@param negotiationNumber - Get negotiation number from request
     *@return Vector of Negotiation Location history details
     */
    private Vector getNegotiationLocationHistory(String negotiationNumber, HttpServletRequest request) throws IOException, CoeusException, DBException, Exception {
        Vector vctDetails = null;
        try {
            if(negotiationNumber!=null && !EMPTY_STRING.equals(negotiationNumber.trim())){
                WebTxnBean webTxnBean = new WebTxnBean();
                Hashtable htLocData = new Hashtable();
                htLocData.put(NEGOTIATION_NUMBER, negotiationNumber );
                htLocData = (Hashtable)webTxnBean.getResults(request,"getNegotiationLocationHistory",htLocData);
                vctDetails =  (Vector)htLocData.get("getNegotiationLocationHistory");
                if(vctDetails != null && vctDetails.size()>0){
                    for(int i = 0; i < vctDetails.size(); i++){
                        DynaValidatorForm negotiationLocationForm = (DynaValidatorForm)vctDetails.get(i);
    //                if(negotiationLocationForm.get("updateTimestamp") != null){
    //                    String updateTimeStamp =(String)negotiationLocationForm.get("updateTimestamp");
    //                    updateTimeStamp = dateUtils.formatDate(updateTimeStamp,SIMPLE_DATE_FORMAT);
    //                    negotiationLocationForm.set("updateTimestamp",updateTimeStamp.toString());
    //                }
                        effectiveDateStr = (String) negotiationLocationForm.get(EFFECTIVE_DATE);
                        String strDatesimple = dateUtils.formatDate(effectiveDateStr,SIMPLE_DATE_FORMAT);
                        effectiveDate = simpleDateFormat.parse(strDatesimple);
                        if(effectiveDateStr != null){
                            if(i == vctDetails.size() -1){
                                effectiveDateNext = new Date();
                            }else{
                                DynaValidatorForm negotiationLocationBeanNext = (DynaValidatorForm)vctDetails.get(i + 1);
                                effectiveDateNextStr = (String) negotiationLocationBeanNext.get(EFFECTIVE_DATE);
                                String strDateNextsimple = dateUtils.formatDate(effectiveDateNextStr,SIMPLE_DATE_FORMAT);
                                effectiveDateNext =  simpleDateFormat.parse(strDateNextsimple);
                            }
                            if(effectiveDate.after(effectiveDateNext)) {
                                negotiationLocationForm.set(NUMBER_OF_DAYS,EMPTY_STRING);
                            }else {
                                int dateDiff = dateUtils.dateDifference(effectiveDateNext, effectiveDate);
                                negotiationLocationForm.set(NUMBER_OF_DAYS,new Integer(dateDiff).toString());
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) {
            UtilFactory.log("FROM COEUS LITE : "+ex.getMessage(),ex,"NegotiationAction","getNegotiationLocationHistory");
        }
        return vctDetails;
    }
    /**This method is used to get the Negotiation Last updated Location Details form DB
     *@param request - HttpServletRequestObject.
     *@param negotiationNumber - Get negotiation number from request
     *@return Vector of Negotiation last Location details
     */
    private Vector getNegotiationLastLocation(String negotiationNumber,HttpServletRequest request) throws IOException, CoeusException, DBException, Exception {
        Vector vctNegLocDetails =null;
        try {
        if(negotiationNumber!=null && !EMPTY_STRING.equals(negotiationNumber.trim())){
            WebTxnBean webTxnBean = new WebTxnBean();
            Hashtable htLastLocData = new Hashtable();
            htLastLocData.put(NEGOTIATION_NUMBER, negotiationNumber );
            htLastLocData = (Hashtable)webTxnBean.getResults(request,"getNegotiationLastLocation",htLastLocData);
            vctNegLocDetails =  (Vector)htLastLocData.get("getNegotiationLastLocation");
        }
        } catch(Exception ex) {
            UtilFactory.log("FROM COEUS LITE : "+ex.getMessage(),ex,"NegotiationAction","getNegotiationLastLocation");
        }
        return vctNegLocDetails;
    }
    
    
    
}
