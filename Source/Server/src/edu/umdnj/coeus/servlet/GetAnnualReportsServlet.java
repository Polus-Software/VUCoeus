/**
 * $Header: /home/cvs/cvsroot/coeus/Source/WEB-INF/classes/edu/umdnj/coeus/servlet/Attic/GetAnnualReportsServlet.java,v 1.1.2.1 2007/07/31 13:36:28 cvs Exp $
 * $Log: GetAnnualReportsServlet.java,v $
 * Revision 1.1.2.1  2007/07/31 13:36:28  cvs
 * Add Annual Reports server-side code
 * Move many methods to base class Reporting Base Bean
 *
 *
 */
/*
 * @(#)GetAnnualReportsServlet.java 
 * Copyright (c) University of Medicine and Dentistry of New Jersey
 * 1 World's Fair Drive, Somerset, New Jersey 08873
 * All rights reserved.
 * 
 * Author: Romerl Elizes
 * Description: Annual Reports Servlet mechanism for UMDNJ COEUS.
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
import edu.umdnj.coeus.reporting.bean.GetAnnualReportsBean;


/////////////////////////////////////////////////////////////////////
/* GetAnnualReportsServlet - specific class to get the list
 *
 */
public class GetAnnualReportsServlet extends HttpServlet
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
    String FiscalYearType = req.getParameter("FiscalYearType");
    String FiscalYear = req.getParameter("FiscalYear");

    GetAnnualReportsBean bean = new GetAnnualReportsBean(UnitName, FiscalYearType, FiscalYear);
    bean.getSpecificPDFOutput(res);
  }


}