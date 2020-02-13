/*
 * S2SValidator.java
 *
 * Created on December 27, 2004, 1:00 PM
 */

package edu.mit.coeus.s2s.validator;

import com.sun.xml.bind.JAXBObject;
import com.sun.xml.bind.RIElement;
import com.sun.xml.bind.validator.ValidatableObject;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.s2s.bean.FormInfoBean;
import edu.mit.coeus.s2s.bean.IS2SSubmissionData;
import edu.mit.coeus.s2s.bean.S2SHeader;
import edu.mit.coeus.s2s.bean.S2SSubmissionDataTxnBean;
import edu.mit.coeus.s2s.formattachment.FormAttachmentExtractService;
import edu.mit.coeus.s2s.generator.S2SBaseStream;
import edu.mit.coeus.s2s.generator.UserAttachedFormStreamGenerator;
import edu.mit.coeus.s2s.util.Converter;
import edu.mit.coeus.utils.S2SConstants;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.xml.generator.BaseStreamGenerator;
import edu.mit.coeus.xml.generator.CoeusXMLGenrator;
import gov.grants.apply.WebServices.ApplicantIntegrationServices_V1_0.ApplicantIntegrationSoapBindingStub;
import gov.grants.apply.WebServices.ApplicantIntegrationServices_V1_0._GetOpportunityListRequest;
import gov.grants.apply.WebServices.ApplicantIntegrationServices_V1_0._GetOpportunityListResponse;
import gov.grants.apply.soap.util.SoapUtils;
import gov.grants.apply.system.Global_V1_0.StringMin1Max100Type;
import gov.grants.apply.system.Global_V1_0.StringMin1Max15Type;
import gov.grants.apply.system.header_v1.GrantSubmissionHeaderType;
import gov.grants.apply.system.metagrantapplication.GrantApplicationType;
import gov.grants.apply.system.metagrantapplication.ObjectVerifierFactory;
import de.fzi.dbs.verification.event.VerificationEventLocator;
import de.fzi.dbs.verification.ObjectVerifier;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.Vector;
import javax.xml.bind.JAXBException;
import javax.xml.bind.ValidationEvent;
import javax.xml.bind.ValidationEventHandler;
import javax.xml.bind.ValidationEventLocator;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;

/**
 *
 * @author  geot
 */
public class S2SValidator implements ErrorHandler, ValidationEventHandler{
    
    
    static final String JAXP_SCHEMA_LANGUAGE =
    "http://java.sun.com/xml/jaxp/properties/schemaLanguage";
    
    static final String W3C_XML_SCHEMA =
    "http://www.w3.org/2001/XMLSchema";
    static final String JAXP_SCHEMA_SOURCE =
    "http://java.sun.com/xml/jaxp/properties/schemaSource";
    
    private static Hashtable custErrors;
    private GrantSubmissionHeaderType header;
    private gov.grants.apply.system.metagrantapplication.ObjectFactory metaObjFactory;
    private String schemaUrl;
    private String instrUrl;
    private _GetOpportunityListResponse opportunityList;
    private S2SValidationException schemaException;
    private int validationMode;
    private ArrayList attachments;
    private ArrayList formUrls;
    private S2SErrorHandler errorHandler;
    private boolean formChanged;
    /** Creates a new instance of S2SValidator */
    public S2SValidator() {
        this(1);
    }
    
    public S2SValidator(int validationMode){
        this.validationMode = validationMode;
        metaObjFactory = new gov.grants.apply.system.metagrantapplication.ObjectFactory();
        schemaException = new S2SValidationException();
    }
    private ArrayList includedForms;
    private S2SHeader headerParam;
    public GrantApplicationType.FormsType validate(IS2SSubmissionData submissionData)
    throws S2SValidationException,CoeusException{
        this.header = submissionData.getHeader();
        headerParam = submissionData.getParams();
        performInitialValidations(headerParam);
        GrantApplicationType.FormsType forms = null;
        try{
            Hashtable bindings = BindingFileReader.getBindings();
            forms = metaObjFactory.createGrantApplicationTypeFormsType();
            List formsList = forms.getAny();
    //        List formsList = forms.getContent();
            if(submissionData.getLocalOpportunity()!=null){
                setSchemaUrl(submissionData.getLocalOpportunity().getSchemaUrl());
                setInstrUrl(submissionData.getLocalOpportunity().getInstructionUrl());
            }
            boolean errflag = false;
            ArrayList sltdForms = getFormUrls(headerParam);
            if(sltdForms!=null){
                int size = sltdForms.size();
                for(int i=0;i<size;i++){
                    FormInfoBean sltdForm = (FormInfoBean)sltdForms.get(i);
                    if(sltdForm.isInclude()){
                        String selFrmNS = sltdForm.getNs();
                        String selFormname = sltdForm.getFormName();
                        BindingInfoBean bindInfo = (BindingInfoBean)bindings.get(selFrmNS);
                        if(bindInfo==null){
                        	Object userAttachedFormObject = generateUserAttachedForms(headerParam.getSubmissionTitle(),selFrmNS,selFormname);
                        	if(userAttachedFormObject!=null){
                        		formsList.add(userAttachedFormObject);
                        	}
                        }else{
                        	formsList.add(invokeFormMethod(bindInfo,headerParam));
                        }
                        if(includedForms==null) includedForms = new ArrayList();
                        includedForms.add(sltdForm);
                    }
                }
                if(attachmentMap!=null){
                    if(attachments ==null) attachments = new ArrayList();
                    attachments.addAll(attachmentMap.values());
                }
            }
        }catch(S2SValidationException sEx){
            if(hasErrorHandler())  errorHandler.exception(sEx);
            else throw sEx;
        }catch(Exception ex){
            UtilFactory.log(ex.getMessage(),ex,"S2SValidator", "validate");
            ex.printStackTrace();
            throw new CoeusException(ex.getMessage());
        }
        
        return forms;
    }

	private Object generateUserAttachedForms(String proposalNumber,
			String namespace, String formname) throws JAXBException, CoeusException, DBException {
		HashMap map = new HashMap();
		map.put("PROPOSAL_NUMBER", proposalNumber);
		map.put("NAMESPACE", namespace);
		map.put("FORM_NAME", formname);
		UserAttachedFormStreamGenerator userAttachedFormStreamGenerator = new UserAttachedFormStreamGenerator();
		if(attachmentMap==null) attachmentMap = new HashMap();
		userAttachedFormStreamGenerator.setAttachmentMap(attachmentMap);
		Object obj = userAttachedFormStreamGenerator.getStream(map);
    	if(obj==null){
    		throw new CoeusException("Coeus does not support the form "+formname+". Please upload the adobe form to the User Attached Form section to avail this form");
    	}
		return obj;
	}

	public void isValidationSuccessful() throws S2SValidationException {
         /*
         *Checking any custom errors added
         */
        if(custErrors!=null){
            Vector errList = (Vector)custErrors.get(headerParam.getSubmissionTitle());
            if(errList !=null && !errList.isEmpty()){
                FormInfoBean tempFrm = new FormInfoBean();
                tempFrm.setFormName("Other Validation Errors");
                schemaException.addError(tempFrm,S2SValidationException.WARNING);
                for(int i=0;i<errList.size();i++){
    //                String err = (String)errList.get(i);
                    schemaException.addError(errList.get(i),S2SValidationException.ERROR);
                    schemaParseError=true;
                }
            }
            custErrors.remove(headerParam.getSubmissionTitle());
        }
        if (schemaParseError) {
            schemaParseError = false;
            String msg = "The generated XML application document is not valid " + "against the opportunity";
            schemaException.setMainError(msg);
            schemaException.setOppSchemaUrl(getSchemaUrl());
            schemaException.setOppInstrUrl(getInstrUrl());
            throw schemaException;
        }
    }
    /*
     *Perform validation against Coeus DB and add the error codes in main exception
     */
    private void performInitialValidations(S2SHeader headerParam) throws CoeusException{
        S2SSubmissionDataTxnBean s2sSubTxnBean = new S2SSubmissionDataTxnBean();
        String errorCodes = null;
        try{
            errorCodes = s2sSubTxnBean.performCoeusSpecificValidations(headerParam.getSubmissionTitle());
        }catch(DBException dbEx){
            UtilFactory.log(dbEx.getMessage(),dbEx,"S2SValidator", "performInitialValidations");
            throw new CoeusException(dbEx.getMessage());
        }
        String[] errorCodeArr = errorCodes.split(",");
        for(int i=0;i<errorCodeArr.length;i++){
            if(errorCodeArr[0].equals("0")) return;
            schemaParseError=true;
            try{
                String genError = S2SErrorMessages.getProperty("s2sIntialValidationError99999999");
                schemaException.addError(
                S2SErrorMessages.getProperty("s2sIntialValidationError"+errorCodeArr[i],
                genError+errorCodeArr[i]),
                schemaException.ERROR);
            }catch(IOException ioEx){
                //do nothing
            }
        }
    }
    private HashMap attachmentMap;
    private String processingFormName;//this will be used to apend the formname for the error message key
    private Object invokeFormMethod(BindingInfoBean bindBean,S2SHeader headerParam)
    		throws S2SValidationException,CoeusException{
    	
        this.processingFormName = bindBean.getFormName();
        try{
            Class streamClass = Class.forName(bindBean.getClassName());
            S2SBaseStream baseStream = (S2SBaseStream)streamClass.newInstance();
            if(attachmentMap==null) attachmentMap = new HashMap();
            baseStream.setAttachmentMap(attachmentMap);
            Object objFormType = baseStream.getStream(headerParam.getStreamParams());
            //            if(attachmentMap!=null){
            //                if(attachments ==null) attachments = new ArrayList();
            //                attachments.addAll(attachmentMap.values());
            //            }
            return objFormType;
        }catch(ClassNotFoundException clEx){
            String msg = bindBean.getClassName()  +
            "not found. Make sure that the class name " +
            "given in the xml file is correct and exist";
            UtilFactory.log(msg,clEx,"S2SValidator", "attachForm");
            clEx.printStackTrace();
            throw new S2SValidationException(msg);
        }catch(InstantiationException iEx){
            String msg = "Not able to instantiate the class "+bindBean.getClassName()+
            " Make sure that the class has got the default constructor " +
            "with no parameter";
            iEx.printStackTrace();
            UtilFactory.log(msg,iEx,"S2SValidator", "attachForm");
            throw new S2SValidationException(msg);
        }catch(ClassCastException ccEx){
            String msg = bindBean.getClassName()+ " is not a S2SBaseStream"+
            " Make sure that the class extends edu.mit.coeus.S2SBaseStream";
            ccEx.printStackTrace();
            UtilFactory.log(msg,ccEx,"S2SValidator", "attachForm");
            throw new S2SValidationException(msg);
        }catch(Exception ex){
            ex.printStackTrace();
            UtilFactory.log(ex.getMessage(),ex,"S2SValidator", "attachForm");
            throw new CoeusException(ex.getMessage());
        }
    }
    public static void main(String []args) throws Exception{
        S2SValidator s = new S2SValidator();
        gov.grants.apply.system.header_v1.ObjectFactory headerObjFactory = new gov.grants.apply.system.header_v1.ObjectFactory();
        GrantSubmissionHeaderType he = headerObjFactory.createGrantSubmissionHeader();
        he.setCFDANumber("11.001");
        he.setOpportunityID("JA-051004");
        HashMap ht = new HashMap();
        ht.put("PROPOSAL_NUMBER", "00000032");
        //        s.validate(he,ht);
    }
    
    /**
     * Getter for property schemaUrl.
     * @return Value of property schemaUrl.
     */
    public java.lang.String getSchemaUrl() {
        return schemaUrl;
    }
    
    /**
     * Setter for property schemaUrl.
     * @param schemaUrl New value of property schemaUrl.
     */
    public void setSchemaUrl(java.lang.String schemaUrl) {
        this.schemaUrl = schemaUrl;
    }
    
    public Document applySchema(Document doc)
    throws S2SValidationException,CoeusException{
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        factory.setValidating(true);
        try {
            factory.setAttribute(JAXP_SCHEMA_LANGUAGE, W3C_XML_SCHEMA);
            factory.setAttribute(JAXP_SCHEMA_SOURCE, new URL(getSchemaUrl()).openStream());
            DocumentBuilder builder = factory.newDocumentBuilder();
            builder.setErrorHandler(this);
            builder.parse(new ByteArrayInputStream(doc2bytes(doc)));
        }catch (Exception x) {
            x.printStackTrace();
            UtilFactory.log(x.getMessage(),x,"S2SValidator", "applySchema");
            throw new CoeusException(x.getMessage());
        }
        if(schemaParseError){
            schemaParseError=false;
            String msg = "The generated XML application document is not valid " +
            "against the schema : <a href="+getSchemaUrl()+">"+
            getSchemaUrl()+"</a>";
            schemaException.setMainError(msg);
            throw schemaException;
        }
        return doc;
    }
    
    /*
     *This method gets called for validate the entire metagrant application
     */
    public boolean checkError(Object grantApplication)
    throws S2SValidationException,CoeusException{
        return checkError(grantApplication,null);
    }
//    public boolean checkError(Object application,String pkgName)
//    throws S2SValidationException,CoeusException{
    private BindingInfoBean bindInfoForIndForm;
    /**
     *This method gets called when individual form validation needs to be done. 
     *For eg: Print individual form
     */
    boolean isFormReset;
    public boolean checkError(Object application,String pkgName)
    		throws S2SValidationException,CoeusException{
    	
        try{
            isFormReset = true;
//            bindInfoForIndForm = bindInfBean;
//            String pkgName = bindInfBean==null?"gov.grants.apply.system.metagrantapplication":
//                                                bindInfBean.getJaxbPkgName();
            Class objVerFactClass = Class.forName(pkgName+".ObjectVerifierFactory");
            Object verifierFactory = objVerFactClass.newInstance();
            Class[] paramType = {application.getClass().getClass()};
            Object[] arg = {application.getClass()};
            Method meth = objVerFactClass.getMethod("newInstance", paramType);
            ObjectVerifier s2sObjVerifier = (ObjectVerifier)meth.invoke(verifierFactory, arg);
//            ObjectVerifier s2sObjVerifier = verifierFactory.newInstance(application.getClass());
//            ObjectVerifier s2sObjVerifier = objVerFactClass.newInstance(.);
            VerificationEventLocator verLoc = new VerificationEventLocator(null,application,"GrantApplication");
            s2sObjVerifier.check(verLoc,this,application);
        }catch(ClassNotFoundException ex){
            UtilFactory.log(ex.getMessage(),ex,"S2SValidator","checkError");
            throw new CoeusException(ex.getMessage());
        }catch(InstantiationException ex){
            UtilFactory.log(ex.getMessage(),ex,"S2SValidator","checkError");
            throw new CoeusException(ex.getMessage());
        }catch(NoSuchMethodException ex){
            UtilFactory.log(ex.getMessage(),ex,"S2SValidator","checkError");
            throw new CoeusException(ex.getMessage());
        }catch(IllegalAccessException ex){
            UtilFactory.log(ex.getMessage(),ex,"S2SValidator","checkError");
            throw new CoeusException(ex.getMessage());
        }catch(InvocationTargetException ex){
            UtilFactory.log(ex.getMessage(),ex,"S2SValidator","checkError");
            throw new CoeusException(ex.getMessage());
        }
       
        //isValidationSuccessful();
        return false;
    }
    public byte[] doc2bytes(Document node)
    throws S2SValidationException,CoeusException{
        try {
        	// JM 8-24-2011 add try catch for debug
        	Source source = null;
        	try {
        		source = new DOMSource(node);
        	}
        	catch (Exception e) {
                e.printStackTrace();
                UtilFactory.log("Could not set source to node");        		
        	}
        	// END
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            StringWriter stringWriter = new StringWriter();
            Result result = new StreamResult(out);
            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer transformer = factory.newTransformer();
            transformer.transform(source, result);
            return out.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
            UtilFactory.log(e.getMessage(),e,"S2SValidator", "doc2bytes");
            throw new CoeusException(e.getMessage());
        }
    }
    private boolean schemaParseError;
    public void error(org.xml.sax.SAXParseException exception) throws org.xml.sax.SAXException {
        String msg = exception.getMessage();
        if(hasErrorHandler()){
            errorHandler.schemaMessage(msg,S2SValidationException.ERROR);
            return;
        }
        schemaParseError=true;
        
        //        System.out.println("Error=>"+exception.getMessage());
        schemaException.addError(msg,S2SValidationException.ERROR);
        UtilFactory.log(exception.getMessage(),exception,"S2SValidator","error");
        //        exception.printStackTrace();
    }
    
    public void fatalError(org.xml.sax.SAXParseException exception) throws org.xml.sax.SAXException {
        String msg = exception.getMessage();
        if(hasErrorHandler()){
            errorHandler.schemaMessage(msg,S2SValidationException.ERROR);
            return;
        }
        schemaParseError=true;
        //        System.out.println("Fatal error=>"+exception.getMessage());
        schemaException.addError(msg,S2SValidationException.FATAL_ERROR);
        UtilFactory.log(exception.getMessage(),exception,"S2SValidator","fatalError");
        //        exception.printStackTrace();
    }
    
    public void warning(org.xml.sax.SAXParseException exception) throws org.xml.sax.SAXException {
        String msg = exception.getMessage();
        if(hasErrorHandler()){
            errorHandler.schemaMessage(msg,S2SValidationException.WARNING);
            return;
        }
        schemaParseError=true;
        //        System.out.println("Warning=>"+exception.getMessage());
        schemaException.addError(msg,S2SValidationException.WARNING);
        UtilFactory.log(exception.getMessage(),exception,"S2SValidator","warning");
        //        exception.getCause().printStackTrace();
    }
    
    /**
     * Getter for property opportunityList.
     * @return Value of property opportunityList.
     */
    public gov.grants.apply.WebServices.ApplicantIntegrationServices_V1_0._GetOpportunityListResponse getOpportunityList() {
        return opportunityList;
    }
    
    /**
     * Getter for property attachments.
     * @return Value of property attachments.
     */
    public java.util.ArrayList getAttachments() {
        return attachments;
    }
    
    /**
     * Setter for property attachments.
     * @param attachments New value of property attachments.
     */
    public void setAttachments(java.util.ArrayList attachments) {
        this.attachments = attachments;
    }
    
    /**
     * Getter for property formUrls.
     * @return Value of property formUrls.
     */
    public ArrayList getFormUrls(S2SHeader params) throws DBException{
        S2SSubmissionDataTxnBean dataTxnBean = new S2SSubmissionDataTxnBean();
        return dataTxnBean.getSelectedOptionalForms(params);
    }
    
    /**
     * Setter for property formUrls.
     * @param formUrls New value of property formUrls.
     */
    public void setFormUrls(java.util.ArrayList formUrls) {
        this.formUrls = formUrls;
    }
    
    /**
     * Getter for property errorHandler.
     * @return Value of property errorHandler.
     */
    private boolean hasErrorHandler() {
        return errorHandler!=null;
    }
    
    /**
     * Setter for property errorHandler.
     * @param errorHandler New value of property errorHandler.
     */
    public void setErrorHandler(S2SErrorHandler errorHandler) {
        this.errorHandler = errorHandler;
    }
    private int prevFrmIndex = -1;
    private String parseMessage(String completeMsgKey){
        StringBuffer msgKey = null;
        StringTokenizer tokenizer = new StringTokenizer(completeMsgKey,"/");
        while(tokenizer.hasMoreTokens()){
            if(msgKey==null) msgKey = new StringBuffer("");
            String token = tokenizer.nextToken();
            int tIndex = token.indexOf("Content");
            int tIndex1 = token.indexOf("[");
            int tIndex2 = token.indexOf("]");
            //            if(tIndex==-1){
            if(tIndex1!=-1 && tIndex2!=-1){
                String tknSubStr = token.substring(0,token.indexOf('['));
                int frmIndex = Integer.parseInt(token.substring(
                    (token.indexOf('[')+1),
                    (token.indexOf(']'))));
                if(tIndex!=-1){
                    FormInfoBean sltdIncFrm = (FormInfoBean)includedForms.get(frmIndex);;
                    if(prevFrmIndex!=frmIndex){
                        prevFrmIndex = frmIndex;
                        schemaException.addError(sltdIncFrm,schemaException.WARNING);
                    }
                    msgKey.append(sltdIncFrm.getFormName()+"/");
                }else{
                    msgKey.append(tknSubStr+"/");
                }
            }else if(this.bindInfoForIndForm!=null && token.equalsIgnoreCase("grantapplication")){
                if(isFormReset){
                        isFormReset = false;
                        FormInfoBean selectedForm = findFormBean(bindInfoForIndForm.getNameSpace());
                        if(selectedForm!=null){
                            schemaException.addError(selectedForm,schemaException.WARNING);
                        }
                    }
                    msgKey.append(token+"/Forms/"+bindInfoForIndForm.getFormName()+"/");
            }else{
                msgKey.append(token+"/");
            }
        }
        return msgKey==null?completeMsgKey:
            msgKey.deleteCharAt(msgKey.length()-1).toString();
    }
    private FormInfoBean findFormBean(String namespace){
        if(includedForms!=null)
        for(int i=0;i<includedForms.size();i++){
            FormInfoBean form = (FormInfoBean)includedForms.get(i);
            if(form.getNs().equals(namespace)){
                return form;
            }
        }
        return null;
    }
    public boolean handleEvent(javax.xml.bind.ValidationEvent validationEvent) {
        de.fzi.dbs.verification.event.VerificationEvent verEvent = ((de.fzi.dbs.verification.event.VerificationEvent)(validationEvent));
        Class c = verEvent.getLocator().getClass();
        String xpath="";
        if(c.getName().equals("de.fzi.dbs.verification.event.EntryLocator")){
            de.fzi.dbs.verification.event.EntryLocator locator = (de.fzi.dbs.verification.event.EntryLocator)verEvent.getLocator();
            xpath = locator.getJXPathExpression();
        }else{
            de.fzi.dbs.verification.event.VerificationEventLocator locator = (de.fzi.dbs.verification.event.VerificationEventLocator)verEvent.getVerificationEventLocator();
            xpath = locator.getJXPathExpression();
        }
        int sev = verEvent.getSeverity();
        String msgKey = parseMessage(xpath);
        //        de.fzi.dbs.verification.event.VerificationEventLocator locator = (de.fzi.dbs.verification.event.VerificationEventLocator)verEvent.getVerificationEventLocator();
        //        int sev = verEvent.getSeverity();
        //        String msgKey = parseMessage(locator.getJXPathExpression());
        System.out.println(msgKey);
        de.fzi.dbs.verification.event.Problem problem = verEvent.getProblem();
        
        String msg = msgKey+" "+problem.getMessage();
        try{
            msg = S2SErrorMessages.getProperty(msgKey,msg);
        }catch(IOException ioEx){
            //nothing to be done
            //send the default message
        }
        System.out.println(msg);
        UtilFactory.log(msg);
        if(hasErrorHandler()){
            errorHandler.schemaMessage(msg,sev);
            return true;
        }
        if(!schemaParseError){
            schemaParseError=true;
            schemaException.setOppSchemaUrl(getSchemaUrl());
            schemaException.setOppInstrUrl(getInstrUrl());
        }
        schemaException.addError(msg,sev);
        return true;
    }
    
    /**
     * Getter for property instrUrl.
     * @return Value of property instrUrl.
     */
    public java.lang.String getInstrUrl() {
        return instrUrl;
    }
    
    /**
     * Setter for property instrUrl.
     * @param instrUrl New value of property instrUrl.
     */
    public void setInstrUrl(java.lang.String instrUrl) {
        this.instrUrl = instrUrl;
    }
    /*
     *Used to add custom error messages from application while streaming.
     */
    public static void addCustError(String proposalNumber, String errorMessage){
        if(custErrors==null) custErrors = new Hashtable();
        Vector custErrList = (Vector)custErrors.get(proposalNumber);
        if(custErrList==null) custErrList = new Vector();
        custErrList.add(errorMessage);
        custErrors.put(proposalNumber, custErrList);
    }
    public static void cleanUpCustErrors(String proposalNumber){
        if(custErrors!=null) custErrors.remove(proposalNumber);
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
    
}
