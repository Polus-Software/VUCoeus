/*
 * MultiCampusNodeReader.java
 *
 * Created on January 16, 2007, 11:24 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package edu.mit.coeus.user.auth.multicampus;

import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.user.auth.CoeusAuthServiceFactory;
import edu.mit.coeus.utils.ComboBoxBean;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import javax.xml.transform.TransformerException;
import org.apache.xpath.XPathAPI;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author geot
 */
public class MultiCampusNodeReader {
    
    /** Creates a new instance of MultiCampusNodeReader */
    public MultiCampusNodeReader() {
    }
    public List getCampusList(){
        NodeList campusList = null;
        int campusListLen = 0;
        try{
            final String campXpath = "CAMPUS";
            Node mulitCampNode = CoeusAuthServiceFactory.getAuthNode("MULTI_CAMPUS_LDAP");
            campusList = XPathAPI.selectNodeList(mulitCampNode , campXpath);
            campusListLen = campusList==null?0:campusList.getLength();
            
            List campuses = null;
            for(int pIndex = 0;pIndex<campusListLen;pIndex++){
                Element campus = (Element)campusList.item(pIndex);
                String code = campus.getAttribute("code");
                String value = campus.getAttribute("value");
                if(code!=null && code.length()>0 && value!=null && value.length()>0){
                    if (campuses==null) campuses = new ArrayList();
                    ComboBoxBean combo = new ComboBoxBean();
                    combo.setCode(code);
                    combo.setDescription(value);
                    campuses.add(combo);
                }
            }
            return campuses;
        }catch(TransformerException ex){
            ex.printStackTrace();
            return null;
        }catch(CoeusException ex){
            return null;
        }
        
    }
    public Properties getNodeProps(String campusCode) throws CoeusException{
        NodeList authProps = null;
        int authPropLength = 0;
        try{
            final String campXpath = "CAMPUS[@code='"+campusCode+"']/PROPERTY";
            Node mulitCampNode = CoeusAuthServiceFactory.getAuthNode("MULTI_CAMPUS_LDAP");
            authProps = XPathAPI.selectNodeList(mulitCampNode , campXpath);
            authPropLength = authProps==null?0:authProps.getLength();
        }catch(TransformerException ex){
            ex.printStackTrace();
        }
        Properties authProperties = new Properties();
        for(int pIndex = 0;pIndex<authPropLength;pIndex++){
            Element authProp = (Element)authProps.item(pIndex);
            String name = authProp.getAttribute("name");
            String value = authProp.getAttribute("value");
            if(name!=null && name.length()>0 && value!=null && value.length()>0)
                authProperties.put(name, value);
        }
        return authProperties;
    }
}
