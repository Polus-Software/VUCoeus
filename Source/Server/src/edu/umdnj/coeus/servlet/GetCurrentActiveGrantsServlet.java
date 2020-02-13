/**
 * $Header: /home/cvs/cvsroot/coeus/Source/WEB-INF/classes/edu/umdnj/coeus/servlet/Attic/GetCurrentActiveGrantsServlet.java,v 1.1.2.4 2007/08/16 18:11:52 cvs Exp $
 * $Log: GetCurrentActiveGrantsServlet.java,v $
 * Revision 1.1.2.4  2007/08/16 18:11:52  cvs
 * 1. Add support for sorting by PI and Department
 * 2. Move sorting methods to parent class
 *
 * Revision 1.1.2.3  2007/07/25 18:40:10  cvs
 * Add Headers and Snippet integrity in PDF
 *
 * Revision 1.1.2.2  2007/06/18 17:17:53  cvs
 * Add check for Enforce Project End Date
 *
 * Revision 1.1.2.1  2007/03/29 16:23:20  cvs
 * Add support for Current Active Grants Report
 *
 *
 */
/*
 * @(#)GetCurrentActiveGrantsServlet.java 
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
 * Description: Testing Servlet mechanism for UMDNJ COEUS 411.
 * 
 */
package edu.umdnj.coeus.servlet;

import java.io.*;
import java.util.*;
import java.sql.Timestamp;
import javax.servlet.*;
import javax.servlet.http.*;

import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.utils.dbengine.DBEngineImpl;
import edu.umdnj.coeus.reporting.bean.GetCurrentActiveGrantsBean;


/////////////////////////////////////////////////////////////////////
/* GetCurrentActiveGrantsServlet - specific class to get the list
 *
 */
public class GetCurrentActiveGrantsServlet extends HttpServlet
{

  //////////////////////////////////////////////////////////////
  /** doPost
   *  doPost Method to populate report.
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
    String UnitName = req.getParameter("UnitName");
    String Format = req.getParameter("Format");
    String NIH = req.getParameter("NIH");
    String EnforceEndDate = req.getParameter("EnforceEndDate");
    String RepeatHeader = req.getParameter("RepeatHeader");
    String Sort = req.getParameter("Sort");
    Vector result = null;

    GetCurrentActiveGrantsBean bean = new GetCurrentActiveGrantsBean(UnitName);
    bean.setNIH(NIH);
    bean.setEnforceEndDate(EnforceEndDate);
    bean.setRepeatHeader(RepeatHeader);
    bean.setSort(Sort);
    if (Format.compareTo("HTML")==0)
    {
        String strout = bean.getSpecificHTMLOutput();    
        res.setContentType("text/html");
        PrintWriter writer = res.getWriter();
        writer.println("<html><body>");
        writer.println(strout);
        writer.println("</body></html>"); 
    }
    else
    if (Format.compareTo("PDF")==0)
    {
        bean.getSpecificPDFOutput(res);
    }
    else
    if (Format.compareTo("XLS")==0)
    {
        bean.getSpecificXLSOutput(res);
    }
    else
    if (Format.compareTo("CSV")==0)
    {
        String strout = bean.getSpecificCSVOutput();    
        res.setContentType("text/plain");
        PrintWriter writer = res.getWriter();
        writer.println(strout);
    }

    
  }


}