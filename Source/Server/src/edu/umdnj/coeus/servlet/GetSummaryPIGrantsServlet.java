/**
 * $Header: /home/cvs/cvsroot/coeus/Source/WEB-INF/classes/edu/umdnj/coeus/servlet/Attic/GetSummaryPIGrantsServlet.java,v 1.1.2.2 2007/02/08 18:14:23 cvs Exp $
 * $Log: GetSummaryPIGrantsServlet.java,v $
 * Revision 1.1.2.2  2007/02/08 18:14:23  cvs
 * Add Support for Top 10 functionality
 *
 * Revision 1.1.2.1  2007/02/08 16:14:48  cvs
 * Add Summary PI Grant functionality
 *
 *
 */
/*
 * @(#)GetSummaryPIGrantsServlet.java 
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
import edu.umdnj.coeus.reporting.bean.GetSummaryPIGrantsBean;


/////////////////////////////////////////////////////////////////////
/* GetSummaryPIGrantsServlet - specific class to get the list
 *
 */
public class GetSummaryPIGrantsServlet extends HttpServlet
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
    String Investigator = req.getParameter("Investigator");
    String NIH = req.getParameter("NIH");
    String Sort = req.getParameter("Sort");
    Vector result = null;

    if (Format == null || Format.length() == 0)
       Format = "PNG";
    
    GetSummaryPIGrantsBean bean = new GetSummaryPIGrantsBean(UnitName, FiscalYear);
    bean.setFormat(Format);
    bean.setNIH(NIH);
    bean.setInvestigator(Investigator);
    bean.setSort(Sort);
    
    bean.getOutput(res);
    
  }


}