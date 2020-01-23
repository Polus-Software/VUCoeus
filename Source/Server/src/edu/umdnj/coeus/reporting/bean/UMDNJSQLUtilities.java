/**
 * $Header: /home/cvs/cvsroot/coeus/Source/WEB-INF/classes/edu/umdnj/coeus/reporting/bean/Attic/UMDNJSQLUtilities.java,v 1.1.2.28 2007/10/17 18:09:32 cvs Exp $
 * $Log: UMDNJSQLUtilities.java,v $
 * Revision 1.1.2.28  2007/10/17 18:09:32  cvs
 * Server-side support for Monthly Report
 *
 * Revision 1.1.2.27  2007/08/20 15:50:15  cvs
 * Add feature Pending Status Report of Proposals by School
 *
 * Revision 1.1.2.26  2007/08/07 17:55:03  cvs
 * Add Percent Proposal Status Chart
 *
 * Revision 1.1.2.25  2007/08/06 18:59:57  cvs
 * Add Quarterly Reports server-side infrastructure
 *
 * Revision 1.1.2.24  2007/07/31 13:36:19  cvs
 * Add Annual Reports server-side code
 * Move many methods to base class Reporting Base Bean
 *
 * Revision 1.1.2.23  2007/06/21 19:14:46  cvs
 * Add support for IRB and IACUC Grant reports
 *
 * Revision 1.1.2.22  2007/03/29 16:22:44  cvs
 * Add support for Current Active Grants Report
 *
 * Revision 1.1.2.21  2007/03/27 15:19:20  cvs
 * Modify code to work with Award FnA Distributions
 *
 * Revision 1.1.2.20  2007/02/16 18:32:16  cvs
 * Add support for Summary PI Grants for all schools
 *
 * Revision 1.1.2.19  2007/02/06 20:51:48  cvs
 * Add support for Special Review Proposal Reports
 *
 * Revision 1.1.2.18  2007/01/26 17:29:02  cvs
 * Add support for Investigator delimiter in Awards by Investigator
 *
 * Revision 1.1.2.17  2007/01/26 16:36:47  cvs
 * Add support for Awards by Investigators Report
 *
 * Revision 1.1.2.16  2007/01/23 20:45:36  cvs
 * Add support for Graph Activity Types
 *
 * Revision 1.1.2.15  2007/01/23 18:13:12  cvs
 * Add GUI support for Graph Sponsor Types
 *
 * Revision 1.1.2.14  2007/01/23 15:41:17  cvs
 * Add Award Type Grant support
 *
 * Revision 1.1.2.13  2007/01/19 19:33:22  cvs
 * Add Summary Grants support
 *
 * Revision 1.1.2.12  2007/01/18 14:59:05  cvs
 * Add Pending Proposal support
 *
 * Revision 1.1.2.11  2007/01/17 17:19:40  cvs
 * Add NIH support
 *
 * Revision 1.1.2.10  2007/01/17 15:26:35  cvs
 * Add CSV Support
 *
 * Revision 1.1.2.9  2007/01/04 19:09:34  cvs
 * Add support for Sponsor Type Graph by School by Sponsor Type
 *
 * Revision 1.1.2.8  2006/12/28 16:19:35  cvs
 *  Add support for grants by activity type data retrieval
 *
 * Revision 1.1.2.7  2006/12/27 17:52:48  cvs
 *  Add support for grants by sponsor type data retrieval
 *
 * Revision 1.1.2.6  2006/12/27 14:35:16  cvs
 * Extend support for NIH-specific grants
 *
 * Revision 1.1.2.5  2006/12/26 20:07:33  cvs
 * Clean up on System.out.println to server-side java
 *
 * Revision 1.1.2.4  2006/12/26 18:07:03  cvs
 * Added support for GetClosedReport data retrieval
 *
 * Revision 1.1.2.3  2006/12/11 20:51:41  cvs
 * Added support for GetSubmittedProposals retrieval
 *
 * Revision 1.1.2.2  2006/12/08 15:28:22  cvs
 * Add sample report for Number of Proposals by Department
 *
 * Revision 1.1.2.1  2006/11/28 19:51:19  cvs
 * Added support for GetGrantsByDept retrieval
 *
 *
 */
/*
 * @(#)UMDNJSQLUtilities.java 
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
 * Description: Reporting Bean to communicate with Oracle DB.
 * 
 */


package edu.umdnj.coeus.reporting.bean;


import java.util.Vector;

import java.util.HashMap;

import java.util.Hashtable;

import java.sql.Date;

import java.sql.Timestamp;

import javax.servlet.*;

import javax.servlet.http.*;

import javax.xml.transform.TransformerException;

import java.io.*;

import edu.mit.coeus.utils.dbengine.DBEngineImpl;

import edu.mit.coeus.utils.dbengine.DBException;

import edu.mit.coeus.utils.dbengine.Parameter;

import edu.mit.coeus.utils.dbengine.DBEngineConstants;

import edu.mit.coeus.utils.CoeusVector;

import edu.mit.coeus.utils.UtilFactory;

import edu.mit.coeus.exception.CoeusException;

import edu.mit.coeus.budget.report.ReportGenerator;


import org.apache.fop.apps.FOPException;

public class UMDNJSQLUtilities 
{
    private boolean bNIH = false;
    private String UnitName = "";
    private String SponsorType = "";
    private String AwardType = "";
    private String ActivityType = "";
    private String Investigator = "";
    
    public UMDNJSQLUtilities()
    {
    }
    
    public void setNIH(boolean bUse)
    {
        this.bNIH = bUse;
    }
    
    public void setUnitName(String str)
    {
        UnitName = str;
    }
    
    public void setSponsorType(String str)
    {
        SponsorType = str;
    }
    
    public void setAwardType(String str)
    {
        AwardType = str;
    }

    public void setActivityType(String str)
    {
        ActivityType = str;
    }

    public void setInvestigator(String str)
    {
        Investigator = str;
    }

    public Vector getGrantsByDepartment(String UnitName)
    {
        DBEngineImpl dbEngine = new DBEngineImpl();
        Vector result = null;
        Vector param=new Vector();
        try {
            if(dbEngine !=null)
            {
               String proc = "call GET_UMDNJ_GRANTS ( << unitname >> , <<OUT RESULTSET rset>> )";
               if (bNIH == true)
                   proc = "call GET_UMDNJ_NIH_GRANTS ( << unitname >> , <<OUT RESULTSET rset>> )";
               param.addElement(new Parameter("unitname", "String", (String) UnitName));
               result = dbEngine.executeRequest("Coeus",
                      proc,
                      "Coeus", param);
            }
        }
        catch (DBException ex) {
            System.out.println("DBException encountered");
            ex.printStackTrace(System.out);
            return null;
        }
        return result;
    }

    public Vector getAllGrants(String UnitName)
    {
        DBEngineImpl dbEngine = new DBEngineImpl();
        Vector result = null;
        Vector param=new Vector();
        try {
            if(dbEngine !=null)
            {
               String proc = "call GET_UMDNJ_ALL_GRANTS ( << unitname >> , <<OUT RESULTSET rset>> )";
               param.addElement(new Parameter("unitname", "String", (String) UnitName));
               result = dbEngine.executeRequest("Coeus",
                      proc,
                      "Coeus", param);
            }
        }
        catch (DBException ex) {
            System.out.println("DBException encountered");
            ex.printStackTrace(System.out);
            return null;
        }
        return result;
    }
    
    
    public Vector getProposalGrants(String UnitName,
            Timestamp tbegindate,
            Timestamp tenddate)
    {
        DBEngineImpl dbEngine = new DBEngineImpl();
        Vector result = null;
        Vector param=new Vector();

        java.sql.Date begindate = new java.sql.Date(tbegindate.getTime());
        java.sql.Date enddate = new java.sql.Date(tenddate.getTime());
        try {
            if(dbEngine !=null)
            {
               String proc = "call GET_UMDNJ_PROPOSAL_GRANTS ( << unitname >> , << begindate >>, << enddate >>, <<OUT RESULTSET rset>> )";
               param.addElement(new Parameter("unitname", "String", (String) UnitName));
               param.addElement(new Parameter("begindate", "Date", (Date) begindate));
               param.addElement(new Parameter("enddate", "Date", (Date) enddate));
               result = dbEngine.executeRequest("Coeus",
                      proc,
                      "Coeus", param);
            }
        }
        catch (DBException ex) {
            System.out.println("DBException encountered");
            ex.printStackTrace(System.out);
            return null;
        }
        return result;
    }
    
    public Vector getProposalDates(String UnitName,
            Timestamp tbegindate,
            Timestamp tenddate)
    {
        DBEngineImpl dbEngine = new DBEngineImpl();
        Vector result = null;
        Vector param=new Vector();

        java.sql.Date begindate = new java.sql.Date(tbegindate.getTime());
        java.sql.Date enddate = new java.sql.Date(tenddate.getTime());
        try {
            if(dbEngine !=null)
            {
               String proc = "call GET_UMDNJ_PROPOSALS_DATES ( << unitname >> , << begindate >>, << enddate >>, <<OUT RESULTSET rset>> )";
               param.addElement(new Parameter("unitname", "String", (String) UnitName));
               param.addElement(new Parameter("begindate", "Date", (Date) begindate));
               param.addElement(new Parameter("enddate", "Date", (Date) enddate));
               result = dbEngine.executeRequest("Coeus",
                      proc,
                      "Coeus", param);
            }
        }
        catch (DBException ex) {
            System.out.println("DBException encountered");
            ex.printStackTrace(System.out);
            return null;
        }
        return result;
    }
    
    
    public Vector getProposalCountByDepartment()
    {
        DBEngineImpl dbEngine = new DBEngineImpl();
        Vector result = null;
        Vector param=new Vector();
        try {
            if(dbEngine !=null)
            {
               result = dbEngine.executeRequest("Coeus",
                      "call GET_UMDNJ_PROPOSAL_CNT_BY_DEPT (  <<OUT RESULTSET rset>> )",
                      "Coeus", param);
            }
        }
        catch (DBException ex) {
            System.out.println("DBException encountered");
            ex.printStackTrace(System.out);
            return null;
        }
        return result;
    }
    
    public Vector getSubmittedProposals(String SchoolName)
    {
        DBEngineImpl dbEngine = new DBEngineImpl();
        Vector result = null;
        Vector param=new Vector();
        try {
            if(dbEngine !=null)
            {
               param.addElement(new Parameter("schoolname", "String", (String) SchoolName));
               result = dbEngine.executeRequest("Coeus",
                      "call GET_UMDNJ_SUBMITTED_PROPOSALS ( << schoolname >> , <<OUT RESULTSET rset>> )",
                      "Coeus", param);
            }
        }
        catch (DBException ex) {
            System.out.println("DBException encountered");
            ex.printStackTrace(System.out);
            return null;
        }
        return result;
    }
    

    public Vector getCloseReport(String UnitName, java.sql.Date closedate)
    {
        DBEngineImpl dbEngine = new DBEngineImpl();
        Vector result = null;
        Vector param=new Vector();
        try {
            if(dbEngine !=null)
            {
               param.addElement(new Parameter("unitname", "String", (String) UnitName));
               param.addElement(new Parameter("closedate", "Date", (Date) closedate));
               result = dbEngine.executeRequest("Coeus",
                      "call GET_UMDNJ_CLOSED_REPORT ( << unitname >> , << closedate >> , <<OUT RESULTSET rset>> )",
                      "Coeus", param);
            }
        }
        catch (DBException ex) {
            System.out.println("DBException encountered");
            ex.printStackTrace(System.out);
            return null;
        }
        return result;
    }
    
    public Vector getGrantsBySponsor()
    {
        DBEngineImpl dbEngine = new DBEngineImpl();
        Vector result = null;
        Vector param=new Vector();
        try {
            if(dbEngine !=null)
            {

               String proc = "call GET_UMDNJ_SPONSOR_GRANTS ( << unitname >>, <<OUT RESULTSET rset>> )";
               param.addElement(new Parameter("unitname", "String", (String) UnitName));
               if (SponsorType != null && SponsorType.length() > 0)
               {  
                 proc = "call GET_UMDNJ_SPONSOR_GRANTS_2 ( << unitname >>, << sponsorname >>, <<OUT RESULTSET rset>> )";
                 if (bNIH == true)
                    proc = "call GET_UMDNJ_SPONSOR_GRANTS_3 ( << unitname >>, << sponsorname >>, <<OUT RESULTSET rset>> )";
                 param.addElement(new Parameter("sponsorname", "String", (String) SponsorType));
               }
               result = dbEngine.executeRequest("Coeus",
                      proc,
                      "Coeus", param);
            }
        }
        catch (DBException ex) {
            System.out.println("DBException encountered");
            ex.printStackTrace(System.out);
            return null;
        }
        return result;
    }
    
    public Vector getGrantsByActivityType(String UnitName)
    {
        DBEngineImpl dbEngine = new DBEngineImpl();
        Vector result = null;
        Vector param=new Vector();
        try {
            if(dbEngine !=null)
            {
               String proc = "call GET_UMDNJ_ACTIVITY_TYPE_GRANTS ( << unitname >> , <<OUT RESULTSET rset>> )";
               param.addElement(new Parameter("unitname", "String", (String) UnitName));
               if (ActivityType != null && ActivityType.length() > 0)
               {
                   proc= "call GET_UMDNJ_ACTIVITY_GRANTS_2 ( << unitname >> , << descriptionname >>, <<OUT RESULTSET rset>> )";                   
                   param.addElement(new Parameter("descriptionname", "String", (String) ActivityType));
               }
               else
               if (bNIH == true)
                   proc= "call GET_UMDNJ_NIH_ACTIVITY_GRANTS ( << unitname >> , <<OUT RESULTSET rset>> )";
               result = dbEngine.executeRequest("Coeus",
                      proc,
                      "Coeus", param);
            }
        }
        catch (DBException ex) {
            System.out.println("DBException encountered");
            ex.printStackTrace(System.out);
            return null;
        }
        return result;
    }

    public Vector getGrantsByPI(String UnitName)
    {
        DBEngineImpl dbEngine = new DBEngineImpl();
        Vector result = null;
        Vector param=new Vector();
        try {
            if(dbEngine !=null)
            {
               String proc = "call GET_UMDNJ_PI_GRANTS ( << unitname >> , <<OUT RESULTSET rset>> )";
               param.addElement(new Parameter("unitname", "String", (String) UnitName));
               if (Investigator != null && Investigator.length()>0)
               {
                    proc = "call GET_UMDNJ_PI_GRANTS_2 ( << unitname >> , << investigatorid >>, <<OUT RESULTSET rset>> )";
                    param.addElement(new Parameter("investigatorid", "String", (String) Investigator));
               }
               result = dbEngine.executeRequest("Coeus",
                      proc,
                      "Coeus", param);
            }
        }
        catch (DBException ex) {
            System.out.println("DBException encountered");
            ex.printStackTrace(System.out);
            return null;
        }
        return result;
    }
 
    public Vector getPendingProposals(String SchoolName)
    {
        DBEngineImpl dbEngine = new DBEngineImpl();
        Vector result = null;
        Vector param=new Vector();
        try {
            if(dbEngine !=null)
            {
               param.addElement(new Parameter("schoolname", "String", (String) SchoolName));
               result = dbEngine.executeRequest("Coeus",
                      "call GET_UMDNJ_PENDING_PROPOSALS ( << schoolname >> , <<OUT RESULTSET rset>> )",
                      "Coeus", param);
            }
        }
        catch (DBException ex) {
            System.out.println("DBException encountered");
            ex.printStackTrace(System.out);
            return null;
        }
        return result;
    }

    public Vector getPendingStatusReport(String SchoolName)
    {
	return getPendingProposals(SchoolName);
    }

    public Vector getSummaryGrants(String UnitName)
    {
        DBEngineImpl dbEngine = new DBEngineImpl();
        Vector result = null;
        Vector param=new Vector();
        try {
            if(dbEngine !=null)
            {
               String proc = "call GET_UMDNJ_SUMMARY_GRANTS ( << unitname >> , <<OUT RESULTSET rset>> )";
               param.addElement(new Parameter("unitname", "String", (String) UnitName));
               result = dbEngine.executeRequest("Coeus",
                      proc,
                      "Coeus", param);
            }
        }
        catch (DBException ex) {
            System.out.println("DBException encountered");
            ex.printStackTrace(System.out);
            return null;
        }
        return result;
    }
    
    public Vector getGrantsByAwardType(String UnitName)
    {
        DBEngineImpl dbEngine = new DBEngineImpl();
        Vector result = null;
        Vector param=new Vector();
        try {
            if(dbEngine !=null)
            {
               String proc = "call GET_UMDNJ_AWARD_TYPE_GRANTS ( << unitname >> , <<OUT RESULTSET rset>> )";
               param.addElement(new Parameter("unitname", "String", (String) UnitName));
               if (AwardType != null && AwardType.length() > 0)
               {
                   proc= "call GET_UMDNJ_AWARD_TYPE_GRANTS_2 ( << unitname >> , << descriptionname >>, <<OUT RESULTSET rset>> )";
                   param.addElement(new Parameter("descriptionname", "String", (String) AwardType));
               }
               else
               if (bNIH == true)
                   proc= "call GET_UMDNJ_NIH_AWARD_GRANTS ( << unitname >> , <<OUT RESULTSET rset>> )";
               result = dbEngine.executeRequest("Coeus",
                      proc,
                      "Coeus", param);
            }
        }
        catch (DBException ex) {
            System.out.println("DBException encountered");
            ex.printStackTrace(System.out);
            return null;
        }
        return result;
    }
    
    public Vector getSpecReviewProposals(String SchoolName)
    {
        DBEngineImpl dbEngine = new DBEngineImpl();
        Vector result = null;
        Vector param=new Vector();
        try {
            if(dbEngine !=null)
            {
               param.addElement(new Parameter("schoolname", "String", (String) SchoolName));
               result = dbEngine.executeRequest("Coeus",
                      "call GET_UMDNJ_PROPOSALS_SPECREVIEW ( << schoolname >> , <<OUT RESULTSET rset>> )",
                      "Coeus", param);
            }
        }
        catch (DBException ex) {
            System.out.println("DBException encountered");
            ex.printStackTrace(System.out);
            return null;
        }
        return result;
    }
    
    public Vector getGrantsByAllPI()
    {
        DBEngineImpl dbEngine = new DBEngineImpl();
        Vector result = null;
        Vector param=new Vector();
        try {
            if(dbEngine !=null)
            {
               String proc = "call GET_UMDNJ_PI_GRANTS_3 ( <<OUT RESULTSET rset>> )";
               result = dbEngine.executeRequest("Coeus",
                      proc,
                      "Coeus", param);
            }
        }
        catch (DBException ex) {
            System.out.println("DBException encountered");
            ex.printStackTrace(System.out);
            return null;
        }
        return result;
    }
    
    public Vector getCurrentActiveGrants(String UnitName)
    {
        DBEngineImpl dbEngine = new DBEngineImpl();
        Vector result = null;
        Vector param=new Vector();
        try {
            if(dbEngine !=null)
            {
               String proc = "call GET_UMDNJ_CURACTIVE_GRANTS ( << unitname >> , <<OUT RESULTSET rset>> )";
               if (bNIH == true)
                   proc = "call GET_UMDNJ_NIH_CURACTIVE_GRANTS ( << unitname >> , <<OUT RESULTSET rset>> )";
               param.addElement(new Parameter("unitname", "String", (String) UnitName));
               result = dbEngine.executeRequest("Coeus",
                      proc,
                      "Coeus", param);
            }
        }
        catch (DBException ex) {
            System.out.println("DBException encountered");
            ex.printStackTrace(System.out);
            return null;
        }
        return result;
    }

    public Vector getCurrentSpecReviewGrants(
            String UnitName, 
            java.util.Date curdate,
            int specreviewnumber)
    {
        DBEngineImpl dbEngine = new DBEngineImpl();
        Vector result = null;
        Vector param=new Vector();
        
        java.sql.Date aDate = new java.sql.Date(curdate.getTime());
        try {
            if(dbEngine !=null)
            {
               param.addElement(new Parameter("unitname", "String", (String) UnitName));
               param.addElement(new Parameter("curdate", "Date", (Date) aDate));
               param.addElement(new Parameter("specreviewnumber", DBEngineConstants.TYPE_INT, ""+specreviewnumber));
               result = dbEngine.executeRequest("Coeus",
                      "call GET_UMDNJ_SPECREVIEW_GRANTS ( << unitname >> , << curdate >> , << specreviewnumber >>, <<OUT RESULTSET rset>> )",
                      "Coeus", param);
            }
        }
        catch (DBException ex) {
            System.out.println("DBException encountered");
            ex.printStackTrace(System.out);
            return null;
        }
        return result;
    }

    public Vector getUnits(String UnitName)
    {
        DBEngineImpl dbEngine = new DBEngineImpl();
        Vector result = null;
        Vector param=new Vector();
        try {
            if(dbEngine !=null)
            {
               String proc = "call GET_UMDNJ_UNITS ( << unitname >> , <<OUT RESULTSET rset>> )";
               param.addElement(new Parameter("unitname", "String", (String) UnitName));
               result = dbEngine.executeRequest("Coeus",
                      proc,
                      "Coeus", param);
            }
        }
        catch (DBException ex) {
            System.out.println("DBException encountered");
            ex.printStackTrace(System.out);
            return null;
        }
        return result;
    }
    
    public Vector getMonthlyProposals(String UnitName)
    {
        DBEngineImpl dbEngine = new DBEngineImpl();
        Vector result = null;
        Vector param=new Vector();
        try {
            if(dbEngine !=null)
            {
               param.addElement(new Parameter("unitname", "String", (String) UnitName));
               result = dbEngine.executeRequest("Coeus",
                      "call GET_UMDNJ_MONTHLY_PROPOSALS ( << unitname >> , <<OUT RESULTSET rset>> )",
                      "Coeus", param);
            }
        }
        catch (DBException ex) {
            System.out.println("DBException encountered");
            ex.printStackTrace(System.out);
            return null;
        }
        return result;
    }

    public Vector getQueryResults(String UnitName, String SQLProcedure)
    {
        DBEngineImpl dbEngine = new DBEngineImpl();
        Vector result = null;
        Vector param=new Vector();
        try {
            if(dbEngine !=null)
            {
               param.addElement(new Parameter("unitname", "String", (String) UnitName));
               result = dbEngine.executeRequest("Coeus",
                      "call "+SQLProcedure+" ( << unitname >> , <<OUT RESULTSET rset>> )",
                      "Coeus", param);
            }
        }
        catch (DBException ex) {
            System.out.println("DBException encountered");
            ex.printStackTrace(System.out);
            return null;
        }
        return result;
    }

    public Vector getQueryResults(String SQLProcedure)
    {
        DBEngineImpl dbEngine = new DBEngineImpl();
        Vector result = null;
        Vector param=new Vector();
        try {
            if(dbEngine !=null)
            {
               result = dbEngine.executeRequest("Coeus",
                      "call "+SQLProcedure+" ( <<OUT RESULTSET rset>> )",
                      "Coeus", param);
            }
        }
        catch (DBException ex) {
            System.out.println("DBException encountered");
            ex.printStackTrace(System.out);
            return null;
        }
        return result;
    }
}
