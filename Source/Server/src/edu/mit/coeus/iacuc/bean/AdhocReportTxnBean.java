/*
 * AdhocReportTxnBean.java
 *
 * Created on December 19, 2003, 3:16 PM
 */

package edu.mit.coeus.iacuc.bean;

import edu.mit.coeus.exception.*;
import edu.mit.coeus.iacuc.bean.ProtoCorrespTypeTxnBean;
import edu.mit.coeus.iacuc.bean.*;
import edu.mit.coeus.utils.*;
import edu.mit.coeus.utils.dbengine.*;
import edu.mit.coeus.utils.pdf.generator.*;
import edu.mit.coeus.xml.iacuc.generator.XMLStreamGenerator;
import java.util.*;
import org.w3c.dom.*;
import edu.mit.coeus.irb.bean.AdhocDetailsBean;

public class AdhocReportTxnBean 
{

    // instance of a dbEngine.
    private DBEngineImpl dbEngine;
    // holds the user id who has logged in.
    private String userId;
    
    private static int CORRESPONDENCE_ACTION_TYPE_CODE = 110 ;
    
    
    /** Creates a new instance of AdhocReportTxnBean */
    public AdhocReportTxnBean()
    {
        dbEngine = new DBEngineImpl();
    }

     public AdhocReportTxnBean(String userId) {
        this.userId = userId;
        dbEngine = new DBEngineImpl();
    }
     
     
    // this method will return a vector containing a list of all the adhoc
    // reports for a particular module. Module - committee or schedule or protocol 
    public Vector getAdhocReportsList(String module)throws DBException, CoeusException {
        Vector result = new Vector(3,2);
        Vector vecAdhoc = new Vector(3,2);
        HashMap hashAdhoc = new HashMap();
        Vector param= new Vector();
        
//        //prps test start
//        AdhocDetailsBean adhocDetailsBean1 = new AdhocDetailsBean() ;
//        AdhocDetailsBean adhocDetailsBean2 = new AdhocDetailsBean() ;
//                
//       if (module.equalsIgnoreCase("S"))
//       {    
//                adhocDetailsBean1.setFormId("11") ;
//                adhocDetailsBean1.setDescription("Schedule Optional Report #1") ;
//                
//                adhocDetailsBean2.setFormId("12") ;
//                adhocDetailsBean2.setDescription("Schedule Optional Report #2") ;
//       }
//       
//       if (module.equalsIgnoreCase("P"))
//       {    
//                adhocDetailsBean1.setFormId("13") ;
//                adhocDetailsBean1.setDescription("Protocol Optional Report #1") ;
//                
//                adhocDetailsBean2.setFormId("14") ;
//                adhocDetailsBean2.setDescription("Protocol Optional Report #2") ;
//       } 
//        
//       vecAdhoc.add(adhocDetailsBean1) ;
//       vecAdhoc.add(adhocDetailsBean2) ; 
//                
//                
//        //prps test end
//        // code below is valid code (do not delete)
                
        System.out.println ("** call get_adhoc_report_for_module **") ;
        param.addElement(new Parameter("AW_MODULE",
        DBEngineConstants.TYPE_STRING, module )) ;
        
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
            "call GET_AC_MODULE_ADHOC_REPORTS( <<AW_MODULE>>, <<OUT RESULTSET rset>> )",
            "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        if (!result.isEmpty()) {
            for(int i=0 ; i<result.size() ; i++)
            {
                hashAdhoc = (HashMap)result.elementAt(i);
                AdhocDetailsBean adhocDetailsBean = new AdhocDetailsBean() ;
                adhocDetailsBean.setFormId(hashAdhoc.get("FORM_ID").toString()) ;
                adhocDetailsBean.setDescription(hashAdhoc.get("DESCRIPTION").toString()) ;
                vecAdhoc.add(adhocDetailsBean) ;
            }
        }
        return vecAdhoc ;
        
    }
   
    /* Method which returns the template
    */
    public byte[] getAdhocReportTemplate(AdhocDetailsBean adhocDetailsBean) throws javax.xml.bind.JAXBException, 
                                                                                                   edu.mit.coeus.exception.CoeusException, DBException
    {
        //String committeeId = "DEFAULT" ;
        //Added for the case COEUSDEV-220-Generate Correspondence
        String committeeId = adhocDetailsBean.getCommitteeId();
        //Added for the case COEUSDEV-220-Generate Correspondence-end
        XMLPDFTxnBean pdfTxnBean = new XMLPDFTxnBean();
        //Added for case id COEUSQA-1724 iacuc protocol stream generation  
        byte[] templateFileFromDb = pdfTxnBean.getIACUCCorrespondenceTemplate(committeeId, Integer.parseInt(adhocDetailsBean.getFormId())) ;
        return templateFileFromDb;
    }
    
    public String generateAdhocReportWithTags(AdhocDetailsBean adhocDetailsBean, String reportPath,byte[] templatewithTags) 
    throws  javax.xml.bind.JAXBException, edu.mit.coeus.exception.CoeusException, DBException,
            javax.xml.parsers.ParserConfigurationException, javax.xml.transform.TransformerConfigurationException,
            javax.xml.transform.TransformerException, java.io.IOException, org.apache.fop.apps.FOPException
    {
        XMLStreamGenerator xmlStreamGenerator = new XMLStreamGenerator();
        XMLPDFTxnBean pdfTxnBean = new XMLPDFTxnBean();
        XMLtoPDFConvertor conv = new XMLtoPDFConvertor() ;
        String committeeId = "DEFAULT" ;
        if (adhocDetailsBean.getModule() == 'C') { //committee module adhoc report
            Document xmlDoc = xmlStreamGenerator.committeeXMLStreamGenerator(adhocDetailsBean.getCommitteeId()) ; //committee id
            UtilFactory.log("Xml doc generating complete!") ;
            UtilFactory.log("** Module " + adhocDetailsBean.getModule() + " Committee " + committeeId) ;
            if (templatewithTags == null) {
                // template is missing
                UtilFactory.log("** Template is missing **" ) ;
                return null ;
            }
            boolean fileGenerated = conv.generatePDF(xmlDoc, templatewithTags,  reportPath, "AdhocReport") ;
            if (fileGenerated) {
                // send the file name/URL back
                return conv.getPdfFileName() ;
            }
        } // end if 'C'
        
        if (adhocDetailsBean.getModule() == 'S') //schedule module adhoc report type 1
        {
            Document xmlDoc = xmlStreamGenerator.scheduleXMLStreamGenerator(adhocDetailsBean.getScheduleId()) ; //schedule id
            UtilFactory.log("Xml doc generating complete!") ;
            UtilFactory.log("** Module " + adhocDetailsBean.getModule() + " Schedule (type 1) " + committeeId) ;
            
            
            if (templatewithTags == null) {
                // template is missing
                UtilFactory.log("** Template is missing **" ) ;
                return null ;
            }
            boolean fileGenerated = conv.generatePDF(xmlDoc, templatewithTags,  reportPath, "AdhocReport") ;
            if (fileGenerated) {
                // send the file name/URL back
                return conv.getPdfFileName() ;
            }
        } // end if 'S' - type 1
        if (adhocDetailsBean.getModule() == 'U') { //schedule module adhoc report type 2
            ProtocolActionsBean actionBean = new ProtocolActionsBean() ;
            actionBean.setProtocolNumber(adhocDetailsBean.getProtocolNumber()) ;
            actionBean.setSequenceNumber(adhocDetailsBean.getSequenceNumber()) ;
            actionBean.setScheduleId(adhocDetailsBean.getScheduleId()) ;
            actionBean.setSubmissionNumber(adhocDetailsBean.getSubmissionNumber()) ;
            
            Document xmlDoc = xmlStreamGenerator.correspondenceXMLStreamGenerator(actionBean) ;
            UtilFactory.log("Xml doc generating complete!") ;
            UtilFactory.log("** Module " + adhocDetailsBean.getModule() + " Schedule (type 2)" + committeeId) ;
            if (templatewithTags == null) {
                // template is missing
                UtilFactory.log("** Template is missing **" ) ;
                return null ;
            }
            boolean fileGenerated = conv.generatePDF(xmlDoc, templatewithTags,  reportPath, "AdhocReport") ;
            if (fileGenerated) {
                // save the pdf to database
                saveCorrespondence(adhocDetailsBean, conv.getGeneratedPdfFileBytes()) ;
                // send the file name/URL back
                return conv.getPdfFileName() ;
            }
        } // end if 'T' type 2
        
        if (adhocDetailsBean.getModule() == 'P') { //Protocol module adhoc report
            Document xmlDoc = xmlStreamGenerator.protocolXMLStreamGenerator(adhocDetailsBean.getProtocolNumber(), adhocDetailsBean.getSequenceNumber()) ; //protocol id
            UtilFactory.log("Xml doc generating complete!") ;
            UtilFactory.log("** Module " + adhocDetailsBean.getModule() + " Protocol " + committeeId) ;
            if (templatewithTags == null) {
                // template is missing
                UtilFactory.log("** Template is missing **" ) ;
                return null ;
            }
            boolean fileGenerated = conv.generatePDF(xmlDoc, templatewithTags,  reportPath, "AdhocReport") ;
            if (fileGenerated) {
                // save the pdf to database
                saveCorrespondence(adhocDetailsBean, conv.getGeneratedPdfFileBytes()) ;
                // send the file name/URL back
                return conv.getPdfFileName() ;
            }
        } // end if 'P'
        return null;
    }
   
    public String generateAdhocReport(AdhocDetailsBean adhocDetailsBean, String reportPath) throws javax.xml.bind.JAXBException, 
                                                                                                   edu.mit.coeus.exception.CoeusException, DBException,
                                                                                                   javax.xml.parsers.ParserConfigurationException, javax.xml.transform.TransformerConfigurationException,
                                                                                                   javax.xml.transform.TransformerException, java.io.IOException, org.apache.fop.apps.FOPException
    {
                XMLStreamGenerator xmlStreamGenerator = new XMLStreamGenerator() ;
                XMLPDFTxnBean pdfTxnBean = new XMLPDFTxnBean() ;
                XMLtoPDFConvertor conv = new XMLtoPDFConvertor() ;
                String committeeId = "DEFAULT" ;
                
            if (adhocDetailsBean.getModule() == 'C') //committee module adhoc report 
            {
                Document xmlDoc = xmlStreamGenerator.committeeXMLStreamGenerator(adhocDetailsBean.getCommitteeId()) ; //committee id
                System.out.println("Xml doc generating complete!") ; 
                UtilFactory.log("Xml doc generating complete!") ;
                
                System.out.println("** Module " + adhocDetailsBean.getModule() + " Committee " + committeeId) ;
                UtilFactory.log("** Module " + adhocDetailsBean.getModule() + " Committee " + committeeId) ;
                //Added for  case id COEUSQA-1724 iacuc protocol stream generation  
                byte[] templateFileFromDb = pdfTxnBean.getIACUCCorrespondenceTemplate(committeeId, Integer.parseInt(adhocDetailsBean.getFormId())) ;
                if (templateFileFromDb == null)
                {
                    // template is missing
                    System.out.println("** Template is missing **" ) ;
                    UtilFactory.log("** Template is missing **" ) ;
                    return null ;
                }    
                boolean fileGenerated = conv.generatePDF(xmlDoc, templateFileFromDb,  reportPath, "AdhocReport") ;
                if (fileGenerated)
                {    
                    // send the file name/URL back 
                    return conv.getPdfFileName() ;
                }
            } // end if 'C'
            
            if (adhocDetailsBean.getModule() == 'S') //schedule module adhoc report type 1
            {
                Document xmlDoc = xmlStreamGenerator.scheduleXMLStreamGenerator(adhocDetailsBean.getScheduleId()) ; //schedule id
                System.out.println("Xml doc generating complete!") ;
                UtilFactory.log("Xml doc generating complete!") ;
                
                System.out.println("** Module " + adhocDetailsBean.getModule() + " Schedule (type 1) " + committeeId) ;
                UtilFactory.log("** Module " + adhocDetailsBean.getModule() + " Schedule (type 1) " + committeeId) ;
                //Added for  case id COEUSQA-1724 iacuc protocol stream generation  
                byte[] templateFileFromDb = pdfTxnBean.getIACUCCorrespondenceTemplate(committeeId, Integer.parseInt(adhocDetailsBean.getFormId())) ;
                if (templateFileFromDb == null)
                {
                    // template is missing
                    System.out.println("** Template is missing **" ) ;
                    UtilFactory.log("** Template is missing **" ) ;
                    return null ;
                }    
                boolean fileGenerated = conv.generatePDF(xmlDoc, templateFileFromDb,  reportPath, "AdhocReport") ;
                if (fileGenerated)
                {    
                    // send the file name/URL back 
                    return conv.getPdfFileName() ;
                }
            } // end if 'S' - type 1

            if (adhocDetailsBean.getModule() == 'U') //schedule module adhoc report type 2
            {
                ProtocolActionsBean actionBean = new ProtocolActionsBean() ;
                actionBean.setProtocolNumber(adhocDetailsBean.getProtocolNumber()) ;
                actionBean.setSequenceNumber(adhocDetailsBean.getSequenceNumber()) ;
                actionBean.setScheduleId(adhocDetailsBean.getScheduleId()) ;
                actionBean.setSubmissionNumber(adhocDetailsBean.getSubmissionNumber()) ;
                
                Document xmlDoc = xmlStreamGenerator.correspondenceXMLStreamGenerator(actionBean) ; 
                System.out.println("Xml doc generating complete!") ;
                UtilFactory.log("Xml doc generating complete!") ;
                
                System.out.println("** Module " + adhocDetailsBean.getModule() + " Schedule (type 2)" + committeeId) ;
                UtilFactory.log("** Module " + adhocDetailsBean.getModule() + " Schedule (type 2)" + committeeId) ;
                //Added for  case id COEUSQA-1724 iacuc protocol stream generation  
                byte[] templateFileFromDb = pdfTxnBean.getIACUCCorrespondenceTemplate(committeeId, Integer.parseInt(adhocDetailsBean.getFormId())) ;
                if (templateFileFromDb == null)
                {
                    // template is missing
                    System.out.println("** Template is missing **" ) ;
                    UtilFactory.log("** Template is missing **" ) ;
                    return null ;
                }    
                boolean fileGenerated = conv.generatePDF(xmlDoc, templateFileFromDb,  reportPath, "AdhocReport") ;
                if (fileGenerated)
                {  
                     // save the pdf to database
                    saveCorrespondence(adhocDetailsBean, conv.getGeneratedPdfFileBytes()) ;
                    // send the file name/URL back 
                    return conv.getPdfFileName() ;
                }
            } // end if 'T' type 2
                
            if (adhocDetailsBean.getModule() == 'P') //Protocol module adhoc report 
            {
                Document xmlDoc = xmlStreamGenerator.protocolXMLStreamGenerator(adhocDetailsBean.getProtocolNumber(), adhocDetailsBean.getSequenceNumber()) ; //protocol id
                System.out.println("Xml doc generating complete!") ;
                UtilFactory.log("Xml doc generating complete!") ;
                
                System.out.println("** Module " + adhocDetailsBean.getModule() + " Protocol " + committeeId) ;
                UtilFactory.log("** Module " + adhocDetailsBean.getModule() + " Protocol " + committeeId) ;
                 //Added for  case id COEUSQA-1724 iacuc protocol stream generation  
                byte[] templateFileFromDb = pdfTxnBean.getIACUCCorrespondenceTemplate(committeeId, Integer.parseInt(adhocDetailsBean.getFormId())) ;
                if (templateFileFromDb == null)
                {
                    // template is missing
                    System.out.println("** Template is missing **" ) ;
                    UtilFactory.log("** Template is missing **" ) ;
                    return null ;
                }    
                boolean fileGenerated = conv.generatePDF(xmlDoc, templateFileFromDb,  reportPath, "AdhocReport") ;
                if (fileGenerated)
                {   
                    // save the pdf to database
                    saveCorrespondence(adhocDetailsBean, conv.getGeneratedPdfFileBytes()) ;
                    // send the file name/URL back    
                    return conv.getPdfFileName() ;
                }
            } // end if 'P'
        
        
        return null ;
    }
    
    
    private boolean saveCorrespondence(AdhocDetailsBean adhocDetailsBean, byte[] generatedFileBytes)
    {
        ProtoCorrespTypeTxnBean protoCorrespTypeTxnBean = new ProtoCorrespTypeTxnBean(userId);
        try
        {
            int actionId = logAction(adhocDetailsBean) ;
            ProtocolActionsBean actionBean = new ProtocolActionsBean() ;
            actionBean.setActionId(actionId) ;
            actionBean.setProtocolNumber(adhocDetailsBean.getProtocolNumber()) ;
            actionBean.setSequenceNumber(adhocDetailsBean.getSequenceNumber()) ;
            actionBean.setSubmissionNumber(adhocDetailsBean.getSubmissionNumber()) ;
            boolean blnCorrGenerated = protoCorrespTypeTxnBean.addUpdProtocolCorrespondence(actionBean, Integer.parseInt(adhocDetailsBean.getFormId()) , generatedFileBytes);
        }
        catch(Exception ex)
        {
            return false ;
        }
        return true ;
    }
    
    
    private int logAction(AdhocDetailsBean adhocDetailsBean)
    {
      int success = -1 ;  
      try
       {
          
       // get the new seq id and do the updation 
        Vector param= new Vector();
        HashMap nextNumRow = null;
        Vector result = new Vector();
        param= new Vector();
        param.add(new Parameter("AV_PROTOCOL_NUMBER",
                    DBEngineConstants.TYPE_STRING, adhocDetailsBean.getProtocolNumber() )) ;
        param.add(new Parameter("AV_SEQUENCE_NUMBER",
                    DBEngineConstants.TYPE_INT, String.valueOf(adhocDetailsBean.getSequenceNumber()))) ;
        param.add(new Parameter("AV_PROTOCOL_ACTION_TYPE_CODE",
                    DBEngineConstants.TYPE_INT, String.valueOf(CORRESPONDENCE_ACTION_TYPE_CODE))) ; // correspondence action type code is set to 110
        param.add(new Parameter("AV_COMMENTS",
                    DBEngineConstants.TYPE_STRING, adhocDetailsBean.getDescription() )) ;
        param.add(new Parameter("AV_SUBMISSION_NUMBER",
                            DBEngineConstants.TYPE_INT, String.valueOf(adhocDetailsBean.getSubmissionNumber()))) ;
        param.add(new Parameter("AV_UPDATE_USER",
                    DBEngineConstants.TYPE_STRING, userId )) ;
		
		if(dbEngine!=null){
            result = dbEngine.executeFunctions("Coeus",
            "{<<OUT INTEGER SUCCESS>> = call pkg_protocol_actions.fn_log_protocol_action( "
            + " << AV_PROTOCOL_NUMBER >> , << AV_SEQUENCE_NUMBER >> , << AV_PROTOCOL_ACTION_TYPE_CODE >> , << AV_COMMENTS >>, << AV_SUBMISSION_NUMBER >> , << AV_UPDATE_USER >> )}", param) ;
        }else{
            throw new CoeusException("db_exceptionCode.1000") ;
        }
        
        if(!result.isEmpty())
        {
            nextNumRow = (HashMap)result.elementAt(0);
            success = Integer.parseInt(nextNumRow.get("SUCCESS").toString());
            System.out.println("** Correspondence generated Action logged") ;
        }
        
     }
     catch(Exception ex)
     {
        ex.printStackTrace() ;
        System.out.println("** Failed to log Correspondence generated Action") ;
     }
     
      return success ;
    }
    
    
    
    
}


