/*
 * ReadJSPPlaceHolder.java
 *
 * Created on April 19, 2005, 3:03 PM
 */

package edu.mit.coeuslite.utils.xmlReader;

import edu.mit.coeuslite.utils.bean.PlaceHolderBean;
import edu.mit.coeuslite.utils.bean.ProcParameterBean;
import edu.mit.coeuslite.utils.bean.ProcedureBean;
import java.util.Hashtable;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.*;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 *
 * @author  bijosht
 */
public class ReadJSPPlaceHolder {
    private Hashtable htJSPData;
    private String fileName;
    private Document doc;

    /** Creates a new instance of ReadJSPPlaceHolder */    
    public ReadJSPPlaceHolder( String fileName) {
        this.fileName = fileName;
    }
    
        public Hashtable readXMLData(String basePage) {
        try {
            if(basePage != null && fileName != null){
                PlaceHolderBean placeHolderBean = null;
                DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
                Document doc = docBuilder.parse(getClass().getResourceAsStream(fileName));//"/edu/mitweb/coeus/irb/xml/data/ProtocolDetails.xml"

                // normalize text representation
                doc.getDocumentElement().normalize();
                NodeList typeList = doc.getElementsByTagName(basePage);
                int count = typeList.getLength();
                htJSPData = new Hashtable();
                for (int index = 0;  index<count;index++) {
                    placeHolderBean = new PlaceHolderBean();
                    Node firstCharNode = typeList.item(index);
                    Element firstElement = (Element)firstCharNode;
                    
                    NodeList placeHolderList = firstElement.getElementsByTagName("placeHolder");
                    NodeList includeJSPList = firstElement.getElementsByTagName("includeValue");
                    
                   
                    Element placeHolderElement = (Element)placeHolderList.item(0);
                    Element includeJSPElement = (Element)includeJSPList.item(0);

                    
                    NodeList placeHolderListData = placeHolderElement.getChildNodes();
                    NodeList includeJSPListData = includeJSPElement.getChildNodes();
                    
                    String placeHolder = (String)((Node)placeHolderListData.item(0)).getNodeValue().trim();
                    String includeFile = (String)((Node)includeJSPListData.item(0)).getNodeValue().trim();
                    placeHolderBean.setBasePage(basePage);
                    placeHolderBean.setIncludeFile(includeFile);
                    placeHolderBean.setPlaceHolder(placeHolder);
                    
                    htJSPData.put(placeHolder,placeHolderBean);
                }
                doc.normalize();
                }// End of protocol Type Loop
            //}//End of if clause
        }catch (SAXParseException err) {
            System.out.println(" " + err.getMessage());
            
        }catch (SAXException e) {
            Exception x = e.getException();
            ((x == null) ? e : x).printStackTrace();
            
        }catch (Throwable t) {
            t.printStackTrace();
        }
        return htJSPData;
    }
        
        /*
         * Reading the mapping.xml for the procedure details
         */
        
        private ProcedureBean readProcedureData(String procedureID) {
            ProcedureBean procedureBean = new ProcedureBean();
            try {
                if(procedureID != null && fileName != null){
                    Vector vecProcedureData = new Vector();
                    NodeList procedureList = doc.getElementsByTagName(procedureID);
                    Node procedureIdNode = procedureList.item(0);
                    Element elProcedureId = (Element)procedureIdNode;
                    NodeList procedureNodeList = elProcedureId.getElementsByTagName("procedure");

                    Element procedureNameElement = (Element)procedureNodeList.item(0);
                    NodeList procedureNameListData = procedureNameElement.getChildNodes();
                    String procedureName = (String)((Node)procedureNameListData.item(0)).getNodeValue().trim();
                    
                    
                    procedureBean.setProcedureId(procedureID);
                    procedureBean.setProcedureName(procedureName);
                        
                    Node procedureNode= procedureNodeList.item(0);
                    NodeList mapNodeList = elProcedureId.getElementsByTagName("map");
                    int mapCount = mapNodeList.getLength();
                    for (int index=0;index<mapCount;index++) {
                        ProcParameterBean procParameterBean = new ProcParameterBean();
                        Node mapNode = mapNodeList.item(index);
                        Element mapElement = (Element)mapNode;
                        
                        NodeList procparameterList = mapElement.getElementsByTagName("procparameter");
                        Element procparameterElement = (Element)procparameterList.item(0);
                        NodeList procparameterListData = procparameterElement.getChildNodes();
                        String procparameter = (String)((Node)procparameterListData.item(0)).getNodeValue().trim();
                        procParameterBean.setProcparameter(procparameter);
                        
                        NodeList propertyList = mapElement.getElementsByTagName("property");
                        Element propertyElement = (Element)propertyList.item(0);
                        NodeList propertyListData = propertyElement.getChildNodes();
                        String property = (String)((Node)propertyListData.item(0)).getNodeValue().trim();
                        procParameterBean.setProperty(property);
                        
                        NodeList typeList = mapElement.getElementsByTagName("type");
                        Element typeElement = (Element)typeList.item(0);
                        NodeList typeListData = typeElement.getChildNodes();
                        String type = (String)((Node)typeListData.item(0)).getNodeValue().trim();
                        procParameterBean.setType(type);
                        
                        NodeList procTypeList = mapElement.getElementsByTagName("proc_type");
			
                        if (procTypeList!=null  && procTypeList.getLength()>0) {
                            Element procTypeElement = (Element)procTypeList.item(0);
                            NodeList procTypeListData = procTypeElement.getChildNodes();
                            String procType = (String)((Node)procTypeListData.item(0)).getNodeValue().trim();
                            procParameterBean.setProcType(procType);
                        }
                        vecProcedureData.add(procParameterBean);
                    }
                    procedureBean.setProcparameterVector(vecProcedureData);

                }
            }catch (Throwable t) {
                t.printStackTrace();
            }
            return procedureBean;
        }
        public ProcedureBean readXMLProcedureData(String procedureID) {
            ProcedureBean procedureBean = null;
            try {
            if(procedureID != null && fileName != null){
                DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
                doc = docBuilder.parse(getClass().getResourceAsStream(fileName));//"/edu/mitweb/coeus/irb/xml/data/ProtocolDetails.xml"
                // normalize text representation
                doc.getDocumentElement().normalize();
                procedureBean=readProcedureData(procedureID);
            }
            }catch (Throwable t) {
                t.printStackTrace();
            }
            return procedureBean;
        }
        
        public Vector readXMLProcedureData(String procedureIDs[]) {
            Vector vecProcedureData = null;
            try {
                if(procedureIDs != null && fileName != null){
                    vecProcedureData = new Vector();
                    DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
                    DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
                    doc = docBuilder.parse(getClass().getResourceAsStream(fileName));//"/edu/mitweb/coeus/irb/xml/data/ProtocolDetails.xml"
                    // normalize text representation
                    doc.getDocumentElement().normalize();
                    
                    for (int index=0;index<procedureIDs.length;index++) {
                        String procedureID = procedureIDs[index];
                        ProcedureBean procedureBean=readProcedureData(procedureID);
                        vecProcedureData.add(procedureBean);
                    }
                }
            }catch (Throwable t) {
                t.printStackTrace();
            }
            return vecProcedureData;
        }

        
        public static void main (String args[]) {
            ReadJSPPlaceHolder readJSPPlaceHolder = new ReadJSPPlaceHolder("JSPBuilder.xml");
           //readJSPPlaceHolder.readXMLData("cwPerson", "JSPBuilder.xml");
           //readJSPPlaceHolder.readXMLProcedureData("UPDATE_PERSON_DETAILS","Mapping.xml");
        }
        
        /**
         * Getter for property fileName.
         * @return Value of property fileName.
         */
        public java.lang.String getFileName() {
            return fileName;
        }
        
        /**
         * Setter for property fileName.
         * @param fileName New value of property fileName.
         */
        public void setFileName(java.lang.String fileName) {
            this.fileName = fileName;
        }
        
}
