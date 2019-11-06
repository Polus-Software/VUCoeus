/**
  $Header: /home/cvs/cvsroot/coeus/Source/WEB-INF/classes/edu/umdnj/coeus/servlet/Attic/GraphingServlet.java,v 1.1.2.4 2007/01/23 18:12:29 cvs Exp $
  $Log: GraphingServlet.java,v $
  Revision 1.1.2.4  2007/01/23 18:12:29  cvs
  Add GUI support for Graph Sponsor Types

  Revision 1.1.2.3  2006/12/08 19:46:23  cvs
  Add support for JFreeChart charting engine

  Revision 1.1.2.2  2006/11/20 20:49:50  cvs
  Add TDG engine support for demo purposes

  Revision 1.1.2.1  2006/11/15 20:27:58  cvs
  Add ReportingServlet and GraphingServlet servlet objects for COEUS application

*/
/*
 * @(#)GraphingServlet.java 
 *
 * Copyright (c) University of Medicine and Dentistry of New Jersey
 *
 * 1 World's Fair Drive, Somerset, New Jersey 08873
 *
 * All rights reserved.
 *
 * 
 * Author: Romerl Elizes
 *
 * Description: Graphing Servlet mechanism for UMDNJ COEUS 411.
 * 
 */

package edu.umdnj.coeus.servlet;

import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.umdnj.coeus.reporting.bean.GraphingBean;

public class GraphingServlet extends HttpServlet
{

  //////////////////////////////////////////////////////////////
  /** doPost
   *  doPost Method to populate graph.
   *  @param req - HttpServletRequest object
   *  @param res - HttpServletResponse object
   *  @exception ServletException
   *  @exception IOException
   */
  public void doPost(HttpServletRequest req, HttpServletResponse res)
         throws ServletException, IOException
  {
    generateGraph(req,res);
  }

  //////////////////////////////////////////////////////////////
  /** doGet
   *  doGet Method that to populate graph.
   *  @param req - HttpServletRequest object
   *  @param res - HttpServletResponse object
   *  @exception ServletException
   *  @exception IOException
   */
  public void doGet(HttpServletRequest req, HttpServletResponse res)
         throws ServletException, IOException
  {
    generateGraph(req,res);
  }

  //////////////////////////////////////////////////////////////
  /** generateGraph
   *  Method that generates Graphs.
   *  @param req - HttpServletRequest object
   *  @param res - HttpServletResponse object
   *  @exception ServletException
   *  @exception IOException
   */
  public void generateGraph(HttpServletRequest req, HttpServletResponse res)
         throws ServletException, IOException
  {
    String strSQL = req.getParameter("SQLRequest");
    String strFormat = req.getParameter("Format");
    String strType = req.getParameter("GraphType");
    String strSchool = req.getParameter("School");
    String strChartTitle = req.getParameter("ChartTitle");
    
    GraphingBean obj = new GraphingBean();
    try {
	obj.SampleGraph(strSQL,strFormat,res,strType,strSchool,strChartTitle);
    }
    catch (CoeusException ex){
        String strOut = "CoeusException : "+ ex.getMessage();
	ex.printStackTrace(System.out);
        res.setContentType("text/html");
        PrintWriter writer = res.getWriter();
        writer.println("<html><body>");
        writer.println("<h1>Sample Report One</h1><br>");
        writer.println(strOut);
        writer.println("</body></html>");
    }
    catch (DBException ex) {
        String strOut = "DBException : "+ ex.getMessage();
	ex.printStackTrace(System.out);
        res.setContentType("text/html");
        PrintWriter writer = res.getWriter();  
        writer.println("<html><body>"); 
        writer.println("<h1>Sample Report One</h1><br>");
        writer.println(strOut);
        writer.println("</body></html>");
    }
  }

}
