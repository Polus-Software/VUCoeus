/*
 * SchedulerServiceMaintServlet.java
 *
 * Created on October 17, 2006, 5:15 PM
 */

package edu.mit.coeus.servlet;

import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.utils.scheduler.SchedulerEngine;
import java.io.*;
import java.net.*;

import javax.servlet.*;
import javax.servlet.http.*;

/**
 *
 * @author  geot
 * @version
 */
public class SchedulerServiceMaintServlet extends CoeusBaseServlet {
    //private SchedulerEngine scheduler;
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
    throws ServletException, IOException {
        //Added for Case#4585 - Protocol opened from list window is not the correct one - Start 
        SchedulerEngine scheduler = null;
        //Case#4585 - End
        try{
            System.out.println("In scheduler servlet ");
            scheduler = SchedulerEngine.getInstance();
            String acType = request.getParameter("acType");
            System.out.println("acType "+acType);
            if(acType.equalsIgnoreCase("stoptask")){
                String taskId = request.getParameter("taskId");
                String jobName = request.getParameter("jobName");
                scheduler.stopService(jobName);
            }else if(acType.equalsIgnoreCase("stopalltasks")){
                scheduler.stopAllServices();
            }else if(acType.equalsIgnoreCase("restartalltasks")){
                scheduler.restartAllServices();
            }
        }catch(CoeusException ex){
            UtilFactory.log(ex.getMessage(),ex, "SchedulerServiceMaintServlet", "processRequest");
        }
        response.sendRedirect("tools/CoeusServiceWatch.jsp");
    }
    
    /** Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        processRequest(request, response);
    }
    
    /** Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        processRequest(request, response);
    }
    
    /** Returns a short description of the servlet.
     */
    public String getServletInfo() {
        return "Short description";
    }
    
}
