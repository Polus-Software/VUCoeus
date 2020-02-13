/**
 * SubcontractServlet.java
 * 
 * Servlet to get subcontract data
 *
 * @created	May 2, 2014
 * @author	Jill McAfee, Vanderbilt University
 */

package edu.vanderbilt.coeus.servlet;

import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.servlet.CoeusBaseServlet;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.utils.TypeConstants;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.utils.CoeusVector;

import javax.servlet.http.*;
import javax.servlet.*;

import java.io.*;
import java.util.Vector;

import edu.vanderbilt.coeus.subcontract.bean.SubcontractTxnBean;

public class SubcontractServlet extends CoeusBaseServlet implements TypeConstants {
    
    private static final char GET_PERSON = 'P';

    /**
     * This method handles all the POST requests from the Client
     *
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @throws ServletException if any ServletException
     * @throws IOException if any IOException
     */
    public void doPost(HttpServletRequest request, HttpServletResponse response)
    	throws ServletException, IOException {
        
        RequesterBean requester = null;
        ResponderBean responder = new ResponderBean();
        
        // Open object input/output streams
        ObjectInputStream inputFromApplet = null;
        ObjectOutputStream outputToApplet = null;
        
        try {
            inputFromApplet = new ObjectInputStream(request.getInputStream());
            requester = (RequesterBean) inputFromApplet.readObject();
            isValidRequest(requester);

            // keep all the beans into vector
            Vector dataObjects = new Vector();
            
            char functionType = requester.getFunctionType();
            SubcontractTxnBean txnBean = new SubcontractTxnBean();
            CoeusVector results = new CoeusVector();
            String personId = "";
            
            /* Get person data */
            if(functionType == GET_PERSON) {
                personId = (String) requester.getId();
                results = txnBean.getPerson(personId);
                responder.setDataObject(results);
                responder.setMessage("Got person information");
                responder.setResponseStatus(true);
             }
            
            else {
            	
            }
        }
        catch (CoeusException e) {
        	UtilFactory.log("SubcontractServlet:: A CoeusException was thrown");        	
        	e.printStackTrace();
        }        
        catch (ClassNotFoundException e) {
        	UtilFactory.log("SubcontractServlet :: Class not found");
        	e.printStackTrace();
        } catch (DBException e) {
        	UtilFactory.log("SubcontractServlet :: Database exception");
        	e.printStackTrace();
		} 
        finally {
            try{
                // Send the object to applet
                outputToApplet = new ObjectOutputStream(response.getOutputStream());
                outputToApplet.writeObject(responder);
                
                // Close the streams
                if (inputFromApplet!=null){
                    inputFromApplet.close();
                }
                if (outputToApplet!=null){
                    outputToApplet.flush();
                    outputToApplet.close();
                }
            }catch (IOException ioe){
                UtilFactory.log(ioe.getMessage(),ioe,"SubcontractServlet","perform");
            }
        }
    }

}
