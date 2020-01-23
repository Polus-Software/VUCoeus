/**
 * $Header: /home/cvs/cvsroot/coeus/Source/WEB-INF/classes/edu/umdnj/coeus/servlet/Attic/ReportingMaintenanceServlet.java,v 1.1.2.6 2007/10/04 19:00:25 cvs Exp $
 * $Log: ReportingMaintenanceServlet.java,v $
 * Revision 1.1.2.6  2007/10/04 19:00:25  cvs
 * If vector input is null, set it as empty vector to prevent NullPointerExceptions
 *
 * Revision 1.1.2.5  2007/06/20 18:46:08  cvs
 * Add database retrieval for Sponsor Type Description
 *
 * Revision 1.1.2.4  2007/03/22 16:10:35  cvs
 * Add support for retrieving Proposal Number by Institution Number
 *
 * Revision 1.1.2.3  2007/02/13 17:44:34  cvs
 * Add Proposal Retrieval Support based on GAFA Number
 *
 * Revision 1.1.2.2  2007/01/30 20:22:04  cvs
 * Add servlet and GUI support for PI delimiter
 *
 * Revision 1.1.2.1  2007/01/29 20:44:10  cvs
 * Add servlet support for PI delimiter
 *
 *
 */
/*
 * @(#)ReportingMaintenanceServlet.java 
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
 * Description: Reporting Maintenance Servlet mechanism for UMDNJ COEUS 411.
 * 
 */
package edu.umdnj.coeus.servlet;


import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.bean.*;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.utils.TypeConstants;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.moduleparameters.bean.ParameterBean;
import edu.mit.coeus.utils.CoeusConstants;
import edu.mit.coeus.utils.KeyConstants;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.award.bean.*;
import edu.mit.coeus.utils.CoeusFunctions;
import edu.mit.coeus.utils.query.Equals;
import edu.mit.coeus.utils.query.And;
import edu.mit.coeus.utils.DateUtils;
import edu.mit.coeus.utils.ObjectCloner;
import edu.mit.coeus.utils.query.NotEquals;
import edu.mit.coeus.user.bean.UserMaintDataTxnBean;
import edu.mit.coeus.utils.locking.LockingBean;
import edu.mit.coeus.utils.locking.LockingException;
import edu.umdnj.coeus.reporting.bean.ReportingTxnBean;
import edu.mit.coeus.servlet.CoeusBaseServlet;
import edu.mit.coeus.utils.CoeusVector;

import java.io.*;
import java.net.*;
import java.util.Hashtable;
import java.util.HashMap;
import java.util.Vector;
import javax.servlet.*;
import javax.servlet.http.*;

public class ReportingMaintenanceServlet extends CoeusBaseServlet implements TypeConstants
{
	
  private static final char GET_PRIMARY_INVESTIGATORS = 'A';
  private static final char GET_SPONSOR_TYPE_DESCRIPTIONS = 'D';
  private static final char GET_UNIT_HIERARCHY = 'E';
	        
  //////////////////////////////////////////////////////////////
  /** doPost
   *  doPost Method to handle maintenance issues.
   *  @param req - HttpServletRequest object
   *  @param res - HttpServletResponse object
   *  @exception ServletException
   *  @exception IOException
   */
  public void doPost(HttpServletRequest req, HttpServletResponse res)
         throws ServletException, IOException
  {
    generateReportingMaintenance(req,res);
  }

  //////////////////////////////////////////////////////////////
  /** doGet
   *  doGet Method to handle maintenance issues.
   *  @param req - HttpServletRequest object
   *  @param res - HttpServletResponse object
   *  @exception ServletException
   *  @exception IOException
   */
  public void doGet(HttpServletRequest req, HttpServletResponse res)
         throws ServletException, IOException
  {
    generateReportingMaintenance(req,res);
  }
	
  //////////////////////////////////////////////////////////////
  /** generateReportingMaintenance
   *  generateReportingMaintenance - Handles the maintenance method.
   *  @param req - servlet request
   *  @param res  - servlet res
   *  @exception ServletException
   *  @exception IOException
   */
  public void generateReportingMaintenance(HttpServletRequest req, HttpServletResponse res)
         throws ServletException, IOException 
  {
     String UnitName = req.getParameter("UnitName");
     String GAFANumber = req.getParameter("GAFANumber");
     String InstitutionNumber = req.getParameter("InstitutionNumber");

     // the request object from applet
     RequesterBean requester = null;

     // the response object to applet
     ResponderBean responder = new ResponderBean();
		
     // open object input/output streams
     ObjectInputStream inputFromApplet = null;
     ObjectOutputStream outputToApplet = null;
		
     String loggedinUser;
     String unitNumber;
		
     try 
     {
        // get an input stream
        inputFromApplet = new ObjectInputStream(req.getInputStream());
        // read the serialized request object from applet
        requester = (RequesterBean) inputFromApplet.readObject();

        loggedinUser = requester.getUserName();

        // get the user
        UserInfoBean userBean = (UserInfoBean)new
        UserDetailsBean().getUserInfo(requester.getUserName());

        unitNumber = userBean.getUnitNumber();
        // keep all the beans into vector
        Vector dataObjects = new Vector();

        char functionType = requester.getFunctionType();
			
        String awardNumber="";
        int sequenceNumber;
        if (functionType == GET_PRIMARY_INVESTIGATORS)
        {
           Hashtable htPI = new Hashtable();
           UserMaintDataTxnBean userMaintTxnBean = new UserMaintDataTxnBean();
           boolean hasRight = userMaintTxnBean.getUserHasOSPRight(loggedinUser, "MAINTAIN_CODE_TABLES");
           ReportingTxnBean reportingTxnBean = new ReportingTxnBean(UnitName);
           CoeusVector cvUMDNJPI = reportingTxnBean.getUMDNJPIs();
           CoeusVector cvNonUMDNJPI = reportingTxnBean.getNonUMDNJPIs();
           // Make an empty vector if cvNonUMDNJPI is null
           if (cvUMDNJPI == null) cvUMDNJPI = new CoeusVector();
           if (cvNonUMDNJPI == null) cvNonUMDNJPI = new CoeusVector();
           htPI.put(new Integer(0), cvUMDNJPI);
           htPI.put(new Integer(1), cvNonUMDNJPI);
           htPI.put(new Integer(2), new Boolean(hasRight));
           responder.setDataObject(htPI);
           responder.setResponseStatus(true);
           responder.setMessage(null);
        }
        else
        if (functionType == GET_SPONSOR_TYPE_DESCRIPTIONS)
        {
           Hashtable htSponsorType = new Hashtable();
           UserMaintDataTxnBean userMaintTxnBean = new UserMaintDataTxnBean();
           boolean hasRight = userMaintTxnBean.getUserHasOSPRight(loggedinUser, "MAINTAIN_CODE_TABLES");
           ReportingTxnBean reportingTxnBean = new ReportingTxnBean("");
           CoeusVector cvSponsorType = reportingTxnBean.getSponsorTypeDescriptions();
           // Make an empty vector if cvSponsorType is null
           if (cvSponsorType == null) cvSponsorType = new CoeusVector();
           htSponsorType.put(new Integer(0), cvSponsorType);
           htSponsorType.put(new Integer(1), new Boolean(hasRight));
           responder.setDataObject(htSponsorType);
           responder.setResponseStatus(true);
           responder.setMessage(null);
        }
        else
        if (functionType == GET_UNIT_HIERARCHY)
        {
           Hashtable htUnitHierarchy = new Hashtable();
           UserMaintDataTxnBean userMaintTxnBean = new UserMaintDataTxnBean();
           boolean hasRight = userMaintTxnBean.getUserHasOSPRight(loggedinUser, "MAINTAIN_CODE_TABLES");
           ReportingTxnBean reportingTxnBean = new ReportingTxnBean("");
           CoeusVector cvUnitHierarchy = reportingTxnBean.getUnitHierarchy();
           // Make an empty vector if cvUnitHierarchy is null
           if (cvUnitHierarchy == null) cvUnitHierarchy = new CoeusVector();
           htUnitHierarchy.put(new Integer(0), cvUnitHierarchy);
           htUnitHierarchy.put(new Integer(1), new Boolean(hasRight));
           responder.setDataObject(htUnitHierarchy);
           responder.setResponseStatus(true);
           responder.setMessage(null);
        }
     }
     catch(LockingException lockEx) 
     {
        lockEx.printStackTrace();
        LockingBean lockingBean = lockEx.getLockingBean();
        String errMsg = lockEx.getErrorMessage();
        CoeusMessageResourcesBean coeusMessageResourcesBean=new CoeusMessageResourcesBean();
        errMsg= coeusMessageResourcesBean.parseMessageKey(errMsg);
        responder.setException(lockEx);
        responder.setResponseStatus(false);
        responder.setMessage(errMsg);
        UtilFactory.log( errMsg, lockEx,"ReportingMaintenanceServlet", "doPost");
     } 
     catch( CoeusException coeusEx ) 
     {
        coeusEx.printStackTrace();
        int index=0;
        String errMsg;
        if (coeusEx.getErrorId()==999999)
        {
           errMsg = "dbEngine_intlErr_exceptionCode.1028";
           responder.setLocked(true);
        }
        else
        {
           errMsg = coeusEx.getMessage();
        }
        CoeusMessageResourcesBean coeusMessageResourcesBean=new CoeusMessageResourcesBean();
        errMsg= coeusMessageResourcesBean.parseMessageKey(errMsg);
        responder.setResponseStatus(false);
        responder.setException(coeusEx);
        responder.setMessage(errMsg);
        UtilFactory.log( errMsg, coeusEx,"ReportingMaintenanceServlet", "doPost");	
     }
     catch( DBException dbEx ) 
     {
        dbEx.printStackTrace();
        int index=0;
        String errMsg = dbEx.getUserMessage();
        if (dbEx.getErrorId() == 20102 ) 
        {
           errMsg = "dbEngine_intlErr_exceptionCode.1028";
        }
        if (errMsg.equals("db_exceptionCode.1111")) 
        {
           responder.setCloseRequired(true);
        }
        CoeusMessageResourcesBean coeusMessageResourcesBean = new CoeusMessageResourcesBean();
        errMsg= coeusMessageResourcesBean.parseMessageKey(errMsg);
	
        responder.setResponseStatus(false);
        responder.setException(dbEx);
        responder.setMessage(errMsg);
        UtilFactory.log( errMsg, dbEx, "ReportingMaintenanceServlet", "doPost");
			
     }
     catch(Exception e) 
     {
        e.printStackTrace();
        responder.setResponseStatus(false);
        responder.setException(e);
        responder.setMessage(e.getMessage());
        UtilFactory.log( e.getMessage(), e,"ReportingMaintenanceServlet", "doPost");
     } 
     finally 
     {
        try
        {
	  outputToApplet = new ObjectOutputStream(res.getOutputStream());
          outputToApplet.writeObject(responder);
          // close the streams
          if (inputFromApplet!=null)
          {
             inputFromApplet.close();
          }
          if (outputToApplet!=null)
          {
             outputToApplet.flush();
             outputToApplet.close();
          }
       }
       catch (IOException ioe)
       {
          UtilFactory.log( ioe.getMessage(), ioe, "ReportingMaintenanceServlet", "doPost");
       }
    }
  }
	
}
