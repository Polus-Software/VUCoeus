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
import edu.mit.coeus.s2s.v2.SubmissionEngineV2;
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
import gov.grants.apply.system.header_2_0_v2.Header20Type;
import gov.grants.apply.system.metagrantapplication.GrantApplicationType;

import java.io.IOException;
import java.util.ArrayList;
import javax.activation.DataHandler;
import org.apache.axis.attachments.AttachmentPart;
import org.apache.axis.client.Call;
import org.w3c.dom.Document;
import java.util.Calendar;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

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
public abstract class SubmissionEngine {
//    private UtilFactory UtilFactory;
    private gov.grants.apply.system.metagrantapplication.ObjectFactory metaObjFactory;
    private final String packageName = "gov.grants.apply.system.metagrantapplication";
//    private S2SValidator validator;
    protected String loggedInUser;
    private boolean validateAgainstSchema;
    protected S2SErrorHandler errorHandler;
    protected CoeusMessageResourcesBean coeusMessageResourcesBean;
//    private ObjectVerifierFactory verifierFactory;
//    private ObjectVerifier s2sObjVerifier;
    private String logDir;
    private int mode;
    private List attachments;   
    private final String header_v1 = "http://apply.grants.gov/system/Header-V1.0";
    private final String header_v2 = "http://apply.grants.gov/system/Header_2_0-V2.0";
        
    /** Creates a new instance of S2SXMLData */
    protected SubmissionEngine(int mode) {
    	this.mode=mode;
        metaObjFactory = new gov.grants.apply.system.metagrantapplication.ObjectFactory();
        coeusMessageResourcesBean = new CoeusMessageResourcesBean();
//        verifierFactory = new ObjectVerifierFactory();
    }
    /** Creates a new instance of S2SXMLData */
    protected SubmissionEngine() {
        this(0);
    }
    public static SubmissionEngine getInstance(){
    	try {
			if(SoapUtils.getProperty("SOAP_SERVER_VERSION")!=null && SoapUtils.getProperty("SOAP_SERVER_VERSION").equals("V2")){
				return new SubmissionEngineV2();
			}else{
				return new SubmissionEngineV1();
			}
		} catch (IOException e) {
			UtilFactory.log(e.getMessage(),e,"SubmissionEngine", "getInstance");
			return new SubmissionEngineV1();
		}
    }
	public abstract Object submitApplication(S2SHeader headerParam)  
            throws S2SValidationException,CoeusException;
//    public Document validateData(S2SHeader headerParam) 
//                        throws S2SValidationException,CoeusException{
        
    public String validateData(S2SHeader headerParam) 
                        throws S2SValidationException,CoeusException{
        GrantApplicationType grantApplication=null;
        try{
            grantApplication = metaObjFactory.createGrantApplication();
//            s2sObjVerifier = verifierFactory.newInstance(gov.grants.apply.system.metagrantapplication.impl.GrantApplicationTypeImpl.class);
        }catch(JAXBException jxbEx){
            UtilFactory.log(jxbEx.getMessage(),jxbEx,"SubmissionEngine","validateData");
            throw new CoeusException(jxbEx.getMessage());
        }
        CoeusXMLGenrator xmlGenerator = new CoeusXMLGenrator();
        S2SSubmissionDataTxnBean subTxnBean = new S2SSubmissionDataTxnBean();
        IS2SSubmissionData xmlInfoBean = subTxnBean.getSubmissionData(headerParam);
                
        String opportunity = xmlInfoBean.getLocalOpportunity().getOpportunity();
        String headerVersion = getHeaderVersion(opportunity);
        
        GrantSubmissionHeaderType header = xmlInfoBean.getHeader();
        Header20Type header20 = xmlInfoBean.getHeader20();        
                         
//        GrantSubmissionFooterType footer = xmlInfoBean.getFooter();         
//        grantApplication.setGrantSubmissionFooter(footer);
        //Delete all autogenrated attachments before starting XML creation
        try{
            new S2STxnBean().deleteAutoGenNarratives(headerParam.getSubmissionTitle());
        }catch(DBException dbEx){
            UtilFactory.log(dbEx.getMessage(),dbEx,"SubmissionEngine", "validateData");
            throw new CoeusException(dbEx.getMessage());
        }
        S2SValidator validator = new S2SValidator(mode);
        validator.setErrorHandler(getErrorHandler());
        GrantApplicationType.FormsType individualForms = validator.validate(xmlInfoBean);
        setAttachments(validator.getAttachments());
        List formsList = individualForms.getAny();
        for (Iterator it = formsList.iterator(); it.hasNext();) {
            Object object = it.next();
            String packagename = object.getClass().getInterfaces()[0].getPackage().getName();
            validator.checkError(object,packagename);
        }
        validator.isValidationSuccessful();
        grantApplication.setForms(individualForms);
        //grantApplication.setForms(
            //validator.validate(xmlInfoBean));
        Document grantAppDocument = null;
        String finalAppDocStr = null;
        try{
//            grantAppDocument = xmlGenerator.marshelObject(grantApplication,packageName,true);
//            HashValueType hashValue = S2SHashValue.getValue(grantAppDocument);
//            String hash = org.apache.xml.security.utils.Base64.encode(hashValue.getValue());
//            if(header!=null) header.setHashValue(hashValue);
//            grantApplication.setGrantSubmissionHeader(header);
//            grantAppDocument = xmlGenerator.marshelObject(grantApplication,packageName,true);
            
            /*
             *Create a dummy hashvalue to override the Coeus S2S Validation. 
             * It checks only for the null value.
             * Generate and create original hashvalue before it sends to grants.gov.
             * This is done because, the 1-1 schema needs to be replaced to 1.1 progranmatically
             * and that causes generating new hashvalues and attach it.
             */
//            Document grantAppDocument = xmlGenerator.marshelObject(grantApplication,packageName,true);
            HashValueType dummyHashValue = S2SHashValue.getDummyHashValue();
                        
            if (checkHeaderVersionIsPresent(opportunity, header_v1)) {
                if(header != null){
                 header.setHashValue(dummyHashValue);
                }
                grantApplication.setGrantSubmissionHeader(header);
            }
            if (checkHeaderVersionIsPresent(opportunity, header_v2)) {
                grantApplication.setHeader20(header20);
            }
            
            String schemaLocation = xmlInfoBean.getLocalOpportunity().getSchemaUrl();
            String schemaLocationPair = "http://apply.grants.gov/system/MetaGrantApplication "+schemaLocation;
            grantAppDocument = xmlGenerator.marshelObject(grantApplication,packageName,true,schemaLocationPair);
            //validator.checkError(grantApplication);
            
            ///////////////////////
//            String tempAppXml = Converter.doc2String(grantAppDocument);
//            Hashtable bindings = BindingFileReader.getBindings();
//            Enumeration en = bindings.elements();
//            StringBuffer strbfr = new StringBuffer(tempAppXml);
//            String appXml = tempAppXml;
//            while(en.hasMoreElements()){
//                BindingInfoBean bindInfo = (BindingInfoBean)en.nextElement();
//                if(bindInfo.isNsChanged()){
//                    int in = tempAppXml.indexOf(bindInfo.getCgdNameSpace());
//                    if(in!=-1)
//                    tempAppXml = Converter.replaceAll(tempAppXml, 
//                                "\""+bindInfo.getCgdNameSpace()+"\"", 
//                                "\""+bindInfo.getNameSpace()+"\"");
//                }
//            }
//            appXml = tempAppXml;
            ////////////////////////////////
            String appXml = Converter.replaceAllCgdNS(grantAppDocument);
//            validator.applySchema(grantAppDocument);
            Document finalAppDoc = Converter.string2Dom(appXml);
            Element appDocElement = finalAppDoc.getDocumentElement();
            NodeList headerNodeList = getHeaderNodeList(appDocElement,headerVersion);
                    
//            NodeList headerNodeList = appDocElement.getElementsByTagName( 
//                            "header:GrantSubmissionHeader");
            Node headerNode = headerNodeList.item(0);
            System.out.println("headernode"+headerNode.getFirstChild().getNodeName());
            NodeList hashNodeList = ((Element)headerNode).getElementsByTagNameNS(
                        "http://apply.grants.gov/system/Global-V1.0", "HashValue");
            Node hashNode = hashNodeList.item(0);
            String hashVal = GrantApplicationHash.computeGrantFormsHash(finalAppDoc);
            Node textNode = null;
            if (hashNode != null) {
                for (textNode = hashNode.getFirstChild(); textNode != null;
                        textNode = textNode.getNextSibling()) {
                    if (textNode.getNodeType() == Node.TEXT_NODE) {
                        textNode.setNodeValue(hashVal);
                    }
                }
            }
            finalAppDocStr = Converter.doc2String(finalAppDoc);         
            validator.applySchema(finalAppDoc);
            return finalAppDocStr;
        }catch(UniqueSchemaNotFoundException uEx){
            throw uEx;
        }catch(S2SValidationException sEx){
            throw sEx;
        }catch(CoeusException ex){
            throw ex;
        }catch(Exception ex){
            ex.printStackTrace();
            UtilFactory.log(ex.getMessage(),ex,"SubmissionEngine","validateData");
            throw new CoeusException(ex.getMessage());
        }finally{
            try{
                String tempLogAppXml = Converter.doc2String(grantAppDocument);
                if(CoeusProperties.getProperty(CoeusPropertyKeys.GENERATE_XML_FOR_DEBUGGING,"No").equalsIgnoreCase("Yes")||
                        CoeusProperties.getProperty(CoeusPropertyKeys.GENERATE_XML_FOR_DEBUGGING,"N").equalsIgnoreCase("Y")){
                    if(header!=null )
                        
                        UtilFactory.logFile(logDir, 
                            header.getSubmissionTitle()+System.currentTimeMillis()+".xml", 
                            finalAppDocStr==null?tempLogAppXml:finalAppDocStr);
                }
            }catch(Exception ex){
                UtilFactory.log("Warning:Not able to log XML file",ex,"SubmissionEngine","validateData");
            }
        }
    }
	protected List getAttachments() {
		return attachments;
	}
	protected void setAttachments(List attachments) {
		this.attachments = attachments;
	}
    private IS2SSubmissionData getSubmissionData(S2SHeader params) throws Exception{
        S2SSubmissionDataTxnBean subTxnBean = new S2SSubmissionDataTxnBean();
        return subTxnBean.getSubmissionData(params);
    }
        
    private String getHeaderVersion(String opportunity){      
        if(opportunity == null){
            return "V10";
        }
        if(opportunity.contains("http://apply.grants.gov/system/Header-V1.0")){
            return "V10";
        }
        if(opportunity.contains("http://apply.grants.gov/system/Header_2_0-V2.0")){
            return "V20";
        }
        return "";       
    }       
    private boolean checkHeaderVersionIsPresent(String opportunity, String headerVersion){      
        if(opportunity == null || headerVersion == null){
            return false;
        }
        if(opportunity.contains(headerVersion)){
            return true;
        }        
        return false;       
    }    
            
    private NodeList getHeaderNodeList(Element appDocElement,String headerVersion) {
        if(headerVersion.equals("V20")){
            return appDocElement.getElementsByTagNameNS(header_v2,"Header_2_0");
        }
        return appDocElement.getElementsByTagNameNS(header_v1,"GrantSubmissionHeader");
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
    /**
     * Getter for property loggedInUser.
     * @return Value of property loggedInUser.
     */
    public java.lang.String getLoggedInUser() {
        return loggedInUser;
    }
    
    /**
     * Setter for property loggedInUser.
     * @param loggedInUser New value of property loggedInUser.
     */
    public void setLoggedInUser(java.lang.String loggedInUser) {
        this.loggedInUser = loggedInUser;
    }
    
    /**
     * Getter for property validateAgainstSchema.
     * @return Value of property validateAgainstSchema.
     */
    public boolean isValidateAgainstSchema() {
        return validateAgainstSchema;
    }
    
    /**
     * Setter for property validateAgainstSchema.
     * @param validateAgainstSchema New value of property validateAgainstSchema.
     */
    public void setValidateAgainstSchema(boolean validateAgainstSchema) {
        this.validateAgainstSchema = validateAgainstSchema;
    }
    
    /**
     * Getter for property errorHandler.
     * @return Value of property errorHandler.
     */
    public boolean hasErrorHandler() {
        return errorHandler!=null;
    }
    
    /**
     * Setter for property errorHandler.
     * @param errorHandler New value of property errorHandler.
     */
    public void setErrorHandler(S2SErrorHandler errorHandler) {
        this.errorHandler = errorHandler;
    }
    public S2SErrorHandler getErrorHandler() {
        return errorHandler;
    }
    public ArrayList getOpportunityList(S2SHeader headerParam) throws Exception{
        return GetOpportunity.getInstance().getOpportunityList(headerParam);
    }
    
    /**
     * Getter for property logDir.
     * @return Value of property logDir.
     */
    public java.lang.String getLogDir() {
        return logDir;
    }
    
    /**
     * Setter for property logDir.
     * @param logDir New value of property logDir.
     */
    public void setLogDir(java.lang.String logDir) {
        this.logDir = logDir;
    }
    
    
    
}
