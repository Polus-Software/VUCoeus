/**
 * $Header: /home/cvs/cvsroot/coeus/Source/WEB-INF/classes/edu/umdnj/coeus/reporting/bean/Attic/ReportingTxnBean.java,v 1.1.2.3 2007/06/20 18:46:01 cvs Exp $
 * $Log: ReportingTxnBean.java,v $
 * Revision 1.1.2.3  2007/06/20 18:46:01  cvs
 * Add database retrieval for Sponsor Type Description
 *
 * Revision 1.1.2.2  2007/01/30 20:22:45  cvs
 * Add servlet and GUI support for PI delimiter
 *
 * Revision 1.1.2.1  2007/01/29 20:45:21  cvs
 * Add servlet support for PI delimiter
 *
 *
 *
 */
/*
 * @(#)ReportingTxnBean.java 
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
 * Description: Bean for data retrieval and population to GUI.
 * 
 */
package edu.umdnj.coeus.reporting.bean;

import edu.mit.coeus.utils.dbengine.DBEngineImpl;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.utils.dbengine.Parameter;
import edu.mit.coeus.utils.dbengine.Parameter;
import edu.mit.coeus.utils.dbengine.ProcReqParameter;
import edu.mit.coeus.utils.dbengine.DBEngineConstants;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.utils.CoeusFunctions;

import edu.umdnj.coeus.reporting.bean.PIBean;
import edu.umdnj.coeus.reporting.bean.UnitHierarchyBean;

import java.util.Vector;
import java.util.HashMap;
import java.util.Hashtable;
import java.sql.Date;
import java.sql.Timestamp;

public class ReportingTxnBean 
{
    
   // Instance of a dbEngine
   private DBEngineImpl dbEngine; 
    
   private static final String DSN = "Coeus";
    
   private String UnitName;
    
   /** Creates a new instance of ReportingTxnBean */
   public ReportingTxnBean() 
   {
      dbEngine = new DBEngineImpl();
   }
    
   public ReportingTxnBean(String UnitName) 
   {
      dbEngine = new DBEngineImpl();
      this.UnitName = UnitName;
   }
    
  //////////////////////////////////////////////////////////////
  /**
   * This method is used to get the UMDNJ primary investigators.
   * The stored procedure used is GET_UMDNJ_PI
   * @param UnitName - Unit Name in Acronym format
   * @return CoeusVector
   * @exception DBException if any error during database transaction.
   * @exception CoeusException if the instance of dbEngine is not available.
   */
   public CoeusVector getUMDNJPIs()
         throws CoeusException, DBException
   {
        if (UnitName == null || UnitName.length()==0)
        {
            System.out.println("getUMDNJPIs: UnitName is null.");
            return null;
        }
        Vector result = new Vector();
        Vector param= new Vector();
        HashMap row = null;
        PIBean piBean = null;
        CoeusVector cvPI = null;
        
        if(dbEngine!=null)
        {
            param.addElement(new Parameter("unitname", "String", (String) UnitName));
            result = dbEngine.executeRequest("Coeus",
            "call GET_UMDNJ_PI ( << unitname >>, <<OUT RESULTSET rset>> )",
            "Coeus", param);
        }
        else
        {
            throw new CoeusException("db_exceptionCode.1000");
        }
        int listSize = result.size();
        if(listSize > 0)
        {
            cvPI = new CoeusVector();
            for(int index = 0; index < listSize; index++)
            {
                piBean = new PIBean();
                row = (HashMap)result.elementAt(index);
                piBean.setPersonID((String)row.get("PERSON_ID"));
                piBean.setLastName((String)row.get("LAST_NAME"));
                piBean.setFirstName((String)row.get("FIRST_NAME"));
                piBean.setFullName((String)row.get("FULL_NAME"));
                cvPI.addElement(piBean);
            }
        }
        return cvPI;
    }    
    

  //////////////////////////////////////////////////////////////
  /**
   * This method is used to get the Non-UMDNJ primary investigators.
   * The stored procedure used is GET_UMDNJ_ROLODEX_PI
   * @param UnitName - Unit Name in Acronym format
   * @return CoeusVector
   * @exception DBException if any error during database transaction.
   * @exception CoeusException if the instance of dbEngine is not available.
   */
  public CoeusVector getNonUMDNJPIs()
     throws CoeusException, DBException
   {
        if (UnitName == null || UnitName.length()==0)
        {
            System.out.println("getNonUMDNJPIs: UnitName is null.");
            return null;
        }
        Vector result = new Vector();
        Vector param= new Vector();
        HashMap row = null;
        PIBean piBean = null;
        CoeusVector cvPI = null;
        
        if(dbEngine!=null)
        {
            param.addElement(new Parameter("unitname", "String", (String) UnitName));
            result = dbEngine.executeRequest("Coeus",
            "call GET_UMDNJ_ROLODEX_PI (  << unitname >>, <<OUT RESULTSET rset>> )",
            "Coeus", param);
        }
        else
        {
            throw new CoeusException("db_exceptionCode.1000");
        }
        int listSize = result.size();
        if(listSize > 0)
        {
            cvPI = new CoeusVector();
            for(int index = 0; index < listSize; index++)
            {
                piBean = new PIBean();
                row = (HashMap)result.elementAt(index);
                int iRolodexID = Integer.parseInt(row.get("ROLODEX_ID").toString());
                piBean.setPersonID(""+iRolodexID);
                piBean.setLastName((String)row.get("LAST_NAME"));
                piBean.setFirstName((String)row.get("FIRST_NAME"));
                piBean.setFullName((String)row.get("PERSON_NAME"));
                cvPI.addElement(piBean);
            }
        }
        return cvPI;
    }    

  //////////////////////////////////////////////////////////////
  /**
   * This method is used to get the UMDNJ sponsor type descriptions.
   * The stored procedure used is DW_GET_SPONSOR_TYPE
   * @return CoeusVector
   * @exception DBException if any error during database transaction.
   * @exception CoeusException if the instance of dbEngine is not available.
   */
   public CoeusVector getSponsorTypeDescriptions()
         throws CoeusException, DBException
   {
        Vector result = new Vector();
        Vector param= new Vector();
        HashMap row = null;
        CoeusVector cvSponsorType = null;
        
        if(dbEngine!=null)
        {
            result = dbEngine.executeRequest("Coeus",
            "call DW_GET_SPONSOR_TYPE (  <<OUT RESULTSET rset>> )",
            "Coeus", param);
        }
        else
        {
            throw new CoeusException("db_exceptionCode.1000");
        }
        int listSize = result.size();
        if(listSize > 0)
        {
            cvSponsorType = new CoeusVector();
            for(int index = 0; index < listSize; index++)
            {
                row = (HashMap)result.elementAt(index);
                String strcand = (String)row.get("DESCRIPTION");
                cvSponsorType.addElement(strcand);
            }
        }
        return cvSponsorType;
    }    


  //////////////////////////////////////////////////////////////
  /**
   * This method is used to get Unit Hierarchy information.
   * The stored procedure used is GET_UNI_HIERARCHY
   * @return CoeusVector
   * @exception DBException if any error during database transaction.
   * @exception CoeusException if the instance of dbEngine is not available.
   */
   public CoeusVector getUnitHierarchy()
         throws CoeusException, DBException
   {
        Vector result = new Vector();
        Vector param= new Vector();
        HashMap row = null;
        UnitHierarchyBean uhBean = null;
        CoeusVector cvUnitHierarchy = null;
        
        if(dbEngine!=null)
        {
            result = dbEngine.executeRequest("Coeus",
            "call GET_UNIT_HIERARCHY (  <<OUT RESULTSET rset>> )",
            "Coeus", param);
        }
        else
        {
            throw new CoeusException("db_exceptionCode.1000");
        }
        int listSize = result.size();
        if(listSize > 0)
        {
            cvUnitHierarchy = new CoeusVector();
            for(int index = 0; index < listSize; index++)
            {
                uhBean = new UnitHierarchyBean();
                row = (HashMap)result.elementAt(index);
                uhBean.setUnitNumber((String)row.get("UNIT_NUMBER"));
                uhBean.setParentUnitNumber((String)row.get("PARENT_UNIT_NUMBER"));
                uhBean.setHasChildren((String)row.get("HAS_CHILDREN_FLAG"));
                uhBean.setUnitName((String)row.get("UNIT_NAME"));
                cvUnitHierarchy.addElement(uhBean);
            }
        }
        return cvUnitHierarchy;
    }    

}

