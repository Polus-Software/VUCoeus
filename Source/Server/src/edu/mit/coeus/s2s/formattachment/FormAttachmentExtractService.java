package edu.mit.coeus.s2s.formattachment;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.UnmarshalException;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.TransformerException;
import javax.xml.transform.dom.DOMSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.apache.xml.security.utils.Base64;
import org.apache.xpath.XPathAPI;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.lowagie.text.pdf.PRStream;
import com.lowagie.text.pdf.PdfArray;
import com.lowagie.text.pdf.PdfDictionary;
import com.lowagie.text.pdf.PdfName;
import com.lowagie.text.pdf.PdfNameTree;
import com.lowagie.text.pdf.PdfObject;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfString;
import com.lowagie.text.pdf.XfaForm;

import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.s2s.bean.UserAttachedS2SFormAttachmentBean;
import edu.mit.coeus.s2s.bean.UserAttachedS2SFormBean;
import edu.mit.coeus.s2s.bean.UserAttachedS2STxnBean;
import edu.mit.coeus.s2s.util.Converter;
import edu.mit.coeus.s2s.util.S2SHashValue;
import edu.mit.coeus.s2s.validator.BindingFileReader;
import edu.mit.coeus.s2s.validator.BindingInfoBean;
import edu.mit.coeus.s2s.validator.S2SValidationException;
import edu.mit.coeus.s2s.validator.S2SValidator;
import edu.mit.coeus.utils.TypeConstants;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.utils.documenttype.DocumentType;
import edu.mit.coeus.utils.documenttype.DocumentTypeChecker;
import gov.grants.apply.system.global_v1.HashValueType;

public class FormAttachmentExtractService {
	private static final String DUPLICATE_FILE_NAMES = "attachments contain duplicate file names";
    private static final String XFA_NS = "http://www.xfa.org/schema/xfa-data/1.0/";
	
	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception{
		FormAttachmentExtractService formAttachmentExtractService = new FormAttachmentExtractService();
//		System.out.println(formAttachmentExtractService.createPackageName("http://apply.grants.gov/system/Attachments-V1.0"));
		UserAttachedS2SFormBean userAttachedFormBean = new UserAttachedS2SFormBean();
		byte[] pdfBytes = formAttachmentExtractService.readFile();
		userAttachedFormBean.setUserAttachedS2SPDF(pdfBytes);
		List l = formAttachmentExtractService.processPdfForm(userAttachedFormBean,null);
//		for (int i = 0; i < l.size(); i++) {
//			UserAttachedS2SFormBean formBean = (UserAttachedS2SFormBean)l.get(i);
//			String result = new String(formBean.getUserAttachedS2SXML());
//			System.out.println(result);
//		}
	}
 
	public List processPdfForm(UserAttachedS2SFormBean userAttachedFormBean) throws Exception{
		return processPdfForm(userAttachedFormBean,null);
	}
	public List processPdfForm(UserAttachedS2SFormBean userAttachedFormBean,String schemaUrl) throws Exception{
		PdfReader reader = null;
		List formBeans = new ArrayList();
		try{
			byte pdfFileContents[] = userAttachedFormBean.getUserAttachedS2SPDF();
			if(pdfFileContents==null || pdfFileContents.length==0){
				formBeans.add(userAttachedFormBean);
			}else{
		        reader = new PdfReader(pdfFileContents);
		        Map attachments = extractAttachments(reader);
				formBeans = extractAndPopulateXml(reader,userAttachedFormBean,attachments);
				if(schemaUrl!=null){
					validateXml(userAttachedFormBean,schemaUrl);
				}
			}
		}finally{
			if(reader!=null) reader.close();
		}
		return formBeans;
	}

	private byte[] readFile() throws Exception{
		RandomAccessFile f = new RandomAccessFile("/Users/geo/Coeus/s2s/4066/PHS398_Training_FIF30-V1.0.pdf", "r");
		byte[] b = new byte[(int)f.length()];
		f.read(b);
		return b;
	}

    private Map extractAttachments(PdfReader reader)throws IOException, CoeusException {
        Map fileMap = new HashMap();
        
        PdfDictionary catalog = reader.getCatalog();
        PdfDictionary names = (PdfDictionary) PdfReader.getPdfObject(catalog.get(PdfName.NAMES));
        if (names != null) {
            PdfDictionary embFiles = (PdfDictionary) PdfReader.getPdfObject(names.get(new PdfName("EmbeddedFiles")));
            if (embFiles != null) {
                HashMap embMap = PdfNameTree.readTree(embFiles);
                
                for (Iterator i = embMap.values().iterator(); i.hasNext();) {
                    PdfDictionary filespec = (PdfDictionary) PdfReader.getPdfObject((PdfObject) i.next());
                    Object[] fileInfo = unpackFile(reader, filespec);
                    if(!fileMap.containsKey(fileInfo[0])) {
                        fileMap.put(fileInfo[0], fileInfo[1]);
                    }
                }
            }
        }
        for (int k = 1; k <= reader.getNumberOfPages(); ++k) {
            PdfArray annots = (PdfArray) PdfReader.getPdfObject(reader.getPageN(k).get(PdfName.ANNOTS));
            if (annots == null)
                continue;
            for (Iterator i = annots.listIterator(); i.hasNext();) {
                PdfDictionary annot = (PdfDictionary) PdfReader.getPdfObject((PdfObject) i.next());
                PdfName subType = (PdfName) PdfReader.getPdfObject(annot.get(PdfName.SUBTYPE));
                if (!PdfName.FILEATTACHMENT.equals(subType))
                    continue;
                PdfDictionary filespec = (PdfDictionary) PdfReader.getPdfObject(annot.get(PdfName.FS));
                Object[] fileInfo = unpackFile(reader, filespec);
                if(fileMap.containsKey(fileInfo[0])) {
                    throw new CoeusException(DUPLICATE_FILE_NAMES);
                }
                fileMap.put(fileInfo[0], fileInfo[1]);
            }
        }
        
        return fileMap;
    }
    /**
     * Unpacks a file attachment.
     *
     * @param reader
     *            The object that reads the PDF document
     * @param filespec
     *            The dictonary containing the file specifications
     * @throws IOException
     */
    private Object[] unpackFile(PdfReader reader, PdfDictionary filespec)throws IOException  {
        UserAttachedS2SFormAttachmentBean userAttachedS2SFormAttachmentBean = new UserAttachedS2SFormAttachmentBean();
    	
        if (filespec == null)
            return null;
        
        PdfName type = (PdfName) PdfReader.getPdfObject(filespec.get(PdfName.TYPE));
        
        if (!PdfName.F.equals(type) && !PdfName.FILESPEC.equals(type))
            return null;
        
        PdfDictionary ef = (PdfDictionary) PdfReader.getPdfObject(filespec.get(PdfName.EF));
        if (ef == null)
            return null;
        
        PdfString fn = (PdfString) PdfReader.getPdfObject(filespec.get(PdfName.F));
        if (fn == null)
            return null;
        
        File fLast = new File(fn.toUnicodeString());
        
        PRStream prs = (PRStream) PdfReader.getPdfObject(ef.get(PdfName.F));
        if (prs == null)
            return null;
        
        
        
        byte attachmentByte[] = PdfReader.getStreamBytes(prs);
        Object[] fileInfo = new Object[2];
        fileInfo[0]=fLast.getName();
        fileInfo[1]=attachmentByte;
        return fileInfo;
    }

	private void validateXml(UserAttachedS2SFormBean userAttachedFormBean, String schemaUrl) throws Exception{
		SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.XML_NS_URI); 
		Schema schema = sf.newSchema(new URL(schemaUrl)); 
		Validator validator = schema.newValidator();
		Document xmlDOM = Converter.string2Dom(new String(userAttachedFormBean.getUserAttachedS2SXML()));
		DOMSource source = new DOMSource(xmlDOM);
		validator.validate(source);
	}

	private List extractAndPopulateXml(PdfReader reader, UserAttachedS2SFormBean userAttachedFormBean, Map attachments) throws Exception {
		List formBeans = new ArrayList();
        XfaForm xfaForm = reader.getAcroFields().getXfa();
        Node domDocument = xfaForm.getDomDocument();
        Element documentElement = ((Document) domDocument).getDocumentElement();

        Element datasetsElement = (Element) documentElement.getElementsByTagNameNS(XFA_NS, "datasets").item(0);
        Element dataElement = (Element) datasetsElement.getElementsByTagNameNS(XFA_NS, "data").item(0);
        Element grantApplicationElement = (Element) dataElement.getChildNodes().item(0);

        byte[] serializedXML = XfaForm.serializeDoc(grantApplicationElement);
        DocumentBuilderFactory domParserFactory = DocumentBuilderFactory.newInstance();
        domParserFactory.setNamespaceAware(true);
        javax.xml.parsers.DocumentBuilder domParser = domParserFactory.newDocumentBuilder();
        domParserFactory.setIgnoringElementContentWhitespace(true);
        ByteArrayInputStream byteArrayInputStream=null;
        org.w3c.dom.Document document = null;
        try{
	        byteArrayInputStream = new ByteArrayInputStream(serializedXML);
	        document = domParser.parse(byteArrayInputStream);
        }finally{
        	if(byteArrayInputStream!=null) byteArrayInputStream.close();
        }
        if (document != null) {
        	Element form = null;
        	NodeList elements = document.getElementsByTagNameNS("http://apply.grants.gov/system/MetaGrantApplication", "Forms");
            Element element = (Element)elements.item(0);
            if(element!=null){
	            NodeList formChildren = element.getChildNodes();
	            int formsCount = formChildren.getLength();
	            if(formsCount>1){
	            	//String xpathSelectedForms = "//*[namespace-uri()='http://apply.grants.gov/system/MetaGrantApplicationWrapper' and *[local-name()='SelectedOptionalForms'] " +
	            	//					"or @*[namespace-uri()='http://apply.grants.gov/system/MetaGrantApplicationWrapper'  and *[local-name()='SelectedOptionalForms']]]";
	            	//NodeList selectedFormElements =  XPathAPI.selectNodeList(document,xpathSelectedForms);
	            	NodeList selectedOptionalFormElements = document.getElementsByTagNameNS("http://apply.grants.gov/system/MetaGrantApplicationWrapper", "SelectedOptionalForms");
	            	int selectedOptionalFormsCount = selectedOptionalFormElements==null?0:selectedOptionalFormElements.getLength();
					if (selectedOptionalFormsCount > 0) {
						Element selectedFormNode = (Element) selectedOptionalFormElements.item(0);
						NodeList selectedForms = selectedFormNode.getElementsByTagNameNS("http://apply.grants.gov/system/MetaGrantApplicationWrapper","FormTagName");
						int selectedFormsCount = selectedForms == null ? 0 : selectedForms.getLength();
						if (selectedFormsCount > 0) {
							List seletctedForms = new ArrayList();
							for (int j = 0; j < selectedFormsCount; j++) {
								Element selectedForm = (Element) selectedForms.item(j);
								//NodeList selectedFormNames = selectedForm.getElementsByTagNameNS("http://apply.grants.gov/system/MetaGrantApplicationWrapper","Name-Version");
								String formName = selectedForm.getTextContent();
								seletctedForms.add(formName);

							}
							List exceptions = new ArrayList();
							for (int i = 0; i < formsCount; i++) {
								form = (Element) formChildren.item(i);
								String formNodeName = form.getLocalName();
								if (seletctedForms.contains(formNodeName)) {
									try{
										addForm(formBeans,form,userAttachedFormBean, attachments);
							        }catch(UnmarshalException ume){
							        	UtilFactory.log("Not able to create xml for the form "+formNodeName, ume, "FormAttachmentExtractService", "processForm");
							        	exceptions.add("Not able to create xml for the form "+formNodeName+" Root Cause:"+ume.getMessage()+"<br>");
							        }

								}
							}
							if(!exceptions.isEmpty()) throw new S2SValidationException(exceptions.toString());
				            if(userAttachedFormBean.getAcType()!=null && userAttachedFormBean.getAcType().equals(TypeConstants.UPDATE_RECORD) && formBeans.size()>1){
				            	throw new CoeusException("Uploaded pdf contains more than one form elements. You cannot update it if it has got more than one form element. Please delete it and use insert option instead of update");
				            }

						}
	            	}
	            }else{
	            	form = (Element) formChildren.item(0);
	            	addForm(formBeans,form,userAttachedFormBean, attachments);
	            }
            }else{
            	form = document.getDocumentElement();
            	addForm(formBeans,form,userAttachedFormBean, attachments);
            }
        }
        return formBeans;
	}
	private void addForm(List formBeans, Element form,
			UserAttachedS2SFormBean userAttachedFormBean, Map attachments) throws Exception {
		UserAttachedS2SFormBean userAttachedForm = processForm(form,userAttachedFormBean,attachments);
		if(userAttachedForm!=null){
			formBeans.add(userAttachedForm);
		}
	}

	private UserAttachedS2SFormBean processForm(Element form,UserAttachedS2SFormBean userAttachedFormBean, Map attachments) throws Exception {
		
		
        String formname = null;
        String namespaceUri = null;
        String formXML = null;
        namespaceUri = form.getNamespaceURI();
        formname = form.getLocalName();
        BindingInfoBean bindingInfoBean = BindingFileReader.get(namespaceUri);
        if(bindingInfoBean != null) {
        	return null;
        }
//		form.setAttribute("xmlns:glob", "\"http://apply.grants.gov/system/Global-V1.0\"");
        Document doc = Converter.node2Dom(form);
        String xpathEmptyNodes = "//*[not(node()) and local-name(.) != 'FileLocation' and local-name(.) != 'HashValue' and local-name(.) != 'FileName']";// and not(FileLocation[@href])]";// and string-length(normalize-space(@*)) = 0 ]";
        String xpathOtherPers = "//*[local-name(.)='ProjectRole' and local-name(../../.)='OtherPersonnel' and count(../NumberOfPersonnel)=0]";
        removeAllEmptyNodes(doc,xpathEmptyNodes,0);
        removeAllEmptyNodes(doc,xpathOtherPers,1);
        removeAllEmptyNodes(doc,xpathEmptyNodes,0);
        NodeList hashValueNodes =  doc.getElementsByTagName("glob:HashValue");
        for (int i = 0; i < hashValueNodes.getLength(); i++) {
			Node hashValue = hashValueNodes.item(i);
			((Element)hashValue).setAttribute("xmlns:glob", "http://apply.grants.gov/system/Global-V1.0");
		}
        //formXML = Converter.doc2String(doc);
    	validateForm(doc,namespaceUri);
        
		UserAttachedS2SFormBean newUserAttachedFormBean = cloneUserAttachedForm(userAttachedFormBean);
        newUserAttachedFormBean.setNamespace(namespaceUri);
        newUserAttachedFormBean.setFormName(formname);
        updateAttachmentNodes(doc,newUserAttachedFormBean,attachments);
        formXML = Converter.doc2String(doc);
        newUserAttachedFormBean.setUserAttachedS2SXML(formXML.toCharArray());
        return newUserAttachedFormBean;
		
	}

	private void validateForm(Document userAttachedFormDocument, String namespace) 
			throws JAXBException, CoeusException, DBException, S2SValidationException{
        String packageName = createPackageName(namespace);
		Object formObj = createJaxbObject(userAttachedFormDocument,packageName);
		new S2SValidator().checkError(formObj, packageName);
	}

	private UserAttachedS2SFormBean cloneUserAttachedForm(
			UserAttachedS2SFormBean userAttachedFormBean) {
		UserAttachedS2SFormBean newUserAttachedFormBean = new UserAttachedS2SFormBean();
		newUserAttachedFormBean.setUserAttachedFormNumber(userAttachedFormBean.getUserAttachedFormNumber());
		newUserAttachedFormBean.setAwUserAttachedFormNumber(userAttachedFormBean.getAwUserAttachedFormNumber());
		newUserAttachedFormBean.setDescription(userAttachedFormBean.getDescription());
		newUserAttachedFormBean.setAwDescription(userAttachedFormBean.getAwDescription());
		newUserAttachedFormBean.setUserAttachedS2SPDF(userAttachedFormBean.getUserAttachedS2SPDF());
		newUserAttachedFormBean.setPdfUpdateUser(userAttachedFormBean.getPdfUpdateUser());
		newUserAttachedFormBean.setPdfUpdateTimestamp(userAttachedFormBean.getPdfUpdateTimestamp());
		newUserAttachedFormBean.setPdfAcType(userAttachedFormBean.getPdfAcType());
		newUserAttachedFormBean.setPdfFileName(userAttachedFormBean.getPdfFileName());
		newUserAttachedFormBean.setUserAttachedS2SXML(userAttachedFormBean.getUserAttachedS2SXML());
		newUserAttachedFormBean.setNamespace(userAttachedFormBean.getNamespace());
		newUserAttachedFormBean.setFormName(userAttachedFormBean.getFormName());
		newUserAttachedFormBean.setAttachments(userAttachedFormBean.getAttachments());
		newUserAttachedFormBean.setProposalNumber(userAttachedFormBean.getProposalNumber());
		newUserAttachedFormBean.setUpdateUser(userAttachedFormBean.getUpdateUser());
		newUserAttachedFormBean.setUpdateTimestamp(userAttachedFormBean.getUpdateTimestamp());
		newUserAttachedFormBean.setAcType(userAttachedFormBean.getAcType());
		return newUserAttachedFormBean;
	}

	public void removeAllEmptyNodes(Document document,String xpath,int parentLevel) throws TransformerException {
        NodeList emptyElements =  XPathAPI.selectNodeList(document,xpath);
        for (int i = emptyElements.getLength()-1; i > -1; i--){
              Node nodeToBeRemoved = emptyElements.item(i);
              int hierLevel = parentLevel;
              while(hierLevel-- > 0){
                  nodeToBeRemoved = nodeToBeRemoved.getParentNode();
              }
              nodeToBeRemoved.getParentNode().removeChild(nodeToBeRemoved);
        }
        NodeList moreEmptyElements =  XPathAPI.selectNodeList(document,xpath);
        if(moreEmptyElements.getLength()>0){
            removeAllEmptyNodes(document,xpath,parentLevel);
        }
    }	
	private void updateAttachmentNodes(Document document, UserAttachedS2SFormBean userAttachedFormBean, Map attachments) throws Exception{
		NodeList lstFileName = document.getElementsByTagNameNS("http://apply.grants.gov/system/Attachments-V1.0", "FileName");
        NodeList lstFileLocation = document.getElementsByTagNameNS("http://apply.grants.gov/system/Attachments-V1.0", "FileLocation");
        NodeList lstHashValue = document.getElementsByTagNameNS("http://apply.grants.gov/system/Global-V1.0", "HashValue");

        if ( (lstFileName.getLength() != lstFileLocation.getLength()) || 
        		(lstFileLocation.getLength() != lstHashValue.getLength())) {
            throw new CoeusException("Attachment node occurances and number of attachments mismatch in PDF File");
        }

        org.w3c.dom.Node fileNode, hashNode;
        org.w3c.dom.NamedNodeMap fileNodeMap;
        String fileName; 
        byte fileBytes[];
        String hashAlgorithm;
        HashValueType hashValueType;
        String hashValue;
        String contentId;
        List attachmentList = new ArrayList();
        DocumentTypeChecker documentTypeChecker = new DocumentTypeChecker();
        for (int index = 0; index < lstFileName.getLength(); index++) {
            fileNode = lstFileName.item(index);
            if (fileNode.getFirstChild() == null) {
                continue;//no attachments
            }
            fileName = fileNode.getFirstChild().getNodeValue(); 
            fileBytes = (byte[]) attachments.get(fileName);
            if (fileBytes == null) {
                throw new CoeusException("FileName mismatch in XML and PDF extracted file");
            }
            hashValueType = S2SHashValue.getValue(fileBytes);
            hashAlgorithm = hashValueType.getHashAlgorithm();
            byte hashBytes[] = hashValueType.getValue();
            hashValue = Base64.encode(hashBytes);
            hashNode = lstHashValue.item(index);
            NamedNodeMap hashNodeMap = hashNode.getAttributes();
            Node temp = document.createTextNode(hashValue);
            hashNode.appendChild(temp);
//            hashNode.setNodeValue(hashAlgorithm);

            Node hashAlgorithmNode = hashNodeMap.getNamedItemNS("http://apply.grants.gov/system/Global-V1.0", "hashAlgorithm");
            hashAlgorithmNode.setNodeValue(hashAlgorithm);
            hashNodeMap.setNamedItemNS(hashAlgorithmNode);
            
            fileNode = lstFileLocation.item(index);
            fileNodeMap = fileNode.getAttributes();
            fileNode = fileNodeMap.getNamedItemNS("http://apply.grants.gov/system/Attachments-V1.0", "href");
            contentId = fileNode.getNodeValue();
            UserAttachedS2SFormAttachmentBean userAttachedFormAttachmentBean = new UserAttachedS2SFormAttachmentBean();
            userAttachedFormAttachmentBean.setAttachment(fileBytes);
            userAttachedFormAttachmentBean.setContentId(contentId);
            userAttachedFormAttachmentBean.setFilename(fileName);

            DocumentType documentType = null;
            try {
                documentType = documentTypeChecker.getDocumentType(fileBytes);
            } catch (Exception exception) {
                UtilFactory.log(exception.getMessage(), exception, "BudgetSubAwardTxnBean", "checkAndUpdate");
                userAttachedFormAttachmentBean.setContentType(null);
            }
            if (documentType != null) {
            	userAttachedFormAttachmentBean.setContentType(documentType.getMimeType());
            }

            userAttachedFormAttachmentBean.setProposalNumber(userAttachedFormBean.getProposalNumber());
            userAttachedFormAttachmentBean.setUserAttachedFormNumber(userAttachedFormBean.getUserAttachedFormNumber());
            userAttachedFormAttachmentBean.setAcType(TypeConstants.INSERT_RECORD);

            attachmentList.add(userAttachedFormAttachmentBean);
        }
        userAttachedFormBean.setAttachments(attachmentList);
	}
	/**
	 * This will fetch the xml data from database for a form, unmarshal it to JaxbObject and return it 
	 * @param proposalNumber
	 * @param namespace
	 * @return
	 * @throws JAXBException 
	 * @throws CoeusException 
	 * @throws DBException 
	 */
	public Object getJaxbObjectForNamespace(String proposalNumber,String namespace,String formName) throws 
								JAXBException, CoeusException, DBException,S2SValidationException{
		String userAttachedFormXml = new UserAttachedS2STxnBean().getUserAttachedFormXml(proposalNumber,namespace);
		if(userAttachedFormXml==null){
			throw new CoeusException("Cannot find user attached form for "+formName);
		}
        Document userAttachedFormDocument = Converter.string2Dom(userAttachedFormXml);
        String packageName = createPackageName(namespace);
        return createJaxbObject(userAttachedFormDocument,packageName);
	}

	private Object createJaxbObject(Document domDocument,String packageName) throws CoeusException,S2SValidationException,
			JAXBException {
		
		byte[] userAttachedFormNodeBytes = Converter.doc2bytes(domDocument);
//    	System.out.println(new String(userAttachedFormNodeBytes));
        JAXBContext jaxbContext = JAXBContext.newInstance(packageName);
        Unmarshaller um = jaxbContext.createUnmarshaller();
        Object jaxbObject = null;
        InputStream bgtIS = null;
        try{
        	bgtIS = new ByteArrayInputStream(userAttachedFormNodeBytes);
        	jaxbObject = um.unmarshal(bgtIS);
        }catch(Exception ex){
        	throw new S2SValidationException(ex);
        }finally{
        
        	if(bgtIS!=null)
				try {
					bgtIS.close();
				} catch (IOException e) {}
        }
		return jaxbObject;
	}

	public String createPackageName(String namespace) {
		String namespaceLowercase = namespace.toLowerCase();
		StringBuffer packageName = new StringBuffer();
		int beginIndex = namespaceLowercase.indexOf("://")+3;
		int endIndex = namespaceLowercase.length();
		String packageSubString = namespaceLowercase.subSequence(beginIndex, endIndex).toString();
		String[] tokens = packageSubString.split("/");
		for (int i = 0; i < tokens.length; i++) {
			String packageToken = tokens[i];
			String[] subTokens = packageToken.split("\\.");
			if(subTokens.length>1){
				int subTokensLength = (i==(tokens.length-1))?subTokens.length-2:subTokens.length-1;
				for (int j = subTokensLength; j >=0 ; j--) {
					String subToken = subTokens[j];
					packageName.append(subToken);
					packageName.append(".");
				}
			}else{
				packageName.append(packageToken);
				packageName.append(".");
			}
		}
		String finalPackageName = packageName.charAt(packageName.length()-1)=='.'?packageName.substring(0, packageName.length()-1):packageName.toString();
		return finalPackageName.toString().replaceAll("-", "_");
	}

	public List getAttachments(String proposalNumber, String namespace) throws DBException, CoeusException {
		return new UserAttachedS2STxnBean().getUserAttachedS2SFormAttachments(proposalNumber, namespace);
	}

	public String createTemplateName(String namespace) {
		String[] tokens = namespace.split("/");
		String formname = tokens[tokens.length-1];
		String templateName = "edu/mit/coeus/s2s/template/"+formname+".xsl";
		return templateName;
	}
	
}
