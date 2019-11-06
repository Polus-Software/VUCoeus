/*
 * S2SXMLData.java
 *
 * Created on October 19, 2004, 4:54 PM
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.s2s;

import de.fzi.dbs.verification.ObjectVerifier;
import de.fzi.dbs.verification.event.VerificationEventLocator;
import edu.mit.coeus.bean.CoeusMessageResourcesBean;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.s2s.bean.ApplicationInfoBean;
import edu.mit.coeus.s2s.bean.IS2SSubmissionData;
import edu.mit.coeus.s2s.bean.S2SHeader;
import edu.mit.coeus.s2s.bean.S2SSubmissionDataTxnBean;
import edu.mit.coeus.s2s.bean.S2STxnBean;
import edu.mit.coeus.s2s.bean.S2SXMLInfoBean;
import edu.mit.coeus.s2s.bean.SubmissionDetailInfoBean;
import edu.mit.coeus.s2s.generator.SF424Stream;
import edu.mit.coeus.s2s.util.Converter;
import edu.mit.coeus.s2s.util.S2SHashValue;
import edu.mit.coeus.s2s.util.S2SStub;
import edu.mit.coeus.s2s.validator.BindingFileReader;
import edu.mit.coeus.s2s.validator.BindingInfoBean;
import edu.mit.coeus.s2s.validator.S2SErrorHandler;
import edu.mit.coeus.s2s.validator.S2SValidationException;
import edu.mit.coeus.s2s.validator.S2SValidator;
import edu.mit.coeus.s2s.validator.UniqueSchemaNotFoundException;
import edu.mit.coeus.utils.CoeusFunctions;
import edu.mit.coeus.utils.CoeusProperties;
import edu.mit.coeus.utils.CoeusPropertyKeys;
import edu.mit.coeus.utils.S2SConstants;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.utils.xml.generator.PropXMLStreamGenerator;
import edu.mit.coeus.xml.generator.CoeusXMLGenrator;
import edu.mit.coeus.xml.generator.ProposalLogStream;
import gov.grants.apply.WebServices.ApplicantIntegrationServices_V1_0.ApplicantIntegrationSoapBindingStub;
import gov.grants.apply.WebServices.ApplicantIntegrationServices_V1_0.ApplicationInformationType;
import gov.grants.apply.WebServices.ApplicantIntegrationServices_V1_0._GetApplicationListResponse;
import gov.grants.apply.WebServices.ApplicantIntegrationServices_V1_0._SubmitApplicationRequest;
import gov.grants.apply.WebServices.ApplicantIntegrationServices_V1_0._SubmitApplicationResponse;
import gov.grants.apply.soap.util.SoapUtils;
import gov.grants.apply.util.GrantApplicationHash;
//import gov.grants.apply.system.footer_v1.GrantSubmissionFooterType;
import gov.grants.apply.system.global_v1.HashValueType;
import gov.grants.apply.system.header_v1.GrantSubmissionHeaderType;
import gov.grants.apply.system.metagrantapplication.GrantApplicationType;
import gov.grants.apply.xml.util.InvalidXmlException;
import gov.grants.apply.xml.util.Utils;
//import gov.grants.apply.system.metagrantapplication.ObjectVerifierFactory;
import java.io.File;
import java.io.FileInputStream;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import javax.activation.DataHandler;
import org.apache.axis.attachments.AttachmentPart;
import org.apache.axis.client.Call;
import org.w3c.dom.Document;
import java.security.MessageDigest;
import java.util.Calendar;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.TimeZone;
import java.util.Vector;
import javax.xml.bind.JAXBException;
import org.apache.soap.util.mime.ByteArrayDataSource;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author  Geo Thomas
 */
public class SubmissionEngineV1 extends SubmissionEngine{
    public _SubmitApplicationResponse submitApplication(S2SHeader headerParam) 
                    throws S2SValidationException,CoeusException{
        _SubmitApplicationRequest submitRequest = new _SubmitApplicationRequest();
        try{
//            ApplicantIntegrationSoapBindingStub stub = SoapUtils.getApplicantSoapStub();
        /*
         *Implement multi campus. Pass duns number to create the stub with appropriate keystore
         *
         */
            ApplicantIntegrationSoapBindingStub stub = new S2SStub().getApplicantSoapStub(headerParam.getSubmissionTitle());
        
            S2SSubmissionDataTxnBean subTxnBean = new S2SSubmissionDataTxnBean();
            subTxnBean.setUserId(getLoggedInUser());
            IS2SSubmissionData xmlInfoBean = subTxnBean.getSubmissionData(headerParam);
            GrantSubmissionHeaderType header = xmlInfoBean.getHeader();
            String appXml = validateData(headerParam);
            List attachments = getAttachments();
            
            submitRequest.setGrantApplicationXML(appXml);
            int size = attachments==null?0:attachments.size();
            for(int i=0;i<size;i++){
                Attachment attBean = null;
                try{
                    attBean = (Attachment)attachments.get(i);
                }catch(ClassCastException ccEx){
                    String msg = coeusMessageResourcesBean.parseMessageKey("exceptionCode.90005");
                    S2SValidationException valEx = new S2SValidationException(ccEx);
                    valEx.addError(msg, S2SValidationException.ERROR);
                    if(hasErrorHandler()){
                        errorHandler.exception(valEx);
                        continue;
                    }else{
                        throw valEx;
                    }
                }
                attach(stub, attBean);
            }

            /*
             *Preparing SubmissionDetailInfoBean for updating the database
             */
            SubmissionDetailInfoBean subInfoBean = new SubmissionDetailInfoBean();
            subInfoBean.setSubmissionTitle(header.getSubmissionTitle());
            subInfoBean.setAcType('I');
            subInfoBean.setStatus(coeusMessageResourcesBean.parseMessageKey("exceptionCode.90006"));
            subInfoBean.setComments(coeusMessageResourcesBean.parseMessageKey("exceptionCode.90007"));
            subInfoBean.setApplicationData(appXml);
            subInfoBean.setAttachments((ArrayList)attachments);
            if(subTxnBean.addUpdDeleteSubmissionDetails(subInfoBean)){
                UtilFactory.log("S2S info have been updated to database successfully");
            }
            subInfoBean.setAttachments(null);
            _SubmitApplicationResponse wsResponse = null;
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
                        wsResponse.getGrants_govTrackingNumber().toString());
            subInfoBean.setAcType('U');
            subInfoBean.setAwProposalNumber(subInfoBean.getSubmissionTitle());
            Calendar ggRecDateCal = wsResponse.getReceivedDateTime();
//            java.util.TimeZone tt = ggRecDateCal.getTimeZone();
//            System.out.println(tt.getDisplayName());
            if(ggRecDateCal!=null){
//                ggRecDateCal.setTimeZone(java.util.TimeZone.getTimeZone("GMT-5.00"));
//                java.util.TimeZone tt1 = ggRecDateCal.getTimeZone();
//                System.out.println(tt1.getDisplayName());
//                java.text.DateFormat formatter = new java.text.SimpleDateFormat("yyyy-MM-dd HH.mm.ss.fffffff");
//                formatter.setCalendar(ggRecDateCal);
//                java.util.TimeZone tz = java.util.TimeZone.getTimeZone(java.util.TimeZone.getDefault());
//                formatter.setTimeZone(java.util.TimeZone.getDefault());
//                System.out.println(Timestamp.valueOf(formatter.format(new Timestamp(
//                                                ggRecDateCal.getTimeInMillis()))));

//                subInfoBean.setReceivedDateTime(Timestamp.valueOf(formatter.format(ggRecDateCal)));
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
    private void attach(ApplicantIntegrationSoapBindingStub stub, Attachment attBean){
        if(attBean==null)
            return;
        AttachmentPart attachmentPart = new AttachmentPart();
        DataHandler attachmentFile = new DataHandler(new ByteArrayDataSource(
                                            attBean.getContent(),
                                            attBean.getContentType()));
        /**
         * Tell the stub that the message being formed also contains an
         * attachment, and it is of type MIME encoding.
         */
        stub._setProperty(Call.ATTACHMENT_ENCAPSULATION_FORMAT,
                            Call.ATTACHMENT_ENCAPSULATION_FORMAT_MIME);
        attachmentPart.setDataHandler(attachmentFile);
        attachmentPart.setContentId(attBean.getContentId());
        attachmentPart.setContentType(attBean.getContentType());
        stub.addAttachment(attachmentPart);
    }
    
}
