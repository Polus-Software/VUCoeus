/*
 * CoeusSearchServlet.java
 *
 * Created on August 19, 2002, 6:30 PM
 */
 
package edu.mit.coeus.servlet;           

import javax.servlet.*;
import javax.servlet.http.*;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;

import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.search.bean.ProcessSearchXMLBean;
import edu.mit.coeus.search.bean.SearchInfoHolderBean;
import edu.mit.coeus.search.exception.CoeusSearchException;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.bean.CoeusMessageResourcesBean;


/** 
 *
 * @author  geo
 * @version 
 * @modified by Sagin
 * @date 29-10-02
 * Description : Implemented Standard Error Handling. 
 * 
 */
public class CoeusSearchServlet extends CoeusBaseServlet {
   
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
    throws ServletException{
        ResponderBean responder = new ResponderBean();
        RequesterBean requester = new RequesterBean();
//        UtilFactory UtilFactory = new UtilFactory();
        
        // modified by ravi to remove session maintenance
        //HttpSession session = request.getSession(true);
        
        // open object input/output streams
        ObjectInputStream inputFromApplet = null;
        ObjectOutputStream outputToApplet = null;
        try {
            responder.setResponseStatus(true);
            //System.out.println("got the connection in coeus servlet");
            // get the search name from applet
            //String searchReq = request.getParameter("searchreq");
            inputFromApplet = new ObjectInputStream(request.getInputStream());
            // read the serialized request object from applet
            requester = (RequesterBean) inputFromApplet.readObject();
            isValidRequest(requester);
            String searchReq = (String)requester.getDataObject();
            //System.out.println("searchname=>"+searchReq);
            ProcessSearchXMLBean processSearchXML = new ProcessSearchXMLBean("",searchReq);
            
            SearchInfoHolderBean searchInfoHolder = processSearchXML.getSearchInfoHolder();

            // modified by ravi to remove session maintenance
            //System.out.println(searchInfoHolder.toString());
            //session.setAttribute("searchinfoholder",searchInfoHolder);
            responder.setDataObject(searchInfoHolder);
        }catch( CoeusSearchException coeusSearchEx ) {
            
            int index=0;
            String errMsg;
            
            errMsg = coeusSearchEx.getMessage();
            CoeusMessageResourcesBean coeusMessageResourcesBean=new CoeusMessageResourcesBean();
            errMsg= coeusMessageResourcesBean.parseMessageKey(errMsg);
            
            responder.setResponseStatus(false);

            //print the error message at client side
            responder.setMessage(errMsg);
            responder.setException(coeusSearchEx);
            UtilFactory.log( errMsg, coeusSearchEx, "CoeusSearchServlet",
                                                                "perform");
            
        }catch(Exception ex) {
            responder.setResponseStatus(false);
            responder.setException(ex);
            responder.setMessage(ex.getMessage());
            UtilFactory.log(ex.getMessage(),ex,"CoeusSearchServlet","perform");
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
                UtilFactory.log(ioe.getMessage(),ioe,"CoeusSearchServlet","perform");
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
