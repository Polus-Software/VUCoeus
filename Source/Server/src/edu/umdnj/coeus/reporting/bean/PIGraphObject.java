/**
 * $Header: /home/cvs/cvsroot/coeus/Source/WEB-INF/classes/edu/umdnj/coeus/reporting/bean/Attic/PIGraphObject.java,v 1.1.2.2 2007/02/16 14:57:17 cvs Exp $
 * $Log: PIGraphObject.java,v $
 * Revision 1.1.2.2  2007/02/16 14:57:17  cvs
 * Add drilldown support to PI Grant graphs
 *
 * Revision 1.1.2.1  2007/02/05 20:18:23  cvs
 * Add servlet support for PI Graphs
 *
 *
 */
/*
 * @(#)PIGraphObject.java 
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
 * Description: PI Graph Object.
 * 
 */
package edu.umdnj.coeus.reporting.bean;


public class PIGraphObject
{
   private String piName;
   private double numGrants;
   private double totalCost;
   private String employeeNumber;

   public void setPiName(String val) { piName    = val; }
   public void setNumGrants(double val) { numGrants = val; }
   public void setTotalCost(double val) { totalCost = val; }
   public void setEmployeeNumber(String val) { employeeNumber = val; }

   public String getPiName() { return piName; }
   public double getNumGrants() { return numGrants; }
   public double getTotalCost() { return totalCost; }
   public String getEmployeeNumber() { return employeeNumber; }

   public String toString()
   {
      StringBuffer buf = new StringBuffer("");
      buf.append(getPiName());
         buf.append(" ;; ");
      buf.append(getNumGrants());
         buf.append(" ;; ");
      buf.append(getTotalCost());
         buf.append(" ;; ");
      buf.append(getEmployeeNumber());
      return buf.toString();
   }
}
