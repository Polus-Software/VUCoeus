/**
 * $Header: /home/cvs/cvsroot/coeus/Source/WEB-INF/classes/edu/umdnj/coeus/reporting/bean/Attic/SpecReviewObject.java,v 1.1.2.2 2007/09/18 18:23:05 cvs Exp $
 * $Log: SpecReviewObject.java,v $
 * Revision 1.1.2.2  2007/09/18 18:23:05  cvs
 * Modify code for GAFA and START_DATE handling
 *
 * Revision 1.1.2.1  2007/02/07 20:19:58  cvs
 * Modifications per Therese recommendations
 *
 *
 */
/*
 * @(#)SpecReviewObject.java 
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
 * Description: Spec Review Object.
 * 
 */

package edu.umdnj.coeus.reporting.bean;


public class SpecReviewObject
{
   public SpecReviewObject() { }

   private String submissiondate;
   private String proposalnumber;
   private String gafanumber;
   private String piname;
   private String projecttitle;
   private String sponsorname;
   private String projectstatus;
   private String specialreviewstatus;
   private String specialreviewdescription;
   private String protocolnumber;
   private String applicationdate;

   public void setSubmissiondate(String val) { submissiondate = val; }
   public void setProposalnumber(String val) { proposalnumber = val; }
   public void setGafanumber(String val) { gafanumber = val; }
   public void setPiname(String val) { piname = val; }
   public void setProjecttitle(String val) { projecttitle = val; }
   public void setSponsorname(String val) { sponsorname = val; }
   public void setProjectstatus(String val) { projectstatus = val; }
   public void setSpecialreviewstatus(String val) { specialreviewstatus = val; }
   public void setSpecialreviewdescription(String val) { specialreviewdescription = val; }
   public void setProtocolnumber(String val) { protocolnumber = val; }
   public void setApplicationdate(String val) { applicationdate = val; }

   public String getSubmissiondate() { return submissiondate; }
   public String getProposalnumber() { return proposalnumber; }
   public String getGafanumber() { return gafanumber; }
   public String getPiname() { return piname; }
   public String getProjecttitle() { return projecttitle; }
   public String getSponsorname() { return sponsorname; }
   public String getProjectstatus() { return projectstatus; }
   public String getSpecialreviewstatus() { return specialreviewstatus; }
   public String getSpecialreviewdescription() { return specialreviewdescription; }
   public String getProtocolnumber() { return protocolnumber; }
   public String getApplicationdate() { return applicationdate; }

   public void addSpecialreviewDescription(String val)
   {
      if (specialreviewdescription == null || specialreviewdescription.length() == 0)
         specialreviewdescription = val;
      else
         specialreviewdescription += ", " + val;
   }
}		  
