/*
 * XMLStreamGenerator.java
 *
 * Created on September 18, 2003, 3:50 PM
 */

package edu.mit.coeus.utils.xml.generator;

import edu.mit.coeus.xml.generator.ReportBaseStream;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.util.* ;
import org.w3c.dom.Document;
import javax.xml.transform.dom.DOMSource; 
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.stream.StreamResult;
import edu.mit.coeus.utils.dbengine.* ;
import edu.mit.coeus.irb.bean.* ;
import edu.mit.coeus.exception.* ;
import edu.mit.coeus.utils.xml.bean.schedule.* ;

public class XMLStreamGenerator extends ReportBaseStream
{
    Marshaller marshaller ;
    JAXBContext jaxbContext ;
    ObjectFactory objFactory ;
    CommitteeType committee ;
    Document doc ; 
    
    /** Creates a new instance of XMLStreamGenerator */
    public XMLStreamGenerator() throws javax.xml.bind.JAXBException
    {
        jaxbContext = JAXBContext.newInstance( "edu.mit.coeus.utils.xml.bean.schedule" ); 
        System.out.println(" ** using package edu.mit.coeus.utils.xml.bean.schedule **") ;
        // creating the ObjectFactory
        objFactory = new ObjectFactory();
        try
        {
               DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
               dbf.setNamespaceAware(true);
               DocumentBuilder db = dbf.newDocumentBuilder();
               doc = db.newDocument();
         }
         catch(Exception ex)
         {
            ex.printStackTrace() ;
         }
         marshaller = jaxbContext.createMarshaller();
         marshaller.setProperty( Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE );
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
    
    
    private void marshalStream(Object streamObj) throws CoeusException, DBException, javax.xml.bind.JAXBException, javax.xml.parsers.ParserConfigurationException,
                                                                            javax.xml.transform.TransformerConfigurationException,javax.xml.transform.TransformerException
    {
        // Now create the stream and output it
        //marshaller.marshal( agenda, System.out ); 
        marshaller.marshal( streamObj, doc );
    
       // below lines is just for testing prupose (sending the xml generated to tomcat console)
       // test start 
        TransformerFactory tFactory =  TransformerFactory.newInstance();
        Transformer transformer = tFactory.newTransformer();

        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(System.out) ;
        transformer.transform(source, result) ;
        // test end
   }
    
    public Document correspondenceXMLStreamGenerator(ProtocolActionsBean actionBean) throws CoeusException, DBException, javax.xml.bind.JAXBException, javax.xml.parsers.ParserConfigurationException,
                                                                            javax.xml.transform.TransformerConfigurationException,javax.xml.transform.TransformerException
    {
      CorrespondenceStream correspondenceStream = new CorrespondenceStream(objFactory) ;
      CorrespondenceType correspondence = correspondenceStream.getCorrespondence(actionBean) ;
      marshalStream(correspondence) ;
      return doc ;        
    }
    //added for IACUC
     public Document correspondenceXMLStreamGenerator(edu.mit.coeus.iacuc.bean.ProtocolActionsBean actionBean) throws CoeusException, DBException, javax.xml.bind.JAXBException, javax.xml.parsers.ParserConfigurationException,
                                                                            javax.xml.transform.TransformerConfigurationException,javax.xml.transform.TransformerException
    {
      CorrespondenceStream correspondenceStream = new CorrespondenceStream(objFactory) ;
      CorrespondenceType correspondence = correspondenceStream.getCorrespondence(actionBean) ;
      marshalStream(correspondence) ;
      return doc ;        
    }     
    /*  This method will get the protocol details from the database and populates
     *  the data to edu.mit.coeus.utils.xml.bean objects and generate the xml stream.
     */
    public Document protocolXMLStreamGenerator(String protocolId, int sequenceNumber) throws CoeusException, DBException, javax.xml.bind.JAXBException, javax.xml.parsers.ParserConfigurationException,
                                                                            javax.xml.transform.TransformerConfigurationException,javax.xml.transform.TransformerException
    {
        ProtocolStream protocolStream = new ProtocolStream(objFactory) ;
        ProtocolType protocol = protocolStream.getProtocol(protocolId, sequenceNumber) ; 
        marshalStream(protocol) ;        
        return doc ;        
    }
     
    public Document protocolXMLStreamGenerator(String protocolId, int sequenceNumber, int submissionNumber) throws CoeusException, DBException, javax.xml.bind.JAXBException, javax.xml.parsers.ParserConfigurationException,
                                                                            javax.xml.transform.TransformerConfigurationException,javax.xml.transform.TransformerException
    {
        ProtocolStream protocolStream = new ProtocolStream(objFactory) ;
        ProtocolType protocol = protocolStream.getProtocol(protocolId, sequenceNumber, submissionNumber) ; 
        marshalStream(protocol) ;        
        return doc ;        
    }
    
    
    public Document scheduleXMLStreamGenerator(String scheduleId) throws CoeusException, DBException, javax.xml.bind.JAXBException, javax.xml.parsers.ParserConfigurationException,
                                                                            javax.xml.transform.TransformerConfigurationException,javax.xml.transform.TransformerException
    {
        ScheduleStream scheduleStream = new ScheduleStream(objFactory) ;
        ScheduleType schedule = scheduleStream.getSchedule(scheduleId) ;
        marshalStream(schedule) ;
        return doc ;
    }
   
    
    public Document committeeXMLStreamGenerator(String committeeId) throws CoeusException, DBException, javax.xml.bind.JAXBException, javax.xml.parsers.ParserConfigurationException,
                                                                            javax.xml.transform.TransformerConfigurationException,javax.xml.transform.TransformerException
    {
        CommitteeStream committeeStream = new CommitteeStream(objFactory) ;
        CommitteeType committee = committeeStream.getCommitteeCompleteDetails(committeeId) ;
        marshalStream(committee) ;
        return doc ;
    }
    
    //prps start - Mar 26 2004
    // renewal reminder stream
    public Document renewalReminderXMLStreamGenerator(ProtocolActionsBean actionBean) throws CoeusException, DBException, javax.xml.bind.JAXBException, javax.xml.parsers.ParserConfigurationException,
                                                                            javax.xml.transform.TransformerConfigurationException,javax.xml.transform.TransformerException
    {
        RenewalReminderStream renewalReminderStream = new RenewalReminderStream(objFactory) ;
        RenewalReminderType renewalReminder = renewalReminderStream.getReminder(actionBean) ; 
        marshalStream(renewalReminder) ;        
        return doc ;        
    }
    public Document renewalReminderXMLStreamGenerator(edu.mit.coeus.iacuc.bean.ProtocolActionsBean actionBean) throws CoeusException, DBException, javax.xml.bind.JAXBException, javax.xml.parsers.ParserConfigurationException,
                                                                            javax.xml.transform.TransformerConfigurationException,javax.xml.transform.TransformerException
    {
        RenewalReminderStream renewalReminderStream = new RenewalReminderStream(objFactory) ;
        RenewalReminderType renewalReminder = renewalReminderStream.getReminder(actionBean) ; 
        marshalStream(renewalReminder) ;        
        return doc ;        
    }
    //prps end - mar 26 2004
    
}
