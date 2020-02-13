/*
 * S2SXMLData.java
 *
 * Created on October 19, 2004, 4:54 PM
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.s2s.v2;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;

import javax.activation.DataHandler;

import org.apache.soap.util.mime.ByteArrayDataSource;

import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.s2s.Attachment;
import edu.mit.coeus.s2s.SubmissionEngine;
import edu.mit.coeus.s2s.bean.IS2SSubmissionData;
import edu.mit.coeus.s2s.bean.S2SHeader;
import edu.mit.coeus.s2s.bean.S2SSubmissionDataTxnBean;
import edu.mit.coeus.s2s.bean.SubmissionDetailInfoBean;
import edu.mit.coeus.s2s.util.Converter;
import edu.mit.coeus.s2s.v2.util.S2SStub;
import edu.mit.coeus.s2s.validator.S2SValidationException;
import edu.mit.coeus.s2s.validator.UniqueSchemaNotFoundException;
import edu.mit.coeus.utils.S2SConstants;
import edu.mit.coeus.utils.UtilFactory;
import gov.grants.apply.services.applicantwebservices_v2.ApplicantWebServicesPortType;
import gov.grants.apply.services.applicantwebservices_v2.SubmitApplicationRequest;
import gov.grants.apply.services.applicantwebservices_v2.SubmitApplicationResponse;
import gov.grants.apply.system.header_v1.GrantSubmissionHeaderType;
//import gov.grants.apply.system.footer_v1.GrantSubmissionFooterType;

/**
 *
 * @author  Geo Thomas
 */
public class SubmissionEngineV2 extends SubmissionEngine{
	
    public SubmitApplicationResponse submitApplication(S2SHeader headerParam) 
                    throws S2SValidationException,CoeusException{
    	
        SubmitApplicationRequest submitRequest = new SubmitApplicationRequest();
        try{
            ApplicantWebServicesPortType stub = new S2SStub().getApplicantSoapStub(headerParam.getSubmissionTitle());
        
            S2SSubmissionDataTxnBean subTxnBean = new S2SSubmissionDataTxnBean();
            subTxnBean.setUserId(getLoggedInUser());
            IS2SSubmissionData xmlInfoBean = subTxnBean.getSubmissionData(headerParam);
            GrantSubmissionHeaderType header = xmlInfoBean.getHeader();
            String appXml = validateData(headerParam);
            ArrayList attachments = (ArrayList)getAttachments();
            
            submitRequest.setGrantApplicationXML(appXml);
            int size = attachments==null?0:attachments.size();
            for(int i=0;i<size;i++){
                Attachment attBean  = (Attachment)attachments.get(i);
                attach(submitRequest, attBean);
            }

            /*
             *Preparing SubmissionDetailInfoBean for updating the database
             */
            SubmissionDetailInfoBean subInfoBean = new SubmissionDetailInfoBean();
            if(header != null){
                subInfoBean.setSubmissionTitle(header.getSubmissionTitle());
            }            
            subInfoBean.setAcType('I');
            subInfoBean.setStatus(coeusMessageResourcesBean.parseMessageKey("exceptionCode.90006"));
            subInfoBean.setComments(coeusMessageResourcesBean.parseMessageKey("exceptionCode.90007"));
            subInfoBean.setApplicationData(appXml);
            subInfoBean.setAttachments(attachments);
            if(subTxnBean.addUpdDeleteSubmissionDetails(subInfoBean)){
                UtilFactory.log("S2S info have been updated to database successfully");
            }
            subInfoBean.setAttachments(null);
            SubmitApplicationResponse wsResponse = null;
            try{
                wsResponse = stub.submitApplication(submitRequest);
            }catch(Exception ex){
                subInfoBean = (SubmissionDetailInfoBean)subTxnBean.getSubmissionDetails(headerParam);
                subInfoBean.setAcType('U');
                subInfoBean.setStatus(coeusMessageResourcesBean.parseMessageKey("exceptionCode.90011"));
                String errMsg = coeusMessageResourcesBean.parseMessageKey("exceptionCode.90008") +
                                       "Route Cause::"+ex.getMessage();
                subInfoBean.setComments(errMsg);
                //Updating the error message to the database
                if(subTxnBean.addUpdDeleteSubmissionDetails(subInfoBean)){
                    UtilFactory.log("Error message logged into table");
                }
                UtilFactory.log(loggedInUser,ex,this.getClass().getName(), "submitApplication");
                throw new CoeusException(errMsg);
            }
            //update the status to database
            subInfoBean = (SubmissionDetailInfoBean)subTxnBean.getSubmissionDetails(headerParam);
            subInfoBean.setStatus(coeusMessageResourcesBean.parseMessageKey("exceptionCode.90009"));
            subInfoBean.setComments(coeusMessageResourcesBean.parseMessageKey("exceptionCode.90010"));
            subInfoBean.setGrantsGovTrackingNumber(
                        wsResponse.getGrantsGovTrackingNumber());
            subInfoBean.setAcType('U');
            subInfoBean.setAwProposalNumber(subInfoBean.getSubmissionTitle());
            Calendar ggRecDateCal = wsResponse.getReceivedDateTime().toGregorianCalendar();
            if(ggRecDateCal!=null){
                ggRecDateCal.setTimeZone(TimeZone.getTimeZone(S2SConstants.EST_TIMEZONE_ID));
                subInfoBean.setReceivedDateTime(Converter.convertCal2Timestamp(ggRecDateCal));
            }
            subInfoBean.setUpdateUser(loggedInUser);
            if(subTxnBean.addUpdDeleteSubmissionDetails(subInfoBean)){
                UtilFactory.log("S2S info have been updated to database successfully");
            }
            
            return wsResponse;
        }catch(UniqueSchemaNotFoundException uEx){
            throw uEx;
        }catch(S2SValidationException sEx){
            throw sEx;
        }catch(CoeusException ex){
            throw ex;
        }catch(Exception ex){
            UtilFactory.log(ex.getMessage(),ex,"SubmissionEngine","submitApplication");
            throw new CoeusException(ex.getMessage());
        }
    }
    private void attach(SubmitApplicationRequest submitRequest, Attachment attBean){
        if(attBean==null)
            return;
        gov.grants.apply.system.grantscommonelements_v1.Attachment attachmentPart = new gov.grants.apply.system.grantscommonelements_v1.Attachment();
        DataHandler attachmentFile = new DataHandler(new ByteArrayDataSource(
                                            attBean.getContent(),
                                            attBean.getContentType()));
        /**
         * Tell the stub that the message being formed also contains an
         * attachment, and it is of type MIME encoding.
         */
//        stub.setProperty(Call.ATTACHMENT_ENCAPSULATION_FORMAT,
//                            Call.ATTACHMENT_ENCAPSULATION_FORMAT_MIME);
        attachmentPart.setFileDataHandler(attachmentFile);
        attachmentPart.setFileContentId(attBean.getContentId());
        submitRequest.getAttachment().add(attachmentPart);
    }
}
