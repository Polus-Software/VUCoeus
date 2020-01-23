/*
 * PropXMLStreamGenerator.java
 *
 * Created on Mar 3 2004
 */

package edu.mit.coeus.xml.iacuc.generator;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.util.* ;
import java.io.* ;
import org.w3c.dom.Document;
import org.w3c.dom.DOMException;
import javax.xml.transform.dom.DOMSource; 
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.stream.StreamResult;

import edu.mit.coeus.utils.pdf.generator.XMLtoPDFConvertor ;
import edu.mit.coeus.utils.xml.bean.proposal.nih.*;
import edu.mit.coeus.utils.xml.bean.proposal.nih.nihGenerator.*;
import edu.mit.coeus.utils.xml.bean.proposal.rar.rarGenerator.RarObjFactoryGenerator;
import edu.mit.coeus.utils.xml.bean.proposal.common.commonGenerator.CommonObjFactoryGenerator;
import edu.mit.coeus.utils.*;
import edu.mit.coeus.utils.dbengine.* ;
import edu.mit.coeus.exception.* ;
import edu.mit.coeus.xml.generator.BaseStreamGenerator;
import java.math.BigInteger;
import javax.xml.bind.ValidationEvent;
import javax.xml.bind.ValidationEventHandler;
  
  
 
public class PropXMLStreamGenerator extends BaseStreamGenerator
{
    Marshaller marshaller ;
    JAXBContext jaxbContext ;
    ObjectFactory objFactory ;
     
    Document doc ; 
    
    /** Creates a new instance of PropXMLStreamGenerator */
    public PropXMLStreamGenerator() throws javax.xml.bind.JAXBException
    {
        jaxbContext = JAXBContext.newInstance( "edu.mit.coeus.utils.xml.bean.proposal.common" ); 
        System.out.println(" ** using package edu.mit.coeus.utils.xml.bean.proposal.common **") ;
        // creating the nih ObjectFactory
        objFactory = new ObjectFactory();
        System.out.println("after creating  objectfactory");
        
        
        try
        { 
               DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
               dbf.setValidating(false);
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
         marshaller.setEventHandler(new ValidationEventHandler(){
             public boolean handleEvent(ValidationEvent event){
                 UtilFactory.log("Warning message, will not break during XML processing",
                    event.getLinkedException(), "PropXMLStreamGenerator","PropXMLStreamGenerator");
                 return true;
             }
         });
    }

    private void marshalStream(Object streamObj) throws CoeusException, DBException, javax.xml.bind.JAXBException, javax.xml.parsers.ParserConfigurationException,
                                                                            javax.xml.transform.TransformerConfigurationException,javax.xml.transform.TransformerException,
                                                                            java.io.FileNotFoundException
    {
        // Now create the stream and output it
        System.out.println("in propXMLStreamGenerator before marshalling");
        
       
        marshaller.marshal( streamObj, doc );
    
        System.out.println("in propXMLStreamGenerator after marshalling");
       // below lines is just for testing prupose (sending the xml generated to tomcat console)
       // test start 
//        TransformerFactory tFactory =  TransformerFactory.newInstance();
//        Transformer transformer = tFactory.newTransformer();

  //     String debugXml = "C:\\jakarta-tomcat-4.0.4\\webapps\\CoeusApplet\\eleoutput.xml" ;
   //     FileOutputStream  xmlOut = new java.io.FileOutputStream(debugXml) ;
                
      // DOMSource source = new DOMSource(doc);
      //  StreamResult result = new StreamResult(xmlOut) ;
      //  transformer.transform(source, result) ;
        // test end
   }
    
   
     public Document rarPropXML (String proposalNumber, String sponsor)
           throws CoeusException, DBException, javax.xml.bind.JAXBException, javax.xml.parsers.ParserConfigurationException,
                  javax.xml.transform.TransformerConfigurationException,javax.xml.transform.TransformerException,
                   java.io.FileNotFoundException
    {
    
         
        ResearchAndRelProjectStream researchAndRelProjStream
                = new ResearchAndRelProjectStream(objFactory,
                                       new RarObjFactoryGenerator().getInstance() ,
                                       new CommonObjFactoryGenerator().getInstance());
        
         
        ResearchAndRelatedProjectType researchAndRelProj
             = researchAndRelProjStream.getResearchAndRelProj(proposalNumber,
                                                              sponsor);
     
                
       
        marshalStream(researchAndRelProj) ;        
        return doc ;         
    }
    
     public Document getStream(Hashtable params) throws CoeusException, DBException, 
                    javax.xml.bind.JAXBException, 
                    javax.xml.parsers.ParserConfigurationException,
                    javax.xml.transform.TransformerConfigurationException,
                    javax.xml.transform.TransformerException,
                    java.io.FileNotFoundException{
         String propNumber = (String)params.get("PROPOSAL_NUMBER");
         String sponsorCode = (String)params.get("SPONSOR_CODE");
         return this.rarPropXML(propNumber, sponsorCode);
     }
}
