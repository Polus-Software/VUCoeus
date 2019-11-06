/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.mit.coeus.budget.xml;

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
import org.apache.xml.security.utils.Base64;
import org.apache.xpath.XPathAPI;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author sharathk
 */
public class BudgetSubawardXmlExtract implements BudgetSubawardXmlModifier {

    private static final String globhashValue = "glob:HashValue";
    private static final String globHashAlgorithm = "glob:hashAlgorithm";
    //private static final String algorithm = "SHA-1";
    private static final String attFileName = "att:FileName";
    private static final String fileLocation = "att:FileLocation";
    private static final String fileContentId = "att:href";
    private boolean alterBudgetYear = true;
    private String rootElement = null;
    private String namespace = null;

    public BudgetSubawardXmlExtract(boolean alterBudgetYear, String rootElement, String namespace) {
        this.alterBudgetYear = alterBudgetYear;
        this.rootElement = rootElement;
        this.namespace = namespace;
    }

    public BudgetSubAwardBean updateXML(byte[] xmlContents, Map fileMap, BudgetSubAwardBean budgetSubAwardBean) throws Exception {
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
            String formName;//, namespace;
            Element element = document.getDocumentElement();
            //namespace = element.getNamespaceURI();
            formName = element.getNodeName();

            //namespace = node.getNodeValue();
            BindingInfoBean bindingInfoBean = BindingFileReader.get(namespace);
            if (bindingInfoBean != null) {
                formName = bindingInfoBean.getFormName();
            }
            budgetSubAwardBean.setNamespace(namespace);
            budgetSubAwardBean.setFormName(formName);
        }
        //scan document again with setNamespaceAware(false) so as to work with namespaces.

        String xpathEmptyNodes = "//*[not(node()) and local-name(.) != 'FileLocation' and local-name(.) != 'HashValue' and local-name(.) != 'FileName']";// and not(FileLocation[@href])]";// and string-length(normalize-space(@*)) = 0 ]";
        removeAllEmptyNodes(document,xpathEmptyNodes,0);
        
        Node oldroot = null;


        NodeList nodeList = document.getElementsByTagNameNS(namespace, rootElement);
        Node childNodes[] = new Node[0];
        if (nodeList != null && nodeList.getLength() > 0) {
            childNodes = new Node[nodeList.getLength()];
            //firstDocTrainingNodes = nodeList.getLength();
            for (int nodeIndex = 0; nodeIndex < nodeList.getLength(); nodeIndex++) {
                childNodes[nodeIndex] = nodeList.item(nodeIndex);
            }//End for nodeList
        }//End if nodeList != null
        document.removeChild(document.getDocumentElement());
        Node newroot = document.appendChild(document.createElement("Forms"));
        for (int index = 0; index < childNodes.length; index++) {
            newroot.appendChild(childNodes[index]);
        }
        // Added for COEUSQA-2115 : Subaward budgeting for Proposal Development - Start
        budgetSubAwardBean.setPDFSubAwardPeriodDetails(getSubAwardPeriodDetails(document,budgetSubAwardBean));
        // Added for COEUSQA-2115 : Subaward budgeting for Proposal Development - End
        
        //getElementsByTagName retreives elements in the order in which they are encountered.
        org.w3c.dom.NodeList lstFileName = document.getElementsByTagName(attFileName);
        org.w3c.dom.NodeList lstFileLocation = document.getElementsByTagName(fileLocation);
        org.w3c.dom.NodeList lstHashValue = document.getElementsByTagName(globhashValue);

        //check if all nodelist have same size, else something wrong.
        if ((lstFileName.getLength() != lstFileLocation.getLength()) || (lstFileLocation.getLength() != lstHashValue.getLength())) {
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
        for (int index = 0; index < lstFileName.getLength(); index++) {
            fileNode = lstFileName.item(index);
            //fileName = fileNode.getTextContent();
            if (fileNode.getFirstChild() == null) {
                continue;//no attachments
            }
            fileName = fileNode.getFirstChild().getNodeValue();
            //Get the File from fileMap
            fileBytes = (byte[]) fileMap.get(fileName);
            if (fileBytes == null) {
                throw new CoeusException("FileName mismatch in XML and PDF extracted file");
            }

            //Generate hash value for the file contents
            hashValueType = S2SHashValue.getValue(fileBytes);
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
            } catch (Exception exception) {
                //Could Not Determine Document Type
                UtilFactory.log(exception.getMessage(), exception, "BudgetSubAwardTxnBean", "checkAndUpdate");
                budgetSubAwardAttachmentBean.setContentType(null);
            }
            if (documentType != null) {
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
    
    /**
     * Method to get the total indirect cost from the node
     * @param element 
     * @param rootElement 
     * @param nodeName 
     * @return nodeValue
     */
    private String getTotalIndirectCost(Element element,String rootElement,String nodeName){
        String nodeValue = "";
        for (int i = 0; i < element.getChildNodes().getLength(); i++) {
            String nodeToCheck = element.getChildNodes().item(i).getNodeName();
            if(nodeName.equalsIgnoreCase(nodeToCheck)){
                NodeList childNode = element.getChildNodes().item(i).getChildNodes();
                for (int j = 0; j < childNode.getLength(); j++) {
                    String totalIndirectNodeName = childNode.item(j).getNodeName();
                    if(totalIndirectNodeName.equalsIgnoreCase(rootElement+":TotalIndirectCosts")){
                        nodeValue = childNode.item(j).getTextContent();
                        break;
                    }
                }
            }
        }
        return nodeValue;
    }
    
    /**
     * Method to get the node value
     * @param element 
     * @param nodeName 
     * @return nodeValue
     */
    private String getNodeValue(Element element,String nodeName) {
        String nodeValue = "";
        for (int i = 0; i < element.getChildNodes().getLength(); i++) {
            if(nodeName.equalsIgnoreCase(element.getChildNodes().item(i).getNodeName())){
                nodeValue = element.getChildNodes().item(i).getTextContent();
                break;
            }
            
        }
        return nodeValue;
    }
    
    /**
     * Method to get the child node from the parent node
     * @param element 
     * @param nodeName 
     * @return node
     */
    private Node getNode(Element element,String nodeName) {
        for (int i = 0; i < element.getChildNodes().getLength(); i++) {
            if(nodeName.equalsIgnoreCase(element.getChildNodes().item(i).getNodeName())){
                return element.getChildNodes().item(i);
            }
            
        }
        return null;
    }
    
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
        NodeList budgetYearList =  XPathAPI.selectNodeList(document,"//*[local-name(.) = 'BudgetYear']");
        int pdfBudgetPeriodCount = budgetYearList.getLength();
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
       * commented for COEUSQA-4062/COEUSQA-4061
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
                            Node bgtYearNode = budgetYearList.item(budgetPDFPeriodIndex);
                            String startDate = getNodeValue((Element)bgtYearNode,rootElement+":BudgetPeriodStartDate");
                            java.sql.Date pdfPeriodStartDate = null;
                            java.sql.Date pdfPeriodEndDate = null;
                            if(startDate != null && !"".equals(startDate)){
                                pdfPeriodStartDate = java.sql.Date.valueOf(startDate);
                            }
                            String endDate = getNodeValue((Element)bgtYearNode,rootElement+":BudgetPeriodEndDate");
                            if(endDate != null && !"".equals(endDate)){
                                pdfPeriodEndDate = java.sql.Date.valueOf(endDate);
                            }
                            if(budgetPeriodBean.getStartDate().equals(pdfPeriodStartDate) && budgetPeriodBean.getEndDate().equals(pdfPeriodEndDate)){
                                subAwardDetailBean = new BudgetSubAwardDetailBean();
                                subAwardDetailBean.setProposalNumber(budgetSubAwardBean.getProposalNumber());
                                subAwardDetailBean.setVersionNumber(budgetSubAwardBean.getVersionNumber());
                                subAwardDetailBean.setSubAwardNumber(budgetSubAwardBean.getSubAwardNumber());
                                String directCost = "0";
                                String totalIndirectCost = "0";
                                /* JM 12-6-2016 updating code here for all FedNonFed forms and to allow 0 indirects */
                                //if(rootElement.equalsIgnoreCase("RR_FedNonFedBudget10")){
                                if (rootElement.toUpperCase().contains("FedNonFed".toUpperCase())) {
                                	Node directCostNode = getNode((Element)bgtYearNode,rootElement+":DirectCosts");
                                    directCost = getNodeValue((Element)directCostNode,rootElement+":FederalSummary");
                                    Node inDirectCostNode = getNode((Element)bgtYearNode,rootElement+":IndirectCosts");
                                    if (inDirectCostNode != null) {
	                                    Node totalInDirectCostNode = getNode((Element)inDirectCostNode,rootElement+":TotalIndirectCosts");
	                                    
	                                    if (totalInDirectCostNode != null) {
	                                    	totalIndirectCost = getNodeValue((Element)totalInDirectCostNode,rootElement+":FederalSummary");
	                                    }
                                    }
                                    Node costSharingNode = getNode((Element)bgtYearNode,rootElement+":TotalCosts"); 
                                    String costSharingAmount = getNodeValue((Element)costSharingNode,rootElement+":NonFederalSummary");
                                    
                                    if(costSharingAmount != null && !"".equals(costSharingAmount)){
                                        subAwardDetailBean.setCostSharingAmount(Double.parseDouble(costSharingAmount));
                                    }
                                }else{
                                    directCost = getNodeValue((Element)bgtYearNode,rootElement+":DirectCosts");
                                    totalIndirectCost =  getTotalIndirectCost((Element)bgtYearNode,rootElement, rootElement+":IndirectCosts");
                                }
                                subAwardDetailBean.setBudgetPeriod(budgetPeriodBean.getBudgetPeriod());
                                if(directCost != null && !"".equals(directCost)){
                                    subAwardDetailBean.setDirectCost(Double.parseDouble(directCost));
                                }
                                if(totalIndirectCost != null && !"".equals(totalIndirectCost)){
                                    subAwardDetailBean.setIndirectCost(Double.parseDouble(totalIndirectCost));
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
                            // When start date and end date in the primary budget is not matched with XML(PDF)                            
                            return null;
                        }
                    }
                // When  primary budget have more periods than XML(PDF), XML(PDF) periods will be iterated and check for the start date and end date in the primary budget
                // If matches, period details in the XML will be update to the BudgetSubawardDetailBean with primary budget period number    
                }else if(pdfBudgetPeriodCount < budgetPeriodCount){
                    for(int budgetPDFPeriodIndex=0;budgetPDFPeriodIndex<pdfBudgetPeriodCount;budgetPDFPeriodIndex++){
                        Node bgtYearNode = budgetYearList.item(budgetPDFPeriodIndex);
                        String startDate = getNodeValue((Element)bgtYearNode,rootElement+":BudgetPeriodStartDate");
                        java.sql.Date pdfPeriodStartDate = null;
                        java.sql.Date pdfPeriodEndDate = null;
                        if(startDate != null && !"".equals(startDate)){
                            pdfPeriodStartDate = java.sql.Date.valueOf(startDate);
                        }
                        String endDate = getNodeValue((Element)bgtYearNode,rootElement+":BudgetPeriodEndDate");
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
                                String directCost = "0";
                                String totalIndirectCost = "0";
                                /* JM 12-6-2016 updating code here for all FedNonFed forms and to allow 0 indirects */
                                //if(rootElement.equalsIgnoreCase("RR_FedNonFedBudget10")){
                                if (rootElement.toUpperCase().contains("FedNonFed".toUpperCase())) {
                                	Node directCostNode = getNode((Element)bgtYearNode,rootElement+":DirectCosts");
                                    directCost = getNodeValue((Element)directCostNode,rootElement+":FederalSummary");
                                    Node inDirectCostNode = getNode((Element)bgtYearNode,rootElement+":IndirectCosts");
                                    if (inDirectCostNode != null) {
	                                    Node totalInDirectCostNode = getNode((Element)inDirectCostNode,rootElement+":TotalIndirectCosts");
	                                    
	                                    if (totalInDirectCostNode != null) {
	                                    	totalIndirectCost = getNodeValue((Element)totalInDirectCostNode,rootElement+":FederalSummary");
	                                    }
                                    }
                                    Node costSharingNode = getNode((Element)bgtYearNode,rootElement+":TotalCosts");
                                    String costSharingAmount = getNodeValue((Element)costSharingNode,rootElement+":NonFederalSummary");
                                    
                                    if(costSharingAmount != null && !"".equals(costSharingAmount)){
                                        subAwardDetailBean.setCostSharingAmount(Double.parseDouble(costSharingAmount));
                                    }
                                 }else{
                                    directCost = getNodeValue((Element)bgtYearNode,rootElement+":DirectCosts");
                                    UtilFactory.log("Getting direct costs here");
                                    totalIndirectCost =  getTotalIndirectCost((Element)bgtYearNode,rootElement, rootElement+":IndirectCosts");
                                 }
                                subAwardDetailBean.setBudgetPeriod(budgetPeriodBean.getBudgetPeriod());
                                if(directCost != null && !"".equals(directCost)){
                                	UtilFactory.log("direct cost :: " + directCost);
                                    subAwardDetailBean.setDirectCost(Double.parseDouble(directCost));
                                }
                                if(totalIndirectCost != null && !"".equals(totalIndirectCost)){
                                    subAwardDetailBean.setIndirectCost(Double.parseDouble(totalIndirectCost));
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
            }else{
                // When periods are not generated in the primary budget                
                return null;
            }
        }
        
        return vecSubAwardPeriodDetail;
    }
    
    public void removeAllEmptyNodes(Document document, String xpath, int parentLevel) throws TransformerException {
        NodeList emptyElements = XPathAPI.selectNodeList(document, xpath);
        //int size = emptyElements.getLength();
        for (int i = emptyElements.getLength() - 1; i > -1; i--) {
            Node nodeToBeRemoved = emptyElements.item(i);
            int hierLevel = parentLevel;
            while (hierLevel-- > 0) {
                nodeToBeRemoved = nodeToBeRemoved.getParentNode();
            }
            nodeToBeRemoved.getParentNode().removeChild(nodeToBeRemoved);
        }
        //Making sure all elements are removed.
        NodeList moreEmptyElements = XPathAPI.selectNodeList(document, xpath);
        if (moreEmptyElements.getLength() > 0) {
            removeAllEmptyNodes(document, xpath, parentLevel);
        }
    }
    
}
