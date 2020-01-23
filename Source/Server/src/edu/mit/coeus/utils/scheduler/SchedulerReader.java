/*
 * GGPollingReader.java
 *
 * Created on October 2, 2006, 10:19 AM
 */

package edu.mit.coeus.utils.scheduler;

import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.utils.UtilFactory;
import java.util.Hashtable;
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
public class SchedulerReader {
    
    /** Creates a new instance of GGPollingReader */
    public SchedulerReader() {
    }
    private static Hashtable scheduleNodes;
    private static final String fileName = "/edu/mit/coeus/utils/scheduler/Scheduler.xml";
    public static Node get(String classname) throws CoeusException{
        getScheduleNodes();
        return (Element)scheduleNodes.get(classname);
    }
    public static Hashtable getScheduleNodes() throws CoeusException{
        if(scheduleNodes==null){
            synchronized(SchedulerReader.class){
                loadBindings();
            }
        }
        return scheduleNodes;
    }
    private static void loadBindings() throws CoeusException{
        
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        Document document = null;
        try{
            DocumentBuilder builder = factory.newDocumentBuilder();
            document = builder.parse( new SchedulerReader().getClass().getResourceAsStream(fileName));
        }catch(Exception ex){
            UtilFactory.log(ex.getMessage(),ex,"ScheduleReader", "loadBindings");
            throw new CoeusException(ex.getMessage());
        }
        Element docElement = document.getDocumentElement();
        NodeList childNodes  = docElement.getChildNodes();
        scheduleNodes = new Hashtable();
        for(int index=0;index<childNodes.getLength();index++){
            if(!(childNodes.item(index) instanceof Element)) continue;
            Element childNode = (Element)childNodes.item(index);
            scheduleNodes.put(childNode.getNodeName(), childNode);
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
    
}
