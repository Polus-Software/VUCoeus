/**
  $Header: /home/cvs/cvsroot/coeus/Source/WEB-INF/classes/edu/umdnj/coeus/servlet/Attic/ReportingServlet.java,v 1.1.2.8 2007/02/21 20:44:43 cvs Exp $
  $Log: ReportingServlet.java,v $
  Revision 1.1.2.8  2007/02/21 20:44:43  cvs
  Add id tags for JUnit Testing

  Revision 1.1.2.7  2007/01/17 15:25:38  cvs
  Add CSV Support

  Revision 1.1.2.6  2006/12/07 15:16:23  cvs
  Add second sample report and clean up code

  Revision 1.1.2.5  2006/12/01 14:15:03  cvs
  Add XLS support for formatted output to Canned Reports dialog

  Revision 1.1.2.4  2006/11/20 14:17:25  cvs
  Subclass ReportingBaseBean to two child classes for easy inheritance of methods

  Revision 1.1.2.3  2006/11/17 20:16:03  cvs
  Add basic PDF data retrieval and browser output mechanism

  Revision 1.1.2.2  2006/11/16 18:55:56  cvs
  Add basic XML and HTML data retrieval and browser output mechanism

  Revision 1.1.2.1  2006/11/15 20:27:58  cvs
  Add ReportingServlet and GraphingServlet servlet objects for COEUS application

*/
/*
 * @(#)ReportingServlet.java 
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
 * Description: Reporting Servlet mechanism for UMDNJ COEUS 411.
 * 
 */
package edu.umdnj.coeus.servlet;

import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.umdnj.coeus.reporting.bean.ReportingBean;

public class ReportingServlet extends HttpServlet
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
    generateReport(req,res);
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
    generateReport(req,res);
  }

  //////////////////////////////////////////////////////////////
  /** generateReport
   *  Method that generates Report.
   *  @param req - HttpServletRequest object
   *  @param res - HttpServletResponse object
   *  @exception ServletException
   *  @exception IOException
   */
  public void generateReport(HttpServletRequest req, HttpServletResponse res)
         throws ServletException, IOException
  {
    String strSQL = req.getParameter("SQLRequest");
    String strFormat = req.getParameter("Format");
    ReportingBean obj = new ReportingBean();
       String strOut = "";
       try {
	if (strFormat.compareTo("HTML")==0)
        {
            strOut = obj.getSampleReport(strSQL,strFormat);
            res.setContentType("text/html");
            PrintWriter writer = res.getWriter();
            writer.println("<html><body>");
            writer.println("<h1 id=\"headertitle1\">Sample Report One</h1><br>");
            writer.println(strOut);
            writer.println("</body></html>");
        }
        else
        if (strFormat.compareTo("XML")==0)
        {
            strOut = obj.getSampleReport(strSQL,strFormat);
            res.setContentType("text/xml");
            PrintWriter writer = res.getWriter();
            writer.println(strOut);
        }
        else
        if (strFormat.compareTo("CSV")==0)
        {
            strOut = obj.getSampleReport(strSQL,strFormat);
            res.setContentType("text/plain");
            PrintWriter writer = res.getWriter();
            writer.println(strOut);
        }
        else
        if (strFormat.compareTo("PDF")==0)
           obj.SampleReportToPDF(strSQL,res);
        else
        if (strFormat.compareTo("XLS")==0)
            obj.SampleReportToXLS(strSQL,res);
           
      }
       catch (CoeusException ex){
           strOut = "CoeusException : "+ ex.getMessage();
	   ex.printStackTrace(System.out);
           res.setContentType("text/html");
           PrintWriter writer = res.getWriter();
           writer.println("<html><body>");
           writer.println("<h1>Sample Report One</h1><br>");
           writer.println(strOut);
           writer.println("</body></html>");
       }
       catch (DBException ex) {
           strOut = "DBException : "+ ex.getMessage();
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