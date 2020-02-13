/*
 * ReportConfigEngine.java
 *
 * Created on December 19, 2005, 11:58 AM
 */

package edu.mit.coeus.xml.conf;

import edu.mit.coeus.bean.CoeusReportGroupBean;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.utils.UtilFactory;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author  geot
 */
public class ReportConfigEngine {
    
    /** Creates a new instance of ReportConfigEngine */
    private ReportConfigEngine() {
    }
    private static LinkedHashMap bindings;
    private static final String fileName = "/edu/mit/coeus/xml/conf/CoeusReportBinding.xml";
    /** Creates a new instance of BindingFileReader */
    public static CoeusReportGroupBean get(String id) throws CoeusException{
        getBindings();
        return (CoeusReportGroupBean)bindings.get(id);
    }
    public static LinkedHashMap getBindings() throws CoeusException{
        if(bindings==null){
            synchronized(ReportConfigEngine.class){
                loadBindings();
            }
        }
//        System.out.println("Report Bindings=>"+bindings.values().toString());
        return bindings;
    }
    private static void loadBindings() throws CoeusException{
        
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        Document document = null;
        try{
            DocumentBuilder builder = factory.newDocumentBuilder();
            document = builder.parse( new ReportConfigEngine().getClass().getResourceAsStream(fileName));
        }catch(Exception ex){
            UtilFactory.log(ex.getMessage(),ex,"ReportConfigEngine", "loadBindings");
            throw new CoeusException(ex.getMessage());
        }
        NodeList formList = document.getElementsByTagName("ReportGroup");
        bindings = new LinkedHashMap();
        for(int index=0;index<formList.getLength();index++){
            Element formNode = (Element)formList.item(index);
            String grpName = formNode.getAttribute("name");
            String display = formNode.getAttribute("display");
            String formName = getValue((Node)formNode);
            CoeusReportGroupBean repGrpBean = new CoeusReportGroupBean(grpName,display);
            repGrpBean.setJaxbPkgName(formNode.getAttribute("jaxbpkgname"));
            repGrpBean.setStreamGenerator(formNode.getAttribute("streamgenerator"));
            NodeList reportList = formNode.getElementsByTagName("Report");
            int length = reportList.getLength();
            for(int i=0;i<reportList.getLength();i++){
                Element repNode = (Element)reportList.item(i);
                String id = repNode.getAttribute("id");
                String streamGen = repNode.getAttribute("streamgenerator");
                String template = repNode.getAttribute("template");
                String jaxbPkgName = repNode.getAttribute("jaxbpkgname");
                String multiple = repNode.getAttribute("multiple");
                String footer = repNode.getAttribute("footer");
                String dispVal = getValue(repNode);
                repGrpBean.addReport(grpName+"/"+id, dispVal,streamGen,template, jaxbPkgName,multiple,footer);
            }
            bindings.put(repGrpBean.getGroupName(),repGrpBean);
        }
    }
    public static CoeusReportGroupBean.Report getReport(String id) throws CoeusException{
        CoeusReportGroupBean.Report rep=null;
        Iterator itKeys = getBindings().keySet().iterator();
        while(itKeys.hasNext()){
            rep = ((CoeusReportGroupBean)bindings.get(itKeys.next().toString())).getReport(id);
            if(rep!=null)
                break;
        }
        return rep;
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
    
}
