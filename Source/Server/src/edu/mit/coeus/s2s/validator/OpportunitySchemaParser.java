/*
 * SchemaParser.java
 *
 * Created on December 29, 2004, 10:03 AM
 */

package edu.mit.coeus.s2s.validator;

import edu.mit.coeus.bean.CoeusMessageResourcesBean;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.s2s.bean.FormInfoBean;
import edu.mit.coeus.s2s.bean.UserAttachedS2STxnBean;
import edu.mit.coeus.s2s.util.Converter;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.utils.dbengine.DBException;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Vector;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 *
 * @author  geot
 */
public class OpportunitySchemaParser {
    
    private ArrayList selectedFormList;
    private static final String XSD_NS = "http://www.w3.org/2001/XMLSchema";
    private static final String XMLNS = "http://www.w3.org/2001/XMLSchema";
    
    /** Creates a new instance of SchemaParser */
    public OpportunitySchemaParser() {
    }
    public ArrayList getFormsList(String proposalNumber,String schemaUrl) throws 
                            ParserConfigurationException,SAXException,IOException,CoeusException{
        HashMap formsMap = getFormSchemaList(schemaUrl);
        ArrayList formsList = null;
        Vector tmpSortList = new Vector();
        Iterator it = formsMap.keySet().iterator();
        while(it.hasNext()){
            if(formsList==null) formsList = new ArrayList();
            String nsKey = it.next().toString();
            FormInfoBean formInfo = (FormInfoBean)formsMap.get(nsKey);
            if(isFormAvailable(proposalNumber,formInfo,nsKey)){
            	formInfo.setAvailable(true);
//            if(formInfo.isAvailable()) {
	            BindingInfoBean bindInfo = (BindingInfoBean)BindingFileReader.get(nsKey);
            	if(bindInfo!=null){
	               formInfo.setFormName((
	                   (BindingInfoBean)BindingFileReader.get(nsKey)).getFormName());
	               formInfo.setSortIndex(bindInfo.getSortIndex());
            	}
               if(formInfo.isMandatory()) formInfo.setInclude(true);
               tmpSortList.add(formInfo);
            }else{
                formsList.add(formInfo);
            }
            
        }
        tmpSortList = Converter.sortForms(tmpSortList);
        if(!tmpSortList.isEmpty()) formsList.addAll(0,tmpSortList);
        return formsList;
    }
    private boolean isFormAvailable(String proposalNumber, FormInfoBean formInfo,String nsKey) throws CoeusException{
    	boolean available = BindingFileReader.get(nsKey)!=null;
    	if(!available){
    		try {
				available = new UserAttachedS2STxnBean().isFormAvailable(proposalNumber,nsKey);
				if(available){
					formInfo.setAttachedForm(true);
				}
			} catch (DBException e) {
				e.printStackTrace();
				UtilFactory.log("Error happend while checking user attached form avaliablity", e, "OpportunitySchemaParser", "isFormAvailable");
				throw new CoeusException(e);
			}
    	}
		return available;
	}
	public HashMap getFormSchemaList(String schemaUrl) 
                    throws ParserConfigurationException,SAXException,IOException{
//        System.out.println("schema=>"+schemaUrl);
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        Document document = null;
        DocumentBuilder builder = factory.newDocumentBuilder();
        document = builder.parse( schemaUrl );
        Element schemaElement = document.getDocumentElement();
//        NodeList importList = document.getElementsByTagName("xsd:import");
//        Node allForms = document.getElementsByTagName("xsd:all").item(0);
//        NodeList formsList = ((Element)allForms).getElementsByTagName("xsd:element");
        NodeList importList = document.getElementsByTagNameNS(XSD_NS,"import");
        Node allForms = document.getElementsByTagNameNS(XSD_NS,"all").item(0);
        NodeList formsList = ((Element)allForms).getElementsByTagNameNS(XSD_NS,"element");
        String[] formNames = new String[formsList.getLength()];
        HashMap schemaSet = new LinkedHashMap();
        for(int formIndex=0;formIndex<formsList.getLength();formIndex++){
            Node form = formsList.item(formIndex);
            String fullFormName = ((Element)form).getAttribute("ref");
            String formName = fullFormName.substring(0, fullFormName.indexOf(':'));
            String minOccurs = ((Element)form).getAttribute("minOccurs");
            String nameSpace = schemaElement.getAttribute("xmlns:"+formName);
            formNames[formIndex] = nameSpace;
            for(int impIndex=0;impIndex<importList.getLength();impIndex++){
                Node importNode = importList.item(impIndex);
                if(((Element)importNode).getAttribute("namespace").equalsIgnoreCase(nameSpace)){
                    String schemaLoc = ((Element)importNode).getAttribute("schemaLocation");
                    schemaSet.put(nameSpace, new FormInfoBean(nameSpace,minOccurs,schemaLoc,formName));
                }
            }
        }
        return schemaSet;
    }
    
    public void checkFormsAvailable(String proposalNumber,String schemaUrl) throws 
                            ParserConfigurationException,SAXException,IOException,CoeusException{
         ArrayList forms = getFormsList(proposalNumber,schemaUrl);
     	String errorMessage = new CoeusMessageResourcesBean().parseMessageKey("exceptionCode.90013");
     	ArrayList errorForms = new ArrayList();
         for(int i=0;i<forms.size();i++){
            FormInfoBean formInfo = (FormInfoBean)forms.get(i);
            if(formInfo.isMandatory()&& !isFormAvailable(proposalNumber,formInfo, formInfo.getNs())){ 
            	errorForms.add(formInfo.getFormName());
//                    throw new CoeusException("exceptionCode.90013");
            }
         }
         if(!errorForms.isEmpty()){
        	 throw new CoeusException(errorMessage+errorForms.toString());
         }
         
    }
    public static void main(String args[]) throws Exception{
        
        OpportunitySchemaParser p = new OpportunitySchemaParser();
        HashMap pp = p.getFormSchemaList("file:///C:/Coeus/S2S/Schemas/oppMIT-RR-TEST1.xsd");
        Iterator it = pp.keySet().iterator();
        for(int i=0;it.hasNext();i++){
            String str = (String)it.next();
            System.out.println(str+":"+pp.get(str));
        }
    }
    
    /**
     * Getter for property selectedFormList.
     * @return Value of property selectedFormList.
     */
    public java.util.ArrayList getSelectedFormList() {
        return selectedFormList;
    }
    
    /**
     * Setter for property selectedFormList.
     * @param selectedFormList New value of property selectedFormList.
     */
    public void setSelectedFormList(java.util.ArrayList selectedFormList) {
        this.selectedFormList = selectedFormList;
    }
    
}
