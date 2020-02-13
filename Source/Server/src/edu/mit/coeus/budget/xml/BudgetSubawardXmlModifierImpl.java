/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.mit.coeus.budget.xml;

import com.lowagie.text.pdf.PdfReader;
import edu.mit.coeus.budget.bean.BudgetDataTxnBean;
import edu.mit.coeus.budget.bean.BudgetPeriodBean;
import edu.mit.coeus.budget.bean.BudgetSubAwardAttachmentBean;
import edu.mit.coeus.budget.bean.BudgetSubAwardBean;
import edu.mit.coeus.budget.bean.BudgetSubAwardDetailBean;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.s2s.util.S2SHashValue;
import edu.mit.coeus.s2s.validator.BindingFileReader;
import edu.mit.coeus.s2s.validator.BindingInfoBean;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.TypeConstants;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.utils.documenttype.DocumentType;
import edu.mit.coeus.utils.documenttype.DocumentTypeChecker;
import edu.mit.coeus.utils.query.Equals;
import edu.mit.coeus.utils.query.GreaterThan;
import gov.grants.apply.system.global_v1.HashValueType;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.TransformerException;
import javax.xml.xpath.XPathConstants;
import org.apache.xml.security.utils.Base64;
import org.apache.xpath.XPathAPI;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author sharathk
 */
public class BudgetSubawardXmlModifierImpl implements BudgetSubawardXmlModifier{

    private static final String globhashValue = "glob:HashValue";
    private static final String globHashAlgorithm = "glob:hashAlgorithm";
    //private static final String algorithm = "SHA-1";
    private static final String attFileName = "att:FileName";
    private static final String fileLocation = "att:FileLocation";
    private static final String fileContentId = "att:href";

    public byte[] modifyXml(byte[] xml){
        return null;
    }

    public byte[] modifyXml(PdfReader pdfReader){
        return  null;
    }

    private void changeDataTypeForNumberOfOtherPersons(Document document) throws Exception{
        NodeList otherPesronsCountNodes =  XPathAPI.selectNodeList(document,"//*[local-name(.)='OtherPersonnelTotalNumber']");
        for (int i = 0; i < otherPesronsCountNodes.getLength(); i++) {
            Node countNode = otherPesronsCountNodes.item(i);
            String value = getValue(countNode);

            if(value!=null && value.length()>0 && value.indexOf('.')!=-1){
                int intVal = Double.valueOf(value).intValue();
                setValue(countNode,""+intVal);
            }
        }
    }


    private void setValue(Node node, String value) {
        Node child = null;
        for (child = node.getFirstChild(); child != null;
             child = child.getNextSibling()) {
             if(child.getNodeType()==Node.TEXT_NODE){
                child.setNodeValue(value);
                break;
             }
        }
    }

    public void removeAllEmptyNodes(Document document,String xpath,int parentLevel) throws TransformerException {
        NodeList emptyElements =  XPathAPI.selectNodeList(document,xpath);
        //int size = emptyElements.getLength();
        for (int i = emptyElements.getLength()-1; i > -1; i--){
              Node nodeToBeRemoved = emptyElements.item(i);
              int hierLevel = parentLevel;
              while(hierLevel-- > 0){
                  nodeToBeRemoved = nodeToBeRemoved.getParentNode();
              }
              nodeToBeRemoved.getParentNode().removeChild(nodeToBeRemoved);
        }
        //Making sure all elements are removed.
        NodeList moreEmptyElements =  XPathAPI.selectNodeList(document,xpath);
        if(moreEmptyElements.getLength()>0){
            removeAllEmptyNodes(document,xpath,parentLevel);
        }
    }

    public Element copyElementToName(Element element,String tagName) {
        Element newElement = element.getOwnerDocument().createElement(tagName);
        NamedNodeMap attrs = element.getAttributes();
        for (int i = 0; i < attrs.getLength(); i++) {
            Node attribute = attrs.item(i);
            newElement.setAttribute(attribute.getNodeName(),attribute.getNodeValue());
        }
        for (int i = 0; i < element.getChildNodes().getLength(); i++) {
            newElement.appendChild(element.getChildNodes().item(i).cloneNode(true));
        }
        return newElement;
    }

    public BudgetSubAwardBean updateXML(byte xmlContents[], Map fileMap, BudgetSubAwardBean budgetSubAwardBean)throws Exception {
        // 4122: Upgrade Stylevision - Start
//        //javax.xml.parsers.DocumentBuilderFactory domParserFactory = javax.xml.parsers.DocumentBuilderFactory.newInstance();
//        javax.xml.parsers.DocumentBuilderFactory domParserFactory = new  com.sun.org.apache.xerces.internal.jaxp.DocumentBuilderFactoryImpl();
        DocumentBuilderFactory domParserFactory = DocumentBuilderFactory.newInstance();
        // 4122: Upgrade Stylevision - End
        domParserFactory.setNamespaceAware(true);
        javax.xml.parsers.DocumentBuilder domParser = domParserFactory.newDocumentBuilder();
        domParserFactory.setIgnoringElementContentWhitespace(true);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(xmlContents);
        org.w3c.dom.Document document = domParser.parse(byteArrayInputStream);
        byteArrayInputStream.close();
        if (document != null) {
            //Node node;
            String formName, namespace;
            Element element = document.getDocumentElement();
            namespace = element.getNamespaceURI();
            formName = element.getNodeName();
            //NamedNodeMap map = element.getAttributes();
            //int lastIndex = element.getNodeName().indexOf(':') == -1 ? element.getNodeName().length() : element.getNodeName().indexOf(':');
            //String namespaceHolder = element.getNodeName().substring(0, lastIndex);
            //node = map.getNamedItem("xmlns:" + namespaceHolder);
            //NodeList nodeList = element.getElementsByTagNameNS(namespace1, namespaceHolder);

            //namespace = node.getNodeValue();
            BindingInfoBean bindingInfoBean = BindingFileReader.get(namespace);
            if(bindingInfoBean != null) {
                formName = bindingInfoBean.getFormName();
            }
            budgetSubAwardBean.setNamespace(namespace);
            budgetSubAwardBean.setFormName(formName);
        }
        //scan document again with setNamespaceAware(false) so as to work with namespaces.
        domParserFactory.setNamespaceAware(false);
        domParser = domParserFactory.newDocumentBuilder();
        domParserFactory.setIgnoringElementContentWhitespace(true);
        byteArrayInputStream = new ByteArrayInputStream(xmlContents);
        document = domParser.parse(byteArrayInputStream);

        String xpathEmptyNodes = "//*[not(node()) and local-name(.) != 'FileLocation' and local-name(.) != 'HashValue' and local-name(.) != 'FileName']";// and not(FileLocation[@href])]";// and string-length(normalize-space(@*)) = 0 ]";
        String xpathOtherPers = "//*[local-name(.)='ProjectRole' and local-name(../../.)='OtherPersonnel' and count(../NumberOfPersonnel)=0]";
        //String completeXpath = xpathEmptyNodes + " or "+xpathOtherPers;
        removeAllEmptyNodes(document,xpathEmptyNodes,0);
        removeAllEmptyNodes(document,xpathOtherPers,1);
        removeAllEmptyNodes(document,xpathEmptyNodes,0);
        changeDataTypeForNumberOfOtherPersons(document);


        //Test only remove later - START
        /*FileOutputStream bos1 = new FileOutputStream("C:/Temp/subaward/CoeusPHS398.xml");
        javax.xml.transform.stream.StreamResult result1 = new javax.xml.transform.stream.StreamResult(bos1);
        javax.xml.transform.dom.DOMSource source1 = new javax.xml.transform.dom.DOMSource(document);
        javax.xml.transform.Transformer transformer1 = javax.xml.transform.TransformerFactory.newInstance().newTransformer();
        transformer1.setOutputProperty(javax.xml.transform.OutputKeys.INDENT, "yes");
        transformer1.transform(source1, result1);
        */
        //Test only Remove Later
        
        NodeList budgetYearList =  XPathAPI.selectNodeList(document,"//*[local-name(.) = 'BudgetYear']");
        for(int i=0;i<budgetYearList.getLength();i++){
            Node bgtYearNode = budgetYearList.item(i);
            String period = getValue(XPathAPI.selectSingleNode(bgtYearNode,"BudgetPeriod"));
            Element newBudgetYearElement = copyElementToName((Element)bgtYearNode,bgtYearNode.getNodeName()+period);
            bgtYearNode.getParentNode().replaceChild(newBudgetYearElement,bgtYearNode);
        }
        budgetSubAwardBean.setPDFSubAwardPeriodDetails(getSubAwardPeriodDetails(document,budgetSubAwardBean));
        
        Node oldroot = document.removeChild(document.getDocumentElement());
        Node newroot = document.appendChild(document.createElement("Forms"));
        newroot.appendChild(oldroot);



//        Element el = document.getDocumentElement();
//        el.setAttribute("xmlns:xsi","http://www.w3.org/2001/XMLSchema-instance");
//        el.setAttribute("xsi:schemaLocation","http://apply.grants.gov/forms/RR_Budget-V1.1 http://atapply.grants.gov/forms/schemas/RR_Budget-V1.1.xsd");
//        String str = Converter.doc2String(document);
//        UtilFactory.logFile("c:/coeus/build/Reports","Sabaward.xml",str);

        //getElementsByTagName retreives elements in the order in which they are encountered.
        org.w3c.dom.NodeList lstFileName = document.getElementsByTagName(attFileName);
        org.w3c.dom.NodeList lstFileLocation = document.getElementsByTagName(fileLocation);
        org.w3c.dom.NodeList lstHashValue = document.getElementsByTagName(globhashValue);

        //check if all nodelist have same size, else something wrong.
        if((lstFileName.getLength() != lstFileLocation.getLength()) || (lstFileLocation.getLength() != lstHashValue.getLength())) {
            throw new CoeusException("Tag occurances mismatch in XML File");
        }

        org.w3c.dom.Node fileNode, hashNode;
        org.w3c.dom.NamedNodeMap fileNodeMap, hashNodeMap;
        String fileName;
        byte fileBytes[];
        String hashAlgorithm;
        HashValueType hashValueType;
        String hashValue;
        String contentId;
        List attachmentList = new ArrayList();
        DocumentTypeChecker documentTypeChecker = new DocumentTypeChecker();
        for(int index = 0; index < lstFileName.getLength(); index++) {
            fileNode = lstFileName.item(index);
            //fileName = fileNode.getTextContent();
            if(fileNode.getFirstChild() == null){
                continue;//no attachments
            }
            fileName = fileNode.getFirstChild().getNodeValue();
            //Get the File from fileMap
            fileBytes = (byte[])fileMap.get(fileName);
            if(fileBytes == null) {
                throw new CoeusException("FileName mismatch in XML and PDF extracted file");
            }

            //Generate hash value for the file contents
            hashValueType =  S2SHashValue.getValue(fileBytes);
            hashAlgorithm = hashValueType.getHashAlgorithm();
            byte hashBytes[] = hashValueType.getValue();
            //hashValue = new String(hashBytes);
            hashValue = Base64.encode(hashBytes);
            //hashValue = new String("Hash value"+index);

            //include the hashvalue in xml document
            hashNode = lstHashValue.item(index);
            hashNodeMap = hashNode.getAttributes();
            //hashNode.setTextContent(hashValue);
            Node temp = document.createTextNode(hashValue);
            hashNode.appendChild(temp);

            hashNode = hashNodeMap.getNamedItem(globHashAlgorithm);
            //hashNode.setTextContent(hashAlgorithm);
            hashNode.setNodeValue(hashAlgorithm);

            //retreive content Id
            fileNode = lstFileLocation.item(index);
            fileNodeMap = fileNode.getAttributes();
            fileNode = fileNodeMap.getNamedItem(fileContentId);
            //contentId = fileNode.getTextContent();
            //contentId = fileNode.getFirstChild().getNodeValue();
            contentId = fileNode.getNodeValue();

            //Update Budget Subaward Bean
            BudgetSubAwardAttachmentBean budgetSubAwardAttachmentBean = new BudgetSubAwardAttachmentBean();
            budgetSubAwardAttachmentBean.setAttachment(fileBytes);
            budgetSubAwardAttachmentBean.setContentId(contentId);

            DocumentType documentType = null;
            try {
                documentType = documentTypeChecker.getDocumentType(fileBytes);
            }catch(Exception exception) {
                //Could Not Determine Document Type
                UtilFactory.log(exception.getMessage(), exception, "BudgetSubAwardTxnBean","checkAndUpdate");
                budgetSubAwardAttachmentBean.setContentType(null);
            }
            if(documentType != null){
                budgetSubAwardAttachmentBean.setContentType(documentType.getMimeType());
            }

            budgetSubAwardAttachmentBean.setProposalNumber(budgetSubAwardBean.getProposalNumber());
            budgetSubAwardAttachmentBean.setVersionNumber(budgetSubAwardBean.getVersionNumber());
            budgetSubAwardAttachmentBean.setSubAwardNumber(budgetSubAwardBean.getSubAwardNumber());
            budgetSubAwardAttachmentBean.setAcType(TypeConstants.INSERT_RECORD);

            attachmentList.add(budgetSubAwardAttachmentBean);


        }//End For
        budgetSubAwardBean.setAttachments(attachmentList);

        //org.w3c.dom.Node node = nodeList.item(0);
        //node.setTextContent("hash Goes here");
        //org.w3c.dom.NamedNodeMap namedNodeMap = node.getAttributes();
        //node = namedNodeMap.getNamedItem(globHashAlgorithm);
        //node.setTextContent(algorithm);


        //Transform Document
        javax.xml.transform.Transformer transformer = javax.xml.transform.TransformerFactory.newInstance().newTransformer();
        transformer.setOutputProperty(javax.xml.transform.OutputKeys.INDENT, "yes");

        //initialize StreamResult with File object to save to file
        //javax.xml.transform.stream.StreamResult result = new javax.xml.transform.stream.StreamResult(new java.io.StringWriter());
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        javax.xml.transform.stream.StreamResult result = new javax.xml.transform.stream.StreamResult(bos);
        javax.xml.transform.dom.DOMSource source = new javax.xml.transform.dom.DOMSource(document);
        transformer.transform(source, result);

        budgetSubAwardBean.setSubAwardXML(new String(bos.toByteArray()).toCharArray());
        bos.close();
        return budgetSubAwardBean;

    }
    
    private static String getValue(Node node){
        String textValue = "";
        Node child = null;
        for (child = node.getFirstChild(); child != null;
             child = child.getNextSibling()) {
             if(child.getNodeType()==Node.TEXT_NODE){
                textValue = child.getNodeValue();
                break;
             }
        }
        return textValue.trim();
    }
    
    // Added for COEUSQA-2115 Subaward budgeting for Proposal Development - Start
    /**
     * Method to get the sub award details form XML, sub award details will be extracted from the XML only when periods are generated in the primary budget
     * @param document 
     * @param budgetSubAwardBean 
     * @throws javax.xml.transform.TransformerException 
     * @throws edu.mit.coeus.utils.dbengine.DBException 
     * @throws edu.mit.coeus.exception.CoeusException 
     * @return vecSubAwardPeriodDetail
     */    
     public Vector getSubAwardPeriodDetails(Document document, BudgetSubAwardBean budgetSubAwardBean) throws TransformerException, DBException, CoeusException{
        NodeList budegtPDFPeriods =  XPathAPI.selectNodeList(document,"//*[local-name(.) = 'BudgetPeriod']");
        int pdfBudgetPeriodCount = budegtPDFPeriods.getLength();
        Vector vecSubAwardPeriodDetail = new Vector();
        BudgetDataTxnBean budgetDataTxnBean = new BudgetDataTxnBean();
        CoeusVector cvBudgetPeriod = budgetDataTxnBean.getBudgetPeriods(budgetSubAwardBean.getProposalNumber(),budgetSubAwardBean.getVersionNumber());
        // When the PDF have period details and primary budget have periods        
        if(pdfBudgetPeriodCount > 0 && cvBudgetPeriod!= null && cvBudgetPeriod.size() > 0){//editted for COEUSQA-4062/COEUSQA-4061 && cvBudgetPeriod.size() > 1){
           /*
            * commented for COEUSQA-4062/COEUSQA-4061
            CoeusVector cvBudgetDetails = budgetDataTxnBean.getBudgetDetail(budgetSubAwardBean.getProposalNumber(),budgetSubAwardBean.getVersionNumber());
            GreaterThan greaterThan1BudgetPeriod = new GreaterThan("budgetPeriod",new Integer(1));
            CoeusVector cvBudgetDetailAfter1Period = cvBudgetDetails.filter(greaterThan1BudgetPeriod);
           *commented for COEUSQA-4062/COEUSQA-4061
           */
            // Period details will be extracted only when periods are generated in the primary budget or primary budget has only one period           
            if(budgetDataTxnBean.isPeriodsGenerated(budgetSubAwardBean.getProposalNumber(),budgetSubAwardBean.getVersionNumber()) ||  cvBudgetPeriod.size() == 1){
                int budgetPeriodCount = cvBudgetPeriod.size();
                // When XML(PDF) have more periods than the primary budget, primary budget periods will be iterated and check for the start date and end date in the XML(PDF)
                // If matches, period details in the XML will be update to the BudgetSubawardDetailBean with primary budget period number
                if(pdfBudgetPeriodCount >= budgetPeriodCount){
                    for(int budgetPeriodIndex=0;budgetPeriodIndex<budgetPeriodCount;budgetPeriodIndex++){
                        BudgetPeriodBean budgetPeriodBean = (BudgetPeriodBean)cvBudgetPeriod.get(budgetPeriodIndex);
                        BudgetSubAwardDetailBean subAwardDetailBean = null;
                        for(int budgetPDFPeriodIndex=0;budgetPDFPeriodIndex<pdfBudgetPeriodCount;budgetPDFPeriodIndex++){
                            Node bgtPeriodNode = budegtPDFPeriods.item(budgetPDFPeriodIndex);
                            String budgetPeriod = getValue(bgtPeriodNode);
                            String elementName = "//*[local-name(.) = 'BudgetYear"+(budgetPeriod)+"']";
                            NodeList budegtPeriodList =  XPathAPI.selectNodeList(document,elementName);
                            if(budegtPeriodList != null && budegtPeriodList.getLength() > 0){
                                Node bgtYearNode = budegtPeriodList.item(0);
                                Node startDateNode = XPathAPI.selectSingleNode(bgtYearNode,"BudgetPeriodStartDate");
                                Node endDateNode = XPathAPI.selectSingleNode(bgtYearNode,"BudgetPeriodEndDate");
                                if(startDateNode != null && endDateNode != null){
                                    String startDate = getValue(startDateNode);
                                    java.sql.Date pdfPeriodStartDate = null;
                                    java.sql.Date pdfPeriodEndDate = null;
                                    if(startDate != null && !"".equals(startDate)){
                                        pdfPeriodStartDate = java.sql.Date.valueOf(startDate);
                                    }
                                    String endDate = getValue(endDateNode);
                                    if(endDate != null && !"".equals(endDate)){
                                        pdfPeriodEndDate = java.sql.Date.valueOf(endDate);
                                    }
                                    if(budgetPeriodBean.getStartDate().equals(pdfPeriodStartDate) && budgetPeriodBean.getEndDate().equals(pdfPeriodEndDate)){
                                        subAwardDetailBean = new BudgetSubAwardDetailBean();
                                        subAwardDetailBean.setProposalNumber(budgetSubAwardBean.getProposalNumber());
                                        subAwardDetailBean.setVersionNumber(budgetSubAwardBean.getVersionNumber());
                                        subAwardDetailBean.setSubAwardNumber(budgetSubAwardBean.getSubAwardNumber());
                                        subAwardDetailBean.setBudgetPeriod(budgetPeriodBean.getBudgetPeriod());
                                        Node directCostNode = XPathAPI.selectSingleNode(bgtYearNode,"DirectCosts");
                                        if(directCostNode != null){
                                            String directCost = getValue(directCostNode);
                                            if(directCost != null && !"".equals(directCost)){
                                                subAwardDetailBean.setDirectCost(Double.parseDouble(directCost));
                                            }
                                        }
                                        
                                        Node indirectCosts =   XPathAPI.selectSingleNode(bgtYearNode,"IndirectCosts");
                                        if(indirectCosts != null){
                                            Node totalIndirectCostNode = XPathAPI.selectSingleNode(indirectCosts,"TotalIndirectCosts");
                                            if(totalIndirectCostNode != null){
                                                String totalIndirectCost =  getValue(totalIndirectCostNode);
                                                if(totalIndirectCost != null && !"".equals(totalIndirectCost)){
                                                    subAwardDetailBean.setIndirectCost(Double.parseDouble(totalIndirectCost));
                                                }
                                            }
                                        }
                                        subAwardDetailBean.setPeriodStartDate(pdfPeriodStartDate);
                                        subAwardDetailBean.setPeriodEndDate(pdfPeriodEndDate);
                                        subAwardDetailBean.setAcType(TypeConstants.INSERT_RECORD);
                                        break;
                                    }
                                }
                            }
                        }
                        
                        if(subAwardDetailBean != null){
                            vecSubAwardPeriodDetail.add(subAwardDetailBean);
                        }else{
                            // When start date and end date in the primary budget is not matched with XML(PDF)                            
                            return null;
                        }
                    }
                // When  primary budget have more periods than XML(PDF), XML(PDF) periods will be iterated and check for the start date and end date in the primary budget
                // If matches, period details in the XML will be update to the BudgetSubawardDetailBean with primary budget period number    
                }else if(pdfBudgetPeriodCount < budgetPeriodCount){
                    for(int budgetPDFPeriodIndex=0;budgetPDFPeriodIndex<pdfBudgetPeriodCount;budgetPDFPeriodIndex++){
                        Node bgtPeriodNode = budegtPDFPeriods.item(budgetPDFPeriodIndex);
                        String budgetPeriod = getValue(bgtPeriodNode);
                        String elementName = "//*[local-name(.) = 'BudgetYear"+(budgetPeriod)+"']";
                        NodeList budegtPeriodList =  XPathAPI.selectNodeList(document,elementName);
                        if(budegtPeriodList != null && budegtPeriodList.getLength() > 0){
                            Node bgtYearNode = budegtPeriodList.item(0);
                            Node startDateNode = XPathAPI.selectSingleNode(bgtYearNode,"BudgetPeriodStartDate");
                            Node endDateNode = XPathAPI.selectSingleNode(bgtYearNode,"BudgetPeriodEndDate");
                            if(startDateNode != null && endDateNode != null){
                                String startDate = getValue(startDateNode);
                                java.sql.Date pdfPeriodStartDate = null;
                                java.sql.Date pdfPeriodEndDate = null;
                                if(startDate != null && !"".equals(startDate)){
                                    pdfPeriodStartDate = java.sql.Date.valueOf(startDate);
                                }
                                String endDate = getValue(endDateNode);
                                if(endDate != null && !"".equals(endDate)){
                                    pdfPeriodEndDate = java.sql.Date.valueOf(endDate);
                                }
                                BudgetSubAwardDetailBean subAwardDetailBean = null;
                                for(int budgetPeriodIndex=0;budgetPeriodIndex<budgetPeriodCount;budgetPeriodIndex++){
                                    BudgetPeriodBean budgetPeriodBean = (BudgetPeriodBean)cvBudgetPeriod.get(budgetPeriodIndex);
                                    if(budgetPeriodBean.getStartDate().equals(pdfPeriodStartDate) && budgetPeriodBean.getEndDate().equals(pdfPeriodEndDate)){
                                        subAwardDetailBean = new BudgetSubAwardDetailBean();
                                        subAwardDetailBean.setProposalNumber(budgetSubAwardBean.getProposalNumber());
                                        subAwardDetailBean.setVersionNumber(budgetSubAwardBean.getVersionNumber());
                                        subAwardDetailBean.setSubAwardNumber(budgetSubAwardBean.getSubAwardNumber());
                                        subAwardDetailBean.setBudgetPeriod(budgetPeriodBean.getBudgetPeriod());
                                        Node directCostNode = XPathAPI.selectSingleNode(bgtYearNode,"DirectCosts");
                                        if(directCostNode != null){
                                            String directCost = getValue(directCostNode);
                                            if(directCost != null && !"".equals(directCost)){
                                                subAwardDetailBean.setDirectCost(Double.parseDouble(directCost));
                                            }
                                        }
                                        
                                        Node indirectCosts =   XPathAPI.selectSingleNode(bgtYearNode,"IndirectCosts");
                                        if(indirectCosts != null){
                                            Node totalIndirectCostNode = XPathAPI.selectSingleNode(indirectCosts,"TotalIndirectCosts");
                                            if(totalIndirectCostNode != null){
                                                String totalIndirectCost =  getValue(totalIndirectCostNode);
                                                
                                                if(totalIndirectCost != null && !"".equals(totalIndirectCost)){
                                                    subAwardDetailBean.setIndirectCost(Double.parseDouble(totalIndirectCost));
                                                }
                                            }
                                        }
                                        subAwardDetailBean.setPeriodStartDate(pdfPeriodStartDate);
                                        subAwardDetailBean.setPeriodEndDate(pdfPeriodEndDate);
                                        subAwardDetailBean.setAcType(TypeConstants.INSERT_RECORD);
                                        break;
                                    }
                                    
                                }
                                if(subAwardDetailBean != null){
                                    vecSubAwardPeriodDetail.add(subAwardDetailBean);
                                }else{
                                    // When start date and end date in the XML(PDF) is not matched with primary budget
                                    return null;
                                }
                            }
                            
                            
                        }
                    }
                }
            }else{
                // When periods are not generated in the primary budget                
                return null;
            }
        }

        return vecSubAwardPeriodDetail;
    }
     
    /**
     * Method to get the child node from the parent node
     * @param element 
     * @param nodeName 
     * @return Node
     */
     private Node getNode(Element element,String nodeName) {
         for (int i = 0; i < element.getChildNodes().getLength(); i++) {
             if(nodeName.equalsIgnoreCase(element.getChildNodes().item(i).getNodeName())){
                 return element.getChildNodes().item(i);
             }
             
         }
         return null;
     }
    // Added for COEUSQA-2115 Subaward budgeting for Proposal Development - End
}
