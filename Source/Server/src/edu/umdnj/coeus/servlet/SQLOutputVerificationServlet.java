/**
  $Header: /home/cvs/cvsroot/coeus/Source/WEB-INF/classes/edu/umdnj/coeus/servlet/Attic/SQLOutputVerificationServlet.java,v 1.1.2.4 2006/12/08 19:46:23 cvs Exp $
  $Log: SQLOutputVerificationServlet.java,v $
  Revision 1.1.2.4  2006/12/08 19:46:23  cvs
  Add support for JFreeChart charting engine

  Revision 1.1.2.3  2006/11/28 19:52:18  cvs
  Added support for GetGrantsByDept retrieval

  Revision 1.1.2.2  2006/11/28 16:17:08  cvs
  Added support for TimeStamp and Number class files

  Revision 1.1.2.1  2006/11/21 19:48:02  cvs
  SQL verification servlet to test against COEUS Oracle database

*/
/*
 * @(#)SQLOutputVerificationServlet.java 
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
import edu.umdnj.coeus.reporting.bean.ReportingBean;
import edu.mit.coeus.utils.dbengine.DBEngineImpl;

import edu.umdnj.coeus.reporting.bean.ReportingBaseBean;


public class SQLOutputVerificationServlet extends HttpServlet
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
    Vector result = null;
    ReportingBaseBean pobj= new ReportingBaseBean();

    DBEngineImpl dbEngine = new DBEngineImpl();
    StringBuffer buf = new StringBuffer("");

    Vector param=new Vector();
    Vector vecFields = pobj.getFields(strSQL);

    strSQL = strSQL.replaceAll(";","");
    String strout = "";
    if(dbEngine !=null)
    {
       try {
          result = dbEngine.executeRequest("Coeus", strSQL, "Coeus", param);
       }
       catch (DBException ex) {
          System.out.println("DBException encountered");
          ex.printStackTrace(System.out);

       }
    }
    if (result != null && result.size() > 0)
    {
        buf.append(pobj.getHTMLOutput(result,vecFields));
        res.setContentType("text/html");
        PrintWriter writer = res.getWriter();
        writer.println("<html><body>");
        writer.println(buf.toString());
        writer.println("</body></html>");
    }
    else
    {
        res.setContentType("text/html");
        PrintWriter writer = res.getWriter();
        writer.println("<html><body>");
        writer.println("Nothing came back");
        writer.println("</body></html>");
    }    
  }


}