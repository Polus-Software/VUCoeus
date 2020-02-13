
/*
 * @(#) NSFProgAnnBean.java
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 */
package edu.mit.coeus.nsfProgAnn.bean;

import java.io.Serializable;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Map;
import java.util.HashMap;
import java.util.Vector;

import edu.mit.coeus.utils.dbengine.DBEngineImpl;
import edu.mit.coeus.utils.dbengine.ProcReqParameter;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.utils.dbengine.Parameter;

public class NSFProgAnnBean implements Serializable
{   /**
     * Instance of DBEngine.  Use its methods for retrieving and updating data.
     */
    private DBEngineImpl dbEngine;

    /**
     * No argument constructor
     * @throws DBException
     */
    public NSFProgAnnBean()
    {
      dbEngine = new DBEngineImpl();
    }

    /*
    declare arrays for all the attributes (columns) in the osp$nsf_prog_announcement
    table.  Since this is a display-only JSP page, make all attributes Strings
    */
    private String[] progAnnounceID;
    private String[] progAnnounceDesc;
    private String[] effectiveDt;
    private String[] expirationDt;
 //   private String[] divisionCode;
 //   private String[] divisionName;
 //   private String[] programElementCode;
  //  private String[] programName;
  //  private String[] orgUnitCode;

    private int listSize;

    public int getListSize()
    {
      return listSize;
    }

    public String getProgAnnounceID(int index)
    {   return progAnnounceID[index];
    }
 
     // this method is to satisfy the iteration in jsp page
     public String[] getProgAnnounceID()
    {   return progAnnounceID;
    }

     public String[] getProgAnnounceDesc()
    {   return progAnnounceDesc;
    }

    public String getEffectiveDt(int index)
    {   return effectiveDt[index];
    }

    public String getExpirationDt(int index)
    {   return expirationDt[index];
    }
/*
    public String getDivisionCode(int index)
    {   return divisionCode[index];
    }

     public String getDivisionName(int index)
    {   return divisionName[index];
    }

    public String getProgramElementCode(int index)
    {   return programElementCode[index];
    }

    public String getProgramName(int index)
    {   return programName[index];
    }

    public String getOrgUnitCode(int index)
    {   return orgUnitCode[index];
    }
*/
     /**
     * Get list of all program announcements
     * Call stored procedure which accesses OSP$NSF_PROG_ANNOUNCEMENT table
     * Set the bean's attributes with Data
     */
    public void getNSFProgAnnInfo() throws SQLException, Exception
    {
      try
      {
        Vector results = new Vector();
        Vector param = new Vector();
        System.out.println(" before call to stored procedure");
     /* 
        results = dbEngine.executeRequest
            ("Coeus", "call get_nsf_prog_ann ( <<OUT RESULTSET rset>> ) ",
            "Coeus", param);
     */
          results = dbEngine.executeRequest
            ("Coeus", "call get_nsf_prog_ann_gps ( <<OUT RESULTSET rset>> ) ",
            "Coeus", param);
          
        listSize =  results.size();
        System.out.println("No of rows:" + listSize);
        if ( listSize > 0)
        {
           //create all the attribues - before we just declared them
		progAnnounceID = new String[listSize];
              
    		progAnnounceDesc = new String[listSize];
          
    		effectiveDt = new String[listSize];
                
    		expirationDt = new String[listSize];
               
    	//	divisionCode = new String[listSize];
              
    	//	divisionName = new String[listSize];
               
    	//	programElementCode = new String[listSize];
                
    	//	programName = new String[listSize];
                 
    	//	orgUnitCode = new String[listSize];
              
              
            /** the dbengine returns a vector (results) . vector has a row number
            * and an object.  the dbengine always puts a hashtable in the object
            * part of the vector
            * declare a hashtable variable that will hold the vector object
            */
            HashMap listAttributes;
               
            for(int rowCount = 0; rowCount < listSize; rowCount++)
             {
              /**
               * in this loop, get each object from the results vector and save
               *  in the hashtable variable we created
               */
              listAttributes = (HashMap)results.get(rowCount);
              /**
               * hashtable is a key value pair. the key part is always the database
               * column name.
               * get each value and save in the bean attribute
               */
              
              this.progAnnounceID [rowCount] = (String)listAttributes.get("PROGRAM_ANNOUNCE_ID");
              progAnnounceDesc [rowCount] = (String)listAttributes.get("PROGRAM_ANNOUNCE_DESC");
              effectiveDt [rowCount] = (String)listAttributes.get("EFFECTIVE_DATE");
              expirationDt [rowCount] = (String)listAttributes.get("EXPIRATION_DATE");
        //      divisionCode [rowCount] = listAttributes .get("DIVISION_CODE").toString();
        //      divisionName [rowCount] = listAttributes .get("DIVISION_NAME").toString();
        //      programElementCode [rowCount] = listAttributes .get("PROGRAM_ELEMENT_CODE").toString();  
        //      programName [rowCount] = listAttributes .get("PROGRAM_NAME").toString();	
         //     orgUnitCode [rowCount] = listAttributes .get("ORG_UNIT_CODE").toString();
                 
             }
           
     
        }
     }
      catch(Exception e){ e.printStackTrace(); }
     }
    }



