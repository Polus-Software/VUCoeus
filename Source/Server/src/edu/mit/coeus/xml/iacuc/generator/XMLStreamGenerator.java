/*
 * XMLStreamGenerator.java
 *
 * Created on September 18, 2003, 3:50 PM
 */

package edu.mit.coeus.xml.iacuc.generator;

import edu.mit.coeus.irb.bean.AdhocDetailsBean;
import edu.mit.coeus.iacuc.bean.ProtocolActionsBean;
import edu.mit.coeus.xml.generator.CoeusXMLException;
import edu.mit.coeus.xml.generator.CoeusXMLGenrator;
import edu.mit.coeus.xml.generator.ReportBaseStream;
import java.util.Hashtable;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import org.w3c.dom.Document;
import javax.xml.transform.dom.DOMSource;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.stream.StreamResult;
import edu.mit.coeus.utils.dbengine.* ;
import edu.mit.coeus.exception.* ;
import edu.mit.coeus.xml.iacuc.* ;

public class XMLStreamGenerator extends ReportBaseStream {
    ObjectFactory objFactory ;
    CommitteeType committee ;
    Document doc ;
    CoeusXMLGenrator xmlGen;
    public static final String XML_GEN_PACKAGE_NAME = "edu.mit.coeus.xml.iacuc";
    /** Creates a new instance of XMLStreamGenerator */
    public XMLStreamGenerator() throws javax.xml.bind.JAXBException {
        
        xmlGen = new CoeusXMLGenrator();
        objFactory = new ObjectFactory();
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setNamespaceAware(true);
            DocumentBuilder db = dbf.newDocumentBuilder();
            doc = db.newDocument();
        } catch(Exception ex) {
            ex.printStackTrace() ;
        }
     
    }
    
    public Object getObjectStream(Hashtable params) throws DBException, CoeusException{
        AdhocDetailsBean adhocDetailsBean = (AdhocDetailsBean)params.get("DATA");
        Object retObj = null;
        try{
            if (adhocDetailsBean.getModule() == 'C'){ //committee module adhoc report
                CommitteeStream committeeStream = new CommitteeStream(objFactory);
                CommitteeType committee = committeeStream.getCommitteeCompleteDetails(adhocDetailsBean.getCommitteeId());
                retObj = committee;
            }else if(adhocDetailsBean.getModule() == 'S'){ //schedule module adhoc report type 1
                ScheduleStream scheduleStream = new ScheduleStream(objFactory) ;
                ScheduleType schedule = scheduleStream.getSchedule(adhocDetailsBean.getScheduleId()) ;
                retObj = schedule;
            }else if (adhocDetailsBean.getModule() == 'U'){ //schedule module adhoc report type 2
                ProtocolActionsBean actionBean = new ProtocolActionsBean() ;
                actionBean.setProtocolNumber(adhocDetailsBean.getProtocolNumber()) ;
                actionBean.setSequenceNumber(adhocDetailsBean.getSequenceNumber()) ;
                actionBean.setScheduleId(adhocDetailsBean.getScheduleId()) ;
                actionBean.setSubmissionNumber(adhocDetailsBean.getSubmissionNumber()) ;
                
                CorrespondenceStream correspondenceStream = new CorrespondenceStream(objFactory) ;
                CorrespondenceType correspondence = correspondenceStream.getCorrespondence(actionBean) ;
                retObj = correspondence;
            }else if (adhocDetailsBean.getModule() == 'P'){ //Protocol module adhoc report
                ProtocolStream protocolStream = new ProtocolStream(objFactory) ;
                ProtocolType protocol = protocolStream.getProtocol(adhocDetailsBean.getProtocolNumber(), adhocDetailsBean.getSequenceNumber()) ;
                retObj = protocol;
            }
        }catch (JAXBException jAXBException) {
            throw new CoeusException(jAXBException.getMessage());
        }
        return retObj;
    }
    
    
    private void marshalStream(Object streamObj) throws CoeusException, DBException{
        doc = xmlGen.marshelObject(streamObj, XML_GEN_PACKAGE_NAME);
       
       //todo: for testing remove or comment after the testing begin
//        CoeusXMLGenrator coeusXmlGen = new CoeusXMLGenrator();
//        try{
//            String packageName = "class edu.mit.coe   us.xml.iacuc.impl.";
//            String fileName =  streamObj.getClass().toString();
//            fileName = fileName.substring(packageName.length()) +".xml";
//            coeusXmlGen.logXMLFile(doc,"D:\\reports\\iacuc",fileName);
//        }catch(Exception e){
//            e.printStackTrace();
//        }
       //todo: for testing remove or comment after the testing end
    }
    
    public Document correspondenceXMLStreamGenerator(ProtocolActionsBean actionBean) throws CoeusException, DBException, javax.xml.bind.JAXBException, javax.xml.parsers.ParserConfigurationException,
            javax.xml.transform.TransformerConfigurationException,javax.xml.transform.TransformerException {
        CorrespondenceStream correspondenceStream = new CorrespondenceStream(objFactory) ;
        CorrespondenceType correspondence = correspondenceStream.getCorrespondence(actionBean) ;
        marshalStream(correspondence) ;
        return doc ;
    }
    
   
    /*  This method will get the protocol details from the database and populates
     *  the data to edu.mit.coeus.utils.xml.bean objects and generate the xml stream.
     */
    public Document protocolXMLStreamGenerator(String protocolId, int sequenceNumber) throws CoeusException, DBException, javax.xml.bind.JAXBException, javax.xml.parsers.ParserConfigurationException,
            javax.xml.transform.TransformerConfigurationException,javax.xml.transform.TransformerException {
        ProtocolStream protocolStream = new ProtocolStream(objFactory) ;
        ProtocolType protocol = protocolStream.getProtocol(protocolId, sequenceNumber) ;
        marshalStream(protocol) ;
        return doc ;
    }
    
    public Document protocolXMLStreamGenerator(String protocolId, int sequenceNumber, int submissionNumber) throws CoeusException, DBException, javax.xml.bind.JAXBException, javax.xml.parsers.ParserConfigurationException,
            javax.xml.transform.TransformerConfigurationException,javax.xml.transform.TransformerException {
        ProtocolStream protocolStream = new ProtocolStream(objFactory) ;
        ProtocolType protocol = protocolStream.getProtocol(protocolId, sequenceNumber, submissionNumber) ;
        marshalStream(protocol) ;
        return doc ;
    }
    
    
    public Document scheduleXMLStreamGenerator(String scheduleId) throws CoeusException, DBException, javax.xml.bind.JAXBException, javax.xml.parsers.ParserConfigurationException,
            javax.xml.transform.TransformerConfigurationException,javax.xml.transform.TransformerException {
        ScheduleStream scheduleStream = new ScheduleStream(objFactory) ;
        ScheduleType schedule = scheduleStream.getSchedule(scheduleId) ;
        marshalStream(schedule) ;
        return doc ;
    }
    
    
    public Document committeeXMLStreamGenerator(String committeeId) throws CoeusException, DBException, javax.xml.bind.JAXBException, javax.xml.parsers.ParserConfigurationException,
            javax.xml.transform.TransformerConfigurationException,javax.xml.transform.TransformerException {
        CommitteeStream committeeStream = new CommitteeStream(objFactory) ;
        CommitteeType committee = committeeStream.getCommitteeCompleteDetails(committeeId) ;
        marshalStream(committee) ;
        return doc ;
    }
    
    //prps start - Mar 26 2004
    // renewal reminder stream
    public Document renewalReminderXMLStreamGenerator(ProtocolActionsBean actionBean) throws CoeusException, DBException, javax.xml.bind.JAXBException, javax.xml.parsers.ParserConfigurationException,
            javax.xml.transform.TransformerConfigurationException,javax.xml.transform.TransformerException {
        RenewalReminderStream renewalReminderStream = new RenewalReminderStream(objFactory) ;
        RenewalReminderType renewalReminder = renewalReminderStream.getReminder(actionBean) ;
        marshalStream(renewalReminder) ;
        return doc ;
    }
    //prps end - mar 26 2004
}
