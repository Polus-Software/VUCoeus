/*
 * S2SPrintForm.java
 *
 * Created on June 15, 2005, 8:47 AM
 */

package edu.mit.coeus.s2s.bean;

import com.lowagie.text.DocumentException;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.s2s.Attachment;
import edu.mit.coeus.s2s.formattachment.FormAttachmentExtractService;
import edu.mit.coeus.s2s.generator.S2SBaseStream;
import edu.mit.coeus.s2s.generator.UserAttachedFormStreamGenerator;
import edu.mit.coeus.s2s.util.Converter;
import edu.mit.coeus.s2s.validator.BindingFileReader;
import edu.mit.coeus.s2s.validator.BindingInfoBean;
import edu.mit.coeus.s2s.validator.S2SValidationException;
import edu.mit.coeus.s2s.validator.S2SValidator;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.utils.document.DocumentConstants;
import edu.mit.coeus.utils.documenttype.DocumentTypeChecker;
import edu.mit.coeus.xml.generator.CoeusXMLGenrator;
import gov.grants.apply.xpath.XPathExecutor;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Vector;
import javax.xml.bind.JAXBException;
import javax.xml.transform.TransformerException;
import org.apache.xpath.XPathAPI;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author  geot
 */
public class S2SPrintForm {
    private CoeusXMLGenrator xmlGen;
    private S2SSubmissionDataTxnBean subTxn;
    /** Creates a new instance of S2SPrintForm */
    public S2SPrintForm() {
        xmlGen = new CoeusXMLGenrator();
        subTxn = new S2SSubmissionDataTxnBean();
    }
    
    public String printForm(Vector formInfoBeans,S2SHeader headerParam,String reportPath)
    throws S2SValidationException,CoeusException{
         
        
        try{
     
            Converter.sortForms(formInfoBeans);
            ByteArrayOutputStream[] pdfArray = getPDFStream(formInfoBeans, headerParam);
            String[] bookmarks = getBookmarks(formInfoBeans);
            FormInfoBean formInfo = (FormInfoBean)formInfoBeans.get(0);
            String reportName = "S2S"+formInfo.getProposalNumber();
            String fileName = xmlGen.mergePdfReports(
            pdfArray,bookmarks,reportPath,reportName);
            return fileName;
        }catch(DocumentException ex){
            UtilFactory.log(ex.getMessage(),ex,"S2SPrintForm", "printForm");
            throw new CoeusException(ex.getMessage());
        }catch(IOException ex){
            UtilFactory.log(ex.getMessage(),ex,"S2SPrintForm", "printForm");
            throw new CoeusException(ex.getMessage());
        }
    }
    
    //    public Vector sortForms(Vector formInfoBeans) throws CoeusException{
    //        CoeusVector cvForms = new CoeusVector();
    //        int size = formInfoBeans.size();
    //        Hashtable bind = BindingFileReader.getBindings();
    //        for(int i=0;i<size;i++){
    //            FormInfoBean frm = (FormInfoBean)formInfoBeans.get(i);
    //            frm.setSortIndex(((BindingInfoBean)bind.get(frm.getNs())).getSortIndex());
    //            cvForms.add(frm);
    //        }
    //        boolean sorted = cvForms.sort("sortIndex");
    //        System.out.println("sorted=>"+sorted);
    //        return cvForms;
    //    }
    private ArrayList bookmarksList;
    public ByteArrayOutputStream[] getPDFStream(Vector formInfoBeans, S2SHeader headerParam)throws S2SValidationException,CoeusException {
        int size = formInfoBeans.size();
//        ByteArrayOutputStream[] pdfArray = new ByteArrayOutputStream[size];
        Vector pdfBaosList = new Vector();
//        String[] bookmarks = new String[size];
        bookmarksList = new ArrayList();
        FormInfoBean formInfo=null;
        byte[] formPdfBytes = null;
        boolean submitted = false;
        String xmlStr = "";
        if(validator==null) validator = new S2SValidator();
        validator.setHeaderParam(headerParam);
        try{
            ApplicationInfoBean appInfo = subTxn.getSubmissionDetails(headerParam);
            if(appInfo!=null && appInfo.getGrantsGovTrackingNumber()!=null &&
            appInfo.getGrantsGovTrackingNumber().length()>0){
                ApplicationInfoBean xmlData = subTxn.getApplicationData(headerParam);
                xmlStr = xmlData.getApplicationData();
                submitted = true;
            }
            boolean isValidationRequired = false;
            for(int frmIndex=0;frmIndex<size;frmIndex++){
                formInfo = (FormInfoBean)formInfoBeans.get(frmIndex);
                String selFrmNS = formInfo.getNs();
                if(submitted&&formInfo.isInclude()){
                    formPdfBytes =generatePdf(headerParam.getSubmissionTitle(),selFrmNS,xmlStr);
                }else{
                    isValidationRequired = true;
                    formPdfBytes =invokeFormStream(selFrmNS,headerParam,submitted,formInfo.getFormName());
                }
                ByteArrayOutputStream stream = null;
                try{
                    stream = new ByteArrayOutputStream();
                    stream.write(formPdfBytes);
                }catch(Throwable th){
                    UtilFactory.log("Warning:  not able to attach "+formInfo.getFormName()+
                                    "; continue with next attachment",
                                    th,"S2SPrintForm","getPDFStream");
                    continue;
                }finally{
                    if(stream!=null){
                        stream.flush();
                        stream.close();
                    }
                }
//                pdfArray [frmIndex] = stream;
                pdfBaosList.add(stream);
                bookmarksList.add(formInfo.getFormName());
//                bookmarks[frmIndex] = formInfo.getFormName();
                if(attachmentMap!=null && !attachmentMap.isEmpty()){
                    //Add attachments
                    Iterator attIt = attachmentMap.keySet().iterator();
                    while(attIt.hasNext()){
                        DocumentTypeChecker dtChecker = new DocumentTypeChecker();
                        Attachment att = (Attachment)attachmentMap.get(attIt.next());
                        if(att==null || att.getContent()==null) continue;
                        try{
                            if(!dtChecker.getDocumentType(att.getContent()).
                                getMimeType().equalsIgnoreCase(DocumentConstants.MIME_PDF)) continue;
                        }catch(Exception ex){
                            UtilFactory.log("Warning:",ex,"S2SPrintForm", "getPDFStream");
                            continue;
                        }
                        ByteArrayOutputStream attStream = null;
                        try{
                            attStream = new ByteArrayOutputStream();
                            attStream.write(att.getContent());
    //                        pdfArray [++frmIndex] = attStream;
                        }catch(Throwable th){
                            UtilFactory.log("Warning:  not able to attach "+att.getContentId()+". Continue with next attachment",th,"S2SPrintForm","getPDFStream");
                            continue;
                        }finally{
                            if(attStream!=null){
                                attStream.flush();
                                attStream.close();
                            }
                        }
                        pdfBaosList.add(attStream);
//                        ++size;
//                        bookmarks[frmIndex] = att.getContentId();
                        bookmarksList.add("   ATT : "+att.getContentId());
                    }
                }
                
            }
            if(isValidationRequired)
                validator.isValidationSuccessful();
        }catch(IOException ex){
            UtilFactory.log(ex.getMessage(),ex,"S2SPrintForm", "getPDFStream");
            throw new CoeusException(ex.getMessage());
        }catch(DBException ex){
            UtilFactory.log(ex.getMessage(),ex,"S2SPrintForm", "getPDFStream");
            throw new CoeusException(ex.getMessage());
        }
        ByteArrayOutputStream[] pdfArray = new ByteArrayOutputStream[pdfBaosList.size()];
        for(int frmIndex=0;frmIndex<pdfArray.length;frmIndex++){
            pdfArray[frmIndex] = (ByteArrayOutputStream)pdfBaosList.get(frmIndex);
        }
        pdfBaosList = null;
        return pdfArray;
    }
    
    public String[] getBookmarks(Vector formInfoBeans) {
//        FormInfoBean formInfo=null;
//        int size = formInfoBeans.size();
//        String[] bookmarks = new String[size];
//        for(int frmIndex=0;frmIndex<size;frmIndex++){
//            formInfo = (FormInfoBean)formInfoBeans.get(frmIndex);
//            bookmarks[frmIndex] = formInfo.getFormName();
//        }
//        return bookmarks;
        String[] bookmarks = new String[bookmarksList.size()];
        for(int frmIndex=0;frmIndex<bookmarks.length;frmIndex++){
            bookmarks[frmIndex] = (String)bookmarksList.get(frmIndex);
        }
        return bookmarks;
//        return bookmarksList.toArray();
    }
    
    private HashMap attachmentMap;
    private S2SValidator validator;
    private byte[] invokeFormStream(String nameSpace,S2SHeader headerParam,boolean submitted, String formName)
    throws S2SValidationException,CoeusException,DBException{
        Hashtable bindings = BindingFileReader.getBindings();
        BindingInfoBean bindInfo = (BindingInfoBean)bindings.get(nameSpace);
        try{
             
             //case 2333
          //Delete all autogenrated attachments before starting XML creation
        	if(!submitted){
        		new S2STxnBean().deleteAutoGenNarratives(headerParam.getSubmissionTitle());
        	}
        	S2SBaseStream baseStream;
        	Object obj;
        	String templateName;
        	String packageName;
        	if(bindInfo!=null){
        //end case 2333
	            Class streamClass = Class.forName(bindInfo.getClassName());
	            baseStream = (S2SBaseStream)streamClass.newInstance();
	            attachmentMap = new HashMap();
	            baseStream.setAttachmentMap(attachmentMap);
	            obj = baseStream.getStream(headerParam.getStreamParams());
	            templateName = bindInfo.getTemplateName();
	            packageName = bindInfo.getJaxbPkgName();
        	}else{
        		baseStream = new UserAttachedFormStreamGenerator();
	            attachmentMap = new HashMap();
	            baseStream.setAttachmentMap(attachmentMap);
        		HashMap map = new HashMap();
        		map.put("PROPOSAL_NUMBER", headerParam.getSubmissionTitle());
        		map.put("NAMESPACE", nameSpace);
        		map.put("FORM_NAME", formName);
        		FormAttachmentExtractService extractService = new FormAttachmentExtractService();
        		templateName = extractService.createTemplateName(nameSpace);
        		packageName = extractService.createPackageName(nameSpace);
        		obj = baseStream.getStream(map);
        	}
        	if(obj==null){
        		throw new CoeusException("Coeus does not support the form "+formName+". Please upload the adobe form to the User Attached Form section to avail this form");
        	}
            validator.checkError(obj,packageName);
            validator.isValidationSuccessful();
            /*
             *Fix # 2330
             */
            return generatePdf(templateName,packageName,obj);
            
        }catch(ClassNotFoundException clEx){
            String msg = bindInfo.getClassName()  +
            "not found. Make sure that the class name " +
            "given in the xml file is correct and exist";
            UtilFactory.log(msg,clEx,"S2SPrintForm", "invokeFormStream");
            clEx.printStackTrace();
            throw new CoeusException(msg);
        }catch(InstantiationException iEx){
            String msg = "Not able to instantiate the class "+bindInfo.getClassName()+
            " Make sure that the class has got the default constructor " +
            "with no parameter";
            iEx.printStackTrace();
            UtilFactory.log(msg,iEx,"S2SPrintForm", "invokeFormStream");
            throw new CoeusException(msg);
        }catch(ClassCastException ccEx){
            String msg = bindInfo.getClassName()+ " is not a S2SBaseStream"+
            " Make sure that the class extends edu.mit.coeus.S2SBaseStream";
            ccEx.printStackTrace();
            UtilFactory.log(msg,ccEx,"S2SValidator", "attachForm");
            throw new CoeusException(msg);
        }catch(IllegalAccessException ilEx){
            String msg = " getStream method of " +bindInfo.getClassName()+
            " is not defined as public";
            ilEx.printStackTrace();
            UtilFactory.log(msg,ilEx,"S2SPrintForm", "invokeFormStream");
            throw new CoeusException(msg);
        }catch(CoeusException cx){
            //added by ele feb 22 2006
            throw cx;
        }catch(S2SValidationException cx){
            throw cx;
        }catch(Exception ex){
            String msg = "Exception while creating the stream for the form" +
            		formName;
            ex.printStackTrace();
            UtilFactory.log(msg,ex,"S2SPrintForm", "invokeFormStream");
            throw new CoeusException(msg);
        }
    }
    
    private byte[] generatePdf(String proposalNumber,String selFrmNS,String xmlStr) throws CoeusException{
        Hashtable bindings = BindingFileReader.getBindings();
        BindingInfoBean bindInfo = (BindingInfoBean)bindings.get(selFrmNS);
        String templateName;
        String packageName;
        if(bindInfo==null){
        	FormAttachmentExtractService extractService = new FormAttachmentExtractService();
        	templateName = extractService.createTemplateName(selFrmNS);
        	packageName = extractService.createPackageName(selFrmNS);
        }else{
        	templateName = bindInfo.getTemplateName();
        	packageName = bindInfo.getJaxbPkgName();
        }
        String frmXpath = "//*[namespace-uri(.) = '"+selFrmNS+"']";
        
        String frmAttXpath = "//*[namespace-uri(.) = '"+selFrmNS+"']//*[local-name(.) = 'FileLocation' and namespace-uri(.) = 'http://apply.grants.gov/system/Attachments-V1.0']";

        //        if(bindInfo.isNsChanged()){
        //            xmlStr = Converter.replaceAll(xmlStr,"\""+selFrmNS+"\"", "\""+bindInfo.getCgdNameSpace()+"\"");
        //            frmXpath = "//*[namespace-uri(.) = '"+bindInfo.getCgdNameSpace()+"']";
        //        }
        Document newDoc = null;
        try{
            XPathExecutor executer = new XPathExecutor(xmlStr);
            org.w3c.dom.Node d = executer.getNode(frmXpath);
            newDoc = Converter.node2Dom(d);
            NodeList attList = XPathAPI.selectNodeList(d, frmAttXpath);
            int attLen = attList.getLength();
            attachmentMap = new LinkedHashMap();
            for(int i=0;i<attLen;i++){
                Node attNode = attList.item(i);
                String contentId = ((Element)attNode).getAttributeNS("http://apply.grants.gov/system/Attachments-V1.0","href");
//                Attachment attBean = subTxn.getAttachment(proposalNumber,contentId);
                Attachment attBean = subTxn.getAttachmentBean(proposalNumber,contentId);
                attachmentMap.put(contentId,attBean);
            }
        }catch(Exception tEx){
            UtilFactory.log(tEx.getMessage(),tEx,"S2SPrintForm", "generatePdf");
            throw new CoeusException(tEx.getMessage());
        }
        return generatePdf(templateName,packageName,newDoc);
    }
    private byte[] generatePdf(String templateName,String packageName,Object xmlObject)
    throws CoeusException{
        try{
            InputStream inStream = getClass().getResourceAsStream("/"+templateName);
            BufferedInputStream bis = new BufferedInputStream(inStream);
            int ii = bis.available();
            byte[] templBytes = new byte[bis.available()];
            bis.read(templBytes);
            Document xmlDoc;
            if(xmlObject instanceof org.w3c.dom.Document){
                xmlDoc = (Document)xmlObject;
            }else{
                xmlDoc = xmlGen.marshelObject(xmlObject,packageName,true);
                String xml = new String(templBytes);
                Document templDoc = Converter.string2Dom(new String(templBytes));
                NamedNodeMap nameNodes = templDoc.getDocumentElement().getAttributes();
                int attLength = nameNodes.getLength();
                ArrayList nsList = new ArrayList();
                
                for(int i=0;i<attLength;i++){
                    Node attNode = nameNodes.item(i);
                    String nodeName = attNode.getNodeName();
                    if(nodeName.indexOf("xmlns")!=-1){
                        nsList.add(attNode.getNodeValue());
                    }
                }
                String xmlStr = Converter.replaceAllCgdNS(xmlDoc,nsList);
                xmlDoc = Converter.string2Dom(xmlStr);
            }
            
            //UtilFactory.log("XML string :: " + xmlStr); // JM for debug
            
            return xmlGen.generatePdfBytes(xmlDoc,templBytes);
        }catch(IOException ioEx){
            UtilFactory.log("Not able to read template "+templateName,ioEx,"S2SPrintForm", "generatePdf");
            throw new CoeusException("Not able to read template "+packageName);
        }
    }
}
