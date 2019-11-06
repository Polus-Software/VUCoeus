/*
 * CoeusAuthServiceFactory.java
 *
 * Created on August 28, 2006, 10:29 AM
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.user.auth;

import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.user.auth.bean.AuthXMLNodeBean;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.utils.XPathExecutor;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Properties;
//import javax.servlet.jsp.el.ELException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.NamedNodeMap;
/**
 *
 * @author  Geo Thomas
 */
public class CoeusAuthServiceFactory {
    private static Hashtable authBeanMap;
    private static Hashtable authNodeMap;
    private static final String fileName = "/Authentication.xml";
    /** Creates a new instance of CoeusAuthServiceFactory */
    private CoeusAuthServiceFactory() {
    }
    private static void init() throws CoeusException{
        if(authBeanMap==null){
            synchronized(CoeusAuthServiceFactory.class){
                loadAuthXML();
            }
        }
    }
    private static void loadAuthXML() throws CoeusException{
        
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        Document document = null;
        try{
            DocumentBuilder builder = factory.newDocumentBuilder();
            document = builder.parse( new CoeusAuthServiceFactory().getClass().getResourceAsStream(fileName));
        }catch(Exception ex){
            UtilFactory.log(ex.getMessage(),ex,"CoeusAuthServiceFactory", "loadBindings");
            throw new CoeusException(ex.getMessage());
        }
        Node authRootNode = (Node)document.getDocumentElement();
        NodeList authList = authRootNode.getChildNodes();
//        NodeList authList = document.getElementsByTagName("AUTHENTICATION");
        
        int lstSize = authList==null?0:authList.getLength();
        for(int index=0;index<lstSize;index++){
            Node authNode = (Node)authList.item(index);
            if(authNode instanceof Element){
                AuthXMLNodeBean nBean = new AuthXMLNodeBean();
                Element authElement = (Element)authNode;
                nBean.setAuthMode(authElement.getNodeName());
                nBean.setClientClass(authElement.getAttribute("clientsideauthclass"));
                nBean.setServerClass(authElement.getAttribute("serversideauthclass"));
                nBean.setLoginScreen(authElement.getAttribute("loginscreen"));
                NodeList authProps = authElement.getElementsByTagName("PROPERTY");
                int authPropLength = authProps==null?0:authProps.getLength();
                Properties authProperties = new Properties();
                for(int pIndex = 0;pIndex<authPropLength;pIndex++){
                    Element authProp = (Element)authProps.item(pIndex);
                    String name = authProp.getAttribute("name");
                    String value = authProp.getAttribute("value");
                    if(name!=null && name.length()>0 && value!=null && value.length()>0)
                        authProperties.put(name, value);
                }
                nBean.setAuthProps(authProperties);
                if(authBeanMap==null) authBeanMap = new Hashtable();
                authBeanMap.put(nBean.getAuthMode(),nBean);
                if(authNodeMap==null) authNodeMap = new Hashtable();
                authNodeMap.put(nBean.getAuthMode(),authNode);
//                if(authPropLength==0){
                    NodeList otherNodeList = authElement.getChildNodes();
                    int othLen = otherNodeList.getLength();
                    List otherNodes = null;
                    
                    for(int oIndex = 0;oIndex<othLen;oIndex++){
                        if(otherNodeList==null || otherNodeList.item(oIndex).getNodeName().equalsIgnoreCase("PROPERTY")) continue;
                        if(otherNodeList==null || otherNodeList.item(oIndex).getNodeType()!=Node.ELEMENT_NODE) continue;
                        
                        if(otherNodes==null) otherNodes = new ArrayList();
                        Element authProp = (Element)otherNodeList.item(oIndex);
                        String otherNodeName = authProp.getNodeName();
                        NamedNodeMap nnMap = authProp.getAttributes();
                        int len = nnMap.getLength();
                        Map attrMap = null;
                        for(int k = 0;k<len;k++){
                            Node attr = nnMap.item(k);
                            if(attr.getNodeType()==Element.ATTRIBUTE_NODE){
                                if(attrMap==null) attrMap = new HashMap();
                                attrMap.put(attr.getNodeName(),attr.getNodeValue());
                            }
                        }
                        if(attrMap!=null) otherNodes.add(attrMap);
                    }
                    if(otherNodes!=null) nBean.setOtherNodes(otherNodes);
//                }
            }
        }
    }
    public static AuthXMLNodeBean getCoeusAuthDetails(String loginMode) throws CoeusException{
        init();
        return (AuthXMLNodeBean)authBeanMap.get(loginMode);
    }
    public static CoeusAuthService getCoeusAuthService(String loginMode) throws CoeusException{
        init();
        AuthXMLNodeBean nBean = (AuthXMLNodeBean)authBeanMap.get(loginMode);
        try{
            Class authClass = Class.forName(nBean.getServerClass());
            CoeusAuthService authServObj = (CoeusAuthService)authClass.newInstance();
            Class[] initParamTypes = {Properties.class};
            Method initMeth = authClass.getMethod("init", initParamTypes);
            Properties[] propArr = {nBean.getAuthProps()};
            initMeth.invoke(authServObj, propArr);
            return authServObj;
        }catch(Throwable t){
            UtilFactory.log(t.getMessage(), t, "CoeusAuthServiceFactory", "getCoeusAuthService");
            throw new CoeusException(t.getMessage());
        }
    }
    public static Node getAuthNode(String loginMode) throws CoeusException{
        init();
        return (Node)authNodeMap.get(loginMode);
    }
}
