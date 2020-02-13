/*
 * CoeusSearchResultServlet.java
 *
 * Created on August 22, 2002, 10:50 PM
 */
 
package edu.mit.coeus.servlet;           

import javax.servlet.*;
import javax.servlet.http.*;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;

import java.util.Hashtable;
import java.util.Vector;

import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.search.bean.ProcessSearchXMLBean;
import edu.mit.coeus.search.bean.SearchInfoHolderBean;
import edu.mit.coeus.search.bean.SearchExecutionBean;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.bean.CoeusMessageResourcesBean;
import edu.mit.coeus.search.exception.CoeusSearchException;
import edu.mit.coeus.utils.dbengine.DBException;
/** 
 *
 * @author  geo
 * @version
 * @modified by Sagin
 * @date 29-10-02
 * Description : Implemented Standard Error Handling. 
 * 
 */
public class CoeusSearchResultServlet extends CoeusBaseServlet {
   
    /** Initializes the servlet.
    */  
    public void init(ServletConfig config) throws ServletException {
        super.init(config);

    }

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

//        UtilFactory UtilFactory = new UtilFactory();
        
        // modified by ravi for removing session maintenance
        //HttpSession session = request.getSession(true);

        try {
            responder.setResponseStatus(true);
            // get an input stream
            inputFromApplet = new ObjectInputStream(request.getInputStream());
            // read the serialized request object from applet
            requester = (RequesterBean) inputFromApplet.readObject();
            isValidRequest(requester);
            // modified by ravi for removing session maintenance
            Vector objects = (Vector)requester.getDataObjects();
            SearchInfoHolderBean searchInfoHolder 
                = (SearchInfoHolderBean)objects.elementAt(0);
            Vector columnList = (Vector) objects.elementAt(1);
            String customQuery = (String) objects.get(2);//Added by Nadh for CustomQuery Search enhancement   start : 3-aug-2005
            //SearchInfoHolderBean searchInfoHolder = (SearchInfoHolderBean)session.getAttribute(
              //                                          "searchinfoholder");
            if(searchInfoHolder==null){
                throw new CoeusSearchException("Search Information is not available ");
            }
            SearchExecutionBean searchExecution = new SearchExecutionBean(searchInfoHolder);
            
            //session.removeValue("searchinfoholder");
            //session.removeAttribute("searchinfoholder");
            searchExecution.setColumnList(columnList);
            searchExecution.setCustomQuery(customQuery);//Added by Nadh for CustomQuery Search enhancement   start : 3-aug-2005
            
            Hashtable result = searchExecution.executeSearchQuery();
            responder.setDataObject(result);
        }catch( CoeusException coeusEx ) {
            
            int index=0;
            String errMsg;
            
            errMsg = coeusEx.getMessage();
            CoeusMessageResourcesBean coeusMessageResourcesBean=new CoeusMessageResourcesBean();
            errMsg= coeusMessageResourcesBean.parseMessageKey(errMsg);
            
            responder.setResponseStatus(false);

            //print the error message at client side
            responder.setException(coeusEx);
            responder.setMessage(errMsg);
            UtilFactory.log( errMsg, coeusEx, "CoeusSearchResultServlet",
                                                                "perform");
            
        }catch( CoeusSearchException coeusSearchEx ) {
            
            int index=0;
            String errMsg;
            
            errMsg = coeusSearchEx.getMessage();
            CoeusMessageResourcesBean coeusMessageResourcesBean=new CoeusMessageResourcesBean();
            errMsg= coeusMessageResourcesBean.parseMessageKey(errMsg);
            
            responder.setResponseStatus(false);

            //print the error message at client side
//            responder.setException(coeusSearchEx);
            responder.setMessage(errMsg);
//            UtilFactory.log( errMsg, coeusSearchEx, "CoeusSearchResultServlet",
//                                                                "perform");
            
        }catch( DBException dbEx ) {
            
            int index=0;
            String errMsg;
            
            errMsg = dbEx.getUserMessage();
            CoeusMessageResourcesBean coeusMessageResourcesBean=new CoeusMessageResourcesBean();
            errMsg= coeusMessageResourcesBean.parseMessageKey(errMsg);
            
            responder.setResponseStatus(false);

            //print the error message at client side
            responder.setException(dbEx);
            responder.setMessage(errMsg);
            UtilFactory.log( errMsg, dbEx, "CoeusSearchResultServlet", "perform");
            
        }catch (Exception ex) {
            responder.setResponseStatus(false);
            responder.setException(ex);
            responder.setMessage(ex.getMessage());
            UtilFactory.log(ex.getMessage(),ex,"CoeusSearchResultServlet","perform");
        }finally{
            try{
                // send the object to applet
                outputToApplet = new ObjectOutputStream(response.getOutputStream());
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
                UtilFactory.log(ioe.getMessage(),ioe,"CoeusSearchResultServlet","perform");
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
