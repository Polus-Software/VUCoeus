/*
 * ReadMassChangeDataForm.java
 *
 * Created on January 18, 2005, 3:26 PM
 */

package edu.mit.coeus.centraladmin.bean;

import edu.mit.coeus.centraladmin.bean.ModuleBean;
import edu.mit.coeus.centraladmin.bean.PersonTypeBean;
import edu.mit.coeus.utils.CoeusVector;
import java.io.File;
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
public class ReadModulePersonType {
    private CoeusVector personTypeData; 
    private CoeusVector moduleData;
    private PersonTypeBean personTypeBean ;
    private ModuleBean moduleBean;
    
    /** Creates a new instance of ReadMassChangeDataForm */
    public ReadModulePersonType() {
        readXMLData();
    }
    
    private void readXMLData() {
        try {
            PersonTypeBean personTypeBean  = null;
            ModuleBean moduleBean = null;
            personTypeData = new CoeusVector();
            moduleData = new CoeusVector();
            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(getClass().getResourceAsStream("/edu/mit/coeus/centraladmin/xml/DataMassChange.xml"));
            
            // normalize text representation
            doc.getDocumentElement().normalize();
            System.out.println("Root element of the doc is " + doc.getDocumentElement().getNodeName());
            NodeList moduleList = doc.getElementsByTagName("Module");
            int totalChars = moduleList.getLength();
            System.out.println("Total No of Modules : " + totalChars);
            personTypeData = new CoeusVector();
            for(int moduleIndex=0; moduleIndex<moduleList.getLength() ; moduleIndex++){
                moduleBean = new ModuleBean();
                Node firstCharNode = moduleList.item(moduleIndex);
                Element firstElement = (Element)firstCharNode;
                
                NodeList moduleCodeList = firstElement.getElementsByTagName("Module_Id");
                NodeList moduleNameList = firstElement.getElementsByTagName("Module_Name");
                
                Element moduleElement = (Element)moduleCodeList.item(0);
                Element moduleNameElement = (Element)moduleNameList.item(0);
                NodeList moduleIdListData = moduleElement.getChildNodes();
                NodeList moduleNameListData = moduleNameElement.getChildNodes();
                
                String moduleCode=(String)((Node)moduleIdListData.item(0)).getNodeValue().trim();
                String moduleName=(String)((Node)moduleNameListData.item(0)).getNodeValue().trim();
                moduleBean.setModuleCode(moduleCode);
                moduleBean.setModuleName(moduleName);
                moduleData.add(moduleBean);
                setModuleData(moduleData);
                
                System.out.println("Module Id : "+moduleCode);
                System.out.println("Module Name : "+moduleName);
                
                
                NodeList typeList = firstElement.getElementsByTagName("Person_Type");
                NodeList personTypeIdList = firstElement.getElementsByTagName("person_Type_Id");
                NodeList personTypeNameList = firstElement.getElementsByTagName("person_Type_Name");
                NodeList moduleIdList = firstElement.getElementsByTagName("Module_id");
                NodeList replaceTypeList = firstElement.getElementsByTagName("ReplaceType");
                for(int index=0; index <typeList.getLength(); index++){
                    personTypeBean = new PersonTypeBean();
                    Element personTypeId = (Element)personTypeIdList.item(index);
                    Element personTypeName = (Element)personTypeNameList.item(index);
                    Element moduleIdElement = (Element)moduleIdList.item(index);
                    Element replaceTypeElement = (Element)replaceTypeList.item(index);
                    
                    NodeList textIDList = personTypeId.getChildNodes();
                    NodeList typeNameList = personTypeName.getChildNodes();
                    NodeList moduleListData = moduleIdElement.getChildNodes();
                    NodeList replaceTypeData = replaceTypeElement.getChildNodes();
                    
                    String typeId=(String)((Node)textIDList.item(0)).getNodeValue().trim();
                    personTypeBean.setTypeId(typeId);
                    
                    //System.out.println("Id is : " + typeId);
                    String typeDescription =(String)((Node)typeNameList.item(0)).getNodeValue().trim();
                    //System.out.println("Value is  : " + typeDescription);
                    personTypeBean.setTypeDescription(typeDescription);
                    String moduleId = (String)((Node)moduleListData.item(0)).getNodeValue().trim();
                    personTypeBean.setModuleId(moduleId);
                    personTypeBean.setReplaceType((String)((Node)replaceTypeData.item(0)).getNodeValue().trim());
                    personTypeData.add(personTypeBean);
                    setPersonTypeData(personTypeData);
                }// end of Person Type Loop
                doc.normalize();
            }// End of Module Loop
            extractData(personTypeData);
        }catch (SAXParseException err) {
            System.out.println("** Parsing error" + ", line " + err.getLineNumber() + ", uri " + err.getSystemId());
            System.out.println(" " + err.getMessage());
            
        }catch (SAXException e) {
            Exception x = e.getException();
            ((x == null) ? e : x).printStackTrace();
            
        }catch (Throwable t) {
            t.printStackTrace();
        }
    }
    
    private  void extractData(CoeusVector data){
        if(data!= null && data.size() > 0){
            for(int index= 0; index < data.size(); index++){
                PersonTypeBean bean = (PersonTypeBean)data.get(index);
                System.out.println("Type id is : "+bean.getTypeId());
                System.out.println("Type Description is : "+bean.getTypeDescription());
            }
        }
    }
    
    /** Getter for property personTypeData.
     * @return Value of property personTypeData.
     *
     */
    public edu.mit.coeus.utils.CoeusVector getPersonTypeData() {
        return personTypeData;
    }    
    
    /** Setter for property personTypeData.
     * @param personTypeData New value of property personTypeData.
     *
     */
    public void setPersonTypeData(edu.mit.coeus.utils.CoeusVector personTypeData) {
        this.personTypeData = personTypeData;
    }
    
    /** Getter for property moduleData.
     * @return Value of property moduleData.
     *
     */
    public edu.mit.coeus.utils.CoeusVector getModuleData() {
        return moduleData;
    }
    
    /** Setter for property moduleData.
     * @param moduleData New value of property moduleData.
     *
     */
    public void setModuleData(edu.mit.coeus.utils.CoeusVector moduleData) {
        this.moduleData = moduleData;
    }
    
}
