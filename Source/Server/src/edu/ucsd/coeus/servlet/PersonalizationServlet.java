package edu.ucsd.coeus.servlet;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.utils.UtilFactory;
import edu.ucsd.coeus.personalization.Personalization;

public class PersonalizationServlet extends HttpServlet {
	
    public void init(ServletConfig config) throws ServletException{
        super.init(config);
    }
    
    /**
     *  This method is used for applets.
     */
    public void doGet(HttpServletRequest request,
    HttpServletResponse response)
    throws ServletException, IOException {
    }
    
    /**
     *  This method is used for applets.
     *  Gets an input stream from the applet, reads the username and password
     *  and authenticates with the database.
     */
    public void doPost(HttpServletRequest request,
    HttpServletResponse response)
    throws ServletException, IOException {
        
        // the request object from applet
        RequesterBean requester = null;
        // the response object to applet
        final ResponderBean responder = new ResponderBean();
        
        // open object input/output streams
        ObjectInputStream inputFromApplet = null;
        ObjectOutputStream outputToApplet = null;
        
        /* message to be returned to the applet */
        boolean success = false;
        
        try {
            // get an input stream
            inputFromApplet = new ObjectInputStream(request.getInputStream());
            // read the serialized request object from applet
            requester = (RequesterBean) inputFromApplet.readObject();
            if (requester.getId().equalsIgnoreCase("PERSONALIZE_XML")) {
                Personalization.getInstance().setLocalizationXML(responder,requester);
                success = true ;
            }
        }catch (Exception ex) {
            /* Log any other exceptions in error log. */
            UtilFactory.log(ex.getMessage(),ex,"PersonalizationServlet","perform");
            success = false;
            responder.setMessage(ex.getMessage());
        } finally {
            try{
                responder.setResponseStatus(success);
                // send the object to applet
                outputToApplet =
                new ObjectOutputStream(response.getOutputStream());
                outputToApplet.writeObject(responder);
                // close the streams
                if (inputFromApplet!=null){
                    inputFromApplet.close();
                }
                if (outputToApplet!=null){
                    outputToApplet.flush();
                    outputToApplet.close();
                }
            }catch (IOException ioe){
                UtilFactory.log( ioe.getMessage(), ioe,
                "PersonalizationServlet", "perform");
            }
        }
    }
    	

}
