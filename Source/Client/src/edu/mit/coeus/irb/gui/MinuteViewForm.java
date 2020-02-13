/*
 * @(#)MinuteViewForm.java
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 */
package edu.mit.coeus.irb.gui;

import edu.mit.coeus.irb.bean.ScheduleAttachmentBean;
import edu.mit.coeus.irb.bean.ScheduleDetailsBean;
import javax.swing.JTable;
import edu.mit.coeus.utils.*;
import edu.mit.coeus.brokers.*;
import java.util.Vector;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.utils.document.DocumentBean;
import edu.mit.coeus.utils.document.DocumentConstants;
import java.util.HashMap;
import java.util.Map;
/**
 * This class is intended to Generate a Response window with ComitteeID/Committee
 * Name and Set of all Active Versions of Minutes for a specific Schedule. The user
 * is privlaged to select a particular Minute Version and can view 
 * the PDF report pertaining to that version.
 *
 * @author  ravikanth
 * Created on July 23, 2003, 11:18 AM
 */
public class MinuteViewForm extends AbstractReportViewForm{

    private CoeusMessageResources messageResources;
    private String scheduleID,minuteID;
    private Vector receivedParams;
    //holds report table.
    private JTable tblReport;
    private static final char MINUTE_MAIL = 'K';
    //COEUSQA:3333 - IRB and IACUC - Ability to Add Attachments to Minutes and Agenda - Start
    private static final int SCHEDULE_AGENDA_ATTACHMENTS = 1;
    private static final int SCHEDULE_MINITES_ATTACHMENTS = 2;
    private static final char GET_SCHEDULE_ATTAHMENTS = 'P';
    private static final String SCHEDULE_MAINTENANCE_SERVLET = "/scheduleMaintSrvlt";
    private static final String ADD_BOOKMARK = "yes";
    //COEUSQA:3333 - End
    /** Creates new form MinuteViewForm 
     */
    public MinuteViewForm(Vector params, boolean modal) {
        super(params, modal );
        initDetails(params);
    }
    
    public MinuteViewForm(Vector params){
        super(params);
        initDetails(params);
    }
    
    private void initDetails(Vector params){
        setTitle("View Minutes");
        receivedParams = params;
        if( receivedParams != null && receivedParams.size() >= 3 ) {
            scheduleID = (String) receivedParams.elementAt( 0 );
        }
        messageResources = CoeusMessageResources.getInstance();
        
    }

    
    /**
     * Method to retrieve all the report version details of a given schedule
     * @return Vector of all the details for each version of Minute report
     */
    public Vector getAllReportDetails() throws Exception{
        String connURL = CoeusGuiConstants.CONNECTION_URL +
                                                     "/MinuteServlet";
        //Vector minuteData  = new Vector(3,2);
        Vector dataObject  = new Vector(3,2);
        RequesterBean request = new RequesterBean();
        request.setFunctionType('A');
        request.setId( scheduleID );
        request.setDataObject(request);

        AppletServletCommunicator comm = new AppletServletCommunicator(
                                                    connURL, request );
        comm.send();
        ResponderBean response = comm.getResponse();
        if ( response.isSuccessfulResponse() ){
            dataObject = (Vector)response.getDataObject();
            if( dataObject == null || dataObject.size() == 0 ) {
                throw new Exception(messageResources.parseMessageKey(
                    "minuteViewFrm_exceptionCode.0724"));
            }
        }else{
            if(response.getDataObject() != null){
                Object obj = response.getDataObject();
                if(obj instanceof CoeusException){
                    throw (CoeusException)response.getDataObject();
                }
            }else{
                throw new Exception(response.getMessage());
            }
        }

        return dataObject;
    }
    /**
     * Method used to get the context name of the selected version of the Minute report
     * @return String representing the context name of the respective pdf file.
     */
    public String getSpecificReportContextName() throws Exception{
//        String connURL = CoeusGuiConstants.CONNECTION_URL + "/MinuteServlet";
        String connURL = CoeusGuiConstants.CONNECTION_URL + "/StreamingServlet";
        String newPDFURL  = null;
        tblReport = getTableRef();
        if( tblReport != null && tblReport.getSelectedRow() != -1 ) {
            int selRow = tblReport.getSelectedRow();
            minuteID = (String) tblReport.getValueAt( selRow, 0 );
        }
        RequesterBean request = new RequesterBean();
//        request.setFunctionType('V');
//        request.setId( scheduleID );
//        Vector param = new Vector();
//        param.add(  minuteID );
//        request.setDataObjects( param );
//        request.setDataObject( request );
        
        //For Streaming
        DocumentBean documentBean = new DocumentBean();
        Map map = new HashMap();
        map.put("DOCUMENT_TYPE", "VIEW_MINUTES");
        map.put("USER_ID", CoeusGuiConstants.getMDIForm().getUserId());
        map.put("SCHEDULE_ID", scheduleID);
        map.put("MINUTE_ID", minuteID);
        map.put(DocumentConstants.READER_CLASS, "edu.mit.coeus.irb.ScheduleDocumentReader");        
        documentBean.setParameterMap(map);
        request.setDataObject(documentBean);
        request.setFunctionType(DocumentConstants.GENERATE_STREAM_URL);
        //For Streaming
        
        AppletServletCommunicator comm = new AppletServletCommunicator(connURL, request );
        comm.send();
        ResponderBean response = comm.getResponse();

        if ( response.isSuccessfulResponse() ){
//            Vector data = (Vector)response.getDataObject();
//            newPDFURL = (String)data.get( 0 );
            map = (Map)response.getDataObject();
            newPDFURL = (String)map.get(DocumentConstants.DOCUMENT_URL);
            newPDFURL = newPDFURL.replace('\\', '/') ; // this is fix for Mac
        }else{
            throw new Exception(response.getMessage());
        }

        return newPDFURL;

 
    }
    
    protected void showMailForm(Vector data) {
        
        int row = ((JTable)getTblVersions()).getSelectedRow();
        String attId = null;
        if(row != -1){
            attId = (String)((JTable)getTblVersions()).getValueAt(row,0);
        }else{
            System.out.println("Selected Row is -1");
            //Added for the case# COEUSDEV-219- send Functionality for agenda-start
            CoeusOptionPane.showInfoDialog(messageResources.parseMessageKey(
                    "abstractReportViewFrm_exceptionCode.1004"));
            //Added for the case# COEUSDEV-219- send Functionality for agenda-start
        }
        if(attId != null && attId.trim().length() > 0){
            
            MailForm mailForm = new MailForm('M', data);
            mailForm.setScheduleId( getScheduleID() );
            mailForm.setAttachId( attId );
            mailForm.setMailFunctionalityCode(MINUTE_MAIL);
            mailForm.showMailInfo();
        }else{
            System.out.println("ERROR PLEASE CHECK");
        }
    }
    
    //COEUSQA:3333 - Start
    /**
     * Method to get PDF Attachments for Schedule ID
     * @param funcType
     * @throws edu.mit.coeus.exception.CoeusException
     * @return
     */
    public int getSchedulePDFAttachmets()
    throws CoeusException {
        int attachCount = 0;
        RequesterBean requesterBean = new RequesterBean();
        ScheduleAttachmentBean scheduleAttachmentBean = new ScheduleAttachmentBean();
        scheduleAttachmentBean.setScheduleId(scheduleID);
        scheduleAttachmentBean.setAttachmentTypeCode(SCHEDULE_MINITES_ATTACHMENTS);        
        scheduleAttachmentBean.setMimeType("application/pdf");
        requesterBean.setDataObject(scheduleAttachmentBean);
        requesterBean.setFunctionType(GET_SCHEDULE_ATTAHMENTS);
        
        AppletServletCommunicator comm = new AppletServletCommunicator(
                CoeusGuiConstants.CONNECTION_URL + SCHEDULE_MAINTENANCE_SERVLET,
                requesterBean);
        comm.send();
        ResponderBean responderBean = comm.getResponse();
        if(!responderBean.isSuccessfulResponse()){
            throw new CoeusException(responderBean.getMessage(),0);
        }
        attachCount = (Integer)responderBean.getDataObject();
        return attachCount;
    }
    
    /**
     * Method to get confirmation to add PDF as book
     * @return
     */
    public boolean attachBookMark() {
        int selectedOption = CoeusOptionPane.showQuestionDialog(
                messageResources.parseMessageKey("commSchdDetFrm_addPDFAsBookmarkConfirmCode.1030"),
                CoeusOptionPane.OPTION_YES_NO,CoeusOptionPane.DEFAULT_NO);
        
        if(selectedOption == CoeusOptionPane.SELECTION_YES){
            return true;
        }
        return false;
    }
    //COEUSQA:3333 - End
}