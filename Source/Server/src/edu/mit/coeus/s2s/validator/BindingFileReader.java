/*
 * BindingFileReader.java
 *
 * Created on December 29, 2004, 2:31 PM
 */

package edu.mit.coeus.s2s.validator;

import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.utils.UtilFactory;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Hashtable;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXParseException;

/**
 *
 * @author  geot
 */
public class BindingFileReader {
    private static Hashtable bindings;
    private static final String fileName = "/edu/mit/coeus/s2s/validator/S2SCoeusFormsBinding.xml";
    /** Creates a new instance of BindingFileReader */
    private BindingFileReader() {}
    public static BindingInfoBean get(String ns) throws CoeusException{
        getBindings();
        return (BindingInfoBean)bindings.get(ns);
    }
    public static Hashtable getBindings() throws CoeusException{
        if(bindings==null){
            synchronized(BindingFileReader.class){
                loadBindings();
            }
        }
        return bindings;
    }
    private static void loadBindings() throws CoeusException{
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        Document document = null;
        try{
        DocumentBuilder builder = factory.newDocumentBuilder();
        document = builder.parse( new BindingFileReader().getClass().getResourceAsStream(fileName));
        }catch(Exception ex){
            UtilFactory.log(ex.getMessage(),ex,"BindingFileReader", "loadBindings");
            throw new CoeusException(ex.getMessage());
        }
        NodeList formList = document.getElementsByTagName("Form");
        bindings = new Hashtable();
        for(int index=0;index<formList.getLength();index++){
            Element formNode = (Element)formList.item(index);
            String formName = getValue((Node)formNode);
//            Element methNode = (Element)formNode.getElementsByTagName("Method").item(0);
            BindingInfoBean bindBean = new BindingInfoBean();
            bindBean.setClassName(formNode.getAttribute("class"));
            bindBean.setNameSpace(formNode.getAttribute("namespace"));
            bindBean.setJaxbPkgName(formNode.getAttribute("jaxbpkgname"));
            bindBean.setTemplateName(formNode.getAttribute("template"));
            bindBean.setCgdNameSpace(formNode.getAttribute("cgdnamespace"));
            bindBean.setFormName(formName);
            
            String si = formNode.getAttribute("sortindex");
            if(si!=null && !si.trim().equals("")){
                bindBean.setSortIndex(si);
            }
            String authzCheck = formNode.getAttribute("authzcheck");
            if(authzCheck!=null && !authzCheck.trim().equals("")){
                try{
                    bindBean.setAuthzCheck(Boolean.valueOf(authzCheck).booleanValue());
                }catch(Exception ex){
                    bindBean.setAuthzCheck(false);
                }
            }
//            bindBean.setMethName(methNode.getAttribute("name"));
//            bindBean.setArgType(methNode.getAttribute("argtype"));
            bindings.put(bindBean.getNameSpace(),bindBean);
        }
    }
    /**
     *  Get the value from a node element
     */
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
//    public static synchronized HashMap getBindingBeans(){
//        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
//        Document document = null;
//        try {
//            DocumentBuilder builder = factory.newDocumentBuilder();
//            document = builder.parse( bfr.getClass().getResourceAsStream(fileName));
//        } catch (Exception spe) {
//            spe.printStackTrace();
//        }
//        NodeList formList = document.getElementsByTagName("Form");
//        HashMap beanList = new HashMap();
//        for(int index=0;index<formList.getLength();index++){
//            Element formNode = (Element)formList.item(index);
//            Element methNode = (Element)formNode.getElementsByTagName("Method").item(0);
//            BindingInfoBean bindBean = new BindingInfoBean();
//            bindBean.setClassName(formNode.getAttribute("class"));
//            bindBean.setNameSpace(formNode.getAttribute("namespace"));
//            bindBean.setMethName(methNode.getAttribute("name"));
//            bindBean.setArgType(methNode.getAttribute("argtype"));
//            beanList.put(bindBean.getNameSpace(),bindBean);
//        }
//        return beanList;
//    }
//    
//    private InputStream readFile(){
//        return getClass().getResourceAsStream("/edu/mit/coeus/s2s/validator/S2SCoeusFormsBinding.xml");
//    }
    
//    public static void main(String args[]){
//        BindingFileReader r = new BindingFileReader();
////        for(int i=0;i<r.getBindingBeans().size();i++)
//        System.out.println(r.getBindingBeans().toString());
//    }
}
