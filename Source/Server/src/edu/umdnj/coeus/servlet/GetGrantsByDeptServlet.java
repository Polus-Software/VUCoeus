/**
 * $Header: /home/cvs/cvsroot/coeus/Source/WEB-INF/classes/edu/umdnj/coeus/servlet/Attic/GetGrantsByDeptServlet.java,v 1.1.2.8 2007/09/05 19:09:05 cvs Exp $
 * $Log: GetGrantsByDeptServlet.java,v $
 * Revision 1.1.2.8  2007/09/05 19:09:05  cvs
 * Server-side modifications for Fiscal Year Delineation
 *
 * Revision 1.1.2.7  2007/02/22 20:05:35  cvs
 * Add support for drilldowns to Reports of Awards by Department
 *
 * Revision 1.1.2.6  2007/01/17 15:25:38  cvs
 * Add CSV Support
 *
 * Revision 1.1.2.5  2006/12/27 14:34:55  cvs
 * Extend support for NIH-specific grants
 *
 * Revision 1.1.2.4  2006/12/05 17:23:38  cvs
 * Add fiscal year filtering for PDF-based SOM model
 *
 * Revision 1.1.2.3  2006/11/30 19:35:42  cvs
 * Add XLS support for formatted output
 *
 * Revision 1.1.2.2  2006/11/30 13:50:16  cvs
 * Add Format parameter to servlet
 *
 * Revision 1.1.2.1  2006/11/28 19:52:18  cvs
 * Added support for GetGrantsByDept retrieval
 *
 *
 */
/*
 * @(#)GetGrantsByDeptServlet.java 
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
import edu.umdnj.coeus.reporting.bean.GetGrantsByDeptBean;


/////////////////////////////////////////////////////////////////////
/* GetGrantsByDeptServlet - specific class to get the list
 *
 */
public class GetGrantsByDeptServlet extends HttpServlet
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
    String NIH = req.getParameter("NIH");
    String DeptName = req.getParameter("DeptName");
    Vector result = null;

    GetGrantsByDeptBean bean = new GetGrantsByDeptBean(UnitName, FiscalYearType, FiscalYear);
    bean.setNIH(NIH);
    bean.setDeptName(DeptName);
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
