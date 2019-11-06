

package edu.mit.coeus.servlet;

import edu.mit.coeus.bean.*;
import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.bean.UserInfoBean;

import edu.mit.coeus.utils.*;
import edu.mit.coeus.bean.CoeusMessageResourcesBean;
import edu.mit.coeus.utils.dbengine.DBException;

import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.irb.bean.*;
import edu.mit.coeus.user.bean.UserMaintDataTxnBean;

import edu.mit.coeus.utils.xml.generator.* ;
import edu.mit.coeus.propdev.bean.*;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.ServletException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.DecimalFormat;
import java.util.Vector;
import java.io.File;
import org.w3c.dom.Document;
import java.io.OutputStream;
import java.io.FileInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Properties;
import edu.mit.coeus.utils.pdf.generator.* ;
import org.apache.fop.apps.FOPException;
import edu.mit.coeus.user.bean.*;


/**
 *
 * @author  phani
 * @version
 */
public class ProposalXMLGeneratorServlet extends CoeusBaseServlet {
    
    
    //    private UtilFactory UtilFactory = new UtilFactory();
    
    
    /** Destroys the servlet.
     */
    public void destroy() {
        
    }
    
    /** Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, java.io.IOException {
        
        // the request object from applet
        RequesterBean requester = null;
        // the response object to applet
        ResponderBean responder = new ResponderBean();
        
        
        // open object input/output streams
        ObjectInputStream inputFromApplet = null;
        ObjectOutputStream outputToApplet = null;
        
        String loggedinUser ="";
        
        String reportPath = null ;
        String pdfPath = null ;
        
        try {
            System.out.println(" Inside New Servlet ") ;
            // get an input stream
            inputFromApplet = new ObjectInputStream(request.getInputStream());
            //// read the serialized request object from applet
            requester = (RequesterBean) inputFromApplet.readObject();
            isValidRequest(requester);
            
            //            System.out.println(" Before reading properties from Coeus.properties file ") ;
            //            CoeusConstants.SERVER_HOME_PATH = this.getServletContext().getRealPath("/");
            //            InputStream is = getClass().getResourceAsStream("/coeus.properties");
            //            System.out.println(" After reading properties from Coeus.properties file ") ;
            //            Properties coeusProps = new Properties();
            //
            //            coeusProps.load(is);
            CoeusConstants.SERVER_HOME_PATH = this.getServletContext().getRealPath("/");
            reportPath = CoeusConstants.SERVER_HOME_PATH
            + CoeusProperties.getProperty(CoeusPropertyKeys.REPORT_GENERATED_PATH) + "/" ;
            pdfPath = "/" + CoeusProperties.getProperty(CoeusPropertyKeys.REPORT_GENERATED_PATH) + "/" ;
            
            if (CoeusProperties.getProperty(CoeusPropertyKeys.GENERATE_XML_FOR_DEBUGGING) != null) {
                responder.setId(CoeusProperties.getProperty(CoeusPropertyKeys.GENERATE_XML_FOR_DEBUGGING)) ;
            }
            else {
                responder.setId("No") ;
            }
            
            
            PropXMLStreamGenerator propXMLStreamGenerator = new PropXMLStreamGenerator() ;
            
            String propNumber = requester.getId();
            
            /** cover page
             */
            //            Document xmlDoc = propXMLStreamGenerator.rarPropXML(propNumber);
            //get sponsor from requester too...add later
            String sponsor = "NIH";
            
            /** cover page
             */
            Document xmlDoc = propXMLStreamGenerator.rarPropXML(propNumber, sponsor);
            
            System.out.println("Xml doc generating complete!") ;
            UtilFactory.log("Xml doc generating complete!") ;
            
            
            
            
            XMLtoPDFConvertor conv = new XMLtoPDFConvertor() ;
            /**get template from report folder
             */
            File templateFile = new File(reportPath + "CoverPage.xsl");
            
            if (templateFile == null) {
                responder.setResponseStatus(false);
                responder.setMessage("Template file is missing");
                return ;
            }
            
            
            boolean fileGenerated = conv.generatePDF(xmlDoc, templateFile,  reportPath, "NIHCoverpage") ;
            if (fileGenerated) {
                // send the file name/URL back
                responder.setDataObject( pdfPath + conv.getPdfFileName()) ;
                responder.setId("Yes") ;
                responder.setResponseStatus(true);
                responder.setMessage("Requested report generated") ;
                
            }
            
            else {
                //No Action Rights message
                responder.setResponseStatus(false);
                CoeusMessageResourcesBean coeusMessageResourcesBean
                =new CoeusMessageResourcesBean();
                String errMsg =
                coeusMessageResourcesBean.parseMessageKey(
                "agendaAuthorization_exceptionCode.3301");
                
                //Sending exception to client side. - Prasanna
                CoeusException ex = new CoeusException(errMsg,1);
                responder.setDataObject(ex);
                responder.setMessage(errMsg);
                //end
            }
            
            
            
            
        }
        catch(DBException dbex) {
            //System.out.println("*** DBError ***") ;
            //dbex.printStackTrace() ;
            responder.setResponseStatus(false);
            responder.setException(dbex);
            responder.setMessage(dbex.getMessage()) ;
            UtilFactory.log( dbex.getMessage(), dbex, "XMLGeneratorServlet", "perform");
        }
        catch(javax.xml.bind.JAXBException jex) {
            //System.out.println("*** JAXBError ***") ;
            responder.setResponseStatus(false);
            //jex.printStackTrace() ;
            responder.setMessage(jex.getMessage()) ;
            responder.setException(jex);
            UtilFactory.log( jex.getMessage(), jex, "XMLGeneratorServlet", "perform");
        }
        catch(IOException iex) {
            //System.out.println("*** IOError ***") ;
            responder.setResponseStatus(false);
            //iex.printStackTrace() ;
            //UtilFactory.log(iex.getMessage()) ;
            responder.setException(iex);
            responder.setMessage(iex.getMessage()) ;
            UtilFactory.log( iex.getMessage(), iex, "XMLGeneratorServlet", "perform");
        }
        catch(FOPException fex) {
            //System.out.println("*** FOPError ***") ;
            responder.setResponseStatus(false);
            //fex.printStackTrace() ;
            //UtilFactory.log(fex.getMessage()) ;
            responder.setException(fex);
            responder.setMessage(fex.getMessage()) ;
            UtilFactory.log( fex.getMessage(), fex, "XMLGeneratorServlet", "perform");
        }
        catch(javax.xml.parsers.ParserConfigurationException pex) {
            //System.out.println("*** Error ***") ;
            responder.setResponseStatus(false);
            //pex.printStackTrace() ;
            //UtilFactory.log(pex.getMessage()) ;
            responder.setException(pex);
            responder.setMessage(pex.getMessage()) ;
            UtilFactory.log( pex.getMessage(), pex, "XMLGeneratorServlet", "perform");
        }
        catch(javax.xml.transform.TransformerConfigurationException tcex) {
            //System.out.println("*** Error ***") ;
            responder.setResponseStatus(false);
            //tcex.printStackTrace() ;
            //UtilFactory.log(tcex.getMessage()) ;
            responder.setException(tcex);
            responder.setMessage(tcex.getMessage()) ;
            UtilFactory.log( tcex.getMessage(), tcex, "XMLGeneratorServlet", "perform");
        }
        catch(javax.xml.transform.TransformerException tex) {
            //System.out.println("*** Error ***") ;
            responder.setResponseStatus(false);
            //tex.printStackTrace() ;
            //UtilFactory.log(tex.getMessage()) ;
            responder.setException(tex);
            responder.setMessage(tex.getMessage()) ;
            UtilFactory.log( tex.getMessage(), tex, "XMLGeneratorServlet", "perform");
        }
        catch(Exception e) {
            //print the error message at server side
            responder.setResponseStatus(false);
            //print the error message at client side
            //UtilFactory.log(e.getMessage()) ;
            responder.setException(e);
            responder.setMessage(e.getMessage());
            UtilFactory.log( e.getMessage(), e, "XMLGeneratorServlet", "perform");
        //Case 3193 - START
        }catch(Throwable throwable){
            Exception ex = new Exception(throwable);
            responder.setException(ex);
            responder.setResponseStatus(false);
            responder.setMessage(ex.getMessage());
            UtilFactory.log( throwable.getMessage(), throwable, "XMLGeneratorServlet", "doPost");
        //Case 3193 - END
        } finally {
            
            //explicitly release the resources like I/O Stream, communication
            //channel
            try{
                
                // send the object to applet
                outputToApplet = new ObjectOutputStream(
                response.getOutputStream());
                outputToApplet.writeObject( responder );
                
                // close the streams
                if (inputFromApplet!=null){
                    inputFromApplet.close();
                }
                
                if (outputToApplet!=null){
                    outputToApplet.flush();
                    outputToApplet.close();
                }
            }catch (IOException ioe){
                UtilFactory.log(ioe.getMessage(),ioe,"PropXMLGeneratorServlet", "perform");
            }
        }
    }
    
    /** Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, java.io.IOException {
        processRequest(request, response);
    }
    
    /** Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, java.io.IOException {
        processRequest(request, response);
    }
    
    /** Returns a short description of the servlet.
     */
    public String getServletInfo() {
        return "Short description";
    }
    
}

