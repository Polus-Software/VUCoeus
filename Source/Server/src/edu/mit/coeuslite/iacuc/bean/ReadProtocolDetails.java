/*
 * ReadProtocolDetails.java
 *
 * Created on March 8, 2005, 5:30 PM
 */

package edu.mit.coeuslite.iacuc.bean;

/*
 *
 * Created on March 8, 2005, 5:36 PM
 */



import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeuslite.utils.bean.CoeusHeaderBean;
import edu.mit.coeuslite.utils.bean.MenuBean;
import edu.mit.coeuslite.utils.bean.SubHeaderBean;
import java.io.File;
import java.util.Vector;
import org.w3c.dom.Document;
import org.w3c.dom.*;


import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 *
 * @author  chandrashekara
 */
public class ReadProtocolDetails {
    private Vector vecProtocolNames;
    
    
    /** Creates a new instance of ReadMassChangeDataForm */
    public ReadProtocolDetails() {
        vecProtocolNames = new Vector();
        //readXMLData("ALL_PROTOCOLS");
        
    }
    
    public Vector readXMLData(String strProtoName, String fileName) {
        try {
            if(strProtoName != null && fileName != null){
                
                DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
                Document doc = docBuilder.parse(getClass().getResourceAsStream(fileName));//"/edu/mitweb/coeus/irb/xml/data/ProtocolDetails.xml"
                
                // normalize text representation
                doc.getDocumentElement().normalize();
                NodeList typeList = doc.getElementsByTagName("TYPE");
                int totalChars = typeList.getLength();
                
                for(int typeIndex=0; typeIndex<typeList.getLength();typeIndex++){
                    
                    Node firstCharNode = typeList.item(typeIndex);
                    Element firstElement = (Element)firstCharNode;
                    
                    NodeList columnNameList = firstElement.getElementsByTagName("COLUMN_NAME");
                    NodeList protocolTypeData = firstElement.getElementsByTagName("PROTOCOL_NAME");
                    
                    
                    for(int index=0; index <columnNameList.getLength(); index++){
                        
                        Element columnName = (Element)columnNameList.item(index);
                        Element protocolTypes = (Element)protocolTypeData.item(index);
                        
                        NodeList columnNameChilds = columnName.getChildNodes();
                        NodeList protocolTypeChilds = protocolTypes.getChildNodes();
                        
                        String protocolName  = (String)((Node)protocolTypeChilds.item(0)).getNodeValue().trim();
                        
                        String colName =(String)((Node)columnNameChilds.item(0)).getNodeValue().trim();
                        if(protocolName.equals(strProtoName)){
                            vecProtocolNames.add(colName);
                        }
                    }// end of column Type Loop
                    doc.normalize();
                }// End of protocol Type Loop
            }//End of if clause
        }catch (SAXParseException err) {
            System.out.println("** Parsing error" + ", line " + err.getLineNumber() + ", uri " + err.getSystemId());
            System.out.println(" " + err.getMessage());
            
        }catch (SAXException e) {
            Exception x = e.getException();
            ((x == null) ? e : x).printStackTrace();
            
        }catch (Throwable t) {
            t.printStackTrace();
        }
        return vecProtocolNames;
    }
    
    
    /**
     * Reads the XML for getting the data for the menu items in the page
     * @ return Vector
     */
    public Vector readXMLDataForMenu( String fileName) {
        Vector menuVector = new Vector();
        try {
            if(fileName != null){
                DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
                Document doc = docBuilder.parse(getClass().getResourceAsStream(fileName));//"/edu/mitweb/coeus/irb/xml/data/ProtocolDetails.xml"
                // normalize text representation
                doc.getDocumentElement().normalize();
                NodeList menuNodeList = doc.getElementsByTagName("MenuItem");
                int menuCount = menuNodeList.getLength();
                for (int menuIndex = 0; menuIndex<menuCount;menuIndex++) {
                    MenuBean menuBean = new MenuBean();
                    Node menuNode = menuNodeList.item(menuIndex);
                    Element menuElement = (Element)menuNode ;
                    
                    NodeList menuIdList = menuElement.getElementsByTagName("Menu_Id");
                    NodeList menuNameList = menuElement.getElementsByTagName("Menu_Name");
                    NodeList menuLinkList = menuElement.getElementsByTagName("Link");
                    NodeList menuFieldNameList = menuElement.getElementsByTagName("FieldName");
                    NodeList menuVisibleList = menuElement.getElementsByTagName("visible");
                    
                    Element menuIdElement = (Element)menuIdList.item(0);
                    Element menuNameElement = (Element)menuNameList.item(0);
                    Element menuLinkElement = (Element)menuLinkList.item(0);
                    Element menuFieldElement = (Element)menuFieldNameList.item(0);
                    Element menuVisibleElement = (Element)menuVisibleList.item(0);
                    
                    NodeList menuIdListData = menuIdElement.getChildNodes();
                    NodeList menuNameListData = menuNameElement.getChildNodes();
                    NodeList menuLinkListData = menuLinkElement.getChildNodes();
                    NodeList menuFieldListData = menuFieldElement.getChildNodes();
                    NodeList menuVisibleListData = menuVisibleElement.getChildNodes();
                    
                    String menuId = (String)((Node)menuIdListData.item(0)).getNodeValue().trim();
                    String menuName = (String)((Node)menuNameListData.item(0)).getNodeValue().trim();
                    String menuLink = (String)((Node)menuLinkListData.item(0)).getNodeValue().trim();
                    String menuField = (String)((Node)menuFieldListData.item(0)).getNodeValue().trim();
                    String menuVisible = (String)((Node)menuVisibleListData.item(0)).getNodeValue().trim();

                    menuBean.setMenuId(menuId);
                    menuBean.setMenuName(menuName);
                    menuBean.setMenuLink(menuLink);
                    menuBean.setFieldName(menuField);                    
                    if(menuVisible.equalsIgnoreCase("true")){
                        menuBean.setVisible(true);
                    }else{
                        menuBean.setVisible(false);
                    }                    

                    //menuBean.setDataSaved(false);
                    if (menuIndex == 0) {
                        menuBean.setSelected(true);
                    } else {
                        menuBean.setSelected(false);
                    }
                    menuVector.add(menuBean);
                } //Menu For loop ends
                doc.normalize();
            }
            //}//End of if clause
        }catch (SAXParseException err) {
            System.out.println("** Parsing error" + ", line " + err.getLineNumber() + ", uri " + err.getSystemId());
            System.out.println(" " + err.getMessage());
        }catch (SAXException e) {
            Exception x = e.getException();
            ((x == null) ? e : x).printStackTrace();
            
        }catch (Throwable t) {
            t.printStackTrace();
        }
        return menuVector;
    }
    
    
    public Vector readXMLDataForMainHeader( String fileName) {
        Vector headerVector = new Vector();
        try {
            if(fileName != null){
                DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
                Document doc = docBuilder.parse(getClass().getResourceAsStream(fileName));//"/edu/mitweb/coeus/irb/xml/data/ProtocolDetails.xml"
                // normalize text representation
                doc.getDocumentElement().normalize();
                NodeList headerNodeList = doc.getElementsByTagName("MenuItem");
                int  headerCount = headerNodeList.getLength();
                for (int headerIndex = 0; headerIndex<headerCount;headerIndex++) {
                    CoeusHeaderBean coeusHeaderBean = new CoeusHeaderBean();
                    Node headerNode = headerNodeList.item(headerIndex);
                    Element headerElement = (Element)headerNode ;
                    
                    NodeList headerIdList = headerElement.getElementsByTagName("Menu_Id");
                    NodeList headerNameList = headerElement.getElementsByTagName("Menu_Name");
                    NodeList headerImagePathList = headerElement.getElementsByTagName("Image_Path");
                    NodeList onLoadImageList = headerElement.getElementsByTagName("OnLoadImage");
                    NodeList headerLinkList = headerElement.getElementsByTagName("Link");
                    
                    Element headerIdElement = (Element)headerIdList.item(0);
                    Element headerNameElement = (Element)headerNameList.item(0);
                    Element headerImagePathElement = (Element)headerImagePathList.item(0);
                    Element onLoadImageElement = (Element)onLoadImageList.item(0);
                    Element headerLinkElement = (Element)headerLinkList.item(0);
                                      
                    NodeList headerIdListData = headerIdElement.getChildNodes();
                    NodeList headerNameListData = headerNameElement.getChildNodes();
                    NodeList headerImagePathListData = headerImagePathElement.getChildNodes();
                    NodeList onLoadImageListData = onLoadImageElement.getChildNodes();
                    NodeList headerLinkListData = headerLinkElement.getChildNodes();
                    
                    String headerId = (String)((Node)headerIdListData.item(0)).getNodeValue().trim();
                    String headerName = (String)((Node)headerNameListData.item(0)).getNodeValue().trim();
                    String imagePath = (String)((Node)headerImagePathListData.item(0)).getNodeValue().trim();
                    String onLoadImage = (String)((Node)onLoadImageListData.item(0)).getNodeValue().trim();
                    String headerLink = (String)((Node)headerLinkListData.item(0)).getNodeValue().trim();
                    
                    coeusHeaderBean.setHeaderId(headerId);
                    coeusHeaderBean.setHeaderName(headerName);
                    coeusHeaderBean.setImagePath(imagePath);
                    coeusHeaderBean.setOnLoadImage(onLoadImage);
                    coeusHeaderBean.setLink(headerLink);
                    headerVector.add(coeusHeaderBean);
                } //Menu For loop ends
                doc.normalize();
            }
            //}//End of if clause
        }catch (SAXParseException err) {
            System.out.println("** Parsing error" + ", line " + err.getLineNumber() + ", uri " + err.getSystemId());
            System.out.println(" " + err.getMessage());
        }catch (SAXException e) {
            Exception x = e.getException();
            ((x == null) ? e : x).printStackTrace();
            
        }catch (Throwable t) {
            t.printStackTrace();
        }
        return headerVector;
    }
    
    
    public Vector readXMLDataForSubHeader( String fileName) {
        Vector subHeaderVector = new Vector();
        try {
            if(fileName != null){
                DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
                Document doc = docBuilder.parse(getClass().getResourceAsStream(fileName));//"/edu/mitweb/coeus/irb/xml/data/ProtocolDetails.xml"
                // normalize text representation
                doc.getDocumentElement().normalize();
                NodeList subHeaderNodeList = doc.getElementsByTagName("Sub_Header");
                int  subHeaderCount = subHeaderNodeList.getLength();
                for (int subHeaderIndex = 0; subHeaderIndex<subHeaderCount;subHeaderIndex++) {
                    SubHeaderBean subHeaderBean = new SubHeaderBean();
                    Node subHeaderNode = subHeaderNodeList.item(subHeaderIndex);
                    Element subHeaderElement = (Element)subHeaderNode ;
                    
                    NodeList subHeaderIdList = subHeaderElement.getElementsByTagName("Sub_Header_Id");
                    NodeList subHeaderNameList = subHeaderElement.getElementsByTagName("Sub_Header_Name");
                    NodeList subHeaderSearchList = subHeaderElement.getElementsByTagName("Search_Window");
                    NodeList subHeaderLinkList = subHeaderElement.getElementsByTagName("Link");
                    
                    
                    Element subHeaderIdElement = (Element)subHeaderIdList.item(0);
                    Element subHeaderNameElement = (Element)subHeaderNameList.item(0);
                    Element subHeaderSearchElement = (Element)subHeaderSearchList.item(0);
                    Element subHeaderLinkElement = (Element)subHeaderLinkList.item(0);
                   // System.out.println("subHeaderSearch"+subHeaderSearchList.item(0));
                    
                    
                    NodeList subHeaderIdListData = subHeaderIdElement.getChildNodes();
                    NodeList subHeaderNameListData = subHeaderNameElement.getChildNodes();
                    NodeList subHeaderSearchElementData = null;
                    if(subHeaderSearchElement!= null){
                        subHeaderSearchElementData = subHeaderSearchElement.getChildNodes();
                    }
                    NodeList subHeaderLinkListData = subHeaderLinkElement.getChildNodes();
                    
                    
                    String subHeaderId = (String)((Node)subHeaderIdListData.item(0)).getNodeValue().trim();
                    String subHeaderName = (String)((Node)subHeaderNameListData.item(0)).getNodeValue().trim();
                    String subHeaderLink = (String)((Node)subHeaderLinkListData.item(0)).getNodeValue().trim();
                    String subHeaderSearch = "";
                    if(subHeaderSearchElementData!= null){
                         subHeaderSearch = (String)((Node)subHeaderSearchElementData.item(0)).getNodeValue().trim();
                    }

                    subHeaderBean.setSubHeaderId(subHeaderId);
                    subHeaderBean.setSubHeaderName(subHeaderName);
                    subHeaderBean.setSubHeaderLink(subHeaderLink);
                    subHeaderBean.setProtocolSearchLink(subHeaderSearch);
                    subHeaderVector.add(subHeaderBean);
                } //Menu For loop ends
                doc.normalize();
            }
            //}//End of if clause
        }catch (SAXParseException err) {
            System.out.println("** Parsing error" + ", line " + err.getLineNumber() + ", uri " + err.getSystemId());
            System.out.println(" " + err.getMessage());
        }catch (SAXException e) {
            Exception x = e.getException();
            ((x == null) ? e : x).printStackTrace();
            
        }catch (Throwable t) {
            t.printStackTrace();
        }
        return subHeaderVector;
    }
    
    
    
    public static void main(String are[]){
        ReadProtocolDetails readProtocolDetails = new ReadProtocolDetails();
        //readProtocolDetails.readXMLDataForMenu("/edu/mitweb/coeus/irb/xml/data/ProtocolMenu.xml");
        //readProtocolDetails.readXMLDataForMainHeader("/edu/mitweb/coeus/irb/xml/data/MainMenu.xml");
        readProtocolDetails.readXMLDataForSubHeader("/edu/mitweb/coeus/irb/xml/data/SubMenu.xml");
    }
}
