/**
 * $Header: /home/cvs/cvsroot/coeus/Source/WEB-INF/classes/edu/umdnj/coeus/servlet/Attic/GetGrantsByPIServlet.java,v 1.1.2.2 2007/09/05 19:09:05 cvs Exp $
 * $Log: GetGrantsByPIServlet.java,v $
 * Revision 1.1.2.2  2007/09/05 19:09:05  cvs
 * Server-side modifications for Fiscal Year Delineation
 *
 * Revision 1.1.2.1  2007/01/26 16:35:27  cvs
 * Add support for Awards by Investigators Report
 *
 *
 */
/*
 * @(#)GetGrantsByPIServlet.java 
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
import edu.umdnj.coeus.reporting.bean.GetGrantsByPIBean;


/////////////////////////////////////////////////////////////////////
/* GetGrantsByPIServlet - specific class to get the list
 *
 */
public class GetGrantsByPIServlet extends HttpServlet
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
    String FiscalYear = req.getParameter("FiscalYear");
    String FiscalYearType = req.getParameter("FiscalYearType");
    String Investigator = req.getParameter("Investigator");
    String NIH = req.getParameter("NIH");
    Vector result = null;

    GetGrantsByPIBean bean = new GetGrantsByPIBean(UnitName, FiscalYearType, FiscalYear);
    bean.setNIH(NIH);
    bean.setInvestigator(Investigator);
    
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
