/*
 * ProcessGrantsSubmission.java
 *
 * Created on January 7, 2005, 10:42 AM
 */

package edu.mit.coeus.s2s.bean;

import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.propdev.bean.ProposalDevelopmentFormBean;
import edu.mit.coeus.s2s.bean.OpportunityInfoBean;
import edu.mit.coeus.s2s.bean.S2SSubmissionStatusBean;
//import edu.mit.coeus.s2s.bean.S2SXMLInfoBean;
import edu.mit.coeus.s2s.controller.OpportunitySelectionController;
import edu.mit.coeus.s2s.controller.S2SSubmissionDetailController;
import edu.mit.coeus.s2s.gui.IGrantsSubmission;
import edu.mit.coeus.s2s.gui.S2SSubmissionDetailForm;
import edu.mit.coeus.s2s.validator.FormException;
import edu.mit.coeus.s2s.validator.S2SValidationException;
import edu.mit.coeus.s2s.validator.UniqueSchemaNotFoundException;
import edu.mit.coeus.utils.AppletServletCommunicator;
import edu.mit.coeus.utils.BaseWindowObservable;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.utils.ObjectCloner;
import edu.mit.coeus.utils.S2SConstants;
import edu.mit.coeus.utils.document.DocumentBean;
import edu.mit.coeus.utils.document.DocumentConstants;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Vector;

/**
 *
 * @author  geot
 */
public class ProcessGrantsSubmission implements IGrantsSubmission{
    private static final String s2sServlet = CoeusGuiConstants.CONNECTION_URL+
                                                            "/S2SServlet";
    private AppletServletCommunicator comm;
    private S2SSubmissionDetailController s2sSubController;
    private S2SHeader headerParam,propHeader;
    private char functionType;
    private CoeusMessageResources coeusMessageResources;
    private BaseWindowObservable observable;
    private ProposalDevelopmentFormBean propDevBean;
    private char invokeType;
    
    /** Creates a new instance of ProcessGrantsSubmission */
    public ProcessGrantsSubmission() {
        this(null);
    }

    /** Creates a new instance of ProcessGrantsSubmission */
    public ProcessGrantsSubmission(S2SHeader objParams) {
        this.headerParam = objParams;
        coeusMessageResources = CoeusMessageResources.getInstance();
        try{
            this.propHeader = (S2SHeader)ObjectCloner.deepCopy(objParams);
        }catch(Exception ex){
            ex.printStackTrace();
            CoeusOptionPane.showErrorDialog(ex.getMessage());
            return;
        }
        comm = new AppletServletCommunicator();
        comm.setConnectTo(s2sServlet);
    }

    private boolean isS2SCandidate() throws CoeusException{
        RequesterBean request = new RequesterBean();
        request.setDataObject(headerParam.getSubmissionTitle());
        request.setFunctionType(S2SConstants.IS_S2S_CANDIDATE);
        comm.setRequest(request);
        comm.send();
        ResponderBean response = comm.getResponse();
        if(response.isSuccessfulResponse()){
            boolean isCand = ((Boolean)response.getDataObject()).booleanValue();
            if(!isCand && getInvokeType()==S2SConstants.AUTO_SUBMISSION){
                return false;
            }
            if(isCand && (headerParam.getCfdaNumber()==null || headerParam.getCfdaNumber().trim().equals(""))
                    && (headerParam.getOpportunityId()==null || headerParam.getOpportunityId().trim().equals(""))){
                    throw new CoeusException(coeusMessageResources.parseMessageKey(
                                "s2ssubdetfrm_exceptionCode.1012"));
            }
            return isCand;
        }else{
            throw new CoeusException(response.getMessage());
        }        
    }
    public void showS2SSubmissionForm() throws CoeusException{
        if(isS2SCandidate()) showS2SSubmissionForm(headerParam);
        else if(getInvokeType()==S2SConstants.AUTO_SUBMISSION) return;
        else{
            throw new CoeusException(coeusMessageResources.parseMessageKey(
                        "s2ssubdetfrm_exceptionCode.1009"));
        }
    }
    public void refreshForm() throws Exception{
        s2sSubController.setFormData(getData());
    }
    public void showS2SSubmissionForm(S2SHeader objParams) throws CoeusException{
        if(headerParam==null){
            headerParam = objParams;
        }
        s2sSubController = new S2SSubmissionDetailController(this);
        s2sSubController.setMode(getFunctionType());
        s2sSubController.setFunctionType(getFunctionType());
        try{
            Object [] objData = getData();
            s2sSubController.setSubmissionTitle(headerParam.getSubmissionTitle());
            s2sSubController.setSponsor(headerParam.getAgency());
            OpportunityInfoBean opportunityInfoBean = (OpportunityInfoBean)objData[0];
            if(opportunityInfoBean.getOpportunityId()!=null){
                //copy database data to header param object
                headerParam.setCfdaNumber(opportunityInfoBean.getCfdaNumber());
                headerParam.setOpportunityId(opportunityInfoBean.getOpportunityId());
                headerParam.setCompetitionId(opportunityInfoBean.getCompetitionId());
            }
            s2sSubController.setFormData(objData);
            s2sSubController.display();
        }catch(UniqueSchemaNotFoundException uEx){
            ArrayList oppList = uEx.getOpportunityList();
            //show form to display all the opportunities to let the user select one
            if(oppList==null || oppList.isEmpty()){
                throw new CoeusException(coeusMessageResources.parseMessageKey(
                        "s2ssubdetfrm_exceptionCode.1010"));
            }
            showOppForm(oppList,'I',uEx.getMainError());
        }catch(Exception ex){
            ex.printStackTrace();
            throw new CoeusException(ex.getMessage());
        }
    }
    public void deleteOpportunity() throws CoeusException{
        RequesterBean request = new RequesterBean();
        DBOpportunityInfoBean dbOppBean = ((DBOpportunityInfoBean)s2sSubController.getOpportunityInfoBean());
        dbOppBean.setAcType('D');
        request.setDataObject(dbOppBean);
        request.setFunctionType(S2SConstants.DELETE_OPPORTUNITY);
        comm.setRequest(request);
        comm.send();
        ResponderBean response = comm.getResponse();
        if(response.isSuccessfulResponse()){
            //s2sSubController.performCancelAction();
            s2sSubController.dispose();
            if(propDevBean!=null){
                dbOppBean.setOpportunityId(null);
                dbOppBean.setOpportunityTitle(null);
                dbOppBean.setCfdaNumber(null);
                propDevBean.setS2sOppSelected(false);
                if(observable!=null){
                   observable.notifyObservers((OpportunityInfoBean)dbOppBean);
            	}
            }            
        }else
            throw new CoeusException(response.getMessage());
    }
    public void showS2SOppForm() throws CoeusException{
        RequesterBean request = new RequesterBean();
        request.setDataObject(propHeader);
        request.setFunctionType(S2SConstants.GET_OPPORTUNITY_LIST);
        comm.setRequest(request);
        comm.send();
        ResponderBean response = comm.getResponse();
        if(response.isSuccessfulResponse()){
            //done
            showOppForm((ArrayList)response.getDataObject(),'U');
        }else
            throw new CoeusException(response.getMessage());
    }
    public void showOppForm(ArrayList oppList,char functionType) throws CoeusException{
        showOppForm(oppList,functionType,null);
    }
    public void showOppForm(ArrayList oppList,char functionType,String errorMsg) throws CoeusException{
        if(oppList==null || oppList.isEmpty()){
            throw new CoeusException(coeusMessageResources.parseMessageKey(
                        "s2ssubdetfrm_exceptionCode.1010"));
        }
        Vector syncOppList = new Vector(oppList.size());
        syncOppList.addAll(oppList);
        int size = oppList.size();
        OpportunitySelectionController oppSelCntlr =
        new OpportunitySelectionController(CoeusGuiConstants.getMDIForm());
        oppSelCntlr.setFunctionType(functionType);
        oppSelCntlr.setErrorMessage(errorMsg);
        oppSelCntlr.setFormData(syncOppList);
        oppSelCntlr.setOppHeader(headerParam);
        oppSelCntlr.setSubmissionTitle(headerParam.getSubmissionTitle());
        if(s2sSubController!=null && s2sSubController.getOpportunityInfoBean()!=null){
            oppSelCntlr.setAwUpdateTimestamp(
                ((DBOpportunityInfoBean)s2sSubController.getOpportunityInfoBean()).
                                getUpdateTimestamp());
        }
        
        oppSelCntlr.display();
        if(oppSelCntlr.isSaveNContinue()){
//            headerParam = oppSelCntlr.getOppHeader();
            if(observable!=null){
                observable.notifyObservers(oppSelCntlr.getSltdOpportunity());
            }
            if(propDevBean!=null){
                OpportunityInfoBean oppBean = oppSelCntlr.getSltdOpportunity();
                propDevBean.setCfdaNumber(oppBean.getCfdaNumber());
                propDevBean.setProgramAnnouncementNumber(oppBean.getOpportunityId());
                propDevBean.setProgramAnnouncementTitle(oppBean.getOpportunityTitle());
                propDevBean.setS2sOppSelected(true);
            }
            switch(functionType){
                case('I'):
                    showS2SSubmissionForm(headerParam);
                    break;
                case('U'):
                    try{
                        refreshForm();
                    }catch(Exception ex){
                        throw new CoeusException(ex.getMessage());
                    }
                    break;
            }
        }
//        if(oppSelCntlr.isSaveNContinue()){
//            showS2SSubmissionForm(headerParam);
//        }
    }
    private Object[] getData () throws Exception{
        RequesterBean request = new RequesterBean();
        request.setDataObject(headerParam);
        request.setFunctionType(S2SConstants.GET_DATA);
        comm.setRequest(request);
        comm.send();
        ResponderBean response = comm.getResponse();
        if(response.isSuccessfulResponse()){
            return (Object[])response.getDataObject();
        }else{
            Exception ex = response.getException();
            if(ex==null) throw new CoeusException(response.getMessage());
            throw ex;
        }
    }
    private boolean schemaSelect;
    public void displaySubResult(S2SSubmissionStatusBean s2sSubStatusBean){
        CoeusOptionPane.showInfoDialog(s2sSubStatusBean.toString());
    }
    
    public boolean submitGrantsGov(Object data) throws Exception {
        return process(S2SConstants.SAVE_FORMS_N_SUBMIT_APP,data);
    }
    
    public boolean saveSubmissionDetails(char type, Object data) throws Exception {
        return process(type, data);
    }
    
    public boolean refreshGrantsGovData() throws Exception {
        boolean refreshFlag = process(S2SConstants.REFRESH_GRANTS_DATA,null);
        if(refreshFlag){
            String agTrackingId = ((S2SSubmissionDetailForm)s2sSubController.getControlledUI()).txtAgTrackingId.getText();
            if(!agTrackingId.equals("") && s2sSubController.getSubmissionData()!=null &&
                        propDevBean!=null && observable!=null){
                propDevBean.setSponsorProposalNumber(agTrackingId);
                observable.notifyObservers(s2sSubController.getSubmissionData());
            }
        }
        return refreshFlag;
    }
    public String printForms(Vector sltdFrmList) throws Exception{
        /* //Print Prior to Streaming
        RequesterBean request = new RequesterBean();
        request.setDataObject(headerParam);
        request.setDataObjects(sltdFrmList);
        request.setFunctionType(S2SConstants.PRINT_FORM);
        comm.setRequest(request);
        comm.send();
        ResponderBean response = comm.getResponse();
        if(response.isSuccessfulResponse()){
            return (String)response.getDataObject();
        }
        if(response.getException()!=null)
            throw response.getException();
        else
            throw new CoeusException(response.getMessage());
        */
        
        //For Streaming
        DocumentBean documentBean = new DocumentBean();
        Map map = new HashMap();
        map.put("Forms", sltdFrmList);
        map.put("S2SHeader", headerParam);
        map.put(DocumentConstants.READER_CLASS, "edu.mit.coeus.s2s.S2SDocumentReader");
        map.put(DocumentConstants.DOC_ON_URL_GENERATION, new Boolean(true));
        documentBean.setParameterMap(map);

        RequesterBean requesterBean = new RequesterBean();
        requesterBean.setDataObject(documentBean);
        requesterBean.setFunctionType(DocumentConstants.GENERATE_STREAM_URL);

        AppletServletCommunicator appletServletCommunicator = new AppletServletCommunicator();
        appletServletCommunicator.setConnectTo(CoeusGuiConstants.CONNECTION_URL + "/StreamingServlet");
        appletServletCommunicator.setRequest(requesterBean);
        appletServletCommunicator.send();
        ResponderBean responderBean = appletServletCommunicator.getResponse();

        Object data = null;
        String url = null;
        if(responderBean.isSuccessfulResponse()) {
            data = responderBean.getDataObject();
            if(data != null && data instanceof Map) {
               map = (Map)data;
               url = (String)map.get(DocumentConstants.DOCUMENT_URL);
               //URLOpener.openUrl(url);
               return url;
            }else {
                throw new CoeusException("Server returned no Data");
            }
        }
        if(responderBean.getException()!=null) {
            throw responderBean.getException();
        }else {
            throw new CoeusException(responderBean.getMessage());
        }
    }
    
    private boolean process(char functionType,Object data) throws Exception{
        RequesterBean request = new RequesterBean();
        request.setDataObjects((Vector)data);
        request.setDataObject(headerParam);
        request.setFunctionType(functionType);
        comm.setRequest(request);
        comm.send();
        ResponderBean response = comm.getResponse();
        if(response.isSuccessfulResponse()){
            //done
            if(s2sSubController!=null) s2sSubController.setFormData(response.getDataObject());
            return true;
        }
        if(response.getException()!=null)
            throw response.getException();
        else
            throw new CoeusException(response.getMessage());
    }
    public boolean validateApplication(Object data) throws Exception {
        return process(S2SConstants.VALIDATE_APPLICATION,data);
    }
    
    /**
     * Getter for property functionType.
     * @return Value of property functionType.
     */
    public char getFunctionType() {
        return functionType;
    }
    
    /**
     * Setter for property functionType.
     * @param functionType New value of property functionType.
     */
    public void setFunctionType(char functionType) {
        this.functionType = functionType;
    }
    
    /**
     * Getter for property headerParam.
     * @return Value of property headerParam.
     */
    public edu.mit.coeus.s2s.bean.S2SHeader getHeaderParam() {
        return headerParam;
    }
    
    /**
     * Setter for property headerParam.
     * @param headerParam New value of property headerParam.
     */
    public void setHeaderParam(edu.mit.coeus.s2s.bean.S2SHeader headerParam) {
        this.headerParam = headerParam;
    }
    
    public Object getStatusDetail(String grantsTrackingId) throws Exception{
        RequesterBean request = new RequesterBean();
        Vector data = new Vector(2);
        data.add(grantsTrackingId);
        data.add(headerParam.getSubmissionTitle());
        request.setDataObjects(data);
        request.setDataObject(grantsTrackingId);
        request.setFunctionType(S2SConstants.GET_STATUS_DETAIL);
        comm.setRequest(request);
        comm.send();
        ResponderBean response = comm.getResponse();
        if(response.isSuccessfulResponse()){
            return response.getDataObject();
        }else{
            Exception ex = response.getException();
            if(ex==null) throw new CoeusException(response.getMessage());
            throw ex;
        }
    }
    
    /**
     * Getter for property observable.
     * @return Value of property observable.
     */
    public edu.mit.coeus.utils.BaseWindowObservable getObservable() {
        return observable;
    }
    
    /**
     * Setter for property observable.
     * @param observable New value of property observable.
     */
    public void setObservable(edu.mit.coeus.utils.BaseWindowObservable observable) {
        this.observable = observable;
    }
    
    /**
     * Getter for property propDevBean.
     * @return Value of property propDevBean.
     */
    public edu.mit.coeus.propdev.bean.ProposalDevelopmentFormBean getPropDevBean() {
        return propDevBean;
    }
    
    /**
     * Setter for property propDevBean.
     * @param propDevBean New value of property propDevBean.
     */
    public void setPropDevBean(edu.mit.coeus.propdev.bean.ProposalDevelopmentFormBean propDevBean) {
        this.propDevBean = propDevBean;
    }
    
    /**
     * Getter for property invokeType.
     * @return Value of property invokeType.
     */
    public char getInvokeType() {
        return invokeType;
    }
    
    /**
     * Setter for property invokeType.
     * @param invokeType New value of property invokeType.
     */
    public void setInvokeType(char invokeType) {
        this.invokeType = invokeType;
    }
    
}
