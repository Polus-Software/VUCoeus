/*
 * CreateRequest.java
 *
 * Created on October 19, 2004, 1:48 PM
 */

package edu.mit.coeus.s2s;

import edu.mit.coeus.s2s.bean.S2SXMLInfoBean;
import edu.mit.coeus.utils.UtilFactory;
import gov.grants.apply.WebServices.ApplicantIntegrationServices_V1_0.ApplicantIntegrationSoapBindingStub;
import gov.grants.apply.WebServices.ApplicantIntegrationServices_V1_0._GetApplicationListRequest;
import gov.grants.apply.WebServices.ApplicantIntegrationServices_V1_0._GetApplicationListRequest_ApplicationFilter;
import gov.grants.apply.WebServices.ApplicantIntegrationServices_V1_0._GetApplicationListRequest_ApplicationFilter_Filter;
import gov.grants.apply.WebServices.ApplicantIntegrationServices_V1_0._GetOpportunityListRequest;
//import gov.grants.apply.struts.forms.GetApplicationListForm;
//import gov.grants.apply.struts.forms.GetOpportunityListForm;
import gov.grants.apply.system.Global_V1_0.StringMin1Max100Type;
import gov.grants.apply.system.Global_V1_0.StringMin1Max15Type;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.activation.CommandInfo;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import org.apache.axis.attachments.AttachmentPart;
import org.apache.axis.client.Call;
import org.apache.soap.util.mime.ByteArrayDataSource;

/**
 *
 * @author  geot
 */
public class S2SRequestCreator {
    public static final String CFDA_NUMBER="CFDA_NUMBER";
    public static final String OPPORTUNITY_ID="OPP_ID";
    public static final String SUBMISSION_TITLE="SUBMISSION_TITLE";
    public static final String STATUS="STATUS";
    public static final String COMPETITION_ID="COMPETITION_ID";
//    private UtilFactory UtilFactory;
    /** Creates a new instance of CreateRequest */
    public S2SRequestCreator() {
//        UtilFactory = new UtilFactory();
    }
    /**
     * Creates an application list request
     *
     * @param form the GetApplicationListForm
     * @return the _GetApplicationListRequest
     */
    public static _GetApplicationListRequest createAppListRequest(HashMap searchParams ) {
        List list = new ArrayList();
        UtilFactory.log("Getting Filter Values");
        if (searchParams.get(STATUS) != null) {
            _GetApplicationListRequest_ApplicationFilter applicationFilter_status = new _GetApplicationListRequest_ApplicationFilter();
            applicationFilter_status.setFilter(
                    _GetApplicationListRequest_ApplicationFilter_Filter.Status);
            applicationFilter_status.setFilterValue(searchParams.get(STATUS).toString());
            UtilFactory.log("Adding a Status filter of: " + searchParams.get(STATUS).toString());
            list.add(applicationFilter_status);
        }
        
        if (searchParams.get(CFDA_NUMBER) != null) {
            _GetApplicationListRequest_ApplicationFilter applicationFilter_CFDANum = new _GetApplicationListRequest_ApplicationFilter();
            applicationFilter_CFDANum.setFilter(_GetApplicationListRequest_ApplicationFilter_Filter.CFDANumber);
            applicationFilter_CFDANum.setFilterValue(searchParams.get(CFDA_NUMBER).toString());
            UtilFactory.log("Adding a CFDANum filter of: "+ searchParams.get(CFDA_NUMBER));
            list.add(applicationFilter_CFDANum);
        }
        
        if (searchParams.get(OPPORTUNITY_ID) != null) {
            _GetApplicationListRequest_ApplicationFilter applicationFilter_OpportunityID = new _GetApplicationListRequest_ApplicationFilter();
            applicationFilter_OpportunityID.setFilter(_GetApplicationListRequest_ApplicationFilter_Filter.OpportunityID);
            applicationFilter_OpportunityID.setFilterValue(searchParams.get(OPPORTUNITY_ID).toString());
            UtilFactory.log("Adding a OppId filter of: "+ searchParams.get(OPPORTUNITY_ID).toString());
            list.add(applicationFilter_OpportunityID);
        }
        
        if (searchParams.get(SUBMISSION_TITLE) != null) {
            _GetApplicationListRequest_ApplicationFilter applicationFilter_SubmTittle = new _GetApplicationListRequest_ApplicationFilter();
            applicationFilter_SubmTittle.setFilter(_GetApplicationListRequest_ApplicationFilter_Filter.SubmissionTitle);
            applicationFilter_SubmTittle.setFilterValue(searchParams.get(SUBMISSION_TITLE).toString());
            UtilFactory.log("Adding a OppId filter of: "+ searchParams.get(SUBMISSION_TITLE).toString());
            list.add(applicationFilter_SubmTittle);
        }

        _GetApplicationListRequest getApplicationListRequest = new _GetApplicationListRequest();
        getApplicationListRequest.setApplicationFilter((
                    _GetApplicationListRequest_ApplicationFilter[]) 
                        list.toArray(
                            new _GetApplicationListRequest_ApplicationFilter[0]));
        return getApplicationListRequest;
    }
    public _GetOpportunityListRequest createRequest( HashMap searchParams) {
        StringMin1Max100Type stringOpportunityID = 
                                    new StringMin1Max100Type(
                                        (String)searchParams.get(OPPORTUNITY_ID));
        StringMin1Max100Type stringCompetitionID =
                                    new StringMin1Max100Type(
                                        (String)searchParams.get(COMPETITION_ID));
        StringMin1Max15Type stringCFDANumber =
                                new StringMin1Max15Type(
                                        (String)searchParams.get(CFDA_NUMBER));
        _GetOpportunityListRequest getOpp_request =
                                    new _GetOpportunityListRequest();

        //	==============================================================
        //	 Conversion of Values from the Bean
        // to _GetOpportunityListRequest data types
        //	==============================================================
        getOpp_request.setOpportunityID(stringOpportunityID);
        getOpp_request.setCFDANumber(stringCFDANumber);
        getOpp_request.setCompetitionID(stringCompetitionID);
        UtilFactory.log("_GetOpporunityList Request[DEBUG]: " + getOpp_request);
        UtilFactory.log("_GetOpporunityList OPP ID[DEBUG]: "
                                + getOpp_request.getOpportunityID());
        UtilFactory.log("_GetOpporunityList CFDA[DEBUG]: "
                                    + getOpp_request.getCFDANumber());
        UtilFactory.log("_GetOpporunityList COMPID[DEBUG]: "
                                + getOpp_request.getCompetitionID());
        //	==============================================================
        //	 Data is sent to the GetOpportunityListProcessor class
        //	==============================================================
        return getOpp_request;
    }
    public void addAttachments(ApplicantIntegrationSoapBindingStub stub,S2SXMLInfoBean xmlInfo){
            attach(stub, new File("C:/Coeus/S2S/Documents/S2S_Technical_Diagram.doc"));
//            attach(stub, new File("C:/Coeus/S2S/sample/budgetjust.pdf"));
//            attach(stub, new File("C:/Coeus/S2S/sample/projDesc.pdf"));
    }
    static int g=0;
    private void attach(ApplicantIntegrationSoapBindingStub stub, File f){
        if(f==null)
            return;
        AttachmentPart attachmentPart = new AttachmentPart();
//        DataHandler attachmentFile = new DataHandler(new FileDataSource(f));
//        DataHandler attachmentFile = new DataHandler(new String("ajghdsahdgsdgfsgdsd"),"application/octet-stream");
        DataHandler attachmentFile = new DataHandler(
                new ByteArrayDataSource("shgsdfjhdsf".getBytes(),"application/octet-stream"));
        /**
         * Tell the stub that the message being formed also contains an
         * attachment, and it is of type MIME encoding.
         */
        stub._setProperty(Call.ATTACHMENT_ENCAPSULATION_FORMAT,
                            Call.ATTACHMENT_ENCAPSULATION_FORMAT_MIME);
        attachmentPart.setDataHandler(attachmentFile);
        
        attachmentPart.setContentId("atttachment"+(++g));
        System.out.println("Attachment File=>"+f.getName());
        
//        stub.addAttachment(attachmentFile);
        stub.addAttachment(attachmentPart);
        
    }

}
